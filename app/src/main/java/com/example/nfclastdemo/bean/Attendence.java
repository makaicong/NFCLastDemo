package com.example.nfclastdemo.bean;

import cn.bmob.v3.BmobObject;

public class Attendence extends BmobObject {
    private String employeeId;
    private String morning_in;
    private String morning_out;
    private String afternoon_in;
    private String afternoon_out;
    //private String date;
    //private String name;
    //private String phone;
    private String month;
    private String year;
    private String day;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getMorning_in() {
        return morning_in;
    }

    public void setMorning_in(String morning_in) {
        this.morning_in = morning_in;
    }

    public String getMorning_out() {
        return morning_out;
    }

    public void setMorning_out(String morning_out) {
        this.morning_out = morning_out;
    }

    public String getAfternoon_in() {
        return afternoon_in;
    }

    public void setAfternoon_in(String afternoon_in) {
        this.afternoon_in = afternoon_in;
    }

    public String getAfternoon_out() {
        return afternoon_out;
    }

    public void setAfternoon_out(String afternoon_out) {
        this.afternoon_out = afternoon_out;
    }

   /* public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }*/

    /*public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }*/

    /*public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }*/

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
