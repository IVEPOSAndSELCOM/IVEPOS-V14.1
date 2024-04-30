package com.intuition.ivepos;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DataModel_w {//extends ArrayList<DataModel_w> {


        public String text;
        public String no_items;
        public String totalprice;
        public String order_type;
        public String cust_name, cust_phno, cust_address, cust_deli_instru,cust_deliv_area,cust_email;
        public String status, order_cancelreason, ordid, dbstatus;
        public String deli_per_name;
        public String deli_per_phon, deli_per_status;
        public String str_merch_id;
        public String item_name,item_quantity,order_instructions1;
        public JSONArray dbitems1,items1, dbitems;

    public String dborder_info_id, dborder_type1, dbdate_time, dbpayme_type, dbrestaurant_id, dbrestaurant_name, dbexternal_order_id, dbrestaurant_address, dbrestaurant_number, dborder_from, dbenable_delivery, dbnet_amount, dbgross_amount, dborder_instructions, dborder_gst, dborder_cgst, dborder_sgst, dbpackaging, dborder_packaging, dbpackaging_cgst_percent, dbpackaging_sgst_percent, dbpackaging_cgst, dbpackaging_sgst, dborder_pckg_cgst, dborder_pckg_sgst, dborder_pckg_cgst_per, dborder_pckg_sgst_per, dbdelivery_charge, dbdiscount_type, dbdiscount_amount, dbcust_name1, dbcust_phno, dbcust_email, dbcust_address, dbcust_deliv_area, dbcust_deli_instru, dbno_items, dorder_status;
    public String rider_name, rider_number, rider_assigned, rider_arrived, pickedup, delivered, rider_arrivetime;
    int time_to_arrive, ordstatus;
    ArrayList orderflag;




        public DataModel_w(String t, String no_items1, String totalprice1, String order_type1, String cust_name1,String cust_phno1,String cust_address1,String cust_deli_instru1,String cust_deliv_area1,String cust_email1, String status1,String order_reason1, String deli_per_name1, String deli_per_phon1, String deli_per_status1, JSONArray items,String order_instructions,String str_merch_id1, String deli_arrivetime1)
        {
            text=t;
            no_items=no_items1;
            totalprice=totalprice1;
            order_type=order_type1;
            cust_name=cust_name1;
            status=status1;
            order_cancelreason=order_reason1;
            deli_per_name=deli_per_name1;
            deli_per_phon=deli_per_phon1;
            deli_per_status=deli_per_status1;
            str_merch_id=str_merch_id1;
            cust_phno=cust_phno1;
            cust_address=cust_address1;
            cust_deli_instru=cust_deli_instru1;
            cust_deliv_area=cust_deliv_area1;
            cust_email=cust_email1;
            order_instructions1=order_instructions;
            items1=items;
            rider_arrivetime=deli_arrivetime1;
        }

    public DataModel_w(String order_info_id, String item_name1, String item_quantity1, String str_merch_id1) {

            text = order_info_id;
            item_name=item_name1;
            item_quantity=item_quantity1;
            str_merch_id=str_merch_id1;
    }

    public DataModel_w(String deli_per_name1, String deli_per_phon1, String deli_per_status1){//, String rider_assigned1, String rider_arrived1, String pickedup1, String delivered1) {

            /*rider_name=rider_name1;
            rider_arrived=rider_arrived1;
            rider_assigned=rider_assigned1;
            time_to_arrive=time_to_arrive1;
            rider_number=rider_number1;
            pickedup=pickedup1;
            delivered=delivered1;*/
        deli_per_name=deli_per_name1;
        deli_per_phon=deli_per_phon1;
        deli_per_status=deli_per_status1;
    }

    public DataModel_w(String order_info_id, String order_type1, String date_time, String payme_type, String restaurant_id, String restaurant_name, String external_order_id, String restaurant_address, String restaurant_number, String order_from, String enable_delivery, String net_amount, String gross_amount, String order_instructions, String order_gst, String order_cgst, String order_sgst, String packaging, String order_packaging, String packaging_cgst_percent, String packaging_sgst_percent, String packaging_cgst, String packaging_sgst, String order_pckg_cgst, String order_pckg_sgst, String order_pckg_cgst_per, String order_pckg_sgst_per, String delivery_charge, String discount_type, String discount_amount, String cust_name1, String cust_phno, String cust_email, String cust_address, String cust_deliv_area, String cust_deli_instru, String no_items, String order_status, JSONArray dbitems) {

        dborder_info_id=order_info_id;
        dborder_type1=order_type1;
        dbdate_time=date_time;
        dbpayme_type=payme_type;
        dbrestaurant_id=restaurant_id;
        dbrestaurant_name=restaurant_name;
        dbexternal_order_id=external_order_id;
        dbrestaurant_address=restaurant_address;
        dbrestaurant_number=restaurant_number;
        dborder_from=order_from;
        dbenable_delivery=enable_delivery;
        dbnet_amount=net_amount;
        dbgross_amount=gross_amount;
        dborder_instructions=order_instructions;
        dborder_gst=order_gst;
        dborder_cgst=order_cgst;
        dborder_sgst=order_sgst;
        dbpackaging=packaging;
        dborder_packaging=order_packaging;
        dbpackaging_cgst_percent=packaging_cgst_percent;
        dbpackaging_sgst_percent=packaging_sgst_percent;
        dbpackaging_cgst=packaging_cgst;
        dbpackaging_sgst=packaging_sgst;
        dborder_pckg_cgst=order_pckg_cgst;
        dborder_pckg_sgst=order_pckg_sgst;
        dborder_pckg_cgst_per=order_pckg_cgst_per;
        dborder_pckg_sgst_per=order_pckg_sgst_per;
        dbdelivery_charge=delivery_charge;
        dbdiscount_type=discount_type;
        dbdiscount_amount=discount_amount;
        dbcust_name1=cust_name1;
        dbcust_phno=cust_phno;
        dbcust_email=cust_email;
        dbcust_address=cust_address;
        dbcust_deliv_area=cust_deliv_area;
        dbcust_deli_instru=cust_deli_instru;
        dbno_items=no_items;
        dorder_status=order_status;
        dbitems1=dbitems;

        }
}

