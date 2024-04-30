package com.intuition.ivepos;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.intuition.ivepos.syncapp.StubProviderApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

public class New_OrderActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemListener {

    RecyclerView recyclerView;
    ArrayList<DataModel> arrayList;

    String url_pending = "https://api.werafoods.com/pos/v2/merchant/pendingorders";
    final String url_accept = "https://api.werafoods.com/pos/v2/order/accept";
    final String url_reject = "https://api.werafoods.com/pos/v2/order/reject";
    final String url_fodready = "https://api.werafoods.com/pos/v2/order/food-ready";
    final String url_delivered = "https://api.werafoods.com/pos/v2/order/delivered";
    String url_orders = "https://api.werafoods.com/pos/v2/merchant/orders";
    String url_menuupload = "https://api.werafoods.com/pos/v2/menu/upload";

    JSONObject jsonBody;
    //    ListView listView;
    ArrayList<HashMap<String, String>> contactList;

    int i_ord;

    SQLiteDatabase db = null;
    ArrayAdapter<Country_out_of_stock> adapter;
    ArrayList<Country_out_of_stock> list = new ArrayList<Country_out_of_stock>();

    Uri contentUri,resultUri;

    JSONObject json2;

    String str_merch_id;

    public SQLiteDatabase db_inapp = null;
    int countlimit;
    int count = 0;

    String WebserviceUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_delivery_neworders_ui2);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(New_OrderActivity.this);
        String account_selection= sharedpreferences_select.getString("account_selection", null);

        if (account_selection.toString().equals("Dine")) {
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }else {
            if (account_selection.toString().equals("Qsr")) {
                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
            }else {
                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
            }
        }

        db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);

        Toast.makeText(New_OrderActivity.this, "order received", Toast.LENGTH_LONG).show();

        db = openOrCreateDatabase("mydb_Appda" +
                "ta", Context.MODE_PRIVATE, null);

        Cursor cursor_merchant_id = db.rawQuery("SELECT * FROM Restaurant_id", null);
        if (cursor_merchant_id.moveToFirst()){
            str_merch_id = cursor_merchant_id.getString(1);
        }
        cursor_merchant_id.close();

        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        arrayList = new ArrayList<>();
//        arrayList.add(new DataModel("Item 1"));
//        arrayList.add(new DataModel("Item 2"));
//        arrayList.add(new DataModel("Item 3"));
//        arrayList.add(new DataModel("Item 4"));
//        arrayList.add(new DataModel("Item 5"));
//        arrayList.add(new DataModel("Item 6"));

//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, arrayList, this);
//        recyclerView.setAdapter(adapter);


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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url_pending, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                                    JSONObject jsonObj = new JSONObject(response.toString());

                            // Getting JSON Array node
                            JSONObject details = response.getJSONObject("details");
                            JSONArray orders = details.getJSONArray("orders");
                            System.out.println("orders are "+orders);
                            ArrayList<String> my_array = new ArrayList<String>();
                            // looping through All Contacts
                            for (int i = 0; i < orders.length(); i++) {
                                JSONObject c = orders.getJSONObject(i);

                                String order_info_id = c.getJSONObject("order_info").getString("order_id");
                                String order_type1 = c.getJSONObject("order_info").getString("order_type");
                                String date_time = c.getJSONObject("order_info").getString("transaction_date");
                                String payme_type = c.getJSONObject("order_info").getString("payment_type");

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
                                    System.out.println("orderid: "+order_info_id+"  name: "+order_type1);//

//                                    my_array.add(name);
//                                    contact.put("itemname", String.valueOf(name));
//                                    contact.put("qty", qty);
//                                    contact.put("date_time", date_time);
//                                    contact.put("order_type", order_type);

//                                    Country_Ingredient NAME = new Country_Ingredient(order_info_id, order_type, date_time, payme_type, cust_name, cust_phno, cust_street, cust_location, cust_deli_instru);
//                                    list.add(NAME);

                                    arrayList.add(new DataModel(order_info_id, String.valueOf(cart.length()), totalprice1, order_type1, cust_name1, time1, "", "", "", str_merch_id));

                                }


//                                String[] mStringArray = new String[my_array.size()];
//                                mStringArray = my_array.toArray(mStringArray);
//
//                                for(int i1 = 0; i1 < mStringArray.length ; i1++){
//                                    Log.d("string is",(String)mStringArray[i1]);
//                                    contact.put("itemname", (String)mStringArray[i1]);
////                                    contactList.add(contact);
//                                }

//                                contactList.add(contact);
//
//                                ((SimpleAdapter) adapter).notifyDataSetChanged();




                            }

                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(New_OrderActivity.this, arrayList, New_OrderActivity.this);
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

        /**
         AutoFitGridLayoutManager that auto fits the cells by the column width defined.
         **/

//        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 500);
//        recyclerView.setLayoutManager(layoutManager);


        Spinner all_s_z_ue_fp = (Spinner) findViewById(R.id.all_s_z_ue_fp);
        final String Text = all_s_z_ue_fp.getSelectedItem().toString();

        if (Text.equals("All")) {
            Toast.makeText(New_OrderActivity.this, "ALL", Toast.LENGTH_LONG).show();
        }


        all_s_z_ue_fp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text1 = parent.getItemAtPosition(position).toString();
                if (text1.toString().equals("All")) {
                    Toast.makeText(New_OrderActivity.this, "Selected All", Toast.LENGTH_LONG).show();
                    all_new_orders();
                }else {
                    if (text1.toString().equals("Swiggy")) {
                        Toast.makeText(New_OrderActivity.this, "Swiggy", Toast.LENGTH_LONG).show();
                        custom_new_orders("Swiggy");
                    }else {
                        if (text1.toString().equals("Zomato")) {
                            Toast.makeText(New_OrderActivity.this, "Zomato", Toast.LENGTH_LONG).show();
                            custom_new_orders("Zomato");
                        }else {
                            if (text1.toString().equals("Uber eats")) {
                                Toast.makeText(New_OrderActivity.this, "Uber eats", Toast.LENGTH_LONG).show();
                                custom_new_orders("Uber eats");
                            }else {
                                if (text1.toString().equals("Food panda")) {
                                    Toast.makeText(New_OrderActivity.this, "Food panda", Toast.LENGTH_LONG).show();
                                    custom_new_orders("Food panda");
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


        LinearLayout preparing_orders = (LinearLayout) findViewById(R.id.preparing_orders);
        preparing_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(New_OrderActivity.this, Preparing_Orders.class);
                startActivity(intent);
            }
        });


        final TextView preparing_orders_value = (TextView) findViewById(R.id.preparing_orders_value);

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

                                String status = c.getJSONObject("order_info").getString("status");
//                                i_ord = 0;
//                                if (status.toString().equals("Accepted") || status.toString().equals("Picked Up") || status.toString().equals("Food Ready")) {
//                                    i_ord++;
//                                    preparing_orders_value.setText(String.valueOf(i_ord));
//                                }
                                if (status.toString().equals("Accepted")) {
                                    i_ord++;
                                    preparing_orders_value.setText(String.valueOf(i_ord));
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
        mQueue1.add(jsonObjectRequest1);


        LinearLayout out_of_stock_items = (LinearLayout) findViewById(R.id.out_of_stock_items);
        out_of_stock_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




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

                            final Dialog dialog = new Dialog(New_OrderActivity.this, R.style.timepicker_date_dialog);
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
                            ////Toast.makeText(New_OrderActivity.this, "limit is a " + limit, Toast.LENGTH_SHORT).show();
                            if (aallrows.moveToFirst()) {
                                do {
                                    String ID = aallrows.getString(0);
                                    String NAme = aallrows.getString(1);
                                    String BAr = aallrows.getString(16);
                                    String out_of_stock = aallrows.getString(51);

                                    Country_out_of_stock NAME = new Country_out_of_stock(NAme, out_of_stock);
                                    list.add(NAME);

                                } while (aallrows.moveToNext());
                            }
                            aallrows.close();

                            adapter = new MyAdapter_out_of_stock(New_OrderActivity.this,list);
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

                                Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE itemname = '"+cb.getText().toString()+"' AND out_of_stock = ''", null);
                                if (cursor.moveToFirst()) {
                                    String tax_na = cursor.getString(1);

                                    if (cb.getText().toString().equals(tax_na)) {
                                        cb1.setChecked(true);
                                    }
                                }
                                cursor.close();
                            }

                            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long l) {

                                    TextView txtview = (TextView) view.findViewById(R.id.label);
                                    final String item = txtview.getText().toString();

                                    final CheckBox checkbox = (CheckBox) view.getTag(R.id.check);

                                    if (checkbox.isChecked()){
                                        checkbox.setChecked(false);

                                        Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE itemname = '"+item+"'", null);
                                        if (cursor1.moveToFirst()){
                                            String id = cursor1.getString(0);
                                            String pri = cursor1.getString(2);
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("out_of_stock", "yes");
                                            String where1 = "_id = '"+id+"' ";


                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                            getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                                            resultUri = new Uri.Builder()
                                                    .scheme("content")
                                                    .authority(StubProviderApp.AUTHORITY)
                                                    .path("Items")
                                                    .appendQueryParameter("operation", "update")
                                                    .appendQueryParameter("_id", id)
                                                    .build();
                                            getContentResolver().notifyChange(resultUri, null);


                                            //    db.update("Items", contentValues, where1, new String[]{});


                                            func_out_of_stock(item, pri, "false");


                                        }
                                        cursor1.close();
                                    }else {
                                        checkbox.setChecked(true);
                                        Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE itemname = '"+item+"'", null);
                                        if (cursor1.moveToFirst()){
                                            String id = cursor1.getString(0);
                                            String pri = cursor1.getString(2);
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("out_of_stock", "");
                                            String where1 = "_id = '"+id+"' ";



                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                            getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                                            resultUri = new Uri.Builder()
                                                    .scheme("content")
                                                    .authority(StubProviderApp.AUTHORITY)
                                                    .path("Items")
                                                    .appendQueryParameter("operation", "update")
                                                    .appendQueryParameter("_id", id)
                                                    .build();
                                            getContentResolver().notifyChange(resultUri, null);

                                            //       db.update("Items", contentValues, where1, new String[]{});

                                            func_out_of_stock(item, pri, "true");


                                        }
                                        cursor1.close();
                                    }

                                    adapter.notifyDataSetChanged();
                                }
                            });
                            adapter.notifyDataSetChanged();

                        }
                        if(id==R.id.linearLayout_menu_upload){
                            Toast.makeText(getApplicationContext(), "menu upload", Toast.LENGTH_SHORT).show();

                            try {

                                JSONArray jsonArray = new JSONArray();
                                Cursor cursor = db.rawQuery("SELECT * FROM items", null);
                                if (cursor.moveToFirst()){
                                    do {
                                        String id1 = cursor.getString(0);
                                        String itemname = cursor.getString(1);
                                        String price = cursor.getString(2);
                                        JSONObject json1 = new JSONObject();
                                        json1.put("id", id1);
                                        json1.put("item_name", itemname);
                                        json1.put("price", price);
                                        json1.put("active", "true");
                                        jsonArray.put(json1);
                                    }while (cursor.moveToNext());
                                }



                                json2= new JSONObject();
                                json2.put("merchant_id",str_merch_id);
                                json2.put("menu",jsonArray);

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
                                            Toast.makeText(New_OrderActivity.this,"menu upload", Toast.LENGTH_LONG).show();
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
                        return true;
                    }
                });
            }
        });

    }


    public void custom_new_orders(final String ord_type){
        arrayList = new ArrayList<>();
        try {
            //I try to use this for send Header is application/json
            jsonBody = new JSONObject();
            jsonBody.put("merchant_id", str_merch_id);
//            jsonBody.put("order_id", "2016204");
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
                                String order_type1 = c.getJSONObject("order_info").getString("order_type");
                                if (order_type1.toString().equals(ord_type)) {
                                    String date_time = c.getJSONObject("order_info").getString("transaction_date");
                                    String payme_type = c.getJSONObject("order_info").getString("payment_type");

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

                                        arrayList.add(new DataModel(order_info_id, String.valueOf(cart.length()), totalprice1, order_type1, cust_name1, time1, "", "", "", str_merch_id));

                                    }
                                }
                            }

                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(New_OrderActivity.this, arrayList, New_OrderActivity.this);
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


    public void all_new_orders(){
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
                                String order_type1 = c.getJSONObject("order_info").getString("order_type");
                                String date_time = c.getJSONObject("order_info").getString("transaction_date");
                                String payme_type = c.getJSONObject("order_info").getString("payment_type");

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

                                    arrayList.add(new DataModel(order_info_id, String.valueOf(cart.length()), totalprice1, order_type1, cust_name1, time1, "", "", "", str_merch_id));

                                }
                            }

                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(New_OrderActivity.this, arrayList, New_OrderActivity.this);
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


    public void all_new_orders_v(){
////////////////////////////////////////////////////////////

        i_ord = 0;
        final TextView preparing_orders_value = (TextView) findViewById(R.id.preparing_orders_value);

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

                                String status = c.getJSONObject("order_info").getString("status");
//                                i_ord = 0;
//                                if (status.toString().equals("Accepted") || status.toString().equals("Picked Up") || status.toString().equals("Food Ready")) {
//                                    i_ord++;
//                                    preparing_orders_value.setText(String.valueOf(i_ord));
//                                }
                                if (status.toString().equals("Accepted")) {
                                    i_ord++;
                                    preparing_orders_value.setText(String.valueOf(i_ord));
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
        mQueue1.add(jsonObjectRequest1);
    }


    @Override
    public void onItemClick(DataModel item) {

        Toast.makeText(getApplicationContext(), item.text + " is clicked", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(New_OrderActivity.this, New_Individualorder_Itemview.class);
        intent.putExtra("order_id", item.text);
        intent.putExtra("total_price", item.totalprice);
        intent.putExtra("order_type", item.order_type);
        startActivity(intent);

    }

    public void func_out_of_stock(final String item, String price, final String status){

        Toast.makeText(getApplicationContext(), ""+item+""+price+""+status, Toast.LENGTH_SHORT).show();

        try {

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
        }

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url_menuupload, json2,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                        Toast.makeText(New_OrderActivity.this,item+" "+status, Toast.LENGTH_LONG).show();
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



    public void remaining_orders1(final String bn){


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




        SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        final String company= sharedpreferences.getString("companyname", null);
        final String store= sharedpreferences.getString("storename", null);
        final String device= sharedpreferences.getString("devicename", null);

//        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
//        final String regId = pref.getString("regId", null);
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        final int finalBn = bn1;
        StringRequest sr = new StringRequest(
                Request.Method.POST,
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

}
