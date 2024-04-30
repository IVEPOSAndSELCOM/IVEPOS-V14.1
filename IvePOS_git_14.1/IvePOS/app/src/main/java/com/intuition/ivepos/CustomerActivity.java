package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 1/6/2015.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.intuition.ivepos.sync.StubProvider;
import com.intuition.ivepos.syncapp.StubProviderApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import au.com.bytecode.opencsv.CSVWriter;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.intuition.ivepos.Constants_Inventory.SKU_DELAROY_PRO_UPGRADE;
import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;
/**
 * Created by Rohithkumar on 1/6/2015.
 */
public class CustomerActivity extends Fragment implements DialogInterface.OnClickListener{

    Fragment frag;
    FragmentTransaction fragTransaction;

    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;
    public Cursor cursor, cursor1;
    //    SimpleCursorAdapter dataAdapterr;
    ArrayList<Customer_activity_listitems> countryList;
    ListView listview;

    SimpleCursorAdapter dataAdapterr;
    EditText myFilter;

    int level;
    String total1;

    File file=null;
    SimpleDateFormat sdff2, sdff1;
    String currentDateandTimee1;
    String timee1;


    String companynameis;
    TextView countget;


    EditText cust_namee, cust_phonenoo, cust_emailidd, cust_addresss;
    String idd, cust_name, cust_phoneeno, cusst_emailid, cust_address, cust_pincode;

    EditText add_cust_name, add_cust_phoneno, add_cust_address, add_cust_pincode, add_cust_emailid, add_cust_userid;
    EditText add_bank_no, add_bank_ifsc, add_bank_cust_name, add_bank_name;
    String add_bank_no_string, add_bank_ifsc_string, add_bank_cust_name_string, add_bank_name_string;
    TextView add_cust_dob;
    ImageButton btn_save, btn_close, btn_bank;

    EditText msg_body, ed;
    float mul1 = 0, mul2 = 0, mul3 = 0, mul4 = 0;

    String aqq;

    private int year, year1;
    private int month, month1;
    private int day, day1;

    String onee, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve;
    String onee1, two1, three1, four1, five1, six1, seven1, eight1, nine1, ten1, eleven1, twelve1;


    float dsirsq1, dsirsq2, dsirsq3, dsirsq4;
    String ropq;

    String dis_val, dis_status, dis_ty;

    TextView rating_perc;


    String sumnew, sumnew1, sumnew2, sumnew3, sumnew4, sumnew5;


    String menu_credit_stauts;
    MenuItem searchItem;

    TextView editText_from_day_visible, editText_from_day_hide, editText_to_day_visible, editText_to_day_hide;
    private int hour;
    private int minute;

    TextView tvkot;
    TextView editText1, editText2, editText11, editText22, editText1_filter, editText2_filter;

    int clickcount=1, clickcounts = 1;
    String total_l, total_l1, total_l3, total_l4, total_l5, total_l6, total_l7;
    String v1_profit, vq1_cash, vqq1_product;

    ImageView rotatearrow;


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


    ArrayList<Country_promotions> list_promotions;
    ArrayAdapter<Country_promotions> adapter_promotions;
    TextView email_re;
    ListView listview_tax_list;

    String idv, idv_email1, idv_address1, idv_pincode1, idv_dob, idv_user_id;
    boolean reachable;

    File file1=null;

    int requestCode_i;
    String filepath;

    ProgressBar proceed_button;
    LinearLayout rela;
    FloatingActionButton add_button;


    IabHelper mHelperPro;
    String mSelectedProSubscription="";
    String company,store, device,email;
    private static int REQ_CODE = 1000;
    ProgressBar bar;
    static final String TAG = "MenuActivity";

    String checking;
    String payload, bucketName;
    int state;
    int i;
    TransferUtility transferUtility;
    SQLiteDatabase db_inapp = null;
    AmazonS3 s3client;
    public static final String PACKAGE_NAME = "com.intuition.ivepos";

    String insert1_cc = "", insert1_rs = "", str_country;

    RequestQueue requestQueue;
    Uri contentUri,resultUri;

    CognitoCachingCredentialsProvider credentialsProvider1;
    AmazonS3 s3client1;

    String t_total_points;
    float v_tq;

    String WebserviceUrl;

    int count = 0;
    String smschceccking;
    int countlimit;

    public CustomerActivity(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View rootview = inflater.inflate(R.layout.fragment_multi_customeractivityreport1, null);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(getActivity());
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

        db1 = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        Cursor cursor_country = db1.rawQuery("SELECT * FROM Country_Selection", null);
        if (cursor_country.moveToFirst()){
            str_country = cursor_country.getString(1);
        }
        cursor_country.close();

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
                        if (str_country.toString().equals("Dinar")) {
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

        credentialsProvider1 = new CognitoCachingCredentialsProvider(
                getActivity(),
                "us-east-1:0730d9ff-ed86-40f5-9c5a-6afa8ecf0dd7", // Identity pool ID
                Regions.US_EAST_1 // Region
        );
        setAmazonS3Client1(credentialsProvider1);
        setTransferUtility();

        Cursor cd = db1.rawQuery("SELECT * FROM promotions", null);
        if (cd.moveToFirst()){
            do {
                String id = cd.getString(0);
                String where = "_id = '" + id + "' ";

                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "promotions");
                getActivity().getContentResolver().delete(contentUri, where,new String[]{});
                resultUri = new Uri.Builder()
                        .scheme("content")
                        .authority(StubProviderApp.AUTHORITY)
                        .path("promotions")
                        .appendQueryParameter("operation", "delete")
                        .appendQueryParameter("_id", id)
                        .build();
                getActivity().getContentResolver().notifyChange(resultUri, null);


//                db1.delete("promotions", where, new String[]{});
            }while (cd.moveToNext());
        }
        cd.close();


        mCredential = GoogleAccountCredential.usingOAuth2(
                getActivity().getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage("Sending mail ...");


        donotshowKeyboard(getActivity());

        if (getActivity() instanceof AppCompatActivity){
            androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionbar.setSubtitle("Management");
        }

        final Calendar c = Calendar.getInstance();
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);

        tvkot = new TextView(getActivity());
        editText1_filter = new TextView(getActivity());
        editText2_filter = new TextView(getActivity());

        rating_perc = (TextView) rootview.findViewById(R.id.rating);

        countget = (TextView)rootview.findViewById(R.id.count);

        db = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        db_inapp = getActivity().openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);

//        countryList = new ArrayList<Customer_activity_listitems>();


        Cursor curCSV0 = db.rawQuery("SELECT SUM(total) FROM Billnumber", null);
        if (curCSV0.moveToFirst()) {
            do {
                float aq = curCSV0.getFloat(0);
                sumnew = String.valueOf(aq);
            }
            while (curCSV0.moveToNext());
        }
        curCSV0.close();


        Cursor cursor12 = db.rawQuery("SELECT SUM(total) FROM Billnumber WHERE paymentmethod = '  Cash' ", null);
        if (cursor12.moveToFirst()) {
            float level_l1 = cursor12.getInt(0);
            total_l1 = String.valueOf(level_l1);
        }
        cursor12.close();

        Cursor cursor13 = db.rawQuery("SELECT SUM(total) FROM Billnumber WHERE paymentmethod = '  Card' ", null);
        if (cursor13.moveToFirst()) {
            float level_l3 = cursor13.getInt(0);
            total_l3 = String.valueOf(level_l3);
        }
        cursor13.close();


        add_button = (FloatingActionButton) rootview.findViewById(R.id.add_button);

        proceed_button = (ProgressBar) rootview.findViewById(R.id.proceed_button);
        rela = (LinearLayout) rootview.findViewById(R.id.rela);

//        DownloadMusicfromInternet1 downloadMusicfromInternet1 = new DownloadMusicfromInternet1();
//        downloadMusicfromInternet1.execute();
//
//
//        DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
//        downloadMusicfromInternet.execute();
        listview = (ListView) rootview.findViewById(R.id.listView);


        myFilter = (EditText)rootview.findViewById(R.id.searchView);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapterr.getFilter().filter(s.toString());
            }
        });

        ImageView deleteicon = (ImageView)rootview.findViewById(R.id.delete_icon);
        deleteicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFilter.setText("");
            }
        });

        Cursor one = db.rawQuery("SELECT * FROM Customerdetails GROUP BY phoneno", null);
        int cou = one.getCount();
        total1 = String.valueOf(cou);
        countget.setText(total1);

        DownloadMusicfromInternet_new downloadMusicfromInternet_new = new DownloadMusicfromInternet_new();
        downloadMusicfromInternet_new.execute();

//        new Handler().postDelayed(new Runnable() {
//
//            /*
//             * Showing splash screen with a timer. This will be useful when you
//             * want to show case your app logo / company
//             */
//
//            @Override
//            public void run() {
//                // This method will be executed once the timer is over
//                // Start your app main activity
//                DownloadMusicfromInternet_new downloadMusicfromInternet_new = new DownloadMusicfromInternet_new();
//                try {
//                    downloadMusicfromInternet_new.execute();
//                    //
//                } catch (Exception e) {
//                    downloadMusicfromInternet_new.cancel(true);
//                    e.printStackTrace();
//                }
//            }
//        }, 2000);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                final Customer_activity_listitems country = (Customer_activity_listitems) parent.getItemAtPosition(position);

                final Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                idd = cursor.getString(cursor.getColumnIndex("user_id"));
                cust_name = cursor.getString(cursor.getColumnIndex("name"));
                cust_phoneeno = cursor.getString(cursor.getColumnIndex("phoneno"));
                cusst_emailid = cursor.getString(cursor.getColumnIndex("emailid"));
                cust_address = cursor.getString(cursor.getColumnIndex("address"));
                cust_pincode = cursor.getString(cursor.getColumnIndex("pincode"));

                final String ph_no = cust_phoneeno;

                final Dialog dialog_cust = new Dialog(getActivity(), R.style.timepicker_date_dialog);
                dialog_cust.setContentView(R.layout.dialog_customerlist_edit);
                dialog_cust.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog_cust.show();

                TextView inn = (TextView) dialog_cust.findViewById(R.id.rs);
                inn.setText(insert1_rs);

                final TextView ph_num = (TextView) dialog_cust.findViewById(R.id.phonenoedit);
                ph_num.setText(cust_phoneeno);

                final TextView cu_name = (TextView) dialog_cust.findViewById(R.id.nameedit);
                cu_name.setText(cust_name);

                Cursor ty = db.rawQuery("SELECT * FROM Customerdetails WHERE phoneno = '"+ph_no+"'", null);
                if (ty.moveToFirst()){
                    idv_email1 = ty.getString(3);
                    idv_address1 = ty.getString(4);
                    idv_pincode1 = ty.getString(79);
                    idv_dob = ty.getString(17);
                    idv_user_id = ty.getString(37);
                }
                ty.close();

                final Cursor c1 = db.rawQuery("SELECT * FROM Customerdetails WHERE phoneno = '"+ph_no+"'", null);
                if (c1.moveToFirst()){
                    idv = c1.getString(0);
                }
                c1.close();

                final TextView cu_email = (TextView) dialog_cust.findViewById(R.id.emailidedit);
                cu_email.setText(idv_email1);

                final TextView cu_add = (TextView) dialog_cust.findViewById(R.id.addressedit);
                cu_add.setText(idv_address1);

                final TextView cu_pinc = (TextView) dialog_cust.findViewById(R.id.pincodeedit);
                cu_pinc.setText(idv_pincode1);

                final TextView cu_dob = (TextView) dialog_cust.findViewById(R.id.dobedit);
                cu_dob.setText(idv_dob);

                final TextView cu_usr_id = (TextView) dialog_cust.findViewById(R.id.cust_id_edit);
                cu_usr_id.setText(idv_user_id);

                final ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

                if (cm != null) {
                    if (Build.VERSION.SDK_INT < 23) {
                        try {
                            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
                            int returnVal = p1.waitFor();
                            reachable = (returnVal==0);
                            System.out.println(""+reachable);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
//                            return result;
                    } else {
                        boolean isOnline = false;
                        try {
                            ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkCapabilities capabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());  // need ACCESS_NETWORK_STATE permission
                            isOnline = capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (isOnline){
                            System.out.println("internet");
                            reachable = true;
                            total_loya(ph_no, dialog_cust);
                        }else {
                            System.out.println("no internet");
                            reachable = false;
                            Cursor cursor2 = db.rawQuery("SELECT * FROM customer_loyalty_points WHERE phoneno = '"+ph_no+"'", null);
                            if (cursor2.moveToFirst()){
                                t_total_points = cursor2.getString(2);

                                Cursor cursor_loyalty_points = db1.rawQuery("SELECT * FROM loyalty_points", null);
                                if (cursor_loyalty_points.moveToFirst()){
                                    String t_point2 = cursor_loyalty_points.getString(3);
                                    String t_money2 = cursor_loyalty_points.getString(4);

                                    v_tq = Float.parseFloat(t_total_points) * Float.parseFloat(t_money2);

                                }
                                cursor_loyalty_points.close();

                            }

                            final TextView total_points = (TextView) dialog_cust.findViewById(R.id.total_points);
                            total_points.setText(t_total_points);

                            final TextView total_amount = (TextView) dialog_cust.findViewById(R.id.total_amount);
                            total_amount.setText(String.format("%.2f", v_tq));
                        }

//                    return isOnline;
                    }
                }



                cu_dob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar now = Calendar.getInstance();
                        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                                datePickerListener,now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                        );

                        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                    }

                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener datePickerListener
                            = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog, int selectedYear1, int selectedMonth1, int selectedDay1) {
                            year1 = selectedYear1;
                            month1 = selectedMonth1;
                            day1 = selectedDay1;

                            populateSetDate(year1, month1 + 1, day1);
                        }
                    };

                    public void populateSetDate(int year, int month, int day) {
                        TextView mEdit1 = (TextView) dialog_cust.findViewById(R.id.dobedit);

                        if (month == 1 && day < 10) {
                            onee1 = "0" + day + " " + "Jan" + " " + year;
                            mEdit1.setText(onee1);
                        } else {
                            if (month == 1) {
                                onee = day + " " + "Jan" + " " + year;
                                mEdit1.setText(onee);
                            }
                        }

                        if (month == 2 && day < 10) {
                            two1 = "0" + day + " " + "Feb" + " " + year;
                            mEdit1.setText(two1);
                        } else {
                            if (month == 2) {
                                two = day + " " + "Feb" + " " + year;
                                mEdit1.setText(two);
                            }
                        }

                        if (month == 3 && day < 10) {
                            three1 = "0" + day + " " + "Mar" + " " + year;
                            mEdit1.setText(three1);
                        } else {
                            if (month == 3) {
                                three = day + " " + "Mar" + " " + year;
                                mEdit1.setText(three);
                            }
                        }

                        if (month == 4 && day < 10) {
                            four1 = "0" + day + " " + "Apr" + " " + year;
                            mEdit1.setText(four1);
                        } else {
                            if (month == 4) {
                                four = day + " " + "Apr" + " " + year;
                                mEdit1.setText(four);
                            }
                        }

                        if (month == 5 && day < 10) {
                            five1 = "0" + day + " " + "May" + " " + year;
                            mEdit1.setText(five1);
                        } else {
                            if (month == 5) {
                                five = day + " " + "May" + " " + year;
                                mEdit1.setText(five);
                            }
                        }

                        if (month == 6 && day < 10) {
                            six1 = "0" + day + " " + "Jun" + " " + year;
                            mEdit1.setText(six1);
                        } else {
                            if (month == 6) {
                                six = day + " " + "Jun" + " " + year;
                                mEdit1.setText(six);
                            }
                        }

                        if (month == 7 && day < 10) {
                            seven1 = "0" + day + " " + "Jul" + " " + year;
                            mEdit1.setText(seven1);
                        } else {
                            if (month == 7) {
                                seven = day + " " + "Jul" + " " + year;
                                mEdit1.setText(seven);
                            }
                        }

                        if (month == 8 && day < 10) {
                            eight1 = "0" + day + " " + "Aug" + " " + year;
                            mEdit1.setText(eight1);
                        } else {
                            if (month == 8) {
                                eight = day + " " + "Aug" + " " + year;
                                mEdit1.setText(eight);
                            }
                        }

                        if (month == 9 && day < 10) {
                            nine1 = "0" + day + " " + "Sep" + " " + year;
                            mEdit1.setText(nine1);
                        } else {
                            if (month == 9) {
                                nine = day + " " + "Sep" + " " + year;
                                mEdit1.setText(nine);
                            }
                        }

                        if (month == 10 && day < 10) {
                            ten1 = "0" + day + " " + "Oct" + " " + year;
                            mEdit1.setText(ten1);
                        } else {
                            if (month == 10) {
                                ten = day + " " + "Oct" + " " + year;
                                mEdit1.setText(ten);
                            }
                        }

                        if (month == 11 && day < 10) {
                            eleven1 = "0" + day + " " + "Nov" + " " + year;
                            mEdit1.setText(eleven1);
                        } else {
                            if (month == 11) {
                                eleven = day + " " + "Nov" + " " + year;
                                mEdit1.setText(eleven);
                            }
                        }

                        if (month == 12 && day < 10) {
                            twelve1 = "0" + day + " " + "Dec" + " " + year;
                            mEdit1.setText(twelve1);
                        } else {
                            if (month == 12) {
                                twelve = day + " " + "Dec" + " " + year;
                                mEdit1.setText(twelve);
                            }
                        }

                    }

                });


                ImageButton cancel = (ImageButton) dialog_cust.findViewById(R.id.canceledit);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_cust.dismiss();
                    }
                });

                ImageButton save = (ImageButton) dialog_cust.findViewById(R.id.okedit);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ph_num.getText().toString().equals(ph_no.toString())){
                            if (cu_name.getText().toString().equals("") || ph_num.getText().toString().equals("")
                                    || cu_pinc.getText().toString().length() < 6){
                                if (cu_name.getText().toString().equals("")){
                                    cu_name.setError("Enter customer name");
                                }
                                if (ph_num.getText().toString().equals("")){
                                    ph_num.setError("Enter customer phone no");
                                }
                                if (cu_pinc.getText().toString().length() < 6){
                                    ph_num.setError("Enter valid pincode");
                                }
                            }else {

                                ContentValues contentValues = new ContentValues();
                                contentValues.put("name", cu_name.getText().toString());
                                contentValues.put("emailid", cu_email.getText().toString());
                                contentValues.put("phoneno", ph_num.getText().toString());
                                contentValues.put("address", cu_add.getText().toString());
                                contentValues.put("pincode", cu_pinc.getText().toString());
                                contentValues.put("dob", cu_dob.getText().toString());
                                contentValues.put("user_id", cu_usr_id.getText().toString());

                                String wherecu = "_id = '" + idv + "'";

                                System.out.println("id is1 "+idv);


                                contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
                                getActivity().getContentResolver().update(contentUri, contentValues,wherecu,new String[]{});
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProvider.AUTHORITY)
                                        .path("Customerdetails")
                                        .appendQueryParameter("operation", "update")
                                        .appendQueryParameter("_id",idv)
                                        .build();
                                getActivity().getContentResolver().notifyChange(resultUri, null);



                           //     db.update("Customerdetails", contentValues, wherecu, new String[]{});

                                dialog_cust.dismiss();

                                Cursor c1 = db.rawQuery("SELECT * FROM Cust_feedback WHERE cust_phoneno = '"+ph_no+"'", null);
                                if (c1.moveToFirst()){
                                    do {
                                        String id = c1.getString(0);

                                        ContentValues co = new ContentValues();
                                        co.put("cust_phoneno", ph_num.getText().toString());
                                        co.put("cust_name", cu_name.getText().toString());
                                        String where = "_id = '" + id + "'";

                                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Cust_feedback");
                                        getActivity().getContentResolver().update(contentUri, co,where,new String[]{});
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProvider.AUTHORITY)
                                                .path("Cust_feedback")
                                                .appendQueryParameter("operation", "update")
                                                .appendQueryParameter("_id",id)
                                                .build();
                                        getActivity().getContentResolver().notifyChange(resultUri, null);
//                                        db.update("Cust_feedback", co, where, new String[]{});
                                    }while (c1.moveToNext());
                                }
                                c1.close();

                                Cursor c2 = db.rawQuery("SELECT * FROM Customerdetails_temporary WHERE phoneno = '"+ph_no+"'", null);
                                if (c2.moveToFirst()){
                                    do {
                                        String id = c2.getString(0);

                                        ContentValues co = new ContentValues();
                                        co.put("phoneno", ph_num.getText().toString());
                                        co.put("name", cu_name.getText().toString());
                                        String where = "_id = '" + id + "'";

                                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails_temporary");
                                        getActivity().getContentResolver().update(contentUri, co,where,new String[]{});
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProvider.AUTHORITY)
                                                .path("Customerdetails_temporary")
                                                .appendQueryParameter("operation", "update")
                                                .appendQueryParameter("_id",id)
                                                .build();
                                        getActivity().getContentResolver().notifyChange(resultUri, null);

//                                        db.update("Customerdetails_temporary", co, where, new String[]{});
                                    }while (c2.moveToNext());
                                }
                                c2.close();

                                Cursor c3 = db.rawQuery("SELECT * FROM Customerdetails WHERE phoneno = '"+ph_no+"'", null);
                                if (c3.moveToFirst()){
                                    do {
                                        String id = c3.getString(0);

                                        ContentValues co = new ContentValues();
                                        co.put("phoneno", ph_num.getText().toString());
                                        co.put("name", cu_name.getText().toString());
                                        co.put("emailid", cu_email.getText().toString());
                                        co.put("phoneno", ph_num.getText().toString());
                                        co.put("address", cu_add.getText().toString());
                                        co.put("pincode", cu_pinc.getText().toString());
                                        co.put("dob", cu_dob.getText().toString());
                                        co.put("user_id", cu_usr_id.getText().toString());
                                        co.put("cust_account_no", add_bank_no_string);
                                        co.put("cust_ifsc_code", add_bank_ifsc_string);
                                        co.put("cust_account_holder_name", add_bank_cust_name_string);
                                        co.put("cust_bank_name", add_bank_name_string);
                                        String where = "_id = '" + id + "'";

                                        System.out.println("id is1 "+id);

                                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
                                        getActivity().getContentResolver().update(contentUri, co,where,new String[]{});
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProvider.AUTHORITY)
                                                .path("Customerdetails")
                                                .appendQueryParameter("operation", "update")
                                                .appendQueryParameter("_id",id)
                                                .build();
                                        getActivity().getContentResolver().notifyChange(resultUri, null);

//                                        db.update("Customerdetails", co, where, new String[]{});
                                    }while (c3.moveToNext());
                                }
                                c3.close();

                                DownloadMusicfromInternet_new downloadMusicfromInternet_new = new DownloadMusicfromInternet_new();
                                try {
                                    downloadMusicfromInternet_new.execute();
                                } catch (Exception e) {
                                    downloadMusicfromInternet_new.cancel(true);
                                    e.printStackTrace();
                                }
                            }
                        }else {
                            Cursor cv = db.rawQuery("Select * from Customerdetails WHERE phoneno = '"+ph_num.getText().toString()+"'", null);
                            if (cv.moveToFirst()) {
                                ph_num.setError("Phone no already in list");
                            }else {
                                if (cu_usr_id.getText().toString().equals(idv_user_id.toString())){
                                    if (cu_name.getText().toString().equals("") || ph_num.getText().toString().equals("")) {
                                        if (cu_name.getText().toString().equals("")) {
                                            cu_name.setError("Enter customer name");
                                        }
                                        if (ph_num.getText().toString().equals("")) {
                                            ph_num.setError("Enter customer phone no");
                                        }
                                    } else {
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("name", cu_name.getText().toString());
                                        contentValues.put("emailid", cu_email.getText().toString());
                                        contentValues.put("phoneno", ph_num.getText().toString());
                                        contentValues.put("address", cu_add.getText().toString());
                                        contentValues.put("pincode", cu_pinc.getText().toString());
                                        contentValues.put("user_id", cu_usr_id.getText().toString());
                                        contentValues.put("cust_account_no", add_bank_no_string);
                                        contentValues.put("cust_ifsc_code", add_bank_ifsc_string);
                                        contentValues.put("cust_account_holder_name", add_bank_cust_name_string);
                                        contentValues.put("cust_bank_name", add_bank_name_string);

                                        String wherecu = "_id = '" + idv + "'";

                                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
                                        getActivity().getContentResolver().update(contentUri, contentValues,wherecu,new String[]{});
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProvider.AUTHORITY)
                                                .path("Customerdetails")
                                                .appendQueryParameter("operation", "update")
                                                .appendQueryParameter("_id",idv)
                                                .build();
                                        getActivity().getContentResolver().notifyChange(resultUri, null);

//                                        db.update("Customerdetails", contentValues, wherecu, new String[]{});

                                        dialog_cust.dismiss();

                                        Cursor c1 = db.rawQuery("SELECT * FROM Cust_feedback WHERE cust_phoneno = '" + ph_no + "'", null);
                                        if (c1.moveToFirst()) {
                                            do {
                                                String id = c1.getString(0);

                                                ContentValues co = new ContentValues();
                                                co.put("cust_phoneno", ph_num.getText().toString());
                                                co.put("cust_name", cu_name.getText().toString());
                                                String where = "_id = '" + id + "'";

                                                contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Cust_feedback");
                                                getActivity().getContentResolver().update(contentUri, co,where,new String[]{});
                                                resultUri = new Uri.Builder()
                                                        .scheme("content")
                                                        .authority(StubProvider.AUTHORITY)
                                                        .path("Cust_feedback")
                                                        .appendQueryParameter("operation", "update")
                                                        .appendQueryParameter("_id",id)
                                                        .build();
                                                getActivity().getContentResolver().notifyChange(resultUri, null);

//                                                db.update("Cust_feedback", co, where, new String[]{});
                                            } while (c1.moveToNext());
                                        }
                                        c1.close();

                                        Cursor c2 = db.rawQuery("SELECT * FROM Customerdetails_temporary WHERE phoneno = '" + ph_no + "'", null);
                                        if (c2.moveToFirst()) {
                                            do {
                                                String id = c2.getString(0);

                                                ContentValues co = new ContentValues();
                                                co.put("phoneno", ph_num.getText().toString());
                                                co.put("name", cu_name.getText().toString());
                                                String where = "_id = '" + id + "'";

                                                contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails_temporary");
                                                getActivity().getContentResolver().update(contentUri, co,where,new String[]{});
                                                resultUri = new Uri.Builder()
                                                        .scheme("content")
                                                        .authority(StubProvider.AUTHORITY)
                                                        .path("Customerdetails_temporary")
                                                        .appendQueryParameter("operation", "update")
                                                        .appendQueryParameter("_id",id)
                                                        .build();
                                                getActivity().getContentResolver().notifyChange(resultUri, null);

//                                                db.update("Customerdetails_temporary", co, where, new String[]{});
                                            } while (c2.moveToNext());
                                        }
                                        c2.close();

                                        Cursor c3 = db.rawQuery("SELECT * FROM Customerdetails WHERE phoneno = '" + ph_no + "'", null);
                                        if (c3.moveToFirst()) {
                                            do {
                                                String id = c3.getString(0);

                                                ContentValues co = new ContentValues();
                                                co.put("phoneno", ph_num.getText().toString());
                                                co.put("name", cu_name.getText().toString());
                                                co.put("emailid", cu_email.getText().toString());
                                                co.put("phoneno", ph_num.getText().toString());
                                                co.put("address", cu_add.getText().toString());
                                                co.put("pincode", cu_pinc.getText().toString());
                                                co.put("dob", cu_dob.getText().toString());
                                                co.put("user_id", cu_usr_id.getText().toString());
                                                contentValues.put("cust_account_no", add_bank_no_string);
                                                contentValues.put("cust_ifsc_code", add_bank_ifsc_string);
                                                contentValues.put("cust_account_holder_name", add_bank_cust_name_string);
                                                contentValues.put("cust_bank_name", add_bank_name_string);
                                                String where = "_id = '" + id + "'";

                                                contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
                                                getActivity().getContentResolver().update(contentUri, co,where,new String[]{});
                                                resultUri = new Uri.Builder()
                                                        .scheme("content")
                                                        .authority(StubProvider.AUTHORITY)
                                                        .path("Customerdetails")
                                                        .appendQueryParameter("operation", "update")
                                                        .appendQueryParameter("_id",id)
                                                        .build();
                                                getActivity().getContentResolver().notifyChange(resultUri, null);

//                                                db.update("Customerdetails", co, where, new String[]{});
                                            } while (c3.moveToNext());
                                        }
                                        c3.close();

                                        DownloadMusicfromInternet_new downloadMusicfromInternet_new = new DownloadMusicfromInternet_new();
                                        try {
                                            downloadMusicfromInternet_new.execute();
                                        } catch (Exception e) {
                                            downloadMusicfromInternet_new.cancel(true);
                                            e.printStackTrace();
                                        }

                                    }
                                }else {
                                    Cursor usr_id_get = db.rawQuery("Select * from Customerdetails WHERE user_id = '" + cu_usr_id.getText().toString() + "'", null);
                                    if (usr_id_get.moveToFirst()) {
                                        cu_usr_id.setError("User id already in list");
                                    } else {
                                        if (cu_name.getText().toString().equals("") || ph_num.getText().toString().equals("")) {
                                            if (cu_name.getText().toString().equals("")) {
                                                cu_name.setError("Enter customer name");
                                            }
                                            if (ph_num.getText().toString().equals("")) {
                                                ph_num.setError("Enter customer phone no");
                                            }
                                        } else {
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("name", cu_name.getText().toString());
                                            contentValues.put("emailid", cu_email.getText().toString());
                                            contentValues.put("phoneno", ph_num.getText().toString());
                                            contentValues.put("address", cu_add.getText().toString());
                                            contentValues.put("pincode", cu_pinc.getText().toString());
                                            contentValues.put("user_id", cu_usr_id.getText().toString());
                                            contentValues.put("cust_account_no", add_bank_no_string);
                                            contentValues.put("cust_ifsc_code", add_bank_ifsc_string);
                                            contentValues.put("cust_account_holder_name", add_bank_cust_name_string);
                                            contentValues.put("cust_bank_name", add_bank_name_string);

                                            String wherecu = "_id = '" + idv + "'";

                                            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
                                            getActivity().getContentResolver().update(contentUri, contentValues,wherecu,new String[]{});
                                            resultUri = new Uri.Builder()
                                                    .scheme("content")
                                                    .authority(StubProvider.AUTHORITY)
                                                    .path("Customerdetails")
                                                    .appendQueryParameter("operation", "update")
                                                    .appendQueryParameter("_id",idv)
                                                    .build();
                                            getActivity().getContentResolver().notifyChange(resultUri, null);

//                                            db.update("Customerdetails", contentValues, wherecu, new String[]{});

                                            dialog_cust.dismiss();

                                            Cursor c1 = db.rawQuery("SELECT * FROM Cust_feedback WHERE cust_phoneno = '" + ph_no + "'", null);
                                            if (c1.moveToFirst()) {
                                                do {
                                                    String id = c1.getString(0);

                                                    ContentValues co = new ContentValues();
                                                    co.put("cust_phoneno", ph_num.getText().toString());
                                                    co.put("cust_name", cu_name.getText().toString());
                                                    String where = "_id = '" + id + "'";

                                                    contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Cust_feedback");
                                                    getActivity().getContentResolver().update(contentUri, co,where,new String[]{});
                                                    resultUri = new Uri.Builder()
                                                            .scheme("content")
                                                            .authority(StubProvider.AUTHORITY)
                                                            .path("Cust_feedback")
                                                            .appendQueryParameter("operation", "update")
                                                            .appendQueryParameter("_id",id)
                                                            .build();
                                                    getActivity().getContentResolver().notifyChange(resultUri, null);

//                                                    db.update("Cust_feedback", co, where, new String[]{});
                                                } while (c1.moveToNext());
                                            }
                                            c1.close();

                                            Cursor c2 = db.rawQuery("SELECT * FROM Customerdetails_temporary WHERE phoneno = '" + ph_no + "'", null);
                                            if (c2.moveToFirst()) {
                                                do {
                                                    String id = c2.getString(0);

                                                    ContentValues co = new ContentValues();
                                                    co.put("phoneno", ph_num.getText().toString());
                                                    co.put("name", cu_name.getText().toString());
                                                    String where = "_id = '" + id + "'";

                                                    contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails_temporary");
                                                    getActivity().getContentResolver().update(contentUri, co,where,new String[]{});
                                                    resultUri = new Uri.Builder()
                                                            .scheme("content")
                                                            .authority(StubProvider.AUTHORITY)
                                                            .path("Customerdetails_temporary")
                                                            .appendQueryParameter("operation", "update")
                                                            .appendQueryParameter("_id",id)
                                                            .build();
                                                    getActivity().getContentResolver().notifyChange(resultUri, null);

//                                                    db.update("Customerdetails_temporary", co, where, new String[]{});
                                                } while (c2.moveToNext());
                                            }
                                            c2.close();

                                            Cursor c3 = db.rawQuery("SELECT * FROM Customerdetails WHERE phoneno = '" + ph_no + "'", null);
                                            if (c3.moveToFirst()) {
                                                do {
                                                    String id = c3.getString(0);

                                                    ContentValues co = new ContentValues();
                                                    co.put("phoneno", ph_num.getText().toString());
                                                    co.put("name", cu_name.getText().toString());
                                                    co.put("emailid", cu_email.getText().toString());
                                                    co.put("phoneno", ph_num.getText().toString());
                                                    co.put("address", cu_add.getText().toString());
                                                    co.put("pincode", cu_pinc.getText().toString());
                                                    co.put("dob", cu_dob.getText().toString());
                                                    co.put("user_id", cu_usr_id.getText().toString());
                                                    contentValues.put("cust_account_no", add_bank_no_string);
                                                    contentValues.put("cust_ifsc_code", add_bank_ifsc_string);
                                                    contentValues.put("cust_account_holder_name", add_bank_cust_name_string);
                                                    contentValues.put("cust_bank_name", add_bank_name_string);
                                                    String where = "_id = '" + id + "'";

                                                    contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
                                                    getActivity().getContentResolver().update(contentUri, co,where,new String[]{});
                                                    resultUri = new Uri.Builder()
                                                            .scheme("content")
                                                            .authority(StubProvider.AUTHORITY)
                                                            .path("Customerdetails")
                                                            .appendQueryParameter("operation", "update")
                                                            .appendQueryParameter("_id",id)
                                                            .build();
                                                    getActivity().getContentResolver().notifyChange(resultUri, null);

//                                                    db.update("Customerdetails", co, where, new String[]{});
                                                } while (c3.moveToNext());
                                            }
                                            c3.close();

                                            DownloadMusicfromInternet_new downloadMusicfromInternet_new = new DownloadMusicfromInternet_new();
                                            try {
                                                downloadMusicfromInternet_new.execute();
                                            } catch (Exception e) {
                                                downloadMusicfromInternet_new.cancel(true);
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    usr_id_get.close();
                                }
                            }
                            cv.close();
                        }


                    }
                });

                ImageButton delete = (ImageButton) dialog_cust.findViewById(R.id.delete);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog_confirm = new Dialog(getActivity(), R.style.timepicker_date_dialog);
                        dialog_confirm.setContentView(R.layout.dialog_customer_management_confirm_delete);
                        dialog_confirm.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialog_confirm.show();

                        ImageView no1 = (ImageView) dialog_confirm.findViewById(R.id.closetext);
                        no1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog_confirm.dismiss();
                            }
                        });

                        Button no = (Button) dialog_confirm.findViewById(R.id.cancel);
                        no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog_confirm.dismiss();
                            }
                        });

                        Button yes = (Button) dialog_confirm.findViewById(R.id.ok);
                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Cursor cz = db.rawQuery("SELECT * FROM Customerdetails WHERE phoneno = '"+ph_no+"'", null);
                                if (cz.moveToFirst()){
                                    do {
                                        String id = cz.getString(0);

                                        String where = "_id = '" + id + "' ";


                                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
                                        getActivity().getContentResolver().delete(contentUri, where, new String[]{});
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProvider.AUTHORITY)
                                                .path("Customerdetails")
                                                .appendQueryParameter("operation", "delete")
                                                .appendQueryParameter("_id", id)
                                                .build();
                                        getActivity().getContentResolver().notifyChange(resultUri, null);



                                   //     db.delete("Customerdetails", where, new String[]{});

                                    }while (cz.moveToNext());
                                }
                                cz.close();

                                Cursor cz1 = db.rawQuery("SELECT * FROM Cust_feedback WHERE cust_phoneno = '"+ph_no+"'", null);
                                if (cz1.moveToFirst()){
                                    do {
                                        String id = cz1.getString(0);

                                        String where = "_id = '" + id + "' ";


                                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Cust_feedback");
                                        getActivity().getContentResolver().delete(contentUri, where, new String[]{});
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProvider.AUTHORITY)
                                                .path("Cust_feedback")
                                                .appendQueryParameter("operation", "delete")
                                                .appendQueryParameter("_id", id)
                                                .build();
                                        getActivity().getContentResolver().notifyChange(resultUri, null);


//                                        db.delete("Cust_feedback", where, new String[]{});

                                    }while (cz1.moveToNext());
                                }
                                cz1.close();

                                DownloadMusicfromInternet_new downloadMusicfromInternet_new = new DownloadMusicfromInternet_new();
                                try {
                                    downloadMusicfromInternet_new.execute();
                                } catch (Exception e) {
                                    downloadMusicfromInternet_new.cancel(true);
                                    e.printStackTrace();
                                }

                                dialog_confirm.dismiss();
                                dialog_cust.dismiss();
                            }
                        });
                    }
                });

            }
        });


        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long l) {

//                final Customer_activity_listitems country = (Customer_activity_listitems) parent.getItemAtPosition(position);

                final Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                idd = cursor.getString(cursor.getColumnIndex("user_id"));
                cust_name = cursor.getString(cursor.getColumnIndex("name"));
                cust_phoneeno = cursor.getString(cursor.getColumnIndex("phoneno"));
                cusst_emailid = cursor.getString(cursor.getColumnIndex("emailid"));
                cust_address = cursor.getString(cursor.getColumnIndex("address"));
                cust_pincode = cursor.getString(cursor.getColumnIndex("pincode"));


                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_selectable_list_item);
                arrayAdapter.add("Feedback");
                arrayAdapter.add("Customer history");
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String strName = arrayAdapter.getItem(which);
                        if (strName.toString().equals("Feedback")) {

                            final Dialog dialogs = new Dialog(getActivity(), R.style.timepicker_date_dialog);
                            dialogs.setContentView(R.layout.dialog_customer_management_new_feedback);
                            dialogs.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            dialogs.show();

                            ImageButton close = (ImageButton) dialogs.findViewById(R.id.btncancel);
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogs.dismiss();
                                }
                            });

                            TextView cust_name1 = (TextView) dialogs.findViewById(R.id.cust_name);
                            cust_name1.setText(cust_name);


                            final RatingBar ambience_rating = (RatingBar) dialogs.findViewById(R.id.ratingBar_Ambience);
                            ambience_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                @Override
                                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromuser) {

                                }
                            });

                            final RatingBar prod_qual_rating = (RatingBar) dialogs.findViewById(R.id.ratingBar_prod_qual);
                            prod_qual_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                @Override
                                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromuser) {

                                }
                            });

                            final RatingBar service_rating = (RatingBar) dialogs.findViewById(R.id.ratingBar_service);
                            service_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                @Override
                                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromuser) {

                                }
                            });

                            final RatingBar all_exp_rating = (RatingBar) dialogs.findViewById(R.id.ratingBar_all_exp);
                            all_exp_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                @Override
                                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromuser) {

                                }
                            });

                            final EditText comments = (EditText) dialogs.findViewById(R.id.comment);

                            ImageButton btnsave = (ImageButton) dialogs.findViewById(R.id.btnsave);
                            btnsave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy MM dd");
                                    final String currentDateandTime1 = sdf2.format(new Date());

                                    SimpleDateFormat normal = new SimpleDateFormat("dd MMM yyyy");
                                    final String normal1 = normal.format(new Date());

                                    Date dt = new Date();
                                    SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
                                    final String time1 = sdf1.format(dt);

                                    Date dtt = new Date();
                                    SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
                                    final String time24 = sdf1t.format(dtt);

                                    float am_ra = ambience_rating.getRating();
                                    float pq_ra = prod_qual_rating.getRating();
                                    float ser_ra = service_rating.getRating();
                                    float ove_exp_ra = all_exp_rating.getRating();

                                    if (am_ra > 0 || pq_ra > 0 || ser_ra > 0 || ove_exp_ra > 0){

                                        if (am_ra > 0){
                                            mul1 = ambience_rating.getRating()*5;
                                        }
                                        if (pq_ra > 0){
                                            mul2 = prod_qual_rating.getRating()*5;
                                        }
                                        if ( ser_ra > 0){
                                            mul3 = service_rating.getRating()*5;
                                        }
                                        if (ove_exp_ra > 0){
                                            mul4 = all_exp_rating.getRating()*5;
                                        }

                                        float addmul = mul1+mul2+mul3+mul4;


                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("cust_name", cust_name);
                                        contentValues.put("cust_phoneno", cust_phoneeno);
                                        contentValues.put("date", currentDateandTime1);
                                        contentValues.put("time", time1);
                                        contentValues.put("ambience_rating", am_ra);
                                        contentValues.put("pro_qual_rating", pq_ra);
                                        contentValues.put("service_rating", ser_ra);
                                        contentValues.put("overall_exp_rating", ove_exp_ra);
                                        contentValues.put("comments", comments.getText().toString());
                                        contentValues.put("percentage", String.valueOf(addmul));


                                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Cust_feedback");
                                        resultUri = getActivity().getContentResolver().insert(contentUri, contentValues);
                                        getActivity().getContentResolver().notifyChange(resultUri, null);

                                  //      db.insert("Cust_feedback", null, contentValues);

                                        dialogs.dismiss();

                                        listview.setAdapter(null);

                                        DownloadMusicfromInternet_new downloadMusicfromInternet_new = new DownloadMusicfromInternet_new();
                                        try {
                                            downloadMusicfromInternet_new.execute();
                                        } catch (Exception e) {
                                            downloadMusicfromInternet_new.cancel(true);
                                            e.printStackTrace();
                                        }

                                    }else {
                                        Toast.makeText(getActivity(), "cannot save", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }else {

//                            final Customer_activity_listitems country = (Customer_activity_listitems) parent.getItemAtPosition(position);

                            Cursor cursorff = db.rawQuery("SELECT * FROM Clicked_cust_name", null);
                            if (cursorff.moveToFirst()){
                                String idff = cursorff.getString(0);
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("name", "");
                                String wherecu = "_id = '" + idff + "'";

                                contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Clicked_cust_name");
                                getActivity().getContentResolver().update(contentUri, contentValues,wherecu,new String[]{});
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProvider.AUTHORITY)
                                        .path("Clicked_cust_name")
                                        .appendQueryParameter("operation", "update")
                                        .appendQueryParameter("_id",idff)
                                        .build();
                                getActivity().getContentResolver().notifyChange(resultUri, null);

//                                            db.update("Clicked_cust_name", contentValues, wherecu, new String[]{});

                            }
                            cursorff.close();

                                        Cursor cursorf = db.rawQuery("SELECT * FROM Clicked_cust_name", null);
                                        if (cursorf.moveToFirst()){
                                            String idff = cursorf.getString(0);
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("name", cust_phoneeno);
                                            String wherecu = "_id = '" + idff + "'";

                                            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Clicked_cust_name");
                                            getActivity().getContentResolver().update(contentUri, contentValues,wherecu,new String[]{});
                                            resultUri = new Uri.Builder()
                                                    .scheme("content")
                                                    .authority(StubProvider.AUTHORITY)
                                                    .path("Clicked_cust_name")
                                                    .appendQueryParameter("operation", "update")
                                                    .appendQueryParameter("_id",idff)
                                                    .build();
                                            getActivity().getContentResolver().notifyChange(resultUri, null);

//                                            db.update("Clicked_cust_name", contentValues, wherecu, new String[]{});

                            }
                            cursorf.close();
                            Intent intent = new Intent(getActivity(), Customer_Info_Activity.class);
                            startActivity(intent);

//                            if (databaseExist()) {
//                                db_inapp = getActivity().openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
//
//                                db_inapp.execSQL("CREATE TABLE if not exists Pro_upgrade (_id integer PRIMARY KEY UNIQUE, status text, orderid text);");
//                                Cursor cursor1_1 = db_inapp.rawQuery("SELECT * FROM Pro_upgrade", null);
//                                if (cursor1_1.moveToFirst()){
//
//                                }else {
//                                    ContentValues cv = new ContentValues();
//                                    cv.put("status", "Not Activated");
//                                    db_inapp.insert("Pro_upgrade", null, cv);
//                                }
//
//                                Cursor cursor1 = db_inapp.rawQuery("SELECT * FROM Pro_upgrade", null);
//                                if (cursor1.moveToFirst()) {
//                                    String st = cursor1.getString(1);
//
//                                    TextView tv = new TextView(getActivity());
//                                    tv.setText(st);
//
//                                    if (tv.getText().toString().equals("Activated")) {
//                                        final Customer_activity_listitems country = (Customer_activity_listitems) parent.getItemAtPosition(position);
//
//                                        Cursor cursorff = db.rawQuery("SELECT * FROM Clicked_cust_name", null);
//                                        if (cursorff.moveToFirst()){
//                                            String idff = cursorff.getString(0);
//                                            ContentValues contentValues = new ContentValues();
//                                            contentValues.put("name", "");
//                                            String wherecu = "_id = '" + idff + "'";
//                                            db.update("Clicked_cust_name", contentValues, wherecu, new String[]{});
//
//                                        }
//                                        cursorff.close();
//
//                                        Cursor cursorf = db.rawQuery("SELECT * FROM Clicked_cust_name", null);
//                                        if (cursorf.moveToFirst()){
//                                            String idff = cursorf.getString(0);
//                                            ContentValues contentValues = new ContentValues();
//                                            contentValues.put("name", country.getphone());
//                                            String wherecu = "_id = '" + idff + "'";
//                                            db.update("Clicked_cust_name", contentValues, wherecu, new String[]{});
//
//                                        }
//                                        cursorf.close();
//                                        Intent intent = new Intent(getActivity(), Customer_Info_Activity.class);
//                                        startActivity(intent);
//                                    } else {
//                                        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(getActivity());
//                                        builder1.setTitle(getString(R.string.title7));
//                                        builder1.setMessage(getString(R.string.setmessage1));
//                                        builder1.setCancelable(true);
//
//                                        builder1.setPositiveButton(
//                                                "OK",
//                                                new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int id) {
//                                                        dialog.cancel();
//                                                    }
//                                                });
//
//                                        builder1.setNegativeButton(
//                                                "Buy now",
//                                                new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int id) {
////                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=1hIGJVeLjgI&index=2&list=PL6YNztMURCKTRsqbTbqFN10yDhrthtSY3")));
//                                                        Log.d(TAG, "Buy gas button clicked.");
//
//                                                        CharSequence[] options;
//
//                                                        options = new CharSequence[1];
//                                                        options[0] = getString(R.string.subscription_period_monthly11);
//
//                                                        int titleResId;
//
//                                                        titleResId = R.string.subscription_period_prompt;
//
//                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                                                        builder.setTitle(titleResId)
//                                                                .setSingleChoiceItems(options, 0 /* checkedItem */,CustomerActivity.this)
//                                                                .setPositiveButton(R.string.subscription_prompt_continue, CustomerActivity.this)
//                                                                .setNegativeButton(R.string.subscription_prompt_cancel, CustomerActivity.this);
//                                                        AlertDialog dialog1 = builder.create();
//                                                        dialog1.show();
//
//                                                        // launch the gas purchase UI flow.
//                                                        // We will be notified of completion via mPurchaseFinishedListener
//                                                      //  setWaitScreen(true);
//                                                        Log.d(TAG, "Launching purchase flow for gas.");
//
//                                                        /* TODO: for security, generate your payload here for verification. See the comments on
//                                                         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
//                                                         *        an empty string, but on a production app you should carefully generate this. */
//                                                        String payload = "";
//                                                    }
//                                                });
//
//                                        builder1.setNeutralButton(
//                                                "Know more",
//                                                new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int id) {
//                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=1hIGJVeLjgI&index=2&list=PL6YNztMURCKTRsqbTbqFN10yDhrthtSY3")));
//                                                    }
//                                                });
//
//                                        android.support.v7.app.AlertDialog alert11 = builder1.create();
//                                        alert11.show();
//                                    }
//                                } else {
//                                    android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(getActivity());
//                                    builder1.setTitle(getString(R.string.title7));
//                                    builder1.setMessage(getString(R.string.setmessage1));
//                                    builder1.setCancelable(true);
//
//                                    builder1.setPositiveButton(
//                                            "OK",
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    dialog.cancel();
//                                                }
//                                            });
//
//                                    builder1.setNegativeButton(
//                                            "Buy now",
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
////                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=1hIGJVeLjgI&index=2&list=PL6YNztMURCKTRsqbTbqFN10yDhrthtSY3")));
//                                                    Log.d(TAG, "Buy gas button clicked.");
//
//                                                    CharSequence[] options;
//                                                    options = new CharSequence[1];
//                                                    options[0] = getString(R.string.subscription_period_monthly11);
//
//                                                    int titleResId;
//                                                    titleResId = R.string.subscription_period_prompt;
//
//
//                                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                                                    builder.setTitle(titleResId)
//                                                            .setSingleChoiceItems(options, 0 /* checkedItem */, CustomerActivity.this)
//                                                            .setPositiveButton(R.string.subscription_prompt_continue, CustomerActivity.this)
//                                                            .setNegativeButton(R.string.subscription_prompt_cancel, CustomerActivity.this);
//                                                    AlertDialog dialog1 = builder.create();
//                                                    dialog1.show();
//
//                                                    // launch the gas purchase UI flow.
//                                                    // We will be notified of completion via mPurchaseFinishedListener
//                                                   // setWaitScreen(true);
//                                                    Log.d(TAG, "Launching purchase flow for gas.");
//
//                                                    /* TODO: for security, generate your payload here for verification. See the comments on
//                                                     *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
//                                                     *        an empty string, but on a production app you should carefully generate this. */
//                                                    String payload = "";
//                                                }
//                                            });
//
//                                    builder1.setNeutralButton(
//                                            "Know more",
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=1hIGJVeLjgI&index=2&list=PL6YNztMURCKTRsqbTbqFN10yDhrthtSY3")));
//                                                }
//                                            });
//
//                                    android.support.v7.app.AlertDialog alert11 = builder1.create();
//                                    alert11.show();
//                                }
//                            }else {
//                                android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(getActivity());
//                                builder1.setTitle(getString(R.string.title7));
//                                builder1.setMessage(getString(R.string.setmessage1));
//                                builder1.setCancelable(true);
//
//                                builder1.setPositiveButton(
//                                        "OK",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                dialog.cancel();
//                                            }
//                                        });
//
//                                builder1.setNegativeButton(
//                                        "Buy now",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
////                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=1hIGJVeLjgI&index=2&list=PL6YNztMURCKTRsqbTbqFN10yDhrthtSY3")));
//                                                Log.d(TAG, "Buy gas button clicked.");
//
//                                                CharSequence[] options;
//
//                                                options = new CharSequence[1];
//                                                options[0] = getString(R.string.subscription_period_monthly11);
//                                                options = new CharSequence[4];
//
//                                                int titleResId;
//                                                titleResId = R.string.subscription_period_prompt;
//
//
//                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                                                builder.setTitle(titleResId)
//                                                        .setSingleChoiceItems(options, 0 /* checkedItem */,CustomerActivity.this)
//                                                        .setPositiveButton(R.string.subscription_prompt_continue,CustomerActivity.this)
//                                                        .setNegativeButton(R.string.subscription_prompt_cancel, CustomerActivity.this);
//                                                AlertDialog dialog1 = builder.create();
//                                                dialog1.show();
//
//                                                // launch the gas purchase UI flow.
//                                                // We will be notified of completion via mPurchaseFinishedListener
//                                              //  setWaitScreen(true);
//                                                Log.d(TAG, "Launching purchase flow for gas.");
//
//                                                /* TODO: for security, generate your payload here for verification. See the comments on
//                                                 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
//                                                 *        an empty string, but on a production app you should carefully generate this. */
//                                                String payload = "";
//                                            }
//                                        });
//
//                                builder1.setNeutralButton(
//                                        "Know more",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=1hIGJVeLjgI&index=2&list=PL6YNztMURCKTRsqbTbqFN10yDhrthtSY3")));
//                                            }
//                                        });
//
//                                android.support.v7.app.AlertDialog alert11 = builder1.create();
//                                alert11.show();
//                            }
                        }
                    }
                });

                alertDialog.show();


                return true;
            }
        });


//        FloatingActionButton add_cust = (FloatingActionButton) rootview.findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog_cust = new Dialog(getActivity(), R.style.timepicker_date_dialog);
                dialog_cust.setContentView(R.layout.dialog_customerlist_new);

                add_cust_name = (EditText) dialog_cust.findViewById(R.id.nameedit);
                add_cust_phoneno = (EditText) dialog_cust.findViewById(R.id.phonenoedit);
                add_cust_emailid = (EditText) dialog_cust.findViewById(R.id.emailidedit);
                add_cust_address = (EditText) dialog_cust.findViewById(R.id.addressedit);
                add_cust_pincode = (EditText) dialog_cust.findViewById(R.id.pincodeedit);
                add_cust_dob = (TextView) dialog_cust.findViewById(R.id.dobedit);
                add_cust_userid = (EditText) dialog_cust.findViewById(R.id.cust_id_edit);

                add_cust_dob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar now = Calendar.getInstance();
                        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                                datePickerListener,now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                        );

                        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                    }

                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener datePickerListener
                            = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog, int selectedYear1, int selectedMonth1, int selectedDay1) {
                            year1 = selectedYear1;
                            month1 = selectedMonth1;
                            day1 = selectedDay1;
                            populateSetDate(year1, month1 + 1, day1);
                        }
                    };

                    public void populateSetDate(int year, int month, int day) {
                        TextView mEdit1 = (TextView) dialog_cust.findViewById(R.id.dobedit);

                        if (month == 1 && day < 10) {
                            onee1 = "0" + day + " " + "Jan" + " " + year;
                            mEdit1.setText(onee1);
                        } else {
                            if (month == 1) {
                                onee = day + " " + "Jan" + " " + year;
                                mEdit1.setText(onee);
                            }
                        }

                        if (month == 2 && day < 10) {
                            two1 = "0" + day + " " + "Feb" + " " + year;
                            mEdit1.setText(two1);
                        } else {
                            if (month == 2) {
                                two = day + " " + "Feb" + " " + year;
                                mEdit1.setText(two);
                            }
                        }

                        if (month == 3 && day < 10) {
                            three1 = "0" + day + " " + "Mar" + " " + year;
                            mEdit1.setText(three1);
                        } else {
                            if (month == 3) {
                                three = day + " " + "Mar" + " " + year;
                                mEdit1.setText(three);
                            }
                        }

                        if (month == 4 && day < 10) {
                            four1 = "0" + day + " " + "Apr" + " " + year;
                            mEdit1.setText(four1);
                        } else {
                            if (month == 4) {
                                four = day + " " + "Apr" + " " + year;
                                mEdit1.setText(four);
                            }
                        }

                        if (month == 5 && day < 10) {
                            five1 = "0" + day + " " + "May" + " " + year;
                            mEdit1.setText(five1);
                        } else {
                            if (month == 5) {
                                five = day + " " + "May" + " " + year;
                                mEdit1.setText(five);
                            }
                        }

                        if (month == 6 && day < 10) {
                            six1 = "0" + day + " " + "Jun" + " " + year;
                            mEdit1.setText(six1);
                        } else {
                            if (month == 6) {
                                six = day + " " + "Jun" + " " + year;
                                mEdit1.setText(six);
                            }
                        }

                        if (month == 7 && day < 10) {
                            seven1 = "0" + day + " " + "Jul" + " " + year;
                            mEdit1.setText(seven1);
                        } else {
                            if (month == 7) {
                                seven = day + " " + "Jul" + " " + year;
                                mEdit1.setText(seven);
                            }
                        }

                        if (month == 8 && day < 10) {
                            eight1 = "0" + day + " " + "Aug" + " " + year;
                            mEdit1.setText(eight1);
                        } else {
                            if (month == 8) {
                                eight = day + " " + "Aug" + " " + year;
                                mEdit1.setText(eight);
                            }
                        }

                        if (month == 9 && day < 10) {
                            nine1 = "0" + day + " " + "Sep" + " " + year;
                            mEdit1.setText(nine1);
                        } else {
                            if (month == 9) {
                                nine = day + " " + "Sep" + " " + year;
                                mEdit1.setText(nine);
                            }
                        }

                        if (month == 10 && day < 10) {
                            ten1 = "0" + day + " " + "Oct" + " " + year;
                            mEdit1.setText(ten1);
                        } else {
                            if (month == 10) {
                                ten = day + " " + "Oct" + " " + year;
                                mEdit1.setText(ten);
                            }
                        }

                        if (month == 11 && day < 10) {
                            eleven1 = "0" + day + " " + "Nov" + " " + year;
                            mEdit1.setText(eleven1);
                        } else {
                            if (month == 11) {
                                eleven = day + " " + "Nov" + " " + year;
                                mEdit1.setText(eleven);
                            }
                        }

                        if (month == 12 && day < 10) {
                            twelve1 = "0" + day + " " + "Dec" + " " + year;
                            mEdit1.setText(twelve1);
                        } else {
                            if (month == 12) {
                                twelve = day + " " + "Dec" + " " + year;
                                mEdit1.setText(twelve);
                            }
                        }

                    }

                });

                btn_close = (ImageButton) dialog_cust.findViewById(R.id.canceledit);
                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_cust.dismiss();
                    }
                });

                btn_save = (ImageButton) dialog_cust.findViewById(R.id.okedit);
                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        savenew(dialog_cust);
                        Cursor one = db.rawQuery("SELECT * FROM Customerdetails GROUP BY phoneno", null);
                        int cou = one.getCount();
                        total1 = String.valueOf(cou);
                        countget.setText(total1);
                    }
                });

                dialog_cust.show();
            }
        });


        mHelperPro = new IabHelper(getActivity(),  Constants.base64EncodedPublicKey);
        mHelperPro.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result)
            {
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app Billing setup failed: " +
                            result);
                } else {
                    Log.d(TAG, "In-app Billing is set up OK");
                    try {
                        mHelperPro.queryInventoryAsync(mReceivedInventoryListenerPro);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        return rootview;
    }



    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListenerPro
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {


            if (result.isFailure()) {
                // Handle failure
            } else {

                if(mSelectedProSubscription.equalsIgnoreCase("")){

                    List<Purchase> list=inventory.getAllPurchases();

                    for(int k=0;k<list.size();k++){
                        Purchase mPurchase = list.get(k);
                        if (mPurchase != null && verifyDeveloperPayload(mPurchase)) {
                            Log.d(TAG, "We have gas. Consuming it.");
                            String token=mPurchase.getSku();
                            if((token.equalsIgnoreCase(SKU_DELAROY_PRO_UPGRADE))){

                                try {
                                    mHelperPro.consumeAsync(mPurchase,
                                            mConsumeFinishedListenerPro);
                                } catch (IabHelper.IabAsyncInProgressException e) {
                                    e.printStackTrace();
                                }

                            }

                            return;
                        }

                    }


                }else{
                    Purchase purchase = inventory.getPurchase(mSelectedProSubscription);

                    try {
                        mHelperPro.consumeAsync(inventory.getPurchase(mSelectedProSubscription),
                                mConsumeFinishedListenerPro);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                    }
                }


            }
        }
    };


    IabHelper.OnConsumeFinishedListener mConsumeFinishedListenerPro =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {

                        if(mSelectedProSubscription.equalsIgnoreCase("")){

                        }else{
                            String orderid=purchase.getOrderId();
                            updateProCloud(orderid);
                        }


                    } else {
                        // handle error
                    }
                }
            };


    private void updateProCloud(String orderid) {


        Cursor cursor_pro=db_inapp.rawQuery("SELECT * FROM Pro_upgrade", null);
        if(cursor_pro.moveToFirst()){

            int id=cursor_pro.getInt(0);
            String status=cursor_pro.getString(1);
            if(!status.equalsIgnoreCase("Activated")){
                ContentValues contentValues =new ContentValues();
                contentValues.put("status","Activated");
                contentValues.put("orderid",orderid);
                String where = "_id = '" + id + "'";
                db_inapp.update("Pro_upgrade", contentValues, where, new String[]{});
            }

        }else{

            ContentValues contentValues =new ContentValues();
            contentValues.put("status","Activated");
            contentValues.put("orderid",orderid);
            db_inapp.insert("Pro_upgrade", null, contentValues);
        }
        cursor_pro.close();

        prolicense(orderid);
    }


    public void prolicense(final String orderid){

        SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(getActivity());
        final String company= sharedpreferences.getString("companyname", null);
        final String store= sharedpreferences.getString("storename", null);
        final String device= sharedpreferences.getString("devicename", null);

        bar.setVisibility(View.VISIBLE);
        requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"propurchase.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")){

                            bar.setVisibility(View.GONE);
                            updateUi();

                        }else{

                            bar.setVisibility(View.GONE);
                            updateUi();
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
                params.put("orderid",orderid);
                params.put("subscription",mSelectedProSubscription);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.

        inflater.inflate(R.menu.customer_activity_menu, menu);

        searchItem = menu.findItem(R.id.action_def_credit);
//        searchItem.

        Cursor allrows = db1.rawQuery("SELECT * FROM Default_credit WHERE _id = '1'", null);
        if (allrows.moveToFirst()) {
            do {
                menu_credit_stauts = allrows.getString(1);
            } while (allrows.moveToNext());
        }
        allrows.close();


        if (menu_credit_stauts.toString().equals("off")){
            searchItem.setChecked(false);
        }else {
            searchItem.setChecked(true);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){


            case R.id.action_def_credit:
                if (searchItem.isChecked()){
                    searchItem.setChecked(false);
                    Cursor cursor = db1.rawQuery("SELECT * FROM Default_credit WHERE _id = '1'", null);
                    if (cursor.moveToFirst()){
                        do {
                            String st = cursor.getString(1);
                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("status", "off");
                            String where = "_id = '1' ";

                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Default_credit");
                            getActivity().getContentResolver().update(contentUri, contentValues1,where,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Default_credit")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id","1")
                                    .build();
                            getActivity().getContentResolver().notifyChange(resultUri, null);


//                            db1.update("Default_credit", contentValues1, where, new String[]{});
                        }while (cursor.moveToNext());
                    }
                    cursor.close();
                }else {
                    searchItem.setChecked(true);
                    Cursor cursor = db1.rawQuery("SELECT * FROM Default_credit WHERE _id = '1'", null);
                    if (cursor.moveToFirst()){
                        do {
                            String st = cursor.getString(1);
                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("status", "on");
                            String where = "_id = '1' ";

                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Default_credit");
                            getActivity().getContentResolver().update(contentUri, contentValues1,where,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Default_credit")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id","1")
                                    .build();
                            getActivity().getContentResolver().notifyChange(resultUri, null);


//                            db1.update("Default_credit", contentValues1, where, new String[]{});
                        }while (cursor.moveToNext());
                    }
                    cursor.close();
                }


                break;
            case R.id.action_export:

                if (dataAdapterr.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.no_report_to_export), Toast.LENGTH_SHORT).show();
                }else {
                    sdff2 = new SimpleDateFormat("ddMMMyy");
                    currentDateandTimee1 = sdff2.format(new Date());

                    Date dt = new Date();
                    sdff1 = new SimpleDateFormat("hhmmssaa");
                    timee1 = sdff1.format(dt);

                    ExportDatabaseCSVTask task = new ExportDatabaseCSVTask();
                    task.execute();
                }
                break;


            case R.id.action_exportmail:

                Cursor curCSV0 = db.rawQuery("SELECT SUM(total) FROM Billnumber", null);
                if (curCSV0.moveToFirst()) {
                    do {
                        float aq = curCSV0.getFloat(0);
                        sumnew = String.valueOf(aq);
                    }
                    while (curCSV0.moveToNext());
                }
                curCSV0.close();

                Cursor curCSV3 = db.rawQuery("SELECT SUM(credit) FROM Customerdetails", null);
                if (curCSV3.moveToFirst()) {
                    do {
                        float aq = curCSV3.getFloat(0);
                        sumnew3 = String.valueOf(aq);
                    }
                    while (curCSV3.moveToNext());
                }
                curCSV3.close();

                Cursor curCSV4 = db.rawQuery("SELECT SUM(deposit) FROM Customerdetails", null);
                if (curCSV4.moveToFirst()) {
                    do {
                        float aq = curCSV4.getFloat(0);
                        sumnew4 = String.valueOf(aq);
                    }
                    while (curCSV4.moveToNext());
                }
                curCSV4.close();

                Cursor curCSV5 = db.rawQuery("SELECT SUM(cashout) FROM Customerdetails", null);
                if (curCSV5.moveToFirst()) {
                    do {
                        float aq = curCSV5.getFloat(0);
                        sumnew5 = String.valueOf(aq);
                    }
                    while (curCSV5.moveToNext());
                }
                curCSV5.close();

                Cursor curCSV6 = db.rawQuery("SELECT SUM(cashout) FROM Customerdetails", null);
                if (curCSV6.moveToFirst()) {
                    do {
                        float aq = curCSV6.getFloat(0);
                        sumnew5 = String.valueOf(aq);
                    }
                    while (curCSV6.moveToNext());
                }
                curCSV6.close();

                float v = (Float.parseFloat(sumnew) - Float.parseFloat(sumnew3)) + (Float.parseFloat(sumnew4) - Float.parseFloat(sumnew5));
                String v1_profit1 = String.format("%.1f", v);

                hideKeyboard(getContext());
                donotshowKeyboard(getActivity());

                if (dataAdapterr.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.no_report_to_mail), Toast.LENGTH_SHORT).show();
                }else {
                    sdff2 = new SimpleDateFormat("ddMMMyy");
                    currentDateandTimee1 = sdff2.format(new Date());

                    Date dt1 = new Date();
                    sdff1 = new SimpleDateFormat("hhmmssaa");
                    timee1 = sdff1.format(dt1);

                    db1 = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor cursor = db1.rawQuery("SELECT * FROM Companydetailss", null);
                    if (cursor.moveToFirst()) {
                        companynameis = cursor.getString(1);
                    }else {
                        companynameis = "";
                    }
                    cursor.close();

                    File dbFile = getActivity().getDatabasePath("mydb_Salesdata");

                    Cursor ccursore = db1.rawQuery("SELECT * FROM Email_setup", null);
                    if (ccursore.moveToFirst()) {
                        Cursor ccursoree = db1.rawQuery("SELECT * FROM Email_recipient", null);
                        if (ccursoree.moveToFirst()) {
//                            File exportDir = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_reports/IVEPOS_customer_list");
                            File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_reports/IVEPOS_customer_list");
                            if (!exportDir.exists()) {
                                exportDir.mkdirs();
                            }

                            file = new File(exportDir, "IvePOS_customer_list" + currentDateandTimee1 + "_" + timee1 + ".csv");
//                            try {

                                ExportDatabaseCSVTask task = new ExportDatabaseCSVTask();
                                task.execute();

//                                db.execSQL("UPDATE Customerdetails SET total_amount = rupees WHERE refunds = '' OR refunds IS NULL");
//
//
//
//                                db.execSQL("delete from Cusotmer_activity_temp");
//
//                                Cursor cursorq = db.rawQuery("SELECT * FROM Customerdetails GROUP BY phoneno", null);
//                                if (cursorq.moveToFirst()){
//                                    do {
//                                        String pn = cursorq.getString(2);
//                                        String id = cursorq.getString(0);
//                                        Cursor c1 = db.rawQuery("SELECT SUM(rupees) FROM Customerdetails WHERE phoneno = '"+pn+"'", null);
//                                        if (c1.moveToFirst()){
//                                            do {
//                                                float aq = c1.getFloat(0);
//                                                aqq = String.valueOf(aq);
//                                            }while (c1.moveToNext());
//                                        }
//                                        c1.close();
//                                        ContentValues contentValues = new ContentValues();
//                                        contentValues.put("total", aqq);
//                                        String where = "_id = '" + id + "' ";
//                                        System.out.println("sum of rupees "+aqq+" _id "+id);
//
//                                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
//                                        getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                                        resultUri = new Uri.Builder()
//                                                .scheme("content")
//                                                .authority(StubProvider.AUTHORITY)
//                                                .path("Customerdetails")
//                                                .appendQueryParameter("operation", "update")
//                                                .appendQueryParameter("_id",id)
//                                                .build();
//                                        getActivity().getContentResolver().notifyChange(resultUri, null);
//
////                        db.update("Customerdetails", contentValues, where, new String[]{});
//
//                                    }
//                                    while (cursorq.moveToNext());
//                                }
//                                cursorq.close();
//
//                                db.execSQL("UPDATE Clicked_cust_name SET name = ''");
//
//                                db.execSQL("delete from Cusotmer_activity_temp");
//
//                                Cursor cursorv = db.rawQuery("Select * from Customerdetails GROUP BY phoneno ", null);
//                                if (cursorv.moveToFirst()){
//                                    do {
//                                        String id = cursorv.getString(0);
//                                        String phon = cursorv.getString(2);
//                                        String name = cursorv.getString(1);
//                                        String email = cursorv.getString(3);
//                                        String addr = cursorv.getString(4);
//                                        String cus_id = cursorv.getString(37);
//                                        String rop = "0";
////                                        String rop1 = "0";
//                                        String rope = "";
////
//                                        ContentValues contentValues = new ContentValues();
////
//                                        contentValues.put("_id", id);
//                                        contentValues.put("name", name);
//                                        contentValues.put("phone_no", phon);
//                                        contentValues.put("email", email);
//                                        contentValues.put("addr", addr);
//                                        contentValues.put("cust_id", cus_id);
//
//                                        Cursor cursor2 = db.rawQuery("Select SUM(total_amount) from Customerdetails WHERE phoneno = '"+phon+"' ", null);
//                                        if (cursor2.moveToFirst()){
//                                            float dsirsq = cursor2.getFloat(0);
//                                            rop = String.format("%.1f", dsirsq);
//
//                                            contentValues.put("total_amount", rop);
//                                        }
//                                        cursor2.close();
//
////                                        Cursor sales_t = db.rawQuery("SELECT SUM(total_amount) FROM Customerdetails WHERE phoneno = '"+phon+"'", null);
////                                        if (sales_t.moveToFirst()){
////                                            dsirsq1 = sales_t.getFloat(0);
////                                        }
////                                        sales_t.close();
//
//                                        Cursor credit_t = db.rawQuery("SELECT SUM(credit) FROM Customerdetails WHERE phoneno = '"+phon+"'", null);
//                                        if (credit_t.moveToFirst()){
//                                            dsirsq2 = credit_t.getFloat(0);
//                                        }
//                                        credit_t.close();
//
//                                        Cursor deposi_t = db.rawQuery("SELECT SUM(deposit) FROM Customerdetails WHERE phoneno = '"+phon+"'", null);
//                                        if (deposi_t.moveToFirst()){
//                                            dsirsq3 = deposi_t.getFloat(0);
//                                        }
//                                        deposi_t.close();
//
//                                        Cursor cashout_t = db.rawQuery("SELECT SUM(cashout) FROM Customerdetails WHERE phoneno = '"+phon+"'", null);
//                                        if (cashout_t.moveToFirst()){
//                                            dsirsq4 = cashout_t.getFloat(0);
//                                        }
//                                        cashout_t.close();
//
//                                        final float cal_sale = ((dsirsq4 + dsirsq2) - dsirsq3);
//                                        String rop1 = String.format("%.1f", cal_sale);
//
//                                        contentValues.put("balance", rop1);
//
//                                        Cursor set21w = db.rawQuery("SELECT * FROM Customerdetails WHERE phoneno = '"+phon+"'", null);
//                                        if (set21w.moveToFirst()){
//                                            dis_val = set21w.getString(29);
//                                            dis_status = set21w.getString(27);
//                                            dis_ty = set21w.getString(30);
//                                        }
//                                        set21w.close();
//
//                                        TextView tv6 = new TextView(getActivity());
//                                        tv6.setText(dis_ty);
//
//                                        if (tv6.getText().toString().equals("") || tv6.getText().toString().equals("off")){
//
//                                        }else {
//                                            //save value
//                                            contentValues.put("discount_value", dis_val);
//                                            contentValues.put("discount_type", dis_ty);
//
//                                        }
//
//                                        Cursor rating_cursor = db.rawQuery("SELECT SUM(percentage) FROM Cust_feedback WHERE cust_phoneno = '"+phon+"'", null);
//                                        if (rating_cursor.moveToFirst()){
//                                            float dsirsq = rating_cursor.getFloat(0);
//                                            ropq = String.format("%.1f", dsirsq);
//                                        }
//                                        rating_cursor.close();
//
//                                        Cursor rating_cursor1 = db.rawQuery("SELECT * FROM Cust_feedback WHERE cust_phoneno = '"+phon+"'", null);
//                                        int coun = rating_cursor1.getCount();
//
//                                        if (ropq.toString().equals("") || coun == 0){
//
//                                        }else {
//                                            float divw = Float.parseFloat(ropq) / coun;
//                                            rope = String.format("%.1f", divw);
//                                            contentValues.put("approval_rate", rope);
//                                        }
//                                        rating_cursor1.close();
//
////                                        Customer_activity_listitems NAME = new Customer_activity_listitems(cus_id, name, phon, email, addr, rop, rop1, dis_val, dis_ty, rope);
////                                        countryList.add(NAME);
//
//                                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Cusotmer_activity_temp");
//                                        resultUri = getActivity().getContentResolver().insert(contentUri, contentValues);
//                                        getActivity().getContentResolver().notifyChange(resultUri, null);
//
//                                        //   db.insert("Cusotmer_activity_temp", null, contentValues);
//
//                                    }while (cursorv.moveToNext());
//                                }
//                                cursorv.close();
//
//                                file.createNewFile();
//                                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
//                                // this is the Column of the table and same for Header of CSV file
//                                String arrStr1[] = {"Name", "Phone no.", "E-mail", "Total("+insert1_rs+")", "Address", "Balance", "Discount value", "unit", "Approval rating"};
//                                csvWrite.writeNext(arrStr1);
//
//                                db = getActivity().openOrCreateDatabase("mydb_Salesdata",
//                                        Context.MODE_PRIVATE, null);
//                                Cursor curCSV = db.rawQuery("SELECT * FROM Cusotmer_activity_temp ORDER BY _id DESC", null);
//                                while (curCSV.moveToNext()) {
//                                    String arrStr[] = {curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(5), curCSV.getString(4),
//                                            curCSV.getString(6), curCSV.getString(7), curCSV.getString(9), curCSV.getString(10)};
//                                    csvWrite.writeNext(arrStr);
//
//                                }
//
//                                csvWrite.close();


//                            } catch (IOException e) {
//                                Log.e("MainActivity", e.getMessage(), e);
//
//
//                            }

                            Uri u1 = null;
                            u1 = Uri.fromFile(file);
                        }
                        ccursoree.close();
                    }
                    ccursore.close();



                    String url = "www.intuitionsoftwares.com";

                    String msg = "Customer list (list attached)\n\n" +
                            "No. of customers: " + countget.getText().toString() + "\n\n"+
                            "All time Sale: " + sumnew + "\n\n" +
                            "Cash sale: "+insert1_rs + total_l1 + "\n\n" +
                            "Card sale: "+insert1_rs + total_l3 + "\n\n" +
                            "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
                            "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
                            "Powered by: " + Uri.parse(url);

                    Cursor cursor11 = db1.rawQuery("SELECT * FROM Email_recipient", null);
                    if (cursor11.moveToFirst()) {
                        do {
                            String unn = cursor11.getString(3);
                            String toEmails = unn;
                            toEmailList = Arrays.asList(toEmails
                                    .split("\\s*,\\s*"));
                        } while (cursor11.moveToNext());
                    }
                    cursor11.close();

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
                                if (em_ca.toString().equals("Gmail")) {
                                    getResultsFromApi();
                                    new MakeRequestTask(mCredential).execute();
                                }else {
                                    if (em_ca.toString().equals("Yahoo")){
//                                        Toast.makeText(getActivity(), "yahoo "+un, Toast.LENGTH_LONG).show();
                                        Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
                                        if (cursor1.moveToFirst()) {
                                            do {
                                                String unn = cursor1.getString(3);
                                                String toEmails = unn;
                                                toEmailList = Arrays.asList(toEmails
                                                        .split("\\s*,\\s*"));
                                                new SendMailTask_Yahoo_attachment_Customerlist(getActivity()).execute(un,
                                                        pwd, toEmailList, companynameis, msg, currentDateandTimee1, timee1);
                                            } while (cursor1.moveToNext());
                                        }
                                        cursor1.close();


                                    }else {
                                        if (em_ca.toString().equals("Hotmail")){
//                                            Toast.makeText(getActivity(), "Hotmail and Outlook "+un, Toast.LENGTH_LONG).show();
                                            Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
                                            if (cursor1.moveToFirst()) {
                                                do {
                                                    String unn = cursor1.getString(3);
                                                    String toEmails = unn;
                                                    toEmailList = Arrays.asList(toEmails
                                                            .split("\\s*,\\s*"));
                                                    new SendMailTask_Hotmail_Outlook_attachment_Customerlist(getActivity()).execute(un,
                                                            pwd, toEmailList, companynameis, msg, currentDateandTimee1, timee1);
                                                } while (cursor1.moveToNext());
                                            }
                                            cursor1.close();
                                        }else {
                                            if (em_ca.toString().equals("Office365")) {
//                                                Toast.makeText(getActivity(), "office 365 " + un, Toast.LENGTH_LONG).show();
                                                Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
                                                if (cursor1.moveToFirst()) {
                                                    do {
                                                        String unn = cursor1.getString(3);
                                                        String toEmails = unn;
                                                        toEmailList = Arrays.asList(toEmails
                                                                .split("\\s*,\\s*"));
                                                        new SendMailTask_Office365_attachment_Customerlist(getActivity()).execute(un,
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
                            final Dialog dialoge = new Dialog(getActivity(), R.style.timepicker_date_dialog);
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
                                    Intent intent = new Intent(getActivity(), EmailSetup_Recipients.class);
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
                                    Intent intent = new Intent(getActivity(), EmailSetup_Recipients.class);
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
                            final Dialog dialoge = new Dialog(getActivity(), R.style.timepicker_date_dialog);
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
                                    Intent intent = new Intent(getActivity(), EmailSetup.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    dialoge.dismiss();
                                }
                            });

                            Button gotosettings1 = (Button) dialoge.findViewById(R.id.gotosettings1);
                            gotosettings1.setVisibility(View.GONE);
                            gotosettings1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), EmailSetup.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    dialoge.dismiss();
                                }
                            });

                        }else {
                            //both recipient and sender not there
                            final Dialog dialoge = new Dialog(getActivity(), R.style.timepicker_date_dialog);
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
                                    Intent intent = new Intent(getActivity(), EmailSetup.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    dialoge.dismiss();
                                }
                            });

                            Button gotosettings1 = (Button) dialoge.findViewById(R.id.gotosettings1);
                            gotosettings1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), EmailSetup_Recipients.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    dialoge.dismiss();
                                }
                            });

                        }
                        cursoree.close();
                    }
                    cursore.close();

                }

                break;

            case R.id.action_promotions:


//                TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);  //gets the current TelephonyManager
//                if (tm.getSimState() != TelephonyManager.SIM_STATE_UNKNOWN){

                    final Dialog dialog_sms = new Dialog(getActivity(), R.style.notitle);
                    dialog_sms.setContentView(R.layout.dialog_customerlist_sms);
                    dialog_sms.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


                    TextView sms_length = (TextView) dialog_sms.findViewById(R.id.sms_length);
                    msg_body = (EditText) dialog_sms.findViewById(R.id.messageedit);


                    msg_body.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {
                        }

                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        public void onTextChanged(CharSequence s, int start, int before, int count) {
//                            sms_length.setText(msg_body.getText().toString().length());
                            sms_length.setText(String.valueOf(s.length()));
                        }
                    });

                    ImageView btn_save = (ImageView) dialog_sms.findViewById(R.id.email);
                    btn_save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendemail();
                        }
                    });

                    ImageView close = (ImageView) dialog_sms.findViewById(R.id.closetext);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_sms.dismiss();
                        }
                    });

                    ImageView btn_close = (ImageView) dialog_sms.findViewById(R.id.sms);
                    btn_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendSMS();
                        }
                    });


                    ImageView whatsapp = (ImageView) dialog_sms.findViewById(R.id.whatsapp);
                    whatsapp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                            whatsappIntent.setType("text/plain");
                            whatsappIntent.putExtra(Intent.EXTRA_TEXT, msg_body.getText().toString());
                            try {
                                startActivity(Intent.createChooser(whatsappIntent, "Share using"));
                            } catch (ActivityNotFoundException ex) {
                            }
                        }
                    });

                    dialog_sms.show();
//                } else {
//                    Toast.makeText(getActivity(), "no Sim option there", Toast.LENGTH_SHORT).show();
//                    final Dialog dialog_sms = new Dialog(getActivity(), R.style.notitle);
//                    dialog_sms.setContentView(R.layout.dialog_customerlist_sms);
//                    dialog_sms.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//
//                    ImageView btn_save = (ImageView) dialog_sms.findViewById(R.id.email);
//                    btn_save.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            sendemail();
//                        }
//                    });
//
//                    ImageView close = (ImageView) dialog_sms.findViewById(R.id.closetext);
//                    close.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog_sms.dismiss();
//                        }
//                    });
//
//                    ImageView btn_close = (ImageView) dialog_sms.findViewById(R.id.sms);
//                    btn_close.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(getActivity(), "no Sim option there", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                    msg_body = (EditText) dialog_sms.findViewById(R.id.messageedit);
//
//                    ImageView whatsapp = (ImageView) dialog_sms.findViewById(R.id.whatsapp);
//                    whatsapp.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
//                            whatsappIntent.setType("text/plain");
//                            whatsappIntent.putExtra(Intent.EXTRA_TEXT, msg_body.getText().toString());
//                            try {
//                                startActivity(Intent.createChooser(whatsappIntent, "Share using"));
//                            } catch (ActivityNotFoundException ex) {
//                            }
//                        }
//                    });
//
//                    dialog_sms.show();
//                }
                break;

            case R.id.action_write_off:
                Intent intent = new Intent(getActivity(), Write_off_report.class);
                startActivity(intent);

                break;

            case R.id.action_download_csv:
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_selectable_list_item);
                arrayAdapter.add(getString(R.string.download));
                arrayAdapter.add(getString(R.string.download_and_send));
                alertDialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String strName = arrayAdapter.getItem(which);
                        if (strName.toString().equals(getString(R.string.download))){
                            sdff2 = new SimpleDateFormat("ddMMMyy");
                            currentDateandTimee1 = sdff2.format(new Date());

                            Date dt = new Date();
                            sdff1 = new SimpleDateFormat("hhmmssaa");
                            timee1 = sdff1.format(dt);

                            ExportDatabaseCSVTask_download_csv task=new ExportDatabaseCSVTask_download_csv();
                            task.execute();

                        }else {
                            if (strName.toString().equals(getString(R.string.download_and_send))){

                                Cursor cursore = db1.rawQuery("SELECT * FROM Email_setup", null);
                                if (cursore.moveToFirst()){
                                    Cursor cursoree = db1.rawQuery("SELECT * FROM Email_recipient", null);
                                    if (cursoree.moveToFirst()){
                                        //both are there

                                        sdff2 = new SimpleDateFormat("ddMMMyy");
                                        currentDateandTimee1 = sdff2.format(new Date());

                                        Date dt = new Date();
                                        sdff1 = new SimpleDateFormat("hhmmssaa");
                                        timee1 = sdff1.format(dt);

                                        File dbFile=getActivity().getDatabasePath("mydb_Salesdata");
                                        //Log.v(TAG, "Db path is: "+dbFile);  //get the path of db

//                                        File exportDir1 = new File(Environment.getExternalStorageDirectory(), "/Download");

//                                        File exportDir1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/Download");
//                                        if (!exportDir1.exists()) {
//                                            exportDir1.mkdirs();
//                                        }
                                        File exportDir1 = new File(Environment.getExternalStorageDirectory(), "/Download");
                                        if (!exportDir1.exists()) {
                                            exportDir1.mkdirs();
                                        }

                                        Cursor getcom = db1.rawQuery("SELECT * FROM Companydetailss", null);
                                        if (getcom.moveToFirst()) {
                                            strcompanyname = getcom.getString(1);
                                        }else {
                                            strcompanyname = "";
                                        }
                                        getcom.close();

                                        file1 = new File(exportDir1, "IvePOS_customers_report"+currentDateandTimee1+"_"+timee1+".csv");
                                        try {

                                            file1.createNewFile();
                                            CSVWriter csvWrite1 = new CSVWriter(new FileWriter(file1));
                                            String arrStr11[] ={"Id", "Customer_name", "Phone_no.", "Email", "address", "pincode", "Date_of_Birth", "Account_no.", "IFSC_Code", "Account_holder_name", "Bank", "User_id"};
                                            csvWrite1.writeNext(arrStr11);

                                            db = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE,null);
                                            Cursor curCSVv = db.rawQuery("SELECT * FROM Customerdetails GROUP BY phoneno",null);
                                            //csvWrite.writeNext(curCSV.getColumnNames());

                                            if (curCSVv.moveToFirst())  {
                                                do {
                                                    String billnos = curCSVv.getString(5);

                                                    String arrStr[] ={curCSVv.getString(0), curCSVv.getString(1), curCSVv.getString(2), curCSVv.getString(3), curCSVv.getString(4), curCSVv.getString(79),
                                                            curCSVv.getString(17), curCSVv.getString(32), curCSVv.getString(33), curCSVv.getString(34), curCSVv.getString(35), curCSVv.getString(37)};
                                                    csvWrite1.writeNext(arrStr);

                                                }while (curCSVv.moveToNext());

                                            }
                                            curCSVv.close();
                                            csvWrite1.close();

                                        }
                                        catch (IOException e){
                                            Log.e("MainActivity", e.getMessage(), e);


                                        }

                                        Uri u2 = null;
                                        u2 = Uri.fromFile(file1);


                                        String url = "www.intuitionsoftwares.com";

                                        String msg = "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
                                                "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
                                                "Powered by: " + Uri.parse(url);

                                        Cursor cursor = db1.rawQuery("SELECT * FROM Email_setup", null);
                                        if (cursor.moveToFirst()) {
                                            String un = cursor.getString(1);
                                            String pwd = cursor.getString(2);
                                            String client = cursor.getString(3);


                                            Cursor cursor11 = db1.rawQuery("SELECT * FROM Email_recipient", null);
                                            if (cursor11.moveToFirst()) {
                                                do {
                                                    String unn = cursor11.getString(3);
                                                    String toEmails = unn;
                                                    toEmailList = Arrays.asList(toEmails
                                                            .split("\\s*,\\s*"));
                                                } while (cursor11.moveToNext());
                                            }
                                            cursor11.close();


                                            if (client.toString().equals("Gmail")) {
                                                getResultsFromApi();
                                                new MakeRequestTask_cust(mCredential).execute();
                                            }else {
                                                if (client.toString().equals("Yahoo")){
                                                    Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
                                                    if (cursor1.moveToFirst()) {
                                                        do {
                                                            String unn = cursor1.getString(3);
                                                            String toEmails = unn;
                                                            toEmailList = Arrays.asList(toEmails
                                                                    .split("\\s*,\\s*"));
                                                            new SendMailTask_Yahoo_attachment_customercsv(getActivity()).execute(un,
                                                                    pwd, toEmailList, strcompanyname, msg, currentDateandTimee1, timee1);
                                                        } while (cursor1.moveToNext());
                                                    }
                                                    cursor1.close();


                                                }else {
                                                    if (client.toString().equals("Hotmail")){
//                                                        Toast.makeText(getActivity(), "Hotmail and Outlook "+un, Toast.LENGTH_LONG).show();
                                                        Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
                                                        if (cursor1.moveToFirst()) {
                                                            do {
                                                                String unn = cursor1.getString(3);
                                                                String toEmails = unn;
                                                                toEmailList = Arrays.asList(toEmails
                                                                        .split("\\s*,\\s*"));
                                                                new SendMailTask_Hotmail_Outlook_attachment_customercsv(getActivity()).execute(un,
                                                                        pwd, toEmailList, strcompanyname, msg, currentDateandTimee1, timee1);
                                                            } while (cursor1.moveToNext());
                                                        }
                                                        cursor1.close();
                                                    }else {
                                                        if (client.toString().equals("Office365")) {
//                                                            Toast.makeText(getActivity(), "office 365 " + un, Toast.LENGTH_LONG).show();
                                                            Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
                                                            if (cursor1.moveToFirst()) {
                                                                do {
                                                                    String unn = cursor1.getString(3);
                                                                    String toEmails = unn;
                                                                    toEmailList = Arrays.asList(toEmails
                                                                            .split("\\s*,\\s*"));
                                                                    new SendMailTask_Office365_attachment_customercsv(getActivity()).execute(un,
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


                                    }else {
                                        //only recipient not there
                                        final Dialog dialoge = new Dialog(getActivity(), R.style.timepicker_date_dialog);
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
                                                Intent intent = new Intent(getActivity(), EmailSetup_Recipients.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                dialoge.dismiss();
                                            }
                                        });

                                        Button gotosettings1 = (Button) dialoge.findViewById(R.id.gotosettings1);
                                        gotosettings1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getActivity(), EmailSetup_Recipients.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                dialoge.dismiss();
                                            }
                                        });


                                    }
                                    cursoree.close();
                                }else {
                                    Cursor cursoree = db1.rawQuery("SELECT * FROM Email_recipient", null);
                                    if (cursoree.moveToFirst()){
                                        //only sender not there
                                        final Dialog dialoge = new Dialog(getActivity(), R.style.timepicker_date_dialog);
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
                                                Intent intent = new Intent(getActivity(), EmailSetup.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
//                                                getActivity().finish();
                                                dialoge.dismiss();
                                            }
                                        });

                                        Button gotosettings1 = (Button) dialoge.findViewById(R.id.gotosettings1);
                                        gotosettings1.setVisibility(View.GONE);
                                        gotosettings1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getActivity(), EmailSetup.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
//                                                getActivity().finish();
                                                dialoge.dismiss();
                                            }
                                        });

                                    }else {
                                        //both recipient and sender not there
                                        final Dialog dialoge = new Dialog(getActivity(), R.style.timepicker_date_dialog);
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
                                                Intent intent = new Intent(getActivity(), EmailSetup.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
//                                                getActivity().finish();
                                                dialoge.dismiss();
                                            }
                                        });

                                        Button gotosettings1 = (Button) dialoge.findViewById(R.id.gotosettings1);
                                        gotosettings1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getActivity(), EmailSetup_Recipients.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
//                                                getActivity().finish();
                                                dialoge.dismiss();
                                            }
                                        });

                                    }
                                    cursoree.close();
                                }
                                cursore.close();

                            }
                        }
                    }
                });
                alertDialog.show();
                break;

            case R.id.action_upload_csv:
                Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
                fileintent.setType("gagt/sdf");
                try {
                    startActivityForResult(fileintent, 1);
                    requestCode_i = 1;
                } catch (ActivityNotFoundException e) {

                }
                break;

            case R.id.action_help:
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/watch?v=xkv4JVGFjnk&list=PL6YNztMURCKT4YGYTmTg-no0fm80YFG1L&index=3")));
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<String> getTableValues() {
        ArrayList<String> my_array = new ArrayList<String>();
        try {
            Cursor allrows = db.rawQuery("Select * from Customerdetails ", null);
            System.out.println("COUNT : " + allrows.getCount());
            if (allrows.moveToFirst()) {
                do {
                    String ID = allrows.getString(0);
                    String NAME = allrows.getString(1);
                    String PLACE = allrows.getString(3);
                    my_array.add(PLACE);
                } while (allrows.moveToNext());
            }
            allrows.close();
        } catch (Exception e) {
        }
        return my_array;
    }

    public ArrayList<String> getTableValuesnum() {
        ArrayList<String> my_array = new ArrayList<String>();
        try {
            Cursor allrows = db.rawQuery("Select * from Customerdetails ", null);
            System.out.println("COUNT : " + allrows.getCount());
            //my_array.add("None");
            if (allrows.moveToFirst()) {
                do {
                    String ID = allrows.getString(0);
                    String NAME = allrows.getString(1);
                    String PLACE = allrows.getString(2);
                    my_array.add(PLACE);
                } while (allrows.moveToNext());
            }
            allrows.close();
        } catch (Exception e) {
        }
        return my_array;
    }

    protected void sendSMS() {

        String textcompanyname = "", storeitem = "", deviceitem = "", compemailid = "";

        Cursor cursor_cred = db_inapp.rawQuery("SELECT * FROM credentialstime", null);
        if (cursor_cred.moveToFirst()) {
            textcompanyname = cursor_cred.getString(6);
            storeitem = cursor_cred.getString(7);
            deviceitem = cursor_cred.getString(8);
            compemailid = cursor_cred.getString(5);
        }
        cursor_cred.close();

        String message = msg_body.getText().toString();

        Cursor allrows = db.rawQuery("Select * from Customerdetails GROUP BY phoneno", null);
        if (allrows.moveToFirst()) {
            do {
                String ID = allrows.getString(0);
                String NAME = allrows.getString(1);
                String PLACE = allrows.getString(2);
                try {
//                        SmsManager smsManager = SmsManager.getDefault();
//                        smsManager.sendTextMessage(PLACE, null, message, null, null);

                    AmazonSNSClient snsClient = new AmazonSNSClient(credentialsProvider1);
//                        String message = "Thank you for your card purchase of "+insert1_rs  + fulltotal.getText().toString() + "/-" + " on bill " +
//                                billnum.getText().toString() + " from " + finalTextcompanyname + ". " + "Please come again.";
                    String mesasge1 = "Hi '"+NAME+"'! Come celebrate with us. We're so excited to announce that our '"+textcompanyname+"' has exciting offers for you. '"+message+"'. Regards Intuition.";
                    String phoneNumber = "+91" + PLACE;
                    Map<String, MessageAttributeValue> smsAttributes =
                            new HashMap<String, MessageAttributeValue>();

//                    PublishResult result = snsClient.publish(new PublishRequest()
//                            .withMessage(mesasge1)
//                            .withPhoneNumber(phoneNumber)
//                            .withMessageAttributes(smsAttributes));
//                    System.out.println(result); // Prints the message ID.

                    RequestQueue queue= RequestSingleton.getInstance(getContext()).getInstance();

                    StringRequest sr1 = new StringRequest(
                            com.android.volley.Request.Method.POST,
                            WebserviceUrl + "app_mobile.php",
                            new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String responseString) {

                                }
                            },
                            new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("Signup confirm7", "Error: " + error.getMessage());
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("message", mesasge1);
                            params.put("phoneNumber", phoneNumber);
                            return params;
                        }
                    };
                    sr1.setRetryPolicy(new DefaultRetryPolicy(0, 0,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(sr1);

                    //Toast.makeText(getActivity(), "SMS sent.", Toast.LENGTH_LONG).show();

                    Cursor cursor = db_inapp.rawQuery("SELECT * FROM Messaginglicense", null);
                    if (cursor.moveToFirst()) {
                        do {
                            String id = cursor.getString(0);
                            String countval = cursor.getString(2);

                            Cursor cursor1 = db_inapp.rawQuery("SELECT * FROM Messaginglicense WHERE Messagessent = '" + countval + "'", null);
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
                                contentValues.put("Messagessent", count + 1);
                                contentValues.put("remainingmessages", countlimit - 1);
                                contentValues.put("time", time1);
                                contentValues.put("date", normal1);
                                String where = "_id = '" + id1 + "'";
                                db_inapp = getActivity().openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
                                db_inapp.update("Messaginglicense", contentValues, where, new String[]{});

                                int bn = countlimit - 1;
                                remaining_msgs1(String.valueOf(bn));

                            }
                            cursor1.close();
                        } while (cursor.moveToNext());
                    }
                    cursor.close();

                }

                catch (Exception e) {
                    //Toast.makeText(getActivity(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } while (allrows.moveToNext());
        }
        allrows.close();



    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
//                    Toast.makeText(getActivity(), "permission granted", Toast.LENGTH_SHORT).show();
                    if (!SdIsPresent()) ;

                    Log.i("Send SMS", "");
                    // String phoneNo = txtphoneNo.getText().toString();
                    String message = msg_body.getText().toString();

                    Cursor allrows = db.rawQuery("Select * from Customerdetails ", null);
                    if (allrows.moveToFirst()) {
                        do {
                            String ID = allrows.getString(0);
                            String NAME = allrows.getString(1);
                            String PLACE = allrows.getString(2);
                            try {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(PLACE, null, message, null, null);
                                //Toast.makeText(getActivity(), "SMS sent.", Toast.LENGTH_LONG).show();
                            }

                            catch (Exception e) {
                                //Toast.makeText(getActivity(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        } while (allrows.moveToNext());
                    }
                    allrows.close();


                } else {

                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static boolean SdIsPresent() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    protected void sendemail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);

        ArrayList<String> array_from_db = getTableValues();
        String[] emails = new String[array_from_db.size()];
        for (int i = 0; i < array_from_db.size(); i++) {
            emails[i] = array_from_db.get(i);
        }
        Cursor cursore = db1.rawQuery("SELECT * FROM Email_setup", null);
        if (cursore.moveToFirst()) {
            final String un = cursore.getString(1);
            final String pwd = cursore.getString(2);
            final String client = cursore.getString(3);


            final Dialog dialog_promotions_mail = new Dialog(getActivity(), R.style.timepicker_date_dialog);
            dialog_promotions_mail.setContentView(R.layout.dialog_promotions_email_selection);
            dialog_promotions_mail.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialog_promotions_mail.show();

            ImageButton btncancel = (ImageButton) dialog_promotions_mail.findViewById(R.id.btncancel);
            btncancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_promotions_mail.dismiss();
                }
            });


            listview_tax_list = (ListView) dialog_promotions_mail.findViewById(R.id.listview);

            list_promotions = new ArrayList<Country_promotions>();
            String statement = "SELECT DISTINCT * FROM Customerdetails GROUP BY emailid";
            //Execute the query
            Cursor aallrows = db.rawQuery(statement, null);
            System.out.println("COUNT : " + aallrows.getCount());
            ////Toast.makeText(getActivity(), "limit is a " + limit, Toast.LENGTH_SHORT).show();
            if (aallrows.moveToFirst()) {
                do {
                    String ID = aallrows.getString(0);
                    String NAme = aallrows.getString(1);
                    String email = aallrows.getString(3);

                    TextView textView = new TextView(getActivity());
                    textView.setText(email);

                    if (textView.getText().toString().equals("")){

                    }else {
                        Country_promotions NAME = new Country_promotions(NAme, email);
                        list_promotions.add(NAME);
                    }

                } while (aallrows.moveToNext());
            }
            aallrows.close();

            adapter_promotions = new MyAdapter_promotions(getActivity(),list_promotions);
            listview_tax_list.setAdapter(adapter_promotions);

            final EditText myFilter = (EditText) dialog_promotions_mail.findViewById(R.id.search_selecteditem);
            myFilter.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter_promotions.getFilter().filter(s.toString());
                }
            });

            final CheckBox chkAll =  (CheckBox) dialog_promotions_mail.findViewById(R.id.chkAll);
            chkAll.setChecked(true);

            View v;
            CheckBox chBox;

            if (chkAll.isChecked()){
                for(int i=0; i < listview_tax_list.getCount(); i++){
                    v = listview_tax_list.getAdapter().getView(i, null, null);

                    CheckBox cb = (CheckBox)v.findViewById(R.id.check);
                    cb.setChecked(true);
                    adapter_promotions.notifyDataSetChanged();
                }
            }

            chkAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View v;
                    CheckBox chBox;

                    if (chkAll.isChecked()){
                        for(int i=0; i < listview_tax_list.getCount(); i++){
                            v = listview_tax_list.getAdapter().getView(i, null, null);

                            CheckBox cb = (CheckBox)v.findViewById(R.id.check);
                            cb.setChecked(true);
                            adapter_promotions.notifyDataSetChanged();
                        }
                    }else {
                        for(int i=0; i < listview_tax_list.getCount(); i++){
                            v = listview_tax_list.getAdapter().getView(i, null, null);

                            CheckBox cb = (CheckBox)v.findViewById(R.id.check);
                            cb.setChecked(false);
                            adapter_promotions.notifyDataSetChanged();
                        }
                    }
                }
            });

            listview_tax_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView txtview = (TextView) view.findViewById(R.id.label);
                    final String item = txtview.getText().toString();

                    final CheckBox checkbox = (CheckBox) view.getTag(R.id.check);
                    if (checkbox.isChecked()){
                        checkbox.setChecked(false);
                    }else {
                        checkbox.setChecked(true);
                    }

                    int count = 0;
                    int size = list_promotions.size();
                    for (int i1=0; i1<size; i1++){
                        if (list_promotions.get(i1).isSelected()){
                            count++;
                        }
                    }

                    if(listview_tax_list.getCount()==count)
                        chkAll.setChecked(true);
                    else
                        chkAll.setChecked(false);

                    adapter_promotions.notifyDataSetChanged();

                }
            });


            db1 = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor cursor = db1.rawQuery("SELECT * FROM Companydetailss", null);
            if (cursor.moveToFirst()) {
                companynameis = cursor.getString(1);
            }else {
                companynameis = "";
            }
            cursor.close();

            Button btnsend = (Button) dialog_promotions_mail.findViewById(R.id.btnsend);
            btnsend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Cursor cd = db1.rawQuery("SELECT * FROM promotions", null);
                    if (cd.moveToFirst()){
                        do {
                            String id = cd.getString(0);
                            String where = "_id = '" + id + "' ";

                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "promotions");
                            getActivity().getContentResolver().delete(contentUri, where,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("promotions")
                                    .appendQueryParameter("operation", "delete")
                                    .appendQueryParameter("_id",id)
                                    .build();
                            getActivity().getContentResolver().notifyChange(resultUri, null);

//                            db1.delete("promotions", where, new String[]{});
                        }while (cd.moveToNext());
                    }
                    cd.close();

                    String url = "www.intuitionsoftwares.com";

                    String msg = msg_body.getText().toString() +" \n"+ Uri.parse(url);

                    for(int i=0; i < listview_tax_list.getCount(); i++) {
                        v = listview_tax_list.getAdapter().getView(i, null, null);

                        email_re = (TextView) v.findViewById(R.id.label1);
                        CheckBox cb1 = (CheckBox) v.findViewById(R.id.check);
                        if (cb1.isChecked()) {

                            ContentValues cv = new ContentValues();
                            cv.put("email", email_re.getText().toString());

                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "promotions");
                            resultUri = getActivity().getContentResolver().insert(contentUri, cv);
                            getActivity().getContentResolver().notifyChange(resultUri, null);

//                            db1.insert("promotions", null, cv);
                        }
                    }

                    if (client.toString().equals("Gmail")) {
                        getResultsFromApi();
                        new MakeRequestTask_promotions(mCredential).execute();
                    }else {
                        if (client.toString().equals("Yahoo")){
                            Cursor cursor1 = db1.rawQuery("SELECT * FROM promotions", null);
                            if (cursor1.moveToFirst()) {
                                do {
                                    String unn = cursor1.getString(1);
                                    String toEmails = unn;
                                    toEmailList = Arrays.asList(toEmails
                                            .split("\\s*,\\s*"));
                                    new SendMailTask_Yahoo(getActivity()).execute(un,
                                            pwd, toEmailList, companynameis, msg);
                                } while (cursor1.moveToNext());
                            }
                            cursor1.close();


                        }else {
                            if (client.toString().equals("Hotmail")){
                                Cursor cursor1 = db1.rawQuery("SELECT * FROM promotions", null);
                                if (cursor1.moveToFirst()) {
                                    do {
                                        String unn = cursor1.getString(1);
                                        String toEmails = unn;
                                        toEmailList = Arrays.asList(toEmails
                                                .split("\\s*,\\s*"));
                                        new SendMailTask_Hotmail(getActivity()).execute(un,
                                                pwd, toEmailList, companynameis, msg);
                                    } while (cursor1.moveToNext());
                                }
                                cursor1.close();
                            }else {
                                if (client.toString().equals("Office365")) {
//                                                Toast.makeText(getActivity(), "office 365 " + un, Toast.LENGTH_LONG).show();
                                    Cursor cursor1 = db1.rawQuery("SELECT * FROM promotions", null);
                                    if (cursor1.moveToFirst()) {
                                        do {
                                            String unn = cursor1.getString(1);
                                            String toEmails = unn;
                                            toEmailList = Arrays.asList(toEmails
                                                    .split("\\s*,\\s*"));
                                            new SendMailTask_Outlook(getActivity()).execute(un,
                                                    pwd, toEmailList, companynameis, msg);
                                        } while (cursor1.moveToNext());
                                    }
                                    cursor1.close();
                                }
                            }
                        }
                    }




                }
            });


        }else {
            //only sender not there recipient not required
            final Dialog dialoge = new Dialog(getActivity(), R.style.timepicker_date_dialog);
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

            LinearLayout recipient_layout = (LinearLayout) dialoge.findViewById(R.id.recipient_layout);
            recipient_layout.setVisibility(View.GONE);

            sender_notset.setVisibility(View.VISIBLE);

            recipient_set.setVisibility(View.VISIBLE);

            Button gotosettings = (Button) dialoge.findViewById(R.id.gotosettings);
            gotosettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EmailSetup.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    dialoge.dismiss();
                }
            });

            Button gotosettings1 = (Button) dialoge.findViewById(R.id.gotosettings1);
            gotosettings1.setVisibility(View.GONE);
            gotosettings1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EmailSetup.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    dialoge.dismiss();
                }
            });
        }
        cursore.close();



    }

    private class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage(getString(R.string.setmessage13));
            this.dialog.show();

        }
        protected Boolean doInBackground(final String... args){

            File dbFile=getActivity().getDatabasePath("mydb_Salesdata");

//            File exportDir = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_reports/IVEPOS_customer_list");
            File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_reports/IVEPOS_customer_list");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            file = new File(exportDir, "IvePOS_customer_list"+currentDateandTimee1+"_"+timee1+".csv");
            try {

                db.execSQL("UPDATE Customerdetails SET total_amount = rupees WHERE refunds = '' OR refunds IS NULL");

                db.execSQL("delete from Cusotmer_activity_temp");

                Cursor cursorq = db.rawQuery("SELECT * FROM Customerdetails GROUP BY phoneno", null);
                if (cursorq.moveToFirst()){
                    do {
                        String pn = cursorq.getString(2);
                        String id = cursorq.getString(0);
                        Cursor c1 = db.rawQuery("SELECT SUM(rupees) FROM Customerdetails WHERE phoneno = '"+pn+"'", null);
                        if (c1.moveToFirst()){
                            do {
                                float aq = c1.getFloat(0);
                                aqq = String.valueOf(aq);
                            }while (c1.moveToNext());
                        }
                        c1.close();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("total", aqq);
                        String where = "_id = '" + id + "' ";
                        System.out.println("sum of rupees "+aqq+" _id "+id);

                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
                        getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProvider.AUTHORITY)
                                .path("Customerdetails")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id",id)
                                .build();
                        getActivity().getContentResolver().notifyChange(resultUri, null);

//                        db.update("Customerdetails", contentValues, where, new String[]{});

                    }
                    while (cursorq.moveToNext());
                }
                cursorq.close();

                db.execSQL("UPDATE Clicked_cust_name SET name = ''");

                db.execSQL("delete from Cusotmer_activity_temp");

                Cursor cursor = db.rawQuery("Select * from Customerdetails GROUP BY phoneno ", null);
                if (cursor.moveToFirst()){
                    do {
                        String id = cursor.getString(0);
                        String phon = cursor.getString(2);
                        String name = cursor.getString(1);
                        String email = cursor.getString(3);
                        String addr = cursor.getString(4);
                        String cus_id = cursor.getString(37);
                        String pin_co = cursor.getString(79);
                        String rop = "0";
//                        String rop1 = "0";
                        String rope = "";
//
                        ContentValues contentValues = new ContentValues();
//
                        contentValues.put("_id", id);
                        contentValues.put("name", name);
                        contentValues.put("phone_no", phon);
                        contentValues.put("email", email);
                        contentValues.put("addr", addr);
                        contentValues.put("cust_id", cus_id);
                        contentValues.put("pincode", pin_co);

                        Cursor cursor2 = db.rawQuery("Select SUM(total_amount) from Customerdetails WHERE phoneno = '"+phon+"' ", null);
                        if (cursor2.moveToFirst()){
                            float dsirsq = cursor2.getFloat(0);
                            rop = String.format("%.1f", dsirsq);

                            contentValues.put("total_amount", rop);
                        }
                        cursor2.close();

//                        Cursor sales_t = db.rawQuery("SELECT SUM(total_amount) FROM Customerdetails WHERE phoneno = '"+phon+"'", null);
//                        if (sales_t.moveToFirst()){
//                            dsirsq1 = sales_t.getFloat(0);
//                        }
//                        sales_t.close();

                        Cursor credit_t = db.rawQuery("SELECT SUM(credit) FROM Customerdetails WHERE phoneno = '"+phon+"'", null);
                        if (credit_t.moveToFirst()){
                            dsirsq2 = credit_t.getFloat(0);
                        }
                        credit_t.close();

                        Cursor deposi_t = db.rawQuery("SELECT SUM(deposit) FROM Customerdetails WHERE phoneno = '"+phon+"'", null);
                        if (deposi_t.moveToFirst()){
                            dsirsq3 = deposi_t.getFloat(0);
                        }
                        deposi_t.close();

                        Cursor cashout_t = db.rawQuery("SELECT SUM(cashout) FROM Customerdetails WHERE phoneno = '"+phon+"'", null);
                        if (cashout_t.moveToFirst()){
                            dsirsq4 = cashout_t.getFloat(0);
                        }
                        cashout_t.close();

                        final float cal_sale = ((dsirsq4 + dsirsq2) - dsirsq3);
                        String rop1 = String.format("%.1f", cal_sale);

                        contentValues.put("balance", rop1);

                        Cursor set21w = db.rawQuery("SELECT * FROM Customerdetails WHERE phoneno = '"+phon+"'", null);
                        if (set21w.moveToFirst()){
                            dis_val = set21w.getString(29);
                            dis_status = set21w.getString(27);
                            dis_ty = set21w.getString(30);
                        }
                        set21w.close();

                        TextView tv6 = new TextView(getActivity());
                        tv6.setText(dis_ty);

                        if (tv6.getText().toString().equals("") || tv6.getText().toString().equals("off")){

                        }else {
                            //save value
                            contentValues.put("discount_value", dis_val);
                            contentValues.put("discount_type", dis_ty);

                        }

                        Cursor rating_cursor = db.rawQuery("SELECT SUM(percentage) FROM Cust_feedback WHERE cust_phoneno = '"+phon+"'", null);
                        if (rating_cursor.moveToFirst()){
                            float dsirsq = rating_cursor.getFloat(0);
                            ropq = String.format("%.1f", dsirsq);
                        }
                        rating_cursor.close();

                        Cursor rating_cursor1 = db.rawQuery("SELECT * FROM Cust_feedback WHERE cust_phoneno = '"+phon+"'", null);
                        int coun = rating_cursor1.getCount();

                        if (ropq.toString().equals("") || coun == 0){

                        }else {
                            float divw = Float.parseFloat(ropq) / coun;
                            rope = String.format("%.1f", divw);
                            contentValues.put("approval_rate", rope);
                        }
                        rating_cursor1.close();

//                        Customer_activity_listitems NAME = new Customer_activity_listitems(cus_id, name, phon, email, addr, rop, rop1, dis_val, dis_ty, rope);
//                        countryList.add(NAME);

                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Cusotmer_activity_temp");
                        resultUri = getActivity().getContentResolver().insert(contentUri, contentValues);
                        getActivity().getContentResolver().notifyChange(resultUri, null);

                        //   db.insert("Cusotmer_activity_temp", null, contentValues);

                    }while (cursor.moveToNext());
                }
                cursor.close();

                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

                //ormlite core method
                // this is the Column of the table and same for Header of CSV file
                String arrStr1[] = {"Name", "Phone no.", "E-mail", "Total("+insert1_rs+")", "Address", "Pincode", "Balance", "Discount value", "unit", "Approval rating"};
                csvWrite.writeNext(arrStr1);

                db = getActivity().openOrCreateDatabase("mydb_Salesdata",
                        Context.MODE_PRIVATE, null);
                Cursor curCSV = db.rawQuery("SELECT * FROM Cusotmer_activity_temp ORDER BY _id DESC",null);

                while (curCSV.moveToNext()) {
                    String arrStr[] = {curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(5), curCSV.getString(4),
                            curCSV.getString(11), curCSV.getString(6), curCSV.getString(7), curCSV.getString(8), curCSV.getString(9)};
                    csvWrite.writeNext(arrStr);

                }

                csvWrite.close();
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
                Toast.makeText(getActivity(), getString(R.string.export_successful), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(), getString(R.string.export_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Cursor fetchCountriesByName1(String inputtext) throws SQLException {

        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
//            mCursor = db.rawQuery("SELECT * FROM Customerdetails GROUP BY phoneno", null);
            mCursor = db.rawQuery("SELECT * FROM Customerdetails GROUP BY phoneno ORDER BY phoneno DESC", null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Customerdetails WHERE phoneno LIKE '%" + inputtext + "%' OR name LIKE '%" + inputtext + "%' OR user_id LIKE '%" + inputtext + "%' GROUP BY phoneno", null);
        }

        return mCursor;
    }

    public static void donotshowKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }



    private android.app.TimePickerDialog.OnTimeSetListener timePickerListener_open = new android.app.TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime_open(hour, minute);

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minutes);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);


            String hour1 = "";
            if (hour < 10)
                hour1 = "0" + hour;
            else
                hour1 = String.valueOf(hour);

            String minutes1 = "";
            if (minute < 10)
                minutes1 = "0" + minute;
            else
                minutes1 = String.valueOf(minute);

            editText_from_day_hide.setText(hour1 + "" + minutes1);


        }
    };

    private android.app.TimePickerDialog.OnTimeSetListener timePickerListener_close = new android.app.TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime_close(hour, minute);

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minutes);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            String hour1 = "";
            if (hour < 10)
                hour1 = "0" + hour;
            else
                hour1 = String.valueOf(hour);

            String minutes1 = "";
            if (minute < 10)
                minutes1 = "0" + minute;
            else
                minutes1 = String.valueOf(minute);

            editText_to_day_hide.setText(hour1 + "" + minutes1);
        }
    };

    private void updateTime_open(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        editText_from_day_visible.setText(aTime);
    }

    private void updateTime_close(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        editText_to_day_visible.setText(aTime);
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, String> {
        private Gmail mService = null;
        private Exception mLastError = null;

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


            Cursor curCSV0 = db.rawQuery("SELECT SUM(total) FROM Billnumber", null);
            if (curCSV0.moveToFirst()) {
                do {
                    float aq = curCSV0.getFloat(0);
                    sumnew = String.valueOf(aq);
                }
                while (curCSV0.moveToNext());
            }
            curCSV0.close();

            Cursor curCSV3 = db.rawQuery("SELECT SUM(credit) FROM Customerdetails", null);
            if (curCSV3.moveToFirst()) {
                do {
                    float aq = curCSV3.getFloat(0);
                    sumnew3 = String.valueOf(aq);
                }
                while (curCSV3.moveToNext());
            }
            curCSV3.close();

            Cursor curCSV4 = db.rawQuery("SELECT SUM(deposit) FROM Customerdetails", null);
            if (curCSV4.moveToFirst()) {
                do {
                    float aq = curCSV4.getFloat(0);
                    sumnew4 = String.valueOf(aq);
                }
                while (curCSV4.moveToNext());
            }
            curCSV4.close();

            Cursor curCSV5 = db.rawQuery("SELECT SUM(cashout) FROM Customerdetails", null);
            if (curCSV5.moveToFirst()) {
                do {
                    float aq = curCSV5.getFloat(0);
                    sumnew5 = String.valueOf(aq);
                }
                while (curCSV5.moveToNext());
            }
            curCSV5.close();

            Cursor curCSV6 = db.rawQuery("SELECT SUM(cashout) FROM Customerdetails", null);
            if (curCSV6.moveToFirst()) {
                do {
                    float aq = curCSV6.getFloat(0);
                    sumnew5 = String.valueOf(aq);
                }
                while (curCSV6.moveToNext());
            }
            curCSV6.close();

            float v = (Float.parseFloat(sumnew) - Float.parseFloat(sumnew3)) + (Float.parseFloat(sumnew4) - Float.parseFloat(sumnew5));
            String v1_profit1 = String.format("%.1f", v);


            String msg = "Customer list (list attached)\n\n" +
                    "No. of customers: " + countget.getText().toString() + "\n\n"+
                    "All time Sale: " + sumnew + "\n\n" +
                    "Cash sale: "+insert1_rs+"" + total_l1 + "\n\n" +
                    "Card sale: "+insert1_rs+"" + total_l3 + "\n\n" +
                    "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
                    "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
                    "Powered by: " + Uri.parse(url);


            Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
            if (cursor1.moveToFirst()) {
                do {
                    String unn = cursor1.getString(3);
                    TextView edtToAddress = new TextView(getActivity());
                    edtToAddress.setText(unn);

                    TextView edtSubject = new TextView(getActivity());
                    edtSubject.setText(strcompanyname);

                    TextView edtMessage = new TextView(getActivity());
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
//                        String filename = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_reports/IVEPOS_customer_list/IvePOS_customer_list"+currentDateandTimee1+"_"+timee1+".csv";
                        String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/IVEPOS_reports/IVEPOS_customer_list/IvePOS_customer_list"+currentDateandTimee1+"_"+timee1+".csv";
                        File f = new File(filename);
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
                Toast.makeText(getActivity(), "not success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
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
//                    Log.v("Errors", mLastError.getMessage());
                }
            } else {
            }
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                getActivity(),
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private void getResultsFromApi() {

        Cursor cursorr = db1.rawQuery("SELECT * FROM Email_setup", null);
        if (cursorr.moveToFirst()) {
            String unn = cursorr.getString(1);
//            Toast.makeText(getActivity(), "a4 " + unn, Toast.LENGTH_SHORT).show();

            TextView tvv = new TextView(getActivity());
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
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
                apiAvailability.isGooglePlayServicesAvailable(getActivity());
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
                apiAvailability.isGooglePlayServicesAvailable(getActivity());
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
        if (EasyPermissions.hasPermissions(
                getActivity(), Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getActivity().getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            startActivityForResult(
                    mCredential.newChooseAccountIntent(),
                    REQUEST_ACCOUNT_PICKER);
//            }
        } else {
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

    private class MakeRequestTask_promotions extends AsyncTask<Void, Void, String> {
        private Gmail mService = null;
        private Exception mLastError = null;

        public MakeRequestTask_promotions(GoogleAccountCredential credential) {
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

            Cursor getcom = db1.rawQuery("SELECT * FROM Companydetailss", null);
            if (getcom.moveToFirst()) {
                do {
                    strcompanyname = getcom.getString(1);
                    straddress1 = getcom.getString(14);
                } while (getcom.moveToNext());
            }else {
                strcompanyname = "";
            }
            getcom.close();

            String url = "www.intuitionsoftwares.com";

//            String msg = "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
//                    "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
//                    "Powered by: " + Uri.parse(url);


            String msg = msg_body.getText().toString() +" \n"+ Uri.parse(url);
//
            Cursor cursor1 = db1.rawQuery("SELECT * FROM promotions", null);
            if (cursor1.moveToFirst()) {
                do {
                    String unn = cursor1.getString(1);


                    Log.v("Receiver", unn);
                    TextView edtToAddress = new TextView(getActivity());
                    edtToAddress.setText(unn);

                    TextView edtSubject = new TextView(getActivity());
                    edtSubject.setText(strcompanyname);

                    TextView edtMessage = new TextView(getActivity());
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

                        mimeMessage = createEmail(to, from, subject, body);
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
                Toast.makeText(getActivity(), "not success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
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


    private class ExportDatabaseCSVTask_download_csv extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage(getString(R.string.setmessage17));
            this.dialog.show();

        }
        protected Boolean doInBackground(final String... args){

            File dbFile=getActivity().getDatabasePath("mydb_Salesdata");
            //Log.v(TAG, "Db path is: "+dbFile);  //get the path of db

            File exportDir1 = new File(Environment.getExternalStorageDirectory(), "/Download");
            if (!exportDir1.exists()) {
                exportDir1.mkdirs();
            }

            file1 = new File(exportDir1, "IvePOS_customers_report"+currentDateandTimee1+"_"+timee1+".csv");
            try {

//                file.createNewFile();
//                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
//
//                // this is the Column of the table and same for Header of CSV file
//                String arrStr1[] ={"Date", "Time", "User", "Bill count", "Bill no.", "Type", "Mode", "Table", "Itemname", "Qty.", "Individualprice", "Total price"};
//                csvWrite.writeNext(arrStr1);
//
//                db = getActivity().openOrCreateDatabase("mydb_Salesdata",
//                        Context.MODE_PRIVATE, null);
//                Cursor curCSV = db.rawQuery("SELECT * FROM Generalorderlistascdesc1",null);
//                //csvWrite.writeNext(curCSV.getColumnNames());
//
//                while(curCSV.moveToNext())  {
//                    String arrStr[] ={curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(18), curCSV.getString(4), curCSV.getString(9),
//                            curCSV.getString(8), curCSV.getString(12), curCSV.getString(10), curCSV.getString(11), curCSV.getString(13), curCSV.getString(17)};
////	                curCSV.getString(2),curCSV.getString(3),curCSV.getString(4),
//                    csvWrite.writeNext(arrStr);
//
//                }
//
//                csvWrite.close();

                file1.createNewFile();
                CSVWriter csvWrite1 = new CSVWriter(new FileWriter(file1));

                // this is the Column of the table and same for Header of CSV file
                String arrStr11[] ={"Id", "Customer_name", "Phone_no.", "Email", "address", "pincode", "Date_of_Birth", "Account_no.", "IFSC_Code", "Account_holder_name", "Bank", "User_id"};
                csvWrite1.writeNext(arrStr11);

                db = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE,null);
                Cursor curCSVv = db.rawQuery("SELECT * FROM Customerdetails GROUP BY phoneno",null);
                //csvWrite.writeNext(curCSV.getColumnNames());

                if (curCSVv.moveToFirst())  {
                    do {
                        String billnos = curCSVv.getString(5);

                        String arrStr[] ={curCSVv.getString(0), curCSVv.getString(1), curCSVv.getString(2), curCSVv.getString(3), curCSVv.getString(4), curCSVv.getString(79),
                                curCSVv.getString(17), curCSVv.getString(32), curCSVv.getString(33), curCSVv.getString(34), curCSVv.getString(35), curCSVv.getString(37)};
                        csvWrite1.writeNext(arrStr);

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
                Toast.makeText(getActivity(), getString(R.string.export_successful), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(), getString(R.string.export_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class MakeRequestTask_cust extends AsyncTask<Void, Void, String> {
        private Gmail mService = null;
        private Exception mLastError = null;
//        private View view = sendFabButton;

        public MakeRequestTask_cust(GoogleAccountCredential credential) {
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

            Cursor getcom = db1.rawQuery("SELECT * FROM Companydetailss", null);
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

            Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
            if (cursor1.moveToFirst()) {
                do {
                    String unn = cursor1.getString(3);
                    TextView edtToAddress = new TextView(getActivity());
                    edtToAddress.setText(unn);

                    TextView edtSubject = new TextView(getActivity());
                    edtSubject.setText(strcompanyname);

                    TextView edtMessage = new TextView(getActivity());
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

                        String filename = Environment.getExternalStorageDirectory().toString()+"/Download/IvePOS_customers_report"+currentDateandTimee1+"_"+timee1+".csv";

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
                Toast.makeText(getActivity(), "not success", Toast.LENGTH_SHORT).show();
//                showMessage(view, "No results returned.");
            } else {
                Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
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
//                    Log.v("Errors", mLastError.getMessage());
                }
            } else {
            }
        }
    }





    public void consumeItemPro() {
        try {
            mHelperPro.queryInventoryAsync(mReceivedInventoryListenerPro);
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }





    public void up_da(int requestCode, int resultCode, Intent data){
        if (data == null)

            return;
        filepath = data.getData().getPath();

        try {

            if (resultCode == getActivity().RESULT_OK) {


                DownloadMusicfromInternet_new downloadMusicfromInternet_new = new DownloadMusicfromInternet_new();
                try {
                    downloadMusicfromInternet_new.execute();
                } catch (Exception e) {
                    downloadMusicfromInternet_new.cancel(true);
                    e.printStackTrace();
                }

            } else {
                Dialog d = new Dialog(getActivity());
                d.setTitle(getString(R.string.title15));
                d.show();
            }
        } catch (Exception ex) {
            Dialog d = new Dialog(getActivity());
            d.setTitle(ex.getMessage().toString() + "second");
            d.show();

            // db.endTransaction();

        }
        requestCode_i = 0;
    }


    class DownloadMusicfromInternet2 extends AsyncTask<Void, Integer, Void> {

        private ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);

        @Override
        protected Void doInBackground(Void... params) {

            try {

                FileReader file = new FileReader(filepath);
                BufferedReader buffer = new BufferedReader(file);

                String line = "";

                while ((line = buffer.readLine()) != null) {

                    String[] str = line.split(",", 11);  // defining 3 columns with null or blank field //values acceptance

                    //Id, Company,Name,Price

                    String imp1 = "0", imp2 = "0";

                    String name = str[0].toString();//id
                    String issn = str[1].toString();//name
                    String imp = str[2].toString();//phoneno
                    imp1 = str[3].toString();//email
                    imp2 = str[4].toString();//address
                    String imp3 = str[5].toString();//dob
                    String imp4 = str[6].toString();//accountno
                    String imp41 = str[7].toString();//ifsc
                    String imp5 = str[8].toString();//acc_hold_name
                    String imp6 = str[9].toString();//bankname
                    String imp7 = str[10].toString();//userid

                    Log.d("itemname", "rohith5");

                    TextView tv = new TextView(getActivity());
                    tv.setText(name);

                    TextView tv1 = new TextView(getActivity());
                    tv1.setText(issn);

                    TextView tv2 = new TextView(getActivity());
                    tv2.setText(imp);

                    TextView tv3 = new TextView(getActivity());
                    tv3.setText(imp1);

                    TextView tv4 = new TextView(getActivity());
                    tv4.setText(imp2);

                    TextView tv5 = new TextView(getActivity());
                    tv5.setText(imp3);

                    TextView tv6 = new TextView(getActivity());
                    tv6.setText(imp4);

                    TextView tv7 = new TextView(getActivity());
                    tv7.setText(imp41);

                    TextView tv8 = new TextView(getActivity());
                    tv8.setText(imp5);

                    TextView tv9 = new TextView(getActivity());
                    tv9.setText(imp6);

                    TextView tv10 = new TextView(getActivity());
                    tv10.setText(imp7);


                    if (tv.getText().toString().contains("\"")) {
                        name = name.toString().replaceAll("\"", "");
                    } else {
                    }

                    if (tv1.getText().toString().contains("\"")) {
                        issn = issn.toString().replaceAll("\"", "");
                    } else {
                    }

                    if (tv2.getText().toString().contains("\"")) {
                        imp = imp.toString().replaceAll("\"", "");
                    } else {
                    }

                    if (tv3.getText().toString().contains("\"")) {
                        imp1 = imp1.toString().replaceAll("\"", "");
                    } else {
                    }

                    if (tv4.getText().toString().contains("\"")) {
                        imp2 = imp2.toString().replaceAll("\"", "");
                    } else {
                    }

                    if (tv5.getText().toString().contains("\"")) {
                        imp3 = imp3.toString().replaceAll("\"", "");
                    } else {
                    }

                    if (tv6.getText().toString().contains("\"")) {
                        imp4 = imp4.toString().replaceAll("\"", "");
                    } else {
                    }

                    if (tv7.getText().toString().contains("\"")) {
                        imp41 = imp41.toString().replaceAll("\"", "");
                    } else {
                    }

                    if (tv8.getText().toString().contains("\"")) {
                        imp5 = imp5.toString().replaceAll("\"", "");
                    } else {
                    }

                    if (tv9.getText().toString().contains("\"")) {
                        imp6 = imp6.toString().replaceAll("\"", "");
                    } else {
                    }

                    if (tv10.getText().toString().contains("\"")) {
                        imp7 = imp7.toString().replaceAll("\"", "");
                    } else {
                    }


                    if (issn.toString().equals("Customer_name")) {
                        Log.d("itemname", "rohith1");
                    } else {
                        Cursor cursor = db.rawQuery("SELECT * FROM Customerdetails WHERE phoneno = '"+imp+"'", null);
                        if (cursor.moveToFirst()) {
                            do {
                                String id = cursor.getString(0);
                                Log.d("itemname", "rohith2");
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("name", issn);
                                contentValues.put("emailid", imp1);
                                contentValues.put("phoneno", imp);
                                contentValues.put("address", imp2);
                                contentValues.put("rupees", "0");
                                contentValues.put("dob", imp3);

                                Cursor cursora = db1.rawQuery("SELECT * FROM Default_credit WHERE _id = '1'", null);
                                if (cursora.moveToFirst()) {
                                    String st = cursora.getString(1);
                                    if (st.toString().equals("off")) {
                                        contentValues.put("credit_default", "off");
                                    } else {
                                        contentValues.put("credit_default", "on");
                                    }
                                }
                                cursora.close();

                                contentValues.put("cust_account_no", imp4);
                                contentValues.put("cust_ifsc_code", imp41);
                                contentValues.put("cust_account_holder_name", imp5);
                                contentValues.put("cust_bank_name", imp6);
                                contentValues.put("proceedings", "off");
                                contentValues.put("user_id", imp7);

                                String where = "_id = '" + id + "' ";

                                contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "customerdetails");
                                getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProvider.AUTHORITY)
                                        .path("customerdetails")
                                        .appendQueryParameter("operation", "update")
                                        .appendQueryParameter("_id",id)
                                        .build();
                                getActivity().getContentResolver().notifyChange(resultUri, null);

//                                db.update("customerdetails", contentValues, where, new String[]{});

                            } while (cursor.moveToNext());
                        }else {
                            Log.d("itemname", "rohith3");
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("name", issn);
                            contentValues.put("emailid", imp1);
                            contentValues.put("phoneno", imp);
                            contentValues.put("address", imp2);
                            contentValues.put("rupees", "0");
                            contentValues.put("dob", imp3);

                            Cursor cursora = db1.rawQuery("SELECT * FROM Default_credit WHERE _id = '1'", null);
                            if (cursora.moveToFirst()) {
                                String st = cursora.getString(1);
                                if (st.toString().equals("off")) {
                                    contentValues.put("credit_default", "off");
                                } else {
                                    contentValues.put("credit_default", "on");
                                }
                            }
                            cursora.close();

                            contentValues.put("cust_account_no", imp4);
                            contentValues.put("cust_ifsc_code", imp41);
                            contentValues.put("cust_account_holder_name", imp5);
                            contentValues.put("cust_bank_name", imp6);
                            contentValues.put("proceedings", "off");
                            contentValues.put("user_id", imp7);

                            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "customerdetails");
                            resultUri = getActivity().getContentResolver().insert(contentUri, contentValues);
                            getActivity().getContentResolver().notifyChange(resultUri, null);

                        //    db.insert("customerdetails", null, contentValues);

                            int i = 1;
                            Cursor c1_11 = db1.rawQuery("SELECT * FROM Taxes WHERE taxtype = 'Globaltax'", null);
                            if (c1_11.moveToFirst()) {
                                do {
                                    String tn = c1_11.getString(1);
                                    String tn_value = c1_11.getString(2);
                                    ContentValues contentValues1 = new ContentValues();
                                    contentValues1.put("tax" + i, tn);
                                    contentValues1.put("tax" + i + "_value", tn_value);
                                    contentValues1.put("tax_selection", "All");
                                    String wherecu1 = "phoneno = '" + imp + "'";

                                    contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
                                    getActivity().getContentResolver().update(contentUri, contentValues1,wherecu1,new String[]{});
                                    resultUri = new Uri.Builder()
                                            .scheme("content")
                                            .authority(StubProvider.AUTHORITY)
                                            .path("Customerdetails")
                                            .appendQueryParameter("operation", "update")
                                            .appendQueryParameter("phoneno",imp)
                                            .build();
                                    getActivity().getContentResolver().notifyChange(resultUri, null);

//                                    db.update("Customerdetails", contentValues1, wherecu1, new String[]{});
                                    i++;
                                } while (c1_11.moveToNext());
                            }
                            c1_11.close();


                        }
                        cursor.close();
                        Log.d("itemname", "rohith4");

                    }
                }



            } catch (SQLException e) {
                Log.e("Error", e.getMessage().toString());
            } catch (IOException e) {
                Dialog d = new Dialog(getActivity());
                d.setTitle(e.getMessage().toString() + "first");
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

            dialog.setMessage("Loading");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            dialog.setMax(1000);
            //Set the current progress to zero
            dialog.setProgress(0);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
        }
    }

    class DownloadMusicfromInternet_new extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            try {

                try {


//                db.execSQL("UPDATE Customerdetails SET total_amount = rupees WHERE refunds = '' OR refunds IS NULL");
//
////                db.execSQL("UPDATE Customerdetails SET total_amount = rupees - refunds WHERE refunds != ''");
//
//                //made this in cancelactivity page while cancelling bill
////                Cursor ref = db.rawQuery("SELECT * FROM Customerdetails", null);
////                if (ref.moveToFirst()){
////                    do {
////                        String id = ref.getString(0);
////                        String old_t = ref.getString(5);
////                        String ref_t = ref.getString(18);
////
////                        TextView tv = new TextView(getActivity());
////                        tv.setText(old_t);
////
////                        TextView tv1 = new TextView(getActivity());
////                        tv1.setText(ref_t);
////
////                        if (tv1.getText().toString().equals("")){
//////                            ContentValues contentValues = new ContentValues();
//////                            contentValues.put("total_amount", tv.getText().toString());
//////                            String wherecu = "_id = '" + id + "'";
//////
//////                            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
//////                            getActivity().getContentResolver().update(contentUri, contentValues,wherecu,new String[]{});
//////                            resultUri = new Uri.Builder()
//////                                    .scheme("content")
//////                                    .authority(StubProvider.AUTHORITY)
//////                                    .path("Customerdetails")
//////                                    .appendQueryParameter("operation", "update")
//////                                    .appendQueryParameter("_id",id)
//////                                    .build();
//////                            getActivity().getContentResolver().notifyChange(resultUri, null);
//////
////////                            db.update("Customerdetails", contentValues, wherecu, new String[]{});
////                        }else {
////                            float ff = Float.parseFloat(tv.getText().toString()) - Float.parseFloat(tv1.getText().toString());
////
////                            ContentValues contentValues = new ContentValues();
////                            contentValues.put("total_amount", String.valueOf(ff));
////                            String wherecu = "_id = '" + id + "'";
////
////                            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
////                            getActivity().getContentResolver().update(contentUri, contentValues,wherecu,new String[]{});
////                            resultUri = new Uri.Builder()
////                                    .scheme("content")
////                                    .authority(StubProvider.AUTHORITY)
////                                    .path("Customerdetails")
////                                    .appendQueryParameter("operation", "update")
////                                    .appendQueryParameter("_id",id)
////                                    .build();
////                            getActivity().getContentResolver().notifyChange(resultUri, null);
////
//////                            db.update("Customerdetails", contentValues, wherecu, new String[]{});
////                        }
////                    }while (ref.moveToNext());
////                }
////                ref.close();
//
//                db.execSQL("delete from Cusotmer_activity_temp");
//
//                Cursor cursorq = db.rawQuery("SELECT * FROM Customerdetails GROUP BY phoneno", null);
//                if (cursorq.moveToFirst()){
//                    do {
//                        String pn = cursorq.getString(2);
//                        String id = cursorq.getString(0);
//                        Cursor c1 = db.rawQuery("SELECT SUM(rupees) FROM Customerdetails WHERE phoneno = '"+pn+"'", null);
//                        if (c1.moveToFirst()){
//                            do {
//                                float aq = c1.getFloat(0);
//                                aqq = String.valueOf(aq);
//                            }while (c1.moveToNext());
//                        }
//                        c1.close();
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("total", aqq);
//                        String where = "_id = '" + id + "' ";
//                        System.out.println("sum of rupees "+aqq+" _id "+id);
//
//                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
//                        getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                        resultUri = new Uri.Builder()
//                                .scheme("content")
//                                .authority(StubProvider.AUTHORITY)
//                                .path("Customerdetails")
//                                .appendQueryParameter("operation", "update")
//                                .appendQueryParameter("_id",id)
//                                .build();
//                        getActivity().getContentResolver().notifyChange(resultUri, null);
//
////                        db.update("Customerdetails", contentValues, where, new String[]{});
//
//                    }
//                    while (cursorq.moveToNext());
//                }
//                cursorq.close();
//
//                db.execSQL("UPDATE Clicked_cust_name SET name = ''");
//
////                Cursor cursorf = db.rawQuery("SELECT * FROM Clicked_cust_name", null);
////                if (cursorf.moveToFirst()){
////                    String idf = cursorf.getString(0);
////                    ContentValues contentValues = new ContentValues();
////                    contentValues.put("name", "");
////                    String wherecu = "_id = '" + idf + "'";
////
////                    contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Clicked_cust_name");
////                    getActivity().getContentResolver().update(contentUri, contentValues,wherecu,new String[]{});
////                    resultUri = new Uri.Builder()
////                            .scheme("content")
////                            .authority(StubProvider.AUTHORITY)
////                            .path("Clicked_cust_name")
////                            .appendQueryParameter("operation", "update")
////                            .appendQueryParameter("_id",idf)
////                            .build();
////                    getActivity().getContentResolver().notifyChange(resultUri, null);
////
//////                    db.update("Clicked_cust_name", contentValues, wherecu, new String[]{});
////
////                }
////                cursorf.close();
//
////                Cursor cursorf1 = db.rawQuery("SELECT * FROM Customerdetails", null);
////                if (cursorf1.moveToFirst()){
////                    do {
////                        String id = cursorf1.getString(0);
////                        String datetimee_new = cursorf1.getString(36);
////                        String date = cursorf1.getString(9);
////                        String time = cursorf1.getString(8);
////
////                        TextView tv = new TextView(getActivity());
////                        tv.setText(datetimee_new);
////                        TextView tv1 = new TextView(getActivity());
////                        tv1.setText(date);
////                        TextView tv2 = new TextView(getActivity());
////                        tv2.setText(time);
////
////                        if (tv.getText().toString().equals("")){
////                            if (tv1.getText().toString().equals("") || tv2.getText().toString().equals("")){
////
////                            }else {
////
////                                if (date.toString().contains(" ")){
////                                    date = date.replace(" ", "");
////                                }
////                                if (time.toString().contains(":")){
////                                    time = time.replace(":", "");
////                                }
////
////                                String tee3 = time;
////                                String tee = time.substring(7, 9);
////
////                                if (tee.toString().equals("PM")){
////                                    String tee1 = time.substring(0, 2);
////                                    String tee2 = time.substring(2);
////                                    if (tee1.toString().equals("01")){
////                                        tee1 = "13";
////                                    }
////                                    if (tee1.toString().equals("02")){
////                                        tee1 = "14";
////                                    }
////                                    if (tee1.toString().equals("03")){
////                                        tee1 = "15";
////                                    }
////                                    if (tee1.toString().equals("04")){
////                                        tee1 = "16";
////                                    }
////                                    if (tee1.toString().equals("05")){
////                                        tee1 = "17";
////                                    }
////                                    if (tee1.toString().equals("06")){
////                                        tee1 = "18";
////                                    }
////                                    if (tee1.toString().equals("07")){
////                                        tee1 = "19";
////                                    }
////                                    if (tee1.toString().equals("08")){
////                                        tee1 = "20";
////                                    }
////                                    if (tee1.toString().equals("09")){
////                                        tee1 = "21";
////                                    }
////                                    if (tee1.toString().equals("10")){
////                                        tee1 = "22";
////                                    }
////                                    if (tee1.toString().equals("11")){
////                                        tee1 = "23";
////                                    }
////                                    tee3 = tee1+""+tee2;
////                                }
////
////                                if (tee.toString().equals("AM")){
////                                    String tee1 = time.substring(0, 2);
////                                    String tee2 = time.substring(2);
////                                    if (tee1.toString().equals("12")){
////                                        tee1 = "00";
////                                    }
////                                    tee3 = tee1+""+tee2;
////                                }
////
////                                String te = date+""+tee3;
////                                String te1 = te.substring(0, 12);
////                                ContentValues contentValues = new ContentValues();
////                                contentValues.put("datetimee_new", te1);
////                                String wherecu = "_id = '" + id + "'";
////
////                                contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
////                                getActivity().getContentResolver().update(contentUri, contentValues,wherecu,new String[]{});
////                                resultUri = new Uri.Builder()
////                                        .scheme("content")
////                                        .authority(StubProvider.AUTHORITY)
////                                        .path("Customerdetails")
////                                        .appendQueryParameter("operation", "update")
////                                        .appendQueryParameter("_id",id)
////                                        .build();
////                                getActivity().getContentResolver().notifyChange(resultUri, null);
////
//////                                db.update("Customerdetails", contentValues, wherecu, new String[]{});
////                            }
////                        }
////
////                    }while (cursorf1.moveToNext());
////                }
////                cursorf1.close();
//
                    db.execSQL("delete from Cusotmer_activity_temp");

//                Cursor cursor = db.rawQuery("Select * from Customerdetails GROUP BY phoneno ", null);
//                if (cursor.moveToFirst()){
//                    do {
////                        String id = cursor.getString(0);
//                        String phon = cursor.getString(2);
////                        String name = cursor.getString(1);
////                        String email = cursor.getString(3);
////                        String addr = cursor.getString(4);
////                        String cus_id = cursor.getString(37);
////                        String rop = "0";
//////                        String rop1 = "0";
//                        String rope = "";
////
//                        ContentValues contentValues = new ContentValues();

                    Cursor ce = db.rawQuery("SELECT * FROM Cust_feedback GROUP BY cust_phoneno", null);
                    if (ce.moveToFirst()) {
                        do {
                            String phon = ce.getString(10);

                            Cursor ce1 = db.rawQuery("SELECT * FROM Cusotmer_activity_temp WHERE phone_no = '" + phon + "'", null);
                            if (ce1.moveToFirst()) {

                            } else {
                                String rope = "";
                                ContentValues contentValues = new ContentValues();
//
//                        contentValues.put("_id", id);
//                        contentValues.put("name", name);
                                contentValues.put("phone_no", phon);
//                        contentValues.put("email", email);
//                        contentValues.put("addr", addr);
//                        contentValues.put("cust_id", cus_id);
//
//                        Cursor cursor2 = db.rawQuery("Select SUM(total_amount) from Customerdetails WHERE phoneno = '"+phon+"' ", null);
//                        if (cursor2.moveToFirst()){
//                            float dsirsq = cursor2.getFloat(0);
//                            rop = String.format("%.1f", dsirsq);
//
//                            contentValues.put("total_amount", rop);
//                        }
//                        cursor2.close();
//
////                        Cursor sales_t = db.rawQuery("SELECT SUM(total_amount) FROM Customerdetails WHERE phoneno = '"+phon+"'", null);
////                        if (sales_t.moveToFirst()){
////                            dsirsq1 = sales_t.getFloat(0);
////                        }
////                        sales_t.close();
//
//                        Cursor credit_t = db.rawQuery("SELECT SUM(credit) FROM Customerdetails WHERE phoneno = '"+phon+"'", null);
//                        if (credit_t.moveToFirst()){
//                            dsirsq2 = credit_t.getFloat(0);
//                        }
//                        credit_t.close();
//
//                        Cursor deposi_t = db.rawQuery("SELECT SUM(deposit) FROM Customerdetails WHERE phoneno = '"+phon+"'", null);
//                        if (deposi_t.moveToFirst()){
//                            dsirsq3 = deposi_t.getFloat(0);
//                        }
//                        deposi_t.close();
//
//                        Cursor cashout_t = db.rawQuery("SELECT SUM(cashout) FROM Customerdetails WHERE phoneno = '"+phon+"'", null);
//                        if (cashout_t.moveToFirst()){
//                            dsirsq4 = cashout_t.getFloat(0);
//                        }
//                        cashout_t.close();
//
//                        final float cal_sale = ((dsirsq4 + dsirsq2) - dsirsq3);
//                        String rop1 = String.format("%.1f", cal_sale);
//
//                        contentValues.put("balance", rop1);
//
//                        Cursor set21w = db.rawQuery("SELECT * FROM Customerdetails WHERE phoneno = '"+phon+"'", null);
//                        if (set21w.moveToFirst()){
//                            dis_val = set21w.getString(29);
//                            dis_status = set21w.getString(27);
//                            dis_ty = set21w.getString(30);
//                        }
//                        set21w.close();
//
//                        TextView tv6 = new TextView(getActivity());
//                        tv6.setText(dis_ty);
//
//                        if (tv6.getText().toString().equals("") || tv6.getText().toString().equals("off")){
//
//                        }else {
//                            //save value
//                            contentValues.put("discount_value", dis_val);
//                            contentValues.put("discount_type", dis_ty);
//
//                        }

                                Cursor rating_cursor = db.rawQuery("SELECT SUM(percentage) FROM Cust_feedback WHERE cust_phoneno = '" + phon + "'", null);
                                if (rating_cursor.moveToFirst()) {
                                    float dsirsq = rating_cursor.getFloat(0);
                                    ropq = String.format("%.1f", dsirsq);
                                }
                                rating_cursor.close();

                                Cursor rating_cursor1 = db.rawQuery("SELECT * FROM Cust_feedback WHERE cust_phoneno = '" + phon + "'", null);
                                int coun = rating_cursor1.getCount();

                                if (ropq.toString().equals("") || coun == 0) {

                                } else {
                                    float divw = Float.parseFloat(ropq) / coun;
                                    rope = String.format("%.1f", divw);
                                    contentValues.put("approval_rate", rope);
                                }
                                rating_cursor1.close();

//                        Customer_activity_listitems NAME = new Customer_activity_listitems(cus_id, name, phon, email, addr, rop, rop1, dis_val, dis_ty, rope);
//                        countryList.add(NAME);

                                contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Cusotmer_activity_temp");
                                resultUri = getActivity().getContentResolver().insert(contentUri, contentValues);
                                getActivity().getContentResolver().notifyChange(resultUri, null);

                                //   db.insert("Cusotmer_activity_temp", null, contentValues);
                            }
                        }while (ce.moveToNext());
                    }
                    ce.close();


//                    }while (cursor.moveToNext());
//                }
//                cursor.close();

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            proceed_button.setVisibility(View.VISIBLE);
//            rela.setVisibility(View.GONE);
//            add_button.setVisibility(View.GONE);
        }


        @Override
        protected void onPostExecute(Integer file_url) {
            proceed_button.setVisibility(View.GONE);
            rela.setVisibility(View.VISIBLE);
            add_button.setVisibility(View.VISIBLE);

            try {
                listview.setAdapter(null);

                Cursor on = db.rawQuery("SELECT * FROM Cusotmer_activity_temp WHERE approval_rate IS NOT NULL", null);
                final int co1 = on.getCount();

                Cursor rating_cursor = db.rawQuery("SELECT SUM(approval_rate) FROM Cusotmer_activity_temp", null);
                if (rating_cursor.moveToFirst()){
                    float dsirsq = rating_cursor.getFloat(0);
                    String rop = String.format("%.1f", dsirsq);

                    float div = dsirsq/Float.parseFloat(String.valueOf(co1));
                    rating_perc.setText(String.format("%.1f", div));
                }
                rating_cursor.close();

//                countryList = new ArrayList<Customer_activity_listitems>();
//
//                Cursor allrows = db.rawQuery("Select * from Cusotmer_activity_temp ORDER BY _id DESC", null);
//                System.out.println("COUNT : " + allrows.getCount());
//
//                if (allrows.moveToFirst()) {
//                    do {
//                        String ID = allrows.getString(0);
//                        String NAme = allrows.getString(1);
//                        String CUst_Id = allrows.getString(11);
//                        String PHoNe = allrows.getString(2);
//                        String EmaIl = allrows.getString(3);
//                        String AdDr = allrows.getString(4);
//                        String SAlE = allrows.getString(5);
//                        String BalANce = allrows.getString(6);
//                        String DIScouNT = allrows.getString(7);
//                        String DIs_TYpe = allrows.getString(9);
//                        String ApPr_RatinG = allrows.getString(10);
//                        Customer_activity_listitems NAME = new Customer_activity_listitems(CUst_Id, NAme, PHoNe, EmaIl, AdDr, SAlE, BalANce, DIScouNT, DIs_TYpe, ApPr_RatinG);
//                        countryList.add(NAME);
//                    } while (allrows.moveToNext());
//                }
//                allrows.close();


                Cursor cursor = db.rawQuery("SELECT * FROM Customerdetails GROUP BY phoneno ORDER BY phoneno DESC", null);
                String[] fromFieldNames = {"user_id", "name", "phoneno", "emailid", "address", "pincode"};
                // the XML defined views which the data will be bound to
                int[] toViewsID = {R.id.cust_idd, R.id.name, R.id.phone, R.id.emailid, R.id.address, R.id.pincode};
                Log.e("Checamos que hay id", String.valueOf(R.id.name));
                dataAdapterr = new SimpleCursorAdapter(getActivity(), R.layout.customer_list_activity, cursor, fromFieldNames, toViewsID, 0);
//                ListView listview = (ListView) dialog.findViewById(R.id.cust_feedback_list);
//                listview.setAdapter(dataAdapterr);// Assign adapter to ListView.... here... the bitch error

//                dataAdapterr = new MyCustomAdapter(getContext(),
//                        R.layout.customer_list_activity, countryList);
                listview.setAdapter(dataAdapterr);

                dataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
                    public Cursor runQuery(CharSequence constraint) {
                        return fetchCountriesByName1(constraint.toString());
                    }
                });

                Cursor one = db.rawQuery("SELECT * FROM Customerdetails GROUP BY phoneno", null);
                int cou = one.getCount();
                total1 = String.valueOf(cou);
                countget.setText(total1);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void savenew(Dialog dialog){
        db = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        if (add_cust_name.getText().toString().equals("") || add_cust_phoneno.getText().toString().equals("")){
            if (add_cust_name.getText().toString().equals("")){
                add_cust_name.setError("Enter customer name");
            }
            if (add_cust_phoneno.getText().toString().equals("")){
                add_cust_phoneno.setError("Enter customer phone no");
            }
        }else {
            Cursor cv = db.rawQuery("Select * from Customerdetails WHERE phoneno = '"+add_cust_phoneno.getText().toString()+"'", null);
            if (cv.moveToFirst()){
                //String ph = cv.getString(2);
                //if (add_cust_phoneno.getText().toString().equals(ph.toString())){
                add_cust_phoneno.setError("Phone no already in list");
//                }else {
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("name", add_cust_name.getText().toString());
//                    contentValues.put("emailid", add_cust_emailid.getText().toString());
//                    contentValues.put("phoneno", add_cust_phoneno.getText().toString());
//                    contentValues.put("address", add_cust_address.getText().toString());
//
//                    db.insert("Customerdetails", null, contentValues);
//                    dialog.dismiss();
//                }
            }else {

                if (add_cust_userid.getText().toString().equals("")){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("name", add_cust_name.getText().toString());
                    contentValues.put("emailid", add_cust_emailid.getText().toString());
                    contentValues.put("phoneno", add_cust_phoneno.getText().toString());
                    contentValues.put("address", add_cust_address.getText().toString());
                    contentValues.put("pincode", add_cust_pincode.getText().toString());
                    contentValues.put("rupees", "0");
                    contentValues.put("dob", add_cust_dob.getText().toString());
                    contentValues.put("user_id", "");

                    Cursor cursor = db1.rawQuery("SELECT * FROM Default_credit WHERE _id = '1'", null);
                    if (cursor.moveToFirst()) {
                        String st = cursor.getString(1);
                        if (st.toString().equals("off")) {
                            contentValues.put("credit_default", "off");
                        } else {
                            contentValues.put("credit_default", "on");
                        }
                    }
                    cursor.close();

                    contentValues.put("cust_account_no", add_bank_no_string);
                    contentValues.put("cust_ifsc_code", add_bank_ifsc_string);
                    contentValues.put("cust_account_holder_name", add_bank_cust_name_string);
                    contentValues.put("cust_bank_name", add_bank_name_string);
                    contentValues.put("proceedings", "off");

                    contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
                    resultUri = getActivity().getContentResolver().insert(contentUri, contentValues);
                    getActivity().getContentResolver().notifyChange(resultUri, null);


              //      db.insert("Customerdetails", null, contentValues);
                    dialog.dismiss();

                    int i = 1;
                    Cursor c1_11 = db1.rawQuery("SELECT * FROM Taxes WHERE taxtype = 'Globaltax'", null);
                    if (c1_11.moveToFirst()) {
                        do {
                            String tn = c1_11.getString(1);
                            String tn_value = c1_11.getString(2);
                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("tax"+i, tn);
                            contentValues1.put("tax"+i+"_value", tn_value);
                            contentValues1.put("tax_selection", "All");
                            String wherecu1 = "phoneno = '" + add_cust_phoneno.getText().toString() + "'";

                            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
                            getActivity().getContentResolver().update(contentUri, contentValues1,wherecu1,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProvider.AUTHORITY)
                                    .path("Customerdetails")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("phoneno",add_cust_phoneno.getText().toString())
                                    .build();
                            getActivity().getContentResolver().notifyChange(resultUri, null);

//                            db.update("Customerdetails", contentValues1, wherecu1, new String[]{});
                            i++;
                        }while (c1_11.moveToNext());
                    }
                    c1_11.close();

                    DownloadMusicfromInternet_new downloadMusicfromInternet_new = new DownloadMusicfromInternet_new();
                    try {
                        downloadMusicfromInternet_new.execute();
                    } catch (Exception e) {
                        downloadMusicfromInternet_new.cancel(true);
                        e.printStackTrace();
                    }
                }else {
                    Cursor userid_check = db.rawQuery("SELECT * from Customerdetails WHERE user_id = '" + add_cust_userid.getText().toString() + "'", null);
                    if (userid_check.moveToFirst()) {
                        add_cust_userid.setError("User id already in list");
                    } else {

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("name", add_cust_name.getText().toString());
                        contentValues.put("emailid", add_cust_emailid.getText().toString());
                        contentValues.put("phoneno", add_cust_phoneno.getText().toString());
                        contentValues.put("address", add_cust_address.getText().toString());
                        contentValues.put("pincode", add_cust_pincode.getText().toString());
                        contentValues.put("rupees", "0");
                        contentValues.put("dob", add_cust_dob.getText().toString());
                        contentValues.put("user_id", add_cust_userid.getText().toString());

                        Cursor cursor = db1.rawQuery("SELECT * FROM Default_credit WHERE _id = '1'", null);
                        if (cursor.moveToFirst()) {
                            String st = cursor.getString(1);
                            if (st.toString().equals("off")) {
                                contentValues.put("credit_default", "off");
                            } else {
                                contentValues.put("credit_default", "on");
                            }
                        }
                        cursor.close();

                        contentValues.put("cust_account_no", add_bank_no_string);
                        contentValues.put("cust_ifsc_code", add_bank_ifsc_string);
                        contentValues.put("cust_account_holder_name", add_bank_cust_name_string);
                        contentValues.put("cust_bank_name", add_bank_name_string);
                        contentValues.put("proceedings", "off");

                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
                        resultUri = getActivity().getContentResolver().insert(contentUri, contentValues);
                        getActivity().getContentResolver().notifyChange(resultUri, null);


                    //    db.insert("Customerdetails", null, contentValues);
                        dialog.dismiss();

                        int i = 1;
                        Cursor c1_11 = db1.rawQuery("SELECT * FROM Taxes WHERE taxtype = 'Globaltax'", null);
                        if (c1_11.moveToFirst()) {
                            do {
                                String tn = c1_11.getString(1);
                                String tn_value = c1_11.getString(2);
                                ContentValues contentValues1 = new ContentValues();
                                contentValues1.put("tax"+i, tn);
                                contentValues1.put("tax"+i+"_value", tn_value);
                                contentValues1.put("tax_selection", "All");
                                String wherecu1 = "phoneno = '" + add_cust_phoneno.getText().toString() + "'";

                                contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
                                getActivity().getContentResolver().update(contentUri, contentValues1,wherecu1,new String[]{});
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProvider.AUTHORITY)
                                        .path("Customerdetails")
                                        .appendQueryParameter("operation", "update")
                                        .appendQueryParameter("phoneno",add_cust_phoneno.getText().toString())
                                        .build();
                                getActivity().getContentResolver().notifyChange(resultUri, null);

//                                db.update("Customerdetails", contentValues1, wherecu1, new String[]{});
                                i++;
                            }while (c1_11.moveToNext());
                        }
                        c1_11.close();

                        DownloadMusicfromInternet_new downloadMusicfromInternet_new = new DownloadMusicfromInternet_new();
                        try {
                            downloadMusicfromInternet_new.execute();
                        } catch (Exception e) {
                            downloadMusicfromInternet_new.cancel(true);
                            e.printStackTrace();
                        }
                    }
                    userid_check.close();
                }

            }
            cv.close();


        }
    }

    @Override
    public void onClick(DialogInterface dialog, int id) {
        Log.d(TAG, "Launching purchase flow for gas1.");
        if (id == 0 /* First choice item */) {

            Log.d(TAG, "Launching purchase flow for gas2.");
        } else if (id == DialogInterface.BUTTON_POSITIVE /* continue button */) {
            Log.d(TAG, "Launching purchase flow for gas3.");
            /* TODO: for security, generate your payload here for verification. See the comments on
             *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
             *        an empty string, but on a production app you should carefully generate
             *        this. */


            mSelectedProSubscription=SKU_DELAROY_PRO_UPGRADE;


            try {
                mHelperPro.launchPurchaseFlow(getActivity(), mSelectedProSubscription, REQ_CODE,
                        mPurchaseFinishedListenerPro, mSelectedProSubscription);
            } catch (IabHelper.IabAsyncInProgressException e) {
                //  complain("Error launching purchase flow. Another async operation in progress.");
                // setWaitScreen(false);
            }




//            if (mSelectedSubscriptionPeriod.toString().equals(mFirstChoiceSku)) {
//                i = 0;
//                String payload = "";
//
//                Cursor cursor = db_inapp.rawQuery("SELECT * FROM pro_upgrade", null);
//                if (cursor.moveToFirst()) {
//                    do {
//                        String idss = cursor.getString(0);
//                        String countval = cursor.getString(1);
//
//                        Cursor cursor1 = db_inapp.rawQuery("SELECT * FROM pro_upgrade WHERE status = '" + countval + "'", null);
//                        if (cursor1.moveToFirst()) {
//                            String id1 = cursor1.getString(0);
//                            String limit = cursor1.getString(1);
//                            ContentValues contentValues = new ContentValues();
//
//                            contentValues.put("status", "Activated");
//
//                            String where = "_id = '" + id1 + "'";
//                            db_inapp.update("pro_upgrade", contentValues, where, new String[]{});
//                        }
//
//                    } while (cursor.moveToNext());
//                }
//            }


        } else if (id != DialogInterface.BUTTON_NEGATIVE) {
            // There are only four buttons, this should not happen
            Log.e(TAG, "Unknown button clicked in subscription dialog: " + id);
        }
    }


    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListenerPro
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(mSelectedProSubscription)) {
                consumeItemPro();

            }

        }
    };




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        if (requestCode_i == 1) {

            up_da(requestCode, resultCode, data);
            requestCode_i = 0;
        }
        else {

        }
        if (!mHelperPro.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    /**
     * Verifies the developer payload of a purchase.
     */
    boolean verifyDeveloperPayload(final Purchase p) {
        payload = p.getDeveloperPayload();
        checking = p.getOrderId();
        state = p.getPurchaseState();


        return true;

    }

    boolean verifyDeveloperPayload1(Purchase p) {
        Cursor cursor = db_inapp.rawQuery("SELECT * FROM Pro_upgrade", null);
        if (cursor.moveToFirst()) {
            do {
                String idss = cursor.getString(0);

                payload = p.getDeveloperPayload();
                checking = p.getOrderId();
                state = p.getPurchaseState();


                ContentValues contentValues = new ContentValues();
                contentValues.put("orderid", checking);

                String where = "_id = '" + idss + "'";
                db_inapp.update("pro_upgrade", contentValues, where, new String[]{});
//                Cursor cursor1 = db_inapp.rawQuery("SELECT * FROM Messaginglicense WHERE Messagessent = '" + countval + "'", null);
//                if (cursor1.moveToFirst()) {
//                    String id1 = cursor1.getString(0);
//
//                }
            } while (cursor.moveToNext());
        }
        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }





    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    // updates UI to reflect model
    public void updateUi() {

        Button subscribeButton = (Button) getActivity().findViewById(R.id.subscribe_button);



        Intent intent = new Intent(getActivity(), Customer_Info_Activity.class);
        startActivity(intent);

        getActivity().finish();



    }

    // Enables or disables the "please wait" screen.
    void setWaitScreen(boolean set) {
//        findViewById(R.id.screen_main).setVisibility(set ? View.GONE : View.VISIBLE);
    }


    // Enables or disables the "please wait" screen.
    void setWaitScreen1(boolean set) {
//        findViewById(R.id.screen_main).setVisibility(set ? View.GONE : View.VISIBLE);
    }

    void complain(String message) {
        Log.e(TAG, "**** Delaroy Error: " + message);
        alert("Error: " + message);
    }

    void complain1(String message) {
        Log.e(TAG, "**** Delaroy Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(getActivity());
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();

    }


    public boolean databaseExist() {
        File DATA_DIRECTORY_DATABASE =
                new File(Environment.getDataDirectory() +
                        "/data/" + "com.intuition.ivepos" +
                        "/databases/" + "amazoninapp");

        return DATA_DIRECTORY_DATABASE.exists();
    }

    public void setAmazonS3Client1(CognitoCachingCredentialsProvider credentialsProvider1) {

        // Create an S3 client
        s3client1 = new AmazonS3Client(credentialsProvider1);

        // Set the region of your S3 bucket
        s3client1.setRegion(Region.getRegion(Regions.US_EAST_1));

    }


    public void setTransferUtility() {

        transferUtility = new TransferUtility(s3client, getActivity());
    }



    public void total_loya(final String ph_no, final Dialog dialog_cust) {
        SharedPreferences prefs = getDefaultSharedPreferencesMultiProcess(getActivity());
        final String email = prefs.getString("emailid", "");
        final String companyname = prefs.getString("companyname", "");


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"loyalty_total.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        String response= "";
                        JSONObject jsonObject=null;
                        try {
                            jsonObject=new JSONObject(responseString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            response = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(response.equalsIgnoreCase("success")){

//                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
//                            String currentDateandTime = sdf.format(new Date());
//                            lastSynced=currentDateandTime.toString();
//                            last_synced.setText("Last synced :"+lastSynced);
//
                            parseJson(jsonObject, ph_no, dialog_cust);



                        }else{
                            Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                })  {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                               /*     params.put("email", email + "");
                                    params.put("password", password + "");*/
                params.put("email",email);
                params.put("ph_no",ph_no);
                params.put("company",companyname);
                return params;
            }
        };
    /*    sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        sr.setRetryPolicy(new DefaultRetryPolicy(0,-1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

//    private void parseJson(JSONObject jsonObject) {
////        items.clear();
//        try {
//            JSONArray jsonArray=jsonObject.getJSONArray("items");
//
//            for(int i=0;i<jsonArray.length();i++){
//
//                JSONObject itemObj=jsonArray.getJSONObject(i);
//                String name = itemObj.getString("phoneno");
//                String value = itemObj.getString("points");
////                StockItemBean itemListBean=new StockItemBean();
////                itemListBean.setItemName(itemObj.getString("itemname"));
////                itemListBean.setQuantity(itemObj.getInt("quantity"));
////                itemListBean.setBarcode(itemObj.getString("barcode"));
////                items.add(itemListBean);
//
//            }
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    private void parseJson(JSONObject jsonObject, String ph_no, Dialog dialog_cust) {

        try {
            String sumOfpoints = jsonObject.getString("total");
            System.out.println("sum of sales: "+sumOfpoints);

            Cursor cursor2 = db.rawQuery("SELECT * FROM customer_loyalty_points WHERE phoneno = '"+ph_no+"'", null);
            if (cursor2.moveToFirst()){
//                t_total_points = cursor2.getString(2);

                Cursor cursor_loyalty_points = db1.rawQuery("SELECT * FROM loyalty_points", null);
                if (cursor_loyalty_points.moveToFirst()){
                    String t_point2 = cursor_loyalty_points.getString(3);
                    String t_money2 = cursor_loyalty_points.getString(4);

                    v_tq = Float.parseFloat(sumOfpoints) * Float.parseFloat(t_money2);

                }
                cursor_loyalty_points.close();

            }

            final TextView total_points = (TextView) dialog_cust.findViewById(R.id.total_points);
            total_points.setText(sumOfpoints);

            final TextView total_amount = (TextView) dialog_cust.findViewById(R.id.total_amount);
            total_amount.setText(String.format("%.2f", v_tq));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void remaining_msgs1(final String bn){

        SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(getActivity());
        final String company= sharedpreferences.getString("companyname", null);
        final String store= sharedpreferences.getString("storename", null);
        final String device= sharedpreferences.getString("devicename", null);

//        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
//        final String regId = pref.getString("regId", null);
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"remaining_msg.php",
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
                params.put("msg",bn);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

    }


}
