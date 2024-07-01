package com.example.myapp;

import android.widget.ImageView;

import java.io.Serializable;

public class ContactItem implements Serializable { // 다른 개체로 넘기기 위해 Serializable implement
    private int idx;
    private String name, department, phoneNumber, email;
    private ImageView profile = null;

    public ContactItem() {
        this.idx = 0;
    }

    public ContactItem(int idx, String name, String department, String phoneNumber, String email) {
        this.idx = idx;
        this.name = name;
        this.department = department;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public ContactItem(String name, String department, String phoneNumber, String email) {
        this.name = name;
        this.department = department;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
/*
    public ContactItem(String name, String department, String phoneNumber, String email, ImageView profile) {
        this.name = name;
        this.department = department;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.profile = profile;
    }*/

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfile(ImageView profile) {
        this.profile = profile;
    }
}
