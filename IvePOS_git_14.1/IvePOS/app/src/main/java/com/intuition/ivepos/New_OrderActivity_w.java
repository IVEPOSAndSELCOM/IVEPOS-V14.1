package com.intuition.ivepos;


import static com.android.volley.Request.Method.POST;
import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.intuition.ivepos.sync.StubProvider;
import com.intuition.ivepos.syncapp.StubProviderApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


public class New_OrderActivity_w extends AppCompatActivity implements RecyclerViewAdapter_w.ItemListener {

    RecyclerView recyclerView, recyclerView1;
    Spinner all_s_z_ue_fp;
    String dateget, dateget1, timeget;
    String billnum;
    String printbillno;
    String u_name;

    String username, u_username;
    String bill_c, bill_count_tag, bill_count_tag1 = "", bill_count_tag2 = "";
    ArrayList<DataModel_w> arrayList, itemlist;
    ArrayList<DataModel_w> orderflag;


    String url_pending = "https://api.werafoods.com/pos/v2/merchant/pendingorders";
    final String url_accept = "https://api.werafoods.com/pos/v2/order/accept";
    final String url_reject = "https://api.werafoods.com/pos/v2/order/reject";
    final String url_fodready = "https://api.werafoods.com/pos/v2/order/food-ready";
    final String url_support = "https://api.werafoods.com/pos/v2/order/callsupport";
    final String url_getdelivery = "https://api.werafoods.com/pos/v2/order/getde";
    final String url_pickup = "https://api.werafoods.com/pos/v2/order/pickedup";
    final String url_delivered = "https://api.werafoods.com/pos/v2/order/delivered";
    final String url_localdb = "http://theandroidpos.com/IVEPOS_NEW/wera/save_order_details.php";
    String url_orders = "https://api.werafoods.com/pos/v2/merchant/orders";
    //    String url_menuupload = "https://api.werafoods.com/pos/v2/menu/upload";
    String url_menuupload = "https://api.werafoods.com/pos/v2/menu/fullmenu";
    String Url_menulink;
    String saveorder = "http://theandroidpos.com/IVEPOS_NEW/wera/save_order_details.php";
    String updateorder = "http://theandroidpos.com/IVEPOS_NEW/wera/update_order_details.php";
    String weraorderdetails = "http://theandroidpos.com/IVEPOS_NEW/wera/order_details_wera.php";
    String werastock = "https://api.werafoods.com/pos/v2/item/toggle";
    String varistock ="https://api.werafoods.com/pos/v2/menu/sizetoggle";
    String restaurant_open ="https://api.werafoods.com/pos/v2/merchant/toggle";
    String restaurant_open_status ="https://api.werafoods.com/pos/v1/merchant/posstatus";


    JSONObject jsonBody;
    //    ListView listView;
    ArrayList<HashMap<String, String>> contactList;

    int i_ord, c_ord;

    SQLiteDatabase db = null, db1 = null;
    ArrayAdapter<Country_out_of_stock_w> adapter, iadapter;
    ArrayList<Country_out_of_stock_w> list = new ArrayList<Country_out_of_stock_w>();

    Uri contentUri, resultUri;
    RequestQueue requestQueue;
    JSONObject json2;
    String account_selection;

    String str_merch_id;// = "2021";
    String company="",store="",device="";

    public SQLiteDatabase db_inapp = null;
    int countlimit;
    int count = 0;
    RelativeLayout progressBar;

    String WebserviceUrl,ordstatus, st;
    private Context activity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_w);

        db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);

        //Toast.makeText(New_OrderActivity_w.this, "order received", Toast.LENGTH_LONG).show();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);

        Cursor cursor_merchant_id = db.rawQuery("SELECT * FROM Restaurant_id", null);
        if (cursor_merchant_id.moveToFirst()) {
            str_merch_id = cursor_merchant_id.getString(1);
        }
        cursor_merchant_id.close();


        Url_menulink = "https://www.werafoods.com/linking/?id=" + str_merch_id;

        recyclerView = (RecyclerView) findViewById(R.id.newrecycle);
        arrayList = new ArrayList<>();
        all_s_z_ue_fp = (Spinner) findViewById(R.id.all_s_z_ue_fp);


        LinearLayout back_activity = (LinearLayout) findViewById(R.id.nback_activity);
        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        SharedPreferences sharedpreferences_select = SplashScreenActivity.getDefaultSharedPreferencesMultiProcess(New_OrderActivity_w.this);
        account_selection = sharedpreferences_select.getString("account_selection", null);

        if (account_selection.toString().equals("Dine")) {
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        } else {
            if (account_selection.toString().equals("Qsr")) {
                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
            } else {
                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
            }
        }
        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        company = sharedpreferences.getString("companyname", null);
        store = sharedpreferences.getString("storename", null);
        device = sharedpreferences.getString("devicename", null);

//8247488369
        backend();
        //all_new_orders_v();
        // orderpreparing();
        // cancelcount();
        //autoweraupdate();
        //refresh();



        LinearLayout preparing_orders = (LinearLayout) findViewById(R.id.preparing_orders);
        preparing_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(New_OrderActivity_w.this, Preparing_Orders_w.class);
                finish();
                startActivity(intent);
                //onPause();
            }
        });

        LinearLayout cancelorderlayout = (LinearLayout) findViewById(R.id.cancelorderlayout);
        cancelorderlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(New_OrderActivity_w.this, Cancelledorders_w.class);
                finish();
                startActivity(intent);
            }
        });

        //new weraDB_update().execute();

        /*Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Refreshing page in ");

                try {
                    //I try to use this for send Header is application/json
                    jsonBody = new JSONObject();
                    jsonBody.put("merchant_id", str_merch_id);
                    //jsonBody.put("order_id", dborder_info_id);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                RequestQueue mQueue1 = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(url_orders, jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    System.out.println("response " +response);
                                    JSONObject details = response.getJSONObject("details");
                                    JSONArray orders = details.getJSONArray("orders");
                                    for (int i = 0; i < orders.length(); i++) {
                                        JSONObject c = orders.getJSONObject(i);

                                        String order_info_id = c.getString("order_id");
                                        String order_type1 = c.getString("order_type");
                                        String date_time = c.getString("order_date_time");
                                        //date_time= Long.parseLong(date_time);
                                        long receive_time=(Long.parseLong(date_time) * 1000L);
                                        Date addeddate = new Date((receive_time) + 600000); // Unix time is in seconds, so convert to milliseconds
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                                        dateFormat.setTimeZone(TimeZone.getDefault());
                                        date_time= dateFormat.format(addeddate);

                                        //System.out.println("Order Date and time " + date_time);
                                        String payme_type = c.getString("payment_mode");

                                        String restaurant_id = c.getString("restaurant_id");
                                        String restaurant_name = c.getString("restaurant_name");
                                        String external_order_id = c.getString("external_order_id");
                                        String restaurant_address = c.getString("restaurant_address");
                                        String restaurant_number = c.getString("restaurant_number");

                                        String order_status = c.getString("status");

                                        String order_from = c.getString("order_from");

                                        String enable_delivery = c.getString("enable_delivery");
                                        String net_amount = c.getString("net_amount");
                                        String gross_amount = c.getString("gross_amount");
                                        String order_instructions = c.getString("order_instructions");
                                        String order_gst = c.getString("gst");
                                        String order_cgst = c.getString("cgst");
                                        String order_sgst = c.getString("sgst");
                                        // String ord_cgst_percent = c.getString("cgst_percent");
                                        // String ord_sgst_percent = c.getString("sgst_percent");
                                        String packaging = c.getString("packaging");
                                        String order_packaging = c.getString("order_packaging");
                                        String order_pckg_cgst = c.getString("order_packaging_cgst");
                                        String order_pckg_sgst = c.getString("order_packaging_sgst");
                                        String order_pckg_cgst_per = c.getString("order_packaging_cgst_percent");
                                        String order_pckg_sgst_per = c.getString("order_packaging_sgst_percent");
                                        String packaging_cgst = c.getString("packaging_cgst");
                                        String packaging_sgst = c.getString("packaging_sgst");
                                        String packaging_cgst_percent = c.getString("packaging_cgst_percent");
                                        String packaging_sgst_percent = c.getString("packaging_sgst_percent");

                                        // String order_gst_per = c.getString("gst_percent");
                                        String delivery_charge = c.getString("delivery_charge");
                                        //* Documented 'type' is 'discount_reason' and 'amount' is 'discount' in JSON data *
                                        String discount_type = c.getString("discount_reason");
                                        String discount_amount = c.getString("discount");

                                        String cust_name1 = c.getJSONObject("customer_details").getString("name");
                                        String cust_phno = c.getJSONObject("customer_details").getString("phone_number");
                                        String cust_email = c.getJSONObject("customer_details").getString("email");
                                        String cust_address = c.getJSONObject("customer_details").getString("address");
                                        String cust_deliv_area = c.getJSONObject("customer_details").getString("delivery_area");
                                        String cust_deli_instru = c.getJSONObject("customer_details").getString("address_instructions");
                                        JSONArray cart = c.getJSONArray("order_items");
                                        String no_items = String.valueOf(cart.length());


//------------------------------Updating local database----------------------------------------------
                                        ContentValues params = new ContentValues();
                                        //params.put("_id", order_info_id);
                                        //params.put("order_status", status);

                                        params.put("_id", order_info_id);
                                        params.put("order_type", order_type1);
                                        params.put("order_date_time", date_time);
                                        params.put("payment_mode", payme_type);
                                        params.put("restaurant_id", restaurant_id);
                                        params.put("restaurant_name", restaurant_name);
                                        params.put("external_order_id", external_order_id);
                                        params.put("restaurant_address", restaurant_address);
                                        params.put("restaurant_number", restaurant_number);
                                        params.put("order_from", order_from);
                                        params.put("enable_delivery", enable_delivery);
                                        params.put("net_amount", net_amount);
                                        params.put("gross_amount", gross_amount);
                                        params.put("order_instructions", order_instructions);
                                        params.put("gst", order_gst);
                                        params.put("cgst", order_cgst);
                                        params.put("sgst", order_sgst);
                                        params.put("packaging", packaging);
                                        params.put("order_packaging", order_packaging);
                                        params.put("packaging_cgst_percent", packaging_cgst_percent);
                                        params.put("packaging_sgst_percent", packaging_sgst_percent);
                                        params.put("packaging_cgst", packaging_cgst);
                                        params.put("packaging_sgst", packaging_sgst);
                                        params.put("order_packaging_cgst", order_pckg_cgst);
                                        params.put("order_packaging_sgst", order_pckg_sgst);
                                        params.put("order_packaging_cgst_percent", order_pckg_cgst_per);
                                        params.put("order_packaging_sgst_percent", order_pckg_sgst_per);
                                        params.put("delivery_charge", delivery_charge);
                                        params.put("discount_reason", discount_type);
                                        params.put("discount", discount_amount);
                                        params.put("cus_name", cust_name1);
                                        params.put("cus_phone_number", cust_phno);
                                        params.put("cus_email", cust_email);
                                        params.put("cus_address", cust_address);
                                        params.put("cus_delivery_area", cust_deliv_area);
                                        params.put("cus_address_instructions", cust_deli_instru);
                                        params.put("num_item", no_items);
                                        params.put("order_status", order_status);


                                        Cursor ffifteen_1 = db1.rawQuery("SELECT * FROM online_order_wera WHERE _id = '" + order_info_id + "'", null);
                                        if (ffifteen_1.moveToFirst()) {
                                            String where11 = "_id = '" + order_info_id + "' ";
                                            Log.d("Wera table", "Update");
                                            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "online_order_wera");
                                            getContentResolver().update(contentUri, params,where11,new String[]{});
                                            resultUri = new Uri.Builder()
                                                    .scheme("content")
                                                    .authority(StubProvider.AUTHORITY)
                                                    .path("online_order_wera")
                                                    .appendQueryParameter("operation", "update")
                                                    .appendQueryParameter("_id",order_info_id)
                                                    .build();
                                            getContentResolver().notifyChange(resultUri, null);

//                            db1.update("Billnumber", contentValues, where11, new String[]{});
                                        }else {
                                            Log.d("Wera table", "Insert");
                                            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "online_order_wera");
                                            resultUri = getContentResolver().insert(contentUri, params);
                                            getContentResolver().notifyChange(resultUri, null);
                                        }
                                        ffifteen_1.close();
                                        // }
                                    }
                                } catch (final JSONException e) {
                                    Log.e("TAG", "Json parsing error: " + e.getMessage());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Toast.makeText(getApplicationContext(),"Json parsing error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                                            System.out.println("Json parsing error: "+e.getMessage());
                                        }
                                    });
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                */

    }

    @Override
    public void onBackPressed() {
        ///super.onBackPressed();
        /*Intent intent = new Intent(New_OrderActivity_w.this, BeveragesMenuFragment_Dine_l.class);
        startActivity(intent);*/
        finish();

    }

    public void backend() {
        progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.newrecycle);
        final TextView preparing_orders_value = (TextView) findViewById(R.id.preparing_orders_valuen);
        final TextView cancelorder_value = (TextView) findViewById(R.id.corder_valuen);

        i_ord = 0;
        c_ord = 0;


        RequestQueue mQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest strord= new StringRequest(POST, weraorderdetails,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response1) {
                        try {
                             /*response1 = response1.replace("\"","'");
                             JSONObject jo = new JSONObject(response1.substring(1,response1.length()-1));*/
                            JSONArray orddetails = new JSONArray(response1);
                            //String orddetails = String.valueOf(response1);
                            DataModel_w id = null, dbdata=null;
                            ArrayList arrayList= new ArrayList<>();
                            // arrayList.clear();
                            for (int k = 0; k < orddetails.length(); k++) {
                                JSONObject c = orddetails.getJSONObject(k);
                                // System.out.println("inloop backend response: " + c + "  " + response1);
                                String orderid = c.getString("_id");
                                String order_from = c.getString("order_from");
                                String status = c.getString("order_status");
                                String order_reason = c.getString("order_reason");
                                 /*if(order_reason.equalsIgnoreCase("Auto accepted by werafoods") && status.equalsIgnoreCase("Pending")){
                                     status="Accepted";
                                 }*/
                                if (status.toString().equalsIgnoreCase("Preparing") || status.toString().equalsIgnoreCase("Accepted") || status.toString().equalsIgnoreCase("auto-accept")) {
                                    i_ord++;
                                    preparing_orders_value.setText(String.valueOf(i_ord));
                                }
                                if (status.contains("Cancelled") || status.equalsIgnoreCase("timeout") || status.equalsIgnoreCase("reject") || status.equalsIgnoreCase("rejected") || status.contains("cancel")) {
                                    // if (ordid.equalsIgnoreCase(finalOrderid)) {
                                    c_ord++;
                                    cancelorder_value.setText(String.valueOf(c_ord));
                                }

                                if (status.toString().equalsIgnoreCase("Pending")) {
                                    String date_time = c.getString("order_date_time");
                                    String order_type = c.getString("order_type");
                                    // date_time=convertUnixTime(Long.parseLong(date_time));
                                    // System.out.println("Order no " + c);
                                    String payme_type = c.getString("payment_mode");

                                    String restaurant_id = c.getString("restaurant_id");
                                    String restaurant_name = c.getString("restaurant_name");
                                    String external_order_id = c.getString("external_order_id");
                                    String restaurant_address = c.getString("restaurant_address");
                                    String restaurant_number = c.getString("restaurant_number");

                                    //String order_status = c.getString("status");

                                    String enable_delivery = c.getString("enable_delivery");
                                    String net_amount = c.getString("net_amount");
                                    String gross_amount = c.getString("gross_amount");
                                    String order_instructions = c.getString("order_instructions");
                                    String order_gst = c.getString("gst");
                                    String order_cgst = c.getString("cgst");
                                    String order_sgst = c.getString("sgst");
                                    // String ord_cgst_percent = c.getString("cgst_percent");
                                    // String ord_sgst_percent = c.getString("sgst_percent");
                                    String packaging = c.getString("packaging");
                                    String order_packaging = c.getString("order_packaging");
                                    String order_pckg_cgst = c.getString("order_packaging_cgst");
                                    String order_pckg_sgst = c.getString("order_packaging_sgst");
                                    String order_pckg_cgst_per = c.getString("order_packaging_cgst_percent");
                                    String order_pckg_sgst_per = c.getString("order_packaging_sgst_percent");
                                    String packaging_cgst = c.getString("packaging_cgst");
                                    String packaging_sgst = c.getString("packaging_sgst");
                                    String packaging_cgst_percent = c.getString("packaging_cgst_percent");
                                    String packaging_sgst_percent = c.getString("packaging_sgst_percent");

                                    // String order_gst_per = c.getString("gst_percent");
                                    String delivery_charge = c.getString("delivery_charge");
                                    /** Documented 'type' is 'discount_reason' and 'amount' is 'discount' in JSON data **/
                                    String discount_type = c.getString("discount_reason");
                                    String discount_amount = c.getString("discount");

                                    String no_items = c.getString("num_item");
                                    //String order_from = c.getString("order_from");
                                    String order_cancelreason = c.getString("order_reason");
                                    String deli_per_name = c.getString("rider_name");
                                    String deli_per_phon = c.getString("rider_number");
                                    String deli_per_status = c.getString("rider_status");
                                    String deli_arrivetime = c.getString("time_to_arrive");
                                    String cust_name1 = c.getString("cus_name");
                                    String cust_phno = c.getString("cus_phone_number");
                                    String cust_email = c.getString("cus_email");
                                    String cust_address = c.getString("cus_address");
                                    String cust_deliv_area = c.getString("cus_delivery_area");
                                    String cust_deli_instru = c.getString("cus_address_instructions");

                                    //JSONObject items=c.getJSONObject("orderItemsJSON");
                                    JSONArray cart = c.getJSONArray("orderItemsJSON");
                                    System.out.println("inloop inner items response: " + cart);

                                    for (int j = 0; j < cart.length(); j++) {
                                        JSONObject items = cart.getJSONObject(j);
//                                         JSONObject items=cart;

                                        String itemid = items.getString("wera_item_id");
                                        //  System.out.println("item: " + itemid);
                                        String item_id = items.getString("item_id");
                                        String item_name = items.getString("item_name");
                                        String item_unit_price = items.getString("item_unit_price");
                                        String subtotal = items.getString("subtotal");
                                        String item_quantity = items.getString("item_quantity");
                                        String item_gst = items.getString("gst");
                                        String item_gst_per = items.getString("gst_percent");
                                        String item_packaging = items.getString("packaging");
                                        String item_dish = items.getString("dish");
                                        String instructions = items.getString("instructions");
                                        String discount = items.getString("discount");
                                        String item_cgst = items.getString("cgst");
                                        String item_sgst = items.getString("sgst");
                                        String item_cgst_per = items.getString("cgst_percent");
                                        String item_sgst_per = items.getString("sgst_percent");
                                        String item_packaging_gst = items.getString("packaging_gst");
                                        String item_packaging_sgst = items.getString("packaging_sgst");
                                        String item_packaging_cgst = items.getString("packaging_cgst");
                                        String item_cgst_percent = items.getString("packaging_cgst_percent");
                                        String item_sgst_percent = items.getString("packaging_sgst_percent");

                                        // for Variants array
                                        JSONArray variant, addons;
                                        try {
                                            variant = items.getJSONArray("variants");
                                        } catch (JSONException e) {
                                            System.out.println("Json error variants"+ e);
                                            throw new RuntimeException(e);
                                        }
                                        for (int p = 0; p < variant.length(); p++) {
                                            JSONObject var = variant.getJSONObject(p);
                                            try {
                                                String var_size_id = var.getString("size_id");
                                                String var_size_name = var.getString("size_name");
                                                String var_price = var.getString("price");
                                                String var_cgst = var.getString("cgst");
                                                String var_sgst = var.getString("sgst");
                                                String var_cgst_percent = var.getString("cgst_percent");
                                                String var_sgst_percent = var.getString("sgst_percent");
                                            } catch (JSONException e) {
                                                System.out.println("Json error variants"+ e);
                                                throw new RuntimeException(e);
                                            }
                                        }


                                        try {
                                            addons = items.getJSONArray("addons");
                                        } catch (JSONException e) {
                                            System.out.println("Json error Addons");
                                            throw new RuntimeException(e);
                                        }
                                        for (int l = 0; l < addons.length(); l++) {
                                            JSONObject addon = addons.getJSONObject(l);
                                            try {
                                                String addon_id = addon.getString("addon_id");
                                                String addon_name = addon.getString("name");
                                                String addon_price = addon.getString("price");
                                                String addon_cgst = addon.getString("cgst");
                                                String addon_sgst = addon.getString("sgst");
                                                String addon_cgst_percent = addon.getString("cgst_percent");
                                                String addon_sgst_percent = addon.getString("sgst_percent");
                                            } catch (JSONException e) {
                                                System.out.println("Json error Addons");
                                                throw new RuntimeException(e);

                                            }

                                        }

                                    }


                                    if (status.toString().equalsIgnoreCase("pending")) {
                                        id = new DataModel_w(orderid, String.valueOf(cart.length()), gross_amount, order_from, cust_name1, cust_phno, cust_address, cust_deli_instru, cust_deliv_area, cust_email, status, "", deli_per_name, deli_per_phon, deli_per_status,cart,order_instructions, str_merch_id, deli_arrivetime);
                                        arrayList.add(id);
                                        System.out.println("Item"+"recycler orderid: " + orderid + "\nType of order: " + "\nOrder Status: " + status + order_from + "\nCustomer name: " + cust_name1 + "\nNumber of items: " + String.valueOf(cart.length()));

                                        ArrayList orddatadb = new ArrayList<>();
                                        dbdata = new DataModel_w(orderid, order_type, date_time, payme_type, restaurant_id, restaurant_name, external_order_id, restaurant_address,restaurant_number,order_from,enable_delivery,net_amount,gross_amount,
                                                order_instructions,order_gst, order_cgst,order_sgst,packaging,order_packaging, packaging_cgst_percent,packaging_sgst_percent,packaging_cgst,packaging_sgst,
                                                order_pckg_cgst,order_pckg_sgst, order_pckg_cgst_per,order_pckg_sgst_per,delivery_charge,discount_type,discount_amount,cust_name1,cust_phno,cust_email,cust_address,cust_deliv_area,cust_deli_instru, String.valueOf(cart.length()), status, cart);
                                        orddatadb.add(dbdata);
                                        //weraindextable(orderid, status, restaurant_id);
                                        //createordersalesdata(dbdata);
                                        addorderdatadb(dbdata);
                                    }
                                }
                            }

                            progressBar.setVisibility(View.GONE);
                            RecyclerViewAdapter_w adapter = new RecyclerViewAdapter_w(New_OrderActivity_w.this, arrayList, New_OrderActivity_w.this);
                            recyclerView.setAdapter(adapter);
                            i_ord = 0;
                            c_ord = 0;



                            final String Text = all_s_z_ue_fp.getSelectedItem().toString();

                            if (Text.equals("All")) {
                                // Toast.makeText(New_OrderActivity_w.this, "ALL", Toast.LENGTH_LONG).show();
                            }

                            all_s_z_ue_fp.setSelection(0, false);
                            all_s_z_ue_fp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String text1 = parent.getItemAtPosition(position).toString();
                                    if (text1.toString().equals("All")) {
                                        //Toast.makeText(New_OrderActivity_w.this, "Selected All", Toast.LENGTH_LONG).show();
                                        //all_new_orders();
                                    } else {
                                        if (text1.toString().equals("Online")) {
                                            //Toast.makeText(New_OrderActivity_w.this, "Online", Toast.LENGTH_LONG).show();
                                            custom_new_orders("Online");
                                        } else {
                                            if (text1.toString().equals("Swiggy")) {
                                                //Toast.makeText(New_OrderActivity_w.this, "Swiggy", Toast.LENGTH_LONG).show();
                                                custom_new_orders("swiggy");
                                            } else {
                                                if (text1.toString().equals("Zomato")) {
                                                    //Toast.makeText(New_OrderActivity_w.this, "Zomato", Toast.LENGTH_LONG).show();
                                                    custom_new_orders("zomato");
                                                } /*else {
                                                    if (text1.toString().equals("Uber eats")) {
                                                        Toast.makeText(New_OrderActivity_w.this, "Uber eats", Toast.LENGTH_LONG).show();
                                                        custom_new_orders("uber");
                                                    } else {
                                                        if (text1.toString().equals("Food panda")) {
                                                            Toast.makeText(New_OrderActivity_w.this, "Food panda", Toast.LENGTH_LONG).show();
                                                            custom_new_orders("food panda");
                                                        }
                                                        }
                                                        }*/

                                            }
                                        }
                                    }
                                }


                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("New order page", "Error: " + error.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + error.getMessage(),
                                        Toast.LENGTH_LONG)
                                .show();*/
                        System.out.println("Json parsing error: " + error.getMessage());
                    }
                });
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> windex = new HashMap<String, String>();
                windex.put("db_name", company + "_"+ store + "_"+ device);
                //windex.put("order_id", order_info_id);
                //windex.put("order_status", "Pending");
                return windex;
            }
        };
        mQueue1.add(strord);








       /* //---------Header
        try {
            //I try to use this for send Header is application/json
            jsonBody = new JSONObject();
            //jsonBody.put("merchant_id", str_merch_id);
            jsonBody.put("db_name", company + "_"+ store + "_"+ device);
            //            jsonBody.put("order_id", "32747560");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        //-------Request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(weraorderdetails, jsonBody,
                new Response.Listener<JSONObject>() {
            @Override
                    public void onResponse(JSONObject response) {
                        DataModel_w iden = null, dbdata=null;
                        try {
                            JSONObject details = response.getJSONObject("details");
                            JSONArray orders = details.getJSONArray("orders");

                            System.out.println("orders of " + orders);
                            for (int i = 0; i < orders.length(); i++) {
                                JSONObject c = orders.getJSONObject(i);

                                String order_info_id = c.getString("order_id");
                                String order_type1 = c.getString("order_type");
                                String date_time = c.getString("order_date_time");
                                date_time=convertUnixTime(Long.parseLong(date_time));
                                System.out.println("Order no " + c);
                                String payme_type = c.getString("payment_mode");

                                String restaurant_id = c.getString("restaurant_id");
                                String restaurant_name = c.getString("restaurant_name");
                                String external_order_id = c.getString("external_order_id");
                                String restaurant_address = c.getString("restaurant_address");
                                String restaurant_number = c.getString("restaurant_number");

                                String order_status = c.getString("status");

                                String order_from = c.getString("order_from");

                                String enable_delivery = c.getString("enable_delivery");
                                String net_amount = c.getString("net_amount");
                                String gross_amount = c.getString("gross_amount");
                                String order_instructions = c.getString("order_instructions");
                                String order_gst = c.getString("gst");
                                String order_cgst = c.getString("cgst");
                                String order_sgst = c.getString("sgst");
                                // String ord_cgst_percent = c.getString("cgst_percent");
                                // String ord_sgst_percent = c.getString("sgst_percent");
                                String packaging = c.getString("packaging");
                                String order_packaging = c.getString("order_packaging");
                                String order_pckg_cgst = c.getString("order_packaging_cgst");
                                String order_pckg_sgst = c.getString("order_packaging_sgst");
                                String order_pckg_cgst_per = c.getString("order_packaging_cgst_percent");
                                String order_pckg_sgst_per = c.getString("order_packaging_sgst_percent");
                                String packaging_cgst = c.getString("packaging_cgst");
                                String packaging_sgst = c.getString("packaging_sgst");
                                String packaging_cgst_percent = c.getString("packaging_cgst_percent");
                                String packaging_sgst_percent = c.getString("packaging_sgst_percent");

                                // String order_gst_per = c.getString("gst_percent");
                                String delivery_charge = c.getString("delivery_charge");
                                */
        /** Documented 'type' is 'discount_reason' and 'amount' is 'discount' in JSON data **//*
                                String discount_type = c.getString("discount_reason");
                                String discount_amount = c.getString("discount");

                                String cust_name1 = c.getJSONObject("customer_details").getString("name");
                                String cust_phno = c.getJSONObject("customer_details").getString("phone_number");
                                String cust_email = c.getJSONObject("customer_details").getString("email");
                                String cust_address = c.getJSONObject("customer_details").getString("address");
                                String cust_deliv_area = c.getJSONObject("customer_details").getString("delivery_area");
                                String cust_deli_instru = c.getJSONObject("customer_details").getString("address_instructions");

                                JSONArray cart = c.getJSONArray("order_items");

                                for (int j = 0; j < cart.length(); j++) {
                                    JSONObject items = cart.getJSONObject(j);

                                    String itemid = items.getString("wera_item_id");
                                    String item_id = items.getString("item_id");
                                    String item_name = items.getString("item_name");
                                    String item_unit_price = items.getString("item_unit_price");
                                    String subtotal = items.getString("subtotal");
                                    String item_quantity = items.getString("item_quantity");
                                    String item_gst = items.getString("gst");
                                    String item_gst_per = items.getString("gst_percent");
                                    String item_packaging = items.getString("packaging");
                                    String item_dish = items.getString("dish");
                                    String instructions = items.getString("instructions");
                                    String discount = items.getString("discount");
                                    String item_packaging_gst = items.getString("packaging_gst");
                                    String item_packaging_sgst = items.getString("packaging_sgst");
                                    String item_packaging_cgst = items.getString("packaging_cgst");
                                    String item_cgst_percent = items.getString("packaging_cgst_percent");
                                    String item_sgst_percent = items.getString("packaging_sgst_percent");

                                }

                                */
         /*      // for Variants array
                                JSONArray variants = c.getJSONArray("variants");
                                for (int k = 0; k < variants.length(); k++) {
                                    JSONObject var = variants.getJSONObject(k);

                                    String var_size_id = var.getJSONObject("variants").getString("size_id");
                                    String var_size_name = var.getJSONObject("variants").getString("size_name");
                                    String var_price = var.getJSONObject("variants").getString("price");
                                    String var_cgst = var.getJSONObject("variants").getString("cgst");
                                    String var_sgst = var.getJSONObject("variants").getString("sgst");
                                    String var_cgst_percent = var.getJSONObject("variants").getString("cgst_percent");
                                    String var_sgst_percent = var.getJSONObject("variants").getString("sgst_percent");
                                }

                                JSONArray addons = c.getJSONArray("addons");
                                for (int l = 0; l < addons.length(); l++) {
                                    JSONObject addon = addons.getJSONObject(l);

                                    String addon_id = addon.getJSONObject("addons_info").getString("addon_id");
                                    String addon_name = addon.getJSONObject("addons_info").getString("name");
                                    String addon_price = addon.getJSONObject("addons_info").getString("price");
                                    String addon_cgst = addon.getJSONObject("addons_info").getString("cgst");
                                    String addon_sgst = addon.getJSONObject("addons_info").getString("sgst");
                                    String addon_cgst_percent = addon.getJSONObject("addons_info").getString("cgst_percent");
                                    String addon_sgst_percent = addon.getJSONObject("addons_info").getString("sgst_percent");

                                }
                               */
         /*

                                System.out.println("outside loop order id  ");

                                ArrayList orddatadb = new ArrayList<>();
                                dbdata = new DataModel_w(order_info_id, order_type1, date_time, payme_type, restaurant_id, restaurant_name, external_order_id, restaurant_address,restaurant_number,order_from,enable_delivery,net_amount,gross_amount,
                                        order_instructions,order_gst, order_cgst,order_sgst,packaging,order_packaging, packaging_cgst_percent,packaging_sgst_percent,packaging_cgst,packaging_sgst,
                                        order_pckg_cgst,order_pckg_sgst, order_pckg_cgst_per,order_pckg_sgst_per,delivery_charge,discount_type,discount_amount,cust_name1,cust_phno,cust_email,cust_address,cust_deliv_area,cust_deli_instru, String.valueOf(cart.length()), order_status);
                                orddatadb.add(dbdata);
                                weraindextable(order_info_id, order_status, restaurant_id);
                                createordersalesdata(dbdata);
                                addorderdatadb(dbdata);


                                if (order_status.toString().equalsIgnoreCase("pending")) {
                                    iden = new DataModel_w(order_info_id, String.valueOf(cart.length()), gross_amount, order_from, cust_name1 ,cust_phno, cust_address, cust_deli_instru,cust_deliv_area,cust_email, "", "", "", "", "", str_merch_id);
                                    arrayList.add(iden);
                                    System.out.println("recycler orderid: " + order_info_id + "\nType of order: " + "\nOrder Status: " + order_status + order_from + "\nCustomer name: " + cust_name1 + "\nNumber of items: " + String.valueOf(cart.length()));
                                }
                            }
                            */
         /*RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(New_OrderActivity_w.this);
                            recyclerView.setLayoutManager(linearLayoutManager);*/
         /*
                            progressBar.setVisibility(View.GONE);
                            RecyclerViewAdapter_w adapter = new RecyclerViewAdapter_w(New_OrderActivity_w.this, arrayList, New_OrderActivity_w.this);
                            recyclerView.setAdapter(adapter);


                            //Spinner all_s_z_ue_fp = (Spinner) findViewById(R.id.all_s_z_ue_fp);
                            final String Text = all_s_z_ue_fp.getSelectedItem().toString();

                            if (Text.equals("All")) {
                                // Toast.makeText(New_OrderActivity_w.this, "ALL", Toast.LENGTH_LONG).show();
                            }

                            all_s_z_ue_fp.setSelection(0, false);
                            all_s_z_ue_fp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String text1 = parent.getItemAtPosition(position).toString();
                                    if (text1.toString().equals("All")) {
                                        //Toast.makeText(New_OrderActivity_w.this, "Selected All", Toast.LENGTH_LONG).show();
                                        all_new_orders();
                                    } else {
                                        if (text1.toString().equals("Online")) {
                                            //Toast.makeText(New_OrderActivity_w.this, "Online", Toast.LENGTH_LONG).show();
                                            custom_new_orders("Online");
                                        } else {
                                            if (text1.toString().equals("Swiggy")) {
                                                //Toast.makeText(New_OrderActivity_w.this, "Swiggy", Toast.LENGTH_LONG).show();
                                                custom_new_orders("swiggy");
                                            } else {
                                                if (text1.toString().equals("Zomato")) {
                                                    //Toast.makeText(New_OrderActivity_w.this, "Zomato", Toast.LENGTH_LONG).show();
                                                    custom_new_orders("zomato");
                                                } */
         /*else {
                                                    if (text1.toString().equals("Uber eats")) {
                                                        Toast.makeText(New_OrderActivity_w.this, "Uber eats", Toast.LENGTH_LONG).show();
                                                        custom_new_orders("uber");
                                                    } else {
                                                        if (text1.toString().equals("Food panda")) {
                                                            Toast.makeText(New_OrderActivity_w.this, "Food panda", Toast.LENGTH_LONG).show();
                                                            custom_new_orders("food panda");
                                                        }
                                                        }
                                                        }*/
         /*

                                                }
                                            }
                                        }
                                    }


                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("TAG", "Json parsing error: " + e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    */
         /*Toast.makeText(getApplicationContext(),
                                                    "Json parsing error: " + e.getMessage(),
                                                    Toast.LENGTH_LONG)
                                            .show();*/
         /*
                                    System.out.println("Json parsing error: "+e.getMessage());
                                }
                            });
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        */
         /*Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + error.getMessage(),
                                        Toast.LENGTH_LONG)
                                .show();*/
         /*
                        System.out.println("Json parsing error: " + error.getMessage());
                    }
                });
            }
        })

                 { //no semicolon or coma
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "X-Wera-Api-Key");
                params.put("X-Wera-Api-Key", "9b1bc8d1-99d2-4597-aa7e-64e0a9580c10");
                params.put("Accept", "");
                return params;
            }
        };
        mQueue.add(jsonObjectRequest);*/


        /*LinearLayout out_of_stock_items = (LinearLayout) findViewById(R.id.out_of_stock_items);
        out_of_stock_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/


        LinearLayout linearLayout_overflow = findViewById(R.id.out_of_stock_items);
        final PopupMenu popup = new PopupMenu(this, linearLayout_overflow);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.online_order_menu, popup.getMenu());
        linearLayout_overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.show();

                final View[] view1 = {view};
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if(id==R.id.linearLayout){
                            Toast.makeText(getApplicationContext(), "Out of stock", Toast.LENGTH_SHORT).show();

                            final Dialog dialog = new Dialog(New_OrderActivity_w.this, R.style.timepicker_date_dialog);
                            dialog.setContentView(R.layout.dialog_select_out_of_stock_items);
                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            dialog.show();

                            ImageButton imageButton = (ImageButton) dialog.findViewById(R.id.btncancel);
                            imageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            final ListView myListView = (ListView) dialog.findViewById(R.id.listview);

                            String statement = "SELECT * FROM Items";
                            //Execute the query
                            Cursor aallrows = db.rawQuery(statement, null);
                            System.out.println("COUNT : " + aallrows.getCount());
                            list=new ArrayList<>();
                            ////Toast.makeText(New_OrderActivity.this, "limit is a " + limit, Toast.LENGTH_SHORT).show();
                            if (aallrows.moveToFirst()) {
                                do {

                                    String ID = aallrows.getString(0);
                                    String NAme = aallrows.getString(1);
                                    String BAr = aallrows.getString(16);
                                    String out_of_stock = aallrows.getString(51);

                                    String var1 = aallrows.getString(38);
                                    TextView vari1 = new TextView(New_OrderActivity_w.this);
                                    vari1.setText(var1);

                                    if(vari1.getText().toString().isEmpty()) {
                                        Country_out_of_stock_w NAME = new Country_out_of_stock_w(NAme, out_of_stock);
                                        list.add(NAME);
                                    }else{
                                        int OoS= 59;
                                        for(int m = 38; m<48;m=m+2){
                                            String variname = aallrows.getString(m);
                                            TextView variantname = new TextView(New_OrderActivity_w.this);
                                            variantname.setText(variname);
                                            if(variantname.getText().toString().isEmpty()) {
                                            }else{
                                                variantname.setText(NAme+" ("+variname+")");
                                                String var_out_of_stock = aallrows.getString(OoS);
                                                Country_out_of_stock_w NAME = new Country_out_of_stock_w(variantname.getText().toString(), var_out_of_stock);
                                                list.add(NAME);
                                                OoS++;
                                            }
                                        }
                                    }
                                } while (aallrows.moveToNext());
                            }
                            aallrows.close();

                            adapter = new MyAdapter_out_of_stock_w(New_OrderActivity_w.this,list);
                            myListView.setAdapter(adapter);

                            final EditText myFilter = (EditText) dialog.findViewById(R.id.search_selecteditem);
                            myFilter.addTextChangedListener(new TextWatcher() {

                                public void afterTextChanged(Editable s) {
                                }

                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    adapter.getFilter().filter(s.toString());
                                }
                            });

                            for(int i=0; i < myListView.getCount(); i++) {
                                view1[0] = myListView.getAdapter().getView(i, null, null);
                                TextView cb = (TextView) view1[0].findViewById(R.id.label);
                                CheckBox cb1 = (CheckBox) view1[0].findViewById(R.id.check);

                                String inputString = cb.getText().toString();
                                String varname1=null, itemname1=null;
                                int varcount=0;
                                // Find the opening and closing parentheses
                                int openingParenIndex = inputString.indexOf('(');
                                int closingParenIndex = inputString.indexOf(')');

                                if (openingParenIndex != -1 && closingParenIndex != -1) {
                                    varcount++;
                                    // Extract the substring between the parentheses
                                    varname1 = inputString.substring(openingParenIndex + 1, closingParenIndex);
                                    itemname1= inputString.substring(0, openingParenIndex - 1);
                                    System.out.println("Extracted text: " + itemname1 +" & "+ varname1);
                                }
                                System.out.println("varient count: " + varcount);

                                if(varcount==0) {
                                    Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + cb.getText().toString() + "' AND out_of_stock = 'yes'", null);
                                    if (cursor.moveToFirst()) {
                                        String tax_na = cursor.getString(1);
                                        System.out.println("out of stock db " + tax_na);

                                        if (cb.getText().toString().equals(tax_na)) {
                                            cb1.setChecked(true);
                                            System.out.println("out of stock db " + cb.getText().toString());
                                        }
                                    }
                                    cursor.close();
                                } else {
                                    Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE variant1 = '" + varname1 + "' AND v1_oofstock = 'yes' AND itemname = '" + itemname1 + "'", null);
                                    if (cursor1.moveToFirst()) {
                                        String tax_na = cursor1.getString(1);
                                        String variname = cursor1.getString(38);

                                        TextView outstockvar = new TextView(New_OrderActivity_w.this);
                                        outstockvar.setText(variname);
                                        outstockvar.setText(tax_na+" ("+variname+")");
                                        System.out.println("out of stock db " + outstockvar.getText().toString());

                                        if (cb.getText().toString().equals(outstockvar.getText().toString())) {
                                            cb1.setChecked(true);
                                            System.out.println("out of stock db " + cb.getText().toString());
                                        }
                                    }
                                    cursor1.close();

                                    Cursor cursor2 = db.rawQuery("SELECT * FROM Items WHERE variant2 = '" + varname1 + "' AND v2_oofstock = 'yes' AND itemname = '" + itemname1 + "'", null);
                                    if (cursor2.moveToFirst()) {
                                        String tax_na = cursor2.getString(1);
                                        String variname = cursor2.getString(40);

                                        TextView outstockvar = new TextView(New_OrderActivity_w.this);
                                        outstockvar.setText(variname);
                                        outstockvar.setText(tax_na+" ("+variname+")");
                                        System.out.println("out of stock db " + outstockvar.getText().toString());

                                        if (cb.getText().toString().equals(outstockvar.getText().toString())) {
                                            cb1.setChecked(true);
                                            System.out.println("out of stock db " + cb.getText().toString());
                                        }
                                    }
                                    cursor2.close();

                                    Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE variant3 = '" + varname1 + "' AND v3_oofstock = 'yes' AND itemname = '" + itemname1 + "'", null);
                                    if (cursor3.moveToFirst()) {
                                        String tax_na = cursor3.getString(1);
                                        String variname = cursor3.getString(42);

                                        TextView outstockvar = new TextView(New_OrderActivity_w.this);
                                        outstockvar.setText(variname);
                                        outstockvar.setText(tax_na+" ("+variname+")");
                                        System.out.println("out of stock db " + outstockvar.getText().toString());

                                        if (cb.getText().toString().equals(outstockvar.getText().toString())) {
                                            cb1.setChecked(true);
                                            System.out.println("out of stock db " + cb.getText().toString());
                                        }
                                    }
                                    cursor3.close();

                                    Cursor cursor4 = db.rawQuery("SELECT * FROM Items WHERE variant4 = '" + varname1 + "' AND v4_oofstock = 'yes' AND itemname = '" + itemname1 + "'", null);
                                    if (cursor4.moveToFirst()) {
                                        String tax_na = cursor4.getString(1);
                                        String variname = cursor4.getString(44);

                                        TextView outstockvar = new TextView(New_OrderActivity_w.this);
                                        outstockvar.setText(variname);
                                        outstockvar.setText(tax_na+" ("+variname+")");
                                        System.out.println("out of stock db " + outstockvar.getText().toString());

                                        if (cb.getText().toString().equals(outstockvar.getText().toString())) {
                                            cb1.setChecked(true);
                                            System.out.println("out of stock db " + cb.getText().toString());
                                        }
                                    }
                                    cursor4.close();

                                    Cursor cursor5 = db.rawQuery("SELECT * FROM Items WHERE variant5 = '" + varname1 + "' AND v5_oofstock = 'yes' AND itemname = '" + itemname1 + "'", null);
                                    if (cursor5.moveToFirst()) {
                                        String tax_na = cursor5.getString(1);
                                        String variname = cursor5.getString(46);

                                        TextView outstockvar = new TextView(New_OrderActivity_w.this);
                                        outstockvar.setText(variname);
                                        outstockvar.setText(tax_na+" ("+variname+")");
                                        System.out.println("out of stock db " + outstockvar.getText().toString());

                                        if (cb.getText().toString().equals(outstockvar.getText().toString())) {
                                            cb1.setChecked(true);
                                            System.out.println("out of stock db " + cb.getText().toString());
                                        }
                                    }
                                    cursor5.close();

                                }
                            }

                            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long l) {

                                    TextView txtview = (TextView) view.findViewById(R.id.label);
                                    final String item = txtview.getText().toString();

                                    final CheckBox checkbox = (CheckBox) view.getTag(R.id.check);

                                    //String inputString = item.getText().toString();
                                    String varname1=null, itemname1=null;
                                    int varcount=0;
                                    // Find the opening and closing parentheses
                                    int openingParenIndex = item.indexOf('(');
                                    int closingParenIndex = item.indexOf(')');

                                    if (openingParenIndex != -1 && closingParenIndex != -1) {
                                        varcount++;
                                        // Extract the substring between the parentheses
                                        varname1 = item.substring(openingParenIndex + 1, closingParenIndex);
                                        itemname1= item.substring(0, openingParenIndex - 1);
                                        System.out.println("Extracted text: " + itemname1 +" & "+ varname1);
                                    }

                                    if (checkbox.isChecked()){
                                        checkbox.setChecked(false);

                                        if(varcount==0) {
                                            Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + item + "'", null);
                                            if (cursor1.moveToFirst()) {

                                                String id = cursor1.getString(0);
                                                String pri = cursor1.getString(2);
                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("out_of_stock", "");
                                                String where1 = "_id = '" + id + "' ";


                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                getContentResolver().update(contentUri, contentValues, where1, new String[]{});
                                                resultUri = new Uri.Builder()
                                                        .scheme("content")
                                                        .authority(StubProviderApp.AUTHORITY)
                                                        .path("Items")
                                                        .appendQueryParameter("operation", "update")
                                                        .appendQueryParameter("_id", id)
                                                        .build();
                                                getContentResolver().notifyChange(resultUri, null);


                                                //    db.update("Items", contentValues, where1, new String[]{});


                                                func_out_of_stock(id, pri, true);


                                            }
                                            cursor1.close();
                                        }else {

                                            Cursor cursor11 = db.rawQuery("SELECT * FROM Items WHERE variant1 = '" + varname1 + "' AND itemname = '" + itemname1 + "'", null);
                                            if (cursor11.moveToFirst()) {

                                                String id = cursor11.getString(0);
                                                String pri = cursor11.getString(39);
                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("v1_oofstock", "");
                                                String where1 = "_id = '" + id + "' ";

                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                getContentResolver().update(contentUri, contentValues, where1, new String[]{});
                                                resultUri = new Uri.Builder()
                                                        .scheme("content")
                                                        .authority(StubProviderApp.AUTHORITY)
                                                        .path("Items")
                                                        .appendQueryParameter("operation", "update")
                                                        .appendQueryParameter("_id", id)
                                                        .build();
                                                getContentResolver().notifyChange(resultUri, null);

                                                int viid=1;
                                                String Varrid=id+viid;
                                                func_out_of_stock_var(id, Varrid, pri, true);

                                            }
                                            cursor11.close();

                                            Cursor cursor12 = db.rawQuery("SELECT * FROM Items WHERE variant2 = '" + varname1 + "' AND itemname = '" + itemname1 + "'", null);
                                            if (cursor12.moveToFirst()) {

                                                String id = cursor12.getString(0);
                                                String pri = cursor12.getString(41);
                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("v2_oofstock", "");
                                                String where1 = "_id = '" + id + "' ";

                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                getContentResolver().update(contentUri, contentValues, where1, new String[]{});
                                                resultUri = new Uri.Builder()
                                                        .scheme("content")
                                                        .authority(StubProviderApp.AUTHORITY)
                                                        .path("Items")
                                                        .appendQueryParameter("operation", "update")
                                                        .appendQueryParameter("_id", id)
                                                        .build();
                                                getContentResolver().notifyChange(resultUri, null);

                                                int viid=2;
                                                String Varrid=id+viid;
                                                func_out_of_stock_var(id, Varrid, pri, true);

                                            }
                                            cursor12.close();

                                            Cursor cursor13 = db.rawQuery("SELECT * FROM Items WHERE variant3 = '" + varname1 + "' AND itemname = '" + itemname1 + "'", null);
                                            if (cursor13.moveToFirst()) {

                                                String id = cursor13.getString(0);
                                                String pri = cursor13.getString(43);
                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("v3_oofstock", "");
                                                String where1 = "_id = '" + id + "' ";

                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                getContentResolver().update(contentUri, contentValues, where1, new String[]{});
                                                resultUri = new Uri.Builder()
                                                        .scheme("content")
                                                        .authority(StubProviderApp.AUTHORITY)
                                                        .path("Items")
                                                        .appendQueryParameter("operation", "update")
                                                        .appendQueryParameter("_id", id)
                                                        .build();
                                                getContentResolver().notifyChange(resultUri, null);

                                                int viid=3;
                                                String Varrid=id+viid;
                                                func_out_of_stock_var(id, Varrid, pri, true);

                                            }
                                            cursor13.close();

                                            Cursor cursor14 = db.rawQuery("SELECT * FROM Items WHERE variant4 = '" + varname1 + "' AND itemname = '" + itemname1 + "'", null);
                                            if (cursor14.moveToFirst()) {

                                                String id = cursor14.getString(0);
                                                String pri = cursor14.getString(45);
                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("v4_oofstock", "");
                                                String where1 = "_id = '" + id + "' ";

                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                getContentResolver().update(contentUri, contentValues, where1, new String[]{});
                                                resultUri = new Uri.Builder()
                                                        .scheme("content")
                                                        .authority(StubProviderApp.AUTHORITY)
                                                        .path("Items")
                                                        .appendQueryParameter("operation", "update")
                                                        .appendQueryParameter("_id", id)
                                                        .build();
                                                getContentResolver().notifyChange(resultUri, null);

                                                int viid=4;
                                                String Varrid=id+viid;
                                                func_out_of_stock_var(id, Varrid, pri, true);

                                            }
                                            cursor14.close();

                                            Cursor cursor15 = db.rawQuery("SELECT * FROM Items WHERE variant5 = '" + varname1 + "' AND itemname = '" + itemname1 + "'", null);
                                            if (cursor15.moveToFirst()) {

                                                String id = cursor15.getString(0);
                                                String pri = cursor15.getString(47);
                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("v5_oofstock", "");
                                                String where1 = "_id = '" + id + "' ";

                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                getContentResolver().update(contentUri, contentValues, where1, new String[]{});
                                                resultUri = new Uri.Builder()
                                                        .scheme("content")
                                                        .authority(StubProviderApp.AUTHORITY)
                                                        .path("Items")
                                                        .appendQueryParameter("operation", "update")
                                                        .appendQueryParameter("_id", id)
                                                        .build();
                                                getContentResolver().notifyChange(resultUri, null);

                                                int viid=5;
                                                String Varrid=id+viid;
                                                func_out_of_stock_var(id, Varrid, pri, true);

                                            }
                                            cursor15.close();

                                        }
                                    }else {
                                        checkbox.setChecked(true);

                                        if(varcount==0) {
                                            Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + item + "'", null);
                                            if (cursor1.moveToFirst()) {
                                                String id = cursor1.getString(0);
                                                String pri = cursor1.getString(2);
                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("out_of_stock", "yes");
                                                String where1 = "_id = '" + id + "' ";


                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                getContentResolver().update(contentUri, contentValues, where1, new String[]{});
                                                resultUri = new Uri.Builder()
                                                        .scheme("content")
                                                        .authority(StubProviderApp.AUTHORITY)
                                                        .path("Items")
                                                        .appendQueryParameter("operation", "update")
                                                        .appendQueryParameter("_id", id)
                                                        .build();
                                                getContentResolver().notifyChange(resultUri, null);

                                                //       db.update("Items", contentValues, where1, new String[]{});

                                                func_out_of_stock(id, pri, false);


                                            }
                                            cursor1.close();
                                        }else{

                                            Cursor cursor11 = db.rawQuery("SELECT * FROM Items WHERE variant1 = '" + varname1 + "' AND itemname = '" + itemname1 + "'", null);
                                            if (cursor11.moveToFirst()) {

                                                String id = cursor11.getString(0);
                                                String pri = cursor11.getString(39);
                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("v1_oofstock", "yes");
                                                String where1 = "_id = '" + id + "' ";

                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                getContentResolver().update(contentUri, contentValues, where1, new String[]{});
                                                resultUri = new Uri.Builder()
                                                        .scheme("content")
                                                        .authority(StubProviderApp.AUTHORITY)
                                                        .path("Items")
                                                        .appendQueryParameter("operation", "update")
                                                        .appendQueryParameter("_id", id)
                                                        .build();
                                                getContentResolver().notifyChange(resultUri, null);

                                                int viid=1;
                                                String Varrid=id+viid;
                                                func_out_of_stock_var(id, Varrid, pri, false);

                                            }
                                            cursor11.close();

                                            Cursor cursor12 = db.rawQuery("SELECT * FROM Items WHERE variant2 = '" + varname1 + "' AND itemname = '" + itemname1 + "'", null);
                                            if (cursor12.moveToFirst()) {

                                                String id = cursor12.getString(0);
                                                String pri = cursor12.getString(41);
                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("v2_oofstock", "yes");
                                                String where1 = "_id = '" + id + "' ";

                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                getContentResolver().update(contentUri, contentValues, where1, new String[]{});
                                                resultUri = new Uri.Builder()
                                                        .scheme("content")
                                                        .authority(StubProviderApp.AUTHORITY)
                                                        .path("Items")
                                                        .appendQueryParameter("operation", "update")
                                                        .appendQueryParameter("_id", id)
                                                        .build();
                                                getContentResolver().notifyChange(resultUri, null);

                                                int viid=2;
                                                String Varrid=id+viid;
                                                func_out_of_stock_var(id, Varrid, pri, false);

                                            }
                                            cursor12.close();

                                            Cursor cursor13 = db.rawQuery("SELECT * FROM Items WHERE variant3 = '" + varname1 + "' AND itemname = '" + itemname1 + "'", null);
                                            if (cursor13.moveToFirst()) {

                                                String id = cursor13.getString(0);
                                                String pri = cursor13.getString(43);
                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("v3_oofstock", "yes");
                                                String where1 = "_id = '" + id + "' ";

                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                getContentResolver().update(contentUri, contentValues, where1, new String[]{});
                                                resultUri = new Uri.Builder()
                                                        .scheme("content")
                                                        .authority(StubProviderApp.AUTHORITY)
                                                        .path("Items")
                                                        .appendQueryParameter("operation", "update")
                                                        .appendQueryParameter("_id", id)
                                                        .build();
                                                getContentResolver().notifyChange(resultUri, null);

                                                int viid=3;
                                                String Varrid=id+viid;
                                                func_out_of_stock_var(id, Varrid, pri, false);

                                            }
                                            cursor13.close();

                                            Cursor cursor14 = db.rawQuery("SELECT * FROM Items WHERE variant4 = '" + varname1 + "' AND itemname = '" + itemname1 + "'", null);
                                            if (cursor14.moveToFirst()) {

                                                String id = cursor14.getString(0);
                                                String pri = cursor14.getString(45);
                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("v4_oofstock", "yes");
                                                String where1 = "_id = '" + id + "' ";

                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                getContentResolver().update(contentUri, contentValues, where1, new String[]{});
                                                resultUri = new Uri.Builder()
                                                        .scheme("content")
                                                        .authority(StubProviderApp.AUTHORITY)
                                                        .path("Items")
                                                        .appendQueryParameter("operation", "update")
                                                        .appendQueryParameter("_id", id)
                                                        .build();
                                                getContentResolver().notifyChange(resultUri, null);

                                                int viid=4;
                                                String Varrid=id+viid;
                                                func_out_of_stock_var(id, Varrid, pri, false);

                                            }
                                            cursor14.close();

                                            Cursor cursor15 = db.rawQuery("SELECT * FROM Items WHERE variant5 = '" + varname1 + "' AND itemname = '" + itemname1 + "'", null);
                                            if (cursor15.moveToFirst()) {

                                                String id = cursor15.getString(0);
                                                String pri = cursor15.getString(47);
                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("v5_oofstock", "yes");
                                                String where1 = "_id = '" + id + "' ";

                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                getContentResolver().update(contentUri, contentValues, where1, new String[]{});
                                                resultUri = new Uri.Builder()
                                                        .scheme("content")
                                                        .authority(StubProviderApp.AUTHORITY)
                                                        .path("Items")
                                                        .appendQueryParameter("operation", "update")
                                                        .appendQueryParameter("_id", id)
                                                        .build();
                                                getContentResolver().notifyChange(resultUri, null);

                                                int viid=5;
                                                String Varrid=id+viid;
                                                func_out_of_stock_var(id, Varrid, pri, false);

                                            }
                                            cursor15.close();

                                        }
                                    }

                                    adapter.notifyDataSetChanged();
                                }
                            });
                            adapter.notifyDataSetChanged();

                        }
                        if(id==R.id.linearLayout_menu_upload){
                            Toast.makeText(getApplicationContext(), "menu upload", Toast.LENGTH_SHORT).show();

                            try {

                                JSONObject  jsonArray = new JSONObject();
                                JSONArray entityarray = new JSONArray();
                                JSONArray mainCategories = new JSONArray();
                                JSONArray items = new JSONArray();
                                JSONObject entity = new JSONObject();


                                Cursor cursor12 = db.rawQuery("SELECT * FROM Hotel", null);
                                if (cursor12.moveToFirst()){
                                    do {
                                       // System.out.println("menu upload begins ");
                                        String c_id1 = cursor12.getString(0);
                                        int c_id2= Integer.parseInt(c_id1);
                                        c_id2=c_id2-2;
                                        TextView c_id = new TextView(New_OrderActivity_w.this);
                                        c_id.setText(String.valueOf(c_id2));
                                        String category1 = cursor12.getString(1);
                                        TextView category = new TextView(New_OrderActivity_w.this);
                                        category.setText(category1);

                                        if(category.getText().toString().equalsIgnoreCase("All") || category.getText().toString().equalsIgnoreCase("Favourites")){

                                        }else {
                                            JSONObject mainCategory = new JSONObject();
                                            int mcid= Integer.parseInt(c_id.getText().toString());
                                            mainCategory.put("id", mcid);
                                            mainCategory.put("name", category.getText().toString());
                                            mainCategory.put("description", null);
                                            mainCategory.put("order", null);
                                            JSONArray subCategories = new JSONArray();
                                            JSONObject subCategory = new JSONObject();
                                            subCategory.put("id", mcid);
                                            subCategory.put("name", category.getText().toString());
                                            subCategory.put("description", category.getText().toString());
                                            subCategory.put("order", null);
                                            subCategories.put(subCategory);
                                            mainCategory.put("sub_categories", subCategories);
                                            mainCategories.put(mainCategory);
                                            entity.put("main_categories", mainCategories);
                                            //jsonArray.put("entity",entity);
                                            }

//
//                                        System.out.println("menu category json array: "+mainCategories);
//                                        System.out.println("menu json array: "+jsonArray);
                                    }while (cursor12.moveToNext());
                                }
                                cursor12.close();


                                Cursor cursor = db.rawQuery("SELECT * FROM items", null);
                                if (cursor.moveToFirst()){
                                    do {
//                                        System.out.println("menu upload begins ");
                                        String id11 = cursor.getString(0);

                                        TextView id1 = new TextView(New_OrderActivity_w.this);
                                        id1.setText(id11);
                                        String itemname1 = cursor.getString(1);
                                        TextView itemname = new TextView(New_OrderActivity_w.this);
                                        itemname.setText(itemname1);
                                        String price1 = cursor.getString(2);
                                        String varavailable1 = cursor.getString(38);
                                        TextView varavailable = new TextView(New_OrderActivity_w.this);
                                        varavailable.setText(varavailable1);
                                        TextView price = new TextView(New_OrderActivity_w.this);
                                        if(varavailable.getText().toString().isEmpty()) {
                                            price.setText(price1);
                                        }else{
                                            price.setText("");
                                        }
                                        String categoryi = cursor.getString(4);
                                        TextView category = new TextView(New_OrderActivity_w.this);
                                        category.setText(categoryi);
                                        String categoryid1=null;
                                        TextView categoryid = new TextView(New_OrderActivity_w.this);
                                        Cursor cursorcat = db.rawQuery("SELECT * FROM Hotel", null);
                                        if (cursorcat.moveToFirst()){
                                            do {

                                                String categorycat1 = cursorcat.getString(1);
                                                TextView categorycat = new TextView(New_OrderActivity_w.this);
                                                categorycat.setText(categorycat1);
                                                if(category.getText().toString().equalsIgnoreCase(categorycat.getText().toString())){
                                                    categoryid1 = cursorcat.getString(0);
                                                    int categoryid2= Integer.parseInt(categoryid1);
                                                    categoryid2=categoryid2-2;
                                                    categoryid.setText(String.valueOf(categoryid2));
                                                }
                                            }while (cursorcat.moveToNext());
                                        }
                                        cursor12.close();
                                        String quanti1 = cursor.getString(3);
                                        TextView quanti = new TextView(New_OrderActivity_w.this);
                                        quanti.setText(quanti1);
                                        String unit1 = cursor.getString(26);
                                        TextView unit = new TextView(New_OrderActivity_w.this);
                                        unit.setText(unit1);
                                        String instock1 = cursor.getString(51);
                                        TextView instock = new TextView(New_OrderActivity_w.this);
                                        instock.setText(instock1);
                                        boolean stockflag = true;
                                        if (instock.getText().toString().isEmpty()) {
                                            stockflag = true;
                                        }else {
                                            if (instock.getText().toString().equalsIgnoreCase("yes")) {
                                                stockflag = false;
                                            } else if (instock.getText().toString().equalsIgnoreCase("no")) {
                                                stockflag = true;
                                            }
                                        }
                                        String isveg1 = cursor.getString(57);
                                        TextView isveg = new TextView(New_OrderActivity_w.this);
                                        isveg.setText(isveg1);

                                        String packingcharges1 = cursor.getString(58);
                                        TextView packingcharges = new TextView(New_OrderActivity_w.this);
                                        packingcharges.setText(packingcharges1);

                                        JSONObject itemdetails = new JSONObject();
                                        int iid= Integer.parseInt(id1.getText().toString());
                                        System.out.println("Items: "+itemname.getText().toString());
                                        int Cid= Integer.parseInt(categoryid.getText().toString());
                                        System.out.println("Category: "+Cid);
                                        itemdetails.put("id", iid);
                                        itemdetails.put("category_id", Cid);
                                        itemdetails.put("sub_category_id", Cid);
                                        itemdetails.put("name", itemname.getText().toString());
                                        if (isveg.getText().toString().isEmpty()) {
                                            itemdetails.put("is_veg", true);
                                        }else {
                                            itemdetails.put("is_veg", isveg.getText().toString());
                                        }
                                        itemdetails.put("image_url_swiggy", null);
                                        itemdetails.put("image_url_zomato", null);
                                        itemdetails.put("description", null);
                                        itemdetails.put("price", price.getText().toString());
                                        JSONArray gst_details = new JSONArray();
                                        JSONObject gstdetail = new JSONObject();
                                        TextView sgst = new TextView(New_OrderActivity_w.this);
                                        TextView cgst = new TextView(New_OrderActivity_w.this);
                                        float sgst1=0,cgst1=0;
                                        Cursor cursortax = db.rawQuery("SELECT * FROM Taxes", null);
                                        if (cursortax.moveToFirst()){
                                            do {
                                                String tax = cursortax.getString(1);
                                                String taxper = cursortax.getString(2);
                                                String taxname=null;
                                                System.out.println("Extracted tax name: " + taxper );
                                                TextView taxper1 = new TextView(New_OrderActivity_w.this);
                                                taxper1.setText(taxper);

                                                // Find the opening and closing parentheses
                                                int openingParenIndex = tax.indexOf('(');
                                                int closingParenIndex = tax.indexOf(')');

                                                if (openingParenIndex != -1 && closingParenIndex != -1) {
                                                    // Extract the substring between the parentheses
                                                    taxname= tax.substring(0, openingParenIndex);
                                                  //  System.out.println("Extracted tax name: " + taxname );
                                                    TextView taxname1 = new TextView(New_OrderActivity_w.this);
                                                    taxname1.setText(taxname);
                                                    if(taxname1.getText().toString().equalsIgnoreCase("sgst")){
                                                        sgst.setText(taxper1.getText().toString());
                                                        sgst1= Float.parseFloat(taxper);
                                                    }else if(taxname1.getText().toString().equalsIgnoreCase("cgst")){
                                                        cgst.setText(taxper1.getText().toString());
                                                        cgst1= Float.parseFloat(taxper);
                                                    }
                                                }
                                            }while (cursortax.moveToNext());
                                        }
                                        cursortax.close();
                                        String gst_liability=null;
                                        if((sgst.getText().toString().equalsIgnoreCase("2.5"))&& (cgst.getText().toString().equalsIgnoreCase("2.5"))){
                                            gst_liability="SWIGGY";
                                        }else{
                                            gst_liability="VENDOR";
                                        }

                                        System.out.println("Tax output SGST: "+ sgst.getText().toString()+"CGST: "+ sgst.getText().toString()+"Liability: "+gst_liability);
                                        gstdetail.put("igst", 0);
                                        gstdetail.put("sgst", sgst1);
                                        gstdetail.put("cgst", cgst1);
                                        gstdetail.put("inclusive", false);
                                        gstdetail.put("gst_liability", gst_liability);
                                        gst_details.put(gstdetail);
                                        itemdetails.put("gst_details",gstdetail);
                                        itemdetails.put("packing_charges", packingcharges.getText().toString());
                                        itemdetails.put("enable", 1);
                                        itemdetails.put("in_stock", stockflag);
                                        itemdetails.put("addon_free_limit", -1);
                                        itemdetails.put("addon_limit", -1);
                                        itemdetails.put("image_url", null);

                                        Cursor cursor1 = db.rawQuery("SELECT * FROM Working_hours", null);
                                        if (cursor1.moveToFirst()) {
                                            String opening1 = cursor1.getString(2);
                                            TextView opening = new TextView(New_OrderActivity_w.this);
                                            opening.setText(opening1);
                                            String closing1 = cursor1.getString(4);
                                            TextView closing = new TextView(New_OrderActivity_w.this);
                                            closing.setText(closing1);
                                            JSONArray item_slots = new JSONArray();
                                            JSONObject itemslots = new JSONObject();
                                            itemslots.put("open_time", opening.getText().toString());
                                            itemslots.put("close_time", closing.getText().toString());
                                            itemslots.put("day_of_week", null);
                                            item_slots.put(itemslots);
                                            itemdetails.put("item_slots", item_slots);
                                        }
                                        cursor1.close();


                                        int vargrid=1;
                                        if(varavailable.getText().toString().isEmpty()) {
                                            String nullvg=null;
                                            JSONArray variant_groups = new JSONArray();
                                            itemdetails.put("variant_groups", variant_groups);
                                        }else {

                                            JSONArray variant_groups = new JSONArray();
                                            JSONObject variantgroups1 = new JSONObject();
                                            variantgroups1.put("id", vargrid);
                                            variantgroups1.put("name", "Size");
                                            variantgroups1.put("order", null);
                                            JSONArray variants = new JSONArray();
                                            //JSONObject variantgroups = new JSONObject();47
                                            int varid = 1;
                                            int OoS = 59;
                                            for (int m = 38; m < 48; m = m + 2) {
//                                            System.out.println("variants upload begins ");
                                                String variant1 = cursor.getString(m);
                                                String variantid = id11 + varid;
                                                TextView variantid1 = new TextView(New_OrderActivity_w.this);
                                                variantid1.setText(variantid);
                                                int variid = Integer.parseInt(variantid1.getText().toString());

                                                TextView variant = new TextView(New_OrderActivity_w.this);
                                                variant.setText(variant1);
                                                String var_out_of_stock1 = cursor.getString(OoS);
                                                TextView var_out_of_stock = new TextView(New_OrderActivity_w.this);
                                                var_out_of_stock.setText(var_out_of_stock1);
                                                boolean vstockflag = true;
                                                if (var_out_of_stock.getText().toString().isEmpty()) {
                                                    vstockflag = true;
                                                } else {
                                                    if (var_out_of_stock.getText().toString().equalsIgnoreCase("yes")) {
                                                        vstockflag = false;
                                                    } else if (var_out_of_stock.getText().toString().equalsIgnoreCase("no")) {
                                                        vstockflag = true;
                                                    }
                                                }
                                                if (variant.getText().toString().isEmpty()) {

                                                } else {
                                                    String vprice1 = cursor.getString(m + 1);
                                                    TextView vprice = new TextView(New_OrderActivity_w.this);
                                                    vprice.setText(vprice1);


                                                    JSONObject variants1 = new JSONObject();
                                                    variants1.put("id", variid);
                                                    variants1.put("name", variant.getText().toString());
                                                    variants1.put("price", vprice.getText().toString());
                                                    variants1.put("default", null);
                                                    variants1.put("order", null);
                                                    variants1.put("in_stock", vstockflag);
                                                    if (isveg.getText().toString().isEmpty()) {
                                                        variants1.put("is_veg", true);
                                                    } else {
                                                        variants1.put("is_veg", isveg.getText().toString());
                                                    }
                                                    variants1.put("is_veg", isveg.getText().toString());
                                                    /*JSONArray vgst_details = new JSONArray();
                                                    JSONObject vgstdetails = new JSONObject();
                                                    vgstdetails.put("igst", null);
                                                    vgstdetails.put("sgst", null);
                                                    vgstdetails.put("cgst", null);
                                                    vgstdetails.put("inclusive", null);
                                                    vgstdetails.put("gst_liability", null);
                                                    vgst_details.put(vgstdetails);
                                                    variants1.put("gst_details", vgst_details);*/
                                                    variants.put(variants1);

                                                    OoS++;
                                                    varid++;
                                                }
                                            }
                                            variantgroups1.put("variants", variants);
                                            variant_groups.put(variantgroups1);
                                            itemdetails.put("variant_groups", variant_groups);


                                            JSONArray pricing_combinations = new JSONArray();

                                            varid = 1;
                                            for (int m = 38; m < 48; m = m + 2) {
                                                String variant1 = cursor.getString(m);
                                                String variantid = id11 + varid;
                                                TextView variantid1 = new TextView(New_OrderActivity_w.this);
                                                variantid1.setText(variantid);
                                                int variid = Integer.parseInt(variantid1.getText().toString());
                                                TextView variant = new TextView(New_OrderActivity_w.this);
                                                variant.setText(variant1);
//                                            System.out.println("variants combination upload begins "+ variant);
                                                if (variant.getText().toString().isEmpty()) {
                                                } else {
                                                    JSONObject pricingcombinations = new JSONObject();
                                                    String vprice1 = cursor.getString(m + 1);
                                                    TextView vprice = new TextView(New_OrderActivity_w.this);
                                                    vprice.setText(vprice1);
                                                    pricingcombinations.put("price", vprice.getText().toString());
                                                    JSONArray variant_combination = new JSONArray();
                                                    JSONObject variantcombination = new JSONObject();
                                                    variantcombination.put("variant_group_id", vargrid);
                                                    variantcombination.put("variant_id", variid);
                                                    variant_combination.put(variantcombination);
                                                    pricingcombinations.put("addon_combination", null);
                                                    pricingcombinations.put("variant_combination", variant_combination);
                                                    pricing_combinations.put(pricingcombinations);
                                                    varid++;
                                                }
                                            }

                                            itemdetails.put("pricing_combinations", pricing_combinations);
                                        }

                                        itemdetails.put("order", null);
                                        itemdetails.put("recommended", false);
                                        JSONArray catalog_attributes = new JSONArray();
                                        JSONObject catalogattributes = new JSONObject();
                                        catalogattributes.put("spice_level", null);
                                        catalogattributes.put("sweet_level", null);
                                        catalogattributes.put("gravy_property", null);
                                        catalogattributes.put("bone_property", null);
                                        catalogattributes.put("contain_seasonal_ingredients", null);
                                        catalogattributes.put("accompaniments", null);
                                        JSONArray quantity = new JSONArray();
                                        JSONObject quant = new JSONObject();
                                        quant.put("value", quanti.getText().toString());
                                        quant.put("unit", unit.getText().toString());
                                        quantity.put(quant);
                                        catalogattributes.put("quantity",quantity);
                                        catalogattributes.put("serves_how_many", null);
                                        catalog_attributes.put(catalogattributes);
//                                        itemdetails.put("catalog_attributes", catalogattributes);
                                        String nullca=null;
                                        itemdetails.put("catalog_attributes", "");



                                        items.put(itemdetails);

                                        //items.put(variant_groups);
                                        entity.put("items", items);




                                         /*JSONObject json1 = new JSONObject();
                                         json1.put("id", id1);
                                         json1.put("item_name", itemname);
                                         json1.put("price", price);
                                         json1.put("price", price);
                                         json1.put("active", "true");
                                         jsonArray.put(json1);*/
                                        // entityarray.put(entity);
                                        //  jsonArray.put("entity",entity);
//                                        System.out.println("menu entity : "+entity);
//                                        System.out.println("menu individual json array: "+items);
//                                        System.out.println("menu json array: "+jsonArray);
                                    }while (cursor.moveToNext());
                                }
                                cursor.close();

                                jsonArray.put("entity",entity);


                                json2= new JSONObject();
                                json2.put("merchant_id",str_merch_id);
                                json2.put("menu",jsonArray);
//                                System.out.println(" Final menu json array: "+json2);

                                String jsonString = json2.toString();

                            }catch (JSONException ex){
                                ex.printStackTrace();
                            }

                            RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url_menuupload, json2,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d("TAG", response.toString());
                                            System.out.println("Upload response: " + response.toString());
                                            Toast.makeText(New_OrderActivity_w.this,"menu uploaded", Toast.LENGTH_LONG).show();
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("TAG", error.getMessage(), error);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                             /*Toast.makeText(getApplicationContext(),
                                                             "Json parsing error: " + error.getMessage(),
                                                             Toast.LENGTH_LONG)
                                                     .show();*/
                                            System.out.println("Json parsing error: " + error.getMessage());
                                        }
                                    });
                                }
                            }) { //no semicolon or coma
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("Content-Type", "X-Wera-Api-Key");
                                    params.put("X-Wera-Api-Key", "9b1bc8d1-99d2-4597-aa7e-64e0a9580c10");
                                    params.put("Accept", "");
                                    return params;
                                }
                            };
                            mQueue.add(jsonObjectRequest);

                        }
                        if(id==R.id.linearLayout_availablity){

                            final Dialog dialog = new Dialog(New_OrderActivity_w.this, R.style.timepicker_date_dialog);
                            dialog.setContentView(R.layout.availablity_w);
                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            Switch switchButton = dialog.findViewById(R.id.switch1);
                            try {
                                //I try to use this for send Header is application/json
                                jsonBody = new JSONObject();
                                jsonBody.put("merchant_id", str_merch_id);
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }

                            RequestQueue mQueuea = Volley.newRequestQueue(getApplicationContext());

                            JsonObjectRequest jsonarraRequest= new JsonObjectRequest(restaurant_open_status, jsonBody, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject responsea) {

                                    Log.d("TAG", responsea.toString());
                                    //Toast.makeText(New_OrderActivity_w.this,"Store is open now", Toast.LENGTH_LONG).show();
                                    // System.out.println("Availablity response " + responsea);
                                    try {

                                        JSONObject jsonObject = new JSONObject(String.valueOf(responsea));
                                        JSONArray detailsArray = jsonObject.getJSONArray("details");
                                        if (detailsArray.length() > 0) {
                                            JSONObject firstDetail = detailsArray.getJSONObject(0);
                                            boolean status = firstDetail.getBoolean("status");
                                            System.out.println("Store Status: " + status);
                                            if(status==true){
                                                switchButton.setChecked(true);
                                            }else{
                                                switchButton.setChecked(false);
                                            }
                                        } else {
                                            System.out.println("No details found.");
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("Json parsing error: " + error.getMessage());
                                }
                            }){ //no semicolon or coma
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("Content-Type", "X-Wera-Api-Key");
                                    params.put("X-Wera-Api-Key", "9b1bc8d1-99d2-4597-aa7e-64e0a9580c10");
                                    params.put("Accept", "");
                                    return params;
                                }
                            };
                            mQueuea.add(jsonarraRequest);
                            dialog.show();

                            LinearLayout LinearLayout = (android.widget.LinearLayout) dialog.findViewById(R.id.avback_activity);
                            LinearLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });



                            // Add an event listener to handle switch state changes
                            switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                if (isChecked) {
                                    // Switch is ON
                                   // switchButton.setChecked(true);
                                  restaurant_availablity(true);
                                } else {
                                    // Switch is OFF
                                    restaurant_availablity(false);
                                    /*try {
                                        //I try to use this for send Header is application/json
                                        jsonBody = new JSONObject();
                                        jsonBody.put("merchant_id", str_merch_id);
                                        jsonBody.put("status", false);
                                        //jsonBody.put("item_ids", arrayList);
                                        jsonBody.put("reason", "Store timing");
                                        jsonBody.put("aggregator", 0);
                                    } catch (JSONException ex) {
                                        ex.printStackTrace();
                                    }

                                    RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

                                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(restaurant_open, jsonBody,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    Log.d("TAG", response.toString());
                                                    Toast.makeText(New_OrderActivity_w.this,"Store is open now", Toast.LENGTH_LONG).show();
                                                    System.out.println("Availablity response " + response);
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("TAG", error.getMessage(), error);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    System.out.println("Json parsing error: " + error.getMessage());
                                                }
                                            });
                                        }
                                    }) { //no semicolon or coma
                                        @Override
                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<String, String>();
                                            params.put("Content-Type", "X-Wera-Api-Key");
                                            params.put("X-Wera-Api-Key", "9b1bc8d1-99d2-4597-aa7e-64e0a9580c10");
                                            params.put("Accept", "");
                                            return params;
                                        }
                                    };
                                    mQueue.add(jsonObjectRequest);*/
                                }
                            });
                        }
                        return true;
                    }
                });
            }
        });
    }

    private void restaurant_availablity(boolean b) {
        try {
            //I try to use this for send Header is application/json
            jsonBody = new JSONObject();
            jsonBody.put("merchant_id", str_merch_id);
            jsonBody.put("status", b);
            //jsonBody.put("item_ids", arrayList);
            jsonBody.put("reason", "Store timing");
            jsonBody.put("aggregator", 0);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(restaurant_open, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                        Toast.makeText(New_OrderActivity_w.this,"Store is open now", Toast.LENGTH_LONG).show();
                        System.out.println("Availablity response " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Json parsing error: " + error.getMessage());
                    }
                });
            }
        }) { //no semicolon or coma
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "X-Wera-Api-Key");
                params.put("X-Wera-Api-Key", "9b1bc8d1-99d2-4597-aa7e-64e0a9580c10");
                params.put("Accept", "");
                return params;
            }
        };
        mQueue.add(jsonObjectRequest);
    }

    public void newbackend() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 10 seconds



                progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
                progressBar.setVisibility(View.VISIBLE);
                recyclerView = (RecyclerView) findViewById(R.id.newrecycle);
                final TextView preparing_orders_value = (TextView) findViewById(R.id.preparing_orders_valuen);
                final TextView cancelorder_value = (TextView) findViewById(R.id.corder_valuen);

                i_ord = 0;
                c_ord = 0;


                RequestQueue mQueue1 = Volley.newRequestQueue(getApplicationContext());
                StringRequest strord= new StringRequest(POST, weraorderdetails,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response1) {
                                try {
                             /*response1 = response1.replace("\"","'");
                             JSONObject jo = new JSONObject(response1.substring(1,response1.length()-1));*/
                                    JSONArray orddetails = new JSONArray(response1);
                                    //String orddetails = String.valueOf(response1);
                                    DataModel_w id = null;
                                    ArrayList arrayList= new ArrayList<>();
                                    // arrayList.clear();
                                    for (int k = 0; k < orddetails.length(); k++) {
                                        JSONObject c = orddetails.getJSONObject(k);
                                        // System.out.println("inloop backend response: " + c + "  " + response1);
                                        String orderid = c.getString("_id");
                                        String order_from = c.getString("order_from");
                                        String status = c.getString("order_status");
                                        if(status.equalsIgnoreCase("auto-accept")){
                                            status="Accepted";
                                        }
                                        if (status.toString().equalsIgnoreCase("Preparing") || status.toString().equalsIgnoreCase("Accepted") || status.toString().equalsIgnoreCase("auto-accept")) {
                                            i_ord++;
                                            preparing_orders_value.setText(String.valueOf(i_ord));
                                        }

                                        if (status.contains("Cancelled") || status.equalsIgnoreCase("timeout") || status.equalsIgnoreCase("reject") || status.equalsIgnoreCase("rejected") || status.contains("cancel")) {
                                            // if (ordid.equalsIgnoreCase(finalOrderid)) {
                                            c_ord++;
                                            cancelorder_value.setText(String.valueOf(c_ord));
                                        }


                                        if (status.toString().equalsIgnoreCase("Pending")) {
                                            String date_time = c.getString("order_date_time");
                                            // date_time=convertUnixTime(Long.parseLong(date_time));
                                            // System.out.println("Order no " + c);
                                            String payme_type = c.getString("payment_mode");

                                            String restaurant_id = c.getString("restaurant_id");
                                            String restaurant_name = c.getString("restaurant_name");
                                            String external_order_id = c.getString("external_order_id");
                                            String restaurant_address = c.getString("restaurant_address");
                                            String restaurant_number = c.getString("restaurant_number");

                                            //String order_status = c.getString("status");

                                            String enable_delivery = c.getString("enable_delivery");
                                            String net_amount = c.getString("net_amount");
                                            String gross_amount = c.getString("gross_amount");
                                            String order_instructions = c.getString("order_instructions");
                                            String order_gst = c.getString("gst");
                                            String order_cgst = c.getString("cgst");
                                            String order_sgst = c.getString("sgst");
                                            // String ord_cgst_percent = c.getString("cgst_percent");
                                            // String ord_sgst_percent = c.getString("sgst_percent");
                                            String packaging = c.getString("packaging");
                                            String order_packaging = c.getString("order_packaging");
                                            String order_pckg_cgst = c.getString("order_packaging_cgst");
                                            String order_pckg_sgst = c.getString("order_packaging_sgst");
                                            String order_pckg_cgst_per = c.getString("order_packaging_cgst_percent");
                                            String order_pckg_sgst_per = c.getString("order_packaging_sgst_percent");
                                            String packaging_cgst = c.getString("packaging_cgst");
                                            String packaging_sgst = c.getString("packaging_sgst");
                                            String packaging_cgst_percent = c.getString("packaging_cgst_percent");
                                            String packaging_sgst_percent = c.getString("packaging_sgst_percent");

                                            // String order_gst_per = c.getString("gst_percent");
                                            String delivery_charge = c.getString("delivery_charge");
                                            /** Documented 'type' is 'discount_reason' and 'amount' is 'discount' in JSON data **/
                                            String discount_type = c.getString("discount_reason");
                                            String discount_amount = c.getString("discount");

                                            String no_items = c.getString("num_item");
                                            //String order_from = c.getString("order_from");
                                            String order_cancelreason = c.getString("order_reason");
                                            String deli_per_name = c.getString("rider_name");
                                            String deli_per_phon = c.getString("rider_number");
                                            String deli_per_status = c.getString("rider_status");
                                            String deli_arrivetime = c.getString("time_to_arrive");
                                            String cust_name1 = c.getString("cus_name");
                                            String cust_phno = c.getString("cus_phone_number");
                                            String cust_email = c.getString("cus_email");
                                            String cust_address = c.getString("cus_address");
                                            String cust_deliv_area = c.getString("cus_delivery_area");
                                            String cust_deli_instru = c.getString("cus_address_instructions");

                                            //JSONObject items=c.getJSONObject("orderItemsJSON");
                                            JSONArray cart = c.getJSONArray("orderItemsJSON");
                                            System.out.println("inloop inner items response: " + cart);

                                            for (int j = 0; j < cart.length(); j++) {
                                                JSONObject items = cart.getJSONObject(j);
//                                         JSONObject items=cart;

                                                String itemid = items.getString("wera_item_id");
                                                //  System.out.println("item: " + itemid);
                                                String item_id = items.getString("item_id");
                                                String item_name = items.getString("item_name");
                                                String item_unit_price = items.getString("item_unit_price");
                                                String subtotal = items.getString("subtotal");
                                                String item_quantity = items.getString("item_quantity");
                                                String item_gst = items.getString("gst");
                                                String item_gst_per = items.getString("gst_percent");
                                                String item_packaging = items.getString("packaging");
                                                String item_dish = items.getString("dish");
                                                String instructions = items.getString("instructions");
                                                String discount = items.getString("discount");
                                                String item_cgst = items.getString("cgst");
                                                String item_sgst = items.getString("sgst");
                                                String item_cgst_per = items.getString("cgst_percent");
                                                String item_sgst_per = items.getString("sgst_percent");
                                                String item_packaging_gst = items.getString("packaging_gst");
                                                String item_packaging_sgst = items.getString("packaging_sgst");
                                                String item_packaging_cgst = items.getString("packaging_cgst");
                                                String item_cgst_percent = items.getString("packaging_cgst_percent");
                                                String item_sgst_percent = items.getString("packaging_sgst_percent");

                                                // for Variants array
                                                JSONArray variant, addons;
                                                try {
                                                    variant = items.getJSONArray("variants");
                                                } catch (JSONException e) {
                                                    System.out.println("Json error variants"+ e);
                                                    throw new RuntimeException(e);
                                                }
                                                for (int p = 0; p < variant.length(); p++) {
                                                    JSONObject var = variant.getJSONObject(p);
                                                    try {
                                                        String var_size_id = var.getString("size_id");
                                                        String var_size_name = var.getString("size_name");
                                                        String var_price = var.getString("price");
                                                        String var_cgst = var.getString("cgst");
                                                        String var_sgst = var.getString("sgst");
                                                        String var_cgst_percent = var.getString("cgst_percent");
                                                        String var_sgst_percent = var.getString("sgst_percent");
                                                    } catch (JSONException e) {
                                                        System.out.println("Json error variants"+ e);
                                                        throw new RuntimeException(e);
                                                    }
                                                }


                                                try {
                                                    addons = items.getJSONArray("addons");
                                                } catch (JSONException e) {
                                                    System.out.println("Json error Addons");
                                                    throw new RuntimeException(e);
                                                }
                                                for (int l = 0; l < addons.length(); l++) {
                                                    JSONObject addon = addons.getJSONObject(l);
                                                    try {
                                                        String addon_id = addon.getString("addon_id");
                                                        String addon_name = addon.getString("name");
                                                        String addon_price = addon.getString("price");
                                                        String addon_cgst = addon.getString("cgst");
                                                        String addon_sgst = addon.getString("sgst");
                                                        String addon_cgst_percent = addon.getString("cgst_percent");
                                                        String addon_sgst_percent = addon.getString("sgst_percent");
                                                    } catch (JSONException e) {
                                                        System.out.println("Json error Addons");
                                                        throw new RuntimeException(e);

                                                    }

                                                }

                                            }


                                            if (status.toString().equalsIgnoreCase("pending")) {
                                                id = new DataModel_w(orderid, String.valueOf(cart.length()), gross_amount, order_from, cust_name1, cust_phno, cust_address, cust_deli_instru, cust_deliv_area, cust_email, status, "", deli_per_name, deli_per_phon, deli_per_status,cart,order_instructions, str_merch_id, deli_arrivetime);
                                                arrayList.add(id);
                                                System.out.println("Item"+"recycler orderid: " + orderid + "\nType of order: " + "\nOrder Status: " + status + order_from + "\nCustomer name: " + cust_name1 + "\nNumber of items: " + String.valueOf(cart.length()));
                                            }
                                            // updateweraindextable(orderid,status,String.valueOf(cart.length()));
                                        }
                                    }

                                    progressBar.setVisibility(View.GONE);
                                    RecyclerViewAdapter_w adapter = new RecyclerViewAdapter_w(New_OrderActivity_w.this, arrayList, New_OrderActivity_w.this);
                                    recyclerView.setAdapter(adapter);c_ord=0;i_ord=0;



                                    final String Text = all_s_z_ue_fp.getSelectedItem().toString();

                                    if (Text.equals("All")) {
                                        // Toast.makeText(New_OrderActivity_w.this, "ALL", Toast.LENGTH_LONG).show();
                                    }

                                    all_s_z_ue_fp.setSelection(0, false);
                                    all_s_z_ue_fp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            String text1 = parent.getItemAtPosition(position).toString();
                                            if (text1.toString().equals("All")) {
                                                //Toast.makeText(New_OrderActivity_w.this, "Selected All", Toast.LENGTH_LONG).show();
                                                //all_new_orders();
                                            } else {
                                                if (text1.toString().equals("Online")) {
                                                    //Toast.makeText(New_OrderActivity_w.this, "Online", Toast.LENGTH_LONG).show();
                                                    custom_new_orders("Online");
                                                } else {
                                                    if (text1.toString().equals("Swiggy")) {
                                                        //Toast.makeText(New_OrderActivity_w.this, "Swiggy", Toast.LENGTH_LONG).show();
                                                        custom_new_orders("swiggy");
                                                    } else {
                                                        if (text1.toString().equals("Zomato")) {
                                                            //Toast.makeText(New_OrderActivity_w.this, "Zomato", Toast.LENGTH_LONG).show();
                                                            custom_new_orders("zomato");
                                                        } /*else {
                                                    if (text1.toString().equals("Uber eats")) {
                                                        Toast.makeText(New_OrderActivity_w.this, "Uber eats", Toast.LENGTH_LONG).show();
                                                        custom_new_orders("uber");
                                                    } else {
                                                        if (text1.toString().equals("Food panda")) {
                                                            Toast.makeText(New_OrderActivity_w.this, "Food panda", Toast.LENGTH_LONG).show();
                                                            custom_new_orders("food panda");
                                                        }
                                                        }
                                                        }*/

                                                    }
                                                }
                                            }
                                        }


                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });


                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("New order page", "Error: " + error.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                        /*Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + error.getMessage(),
                                        Toast.LENGTH_LONG)
                                .show();*/
                                System.out.println("Json parsing error: " + error.getMessage());
                            }
                        });
                    }
                }){
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String,String> windex = new HashMap<String, String>();
                        windex.put("db_name", company + "_"+ store + "_"+ device);
                        //windex.put("order_id", order_info_id);
                        //windex.put("order_status", "Pending");
                        return windex;
                    }
                };
                mQueue1.add(strord);
            }
        }, 600);

    }
    private void cancelcount() {
        final TextView cancelorder_value = (TextView) findViewById(R.id.corder_valuen);

        i_ord = 0;
        System.out.println("Cancelled order counter ");

        RequestQueue mQueue2 = Volley.newRequestQueue(getApplicationContext());
        //String finalOrderid = orderid;
        StringRequest strord = new StringRequest(POST, weraorderdetails, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                DataModel_w id = null;
                ArrayList orddatadb = new ArrayList<>();
                System.out.println("inloop " + response);
                //for(int i=0;i<response.length();i++) {
                try {
                    //JSONObject ord = new JSONObject(response);
                    JSONArray orddetails = new JSONArray(response);
                    for (int k = 0; k < orddetails.length(); k++) {
                        JSONObject c = orddetails.getJSONObject(k);
                        String ordid = c.getString("_id");
                        String order_status = c.getString("order_status");
                        if (order_status.contains("Cancelled") || order_status.equalsIgnoreCase("timeout") || order_status.equalsIgnoreCase("reject") || order_status.equalsIgnoreCase("rejected") || order_status.contains("cancel")) {
                            // if (ordid.equalsIgnoreCase(finalOrderid)) {
                            i_ord++;
                            cancelorder_value.setText(String.valueOf(i_ord));
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                // }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Cancel order details: ", "Error: " + error.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                                                    /*Toast.makeText(getApplicationContext(),
                                                                    "Json parsing error: " + error.getMessage(),
                                                                    Toast.LENGTH_LONG)
                                                            .show();*/
                        System.out.println("Cancel order details: " + error.getMessage());
                    }
                });
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> windex = new HashMap<String, String>();
                windex.put("db_name", company + "_" + store + "_" + device);
                //windex.put("order_id", order_info_id1);
                //windex.put("order_status", status);
                return windex;
            }
        };
        mQueue2.add(strord);

    }
    private void addorderdatadb(DataModel_w order) {

        ContentValues params = new ContentValues();
        params.put("_id", order.dborder_info_id);
        params.put("order_type", order.dborder_type1);
        params.put("order_date_time", order.dbdate_time);
        params.put("payment_mode", order.dbpayme_type);
        params.put("restaurant_id", order.dbrestaurant_id);
        params.put("restaurant_name", order.dbrestaurant_name);
        params.put("external_order_id", order.dbexternal_order_id);
        params.put("restaurant_address", order.dbrestaurant_address);
        params.put("restaurant_number", order.dbrestaurant_number);
        params.put("order_from", order.dborder_from);
        params.put("enable_delivery", order.dbenable_delivery);
        params.put("net_amount", order.dbnet_amount);
        params.put("gross_amount", order.dbgross_amount);
        params.put("order_instructions", order.dborder_instructions);
        params.put("gst", order.dborder_gst);
        params.put("cgst", order.dborder_cgst);
        params.put("sgst", order.dborder_sgst);
        params.put("packaging", order.dbpackaging);
        params.put("order_packaging", order.dborder_packaging);
        params.put("packaging_cgst_percent", order.dbpackaging_cgst_percent);
        params.put("packaging_sgst_percent", order.dbpackaging_sgst_percent);
        params.put("packaging_cgst", order.dbpackaging_cgst);
        params.put("packaging_sgst", order.dbpackaging_sgst);
        params.put("order_packaging_cgst", order.dborder_pckg_cgst);
        params.put("order_packaging_sgst", order.dborder_pckg_sgst);
        params.put("order_packaging_cgst_percent", order.dborder_pckg_cgst_per);
        params.put("order_packaging_sgst_percent", order.dborder_pckg_sgst_per);
        params.put("delivery_charge", order.dbdelivery_charge);
        params.put("discount_reason", order.dbdiscount_type);
        params.put("discount", order.dbdiscount_amount);
        params.put("cus_name", order.dbcust_name1);
        params.put("cus_phone_number", order.dbcust_phno);
        params.put("cus_email", order.dbcust_email);
        params.put("cus_address", order.dbcust_address);
        params.put("cus_delivery_area", order.dbcust_deliv_area);
        params.put("cus_address_instructions", order.dbcust_deli_instru);
        params.put("num_item", order.dbno_items);
        params.put("order_status", order.dorder_status);
        Log.d("TAG", "inside DBsection");
        String order_info_id=order.dborder_info_id;

        System.out.println("order_id is "+order.dborder_info_id);


        Cursor ffifteen_1 = db1.rawQuery("SELECT * FROM online_order_wera WHERE _id = '" + order_info_id + "'", null);
        if (ffifteen_1.moveToFirst()) {
            String where11 = "_id = '" + order_info_id + "' ";
            Log.d("Wera table", "Update");
            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "online_order_wera");
            getContentResolver().update(contentUri, params,where11,new String[]{});
            resultUri = new Uri.Builder()
                    .scheme("content")
                    .authority(StubProvider.AUTHORITY)
                    .path("online_order_wera")
                    .appendQueryParameter("operation", "update")
                    .appendQueryParameter("_id",order_info_id)
                    .build();
            getContentResolver().notifyChange(resultUri, null);
//                            db1.update("Billnumber", contentValues, where11, new String[]{});
        }else {
            Log.d("Wera table", "Insert");
            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "online_order_wera");
            resultUri = getContentResolver().insert(contentUri, params);
            getContentResolver().notifyChange(resultUri, null);
        }
        ffifteen_1.close();

        Log.d("TAG", "End of DBsection");
    }
    public void updateorderdatadb(String dborder_info_id, String status, String no_items) {


        ContentValues params = new ContentValues();
        params.put("_id", dborder_info_id);
        params.put("order_status", status);
        params.put("num_item", no_items);
        Log.d("TAG", "status Update DBsection");

        Cursor ffifteen_1 = db1.rawQuery("SELECT * FROM online_order_wera WHERE _id = '" + dborder_info_id + "'", null);
        if (ffifteen_1.moveToFirst()) {
            String where11 = "_id = '" + dborder_info_id + "' ";
            Log.d("Wera table", "Update");
            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "online_order_wera");
            getContentResolver().update(contentUri, params,where11,new String[]{});
            resultUri = new Uri.Builder()
                    .scheme("content")
                    .authority(StubProvider.AUTHORITY)
                    .path("online_order_wera")
                    .appendQueryParameter("operation", "update")
                    .appendQueryParameter("_id",dborder_info_id)
                    .build();
            getContentResolver().notifyChange(resultUri, null);

//                            db1.update("Billnumber", contentValues, where11, new String[]{});
        }else {
            Log.d("Wera table", "Insert");
            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "online_order_wera");
            resultUri = getContentResolver().insert(contentUri, params);
            getContentResolver().notifyChange(resultUri, null);
        }
        ffifteen_1.close();
        Log.d("TAG", "End of status update DBsection");

    }
    private void weraindextable(String dborder_info_id, String dorder_status, String dbrestaurant_id) {

        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                saveorder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Order Indexing", "Data passed to indexing");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Order indexing", "Error: " + error.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                /*Toast.makeText(getApplicationContext(),
                                                "Json parsing error: " + error.getMessage(),
                                                Toast.LENGTH_LONG)
                                        .show();*/
                                System.out.println("Json parsing error: " + error.getMessage());
                            }
                        });
                    }
                }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> windex = new HashMap<String, String>();
                windex.put("order_id", dborder_info_id);
                windex.put("restaurant_id", dbrestaurant_id);
                windex.put("order_status", dorder_status);
                windex.put("db_name", company + "_"+ store + "_"+ device);

                return windex;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);
    }

    public void updateweraindextable(String dborder_info_id, String status1, String no_items) {
//------------------------------Updating local database----------------------------------------------
        ContentValues params = new ContentValues();
        params.put("_id", dborder_info_id);
        params.put("order_status", status1);
        //  params.put("num_item", no_items);
        Cursor ffifteen_1 = db1.rawQuery("SELECT * FROM online_order_wera WHERE _id = '" + dborder_info_id + "'", null);
        if (ffifteen_1.moveToFirst()) {
            String where11 = "_id = '" + dborder_info_id + "' ";
            Log.d("Wera table", "Update");
            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "online_order_wera");
            getContentResolver().update(contentUri, params,where11,new String[]{});
            resultUri = new Uri.Builder()
                    .scheme("content")
                    .authority(StubProvider.AUTHORITY)
                    .path("online_order_wera")
                    .appendQueryParameter("operation", "update")
                    .appendQueryParameter("_id",dborder_info_id)
                    .build();
            getContentResolver().notifyChange(resultUri, null);

//                            db1.update("Billnumber", contentValues, where11, new String[]{});
        }else {
            Log.d("Wera table", "Insert");
            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "online_order_wera");
            resultUri = getContentResolver().insert(contentUri, params);
            getContentResolver().notifyChange(resultUri, null);
        }
        ffifteen_1.close();

//----------------------------------Pushing data to wera index table
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest sr = new StringRequest(
                POST,
                updateorder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Updating Order Indexing", "Data updated in indexing");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Updating Order indexing", "Error: " + error.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                                "Json parsing error: " + error.getMessage(),
                                                Toast.LENGTH_LONG)
                                        .show();
                                System.out.println("Json parsing error: " + error.getMessage());
                            }
                        });
                    }
                }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> windex = new HashMap<String, String>();
                windex.put("order_id", dborder_info_id);
                windex.put("order_status", status1);
                //       windex.put("num_item", no_items);

                return windex;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);


        /*all_new_orders();
        all_new_orders_v();*/
    }
    private void createordersalesdata(DataModel_w order) {

        requestQueue = Volley.newRequestQueue(this);
        Log.d("weraorder","inside createsalesdata");
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"createsalesdata.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("weratablecreation","response2");
                        if(response.equalsIgnoreCase("success")){
                            Log.d("weratablecreation","success2");
                        }else{
                            Log.d("weratablecreation","failed2");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // publishResults();
                        Log.d("weratablecreation", "Error: " + error.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                                "Json parsing error: " + error.getMessage(),
                                                Toast.LENGTH_LONG)
                                        .show();
                                System.out.println("Json parsing error: " + error.getMessage());
                            }
                        });
                    }
                })  {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();

                params.put("_id", order.dborder_info_id);
                params.put("order_type", order.dborder_type1);
                params.put("order_date_time", order.dbdate_time);
                params.put("payment_mode", order.dbpayme_type);
                params.put("restaurant_id", order.dbrestaurant_id);
                params.put("restaurant_name", order.dbrestaurant_name);
                params.put("external_order_id", order.dbexternal_order_id);
                params.put("restaurant_address", order.dbrestaurant_address);
                params.put("restaurant_number", order.dbrestaurant_number);
                params.put("order_from", order.dborder_from);
                params.put("enable_delivery", order.dbenable_delivery);
                params.put("net_amount", order.dbnet_amount);
                params.put("gross_amount", order.dbgross_amount);
                params.put("order_instructions", order.dborder_instructions);
                params.put("gst", order.dborder_gst);
                params.put("cgst", order.dborder_cgst);
                params.put("sgst", order.dborder_sgst);
                params.put("packaging", order.dbpackaging);
                params.put("order_packaging", order.dborder_packaging);
                params.put("packaging_cgst_percent", order.dbpackaging_cgst_percent);
                params.put("packaging_sgst_percent", order.dbpackaging_sgst_percent);
                params.put("packaging_cgst", order.dbpackaging_cgst);
                params.put("packaging_sgst", order.dbpackaging_sgst);
                params.put("order_packaging_cgst", order.dborder_pckg_cgst);
                params.put("order_packaging_sgst", order.dborder_pckg_sgst);
                params.put("order_packaging_cgst_percent", order.dborder_pckg_cgst_per);
                params.put("order_packaging_sgst_percent", order.dborder_pckg_sgst_per);
                params.put("delivery_charge", order.dbdelivery_charge);
                params.put("discount_reason", order.dbdiscount_type);
                params.put("discount", order.dbdiscount_amount);
                params.put("cus_name", order.dbcust_name1);
                params.put("cus_phone_number", order.dbcust_phno);
                params.put("cus_email", order.dbcust_email);
                params.put("cus_address", order.dbcust_address);
                params.put("cus_delivery_area", order.dbcust_deliv_area);
                params.put("cus_address_instructions", order.dbcust_deli_instru);
                params.put("num_item", order.dbno_items);
                params.put("order_status", order.status);
                params.put("order_reason", "");
                params.put("rider_name", "");
                params.put("rider_number", "");
                params.put("rider_status", "");
                params.put("time_to_arrive", "");

                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(0,-1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);
    }

    public static String convertUnixTime(long unixTime) {
        long receive_time=(unixTime * 1000L);
        //receive_time=(receive_time / 60000) % 60;
        //Date date = new Date((receive_time) + 600000);
        Date addeddate = new Date((receive_time) + 600000); // Unix time is in seconds, so convert to milliseconds
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

        //SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.format(addeddate);
    }
    public void func_out_of_stock(final String item, String price, boolean status){

       // Toast.makeText(getApplicationContext(), " "+item+" "+price+" "+status, Toast.LENGTH_SHORT).show();

        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(item);
        boolean stockstatus = false;
       /* if(status.equals("OFF")){
            stockstatus= true;
        } else if (status.equals("ON")) {
            stockstatus= false;
        }*/

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String current = dateFormat.format(calendar.getTime());
        String nextday;


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            calendar.setTime(sdf.parse(current));
            calendar.add(Calendar.DATE, 1); // Add one day
            nextday= sdf.format(calendar.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        /*try {

            JSONArray jsonArray = new JSONArray();
            JSONObject json1 = new JSONObject();
            json1.put("id", item);
            json1.put("item_name", item);
            json1.put("price", price);
            json1.put("active", status);
            jsonArray.put(json1);



            json2= new JSONObject();
            json2.put("merchant_id",str_merch_id);
            json2.put("menu",jsonArray);

            String jsonString = json2.toString();

        }catch (JSONException ex){
            ex.printStackTrace();
        }*/
        try {
            //I try to use this for send Header is application/json
            JSONArray jsonArray = new JSONArray();
            jsonBody = new JSONObject();
            jsonArray.put(item);
            jsonBody.put("merchant_id", str_merch_id);
            //jsonBody.put("item_ids", arrayList);
            jsonBody.put("item_ids", jsonArray);
            jsonBody.put("status", status);
            jsonBody.put("from_time", current);
            jsonBody.put("to_time", nextday);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        System.out.println("out of stock json " + jsonBody);

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(werastock, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                        if(status==true){
                            Toast.makeText(New_OrderActivity_w.this,"Item marked In stock ", Toast.LENGTH_LONG).show();
                        } else if (status==false) {
                            Toast.makeText(New_OrderActivity_w.this,"Item marked Out of stock ", Toast.LENGTH_LONG).show();
                        }
               //         Toast.makeText(New_OrderActivity_w.this,item+" "+status, Toast.LENGTH_LONG).show();
                        System.out.println("out of stock response " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + error.getMessage(),
                                        Toast.LENGTH_LONG)
                                .show();*/
                        System.out.println("Json parsing error: " + error.getMessage());
                    }
                });
            }
        }) { //no semicolon or coma
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "X-Wera-Api-Key");
                params.put("X-Wera-Api-Key", "9b1bc8d1-99d2-4597-aa7e-64e0a9580c10");
                params.put("Accept", "");
                return params;
            }
        };
        mQueue.add(jsonObjectRequest);

    }

    public void func_out_of_stock_var(final String item,final String var, String price, boolean status){

      //  Toast.makeText(getApplicationContext(), " "+item+" "+var+" "+price+" "+status, Toast.LENGTH_SHORT).show();

        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(item);
        boolean stockstatus = false;
        /*if(status.equals("OFF")){
            stockstatus= false;
        } else if (status.equals("ON")) {
            stockstatus= true;
        }*/


        try {
            //I try to use this for send Header is application/json
            JSONArray jsonArray = new JSONArray();
            jsonBody = new JSONObject();
            jsonArray.put(item);
            jsonBody.put("merchant_id", str_merch_id);
            //jsonBody.put("item_ids", arrayList);
            jsonBody.put("item_id", item);
            jsonBody.put("size_id", var);
            jsonBody.put("status", status);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

        System.out.println("out of stock var json " + jsonBody);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(varistock, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                        if(status==true){
                            Toast.makeText(New_OrderActivity_w.this,"Item marked In stock ", Toast.LENGTH_LONG).show();
                        } else if (status==false) {
                            Toast.makeText(New_OrderActivity_w.this,"Item marked Out of stock ", Toast.LENGTH_LONG).show();
                        }
                    //    Toast.makeText(New_OrderActivity_w.this,item+" "+status, Toast.LENGTH_LONG).show();
                        System.out.println("out of stock variant response " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + error.getMessage(),
                                        Toast.LENGTH_LONG)
                                .show();*/
                        System.out.println("Json parsing error: " + error.getMessage());
                    }
                });
            }
        }) { //no semicolon or coma
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "X-Wera-Api-Key");
                params.put("X-Wera-Api-Key", "9b1bc8d1-99d2-4597-aa7e-64e0a9580c10");
                params.put("Accept", "");
                return params;
            }
        };
        mQueue.add(jsonObjectRequest);

    }

    private void custom_new_orders(final String ord_type) {
        arrayList = new ArrayList<>();
        //arrayList.clear();
        RequestQueue mQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest strord= new StringRequest(POST, weraorderdetails,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response1) {
                        try {
                             /*response1 = response1.replace("\"","'");
                             JSONObject jo = new JSONObject(response1.substring(1,response1.length()-1));*/
                            JSONArray orddetails = new JSONArray(response1);
                            //String orddetails = String.valueOf(response1);
                            DataModel_w id = null;
                            //ArrayList arrayList = new ArrayList<>();

                            for (int k = 0; k < orddetails.length(); k++) {
                                JSONObject c = orddetails.getJSONObject(k);
                                //System.out.println("inloop Custom_new_orders response: " + c + "  " + response1);
                                String orderid = c.getString("_id");
                                String order_from = c.getString("order_from");
                                String status = c.getString("order_status");
                                if(status.equalsIgnoreCase("auto-accept")){
                                    status="Accepted";
                                }

                                if (order_from.equalsIgnoreCase(ord_type)) {
                                    if (status.toString().equalsIgnoreCase("Pending")) {
                                        String date_time = c.getString("order_date_time");
                                        // date_time=convertUnixTime(Long.parseLong(date_time));
                                        // System.out.println("Order no " + c);
                                        String payme_type = c.getString("payment_mode");

                                        String restaurant_id = c.getString("restaurant_id");
                                        String restaurant_name = c.getString("restaurant_name");
                                        String external_order_id = c.getString("external_order_id");
                                        String restaurant_address = c.getString("restaurant_address");
                                        String restaurant_number = c.getString("restaurant_number");

                                        //String order_status = c.getString("status");

                                        String enable_delivery = c.getString("enable_delivery");
                                        String net_amount = c.getString("net_amount");
                                        String gross_amount = c.getString("gross_amount");
                                        String order_instructions = c.getString("order_instructions");
                                        String order_gst = c.getString("gst");
                                        String order_cgst = c.getString("cgst");
                                        String order_sgst = c.getString("sgst");
                                        // String ord_cgst_percent = c.getString("cgst_percent");
                                        // String ord_sgst_percent = c.getString("sgst_percent");
                                        String packaging = c.getString("packaging");
                                        String order_packaging = c.getString("order_packaging");
                                        String order_pckg_cgst = c.getString("order_packaging_cgst");
                                        String order_pckg_sgst = c.getString("order_packaging_sgst");
                                        String order_pckg_cgst_per = c.getString("order_packaging_cgst_percent");
                                        String order_pckg_sgst_per = c.getString("order_packaging_sgst_percent");
                                        String packaging_cgst = c.getString("packaging_cgst");
                                        String packaging_sgst = c.getString("packaging_sgst");
                                        String packaging_cgst_percent = c.getString("packaging_cgst_percent");
                                        String packaging_sgst_percent = c.getString("packaging_sgst_percent");

                                        // String order_gst_per = c.getString("gst_percent");
                                        String delivery_charge = c.getString("delivery_charge");
                                        /** Documented 'type' is 'discount_reason' and 'amount' is 'discount' in JSON data **/
                                        String discount_type = c.getString("discount_reason");
                                        String discount_amount = c.getString("discount");

                                        String no_items = c.getString("num_item");
                                        //String order_from = c.getString("order_from");
                                        String order_cancelreason = c.getString("order_reason");
                                        String deli_per_name = c.getString("rider_name");
                                        String deli_per_phon = c.getString("rider_number");
                                        String deli_per_status = c.getString("rider_status");
                                        String deli_arrivetime = c.getString("time_to_arrive");
                                        String cust_name1 = c.getString("cus_name");
                                        String cust_phno = c.getString("cus_phone_number");
                                        String cust_email = c.getString("cus_email");
                                        String cust_address = c.getString("cus_address");
                                        String cust_deliv_area = c.getString("cus_delivery_area");
                                        String cust_deli_instru = c.getString("cus_address_instructions");

                                        //JSONObject items=c.getJSONObject("orderItemsJSON");
                                        JSONArray cart = c.getJSONArray("orderItemsJSON");
                                        System.out.println("inloop inner items response: " + cart);

                                        for (int j = 0; j < cart.length(); j++) {
                                            JSONObject items = cart.getJSONObject(j);
//                                         JSONObject items=cart;

                                            String itemid = items.getString("wera_item_id");
                                            //  System.out.println("item: " + itemid);
                                            String item_id = items.getString("item_id");
                                            String item_name = items.getString("item_name");
                                            String item_unit_price = items.getString("item_unit_price");
                                            String subtotal = items.getString("subtotal");
                                            String item_quantity = items.getString("item_quantity");
                                            String item_gst = items.getString("gst");
                                            String item_gst_per = items.getString("gst_percent");
                                            String item_packaging = items.getString("packaging");
                                            String item_dish = items.getString("dish");
                                            String instructions = items.getString("instructions");
                                            String discount = items.getString("discount");
                                            String item_cgst = items.getString("cgst");
                                            String item_sgst = items.getString("sgst");
                                            String item_cgst_per = items.getString("cgst_percent");
                                            String item_sgst_per = items.getString("sgst_percent");
                                            String item_packaging_gst = items.getString("packaging_gst");
                                            String item_packaging_sgst = items.getString("packaging_sgst");
                                            String item_packaging_cgst = items.getString("packaging_cgst");
                                            String item_cgst_percent = items.getString("packaging_cgst_percent");
                                            String item_sgst_percent = items.getString("packaging_sgst_percent");

                                            // for Variants array
                                            JSONArray variant, addons;
                                            try {
                                                variant = items.getJSONArray("variants");
                                            } catch (JSONException e) {
                                                System.out.println("Json error variants"+ e);
                                                throw new RuntimeException(e);
                                            }
                                            for (int p = 0; p < variant.length(); p++) {
                                                JSONObject var = variant.getJSONObject(p);
                                                try {
                                                    String var_size_id = var.getString("size_id");
                                                    String var_size_name = var.getString("size_name");
                                                    String var_price = var.getString("price");
                                                    String var_cgst = var.getString("cgst");
                                                    String var_sgst = var.getString("sgst");
                                                    String var_cgst_percent = var.getString("cgst_percent");
                                                    String var_sgst_percent = var.getString("sgst_percent");
                                                } catch (JSONException e) {
                                                    System.out.println("Json error variants"+ e);
                                                    throw new RuntimeException(e);
                                                }
                                            }


                                            try {
                                                addons = items.getJSONArray("addons");
                                            } catch (JSONException e) {
                                                System.out.println("Json error Addons");
                                                throw new RuntimeException(e);
                                            }
                                            for (int l = 0; l < addons.length(); l++) {
                                                JSONObject addon = addons.getJSONObject(l);
                                                try {
                                                    String addon_id = addon.getString("addon_id");
                                                    String addon_name = addon.getString("name");
                                                    String addon_price = addon.getString("price");
                                                    String addon_cgst = addon.getString("cgst");
                                                    String addon_sgst = addon.getString("sgst");
                                                    String addon_cgst_percent = addon.getString("cgst_percent");
                                                    String addon_sgst_percent = addon.getString("sgst_percent");
                                                } catch (JSONException e) {
                                                    System.out.println("Json error Addons");
                                                    throw new RuntimeException(e);

                                                }

                                            }

                                        }


                                        if (status.toString().equalsIgnoreCase("pending")) {
                                            id = new DataModel_w(orderid, String.valueOf(cart.length()), gross_amount, order_from, cust_name1, cust_phno, cust_address, cust_deli_instru, cust_deliv_area, cust_email, status, "", deli_per_name, deli_per_phon, deli_per_status, cart,order_instructions, str_merch_id, deli_arrivetime);
                                            arrayList.add(id);
                                            System.out.println("Item" + "recycler orderid: " + orderid + "\nType of order: " + "\nOrder Status: " + status + order_from + "\nCustomer name: " + cust_name1 + "\nNumber of items: " + String.valueOf(cart.length()));
                                        }
                                    }
                                }
                            }

                            //  progressBar.setVisibility(View.GONE);
                            RecyclerViewAdapter_w adapter = new RecyclerViewAdapter_w(New_OrderActivity_w.this, arrayList, New_OrderActivity_w.this);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("New order page", "Error: " + error.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + error.getMessage(),
                                        Toast.LENGTH_LONG)
                                .show();*/
                        System.out.println("Json parsing error: " + error.getMessage());
                    }
                });
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> windex = new HashMap<String, String>();
                windex.put("db_name", company + "_"+ store + "_"+ device);
                //windex.put("order_id", order_info_id);
                windex.put("order_status", "Pending");
                return windex;
            }
        };
        mQueue1.add(strord);





/*        arrayList = new ArrayList<>();
        try {
            //I try to use this for send Header is application/json
            jsonBody = new JSONObject();
            jsonBody.put("merchant_id", str_merch_id);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url_pending, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                                    JSONObject jsonObj = new JSONObject(response.toString());
                            DataModel_w id=null;
                            // Getting JSON Array node
                            JSONObject details = response.getJSONObject("details");
                            JSONArray orders = details.getJSONArray("orders");
                            ArrayList<String> my_array = new ArrayList<String>();
                            // looping through All Contacts
                            for (int i = 0; i < orders.length(); i++) {
                                JSONObject c = orders.getJSONObject(i);
                                System.out.println(ord_type);

                                String order_info_id = c.getString("order_id");
                                String order_type1 = c.getString("order_from");
                                String order_status = c.getString("status");

                                System.out.println(order_type1);

                                if (order_type1.equalsIgnoreCase(ord_type)) {
                                    String date_time = c.getString("order_date_time");
                                    String payme_type = c.getString("payment_mode");

                                    String enable_delivery = c.getString("enable_delivery");

                                    String cust_name1 = c.getJSONObject("customer_details").getString("name");
                                    String cust_phno = c.getJSONObject("customer_details").getString("phone_number");
                                    String cust_email = c.getJSONObject("customer_details").getString("email");
                                    String cust_address = c.getJSONObject("customer_details").getString("address");
                                    String cust_deliv_area = c.getJSONObject("customer_details").getString("delivery_area");
                                    String cust_deli_instru = c.getJSONObject("customer_details").getString("address_instructions");

                                    String gross_amount = c.getString("gross_amount");

                                    JSONArray cart = c.getJSONArray("order_items");

                                    for (int j = 0; j < cart.length(); j++) {
                                        JSONObject items = cart.getJSONObject(j);
                                        String item_id = items.getString("item_id");
                                        String item_name = items.getString("item_name");
                                        String item_quantity = items.getString("item_quantity");
                                        //System.out.println("orderid: " + order_info_id + "  name: " + order_type1);

                                    }
                                    id= new DataModel_w(order_info_id, String.valueOf(cart.length()), gross_amount, order_type1, cust_name1 ,cust_phno, cust_address, cust_deli_instru,cust_deliv_area,cust_email, "", "", "", "", "", str_merch_id);
                                    arrayList.add(id);
                                }
                            }

                            RecyclerViewAdapter_w adapter = new RecyclerViewAdapter_w(New_OrderActivity_w.this, arrayList, New_OrderActivity_w.this);
                            recyclerView.setAdapter(adapter);

                        } catch (final JSONException e) {
                            Log.e("TAG", "Json parsing error: " + e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    *//*Toast.makeText(getApplicationContext(),
                                                    "Json parsing error: " + e.getMessage(),
                                                    Toast.LENGTH_LONG)
                                            .show();*//*
                                    System.out.println("Json parsing error: "+e.getMessage());
                                }
                            });
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        *//*Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + error.getMessage(),
                                        Toast.LENGTH_LONG)
                                .show();*//*
                        System.out.println("Json parsing error: " + error.getMessage());
                    }
                });
            }
        }) { //no semicolon or coma
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "X-Wera-Api-Key");
                params.put("X-Wera-Api-Key", "9b1bc8d1-99d2-4597-aa7e-64e0a9580c10");
                params.put("Accept", "");
                return params;
            }
        };
        mQueue.add(jsonObjectRequest);*/
    }

    public void remaining_orders1(String s) {
        int bn1 = 0;

        Cursor cursor = db_inapp.rawQuery("SELECT * FROM Orderlicense", null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String countval = cursor.getString(2);

                Cursor cursor1 = db_inapp.rawQuery("SELECT * FROM Orderlicense WHERE ordersrece = '" + countval + "'", null);
                if (cursor1.moveToFirst()) {
                    String id1 = cursor1.getString(0);
                    String limit = cursor1.getString(1);
                    String countv = cursor1.getString(2);
                    countlimit = Integer.parseInt(limit);
                    count = Integer.parseInt(countv);

                    SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyyyy");
                    final String normal1 = normal2.format(new Date());

                    Date dt = new Date();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
                    final String time1 = sdf1.format(dt);


                    ContentValues contentValues = new ContentValues();
                    contentValues.put("ordersrece", count + 1);
                    contentValues.put("remainingorders", countlimit - 1);
                    contentValues.put("time", time1);
                    contentValues.put("date", normal1);
                    String where = "_id = '" + id1 + "'";
                    db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
                    db_inapp.update("Orderlicense", contentValues, where, new String[]{});

                    bn1 = countlimit - 1;

                }
                cursor1.close();
            } while (cursor.moveToNext());
        }
        cursor.close();




        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        final int finalBn = bn1;
        StringRequest sr = new StringRequest(
                POST,
                WebserviceUrl+"remaining_order.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                /*Toast.makeText(getApplicationContext(),
                                                "Json parsing error: " + error.getMessage(),
                                                Toast.LENGTH_LONG)
                                        .show();*/
                                System.out.println("Json parsing error: " + error.getMessage());
                            }
                        });
                    }
                }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put("company", company+ "");
                params.put("store", store+ "");
                params.put("device", device);
                params.put("order", String.valueOf(finalBn));
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);
    }

    public void all_new_orders() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 10 seconds
                System.out.println("Second new order");
        /*progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
        progressBar.setVisibility(View.VISIBLE);*/
                arrayList.clear();
                recyclerView1 =(RecyclerView) findViewById(R.id.newrecycle);
                RequestQueue mQueue1 = Volley.newRequestQueue(getApplicationContext());
                StringRequest strord= new StringRequest(POST, weraorderdetails,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response1) {
                                try {
                             /*response1 = response1.replace("\"","'");
                             JSONObject jo = new JSONObject(response1.substring(1,response1.length()-1));*/
                                    JSONArray orddetails = new JSONArray(response1);
                                    //String orddetails = String.valueOf(response1);
                                    DataModel_w id = null;
                                    ArrayList arrayList = new ArrayList<>();

                                    for (int k = 0; k < orddetails.length(); k++) {
                                        JSONObject c = orddetails.getJSONObject(k);
                                        // System.out.println("inloop all_new response: " + c + "  " + response1);
                                        String orderid = c.getString("_id");
                                        String order_from = c.getString("order_from");
                                        String status = c.getString("order_status");
                                        if(status.equalsIgnoreCase("auto-accept")){
                                            status="Accepted";
                                        }

                                        if (status.toString().equalsIgnoreCase("Pending")) {
                                            String date_time = c.getString("order_date_time");
                                            // date_time=convertUnixTime(Long.parseLong(date_time));
                                            // System.out.println("Order no " + c);
                                            String payme_type = c.getString("payment_mode");

                                            String restaurant_id = c.getString("restaurant_id");
                                            String restaurant_name = c.getString("restaurant_name");
                                            String external_order_id = c.getString("external_order_id");
                                            String restaurant_address = c.getString("restaurant_address");
                                            String restaurant_number = c.getString("restaurant_number");

                                            //String order_status = c.getString("status");

                                            String enable_delivery = c.getString("enable_delivery");
                                            String net_amount = c.getString("net_amount");
                                            String gross_amount = c.getString("gross_amount");
                                            String order_instructions = c.getString("order_instructions");
                                            String order_gst = c.getString("gst");
                                            String order_cgst = c.getString("cgst");
                                            String order_sgst = c.getString("sgst");
                                            // String ord_cgst_percent = c.getString("cgst_percent");
                                            // String ord_sgst_percent = c.getString("sgst_percent");
                                            String packaging = c.getString("packaging");
                                            String order_packaging = c.getString("order_packaging");
                                            String order_pckg_cgst = c.getString("order_packaging_cgst");
                                            String order_pckg_sgst = c.getString("order_packaging_sgst");
                                            String order_pckg_cgst_per = c.getString("order_packaging_cgst_percent");
                                            String order_pckg_sgst_per = c.getString("order_packaging_sgst_percent");
                                            String packaging_cgst = c.getString("packaging_cgst");
                                            String packaging_sgst = c.getString("packaging_sgst");
                                            String packaging_cgst_percent = c.getString("packaging_cgst_percent");
                                            String packaging_sgst_percent = c.getString("packaging_sgst_percent");

                                            // String order_gst_per = c.getString("gst_percent");
                                            String delivery_charge = c.getString("delivery_charge");
                                            /** Documented 'type' is 'discount_reason' and 'amount' is 'discount' in JSON data **/
                                            String discount_type = c.getString("discount_reason");
                                            String discount_amount = c.getString("discount");

                                            String no_items = c.getString("num_item");
                                            //String order_from = c.getString("order_from");
                                            String order_cancelreason = c.getString("order_reason");
                                            String deli_per_name = c.getString("rider_name");
                                            String deli_per_phon = c.getString("rider_number");
                                            String deli_per_status = c.getString("rider_status");
                                            String deli_arrivetime = c.getString("time_to_arrive");
                                            String cust_name1 = c.getString("cus_name");
                                            String cust_phno = c.getString("cus_phone_number");
                                            String cust_email = c.getString("cus_email");
                                            String cust_address = c.getString("cus_address");
                                            String cust_deliv_area = c.getString("cus_delivery_area");
                                            String cust_deli_instru = c.getString("cus_address_instructions");

                                            //JSONObject items=c.getJSONObject("orderItemsJSON");
                                            JSONArray cart = c.getJSONArray("orderItemsJSON");
                                            System.out.println("inloop inner items response: " + cart);

                                            for (int j = 0; j < cart.length(); j++) {
                                                JSONObject items = cart.getJSONObject(j);
//                                         JSONObject items=cart;

                                                String itemid = items.getString("wera_item_id");
                                                //  System.out.println("item: " + itemid);
                                                String item_id = items.getString("item_id");
                                                String item_name = items.getString("item_name");
                                                String item_unit_price = items.getString("item_unit_price");
                                                String subtotal = items.getString("subtotal");
                                                String item_quantity = items.getString("item_quantity");
                                                String item_gst = items.getString("gst");
                                                String item_gst_per = items.getString("gst_percent");
                                                String item_packaging = items.getString("packaging");
                                                String item_dish = items.getString("dish");
                                                String instructions = items.getString("instructions");
                                                String discount = items.getString("discount");
                                                String item_cgst = items.getString("cgst");
                                                String item_sgst = items.getString("sgst");
                                                String item_cgst_per = items.getString("cgst_percent");
                                                String item_sgst_per = items.getString("sgst_percent");
                                                String item_packaging_gst = items.getString("packaging_gst");
                                                String item_packaging_sgst = items.getString("packaging_sgst");
                                                String item_packaging_cgst = items.getString("packaging_cgst");
                                                String item_cgst_percent = items.getString("packaging_cgst_percent");
                                                String item_sgst_percent = items.getString("packaging_sgst_percent");

                      /*                          // for Variants array
                                JSONArray variants = c.getJSONArray("variants");
                                for (int k = 0; k < variants.length(); k++) {
                                    JSONObject var = variants.getJSONObject(k);

                                    String var_size_id = var.getString("size_id");
                                    String var_size_name = var.getString("size_name");
                                    String var_price = var.getString("price");
                                    String var_cgst = var.getString("cgst");
                                    String var_sgst = var.getString("sgst");
                                    String var_cgst_percent = var.getString("cgst_percent");
                                    String var_sgst_percent = var.getString("sgst_percent");
                                }

                                JSONArray addons = c.getJSONArray("addons");
                                for (int l = 0; l < addons.length(); l++) {
                                    JSONObject addon = addons.getJSONObject(l);

                                    String addon_id = addon.getJSONObject("addons_info").getString("addon_id");
                                    String addon_name = addon.getJSONObject("addons_info").getString("name");
                                    String addon_price = addon.getJSONObject("addons_info").getString("price");
                                    String addon_cgst = addon.getJSONObject("addons_info").getString("cgst");
                                    String addon_sgst = addon.getJSONObject("addons_info").getString("sgst");
                                    String addon_cgst_percent = addon.getJSONObject("addons_info").getString("cgst_percent");
                                    String addon_sgst_percent = addon.getJSONObject("addons_info").getString("sgst_percent");

                                }*/

                                            }


                                            // if (status.toString().equalsIgnoreCase("pending")) {
                                            id = new DataModel_w(orderid, String.valueOf(cart.length()), gross_amount, order_from, cust_name1, cust_phno, cust_address, cust_deli_instru, cust_deliv_area, cust_email, status, "", deli_per_name, deli_per_phon, deli_per_status, cart,order_instructions, str_merch_id, deli_arrivetime);
                                            arrayList.add(id);
                                            System.out.println("Item" + "recycler orderid: " + orderid + "\nType of order: " + "\nOrder Status: " + status + order_from + "\nCustomer name: " + cust_name1 + "\nNumber of items: " + String.valueOf(cart.length()));
                                            // }
                                        }
                                    }

                                    //progressBar.setVisibility(View.GONE);
                                    RecyclerViewAdapter_w adapter1 = new RecyclerViewAdapter_w(New_OrderActivity_w.this, arrayList, New_OrderActivity_w.this);
                                    recyclerView1.setAdapter(adapter1);


                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("New order page", "Error: " + error.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                        /*Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + error.getMessage(),
                                        Toast.LENGTH_LONG)
                                .show();*/
                                System.out.println("Json parsing error: " + error.getMessage());
                            }
                        });
                    }
                }){
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String,String> windex = new HashMap<String, String>();
                        windex.put("db_name", company + "_"+ store + "_"+ device);
                        //windex.put("order_id", order_info_id);
                        //windex.put("order_status", "Pending");
                        return windex;
                    }
                };
                mQueue1.add(strord);
            }
        }, 800);
    }

    public void all_new_orders_v() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 10 seconds

                final TextView preparing_orders_value = (TextView) findViewById(R.id.preparing_orders_valuen);

                i_ord = 0;
                System.out.println("Preparing order counter ");
                RequestQueue mQueue2 = Volley.newRequestQueue(getApplicationContext());
                StringRequest strord = new StringRequest(POST, weraorderdetails, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response2) {
                        System.out.println("preparing counter " + response2 );
                        //for(int i=0;i<response.length();i++) {
                        try {
                            //JSONObject ord = new JSONObject(response);
                            JSONArray orddetails = new JSONArray(response2);
                            for (int k = 0; k < orddetails.length(); k++) {
                                JSONObject c = orddetails.getJSONObject(k);
                                String ordid = c.getString("_id");
                                String status = c.getString("order_status");
                                if (status.toString().equalsIgnoreCase("Preparing") || status.toString().equalsIgnoreCase("Accepted") || status.toString().equalsIgnoreCase("auto-accept")) {
                                    i_ord++;
                                    preparing_orders_value.setText(String.valueOf(i_ord));
                                }
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        // }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Getting order details", "Error: " + error.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                                    /*Toast.makeText(getApplicationContext(),
                                                                    "Json parsing error: " + error.getMessage(),
                                                                    Toast.LENGTH_LONG)
                                                            .show();*/
                                System.out.println("Json parsing error: " + error.getMessage());
                            }
                        });
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> windex = new HashMap<String, String>();
                        windex.put("db_name", company + "_" + store + "_" + device);
                        //windex.put("order_id", order_info_id1);
                        // windex.put("order_status", "Accepted");
                        return windex;
                    }
                };
                mQueue2.add(strord);
            }
        }, 500);


    }
    /*private void orderpreparing() {
        final TextView preparing_orders_value = (TextView) findViewById(R.id.preparing_orders_valuen);
        i_ord = 0;
        System.out.println("Preparing order counter ");
        try {
            //I try to use this for send Header is application/json
            jsonBody = new JSONObject();
            jsonBody.put("merchant_id", str_merch_id);
//            jsonBody.put("order_id", "2016204");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        RequestQueue mQueue1 = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(url_orders, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject details = response.getJSONObject("details");
                            JSONArray orders = details.getJSONArray("orders");
                            for (int i = 0; i < orders.length(); i++) {
                                JSONObject c = orders.getJSONObject(i);
                                String orderid = c.getString("order_id");
                                String status = c.getString("status");
                                if (status.toString().equalsIgnoreCase("Accepted")) {

                                   *//* RequestQueue mQueue2 = Volley.newRequestQueue(getApplicationContext());
                                    String finalOrderid = orderid;
                                    StringRequest strord = new StringRequest(POST, weraorderdetails, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            DataModel_w id = null;
                                            ArrayList orddatadb = new ArrayList<>();
                                            System.out.println("inloop " + response );
                                            //for(int i=0;i<response.length();i++) {
                                            try {
                                                //JSONObject ord = new JSONObject(response);
                                                JSONArray orddetails = new JSONArray(response);
                                                for (int k = 0; k < orddetails.length(); k++) {
                                                    JSONObject c = orddetails.getJSONObject(k);
                                                    String ordid = c.getString("_id");
                                                    if (ordid.equalsIgnoreCase(finalOrderid)) {*//*
                                                        i_ord++;
                                                        preparing_orders_value.setText(String.valueOf(i_ord));
                                                   *//* }
                                                }
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }
                                            // }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("Getting order details", "Error: " + error.getMessage());
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                        *//**//*Toast.makeText(getApplicationContext(),
                                                                        "Json parsing error: " + error.getMessage(),
                                                                        Toast.LENGTH_LONG)
                                                                .show();*//**//*
                                                    System.out.println("Json parsing error: " + error.getMessage());
                                                }
                                            });
                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> windex = new HashMap<String, String>();
                                            windex.put("db_name", company + "_" + store + "_" + device);
                                            //windex.put("order_id", order_info_id1);
                                            //windex.put("order_status", status);
                                            return windex;
                                        }
                                    };
                                    mQueue2.add(strord);*//*

                                }
                            }

                        } catch (final JSONException e) {
                            Log.e("TAG", "Json parsing error: " + e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    *//*Toast.makeText(getApplicationContext(),
                                                    "Json parsing error: " + e.getMessage(),
                                                    Toast.LENGTH_LONG)
                                            .show();*//*
                                    System.out.println("Json parsing error: "+e.getMessage());
                                }
                            });

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            *//*Toast.makeText(getApplicationContext(),
                                            "Json parsing error: " + error.getMessage(),
                                            Toast.LENGTH_LONG)
                                    .show();*//*
                        System.out.println("Json parsing error: " + error.getMessage());
                    }
                });
            }
        }) { //no semicolon or coma
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "X-Wera-Api-Key");
                params.put("X-Wera-Api-Key", "9b1bc8d1-99d2-4597-aa7e-64e0a9580c10");
                params.put("Accept", "");
                return params;
            }
        };
        mQueue1.add(jsonObjectRequest1);

    }*/


    @Override
    public void onItemClick(DataModel_w item) {
        /*Toast.makeText(getApplicationContext(), item.text + " is clicked", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(New_OrderActivity_w.this, New_Individualorder_Itemview.class);
        intent.putExtra("order_id", item.text);
        intent.putExtra("gross_amount", item.totalprice);
        intent.putExtra("order_from", item.order_type);
        startActivity(intent);*/

    }

    /*public void order_items(String order_id, String merch_id, Context mContext) {

        final Dialog dialog = new Dialog(this, R.style.timepicker_date_dialog);
        dialog.setContentView(R.layout.order_items_w);
        dialog.show();
        itemlist = new ArrayList<>();
        Context icontext= mContext;

        // TableLayout itemtab = (TableLayout) dialog.findViewById(R.id.itemtab);

        LinearLayout back_activity = (LinearLayout) dialog.findViewById(R.id.back_activity);
        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TextView itmname = (TextView) dialog.findViewById(R.id.itemname);
        TextView itmqnty = (TextView) dialog.findViewById(R.id.itemqnty);

        try {
            //I try to use this for send Header is application/json
            jsonBody = new JSONObject();
            jsonBody.put("merchant_id", merch_id);
            //jsonBody.put("order_id", order_id);
            //  jsonBody.put("rejection_id", rr_id);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        RequestQueue mQueue = Volley.newRequestQueue(icontext);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url_pending, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject iresponse) {
                        DataModel_w iden = null;
                        try {
                            JSONObject details = iresponse.getJSONObject("details");
                            JSONArray orders = details.getJSONArray("orders");
                            for (int i = 0; i < orders.length(); i++) {
                                JSONObject c = orders.getJSONObject(i);
                                String order_info_id = c.getString("order_id");

                                if(order_info_id.equalsIgnoreCase(order_id)) {
                                    JSONArray cart = c.getJSONArray("order_items");

                                    for (int j = 0; j < cart.length(); j++) {
                                        JSONObject items = cart.getJSONObject(j);
                                        String itemid = items.getString("wera_item_id");
                                        String item_id = items.getString("item_id");
                                        String item_name = items.getString("item_name");
                                        String item_quantity = items.getString("item_quantity");
                                        System.out.println("item name " + item_name + "item quantity " + item_quantity);
                                        itmqnty.append(item_quantity + "     " + "\n");
                                        itmname.append(item_id + "     " + "\n");
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        *//*Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + error.getMessage(),
                                        Toast.LENGTH_LONG)
                                .show();*//*
                        System.out.println("Json parsing error: " + error.getMessage());
                    }
                });
            }
        }) { //no semicolon or coma
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "X-Wera-Api-Key");
                params.put("X-Wera-Api-Key", "9b1bc8d1-99d2-4597-aa7e-64e0a9580c10");
                params.put("Accept", "");
                return params;
            }
        };
        mQueue.add(jsonObjectRequest);
    }*/

    public void setActivity(Context activity) {
        this.activity = activity;
    }
    public void saveinDB(final String order_id_wera){
        /*progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
        progressBar.setVisibility(View.VISIBLE);*/
        db = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        db1 = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy MM dd");
        final String currentDateandTime1 = sdf2.format(new Date());

        SimpleDateFormat normal = new SimpleDateFormat("dd MMM yyyy");
        final String normal1 = normal.format(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        final String currentDateandTime = sdf.format(new Date());

        Date dt = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
        final String time1 = sdf1.format(dt);

        Date dtt = new Date();
        SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
        final String time24 = sdf1t.format(dtt);

        Date dtt_new = new Date();
        SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");
        final String time24_new = sdf1t_new.format(dtt_new);

        Date dt4 = new Date();
        SimpleDateFormat sdf14 = new SimpleDateFormat("kk");
        String time14 = sdf14.format(dt4);

        dateget = currentDateandTime1;
        dateget1 = normal1;
        timeget = time1;

        Cursor count = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber LIKE  '" + currentDateandTime + "-" + "000" + "0" + "1" + "%' "+" AND order_id!='" + order_id_wera + "'", null);

        final int bill = count.getCount() + 1;
        String bills = String.valueOf(bill);

        final TextView billnum = new TextView(New_OrderActivity_w.this);
        billnum.setText(currentDateandTime + "-" + "000" + "0" + "1" + bills);
        printbillno=billnum.getText().toString();

        dateget = currentDateandTime1;
        dateget1 = normal1;
        timeget = time1;

        Cursor cursor_user = db1.rawQuery("SELECT * FROM LoginUser", null);
        if (cursor_user.moveToFirst()) {
            u_username = cursor_user.getString(1);
            username = cursor_user.getString(1);
            String password = cursor_user.getString(2);
            Cursor ladmin = db1.rawQuery("SELECT * FROM LAdmin WHERE username = '" + u_username + "'", null);
            if (ladmin.moveToFirst()) {
                u_name = ladmin.getString(3);
            } else {
                Cursor madmin = db1.rawQuery("SELECT * FROM LOGIN WHERE username = '" + u_username + "'", null);
                if (madmin.moveToFirst()) {
                    u_name = madmin.getString(3);
                } else {
                    Cursor user1 = db1.rawQuery("SELECT * FROM User1 WHERE username = '" + u_username + "'", null);
                    if (user1.moveToFirst()) {
                        u_name = user1.getString(1);
                    } else {
                        Cursor user2 = db1.rawQuery("SELECT * FROM User2 WHERE username = '" + u_username + "'", null);
                        if (user2.moveToFirst()) {
                            u_name = user2.getString(1);
                        } else {
                            Cursor user3 = db1.rawQuery("SELECT * FROM User3 WHERE username = '" + u_username + "'", null);
                            if (user3.moveToFirst()) {
                                u_name = user3.getString(1);
                            } else {
                                Cursor user4 = db1.rawQuery("SELECT * FROM User4 WHERE username = '" + u_username + "'", null);
                                if (user4.moveToFirst()) {
                                    u_name = user4.getString(1);
                                } else {
                                    Cursor user5 = db1.rawQuery("SELECT * FROM User5 WHERE username = '" + u_username + "'", null);
                                    if (user5.moveToFirst()) {
                                        u_name = user5.getString(1);
                                    } else {
                                        Cursor user6 = db1.rawQuery("SELECT * FROM User6 WHERE username = '" + u_username + "'", null);
                                        if (user6.moveToFirst()) {
                                            u_name = user6.getString(1);
                                        } else {

                                        }
                                        user6.close();
                                    }
                                    user5.close();
                                }
                                user4.close();
                            }
                            user3.close();
                        }
                        user2.close();
                    }
                    user1.close();
                }
                madmin.close();
            }
            ladmin.close();
        }
        cursor_user.close();

        Cursor cursor_bill_count_tag = db1.rawQuery("SELECT * FROM Billcount_tag", null);
        if (cursor_bill_count_tag.moveToFirst()){
            bill_count_tag = cursor_bill_count_tag.getString(1);
            bill_count_tag1 = bill_count_tag+"_";
            bill_count_tag2 = bill_count_tag+"_";
            System.out.println("DB stage 1a ");
        }else {
            bill_count_tag1 = "";
            bill_count_tag2 = "";
            System.out.println("DB stage 1b ");
        }

        RequestQueue mQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest strord= new StringRequest(POST, weraorderdetails,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response1) {
                        try {

                            ArrayList l2 = new ArrayList();
                            JSONArray orders = new JSONArray(response1);

                            System.out.println("DB stage 2 ");
                            System.out.println("orders of " + orders);
                            for (int i = 0; i < orders.length(); i++) {
                                JSONObject c = orders.getJSONObject(i);

                                float p1 = 0;
                                float g1 = 0;
                                String  p_c, g_c;

                                //String deliv_char = c.getString("delivery_charge");
                                String order_info_id = c.getString("_id");
                                String order_type = c.getString("order_type");
                                String date_time = c.getString("order_date_time");
                                // date_time=convertUnixTime(Long.parseLong(date_time));
                                System.out.println("Order Date and time " + date_time);
                                String payme_type = c.getString("payment_mode");

                                String restaurant_id = c.getString("restaurant_id");
                                String restaurant_name = c.getString("restaurant_name");
                                String external_order_id = c.getString("external_order_id");
                                String restaurant_address = c.getString("restaurant_address");
                                String restaurant_number = c.getString("restaurant_number");

                                String order_status = c.getString("order_status");
                                if(order_status.equalsIgnoreCase("auto-accept")){
                                    order_status="Accepted";
                                }

                                String order_from = c.getString("order_from");

                                String enable_delivery = c.getString("enable_delivery");
                                String subtotal = c.getString("net_amount");
                                String totalprice1 = c.getString("gross_amount");
                                String order_instructions = c.getString("order_instructions");
                                //String order_gst = c.getString("gst");
                                String order_cgst = c.getString("cgst");
                                String order_sgst = c.getString("sgst");
                                g_c = c.getString("gst");
                                if(g_c.equals("null3")){
                                    g1=Float.parseFloat(order_cgst)+Float.parseFloat(order_sgst);
                                }else {g1 = Float.parseFloat(g_c);}
                                System.out.println("GST : " + g1);

                                // String ord_cgst_percent = c.getString("cgst_percent");
                                // String ord_sgst_percent = c.getString("sgst_percent");
                                //String packaging = c.getString("packaging");
                                p_c= c.getString("order_packaging");
                                if(p_c.contains("null")){
                                    p1 = 0;
                                }else {p1 = Float.parseFloat(p_c);}
                                System.out.println("Packing charges : " + p1);

                                String packaging = c.getString("packaging");
                                String order_pckg_cgst = c.getString("order_packaging_cgst");
                                String order_pckg_sgst = c.getString("order_packaging_sgst");
                                String order_pckg_cgst_per = c.getString("order_packaging_cgst_percent");
                                String order_pckg_sgst_per = c.getString("order_packaging_sgst_percent");
                                String packaging_cgst = c.getString("packaging_cgst");
                                String packaging_sgst = c.getString("packaging_sgst");
                                String packaging_cgst_percent = c.getString("packaging_cgst_percent");
                                String packaging_sgst_percent = c.getString("packaging_sgst_percent");

                                // String order_gst_per = c.getString("gst_percent");
                                //String delivery_charge = c.getString("delivery_charge");
                                String deliv_char = c.getString("delivery_charge");
                                /** Documented 'type' is 'discount_reason' and 'amount' is 'discount' in JSON data **/
                                String discount_type = c.getString("discount_reason");
                                String discount_amount = c.getString("discount");
                                float subtotal1= Float.parseFloat(subtotal);
                                float dis_amt= Float.parseFloat(discount_amount);
                                float dis_per1= dis_amt/subtotal1;
                                float dis_per=dis_per1*100;

                                String cust_name1 = c.getString("cus_name");
                                String cust_phno = c.getString("cus_phone_number");
                                String cust_email = c.getString("cus_email");
                                String cust_address = c.getString("cus_address");
                                String cust_deliv_area = c.getString("cus_delivery_area");
                                String cust_deli_instru = c.getString("cus_address_instructions");

                                JSONArray cart = c.getJSONArray("orderItemsJSON");

                                if (order_info_id.toString().equals(order_id_wera)) {
//                                    date_time1.setText(date_time);
                                    for (int j = 0; j < cart.length(); j++) {
                                        JSONObject items = cart.getJSONObject(j);

                                        String itemid = items.getString("wera_item_id");
                                        String name = items.getString("item_id");
                                        String online_name = items.getString("item_name");
                                        String price0 = items.getString("item_unit_price");
                                        String total0 = items.getString("subtotal");
                                        String qty = items.getString("item_quantity");
                                        String item_gst = items.getString("gst");
                                        String item_gst_per = items.getString("gst_percent");
                                        String item_packaging = items.getString("packaging");
                                        String item_dish = items.getString("dish");
                                        String instructions = items.getString("instructions");
                                        String discount = items.getString("discount");
                                        String item_packaging_gst = items.getString("packaging_gst");
                                        String item_packaging_sgst = items.getString("packaging_sgst");
                                        String item_packaging_cgst = items.getString("packaging_cgst");
                                        String item_cgst_percent = items.getString("packaging_cgst_percent");
                                        String item_sgst_percent = items.getString("packaging_sgst_percent");
                                        String orderchannel;
                                        if(order_from.equals("zomato")){
                                            orderchannel="Zomato";
                                        } else if (order_from.equals("swiggy")) {
                                            orderchannel="Swiggy";
                                        }else{
                                            orderchannel=order_from;
                                        }

                                        float price = 0;

                                        l2.add(name);
                                        l2.add(online_name);
                                        l2.add(total0);
                                        l2.add("\n");
                                        System.out.println("DB stage 2a ");


                                        try {
                                            // for Variants array
                                            JSONArray variants = c.getJSONArray("variants");
                                            for (int k = 0; k < variants.length(); k++) {
                                                JSONObject var = variants.getJSONObject(k);

                                                String var_size_id = var.getJSONObject("variants").getString("size_id");
                                                String var_size_name = var.getJSONObject("variants").getString("size_name");
                                                String var_price = var.getJSONObject("variants").getString("price");
                                                String var_cgst = var.getJSONObject("variants").getString("cgst");
                                                String var_sgst = var.getJSONObject("variants").getString("sgst");
                                                String var_cgst_percent = var.getJSONObject("variants").getString("cgst_percent");
                                                String var_sgst_percent = var.getJSONObject("variants").getString("sgst_percent");
                                            }
                                        }catch (final JSONException e) {
                                            Log.e("TAG", "Json parsing error: " + e.getMessage());
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //Toast.makeText(getApplicationContext(),"Json parsing error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }

                                        try {
                                            JSONArray addons = c.getJSONArray("addons");
                                            for (int l = 0; l < addons.length(); l++) {
                                                JSONObject addon = addons.getJSONObject(l);

                                                String addon_id = addon.getJSONObject("addons_info").getString("addon_id");
                                                String addon_name = addon.getJSONObject("addons_info").getString("name");
                                                String addon_price = addon.getJSONObject("addons_info").getString("price");
                                                String addon_cgst = addon.getJSONObject("addons_info").getString("cgst");
                                                String addon_sgst = addon.getJSONObject("addons_info").getString("sgst");
                                                String addon_cgst_percent = addon.getJSONObject("addons_info").getString("cgst_percent");
                                                String addon_sgst_percent = addon.getJSONObject("addons_info").getString("sgst_percent");
                                            }
                                        }catch (final JSONException e) {
                                            Log.e("TAG", "Json parsing error: " + e.getMessage());
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    // Toast.makeText(getApplicationContext(),"Json parsing error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                        /*for (int k = 0; k < sub_item_content.length(); k++) {
                                            JSONObject name2 = sub_item_content.getJSONObject(k);
                                            String category_name = name2.getString("category_name");
                                            String sub_item_name = name2.getString("sub_item_name");
                                            String sub_item_price = name2.getString("total_pretty");

                                            price = price+Float.parseFloat(sub_item_price);


                                            l2.add(category_name);
                                            l2.add(sub_item_name);
                                            l2.add(sub_item_price);
                                            l2.add("\n");


                                        }*/

                                        String category_get1 = "", barcode_ge_table = "";
                                        if (name.contains("'")){
                                            name = name.replace("'", "");
                                        }
                                        if (online_name.contains("'")){
                                            online_name = online_name.replace("'", "");
                                        }

                                        Cursor cursor = db1.rawQuery("SELECT * FROM Items WHERE itemname = '"+online_name+"'", null);
                                        if (cursor.moveToFirst()){
                                            category_get1 = cursor.getString(4);
                                            barcode_ge_table = cursor.getString(16);
                                        }

                                        ContentValues newValues = new ContentValues();
                                        newValues.put("order_id", order_id_wera);
                                        newValues.put("quantity", qty);
                                        newValues.put("itemname", online_name);
                                        newValues.put("price", price0);
                                        newValues.put("total", total0);
                                        newValues.put("type", "Item");
                                        newValues.put("mod_assigned", "no");
                                        newValues.put("bill_no", printbillno);
                                        newValues.put("time", time1);
                                        newValues.put("datetimee", time24);
                                        newValues.put("datetimee_new", time24_new);
                                        newValues.put("date", dateget);
                                        newValues.put("date1", dateget1);
                                        newValues.put("user", username);//copy from home page
                                        newValues.put("table_id", "1");
                                        newValues.put("billtype", "  Home delivery");
                                        newValues.put("paymentmethod", orderchannel);
                                        newValues.put("billamount_disapply", totalprice1);
                                        newValues.put("billamount_disnotapply", totalprice1);
                                        newValues.put("disc_type", "%");
                                        newValues.put("disc_value", "0");
                                        newValues.put("newtotal", total0);
                                        newValues.put("disc_thereornot", "no");
                                        newValues.put("disc_indiv_total", "0.00");
                                        newValues.put("category", category_get1);
                                        newValues.put("counterperson_username", u_username);
                                        newValues.put("counterperson_name", u_name);
                                        newValues.put("barcode_get", barcode_ge_table);
                                        newValues.put("item_online", online_name);


                                        newValues.put("credit", "0");

                                        /*contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "All_Sales");
                                        resultUri = getContentResolver().insert(contentUri, newValues);
                                        getContentResolver().notifyChange(resultUri, null);*/
                                        System.out.println("DB stage 3 ");

                                        Cursor ffifteen_1 = db.rawQuery("SELECT * FROM All_Sales WHERE item_online = '" + online_name + "' AND bill_no = '"+ printbillno + "'", null);
                                        if (ffifteen_1.moveToFirst()) {
                                            String where11 = "item_online = '" + online_name + "' ";
                                            Log.d("All_sales table", "Update");
                                            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "All_Sales");
                                            getContentResolver().update(contentUri, newValues,where11,new String[]{});
                                            resultUri = new Uri.Builder()
                                                    .scheme("content")
                                                    .authority(StubProvider.AUTHORITY)
                                                    .path("All_Sales")
                                                    .appendQueryParameter("operation", "update")
                                                    .appendQueryParameter("item_online",online_name)
                                                    .build();
                                            getContentResolver().notifyChange(resultUri, null);

//                            db1.update("Billnumber", contentValues, where11, new String[]{});
                                        }else {
                                            Log.d("All_sales table", "Insert");
                                            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "All_Sales");
                                            resultUri = getContentResolver().insert(contentUri, newValues);
                                            getContentResolver().notifyChange(resultUri, null);
                                        }
                                        ffifteen_1.close();




//                                        db1.insert("all_sales", null, newValues);


                                        /*float p1 = 0;
                                        float g1 = 0;
                                        String deliv_char = "", p_c = "", g_c = "";*/

                                        //JSONArray extra = c.getJSONArray("addons");

                                        //  JSONArray extra = c.getJSONArray("extra");
                                    /*    if (order_info_id.toString().equals(order_id_wera)) {
//                                    date_time1.setText(date_time);

                                            for (int j1 = 0; j1 < extra.length(); j1++) {
                                                JSONObject name11 = extra.getJSONObject(j1);
                                                String type = name11.getString("type");
                                                String amount_pretty = name11.getJSONObject("addons_info").getString("price");


                                                if (type.toString().equals("Delivery Charge")){
                                                    deliv_char = amount_pretty;
                                                }

                                                if (type.toString().equals("Packaging Charge")){
                                                    p1 = Float.parseFloat(amount_pretty);
                                                    p_c = amount_pretty;
                                                }

                                                if (type.toString().equals("GST")){
                                                    g1 = Float.parseFloat(amount_pretty);
                                                    g_c = amount_pretty;
                                                }



                                            }
                                        }*/

                                        ContentValues contentValues1 = new ContentValues();
                                        contentValues1.put("billnumber", printbillno);
                                        contentValues1.put("total", totalprice1);
                                        contentValues1.put("user", username);
                                        contentValues1.put("date", currentDateandTime1);
                                        contentValues1.put("paymentmethod", orderchannel);
                                        contentValues1.put("billtype", "  Home delivery");
                                        contentValues1.put("subtotal", subtotal);
                                        contentValues1.put("taxtotal", g1);
                                        contentValues1.put("roundoff", 0);
                                        contentValues1.put("globaltaxtotal", 0);
                                        contentValues1.put("datetimee_new", time24_new);
                                        contentValues1.put("delivery_fee", deliv_char);
                                        contentValues1.put("packing_charges", p1);
                                        contentValues1.put("order_id", order_info_id);

                                        Cursor cdv = db.rawQuery("SELECT * FROM Billnumber WHERE order_id != '" + order_id_wera + "'", null);
                                        if (cdv.moveToFirst()){
                                            do {
                                                bill_c = cdv.getString(11);
                                            }while(cdv.moveToNext());
                                            TextView tv = new TextView(New_OrderActivity_w.this);
                                            tv.setText(bill_c);

                                            if (bill_c.contains("_")) {
                                                System.out.println("contains _");
                                                String match = "_";
                                                int position = bill_c.indexOf(match);
                                                String itemnameget1 = bill_c.substring(0, position);
                                                String string2 = bill_c.substring(position+1);
                                                bill_c = string2;
                                            }

                                            if (tv.getText().toString().equals("")){
                                                Cursor c_bill_count = db.rawQuery("SELECT * FROM BillCount", null);
                                                if (c_bill_count.moveToFirst()){
                                                    String bill_c = c_bill_count.getString(1);
                                                    int cdvv1 = Integer.parseInt(bill_c);
                                                    int cdvv = cdvv1+1;
                                                    String cdv2 = String.valueOf(cdvv);
                                                    contentValues1.put("billcount", bill_count_tag1+""+cdv2);
                                                    //Toast.makeText(Preparing_Orders.this, "billnumer is not there "+cdv2, Toast.LENGTH_LONG).show();
                                                }
                                                c_bill_count.close();
                                            }else {
                                                int cdvv1 = Integer.parseInt(bill_c);
                                                int cdvv = cdvv1 + 1;
                                                String cdv2 = String.valueOf(cdvv);
                                                contentValues1.put("billcount", bill_count_tag1+""+cdv2);
                                                //Toast.makeText(Preparing_Orders.this, "billnumer is there " + cdv2, Toast.LENGTH_LONG).show();
                                            }
                                        }else {
                                            Cursor c_bill_count = db.rawQuery("SELECT * FROM BillCount", null);
                                            if (c_bill_count.moveToFirst()){
                                                String bill_c = c_bill_count.getString(1);
                                                int cdvv1 = Integer.parseInt(bill_c);
                                                int cdvv = cdvv1+1;
                                                String cdv2 = String.valueOf(cdvv);
                                                contentValues1.put("billcount", bill_count_tag1+""+cdv2);
                                                //Toast.makeText(Preparing_Orders.this, "billnumer is not there "+cdv2, Toast.LENGTH_LONG).show();
                                            }
                                            c_bill_count.close();
                                        }

                                        /*contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Billnumber");
                                        resultUri = getContentResolver().insert(contentUri, contentValues1);
                                        getContentResolver().notifyChange(resultUri, null);*/
                                        System.out.println("DB stage 3a ");

                                        Cursor ffifteen_0 = db.rawQuery("SELECT * FROM Billnumber WHERE order_id = '" + order_id_wera + "'", null);
                                        if (ffifteen_0.moveToFirst()) {
                                            String where11 = "order_id = '" + order_id_wera + "' ";
                                            Log.d("Billnumber table", "Update");
                                            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Billnumber");
                                            getContentResolver().update(contentUri, contentValues1,where11,new String[]{});
                                            resultUri = new Uri.Builder()
                                                    .scheme("content")
                                                    .authority(StubProvider.AUTHORITY)
                                                    .path("Billnumber")
                                                    .appendQueryParameter("operation", "update")
                                                    .appendQueryParameter("order_id",order_id_wera)
                                                    .build();
                                            getContentResolver().notifyChange(resultUri, null);

//                            db1.update("Billnumber", contentValues, where11, new String[]{});
                                        }else {
                                            Log.d("Billnumber table", "Insert");
                                            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Billnumber");
                                            resultUri = getContentResolver().insert(contentUri, contentValues1);
                                            getContentResolver().notifyChange(resultUri, null);
                                        }
                                        ffifteen_0.close();

                                        // db1.insert("Billnumber", null, contentValues1);
//                                        permanent1.close();

                                        // JSONArray discount = c.getJSONArray("discount");

                                        if (order_info_id.toString().equals(order_id_wera)) {
//                                    date_time1.setText(date_time);
                                            // for (int j2 = 0; j2 < discount.length(); j2++) {
                                            //   JSONObject name12 = discount.getJSONObject(j2);
                                            String name2 = c.getString("discount");
                                            //String per = name12.getString("percent");
                                            String type = c.getString("discount_reason");

                                            float v = Float.parseFloat(totalprice1) - Float.parseFloat(name2);


                                            ContentValues contentValues2 = new ContentValues();
                                            contentValues2.put("date", currentDateandTime1);
                                            contentValues2.put("time", time1);
                                            contentValues2.put("billno", printbillno);
                                            contentValues2.put("Discountcodeno", type);
                                            contentValues2.put("Discount_percent", dis_per);
                                            contentValues2.put("Billamount_rupess", String.format("%.2f", v));
                                            contentValues2.put("Discount_rupees", name2);
                                            contentValues2.put("date1", normal1);
                                            contentValues2.put("original_amount", totalprice1);
                                            contentValues2.put("datetimee_new", time24_new);
                                            contentValues2.put("paymentmethod", orderchannel);

                                            Cursor cdv1 = db.rawQuery("Select * from Billnumber WHERE billnumber = '" + printbillno + "'", null);
                                            if (cdv1.moveToFirst()) {
                                                String cdv2 = cdv1.getString(11);
                                                ContentValues contentValuesh = new ContentValues();
                                                contentValuesh.put("billcount", bill_count_tag1+""+cdv2);
                                                String whereh = "billno = '" + printbillno + "' ";

                                                contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Discountdetails");
                                                getContentResolver().update(contentUri, contentValuesh,whereh,new String[]{});
                                                resultUri = new Uri.Builder()
                                                        .scheme("content")
                                                        .authority(StubProvider.AUTHORITY)
                                                        .path("Discountdetails" )
                                                        .appendQueryParameter("operation", "update")
                                                        .appendQueryParameter("billno",printbillno)
                                                        .build();
                                                getContentResolver().notifyChange(resultUri, null);
                                                System.out.println("DB stage 4a ");



                                                //      db1.update("Discountdetails", contentValuesh, whereh, new String[]{});
                                            }
                                            cdv1.close();

                                            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Discountdetails");
                                            resultUri = getContentResolver().insert(contentUri, contentValues2);
                                            getContentResolver().notifyChange(resultUri, null);
                                            System.out.println("DB stage 4b ");

                                            // db1.insert("Discountdetails", null, contentValues2);

                                            //}
                                        }



//                                        Country_Ingredient1_order NAME = new Country_Ingredient1_order(name, qty, price1, l2, "", instructions);
//                                        list.add(NAME);

                                    }

//                                    String subtotal = c.getJSONObject("subtotal").getString("amount_pretty");
//                                    sub_total.setText(subtotal);

                                }





                            }

//                            MyAdapter_Ingredient adapter = new MyAdapter_Ingredient(New_Individualorder_Itemview.this,list);
//                            listView.setAdapter(adapter);

                        } catch (final JSONException e) {
                            Log.e("TAG", "Json parsing error: " + e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    /*Toast.makeText(getApplicationContext(),
                                                    "Json parsing error: " + e.getMessage(),
                                                    Toast.LENGTH_LONG)
                                            .show();*/
                                    System.out.println("Json parsing error: "+e.getMessage());
                                }
                            });

                        }

                        // printer(billnum.getText().toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("New order page", "Error: " + error.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + error.getMessage(),
                                        Toast.LENGTH_LONG)
                                .show();*/
                        System.out.println("Json parsing error: " + error.getMessage());
                    }
                });
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> windex = new HashMap<String, String>();
                windex.put("db_name", company + "_"+ store + "_"+ device);
                windex.put("order_id", order_id_wera);
                //windex.put("order_status", status);
                return windex;
            }
        };
        mQueue1.add(strord);
        //  progressBar.setVisibility(View.VISIBLE);

    }

}

