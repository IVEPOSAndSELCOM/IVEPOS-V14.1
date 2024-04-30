package com.intuition.ivepos;

import java.util.ArrayList;

/**
 * Created by Rohithkumar on 11/17/2015.
 */
public class Country_Ingredient1_order {

    String code = null;
    String name = null;
    String order_id = null;
    String order_type = null;
    String date_time = null;
    String payme_type = null;
    String cust_name = null;
    String cust_phno = null;
    String cust_street = null;
    String cust_location = null;
    String cust_deli_instru = null;
    String qty = null;
    String price = null;

    ArrayList sub_item_name = null;
    String sub_item_price = null;
    String instructions = null;

    String id = null;
    String barvalue = null;
    String qquu = null;
    String continent = null;
    String region = null;
    private boolean selected;

    public Country_Ingredient1_order(String name, String qty, String price, ArrayList sub_item_name, String sub_item_price, String instructions) {
        super();
        this.code = code;
        this.name = name;
        this.order_id = order_id;
        this.order_type = order_type;
        this.date_time = date_time;
        this.payme_type = payme_type;
        this.cust_name = cust_name;
        this.cust_phno = cust_phno;
        this.cust_street = cust_street;
        this.cust_location = cust_location;
        this.cust_deli_instru = cust_deli_instru;
        this.qty = qty;
        this.price = price;

        this.sub_item_name = sub_item_name;
        this.sub_item_price = sub_item_price;
        this.instructions = instructions;

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


    public String getOrder_id() {
        return order_id;
    }
    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }


    public String getOrder_type() {
        return order_type;
    }
    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }


    public String getDate_time() {
        return date_time;
    }
    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getPayme_type() {
        return payme_type;
    }
    public void setPayme_type(String payme_type) {
        this.payme_type = payme_type;
    }


    public String getCust_name() {
        return cust_name;
    }
    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }


    public String getCust_phno() {
        return cust_phno;
    }
    public void setCust_phno(String cust_phno) {
        this.cust_phno = cust_phno;
    }


    public String getCust_street() {
        return cust_street;
    }
    public void setCust_street(String cust_street) {
        this.cust_street = cust_street;
    }


    public String getCust_location() {
        return cust_location;
    }
    public void setCust_location(String cust_location) {
        this.cust_location = cust_location;
    }


    public String getCust_deli_instru() {
        return cust_deli_instru;
    }
    public void setCust_deli_instru(String cust_deli_instru) {
        this.cust_deli_instru = cust_deli_instru;
    }


    public String getQty() {
        return qty;
    }
    public void setQty(String qty) {
        this.qty = qty;
    }






    public String getSub_item_name() {
        return String.valueOf(sub_item_name);
    }
    public void setSub_item_name(ArrayList sub_item_name) {
        this.sub_item_name = sub_item_name;
    }


    public String getSub_item_price() {
        return sub_item_price;
    }
    public void setSub_item_price(String sub_item_price) {
        this.sub_item_price = sub_item_price;
    }

    public String getInstructions() {
        return instructions;
    }
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }





    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
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
