package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 11/17/2015.
 */
public class Country_category {

    String code = null;
    String name = null;
    String value = null;
    String continent = null;
    String region = null;

    public Country_category(String code, String name, String value) {
        super();
        this.code = code;
        this.name = name;
        this.value = value;
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

    public String getvalue() {
        return value;
    }
    public void setvalue(String value) {
        this.value = value;
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
        return  code + " " + name + " "
                + continent + " " + region;
    }


}
