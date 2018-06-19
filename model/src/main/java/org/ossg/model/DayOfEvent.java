package org.ossg.model;

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

    public boolean isEqual(DayOfEvent doe){
        return ((this.year == doe.year) && (this.month == doe.month) && (this.day == doe.day));
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

    public String text() {
        return new StringBuffer().append(day).append(" ").append(getMonthName()).append(" ").append(year).toString();
    }

    private String getMonthName() {
        switch(month) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            default:
                return "Dic";
        }
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

    public DayOfEvent build(String date){
        // This method can be used in case the date string is in the format:
        // DD/MM/YYYY
        this.day = Integer.parseInt(date.substring(0,2));
        this.month = Integer.parseInt(date.substring(3,5));
        this.year = Integer.parseInt(date.substring(6,10));
        return this;
    }

}
