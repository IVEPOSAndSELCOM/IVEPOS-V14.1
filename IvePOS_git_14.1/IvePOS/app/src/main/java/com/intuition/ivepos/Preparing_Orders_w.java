package com.intuition.ivepos;

import static com.android.volley.Request.Method.POST;
import static com.intuition.ivepos.BluetoothPrintDriver.BT_Write;
import static com.intuition.ivepos.Cash_Card_Credit_Sales.printByteData;
import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.epson.epos2.printer.Printer;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.gmail.GmailScopes;
import com.intuition.ivepos.A4.A4_Printer_new;
import com.intuition.ivepos.mSwipe.ApplicationData;
import com.intuition.ivepos.mSwipe.Logs;
import com.intuition.ivepos.sync.StubProvider;
import com.intuition.ivepos.wisepos.ReceiptBitmap;
import com.mswipetech.wisepad.sdk.Print;
import com.mswipetech.wisepad.sdk.device.MSWisepadDeviceController;
import com.socsi.smartposapi.printer.Align;
import com.socsi.smartposapi.printer.FontLattice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Preparing_Orders_w extends AppCompatActivity implements RecyclerViewAdapter_preparing_w.ItemListener {

    RecyclerView recyclerView;
    RelativeLayout progressBar;
    String billnum;
    String printbillno;
    String billno, date, time, user, date11, date111;
    ListView listView, listView1, listviewdis, listView2;
    SimpleCursorAdapter adapter, dataAdapterr;

    float dsirsq1, dsirsq2, dsirsq3, dsirsq4;
    String dis_val, dis_status, dis_ty;
    String ropq;

    String Itemtype, itemtotal, id5, total1quan;

    String strpaymentmethod, strbilltype, max, iittnn, iittnnquan, iittnntable, iittnnindprice, iittnnindtotalprice, bill_coun, iittnninddate, cardnum;

    int level;
    String total, total1, discount;

    Cursor cursor1;
    Spinner getlisting;

    EditText editText1_filter, editText2_filter;


    String billnumb, datee, timee;
    String ipnamegets, portgets, statusnets, addgets, namegets, statussusbs;
    String ipnamegets_counter, portgets_counter, statusnets_counter;


    String ipnameget, portget, statusnet, nameget, addget, statussusb;
    String ipnameget_counter, portget_counter, statusnet_counter;
    String assa, assa1, assa2;

    TextView tv8, disc_tv;
    float ss;
    String ss1;

    File file=null, file1=null;
    SimpleDateFormat sdff2, sdff1;
    String currentDateandTimee1;
    String timee1;
    private View mView;
    String sub, compna, companynameis;
    ImageView imageViewPicture;
    byte[] img;


    private int hour;
    private int minute; TextView mTextView1, mTextView2;

    byte[][] allbuf, allbuf1, allbuf2, allbuf3, allbuf4, allbuf5, allbuf6, allbuf7, allbuf8, allbuf9, allbuf10, allbuf11, allbufqty, allbufitems, allbufmodifiers, allbufsubtot,
            allbuftax, allbufdisc, allbufrounded, allbuffulltot, allbuf12, allbuf13, allbuf14,allbufbillno,allbuftime,allbufline1,allbufline,allbufcust,allbufcustname,
            allbufcustadd,allbufcustph,allbufcustemail, allbuftaxestype2, allbuftaxestype1, allbuf1122, allbufKOT;
    String strcompanyname, straddress1, straddress2, straddress3, strphone, stremailid, strwebsite, strtaxone, strbillone;

    byte[] setHT32, setHT321, setHT33, setHT34, setHT3212, setHT32122, setHTKOT, feedcut2;
    int nPaperWidth;
    int charlength, charlength1, charlength2, quanlentha;
    String NAME, tableidaa, tableida;
    String paymmethodaa, paymmethoda, billtypeaa, billtypea;
    TextView tvkot;
    EditText search;

    String total_disc_print_q;
    String taxpe, dsirs, subro, alltotal1;

    TextView totalsales, totalsales_r, noofbills, avgsales, avgsales_r;

    String response;
    GoogleAccountCredential mCredential;
    ProgressDialog mProgress;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Gmail API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { GmailScopes.GMAIL_SEND };


    EditText email_id_send;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    TextView editText1, editText2, editText11, editText22;
    TextView editText_from_day_hide, editText_from_day_visible, editText_to_day_hide, editText_to_day_visible;
    private int year, year1;
    private int month, month1;
    private int day, day1;

    String onee, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve;
    String onee1, two1, three1, four1, five1, six1, seven1, eight1, nine1, ten1, eleven1, twelve1;

    int clickcount=1, clickcounts = 1;

    List toEmailList;
    int dineinsales, takeawaysales, homedeliverysales, refunded;
    String dineinsalesstr, takeawaysalesstr, homedeliverysalesstr, refundedstr, rupper1;
    String total11, total111, total111card, totalbillis;
    String oone1, ppone, mmax, ppercen;

    String str_print_ty;

    private Context mContext = null;

    private Printer mPrinter = null;
    int barcodeWidth, barcodeHeight, pageAreaHeight, pageAreaWidth;

    private EditText mEditTarget = null;
    private Spinner mSpnSeries = null;
    private Spinner mSpnLang = null;
    Bitmap logoData, yourBitmap;

    private WifiPrintDriver wifiSocket = null;
    private WifiPrintDriver2 wifiSocket2 = null;

    String insert1_cc = "", insert1_rs = "", str_country;

    Uri contentUri,resultUri;
    String u_name;
    String account_selection;

    public MSWisepadDeviceController mMSWisepadDeviceController = null;
    private ArrayList<byte[]> mPrintData;

    boolean mswipe_text = true;

    byte[] testBytes;
    byte[] command;
    private UsbManager mUsbManager;
    private UsbDevice mDevice;
    private UsbDeviceConnection mConnection;
    private UsbInterface mInterface;
    private UsbEndpoint mEndPoint;
    private static Boolean forceCLaim = true;
    HashMap<String, UsbDevice> mDeviceList;
    Iterator<UsbDevice> mDeviceIterator;

    Formatter fmt;

    ArrayList<DataModel_w> arrayList, riderdetails;
    String url_orders = "https://api.werafoods.com/pos/v2/merchant/orders";
    String url_pending = "https://api.werafoods.com/pos/v2/merchant/pendingorders";
    String url_getdelivery = "https://api.werafoods.com/pos/v2/order/getde";

    String updateorder = "http://theandroidpos.com/IVEPOS_NEW/wera/update_order_details.php";
    String weraorderdetails = "http://theandroidpos.com/IVEPOS_NEW/wera/order_details_wera.php";

    JSONObject jsonBody;

    private SQLiteDatabase db, db1;

    String dateget, dateget1, timeget;

    int i_ord;
    String username, u_username;
    String bill_c, bill_count_tag, bill_count_tag1 = "", bill_count_tag2 = "";



    RequestQueue requestQueue;
    String company="",store="",device="";
    String stat, options, stage;
    String deli_per_name, deli_per_phon, deli_per_status, ordstatus;
    String str_merch_id;//= "2021";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_delivery_preparing_ui1_w);

        recyclerView = (RecyclerView) findViewById(R.id.preparingrecycle);
        arrayList = new ArrayList<>();
        tvkot = new TextView(Preparing_Orders_w.this);
        totalsales_r = new TextView(Preparing_Orders_w.this);
        avgsales_r = new TextView(Preparing_Orders_w.this);


        db1 = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        db = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);

        Cursor cursor_merchant_id = db1.rawQuery("SELECT * FROM Restaurant_id", null);
        if (cursor_merchant_id.moveToFirst()) {
            str_merch_id = cursor_merchant_id.getString(1);
        }
        cursor_merchant_id.close();

        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        company = sharedpreferences.getString("companyname", null);
        store = sharedpreferences.getString("storename", null);
        device = sharedpreferences.getString("devicename", null);

        LinearLayout back_activity = (LinearLayout) findViewById(R.id.pback_activity);
        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Preparing_Orders_w.this, New_OrderActivity_w.class);
                finish();
                startActivity(intent);

            }
        });
        LinearLayout cancelorderlayout = (LinearLayout) findViewById(R.id.cancelorderlayout);
        cancelorderlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Preparing_Orders_w.this, Cancelledorders_w.class);
                finish();
                startActivity(intent);

            }
        });

        LinearLayout refreshlayout = (LinearLayout) findViewById(R.id.refreshlay);
        refreshlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });


        cancelcount();
        new_orders();
        //new weraDB_update();
        preparingorders();


        final Spinner order_options = (Spinner) findViewById(R.id.pre_deli_pu_oth);
        options = order_options.getSelectedItem().toString();

        final Spinner order_stage = (Spinner) findViewById(R.id.all_s_z_ue_fp);
        stage = order_stage.getSelectedItem().toString();

        if (options.equals("All")) {
            // Toast.makeText(Preparing_Orders_w.this, "ALL", Toast.LENGTH_LONG).show();
        }


        order_options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stage = order_stage.getSelectedItem().toString();
                String text1 = parent.getItemAtPosition(position).toString();

                System.out.println("Order options " + text1+ " "+stage);
                switch (text1) {
                    case "All": { //Toast.makeText(Preparing_Orders_w.this, "Selected All", Toast.LENGTH_LONG).show();
                        all_new_orders(stage);
                        break;}
                    case "Online": {//Toast.makeText(Preparing_Orders_w.this, "Swiggy", Toast.LENGTH_LONG).show();
                        custom_new_orders("online", stage);
                        break;}
                    case "Swiggy": {//Toast.makeText(Preparing_Orders_w.this, "Swiggy", Toast.LENGTH_LONG).show();
                        custom_new_orders("swiggy", stage);
                        break;}
                    case "Zomato": {//Toast.makeText(Preparing_Orders_w.this, "Zomato", Toast.LENGTH_LONG).show();
                        custom_new_orders("zomato", stage);
                        break;}
                    /*case "Uber eats": {//Toast.makeText(Preparing_Orders_w.this, "Uber eats", Toast.LENGTH_LONG).show();
                        custom_new_orders("uber", stage);
                        break;}
                    case "Food panda": {//Toast.makeText(Preparing_Orders_w.this, "Food panda", Toast.LENGTH_LONG).show();
                        custom_new_orders("food panda", stage);
                        break;}*/
                    //default: {Toast.makeText(Preparing_Orders_w.this, "Delivery options error", Toast.LENGTH_LONG).show();}


                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        order_stage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                options = order_options.getSelectedItem().toString();
                String text1 = parent.getItemAtPosition(position).toString();

                System.out.println("Order stage " + text1 + " "+options);

                switch (text1){
                    case "Preparing" : {stat = "Accepted";
                        //Toast.makeText(Preparing_Orders_w.this, "Accepted", Toast.LENGTH_LONG).show();
                        custom_new_orders(options, "Accepted");
                        break;}
                    case "Food Ready" : {stat = "Food Ready";
                        //Toast.makeText(Preparing_Orders_w.this, "Food Ready", Toast.LENGTH_LONG).show();
                        custom_new_orders(options, "Food Ready");
                        break;}
                    case "Picked up" : {stat = "Picked Up";
                        //Toast.makeText(Preparing_Orders_w.this, "Picked Up", Toast.LENGTH_LONG).show();
                        custom_new_orders(options, "Picked Up");
                        break;}
                    case "Delivered" : {stat = "Delivered";
                        //Toast.makeText(Preparing_Orders_w.this, "Delivered", Toast.LENGTH_LONG).show();
                        custom_new_orders(options, "Delivered");
                        break;}
                    /*case "Others" : {stat = "others";
                        //Toast.makeText(Preparing_Orders_w.this, "Others", Toast.LENGTH_LONG).show();
                        custom_new_orders(options, "Others");
                        break;}*/
                    //default: {Toast.makeText(Preparing_Orders_w.this, "Status error", Toast.LENGTH_LONG).show();}
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
                finish();
                Intent intent = new Intent(Preparing_Orders_w.this, New_OrderActivity_w.class);
                startActivity(intent);

            }
        });


    }
    public void preparingorders() {
        RequestQueue mQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest strord= new StringRequest(POST, weraorderdetails,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response1) {
                        try {

                            JSONArray orddetails = new JSONArray(response1);
                            //String orddetails = String.valueOf(response1);
                            DataModel_w id = null, dbdata=null;
                            ArrayList arrayList= new ArrayList<>();
                            for (int k = 0; k < orddetails.length(); k++) {
                                JSONObject c = orddetails.getJSONObject(k);
                                //System.out.println("inloop inner individual response: " + c);
                                String orderid = c.getString("_id");
                                // getdelivery(orderid);
                                String order_from = c.getString("order_from");
                                String order_type = c.getString("order_type");
                                String status = c.getString("order_status");
                                String order_reason = c.getString("order_reason");
                                /*if(order_reason.equalsIgnoreCase("Auto accepted by werafoods") && status.equalsIgnoreCase("Pending")){
                                    status="Accepted";
                                }*/


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

                                if (status.toString().equalsIgnoreCase("Accepted")) {
//                                    saveinDB(orderid);
                                    printkot();
                                    id= new DataModel_w(orderid, no_items, gross_amount, order_from, cust_name1, cust_phno, cust_address, cust_deli_instru,cust_deliv_area,cust_email, status, order_cancelreason, deli_per_name, deli_per_phon, deli_per_status,cart,order_instructions, str_merch_id, deli_arrivetime);
                                    arrayList.add(id);


                                    //if(order_reason.equalsIgnoreCase("Auto accepted by werafoods")) {

                                    ArrayList orddatadb = new ArrayList<>();
                                    dbdata = new DataModel_w(orderid, order_type, date_time, payme_type, restaurant_id, restaurant_name, external_order_id, restaurant_address, restaurant_number, order_from, enable_delivery, net_amount, gross_amount,
                                            order_instructions, order_gst, order_cgst, order_sgst, packaging, order_packaging, packaging_cgst_percent, packaging_sgst_percent, packaging_cgst, packaging_sgst,
                                            order_pckg_cgst, order_pckg_sgst, order_pckg_cgst_per, order_pckg_sgst_per, delivery_charge, discount_type, discount_amount, cust_name1, cust_phno, cust_email, cust_address, cust_deliv_area, cust_deli_instru, String.valueOf(cart.length()), status, cart);
                                    orddatadb.add(dbdata);
                                    //weraindextable(orderid, status, restaurant_id);
                                    //createordersalesdata(dbdata);
                                    //addorderdatadb(dbdata);
                                    // updateweralocaltable(orderid,status,no_items);
                                    //updateweraindextable(orderid,status,no_items);
                                    //  }
                                }

                            }
                            RecyclerViewAdapter_preparing_w adapter = new RecyclerViewAdapter_preparing_w(Preparing_Orders_w.this, arrayList, Preparing_Orders_w.this);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Delivery Agent details", "Error: " + error.getMessage());
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
                //windex.put("order_status", status);
                return windex;
            }
        };
        mQueue1.add(strord);
    }

    private void printkot() {

        /*printimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                mHandler1 = new MHandler1(BeveragesMenuFragment_Dine_l.this);
//                DrawerService1.addHandler(mHandler1);

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

                    if (statusnet.toString().equals("ok")) {
                        wifiSocket.WIFISocket(ipnameget, 9100);
                    }
                    if (statusnets_kot1.toString().equals("ok")) {
                        wifiSocket_kot1.WIFISocket(ipnamegets_kot1, 9100);
                    }
                    if (statusnets_kot2.toString().equals("ok")) {
                        wifiSocket_kot2.WIFISocket(ipnamegets_kot2, 9100);
                    }
                    if (statusnets_kot3.toString().equals("ok")) {
                        wifiSocket_kot3.WIFISocket(ipnamegets_kot3, 9100);
                    }
                    if (statusnets_kot4.toString().equals("ok")) {
                        wifiSocket_kot4.WIFISocket(ipnamegets_kot4, 9100);
                    }

                    DownloadMusicfromInternet2 downloadMusicfromInternet2 = new DownloadMusicfromInternet2();
                    downloadMusicfromInternet2.execute();

                } else {

                    String printer_type="";
                    Cursor aallrows = db.rawQuery("SELECT * FROM Printer_type WHERE _id = '1'", null);
                    if (aallrows.moveToFirst()) {
                        do {
                            printer_type = aallrows.getString(1);

                        } while (aallrows.moveToNext());
                    }
                    aallrows.close();

                    if(printer_type.equalsIgnoreCase("wiseposplus")){

                        if (statusnet.toString().equals("ok")) {
                            wifiSocket.WIFISocket(ipnameget, 9100);
                        }
                        if (statusnets_kot1.toString().equals("ok")) {
                            wifiSocket_kot1.WIFISocket(ipnamegets_kot1, 9100);
                        }
                        if (statusnets_kot2.toString().equals("ok")) {
                            wifiSocket_kot2.WIFISocket(ipnamegets_kot2, 9100);
                        }
                        if (statusnets_kot3.toString().equals("ok")) {
                            wifiSocket_kot3.WIFISocket(ipnamegets_kot3, 9100);
                        }
                        if (statusnets_kot4.toString().equals("ok")) {
                            wifiSocket_kot4.WIFISocket(ipnamegets_kot4, 9100);
                        }

                        DownloadMusicfromInternet2 downloadMusicfromInternet2 = new DownloadMusicfromInternet2();
                        downloadMusicfromInternet2.execute();

                    }else {
                        final Dialog dialogconn = new Dialog(BeveragesMenuFragment_Dine_l.this, R.style.notitle);
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
                }
            }
        });*/
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Preparing_Orders_w.this, New_OrderActivity_w.class);
        finish();
        startActivity(intent);

    }
    public void custom_new_orders(final String ord_type, final String sta){

        if (sta.toString().equalsIgnoreCase("Preparing") || ord_type.toString().equalsIgnoreCase("Accepted") || sta.toString().equalsIgnoreCase("Accepted")){
            stat = "Accepted";
        }
        if (sta.toString().equalsIgnoreCase("Food Ready") || ord_type.toString().equalsIgnoreCase("Food Ready")) {
            stat = "Food Ready";
        }
        if (sta.toString().equalsIgnoreCase("picked up") || ord_type.toString().equalsIgnoreCase("Picked Up")){
            stat = "Picked Up";
        }
        if (sta.toString().equalsIgnoreCase("delivered") || ord_type.toString().equalsIgnoreCase("Delivered")){
            stat = "Delivered";
        }

        final ArrayList arrayList = new ArrayList<>();
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.preparingrecycle);

        RequestQueue mQueue1 = Volley.newRequestQueue(getApplicationContext());
        //String finalOrderid = order_info_id;
        final String finalStat = stat;
        final String finaltype = ord_type;

        StringRequest strord= new StringRequest(POST, weraorderdetails,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response1) {
                        try {
                            JSONArray orddetails = new JSONArray(response1);
                            //String orddetails = String.valueOf(response1);
                            DataModel_w id = null;
                            ArrayList arrayList= new ArrayList<>();
                            System.out.println("Selected type " + ord_type + " " + response1);
                            for (int k = 0; k < orddetails.length(); k++) {
                                JSONObject c = orddetails.getJSONObject(k);
                                //System.out.println("inloop inner individual response: " + c);
                                String orderid = c.getString("_id");
                                // getdelivery(orderid);
                                String order_from = c.getString("order_from");

                                if (ord_type.toString().equalsIgnoreCase("All")){
                                    String status = c.getString("order_status");
                                    if(status.equalsIgnoreCase("auto-accept")){
                                        status="Accepted";
                                    }
                                    System.out.println("All is selected " +ord_type.toString());

                                    if (status.toString().equalsIgnoreCase(finalStat)) {

                                        //Toast.makeText(Preparing_Orders_w.this, "" + ord_type + " " + finalStat, Toast.LENGTH_LONG).show();
                                        System.out.println("selected id is " + ord_type + " " + finalStat);
                                        String no_items = c.getString("num_item");
                                        String totalprice = c.getString("gross_amount");
                                        //String order_from = c.getString("order_from");
                                        String cust_name = c.getString("cus_name");
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
                                        String order_instructions = c.getString("order_instructions");
                                        JSONArray cart = c.getJSONArray("orderItemsJSON");


                                        id= new DataModel_w(orderid, no_items, totalprice, order_from, cust_name1, cust_phno, cust_address, cust_deli_instru,cust_deliv_area,cust_email, status, order_cancelreason, deli_per_name, deli_per_phon, deli_per_status,cart,order_instructions, str_merch_id, deli_arrivetime);
                                        arrayList.add(id);
                                    }
                                }else if (order_from.toString().equalsIgnoreCase(ord_type)) {
                                    System.out.println("not All is selected " +ord_type.toString());
                                    String status = c.getString("order_status");
                                    if(status.equalsIgnoreCase("auto-accept")){
                                        status="Accepted";
                                    }
                                    if (status.toString().equalsIgnoreCase(finalStat)) {

                                        //Toast.makeText(Preparing_Orders_w.this, "" + ord_type + " " + finalStat, Toast.LENGTH_LONG).show();
                                        System.out.println("selected id is not all " + ord_type + " " + finalStat);

                                        String no_items = c.getString("num_item");
                                        String totalprice = c.getString("gross_amount");
                                        //String order_from = c.getString("order_from");
                                        String cust_name = c.getString("cus_name");
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
                                        String order_instructions = c.getString("order_instructions");
                                        JSONArray cart = c.getJSONArray("orderItemsJSON");


                                        id= new DataModel_w(orderid, no_items, totalprice, order_from, cust_name1, cust_phno, cust_address, cust_deli_instru,cust_deliv_area,cust_email, status, order_cancelreason, deli_per_name, deli_per_phon, deli_per_status,cart,order_instructions, str_merch_id, deli_arrivetime);
                                        arrayList.add(id);
                                    }
                                }
                                //}

                                RecyclerViewAdapter_preparing_w adapter = new RecyclerViewAdapter_preparing_w(Preparing_Orders_w.this, arrayList, Preparing_Orders_w.this);
                                recyclerView.setAdapter(adapter);

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Delivery Agent details", "Error: " + error.getMessage());
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
                windex.put("column_name", "order_status");
                windex.put("column_value", stat);
                //windex.put("order_id", order_info_id);
                //windex.put("order_status", finalStat);
                return windex;
            }
        };
        mQueue1.add(strord);

    }
    public void all_new_orders(final String sta){

        if (sta.toString().equalsIgnoreCase("Preparing") || sta.toString().equals("Accepted")){
            stat = "Accepted";
        }
        if (sta.toString().equalsIgnoreCase("Food Ready")) {
            stat = "Food Ready";
        }
        if (sta.toString().equalsIgnoreCase("picked up")){
            stat = "Picked Up";
        }
        if (sta.toString().equalsIgnoreCase("delivered")){
            stat = "Delivered";
        }

        final ArrayList arrayList = new ArrayList<>();
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.preparingrecycle);
        //final String finalStat = stat;

        RequestQueue mQueue1 = Volley.newRequestQueue(getApplicationContext());
        //String finalOrderid = order_info_id;
        final String finalStat = stat;
        ArrayList riderdetails= new ArrayList<>();
        StringRequest strord= new StringRequest(POST, weraorderdetails, new Response.Listener<String>() {
            @Override
            public void onResponse(String response1) {
                JSONArray orddetails = null;
                try {
                    orddetails = new JSONArray(response1);
                    DataModel_w ide = null;
                    ArrayList arrayList= new ArrayList<>();
                    System.out.println("All option " + finalStat+ " " + response1);
                    //  for (int i = 0; i < orddetails.length(); i++) {
                    //System.out.println("inloop inner response: " + response1 + "  " + orddetails.length());// + "  Agent Response: " + deli_per_name + "  " + deli_per_phon + "  " + deli_per_status+"  "+finaldeli_per_name[0]);
                    //String orderid = null, no_items=null, totalprice=null, order_from=null, cust_name=null, order_cancelreason=null;
                    for (int k = 0; k < orddetails.length(); k++) {

                        JSONObject c = orddetails.getJSONObject(k);
                        //System.out.println("inloop inner individual response: " + c);
                        String orderid = c.getString("_id");
                        //  getdelivery(orderid);
                        String order_from = c.getString("order_from");
                        String status = c.getString("order_status");
                        if(status.equalsIgnoreCase("auto-accept")){
                            status="Accepted";
                        }


                        if (status.toString().equalsIgnoreCase(finalStat)) {

                            String no_items = c.getString("num_item");
                            String totalprice = c.getString("gross_amount");
                            String order_instructions = c.getString("order_instructions");
                            //String order_from = c.getString("order_from");
                            String cust_name = c.getString("cus_name");
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
                            JSONArray cart = c.getJSONArray("orderItemsJSON");


                            ide= new DataModel_w(orderid, no_items, totalprice, order_from, cust_name1, cust_phno, cust_address, cust_deli_instru,cust_deliv_area,cust_email, status, order_cancelreason, deli_per_name, deli_per_phon, deli_per_status,cart,order_instructions, str_merch_id, deli_arrivetime);
                            arrayList.add(ide);
                        }
                    }

                    RecyclerViewAdapter_preparing_w adapter = new RecyclerViewAdapter_preparing_w(Preparing_Orders_w.this, arrayList, Preparing_Orders_w.this);
                    recyclerView.setAdapter(adapter);

                    //}
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Delivery Agent details", "Error: " + error.getMessage());
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
                windex.put("column_name", "order_status");
                windex.put("column_value", stat);
                //windex.put("order_id", order_info_id);
                //windex.put("order_status", finalStat);
                return windex;
            }
        };
        mQueue1.add(strord);






        /*try {
            //I try to use this for send Header is application/json
            jsonBody = new JSONObject();
            jsonBody.put("merchant_id", str_merch_id);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url_orders, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DataModel_w ide=null;
                        try {
                            // Getting JSON Array node
                            JSONObject details = response.getJSONObject("details");
                            JSONArray orders = details.getJSONArray("orders");
                            ArrayList<String> my_array = new ArrayList<String>();
                            // looping through All Contacts
                            for (int i = 0; i < orders.length(); i++) {
                                JSONObject c = orders.getJSONObject(i);

                                String order_info_id = c.getString("order_id");
                                String order_type1 = c.getString("order_type");
                                String date_time = c.getString("order_date_time");
                                String payme_type = c.getString("payment_mode");
                                String order_from = c.getString("order_from");
                                String status = c.getString("status");
                                if (status.toString().equals(finalStat)) {

                                    String deli_per_name = c.getJSONObject("rider").getString("is_rider_available");
                                    String deli_per_phon = "";

                                    if (deli_per_name.toString().equals("null")){
                                        System.out.println("data is not there");
                                        deli_per_name = "Not Assigned";
                                    }else {
                                        System.out.println("data is there");
                                        // deli_per_name = c.getJSONObject("order_info").getJSONObject("rider_info").getString("rider_name");
                                        // deli_per_phon = c.getJSONObject("order_info").getJSONObject("rider_info").getString("rider_phone_number");
                                    }
                                    String deliv_time1 = c.getString("expected_delivery_time");

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
                                        System.out.println("orderid: " + order_info_id + "  name: " + order_from);//

                                        ide=new DataModel_w(order_info_id, String.valueOf(cart.length()), gross_amount, order_from, cust_name1, status, "","","","", str_merch_id);
                                        arrayList.add(ide);
                                    }
                                }
                            }

                            RecyclerViewAdapter_preparing_w adapter = new RecyclerViewAdapter_preparing_w(Preparing_Orders_w.this, arrayList, Preparing_Orders_w.this);
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
                runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                                "Json parsing error: " + error.getMessage(),
                                                Toast.LENGTH_LONG)
                                        .show();
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



    private void cancelcount() {
        final TextView cancelorder_value = (TextView) findViewById(R.id.corder_valuep);

        i_ord = 0;
        System.out.println("Cancelled order counter ");

        RequestQueue mQueue2 = Volley.newRequestQueue(getApplicationContext());
        //String finalOrderid = orderid;
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
    public void new_orders() {
        final TextView new_orders_value = (TextView) findViewById(R.id.new_orders_valuep);

        i_ord=0;
        RequestQueue mQueue2 = Volley.newRequestQueue(getApplicationContext());
        StringRequest strord = new StringRequest(POST, weraorderdetails, new Response.Listener<String>() {
            @Override
            public void onResponse(String response2) {
                DataModel_w id = null;
                ArrayList orddatadb = new ArrayList<>();
                //System.out.println("preparing counter " + response2 );
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
    public void updateweraindextable(String dborder_info_id, String status1, String no_items) {

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
                // windex.put("num_item", no_items);

                return windex;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

//------------------------------Updating local database----------------------------------------------
        ContentValues params = new ContentValues();
        params.put("_id", dborder_info_id);
        params.put("order_status", status1);
        // params.put("num_item", no_items);
        Cursor ffifteen_1 = db.rawQuery("SELECT * FROM online_order_wera WHERE _id = '" + dborder_info_id + "'", null);
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
    }
    public void updateweralocaltable(String dborder_info_id, String status1, String no_items) {

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
                // windex.put("order_status", status1);
                // windex.put("num_item", no_items);

                return windex;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

//------------------------------Updating local database----------------------------------------------
        ContentValues params = new ContentValues();
        params.put("_id", dborder_info_id);
        //params.put("order_status", status1);
        // params.put("num_item", no_items);
        Cursor ffifteen_1 = db.rawQuery("SELECT * FROM online_order_wera WHERE _id = '" + dborder_info_id + "'", null);
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
    }
    @Override
    public void onItemClick(DataModel_w item) {
       /* Toast.makeText(getApplicationContext(), item.text + " is clicked", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Preparing_Orders_w.this, New_Individualorder_Itemview.class);
        intent.putExtra("order_id", item.text);
        intent.putExtra("total_price", item.totalprice);
        intent.putExtra("order_type", item.order_type);
        startActivity(intent);


        */
    }
    public void saveinDB(final String order_id_wera, String currentDateandTime1, String normal1, String currentDateandTime, String time1, String time24, String time24_new, String time14, String bills, String printbillno){
        /*progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
        progressBar.setVisibility(View.VISIBLE);*/
        db = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        db1 = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        /*SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy MM dd");
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

        final TextView billnum = new TextView(Preparing_Orders_w.this);
        billnum.setText(currentDateandTime + "-" + "000" + "0" + "1" + bills);
        printbillno=billnum.getText().toString();*/

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

                                        String var_name = null;
                                        String var_price =null;
                                        TextView vari = new TextView(Preparing_Orders_w.this);
                                        TextView pri = new TextView(Preparing_Orders_w.this);


                                        try {
                                            // for Variants array
                                            JSONArray variants = items.getJSONArray("variants");
                                            for (int k = 0; k < variants.length(); k++) {
                                                JSONObject var = variants.getJSONObject(k);
                                                var_name = var.getString("variant_name");
                                                vari.setText(var_name);
                                                var_price = var.getString("price");
                                                String var_size_id = var.getJSONObject("variants").getString("size_id");
                                                String var_size_name = var.getJSONObject("variants").getString("size_name");


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
                                            JSONArray addons = items.getJSONArray("addons");
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


                                        if(vari.getText().toString().isEmpty()) {
                                            vari.setText(online_name);
                                            pri.setText(price0);
                                        }else{
                                            vari.setText(online_name+" ("+var_name+")");
                                            pri.setText(total0);
                                            System.out.println("variant name  "+vari.getText().toString());
                                        }
                                        Cursor cursor = db1.rawQuery("SELECT * FROM Items WHERE itemname = '"+online_name+"'", null);
                                        if (cursor.moveToFirst()){
                                            category_get1 = cursor.getString(4);
                                            barcode_ge_table = cursor.getString(16);
                                        }

                                        ContentValues newValues = new ContentValues();
                                        newValues.put("order_id", order_id_wera);
                                        newValues.put("quantity", qty);
                                        newValues.put("itemname", vari.getText().toString());
                                        newValues.put("price", pri.getText().toString());
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
                                            TextView tv = new TextView(Preparing_Orders_w.this);
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

    public void printer(String printbillno){
        //================================Printer code==================================================
        billnum= printbillno;

        System.out.println("Printer code starts"+billnum);

     /*   final Dialog dialog1 = new Dialog(Preparing_Orders_w.this, R.style.notitle);
        dialog1.setContentView(R.layout.online_bill);
        dialog1.setTitle(Html.fromHtml("<font color='#ffffff'>Bill</font>"));
        System.out.println("Printer code starts within");

        TableLayout tableLayoutt = (TableLayout) dialog1.findViewById(R.id.lytpedidooo);
        tableLayoutt.removeAllViews();

        Button okcl = (Button)dialog1.findViewById(R.id.btncancel);
        okcl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        TextView rs = (TextView) dialog1.findViewById(R.id.rs);
        rs.setText(insert1_cc);

        Button print = (Button)dialog1.findViewById(R.id.btnprint);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {*/
        final Dialog dialog1 = new Dialog(Preparing_Orders_w.this, R.style.notitle);
        dialog1.setContentView(R.layout.online_bill);
        System.out.println("Printer code starts click");
        // final Dialog dialog1 = new Dialog(Preparing_Orders_w.this, R.style.notitle);
//                                dialog1.dismiss();
        Cursor connnet = db1.rawQuery("SELECT * FROM IPConn", null);
        if (connnet.moveToFirst()) {
            ipnameget = connnet.getString(1);
            portget = connnet.getString(2);
            statusnet = connnet.getString(3);
        }
        connnet.close();

        Cursor connnet_counter = db1.rawQuery("SELECT * FROM IPConn_Counter", null);
        if (connnet_counter.moveToFirst()) {
            ipnameget_counter = connnet_counter.getString(1);
            portget_counter = connnet_counter.getString(2);
            statusnet_counter = connnet_counter.getString(3);
        }
        connnet_counter.close();

        Cursor conn = db1.rawQuery("SELECT * FROM BTConn", null);
        if (conn.moveToFirst()) {
            nameget = conn.getString(1);
            addget = conn.getString(2);
            statussusb = conn.getString(3);
        }
        conn.close();



        Cursor cc = db1.rawQuery("SELECT * FROM Printerreceiptsize", null);
        if (cc.moveToFirst()) {
            String Na = cc.getString(1);
            if (Na.equals("A4")) {
//                                Toast.makeText(getApplicationContext(), "A4", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Preparing_Orders_w.this, A4_Printer_new.class);
                intent.putExtra("billnumber", billnum);
                startActivity(intent);
            } else {
//                                Toast.makeText(getApplicationContext(), "hi", Toast.LENGTH_LONG).show();
                if (statusnet.equals("ok") || statusnet_counter.equals("ok") || statussusb.equals("ok")) {

                    printbillcopy1(dialog1, billnum);
                    //dialog1.dismiss();


                } else {

                    String printer_type = "";
                    Cursor aallrows = db1.rawQuery("SELECT * FROM Printer_type WHERE _id = '1'", null);
                    if (aallrows.moveToFirst()) {
                        do {
                            printer_type = aallrows.getString(1);

                        } while (aallrows.moveToNext());
                    }
                    aallrows.close();

                    if (printer_type.equalsIgnoreCase("wiseposplus")) {
                        if (MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO) {
                            mPrintData = new ArrayList<>();

                            Log.e(Preparing_Orders_w.class.getSimpleName(), "mPrintData: " + mPrintData);
                            byte[] receiptData = neoprintbillcopy1(dialog1);
                            mPrintData.add(receiptData);


                            if (mPrintData != null && mPrintData.size() > 0) {
                                mMSWisepadDeviceController.print(mPrintData);
                            }

                        } else {
                            wiseposprintbillcopy1(dialog1);

                        }
                    } else {

                        final Dialog dialogconn = new Dialog(Preparing_Orders_w.this, R.style.notitle);
                        dialogconn.setContentView(R.layout.dialog_printer_conn_error_orderlist);

                        Button conti = (Button) dialogconn.findViewById(R.id.ok);
                        conti.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(Preparing_Orders_w.this, "checkprinterconncash11", Toast.LENGTH_SHORT).show();
                                dialogconn.dismiss();
                            }
                        });

                        dialogconn.show();
                    }
                }
                //printbillcopy1(dialog1);
            }
        }
        cc.close();
           /* }
        });*/
        //================================================================================================

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
    /*public void pre_order_items(String order_id, String merch_id, Context mContext) {

        System.out.println("in the method");
        final Dialog dialog1 = new Dialog(this, R.style.timepicker_date_dialog);
        dialog1.setContentView(R.layout.order_items_w);
        dialog1.show();
        Context icontext= mContext;

        // TableLayout itemtab = (TableLayout) dialog1.findViewById(R.id.itemtab);

        LinearLayout back_activity = (LinearLayout) dialog1.findViewById(R.id.back_activity);
        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        TextView cancel = (TextView) dialog1.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        TextView itmname = (TextView) dialog1.findViewById(R.id.itemname);
        TextView itmqnty = (TextView) dialog1.findViewById(R.id.itemqnty);

        try {
            //I try to use this for send Header is application/json
            jsonBody = new JSONObject();
            jsonBody.put("merchant_id", merch_id);
            jsonBody.put("order_id", order_id);
            //  jsonBody.put("rejection_id", rr_id);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }


        RequestQueue mQueue = Volley.newRequestQueue(icontext);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url_orders, jsonBody,
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
    public void printbillcopy1(Dialog dialog1, String billnum) {
        billnumb=billnum;

        Cursor connnet = db1.rawQuery("SELECT * FROM IPConn", null);
        if (connnet.moveToFirst()) {
            ipnamegets = connnet.getString(1);
            portgets = connnet.getString(2);
            statusnets = connnet.getString(3);
        }
        connnet.close();

        Cursor connnet_counter = db1.rawQuery("SELECT * FROM IPConn_Counter", null);
        if (connnet_counter.moveToFirst()) {
            ipnamegets_counter = connnet_counter.getString(1);
            portgets_counter = connnet_counter.getString(2);
            statusnets_counter = connnet_counter.getString(3);
        }
        connnet_counter.close();

        Cursor connusb = db1.rawQuery("SELECT * FROM BTConn", null);
        if (connusb.moveToFirst()) {
            addgets = connusb.getString(1);
            namegets = connusb.getString(2);
            statussusbs = connusb.getString(3);
        }
        connusb.close();

        byte[] LF1 = {0x0d,0x0a};

        allbufline = new byte[][]{
                " ".getBytes(),LF1
        };

        //Toast.makeText(Preparing_Orders_w.this, "printbillonly one ", Toast.LENGTH_SHORT).show();
        byte[] setHT34M = {0x1b,0x44,0x04,0x11,0x19,0x00};
        byte[] dotfeed = {0x1b,0x4a,0x10};
        byte[] HTRight = {0x1b,0x61, 0x02,0x09};
        byte[] HT = {0x09};
        byte[] HT1 = {0x09};
        byte[] LF = {0x0d,0x0a};

        byte[] left = {0x1b,0x61, 0x00};
        byte[] cen = {0x1b,0x61, 0x01};
        byte[] right = {0x1b,0x61, 0x02};
        byte[] bold = {0x1B,0x21,0x10};
        byte[] normal = {0x1d, 0x21, 0x00};
        byte[] horiz1 = {0x1b,0x44,0x19, 0x19, 0x00};
        byte[] horiz = {0x1b,0x44, 0x04, 0x06, 0x04, 0x04, 0x0A, 0x00, 0x09, 0x30, 0x09, 0x31, 0x09, 0x32, 0x09, 0x33, 0x09, 0x34, 0x0d,0x0a};

        byte[] un = {0x1b, 0x2d, 0x02};
        byte[] un1 = {0x1b, 0x2d, 0x00};

        String str_line = "";

        Cursor print_ty = db1.rawQuery("SELECT * FROM Printer_type", null);
        if (print_ty.moveToFirst()){
            str_print_ty = print_ty.getString(1);
        }
        print_ty.close();

        Cursor cc = db1.rawQuery("SELECT * FROM Printerreceiptsize", null);

        if (cc.moveToFirst()) {
            cc.moveToFirst();
            do {
                NAME = cc.getString(1);
                if (NAME.equals("3 inch")) {
                    if (str_print_ty.toString().equals("Generic") || str_print_ty.toString().equals("Epson/others")) {
                        setHT32 = new byte[]{0x1b, 0x44, 0x29, 0x00};//2 tabs 3"
                        setHT321 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 3"
                        setHT3212 = new byte[]{0x1b, 0x44, 0x25, 0x00};//2 tabs 3"
                        setHT32122 = new byte[]{0x1b, 0x44, 0x24, 0x00};//2 tabs 3"
                        setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                        setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x20, 0x28, 0x00};//4 tabs 3"
                        nPaperWidth = 576;
                        charlength = 22;
                        charlength1 = 46;
                        charlength2 = 69;
                        quanlentha = 5;
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
                            setHT32122 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                            setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x10, 0x14, 0x00};//4 tabs 3"
                            nPaperWidth = 576;
                            charlength = 22;
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
//                            Toast.makeText(Preparing_Orders_w.this, "phi", Toast.LENGTH_SHORT).show();
                        setHT32 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 2"
                        setHT321 = new byte[]{0x1b, 0x44, 0x13, 0x00};//2 tabs 3"
                        setHT3212 = new byte[]{0x1b, 0x44, 0x15, 0x00};//2 tabs 3"
                        setHT32122 = new byte[]{0x1b, 0x44, 0x14, 0x00};//2 tabs 3"
                        setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                        setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x10, 0x18, 0x00};//4 tabs 2"
                        nPaperWidth = 384;
                        charlength = 8;
                        charlength1 = 20;
                        charlength2 = 30;
                        quanlentha = 5;
                        HT1 = new byte[]{0x09};
                        str_line = "--------------------------------";
                        allbufline = new byte[][]{
                                left, un1, "--------------------------------".getBytes(), LF

                        };
                    } else {
                        if (str_print_ty.toString().equals("Epson/others")) {
//                            Toast.makeText(Preparing_Orders_w.this, "epson", Toast.LENGTH_SHORT).show();
                            setHT32 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 2"
                            setHT321 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x21, 0x00};//2 tabs 3"
                            setHT32122 = new byte[]{0x1b, 0x44, 0x20, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                            setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x17, 0x20, 0x00};//4 tabs 2"
                            nPaperWidth = 384;
                            charlength = 14;
                            charlength1 = 32;
                            charlength2 = 48;
                            quanlentha = 5;
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
                                setHT32122 = new byte[]{0x1b, 0x44, 0x08, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x12, 0x21, 0x00};//4 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x05, 0x08, 0x00};//4 tabs 2"
                                setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x06, 0x08, 0x00};//4 tabs 2"
                                nPaperWidth = 384;
                                charlength = 9;
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

        String dd = "";
        TextView qazcvb = new TextView(Preparing_Orders_w.this);
        Cursor cvonnusb = db1.rawQuery("SELECT * FROM BTConn", null);
        if (cvonnusb.moveToFirst()) {
            addgets = cvonnusb.getString(1);
            namegets = cvonnusb.getString(2);
            statussusbs = cvonnusb.getString(3);
            dd = cvonnusb.getString(4);
        }
        cvonnusb.close();
        qazcvb.setText(dd);
        if (qazcvb.getText().toString().equals("usb") && statussusbs.equals("ok")) {
//            runPrintCouponSequence(dialog1);
            print_printbillcopy1(dialog1);
        }else {
            imageViewPicture = (ImageView) dialog1.findViewById(R.id.imageViewPicturew);
            mView = dialog1.findViewById(R.id.f_vieww1);


            allbuf1 = new byte[][]{
                    bold, un, "Bill copy".getBytes(), LF

            };
            if (statussusbs.equals("ok")) {
                BluetoothPrintDriver.BT_Write(bold);    //
                BluetoothPrintDriver.BT_Write(un);    //
                BT_Write("Bill copy");
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(bold);    //
                    wifiSocket2.WIFI_Write(un);    //
                    wifiSocket2.WIFI_Write("Bill copy");
                    wifiSocket2.WIFI_Write(LF);    //
                }else {
                    if (statusnets.toString().equals("ok")) {
                        wifiSocket.WIFI_Write(bold);    //
                        wifiSocket.WIFI_Write(un);    //
                        wifiSocket.WIFI_Write("Bill copy");
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }

            ImageView imageButton = (ImageView) mView.findViewById(R.id.viewImagee);
            //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(116, 95);
            //imageButton.setLayoutParams(layoutParams);
            //imageButton.setLayoutParams(116, 95);

            if (NAME.equals("3 inch")) {
//            Toast.makeText(Preparing_Orders_w.this, "3 inch", Toast.LENGTH_SHORT).show();
                imageViewPicture.getLayoutParams().height = 94;
                imageViewPicture.getLayoutParams().width = 576;
                imageButton.getLayoutParams().height = 94;
                imageButton.getLayoutParams().width = 576;
            } else {
//            Toast.makeText(Preparing_Orders_w.this, "2 inch", Toast.LENGTH_SHORT).show();
                imageViewPicture.getLayoutParams().height = 94;
                imageViewPicture.getLayoutParams().width = 384;
                imageButton.getLayoutParams().height = 94;
                imageButton.getLayoutParams().width = 384;
            }

            String[] col = {"companylogo"};
            Cursor c = db1.query("Logo", col, null, null, null, null, null);
            if (c.moveToFirst()) {
                do {
                    byte[] img = c.getBlob(c.getColumnIndex("companylogo"));
                    final Bitmap b1 = BitmapFactory.decodeByteArray(img, 0, img.length);

                    imageButton.setImageBitmap(b1);


                    mView.setDrawingCacheEnabled(true);
                    mView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    mView.layout(0, 0, mView.getMeasuredWidth(), mView.getMeasuredHeight());
                    mView.buildDrawingCache(true);

                    Bitmap b = Bitmap.createBitmap(mView.getDrawingCache());
                    //mView.setDrawingCacheEnabled(false);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                    imageViewPicture.setImageBitmap(b);

                    Bitmap mBitmap = ((BitmapDrawable) imageViewPicture.getDrawable())
                            .getBitmap();


                    //if (mBitmap != null) {
                    if (statussusbs.equals("ok")) {
                        if (mBitmap != null) {
                            byte[] command = Utils.decodeBitmap(mBitmap);
                            printByteData(command);
                        } else {
                            Log.e("Print Photo error", "the file isn't exists");
                        }
                        Bundle data = new Bundle();
                        //data.putParcelable(Global.OBJECT1, mBitmap);
                        data.putParcelable(Global.PARCE1, mBitmap);
                        data.putInt(Global.INTPARA1, nPaperWidth);
                        data.putInt(Global.INTPARA2, 0);
//                        DrawerService.workThread.handleCmd(
//                                Global.CMD_POS_PRINTBWPICTURE, data);
                    } else {
                        if (statusnets_counter.equals("ok")) {
                            if (mBitmap != null) {
                                byte[] command = Utils.decodeBitmap(mBitmap);
                                printByteData_wifi_counter(command);
                            } else {
                                Log.e("Print Photo error", "the file isn't exists");
                            }
                            Bundle data = new Bundle();
                            data.putParcelable(Global1.PARCE1, mBitmap);
                            data.putInt(Global1.INTPARA1, nPaperWidth);
                            data.putInt(Global1.INTPARA2, 0);
                        }else {
                            if (statusnets.equals("ok")) {
                                if (mBitmap != null) {
                                    byte[] command = Utils.decodeBitmap(mBitmap);
                                    printByteData_wifi(command);
                                } else {
                                    Log.e("Print Photo error", "the file isn't exists");
                                }
                                Bundle data = new Bundle();
                                data.putParcelable(Global1.PARCE1, mBitmap);
                                data.putInt(Global1.INTPARA1, nPaperWidth);
                                data.putInt(Global1.INTPARA2, 0);
                            }
                        }
                    }
                    //}
                } while (c.moveToNext());
            } else {
                imageButton.setVisibility(View.GONE);
            }
            c.close();

            Cursor getcom = db1.rawQuery("SELECT * FROM Companydetailss", null);
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


            tvkot.setText(strcompanyname);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf1 = new byte[][]{
                        bold, un1, cen, strcompanyname.getBytes(), LF

                };
                if (statussusbs.equals("ok")) {
                    BluetoothPrintDriver.BT_Write(bold);    //
                    BluetoothPrintDriver.BT_Write(un1);    //
                    BluetoothPrintDriver.BT_Write(cen);    //
                    BT_Write(strcompanyname);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.equals("ok")) {
                        wifiSocket2.WIFI_Write(bold);    //
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);    //
                        wifiSocket2.WIFI_Write(strcompanyname);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.equals("ok")) {
                            wifiSocket.WIFI_Write(bold);    //
                            wifiSocket.WIFI_Write(un1);    //
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
                        normal, straddress1.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.equals("ok")) {
                    BluetoothPrintDriver.BT_Write(normal);    //
                    BT_Write(straddress1);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.equals("ok")) {
                        wifiSocket2.WIFI_Write(normal);    //
                        wifiSocket2.WIFI_Write(straddress1);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.equals("ok")) {
                            wifiSocket.WIFI_Write(normal);    //
                            wifiSocket.WIFI_Write(straddress1);
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }


            tvkot.setText(straddress2);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf3 = new byte[][]{
                        normal, straddress2.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.equals("ok")) {
                    BluetoothPrintDriver.BT_Write(normal);    //
                    BT_Write(straddress2);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.equals("ok")) {
                        wifiSocket2.WIFI_Write(normal);    //
                        wifiSocket2.WIFI_Write(straddress2);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.equals("ok")) {
                            wifiSocket.WIFI_Write(normal);    //
                            wifiSocket.WIFI_Write(straddress2);
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }


            tvkot.setText(straddress3);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf4 = new byte[][]{
                        normal, straddress3.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.equals("ok")) {
                    BluetoothPrintDriver.BT_Write(normal);    //
                    BT_Write(straddress3);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.equals("ok")) {
                        wifiSocket2.WIFI_Write(normal);    //
                        wifiSocket2.WIFI_Write(straddress3);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.equals("ok")) {
                            wifiSocket.WIFI_Write(normal);    //
                            wifiSocket.WIFI_Write(straddress3);
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }

            tvkot.setText(strphone);
            String pp = "Ph. " + strphone;
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf5 = new byte[][]{
                        normal, pp.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.equals("ok")) {
                    BluetoothPrintDriver.BT_Write(normal);    //
                    BT_Write(pp);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.equals("ok")) {
                        wifiSocket2.WIFI_Write(normal);    //
                        wifiSocket2.WIFI_Write(pp);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.equals("ok")) {
                            wifiSocket.WIFI_Write(normal);    //
                            wifiSocket.WIFI_Write(pp);
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }

            tvkot.setText(stremailid);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf6 = new byte[][]{
                        normal, stremailid.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.equals("ok")) {
                    BluetoothPrintDriver.BT_Write(normal);    //
                    BT_Write(stremailid);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.equals("ok")) {
                        wifiSocket2.WIFI_Write(normal);    //
                        wifiSocket2.WIFI_Write(stremailid);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.equals("ok")) {
                            wifiSocket.WIFI_Write(normal);    //
                            wifiSocket.WIFI_Write(stremailid);
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }

            tvkot.setText(strwebsite);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf7 = new byte[][]{
                        normal, strwebsite.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.equals("ok")) {
                    BluetoothPrintDriver.BT_Write(normal);    //
                    BT_Write(strwebsite);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.equals("ok")) {
                        wifiSocket2.WIFI_Write(normal);    //
                        wifiSocket2.WIFI_Write(strwebsite);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.equals("ok")) {
                            wifiSocket.WIFI_Write(normal);    //
                            wifiSocket.WIFI_Write(strwebsite);
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }

            tvkot.setText(strtaxone);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf8 = new byte[][]{
                        normal, strtaxone.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.equals("ok")) {
                    BluetoothPrintDriver.BT_Write(normal);    //
                    BT_Write(strtaxone);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.equals("ok")) {
                        wifiSocket2.WIFI_Write(normal);    //
                        wifiSocket2.WIFI_Write(strtaxone);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.equals("ok")) {
                            wifiSocket.WIFI_Write(normal);    //
                            wifiSocket.WIFI_Write(strtaxone);
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }


            if (statussusbs.equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BT_Write(str_line);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.equals("ok")) {
                    wifiSocket2.WIFI_Write(left);	//
                    wifiSocket2.WIFI_Write(un1);	//
                    wifiSocket2.WIFI_Write(str_line);
                    wifiSocket2.WIFI_Write(LF);	//
                }else {
                    if (statusnets.equals("ok")) {
                        wifiSocket.WIFI_Write(left);	//
                        wifiSocket.WIFI_Write(un1);	//
                        wifiSocket.WIFI_Write(str_line);
                        wifiSocket.WIFI_Write(LF);	//
                    }
                }
            }


            //billnumb=printbillno;

            Cursor cursor10 = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billnumb + "'", null);
            if (cursor10.moveToFirst()) {
                billtypea = cursor10.getString(5);
                paymmethoda = cursor10.getString(6);
            }
            cursor10.close();

            TextView ttv = new TextView(Preparing_Orders_w.this);
            ttv.setText(billtypea);

            TextView ttv1 = new TextView(Preparing_Orders_w.this);
            ttv1.setText(paymmethoda);

//            if (ttv.getText().toString().equals("  Cash")) {
//                billtypeaa = "Cash";
//            } else {
//                billtypeaa = "Card";
//            }

            if (ttv.getText().toString().equals("  Cash")) {
                billtypeaa = "Cash"; //0
            }
            if (ttv.getText().toString().equals("  Card")) {
                billtypeaa = "Card"; //0
            }
            if (ttv.getText().toString().equals("  Paytm")) {
                billtypeaa = "Paytm"; //0
            }
            if (ttv.getText().toString().equals("  Mobikwik")) {
                billtypeaa = "Mobikwik"; //0
            }
            if (ttv.getText().toString().equals("  Freecharge")) {
                billtypeaa = "Freecharge"; //0
            }
            if (ttv.getText().toString().equals("  Pay Later")) {
                billtypeaa = "Pay Later"; //0
            }
            if (ttv.getText().toString().equals("  Cheque")) {
                billtypeaa = "Cheque"; //0
            }
            if (ttv.getText().toString().equals("  Sodexo")) {
                billtypeaa = "Sodexo"; //0
            }
            if (ttv.getText().toString().equals("  Zeta")) {
                billtypeaa = "Zeta"; //0
            }
            if (ttv.getText().toString().equals("  Ticket")) {
                billtypeaa = "Ticket"; //0
            }
            if (ttv.getText().toString().equals("  Upiqr")) {
                billtypeaa = "upiqr"; //0
            }
            if (ttv.getText().toString().equals("Zomato")) {
                billtypeaa = "Zomato"; //0
            }
            if (ttv.getText().toString().equals("Swiggy")) {
                billtypeaa = "Swiggy"; //0
            }
            if (ttv.getText().toString().equals("Online")) {
                billtypeaa = "Online"; //0
            }

            billtypeaa = ttv.getText().toString().replace(" ", "");
            System.out.println("Bill number:  "+printbillno+" 1 "+billtypeaa+" 2 "+billnumb);
            //String bill_no = billnum.getText().toString();
            allbufbillno = new byte[][]{
                    setHT32, un1, billnumb.getBytes(), HT, "   ".getBytes(), billtypeaa.getBytes(), LF
//						left, normal, setHT22, "DECAF16".getBytes(), HT, right, "30".getBytes(), LF,
//						left, normal, setHT22, "BREVE".getBytes(), HT, right, "1000".getBytes(), LF,
            };

            if (str_print_ty.equals("POS")) {
                if (statussusbs.equals("ok")) {
                    BluetoothPrintDriver.BT_Write(setHT321);    //
                    BluetoothPrintDriver.BT_Write(normal);    //
                    BluetoothPrintDriver.BT_Write(un1);    //
                    BT_Write("Bill no." + billnumb);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.equals("ok")) {
                        wifiSocket2.WIFI_Write(setHT321);    //
                        wifiSocket2.WIFI_Write(normal);    //
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write("Bill no." + billnumb);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.equals("ok")) {
                            wifiSocket.WIFI_Write(setHT321);    //
                            wifiSocket.WIFI_Write(normal);    //
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write("Bill no." + billnumb);
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }else {
                if (statussusbs.equals("ok")) {
                    BluetoothPrintDriver.BT_Write(setHT32);    //
                    BluetoothPrintDriver.BT_Write(normal);    //
                    BluetoothPrintDriver.BT_Write(un1);    //
                    BT_Write("Bill no."+billnumb);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.equals("ok")) {
                        wifiSocket2.WIFI_Write(setHT32);    //
                        wifiSocket2.WIFI_Write(normal);    //
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write("Bill no."+billnumb);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.equals("ok")) {
                            wifiSocket.WIFI_Write(setHT32);    //
                            wifiSocket.WIFI_Write(normal);    //
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write("Bill no."+billnumb);
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }

            if (str_print_ty.equals("POS")) {
                if (statussusbs.equals("ok")) {
                    BluetoothPrintDriver.BT_Write(setHT321);    //
                    BT_Write(billtypeaa);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.equals("ok")) {
                        wifiSocket2.WIFI_Write(setHT321);
                        wifiSocket2.WIFI_Write(billtypeaa);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.equals("ok")) {
                            wifiSocket.WIFI_Write(setHT321);    //
                            wifiSocket.WIFI_Write(billtypeaa);
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }else {
                if (statussusbs.equals("ok")) {
                    BluetoothPrintDriver.BT_Write(setHT32);    //
                    BT_Write(billtypeaa);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.equals("ok")) {
                        wifiSocket2.WIFI_Write(setHT32);    //
                        wifiSocket2.WIFI_Write(billtypeaa);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.equals("ok")) {
                            wifiSocket.WIFI_Write(setHT32);    //
                            wifiSocket.WIFI_Write(billtypeaa);
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }

            if (ttv1.getText().toString().equals("  Dine-in") || ttv1.getText().toString().equals("  General") || ttv1.getText().toString().equals("  Others")) {
//                paymmethodaa = "Dine-in";
                //billtypee.setText("Dine-in");
                if (account_selection.toString().equals("Dine") || account_selection.toString().equals("Qsr")) {
                    paymmethodaa = "Dine-in";
                }else {
                    paymmethodaa = "General";
                }
            } else {
                if (ttv1.getText().toString().equals("  Takeaway") || ttv1.getText().toString().equals("  Main")) {
                    paymmethodaa = "Takeaway";
                    //billtypee.setText("Takeaway");
                } else {
                    paymmethodaa = "Home delivery";
                    //billtypee.setText("Home delivery");
                }
            }

            Cursor date = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "'", null);
            if (date.moveToFirst()) {
                datee = date.getString(25);
                timee = date.getString(12);
            }
            date.close();

            allbuf10 = new byte[][]{
                    setHT321, left, paymmethodaa.getBytes(), HT, "  ".getBytes(), datee.getBytes(), LF
                    //setHT321,left,paymmethodaa.getBytes(),HT,datee.getBytes(),LF

            };
            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

            if (statussusbs.equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHT321);    //
                BluetoothPrintDriver.BT_Write(left);    //
                BT_Write(paymmethodaa);
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write("  " + datee);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.equals("ok")) {
                    wifiSocket2.WIFI_Write(setHT321);    //
                    wifiSocket2.WIFI_Write(left);    //
                    wifiSocket2.WIFI_Write(paymmethodaa);
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write("  " + datee);
                    wifiSocket2.WIFI_Write(LF);    //
                }else {
                    if (statusnets.equals("ok")) {
                        wifiSocket.WIFI_Write(setHT321);    //
                        wifiSocket.WIFI_Write(left);    //
                        wifiSocket.WIFI_Write(paymmethodaa);
                        wifiSocket.WIFI_Write(HT);    //
                        wifiSocket.WIFI_Write("  " + datee);
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }

            Cursor cursor9 = db.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billnumb + "'", null);
            if (cursor9.moveToFirst()) {
                tableida = cursor9.getString(15);
                Cursor vbnm = db1.rawQuery("SELECT * FROM asd1 WHERE _id = '" + tableida + "'", null);
                if (vbnm.moveToFirst()) {
                    assa1 = vbnm.getString(1);
                    assa2 = vbnm.getString(2);
                }
                vbnm.close();
                TextView cx = new TextView(Preparing_Orders_w.this);
                cx.setText(assa1);

                if (cx.getText().toString().equals("")) {
                    tableidaa = "Tab" + assa2;
                    allbuftime = new byte[][]{
                            setHT321, left, tableidaa.getBytes(), HT, "  ".getBytes(), timee.getBytes(), LF
                    };
                } else {
                    tableidaa = "Tab" + assa1;
                    allbuftime = new byte[][]{
                            setHT321, left, tableidaa.getBytes(), HT, "  ".getBytes(), timee.getBytes(), LF
                    };
                }

            }
            cursor9.close();

            if (statussusbs.equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHT321);    //
                BluetoothPrintDriver.BT_Write(left);    //
                BT_Write(tableidaa);
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write("  " + timee);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.equals("ok")) {
                    wifiSocket2.WIFI_Write(setHT321);    //
                    wifiSocket2.WIFI_Write(left);    //
                    wifiSocket2.WIFI_Write(tableidaa);
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write("  " + timee);
                    wifiSocket2.WIFI_Write(LF);    //
                }else {
                    if (statusnets.equals("ok")) {
                        wifiSocket.WIFI_Write(setHT321);    //
                        wifiSocket.WIFI_Write(left);    //
                        wifiSocket.WIFI_Write(tableidaa);
                        wifiSocket.WIFI_Write(HT);    //
                        wifiSocket.WIFI_Write("  " + timee);
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }

            Cursor cursor9_1 = db.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billnumb + "'", null);
            if (cursor9_1.moveToFirst()) {
                u_name = cursor9_1.getString(45);
            }
            cursor9_1.close();

            TextView tv_u_name = new TextView(Preparing_Orders_w.this);
            tv_u_name.setText(u_name);

            if (tv_u_name.getText().toString().equals("")){

            }else {
                allbuf11 = new byte[][]{
                        left, setHT321, "Counter person ".getBytes(), LF
                };
                if (statussusbs.equals("ok")) {
                    BluetoothPrintDriver.BT_Write(left);    //
                    BluetoothPrintDriver.BT_Write(setHT321);    //
                    BT_Write("Counter person: " + tv_u_name.getText().toString());
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.equals("ok")) {
                        wifiSocket2.WIFI_Write(left);    //
                        wifiSocket2.WIFI_Write(setHT321);    //
                        wifiSocket2.WIFI_Write("Counter person: " + tv_u_name.getText().toString());
                        wifiSocket2.WIFI_Write(LF);    //
                    } else {
                        if (statusnets.equals("ok")) {
                            wifiSocket.WIFI_Write(left);    //
                            wifiSocket.WIFI_Write(setHT321);    //
                            wifiSocket.WIFI_Write("Counter person: " + tv_u_name.getText().toString());
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }

            if (statussusbs.equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BT_Write(str_line);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.equals("ok")) {
                    wifiSocket2.WIFI_Write(left);	//
                    wifiSocket2.WIFI_Write(un1);	//
                    wifiSocket2.WIFI_Write(str_line);
                    wifiSocket2.WIFI_Write(LF);	//
                }else {
                    if (statusnets.equals("ok")) {
                        wifiSocket.WIFI_Write(left);	//
                        wifiSocket.WIFI_Write(un1);	//
                        wifiSocket.WIFI_Write(str_line);
                        wifiSocket.WIFI_Write(LF);	//
                    }
                }
            }

            Cursor caddress = db.rawQuery("SELECT * FROM Customerdetails WHERE billnumber = '" + billnumb + "'", null);
            if (caddress.moveToFirst()) {
                String nam = caddress.getString(1);
                String addr = caddress.getString(4);
                String phon = caddress.getString(2);
                String emai = caddress.getString(3);

                if (nam.length() > 0 || addr.length() > 0 ||
                        phon.length() > 0 || emai.length() > 0) {
                    allbufcust = new byte[][]{
                            left, un, "Customer:".getBytes(), LF, un1,
                    };

                    if (statussusbs.equals("ok")) {
                        BluetoothPrintDriver.BT_Write(left);    //
                        BluetoothPrintDriver.BT_Write(un);    //
                        BT_Write("Customer:");
                        BluetoothPrintDriver.BT_Write(LF);    //
                        BluetoothPrintDriver.BT_Write(un1);    //
                    } else {
                        if (statusnets_counter.equals("ok")) {
                            wifiSocket2.WIFI_Write(left);    //
                            wifiSocket2.WIFI_Write(un);    //
                            wifiSocket2.WIFI_Write("Customer:");
                            wifiSocket2.WIFI_Write(LF);    //
                            wifiSocket2.WIFI_Write(un1);    //
                        }else {
                            if (statusnets.equals("ok")) {
                                wifiSocket.WIFI_Write(left);    //
                                wifiSocket.WIFI_Write(un);    //
                                wifiSocket.WIFI_Write("Customer:");
                                wifiSocket.WIFI_Write(LF);    //
                                wifiSocket.WIFI_Write(un1);    //
                            }
                        }
                    }
                } else {

                }

                if (nam.length() > 0) {
                    allbufcustname = new byte[][]{
                            nam.getBytes(), LF

                    };
                    //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                    if (statussusbs.equals("ok")) {
                        BT_Write(nam);
                        BluetoothPrintDriver.BT_Write(LF);    //
                    } else {
                        if (statusnets_counter.equals("ok")) {
                            wifiSocket2.WIFI_Write(nam);
                            wifiSocket2.WIFI_Write(LF);    //
                        }else {
                            if (statusnets.equals("ok")) {
                                wifiSocket.WIFI_Write(nam);
                                wifiSocket.WIFI_Write(LF);    //
                            }
                        }
                    }
                } else {

                }

                if (addr.length() > 0) {
                    allbufcustadd = new byte[][]{
                            addr.toString().getBytes(), LF

                    };
                    //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                    if (statussusbs.equals("ok")) {
                        BT_Write(addr);
                        BluetoothPrintDriver.BT_Write(LF);    //
                    } else {
                        if (statusnets_counter.equals("ok")) {
                            wifiSocket2.WIFI_Write(addr);
                            wifiSocket2.WIFI_Write(LF);    //
                        }else {
                            if (statusnets.equals("ok")) {
                                wifiSocket.WIFI_Write(addr);
                                wifiSocket.WIFI_Write(LF);    //
                            }
                        }
                    }
                } else {

                }

                if (phon.length() > 0) {
                    String cust_ph = "Ph. " + phon;
                    allbufcustph = new byte[][]{
                            cust_ph.getBytes(), LF

                    };
                    //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                    if (statussusbs.equals("ok")) {
                        BT_Write(cust_ph);
                        BluetoothPrintDriver.BT_Write(LF);    //
                    } else {
                        if (statusnets_counter.equals("ok")) {
                            wifiSocket2.WIFI_Write(cust_ph);
                            wifiSocket2.WIFI_Write(LF);    //
                        }else {
                            if (statusnets.equals("ok")) {
                                wifiSocket.WIFI_Write(cust_ph);
                                wifiSocket.WIFI_Write(LF);    //
                            }
                        }
                    }
                } else {

                }

                if (emai.length() > 0) {
                    allbufcustemail = new byte[][]{
                            emai.toString().getBytes(), LF

                    };
                    //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                    if (statussusbs.equals("ok")) {
                        BT_Write(emai);
                        BluetoothPrintDriver.BT_Write(LF);    //
                    } else {
                        if (statusnets_counter.equals("ok")) {
                            wifiSocket2.WIFI_Write(emai);
                            wifiSocket2.WIFI_Write(LF);    //
                        }else {
                            if (statusnets.equals("ok")) {
                                wifiSocket.WIFI_Write(emai);
                                wifiSocket.WIFI_Write(LF);    //
                            }
                        }
                    }
                } else {

                }

                if (statussusbs.equals("ok")) {
                    BluetoothPrintDriver.BT_Write(left);    //
                    BluetoothPrintDriver.BT_Write(un1);    //
                    BT_Write(str_line);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.equals("ok")) {
                        wifiSocket2.WIFI_Write(left);	//
                        wifiSocket2.WIFI_Write(un1);	//
                        wifiSocket2.WIFI_Write(str_line);
                        wifiSocket2.WIFI_Write(LF);	//
                    }else {
                        if (statusnets.equals("ok")) {
                            wifiSocket.WIFI_Write(left);	//
                            wifiSocket.WIFI_Write(un1);	//
                            wifiSocket.WIFI_Write(str_line);
                            wifiSocket.WIFI_Write(LF);	//
                        }
                    }
                }
            }
            caddress.close();

            allbufqty = new byte[][]{
                    setHT34, normal, "Qty".getBytes(), HT, "Item".getBytes(), HT, "Price".getBytes(), HT, "Amount".getBytes(), LF
            };

            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

            if (statussusbs.equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHT34);    //
                BluetoothPrintDriver.BT_Write(normal);    //
                BT_Write("Qty");
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write("Item");
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write("  Price");
                BluetoothPrintDriver.BT_Write(HT1);    //
                BT_Write("  Amount");
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.equals("ok")) {
                    wifiSocket2.WIFI_Write(setHT34);    //
                    wifiSocket2.WIFI_Write(normal);    //
                    wifiSocket2.WIFI_Write("Qty");
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write("Item");
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write("  Price");
                    wifiSocket2.WIFI_Write(HT1);    //
                    wifiSocket2.WIFI_Write("  Amount");
                    wifiSocket2.WIFI_Write(LF);    //
                }else {
                    if (statusnets.equals("ok")) {
                        wifiSocket.WIFI_Write(setHT34);    //
                        wifiSocket.WIFI_Write(normal);    //
                        wifiSocket.WIFI_Write("Qty");
                        wifiSocket.WIFI_Write(HT);    //
                        wifiSocket.WIFI_Write("Item");
                        wifiSocket.WIFI_Write(HT);    //
                        wifiSocket.WIFI_Write("  Price");
                        wifiSocket.WIFI_Write(HT1);    //
                        wifiSocket.WIFI_Write("  Amount");
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }

            if (statussusbs.equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BT_Write(str_line);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(left);	//
                    wifiSocket2.WIFI_Write(un1);	//
                    wifiSocket2.WIFI_Write(str_line);
                    wifiSocket2.WIFI_Write(LF);	//
                }else {
                    if (statusnets.toString().equals("ok")) {
                        wifiSocket.WIFI_Write(left);	//
                        wifiSocket.WIFI_Write(un1);	//
                        wifiSocket.WIFI_Write(str_line);
                        wifiSocket.WIFI_Write(LF);	//
                    }
                }
            }

            Cursor ccursorr = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (ccursorr.moveToFirst()) {

                do {

                    String nbg = ccursorr.getString(0);
                    String name = ccursorr.getString(1);
                    String value = ccursorr.getString(2);
                    String pq = ccursorr.getString(5);
                    String itna = ccursorr.getString(2);
                    String pricee = ccursorr.getString(3);
                    String tototot = ccursorr.getString(4);
                    String tototot1 = ccursorr.getString(4);

                    if (pricee.length() > 7) {
//                        price = " "+price;
                    }
                    if (pricee.length() == 6) {
                        pricee = " "+pricee;
                    }
                    if (pricee.length() == 5) {
                        pricee = "  "+pricee;
                    }
                    if (pricee.length() == 4) {
                        pricee = "   "+pricee;
                    }
                    if (pricee.length() == 3) {
                        pricee = "    "+pricee;
                    }

                    if (tototot.length() >= 8) {

                    }
                    if (tototot.length() == 7) {
                        tototot = " "+tototot;
                    }
                    if (tototot.length() == 6) {
                        tototot = "  "+tototot;
                    }
                    if (tototot.length() == 5) {
                        tototot = "   "+tototot;
                    }
                    if (tototot.length() == 4) {
                        tototot = "    "+tototot;
                    }
                    if (tototot.length() == 3) {
                        tototot = "     "+tototot;
                    }

                    final String newid = ccursorr.getString(20);
                    int padding_in_px;

                    int padding_in_dp = 30;  // 34 dps
                    final float scale1 = getResources().getDisplayMetrics().density;
                    padding_in_px = (int) (padding_in_dp * scale1 + 0.5f);


                    if (pq.equals("Item")) {
                        final TableRow row = new TableRow(Preparing_Orders_w.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));


                        final TableRow row1 = new TableRow(Preparing_Orders_w.this);
                        row1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));

                        final TableRow row2 = new TableRow(Preparing_Orders_w.this);
                        row2.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                        final TableLayout tableLayout1 = new TableLayout(Preparing_Orders_w.this);

                        TableRow.LayoutParams lp, lp1, lp2;

                        TextView tv = new TextView(Preparing_Orders_w.this);
                        tv.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 0.70f));
                        tv.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                        tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tv.setText(value);
                        row.addView(tv);

                        TextView tv1 = new TextView(Preparing_Orders_w.this);
                        tv1.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.6f));
                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv1.setGravity(Gravity.CENTER_VERTICAL);
                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tv1.setText(name);
                        String value1 = tv1.getText().toString();
                        row.addView(tv1);

                        TextView tv2 = new TextView(Preparing_Orders_w.this);
                        tv2.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.0f));
                        tv2.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv2.setText(pricee);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(tv2);

                        TextView tv3 = new TextView(Preparing_Orders_w.this);
                        tv3.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                        tv2.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tv3.setText(tototot1);

                        String value2 = tv3.getText().toString();

                        Cursor modcursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND parent = '" + name + "' AND parentid = '" + newid + "' ", null);

                        if (modcursor.moveToFirst()) {

                            Cursor cursor4 = db.rawQuery("SELECT SUM(total) FROM All_Sales WHERE bill_no = '" + billnumb + "'AND parent = '" + name + "' AND parentid = '" + newid + "'", null);
                            if (cursor4.moveToFirst()) {
                                float sub2a = cursor4.getFloat(0);
                                String sub2a1 = String.valueOf(sub2a);
                                ss = Float.parseFloat(sub2a1) + Float.parseFloat(tototot1);
                                ss1 = String.valueOf(ss);

                                if (ss1.length() >= 8) {

                                }
                                if (ss1.length() == 7) {
                                    ss1 = " "+ss1;
                                }
                                if (ss1.length() == 6) {
                                    ss1 = "  "+ss1;
                                }
                                if (ss1.length() == 5) {
                                    ss1 = "   "+ss1;
                                }
                                if (ss1.length() == 4) {
                                    ss1 = "    "+ss1;
                                }
                                if (ss1.length() == 3) {
                                    ss1 = "     "+ss1;
                                }

                            }
                            cursor4.close();

                            if (name.length() > charlength) {
                                int print1 = 0;
//                                Toast.makeText(Preparing_Orders_w.this, "1", Toast.LENGTH_SHORT).show();

                                if (value.length() > quanlentha && name.length() > charlength) {
//                                    Toast.makeText(Preparing_Orders_w.this, "2", Toast.LENGTH_SHORT).show();
                                    String string1quan = value.substring(0, quanlentha);
                                    String string2quan = value.substring(quanlentha);
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength);
                                    allbufitems = new byte[][]{
                                            setHT34, normal, string1quan.getBytes(), HT, string1.getBytes(), HT, pricee.getBytes(), HT1, ss1.getBytes(), LF, string2quan.getBytes(), HT, string2.getBytes(), LF
                                    };
                                    if (print1 == 0){
                                        if (statussusbs.equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(setHT34);    //
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write(string1quan);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write(string1);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BluetoothPrintDriver.BT_Write(right);    //
                                            BT_Write(pricee);
                                            BluetoothPrintDriver.BT_Write(HT1);    //
                                            BT_Write(ss1);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            BT_Write(string2quan);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write(string2);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            print1 = 1;
                                        } else {
                                            if (statusnets_counter.equals("ok")) {
                                                wifiSocket2.WIFI_Write(setHT34);    //
                                                wifiSocket2.WIFI_Write(normal);    //
                                                wifiSocket2.WIFI_Write(string1quan);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(string1);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(right);    //
                                                wifiSocket2.WIFI_Write(pricee);
                                                wifiSocket2.WIFI_Write(HT1);    //
                                                wifiSocket2.WIFI_Write(ss1);
                                                wifiSocket2.WIFI_Write(LF);    //
                                                wifiSocket2.WIFI_Write(string2quan);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(string2);
                                                wifiSocket2.WIFI_Write(LF);    //
                                                print1 = 1;
                                            }else {
                                                if (statusnets.equals("ok")) {
                                                    wifiSocket.WIFI_Write(setHT34);    //
                                                    wifiSocket.WIFI_Write(normal);    //
                                                    wifiSocket.WIFI_Write(string1quan);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(string1);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(right);    //
                                                    wifiSocket.WIFI_Write(pricee);
                                                    wifiSocket.WIFI_Write(HT1);    //
                                                    wifiSocket.WIFI_Write(ss1);
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
                                if (value.length() <= quanlentha && name.length() > charlength) {
//                                    Toast.makeText(Preparing_Orders_w.this, "3", Toast.LENGTH_SHORT).show();
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength);
                                    allbufitems = new byte[][]{
                                            setHT34, normal, value.getBytes(), HT, string1.getBytes(), HT, pricee.getBytes(), HT1, ss1.getBytes(), LF, "      ".getBytes(), string2.getBytes(), LF
                                    };
                                    if (print1 == 0){
                                        if (statussusbs.equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(setHT34);    //
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write(value);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write(string1);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BluetoothPrintDriver.BT_Write(right);    //
                                            BT_Write(pricee);
                                            BluetoothPrintDriver.BT_Write(HT1);    //
                                            BT_Write(ss1);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            BT_Write("      ");
                                            BT_Write(string2);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            print1 = 1;
                                        } else {
                                            if (statusnets_counter.equals("ok")) {
                                                wifiSocket2.WIFI_Write(setHT34);    //
                                                wifiSocket2.WIFI_Write(normal);    //
                                                wifiSocket2.WIFI_Write(value);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(string1);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(right);    //
                                                wifiSocket2.WIFI_Write(pricee);
                                                wifiSocket2.WIFI_Write(HT1);    //
                                                wifiSocket2.WIFI_Write(ss1);
                                                wifiSocket2.WIFI_Write(LF);    //
                                                wifiSocket2.WIFI_Write("      ");
                                                wifiSocket2.WIFI_Write(string2);
                                                wifiSocket2.WIFI_Write(LF);    //
                                                print1 = 1;
                                            }else {
                                                if (statusnets.equals("ok")) {
                                                    wifiSocket.WIFI_Write(setHT34);    //
                                                    wifiSocket.WIFI_Write(normal);    //
                                                    wifiSocket.WIFI_Write(value);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(string1);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(right);    //
                                                    wifiSocket.WIFI_Write(pricee);
                                                    wifiSocket.WIFI_Write(HT1);    //
                                                    wifiSocket.WIFI_Write(ss1);
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

                                if (value.length() > quanlentha && name.length() > charlength1) {
//                                    Toast.makeText(Preparing_Orders_w.this, "4", Toast.LENGTH_SHORT).show();
                                    String string1quan = value.substring(0, quanlentha);
                                    String string2quan = value.substring(quanlentha);
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength, charlength1);
                                    String string3 = name.substring(charlength1);
                                    allbufitems = new byte[][]{
                                            setHT34, normal, string1quan.getBytes(), HT, string1.getBytes(), HT, pricee.getBytes(), HT1, ss1.getBytes(), LF, string2quan.getBytes(), HT, string2.getBytes(), LF, "      ".getBytes(), string3.getBytes(), left, LF,
                                    };
                                    if (print1 == 0){
                                        if (statussusbs.equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(setHT34);    //
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write(string1quan);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write(string1);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BluetoothPrintDriver.BT_Write(right);    //
                                            BT_Write(pricee);
                                            BluetoothPrintDriver.BT_Write(HT1);    //
                                            BT_Write(ss1);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            BT_Write(string2quan);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write(string2);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            BT_Write("      ");
                                            BT_Write(string3);
                                            BluetoothPrintDriver.BT_Write(right);    //
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            print1 = 1;
                                        } else {
                                            if (statusnets_counter.equals("ok")) {
                                                wifiSocket2.WIFI_Write(setHT34);    //
                                                wifiSocket2.WIFI_Write(normal);    //
                                                wifiSocket2.WIFI_Write(string1quan);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(string1);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(right);    //
                                                wifiSocket2.WIFI_Write(pricee);
                                                wifiSocket2.WIFI_Write(HT1);    //
                                                wifiSocket2.WIFI_Write(ss1);
                                                wifiSocket2.WIFI_Write(LF);    //
                                                wifiSocket2.WIFI_Write(string2quan);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(string2);
                                                wifiSocket2.WIFI_Write(LF);    //
                                                wifiSocket2.WIFI_Write("      ");
                                                wifiSocket2.WIFI_Write(string3);
                                                wifiSocket2.WIFI_Write(right);    //
                                                wifiSocket2.WIFI_Write(LF);    //
                                                print1 = 1;
                                            }else {
                                                if (statusnets.equals("ok")) {
                                                    wifiSocket.WIFI_Write(setHT34);    //
                                                    wifiSocket.WIFI_Write(normal);    //
                                                    wifiSocket.WIFI_Write(string1quan);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(string1);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(right);    //
                                                    wifiSocket.WIFI_Write(pricee);
                                                    wifiSocket.WIFI_Write(HT1);    //
                                                    wifiSocket.WIFI_Write(ss1);
                                                    wifiSocket.WIFI_Write(LF);    //
                                                    wifiSocket.WIFI_Write(string2quan);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(string2);
                                                    wifiSocket.WIFI_Write(LF);    //
                                                    wifiSocket.WIFI_Write("      ");
                                                    wifiSocket.WIFI_Write(string3);
                                                    wifiSocket.WIFI_Write(right);    //
                                                    wifiSocket.WIFI_Write(LF);    //
                                                    print1 = 1;
                                                }
                                            }
                                        }
                                    }

                                }
                                if (value.length() <= quanlentha && name.length() > charlength1) {
//                                    Toast.makeText(Preparing_Orders_w.this, "5", Toast.LENGTH_SHORT).show();
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength, charlength1);
                                    String string3 = name.substring(charlength1);
                                    allbufitems = new byte[][]{
                                            setHT34, normal, value.getBytes(), HT, string1.getBytes(), HT, pricee.getBytes(), HT1, ss1.getBytes(), LF, "      ".getBytes(), string2.getBytes(), LF, "      ".getBytes(), string3.getBytes(), left, LF,
                                    };

                                    if(print1 == 0){
                                        if (statussusbs.equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(setHT34);    //
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write(value);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write(string1);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BluetoothPrintDriver.BT_Write(right);    //
                                            BT_Write(pricee);
                                            BluetoothPrintDriver.BT_Write(HT1);    //
                                            BT_Write(ss1);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            BT_Write("      ");
                                            BT_Write(string2);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            BT_Write("      ");
                                            BT_Write(string3);
                                            BluetoothPrintDriver.BT_Write(right);    //
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            print1 = 1;
                                        } else {
                                            if (statusnets_counter.equals("ok")) {
                                                wifiSocket2.WIFI_Write(setHT34);    //
                                                wifiSocket2.WIFI_Write(normal);    //
                                                wifiSocket2.WIFI_Write(value);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(string1);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(right);    //
                                                wifiSocket2.WIFI_Write(pricee);
                                                wifiSocket2.WIFI_Write(HT1);    //
                                                wifiSocket2.WIFI_Write(ss1);
                                                wifiSocket2.WIFI_Write(LF);    //
                                                wifiSocket2.WIFI_Write("      ");
                                                wifiSocket2.WIFI_Write(string2);
                                                wifiSocket2.WIFI_Write(LF);    //
                                                wifiSocket2.WIFI_Write("      ");
                                                wifiSocket2.WIFI_Write(string3);
                                                wifiSocket2.WIFI_Write(right);    //
                                                wifiSocket2.WIFI_Write(LF);    //
                                                print1 = 1;
                                            }else {
                                                if (statusnets.equals("ok")) {
                                                    wifiSocket.WIFI_Write(setHT34);    //
                                                    wifiSocket.WIFI_Write(normal);    //
                                                    wifiSocket.WIFI_Write(value);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(string1);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(right);    //
                                                    wifiSocket.WIFI_Write(pricee);
                                                    wifiSocket.WIFI_Write(HT1);    //
                                                    wifiSocket.WIFI_Write(ss1);
                                                    wifiSocket.WIFI_Write(LF);    //
                                                    wifiSocket.WIFI_Write("      ");
                                                    wifiSocket.WIFI_Write(string2);
                                                    wifiSocket.WIFI_Write(LF);    //
                                                    wifiSocket.WIFI_Write("      ");
                                                    wifiSocket.WIFI_Write(string3);
                                                    wifiSocket.WIFI_Write(right);    //
                                                    wifiSocket.WIFI_Write(LF);    //
                                                    print1 = 1;
                                                }
                                            }
                                        }
                                    }

                                }

                                Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                if (ccursor.moveToFirst()) {
                                    String hsn = ccursor.getString(34);

                                    TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                    hsn_hsn.setText(hsn);

                                    if (hsn_hsn.getText().toString().equals("")) {
                                    } else {
                                        allbuftaxestype1 = new byte[][]{
                                                left, normal, hsn.getBytes(), HT, LF
                                        };
                                        if (statussusbs.equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(left);    //
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write("HSN "+hsn);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        } else {
                                            if (statusnets_counter.equals("ok")) {
                                                wifiSocket2.WIFI_Write(left);    //
                                                wifiSocket2.WIFI_Write(normal);    //
                                                wifiSocket2.WIFI_Write("HSN "+hsn);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(LF);    //
                                            }else {
                                                if (statusnets.equals("ok")) {
                                                    wifiSocket.WIFI_Write(left);    //
                                                    wifiSocket.WIFI_Write(normal);    //
                                                    wifiSocket.WIFI_Write("HSN "+hsn);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(LF);    //
                                                }
                                            }
                                        }
                                    }
                                }

                            } else {
//                                Toast.makeText(Preparing_Orders_w.this, "6", Toast.LENGTH_SHORT).show();
                                if (value.length() > quanlentha) {
//                                    Toast.makeText(Preparing_Orders_w.this, "7", Toast.LENGTH_SHORT).show();
                                    String string1quan = value.substring(0, quanlentha);
                                    String string2quan = value.substring(quanlentha);
                                    allbufitems = new byte[][]{
                                            setHT34, normal, string1quan.getBytes(), HT, name.getBytes(), HT, pricee.getBytes(), HT1, ss1.getBytes(), left, LF, string2quan.getBytes(), LF
                                    };
                                    if (statussusbs.equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(setHT34);    //
                                        BluetoothPrintDriver.BT_Write(normal);    //
                                        BT_Write(string1quan);
                                        BluetoothPrintDriver.BT_Write(HT);    //
                                        BT_Write(name);
                                        BluetoothPrintDriver.BT_Write(HT);    //
                                        BluetoothPrintDriver.BT_Write(right);    //
                                        BT_Write(pricee);
                                        BluetoothPrintDriver.BT_Write(HT1);    //
                                        BT_Write(ss1);
                                        BluetoothPrintDriver.BT_Write(right);    //
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                        BT_Write(string2quan);
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    } else {
                                        if (statusnets_counter.equals("ok")) {
                                            wifiSocket2.WIFI_Write(setHT34);    //
                                            wifiSocket2.WIFI_Write(normal);    //
                                            wifiSocket2.WIFI_Write(string1quan);
                                            wifiSocket2.WIFI_Write(HT);    //
                                            wifiSocket2.WIFI_Write(name);
                                            wifiSocket2.WIFI_Write(HT);    //
                                            wifiSocket2.WIFI_Write(right);    //
                                            wifiSocket2.WIFI_Write(pricee);
                                            wifiSocket2.WIFI_Write(HT1);    //
                                            wifiSocket2.WIFI_Write(ss1);
                                            wifiSocket2.WIFI_Write(right);    //
                                            wifiSocket2.WIFI_Write(LF);    //
                                            wifiSocket2.WIFI_Write(string2quan);
                                            wifiSocket2.WIFI_Write(LF);    //
                                        }else {
                                            if (statusnets.equals("ok")) {
                                                wifiSocket.WIFI_Write(setHT34);    //
                                                wifiSocket.WIFI_Write(normal);    //
                                                wifiSocket.WIFI_Write(string1quan);
                                                wifiSocket.WIFI_Write(HT);    //
                                                wifiSocket.WIFI_Write(name);
                                                wifiSocket.WIFI_Write(HT);    //
                                                wifiSocket.WIFI_Write(right);    //
                                                wifiSocket.WIFI_Write(pricee);
                                                wifiSocket.WIFI_Write(HT1);    //
                                                wifiSocket.WIFI_Write(ss1);
                                                wifiSocket.WIFI_Write(right);    //
                                                wifiSocket.WIFI_Write(LF);    //
                                                wifiSocket.WIFI_Write(string2quan);
                                                wifiSocket.WIFI_Write(LF);    //
                                            }
                                        }
                                    }

                                    Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                    if (ccursor.moveToFirst()) {
                                        String hsn = ccursor.getString(34);

                                        TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                        hsn_hsn.setText(hsn);

                                        if (hsn_hsn.getText().toString().equals("")) {
                                        } else {
                                            allbuftaxestype1 = new byte[][]{
                                                    left, normal, hsn.getBytes(), HT, LF
                                            };
                                            if (statussusbs.equals("ok")) {
                                                BluetoothPrintDriver.BT_Write(left);    //
                                                BluetoothPrintDriver.BT_Write(normal);    //
                                                BT_Write("HSN "+hsn);
                                                BluetoothPrintDriver.BT_Write(HT);    //
                                                BluetoothPrintDriver.BT_Write(LF);    //
                                            } else {
                                                if (statusnets_counter.equals("ok")) {
                                                    wifiSocket2.WIFI_Write(left);    //
                                                    wifiSocket2.WIFI_Write(normal);    //
                                                    wifiSocket2.WIFI_Write("HSN "+hsn);
                                                    wifiSocket2.WIFI_Write(HT);    //
                                                    wifiSocket2.WIFI_Write(LF);    //
                                                }else {
                                                    if (statusnets.equals("ok")) {
                                                        wifiSocket.WIFI_Write(left);    //
                                                        wifiSocket.WIFI_Write(normal);    //
                                                        wifiSocket.WIFI_Write("HSN "+hsn);
                                                        wifiSocket.WIFI_Write(HT);    //
                                                        wifiSocket.WIFI_Write(LF);    //
                                                    }
                                                }
                                            }
                                        }
                                    }

                                } else {
//                                    Toast.makeText(Preparing_Orders_w.this, "8", Toast.LENGTH_SHORT).show();
                                    allbufitems = new byte[][]{
                                            setHT34, normal, value.getBytes(), HT, name.getBytes(), HT, pricee.getBytes(), HT1, ss1.getBytes(), left, LF,
                                    };
                                    if (statussusbs.equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(setHT34);    //
                                        BluetoothPrintDriver.BT_Write(normal);    //
                                        BT_Write(value);
                                        BluetoothPrintDriver.BT_Write(HT);    //
                                        BT_Write(name);
                                        BluetoothPrintDriver.BT_Write(HT);    //
                                        BluetoothPrintDriver.BT_Write(right);    //
                                        BT_Write(pricee);
                                        BluetoothPrintDriver.BT_Write(HT1);    //
                                        BT_Write(ss1);
                                        BluetoothPrintDriver.BT_Write(right);    //
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    } else {
                                        if (statusnets_counter.equals("ok")) {
                                            wifiSocket2.WIFI_Write(setHT34);    //
                                            wifiSocket2.WIFI_Write(normal);    //
                                            wifiSocket2.WIFI_Write(value);
                                            wifiSocket2.WIFI_Write(HT);    //
                                            wifiSocket2.WIFI_Write(name);
                                            wifiSocket2.WIFI_Write(HT);    //
                                            wifiSocket2.WIFI_Write(right);    //
                                            wifiSocket2.WIFI_Write(pricee);
                                            wifiSocket2.WIFI_Write(HT1);    //
                                            wifiSocket2.WIFI_Write(ss1);
                                            wifiSocket2.WIFI_Write(right);    //
                                            wifiSocket2.WIFI_Write(LF);    //
                                        }else {
                                            if (statusnets.equals("ok")) {
                                                wifiSocket.WIFI_Write(setHT34);    //
                                                wifiSocket.WIFI_Write(normal);    //
                                                wifiSocket.WIFI_Write(value);
                                                wifiSocket.WIFI_Write(HT);    //
                                                wifiSocket.WIFI_Write(name);
                                                wifiSocket.WIFI_Write(HT);    //
                                                wifiSocket.WIFI_Write(right);    //
                                                wifiSocket.WIFI_Write(pricee);
                                                wifiSocket.WIFI_Write(HT1);    //
                                                wifiSocket.WIFI_Write(ss1);
                                                wifiSocket.WIFI_Write(right);    //
                                                wifiSocket.WIFI_Write(LF);    //
                                            }
                                        }
                                    }

                                    Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                    if (ccursor.moveToFirst()) {
                                        String hsn = ccursor.getString(34);

                                        TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                        hsn_hsn.setText(hsn);

                                        if (hsn_hsn.getText().toString().equals("")) {
                                        } else {
                                            allbuftaxestype1 = new byte[][]{
                                                    left, normal, hsn.getBytes(), HT, LF
                                            };
                                            if (statussusbs.equals("ok")) {
                                                BluetoothPrintDriver.BT_Write(left);    //
                                                BluetoothPrintDriver.BT_Write(normal);    //
                                                BT_Write("HSN "+hsn);
                                                BluetoothPrintDriver.BT_Write(HT);    //
                                                BluetoothPrintDriver.BT_Write(LF);    //
                                            } else {
                                                if (statusnets_counter.equals("ok")) {
                                                    wifiSocket2.WIFI_Write(left);    //
                                                    wifiSocket2.WIFI_Write(normal);    //
                                                    wifiSocket2.WIFI_Write("HSN "+hsn);
                                                    wifiSocket2.WIFI_Write(HT);    //
                                                    wifiSocket2.WIFI_Write(LF);    //
                                                }else {
                                                    if (statusnets.equals("ok")) {
                                                        wifiSocket.WIFI_Write(left);    //
                                                        wifiSocket.WIFI_Write(normal);    //
                                                        wifiSocket.WIFI_Write("HSN "+hsn);
                                                        wifiSocket.WIFI_Write(HT);    //
                                                        wifiSocket.WIFI_Write(LF);    //
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }

                            }

                            do {

                                final String modiname = modcursor.getString(1);
                                final String modiquan = modcursor.getString(2);
                                String modiprice = modcursor.getString(3);
                                String moditotal = modcursor.getString(4);
                                final String modiid = modcursor.getString(0);

                                float modprice1 = Float.parseFloat(modiprice);
                                String modpricestr = String.valueOf(modprice1);

                                if (modiname.length() > charlength) {
                                    if (modiname.length() > charlength) {
                                        String string1 = modiname.substring(0, charlength);
                                        String string2 = modiname.substring(charlength);
                                        allbufmodifiers = new byte[][]{
                                                setHT34, normal, "".getBytes(), HT, ">".getBytes(), string1.getBytes(), HT, modpricestr.getBytes(), HT1, "".getBytes(), LF, "    ".getBytes(), string2.getBytes(), LF
                                                //setHT34, normal,total.getBytes(),HT,modiname.getBytes(),HT, modiprice.getBytes(),HT, "125.0".getBytes(),LF

                                        };
                                        if (statussusbs.equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(setHT34);    //
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write("");
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write(">");
                                            BT_Write(string1);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BluetoothPrintDriver.BT_Write(right);    //
                                            BT_Write(modpricestr);
                                            BluetoothPrintDriver.BT_Write(HT1);    //
                                            BT_Write("");
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            BT_Write("    ");
                                            BT_Write(string2);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        } else {
                                            if (statusnets_counter.equals("ok")) {
                                                wifiSocket2.WIFI_Write(setHT34);    //
                                                wifiSocket2.WIFI_Write(normal);    //
                                                wifiSocket2.WIFI_Write("");
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(">");
                                                wifiSocket2.WIFI_Write(string1);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(right);    //
                                                wifiSocket2.WIFI_Write(modpricestr);
                                                wifiSocket2.WIFI_Write(HT1);    //
                                                wifiSocket2.WIFI_Write("");
                                                wifiSocket2.WIFI_Write(LF);    //
                                                wifiSocket2.WIFI_Write("    ");
                                                wifiSocket2.WIFI_Write(string2);
                                                wifiSocket2.WIFI_Write(LF);    //
                                            }else {
                                                if (statusnets.equals("ok")) {
                                                    wifiSocket.WIFI_Write(setHT34);    //
                                                    wifiSocket.WIFI_Write(normal);    //
                                                    wifiSocket.WIFI_Write("");
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(">");
                                                    wifiSocket.WIFI_Write(string1);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(right);    //
                                                    wifiSocket.WIFI_Write(modpricestr);
                                                    wifiSocket.WIFI_Write(HT1);    //
                                                    wifiSocket.WIFI_Write("");
                                                    wifiSocket.WIFI_Write(LF);    //
                                                    wifiSocket.WIFI_Write("    ");
                                                    wifiSocket.WIFI_Write(string2);
                                                    wifiSocket.WIFI_Write(LF);    //
                                                }
                                            }
                                        }
                                    }
                                    if (modiname.length() > charlength1) {
                                        String string1 = modiname.substring(0, charlength);
                                        String string2 = modiname.substring(charlength, charlength1);
                                        String string3 = modiname.substring(charlength1);
                                        allbufmodifiers = new byte[][]{
                                                setHT34, normal, "".getBytes(), HT, ">".getBytes(), string1.getBytes(), HT, modpricestr.getBytes(), HT1, "".getBytes(), LF, "    ".getBytes(), string2.getBytes(), LF, "    ".getBytes(), string3.getBytes(), left, LF
                                                //setHT34, normal,total.getBytes(),HT,modiname.getBytes(),HT, modiprice.getBytes(),HT, "125.0".getBytes(),LF

                                        };
                                        if (statussusbs.equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(setHT34);    //
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write("");
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write(">");
                                            BT_Write(string1);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write(modpricestr);
                                            BluetoothPrintDriver.BT_Write(HT1);    //
                                            BT_Write("");
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            BT_Write("    ");
                                            BT_Write(string2);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            BT_Write("    ");
                                            BT_Write(string3);
                                            BluetoothPrintDriver.BT_Write(left);    //
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        } else {
                                            if (statusnets_counter.equals("ok")) {
                                                wifiSocket2.WIFI_Write(setHT34);    //
                                                wifiSocket2.WIFI_Write(normal);    //
                                                wifiSocket2.WIFI_Write("");
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(">");
                                                wifiSocket2.WIFI_Write(string1);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(modpricestr);
                                                wifiSocket2.WIFI_Write(HT1);    //
                                                wifiSocket2.WIFI_Write("");
                                                wifiSocket2.WIFI_Write(LF);    //
                                                wifiSocket2.WIFI_Write("    ");
                                                wifiSocket2.WIFI_Write(string2);
                                                wifiSocket2.WIFI_Write(LF);    //
                                                wifiSocket2.WIFI_Write("    ");
                                                wifiSocket2.WIFI_Write(string3);
                                                wifiSocket2.WIFI_Write(left);    //
                                                wifiSocket2.WIFI_Write(LF);    //
                                            }else {
                                                if (statusnets.equals("ok")) {
                                                    wifiSocket.WIFI_Write(setHT34);    //
                                                    wifiSocket.WIFI_Write(normal);    //
                                                    wifiSocket.WIFI_Write("");
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(">");
                                                    wifiSocket.WIFI_Write(string1);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(modpricestr);
                                                    wifiSocket.WIFI_Write(HT1);    //
                                                    wifiSocket.WIFI_Write("");
                                                    wifiSocket.WIFI_Write(LF);    //
                                                    wifiSocket.WIFI_Write("    ");
                                                    wifiSocket.WIFI_Write(string2);
                                                    wifiSocket.WIFI_Write(LF);    //
                                                    wifiSocket.WIFI_Write("    ");
                                                    wifiSocket.WIFI_Write(string3);
                                                    wifiSocket.WIFI_Write(left);    //
                                                    wifiSocket.WIFI_Write(LF);    //
                                                }
                                            }
                                        }
                                    }

                                    Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                    if (ccursor.moveToFirst()) {
                                        String hsn = ccursor.getString(34);

                                        TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                        hsn_hsn.setText(hsn);

                                        if (hsn_hsn.getText().toString().equals("")) {
                                        } else {
                                            allbuftaxestype1 = new byte[][]{
                                                    left, normal, hsn.getBytes(), HT, LF
                                            };
                                            if (statussusbs.equals("ok")) {
                                                BluetoothPrintDriver.BT_Write(left);    //
                                                BluetoothPrintDriver.BT_Write(normal);    //
                                                BT_Write("HSN "+hsn);
                                                BluetoothPrintDriver.BT_Write(HT);    //
                                                BluetoothPrintDriver.BT_Write(LF);    //
                                            } else {
                                                if (statusnets_counter.equals("ok")) {
                                                    wifiSocket2.WIFI_Write(left);    //
                                                    wifiSocket2.WIFI_Write(normal);    //
                                                    wifiSocket2.WIFI_Write("HSN "+hsn);
                                                    wifiSocket2.WIFI_Write(HT);    //
                                                    wifiSocket2.WIFI_Write(LF);    //
                                                }else {
                                                    if (statusnets.equals("ok")) {
                                                        wifiSocket.WIFI_Write(left);    //
                                                        wifiSocket.WIFI_Write(normal);    //
                                                        wifiSocket.WIFI_Write("HSN "+hsn);
                                                        wifiSocket.WIFI_Write(HT);    //
                                                        wifiSocket.WIFI_Write(LF);    //
                                                    }
                                                }
                                            }
                                        }
                                    }

                                } else {
                                    allbufmodifiers = new byte[][]{
                                            setHT34, normal, "".getBytes(), HT, ">".getBytes(), modiname.getBytes(), HT, modpricestr.getBytes(), HT1, "".getBytes(), left, LF
                                            //setHT34, normal,total.getBytes(),HT,modiname.getBytes(),HT, modiprice.getBytes(),HT, "125.0".getBytes(),LF

                                    };
                                    //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                                    if (statussusbs.equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(setHT34);    //
                                        BluetoothPrintDriver.BT_Write(normal);    //
                                        BT_Write("");
                                        BluetoothPrintDriver.BT_Write(HT);    //
                                        BT_Write(">");
                                        BT_Write(modiname);
                                        BluetoothPrintDriver.BT_Write(HT);    //
                                        BluetoothPrintDriver.BT_Write(right);    //
                                        BT_Write(modpricestr);
                                        BluetoothPrintDriver.BT_Write(HT1);    //
                                        BT_Write("");
                                        BluetoothPrintDriver.BT_Write(right);    //
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    } else {
                                        if (statusnets_counter.equals("ok")) {
                                            wifiSocket2.WIFI_Write(setHT34);    //
                                            wifiSocket2.WIFI_Write(normal);    //
                                            wifiSocket2.WIFI_Write("");
                                            wifiSocket2.WIFI_Write(HT);    //
                                            wifiSocket2.WIFI_Write(">");
                                            wifiSocket2.WIFI_Write(modiname);
                                            wifiSocket2.WIFI_Write(HT);    //
                                            wifiSocket2.WIFI_Write(right);    //
                                            wifiSocket2.WIFI_Write(modpricestr);
                                            wifiSocket2.WIFI_Write(HT1);    //
                                            wifiSocket2.WIFI_Write("");
                                            wifiSocket2.WIFI_Write(right);    //
                                            wifiSocket2.WIFI_Write(LF);    //
                                        }else {
                                            if (statusnets.equals("ok")) {
                                                wifiSocket.WIFI_Write(setHT34);    //
                                                wifiSocket.WIFI_Write(normal);    //
                                                wifiSocket.WIFI_Write("");
                                                wifiSocket.WIFI_Write(HT);    //
                                                wifiSocket.WIFI_Write(">");
                                                wifiSocket.WIFI_Write(modiname);
                                                wifiSocket.WIFI_Write(HT);    //
                                                wifiSocket.WIFI_Write(right);    //
                                                wifiSocket.WIFI_Write(modpricestr);
                                                wifiSocket.WIFI_Write(HT1);    //
                                                wifiSocket.WIFI_Write("");
                                                wifiSocket.WIFI_Write(right);    //
                                                wifiSocket.WIFI_Write(LF);    //
                                            }
                                        }
                                    }

                                    Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                    if (ccursor.moveToFirst()) {
                                        String hsn = ccursor.getString(34);

                                        TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                        hsn_hsn.setText(hsn);

                                        if (hsn_hsn.getText().toString().equals("")) {
                                        } else {
                                            allbuftaxestype1 = new byte[][]{
                                                    left, normal, hsn.getBytes(), HT, LF
                                            };
                                            if (statussusbs.equals("ok")) {
                                                BluetoothPrintDriver.BT_Write(left);    //
                                                BluetoothPrintDriver.BT_Write(normal);    //
                                                BT_Write("HSN "+hsn);
                                                BluetoothPrintDriver.BT_Write(HT);    //
                                                BluetoothPrintDriver.BT_Write(LF);    //
                                            } else {
                                                if (statusnets_counter.equals("ok")) {
                                                    wifiSocket2.WIFI_Write(left);    //
                                                    wifiSocket2.WIFI_Write(normal);    //
                                                    wifiSocket2.WIFI_Write("HSN "+hsn);
                                                    wifiSocket2.WIFI_Write(HT);    //
                                                    wifiSocket2.WIFI_Write(LF);    //
                                                }else {
                                                    if (statusnets.equals("ok")) {
                                                        wifiSocket.WIFI_Write(left);    //
                                                        wifiSocket.WIFI_Write(normal);    //
                                                        wifiSocket.WIFI_Write("HSN "+hsn);
                                                        wifiSocket.WIFI_Write(HT);    //
                                                        wifiSocket.WIFI_Write(LF);    //
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }

                                final TableRow tableRow11 = new TableRow(Preparing_Orders_w.this);
                                tableRow11.setLayoutParams(new TableLayout.LayoutParams(
                                        TableRow.LayoutParams.MATCH_PARENT,
                                        TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                                final TextView tvv = new TextView(Preparing_Orders_w.this);
                                // tv.setLayoutParams(new TableRow.LayoutParams(40, TableRow.LayoutParams.MATCH_PARENT));
                                tvv.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 0.70f));
                                //tv.setBackgroundResource(R.drawable.cell_shape);
                                //tv.setGravity(Gravity.CENTER);
                                tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                                tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                                tvv.setText("");
                                tableRow11.addView(tvv);

                                TextView tv4 = new TextView(Preparing_Orders_w.this);
                                //lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                                tv4.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.6f));
                                //tv3.setPadding(5, 0, 0, 0);
                                //tv.setBackgroundResource(R.drawable.cell_shape);
                                tv4.setText(modiname);
                                tv4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                                tv4.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                                tv4.setGravity(Gravity.CENTER_VERTICAL);
                                //tv3.setBackgroundColor(getResources().getColor(R.color.six));
                                //tv3.setTextColor(R.color.black);
                                tableRow11.addView(tv4);

                                TextView tv5 = new TextView(Preparing_Orders_w.this);
                                //lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                                tv5.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.0f));
                                //tv3.setPadding(5, 0, 0, 0);
                                //tv.setBackgroundResource(R.drawable.cell_shape);
                                tv5.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                                tv5.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                                //tv2.setPadding(0, 0, 1, 0);
                                tv5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                                tv5.setText(modiprice);
                                //tv3.setBackgroundColor(getResources().getColor(R.color.six));
                                //tv3.setTextColor(R.color.black);
                                tableRow11.addView(tv5);

                                TextView tv6 = new TextView(Preparing_Orders_w.this);
                                //lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                                tv6.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                                //tv3.setPadding(5, 0, 0, 0);
                                tv6.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                                tv6.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                                tv6.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                                //tv.setBackgroundResource(R.drawable.cell_shape);
                                tv6.setText("");
                                //tv3.setBackgroundColor(getResources().getColor(R.color.six));
                                //tv3.setTextColor(R.color.black);
                                tableRow11.addView(tv6);


                                final TextView tv7 = new TextView(Preparing_Orders_w.this);
                                //tv3.setLayoutParams(new TableRow.LayoutParams(90, TableRow.LayoutParams.MATCH_PARENT));
                                tv7.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                                tv7.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                                tv7.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                                //tv3.setPadding(0,0,10,0);
                                tv7.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                                //String modtotal = String.valueOf(Integer.parseInt(modiquan) * Integer.parseInt(modiprice));

                                final String number = tv.getText().toString();
                                float newmul = Float.parseFloat(number);
                                //final float in = Float.parseFloat(cursor.getString(4));
                                String multiply = String.valueOf(newmul * Float.parseFloat(pricee));
                                //newmul = Integer.parseInt(multiply);
                                //tv3.setText(String.valueOf(Float.parseFloat(multiply)+Float.parseFloat(modtotal)));
                                //row.addView(tv3);

                                row.removeView(tv8);


                                tv8 = new TextView(Preparing_Orders_w.this);
                                tv8.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                                //tv3.setLayoutParams(new TableRow.LayoutParams(90, TableRow.LayoutParams.MATCH_PARENT));
                                //tv3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                                //tv.setBackgroundResource(R.drawable.cell_shape);
                                tv8.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                                //tv3.setPadding(0, 0, 10, 0);
                                tv8.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                                tv8.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                                final String numberr = tv.getText().toString();
                                float newmulr = Float.parseFloat(numberr);
                                //final float in = Float.parseFloat(cursor.getString(4));
                                String multiplyr = String.valueOf(newmulr * Float.parseFloat(pricee));
                                //newmul = Integer.parseInt(multiply);
                                tv8.setText(String.valueOf(ss));
                                row.addView(tv8);


                                tableLayout1.addView(tableRow11);
                            } while (modcursor.moveToNext());

                            //Cursor modcursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND parent = '" + name + "' AND parentid = '" + newid + "' ", null);
                            Cursor disc_cursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '" + name + "' AND _id = '" + nbg + "'  ", null);
                            if (disc_cursor.moveToFirst()) {
                                do {
                                    String disc_vv = disc_cursor.getString(12);
                                    String disc_there = disc_cursor.getString(30);
                                    float vtq = disc_cursor.getFloat(31);
                                    if (disc_there.equals("no")) {

                                    } else {

                                        total_disc_print_q = String.valueOf(vtq);

                                        allbufrounded = new byte[][]{
                                                setHT34, normal, "".getBytes(), HT, "".getBytes(), HT, "".getBytes(), HT1, "(".getBytes(), "-".getBytes(), total_disc_print_q.getBytes(), ")".getBytes(), left, LF,
                                        };

                                        if (statussusbs.equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(setHT34);    //
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write("");
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write("");
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write("");
                                            BluetoothPrintDriver.BT_Write(HT1);    //
                                            BT_Write("(");
                                            BT_Write("-");
                                            BT_Write(total_disc_print_q);
                                            BT_Write(")");
                                            BluetoothPrintDriver.BT_Write(left);    //
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        } else {
                                            if (statusnets_counter.equals("ok")) {
                                                wifiSocket2.WIFI_Write(setHT34);    //
                                                wifiSocket2.WIFI_Write(normal);    //
                                                wifiSocket2.WIFI_Write("");
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write("");
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write("");
                                                wifiSocket2.WIFI_Write(HT1);    //
                                                wifiSocket2.WIFI_Write("(");
                                                wifiSocket2.WIFI_Write("-");
                                                wifiSocket2.WIFI_Write(total_disc_print_q);
                                                wifiSocket2.WIFI_Write(")");
                                                wifiSocket2.WIFI_Write(left);    //
                                                wifiSocket2.WIFI_Write(LF);    //
                                            }else {
                                                if (statusnets.equals("ok")) {
                                                    wifiSocket.WIFI_Write(setHT34);    //
                                                    wifiSocket.WIFI_Write(normal);    //
                                                    wifiSocket.WIFI_Write("");
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write("");
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write("");
                                                    wifiSocket.WIFI_Write(HT1);    //
                                                    wifiSocket.WIFI_Write("(");
                                                    wifiSocket.WIFI_Write("-");
                                                    wifiSocket.WIFI_Write(total_disc_print_q);
                                                    wifiSocket.WIFI_Write(")");
                                                    wifiSocket.WIFI_Write(left);    //
                                                    wifiSocket.WIFI_Write(LF);    //
                                                }
                                            }
                                        }
                                    }
                                } while (disc_cursor.moveToNext());
                            }
                            disc_cursor.close();

                        } else {

                            if (name.length() > charlength) {
//                                Toast.makeText(Preparing_Orders_w.this, "10", Toast.LENGTH_SHORT).show();
                                int print1 = 0;
                                if (value.length() > quanlentha && name.length() > charlength) {
//                                    Toast.makeText(Preparing_Orders_w.this, "11", Toast.LENGTH_SHORT).show();
                                    String string1quan = value.substring(0, quanlentha);
                                    String string2quan = value.substring(quanlentha);
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength);
                                    allbufitems = new byte[][]{
                                            setHT34, normal, string1quan.getBytes(), HT, string1.getBytes(), HT, pricee.getBytes(), HT1, tototot.getBytes(), LF, string2quan.getBytes(), HT, string2.getBytes(), LF
                                    };
                                    if (print1 == 0) {
                                        if (statussusbs.equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(setHT34);    //
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write(string1quan);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write(string1);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BluetoothPrintDriver.BT_Write(right);    //
                                            BT_Write(pricee);
                                            BluetoothPrintDriver.BT_Write(HT1);    //
                                            BT_Write(tototot);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            BT_Write(string2quan);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write(string2);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            print1 = 1;
                                        } else {
                                            if (statusnets_counter.equals("ok")) {
                                                wifiSocket2.WIFI_Write(setHT34);    //
                                                wifiSocket2.WIFI_Write(normal);    //
                                                wifiSocket2.WIFI_Write(string1quan);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(string1);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(right);    //
                                                wifiSocket2.WIFI_Write(pricee);
                                                wifiSocket2.WIFI_Write(HT1);    //
                                                wifiSocket2.WIFI_Write(tototot);
                                                wifiSocket2.WIFI_Write(LF);    //
                                                wifiSocket2.WIFI_Write(string2quan);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(string2);
                                                wifiSocket2.WIFI_Write(LF);    //
                                                print1 = 1;
                                            }else {
                                                if (statusnets.equals("ok")) {
                                                    wifiSocket.WIFI_Write(setHT34);    //
                                                    wifiSocket.WIFI_Write(normal);    //
                                                    wifiSocket.WIFI_Write(string1quan);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(string1);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(right);    //
                                                    wifiSocket.WIFI_Write(pricee);
                                                    wifiSocket.WIFI_Write(HT1);    //
                                                    wifiSocket.WIFI_Write(tototot);
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
                                if (value.length() <= quanlentha && name.length() > charlength) {
//                                    Toast.makeText(Preparing_Orders_w.this, "12", Toast.LENGTH_SHORT).show();
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength);
                                    allbufitems = new byte[][]{
                                            setHT34, normal, value.getBytes(), HT, string1.getBytes(), HT, pricee.getBytes(), HT1, tototot.getBytes(), LF, "      ".getBytes(), string2.getBytes(), LF
                                    };
                                    if (print1 == 0) {
                                        if (statussusbs.equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(setHT34);    //
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write(value);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write(string1);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BluetoothPrintDriver.BT_Write(right);    //
                                            BT_Write(pricee);
                                            BluetoothPrintDriver.BT_Write(HT1);    //
                                            BT_Write(tototot);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            BT_Write("      ");
                                            BT_Write(string2);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            print1 = 1;
                                        } else {
                                            if (statusnets_counter.equals("ok")) {
                                                wifiSocket2.WIFI_Write(setHT34);    //
                                                wifiSocket2.WIFI_Write(normal);    //
                                                wifiSocket2.WIFI_Write(value);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(string1);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(right);    //
                                                wifiSocket2.WIFI_Write(pricee);
                                                wifiSocket2.WIFI_Write(HT1);    //
                                                wifiSocket2.WIFI_Write(tototot);
                                                wifiSocket2.WIFI_Write(LF);    //
                                                wifiSocket2.WIFI_Write("      ");
                                                wifiSocket2.WIFI_Write(string2);
                                                wifiSocket2.WIFI_Write(LF);    //
                                                print1 = 1;
                                            }else {
                                                if (statusnets.equals("ok")) {
                                                    wifiSocket.WIFI_Write(setHT34);    //
                                                    wifiSocket.WIFI_Write(normal);    //
                                                    wifiSocket.WIFI_Write(value);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(string1);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(right);    //
                                                    wifiSocket.WIFI_Write(pricee);
                                                    wifiSocket.WIFI_Write(HT1);    //
                                                    wifiSocket.WIFI_Write(tototot);
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
////////////////////////////////////////////////
                                if (value.length() > quanlentha && name.length() > charlength1) {
//                                    Toast.makeText(Preparing_Orders_w.this, "13", Toast.LENGTH_SHORT).show();
                                    String string1quan = value.substring(0, quanlentha);
                                    String string2quan = value.substring(quanlentha);
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength, charlength1);
                                    String string3 = name.substring(charlength1);
                                    allbufitems = new byte[][]{
                                            setHT34, normal, string1quan.getBytes(), HT, string1.getBytes(), HT, pricee.getBytes(), HT1, tototot.getBytes(), LF, string2quan.getBytes(), HT, string2.getBytes(), LF, "      ".getBytes(), string3.getBytes(), left, LF,
                                    };
                                    if (print1 == 0) {
                                        if (statussusbs.equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(setHT34);    //
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write(string1quan);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write(string1);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BluetoothPrintDriver.BT_Write(right);    //
                                            BT_Write(pricee);
                                            BluetoothPrintDriver.BT_Write(HT1);    //
                                            BT_Write(tototot);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            BT_Write(string2quan);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write(string2);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            BT_Write("      ");
                                            BT_Write(string3);
                                            BluetoothPrintDriver.BT_Write(right);    //
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            print1 = 1;
                                        } else {
                                            if (statusnets_counter.equals("ok")) {
                                                wifiSocket2.WIFI_Write(setHT34);    //
                                                wifiSocket2.WIFI_Write(normal);    //
                                                wifiSocket2.WIFI_Write(string1quan);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(string1);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(right);    //
                                                wifiSocket2.WIFI_Write(pricee);
                                                wifiSocket2.WIFI_Write(HT1);    //
                                                wifiSocket2.WIFI_Write(tototot);
                                                wifiSocket2.WIFI_Write(LF);    //
                                                wifiSocket2.WIFI_Write(string2quan);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(string2);
                                                wifiSocket2.WIFI_Write(LF);    //
                                                wifiSocket2.WIFI_Write("      ");
                                                wifiSocket2.WIFI_Write(string3);
                                                wifiSocket2.WIFI_Write(right);    //
                                                wifiSocket2.WIFI_Write(LF);    //
                                                print1 = 1;
                                            }else {
                                                if (statusnets.equals("ok")) {
                                                    wifiSocket.WIFI_Write(setHT34);    //
                                                    wifiSocket.WIFI_Write(normal);    //
                                                    wifiSocket.WIFI_Write(string1quan);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(string1);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(right);    //
                                                    wifiSocket.WIFI_Write(pricee);
                                                    wifiSocket.WIFI_Write(HT1);    //
                                                    wifiSocket.WIFI_Write(tototot);
                                                    wifiSocket.WIFI_Write(LF);    //
                                                    wifiSocket.WIFI_Write(string2quan);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(string2);
                                                    wifiSocket.WIFI_Write(LF);    //
                                                    wifiSocket.WIFI_Write("      ");
                                                    wifiSocket.WIFI_Write(string3);
                                                    wifiSocket.WIFI_Write(right);    //
                                                    wifiSocket.WIFI_Write(LF);    //
                                                    print1 = 1;
                                                }
                                            }
                                        }
                                    }

                                }
                                if (value.length() <= quanlentha && name.length() > charlength1) {
//                                    Toast.makeText(Preparing_Orders_w.this, "14", Toast.LENGTH_SHORT).show();
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength, charlength1);
                                    String string3 = name.substring(charlength1);
                                    allbufitems = new byte[][]{
                                            setHT34, normal, value.getBytes(), HT, string1.getBytes(), HT, pricee.getBytes(), HT1, tototot.getBytes(), LF, "      ".getBytes(), string2.getBytes(), LF, "      ".getBytes(), string3.getBytes(), left, LF,
                                    };
                                    if (print1 == 0) {
                                        if (statussusbs.equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(setHT34);    //
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write(value);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write(string1);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BluetoothPrintDriver.BT_Write(right);    //
                                            BT_Write(pricee);
                                            BluetoothPrintDriver.BT_Write(HT1);    //
                                            BT_Write(tototot);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            BT_Write("      ");
                                            BT_Write(string2);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            BT_Write("      ");
                                            BT_Write(string3);
                                            BluetoothPrintDriver.BT_Write(right);    //
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            print1 = 1;
                                        } else {
                                            if (statusnets_counter.equals("ok")) {
                                                wifiSocket2.WIFI_Write(setHT34);    //
                                                wifiSocket2.WIFI_Write(normal);    //
                                                wifiSocket2.WIFI_Write(value);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(string1);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(right);    //
                                                wifiSocket2.WIFI_Write(pricee);
                                                wifiSocket2.WIFI_Write(HT1);    //
                                                wifiSocket2.WIFI_Write(tototot);
                                                wifiSocket2.WIFI_Write(LF);    //
                                                wifiSocket2.WIFI_Write("      ");
                                                wifiSocket2.WIFI_Write(string2);
                                                wifiSocket2.WIFI_Write(LF);    //
                                                wifiSocket2.WIFI_Write("      ");
                                                wifiSocket2.WIFI_Write(string3);
                                                wifiSocket2.WIFI_Write(right);    //
                                                wifiSocket2.WIFI_Write(LF);    //
                                                print1 = 1;
                                            }else {
                                                if (statusnets.equals("ok")) {
                                                    wifiSocket.WIFI_Write(setHT34);    //
                                                    wifiSocket.WIFI_Write(normal);    //
                                                    wifiSocket.WIFI_Write(value);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(string1);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(right);    //
                                                    wifiSocket.WIFI_Write(pricee);
                                                    wifiSocket.WIFI_Write(HT1);    //
                                                    wifiSocket.WIFI_Write(tototot);
                                                    wifiSocket.WIFI_Write(LF);    //
                                                    wifiSocket.WIFI_Write("      ");
                                                    wifiSocket.WIFI_Write(string2);
                                                    wifiSocket.WIFI_Write(LF);    //
                                                    wifiSocket.WIFI_Write("      ");
                                                    wifiSocket.WIFI_Write(string3);
                                                    wifiSocket.WIFI_Write(right);    //
                                                    wifiSocket.WIFI_Write(LF);    //
                                                    print1 = 1;
                                                }
                                            }
                                        }
                                    }

                                }

                                Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                if (ccursor.moveToFirst()) {
                                    String hsn = ccursor.getString(34);

                                    TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                    hsn_hsn.setText(hsn);

                                    if (hsn_hsn.getText().toString().equals("")) {
                                    } else {
                                        allbuftaxestype1 = new byte[][]{
                                                left, normal, hsn.getBytes(), HT, LF
                                        };
                                        if (statussusbs.equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(left);    //
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write("HSN "+hsn);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        } else {
                                            if (statusnets_counter.equals("ok")) {
                                                wifiSocket2.WIFI_Write(left);    //
                                                wifiSocket2.WIFI_Write(normal);    //
                                                wifiSocket2.WIFI_Write("HSN "+hsn);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(LF);    //
                                            }else {
                                                if (statusnets.equals("ok")) {
                                                    wifiSocket.WIFI_Write(left);    //
                                                    wifiSocket.WIFI_Write(normal);    //
                                                    wifiSocket.WIFI_Write("HSN "+hsn);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(LF);    //
                                                }
                                            }
                                        }
                                    }
                                }

                            } else {
                                if (value.length() > quanlentha) {
//                                    Toast.makeText(Preparing_Orders_w.this, "15", Toast.LENGTH_SHORT).show();
                                    String string1quan = value.substring(0, quanlentha);
                                    String string2quan = value.substring(quanlentha);
                                    allbufitems = new byte[][]{
                                            setHT34, normal, string1quan.getBytes(), HT, name.getBytes(), HT, pricee.getBytes(), HT1, tototot.getBytes(), left, LF, string2quan.getBytes(), LF
                                    };
                                    if (statussusbs.equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(setHT34);    //
                                        BluetoothPrintDriver.BT_Write(normal);    //
                                        BT_Write(string1quan);
                                        BluetoothPrintDriver.BT_Write(HT);    //
                                        BT_Write(name);
                                        BluetoothPrintDriver.BT_Write(HT);    //
                                        BluetoothPrintDriver.BT_Write(right);    //
                                        BT_Write(pricee);
                                        BluetoothPrintDriver.BT_Write(HT1);    //
                                        BT_Write(tototot);
                                        BluetoothPrintDriver.BT_Write(right);    //
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                        BT_Write(string2quan);
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    } else {
                                        if (statusnets_counter.equals("ok")) {
                                            wifiSocket2.WIFI_Write(setHT34);    //
                                            wifiSocket2.WIFI_Write(normal);    //
                                            wifiSocket2.WIFI_Write(string1quan);
                                            wifiSocket2.WIFI_Write(HT);    //
                                            wifiSocket2.WIFI_Write(name);
                                            wifiSocket2.WIFI_Write(HT);    //
                                            wifiSocket2.WIFI_Write(right);    //
                                            wifiSocket2.WIFI_Write(pricee);
                                            wifiSocket2.WIFI_Write(HT1);    //
                                            wifiSocket2.WIFI_Write(tototot);
                                            wifiSocket2.WIFI_Write(right);    //
                                            wifiSocket2.WIFI_Write(LF);    //
                                            wifiSocket2.WIFI_Write(string2quan);
                                            wifiSocket2.WIFI_Write(LF);    //
                                        }else {
                                            if (statusnets.equals("ok")) {
                                                wifiSocket.WIFI_Write(setHT34);    //
                                                wifiSocket.WIFI_Write(normal);    //
                                                wifiSocket.WIFI_Write(string1quan);
                                                wifiSocket.WIFI_Write(HT);    //
                                                wifiSocket.WIFI_Write(name);
                                                wifiSocket.WIFI_Write(HT);    //
                                                wifiSocket.WIFI_Write(right);    //
                                                wifiSocket.WIFI_Write(pricee);
                                                wifiSocket.WIFI_Write(HT1);    //
                                                wifiSocket.WIFI_Write(tototot);
                                                wifiSocket.WIFI_Write(right);    //
                                                wifiSocket.WIFI_Write(LF);    //
                                                wifiSocket.WIFI_Write(string2quan);
                                                wifiSocket.WIFI_Write(LF);    //
                                            }
                                        }
                                    }
                                } else {
//                                    Toast.makeText(Preparing_Orders_w.this, "16", Toast.LENGTH_SHORT).show();
                                    allbufitems = new byte[][]{
                                            setHT34, normal, value.getBytes(), HT, name.getBytes(), HT, pricee.getBytes(), HT1, tototot.getBytes(), left, LF,
                                    };
                                    if (statussusbs.equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(setHT34);    //
                                        BluetoothPrintDriver.BT_Write(normal);    //
                                        BT_Write(value);
                                        BluetoothPrintDriver.BT_Write(HT);    //
                                        BT_Write(name);
                                        BluetoothPrintDriver.BT_Write(HT);    //
                                        BluetoothPrintDriver.BT_Write(right);    //
                                        BT_Write(pricee);
                                        BluetoothPrintDriver.BT_Write(HT1);    //
                                        BT_Write(tototot);
                                        BluetoothPrintDriver.BT_Write(right);    //
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    } else {
                                        if (statusnets_counter.equals("ok")) {
                                            wifiSocket2.WIFI_Write(setHT34);    //
                                            wifiSocket2.WIFI_Write(normal);    //
                                            wifiSocket2.WIFI_Write(value);
                                            wifiSocket2.WIFI_Write(HT);    //
                                            wifiSocket2.WIFI_Write(name);
                                            wifiSocket2.WIFI_Write(HT);    //
                                            wifiSocket2.WIFI_Write(right);    //
                                            wifiSocket2.WIFI_Write(pricee);
                                            wifiSocket2.WIFI_Write(HT1);    //
                                            wifiSocket2.WIFI_Write(tototot);
                                            wifiSocket2.WIFI_Write(right);    //
                                            wifiSocket2.WIFI_Write(LF);    //
                                        }else {
                                            if (statusnets.equals("ok")) {
                                                wifiSocket.WIFI_Write(setHT34);    //
                                                wifiSocket.WIFI_Write(normal);    //
                                                wifiSocket.WIFI_Write(value);
                                                wifiSocket.WIFI_Write(HT);    //
                                                wifiSocket.WIFI_Write(name);
                                                wifiSocket.WIFI_Write(HT);    //
                                                wifiSocket.WIFI_Write(right);    //
                                                wifiSocket.WIFI_Write(pricee);
                                                wifiSocket.WIFI_Write(HT1);    //
                                                wifiSocket.WIFI_Write(tototot);
                                                wifiSocket.WIFI_Write(right);    //
                                                wifiSocket.WIFI_Write(LF);    //
                                            }
                                        }
                                    }
                                }

                                Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                if (ccursor.moveToFirst()) {
                                    String hsn = ccursor.getString(34);

                                    TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                    hsn_hsn.setText(hsn);

                                    if (hsn_hsn.getText().toString().equals("")) {
                                    } else {
                                        allbuftaxestype1 = new byte[][]{
                                                left, normal, hsn.getBytes(), HT, LF
                                        };
                                        if (statussusbs.equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(left);    //
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write("HSN "+hsn);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        } else {
                                            if (statusnets_counter.equals("ok")) {
                                                wifiSocket2.WIFI_Write(left);    //
                                                wifiSocket2.WIFI_Write(normal);    //
                                                wifiSocket2.WIFI_Write("HSN "+hsn);
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write(LF);    //
                                            }else {
                                                if (statusnets.equals("ok")) {
                                                    wifiSocket.WIFI_Write(left);    //
                                                    wifiSocket.WIFI_Write(normal);    //
                                                    wifiSocket.WIFI_Write("HSN "+hsn);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(LF);    //
                                                }
                                            }
                                        }
                                    }
                                }


                            }

                            tv8 = new TextView(Preparing_Orders_w.this);
                            tv8.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                            //tv3.setLayoutParams(new TableRow.LayoutParams(90, TableRow.LayoutParams.MATCH_PARENT));
                            //tv3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            tv8.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                            //tv3.setPadding(0, 0, 10, 0);
                            tv8.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tv8.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                            final String number = tv.getText().toString();
                            float newmul = Float.parseFloat(number);
                            //final float in = Float.parseFloat(cursor.getString(4));
                            String pricee1 = ccursorr.getString(3);
                            String multiply = String.valueOf(newmul * Float.parseFloat(pricee1));
                            //newmul = Integer.parseInt(multiply);
                            tv8.setText(String.valueOf(multiply));
                            row.addView(tv8);

                            Cursor disc_cursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '" + name + "' AND _id = '" + nbg + "'  ", null);
                            if (disc_cursor.moveToFirst()) {
                                do {
                                    String disc_vv = disc_cursor.getString(12);
                                    String disc_there = disc_cursor.getString(30);
                                    float vtq = disc_cursor.getFloat(31);
                                    if (disc_there.equals("no")) {

                                    } else {

                                        total_disc_print_q = String.valueOf(vtq);

                                        allbufrounded = new byte[][]{
                                                setHT34, normal, "".getBytes(), HT, "".getBytes(), HT, "".getBytes(), HT1, "(".getBytes(), "-".getBytes(), total_disc_print_q.getBytes(), ")".getBytes(), left, LF,
                                        };

                                        if (statussusbs.equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(setHT34);    //
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write("");
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write("");
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write("");
                                            BluetoothPrintDriver.BT_Write(HT1);    //
                                            BT_Write("(");
                                            BT_Write("-");
                                            BT_Write(total_disc_print_q);
                                            BT_Write(")");
                                            BluetoothPrintDriver.BT_Write(left);    //
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        } else {
                                            if (statusnets_counter.equals("ok")) {
                                                wifiSocket2.WIFI_Write(setHT34);    //
                                                wifiSocket2.WIFI_Write(normal);    //
                                                wifiSocket2.WIFI_Write("");
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write("");
                                                wifiSocket2.WIFI_Write(HT);    //
                                                wifiSocket2.WIFI_Write("");
                                                wifiSocket2.WIFI_Write(HT1);    //
                                                wifiSocket2.WIFI_Write("(");
                                                wifiSocket2.WIFI_Write("-");
                                                wifiSocket2.WIFI_Write(total_disc_print_q);
                                                wifiSocket2.WIFI_Write(")");
                                                wifiSocket2.WIFI_Write(left);    //
                                                wifiSocket2.WIFI_Write(LF);    //
                                            }else {
                                                if (statusnets.equals("ok")) {
                                                    wifiSocket.WIFI_Write(setHT34);    //
                                                    wifiSocket.WIFI_Write(normal);    //
                                                    wifiSocket.WIFI_Write("");
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write("");
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write("");
                                                    wifiSocket.WIFI_Write(HT1);    //
                                                    wifiSocket.WIFI_Write("(");
                                                    wifiSocket.WIFI_Write("-");
                                                    wifiSocket.WIFI_Write(total_disc_print_q);
                                                    wifiSocket.WIFI_Write(")");
                                                    wifiSocket.WIFI_Write(left);    //
                                                    wifiSocket.WIFI_Write(LF);    //
                                                }
                                            }
                                        }
                                    }
                                } while (disc_cursor.moveToNext());
                            }
                            disc_cursor.close();
                        }
                        modcursor.close();
                    }


                } while (ccursorr.moveToNext());
            }
            ccursorr.close();

            if (statussusbs.equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BT_Write(str_line);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.equals("ok")) {
                    wifiSocket2.WIFI_Write(left);	//
                    wifiSocket2.WIFI_Write(un1);	//
                    wifiSocket2.WIFI_Write(str_line);
                    wifiSocket2.WIFI_Write(LF);	//
                }else {
                    if (statusnets.equals("ok")) {
                        wifiSocket.WIFI_Write(left);	//
                        wifiSocket.WIFI_Write(un1);	//
                        wifiSocket.WIFI_Write(str_line);
                        wifiSocket.WIFI_Write(LF);	//
                    }
                }
            }
////////////////////////////////////sub total
            Cursor cursor3 = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billnumb + "'", null);
            if (cursor3.moveToFirst()) {
                sub = cursor3.getString(7);
            }
            cursor3.close();


//        int sub12 = sub1;
//        String total2 = String.valueOf(sub12);
            float to = Float.parseFloat(sub);
            String tot = String.format("%.2f", to);

            if (tot.length() >= 11) {

            }
            if (tot.length() == 10) {
                tot = " "+tot;
            }
            if (tot.length() == 9) {
                tot = "  "+tot;
            }
            if (tot.length() == 8) {
                tot = "   "+tot;
            }
            if (tot.length() == 7) {
                tot = "    "+tot;
            }
            if (tot.length() == 6) {
                tot = "     "+tot;
            }
            if (tot.length() == 5) {
                tot = "      "+tot;
            }
            if (tot.length() == 4) {
                tot = "       "+tot;
            }
            allbufsubtot = new byte[][]{
                    setHT3212, left, "Sub total".getBytes(), HT, tot.getBytes(), LF
//						left, normal, setHT22, "DECAF16".getBytes(), HT, right, "30".getBytes(), LF,
//						left, normal, setHT22, "BREVE".getBytes(), HT, right, "1000".getBytes(), LF,
            };

            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

            if (statussusbs.equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHT3212);    //
                BluetoothPrintDriver.BT_Write(left);    //
                BT_Write("Sub total");
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write(tot);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.equals("ok")) {
                    wifiSocket2.WIFI_Write(setHT3212);    //
                    wifiSocket2.WIFI_Write(left);    //
                    wifiSocket2.WIFI_Write("Sub total");
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write(tot);
                    wifiSocket2.WIFI_Write(LF);    //
                }else {
                    if (statusnets.equals("ok")) {
                        wifiSocket.WIFI_Write(setHT3212);    //
                        wifiSocket.WIFI_Write(left);    //
                        wifiSocket.WIFI_Write("Sub total");
                        wifiSocket.WIFI_Write(HT);    //
                        wifiSocket.WIFI_Write(tot);
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }
/////////////////////////tax
            TableLayout tableLayout1 = new TableLayout(Preparing_Orders_w.this);
            tableLayout1.removeAllViews();

            Cursor ccursor = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (ccursor.moveToFirst()) {

                do {
                    String name = ccursor.getString(10);
                    String value = ccursor.getString(9);
                    String pq = ccursor.getString(4);
                    String itna = ccursor.getString(1);

                    TextView v = new TextView(Preparing_Orders_w.this);
                    v.setText(value);

                    TextView v1 = new TextView(Preparing_Orders_w.this);
                    v1.setText(name);
                    if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                            || v.getText().toString().equals("") || v1.getText().toString().equals("") || v.getText().toString().equals("0.00")) {

                    } else {
                        final TableRow row = new TableRow(Preparing_Orders_w.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                        TableRow.LayoutParams lp, lp1, lp2;

                        TextView tvv = new TextView(Preparing_Orders_w.this);
                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tvv.setGravity(Gravity.START);
                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tvv.setText(name);

                        TextView tv1 = new TextView(Preparing_Orders_w.this);
                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        tv1.setGravity(Gravity.START);
                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tv1.setText(value);
                        float vbn = Float.parseFloat(value);
                        String bvn = String.format(Locale.US,"%.2f", vbn);
                        String value1 = tv1.getText().toString();

                        TextView tv2 = new TextView(Preparing_Orders_w.this);
                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                        tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                        tv2.append(name);
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv2.setPadding(0, 0, 20, 0);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        //tv2.setTextColor(Color.parseColor("#000000"));
                        row.addView(tv2);

                        TextView textView1 = new TextView(Preparing_Orders_w.this);
                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(textView1);

                        TextView tv3 = new TextView(Preparing_Orders_w.this);
                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                        String tota1 = String.format(Locale.US,"%.2f", tota);
                        tv3.setText(String.valueOf(tota));
                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        String value2 = tv3.getText().toString();
                        row.addView(tv3);

                        tableLayout1.addView(row);
                    }
                } while (ccursor.moveToNext());
            }
            ccursor.close();

            Cursor ccursor2 = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (ccursor2.moveToFirst()) {

                do {
                    String name = ccursor2.getString(35);
                    String value = ccursor2.getString(36);
                    String pq = ccursor2.getString(4);
                    String itna = ccursor2.getString(1);

                    TextView v = new TextView(Preparing_Orders_w.this);
                    v.setText(value);

                    TextView v1 = new TextView(Preparing_Orders_w.this);
                    v1.setText(name);
                    if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                            || v.getText().toString().equals("") || v1.getText().toString().equals("") || v.getText().toString().equals("0.00")) {

                    } else {
                        final TableRow row = new TableRow(Preparing_Orders_w.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                        TableRow.LayoutParams lp, lp1, lp2;

                        TextView tvv = new TextView(Preparing_Orders_w.this);
                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tvv.setGravity(Gravity.START);
                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tvv.setText(name);

                        TextView tv1 = new TextView(Preparing_Orders_w.this);
                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        tv1.setGravity(Gravity.START);
                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tv1.setText(value);
                        float vbn = Float.parseFloat(value);
                        String bvn = String.format(Locale.US,"%.2f", vbn);
                        String value1 = tv1.getText().toString();

                        TextView tv2 = new TextView(Preparing_Orders_w.this);
                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                        tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                        tv2.append(name);
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv2.setPadding(0, 0, 20, 0);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        //tv2.setTextColor(Color.parseColor("#000000"));
                        row.addView(tv2);

                        TextView textView1 = new TextView(Preparing_Orders_w.this);
                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(textView1);

                        TextView tv3 = new TextView(Preparing_Orders_w.this);
                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                        String tota1 = String.format(Locale.US,"%.2f", tota);
                        tv3.setText(String.valueOf(tota));
                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        String value2 = tv3.getText().toString();
                        row.addView(tv3);

                        tableLayout1.addView(row);
                    }
                } while (ccursor2.moveToNext());
            }
            ccursor2.close();

            Cursor ccursor3 = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (ccursor3.moveToFirst()) {

                do {
                    String name = ccursor3.getString(37);
                    String value = ccursor3.getString(38);
                    String pq = ccursor3.getString(4);
                    String itna = ccursor3.getString(1);

                    TextView v = new TextView(Preparing_Orders_w.this);
                    v.setText(value);

                    TextView v1 = new TextView(Preparing_Orders_w.this);
                    v1.setText(name);
                    if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                            || v.getText().toString().equals("") || v1.getText().toString().equals("") || v.getText().toString().equals("0.00")) {

                    } else {
                        final TableRow row = new TableRow(Preparing_Orders_w.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                        TableRow.LayoutParams lp, lp1, lp2;

                        TextView tvv = new TextView(Preparing_Orders_w.this);
                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tvv.setGravity(Gravity.START);
                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tvv.setText(name);

                        TextView tv1 = new TextView(Preparing_Orders_w.this);
                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        tv1.setGravity(Gravity.START);
                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tv1.setText(value);
                        float vbn = Float.parseFloat(value);
                        String bvn = String.format(Locale.US,"%.2f", vbn);
                        String value1 = tv1.getText().toString();

                        TextView tv2 = new TextView(Preparing_Orders_w.this);
                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                        tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                        tv2.append(name);
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv2.setPadding(0, 0, 20, 0);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        //tv2.setTextColor(Color.parseColor("#000000"));
                        row.addView(tv2);

                        TextView textView1 = new TextView(Preparing_Orders_w.this);
                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(textView1);

                        TextView tv3 = new TextView(Preparing_Orders_w.this);
                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                        String tota1 = String.format(Locale.US,"%.2f", tota);
                        tv3.setText(String.valueOf(tota));
                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        String value2 = tv3.getText().toString();
                        row.addView(tv3);

                        tableLayout1.addView(row);
                    }
                } while (ccursor3.moveToNext());
            }
            ccursor3.close();

            Cursor ccursor4 = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (ccursor4.moveToFirst()) {

                do {
                    String name = ccursor4.getString(39);
                    String value = ccursor4.getString(40);
                    String pq = ccursor4.getString(4);
                    String itna = ccursor4.getString(1);

                    TextView v = new TextView(Preparing_Orders_w.this);
                    v.setText(value);

                    TextView v1 = new TextView(Preparing_Orders_w.this);
                    v1.setText(name);
                    if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                            || v.getText().toString().equals("") || v1.getText().toString().equals("") || v.getText().toString().equals("0.00")) {

                    } else {
                        final TableRow row = new TableRow(Preparing_Orders_w.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                        TableRow.LayoutParams lp, lp1, lp2;

                        TextView tvv = new TextView(Preparing_Orders_w.this);
                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tvv.setGravity(Gravity.START);
                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tvv.setText(name);

                        TextView tv1 = new TextView(Preparing_Orders_w.this);
                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        tv1.setGravity(Gravity.START);
                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tv1.setText(value);
                        float vbn = Float.parseFloat(value);
                        String bvn = String.format(Locale.US,"%.2f", vbn);
                        String value1 = tv1.getText().toString();

                        TextView tv2 = new TextView(Preparing_Orders_w.this);
                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                        tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                        tv2.append(name);
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv2.setPadding(0, 0, 20, 0);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        //tv2.setTextColor(Color.parseColor("#000000"));
                        row.addView(tv2);

                        TextView textView1 = new TextView(Preparing_Orders_w.this);
                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(textView1);

                        TextView tv3 = new TextView(Preparing_Orders_w.this);
                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                        String tota1 = String.format(Locale.US,"%.2f", tota);
                        tv3.setText(String.valueOf(tota));
                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        String value2 = tv3.getText().toString();
                        row.addView(tv3);

                        tableLayout1.addView(row);
                    }
                } while (ccursor4.moveToNext());
            }
            ccursor4.close();

            Cursor ccursor5 = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (ccursor5.moveToFirst()) {

                do {
                    String name = ccursor5.getString(41);
                    String value = ccursor5.getString(42);
                    String pq = ccursor5.getString(4);
                    String itna = ccursor5.getString(1);

                    TextView v = new TextView(Preparing_Orders_w.this);
                    v.setText(value);

                    TextView v1 = new TextView(Preparing_Orders_w.this);
                    v1.setText(name);
                    if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                            || v.getText().toString().equals("") || v1.getText().toString().equals("") || v.getText().toString().equals("0.00")) {

                    } else {
                        final TableRow row = new TableRow(Preparing_Orders_w.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                        TableRow.LayoutParams lp, lp1, lp2;

                        TextView tvv = new TextView(Preparing_Orders_w.this);
                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tvv.setGravity(Gravity.START);
                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tvv.setText(name);

                        TextView tv1 = new TextView(Preparing_Orders_w.this);
                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        tv1.setGravity(Gravity.START);
                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tv1.setText(value);
                        float vbn = Float.parseFloat(value);
                        String bvn = String.format(Locale.US,"%.2f", vbn);
                        String value1 = tv1.getText().toString();

                        TextView tv2 = new TextView(Preparing_Orders_w.this);
                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                        tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                        tv2.append(name);
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv2.setPadding(0, 0, 20, 0);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        //tv2.setTextColor(Color.parseColor("#000000"));
                        row.addView(tv2);

                        TextView textView1 = new TextView(Preparing_Orders_w.this);
                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(textView1);

                        TextView tv3 = new TextView(Preparing_Orders_w.this);
                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                        String tota1 = String.format(Locale.US,"%.2f", tota);
                        tv3.setText(String.valueOf(tota));
                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        String value2 = tv3.getText().toString();
                        row.addView(tv3);

                        tableLayout1.addView(row);
                    }
                } while (ccursor5.moveToNext());
            }
            ccursor5.close();


            ArrayList<String> groupList = new ArrayList<String>();

            float sum_p = 0;
            for (int i = 0; i < tableLayout1.getChildCount(); i++) {
                TableRow mRow = (TableRow) tableLayout1.getChildAt(i);
                TextView mTextView = (TextView) mRow.getChildAt(0);
//                                Toast.makeText(Preparing_Orders_w.this, "a "+mTextView.getText().toString(), Toast.LENGTH_LONG).show();

                if (groupList.contains(mTextView.getText().toString())) {

                }else {
                    sum_p = 0;
                    for (int j = 0; j < tableLayout1.getChildCount(); j++) {
                        TableRow mRow1 = (TableRow) tableLayout1.getChildAt(j);
                        mTextView1 = (TextView) mRow1.getChildAt(0);
                        mTextView2 = (TextView) mRow1.getChildAt(2);
                        if (groupList.contains(mTextView.getText().toString())) {
                            if (mTextView.getText().toString().equals(mTextView1.getText().toString())) {
                                sum_p = sum_p+Float.parseFloat(mTextView2.getText().toString());
//                                                Toast.makeText(Preparing_Orders_w.this, "b " + mTextView2.getText().toString()+" "+sum, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            if (mTextView.getText().toString().equals(mTextView1.getText().toString())) {
                                groupList.add(mTextView.getText().toString());
                                sum_p = sum_p+Float.parseFloat(mTextView2.getText().toString());
//                                                Toast.makeText(Preparing_Orders_w.this, "c " + mTextView2.getText().toString()+" "+sum, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
//                    Toast.makeText(Preparing_Orders_w.this, "aa "+mTextView.getText().toString() +" "+sum_p, Toast.LENGTH_LONG).show();

                    String mod1 = mTextView.getText().toString() + "" + String.format("%.2f", sum_p);
                    String mod12 = mTextView.getText().toString();
                    String sum_p_float = String.format("%.2f", sum_p);

                    if (sum_p_float.length() >= 11) {

                    }
                    if (sum_p_float.length() == 10) {
                        sum_p_float = " "+sum_p_float;
                    }
                    if (sum_p_float.length() == 9) {
                        sum_p_float = "  "+sum_p_float;
                    }
                    if (sum_p_float.length() == 8) {
                        sum_p_float = "   "+sum_p_float;
                    }
                    if (sum_p_float.length() == 7) {
                        sum_p_float = "    "+sum_p_float;
                    }
                    if (sum_p_float.length() == 6) {
                        sum_p_float = "     "+sum_p_float;
                    }
                    if (sum_p_float.length() == 5) {
                        sum_p_float = "      "+sum_p_float;
                    }
                    if (sum_p_float.length() == 4) {
                        sum_p_float = "       "+sum_p_float;
                    }

                    allbuftaxestype1 = new byte[][]{
                            left, normal, mod12.getBytes(), HT, LF
                            //setHT34, normal,total.getBytes(),HT,modiname.getBytes(),HT, modiprice.getBytes(),HT, "125.0".getBytes(),LF
//						left, normal, setHT22, "DECAF16".getBytes(), HT, right, "30".getBytes(), LF,
//						left, normal, setHT22, "BREVE".getBytes(), HT, right, "1000".getBytes(), LF,
                    };
                    //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                    if (statussusbs.equals("ok")) {
                        BluetoothPrintDriver.BT_Write(setHT3212);    //
                        BT_Write(normal);
                        BT_Write(mod12);
                        BluetoothPrintDriver.BT_Write(HT);    //
                        BT_Write(sum_p_float);
                        BluetoothPrintDriver.BT_Write(LF);    //
                    } else {
                        if (statusnets_counter.equals("ok")) {
                            wifiSocket2.WIFI_Write(setHT3212);    //
                            wifiSocket2.WIFI_Write(normal);
                            wifiSocket2.WIFI_Write(mod12);
                            wifiSocket2.WIFI_Write(HT);    //
                            wifiSocket2.WIFI_Write(sum_p_float);
                            wifiSocket2.WIFI_Write(LF);    //
                        }else {
                            if (statusnets.equals("ok")) {
                                wifiSocket.WIFI_Write(setHT3212);    //
                                wifiSocket.WIFI_Write(normal);
                                wifiSocket.WIFI_Write(mod12);
                                wifiSocket.WIFI_Write(HT);    //
                                wifiSocket.WIFI_Write(sum_p_float);
                                wifiSocket.WIFI_Write(LF);    //
                            }
                        }
                    }

//                    String match = "@";
//                    int position = mTextView.getText().toString().indexOf(match);
                    String mod2 = mTextView.getText().toString();
//                    Toast.makeText(Preparing_Orders_w.this, " "+mod2, Toast.LENGTH_LONG).show();
                    Cursor ccursor6 = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND taxname = '"+mod2+"' OR taxname2 = '"+mod2+"' OR taxname3 = '"+mod2+"' OR taxname4 = '"+mod2+"' OR taxname5 = '"+mod2+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                    if (ccursor6.moveToFirst()) {
                        String hsn = ccursor6.getString(34);

                        TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                        hsn_hsn.setText(hsn);

                        if (hsn_hsn.getText().toString().equals("")) {
                        } else {
                            allbuftaxestype1 = new byte[][]{
                                    left, normal, hsn.getBytes(), HT, LF
                            };
                            if (statussusbs.equals("ok")) {
                                BluetoothPrintDriver.BT_Write(left);    //
                                BT_Write(normal);
                                BT_Write("HSN "+hsn);
                                BluetoothPrintDriver.BT_Write(HT);    //
                                BluetoothPrintDriver.BT_Write(LF);    //
                            } else {
                                if (statusnets_counter.equals("ok")) {
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write(normal);
                                    wifiSocket2.WIFI_Write("HSN "+hsn);
                                    wifiSocket2.WIFI_Write(HT);    //
                                    wifiSocket2.WIFI_Write(LF);    //
                                }else {
                                    if (statusnets.equals("ok")) {
                                        wifiSocket.WIFI_Write(left);    //
                                        wifiSocket.WIFI_Write(normal);
                                        wifiSocket.WIFI_Write("HSN "+hsn);
                                        wifiSocket.WIFI_Write(HT);    //
                                        wifiSocket.WIFI_Write(LF);    //
                                    }
                                }
                            }
                        }
                    }

                }
            }

//            Cursor ccursor = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
//            if (ccursor.moveToFirst()) {
//
//                do {
//
//                    String name = ccursor.getString(10);
//                    String value = ccursor.getString(9);
//                    String pq = ccursor.getString(4);
//                    String itna = ccursor.getString(1);
//
//                    if (value.equals("0") || name.equals("NONE") || name.equals("None") || value.equals("0.0")) {
//
//                    } else {
//
////                    final TableRow row = new TableRow(CancelActivity.this);
////                    row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
////                            TableRow.LayoutParams.WRAP_CONTENT));
////                    row.setGravity(Gravity.CENTER);
//
//                        final TableRow row = new TableRow(Preparing_Orders_w.this);
//                        row.setLayoutParams(new TableLayout.LayoutParams(
//                                TableRow.LayoutParams.MATCH_PARENT,
//                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));
//
//                        TableRow.LayoutParams lp, lp1, lp2;
//
////                                    final TextView tv = new TextView(CancelActivity.this);
////                                    //tv.setLayoutParams(new TableRow.LayoutParams(40, TableRow.LayoutParams.MATCH_PARENT));
////                                    tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.7f));
////                                    tv.setTextSize(16);
////                                    tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
////                                    row.addView(tv);
//
//                        TextView tvv = new TextView(Preparing_Orders_w.this);
//                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
//                        //tv.setBackgroundResource(R.drawable.cell_shape);
//                        tvv.setGravity(Gravity.START);
//                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tvv.setText(name);
//
//                        TextView tv1 = new TextView(Preparing_Orders_w.this);
//                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
//                        tv1.setGravity(Gravity.START);
//                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        tv1.setText(value);
//                        float vbn = Float.parseFloat(value);
//                        String bvn = String.format(Locale.US,"%.2f", vbn);
//                        String value1 = tv1.getText().toString();
//
//                        TextView tv2 = new TextView(Preparing_Orders_w.this);
////                    tv2.setLayoutParams(new android.widget.TableRow.LayoutParams(145,
////                            android.widget.TableRow.LayoutParams.WRAP_CONTENT));
//                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
//                        tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
//                        tv2.append(name + " @ " + bvn + "%" + "(" + itna + ")");
//                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tv2.setPadding(0, 0, 20, 0);
//                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        //tv2.setTextColor(Color.parseColor("#000000"));
//                        row.addView(tv2);
//
//                        TextView textView1 = new TextView(Preparing_Orders_w.this);
//                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        row.addView(textView1);
//
//                        TextView tv3 = new TextView(Preparing_Orders_w.this);
////                    tv3.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.WRAP_CONTENT,
////                            android.widget.TableRow.LayoutParams.WRAP_CONTENT));
//                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
//                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
//                        float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
////                        float tota = mul;
//                        String tota1 = String.format(Locale.US,"%.2f", tota);
//                        //tv3.setPadding(0,0,10,0);
//                        tv3.setText(String.valueOf(tota));
//                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        //tv3.setTextColor(Color.parseColor("#000000"));
//                        //row.addView(tv3);
//
//
//                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        String value2 = tv3.getText().toString();
//                        row.addView(tv3);
//
//                        tableLayout1.addView(row);
//                        String mod1 = name + " @ " + bvn + "%" + "(" + itna + ")" + "---" + String.valueOf(tota1);
//                        allbuftaxestype1 = new byte[][]{
//                                left, normal, mod1.getBytes(), HT, LF
//                                //setHT34, normal,total.getBytes(),HT,modiname.getBytes(),HT, modiprice.getBytes(),HT, "125.0".getBytes(),LF
////						left, normal, setHT22, "DECAF16".getBytes(), HT, right, "30".getBytes(), LF,
////						left, normal, setHT22, "BREVE".getBytes(), HT, right, "1000".getBytes(), LF,
//                        };
//                        //byte[] buf1 = DataUtils.byteArraysToBytes(compname);
//
//                        if (statussusbs.equals("ok")) {
//                            BluetoothPrintDriver.BT_Write(left);    //
//                            BT_Write(normal);
//                            BT_Write(mod1);
//                            BluetoothPrintDriver.BT_Write(HT);    //
//                            BluetoothPrintDriver.BT_Write(LF);    //
//                        } else {
//                            if (statusnets.equals("ok")) {
//                                wifiSocket.WIFI_Write(left);    //
//                                wifiSocket.WIFI_Write(normal);
//                                wifiSocket.WIFI_Write(mod1);
//                                wifiSocket.WIFI_Write(HT);    //
//                                wifiSocket.WIFI_Write(LF);    //
//                            }
//                        }
//                    }
//
//
//                } while (ccursor.moveToNext());
//            }
//            ccursor.close();


            String phon = "0";
            Cursor caddress1 = db.rawQuery("SELECT * FROM Customerdetails WHERE billnumber = '" + billnumb + "'", null);
            if (caddress1.moveToFirst()) {
                phon = caddress1.getString(2);
            }
            caddress1.close();

            TextView tvvs = new TextView(Preparing_Orders_w.this);
            tvvs.setText(phon);

            Cursor us_name1 = db.rawQuery("Select * from Customerdetails WHERE phoneno = '" + tvvs.getText().toString() + "'", null);
            if (us_name1.moveToLast()) {
//            Toast.makeText(Preparing_Orders_w.this, "user id there", Toast.LENGTH_LONG).show();
                String na53 = us_name1.getString(53);
                String na38 = us_name1.getString(38);
                String na39 = us_name1.getString(39);
                String na40 = us_name1.getString(40);
                String na41 = us_name1.getString(41);
                String na42 = us_name1.getString(42);
                String na43 = us_name1.getString(43);
                String na44 = us_name1.getString(44);
                String na45 = us_name1.getString(45);
                String na46 = us_name1.getString(46);
                String na47 = us_name1.getString(47);
                String na48 = us_name1.getString(48);
                String na49 = us_name1.getString(49);
                String na50 = us_name1.getString(50);
                String na51 = us_name1.getString(51);
                String na52 = us_name1.getString(52);
                String na38_value = us_name1.getString(54);
                String na39_value = us_name1.getString(55);
                String na40_value = us_name1.getString(56);
                String na41_value = us_name1.getString(57);
                String na42_value = us_name1.getString(58);
                String na43_value = us_name1.getString(59);
                String na44_value = us_name1.getString(60);
                String na45_value = us_name1.getString(61);
                String na46_value = us_name1.getString(62);
                String na47_value = us_name1.getString(63);
                String na48_value = us_name1.getString(64);
                String na49_value = us_name1.getString(65);
                String na50_value = us_name1.getString(66);
                String na51_value = us_name1.getString(67);
                String na52_value = us_name1.getString(68);

                String proc = us_name1.getString(69);

                TextView hid = new TextView(Preparing_Orders_w.this);
                hid.setText(proc);

                if (hid.getText().toString().equals("off")) {
                    Cursor cursorr = null;
                    if (paymmethoda.equals("  Dine-in") || paymmethoda.equals("  General") || paymmethoda.equals("  Others")) {
                        cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax1 = 'dine_in'", null);//replace to cursor = dbHelper.fetchAllHotels();
                    }
                    if (paymmethoda.equals("  Takeaway") || paymmethoda.equals("  Main")) {
                        cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax2 = 'takeaway'", null);//replace to cursor = dbHelper.fetchAllHotels();
                    }
                    if (paymmethoda.equals("  Home delivery")) {
                        cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax3 = 'homedelivery'", null);//replace to cursor = dbHelper.fetchAllHotels();
                    }
//            ccursor = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax'", null);//replace to ccursor = dbHelper.fetchAllHotels();
                    if (cursorr.moveToFirst()) {

                        do {

                            String name = cursorr.getString(1);
                            String value = cursorr.getString(2);

                            final TableRow row = new TableRow(Preparing_Orders_w.this);
                            row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));
                            row.setGravity(Gravity.CENTER);

                            TableRow.LayoutParams lp, lp1, lp2;

//                                final TextView tv = new TextView(CancelActivity.this);
//                                //tv.setLayoutParams(new TableRow.LayoutParams(40, TableRow.LayoutParams.MATCH_PARENT));
//                                tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.7f));
//                                tv.setTextSize(16);
//                                tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                                row.addView(tv);

                            TextView tvv = new TextView(Preparing_Orders_w.this);
                            tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            tvv.setGravity(Gravity.START);
                            tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tvv.setText(name);

                            TextView tv1 = new TextView(Preparing_Orders_w.this);
                            tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                            tv1.setGravity(Gravity.START);
                            tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tv1.setText(value);
                            float vbn = Float.parseFloat(value);
                            String bvn = String.format(Locale.US,"%.2f", vbn);
                            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            String value1 = tv1.getText().toString();

                            TextView tv2 = new TextView(Preparing_Orders_w.this);
                            //lp = new TableRow.LayoutParams(145, TableRow.LayoutParams.WRAP_CONTENT);
                            //tv2.setLayoutParams(lp);
                            tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                            tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                            tv2.append(name + " @ " + bvn + "%");
                            tv2.setPadding(0, 0, 20, 0);
                            tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            row.addView(tv2);

                            TextView textView1 = new TextView(Preparing_Orders_w.this);
                            textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            row.addView(textView1);

                            TextView tv3 = new TextView(Preparing_Orders_w.this);
//                lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
//                tv3.setLayoutParams(lp2);
                            tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                            //tv3.setPadding(0,0,10,0);
                            tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                            float tota = Float.parseFloat(value1) * Float.parseFloat(String.valueOf(to)) / 100;
//                            float tota = mul;
                            String tota1 = String.format(Locale.US,"%.2f", tota);
                            tv3.setText(String.valueOf(tota));
                            //row.addView(tv3);


                            tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            String value2 = tv3.getText().toString();
                            row.addView(tv3);

                            tableLayout1.addView(row);

                            String mod1 = name + " @ " + bvn + "%" + "---" + String.valueOf(tota1);
                            String mod1_1 = name + "" + String.valueOf(tota1);
                            String mod12 = name;
                            String sum_p_float = String.valueOf(tota1);

                            if (sum_p_float.length() >= 11) {

                            }
                            if (sum_p_float.length() == 10) {
                                sum_p_float = " "+sum_p_float;
                            }
                            if (sum_p_float.length() == 9) {
                                sum_p_float = "  "+sum_p_float;
                            }
                            if (sum_p_float.length() == 8) {
                                sum_p_float = "   "+sum_p_float;
                            }
                            if (sum_p_float.length() == 7) {
                                sum_p_float = "    "+sum_p_float;
                            }
                            if (sum_p_float.length() == 6) {
                                sum_p_float = "     "+sum_p_float;
                            }
                            if (sum_p_float.length() == 5) {
                                sum_p_float = "      "+sum_p_float;
                            }
                            if (sum_p_float.length() == 4) {
                                sum_p_float = "       "+sum_p_float;
                            }

                            allbuftaxestype2 = new byte[][]{
                                    left, normal, mod1.getBytes(), HT, LF
                                    //setHT34, normal,total.getBytes(),HT,modiname.getBytes(),HT, modiprice.getBytes(),HT, "125.0".getBytes(),LF
//						left, normal, setHT22, "DECAF16".getBytes(), HT, right, "30".getBytes(), LF,
//						left, normal, setHT22, "BREVE".getBytes(), HT, right, "1000".getBytes(), LF,
                            };
                            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                            if (statussusbs.equals("ok")) {
//                                BluetoothPrintDriver.BT_Write(left);    //
//                                BT_Write(normal);
//                                BT_Write(mod1);
//                                BluetoothPrintDriver.BT_Write(HT);    //
//                                BluetoothPrintDriver.BT_Write(LF);    //
                                BluetoothPrintDriver.BT_Write(setHT3212);    //
                                BT_Write(normal);
                                BT_Write(mod12);
                                BluetoothPrintDriver.BT_Write(HT);    //
                                BT_Write(sum_p_float);
                                BluetoothPrintDriver.BT_Write(LF);    //
                            } else {
                                if (statusnets_counter.equals("ok")) {
                                    wifiSocket2.WIFI_Write(setHT3212);    //
                                    wifiSocket2.WIFI_Write(normal);
                                    wifiSocket2.WIFI_Write(mod12);
                                    wifiSocket2.WIFI_Write(HT);    //
                                    wifiSocket2.WIFI_Write(sum_p_float);
                                    wifiSocket2.WIFI_Write(LF);    //
                                }else {
                                    if (statusnets.equals("ok")) {
                                        wifiSocket.WIFI_Write(setHT3212);    //
                                        wifiSocket.WIFI_Write(normal);
                                        wifiSocket.WIFI_Write(mod12);
                                        wifiSocket.WIFI_Write(HT);    //
                                        wifiSocket.WIFI_Write(sum_p_float);
                                        wifiSocket.WIFI_Write(LF);    //
                                    }
                                }
                            }

                        } while (cursorr.moveToNext());
                    }
                    cursorr.close();
                } else {

                    for (int i2 = 38; i2 < 53; i2++) {

//                                tv33.setText("0.0");
//                                for (int i1 = 54; i1<69; i1++) {
                        int i1 = 0;
                        if (i2 == 38) {
                            i1 = 54;
                        }
                        if (i2 == 39) {
                            i1 = 55;
                        }
                        if (i2 == 40) {
                            i1 = 56;
                        }
                        if (i2 == 41) {
                            i1 = 57;
                        }
                        if (i2 == 42) {
                            i1 = 58;
                        }
                        if (i2 == 43) {
                            i1 = 59;
                        }
                        if (i2 == 44) {
                            i1 = 60;
                        }
                        if (i2 == 45) {
                            i1 = 61;
                        }
                        if (i2 == 46) {
                            i1 = 62;
                        }
                        if (i2 == 47) {
                            i1 = 63;
                        }
                        if (i2 == 48) {
                            i1 = 64;
                        }
                        if (i2 == 49) {
                            i1 = 65;
                        }
                        if (i2 == 50) {
                            i1 = 66;
                        }
                        if (i2 == 51) {
                            i1 = 67;
                        }
                        if (i2 == 52) {
                            i1 = 68;
                        }


                        final TableRow row = new TableRow(Preparing_Orders_w.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));
//                                    TextView tv33 = new TextView(Preparing_Orders_w.this);;
                        TableRow.LayoutParams lp, lp1, lp2;

                        TextView tv = new TextView(Preparing_Orders_w.this);
                        tv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        tv.setGravity(Gravity.START);
                        tv.setTextSize(15);
                        //text = cursor.getString(1);
//                String v = na;

                        tv.setText(us_name1.getString(i2));


                        TextView tv1 = new TextView(Preparing_Orders_w.this);
                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        tv1.setGravity(Gravity.START);
                        //text = cursor.getString(1);
                        tv1.setText(us_name1.getString(i1));
                        String value1 = "0";
                        if (tv1.getText().toString().equals("")) {

                        } else {
                            value1 = tv1.getText().toString();
                        }


                        TextView tv2 = new TextView(Preparing_Orders_w.this);
                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                        tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                        tv2.append(us_name1.getString(i2) + " @ " + us_name1.getString(i1) + "%");
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv2.setPadding(0, 0, 20, 0);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(tv2);
//                    Toast.makeText(Preparing_Orders_w.this, "hiii "+na38 + " @ " + us_name1.getString(i1) + "%", Toast.LENGTH_LONG).show();

                        TextView textView1 = new TextView(Preparing_Orders_w.this);
                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(textView1);

//                    Toast.makeText(Preparing_Orders_w.this, " "+i1 + " @ " + value1 + "%", Toast.LENGTH_LONG).show();

                        TextView tv33 = new TextView(Preparing_Orders_w.this);
                        tv33.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                        tv33.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        float tota = Float.parseFloat(value1) * Float.parseFloat(String.valueOf(to)) / 100;
//                float mul = ((Float.parseFloat(total2)+Float.parseFloat(total_disc_print)) / 100) * Float.parseFloat(value1);
//                    float mul = Float.parseFloat(value1) * (Float.parseFloat(total)+Float.parseFloat(total_disc)) / 100;
//                        float tota = mul;
                        String tota1 = String.format(Locale.US,"%.2f", tota);
                        tv33.setText(String.valueOf(tota));
                        tv33.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        //tv3.setTextColor(Color.parseColor("#000000"));
                        //row.addView(tv3);


                        tv33.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(tv33);

                        String value2 = tv33.getText().toString();
//                    Toast.makeText(Preparing_Orders_w.this, "11 " + String.valueOf(tota1), Toast.LENGTH_LONG).show();

                        if (tv33.getText().toString().equals("0") || tv33.getText().toString().equals("0.0") || tv33.getText().toString().equals("0.00")
                                || tv33.getText().toString().equals("") || tv.getText().toString().equals("")) {

                        } else {
                            tableLayout1.addView(row);
//                        Toast.makeText(Preparing_Orders_w.this, "22 " + us_name1.getString(i2) + " @ " + us_name1.getString(i1) + "%" + "---" + String.valueOf(tota1), Toast.LENGTH_LONG).show();
                            String mod1 = us_name1.getString(i2) + " @ " + us_name1.getString(i1) + "%" + "---" + String.valueOf(tota1);
                            String mod1_1 = us_name1.getString(i2) + "" + String.valueOf(tota1);
                            String mod12 = us_name1.getString(i2);
                            String sum_p_float = String.valueOf(tota1);

                            if (sum_p_float.length() >= 11) {

                            }
                            if (sum_p_float.length() == 10) {
                                sum_p_float = " "+sum_p_float;
                            }
                            if (sum_p_float.length() == 9) {
                                sum_p_float = "  "+sum_p_float;
                            }
                            if (sum_p_float.length() == 8) {
                                sum_p_float = "   "+sum_p_float;
                            }
                            if (sum_p_float.length() == 7) {
                                sum_p_float = "    "+sum_p_float;
                            }
                            if (sum_p_float.length() == 6) {
                                sum_p_float = "     "+sum_p_float;
                            }
                            if (sum_p_float.length() == 5) {
                                sum_p_float = "      "+sum_p_float;
                            }
                            if (sum_p_float.length() == 4) {
                                sum_p_float = "       "+sum_p_float;
                            }

                            allbuftaxestype2 = new byte[][]{
                                    left, normal, mod1.getBytes(), HT, LF
                            };

                            if (statussusbs.equals("ok")) {
//                                BluetoothPrintDriver.BT_Write(left);    //
//                                BT_Write(normal);
//                                BT_Write(mod1);
//                                BluetoothPrintDriver.BT_Write(HT);    //
//                                BluetoothPrintDriver.BT_Write(LF);    //
                                BluetoothPrintDriver.BT_Write(setHT3212);    //
                                BT_Write(normal);
                                BT_Write(mod12);
                                BluetoothPrintDriver.BT_Write(HT);    //
                                BT_Write(sum_p_float);
                                BluetoothPrintDriver.BT_Write(LF);    //
                            } else {
                                if (statusnets_counter.equals("ok")) {
                                    wifiSocket2.WIFI_Write(setHT3212);    //
                                    wifiSocket2.WIFI_Write(normal);
                                    wifiSocket2.WIFI_Write(mod12);
                                    wifiSocket2.WIFI_Write(HT);    //
                                    wifiSocket2.WIFI_Write(sum_p_float);
                                    wifiSocket2.WIFI_Write(LF);    //
                                }else {
                                    if (statusnets.equals("ok")) {
                                        wifiSocket.WIFI_Write(setHT3212);    //
                                        wifiSocket.WIFI_Write(normal);
                                        wifiSocket.WIFI_Write(mod12);
                                        wifiSocket.WIFI_Write(HT);    //
                                        wifiSocket.WIFI_Write(sum_p_float);
                                        wifiSocket.WIFI_Write(LF);    //
                                    }
                                }
                            }

                        }

                    }
                }

            } else {
//            Toast.makeText(Preparing_Orders_w.this, "user id not there", Toast.LENGTH_LONG).show();
                Cursor cursorr = null;
                if (paymmethoda.equals("  Dine-in") || paymmethoda.equals("  General") || paymmethoda.equals("  Others")) {
                    cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax1 = 'dine_in'", null);//replace to cursor = dbHelper.fetchAllHotels();
                }
                if (paymmethoda.equals("  Takeaway") || paymmethoda.equals("  Main")) {
                    cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax2 = 'takeaway'", null);//replace to cursor = dbHelper.fetchAllHotels();
                }
                if (paymmethoda.equals("  Home delivery")) {
                    cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax3 = 'homedelivery'", null);//replace to cursor = dbHelper.fetchAllHotels();
                }
//            ccursor = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax'", null);//replace to ccursor = dbHelper.fetchAllHotels();
                if (cursorr.moveToFirst()) {

                    do {

                        String name = cursorr.getString(1);
                        String value = cursorr.getString(2);

                        final TableRow row = new TableRow(Preparing_Orders_w.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        row.setGravity(Gravity.CENTER);

                        TableRow.LayoutParams lp, lp1, lp2;

//                                final TextView tv = new TextView(CancelActivity.this);
//                                //tv.setLayoutParams(new TableRow.LayoutParams(40, TableRow.LayoutParams.MATCH_PARENT));
//                                tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.7f));
//                                tv.setTextSize(16);
//                                tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                                row.addView(tv);

                        TextView tvv = new TextView(Preparing_Orders_w.this);
                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tvv.setGravity(Gravity.START);
                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tvv.setText(name);

                        TextView tv1 = new TextView(Preparing_Orders_w.this);
                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        tv1.setGravity(Gravity.START);
                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv1.setText(value);
                        float vbn = Float.parseFloat(value);
                        String bvn = String.format(Locale.US,"%.2f", vbn);
                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        String value1 = tv1.getText().toString();

                        TextView tv2 = new TextView(Preparing_Orders_w.this);
                        //lp = new TableRow.LayoutParams(145, TableRow.LayoutParams.WRAP_CONTENT);
                        //tv2.setLayoutParams(lp);
                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                        tv2.append(name + " @ " + bvn + "%");
                        tv2.setPadding(0, 0, 20, 0);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(tv2);

                        TextView textView1 = new TextView(Preparing_Orders_w.this);
                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(textView1);

                        TextView tv3 = new TextView(Preparing_Orders_w.this);
//                lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
//                tv3.setLayoutParams(lp2);
                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                        //tv3.setPadding(0,0,10,0);
                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        float tota = Float.parseFloat(value1) * Float.parseFloat(String.valueOf(to)) / 100;
//                        float tota = mul;
                        String tota1 = String.format(Locale.US,"%.2f", tota);
                        tv3.setText(String.valueOf(tota));
                        //row.addView(tv3);


                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        String value2 = tv3.getText().toString();
                        row.addView(tv3);

                        tableLayout1.addView(row);

                        String mod1 = name + " @ " + bvn + "%" + "---" + String.valueOf(tota1);
                        String mod1_1 = name + "" + String.valueOf(tota1);
                        String mod12 = name;
                        String sum_p_float = String.valueOf(tota1);

                        if (sum_p_float.length() >= 11) {

                        }
                        if (sum_p_float.length() == 10) {
                            sum_p_float = " "+sum_p_float;
                        }
                        if (sum_p_float.length() == 9) {
                            sum_p_float = "  "+sum_p_float;
                        }
                        if (sum_p_float.length() == 8) {
                            sum_p_float = "   "+sum_p_float;
                        }
                        if (sum_p_float.length() == 7) {
                            sum_p_float = "    "+sum_p_float;
                        }
                        if (sum_p_float.length() == 6) {
                            sum_p_float = "     "+sum_p_float;
                        }
                        if (sum_p_float.length() == 5) {
                            sum_p_float = "      "+sum_p_float;
                        }
                        if (sum_p_float.length() == 4) {
                            sum_p_float = "       "+sum_p_float;
                        }

                        allbuftaxestype2 = new byte[][]{
                                left, normal, mod1.getBytes(), HT, LF
                                //setHT34, normal,total.getBytes(),HT,modiname.getBytes(),HT, modiprice.getBytes(),HT, "125.0".getBytes(),LF
//						left, normal, setHT22, "DECAF16".getBytes(), HT, right, "30".getBytes(), LF,
//						left, normal, setHT22, "BREVE".getBytes(), HT, right, "1000".getBytes(), LF,
                        };
                        //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                        if (statussusbs.equals("ok")) {
//                            BluetoothPrintDriver.BT_Write(left);    //
//                            BT_Write(normal);
//                            BT_Write(mod1);
//                            BluetoothPrintDriver.BT_Write(HT);    //
//                            BluetoothPrintDriver.BT_Write(LF);    //
                            BluetoothPrintDriver.BT_Write(setHT3212);    //
                            BT_Write(normal);
                            BT_Write(mod12);
                            BluetoothPrintDriver.BT_Write(HT);    //
                            BT_Write(sum_p_float);
                            BluetoothPrintDriver.BT_Write(LF);    //
                        } else {
                            if (statusnets_counter.equals("ok")) {
                                wifiSocket2.WIFI_Write(setHT3212);    //
                                wifiSocket2.WIFI_Write(normal);
                                wifiSocket2.WIFI_Write(mod12);
                                wifiSocket2.WIFI_Write(HT);    //
                                wifiSocket2.WIFI_Write(sum_p_float);
                                wifiSocket2.WIFI_Write(LF);    //
                            }else {
                                if (statusnets.equals("ok")) {
                                    wifiSocket.WIFI_Write(setHT3212);    //
                                    wifiSocket.WIFI_Write(normal);
                                    wifiSocket.WIFI_Write(mod12);
                                    wifiSocket.WIFI_Write(HT);    //
                                    wifiSocket.WIFI_Write(sum_p_float);
                                    wifiSocket.WIFI_Write(LF);    //
                                }
                            }
                        }

                    } while (cursorr.moveToNext());
                }
                cursorr.close();
            }
            us_name1.close();

            float sum = 0;
            for (int i = 0; i < tableLayout1.getChildCount(); i++) {
                TableRow mRow = (TableRow) tableLayout1.getChildAt(i);
                TextView mTextView = (TextView) mRow.getChildAt(2);
                sum = sum
                        + Float.parseFloat(mTextView.getText().toString());
            }


            String newsum = String.format("%.2f", sum);

            if (newsum.length() >= 11) {

            }
            if (newsum.length() == 10) {
                newsum = " "+newsum;
            }
            if (newsum.length() == 9) {
                newsum = "  "+newsum;
            }
            if (newsum.length() == 8) {
                newsum = "   "+newsum;
            }
            if (newsum.length() == 7) {
                newsum = "    "+newsum;
            }
            if (newsum.length() == 6) {
                newsum = "     "+newsum;
            }
            if (newsum.length() == 5) {
                newsum = "      "+newsum;
            }
            if (newsum.length() == 4) {
                newsum = "       "+newsum;
            }

            if (sum == 0 || sum == 0.0 || sum == 0.00) {

            } else {
                allbuftax = new byte[][]{
                        setHT3212, left, "Tax".getBytes(), HT, newsum.getBytes(), LF
                };

                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.equals("ok")) {
                    BluetoothPrintDriver.BT_Write(setHT3212);    //
                    BluetoothPrintDriver.BT_Write(left);    //
                    BT_Write("Tax");
                    BluetoothPrintDriver.BT_Write(HT);    //
                    BT_Write(newsum);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.equals("ok")) {
                        wifiSocket2.WIFI_Write(setHT3212);    //
                        wifiSocket2.WIFI_Write(left);    //
                        wifiSocket2.WIFI_Write("Tax");
                        wifiSocket2.WIFI_Write(HT);    //
                        wifiSocket2.WIFI_Write(newsum);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.equals("ok")) {
                            wifiSocket.WIFI_Write(setHT3212);    //
                            wifiSocket.WIFI_Write(left);    //
                            wifiSocket.WIFI_Write("Tax");
                            wifiSocket.WIFI_Write(HT);    //
                            wifiSocket.WIFI_Write(newsum);
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }
///////////////////////////////// discount

            Cursor cursor4 = db.rawQuery("SELECT * FROM Discountdetails WHERE billno = '" + billnumb + "'", null);
            if (cursor4.moveToFirst()) {
                taxpe = cursor4.getString(5);
            } else {
                taxpe = "0.00";
            }
            cursor4.close();

            Cursor cursor5 = db.rawQuery("SELECT * FROM Discountdetails WHERE billno = '" + billnumb + "'", null);
            if (cursor5.moveToFirst()) {
                dsirs = cursor5.getString(7);
            } else {
                dsirs = "0.00";
            }
            cursor5.close();

            String alldiscinperc1 = "Discount(" + taxpe + "%)";
            allbufdisc = new byte[][]{
                    setHT3212, left, alldiscinperc1.getBytes(), HT, dsirs.getBytes(), LF
//						left, normal, setHT22, "DECAF16".getBytes(), HT, right, "30".getBytes(), LF,
//						left, normal, setHT22, "BREVE".getBytes(), HT, right, "1000".getBytes(), LF,
            };

            if (dsirs.length() >= 11) {

            }
            if (dsirs.length() == 10) {
                dsirs = " "+dsirs;
            }
            if (dsirs.length() == 9) {
                dsirs = "  "+dsirs;
            }
            if (dsirs.length() == 8) {
                dsirs = "   "+dsirs;
            }
            if (dsirs.length() == 7) {
                dsirs = "    "+dsirs;
            }
            if (dsirs.length() == 6) {
                dsirs = "     "+dsirs;
            }
            if (dsirs.length() == 5) {
                dsirs = "      "+dsirs;
            }
            if (dsirs.length() == 4) {
                dsirs = "       "+dsirs;
            }

            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

            if (statussusbs.equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHT3212);    //
                BluetoothPrintDriver.BT_Write(left);    //
                BT_Write(alldiscinperc1);
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write(dsirs);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.equals("ok")) {
                    wifiSocket2.WIFI_Write(setHT3212);    //
                    wifiSocket2.WIFI_Write(left);    //
                    wifiSocket2.WIFI_Write(alldiscinperc1);
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write(dsirs);
                    wifiSocket2.WIFI_Write(LF);    //
                }else {
                    if (statusnets.equals("ok")) {
                        wifiSocket.WIFI_Write(setHT3212);    //
                        wifiSocket.WIFI_Write(left);    //
                        wifiSocket.WIFI_Write(alldiscinperc1);
                        wifiSocket.WIFI_Write(HT);    //
                        wifiSocket.WIFI_Write(dsirs);
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }
            float newe;
//////////////////////////////////////////////

            Cursor cursor113 = db.rawQuery("SELECT SUM(disc_indiv_total) FROM All_Sales WHERE disc_thereornot = 'yes' AND bill_no = '" + billnumb + "'", null);
            if (cursor113.moveToFirst()) {
                float level = cursor113.getFloat(0);
                total = String.valueOf(level);
                float total1 = Float.parseFloat(total);
                total_disc_print_q = String.format(Locale.US,"%.2f", total1);

                if (total_disc_print_q.length() >= 11) {

                }
                if (total_disc_print_q.length() == 10) {
                    total_disc_print_q = " "+total_disc_print_q;
                }
                if (total_disc_print_q.length() == 9) {
                    total_disc_print_q = "  "+total_disc_print_q;
                }
                if (total_disc_print_q.length() == 8) {
                    total_disc_print_q = "   "+total_disc_print_q;
                }
                if (total_disc_print_q.length() == 7) {
                    total_disc_print_q = "    "+total_disc_print_q;
                }
                if (total_disc_print_q.length() == 6) {
                    total_disc_print_q = "     "+total_disc_print_q;
                }
                if (total_disc_print_q.length() == 5) {
                    total_disc_print_q = "      "+total_disc_print_q;
                }
                if (total_disc_print_q.length() == 4) {
                    total_disc_print_q = "       "+total_disc_print_q;
                }

                allbufrounded = new byte[][]{
                        setHT3212, left, "Savings".getBytes(), HT, total_disc_print_q.getBytes(), LF
                };

                if (statussusbs.equals("ok")) {
                    BluetoothPrintDriver.BT_Write(setHT3212);    //
                    BluetoothPrintDriver.BT_Write(left);    //
                    BT_Write("Savings");
                    BluetoothPrintDriver.BT_Write(HT);    //
                    BT_Write(total_disc_print_q);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.equals("ok")) {
                        wifiSocket2.WIFI_Write(setHT3212);    //
                        wifiSocket2.WIFI_Write(left);    //
                        wifiSocket2.WIFI_Write("Savings");
                        wifiSocket2.WIFI_Write(HT);    //
                        wifiSocket2.WIFI_Write(total_disc_print_q);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.equals("ok")) {
                            wifiSocket.WIFI_Write(setHT3212);    //
                            wifiSocket.WIFI_Write(left);    //
                            wifiSocket.WIFI_Write("Savings");
                            wifiSocket.WIFI_Write(HT);    //
                            wifiSocket.WIFI_Write(total_disc_print_q);
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }

            }
            cursor113.close();
////////////////////////////////

            Cursor cursor = db.rawQuery("SELECT * FROM billnumber WHERE billnumber = '" + billnumb + "'", null);
            if (cursor.moveToFirst()){
                String t_total_points = cursor.getString(16);
                String v_tq = cursor.getString(17);

                String v_tq_loy = String.format("%.2f", v_tq);

                if (v_tq_loy.length() >= 11) {

                }
                if (v_tq_loy.length() == 10) {
                    v_tq_loy = " "+v_tq_loy;
                }
                if (v_tq_loy.length() == 9) {
                    v_tq_loy = "  "+v_tq_loy;
                }
                if (v_tq_loy.length() == 8) {
                    v_tq_loy = "   "+v_tq_loy;
                }
                if (v_tq_loy.length() == 7) {
                    v_tq_loy = "    "+v_tq_loy;
                }
                if (v_tq_loy.length() == 6) {
                    v_tq_loy = "     "+v_tq_loy;
                }
                if (v_tq_loy.length() == 5) {
                    v_tq_loy = "      "+v_tq_loy;
                }
                if (v_tq_loy.length() == 4) {
                    v_tq_loy = "       "+v_tq_loy;
                }

                TextView tv = new TextView(Preparing_Orders_w.this);
                tv.setText(t_total_points);

                if (tv.getText().toString().equals("")){

                }else {
                    allbufrounded = new byte[][]{
                            setHT3212, left, "Loyalty".getBytes(), HT, v_tq.getBytes(), LF
                    };

                    if (statussusbs.equals("ok")) {
                        BluetoothPrintDriver.BT_Write(setHT3212);    //
                        BluetoothPrintDriver.BT_Write(left);    //
                        BT_Write("Loyalty(" + t_total_points + ")");
                        BluetoothPrintDriver.BT_Write(HT);    //
                        BT_Write(v_tq_loy);
                        BluetoothPrintDriver.BT_Write(LF);    //
                    } else {
                        if (statusnets_counter.equals("ok")) {
                            wifiSocket2.WIFI_Write(setHT3212);    //
                            wifiSocket2.WIFI_Write(left);    //
                            wifiSocket2.WIFI_Write("Loyalty(" + t_total_points + ")");
                            wifiSocket2.WIFI_Write(HT);    //
                            wifiSocket2.WIFI_Write(v_tq_loy);
                            wifiSocket2.WIFI_Write(LF);    //
                        } else {
                            if (statusnets.equals("ok")) {
                                wifiSocket.WIFI_Write(setHT3212);    //
                                wifiSocket.WIFI_Write(left);    //
                                wifiSocket.WIFI_Write("Loyalty(" + t_total_points + ")");
                                wifiSocket.WIFI_Write(HT);    //
                                wifiSocket.WIFI_Write(v_tq_loy);
                                wifiSocket.WIFI_Write(LF);    //
                            }
                        }
                    }
                }
            }

//////////////////////////////////////Rounded off

            Cursor cursor7 = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billnumb + "'", null);
            if (cursor7.moveToFirst()) {
                subro = cursor7.getString(9);
            }
            cursor7.close();

//            float subro1 = Float.parseFloat(subro);
//            subro = String.format("%.2f", subro1);

            if (subro.length() >= 11) {

            }
            if (subro.length() == 10) {
                subro = " "+subro;
            }
            if (subro.length() == 9) {
                subro = "  "+subro;
            }
            if (subro.length() == 8) {
                subro = "   "+subro;
            }
            if (subro.length() == 7) {
                subro = "    "+subro;
            }
            if (subro.length() == 6) {
                subro = "     "+subro;
            }
            if (subro.length() == 5) {
                subro = "      "+subro;
            }
            if (subro.length() == 4) {
                subro = "       "+subro;
            }

            allbufrounded = new byte[][]{
                    setHT3212, left, "Rounded".getBytes(), HT, subro.getBytes(), LF
//						left, normal, setHT22, "DECAF16".getBytes(), HT, right, "30".getBytes(), LF,
//						left, normal, setHT22, "BREVE".getBytes(), HT, right, "1000".getBytes(), LF,
            };

            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

            if (statussusbs.equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHT3212);    //
                BluetoothPrintDriver.BT_Write(left);    //
                BT_Write("Rounded");
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write(subro);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.equals("ok")) {
                    wifiSocket2.WIFI_Write(setHT3212);    //
                    wifiSocket2.WIFI_Write(left);    //
                    wifiSocket2.WIFI_Write("Rounded");
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write(subro);
                    wifiSocket2.WIFI_Write(LF);    //
                }else {
                    if (statusnets.equals("ok")) {
                        wifiSocket.WIFI_Write(setHT3212);    //
                        wifiSocket.WIFI_Write(left);    //
                        wifiSocket.WIFI_Write("Rounded");
                        wifiSocket.WIFI_Write(HT);    //
                        wifiSocket.WIFI_Write(subro);
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }

            if (statussusbs.equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BT_Write(str_line);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.equals("ok")) {
                    wifiSocket2.WIFI_Write(left);	//
                    wifiSocket2.WIFI_Write(un1);	//
                    wifiSocket2.WIFI_Write(str_line);
                    wifiSocket2.WIFI_Write(LF);	//
                }else {
                    if (statusnets.equals("ok")) {
                        wifiSocket.WIFI_Write(left);	//
                        wifiSocket.WIFI_Write(un1);	//
                        wifiSocket.WIFI_Write(str_line);
                        wifiSocket.WIFI_Write(LF);	//
                    }
                }
            }
//////////////Total main

            Cursor cursor8 = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billnumb + "'", null);
            if (cursor8.moveToFirst()) {
                alltotal1 = cursor8.getString(2);
            }
            cursor8.close();

            float all = Float.parseFloat(alltotal1);
            String newf = String.format("%.2f", all);

            allbufrounded = new byte[][]{
                    setHT32122, left, "Total".getBytes(), HT, "Rs ".getBytes(), newf.getBytes(), LF
//						left, normal, setHT22, "DECAF16".getBytes(), HT, right, "30".getBytes(), LF,
//						left, normal, setHT22, "BREVE".getBytes(), HT, right, "1000".getBytes(), LF,
            };
            String allt = insert1_rs+""+newf;
            if (allt.length() >= 12) {

            }
            if (allt.length() == 11) {
                allt = " "+allt;
            }
            if (allt.length() == 10) {
                allt = "  "+allt;
            }
            if (allt.length() == 9) {
                allt = "   "+allt;
            }
            if (allt.length() == 8) {
                allt = "    "+allt;
            }
            if (allt.length() == 7) {
                allt = "     "+allt;
            }
            if (allt.length() == 6) {
                allt = "      "+allt;
            }
            if (allt.length() == 5) {
                allt = "       "+allt;
            }
            if (allt.length() == 4) {
                allt = "        "+allt;
            }

            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

            if (statussusbs.equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHT32122);    //
                BluetoothPrintDriver.BT_Write(bold);    //
                BluetoothPrintDriver.BT_Write(left);    //
                BT_Write("Total");
                BluetoothPrintDriver.BT_Write(HT);    //
//                BT_Write(insert1_rs);
//                BT_Write(newf);
                BT_Write(allt);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.equals("ok")) {
                    wifiSocket2.WIFI_Write(setHT32122);    //
                    wifiSocket2.WIFI_Write(bold);    //
                    wifiSocket2.WIFI_Write(left);    //
                    wifiSocket2.WIFI_Write("Total");
                    wifiSocket2.WIFI_Write(HT);    //
//                    wifiSocket2.WIFI_Write(insert1_rs);
//                    wifiSocket2.WIFI_Write(newf);
                    wifiSocket2.WIFI_Write(allt);    //
                    wifiSocket2.WIFI_Write(LF);    //
                }else {
                    if (statusnets.equals("ok")) {
                        wifiSocket.WIFI_Write(setHT32122);    //
                        wifiSocket.WIFI_Write(bold);    //
                        wifiSocket.WIFI_Write(left);    //
                        wifiSocket.WIFI_Write("Total");
                        wifiSocket.WIFI_Write(HT);    //
//                        wifiSocket.WIFI_Write(insert1_rs);
//                        wifiSocket.WIFI_Write(newf);
                        wifiSocket.WIFI_Write(allt);    //
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }

            if (statussusbs.equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(normal);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BT_Write(str_line);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.equals("ok")) {
                    wifiSocket2.WIFI_Write(left);	//
                    wifiSocket2.WIFI_Write(normal);	//
                    wifiSocket2.WIFI_Write(un1);	//
                    wifiSocket2.WIFI_Write(str_line);
                    wifiSocket2.WIFI_Write(LF);	//
                }else {
                    if (statusnets.equals("ok")) {
                        wifiSocket.WIFI_Write(left);	//
                        wifiSocket.WIFI_Write(normal);	//
                        wifiSocket.WIFI_Write(un1);	//
                        wifiSocket.WIFI_Write(str_line);
                        wifiSocket.WIFI_Write(LF);	//
                    }
                }
            }

            tvkot.setText(bill_coun);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf7 = new byte[][]{
                        normal, bill_coun.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.equals("ok")) {
                    BluetoothPrintDriver.BT_Write(normal);    //
                    BluetoothPrintDriver.BT_Write(left);    //
                    BT_Write("Bill id.");
                    BT_Write(bill_coun);
                    BluetoothPrintDriver.BT_Write(LF);    //
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.equals("ok")) {
                        wifiSocket2.WIFI_Write(normal);    //
                        wifiSocket2.WIFI_Write(left);    //
                        wifiSocket2.WIFI_Write("Bill id.");
                        wifiSocket2.WIFI_Write(bill_coun);
                        wifiSocket2.WIFI_Write(LF);    //
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.equals("ok")) {
                            wifiSocket.WIFI_Write(normal);    //
                            wifiSocket.WIFI_Write(left);    //
                            wifiSocket.WIFI_Write("Bill id.");
                            wifiSocket.WIFI_Write(bill_coun);
                            wifiSocket.WIFI_Write(LF);    //
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }


            footer();
        }
    }
    public  byte[] neoprintbillcopy1(Dialog dialog1) {


        Typeface tf = Typeface.SERIF;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ReceiptBitmap receiptBitmap = new ReceiptBitmap().getInstance();
        int cont=5;
        Cursor cursor34 = db.rawQuery("Select count(*) from All_Sales WHERE bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (cursor34.moveToFirst()) {
            cont=cursor34.getInt(0);

        }
        Log.e("count:",cont+"");
        cursor34.close();
        receiptBitmap.init(700+(cont*50));
        receiptBitmap.setTextSize(25);
        receiptBitmap.setTypeface(Typeface.create(tf, Typeface.NORMAL));


        charlength = 10;
        charlength1 = 20;
        charlength2 = 30;
        quanlentha = 5;

        Cursor getcom = db1.rawQuery("SELECT * FROM Companydetailss", null);
        if (getcom.moveToFirst()) {
            strcompanyname = getcom.getString(1);
            strbillone = getcom.getString(12);
        }
        getcom.close();

        tvkot.setText(strcompanyname);
        if (tvkot.getText().toString().equals("")) {

        } else {
            // Print.StartPrinting(strcompanyname ,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
            receiptBitmap.drawCenterText(strcompanyname);
        }

        receiptBitmap.drawCenterText("Bill copy");


        String str_line="----------------------";
        receiptBitmap.drawLeftText(str_line);



        //   Print.StartPrinting(NAme111+"   "+tablen,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        receiptBitmap.drawLeftText("Bill id."+bill_coun);
        receiptBitmap.drawLeftText("Bill no." + billnumb);

        Cursor cursor10 = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billnumb + "'", null);
        if (cursor10.moveToFirst()) {
            billtypea = cursor10.getString(5);
            paymmethoda = cursor10.getString(6);
        }
        cursor10.close();

        TextView ttv = new TextView(Preparing_Orders_w.this);
        ttv.setText(billtypea);

        TextView ttv1 = new TextView(Preparing_Orders_w.this);
        ttv1.setText(paymmethoda);

//            if (ttv.getText().toString().equals("  Cash")) {
//                billtypeaa = "Cash";
//            } else {
//                billtypeaa = "Card";
//            }

        if (ttv.getText().toString().equals("  Cash")) {
            billtypeaa = "Cash"; //0
        }
        if (ttv.getText().toString().equals("  Card")) {
            billtypeaa = "Card"; //0
        }
        if (ttv.getText().toString().equals("  Paytm")) {
            billtypeaa = "Paytm"; //0
        }
        if (ttv.getText().toString().equals("  Mobikwik")) {
            billtypeaa = "Mobikwik"; //0
        }
        if (ttv.getText().toString().equals("  Freecharge")) {
            billtypeaa = "Freecharge"; //0
        }
        if (ttv.getText().toString().equals("  Pay Later")) {
            billtypeaa = "Pay Later"; //0
        }
        if (ttv.getText().toString().equals("  Cheque")) {
            billtypeaa = "Cheque"; //0
        }
        if (ttv.getText().toString().equals("  Sodexo")) {
            billtypeaa = "Sodexo"; //0
        }
        if (ttv.getText().toString().equals("  Zeta")) {
            billtypeaa = "Zeta"; //0
        }
        if (ttv.getText().toString().equals("  Ticket")) {
            billtypeaa = "Ticket"; //0
        }
        if (ttv.getText().toString().equals("  Upiqr")) {
            billtypeaa = "Upiqr"; //0
        }

        if (ttv1.getText().toString().equals("  Dine-in") || ttv1.getText().toString().equals("  General") || ttv1.getText().toString().equals("  Others")) {
            paymmethodaa = "Dine-in";
            //billtypee.setText("Dine-in");
        } else {
            if (ttv1.getText().toString().equals("  Takeaway") || ttv1.getText().toString().equals("  Main")) {
                paymmethodaa = "Takeaway";
                //billtypee.setText("Takeaway");
            } else {
                paymmethodaa = "Home delivery";
                //billtypee.setText("Home delivery");
            }
        }

        Cursor date = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "'", null);
        if (date.moveToFirst()) {
            datee = date.getString(25);
            timee = date.getString(12);
        }
        date.close();


        receiptBitmap.drawLeftText(paymmethodaa+"   "+datee);

        Cursor cursor9 = db.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billnumb + "'", null);
        if (cursor9.moveToFirst()) {
            tableida = cursor9.getString(15);
            Cursor vbnm = db1.rawQuery("SELECT * FROM asd1 WHERE _id = '" + tableida + "'", null);
            if (vbnm.moveToFirst()) {
                assa1 = vbnm.getString(1);
                assa2 = vbnm.getString(2);
            }
            vbnm.close();
            TextView cx = new TextView(Preparing_Orders_w.this);
            cx.setText(assa1);

            if (cx.getText().toString().equals("")) {
                tableidaa = "Tab" + assa2;
            } else {
                tableidaa = "Tab" + assa1;
            }

        }
        cursor9.close();

        receiptBitmap.drawLeftText(tableidaa+"   "+timee);

        Cursor cursor9_1 = db.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billnumb + "'", null);
        if (cursor9_1.moveToFirst()) {
            u_name = cursor9_1.getString(45);
        }
        cursor9_1.close();

        TextView tv_u_name = new TextView(Preparing_Orders_w.this);
        tv_u_name.setText(u_name);

        receiptBitmap.drawLeftText("Counter person  : "+u_name);

        receiptBitmap.drawLeftText(str_line);

        Cursor caddress = db.rawQuery("SELECT * FROM Customerdetails WHERE billnumber = '" + billnumb + "'", null);
        if (caddress.moveToFirst()) {
            String nam = caddress.getString(1);
            String addr = caddress.getString(4);
            String phon = caddress.getString(2);
            String emai = caddress.getString(3);

            if (nam.length() > 0 || addr.length() > 0 ||
                    phon.length() > 0 || emai.length() > 0) {

                receiptBitmap.drawLeftText("Customer:");

            } else {

            }

            if (nam.length() > 0) {
                receiptBitmap.drawLeftText(nam);
            } else {

            }

            if (addr.length() > 0) {
                receiptBitmap.drawLeftText(addr);
            } else {

            }

            if (phon.length() > 0) {
                String cust_ph = "Ph. " + phon;
                receiptBitmap.drawLeftText(cust_ph);
            } else {

            }

            if (emai.length() > 0) {
                receiptBitmap.drawLeftText(emai);
            } else {

            }

            receiptBitmap.drawLeftText(str_line);

        }
        caddress.close();

        receiptBitmap.drawLeftText("Qty"+"  "+"Item"+"  "+"Price"+"  "+"Amount");
        receiptBitmap.drawLeftText(str_line);



        Cursor ccursorr = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (ccursorr.moveToFirst()) {

            do {

                String nbg = ccursorr.getString(0);
                String name = ccursorr.getString(1);
                String value = ccursorr.getString(2);
                String pq = ccursorr.getString(5);
                String itna = ccursorr.getString(2);
                String pricee = ccursorr.getString(3);
                String tototot = ccursorr.getString(4);

                final String newid = ccursorr.getString(20);
                int padding_in_px;

                int padding_in_dp = 30;  // 34 dps
                final float scale1 = getResources().getDisplayMetrics().density;
                padding_in_px = (int) (padding_in_dp * scale1 + 0.5f);


                if (pq.equals("Item")) {
                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 4.5f));


                    final TableRow row1 = new TableRow(Preparing_Orders_w.this);
                    row1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));

                    final TableRow row2 = new TableRow(Preparing_Orders_w.this);
                    row2.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    final TableLayout tableLayout1 = new TableLayout(Preparing_Orders_w.this);

                    TableRow.LayoutParams lp, lp1, lp2;

                    TextView tv = new TextView(Preparing_Orders_w.this);
                    tv.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 0.70f));
                    tv.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv.setText(value);
                    row.addView(tv);

                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.6f));
                    tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv1.setGravity(Gravity.CENTER_VERTICAL);
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv1.setText(name);
                    String value1 = tv1.getText().toString();
                    row.addView(tv1);

                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.0f));
                    tv2.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setText(pricee);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(tv2);

                    TextView tv3 = new TextView(Preparing_Orders_w.this);
                    tv3.setLayoutParams(new android.widget.TableRow.LayoutParams(0, padding_in_px, 1.2f));
                    tv2.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv3.setText(tototot);

                    String value2 = tv3.getText().toString();

                    Cursor modcursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND parent = '" + name + "' AND parentid = '" + newid + "' ", null);

                    if (modcursor.moveToFirst()) {

                        Cursor cursor4 = db.rawQuery("SELECT SUM(total) FROM All_Sales WHERE bill_no = '" + billnumb + "'AND parent = '" + name + "' AND parentid = '" + newid + "'", null);
                        if (cursor4.moveToFirst()) {
                            float sub2a = cursor4.getFloat(0);
                            String sub2a1 = String.valueOf(sub2a);
                            ss = Float.parseFloat(sub2a1) + Float.parseFloat(tototot);
                            ss1 = String.valueOf(ss);
                        }
                        cursor4.close();

                        if (name.length() > charlength) {
                            int print1 = 0;
//                                Toast.makeText(Preparing_Orders_w.this, "1", Toast.LENGTH_SHORT).show();

                            if (value.length() > quanlentha && name.length() > charlength) {
//                                    Toast.makeText(Preparing_Orders_w.this, "2", Toast.LENGTH_SHORT).show();
                                String string1quan = value.substring(0, quanlentha);
                                String string2quan = value.substring(quanlentha);
                                String string1 = name.substring(0, charlength);
                                String string2 = name.substring(charlength);

                                if (print1 == 0){
                                    receiptBitmap.drawLeftText(string1quan+"  "+string1+"  "+pricee+"  "+ss1);
                                    receiptBitmap.drawLeftText(string2quan+"  "+string2);
                                    print1 = 1;
                                }

                            }
                            if (value.length() <= quanlentha && name.length() > charlength) {
//                                    Toast.makeText(Preparing_Orders_w.this, "3", Toast.LENGTH_SHORT).show();
                                String string1 = name.substring(0, charlength);
                                String string2 = name.substring(charlength);

                                if (print1 == 0){
                                    receiptBitmap.drawLeftText(value+"  "+string1+"  "+pricee+"  "+ss1);
                                    receiptBitmap.drawLeftText("      "+string2);
                                    print1 = 1;
                                }

                            }

                            if (value.length() > quanlentha && name.length() > charlength1) {
//                                    Toast.makeText(Preparing_Orders_w.this, "4", Toast.LENGTH_SHORT).show();
                                String string1quan = value.substring(0, quanlentha);
                                String string2quan = value.substring(quanlentha);
                                String string1 = name.substring(0, charlength);
                                String string2 = name.substring(charlength, charlength1);
                                String string3 = name.substring(charlength1);

                                if (print1 == 0){
                                    receiptBitmap.drawLeftText(string1quan+"  "+string1+"  "+pricee+"  "+ss1);
                                    receiptBitmap.drawLeftText(string2quan+"  "+string2+"      "+string3);
                                    print1 = 1;
                                }

                            }
                            if (value.length() <= quanlentha && name.length() > charlength1) {
//                                    Toast.makeText(Preparing_Orders_w.this, "5", Toast.LENGTH_SHORT).show();
                                String string1 = name.substring(0, charlength);
                                String string2 = name.substring(charlength, charlength1);
                                String string3 = name.substring(charlength1);

                                if(print1 == 0){
                                    receiptBitmap.drawLeftText(value+"  "+string1+"  "+pricee+"  "+ss1);
                                    receiptBitmap.drawLeftText("      "+string2+"      "+string3);
                                    print1 = 1;
                                }

                            }

                            Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                            if (ccursor.moveToFirst()) {
                                String hsn = ccursor.getString(34);

                                TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                hsn_hsn.setText(hsn);

                                if (hsn_hsn.getText().toString().equals("")) {
                                } else {
                                    receiptBitmap.drawLeftText("HSN "+hsn);
                                }
                            }

                        } else {
//                                Toast.makeText(Preparing_Orders_w.this, "6", Toast.LENGTH_SHORT).show();
                            if (value.length() > quanlentha) {
//                                    Toast.makeText(Preparing_Orders_w.this, "7", Toast.LENGTH_SHORT).show();
                                String string1quan = value.substring(0, quanlentha);
                                String string2quan = value.substring(quanlentha);

                                receiptBitmap.drawLeftText(string1quan+"  "+name+"  "+pricee+"  "+ss1);
                                receiptBitmap.drawLeftText(string2quan);

                                Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                if (ccursor.moveToFirst()) {
                                    String hsn = ccursor.getString(34);

                                    TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                    hsn_hsn.setText(hsn);

                                    if (hsn_hsn.getText().toString().equals("")) {
                                    } else {
                                        receiptBitmap.drawLeftText("HSN "+hsn);
                                    }
                                }

                            } else {
//                                    Toast.makeText(Preparing_Orders_w.this, "8", Toast.LENGTH_SHORT).show();
                                receiptBitmap.drawLeftText(value+"  "+name+"  "+pricee+"  "+ss1);

                                Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                if (ccursor.moveToFirst()) {
                                    String hsn = ccursor.getString(34);

                                    TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                    hsn_hsn.setText(hsn);

                                    if (hsn_hsn.getText().toString().equals("")) {
                                    } else {
                                        receiptBitmap.drawLeftText("HSN "+hsn);
                                    }
                                }

                            }

                        }

                        do {

                            final String modiname = modcursor.getString(1);
                            final String modiquan = modcursor.getString(2);
                            String modiprice = modcursor.getString(3);
                            String moditotal = modcursor.getString(4);
                            final String modiid = modcursor.getString(0);

                            float modprice1 = Float.parseFloat(modiprice);
                            String modpricestr = String.valueOf(modprice1);

                            if (modiname.length() > charlength) {
                                if (modiname.length() > charlength) {
                                    String string1 = modiname.substring(0, charlength);
                                    String string2 = modiname.substring(charlength);

                                    receiptBitmap.drawLeftText(""+"  "+">"+string1+"  "+modpricestr);
                                    receiptBitmap.drawLeftText("    "+string2);

                                }
                                if (modiname.length() > charlength1) {
                                    String string1 = modiname.substring(0, charlength);
                                    String string2 = modiname.substring(charlength, charlength1);
                                    String string3 = modiname.substring(charlength1);

                                    receiptBitmap.drawLeftText(""+"  "+">"+string1+"  "+modpricestr);
                                    receiptBitmap.drawLeftText("    "+string2+"    "+string3);

                                }

                                Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                if (ccursor.moveToFirst()) {
                                    String hsn = ccursor.getString(34);

                                    TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                    hsn_hsn.setText(hsn);

                                    if (hsn_hsn.getText().toString().equals("")) {
                                    } else {
                                        receiptBitmap.drawLeftText("HSN "+hsn);
                                    }
                                }

                            } else {

                                receiptBitmap.drawLeftText(""+"  "+">"+modiname+"  "+modpricestr);

                                Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                if (ccursor.moveToFirst()) {
                                    String hsn = ccursor.getString(34);

                                    TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                    hsn_hsn.setText(hsn);

                                    if (hsn_hsn.getText().toString().equals("")) {
                                    } else {
                                        receiptBitmap.drawLeftText("HSN "+hsn);
                                    }
                                }

                            }

                            final TableRow tableRow11 = new TableRow(Preparing_Orders_w.this);
                            tableRow11.setLayoutParams(new TableLayout.LayoutParams(
                                    TableRow.LayoutParams.MATCH_PARENT,
                                    TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                            final TextView tvv = new TextView(Preparing_Orders_w.this);
                            // tv.setLayoutParams(new TableRow.LayoutParams(40, TableRow.LayoutParams.MATCH_PARENT));
                            tvv.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 0.70f));
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            //tv.setGravity(Gravity.CENTER);
                            tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tvv.setText("");
                            tableRow11.addView(tvv);

                            TextView tv4 = new TextView(Preparing_Orders_w.this);
                            //lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                            tv4.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.6f));
                            //tv3.setPadding(5, 0, 0, 0);
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            tv4.setText(modiname);
                            tv4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            tv4.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tv4.setGravity(Gravity.CENTER_VERTICAL);
                            //tv3.setBackgroundColor(getResources().getColor(R.color.six));
                            //tv3.setTextColor(R.color.black);
                            tableRow11.addView(tv4);

                            TextView tv5 = new TextView(Preparing_Orders_w.this);
                            //lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                            tv5.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.0f));
                            //tv3.setPadding(5, 0, 0, 0);
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            tv5.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                            tv5.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            //tv2.setPadding(0, 0, 1, 0);
                            tv5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            tv5.setText(modiprice);
                            //tv3.setBackgroundColor(getResources().getColor(R.color.six));
                            //tv3.setTextColor(R.color.black);
                            tableRow11.addView(tv5);

                            TextView tv6 = new TextView(Preparing_Orders_w.this);
                            //lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                            tv6.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                            //tv3.setPadding(5, 0, 0, 0);
                            tv6.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                            tv6.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tv6.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            tv6.setText("");
                            //tv3.setBackgroundColor(getResources().getColor(R.color.six));
                            //tv3.setTextColor(R.color.black);
                            tableRow11.addView(tv6);


                            final TextView tv7 = new TextView(Preparing_Orders_w.this);
                            //tv3.setLayoutParams(new TableRow.LayoutParams(90, TableRow.LayoutParams.MATCH_PARENT));
                            tv7.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                            tv7.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                            tv7.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            //tv3.setPadding(0,0,10,0);
                            tv7.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                            //String modtotal = String.valueOf(Integer.parseInt(modiquan) * Integer.parseInt(modiprice));

                            final String number = tv.getText().toString();
                            float newmul = Float.parseFloat(number);
                            //final float in = Float.parseFloat(cursor.getString(4));
                            String multiply = String.valueOf(newmul * Float.parseFloat(pricee));
                            //newmul = Integer.parseInt(multiply);
                            //tv3.setText(String.valueOf(Float.parseFloat(multiply)+Float.parseFloat(modtotal)));
                            //row.addView(tv3);

                            row.removeView(tv8);


                            tv8 = new TextView(Preparing_Orders_w.this);
                            tv8.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                            //tv3.setLayoutParams(new TableRow.LayoutParams(90, TableRow.LayoutParams.MATCH_PARENT));
                            //tv3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            tv8.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                            //tv3.setPadding(0, 0, 10, 0);
                            tv8.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tv8.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                            final String numberr = tv.getText().toString();
                            float newmulr = Float.parseFloat(numberr);
                            //final float in = Float.parseFloat(cursor.getString(4));
                            String multiplyr = String.valueOf(newmulr * Float.parseFloat(pricee));
                            //newmul = Integer.parseInt(multiply);
                            tv8.setText(String.valueOf(ss));
                            row.addView(tv8);


                            tableLayout1.addView(tableRow11);
                        } while (modcursor.moveToNext());

                        //Cursor modcursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND parent = '" + name + "' AND parentid = '" + newid + "' ", null);
                        Cursor disc_cursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '" + name + "' AND _id = '" + nbg + "'  ", null);
                        if (disc_cursor.moveToFirst()) {
                            do {
                                String disc_vv = disc_cursor.getString(12);
                                String disc_there = disc_cursor.getString(30);
                                float vtq = disc_cursor.getFloat(31);
                                if (disc_there.equals("no")) {

                                } else {

                                    total_disc_print_q = String.valueOf(vtq);

                                    receiptBitmap.drawLeftText(""+"  "+""+"  "+""+"  "+"("+"-"+total_disc_print_q+")");

                                }
                            } while (disc_cursor.moveToNext());
                        }
                        disc_cursor.close();

                    } else {

                        if (name.length() > charlength) {
//                                Toast.makeText(Preparing_Orders_w.this, "10", Toast.LENGTH_SHORT).show();
                            int print1 = 0;
                            if (value.length() > quanlentha && name.length() > charlength) {
//                                    Toast.makeText(Preparing_Orders_w.this, "11", Toast.LENGTH_SHORT).show();
                                String string1quan = value.substring(0, quanlentha);
                                String string2quan = value.substring(quanlentha);
                                String string1 = name.substring(0, charlength);
                                String string2 = name.substring(charlength);

                                if (print1 == 0) {
                                    receiptBitmap.drawLeftText(string1quan+"  "+string1+"  "+pricee+"  "+tototot);
                                    receiptBitmap.drawLeftText(string2quan+"  "+string2);
                                    print1 = 1;
                                }

                            }
                            if (value.length() <= quanlentha && name.length() > charlength) {
//                                    Toast.makeText(Preparing_Orders_w.this, "12", Toast.LENGTH_SHORT).show();
                                String string1 = name.substring(0, charlength);
                                String string2 = name.substring(charlength);

                                if (print1 == 0) {
                                    receiptBitmap.drawLeftText(value+"  "+string1+"  "+pricee+"  "+tototot);
                                    receiptBitmap.drawLeftText("      "+string2);
                                    print1 = 1;
                                }

                            }
////////////////////////////////////////////////
                            if (value.length() > quanlentha && name.length() > charlength1) {
//                                    Toast.makeText(Preparing_Orders_w.this, "13", Toast.LENGTH_SHORT).show();
                                String string1quan = value.substring(0, quanlentha);
                                String string2quan = value.substring(quanlentha);
                                String string1 = name.substring(0, charlength);
                                String string2 = name.substring(charlength, charlength1);
                                String string3 = name.substring(charlength1);

                                if (print1 == 0) {
                                    receiptBitmap.drawLeftText(string1quan+"  "+string1+"  "+pricee+"  "+tototot);
                                    receiptBitmap.drawLeftText(string2quan+"  "+string2);
                                    receiptBitmap.drawLeftText("      "+string3);
                                    print1 = 1;
                                }

                            }
                            if (value.length() <= quanlentha && name.length() > charlength1) {
//                                    Toast.makeText(Preparing_Orders_w.this, "14", Toast.LENGTH_SHORT).show();
                                String string1 = name.substring(0, charlength);
                                String string2 = name.substring(charlength, charlength1);
                                String string3 = name.substring(charlength1);

                                if (print1 == 0) {
                                    receiptBitmap.drawLeftText(value+"  "+string1+"  "+pricee+"  "+tototot);
                                    receiptBitmap.drawLeftText("      "+string2);
                                    receiptBitmap.drawLeftText("      "+string3);
                                    print1 = 1;
                                }

                            }

                            Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                            if (ccursor.moveToFirst()) {
                                String hsn = ccursor.getString(34);

                                TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                hsn_hsn.setText(hsn);

                                if (hsn_hsn.getText().toString().equals("")) {
                                } else {
                                    receiptBitmap.drawLeftText("HSN "+hsn);
                                }
                            }

                        } else {
                            if (value.length() > quanlentha) {
//                                    Toast.makeText(Preparing_Orders_w.this, "15", Toast.LENGTH_SHORT).show();
                                String string1quan = value.substring(0, quanlentha);
                                String string2quan = value.substring(quanlentha);

                                receiptBitmap.drawLeftText(string1quan+"  "+name+"  "+pricee+"  "+tototot);
                                receiptBitmap.drawLeftText(string2quan);

                            } else {
//                                    Toast.makeText(Preparing_Orders_w.this, "16", Toast.LENGTH_SHORT).show();
                                receiptBitmap.drawLeftText(value+"  "+name+"  "+pricee+"  "+tototot);
                            }

                            Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                            if (ccursor.moveToFirst()) {
                                String hsn = ccursor.getString(34);

                                TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                hsn_hsn.setText(hsn);

                                if (hsn_hsn.getText().toString().equals("")) {
                                } else {
                                    receiptBitmap.drawLeftText("HSN "+hsn);
                                }
                            }


                        }

                        tv8 = new TextView(Preparing_Orders_w.this);
                        tv8.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                        //tv3.setLayoutParams(new TableRow.LayoutParams(90, TableRow.LayoutParams.MATCH_PARENT));
                        //tv3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tv8.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        //tv3.setPadding(0, 0, 10, 0);
                        tv8.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv8.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                        final String number = tv.getText().toString();
                        float newmul = Float.parseFloat(number);
                        //final float in = Float.parseFloat(cursor.getString(4));
                        String multiply = String.valueOf(newmul * Float.parseFloat(pricee));
                        //newmul = Integer.parseInt(multiply);
                        tv8.setText(String.valueOf(multiply));
                        row.addView(tv8);

                        Cursor disc_cursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '" + name + "' AND _id = '" + nbg + "'  ", null);
                        if (disc_cursor.moveToFirst()) {
                            do {
                                String disc_vv = disc_cursor.getString(12);
                                String disc_there = disc_cursor.getString(30);
                                float vtq = disc_cursor.getFloat(31);
                                if (disc_there.equals("no")) {

                                } else {

                                    total_disc_print_q = String.valueOf(vtq);

                                    receiptBitmap.drawLeftText(""+"  "+""+"  "+""+"  "+"("+"-"+total_disc_print_q+")");
                                }
                            } while (disc_cursor.moveToNext());
                        }
                        disc_cursor.close();
                    }
                    modcursor.close();
                }


            } while (ccursorr.moveToNext());
        }
        ccursorr.close();



        receiptBitmap.drawLeftText(str_line);


////////////////////////////////////sub total
        Cursor cursor3 = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billnumb + "'", null);
        if (cursor3.moveToFirst()) {
            sub = cursor3.getString(7);
        }
        cursor3.close();


//        int sub12 = sub1;
//        String total2 = String.valueOf(sub12);
        float to = Float.parseFloat(sub);
        String tot = String.valueOf(to);

        receiptBitmap.drawLeftText("Sub total"+"                       "+tot);

/////////////////////////tax
        TableLayout tableLayout1 = new TableLayout(Preparing_Orders_w.this);
        tableLayout1.removeAllViews();

        Cursor ccursor = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (ccursor.moveToFirst()) {

            do {
                String name = ccursor.getString(10);
                String value = ccursor.getString(9);
                String pq = ccursor.getString(4);
                String itna = ccursor.getString(1);

                TextView v = new TextView(Preparing_Orders_w.this);
                v.setText(value);

                TextView v1 = new TextView(Preparing_Orders_w.this);
                v1.setText(name);
                if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                        || v.getText().toString().equals("")) {

                } else {
                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                    TableRow.LayoutParams lp, lp1, lp2;

                    TextView tvv = new TextView(Preparing_Orders_w.this);
                    tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    //tv.setBackgroundResource(R.drawable.cell_shape);
                    tvv.setGravity(Gravity.START);
                    tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tvv.setText(name);

                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv1.setGravity(Gravity.START);
                    tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv1.setText(value);
                    float vbn = Float.parseFloat(value);
                    String bvn = String.format(Locale.US,"%.2f", vbn);
                    String value1 = tv1.getText().toString();

                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                    tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv2.append(name + "@" + bvn + "%");
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setPadding(0, 0, 20, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    //tv2.setTextColor(Color.parseColor("#000000"));
                    row.addView(tv2);

                    TextView textView1 = new TextView(Preparing_Orders_w.this);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(textView1);

                    TextView tv3 = new TextView(Preparing_Orders_w.this);
                    tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                    tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                    String tota1 = String.format(Locale.US,"%.2f", tota);
                    tv3.setText(String.valueOf(tota));
                    tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    String value2 = tv3.getText().toString();
                    row.addView(tv3);

                    tableLayout1.addView(row);
                }
            } while (ccursor.moveToNext());
        }
        ccursor.close();

        Cursor ccursor2 = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (ccursor2.moveToFirst()) {

            do {
                String name = ccursor2.getString(35);
                String value = ccursor2.getString(36);
                String pq = ccursor2.getString(4);
                String itna = ccursor2.getString(1);

                TextView v = new TextView(Preparing_Orders_w.this);
                v.setText(value);

                TextView v1 = new TextView(Preparing_Orders_w.this);
                v1.setText(name);
                if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                        || v.getText().toString().equals("")) {

                } else {
                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                    TableRow.LayoutParams lp, lp1, lp2;

                    TextView tvv = new TextView(Preparing_Orders_w.this);
                    tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    //tv.setBackgroundResource(R.drawable.cell_shape);
                    tvv.setGravity(Gravity.START);
                    tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tvv.setText(name);

                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv1.setGravity(Gravity.START);
                    tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv1.setText(value);
                    float vbn = Float.parseFloat(value);
                    String bvn = String.format(Locale.US,"%.2f", vbn);
                    String value1 = tv1.getText().toString();

                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                    tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv2.append(name + "@" + bvn + "%");
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setPadding(0, 0, 20, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    //tv2.setTextColor(Color.parseColor("#000000"));
                    row.addView(tv2);

                    TextView textView1 = new TextView(Preparing_Orders_w.this);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(textView1);

                    TextView tv3 = new TextView(Preparing_Orders_w.this);
                    tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                    tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                    String tota1 = String.format(Locale.US,"%.2f", tota);
                    tv3.setText(String.valueOf(tota));
                    tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    String value2 = tv3.getText().toString();
                    row.addView(tv3);

                    tableLayout1.addView(row);
                }
            } while (ccursor2.moveToNext());
        }
        ccursor2.close();

        Cursor ccursor3 = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (ccursor3.moveToFirst()) {

            do {
                String name = ccursor3.getString(37);
                String value = ccursor3.getString(38);
                String pq = ccursor3.getString(4);
                String itna = ccursor3.getString(1);

                TextView v = new TextView(Preparing_Orders_w.this);
                v.setText(value);

                TextView v1 = new TextView(Preparing_Orders_w.this);
                v1.setText(name);
                if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                        || v.getText().toString().equals("")) {

                } else {
                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                    TableRow.LayoutParams lp, lp1, lp2;

                    TextView tvv = new TextView(Preparing_Orders_w.this);
                    tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    //tv.setBackgroundResource(R.drawable.cell_shape);
                    tvv.setGravity(Gravity.START);
                    tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tvv.setText(name);

                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv1.setGravity(Gravity.START);
                    tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv1.setText(value);
                    float vbn = Float.parseFloat(value);
                    String bvn = String.format(Locale.US,"%.2f", vbn);
                    String value1 = tv1.getText().toString();

                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                    tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv2.append(name + "@" + bvn + "%");
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setPadding(0, 0, 20, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    //tv2.setTextColor(Color.parseColor("#000000"));
                    row.addView(tv2);

                    TextView textView1 = new TextView(Preparing_Orders_w.this);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(textView1);

                    TextView tv3 = new TextView(Preparing_Orders_w.this);
                    tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                    tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                    String tota1 = String.format(Locale.US,"%.2f", tota);
                    tv3.setText(String.valueOf(tota));
                    tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    String value2 = tv3.getText().toString();
                    row.addView(tv3);

                    tableLayout1.addView(row);
                }
            } while (ccursor3.moveToNext());
        }
        ccursor3.close();

        Cursor ccursor4 = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (ccursor4.moveToFirst()) {

            do {
                String name = ccursor4.getString(39);
                String value = ccursor4.getString(40);
                String pq = ccursor4.getString(4);
                String itna = ccursor4.getString(1);

                TextView v = new TextView(Preparing_Orders_w.this);
                v.setText(value);

                TextView v1 = new TextView(Preparing_Orders_w.this);
                v1.setText(name);
                if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                        || v.getText().toString().equals("")) {

                } else {
                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                    TableRow.LayoutParams lp, lp1, lp2;

                    TextView tvv = new TextView(Preparing_Orders_w.this);
                    tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    //tv.setBackgroundResource(R.drawable.cell_shape);
                    tvv.setGravity(Gravity.START);
                    tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tvv.setText(name);

                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv1.setGravity(Gravity.START);
                    tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv1.setText(value);
                    float vbn = Float.parseFloat(value);
                    String bvn = String.format(Locale.US,"%.2f", vbn);
                    String value1 = tv1.getText().toString();

                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                    tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv2.append(name + "@" + bvn + "%");
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setPadding(0, 0, 20, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    //tv2.setTextColor(Color.parseColor("#000000"));
                    row.addView(tv2);

                    TextView textView1 = new TextView(Preparing_Orders_w.this);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(textView1);

                    TextView tv3 = new TextView(Preparing_Orders_w.this);
                    tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                    tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                    String tota1 = String.format(Locale.US,"%.2f", tota);
                    tv3.setText(String.valueOf(tota));
                    tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    String value2 = tv3.getText().toString();
                    row.addView(tv3);

                    tableLayout1.addView(row);
                }
            } while (ccursor4.moveToNext());
        }
        ccursor4.close();

        Cursor ccursor5 = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (ccursor5.moveToFirst()) {

            do {
                String name = ccursor5.getString(41);
                String value = ccursor5.getString(42);
                String pq = ccursor5.getString(4);
                String itna = ccursor5.getString(1);

                TextView v = new TextView(Preparing_Orders_w.this);
                v.setText(value);

                TextView v1 = new TextView(Preparing_Orders_w.this);
                v1.setText(name);
                if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                        || v.getText().toString().equals("")) {

                } else {
                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                    TableRow.LayoutParams lp, lp1, lp2;

                    TextView tvv = new TextView(Preparing_Orders_w.this);
                    tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    //tv.setBackgroundResource(R.drawable.cell_shape);
                    tvv.setGravity(Gravity.START);
                    tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tvv.setText(name);

                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv1.setGravity(Gravity.START);
                    tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv1.setText(value);
                    float vbn = Float.parseFloat(value);
                    String bvn = String.format(Locale.US,"%.2f", vbn);
                    String value1 = tv1.getText().toString();

                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                    tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv2.append(name + "@" + bvn + "%");
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setPadding(0, 0, 20, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    //tv2.setTextColor(Color.parseColor("#000000"));
                    row.addView(tv2);

                    TextView textView1 = new TextView(Preparing_Orders_w.this);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(textView1);

                    TextView tv3 = new TextView(Preparing_Orders_w.this);
                    tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                    tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                    String tota1 = String.format(Locale.US,"%.2f", tota);
                    tv3.setText(String.valueOf(tota));
                    tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    String value2 = tv3.getText().toString();
                    row.addView(tv3);

                    tableLayout1.addView(row);
                }
            } while (ccursor5.moveToNext());
        }
        ccursor5.close();


        ArrayList<String> groupList = new ArrayList<String>();

        float sum_p = 0;
        for (int i = 0; i < tableLayout1.getChildCount(); i++) {
            TableRow mRow = (TableRow) tableLayout1.getChildAt(i);
            TextView mTextView = (TextView) mRow.getChildAt(0);
//                                Toast.makeText(BeveragesMenuFragment.this, "a "+mTextView.getText().toString(), Toast.LENGTH_LONG).show();

            if (groupList.contains(mTextView.getText().toString())) {

            }else {
                sum_p = 0;
                for (int j = 0; j < tableLayout1.getChildCount(); j++) {
                    TableRow mRow1 = (TableRow) tableLayout1.getChildAt(j);
                    mTextView1 = (TextView) mRow1.getChildAt(0);
                    mTextView2 = (TextView) mRow1.getChildAt(2);
                    if (groupList.contains(mTextView.getText().toString())) {
                        if (mTextView.getText().toString().equals(mTextView1.getText().toString())) {
                            sum_p = sum_p+Float.parseFloat(mTextView2.getText().toString());
//                                                Toast.makeText(BeveragesMenuFragment.this, "b " + mTextView2.getText().toString()+" "+sum, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (mTextView.getText().toString().equals(mTextView1.getText().toString())) {
                            groupList.add(mTextView.getText().toString());
                            sum_p = sum_p+Float.parseFloat(mTextView2.getText().toString());
//                                                Toast.makeText(BeveragesMenuFragment.this, "c " + mTextView2.getText().toString()+" "+sum, Toast.LENGTH_LONG).show();
                        }
                    }
                }
//                    Toast.makeText(Preparing_Orders_w.this, "aa "+mTextView.getText().toString() +" "+sum_p, Toast.LENGTH_LONG).show();

                String mod1 = mTextView.getText().toString() + "---" + String.format("%.2f", sum_p);

                receiptBitmap.drawLeftText(mod1);

                String match = "@";
                int position = mTextView.getText().toString().indexOf(match);
                String mod2 = mTextView.getText().toString().substring(0, position);//keep toastmessage
//                    Toast.makeText(Preparing_Orders_w.this, " "+mod2, Toast.LENGTH_LONG).show();
                Cursor ccursor6 = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND taxname = '"+mod2+"' OR taxname2 = '"+mod2+"' OR taxname3 = '"+mod2+"' OR taxname4 = '"+mod2+"' OR taxname5 = '"+mod2+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                if (ccursor6.moveToFirst()) {
                    String hsn = ccursor6.getString(34);

                    TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                    hsn_hsn.setText(hsn);

                    if (hsn_hsn.getText().toString().equals("")) {
                    } else {
                        receiptBitmap.drawLeftText("HSN " + hsn);
                    }
                }

            }
        }

//            Cursor ccursor = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
//            if (ccursor.moveToFirst()) {
//
//                do {
//
//                    String name = ccursor.getString(10);
//                    String value = ccursor.getString(9);
//                    String pq = ccursor.getString(4);
//                    String itna = ccursor.getString(1);
//
//                    if (value.equals("0") || name.equals("NONE") || name.equals("None") || value.equals("0.0")) {
//
//                    } else {
//
////                    final TableRow row = new TableRow(CancelActivity.this);
////                    row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
////                            TableRow.LayoutParams.WRAP_CONTENT));
////                    row.setGravity(Gravity.CENTER);
//
//                        final TableRow row = new TableRow(Preparing_Orders_w.this);
//                        row.setLayoutParams(new TableLayout.LayoutParams(
//                                TableRow.LayoutParams.MATCH_PARENT,
//                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));
//
//                        TableRow.LayoutParams lp, lp1, lp2;
//
////                                    final TextView tv = new TextView(CancelActivity.this);
////                                    //tv.setLayoutParams(new TableRow.LayoutParams(40, TableRow.LayoutParams.MATCH_PARENT));
////                                    tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.7f));
////                                    tv.setTextSize(16);
////                                    tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
////                                    row.addView(tv);
//
//                        TextView tvv = new TextView(Preparing_Orders_w.this);
//                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
//                        //tv.setBackgroundResource(R.drawable.cell_shape);
//                        tvv.setGravity(Gravity.START);
//                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tvv.setText(name);
//
//                        TextView tv1 = new TextView(Preparing_Orders_w.this);
//                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
//                        tv1.setGravity(Gravity.START);
//                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        tv1.setText(value);
//                        float vbn = Float.parseFloat(value);
//                        String bvn = String.format(Locale.US,"%.2f", vbn);
//                        String value1 = tv1.getText().toString();
//
//                        TextView tv2 = new TextView(Preparing_Orders_w.this);
////                    tv2.setLayoutParams(new android.widget.TableRow.LayoutParams(145,
////                            android.widget.TableRow.LayoutParams.WRAP_CONTENT));
//                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
//                        tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
//                        tv2.append(name + " @ " + bvn + "%" + "(" + itna + ")");
//                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tv2.setPadding(0, 0, 20, 0);
//                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        //tv2.setTextColor(Color.parseColor("#000000"));
//                        row.addView(tv2);
//
//                        TextView textView1 = new TextView(Preparing_Orders_w.this);
//                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        row.addView(textView1);
//
//                        TextView tv3 = new TextView(Preparing_Orders_w.this);
////                    tv3.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.WRAP_CONTENT,
////                            android.widget.TableRow.LayoutParams.WRAP_CONTENT));
//                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
//                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
//                        float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
////                        float tota = mul;
//                        String tota1 = String.format(Locale.US,"%.2f", tota);
//                        //tv3.setPadding(0,0,10,0);
//                        tv3.setText(String.valueOf(tota));
//                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        //tv3.setTextColor(Color.parseColor("#000000"));
//                        //row.addView(tv3);
//
//
//                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        String value2 = tv3.getText().toString();
//                        row.addView(tv3);
//
//                        tableLayout1.addView(row);
//                        String mod1 = name + " @ " + bvn + "%" + "(" + itna + ")" + "---" + String.valueOf(tota1);
//                        allbuftaxestype1 = new byte[][]{
//                                left, normal, mod1.getBytes(), HT, LF
//                                //setHT34, normal,total.getBytes(),HT,modiname.getBytes(),HT, modiprice.getBytes(),HT, "125.0".getBytes(),LF
////						left, normal, setHT22, "DECAF16".getBytes(), HT, right, "30".getBytes(), LF,
////						left, normal, setHT22, "BREVE".getBytes(), HT, right, "1000".getBytes(), LF,
//                        };
//                        //byte[] buf1 = DataUtils.byteArraysToBytes(compname);
//
//                        if (statussusbs.equals("ok")) {
//                            BluetoothPrintDriver.BT_Write(left);    //
//                            BT_Write(normal);
//                            BT_Write(mod1);
//                            BluetoothPrintDriver.BT_Write(HT);    //
//                            BluetoothPrintDriver.BT_Write(LF);    //
//                        } else {
//                            if (statusnets.equals("ok")) {
//                                wifiSocket.WIFI_Write(left);    //
//                                wifiSocket.WIFI_Write(normal);
//                                wifiSocket.WIFI_Write(mod1);
//                                wifiSocket.WIFI_Write(HT);    //
//                                wifiSocket.WIFI_Write(LF);    //
//                            }
//                        }
//                    }
//
//
//                } while (ccursor.moveToNext());
//            }
//            ccursor.close();


        String phon = "0";
        Cursor caddress1 = db.rawQuery("SELECT * FROM Customerdetails WHERE billnumber = '" + billnumb + "'", null);
        if (caddress1.moveToFirst()) {
            phon = caddress1.getString(2);
        }
        caddress1.close();

        TextView tvvs = new TextView(Preparing_Orders_w.this);
        tvvs.setText(phon);

        Cursor us_name1 = db.rawQuery("Select * from Customerdetails WHERE phoneno = '" + tvvs.getText().toString() + "'", null);
        if (us_name1.moveToLast()) {
//            Toast.makeText(Preparing_Orders_w.this, "user id there", Toast.LENGTH_LONG).show();
            String na53 = us_name1.getString(53);
            String na38 = us_name1.getString(38);
            String na39 = us_name1.getString(39);
            String na40 = us_name1.getString(40);
            String na41 = us_name1.getString(41);
            String na42 = us_name1.getString(42);
            String na43 = us_name1.getString(43);
            String na44 = us_name1.getString(44);
            String na45 = us_name1.getString(45);
            String na46 = us_name1.getString(46);
            String na47 = us_name1.getString(47);
            String na48 = us_name1.getString(48);
            String na49 = us_name1.getString(49);
            String na50 = us_name1.getString(50);
            String na51 = us_name1.getString(51);
            String na52 = us_name1.getString(52);
            String na38_value = us_name1.getString(54);
            String na39_value = us_name1.getString(55);
            String na40_value = us_name1.getString(56);
            String na41_value = us_name1.getString(57);
            String na42_value = us_name1.getString(58);
            String na43_value = us_name1.getString(59);
            String na44_value = us_name1.getString(60);
            String na45_value = us_name1.getString(61);
            String na46_value = us_name1.getString(62);
            String na47_value = us_name1.getString(63);
            String na48_value = us_name1.getString(64);
            String na49_value = us_name1.getString(65);
            String na50_value = us_name1.getString(66);
            String na51_value = us_name1.getString(67);
            String na52_value = us_name1.getString(68);

            String proc = us_name1.getString(69);

            TextView hid = new TextView(Preparing_Orders_w.this);
            hid.setText(proc);

            if (hid.getText().toString().equals("off")) {
                Cursor cursorr = null;
                if (paymmethoda.equals("  Dine-in") || paymmethoda.equals("  General") || paymmethoda.equals("  Others")) {
                    cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax1 = 'dine_in'", null);//replace to cursor = dbHelper.fetchAllHotels();
                }
                if (paymmethoda.equals("  Takeaway") || paymmethoda.equals("  Main")) {
                    cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax2 = 'takeaway'", null);//replace to cursor = dbHelper.fetchAllHotels();
                }
                if (paymmethoda.equals("  Home delivery")) {
                    cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax3 = 'homedelivery'", null);//replace to cursor = dbHelper.fetchAllHotels();
                }
//            ccursor = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax'", null);//replace to ccursor = dbHelper.fetchAllHotels();
                if (cursorr.moveToFirst()) {

                    do {

                        String name = cursorr.getString(1);
                        String value = cursorr.getString(2);

                        final TableRow row = new TableRow(Preparing_Orders_w.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        row.setGravity(Gravity.CENTER);

                        TableRow.LayoutParams lp, lp1, lp2;

//                                final TextView tv = new TextView(CancelActivity.this);
//                                //tv.setLayoutParams(new TableRow.LayoutParams(40, TableRow.LayoutParams.MATCH_PARENT));
//                                tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.7f));
//                                tv.setTextSize(16);
//                                tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                                row.addView(tv);

                        TextView tvv = new TextView(Preparing_Orders_w.this);
                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tvv.setGravity(Gravity.START);
                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tvv.setText(name);

                        TextView tv1 = new TextView(Preparing_Orders_w.this);
                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        tv1.setGravity(Gravity.START);
                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv1.setText(value);
                        float vbn = Float.parseFloat(value);
                        String bvn = String.format(Locale.US,"%.2f", vbn);
                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        String value1 = tv1.getText().toString();

                        TextView tv2 = new TextView(Preparing_Orders_w.this);
                        //lp = new TableRow.LayoutParams(145, TableRow.LayoutParams.WRAP_CONTENT);
                        //tv2.setLayoutParams(lp);
                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                        tv2.append(name + " @ " + bvn + "%");
                        tv2.setPadding(0, 0, 20, 0);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(tv2);

                        TextView textView1 = new TextView(Preparing_Orders_w.this);
                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(textView1);

                        TextView tv3 = new TextView(Preparing_Orders_w.this);
//                lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
//                tv3.setLayoutParams(lp2);
                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                        //tv3.setPadding(0,0,10,0);
                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        float tota = Float.parseFloat(value1) * Float.parseFloat(String.valueOf(to)) / 100;
//                            float tota = mul;
                        String tota1 = String.format(Locale.US,"%.2f", tota);
                        tv3.setText(String.valueOf(tota));
                        //row.addView(tv3);


                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        String value2 = tv3.getText().toString();
                        row.addView(tv3);

                        tableLayout1.addView(row);

                        String mod1 = name + " @ " + bvn + "%" + "---" + String.valueOf(tota1);
                        receiptBitmap.drawLeftText(mod1);

                    } while (cursorr.moveToNext());
                }
                cursorr.close();
            } else {

                for (int i2 = 38; i2 < 53; i2++) {

//                                tv33.setText("0.0");
//                                for (int i1 = 54; i1<69; i1++) {
                    int i1 = 0;
                    if (i2 == 38) {
                        i1 = 54;
                    }
                    if (i2 == 39) {
                        i1 = 55;
                    }
                    if (i2 == 40) {
                        i1 = 56;
                    }
                    if (i2 == 41) {
                        i1 = 57;
                    }
                    if (i2 == 42) {
                        i1 = 58;
                    }
                    if (i2 == 43) {
                        i1 = 59;
                    }
                    if (i2 == 44) {
                        i1 = 60;
                    }
                    if (i2 == 45) {
                        i1 = 61;
                    }
                    if (i2 == 46) {
                        i1 = 62;
                    }
                    if (i2 == 47) {
                        i1 = 63;
                    }
                    if (i2 == 48) {
                        i1 = 64;
                    }
                    if (i2 == 49) {
                        i1 = 65;
                    }
                    if (i2 == 50) {
                        i1 = 66;
                    }
                    if (i2 == 51) {
                        i1 = 67;
                    }
                    if (i2 == 52) {
                        i1 = 68;
                    }


                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 4.5f));
//                                    TextView tv33 = new TextView(BeveragesMenuFragment.this);;
                    TableRow.LayoutParams lp, lp1, lp2;

                    TextView tv = new TextView(Preparing_Orders_w.this);
                    tv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv.setGravity(Gravity.START);
                    tv.setTextSize(15);
                    //text = cursor.getString(1);
//                String v = na;

                    tv.setText(us_name1.getString(i2));


                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv1.setGravity(Gravity.START);
                    //text = cursor.getString(1);
                    tv1.setText(us_name1.getString(i1));
                    String value1 = "0";
                    if (tv1.getText().toString().equals("")) {

                    } else {
                        value1 = tv1.getText().toString();
                    }


                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                    tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv2.append(us_name1.getString(i2) + " @ " + us_name1.getString(i1) + "%");
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setPadding(0, 0, 20, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(tv2);
//                    Toast.makeText(BeveragesMenuFragment.this, "hiii "+na38 + " @ " + us_name1.getString(i1) + "%", Toast.LENGTH_LONG).show();

                    TextView textView1 = new TextView(Preparing_Orders_w.this);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(textView1);

//                    Toast.makeText(BeveragesMenuFragment.this, " "+i1 + " @ " + value1 + "%", Toast.LENGTH_LONG).show();

                    TextView tv33 = new TextView(Preparing_Orders_w.this);
                    tv33.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                    tv33.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    float tota = Float.parseFloat(value1) * Float.parseFloat(String.valueOf(to)) / 100;
//                float mul = ((Float.parseFloat(total2)+Float.parseFloat(total_disc_print)) / 100) * Float.parseFloat(value1);
//                    float mul = Float.parseFloat(value1) * (Float.parseFloat(total)+Float.parseFloat(total_disc)) / 100;
//                        float tota = mul;
                    String tota1 = String.format(Locale.US,"%.2f", tota);
                    tv33.setText(String.valueOf(tota));
                    tv33.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    //tv3.setTextColor(Color.parseColor("#000000"));
                    //row.addView(tv3);


                    tv33.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(tv33);

                    String value2 = tv33.getText().toString();
//                    Toast.makeText(Preparing_Orders_w.this, "11 " + String.valueOf(tota1), Toast.LENGTH_LONG).show();

                    if (tv33.getText().toString().equals("0") || tv33.getText().toString().equals("0.0") || tv33.getText().toString().equals("0.00")
                            || tv33.getText().toString().equals("") || tv.getText().toString().equals("")) {

                    } else {
                        tableLayout1.addView(row);
//                        Toast.makeText(Preparing_Orders_w.this, "22 " + us_name1.getString(i2) + " @ " + us_name1.getString(i1) + "%" + "---" + String.valueOf(tota1), Toast.LENGTH_LONG).show();
                        String mod1 = us_name1.getString(i2) + " @ " + us_name1.getString(i1) + "%" + "---" + String.valueOf(tota1);
                        receiptBitmap.drawLeftText(mod1);

                    }

                }
            }

        } else {
//            Toast.makeText(Preparing_Orders_w.this, "user id not there", Toast.LENGTH_LONG).show();
            Cursor cursorr = null;
            if (paymmethoda.equals("  Dine-in") || paymmethoda.equals("  General") || paymmethoda.equals("  Others")) {
                cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax1 = 'dine_in'", null);//replace to cursor = dbHelper.fetchAllHotels();
            }
            if (paymmethoda.equals("  Takeaway") || paymmethoda.equals("  Main")) {
                cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax2 = 'takeaway'", null);//replace to cursor = dbHelper.fetchAllHotels();
            }
            if (paymmethoda.equals("  Home delivery")) {
                cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax3 = 'homedelivery'", null);//replace to cursor = dbHelper.fetchAllHotels();
            }
//            ccursor = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax'", null);//replace to ccursor = dbHelper.fetchAllHotels();
            if (cursorr.moveToFirst()) {

                do {

                    String name = cursorr.getString(1);
                    String value = cursorr.getString(2);

                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    row.setGravity(Gravity.CENTER);

                    TableRow.LayoutParams lp, lp1, lp2;

//                                final TextView tv = new TextView(CancelActivity.this);
//                                //tv.setLayoutParams(new TableRow.LayoutParams(40, TableRow.LayoutParams.MATCH_PARENT));
//                                tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.7f));
//                                tv.setTextSize(16);
//                                tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                                row.addView(tv);

                    TextView tvv = new TextView(Preparing_Orders_w.this);
                    tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    //tv.setBackgroundResource(R.drawable.cell_shape);
                    tvv.setGravity(Gravity.START);
                    tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tvv.setText(name);

                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv1.setGravity(Gravity.START);
                    tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv1.setText(value);
                    float vbn = Float.parseFloat(value);
                    String bvn = String.format(Locale.US,"%.2f", vbn);
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    String value1 = tv1.getText().toString();

                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    //lp = new TableRow.LayoutParams(145, TableRow.LayoutParams.WRAP_CONTENT);
                    //tv2.setLayoutParams(lp);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv2.append(name + " @ " + bvn + "%");
                    tv2.setPadding(0, 0, 20, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(tv2);

                    TextView textView1 = new TextView(Preparing_Orders_w.this);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(textView1);

                    TextView tv3 = new TextView(Preparing_Orders_w.this);
//                lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
//                tv3.setLayoutParams(lp2);
                    tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                    //tv3.setPadding(0,0,10,0);
                    tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    float tota = Float.parseFloat(value1) * Float.parseFloat(String.valueOf(to)) / 100;
//                        float tota = mul;
                    String tota1 = String.format(Locale.US,"%.2f", tota);
                    tv3.setText(String.valueOf(tota));
                    //row.addView(tv3);


                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    String value2 = tv3.getText().toString();
                    row.addView(tv3);

                    tableLayout1.addView(row);

                    String mod1 = name + " @ " + bvn + "%" + "---" + String.valueOf(tota1);
                    receiptBitmap.drawLeftText(mod1);

                } while (cursorr.moveToNext());
            }
            cursorr.close();
        }
        us_name1.close();

        float sum = 0;
        for (int i = 0; i < tableLayout1.getChildCount(); i++) {
            TableRow mRow = (TableRow) tableLayout1.getChildAt(i);
            TextView mTextView = (TextView) mRow.getChildAt(2);
            sum = sum
                    + Float.parseFloat(mTextView.getText().toString());
        }


        String newsum = String.format(Locale.US,"%.2f", sum);

        if (sum == 0 || sum == 0.0 || sum == 0.00) {

        } else {
            receiptBitmap.drawLeftText("Tax"+"                       "+newsum);
        }
///////////////////////////////// discount

        Cursor cursor4 = db.rawQuery("SELECT * FROM Discountdetails WHERE billno = '" + billnumb + "'", null);
        if (cursor4.moveToFirst()) {
            taxpe = cursor4.getString(5);
        } else {
            taxpe = "0";
        }
        cursor4.close();

        Cursor cursor5 = db.rawQuery("SELECT * FROM Discountdetails WHERE billno = '" + billnumb + "'", null);
        if (cursor5.moveToFirst()) {
            dsirs = cursor5.getString(7);
        } else {
            dsirs = "0";
        }
        cursor5.close();

        String alldiscinperc1 = "Discount(" + taxpe + "%)";
        receiptBitmap.drawLeftText(alldiscinperc1+"                       "+dsirs);

        float newe;
//////////////////////////////////////////////

        Cursor cursor113 = db.rawQuery("SELECT SUM(disc_indiv_total) FROM All_Sales WHERE disc_thereornot = 'yes' AND bill_no = '" + billnumb + "'", null);
        if (cursor113.moveToFirst()) {
            float level = cursor113.getFloat(0);
            total = String.valueOf(level);
            float total1 = Float.parseFloat(total);
            total_disc_print_q = String.format(Locale.US,"%.2f", total1);

            receiptBitmap.drawLeftText("Savings"+"                       "+total_disc_print_q);

            Cursor cursor = db.rawQuery("SELECT * FROM billnumber WHERE billnumber = '" + billnumb + "'", null);
            if (cursor.moveToFirst()){
                String t_total_points = cursor.getString(16);
                String v_tq = cursor.getString(17);

                TextView tv = new TextView(Preparing_Orders_w.this);
                tv.setText(t_total_points);

                if (tv.getText().toString().equals("")){

                }else {

                    receiptBitmap.drawLeftText("Loyalty(" + t_total_points + ")"+"                       "+v_tq);

                }
            }



        }
        cursor113.close();

//////////////////////////////////////Rounded off

        Cursor cursor7 = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billnumb + "'", null);
        if (cursor7.moveToFirst()) {
            subro = cursor7.getString(9);
        }
        cursor7.close();

        receiptBitmap.drawLeftText("Rounded"+"                       "+subro);

        receiptBitmap.drawLeftText(str_line);

//////////////Total main

        Cursor cursor8 = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billnumb + "'", null);
        if (cursor8.moveToFirst()) {
            alltotal1 = cursor8.getString(2);
        }
        cursor8.close();

        float all = Float.parseFloat(alltotal1);
        String newf = String.valueOf(all);

        receiptBitmap.drawLeftText("Total"+"                       "+"Rs "+newf);

        receiptBitmap.drawLeftText(str_line);

        receiptBitmap.drawLeftText("       ");

        tvkot.setText(strbillone);
        if (tvkot.getText().toString().equals("")) {

        } else {
            // Print.StartPrinting(strcompanyname ,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
            receiptBitmap.drawCenterText(strbillone);
        }

        receiptBitmap.drawLeftText("       ");
        receiptBitmap.drawLeftText("       ");

        Bitmap canvasbitmap = receiptBitmap.getReceiptBitmap();

        if(ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"getHeight: " + canvasbitmap.getHeight(),true,true);

        if(ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"getReceiptHeight: " + receiptBitmap.getReceiptHeight(),true,true);

        Bitmap croppedBmp = Bitmap.createBitmap(canvasbitmap, 0, 0, canvasbitmap.getWidth(), canvasbitmap.getHeight());

        byte[] imageCommand = mMSWisepadDeviceController.getPrintData(croppedBmp, 150);

        baos.write(imageCommand, 0, imageCommand.length);

        if(ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"end of reciept",true,true);

        return baos.toByteArray();

    }
    public void wiseposprintbillcopy1(Dialog dialog1) {


        charlength = 10;
        charlength1 = 20;
        charlength2 = 30;
        quanlentha = 5;

//        Toast.makeText(Preparing_Orders_w.this, "printbill", Toast.LENGTH_SHORT).show();

        imageViewPicture = (ImageView) dialog1.findViewById(R.id.imageViewPicturew);
        mView = dialog1.findViewById(R.id.f_vieww1);

        ImageView imageButton = (ImageView) mView.findViewById(R.id.viewImagee);
        imageViewPicture.getLayoutParams().height = 94;
        imageViewPicture.getLayoutParams().width = 384;
        imageButton.getLayoutParams().height = 94;
        imageButton.getLayoutParams().width = 384;


        String[] col = {"companylogo"};
        Cursor c = db1.query("Logo", col, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                byte[] img = c.getBlob(c.getColumnIndex("companylogo"));
                final Bitmap b1 = BitmapFactory.decodeByteArray(img, 0, img.length);

                imageButton.setImageBitmap(b1);

                mView.setDrawingCacheEnabled(true);
                mView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                mView.layout(0, 0, mView.getMeasuredWidth(), mView.getMeasuredHeight());
                mView.buildDrawingCache(true);

                Bitmap b = Bitmap.createBitmap(mView.getDrawingCache());
                //mView.setDrawingCacheEnabled(false);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                imageViewPicture.setImageBitmap(b);

                Bitmap mBitmap = ((BitmapDrawable) imageViewPicture.getDrawable())
                        .getBitmap();

                Print.StartPrintingImage(mBitmap, Align.CENTER);
            } while (c.moveToNext());
        } else {
            imageButton.setVisibility(View.GONE);
        }
        c.close();

        Print.StartPrinting("Bill Copy" , FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);

        Cursor getcom = db1.rawQuery("SELECT * FROM Companydetailss", null);
        if (getcom.moveToFirst()) {
            do {
                strcompanyname = getcom.getString(1);
                straddress1 = getcom.getString(14);
                straddress2 = getcom.getString(17);
                straddress3 = getcom.getString(18);
                strphone = getcom.getString(9);
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
            Print.StartPrinting(strcompanyname , FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        }
/////////
        tvkot.setText(straddress1);
        if (tvkot.getText().toString().equals("")) {

        } else {
            Print.StartPrinting(straddress1 ,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        }
///////////////////////////////
        tvkot.setText(straddress2);
        if (tvkot.getText().toString().equals("")) {

        } else {
            Print.StartPrinting(straddress2 ,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        }
////////////////////////////////
        tvkot.setText(straddress3);
        if (tvkot.getText().toString().equals("")) {

        } else {
            Print.StartPrinting(straddress3 ,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        }
//////////////////////////
        tvkot.setText(strphone);
        String pp = "Ph. " + strphone;
        if (tvkot.getText().toString().equals("")) {

        } else {
            Print.StartPrinting(pp ,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        }
////////////////////////////////////
        tvkot.setText(stremailid);
        if (tvkot.getText().toString().equals("")) {

        } else {
            Print.StartPrinting(stremailid ,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        }
////////////////////////////////////////
        tvkot.setText(strwebsite);
        if (tvkot.getText().toString().equals("")) {

        } else {
            Print.StartPrinting(strwebsite ,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        }
///////////////////////////////////////
        tvkot.setText(strtaxone);
        if (tvkot.getText().toString().equals("")) {

        } else {
            Print.StartPrinting(strtaxone ,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        }
        Print.StartPrinting("----------------------" ,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);

        Print.StartPrinting("Bill id."+bill_coun,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

        Cursor cursor10 = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billnumb + "'", null);
        if (cursor10.moveToFirst()) {
            billtypea = cursor10.getString(5);
            paymmethoda = cursor10.getString(6);
        }
        cursor10.close();

        TextView ttv = new TextView(Preparing_Orders_w.this);
        ttv.setText(billtypea);

        TextView ttv1 = new TextView(Preparing_Orders_w.this);
        ttv1.setText(paymmethoda);

//            if (ttv.getText().toString().equals("  Cash")) {
//                billtypeaa = "Cash";
//            } else {
//                billtypeaa = "Card";
//            }

        if (ttv.getText().toString().equals("  Cash")) {
            billtypeaa = "Cash"; //0
        }
        if (ttv.getText().toString().equals("  Card")) {
            billtypeaa = "Card"; //0
        }
        if (ttv.getText().toString().equals("  Paytm")) {
            billtypeaa = "Paytm"; //0
        }
        if (ttv.getText().toString().equals("  Mobikwik")) {
            billtypeaa = "Mobikwik"; //0
        }
        if (ttv.getText().toString().equals("  Freecharge")) {
            billtypeaa = "Freecharge"; //0
        }
        if (ttv.getText().toString().equals("  Pay Later")) {
            billtypeaa = "Pay Later"; //0
        }
        if (ttv.getText().toString().equals("  Cheque")) {
            billtypeaa = "Cheque"; //0
        }
        if (ttv.getText().toString().equals("  Sodexo")) {
            billtypeaa = "Sodexo"; //0
        }
        if (ttv.getText().toString().equals("  Zeta")) {
            billtypeaa = "Zeta"; //0
        }
        if (ttv.getText().toString().equals("  Ticket")) {
            billtypeaa = "Ticket"; //0
        }
        if (ttv.getText().toString().equals("  Upiqr")) {
            billtypeaa = "Upiqr"; //0
        }

        String bill_no = billnumb;



        //  Print.StartPrinting(strtaxone ,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        String b="Bill no." + bill_no+""+billtypeaa;
        int length1=b.length();
        int splength=32-length1;
        String input="Bill no." + bill_no;
        for(int i=0;i<splength;i++){
            input=input+" ";
        }
        input=input+billtypeaa;
        Print.StartPrinting(input ,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
        // Print.StartPrinting(rr2 ,FontLattice.SIXTEEN, false, Align.RIGHT, true);



        if (ttv1.getText().toString().equals("  Dine-in") || ttv1.getText().toString().equals("  General") || ttv1.getText().toString().equals("  Others")) {
            paymmethodaa = "Dine-in";
            //billtypee.setText("Dine-in");
        } else {
            if (ttv1.getText().toString().equals("  Takeaway") || ttv1.getText().toString().equals("  Main")) {
                paymmethodaa = "Takeaway";
                //billtypee.setText("Takeaway");
            } else {
                paymmethodaa = "Home delivery";
                //billtypee.setText("Home delivery");
            }
        }

        Cursor date = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "'", null);
        if (date.moveToFirst()) {
            datee = date.getString(25);
            timee = date.getString(12);
        }
        date.close();
        //  Print.StartPrinting(rr1 +"          "+normal1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);


        String b1=paymmethodaa+ datee;
        int length11=b1.length();
        int splength1=32-length11;
        String input1=paymmethodaa;
        for(int i=0;i<splength1;i++){
            input1=input1+" ";
        }
        input1=input1+datee;

        Print.StartPrinting(input1 , FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
        //  Print.StartPrinting(normal1 ,FontLattice.TWENTY_FOUR, mswipe_text, Align.RIGHT, true);



        Cursor cursor9 = db.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billnumb + "'", null);
        if (cursor9.moveToFirst()) {
            tableida = cursor9.getString(15);
            Cursor vbnm = db1.rawQuery("SELECT * FROM asd1 WHERE _id = '" + tableida + "'", null);
            if (vbnm.moveToFirst()) {
                assa1 = vbnm.getString(1);
                assa2 = vbnm.getString(2);
            }
            vbnm.close();
            TextView cx = new TextView(Preparing_Orders_w.this);
            cx.setText(assa1);

            if (cx.getText().toString().equals("")) {
                tableidaa = "Tab" + assa2;
            } else {
                tableidaa = "Tab" + assa1;
            }

        }
        cursor9.close();


        String b2=tableidaa+""+timee;
        int length12=b2.length();
        int splength2=32-length12;
        String input2=tableidaa;
        for(int i=0;i<splength2;i++){
            input2=input2+" ";
        }
        input2=input2+timee;

        Cursor cursor9_1 = db.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billnumb + "'", null);
        if (cursor9_1.moveToFirst()) {
            u_name = cursor9_1.getString(45);
        }
        cursor9_1.close();

        TextView tv_u_name = new TextView(Preparing_Orders_w.this);
        tv_u_name.setText(u_name);

        Print.StartPrinting(input2 ,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
        // Print.StartPrinting( time1 ,FontLattice.TWENTY_FOUR, mswipe_text, Align.RIGHT, true);
        Print.StartPrinting("Counter person: "+u_name,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
        String str_line = "-----------------------------";
        Print.StartPrinting(str_line,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);


        TextView custph = new TextView(Preparing_Orders_w.this);

        Cursor caddress = db.rawQuery("SELECT * FROM Customerdetails WHERE billnumber = '" + billnumb + "'", null);
        if (caddress.moveToFirst()) {
            String nam = caddress.getString(1);
            String addr = caddress.getString(4);
            String phon = caddress.getString(2);
            String emai = caddress.getString(3);

            if (nam.length() > 0 || addr.length() > 0 ||
                    phon.length() > 0 || emai.length() > 0) {
                Print.StartPrinting("Customer:",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
            } else {

            }

            if (nam.length() > 0) {
                Print.StartPrinting(nam,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
            } else {

            }

            if (addr.length() > 0) {
                Print.StartPrinting(addr,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
            } else {

            }

            if (phon.length() > 0) {
                String cust_ph = "Ph. " + phon;
                Print.StartPrinting(cust_ph,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
            } else {

            }

            if (emai.length() > 0) {
                Print.StartPrinting(emai,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
            } else {

            }

            Print.StartPrinting(str_line,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        }
        caddress.close();

        Print.StartPrinting("Qty"+"  "+"Item"+"        "+"Price"+"  "+"Amount",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
//           Print.StartPrinting("  "+"Item",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, false);
//           Print.StartPrinting("  "+"Price",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, false);
//           Print.StartPrinting("  "+"Amount",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
        Print.StartPrinting(str_line,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);


        Cursor ccursorr = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (ccursorr.moveToFirst()) {

            do {

                String nbg = ccursorr.getString(0);
                String name = ccursorr.getString(1);
                String value = ccursorr.getString(2);
                String pq = ccursorr.getString(5);
                String itna = ccursorr.getString(2);
                String pricee = ccursorr.getString(3);
                String tototot = ccursorr.getString(4);

                final String newid = ccursorr.getString(20);
                int padding_in_px;

                int padding_in_dp = 30;  // 34 dps
                final float scale1 = getResources().getDisplayMetrics().density;
                padding_in_px = (int) (padding_in_dp * scale1 + 0.5f);


                if (pq.equals("Item")) {
                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 4.5f));


                    final TableRow row1 = new TableRow(Preparing_Orders_w.this);
                    row1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));

                    final TableRow row2 = new TableRow(Preparing_Orders_w.this);
                    row2.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    final TableLayout tableLayout1 = new TableLayout(Preparing_Orders_w.this);

                    TableRow.LayoutParams lp, lp1, lp2;

                    TextView tv = new TextView(Preparing_Orders_w.this);
                    tv.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 0.70f));
                    tv.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv.setText(value);
                    row.addView(tv);

                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.6f));
                    tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv1.setGravity(Gravity.CENTER_VERTICAL);
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv1.setText(name);
                    String value1 = tv1.getText().toString();
                    row.addView(tv1);

                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.0f));
                    tv2.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setText(pricee);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(tv2);

                    TextView tv3 = new TextView(Preparing_Orders_w.this);
                    tv3.setLayoutParams(new android.widget.TableRow.LayoutParams(0, padding_in_px, 1.2f));
                    tv2.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv3.setText(tototot);

                    String value2 = tv3.getText().toString();

                    Cursor modcursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND parent = '" + name + "' AND parentid = '" + newid + "' ", null);

                    if (modcursor.moveToFirst()) {

                        Cursor cursor4 = db.rawQuery("SELECT SUM(total) FROM All_Sales WHERE bill_no = '" + billnumb + "'AND parent = '" + name + "' AND parentid = '" + newid + "'", null);
                        if (cursor4.moveToFirst()) {
                            float sub2a = cursor4.getFloat(0);
                            String sub2a1 = String.valueOf(sub2a);
                            ss = Float.parseFloat(sub2a1) + Float.parseFloat(tototot);
                            ss1 = String.valueOf(ss);
                        }
                        cursor4.close();

                        if (name.length() > charlength) {
                            int print1 = 0;
//                                Toast.makeText(Preparing_Orders_w.this, "1", Toast.LENGTH_SHORT).show();

                            if (value.length() > quanlentha && name.length() > charlength) {
//                                    Toast.makeText(Preparing_Orders_w.this, "2", Toast.LENGTH_SHORT).show();
                                String string1quan = value.substring(0, quanlentha);
                                String string2quan = value.substring(quanlentha);
                                String string1 = name.substring(0, charlength);
                                String string2 = name.substring(charlength);

                                Print.StartPrinting(string1quan+string1+"    "+pricee+"  "+ss1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                Print.StartPrinting(string2quan+"  "+string2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT,true);
//                                    Print.StartPrinting(price,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, false);
//                                    Print.StartPrinting(aqq2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT,false);
//                                    Print.StartPrinting(string2quan,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT,false);
//                                    Print.StartPrinting(string2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                print1 = 1;
                            }
                            if (value.length() <= quanlentha && name.length() > charlength) {
//                                    Toast.makeText(Preparing_Orders_w.this, "3", Toast.LENGTH_SHORT).show();
                                String string1 = name.substring(0, charlength);
                                String string2 = name.substring(charlength);
                                String itemName=string1;
                                if(string1.length()<11){
                                    for(int i=0;i<(11-string1.length());i++){
                                        itemName=itemName+"";
                                    }
                                }
                                if (print1 == 0) {
                                    Print.StartPrinting(value+"  "+itemName+"    "+pricee+"  "+ss1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                    Print.StartPrinting("  "+string2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
//                                    Print.StartPrinting(string1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, false);
//                                    Print.StartPrinting(price,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, false);
//                                    Print.StartPrinting(aqq2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, false);
//                                    Print.StartPrinting("     ",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, false);
//                                    Print.StartPrinting(string2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                    print1 = 1;
                                }

                            }

                            if (value.length() > quanlentha && name.length() > charlength1) {
//                                    Toast.makeText(Preparing_Orders_w.this, "4", Toast.LENGTH_SHORT).show();
                                String string1quan = value.substring(0, quanlentha);
                                String string2quan = value.substring(quanlentha);
                                String string1 = name.substring(0, charlength);
                                String string2 = name.substring(charlength, charlength1);
                                String string3 = name.substring(charlength1);

                                if (print1 == 0) {
                                    String itemName=string1;
                                    if(string1.length()<11){
                                        for(int i=0;i<(11-string1.length());i++){
                                            itemName=itemName+"";
                                        }
                                    }

                                    Print.StartPrinting(string1quan+" "+itemName+"   "+pricee+" "+ss1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                    Print.StartPrinting(string2quan+" "+string2+" "+string3,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
//                                    Print.StartPrinting(string1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT,  true);
//                                    Print.StartPrinting(price,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT,  true);
//                                    Print.StartPrinting(aqq2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT,  true);
//                                    Print.StartPrinting(string2quan,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT,  true);
//                                    Print.StartPrinting(string2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT,  true);
//                                    Print.StartPrinting(string3,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT,  true);
                                    print1 = 1;

                                }

                            }
                            if (value.length() <= quanlentha && name.length() > charlength1) {
//                                    Toast.makeText(Preparing_Orders_w.this, "5", Toast.LENGTH_SHORT).show();
                                String string1 = name.substring(0, charlength);
                                String string2 = name.substring(charlength, charlength1);
                                String string3 = name.substring(charlength1);

                                String itemName=string1;
                                if(string1.length()<11){
                                    for(int i=0;i<(11-string1.length());i++){
                                        itemName=itemName+"";
                                    }
                                }
                                if (print1 == 0) {
                                    Print.StartPrinting(value+" "+itemName+"   "+pricee+" "+ss1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                    Print.StartPrinting(" "+string2+" "+string3,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
//                                    Print.StartPrinting(string1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT,  true);
//                                    Print.StartPrinting(price,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT,  true);
//                                    Print.StartPrinting(aqq2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
//                                    Print.StartPrinting(string3,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                    print1 = 1;
                                }

                            }

                            Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                            if (ccursor.moveToFirst()) {
                                String hsn = ccursor.getString(34);

                                TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                hsn_hsn.setText(hsn);

                                if (hsn_hsn.getText().toString().equals("")) {
                                } else {
                                    Print.StartPrinting("HSN " + hsn,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
                                }
                            }

                        } else {
//                                Toast.makeText(Preparing_Orders_w.this, "6", Toast.LENGTH_SHORT).show();
                            String itemName=name;
                            if(name.length()<11) {
                                for (int i = 0; i < (11 - name.length()); i++) {
                                    itemName = itemName + "";
                                }
                            }
                            if (value.length() > quanlentha) {
//                                    Toast.makeText(Preparing_Orders_w.this, "7", Toast.LENGTH_SHORT).show();
                                String string1quan = value.substring(0, quanlentha);
                                String string2quan = value.substring(quanlentha);

                                Print.StartPrinting(string1quan+" "+itemName+"    "+pricee+"  "+ss1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                Print.StartPrinting(" "+string2quan,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                                Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                if (ccursor.moveToFirst()) {
                                    String hsn = ccursor.getString(34);

                                    TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                    hsn_hsn.setText(hsn);

                                    if (hsn_hsn.getText().toString().equals("")) {
                                    } else {
                                        Print.StartPrinting("HSN " + hsn,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
                                    }
                                }

                            } else {
//                                    Toast.makeText(Preparing_Orders_w.this, "8", Toast.LENGTH_SHORT).show();
                                Print.StartPrinting(value+" "+name+"     "+pricee+"  "+ss1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                                Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                if (ccursor.moveToFirst()) {
                                    String hsn = ccursor.getString(34);

                                    TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                    hsn_hsn.setText(hsn);

                                    if (hsn_hsn.getText().toString().equals("")) {
                                    } else {
                                        Print.StartPrinting("HSN " + hsn,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
                                    }
                                }

                            }

                        }

                        do {

                            final String modiname = modcursor.getString(1);
                            final String modiquan = modcursor.getString(2);
                            String modiprice = modcursor.getString(3);
                            String moditotal = modcursor.getString(4);
                            final String modiid = modcursor.getString(0);

                            float modprice1 = Float.parseFloat(modiprice);
                            String modpricestr = String.valueOf(modprice1);

                            if (modiname.length() > charlength) {
                                if (modiname.length() > charlength) {
                                    String string1 = modiname.substring(0, charlength);
                                    String string2 = modiname.substring(charlength);
                                    Print.StartPrinting(""+">"+string1+" "+modpricestr+"",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                    Print.StartPrinting("    "+string2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                }
                                if (modiname.length() > charlength1) {
                                    String string1 = modiname.substring(0, charlength);
                                    String string2 = modiname.substring(charlength, charlength1);
                                    String string3 = modiname.substring(charlength1);
                                    Print.StartPrinting(""+">"+string1+" "+modpricestr+"",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                    Print.StartPrinting("    "+string2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                    Print.StartPrinting("    "+string3,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                }

                                Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                if (ccursor.moveToFirst()) {
                                    String hsn = ccursor.getString(34);

                                    TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                    hsn_hsn.setText(hsn);

                                    if (hsn_hsn.getText().toString().equals("")) {
                                    } else {
                                        Print.StartPrinting("HSN " + hsn,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                    }
                                }

                            } else {

                                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);
                                Print.StartPrinting(""+">"+modiname+" "+modpricestr+""+"",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                                Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                if (ccursor.moveToFirst()) {
                                    String hsn = ccursor.getString(34);

                                    TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                    hsn_hsn.setText(hsn);

                                    if (hsn_hsn.getText().toString().equals("")) {
                                    } else {
                                        Print.StartPrinting("HSN " + hsn,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                    }
                                }

                            }

                            final TableRow tableRow11 = new TableRow(Preparing_Orders_w.this);
                            tableRow11.setLayoutParams(new TableLayout.LayoutParams(
                                    TableRow.LayoutParams.MATCH_PARENT,
                                    TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                            final TextView tvv = new TextView(Preparing_Orders_w.this);
                            // tv.setLayoutParams(new TableRow.LayoutParams(40, TableRow.LayoutParams.MATCH_PARENT));
                            tvv.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 0.70f));
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            //tv.setGravity(Gravity.CENTER);
                            tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tvv.setText("");
                            tableRow11.addView(tvv);

                            TextView tv4 = new TextView(Preparing_Orders_w.this);
                            //lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                            tv4.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.6f));
                            //tv3.setPadding(5, 0, 0, 0);
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            tv4.setText(modiname);
                            tv4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            tv4.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tv4.setGravity(Gravity.CENTER_VERTICAL);
                            //tv3.setBackgroundColor(getResources().getColor(R.color.six));
                            //tv3.setTextColor(R.color.black);
                            tableRow11.addView(tv4);

                            TextView tv5 = new TextView(Preparing_Orders_w.this);
                            //lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                            tv5.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.0f));
                            //tv3.setPadding(5, 0, 0, 0);
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            tv5.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                            tv5.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            //tv2.setPadding(0, 0, 1, 0);
                            tv5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            tv5.setText(modiprice);
                            //tv3.setBackgroundColor(getResources().getColor(R.color.six));
                            //tv3.setTextColor(R.color.black);
                            tableRow11.addView(tv5);

                            TextView tv6 = new TextView(Preparing_Orders_w.this);
                            //lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                            tv6.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                            //tv3.setPadding(5, 0, 0, 0);
                            tv6.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                            tv6.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tv6.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            tv6.setText("");
                            //tv3.setBackgroundColor(getResources().getColor(R.color.six));
                            //tv3.setTextColor(R.color.black);
                            tableRow11.addView(tv6);


                            final TextView tv7 = new TextView(Preparing_Orders_w.this);
                            //tv3.setLayoutParams(new TableRow.LayoutParams(90, TableRow.LayoutParams.MATCH_PARENT));
                            tv7.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                            tv7.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                            tv7.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            //tv3.setPadding(0,0,10,0);
                            tv7.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                            //String modtotal = String.valueOf(Integer.parseInt(modiquan) * Integer.parseInt(modiprice));

                            final String number = tv.getText().toString();
                            float newmul = Float.parseFloat(number);
                            //final float in = Float.parseFloat(cursor.getString(4));
                            String multiply = String.valueOf(newmul * Float.parseFloat(pricee));
                            //newmul = Integer.parseInt(multiply);
                            //tv3.setText(String.valueOf(Float.parseFloat(multiply)+Float.parseFloat(modtotal)));
                            //row.addView(tv3);

                            row.removeView(tv8);


                            tv8 = new TextView(Preparing_Orders_w.this);
                            tv8.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                            //tv3.setLayoutParams(new TableRow.LayoutParams(90, TableRow.LayoutParams.MATCH_PARENT));
                            //tv3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            tv8.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                            //tv3.setPadding(0, 0, 10, 0);
                            tv8.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tv8.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                            final String numberr = tv.getText().toString();
                            float newmulr = Float.parseFloat(numberr);
                            //final float in = Float.parseFloat(cursor.getString(4));
                            String multiplyr = String.valueOf(newmulr * Float.parseFloat(pricee));
                            //newmul = Integer.parseInt(multiply);
                            tv8.setText(String.valueOf(ss));
                            row.addView(tv8);


                            tableLayout1.addView(tableRow11);
                        } while (modcursor.moveToNext());

                        //Cursor modcursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND parent = '" + name + "' AND parentid = '" + newid + "' ", null);
                        Cursor disc_cursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '" + name + "' AND _id = '" + nbg + "'  ", null);
                        if (disc_cursor.moveToFirst()) {
                            do {
                                String disc_vv = disc_cursor.getString(12);
                                String disc_there = disc_cursor.getString(30);
                                float vtq = disc_cursor.getFloat(31);
                                if (disc_there.equals("no")) {

                                } else {

                                    total_disc_print_q = String.valueOf(vtq);
                                    Print.StartPrinting(""+""+""+"("+"-"+total_disc_print_q+")",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                                }
                            } while (disc_cursor.moveToNext());
                        }
                        disc_cursor.close();

                    } else {

                        if (name.length() > charlength) {
//                                Toast.makeText(Preparing_Orders_w.this, "10", Toast.LENGTH_SHORT).show();
                            int print1 = 0;
                            if (value.length() > quanlentha && name.length() > charlength) {
//                                    Toast.makeText(Preparing_Orders_w.this, "11", Toast.LENGTH_SHORT).show();
                                String string1quan = value.substring(0, quanlentha);
                                String string2quan = value.substring(quanlentha);
                                String string1 = name.substring(0, charlength);
                                String string2 = name.substring(charlength);
                                String itemName=string1;
                                if(string1.length()<11){
                                    for(int i=0;i<(11-string1.length());i++){
                                        itemName=itemName+"";
                                    }
                                }

                                //  Print.StartPrinting(string2quan+"  "+string2+""+price+"  "+pr1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                                if (print1 == 0) {
                                    Print.StartPrinting(string1quan+" "+itemName+" "+"    "+pricee+"  "+tototot,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                    Print.StartPrinting(" "+string2quan+" "+string2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                    print1 = 1;
                                }

                            }
                            if (value.length() <= quanlentha && name.length() > charlength) {
//                                    Toast.makeText(Preparing_Orders_w.this, "12", Toast.LENGTH_SHORT).show();
                                String string1 = name.substring(0, charlength);
                                String string2 = name.substring(charlength);

                                String itemName=string1;
                                if(string1.length()<11){
                                    for(int i=0;i<(11-string1.length());i++){
                                        itemName=itemName+"";
                                    }
                                }
                                if (print1 == 0) {
                                    Print.StartPrinting(value+" "+itemName+"    "+pricee+"  "+tototot,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                    Print.StartPrinting("      "+string2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
//                                    Print.StartPrinting(total,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
//                                    Print.StartPrinting(string1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
//                                    Print.StartPrinting(price,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
//                                    Print.StartPrinting(pr1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
//                                    Print.StartPrinting("      ",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
//                                    Print.StartPrinting(string2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                    print1 = 1;
                                }

                            }
////////////////////////////////////////////////
                            if (value.length() > quanlentha && name.length() > charlength1) {
//                                    Toast.makeText(Preparing_Orders_w.this, "13", Toast.LENGTH_SHORT).show();
                                String string1quan = value.substring(0, quanlentha);
                                String string2quan = value.substring(quanlentha);
                                String string1 = name.substring(0, charlength);
                                String string2 = name.substring(charlength, charlength1);
                                String string3 = name.substring(charlength1);

                                String itemName=string1;
                                if(string1.length()<11){
                                    for(int i=0;i<(11-string1.length());i++){
                                        itemName=itemName+"";
                                    }
                                }
                                if (print1 == 0) {
                                    Print.StartPrinting(string1quan+" "+itemName+"    "+pricee+"  "+tototot,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                    Print.StartPrinting("  "+string2quan+" "+string2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                    Print.StartPrinting("      "+string3,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                    print1 = 1;
                                }

                            }
                            if (value.length() <= quanlentha && name.length() > charlength1) {
//                                    Toast.makeText(Preparing_Orders_w.this, "14", Toast.LENGTH_SHORT).show();
                                String string1 = name.substring(0, charlength);
                                String string2 = name.substring(charlength, charlength1);
                                String string3 = name.substring(charlength1);

                                String itemName=string1;
                                if(string1.length()<11){
                                    for(int i=0;i<(11-string1.length());i++){
                                        itemName=itemName+"";
                                    }
                                }
                                if (print1 == 0) {
                                    Print.StartPrinting(value+" "+itemName+"    "+pricee+"  "+tototot,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                    Print.StartPrinting("  "+string2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                    Print.StartPrinting("      "+string3,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                    print1 = 1;
                                }

                            }

                            Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                            if (ccursor.moveToFirst()) {
                                String hsn = ccursor.getString(34);

                                TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                hsn_hsn.setText(hsn);

                                if (hsn_hsn.getText().toString().equals("")) {
                                } else {
                                    Print.StartPrinting("HSN " + hsn,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                }
                            }

                        } else {
                            String itemName=name;
                            if(name.length()<11){
                                for(int i=0;i<(11-name.length());i++){
                                    itemName=itemName+"";
                                }
                            }
                            if (value.length() > quanlentha) {
//                                    Toast.makeText(Preparing_Orders_w.this, "15", Toast.LENGTH_SHORT).show();
                                String string1quan = value.substring(0, quanlentha);
                                String string2quan = value.substring(quanlentha);
                                Print.StartPrinting(string1quan+" "+itemName+"    "+pricee+"  "+tototot,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                Print.StartPrinting("  "+string2quan,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                            } else {
//                                    Toast.makeText(Preparing_Orders_w.this, "16", Toast.LENGTH_SHORT).show();
                                Print.StartPrinting(value+" "+itemName+"    "+pricee+"  "+tototot,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                            }

                            Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                            if (ccursor.moveToFirst()) {
                                String hsn = ccursor.getString(34);

                                TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                hsn_hsn.setText(hsn);

                                if (hsn_hsn.getText().toString().equals("")) {
                                } else {
                                    Print.StartPrinting("HSN " + hsn,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                }
                            }


                        }

                        tv8 = new TextView(Preparing_Orders_w.this);
                        tv8.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                        //tv3.setLayoutParams(new TableRow.LayoutParams(90, TableRow.LayoutParams.MATCH_PARENT));
                        //tv3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tv8.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        //tv3.setPadding(0, 0, 10, 0);
                        tv8.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv8.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                        final String number = tv.getText().toString();
                        float newmul = Float.parseFloat(number);
                        //final float in = Float.parseFloat(cursor.getString(4));
                        String multiply = String.valueOf(newmul * Float.parseFloat(pricee));
                        //newmul = Integer.parseInt(multiply);
                        tv8.setText(String.valueOf(multiply));
                        row.addView(tv8);

                        Cursor disc_cursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '" + name + "' AND _id = '" + nbg + "'  ", null);
                        if (disc_cursor.moveToFirst()) {
                            do {
                                String disc_vv = disc_cursor.getString(12);
                                String disc_there = disc_cursor.getString(30);
                                float vtq = disc_cursor.getFloat(31);
                                if (disc_there.equals("no")) {

                                } else {

                                    total_disc_print_q = String.valueOf(vtq);
                                    Print.StartPrinting(name+""+""+""+"("+"-"+total_disc_print_q+")",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                }
                            } while (disc_cursor.moveToNext());
                        }
                        disc_cursor.close();
                    }
                    modcursor.close();
                }


            } while (ccursorr.moveToNext());
        }
        ccursorr.close();
        Print.StartPrinting(str_line,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
////////////////////////////////////sub total
        Cursor cursor3 = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billnumb + "'", null);
        if (cursor3.moveToFirst()) {
            sub = cursor3.getString(7);
        }
        cursor3.close();


//        int sub12 = sub1;
//        String total2 = String.valueOf(sub12);
        float to = Float.parseFloat(sub);
        String tot = String.valueOf(to);

        Print.StartPrinting("Sub total"+"              "+tot,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
/////////////////////////tax
        TableLayout tableLayout1 = new TableLayout(Preparing_Orders_w.this);
        tableLayout1.removeAllViews();

        Cursor ccursor = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (ccursor.moveToFirst()) {

            do {
                String name = ccursor.getString(10);
                String value = ccursor.getString(9);
                String pq = ccursor.getString(4);
                String itna = ccursor.getString(1);

                TextView v = new TextView(Preparing_Orders_w.this);
                v.setText(value);

                TextView v1 = new TextView(Preparing_Orders_w.this);
                v1.setText(name);
                if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                        || v.getText().toString().equals("")) {

                } else {
                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                    TableRow.LayoutParams lp, lp1, lp2;

                    TextView tvv = new TextView(Preparing_Orders_w.this);
                    tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    //tv.setBackgroundResource(R.drawable.cell_shape);
                    tvv.setGravity(Gravity.START);
                    tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tvv.setText(name);

                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv1.setGravity(Gravity.START);
                    tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv1.setText(value);
                    float vbn = Float.parseFloat(value);
                    String bvn = String.format(Locale.US,"%.2f", vbn);
                    String value1 = tv1.getText().toString();

                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                    tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv2.append(name + "@" + bvn + "%");
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setPadding(0, 0, 20, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    //tv2.setTextColor(Color.parseColor("#000000"));
                    row.addView(tv2);

                    TextView textView1 = new TextView(Preparing_Orders_w.this);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(textView1);

                    TextView tv3 = new TextView(Preparing_Orders_w.this);
                    tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                    tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                    String tota1 = String.format(Locale.US,"%.2f", tota);
                    tv3.setText(String.valueOf(tota));
                    tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    String value2 = tv3.getText().toString();
                    row.addView(tv3);

                    tableLayout1.addView(row);
                }
            } while (ccursor.moveToNext());
        }
        ccursor.close();

        Cursor ccursor2 = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (ccursor2.moveToFirst()) {

            do {
                String name = ccursor2.getString(35);
                String value = ccursor2.getString(36);
                String pq = ccursor2.getString(4);
                String itna = ccursor2.getString(1);

                TextView v = new TextView(Preparing_Orders_w.this);
                v.setText(value);

                TextView v1 = new TextView(Preparing_Orders_w.this);
                v1.setText(name);
                if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                        || v.getText().toString().equals("")) {

                } else {
                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                    TableRow.LayoutParams lp, lp1, lp2;

                    TextView tvv = new TextView(Preparing_Orders_w.this);
                    tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    //tv.setBackgroundResource(R.drawable.cell_shape);
                    tvv.setGravity(Gravity.START);
                    tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tvv.setText(name);

                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv1.setGravity(Gravity.START);
                    tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv1.setText(value);
                    float vbn = Float.parseFloat(value);
                    String bvn = String.format(Locale.US,"%.2f", vbn);
                    String value1 = tv1.getText().toString();

                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                    tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv2.append(name + "@" + bvn + "%");
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setPadding(0, 0, 20, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    //tv2.setTextColor(Color.parseColor("#000000"));
                    row.addView(tv2);

                    TextView textView1 = new TextView(Preparing_Orders_w.this);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(textView1);

                    TextView tv3 = new TextView(Preparing_Orders_w.this);
                    tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                    tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                    String tota1 = String.format(Locale.US,"%.2f", tota);
                    tv3.setText(String.valueOf(tota));
                    tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    String value2 = tv3.getText().toString();
                    row.addView(tv3);

                    tableLayout1.addView(row);
                }
            } while (ccursor2.moveToNext());
        }
        ccursor2.close();

        Cursor ccursor3 = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (ccursor3.moveToFirst()) {

            do {
                String name = ccursor3.getString(37);
                String value = ccursor3.getString(38);
                String pq = ccursor3.getString(4);
                String itna = ccursor3.getString(1);

                TextView v = new TextView(Preparing_Orders_w.this);
                v.setText(value);

                TextView v1 = new TextView(Preparing_Orders_w.this);
                v1.setText(name);
                if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                        || v.getText().toString().equals("")) {

                } else {
                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                    TableRow.LayoutParams lp, lp1, lp2;

                    TextView tvv = new TextView(Preparing_Orders_w.this);
                    tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    //tv.setBackgroundResource(R.drawable.cell_shape);
                    tvv.setGravity(Gravity.START);
                    tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tvv.setText(name);

                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv1.setGravity(Gravity.START);
                    tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv1.setText(value);
                    float vbn = Float.parseFloat(value);
                    String bvn = String.format(Locale.US,"%.2f", vbn);
                    String value1 = tv1.getText().toString();

                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                    tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv2.append(name + "@" + bvn + "%");
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setPadding(0, 0, 20, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    //tv2.setTextColor(Color.parseColor("#000000"));
                    row.addView(tv2);

                    TextView textView1 = new TextView(Preparing_Orders_w.this);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(textView1);

                    TextView tv3 = new TextView(Preparing_Orders_w.this);
                    tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                    tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                    String tota1 = String.format(Locale.US,"%.2f", tota);
                    tv3.setText(String.valueOf(tota));
                    tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    String value2 = tv3.getText().toString();
                    row.addView(tv3);

                    tableLayout1.addView(row);
                }
            } while (ccursor3.moveToNext());
        }
        ccursor3.close();

        Cursor ccursor4 = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (ccursor4.moveToFirst()) {

            do {
                String name = ccursor4.getString(39);
                String value = ccursor4.getString(40);
                String pq = ccursor4.getString(4);
                String itna = ccursor4.getString(1);

                TextView v = new TextView(Preparing_Orders_w.this);
                v.setText(value);

                TextView v1 = new TextView(Preparing_Orders_w.this);
                v1.setText(name);
                if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                        || v.getText().toString().equals("")) {

                } else {
                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                    TableRow.LayoutParams lp, lp1, lp2;

                    TextView tvv = new TextView(Preparing_Orders_w.this);
                    tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    //tv.setBackgroundResource(R.drawable.cell_shape);
                    tvv.setGravity(Gravity.START);
                    tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tvv.setText(name);

                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv1.setGravity(Gravity.START);
                    tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv1.setText(value);
                    float vbn = Float.parseFloat(value);
                    String bvn = String.format(Locale.US,"%.2f", vbn);
                    String value1 = tv1.getText().toString();

                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                    tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv2.append(name + "@" + bvn + "%");
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setPadding(0, 0, 20, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    //tv2.setTextColor(Color.parseColor("#000000"));
                    row.addView(tv2);

                    TextView textView1 = new TextView(Preparing_Orders_w.this);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(textView1);

                    TextView tv3 = new TextView(Preparing_Orders_w.this);
                    tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                    tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                    String tota1 = String.format(Locale.US,"%.2f", tota);
                    tv3.setText(String.valueOf(tota));
                    tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    String value2 = tv3.getText().toString();
                    row.addView(tv3);

                    tableLayout1.addView(row);
                }
            } while (ccursor4.moveToNext());
        }
        ccursor4.close();

        Cursor ccursor5 = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (ccursor5.moveToFirst()) {

            do {
                String name = ccursor5.getString(41);
                String value = ccursor5.getString(42);
                String pq = ccursor5.getString(4);
                String itna = ccursor5.getString(1);

                TextView v = new TextView(Preparing_Orders_w.this);
                v.setText(value);

                TextView v1 = new TextView(Preparing_Orders_w.this);
                v1.setText(name);
                if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                        || v.getText().toString().equals("")) {

                } else {
                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                    TableRow.LayoutParams lp, lp1, lp2;

                    TextView tvv = new TextView(Preparing_Orders_w.this);
                    tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    //tv.setBackgroundResource(R.drawable.cell_shape);
                    tvv.setGravity(Gravity.START);
                    tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tvv.setText(name);

                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv1.setGravity(Gravity.START);
                    tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv1.setText(value);
                    float vbn = Float.parseFloat(value);
                    String bvn = String.format(Locale.US,"%.2f", vbn);
                    String value1 = tv1.getText().toString();

                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                    tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv2.append(name + "@" + bvn + "%");
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setPadding(0, 0, 20, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    //tv2.setTextColor(Color.parseColor("#000000"));
                    row.addView(tv2);

                    TextView textView1 = new TextView(Preparing_Orders_w.this);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(textView1);

                    TextView tv3 = new TextView(Preparing_Orders_w.this);
                    tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                    tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                    String tota1 = String.format(Locale.US,"%.2f", tota);
                    tv3.setText(String.valueOf(tota));
                    tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    String value2 = tv3.getText().toString();
                    row.addView(tv3);

                    tableLayout1.addView(row);
                }
            } while (ccursor5.moveToNext());
        }
        ccursor5.close();


        ArrayList<String> groupList = new ArrayList<String>();

        float sum_p = 0;
        for (int i = 0; i < tableLayout1.getChildCount(); i++) {
            TableRow mRow = (TableRow) tableLayout1.getChildAt(i);
            TextView mTextView = (TextView) mRow.getChildAt(0);
//                                Toast.makeText(BeveragesMenuFragment.this, "a "+mTextView.getText().toString(), Toast.LENGTH_LONG).show();

            if (groupList.contains(mTextView.getText().toString())) {

            }else {
                sum_p = 0;
                for (int j = 0; j < tableLayout1.getChildCount(); j++) {
                    TableRow mRow1 = (TableRow) tableLayout1.getChildAt(j);
                    mTextView1 = (TextView) mRow1.getChildAt(0);
                    mTextView2 = (TextView) mRow1.getChildAt(2);
                    if (groupList.contains(mTextView.getText().toString())) {
                        if (mTextView.getText().toString().equals(mTextView1.getText().toString())) {
                            sum_p = sum_p+Float.parseFloat(mTextView2.getText().toString());
//                                                Toast.makeText(BeveragesMenuFragment.this, "b " + mTextView2.getText().toString()+" "+sum, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (mTextView.getText().toString().equals(mTextView1.getText().toString())) {
                            groupList.add(mTextView.getText().toString());
                            sum_p = sum_p+Float.parseFloat(mTextView2.getText().toString());
//                                                Toast.makeText(BeveragesMenuFragment.this, "c " + mTextView2.getText().toString()+" "+sum, Toast.LENGTH_LONG).show();
                        }
                    }
                }
//                    Toast.makeText(Preparing_Orders_w.this, "aa "+mTextView.getText().toString() +" "+sum_p, Toast.LENGTH_LONG).show();

                String mod1 = mTextView.getText().toString() + "---" + String.format("%.2f", sum_p);

                Print.StartPrinting(mod1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                String match = "@";
                int position = mTextView.getText().toString().indexOf(match);
                String mod2 = mTextView.getText().toString().substring(0, position);//keep toastmessage
//                    Toast.makeText(Preparing_Orders_w.this, " "+mod2, Toast.LENGTH_LONG).show();
                Cursor ccursor6 = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND taxname = '"+mod2+"' OR taxname2 = '"+mod2+"' OR taxname3 = '"+mod2+"' OR taxname4 = '"+mod2+"' OR taxname5 = '"+mod2+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                if (ccursor6.moveToFirst()) {
                    String hsn = ccursor6.getString(34);

                    TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                    hsn_hsn.setText(hsn);

                    if (hsn_hsn.getText().toString().equals("")) {
                    } else {
                        Print.StartPrinting("HSN " + hsn,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                    }
                }

            }
        }

//            Cursor ccursor = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
//            if (ccursor.moveToFirst()) {
//
//                do {
//
//                    String name = ccursor.getString(10);
//                    String value = ccursor.getString(9);
//                    String pq = ccursor.getString(4);
//                    String itna = ccursor.getString(1);
//
//                    if (value.equals("0") || name.equals("NONE") || name.equals("None") || value.equals("0.0")) {
//
//                    } else {
//
////                    final TableRow row = new TableRow(CancelActivity.this);
////                    row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
////                            TableRow.LayoutParams.WRAP_CONTENT));
////                    row.setGravity(Gravity.CENTER);
//
//                        final TableRow row = new TableRow(Preparing_Orders_w.this);
//                        row.setLayoutParams(new TableLayout.LayoutParams(
//                                TableRow.LayoutParams.MATCH_PARENT,
//                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));
//
//                        TableRow.LayoutParams lp, lp1, lp2;
//
////                                    final TextView tv = new TextView(CancelActivity.this);
////                                    //tv.setLayoutParams(new TableRow.LayoutParams(40, TableRow.LayoutParams.MATCH_PARENT));
////                                    tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.7f));
////                                    tv.setTextSize(16);
////                                    tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
////                                    row.addView(tv);
//
//                        TextView tvv = new TextView(Preparing_Orders_w.this);
//                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
//                        //tv.setBackgroundResource(R.drawable.cell_shape);
//                        tvv.setGravity(Gravity.START);
//                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tvv.setText(name);
//
//                        TextView tv1 = new TextView(Preparing_Orders_w.this);
//                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
//                        tv1.setGravity(Gravity.START);
//                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        tv1.setText(value);
//                        float vbn = Float.parseFloat(value);
//                        String bvn = String.format(Locale.US,"%.2f", vbn);
//                        String value1 = tv1.getText().toString();
//
//                        TextView tv2 = new TextView(Preparing_Orders_w.this);
////                    tv2.setLayoutParams(new android.widget.TableRow.LayoutParams(145,
////                            android.widget.TableRow.LayoutParams.WRAP_CONTENT));
//                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
//                        tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
//                        tv2.append(name + " @ " + bvn + "%" + "(" + itna + ")");
//                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tv2.setPadding(0, 0, 20, 0);
//                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        //tv2.setTextColor(Color.parseColor("#000000"));
//                        row.addView(tv2);
//
//                        TextView textView1 = new TextView(Preparing_Orders_w.this);
//                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        row.addView(textView1);
//
//                        TextView tv3 = new TextView(Preparing_Orders_w.this);
////                    tv3.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.WRAP_CONTENT,
////                            android.widget.TableRow.LayoutParams.WRAP_CONTENT));
//                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
//                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
//                        float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
////                        float tota = mul;
//                        String tota1 = String.format(Locale.US,"%.2f", tota);
//                        //tv3.setPadding(0,0,10,0);
//                        tv3.setText(String.valueOf(tota));
//                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        //tv3.setTextColor(Color.parseColor("#000000"));
//                        //row.addView(tv3);
//
//
//                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        String value2 = tv3.getText().toString();
//                        row.addView(tv3);
//
//                        tableLayout1.addView(row);
//                        String mod1 = name + " @ " + bvn + "%" + "(" + itna + ")" + "---" + String.valueOf(tota1);
//                        allbuftaxestype1 = new byte[][]{
//                                left, normal, mod1.getBytes(), HT, LF
//                                //setHT34, normal,total.getBytes(),HT,modiname.getBytes(),HT, modiprice.getBytes(),HT, "125.0".getBytes(),LF
////						left, normal, setHT22, "DECAF16".getBytes(), HT, right, "30".getBytes(), LF,
////						left, normal, setHT22, "BREVE".getBytes(), HT, right, "1000".getBytes(), LF,
//                        };
//                        //byte[] buf1 = DataUtils.byteArraysToBytes(compname);
//
//                        if (statussusbs.equals("ok")) {
//                            BluetoothPrintDriver.BT_Write(left);    //
//                            BT_Write(normal);
//                            BT_Write(mod1);
//                            BluetoothPrintDriver.BT_Write(HT);    //
//                            BluetoothPrintDriver.BT_Write(LF);    //
//                        } else {
//                            if (statusnets.equals("ok")) {
//                                wifiSocket.WIFI_Write(left);    //
//                                wifiSocket.WIFI_Write(normal);
//                                wifiSocket.WIFI_Write(mod1);
//                                wifiSocket.WIFI_Write(HT);    //
//                                wifiSocket.WIFI_Write(LF);    //
//                            }
//                        }
//                    }
//
//
//                } while (ccursor.moveToNext());
//            }
//            ccursor.close();


        String phon = "0";
        Cursor caddress1 = db.rawQuery("SELECT * FROM Customerdetails WHERE billnumber = '" + billnumb + "'", null);
        if (caddress1.moveToFirst()) {
            phon = caddress1.getString(2);
        }
        caddress1.close();

        TextView tvvs = new TextView(Preparing_Orders_w.this);
        tvvs.setText(phon);

        Cursor us_name1 = db.rawQuery("Select * from Customerdetails WHERE phoneno = '" + tvvs.getText().toString() + "'", null);
        if (us_name1.moveToLast()) {
//            Toast.makeText(Preparing_Orders_w.this, "user id there", Toast.LENGTH_LONG).show();
            String na53 = us_name1.getString(53);
            String na38 = us_name1.getString(38);
            String na39 = us_name1.getString(39);
            String na40 = us_name1.getString(40);
            String na41 = us_name1.getString(41);
            String na42 = us_name1.getString(42);
            String na43 = us_name1.getString(43);
            String na44 = us_name1.getString(44);
            String na45 = us_name1.getString(45);
            String na46 = us_name1.getString(46);
            String na47 = us_name1.getString(47);
            String na48 = us_name1.getString(48);
            String na49 = us_name1.getString(49);
            String na50 = us_name1.getString(50);
            String na51 = us_name1.getString(51);
            String na52 = us_name1.getString(52);
            String na38_value = us_name1.getString(54);
            String na39_value = us_name1.getString(55);
            String na40_value = us_name1.getString(56);
            String na41_value = us_name1.getString(57);
            String na42_value = us_name1.getString(58);
            String na43_value = us_name1.getString(59);
            String na44_value = us_name1.getString(60);
            String na45_value = us_name1.getString(61);
            String na46_value = us_name1.getString(62);
            String na47_value = us_name1.getString(63);
            String na48_value = us_name1.getString(64);
            String na49_value = us_name1.getString(65);
            String na50_value = us_name1.getString(66);
            String na51_value = us_name1.getString(67);
            String na52_value = us_name1.getString(68);

            String proc = us_name1.getString(69);

            TextView hid = new TextView(Preparing_Orders_w.this);
            hid.setText(proc);

            if (hid.getText().toString().equals("off")) {
                Cursor cursorr = null;
                if (paymmethoda.equals("  Dine-in") || paymmethoda.equals("  General") || paymmethoda.equals("  Others")) {
                    cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax1 = 'dine_in'", null);//replace to cursor = dbHelper.fetchAllHotels();
                }
                if (paymmethoda.equals("  Takeaway") || paymmethoda.equals("  Main")) {
                    cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax2 = 'takeaway'", null);//replace to cursor = dbHelper.fetchAllHotels();
                }
                if (paymmethoda.equals("  Home delivery")) {
                    cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax3 = 'homedelivery'", null);//replace to cursor = dbHelper.fetchAllHotels();
                }
//            ccursor = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax'", null);//replace to ccursor = dbHelper.fetchAllHotels();
                if (cursorr.moveToFirst()) {

                    do {

                        String name = cursorr.getString(1);
                        String value = cursorr.getString(2);

                        final TableRow row = new TableRow(Preparing_Orders_w.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        row.setGravity(Gravity.CENTER);

                        TableRow.LayoutParams lp, lp1, lp2;

//                                final TextView tv = new TextView(CancelActivity.this);
//                                //tv.setLayoutParams(new TableRow.LayoutParams(40, TableRow.LayoutParams.MATCH_PARENT));
//                                tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.7f));
//                                tv.setTextSize(16);
//                                tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                                row.addView(tv);

                        TextView tvv = new TextView(Preparing_Orders_w.this);
                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tvv.setGravity(Gravity.START);
                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tvv.setText(name);

                        TextView tv1 = new TextView(Preparing_Orders_w.this);
                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        tv1.setGravity(Gravity.START);
                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv1.setText(value);
                        float vbn = Float.parseFloat(value);
                        String bvn = String.format(Locale.US,"%.2f", vbn);
                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        String value1 = tv1.getText().toString();

                        TextView tv2 = new TextView(Preparing_Orders_w.this);
                        //lp = new TableRow.LayoutParams(145, TableRow.LayoutParams.WRAP_CONTENT);
                        //tv2.setLayoutParams(lp);
                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                        tv2.append(name + " @ " + bvn + "%");
                        tv2.setPadding(0, 0, 20, 0);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(tv2);

                        TextView textView1 = new TextView(Preparing_Orders_w.this);
                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(textView1);

                        TextView tv3 = new TextView(Preparing_Orders_w.this);
//                lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
//                tv3.setLayoutParams(lp2);
                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                        //tv3.setPadding(0,0,10,0);
                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        float tota = Float.parseFloat(value1) * Float.parseFloat(String.valueOf(to)) / 100;
//                            float tota = mul;
                        String tota1 = String.format(Locale.US,"%.2f", tota);
                        tv3.setText(String.valueOf(tota));
                        //row.addView(tv3);


                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        String value2 = tv3.getText().toString();
                        row.addView(tv3);

                        tableLayout1.addView(row);

                        String mod1 = name + " @ " + bvn + "%" + "---" + String.valueOf(tota1);
                        Print.StartPrinting(mod1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                    } while (cursorr.moveToNext());
                }
                cursorr.close();
            } else {

                for (int i2 = 38; i2 < 53; i2++) {

//                                tv33.setText("0.0");
//                                for (int i1 = 54; i1<69; i1++) {
                    int i1 = 0;
                    if (i2 == 38) {
                        i1 = 54;
                    }
                    if (i2 == 39) {
                        i1 = 55;
                    }
                    if (i2 == 40) {
                        i1 = 56;
                    }
                    if (i2 == 41) {
                        i1 = 57;
                    }
                    if (i2 == 42) {
                        i1 = 58;
                    }
                    if (i2 == 43) {
                        i1 = 59;
                    }
                    if (i2 == 44) {
                        i1 = 60;
                    }
                    if (i2 == 45) {
                        i1 = 61;
                    }
                    if (i2 == 46) {
                        i1 = 62;
                    }
                    if (i2 == 47) {
                        i1 = 63;
                    }
                    if (i2 == 48) {
                        i1 = 64;
                    }
                    if (i2 == 49) {
                        i1 = 65;
                    }
                    if (i2 == 50) {
                        i1 = 66;
                    }
                    if (i2 == 51) {
                        i1 = 67;
                    }
                    if (i2 == 52) {
                        i1 = 68;
                    }


                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 4.5f));
//                                    TextView tv33 = new TextView(BeveragesMenuFragment.this);;
                    TableRow.LayoutParams lp, lp1, lp2;

                    TextView tv = new TextView(Preparing_Orders_w.this);
                    tv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv.setGravity(Gravity.START);
                    tv.setTextSize(15);
                    //text = cursor.getString(1);
//                String v = na;

                    tv.setText(us_name1.getString(i2));


                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv1.setGravity(Gravity.START);
                    //text = cursor.getString(1);
                    tv1.setText(us_name1.getString(i1));
                    String value1 = "0";
                    if (tv1.getText().toString().equals("")) {

                    } else {
                        value1 = tv1.getText().toString();
                    }


                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                    tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv2.append(us_name1.getString(i2) + " @ " + us_name1.getString(i1) + "%");
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setPadding(0, 0, 20, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(tv2);
//                    Toast.makeText(BeveragesMenuFragment.this, "hiii "+na38 + " @ " + us_name1.getString(i1) + "%", Toast.LENGTH_LONG).show();

                    TextView textView1 = new TextView(Preparing_Orders_w.this);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(textView1);

//                    Toast.makeText(BeveragesMenuFragment.this, " "+i1 + " @ " + value1 + "%", Toast.LENGTH_LONG).show();

                    TextView tv33 = new TextView(Preparing_Orders_w.this);
                    tv33.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                    tv33.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    float tota = Float.parseFloat(value1) * Float.parseFloat(String.valueOf(to)) / 100;
//                float mul = ((Float.parseFloat(total2)+Float.parseFloat(total_disc_print)) / 100) * Float.parseFloat(value1);
//                    float mul = Float.parseFloat(value1) * (Float.parseFloat(total)+Float.parseFloat(total_disc)) / 100;
//                        float tota = mul;
                    String tota1 = String.format(Locale.US,"%.2f", tota);
                    tv33.setText(String.valueOf(tota));
                    tv33.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    //tv3.setTextColor(Color.parseColor("#000000"));
                    //row.addView(tv3);


                    tv33.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(tv33);

                    String value2 = tv33.getText().toString();
//                    Toast.makeText(Preparing_Orders_w.this, "11 " + String.valueOf(tota1), Toast.LENGTH_LONG).show();

                    if (tv33.getText().toString().equals("0") || tv33.getText().toString().equals("0.0") || tv33.getText().toString().equals("0.00")
                            || tv33.getText().toString().equals("") || tv.getText().toString().equals("")) {

                    } else {
                        tableLayout1.addView(row);
//                        Toast.makeText(Preparing_Orders_w.this, "22 " + us_name1.getString(i2) + " @ " + us_name1.getString(i1) + "%" + "---" + String.valueOf(tota1), Toast.LENGTH_LONG).show();
                        String mod1 = us_name1.getString(i2) + " @ " + us_name1.getString(i1) + "%" + "---" + String.valueOf(tota1);
                        Print.StartPrinting(mod1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                    }

                }
            }

        } else {
//            Toast.makeText(Preparing_Orders_w.this, "user id not there", Toast.LENGTH_LONG).show();
            Cursor cursorr = null;
            if (paymmethoda.equals("  Dine-in") || paymmethoda.equals("  General") || paymmethoda.equals("  Others")) {
                cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax1 = 'dine_in'", null);//replace to cursor = dbHelper.fetchAllHotels();
            }
            if (paymmethoda.equals("  Takeaway") || paymmethoda.equals("  Main")) {
                cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax2 = 'takeaway'", null);//replace to cursor = dbHelper.fetchAllHotels();
            }
            if (paymmethoda.equals("  Home delivery")) {
                cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax3 = 'homedelivery'", null);//replace to cursor = dbHelper.fetchAllHotels();
            }
//            ccursor = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax'", null);//replace to ccursor = dbHelper.fetchAllHotels();
            if (cursorr.moveToFirst()) {

                do {

                    String name = cursorr.getString(1);
                    String value = cursorr.getString(2);

                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    row.setGravity(Gravity.CENTER);

                    TableRow.LayoutParams lp, lp1, lp2;

//                                final TextView tv = new TextView(CancelActivity.this);
//                                //tv.setLayoutParams(new TableRow.LayoutParams(40, TableRow.LayoutParams.MATCH_PARENT));
//                                tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.7f));
//                                tv.setTextSize(16);
//                                tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                                row.addView(tv);

                    TextView tvv = new TextView(Preparing_Orders_w.this);
                    tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    //tv.setBackgroundResource(R.drawable.cell_shape);
                    tvv.setGravity(Gravity.START);
                    tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tvv.setText(name);

                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv1.setGravity(Gravity.START);
                    tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv1.setText(value);
                    float vbn = Float.parseFloat(value);
                    String bvn = String.format(Locale.US,"%.2f", vbn);
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    String value1 = tv1.getText().toString();

                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    //lp = new TableRow.LayoutParams(145, TableRow.LayoutParams.WRAP_CONTENT);
                    //tv2.setLayoutParams(lp);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv2.append(name + " @ " + bvn + "%");
                    tv2.setPadding(0, 0, 20, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(tv2);

                    TextView textView1 = new TextView(Preparing_Orders_w.this);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(textView1);

                    TextView tv3 = new TextView(Preparing_Orders_w.this);
//                lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
//                tv3.setLayoutParams(lp2);
                    tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                    //tv3.setPadding(0,0,10,0);
                    tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    float tota = Float.parseFloat(value1) * Float.parseFloat(String.valueOf(to)) / 100;
//                        float tota = mul;
                    String tota1 = String.format(Locale.US,"%.2f", tota);
                    tv3.setText(String.valueOf(tota));
                    //row.addView(tv3);


                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    String value2 = tv3.getText().toString();
                    row.addView(tv3);

                    tableLayout1.addView(row);

                    String mod1 = name + " @ " + bvn + "%" + "---" + String.valueOf(tota1);
                    Print.StartPrinting(mod1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                } while (cursorr.moveToNext());
            }
            cursorr.close();
        }
        us_name1.close();

        float sum = 0;
        for (int i = 0; i < tableLayout1.getChildCount(); i++) {
            TableRow mRow = (TableRow) tableLayout1.getChildAt(i);
            TextView mTextView = (TextView) mRow.getChildAt(2);
            sum = sum
                    + Float.parseFloat(mTextView.getText().toString());
        }


        String newsum = String.format(Locale.US,"%.2f", sum);



        if (sum == 0 || sum == 0.0 || sum == 0.00) {

        } else {
            Print.StartPrinting("Tax"+"              "+newsum,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
        }
///////////////////////////////// discount

        Cursor cursor4 = db.rawQuery("SELECT * FROM Discountdetails WHERE billno = '" + billnumb + "'", null);
        if (cursor4.moveToFirst()) {
            taxpe = cursor4.getString(5);
        } else {
            taxpe = "0";
        }
        cursor4.close();

        Cursor cursor5 = db.rawQuery("SELECT * FROM Discountdetails WHERE billno = '" + billnumb + "'", null);
        if (cursor5.moveToFirst()) {
            dsirs = cursor5.getString(7);
        } else {
            dsirs = "0";
        }
        cursor5.close();

        String alldiscinperc1 = "Discount(" + taxpe + "%)";

        //byte[] buf1 = DataUtils.byteArraysToBytes(compname);
        Print.StartPrinting(alldiscinperc1+"              "+dsirs,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

        float newe;
//////////////////////////////////////////////

        Cursor cursor113 = db.rawQuery("SELECT SUM(disc_indiv_total) FROM All_Sales WHERE disc_thereornot = 'yes' AND bill_no = '" + billnumb + "'", null);
        if (cursor113.moveToFirst()) {
            float level = cursor113.getFloat(0);
            total = String.valueOf(level);
            float total1 = Float.parseFloat(total);
            total_disc_print_q = String.format(Locale.US,"%.2f", total1);

            Print.StartPrinting("Savings"+"              "+total_disc_print_q,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);


            Cursor cursor = db.rawQuery("SELECT * FROM billnumber WHERE billnumber = '" + billnumb + "'", null);
            if (cursor.moveToFirst()){
                String t_total_points = cursor.getString(16);
                String v_tq = cursor.getString(17);

                TextView tv = new TextView(Preparing_Orders_w.this);
                tv.setText(t_total_points);

                if (tv.getText().toString().equals("")){

                }else {
                    Print.StartPrinting("Loyalty"+"              "+v_tq,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                }
            }



        }
        cursor113.close();

//////////////////////////////////////Rounded off

        Cursor cursor7 = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billnumb + "'", null);
        if (cursor7.moveToFirst()) {
            subro = cursor7.getString(9);
        }
        cursor7.close();

        Print.StartPrinting("Rounded"+"              "+subro,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

        Print.StartPrinting(str_line,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

//////////////Total main

        Cursor cursor8 = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billnumb + "'", null);
        if (cursor8.moveToFirst()) {
            alltotal1 = cursor8.getString(2);
        }
        cursor8.close();

        float all = Float.parseFloat(alltotal1);
        String newf = String.valueOf(all);

        Print.StartPrinting("Total"+"              "+insert1_rs+newf,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

        Print.StartPrinting(str_line,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

        Print.StartPrinting("",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

        tvkot.setText(strbillone);
        if (tvkot.getText().toString().equals("")) {

        } else {
            // Print.StartPrinting(strcompanyname ,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
            Print.StartPrinting(strbillone,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
        }
        Print.StartPrinting("",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
        Print.StartPrinting("",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
        //footer();
    }
    public void printByteData_wifi_counter(byte[] buf) {
        wifiSocket2.WIFI_Write(buf);
        wifiSocket2.WIFI_Write(new byte[]{10});
    }
    public void printByteData_wifi(byte[] buf) {
        wifiSocket.WIFI_Write(buf);
        wifiSocket.WIFI_Write(new byte[]{10});
    }
    public void print_printbillcopy1(Dialog dialog1) {

        fmt = new Formatter();

        Cursor connnet = db1.rawQuery("SELECT * FROM IPConn", null);
        if (connnet.moveToFirst()) {
            ipnamegets = connnet.getString(1);
            portgets = connnet.getString(2);
            statusnets = connnet.getString(3);
        }
        connnet.close();

        Cursor connnet_counter = db1.rawQuery("SELECT * FROM IPConn_Counter", null);
        if (connnet_counter.moveToFirst()) {
            ipnamegets_counter = connnet_counter.getString(1);
            portgets_counter = connnet_counter.getString(2);
            statusnets_counter = connnet_counter.getString(3);
        }
        connnet_counter.close();

        Cursor connusb = db1.rawQuery("SELECT * FROM BTConn", null);
        if (connusb.moveToFirst()) {
            addgets = connusb.getString(1);
            namegets = connusb.getString(2);
            statussusbs = connusb.getString(3);
        }
        connusb.close();

        byte[] LF1 = {0x0d,0x0a};

        allbufline = new byte[][]{
                " ".getBytes(),LF1
        };

        //Toast.makeText(Preparing_Orders_w.this, "printbillonly one ", Toast.LENGTH_SHORT).show();
        byte[] setHT34M = {0x1b,0x44,0x04,0x11,0x19,0x00};
        byte[] dotfeed = {0x1b,0x4a,0x10};
        byte[] HTRight = {0x1b,0x61, 0x02,0x09};
        byte[] HT = {0x09};
        byte[] HT1 = {0x09};
        byte[] LF = {0x0d,0x0a};

        byte[] left = {0x1b,0x61, 0x00};
        byte[] cen = {0x1b,0x61, 0x01};
        byte[] right = {0x1b,0x61, 0x02};
        byte[] bold = {0x1B,0x21,0x10};
        byte[] normal = {0x1d, 0x21, 0x00};
        byte[] horiz1 = {0x1b,0x44,0x19, 0x19, 0x00};
        byte[] horiz = {0x1b,0x44, 0x04, 0x06, 0x04, 0x04, 0x0A, 0x00, 0x09, 0x30, 0x09, 0x31, 0x09, 0x32, 0x09, 0x33, 0x09, 0x34, 0x0d,0x0a};

        byte[] un = {0x1b, 0x2d, 0x02};
        byte[] un1 = {0x1b, 0x2d, 0x00};

        String str_line = "";

        Cursor print_ty = db1.rawQuery("SELECT * FROM Printer_type", null);
        if (print_ty.moveToFirst()){
            str_print_ty = print_ty.getString(1);
        }
        print_ty.close();


        imageViewPicture = (ImageView) dialog1.findViewById(R.id.imageViewPicturew);
        mView = dialog1.findViewById(R.id.f_vieww1);

        ImageView imageButton = (ImageView) mView.findViewById(R.id.viewImagee);
        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(116, 95);
        //imageButton.setLayoutParams(layoutParams);
        //imageButton.setLayoutParams(116, 95);

        if (NAME.equals("3 inch")) {
//            Toast.makeText(Preparing_Orders_w.this, "3 inch", Toast.LENGTH_SHORT).show();
            imageViewPicture.getLayoutParams().height = 94;
            imageViewPicture.getLayoutParams().width = 576;
            imageButton.getLayoutParams().height = 94;
            imageButton.getLayoutParams().width = 576;
        } else {
//            Toast.makeText(Preparing_Orders_w.this, "2 inch", Toast.LENGTH_SHORT).show();
            imageViewPicture.getLayoutParams().height = 94;
            imageViewPicture.getLayoutParams().width = 384;
            imageButton.getLayoutParams().height = 94;
            imageButton.getLayoutParams().width = 384;
        }

        int image_there = 0;
        String[] col = {"companylogo"};
        Cursor c = db1.query("Logo", col, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                byte[] img = c.getBlob(c.getColumnIndex("companylogo"));
                final Bitmap b1 = BitmapFactory.decodeByteArray(img, 0, img.length);

                imageButton.setImageBitmap(b1);


                mView.setDrawingCacheEnabled(true);
                mView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                mView.layout(0, 0, mView.getMeasuredWidth(), mView.getMeasuredHeight());
                mView.buildDrawingCache(true);

                Bitmap b = Bitmap.createBitmap(mView.getDrawingCache());
                //mView.setDrawingCacheEnabled(false);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                imageViewPicture.setImageBitmap(b);

                Bitmap mBitmap = ((BitmapDrawable) imageViewPicture.getDrawable())
                        .getBitmap();


                //if (mBitmap != null) {
                image_there = 1;
                command = Utils.decodeBitmap(mBitmap);
                //}
            } while (c.moveToNext());
        } else {
            imageButton.setVisibility(View.GONE);
        }
        c.close();

        fmt.format("Bill copy"+"\n");

//        ImageView imageButton = (ImageView) mView.findViewById(R.id.viewImagee);
        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(116, 95);
        //imageButton.setLayoutParams(layoutParams);
        //imageButton.setLayoutParams(116, 95);

//        if (NAME.equals("3 inch")) {
////            Toast.makeText(Preparing_Orders_w.this, "3 inch", Toast.LENGTH_SHORT).show();
//            imageViewPicture.getLayoutParams().height = 94;
//            imageViewPicture.getLayoutParams().width = 576;
//            imageButton.getLayoutParams().height = 94;
//            imageButton.getLayoutParams().width = 576;
//        } else {
////            Toast.makeText(Preparing_Orders_w.this, "2 inch", Toast.LENGTH_SHORT).show();
//            imageViewPicture.getLayoutParams().height = 94;
//            imageViewPicture.getLayoutParams().width = 384;
//            imageButton.getLayoutParams().height = 94;
//            imageButton.getLayoutParams().width = 384;
//        }

//        String[] col = {"companylogo"};
//        Cursor c = db1.query("Logo", col, null, null, null, null, null);

//        if (c.moveToFirst()) {
//            do {
//                byte[] img = c.getBlob(c.getColumnIndex("companylogo"));
//                final Bitmap b1 = BitmapFactory.decodeByteArray(img, 0, img.length);
//
//                imageButton.setImageBitmap(b1);
//
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
//
//                //if (mBitmap != null) {
//                if (statussusbs.equals("ok")) {
//                    if (mBitmap != null) {
//                        byte[] command = Utils.decodeBitmap(mBitmap);
//                        printByteData(command);
//                    } else {
//                        Log.e("Print Photo error", "the file isn't exists");
//                    }
//                    Bundle data = new Bundle();
//                    //data.putParcelable(Global.OBJECT1, mBitmap);
//                    data.putParcelable(Global.PARCE1, mBitmap);
//                    data.putInt(Global.INTPARA1, nPaperWidth);
//                    data.putInt(Global.INTPARA2, 0);
////                        DrawerService.workThread.handleCmd(
////                                Global.CMD_POS_PRINTBWPICTURE, data);
//                } else {
//                    if (statusnets_counter.equals("ok")) {
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
//                    }else {
//                        if (statusnets.equals("ok")) {
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
//                //}
//            } while (c.moveToNext());
//        } else {
//            imageButton.setVisibility(View.GONE);
//        }
//        c.close();

        Cursor getcom = db1.rawQuery("SELECT * FROM Companydetailss", null);
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


        tvkot.setText(strcompanyname);
        if (tvkot.getText().toString().equals("")) {

        } else {
            fmt.format(strcompanyname+"\n");
        }

/////////
        tvkot.setText(straddress1);
        if (tvkot.getText().toString().equals("")) {

        } else {
            fmt.format(straddress1+"\n");
        }


        tvkot.setText(straddress2);
        if (tvkot.getText().toString().equals("")) {

        } else {
            fmt.format(straddress2+"\n");
        }


        tvkot.setText(strphone);
        String pp = "Ph. " + strphone;
        if (tvkot.getText().toString().equals("")) {

        } else {
            fmt.format(strphone+"\n");
        }


        tvkot.setText(stremailid);
        if (tvkot.getText().toString().equals("")) {

        } else {
            fmt.format(stremailid+"\n");
        }


        tvkot.setText(strwebsite);
        if (tvkot.getText().toString().equals("")) {

        } else {
            fmt.format(strwebsite+"\n");
        }


        tvkot.setText(strtaxone);
        if (tvkot.getText().toString().equals("")) {

        } else {
            fmt.format(strtaxone+"\n");
        }

        fmt.format("------------------------------------------------", " ");


        Cursor cursor10 = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billnumb + "'", null);
        if (cursor10.moveToFirst()) {
            billtypea = cursor10.getString(5);
            paymmethoda = cursor10.getString(6);
        }
        cursor10.close();

        TextView ttv = new TextView(Preparing_Orders_w.this);
        ttv.setText(billtypea);

        TextView ttv1 = new TextView(Preparing_Orders_w.this);
        ttv1.setText(paymmethoda);

//            if (ttv.getText().toString().equals("  Cash")) {
//                billtypeaa = "Cash";
//            } else {
//                billtypeaa = "Card";
//            }

        if (ttv.getText().toString().equals("  Cash")) {
            billtypeaa = "Cash"; //0
        }
        if (ttv.getText().toString().equals("  Card")) {
            billtypeaa = "Card"; //0
        }
        if (ttv.getText().toString().equals("  Paytm")) {
            billtypeaa = "Paytm"; //0
        }
        if (ttv.getText().toString().equals("  Mobikwik")) {
            billtypeaa = "Mobikwik"; //0
        }
        if (ttv.getText().toString().equals("  Freecharge")) {
            billtypeaa = "Freecharge"; //0
        }
        if (ttv.getText().toString().equals("  Pay Later")) {
            billtypeaa = "Pay Later"; //0
        }
        if (ttv.getText().toString().equals("  Cheque")) {
            billtypeaa = "Cheque"; //0
        }
        if (ttv.getText().toString().equals("  Sodexo")) {
            billtypeaa = "Sodexo"; //0
        }
        if (ttv.getText().toString().equals("  Zeta")) {
            billtypeaa = "Zeta"; //0
        }
        if (ttv.getText().toString().equals("  Ticket")) {
            billtypeaa = "Ticket"; //0
        }
        if (ttv.getText().toString().equals("  Upiqr")) {
            billtypeaa = "upiqr"; //0
        }
        if (ttv.getText().toString().equals("  Upiqr")) {
            billtypeaa = "Upiqr"; //0
        }
        billtypeaa = ttv.getText().toString().replace(" ", "");

        //String bill_no = billnum.getText().toString();

//        fmt.format("%-34s %13s\n", "Bill no." + billnumb, "");
        fmt.format("Bill no." + billnumb+"\n");

//        fmt.format("%-34s %13s\n", billtypeaa, "");
        fmt.format(billtypeaa+"\n");

        if (ttv1.getText().toString().equals("  Dine-in") || ttv1.getText().toString().equals("  General") || ttv1.getText().toString().equals("  Others")) {
//                paymmethodaa = "Dine-in";
            //billtypee.setText("Dine-in");
            if (account_selection.toString().equals("Dine") || account_selection.toString().equals("Qsr")) {
                paymmethodaa = "Dine-in";
            }else {
                paymmethodaa = "General";
            }
        } else {
            if (ttv1.getText().toString().equals("  Takeaway") || ttv1.getText().toString().equals("  Main")) {
                paymmethodaa = "Takeaway";
                //billtypee.setText("Takeaway");
            } else {
                paymmethodaa = "Home delivery";
                //billtypee.setText("Home delivery");
            }
        }

        Cursor date = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "'", null);
        if (date.moveToFirst()) {
            datee = date.getString(25);
            timee = date.getString(12);
        }
        date.close();

        //byte[] buf1 = DataUtils.byteArraysToBytes(compname);
        fmt.format("%-34s %13s\n", paymmethodaa, datee);

        Cursor cursor9 = db.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billnumb + "'", null);
        if (cursor9.moveToFirst()) {
            tableida = cursor9.getString(15);
            Cursor vbnm = db1.rawQuery("SELECT * FROM asd1 WHERE _id = '" + tableida + "'", null);
            if (vbnm.moveToFirst()) {
                assa1 = vbnm.getString(1);
                assa2 = vbnm.getString(2);
            }
            vbnm.close();
            TextView cx = new TextView(Preparing_Orders_w.this);
            cx.setText(assa1);

            if (cx.getText().toString().equals("")) {
                tableidaa = "Tab" + assa2;
            } else {
                tableidaa = "Tab" + assa1;
            }

        }
        cursor9.close();

        fmt.format("%-34s %13s\n", tableidaa, timee);

        Cursor cursor9_1 = db.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billnumb + "'", null);
        if (cursor9_1.moveToFirst()) {
            u_name = cursor9_1.getString(45);
        }
        cursor9_1.close();

        TextView tv_u_name = new TextView(Preparing_Orders_w.this);
        tv_u_name.setText(u_name);

        if (tv_u_name.getText().toString().equals("")){

        }else {
            fmt.format("Counter person: " + tv_u_name.getText().toString()+"\n");
        }

        fmt.format("------------------------------------------------", " ");

        Cursor caddress = db.rawQuery("SELECT * FROM Customerdetails WHERE billnumber = '" + billnumb + "'", null);
        if (caddress.moveToFirst()) {
            String nam = caddress.getString(1);
            String addr = caddress.getString(4);
            String phon = caddress.getString(2);
            String emai = caddress.getString(3);

            if (nam.length() > 0 || addr.length() > 0 ||
                    phon.length() > 0 || emai.length() > 0) {
                fmt.format("Customer:"+"\n");
            } else {

            }

            if (nam.length() > 0) {
                fmt.format(nam+"\n");
            } else {

            }

            if (addr.length() > 0) {
                fmt.format(addr+"\n");
            } else {

            }

            if (phon.length() > 0) {
                String cust_ph = "Ph. " + phon;
                fmt.format(cust_ph+"\n");
            } else {

            }

            if (emai.length() > 0) {
                fmt.format(emai+"\n");
            } else {

            }

            fmt.format("------------------------------------------------", " ");
        }
        caddress.close();

        fmt.format("%-5s %-25s %7s %7s\n", "Qty", "Item", "Price", "Amount");

        fmt.format("------------------------------------------------", " ");

        Cursor ccursorr = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (ccursorr.moveToFirst()) {

            do {

                String nbg = ccursorr.getString(0);
                String name = ccursorr.getString(1);
                String value = ccursorr.getString(2);
                String pq = ccursorr.getString(5);
                String itna = ccursorr.getString(2);
                String pricee = ccursorr.getString(3);
                String tototot = ccursorr.getString(4);
                String tototot1 = ccursorr.getString(4);

//                if (pricee.length() > 7) {
////                        price = " "+price;
//                }
//                if (pricee.length() == 6) {
//                    pricee = " "+pricee;
//                }
//                if (pricee.length() == 5) {
//                    pricee = "  "+pricee;
//                }
//                if (pricee.length() == 4) {
//                    pricee = "   "+pricee;
//                }
//                if (pricee.length() == 3) {
//                    pricee = "    "+pricee;
//                }
//
//                if (tototot.length() >= 8) {
//
//                }
//                if (tototot.length() == 7) {
//                    tototot = " "+tototot;
//                }
//                if (tototot.length() == 6) {
//                    tototot = "  "+tototot;
//                }
//                if (tototot.length() == 5) {
//                    tototot = "   "+tototot;
//                }
//                if (tototot.length() == 4) {
//                    tototot = "    "+tototot;
//                }
//                if (tototot.length() == 3) {
//                    tototot = "     "+tototot;
//                }

                final String newid = ccursorr.getString(20);
                int padding_in_px;

                int padding_in_dp = 30;  // 34 dps
                final float scale1 = getResources().getDisplayMetrics().density;
                padding_in_px = (int) (padding_in_dp * scale1 + 0.5f);


                if (pq.equals("Item")) {
                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 4.5f));


                    final TableRow row1 = new TableRow(Preparing_Orders_w.this);
                    row1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));

                    final TableRow row2 = new TableRow(Preparing_Orders_w.this);
                    row2.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    final TableLayout tableLayout1 = new TableLayout(Preparing_Orders_w.this);

                    TableRow.LayoutParams lp, lp1, lp2;

                    TextView tv = new TextView(Preparing_Orders_w.this);
                    tv.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 0.70f));
                    tv.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv.setText(value);
                    row.addView(tv);

                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.6f));
                    tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv1.setGravity(Gravity.CENTER_VERTICAL);
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv1.setText(name);
                    String value1 = tv1.getText().toString();
                    row.addView(tv1);

                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.0f));
                    tv2.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setText(pricee);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(tv2);

                    TextView tv3 = new TextView(Preparing_Orders_w.this);
                    tv3.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                    tv2.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv3.setText(tototot1);

                    String value2 = tv3.getText().toString();

                    Cursor modcursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND parent = '" + name + "' AND parentid = '" + newid + "' ", null);

                    if (modcursor.moveToFirst()) {

                        Cursor cursor4 = db.rawQuery("SELECT SUM(total) FROM All_Sales WHERE bill_no = '" + billnumb + "'AND parent = '" + name + "' AND parentid = '" + newid + "'", null);
                        if (cursor4.moveToFirst()) {
                            float sub2a = cursor4.getFloat(0);
                            String sub2a1 = String.valueOf(sub2a);
                            ss = Float.parseFloat(sub2a1) + Float.parseFloat(tototot1);
                            ss1 = String.valueOf(ss);

//                            if (ss1.length() >= 8) {
//
//                            }
//                            if (ss1.length() == 7) {
//                                ss1 = " "+ss1;
//                            }
//                            if (ss1.length() == 6) {
//                                ss1 = "  "+ss1;
//                            }
//                            if (ss1.length() == 5) {
//                                ss1 = "   "+ss1;
//                            }
//                            if (ss1.length() == 4) {
//                                ss1 = "    "+ss1;
//                            }
//                            if (ss1.length() == 3) {
//                                ss1 = "     "+ss1;
//                            }

                        }
                        cursor4.close();

                        String[][] items_array = {{value, name, pricee, ss1}};

                        for (int i = 0; i < items_array.length; i++) {
                            List<String> item_split_qty = new ArrayList<>();
                            List<String> item_split_item = new ArrayList<>();
                            List<String> item_split_price = new ArrayList<>();
                            List<String> item_split_amount = new ArrayList<>();

                            for (int j = 0; j < items_array[i][0].length(); j += 5) {
                                item_split_qty.add(items_array[i][0].substring(j, Math.min(items_array[i][0].length(), j + 5)));
                            }
                            for (int j = 0; j < items_array[i][1].length(); j += 25) {
                                item_split_item.add(items_array[i][1].substring(j, Math.min(items_array[i][1].length(), j + 25)));
                            }
                            for (int j = 0; j < items_array[i][2].length(); j += 7) {
                                item_split_price.add(items_array[i][2].substring(j, Math.min(items_array[i][2].length(), j + 7)));
                            }
                            for (int j = 0; j < items_array[i][3].length(); j += 7) {
                                item_split_amount.add(items_array[i][3].substring(j, Math.min(items_array[i][3].length(), j + 7)));
                            }

                            Log.d("DATA_QTY", String.valueOf(item_split_qty));
                            Log.d("DATA_ITEM", String.valueOf(item_split_item));
                            Log.d("DATA_PRICE", String.valueOf(item_split_price));
                            Log.d("DATA_Amount", String.valueOf(item_split_amount));

                            List<Integer> maxListValue = new ArrayList<>();
                            maxListValue.add(item_split_qty.size());
                            maxListValue.add(item_split_item.size());
                            maxListValue.add(item_split_price.size());
                            maxListValue.add(item_split_amount.size());

                            int maxValue = maxListValue.get(0);

                            for (int x = 1; x < maxListValue.size(); x++) {
                                if (maxValue < maxListValue.get(x)) {
                                    maxValue = maxListValue.get(x);
                                }
                            }

                            Log.d("DATA_MAXVALUE", String.valueOf(maxValue));

                            for (int k = 0; k < maxValue; k++) {

                                String qty,item,price_new,amount = "";

                                boolean qty_check = (k >= 0) && (k < item_split_qty.size());
                                boolean item_check = (k >= 0) && (k < item_split_item.size());
                                boolean price_check = (k >= 0) && (k < item_split_price.size());
                                boolean amount_check = (k >= 0) && (k < item_split_amount.size());

                                if(qty_check == true){
                                    qty = item_split_qty.get(k);
                                }
                                else {
                                    qty = " ";
                                }
                                if(item_check == true){
                                    item = item_split_item.get(k);
                                }
                                else {
                                    item = " ";
                                }
                                if(price_check == true){
                                    price_new = item_split_price.get(k);
                                }
                                else {
                                    price_new = " ";
                                }
                                if(amount_check == true){
                                    amount = item_split_amount.get(k);
                                }
                                else {
                                    amount = " ";
                                }

                                fmt.format("%-5s %-25s %7s %7s\n",qty,item,price_new,amount);

                            }
                            item_split_qty.removeAll(item_split_qty);
                            item_split_item.removeAll(item_split_item);
                            item_split_price.removeAll(item_split_price);
                            item_split_amount.removeAll(item_split_amount);

                        }

                        do {

                            final String modiname = modcursor.getString(1);
                            final String modiquan = modcursor.getString(2);
                            String modiprice = modcursor.getString(3);
                            String moditotal = modcursor.getString(4);
                            final String modiid = modcursor.getString(0);

                            float modprice1 = Float.parseFloat(modiprice);
                            String modpricestr = String.valueOf(modprice1);

                            String[][] items_array1 = {{"", ">", modiname, modpricestr}};

                            for (int i = 0; i < items_array1.length; i++) {
                                List<String> item_split_qty = new ArrayList<>();
                                List<String> item_split_item = new ArrayList<>();
                                List<String> item_split_price = new ArrayList<>();
                                List<String> item_split_amount = new ArrayList<>();

                                for (int j = 0; j < items_array1[i][0].length(); j += 5) {
                                    item_split_qty.add(items_array1[i][0].substring(j, Math.min(items_array1[i][0].length(), j + 5)));
                                }
                                for (int j = 0; j < items_array1[i][1].length(); j += 25) {
                                    item_split_item.add(items_array1[i][1].substring(j, Math.min(items_array1[i][1].length(), j + 25)));
                                }
                                for (int j = 0; j < items_array1[i][2].length(); j += 7) {
                                    item_split_price.add(items_array1[i][2].substring(j, Math.min(items_array1[i][2].length(), j + 7)));
                                }
                                for (int j = 0; j < items_array1[i][3].length(); j += 7) {
                                    item_split_amount.add(items_array1[i][3].substring(j, Math.min(items_array1[i][3].length(), j + 7)));
                                }

                                Log.d("DATA_QTY", String.valueOf(item_split_qty));
                                Log.d("DATA_ITEM", String.valueOf(item_split_item));
                                Log.d("DATA_PRICE", String.valueOf(item_split_price));
                                Log.d("DATA_Amount", String.valueOf(item_split_amount));

                                List<Integer> maxListValue = new ArrayList<>();
                                maxListValue.add(item_split_qty.size());
                                maxListValue.add(item_split_item.size());
                                maxListValue.add(item_split_price.size());
                                maxListValue.add(item_split_amount.size());

                                int maxValue = maxListValue.get(0);

                                for (int x = 1; x < maxListValue.size(); x++) {
                                    if (maxValue < maxListValue.get(x)) {
                                        maxValue = maxListValue.get(x);
                                    }
                                }

                                Log.d("DATA_MAXVALUE", String.valueOf(maxValue));

                                for (int k = 0; k < maxValue; k++) {

                                    String qty,item,price_new,amount = "";

                                    boolean qty_check = (k >= 0) && (k < item_split_qty.size());
                                    boolean item_check = (k >= 0) && (k < item_split_item.size());
                                    boolean price_check = (k >= 0) && (k < item_split_price.size());
                                    boolean amount_check = (k >= 0) && (k < item_split_amount.size());

                                    if(qty_check == true){
                                        qty = item_split_qty.get(k);
                                    }
                                    else {
                                        qty = " ";
                                    }
                                    if(item_check == true){
                                        item = item_split_item.get(k);
                                    }
                                    else {
                                        item = " ";
                                    }
                                    if(price_check == true){
                                        price_new = item_split_price.get(k);
                                    }
                                    else {
                                        price_new = " ";
                                    }
                                    if(amount_check == true){
                                        amount = item_split_amount.get(k);
                                    }
                                    else {
                                        amount = " ";
                                    }

                                    fmt.format("%-5s %-25s %7s %7s\n",qty,item,price_new,amount);

                                }
                                item_split_qty.removeAll(item_split_qty);
                                item_split_item.removeAll(item_split_item);
                                item_split_price.removeAll(item_split_price);
                                item_split_amount.removeAll(item_split_amount);

                            }

                            Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                            if (ccursor.moveToFirst()) {
                                String hsn = ccursor.getString(34);

                                TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                                hsn_hsn.setText(hsn);

                                if (hsn_hsn.getText().toString().equals("")) {
                                } else {
                                    fmt.format("HSN "+hsn, " ");
                                }
                            }

                            final TableRow tableRow11 = new TableRow(Preparing_Orders_w.this);
                            tableRow11.setLayoutParams(new TableLayout.LayoutParams(
                                    TableRow.LayoutParams.MATCH_PARENT,
                                    TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                            final TextView tvv = new TextView(Preparing_Orders_w.this);
                            // tv.setLayoutParams(new TableRow.LayoutParams(40, TableRow.LayoutParams.MATCH_PARENT));
                            tvv.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 0.70f));
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            //tv.setGravity(Gravity.CENTER);
                            tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tvv.setText("");
                            tableRow11.addView(tvv);

                            TextView tv4 = new TextView(Preparing_Orders_w.this);
                            //lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                            tv4.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.6f));
                            //tv3.setPadding(5, 0, 0, 0);
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            tv4.setText(modiname);
                            tv4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            tv4.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tv4.setGravity(Gravity.CENTER_VERTICAL);
                            //tv3.setBackgroundColor(getResources().getColor(R.color.six));
                            //tv3.setTextColor(R.color.black);
                            tableRow11.addView(tv4);

                            TextView tv5 = new TextView(Preparing_Orders_w.this);
                            //lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                            tv5.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.0f));
                            //tv3.setPadding(5, 0, 0, 0);
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            tv5.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                            tv5.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            //tv2.setPadding(0, 0, 1, 0);
                            tv5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            tv5.setText(modiprice);
                            //tv3.setBackgroundColor(getResources().getColor(R.color.six));
                            //tv3.setTextColor(R.color.black);
                            tableRow11.addView(tv5);

                            TextView tv6 = new TextView(Preparing_Orders_w.this);
                            //lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                            tv6.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                            //tv3.setPadding(5, 0, 0, 0);
                            tv6.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                            tv6.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tv6.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            tv6.setText("");
                            //tv3.setBackgroundColor(getResources().getColor(R.color.six));
                            //tv3.setTextColor(R.color.black);
                            tableRow11.addView(tv6);


                            final TextView tv7 = new TextView(Preparing_Orders_w.this);
                            //tv3.setLayoutParams(new TableRow.LayoutParams(90, TableRow.LayoutParams.MATCH_PARENT));
                            tv7.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                            tv7.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                            tv7.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            //tv3.setPadding(0,0,10,0);
                            tv7.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                            //String modtotal = String.valueOf(Integer.parseInt(modiquan) * Integer.parseInt(modiprice));

                            final String number = tv.getText().toString();
                            float newmul = Float.parseFloat(number);
                            //final float in = Float.parseFloat(cursor.getString(4));
                            String multiply = String.valueOf(newmul * Float.parseFloat(pricee));
                            //newmul = Integer.parseInt(multiply);
                            //tv3.setText(String.valueOf(Float.parseFloat(multiply)+Float.parseFloat(modtotal)));
                            //row.addView(tv3);

                            row.removeView(tv8);


                            tv8 = new TextView(Preparing_Orders_w.this);
                            tv8.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                            //tv3.setLayoutParams(new TableRow.LayoutParams(90, TableRow.LayoutParams.MATCH_PARENT));
                            //tv3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            tv8.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                            //tv3.setPadding(0, 0, 10, 0);
                            tv8.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tv8.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                            final String numberr = tv.getText().toString();
                            float newmulr = Float.parseFloat(numberr);
                            //final float in = Float.parseFloat(cursor.getString(4));
                            String multiplyr = String.valueOf(newmulr * Float.parseFloat(pricee));
                            //newmul = Integer.parseInt(multiply);
                            tv8.setText(String.valueOf(ss));
                            row.addView(tv8);


                            tableLayout1.addView(tableRow11);
                        } while (modcursor.moveToNext());

                        //Cursor modcursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND parent = '" + name + "' AND parentid = '" + newid + "' ", null);
                        Cursor disc_cursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '" + name + "' AND _id = '" + nbg + "'  ", null);
                        if (disc_cursor.moveToFirst()) {
                            do {
                                String disc_vv = disc_cursor.getString(12);
                                String disc_there = disc_cursor.getString(30);
                                float vtq = disc_cursor.getFloat(31);
                                if (disc_there.equals("no")) {

                                } else {

                                    total_disc_print_q = String.valueOf(vtq);

                                    String[][] items_array1 = {{"", "", "", "("+"-"+total_disc_print_q+")"}};

                                    for (int i = 0; i < items_array1.length; i++) {
                                        List<String> item_split_qty = new ArrayList<>();
                                        List<String> item_split_item = new ArrayList<>();
                                        List<String> item_split_price = new ArrayList<>();
                                        List<String> item_split_amount = new ArrayList<>();

                                        for (int j = 0; j < items_array1[i][0].length(); j += 5) {
                                            item_split_qty.add(items_array1[i][0].substring(j, Math.min(items_array1[i][0].length(), j + 5)));
                                        }
                                        for (int j = 0; j < items_array1[i][1].length(); j += 25) {
                                            item_split_item.add(items_array1[i][1].substring(j, Math.min(items_array1[i][1].length(), j + 25)));
                                        }
                                        for (int j = 0; j < items_array1[i][2].length(); j += 7) {
                                            item_split_price.add(items_array1[i][2].substring(j, Math.min(items_array1[i][2].length(), j + 7)));
                                        }
                                        for (int j = 0; j < items_array1[i][3].length(); j += 7) {
                                            item_split_amount.add(items_array1[i][3].substring(j, Math.min(items_array1[i][3].length(), j + 7)));
                                        }

                                        Log.d("DATA_QTY", String.valueOf(item_split_qty));
                                        Log.d("DATA_ITEM", String.valueOf(item_split_item));
                                        Log.d("DATA_PRICE", String.valueOf(item_split_price));
                                        Log.d("DATA_Amount", String.valueOf(item_split_amount));

                                        List<Integer> maxListValue = new ArrayList<>();
                                        maxListValue.add(item_split_qty.size());
                                        maxListValue.add(item_split_item.size());
                                        maxListValue.add(item_split_price.size());
                                        maxListValue.add(item_split_amount.size());

                                        int maxValue = maxListValue.get(0);

                                        for (int x = 1; x < maxListValue.size(); x++) {
                                            if (maxValue < maxListValue.get(x)) {
                                                maxValue = maxListValue.get(x);
                                            }
                                        }

                                        Log.d("DATA_MAXVALUE", String.valueOf(maxValue));

                                        for (int k = 0; k < maxValue; k++) {

                                            String qty,item,price_new,amount = "";

                                            boolean qty_check = (k >= 0) && (k < item_split_qty.size());
                                            boolean item_check = (k >= 0) && (k < item_split_item.size());
                                            boolean price_check = (k >= 0) && (k < item_split_price.size());
                                            boolean amount_check = (k >= 0) && (k < item_split_amount.size());

                                            if(qty_check == true){
                                                qty = item_split_qty.get(k);
                                            }
                                            else {
                                                qty = " ";
                                            }
                                            if(item_check == true){
                                                item = item_split_item.get(k);
                                            }
                                            else {
                                                item = " ";
                                            }
                                            if(price_check == true){
                                                price_new = item_split_price.get(k);
                                            }
                                            else {
                                                price_new = " ";
                                            }
                                            if(amount_check == true){
                                                amount = item_split_amount.get(k);
                                            }
                                            else {
                                                amount = " ";
                                            }

                                            fmt.format("%-5s %-25s %7s %7s\n",qty,item,price_new,amount);

                                        }
                                        item_split_qty.removeAll(item_split_qty);
                                        item_split_item.removeAll(item_split_item);
                                        item_split_price.removeAll(item_split_price);
                                        item_split_amount.removeAll(item_split_amount);

                                    }
                                }
                            } while (disc_cursor.moveToNext());
                        }
                        disc_cursor.close();

                    } else {

                        String[][] items_array = {{value, name, pricee, tototot}};

                        for (int i = 0; i < items_array.length; i++) {
                            List<String> item_split_qty = new ArrayList<>();
                            List<String> item_split_item = new ArrayList<>();
                            List<String> item_split_price = new ArrayList<>();
                            List<String> item_split_amount = new ArrayList<>();

                            for (int j = 0; j < items_array[i][0].length(); j += 5) {
                                item_split_qty.add(items_array[i][0].substring(j, Math.min(items_array[i][0].length(), j + 5)));
                            }
                            for (int j = 0; j < items_array[i][1].length(); j += 25) {
                                item_split_item.add(items_array[i][1].substring(j, Math.min(items_array[i][1].length(), j + 25)));
                            }
                            for (int j = 0; j < items_array[i][2].length(); j += 7) {
                                item_split_price.add(items_array[i][2].substring(j, Math.min(items_array[i][2].length(), j + 7)));
                            }
                            for (int j = 0; j < items_array[i][3].length(); j += 7) {
                                item_split_amount.add(items_array[i][3].substring(j, Math.min(items_array[i][3].length(), j + 7)));
                            }

                            Log.d("DATA_QTY", String.valueOf(item_split_qty));
                            Log.d("DATA_ITEM", String.valueOf(item_split_item));
                            Log.d("DATA_PRICE", String.valueOf(item_split_price));
                            Log.d("DATA_Amount", String.valueOf(item_split_amount));

                            List<Integer> maxListValue = new ArrayList<>();
                            maxListValue.add(item_split_qty.size());
                            maxListValue.add(item_split_item.size());
                            maxListValue.add(item_split_price.size());
                            maxListValue.add(item_split_amount.size());

                            int maxValue = maxListValue.get(0);

                            for (int x = 1; x < maxListValue.size(); x++) {
                                if (maxValue < maxListValue.get(x)) {
                                    maxValue = maxListValue.get(x);
                                }
                            }

                            Log.d("DATA_MAXVALUE", String.valueOf(maxValue));

                            for (int k = 0; k < maxValue; k++) {

                                String qty,item,price_new,amount = "";

                                boolean qty_check = (k >= 0) && (k < item_split_qty.size());
                                boolean item_check = (k >= 0) && (k < item_split_item.size());
                                boolean price_check = (k >= 0) && (k < item_split_price.size());
                                boolean amount_check = (k >= 0) && (k < item_split_amount.size());

                                if(qty_check == true){
                                    qty = item_split_qty.get(k);
                                }
                                else {
                                    qty = " ";
                                }
                                if(item_check == true){
                                    item = item_split_item.get(k);
                                }
                                else {
                                    item = " ";
                                }
                                if(price_check == true){
                                    price_new = item_split_price.get(k);
                                }
                                else {
                                    price_new = " ";
                                }
                                if(amount_check == true){
                                    amount = item_split_amount.get(k);
                                }
                                else {
                                    amount = " ";
                                }

                                fmt.format("%-5s %-25s %7s %7s\n",qty,item,price_new,amount);

                            }
                            item_split_qty.removeAll(item_split_qty);
                            item_split_item.removeAll(item_split_item);
                            item_split_price.removeAll(item_split_price);
                            item_split_amount.removeAll(item_split_amount);

                        }

                        Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                        if (ccursor.moveToFirst()) {
                            String hsn = ccursor.getString(34);

                            TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                            hsn_hsn.setText(hsn);

                            if (hsn_hsn.getText().toString().equals("")) {
                            } else {
                                fmt.format("%-48s\n", "HSN " + hsn);
                            }
                        }

                        tv8 = new TextView(Preparing_Orders_w.this);
                        tv8.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                        //tv3.setLayoutParams(new TableRow.LayoutParams(90, TableRow.LayoutParams.MATCH_PARENT));
                        //tv3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tv8.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        //tv3.setPadding(0, 0, 10, 0);
                        tv8.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv8.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                        final String number = tv.getText().toString();
                        float newmul = Float.parseFloat(number);
                        //final float in = Float.parseFloat(cursor.getString(4));
                        String pricee1 = ccursorr.getString(3);
                        String multiply = String.valueOf(newmul * Float.parseFloat(pricee1));
                        //newmul = Integer.parseInt(multiply);
                        tv8.setText(String.valueOf(multiply));
                        row.addView(tv8);

                        Cursor disc_cursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '" + name + "' AND _id = '" + nbg + "'  ", null);
                        if (disc_cursor.moveToFirst()) {
                            do {
                                String disc_vv = disc_cursor.getString(12);
                                String disc_there = disc_cursor.getString(30);
                                float vtq = disc_cursor.getFloat(31);
                                if (disc_there.equals("no")) {

                                } else {

                                    total_disc_print_q = String.valueOf(vtq);

                                    String[][] items_array1 = {{"", "", "", "("+"-"+total_disc_print_q+")"}};

                                    for (int i = 0; i < items_array1.length; i++) {
                                        List<String> item_split_qty = new ArrayList<>();
                                        List<String> item_split_item = new ArrayList<>();
                                        List<String> item_split_price = new ArrayList<>();
                                        List<String> item_split_amount = new ArrayList<>();

                                        for (int j = 0; j < items_array1[i][0].length(); j += 5) {
                                            item_split_qty.add(items_array1[i][0].substring(j, Math.min(items_array1[i][0].length(), j + 5)));
                                        }
                                        for (int j = 0; j < items_array1[i][1].length(); j += 25) {
                                            item_split_item.add(items_array1[i][1].substring(j, Math.min(items_array1[i][1].length(), j + 25)));
                                        }
                                        for (int j = 0; j < items_array1[i][2].length(); j += 7) {
                                            item_split_price.add(items_array1[i][2].substring(j, Math.min(items_array1[i][2].length(), j + 7)));
                                        }
                                        for (int j = 0; j < items_array1[i][3].length(); j += 7) {
                                            item_split_amount.add(items_array1[i][3].substring(j, Math.min(items_array1[i][3].length(), j + 7)));
                                        }

                                        Log.d("DATA_QTY", String.valueOf(item_split_qty));
                                        Log.d("DATA_ITEM", String.valueOf(item_split_item));
                                        Log.d("DATA_PRICE", String.valueOf(item_split_price));
                                        Log.d("DATA_Amount", String.valueOf(item_split_amount));

                                        List<Integer> maxListValue = new ArrayList<>();
                                        maxListValue.add(item_split_qty.size());
                                        maxListValue.add(item_split_item.size());
                                        maxListValue.add(item_split_price.size());
                                        maxListValue.add(item_split_amount.size());

                                        int maxValue = maxListValue.get(0);

                                        for (int x = 1; x < maxListValue.size(); x++) {
                                            if (maxValue < maxListValue.get(x)) {
                                                maxValue = maxListValue.get(x);
                                            }
                                        }

                                        Log.d("DATA_MAXVALUE", String.valueOf(maxValue));

                                        for (int k = 0; k < maxValue; k++) {

                                            String qty,item,price_new,amount = "";

                                            boolean qty_check = (k >= 0) && (k < item_split_qty.size());
                                            boolean item_check = (k >= 0) && (k < item_split_item.size());
                                            boolean price_check = (k >= 0) && (k < item_split_price.size());
                                            boolean amount_check = (k >= 0) && (k < item_split_amount.size());

                                            if(qty_check == true){
                                                qty = item_split_qty.get(k);
                                            }
                                            else {
                                                qty = " ";
                                            }
                                            if(item_check == true){
                                                item = item_split_item.get(k);
                                            }
                                            else {
                                                item = " ";
                                            }
                                            if(price_check == true){
                                                price_new = item_split_price.get(k);
                                            }
                                            else {
                                                price_new = " ";
                                            }
                                            if(amount_check == true){
                                                amount = item_split_amount.get(k);
                                            }
                                            else {
                                                amount = " ";
                                            }

                                            fmt.format("%-5s %-25s %7s %7s\n",qty,item,price_new,amount);

                                        }
                                        item_split_qty.removeAll(item_split_qty);
                                        item_split_item.removeAll(item_split_item);
                                        item_split_price.removeAll(item_split_price);
                                        item_split_amount.removeAll(item_split_amount);

                                    }
                                }
                            } while (disc_cursor.moveToNext());
                        }
                        disc_cursor.close();
                    }
                    modcursor.close();
                }


            } while (ccursorr.moveToNext());
        }
        ccursorr.close();

        fmt.format("------------------------------------------------", " ");

////////////////////////////////////sub total
        Cursor cursor3 = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billnumb + "'", null);
        if (cursor3.moveToFirst()) {
            sub = cursor3.getString(7);
        }
        cursor3.close();


//        int sub12 = sub1;
//        String total2 = String.valueOf(sub12);
        float to = Float.parseFloat(sub);
        String tot = String.format("%.2f", to);

//        if (tot.length() >= 11) {
//
//        }
//        if (tot.length() == 10) {
//            tot = " "+tot;
//        }
//        if (tot.length() == 9) {
//            tot = "  "+tot;
//        }
//        if (tot.length() == 8) {
//            tot = "   "+tot;
//        }
//        if (tot.length() == 7) {
//            tot = "    "+tot;
//        }
//        if (tot.length() == 6) {
//            tot = "     "+tot;
//        }
//        if (tot.length() == 5) {
//            tot = "      "+tot;
//        }
//        if (tot.length() == 4) {
//            tot = "       "+tot;
//        }

        fmt.format("%-38s %9s\n", "Sub total", tot);

/////////////////////////tax
        TableLayout tableLayout1 = new TableLayout(Preparing_Orders_w.this);
        tableLayout1.removeAllViews();

        Cursor ccursor = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (ccursor.moveToFirst()) {

            do {
                String name = ccursor.getString(10);
                String value = ccursor.getString(9);
                String pq = ccursor.getString(4);
                String itna = ccursor.getString(1);

                TextView v = new TextView(Preparing_Orders_w.this);
                v.setText(value);

                TextView v1 = new TextView(Preparing_Orders_w.this);
                v1.setText(name);
                if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                        || v.getText().toString().equals("") || v1.getText().toString().equals("") || v.getText().toString().equals("0.00")) {

                } else {
                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                    TableRow.LayoutParams lp, lp1, lp2;

                    TextView tvv = new TextView(Preparing_Orders_w.this);
                    tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    //tv.setBackgroundResource(R.drawable.cell_shape);
                    tvv.setGravity(Gravity.START);
                    tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tvv.setText(name);

                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv1.setGravity(Gravity.START);
                    tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv1.setText(value);
                    float vbn = Float.parseFloat(value);
                    String bvn = String.format(Locale.US,"%.2f", vbn);
                    String value1 = tv1.getText().toString();

                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                    tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv2.append(name);
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setPadding(0, 0, 20, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    //tv2.setTextColor(Color.parseColor("#000000"));
                    row.addView(tv2);

                    TextView textView1 = new TextView(Preparing_Orders_w.this);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(textView1);

                    TextView tv3 = new TextView(Preparing_Orders_w.this);
                    tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                    tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                    String tota1 = String.format(Locale.US,"%.2f", tota);
                    tv3.setText(String.valueOf(tota));
                    tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    String value2 = tv3.getText().toString();
                    row.addView(tv3);

                    tableLayout1.addView(row);
                }
            } while (ccursor.moveToNext());
        }
        ccursor.close();

        Cursor ccursor2 = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (ccursor2.moveToFirst()) {

            do {
                String name = ccursor2.getString(35);
                String value = ccursor2.getString(36);
                String pq = ccursor2.getString(4);
                String itna = ccursor2.getString(1);

                TextView v = new TextView(Preparing_Orders_w.this);
                v.setText(value);

                TextView v1 = new TextView(Preparing_Orders_w.this);
                v1.setText(name);
                if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                        || v.getText().toString().equals("") || v1.getText().toString().equals("") || v.getText().toString().equals("0.00")) {

                } else {
                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                    TableRow.LayoutParams lp, lp1, lp2;

                    TextView tvv = new TextView(Preparing_Orders_w.this);
                    tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    //tv.setBackgroundResource(R.drawable.cell_shape);
                    tvv.setGravity(Gravity.START);
                    tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tvv.setText(name);

                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv1.setGravity(Gravity.START);
                    tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv1.setText(value);
                    float vbn = Float.parseFloat(value);
                    String bvn = String.format(Locale.US,"%.2f", vbn);
                    String value1 = tv1.getText().toString();

                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                    tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv2.append(name);
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setPadding(0, 0, 20, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    //tv2.setTextColor(Color.parseColor("#000000"));
                    row.addView(tv2);

                    TextView textView1 = new TextView(Preparing_Orders_w.this);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(textView1);

                    TextView tv3 = new TextView(Preparing_Orders_w.this);
                    tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                    tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                    String tota1 = String.format(Locale.US,"%.2f", tota);
                    tv3.setText(String.valueOf(tota));
                    tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    String value2 = tv3.getText().toString();
                    row.addView(tv3);

                    tableLayout1.addView(row);
                }
            } while (ccursor2.moveToNext());
        }
        ccursor2.close();

        Cursor ccursor3 = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (ccursor3.moveToFirst()) {

            do {
                String name = ccursor3.getString(37);
                String value = ccursor3.getString(38);
                String pq = ccursor3.getString(4);
                String itna = ccursor3.getString(1);

                TextView v = new TextView(Preparing_Orders_w.this);
                v.setText(value);

                TextView v1 = new TextView(Preparing_Orders_w.this);
                v1.setText(name);
                if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                        || v.getText().toString().equals("") || v1.getText().toString().equals("") || v.getText().toString().equals("0.00")) {

                } else {
                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                    TableRow.LayoutParams lp, lp1, lp2;

                    TextView tvv = new TextView(Preparing_Orders_w.this);
                    tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    //tv.setBackgroundResource(R.drawable.cell_shape);
                    tvv.setGravity(Gravity.START);
                    tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tvv.setText(name);

                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv1.setGravity(Gravity.START);
                    tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv1.setText(value);
                    float vbn = Float.parseFloat(value);
                    String bvn = String.format(Locale.US,"%.2f", vbn);
                    String value1 = tv1.getText().toString();

                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                    tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv2.append(name);
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setPadding(0, 0, 20, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    //tv2.setTextColor(Color.parseColor("#000000"));
                    row.addView(tv2);

                    TextView textView1 = new TextView(Preparing_Orders_w.this);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(textView1);

                    TextView tv3 = new TextView(Preparing_Orders_w.this);
                    tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                    tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                    String tota1 = String.format(Locale.US,"%.2f", tota);
                    tv3.setText(String.valueOf(tota));
                    tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    String value2 = tv3.getText().toString();
                    row.addView(tv3);

                    tableLayout1.addView(row);
                }
            } while (ccursor3.moveToNext());
        }
        ccursor3.close();

        Cursor ccursor4 = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (ccursor4.moveToFirst()) {

            do {
                String name = ccursor4.getString(39);
                String value = ccursor4.getString(40);
                String pq = ccursor4.getString(4);
                String itna = ccursor4.getString(1);

                TextView v = new TextView(Preparing_Orders_w.this);
                v.setText(value);

                TextView v1 = new TextView(Preparing_Orders_w.this);
                v1.setText(name);
                if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                        || v.getText().toString().equals("") || v1.getText().toString().equals("") || v.getText().toString().equals("0.00")) {

                } else {
                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                    TableRow.LayoutParams lp, lp1, lp2;

                    TextView tvv = new TextView(Preparing_Orders_w.this);
                    tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    //tv.setBackgroundResource(R.drawable.cell_shape);
                    tvv.setGravity(Gravity.START);
                    tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tvv.setText(name);

                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv1.setGravity(Gravity.START);
                    tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv1.setText(value);
                    float vbn = Float.parseFloat(value);
                    String bvn = String.format(Locale.US,"%.2f", vbn);
                    String value1 = tv1.getText().toString();

                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                    tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv2.append(name);
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setPadding(0, 0, 20, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    //tv2.setTextColor(Color.parseColor("#000000"));
                    row.addView(tv2);

                    TextView textView1 = new TextView(Preparing_Orders_w.this);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(textView1);

                    TextView tv3 = new TextView(Preparing_Orders_w.this);
                    tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                    tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                    String tota1 = String.format(Locale.US,"%.2f", tota);
                    tv3.setText(String.valueOf(tota));
                    tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    String value2 = tv3.getText().toString();
                    row.addView(tv3);

                    tableLayout1.addView(row);
                }
            } while (ccursor4.moveToNext());
        }
        ccursor4.close();

        Cursor ccursor5 = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (ccursor5.moveToFirst()) {

            do {
                String name = ccursor5.getString(41);
                String value = ccursor5.getString(42);
                String pq = ccursor5.getString(4);
                String itna = ccursor5.getString(1);

                TextView v = new TextView(Preparing_Orders_w.this);
                v.setText(value);

                TextView v1 = new TextView(Preparing_Orders_w.this);
                v1.setText(name);
                if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                        || v.getText().toString().equals("") || v1.getText().toString().equals("") || v.getText().toString().equals("0.00")) {

                } else {
                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                    TableRow.LayoutParams lp, lp1, lp2;

                    TextView tvv = new TextView(Preparing_Orders_w.this);
                    tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    //tv.setBackgroundResource(R.drawable.cell_shape);
                    tvv.setGravity(Gravity.START);
                    tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tvv.setText(name);

                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv1.setGravity(Gravity.START);
                    tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv1.setText(value);
                    float vbn = Float.parseFloat(value);
                    String bvn = String.format(Locale.US,"%.2f", vbn);
                    String value1 = tv1.getText().toString();

                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                    tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv2.append(name);
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setPadding(0, 0, 20, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    //tv2.setTextColor(Color.parseColor("#000000"));
                    row.addView(tv2);

                    TextView textView1 = new TextView(Preparing_Orders_w.this);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(textView1);

                    TextView tv3 = new TextView(Preparing_Orders_w.this);
                    tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                    tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                    String tota1 = String.format(Locale.US,"%.2f", tota);
                    tv3.setText(String.valueOf(tota));
                    tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    String value2 = tv3.getText().toString();
                    row.addView(tv3);

                    tableLayout1.addView(row);
                }
            } while (ccursor5.moveToNext());
        }
        ccursor5.close();


        ArrayList<String> groupList = new ArrayList<String>();

        float sum_p = 0;
        for (int i = 0; i < tableLayout1.getChildCount(); i++) {
            TableRow mRow = (TableRow) tableLayout1.getChildAt(i);
            TextView mTextView = (TextView) mRow.getChildAt(0);
//                                Toast.makeText(Preparing_Orders_w.this, "a "+mTextView.getText().toString(), Toast.LENGTH_LONG).show();

            if (groupList.contains(mTextView.getText().toString())) {

            }else {
                sum_p = 0;
                for (int j = 0; j < tableLayout1.getChildCount(); j++) {
                    TableRow mRow1 = (TableRow) tableLayout1.getChildAt(j);
                    mTextView1 = (TextView) mRow1.getChildAt(0);
                    mTextView2 = (TextView) mRow1.getChildAt(2);
                    if (groupList.contains(mTextView.getText().toString())) {
                        if (mTextView.getText().toString().equals(mTextView1.getText().toString())) {
                            sum_p = sum_p+Float.parseFloat(mTextView2.getText().toString());
//                                                Toast.makeText(Preparing_Orders_w.this, "b " + mTextView2.getText().toString()+" "+sum, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (mTextView.getText().toString().equals(mTextView1.getText().toString())) {
                            groupList.add(mTextView.getText().toString());
                            sum_p = sum_p+Float.parseFloat(mTextView2.getText().toString());
//                                                Toast.makeText(Preparing_Orders_w.this, "c " + mTextView2.getText().toString()+" "+sum, Toast.LENGTH_LONG).show();
                        }
                    }
                }
//                    Toast.makeText(Preparing_Orders_w.this, "aa "+mTextView.getText().toString() +" "+sum_p, Toast.LENGTH_LONG).show();

                String mod1 = mTextView.getText().toString() + "" + String.format("%.2f", sum_p);
                String mod12 = mTextView.getText().toString();
                String sum_p_float = String.format("%.2f", sum_p);

//                if (sum_p_float.length() >= 11) {
//
//                }
//                if (sum_p_float.length() == 10) {
//                    sum_p_float = " "+sum_p_float;
//                }
//                if (sum_p_float.length() == 9) {
//                    sum_p_float = "  "+sum_p_float;
//                }
//                if (sum_p_float.length() == 8) {
//                    sum_p_float = "   "+sum_p_float;
//                }
//                if (sum_p_float.length() == 7) {
//                    sum_p_float = "    "+sum_p_float;
//                }
//                if (sum_p_float.length() == 6) {
//                    sum_p_float = "     "+sum_p_float;
//                }
//                if (sum_p_float.length() == 5) {
//                    sum_p_float = "      "+sum_p_float;
//                }
//                if (sum_p_float.length() == 4) {
//                    sum_p_float = "       "+sum_p_float;
//                }

                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                fmt.format("%-38s %9s\n", mod12, sum_p_float);

//                    String match = "@";
//                    int position = mTextView.getText().toString().indexOf(match);
                String mod2 = mTextView.getText().toString();
//                    Toast.makeText(Preparing_Orders_w.this, " "+mod2, Toast.LENGTH_LONG).show();
                Cursor ccursor6 = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND taxname = '"+mod2+"' OR taxname2 = '"+mod2+"' OR taxname3 = '"+mod2+"' OR taxname4 = '"+mod2+"' OR taxname5 = '"+mod2+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                if (ccursor6.moveToFirst()) {
                    String hsn = ccursor6.getString(34);

                    TextView hsn_hsn = new TextView(Preparing_Orders_w.this);
                    hsn_hsn.setText(hsn);

                    if (hsn_hsn.getText().toString().equals("")) {
                    } else {
                        fmt.format("%-48s\n", "HSN " + hsn);
                    }
                }

            }
        }

//            Cursor ccursor = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
//            if (ccursor.moveToFirst()) {
//
//                do {
//
//                    String name = ccursor.getString(10);
//                    String value = ccursor.getString(9);
//                    String pq = ccursor.getString(4);
//                    String itna = ccursor.getString(1);
//
//                    if (value.equals("0") || name.equals("NONE") || name.equals("None") || value.equals("0.0")) {
//
//                    } else {
//
////                    final TableRow row = new TableRow(CancelActivity.this);
////                    row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
////                            TableRow.LayoutParams.WRAP_CONTENT));
////                    row.setGravity(Gravity.CENTER);
//
//                        final TableRow row = new TableRow(Preparing_Orders_w.this);
//                        row.setLayoutParams(new TableLayout.LayoutParams(
//                                TableRow.LayoutParams.MATCH_PARENT,
//                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));
//
//                        TableRow.LayoutParams lp, lp1, lp2;
//
////                                    final TextView tv = new TextView(CancelActivity.this);
////                                    //tv.setLayoutParams(new TableRow.LayoutParams(40, TableRow.LayoutParams.MATCH_PARENT));
////                                    tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.7f));
////                                    tv.setTextSize(16);
////                                    tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
////                                    row.addView(tv);
//
//                        TextView tvv = new TextView(Preparing_Orders_w.this);
//                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
//                        //tv.setBackgroundResource(R.drawable.cell_shape);
//                        tvv.setGravity(Gravity.START);
//                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tvv.setText(name);
//
//                        TextView tv1 = new TextView(Preparing_Orders_w.this);
//                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
//                        tv1.setGravity(Gravity.START);
//                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        tv1.setText(value);
//                        float vbn = Float.parseFloat(value);
//                        String bvn = String.format(Locale.US,"%.2f", vbn);
//                        String value1 = tv1.getText().toString();
//
//                        TextView tv2 = new TextView(Preparing_Orders_w.this);
////                    tv2.setLayoutParams(new android.widget.TableRow.LayoutParams(145,
////                            android.widget.TableRow.LayoutParams.WRAP_CONTENT));
//                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
//                        tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
//                        tv2.append(name + " @ " + bvn + "%" + "(" + itna + ")");
//                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tv2.setPadding(0, 0, 20, 0);
//                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        //tv2.setTextColor(Color.parseColor("#000000"));
//                        row.addView(tv2);
//
//                        TextView textView1 = new TextView(Preparing_Orders_w.this);
//                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        row.addView(textView1);
//
//                        TextView tv3 = new TextView(Preparing_Orders_w.this);
////                    tv3.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.WRAP_CONTENT,
////                            android.widget.TableRow.LayoutParams.WRAP_CONTENT));
//                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
//                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
//                        float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
////                        float tota = mul;
//                        String tota1 = String.format(Locale.US,"%.2f", tota);
//                        //tv3.setPadding(0,0,10,0);
//                        tv3.setText(String.valueOf(tota));
//                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        //tv3.setTextColor(Color.parseColor("#000000"));
//                        //row.addView(tv3);
//
//
//                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        String value2 = tv3.getText().toString();
//                        row.addView(tv3);
//
//                        tableLayout1.addView(row);
//                        String mod1 = name + " @ " + bvn + "%" + "(" + itna + ")" + "---" + String.valueOf(tota1);
//                        allbuftaxestype1 = new byte[][]{
//                                left, normal, mod1.getBytes(), HT, LF
//                                //setHT34, normal,total.getBytes(),HT,modiname.getBytes(),HT, modiprice.getBytes(),HT, "125.0".getBytes(),LF
////						left, normal, setHT22, "DECAF16".getBytes(), HT, right, "30".getBytes(), LF,
////						left, normal, setHT22, "BREVE".getBytes(), HT, right, "1000".getBytes(), LF,
//                        };
//                        //byte[] buf1 = DataUtils.byteArraysToBytes(compname);
//
//                        if (statussusbs.equals("ok")) {
//                            BluetoothPrintDriver.BT_Write(left);    //
//                            BT_Write(normal);
//                            BT_Write(mod1);
//                            BluetoothPrintDriver.BT_Write(HT);    //
//                            BluetoothPrintDriver.BT_Write(LF);    //
//                        } else {
//                            if (statusnets.equals("ok")) {
//                                wifiSocket.WIFI_Write(left);    //
//                                wifiSocket.WIFI_Write(normal);
//                                wifiSocket.WIFI_Write(mod1);
//                                wifiSocket.WIFI_Write(HT);    //
//                                wifiSocket.WIFI_Write(LF);    //
//                            }
//                        }
//                    }
//
//
//                } while (ccursor.moveToNext());
//            }
//            ccursor.close();


        String phon = "0";
        Cursor caddress1 = db.rawQuery("SELECT * FROM Customerdetails WHERE billnumber = '" + billnumb + "'", null);
        if (caddress1.moveToFirst()) {
            phon = caddress1.getString(2);
        }
        caddress1.close();

        TextView tvvs = new TextView(Preparing_Orders_w.this);
        tvvs.setText(phon);

        Cursor us_name1 = db.rawQuery("Select * from Customerdetails WHERE phoneno = '" + tvvs.getText().toString() + "'", null);
        if (us_name1.moveToLast()) {
//            Toast.makeText(Preparing_Orders_w.this, "user id there", Toast.LENGTH_LONG).show();
            String na53 = us_name1.getString(53);
            String na38 = us_name1.getString(38);
            String na39 = us_name1.getString(39);
            String na40 = us_name1.getString(40);
            String na41 = us_name1.getString(41);
            String na42 = us_name1.getString(42);
            String na43 = us_name1.getString(43);
            String na44 = us_name1.getString(44);
            String na45 = us_name1.getString(45);
            String na46 = us_name1.getString(46);
            String na47 = us_name1.getString(47);
            String na48 = us_name1.getString(48);
            String na49 = us_name1.getString(49);
            String na50 = us_name1.getString(50);
            String na51 = us_name1.getString(51);
            String na52 = us_name1.getString(52);
            String na38_value = us_name1.getString(54);
            String na39_value = us_name1.getString(55);
            String na40_value = us_name1.getString(56);
            String na41_value = us_name1.getString(57);
            String na42_value = us_name1.getString(58);
            String na43_value = us_name1.getString(59);
            String na44_value = us_name1.getString(60);
            String na45_value = us_name1.getString(61);
            String na46_value = us_name1.getString(62);
            String na47_value = us_name1.getString(63);
            String na48_value = us_name1.getString(64);
            String na49_value = us_name1.getString(65);
            String na50_value = us_name1.getString(66);
            String na51_value = us_name1.getString(67);
            String na52_value = us_name1.getString(68);

            String proc = us_name1.getString(69);

            TextView hid = new TextView(Preparing_Orders_w.this);
            hid.setText(proc);

            if (hid.getText().toString().equals("off")) {
                Cursor cursorr = null;
                if (paymmethoda.equals("  Dine-in") || paymmethoda.equals("  General") || paymmethoda.equals("  Others")) {
                    cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax1 = 'dine_in'", null);//replace to cursor = dbHelper.fetchAllHotels();
                }
                if (paymmethoda.equals("  Takeaway") || paymmethoda.equals("  Main")) {
                    cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax2 = 'takeaway'", null);//replace to cursor = dbHelper.fetchAllHotels();
                }
                if (paymmethoda.equals("  Home delivery")) {
                    cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax3 = 'homedelivery'", null);//replace to cursor = dbHelper.fetchAllHotels();
                }
//            ccursor = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax'", null);//replace to ccursor = dbHelper.fetchAllHotels();
                if (cursorr.moveToFirst()) {

                    do {

                        String name = cursorr.getString(1);
                        String value = cursorr.getString(2);

                        final TableRow row = new TableRow(Preparing_Orders_w.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        row.setGravity(Gravity.CENTER);

                        TableRow.LayoutParams lp, lp1, lp2;

//                                final TextView tv = new TextView(CancelActivity.this);
//                                //tv.setLayoutParams(new TableRow.LayoutParams(40, TableRow.LayoutParams.MATCH_PARENT));
//                                tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.7f));
//                                tv.setTextSize(16);
//                                tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                                row.addView(tv);

                        TextView tvv = new TextView(Preparing_Orders_w.this);
                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tvv.setGravity(Gravity.START);
                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tvv.setText(name);

                        TextView tv1 = new TextView(Preparing_Orders_w.this);
                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        tv1.setGravity(Gravity.START);
                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv1.setText(value);
                        float vbn = Float.parseFloat(value);
                        String bvn = String.format(Locale.US,"%.2f", vbn);
                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        String value1 = tv1.getText().toString();

                        TextView tv2 = new TextView(Preparing_Orders_w.this);
                        //lp = new TableRow.LayoutParams(145, TableRow.LayoutParams.WRAP_CONTENT);
                        //tv2.setLayoutParams(lp);
                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                        tv2.append(name + " @ " + bvn + "%");
                        tv2.setPadding(0, 0, 20, 0);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(tv2);

                        TextView textView1 = new TextView(Preparing_Orders_w.this);
                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(textView1);

                        TextView tv3 = new TextView(Preparing_Orders_w.this);
//                lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
//                tv3.setLayoutParams(lp2);
                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                        //tv3.setPadding(0,0,10,0);
                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        float tota = Float.parseFloat(value1) * Float.parseFloat(String.valueOf(to)) / 100;
//                            float tota = mul;
                        String tota1 = String.format(Locale.US,"%.2f", tota);
                        tv3.setText(String.valueOf(tota));
                        //row.addView(tv3);


                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        String value2 = tv3.getText().toString();
                        row.addView(tv3);

                        tableLayout1.addView(row);

                        String mod1 = name + " @ " + bvn + "%" + "---" + String.valueOf(tota1);
                        String mod1_1 = name + "" + String.valueOf(tota1);
                        String mod12 = name;
                        String sum_p_float = String.valueOf(tota1);

//                        if (sum_p_float.length() >= 11) {
//
//                        }
//                        if (sum_p_float.length() == 10) {
//                            sum_p_float = " "+sum_p_float;
//                        }
//                        if (sum_p_float.length() == 9) {
//                            sum_p_float = "  "+sum_p_float;
//                        }
//                        if (sum_p_float.length() == 8) {
//                            sum_p_float = "   "+sum_p_float;
//                        }
//                        if (sum_p_float.length() == 7) {
//                            sum_p_float = "    "+sum_p_float;
//                        }
//                        if (sum_p_float.length() == 6) {
//                            sum_p_float = "     "+sum_p_float;
//                        }
//                        if (sum_p_float.length() == 5) {
//                            sum_p_float = "      "+sum_p_float;
//                        }
//                        if (sum_p_float.length() == 4) {
//                            sum_p_float = "       "+sum_p_float;
//                        }

                        //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                        fmt.format("%-38s %9s\n", mod12, sum_p_float);

                    } while (cursorr.moveToNext());
                }
                cursorr.close();
            } else {

                for (int i2 = 38; i2 < 53; i2++) {

//                                tv33.setText("0.0");
//                                for (int i1 = 54; i1<69; i1++) {
                    int i1 = 0;
                    if (i2 == 38) {
                        i1 = 54;
                    }
                    if (i2 == 39) {
                        i1 = 55;
                    }
                    if (i2 == 40) {
                        i1 = 56;
                    }
                    if (i2 == 41) {
                        i1 = 57;
                    }
                    if (i2 == 42) {
                        i1 = 58;
                    }
                    if (i2 == 43) {
                        i1 = 59;
                    }
                    if (i2 == 44) {
                        i1 = 60;
                    }
                    if (i2 == 45) {
                        i1 = 61;
                    }
                    if (i2 == 46) {
                        i1 = 62;
                    }
                    if (i2 == 47) {
                        i1 = 63;
                    }
                    if (i2 == 48) {
                        i1 = 64;
                    }
                    if (i2 == 49) {
                        i1 = 65;
                    }
                    if (i2 == 50) {
                        i1 = 66;
                    }
                    if (i2 == 51) {
                        i1 = 67;
                    }
                    if (i2 == 52) {
                        i1 = 68;
                    }


                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 4.5f));
//                                    TextView tv33 = new TextView(Preparing_Orders_w.this);;
                    TableRow.LayoutParams lp, lp1, lp2;

                    TextView tv = new TextView(Preparing_Orders_w.this);
                    tv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv.setGravity(Gravity.START);
                    tv.setTextSize(15);
                    //text = cursor.getString(1);
//                String v = na;

                    tv.setText(us_name1.getString(i2));


                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv1.setGravity(Gravity.START);
                    //text = cursor.getString(1);
                    tv1.setText(us_name1.getString(i1));
                    String value1 = "0";
                    if (tv1.getText().toString().equals("")) {

                    } else {
                        value1 = tv1.getText().toString();
                    }


                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                    tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv2.append(us_name1.getString(i2) + " @ " + us_name1.getString(i1) + "%");
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setPadding(0, 0, 20, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(tv2);
//                    Toast.makeText(Preparing_Orders_w.this, "hiii "+na38 + " @ " + us_name1.getString(i1) + "%", Toast.LENGTH_LONG).show();

                    TextView textView1 = new TextView(Preparing_Orders_w.this);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(textView1);

//                    Toast.makeText(Preparing_Orders_w.this, " "+i1 + " @ " + value1 + "%", Toast.LENGTH_LONG).show();

                    TextView tv33 = new TextView(Preparing_Orders_w.this);
                    tv33.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                    tv33.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    float tota = Float.parseFloat(value1) * Float.parseFloat(String.valueOf(to)) / 100;
//                float mul = ((Float.parseFloat(total2)+Float.parseFloat(total_disc_print)) / 100) * Float.parseFloat(value1);
//                    float mul = Float.parseFloat(value1) * (Float.parseFloat(total)+Float.parseFloat(total_disc)) / 100;
//                        float tota = mul;
                    String tota1 = String.format(Locale.US,"%.2f", tota);
                    tv33.setText(String.valueOf(tota));
                    tv33.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    //tv3.setTextColor(Color.parseColor("#000000"));
                    //row.addView(tv3);


                    tv33.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(tv33);

                    String value2 = tv33.getText().toString();
//                    Toast.makeText(Preparing_Orders_w.this, "11 " + String.valueOf(tota1), Toast.LENGTH_LONG).show();

                    if (tv33.getText().toString().equals("0") || tv33.getText().toString().equals("0.0") || tv33.getText().toString().equals("0.00")
                            || tv33.getText().toString().equals("") || tv.getText().toString().equals("")) {

                    } else {
                        tableLayout1.addView(row);
//                        Toast.makeText(Preparing_Orders_w.this, "22 " + us_name1.getString(i2) + " @ " + us_name1.getString(i1) + "%" + "---" + String.valueOf(tota1), Toast.LENGTH_LONG).show();
                        String mod1 = us_name1.getString(i2) + " @ " + us_name1.getString(i1) + "%" + "---" + String.valueOf(tota1);
                        String mod1_1 = us_name1.getString(i2) + "" + String.valueOf(tota1);
                        String mod12 = us_name1.getString(i2);
                        String sum_p_float = String.valueOf(tota1);

//                        if (sum_p_float.length() >= 11) {
//
//                        }
//                        if (sum_p_float.length() == 10) {
//                            sum_p_float = " "+sum_p_float;
//                        }
//                        if (sum_p_float.length() == 9) {
//                            sum_p_float = "  "+sum_p_float;
//                        }
//                        if (sum_p_float.length() == 8) {
//                            sum_p_float = "   "+sum_p_float;
//                        }
//                        if (sum_p_float.length() == 7) {
//                            sum_p_float = "    "+sum_p_float;
//                        }
//                        if (sum_p_float.length() == 6) {
//                            sum_p_float = "     "+sum_p_float;
//                        }
//                        if (sum_p_float.length() == 5) {
//                            sum_p_float = "      "+sum_p_float;
//                        }
//                        if (sum_p_float.length() == 4) {
//                            sum_p_float = "       "+sum_p_float;
//                        }

                        fmt.format("%-38s %9s\n", mod12, sum_p_float);

                    }

                }
            }

        } else {
//            Toast.makeText(Preparing_Orders_w.this, "user id not there", Toast.LENGTH_LONG).show();
            Cursor cursorr = null;
            if (paymmethoda.equals("  Dine-in") || paymmethoda.equals("  General") || paymmethoda.equals("  Others")) {
                cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax1 = 'dine_in'", null);//replace to cursor = dbHelper.fetchAllHotels();
            }
            if (paymmethoda.equals("  Takeaway") || paymmethoda.equals("  Main")) {
                cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax2 = 'takeaway'", null);//replace to cursor = dbHelper.fetchAllHotels();
            }
            if (paymmethoda.equals("  Home delivery")) {
                cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax3 = 'homedelivery'", null);//replace to cursor = dbHelper.fetchAllHotels();
            }
//            ccursor = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax'", null);//replace to ccursor = dbHelper.fetchAllHotels();
            if (cursorr.moveToFirst()) {

                do {

                    String name = cursorr.getString(1);
                    String value = cursorr.getString(2);

                    final TableRow row = new TableRow(Preparing_Orders_w.this);
                    row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    row.setGravity(Gravity.CENTER);

                    TableRow.LayoutParams lp, lp1, lp2;

//                                final TextView tv = new TextView(CancelActivity.this);
//                                //tv.setLayoutParams(new TableRow.LayoutParams(40, TableRow.LayoutParams.MATCH_PARENT));
//                                tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.7f));
//                                tv.setTextSize(16);
//                                tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                                row.addView(tv);

                    TextView tvv = new TextView(Preparing_Orders_w.this);
                    tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    //tv.setBackgroundResource(R.drawable.cell_shape);
                    tvv.setGravity(Gravity.START);
                    tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tvv.setText(name);

                    TextView tv1 = new TextView(Preparing_Orders_w.this);
                    tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                    tv1.setGravity(Gravity.START);
                    tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv1.setText(value);
                    float vbn = Float.parseFloat(value);
                    String bvn = String.format(Locale.US,"%.2f", vbn);
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    String value1 = tv1.getText().toString();

                    TextView tv2 = new TextView(Preparing_Orders_w.this);
                    //lp = new TableRow.LayoutParams(145, TableRow.LayoutParams.WRAP_CONTENT);
                    //tv2.setLayoutParams(lp);
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                    tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    tv2.append(name + " @ " + bvn + "%");
                    tv2.setPadding(0, 0, 20, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(tv2);

                    TextView textView1 = new TextView(Preparing_Orders_w.this);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    row.addView(textView1);

                    TextView tv3 = new TextView(Preparing_Orders_w.this);
//                lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
//                tv3.setLayoutParams(lp2);
                    tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                    //tv3.setPadding(0,0,10,0);
                    tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    float tota = Float.parseFloat(value1) * Float.parseFloat(String.valueOf(to)) / 100;
//                        float tota = mul;
                    String tota1 = String.format(Locale.US,"%.2f", tota);
                    tv3.setText(String.valueOf(tota));
                    //row.addView(tv3);


                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    String value2 = tv3.getText().toString();
                    row.addView(tv3);

                    tableLayout1.addView(row);

                    String mod1 = name + " @ " + bvn + "%" + "---" + String.valueOf(tota1);
                    String mod1_1 = name + "" + String.valueOf(tota1);
                    String mod12 = name;
                    String sum_p_float = String.valueOf(tota1);

//                    if (sum_p_float.length() >= 11) {
//
//                    }
//                    if (sum_p_float.length() == 10) {
//                        sum_p_float = " "+sum_p_float;
//                    }
//                    if (sum_p_float.length() == 9) {
//                        sum_p_float = "  "+sum_p_float;
//                    }
//                    if (sum_p_float.length() == 8) {
//                        sum_p_float = "   "+sum_p_float;
//                    }
//                    if (sum_p_float.length() == 7) {
//                        sum_p_float = "    "+sum_p_float;
//                    }
//                    if (sum_p_float.length() == 6) {
//                        sum_p_float = "     "+sum_p_float;
//                    }
//                    if (sum_p_float.length() == 5) {
//                        sum_p_float = "      "+sum_p_float;
//                    }
//                    if (sum_p_float.length() == 4) {
//                        sum_p_float = "       "+sum_p_float;
//                    }

                    fmt.format("%-38s %9s\n", mod12, sum_p_float);

                } while (cursorr.moveToNext());
            }
            cursorr.close();
        }
        us_name1.close();

        float sum = 0;
        for (int i = 0; i < tableLayout1.getChildCount(); i++) {
            TableRow mRow = (TableRow) tableLayout1.getChildAt(i);
            TextView mTextView = (TextView) mRow.getChildAt(2);
            sum = sum
                    + Float.parseFloat(mTextView.getText().toString());
        }


        String newsum = String.format("%.2f", sum);

//        if (newsum.length() >= 11) {
//
//        }
//        if (newsum.length() == 10) {
//            newsum = " "+newsum;
//        }
//        if (newsum.length() == 9) {
//            newsum = "  "+newsum;
//        }
//        if (newsum.length() == 8) {
//            newsum = "   "+newsum;
//        }
//        if (newsum.length() == 7) {
//            newsum = "    "+newsum;
//        }
//        if (newsum.length() == 6) {
//            newsum = "     "+newsum;
//        }
//        if (newsum.length() == 5) {
//            newsum = "      "+newsum;
//        }
//        if (newsum.length() == 4) {
//            newsum = "       "+newsum;
//        }

        if (sum == 0 || sum == 0.0 || sum == 0.00) {

        } else {
            fmt.format("%-38s %9s\n", "Tax", newsum);
        }
///////////////////////////////// discount

        Cursor cursor4 = db.rawQuery("SELECT * FROM Discountdetails WHERE billno = '" + billnumb + "'", null);
        if (cursor4.moveToFirst()) {
            taxpe = cursor4.getString(5);
        } else {
            taxpe = "0.00";
        }
        cursor4.close();

        Cursor cursor5 = db.rawQuery("SELECT * FROM Discountdetails WHERE billno = '" + billnumb + "'", null);
        if (cursor5.moveToFirst()) {
            dsirs = cursor5.getString(7);
        } else {
            dsirs = "0.00";
        }
        cursor5.close();

        String alldiscinperc1 = "Discount(" + taxpe + "%)";

//        if (dsirs.length() >= 11) {
//
//        }
//        if (dsirs.length() == 10) {
//            dsirs = " "+dsirs;
//        }
//        if (dsirs.length() == 9) {
//            dsirs = "  "+dsirs;
//        }
//        if (dsirs.length() == 8) {
//            dsirs = "   "+dsirs;
//        }
//        if (dsirs.length() == 7) {
//            dsirs = "    "+dsirs;
//        }
//        if (dsirs.length() == 6) {
//            dsirs = "     "+dsirs;
//        }
//        if (dsirs.length() == 5) {
//            dsirs = "      "+dsirs;
//        }
//        if (dsirs.length() == 4) {
//            dsirs = "       "+dsirs;
//        }
        fmt.format("%-38s %9s\n", alldiscinperc1, dsirs);
        //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

        float newe;
//////////////////////////////////////////////

        Cursor cursor113 = db.rawQuery("SELECT SUM(disc_indiv_total) FROM All_Sales WHERE disc_thereornot = 'yes' AND bill_no = '" + billnumb + "'", null);
        if (cursor113.moveToFirst()) {
            float level = cursor113.getFloat(0);
            total = String.valueOf(level);
            float total1 = Float.parseFloat(total);
            total_disc_print_q = String.format(Locale.US,"%.2f", total1);

//            if (total_disc_print_q.length() >= 11) {
//
//            }
//            if (total_disc_print_q.length() == 10) {
//                total_disc_print_q = " "+total_disc_print_q;
//            }
//            if (total_disc_print_q.length() == 9) {
//                total_disc_print_q = "  "+total_disc_print_q;
//            }
//            if (total_disc_print_q.length() == 8) {
//                total_disc_print_q = "   "+total_disc_print_q;
//            }
//            if (total_disc_print_q.length() == 7) {
//                total_disc_print_q = "    "+total_disc_print_q;
//            }
//            if (total_disc_print_q.length() == 6) {
//                total_disc_print_q = "     "+total_disc_print_q;
//            }
//            if (total_disc_print_q.length() == 5) {
//                total_disc_print_q = "      "+total_disc_print_q;
//            }
//            if (total_disc_print_q.length() == 4) {
//                total_disc_print_q = "       "+total_disc_print_q;
//            }

            fmt.format("%-38s %9s\n", "Savings", total_disc_print_q);

        }
        cursor113.close();
////////////////////////////////

        Cursor cursor = db.rawQuery("SELECT * FROM billnumber WHERE billnumber = '" + billnumb + "'", null);
        if (cursor.moveToFirst()){
            String t_total_points = cursor.getString(16);
            String v_tq = cursor.getString(17);

            String v_tq_loy = String.format("%.2f", v_tq);

//            if (v_tq_loy.length() >= 11) {
//
//            }
//            if (v_tq_loy.length() == 10) {
//                v_tq_loy = " "+v_tq_loy;
//            }
//            if (v_tq_loy.length() == 9) {
//                v_tq_loy = "  "+v_tq_loy;
//            }
//            if (v_tq_loy.length() == 8) {
//                v_tq_loy = "   "+v_tq_loy;
//            }
//            if (v_tq_loy.length() == 7) {
//                v_tq_loy = "    "+v_tq_loy;
//            }
//            if (v_tq_loy.length() == 6) {
//                v_tq_loy = "     "+v_tq_loy;
//            }
//            if (v_tq_loy.length() == 5) {
//                v_tq_loy = "      "+v_tq_loy;
//            }
//            if (v_tq_loy.length() == 4) {
//                v_tq_loy = "       "+v_tq_loy;
//            }

            TextView tv = new TextView(Preparing_Orders_w.this);
            tv.setText(t_total_points);

            if (tv.getText().toString().equals("")){

            }else {

                fmt.format("%-38s %9s\n", "Loyalty(" + t_total_points + ")", v_tq_loy);

            }
        }

//////////////////////////////////////Rounded off

        Cursor cursor7 = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billnumb + "'", null);
        if (cursor7.moveToFirst()) {
            subro = cursor7.getString(9);
        }
        cursor7.close();

//            float subro1 = Float.parseFloat(subro);
//            subro = String.format("%.2f", subro1);

//        if (subro.length() >= 11) {
//
//        }
//        if (subro.length() == 10) {
//            subro = " "+subro;
//        }
//        if (subro.length() == 9) {
//            subro = "  "+subro;
//        }
//        if (subro.length() == 8) {
//            subro = "   "+subro;
//        }
//        if (subro.length() == 7) {
//            subro = "    "+subro;
//        }
//        if (subro.length() == 6) {
//            subro = "     "+subro;
//        }
//        if (subro.length() == 5) {
//            subro = "      "+subro;
//        }
//        if (subro.length() == 4) {
//            subro = "       "+subro;
//        }

        fmt.format("%-38s %9s\n", "Rounded", subro);

        //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

        fmt.format("------------------------------------------------", " ");
//////////////Total main

        Cursor cursor8 = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billnumb + "'", null);
        if (cursor8.moveToFirst()) {
            alltotal1 = cursor8.getString(2);
        }
        cursor8.close();

        float all = Float.parseFloat(alltotal1);
        String newf = String.format("%.2f", all);

        String allt = insert1_rs+""+newf;
//        if (allt.length() >= 12) {
//
//        }
//        if (allt.length() == 11) {
//            allt = " "+allt;
//        }
//        if (allt.length() == 10) {
//            allt = "  "+allt;
//        }
//        if (allt.length() == 9) {
//            allt = "   "+allt;
//        }
//        if (allt.length() == 8) {
//            allt = "    "+allt;
//        }
//        if (allt.length() == 7) {
//            allt = "     "+allt;
//        }
//        if (allt.length() == 6) {
//            allt = "      "+allt;
//        }
//        if (allt.length() == 5) {
//            allt = "       "+allt;
//        }
//        if (allt.length() == 4) {
//            allt = "        "+allt;
//        }

        //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

        fmt.format("%-34s %13s\n", "Total", allt);

        fmt.format("------------------------------------------------", " ");
        fmt.format("\n", " ");

        tvkot.setText(bill_coun);
        if (tvkot.getText().toString().equals("")) {

        } else {
            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);
            fmt.format("Bill id."+bill_coun);
        }


        tvkot.setText(strbillone);
        if (tvkot.getText().toString().equals("")){

        }else {
            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);
            fmt.format(strbillone);
        }

        fmt.format("\n", " ");

        testBytes = fmt.toString().getBytes();

        //Getting current device count to check if the printer is attached or not
        mUsbManager = (UsbManager) getSystemService(this.USB_SERVICE);
        mDeviceList = mUsbManager.getDeviceList();

        if (mDeviceList.size() > 0) {

            mDeviceIterator = mDeviceList.values().iterator();
            String usbDevice = "";
            while (mDeviceIterator.hasNext()) {
                UsbDevice usbDevice1 = mDeviceIterator.next();
                mDevice = usbDevice1;
            }

            mInterface = mDevice.getInterface(0);
            mEndPoint = mInterface.getEndpoint(1);// 0 IN and  1 OUT to printer.
            mConnection = mUsbManager.openDevice(mDevice);


            if (mInterface == null) {
                Toast.makeText(this, "INTERFACE IS NULL", Toast.LENGTH_SHORT).show();
            } else if (mConnection == null) {
                Toast.makeText(this, "CONNECTION IS NULL", Toast.LENGTH_SHORT).show();
            } else if (forceCLaim == null) {
                Toast.makeText(this, "FORCE CLAIM IS NULL", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Prepare print... ", Toast.LENGTH_SHORT).show();
                int finalImage_there = image_there;
                Thread thread = new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        Log.e("u", "Printing.. ");
                        UsbInterface intf = mDevice.getInterface(0);
                        UsbEndpoint endpoint = intf.getEndpoint(0);
                        UsbDeviceConnection conn = mUsbManager.openDevice(mDevice);
                        conn.claimInterface(intf, true);

                        byte[] center = new byte[]{0x1b, 0x61, 0x00};
                        byte[] cut_paper = {0x1D, 0x56, 0x41, 0x10};

//                        try {
//                            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
//                                    R.drawable.titto);
//                            if(bmp!=null){
//                                command = Utils.decodeBitmap(bmp);
//
//                            }else{
//                                Log.e("Print Photo error", "the file isn't exists");
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.e("PrintTools", "the file isn't exists");
//                        }

                        conn.bulkTransfer(endpoint,center,center.length,0);
                        if (finalImage_there == 1) {
                            conn.bulkTransfer(endpoint, command, command.length, 0);
                        }
                        conn.bulkTransfer(endpoint, testBytes, testBytes.length, 0);
                        conn.bulkTransfer(endpoint, cut_paper, cut_paper.length, 0);
                    }
                });
                thread.run();
            }
        } else {
            Toast.makeText(this, "No Printer Attached ", Toast.LENGTH_SHORT).show();
        }

    }
    public void footer(){

        Cursor connnet = db1.rawQuery("SELECT * FROM IPConn", null);
        if (connnet.moveToFirst()) {
            ipnamegets = connnet.getString(1);
            portgets = connnet.getString(2);
            statusnets = connnet.getString(3);
        }
        connnet.close();

        Cursor connnet_counter = db1.rawQuery("SELECT * FROM IPConn_Counter", null);
        if (connnet_counter.moveToFirst()) {
            ipnamegets_counter = connnet_counter.getString(1);
            portgets_counter = connnet_counter.getString(2);
            statusnets_counter = connnet_counter.getString(3);
        }
        connnet_counter.close();

        Cursor connusb = db1.rawQuery("SELECT * FROM BTConn", null);
        if (connusb.moveToFirst()) {
            addgets = connusb.getString(1);
            namegets = connusb.getString(2);
            statussusbs = connusb.getString(3);
        }
        connusb.close();

        byte[] setHT34M = {0x1b,0x44,0x04,0x11,0x19,0x00};
        byte[] dotfeed = {0x1b,0x4a,0x10};
        byte[] HTRight = {0x1b,0x61, 0x02,0x09};
        byte[] HT = {0x09};
        byte[] LF = {0x0d,0x0a};

        byte[] left = {0x1b,0x61, 0x00};
        byte[] cen = {0x1b,0x61, 0x01};
        byte[] right = {0x1b,0x61, 0x02};
        byte[] bold = {0x1B,0x21,0x10};
        byte[] normal = {0x1d, 0x21, 0x00};
        byte[] horiz1 = {0x1b,0x44,0x19, 0x19, 0x00};
        byte[] horiz = {0x1b,0x44, 0x04, 0x06, 0x04, 0x04, 0x0A, 0x00, 0x09, 0x30, 0x09, 0x31, 0x09, 0x32, 0x09, 0x33, 0x09, 0x34, 0x0d,0x0a};

        Cursor cc=db1.rawQuery("SELECT * FROM Printerreceiptsize", null);

        if(cc.moveToFirst()){
            cc.moveToFirst();
            do{
                NAME = cc.getString(1);
                if (NAME.equals("3 inch")) {
                    setHT32 = new byte[]{0x1b, 0x44, 0x27, 0x00};//2 tabs 3"
                    setHT321 = new byte[]{0x1b,0x44,0x23,0x00};//2 tabs 3"
                    setHT3212 = new byte[]{0x1b,0x44,0x23,0x00};//2 tabs 3"
                    setHT33 = new byte[]{0x1b,0x44,0x13,0x27,0x00};//3 tabs 3"
                    setHT34 = new byte[]{0x1b,0x44,0x08,0x17,0x27,0x00};//4 tabs 3"
                    nPaperWidth = 576;
                    allbufline = new byte[][]{
                            left, "------------------------------------------------".getBytes(), LF

                    };
                }
                else {
                    setHT32 = new byte[]{0x1b,0x44,0x19,0x00};//2 tabs 2"
                    setHT321 = new byte[]{0x1b,0x44,0x13,0x00};//2 tabs 3"
                    setHT3212 = new byte[]{0x1b,0x44,0x15,0x00};//2 tabs 3"
                    setHT33 = new byte[] {0x1b,0x44,0x09,0x19,0x00};//3 tabs 2"
                    setHT34 = new byte[]{0x1b,0x44,0x04,0x12,0x19,0x00};//4 tabs 2"
                    nPaperWidth = 384;
                    allbufline = new byte[][]{
                            left, "--------------------------------".getBytes(), LF

                    };
                }
            }while(cc.moveToNext());
        }
        cc.close();

//        allbuf2 = new byte[][]{
//                normal, "Thank you! visit again.".getBytes(), LF, LF
//
//        };
//        //byte[] buf1 = DataUtils.byteArraysToBytes(compname);
//
//        if (statussusbs.equals("ok")) {
//            BluetoothPrintDriver.BT_Write(normal);	//
//            BT_Write("Thank you! visit again.");
//            BluetoothPrintDriver.BT_Write(LF);	//
//            BluetoothPrintDriver.BT_Write(LF);	//
//        } else {
//            if (statusnets_counter.equals("ok")) {
//                wifiSocket2.WIFI_Write(normal);	//
//                wifiSocket2.WIFI_Write("Thank you! visit again.");
//                wifiSocket2.WIFI_Write(LF);	//
//                wifiSocket2.WIFI_Write(LF);	//
//            }else {
//                if (statusnets.equals("ok")) {
//                    wifiSocket.WIFI_Write(normal);	//
//                    wifiSocket.WIFI_Write("Thank you! visit again.");
//                    wifiSocket.WIFI_Write(LF);	//
//                    wifiSocket.WIFI_Write(LF);	//
//                }
//            }
//        }

        tvkot.setText(strbillone);
        if (tvkot.getText().toString().equals("")){

        }else {
            allbuf2 = new byte[][]{
                    normal, strbillone.getBytes(), LF

            };
            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

            if (statussusbs.equals("ok")) {
                BluetoothPrintDriver.BT_Write(normal);	//
                BluetoothPrintDriver.BT_Write(cen);	//
                BT_Write(strbillone);
                BluetoothPrintDriver.BT_Write(LF);	//
            } else {
                if (statusnets_counter.equals("ok")) {
                    wifiSocket2.WIFI_Write(normal);	//
                    wifiSocket2.WIFI_Write(cen);	//
                    wifiSocket2.WIFI_Write(strbillone);
                    wifiSocket2.WIFI_Write(LF);	//
                }else {
                    if (statusnets.equals("ok")) {
                        wifiSocket.WIFI_Write(normal);	//
                        wifiSocket.WIFI_Write(cen);	//
                        wifiSocket.WIFI_Write(strbillone);
                        wifiSocket.WIFI_Write(LF);	//
                    }
                }
            }
        }

        feedcut();

    }
    public void feedcut(){
        Cursor cc=db1.rawQuery("SELECT * FROM Printerreceiptsize", null);

        if(cc.moveToFirst()){
            cc.moveToFirst();
            do{
                NAME = cc.getString(1);
                if (NAME.equals("3 inch")) {
                    feedcut2 = new byte[]{0x1b,0x64,0x05, 0x1d,0x56,0x00};
                }
                else {
                    feedcut2 = new byte[]{0x1b,0x64,0x03, 0x1d,0x56,0x00};
                }
            }while(cc.moveToNext());
        }
        cc.close();

        byte[][] allbuf = new byte[][]{
                feedcut2
        };
        if (statussusbs.equals("ok")) {
            BluetoothPrintDriver.BT_Write(feedcut2);	//
        }else {
            if (statusnets_counter.equals("ok")) {
                wifiSocket2.WIFI_Write(feedcut2);	//
            }else {
                if (statusnets.equals("ok")) {
                    wifiSocket.WIFI_Write(feedcut2);	//
                }
            }
        }

        if (str_print_ty.equals("POS")) {
            if (statussusbs.equals("ok")) {
                BluetoothPrintDriver.BT_Write(feedcut2);	//
            }else {
                if (statusnets_counter.equals("ok")) {
                    wifiSocket2.WIFI_Write(feedcut2);	//
                }else {
                    if (statusnets.equals("ok")) {
                        wifiSocket.WIFI_Write(feedcut2);	//
                    }
                }
            }
        }
    }

}
