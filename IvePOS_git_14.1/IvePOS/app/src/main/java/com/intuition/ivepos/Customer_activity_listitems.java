package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 11/17/2015.
 */
public class Customer_activity_listitems {

    String code = null;
    String name = null;
    String cust_id = null;
    String email = null;
    String phone = null;
    String addr = null;
    String sale = null;
    String balance = null;
    String discount = null;
    String discount_type = null;
    String appr_rating = null;

//    String position = null;

    public Customer_activity_listitems(String cust_id, String name, String phone, String email, String addr, String sale, String balance, String discount, String discount_type, String appr_rating) {
        super();
        this.code = code;
        this.name = name;
        this.cust_id = cust_id;
        this.phone = phone;
        this.email = email;
        this.addr = addr;
        this.sale = sale;
        this.balance = balance;
        this.discount = discount;
        this.discount_type = discount_type;
        this.appr_rating = appr_rating;
//        this.position = position;
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

    public String getphone() {
        return phone;
    }
    public void setphone(String phone) {
        this.phone = phone;
    }

    public String getcust_id() {
        return cust_id;
    }
    public void setcust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getemail() {
        return email;
    }
    public void setemail(String email) {
        this.email = email;
    }


    public String getaddr() {
        return addr;
    }
    public void setaddr(String addr) {
        this.addr = addr;
    }

    public String getsale() {
        return sale;
    }
    public void setsale(String sale) {
        this.sale = sale;
    }

    public String getbalance() {
        return balance;
    }
    public void setbalance(String balance) {
        this.balance = balance;
    }

    public String getdiscount() {
        return discount;
    }
    public void setdiscount(String discount) {
        this.discount = discount;
    }

    public String getdiscount_type() {
        return discount_type;
    }
    public void setdiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public String getappr_rating() {
        return appr_rating;
    }
    public void setappr_rating(String appr_rating) {
        this.appr_rating = appr_rating;
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
        return  name + " " + phone + " " + cust_id;
    }


}
