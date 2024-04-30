/*
 *  Copyright 2013-2016 Amazon.com,
 *  Inc. or its affiliates. All Rights Reserved.
 *
 *  Licensed under the Amazon Software License (the "License").
 *  You may not use this file except in compliance with the
 *  License. A copy of the License is located at
 *
 *      http://aws.amazon.com/asl/
 *
 *  or in the "license" file accompanying this file. This file is
 *  distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *  CONDITIONS OF ANY KIND, express or implied. See the License
 *  for the specific language governing permissions and
 *  limitations under the License.
 */

package com.intuition.ivepos;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.intuition.ivepos.signup.DownloadService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

public class SignUpConfirmActivity extends AppCompatActivity {

    private TextView username;
    private EditText confCode;
    private Button confirm;
    private TextView reqCode;
    private String userName;
    private String emailid, name,nam;
    private AlertDialog userDialog;
    RelativeLayout progressBar1;
    String company, store, device, phone_number, email,password, company_special;
    String selection;
    public static final String PACKAGE_NAME = "com.intuition.ivepos";
    RequestQueue requestQueue;

    String WebserviceUrl;

    SQLiteDatabase db = null;
    SQLiteDatabase db_appda = null;
    String  textcompanyname, storeitem, deviceitem, from;

    String checking;
    boolean mSubscribedToDelaroy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_confirm);

//        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(SignUpConfirmActivity.this);
//        String account_selection= sharedpreferences_select.getString("account_selection", null);
//
//        if (account_selection.toString().equals("Dine")) {
//            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
//        }else {
//            if (account_selection.toString().equals("Qsr")) {
//                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
//            }else {
//                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
//            }
//        }

        Bundle extras = getIntent().getExtras();
        company = extras.getString("companyname");
        company_special = extras.getString("companyname_special");
        email = extras.getString("emailid");
        phone_number = extras.getString("phone_number");
        store = extras.getString("storename");
        device = extras.getString("devicename");
        password = extras.getString("password");
        selection = extras.getString("account_selection");
        emailid = email;


        System.out.println("signupconfirm "+selection+" "+company_special);


        if (selection.toString().equals("Dine") || selection.toString().equals(getString(R.string.app_name))) {
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }else {
            if (selection.toString().equals("Qsr")) {
                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
            }else {
                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
            }
        }

        init();
        requestQueue = Volley.newRequestQueue(this);
    }

    private void init() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("emailid")) {
                userName = extras.getString("emailid");
                username = (TextView) findViewById(R.id.editTextConfirmUserId);
                username.setText(userName);
                confCode = (EditText) findViewById(R.id.editTextConfirmCode);
                confCode.requestFocus();

                if (extras.containsKey("destination")) {
                    String dest = extras.getString("destination");
                    String delMed = extras.getString("deliveryMed");

                    TextView screenSubtext = (TextView) findViewById(R.id.textViewConfirmSubtext_1);
                    if (dest != null && delMed != null && dest.length() > 0 && delMed.length() > 0) {
                        screenSubtext.setText("A confirmation code was sent to " + dest + " via " + delMed);
                    } else {
                        screenSubtext.setText("A confirmation code was sent");
                    }
                }
            } else {
                TextView screenSubtext = (TextView) findViewById(R.id.textViewConfirmSubtext_1);
                screenSubtext.setText("Request for a confirmation code or confirm with the code you already have.");
            }

        }

        username = (TextView) findViewById(R.id.editTextConfirmUserId);
        nam = username.getText().toString();
        name = username.getText().toString();
        username.setText(getIntent().getStringExtra("emailid"));
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewConfirmUserIdLabel);
                    label.setText(username.getHint());
//                    username.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewConfirmUserIdMessage);
                label.setText(" ");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewConfirmUserIdLabel);
                    label.setText("");
                }
            }
        });

        confCode = (EditText) findViewById(R.id.editTextConfirmCode);
        confCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewConfirmCodeLabel);
                    label.setText(confCode.getHint());
//                    confCode.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewConfirmCodeMessage);
                label.setText(" ");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewConfirmCodeLabel);
                    label.setText("");
                }
            }
        });

        confirm = (Button) findViewById(R.id.confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sendConfCode(v);
                if (isDeviceOnline() == true) {
                    new LoginTask().execute();
                } else {
                    showAlert();
                }
            }
        });


        reqCode = (TextView) findViewById(R.id.resend_confirm_req);
        reqCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqConfCode();
            }
        });
    }

    private class LoginTask extends AsyncTask<String, String, String> {
        String confirmCode;
        @Override
        protected void onPreExecute() {
            progressBar1 = (RelativeLayout) findViewById(R.id.progressbar1);
            progressBar1.setVisibility(View.VISIBLE);
            confirmCode = confCode.getText().toString();
        }

        protected String doInBackground(String... urls) {

            if (selection.toString().equals("Retail")) {
                AppHelper_Retail.getPool().getUser(userName).confirmSignUpInBackground(confirmCode, true, confHandler);
            }else {
                AppHelper.getPool().getUser(userName).confirmSignUpInBackground(confirmCode, true, confHandler);
            }
            return null;

        }

        protected void onProgressUpdate(String... progress) {

        }

        protected void onPostExecute(String result) {

        }
    }

    private void reqConfCode() {
        userName = username.getText().toString();
        if (userName == null || userName.length() < 1) {
            TextView label = (TextView) findViewById(R.id.textViewConfirmUserIdMessage);
            label.setText(username.getHint() + " cannot be empty");
//            username.setBackground(getDrawable(R.drawable.text_border_error));
            return;
        }
        if (selection.toString().equals("Retail")) {
            AppHelper_Retail.getPool().getUser(userName).resendConfirmationCodeInBackground(resendConfCodeHandler);
        }else {
            AppHelper.getPool().getUser(userName).resendConfirmationCodeInBackground(resendConfCodeHandler);
        }

    }

    GenericHandler confHandler = new GenericHandler() {
        @Override
        public void onSuccess() {
            progressBar1.setVisibility(View.GONE);
            showDialogMessage("Success!", userName + " has been confirmed!", true);

        }

        @Override
        public void onFailure(Exception exception) {
            progressBar1.setVisibility(View.GONE);
            TextView label = (TextView) findViewById(R.id.textViewConfirmUserIdMessage);
            label.setText(getString(R.string.confi4));
//            username.setBackground(getDrawable(R.drawable.text_border_error));

            label = (TextView) findViewById(R.id.textViewConfirmCodeMessage);
            label.setText(getString(R.string.confi4));
//            confCode.setBackground(getDrawable(R.drawable.text_border_error));

            if (selection.toString().equals("Retail")) {
                showDialogMessage(getString(R.string.sdm14), AppHelper_Retail.formatException(exception), false);
            }else {
                showDialogMessage(getString(R.string.sdm14), AppHelper.formatException(exception), false);
            }
        }
    };

    VerificationHandler resendConfCodeHandler = new VerificationHandler() {
        @Override
        public void onSuccess(CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            TextView mainTitle = (TextView) findViewById(R.id.textViewConfirmTitle);
            mainTitle.setText("Confirm your account");
            confCode = (EditText) findViewById(R.id.editTextConfirmCode);
            confCode.requestFocus();
            showDialogMessage(getString(R.string.sdm15), getString(R.string.sdm16)+" " + cognitoUserCodeDeliveryDetails.getDestination() + " via " + cognitoUserCodeDeliveryDetails.getDeliveryMedium() + ".", false);
        }

        @Override
        public void onFailure(Exception exception) {
            TextView label = (TextView) findViewById(R.id.textViewConfirmUserIdMessage);
            label.setText("Confirmation code resend failed");
            if (selection.toString().equals("Retail")) {
                showDialogMessage(getString(R.string.sdm17), AppHelper_Retail.formatException(exception), false);
            }else {
                showDialogMessage(getString(R.string.sdm17), AppHelper.formatException(exception), false);
            }
        }
    };

    private void showDialogMessage(String title, String body, final boolean exitActivity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                    if (exitActivity) {

                        register();

                    }
                } catch (Exception e) {
                    //exit();
                }
            }
        });
        userDialog = builder.create();
        userDialog.setCanceledOnTouchOutside(false);
        userDialog.show();
    }

    private void register() {

        progressBar1.setVisibility(View.VISIBLE);

        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"newcomp_phon_spec_ema.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("newcompany_phone_speci_email "+response);
                        if(response.equalsIgnoreCase("success")){
                            Log.d("response",response);



                            Intent intent = new Intent(SignUpConfirmActivity.this, DownloadService.class);
                            // add infos for the service which file to download and where to store
                            intent.putExtra("company", company);
                            intent.putExtra("store", store);
                            intent.putExtra("device", device);
                            intent.putExtra("emailid", emailid);
                            startService(intent);

                            Log.d("Company",company);
                            Log.d("store",store);
                            Log.d("device",device);

                               /*          SyncDatabase syncDatabase=new SyncDatabase();
                            syncDatabase.createSyncDb(SignUpConfirmActivity.this);*/
                            //    progressBar1.setVisibility(View.GONE);


                            SharedPreferences pref = getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("emailid", emailid); // Storing boolean - true/false
                            editor.putString("storename", store); // Storing string
                            editor.putString("devicename", device); // Storing integer
                            editor.putString("companyname", company); // Storing float// Storing long
                            editor.putString("companyname_special", company_special); // Storing float// Storing long
                            editor.putString("password", password);
                            editor.putString("account_selection", selection); // Storing float// Storing long
                            // Storing float// Storing long
                            editor.commit(); // commit changes

                            db = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
                            db_appda = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

                            db.execSQL("CREATE TABLE if not exists credentialstime (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, orderid text,time text,date text,trial_time text, " +
                                    "email_id text, company_name text, store_name text, dev_name text, todate text);");
                            db.execSQL("CREATE TABLE if not exists Pro_upgrade (_id integer PRIMARY KEY UNIQUE, status text, orderid text);");
                            db = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);

                            checking="7Days";
                            mSubscribedToDelaroy=true;
//        mSelectedSubscriptionPeriod = SKU_DELAROY_MONTHLY;
                            Cursor cursor_cred = db.rawQuery("SELECT * FROM credentialstime", null);
                            if (cursor_cred.moveToFirst()) {

                                textcompanyname = cursor_cred.getString(6);
                                storeitem = cursor_cred.getString(7);
                                deviceitem = cursor_cred.getString(8);
                                email =  cursor_cred.getString(5);

                            }
                            SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyyyy");
                            final String normal1 = normal2.format(new Date());

                            Date dt = new Date();
                            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
                            final String time1 = sdf1.format(dt);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("orderid", checking);
                            contentValues.put("time", time1);
                            contentValues.put("date", normal1);

                            contentValues.put("trial_time", "7days");

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                            Calendar currentCal = Calendar.getInstance();
                            String currentdate = dateFormat.format(currentCal.getTime());
                            currentCal.add(Calendar.DATE, 7);
                            String toDate = dateFormat.format(currentCal.getTime());

                            contentValues.put("todate", toDate);


                            contentValues.put("email_id", email);
                            contentValues.put("company_name", textcompanyname);
                            contentValues.put("store_name", storeitem);
                            contentValues.put("dev_name", deviceitem);
//                            if(storeitem.contains("-"+email)){
//                                storeitem=storeitem.replace("-"+email,"");
//                            }


                            Cursor cursor_pro=db.rawQuery("SELECT * FROM Pro_upgrade", null);
                            if(cursor_pro.moveToFirst()){

                                int id=cursor_pro.getInt(0);
                                String status=cursor_pro.getString(1);
                                if(!status.equalsIgnoreCase("Activated")){
                                    ContentValues contentValues1 =new ContentValues();
                                    contentValues1.put("status","Activated");
                                    contentValues1.put("orderid","");
                                    String where = "_id = '" + id + "'";
                                    db.update("Pro_upgrade", contentValues1, where, new String[]{});
                                }

                            }else{

                                ContentValues contentValues1 =new ContentValues();
                                contentValues1.put("status","Activated");
                                contentValues1.put("orderid","");
                                db.insert("Pro_upgrade", null, contentValues1);
                            }
                            cursor_pro.close();

                            Cursor cursor7=null;
                            cursor7 = db.rawQuery("SELECT * FROM credentialstime where store_name ='"+storeitem+"'"+" and dev_name ='"+deviceitem+"'", null);
                            if(cursor7!=null) {
                                if (cursor7.moveToFirst()) {
                                    do {
                                        String id = cursor7.getString(0);
                                        String where = "_id = " + id;
                                        db.update("credentialstime", contentValues, where, new String[]{});
                                        update();
                                    } while (cursor7.moveToNext());
                                }else{
                                    db.insert("credentialstime", null, contentValues);

                                    update();

                                }
                            }else{
                                db.insert("credentialstime", null, contentValues);

                                update();

                            }

//                            Intent i = new Intent(SignUpConfirmActivity.this, MainActivity_subscription.class);
//                            i.putExtra("emailid", emailid);
//                            i.putExtra("storename", store);
//                            i.putExtra("devicename", device);
//                            i.putExtra("companyname", company);
//                            i.putExtra("from", "register");
//                            startActivity(i);






                            //   createappdata();
                        }else{
                            showDialogMessage("signup failed","signup failed",false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                })  {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", email + "");
                params.put("phone_number", phone_number + "");
                params.put("password", password + "");
                params.put("company", company + "");
                params.put("company_special", company_special + "");
                params.put("store", store + "");
                params.put("device", device + "");
                System.out.println("newcompany_phone_speci_email1 "+email+" "+phone_number+" "+password+" "+company+" "+company_special+" "+store+" "+device);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);



    }

    private void createappdata() {

        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"createappdata.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")){
                            Log.d("response",response);
                            createsalesdata();
                        }else{
                            showDialogMessage("signup failed","signup failed",false);
                        }
                    }
                },
                new Response.ErrorListener() {
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
                params.put("company", company + "");
                params.put("store", store + "");
                params.put("device", device + "");
                return params;
            }
        };
    /*    sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        sr.setRetryPolicy(new DefaultRetryPolicy(0,-1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(sr);

    }

    private void createsalesdata() {
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"createsalesdata.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")){
                            Log.d("response",response);
                  /*          SyncDatabase syncDatabase=new SyncDatabase();
                            syncDatabase.createSyncDb(SignUpConfirmActivity.this);*/
                            progressBar1.setVisibility(View.GONE);


                            SharedPreferences pref= getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("emailid", emailid); // Storing boolean - true/false
                            editor.putString("storename", store); // Storing string
                            editor.putString("devicename", device); // Storing integer
                            editor.putString("companyname", company); // Storing float// Storing long
                            editor.putString("company_special", company_special); // Storing float// Storing long
                            editor.putString("account_selection", selection); // Storing float// Storing long
                            editor.commit(); // commit changes

                            Intent i = new Intent(SignUpConfirmActivity.this, MainActivity_subscription.class);
                            i.putExtra("emailid", emailid);
                            i.putExtra("storename", store);
                            i.putExtra("devicename", device);
                            i.putExtra("companyname", company);
                            i.putExtra("from", "register");
                            startActivity(i);
                        }else{
                            showDialogMessage("signup failed","signup failed",false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                })  {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                                                    /*    params.put("email", email + "");
                                                        params.put("password", password + "");*/
                params.put("company", company + "");
                params.put("store", store + "");
                params.put("device", device + "");
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(0,-1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);
    }

    /*private void exit() {
        Intent intent = new Intent();
        if (userName == null)
            userName = "";
        intent.putExtra("name", checking);
        intent.putExtra("mytext", name);
        intent.putExtra("email", emailid);
        intent.putExtra("text", userName);
        setResult(RESULT_OK, intent);
        finish();
    }*/

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(SignUpConfirmActivity.this).create();

        alertDialog.setTitle(getString(R.string.title10));
        alertDialog.setMessage(getString(R.string.setmessage19));
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
          /*  alertDialog.setButton("OK", DialogInterface.BUTTON_POSITIVE,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();

                }
            });*/
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // finish();
            }
        });

        alertDialog.show();
    }


    private void update() {

//        if (i == 5){
//            prolicense("");
//        }else {
            poslicense(checking);
//        }
    }

    public void poslicense(final String orderid){

//        mSubscribedToDelaroy=true;
        String mSelectedSubscriptionPeriod = "pro_upgrade_demo";

        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        final String company= sharedpreferences.getString("companyname", null);
        final String store= sharedpreferences.getString("storename", null);
        final String device= sharedpreferences.getString("devicename", null);

//        progressbar1.setVisibility(View.VISIBLE);
        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"pospurchase.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")){



                            TextView from1 = new TextView(SignUpConfirmActivity.this);
                            from1.setText(from);

                            Cursor cursor_cred = db.rawQuery("SELECT * FROM credentialstime", null);
                            if (cursor_cred.moveToFirst()) {
                                do{
                                    textcompanyname = cursor_cred.getString(6);
                                    storeitem = cursor_cred.getString(7);
                                    deviceitem = cursor_cred.getString(8);
                                    email =  cursor_cred.getString(5);
                                }while (cursor_cred.moveToNext());
                            }
                            updateUi();
                            //    progressbar1.setVisibility(View.GONE);

                        }else{
                            //    progressbar1.setVisibility(View.GONE);
                            TextView from1 = new TextView(SignUpConfirmActivity.this);
                            from1.setText(from);

                            Cursor cursor_cred = db.rawQuery("SELECT * FROM credentialstime", null);
                            if (cursor_cred.moveToFirst()) {
                                do{
                                    textcompanyname = cursor_cred.getString(6);
                                    storeitem = cursor_cred.getString(7);
                                    deviceitem = cursor_cred.getString(8);
                                    email =  cursor_cred.getString(5);
                                }while (cursor_cred.moveToNext());
                            }
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
                params.put("orderid","");
                params.put("subscription",mSelectedSubscriptionPeriod);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

    }

    public void updateUi() {
//        if (mSubscribedToDelaroy) {

        SharedPreferences sharedPreferences = getSharedPreferences("signupconfirm", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("from", "signupconfirmpage");
        myEdit.apply();

        if (selection.toString().equals("Dine") || selection.toString().equals(getString(R.string.app_name))) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            if (selection.toString().equals("Qsr")) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(this, MainActivity_Retail.class);
                startActivity(intent);
                finish();
            }
        }


//        } else {
//
//        }
    }

}
