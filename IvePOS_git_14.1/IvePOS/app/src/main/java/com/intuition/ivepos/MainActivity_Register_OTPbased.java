package com.intuition.ivepos;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.AmplifyConfiguration;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hbb20.CountryCodePicker;
import com.intuition.ivepos.signup.DownloadService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.regex.Pattern;


public class MainActivity_Register_OTPbased extends AppCompatActivity {
    String TAG = "SignUp";
    EditText et_phone_number, et_email,et_password, et_confirmpassword, inUsername;
    TextView et_store,et_device;
    TextView tv_passwordmsg;
    String company, store, device,phone_number, phone_number1, email,password,confpassword, company_special;
    TextView tv_signUp;
    AlertDialog userDialog;
    Context c = this;
    SQLiteDatabase db;
    SQLiteDatabase db_appda = null;
    DatabaseHelper1 databaseHelper;
    Charity registeredData;
    int i = 0;
    RequestQueue requestQueue;

    RadioGroup radioGroupSplit;
    RadioButton radioBtnsplit, radio_dine, radio_qsr, radio_retail;
    LinearLayout layout_dine, layout_qsr, layout_retail;
    RadioGroup radioGroup1;
    String selection, selection1;

    String WebserviceUrl;


    Dialog dialog_auth;;
    EditText editTextUserId,editTextPassword;
    Button btnlogin;
    RelativeLayout progressBar;

    private String finalData = "";
    private int configResourceId;
    String name_exte, country_name;

    String checking;
    boolean mSubscribedToDelaroy;
    String  textcompanyname, storeitem, deviceitem, from;

    Switch switch_button1;
    String name_exte_dialog;
    EditText et_phone_number_dialog;

    Switch switch_button;

    String ipAddress_stored;
    Integer ipCount;
    SharedPreferences prefs;
    Button button;
    TextView textView2;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_register_otp);
        progressBar = (RelativeLayout) findViewById(R.id.progressbar_register);

        textView2 = (TextView) findViewById(R.id.seconds);
        textView2.setText("");

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();


        ipAddress_stored = prefs.getString("ip_address", "");
        if (ipAddress_stored != "") {
            ipCount  = prefs.getInt("ip_count",-1);
//            textView.setText(ipCount.toString());
        }

//        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(MainActivity_Register_OTPbased.this);
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

        ImageView leftarrow = (ImageView) findViewById(R.id.leftarrow);
        leftarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        db = openOrCreateDatabase("amazon.db", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE if not exists register (_id integer PRIMARY KEY UNIQUE, companyname text, storename text,devicename text, email text, password text,confirmpassword text);");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // get back to main screen
            String value = extras.getString("TODO");
            if (value.equals("exit")) {
                onBackPressed();
            }
        }

//        layout_dine = (LinearLayout) findViewById(R.id.layout_dine);
//        layout_qsr = (LinearLayout) findViewById(R.id.layout_qsr);
//        layout_retail = (LinearLayout) findViewById(R.id.layout_retail);

        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);

        radio_dine = (RadioButton) findViewById(R.id.radio_dine);
        radio_qsr = (RadioButton) findViewById(R.id.radio_qsr);
        radio_retail = (RadioButton) findViewById(R.id.radio_retail);

        final int selected1 = radioGroup1.getCheckedRadioButtonId();
        if (selected1 == radio_dine.getId()) {
            radio_dine.setChecked(true);
            radio_qsr.setChecked(false);
            radio_retail.setChecked(false);
            selection = "Dine";
            selection1 = "dine";
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }
        if (selected1 == radio_qsr.getId()) {
            radio_dine.setChecked(false);
            radio_qsr.setChecked(true);
            radio_retail.setChecked(false);
            selection = "Qsr";
            selection1 = "qsr";
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }
        if (selected1 == radio_retail.getId()) {
            radio_dine.setChecked(false);
            radio_qsr.setChecked(false);
            radio_retail.setChecked(true);
            selection = "Retail";
            selection1 = "retail";
            WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
        }

//        layout_dine.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                radio_dine.setChecked(true);
//                radio_qsr.setChecked(false);
//                radio_retail.setChecked(false);
//                selection = "Dine";
//                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
//            }
//        });
//
//        layout_qsr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                radio_dine.setChecked(false);
//                radio_qsr.setChecked(true);
//                radio_retail.setChecked(false);
//                selection = "Qsr";
//                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
//            }
//        });
//
//        layout_retail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                radio_dine.setChecked(false);
//                radio_qsr.setChecked(false);
//                radio_retail.setChecked(true);
//                selection = "Retail";
//                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
//            }
//        });

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                final int selected1 = radioGroup1.getCheckedRadioButtonId();
                radioBtnsplit = (RadioButton) findViewById(selected1);

                if (selected1 == radio_dine.getId()) {
                    radio_dine.setChecked(true);
                    radio_qsr.setChecked(false);
                    radio_retail.setChecked(false);
                    selection = "Dine";
                    selection1 = "dine";
                    WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                }
                if (selected1 == radio_qsr.getId()) {
                    radio_dine.setChecked(false);
                    radio_qsr.setChecked(true);
                    radio_retail.setChecked(false);
                    selection = "Qsr";
                    selection1 = "qsr";
                    WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                }
                if (selected1 == radio_retail.getId()) {
                    radio_dine.setChecked(false);
                    radio_qsr.setChecked(false);
                    radio_retail.setChecked(true);
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
//                    bottom.setVisibility(View.VISIBLE);
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
//                    bottom.setVisibility(View.VISIBLE);
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
//                    bottom.setVisibility(View.VISIBLE);
                    selection = "Retail";
                    selection1 = "retail";
                    WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
                }
            }
        });

        LinearLayout email_layout = (LinearLayout) findViewById(R.id.email_layout);
        LinearLayout phone_number_layout = (LinearLayout) findViewById(R.id.phone_number_layout);

        switch_button = (Switch) findViewById(R.id.switch_button);
        switch_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    Toast.makeText(MainActivity_Signin_OTPbased.this, "checked", Toast.LENGTH_LONG).show();
                    phone_number_layout.setVisibility(View.GONE);
                    email_layout.setVisibility(View.VISIBLE);
                }else {
//                    Toast.makeText(MainActivity_Signin_OTPbased.this, "not checked", Toast.LENGTH_LONG).show();
                    phone_number_layout.setVisibility(View.VISIBLE);
                    email_layout.setVisibility(View.GONE);
                }
            }
        });

        SharedPreferenceManager.initializeInstance(getApplicationContext());

        Log.d("ConfiguringAmplify1", "Initiating");

        init();


        TextView signin = (TextView) findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity_Register_OTPbased.this, MainActivity_Signin_OTPbased.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });





    }

    private void init() {

////        if (selection.toString().equals("Retail")) {
//        AppHelper_Retail.init(getApplicationContext());
////        }else {
//        AppHelper.init(getApplicationContext());
////        }
        //db_appdata class object
        databaseHelper = new DatabaseHelper1(this);
        registeredData = new Charity();
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        inUsername = (EditText) findViewById(R.id.editTextUserId);
//        et_store= (EditText) findViewById(R.id.et_store);
        et_store = new TextView(MainActivity_Register_OTPbased.this);
        et_store.setText("store1");
//        et_device= (EditText) findViewById(R.id.et_device);
        et_device = new TextView(MainActivity_Register_OTPbased.this);
        et_device.setText("device1");


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

                        TextView tv = new TextView(MainActivity_Register_OTPbased.this);
                        tv.setText(selection);
                        if (tv.getText().toString().equals("")) {
                            Toast.makeText(MainActivity_Register_OTPbased.this, "Select business type", Toast.LENGTH_LONG).show();
                        }else {
                            int id = item.getItemId();
                            if (id == R.id.linearLayout) {
                                dialog_auth = new Dialog(MainActivity_Register_OTPbased.this, R.style.notitle);
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
                                                login();
                                            }
                                        }else {
                                            if (et_phone_number_dialog.getText().toString().equalsIgnoreCase("")) {
                                                et_phone_number_dialog.setError("Please enter Phone number");
                                            } else {
                                                if (country_name.toString().equalsIgnoreCase("India")) {
                                                    if (et_phone_number_dialog.getText().toString().length() != 10) {
                                                        et_phone_number_dialog.setError("Enter valid number");
                                                    }else {
                                                        login();
                                                    }
                                                }else {
                                                    if (country_name.toString().equalsIgnoreCase("Singapore")) {
                                                        if (et_phone_number_dialog.getText().toString().length() != 8) {
                                                            et_phone_number_dialog.setError("Enter valid number");
                                                        }else {
                                                            login();
                                                        }
                                                    }else {
                                                        if (country_name.toString().equalsIgnoreCase("Malaysia")) {
                                                            if (et_phone_number_dialog.getText().toString().length() != 9) {
                                                                et_phone_number_dialog.setError("Enter valid number");
                                                            }else {
                                                                login();
                                                            }
                                                        }else {
                                                            login();
                                                        }
                                                    }
                                                }

                                            }
                                        }


                                    }
                                });
                            } else {
                                if (id == R.id.linearLayout_device) {
                                    dialog_auth = new Dialog(MainActivity_Register_OTPbased.this, R.style.notitle);
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


        tv_signUp = (TextView) findViewById(R.id.tv_signUp);
        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView tv = new TextView(MainActivity_Register_OTPbased.this);
                tv.setText(selection);
                if (tv.getText().toString().equals("")) {
                    Toast.makeText(MainActivity_Register_OTPbased.this, "Select business type", Toast.LENGTH_LONG).show();
                }else {

                    if (isDeviceOnline() == true) {


                        System.out.println("user name.....1");
                        if (switch_button.isChecked()) {
                            phone_number = inUsername.getText().toString();
                            if (inUsername.getText().toString().contains("@")) {
                                String match = "@";
                                int position1 = inUsername.getText().toString().indexOf(match);
                                phone_number1 = inUsername.getText().toString().substring(0, position1);
                            }else {
                                phone_number1 = et_phone_number.getText().toString();
                            }
                        }else {
                            phone_number = et_phone_number.getText().toString();
                            phone_number1 = et_phone_number.getText().toString();
                        }
                        store = et_store.getText().toString();
                        device = et_device.getText().toString();
                        boolean invalid = false;


                        if (switch_button.isChecked()) {
                            if (phone_number.equals("") || phone_number.length() == 0) {
                                invalid = true;
                                inUsername.setError("Enter Your E-Mail ID");
                            }else if (invalid == false) {

//                        signup();

//                            System.out.println("text is " + company.replaceAll("[^A-Za-z0-9]", ""));
//                            company = company.replaceAll("[^A-Za-z0-9]", "");
//                            company = company.toLowerCase();

                                CountryCodePicker countryCode_picker = (CountryCodePicker) findViewById(R.id.countryCode_picker);

                                country_name = countryCode_picker.getSelectedCountryName();
                                String name_code = countryCode_picker.getSelectedCountryNameCode();
                                name_exte = countryCode_picker.getSelectedCountryCodeWithPlus();

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
                                et_phone_number.setError("Enter Your Phone number");
                            }else if (invalid == false) {

//                        signup();

//                            System.out.println("text is " + company.replaceAll("[^A-Za-z0-9]", ""));
//                            company = company.replaceAll("[^A-Za-z0-9]", "");
//                            company = company.toLowerCase();

                                CountryCodePicker countryCode_picker = (CountryCodePicker) findViewById(R.id.countryCode_picker);

                                country_name = countryCode_picker.getSelectedCountryName();
                                String name_code = countryCode_picker.getSelectedCountryNameCode();
                                name_exte = countryCode_picker.getSelectedCountryCodeWithPlus();

                                if (name_exte.toString().contains("+880")) {
                                    et_phone_number.setError("Contact www.ivepos.com for more details");
                                }else {
                                    if (country_name.toString().equalsIgnoreCase("India")) {
                                        if (phone_number.length() != 10) {
                                            et_phone_number.setError("Enter valid number");
                                        } else {
                                            finalData = name_exte + phone_number;
                                            store = store.toLowerCase();
                                            device = device.toLowerCase();
                                            test_email_company();
                                        }
                                    } else {
                                        if (country_name.toString().equalsIgnoreCase("Singapore")) {
                                            if (phone_number.length() != 8) {
                                                et_phone_number.setError("Enter valid number");
                                            } else {
                                                finalData = name_exte + phone_number;
                                                store = store.toLowerCase();
                                                device = device.toLowerCase();
                                                test_email_company();
                                            }
                                        } else {
                                            if (country_name.toString().equalsIgnoreCase("Malaysia")) {
                                                if (phone_number.length() != 9) {
                                                    et_phone_number.setError("Enter valid number");
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
                        showAlert();
                    }

                }


            }
        });
    }

    public  void test_email_company() {
        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"test_email_company_otpbased.php",
                new Response.Listener<String>() {
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
                                Toast.makeText(MainActivity_Register_OTPbased.this, "Proceed without OTP", Toast.LENGTH_LONG).show();
                                register();
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


    public void signup() {

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
            tv_signUp.setEnabled(false);
            tv_signUp.setBackgroundResource(R.drawable.dark_black_rounded_corners);

            new CountDownTimer(30000, 1000) {

                public void onTick(long millisUntilFinished) {
                    textView2.setText("Request OTP after " + millisUntilFinished / 1000+" seconds");
                    // logic to set the EditText could go here
                }

                public void onFinish() {
                    textView2.setText("");
                    tv_signUp.setBackgroundResource(R.drawable.red_click_shape_rounded_corners);
                    tv_signUp.setEnabled(true);
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
        if (switch_button.isChecked()) {
            attributes.add(new AuthUserAttribute(AuthUserAttributeKey.email(), finalData));
        }else {
            attributes.add(new AuthUserAttribute(AuthUserAttributeKey.phoneNumber(), finalData));
        }
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
                                Toast.makeText(MainActivity_Register_OTPbased.this, "OTP sent on " + finalData, Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(MainActivity_Register_OTPbased.this, SignUpConfirmActivity_OTPbased.class);
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

                                hideKeyboard(MainActivity_Register_OTPbased.this);
                                donotshowKeyboard(MainActivity_Register_OTPbased.this);
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
                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity_Register_OTPbased.this).create();

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


//    SignUpHandler signUpHandler = new SignUpHandler() {
//        @Override
//        public void onSuccess(CognitoUser user, boolean signUpConfirmationState,
//                              CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
//            if (signUpConfirmationState) {
//                // User is already confirmed
//                showDialogMessage("Sign up successful!");
//            } else {
//                // User is not confirmed
//                confirmSignUp(cognitoUserCodeDeliveryDetails);
//            }
//        }
//
//        @Override
//        public void onFailure(Exception exception) {
//
//            Log.e("signup exception",exception.toString());
//
//            if(exception.toString().contains("UsernameExistsException")){
//
//                //signup();
//
////                Log.e("exception1","delete user");
////                Log.e("user id",email);
////               // AppHelper.getPool().getUser(email).deleteUser(genericHandler);
////                try {
////                    AppHelper.getPool().getUser(email);
////                } catch (Exception e) {
////                    e.printStackTrace();
////                    Log.e("exception getuser",e.toString());
////                }
////                // AppHelper.getPool().getUser(email).getDetailsInBackground(getDetailsHandler);
////            }else{
//                TextView label = (TextView) findViewById(R.id.textViewUserRegCompanyMessage);
//                label.setText("Sign up failed. Email id already used");
////            username.setBackground(getDrawable(R.drawable.text_border_error));
//                showDialogMessage("Sign up failed. Email id already used");
//            }
//
//        }
//    };


    GetDetailsHandler getDetailsHandler = new GetDetailsHandler() {
        @Override
        public void onSuccess(CognitoUserDetails cognitoUserDetails) {
            Log.d("details", "success");
            Map<String, String> user = cognitoUserDetails.getAttributes().getAttributes();
            for (Map.Entry<String, String> entry : user.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
//                Log.d("key", "key: " + key);
//                Log.d("key", "value: " + value);

            }

        }

        @Override
        public void onFailure(Exception exception) {
            Log.d("exception-details", exception.toString());
        }
    };

    GenericHandler genericHandler = new GenericHandler(){

        @Override
        public void onSuccess() {
            Log.e("exception2","delete success");
            signup();
        }

        @Override
        public void onFailure(Exception exception) {
            Log.e("exception3","delete failed");
//            Log.e("exception3delete",exception.toString());
            TextView label = (TextView) findViewById(R.id.textViewUserRegCompanyMessage);
            label.setText(getString(R.string.sdm1));
//            username.setBackground(getDrawable(R.drawable.text_border_error));
            showDialogMessage(getString(R.string.sdm1));


        }
    };


    private void confirmSignUp(CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {

        progressBar.setVisibility(View.GONE);

        Intent intent = new Intent(this, SignUpConfirmActivity.class);
        intent.putExtra("phone_number", phone_number);
        intent.putExtra("storename", store);
        intent.putExtra("devicename", device);
        intent.putExtra("source", "signup");
        intent.putExtra("companyname", phone_number1+""+selection1);
        intent.putExtra("companyname_special", phone_number1+""+selection1);
        intent.putExtra("account_selection", selection);
        intent.putExtra("destination", cognitoUserCodeDeliveryDetails.getDestination());
        intent.putExtra("deliveryMed", cognitoUserCodeDeliveryDetails.getDeliveryMedium());
        intent.putExtra("attribute", cognitoUserCodeDeliveryDetails.getAttributeName());
        startActivity(intent);
    }


    private boolean isValidEmaillId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
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

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void showDialogMessage(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity_Register_OTPbased.this).create();

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

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity_Register_OTPbased.this).create();

        alertDialog.setTitle(getString(R.string.title10));
        alertDialog.setMessage(getString(R.string.setmessage19));
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();
    }


    private static boolean checkString(String str) {
        char ch;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        boolean specialFlag=false;
        String specialChars = "/*!@#$%^&*()\"{}_[]|\\?/<>,.";
        for(int i=0;i < str.length();i++) {
            ch = str.charAt(i);
            if( Character.isDigit(ch)) {
                numberFlag = true;
            }
            else if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }else if (specialChars.contains(ch+"")){
                specialFlag=true;
            }
            if(numberFlag && capitalFlag && lowerCaseFlag && specialFlag)
                return true;
        }
        return false;
    }

    public boolean containsSpecialCharacter(String s) {
        return (s == null) ? false : s.matches("[^A-Za-z0-9 ]");
    }

    public void login() {
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
                Request.Method.POST,
                WebserviceUrl+"login_otp.php",
                new Response.Listener<String>() {
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
                            Toast.makeText(MainActivity_Register_OTPbased.this, "Signup failed", Toast.LENGTH_LONG).show();
                        }
                        if(status.equalsIgnoreCase("success")){
                            progressBar.setVisibility(View.GONE);
                            dialog_auth.dismiss();
                            Intent intent=new Intent(MainActivity_Register_OTPbased.this,NewStoreActivity_OTP.class);
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
                Request.Method.POST,
                WebserviceUrl+"login_otp.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("response",response.toString());
                        JSONObject jsonObject= null;
                        String status="",company="",email="",password="";
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
                            Toast.makeText(MainActivity_Register_OTPbased.this, "Signup failed", Toast.LENGTH_LONG).show();
                        }
                        if(status.equalsIgnoreCase("success")){
                            progressBar.setVisibility(View.GONE);
                            dialog_auth.dismiss();
                            Intent intent=new Intent(MainActivity_Register_OTPbased.this,NewDeviceActivity.class);
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
                params.put("email", email + "");
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);


    }

    private void setAmplifyConfiguration(int configResourceId) {
        try {
//            Amplify.configure(MainActivity.this);

//            Amplify.configure(AmplifyConfiguration.fromConfigFile(getApplicationContext(), getConfigResourceId(getApplicationContext())), getApplicationContext());
            Amplify.configure(AmplifyConfiguration.fromConfigFile(getApplicationContext(), configResourceId), getApplicationContext());

            Log.d("ConfiguringAmplify2", "Initiated");
            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException e) {
            e.printStackTrace();
            Log.e("MyAmplifyApp", "Could not initialize Amplify", e);
        }
    }


    public int getConfigResourceId(Context context, String configureFileName) {
        return context.getResources().getIdentifier(configureFileName, "raw", context.getPackageName());
    }

    private void register() {
//        progressBar1 = (RelativeLayout) findViewById(R.id.progressbar1);
//        progressBar1.setVisibility(View.VISIBLE);

        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"newcomp_phon_spec_ema_otp.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        System.out.println("newcompany_phone_speci_email "+response);
                        if(response.equalsIgnoreCase("success")){
//                            Log.d("response",response);



                            Intent intent = new Intent(MainActivity_Register_OTPbased.this, DownloadService.class);
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
                params.put("company", phone_number1+""+selection1);
                params.put("company_special", phone_number1+""+selection1);
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



                            TextView from1 = new TextView(MainActivity_Register_OTPbased.this);
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
                            TextView from1 = new TextView(MainActivity_Register_OTPbased.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Amplify.Auth.fetchAuthSession(
                resultSession -> {
//                    Log.i("AmplifyAuthSession", resultSession.toString());
                    if (resultSession.isSignedIn()) {
                        // Pin Activity Will Open
                        AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) resultSession;
//                        Log.d("CognitoSessionIDT", String.valueOf(cognitoAuthSession));

//                        assert cognitoAuthSession.getUserPoolTokens().getValue() != null;
                        if (cognitoAuthSession.getUserPoolTokens().getValue().getAccessToken() != null) {
//                            Log.d("AccessToken", cognitoAuthSession.getUserPoolTokens().getValue().getAccessToken());

                            SharedPreferenceManager.getInstance().setDataDownloadValue(false);

                            Amplify.Auth.signOut(
                                    () -> Log.i("AuthQuickstart", "Signed out successfully"),
                                    error -> Log.e("AuthQuickstart", error.toString())
                            );
                        } else {
                            Log.d("AccessTokenE", "Token Is Null");
                        }
                    } else {
                    }
                },
                error -> Log.e("AmplifyQuickstart", error.toString())
        );
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

}