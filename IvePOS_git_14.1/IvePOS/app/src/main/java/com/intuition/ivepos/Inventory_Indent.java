package com.intuition.ivepos;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.textfield.TextInputLayout;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.intuition.ivepos.csv.RequestSingleton;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import androidx.appcompat.app.AppCompatActivity;
import au.com.bytecode.opencsv.CSVWriter;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

/**
 * Created by Rohithkumar on 8/29/2017.
 */

public class Inventory_Indent extends AppCompatActivity {

    public SQLiteDatabase db = null;
    //    MyCustomAdapter dataAdapter, dataadapter;
    ArrayList<Country_items_inventory_indent> countryList = new ArrayList<Country_items_inventory_indent>();

    Cursor cursor;

    ListView list;
    SimpleCursorAdapter ddataAdapterr;

    TextView text2;
    TextView item_ro, qty_ro, rs_ro;

    String insert1_cc = "", insert1_rs = "", str_country;

    SimpleDateFormat sdff2, sdff1;
    String currentDateandTimee1;
    String timee1;

    File file=null, file1=null;

    int requestCode_i;
    String filepath;


    EditText noofprints;
    ListView listView_setmin_qty;

    String invoice_no = "";

    String strcompanyname, straddress1;

    List toEmailList;

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

    String WebserviceUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_indent);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);

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
                        if (str_country.toString().equals("Dinar")) {
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

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(Inventory_Indent.this);
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

        mCredential = GoogleAccountCredential.usingOAuth2(
                        getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        mProgress = new ProgressDialog(Inventory_Indent.this);
        mProgress.setMessage("Sending mail ...");

        DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
        downloadMusicfromInternet.execute();

        LinearLayout back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        list = (ListView) findViewById(R.id.list);

//        list.setAdapter(null);




        item_ro = (TextView) findViewById(R.id.item_ro);
        qty_ro = (TextView) findViewById(R.id.qty_ro);
        rs_ro = (TextView) findViewById(R.id.rs_ro);

        LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);
        Cursor cd = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
        if (cd.moveToFirst()){
            bottom.setVisibility(View.VISIBLE);
            Cursor cursor_qw1 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
            int co1 = cursor_qw1.getCount();
            item_ro.setText(String.valueOf(co1));

            float co2 = 0;
            Cursor cursor_qw2 = db.rawQuery("SELECT SUM(add_qty) FROM Items WHERE status_qty_updated = 'Add'", null);
            if (cursor_qw2.moveToFirst()){
                co2 = cursor_qw2.getFloat(0);
            }
            cursor_qw2.close();
            qty_ro.setText(String.valueOf(co2));

            float co3 = 0;
            Cursor cursor_qw3 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
            if (cursor_qw3.moveToFirst()){
                do {
                    String add_q = cursor_qw3.getString(22);
                    String ind_p = cursor_qw3.getString(25);

                    float co4 = Float.parseFloat(add_q) * Float.parseFloat(ind_p);
                    co3 = co3+co4;
                }while (cursor_qw3.moveToNext());
            }
            cursor_qw3.close();

            rs_ro.setText(String.format("%.1f", co3));
        }else {
            bottom.setVisibility(View.GONE);
        }
        cd.close();


        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inventory_Indent.this, Inventory_Indent_Processing.class);
                startActivity(intent);
            }
        });

        text2 = (TextView) findViewById(R.id.text2);

        Cursor cursor_qw = db.rawQuery("SELECT * FROM Items WHERE status_low = 'Low'", null);
        int co = cursor_qw.getCount();
        text2.setText(String.valueOf(co));
//            //Toast.makeText(getActivity(), " "+selected, Toast.LENGTH_SHORT).show();
//            try {
//                db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//                Cursor allrows = db.rawQuery("SELECT * FROM Items ", null);
//                System.out.println("COUNT : " + allrows.getCount());
//
//
//                //Country_items_inventory_indent country = new Country_items_inventory_indent(name, name, name, name);
//
//                if (allrows.moveToFirst()) {
//                    do {
//                        String NAme = allrows.getString(1);
//                        String BAr = allrows.getString(16);
//                        String QtY = allrows.getString(3);
//                        String PRiCe = allrows.getString(2);
//                        String PRiCe1 = allrows.getString(2);
//                        Country_items_inventory_indent NAME = new Country_items_inventory_indent(NAme, BAr, QtY, PRiCe, PRiCe1);
//                        //Country_items_inventory_indent PLACE = new Country_items_inventory_indent(PlaCe, PlaCe, PlaCe, PlaCe);
//                        countryList.add(NAME);
//                        //countryList.add(PLACE);
//                    } while (allrows.moveToNext());
//                }
//                allrows.close();
////                        db.close();
//            } catch (Exception e) {
//                Toast.makeText(Inventory_Indent.this, "Error encountered.",
//                        Toast.LENGTH_LONG);
//            }
//
//
//
//
//
//        dataAdapter = new MyCustomAdapter(Inventory_Indent.this,
//                R.layout.inventory_indent_listview, countryList);
//        // Assign adapter to ListView
//        list.setAdapter(dataAdapter);


        final EditText myFilter = (EditText) findViewById(R.id.searchView);
//        myFilter.addTextChangedListener(new TextWatcher() {
//
//            public void afterTextChanged(Editable s) {
//            }
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                ddataAdapterr.getFilter().filter(s.toString());
//            }
//        });


        ImageView delete_icon = (ImageView) findViewById(R.id.delete_icon);
        delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFilter.setText("");
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final Cursor cursor1 = (Cursor) adapterView.getItemAtPosition(i);
                final String ItemID = cursor1.getString(cursor1.getColumnIndex("itemname"));
                final String id = cursor1.getString(cursor1.getColumnIndex("_id"));
                final String priceget = cursor1.getString(cursor1.getColumnIndex("price"));
                final String indiv_priceget = cursor1.getString(cursor1.getColumnIndex("individual_price"));
                final String status_qty_updated = cursor1.getString(cursor1.getColumnIndex("status_qty_updated"));
                final String add_qty = cursor1.getString(cursor1.getColumnIndex("add_qty"));



                final Dialog dialog = new Dialog(Inventory_Indent.this, R.style.timepicker_date_dialog);
                dialog.setContentView(R.layout.dialog_inventory_indent_itemclick);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                TextView inn = (TextView) dialog.findViewById(R.id.inn);
                TextView inn1 = (TextView) dialog.findViewById(R.id.inn1);
                inn.setText(insert1_cc);
                inn1.setText(insert1_cc);
//                Toast.makeText(Inventory_Indent.this, "name is "+ItemID, Toast.LENGTH_LONG).show();


                final TextView pending = (TextView) dialog.findViewById(R.id.pending);
                pending.setText(ItemID);

                final TextView price = (TextView) dialog.findViewById(R.id.price);


                final TextInputLayout qty_edit_layout = (TextInputLayout) dialog.findViewById(R.id.qty_edit_layout);
                final TextInputLayout price_edit_layout = (TextInputLayout) dialog.findViewById(R.id.price_edit_layout);

                final EditText qtyedit = (EditText) dialog.findViewById(R.id.qtyedit);
                qtyedit.setText(add_qty);
                final EditText indiv_price_edit = (EditText) dialog.findViewById(R.id.indiv_price_edit);
                indiv_price_edit.setText(indiv_priceget);

                Cursor cursorz = db.rawQuery("SELECT * FROM Vendor_sold_item_details WHERE itemname = '"+ItemID+"'", null);
                if (cursorz.moveToLast()){
                    String ind = cursorz.getString(5);
                    indiv_price_edit.setText(ind);
                }
                cursorz.close();

                if (indiv_price_edit.getText().toString().equals("") || qtyedit.getText().toString().equals("")){
                    price.setText("0");
                }else {
                    float one = Float.parseFloat(qtyedit.getText().toString()) * Float.parseFloat(indiv_price_edit.getText().toString());
                    price.setText(String.format("%.2f", one));
                }

                qtyedit.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        qty_edit_layout.setError(null);

                        if (qtyedit.getText().toString().equals("") || indiv_price_edit.getText().toString().equals("")){
                            price.setText("0");
                        }else {
                            float one = Float.parseFloat(qtyedit.getText().toString()) * Float.parseFloat(indiv_price_edit.getText().toString());
                            price.setText(String.format("%.2f", one));
                        }
                    }
                });

                indiv_price_edit.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        price_edit_layout.setError(null);


                        if (qtyedit.getText().toString().equals("") || indiv_price_edit.getText().toString().equals("")){
                            price.setText("0");
                        }else {
                            float one = Float.parseFloat(qtyedit.getText().toString()) * Float.parseFloat(indiv_price_edit.getText().toString());
                            price.setText(String.format("%.2f", one));
                        }


                    }
                });


                ImageView closetext = (ImageView) dialog.findViewById(R.id.closetext);
                closetext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm1.hideSoftInputFromWindow(qtyedit.getWindowToken(), 0);

                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(indiv_price_edit.getWindowToken(), 0);

                        dialog.dismiss();
                    }
                });


                Button cleartext = (Button) dialog.findViewById(R.id.cleartext);

                TextView tv1 = new TextView(Inventory_Indent.this);
                tv1.setText(status_qty_updated);
                if (tv1.getText().toString().equals("")){
                    cleartext.setVisibility(View.GONE);
                }else {
                    cleartext.setVisibility(View.VISIBLE);
                }

                cleartext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        qtyedit.setText("");
                        indiv_price_edit.setText("");

                        ContentValues cv = new ContentValues();
                        cv.put("add_qty", "0");
                        cv.put("status_qty_updated", "");
                        cv.put("individual_price", "");
                        String where = "_id = '" + id + "'";
                        db.update("Items", cv, where, new String[]{});

                        String where1_v1 = "docid = '" + id + "' ";
                        db.update("Items_Virtual", cv, where1_v1, new String[]{});

                        cursor1.moveToPosition(i);
                        cursor1.requery();
                        ddataAdapterr.notifyDataSetChanged();



                        InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm1.hideSoftInputFromWindow(qtyedit.getWindowToken(), 0);

                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(indiv_price_edit.getWindowToken(), 0);

                        dialog.dismiss();

                        LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);
                        Cursor cd = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
                        if (cd.moveToFirst()){
                            bottom.setVisibility(View.VISIBLE);
                            Cursor cursor_qw1 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
                            int co1 = cursor_qw1.getCount();
                            item_ro.setText(String.valueOf(co1));

                            float co2 = 0;
                            Cursor cursor_qw2 = db.rawQuery("SELECT SUM(add_qty) FROM Items WHERE status_qty_updated = 'Add'", null);
                            if (cursor_qw2.moveToFirst()){
                                co2 = cursor_qw2.getFloat(0);
                            }
                            cursor_qw2.close();
                            qty_ro.setText(String.valueOf(co2));

                            float co3 = 0;
                            Cursor cursor_qw3 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
                            if (cursor_qw3.moveToFirst()){
                                do {
                                    String add_q = cursor_qw3.getString(22);
                                    String ind_p = cursor_qw3.getString(25);

                                    float co4 = Float.parseFloat(add_q) * Float.parseFloat(ind_p);
                                    co3 = co3+co4;
                                }while (cursor_qw3.moveToNext());
                            }
                            cursor_qw3.close();
                            rs_ro.setText(String.format("%.1f", co3));
                        }else {
                            bottom.setVisibility(View.GONE);
                        }
                        cd.close();

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
                                        if (str_country.toString().equals("Dinar")) {
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
                });

                final Button quantitycontinue = (Button) dialog.findViewById(R.id.quantitycontinue);
                quantitycontinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (qtyedit.getText().toString().equals("") || indiv_price_edit.getText().toString().equals("") || qtyedit.getText().toString().equals("0")){
                            if (qtyedit.getText().toString().equals("")){
                                qty_edit_layout.setError("Fill quantity");
                            }
                            if (indiv_price_edit.getText().toString().equals("")){
                                price_edit_layout.setError("Fill price");
                            }
                        }else {
                            ContentValues cv = new ContentValues();
                            cv.put("add_qty", qtyedit.getText().toString());
                            cv.put("individual_price", indiv_price_edit.getText().toString());
                            cv.put("status_qty_updated", "Add");
                            String where = "_id = '" + id + "'";
                            db.update("Items", cv, where, new String[]{});

                            String where1_v1 = "docid = '" + id + "' ";
                            db.update("Items_Virtual", cv, where1_v1, new String[]{});

                            cursor1.moveToPosition(i);
                            cursor1.requery();
                            ddataAdapterr.notifyDataSetChanged();

                            InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm1.hideSoftInputFromWindow(qtyedit.getWindowToken(), 0);

                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(indiv_price_edit.getWindowToken(), 0);

                            dialog.dismiss();

                            LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);
                            Cursor cd = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
                            if (cd.moveToFirst()){
                                bottom.setVisibility(View.VISIBLE);
                                Cursor cursor_qw1 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
                                int co1 = cursor_qw1.getCount();
                                item_ro.setText(String.valueOf(co1));

                                float co2 = 0;
                                Cursor cursor_qw2 = db.rawQuery("SELECT SUM(add_qty) FROM Items WHERE status_qty_updated = 'Add'", null);
                                if (cursor_qw2.moveToFirst()){
                                    co2 = cursor_qw2.getFloat(0);
                                }
                                cursor_qw2.close();
                                qty_ro.setText(String.valueOf(co2));

                                float co3 = 0;
                                Cursor cursor_qw3 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
                                if (cursor_qw3.moveToFirst()){
                                    do {
                                        String add_q = cursor_qw3.getString(22);
                                        String ind_p = cursor_qw3.getString(25);

                                        float co4 = Float.parseFloat(add_q) * Float.parseFloat(ind_p);
                                        co3 = co3+co4;
                                    }while (cursor_qw3.moveToNext());
                                }
                                cursor_qw3.close();
                                rs_ro.setText(String.format("%.1f", co3));
                            }else {
                                bottom.setVisibility(View.GONE);
                            }
                            cd.close();


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
                                            if (str_country.toString().equals("Dinar")) {
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


//                        dataAdapter = new MyCustomAdapter(Inventory_Indent.this, R.layout.inventory_indent_listview, countryList);
//                            Toast.makeText(Inventory_Indent.this, "updated is " + ItemID + " quan is " + qtyedit.getText().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                indiv_price_edit.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_GO) {
                            quantitycontinue.performClick();
                            return true;
                        }
                        return false;
                    }
                });

            }
        });


        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                DownloadMusicfromInternet1 downloadMusicfromInternet = new DownloadMusicfromInternet1();
//                downloadMusicfromInternet.execute();


                db.execSQL("update Items set minimum_qty_copy = minimum_qty");

//                Cursor cursorq = db.rawQuery("SELECT * FROM Items WHERE minimum_qty_copy != minimum_qty", null);
//                if (cursorq.moveToFirst()){
//                    do {
//                        String id = cursorq.getString(0);
//                        String prese_qty = cursorq.getString(3);
//                        String min_qty = cursorq.getString(20);
//
//
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("minimum_qty_copy", min_qty);
//                        String where = "_id = '"+id+"'";
//                        db.update("Items", contentValues, where, new String[]{});
//                    }while (cursorq.moveToNext());
//                }

                final Dialog dialog = new Dialog(Inventory_Indent.this, R.style.timepicker_date_dialog);
                dialog.setContentView(R.layout.dialog_inventory_indent_addqty);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog.show();


                final EditText editText = (EditText) dialog.findViewById(R.id.search_selecteditem);


                editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            donotshowKeyboard(Inventory_Indent.this);
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            return true;
                        }
                        return false;
                    }
                });


                listView_setmin_qty = (ListView) dialog.findViewById(R.id.listview);
                listView_setmin_qty.setAdapter(null);

                final Cursor cursor3 = db.rawQuery("SELECT * FROM Items", null);
                String[] fromFieldNames = {"itemname", "minimum_qty_copy", "barcode_value"};
                int[] toViewsID = {R.id.selected_item, R.id.enter_value, R.id.bar_value};
                final SimpleCursorAdapter dddataAdapterr = new SimpleCursorAdapter(Inventory_Indent.this, R.layout.dialog_listview_addqty_inventory, cursor3, fromFieldNames, toViewsID, 0);
                listView_setmin_qty.setAdapter(dddataAdapterr);


                listView_setmin_qty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                        final Cursor cursor1 = (Cursor) adapterView.getItemAtPosition(i);
                        final String ItemID = cursor1.getString(cursor1.getColumnIndex("itemname"));
                        final String min_qty_co = cursor1.getString(cursor1.getColumnIndex("minimum_qty_copy"));

                        final TextView tv = (TextView) view.findViewById(R.id.enter_value);

//                        Toast.makeText(BarcodeBulkPrinting.this, "itemname "+ItemID+" "+tv.getText().toString(), Toast.LENGTH_SHORT).show();

                        final Dialog dialog1 = new Dialog(Inventory_Indent.this, R.style.timepicker_date_dialog);
                        dialog1.setContentView(R.layout.dialog_indiv_item_minqty);
                        dialog1.show();

                        TextView tv1 = (TextView) dialog1.findViewById(R.id.itemname);
                        tv1.setText(ItemID);

                        final EditText tv2 = (EditText) dialog1.findViewById(R.id.editText1);
                        tv2.setText(tv.getText().toString());
                        tv2.requestFocus();

                        ImageButton delete1 = (ImageButton) dialog1.findViewById(R.id.btncancel);
                        delete1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.dismiss();
                            }
                        });

                        final Button save = (Button) dialog1.findViewById(R.id.btnsave);
                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (tv2.getText().toString().equals("")){
                                    dialog1.dismiss();
                                }else {
                                    tv.setText(tv2.getText().toString());
                                    Cursor cursor5 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + ItemID + "'", null);
                                    if (cursor5.moveToFirst()) {
                                        do {
                                            String id = cursor5.getString(0);
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("minimum_qty_copy", tv2.getText().toString());
                                            String where1 = "_id = '" + id + "' ";
                                            db.update("Items", contentValues, where1, new String[]{});

                                            String where1_v1 = "docid = '" + id + "' ";
                                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                                        } while (cursor5.moveToNext());
                                    }
                                    cursor5.close();

                                    cursor1.moveToPosition(i);
                                    cursor1.requery();
                                    dddataAdapterr.notifyDataSetChanged();

                                    dialog1.dismiss();
//                                cursor.moveToPosition(position);
//                                cursor.requery();
//                                ddataAdapterr.notifyDataSetChanged();
                                }
                            }
                        });

                        tv2.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                if (actionId == EditorInfo.IME_ACTION_GO) {
                                    save.performClick();
                                    return true;
                                }
                                return false;
                            }
                        });

                    }
                });


                editText.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        dddataAdapterr.getFilter().filter(s.toString());
                    }
                });

                dddataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
                    public Cursor runQuery(CharSequence constraint) {
                        return fetchCountriesByName_bar(constraint.toString());
                    }
                });

                noofprints = (EditText) dialog.findViewById(R.id.noofprints);

                ImageButton imageButton = (ImageButton) dialog.findViewById(R.id.btncancel);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm1.hideSoftInputFromWindow(noofprints.getWindowToken(), 0);

                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                        dialog.dismiss();

                    }
                });


                final ImageView enter = (ImageView) dialog.findViewById(R.id.enter);
                enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (noofprints.getText().toString().equals("")){

                        }else {

                            DownloadMusicfromInternet_setmin_qty downloadMusicfromInternet_setmin_qty = new DownloadMusicfromInternet_setmin_qty();
                            downloadMusicfromInternet_setmin_qty.execute();

//                            db.execSQL("UPDATE Items SET minimum_qty_copy = '"+noofprints.getText().toString()+"'");
//
////                            Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
////                            if (cursor.moveToFirst()) {
////                                do {
////                                    String id = cursor.getString(0);
////                                    ContentValues contentValues = new ContentValues();
////                                    contentValues.put("minimum_qty_copy", noofprints.getText().toString());
////                                    String where1 = "_id = '" + id + "' ";
////                                    db.update("Items", contentValues, where1, new String[]{});
////
////                                    String where1_v1 = "docid = '" + id + "' ";
////                                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
////
////                                } while (cursor.moveToNext());
////                            }
////                            cursor.close();
//
//                            listView_setmin_qty.setAdapter(null);
//                            final Cursor cursor2 = db.rawQuery("SELECT * FROM Items", null);
//                            String[] fromFieldNames = {"itemname", "minimum_qty_copy", "barcode_value"};
//                            int[] toViewsID = {R.id.selected_item, R.id.enter_value, R.id.bar_value};
//                            final SimpleCursorAdapter ddataAdapterr = new SimpleCursorAdapter(Inventory_Indent.this, R.layout.dialog_listview_addqty_inventory, cursor2, fromFieldNames, toViewsID, 0);
//                            listView_setmin_qty.setAdapter(ddataAdapterr);
//
////                            hideKeyboard(getBaseContext());
//                            donotshowKeyboard(Inventory_Indent.this);
//
//                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.hideSoftInputFromWindow(noofprints.getWindowToken(), 0);

                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        }
                    }
                });


                final ImageButton btnsave = (ImageButton) dialog.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        db.execSQL("UPDATE Items SET minimum_qty = minimum_qty_copy");

//                        Cursor cursor1 = db.rawQuery("SELECT * FROM Items", null);
//                        if (cursor1.moveToFirst()){
//                            do {
//                                String id = cursor1.getString(0);
//                                String min_qty = cursor1.getString(20);
//                                String min_qty_copy = cursor1.getString(21);
//                                ContentValues cv = new ContentValues();
//                                cv.put("minimum_qty", min_qty_copy);
//                                String where1 = "_id = '" + id + "' ";
//                                db.update("Items", cv, where1, new String[]{});
//
//                                String where1_v1 = "docid = '" + id + "' ";
//                                db.update("Items_Virtual", cv, where1_v1, new String[]{});
//
//                            }while (cursor1.moveToNext());
//                        }

                        dialog.dismiss();


                        DownloadMusicfromInternet2 downloadMusicfromInternet = new DownloadMusicfromInternet2();
                        downloadMusicfromInternet.execute();


                    }
                });


                noofprints.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_GO) {
                            enter.performClick();
//                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.hideSoftInputFromWindow(noofprints.getWindowToken(), 0);
                            return true;
                        }
                        return false;
                    }
                });


            }
        });


        LinearLayout linearLayout_hist = (LinearLayout) findViewById(R.id.linearLayout_hist);
        linearLayout_hist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inventory_Indent.this, Inventory_Indent_History.class);
                startActivity(intent);
            }
        });

        LinearLayout linearLayout_vend = (LinearLayout) findViewById(R.id.linearLayout_vend);
        linearLayout_vend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inventory_Indent.this, Inventory_Indent_Vendor_list.class);
                startActivity(intent);
            }
        });

        LinearLayout linearLayout_item_report = (LinearLayout) findViewById(R.id.linearLayout_item_report);
        linearLayout_item_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inventory_Indent.this, Inventory_Indent_Items_History.class);
                startActivity(intent);
            }
        });


        final TextView text1 = (TextView) findViewById(R.id.text1);
        final TextView text2 = (TextView) findViewById(R.id.text2);
        final TextView text3 = (TextView) findViewById(R.id.text3);
        RelativeLayout rel = (RelativeLayout) findViewById(R.id.rel);
        rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!text1.getText().toString().equals("Low stock")){
                    text1.setText("Low stock");

                    cursor = db.rawQuery("SELECT * FROM Items", null);
                    String[] fromFieldNames = {"itemname", "barcode_value", "stockquan", "add_qty", "minimum_qty_copy", "status_low", "status_qty_updated"};
                    int[] toViewsID = {R.id.itemname, R.id.barcode_value, R.id.current_qty, R.id.add_qty, R.id.min_qty, R.id.image, R.id.image1};
                    ddataAdapterr = new SimpleCursorAdapter(Inventory_Indent.this, R.layout.inventory_indent_listview, cursor, fromFieldNames, toViewsID, 0);
                    list.setAdapter(ddataAdapterr);


                    LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);
                    Cursor cd = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
                    if (cd.moveToFirst()){
                        bottom.setVisibility(View.VISIBLE);
                        Cursor cursor_qw1 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
                        int co1 = cursor_qw1.getCount();
                        item_ro.setText(String.valueOf(co1));

                        float co2 = 0;
                        Cursor cursor_qw2 = db.rawQuery("SELECT SUM(add_qty) FROM Items WHERE status_qty_updated = 'Add'", null);
                        if (cursor_qw2.moveToFirst()){
                            co2 = cursor_qw2.getFloat(0);
                        }
                        cursor_qw2.close();
                        qty_ro.setText(String.valueOf(co2));

                        float co3 = 0;
                        Cursor cursor_qw3 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
                        if (cursor_qw3.moveToFirst()){
                            do {
                                String add_q = cursor_qw3.getString(22);
                                String ind_p = cursor_qw3.getString(25);

                                float co4 = Float.parseFloat(add_q) * Float.parseFloat(ind_p);
                                co3 = co3+co4;
                            }while (cursor_qw3.moveToNext());
                        }
                        cursor_qw3.close();
                        rs_ro.setText(String.format("%.1f", co3));
                    }else {
                        bottom.setVisibility(View.GONE);
                    }
                    cd.close();

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
                                    if (str_country.toString().equals("Dinar")) {
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

                    bottom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Inventory_Indent.this, Inventory_Indent_Processing.class);
                            startActivity(intent);
                        }
                    });

                    final EditText myFilter = (EditText) findViewById(R.id.searchView);
                    myFilter.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {
                        }

                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            ddataAdapterr.getFilter().filter(s.toString());
                        }
                    });

                    ddataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
                        public Cursor runQuery(CharSequence constraint) {
                            return fetchCountriesByName1(constraint.toString());
                        }
                    });

                    Cursor cursor_qw = db.rawQuery("SELECT * FROM Items WHERE status_low = 'Low'", null);
                    int co = cursor_qw.getCount();
                    text2.setText(String.valueOf(co));
                    text3.setText(" Items");

                }else {
                    text1.setText("All stock");
                    text2.setText("");
                    text3.setText("");

                    cursor = db.rawQuery("SELECT * FROM Items WHERE status_low = 'Low'", null);
                    String[] fromFieldNames = {"itemname", "barcode_value", "stockquan", "add_qty", "minimum_qty_copy", "status_low", "status_qty_updated"};
                    int[] toViewsID = {R.id.itemname, R.id.barcode_value, R.id.current_qty, R.id.add_qty, R.id.min_qty, R.id.image, R.id.image1};
                    ddataAdapterr = new SimpleCursorAdapter(Inventory_Indent.this, R.layout.inventory_indent_listview, cursor, fromFieldNames, toViewsID, 0);
                    list.setAdapter(ddataAdapterr);


                    LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);
                    Cursor cd = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
                    if (cd.moveToFirst()){
                        bottom.setVisibility(View.VISIBLE);
                        Cursor cursor_qw1 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
                        int co1 = cursor_qw1.getCount();
                        item_ro.setText(String.valueOf(co1));

                        float co2 = 0;
                        Cursor cursor_qw2 = db.rawQuery("SELECT SUM(add_qty) FROM Items WHERE status_qty_updated = 'Add'", null);
                        if (cursor_qw2.moveToFirst()){
                            co2 = cursor_qw2.getFloat(0);
                        }
                        cursor_qw2.close();
                        qty_ro.setText(String.valueOf(co2));

                        float co3 = 0;
                        Cursor cursor_qw3 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
                        if (cursor_qw3.moveToFirst()){
                            do {
                                String add_q = cursor_qw3.getString(22);
                                String ind_p = cursor_qw3.getString(25);

                                float co4 = Float.parseFloat(add_q) * Float.parseFloat(ind_p);
                                co3 = co3+co4;
                            }while (cursor_qw3.moveToNext());
                        }
                        cursor_qw3.close();
                        rs_ro.setText(String.format("%.1f", co3));
                    }else {
                        bottom.setVisibility(View.GONE);
                    }
                    cd.close();

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
                                    if (str_country.toString().equals("Dinar")) {
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

                    bottom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Inventory_Indent.this, Inventory_Indent_Processing.class);
                            startActivity(intent);
                        }
                    });

                    final EditText myFilter = (EditText) findViewById(R.id.searchView);
                    myFilter.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {
                        }

                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            ddataAdapterr.getFilter().filter(s.toString());
                        }
                    });

                    ddataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
                        public Cursor runQuery(CharSequence constraint) {
                            return fetchCountriesByName1(constraint.toString());
                        }
                    });

                }
            }
        });


        LinearLayout linearLayout_csv = (LinearLayout) findViewById(R.id.linearLayout_csv);
        linearLayout_csv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Inventory_Indent.this);
//                alertDialog.setMessage("hi");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Inventory_Indent.this, android.R.layout.simple_selectable_list_item);
                arrayAdapter.add(getString(R.string.download));
                arrayAdapter.add(getString(R.string.upload));
                alertDialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String strName = arrayAdapter.getItem(which);
                        if (strName.equals(getString(R.string.download))){
                            sdff2 = new SimpleDateFormat("ddMMMyy", Locale.US);
                            currentDateandTimee1 = sdff2.format(new Date());

                            Date dt = new Date();
                            sdff1 = new SimpleDateFormat("hhmmssaa", Locale.US);
                            timee1 = sdff1.format(dt);

                            ExportDatabaseCSVTask task=new ExportDatabaseCSVTask();
                            task.execute();
                        }else {
                            if (strName.equals(getString(R.string.upload))){
                                Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
                                fileintent.addCategory(Intent.CATEGORY_OPENABLE);
                                fileintent.setType( "*/*");
                                startActivityForResult(Intent.createChooser(fileintent, "Open CSV"), 1);
                                requestCode_i=1;
                            }
                        }

                    }
                });

                alertDialog.show();
            }
        });

        LinearLayout linearLayout_overflow = findViewById(R.id.linearLayout_overflow);
        final PopupMenu popup = new PopupMenu(this, linearLayout_overflow);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.inventory_indent_menu, popup.getMenu());
        linearLayout_overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if(id==R.id.linearLayout){

                            db.execSQL("update Items set minimum_qty_copy = minimum_qty");

                            final Dialog dialog = new Dialog(Inventory_Indent.this, R.style.timepicker_date_dialog);
                            dialog.setContentView(R.layout.dialog_inventory_indent_addqty);
                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            dialog.show();


                            final EditText editText = (EditText) dialog.findViewById(R.id.search_selecteditem);


                            editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                                @Override
                                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                                        donotshowKeyboard(Inventory_Indent.this);
                                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                        return true;
                                    }
                                    return false;
                                }
                            });


                            listView_setmin_qty = (ListView) dialog.findViewById(R.id.listview);
                            listView_setmin_qty.setAdapter(null);

                            final Cursor cursor3 = db.rawQuery("SELECT * FROM Items", null);
                            String[] fromFieldNames = {"itemname", "minimum_qty_copy", "barcode_value"};
                            int[] toViewsID = {R.id.selected_item, R.id.enter_value, R.id.bar_value};
                            final SimpleCursorAdapter dddataAdapterr = new SimpleCursorAdapter(Inventory_Indent.this, R.layout.dialog_listview_addqty_inventory, cursor3, fromFieldNames, toViewsID, 0);
                            listView_setmin_qty.setAdapter(dddataAdapterr);


                            listView_setmin_qty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                                    final Cursor cursor1 = (Cursor) adapterView.getItemAtPosition(i);
                                    final String ItemID = cursor1.getString(cursor1.getColumnIndex("itemname"));
                                    final String min_qty_co = cursor1.getString(cursor1.getColumnIndex("minimum_qty_copy"));

                                    final TextView tv = (TextView) view.findViewById(R.id.enter_value);

//                        Toast.makeText(BarcodeBulkPrinting.this, "itemname "+ItemID+" "+tv.getText().toString(), Toast.LENGTH_SHORT).show();

                                    final Dialog dialog1 = new Dialog(Inventory_Indent.this, R.style.timepicker_date_dialog);
                                    dialog1.setContentView(R.layout.dialog_indiv_item_minqty);
                                    dialog1.show();

                                    TextView tv1 = (TextView) dialog1.findViewById(R.id.itemname);
                                    tv1.setText(ItemID);

                                    final EditText tv2 = (EditText) dialog1.findViewById(R.id.editText1);
                                    tv2.setText(tv.getText().toString());
                                    tv2.requestFocus();

                                    ImageButton delete1 = (ImageButton) dialog1.findViewById(R.id.btncancel);
                                    delete1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog1.dismiss();
                                        }
                                    });

                                    final Button save = (Button) dialog1.findViewById(R.id.btnsave);
                                    save.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (tv2.getText().toString().equals("")){
                                                dialog1.dismiss();
                                            }else {
                                                tv.setText(tv2.getText().toString());
                                                Cursor cursor5 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + ItemID + "'", null);
                                                if (cursor5.moveToFirst()) {
                                                    do {
                                                        String id = cursor5.getString(0);
                                                        ContentValues contentValues = new ContentValues();
                                                        contentValues.put("minimum_qty_copy", tv2.getText().toString());
                                                        String where1 = "_id = '" + id + "' ";
                                                        db.update("Items", contentValues, where1, new String[]{});

                                                        String where1_v1 = "docid = '" + id + "' ";
                                                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                                                    } while (cursor5.moveToNext());
                                                }
                                                cursor5.close();

                                                cursor1.moveToPosition(i);
                                                cursor1.requery();
                                                dddataAdapterr.notifyDataSetChanged();

                                                dialog1.dismiss();
//                                cursor.moveToPosition(position);
//                                cursor.requery();
//                                ddataAdapterr.notifyDataSetChanged();
                                            }
                                        }
                                    });

                                    tv2.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                                        @Override
                                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                            if (actionId == EditorInfo.IME_ACTION_GO) {
                                                save.performClick();
                                                return true;
                                            }
                                            return false;
                                        }
                                    });

                                }
                            });


                            editText.addTextChangedListener(new TextWatcher() {

                                public void afterTextChanged(Editable s) {
                                }

                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    dddataAdapterr.getFilter().filter(s.toString());
                                }
                            });

                            dddataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
                                public Cursor runQuery(CharSequence constraint) {
                                    return fetchCountriesByName_bar(constraint.toString());
                                }
                            });

                            noofprints = (EditText) dialog.findViewById(R.id.noofprints);

                            ImageButton imageButton = (ImageButton) dialog.findViewById(R.id.btncancel);
                            imageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm1.hideSoftInputFromWindow(noofprints.getWindowToken(), 0);

                                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                                    dialog.dismiss();

                                }
                            });


                            final ImageView enter = (ImageView) dialog.findViewById(R.id.enter);
                            enter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (noofprints.getText().toString().equals("")){

                                    }else {

                                        DownloadMusicfromInternet_setmin_qty downloadMusicfromInternet_setmin_qty = new DownloadMusicfromInternet_setmin_qty();
                                        downloadMusicfromInternet_setmin_qty.execute();

//                                        db.execSQL("UPDATE Items SET minimum_qty_copy = '"+noofprints.getText().toString()+"'");
//
////                                        Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
////                                        if (cursor.moveToFirst()) {
////                                            do {
////                                                String id = cursor.getString(0);
////                                                ContentValues contentValues = new ContentValues();
////                                                contentValues.put("minimum_qty_copy", noofprints.getText().toString());
////                                                String where1 = "_id = '" + id + "' ";
////                                                db.update("Items", contentValues, where1, new String[]{});
////
////                                                String where1_v1 = "docid = '" + id + "' ";
////                                                db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
////
////                                            } while (cursor.moveToNext());
////                                        }
////                                        cursor.close();
//
//                                        listView_setmin_qty.setAdapter(null);
//                                        final Cursor cursor2 = db.rawQuery("SELECT * FROM Items", null);
//                                        String[] fromFieldNames = {"itemname", "minimum_qty_copy", "barcode_value"};
//                                        int[] toViewsID = {R.id.selected_item, R.id.enter_value, R.id.bar_value};
//                                        final SimpleCursorAdapter ddataAdapterr = new SimpleCursorAdapter(Inventory_Indent.this, R.layout.dialog_listview_addqty_inventory, cursor2, fromFieldNames, toViewsID, 0);
//                                        listView_setmin_qty.setAdapter(ddataAdapterr);
//
////                            hideKeyboard(getBaseContext());
//                                        donotshowKeyboard(Inventory_Indent.this);
//
//                                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                                        imm.hideSoftInputFromWindow(noofprints.getWindowToken(), 0);

                                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                    }
                                }
                            });


                            final ImageButton btnsave = (ImageButton) dialog.findViewById(R.id.btnsave);
                            btnsave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

//                                    db.execSQL("UPDATE Items SET minimum_qty = minimum_qty_copy");

//                                    Cursor cursor1 = db.rawQuery("SELECT * FROM Items", null);
//                                    if (cursor1.moveToFirst()){
//                                        do {
//                                            String id = cursor1.getString(0);
//                                            String min_qty = cursor1.getString(20);
//                                            String min_qty_copy = cursor1.getString(21);
//                                            ContentValues cv = new ContentValues();
//                                            cv.put("minimum_qty", min_qty_copy);
//                                            String where1 = "_id = '" + id + "' ";
//                                            db.update("Items", cv, where1, new String[]{});
//
//                                            String where1_v1 = "docid = '" + id + "' ";
//                                            db.update("Items_Virtual", cv, where1_v1, new String[]{});
//
//                                        }while (cursor1.moveToNext());
//                                    }

                                    dialog.dismiss();


                                    DownloadMusicfromInternet2 downloadMusicfromInternet = new DownloadMusicfromInternet2();
                                    downloadMusicfromInternet.execute();


//                        list.setAdapter(null);

//                        cursor = db.rawQuery("SELECT * FROM Items", null);
//                        String[] fromFieldNames = {"itemname", "barcode_value", "stockquan", "add_qty", "minimum_qty_copy", "status_low", "status_qty_updated"};
//                        int[] toViewsID = {R.id.itemname, R.id.barcode_value, R.id.current_qty, R.id.add_qty, R.id.min_qty, R.id.image, R.id.image1};
//                        ddataAdapterr = new SimpleCursorAdapter(Inventory_Indent.this, R.layout.inventory_indent_listview, cursor, fromFieldNames, toViewsID, 0);
//                        list.setAdapter(ddataAdapterr);



                                }
                            });


                            noofprints.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                                @Override
                                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                    if (actionId == EditorInfo.IME_ACTION_GO) {
                                        enter.performClick();
//                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.hideSoftInputFromWindow(noofprints.getWindowToken(), 0);
                                        return true;
                                    }
                                    return false;
                                }
                            });


                        }
                        if(id==R.id.linearLayout_csv){
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Inventory_Indent.this);
                            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Inventory_Indent.this, android.R.layout.simple_selectable_list_item);
                            arrayAdapter.add(getString(R.string.download));
                            arrayAdapter.add(getString(R.string.upload));
                            alertDialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final String strName = arrayAdapter.getItem(which);
                                    if (strName.equals(getString(R.string.download))){
                                        sdff2 = new SimpleDateFormat("ddMMMyy", Locale.US);
                                        currentDateandTimee1 = sdff2.format(new Date());

                                        Date dt = new Date();
                                        sdff1 = new SimpleDateFormat("hhmmssaa", Locale.US);
                                        timee1 = sdff1.format(dt);

                                        ExportDatabaseCSVTask task=new ExportDatabaseCSVTask();
                                        task.execute();
                                    }else {
                                        if (strName.equals(getString(R.string.upload))){
                                            Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
                                            fileintent.addCategory(Intent.CATEGORY_OPENABLE);
                                            fileintent.setType( "*/*");
                                            startActivityForResult(Intent.createChooser(fileintent, "Open CSV"), 1);
                                            requestCode_i=1;
                                        }
                                    }

                                }
                            });

                            alertDialog.show();
                        }
                        if(id==R.id.linearLayout_mail){
                            Cursor cursore = db.rawQuery("SELECT * FROM Email_setup", null);
                            if (cursore.moveToFirst()){
                                Cursor cursoree = db.rawQuery("SELECT * FROM Email_recipient", null);
                                if (cursoree.moveToFirst()){
                                    //both are there

                                    sdff2 = new SimpleDateFormat("ddMMMyy", Locale.US);
                                    currentDateandTimee1 = sdff2.format(new Date());

                                    Date dt = new Date();
                                    sdff1 = new SimpleDateFormat("hhmmssaa", Locale.US);
                                    timee1 = sdff1.format(dt);

                                    ExportDatabaseCSVTask1 task=new ExportDatabaseCSVTask1();
                                    task.execute();




                                }else {
                                    //only recipient not there
                                    final Dialog dialoge = new Dialog(Inventory_Indent.this, R.style.timepicker_date_dialog);
                                    dialoge.setContentView(R.layout.email_prerequisites);
                                    dialoge.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                    dialoge.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                    dialoge.show();

                                    ImageButton btncancel = (ImageButton) dialoge.findViewById(R.id.btncancel);
                                    btncancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialoge.dismiss();
                                        }
                                    });

                                    ImageView recipient_notset = (ImageView) dialoge.findViewById(R.id.recipient_notset);
                                    ImageView recipient_set = (ImageView) dialoge.findViewById(R.id.recipient_set);

                                    ImageView sender_notset = (ImageView) dialoge.findViewById(R.id.sender_notset);
                                    ImageView sender_set = (ImageView) dialoge.findViewById(R.id.sender_set);

                                    recipient_notset.setVisibility(View.VISIBLE);

                                    sender_set.setVisibility(View.VISIBLE);

                                    Button gotosettings = (Button) dialoge.findViewById(R.id.gotosettings);
                                    gotosettings.setVisibility(View.GONE);
                                    gotosettings.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Inventory_Indent.this, EmailSetup_Recipients.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
//                                                finish();
                                            dialoge.dismiss();
                                        }
                                    });

                                    Button gotosettings1 = (Button) dialoge.findViewById(R.id.gotosettings1);
                                    gotosettings1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Inventory_Indent.this, EmailSetup_Recipients.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
//                                                finish();
                                            dialoge.dismiss();
                                        }
                                    });


                                }
                                cursoree.close();
                            }else {
                                Cursor cursoree = db.rawQuery("SELECT * FROM Email_recipient", null);
                                if (cursoree.moveToFirst()){
                                    //only sender not there
                                    final Dialog dialoge = new Dialog(Inventory_Indent.this, R.style.timepicker_date_dialog);
                                    dialoge.setContentView(R.layout.email_prerequisites);
                                    dialoge.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                    dialoge.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                    dialoge.show();

                                    ImageButton btncancel = (ImageButton) dialoge.findViewById(R.id.btncancel);
                                    btncancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialoge.dismiss();
                                        }
                                    });

                                    ImageView recipient_notset = (ImageView) dialoge.findViewById(R.id.recipient_notset);
                                    ImageView recipient_set = (ImageView) dialoge.findViewById(R.id.recipient_set);

                                    ImageView sender_notset = (ImageView) dialoge.findViewById(R.id.sender_notset);
                                    ImageView sender_set = (ImageView) dialoge.findViewById(R.id.sender_set);

                                    sender_notset.setVisibility(View.VISIBLE);

                                    recipient_set.setVisibility(View.VISIBLE);

                                    Button gotosettings = (Button) dialoge.findViewById(R.id.gotosettings);
                                    gotosettings.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Inventory_Indent.this, EmailSetup.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
//                                                finish();
                                            dialoge.dismiss();
                                        }
                                    });

                                    Button gotosettings1 = (Button) dialoge.findViewById(R.id.gotosettings1);
                                    gotosettings1.setVisibility(View.GONE);
                                    gotosettings1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Inventory_Indent.this, EmailSetup.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
//                                                finish();
                                            dialoge.dismiss();
                                        }
                                    });

                                }else {
                                    //both recipient and sender not there
                                    final Dialog dialoge = new Dialog(Inventory_Indent.this, R.style.timepicker_date_dialog);
                                    dialoge.setContentView(R.layout.email_prerequisites);
                                    dialoge.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                    dialoge.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                    dialoge.show();

                                    ImageButton btncancel = (ImageButton) dialoge.findViewById(R.id.btncancel);
                                    btncancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialoge.dismiss();
                                        }
                                    });

                                    ImageView recipient_notset = (ImageView) dialoge.findViewById(R.id.recipient_notset);
                                    ImageView recipient_set = (ImageView) dialoge.findViewById(R.id.recipient_set);

                                    ImageView sender_notset = (ImageView) dialoge.findViewById(R.id.sender_notset);
                                    ImageView sender_set = (ImageView) dialoge.findViewById(R.id.sender_set);

                                    recipient_notset.setVisibility(View.VISIBLE);
                                    sender_notset.setVisibility(View.VISIBLE);

                                    Button gotosettings = (Button) dialoge.findViewById(R.id.gotosettings);
                                    gotosettings.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Inventory_Indent.this, EmailSetup.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
//                                                finish();
                                            dialoge.dismiss();
                                        }
                                    });

                                    Button gotosettings1 = (Button) dialoge.findViewById(R.id.gotosettings1);
                                    gotosettings1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Inventory_Indent.this, EmailSetup_Recipients.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
//                                                finish();
                                            dialoge.dismiss();
                                        }
                                    });

                                }
                                cursoree.close();
                            }
                            cursore.close();
                        }
                        return true;
                    }
                });
            }
        });


    }

    public Cursor fetchCountriesByName1(String inputtext) throws SQLException {

        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
            mCursor = db.rawQuery("SELECT * FROM Items", null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Items WHERE itemname LIKE '%" + inputtext + "%' OR barcode_value LIKE '%" + inputtext + "%'", null);
        }

        return mCursor;
    }


    public Cursor fetchCountriesByName_bar(String inputtext) throws SQLException {

        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
            mCursor = db.rawQuery("SELECT * FROM Items", null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Items WHERE itemname LIKE '%" + inputtext + "%' OR barcode_value LIKE '%" + inputtext + "%'", null);
        }

        return mCursor;
    }



    class DownloadMusicfromInternet extends AsyncTask<Void, Integer, Void> {
        private ProgressDialog dialog = new ProgressDialog(Inventory_Indent.this, R.style.timepicker_date_dialog);

        @Override
        protected Void doInBackground(Void... params) {


//            Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
//            if (cursor.moveToFirst()){
//                do {
//                    String id = cursor.getString(0);
//                    String prese_qty = cursor.getString(3);
//                    String min_qty = cursor.getString(20);
//
//                    TextView tv = new TextView(Inventory_Indent.this);
//                    tv.setText(min_qty);
//
//                    TextView tv1 = new TextView(Inventory_Indent.this);
//                    tv1.setText(prese_qty);
//
//                    if (tv.getText().toString().matches(".*[a-zA-Z]+.*")){
//                        ContentValues contentValues1 = new ContentValues();
//                        contentValues1.put("minimum_qty", "0");
//                        String where1 = "_id = '"+id+"'";
//                        db.update("Items", contentValues1, where1, new String[]{});
//
//                        String where1_v1 = "docid = '" + id + "' ";
//                        db.update("Items_Virtual", contentValues1, where1_v1, new String[]{});
//
//                    }
//                }while (cursor.moveToNext());
//            }

            db.execSQL("UPDATE Items SET status_low = ''");
            db.execSQL("UPDATE Items SET minimum_qty_copy = minimum_qty");

//            db.execSQL("UPDATE Items SET status_low = 'Low' WHERE minimum_qty = '' OR minimum_qty IS NULL");

            db.execSQL("UPDATE Items SET status_low = 'Low' WHERE stockquan <= minimum_qty");

//            db.execSQL("UPDATE Items SET status_low = '' WHERE stockquan < 0 AND minimum_qty = 0");

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);

            dialog.setMessage("Loading");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            dialog.setMax(1000);
            //Set the current progress to zero
            dialog.setProgress(0);
            //Display the progress dialog
//            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                @Override
//                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        //dialog.dismiss();
//                        //row.setBackgroundResource(0);
//                        return true;
//                    }
//                    return false;
//                }
//            });
            dialog.show();
        }


        @Override
        protected void onPostExecute(Void result) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();

            dialog.dismiss();

            cursor = db.rawQuery("SELECT * FROM Items", null);
            String[] fromFieldNames = {"itemname", "barcode_value", "stockquan", "add_qty", "minimum_qty_copy", "status_low", "status_qty_updated"};
            int[] toViewsID = {R.id.itemname, R.id.barcode_value, R.id.current_qty, R.id.add_qty, R.id.min_qty, R.id.image, R.id.image1};
            ddataAdapterr = new SimpleCursorAdapter(Inventory_Indent.this, R.layout.inventory_indent_listview, cursor, fromFieldNames, toViewsID, 0);
            list.setAdapter(ddataAdapterr);


            LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);
            Cursor cd = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
            if (cd.moveToFirst()){
                bottom.setVisibility(View.VISIBLE);
                Cursor cursor_qw1 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
                int co1 = cursor_qw1.getCount();
                item_ro.setText(String.valueOf(co1));

                float co2 = 0;
                Cursor cursor_qw2 = db.rawQuery("SELECT SUM(add_qty) FROM Items WHERE status_qty_updated = 'Add'", null);
                if (cursor_qw2.moveToFirst()){
                    co2 = cursor_qw2.getFloat(0);
                }
                cursor_qw2.close();
                qty_ro.setText(String.valueOf(co2));

                float co3 = 0;
                Cursor cursor_qw3 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
                if (cursor_qw3.moveToFirst()){
                    do {
                        String add_q = cursor_qw3.getString(22);
                        String ind_p = cursor_qw3.getString(25);

                        float co4 = Float.parseFloat(add_q) * Float.parseFloat(ind_p);
                        co3 = co3+co4;
                    }while (cursor_qw3.moveToNext());
                }
                cursor_qw3.close();
                rs_ro.setText(String.format("%.1f", co3));
            }else {
                bottom.setVisibility(View.GONE);
            }
            cd.close();

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
                            if (str_country.toString().equals("Dinar")) {
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

            bottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Inventory_Indent.this, Inventory_Indent_Processing.class);
                    startActivity(intent);
                }
            });

            text2 = (TextView) findViewById(R.id.text2);

            Cursor cursor_qw = db.rawQuery("SELECT * FROM Items WHERE status_low = 'Low'", null);
            int co = cursor_qw.getCount();
            text2.setText(String.valueOf(co));

            final EditText myFilter = (EditText) findViewById(R.id.searchView);
            myFilter.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ddataAdapterr.getFilter().filter(s.toString());
                }
            });

            ddataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
                public Cursor runQuery(CharSequence constraint) {
                    return fetchCountriesByName1(constraint.toString());
                }
            });


//            cursor = db.rawQuery("SELECT * FROM Items", null);
//            String[] fromFieldNames = {"itemname", "barcode_value", "stockquan", "add_qty", "minimum_qty_copy", "status_low", "status_qty_updated"};
//            int[] toViewsID = {R.id.itemname, R.id.barcode_value, R.id.current_qty, R.id.add_qty, R.id.min_qty, R.id.image, R.id.image1};
//            ddataAdapterr = new SimpleCursorAdapter(Inventory_Indent.this, R.layout.inventory_indent_listview, cursor, fromFieldNames, toViewsID, 0);
//            list.setAdapter(ddataAdapterr);


        }
    }

    class DownloadMusicfromInternet1 extends AsyncTask<Void, Integer, Void> {
        private ProgressDialog dialog = new ProgressDialog(Inventory_Indent.this, R.style.timepicker_date_dialog);

        @Override
        protected Void doInBackground(Void... params) {


            Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
            if (cursor.moveToFirst()){
                do {
                    String id = cursor.getString(0);
                    String prese_qty = cursor.getString(3);
                    String min_qty = cursor.getString(20);


                    ContentValues contentValues = new ContentValues();
                    contentValues.put("status_low", "");
                    contentValues.put("minimum_qty_copy", min_qty);
                    String where = "_id = '"+id+"'";
                    db.update("Items", contentValues, where, new String[]{});

                    String where1_v1 = "docid = '" + id + "' ";
                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});


                    TextView tv = new TextView(Inventory_Indent.this);
                    tv.setText(min_qty);

                    if (tv.getText().toString().equals("")){

                    }else {
                        if (Float.parseFloat(prese_qty) <= Float.parseFloat(tv.getText().toString())){
                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("status_low", "Low");
                            String where1 = "_id = '"+id+"'";
                            db.update("Items", contentValues1, where1, new String[]{});

                            String where1_v11 = "docid = '" + id + "' ";
                            db.update("Items_Virtual", contentValues1, where1_v11, new String[]{});

                        }
                    }
                }while (cursor.moveToNext());
            }
            cursor.close();


            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);

            dialog.setMessage("Loading");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            dialog.setMax(1000);
            //Set the current progress to zero
            dialog.setProgress(0);
            //Display the progress dialog
//            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                @Override
//                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        //dialog.dismiss();
//                        //row.setBackgroundResource(0);
//                        return true;
//                    }
//                    return false;
//                }
//            });
            dialog.show();
        }


        @Override
        protected void onPostExecute(Void result) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();

            dialog.dismiss();

            Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
            if (cursor.moveToFirst()){
                do {
                    String id = cursor.getString(0);
                    String prese_qty = cursor.getString(3);
                    String min_qty = cursor.getString(20);
                }while (cursor.moveToNext());
            }
            cursor.close();

        }
    }



    class DownloadMusicfromInternet2 extends AsyncTask<Void, Integer, Void> {
        private ProgressDialog dialog = new ProgressDialog(Inventory_Indent.this, R.style.timepicker_date_dialog);

        @Override
        protected Void doInBackground(Void... params) {

            db.execSQL("UPDATE Items SET minimum_qty = minimum_qty_copy");
            webservicequery("UPDATE Items SET minimum_qty = minimum_qty_copy");

//            db.execSQL("UPDATE Items SET minimum_qty_copy = minimum_qty");//removed while keeping loading page

            db.execSQL("UPDATE Items SET status_low = ''");
            db.execSQL("UPDATE Items SET status_low = 'Low' WHERE stockquan <= minimum_qty");

            db.execSQL("UPDATE Items SET status_low = '' WHERE stockquan < 0 AND minimum_qty = 0");

//            Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
//            if (cursor.moveToFirst()){
//                do {
//                    String id = cursor.getString(0);
//                    String prese_qty = cursor.getString(3);
//                    String min_qty = cursor.getString(20);
//
//
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("status_low", "");
//                    contentValues.put("minimum_qty_copy", min_qty);
//                    String where = "_id = '"+id+"'";
//                    db.update("Items", contentValues, where, new String[]{});
//
//                    String where1_v1 = "docid = '" + id + "' ";
//                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//
//                    TextView tv = new TextView(Inventory_Indent.this);
//                    tv.setText(min_qty);
//
//                    if (tv.getText().toString().equals("")){
//
//                    }else {
//                        if (Float.parseFloat(prese_qty) <= Float.parseFloat(tv.getText().toString())){
//                            ContentValues contentValues1 = new ContentValues();
//                            contentValues1.put("status_low", "Low");
//                            String where1 = "_id = '"+id+"'";
//                            db.update("Items", contentValues1, where1, new String[]{});
//
//                            String where1_v11 = "docid = '" + id + "' ";
//                            db.update("Items_Virtual", contentValues1, where1_v11, new String[]{});
//                        }
//                    }
//                }while (cursor.moveToNext());
//            }


            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);

            dialog.setMessage("Loading");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            dialog.setMax(1000);
            //Set the current progress to zero
            dialog.setProgress(0);
            //Display the progress dialog
//            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                @Override
//                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        //dialog.dismiss();
//                        //row.setBackgroundResource(0);
//                        return true;
//                    }
//                    return false;
//                }
//            });
            dialog.show();
        }


        @Override
        protected void onPostExecute(Void result) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();

            dialog.dismiss();

            Cursor cursor_qw = db.rawQuery("SELECT * FROM Items WHERE status_low = 'Low'", null);
            int co = cursor_qw.getCount();
            text2.setText(String.valueOf(co));

            LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);
            Cursor cd = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
            if (cd.moveToFirst()){
                bottom.setVisibility(View.VISIBLE);
                Cursor cursor_qw1 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
                int co1 = cursor_qw1.getCount();
                item_ro.setText(String.valueOf(co1));

                float co2 = 0;
                Cursor cursor_qw2 = db.rawQuery("SELECT SUM(add_qty) FROM Items WHERE status_qty_updated = 'Add'", null);
                if (cursor_qw2.moveToFirst()){
                    co2 = cursor_qw2.getFloat(0);
                }
                cursor_qw2.close();
                qty_ro.setText(String.valueOf(co2));

                float co3 = 0;
                Cursor cursor_qw3 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
                if (cursor_qw3.moveToFirst()){
                    do {
                        String add_q = cursor_qw3.getString(22);
                        String ind_p = cursor_qw3.getString(25);

                        float co4 = Float.parseFloat(add_q) * Float.parseFloat(ind_p);
                        co3 = co3+co4;
                    }while (cursor_qw3.moveToNext());
                }
                cursor_qw3.close();
                rs_ro.setText(String.format("%.1f", co3));
            }else {
                bottom.setVisibility(View.GONE);
            }
            cd.close();

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
                            if (str_country.toString().equals("Dinar")) {
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

            cursor = db.rawQuery("SELECT * FROM Items", null);
            String[] fromFieldNames = {"itemname", "barcode_value", "stockquan", "add_qty", "minimum_qty_copy", "status_low", "status_qty_updated"};
            int[] toViewsID = {R.id.itemname, R.id.barcode_value, R.id.current_qty, R.id.add_qty, R.id.min_qty, R.id.image, R.id.image1};
            ddataAdapterr = new SimpleCursorAdapter(Inventory_Indent.this, R.layout.inventory_indent_listview, cursor, fromFieldNames, toViewsID, 0);
            list.setAdapter(ddataAdapterr);

            final EditText myFilter = (EditText) findViewById(R.id.searchView);
            myFilter.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ddataAdapterr.getFilter().filter(s.toString());
                }
            });

            ddataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
                public Cursor runQuery(CharSequence constraint) {
                    return fetchCountriesByName1(constraint.toString());
                }
            });

        }
    }


    class DownloadMusicfromInternet_setmin_qty extends AsyncTask<Void, Integer, Void> {
        private ProgressDialog dialog = new ProgressDialog(Inventory_Indent.this, R.style.timepicker_date_dialog);

        @Override
        protected Void doInBackground(Void... params) {

            db.execSQL("UPDATE Items SET minimum_qty_copy = '"+noofprints.getText().toString()+"'");
            webservicequery("UPDATE Items SET minimum_qty_copy = '"+noofprints.getText().toString()+"'");

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage("Updating....");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            dialog.setMax(1000);
            //Set the current progress to zero
            dialog.setProgress(0);
            dialog.show();
        }


        @Override
        protected void onPostExecute(Void result) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();

            dialog.dismiss();

            listView_setmin_qty.setAdapter(null);
            final Cursor cursor2 = db.rawQuery("SELECT * FROM Items", null);
            String[] fromFieldNames = {"itemname", "minimum_qty_copy", "barcode_value"};
            int[] toViewsID = {R.id.selected_item, R.id.enter_value, R.id.bar_value};
            final SimpleCursorAdapter ddataAdapterr = new SimpleCursorAdapter(Inventory_Indent.this, R.layout.dialog_listview_addqty_inventory, cursor2, fromFieldNames, toViewsID, 0);
            listView_setmin_qty.setAdapter(ddataAdapterr);

//                            hideKeyboard(getBaseContext());
            donotshowKeyboard(Inventory_Indent.this);

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(noofprints.getWindowToken(), 0);

        }
    }


    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static void showKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static void donotshowKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    private class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(Inventory_Indent.this, R.style.timepicker_date_dialog);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage(getString(R.string.setmessage17));
            this.dialog.show();

        }
        protected Boolean doInBackground(final String... args){

            File dbFile = getDatabasePath("mydb_Salesdata");
            //Log.v(TAG, "Db path is: "+dbFile);  //get the path of db

            File exportDir1 = new File(Environment.getExternalStorageDirectory(), "/Download");
            if (!exportDir1.exists()) {
                exportDir1.mkdirs();
            }

            file1 = new File(exportDir1, "IvePOS_items_inventory"+currentDateandTimee1+"_"+timee1+".csv");
            try {

                file1.createNewFile();
                CSVWriter csvWrite1 = new CSVWriter(new FileWriter(file1));

                // this is the Column of the table and same for Header of CSV file
                String arrStr11[] ={"Itemname", "Qty", "Price", "Invoice No."};
                csvWrite1.writeNext(arrStr11);

//                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                Cursor curCSVv = db.rawQuery("SELECT * FROM Items",null);
                //csvWrite.writeNext(curCSV.getColumnNames());

                if (curCSVv.moveToFirst())  {
                    do {
                        String ItemID = curCSVv.getString(1);
                        Cursor cursorz = db.rawQuery("SELECT * FROM Vendor_sold_item_details WHERE itemname = '"+ItemID+"'", null);
                        if (cursorz.moveToLast()){
                            String ind = cursorz.getString(5);
                            String arrStr[] ={curCSVv.getString(1), "", ind};
                            csvWrite1.writeNext(arrStr);
                        }else {
                            String arrStr[] ={curCSVv.getString(1), "", ""};
                            csvWrite1.writeNext(arrStr);
                        }

                        cursorz.close();
                    }while (curCSVv.moveToNext());
                }
                curCSVv.close();
                csvWrite1.close();

                return true;

            }
            catch (IOException e){
                Log.e("MainActivity", e.getMessage(), e);
                return false;

            }

        }

        @Override
        protected void onPostExecute(final Boolean success)	{

            if (this.dialog.isShowing()){
                this.dialog.dismiss();
            }
            if (success){
                Toast.makeText(Inventory_Indent.this, getString(R.string.export_successful), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(Inventory_Indent.this, getString(R.string.export_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ExportDatabaseCSVTask1 extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(Inventory_Indent.this, R.style.timepicker_date_dialog);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage(getString(R.string.setmessage17));
            this.dialog.show();

        }
        protected Boolean doInBackground(final String... args){

            File dbFile = getDatabasePath("mydb_Salesdata");
            //Log.v(TAG, "Db path is: "+dbFile);  //get the path of db

            File exportDir1 = new File(Environment.getExternalStorageDirectory(), "/Download");
            if (!exportDir1.exists()) {
                exportDir1.mkdirs();
            }

            file1 = new File(exportDir1, "IvePOS_items_inventory"+currentDateandTimee1+"_"+timee1+".csv");
            try {

                file1.createNewFile();
                CSVWriter csvWrite1 = new CSVWriter(new FileWriter(file1));

                // this is the Column of the table and same for Header of CSV file
                String arrStr11[] ={"Itemname", "Qty", "Price", "Invoice No."};
                csvWrite1.writeNext(arrStr11);

//                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                Cursor curCSVv = db.rawQuery("SELECT * FROM Items",null);
                //csvWrite.writeNext(curCSV.getColumnNames());

                if (curCSVv.moveToFirst())  {
                    do {
                        String ItemID = curCSVv.getString(1);
                        Cursor cursorz = db.rawQuery("SELECT * FROM Vendor_sold_item_details WHERE itemname = '"+ItemID+"'", null);
                        if (cursorz.moveToLast()){
                            String ind = cursorz.getString(5);
                            String arrStr[] ={curCSVv.getString(1), "", ind};
                            csvWrite1.writeNext(arrStr);
                        }else {
                            String arrStr[] ={curCSVv.getString(1), "", ""};
                            csvWrite1.writeNext(arrStr);
                        }

                        cursorz.close();
                    }while (curCSVv.moveToNext());
                }
                curCSVv.close();
                csvWrite1.close();

                return true;

            }
            catch (IOException e){
                Log.e("MainActivity", e.getMessage(), e);
                return false;

            }

        }

        @Override
        protected void onPostExecute(final Boolean success)	{

            if (this.dialog.isShowing()){
                this.dialog.dismiss();
            }
            if (success){
                Toast.makeText(Inventory_Indent.this, getString(R.string.export_successful), Toast.LENGTH_SHORT).show();

                Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
                if (getcom.moveToFirst()) {
                    strcompanyname = getcom.getString(1);
                }else {
                    strcompanyname = "";
                }
                getcom.close();

                File exportDir1 = new File(Environment.getExternalStorageDirectory(), "/Download");
                if (!exportDir1.exists()) {
                    exportDir1.mkdirs();
                }

                file1 = new File(exportDir1, "IvePOS_items_inventory"+currentDateandTimee1+"_"+timee1+".csv");
                try {

                    file1.createNewFile();
                    CSVWriter csvWrite1 = new CSVWriter(new FileWriter(file1));

                    // this is the Column of the table and same for Header of CSV file
                    String arrStr11[] ={"Itemname", "Qty", "Price", "Invoice No."};
                    csvWrite1.writeNext(arrStr11);

//                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                    Cursor curCSVv = db.rawQuery("SELECT * FROM Items",null);
                    //csvWrite.writeNext(curCSV.getColumnNames());

                    if (curCSVv.moveToFirst())  {
                        do {
                            String ItemID = curCSVv.getString(1);
                            Cursor cursorz = db.rawQuery("SELECT * FROM Vendor_sold_item_details WHERE itemname = '"+ItemID+"'", null);
                            if (cursorz.moveToLast()){
                                String ind = cursorz.getString(5);
                                String arrStr[] ={curCSVv.getString(1), "", ind};
                                csvWrite1.writeNext(arrStr);
                            }else {
                                String arrStr[] ={curCSVv.getString(1), "", ""};
                                csvWrite1.writeNext(arrStr);
                            }

                            cursorz.close();
                        }while (curCSVv.moveToNext());
                    }
                    curCSVv.close();
                    csvWrite1.close();

                }
                catch (IOException e){
                    Log.e("MainActivity", e.getMessage(), e);

                }

//                                    Uri u2 = null;
//                                    u2 = Uri.fromFile(file1);


                String url = "www.intuitionsoftwares.com";


                String msg = "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
                        "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
                        "Powered by: " + Uri.parse(url);



                Cursor cursor = db.rawQuery("SELECT * FROM Email_setup", null);
                if (cursor.moveToFirst()) {
                    String un = cursor.getString(1);
                    String pwd = cursor.getString(2);
                    String client = cursor.getString(3);


                    Cursor cursor11 = db.rawQuery("SELECT * FROM Email_recipient", null);
                    if (cursor11.moveToFirst()) {
                        do {
                            String unn = cursor11.getString(3);
                            String toEmails = unn;
                            toEmailList = Arrays.asList(toEmails
                                    .split("\\s*,\\s*"));
                        } while (cursor11.moveToNext());
                    }
                    cursor11.close();


                    if (client.equals("Gmail")) {
                        getResultsFromApi();
                        new MakeRequestTask(mCredential).execute();
                    }else {
                        if (client.equals("Yahoo")){
//                                                    Toast.makeText(Inventory_Indent.this, "yahoo "+un, Toast.LENGTH_LONG).show();
                            Cursor cursor1 = db.rawQuery("SELECT * FROM Email_recipient", null);
                            if (cursor1.moveToFirst()) {
                                do {
                                    String unn = cursor1.getString(3);
                                    String toEmails = unn;
                                    toEmailList = Arrays.asList(toEmails
                                            .split("\\s*,\\s*"));
                                    new SendMailTask_Yahoo_attachment(Inventory_Indent.this).execute(un,
                                            pwd, toEmailList, strcompanyname, msg, currentDateandTimee1, timee1);
                                } while (cursor1.moveToNext());
                            }
                            cursor1.close();


                        }else {
                            if (client.equals("Hotmail")){
//                                                        Toast.makeText(Inventory_Indent.this, "Hotmail and Outlook "+un, Toast.LENGTH_LONG).show();
                                Cursor cursor1 = db.rawQuery("SELECT * FROM Email_recipient", null);
                                if (cursor1.moveToFirst()) {
                                    do {
                                        String unn = cursor1.getString(3);
                                        String toEmails = unn;
                                        toEmailList = Arrays.asList(toEmails
                                                .split("\\s*,\\s*"));
                                        new SendMailTask_Hotmail_Outlook_attachment(Inventory_Indent.this).execute(un,
                                                pwd, toEmailList, strcompanyname, msg, currentDateandTimee1, timee1);
                                    } while (cursor1.moveToNext());
                                }
                                cursor1.close();
                            }else {
                                if (client.equals("Office365")) {
//                                                            Toast.makeText(Inventory_Indent.this, "office 365 " + un, Toast.LENGTH_LONG).show();
                                    Cursor cursor1 = db.rawQuery("SELECT * FROM Email_recipient", null);
                                    if (cursor1.moveToFirst()) {
                                        do {
                                            String unn = cursor1.getString(3);
                                            String toEmails = unn;
                                            toEmailList = Arrays.asList(toEmails
                                                    .split("\\s*,\\s*"));
                                            new SendMailTask_Office365_attachment(Inventory_Indent.this).execute(un,
                                                    pwd, toEmailList, strcompanyname, msg, currentDateandTimee1, timee1);
                                        } while (cursor1.moveToNext());
                                    }
                                    cursor1.close();
                                }
                            }
                        }
                    }

                }
                cursor.close();

            }
            else {
                Toast.makeText(Inventory_Indent.this, getString(R.string.export_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Toast.makeText(getActivity(), "a", Toast.LENGTH_SHORT).show();


        if (requestCode_i == 1) {
            String filePath="";
            if (data != null){

                Uri uri = data.getData();

                if(Build.VERSION.SDK_INT >= 26){
                    //create path from uri
                    File file = new File(uri.getPath());
                    if(file.exists()){
                        final String[] split = file.getPath().split(":");//split the path.
                        if(split.length==1){
                            filePath = split[0];
                        }else if(split.length==2){
                            filePath = split[1];
                        }else{
                            filePath = split[1];
                        }
                        //assign it to a string(your choice).
                        Log.e("path ",filePath);
                    }else{
                        try {
                            filePath= PathUtil.getPath(this,uri);
                            Log.e("path pathutil ",filePath);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                }else{
                    File file = new File(uri.getPath());
                    if(file.exists()){
                        //create path from uri
                        final String[] split = file.getPath().split(":");//split the path.
                        if(split.length==1){
                            filePath = split[0];
                        }else if(split.length==2){
                            filePath = split[1];
                        }else{
                            filePath = split[1];
                        }
                        //assign it to a string(your choice).
                        Log.e("path ",filePath);
                    }else{
                        try {
                            filePath= PathUtil.getPath(this,uri);
                            Log.e("path pathutil ",filePath);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }

            up_da(requestCode, resultCode, filePath);
            requestCode_i = 0;
        }

    }


    public void up_da(int requestCode, int resultCode, String data){
        if (data == null)

            return;
        filepath = data;
//                    controller = new DBController(getApplicationContext());
//                    SQLiteDatabase db = controller.getWritableDatabase();
//                String tableName = "Items";
//                db.execSQL("delete from " + tableName);

        try {

            if (resultCode == RESULT_OK) {


                DownloadMusicfromInternet2_upload downloadMusicfromInternet_upload = new DownloadMusicfromInternet2_upload();
                downloadMusicfromInternet_upload.execute();


            } else {
                Dialog d = new Dialog(Inventory_Indent.this);
                d.setTitle(getString(R.string.title15));
                d.show();
            }
        } catch (Exception ex) {
            Dialog d = new Dialog(Inventory_Indent.this);
            d.setTitle(ex.getMessage() + "second");
            d.show();

            // db.endTransaction();

        }
        requestCode_i = 0;
    }

    class DownloadMusicfromInternet2_upload extends AsyncTask<Void, Integer, Void> {
        private ProgressDialog dialog = new ProgressDialog(Inventory_Indent.this, R.style.timepicker_date_dialog);

        @Override
        protected Void doInBackground(Void... params) {

            try {
                FileReader file = new FileReader(filepath);
                BufferedReader buffer = new BufferedReader(file);

                String line = "";
//                        db.beginTransaction();

                while ((line = buffer.readLine()) != null) {
//                            Toast.makeText(getActivity(), "updated", Toast.LENGTH_SHORT).show();


                    String[] str = line.split(",", 4);  // defining 3 columns with null or blank field //values acceptance

                    //Id, Company,Name,Price

                    String name = str[0];
                    String issn = str[1];
                    String imp = str[2];
                    invoice_no = str[3];



                    if (name.contains("\"")) {
                        name = name.replaceAll("\"", "");
                    }

                    if (issn.contains("\"")) {
                        issn = issn.replaceAll("\"", "");
                    }

                    if (issn.contains("'")) {
                        issn = issn.replaceAll("'", "");
                    }

                    if (issn.contains("&")) {
                        issn = issn.replaceAll("&", " and ");
                    }

                    if (imp.contains("\"")) {
                        imp = imp.replaceAll("\"", "");
                    }


                    if (issn.equals("Itemname")) {

                    } else {

                        Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE itemname = '"+name+"'", null);
                        if (cursor.moveToFirst()){
                            String id = cursor.getString(0);
                            ContentValues cv = new ContentValues();
                            if (issn.toString().equals("")){
                                cv.put("add_qty", "0");
                                cv.put("status_qty_updated", "");
                            }else {
                                cv.put("add_qty", issn);
                                cv.put("status_qty_updated", "Add");
                            }
                            if (imp.toString().equals("")){
                                cv.put("individual_price", "0");
                            }else {
                                cv.put("individual_price", imp);
                            }

                            String where = "_id = '" + id + "'";
                            db.update("Items", cv, where, new String[]{});
                        }
                        cursor.close();

                    }


                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref_invoice", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("invoice_no", invoice_no);
                    myEdit.apply();


                }

            } catch (SQLException e) {
                Log.e("Error", e.getMessage());
            } catch (IOException e) {
                Dialog d = new Dialog(Inventory_Indent.this);
                d.setTitle(e.getMessage() + "first");
                d.show();
                // db.endTransaction();
            }


            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);

            dialog.setMessage("Uploading CSV data...\nIt may take few secs to few mins based on data size.");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            dialog.setMax(1000);
            //Set the current progress to zero
            dialog.setProgress(0);
            //Display the progress dialog
//            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                @Override
//                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        //dialog.dismiss();
//                        //row.setBackgroundResource(0);
//                        return true;
//                    }
//                    return false;
//                }
//            });
            dialog.show();
        }


        @Override
        protected void onPostExecute(Void result) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();
            dialog.dismiss();

            cursor = db.rawQuery("SELECT * FROM Items", null);
            String[] fromFieldNames = {"itemname", "barcode_value", "stockquan", "add_qty", "minimum_qty_copy", "status_low", "status_qty_updated"};
            int[] toViewsID = {R.id.itemname, R.id.barcode_value, R.id.current_qty, R.id.add_qty, R.id.min_qty, R.id.image, R.id.image1};
            ddataAdapterr = new SimpleCursorAdapter(Inventory_Indent.this, R.layout.inventory_indent_listview, cursor, fromFieldNames, toViewsID, 0);
            list.setAdapter(ddataAdapterr);

            LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);
            Cursor cd = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
            if (cd.moveToFirst()){
                bottom.setVisibility(View.VISIBLE);
                Cursor cursor_qw1 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
                int co1 = cursor_qw1.getCount();
                item_ro.setText(String.valueOf(co1));

                float co2 = 0;
                Cursor cursor_qw2 = db.rawQuery("SELECT SUM(add_qty) FROM Items WHERE status_qty_updated = 'Add'", null);
                if (cursor_qw2.moveToFirst()){
                    co2 = cursor_qw2.getFloat(0);
                }
                cursor_qw2.close();
                qty_ro.setText(String.valueOf(co2));

                float co3 = 0;
                Cursor cursor_qw3 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
                if (cursor_qw3.moveToFirst()){
                    do {
                        String add_q = cursor_qw3.getString(22);
                        String ind_p = cursor_qw3.getString(25);

                        float co4 = Float.parseFloat(add_q) * Float.parseFloat(ind_p);
                        co3 = co3+co4;
                    }while (cursor_qw3.moveToNext());
                }
                cursor_qw3.close();
                rs_ro.setText(String.format("%.1f", co3));
            }else {
                bottom.setVisibility(View.GONE);
            }
            cd.close();

        }
    }


    private class MakeRequestTask extends AsyncTask<Void, Void, String> {
        private Gmail mService = null;
        private Exception mLastError = null;
//        private View view = sendFabButton;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new Gmail.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName(getResources().getString(R.string.app_name))
                    .build();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private String getDataFromApi() throws IOException {
            // getting Values for to Address, from Address, Subject and Body

            Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
            if (getcom.moveToFirst()) {
                do {
                    strcompanyname = getcom.getString(1);
                    straddress1 = getcom.getString(14);
                } while (getcom.moveToNext());
            }
            getcom.close();

            String url = "www.intuitionsoftwares.com";

            String msg = "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
                    "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
                    "Powered by: " + Uri.parse(url);

            Cursor cursor1 = db.rawQuery("SELECT * FROM Email_recipient", null);
            if (cursor1.moveToFirst()) {
                do {
                    String unn = cursor1.getString(3);
                    TextView edtToAddress = new TextView(Inventory_Indent.this);
                    edtToAddress.setText(unn);

                    TextView edtSubject = new TextView(Inventory_Indent.this);
                    edtSubject.setText(strcompanyname);

                    TextView edtMessage = new TextView(Inventory_Indent.this);
                    edtMessage.setText(msg);

                    String user = "me";
                    String to = Utils.getString(edtToAddress);
                    String from = mCredential.getSelectedAccountName();
                    Log.v("sender email", from);
                    String subject = Utils.getString(edtSubject);
                    String body = Utils.getString(edtMessage);
                    MimeMessage mimeMessage;
                    response = "";
                    try {

//                        File exportDir1 = new File(Environment.getExternalStorageDirectory(), "/Download");
                        String filename = Environment.getExternalStorageDirectory().toString()+"/Download/IvePOS_items_inventory"+currentDateandTimee1+"_"+timee1+".csv";
//                String filename = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_reports/IVEPOS_sales_report/IvePOS_sales_report"+"12May17"+"_"+"013048PM"+".csv";
//                String path = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_backup/";


                        File f = new File(filename);
//
                        mimeMessage = createEmailWithAttachment(to, from, subject, body, f);



//                        mimeMessage = createEmail(to, from, subject, body);
                        response = sendMessage(mService, user, mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }

                } while (cursor1.moveToNext());
            }
            cursor1.close();
            return response;
        }

        // Method to send email
        private String sendMessage(Gmail service,
                                   String userId,
                                   MimeMessage email)
                throws MessagingException, IOException {
            com.google.api.services.gmail.model.Message message = createMessageWithEmail(email);
            // GMail's official method to send email with oauth2.0
            message = service.users().messages().send(userId, message).execute();

            System.out.println("user ID " + userId);

            System.out.println("Message id: " + message.getId());
            System.out.println(message.toPrettyString());
            return message.getId();
        }

        public MimeMessage createEmailWithAttachment(String to,
                                                     String from,
                                                     String subject,
                                                     String bodyText,
                                                     File file)
                throws MessagingException, IOException {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            MimeMessage email = new MimeMessage(session);

            email.setFrom(new InternetAddress(from));
            email.addRecipient(javax.mail.Message.RecipientType.TO,
                    new InternetAddress(to));
            email.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(bodyText, "text/plain");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            mimeBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(file);

            mimeBodyPart.setDataHandler(new DataHandler(source));
            mimeBodyPart.setFileName(file.getName());

            multipart.addBodyPart(mimeBodyPart);
            email.setContent(multipart);

            return email;
        }

        // Method to create email Params
        private MimeMessage createEmail(String to,
                                        String from,
                                        String subject,
                                        String bodyText) throws MessagingException {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            MimeMessage email = new MimeMessage(session);
            InternetAddress tAddress = new InternetAddress(to);
            InternetAddress fAddress = new InternetAddress(from);


            System.out.println("From  " + from);
            System.out.println("To  " + to);


            email.setFrom(fAddress);
            email.addRecipient(javax.mail.Message.RecipientType.TO, tAddress);
            email.setSubject(subject);
            email.setText(bodyText);
            return email;
        }

        private com.google.api.services.gmail.model.Message createMessageWithEmail(MimeMessage email)
                throws MessagingException, IOException {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            email.writeTo(bytes);
            String encodedEmail = Base64.encodeBase64URLSafeString(bytes.toByteArray());
            com.google.api.services.gmail.model.Message message = new com.google.api.services.gmail.model.Message();
            message.setRaw(encodedEmail);
            return message;
        }

        @Override
        protected void onPreExecute() {
            mProgress.show();
        }

        @Override
        protected void onPostExecute(String output) {
            Log.d("post execute", "error");
            mProgress.hide();
            if (output == null || output.length() == 0) {
                Toast.makeText(Inventory_Indent.this, "not success", Toast.LENGTH_SHORT).show();
//                showMessage(view, "No results returned.");
            } else {
                Toast.makeText(Inventory_Indent.this, "success", Toast.LENGTH_SHORT).show();
//                showMessage(view, output);
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
//                Log.v("Errors3", mLastError.getMessage());
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
//                    Log.v("Errors1", mLastError.getMessage());
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
//                    Log.v("Errors2", mLastError.getMessage());
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            Utils.REQUEST_AUTHORIZATION);
                } else {
//                    showMessage(view, "The following error occurred:\n" + mLastError.getMessage());
//                    Log.v("Errors", mLastError.getMessage());
                }
            } else {
//                showMessage(view, "Request Cancelled.");
            }
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                Inventory_Indent.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private void getResultsFromApi() {

        Cursor cursorr = db.rawQuery("SELECT * FROM Email_setup", null);
        if (cursorr.moveToFirst()) {
            String unn = cursorr.getString(1);
//            Toast.makeText(getActivity(), "a4 " + unn, Toast.LENGTH_SHORT).show();

            TextView tvv = new TextView(Inventory_Indent.this);
            tvv.setText(unn);

            if (tvv.getText().toString().equals("")) {

            }else {
                mCredential.setSelectedAccountName(tvv.getText().toString());
            }
        }
        cursorr.close();

        if (! isGooglePlayServicesAvailable()) {
//            Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
//            Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
//            chooseAccount();
        } else if (! isDeviceOnline()) {
//            Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();
//            mOutputText.setText("No network connection available.");
        } else {
//            Toast.makeText(getActivity(), "4", Toast.LENGTH_SHORT).show();
            new MakeRequestTask1(mCredential).execute();
        }
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(Inventory_Indent.this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(Inventory_Indent.this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
//        Toast.makeText(getActivity(), "s1", Toast.LENGTH_SHORT).show();
        if (EasyPermissions.hasPermissions(
                Inventory_Indent.this, android.Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
//            Toast.makeText(getActivity(), "s2", Toast.LENGTH_SHORT).show();
//            if (accountName != null) {
//                mCredential.setSelectedAccountName(accountName);
//                Toast.makeText(getActivity(), "s3", Toast.LENGTH_SHORT).show();
//                getResultsFromApi();
//            } else {
            // Start a dialog from which the user can choose an account
            startActivityForResult(
                    mCredential.newChooseAccountIntent(),
                    REQUEST_ACCOUNT_PICKER);
//            Toast.makeText(getActivity(), "s4", Toast.LENGTH_SHORT).show();
//            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
//            Toast.makeText(getActivity(), "s5", Toast.LENGTH_SHORT).show();
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }


    private class MakeRequestTask1 extends AsyncTask<Void, Void, List<String>> {
        private Gmail mService = null;
        private Exception mLastError = null;

        MakeRequestTask1(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            System.out.println("labels mservice11 " + mService);

            mService = new Gmail.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Gmail API Android Quickstart")
                    .build();
            Log.d("labels credential", String.valueOf(credential));

            System.out.println("labels mservice " + mService);
        }

        /**
         * Background task to call Gmail API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            Log.d("hiiiiii11", "error");

            try {
                Log.d("hiiiiii111", "error");
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                Log.d("hiiiiii1111", "error");
                return null;
            }
        }

        /**
         * Fetch a list of Gmail labels attached to the specified account.
         * @return List of Strings labels.
         * @throws IOException
         */
        private List<String> getDataFromApi() throws IOException {
            // Get the labels in the user's account.
            String user = "me";
            List<String> labels = new ArrayList<String>();
            ListLabelsResponse listResponse =
                    mService.users().labels().list(user).execute();
            System.out.println("ListLabelsResponse " + listResponse);
            for (Label label : listResponse.getLabels()) {
                labels.add(label.getName());

//                Log.d("labels", String.valueOf(labels));//will be displaying all the folders one by one by looping

//                System.out.println("user ID " + labels.add(label.getName()));
            }
            return labels;
        }


        @Override
        protected void onPreExecute() {
//            mOutputText.setText("");
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
//            System.out.println("labelsss " + output);//will be displaying details and folders in mail like inbox, sent, outbox, junk, etc
            mProgress.hide();
            if (output == null || output.size() == 0) {
//                mOutputText.setText("No results returned.");
            } else {
                output.add(0, "Data retrieved using the Gmail API:");
//                mOutputText.setText(TextUtils.join("\n", output));
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            EmailSetup_Google.REQUEST_AUTHORIZATION);
                } else {
//                    mOutputText.setText("The following error occurred:\n"
//                            + mLastError.getMessage());
                }
            } else {
//                mOutputText.setText("Request cancelled.");
            }
        }
    }

    public void webservicequery(final String webserviceQuery){


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(Inventory_Indent.this);
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
        RequestQueue queue;
        StringRequest sr1;
        // queue = Volley.newRequestQueue(getActivity());

        queue= RequestSingleton.getInstance(Inventory_Indent.this).getInstance();

        sr1 = new StringRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl + "webservicequery.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                               /*     params.put("email", email + "");
                                    params.put("password", password + "");*/


//                            final String email = prefs.getString("emailid", "");
//                            final String pwd = prefs.getString("password", "");
                params.put("device", device);
                params.put("store", store);
                params.put("company", company);
                params.put("data", webserviceQuery);
                return params;
            }
        };
    /*    sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        sr1.setRetryPolicy(new DefaultRetryPolicy(0, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr1);
    }

}
