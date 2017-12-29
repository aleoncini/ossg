package org.beccaria.ossg.model;

import org.bson.Document;

import java.util.Calendar;

public class DayOfEvent {
    private int year;
    private int month;
    private int day;

    public DayOfEvent today() {
        this.day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        this.month = Calendar.getInstance().get(Calendar.MONTH) +1;
        this.year = Calendar.getInstance().get(Calendar.YEAR);
        return this;
    }

    public int getYear() {
        return year;
    }

    public DayOfEvent setYear(int year) {
        this.year = year;
        return this;
    }

    public int getMonth() {
        return month;
    }

    public DayOfEvent setMonth(int month) {
        this.month = month;
        return this;
    }

    public int getDay() {
        return day;
    }

    public DayOfEvent setDay(int day) {
        this.day = day;
        return this;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");
        buffer.append("\"day\": ").append(day).append(", ");
        buffer.append("\"month\": ").append(month).append(", ");
        buffer.append("\"year\": ").append(year).append(" }");
        return  buffer.toString();
    }

    public String prettyPrint() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n{ \n");
        buffer.append("\"day\": ").append(day);
        buffer.append(", \n");
        buffer.append("\"month\": ").append(month);
        buffer.append(", \n");
        buffer.append("\"year\": ").append(year);
        buffer.append("\n }\n");
        return  buffer.toString();
    }

    public Document getDocument(){
        return new Document("day", this.day).append("month", this.month).append("year", this.year);
    }

    public DayOfEvent build(Document doc){
        this.day = doc.getInteger("day");
        this.month = doc.getInteger("month");
        this.year = doc.getInteger("year");
        return this;
    }

}
