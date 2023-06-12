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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Days {
    int date;
    String month;
    String link;

    public Days(int date, String month, String link) {
        this.date = date;
        this.month = month;
        this.link = link;
    }

    public Days(int date, String month) {
        this.date = date;
        this.month = month;

        this.link = null;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Days{" +
                "date=" + date +
                ", month='" + month + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}

class InfoDay {
    private String date;
    private List<String> hours = new ArrayList<>(8);
    private List<String> temp = new ArrayList<>(8);
    private List<String> wind = new ArrayList<>(8);

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getHours() {
        return hours;
    }

    public void setHours(List<String> hours) {
        this.hours = hours;
    }

    public List<String> getTemp() {
        return temp;
    }

    public void setTemp(List<String> temp) {
        this.temp = temp;
    }

    public List<String> getWind() {
        return wind;
    }

    public void setWind(List<String> wind) {
        this.wind = wind;
    }

    public InfoDay(String date, List<String> hours, List<String> temp, List<String> wind) {
        this.date = date;
        this.hours = hours;
        this.temp = temp;
        this.wind = wind;
    }

    @Override
    public String toString() {
        return "InfoDay{" + "date='" + date + '\'' + ", hours=" + hours + ", temp=" + temp + ", wind=" + wind + '}';
    }
}

public class Main {

    public static String website = "https://www.gismeteo.com";

    private static Document getMonthPage() throws IOException {
        Document page = Jsoup.parse(new URL(website + "/weather-timisoara-3370/month/"), 3000);
        return page;
    }
    private static Document getDayPage() throws IOException {
        String url = "https://www.gismeteo.com/weather-timisoara-3370/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    public static void main(String[] args) throws Exception {
        List<Days> daysList = new ArrayList<>();
        InitilizeCalendar(daysList);
        daysList.stream().forEach(System.out::println);

        menu();
    }

    public static void menu() {

        String opt = null;
        while(true) {
            System.out.println("1. Watch temperature in a specific day : ");
            System.out.println("2. Watch detailed information about days (max 10 days in future)");
            System.out.println("0. Exit");

            switch (opt) {
                case "1" :
                    break;
                case "2" :
                    break;
                case "0" :
                    break;
                default:
                    System.out.println("Inexistent Option - Try Another One ?");
                    break;
            }
        }
    }

    public static void InitilizeCalendar(List<Days> data) throws IOException {
        Elements daysInfo = getMonthPage().select("div[class=widget-body]").select("a[class=row-item]");
        StringBuilder m = new StringBuilder();

        for (Element e : daysInfo) {
            //Extract [0] - date(obligatory), [1] - month(Optional)
            String[] date = e.select("div[class~=date item-day-\\d+]").text().split(" ");

            //Checks if the month exists; if not, add it like before one. If the month exists, update it.
            if (date.length == 2)
                m = new StringBuilder(date[1]);

            //If the "href" attribute is present, extract and add them to an object.
            // If the link exists, it is likely to provide detailed information about the watch.
            Element l = e.selectFirst("a.row-item[href]");
            String link = null;
            if (l != null) {
                link = l.attr("href").toString();
                data.add(new Days(Integer.parseInt(date[0]), m.toString(), website + link)); //If link exist
            } else
                data.add(new Days(Integer.parseInt(date[0]), m.toString())); //If the link does not exist
        }
    }

    public InfoDay CollectDayInfo() throws IOException {
        Element tableInfo = getDayPage().select("div[class=widget widget-weather-parameters widget-oneday]").first();

        Element date = tableInfo.select("span[class=item item-day-1]").first();
        //System.out.println("Date : " + date.text());

        //System.out.println("Hours : ");
        Elements hours = tableInfo.select("div[class=widget-row widget-row-time]").select("span");
        List<String> h = new ArrayList<>();
        for (Element e : hours) {
            String b = e.text();
            b = b.substring(0, b.length() - 2);
            h.add(b);
        }
        //h.forEach(System.out::println);

        //System.out.println("Temperature : ");
        Elements temp = tableInfo.select("div[class=values]").select("span[class=unit unit_temperature_c]");
        List<String> t = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            t.add(temp.get(i).ownText());
        }
        //t.forEach(System.out::println);

        //System.out.println("Wind m/s : ");
        Elements wind = tableInfo.select("div[class=widget-row widget-row-wind-gust row-with-caption]").select("div[class=row-item]");
        wind = wind.select("span");
        wind.removeIf(a -> a.hasClass("unit_wind_km_h"));
        List<String> w = wind.stream().map(Element::text).collect(Collectors.toList());

        InfoDay now = new InfoDay(date.text(), h, t, w);
        System.out.println(now);


        Elements days = getDayPage().select("div[class=weathertabs day-1]");
        System.out.println(days);

        for (Element e : days) {
            if (e.hasAttr("a")) System.out.println("sad");
        }

        return new InfoDay(date.text(), h, t, w);
    }
}