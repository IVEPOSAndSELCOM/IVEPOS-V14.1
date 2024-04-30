package com.intuition.ivepos;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.intuition.ivepos.sync.StubProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Preparing_Orders extends AppCompatActivity implements RecyclerViewAdapter_preparing.ItemListener {

    RecyclerView recyclerView;
    ArrayList<DataModel> arrayList;

    String url_orders = "https://api.werafoods.com/pos/v2/merchant/orders";
    String url_pending = "https://api.werafoods.com/pos/v2/merchant/pendingorders";

    JSONObject jsonBody;

    private SQLiteDatabase db, db1;

    String dateget, dateget1, timeget;

    int i_ord;
    String username, u_username, u_name;
    String bill_c, bill_count_tag, bill_count_tag1 = "", bill_count_tag2 = "";

    Uri contentUri,resultUri;

    String text12, Text, stat;

    String str_merch_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_delivery_preparing_ui1);

        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        Cursor cursor_merchant_id = db.rawQuery("SELECT * FROM Restaurant_id", null);
        if (cursor_merchant_id.moveToFirst()){
            str_merch_id = cursor_merchant_id.getString(1);
        }
        cursor_merchant_id.close();

        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        arrayList = new ArrayList<>();

        LinearLayout back_activity = (LinearLayout) findViewById(R.id.back_activity);
        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
            //I try to use this for send Header is application/json
            jsonBody = new JSONObject();
            jsonBody.put("merchant_id", str_merch_id);
//            jsonBody.put("order_id", "2016204");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url_orders, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                                    JSONObject jsonObj = new JSONObject(response.toString());

                            // Getting JSON Array node
                            JSONObject details = response.getJSONObject("details");
                            JSONArray orders = details.getJSONArray("orders");
                            ArrayList<String> my_array = new ArrayList<String>();
                            // looping through All Contacts
                            for (int i = 0; i < orders.length(); i++) {
                                JSONObject c = orders.getJSONObject(i);

                                String order_info_id = c.getJSONObject("order_info").getString("order_id");
                                String order_type1 = c.getJSONObject("order_info").getString("order_type");
                                String date_time = c.getJSONObject("order_info").getString("transaction_date");
                                String payme_type = c.getJSONObject("order_info").getString("payment_type");
                                String status = c.getJSONObject("order_info").getString("status");
                                if (status.toString().equals("Accepted")) {


                                    String deli_per_name = c.getJSONObject("order_info").getString("rider_info");
                                    String deli_per_phon = "";

                                    if (deli_per_name.toString().equals("null")){
                                        System.out.println("data is not there");
                                        deli_per_name = "Not Assigned";
                                    }else {
                                        System.out.println("data is there");
                                        deli_per_name = c.getJSONObject("order_info").getJSONObject("rider_info").getString("rider_name");
                                        deli_per_phon = c.getJSONObject("order_info").getJSONObject("rider_info").getString("rider_phone_number");
                                    }




                                    String deliv_time1 = c.getJSONObject("order_info").getString("delivery_time");

                                    String match = " ";
                                    int aposition = date_time.toString().indexOf(match);
                                    String time1 = date_time.toString().substring(aposition);

                                    String cust_name1 = c.getJSONObject("customer").getString("name");
                                    String cust_phno = c.getJSONObject("customer").getString("mobile");
                                    String cust_street = c.getJSONObject("customer").getString("street");
                                    String cust_location = c.getJSONObject("customer").getString("location");
                                    String cust_deli_instru = c.getJSONObject("customer").getString("delivery_instruction");

                                    String totalprice1 = c.getJSONObject("grand_total").getString("amount_pretty");

                                    JSONArray cart = c.getJSONArray("cart");

//                                HashMap<String, String> contact = new HashMap<>();
//                                my_array.clear();

//                                contact.put("orderid", order_info_id);
                                    for (int j = 0; j < cart.length(); j++) {
                                        JSONObject name1 = cart.getJSONObject(j);
                                        String name = name1.getString("pos_item_name");
                                        String qty = name1.getString("qty");
                                        System.out.println("orderid: " + order_info_id + "  name: " + order_type1);//

//                                    my_array.add(name);
//                                    contact.put("itemname", String.valueOf(name));
//                                    contact.put("qty", qty);
//                                    contact.put("date_time", date_time);
//                                    contact.put("order_type", order_type);

//                                    Country_Ingredient NAME = new Country_Ingredient(order_info_id, order_type, date_time, payme_type, cust_name, cust_phno, cust_street, cust_location, cust_deli_instru);
//                                    list.add(NAME);

                                        arrayList.add(new DataModel(order_info_id, String.valueOf(cart.length()), totalprice1, order_type1, cust_name1, time1, status, deli_per_name, deli_per_phon, str_merch_id));

                                    }
                                }




                            }

                            RecyclerViewAdapter_preparing adapter = new RecyclerViewAdapter_preparing(Preparing_Orders.this, arrayList, Preparing_Orders.this);
                            recyclerView.setAdapter(adapter);

//                            recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                    final TextView checkbox1 = (TextView) view.getTag(R.id.order_id);
//                                    Toast.makeText(New_OrderActivity.this, "2 "+checkbox1.getText().toString(), Toast.LENGTH_LONG).show();
//
//
//                                    Intent intent = new Intent(New_OrderActivity.this, Items_View.class);
//                                    intent.putExtra("PLAYER1NAME", checkbox1.getText().toString());
//                                    startActivity(intent);
//
//
//                                }
//                            });

                        } catch (final JSONException e) {
                            Log.e("TAG", "Json parsing error: " + e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Json parsing error: " + e.getMessage(),
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            });

                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
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

        final Spinner pre_deli_pu_oth = (Spinner) findViewById(R.id.pre_deli_pu_oth);
        text12 = pre_deli_pu_oth.getSelectedItem().toString();

        final Spinner all_s_z_ue_fp = (Spinner) findViewById(R.id.all_s_z_ue_fp);
        Text = all_s_z_ue_fp.getSelectedItem().toString();

        if (Text.equals("All")) {
            Toast.makeText(Preparing_Orders.this, "ALL", Toast.LENGTH_LONG).show();
        }


        all_s_z_ue_fp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text12 = pre_deli_pu_oth.getSelectedItem().toString();
                String text1 = parent.getItemAtPosition(position).toString();
                if (text1.toString().equals("All")) {
                    Toast.makeText(Preparing_Orders.this, "Selected All", Toast.LENGTH_LONG).show();
                    all_new_orders(text12);
                }else {
                    if (text1.toString().equals("Swiggy")) {
                        Toast.makeText(Preparing_Orders.this, "Swiggy", Toast.LENGTH_LONG).show();
                        custom_new_orders("Swiggy", text12);
                    }else {
                        if (text1.toString().equals("Zomato")) {
                            Toast.makeText(Preparing_Orders.this, "Zomato", Toast.LENGTH_LONG).show();
                            custom_new_orders("Zomato", text12);
                        }else {
                            if (text1.toString().equals("Uber eats")) {
                                Toast.makeText(Preparing_Orders.this, "Uber eats", Toast.LENGTH_LONG).show();
                                custom_new_orders("Uber eats", text12);
                            }else {
                                if (text1.toString().equals("Food panda")) {
                                    Toast.makeText(Preparing_Orders.this, "Food panda", Toast.LENGTH_LONG).show();
                                    custom_new_orders("Food panda", text12);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        pre_deli_pu_oth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Text = all_s_z_ue_fp.getSelectedItem().toString();
                String text1 = parent.getItemAtPosition(position).toString();
                if (text1.toString().equals("Preparing")) {
                    stat = "Accepted";
                    Toast.makeText(Preparing_Orders.this, "Selected All", Toast.LENGTH_LONG).show();
                    custom_new_orders(Text, "Accepted");
                }else {
                    if (text1.toString().equals("picked up")) {
                        stat = "Picked Up";
                        Toast.makeText(Preparing_Orders.this, "Swiggy", Toast.LENGTH_LONG).show();
                        custom_new_orders(Text, "Picked Up");
                    }else {
                        if (text1.toString().equals("delivered")) {
                            stat = "Delivered";
                            Toast.makeText(Preparing_Orders.this, "Zomato", Toast.LENGTH_LONG).show();
                            custom_new_orders(Text, "Delivered");
                        }else {
                            if (text1.toString().equals("others")) {
                                stat = "Others";
                                Toast.makeText(Preparing_Orders.this, "Uber eats", Toast.LENGTH_LONG).show();
                                custom_new_orders(Text, "Others");
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        LinearLayout new_orders = (LinearLayout) findViewById(R.id.new_orders1);
        new_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Preparing_Orders.this, New_OrderActivity.class);
                startActivity(intent);
            }
        });

        final TextView new_orders_value = (TextView) findViewById(R.id.new_orders_value);

        try {
            //I try to use this for send Header is application/json
            jsonBody = new JSONObject();
            jsonBody.put("merchant_id", str_merch_id);
//            jsonBody.put("order_id", "2016204");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        RequestQueue mQueue1 = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(url_pending, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject details = response.getJSONObject("details");
                            JSONArray orders = details.getJSONArray("orders");
                            for (int i = 0; i < orders.length(); i++) {
                                JSONObject c = orders.getJSONObject(i);

                                String status = c.getJSONObject("order_info").getString("status");
//                                i_ord = 0;
//                                if (status.toString().equals("Accepted") || status.toString().equals("Picked Up") || status.toString().equals("Food Ready")) {
                                i_ord++;
                                new_orders_value.setText(String.valueOf(i_ord));
//                                }
                            }

                        } catch (final JSONException e) {
                            Log.e("TAG", "Json parsing error: " + e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Json parsing error: " + e.getMessage(),
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            });

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
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

    }

    public void custom_new_orders(final String ord_type, final String sta){

//        String stat = "";
        if (sta.toString().equalsIgnoreCase("Preparing") || ord_type.toString().equals("Accepted") || sta.toString().equalsIgnoreCase("Accepted")){
            stat = "Accepted";
        }
        if (sta.toString().equalsIgnoreCase("picked up") || ord_type.toString().equals("Picked Up")){
            stat = "Picked Up";
        }
        if (sta.toString().equalsIgnoreCase("delivered") || ord_type.toString().equals("Delivered")){
            stat = "Delivered";
        }
//        arrayList = new ArrayList<>();
        final ArrayList arrayList = new ArrayList<>();
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle);
        try {
            //I try to use this for send Header is application/json
            jsonBody = new JSONObject();
            jsonBody.put("merchant_id", str_merch_id);
//            jsonBody.put("order_id", "2016204");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

        final String finalStat = stat;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url_orders, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                                    JSONObject jsonObj = new JSONObject(response.toString());

                            // Getting JSON Array node
                            JSONObject details = response.getJSONObject("details");
                            JSONArray orders = details.getJSONArray("orders");
                            ArrayList<String> my_array = new ArrayList<String>();
                            // looping through All Contacts
                            for (int i = 0; i < orders.length(); i++) {
                                JSONObject c = orders.getJSONObject(i);

                                String order_info_id = c.getJSONObject("order_info").getString("order_id");
                                String order_type1 = c.getJSONObject("order_info").getString("order_type");
                                if (ord_type.toString().equalsIgnoreCase("All")){
                                    String date_time = c.getJSONObject("order_info").getString("transaction_date");
                                    String payme_type = c.getJSONObject("order_info").getString("payment_type");
                                    String status = c.getJSONObject("order_info").getString("status");
                                    if (status.toString().equals(finalStat)) {

                                        Toast.makeText(Preparing_Orders.this, "" + ord_type + " " + finalStat, Toast.LENGTH_LONG).show();
                                        System.out.println("selected id is " + ord_type + "" + finalStat);


                                        String deli_per_name = c.getJSONObject("order_info").getString("rider_info");
                                        String deli_per_phon = "";

                                        if (deli_per_name.toString().equals("null")) {
                                            System.out.println("data is not there");
                                            deli_per_name = "Not Assigned";
                                        } else {
                                            System.out.println("data is there");
                                            deli_per_name = c.getJSONObject("order_info").getJSONObject("rider_info").getString("rider_name");
                                            deli_per_phon = c.getJSONObject("order_info").getJSONObject("rider_info").getString("rider_phone_number");
                                        }


                                        String deliv_time1 = c.getJSONObject("order_info").getString("delivery_time");

                                        String match = " ";
                                        int aposition = date_time.toString().indexOf(match);
                                        String time1 = date_time.toString().substring(aposition);

                                        String cust_name1 = c.getJSONObject("customer").getString("name");
                                        String cust_phno = c.getJSONObject("customer").getString("mobile");
                                        String cust_street = c.getJSONObject("customer").getString("street");
                                        String cust_location = c.getJSONObject("customer").getString("location");
                                        String cust_deli_instru = c.getJSONObject("customer").getString("delivery_instruction");

                                        String totalprice1 = c.getJSONObject("grand_total").getString("amount_pretty");

                                        JSONArray cart = c.getJSONArray("cart");

//                                contact.put("orderid", order_info_id);
                                        for (int j = 0; j < cart.length(); j++) {
                                            JSONObject name1 = cart.getJSONObject(j);
                                            String name = name1.getString("pos_item_name");
                                            String qty = name1.getString("qty");
                                            System.out.println("orderid: " + order_info_id + "  name: " + order_type1);//

                                            arrayList.add(new DataModel(order_info_id, String.valueOf(cart.length()), totalprice1, order_type1, cust_name1, time1, status, deli_per_name, deli_per_phon, str_merch_id));

                                        }
                                    }
                                }else {
                                    if (order_type1.toString().equals(ord_type)) {
                                        String date_time = c.getJSONObject("order_info").getString("transaction_date");
                                        String payme_type = c.getJSONObject("order_info").getString("payment_type");
                                        String status = c.getJSONObject("order_info").getString("status");
                                        if (status.toString().equals(finalStat)) {

                                            Toast.makeText(Preparing_Orders.this, "" + ord_type + " " + finalStat, Toast.LENGTH_LONG).show();
                                            System.out.println("selected id is " + ord_type + "" + finalStat);


                                            String deli_per_name = c.getJSONObject("order_info").getString("rider_info");
                                            String deli_per_phon = "";

                                            if (deli_per_name.toString().equals("null")) {
                                                System.out.println("data is not there");
                                                deli_per_name = "Not Assigned";
                                            } else {
                                                System.out.println("data is there");
                                                deli_per_name = c.getJSONObject("order_info").getJSONObject("rider_info").getString("rider_name");
                                                deli_per_phon = c.getJSONObject("order_info").getJSONObject("rider_info").getString("rider_phone_number");
                                            }


                                            String deliv_time1 = c.getJSONObject("order_info").getString("delivery_time");

                                            String match = " ";
                                            int aposition = date_time.toString().indexOf(match);
                                            String time1 = date_time.toString().substring(aposition);

                                            String cust_name1 = c.getJSONObject("customer").getString("name");
                                            String cust_phno = c.getJSONObject("customer").getString("mobile");
                                            String cust_street = c.getJSONObject("customer").getString("street");
                                            String cust_location = c.getJSONObject("customer").getString("location");
                                            String cust_deli_instru = c.getJSONObject("customer").getString("delivery_instruction");

                                            String totalprice1 = c.getJSONObject("grand_total").getString("amount_pretty");

                                            JSONArray cart = c.getJSONArray("cart");

//                                contact.put("orderid", order_info_id);
                                            for (int j = 0; j < cart.length(); j++) {
                                                JSONObject name1 = cart.getJSONObject(j);
                                                String name = name1.getString("pos_item_name");
                                                String qty = name1.getString("qty");
                                                System.out.println("orderid: " + order_info_id + "  name: " + order_type1);//

                                                arrayList.add(new DataModel(order_info_id, String.valueOf(cart.length()), totalprice1, order_type1, cust_name1, time1, status, deli_per_name, deli_per_phon, str_merch_id));

                                            }
                                        }
                                    }
                                }
                            }

                            RecyclerViewAdapter_preparing adapter = new RecyclerViewAdapter_preparing(Preparing_Orders.this, arrayList, Preparing_Orders.this);
                            recyclerView.setAdapter(adapter);

                        } catch (final JSONException e) {
                            Log.e("TAG", "Json parsing error: " + e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Json parsing error: " + e.getMessage(),
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            });

                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
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


    public void all_new_orders(final String sta){

//        String stat = "";
        if (sta.toString().equalsIgnoreCase("Preparing")){
            stat = "Accepted";
        }
        if (sta.toString().equalsIgnoreCase("picked up")){
            stat = "Picked Up";
        }
        if (sta.toString().equalsIgnoreCase("delivered")){
            stat = "Delivered";
        }

        final ArrayList arrayList = new ArrayList<>();
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle);
        try {
            //I try to use this for send Header is application/json
            jsonBody = new JSONObject();
            jsonBody.put("merchant_id", str_merch_id);
//            jsonBody.put("order_id", "2016204");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

        final String finalStat = stat;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url_orders, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                                    JSONObject jsonObj = new JSONObject(response.toString());

                            // Getting JSON Array node
                            JSONObject details = response.getJSONObject("details");
                            JSONArray orders = details.getJSONArray("orders");
                            ArrayList<String> my_array = new ArrayList<String>();
                            // looping through All Contacts
                            for (int i = 0; i < orders.length(); i++) {
                                JSONObject c = orders.getJSONObject(i);

                                String order_info_id = c.getJSONObject("order_info").getString("order_id");
                                String order_type1 = c.getJSONObject("order_info").getString("order_type");
                                String date_time = c.getJSONObject("order_info").getString("transaction_date");
                                String payme_type = c.getJSONObject("order_info").getString("payment_type");
                                String status = c.getJSONObject("order_info").getString("status");
                                if (status.toString().equals(finalStat)) {


                                    String deli_per_name = c.getJSONObject("order_info").getString("rider_info");
                                    String deli_per_phon = "";

                                    if (deli_per_name.toString().equals("null")){
                                        System.out.println("data is not there");
                                        deli_per_name = "Not Assigned";
                                    }else {
                                        System.out.println("data is there");
                                        deli_per_name = c.getJSONObject("order_info").getJSONObject("rider_info").getString("rider_name");
                                        deli_per_phon = c.getJSONObject("order_info").getJSONObject("rider_info").getString("rider_phone_number");
                                    }


                                    String deliv_time1 = c.getJSONObject("order_info").getString("delivery_time");

                                    String match = " ";
                                    int aposition = date_time.toString().indexOf(match);
                                    String time1 = date_time.toString().substring(aposition);

                                    String cust_name1 = c.getJSONObject("customer").getString("name");
                                    String cust_phno = c.getJSONObject("customer").getString("mobile");
                                    String cust_street = c.getJSONObject("customer").getString("street");
                                    String cust_location = c.getJSONObject("customer").getString("location");
                                    String cust_deli_instru = c.getJSONObject("customer").getString("delivery_instruction");

                                    String totalprice1 = c.getJSONObject("grand_total").getString("amount_pretty");

                                    JSONArray cart = c.getJSONArray("cart");

//                                contact.put("orderid", order_info_id);
                                    for (int j = 0; j < cart.length(); j++) {
                                        JSONObject name1 = cart.getJSONObject(j);
                                        String name = name1.getString("pos_item_name");
                                        String qty = name1.getString("qty");
                                        System.out.println("orderid: " + order_info_id + "  name: " + order_type1);//

                                        arrayList.add(new DataModel(order_info_id, String.valueOf(cart.length()), totalprice1, order_type1, cust_name1, time1, status, deli_per_name, deli_per_phon, str_merch_id));

                                    }
                                }
                            }

                            RecyclerViewAdapter_preparing adapter = new RecyclerViewAdapter_preparing(Preparing_Orders.this, arrayList, Preparing_Orders.this);
                            recyclerView.setAdapter(adapter);

                        } catch (final JSONException e) {
                            Log.e("TAG", "Json parsing error: " + e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Json parsing error: " + e.getMessage(),
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            });

                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
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

    @Override
    public void onItemClick(DataModel item) {
        Toast.makeText(getApplicationContext(), item.text + " is clicked", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Preparing_Orders.this, New_Individualorder_Itemview.class);
        intent.putExtra("order_id", item.text);
        intent.putExtra("total_price", item.totalprice);
        intent.putExtra("order_type", item.order_type);
        startActivity(intent);
    }


    public void saveinDB(final String order_id_wera){

        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

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

        Cursor count = db1.rawQuery("SELECT * FROM Billnumber WHERE billnumber LIKE  '" + currentDateandTime + "-" + "000" + "0" + "1" + "%' ", null);

        final int bill = count.getCount() + 1;
        String bills = String.valueOf(bill);

        final TextView billnum = new TextView(Preparing_Orders.this);
        billnum.setText(currentDateandTime + "-" + "000" + "0" + "1" + bills);


        Cursor cursor_user = db.rawQuery("SELECT * FROM LoginUser", null);
        if (cursor_user.moveToFirst()) {
            u_username = cursor_user.getString(1);
            username = cursor_user.getString(1);
            String password = cursor_user.getString(2);
            Cursor ladmin = db.rawQuery("SELECT * FROM LAdmin WHERE username = '" + u_username + "'", null);
            if (ladmin.moveToFirst()) {
                u_name = ladmin.getString(3);
            } else {
                Cursor madmin = db.rawQuery("SELECT * FROM LOGIN WHERE username = '" + u_username + "'", null);
                if (madmin.moveToFirst()) {
                    u_name = madmin.getString(3);
                } else {
                    Cursor user1 = db.rawQuery("SELECT * FROM User1 WHERE username = '" + u_username + "'", null);
                    if (user1.moveToFirst()) {
                        u_name = user1.getString(1);
                    } else {
                        Cursor user2 = db.rawQuery("SELECT * FROM User2 WHERE username = '" + u_username + "'", null);
                        if (user2.moveToFirst()) {
                            u_name = user2.getString(1);
                        } else {
                            Cursor user3 = db.rawQuery("SELECT * FROM User3 WHERE username = '" + u_username + "'", null);
                            if (user3.moveToFirst()) {
                                u_name = user3.getString(1);
                            } else {
                                Cursor user4 = db.rawQuery("SELECT * FROM User4 WHERE username = '" + u_username + "'", null);
                                if (user4.moveToFirst()) {
                                    u_name = user4.getString(1);
                                } else {
                                    Cursor user5 = db.rawQuery("SELECT * FROM User5 WHERE username = '" + u_username + "'", null);
                                    if (user5.moveToFirst()) {
                                        u_name = user5.getString(1);
                                    } else {
                                        Cursor user6 = db.rawQuery("SELECT * FROM User6 WHERE username = '" + u_username + "'", null);
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

        Cursor cursor_bill_count_tag = db.rawQuery("SELECT * FROM Billcount_tag", null);
        if (cursor_bill_count_tag.moveToFirst()){
            bill_count_tag = cursor_bill_count_tag.getString(1);
            bill_count_tag1 = bill_count_tag+"_";
            bill_count_tag2 = bill_count_tag+"_";
        }else {
            bill_count_tag1 = "";
            bill_count_tag2 = "";
        }

        try {
            //I try to use this for send Header is application/json
            jsonBody = new JSONObject();
            jsonBody.put("merchant_id", str_merch_id);
            jsonBody.put("order_id", order_id_wera);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url_orders, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                                    JSONObject jsonObj = new JSONObject(response.toString());

                            // Getting JSON Array node
                            JSONObject details = response.getJSONObject("details");
                            JSONArray orders = details.getJSONArray("orders");
                            ArrayList<String> my_array = new ArrayList<String>();
                            // looping through All Contacts
                            for (int i = 0; i < orders.length(); i++) {
                                JSONObject c = orders.getJSONObject(i);

                                String order_info_id = c.getJSONObject("order_info").getString("order_id");
                                String order_type = c.getJSONObject("order_info").getString("order_type");
                                String date_time = c.getJSONObject("order_info").getString("transaction_date");
                                String payme_type = c.getJSONObject("order_info").getString("payment_type");


//                                cust_name = c.getJSONObject("customer").getString("name");
//                                cust_phno = c.getJSONObject("customer").getString("mobile");
//                                cust_street = c.getJSONObject("customer").getString("street");
//                                cust_location = c.getJSONObject("customer").getString("location");
                                String cust_deli_instru = c.getJSONObject("customer").getString("delivery_instruction");

                                String totalprice1 = c.getJSONObject("grand_total").getString("amount_pretty");
                                String subtotal = c.getJSONObject("subtotal").getString("amount_pretty");

                                JSONArray cart = c.getJSONArray("cart");

//                                HashMap<String, String> contact = new HashMap<>();
//                                my_array.clear();

//                                contact.put("orderid", order_info_id);

                                ArrayList l2 = new ArrayList();

                                if (order_info_id.toString().equals(order_id_wera)) {
//                                    date_time1.setText(date_time);
                                    for (int j = 0; j < cart.length(); j++) {
                                        JSONObject name1 = cart.getJSONObject(j);
                                        String name = name1.getString("pos_item_name");
                                        String qty = name1.getString("qty");
                                        String price0 = name1.getString("price_pretty");
                                        String total0 = name1.getString("total_pretty");
                                        System.out.println("orderid: " + order_info_id + "  name: " + order_type);

//                                        Country_Ingredient1_order NAME1 = new Country_Ingredient1_order(name, qty, price, , "");
//                                        list.add(NAME1);

                                        float price = 0;

                                        JSONObject sub_item = name1.getJSONObject("sub_item");
                                        JSONArray sub_item_content = sub_item.getJSONArray("sub_item_content");

                                        for (int k = 0; k < sub_item_content.length(); k++) {
                                            JSONObject name2 = sub_item_content.getJSONObject(k);
                                            String category_name = name2.getString("category_name");
                                            String sub_item_name = name2.getString("sub_item_name");
                                            String sub_item_price = name2.getString("total_pretty");

                                            price = price+Float.parseFloat(sub_item_price);


                                            l2.add(category_name);
                                            l2.add(sub_item_name);
                                            l2.add(sub_item_price);
                                            l2.add("\n");




                                        }

                                        String category_get1 = "", barcode_ge_table = "";
                                        if (name.contains("'")){
                                            name = name.replace("'", "");
                                        }

                                        Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE itemname = '"+name+"'", null);
                                        if (cursor.moveToFirst()){
                                            category_get1 = cursor.getString(4);
                                            barcode_ge_table = cursor.getString(16);
                                        }

                                        ContentValues newValues = new ContentValues();
                                        newValues.put("order_id", order_id_wera);
                                        newValues.put("quantity", qty);
                                        newValues.put("itemname", name);
                                        newValues.put("price", price0);
                                        newValues.put("total", total0);
                                        newValues.put("type", "Item");
                                        newValues.put("bill_no", billnum.getText().toString());
                                        newValues.put("time", time1);
                                        newValues.put("datetimee", time24);
                                        newValues.put("datetimee_new", time24_new);
                                        newValues.put("date", dateget);
                                        newValues.put("date1", dateget1);
                                        newValues.put("user", username);//copy from home page
                                        newValues.put("table_id", "1");
                                        newValues.put("billtype", "  Home delivery");
                                        newValues.put("paymentmethod", order_type);
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


//                                        Cursor cursor_hsn = db.rawQuery("SELECT * FROM Taxes WHERE taxname = '" + taxnameget + "'", null);
//                                        if (cursor_hsn.moveToFirst()) {
//                                            String hsn_code = cursor_hsn.getString(8);
//                                            newValues.put("hsn_code", hsn_code);
//                                        } else {
//                                            newValues.put("hsn_code", "");
//                                            Cursor cursor_hsn2 = db.rawQuery("SELECT * FROM Taxes WHERE taxname = '" + taxx2 + "'", null);
//                                            if (cursor_hsn2.moveToFirst()) {
//                                                String hsn_code = cursor_hsn2.getString(8);
//                                                newValues.put("hsn_code", hsn_code);
//                                            } else {
//                                                newValues.put("hsn_code", "");
//                                                Cursor cursor_hsn3 = db.rawQuery("SELECT * FROM Taxes WHERE taxname = '" + taxx3 + "'", null);
//                                                if (cursor_hsn3.moveToFirst()) {
//                                                    String hsn_code = cursor_hsn3.getString(8);
//                                                    newValues.put("hsn_code", hsn_code);
//                                                } else {
//                                                    newValues.put("hsn_code", "");
//                                                    Cursor cursor_hsn4 = db.rawQuery("SELECT * FROM Taxes WHERE taxname = '" + taxx4 + "'", null);
//                                                    if (cursor_hsn4.moveToFirst()) {
//                                                        String hsn_code = cursor_hsn4.getString(8);
//                                                        newValues.put("hsn_code", hsn_code);
//                                                    } else {
//                                                        newValues.put("hsn_code", "");
//                                                        Cursor cursor_hsn5 = db.rawQuery("SELECT * FROM Taxes WHERE taxname = '" + taxx5 + "'", null);
//                                                        if (cursor_hsn5.moveToFirst()) {
//                                                            String hsn_code = cursor_hsn5.getString(8);
//                                                            newValues.put("hsn_code", hsn_code);
//                                                        } else {
//                                                            newValues.put("hsn_code", "");
//                                                        }
//                                                        cursor_hsn5.close();
//                                                    }
//                                                    cursor_hsn4.close();
//                                                }
//                                                cursor_hsn3.close();
//                                            }
//                                            cursor_hsn2.close();
//                                        }
//                                        cursor_hsn.close();

                                        newValues.put("credit", "0");

                                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "All_Sales");
                                        resultUri = getContentResolver().insert(contentUri, newValues);
                                        getContentResolver().notifyChange(resultUri, null);


//                                        db1.insert("all_sales", null, newValues);


                                        float p1 = 0;
                                        float g1 = 0;
                                        String deliv_char = "", p_c = "", g_c = "";

                                        JSONArray extra = c.getJSONArray("extra");
                                        if (order_info_id.toString().equals(order_id_wera)) {
//                                    date_time1.setText(date_time);
                                            for (int j1 = 0; j1 < extra.length(); j1++) {
                                                JSONObject name11 = extra.getJSONObject(j1);
                                                String type = name11.getString("type");
                                                String amount_pretty = name11.getString("amount_pretty");


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
                                        }

                                        ContentValues contentValues1 = new ContentValues();
                                        contentValues1.put("billnumber", billnum.getText().toString());
                                        contentValues1.put("total", totalprice1);
                                        contentValues1.put("user", username);
                                        contentValues1.put("date", currentDateandTime1);
                                        contentValues1.put("paymentmethod", order_type);
                                        contentValues1.put("billtype", "  Home delivery");
                                        contentValues1.put("subtotal", subtotal);
                                        contentValues1.put("taxtotal", String.valueOf(g_c));
//                                        contentValues1.put("roundoff", dialog_rounded.getText().toString());
//                                        contentValues1.put("globaltaxtotal", gtotal1);
                                        contentValues1.put("datetimee_new", time24_new);
                                        contentValues1.put("delivery_fee", deliv_char);
                                        contentValues1.put("packing_charges", p_c);

                                        Cursor cdv = db1.rawQuery("Select * from Billnumber", null);
                                        if (cdv.moveToFirst()){
                                            do {
                                                bill_c = cdv.getString(11);
                                            }while(cdv.moveToNext());
                                            TextView tv = new TextView(Preparing_Orders.this);
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
                                                Cursor c_bill_count = db1.rawQuery("SELECT * FROM BillCount", null);
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
                                            Cursor c_bill_count = db1.rawQuery("SELECT * FROM BillCount", null);
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

                                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Billnumber");
                                        resultUri = getContentResolver().insert(contentUri, contentValues1);
                                        getContentResolver().notifyChange(resultUri, null);

                                        // db1.insert("Billnumber", null, contentValues1);
//                                        permanent1.close();

                                        JSONArray discount = c.getJSONArray("discount");

                                        if (order_info_id.toString().equals(order_id_wera)) {
//                                    date_time1.setText(date_time);
                                            for (int j2 = 0; j2 < discount.length(); j2++) {
                                                JSONObject name12 = discount.getJSONObject(j2);
                                                String name2 = name12.getString("amount_pretty");
                                                String per = name12.getString("percent");
                                                String type = name12.getString("type");

                                                float v = Float.parseFloat(totalprice1) - Float.parseFloat(name2);

                                                ContentValues contentValues2 = new ContentValues();
                                                contentValues2.put("date", currentDateandTime1);
                                                contentValues2.put("time", time1);
                                                contentValues2.put("billno", billnum.getText().toString());
                                                contentValues2.put("Discountcodeno", type);
                                                contentValues2.put("Discount_percent", per);
                                                contentValues2.put("Billamount_rupess", String.format("%.2f", v));
                                                contentValues2.put("Discount_rupees", name2);
                                                contentValues2.put("date1", normal1);
                                                contentValues2.put("original_amount", totalprice1);
                                                contentValues2.put("datetimee_new", time24_new);
                                                contentValues2.put("paymentmethod", order_type);

                                                Cursor cdv1 = db1.rawQuery("Select * from Billnumber WHERE billnumber = '" + billnum.getText().toString() + "'", null);
                                                if (cdv1.moveToFirst()) {
                                                    String cdv2 = cdv1.getString(11);
                                                    ContentValues contentValuesh = new ContentValues();
                                                    contentValuesh.put("billcount", bill_count_tag1+""+cdv2);
                                                    String whereh = "billno = '" + billnum.getText().toString() + "' ";

                                                    contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Discountdetails");
                                                    getContentResolver().update(contentUri, contentValuesh,whereh,new String[]{});
                                                    resultUri = new Uri.Builder()
                                                            .scheme("content")
                                                            .authority(StubProvider.AUTHORITY)
                                                            .path("Discountdetails" )
                                                            .appendQueryParameter("operation", "update")
                                                            .appendQueryParameter("billno",billnum.getText().toString())
                                                            .build();
                                                    getContentResolver().notifyChange(resultUri, null);



                                                    //      db1.update("Discountdetails", contentValuesh, whereh, new String[]{});
                                                }
                                                cdv1.close();

                                                contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Discountdetails");
                                                resultUri = getContentResolver().insert(contentUri, contentValues2);
                                                getContentResolver().notifyChange(resultUri, null);

                                                // db1.insert("Discountdetails", null, contentValues2);

                                            }
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
                                    Toast.makeText(getApplicationContext(),
                                            "Json parsing error: " + e.getMessage(),
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            });

                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
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
}
