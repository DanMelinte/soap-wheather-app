package org.Main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InfoDay extends Day{
    private List<String> hours = new ArrayList<>(8);
    private List<String> temp = new ArrayList<>(8);
    private List<String> wind = new ArrayList<>(8);
    private List<String> prec = new ArrayList<>(8);

    public InfoDay(LocalDate date, String maxTemp, String minTemp, String link, List<String> hours, List<String> temp, List<String> wind, List<String> prec) {
        super(date, maxTemp, minTemp, link);
        this.hours = hours;
        this.temp = temp;
        this.wind = wind;
        this.prec = prec;
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

    public List<String> getPrec() {
        return prec;
    }

    public void setPrec(List<String> prec) {
        this.prec = prec;
    }

    @Override
    public String toString() {
        return "InfoDay{" +
                "hours=" + hours +
                ", temp=" + temp +
                ", wind=" + wind +
                ", prec=" + prec +
                ", date=" + date +
                ", MaxTemp='" + MaxTemp + '\'' +
                ", MinTemp='" + MinTemp + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}