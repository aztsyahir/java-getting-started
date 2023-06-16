package com.heroku.java;

public class baker {
    public String bakername;
    public String bakeremail;
    public String bakerphonenumber;
    public String bakeraddress;
    public String bakerpassword;


    public baker(String bakername, String bakeremail, String bakerphonenumber, String bakeraddress, String bakerpassword) {
        this.bakername = bakername;
        this.bakeremail = bakeremail;
        this.bakerphonenumber = bakerphonenumber;
        this.bakeraddress = bakeraddress;
        this.bakerpassword = bakerpassword;
    }
    
    public String getBakername() {
        return this.bakername;
    }

    public void setBakername(String bakername) {
        this.bakername = bakername;
    }

    public String getBakeremail() {
        return this.bakeremail;
    }

    public void setBakeremail(String bakeremail) {
        this.bakeremail = bakeremail;
    }

    public String getBakerphonenumber() {
        return this.bakerphonenumber;
    }

    public void setBakerphonenumber(String bakerphonenumber) {
        this.bakerphonenumber = bakerphonenumber;
    }

    public String getBakeraddress() {
        return this.bakeraddress;
    }

    public void setBakeraddress(String bakeraddress) {
        this.bakeraddress = bakeraddress;
    }

    public String getBakerpassword() {
        return this.bakerpassword;
    }

    public void setBakerpassword(String bakerpassword) {
        this.bakerpassword = bakerpassword;
    }
    
}

