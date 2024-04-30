package com.intuition.ivepos;

import static com.android.volley.Request.Method.POST;
import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cancelledorders_w extends AppCompatActivity implements RecyclerViewAdapter_cancel.ItemListener{
    RecyclerView recyclerView;
    Spinner all_s_z_ue_fp;
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
    String url_menuupload = "https://api.werafoods.com/pos/v2/menu/upload";
    String Url_menulink;
    String saveorder = "http://theandroidpos.com/IVEPOS_NEW/wera/save_order_details.php";
    String updateorder = "http://theandroidpos.com/IVEPOS_NEW/wera/update_order_details.php";
    String weraorderdetails = "http://theandroidpos.com/IVEPOS_NEW/wera/order_details_wera.php";


    JSONObject jsonBody;
    //    ListView listView;
    ArrayList<HashMap<String, String>> contactList;

    int i_ord;

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

    String WebserviceUrl,ordstatus, st;
    private Context activity;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancelorder_w);

        db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);

        Cursor cursor_merchant_id = db.rawQuery("SELECT * FROM Restaurant_id", null);
        if (cursor_merchant_id.moveToFirst()){
            str_merch_id = cursor_merchant_id.getString(1);
        }
        cursor_merchant_id.close();

        recyclerView = (RecyclerView) findViewById(R.id.cancelrecycle);
        arrayList = new ArrayList<>();
        //all_s_z_ue_fp = (Spinner) findViewById(R.id.all_s_z_ue_fp);

        /*new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                System.out.println("Request OTP after " + millisUntilFinished / 1000+" seconds");
                // logic to set the EditText could go here
            }
            public void onFinish() {
                *//*all_new_orders();
                orderpreparing();
                getcancelledstatus();
                Intent intent = new Intent(Cancelledorders_w.this, Cancelledorders_w.class);
                startActivity(intent);*//*
                refresh();
            }
        }.start();*/


        LinearLayout back_activity = (LinearLayout) findViewById(R.id.cback_activity);
        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Cancelledorders_w.this, New_OrderActivity_w.class);
                finish();
                startActivity(intent);

            }
        });

        LinearLayout refreshlayout = (LinearLayout) findViewById(R.id.crefreshlay);
        refreshlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        SharedPreferences sharedpreferences_select =  SplashScreenActivity.getDefaultSharedPreferencesMultiProcess(Cancelledorders_w.this);
        account_selection= sharedpreferences_select.getString("account_selection", null);

        if (account_selection.toString().equals("Dine")) {
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }else {
            if (account_selection.toString().equals("Qsr")) {
                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
            }else {
                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
            }
        }
        SharedPreferences sharedpreferences=getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        company= sharedpreferences.getString("companyname", null);
        store= sharedpreferences.getString("storename", null);
        device= sharedpreferences.getString("devicename", null);


        LinearLayout preparing_orders = (LinearLayout) findViewById(R.id.new_orders);
        preparing_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Cancelledorders_w.this, New_OrderActivity_w.class);
                finish();
                startActivity(intent);

            }
        });

        LinearLayout cancelorderlayout = (LinearLayout) findViewById(R.id.orderpreparing);
        cancelorderlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Cancelledorders_w.this, Preparing_Orders_w.class);
                finish();
                startActivity(intent);

            }
        });

        all_new_orders();
        orderpreparing();
        getcancelledstatus();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Cancelledorders_w.this, New_OrderActivity_w.class);
        finish();
        startActivity(intent);

    }
    private void orderpreparing() {
            final TextView preparing_orders_value = (TextView) findViewById(R.id.preparing_orders_valuec);
            i_ord = 0;
            System.out.println("Preparing order counter ");
        RequestQueue mQueue2 = Volley.newRequestQueue(getApplicationContext());
        StringRequest strord = new StringRequest(POST, weraorderdetails, new Response.Listener<String>() {
            @Override
            public void onResponse(String response2) {
                DataModel_w id = null;
                ArrayList orddatadb = new ArrayList<>();
                System.out.println("preparing counter " + response2 );
                //for(int i=0;i<response.length();i++) {
                try {
                    //JSONObject ord = new JSONObject(response);
                    JSONArray orddetails = new JSONArray(response2);
                    for (int k = 0; k < orddetails.length(); k++) {
                        JSONObject c = orddetails.getJSONObject(k);
                        String ordid = c.getString("_id");
                        String status = c.getString("order_status");
                        if(status.equalsIgnoreCase("auto-accept")){
                            status="Accepted";
                        }
                        if (status.toString().equalsIgnoreCase("Accepted")) {
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

    private void all_new_orders() {
        final TextView new_orders_value = (TextView) findViewById(R.id.new_orders_valuec);

        RequestQueue mQueue2 = Volley.newRequestQueue(getApplicationContext());
        StringRequest strord = new StringRequest(POST, weraorderdetails, new Response.Listener<String>() {
            @Override
            public void onResponse(String response2) {
                DataModel_w id = null;
                ArrayList orddatadb = new ArrayList<>();
                System.out.println("preparing counter " + response2 );
                //for(int i=0;i<response.length();i++) {
                try {
                    //JSONObject ord = new JSONObject(response);
                    JSONArray orddetails = new JSONArray(response2);
                    for (int k = 0; k < orddetails.length(); k++) {
                        JSONObject c = orddetails.getJSONObject(k);
                        String ordid = c.getString("_id");
                        String status = c.getString("order_status");
                        if (status.toString().equalsIgnoreCase("Pending")) {
                            i_ord++;
                            new_orders_value.setText(String.valueOf(i_ord));
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
                //windex.put("order_status", "Accepted");
                return windex;
            }
        };
        mQueue2.add(strord);
    }
    /*public void refresh(){
        recreate();
    }*/

    private void getcancelledstatus() {


                                    RequestQueue mQueue2 = Volley.newRequestQueue(getApplicationContext());
                                   // String finalOrderid = orderid;
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
                                                    String order_status = c.getString("order_status");
                                                    if (order_status.contains("Cancelled") || order_status.equalsIgnoreCase("timeout") || order_status.equalsIgnoreCase("reject") || order_status.equalsIgnoreCase("rejected") || order_status.contains("cancel")) {
                                                    //if (ordid.equalsIgnoreCase(finalOrderid)) {
                                                        String num_item = c.getString("num_item");
                                                        String gross_amount = c.getString("gross_amount");
                                                        String order_from = c.getString("order_from");
                                                        String cus_name1 = c.getString("cus_name");
                                                        String order_reason = c.getString("order_reason");
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
                                                    JSONArray cart = c.getJSONArray("orderItemsJSON");
                                                       // if (status.toString().contains("Cancelled")) {

                                                            id = new DataModel_w(ordid, num_item, gross_amount, order_from, cus_name1, cust_phno, cust_address, cust_deli_instru,cust_deliv_area,cust_email, order_status, order_reason, deli_per_name, deli_per_phon, deli_per_status,cart,"" ,  str_merch_id, deli_arrivetime);
                                                            arrayList.add(id);
                                                        }
                                                    //}
                                                }
                                                RecyclerViewAdapter_cancel canceladapter= new RecyclerViewAdapter_cancel(Cancelledorders_w.this, arrayList,Cancelledorders_w.this);
                                                recyclerView.setAdapter(canceladapter);
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
                                            //windex.put("order_status", status);
                                            return windex;
                                        }
                                    };
                                    mQueue2.add(strord);


    }

    @Override
    public void onItemClick(DataModel_w item) {

    }
}
