package org.Main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    public static String website = "https://www.gismeteo.com";

    private static Document getMonthPage() throws IOException {
        Document page = Jsoup.parse(new URL(website + "/weather-timisoara-3370/month/"), 3000);
        return page;
    }

    private static Document getDayPage(String dayLink) throws IOException {
        return Jsoup.parse(new URL(dayLink), 50000);
    }
    public static void InitilizeCalendar(List<Day> DayList) throws IOException {
        Elements DayInfo = getMonthPage()
                .select("div[class=widget-body]")
                .select("a[class=row-item]");
        StringBuilder m = new StringBuilder();

        for (Element e : DayInfo) {
            //Extract [0] - date(obligatory), [1] - month(Optional)
            String[] date = e.select("div[class~=date item-day-\\d+]").text().split(" ");

            //Checks if the month exists; if not, add it like before one. If the month exists, update it.
            if (date.length == 2) m = new StringBuilder(date[1]);

            //Extract min and max Temperature for each day
            String maxTemp = e.select("div[class=temp]").select("div[class=maxt]").select("span[class=unit unit_temperature_c]").text();
            String minTemp = e.select("div[class=temp]").select("div[class=mint]").select("span[class=unit unit_temperature_c]").text();

            int day = Integer.parseInt(date[0]);
            int month = convertToAbbreviation(m.toString().toUpperCase().trim());
            LocalDate data = LocalDate.of(LocalDate.now().getYear(), month, day);

            //If the "href" attribute is present, extract and add them to an object.
            // If the link exists, it is likely to provide detailed information about the watch.
            Element l = e.selectFirst("a.row-item[href]");
            String link = null;
            if (l != null) {
                link = l.attr("href").toString();
                DayList.add(new Day(data, maxTemp, minTemp, website + link)); //If link exist
            } else
                DayList.add(new Day(data, maxTemp, minTemp)); //If the link does not exist
        }
    }

    private static List<Day> DayList = new ArrayList<>(); // main collection with Calendar Data

    public static void main(String[] args) throws Exception {
        InitilizeCalendar(DayList);
        //DayList.stream().forEach(System.out::println);

        menu();
    }

    public static void menu() throws IOException {
        Scanner scan = new Scanner(System.in);
        String opt = null;
        while (true) {
            System.out.println("1. Watch temperature in a specific day : ");
            System.out.println("0. Exit");

            System.out.print("Select Option : ");
            opt = scan.nextLine();

            switch (opt) {
                case "1":
                    int ok;
                    int a;
                    do {
                        ok = 1;
                        System.out.print("Insert date : ");
                        a = Integer.parseInt(scan.nextLine());
                        if (a < 1 || a > 31) ok = 0;
                    } while (ok == 0);

                    System.out.print("Insert month : ");
                    String b = scan.nextLine().toUpperCase().trim();
                    int month = convertToAbbreviation(b);

                    LocalDate data = LocalDate.of(LocalDate.now().getYear(), month, a);
                    OnlyTemperature(data);
                    break;

                case "0":
                    break;
                default:
                    System.out.println("Inexistent Option - Try Another One ?");
                    break;
            }
        }
    }

    public static int convertToAbbreviation(String input) {
        switch (input.toUpperCase()) {
            case "JAN":
            case "JANUARY":
            case "1":
                return 1;
            case "FEB":
            case "FEBRUARY":
            case "2":
                return 2;
            case "MAR":
            case "MARCH":
            case "3":
                return 3;
            case "APR":
            case "APRIL":
            case "4":
                return 4;
            case "MAY":
            case "5":
                return 5;
            case "JUN":
            case "JUNE":
            case "6":
                return 6;
            case "JUL":
            case "JULY":
            case "7":
                return 7;
            case "AUG":
            case "AUGUST":
            case "8":
                return 8;
            case "SEP":
            case "SEPTEMBER":
            case "9":
                return 9;
            case "OCT":
            case "OCTOBER":
            case "10":
                return 10;
            case "NOV":
            case "NOVEMBER":
            case "11":
                return 11;
            case "DEC":
            case "DECEMBER":
            case "12":
                return 12;
            default:
                return 0;
        }
    }

    public static void OnlyTemperature(LocalDate date) throws IOException {
        Day day = DayList.stream()
                .filter(a -> a.getDate().equals(date))
                .findFirst()
                .orElse(null);

        if (day != null && day.getLink() != null) {
            InfoDay SpecificDay = DayInfo(day);

            System.out.println("Date  : " + SpecificDay.getDate());
            System.out.println("Max T : " + SpecificDay.getMaxTemp());
            System.out.println("Min T : " + SpecificDay.getMinTemp());

            SpecificDay.getHours().forEach(a -> System.out.printf("%4s", a));
            System.out.println();
            SpecificDay.getTemp().forEach(a -> System.out.printf("%4s", a));
            System.out.println();
            SpecificDay.getWind().forEach(a -> System.out.printf("%4s", a));
            System.out.println();
            SpecificDay.getPrec().forEach(a -> System.out.printf("%4s", a));
            System.out.println();

            System.out.println(SpecificDay.getLink());

        } else if (day != null){
            System.out.println("Date  : " + day.getDate());
            System.out.println("Max T : " + day.getMaxTemp());
            System.out.println("Min T : " + day.getMinTemp());
            System.out.println("Only these information are avaible for current time");
        } else
            System.out.println("There are no information");
    }

    public static InfoDay DayInfo(Day day) throws IOException {
        Element tableInfo = getDayPage(day.getLink()).select("div[class=widget widget-weather-parameters widget-oneday]").first();

        //Hours
        Elements hours = tableInfo.select("div[class=widget-row widget-row-time]").select("span");
        List<String> hoursList = new ArrayList<>();
        for (Element e : hours) {
            String b = e.text();
            b = b.substring(0, b.length() - 2);
            hoursList.add(b);
        }

        //Temperature
        Elements temp = tableInfo.select("div[class=values]").select("span[class=unit unit_temperature_c]");
        List<String> tempList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            tempList.add(temp.get(i).ownText());
        }

        //Wind
        Elements wind = tableInfo.select("div[class=widget-row widget-row-wind-gust row-with-caption]").select("div[class=row-item]");
        wind = wind.select("span");
        wind.removeIf(a -> a.hasClass("unit_wind_km_h"));
        List<String> windList = wind.stream().map(Element::text).collect(Collectors.toList());

        //Precipitations
        Elements prec = tableInfo.select("div[class=widget-row widget-row-precipitation-bars row-with-caption]")
                .select("div[class=row-item]");
        List<String> precList = prec.stream().map(Element::text).collect(Collectors.toList());

        return new InfoDay(day.getDate(), day.MaxTemp, day.MinTemp, day.link, hoursList, tempList, windList, precList);
=======
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}