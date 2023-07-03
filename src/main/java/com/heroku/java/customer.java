package com.heroku.java;

public class customer extends User{
    
    public String phonenumber;
    public String address;

    //constructor

    public customer(String fullname, String email, String password,String usertype, String phonenumber, String address){
        super(fullname, email, password, usertype);
        this.phonenumber = phonenumber;
        this.address = address;
    }


    public String getPhonenumber() {
        return this.phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAddress() {
        return this.address;
    }

    public void setCustaddress(String address) {
        this.address = address;
    }


}
