package org.Main;

import java.time.LocalDate;

public class Day {
    LocalDate date;
    String MaxTemp;
    String MinTemp;
    String link = null;

    public Day(LocalDate date, String maxTemp, String minTemp, String link) {
        this.date = date;
        MaxTemp = maxTemp;
        MinTemp = minTemp;
        this.link = link;
    }

    public Day(LocalDate date, String maxTemp, String minTemp) {
        this.date = date;
        MaxTemp = maxTemp;
        MinTemp = minTemp;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMaxTemp() {
        return MaxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        MaxTemp = maxTemp;
    }

    public String getMinTemp() {
        return MinTemp;
    }

    public void setMinTemp(String minTemp) {
        MinTemp = minTemp;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Day{" +
                "date=" + date +
                ", MaxTemp='" + MaxTemp + '\'' +
                ", MinTemp='" + MinTemp + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}

