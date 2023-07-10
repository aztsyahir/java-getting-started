package com.heroku.java;

public class Cupcakes extends Products{
    public String cuptoppings;

    public Cupcakes(){

    }

    public Cupcakes(int proid,String proname,String protype,int proprice,byte[] proimg,String cuptoppings){
        super(proid,proname,protype,proprice,proimg);
        this.cuptoppings =cuptoppings;
    }


    public String getCuptoppings() {
        return this.cuptoppings;
    }

    public void setCuptoppings(String cuptoppings) {
        this.cuptoppings = cuptoppings;
    }

}
