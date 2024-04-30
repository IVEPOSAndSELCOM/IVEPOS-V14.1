package com.intuition.ivepos;

import java.io.Serializable;

/**
 * Created by Rohithkumar on 11/17/2015.
 */
public class Country_items implements Serializable {

    String code = null;
    String name = null;
    String barvalue = null;
    String qty = null;
    String price = null;
    String continent = null;
    String region = null;

    public Country_items(String code, String name, String barvalue, String qty, String price) {
        super();
        this.code = code;
        this.name = name;
        this.barvalue = barvalue;
        this.qty = qty;
        this.price = price;
        this.continent = continent;
        this.region = region;
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

    public String getbarvalue() {
        return barvalue;
    }
    public void setbarvalue(String barvalue) {
        this.barvalue = barvalue;
    }

    public String getqty() {
        return qty;
    }
    public void setqty(String qty) {
        this.qty = qty;
    }

    public String getprice() {
        return price;
    }
    public void setprice(String price) {
        this.price = price;
    }


    public String getContinent() {
        return continent;
    }
    public void setContinent(String continent) {
        this.continent = continent;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return  code + " " + name + " " + barvalue + " " + continent + " " + region;
    }


}
