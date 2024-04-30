package com.intuition.ivepos;

/**
 * Created by HATSUN on 3/23/2017.
 */

public class Charity {
    int id;
    String companyName;
    String storename;
    String devicename;
    String email;
    String password;
    String confirmpassword;


    public Charity() {

    }

    public Charity(int id, String companyName, String storename, String devicename, String email, String password, String confirmpassword) {
        this.id = id;
        this.companyName = companyName;
        this.storename = storename;
        this.devicename = devicename;
        this.email = email;
        this.password = password;
        this.confirmpassword = confirmpassword;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }
}