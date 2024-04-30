package com.intuition.ivepos.firebase;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.intuition.ivepos.R;
import com.intuition.ivepos.SplashScreenActivity_Selection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

public class MyService  extends IntentService
{
    public static final String TAG = MyService.class.getSimpleName();

    String WebserviceUrl;

    public MyService()
    {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {

        SharedPreferences sharedpreferences =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        String account_selection= sharedpreferences.getString("account_selection", null);

        if (account_selection.toString().equals(getString(R.string.dine_nospace))) {
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }else {
            if (account_selection.toString().equals(getString(R.string.qsr_nospace))) {
                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
            }else {
                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
            }
        }

        storeRegIdInPref("");

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
//                            String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, token);

                        storeRegIdInPref(token);
                        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
                        registrationComplete.putExtra("token", token);
                        LocalBroadcastManager.getInstance(MyService.this).sendBroadcast(registrationComplete);
                        firebaseRegistration();
//                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

//            FirebaseInstanceId.getInstance().deleteInstanceId();
//            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//            storeRegIdInPref(refreshedToken);
//            Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
//            registrationComplete.putExtra("token", refreshedToken);
//            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        // Saving reg id to shared preferences



    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }

    public void firebaseRegistration(){

        SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        final String company= sharedpreferences.getString("companyname", null);
        final String store= sharedpreferences.getString("storename", null);
        final String device= sharedpreferences.getString("devicename", null);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        final String regId = pref.getString("regId", null);
        Log.e("firebase", "sendRegistrationToServer: " + regId);
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"firebaseregid.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")) {

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
                params.put("regid",regId);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

    }
}
