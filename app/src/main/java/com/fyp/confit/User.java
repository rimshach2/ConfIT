package com.fyp.confit;

import android.provider.ContactsContract;

import java.io.Serializable;

public class User implements Serializable {
    int id;
    String name;
    String email;
    String phone;
    String authentication_token;
    String source;
    String occupation;
    String city;
    String date_of_birth;

    public User()
    {}
    public User(int id, String name, String email, String phone, String authentication_token, String source) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.authentication_token = authentication_token;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAuthentication_token() {
        return authentication_token;
    }

    public void setAuthentication_token(String authentication_token) {
        this.authentication_token = authentication_token;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
}
