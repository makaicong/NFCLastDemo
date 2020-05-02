package com.example.nfclastdemo.bean;

public class Statistics {
    private String employeeId;
    private String name;
    private String date;
    private String is_complete;
    private String remark;

    public Statistics(String employeeId,String name,String date,String is_complete,String remark){
        this.employeeId=employeeId;
        this.name=name;
        this.date=date;
        this.is_complete=is_complete;
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIs_complete() {
        return is_complete;
    }

    public void setIs_complete(String is_complete) {
        this.is_complete = is_complete;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
