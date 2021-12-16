package com.greenhouse.android.Wrappers;

import androidx.room.Embedded;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email;
    private String password;
    private String name;
    private int id;
    private ArrayList<Device> devices;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User(String email, String password, String name, int id, ArrayList<Device> devices) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.id = id;
        this.devices = devices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDevices(ArrayList<Device> devices) {
        this.devices = devices;
    }


    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Device> getDevices() {
        return devices;
    }


    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", devices=" + devices +
                '}';
    }
}
