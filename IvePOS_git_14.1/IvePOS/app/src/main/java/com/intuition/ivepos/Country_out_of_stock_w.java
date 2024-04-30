package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 11/17/2015.
 */
public class Country_out_of_stock_w {

    String code = null;
    String name = null;
    String out_of_stock = null;
    String id = null;
    String barvalue = null;
    String qquu = null;
    String continent = null;
    String region = null;
    private boolean selected;

    public Country_out_of_stock_w(String name, String out_of_stock) {
        super();
        this.code = code;
        this.name = name;
        this.out_of_stock = out_of_stock;
        this.barvalue = barvalue;
        this.id = id;
        this.qquu = qquu;
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
    public void setName(String name) {
        this.name = name;
    }

    public String getOut_of_stock() {
        return out_of_stock;
    }
    public void setOut_of_stock(String out_of_stock) {
        this.out_of_stock = out_of_stock;
    }

    public String getBarvalue() {
        return barvalue;
    }
    public void setBarvalue(String barvalue) {
        this.barvalue = barvalue;
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getqquu() {
        return qquu;
    }
    public void setqquu(String qquu) {
        this.qquu = qquu;
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

    public boolean isSelected() {
        return selected;
    }


    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return  name + " "
                + continent + " " + region;
    }


}
