package com.hui2020.huihui.Payment;

import java.io.Serializable;

/**
 * Created by FD-GHOST on 2017/1/11.
 */

public class Contact implements Serializable{
    private String name;
    private String phone;
    private String job;
    private String company;

    public Contact(String name, String phone, String job, String company){
        this.name = name;
        this.phone = phone;
        this.job = job;
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
