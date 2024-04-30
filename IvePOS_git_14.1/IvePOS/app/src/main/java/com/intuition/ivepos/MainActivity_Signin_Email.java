package com.intuition.ivepos;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

import android.Manifest;
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
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity_Signin_Email extends AppCompatActivity {



    private final String TAG = "MainActivity";
    private EditText inUsername;
    private EditText inPassword;
    RelativeLayout progressBar;
    SQLiteDatabase db = null;
    SQLiteDatabase db1 = null;
    private static final String LOG_TAG = "CheckNetworkStatus";
    private boolean isConnected = false;
    ConnectionQuality mConnectionClass = ConnectionQuality.UNKNOWN;
    ConnectionClassManager mConnectionClassManager;
    DeviceBandwidthSampler mDeviceBandwidthSampler;
    int mTries=0;
    public static ArrayList<StoreBean> storeList=new ArrayList<StoreBean>();
    String username="",password="";
    private ForgotPasswordContinuation forgotPasswordContinuation;
    TextView textViewUserForgotPassword;
    RequestQueue requestQueue;
    String newPass="",code="";

    String WebserviceUrl;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_signin_email);

        progressBar = (RelativeLayout) findViewById(R.id.progressbar1);

        initApp();

//        SharedPreferences sharedpreferences_select =  MainActivity_Signin.getDefaultSharedPreferencesMultiProcess(MainActivity_Signin_Email.this);
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
    }




    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }


    public void signUp(View view) {
        signUpNewUser();
    }
    public void logIn(View view) {
        if (isDeviceOnline() == true) {

            if (inPassword.getText().toString().equals("") || inUsername.getText().toString().equals("")){
                Toast.makeText(MainActivity_Signin_Email.this, "Enter Username and Password", Toast.LENGTH_LONG).show();
            }else {


                mConnectionClassManager = ConnectionClassManager.getInstance();
                mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
                progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
                progressBar.setVisibility(View.VISIBLE);
                mTries=0;

                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = cm.getActiveNetworkInfo();
                if(info.getType() == ConnectivityManager.TYPE_WIFI){
                    loginWebservice();
                } else if(info.getType() == ConnectivityManager.TYPE_MOBILE){
                    // check NetworkInfo subtype
                    if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS){
                        speedTestTask();
                        // Bandwidth between 100 kbps and below
                    } else if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE){
                        speedTestTask();

                    }
                    else if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_1xRTT){
                        speedTestTask();
                    }
                    else if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA){
                        speedTestTask();
                    }
                    else if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_UNKNOWN ){
                        speedTestTask();
                    }
                    else if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_IDEN ){
                        speedTestTask();
                    }
                    else if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_UMTS ){
                        speedTestTask();
                    }
                    else if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_0 ){
                        speedTestTask();
                    }
                    else if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_A ){
                        speedTestTask();
                    }
                    else if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSDPA ){
                        speedTestTask();
                    }
                    else if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSPA ){
                        speedTestTask();
                    }
                    else if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_B ){
                        speedTestTask();
                    }
                    else if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSPAP ){
                        speedTestTask();
                    }
                    else if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_EHRPD ){
                        speedTestTask();
                    }
                    else if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSUPA){
                        speedTestTask();
                    }
                    else {
                        loginWebservice();
                    }

                }



            }
        } else {
            showAlert();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MainActivity_Signin_Email.storeList.clear();
    }

    private void signUpNewUser() {

        if (ContextCompat.checkSelfPermission(MainActivity_Signin_Email.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity_Signin_Email.this,
                    permissions(),
                    1);
            /*// Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity_Signin_Email.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

//                        Toast.makeText(ForgotPasswordActivity.this, "111111111", Toast.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(MainActivity_Signin_Email.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
//                        Toast.makeText(ForgotPasswordActivity.this, "no permission", Toast.LENGTH_SHORT).show();

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity_Signin_Email.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }*/
        } else {
//                    Toast.makeText(ForgotPasswordActivity.this, "hiiii", Toast.LENGTH_SHORT).show();

//            if (!SdIsPresent()) ;

            Intent registerActivity = new Intent(this, SignupActivity.class);
            startActivity(registerActivity);
        }

//        Intent registerActivity = new Intent(this, MainActivity_Register.class);
//        startActivityForResult(registerActivity, 1);
    }

    private void initApp() {
        inUsername = (EditText) findViewById(R.id.editTextUserId);
        inPassword = (EditText) findViewById(R.id.editTextUserPassword);

        AppCompatCheckBox checkbox = (AppCompatCheckBox) findViewById(R.id.checkbox);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    // hide password
                    inPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    inPassword.setSelection(inPassword.getText().length());
                } else {
                    // show password
                    inPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    inPassword.setSelection(inPassword.getText().length());
                }
            }
        });

        inPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    Button submit=findViewById(R.id.login1);
                    submit.performClick();
                    return true;
                }
                return false;
            }
        });

        textViewUserForgotPassword=findViewById(R.id.textViewUserForgotPassword);
        textViewUserForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpasswordUser();
            }
        });

    }


    private void forgotpasswordUser() {
        username = inUsername.getText().toString();
        if(username == null) {
            inUsername.setError("userid cannot be empty");
            return;
        }

        if(username.length() < 1) {
            inUsername.setError("userid cannot be empty");
            return;
        }
        progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
        progressBar.setVisibility(View.VISIBLE);

        AppHelper.getPool().getUser(inUsername.getText().toString()).forgotPasswordInBackground(forgotPasswordHandler);
    }

    ForgotPasswordHandler forgotPasswordHandler = new ForgotPasswordHandler() {
        @Override
        public void onSuccess() {


            if (newPass != null && code != null) {
                if (!newPass.isEmpty() && !code.isEmpty()) {
                    updateCloudPassword();
                }
            }
        }

        @Override
        public void getResetCode(ForgotPasswordContinuation forgotPasswordContinuation) {
            progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
            progressBar.setVisibility(View.GONE);
            getForgotPasswordCode(forgotPasswordContinuation);
        }

        @Override
        public void onFailure(Exception e) {
            progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity_Signin_Email.this,"Forgot password failed",Toast.LENGTH_LONG).show();

        }
    };

    private void updateCloudPassword() {
        progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
        requestQueue = Volley.newRequestQueue(MainActivity_Signin_Email.this);

        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"updatepassword.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("success")){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity_Signin_Email.this,"Password successfully changed!",Toast.LENGTH_LONG).show();
                            inPassword.setText("");
                            inPassword.requestFocus();
                        }else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity_Signin_Email.this,"Cloud Password change failed!",Toast.LENGTH_LONG).show();
                            inPassword.setText("");
                            inPassword.requestFocus();
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
                params.put("username",inUsername.getText().toString());
                params.put("password", password);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);



    }

    private void getForgotPasswordCode(ForgotPasswordContinuation forgotPasswordContinuation) {
        this.forgotPasswordContinuation = forgotPasswordContinuation;
        Intent intent = new Intent(this, ForgotPasswordActivity_cloud.class);
        intent.putExtra("destination",forgotPasswordContinuation.getParameters().getDestination());
        intent.putExtra("deliveryMed", forgotPasswordContinuation.getParameters().getDeliveryMedium());
        startActivityForResult(intent, 3);
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
                            Toast.makeText(MainActivity_Signin_Email.this,"Setting new password...",Toast.LENGTH_SHORT).show();
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

        progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(MainActivity_Signin_Email.this);
        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"login.php",
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

                            parseJson(jsonObject);
                            progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
                            progressBar.setVisibility(View.GONE);

                        }else{
                            progressBar = (RelativeLayout) findViewById(R.id.progressbar1);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity_Signin_Email.this, "Login failed", Toast.LENGTH_SHORT).show();
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
                params.put("email",inUsername.getText().toString());
                params.put("password",inPassword.getText().toString());
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
        String company="",email="",password="";

        try {
            company=jarray.getString("company");
            email=jarray.getString("email");
            password=jarray.getString("password");
            JSONArray stores=jarray.getJSONArray("store");

            for(int i=0;i<stores.length();i++){

                StoreBean storeBean=new StoreBean();
                JSONObject storeobject=stores.getJSONObject(i);
                String store_name=storeobject.getString("store_name");
                JSONArray devices=storeobject.getJSONArray("devices");
                String[] deviceArray=new String[devices.length()];
                for(int j=0;j<devices.length();j++){
                    deviceArray[j]=devices.getString(j);
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
        editor.putString("password",password);
        editor.commit();

        Intent intent=new Intent(MainActivity_Signin_Email.this,Checking_Store.class);
        intent.putExtra("company",company);
        intent.putExtra("email",email);
        startActivity(intent);
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
                    // Toast.makeText(MainActivity_Signin_Email.this, "permission granted", Toast.LENGTH_SHORT).show();
                    if (!SdIsPresent()) ;

                    Intent registerActivity = new Intent(this, SignupActivity.class);
                    startActivity(registerActivity);


                } else {

                    //   Toast.makeText(MainActivity_Signin_Email.this, "permission denied", Toast.LENGTH_SHORT).show();

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
                    //Toast.makeText(MainActivity_Signin_Email.this, "permission granted", Toast.LENGTH_SHORT).show();
                    System.out.println("permission granted");
                    if (!SdIsPresent()) ;


                } else {

                    Toast.makeText(MainActivity_Signin_Email.this, "permission denied", Toast.LENGTH_SHORT).show();

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


    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity_Signin_Email.this).create();

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
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity_Signin_Email.this).create();

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
}
