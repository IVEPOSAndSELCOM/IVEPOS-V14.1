package com.intuition.ivepos.signup;

/**
 * Created by HP on 1/11/2019.
 */

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.intuition.ivepos.SplashScreenActivity;

import java.util.HashMap;
import java.util.Map;

public class DownloadService extends IntentService {

    String company,store,device;
    RequestQueue requestQueue ;

    String WebserviceUrl;

    public DownloadService() {
        super("DownloadService");
    }

    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(Intent intent) {

        requestQueue = Volley.newRequestQueue(this);

        company=intent.getStringExtra("company");
        store=intent.getStringExtra("store");
        device=intent.getStringExtra("device");
        //emailid=intent.getStringExtra("emailid");

        Log.d("downloadserv","inside onhandle");

        SharedPreferences sharedpreferences =  SplashScreenActivity.getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        String account_selection= sharedpreferences.getString("account_selection", null);

        TextView tv = new TextView(getApplicationContext());
        tv.setText(account_selection);

        if (tv.getText().toString().equals("Dine")) {
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }else {
            if (tv.getText().toString().equals("Qsr")) {
                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
            }else {
                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
            }
        }

        createappdata();
        createsalesdata();
    }



    private void createappdata() {
        Log.d("downloadserv","inside createappdata");
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"createappdata.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("downloadserv","inside response1");
                        if(response.equalsIgnoreCase("success")){
                            Log.d("response",response);
                            Log.d("downloadserv","success1");
                        }else{
                            Log.d("downloadserv","failed1");
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


                Log.d("downloadserv",company);
                Log.d("downloadserv",store);
                Log.d("downloadserv",device);
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

        Log.d("downloadserv","inside createsalesdata");
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"createsalesdata.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("downloadserv","response2");
                        if(response.equalsIgnoreCase("success")){

                           publishResults();

                            Log.d("response",response);
                            Log.d("downloadserv","success2");

                        }else{
                            Log.d("downloadserv","failed2");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        publishResults();
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



    private void publishResults() {
        Intent intent = new Intent("com.intuition.ivepos.createdata.receiver");
        sendBroadcast(intent);
    }




}