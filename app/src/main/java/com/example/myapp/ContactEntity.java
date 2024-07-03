package com.example.myapp;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "contact",
        indices = {@Index(value = {"name", "phone", "email"}, unique = true)})
public class ContactEntity {
    @PrimaryKey(autoGenerate = true)
    private int idx;

    @ColumnInfo
    private String name;
    private String department;
    private String phone;
    private String email;

    public ContactEntity(String name, String department, String phone, String email) {
        this.name = name;
        this.department = department;
        this.phone = phone;
        this.email = email;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
