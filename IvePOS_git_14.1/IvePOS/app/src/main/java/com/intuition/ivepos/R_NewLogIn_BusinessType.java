package com.intuition.ivepos;

import static com.intuition.ivepos.Constant.WebserviceUrl;
import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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
import com.intuition.ivepos.signup.DownloadService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class R_NewLogIn_BusinessType extends AppCompatActivity {
    SharedPreferences prefs;
    Dialog dialog_auth;
    TextView tv;
    EditText et_phone_number_dialog;
    String  textcompanyname, storeitem, deviceitem, from, countrynameget,countrycodeget;
    boolean mSubscribedToDelaroy;
    Switch switch_button1;
    String checking;
    SQLiteDatabase db;
    Button btnlogin;
    SQLiteDatabase db_appda = null;
    TextView et_store,et_device;
    String name_exte, country_name;
    SharedPreferences.Editor editor;
    String company, store, device,phone_number, phone_number1, email,password,confpassword, company_special;
    private String finalData = "";

    JSONObject jsonObject;

    public static ArrayList<StoreBean> storeList=new ArrayList<StoreBean>();
    static final String URL_END_POINT_GET_OLD_USER_STATUS = "getOldUserData.php";
    private static final String URL_END_POINT_UPDATE_EXISTING_OLD_USER_STATUS = "updateExistingOldUserStatus.php";

    private static final String FINAL_DATA = "FinalData";
    private static final String CUSTOMER_STATUS = "CustomerStatus";


    private static final String BASE_URL_LINK = "BaseUrlLink";
    private static String BASE_URL = "";
    private EditText inUsername;
    EditText editTextUserId;
   private EditText et_phone_number;
    boolean doubleBackToExitPressedOnce = false;
    RadioGroup radioGroupSplit;
    RadioButton radioBtnsplit, radio_dine, radio_qsr, radio_retail;
    LinearLayout layout_dine, layout_qsr, layout_retail;
    RadioGroup radioGroup1;
    String selection,selection1;
    RelativeLayout progressBar;
    String name_exte_dialog;
    private String customerEmailId;
    String customerStatus = "";
    RequestQueue requestQueue;
    TextView textView2;
  //  String selection;
   private String email_signin;

    Button button;
    ConnectionClassManager mConnectionClassManager;
    DeviceBandwidthSampler mDeviceBandwidthSampler;
    private final String TAG = "MainActivity";
    ConnectionQuality mConnectionClass = ConnectionQuality.UNKNOWN;

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
    int mTries=0;
    Integer ipCount;
    String ipAddress_stored;
    String email_signin1;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rnew_log_in_business_type);


         prefs = PreferenceManager.getDefaultSharedPreferences(R_NewLogIn_BusinessType.this);
         editor = prefs.edit();

         // for database
        db = openOrCreateDatabase("amazon.db", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE if not exists register (_id integer PRIMARY KEY UNIQUE, companyname text, storename text,devicename text, email text, password text,confirmpassword text);");


        // email login passing data
        Bundle extras1 = getIntent().getExtras();
        email_signin1 = extras1.getString("email_ph");
        status = extras1.getString("status");
        System.out.println(email_signin1);

  //      password = extras1.getString("password");

        if (status.equals("email")){
            email_signin = extras1.getString("email_ph");
        }
        else {
            email_signin =extras1.getString("phone_ph");
        }

        phone_number=extras1.getString("et_phoneno_only");

        phone_number1=extras1.getString("et_phoneno_only");


        countrynameget =extras1.getString("country_name1");
        countrycodeget =extras1.getString("country_code");

        country_name = countrynameget;



        ipAddress_stored = prefs.getString("ip_address", "");
        if (ipAddress_stored != "") {
            ipCount  = prefs.getInt("ip_count",-1);
//            textView.setText(ipCount.toString());
        }




        layout_dine = (LinearLayout) findViewById(R.id.layout_dine);
        layout_qsr = (LinearLayout) findViewById(R.id.layout_qsr);
        layout_retail = (LinearLayout) findViewById(R.id.layout_retail);

        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);

        radio_dine = (RadioButton) findViewById(R.id.radio_dine);
        radio_qsr = (RadioButton) findViewById(R.id.radio_qsr);
        radio_retail = (RadioButton) findViewById(R.id.radio_retail);


        button = (Button) findViewById(R.id.login1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginmethod();
            }
        });



        final int selected1 = radioGroup1.getCheckedRadioButtonId();
        if (selected1 == radio_dine.getId()) {
            radio_dine.setChecked(true);
        //    Toast.makeText(R_NewLogIn_BusinessType.this, "Clicked Dine", Toast.LENGTH_SHORT).show();
            radio_qsr.setChecked(false);
            radio_retail.setChecked(false);
            selection = "Dine";
            selection1 = "dine";
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }
        if (selected1 == radio_qsr.getId()) {
            radio_dine.setChecked(false);
            radio_qsr.setChecked(true);
         //   Toast.makeText(R_NewLogIn_BusinessType.this, "Clicked fastfood", Toast.LENGTH_SHORT).show();
            radio_retail.setChecked(false);
            selection = "Qsr";
            selection1 = "qsr";
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }
        if (selected1 == radio_retail.getId()) {
            radio_dine.setChecked(false);
            radio_qsr.setChecked(false);
            radio_retail.setChecked(true);
         //   Toast.makeText(R_NewLogIn_BusinessType.this, "Clicked Retail", Toast.LENGTH_SHORT).show();
            selection = "Retail";
            selection1 = "retail";
            WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
        }

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                final int selected1 = radioGroup1.getCheckedRadioButtonId();
                radioBtnsplit = (RadioButton) findViewById(selected1);

                if (selected1 == radio_dine.getId()) {
                    radio_dine.setChecked(true);
                 //   Toast.makeText(R_NewLogIn_BusinessType.this,"Clicked dine",Toast.LENGTH_LONG).show();
                    radio_qsr.setChecked(false);
                    radio_retail.setChecked(false);
                    selection = "Dine";
                    selection1 = "dine";
                    WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                }
                if (selected1 == radio_qsr.getId()) {
                    radio_dine.setChecked(false);
                    radio_qsr.setChecked(true);
                 //   Toast.makeText(R_NewLogIn_BusinessType.this,"Clicked qsr",Toast.LENGTH_LONG).show();
                    radio_retail.setChecked(false);
                    selection = "Qsr";
                    selection1 = "qsr";
                    WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                }
                if (selected1 == radio_retail.getId()) {
                    radio_dine.setChecked(false);
                    radio_qsr.setChecked(false);
                    radio_retail.setChecked(true);
                  //  Toast.makeText(R_NewLogIn_BusinessType.this,"Clicked retail",Toast.LENGTH_LONG).show();
                    selection = "Retail";
                    selection1 = "retail";
                    WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
                }

            }
        });

        radio_dine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radio_dine.isChecked()) {
                    radio_qsr.setChecked(false);
                    radio_retail.setChecked(false);
                    selection = "Dine";
                    selection1 = "dine";
                    WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                }
            }
        });

        radio_qsr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radio_qsr.isChecked()) {
                    radio_dine.setChecked(false);
                    radio_retail.setChecked(false);
                    selection = "Qsr";
                    selection1 = "qsr";
                    WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                }
            }
        });

        radio_retail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radio_retail.isChecked()) {
                    radio_dine.setChecked(false);
                    radio_qsr.setChecked(false);
                    selection = "Retail";
                    selection1 = "retail";
                    WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
                }
            }
        });


//        TextView copy1 = findViewById(R.id.copy1);
//        copy1.setMovementMethod(LinkMovementMethod.getInstance());


        Amplify.Auth.fetchAuthSession(
                result -> {
//                    Log.i("AmplifyAuthSession", result.toString());
                    if (result.isSignedIn()) {
                        // Pin Activity Will Open
                        AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;
//                        Log.d("CognitoSessionIDT", String.valueOf(cognitoAuthSession));

//                        assert cognitoAuthSession.getUserPoolTokens().getValue() != null;
  //                      assert cognitoAuthSession.getUserPoolTokens().getValue() != null;
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
            final Dialog dialog = new Dialog(R_NewLogIn_BusinessType.this, R.style.notitle);
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

                        R_NewLogIn_BusinessType.this.doubleBackToExitPressedOnce = true;
                        Toast.makeText(R_NewLogIn_BusinessType.this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

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

   //******************************************************store create codes
        ImageView linearLayout_overflow = findViewById(R.id.linearLayout_overflow);
        final PopupMenu popup = new PopupMenu(this, linearLayout_overflow);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.signup_menu, popup.getMenu());
        linearLayout_overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        tv = new TextView(R_NewLogIn_BusinessType.this);
                        tv.setText(selection);
                        if (tv.getText().toString().equals("")) {
                            Toast.makeText(R_NewLogIn_BusinessType.this, "Select business type", Toast.LENGTH_LONG).show();
                        }else {
                            int id = item.getItemId();
                            if (id == R.id.linearLayout) {
                                dialog_auth = new Dialog(R_NewLogIn_BusinessType.this, R.style.notitle);
                                dialog_auth.setContentView(R.layout.signin_dialog_otp);
                                dialog_auth.setCanceledOnTouchOutside(false);
                                dialog_auth.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                dialog_auth.show();

                                editTextUserId = dialog_auth.findViewById(R.id.editTextUserId);

                                et_phone_number_dialog = (EditText) dialog_auth.findViewById(R.id.et_phone_number_dialog);

                                LinearLayout email_layout = (LinearLayout) dialog_auth.findViewById(R.id.email_layout);
                                LinearLayout phone_number_layout = (LinearLayout) dialog_auth.findViewById(R.id.phone_number_layout);

                                switch_button1 = (Switch) dialog_auth.findViewById(R.id.switch_button);
                                switch_button1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked) {
//                                            Toast.makeText(MainActivity_Register_OTPbased.this, "checked", Toast.LENGTH_LONG).show();
                                            phone_number_layout.setVisibility(View.GONE);
                                            email_layout.setVisibility(View.VISIBLE);
                                        }else {
//                                            Toast.makeText(MainActivity_Register_OTPbased.this, "not checked", Toast.LENGTH_LONG).show();
                                            phone_number_layout.setVisibility(View.VISIBLE);
                                            email_layout.setVisibility(View.GONE);
                                        }
                                    }
                                });

                                btnlogin = dialog_auth.findViewById(R.id.login1);
                                btnlogin.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        CountryCodePicker countryCode_picker = (CountryCodePicker) dialog_auth.findViewById(R.id.countryCode_picker);

                                        String country_name = countryCode_picker.getSelectedCountryName();
                                        String name_code = countryCode_picker.getSelectedCountryNameCode();
                                        name_exte_dialog = countryCode_picker.getSelectedCountryCodeWithPlus();



                                        SharedPreferences pref = getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
                                        SharedPreferences.Editor editor = pref.edit();
                                        if (selection.toString().equals("Dine")) {
                                            editor.putString("account_selection", "Dine");
                                            System.out.println("selection is " + selection + " IVEPOS");
                                        } else {
                                            if (selection.toString().equals("Qsr")) {
                                                editor.putString("account_selection", "Qsr");
                                                System.out.println("selection is " + selection + " IVEPOSQSR");
                                            } else {
                                                editor.putString("account_selection", "Retail");
                                                System.out.println("selection is " + selection + " IVEPOSRETAIL");
                                            }
                                        }
                                        editor.commit();

                                        if (switch_button1.isChecked()) {
                                            if (editTextUserId.getText().toString().equalsIgnoreCase("")) {
                                                editTextUserId.setError("Please enter Email/Username");
                                            } else {
                                                login3();
                                            }
                                        }else {
                                            if (et_phone_number_dialog.getText().toString().equalsIgnoreCase("")) {
                                                et_phone_number_dialog.setError("Please enter Phone number");
                                            } else {
                                                if (country_name.toString().equalsIgnoreCase("India")) {
                                                    if (et_phone_number_dialog.getText().toString().length() != 10) {
                                                        et_phone_number_dialog.setError("Enter valid number");
                                                    }else {
                                                        login3();
                                                    }
                                                }else {
                                                    if (country_name.toString().equalsIgnoreCase("Singapore")) {
                                                        if (et_phone_number_dialog.getText().toString().length() != 8) {
                                                            et_phone_number_dialog.setError("Enter valid number");
                                                        }else {
                                                            login3();
                                                        }
                                                    }else {
                                                        if (country_name.toString().equalsIgnoreCase("Malaysia")) {
                                                            if (et_phone_number_dialog.getText().toString().length() != 9) {
                                                                et_phone_number_dialog.setError("Enter valid number");
                                                            }else {
                                                                login3();
                                                            }
                                                        }else {
                                                            login3();
                                                        }
                                                    }
                                                }

                                            }
                                        }


                                    }
                                });
                            } else {
                                if (id == R.id.linearLayout_device) {
                                    dialog_auth = new Dialog(R_NewLogIn_BusinessType.this, R.style.notitle);
                                    dialog_auth.setContentView(R.layout.signin_dialog_otp);
                                    dialog_auth.setCanceledOnTouchOutside(false);
                                    dialog_auth.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                    dialog_auth.show();

                                    editTextUserId = dialog_auth.findViewById(R.id.editTextUserId);

                                    et_phone_number_dialog = (EditText) dialog_auth.findViewById(R.id.et_phone_number_dialog);

                                    LinearLayout email_layout = (LinearLayout) dialog_auth.findViewById(R.id.email_layout);
                                    LinearLayout phone_number_layout = (LinearLayout) dialog_auth.findViewById(R.id.phone_number_layout);

                                    switch_button1 = (Switch) dialog_auth.findViewById(R.id.switch_button);
                                    switch_button1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
//                                                Toast.makeText(MainActivity_Register_OTPbased.this, "checked", Toast.LENGTH_LONG).show();
                                                phone_number_layout.setVisibility(View.GONE);
                                                email_layout.setVisibility(View.VISIBLE);
                                            }else {
//                                                Toast.makeText(MainActivity_Register_OTPbased.this, "not checked", Toast.LENGTH_LONG).show();
                                                phone_number_layout.setVisibility(View.VISIBLE);
                                                email_layout.setVisibility(View.GONE);
                                            }
                                        }
                                    });

                                    btnlogin = dialog_auth.findViewById(R.id.login1);
                                    btnlogin.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            CountryCodePicker countryCode_picker = (CountryCodePicker) dialog_auth.findViewById(R.id.countryCode_picker);

                                            String country_name = countryCode_picker.getSelectedCountryName();
                                            String name_code = countryCode_picker.getSelectedCountryNameCode();
                                            name_exte_dialog = countryCode_picker.getSelectedCountryCodeWithPlus();

                                            SharedPreferences pref = getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
                                            SharedPreferences.Editor editor = pref.edit();
                                            if (selection.toString().equals("Dine")) {
                                                editor.putString("account_selection", "Dine");
                                                System.out.println("selection is " + selection + " IVEPOS");
                                            } else {
                                                if (selection.toString().equals("Qsr")) {
                                                    editor.putString("account_selection", "Qsr");
                                                    System.out.println("selection is " + selection + " IVEPOSQSR");
                                                } else {
                                                    editor.putString("account_selection", "Retail");
                                                    System.out.println("selection is " + selection + " IVEPOSRETAIL");
                                                }
                                            }
                                            editor.commit();

                                            if (switch_button1.isChecked()) {
                                                if (editTextUserId.getText().toString().equalsIgnoreCase("")) {
                                                    editTextUserId.setError("Please enter Email/Username");
                                                } else {
                                                    loginDevice();
                                                }
                                            }else {
                                                if (et_phone_number_dialog.getText().toString().equalsIgnoreCase("")) {
                                                    et_phone_number_dialog.setError("Please enter Phone number");
                                                } else {
                                                    if (country_name.toString().equalsIgnoreCase("India")) {
                                                        if (et_phone_number_dialog.getText().toString().length() != 10) {
                                                            et_phone_number_dialog.setError("Enter valid number");
                                                        }else {
                                                            loginDevice();
                                                        }
                                                    }else {
                                                        if (country_name.toString().equalsIgnoreCase("Singapore")) {
                                                            if (et_phone_number_dialog.getText().toString().length() != 8) {
                                                                et_phone_number_dialog.setError("Enter valid number");
                                                            }else {
                                                                loginDevice();
                                                            }
                                                        }else {
                                                            if (country_name.toString().equalsIgnoreCase("Malaysia")) {
                                                                if (et_phone_number_dialog.getText().toString().length() != 9) {
                                                                    et_phone_number_dialog.setError("Enter valid number");
                                                                }else {
                                                                    loginDevice();
                                                                }
                                                            }else {
                                                                loginDevice();
                                                            }
                                                        }
                                                    }

                                                }
                                            }

//                                            if (editTextUserId.getText().toString().equalsIgnoreCase("")) {
//                                                editTextUserId.setError("Please enter the username");
//                                            } else {
//                                                loginDevice();
//                                            }

                                        }
                                    });
                                }

                            }
                        }

                        return true;
                    }
                });
            }
        });

   //******************************************************store create codes


    }
    public void login3() {
        progressBar = (RelativeLayout) dialog_auth.findViewById(R.id.progressbar1);
        progressBar.setVisibility(View.VISIBLE);
//        email=editTextUserId.getText().toString();


        if (switch_button1.isChecked()) {
            email = editTextUserId.getText().toString();
        }else {
            email = name_exte_dialog+""+et_phone_number_dialog.getText().toString();
        }

//        System.out.println("Selected switch "+email);

        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"login_otp.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("response",response.toString());
                        JSONObject jsonObject= null;
                        String status="",company="",email="",password="";
                        try {
                            jsonObject = new JSONObject(response);
                            status=jsonObject.getString("status");
                            company=jsonObject.getString("company");
                            email=jsonObject.getString("email");
                            password=jsonObject.getString("password");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(R_NewLogIn_BusinessType.this, "Enter Valid Email", Toast.LENGTH_LONG).show();
                        }
                        if(status.equalsIgnoreCase("success")){
                            progressBar.setVisibility(View.GONE);
                            dialog_auth.dismiss();
                            Intent intent=new Intent(R_NewLogIn_BusinessType.this,NewStoreActivity_OTP.class);
                            intent.putExtra("company",company);
                            intent.putExtra("email",email);
                            intent.putExtra("password",password);
                            startActivity(intent);
                        }else{
                            progressBar.setVisibility(View.GONE);
                            //   dialog_auth.dismiss();
                            showDialogMessage1("signup failed","signup failed");
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
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", email + "");
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);


    }


    public void loginDevice() {
        progressBar = (RelativeLayout) dialog_auth.findViewById(R.id.progressbar1);
        progressBar.setVisibility(View.VISIBLE);
//        email=editTextUserId.getText().toString();

        if (switch_button1.isChecked()) {
            email = editTextUserId.getText().toString();
        }else {
            email = name_exte_dialog+""+et_phone_number_dialog.getText().toString();
        }

//        System.out.println("Selected switch "+email);

        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"login_otp.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("response",response.toString());
                        JSONObject jsonObject= null;
                        String status="",company="",email="";
                        String[] stores = new String[0];
                        try {
                            jsonObject = new JSONObject(response);
                            status=jsonObject.getString("status");
                            if(status.equalsIgnoreCase("success")){
                                company=jsonObject.getString("company");
                                email=jsonObject.getString("email");
                                password=jsonObject.getString("password");

                                JSONArray jsonArray= jsonObject.getJSONArray("store");
                                stores=new String[jsonArray.length()];
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject store= (JSONObject) jsonArray.get(i);
                                    String store_name=store.getString("store_name");
                                    stores[i]=store_name;
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(R_NewLogIn_BusinessType.this, "Signup failed", Toast.LENGTH_LONG).show();
                        }
                        if(status.equalsIgnoreCase("success")){
                            progressBar.setVisibility(View.GONE);
                            dialog_auth.dismiss();
                            Intent intent=new Intent(R_NewLogIn_BusinessType.this,NewDeviceActivity.class);
                            intent.putExtra("company",company);
                            intent.putExtra("email",email);
                            intent.putExtra("password",password);
                            intent.putExtra("stores",stores);
                            startActivity(intent);
                        }else{
                            progressBar.setVisibility(View.GONE);
                            //   dialog_auth.dismiss();
                            showDialogMessage1("signup failed","signup failed");
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
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", email + "");
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);


    }
    private void showDialogMessage1(String title, String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    //  dialog_auth.dismiss();

                } catch (Exception e) {
                    //exit();
                }
            }
        });

    }
    private void initApp() {
        inUsername = (EditText) findViewById(R.id.editTextUserId);
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);

    }
    private void login(){
        if (inUsername==null || et_phone_number == null){
           Toast.makeText(R_NewLogIn_BusinessType.this,"Error no data",Toast.LENGTH_LONG).show();
        }
    }


    public void loginmethod() {
        tv = new TextView(R_NewLogIn_BusinessType.this);
        tv.setText(selection);
        if (tv.getText().toString().equals("")) {
            Toast.makeText(R_NewLogIn_BusinessType.this, "Select business type", Toast.LENGTH_LONG).show();
        }else{
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

    public void loginWebservice() {

        if (ContextCompat.checkSelfPermission(R_NewLogIn_BusinessType.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(R_NewLogIn_BusinessType.this,
                    permissions(),
                    3);
            /*if (ActivityCompat.shouldShowRequestPermissionRationale(R_NewLogIn_BusinessType.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(R_NewLogIn_BusinessType.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        3);
            } else {
                ActivityCompat.requestPermissions(R_NewLogIn_BusinessType.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        3);
            }*/
        } else {
//            loginWebservice1();
            if (ContextCompat.checkSelfPermission(R_NewLogIn_BusinessType.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(R_NewLogIn_BusinessType.this,
                        permissions(),
                        3);
                /*if (ActivityCompat.shouldShowRequestPermissionRationale(R_NewLogIn_BusinessType.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(R_NewLogIn_BusinessType.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            3);
                } else {
                    ActivityCompat.requestPermissions(R_NewLogIn_BusinessType.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            3);
                }*/
            } else {
                loginWebservice1();

            }

        }

    }

    public void loginWebservice1() {
        //        progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
//        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(R_NewLogIn_BusinessType.this);
        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"login_caps_otp.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        System.out.println("email signin "+responseString);
                        String response= "";
                         jsonObject=null;
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
                System.out.println("email signin "+email_signin);
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

    public void speedTestTask() {

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



    public void parseJson(JSONObject jarray) {
        String company = "", email = "", password = "", selection1 = "", company_special = "";

        try {
            company = jarray.getString("company");
            email = jarray.getString("email");
            password = jarray.getString("password");
            company_special = jarray.getString("company_special");
//            selection1=jarray.getString("account_selection");
            JSONArray stores = jarray.getJSONArray("store");

            for (int i = 0; i < stores.length(); i++) {

                StoreBean storeBean = new StoreBean();
                JSONObject storeobject = stores.getJSONObject(i);
                String store_name = storeobject.getString("store_name");
                System.out.println(store_name);
                JSONArray devices = storeobject.getJSONArray("devices");
                System.out.println(devices);
                String[] deviceArray = new String[devices.length()];
                for (int j = 0; j < devices.length(); j++) {
                    deviceArray[j] = devices.getString(j);
                }

                storeBean.setStoreName(store_name);
                storeBean.setDevices(deviceArray);
                storeList.add(storeBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences pref = getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("emailid", email);
        editor.putString("companyname", company);
        editor.putString("password", password);
        editor.putString("companyname_special", company_special);

        System.out.println("list is " + email + " " + company + " " + password + " " + company_special);

//        if (selection.toString().equals("Dine")) {
//            editor.putString("account_selection", "Dine");
//            System.out.println("selection is " + selection + " IVEPOS");
//        } else  {
//            if (selection.toString().equals("Qsr")) {
//                editor.putString("account_selection", "Qsr");
//                System.out.println("selection is " + selection + " IVEPOSQSR");
//            } else {
//                editor.putString("account_selection", "Retail");
//                System.out.println("selection is " + selection + " IVEPOSRETAIL");
//            }
//        }
        TextView tv = new TextView(R_NewLogIn_BusinessType.this);
        tv.setText(selection);
        if (tv.getText().toString().equals("")) {
            Toast.makeText(R_NewLogIn_BusinessType.this, "Select business type", Toast.LENGTH_LONG).show();
        }else{
                    if (selection.toString().equals("Dine")) {
            editor.putString("account_selection", "Dine");
            System.out.println("selection is " + selection + " IVEPOS");
        } else  {
            if (selection.toString().equals("Qsr")) {
                editor.putString("account_selection", "Qsr");
                System.out.println("selection is " + selection + " IVEPOSQSR");
            } else {
                editor.putString("account_selection", "Retail");
                System.out.println("selection is " + selection + " IVEPOSRETAIL");
            }
        }
        }

//        if (selection.toString().equals("Dine")){
//            editor.putString("account_selection", "Dine");
//            System.out.println("selection is " + selection + " IVEPOS");
//        }else if (selection.toString().equals("Qsr")){
//            editor.putString("account_selection", "Qsr");
//            System.out.println("selection is " + selection + " IVEPOSQSR");
//        } else if (selection.toString().equals("Retail")) {
//            editor.putString("account_selection", "Retail");
//            System.out.println("selection is " + selection + " IVEPOSRETAIL");
//        }else {
//            Toast.makeText(R_NewLogIn_BusinessType.this,"Please Select Your business Type",Toast.LENGTH_LONG).show();
//        }

//        if (selection.toString().equals("Dine")){
//            editor.putString("account_selection", "Dine");
//            System.out.println("selection is " + selection + " IVEPOS");
//        }else {
//            if (selection.toString().equals("Qsr")){
//                editor.putString("account_selection", "Qsr");
//                System.out.println("selection is " + selection + " IVEPOSQSR");
//            } else if (selection.toString().equals("retail")) {
//                editor.putString("account_selection", "Retail");
//                System.out.println("selection is " + selection + " IVEPOSRETAIL");
//            }else {
//                Toast.makeText(R_NewLogIn_BusinessType.this,"Please Select Your business Type",Toast.LENGTH_LONG).show();
//            }
//        }


//        editor.commit();

//        SharedPreferences pref1 = getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
//        SharedPreferences.Editor editor1 = pref1.edit();
//        editor.putString("account_selection", selection);
        editor.commit();

        textView2 = (TextView) findViewById(R.id.seconds);
        textView2.setText("");

        prefs = PreferenceManager.getDefaultSharedPreferences(R_NewLogIn_BusinessType.this);
        SharedPreferences.Editor editor1 = prefs.edit();

        String ipAddress = getLocalIpAddress();
        String ipAddress_stored = prefs.getString("ip_address", "");
//        Log.d("ip addressed shared", ipAddress_stored);
        Integer ipCount = prefs.getInt("ip_count", -1);

        if (ipAddress_stored.equals("") || ipCount.equals("-1")) {
            editor1.putInt("ip_count", 1);
            editor1.putString("ip_address", getLocalIpAddress());
            editor1.apply();
        }

//        Log.d("ip count", String.valueOf(ipCount));

        if (ipCount > 3) {
//            Log.d("Count exceeded", ipCount.toString());
            button.setEnabled(false);
            button.setBackgroundResource(R.drawable.dark_black_rounded_corners);

            new CountDownTimer(30000, 1000) {

                public void onTick(long millisUntilFinished) {
                    textView2.setText("Request OTP after " + millisUntilFinished / 1000 + " seconds");
                    // logic to set the EditText could go here
                }

                public void onFinish() {
                    textView2.setText("");
                    button.setBackgroundResource(R.drawable.r_new_login_button2);
                    button.setEnabled(true);
                }

            }.start();
        }
//        textView.setText(ipCount.toString());
        if (ipAddress.equals(ipAddress_stored)) {
//            Log.d("Ip match", ipAddress_stored);
            Integer ipCount_increase = ipCount + 1;
            editor1.putInt("ip_count", ipCount_increase);
            editor1.putString("ip_address", ipAddress);
            editor1.apply();
//            textView.setText(ipCount_increase.toString());
        }
        final int selected1 = radioGroup1.getCheckedRadioButtonId();
        radioBtnsplit = (RadioButton) findViewById(selected1);

//        if (selected1 != -1) {
            loadExistingOldCustomerStatusData(email_signin, company, email);
//            Log.d("ExistingUser", customerStatus);
    //    }
//        else {
//            loadExistingOldCustomerStatusData(email_signin, company, email);
////            signInWithPhoneNumber(email_signin, company, email);
//        }

//        Intent intent=new Intent(MainActivity_Signin_OTPbased.this,Checking_Store.class);
//        intent.putExtra("company",company);
//        intent.putExtra("email",email);
//        startActivity(intent);
    }
    @SuppressLint("MissingSuperCall")
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
                    //Toast.makeText(R_NewLogIn_BusinessType.this, "permission granted", Toast.LENGTH_SHORT).show();
                    System.out.println("permission granted");
                    if (!SdIsPresent()) ;


                } else {

                    Toast.makeText(R_NewLogIn_BusinessType.this, "permission denied", Toast.LENGTH_SHORT).show();

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
                    RequestQueue queue = Volley.newRequestQueue(R_NewLogIn_BusinessType.this);
                    StringRequest sr = new StringRequest(
                            com.android.volley.Request.Method.POST,
                            WebserviceUrl+"login_caps_otp.php",
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

                                        progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
                                        progressBar.setVisibility(View.GONE);
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
    public  void test_email_company_signin() {
        requestQueue = Volley.newRequestQueue(R_NewLogIn_BusinessType.this);
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
                               // showDialogMessage(getString(R.string.sdm6));
                                Toast.makeText(R_NewLogIn_BusinessType.this,"Not Registered want to Signup??",Toast.LENGTH_LONG).show();
//                                Intent regi = new Intent(R_NewLogIn_BusinessType.this, MainActivity_Register_OTPbased.class);
//                                regi.putExtra("phone_ph", email_signin);
//                                startActivity(regi);
                                if (status.equals("email")){
                                    Toast.makeText(R_NewLogIn_BusinessType.this,"Please Signup with your phone no",Toast.LENGTH_LONG).show();
                                }else {
                                 //   Intent intent = new Intent(R_NewLogIn_BusinessType.this, MainActivity_Register_OTPbased.class);
                                 //   intent.putExtra("phone_ph", email_signin);
                                 //   startActivity(intent);
                                   // signup();
                                   // test_email_company();
                                    createaccount();
                                }


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
    public void onBackPressed() {
        // Navigate back to the main activity
        Intent intent = new Intent(this, MainActivity_Signin_OTPbased.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
    private void loadExistingOldCustomerStatusData(String email_signin, String company, String email) {

//        Log.d("Data", email);

        RequestQueue requestQueue = Volley.newRequestQueue(R_NewLogIn_BusinessType.this);
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
                            Toast.makeText(R_NewLogIn_BusinessType.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
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
            R_NewLogIn_BusinessType.this.customerStatus = responseObjJSONObject.getString("existing_user_status");
            R_NewLogIn_BusinessType.this.customerEmailId = responseObjJSONObject.getString("email");
//            Log.d("ExistingUserInside", customerStatus);

            if (customerStatus.equals("true")) {
                Amplify.Auth.signIn(
                        R_NewLogIn_BusinessType.this.email_signin,
                        "ivepos@123",
                        result -> {
                            Intent intent = new Intent(R_NewLogIn_BusinessType.this, SignIn_OTP_Verify_Activity.class);
                            intent.putExtra("company",company);
                            intent.putExtra("email",email);
                            intent.putExtra("emailresend",email_signin);
                            intent.putExtra("account_selection", selection);
                            intent.putExtra("status",status);
                            intent.putExtra("customerEmailId",customerEmailId);
                            intent.putExtra(FINAL_DATA, email_signin);
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

                if (status.toString().equalsIgnoreCase("email")) {
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
    private void showDialogMessage(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(R_NewLogIn_BusinessType.this).create();

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
    private void updateExistingCustomerStatus(String email, String bool, String company) {
//        Log.d("Data", email + "true");
//        this.requestQueue = RequestSingleton.getInstance(requireActivity()).getInstance();
        RequestQueue requestQueue = Volley.newRequestQueue(R_NewLogIn_BusinessType.this);
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
                                final int selected1 = radioGroup1.getCheckedRadioButtonId();
                                radioBtnsplit = (RadioButton) findViewById(selected1);
                                if (selected1 != -1) {
                                    loadExistingOldCustomerStatusData(email_signin, company, email);
//                                    Log.d("ExistingUser", customerStatus);
                                } else {
                                    loadExistingOldCustomerStatusData(email_signin, company, email);
//                                    signInWithPhoneNumber(email_signin, company, email);

                                }
                            }

                        } else {
                            Toast.makeText(R_NewLogIn_BusinessType.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
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
                                    Intent intent = new Intent(R_NewLogIn_BusinessType.this, SignIn_OTP_Verify_Activity.class);
                                    intent.putExtra("company",company);
                                    intent.putExtra("email",email);
                                    intent.putExtra("account_selection", selection);
                                    intent.putExtra(FINAL_DATA, email_signin);
                                    intent.putExtra("status",status);
                                    intent.putExtra("json",jsonObject.toString());
                                    intent.putExtra("emailresend",email_signin);
                                    intent.putExtra(BASE_URL_LINK, WebserviceUrl);
                                    intent.putExtra(CUSTOMER_STATUS, customerStatus);
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
                        R_NewLogIn_BusinessType.this.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(R_NewLogIn_BusinessType.this).create();

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
                                    Intent intent = new Intent(R_NewLogIn_BusinessType.this, SignIn_OTP_Verify_Activity.class);
                                    intent.putExtra("company",company);
                                    intent.putExtra("email",email);
                                    intent.putExtra("status",status);
                                    intent.putExtra("json",jsonObject.toString());
                                    intent.putExtra("account_selection", selection);
                                    intent.putExtra("emailresend",email_signin);
                                    intent.putExtra(FINAL_DATA, finalData);
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
                        R_NewLogIn_BusinessType.this.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(R_NewLogIn_BusinessType.this).create();

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

    public  void test_email_company() {
        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"test_email_company_otpbased.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("response123",response);
                        if(response.equalsIgnoreCase("phone exists")){
                            Log.d("response123","phone exists");
                            showDialogMessage(getString(R.string.sdm5));
                        }else{
                            if (response.equalsIgnoreCase("phone exists another")){
                                Log.d("response123","phone exists another");
                                //Signup without OTP
                                Toast.makeText(R_NewLogIn_BusinessType.this, "Proceed without OTP", Toast.LENGTH_LONG).show();
                                register();
                              //  createaccount();
                              //  signup();
                                progressBar.setVisibility(View.VISIBLE);

//                                Intent intent = new Intent(MainActivity_Register_OTPbased.this, SignUpConfirmActivity_OTPbased.class);
//                                intent.putExtra("phone_number", phone_number);
//                                intent.putExtra("storename", store);
//                                intent.putExtra("devicename", device);
//                                intent.putExtra("source", "signup");
//                                intent.putExtra("companyname", phone_number+""+selection);
//                                intent.putExtra("companyname_special", phone_number+""+selection);
//                                intent.putExtra("account_selection", selection);
//                                intent.putExtra("country_code", name_exte);
//                                startActivity(intent);
//
//                                hideKeyboard(MainActivity_Register_OTPbased.this);
//                                donotshowKeyboard(MainActivity_Register_OTPbased.this);
                            }else {
                                if (response.equalsIgnoreCase("phone not exists")){
                                    Log.d("response123","phone not exists");
                                    signup();
                                    progressBar.setVisibility(View.VISIBLE);
                                }
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
//                Log.d("phonenumber",name_exte + ""+phone_number);
                params.put("phonenumber", name_exte + ""+phone_number + "");
                params.put("selection", selection + "");
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);
    }



    public void createaccount(){
        TextView tv = new TextView(R_NewLogIn_BusinessType.this);
        tv.setText(selection);
        if (tv.getText().toString().equals("")) {
            Toast.makeText(R_NewLogIn_BusinessType.this, "Select business type", Toast.LENGTH_LONG).show();
        }else {

            if (isDeviceOnline() == true) {


                System.out.println("user name.....1");
//                if (switch_button.isChecked()) {
//                    phone_number = inUsername.getText().toString();
//                    if (inUsername.getText().toString().contains("@")) {
//                        String match = "@";
//                        int position1 = inUsername.getText().toString().indexOf(match);
//                        phone_number1 = inUsername.getText().toString().substring(0, position1);
//                    }else {
//                        phone_number1 = et_phone_number.getText().toString();
//                    }
//                }else {
                   // phone_number = email_signin;
                   // phone_number1 = email_signin;
       //         }

                et_store = new TextView(R_NewLogIn_BusinessType.this);
                et_store.setText("store1");
//        et_device= (EditText) findViewById(R.id.et_device);
                et_device = new TextView(R_NewLogIn_BusinessType.this);
                et_device.setText("device1");

                store = et_store.getText().toString();
                device = et_device.getText().toString();
                boolean invalid = false;


                if (status.equals("email")) {
                    if (phone_number.equals("") || phone_number.length() == 0) {
                        invalid = true;
                        inUsername.setError("Enter Your E-Mail ID");
                    }else if (invalid == false) {

//                        signup();

//                            System.out.println("text is " + company.replaceAll("[^A-Za-z0-9]", ""));
//                            company = company.replaceAll("[^A-Za-z0-9]", "");
//                            company = company.toLowerCase();

                   //     CountryCodePicker countryCode_picker = (CountryCodePicker) findViewById(R.id.countryCode_picker);

                    //    country_name = countryCode_picker.getSelectedCountryName();
                        country_name = countrynameget;
                   //     String name_code = countryCode_picker.getSelectedCountryNameCode();
                     //   name_exte = countryCode_picker.getSelectedCountryCodeWithPlus();
                        name_exte = countrycodeget;

                        country_name = "";
                        name_exte = "";

                        finalData = name_exte + phone_number;
                        store = store.toLowerCase();
                        device = device.toLowerCase();
                        test_email_company();

                    }
                }else {
                    if (phone_number.equals("") || phone_number.length() == 0) {
                        invalid = true;
                      //  et_phone_number.setError("Enter Your Phone number");
                        Toast.makeText(R_NewLogIn_BusinessType.this,"Enter valid number ",Toast.LENGTH_LONG).show();
                    }else if (invalid == false) {

//                        signup();

//                            System.out.println("text is " + company.replaceAll("[^A-Za-z0-9]", ""));
//                            company = company.replaceAll("[^A-Za-z0-9]", "");
//                            company = company.toLowerCase();

                   //     CountryCodePicker countryCode_picker = (CountryCodePicker) findViewById(R.id.countryCode_picker);



                  //      country_name = countryCode_picker.getSelectedCountryName();
                          country_name=countrynameget;
                   //     String name_code = countryCode_picker.getSelectedCountryNameCode();
                 //       name_exte = countryCode_picker.getSelectedCountryCodeWithPlus();
                        name_exte=  countrycodeget;

                        if (name_exte.toString().contains("+880")) {
                     //       et_phone_number.setError("Contact www.ivepos.com for more details");
                            Toast.makeText(R_NewLogIn_BusinessType.this,"Contact to www.ivepos.com",Toast.LENGTH_LONG).show();
                        }else {
                            if (country_name.toString().equalsIgnoreCase("India")) {
                                if (phone_number.length() != 10) {
                                 //   et_phone_number.setError("Enter valid number");
                                    Toast.makeText(R_NewLogIn_BusinessType.this,"Enter valid number",Toast.LENGTH_LONG).show();
                                } else {
                                    finalData = name_exte + phone_number;
                                    store = store.toLowerCase();
                                    device = device.toLowerCase();
                                    test_email_company();
                                }
                            } else {
                                if (country_name.toString().equalsIgnoreCase("Singapore")) {
                                    if (phone_number.length() != 8) {
                                    //    et_phone_number.setError("Enter valid number");
                                        Toast.makeText(R_NewLogIn_BusinessType.this,"Enter valid number",Toast.LENGTH_LONG).show();
                                    } else {
                                        finalData = name_exte + phone_number;
                                        store = store.toLowerCase();
                                        device = device.toLowerCase();
                                        test_email_company();
                                    }
                                } else {
                                    if (country_name.toString().equalsIgnoreCase("Malaysia")) {
                                        if (phone_number.length() != 9) {
                                         //   et_phone_number.setError("Enter valid number");
                                            Toast.makeText(R_NewLogIn_BusinessType.this,"Enter valid number",Toast.LENGTH_LONG).show();
                                        } else {
                                            finalData = name_exte + phone_number;
                                            store = store.toLowerCase();
                                            device = device.toLowerCase();
                                            test_email_company();
                                        }
                                    } else {
                                        finalData = name_exte + phone_number;
                                        store = store.toLowerCase();
                                        device = device.toLowerCase();
                                        test_email_company();
                                    }
                                }
                            }
                        }




                    }
                }

            } else {
               // showAlert();
                Toast.makeText(R_NewLogIn_BusinessType.this,"Something Wrong",Toast.LENGTH_LONG).show();
            }

        }


    }


    public void updateUi() {
//        if (mSubscribedToDelaroy) {

        progressBar.setVisibility(View.GONE);

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
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"pospurchase.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")){



                            TextView from1 = new TextView(R_NewLogIn_BusinessType.this);
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
                            TextView from1 = new TextView(R_NewLogIn_BusinessType.this);
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
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put("company", phone_number1+""+selection1);
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


    private void update() {

//        if (i == 5){
//            prolicense("");
//        }else {
        poslicense(checking);
//        }
    }

    private void register() {
//        progressBar1 = (RelativeLayout) findViewById(R.id.progressbar1);
//        progressBar1.setVisibility(View.VISIBLE);

        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"newcomp_phon_spec_ema_otp.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        System.out.println("newcompany_phone_speci_email "+response);
                        if(response.equalsIgnoreCase("success")){
//                            Log.d("response",response);



                            Intent intent = new Intent(R_NewLogIn_BusinessType.this, DownloadService.class);
                            // add infos for the service which file to download and where to store
                            intent.putExtra("company", phone_number1+""+selection1);
                            intent.putExtra("store", store);
                            intent.putExtra("device", device);
                            intent.putExtra("emailid", name_exte + ""+phone_number);
                            startService(intent);

//                            Log.d("Company",phone_number1+""+selection1);
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
                            editor.putString("companyname", phone_number1+""+selection1); // Storing float// Storing long
                            editor.putString("companyname_special", company_special+""+selection1); // Storing float// Storing long
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

//                            Intent i = new Intent(SignUpConfirmActivity_OTPbased.this, MainActivity_subscription.class);
//                            i.putExtra("emailid", emailid);
//                            i.putExtra("storename", store);
//                            i.putExtra("devicename", device);
//                            i.putExtra("companyname", company);
//                            i.putExtra("from", "register");
//                            startActivity(i);






                            //   createappdata();
                        }else{
                            showDialogMessage("signup failed");
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
                params.put("email", name_exte + ""+phone_number + "");
                params.put("phone_number", phone_number + "");
                params.put("password", "ivepos@123" + "");
                params.put("company", phone_number1+""+selection1);
                params.put("company_special", phone_number1+""+selection1);
                params.put("store", store + "");
                params.put("device", device + "");
                params.put("country_code", name_exte + "");
                params.put("username", phone_number);
                params.put("country", country_name);
                System.out.println("newcompany_phone_speci_email1 "+email+" "+phone_number+" "+password+" "+company+" "+company_special+" "+store+" "+device);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);



    }




    public void signup() {
        textView2 = (TextView) findViewById(R.id.seconds);
        String ipAddress = getLocalIpAddress();
        String ipAddress_stored = prefs.getString("ip_address", "");
//        Log.d("ip addressed shared", ipAddress_stored);
        Integer ipCount  = prefs.getInt("ip_count",-1);

        if(ipAddress_stored.equals("") || ipCount.equals("-1")){
            editor.putInt("ip_count", 1);
            editor.putString("ip_address", getLocalIpAddress());
            editor.apply();
        }

//        Log.d("ip count", String.valueOf(ipCount));

        if(ipCount > 3){
//            Log.d("Count exceeded", ipCount.toString());
         //   tv_signUp.setEnabled(false);
          //  tv_signUp.setBackgroundResource(R.drawable.dark_black_rounded_corners);

            new CountDownTimer(30000, 1000) {

                public void onTick(long millisUntilFinished) {
                    textView2.setText("Request OTP after " + millisUntilFinished / 1000+" seconds");
                    // logic to set the EditText could go here
                }

                public void onFinish() {
                    textView2.setText("");
                //    tv_signUp.setBackgroundResource(R.drawable.red_click_shape_rounded_corners);
                  //  tv_signUp.setEnabled(true);
                }

            }.start();
        }
//        textView.setText(ipCount.toString());
        if(ipAddress.equals(ipAddress_stored)){
//            Log.d("Ip match", ipAddress_stored);
            Integer ipCount_increase = ipCount + 1;
            editor.putInt("ip_count", ipCount_increase);
            editor.putString("ip_address", ipAddress);
            editor.apply();
//            textView.setText(ipCount_increase.toString());
        }

        ArrayList<AuthUserAttribute> attributes = new ArrayList<>();
//                attributes.add(new AuthUserAttribute(AuthUserAttributeKey.name(), finalData));
//        if (switch_button.isChecked()) {
//            attributes.add(new AuthUserAttribute(AuthUserAttributeKey.email(), finalData));
//        }else {
            attributes.add(new AuthUserAttribute(AuthUserAttributeKey.phoneNumber(), finalData));
 //       }
//        attributes.add(new AuthUserAttribute(AuthUserAttributeKey.phoneNumber(), finalData));//changed while implementing both email and phone based OTP
//                attributes.add(new AuthUserAttribute(AuthUserAttributeKey.phoneNumber(), "+919670786762"));
//                attributes.add(new AuthUserAttribute(AuthUserAttributeKey.email(), "akshay21sep@gmail.com"));
//                attributes.add(new AuthUserAttribute(AuthUserAttributeKey.email(), "akshaydinein@gmail.com"));
        attributes.add(new AuthUserAttribute(AuthUserAttributeKey.custom("custom:ip_address"), ipAddress)); // RestaurantRetailNewOTPUserPool

        Amplify.Auth.signUp(
                        /*"+919670786762",
                        "Ak@12345",*/
                finalData,
//                        "akshay21sep@gmail.com",
//                        "akshaydinein@gmail.com",
                "ivepos@123",
                AuthSignUpOptions.builder().userAttributes(attributes).build(),
                result -> {
//                    Log.i("AuthQuickstart1.11", result.toString());
                    if (result.isSignUpComplete()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(R_NewLogIn_BusinessType.this, "OTP sent on " + finalData, Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(R_NewLogIn_BusinessType.this, SignUpConfirmActivity_OTPbased.class);
                                intent.putExtra("phone_number", phone_number);
                                intent.putExtra("storename", store);
                                intent.putExtra("devicename", device);
                                intent.putExtra("source", "signup");
                                intent.putExtra("companyname", phone_number1+""+selection1);
                                intent.putExtra("companyname_special", phone_number1+""+selection1);
                                intent.putExtra("account_selection", selection);
                                intent.putExtra("country_code", name_exte);
                                intent.putExtra("country", country_name);
                                startActivity(intent);

                              //  hideKeyboard(R_NewLogIn_BusinessType.this);
                              //  donotshowKeyboard(R_NewLogIn_BusinessType.this);
                            }
                        });
//                        Intent intent = new Intent(MainActivity.this, SignUp_OTP_Verify_Activity.class);
//                        intent.putExtra(FINAL_DATA, finalData);
//                        intent.putExtra(USER_PHONE_NUMBER, phoneNo);
//                        startActivity(intent);
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
                                AlertDialog alertDialog = new AlertDialog.Builder(R_NewLogIn_BusinessType.this).create();

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


}