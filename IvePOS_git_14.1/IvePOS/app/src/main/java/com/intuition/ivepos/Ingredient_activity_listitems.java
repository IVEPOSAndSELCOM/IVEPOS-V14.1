package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 11/17/2015.
 */
public class Ingredient_activity_listitems {


    String code = null;
    String name = null;
    String barcode = null;
    String min = null;
    String opt = null;
    String max = null;
    String current = null;
    String price = null;
    String unit = null;

//    String position = null;

    public Ingredient_activity_listitems(String name, String barcode, String unit, String min, String max, String current, String price) {
        super();
        this.name = name;
        this.barcode = barcode;
        this.unit = unit;
        this.min = min;
        this.max = max;
        this.current = current;
        this.price = price;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }


    public String getName() {
        return name;
    }

    boolean moveToPosition(int position) {
        return true;
    }

    @Deprecated
    boolean requery() {
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getmin() {
        return min;
    }
    public void setmin(String min) {
        this.min = min;
    }

    public String getopt() {
        return opt;
    }
    public void setopt(String opt) {
        this.opt = opt;
    }


    public String getmax() {
        return max;
    }
    public void setmax(String max) {
        this.max = max;
    }

    public String getcurrent() {
        return current;
    }
    public void setcurrent(String current) {
        this.current = current;
    }

    public String getprice() {
        return price;
    }
    public void setprice(String price) {
        this.price = price;
    }

    public String getunit() {
        return unit;
    }
    public void setunit(String unit) {
        this.unit = unit;
    }

//    public String getposition() {
//        return position;
//    }
//
//    public void setposition(String position) {
//        this.position = position;
//    }

//    public String getContinent() {
//        return continent;
//    }
//    public void setContinent(String continent) {
//        this.continent = continent;
//    }
//    public String getRegion() {
//        return region;
//    }
//    public void setRegion(String region) {
//        this.region = region;
//    }

    @Override
    public String toString() {
        return  name + " "
                + barcode;
    }


}
