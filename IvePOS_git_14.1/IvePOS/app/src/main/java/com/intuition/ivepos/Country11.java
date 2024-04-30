package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 11/17/2015.
 */
public class Country11 {

    String code = null;
    String name = null;
    String id = null;
    String barvalue = null;
    String qquu = null;
    String continent = null;
    String region = null;

    public Country11(String code, String name,  String qquu, String id, String barvalue, String continent) {
        super();
        this.code = code;
        this.name = name;
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

    @Override
    public String toString() {
        return  code + " " + name + " " + " " + barvalue
                + continent + " " + region;
    }


}
