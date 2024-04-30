package com.intuition.ivepos;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.intuition.ivepos.Constants.base64EncodedPublicKey;
import static com.intuition.ivepos.Constants_Dashboard.SKU_DELAROY_DASHBOARD_MONTHLY;
import static com.intuition.ivepos.Constants_Dashboard.SKU_DELAROY_DASHBOARD_SIXMONTH;
import static com.intuition.ivepos.Constants_Dashboard.SKU_DELAROY_DASHBOARD_THREEMONTH;
import static com.intuition.ivepos.Constants_Dashboard.SKU_DELAROY_DASHBOARD_WEEKLY;
import static com.intuition.ivepos.Constants_Dashboard.SKU_DELAROY_DASHBOARD_YEARLY;
import static com.intuition.ivepos.Constants_Inventory.SKU_DELAROY_PRO_UPGRADE;
import static com.intuition.ivepos.Constants_Inventory.SKU_DELAROY_PRO_UPGRADE_DEMO;
import static com.intuition.ivepos.Constants_Messaging.SKU_DELAROY_MESSAGING_MONTHLY;
import static com.intuition.ivepos.Constants_Messaging.SKU_DELAROY_MESSAGING_SIXMONTH;
import static com.intuition.ivepos.Constants_Messaging.SKU_DELAROY_MESSAGING_THREEMONTH;
import static com.intuition.ivepos.Constants_Messaging.SKU_DELAROY_MESSAGING_WEEKLY;
import static com.intuition.ivepos.Constants_Messaging.SKU_DELAROY_MESSAGING_YEARLY;
import static com.intuition.ivepos.Constants_Messaging.base64EncodedPublicKey1;
import static com.intuition.ivepos.Constant_Onlineorder.SKU_DELAROY_Online_order_1;
import static com.intuition.ivepos.Constant_Onlineorder.SKU_DELAROY_Online_order_2;
import static com.intuition.ivepos.Constant_Onlineorder.SKU_DELAROY_Online_order_3;
import static com.intuition.ivepos.Constant_Onlineorder.base64EncodedPublicKey2;
import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

/**
 * Created by HP on 6/28/2018.
 */

public class CloudSubscritionActivity extends AppCompatActivity {
    static final String TAG = "CloudSubscriptionActivity";
    private static int REQ_CODE = 1000;
    IabHelper mHelper,mHelperMessage,mHelperPro, mHelperOrder;
    SQLiteDatabase db_subs,db_inapp;
    String company,store, device, email, app_validity,dashboard_validity;
    TextView tv_valid_app;
    TextView tv_dashboard_validity;
    ProgressBar progressBar;
    ScrollView scroll;
    Button btn_cloud_buy;
    TextView tv_msg_count, tv_order_count;
    String value,remaining_msgs, value_order,remaining_orders;
    int count1 = 0;
    int count_inside1 = 0;
    String checking;
    String payload, bucketName;
    int state;
    public static final String PACKAGE_NAME = "com.intuition.ivepos";
    int i;
    String mSelectedSubscriptionPeriod="",mSelectedMesaageSubscription="",mSelectedProSubscription="", mSelectedonlineorderSubscription="";
    int countlimit;
    TextView total_msgs, total_order, tv_provalidity;
    Button pro_buy;
    RequestQueue requestQueue;

    String currentDateandTime1_pro;
    int i_pro_sel;
    Dialog dialog_pro;

    String WebserviceUrl;
    private ProgressDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cloud_dashboard_subscription_enabled);

//        progressBar_license = (CardView) findViewById(R.id.progressbar1);

        tv_dashboard_validity = (TextView) findViewById(R.id.valid);
        btn_cloud_buy = (Button) findViewById(R.id.cloud_buy);
        tv_msg_count = (TextView) findViewById(R.id.textvalue);
        tv_order_count = (TextView) findViewById(R.id.online_order_textvalue);
        total_msgs=(TextView) findViewById(R.id.total_msgs);
        total_order=(TextView) findViewById(R.id.online_order_total_msgs);
        progressBar = (ProgressBar) findViewById(R.id.proceed_button);
        scroll = (ScrollView) findViewById(R.id.scroll);
        tv_valid_app = (TextView) findViewById(R.id.valid_app);
        tv_provalidity=(TextView) findViewById(R.id.tv_provalidity);
        pro_buy=findViewById(R.id.pro_buy);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(CloudSubscritionActivity.this);
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

        dialog = new ProgressDialog(CloudSubscritionActivity.this, R.style.timepicker_date_dialog);

        DownloadMusicfromInternet2 downloadMusicfromInternet = new DownloadMusicfromInternet2();
        downloadMusicfromInternet.execute();






    }

    class DownloadMusicfromInternet2 extends AsyncTask<Void, Integer, Void> {


        @Override
        protected Void doInBackground(Void... params) {

//            CloudSubscritionActivity.this.deleteDatabase("amazoninapp");
//            CloudSubscritionActivity.this.deleteDatabase("dashboard_subscription");

            SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getApplicationContext());
            final String company= sharedpreferences.getString("companyname", null);
            final String store= sharedpreferences.getString("storename", null);
            final String device= sharedpreferences.getString("devicename", null);
            final String email=sharedpreferences.getString("emailid",null);


//            progressBar_license.setVisibility(View.VISIBLE);
            requestQueue = Volley.newRequestQueue(CloudSubscritionActivity.this);
            StringRequest sr = new StringRequest(
                    Request.Method.POST,
                    WebserviceUrl+"getlicense.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject=new JSONObject(response);

                                String status=jsonObject.getString("status");

                                if(status.equalsIgnoreCase("success")){

                                    //    progressBar_license.setVisibility(View.GONE);

                                    String postodate=jsonObject.getString("postodate");
                                    String dashboardexpiry=jsonObject.getString("dashboardexpiry");
                                    String remaining_msgs=jsonObject.getString("remaining_msgs");
                                    String remaining_orders=jsonObject.getString("remaining_orders");
                                    String proactivated=jsonObject.getString("proactivated");
                                    String pro_expiry=jsonObject.getString("pro_expiry");

                                    String match = " ";
                                    int position = pro_expiry.toString().indexOf(match);
                                    String mod2 = pro_expiry.toString().substring(0, position);
                                    mod2 = mod2.replace("-", "");



                                    SQLiteDatabase db = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);


                                    db.execSQL("CREATE TABLE if not exists credentialstime (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, orderid text,time text,date text,trial_time text, " +
                                            "email_id text, company_name text, store_name text, dev_name text, todate text);");
                                    db.execSQL("DELETE FROM credentialstime");

                                    ContentValues contentValues=new ContentValues();
                                    contentValues.put("email_id", email);
                                    contentValues.put("company_name", company);
                                    contentValues.put("store_name", store);
                                    contentValues.put("dev_name", device);

                                    String[] separated = postodate.split(" ");
                                    String convertedDate=separated[0].replace("-","");


                                    Log.e("converted pos date",convertedDate);
                                    contentValues.put("todate", convertedDate);
                                    db.insert("credentialstime", null, contentValues);


                                    db.execSQL("CREATE TABLE if not exists Pro_upgrade (_id integer PRIMARY KEY UNIQUE, status text, orderid text);");
                                    db.execSQL("DELETE FROM Pro_upgrade");
                                    db = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
                                    Cursor co31 = db.rawQuery("SELECT * FROM Pro_upgrade", null);
                                    int cou31 = co31.getColumnCount();
                                    if (String.valueOf(cou31).equals("3")) {
                                        db.execSQL("ALTER TABLE Pro_upgrade ADD COLUMN pro_expiry");

                                        ContentValues cv = new ContentValues();
                                        if(proactivated.equalsIgnoreCase("NO")){
                                            cv.put("status", "Not Activated");
                                            cv.put("pro_expiry", mod2);
                                            db.insert("Pro_upgrade", null, cv);
                                        }else{
                                            cv.put("status", "Activated");
                                            cv.put("pro_expiry", mod2);
                                            db.insert("Pro_upgrade", null, cv);
                                        }
                                    }else {
                                        ContentValues cv = new ContentValues();
                                        if(proactivated.equalsIgnoreCase("NO")){
                                            cv.put("status", "Not Activated");
                                            cv.put("pro_expiry", mod2);
                                            db.insert("Pro_upgrade", null, cv);
                                        }else{
                                            cv.put("status", "Activated");
                                            cv.put("pro_expiry", mod2);
                                            db.insert("Pro_upgrade", null, cv);
                                        }
                                    }
                                    co31.close();



                                    db.execSQL("CREATE TABLE if not exists Messaginglicense (_id integer PRIMARY KEY UNIQUE, remainingmessages text, Messagessent text,orderid text,time text,date text, package text);");
                                    db.execSQL("DELETE FROM Messaginglicense");
                                    ContentValues cv1 = new ContentValues();
                                    cv1.put("Messagessent", "0");
                                    cv1.put("remainingmessages", Integer.parseInt(remaining_msgs));
                                    cv1.put("date", "");
                                    cv1.put("time", "");
                                    db.insert("Messaginglicense", null, cv1);



                                    db.execSQL("CREATE TABLE if not exists Orderlicense (_id integer PRIMARY KEY UNIQUE, remainingorders text, ordersrece text,orderid text,time text,date text, package text);");
                                    db.execSQL("DELETE FROM Orderlicense");
                                    ContentValues cv2 = new ContentValues();
                                    cv2.put("ordersrece", "0");
                                    cv2.put("remainingorders", Integer.parseInt(remaining_orders));
                                    cv2.put("date", "");
                                    cv2.put("time", "");
                                    db.insert("Orderlicense", null, cv2);


                                    SQLiteDatabase db_subs = openOrCreateDatabase("dashboard_subscription", Context.MODE_PRIVATE, null);

                                    db_subs.execSQL("CREATE TABLE if not exists subscription (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, orderid text,time text,date text,trial_time text, " +
                                            "email_id text, company_name text, store_name text, dev_name text, todate text);");
                                    db_subs.execSQL("DELETE FROM subscription");

                                    String[] separateds = dashboardexpiry.split(" ");
                                    String dashboardexpiryDate=separateds[0].replace("-","");

                                    ContentValues cv11=new ContentValues();
                                    cv11.put("todate", dashboardexpiryDate);
                                    cv11.put("email_id", email);
                                    cv11.put("company_name", company);
                                    cv11.put("store_name", store);
                                    cv11.put("dev_name", device);
                                    db_subs.insert("subscription", null, cv11);

                                    initviews();

                                }else{
                                    //    progressBar_license.setVisibility(View.GONE);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Signup confirm", "Error: " + error.getMessage());
                            initviews();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("company", company+ "");
                    params.put("store", store+ "");
                    params.put("device", device);
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(sr);


            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage("Loading.");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            dialog.setMax(1000);
            //Set the current progress to zero
            dialog.setProgress(0);




            dialog.show();
        }


        @Override
        protected void onPostExecute(Void result) {





        }
    }

    public void initviews() {
        dialog.dismiss();
        mHelper = new IabHelper(CloudSubscritionActivity.this,  base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result)
            {
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app Billing setup failed: " +
                            result);
                } else {
                    Log.d(TAG, "In-app Billing is set up OK");
                }
            }
        });


        mHelperMessage = new IabHelper(CloudSubscritionActivity.this,  base64EncodedPublicKey1);

        mHelperMessage.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result)
            {
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app Billing setup failed: " +
                            result);
                } else {
                    Log.d(TAG, "In-app Billing is set up OK");
                    try {
                        mHelperMessage.queryInventoryAsync(mReceivedInventoryListenerMessage);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                    }
                }

            }
        });



        mHelperOrder = new IabHelper(CloudSubscritionActivity.this,  base64EncodedPublicKey2);

        mHelperOrder.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result)
            {
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app Billing setup failed: " +
                            result);
                } else {
                    Log.d(TAG, "In-app Billing is set up OK");
                    try {
                        mHelperOrder.queryInventoryAsync(mReceivedInventoryListenerOrder);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                    }
                }

            }
        });



        mHelperPro = new IabHelper(CloudSubscritionActivity.this,  base64EncodedPublicKey1);

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

        SimpleDateFormat sdf2_pro = new SimpleDateFormat("yyyyMMdd");
        currentDateandTime1_pro = sdf2_pro.format(new Date());

        LinearLayout back_activity = (LinearLayout) findViewById(R.id.back_activity);
        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CloudSubscritionActivity.this, MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        LinearLayout account_details = (LinearLayout) findViewById(R.id.account_details);
        account_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(CloudSubscritionActivity.this);
                final String company= sharedpreferences.getString("companyname", null);
                final String store= sharedpreferences.getString("storename", null);
                final String device= sharedpreferences.getString("devicename", null);
                final String companyname_special= sharedpreferences.getString("companyname_special", null);

                final Dialog dialog_comp_details = new Dialog(CloudSubscritionActivity.this, R.style.timepicker_date_dialog);
                dialog_comp_details.setContentView(R.layout.company_details_notedit);
                dialog_comp_details.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog_comp_details.show();

                ImageButton cancel = (ImageButton) dialog_comp_details.findViewById(R.id.btncancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_comp_details.dismiss();
                    }
                });

                TextView company_name = (TextView) dialog_comp_details.findViewById(R.id.company_name);
                TextView store_name = (TextView) dialog_comp_details.findViewById(R.id.store_name);
                TextView device_name = (TextView) dialog_comp_details.findViewById(R.id.device_name);

                company_name.setText(companyname_special);
                store_name.setText(store);
                device_name.setText(device);

            }
        });

        //   if(databaseExist()){

        db_subs = openOrCreateDatabase("dashboard_subscription", Context.MODE_PRIVATE, null);
        db_subs.execSQL("CREATE TABLE if not exists subscription (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, orderid text,time text,date text,trial_time text, " +
                "email_id text, company_name text, store_name text, dev_name text, todate text);");
        db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);


        Cursor cursor = db_inapp.rawQuery("SELECT * FROM credentialstime", null);
        if (cursor.moveToFirst()) {
            company = cursor.getString(6);
            store = cursor.getString(7);
            device = cursor.getString(8);
            email = cursor.getString(5);
            app_validity = cursor.getString(9);
        }




        if((app_validity!=null)&&(!app_validity.equalsIgnoreCase(""))){
            String ye1 = app_validity.substring(0, 4);
            String mon1 = app_validity.substring(4, 6);
            String day1 = app_validity.substring(6, 8);
            tv_valid_app.setText(day1 + "/" + mon1 + "/" + ye1);
        }


        Cursor cursor1 = db_subs.rawQuery("SELECT * FROM subscription", null);
        if (cursor1.moveToFirst()) {
            dashboard_validity = cursor1.getString(9);
        }

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        final String currentDateandTime1 = sdf2.format(new Date());
        final int intdate = Integer.parseInt(currentDateandTime1);
        if(dashboard_validity==null) {
            tv_dashboard_validity.setText("Not Subscribed");
            btn_cloud_buy.setVisibility(View.VISIBLE);
        }else{
            if((dashboard_validity!=null)&&(!dashboard_validity.equalsIgnoreCase(""))){
                String ye = dashboard_validity.substring(0, 4);
                String mon = dashboard_validity.substring(4, 6);
                String day = dashboard_validity.substring(6, 8);
                tv_dashboard_validity.setText(day + "/" + mon + "/" + ye);
            }

        }


        Cursor cursor11 = db_inapp.rawQuery("SELECT * FROM Messaginglicense", null);
        if (cursor11.moveToFirst()) {
            value = cursor11.getString(2);
            remaining_msgs=cursor11.getString(1);
        }
        int totalmsgs=Integer.parseInt(value)+Integer.parseInt(remaining_msgs);
        tv_msg_count.setText(value);
        total_msgs.setText(totalmsgs+"");


        Cursor cursor12 = db_inapp.rawQuery("SELECT * FROM Orderlicense", null);
        if (cursor12.moveToFirst()) {
            value_order = cursor12.getString(2);
            remaining_orders=cursor12.getString(1);
        }
        int totalorders=Integer.parseInt(value_order)+Integer.parseInt(remaining_orders);
        tv_order_count.setText(value_order);
        total_order.setText(totalorders+"");


        if(dashboard_validity==null){
            tv_dashboard_validity.setText("Not Subscribed");
            btn_cloud_buy.setVisibility(View.VISIBLE);
        }else{
            if ((dashboard_validity!=null)&&(!dashboard_validity.equalsIgnoreCase(""))) {
                if((intdate <= Integer.parseInt(dashboard_validity))){
                    btn_cloud_buy.setVisibility(View.INVISIBLE);
                }else{
                    btn_cloud_buy.setVisibility(View.VISIBLE);
                }

            } else {
                tv_dashboard_validity.setText("Not Subscribed");
                btn_cloud_buy.setVisibility(View.VISIBLE);
            }
        }




        Cursor cursor_pro=db_inapp.rawQuery("SELECT * FROM Pro_upgrade", null);
        if(cursor_pro.moveToFirst()){

            String status=cursor_pro.getString(1);
            String status_da=cursor_pro.getString(3);
            if(status.equalsIgnoreCase("Activated")){

                if (Integer.parseInt(status_da) > Integer.parseInt(currentDateandTime1_pro)) {

                    pro_buy.setVisibility(View.INVISIBLE);
                    if ((app_validity != null) && (!app_validity.equalsIgnoreCase(""))) {
                        String ye1 = app_validity.substring(0, 4);
                        String mon1 = app_validity.substring(4, 6);
                        String day1 = app_validity.substring(6, 8);
                        tv_provalidity.setText("Activated");
                    }
                }else {
                    pro_buy.setVisibility(View.VISIBLE);
                }
            }else{
                pro_buy.setVisibility(View.VISIBLE);
            }


        }else{

            pro_buy.setVisibility(View.VISIBLE);


        }




        //   }


        pro_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dialog_pro = new Dialog(CloudSubscritionActivity.this, R.style.notitle);
                dialog_pro.setContentView(R.layout.dialog_pro_purchase);
                dialog_pro.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog_pro.show();

                final RadioGroup radioGroupSplit = (RadioGroup) dialog_pro.findViewById(R.id.splitgroup);

                final RadioButton one_demo = (RadioButton) dialog_pro.findViewById(R.id.btnone);
                final RadioButton two_buy = (RadioButton) dialog_pro.findViewById(R.id.btntwo);
                final RadioButton three_coupon = (RadioButton) dialog_pro.findViewById(R.id.btnthree);



                Cursor cursor_pro=db_inapp.rawQuery("SELECT * FROM Pro_upgrade", null);
                if(cursor_pro.moveToFirst()){
                    String status=cursor_pro.getString(1);
                    String status_da=cursor_pro.getString(3);
                    if(status.equalsIgnoreCase("Activated")){
                        if (Integer.parseInt(status_da) > Integer.parseInt(currentDateandTime1_pro)) {
                            one_demo.setVisibility(View.GONE);
                            two_buy.setChecked(true);
                        }else {
                            one_demo.setVisibility(View.GONE);
                            two_buy.setChecked(true);
                        }
                    }else{
                        one_demo.setVisibility(View.VISIBLE);
                        one_demo.setChecked(true);
                    }
                }

                Button procancel = (Button) dialog_pro.findViewById(R.id.procancel);
                procancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_pro.dismiss();
                    }
                });

                ImageView closetext = (ImageView) dialog_pro.findViewById(R.id.closetext);
                closetext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_pro.dismiss();
                    }
                });

                Button proapply = (Button) dialog_pro.findViewById(R.id.proapply);
                proapply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final int selected1 = radioGroupSplit.getCheckedRadioButtonId();
                        RadioButton radioBtnSplit = (RadioButton) dialog_pro.findViewById(selected1);

                        if (one_demo.isChecked()) {
//                            Toast.makeText(CloudSubscritionActivity.this, "demo", Toast.LENGTH_LONG).show();

                            i_pro_sel = 1;
                            checking="123";
                            updateProCloud("");

                        }else {
                            if (two_buy.isChecked()){
//                                Toast.makeText(CloudSubscritionActivity.this, "buy", Toast.LENGTH_LONG).show();
                                dialog_pro.dismiss();

                                mSelectedProSubscription=SKU_DELAROY_PRO_UPGRADE;


                                try {
                                    mHelperPro.launchPurchaseFlow(CloudSubscritionActivity.this, mSelectedProSubscription, REQ_CODE,
                                            mPurchaseFinishedListenerPro, mSelectedProSubscription);
                                } catch (IabHelper.IabAsyncInProgressException e) {
                                    //  complain("Error launching purchase flow. Another async operation in progress.");
                                    // setWaitScreen(false);
                                }


                            }else {
                                if (three_coupon.isChecked()){
//                                    Toast.makeText(CloudSubscritionActivity.this, "coupon", Toast.LENGTH_LONG).show();

                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(CloudSubscritionActivity.this);
                                    alertDialog.setTitle(getString(R.string.title13));
                                    alertDialog.setMessage(getString(R.string.setmessage15));
                                    final EditText input = new EditText(CloudSubscritionActivity.this);
                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT);
                                    input.setLayoutParams(lp);
                                    alertDialog.setView(input);

                                    alertDialog.setPositiveButton("Submit",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String code = input.getText().toString();

                                                    i_pro_sel = 3;
                                                    checking=code;
                                                    updateProCloud(code);

                                                }
                                            });

                                    alertDialog.setNegativeButton("NO",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                    alertDialog.show();

                                }
                            }
                        }
                    }
                });

//                CharSequence[] options;
//                options = new CharSequence[3];
//                options[0] = getString(R.string.subscription_period_pro_demo);
//                options[1] = getString(R.string.subscription_period_pro_yearly1);
//                options[2] = getString(R.string.subscription_period_pro_coupon);
//
////                mSelectedMesaageSubscription = SKU_DELAROY_MESSAGING_WEEKLY;
//                int titleResId = R.string.subscription_period_prompt;
//                AlertDialog.Builder builder = new AlertDialog.Builder(CloudSubscritionActivity.this);
//                builder.setTitle(titleResId)
//                        .setSingleChoiceItems(options, 0 /* checkedItem */, oncicklistenerpro)
//                        .setPositiveButton(R.string.subscription_prompt_continue, oncicklistenerpro)
//                        .setNegativeButton(R.string.subscription_prompt_cancel, oncicklistenerpro);
//                AlertDialog dialog = builder.create();
//                dialog.show();

//                mSelectedProSubscription=SKU_DELAROY_PRO_UPGRADE;
//
//
//                try {
//                    mHelperPro.launchPurchaseFlow(CloudSubscritionActivity.this, mSelectedProSubscription, REQ_CODE,
//                            mPurchaseFinishedListenerPro, mSelectedProSubscription);
//                } catch (IabHelper.IabAsyncInProgressException e) {
//                    //  complain("Error launching purchase flow. Another async operation in progress.");
//                    // setWaitScreen(false);
//                }

            }
        });
    }

    public boolean databaseExist() {
        File DATA_DIRECTORY_DATABASE =
                new File(Environment.getDataDirectory() +
                        "/data/" + "com.intuition.ivepos" +
                        "/databases/" + "dashboard_subscription");

        return DATA_DIRECTORY_DATABASE.exists();
    }


    public void onSubscribeButtonClicked_dashboard(View arg0){

        CharSequence[] options;
        options = new CharSequence[5];
        options[0] = getString(R.string.subscription_period_weekly_dashboard);
        options[1] = getString(R.string.subscription_period_monthly_dashboard);
        options[2] = getString(R.string.subscription_period_threemonth_dashboard);
        options[3] = getString(R.string.subscription_period_sixmonth_dashboard);
        options[4] = getString(R.string.subscription_period_yearly_dashboard);

        mSelectedSubscriptionPeriod = SKU_DELAROY_DASHBOARD_WEEKLY;

        int titleResId = R.string.subscription_period_prompt;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titleResId)
                .setSingleChoiceItems(options, 0 /* checkedItem */, oncicklistener)
                .setPositiveButton(R.string.subscription_prompt_continue, oncicklistener)
                .setNegativeButton(R.string.subscription_prompt_cancel, oncicklistener);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void onBuyClicked(View arg0){
        CharSequence[] options;
        options = new CharSequence[5];
        options[0] = getString(R.string.subscription_period_monthly1);
        options[1] = getString(R.string.subscription_period_yearly1);
        options[2] = getString(R.string.subscription_period_threemonth1);
        options[3] = getString(R.string.subscription_period_sixmonth1);
        options[4] = getString(R.string.subscription_period_ninemonth1);

        mSelectedMesaageSubscription = SKU_DELAROY_MESSAGING_WEEKLY;
        int titleResId = R.string.subscription_period_prompt;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titleResId)
                .setSingleChoiceItems(options, 0 /* checkedItem */, oncicklistenermessage)
                .setPositiveButton(R.string.subscription_prompt_continue, oncicklistenermessage)
                .setNegativeButton(R.string.subscription_prompt_cancel, oncicklistenermessage);
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    public void onBuyClicked_onlineorder(View arg0){
        CharSequence[] options;
        options = new CharSequence[3];
        options[0] = getString(R.string.subscription_period_onlineorder1);
        options[1] = getString(R.string.subscription_period_onlineorder2);
        options[2] = getString(R.string.subscription_period_onlineorder3);

        mSelectedonlineorderSubscription = SKU_DELAROY_Online_order_1;
        int titleResId = R.string.online_order_prompt;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titleResId)
                .setSingleChoiceItems(options, 0 /* checkedItem */, oncicklistenerorders)
                .setPositiveButton(R.string.subscription_prompt_continue, oncicklistenerorders)
                .setNegativeButton(R.string.subscription_prompt_cancel, oncicklistenerorders);
        AlertDialog dialog = builder.create();
        dialog.show();
    }




    DialogInterface.OnClickListener oncicklistener=new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface dialog, int id) {

            if (id == 0 /* First choice item */) {
                i=0;
                mSelectedSubscriptionPeriod = SKU_DELAROY_DASHBOARD_WEEKLY;
            } else if (id == 1 /* Second choice item */) {
                i=1;
                mSelectedSubscriptionPeriod = SKU_DELAROY_DASHBOARD_MONTHLY;
            } else if (id == 2) {
                i=2;
                mSelectedSubscriptionPeriod = SKU_DELAROY_DASHBOARD_THREEMONTH;

            } else if (id == 3) {
                i=3;
                mSelectedSubscriptionPeriod = SKU_DELAROY_DASHBOARD_SIXMONTH;

            } else if (id == 4) {
                i=4;
                mSelectedSubscriptionPeriod = SKU_DELAROY_DASHBOARD_YEARLY;

            } else if (id == DialogInterface.BUTTON_POSITIVE /* continue button */) {


                try {
                    mHelper.launchSubscriptionPurchaseFlow(CloudSubscritionActivity.this, mSelectedSubscriptionPeriod, REQ_CODE,
                            mPurchaseFinishedListener, mSelectedSubscriptionPeriod);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    //  complain("Error launching purchase flow. Another async operation in progress.");
                    // setWaitScreen(false);
                }



            } else if (id == DialogInterface.BUTTON_NEGATIVE) {
                dialog.cancel();
            }



        }
    };



    DialogInterface.OnClickListener oncicklistenermessage=new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface dialog, int id) {

            if (id == 0 /* First choice item */) {
                i=0;
                mSelectedMesaageSubscription = SKU_DELAROY_MESSAGING_WEEKLY;
            } else if (id == 1 /* Second choice item */) {
                i=1;
                mSelectedMesaageSubscription = SKU_DELAROY_MESSAGING_MONTHLY;
            } else if (id == 2) {
                i=2;
                mSelectedMesaageSubscription = SKU_DELAROY_MESSAGING_THREEMONTH;

            } else if (id == 3) {
                i=3;
                mSelectedMesaageSubscription = SKU_DELAROY_MESSAGING_SIXMONTH;

            } else if (id == 4) {
                i=4;
                mSelectedMesaageSubscription = SKU_DELAROY_MESSAGING_YEARLY;

            } else if (id == DialogInterface.BUTTON_POSITIVE /* continue button */) {


                try {
                    mHelperMessage.launchPurchaseFlow(CloudSubscritionActivity.this, mSelectedMesaageSubscription, REQ_CODE,
                            mPurchaseFinishedListenerMessage, mSelectedMesaageSubscription);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    //  complain("Error launching purchase flow. Another async operation in progress.");
                    // setWaitScreen(false);
                }



            } else if (id == DialogInterface.BUTTON_NEGATIVE) {
                dialog.cancel();
            }



        }
    };



    DialogInterface.OnClickListener oncicklistenerorders=new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface dialog, int id) {

            if (id == 0 /* First choice item */) {
                i=0;
                mSelectedonlineorderSubscription = SKU_DELAROY_Online_order_1;
            } else if (id == 1 /* Second choice item */) {
                i=1;
                mSelectedonlineorderSubscription = SKU_DELAROY_Online_order_2;
            } else if (id == 2) {
                i=2;
                mSelectedonlineorderSubscription = SKU_DELAROY_Online_order_3;

            } else if (id == DialogInterface.BUTTON_POSITIVE /* continue button */) {


                try {
                    mHelperOrder.launchPurchaseFlow(CloudSubscritionActivity.this, mSelectedonlineorderSubscription, REQ_CODE,
                            mPurchaseFinishedListenerOrder, mSelectedonlineorderSubscription);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    //  complain("Error launching purchase flow. Another async operation in progress.");
                    // setWaitScreen(false);
                }



            } else if (id == DialogInterface.BUTTON_NEGATIVE) {
                dialog.cancel();
            }



        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
        if (!mHelperMessage.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
        if (!mHelperOrder.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (!mHelperPro.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(mSelectedSubscriptionPeriod)) {
                consumeItem();

            }

        }
    };

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListenerMessage
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(mSelectedMesaageSubscription)) {
                consumeItemMessage();

            }

        }
    };


    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListenerOrder
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(mSelectedonlineorderSubscription)) {
                consumeItemOrder();

            }

        }
    };

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



    public void consumeItem() {
        try {
            mHelper.queryInventoryAsync(mReceivedInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }


    public void consumeItemMessage() {
        try {
            mHelperMessage.queryInventoryAsync(mReceivedInventoryListenerMessage);
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    public void consumeItemOrder() {
        try {
            mHelperOrder.queryInventoryAsync(mReceivedInventoryListenerOrder);
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    public void consumeItemPro() {
        try {
            mHelperPro.queryInventoryAsync(mReceivedInventoryListenerPro);
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {


            if (result.isFailure()) {
                // Handle failure
            } else {

                Purchase purchase = inventory.getPurchase(mSelectedSubscriptionPeriod);
                updateCloud(purchase);
                /*    mHelper.consumeAsync(inventory.getPurchase(mSelectedSubscriptionPeriod),
                            mConsumeFinishedListener);*/

            }
        }
    };



    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListenerMessage
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {


            if (result.isFailure()) {
                // Handle failure
            } else {

                if(mSelectedMesaageSubscription.equalsIgnoreCase("")){

                    List<Purchase> list=inventory.getAllPurchases();

                    for(int k=0;k<list.size();k++){
                        Purchase mPurchase = list.get(k);
                        if (mPurchase != null && verifyDeveloperPayload(mPurchase)) {
                            Log.d(TAG, "We have gas. Consuming it.");
                            String token=mPurchase.getSku();
                            if((token.equalsIgnoreCase(SKU_DELAROY_MESSAGING_MONTHLY))||
                                    (token.equalsIgnoreCase(SKU_DELAROY_MESSAGING_YEARLY))||
                                    (token.equalsIgnoreCase(SKU_DELAROY_MESSAGING_THREEMONTH))||
                                    (token.equalsIgnoreCase(SKU_DELAROY_MESSAGING_SIXMONTH))||
                                    (token.equalsIgnoreCase(SKU_DELAROY_MESSAGING_WEEKLY))){

                                try {
                                    mHelperMessage.consumeAsync(mPurchase,
                                            mConsumeFinishedListener);
                                } catch (IabHelper.IabAsyncInProgressException e) {
                                    e.printStackTrace();
                                }

                            }

                            return;
                        }

                    }


                }else{
                    Purchase purchase = inventory.getPurchase(mSelectedMesaageSubscription);
                    checking=purchase.getOrderId();
                    try {
                        mHelperMessage.consumeAsync(inventory.getPurchase(mSelectedMesaageSubscription),
                                mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                    }
                }





            }
        }
    };



    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListenerOrder
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {


            if (result.isFailure()) {
                // Handle failure
            } else {

                if(mSelectedonlineorderSubscription.equalsIgnoreCase("")){

                    List<Purchase> list=inventory.getAllPurchases();

                    for(int k=0;k<list.size();k++){
                        Purchase mPurchase = list.get(k);
                        if (mPurchase != null && verifyDeveloperPayload(mPurchase)) {
                            Log.d(TAG, "We have gas. Consuming it.");
                            String token=mPurchase.getSku();
                            if((token.equalsIgnoreCase(SKU_DELAROY_Online_order_1))||
                                    (token.equalsIgnoreCase(SKU_DELAROY_Online_order_2))||
                                    (token.equalsIgnoreCase(SKU_DELAROY_Online_order_3))){

                                try {
                                    mHelperOrder.consumeAsync(mPurchase,
                                            mConsumeFinishedListenerorder);
                                } catch (IabHelper.IabAsyncInProgressException e) {
                                    e.printStackTrace();
                                }

                            }

                            return;
                        }

                    }


                }else{
                    Purchase purchase = inventory.getPurchase(mSelectedonlineorderSubscription);
                    checking=purchase.getOrderId();
                    try {
                        mHelperOrder.consumeAsync(inventory.getPurchase(mSelectedonlineorderSubscription),
                                mConsumeFinishedListenerorder);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                    }
                }





            }
        }
    };



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
                    checking=purchase.getOrderId();
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









    boolean verifyDeveloperPayload(final Purchase p) {
        payload = p.getDeveloperPayload();
        checking = p.getOrderId();
        state = p.getPurchaseState();
        return true;
    }


    private void updateMessageCloud() {
        int addcount=0;
        if(i==0){
            addcount=500;
        }else  if(i==1){
            addcount=1000;
        }else if(i==2){
            addcount=2500;
        }else if(i==3){
            addcount=5000;
        }else if(i==4){
            addcount=10000;
        }

        db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
        Cursor cursor = db_inapp.rawQuery("SELECT * FROM Messaginglicense", null);
        if (cursor.moveToFirst()) {
            do {
                String idss = cursor.getString(0);
                String countval = cursor.getString(2);

                Cursor cursor1 = db_inapp.rawQuery("SELECT * FROM Messaginglicense WHERE Messagessent = '" + countval + "'", null);
                if (cursor1.moveToFirst()) {
                    String id1 = cursor1.getString(0);
                    String limit = cursor1.getString(1);
                    countlimit = Integer.parseInt(limit);
                    ContentValues contentValues = new ContentValues();

                    contentValues.put("remainingmessages", countlimit + addcount);

                    String where = "_id = '" + id1 + "'";
                    db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
                    db_inapp.update("Messaginglicense", contentValues, where, new String[]{});
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        uploadMessage();


    }



    private void updateOrderCloud() {
        int addcount=0;
        if(i==0){
            addcount=500;
        }else  if(i==1){
            addcount=1000;
        }else if(i==2){
            addcount=5000;
        }

        db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
        Cursor cursor = db_inapp.rawQuery("SELECT * FROM Orderlicense", null);
        if (cursor.moveToFirst()) {
            do {
                String idss = cursor.getString(0);
                String countval = cursor.getString(2);

                Cursor cursor1 = db_inapp.rawQuery("SELECT * FROM Orderlicense WHERE ordersrece = '" + countval + "'", null);
                if (cursor1.moveToFirst()) {
                    String id1 = cursor1.getString(0);
                    String limit = cursor1.getString(1);
                    countlimit = Integer.parseInt(limit);
                    ContentValues contentValues = new ContentValues();

                    contentValues.put("remainingorders", countlimit + addcount);

                    String where = "_id = '" + id1 + "'";
                    db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
                    db_inapp.update("Orderlicense", contentValues, where, new String[]{});
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        uploadOrder();


    }



    private void updateProCloud(String orderid) {

        db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
        Cursor cursor_pro=db_inapp.rawQuery("SELECT * FROM Pro_upgrade", null);
        if(cursor_pro.moveToFirst()){

            int id=cursor_pro.getInt(0);
            String status=cursor_pro.getString(1);
            if(!status.equalsIgnoreCase("Activated")){
                ContentValues contentValues =new ContentValues();
                contentValues.put("status","Activated");
                contentValues.put("orderid",orderid);
                if (i_pro_sel == 1){
                    checking="123";
                    mSelectedProSubscription = SKU_DELAROY_PRO_UPGRADE_DEMO;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                    Calendar currentCal = Calendar.getInstance();
                    String currentdate = dateFormat.format(currentCal.getTime());
                    currentCal.add(Calendar.DATE, 7);
                    String toDate = dateFormat.format(currentCal.getTime());
                    contentValues.put("pro_expiry",toDate);
                }else {
                    if (i_pro_sel == 2){
                        checking="123";
                        mSelectedProSubscription = SKU_DELAROY_PRO_UPGRADE;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                        Calendar currentCal = Calendar.getInstance();
                        String currentdate = dateFormat.format(currentCal.getTime());
                        currentCal.add(Calendar.DATE, 365);
                        String toDate = dateFormat.format(currentCal.getTime());
                        contentValues.put("pro_expiry",toDate);
                    }else {
                        checking=orderid;
                        mSelectedProSubscription = SKU_DELAROY_PRO_UPGRADE;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                        Calendar currentCal = Calendar.getInstance();
                        String currentdate = dateFormat.format(currentCal.getTime());
                        currentCal.add(Calendar.DATE, 365);
                        String toDate = dateFormat.format(currentCal.getTime());
                        contentValues.put("pro_expiry",toDate);
                    }
                }
                String where = "_id = '" + id + "'";
                db_inapp.update("Pro_upgrade", contentValues, where, new String[]{});
            }

        }else{

            ContentValues contentValues =new ContentValues();
            contentValues.put("status","Activated");
            contentValues.put("orderid",orderid);
            if (i_pro_sel == 1){
                checking="123";
                mSelectedProSubscription = SKU_DELAROY_PRO_UPGRADE_DEMO;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                Calendar currentCal = Calendar.getInstance();
                String currentdate = dateFormat.format(currentCal.getTime());
                currentCal.add(Calendar.DATE, 7);
                String toDate = dateFormat.format(currentCal.getTime());
                contentValues.put("pro_expiry",toDate);
            }else {
                if (i_pro_sel == 2){
                    checking="123";
                    mSelectedProSubscription = SKU_DELAROY_PRO_UPGRADE;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                    Calendar currentCal = Calendar.getInstance();
                    String currentdate = dateFormat.format(currentCal.getTime());
                    currentCal.add(Calendar.DATE, 365);
                    String toDate = dateFormat.format(currentCal.getTime());
                    contentValues.put("pro_expiry",toDate);
                }else {
                    checking=orderid;
                    mSelectedProSubscription = SKU_DELAROY_PRO_UPGRADE;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                    Calendar currentCal = Calendar.getInstance();
                    String currentdate = dateFormat.format(currentCal.getTime());
                    currentCal.add(Calendar.DATE, 365);
                    String toDate = dateFormat.format(currentCal.getTime());
                    contentValues.put("pro_expiry",toDate);
                }
            }
            db_inapp.insert("Pro_upgrade", null, contentValues);
        }

        uploadPro();
    }


    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {

                        if(mSelectedMesaageSubscription.equalsIgnoreCase("")){

                        }else{
                            updateMessageCloud();
                        }


                    } else {
                        // handle error
                    }
                }
            };



    IabHelper.OnConsumeFinishedListener mConsumeFinishedListenerorder =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {

                        if(mSelectedonlineorderSubscription.equalsIgnoreCase("")){

                        }else{
                            updateOrderCloud();
                        }


                    } else {
                        // handle error
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




    private void updateCloud(Purchase purchase) {
        payload = purchase.getDeveloperPayload();
        checking = purchase.getOrderId();
        state = purchase.getPurchaseState();

        SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyyyy");
        final String normal1 = normal2.format(new Date());

        Date dt = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
        final String time1 = sdf1.format(dt);

        ContentValues contentValues = new ContentValues();
        contentValues.put("orderid", checking);
        contentValues.put("time", time1);
        contentValues.put("date", normal1);
        if (i == 0) {
            contentValues.put("trial_time", "7days");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Calendar currentCal = Calendar.getInstance();
            String currentdate = dateFormat.format(currentCal.getTime());
            currentCal.add(Calendar.DATE, 7);
            String toDate = dateFormat.format(currentCal.getTime());

            contentValues.put("todate", toDate);
        } else if (i == 1) {
            contentValues.put("trial_time", "1month");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Calendar currentCal = Calendar.getInstance();
            String currentdate = dateFormat.format(currentCal.getTime());
            currentCal.add(Calendar.DATE, 30);
            String toDate = dateFormat.format(currentCal.getTime());

            contentValues.put("todate", toDate);
        } else if (i == 2) {
            contentValues.put("trial_time", "3month");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Calendar currentCal = Calendar.getInstance();
            String currentdate = dateFormat.format(currentCal.getTime());
            currentCal.add(Calendar.DATE, 90);
            String toDate = dateFormat.format(currentCal.getTime());

            contentValues.put("todate", toDate);
        } else if (i == 3) {
            contentValues.put("trial_time", "6month");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Calendar currentCal = Calendar.getInstance();
            String currentdate = dateFormat.format(currentCal.getTime());
            currentCal.add(Calendar.DATE, 180);
            String toDate = dateFormat.format(currentCal.getTime());

            contentValues.put("todate", toDate);
        } else if (i == 4) {
            contentValues.put("trial_time", "1year");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Calendar currentCal = Calendar.getInstance();
            String currentdate = dateFormat.format(currentCal.getTime());
            currentCal.add(Calendar.DATE, 365);
            String toDate = dateFormat.format(currentCal.getTime());

            contentValues.put("todate", toDate);
        }

        contentValues.put("email_id", email);
        contentValues.put("company_name", company);
        contentValues.put("store_name", store);
        contentValues.put("dev_name", device);

        db_subs = openOrCreateDatabase("dashboard_subscription", Context.MODE_PRIVATE, null);

//        contentValues.put("");

//        Toast.makeText(MainActivity.this, "current date....." + currentdate, Toast.LENGTH_LONG).show();
//        Toast.makeText(MainActivity.this, "new date......" + toDate, Toast.LENGTH_LONG).show();

        Cursor cursor7=null;
        cursor7 = db_subs.rawQuery("SELECT * FROM subscription where store_name ='"+store+"'"+" and dev_name ='"+device+"'", null);
        if(cursor7!=null) {
            if (cursor7.moveToFirst()) {
                do {
                    String id = cursor7.getString(0);
                    String where = "_id = " + id;
                    db_subs.update("subscription", contentValues, where, new String[]{});

                } while (cursor7.moveToNext());
            }else{
                db_subs.insert("subscription", null, contentValues);

            }
        }else{
            db_subs.insert("subscription", null, contentValues);

        }

        upload();


    }
    private void upload() {
        // progressBar.setVisibility(View.GONE);

        String orderid="";
        db_subs = openOrCreateDatabase("dashboard_subscription", Context.MODE_PRIVATE, null);
        Cursor cursor_d = db_subs.rawQuery("SELECT * FROM subscription", null);
        if (cursor_d.moveToFirst()) {
            dashboard_validity = cursor_d.getString(9);
            orderid=cursor_d.getString(1);
        }
        if(dashboard_validity==null){

        }else{
            if((dashboard_validity!=null)&&(!dashboard_validity.equalsIgnoreCase(""))){
                String ye = dashboard_validity.substring(0, 4);
                String mon = dashboard_validity.substring(4, 6);
                String day = dashboard_validity.substring(6, 8);
                dashboard_validity=(ye+"-"+mon+"-"+day+" 00:00:01");
                //   tv_dashboard_validity.setText(ye+"-"+mon+"-"+day);
            }
        }
        dashboardlicense(orderid);

    }


    private void uploadMessage() {

        messagelicense(checking);
    }



    private void uploadOrder() {

        orderlicense(checking);
    }



    private void uploadPro() {
        if (i_pro_sel == 3){
            RequestQueue requestQueue;
            requestQueue = Volley.newRequestQueue(this);
            StringRequest sr = new StringRequest(
                    Request.Method.POST,
                    WebserviceUrl+"coupon_pr.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("success")) {
                                prolicense(checking);

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
                    params.put("coupon", checking+ "");
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(sr);
        }else {
            prolicense(checking);
        }
    }


    public void dashboardlicense(final String orderid){

        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        final String company= sharedpreferences.getString("companyname", null);
        final String store= sharedpreferences.getString("storename", null);
        final String device= sharedpreferences.getString("devicename", null);

        progressBar.setVisibility(View.VISIBLE);
        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"dashboard.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")){


                            progressBar.setVisibility(View.GONE);
                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                            final String currentDateandTime1 = sdf2.format(new Date());
                            final int intdate = Integer.parseInt(currentDateandTime1);

                            db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
                            db_subs = openOrCreateDatabase("dashboard_subscription", Context.MODE_PRIVATE, null);

                            Cursor cursor = db_inapp.rawQuery("SELECT * FROM credentialstime", null);
                            if (cursor.moveToFirst()) {
                                app_validity = cursor.getString(9);
                            }
                            if((app_validity!=null)&&(!app_validity.equalsIgnoreCase(""))){
                                String ye1 = app_validity.substring(0, 4);
                                String mon1 = app_validity.substring(4, 6);
                                String day1 = app_validity.substring(6, 8);
                                tv_valid_app.setText(day1 + "/" + mon1 + "/" + ye1);
                            }

                            Cursor cursor1 = db_subs.rawQuery("SELECT * FROM subscription", null);
                            if (cursor1.moveToFirst()) {
                                dashboard_validity = cursor1.getString(9);
                            }
                            if(dashboard_validity==null){

                            }else{
                                if((dashboard_validity!=null)&&(!dashboard_validity.equalsIgnoreCase(""))){
                                    String ye = dashboard_validity.substring(0, 4);
                                    String mon = dashboard_validity.substring(4, 6);
                                    String day = dashboard_validity.substring(6, 8);
                                    tv_dashboard_validity.setText(day + "/" + mon + "/" + ye);
                                }
                            }

                            if(dashboard_validity==null){
                                tv_dashboard_validity.setText("Not Subscribed");
                                btn_cloud_buy.setVisibility(View.VISIBLE);
                            }else{
                                if ((dashboard_validity!=null)&&(!dashboard_validity.equalsIgnoreCase(""))) {
                                    if((intdate <= Integer.parseInt(dashboard_validity))){
                                        btn_cloud_buy.setVisibility(View.INVISIBLE);

                                    }else{
                                        btn_cloud_buy.setVisibility(View.VISIBLE);

                                    }

                                } else {
                                    tv_dashboard_validity.setText("Not Subscribed");
                                    btn_cloud_buy.setVisibility(View.VISIBLE);
                                }
                            }


                            Cursor cursor11 = db_inapp.rawQuery("SELECT * FROM Messaginglicense", null);
                            if (cursor11.moveToFirst()) {
                                value = cursor11.getString(2);
                                remaining_msgs=cursor11.getString(1);
                            }
                            int totalmsgs=Integer.parseInt(value)+Integer.parseInt(remaining_msgs);
                            tv_msg_count.setText(value);
                            total_msgs.setText(totalmsgs+"");

                        }else{


                            progressBar.setVisibility(View.GONE);
                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                            final String currentDateandTime1 = sdf2.format(new Date());
                            final int intdate = Integer.parseInt(currentDateandTime1);

                            db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
                            db_subs = openOrCreateDatabase("dashboard_subscription", Context.MODE_PRIVATE, null);

                            Cursor cursor = db_inapp.rawQuery("SELECT * FROM credentialstime", null);
                            if (cursor.moveToFirst()) {
                                app_validity = cursor.getString(9);
                            }
                            if((app_validity!=null)&&(!app_validity.equalsIgnoreCase(""))){
                                String ye1 = app_validity.substring(0, 4);
                                String mon1 = app_validity.substring(4, 6);
                                String day1 = app_validity.substring(6, 8);
                                tv_valid_app.setText(day1 + "/" + mon1 + "/" + ye1);
                            }

                            Cursor cursor1 = db_subs.rawQuery("SELECT * FROM subscription", null);
                            if (cursor1.moveToFirst()) {
                                dashboard_validity = cursor1.getString(9);
                            }
                            if(dashboard_validity==null){

                            }else{
                                if((dashboard_validity!=null)&&(!dashboard_validity.equalsIgnoreCase(""))){
                                    String ye = dashboard_validity.substring(0, 4);
                                    String mon = dashboard_validity.substring(4, 6);
                                    String day = dashboard_validity.substring(6, 8);
                                    tv_dashboard_validity.setText(day + "/" + mon + "/" + ye);
                                }
                            }

                            if(dashboard_validity==null){
                                tv_dashboard_validity.setText("Not Subscribed");
                                btn_cloud_buy.setVisibility(View.VISIBLE);
                            }else{
                                if ((dashboard_validity!=null)&&(!dashboard_validity.equalsIgnoreCase(""))) {
                                    if((intdate <= Integer.parseInt(dashboard_validity))){
                                        btn_cloud_buy.setVisibility(View.INVISIBLE);

                                    }else{
                                        btn_cloud_buy.setVisibility(View.VISIBLE);

                                    }

                                } else {
                                    tv_dashboard_validity.setText("Not Subscribed");
                                    btn_cloud_buy.setVisibility(View.VISIBLE);
                                }
                            }


                            Cursor cursor11 = db_inapp.rawQuery("SELECT * FROM Messaginglicense", null);
                            if (cursor11.moveToFirst()) {
                                value = cursor11.getString(2);
                                remaining_msgs=cursor11.getString(1);
                            }
                            int totalmsgs=Integer.parseInt(value)+Integer.parseInt(remaining_msgs);
                            tv_msg_count.setText(value);
                            total_msgs.setText(totalmsgs+"");

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
                params.put("subscription",mSelectedSubscriptionPeriod);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

    }




    public void messagelicense(final String orderid){

        SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        final String company= sharedpreferences.getString("companyname", null);
        final String store= sharedpreferences.getString("storename", null);
        final String device= sharedpreferences.getString("devicename", null);

        progressBar.setVisibility(View.VISIBLE);
        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"messagepurchase.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")){


                            progressBar.setVisibility(View.GONE);
                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                            final String currentDateandTime1 = sdf2.format(new Date());
                            final int intdate = Integer.parseInt(currentDateandTime1);

                            db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
                            db_subs = openOrCreateDatabase("dashboard_subscription", Context.MODE_PRIVATE, null);

                            Cursor cursor = db_inapp.rawQuery("SELECT * FROM credentialstime", null);
                            if (cursor.moveToFirst()) {
                                app_validity = cursor.getString(9);
                            }
                            if((app_validity!=null)&&(!app_validity.equalsIgnoreCase(""))){
                                String ye1 = app_validity.substring(0, 4);
                                String mon1 = app_validity.substring(4, 6);
                                String day1 = app_validity.substring(6, 8);
                                tv_valid_app.setText(day1 + "/" + mon1 + "/" + ye1);
                            }



                            Cursor cursor1 = db_subs.rawQuery("SELECT * FROM subscription", null);
                            if (cursor1.moveToFirst()) {
                                dashboard_validity = cursor1.getString(9);
                            }
                            if(dashboard_validity==null){

                            }else{
                                if((dashboard_validity!=null)&&(!dashboard_validity.equalsIgnoreCase(""))){
                                    String ye = dashboard_validity.substring(0, 4);
                                    String mon = dashboard_validity.substring(4, 6);
                                    String day = dashboard_validity.substring(6, 8);
                                    tv_dashboard_validity.setText(day + "/" + mon + "/" + ye);
                                }
                            }


                            if(dashboard_validity==null){
                                tv_dashboard_validity.setText("Not Subscribed");
                                btn_cloud_buy.setVisibility(View.VISIBLE);
                            }else{
                                if ((dashboard_validity!=null)&&(!dashboard_validity.equalsIgnoreCase(""))) {
                                    if(intdate <= Integer.parseInt(dashboard_validity)){
                                        btn_cloud_buy.setVisibility(View.INVISIBLE);
                                    }else{
                                        btn_cloud_buy.setVisibility(View.VISIBLE);
                                    }

                                } else {
                                    tv_dashboard_validity.setText("Not Subscribed");
                                    btn_cloud_buy.setVisibility(View.VISIBLE);
                                }
                            }


                            Cursor cursor11 = db_inapp.rawQuery("SELECT * FROM Messaginglicense", null);
                            if (cursor11.moveToFirst()) {
                                value = cursor11.getString(2);
                                remaining_msgs=cursor11.getString(1);
                            }
                            int totalmsgs=Integer.parseInt(value)+Integer.parseInt(remaining_msgs);
                            tv_msg_count.setText(value);
                            total_msgs.setText(totalmsgs+"");


                        }else{


                            progressBar.setVisibility(View.GONE);
                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                            final String currentDateandTime1 = sdf2.format(new Date());
                            final int intdate = Integer.parseInt(currentDateandTime1);

                            db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
                            db_subs = openOrCreateDatabase("dashboard_subscription", Context.MODE_PRIVATE, null);

                            Cursor cursor = db_inapp.rawQuery("SELECT * FROM credentialstime", null);
                            if (cursor.moveToFirst()) {
                                app_validity = cursor.getString(9);
                            }
                            if((app_validity!=null)&&(!app_validity.equalsIgnoreCase(""))){
                                String ye1 = app_validity.substring(0, 4);
                                String mon1 = app_validity.substring(4, 6);
                                String day1 = app_validity.substring(6, 8);
                                tv_valid_app.setText(day1 + "/" + mon1 + "/" + ye1);
                            }



                            Cursor cursor1 = db_subs.rawQuery("SELECT * FROM subscription", null);
                            if (cursor1.moveToFirst()) {
                                dashboard_validity = cursor1.getString(9);
                            }
                            if(dashboard_validity==null){

                            }else{
                                if((dashboard_validity!=null)&&(!dashboard_validity.equalsIgnoreCase(""))){
                                    String ye = dashboard_validity.substring(0, 4);
                                    String mon = dashboard_validity.substring(4, 6);
                                    String day = dashboard_validity.substring(6, 8);
                                    tv_dashboard_validity.setText(day + "/" + mon + "/" + ye);
                                }
                            }


                            if(dashboard_validity==null){
                                tv_dashboard_validity.setText("Not Subscribed");
                                btn_cloud_buy.setVisibility(View.VISIBLE);
                            }else{
                                if ((dashboard_validity!=null)&&(!dashboard_validity.equalsIgnoreCase(""))) {
                                    if(intdate <= Integer.parseInt(dashboard_validity)){
                                        btn_cloud_buy.setVisibility(View.INVISIBLE);
                                    }else{
                                        btn_cloud_buy.setVisibility(View.VISIBLE);
                                    }

                                } else {
                                    tv_dashboard_validity.setText("Not Subscribed");
                                    btn_cloud_buy.setVisibility(View.VISIBLE);
                                }
                            }


                            Cursor cursor11 = db_inapp.rawQuery("SELECT * FROM Messaginglicense", null);
                            if (cursor11.moveToFirst()) {
                                value = cursor11.getString(2);
                                remaining_msgs=cursor11.getString(1);
                            }
                            int totalmsgs=Integer.parseInt(value)+Integer.parseInt(remaining_msgs);
                            tv_msg_count.setText(value);
                            total_msgs.setText(totalmsgs+"");


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
                params.put("subscription",mSelectedMesaageSubscription);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

    }




    public void orderlicense(final String orderid){

        SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        final String company= sharedpreferences.getString("companyname", null);
        final String store= sharedpreferences.getString("storename", null);
        final String device= sharedpreferences.getString("devicename", null);

        progressBar.setVisibility(View.VISIBLE);
        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"orderpurchase.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")){


                            progressBar.setVisibility(View.GONE);
                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                            final String currentDateandTime1 = sdf2.format(new Date());
                            final int intdate = Integer.parseInt(currentDateandTime1);

                            db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);


                            Cursor cursor11 = db_inapp.rawQuery("SELECT * FROM Orderlicense", null);
                            if (cursor11.moveToFirst()) {
                                value_order = cursor11.getString(2);
                                remaining_orders=cursor11.getString(1);
                            }
                            int totalorders=Integer.parseInt(value_order)+Integer.parseInt(remaining_orders);
                            tv_order_count.setText(value_order);
                            total_order.setText(totalorders+"");


                        }else{


                            progressBar.setVisibility(View.GONE);
                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                            final String currentDateandTime1 = sdf2.format(new Date());
                            final int intdate = Integer.parseInt(currentDateandTime1);

                            db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);


                            Cursor cursor11 = db_inapp.rawQuery("SELECT * FROM Orderlicense", null);
                            if (cursor11.moveToFirst()) {
                                value_order = cursor11.getString(2);
                                remaining_orders=cursor11.getString(1);
                            }
                            int totalorders=Integer.parseInt(value_order)+Integer.parseInt(remaining_orders);
                            tv_order_count.setText(value_order);
                            total_order.setText(totalorders+"");


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
                params.put("subscription",mSelectedonlineorderSubscription);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

    }




    public void prolicense(final String orderid){

        SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        final String company= sharedpreferences.getString("companyname", null);
        final String store= sharedpreferences.getString("storename", null);
        final String device= sharedpreferences.getString("devicename", null);

        progressBar.setVisibility(View.VISIBLE);
        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"propurchase.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")){

                            progressBar.setVisibility(View.GONE);
                            dialog_pro.dismiss();

                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                            final String currentDateandTime1 = sdf2.format(new Date());
                            final int intdate = Integer.parseInt(currentDateandTime1);

                            db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
                            db_subs = openOrCreateDatabase("dashboard_subscription", Context.MODE_PRIVATE, null);

                            Cursor cursor = db_inapp.rawQuery("SELECT * FROM credentialstime", null);
                            if (cursor.moveToFirst()) {
                                app_validity = cursor.getString(9);
                            }
                            if((app_validity!=null)&&(!app_validity.equalsIgnoreCase(""))){
                                String ye1 = app_validity.substring(0, 4);
                                String mon1 = app_validity.substring(4, 6);
                                String day1 = app_validity.substring(6, 8);
                                tv_valid_app.setText(day1 + "/" + mon1 + "/" + ye1);
                            }

                            Cursor cursor1 = db_subs.rawQuery("SELECT * FROM subscription", null);
                            if (cursor1.moveToFirst()) {
                                dashboard_validity = cursor1.getString(9);
                            }
                            if(dashboard_validity==null){

                            }else{
                                if((dashboard_validity!=null)&&(!dashboard_validity.equalsIgnoreCase(""))){
                                    String ye = dashboard_validity.substring(0, 4);
                                    String mon = dashboard_validity.substring(4, 6);
                                    String day = dashboard_validity.substring(6, 8);
                                    tv_dashboard_validity.setText(day + "/" + mon + "/" + ye);
                                }
                            }

                            if(dashboard_validity==null){
                                tv_dashboard_validity.setText("Not Subscribed");
                                btn_cloud_buy.setVisibility(View.VISIBLE);
                            }else{
                                if ((dashboard_validity!=null)&&(!dashboard_validity.equalsIgnoreCase(""))) {
                                    if((intdate <= Integer.parseInt(dashboard_validity))){
                                        btn_cloud_buy.setVisibility(View.INVISIBLE);

                                    }else{
                                        btn_cloud_buy.setVisibility(View.VISIBLE);

                                    }

                                } else {
                                    tv_dashboard_validity.setText("Not Subscribed");
                                    btn_cloud_buy.setVisibility(View.VISIBLE);
                                }
                            }


                            Cursor cursor11 = db_inapp.rawQuery("SELECT * FROM Messaginglicense", null);
                            if (cursor11.moveToFirst()) {
                                value = cursor11.getString(2);
                                remaining_msgs=cursor11.getString(1);
                            }
                            int totalmsgs=Integer.parseInt(value)+Integer.parseInt(remaining_msgs);
                            tv_msg_count.setText(value);
                            total_msgs.setText(totalmsgs+"");

                            Cursor cursor_pro=db_inapp.rawQuery("SELECT * FROM Pro_upgrade", null);
                            if(cursor_pro.moveToFirst()){

                                String status=cursor_pro.getString(1);
                                String status_da=cursor_pro.getString(3);

                                if(status.equalsIgnoreCase("Activated")){

                                    if (Integer.parseInt(status_da) > Integer.parseInt(currentDateandTime1_pro)) {
                                        pro_buy.setVisibility(View.INVISIBLE);
                                        if ((app_validity != null) && (!app_validity.equalsIgnoreCase(""))) {
                                            String ye1 = app_validity.substring(0, 4);
                                            String mon1 = app_validity.substring(4, 6);
                                            String day1 = app_validity.substring(6, 8);
                                            tv_provalidity.setText("Activated");
                                        }
                                    }else {
                                        pro_buy.setVisibility(View.VISIBLE);
                                    }
                                }else{
                                    pro_buy.setVisibility(View.VISIBLE);
                                }

                            }else{


                                pro_buy.setVisibility(View.VISIBLE);


                            }

                        }else{

                            progressBar.setVisibility(View.GONE);
                            dialog_pro.dismiss();

                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                            final String currentDateandTime1 = sdf2.format(new Date());
                            final int intdate = Integer.parseInt(currentDateandTime1);

                            db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
                            db_subs = openOrCreateDatabase("dashboard_subscription", Context.MODE_PRIVATE, null);

                            Cursor cursor = db_inapp.rawQuery("SELECT * FROM credentialstime", null);
                            if (cursor.moveToFirst()) {
                                app_validity = cursor.getString(9);
                            }
                            if((app_validity!=null)&&(!app_validity.equalsIgnoreCase(""))){
                                String ye1 = app_validity.substring(0, 4);
                                String mon1 = app_validity.substring(4, 6);
                                String day1 = app_validity.substring(6, 8);
                                tv_valid_app.setText(day1 + "/" + mon1 + "/" + ye1);
                            }

                            Cursor cursor1 = db_subs.rawQuery("SELECT * FROM subscription", null);
                            if (cursor1.moveToFirst()) {
                                dashboard_validity = cursor1.getString(9);
                            }
                            if(dashboard_validity==null){

                            }else{
                                if((dashboard_validity!=null)&&(!dashboard_validity.equalsIgnoreCase(""))){
                                    String ye = dashboard_validity.substring(0, 4);
                                    String mon = dashboard_validity.substring(4, 6);
                                    String day = dashboard_validity.substring(6, 8);
                                    tv_dashboard_validity.setText(day + "/" + mon + "/" + ye);
                                }
                            }

                            if(dashboard_validity==null){
                                tv_dashboard_validity.setText("Not Subscribed");
                                btn_cloud_buy.setVisibility(View.VISIBLE);
                            }else{
                                if ((dashboard_validity!=null)&&(!dashboard_validity.equalsIgnoreCase(""))) {
                                    if((intdate <= Integer.parseInt(dashboard_validity))){
                                        btn_cloud_buy.setVisibility(View.INVISIBLE);

                                    }else{
                                        btn_cloud_buy.setVisibility(View.VISIBLE);

                                    }

                                } else {
                                    tv_dashboard_validity.setText("Not Subscribed");
                                    btn_cloud_buy.setVisibility(View.VISIBLE);
                                }
                            }


                            Cursor cursor11 = db_inapp.rawQuery("SELECT * FROM Messaginglicense", null);
                            if (cursor11.moveToFirst()) {
                                value = cursor11.getString(2);
                                remaining_msgs=cursor11.getString(1);
                            }
                            int totalmsgs=Integer.parseInt(value)+Integer.parseInt(remaining_msgs);
                            tv_msg_count.setText(value);
                            total_msgs.setText(totalmsgs+"");

                            Cursor cursor_pro=db_inapp.rawQuery("SELECT * FROM Pro_upgrade", null);
                            if(cursor_pro.moveToFirst()){

                                String status=cursor_pro.getString(1);
                                String status_da=cursor_pro.getString(3);

                                if(status.equalsIgnoreCase("Activated")){

                                    if (Integer.parseInt(status_da) > Integer.parseInt(currentDateandTime1_pro)) {
                                        pro_buy.setVisibility(View.INVISIBLE);
                                        if ((app_validity != null) && (!app_validity.equalsIgnoreCase(""))) {
                                            String ye1 = app_validity.substring(0, 4);
                                            String mon1 = app_validity.substring(4, 6);
                                            String day1 = app_validity.substring(6, 8);
                                            tv_provalidity.setText("Activated");
                                        }
                                    }else {
                                        pro_buy.setVisibility(View.VISIBLE);
                                    }
                                }else{
                                    pro_buy.setVisibility(View.VISIBLE);
                                }

                            }else{


                                pro_buy.setVisibility(View.VISIBLE);


                            }

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



//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (mHelper != null) try {
//            mHelper.dispose();
//        } catch (IabHelper.IabAsyncInProgressException e) {
//            e.printStackTrace();
//        }
//        mHelper = null;
//    }


    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
