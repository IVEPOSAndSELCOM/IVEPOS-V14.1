package com.intuition.ivepos;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;


public class MainActivity_Register extends AppCompatActivity {
    String TAG = "SignUp";
    EditText et_company,et_phone_number, et_email,et_password, et_store,et_device,et_confirmpassword;
    TextView tv_passwordmsg;
    String company, store, device,phone_number, email,password,confpassword, company_special;
    TextView tv_signUp;
    AlertDialog userDialog;
    Context c = this;
    SQLiteDatabase db;
    DatabaseHelper1 databaseHelper;
    Charity registeredData;
    int i = 0;
    RequestQueue requestQueue;

    RadioGroup radioGroupSplit;
    RadioButton radioBtnsplit, radio_dine, radio_qsr, radio_retail;
    LinearLayout layout_dine, layout_qsr, layout_retail;
    RadioGroup radioGroup1;
    String selection;

    String WebserviceUrl;


    Dialog dialog_auth;;
    EditText editTextUserId,editTextPassword;
    Button btnlogin;
    RelativeLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_register);
        progressBar = (RelativeLayout) findViewById(R.id.progressbar_register);

//        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(MainActivity_Register.this);
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

        layout_dine = (LinearLayout) findViewById(R.id.layout_dine);
        layout_qsr = (LinearLayout) findViewById(R.id.layout_qsr);
        layout_retail = (LinearLayout) findViewById(R.id.layout_retail);

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
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }
        if (selected1 == radio_qsr.getId()) {
            radio_dine.setChecked(false);
            radio_qsr.setChecked(true);
            radio_retail.setChecked(false);
            selection = "Qsr";
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }
        if (selected1 == radio_retail.getId()) {
            radio_dine.setChecked(false);
            radio_qsr.setChecked(false);
            radio_retail.setChecked(true);
            selection = "Retail";
            WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
        }

        layout_dine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_dine.setChecked(true);
                radio_qsr.setChecked(false);
                radio_retail.setChecked(false);
                selection = "Dine";
                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
            }
        });

        layout_qsr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_dine.setChecked(false);
                radio_qsr.setChecked(true);
                radio_retail.setChecked(false);
                selection = "Qsr";
                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
            }
        });

        layout_retail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_dine.setChecked(false);
                radio_qsr.setChecked(false);
                radio_retail.setChecked(true);
                selection = "Retail";
                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
            }
        });

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
                    WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                }
                if (selected1 == radio_qsr.getId()) {
                    radio_dine.setChecked(false);
                    radio_qsr.setChecked(true);
                    radio_retail.setChecked(false);
                    selection = "Qsr";
                    WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                }
                if (selected1 == radio_retail.getId()) {
                    radio_dine.setChecked(false);
                    radio_qsr.setChecked(false);
                    radio_retail.setChecked(true);
                    selection = "Retail";
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
                    WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
                }
            }
        });

        init();



    }

    private void init() {

//        if (selection.toString().equals("Retail")) {
        AppHelper_Retail.init(getApplicationContext());
//        }else {
        AppHelper.init(getApplicationContext());
//        }
        //db_appdata class object
        databaseHelper = new DatabaseHelper1(this);
        registeredData = new Charity();
        et_company = (EditText) findViewById(R.id.et_company);
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        et_store= (EditText) findViewById(R.id.et_store);
        et_store.setText("store1");
        et_device= (EditText) findViewById(R.id.et_device);
        et_device.setText("device1");
        et_confirmpassword = (EditText) findViewById(R.id.et_confirmpwd);
        tv_passwordmsg=(TextView) findViewById(R.id.textViewUserRegPasswordMessage);


//        et_password.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                if(checkString(s.toString())){
//                    tv_passwordmsg.setVisibility(View.GONE);
//                }else{
//                    tv_passwordmsg.setVisibility(View.VISIBLE);
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                if(checkString(s.toString())){
//                    tv_passwordmsg.setVisibility(View.GONE);
//                }else{
//                    tv_passwordmsg.setVisibility(View.VISIBLE);
//                }
//
//            }
//        });




        et_confirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegUserconfirmPasswordLabel);
                    label.setText(et_confirmpassword.getHint());
//                    password.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewUserRegconfirmPasswordMessage);
                label.setText("");

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegUserconfirmPasswordLabel);
                    label.setText("");
                }
            }
        });


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

                        TextView tv = new TextView(MainActivity_Register.this);
                        tv.setText(selection);
                        if (tv.getText().toString().equals("")) {
                            Toast.makeText(MainActivity_Register.this, "Select business type", Toast.LENGTH_LONG).show();
                        }else {
                            int id = item.getItemId();
                            if (id == R.id.linearLayout) {
                                dialog_auth = new Dialog(MainActivity_Register.this, R.style.notitle);
                                dialog_auth.setContentView(R.layout.signin_dialog);
                                dialog_auth.setCanceledOnTouchOutside(false);
                                dialog_auth.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                dialog_auth.show();

                                editTextUserId = dialog_auth.findViewById(R.id.editTextUserId);
                                editTextPassword = dialog_auth.findViewById(R.id.editTextUserPassword);
                                btnlogin = dialog_auth.findViewById(R.id.login1);
                                btnlogin.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


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

                                        if (editTextUserId.getText().toString().equalsIgnoreCase("")) {
                                            editTextUserId.setError("Please enter the username");
                                        } else if (editTextPassword.getText().toString().equalsIgnoreCase("")) {
                                            editTextPassword.setError("Please enter the password");
                                        } else {
                                            login();
                                        }

                                    }
                                });
                            } else {
                                if (id == R.id.linearLayout_device) {
                                    dialog_auth = new Dialog(MainActivity_Register.this, R.style.notitle);
                                    dialog_auth.setContentView(R.layout.signin_dialog);
                                    dialog_auth.setCanceledOnTouchOutside(false);
                                    dialog_auth.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                    dialog_auth.show();

                                    editTextUserId = dialog_auth.findViewById(R.id.editTextUserId);
                                    editTextPassword = dialog_auth.findViewById(R.id.editTextUserPassword);
                                    btnlogin = dialog_auth.findViewById(R.id.login1);
                                    btnlogin.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {



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

                                            if (editTextUserId.getText().toString().equalsIgnoreCase("")) {
                                                editTextUserId.setError("Please enter the username");
                                            } else if (editTextPassword.getText().toString().equalsIgnoreCase("")) {
                                                editTextPassword.setError("Please enter the password");
                                            } else {
                                                loginDevice();
                                            }

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

                TextView tv = new TextView(MainActivity_Register.this);
                tv.setText(selection);
                if (tv.getText().toString().equals("")) {
                    Toast.makeText(MainActivity_Register.this, "Select business type", Toast.LENGTH_LONG).show();
                }else {

                    if (et_password.getText().toString().length() < 6 || et_confirmpassword.getText().toString().length() < 6) {
                        if (et_password.getText().toString().length() < 6) {
                            et_password.setError("Passsword should have more than 6 Characters");
                        }
                        if (et_confirmpassword.getText().toString().length() < 6) {
                            et_confirmpassword.setError("Passsword should have more than 6 Characters");
                        }

                    } else {

                        if (isDeviceOnline() == true) {


                            System.out.println("user name.....1");
                            password = et_password.getText().toString();
                            confpassword = et_confirmpassword.getText().toString();
                            company = et_company.getText().toString();
                            company_special = et_company.getText().toString();
                            email = et_email.getText().toString();
                            phone_number = et_phone_number.getText().toString();
                            store = et_store.getText().toString();
                            device = et_device.getText().toString();
                            boolean invalid = false;


                            if (company.equals("") || company.length() == 0) {
                                invalid = true;
                                et_company.setError("Enter Your CompanyName");
                            } else if (phone_number.equals("") || phone_number.length() == 0) {
                                invalid = true;
                                et_phone_number.setError("Enter Your Phone number");
                            } else if (email.equals("") || email.length() == 0) {
                                invalid = true;
                                et_email.setError("Enter Your Email");
                            } else if (!isValidEmaillId(email)) {
                                invalid = true;
                                et_email.setError("Enter Valid Email Id");
                            } else if (password.equals("") || password.length() == 0) {
                                invalid = true;
                                et_password.setError("Enter the Password");
                            } else if (confpassword.equals("") || confpassword.length() == 0) {
                                invalid = true;
                                et_confirmpassword.setError("Enter the confirmPassword");
                            } else if (store.equals("") || store.length() == 0) {
                                invalid = true;
                                et_store.setError("Enter the store");
                            } else if (device.equals("") || device.length() == 0) {
                                invalid = true;
                                et_device.setError("Enter the device");
                            } else if (!confpassword.equals(password)) {
                                invalid = true;
                                et_confirmpassword.setError("Password and Confirm Password does not match");
                            } else if (invalid == false) {

//                        signup();

                                System.out.println("text is " + company.replaceAll("[^A-Za-z0-9]", ""));
                                company = company.replaceAll("[^A-Za-z0-9]", "");
                                company = company.toLowerCase();
                                store = store.toLowerCase();
                                device = device.toLowerCase();
                                test_email_company();


                            }
                        } else {
                            showAlert();
                        }
                    }
                }


            }
        });
    }

    public void ShowHidePass(View view){

        if(view.getId()==R.id.show_pass_btn){

            if(et_password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_icon_eye_hide);

                //Show Password
                et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                et_password.setSelection(et_password.getText().length());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_icon_eye);

                //Hide Password
                et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                et_password.setSelection(et_password.getText().length());

            }
        }
    }

    public void ShowHidePass_conf(View view){

        if(view.getId()==R.id.show__confi_pass_btn){

            if(et_confirmpassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_icon_eye_hide);

                //Show Password
                et_confirmpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                et_confirmpassword.setSelection(et_confirmpassword.getText().length());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_icon_eye);

                //Hide Password
                et_confirmpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                et_confirmpassword.setSelection(et_confirmpassword.getText().length());

            }
        }
    }

    public  void test_email_company() {
        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"test_email_company.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response123",response);
                        if(response.equalsIgnoreCase("company exists")){
                            showDialogMessage(getString(R.string.sdm2));
                        }else{
                            if(response.equalsIgnoreCase("email exists")){
                                showDialogMessage(getString(R.string.sdm3));
                            }else{
                                if(response.equalsIgnoreCase("company existsemail exists")){
                                    showDialogMessage(getString(R.string.sdm4));
                                }else{
                                    Log.d("response123","createappand sales");
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
                params.put("email", email + "");
                params.put("company", company + "");
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);
    }


    public void signup() {

//        if (selection.toString().equals("Retail")) {
//            CognitoUserAttributes userAttributes = new CognitoUserAttributes();
//            userAttributes.addAttribute(AppHelper_Retail.getSignUpFieldsC2O().get(et_email.getHint()).toString(), email);
//            AppHelper_Retail.getPool().signUpInBackground(email, password, userAttributes, null, signUpHandler);
//        }else {
//            CognitoUserAttributes userAttributes = new CognitoUserAttributes();
//            userAttributes.addAttribute(AppHelper.getSignUpFieldsC2O().get(et_email.getHint()).toString(), email);
//            AppHelper.getPool().signUpInBackground(email, password, userAttributes, null, signUpHandler);
//        }


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
                Log.d("key", "key: " + key);
                Log.d("key", "value: " + value);

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
            Log.e("exception3delete",exception.toString());
            TextView label = (TextView) findViewById(R.id.textViewUserRegCompanyMessage);
            label.setText(getString(R.string.sdm1));
//            username.setBackground(getDrawable(R.drawable.text_border_error));
            showDialogMessage(getString(R.string.sdm1));


        }
    };


    private void confirmSignUp(CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {

        progressBar.setVisibility(View.GONE);

        Intent intent = new Intent(this, SignUpConfirmActivity.class);
        intent.putExtra("emailid", email);
        intent.putExtra("phone_number", phone_number);
        intent.putExtra("storename", store);
        intent.putExtra("devicename", device);
        intent.putExtra("source", "signup");
        intent.putExtra("companyname", company);
        intent.putExtra("companyname_special", company_special);
        intent.putExtra("password", password);
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

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void showDialogMessage(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity_Register.this).create();

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
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity_Register.this).create();

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
        email=editTextUserId.getText().toString();
        password=editTextPassword.getText().toString();

        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response",response.toString());
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
                        }
                        if(status.equalsIgnoreCase("success")){
                            progressBar.setVisibility(View.GONE);
                            dialog_auth.dismiss();
                            Intent intent=new Intent(MainActivity_Register.this,NewStoreActivity.class);
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
                params.put("password", password+ "");
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
        email=editTextUserId.getText().toString();
        password=editTextPassword.getText().toString();

        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response",response.toString());
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
                        }
                        if(status.equalsIgnoreCase("success")){
                            progressBar.setVisibility(View.GONE);
                            dialog_auth.dismiss();
                            Intent intent=new Intent(MainActivity_Register.this,NewDeviceActivity.class);
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
                params.put("password", password+ "");
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);


    }

}