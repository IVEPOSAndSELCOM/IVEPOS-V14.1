package com.intuition.ivepos;

public class DataModel {


    public String text;
    public String no_items;
    public String totalprice;
    public String order_type;
    public String cust_name;
    public String time;
    public String status;
    public String deli_per_name;
    public String deli_per_phon;
    public String str_merch_id;
//    public int drawable;
//    public String color;

    public DataModel(String t, String no_items1, String totalprice1, String order_type1, String cust_name1, String time1, String status1, String deli_per_name1, String deli_per_phon1, String str_merch_id1)
    {
        text=t;
        no_items=no_items1;
        totalprice=totalprice1;
        order_type=order_type1;
        cust_name=cust_name1;
        time=time1;
        status=status1;
        deli_per_name=deli_per_name1;
        deli_per_phon=deli_per_phon1;
        str_merch_id=str_merch_id1;
//        drawable=d;
//        color=c;
    }
}
