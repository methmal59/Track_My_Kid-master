package com.project.dreamsquad.trackmykid.models;

/**
 * Created by this pc on 14-05-17.
 */

public class Notifications {

    private String reminder;
    private String bus_status;
    private String school;
    private String day;

    public Notifications(String reminder, String bus_status, String school, String day) {
        this.reminder = reminder;
        this.bus_status = bus_status;
        this.school = school;
        this.day = day;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getBus_status() {
        return bus_status;
    }

    public void setBus_status(String bus_status) {
        this.bus_status = bus_status;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
