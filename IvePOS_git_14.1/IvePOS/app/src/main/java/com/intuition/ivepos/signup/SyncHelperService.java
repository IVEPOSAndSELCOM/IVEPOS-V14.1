package com.intuition.ivepos.signup;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.intuition.ivepos.SplashScreenActivity_Selection;
import com.intuition.ivepos.sync.SyncHelper;
import com.intuition.ivepos.syncapp.SyncHelperApp;
import com.intuition.ivepos.syncdb.SyncDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

/**
 * Created by HP on 1/14/2019.
 */

public class SyncHelperService extends IntentService {
    String backup="";
    RequestQueue requestQueue ;

    String WebserviceUrl;

    public SyncHelperService() {
        super("SyncHelperService");


    }

    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(Intent intent) {


         backup=intent.getStringExtra("backup");

        requestQueue = Volley.newRequestQueue(this);

        SharedPreferences sharedpreferences =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        String account_selection= sharedpreferences.getString("account_selection", null);

        if (account_selection.toString().equals("Dine")) {
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }else {
            if (account_selection.toString().equals("Qsr")) {
                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
            }else {
                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
            }
        }

        if(backup.equalsIgnoreCase("appdata")){
            deleteAppdata();
        }

        if(backup.equalsIgnoreCase("salesdata")){
            deleteAllSalesData();
        }

        if(backup.equalsIgnoreCase("both")){
            deleteAppdata();
            deleteAllSalesData();
        }





    }


    public void deleteAppdata(){

        SharedPreferences sharedpreferences=getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        String company= sharedpreferences.getString("companyname", null);
        String store= sharedpreferences.getString("storename", null);
        String device= sharedpreferences.getString("devicename", null);

        SharedPreferences.Editor editor =getDefaultSharedPreferencesMultiProcess(getApplicationContext()).edit();
        editor.putString("storename",store);
        editor.putString("devicename",device);
        editor.apply();

        JSONObject params = new JSONObject();

        try {
            params.put("device",device);
            params.put("store",store);
            params.put("company",company);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue queue = Volley.newRequestQueue(SyncHelperService.this);
        JsonObjectRequest sr = new JsonObjectRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"deleteallappdata.php",params,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responseString) {
                        String response= "";
                        JSONObject jsonObject=null;
                        try {
                            jsonObject=responseString;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            response = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(response.equalsIgnoreCase("success")){

                            SyncDatabase syncdatabase=new SyncDatabase();
                            syncdatabase.clearSyncDbApp(getApplicationContext());

                            SyncHelperApp syncHelperapp=new SyncHelperApp();
                            syncHelperapp.tablesync(getApplicationContext());
                            syncHelperapp.beginPeriodicSync(20,getApplicationContext(),null);





                        }else{
                            Toast.makeText(SyncHelperService.this, "download failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                })  {

        };
/*    sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        sr.setRetryPolicy(new DefaultRetryPolicy(0,-1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    private void deleteAllSalesData() {

        SharedPreferences sharedpreferences=getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        String company= sharedpreferences.getString("companyname", null);
        String store= sharedpreferences.getString("storename", null);
        String device= sharedpreferences.getString("devicename", null);

        SharedPreferences.Editor editor =getDefaultSharedPreferencesMultiProcess(getApplicationContext()).edit();
        editor.putString("storename",store);
        editor.putString("devicename",device);
        editor.apply();

        JSONObject params = new JSONObject();

        try {
            params.put("device",device);
            params.put("store",store);
            params.put("company",company);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue queue = Volley.newRequestQueue(SyncHelperService.this);
        JsonObjectRequest sr = new JsonObjectRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"deleteallsalesdata.php",params,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responseString) {
                        String response= "";
                        JSONObject jsonObject=null;
                        try {
                            jsonObject=responseString;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            response = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(response.equalsIgnoreCase("success")){


                            SyncDatabase syncdatabase=new SyncDatabase();
                            syncdatabase.clearSyncDb(getApplicationContext());

                            SyncHelper syncHelper=new SyncHelper();
                            syncHelper.tablesync(getApplicationContext());
                            syncHelper.beginPeriodicSync(20,getApplicationContext(),null);



               }else{
                            Toast.makeText(SyncHelperService.this, "download failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                })  {

        };
/*    sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        sr.setRetryPolicy(new DefaultRetryPolicy(0,-1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);



    }


}
