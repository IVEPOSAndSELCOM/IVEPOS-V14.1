package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 11/17/2015.
 */
public class Country_items_inventory_indent {

    String name = null;
    String barvalue = null;
    String qty = null;
    String price = null;
    String price1 = null;
    String continent = null;
    String region = null;

    public Country_items_inventory_indent(String name, String barvalue, String qty, String price, String price1) {
        super();
        this.name = name;
        this.barvalue = barvalue;
        this.qty = qty;
        this.price = price;
        this.price1 = price1;
        this.continent = continent;
        this.region = region;
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

    public String getprice1() {
        return price1;
    }
    public void setprice1(String price1) {
        this.price1 = price1;
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
        return  name + " " + barvalue + " " + continent + " " + region;
    }


}
