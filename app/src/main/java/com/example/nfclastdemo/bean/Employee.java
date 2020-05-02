package com.example.nfclastdemo.bean;

import cn.bmob.v3.BmobObject;

public class Employee extends BmobObject {
    private String employeeId;
    private String name;
    private String sex;
    private String age;
    private String phone;
    private String department;
    //private String ismanage;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

   /* public String getIsmanage() {
        return ismanage;
    }

    public void setIsmanage(String ismanage) {
        this.ismanage = ismanage;
    }*/
}
