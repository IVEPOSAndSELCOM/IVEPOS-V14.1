package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 3/9/2018.
 */

public class Store {
    int _id;
    String storeuserName;
    String storepassword;
    String storename;

    public Store() {

    }

    public Store(int _id, String storeuserName, String storepassword, String storename) {
        this._id = _id;
        this.storeuserName = storeuserName;
        this.storepassword = storepassword;
        this.storename = storename;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getStoreuserName() {
        return storeuserName;
    }

    public void setStoreuserName(String storeuserName) {
        this.storeuserName = storeuserName;
    }

    public String getStorepassword() {
        return storepassword;
    }

    public void setStorepassword(String storepassword) {
        this.storepassword = storepassword;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }
}