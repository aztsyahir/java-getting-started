package com.heroku.java.MODEL;

public class customer extends User{
    
    public String custaddress;
    public int custphone;
    
    //constructor
    public customer(){

    }

    public customer(int userid,String fullname,String email,String password,String custaddress, int custphone) {
        super(userid, fullname, email, password);
        this.custaddress = custaddress;
        this.custphone = custphone;
    }


    public String getCustaddress() {
        return this.custaddress;
    }

    public void setCustaddress(String custaddress) {
        this.custaddress = custaddress;
    }

    public int getCustphone() {
        return this.custphone;
    }

    public void setCustphone(int custphone) {
        this.custphone = custphone;
    }

}
