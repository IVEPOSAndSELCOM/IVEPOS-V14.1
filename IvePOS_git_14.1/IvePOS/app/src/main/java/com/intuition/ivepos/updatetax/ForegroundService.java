package com.intuition.ivepos.updatetax;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.intuition.ivepos.Country_items;
import com.intuition.ivepos.MultiFragDatabaseActivity;
import com.intuition.ivepos.SplashScreenActivity_Selection;
import com.intuition.ivepos.deleteitems.Restarter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public static SparseBooleanArray sbarray;
    public static int count;
    public static ArrayList<Country_items> countryList;
    SQLiteDatabase db;
    Uri contentUri,resultUri;
    public static boolean flag=false;
    public static  int j=0;

    String WebserviceUrl;

    @Override
    public void onCreate() {
        super.onCreate();

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

        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
            Intent notificationIntent = new Intent(this, MultiFragDatabaseActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            PendingIntent pendingIntent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
            }
            else {
                pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
            }
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Deleting items")
                    .setContentText("Deleting items")
                    .setContentIntent(pendingIntent)
                    .build();

            Log.e("before service start", "before service start002");
            startForeground(1, notification);

        }else{
            startForeground(1, new Notification());
        }


    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        flag=false;

        Log.e("before service start","before service start001");

        Log.e("before service start","before service start4");

        String input = intent.getStringExtra("inputExtra");
        if(input==null){
            startTimer();
        }else{

            if(input.equalsIgnoreCase("items delete service")){
                sbarray = (SparseBooleanArray) intent.getParcelableExtra("checked");
                count = intent.getIntExtra("count",1);
                countryList = (ArrayList<Country_items>) intent.getSerializableExtra("country");

                startTimer();
            }else{

                startTimer();
            }

        }

        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        stoptimertask();

        if(flag==false){
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(this, Restarter.class);
            this.sendBroadcast(broadcastIntent);
        }



    }


    private Timer timer;
    private TimerTask timerTask;
    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {

                final Thread t2=new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Log.e("before service start","before service start5");
                        deleteitems();
                        j=0;
                        flag=true;
                        stoptimertask();
                        Intent intent1 = new Intent();
                        sendBroadcast(intent1);
                        Log.e("before service start","before service start8");

                        stopSelf();



                    }
                },"t2");
                t2.start();

            }
        };
        timer.schedule(timerTask, 1000); //
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }


    public void deleteitems(){
        try {

            Log.e("before service start","before service start6");

            db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            int len = count;
            SparseBooleanArray checked = sbarray;
            for (int i = j; i < len; i++) {
                if (checked.get(i)) {

                    Country_items country = countryList.get(i);
                    String an = country.getCode();
                    String an_name = country.getName();

                    /* do whatever you want with the checked item */
                    String where = "_id = '" + an + "' ";


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


                    deleteItem(an);
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

    }


    public void deleteItem(String an){


        SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(ForegroundService.this);
        String company= sharedpreferences.getString("companyname", null);
        String store= sharedpreferences.getString("storename", null);
        String device= sharedpreferences.getString("devicename", null);

        JSONObject params = new JSONObject();

        try {
            params.put("device",device);
            params.put("store",store);
            params.put("company",company);
            params.put("id",an);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue queue = Volley.newRequestQueue(ForegroundService.this);
        JsonObjectRequest sr = new JsonObjectRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"deletesingle.php",params,
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
                            Toast.makeText(ForegroundService.this, "delete failed", Toast.LENGTH_SHORT).show();
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