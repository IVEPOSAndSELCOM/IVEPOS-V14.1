package com.intuition.ivepos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
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

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class Taxes_List_Sales1 extends AppCompatActivity {

    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;

    TextView editText1, editText2, editText11, editText22;
    TextView editText_from_day_hide, editText_from_day_visible, editText_to_day_hide, editText_to_day_visible;
    TextView editText1_filter, editText2_filter;

    int padding_in_px, padding_in_px1, padding_in_px2,size_in_95px, size_in_116px, size_in_107px, size_in_60px;

    SimpleDateFormat sdff2, sdff1;
    String currentDateandTimee1;
    String timee1;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Gmail API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { GmailScopes.GMAIL_SEND };

    String response;
    GoogleAccountCredential mCredential;
    ProgressDialog mProgress;

    List toEmailList;
    String companynameis;

    String insert1_cc = "", insert1_rs = "", str_country;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taxes_list_sales);


        mCredential = GoogleAccountCredential.usingOAuth2(
                Taxes_List_Sales1.this.getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        mProgress = new ProgressDialog(Taxes_List_Sales1.this, R.style.timepicker_date_dialog);
        mProgress.setMessage(getString(R.string.setmessage14));

        LinearLayout back_activity = (LinearLayout) findViewById(R.id.back_activity);
        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        String player1name = extras.getString("PLAYER1NAME");
        String player2name = extras.getString("PLAYER2NAME");
        String str_edittext1 = extras.getString("edittext1");
        String str_edittext11 = extras.getString("edittext11");
        String str_edittext2 = extras.getString("edittext2");
        String str_edittext22 = extras.getString("edittext22");
        String str_edittext_from_day_visible = extras.getString("edittext_from_day_visible");
        String str_edittext_from_day_hide = extras.getString("edittext_from_day_hide");
        String str_edittext_to_day_visible = extras.getString("edittext_to_day_visible");
        String str_edittext_to_day_hide = extras.getString("edittext_to_day_hide");


        editText1 = new TextView(Taxes_List_Sales1.this);
        editText11 = new TextView(Taxes_List_Sales1.this);
        editText2 = new TextView(Taxes_List_Sales1.this);
        editText22 = new TextView(Taxes_List_Sales1.this);
        editText_from_day_visible = new TextView(Taxes_List_Sales1.this);
        editText_from_day_hide = new TextView(Taxes_List_Sales1.this);
        editText_to_day_visible = new TextView(Taxes_List_Sales1.this);
        editText_to_day_hide = new TextView(Taxes_List_Sales1.this);

        editText1_filter = new TextView(Taxes_List_Sales1.this);
        editText2_filter = new TextView(Taxes_List_Sales1.this);

        editText1_filter.setText(player1name);
        editText2_filter.setText(player2name);

        editText1.setText(str_edittext1);
        editText11.setText(str_edittext11);
        editText2.setText(str_edittext2);
        editText22.setText(str_edittext22);
        editText_from_day_visible.setText(str_edittext_from_day_visible);
        editText_from_day_hide.setText(str_edittext_from_day_hide);
        editText_to_day_visible.setText(str_edittext_to_day_visible);
        editText_to_day_hide.setText(str_edittext_to_day_hide);

        db = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        db1 = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        Cursor cursor_country = db1.rawQuery("SELECT * FROM Country_Selection", null);
        if (cursor_country.moveToFirst()){
            str_country = cursor_country.getString(1);
        }

        TextView inn = (TextView) findViewById(R.id.inn);
        TextView inn1 = (TextView) findViewById(R.id.inn1);
        TextView inn2 = (TextView) findViewById(R.id.inn2);

        if (str_country.toString().equals("Rupee") || str_country.toString().equals("India")) {
            insert1_cc = "\u20B9";
            insert1_rs = "Rs.";
        }else {
            if (str_country.toString().equals("Pound") || str_country.toString().equals("UK Pound")) {
                insert1_cc = "\u00a3";
                insert1_rs = "BP.";
            }else {
                if (str_country.toString().equals("Euro") || str_country.toString().equals("UK Euro")) {
                    insert1_cc = "\u20ac";
                    insert1_rs = "EU.";
                }else {
                    if (str_country.toString().equals("Dollar")) {
                        insert1_cc = "\u0024";
                        insert1_rs = "\u0024";
                    }else {
                        if (str_country.toString().equals("Dinars")) {
                            insert1_cc = "D";
                            insert1_rs = "KD.";
                        }else {
                            if (str_country.toString().equals("Shilling")) {
                                insert1_cc = "S";
                                insert1_rs = "S.";
                            }else {
                                if (str_country.toString().equals("Ringitt")) {
                                    insert1_cc = "R";
                                    insert1_rs = "RM.";
                                }else {
                                    if (str_country.toString().equals("Rial")) {
                                        insert1_cc = "R";
                                        insert1_rs = "OR.";
                                    }else {
                                        if (str_country.toString().equals("Yen")) {
                                            insert1_cc = "\u00a5";
                                            insert1_rs = "\u00a5";
                                        }else {
                                            if (str_country.toString().equals("Papua New Guinean")) {
                                                insert1_cc = "K";
                                                insert1_rs = "K.";
                                            }else {
                                                if (str_country.toString().equals("UAE")) {
                                                    insert1_cc = "D";
                                                    insert1_rs = "DH.";
                                                }else {
                                                    if (str_country.toString().equals("South African Rand")) {
                                                        insert1_cc = "R";
                                                        insert1_rs = "R.";
                                                    }else {
                                                        if (str_country.toString().equals("Congolese Franc")) {
                                                            insert1_cc = "F";
                                                            insert1_rs = "FC.";
                                                        }else {
                                                            if (str_country.toString().equals("Qatari Riyals")) {
                                                                insert1_cc = "QAR";
                                                                insert1_rs = "QAR.";
                                                            }else {
                                                                if (str_country.toString().equals("Dirhams")) {
                                                                    insert1_cc = "AED";
                                                                    insert1_rs = "AED.";
                                                                }else {
                                                                    if (str_country.toString().equals("Kuwait Dinar")) {
                                                                        insert1_cc = "KWD";
                                                                        insert1_rs = "KWD.";
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

        inn.setText(insert1_cc);
        inn1.setText(insert1_cc);
        inn2.setText(insert1_cc);

        Cursor cursor_c = db1.rawQuery("SELECT * FROM Companydetailss", null);
        if (cursor_c.moveToFirst()) {
            companynameis = cursor_c.getString(1);
        }else {
            companynameis = "";
        }
        cursor_c.close();

        int padding_in_dp = 10;  // 20 dps
        final float scale = getResources().getDisplayMetrics().density;
        padding_in_px = (int) (padding_in_dp * scale + 0.5f);

        int padding_in_dp1 = 12;  // 12 dps
        final float scale1 = getResources().getDisplayMetrics().density;
        padding_in_px1 = (int) (padding_in_dp1 * scale1 + 0.5f);

        int padding_in_dp2 = 9;  // 8 dps
        final float scale2 = getResources().getDisplayMetrics().density;
        padding_in_px2 = (int) (padding_in_dp2 * scale2 + 0.5f);

//        final String selectQuery = "Select DISTINCT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname != '' AND taxname2 != '' AND taxname3 != '' AND taxname4 != '' AND taxname5 != ''";
//
//        Cursor cursor = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
//        // The desired columns to be bound
//        final String[] fromFieldNames = {"taxname", "taxname2", "taxname3", "taxname4", "taxname5", "hsn_code"};
//        // the XML defined views which the data will be bound to
//        //final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.billno, R.id.sales, R.id.billdetails, R.id.itemqqau};
//        final int[] toViewsID = {R.id.taxname, R.id.taxname, R.id.taxname, R.id.taxname, R.id.taxname, R.id.hsn_code};
//        //Log.e("Checamos que hay id", String.valueOf(R.id.name));
//        SimpleCursorAdapter adapter = new SimpleCursorAdapter(Taxes_List_Sales1.this, R.layout.taxes_listivew_sales, cursor, fromFieldNames, toViewsID, 0);
//        listView.setAdapter(adapter);


        final TableLayout tableLayout = (TableLayout) findViewById(R.id.lytpedido);
        tableLayout.removeAllViews();


        float value1 = 0;
        Cursor cursor_global = db.rawQuery("Select * from billnumber WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (cursor_global.moveToFirst()) {
            do {
                String global_percent = cursor_global.getString(10);
                String subtotal_value = cursor_global.getString(7);

                float value = (Float.parseFloat(subtotal_value)/100) * Float.parseFloat(global_percent);
                value1 = value+value1;

            } while (cursor_global.moveToNext());
        }


        if (value1 == 0 || value1 == 0.0 || value1 == 0.00) {

        }else {

            TableRow row = (TableRow) LayoutInflater.from(Taxes_List_Sales1.this).inflate(R.layout.attrib_row, null);

            TextView tax_n = (TextView) row.findViewById(R.id.tax);
            tax_n.setText("Global tax");

            TextView hsn_n = (TextView) row.findViewById(R.id.hsn);
            hsn_n.setText("NA");

            TextView items_qty = (TextView) row.findViewById(R.id.items_qty);
            items_qty.setText("NA");

            TextView sale = (TextView) row.findViewById(R.id.sale);
            sale.setText("NA");

            TextView tax_value1 = (TextView) row.findViewById(R.id.tax_value);
            tax_value1.setText(String.format("%.2f", value1));

            TextView total = (TextView) row.findViewById(R.id.total);
            total.setText("NA");

            tableLayout.addView(row);
        }

        String tax = "", tax2 = "", tax3 = "", tax4 = "", tax5 = "";
        Cursor cursor = db.rawQuery("Select * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' GROUP BY taxname", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (cursor.moveToFirst()) {
            do {
                tax = cursor.getString(10);
                System.out.println("tax value is 1 "+tax);

                TextView tv = new TextView(Taxes_List_Sales1.this);
                tv.setText(tax);

                if (tv.getText().toString().equals("0") || tv.getText().toString().equals("NONE") || tv.getText().toString().equals("None") || tv.getText().toString().equals("0.0")
                        || tv.getText().toString().equals("")) {

                }else {

                    TableRow row = (TableRow) LayoutInflater.from(Taxes_List_Sales1.this).inflate(R.layout.attrib_row, null);

                    String hsn_code = "";
                    Cursor cursor1 = db.rawQuery("SELECT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax + "' GROUP BY taxname", null);
                    if (cursor1.moveToFirst()) {
                        hsn_code = cursor1.getString(34);
                        System.out.println("han_code is 1 " + hsn_code);
                    }

                    String items = "0", items2 = "0", items3 = "0", items4 = "0", items5 = "0";
                    Cursor cursor21 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax + "'", null);
                    if (cursor21.moveToFirst()) {
                        int leveliss = cursor21.getInt(0);
                        items = String.valueOf(leveliss);
                        System.out.println("no. of items 1 " + items);
                    }

                    Cursor cursor212 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax + "'", null);
                    if (cursor212.moveToFirst()) {
                        int leveliss = cursor212.getInt(0);
                        items2 = String.valueOf(leveliss);
                        System.out.println("no. of items 12 " + items2);
                    }

                    Cursor cursor213 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax + "'", null);
                    if (cursor213.moveToFirst()) {
                        int leveliss = cursor213.getInt(0);
                        items3 = String.valueOf(leveliss);
                        System.out.println("no. of items 13 " + items3);
                    }

                    Cursor cursor214 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax + "'", null);
                    if (cursor214.moveToFirst()) {
                        int leveliss = cursor214.getInt(0);
                        items4 = String.valueOf(leveliss);
                        System.out.println("no. of items 14 " + items4);
                    }

                    Cursor cursor215 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax + "'", null);
                    if (cursor215.moveToFirst()) {
                        int leveliss = cursor215.getInt(0);
                        items5 = String.valueOf(leveliss);
                        System.out.println("no. of items 15 " + items5);
                    }

                    float a1 = Float.parseFloat(items)+Float.parseFloat(items2)+Float.parseFloat(items3)+Float.parseFloat(items4)+Float.parseFloat(items5);

                    String qty = "0", qty2 = "0", qty3 = "0", qty4 = "0", qty5 = "0";
                    Cursor cursor22 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax + "'", null);
                    if (cursor22.moveToFirst()) {
                        int leveliss = cursor22.getInt(0);
                        qty = String.valueOf(leveliss);
                        System.out.println("no. of qty 1 " + qty);
                    }

                    Cursor cursor222 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax + "'", null);
                    if (cursor222.moveToFirst()) {
                        int leveliss = cursor222.getInt(0);
                        qty2 = String.valueOf(leveliss);
                        System.out.println("no. of qty 12 " + qty2);
                    }

                    Cursor cursor223 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax + "'", null);
                    if (cursor223.moveToFirst()) {
                        int leveliss = cursor223.getInt(0);
                        qty3 = String.valueOf(leveliss);
                        System.out.println("no. of qty 13 " + qty3);
                    }

                    Cursor cursor224 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax + "'", null);
                    if (cursor224.moveToFirst()) {
                        int leveliss = cursor224.getInt(0);
                        qty4 = String.valueOf(leveliss);
                        System.out.println("no. of qty 14 " + qty4);
                    }

                    Cursor cursor225 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax + "'", null);
                    if (cursor225.moveToFirst()) {
                        int leveliss = cursor225.getInt(0);
                        qty5 = String.valueOf(leveliss);
                        System.out.println("no. of qty 15 " + qty5);
                    }

                    float a3 = Float.parseFloat(qty)+Float.parseFloat(qty2)+Float.parseFloat(qty3)+Float.parseFloat(qty4)+Float.parseFloat(qty5);

                    String totalbillis = "0", totalbillis2 = "0", totalbillis3 = "0", totalbillis4 = "0", totalbillis5 = "0";
                    Cursor cursor23 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax + "'", null);
                    if (cursor23.moveToFirst()) {
                        int leveliss = cursor23.getInt(0);
                        totalbillis = String.valueOf(leveliss);
                        System.out.println("no. of total 11 " + totalbillis);
                    }

                    Cursor cursor232 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax + "'", null);
                    if (cursor232.moveToFirst()) {
                        int leveliss = cursor232.getInt(0);
                        totalbillis2 = String.valueOf(leveliss);
                        System.out.println("no. of total 12 " + totalbillis2);
                    }

                    Cursor cursor233 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax + "'", null);
                    if (cursor233.moveToFirst()) {
                        int leveliss = cursor233.getInt(0);
                        totalbillis3 = String.valueOf(leveliss);
                        System.out.println("no. of total 13 " + totalbillis3);
                    }

                    Cursor cursor234 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax + "'", null);
                    if (cursor234.moveToFirst()) {
                        int leveliss = cursor234.getInt(0);
                        totalbillis4 = String.valueOf(leveliss);
                        System.out.println("no. of total 14 " + totalbillis4);
                    }

                    Cursor cursor235 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax + "'", null);
                    if (cursor235.moveToFirst()) {
                        int leveliss = cursor235.getInt(0);
                        totalbillis5 = String.valueOf(leveliss);
                        System.out.println("no. of total 15 " + totalbillis5);
                    }

                    float a2 = Float.parseFloat(totalbillis)+Float.parseFloat(totalbillis2)+Float.parseFloat(totalbillis3)+Float.parseFloat(totalbillis4)+Float.parseFloat(totalbillis5);

                    String tax_value = "";
                    float a = 0, b = 0;
                    Cursor cursor24 = db.rawQuery("SELECT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND (taxname = '" + tax + "' OR taxname2 = '" + tax + "' OR taxname3 = '" + tax + "' OR taxname4 = '" + tax + "' OR taxname5 = '" + tax + "')", null);
                    if (cursor24.moveToFirst()) {
                        tax_value = cursor24.getString(9);
                        System.out.println("tax value is 1 " + tax_value);

                        TextView tv1 = new TextView(Taxes_List_Sales1.this);
                        tv1.setText(tax_value);

                        if (tv1.getText().toString().equals("")) {

                        }else {
                            a = (a2 / 100) * Float.parseFloat(tax_value);
                            System.out.println("total tax value is 1 " + a);

                            b = a2 + a;
                            System.out.println("total sales value is 1 " + b);
                        }
                    }

                    int n2 = Math.round(a1);//no of items
                    int n4 = Math.round(a3);//no of qty
                    int n1 = Math.round(a2);//sale
                    int n3 = Math.round(a);//tax

                    TextView tax_n = (TextView) row.findViewById(R.id.tax);
                    tax_n.setText(tax);

                    TextView hsn_n = (TextView) row.findViewById(R.id.hsn);
                    hsn_n.setText(hsn_code);

                    TextView items_qty = (TextView) row.findViewById(R.id.items_qty);
                    String qa = String.valueOf(n2)+"/"+String.valueOf(n4);
                    items_qty.setText(qa);

                    TextView sale = (TextView) row.findViewById(R.id.sale);
                    sale.setText(String.valueOf(n1));

                    TextView tax_value1 = (TextView) row.findViewById(R.id.tax_value);
                    tax_value1.setText(String.valueOf(n3));

                    TextView total = (TextView) row.findViewById(R.id.total);
                    total.setText(String.valueOf(Math.round(b)));

                    tableLayout.addView(row);

                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TableRow lTableRow = ((TableRow) view);
                            // to get the TextView use getChildAt or findViewById
                            LinearLayout linearLayout = (LinearLayout) lTableRow.getChildAt(0);
                            TextView lTextView = (TextView) linearLayout.getChildAt(0);
                            //get the text from the TextView
                            final String vinNum = lTextView.getText().toString();

                            //Toast.maketext(Taxes_List_Sales1.this, "11 "+lTextView.getText().toString(), Toast.LENGTH_LONG).show();

                            final Dialog dialog = new Dialog(Taxes_List_Sales1.this, R.style.notitle);
                            dialog.setContentView(R.layout.tax_selected_listview);
                            dialog.show();

                            ImageView btncancel = (ImageView) dialog.findViewById(R.id.btncancel);
                            btncancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            TextView tax_name = (TextView) dialog.findViewById(R.id.tax_name);
                            tax_name.setText(vinNum);

                            final TableLayout tableLayout_selected = (TableLayout) dialog.findViewById(R.id.lytpedidooo);
                            tableLayout_selected.removeAllViews();

                            Cursor cursor1 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor1.moveToFirst()){
                                do {
                                    String i_name = cursor1.getString(1);
                                    String tax_v = cursor1.getString(9);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor1.moveToNext());
                            }

                            Cursor cursor12 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor12.moveToFirst()){
                                do {
                                    String i_name = cursor12.getString(1);
                                    String tax_v = cursor12.getString(36);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname2 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname2 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor12.moveToNext());
                            }

                            Cursor cursor13 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor13.moveToFirst()){
                                do {
                                    String i_name = cursor13.getString(1);
                                    String tax_v = cursor13.getString(38);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname3 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname3 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor13.moveToNext());
                            }

                            Cursor cursor14 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor14.moveToFirst()){
                                do {
                                    String i_name = cursor14.getString(1);
                                    String tax_v = cursor14.getString(40);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname4 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname4 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor14.moveToNext());
                            }

                            Cursor cursor15 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor15.moveToFirst()){
                                do {
                                    String i_name = cursor15.getString(1);
                                    String tax_v = cursor15.getString(42);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname5 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname5 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor15.moveToNext());
                            }

                        }
                    });
                }
            } while (cursor.moveToNext());
        }

        Cursor cursor2 = db.rawQuery("Select DISTINCT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 != '"+tax+"' GROUP BY taxname2 ", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (cursor2.moveToFirst()) {
            do {
                tax2 = cursor2.getString(35);
                System.out.println("tax value is 2 "+tax2);

                TextView tv = new TextView(Taxes_List_Sales1.this);
                tv.setText(tax2);

                if (tv.getText().toString().equals("0") || tv.getText().toString().equals("NONE") || tv.getText().toString().equals("None") || tv.getText().toString().equals("0.0")
                        || tv.getText().toString().equals("")) {

                }else {

                    TableRow row = (TableRow) LayoutInflater.from(Taxes_List_Sales1.this).inflate(R.layout.attrib_row, null);

                    String hsn_code = "";
                    Cursor cursor1 = db.rawQuery("SELECT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax2 + "' GROUP BY taxname2", null);
                    if (cursor1.moveToFirst()) {
                        hsn_code = cursor1.getString(51);
                        System.out.println("han_code is 2 " + hsn_code);
                    }

                    String items = "0", items2 = "0", items3 = "0", items4 = "0", items5 = "0";
                    Cursor cursor21 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax2 + "'", null);
                    if (cursor21.moveToFirst()) {
                        int leveliss = cursor21.getInt(0);
                        items = String.valueOf(leveliss);
                        System.out.println("no. of items 2 " + items);
                    }

                    Cursor cursor212 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax2 + "'", null);
                    if (cursor212.moveToFirst()) {
                        int leveliss = cursor212.getInt(0);
                        items2 = String.valueOf(leveliss);
                        System.out.println("no. of items 22 " + items2);
                    }

                    Cursor cursor213 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax2 + "'", null);
                    if (cursor213.moveToFirst()) {
                        int leveliss = cursor213.getInt(0);
                        items3 = String.valueOf(leveliss);
                        System.out.println("no. of items 23 " + items3);
                    }

                    Cursor cursor214 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax2 + "'", null);
                    if (cursor214.moveToFirst()) {
                        int leveliss = cursor214.getInt(0);
                        items4 = String.valueOf(leveliss);
                        System.out.println("no. of items 24 " + items4);
                    }

                    Cursor cursor215 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax2 + "'", null);
                    if (cursor215.moveToFirst()) {
                        int leveliss = cursor215.getInt(0);
                        items5 = String.valueOf(leveliss);
                        System.out.println("no. of items 25 " + items5);
                    }

                    float a1 = Float.parseFloat(items)+Float.parseFloat(items2)+Float.parseFloat(items3)+Float.parseFloat(items4)+Float.parseFloat(items5);


                    String qty = "0", qty2 = "0", qty3 = "0", qty4 = "0", qty5 = "0";
                    Cursor cursor22 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax2 + "'", null);
                    if (cursor22.moveToFirst()) {
                        int leveliss = cursor22.getInt(0);
                        qty = String.valueOf(leveliss);
                        System.out.println("no. of qty 2 " + qty);
                    }

                    Cursor cursor222 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax2 + "'", null);
                    if (cursor222.moveToFirst()) {
                        int leveliss = cursor222.getInt(0);
                        qty2 = String.valueOf(leveliss);
                        System.out.println("no. of qty 22 " + qty2);
                    }

                    Cursor cursor223 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax2 + "'", null);
                    if (cursor223.moveToFirst()) {
                        int leveliss = cursor223.getInt(0);
                        qty3 = String.valueOf(leveliss);
                        System.out.println("no. of qty 23 " + qty3);
                    }

                    Cursor cursor224 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax2 + "'", null);
                    if (cursor224.moveToFirst()) {
                        int leveliss = cursor224.getInt(0);
                        qty4 = String.valueOf(leveliss);
                        System.out.println("no. of qty 24 " + qty4);
                    }

                    Cursor cursor225 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax2 + "'", null);
                    if (cursor225.moveToFirst()) {
                        int leveliss = cursor225.getInt(0);
                        qty5 = String.valueOf(leveliss);
                        System.out.println("no. of qty 25 " + qty5);
                    }

                    float a3 = Float.parseFloat(qty)+Float.parseFloat(qty2)+Float.parseFloat(qty3)+Float.parseFloat(qty4)+Float.parseFloat(qty5);

                    String totalbillis = "0", totalbillis2 = "0", totalbillis3 = "0", totalbillis4 = "0", totalbillis5 = "0";
                    Cursor cursor23 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax2 + "'", null);
                    if (cursor23.moveToFirst()) {
                        int leveliss = cursor23.getInt(0);
                        totalbillis = String.valueOf(leveliss);
                        System.out.println("no. of total 22 " + totalbillis);
                    }

                    Cursor cursor232 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax2 + "'", null);
                    if (cursor232.moveToFirst()) {
                        int leveliss = cursor232.getInt(0);
                        totalbillis2 = String.valueOf(leveliss);
                        System.out.println("no. of total 23 " + totalbillis2);
                    }

                    Cursor cursor233 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax2 + "'", null);
                    if (cursor233.moveToFirst()) {
                        int leveliss = cursor233.getInt(0);
                        totalbillis3 = String.valueOf(leveliss);
                        System.out.println("no. of total 24 " + totalbillis3);
                    }

                    Cursor cursor234 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax2 + "'", null);
                    if (cursor234.moveToFirst()) {
                        int leveliss = cursor234.getInt(0);
                        totalbillis4 = String.valueOf(leveliss);
                        System.out.println("no. of total 25 " + totalbillis4);
                    }

                    Cursor cursor235 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax2 + "'", null);
                    if (cursor235.moveToFirst()) {
                        int leveliss = cursor235.getInt(0);
                        totalbillis5 = String.valueOf(leveliss);
                        System.out.println("no. of total 15 " + totalbillis5);
                    }

                    float a2 = Float.parseFloat(totalbillis)+Float.parseFloat(totalbillis2)+Float.parseFloat(totalbillis3)+Float.parseFloat(totalbillis4)+Float.parseFloat(totalbillis5);

                    String tax_value = "";
                    float a = 0, b = 0;
                    Cursor cursor24 = db.rawQuery("SELECT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND (taxname = '" + tax2 + "' OR taxname2 = '" + tax2 + "' OR taxname3 = '" + tax2 + "' OR taxname4 = '" + tax2 + "' OR taxname5 = '" + tax2 + "')", null);
                    if (cursor24.moveToFirst()) {
                        tax_value = cursor24.getString(36);
                        System.out.println("tax value is 2 " + tax_value);

                        TextView tv1 = new TextView(Taxes_List_Sales1.this);
                        tv1.setText(tax_value);

                        if (tv1.getText().toString().equals("")) {

                        }else {

                            a = (a2 / 100) * Float.parseFloat(tax_value);
                            System.out.println("total tax value is 2 " + a);

                            b = a2 + a;
                            System.out.println("total sales value is 2 " + b);
                        }
                    }

                    int n2 = Math.round(a1);//no of items
                    int n4 = Math.round(a3);//no of qty
                    int n1 = Math.round(a2);//sale
                    int n3 = Math.round(a);//tax

                    TextView tax_n = (TextView) row.findViewById(R.id.tax);
                    tax_n.setText(tax2);

                    TextView hsn_n = (TextView) row.findViewById(R.id.hsn);
                    hsn_n.setText(hsn_code);

                    TextView items_qty = (TextView) row.findViewById(R.id.items_qty);
                    String qa = String.valueOf(n2)+"/"+String.valueOf(n4);
                    items_qty.setText(qa);

                    TextView sale = (TextView) row.findViewById(R.id.sale);
                    sale.setText(String.valueOf(n1));

                    TextView tax_value1 = (TextView) row.findViewById(R.id.tax_value);
                    tax_value1.setText(String.valueOf(n3));

                    TextView total = (TextView) row.findViewById(R.id.total);
                    total.setText(String.valueOf(Math.round(b)));

                    tableLayout.addView(row);

                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TableRow lTableRow = ((TableRow) view);
                            // to get the TextView use getChildAt or findViewById
                            LinearLayout linearLayout = (LinearLayout) lTableRow.getChildAt(0);
                            TextView lTextView = (TextView) linearLayout.getChildAt(0);
                            //get the text from the TextView
                            final String vinNum = lTextView.getText().toString();

                            //Toast.maketext(Taxes_List_Sales1.this, "11 "+lTextView.getText().toString(), Toast.LENGTH_LONG).show();

                            final Dialog dialog = new Dialog(Taxes_List_Sales1.this, R.style.notitle);
                            dialog.setContentView(R.layout.tax_selected_listview);
                            dialog.show();

                            ImageView btncancel = (ImageView) dialog.findViewById(R.id.btncancel);
                            btncancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            TextView tax_name = (TextView) dialog.findViewById(R.id.tax_name);
                            tax_name.setText(vinNum);

                            final TableLayout tableLayout_selected = (TableLayout) dialog.findViewById(R.id.lytpedidooo);
                            tableLayout_selected.removeAllViews();

                            Cursor cursor1 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor1.moveToFirst()){
                                do {
                                    String i_name = cursor1.getString(1);
                                    String tax_v = cursor1.getString(9);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor1.moveToNext());
                            }

                            Cursor cursor12 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor12.moveToFirst()){
                                do {
                                    String i_name = cursor12.getString(1);
                                    String tax_v = cursor12.getString(36);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname2 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname2 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor12.moveToNext());
                            }

                            Cursor cursor13 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor13.moveToFirst()){
                                do {
                                    String i_name = cursor13.getString(1);
                                    String tax_v = cursor13.getString(38);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname3 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname3 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor13.moveToNext());
                            }

                            Cursor cursor14 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor14.moveToFirst()){
                                do {
                                    String i_name = cursor14.getString(1);
                                    String tax_v = cursor14.getString(40);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname4 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname4 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor14.moveToNext());
                            }

                            Cursor cursor15 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor15.moveToFirst()){
                                do {
                                    String i_name = cursor15.getString(1);
                                    String tax_v = cursor15.getString(42);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname5 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname5 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor15.moveToNext());
                            }

                        }
                    });

                }
            } while (cursor2.moveToNext());
        }


        Cursor cursor3 = db.rawQuery("Select DISTINCT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 != '"+tax+"' AND taxname3 != '"+tax2+"' GROUP BY taxname3", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (cursor3.moveToFirst()) {
            do {
                tax3 = cursor3.getString(37);
                System.out.println("tax value is 3 "+tax3);

                TextView tv = new TextView(Taxes_List_Sales1.this);
                tv.setText(tax3);

                if (tv.getText().toString().equals("0") || tv.getText().toString().equals("NONE") || tv.getText().toString().equals("None") || tv.getText().toString().equals("0.0")
                        || tv.getText().toString().equals("")) {

                }else {

                    TableRow row = (TableRow) LayoutInflater.from(Taxes_List_Sales1.this).inflate(R.layout.attrib_row, null);

                    String hsn_code = "";
                    Cursor cursor1 = db.rawQuery("SELECT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax3 + "' GROUP BY taxname3", null);
                    if (cursor1.moveToFirst()) {
                        hsn_code = cursor1.getString(52);
                        System.out.println("han_code is 3 " + hsn_code);
                    }

                    String items = "0", items2 = "0", items3 = "0", items4 = "0", items5 = "0";
                    Cursor cursor21 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax3 + "'", null);
                    if (cursor21.moveToFirst()) {
                        int leveliss = cursor21.getInt(0);
                        items = String.valueOf(leveliss);
                        System.out.println("no. of items 3 " + items);
                    }

                    Cursor cursor212 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax3 + "'", null);
                    if (cursor212.moveToFirst()) {
                        int leveliss = cursor212.getInt(0);
                        items2 = String.valueOf(leveliss);
                        System.out.println("no. of items 32 " + items2);
                    }

                    Cursor cursor213 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax3 + "'", null);
                    if (cursor213.moveToFirst()) {
                        int leveliss = cursor213.getInt(0);
                        items3 = String.valueOf(leveliss);
                        System.out.println("no. of items 33 " + items3);
                    }

                    Cursor cursor214 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax3 + "'", null);
                    if (cursor214.moveToFirst()) {
                        int leveliss = cursor214.getInt(0);
                        items4 = String.valueOf(leveliss);
                        System.out.println("no. of items 34 " + items4);
                    }

                    Cursor cursor215 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax3 + "'", null);
                    if (cursor215.moveToFirst()) {
                        int leveliss = cursor215.getInt(0);
                        items5 = String.valueOf(leveliss);
                        System.out.println("no. of items 35 " + items5);
                    }

                    float a1 = Float.parseFloat(items)+Float.parseFloat(items2)+Float.parseFloat(items3)+Float.parseFloat(items4)+Float.parseFloat(items5);


                    String qty = "0", qty2 = "0", qty3 = "0", qty4 = "0", qty5 = "0";
                    Cursor cursor22 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax3 + "'", null);
                    if (cursor22.moveToFirst()) {
                        int leveliss = cursor22.getInt(0);
                        qty = String.valueOf(leveliss);
                        System.out.println("no. of qty 3 " + qty);
                    }

                    Cursor cursor222 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax3 + "'", null);
                    if (cursor222.moveToFirst()) {
                        int leveliss = cursor222.getInt(0);
                        qty2 = String.valueOf(leveliss);
                        System.out.println("no. of qty 32 " + qty2);
                    }

                    Cursor cursor223 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax3 + "'", null);
                    if (cursor223.moveToFirst()) {
                        int leveliss = cursor223.getInt(0);
                        qty3 = String.valueOf(leveliss);
                        System.out.println("no. of qty 33 " + qty3);
                    }

                    Cursor cursor224 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax3 + "'", null);
                    if (cursor224.moveToFirst()) {
                        int leveliss = cursor224.getInt(0);
                        qty4 = String.valueOf(leveliss);
                        System.out.println("no. of qty 34 " + qty4);
                    }

                    Cursor cursor225 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax3 + "'", null);
                    if (cursor225.moveToFirst()) {
                        int leveliss = cursor225.getInt(0);
                        qty5 = String.valueOf(leveliss);
                        System.out.println("no. of qty 35 " + qty5);
                    }

                    float a3 = Float.parseFloat(qty)+Float.parseFloat(qty2)+Float.parseFloat(qty3)+Float.parseFloat(qty4)+Float.parseFloat(qty5);

                    String totalbillis = "0", totalbillis2 = "0", totalbillis3 = "0", totalbillis4 = "0", totalbillis5 = "0";
                    Cursor cursor23 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax3 + "'", null);
                    if (cursor23.moveToFirst()) {
                        int leveliss = cursor23.getInt(0);
                        totalbillis = String.valueOf(leveliss);
                        System.out.println("no. of total 3 " + totalbillis);
                    }

                    Cursor cursor232 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax3 + "'", null);
                    if (cursor232.moveToFirst()) {
                        int leveliss = cursor232.getInt(0);
                        totalbillis2 = String.valueOf(leveliss);
                        System.out.println("no. of total 32 " + totalbillis2);
                    }

                    Cursor cursor233 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax3 + "'", null);
                    if (cursor233.moveToFirst()) {
                        int leveliss = cursor233.getInt(0);
                        totalbillis3 = String.valueOf(leveliss);
                        System.out.println("no. of total 33 " + totalbillis3);
                    }

                    Cursor cursor234 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax3 + "'", null);
                    if (cursor234.moveToFirst()) {
                        int leveliss = cursor234.getInt(0);
                        totalbillis4 = String.valueOf(leveliss);
                        System.out.println("no. of total 34 " + totalbillis4);
                    }

                    Cursor cursor235 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax3 + "'", null);
                    if (cursor235.moveToFirst()) {
                        int leveliss = cursor235.getInt(0);
                        totalbillis5 = String.valueOf(leveliss);
                        System.out.println("no. of total 35 " + totalbillis5);
                    }

                    float a2 = Float.parseFloat(totalbillis)+Float.parseFloat(totalbillis2)+Float.parseFloat(totalbillis3)+Float.parseFloat(totalbillis4)+Float.parseFloat(totalbillis5);

                    String tax_value = "";
                    float a = 0, b = 0;
                    Cursor cursor24 = db.rawQuery("SELECT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND (taxname = '" + tax3 + "' OR taxname2 = '" + tax3 + "' OR taxname3 = '" + tax3 + "' OR taxname4 = '" + tax3 + "' OR taxname5 = '" + tax3 + "')", null);
                    if (cursor24.moveToFirst()) {
                        tax_value = cursor24.getString(38);
                        System.out.println("tax value is 3 " + tax_value);

                        TextView tv1 = new TextView(Taxes_List_Sales1.this);
                        tv1.setText(tax_value);

                        if (tv1.getText().toString().equals("")) {

                        }else {

                            a = (a2 / 100) * Float.parseFloat(tax_value);
                            System.out.println("total tax value is 3 " + a);

                            b = a2 + a;
                            System.out.println("total sales value is 3 " + b);
                        }
                    }

                    int n2 = Math.round(a1);//no of items
                    int n4 = Math.round(a3);//no of qty
                    int n1 = Math.round(a2);//sale
                    int n3 = Math.round(a);//tax

                    TextView tax_n = (TextView) row.findViewById(R.id.tax);
                    tax_n.setText(tax3);

                    TextView hsn_n = (TextView) row.findViewById(R.id.hsn);
                    hsn_n.setText(hsn_code);

                    TextView items_qty = (TextView) row.findViewById(R.id.items_qty);
                    String qa = String.valueOf(n2)+"/"+String.valueOf(n4);
                    items_qty.setText(qa);

                    TextView sale = (TextView) row.findViewById(R.id.sale);
                    sale.setText(String.valueOf(n1));

                    TextView tax_value1 = (TextView) row.findViewById(R.id.tax_value);
                    tax_value1.setText(String.valueOf(n3));

                    TextView total = (TextView) row.findViewById(R.id.total);
                    total.setText(String.valueOf(Math.round(b)));

                    tableLayout.addView(row);

                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TableRow lTableRow = ((TableRow) view);
                            // to get the TextView use getChildAt or findViewById
                            LinearLayout linearLayout = (LinearLayout) lTableRow.getChildAt(0);
                            TextView lTextView = (TextView) linearLayout.getChildAt(0);
                            //get the text from the TextView
                            final String vinNum = lTextView.getText().toString();

                            //Toast.maketext(Taxes_List_Sales1.this, "11 "+lTextView.getText().toString(), Toast.LENGTH_LONG).show();

                            final Dialog dialog = new Dialog(Taxes_List_Sales1.this, R.style.notitle);
                            dialog.setContentView(R.layout.tax_selected_listview);
                            dialog.show();

                            ImageView btncancel = (ImageView) dialog.findViewById(R.id.btncancel);
                            btncancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            TextView tax_name = (TextView) dialog.findViewById(R.id.tax_name);
                            tax_name.setText(vinNum);

                            final TableLayout tableLayout_selected = (TableLayout) dialog.findViewById(R.id.lytpedidooo);
                            tableLayout_selected.removeAllViews();

                            Cursor cursor1 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor1.moveToFirst()){
                                do {
                                    String i_name = cursor1.getString(1);
                                    String tax_v = cursor1.getString(9);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor1.moveToNext());
                            }

                            Cursor cursor12 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor12.moveToFirst()){
                                do {
                                    String i_name = cursor12.getString(1);
                                    String tax_v = cursor12.getString(36);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname2 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname2 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor12.moveToNext());
                            }

                            Cursor cursor13 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor13.moveToFirst()){
                                do {
                                    String i_name = cursor13.getString(1);
                                    String tax_v = cursor13.getString(38);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname3 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname3 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor13.moveToNext());
                            }

                            Cursor cursor14 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor14.moveToFirst()){
                                do {
                                    String i_name = cursor14.getString(1);
                                    String tax_v = cursor14.getString(40);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname4 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname4 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor14.moveToNext());
                            }

                            Cursor cursor15 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor15.moveToFirst()){
                                do {
                                    String i_name = cursor15.getString(1);
                                    String tax_v = cursor15.getString(42);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname5 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname5 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor15.moveToNext());
                            }

                        }
                    });

                }
            } while (cursor3.moveToNext());
        }

        Cursor cursor4 = db.rawQuery("Select DISTINCT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 != '"+tax+"' AND taxname4 != '"+tax2+"' AND taxname4 != '"+tax3+"' GROUP BY taxname4", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (cursor4.moveToFirst()) {
            do {
                tax4 = cursor4.getString(39);
                System.out.println("tax value is 4 "+tax4);

                TextView tv = new TextView(Taxes_List_Sales1.this);
                tv.setText(tax4);

                if (tv.getText().toString().equals("0") || tv.getText().toString().equals("NONE") || tv.getText().toString().equals("None") || tv.getText().toString().equals("0.0")
                        || tv.getText().toString().equals("")) {

                }else {

                    TableRow row = (TableRow) LayoutInflater.from(Taxes_List_Sales1.this).inflate(R.layout.attrib_row, null);

                    String hsn_code = "";
                    Cursor cursor1 = db.rawQuery("SELECT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax4 + "' GROUP BY taxname4", null);
                    if (cursor1.moveToFirst()) {
                        hsn_code = cursor1.getString(53);
                        System.out.println("han_code is 4 " + hsn_code);
                    }

                    String items = "0", items2 = "0", items3 = "0", items4 = "0", items5 = "0";
                    Cursor cursor21 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax4 + "'", null);
                    if (cursor21.moveToFirst()) {
                        int leveliss = cursor21.getInt(0);
                        items = String.valueOf(leveliss);
                        System.out.println("no. of items 4 " + items);
                    }

                    Cursor cursor212 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax4 + "'", null);
                    if (cursor212.moveToFirst()) {
                        int leveliss = cursor212.getInt(0);
                        items2 = String.valueOf(leveliss);
                        System.out.println("no. of items 42 " + items2);
                    }

                    Cursor cursor213 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax4 + "'", null);
                    if (cursor213.moveToFirst()) {
                        int leveliss = cursor213.getInt(0);
                        items3 = String.valueOf(leveliss);
                        System.out.println("no. of items 43 " + items3);
                    }

                    Cursor cursor214 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax4 + "'", null);
                    if (cursor214.moveToFirst()) {
                        int leveliss = cursor214.getInt(0);
                        items4 = String.valueOf(leveliss);
                        System.out.println("no. of items 44 " + items4);
                    }

                    Cursor cursor215 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax4 + "'", null);
                    if (cursor215.moveToFirst()) {
                        int leveliss = cursor215.getInt(0);
                        items5 = String.valueOf(leveliss);
                        System.out.println("no. of items 45 " + items5);
                    }

                    float a1 = Float.parseFloat(items)+Float.parseFloat(items2)+Float.parseFloat(items3)+Float.parseFloat(items4)+Float.parseFloat(items5);


                    String qty = "0", qty2 = "0", qty3 = "0", qty4 = "0", qty5 = "0";
                    Cursor cursor22 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax4 + "'", null);
                    if (cursor22.moveToFirst()) {
                        int leveliss = cursor22.getInt(0);
                        qty = String.valueOf(leveliss);
                        System.out.println("no. of qty 4 " + qty);
                    }

                    Cursor cursor222 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax4 + "'", null);
                    if (cursor222.moveToFirst()) {
                        int leveliss = cursor222.getInt(0);
                        qty2 = String.valueOf(leveliss);
                        System.out.println("no. of qty 42 " + qty2);
                    }

                    Cursor cursor223 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax4 + "'", null);
                    if (cursor223.moveToFirst()) {
                        int leveliss = cursor223.getInt(0);
                        qty3 = String.valueOf(leveliss);
                        System.out.println("no. of qty 43 " + qty3);
                    }

                    Cursor cursor224 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax4 + "'", null);
                    if (cursor224.moveToFirst()) {
                        int leveliss = cursor224.getInt(0);
                        qty4 = String.valueOf(leveliss);
                        System.out.println("no. of qty 44 " + qty4);
                    }

                    Cursor cursor225 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax4 + "'", null);
                    if (cursor225.moveToFirst()) {
                        int leveliss = cursor225.getInt(0);
                        qty5 = String.valueOf(leveliss);
                        System.out.println("no. of qty 45 " + qty5);
                    }

                    float a3 = Float.parseFloat(qty)+Float.parseFloat(qty2)+Float.parseFloat(qty3)+Float.parseFloat(qty4)+Float.parseFloat(qty5);

                    String totalbillis = "0", totalbillis2 = "0", totalbillis3 = "0", totalbillis4 = "0", totalbillis5 = "0";
                    Cursor cursor23 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax4 + "'", null);
                    if (cursor23.moveToFirst()) {
                        int leveliss = cursor23.getInt(0);
                        totalbillis = String.valueOf(leveliss);
                        System.out.println("no. of total 4 " + totalbillis);
                    }

                    Cursor cursor232 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax4 + "'", null);
                    if (cursor232.moveToFirst()) {
                        int leveliss = cursor232.getInt(0);
                        totalbillis2 = String.valueOf(leveliss);
                        System.out.println("no. of total 42 " + totalbillis2);
                    }

                    Cursor cursor233 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax4 + "'", null);
                    if (cursor233.moveToFirst()) {
                        int leveliss = cursor233.getInt(0);
                        totalbillis3 = String.valueOf(leveliss);
                        System.out.println("no. of total 43 " + totalbillis3);
                    }

                    Cursor cursor234 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax4 + "'", null);
                    if (cursor234.moveToFirst()) {
                        int leveliss = cursor234.getInt(0);
                        totalbillis4 = String.valueOf(leveliss);
                        System.out.println("no. of total 44 " + totalbillis4);
                    }

                    Cursor cursor235 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax4 + "'", null);
                    if (cursor235.moveToFirst()) {
                        int leveliss = cursor235.getInt(0);
                        totalbillis5 = String.valueOf(leveliss);
                        System.out.println("no. of total 45 " + totalbillis5);
                    }

                    float a2 = Float.parseFloat(totalbillis)+Float.parseFloat(totalbillis2)+Float.parseFloat(totalbillis3)+Float.parseFloat(totalbillis4)+Float.parseFloat(totalbillis5);

                    String tax_value = "";
                    float a = 0, b = 0;
                    Cursor cursor24 = db.rawQuery("SELECT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND (taxname = '" + tax4 + "' OR taxname2 = '" + tax4 + "' OR taxname3 = '" + tax4 + "' OR taxname4 = '" + tax4 + "' OR taxname5 = '" + tax4 + "')", null);
                    if (cursor24.moveToFirst()) {
                        tax_value = cursor24.getString(40);
                        System.out.println("tax value is 4 " + tax_value);

                        TextView tv1 = new TextView(Taxes_List_Sales1.this);
                        tv1.setText(tax_value);

                        if (tv1.getText().toString().equals("")) {

                        }else {

                            a = (a2 / 100) * Float.parseFloat(tax_value);
                            System.out.println("total tax value is 4 " + a);

                            b = a2 + a;
                            System.out.println("total sales value is 4 " + b);
                        }
                    }

                    int n2 = Math.round(a1);//no of items
                    int n4 = Math.round(a3);//no of qty
                    int n1 = Math.round(a2);//sale
                    int n3 = Math.round(a);//tax

                    TextView tax_n = (TextView) row.findViewById(R.id.tax);
                    tax_n.setText(tax4);

                    TextView hsn_n = (TextView) row.findViewById(R.id.hsn);
                    hsn_n.setText(hsn_code);

                    TextView items_qty = (TextView) row.findViewById(R.id.items_qty);
                    String qa = String.valueOf(n2)+"/"+String.valueOf(n4);
                    items_qty.setText(qa);

                    TextView sale = (TextView) row.findViewById(R.id.sale);
                    sale.setText(String.valueOf(n1));

                    TextView tax_value1 = (TextView) row.findViewById(R.id.tax_value);
                    tax_value1.setText(String.valueOf(n3));

                    TextView total = (TextView) row.findViewById(R.id.total);
                    total.setText(String.valueOf(Math.round(b)));

                    tableLayout.addView(row);

                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TableRow lTableRow = ((TableRow) view);
                            // to get the TextView use getChildAt or findViewById
                            LinearLayout linearLayout = (LinearLayout) lTableRow.getChildAt(0);
                            TextView lTextView = (TextView) linearLayout.getChildAt(0);
                            //get the text from the TextView
                            final String vinNum = lTextView.getText().toString();

                            //Toast.maketext(Taxes_List_Sales1.this, "11 "+lTextView.getText().toString(), Toast.LENGTH_LONG).show();

                            final Dialog dialog = new Dialog(Taxes_List_Sales1.this, R.style.notitle);
                            dialog.setContentView(R.layout.tax_selected_listview);
                            dialog.show();

                            ImageView btncancel = (ImageView) dialog.findViewById(R.id.btncancel);
                            btncancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            TextView tax_name = (TextView) dialog.findViewById(R.id.tax_name);
                            tax_name.setText(vinNum);

                            final TableLayout tableLayout_selected = (TableLayout) dialog.findViewById(R.id.lytpedidooo);
                            tableLayout_selected.removeAllViews();

                            Cursor cursor1 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor1.moveToFirst()){
                                do {
                                    String i_name = cursor1.getString(1);
                                    String tax_v = cursor1.getString(9);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor1.moveToNext());
                            }

                            Cursor cursor12 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor12.moveToFirst()){
                                do {
                                    String i_name = cursor12.getString(1);
                                    String tax_v = cursor12.getString(36);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname2 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname2 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor12.moveToNext());
                            }

                            Cursor cursor13 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor13.moveToFirst()){
                                do {
                                    String i_name = cursor13.getString(1);
                                    String tax_v = cursor13.getString(38);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname3 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname3 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor13.moveToNext());
                            }

                            Cursor cursor14 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor14.moveToFirst()){
                                do {
                                    String i_name = cursor14.getString(1);
                                    String tax_v = cursor14.getString(40);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname4 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname4 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor14.moveToNext());
                            }

                            Cursor cursor15 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor15.moveToFirst()){
                                do {
                                    String i_name = cursor15.getString(1);
                                    String tax_v = cursor15.getString(42);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname5 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname5 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor15.moveToNext());
                            }

                        }
                    });
                }
            } while (cursor4.moveToNext());
        }


        Cursor cursor5 = db.rawQuery("Select DISTINCT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 != '"+tax+"' AND taxname4 != '"+tax2+"' AND taxname4 != '"+tax3+"' AND taxname4 != '"+tax4+"' GROUP BY taxname4", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (cursor5.moveToFirst()) {
            do {
                tax5 = cursor5.getString(41);
                System.out.println("tax value is 5 "+tax5);

                TextView tv = new TextView(Taxes_List_Sales1.this);
                tv.setText(tax5);

                if (tv.getText().toString().equals("0") || tv.getText().toString().equals("NONE") || tv.getText().toString().equals("None") || tv.getText().toString().equals("0.0")
                        || tv.getText().toString().equals("")) {

                }else {

                    TableRow row = (TableRow) LayoutInflater.from(Taxes_List_Sales1.this).inflate(R.layout.attrib_row, null);

                    String hsn_code = "";
                    Cursor cursor1 = db.rawQuery("SELECT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax5 + "' GROUP BY taxname4", null);
                    if (cursor1.moveToFirst()) {
                        hsn_code = cursor1.getString(54);
                        System.out.println("han_code is 5 " + hsn_code);
                    }

                    String items = "0", items2 = "0", items3 = "0", items4 = "0", items5 = "0";
                    Cursor cursor21 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax5 + "'", null);
                    if (cursor21.moveToFirst()) {
                        int leveliss = cursor21.getInt(0);
                        items = String.valueOf(leveliss);
                        System.out.println("no. of items 5 " + items);
                    }

                    Cursor cursor212 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax5 + "'", null);
                    if (cursor212.moveToFirst()) {
                        int leveliss = cursor212.getInt(0);
                        items2 = String.valueOf(leveliss);
                        System.out.println("no. of items 52 " + items2);
                    }

                    Cursor cursor213 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax5 + "'", null);
                    if (cursor213.moveToFirst()) {
                        int leveliss = cursor213.getInt(0);
                        items3 = String.valueOf(leveliss);
                        System.out.println("no. of items 53 " + items3);
                    }

                    Cursor cursor214 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax5 + "'", null);
                    if (cursor214.moveToFirst()) {
                        int leveliss = cursor214.getInt(0);
                        items4 = String.valueOf(leveliss);
                        System.out.println("no. of items 54 " + items4);
                    }

                    Cursor cursor215 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax5 + "'", null);
                    if (cursor215.moveToFirst()) {
                        int leveliss = cursor215.getInt(0);
                        items5 = String.valueOf(leveliss);
                        System.out.println("no. of items 55 " + items5);
                    }

                    float a1 = Float.parseFloat(items)+Float.parseFloat(items2)+Float.parseFloat(items3)+Float.parseFloat(items4)+Float.parseFloat(items5);


                    String qty = "0", qty2 = "0", qty3 = "0", qty4 = "0", qty5 = "0";
                    Cursor cursor22 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax5 + "'", null);
                    if (cursor22.moveToFirst()) {
                        int leveliss = cursor22.getInt(0);
                        qty = String.valueOf(leveliss);
                        System.out.println("no. of qty 5 " + qty);
                    }

                    Cursor cursor222 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax5 + "'", null);
                    if (cursor222.moveToFirst()) {
                        int leveliss = cursor222.getInt(0);
                        qty2 = String.valueOf(leveliss);
                        System.out.println("no. of qty 52 " + qty2);
                    }

                    Cursor cursor223 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax5 + "'", null);
                    if (cursor223.moveToFirst()) {
                        int leveliss = cursor223.getInt(0);
                        qty3 = String.valueOf(leveliss);
                        System.out.println("no. of qty 53 " + qty3);
                    }

                    Cursor cursor224 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax5 + "'", null);
                    if (cursor224.moveToFirst()) {
                        int leveliss = cursor224.getInt(0);
                        qty4 = String.valueOf(leveliss);
                        System.out.println("no. of qty 54 " + qty4);
                    }

                    Cursor cursor225 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax5 + "'", null);
                    if (cursor225.moveToFirst()) {
                        int leveliss = cursor225.getInt(0);
                        qty5 = String.valueOf(leveliss);
                        System.out.println("no. of qty 55 " + qty5);
                    }

                    float a3 = Float.parseFloat(qty)+Float.parseFloat(qty2)+Float.parseFloat(qty3)+Float.parseFloat(qty4)+Float.parseFloat(qty5);

                    String totalbillis = "0", totalbillis2 = "0", totalbillis3 = "0", totalbillis4 = "0", totalbillis5 = "0";
                    Cursor cursor23 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax5 + "'", null);
                    if (cursor23.moveToFirst()) {
                        int leveliss = cursor23.getInt(0);
                        totalbillis = String.valueOf(leveliss);
                        System.out.println("no. of total 5 " + totalbillis);
                    }

                    Cursor cursor232 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax5 + "'", null);
                    if (cursor232.moveToFirst()) {
                        int leveliss = cursor232.getInt(0);
                        totalbillis2 = String.valueOf(leveliss);
                        System.out.println("no. of total 52 " + totalbillis2);
                    }

                    Cursor cursor233 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax5 + "'", null);
                    if (cursor233.moveToFirst()) {
                        int leveliss = cursor233.getInt(0);
                        totalbillis3 = String.valueOf(leveliss);
                        System.out.println("no. of total 53 " + totalbillis3);
                    }

                    Cursor cursor234 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax5 + "'", null);
                    if (cursor234.moveToFirst()) {
                        int leveliss = cursor234.getInt(0);
                        totalbillis4 = String.valueOf(leveliss);
                        System.out.println("no. of total 54 " + totalbillis4);
                    }

                    Cursor cursor235 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax5 + "'", null);
                    if (cursor235.moveToFirst()) {
                        int leveliss = cursor235.getInt(0);
                        totalbillis5 = String.valueOf(leveliss);
                        System.out.println("no. of total 55 " + totalbillis5);
                    }

                    float a2 = Float.parseFloat(totalbillis)+Float.parseFloat(totalbillis2)+Float.parseFloat(totalbillis3)+Float.parseFloat(totalbillis4)+Float.parseFloat(totalbillis5);

                    String tax_value = "";
                    float a = 0, b = 0;
                    Cursor cursor24 = db.rawQuery("SELECT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND (taxname = '" + tax5 + "' OR taxname2 = '" + tax5 + "' OR taxname3 = '" + tax5 + "' OR taxname4 = '" + tax5 + "' OR taxname5 = '" + tax5 + "')", null);
                    if (cursor24.moveToFirst()) {
                        tax_value = cursor24.getString(42);
                        System.out.println("tax value is 5 " + tax_value);

                        TextView tv1 = new TextView(Taxes_List_Sales1.this);
                        tv1.setText(tax_value);

                        if (tv1.getText().toString().equals("")) {

                        }else {

                            a = (a2 / 100) * Float.parseFloat(tax_value);
                            System.out.println("total tax value is 5 " + a);

                            b = a2 + a;
                            System.out.println("total sales value is 5 " + b);
                        }
                    }

                    int n2 = Math.round(a1);//no of items
                    int n4 = Math.round(a3);//no of qty
                    int n1 = Math.round(a2);//sale
                    int n3 = Math.round(a);//tax

                    TextView tax_n = (TextView) row.findViewById(R.id.tax);
                    tax_n.setText(tax5);

                    TextView hsn_n = (TextView) row.findViewById(R.id.hsn);
                    hsn_n.setText(hsn_code);

                    TextView items_qty = (TextView) row.findViewById(R.id.items_qty);
                    String qa = String.valueOf(n2)+"/"+String.valueOf(n4);
                    items_qty.setText(qa);

                    TextView sale = (TextView) row.findViewById(R.id.sale);
                    sale.setText(String.valueOf(n1));

                    TextView tax_value1 = (TextView) row.findViewById(R.id.tax_value);
                    tax_value1.setText(String.valueOf(n3));

                    TextView total = (TextView) row.findViewById(R.id.total);
                    total.setText(String.valueOf(Math.round(b)));

                    tableLayout.addView(row);

                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TableRow lTableRow = ((TableRow) view);
                            // to get the TextView use getChildAt or findViewById
                            LinearLayout linearLayout = (LinearLayout) lTableRow.getChildAt(0);
                            TextView lTextView = (TextView) linearLayout.getChildAt(0);
                            //get the text from the TextView
                            final String vinNum = lTextView.getText().toString();

                            //Toast.maketext(Taxes_List_Sales1.this, "11 "+lTextView.getText().toString(), Toast.LENGTH_LONG).show();

                            final Dialog dialog = new Dialog(Taxes_List_Sales1.this, R.style.notitle);
                            dialog.setContentView(R.layout.tax_selected_listview);
                            dialog.show();

                            ImageView btncancel = (ImageView) dialog.findViewById(R.id.btncancel);
                            btncancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            TextView tax_name = (TextView) dialog.findViewById(R.id.tax_name);
                            tax_name.setText(vinNum);

                            final TableLayout tableLayout_selected = (TableLayout) dialog.findViewById(R.id.lytpedidooo);
                            tableLayout_selected.removeAllViews();

                            Cursor cursor1 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor1.moveToFirst()){
                                do {
                                    String i_name = cursor1.getString(1);
                                    String tax_v = cursor1.getString(9);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor1.moveToNext());
                            }

                            Cursor cursor12 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor12.moveToFirst()){
                                do {
                                    String i_name = cursor12.getString(1);
                                    String tax_v = cursor12.getString(36);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname2 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname2 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor12.moveToNext());
                            }

                            Cursor cursor13 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor13.moveToFirst()){
                                do {
                                    String i_name = cursor13.getString(1);
                                    String tax_v = cursor13.getString(38);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname3 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname3 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor13.moveToNext());
                            }

                            Cursor cursor14 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor14.moveToFirst()){
                                do {
                                    String i_name = cursor14.getString(1);
                                    String tax_v = cursor14.getString(40);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname4 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname4 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor14.moveToNext());
                            }

                            Cursor cursor15 = db.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '"+vinNum+"' GROUP BY itemname", null);
                            if (cursor15.moveToFirst()){
                                do {
                                    String i_name = cursor15.getString(1);
                                    String tax_v = cursor15.getString(42);

                                    //Toast.maketext(Taxes_List_Sales1.this, "i_name "+i_name, Toast.LENGTH_LONG).show();

                                    int sum_qty = 0, sum_tot = 0;
                                    Cursor cursor2 = db.rawQuery("SELECT SUM(quantity) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname5 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor2.moveToFirst()){
                                        sum_qty = cursor2.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "i_name "+sum_qty, Toast.LENGTH_LONG).show();
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT SUM(old_total) FROM All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND itemname = '"+i_name+"' AND taxname5 = '"+vinNum+"' GROUP BY itemname", null);
                                    if (cursor3.moveToFirst()){
                                        sum_tot = cursor3.getInt(0);
                                        //Toast.maketext(Taxes_List_Sales1.this, "total "+sum_tot, Toast.LENGTH_LONG).show();
                                    }

                                    float a = (Float.parseFloat(String.valueOf(sum_tot))/100)*Float.parseFloat(tax_v);

                                    final TableRow row_1 = new TableRow(Taxes_List_Sales1.this);
                                    row_1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                                    row_1.setPadding(0, 0, 0, 0);

                                    final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                                    tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv1.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv1.setTextColor(Color.parseColor("#000000"));
                                    tv1.setText(String.valueOf(sum_qty));
                                    row_1.addView(tv1);

                                    final TextView tv11 = new TextView(Taxes_List_Sales1.this);
                                    tv11.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                                    tv11.setGravity(Gravity.TOP | Gravity.CENTER);
                                    tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv11.setTextColor(Color.parseColor("#000000"));
                                    tv11.setText(" x ");
                                    row_1.addView(tv11);

                                    final TextView tv12 = new TextView(Taxes_List_Sales1.this);
                                    tv12.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.6f));
                                    tv12.setGravity(Gravity.LEFT | Gravity.CENTER);
                                    tv12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv12.setTextColor(Color.parseColor("#000000"));
                                    tv12.setText(i_name);
                                    row_1.addView(tv12);

                                    final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                                    tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                                    tv2.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER);
                                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                    tv2.setTextColor(Color.parseColor("#000000"));
                                    tv2.setText(R.string.Rs);
                                    tv2.append(String.format("%.2f", a));
                                    row_1.addView(tv2);

                                    tableLayout_selected.addView(row_1);

                                }while (cursor15.moveToNext());
                            }

                        }
                    });
                }
            } while (cursor5.moveToNext());
        }


        ImageButton action_export = (ImageButton) findViewById(R.id.action_export);
        action_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sdff2 = new SimpleDateFormat("ddMMMyy", Locale.US);
                currentDateandTimee1 = sdff2.format(new Date());

                Date dt = new Date();
                sdff1 = new SimpleDateFormat("hhmmssaa",Locale.US);
                timee1 = sdff1.format(dt);

                ExportDatabaseCSVTask task=new ExportDatabaseCSVTask();
                task.execute();
            }
        });

        ImageButton action_exportmail = (ImageButton) findViewById(R.id.action_exportmail);
        action_exportmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "www.intuitionsoftwares.com";
                String msg = "" + Uri.parse(url);

                Cursor cursore = db1.rawQuery("SELECT * FROM Email_setup", null);
                if (cursore.moveToFirst()){
                    Cursor cursoree = db1.rawQuery("SELECT * FROM Email_recipient", null);
                    if (cursoree.moveToFirst()){
                        //both are there
                        Cursor cursoor = db1.rawQuery("SELECT * FROM Email_setup", null);
                        if (cursoor.moveToFirst()) {
                            String un = cursoor.getString(1);
                            String pwd = cursoor.getString(2);
                            String em_ca = cursoor.getString(3);
                            if (em_ca.equals("Gmail")) {
                                getResultsFromApi();
                                new MakeRequestTask(mCredential).execute();
                            }else {
                                if (em_ca.equals("Yahoo")){
//                                        //Toast.maketext(getActivity(), "yahoo "+un, Toast.LENGTH_LONG).show();
                                    Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
                                    if (cursor1.moveToFirst()) {
                                        do {
                                            String unn = cursor1.getString(3);
                                            String toEmails = unn;
                                            toEmailList = Arrays.asList(toEmails
                                                    .split("\\s*,\\s*"));
                                            new SendMailTask_Yahoo_attachment_Taxlist(Taxes_List_Sales1.this).execute(un,
                                                    pwd, toEmailList, companynameis, msg, currentDateandTimee1, timee1);
                                        } while (cursor1.moveToNext());
                                    }
                                    cursor1.close();


                                }else {
                                    if (em_ca.equals("Hotmail")){
//                                            //Toast.maketext(getActivity(), "Hotmail and Outlook "+un, Toast.LENGTH_LONG).show();
                                        Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
                                        if (cursor1.moveToFirst()) {
                                            do {
                                                String unn = cursor1.getString(3);
                                                String toEmails = unn;
                                                toEmailList = Arrays.asList(toEmails
                                                        .split("\\s*,\\s*"));
                                                new SendMailTask_Hotmail_Outlook_attachment_Taxlist(Taxes_List_Sales1.this).execute(un,
                                                        pwd, toEmailList, companynameis, msg, currentDateandTimee1, timee1);
                                            } while (cursor1.moveToNext());
                                        }
                                        cursor1.close();
                                    }else {
                                        if (em_ca.equals("Office365")) {
//                                                //Toast.maketext(getActivity(), "office 365 " + un, Toast.LENGTH_LONG).show();
                                            Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
                                            if (cursor1.moveToFirst()) {
                                                do {
                                                    String unn = cursor1.getString(3);
                                                    String toEmails = unn;
                                                    toEmailList = Arrays.asList(toEmails
                                                            .split("\\s*,\\s*"));
                                                    new SendMailTask_Office365_attachment_Taxlist(Taxes_List_Sales1.this).execute(un,
                                                            pwd, toEmailList, companynameis, msg, currentDateandTimee1, timee1);
                                                } while (cursor1.moveToNext());
                                            }
                                            cursor1.close();
                                        }
                                    }
                                }
                            }
                        }
                        cursoor.close();
                    }else {
                        //only recipient not there
                        final Dialog dialoge = new Dialog(Taxes_List_Sales1.this, R.style.timepicker_date_dialog);
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
                                Intent intent = new Intent(Taxes_List_Sales1.this, EmailSetup_Recipients.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
//                                                                getActivity().finish();
                                dialoge.dismiss();
                            }
                        });

                        Button gotosettings1 = (Button) dialoge.findViewById(R.id.gotosettings1);
                        gotosettings1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Taxes_List_Sales1.this, EmailSetup_Recipients.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
//                                                                getActivity().finish();
                                dialoge.dismiss();
                            }
                        });


                    }
                    cursoree.close();
                }else {
                    Cursor cursoree = db1.rawQuery("SELECT * FROM Email_recipient", null);
                    if (cursoree.moveToFirst()){
                        //only sender not there
                        final Dialog dialoge = new Dialog(Taxes_List_Sales1.this, R.style.timepicker_date_dialog);
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
                                Intent intent = new Intent(Taxes_List_Sales1.this, EmailSetup.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
//                                                                getActivity().finish();
                                dialoge.dismiss();
                            }
                        });

                        Button gotosettings1 = (Button) dialoge.findViewById(R.id.gotosettings1);
                        gotosettings1.setVisibility(View.GONE);
                        gotosettings1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Taxes_List_Sales1.this, EmailSetup.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
//                                                                getActivity().finish();
                                dialoge.dismiss();
                            }
                        });

                    }else {
                        //both recipient and sender not there
                        final Dialog dialoge = new Dialog(Taxes_List_Sales1.this, R.style.timepicker_date_dialog);
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
                                Intent intent = new Intent(Taxes_List_Sales1.this, EmailSetup.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
//                                                                getActivity().finish();
                                dialoge.dismiss();
                            }
                        });

                        Button gotosettings1 = (Button) dialoge.findViewById(R.id.gotosettings1);
                        gotosettings1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Taxes_List_Sales1.this, EmailSetup_Recipients.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
//                                                                getActivity().finish();
                                dialoge.dismiss();
                            }
                        });

                    }
                    cursoree.close();
                }
                cursore.close();
            }
        });


    }


    private class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(Taxes_List_Sales1.this, R.style.timepicker_date_dialog);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage(getString(R.string.setmessage13));
            this.dialog.show();

        }

        protected Boolean doInBackground(final String... args){

//            File exportDir = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_reports/IVEPOS_Taxwise_report");
            File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_reports/IVEPOS_Taxwise_report");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            File file = new File(exportDir, "IvePOS_Taxwise_report"+currentDateandTimee1+"_"+timee1+".csv");
            try {

                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

                String arrStr1[] ={"Tax", "HSN", "No. of Items", "No. of Qty.", "Sale", "Tax", "Total"};
                csvWrite.writeNext(arrStr1);

                db = openOrCreateDatabase("mydb_Salesdata",
                        Context.MODE_PRIVATE, null);


                final TableLayout tableLayout = new TableLayout(Taxes_List_Sales1.this);
                tableLayout.removeAllViews();


                String tax = "", tax2 = "", tax3 = "", tax4 = "", tax5 = "";
                Cursor cursor = db.rawQuery("Select * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' GROUP BY taxname", null);//replace to cursor = dbHelper.fetchAllHotels();
                if (cursor.moveToFirst()) {
                    do {

                        final TableRow row = new TableRow(Taxes_List_Sales1.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                        row.setPadding(5, 5, 0, 0);

                        final TableRow row1 = new TableRow(Taxes_List_Sales1.this);
                        row1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                        row1.setPadding(5, 0, 0, 0);

                        final TableRow row2 = new TableRow(Taxes_List_Sales1.this);
                        row2.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                        row2.setPadding(5, 0, 0, 0);

                        final TableRow row3 = new TableRow(Taxes_List_Sales1.this);
                        row3.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                        row3.setPadding(5, 0, 0, 5);

                        tax = cursor.getString(10);
                        System.out.println("tax value is 1 "+tax);

                        TextView tv = new TextView(Taxes_List_Sales1.this);
                        tv.setText(tax);

                        if (tv.getText().toString().equals("0") || tv.getText().toString().equals("NONE") || tv.getText().toString().equals("None") || tv.getText().toString().equals("0.0")
                                || tv.getText().toString().equals("")) {

                        }else {
                            String hsn_code = "";
                            Cursor cursor1 = db.rawQuery("SELECT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax + "' GROUP BY taxname", null);
                            if (cursor1.moveToFirst()) {
                                hsn_code = cursor1.getString(34);
                                System.out.println("han_code is 1 " + hsn_code);
                            }

//                    String items = "";
//                    Cursor cursor21 = db.rawQuery("SELECT COUNT(itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax + "' OR taxname2 = '" + tax + "' OR taxname3 = '" + tax + "' OR taxname4 = '" + tax + "' OR taxname5 = '" + tax + "' GROUP BY itemname", null);
//                    if (cursor21.moveToFirst()) {
//                        int leveliss = cursor21.getInt(0);
//                        items = String.valueOf(leveliss);
//                        System.out.println("no. of items 1 " + items);
//                    }

                            String items = "0", items2 = "0", items3 = "0", items4 = "0", items5 = "0";
                            Cursor cursor21 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax + "'", null);
                            if (cursor21.moveToFirst()) {
                                int leveliss = cursor21.getInt(0);
                                items = String.valueOf(leveliss);
                                System.out.println("no. of items 1 " + items);
                            }

                            Cursor cursor212 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax + "'", null);
                            if (cursor212.moveToFirst()) {
                                int leveliss = cursor212.getInt(0);
                                items2 = String.valueOf(leveliss);
                                System.out.println("no. of items 12 " + items2);
                            }

                            Cursor cursor213 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax + "'", null);
                            if (cursor213.moveToFirst()) {
                                int leveliss = cursor213.getInt(0);
                                items3 = String.valueOf(leveliss);
                                System.out.println("no. of items 13 " + items3);
                            }

                            Cursor cursor214 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax + "'", null);
                            if (cursor214.moveToFirst()) {
                                int leveliss = cursor214.getInt(0);
                                items4 = String.valueOf(leveliss);
                                System.out.println("no. of items 14 " + items4);
                            }

                            Cursor cursor215 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax + "'", null);
                            if (cursor215.moveToFirst()) {
                                int leveliss = cursor215.getInt(0);
                                items5 = String.valueOf(leveliss);
                                System.out.println("no. of items 15 " + items5);
                            }

                            float a1 = Float.parseFloat(items)+Float.parseFloat(items2)+Float.parseFloat(items3)+Float.parseFloat(items4)+Float.parseFloat(items5);

                            String qty = "0", qty2 = "0", qty3 = "0", qty4 = "0", qty5 = "0";
                            Cursor cursor22 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax + "'", null);
                            if (cursor22.moveToFirst()) {
                                int leveliss = cursor22.getInt(0);
                                qty = String.valueOf(leveliss);
                                System.out.println("no. of qty 1 " + qty);
                            }

                            Cursor cursor222 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax + "'", null);
                            if (cursor222.moveToFirst()) {
                                int leveliss = cursor222.getInt(0);
                                qty2 = String.valueOf(leveliss);
                                System.out.println("no. of qty 12 " + qty2);
                            }

                            Cursor cursor223 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax + "'", null);
                            if (cursor223.moveToFirst()) {
                                int leveliss = cursor223.getInt(0);
                                qty3 = String.valueOf(leveliss);
                                System.out.println("no. of qty 13 " + qty3);
                            }

                            Cursor cursor224 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax + "'", null);
                            if (cursor224.moveToFirst()) {
                                int leveliss = cursor224.getInt(0);
                                qty4 = String.valueOf(leveliss);
                                System.out.println("no. of qty 14 " + qty4);
                            }

                            Cursor cursor225 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax + "'", null);
                            if (cursor225.moveToFirst()) {
                                int leveliss = cursor225.getInt(0);
                                qty5 = String.valueOf(leveliss);
                                System.out.println("no. of qty 15 " + qty5);
                            }

                            float a3 = Float.parseFloat(qty)+Float.parseFloat(qty2)+Float.parseFloat(qty3)+Float.parseFloat(qty4)+Float.parseFloat(qty5);

                            String totalbillis = "0", totalbillis2 = "0", totalbillis3 = "0", totalbillis4 = "0", totalbillis5 = "0";
                            Cursor cursor23 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax + "'", null);
                            if (cursor23.moveToFirst()) {
                                int leveliss = cursor23.getInt(0);
                                totalbillis = String.valueOf(leveliss);
                                System.out.println("no. of total 11 " + totalbillis);
                            }

                            Cursor cursor232 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax + "'", null);
                            if (cursor232.moveToFirst()) {
                                int leveliss = cursor232.getInt(0);
                                totalbillis2 = String.valueOf(leveliss);
                                System.out.println("no. of total 12 " + totalbillis2);
                            }

                            Cursor cursor233 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax + "'", null);
                            if (cursor233.moveToFirst()) {
                                int leveliss = cursor233.getInt(0);
                                totalbillis3 = String.valueOf(leveliss);
                                System.out.println("no. of total 13 " + totalbillis3);
                            }

                            Cursor cursor234 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax + "'", null);
                            if (cursor234.moveToFirst()) {
                                int leveliss = cursor234.getInt(0);
                                totalbillis4 = String.valueOf(leveliss);
                                System.out.println("no. of total 14 " + totalbillis4);
                            }

                            Cursor cursor235 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax + "'", null);
                            if (cursor235.moveToFirst()) {
                                int leveliss = cursor235.getInt(0);
                                totalbillis5 = String.valueOf(leveliss);
                                System.out.println("no. of total 15 " + totalbillis5);
                            }

                            float a2 = Float.parseFloat(totalbillis)+Float.parseFloat(totalbillis2)+Float.parseFloat(totalbillis3)+Float.parseFloat(totalbillis4)+Float.parseFloat(totalbillis5);

                            String tax_value = "";
                            float a = 0, b = 0;
                            Cursor cursor24 = db.rawQuery("SELECT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND (taxname = '" + tax + "' OR taxname2 = '" + tax + "' OR taxname3 = '" + tax + "' OR taxname4 = '" + tax + "' OR taxname5 = '" + tax + "')", null);
                            if (cursor24.moveToFirst()) {
                                tax_value = cursor24.getString(9);
                                System.out.println("tax value is 1 " + tax_value);

                                a = (a2 / 100) * Float.parseFloat(tax_value);
                                System.out.println("total tax value is 1 " + a);

                                b = a2+a;
                                System.out.println("total sales value is 1 " + b);
                            }

                            final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                            tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv1.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv1.setTextColor(Color.parseColor("#000000"));
                            tv1.setText(tax);
                            row.addView(tv1);

                            final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                            tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv2.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv2.setTextColor(Color.parseColor("#000000"));
                            int n2 = Math.round(a1);
                            tv2.setText(String.valueOf(n2));
                            row.addView(tv2);

                            final TextView tv3 = new TextView(Taxes_List_Sales1.this);
                            tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv3.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv3.setTextColor(Color.parseColor("#000000"));
                            int n1 = Math.round(a2);
                            tv3.setText(String.valueOf(n1));
                            row.addView(tv3);

                            final TextView tv4 = new TextView(Taxes_List_Sales1.this);
                            tv4.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv4.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv4.setTextColor(Color.parseColor("#000000"));
                            tv4.setText(hsn_code);
                            row1.addView(tv4);

                            final TextView tv5 = new TextView(Taxes_List_Sales1.this);
                            tv5.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv5.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv5.setTextColor(Color.parseColor("#000000"));
                            int n4 = Math.round(a3);
                            tv5.setText(String.valueOf(n4));
                            row1.addView(tv5);

                            final TextView tv6 = new TextView(Taxes_List_Sales1.this);
                            tv6.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv6.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv6.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv6.setTextColor(Color.parseColor("#000000"));
                            int n3 = Math.round(a);
                            tv6.setText(String.valueOf(n3));
                            row1.addView(tv6);

                            final TextView tv7 = new TextView(Taxes_List_Sales1.this);
                            tv7.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv7.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv7.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv7.setTextColor(Color.parseColor("#000000"));
                            tv7.setText("");
                            row2.addView(tv7);

                            final TextView tv8 = new TextView(Taxes_List_Sales1.this);
                            tv8.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv8.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv8.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv8.setTextColor(Color.parseColor("#000000"));
                            tv8.setText("");
                            row2.addView(tv8);

                            final TextView tv9 = new TextView(Taxes_List_Sales1.this);
                            tv9.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv9.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv9.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv9.setTextColor(Color.parseColor("#000000"));
                            tv9.setText(String.valueOf(Math.round(b)));
                            row2.addView(tv9);

                            String arrStr[] ={tax, hsn_code, String.valueOf(n2), String.valueOf(n4), String.valueOf(n1), String.valueOf(n3), String.valueOf(Math.round(b))};
                            csvWrite.writeNext(arrStr);

                        }

//                ImageView imageView = new ImageView(Taxes_List_Sales1.this);
//                imageView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 3.0f));
//                imageView.setBackgroundColor(getResources().getColor(R.color.black));
//                row3.addView(imageView);

                        View v = new View(Taxes_List_Sales1.this);
                        v.setLayoutParams(new LinearLayout.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                5
                        ));
                        v.setBackgroundColor(Color.parseColor("#B3B3B3"));

                        row3.addView(v);

                        tableLayout.addView(row);
                        tableLayout.addView(row1);
                        tableLayout.addView(row2);
                        tableLayout.addView(row3);

                    } while (cursor.moveToNext());
                }
                cursor.close();

                Cursor cursor2 = db.rawQuery("Select DISTINCT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 != '"+tax+"' GROUP BY taxname2 ", null);//replace to cursor = dbHelper.fetchAllHotels();
                if (cursor2.moveToFirst()) {
                    do {

                        final TableRow row = new TableRow(Taxes_List_Sales1.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                        row.setPadding(5, 5, 0, 0);

                        final TableRow row1 = new TableRow(Taxes_List_Sales1.this);
                        row1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                        row1.setPadding(5, 0, 0, 0);

                        final TableRow row2 = new TableRow(Taxes_List_Sales1.this);
                        row2.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                        row2.setPadding(5, 0, 0, 0);

                        final TableRow row3 = new TableRow(Taxes_List_Sales1.this);
                        row3.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                        row3.setPadding(5, 0, 0, 5);

                        tax2 = cursor2.getString(35);
                        System.out.println("tax value is 2 "+tax2);

                        TextView tv = new TextView(Taxes_List_Sales1.this);
                        tv.setText(tax2);

                        if (tv.getText().toString().equals("0") || tv.getText().toString().equals("NONE") || tv.getText().toString().equals("None") || tv.getText().toString().equals("0.0")
                                || tv.getText().toString().equals("")) {

                        }else {
                            String hsn_code = "";
                            Cursor cursor1 = db.rawQuery("SELECT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax2 + "' GROUP BY taxname2", null);
                            if (cursor1.moveToFirst()) {
                                hsn_code = cursor1.getString(51);
                                System.out.println("han_code is 2 " + hsn_code);
                            }

//                    String items = "";
//                    Cursor cursor21 = db.rawQuery("SELECT COUNT(itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax2 + "' OR taxname2 = '" + tax2 + "' OR taxname3 = '" + tax2 + "' OR taxname4 = '" + tax2 + "' OR taxname5 = '" + tax2 + "' GROUP BY itemname", null);
//                    if (cursor21.moveToFirst()) {
//                        int leveliss = cursor21.getInt(0);
//                        items = String.valueOf(leveliss);
//                        System.out.println("no. of items 2 " + items);
//                    }

                            String items = "0", items2 = "0", items3 = "0", items4 = "0", items5 = "0";
                            Cursor cursor21 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax2 + "'", null);
                            if (cursor21.moveToFirst()) {
                                int leveliss = cursor21.getInt(0);
                                items = String.valueOf(leveliss);
                                System.out.println("no. of items 2 " + items);
                            }

                            Cursor cursor212 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax2 + "'", null);
                            if (cursor212.moveToFirst()) {
                                int leveliss = cursor212.getInt(0);
                                items2 = String.valueOf(leveliss);
                                System.out.println("no. of items 22 " + items2);
                            }

                            Cursor cursor213 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax2 + "'", null);
                            if (cursor213.moveToFirst()) {
                                int leveliss = cursor213.getInt(0);
                                items3 = String.valueOf(leveliss);
                                System.out.println("no. of items 23 " + items3);
                            }

                            Cursor cursor214 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax2 + "'", null);
                            if (cursor214.moveToFirst()) {
                                int leveliss = cursor214.getInt(0);
                                items4 = String.valueOf(leveliss);
                                System.out.println("no. of items 24 " + items4);
                            }

                            Cursor cursor215 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax2 + "'", null);
                            if (cursor215.moveToFirst()) {
                                int leveliss = cursor215.getInt(0);
                                items5 = String.valueOf(leveliss);
                                System.out.println("no. of items 25 " + items5);
                            }

                            float a1 = Float.parseFloat(items)+Float.parseFloat(items2)+Float.parseFloat(items3)+Float.parseFloat(items4)+Float.parseFloat(items5);


                            String qty = "0", qty2 = "0", qty3 = "0", qty4 = "0", qty5 = "0";
                            Cursor cursor22 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax2 + "'", null);
                            if (cursor22.moveToFirst()) {
                                int leveliss = cursor22.getInt(0);
                                qty = String.valueOf(leveliss);
                                System.out.println("no. of qty 2 " + qty);
                            }

                            Cursor cursor222 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax2 + "'", null);
                            if (cursor222.moveToFirst()) {
                                int leveliss = cursor222.getInt(0);
                                qty2 = String.valueOf(leveliss);
                                System.out.println("no. of qty 22 " + qty2);
                            }

                            Cursor cursor223 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax2 + "'", null);
                            if (cursor223.moveToFirst()) {
                                int leveliss = cursor223.getInt(0);
                                qty3 = String.valueOf(leveliss);
                                System.out.println("no. of qty 23 " + qty3);
                            }

                            Cursor cursor224 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax2 + "'", null);
                            if (cursor224.moveToFirst()) {
                                int leveliss = cursor224.getInt(0);
                                qty4 = String.valueOf(leveliss);
                                System.out.println("no. of qty 24 " + qty4);
                            }

                            Cursor cursor225 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax2 + "'", null);
                            if (cursor225.moveToFirst()) {
                                int leveliss = cursor225.getInt(0);
                                qty5 = String.valueOf(leveliss);
                                System.out.println("no. of qty 25 " + qty5);
                            }

                            float a3 = Float.parseFloat(qty)+Float.parseFloat(qty2)+Float.parseFloat(qty3)+Float.parseFloat(qty4)+Float.parseFloat(qty5);

                            String totalbillis = "0", totalbillis2 = "0", totalbillis3 = "0", totalbillis4 = "0", totalbillis5 = "0";
                            Cursor cursor23 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax2 + "'", null);
                            if (cursor23.moveToFirst()) {
                                int leveliss = cursor23.getInt(0);
                                totalbillis = String.valueOf(leveliss);
                                System.out.println("no. of total 22 " + totalbillis);
                            }

                            Cursor cursor232 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax2 + "'", null);
                            if (cursor232.moveToFirst()) {
                                int leveliss = cursor232.getInt(0);
                                totalbillis2 = String.valueOf(leveliss);
                                System.out.println("no. of total 23 " + totalbillis2);
                            }

                            Cursor cursor233 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax2 + "'", null);
                            if (cursor233.moveToFirst()) {
                                int leveliss = cursor233.getInt(0);
                                totalbillis3 = String.valueOf(leveliss);
                                System.out.println("no. of total 24 " + totalbillis3);
                            }

                            Cursor cursor234 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax2 + "'", null);
                            if (cursor234.moveToFirst()) {
                                int leveliss = cursor234.getInt(0);
                                totalbillis4 = String.valueOf(leveliss);
                                System.out.println("no. of total 25 " + totalbillis4);
                            }

                            Cursor cursor235 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax2 + "'", null);
                            if (cursor235.moveToFirst()) {
                                int leveliss = cursor235.getInt(0);
                                totalbillis5 = String.valueOf(leveliss);
                                System.out.println("no. of total 15 " + totalbillis5);
                            }

                            float a2 = Float.parseFloat(totalbillis)+Float.parseFloat(totalbillis2)+Float.parseFloat(totalbillis3)+Float.parseFloat(totalbillis4)+Float.parseFloat(totalbillis5);

                            String tax_value = "";
                            float a = 0, b = 0;
                            Cursor cursor24 = db.rawQuery("SELECT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND (taxname = '" + tax2 + "' OR taxname2 = '" + tax2 + "' OR taxname3 = '" + tax2 + "' OR taxname4 = '" + tax2 + "' OR taxname5 = '" + tax2 + "')", null);
                            if (cursor24.moveToFirst()) {
                                tax_value = cursor24.getString(36);
                                System.out.println("tax value is 2 " + tax_value);

                                a = (a2 / 100) * Float.parseFloat(tax_value);
                                System.out.println("total tax value is 2 " + a);

                                b = a2+a;
                                System.out.println("total sales value is 2 " + b);
                            }

                            final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                            tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv1.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv1.setTextColor(Color.parseColor("#000000"));
                            tv1.setText(tax2);
                            row.addView(tv1);

                            final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                            tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv2.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv2.setTextColor(Color.parseColor("#000000"));
                            int n2 = Math.round(a1);
                            tv2.setText(String.valueOf(n2));
                            row.addView(tv2);

                            final TextView tv3 = new TextView(Taxes_List_Sales1.this);
                            tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv3.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv3.setTextColor(Color.parseColor("#000000"));
                            int n1 = Math.round(a2);
                            tv3.setText(String.valueOf(n1));
                            row.addView(tv3);

                            final TextView tv4 = new TextView(Taxes_List_Sales1.this);
                            tv4.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv4.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv4.setTextColor(Color.parseColor("#000000"));
                            tv4.setText(hsn_code);
                            row1.addView(tv4);

                            final TextView tv5 = new TextView(Taxes_List_Sales1.this);
                            tv5.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv5.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv5.setTextColor(Color.parseColor("#000000"));
                            int n4 = Math.round(a3);
                            tv5.setText(String.valueOf(n4));
                            row1.addView(tv5);

                            final TextView tv6 = new TextView(Taxes_List_Sales1.this);
                            tv6.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv6.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv6.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv6.setTextColor(Color.parseColor("#000000"));
                            int n3 = Math.round(a);
                            tv6.setText(String.valueOf(n3));
                            row1.addView(tv6);

                            final TextView tv7 = new TextView(Taxes_List_Sales1.this);
                            tv7.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv7.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv7.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv7.setTextColor(Color.parseColor("#000000"));
                            tv7.setText("");
                            row2.addView(tv7);

                            final TextView tv8 = new TextView(Taxes_List_Sales1.this);
                            tv8.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv8.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv8.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv8.setTextColor(Color.parseColor("#000000"));
                            tv8.setText("");
                            row2.addView(tv8);

                            final TextView tv9 = new TextView(Taxes_List_Sales1.this);
                            tv9.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv9.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv9.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv9.setTextColor(Color.parseColor("#000000"));
                            tv9.setText(String.valueOf(Math.round(b)));
                            row2.addView(tv9);

                            String arrStr[] ={tax2, hsn_code, String.valueOf(n2), String.valueOf(n4), String.valueOf(n1), String.valueOf(n3), String.valueOf(Math.round(b))};
                            csvWrite.writeNext(arrStr);

                        }

//                ImageView imageView = new ImageView(Taxes_List_Sales1.this);
//                imageView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 3.0f));
//                imageView.setBackgroundColor(getResources().getColor(R.color.black));
//                row3.addView(imageView);

                        View v = new View(Taxes_List_Sales1.this);
                        v.setLayoutParams(new LinearLayout.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                5
                        ));
                        v.setBackgroundColor(Color.parseColor("#B3B3B3"));

                        row3.addView(v);

                        tableLayout.addView(row);
                        tableLayout.addView(row1);
                        tableLayout.addView(row2);
                        tableLayout.addView(row3);

                    } while (cursor2.moveToNext());
                }
                cursor2.close();

                Cursor cursor3 = db.rawQuery("Select DISTINCT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 != '"+tax+"' AND taxname3 != '"+tax2+"' GROUP BY taxname3", null);//replace to cursor = dbHelper.fetchAllHotels();
                if (cursor3.moveToFirst()) {
                    do {
                        final TableRow row = new TableRow(Taxes_List_Sales1.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                        row.setPadding(5, 5, 0, 0);

                        final TableRow row1 = new TableRow(Taxes_List_Sales1.this);
                        row1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                        row1.setPadding(5, 0, 0, 0);

                        final TableRow row2 = new TableRow(Taxes_List_Sales1.this);
                        row2.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                        row2.setPadding(5, 0, 0, 0);

                        final TableRow row3 = new TableRow(Taxes_List_Sales1.this);
                        row3.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                        row3.setPadding(5, 0, 0, 5);

                        tax3 = cursor3.getString(37);
                        System.out.println("tax value is 3 "+tax3);

                        TextView tv = new TextView(Taxes_List_Sales1.this);
                        tv.setText(tax3);

                        if (tv.getText().toString().equals("0") || tv.getText().toString().equals("NONE") || tv.getText().toString().equals("None") || tv.getText().toString().equals("0.0")
                                || tv.getText().toString().equals("")) {

                        }else {
                            String hsn_code = "";
                            Cursor cursor1 = db.rawQuery("SELECT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax3 + "' GROUP BY taxname3", null);
                            if (cursor1.moveToFirst()) {
                                hsn_code = cursor1.getString(52);
                                System.out.println("han_code is 3 " + hsn_code);
                            }

//                    String items = "";
//                    Cursor cursor21 = db.rawQuery("SELECT COUNT(itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax3 + "' OR taxname2 = '" + tax3 + "' OR taxname3 = '" + tax3 + "' OR taxname4 = '" + tax3 + "' OR taxname5 = '" + tax3 + "' GROUP BY itemname", null);
//                    if (cursor21.moveToFirst()) {
//                        int leveliss = cursor21.getInt(0);
//                        items = String.valueOf(leveliss);
//                        System.out.println("no. of items 3 " + items);
//                    }

                            String items = "0", items2 = "0", items3 = "0", items4 = "0", items5 = "0";
                            Cursor cursor21 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax3 + "'", null);
                            if (cursor21.moveToFirst()) {
                                int leveliss = cursor21.getInt(0);
                                items = String.valueOf(leveliss);
                                System.out.println("no. of items 3 " + items);
                            }

                            Cursor cursor212 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax3 + "'", null);
                            if (cursor212.moveToFirst()) {
                                int leveliss = cursor212.getInt(0);
                                items2 = String.valueOf(leveliss);
                                System.out.println("no. of items 32 " + items2);
                            }

                            Cursor cursor213 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax3 + "'", null);
                            if (cursor213.moveToFirst()) {
                                int leveliss = cursor213.getInt(0);
                                items3 = String.valueOf(leveliss);
                                System.out.println("no. of items 33 " + items3);
                            }

                            Cursor cursor214 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax3 + "'", null);
                            if (cursor214.moveToFirst()) {
                                int leveliss = cursor214.getInt(0);
                                items4 = String.valueOf(leveliss);
                                System.out.println("no. of items 34 " + items4);
                            }

                            Cursor cursor215 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax3 + "'", null);
                            if (cursor215.moveToFirst()) {
                                int leveliss = cursor215.getInt(0);
                                items5 = String.valueOf(leveliss);
                                System.out.println("no. of items 35 " + items5);
                            }

                            float a1 = Float.parseFloat(items)+Float.parseFloat(items2)+Float.parseFloat(items3)+Float.parseFloat(items4)+Float.parseFloat(items5);


                            String qty = "0", qty2 = "0", qty3 = "0", qty4 = "0", qty5 = "0";
                            Cursor cursor22 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax3 + "'", null);
                            if (cursor22.moveToFirst()) {
                                int leveliss = cursor22.getInt(0);
                                qty = String.valueOf(leveliss);
                                System.out.println("no. of qty 3 " + qty);
                            }

                            Cursor cursor222 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax3 + "'", null);
                            if (cursor222.moveToFirst()) {
                                int leveliss = cursor222.getInt(0);
                                qty2 = String.valueOf(leveliss);
                                System.out.println("no. of qty 32 " + qty2);
                            }

                            Cursor cursor223 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax3 + "'", null);
                            if (cursor223.moveToFirst()) {
                                int leveliss = cursor223.getInt(0);
                                qty3 = String.valueOf(leveliss);
                                System.out.println("no. of qty 33 " + qty3);
                            }

                            Cursor cursor224 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax3 + "'", null);
                            if (cursor224.moveToFirst()) {
                                int leveliss = cursor224.getInt(0);
                                qty4 = String.valueOf(leveliss);
                                System.out.println("no. of qty 34 " + qty4);
                            }

                            Cursor cursor225 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax3 + "'", null);
                            if (cursor225.moveToFirst()) {
                                int leveliss = cursor225.getInt(0);
                                qty5 = String.valueOf(leveliss);
                                System.out.println("no. of qty 35 " + qty5);
                            }

                            float a3 = Float.parseFloat(qty)+Float.parseFloat(qty2)+Float.parseFloat(qty3)+Float.parseFloat(qty4)+Float.parseFloat(qty5);

                            String totalbillis = "0", totalbillis2 = "0", totalbillis3 = "0", totalbillis4 = "0", totalbillis5 = "0";
                            Cursor cursor23 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax3 + "'", null);
                            if (cursor23.moveToFirst()) {
                                int leveliss = cursor23.getInt(0);
                                totalbillis = String.valueOf(leveliss);
                                System.out.println("no. of total 3 " + totalbillis);
                            }

                            Cursor cursor232 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax3 + "'", null);
                            if (cursor232.moveToFirst()) {
                                int leveliss = cursor232.getInt(0);
                                totalbillis2 = String.valueOf(leveliss);
                                System.out.println("no. of total 32 " + totalbillis2);
                            }

                            Cursor cursor233 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax3 + "'", null);
                            if (cursor233.moveToFirst()) {
                                int leveliss = cursor233.getInt(0);
                                totalbillis3 = String.valueOf(leveliss);
                                System.out.println("no. of total 33 " + totalbillis3);
                            }

                            Cursor cursor234 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax3 + "'", null);
                            if (cursor234.moveToFirst()) {
                                int leveliss = cursor234.getInt(0);
                                totalbillis4 = String.valueOf(leveliss);
                                System.out.println("no. of total 34 " + totalbillis4);
                            }

                            Cursor cursor235 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax3 + "'", null);
                            if (cursor235.moveToFirst()) {
                                int leveliss = cursor235.getInt(0);
                                totalbillis5 = String.valueOf(leveliss);
                                System.out.println("no. of total 35 " + totalbillis5);
                            }

                            float a2 = Float.parseFloat(totalbillis)+Float.parseFloat(totalbillis2)+Float.parseFloat(totalbillis3)+Float.parseFloat(totalbillis4)+Float.parseFloat(totalbillis5);

                            String tax_value = "";
                            float a = 0, b = 0;
                            Cursor cursor24 = db.rawQuery("SELECT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND (taxname = '" + tax3 + "' OR taxname2 = '" + tax3 + "' OR taxname3 = '" + tax3 + "' OR taxname4 = '" + tax3 + "' OR taxname5 = '" + tax3 + "')", null);
                            if (cursor24.moveToFirst()) {
                                tax_value = cursor24.getString(38);
                                System.out.println("tax value is 3 " + tax_value);

                                a = (a2 / 100) * Float.parseFloat(tax_value);
                                System.out.println("total tax value is 3 " + a);

                                b = a2+a;
                                System.out.println("total sales value is 3 " + b);
                            }

                            final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                            tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv1.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv1.setTextColor(Color.parseColor("#000000"));
                            tv1.setText(tax3);
                            row.addView(tv1);

                            final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                            tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv2.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv2.setTextColor(Color.parseColor("#000000"));
                            int n2 = Math.round(a1);
                            tv2.setText(String.valueOf(n2));
                            row.addView(tv2);

                            final TextView tv3 = new TextView(Taxes_List_Sales1.this);
                            tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv3.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv3.setTextColor(Color.parseColor("#000000"));
                            int n1 = Math.round(a2);
                            tv3.setText(String.valueOf(n1));
                            row.addView(tv3);

                            final TextView tv4 = new TextView(Taxes_List_Sales1.this);
                            tv4.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv4.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv4.setTextColor(Color.parseColor("#000000"));
                            tv4.setText(hsn_code);
                            row1.addView(tv4);

                            final TextView tv5 = new TextView(Taxes_List_Sales1.this);
                            tv5.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv5.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv5.setTextColor(Color.parseColor("#000000"));
                            int n4 = Math.round(a3);
                            tv5.setText(String.valueOf(n4));
                            row1.addView(tv5);

                            final TextView tv6 = new TextView(Taxes_List_Sales1.this);
                            tv6.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv6.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv6.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv6.setTextColor(Color.parseColor("#000000"));
                            int n3 = Math.round(a);
                            tv6.setText(String.valueOf(n3));
                            row1.addView(tv6);

                            final TextView tv7 = new TextView(Taxes_List_Sales1.this);
                            tv7.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv7.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv7.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv7.setTextColor(Color.parseColor("#000000"));
                            tv7.setText("");
                            row2.addView(tv7);

                            final TextView tv8 = new TextView(Taxes_List_Sales1.this);
                            tv8.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv8.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv8.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv8.setTextColor(Color.parseColor("#000000"));
                            tv8.setText("");
                            row2.addView(tv8);

                            final TextView tv9 = new TextView(Taxes_List_Sales1.this);
                            tv9.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv9.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv9.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv9.setTextColor(Color.parseColor("#000000"));
                            tv9.setText(String.valueOf(Math.round(b)));
                            row2.addView(tv9);

                            String arrStr[] ={tax3, hsn_code, String.valueOf(n2), String.valueOf(n4), String.valueOf(n1), String.valueOf(n3), String.valueOf(Math.round(b))};
                            csvWrite.writeNext(arrStr);

                        }

//                ImageView imageView = new ImageView(Taxes_List_Sales1.this);
//                imageView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 3.0f));
//                imageView.setBackgroundColor(getResources().getColor(R.color.black));
//                row3.addView(imageView);

                        View v = new View(Taxes_List_Sales1.this);
                        v.setLayoutParams(new LinearLayout.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                5
                        ));
                        v.setBackgroundColor(Color.parseColor("#B3B3B3"));

                        row3.addView(v);

                        tableLayout.addView(row);
                        tableLayout.addView(row1);
                        tableLayout.addView(row2);
                        tableLayout.addView(row3);
                    } while (cursor3.moveToNext());
                }
                cursor3.close();

                Cursor cursor4 = db.rawQuery("Select DISTINCT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 != '"+tax+"' AND taxname4 != '"+tax2+"' AND taxname4 != '"+tax3+"' GROUP BY taxname4", null);//replace to cursor = dbHelper.fetchAllHotels();
                if (cursor4.moveToFirst()) {
                    do {
                        final TableRow row = new TableRow(Taxes_List_Sales1.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                        row.setPadding(5, 5, 0, 0);

                        final TableRow row1 = new TableRow(Taxes_List_Sales1.this);
                        row1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                        row1.setPadding(5, 0, 0, 0);

                        final TableRow row2 = new TableRow(Taxes_List_Sales1.this);
                        row2.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                        row2.setPadding(5, 0, 0, 0);

                        final TableRow row3 = new TableRow(Taxes_List_Sales1.this);
                        row3.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                        row3.setPadding(5, 0, 0, 5);

                        tax4 = cursor4.getString(39);
                        System.out.println("tax value is 4 "+tax4);

                        TextView tv = new TextView(Taxes_List_Sales1.this);
                        tv.setText(tax4);

                        if (tv.getText().toString().equals("0") || tv.getText().toString().equals("NONE") || tv.getText().toString().equals("None") || tv.getText().toString().equals("0.0")
                                || tv.getText().toString().equals("")) {

                        }else {
                            String hsn_code = "";
                            Cursor cursor1 = db.rawQuery("SELECT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax4 + "' GROUP BY taxname4", null);
                            if (cursor1.moveToFirst()) {
                                hsn_code = cursor1.getString(53);
                                System.out.println("han_code is 4 " + hsn_code);
                            }

//                    String items = "";
//                    Cursor cursor21 = db.rawQuery("SELECT COUNT(itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax4 + "' OR taxname2 = '" + tax4 + "' OR taxname3 = '" + tax4 + "' OR taxname4 = '" + tax4 + "' OR taxname5 = '" + tax4 + "' GROUP BY itemname", null);
//                    if (cursor21.moveToFirst()) {
//                        int leveliss = cursor21.getInt(0);
//                        items = String.valueOf(leveliss);
//                        System.out.println("no. of items 4 " + items);
//                    }

                            String items = "0", items2 = "0", items3 = "0", items4 = "0", items5 = "0";
                            Cursor cursor21 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax4 + "'", null);
                            if (cursor21.moveToFirst()) {
                                int leveliss = cursor21.getInt(0);
                                items = String.valueOf(leveliss);
                                System.out.println("no. of items 4 " + items);
                            }

                            Cursor cursor212 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax4 + "'", null);
                            if (cursor212.moveToFirst()) {
                                int leveliss = cursor212.getInt(0);
                                items2 = String.valueOf(leveliss);
                                System.out.println("no. of items 42 " + items2);
                            }

                            Cursor cursor213 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax4 + "'", null);
                            if (cursor213.moveToFirst()) {
                                int leveliss = cursor213.getInt(0);
                                items3 = String.valueOf(leveliss);
                                System.out.println("no. of items 43 " + items3);
                            }

                            Cursor cursor214 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax4 + "'", null);
                            if (cursor214.moveToFirst()) {
                                int leveliss = cursor214.getInt(0);
                                items4 = String.valueOf(leveliss);
                                System.out.println("no. of items 44 " + items4);
                            }

                            Cursor cursor215 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax4 + "'", null);
                            if (cursor215.moveToFirst()) {
                                int leveliss = cursor215.getInt(0);
                                items5 = String.valueOf(leveliss);
                                System.out.println("no. of items 45 " + items5);
                            }

                            float a1 = Float.parseFloat(items)+Float.parseFloat(items2)+Float.parseFloat(items3)+Float.parseFloat(items4)+Float.parseFloat(items5);


                            String qty = "0", qty2 = "0", qty3 = "0", qty4 = "0", qty5 = "0";
                            Cursor cursor22 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax4 + "'", null);
                            if (cursor22.moveToFirst()) {
                                int leveliss = cursor22.getInt(0);
                                qty = String.valueOf(leveliss);
                                System.out.println("no. of qty 4 " + qty);
                            }

                            Cursor cursor222 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax4 + "'", null);
                            if (cursor222.moveToFirst()) {
                                int leveliss = cursor222.getInt(0);
                                qty2 = String.valueOf(leveliss);
                                System.out.println("no. of qty 42 " + qty2);
                            }

                            Cursor cursor223 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax4 + "'", null);
                            if (cursor223.moveToFirst()) {
                                int leveliss = cursor223.getInt(0);
                                qty3 = String.valueOf(leveliss);
                                System.out.println("no. of qty 43 " + qty3);
                            }

                            Cursor cursor224 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax4 + "'", null);
                            if (cursor224.moveToFirst()) {
                                int leveliss = cursor224.getInt(0);
                                qty4 = String.valueOf(leveliss);
                                System.out.println("no. of qty 44 " + qty4);
                            }

                            Cursor cursor225 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax4 + "'", null);
                            if (cursor225.moveToFirst()) {
                                int leveliss = cursor225.getInt(0);
                                qty5 = String.valueOf(leveliss);
                                System.out.println("no. of qty 45 " + qty5);
                            }

                            float a3 = Float.parseFloat(qty)+Float.parseFloat(qty2)+Float.parseFloat(qty3)+Float.parseFloat(qty4)+Float.parseFloat(qty5);

                            String totalbillis = "0", totalbillis2 = "0", totalbillis3 = "0", totalbillis4 = "0", totalbillis5 = "0";
                            Cursor cursor23 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax4 + "'", null);
                            if (cursor23.moveToFirst()) {
                                int leveliss = cursor23.getInt(0);
                                totalbillis = String.valueOf(leveliss);
                                System.out.println("no. of total 4 " + totalbillis);
                            }

                            Cursor cursor232 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax4 + "'", null);
                            if (cursor232.moveToFirst()) {
                                int leveliss = cursor232.getInt(0);
                                totalbillis2 = String.valueOf(leveliss);
                                System.out.println("no. of total 42 " + totalbillis2);
                            }

                            Cursor cursor233 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax4 + "'", null);
                            if (cursor233.moveToFirst()) {
                                int leveliss = cursor233.getInt(0);
                                totalbillis3 = String.valueOf(leveliss);
                                System.out.println("no. of total 43 " + totalbillis3);
                            }

                            Cursor cursor234 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax4 + "'", null);
                            if (cursor234.moveToFirst()) {
                                int leveliss = cursor234.getInt(0);
                                totalbillis4 = String.valueOf(leveliss);
                                System.out.println("no. of total 44 " + totalbillis4);
                            }

                            Cursor cursor235 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax4 + "'", null);
                            if (cursor235.moveToFirst()) {
                                int leveliss = cursor235.getInt(0);
                                totalbillis5 = String.valueOf(leveliss);
                                System.out.println("no. of total 45 " + totalbillis5);
                            }

                            float a2 = Float.parseFloat(totalbillis)+Float.parseFloat(totalbillis2)+Float.parseFloat(totalbillis3)+Float.parseFloat(totalbillis4)+Float.parseFloat(totalbillis5);

                            String tax_value = "";
                            float a = 0, b = 0;
                            Cursor cursor24 = db.rawQuery("SELECT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND (taxname = '" + tax4 + "' OR taxname2 = '" + tax4 + "' OR taxname3 = '" + tax4 + "' OR taxname4 = '" + tax4 + "' OR taxname5 = '" + tax4 + "')", null);
                            if (cursor24.moveToFirst()) {
                                tax_value = cursor24.getString(40);
                                System.out.println("tax value is 4 " + tax_value);

                                a = (a2 / 100) * Float.parseFloat(tax_value);
                                System.out.println("total tax value is 4 " + a);

                                b = a2+a;
                                System.out.println("total sales value is 4 " + b);
                            }

                            final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                            tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv1.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv1.setTextColor(Color.parseColor("#000000"));
                            tv1.setText(tax4);
                            row.addView(tv1);

                            final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                            tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv2.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv2.setTextColor(Color.parseColor("#000000"));
                            int n2 = Math.round(a1);
                            tv2.setText(String.valueOf(n2));
                            row.addView(tv2);

                            final TextView tv3 = new TextView(Taxes_List_Sales1.this);
                            tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv3.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv3.setTextColor(Color.parseColor("#000000"));
                            int n1 = Math.round(a2);
                            tv3.setText(String.valueOf(n1));
                            row.addView(tv3);

                            final TextView tv4 = new TextView(Taxes_List_Sales1.this);
                            tv4.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv4.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv4.setTextColor(Color.parseColor("#000000"));
                            tv4.setText(hsn_code);
                            row1.addView(tv4);

                            final TextView tv5 = new TextView(Taxes_List_Sales1.this);
                            tv5.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv5.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv5.setTextColor(Color.parseColor("#000000"));
                            int n4 = Math.round(a3);
                            tv5.setText(String.valueOf(n4));
                            row1.addView(tv5);

                            final TextView tv6 = new TextView(Taxes_List_Sales1.this);
                            tv6.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv6.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv6.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv6.setTextColor(Color.parseColor("#000000"));
                            int n3 = Math.round(a);
                            tv6.setText(String.valueOf(n3));
                            row1.addView(tv6);

                            final TextView tv7 = new TextView(Taxes_List_Sales1.this);
                            tv7.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv7.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv7.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv7.setTextColor(Color.parseColor("#000000"));
                            tv7.setText("");
                            row2.addView(tv7);

                            final TextView tv8 = new TextView(Taxes_List_Sales1.this);
                            tv8.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv8.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv8.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv8.setTextColor(Color.parseColor("#000000"));
                            tv8.setText("");
                            row2.addView(tv8);

                            final TextView tv9 = new TextView(Taxes_List_Sales1.this);
                            tv9.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv9.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv9.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv9.setTextColor(Color.parseColor("#000000"));
                            tv9.setText(String.valueOf(Math.round(b)));
                            row2.addView(tv9);

                            String arrStr[] ={tax4, hsn_code, String.valueOf(n2), String.valueOf(n4), String.valueOf(n1), String.valueOf(n3), String.valueOf(Math.round(b))};
                            csvWrite.writeNext(arrStr);

                        }

//                ImageView imageView = new ImageView(Taxes_List_Sales1.this);
//                imageView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 3.0f));
//                imageView.setBackgroundColor(getResources().getColor(R.color.black));
//                row3.addView(imageView);

                        View v = new View(Taxes_List_Sales1.this);
                        v.setLayoutParams(new LinearLayout.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                5
                        ));
                        v.setBackgroundColor(Color.parseColor("#B3B3B3"));

                        row3.addView(v);

                        tableLayout.addView(row);
                        tableLayout.addView(row1);
                        tableLayout.addView(row2);
                        tableLayout.addView(row3);
                    } while (cursor4.moveToNext());
                }
                cursor4.close();

                Cursor cursor5 = db.rawQuery("Select DISTINCT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 != '"+tax+"' AND taxname4 != '"+tax2+"' AND taxname4 != '"+tax3+"' AND taxname4 != '"+tax4+"' GROUP BY taxname4", null);//replace to cursor = dbHelper.fetchAllHotels();
                if (cursor5.moveToFirst()) {
                    do {
                        final TableRow row = new TableRow(Taxes_List_Sales1.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                        row.setPadding(5, 5, 0, 0);

                        final TableRow row1 = new TableRow(Taxes_List_Sales1.this);
                        row1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                        row1.setPadding(5, 0, 0, 0);

                        final TableRow row2 = new TableRow(Taxes_List_Sales1.this);
                        row2.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                        row2.setPadding(5, 0, 0, 0);

                        final TableRow row3 = new TableRow(Taxes_List_Sales1.this);
                        row3.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                        row3.setPadding(5, 0, 0, 5);

                        tax5 = cursor5.getString(41);
                        System.out.println("tax value is 5 "+tax5);

                        TextView tv = new TextView(Taxes_List_Sales1.this);
                        tv.setText(tax5);

                        if (tv.getText().toString().equals("0") || tv.getText().toString().equals("NONE") || tv.getText().toString().equals("None") || tv.getText().toString().equals("0.0")
                                || tv.getText().toString().equals("")) {

                        }else {
                            String hsn_code = "";
                            Cursor cursor1 = db.rawQuery("SELECT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax5 + "' GROUP BY taxname4", null);
                            if (cursor1.moveToFirst()) {
                                hsn_code = cursor1.getString(54);
                                System.out.println("han_code is 5 " + hsn_code);
                            }

//                    String items = "";
//                    Cursor cursor21 = db.rawQuery("SELECT COUNT(itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax5 + "' OR taxname2 = '" + tax5 + "' OR taxname3 = '" + tax5 + "' OR taxname4 = '" + tax5 + "' OR taxname5 = '" + tax5 + "' GROUP BY itemname", null);
//                    if (cursor21.moveToFirst()) {
//                        int leveliss = cursor21.getInt(0);
//                        items = String.valueOf(leveliss);
//                        System.out.println("no. of items 5 " + items);
//                    }

                            String items = "0", items2 = "0", items3 = "0", items4 = "0", items5 = "0";
                            Cursor cursor21 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax5 + "'", null);
                            if (cursor21.moveToFirst()) {
                                int leveliss = cursor21.getInt(0);
                                items = String.valueOf(leveliss);
                                System.out.println("no. of items 5 " + items);
                            }

                            Cursor cursor212 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax5 + "'", null);
                            if (cursor212.moveToFirst()) {
                                int leveliss = cursor212.getInt(0);
                                items2 = String.valueOf(leveliss);
                                System.out.println("no. of items 52 " + items2);
                            }

                            Cursor cursor213 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax5 + "'", null);
                            if (cursor213.moveToFirst()) {
                                int leveliss = cursor213.getInt(0);
                                items3 = String.valueOf(leveliss);
                                System.out.println("no. of items 53 " + items3);
                            }

                            Cursor cursor214 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax5 + "'", null);
                            if (cursor214.moveToFirst()) {
                                int leveliss = cursor214.getInt(0);
                                items4 = String.valueOf(leveliss);
                                System.out.println("no. of items 54 " + items4);
                            }

                            Cursor cursor215 = db.rawQuery("SELECT COUNT(DISTINCT itemname) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax5 + "'", null);
                            if (cursor215.moveToFirst()) {
                                int leveliss = cursor215.getInt(0);
                                items5 = String.valueOf(leveliss);
                                System.out.println("no. of items 55 " + items5);
                            }

                            float a1 = Float.parseFloat(items)+Float.parseFloat(items2)+Float.parseFloat(items3)+Float.parseFloat(items4)+Float.parseFloat(items5);


                            String qty = "0", qty2 = "0", qty3 = "0", qty4 = "0", qty5 = "0";
                            Cursor cursor22 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax5 + "'", null);
                            if (cursor22.moveToFirst()) {
                                int leveliss = cursor22.getInt(0);
                                qty = String.valueOf(leveliss);
                                System.out.println("no. of qty 5 " + qty);
                            }

                            Cursor cursor222 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax5 + "'", null);
                            if (cursor222.moveToFirst()) {
                                int leveliss = cursor222.getInt(0);
                                qty2 = String.valueOf(leveliss);
                                System.out.println("no. of qty 52 " + qty2);
                            }

                            Cursor cursor223 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax5 + "'", null);
                            if (cursor223.moveToFirst()) {
                                int leveliss = cursor223.getInt(0);
                                qty3 = String.valueOf(leveliss);
                                System.out.println("no. of qty 53 " + qty3);
                            }

                            Cursor cursor224 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax5 + "'", null);
                            if (cursor224.moveToFirst()) {
                                int leveliss = cursor224.getInt(0);
                                qty4 = String.valueOf(leveliss);
                                System.out.println("no. of qty 54 " + qty4);
                            }

                            Cursor cursor225 = db.rawQuery("SELECT SUM(quantity) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax5 + "'", null);
                            if (cursor225.moveToFirst()) {
                                int leveliss = cursor225.getInt(0);
                                qty5 = String.valueOf(leveliss);
                                System.out.println("no. of qty 55 " + qty5);
                            }

                            float a3 = Float.parseFloat(qty)+Float.parseFloat(qty2)+Float.parseFloat(qty3)+Float.parseFloat(qty4)+Float.parseFloat(qty5);

                            String totalbillis = "0", totalbillis2 = "0", totalbillis3 = "0", totalbillis4 = "0", totalbillis5 = "0";
                            Cursor cursor23 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname = '" + tax5 + "'", null);
                            if (cursor23.moveToFirst()) {
                                int leveliss = cursor23.getInt(0);
                                totalbillis = String.valueOf(leveliss);
                                System.out.println("no. of total 5 " + totalbillis);
                            }

                            Cursor cursor232 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname2 = '" + tax5 + "'", null);
                            if (cursor232.moveToFirst()) {
                                int leveliss = cursor232.getInt(0);
                                totalbillis2 = String.valueOf(leveliss);
                                System.out.println("no. of total 52 " + totalbillis2);
                            }

                            Cursor cursor233 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname3 = '" + tax5 + "'", null);
                            if (cursor233.moveToFirst()) {
                                int leveliss = cursor233.getInt(0);
                                totalbillis3 = String.valueOf(leveliss);
                                System.out.println("no. of total 53 " + totalbillis3);
                            }

                            Cursor cursor234 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname4 = '" + tax5 + "'", null);
                            if (cursor234.moveToFirst()) {
                                int leveliss = cursor234.getInt(0);
                                totalbillis4 = String.valueOf(leveliss);
                                System.out.println("no. of total 54 " + totalbillis4);
                            }

                            Cursor cursor235 = db.rawQuery("SELECT SUM(old_total) from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND taxname5 = '" + tax5 + "'", null);
                            if (cursor235.moveToFirst()) {
                                int leveliss = cursor235.getInt(0);
                                totalbillis5 = String.valueOf(leveliss);
                                System.out.println("no. of total 55 " + totalbillis5);
                            }

                            float a2 = Float.parseFloat(totalbillis)+Float.parseFloat(totalbillis2)+Float.parseFloat(totalbillis3)+Float.parseFloat(totalbillis4)+Float.parseFloat(totalbillis5);

                            String tax_value = "";
                            float a = 0, b = 0;
                            Cursor cursor24 = db.rawQuery("SELECT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' AND (taxname = '" + tax5 + "' OR taxname2 = '" + tax5 + "' OR taxname3 = '" + tax5 + "' OR taxname4 = '" + tax5 + "' OR taxname5 = '" + tax5 + "')", null);
                            if (cursor24.moveToFirst()) {
                                tax_value = cursor24.getString(42);
                                System.out.println("tax value is 5 " + tax_value);

                                a = (a2 / 100) * Float.parseFloat(tax_value);
                                System.out.println("total tax value is 5 " + a);

                                b = a2+a;
                                System.out.println("total sales value is 5 " + b);
                            }

                            final TextView tv1 = new TextView(Taxes_List_Sales1.this);
                            tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv1.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv1.setTextColor(Color.parseColor("#000000"));
                            tv1.setText(tax5);
                            row.addView(tv1);

                            final TextView tv2 = new TextView(Taxes_List_Sales1.this);
                            tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv2.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv2.setTextColor(Color.parseColor("#000000"));
                            int n2 = Math.round(a1);
                            tv2.setText(String.valueOf(n2));
                            row.addView(tv2);

                            final TextView tv3 = new TextView(Taxes_List_Sales1.this);
                            tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv3.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv3.setTextColor(Color.parseColor("#000000"));
                            int n1 = Math.round(a2);
                            tv3.setText(String.valueOf(n1));
                            row.addView(tv3);

                            final TextView tv4 = new TextView(Taxes_List_Sales1.this);
                            tv4.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv4.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv4.setTextColor(Color.parseColor("#000000"));
                            tv4.setText(hsn_code);
                            row1.addView(tv4);

                            final TextView tv5 = new TextView(Taxes_List_Sales1.this);
                            tv5.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv5.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv5.setTextColor(Color.parseColor("#000000"));
                            int n4 = Math.round(a3);
                            tv5.setText(String.valueOf(n4));
                            row1.addView(tv5);

                            final TextView tv6 = new TextView(Taxes_List_Sales1.this);
                            tv6.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv6.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv6.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv6.setTextColor(Color.parseColor("#000000"));
                            int n3 = Math.round(a);
                            tv6.setText(String.valueOf(n3));
                            row1.addView(tv6);

                            final TextView tv7 = new TextView(Taxes_List_Sales1.this);
                            tv7.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv7.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv7.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv7.setTextColor(Color.parseColor("#000000"));
                            tv7.setText("");
                            row2.addView(tv7);

                            final TextView tv8 = new TextView(Taxes_List_Sales1.this);
                            tv8.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv8.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv8.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv8.setTextColor(Color.parseColor("#000000"));
                            tv8.setText("");
                            row2.addView(tv8);

                            final TextView tv9 = new TextView(Taxes_List_Sales1.this);
                            tv9.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv9.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv9.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv9.setTextColor(Color.parseColor("#000000"));
                            tv9.setText(String.valueOf(Math.round(b)));
                            row2.addView(tv9);

                            String arrStr[] ={tax5, hsn_code, String.valueOf(n2), String.valueOf(n4), String.valueOf(n1), String.valueOf(n3), String.valueOf(Math.round(b))};
                            csvWrite.writeNext(arrStr);

                        }

//                ImageView imageView = new ImageView(Taxes_List_Sales1.this);
//                imageView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 3.0f));
//                imageView.setBackgroundColor(getResources().getColor(R.color.black));
//                row3.addView(imageView);

                        View v = new View(Taxes_List_Sales1.this);
                        v.setLayoutParams(new LinearLayout.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                5
                        ));
                        v.setBackgroundColor(Color.parseColor("#B3B3B3"));

                        row3.addView(v);

                        tableLayout.addView(row);
                        tableLayout.addView(row1);
                        tableLayout.addView(row2);
                        tableLayout.addView(row3);
                    } while (cursor5.moveToNext());
                }
                cursor5.close();


                csvWrite.close();


                String filepath = String.valueOf(file);
                FileReader filee = new FileReader(filepath);

                BufferedReader buffer = new BufferedReader(filee);
                String[] line;

                String linee; int c= 0;
                while ((linee = buffer.readLine()) != null) {
                    c = StringUtils.countMatches(linee, ",");
                }

                CSVWriter csvWrite2 = new CSVWriter(new FileWriter(file, true));
                String arrStr112[] ={"", "", "", "", "", "", ""};
                int lee = 1;
                String arrStrTax2[]=new String[(lee+6)];
                int mm=5;

                float sum = 0;
                for (int i = 5; i<6; i++) {
                    CSVReader reader = new CSVReader(new FileReader(filepath));
                    while ((line = reader.readNext()) != null) {
//                    rowNumber++;
                        String name = line[i];
                        if (name.contains("\"")) {
                            name = name.replaceAll("\"", "");
                        }
                        if (name.matches(".*[a-zA-Z]+.*")) {

                        }else {
//                            System.out.println(i+" count , is "+name);
                            sum = sum + Float.parseFloat(name);
//                            break;
                        }
                    }
                    System.out.println(c+" "+i+" count , is " + sum);
//                    for (int ii =0; ii<lee; ii++){
//                        String tax_l=groupList.get(ii);
                    arrStrTax2[mm]=String.valueOf(String.format("%.2f",sum));
                    mm++;
//                    }
                    sum = 0;
                }
                csvWrite2.writeNext(arrStrTax2);
                csvWrite2.close();

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
                Toast.makeText(Taxes_List_Sales1.this, getString(R.string.export_successful), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(Taxes_List_Sales1.this, getString(R.string.export_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                Taxes_List_Sales1.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private void getResultsFromApi() {

        Cursor cursorr = db1.rawQuery("SELECT * FROM Email_setup", null);
        if (cursorr.moveToFirst()) {
            String unn = cursorr.getString(1);
//            //Toast.maketext(Cash_Card_Credit_Sales.this, "a4 " + unn, Toast.LENGTH_SHORT).show();

            TextView tvv = new TextView(Taxes_List_Sales1.this);
            tvv.setText(unn);

            if (tvv.getText().toString().equals("")) {

            }else {
                mCredential.setSelectedAccountName(tvv.getText().toString());
            }
        }
        cursorr.close();

        if (! isGooglePlayServicesAvailable()) {
//            //Toast.maketext(Cash_Card_Credit_Sales.this, "1", Toast.LENGTH_SHORT).show();
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
//            //Toast.maketext(Cash_Card_Credit_Sales.this, "2", Toast.LENGTH_SHORT).show();
//            chooseAccount();
        } else if (! isDeviceOnline()) {
//            //Toast.maketext(Cash_Card_Credit_Sales.this, "3", Toast.LENGTH_SHORT).show();
//            mOutputText.setText("No network connection available.");
        } else {
//            //Toast.maketext(Cash_Card_Credit_Sales.this, "4", Toast.LENGTH_SHORT).show();
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
                apiAvailability.isGooglePlayServicesAvailable(Taxes_List_Sales1.this);
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
                apiAvailability.isGooglePlayServicesAvailable(Taxes_List_Sales1.this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    private class MakeRequestTask1 extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;

        MakeRequestTask1(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            System.out.println("labels mservice11 " + mService);

            mService = new com.google.api.services.gmail.Gmail.Builder(
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


    private class MakeRequestTask extends AsyncTask<Void, Void, String> {
        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;
//        private View view = sendFabButton;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.gmail.Gmail.Builder(
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

            String strcompanyname = "", straddress1 = "";
            Cursor getcom = db1.rawQuery("SELECT * FROM Companydetailss", null);
            if (getcom.moveToFirst()) {
                do {
                    strcompanyname = getcom.getString(1);
                    straddress1 = getcom.getString(14);
                } while (getcom.moveToNext());
            }
            getcom.close();

            String url = "www.intuitionsoftwares.com";

//            String msg = "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
//                    "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
//                    "Powered by: " + Uri.parse(url);
            String msg = "" + Uri.parse(url);

            Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
            if (cursor1.moveToFirst()) {
                do {
                    String unn = cursor1.getString(3);
                    TextView edtToAddress = new TextView(Taxes_List_Sales1.this);
                    edtToAddress.setText(unn);

                    TextView edtSubject = new TextView(Taxes_List_Sales1.this);
                    edtSubject.setText(strcompanyname);

                    TextView edtMessage = new TextView(Taxes_List_Sales1.this);
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
//                        String filename = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_reports/IVEPOS_Taxwise_report/IvePOS_Taxwise_report"+currentDateandTimee1+"_"+timee1+".csv";
                        String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/IVEPOS_reports/IVEPOS_Taxwise_report/IvePOS_Taxwise_report"+currentDateandTimee1+"_"+timee1+".csv";

                        File f = new File(filename);
//
                        mimeMessage = createEmailWithAttachment(to, from, subject, body, f);

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
                Toast.makeText(Taxes_List_Sales1.this, "not success", Toast.LENGTH_SHORT).show();
//                showMessage(view, "No results returned.");
            } else {
                Toast.makeText(Taxes_List_Sales1.this, "success", Toast.LENGTH_SHORT).show();
//                showMessage(view, output);
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                Log.v("Errors3", mLastError.getMessage());
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    Log.v("Errors1", mLastError.getMessage());
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    Log.v("Errors2", mLastError.getMessage());
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            Utils.REQUEST_AUTHORIZATION);
                } else {
//                    showMessage(view, "The following error occurred:\n" + mLastError.getMessage());
                    Log.v("Errors", mLastError.getMessage());
                }
            } else {
//                showMessage(view, "Request Cancelled.");
            }
        }
    }
}
