package com.heroku.java;

public class Cakes extends Products {
    public int cakesize;

    public Cakes(){

    }
 
    public Cakes(int proid,String proname,String protype,int proprice,byte[] proimg,int cakesize){
        super(proid,proname,protype,proprice,proimg);
        this.cakesize = cakesize;
    }


    public int getCakesize() {
        return this.cakesize;
    }

    public void setCakesize(int cakesize) {
        this.cakesize = cakesize;
    }


}
