package com.heroku.java;

public class staff {
     public int staffsid;
    public String staffsname;
    public String staffsemail;
    public String staffspassword;
    public String staffsrole;

    public staff(int staffsid,String staffsname,String staffsemail,String staffspassword,String staffsrole){
        this.staffsid = staffsid;
        this.staffsname = staffsname;
        this.staffsemail = staffsemail;
        this.staffspassword = staffspassword;
        this.staffsrole = staffsrole;
    }


    public int getStaffsid() {
        return this.staffsid;
    }

    public void setStaffsid(int staffsid) {
        this.staffsid = staffsid;
    }

    public String getStaffsname() {
        return this.staffsname;
    }

    public void setStaffsname(String staffsname) {
        this.staffsname = staffsname;
    }

    public String getStaffsemail() {
        return this.staffsemail;
    }

    public void setStaffsemail(String staffsemail) {
        this.staffsemail = staffsemail;
    }

    public String getStaffspassword() {
        return this.staffspassword;
    }

    public void setStaffspassword(String staffspassword) {
        this.staffspassword = staffspassword;
    }

    public String getStaffsrole() {
        return this.staffsrole;
    }

    public void setStaffsrole(String staffsrole) {
        this.staffsrole = staffsrole;
    }


}
