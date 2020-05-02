package com.example.nfclastdemo.bean;

import cn.bmob.v3.BmobObject;

public class OfficialBusiness extends BmobObject {

    //private String phone;
    private String begin;
    private String end;
    private String employeeId;

   /* public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }*/

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
