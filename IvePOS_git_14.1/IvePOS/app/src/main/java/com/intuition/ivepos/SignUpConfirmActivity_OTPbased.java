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

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

import android.app.ProgressDialog;
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
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;
import com.amplifyframework.core.Amplify;
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

public class SignUpConfirmActivity_OTPbased extends AppCompatActivity {

    private TextView username;
    private EditText confCode;
    private TextView confirm;
    private TextView reqCode;
    private String userName;
    private String name,nam;
    private AlertDialog userDialog;
    RelativeLayout progressBar1;
    String company, store, device, phone_number, company_special;
    String selection;
    public static final String PACKAGE_NAME = "com.intuition.ivepos";
    RequestQueue requestQueue;

    String WebserviceUrl;

    SQLiteDatabase db = null;
    SQLiteDatabase db_appda = null;
    String  textcompanyname, storeitem, deviceitem, from;

    String checking;
    boolean mSubscribedToDelaroy;

    TextView et_phone_number;
    private String finalData = "";

    ProgressDialog dialog;

    String name_exte, country_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_otp);

//        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(SignUpConfirmActivity_OTPbased.this);
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
        phone_number = extras.getString("phone_number");
        store = extras.getString("storename");
        device = extras.getString("devicename");
        selection = extras.getString("account_selection");
        name_exte = extras.getString("country_code");
        country_name = extras.getString("country");

        dialog = new ProgressDialog(SignUpConfirmActivity_OTPbased.this, R.style.timepicker_date_dialog);

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

        SharedPreferenceManager.initializeInstance(getApplicationContext());

        if(SharedPreferenceManager.getInstance().getValue() !=0){

//            Log.i("SharedPrefSessionValue", String.valueOf(SharedPreferenceManager.getInstance().getValue()));

            Amplify.Auth.fetchAuthSession(
                    result -> Log.i("AmplifyAuthSession", ""),
                    error -> Log.e("AmplifyQuickstart", "")
            );
        }

        init();
        requestQueue = Volley.newRequestQueue(this);

        ImageView leftarrow = (ImageView) findViewById(R.id.leftarrow);
        leftarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // changed to
                Intent intent1 = new Intent(SignUpConfirmActivity_OTPbased.this, MainActivity_Signin_OTPbased.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
            }
        });
    }

    private void init() {

        et_phone_number = (TextView) findViewById(R.id.phone_number);
        et_phone_number.setText(name_exte+""+phone_number);


//        int maxLength =6;
//        confCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        confCode = (EditText) findViewById(R.id.editTextConfirmCode);
//        confCode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                if (s.length() == 0) {
//                    TextView label = (TextView) findViewById(R.id.textViewConfirmCodeLabel);
//                    label.setText(confCode.getHint());
////                    confCode.setBackground(getDrawable(R.drawable.text_border_selector));
//                }
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                TextView label = (TextView) findViewById(R.id.textViewConfirmCodeMessage);
//                label.setText(" ");
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() == 0) {
//                    TextView label = (TextView) findViewById(R.id.textViewConfirmCodeLabel);
//                    label.setText("");
//                }
//            }
//        });

        finalData = name_exte + phone_number;

        confirm = (TextView) findViewById(R.id.confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sendConfCode(v);

                EditText signUpConfirmationCode = findViewById(R.id.editTextConfirmCode);
                String confirmOTPCode = signUpConfirmationCode.getText().toString().trim();

                if (isDeviceOnline() == true) {
//                    new LoginTask().execute();
                    if (confCode.getText().toString().isEmpty()) {
                        Toast.makeText(SignUpConfirmActivity_OTPbased.this, "Please Enter OTP", Toast.LENGTH_LONG).show();
                    } else if (confCode.getText().toString().length() == 0) {
                        Toast.makeText(SignUpConfirmActivity_OTPbased.this, "Enter Correct OTP", Toast.LENGTH_LONG).show();

                    } else {

                        dialog.setMessage("Loading");
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        dialog.show();

                        Amplify.Auth.confirmSignUp(
                                finalData,
                                confirmOTPCode,
                                result -> {
//                                    Log.i("AuthQuickstartConfSign1", result.getNextStep().getSignUpStep().toString());
//                                    Log.i("AuthQuickstartConfSign1", result.getNextStep().getSignUpStep().name());
                                    if (result.getNextStep().getSignUpStep().toString().contains("Confirmation code entered is not correct")) {
                                        dialog.dismiss();
                                        Log.i("resultelse", "result.toString()");

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog alertDialog = new AlertDialog.Builder(SignUpConfirmActivity_OTPbased.this).create();

                                                alertDialog.setTitle(getString(R.string.title10));
                                                alertDialog.setMessage("Enter correct OTP");
                                                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

                                                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                                dialog.dismiss();

                                                alertDialog.show();
                                            }
                                        });
                                    }else {
                                        if (result.isSignUpComplete()) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    dialog.dismiss();
                                                    Toast.makeText(SignUpConfirmActivity_OTPbased.this, "OTP Verified and SignUp Completed", Toast.LENGTH_LONG).show();
                                                    System.out.println("pospurchase 04");
                                                    register();
                                                }
                                            });
                                        } else {
                                            dialog.dismiss();
                                            Log.i("resultelse", "result.toString()");

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AlertDialog alertDialog = new AlertDialog.Builder(SignUpConfirmActivity_OTPbased.this).create();

                                                    alertDialog.setTitle(getString(R.string.title10));
                                                    alertDialog.setMessage("Try Again");
                                                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

                                                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });

                                                    alertDialog.show();
                                                }
                                            });
                                        }
                                    }
                                },
                                error -> {Log.e("AuthQuickstart", error.toString());
                                    if (error.toString().contains("Confirmation code entered is not correct")) {
                                        SignUpConfirmActivity_OTPbased.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                AlertDialog alertDialog = new AlertDialog.Builder(SignUpConfirmActivity_OTPbased.this).create();

                                                alertDialog.setTitle(getString(R.string.title10));
                                                alertDialog.setMessage("Enter correct OTP");
                                                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

                                                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                                dialog.dismiss();

                                                alertDialog.show();
                                            }
                                        });
                                    }
                                }
                        );

//                        Amplify.Auth.confirmSignUp(
//                                finalData,
//                                confirmOTPCode,
//                                result -> {
//                                    Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete");
//                                    if (result.isSignUpComplete()) {
//
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                dialog.dismiss();
//                                                Toast.makeText(SignUpConfirmActivity_OTPbased.this, "OTP verified successfully", Toast.LENGTH_LONG).show();
//                                                register();
//                                            }
//                                        });
//                                    }
//                                },
//                                error -> {
//                                    Log.e("AuthQuickstart", error.toString());
//                                }
//                        );
                    }
                } else {
                    showAlert();
                }
            }
        });


        reqCode = findViewById(R.id.resend_confirm_req);
//        reqCode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                reqConfCode();
//            }
//        });
       reqCode.setOnClickListener(view -> {
           reqConfCode();
           Toast.makeText(SignUpConfirmActivity_OTPbased.this,"Clicked",Toast.LENGTH_LONG).show();
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
            int maxLength =6;
            confCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
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
        progressBar1 = (RelativeLayout) findViewById(R.id.progressbar1);
        progressBar1.setVisibility(View.VISIBLE);

        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"newcomp_phon_spec_ema_otp.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("newcompany_phone_speci_email "+response);
                        if(response.equalsIgnoreCase("success")){
                            Log.d("response",response);

                            System.out.println("pospurchase 03");

                            Intent intent = new Intent(SignUpConfirmActivity_OTPbased.this, DownloadService.class);
                            // add infos for the service which file to download and where to store
                            intent.putExtra("company", company);
                            intent.putExtra("store", store);
                            intent.putExtra("device", device);
                            intent.putExtra("emailid", name_exte + ""+phone_number);
                            startService(intent);

//                            Log.d("Company",company);
//                            Log.d("store",store);
//                            Log.d("device",device);

                               /*          SyncDatabase syncDatabase=new SyncDatabase();
                            syncDatabase.createSyncDb(SignUpConfirmActivity_OTPbased.this);*/
                            //    progressBar1.setVisibility(View.GONE);


                            SharedPreferences pref = getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("emailid", name_exte + ""+phone_number); // Storing boolean - true/false
                            editor.putString("storename", store); // Storing string
                            editor.putString("devicename", device); // Storing integer
                            editor.putString("companyname", company); // Storing float// Storing long
                            editor.putString("companyname_special", company_special); // Storing float// Storing long
                            editor.putString("password", "ivepos@123");
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
//                                email =  cursor_cred.getString(5);

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


                            contentValues.put("email_id", phone_number);
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
                                        System.out.println("pospurchase 00");
                                        update();
                                    } while (cursor7.moveToNext());
                                }else{
                                    db.insert("credentialstime", null, contentValues);
                                    System.out.println("pospurchase 01");
                                    update();

                                }
                            }else{
                                db.insert("credentialstime", null, contentValues);
                                System.out.println("pospurchase 02");
                                update();

                            }

//                            Intent i = new Intent(SignUpConfirmActivity_OTPbased.this, MainActivity_subscription.class);
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
                params.put("email", name_exte + ""+phone_number + "");
                params.put("phone_number", phone_number + "");
                params.put("password", "ivepos@123" + "");
                params.put("company", company + "");
                params.put("company_special", company_special + "");
                params.put("store", store + "");
                params.put("device", device + "");
                params.put("country_code", name_exte + "");
                params.put("username", phone_number);
                params.put("country", country_name);
//                System.out.println("newcompany_phone_speci_email1 "+email+" "+phone_number+" "+password+" "+company+" "+company_special+" "+store+" "+device);
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
                            syncDatabase.createSyncDb(SignUpConfirmActivity_OTPbased.this);*/
                            progressBar1.setVisibility(View.GONE);


                            SharedPreferences pref= getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("emailid", name_exte + ""+phone_number); // Storing boolean - true/false
                            editor.putString("storename", store); // Storing string
                            editor.putString("devicename", device); // Storing integer
                            editor.putString("companyname", company); // Storing float// Storing long
                            editor.putString("company_special", company_special); // Storing float// Storing long
                            editor.putString("account_selection", selection); // Storing float// Storing long
                            editor.commit(); // commit changes

                            Intent i = new Intent(SignUpConfirmActivity_OTPbased.this, MainActivity_subscription.class);
                            i.putExtra("emailid", name_exte + ""+phone_number);
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
        AlertDialog alertDialog = new AlertDialog.Builder(SignUpConfirmActivity_OTPbased.this).create();

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
        System.out.println("pospurchase 0");
//        if (i == 5){
//            prolicense("");
//        }else {
        poslicense(checking);
//        }
    }

    public void poslicense(final String orderid){
        System.out.println("pospurchase 1");
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
                        System.out.println("pospurchase "+response);
                        if(response.equalsIgnoreCase("success")){



                            TextView from1 = new TextView(SignUpConfirmActivity_OTPbased.this);
                            from1.setText(from);

                            Cursor cursor_cred = db.rawQuery("SELECT * FROM credentialstime", null);
                            if (cursor_cred.moveToFirst()) {
                                do{
                                    textcompanyname = cursor_cred.getString(6);
                                    storeitem = cursor_cred.getString(7);
                                    deviceitem = cursor_cred.getString(8);
//                                    email =  cursor_cred.getString(5);
                                }while (cursor_cred.moveToNext());
                            }
                            updateUi();
                            //    progressbar1.setVisibility(View.GONE);

                        }else{
                            //    progressbar1.setVisibility(View.GONE);
                            TextView from1 = new TextView(SignUpConfirmActivity_OTPbased.this);
                            from1.setText(from);

                            Cursor cursor_cred = db.rawQuery("SELECT * FROM credentialstime", null);
                            if (cursor_cred.moveToFirst()) {
                                do{
                                    textcompanyname = cursor_cred.getString(6);
                                    storeitem = cursor_cred.getString(7);
                                    deviceitem = cursor_cred.getString(8);
//                                    email =  cursor_cred.getString(5);
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
