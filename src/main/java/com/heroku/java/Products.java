    package com.heroku.java;

    public class Products {
        public int proid ;
        public String proname;
        public String protype;
        public int proprice;
        public byte[] proimg;

        public Products(){

        }

        public Products(int proid, String proname, String protype, int proprice, byte[] proimg) {
            this.proid = proid;
            this.proname = proname;
            this.protype = protype;
            this.proprice = proprice;
            this.proimg = proimg;
        }


        public int getProid() {
            return this.proid;
        }

        public void setProid(int proid) {
            this.proid = proid;
        }

        public String getProname() {
            return this.proname;
        }

        public void setProname(String proname) {
            this.proname = proname;
        }

        public String getProtype() {
            return this.protype;
        }

        public void setProtype(String protype) {
            this.protype = protype;
        }

        public int getProprice() {
            return this.proprice;
        }

        public void setProprice(int proprice) {
            this.proprice = proprice;
        }

        public byte[] getProimg() {
            return this.proimg;
        }

        public void setProimg(byte[] proimg) {
            this.proimg = proimg;
        }

        
    }
