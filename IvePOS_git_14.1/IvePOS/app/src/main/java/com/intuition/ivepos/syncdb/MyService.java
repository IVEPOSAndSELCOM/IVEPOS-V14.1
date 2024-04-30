package com.intuition.ivepos.syncdb;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.intuition.ivepos.R;
import com.intuition.ivepos.SplashScreenActivity_Selection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import androidx.core.app.NotificationCompat;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

public class MyService extends Service {
    public SQLiteDatabase db1 = null;
    public static int pagebillnumber=1;
    public static  int pageallsales=1;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static OnProgressUpdateListener progressListener;
    int tableIndex=0;
    ArrayList<String> tableList = new ArrayList<String>();
    public static void setOnProgressChangedListener(OnProgressUpdateListener _listener) {
        progressListener = _listener;
    }
    RequestQueue queue;

    String WebserviceUrl;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {

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

        //  Toast.makeText(this, " MyService Created ", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //  Toast.makeText(this, " MyService Started", Toast.LENGTH_LONG).show();

        Log.e("myservice",getString(R.string.Button9));
        final int currentId = startId;
        pagebillnumber=1;
        pageallsales=1;

        String input = "salesdata";
        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("initializing")
                .setContentText(input)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        startForeground(1, notification);

        Runnable r = new Runnable() {
            public void run() {
                tableList.clear();
                db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor c = db1.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
                if (c.moveToFirst()) {

                    Log.e("myservice","c.moveToFirst()");
                    while ( !c.isAfterLast() ) {
                        tableList.add(c.getString(0));
                        c.moveToNext();

                        Log.e("myservice"," while ( !c.isAfterLast() ) ");
                        if(c.getString(0).toLowerCase().contains("table")){
                            Log.e("myservice","contains(table)");
                            if(c.getString(0).equalsIgnoreCase("Tablepayment")){
                                tableList.add(c.getString(0));
                                c.moveToNext();
                            }

                        }else{
                            Log.e("myservice","else");
                            Log.e("myservice",c.getString(0));
                            tableList.add(c.getString(0));
                            c.moveToNext();
                       }

                    }
                }
                //downloadAppdata();
                tableIndex=0;
                downloadSalesdataAllsales(tableList.get(tableIndex));

            }
        };

        Thread t = new Thread(r);
        t.start();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
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

    public void downloadSalesdata(){

        int myDays = 60;

        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, (myDays * -1));  // number of days to subtract
        int newDate =     (c.get(Calendar.YEAR) * 10000) +
                ((c.get(Calendar.MONTH) + 1) * 100) +
                (c.get(Calendar.DAY_OF_MONTH));
        String newDate1 = String.valueOf(newDate)+"0000";
        System.out.println("date is "+newDate1);

        SharedPreferences sharedpreferences=getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        String company= sharedpreferences.getString("companyname", null);
        String store=  sharedpreferences.getString("storename", null);
        String device= sharedpreferences.getString("devicename", null);
        JSONObject params = new JSONObject();

        try {
            params.put("device",device);
            params.put("store",store);
            params.put("company",company);
            params.put("date1", newDate1);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue queue = Volley.newRequestQueue(MyService.this);
        JsonObjectRequest sr = new JsonObjectRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"tdownloadsalesdata.php",params,
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

                            parseJsonSales(jsonObject);

                        }else{
                            Toast.makeText(MyService.this, "download failed", Toast.LENGTH_SHORT).show();
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


    public void parseJsonSales(JSONObject jsonObject){

        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        try {
            JSONArray jsonArray=jsonObject.getJSONArray("salesdata");
            for(int i=0;i<jsonArray.length();i++){

                JSONObject tableObject=jsonArray.getJSONObject(i) ;
                String tableName=tableObject.getString("table");
                JSONArray dataJsonArray=tableObject.getJSONArray("data");
                for(int j=0;j<dataJsonArray.length();j++){
                    JSONObject rowObject=dataJsonArray.getJSONObject(j);
                    Iterator<?> keys = rowObject.keys();
                    ContentValues cv=new ContentValues();
                    while( keys.hasNext() ) {
                        String key = (String)keys.next();
                        if (!rowObject.isNull(key)){
                            cv.put(key,rowObject.getString(key));
                        }

                    }

                    db1.insert(tableName, null, cv);
                }

                progressListener.onProgressUpdateSales((1));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
           db1.close();

            downloadSalesdataBillnumber();

        }

    }

    private void publishResults() {
        Intent intent = new Intent("com.intuition.ivepos.Checking_Store.receiver");
        sendBroadcast(intent);
    }

    public interface OnProgressUpdateListener {

        void onProgressUpdateSales(int progress);
    }



    public void downloadSalesdataBillnumber(){

        int myDays = 60;

        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, (myDays * -1));  // number of days to subtract
        int newDate =     (c.get(Calendar.YEAR) * 10000) +
                ((c.get(Calendar.MONTH) + 1) * 100) +
                (c.get(Calendar.DAY_OF_MONTH));
        String newDate1 = String.valueOf(newDate)+"0000";
        System.out.println("date is "+newDate1);

        SharedPreferences sharedpreferences=getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        String company= sharedpreferences.getString("companyname", null);
        String store=  sharedpreferences.getString("storename", null);
        String device= sharedpreferences.getString("devicename", null);
        JSONObject params = new JSONObject();

        try {
            params.put("device",device);
            params.put("store",store);
            params.put("company",company);
            params.put("date1", newDate1);
            params.put("table", "billnumber");
            params.put("page", pagebillnumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue queue = Volley.newRequestQueue(MyService.this);
        JsonObjectRequest sr = new JsonObjectRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"pagination_sdata.php",params,
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

                            parseJsonSalesBillnumber(jsonObject);

                        }else if(response.equalsIgnoreCase("over")){
                            progressListener.onProgressUpdateSales((1));
                            //downloadSalesdataAllsales();

                        }else{
                            Toast.makeText(MyService.this, "download failed", Toast.LENGTH_SHORT).show();
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

    public void parseJsonSalesBillnumber(JSONObject jsonObject){

        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        try {
            JSONArray jsonArray=jsonObject.getJSONArray("salesdata");
            for(int i=0;i<jsonArray.length();i++){

                JSONObject tableObject=jsonArray.getJSONObject(i) ;
                String tableName=tableObject.getString("table");
                JSONArray dataJsonArray=tableObject.getJSONArray("data");
                for(int j=0;j<dataJsonArray.length();j++){
                    JSONObject rowObject=dataJsonArray.getJSONObject(j);
                    Iterator<?> keys = rowObject.keys();
                    ContentValues cv=new ContentValues();
                    while( keys.hasNext() ) {
                        String key = (String)keys.next();
                        if (!rowObject.isNull(key)){
                            cv.put(key,rowObject.getString(key));
                        }

                    }

                    db1.insert(tableName, null, cv);
                }

                progressListener.onProgressUpdateSales((1));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            db1.close();
            pagebillnumber=pagebillnumber+1;
            downloadSalesdataBillnumber();

        }

    }

    public void parseJsonSalesAllsales(JSONObject jsonObject,String table){

        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        try {
            JSONArray jsonArray=jsonObject.getJSONArray("salesdata");
            for(int i=0;i<jsonArray.length();i++){

                JSONObject tableObject=jsonArray.getJSONObject(i) ;
                String tableName=tableObject.getString("table");
                JSONArray dataJsonArray=tableObject.getJSONArray("data");
                for(int j=0;j<dataJsonArray.length();j++){
                    JSONObject rowObject=dataJsonArray.getJSONObject(j);
                    Iterator<?> keys = rowObject.keys();
                    ContentValues cv=new ContentValues();
                    while( keys.hasNext() ) {
                        String key = (String)keys.next();
                        if (!rowObject.isNull(key)){
                            cv.put(key,rowObject.getString(key));
                        }

                    }

                    db1.insert(tableName, null, cv);
                }

                progressListener.onProgressUpdateSales((1));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            db1.close();
            pageallsales=pageallsales+1;
            Log.e("downloadSa",table);
            downloadSalesdataAllsales(table);

        }

    }

    public void downloadSalesdataAllsales(final String table){

        Log.e("myservice",getString(R.string.Button10));

        int myDays = 60;

        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, (myDays * -1));  // number of days to subtract
        int newDate =     (c.get(Calendar.YEAR) * 10000) +
                ((c.get(Calendar.MONTH) + 1) * 100) +
                (c.get(Calendar.DAY_OF_MONTH));
        String newDate1 = String.valueOf(newDate)+"0000";
        System.out.println("date is "+newDate1);

        SharedPreferences sharedpreferences=getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        String company= sharedpreferences.getString("companyname", null);
        String store=  sharedpreferences.getString("storename", null);
        String device= sharedpreferences.getString("devicename", null);
        JSONObject params = new JSONObject();

        try {
            params.put("device",device);
            params.put("store",store);
            params.put("company",company);
            params.put("date1", newDate1);
            params.put("page", pageallsales);
            params.put("table", table);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(queue==null){
            queue = Volley.newRequestQueue(MyService.this);
        }

        JsonObjectRequest sr = new JsonObjectRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"pagination_sdata.php",params,
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
                            Log.e("myservice-table",table);

                            parseJsonSalesAllsales(jsonObject,table);

                        }else if(response.equalsIgnoreCase("over")){
                            progressListener.onProgressUpdateSales((1));
                            tableIndex++;

                            if(tableIndex<tableList.size()){
                                Log.e("tablename",tableList.get(tableIndex));
                                pageallsales=1;
                                downloadSalesdataAllsales(tableList.get(tableIndex));
                            }else{
                                Log.e("publish results",getString(R.string.Button9));
                                publishResults();
                                stopSelf();
                            }

                        }else{
                            Toast.makeText(MyService.this, "download failed", Toast.LENGTH_SHORT).show();
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