package com.intuition.ivepos.mSwipe;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.AmplifyConfiguration;
import com.amplifyframework.hub.AWSHubPlugin;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.intuition.ivepos.SharedPreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.multidex.MultiDexApplication;

public class ApplicationData extends MultiDexApplication
{
    public final static boolean IS_DEBUGGING_ON = true;
    public final static String packName = "com.intuition.ivepos.mSwipe";
    public static final String SERVER_NAME = "Mswipe";

    /*The application global attributes */
    public static final int PhoneNoLength = 10;
    public static final String Currency_Code = "INR.";
    public static final String smsCode = "+91";
    public static final String mTipRequired = "false";
    private static ApplicationData mInstance;

    public Typeface font = null;
    public Typeface fontBold = null;
    public Typeface fontMedium = null;
    public Typeface fontLight = null;
    public Typeface fontLightItalic = null;

    public static String mCurrency = "inr";

    public static final int mCashAtPosAmountLimit = 2000;



    /*The application context statically available to the entire application */
    private static Context mContext = null;

    private static ApplicationData mApplicationData = null;


    public static final int font_size_amount_small = 40 ;
    public static final int font_size_amount_normal = 60;

    public static final int REQUEST_THRESHOLD_TIME = 2000;


    boolean reachable;


    public static SharedPreferences getDefaultSharedPreferencesMultiProcess(
            Context context) {
        return context.getSharedPreferences(
                context.getPackageName() + "_preferences",
                Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
        mContext = getApplicationContext();
        mInstance = this;

        font = Typeface.createFromAsset(mContext.getAssets(),"fonts/english/roboto/Roboto-Regular.ttf");
        fontBold = Typeface.createFromAsset(mContext.getAssets(),"fonts/english/roboto/Roboto-Bold.ttf");
        fontMedium = Typeface.createFromAsset(mContext.getAssets(),"fonts/english/roboto/Roboto-Medium.ttf");
        fontLight = Typeface.createFromAsset(mContext.getAssets(),"fonts/english/roboto/Roboto-Light.ttf");
        fontLightItalic = Typeface.createFromAsset(mContext.getAssets(),"fonts/english/roboto/Roboto-LightItalic.ttf");



        final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

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
                    ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkCapabilities capabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());  // need ACCESS_NETWORK_STATE permission
                    isOnline = capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (isOnline){
                    System.out.println("application internet");
                    reachable = true;
//                    getSecurityKey();
                }else {
                    System.out.println("application1 no internet");
                    reachable = false;

                    SharedPreferences pref = getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
                    boolean isSignedin= pref.getBoolean("signindb", false);

                    System.out.println("splashhh1 "+isSignedin);

                    if(!isSignedin){
                        System.out.println("application1 not signed in");
                    }else {
                        System.out.println("application1 signed in");
                    }

                }

//                    return isOnline;
            }
        }



//        getSecurityKey();

        // Add this line, to include the Auth plugin.
        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSHubPlugin());
        } catch (AmplifyException e) {
            e.printStackTrace();
        }

//        try {
//            JSONObject jsonObject = jsonResult();
//            Amplify.configure(AmplifyConfiguration.fromJson(jsonObject));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        try {
//            JSONObject jsonObject = jsonResult();
//            Amplify.configure(AmplifyConfiguration.fromJson(jsonObject), getApplicationContex());
//            Log.i("MyAmplifyApp", "Initialized Amplify");



//        } catch () {
//            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
//        }

//        try {
//            Amplify.configure(getApplicationContext());
//            Log.i("MyAmplifyApp", "Initialized Amplify");
//        } catch (AmplifyException error) {
//            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
//        }

    }

    public static synchronized ApplicationData getInstance() {
        return mInstance;
    }

    private void getSecurityKey() {

//        Log.d("GetEncryptionKey1_Fun", "SecurityKey");

        RequestQueue queue = Volley.newRequestQueue(getInstance());
        StringRequest sr = new StringRequest(com.android.volley.Request.Method.POST, "https://theandroidpos.com/IVEPOS_NEW/" + "aws_secret_key.php", new com.android.volley.Response.Listener<String>() {
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
                    parseJson(jsonObject);
                } else {
                    Toast.makeText(getInstance(), "Getting Key Failed", Toast.LENGTH_SHORT).show();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
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
//                params.put("email", inUsername.getText().toString());
//                params.put("password", inPassword.getText().toString());
                return params;
            }
        };
    /*    sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        sr.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }


    private void parseJson(JSONObject responseObj) {

        try {
            String encKey = responseObj.getString("encry");
//            Log.d("GetEncryptionKey2:- ", encKey);

            if(encKey.length() != 0){
                getAWSJSONData(encKey);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAWSJSONData(String encKey){
//        Log.d("EncryptKey", encKey);

        RequestQueue requestQueue = Volley.newRequestQueue(getInstance());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://theandroidpos.com/IVEPOS_NEW/" + "aws_values_new.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("AWSJSonDataResponse", response);
                        JSONObject responseObj = null;
                        try {
                            responseObj = new JSONObject(response);
                            getAWSJSONResponseDataValue(responseObj);
//                            Log.d("AWSJSONOBJ", String.valueOf(responseObj));
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
                            Toast.makeText(getInstance(), "Something Went Wrong", Toast.LENGTH_LONG).show();
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
                params.put("encry", encKey);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private static void getAWSJSONResponseDataValue(JSONObject responseObj) {
        try {
            JSONObject responseObjJSONObject = responseObj.getJSONObject("response");
            JSONObject awsFinalJsonObjectData = responseObj.getJSONObject("response");

//            Log.d("AwsJsonObject", String.valueOf(responseObjJSONObject));

            // Add this line, to include the Auth plugin.
            try {
                //SecurityAwsJson
                Amplify.configure(AmplifyConfiguration.fromJson(awsFinalJsonObjectData), getInstance());

                Log.i("MyAmplifyApp", "Initialized Amplify");
            } catch (AmplifyException error) {
                Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The  context of the application that can be accesses
     * @param
     *
     * @return
     * the application context
     */
    public static Context getApplicationContex()
    {
        return mContext;

    }

    /**
     * single instance.
     * @return
     */
    public static ApplicationData getApplicationDataSharedInstance()
    {
        if(mApplicationData == null)
        {
            mApplicationData = new ApplicationData();
        }
        return mApplicationData;
    }


}





