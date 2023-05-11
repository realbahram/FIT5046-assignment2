package com.example.goal.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Customer {

    public Customer() {

    }

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "name")
    @NonNull
    private String name;

    @ColumnInfo(name = "email")
    @NonNull
    private String email;

    @ColumnInfo(name = "address")
    @NonNull
    private String address;

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Customer(@NonNull int id, @NonNull String name, @NonNull String email, @NonNull String address) {
        this.uid = id;
        this.name = name;
        this.email = email;
        this.address = address;
    }
    public int getUid() {
        return uid;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }
}