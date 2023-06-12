package org.Main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Days {
    int date;
    List<String> linkDays = new ArrayList<>(10);

    public Days(int date, List<String> linkDays) {
        this.date = date;
        this.linkDays = linkDays;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public List<String> getLinkDays() {
        return linkDays;
    }

    public void setLinkDays(List<String> linkDays) {
        this.linkDays = linkDays;
    }

    @Override
    public String toString() {
        return "Days{" + "date=" + date + ", linkDays=" + linkDays + '}';
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
    private static Document MainPage() throws IOException {
        Document page = Jsoup.parse(new URL("https://www.gismeteo.com/weather-timisoara-3370/month/"), 3000);
        return page;
    }

    private static Document getPage() throws IOException {
        String url = "https://www.gismeteo.com/weather-timisoara-3370/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    private Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");
    private String getData(String a) throws Exception {
        Matcher matcher = pattern.matcher(a);
        if (matcher.find()) return matcher.group();
        throw new Exception("Inexistent");
    }

    public static void main(String[] args) throws IOException {


    }

    public InfoDay CollectDayInfo() throws IOException {
        Element tableInfo = getPage().select("div[class=widget widget-weather-parameters widget-oneday]").first();

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


        Elements days = getPage().select("div[class=weathertabs day-1]");
        System.out.println(days);

        for (Element e : days) {
            if (e.hasAttr("a")) System.out.println("sad");
        }

        return new InfoDay(date.text(), h, t, w);
    }
}