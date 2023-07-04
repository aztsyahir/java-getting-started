package com.heroku.java;

public class cake {
    public String caketype;
    public String cakeprice;
    public int cakesize;
    public byte[] cakeimg;


    public cake(String caketype, String cakeprice, int cakesize, byte[] cakeimg) {
        this.caketype = caketype;
        this.cakeprice = cakeprice;
        this.cakesize = cakesize;
        this.cakeimg = cakeimg;
    }
    

    public String getCaketype() {
        return this.caketype;
    }

    public void setCaketype(String caketype) {
        this.caketype = caketype;
    }

    public String getCakeprice() {
        return this.cakeprice;
    }

    public void setCakeprice(String cakeprice) {
        this.cakeprice = cakeprice;
    }

    public int getCakesize() {
        return this.cakesize;
    }

    public void setCakesize(int cakesize) {
        this.cakesize = cakesize;
    }

    public byte[] getCakeimg() {
        return this.cakeimg;
    }

    public void setCakeimg(byte[] cakeimg) {
        this.cakeimg = cakeimg;
    }
}
