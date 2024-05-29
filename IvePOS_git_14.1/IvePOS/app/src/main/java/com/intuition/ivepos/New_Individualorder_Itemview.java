package com.intuition.ivepos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.intuition.ivepos.BluetoothPrintDriver.BT_Write;

public class New_Individualorder_Itemview extends AppCompatActivity {


    String url_pending = "https://api.werafoods.com/pos/v2/merchant/pendingorders";
    String url_orders = "https://api.werafoods.com/pos/v2/merchant/orders";
    JSONObject jsonBody;

    String p_c, g_c;

    ArrayList<Country_Ingredient1_order> list = new ArrayList<Country_Ingredient1_order>();

    String cust_name, cust_phno, cust_street, cust_location;

    String addget, nameget, statussusb, deviceget;
    String ipnameget, portget, statusnet;
    String addgets, namegets, statussusbs;
    String ipnamegets, portgets, statusnets;
    String ipnamegets_kot1, portgets_kot1, statusnets_kot1, name_kot1;
    String ipnamegets_kot2, portgets_kot2, statusnets_kot2, name_kot2;
    String ipnamegets_kot3, portgets_kot3, statusnets_kot3, name_kot3;
    String ipnamegets_kot4, portgets_kot4, statusnets_kot4, name_kot4;

    private WifiPrintDriver wifiSocket = null;
    private WifiPrintDriver2 wifiSocket2 = null;
    private WifiPrintDriver_kot1 wifiSocket_kot1 = null;
    private WifiPrintDriver_kot2 wifiSocket_kot2 = null;
    private WifiPrintDriver_kot3 wifiSocket_kot3 = null;
    private WifiPrintDriver_kot4 wifiSocket_kot4 = null;

    byte[] setHT32, setHT321, setHT33, setHT34, setHT3212, setHTKOT, feedcut2;
    int nPaperWidth;

    String str_print_ty, NAME, strcompanyname, order_type, order_id;
    String straddress1, straddress2, straddress3, strphone, stremailid, strwebsite, strtaxone, strbillone;
    String u_name, u_username, username, password;

    int iRowNumber, charlength, charlength1, charlength2, quanlentha;

    byte[][] allbuf, allbuf1, allbuf2, allbuf3, allbuf4, allbuf5, allbuf6, allbuf7, allbuf8, allbuf9, allbuf10, allbuf11, allbufqty, allbufitems, allbufmodifiers, allbufsubtot,
            allbuftax, allbufdisc, allbufrounded, allbuffulltot, allbuf12, allbuf13, allbuf14, allbufbillno, allbuftime, allbufline1, allbufline, allbufcust, allbufcustname,
            allbufcustadd, allbufcustph, allbufcustemail, allbuftaxestype2, allbuftaxestype1, allbuf1122, allbufKOT;

    TextView textViewstatusnets_kot1, textViewstatusnets_kot2, textViewstatusnets_kot3, textViewstatusnets_kot4, tvkot;

    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;

    String ipnamegets_counter, portgets_counter, statusnets_counter;
    TextView textViewstatussusbs, textViewstatusnets, textViewstatusnets_counter;

    TextView sub_total, disc_value, disc_percent, totalprice, deliv_char;

    String insert1_cc = "", insert1_rs = "", str_country;

    int cart_leng;

    String str_merch_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_delivery_preparing_neworder_individual_order);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);

        textViewstatusnets_kot1 = new TextView(New_Individualorder_Itemview.this);
        textViewstatusnets_kot2 = new TextView(New_Individualorder_Itemview.this);
        textViewstatusnets_kot3 = new TextView(New_Individualorder_Itemview.this);
        textViewstatusnets_kot4 = new TextView(New_Individualorder_Itemview.this);


        textViewstatussusbs = new TextView(New_Individualorder_Itemview.this);
        textViewstatusnets = new TextView(New_Individualorder_Itemview.this);
        textViewstatusnets_counter = new TextView(New_Individualorder_Itemview.this);

        tvkot = new TextView(New_Individualorder_Itemview.this);

        LinearLayout back_activity = (LinearLayout) findViewById(R.id.back_activity);
        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Bundle extras = getIntent().getExtras();
        order_id = extras.getString("order_id");
        String total_price = extras.getString("total_price");
        order_type = extras.getString("order_type");

        TextView orderid = (TextView) findViewById(R.id.order_id);
        orderid.setText(order_id);

        totalprice = (TextView) findViewById(R.id.totalprice);
        totalprice.setText(total_price);

        TextView ordertype = (TextView) findViewById(R.id.order_type);
        ordertype.setText(order_type);


        Cursor cursor_user = db.rawQuery("SELECT * FROM LoginUser", null);
        if (cursor_user.moveToFirst()) {
            u_username = cursor_user.getString(1);
            username = cursor_user.getString(1);
            password = cursor_user.getString(2);
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

        Cursor cursor_country = db.rawQuery("SELECT * FROM Country_Selection", null);
        if (cursor_country.moveToFirst()){
            str_country = cursor_country.getString(1);
        }
        cursor_country.close();

        TextView inn = (TextView) findViewById(R.id.inn);
        if (str_country.toString().equals("Rupee") || str_country.toString().equals("India")) {
            insert1_cc = "\u20B9";
            insert1_rs = "Rs.";
            inn.setText(insert1_cc);
        }else {
            if (str_country.toString().equals("Pound") || str_country.toString().equals("UK Pound")) {
                insert1_cc = "\u00a3";
                insert1_rs = "BP.";
                inn.setText(insert1_cc);
            }else {
                if (str_country.toString().equals("Euro") || str_country.toString().equals("UK Euro")) {
                    insert1_cc = "\u20ac";
                    insert1_rs = "EU.";
                    inn.setText(insert1_cc);
                }else {
                    if (str_country.toString().equals("Dollar")) {
                        insert1_cc = "\u0024";
                        insert1_rs = "\u0024";
                        inn.setText(insert1_cc);
                    }else {
                        if (str_country.toString().equals("Dinars")) {
                            insert1_cc = "D";
                            insert1_rs = "KD.";
                            inn.setText(insert1_cc);
                        }else {
                            if (str_country.toString().equals("Shilling")) {
                                insert1_cc = "S";
                                insert1_rs = "S.";
                                inn.setText(insert1_cc);
                            }else {
                                if (str_country.toString().equals("Ringitt")) {
                                    insert1_cc = "R";
                                    insert1_rs = "RM.";
                                    inn.setText(insert1_cc);
                                }else {
                                    if (str_country.toString().equals("Rial")) {
                                        insert1_cc = "R";
                                        insert1_rs = "OR.";
                                        inn.setText(insert1_cc);
                                    }else {
                                        if (str_country.toString().equals("Yen")) {
                                            insert1_cc = "\u00a5";
                                            insert1_rs = "\u00a5";
                                            inn.setText(insert1_cc);
                                        }else {
                                            if (str_country.toString().equals("Papua New Guinean")) {
                                                insert1_cc = "K";
                                                insert1_rs = "K.";
                                                inn.setText(insert1_cc);
                                            }else {
                                                if (str_country.toString().equals("UAE")) {
                                                    insert1_cc = "D";
                                                    insert1_rs = "DH.";
                                                    inn.setText(insert1_cc);
                                                }else {
                                                    if (str_country.toString().equals("South African Rand")) {
                                                        insert1_cc = "R";
                                                        insert1_rs = "R.";
                                                        inn.setText(insert1_cc);
                                                    }else {
                                                        if (str_country.toString().equals("Congolese Franc")) {
                                                            insert1_cc = "F";
                                                            insert1_rs = "FC.";
                                                            inn.setText(insert1_cc);
                                                        }else {
                                                            if (str_country.toString().equals("Qatari Riyals")) {
                                                                insert1_cc = "QAR";
                                                                insert1_rs = "QAR.";
                                                                inn.setText(insert1_cc);
                                                            }else {
                                                                if (str_country.toString().equals("Dirhams")) {
                                                                    insert1_cc = "AED";
                                                                    insert1_rs = "AED.";
                                                                    inn.setText(insert1_cc);
                                                                }else {
                                                                    if (str_country.toString().equals("Kuwait Dinar")) {
                                                                        insert1_cc = "KWD";
                                                                        insert1_rs = "KWD.";
                                                                        inn.setText(insert1_cc);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Cursor cursor_merchant_id = db.rawQuery("SELECT * FROM Restaurant_id", null);
        if (cursor_merchant_id.moveToFirst()){
            str_merch_id = cursor_merchant_id.getString(1);
        }
        cursor_merchant_id.close();

        final ListView listView = (ListView) findViewById(R.id.items_listView);

        deliv_char = (TextView) findViewById(R.id.delivery_fee);

        sub_total = (TextView) findViewById(R.id.subtotal);
        disc_value = (TextView) findViewById(R.id.disc_value);

        disc_percent = new TextView(New_Individualorder_Itemview.this);

        final TextView res_charges = (TextView) findViewById(R.id.res_charges);

        list = new ArrayList<Country_Ingredient1_order>();

        try {
            //I try to use this for send Header is application/json
            jsonBody = new JSONObject();
            jsonBody.put("merchant_id", str_merch_id);
            jsonBody.put("order_id", order_id);
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


                                if (order_info_id.toString().equals(order_id)) {

                                    cust_name = c.getJSONObject("customer").getString("name");
                                    cust_phno = c.getJSONObject("customer").getString("mobile");
                                    cust_street = c.getJSONObject("customer").getString("street");
                                    cust_location = c.getJSONObject("customer").getString("location");
                                    String cust_deli_instru = c.getJSONObject("customer").getString("delivery_instruction");

                                    JSONArray cart = c.getJSONArray("cart");
                                    cart_leng = cart.length();

    //                                HashMap<String, String> contact = new HashMap<>();
    //                                my_array.clear();

    //                                contact.put("orderid", order_info_id);

                                    ArrayList l2 = new ArrayList();


//                                    date_time1.setText(date_time);
                                    for (int j = 0; j < cart.length(); j++) {
                                        JSONObject name1 = cart.getJSONObject(j);
                                        String name = name1.getString("pos_item_name");
                                        String qty = name1.getString("qty");
                                        String price0 = name1.getString("price");
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

                                        String price1 = String.valueOf(Float.parseFloat(price0)+price);

                                        String instructions = name1.getString("order_notes");

                                        Country_Ingredient1_order NAME = new Country_Ingredient1_order(name, qty, price1, l2, "", instructions);
                                        list.add(NAME);

                                    }

                                    String subtotal = c.getJSONObject("subtotal").getString("amount_pretty");
                                    sub_total.setText(subtotal);

                                }



                                float p1 = 0;
                                float g1 = 0;

                                JSONArray extra = c.getJSONArray("extra");
                                if (order_info_id.toString().equals(order_id)) {
//                                    date_time1.setText(date_time);
                                    for (int j = 0; j < extra.length(); j++) {
                                        JSONObject name1 = extra.getJSONObject(j);
                                        String type = name1.getString("type");
                                        String amount_pretty = name1.getString("amount_pretty");


                                        if (type.toString().equals("Delivery Charge")){
                                            deliv_char.setText(amount_pretty);
                                        }

                                        if (type.toString().equals("Packaging Charge")){
                                            p1 = Float.parseFloat(amount_pretty);
                                            p_c = amount_pretty;
                                        }

                                        if (type.toString().equals("GST")){
                                            g1 = Float.parseFloat(amount_pretty);
                                            g_c = amount_pretty;
                                        }


                                        res_charges.setText(String.valueOf(p1+g1));



                                    }
                                }




                                JSONArray discount = c.getJSONArray("discount");

                                if (order_info_id.toString().equals(order_id)) {
//                                    date_time1.setText(date_time);
                                    for (int j = 0; j < discount.length(); j++) {
                                        JSONObject name1 = discount.getJSONObject(j);
                                        String name = name1.getString("amount_pretty");
                                        String percent = name1.getString("percent");

                                        disc_value.setText(name);
                                        disc_percent.setText(percent);
                                    }
                                }



                            }

                            MyAdapter_Ingredient adapter = new MyAdapter_Ingredient(New_Individualorder_Itemview.this,list);
                            listView.setAdapter(adapter);

//                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                    final TextView checkbox1 = (TextView) view.getTag(R.id.order_id);
//                                    Toast.makeText(New_Individualorder_Itemview.this, "2 "+checkbox1.getText().toString(), Toast.LENGTH_LONG).show();
//
//
//                                    Intent intent = new Intent(New_Individualorder_Itemview.this, Items_View.class);
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



        res_charges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(New_Individualorder_Itemview.this, R.style.notitle);
                dialog.setContentView(R.layout.restau_charges);
                dialog.show();

                Button close = (Button) dialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                ImageView closetext = (ImageView) dialog.findViewById(R.id.closetext);
                closetext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                TextView tv = (TextView) dialog.findViewById(R.id.pack_ch);
                tv.setText("Packaging Charges: "+p_c);

                TextView tv1 = (TextView) dialog.findViewById(R.id.gst_ch);
                tv1.setText("GST: "+g_c);

            }
        });


        ImageButton customer_details = (ImageButton) findViewById(R.id.customer_details);
        customer_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog_cust_details = new Dialog(New_Individualorder_Itemview.this);
                dialog_cust_details.setContentView(R.layout.dialog_customer_view);
                dialog_cust_details.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog_cust_details.show();

                ImageButton btncancel = (ImageButton) dialog_cust_details.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_cust_details.dismiss();
                    }
                });

                TextView nameedit = (TextView) dialog_cust_details.findViewById(R.id.nameedit);
                TextView phonenoedit = (TextView) dialog_cust_details.findViewById(R.id.phonenoedit);
                TextView addressedit = (TextView) dialog_cust_details.findViewById(R.id.addressedit);
                TextView locationedit = (TextView) dialog_cust_details.findViewById(R.id.locationedit);

                nameedit.setText(cust_name);
                phonenoedit.setText(cust_phno);
                addressedit.setText(cust_street);
                locationedit.setText(cust_location);
            }
        });

        ImageButton print_kot_bill = (ImageButton) findViewById(R.id.print_kot_bill);
        print_kot_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(New_Individualorder_Itemview.this);
//                alertDialog.setMessage("hi");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(New_Individualorder_Itemview.this, android.R.layout.simple_selectable_list_item);
                arrayAdapter.add("KOT");
                arrayAdapter.add("Print bill");
                alertDialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String strName = arrayAdapter.getItem(which);
                        if (strName.equals("KOT")){
//                            Toast.makeText(getActivity(), "download", Toast.LENGTH_SHORT).show();

                            Cursor conn = db.rawQuery("SELECT * FROM BTConn", null);
                            if (conn.moveToFirst()) {
                                nameget = conn.getString(1);
                                addget = conn.getString(2);
                                statussusb = conn.getString(3);
                            }
                            conn.close();

                            Cursor connnet = db.rawQuery("SELECT * FROM IPConn", null);
                            if (connnet.moveToFirst()) {
                                ipnameget = connnet.getString(1);
                                portget = connnet.getString(2);
                                statusnet = connnet.getString(3);
                            }
                            connnet.close();

                            Cursor c_kot1 = db.rawQuery("SELECT * FROM IPConn_KOT1", null);
                            if (c_kot1.moveToFirst()) {
                                ipnamegets_kot1 = c_kot1.getString(1);
                                portgets_kot1 = c_kot1.getString(2);
                                statusnets_kot1 = c_kot1.getString(3);
                                name_kot1 = c_kot1.getString(5);
                            }
                            c_kot1.close();

                            Cursor c_kot2 = db.rawQuery("SELECT * FROM IPConn_KOT2", null);
                            if (c_kot2.moveToFirst()) {
                                ipnamegets_kot2 = c_kot2.getString(1);
                                portgets_kot2 = c_kot2.getString(2);
                                statusnets_kot2 = c_kot2.getString(3);
                                name_kot2 = c_kot2.getString(5);
                            }
                            c_kot2.close();

                            Cursor c_kot3 = db.rawQuery("SELECT * FROM IPConn_KOT3", null);
                            if (c_kot3.moveToFirst()) {
                                ipnamegets_kot3 = c_kot3.getString(1);
                                portgets_kot3 = c_kot3.getString(2);
                                statusnets_kot3 = c_kot3.getString(3);
                                name_kot3 = c_kot3.getString(5);
                            }
                            c_kot3.close();

                            Cursor c_kot4 = db.rawQuery("SELECT * FROM IPConn_KOT4", null);
                            if (c_kot4.moveToFirst()) {
                                ipnamegets_kot4 = c_kot4.getString(1);
                                portgets_kot4 = c_kot4.getString(2);
                                statusnets_kot4 = c_kot4.getString(3);
                                name_kot4 = c_kot4.getString(5);
                            }
                            c_kot4.close();

                            if (statussusb.toString().equals("ok") || statusnet.toString().equals("ok") || statusnets_kot1.toString().equals("ok") ||
                                    statusnets_kot2.toString().equals("ok") || statusnets_kot3.toString().equals("ok") || statusnets_kot4.toString().equals("ok")) {

//                                if (statusnet.toString().equals("ok")) {
//                                    wifiSocket.WIFISocket(ipnameget, 9100);
//                                }
//                                if (statusnets_kot1.toString().equals("ok")) {
//                                    wifiSocket_kot1.WIFISocket(ipnamegets_kot1, 9100);
//                                }
//                                if (statusnets_kot2.toString().equals("ok")) {
//                                    wifiSocket_kot2.WIFISocket(ipnamegets_kot2, 9100);
//                                }
//                                if (statusnets_kot3.toString().equals("ok")) {
//                                    wifiSocket_kot3.WIFISocket(ipnamegets_kot3, 9100);
//                                }
//                                if (statusnets_kot4.toString().equals("ok")) {
//                                    wifiSocket_kot4.WIFISocket(ipnamegets_kot4, 9100);
//                                }

                                DownloadMusicfromInternet2 downloadMusicfromInternet2 = new DownloadMusicfromInternet2();
                                downloadMusicfromInternet2.execute();

                            } else {
                                final Dialog dialogconn = new Dialog(New_Individualorder_Itemview.this, R.style.notitle);
                                dialogconn.setContentView(R.layout.dialog_printer_conn_error_kot);

                                Button conti = dialogconn.findViewById(R.id.ok);
                                conti.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogconn.dismiss();
                                    }
                                });

                                dialogconn.show();
                            }

                        }else {
                            if (strName.equals("Print bill")){
//                                Toast.makeText(getActivity(), "download and send", Toast.LENGTH_SHORT).show();
                                printbill1();

                            }
                        }

                    }
                });

                alertDialog.show();
            }
        });

        final LinearLayout panel1 = (LinearLayout) findViewById(R.id.panel1);
        FrameLayout text1 = (FrameLayout) findViewById(R.id.text1);
        final ImageView rotatearrow = (ImageView) findViewById(R.id.arrow);

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (panel1.getVisibility() == View.VISIBLE) {
                    rotatearrow.setRotation(360);
                    panel1.setVisibility(View.GONE);
                } else {
                    rotatearrow.setRotation(180);
                    panel1.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private class MyAdapter_Ingredient extends ArrayAdapter<Country_Ingredient1_order> {

        private ArrayList<Country_Ingredient1_order> list;
        private ArrayList<Country_Ingredient1_order> list1;
        //    private ArrayList<Country_Ingredient1_order> originalList;
        private final Activity context;
        boolean checkAll_flag = false;
        boolean checkItem_flag = false;
        private CountryFilter filter;
//    private List<Country_Ingredient1_order> countryList;

        public MyAdapter_Ingredient(Activity context, ArrayList<Country_Ingredient1_order> list) {
            super(context, R.layout.items_view_list, list);
            this.context = context;
//        this.list = list;
//        this.list = new ArrayList<Country_Ingredient1_order>();
//        this.list.addAll(list);
            this.list = new ArrayList<Country_Ingredient1_order>();
            this.list.addAll(list);
            this.list1 = new ArrayList<Country_Ingredient1_order>();
            this.list1.addAll(list);
        }

        class ViewHolder {
            protected TextView name;
            protected TextView qty;
            protected TextView price;

            protected TextView sub_item_name;
            protected TextView sub_item_price;

            protected TextView instructions;
        }

        @Override
        public Filter getFilter() {
            if (filter == null){
                filter  = new MyAdapter_Ingredient.CountryFilter();
            }
            return filter;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final String url = "https://api.werafoods.com/pos/v1/order/accept";
            String url_pending = "https://api.werafoods.com/pos/v1/merchant/pendingorders";

            final JSONObject[] jsonBody = new JSONObject[1];

            ViewHolder viewHolder = null;
            if (convertView == null) {
                LayoutInflater inflator = context.getLayoutInflater();
                convertView = inflator.inflate(R.layout.items_view_list, null);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                viewHolder.qty = (TextView) convertView.findViewById(R.id.qty);
                viewHolder.price = (TextView) convertView.findViewById(R.id.price);

                viewHolder.sub_item_name = (TextView) convertView.findViewById(R.id.sub_item_name);
                viewHolder.sub_item_price = (TextView) convertView.findViewById(R.id.sub_item_price);
                viewHolder.instructions = (TextView) convertView.findViewById(R.id.instructions);

                final ViewHolder finalViewHolder = viewHolder;
                final ViewHolder finalViewHolder1 = viewHolder;


                convertView.setTag(viewHolder);
                convertView.setTag(R.id.name, viewHolder.name);
                convertView.setTag(R.id.qty, viewHolder.qty);
                convertView.setTag(R.id.price, viewHolder.price);

                convertView.setTag(R.id.sub_item_name, viewHolder.sub_item_name);
                convertView.setTag(R.id.sub_item_price, viewHolder.sub_item_price);
                convertView.setTag(R.id.instructions, viewHolder.instructions);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.name.setText(list.get(position).getName());
            viewHolder.qty.setText(list.get(position).getQty());
            viewHolder.price.setText(list.get(position).getPrice());

            viewHolder.sub_item_name.setText(list.get(position).getSub_item_name());
            viewHolder.sub_item_price.setText(list.get(position).getSub_item_price());
            viewHolder.instructions.setText(list.get(position).getInstructions());

            return convertView;
        }

        private class CountryFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
//            Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();
                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();
                if(constraint != null && constraint.toString().length() > 0) {
                    ArrayList<Country_Ingredient1_order> filteredItems = new ArrayList<Country_Ingredient1_order>();
                    for(int i = 0, l = list1.size(); i < l; i++) {
                        Country_Ingredient1_order country = list1.get(i);
                        if(country.toString().toLowerCase().contains(constraint))
                            filteredItems.add(country);
                    }
                    result.count = filteredItems.size();
                    result.values = filteredItems;
                }
                else {
                    synchronized(this) {
                        result.values = list1;
                        result.count = list1.size();
                    }
                }
                return result;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                list = (ArrayList<Country_Ingredient1_order>)results.values;
                notifyDataSetChanged();
                clear();
                for(int i = 0, l = list.size(); i < l; i++)
                    add(list.get(i));
                notifyDataSetInvalidated();
            }
        }

    }

    class DownloadMusicfromInternet2 extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            try {

                printbillsplithome_kot1();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected void onPostExecute(Integer file_url) {


        }
    }

    public void printbillsplithome_kot1() {

        byte[] setHT34M = {0x1b, 0x44, 0x04, 0x11, 0x19, 0x00};
        byte[] dotfeed = {0x1b, 0x4a, 0x10};
        byte[] HTRight = {0x1b, 0x61, 0x02, 0x09};
        final byte[] HT = {0x09};
        byte[] HT1 = {0x09};
        final byte[] LF = {0x0d, 0x0a};

        final byte[] left = {0x1b, 0x61, 0x00};
        byte[] cen = {0x1b, 0x61, 0x01};
        byte[] right = {0x1b, 0x61, 0x02};
        byte[] bold = {0x1B,0x21,0x10};
        final byte[] normal = {0x1d, 0x21, 0x00};
        byte[] horiz1 = {0x1b, 0x44, 0x19, 0x19, 0x00};
        byte[] horiz = {0x1b, 0x44, 0x04, 0x06, 0x04, 0x04, 0x0A, 0x00, 0x09, 0x30, 0x09, 0x31, 0x09, 0x32, 0x09, 0x33, 0x09, 0x34, 0x0d, 0x0a};

        byte[] un = {0x1b, 0x2d, 0x02};
        final byte[] un1 = {0x1b, 0x2d, 0x00};
        String str_line = "";

        Cursor print_ty = db.rawQuery("SELECT * FROM Printer_type", null);
        if (print_ty.moveToFirst()) {
            str_print_ty = print_ty.getString(1);
        }
        print_ty.close();

        Cursor getprint_type = db.rawQuery("SELECT * FROM Printer_text_size", null);
        if (getprint_type.moveToFirst()) {
            String type = getprint_type.getString(1);

            Cursor cc = db.rawQuery("SELECT * FROM Printerreceiptsize", null);

            if (cc.moveToFirst()) {
                cc.moveToFirst();
                do {
                    NAME = cc.getString(1);
                    if (NAME.equals("3 inch")) {
                        if (str_print_ty.toString().equals("Generic") || str_print_ty.toString().equals("Epson/others")) {
                            setHT32 = new byte[]{0x1b, 0x44, 0x29, 0x00};//2 tabs 3"
                            setHT321 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x25, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                            setHT34 = new byte[]{0x1b, 0x44, 0x08, 0x20, 0x29, 0x00};//4 tabs 3"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 3"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                            nPaperWidth = 576;
                            charlength = 41;
                            HT1 = new byte[]{0x09};
                            str_line = "------------------------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "------------------------------------------------".getBytes(), LF

                            };
                        } else {
                            if (str_print_ty.toString().equals("POS")) {
                                setHT32 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT321 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x10, 0x15, 0x00};//4 tabs 3"
                                setHTKOT = new byte[]{0x1b, 0x44, 0x05, 0x00};//2 tabs 3"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                nPaperWidth = 576;
                                charlength = 23;
                                charlength1 = 46;
                                charlength2 = 69;
                                quanlentha = 4;
                                HT1 = new byte[]{0x2F};
                                str_line = "------------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------------".getBytes(), LF

                                };
                            }
                        }
                    } else {
                        if (str_print_ty.toString().equals("Generic")) {
//                            Toast.makeText(New_Individualorder_Itemview.this, "phi", Toast.LENGTH_SHORT).show();
                            setHT32 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 2"
                            setHT321 = new byte[]{0x1b, 0x44, 0x13, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x15, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                            setHT34 = new byte[]{0x1b, 0x44, 0x04, 0x12, 0x19, 0x00};//4 tabs 2"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 2"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                            nPaperWidth = 384;
                            charlength = 25;
                            HT1 = new byte[]{0x09};
                            str_line = "--------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "--------------------------------".getBytes(), LF
                            };
                        } else {
                            if (str_print_ty.toString().equals("Epson/others")) {
//                            Toast.makeText(New_Individualorder_Itemview.this, "epson", Toast.LENGTH_SHORT).show();
                                setHT32 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 2"
                                setHT321 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x21, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                                setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x19, 0x21, 0x00};//4 tabs 2"
                                setHTKOT = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                                nPaperWidth = 384;
                                charlength = 28;
                                HT1 = new byte[]{0x09};
                                str_line = "------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------".getBytes(), LF
                                };
                            } else {
                                if (str_print_ty.toString().equals("POS")) {
                                    setHT32 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT321 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT3212 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 3"
                                    setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x12, 0x21, 0x00};//4 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x05, 0x08, 0x00};//4 tabs 2"
                                    setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x08, 0x09, 0x00};//4 tabs 2"
                                    setHTKOT = new byte[]{0x1b, 0x44, 0x05, 0x00};//2 tabs 3"
                                    feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                    nPaperWidth = 384;
                                    charlength = 11;
                                    charlength1 = 22;
                                    charlength2 = 33;
                                    quanlentha = 3;
                                    HT1 = new byte[]{0x2F};
                                    str_line = "--------------------------------";
                                    allbufline = new byte[][]{
                                            left, un1, "--------------------------------".getBytes(), LF
                                    };
                                }
                            }
                        }
                    }
                } while (cc.moveToNext());
            }
            cc.close();

        }
        getprint_type.close();

        String dd = "";
        TextView qazcvb = new TextView(New_Individualorder_Itemview.this);
        Cursor cvonnusb = db.rawQuery("SELECT * FROM BTConn", null);
        if (cvonnusb.moveToFirst()) {
            addgets = cvonnusb.getString(1);
            namegets = cvonnusb.getString(2);
            statussusbs = cvonnusb.getString(3);
            dd = cvonnusb.getString(4);
        }
        cvonnusb.close();
        qazcvb.setText(dd);
        if (qazcvb.getText().toString().equals("usb") && statussusbs.toString().equals("ok")) {
//            runPrintCouponSequence();
        } else {

            Cursor connnet = db.rawQuery("SELECT * FROM IPConn", null);
            if (connnet.moveToFirst()) {
                ipnamegets = connnet.getString(1);
                portgets = connnet.getString(2);
                statusnets = connnet.getString(3);
            }
            connnet.close();

            Cursor connnet_counter = db.rawQuery("SELECT * FROM IPConn_Counter", null);
            if (connnet_counter.moveToFirst()) {
                ipnamegets_counter = connnet_counter.getString(1);
                portgets_counter = connnet_counter.getString(2);
                statusnets_counter = connnet_counter.getString(3);
            }
            connnet_counter.close();

            Cursor connusb = db.rawQuery("SELECT * FROM BTConn", null);
            if (connusb.moveToFirst()) {
                addgets = connusb.getString(1);
                namegets = connusb.getString(2);
                statussusbs = connusb.getString(3);
            }
            connusb.close();

            textViewstatusnets.setText(statusnet);
            textViewstatusnets_counter.setText(statusnets_counter);
            textViewstatussusbs.setText(statussusbs);

            Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
            if (getcom.moveToFirst()) {
                strcompanyname = getcom.getString(1);
            }
            getcom.close();

            tvkot.setText(strcompanyname);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf1 = new byte[][]{
                        bold, cen, strcompanyname.getBytes(), LF, LF

                };

                if (textViewstatussusbs.getText().toString().equals("ok")) {
                    BluetoothPrintDriver.BT_Write(bold);    //
                    BluetoothPrintDriver.BT_Write(cen);    //
                    BT_Write(strcompanyname);
                    BluetoothPrintDriver.BT_Write(LF);    //
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (textViewstatusnets.getText().toString().equals("ok")) {
                        wifiSocket.WIFI_Write(bold);    //
                        wifiSocket.WIFI_Write(cen);    //
                        wifiSocket.WIFI_Write(strcompanyname);
                        wifiSocket.WIFI_Write(LF);    //
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }

            allbufKOT = new byte[][]{
                    un, cen, "Order Ticket".getBytes(), LF
            };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(normal);    //
                BluetoothPrintDriver.BT_Write(un);    //
                BluetoothPrintDriver.BT_Write(cen);    //
                BT_Write("Order Ticket");
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(normal);    //
                    wifiSocket.WIFI_Write(un);    //
                    wifiSocket.WIFI_Write(cen);    //
                    wifiSocket.WIFI_Write("Order Ticket");
                    wifiSocket.WIFI_Write(LF);    //
                }
            }

//            Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
//            if (vbnm.moveToFirst()) {
//                assa1 = vbnm.getString(1);
//                assa2 = vbnm.getString(2);
//            }
//            vbnm.close();
//            TextView cx = new TextView(New_Individualorder_Itemview.this);
//            cx.setText(assa1);
//            String tablen = "";
//            if (cx.getText().toString().equals("")) {
//                tablen = "Tab" + assa2;
//            } else {
//                tablen = "Tab" + assa1;
//            }
//
//            Spinner billing_spinner = (Spinner) findViewById(R.id.billing_spinner);
//            NAme111 = billing_spinner.getSelectedItem().toString();
//
//            allbuf11 = new byte[][]{
//                    left, un1, setHT321, tablen.getBytes(), LF
//            };

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BluetoothPrintDriver.BT_Write(setHT321);    //
                BT_Write(order_type+" - "+order_id);
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write("   ");
//                BT_Write(tablen);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(left);    //
                    wifiSocket.WIFI_Write(un1);    //
                    wifiSocket.WIFI_Write(setHT321);    //
                    wifiSocket.WIFI_Write(order_type+" - "+order_id);
                    wifiSocket.WIFI_Write(HT);    //
                    wifiSocket.WIFI_Write("   ");
//                    wifiSocket.WIFI_Write(tablen);
                    wifiSocket.WIFI_Write(LF);    //
                }
            }

            SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyyyy");
            final String normal1 = normal2.format(new Date());

            Date dt = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
            final String time1 = sdf1.format(dt);

            Date dtt = new Date();
            SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
            String time24 = sdf1t.format(dtt);

            allbuf10 = new byte[][]{
                    setHT321, left, normal1.getBytes(), HT, "   ".getBytes(), time1.getBytes(), LF
            };

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHT321);    //
                BluetoothPrintDriver.BT_Write(left);    //
                BT_Write(normal1);
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write("   ");
                BT_Write(time1);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(setHT321);    //
                    wifiSocket.WIFI_Write(left);    //
                    wifiSocket.WIFI_Write(normal1);
                    wifiSocket.WIFI_Write(HT);    //
                    wifiSocket.WIFI_Write("   ");
                    wifiSocket.WIFI_Write(time1);
                    wifiSocket.WIFI_Write(LF);    //
                }
            }


            allbuf11 = new byte[][]{
                    left, setHT321, "Counter person ".getBytes(), LF
            };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(setHT321);    //
                BT_Write("Counter person: " + u_name);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(left);    //
                    wifiSocket.WIFI_Write(setHT321);    //
                    wifiSocket.WIFI_Write("Counter person: " + u_name);
                    wifiSocket.WIFI_Write(LF);    //
                }
            }


            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BT_Write(str_line);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(left);    //
                    wifiSocket.WIFI_Write(un1);    //
                    wifiSocket.WIFI_Write(str_line);
                    wifiSocket.WIFI_Write(LF);    //
                }
            }

            allbufqty = new byte[][]{
                    setHTKOT, normal, "Qty".getBytes(), HT, "Item".getBytes(), left, LF
            };

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHTKOT);    //
                BluetoothPrintDriver.BT_Write(normal);    //
                BT_Write("Qty");
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write("Item");
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(setHTKOT);    //
                    wifiSocket.WIFI_Write(normal);    //
                    wifiSocket.WIFI_Write("Qty");
                    wifiSocket.WIFI_Write(HT);    //
                    wifiSocket.WIFI_Write("Item");
                    wifiSocket.WIFI_Write(left);    //
                    wifiSocket.WIFI_Write(LF);    //
                }
            }

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BT_Write(str_line);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(left);    //
                    wifiSocket.WIFI_Write(un1);    //
                    wifiSocket.WIFI_Write(str_line);
                    wifiSocket.WIFI_Write(LF);    //
                }
            }



            try {
                //I try to use this for send Header is application/json
                jsonBody = new JSONObject();
                jsonBody.put("merchant_id", str_merch_id);
                jsonBody.put("order_id", order_id);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

            final String finalStr_line = str_line;
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

                                    if (order_info_id.toString().equals(order_id)) {

                                        cust_name = c.getJSONObject("customer").getString("name");
                                        cust_phno = c.getJSONObject("customer").getString("mobile");
                                        cust_street = c.getJSONObject("customer").getString("street");
                                        cust_location = c.getJSONObject("customer").getString("location");
                                        String cust_deli_instru = c.getJSONObject("customer").getString("delivery_instruction");

                                        JSONArray cart = c.getJSONArray("cart");

    //                                HashMap<String, String> contact = new HashMap<>();
    //                                my_array.clear();

    //                                contact.put("orderid", order_info_id);

                                        ArrayList l2 = new ArrayList();


//                                    date_time1.setText(date_time);
                                        for (int j = 0; j < cart.length(); j++) {
                                            JSONObject name1 = cart.getJSONObject(j);
                                            String name = name1.getString("pos_item_name");
                                            String qty = name1.getString("qty");
                                            String price0 = name1.getString("price");
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

                                            String instructions = name1.getString("order_notes");

                                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                                BluetoothPrintDriver.BT_Write(normal);    //
                                                BT_Write(qty);
                                                BluetoothPrintDriver.BT_Write(HT);    //
                                                BT_Write(name);
                                                BluetoothPrintDriver.BT_Write(LF);    //
                                            }else {
                                                if (textViewstatusnets.getText().toString().equals("ok")) {
                                                    wifiSocket.WIFI_Write(setHTKOT);    //
                                                    wifiSocket.WIFI_Write(normal);    //
                                                    wifiSocket.WIFI_Write(qty);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(name);
                                                    wifiSocket.WIFI_Write(LF);    //
                                                }
                                            }

                                            TextView t = new TextView(New_Individualorder_Itemview.this);
                                            t.setText(instructions);

                                            if (t.getText().toString().equals("")){

                                            }else {
                                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                    BluetoothPrintDriver.BT_Write(left);    //
                                                    BluetoothPrintDriver.BT_Write(un1);    //
                                                    BT_Write(instructions);
                                                    BluetoothPrintDriver.BT_Write(LF);    //
                                                } else {
                                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                                        wifiSocket.WIFI_Write(left);    //
                                                        wifiSocket.WIFI_Write(un1);    //
                                                        wifiSocket.WIFI_Write(instructions);
                                                        wifiSocket.WIFI_Write(LF);    //
                                                    }
                                                }
                                            }

                                        }

                                    }
                                }


                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                    BluetoothPrintDriver.BT_Write(left);    //
                                    BluetoothPrintDriver.BT_Write(un1);    //
                                    BT_Write(finalStr_line);
                                    BluetoothPrintDriver.BT_Write(LF);    //
                                }else {
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(left);    //
                                        wifiSocket.WIFI_Write(un1);    //
                                        wifiSocket.WIFI_Write(finalStr_line);
                                        wifiSocket.WIFI_Write(LF);    //
                                    }
                                }



                                byte[][] allbuf = new byte[][]{
                                        feedcut2
                                };
                                System.out.println("feedcut executed now 3");
                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                    BluetoothPrintDriver.BT_Write(feedcut2);    //
                                }else {
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(feedcut2);    //
                                    }
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
            mQueue.add(jsonObjectRequest);





        }

    }


    public void printbill1(){

        Cursor connnet = db.rawQuery("SELECT * FROM IPConn", null);
        if (connnet.moveToFirst()) {
            ipnamegets = connnet.getString(1);
            portgets = connnet.getString(2);
            statusnets = connnet.getString(3);
        }
        connnet.close();

        Cursor connnet_counter = db.rawQuery("SELECT * FROM IPConn_Counter", null);
        if (connnet_counter.moveToFirst()) {
            ipnamegets_counter = connnet_counter.getString(1);
            portgets_counter = connnet_counter.getString(2);
            statusnets_counter = connnet_counter.getString(3);
        }
        connnet_counter.close();

        Cursor connusb = db.rawQuery("SELECT * FROM BTConn", null);
        if (connusb.moveToFirst()) {
            addgets = connusb.getString(1);
            namegets = connusb.getString(2);
            statussusbs = connusb.getString(3);
        }
        connusb.close();
//        Toast.makeText(getApplicationContext(), "saving111", Toast.LENGTH_LONG).show();

        Cursor connnet1 = db.rawQuery("SELECT * FROM IPConn", null);
        if (connnet1.moveToFirst()) {
            ipnameget = connnet1.getString(1);
            portget = connnet1.getString(2);
            statusnet = connnet1.getString(3);
        }
        connnet1.close();

        textViewstatusnets.setText(statusnet);
        textViewstatusnets_counter.setText(statusnets_counter);
        textViewstatussusbs.setText(statussusbs);

        byte[] setHT34M = {0x1b, 0x44, 0x04, 0x11, 0x19, 0x00};
        byte[] dotfeed = {0x1b, 0x4a, 0x10};
        byte[] HTRight = {0x1b, 0x61, 0x02, 0x09};
        final byte[] HT = {0x09};
        byte[] HT1 = {0x09};
        final byte[] LF = {0x0d, 0x0a};

        final byte[] left = {0x1b, 0x61, 0x00};
        final byte[] cen = {0x1b, 0x61, 0x01};
        byte[] right = {0x1b, 0x61, 0x02};
        final byte[] bold = {0x1B,0x21,0x10};
        final byte[] normal = {0x1d, 0x21, 0x00};
        byte[] horiz1 = {0x1b, 0x44, 0x19, 0x19, 0x00};
        byte[] horiz = {0x1b, 0x44, 0x04, 0x06, 0x04, 0x04, 0x0A, 0x00, 0x09, 0x30, 0x09, 0x31, 0x09, 0x32, 0x09, 0x33, 0x09, 0x34, 0x0d, 0x0a};

        byte[] un = {0x1b, 0x2d, 0x02};
        final byte[] un1 = {0x1b, 0x2d, 0x00};
        String str_line = "";

        Cursor print_ty = db.rawQuery("SELECT * FROM Printer_type", null);
        if (print_ty.moveToFirst()) {
            str_print_ty = print_ty.getString(1);
        }

        Cursor getprint_type = db.rawQuery("SELECT * FROM Printer_text_size", null);
        if (getprint_type.moveToFirst()) {
            String type = getprint_type.getString(1);

            Cursor cc = db.rawQuery("SELECT * FROM Printerreceiptsize", null);

            if (cc.moveToFirst()) {
                cc.moveToFirst();
                do {
                    NAME = cc.getString(1);
                    if (NAME.equals("3 inch")) {
                        if (str_print_ty.equals("Generic") || str_print_ty.equals("Epson/others")) {
                            setHT32 = new byte[]{0x1b, 0x44, 0x29, 0x00};//2 tabs 3"
                            setHT321 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x25, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                            setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x20, 0x29, 0x00};//4 tabs 3"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                            nPaperWidth = 576;
                            charlength = 23;
                            charlength1 = 46;
                            charlength2 = 69;
                            quanlentha = 5;
                            HT1 = new byte[]{0x09};
                            str_line = "------------------------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "------------------------------------------------".getBytes(), LF
                            };
                        } else {
                            if (str_print_ty.equals("POS")) {
                                setHT32 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT321 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x10, 0x15, 0x00};//4 tabs 3"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                                nPaperWidth = 576;
                                charlength = 23;
                                charlength1 = 46;
                                charlength2 = 69;
                                quanlentha = 4;
                                HT1 = new byte[]{0x2F};
                                str_line = "------------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------------".getBytes(), LF

                                };
                            }
                        }
                    } else {
                        if (str_print_ty.equals("Generic")) {
//                            Toast.makeText(New_Individualorder_Itemview.this, "phi", Toast.LENGTH_SHORT).show();
                            setHT32 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 2"
                            setHT321 = new byte[]{0x1b, 0x44, 0x13, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x15, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                            setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x12, 0x19, 0x00};//4 tabs 2"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                            nPaperWidth = 384;
                            charlength = 10;
                            charlength1 = 20;
                            charlength2 = 30;
                            quanlentha = 5;
                            HT1 = new byte[]{0x09};
                            str_line = "--------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "--------------------------------".getBytes(), LF

                            };
                        } else {
                            if (str_print_ty.equals("Epson/others")) {
//                            Toast.makeText(New_Individualorder_Itemview.this, "epson", Toast.LENGTH_SHORT).show();
                                setHT32 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 2"
                                setHT321 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x21, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                                setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x19, 0x21, 0x00};//4 tabs 2"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                                nPaperWidth = 384;
                                charlength = 16;
                                charlength1 = 32;
                                charlength2 = 48;
                                quanlentha = 5;
                                HT1 = new byte[]{0x09};
                                str_line = "------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------".getBytes(), LF
                                };
                            } else {
                                if (str_print_ty.equals("POS")) {
                                    setHT32 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT321 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT3212 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 3"
                                    setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x12, 0x21, 0x00};//4 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x05, 0x08, 0x00};//4 tabs 2"
                                    setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x08, 0x09, 0x00};//4 tabs 2"
                                    feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                                    nPaperWidth = 384;
                                    charlength = 11;
                                    charlength1 = 22;
                                    charlength2 = 33;
                                    quanlentha = 3;
                                    HT1 = new byte[]{0x2F};
                                    str_line = "--------------------------------";
                                    allbufline = new byte[][]{
                                            left, un1, "--------------------------------".getBytes(), LF
                                    };
                                }
                            }
                        }
                        print_ty.close();
                    }
                } while (cc.moveToNext());
            }
            cc.close();

        }
        getprint_type.close();

//        Toast.makeText(New_Individualorder_Itemview.this, "printbill", Toast.LENGTH_SHORT).show();

//        imageViewPicture = (ImageView) dialog.findViewById(R.id.imageViewPicture);
//        mView = dialog.findViewById(R.id.f_viewnew);
//
//        ImageView imageButton = (ImageView) mView.findViewById(R.id.viewImagee);
//
//        if (NAME.equals("3 inch")) {
////            Toast.makeText(New_Individualorder_Itemview.this, "3 inch", Toast.LENGTH_SHORT).show();
//            imageViewPicture.getLayoutParams().height = 94;
//            imageViewPicture.getLayoutParams().width = 576;
//            imageButton.getLayoutParams().height = 94;
//            imageButton.getLayoutParams().width = 576;
//        } else {
////            Toast.makeText(New_Individualorder_Itemview.this, "2 inch", Toast.LENGTH_SHORT).show();
//            imageViewPicture.getLayoutParams().height = 94;
//            imageViewPicture.getLayoutParams().width = 384;
//            imageButton.getLayoutParams().height = 94;
//            imageButton.getLayoutParams().width = 384;
//        }
//
//        String[] col = {"companylogo"};
//        Cursor c = db.query("Logo", col, null, null, null, null, null);
//        if (c.moveToFirst()) {
//            do {
//                byte[] img = c.getBlob(c.getColumnIndex("companylogo"));
//                final Bitmap b1 = BitmapFactory.decodeByteArray(img, 0, img.length);
//
//                imageButton.setImageBitmap(b1);
//
//                mView.setDrawingCacheEnabled(true);
//                mView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//                mView.layout(0, 0, mView.getMeasuredWidth(), mView.getMeasuredHeight());
//                mView.buildDrawingCache(true);
//
//                Bitmap b = Bitmap.createBitmap(mView.getDrawingCache());
//                //mView.setDrawingCacheEnabled(false);
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//
//                imageViewPicture.setImageBitmap(b);
//
//                Bitmap mBitmap = ((BitmapDrawable) imageViewPicture.getDrawable())
//                        .getBitmap();
//
//                if (textViewstatussusbs.getText().toString().equals("ok")) {
//                    if (mBitmap != null) {
//                        byte[] command = Utils.decodeBitmap(mBitmap);
//                        printByteData(command);
//                    } else {
//                        Log.e("Print Photo error", "the file isn't exists");
//                    }
//                    Bundle data = new Bundle();
//                    data.putParcelable(Global.PARCE1, mBitmap);
//                    data.putInt(Global.INTPARA1, nPaperWidth);
//                    data.putInt(Global.INTPARA2, 0);
////                    DrawerService.workThread.handleCmd(
////                            Global.CMD_POS_PRINTBWPICTURE, data);
//                } else {
//                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
//                        if (mBitmap != null) {
//                            byte[] command = Utils.decodeBitmap(mBitmap);
//                            printByteData_wifi_counter(command);
//                        } else {
//                            Log.e("Print Photo error", "the file isn't exists");
//                        }
//                        Bundle data = new Bundle();
//                        data.putParcelable(Global1.PARCE1, mBitmap);
//                        data.putInt(Global1.INTPARA1, nPaperWidth);
//                        data.putInt(Global1.INTPARA2, 0);
//                    } else {
//                        if (textViewstatusnets.getText().toString().equals("ok")) {
//                            if (mBitmap != null) {
//                                byte[] command = Utils.decodeBitmap(mBitmap);
//                                printByteData_wifi(command);
//                            } else {
//                                Log.e("Print Photo error", "the file isn't exists");
//                            }
//                            Bundle data = new Bundle();
//                            data.putParcelable(Global1.PARCE1, mBitmap);
//                            data.putInt(Global1.INTPARA1, nPaperWidth);
//                            data.putInt(Global1.INTPARA2, 0);
//                        }
//                    }
//                }
//            } while (c.moveToNext());
//        } else {
//            imageButton.setVisibility(View.GONE);
//        }
//        c.close();

        Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
        if (getcom.moveToFirst()) {
            do {
                strcompanyname = getcom.getString(1);
                straddress1 = getcom.getString(14);
                straddress2 = getcom.getString(17);
                straddress3 = getcom.getString(18);
                strphone = getcom.getString(2);
                stremailid = getcom.getString(15);
                strwebsite = getcom.getString(16);
                strtaxone = getcom.getString(10);
                strbillone = getcom.getString(12);
            } while (getcom.moveToNext());
        }
        getcom.close();
//        Toast.makeText(getApplicationContext(), "saving4", Toast.LENGTH_LONG).show();
        tvkot.setText(strcompanyname);
        if (tvkot.getText().toString().equals("")) {

        } else {
            allbuf1 = new byte[][]{
                    bold, cen, strcompanyname.getBytes(), LF
            };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(bold);    //
                BluetoothPrintDriver.BT_Write(cen);    //
                BT_Write(strcompanyname);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(bold);    //
                    wifiSocket2.WIFI_Write(cen);    //
                    wifiSocket2.WIFI_Write(strcompanyname);
                    wifiSocket2.WIFI_Write(LF);    //
                } else {
                    if (textViewstatusnets.getText().toString().equals("ok")) {
                        wifiSocket.WIFI_Write(bold);    //
                        wifiSocket.WIFI_Write(cen);    //
                        wifiSocket.WIFI_Write(strcompanyname);
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }
        }
/////////
        tvkot.setText(straddress1);
        if (tvkot.getText().toString().equals("")) {

        } else {
            allbuf2 = new byte[][]{
                    normal, cen, straddress1.getBytes(), LF
            };

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(normal);    //
                BluetoothPrintDriver.BT_Write(cen);    //
                BT_Write(straddress1);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(normal);    //
                    wifiSocket2.WIFI_Write(cen);    //
                    wifiSocket2.WIFI_Write(straddress1);
                    wifiSocket2.WIFI_Write(LF);    //
                } else {
                    if (textViewstatusnets.getText().toString().equals("ok")) {
                        wifiSocket.WIFI_Write(normal);    //
                        wifiSocket.WIFI_Write(cen);    //
                        wifiSocket.WIFI_Write(straddress1);
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }
        }
///////////////////////////////
        tvkot.setText(straddress2);
        if (tvkot.getText().toString().equals("")) {

        } else {
            allbuf3 = new byte[][]{
                    normal, cen, straddress2.getBytes(), LF
            };

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(normal);    //
                BluetoothPrintDriver.BT_Write(cen);    //
                BT_Write(straddress2);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(normal);    //
                    wifiSocket2.WIFI_Write(cen);    //
                    wifiSocket2.WIFI_Write(straddress2);
                    wifiSocket2.WIFI_Write(LF);    //
                } else {
                    if (textViewstatusnets.getText().toString().equals("ok")) {
                        wifiSocket.WIFI_Write(normal);    //
                        wifiSocket.WIFI_Write(cen);    //
                        wifiSocket.WIFI_Write(straddress2);
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }
        }
////////////////////////////////
        tvkot.setText(straddress3);
        if (tvkot.getText().toString().equals("")) {

        } else {
            allbuf4 = new byte[][]{
                    normal, cen, straddress3.getBytes(), LF
            };

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(normal);    //
                BluetoothPrintDriver.BT_Write(cen);    //
                BT_Write(straddress3);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(normal);    //
                    wifiSocket2.WIFI_Write(cen);    //
                    wifiSocket2.WIFI_Write(straddress3);
                    wifiSocket2.WIFI_Write(LF);    //
                } else {
                    if (textViewstatusnets.getText().toString().equals("ok")) {
                        wifiSocket.WIFI_Write(normal);    //
                        wifiSocket.WIFI_Write(cen);    //
                        wifiSocket.WIFI_Write(straddress3);
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }
        }
//////////////////////////
        tvkot.setText(strphone);
        String pp = "Ph. " + strphone;
        if (tvkot.getText().toString().equals("")) {

        } else {
            allbuf5 = new byte[][]{
                    normal, cen, pp.getBytes(), LF
            };

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(normal);    //
                BluetoothPrintDriver.BT_Write(cen);    //
                BT_Write(pp);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(normal);    //
                    wifiSocket2.WIFI_Write(cen);    //
                    wifiSocket2.WIFI_Write(pp);
                    wifiSocket2.WIFI_Write(LF);    //
                } else {
                    if (textViewstatusnets.getText().toString().equals("ok")) {
                        wifiSocket.WIFI_Write(normal);    //
                        wifiSocket.WIFI_Write(cen);    //
                        wifiSocket.WIFI_Write(pp);
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }
        }
////////////////////////////////////
        tvkot.setText(stremailid);
        if (tvkot.getText().toString().equals("")) {

        } else {
            allbuf6 = new byte[][]{
                    normal, cen, stremailid.getBytes(), LF
            };

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(normal);    //
                BluetoothPrintDriver.BT_Write(cen);    //
                BT_Write(stremailid);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(normal);    //
                    wifiSocket2.WIFI_Write(cen);    //
                    wifiSocket2.WIFI_Write(stremailid);
                    wifiSocket2.WIFI_Write(LF);    //
                } else {
                    if (textViewstatusnets.getText().toString().equals("ok")) {
                        wifiSocket.WIFI_Write(normal);    //
                        wifiSocket.WIFI_Write(cen);    //
                        wifiSocket.WIFI_Write(stremailid);
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }
        }
////////////////////////////////////////
        tvkot.setText(strwebsite);
        if (tvkot.getText().toString().equals("")) {

        } else {
            allbuf7 = new byte[][]{
                    normal, cen, strwebsite.getBytes(), LF
            };

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(normal);    //
                BluetoothPrintDriver.BT_Write(cen);    //
                BT_Write(strwebsite);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(normal);    //
                    wifiSocket2.WIFI_Write(cen);    //
                    wifiSocket2.WIFI_Write(strwebsite);
                    wifiSocket2.WIFI_Write(LF);    //
                } else {
                    if (textViewstatusnets.getText().toString().equals("ok")) {
                        wifiSocket.WIFI_Write(normal);    //
                        wifiSocket.WIFI_Write(cen);    //
                        wifiSocket.WIFI_Write(strwebsite);
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }
        }
///////////////////////////////////////
        tvkot.setText(strtaxone);
        if (tvkot.getText().toString().equals("")) {

        } else {
            allbuf8 = new byte[][]{
                    normal, cen, strtaxone.getBytes(), LF
            };

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(normal);    //
                BluetoothPrintDriver.BT_Write(cen);    //
                BT_Write(strtaxone);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(normal);    //
                    wifiSocket2.WIFI_Write(cen);    //
                    wifiSocket2.WIFI_Write(strtaxone);
                    wifiSocket2.WIFI_Write(LF);    //
                } else {
                    if (textViewstatusnets.getText().toString().equals("ok")) {
                        wifiSocket.WIFI_Write(normal);    //
                        wifiSocket.WIFI_Write(cen);    //
                        wifiSocket.WIFI_Write(strtaxone);
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }
        }
/////////////////////////////////////////
        if (textViewstatussusbs.getText().toString().equals("ok")) {
            BluetoothPrintDriver.BT_Write(normal);    //
            BluetoothPrintDriver.BT_Write(left);    //
            BluetoothPrintDriver.BT_Write(un1);    //
            BT_Write(str_line);
            BluetoothPrintDriver.BT_Write(LF);    //
        } else {
            if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                wifiSocket2.WIFI_Write(normal);    //
                wifiSocket2.WIFI_Write(left);    //
                wifiSocket2.WIFI_Write(un1);    //
                wifiSocket2.WIFI_Write(str_line);
                wifiSocket2.WIFI_Write(LF);    //
            } else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(normal);    //
                    wifiSocket.WIFI_Write(left);    //
                    wifiSocket.WIFI_Write(un1);    //
                    wifiSocket.WIFI_Write(str_line);
                    wifiSocket.WIFI_Write(LF);    //
                }
            }
        }

//        if (e_wallet.equals("Cash")) {
//            rr2 = "Cash"; //0
//        }
//        if (e_wallet.equals("Card")) {
//            rr2 = "Card"; //0
//        }
//        if (e_wallet.equals("Paytm")) {
//            rr2 = "Paytm"; //0
//        }
//        if (e_wallet.equals("Mobikwik")) {
//            rr2 = "Mobikwik"; //0
//        }
//        if (e_wallet.equals("Freecharge")) {
//            rr2 = "Freecharge"; //0
//        }
//        if (e_wallet.equals("Credit")) {
//            rr2 = "Credit"; //0
//        }
//        if (e_wallet.equals("Cheque")) {
//            rr2 = "Cheque"; //0
//        }
//        if (e_wallet.equals("Sodexo")) {
//            rr2 = "Sodexo"; //0
//        }
//        if (e_wallet.equals("Zeta")) {
//            rr2 = "Zeta"; //0
//        }
//        if (e_wallet.equals("Ticket")) {
//            rr2 = "Ticket"; //0
//        }

//        String bill_no = billnum.getText().toString();
//        allbufbillno = new byte[][]{
//                setHT32, "Bill no.".getBytes(), bill_no.getBytes(), HT, "   ".getBytes(), rr2.getBytes(), LF
//        };

        if (textViewstatussusbs.getText().toString().equals("ok")) {
            BluetoothPrintDriver.BT_Write(setHT321);    //
            BluetoothPrintDriver.BT_Write(un1);    //
            BT_Write("Order no." + order_id);
//            BluetoothPrintDriver.BT_Write(HT);    //
//            BT_Write("   ");
//            BT_Write(order_type);
            BluetoothPrintDriver.BT_Write(LF);    //
        } else {
            if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                wifiSocket2.WIFI_Write(setHT321);    //
                wifiSocket2.WIFI_Write(un1);    //
                wifiSocket2.WIFI_Write("Order no." + order_id);
//                wifiSocket2.WIFI_Write(HT);    //
//                wifiSocket2.WIFI_Write("   ");
//                wifiSocket2.WIFI_Write(order_type);
                wifiSocket2.WIFI_Write(LF);    //
            } else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(setHT321);    //
                    wifiSocket.WIFI_Write(un1);    //
                    wifiSocket.WIFI_Write("Order no." + order_id);
//                    wifiSocket.WIFI_Write(HT);    //
//                    wifiSocket.WIFI_Write("   ");
//                    wifiSocket.WIFI_Write(order_type);
                    wifiSocket.WIFI_Write(LF);    //
                }
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        final String currentDateandTime = sdf.format(new Date());

        Cursor count = db1.rawQuery("SELECT * FROM Billnumber WHERE billnumber LIKE  '" + currentDateandTime + "-" + "000" + "0" + "1" + "%' ", null);

        final int bill = count.getCount() + 1;
        String bills = String.valueOf(bill);

//        billnum = (TextView) dialog_p1.findViewById(R.id.billnumber1);
        TextView bill_no = new TextView(New_Individualorder_Itemview.this);
        bill_no.setText(currentDateandTime + "-" + "000" + "0" + "1" + bills);

        if (str_print_ty.toString().equals("POS")) {
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHT321);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BT_Write("Bill no." + bill_no.getText().toString());
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write("   ");
                BT_Write(order_type);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(setHT321);    //
                    wifiSocket2.WIFI_Write(un1);    //
                    wifiSocket2.WIFI_Write("Bill no." + bill_no.getText().toString());
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write("   ");
                    wifiSocket2.WIFI_Write(order_type);
                    wifiSocket2.WIFI_Write(LF);    //
                } else {
                    if (textViewstatusnets.getText().toString().equals("ok")) {
                        wifiSocket.WIFI_Write(setHT321);    //
                        wifiSocket.WIFI_Write(un1);    //
                        wifiSocket.WIFI_Write("Bill no." + bill_no.getText().toString());
                        wifiSocket.WIFI_Write(HT);    //
                        wifiSocket.WIFI_Write("   ");
                        wifiSocket.WIFI_Write(order_type);
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }
        } else {
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHT32);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BT_Write("Bill no." + bill_no.getText().toString());
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write(order_type);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(setHT32);    //
                    wifiSocket2.WIFI_Write(un1);    //
                    wifiSocket2.WIFI_Write("Bill no." + bill_no.getText().toString());
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write(order_type);
                    wifiSocket2.WIFI_Write(LF);    //
                } else {
                    if (textViewstatusnets.getText().toString().equals("ok")) {
                        wifiSocket.WIFI_Write(setHT32);    //
                        wifiSocket.WIFI_Write(un1);    //
                        wifiSocket.WIFI_Write("Bill no." + bill_no.getText().toString());
                        wifiSocket.WIFI_Write(HT);    //
                        wifiSocket.WIFI_Write(order_type);
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }
        }

//        if (radioBtncash.getText().toString().equals("  Dine-in") || radioBtncash.getText().toString().equals("  General") || radioBtncash.getText().toString().equals("  Others")) {
//            rr1 = "Dine-in";
//        } else {
//            if (radioBtncash.getText().toString().equals("  Takeaway") || radioBtncash.getText().toString().equals("  Main")) {
//                rr1 = "Takeaway";
//            } else {
//                rr1 = "Home delivery";
//            }
//        }

        SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyy");
        final String normal1 = normal2.format(new Date());

        Date dt = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
        final String time1 = sdf1.format(dt);

        Date dtt = new Date();
        SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
        String time24 = sdf1t.format(dtt);

//        allbuf10 = new byte[][]{
//                setHT32, left, rr1.getBytes(), HT, normal1.getBytes(), LF
//        };

        if (str_print_ty.toString().equals("POS")) {
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHT321);    //
                BluetoothPrintDriver.BT_Write(left);    //
                BT_Write(normal1);
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write("   " + time1);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(setHT321);    //
                    wifiSocket2.WIFI_Write(left);    //
                    wifiSocket2.WIFI_Write(normal1);
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write("   " + time1);
                    wifiSocket2.WIFI_Write(LF);    //
                } else {
                    if (textViewstatusnets.getText().toString().equals("ok")) {
                        wifiSocket.WIFI_Write(setHT321);    //
                        wifiSocket.WIFI_Write(left);    //
                        wifiSocket.WIFI_Write(normal1);
                        wifiSocket.WIFI_Write(HT);    //
                        wifiSocket.WIFI_Write("   " + time1);
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }
        } else {
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHT32);    //
                BluetoothPrintDriver.BT_Write(left);    //
                BT_Write(normal1);
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write(time1);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(setHT32);    //
                    wifiSocket2.WIFI_Write(left);    //
                    wifiSocket2.WIFI_Write(normal1);
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write(time1);
                    wifiSocket2.WIFI_Write(LF);    //
                } else {
                    if (textViewstatusnets.getText().toString().equals("ok")) {
                        wifiSocket.WIFI_Write(setHT32);    //
                        wifiSocket.WIFI_Write(left);    //
                        wifiSocket.WIFI_Write(normal1);
                        wifiSocket.WIFI_Write(HT);    //
                        wifiSocket.WIFI_Write(time1);
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }
        }

//        Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
//        if (vbnm.moveToFirst()) {
//            assa1 = vbnm.getString(1);
//            assa2 = vbnm.getString(2);
//        }
//        vbnm.close();
//
//        TextView cx = new TextView(New_Individualorder_Itemview.this);
//        cx.setText(assa1);
//        String table_idddd = "";
//        Cursor getprint_type1 = db.rawQuery("SELECT * FROM Printer_text_size", null);
//        if (getprint_type1.moveToFirst()) {
//            String type = getprint_type1.getString(1);
//
//            if (cx.getText().toString().equals("")) {
//                table_idddd = "Tab" + assa2;
//            } else {
//                table_idddd = "Tab" + assa1;
//            }
//            allbuftime = new byte[][]{
//                    setHT321, left, table_idddd.getBytes(), HT, "   ".getBytes(), time1.getBytes(), LF
//            };
//
//        }
//        getprint_type1.close();

//        if (textViewstatussusbs.getText().toString().equals("ok")) {
//            BluetoothPrintDriver.BT_Write(setHT321);    //
//            BluetoothPrintDriver.BT_Write(left);    //
////            BT_Write(table_idddd);
////            BluetoothPrintDriver.BT_Write(HT);    //
//            BT_Write("   " + time1);
//            BluetoothPrintDriver.BT_Write(LF);    //
//        } else {
//            if (textViewstatusnets_counter.getText().toString().equals("ok")) {
//                wifiSocket2.WIFI_Write(setHT321);    //
//                wifiSocket2.WIFI_Write(left);    //
////                wifiSocket2.WIFI_Write(table_idddd);
////                wifiSocket2.WIFI_Write(HT);    //
//                wifiSocket2.WIFI_Write("   " + time1);
//                wifiSocket2.WIFI_Write(LF);    //
//            } else {
//                if (textViewstatusnets.getText().toString().equals("ok")) {
//                    wifiSocket.WIFI_Write(setHT321);    //
//                    wifiSocket.WIFI_Write(left);    //
////                    wifiSocket.WIFI_Write(table_idddd);
////                    wifiSocket.WIFI_Write(HT);    //
//                    wifiSocket.WIFI_Write("   " + time1);
//                    wifiSocket.WIFI_Write(LF);    //
//                }
//            }
//        }

        allbuf11 = new byte[][]{
                left, setHT321, "Counter person ".getBytes(), LF
        };
        if (textViewstatussusbs.getText().toString().equals("ok")) {
            BluetoothPrintDriver.BT_Write(left);    //
            BluetoothPrintDriver.BT_Write(setHT321);    //
            BT_Write("Counter person: "+u_name);
            BluetoothPrintDriver.BT_Write(LF);    //
        } else {
            if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                wifiSocket2.WIFI_Write(left);    //
                wifiSocket2.WIFI_Write(setHT321);    //
                wifiSocket2.WIFI_Write("Counter person: "+u_name);
                wifiSocket2.WIFI_Write(LF);    //
            } else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(left);    //
                    wifiSocket.WIFI_Write(setHT321);    //
                    wifiSocket.WIFI_Write("Counter person: "+u_name);
                    wifiSocket.WIFI_Write(LF);    //
                }
            }
        }

        if (textViewstatussusbs.getText().toString().equals("ok")) {
            BluetoothPrintDriver.BT_Write(left);    //
            BluetoothPrintDriver.BT_Write(un1);    //
            BT_Write(str_line);
            BluetoothPrintDriver.BT_Write(LF);    //
        } else {
            if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                wifiSocket2.WIFI_Write(left);    //
                wifiSocket2.WIFI_Write(un1);    //
                wifiSocket2.WIFI_Write(str_line);
                wifiSocket2.WIFI_Write(LF);    //
            } else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(left);    //
                    wifiSocket.WIFI_Write(un1);    //
                    wifiSocket.WIFI_Write(str_line);
                    wifiSocket.WIFI_Write(LF);    //
                }
            }
        }


////////////////////////////////////////////customer details

        if (textViewstatussusbs.getText().toString().equals("ok")) {
            BluetoothPrintDriver.BT_Write(left);    //
            BluetoothPrintDriver.BT_Write(un);    //
            BT_Write("Customer:");
            BluetoothPrintDriver.BT_Write(LF);    //
            BluetoothPrintDriver.BT_Write(un1);    //
        } else {
            if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                wifiSocket2.WIFI_Write(left);    //
                wifiSocket2.WIFI_Write(un);    //
                wifiSocket2.WIFI_Write("Customer:");
                wifiSocket2.WIFI_Write(LF);    //
                wifiSocket2.WIFI_Write(un1);    //
            } else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(left);    //
                    wifiSocket.WIFI_Write(un);    //
                    wifiSocket.WIFI_Write("Customer:");
                    wifiSocket.WIFI_Write(LF);    //
                    wifiSocket.WIFI_Write(un1);    //
                }
            }
        }


        if (textViewstatussusbs.getText().toString().equals("ok")) {
            BT_Write(cust_name);
            BluetoothPrintDriver.BT_Write(LF);    //
        } else {
            if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                wifiSocket2.WIFI_Write(cust_name);
                wifiSocket2.WIFI_Write(LF);    //
            } else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(cust_name);
                    wifiSocket.WIFI_Write(LF);    //
                }
            }
        }

        if (textViewstatussusbs.getText().toString().equals("ok")) {
            BT_Write("Ph. "+cust_phno);
            BluetoothPrintDriver.BT_Write(LF);    //
        } else {
            if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                wifiSocket2.WIFI_Write("Ph. "+cust_phno);
                wifiSocket2.WIFI_Write(LF);    //
            } else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write("Ph. "+cust_phno);
                    wifiSocket.WIFI_Write(LF);    //
                }
            }
        }

        if (textViewstatussusbs.getText().toString().equals("ok")) {
            BT_Write(cust_location);
            BluetoothPrintDriver.BT_Write(LF);    //
        } else {
            if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                wifiSocket2.WIFI_Write(cust_location);
                wifiSocket2.WIFI_Write(LF);    //
            } else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(cust_location);
                    wifiSocket.WIFI_Write(LF);    //
                }
            }
        }

        if (textViewstatussusbs.getText().toString().equals("ok")) {
            BT_Write(cust_street);
            BluetoothPrintDriver.BT_Write(LF);    //
        } else {
            if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                wifiSocket2.WIFI_Write(cust_street);
                wifiSocket2.WIFI_Write(LF);    //
            } else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(cust_street);
                    wifiSocket.WIFI_Write(LF);    //
                }
            }
        }

        if (textViewstatussusbs.getText().toString().equals("ok")) {
            BluetoothPrintDriver.BT_Write(left);    //
            BluetoothPrintDriver.BT_Write(un1);    //
            BT_Write(str_line);
            BluetoothPrintDriver.BT_Write(LF);    //
        } else {
            if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                wifiSocket2.WIFI_Write(left);    //
                wifiSocket2.WIFI_Write(un1);    //
                wifiSocket2.WIFI_Write(str_line);
                wifiSocket2.WIFI_Write(LF);    //
            } else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(left);    //
                    wifiSocket.WIFI_Write(un1);    //
                    wifiSocket.WIFI_Write(str_line);
                    wifiSocket.WIFI_Write(LF);    //
                }
            }
        }


        allbufqty = new byte[][]{
                setHT34, normal, "Qty".getBytes(), HT, "Item".getBytes(), HT, "Price".getBytes(), HT1, "Amount".getBytes(), left, LF
        };

        if (textViewstatussusbs.getText().toString().equals("ok")) {
            BluetoothPrintDriver.BT_Write(setHT34);    //
            BluetoothPrintDriver.BT_Write(normal);    //
            BT_Write("Qty");
            BluetoothPrintDriver.BT_Write(HT);    //
            BT_Write("Item");
            BluetoothPrintDriver.BT_Write(HT);    //
            BT_Write("Price");
            BluetoothPrintDriver.BT_Write(HT1);    //
            BT_Write("Amount");
            BluetoothPrintDriver.BT_Write(left);    //
            BluetoothPrintDriver.BT_Write(LF);    //
        } else {
            if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                wifiSocket2.WIFI_Write(setHT34);    //
                wifiSocket2.WIFI_Write(normal);    //
                wifiSocket2.WIFI_Write("Qty");
                wifiSocket2.WIFI_Write(HT);    //
                wifiSocket2.WIFI_Write("Item");
                wifiSocket2.WIFI_Write(HT);    //
                wifiSocket2.WIFI_Write("Price");
                wifiSocket2.WIFI_Write(HT1);    //
                wifiSocket2.WIFI_Write("Amount");
                wifiSocket2.WIFI_Write(left);    //
                wifiSocket2.WIFI_Write(LF);    //
            } else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(setHT34);    //
                    wifiSocket.WIFI_Write(normal);    //
                    wifiSocket.WIFI_Write("Qty");
                    wifiSocket.WIFI_Write(HT);    //
                    wifiSocket.WIFI_Write("Item");
                    wifiSocket.WIFI_Write(HT);    //
                    wifiSocket.WIFI_Write("Price");
                    wifiSocket.WIFI_Write(HT1);    //
                    wifiSocket.WIFI_Write("Amount");
                    wifiSocket.WIFI_Write(left);    //
                    wifiSocket.WIFI_Write(LF);    //
                }
            }
        }

        if (textViewstatussusbs.getText().toString().equals("ok")) {
            BluetoothPrintDriver.BT_Write(left);    //
            BluetoothPrintDriver.BT_Write(un1);    //
            BT_Write(str_line);
            BluetoothPrintDriver.BT_Write(LF);    //
        } else {
            if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                wifiSocket2.WIFI_Write(left);    //
                wifiSocket2.WIFI_Write(un1);    //
                wifiSocket2.WIFI_Write(str_line);
                wifiSocket2.WIFI_Write(LF);    //
            } else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(left);    //
                    wifiSocket.WIFI_Write(un1);    //
                    wifiSocket.WIFI_Write(str_line);
                    wifiSocket.WIFI_Write(LF);    //
                }
            }
        }



        try {
            //I try to use this for send Header is application/json
            jsonBody = new JSONObject();
            jsonBody.put("merchant_id", str_merch_id);
            jsonBody.put("order_id", order_id);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

        final String finalStr_line = str_line;
        final byte[] finalHT = HT1;
        final String finalStr_line1 = str_line;
        final String finalStr_line2 = str_line;
        final String finalStr_line3 = str_line;
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

                                if (order_info_id.toString().equals(order_id)) {
                                    cust_name = c.getJSONObject("customer").getString("name");
                                    cust_phno = c.getJSONObject("customer").getString("mobile");
                                    cust_street = c.getJSONObject("customer").getString("street");
                                    cust_location = c.getJSONObject("customer").getString("location");
                                    String cust_deli_instru = c.getJSONObject("customer").getString("delivery_instruction");

                                    JSONArray cart = c.getJSONArray("cart");

    //                                HashMap<String, String> contact = new HashMap<>();
    //                                my_array.clear();

    //                                contact.put("orderid", order_info_id);

                                    ArrayList l2 = new ArrayList();


//                                    date_time1.setText(date_time);
                                    for (int j = 0; j < cart.length(); j++) {
                                        JSONObject name1 = cart.getJSONObject(j);
                                        String name = name1.getString("pos_item_name");
                                        String qty = name1.getString("qty");
                                        String price0 = name1.getString("price");
                                        String total_pretty = name1.getString("total_pretty");
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

                                        String instructions = name1.getString("order_notes");

                                        if (name.toString().length() > charlength) {
                                            int print1 = 0;

                                            if (qty.length() > quanlentha && name.toString().length() > charlength) {
                                                String string1quan = qty.substring(0, quanlentha);
                                                String string2quan = qty.substring(quanlentha);
                                                String string1 = name.substring(0, charlength);
                                                String string2 = name.substring(charlength);
                                                allbufitems = new byte[][]{
                                                        setHT34, normal, string1quan.getBytes(), HT, string1.getBytes(), HT, price0.getBytes(), finalHT, total_pretty.getBytes(), LF, string2quan.getBytes(), HT, string2.getBytes(), LF
                                                };
                                                if (print1 == 0) {
                                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                        BluetoothPrintDriver.BT_Write(setHT34);    //
                                                        BluetoothPrintDriver.BT_Write(normal);    //
                                                        BT_Write(string1quan);
                                                        BluetoothPrintDriver.BT_Write(HT);    //
                                                        BT_Write(string1);
                                                        BluetoothPrintDriver.BT_Write(HT);    //
                                                        BT_Write(price0);
                                                        BluetoothPrintDriver.BT_Write(finalHT);    //
                                                        BT_Write(total_pretty);
                                                        BluetoothPrintDriver.BT_Write(LF);    //
                                                        BT_Write(string2quan);
                                                        BluetoothPrintDriver.BT_Write(HT);    //
                                                        BT_Write(string2);
                                                        BluetoothPrintDriver.BT_Write(LF);    //
                                                        print1 = 1;
                                                    } else {
                                                        if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                                            wifiSocket2.WIFI_Write(setHT34);    //
                                                            wifiSocket2.WIFI_Write(normal);    //
                                                            wifiSocket2.WIFI_Write(string1quan);
                                                            wifiSocket2.WIFI_Write(HT);    //
                                                            wifiSocket2.WIFI_Write(string1);
                                                            wifiSocket2.WIFI_Write(HT);    //
                                                            wifiSocket2.WIFI_Write(price0);
                                                            wifiSocket2.WIFI_Write(finalHT);    //
                                                            wifiSocket2.WIFI_Write(total_pretty);
                                                            wifiSocket2.WIFI_Write(LF);    //
                                                            wifiSocket2.WIFI_Write(string2quan);
                                                            wifiSocket2.WIFI_Write(HT);    //
                                                            wifiSocket2.WIFI_Write(string2);
                                                            wifiSocket2.WIFI_Write(LF);    //
                                                            print1 = 1;
                                                        } else {
                                                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                                                wifiSocket.WIFI_Write(setHT34);    //
                                                                wifiSocket.WIFI_Write(normal);    //
                                                                wifiSocket.WIFI_Write(string1quan);
                                                                wifiSocket.WIFI_Write(HT);    //
                                                                wifiSocket.WIFI_Write(string1);
                                                                wifiSocket.WIFI_Write(HT);    //
                                                                wifiSocket.WIFI_Write(price0);
                                                                wifiSocket.WIFI_Write(finalHT);    //
                                                                wifiSocket.WIFI_Write(total_pretty);
                                                                wifiSocket.WIFI_Write(LF);    //
                                                                wifiSocket.WIFI_Write(string2quan);
                                                                wifiSocket.WIFI_Write(HT);    //
                                                                wifiSocket.WIFI_Write(string2);
                                                                wifiSocket.WIFI_Write(LF);    //
                                                                print1 = 1;
                                                            }
                                                        }
                                                    }
                                                }

                                            }
                                            if (qty.length() <= quanlentha && name.toString().length() > charlength) {
                                                String string1 = name.substring(0, charlength);
                                                String string2 = name.substring(charlength);
                                                allbufitems = new byte[][]{
                                                        setHT34, normal, qty.getBytes(), HT, string1.getBytes(), HT, price0.getBytes(), finalHT, total_pretty.getBytes(), LF, "      ".getBytes(), string2.getBytes(), LF
                                                };
                                                if (print1 == 0) {
                                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                        BluetoothPrintDriver.BT_Write(setHT34);    //
                                                        BluetoothPrintDriver.BT_Write(normal);    //
                                                        BT_Write(qty);
                                                        BluetoothPrintDriver.BT_Write(HT);    //
                                                        BT_Write(string1);
                                                        BluetoothPrintDriver.BT_Write(HT);    //
                                                        BT_Write(price0);
                                                        BluetoothPrintDriver.BT_Write(finalHT);    //
                                                        BT_Write(total_pretty);
                                                        BluetoothPrintDriver.BT_Write(LF);    //
                                                        BT_Write("      ");
                                                        BT_Write(string2);
                                                        BluetoothPrintDriver.BT_Write(LF);    //
                                                        print1 = 1;
                                                    } else {
                                                        if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                                            wifiSocket2.WIFI_Write(setHT34);    //
                                                            wifiSocket2.WIFI_Write(normal);    //
                                                            wifiSocket2.WIFI_Write(qty);
                                                            wifiSocket2.WIFI_Write(HT);    //
                                                            wifiSocket2.WIFI_Write(string1);
                                                            wifiSocket2.WIFI_Write(HT);    //
                                                            wifiSocket2.WIFI_Write(price0);
                                                            wifiSocket2.WIFI_Write(finalHT);    //
                                                            wifiSocket2.WIFI_Write(total_pretty);
                                                            wifiSocket2.WIFI_Write(LF);    //
                                                            wifiSocket2.WIFI_Write("      ");
                                                            wifiSocket2.WIFI_Write(string2);
                                                            wifiSocket2.WIFI_Write(LF);    //
                                                            print1 = 1;
                                                        } else {
                                                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                                                wifiSocket.WIFI_Write(setHT34);    //
                                                                wifiSocket.WIFI_Write(normal);    //
                                                                wifiSocket.WIFI_Write(qty);
                                                                wifiSocket.WIFI_Write(HT);    //
                                                                wifiSocket.WIFI_Write(string1);
                                                                wifiSocket.WIFI_Write(HT);    //
                                                                wifiSocket.WIFI_Write(price0);
                                                                wifiSocket.WIFI_Write(finalHT);    //
                                                                wifiSocket.WIFI_Write(total_pretty);
                                                                wifiSocket.WIFI_Write(LF);    //
                                                                wifiSocket.WIFI_Write("      ");
                                                                wifiSocket.WIFI_Write(string2);
                                                                wifiSocket.WIFI_Write(LF);    //
                                                                print1 = 1;
                                                            }
                                                        }
                                                    }
                                                }
                                            }

/////////////////////////////////////////////////////////////
                                            if (qty.length() > quanlentha && name.toString().length() > charlength1) {
                                                String string1quan = qty.substring(0, quanlentha);
                                                String string2quan = qty.substring(quanlentha);
                                                String string1 = name.substring(0, charlength);
                                                String string2 = name.substring(charlength, charlength1);
                                                String string3 = name.substring(charlength1);
                                                allbufitems = new byte[][]{
                                                        setHT34, normal, string1quan.getBytes(), HT, string1.getBytes(), HT, price0.getBytes(), finalHT, total_pretty.getBytes(), LF, string2quan.getBytes(), HT, string2.getBytes(), LF, "      ".getBytes(), string3.getBytes(), left, LF,
                                                };
                                                if (print1 == 0) {
                                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                        BluetoothPrintDriver.BT_Write(setHT34);    //
                                                        BluetoothPrintDriver.BT_Write(normal);    //
                                                        BT_Write(string1quan);
                                                        BluetoothPrintDriver.BT_Write(HT);    //
                                                        BT_Write(string1);
                                                        BluetoothPrintDriver.BT_Write(HT);    //
                                                        BT_Write(price0);
                                                        BluetoothPrintDriver.BT_Write(finalHT);    //
                                                        BT_Write(total_pretty);
                                                        BluetoothPrintDriver.BT_Write(LF);    //
                                                        BT_Write(string2quan);
                                                        BluetoothPrintDriver.BT_Write(HT);    //
                                                        BT_Write(string2);
                                                        BluetoothPrintDriver.BT_Write(LF);    //
                                                        BT_Write("      ");
                                                        BT_Write(string3);
                                                        BluetoothPrintDriver.BT_Write(left);    //
                                                        BluetoothPrintDriver.BT_Write(LF);    //
                                                        print1 = 1;
                                                    } else {
                                                        if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                                            wifiSocket2.WIFI_Write(setHT34);    //
                                                            wifiSocket2.WIFI_Write(normal);    //
                                                            wifiSocket2.WIFI_Write(string1quan);
                                                            wifiSocket2.WIFI_Write(HT);    //
                                                            wifiSocket2.WIFI_Write(string1);
                                                            wifiSocket2.WIFI_Write(HT);    //
                                                            wifiSocket2.WIFI_Write(price0);
                                                            wifiSocket2.WIFI_Write(finalHT);    //
                                                            wifiSocket2.WIFI_Write(total_pretty);
                                                            wifiSocket2.WIFI_Write(LF);    //
                                                            wifiSocket2.WIFI_Write(string2quan);
                                                            wifiSocket2.WIFI_Write(HT);    //
                                                            wifiSocket2.WIFI_Write(string2);
                                                            wifiSocket2.WIFI_Write(LF);    //
                                                            wifiSocket2.WIFI_Write("      ");
                                                            wifiSocket2.WIFI_Write(string3);
                                                            wifiSocket2.WIFI_Write(left);    //
                                                            wifiSocket2.WIFI_Write(LF);    //
                                                            print1 = 1;
                                                        } else {
                                                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                                                wifiSocket.WIFI_Write(setHT34);    //
                                                                wifiSocket.WIFI_Write(normal);    //
                                                                wifiSocket.WIFI_Write(string1quan);
                                                                wifiSocket.WIFI_Write(HT);    //
                                                                wifiSocket.WIFI_Write(string1);
                                                                wifiSocket.WIFI_Write(HT);    //
                                                                wifiSocket.WIFI_Write(price0);
                                                                wifiSocket.WIFI_Write(finalHT);    //
                                                                wifiSocket.WIFI_Write(total_pretty);
                                                                wifiSocket.WIFI_Write(LF);    //
                                                                wifiSocket.WIFI_Write(string2quan);
                                                                wifiSocket.WIFI_Write(HT);    //
                                                                wifiSocket.WIFI_Write(string2);
                                                                wifiSocket.WIFI_Write(LF);    //
                                                                wifiSocket.WIFI_Write("      ");
                                                                wifiSocket.WIFI_Write(string3);
                                                                wifiSocket.WIFI_Write(left);    //
                                                                wifiSocket.WIFI_Write(LF);    //
                                                                print1 = 1;
                                                            }
                                                        }
                                                    }
                                                }

                                            }
                                            if (qty.length() <= quanlentha && name.toString().length() > charlength1) {
                                                String string1 = name.substring(0, charlength);
                                                String string2 = name.substring(charlength, charlength1);
                                                String string3 = name.substring(charlength1);
                                                allbufitems = new byte[][]{
                                                        setHT34, normal, qty.getBytes(), HT, string1.getBytes(), HT, price0.getBytes(), finalHT, total_pretty.getBytes(), LF, "      ".getBytes(), string2.getBytes(), LF, "      ".getBytes(), string3.getBytes(), left, LF,
                                                };
                                                if (print1 == 0) {
                                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                        BluetoothPrintDriver.BT_Write(setHT34);    //
                                                        BluetoothPrintDriver.BT_Write(normal);    //
                                                        BT_Write(qty);
                                                        BluetoothPrintDriver.BT_Write(HT);    //
                                                        BT_Write(string1);
                                                        BluetoothPrintDriver.BT_Write(HT);    //
                                                        BT_Write(price0);
                                                        BluetoothPrintDriver.BT_Write(finalHT);    //
                                                        BT_Write(total_pretty);
                                                        BluetoothPrintDriver.BT_Write(LF);    //
                                                        BT_Write("      ");
                                                        BT_Write(string2);
                                                        BluetoothPrintDriver.BT_Write(LF);    //
                                                        BT_Write("      ");
                                                        BT_Write(string3);
                                                        BluetoothPrintDriver.BT_Write(left);    //
                                                        BluetoothPrintDriver.BT_Write(LF);    //
                                                        print1 = 1;
                                                    } else {
                                                        if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                                            wifiSocket2.WIFI_Write(setHT34);    //
                                                            wifiSocket2.WIFI_Write(normal);    //
                                                            wifiSocket2.WIFI_Write(qty);
                                                            wifiSocket2.WIFI_Write(HT);    //
                                                            wifiSocket2.WIFI_Write(string1);
                                                            wifiSocket2.WIFI_Write(HT);    //
                                                            wifiSocket2.WIFI_Write(price0);
                                                            wifiSocket2.WIFI_Write(finalHT);    //
                                                            wifiSocket2.WIFI_Write(total_pretty);
                                                            wifiSocket2.WIFI_Write(LF);    //
                                                            wifiSocket2.WIFI_Write("      ");
                                                            wifiSocket2.WIFI_Write(string2);
                                                            wifiSocket2.WIFI_Write(LF);    //
                                                            wifiSocket2.WIFI_Write("      ");
                                                            wifiSocket2.WIFI_Write(string3);
                                                            wifiSocket2.WIFI_Write(left);    //
                                                            wifiSocket2.WIFI_Write(LF);    //
                                                            print1 = 1;
                                                        } else {
                                                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                                                wifiSocket.WIFI_Write(setHT34);    //
                                                                wifiSocket.WIFI_Write(normal);    //
                                                                wifiSocket.WIFI_Write(qty);
                                                                wifiSocket.WIFI_Write(HT);    //
                                                                wifiSocket.WIFI_Write(string1);
                                                                wifiSocket.WIFI_Write(HT);    //
                                                                wifiSocket.WIFI_Write(price0);
                                                                wifiSocket.WIFI_Write(finalHT);    //
                                                                wifiSocket.WIFI_Write(total_pretty);
                                                                wifiSocket.WIFI_Write(LF);    //
                                                                wifiSocket.WIFI_Write("      ");
                                                                wifiSocket.WIFI_Write(string2);
                                                                wifiSocket.WIFI_Write(LF);    //
                                                                wifiSocket.WIFI_Write("      ");
                                                                wifiSocket.WIFI_Write(string3);
                                                                wifiSocket.WIFI_Write(left);    //
                                                                wifiSocket.WIFI_Write(LF);    //
                                                                print1 = 1;
                                                            }
                                                        }
                                                    }
                                                }
                                            }


                                        } else {

                                            if (qty.toString().length() > quanlentha) {
                                                String string1quan = qty.substring(0, quanlentha);
                                                String string2quan = qty.substring(quanlentha);
                                                allbufitems = new byte[][]{
                                                        setHT34, normal, string1quan.getBytes(), HT, name.getBytes(), HT, price0.getBytes(), finalHT, total_pretty.getBytes(), left, LF, string2quan.getBytes(), LF
                                                };
                                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                    BluetoothPrintDriver.BT_Write(setHT34);    //
                                                    BluetoothPrintDriver.BT_Write(normal);    //
                                                    BT_Write(string1quan);
                                                    BluetoothPrintDriver.BT_Write(HT);    //
                                                    BT_Write(name);
                                                    BluetoothPrintDriver.BT_Write(HT);    //
                                                    BT_Write(price0);
                                                    BluetoothPrintDriver.BT_Write(finalHT);    //
                                                    BT_Write(total_pretty);
                                                    BluetoothPrintDriver.BT_Write(left);    //
                                                    BluetoothPrintDriver.BT_Write(LF);    //
                                                    BT_Write(string2quan);
                                                    BluetoothPrintDriver.BT_Write(LF);    //
                                                } else {
                                                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                                        wifiSocket2.WIFI_Write(setHT34);    //
                                                        wifiSocket2.WIFI_Write(normal);    //
                                                        wifiSocket2.WIFI_Write(string1quan);
                                                        wifiSocket2.WIFI_Write(HT);    //
                                                        wifiSocket2.WIFI_Write(name);
                                                        wifiSocket2.WIFI_Write(HT);    //
                                                        wifiSocket2.WIFI_Write(price0);
                                                        wifiSocket2.WIFI_Write(finalHT);    //
                                                        wifiSocket2.WIFI_Write(total_pretty);
                                                        wifiSocket2.WIFI_Write(left);    //
                                                        wifiSocket2.WIFI_Write(LF);    //
                                                        wifiSocket2.WIFI_Write(string2quan);
                                                        wifiSocket2.WIFI_Write(LF);    //
                                                    } else {
                                                        if (textViewstatusnets.getText().toString().equals("ok")) {
                                                            wifiSocket.WIFI_Write(setHT34);    //
                                                            wifiSocket.WIFI_Write(normal);    //
                                                            wifiSocket.WIFI_Write(string1quan);
                                                            wifiSocket.WIFI_Write(HT);    //
                                                            wifiSocket.WIFI_Write(name);
                                                            wifiSocket.WIFI_Write(HT);    //
                                                            wifiSocket.WIFI_Write(price0);
                                                            wifiSocket.WIFI_Write(finalHT);    //
                                                            wifiSocket.WIFI_Write(total_pretty);
                                                            wifiSocket.WIFI_Write(left);    //
                                                            wifiSocket.WIFI_Write(LF);    //
                                                            wifiSocket.WIFI_Write(string2quan);
                                                            wifiSocket.WIFI_Write(LF);    //
                                                        }
                                                    }
                                                }


                                            } else {
                                                allbufitems = new byte[][]{
                                                        setHT34, normal, qty.getBytes(), HT, name.getBytes(), HT, price0.getBytes(), finalHT, total_pretty.getBytes(), left, LF,
                                                };
                                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                    BluetoothPrintDriver.BT_Write(setHT34);    //
                                                    BluetoothPrintDriver.BT_Write(normal);    //
                                                    BT_Write(qty);
                                                    BluetoothPrintDriver.BT_Write(HT);    //
                                                    BT_Write(name);
                                                    BluetoothPrintDriver.BT_Write(HT);    //
                                                    BT_Write(price0);
                                                    BluetoothPrintDriver.BT_Write(finalHT);    //
                                                    BT_Write(total_pretty);
                                                    BluetoothPrintDriver.BT_Write(left);    //
                                                    BluetoothPrintDriver.BT_Write(LF);    //
                                                } else {
                                                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                                        wifiSocket2.WIFI_Write(setHT34);    //
                                                        wifiSocket2.WIFI_Write(normal);    //
                                                        wifiSocket2.WIFI_Write(qty);
                                                        wifiSocket2.WIFI_Write(HT);    //
                                                        wifiSocket2.WIFI_Write(name);
                                                        wifiSocket2.WIFI_Write(HT);    //
                                                        wifiSocket2.WIFI_Write(price0);
                                                        wifiSocket2.WIFI_Write(finalHT);    //
                                                        wifiSocket2.WIFI_Write(total_pretty);
                                                        wifiSocket2.WIFI_Write(left);    //
                                                        wifiSocket2.WIFI_Write(LF);    //
                                                    } else {
                                                        if (textViewstatusnets.getText().toString().equals("ok")) {
                                                            wifiSocket.WIFI_Write(setHT34);    //
                                                            wifiSocket.WIFI_Write(normal);    //
                                                            wifiSocket.WIFI_Write(qty);
                                                            wifiSocket.WIFI_Write(HT);    //
                                                            wifiSocket.WIFI_Write(name);
                                                            wifiSocket.WIFI_Write(HT);    //
                                                            wifiSocket.WIFI_Write(price0);
                                                            wifiSocket.WIFI_Write(finalHT);    //
                                                            wifiSocket.WIFI_Write(total_pretty);
                                                            wifiSocket.WIFI_Write(left);    //
                                                            wifiSocket.WIFI_Write(LF);    //
                                                        }
                                                    }
                                                }

                                            }

                                        }

                                    }

                                }
                            }


                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                BluetoothPrintDriver.BT_Write(left);    //
                                BluetoothPrintDriver.BT_Write(un1);    //
                                BT_Write(finalStr_line1);
                                BluetoothPrintDriver.BT_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write(un1);    //
                                    wifiSocket2.WIFI_Write(finalStr_line1);
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(left);    //
                                        wifiSocket.WIFI_Write(un1);    //
                                        wifiSocket.WIFI_Write(finalStr_line1);
                                        wifiSocket.WIFI_Write(LF);    //
                                    }
                                }
                            }


                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                BluetoothPrintDriver.BT_Write(setHT32);    //
                                BT_Write("Sub total");
                                BluetoothPrintDriver.BT_Write(HT);    //
                                BT_Write(sub_total.getText().toString());
                                BluetoothPrintDriver.BT_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(setHT32);    //
                                    wifiSocket2.WIFI_Write("Sub total");
                                    wifiSocket2.WIFI_Write(HT);    //
                                    wifiSocket2.WIFI_Write(sub_total.getText().toString());
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(setHT32);    //
                                        wifiSocket.WIFI_Write("Sub total");
                                        wifiSocket.WIFI_Write(HT);    //
                                        wifiSocket.WIFI_Write(sub_total.getText().toString());
                                        wifiSocket.WIFI_Write(LF);    //
                                    }
                                }
                            }

                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                BluetoothPrintDriver.BT_Write(setHT32);    //
                                BluetoothPrintDriver.BT_Write(left);    //
                                BT_Write("Tax");
                                BluetoothPrintDriver.BT_Write(HT);    //
                                BT_Write(g_c);
                                BluetoothPrintDriver.BT_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(setHT32);    //
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write("Tax");
                                    wifiSocket2.WIFI_Write(HT);    //
                                    wifiSocket2.WIFI_Write(g_c);
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(setHT32);    //
                                        wifiSocket.WIFI_Write(left);    //
                                        wifiSocket.WIFI_Write("Tax");
                                        wifiSocket.WIFI_Write(HT);    //
                                        wifiSocket.WIFI_Write(g_c);
                                        wifiSocket.WIFI_Write(LF);    //
                                    }
                                }
                            }


                            String alldiscinperc1 = "Discount(" + disc_percent.getText().toString() + "%)";

                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                BluetoothPrintDriver.BT_Write(setHT32);    //
                                BluetoothPrintDriver.BT_Write(left);    //
                                BT_Write(alldiscinperc1);
                                BluetoothPrintDriver.BT_Write(HT);    //
                                BT_Write(disc_value.getText().toString());
                                BluetoothPrintDriver.BT_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(setHT32);    //
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write(alldiscinperc1);
                                    wifiSocket2.WIFI_Write(HT);    //
                                    wifiSocket2.WIFI_Write(disc_value.getText().toString());
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(setHT32);    //
                                        wifiSocket.WIFI_Write(left);    //
                                        wifiSocket.WIFI_Write(alldiscinperc1);
                                        wifiSocket.WIFI_Write(HT);    //
                                        wifiSocket.WIFI_Write(disc_value.getText().toString());
                                        wifiSocket.WIFI_Write(LF);    //
                                    }
                                }
                            }

                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                BluetoothPrintDriver.BT_Write(setHT32);    //
                                BluetoothPrintDriver.BT_Write(left);    //
                                BT_Write("Delivery Charges");
                                BluetoothPrintDriver.BT_Write(HT);    //
                                BT_Write(deliv_char.getText().toString());
                                BluetoothPrintDriver.BT_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(setHT32);    //
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write("Delivery Charges");
                                    wifiSocket2.WIFI_Write(HT);    //
                                    wifiSocket2.WIFI_Write(deliv_char.getText().toString());
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(setHT32);    //
                                        wifiSocket.WIFI_Write(left);    //
                                        wifiSocket.WIFI_Write("Delivery Charges");
                                        wifiSocket.WIFI_Write(HT);    //
                                        wifiSocket.WIFI_Write(deliv_char.getText().toString());
                                        wifiSocket.WIFI_Write(LF);    //
                                    }
                                }
                            }


                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                BluetoothPrintDriver.BT_Write(setHT32);    //
                                BluetoothPrintDriver.BT_Write(left);    //
                                BT_Write("Packing Charges");
                                BluetoothPrintDriver.BT_Write(HT);    //
                                BT_Write(p_c);
                                BluetoothPrintDriver.BT_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(setHT32);    //
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write("Packing Charges");
                                    wifiSocket2.WIFI_Write(HT);    //
                                    wifiSocket2.WIFI_Write(p_c);
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(setHT32);    //
                                        wifiSocket.WIFI_Write(left);    //
                                        wifiSocket.WIFI_Write("Packing Charges");
                                        wifiSocket.WIFI_Write(HT);    //
                                        wifiSocket.WIFI_Write(p_c);
                                        wifiSocket.WIFI_Write(LF);    //
                                    }
                                }
                            }

                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                BluetoothPrintDriver.BT_Write(left);    //
                                BluetoothPrintDriver.BT_Write(un1);    //
                                BT_Write(finalStr_line2);
                                BluetoothPrintDriver.BT_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write(un1);    //
                                    wifiSocket2.WIFI_Write(finalStr_line2);
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(left);    //
                                        wifiSocket.WIFI_Write(un1);    //
                                        wifiSocket.WIFI_Write(finalStr_line2);
                                        wifiSocket.WIFI_Write(LF);    //
                                    }
                                }
                            }

                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                BluetoothPrintDriver.BT_Write(setHT3212);    //
                                BluetoothPrintDriver.BT_Write(bold);    //
                                BluetoothPrintDriver.BT_Write(left);    //
                                BT_Write("Total");
                                BluetoothPrintDriver.BT_Write(HT);    //
//                                BT_Write(insert1_rs);
                                BT_Write(totalprice.getText().toString());
                                BluetoothPrintDriver.BT_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(setHT3212);    //
                                    wifiSocket2.WIFI_Write(bold);    //
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write("Total");
                                    wifiSocket2.WIFI_Write(HT);    //
//                                    wifiSocket2.WIFI_Write(insert1_rs);
                                    wifiSocket2.WIFI_Write(totalprice.getText().toString());
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(setHT3212);    //
                                        wifiSocket.WIFI_Write(bold);    //
                                        wifiSocket.WIFI_Write(left);    //
                                        wifiSocket.WIFI_Write("Total");
                                        wifiSocket.WIFI_Write(HT);    //
                                        wifiSocket.WIFI_Write(insert1_rs);
                                        wifiSocket.WIFI_Write(totalprice.getText().toString());
                                        wifiSocket.WIFI_Write(LF);    //
                                    }
                                }
                            }
///////////////////////////////////////////////////////////////
                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                BluetoothPrintDriver.BT_Write(normal);    //
                                BluetoothPrintDriver.BT_Write(left);    //
                                BluetoothPrintDriver.BT_Write(un1);    //
                                BT_Write(finalStr_line3);
                                BluetoothPrintDriver.BT_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(normal);    //
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write(un1);    //
                                    wifiSocket2.WIFI_Write(finalStr_line3);
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(normal);    //
                                        wifiSocket.WIFI_Write(left);    //
                                        wifiSocket.WIFI_Write(un1);    //
                                        wifiSocket.WIFI_Write(finalStr_line3);
                                        wifiSocket.WIFI_Write(LF);    //
                                    }
                                }
                            }


                            String totalqu = "No. of items : " + String.valueOf(cart_leng);
                            allbuf11 = new byte[][]{
                                    left, setHT321, totalqu.getBytes(), LF
                            };

                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                BluetoothPrintDriver.BT_Write(left);    //
                                BluetoothPrintDriver.BT_Write(setHT321);    //
                                BT_Write(totalqu);
                                BluetoothPrintDriver.BT_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write(setHT321);    //
                                    wifiSocket2.WIFI_Write(totalqu);
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(left);    //
                                        wifiSocket.WIFI_Write(setHT321);    //
                                        wifiSocket.WIFI_Write(totalqu);
                                        wifiSocket.WIFI_Write(LF);    //
                                    }
                                }
                            }

                            TextView t = new TextView(New_Individualorder_Itemview.this);
                            t.setText(strbillone);

                            if (t.getText().toString().equals("")){

                            }else {
                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                    BluetoothPrintDriver.BT_Write(cen);    //
                                    BluetoothPrintDriver.BT_Write(normal);    //
                                    BT_Write(strbillone);
                                    BluetoothPrintDriver.BT_Write(LF);    //
                                } else {
                                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                        wifiSocket2.WIFI_Write(cen);    //
                                        wifiSocket2.WIFI_Write(normal);    //
                                        wifiSocket2.WIFI_Write(strbillone);
                                        wifiSocket2.WIFI_Write(LF);    //
                                    } else {
                                        if (textViewstatusnets.getText().toString().equals("ok")) {
                                            wifiSocket.WIFI_Write(cen);    //
                                            wifiSocket.WIFI_Write(normal);    //
                                            wifiSocket.WIFI_Write(strbillone);
                                            wifiSocket.WIFI_Write(LF);    //
                                        }
                                    }
                                }
                            }

                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                BluetoothPrintDriver.BT_Write(feedcut2);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(feedcut2);    //
                                } else {
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(feedcut2);    //
                                    }
                                }
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
        mQueue.add(jsonObjectRequest);



    }


}
