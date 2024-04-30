package com.intuition.ivepos;
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

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity_Signin_OTPbased extends AppCompatActivity {

    //risi code new login
        Button nextph, nextemil , phonenologin;
    //risi code new login

    private final String TAG = "MainActivity";
    public EditText inUsername;
    RelativeLayout progressBar;
    SQLiteDatabase db = null;
    SQLiteDatabase db1 = null;
    private static final String LOG_TAG = "CheckNetworkStatus";
    private boolean isConnected = false;
    ConnectionQuality mConnectionClass = ConnectionQuality.UNKNOWN;
    ConnectionClassManager mConnectionClassManager;
    DeviceBandwidthSampler mDeviceBandwidthSampler;
    int mTries=0;
    public static  ArrayList<StoreBean> storeList=new ArrayList<StoreBean>();
    String username="",password="";
    private ForgotPasswordContinuation forgotPasswordContinuation;
    RequestQueue requestQueue;
    String newPass="",code="";

    String WebserviceUrl;


    RadioGroup radioGroupSplit;
    RadioButton radioBtnsplit, radio_dine, radio_qsr, radio_retail;
    LinearLayout layout_dine, layout_qsr, layout_retail;
    RadioGroup radioGroup1;
    String selection;

    Switch switch_button;
    String email_signin, emailid1;
    String name_exte, country_name;
    EditText et_phone_number;

    private static final String FINAL_DATA = "FinalData";
    private static final String CUSTOMER_STATUS = "CustomerStatus";


    String customerStatus = "";
    private String customerEmailId;

    private static String BASE_URL = "";
    //    private static final String BASE_URL_FOR_RETAIL = "";
    private static final String URL_END_POINT_GET_OLD_USER_STATUS = "getOldUserData.php";
    private static final String BASE_URL_LINK = "BaseUrlLink";

    private static final String URL_END_POINT_UPDATE_EXISTING_OLD_USER_STATUS = "updateExistingOldUserStatus.php";

    String ipAddress_stored;
    Integer ipCount;
    SharedPreferences prefs;
    Button button ,emailsingin ,nexteml;
    TextView textView2,ckbss;
    SharedPreferences.Editor editor1;


    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;
    }
    boolean doubleBackToExitPressedOnce = false;


    private RelativeLayout hiddenbtype;
    CheckBox prvypol;

    private LinearLayout hiddenemail,hiddenphbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_new_sign_in);
    //    progressBar = (RelativeLayout) findViewById(R.id.progressbar1);

        ckbss = findViewById(R.id.linkone);
        ckbss.setMovementMethod(LinkMovementMethod.getInstance());


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor1 = prefs.edit();


        ipAddress_stored = prefs.getString("ip_address", "");
        if (ipAddress_stored != "") {
            ipCount  = prefs.getInt("ip_count",-1);
       //     textView.setText(ipCount.toString());
        }
//        SharedPreferences sharedpreferences =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(MainActivity_Signin_OTPbased.this);
//        String account_selection= sharedpreferences.getString("account_selection", null);
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

        layout_dine = (LinearLayout) findViewById(R.id.layout_dine);
        layout_qsr = (LinearLayout) findViewById(R.id.layout_qsr);
        layout_retail = (LinearLayout) findViewById(R.id.layout_retail);

        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);

        radio_dine = (RadioButton) findViewById(R.id.radio_dine);
        radio_qsr = (RadioButton) findViewById(R.id.radio_qsr);
        radio_retail = (RadioButton) findViewById(R.id.radio_retail);

        //risi code new login

//        TextView signuptxt = findViewById(R.id.signup_textview);
//        String text = "Don't have account ? Join us";
//
//        SpannableString ss = new SpannableString(text);
//        ClickableSpan clickableSpan1 = new ClickableSpan() {
//            @Override
//            public void onClick(@NonNull View view) {
//                Intent itt = new Intent(MainActivity_Signin_OTPbased.this, MainActivity_Register_OTPbased.class);
//                startActivity(itt);
//            }
//
//            @Override
//            public void updateDrawState(@NonNull TextPaint ds) {
//                super.updateDrawState(ds);
//                ds.setColor(Color.BLUE);
//                ds.setUnderlineText(false);
//            }
//        };
//        ss.setSpan(clickableSpan1,21,28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        signuptxt.setText(ss);
//        signuptxt.setMovementMethod(LinkMovementMethod.getInstance());

     //   hiddenbtype = findViewById(R.id.hiddenbusiness);
        hiddenphbutton = findViewById(R.id.hiddenloginbtton);
        hiddenemail = findViewById(R.id.InvisBtn);
        phonenologin = findViewById(R.id.signup4);
            emailsingin = findViewById(R.id.signup3);
            prvypol = findViewById(R.id.checkBoxPolicy);
            nextph = findViewById(R.id.btypeph);
            nextemil = findViewById(R.id.nextemailbtn);

        inUsername = findViewById(R.id.editTextUserId);
        et_phone_number = findViewById(R.id.et_phone_number);

            emailsingin.setOnClickListener(view -> {

                int isVisible2 = hiddenemail.getVisibility();
                int isVisible3 = hiddenphbutton.getVisibility();
//
                if (isVisible2==View.VISIBLE && isVisible3 == View.VISIBLE){
                    hiddenemail.setVisibility(View.GONE);
                    hiddenphbutton.setVisibility(View.GONE);
                }
                else {
                    hiddenemail.setVisibility(View.VISIBLE);
                    hiddenphbutton.setVisibility(View.VISIBLE);
                }

            });
            phonenologin.setOnClickListener(View ->{
                int isVisible2 = hiddenemail.getVisibility();
                int isVisible3 = hiddenphbutton.getVisibility();
                if (isVisible2== android.view.View.VISIBLE && isVisible3 == android.view.View.VISIBLE){
                hiddenemail.setVisibility(android.view.View.GONE);
                    hiddenphbutton.setVisibility(android.view.View.GONE);
            }
            else {
                hiddenemail.setVisibility(android.view.View.VISIBLE);
                    hiddenphbutton.setVisibility(android.view.View.VISIBLE);
            }

            });


            nextph.setOnClickListener(view -> {

                phlogIn();


            });

            nextemil.setOnClickListener(view -> {

                logIn();

            });
        //risi code new login






//        final int selected1 = radioGroup1.getCheckedRadioButtonId();
//        if (selected1 == radio_dine.getId()) {
//            radio_dine.setChecked(true);
//            radio_qsr.setChecked(false);
//            radio_retail.setChecked(false);
//            selection = "Dine";
//            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
//        }
//        if (selected1 == radio_qsr.getId()) {
//            radio_dine.setChecked(false);
//            radio_qsr.setChecked(true);
//            radio_retail.setChecked(false);
//            selection = "Qsr";
//            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
//        }
//        if (selected1 == radio_retail.getId()) {
//            radio_dine.setChecked(false);
//            radio_qsr.setChecked(false);
//            radio_retail.setChecked(true);
//            selection = "Retail";
//            WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
//        }
//
//        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                final int selected1 = radioGroup1.getCheckedRadioButtonId();
//                radioBtnsplit = (RadioButton) findViewById(selected1);
//
//                if (selected1 == radio_dine.getId()) {
//                    radio_dine.setChecked(true);
//                    radio_qsr.setChecked(false);
//                    radio_retail.setChecked(false);
//                    selection = "Dine";
//                    WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
//                }
//                if (selected1 == radio_qsr.getId()) {
//                    radio_dine.setChecked(false);
//                    radio_qsr.setChecked(true);
//                    radio_retail.setChecked(false);
//                    selection = "Qsr";
//                    WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
//                }
//                if (selected1 == radio_retail.getId()) {
//                    radio_dine.setChecked(false);
//                    radio_qsr.setChecked(false);
//                    radio_retail.setChecked(true);
//                    selection = "Retail";
//                    WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
//                }
//
//            }
//        });
//
//        radio_dine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (radio_dine.isChecked()) {
//                    radio_qsr.setChecked(false);
//                    radio_retail.setChecked(false);
//                    selection = "Dine";
//                    WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
//                }
//            }
//        });
//
//        radio_qsr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (radio_qsr.isChecked()) {
//                    radio_dine.setChecked(false);
//                    radio_retail.setChecked(false);
//                    selection = "Qsr";
//                    WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
//                }
//            }
//        });
//
//        radio_retail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (radio_retail.isChecked()) {
//                    radio_dine.setChecked(false);
//                    radio_qsr.setChecked(false);
//                    selection = "Retail";
//                    WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
//                }
//            }
//        });


        TextView copy1 = findViewById(R.id.copy1);
        copy1.setMovementMethod(LinkMovementMethod.getInstance());

// new changes r


        Amplify.Auth.fetchAuthSession(
                result -> {
//                    Log.i("AmplifyAuthSession", result.toString());
                    if (result.isSignedIn()) {
                        // Pin Activity Will Open
                        AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;
//                        Log.d("CognitoSessionIDT", String.valueOf(cognitoAuthSession));

 //                       assert cognitoAuthSession.getUserPoolTokens().getValue() != null;

                        if (cognitoAuthSession.getUserPoolTokens().getValue().getAccessToken() != null) {
//                            Log.d("AccessToken", cognitoAuthSession.getUserPoolTokens().getValue().getAccessToken());
//                            Intent intent = new Intent(MainActivity_Signin_OTPbased.this, MainActivity.class);
//                            startActivity(intent);
                        } else {
                            Log.d("AccessTokenE", "Token Is Null");

//                            Log.d("AccessTokenE", "Token Is Null");
//                            Amplify.Auth.signOut(
//                                    () -> Log.i("AuthQuickstart", "Signed out successfully"),
//                                    error -> Log.e("AuthQuickstart", error.toString())
//                            );

                        }

                    } else {
                        // Sign-in screen will Open
                    }
                },
                error -> Log.e("AmplifyQuickstart", error.toString())
        );

        LinearLayout email_layout = (LinearLayout) findViewById(R.id.email_layout);
        LinearLayout phone_number_layout = (LinearLayout) findViewById(R.id.phone_number_layout);

//        switch_button = (Switch) findViewById(R.id.switch_button);
//        switch_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
////                    Toast.makeText(MainActivity_Signin_OTPbased.this, "checked", Toast.LENGTH_LONG).show();
//                    phone_number_layout.setVisibility(View.GONE);
//                    email_layout.setVisibility(View.VISIBLE);
//                }else {
////                    Toast.makeText(MainActivity_Signin_OTPbased.this, "not checked", Toast.LENGTH_LONG).show();
//                    phone_number_layout.setVisibility(View.VISIBLE);
//                    email_layout.setVisibility(View.GONE);
//                }
//            }
//        });

        initApp();


        boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
        if (firstrun){
            final Dialog dialog = new Dialog(MainActivity_Signin_OTPbased.this, R.style.notitle);
            dialog.setContentView(R.layout.terms_conditions);
            dialog.setCanceledOnTouchOutside(false);
//            dialog.setCancelable(false);
            dialog.show();

            Button agree = (Button) dialog.findViewById(R.id.agree);
            agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();

                    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                            .edit()
                            .putBoolean("firstrun", false)
                            .commit();
                }
            });

            dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface dialog, int keyCode,
                                     KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                        if (doubleBackToExitPressedOnce) {
                            System.out.println("Back 1");
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finishAffinity();
                        }

                        MainActivity_Signin_OTPbased.this.doubleBackToExitPressedOnce = true;
                        Toast.makeText(MainActivity_Signin_OTPbased.this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                doubleBackToExitPressedOnce=false;
                                System.out.println("Back 2");
                            }
                        }, 2000);
                    }
                    return true;
                }
            });


        }



    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }


    public void signUp(View view) {
        signUpNewUser();
    }


    public void phlogIn(){
        CountryCodePicker countryCode_picker = (CountryCodePicker) findViewById(R.id.countryCode_picker);
      //  countryCode_picker.registerCarrierNumberEditText(et_phone_number);
        country_name = countryCode_picker.getSelectedCountryName();
        String name_code = countryCode_picker.getSelectedCountryNameCode();
        name_exte = countryCode_picker.getSelectedCountryCodeWithPlus();


        if (isDeviceOnline()&& prvypol.isChecked()){

            email_signin = name_exte+""+et_phone_number.getText().toString();

            String onlyphno= et_phone_number.getText().toString();


//            if (prvypol.isChecked()){
                if (et_phone_number.getText().toString().equalsIgnoreCase("")) {
                    et_phone_number.setError("Please enter Phone number");
                    // progressBar.setVisibility(View.GONE);
                }else {
                    if (country_name.toString().equalsIgnoreCase("India")) {
                        if (et_phone_number.getText().toString().length() != 10) {
                            et_phone_number.setError("Enter valid number");
                            //  progressBar.setVisibility(View.GONE);
                        }else {
                            // loginmethod();
                            Intent it1 = new Intent(MainActivity_Signin_OTPbased.this, R_NewLogIn_BusinessType.class);
                            it1.putExtra("phone_ph", email_signin);
                            it1.putExtra("status","phone");
                            it1.putExtra("country_name1", country_name );
                            it1.putExtra("country_code",name_exte);
                            it1.putExtra("et_phoneno_only",onlyphno);
                            startActivity(it1);
                            //  progressBar.setVisibility(View.GONE);
                        }
                    }else {

                        Intent it1 = new Intent(MainActivity_Signin_OTPbased.this, R_NewLogIn_BusinessType.class);
                        it1.putExtra("phone_ph", email_signin);
                        it1.putExtra("status","phone");
                        it1.putExtra("country_name1", country_name );
                        it1.putExtra("country_code",name_exte);
                        it1.putExtra("et_phoneno_only",onlyphno);
                        startActivity(it1);

//                        if (country_name.toString().equalsIgnoreCase("United Arab Emirates (UAE)") || country_name.toString().equalsIgnoreCase("Singapore")) {
//                            if (et_phone_number.getText().toString().length() != 8) {
//                                et_phone_number.setError("Enter valid number");
//                                //  progressBar.setVisibility(View.GONE);
//                            }else {
//                                //  loginmethod();
//                                Intent it1 = new Intent(MainActivity_Signin_OTPbased.this, R_NewLogIn_BusinessType.class);
//                                it1.putExtra("phone_ph", email_signin);
//                                it1.putExtra("status","phone");
//                                it1.putExtra("country_name1", country_name );
//                                it1.putExtra("country_code",name_exte);
//                                it1.putExtra("et_phoneno_only",onlyphno);
//                                startActivity(it1);
//                                //  progressBar.setVisibility(View.GONE);
//                            }
//                        }else {
//                            if (country_name.toString().equalsIgnoreCase("Malaysia")) {
//                                if (et_phone_number.getText().toString().length() != 9) {
//                                    et_phone_number.setError("Enter valid number");
//                                    //  progressBar.setVisibility(View.GONE);
//                                }else {
//                                    //loginmethod();
//                                    Intent it1 = new Intent(MainActivity_Signin_OTPbased.this, R_NewLogIn_BusinessType.class);
//                                    it1.putExtra("phone_ph", email_signin);
//                                    it1.putExtra("status","phone");
//                                    it1.putExtra("country_name1", country_name );
//                                    it1.putExtra("country_code",name_exte);
//                                    it1.putExtra("et_phoneno_only",onlyphno);
//                                    startActivity(it1);
//                                    //  progressBar.setVisibility(View.GONE);
//                                }
//                            }else {
//                              //  countryCode_picker.registerCarrierNumberEditText(et_phone_number);
//                              //  countryCode_picker.getFullNumber();
//                              //  countryCode_picker.isValidFullNumber();
//                               // email_signin = name_exte+""+et_phone_number.getText().toString();
//                                //et_phone_number.getText();
//                             //   Intent it1 = new Intent(MainActivity_Signin_OTPbased.this, R_NewLogIn_BusinessType.class);
////                                it1.putExtra("phone_ph", email_signin);
////                                it1.putExtra("status","phone");
////                                it1.putExtra("country_name1", country_name );
////                                it1.putExtra("country_code",name_exte);
////                                it1.putExtra("et_phoneno_only",onlyphno);
//                                // loginmethod();
////                                Intent it1 = new Intent(MainActivity_Signin_OTPbased.this, R_NewLogIn_BusinessType.class);
////                                startActivity(it1);
//                                // progressBar.setVisibility(View.GONE);
//                                Intent it1 = new Intent(MainActivity_Signin_OTPbased.this, R_NewLogIn_BusinessType.class);
//                                it1.putExtra("phone_ph", email_signin);
//                                it1.putExtra("status","phone");
//                                it1.putExtra("country_name1", country_name );
//                                it1.putExtra("country_code",name_exte);
//                                it1.putExtra("et_phoneno_only",onlyphno);
//                                startActivity(it1);
//
//                             //  Toast.makeText(MainActivity_Signin_OTPbased.this,"Not Available for Your Country",Toast.LENGTH_SHORT).show();
//                            }
//                        }
                    }
                }
      //      }

        }
        else {
            //showAlert();
            Toast.makeText(MainActivity_Signin_OTPbased.this,"Please select pivacy policy ",Toast.LENGTH_SHORT).show();
        }
    }
    public void logIn() {

        CountryCodePicker countryCode_picker = (CountryCodePicker) findViewById(R.id.countryCode_picker);

        System.out.println("Country is " + country_name);

        if (isDeviceOnline() == true) {

            if (!prvypol.isChecked()) {
                Toast.makeText(MainActivity_Signin_OTPbased.this, "please accept privacy policy", Toast.LENGTH_LONG).show();

            } else {

                email_signin = inUsername.getText().toString();


           //     if (prvypol.isChecked()) {
                    if (inUsername.getText().toString().equalsIgnoreCase("")) {
                        inUsername.setError("Please enter Email/Username");
                    } else {
                        // loginmethod();
                        Intent it1 = new Intent(MainActivity_Signin_OTPbased.this, R_NewLogIn_BusinessType.class);
                        it1.putExtra("email_ph", email_signin);
                        it1.putExtra("status","email");
                        startActivity(it1);
                    }
            //
            }
        }


//                if (email_signin.toString().equals("")) {
//                    Toast.makeText(MainActivity_Signin_OTPbased.this, "Enter Username and Password", Toast.LENGTH_LONG).show();
//                } else {
//
//
//                    mConnectionClassManager = ConnectionClassManager.getInstance();
//                    mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
//                    progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
//                    progressBar.setVisibility(View.VISIBLE);
//                    mTries = 0;
//
//                    ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//                    NetworkInfo info = cm.getActiveNetworkInfo();
//                    if (info.getType() == ConnectivityManager.TYPE_WIFI) {
//                        loginWebservice();
//                    } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
//                        // check NetworkInfo subtype
//                        if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS) {
//                            speedTestTask();
//                            // Bandwidth between 100 kbps and below
//                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE) {
//                            speedTestTask();
//
//                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_1xRTT) {
//                            speedTestTask();
//                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA) {
//                            speedTestTask();
//                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
//                            speedTestTask();
//                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_IDEN) {
//                            speedTestTask();
//                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_UMTS) {
//                            speedTestTask();
//                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_0) {
//                            speedTestTask();
//                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_A) {
//                            speedTestTask();
//                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSDPA) {
//                            speedTestTask();
//                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSPA) {
//                            speedTestTask();
//                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_B) {
//                            speedTestTask();
//                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSPAP) {
//                            speedTestTask();
//                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EHRPD) {
//                            speedTestTask();
//                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSUPA) {
//                            speedTestTask();
//                        } else {
//                            loginWebservice();
//                        }
//
//                    }
//
//
//                }

        else {
            //  progressBar.setVisibility(View.GONE);
            showAlert();
        }
    }





    @Override
    protected void onResume() {
        super.onResume();
        MainActivity_Signin_OTPbased.storeList.clear();
    }

    private void signUpNewUser() {

//        if (ContextCompat.checkSelfPermission(MainActivity_Signin_OTPbased.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity_Signin_OTPbased.this,
//                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//
////                        Toast.makeText(ForgotPasswordActivity.this, "111111111", Toast.LENGTH_SHORT).show();
//
//                ActivityCompat.requestPermissions(MainActivity_Signin_OTPbased.this,
//                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        1);
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
////                        Toast.makeText(ForgotPasswordActivity.this, "no permission", Toast.LENGTH_SHORT).show();
//
//            } else {
//
//                // No explanation needed, we can request the permission.
//
//                ActivityCompat.requestPermissions(MainActivity_Signin_OTPbased.this,
//                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        1);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        } else {
////                    Toast.makeText(ForgotPasswordActivity.this, "hiiii", Toast.LENGTH_SHORT).show();
//
////            if (!SdIsPresent()) ;
//
//            Intent registerActivity = new Intent(this, MainActivity_Register_OTPbased.class);
//            startActivity(registerActivity);
//        }

        Intent registerActivity = new Intent(this, MainActivity_Register_OTPbased.class);
        startActivity(registerActivity);

//        Intent registerActivity = new Intent(this, MainActivity_Register_OTPbased.class);
//        startActivityForResult(registerActivity, 1);
    }

    private void initApp() {
        inUsername = (EditText) findViewById(R.id.editTextUserId);
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);


    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case 3:
                // Forgot password
                if(resultCode == RESULT_OK) {
                    newPass = data.getStringExtra("newPass");
                    code = data.getStringExtra("code");
                    username=data.getStringExtra("destination");
                    password=data.getStringExtra("password");
                    if (newPass != null && code != null) {
                        if (!newPass.isEmpty() && !code.isEmpty()) {
                            Toast.makeText(MainActivity_Signin_OTPbased.this,"Setting new password...",Toast.LENGTH_SHORT).show();
                            forgotPasswordContinuation.setPassword(newPass);
                            forgotPasswordContinuation.setVerificationCode(code);
                            forgotPasswordContinuation.continueTask();
                        }
                    }
                }
                break;

        }
    }









    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();

    }


    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            Log.v(LOG_TAG, "Receieved notification about network status");
            isNetworkAvailable(context);

        }

        private boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivity = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            if (!isConnected) {
                                Log.v(LOG_TAG, "Now you are connected to Internet!");
                                Toast.makeText(context, "Now you are connected to Internet!", Toast.LENGTH_SHORT).show();
                                isConnected = true;
                                //do your processing here ---
                                //if you need to post any data to the server or get status
                                //update from the server
                            }
                            return true;
                        }
                    }
                }
            }
            Log.v(LOG_TAG, "You are not connected to Internet!");
            Toast.makeText(context, "You are not connected to Internet!", Toast.LENGTH_SHORT).show();
            isConnected = false;
            return false;
        }
    }

    public void loginWebservice(){

        if (ContextCompat.checkSelfPermission(MainActivity_Signin_OTPbased.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity_Signin_OTPbased.this,
                    permissions(),
                    3);
            /*if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity_Signin_OTPbased.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity_Signin_OTPbased.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        3);
            } else {
                ActivityCompat.requestPermissions(MainActivity_Signin_OTPbased.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        3);
            }*/
        } else {
//            loginWebservice1();
            if (ContextCompat.checkSelfPermission(MainActivity_Signin_OTPbased.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity_Signin_OTPbased.this,
                        permissions(),
                        3);
                /*if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity_Signin_OTPbased.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(MainActivity_Signin_OTPbased.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            3);
                } else {
                    ActivityCompat.requestPermissions(MainActivity_Signin_OTPbased.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            3);
                }*/
            } else {
                loginWebservice1();

            }

        }


    }

    public void loginWebservice1(){
//        progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
//        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(MainActivity_Signin_OTPbased.this);
        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"login_caps_otp.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
//                        System.out.println("email signin "+responseString);
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
//                        Log.e("response",response.toString());
//                        Log.e("response",jsonObject.toString());
                        if(response.equalsIgnoreCase("success")){

                            progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
//                            progressBar.setVisibility(View.GONE);
                            parseJson(jsonObject);
                            System.out.println(jsonObject);




                        }else{
                            progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
                            progressBar.setVisibility(View.GONE);

                            test_email_company_signin();

//                            Toast.makeText(MainActivity_Signin_OTPbased.this, "Login failed", Toast.LENGTH_SHORT).show();
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
                params.put("email",email_signin);
//                System.out.println("email signin "+email_signin);
                return params;
            }
        };
    /*    sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        sr.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        queue.add(sr);
    }

    public  void test_email_company_signin() {
        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"test_email_company_signin.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response1234",response);
                        if(response.equalsIgnoreCase("email not exists")){
                            showDialogMessage("Enter valid Email id");
                        }else{
                            if(response.equalsIgnoreCase("password not exists")){
                                showDialogMessage("Enter valid Password");
                            }else{
                                showDialogMessage(getString(R.string.sdm6));
                            }
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
                params.put("email",email_signin);
//                params.put("password",inPassword.getText().toString());
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);
    }

    private void showDialogMessage(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity_Signin_OTPbased.this).create();

        alertDialog.setTitle(getString(R.string.title10));
        alertDialog.setMessage(message);
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void parseJson(JSONObject jarray) {
        String company="",email="",password="",selection1="", company_special= "";

        try {
            company=jarray.getString("company");
            email=jarray.getString("email");
            password=jarray.getString("password");
            company_special=jarray.getString("company_special");
  //          selection1=jarray.getString("account_selection");
            JSONArray stores=jarray.getJSONArray("store");

//            for(int i=0;i<stores.length();i++){
//
//                StoreBean storeBean=new StoreBean();
//                JSONObject storeobject=stores.getJSONObject(i);
//                String store_name=storeobject.getString("store_name");
//                JSONArray devices=storeobject.getJSONArray("devices");
//                String[] deviceArray=new String[devices.length()];
//                for(int j=0;j<devices.length();j++){
//                    deviceArray[j]=devices.getString(j);
//                }
//
//                storeBean.setStoreName(store_name);
//                storeBean.setDevices(deviceArray);
//                storeList.add(storeBean);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences pref = getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("emailid", email);
        editor.putString("companyname", company);
        editor.putString("password",password);
        editor.putString("companyname_special",company_special);

        System.out.println("list is "+email+" "+company+" "+password+" "+company_special);

        if (selection.toString().equals("Dine")) {
            editor.putString("account_selection", "Dine");
            System.out.println("selection is "+selection+" IVEPOS");
        }else {
            if (selection.toString().equals("Qsr")) {
                editor.putString("account_selection", "Qsr");
                System.out.println("selection is "+selection+" IVEPOSQSR");
            }else {
                editor.putString("account_selection", "Retail");
                System.out.println("selection is " + selection + " IVEPOSRETAIL");
            }
        }
//        editor.commit();

//        SharedPreferences pref1 = getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
//        SharedPreferences.Editor editor1 = pref1.edit();
//        editor.putString("account_selection", selection);
        editor.commit();

        textView2 = (TextView) findViewById(R.id.seconds);
        textView2.setText("");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor1 = prefs.edit();

        String ipAddress = getLocalIpAddress();
        String ipAddress_stored = prefs.getString("ip_address", "");
//        Log.d("ip addressed shared", ipAddress_stored);
        Integer ipCount  = prefs.getInt("ip_count",-1);

        if(ipAddress_stored.equals("") || ipCount.equals("-1")){
            editor1.putInt("ip_count", 1);
            editor1.putString("ip_address", getLocalIpAddress());
            editor1.apply();
        }

//        Log.d("ip count", String.valueOf(ipCount));

        if(ipCount > 3){
//            Log.d("Count exceeded", ipCount.toString());
            button.setEnabled(false);
            button.setBackgroundResource(R.drawable.dark_black_rounded_corners);

            new CountDownTimer(30000, 1000) {

                @SuppressLint("SetTextI18n")
                public void onTick(long millisUntilFinished) {
                    textView2.setText("Request OTP after " + millisUntilFinished / 1000+" seconds");
                    // logic to set the EditText could go here
                }

                public void onFinish() {
                    textView2.setText("");
                    button.setBackgroundResource(R.drawable.red_click_shape_rounded_corners);
                    button.setEnabled(true);
                }

            }.start();
        }
//        textView.setText(ipCount.toString());
        if(ipAddress.equals(ipAddress_stored)){
//            Log.d("Ip match", ipAddress_stored);
            Integer ipCount_increase = ipCount + 1;
            editor1.putInt("ip_count", ipCount_increase);
            editor1.putString("ip_address", ipAddress);
            editor1.apply();
//            textView.setText(ipCount_increase.toString());
        }

//        if (prvypol.isChecked()) {
            loadExistingOldCustomerStatusData(email_signin, company, email);
//            Log.d("ExistingUser", customerStatus);
//        }else {
          //  loadExistingOldCustomerStatusData(email_signin, company, email);
          //  signInWithPhoneNumber(email_signin, company, email);
        }

//        Intent intent=new Intent(MainActivity_Signin_OTPbased.this,Checking_Store.class);
//        intent.putExtra("company",company);
//        intent.putExtra("email",email);
//        startActivity(intent);
 //   }


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
                    // Toast.makeText(MainActivity_Signin_OTPbased.this, "permission granted", Toast.LENGTH_SHORT).show();
                    if (!SdIsPresent()) ;

                    Intent registerActivity = new Intent(this, MainActivity_Register_OTPbased.class);
                    startActivity(registerActivity);


                } else {

                    //   Toast.makeText(MainActivity_Signin_OTPbased.this, "permission denied", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //Toast.makeText(MainActivity_Signin_OTPbased.this, "permission granted", Toast.LENGTH_SHORT).show();
                    System.out.println("permission granted");
                    if (!SdIsPresent()) ;


                } else {

                    Toast.makeText(MainActivity_Signin_OTPbased.this, "permission denied", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case 3: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // Toast.makeText(MainActivity_Signin_OTPbased.this, "permission granted", Toast.LENGTH_SHORT).show();
                    progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
                    progressBar.setVisibility(View.VISIBLE);
                    RequestQueue queue = Volley.newRequestQueue(MainActivity_Signin_OTPbased.this);
                    StringRequest sr = new StringRequest(
                            com.android.volley.Request.Method.POST,
                            WebserviceUrl + "login_caps_otp.php",
                            new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String responseString) {
                                    String response = "";
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(responseString);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        response = jsonObject.getString("status");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (response.equalsIgnoreCase("success")) {

                                        progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
                                        progressBar.setVisibility(View.GONE);
                                        parseJson(jsonObject);
                                        System.out.println(jsonObject);


                                    } else {
                                        progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
                                        progressBar.setVisibility(View.GONE);

                                        test_email_company_signin();

//                            Toast.makeText(MainActivity_Signin_OTPbased.this, "Login failed", Toast.LENGTH_SHORT).show();
                                    }

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
                            params.put("email", email_signin);
//                            params.put("password",inPassword.getText().toString());
                            return params;
                        }
                    };
    /*    sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
                    sr.setRetryPolicy(new RetryPolicy() {
                        @Override
                        public int getCurrentTimeout() {
                            return 50000;
                        }

                        @Override
                        public int getCurrentRetryCount() {
                            return 50000;
                        }

                        @Override
                        public void retry(VolleyError error) throws VolleyError {

                        }
                    });
                    queue.add(sr);


                } else {

                    //   Toast.makeText(MainActivity_Signin_OTPbased.this, "permission denied", Toast.LENGTH_SHORT).show();

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


    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity_Signin_OTPbased.this).create();

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


    public void speedTestTask(){

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.groovypost.com/wp-content/uploads/2015/05/Android-file-transfer.png")
                .build();

        mDeviceBandwidthSampler.startSampling();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                mDeviceBandwidthSampler.stopSampling();
                // Retry for up to 10 times until we find a ConnectionClass.
                if (mConnectionClass == ConnectionQuality.UNKNOWN && mTries < 10) {
                    mTries++;
                    speedTestTask();
                }
                if (!mDeviceBandwidthSampler.isSampling()) {
                    progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    Log.d(TAG, responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                Log.d(TAG, response.body().string());
                Log.d(TAG, mConnectionClassManager.getCurrentBandwidthQuality().toString());

                if((mConnectionClassManager.getCurrentBandwidthQuality().toString().equalsIgnoreCase("POOR"))||(mConnectionClassManager.getCurrentBandwidthQuality().toString().equalsIgnoreCase("UNKNOWN"))){
//                    showAlertSlow();
                    loginWebservice();
                }else {
                    loginWebservice();
                }

                mDeviceBandwidthSampler.stopSampling();
            }

        });

    }


    private void showAlertSlow() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity_Signin_OTPbased.this).create();

        alertDialog.setTitle(getString(R.string.title10));
        alertDialog.setMessage(getString(R.string.setmessage31));
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialog.show();
    }


    public  void loginmethod() {

        SharedPreferences pref = getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
        boolean isSignedin= pref.getBoolean("signindb", false);

        if(!isSignedin){
            SharedPreferenceManager.initializeInstance(getApplicationContext());
            boolean dataDownloadValue = SharedPreferenceManager.getInstance().getDataDownloadValue();


            int SPLASH_DISPLAY_LENGTH = 1500;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Amplify.Auth.fetchAuthSession(
                            result -> {
//                                Log.i("AmplifyAuthSession", result.toString());
                                if (result.isSignedIn()) {
                                    // Pin Activity Will Open
                                    AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;
//                                    Log.d("CognitoSessionIDT", String.valueOf(cognitoAuthSession));

//                                    assert cognitoAuthSession.getUserPoolTokens().getValue() != null;
                                    if (cognitoAuthSession.getUserPoolTokens().getValue().getAccessToken() != null && dataDownloadValue) {
//                                        Log.d("AccessToken", cognitoAuthSession.getUserPoolTokens().getValue().getAccessToken());
//                                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
//                                    startActivity(intent);
//                                    finish();
                                    } else {

                                        Amplify.Auth.signOut(
                                                () -> Log.i("AuthQuickstart", "Signed out successfully"),
                                                error -> Log.e("AuthQuickstart", error.toString())
                                        );

                                        Log.d("AccessTokenE", "Token Is Null");
//                                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity_Signin_OTPbased.class);
//                                    startActivity(intent);
//                                    finish();
                                    }

                                } else {
                                    // Sign-in screen will Open
                                    /* Create an Intent that will start the Menu-Activity. */
//                                Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity_Signin_OTPbased.class);
//                                startActivity(mainIntent);
//                                finish();
                                }
                            },
                            error -> Log.e("AmplifyQuickstart", error.toString())

                    );

                    mConnectionClassManager = ConnectionClassManager.getInstance();
                    mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
                    progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
                    progressBar.setVisibility(View.VISIBLE);
                    mTries = 0;

                    ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo info = cm.getActiveNetworkInfo();
                    if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                        loginWebservice();
                    } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // check NetworkInfo subtype
                        if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS) {
                            speedTestTask();
                            // Bandwidth between 100 kbps and below
                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE) {
                            speedTestTask();

                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_1xRTT) {
                            speedTestTask();
                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA) {
                            speedTestTask();
                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
                            speedTestTask();
                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_IDEN) {
                            speedTestTask();
                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_UMTS) {
                            speedTestTask();
                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_0) {
                            speedTestTask();
                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_A) {
                            speedTestTask();
                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSDPA) {
                            speedTestTask();
                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSPA) {
                            speedTestTask();
                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                            speedTestTask();
                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSPAP) {
                            speedTestTask();
                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EHRPD) {
                            speedTestTask();
                        } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSUPA) {
                            speedTestTask();
                        } else {
                            loginWebservice();
                        }

                    }
                }
            }, SPLASH_DISPLAY_LENGTH);
        }

//        mConnectionClassManager = ConnectionClassManager.getInstance();
//        mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
//        progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
//        progressBar.setVisibility(View.VISIBLE);
//        mTries = 0;
//
//        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo info = cm.getActiveNetworkInfo();
//        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
//            loginWebservice();
//        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
//            // check NetworkInfo subtype
//            if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS) {
//                speedTestTask();
//                // Bandwidth between 100 kbps and below
//            } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE) {
//                speedTestTask();
//
//            } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_1xRTT) {
//                speedTestTask();
//            } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA) {
//                speedTestTask();
//            } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
//                speedTestTask();
//            } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_IDEN) {
//                speedTestTask();
//            } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_UMTS) {
//                speedTestTask();
//            } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_0) {
//                speedTestTask();
//            } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_A) {
//                speedTestTask();
//            } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSDPA) {
//                speedTestTask();
//            } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSPA) {
//                speedTestTask();
//            } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_B) {
//                speedTestTask();
//            } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSPAP) {
//                speedTestTask();
//            } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EHRPD) {
//                speedTestTask();
//            } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSUPA) {
//                speedTestTask();
//            } else {
//                loginWebservice();
//            }
//
//        }
    }

    /*  Phone Number Sign-In  */
    private void signInWithPhoneNumber(String finalData, String company, String email) {
        Log.d("LoginScreenActivity", "PhoneNumberMethod");

        String ipAddress = getLocalIpAddress();

        ArrayList<AuthUserAttribute> attributes = new ArrayList<>();
        attributes.add(new AuthUserAttribute(AuthUserAttributeKey.custom("custom:existing_user_signup"), "true"));
        attributes.add(new AuthUserAttribute(AuthUserAttributeKey.custom("custom:ip_address"), ipAddress));

//        if (email_signin.equalsIgnoreCase(customerEmailId)) {
        attributes.add(new AuthUserAttribute(AuthUserAttributeKey.phoneNumber(), finalData));
//        }

        if (customerStatus.equals("false")) {
            System.out.println("update phonenumber "+finalData);
            updateExistingCustomerStatus(finalData, "true", company);
        }

        Amplify.Auth.signUp(//this create a new user
                finalData,
                "ivepos@123",//<- password from database
                AuthSignUpOptions.builder().userAttributes(attributes).build(),
                result -> {
//                    Log.i("AuthQuickstart1.11", result.toString());
                    if (result.isSignUpComplete()) {

                        Amplify.Auth.signIn( // this will sign-in the new user
                                finalData,
                                "ivepos@123",
                                result2 -> {
//                                    Log.i("AuthQuickstart-1.1", result2.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");
                                    Intent intent = new Intent(MainActivity_Signin_OTPbased.this, SignIn_OTP_Verify_Activity.class);
                                    intent.putExtra("company",company);
                                    intent.putExtra("email",email);
                                    intent.putExtra(FINAL_DATA, finalData);
                                    intent.putExtra("password",password);
                                    intent.putExtra(CUSTOMER_STATUS, customerStatus);
                                    startActivity(intent);
                                    progressBar.setVisibility(View.GONE);
                                },
                                error -> Log.e("AuthQuickstart-1.21", error.toString())
                        );
                    }
                },
//                error -> Log.e("AuthQuickstart1.12", error.toString())

                error -> {Log.e("AuthQuickstart-1.12", error.toString());
//                            Log.d("AuthQuickstart-1.221", error.getMessage());
//                            Log.d("AuthQuickstart-1.222", String.valueOf(error.getStackTrace()));
                    if (error.toString().contains("PreSignUp failed with error Access denied")) {
//                        System.out.println("PreSignUp failed with error Access denied");
                        this.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity_Signin_OTPbased.this).create();

                                alertDialog.setTitle(getString(R.string.title10));
                                alertDialog.setMessage(getString(R.string.setmessage30));
                                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

                                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                progressBar.setVisibility(View.GONE);

                                alertDialog.show();
                            }
                        });
                    }
                }
        );

//        Amplify.Auth.signIn(
//                finalData,
//                "ivepos@123",
//                result -> {
//                    Log.i("AuthQuickstart-1.1", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");
//                    Intent intent = new Intent(MainActivity_Signin_OTPbased.this, SignIn_OTP_Verify_Activity.class);
//                    intent.putExtra("company",company);
//                    intent.putExtra("email",email);
//                    intent.putExtra(FINAL_DATA, finalData);
//                    intent.putExtra(CUSTOMER_STATUS, customerStatus);
//                    startActivity(intent);
//                    progressBar.setVisibility(View.GONE);
//                },
//                error -> Log.e("AuthQuickstart-1.2", error.toString())
//        );
    }

    private void loadExistingOldCustomerStatusData(String email_signin, String company, String email) {

//        Log.d("Data", email);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, WebserviceUrl + URL_END_POINT_GET_OLD_USER_STATUS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ExistingUserStatusData", response);
                        JSONObject responseObj = null;
                        try {
                            responseObj = new JSONObject(response);
//                            Log.d("ExistingUserJSONOBJ", String.valueOf(responseObj));
                            getResponseDataValue(responseObj, company, email);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            response = responseObj.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (response.equalsIgnoreCase("success")) {
//                            Log.d("ExistingUserParsedData", responseObj.toString());
                        } else {
                            Toast.makeText(MainActivity_Signin_OTPbased.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ExistingUserDetails:", "Error:-" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getResponseDataValue(JSONObject responseObj, String company, String email) {
        try {
            JSONObject responseObjJSONObject = responseObj.getJSONObject("response");
            this.customerStatus = responseObjJSONObject.getString("existing_user_status");
            this.customerEmailId = responseObjJSONObject.getString("email");
//            Log.d("ExistingUserInside", customerStatus);

            if (customerStatus.equals("true")) {
                Amplify.Auth.signIn(
                        this.email_signin,
                        "ivepos@123",
                        result -> {
                            Intent intent = new Intent(MainActivity_Signin_OTPbased.this, SignIn_OTP_Verify_Activity.class);
                            intent.putExtra("company",company);
                            intent.putExtra("email",email);
                            intent.putExtra(FINAL_DATA, email_signin);
                            intent.putExtra("password",password);
                            intent.putExtra(CUSTOMER_STATUS, customerStatus);
                            startActivity(intent);
                            progressBar.setVisibility(View.GONE);
//                            Log.i("AuthQuickstart-1.1", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");
                        },

                        error -> {Log.e("AuthQuickstart-1.22", error.toString());
//                            Log.d("AuthQuickstart-1.221", error.getMessage());
//                            Log.d("AuthQuickstart-1.222", String.valueOf(error.getStackTrace()));
                            if (error.toString().contains("User not found in the system")) {
                                updateExistingCustomerStatus(email, "false", company);
                            }
                        }


                );
            } else {
                if (prvypol.isChecked()) {
                    signInWithEmailId(customerEmailId, company, email, customerStatus);
                }else {
                    signInWithPhoneNumber(email_signin, company, email);
                }
//                signInWithEmailId(customerEmailId, company, email, customerStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*  Email ID Sign-In    */
    private void signInWithEmailId(String customerEmailId, String company, String email, String customerStatus) {

//        Log.d("LoginScreenActivity", "EmailMethod");

        String ipAddress = getLocalIpAddress();

        ArrayList<AuthUserAttribute> attributes = new ArrayList<>();
        attributes.add(new AuthUserAttribute(AuthUserAttributeKey.custom("custom:existing_user_signup"), "true"));
        attributes.add(new AuthUserAttribute(AuthUserAttributeKey.custom("custom:ip_address"), ipAddress));

        if (email_signin.equalsIgnoreCase(customerEmailId)) {
            attributes.add(new AuthUserAttribute(AuthUserAttributeKey.email(), email_signin));
        }

        if (customerStatus.equals("false")) {
            updateExistingCustomerStatus(customerEmailId, "true", company);
        }

        Amplify.Auth.signUp(//this create a new user
                email_signin,
                "ivepos@123",//<- password from database
                AuthSignUpOptions.builder().userAttributes(attributes).build(),
                result -> {
//                    Log.i("AuthQuickstart1.11", result.toString());
                    if (result.isSignUpComplete()) {

                        Amplify.Auth.signIn( // this will sign-in the new user
                                email_signin,
                                "ivepos@123",
                                result2 -> {
//                                    Log.d("result2", String.valueOf(result2.isSignInComplete()));
//                                    Log.i("AuthQuickstart-1.1", result2.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");
                                    Intent intent = new Intent(MainActivity_Signin_OTPbased.this, SignIn_OTP_Verify_Activity.class);
                                    intent.putExtra("company",company);
                                    intent.putExtra("email",email);
                                    intent.putExtra(FINAL_DATA, email_signin);
                                    intent.putExtra(BASE_URL_LINK, WebserviceUrl);
                                    intent.putExtra(CUSTOMER_STATUS, customerStatus);
                                    intent.putExtra("password",password);
                                    startActivity(intent);
                                    progressBar.setVisibility(View.GONE);
                                },
                                error -> Log.e("AuthQuickstart-1.23", error.toString())
                        );
                    }
                },
//                error -> Log.e("AuthQuickstart1.12", error.toString())

                error -> {Log.e("AuthQuickstart-1.12", error.toString());
//                            Log.d("AuthQuickstart-1.221", error.getMessage());
//                            Log.d("AuthQuickstart-1.222", String.valueOf(error.getStackTrace()));
                    if (error.toString().contains("PreSignUp failed with error Access denied")) {
//                        System.out.println("PreSignUp failed with error Access denied");
                        this.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity_Signin_OTPbased.this).create();

                                alertDialog.setTitle(getString(R.string.title10));
                                alertDialog.setMessage(getString(R.string.setmessage30));
                                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

                                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                progressBar.setVisibility(View.GONE);

                                alertDialog.show();
                            }
                        });
                    }
                }
        );
    }

    private void updateExistingCustomerStatus(String email, String bool, String company) {
//        Log.d("Data", email + "true");
//        this.requestQueue = RequestSingleton.getInstance(requireActivity()).getInstance();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        System.out.println("update phonenumber1 "+BASE_URL+" "+URL_END_POINT_UPDATE_EXISTING_OLD_USER_STATUS);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, WebserviceUrl+BASE_URL + URL_END_POINT_UPDATE_EXISTING_OLD_USER_STATUS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("CustHistoryHelloData", response);
                        JSONObject responseObj = null;
//                        parsedJsonResponseData(response);
                        try {
                            responseObj = new JSONObject(response);
//                            Log.d("CustomerHistoryJSONOBJ", String.valueOf(responseObj));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            response = responseObj.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (response.equalsIgnoreCase("success")) {
//                            Log.d("CustHistoryParsedData", responseObj.toString());

                            if (bool.toString().equals("false")) {
                                if (prvypol.isChecked()) {
                                    loadExistingOldCustomerStatusData(email_signin, company, email);
//                                    Log.d("ExistingUser", customerStatus);
                                } else {
                                    loadExistingOldCustomerStatusData(email_signin, company, email);
//                                    signInWithPhoneNumber(email_signin, company, email);
                                }
                            }

                        } else {
                            Toast.makeText(MainActivity_Signin_OTPbased.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CustomerHistoryDetails:", "Error:-" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("status", bool);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public String getLocalIpAddress() {
        String TAG ="IP address";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.i(TAG, "***** IP="+ ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        return null;
    }

    Boolean validation()

     {
         CountryCodePicker countryCode_picker = (CountryCodePicker) findViewById(R.id.countryCode_picker);
         countryCode_picker.registerCarrierNumberEditText(et_phone_number);
         et_phone_number.getText();
         return true;
    }

}