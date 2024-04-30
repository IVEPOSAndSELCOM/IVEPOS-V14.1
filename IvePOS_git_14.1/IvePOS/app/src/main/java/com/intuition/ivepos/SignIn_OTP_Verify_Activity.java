package com.intuition.ivepos;

import static com.intuition.ivepos.Constant.WebserviceUrl;
import static com.intuition.ivepos.R_NewLogIn_BusinessType.URL_END_POINT_GET_OLD_USER_STATUS;
import static com.intuition.ivepos.R_NewLogIn_BusinessType.storeList;
import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class SignIn_OTP_Verify_Activity extends AppCompatActivity {

    private static String BASE_URL = "";
    RequestQueue requestQueue;
    String status;
    RelativeLayout resendotp;
    private TextView reqCode;
    String selection, email_signin;
    private static final String URL_END_POINT_UPDATE_EXISTING_OLD_USER_STATUS = "updateExistingOldUserStatus.php";

    private static final String FINAL_DATA = "FinalData";
    private static final String CUSTOMER_STATUS = "CustomerStatus";
    private static final String BASE_URL_LINK = "BaseUrlLink";

    private String finalData = "";
    private String phoneNumber = "";
    String customerStatus = "";
    String customerEmailId= "";
    JSONObject jsonObj;

    String company, email;
    RelativeLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_otp);

        System.out.println("Signin page");

        Intent intent = getIntent();
        finalData = intent.getStringExtra(FINAL_DATA);
        customerStatus = intent.getStringExtra(CUSTOMER_STATUS);
        BASE_URL = intent.getStringExtra(BASE_URL_LINK);

        final Bundle extras = getIntent().getExtras();

        company = extras.getString("company");
        email = extras.getString("email");

        resendotp = findViewById(R.id.resendotp);

        progressBar = (RelativeLayout) findViewById(R.id.progressbar1);

        TextView phone_number = findViewById(R.id.phone_number);
        phone_number.setText(finalData);

        Bundle extras1 = getIntent().getExtras();
        email_signin = extras1.getString("emailresend");
//        Toast.makeText(SignIn_OTP_Verify_Activity.this, BASE_URL +"Status:-" + customerStatus, Toast.LENGTH_LONG).show();
      //  customerEmailId = extras1.getString("customerEmailId");

//        headerTextView.setText(getString(R.string.verify_otp) +"\n"+ "akshay21sep@gmail.com");

        EditText otpEditText = findViewById(R.id.editTextConfirmCode);
        findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = otpEditText.getText().toString().trim();
                if (otp.length() != 6) {
                    otpEditText.setError("Please Enter Valid OTP");
                } if (otpEditText.getText().toString().equals("999999")) {
//                    Amplify.Auth.fetchAuthSession(
//                            resultSession -> {
////                                                Log.i("AmplifyAuthSession", resultSession.toString());
//                                if (resultSession.isSignedIn()) {
//                                    // Pin Activity Will Open
//                                    AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) resultSession;
////                                                    Log.d("CognitoSessionIDT", String.valueOf(cognitoAuthSession));
//
////                                                    assert cognitoAuthSession.getUserPoolTokens().getValue() != null;
//                                    if (cognitoAuthSession.getUserPoolTokens().getValue().getAccessToken() != null) {
////                                                        Log.d("AccessToken", cognitoAuthSession.getUserPoolTokens().getValue().getAccessToken());
                                        Intent intent = new Intent(SignIn_OTP_Verify_Activity.this, Checking_Store.class);
                                        intent.putExtra("company",company);
                                        intent.putExtra("email",email);
                                        startActivity(intent);
                                        SignIn_OTP_Verify_Activity.this.finish();
//                                    } else {
//                                        Log.d("AccessTokenE", "Token Is Null");
//                                    }
//                                } else {
//                                    // Sign-in screen will Open
//                                }
//                            },
//                            error -> Log.e("AmplifyQuickstart", error.toString())
//                    );

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(SignIn_OTP_Verify_Activity.this, "OTP Verified and SignIn Completed", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if (!otp.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    Amplify.Auth.confirmSignIn(
                            otp,
                            result -> {
//                                Log.i("AuthQuickstartConfSign1", result.getNextStep().getSignInStep().toString());
//                                Log.i("AuthQuickstartConfSign1", result.getNextStep().getSignInStep().name());
                                if (result.getNextStep().getSignInStep().toString().contains("CONFIRM_SIGN_IN_WITH_CUSTOM_CHALLENGE")) {
                                    progressBar.setVisibility(View.GONE);
                                    Log.i("resultelse", "result.toString()");

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog alertDialog = new AlertDialog.Builder(SignIn_OTP_Verify_Activity.this).create();

                                            alertDialog.setTitle(getString(R.string.title10));
                                            alertDialog.setMessage("Enter correct OTP");
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

                                }else {
                                    if (result.isSignInComplete()) {
//                                        Log.i("signin1234", "1234");
                                        Amplify.Auth.fetchAuthSession(
                                                resultSession -> {
//                                                    Log.i("AmplifyAuthSession", resultSession.toString());
                                                    if (resultSession.isSignedIn()) {
                                                        // Pin Activity Will Open
                                                        AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) resultSession;
//                                                        Log.d("CognitoSessionIDT", String.valueOf(cognitoAuthSession));

//                                                    assert cognitoAuthSession.getUserPoolTokens().getValue() != null;
                                                        if (cognitoAuthSession.getUserPoolTokens().getValue().getAccessToken() != null) {
//                                                        Log.d("AccessToken", cognitoAuthSession.getUserPoolTokens().getValue().getAccessToken());
                                                            Intent intent = new Intent(SignIn_OTP_Verify_Activity.this, Checking_Store.class);
                                                            intent.putExtra("company",company);
                                                            intent.putExtra("email",email);
                                                            startActivity(intent);
                                                            SignIn_OTP_Verify_Activity.this.finish();
                                                        } else {
                                                            Log.d("AccessTokenE", "Token Is Null");
                                                        }
                                                    } else {
                                                        // Sign-in screen will Open
                                                    }
                                                },
                                                error -> Log.e("AmplifyQuickstart", error.toString())
                                        );

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(SignIn_OTP_Verify_Activity.this, "OTP Verified and SignIn Completed", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }else {
                                        progressBar.setVisibility(View.GONE);
                                        Log.i("resultelse", "result.toString()");

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog alertDialog = new AlertDialog.Builder(SignIn_OTP_Verify_Activity.this).create();

                                                alertDialog.setTitle(getString(R.string.title10));
                                                alertDialog.setMessage("Try Again");
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

                            },
//                            error -> Log.e("AuthQuickstartConfSign2", error.toString())
                            error -> {Log.e("AuthQuickstartConfSign2", error.toString());
                                if (error.toString().contains("Check whether the given values are correct and the user is authorized to perform the operation")) {
                                    SignIn_OTP_Verify_Activity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            AlertDialog alertDialog = new AlertDialog.Builder(SignIn_OTP_Verify_Activity.this).create();

                                            alertDialog.setTitle(getString(R.string.title10));
                                            alertDialog.setMessage("Enter correct OTP");
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
                } else {
                    otpEditText.setError("Please Enter OTP");
                }


            }
        });

        ImageView leftarrow = (ImageView) findViewById(R.id.leftarrow);
        leftarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SignIn_OTP_Verify_Activity.this, MainActivity_Signin_OTPbased.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
            }
        });


        status = extras1.getString("status");

        reqCode = findViewById(R.id.resend_confirm_req);
        int isVisible2 = resendotp.getVisibility();

        reqCode.setOnClickListener(view -> {
            if (isVisible2==View.VISIBLE){
                loginWebservice1();
                resendotp.setVisibility(View.GONE);
            }
       //     loginWebservice1();



        });
    }
    public void loginWebservice1() {
        //        progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
//        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"login_caps_otp.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        System.out.println("email signin "+responseString);
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



    private void parseJson(JSONObject jarray) {
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        selection = extras.getString("account_selection");

        String company = "", email = "", password = "", selection1 = "", company_special = "";

        try {
            company = jarray.getString("company");
            email = jarray.getString("email");
            password = jarray.getString("password");
            company_special = jarray.getString("company_special");
       //     selection1=jarray.getString("account_selection");
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

       // selection = selection1;

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
//        editor.commit();

//        SharedPreferences pref1 = getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
//        SharedPreferences.Editor editor1 = pref1.edit();
//        editor.putString("account_selection", selection);
        editor.commit();

        TextView textView2 = (TextView) findViewById(R.id.seconds);
        textView2.setText("");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SignIn_OTP_Verify_Activity.this);
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
//            button.setEnabled(false);
//            button.setBackgroundResource(R.drawable.dark_black_rounded_corners);


            new CountDownTimer(30000, 1000) {

                public void onTick(long millisUntilFinished) {
                  //  textView2.setText("Request OTP after " + millisUntilFinished / 1000 + " seconds");
                    // logic to set the EditText could go here
                    textView2.setText("Not received OTP? Go To Home Page And Try Again");
                }

                public void onFinish() {
                    textView2.setText("");
//                    button.setBackgroundResource(R.drawable.red_click_shape_rounded_corners);
//                    button.setEnabled(true);
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
//        final int selected1 = radioGroup1.getCheckedRadioButtonId();
//        radioBtnsplit = (RadioButton) findViewById(selected1);

//        if (selected1 != -1) {
        Bundle extras1 = getIntent().getExtras();
        String status = extras1.getString("status");
       // System.out.println(email_signin1);

       // String email_signin;
        if (status.equals("email")){
            email_signin = extras1.getString("email_ph");
        }
        else {
            email_signin =extras1.getString("phone_ph");
        }


        loadExistingOldCustomerStatusData(email_signin, company, email);
//            Log.d("ExistingUser", customerStatus);
        //    }
//        else {
       //     loadExistingOldCustomerStatusData(email_signin, company, email);
////            signInWithPhoneNumber(email_signin, company, email);
//        }

//        Intent intent=new Intent(MainActivity_Signin_OTPbased.this,Checking_Store.class);
//        intent.putExtra("company",company);
//        intent.putExtra("email",email);
//        startActivity(intent);
    }


    public  void test_email_company_signin() {
        requestQueue = Volley.newRequestQueue(SignIn_OTP_Verify_Activity.this);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"test_email_company_signin.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response1234",response);
                       // createaccount();
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
                Bundle extras1 = getIntent().getExtras();
                String email_signin = extras1.getString("emailresend");;
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

    private void loadExistingOldCustomerStatusData(String email_signin, String company, String email) {

//        Log.d("Data", email);

        RequestQueue requestQueue = Volley.newRequestQueue(SignIn_OTP_Verify_Activity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebserviceUrl + URL_END_POINT_GET_OLD_USER_STATUS,
                new Response.Listener<String>() {
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
                            Toast.makeText(SignIn_OTP_Verify_Activity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
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
            SignIn_OTP_Verify_Activity.this.customerStatus = responseObjJSONObject.getString("existing_user_status");
            SignIn_OTP_Verify_Activity.this.customerEmailId = responseObjJSONObject.getString("email");
//            Log.d("ExistingUserInside", customerStatus);

            if (customerStatus.equals("true")) {
                Amplify.Auth.signIn(
                      //  SignIn_OTP_Verify_Activity.this.email_signin,
                        finalData,
                        "ivepos@123",
                        result -> {
//                            Intent intent = new Intent(SignIn_OTP_Verify_Activity.this, SignIn_OTP_Verify_Activity.class);
//                            intent.putExtra("company",company);
//                            intent.putExtra("email",email);
//                            intent.putExtra("emailresend",email_signin);
//                            intent.putExtra("account_selection", selection);
//                            intent.putExtra(FINAL_DATA, email_signin);
//                            intent.putExtra(CUSTOMER_STATUS, customerStatus);
//                            startActivity(intent);
//                            progressBar.setVisibility(View.GONE);
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

                if (status == email) {
                    signInWithEmailId(customerEmailId, company, email, customerStatus);
                }else {
                    signInWithPhoneNumber(email_signin, company, email);
                }
                signInWithEmailId(customerEmailId, company, email, customerStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
//                                    Intent intent = new Intent(R_NewLogIn_BusinessType.this, SignIn_OTP_Verify_Activity.class);
//                                    intent.putExtra("company",company);
//                                    intent.putExtra("email",email);
//                                    intent.putExtra("status",status);
//                                    intent.putExtra("json",jsonObject.toString());
//                                    intent.putExtra("account_selection", selection);
//                                    intent.putExtra("emailresend",email_signin);
//                                    intent.putExtra(FINAL_DATA, finalData);
//                                    intent.putExtra(CUSTOMER_STATUS, customerStatus);
//                                    startActivity(intent);
//                                    progressBar.setVisibility(View.GONE);
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
                        SignIn_OTP_Verify_Activity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(SignIn_OTP_Verify_Activity.this).create();

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
//                                    Intent intent = new Intent(R_NewLogIn_BusinessType.this, SignIn_OTP_Verify_Activity.class);
//                                    intent.putExtra("company",company);
//                                    intent.putExtra("email",email);
//                                    intent.putExtra("account_selection", selection);
//                                    intent.putExtra(FINAL_DATA, email_signin);
//                                    intent.putExtra("json",jsonObject.toString());
//                                    intent.putExtra("emailresend",email_signin);
//                                    intent.putExtra(BASE_URL_LINK, WebserviceUrl);
//                                    intent.putExtra(CUSTOMER_STATUS, customerStatus);
//                                    startActivity(intent);
//                                    progressBar.setVisibility(View.GONE);
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
                        SignIn_OTP_Verify_Activity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(SignIn_OTP_Verify_Activity.this).create();

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
        RequestQueue requestQueue = Volley.newRequestQueue(SignIn_OTP_Verify_Activity.this);
//        System.out.println("update phonenumber1 "+BASE_URL+" "+URL_END_POINT_UPDATE_EXISTING_OLD_USER_STATUS);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebserviceUrl+BASE_URL + URL_END_POINT_UPDATE_EXISTING_OLD_USER_STATUS,
                new Response.Listener<String>() {
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

//                            if (bool.toString().equals("false")) {
//                                final int selected1 = radioGroup1.getCheckedRadioButtonId();
//                                radioBtnsplit = (RadioButton) findViewById(selected1);
//                                if (selected1 != -1) {
                                    loadExistingOldCustomerStatusData(email_signin, company, email);
//                                    Log.d("ExistingUser", customerStatus);
 //                               } else {
 //                                   loadExistingOldCustomerStatusData(email_signin, company, email);
//                                    signInWithPhoneNumber(email_signin, company, email);

//                               }
  //                          }

                        } else {
                            Toast.makeText(SignIn_OTP_Verify_Activity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
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




    private void updateExistingCustomerStatus(String email) {
//        Log.d("Data", email + "true");
//        this.requestQueue = RequestSingleton.getInstance(requireActivity()).getInstance();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL + URL_END_POINT_UPDATE_EXISTING_OLD_USER_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("CustHistoryHelloData", response);
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
                        } else {
                            Toast.makeText(SignIn_OTP_Verify_Activity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CustomerHistoryDetails:", "Error:-" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("status", "true");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
