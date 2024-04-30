package com.intuition.ivepos.deleteitems;


import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.intuition.ivepos.Country_items;
import com.intuition.ivepos.R;
import com.intuition.ivepos.SplashScreenActivity_Selection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//import androidx.annotation.NonNull;
//import androidx.core.app.NotificationCompat;
//import androidx.work.Worker;
//import androidx.work.WorkerParameters;

public class MyWorker extends Worker {
    RequestQueue queue;
    JsonObjectRequest sr;
    public static boolean flag=false;
    public static  int j=0;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public static SparseBooleanArray sbarray;
    public static int count;
    public static ArrayList<Country_items> countryList;
    SQLiteDatabase db;
    public static ArrayList<String> delList= new ArrayList<String>();

    String WebserviceUrl;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        SharedPreferences sharedpreferences =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(context);
        String account_selection= sharedpreferences.getString("account_selection", null);

        if (account_selection.toString().equals((R.string.dine_nospace))) {
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }else {
            if (account_selection.toString().equals((R.string.qsr_nospace))) {
                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
            }else {
                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
            }
        }
    }

    /*
     * This method is responsible for doing the work
     * so whatever work that is needed to be performed
     * we will put it here
     *
     * For example, here I am calling the method displayNotification()
     * It will display a notification
     * So that we will understand the work is executed
     * */

    @NonNull
    @Override
    public Result doWork() {
        delList.clear();
        queue = Volley.newRequestQueue(getApplicationContext());

        flag=false;

        Log.e("before service start","before service start001");

        Log.e("before service start","before service start4");




        deleteitems();
        j=0;
        flag=true;

        // displayNotification("My Worker", "Hey I finished my work");
        return Result.success();
    }

    /*
     * The method is doing nothing but only generating
     * a simple notification
     * If you are confused about it
     * you should check the Android Notification Tutorial
     * */
    private void displayNotification(String title, String task) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("IVEPOS Retail", "IVEPOS Retail", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "IVEPOS Retail")
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(1, notification.build());
    }


    public void deleteitems(){
        try {

            Log.e("before service start","before service start6");

            db = getApplicationContext().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            int len = count;
            SparseBooleanArray checked = sbarray;
            for (int i = j; i < len; i++) {
                if (checked.get(i)) {

                    Country_items country = countryList.get(i);
                    String an = country.getCode();
                    String an_name = country.getName();

                    /* do whatever you want with the checked item */
                    String where = "_id = '" + an + "' ";
                    Log.e("before service start",an);


//                  Log.e("before service start","before service start7");
//                  contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                  getContentResolver().delete(contentUri, where, new String[]{});
//                  resultUri = new Uri.Builder()
//                          .scheme("content")
//                          .authority(StubProviderApp.AUTHORITY)
//                          .path("Items")
//                          .appendQueryParameter("operation", "delete")
//                          .appendQueryParameter("_id", an)
//                          .build();
//                  getContentResolver().notifyChange(resultUri, null);

                    delList.add(an);

                    db.delete("Items", where, new String[]{});

                    String where1_v1 = "itemname = '" + an_name + "' ";
                    db.delete("Items_Virtual", where1_v1, new String[]{});
                    j=i;
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }

        deleteItem(delList);

    }


    public void deleteItem(ArrayList<String> delList){


        SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        String company= sharedpreferences.getString("companyname", null);
        String store= sharedpreferences.getString("storename", null);
        String device= sharedpreferences.getString("devicename", null);

        JSONObject params = new JSONObject();

        try {
            params.put("device",device);
            params.put("store",store);
            params.put("company",company);

            JSONArray jsonArray = new JSONArray();
            for (int i=0; i < delList.size(); i++) {
                jsonArray.put(delList.get(i));
            }
            params.put("id",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        sr = new JsonObjectRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"deletemultiple.php",params,
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


                        }else{
                            Toast.makeText(getApplicationContext(), "delete failed", Toast.LENGTH_SHORT).show();
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
