package com.heroku.java;

public class User {
    public String name;
    public String email;
    public String phonenumber;
    public String address;
    public String password;
    public String radio;

    public User(String name, String email, String phonenumber, String address, String password) {
        this.name = name;
        this.email = email;
        this.phonenumber = phonenumber;
        this.address = address;
        this.password = password;

     public String getRadio() {
        return radio;
    }

    public void setRadio(String radio) {
        this.radio = radio;
    }
}
}

