package com.heroku.java;

public class User {
    public String fullname;
    public String email;
    public String password;
    public String usertype;

     public User(String fullname, String email, String password, String usertype) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.usertype= usertype;
    }

    public String getName() {
        return this.fullname;
    }

    public void setName(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsertype() {
        return this.usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

}

