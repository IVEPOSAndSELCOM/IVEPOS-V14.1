package com.intuition.ivepos;

/**
 * Created by HP on 10/3/2018.
 */

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.intuition.ivepos.csv.ForegroundService;
import com.intuition.ivepos.csv.UpdateTaxeService;
import com.intuition.ivepos.firebase.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;


/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    String WebserviceUrl;

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token123: " + token);



        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        storeRegIdInPref(token);
        sendRegistrationToServer(token);
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        /*if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }*/

        // Check if message contains a data payload.

      /*  NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.playNotificationSound();*/
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    public void startService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Processing");

        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void startServiceTaxes() {
        Intent serviceIntent = new Intent(this, UpdateTaxeService.class);
        serviceIntent.putExtra("inputExtra", "Processing");

        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");
            String query="INSERT INTO items(_id,itemname,price,stockquan,category,itemtax,image,favourites,disc_type,disc_value,image_text,barcode_value,quantity_sold,minimum_qty,minimum_qty_copy,add_qty,unit_type,tax_value,itemtax2,tax_value2,itemtax3,tax_value3,itemtax4,tax_value4,itemtax5,tax_value5,variant2,variant_price2,variant3,variant_price3,variant4,variant_price4,variant5,variant_price5) VALUES";

            if (data.has("message1")) {

                boolean foregroud = new ForegroundCheckTask().execute(getApplicationContext()).get();

                if(foregroud){
                    Intent intent = new Intent("myFunction");
                    intent.putExtra("items","items");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }

                startService();
//                SQLiteDatabase db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//                String message1 = data.getString("message1");
//                db.execSQL(query+message1);
//                String message2 = data.getString("message2");
//                db.execSQL(query+message2);
//                String message3 = data.getString("message3");
//                db.execSQL(query+message3);
//                String message4 = data.getString("message4");
//                db.execSQL(query+message4);
//                String message5 = data.getString("message5");
//                db.execSQL(query+message5);
//                String message6 = data.getString("message6");
//                db.execSQL(query+message6);
//                String message7 = data.getString("message7");
//                db.execSQL(query+message7);
//                String message8 = data.getString("message8");
//                db.execSQL(query+message8);
//                String message9 = data.getString("message9");
//                db.execSQL(query+message9);
            }else if(data.has("message2")) {
                boolean foregroud = new ForegroundCheckTask().execute(getApplicationContext()).get();

                if(foregroud){
                    Intent intent = new Intent("myFunction");
                    intent.putExtra("items","items");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }
                startServiceTaxes();
            }else{
                String message = data.getString("message");
                System.out.println("message is "+message);
                SQLiteDatabase db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                SQLiteDatabase db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                if(message.equalsIgnoreCase("delete from items")){

                }


                if ((message.contains("customerdetails")) || (message.contains("cust_feedback"))){
                    db1.execSQL(message);
                }else {
                    if (message.contains("entity")) {
                        Intent intent = new Intent("myFunction1");
                        intent.putExtra("items1",message);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    }else {
                        db.execSQL(message);
                    }
                }
            }



        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }

        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        final String company= sharedpreferences.getString("companyname", null);
        final String store= sharedpreferences.getString("storename", null);
        final String device= sharedpreferences.getString("devicename", null);
        if((company!=null)&&(store!=null)&&(device!=null)){
            firebaseRegistration();
        }

    }

    public void firebaseRegistration(){

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        String account_selection= sharedpreferences_select.getString("account_selection", null);

        if (account_selection.toString().equals("Dine")) {
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }else {
            if (account_selection.toString().equals("Qsr")) {
                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
            }else {
                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
            }
        }

        SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        final String company= sharedpreferences.getString("companyname", null);
        final String store= sharedpreferences.getString("storename", null);
        final String device= sharedpreferences.getString("devicename", null);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        final String regId = pref.getString("regId", null);
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
