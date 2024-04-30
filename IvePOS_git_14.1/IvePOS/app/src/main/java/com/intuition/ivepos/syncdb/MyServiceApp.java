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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import androidx.core.app.NotificationCompat;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

public class MyServiceApp extends Service {


    public SQLiteDatabase db1 = null;
    private SQLiteDatabase db;
    private static com.intuition.ivepos.syncdb.MyServiceApp.OnProgressUpdateListener progressListener;
    public static int page =1;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    ArrayList<String> tableList = new ArrayList<String>();
    int tableIndex=0;
    public static void setOnProgressChangedListener(com.intuition.ivepos.syncdb.MyServiceApp.OnProgressUpdateListener _listener) {
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
        final int currentId = startId;
        page =1;
        String input = "appdata";
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
                db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
                if (c.moveToFirst()) {
                    while ( !c.isAfterLast() ) {
                        tableList.add(c.getString(0));
                        c.moveToNext();
                    }
                }
                //downloadAppdata();
                tableIndex=0;
                downloadAppdataitems(tableList.get(tableIndex));

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




    public void downloadAppdata(){

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


        RequestQueue queue = Volley.newRequestQueue(com.intuition.ivepos.syncdb.MyServiceApp.this);
        JsonObjectRequest sr = new JsonObjectRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"tdownloadappdata.php",params,
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

                            parseJson(jsonObject);

                        }else{
                            Toast.makeText(com.intuition.ivepos.syncdb.MyServiceApp.this, "download failed", Toast.LENGTH_SHORT).show();
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



    public void parseJson(JSONObject jsonObject){

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        try {
            JSONArray jsonArray=jsonObject.getJSONArray("appdata");
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


                        if ((tableName.equalsIgnoreCase("items"))&&(key.equalsIgnoreCase("image"))){


                            String encodedImage=rowObject.getString(key) ;
                            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            if(decodedByte==null){
                                cv.put("image", "");
                            }else{
                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, bos);
                                byte[]  img = bos.toByteArray();
                                cv.put("image", img);
                            }


                        }else   if ((tableName.equalsIgnoreCase("items1"))&&(key.equalsIgnoreCase("image"))){


                            String encodedImage=rowObject.getString(key) ;
                            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            if(decodedByte==null){
                                cv.put("image", "");
                            }else{
                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, bos);
                                byte[]  img = bos.toByteArray();
                                cv.put("image", img);
                            }


                        }else   if ((tableName.equalsIgnoreCase("Logo"))&&(key.equalsIgnoreCase("companylogo"))){


                            String encodedImage=rowObject.getString(key) ;
                            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            if(decodedByte==null){
                                cv.put("companylogo", "");
                            }else{
                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, bos);
                                byte[]  img = bos.toByteArray();
                                cv.put("companylogo", img);
                            }


                        }else   if ((tableName.equalsIgnoreCase("Modifiers"))&&(key.equalsIgnoreCase("modimage"))){


                            String encodedImage=rowObject.getString(key) ;
                            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            if(decodedByte==null){
                                cv.put("modimage", "");
                            }else{
                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, bos);
                                byte[]  img = bos.toByteArray();
                                cv.put("modimage", img);
                            }


                        }else   if ((tableName.equalsIgnoreCase("asd1"))&&(key.equalsIgnoreCase("image"))){


                            String encodedImage=rowObject.getString(key) ;
                            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            if(decodedByte==null){
                                cv.put("image", "");
                            }else{
                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, bos);
                                byte[]  img = bos.toByteArray();
                                cv.put("image", img);
                            }


                        }else{
                            if (!rowObject.isNull(key)){
                                cv.put(key,rowObject.getString(key));
                            }
                        }
                    }

                    db.insert(tableName, null, cv);
                }

                progressListener.onProgressUpdate((1));


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            db.close();
            downloadAppdataitems("items");

        }

    }

    private void publishResults() {
        Intent intent = new Intent("com.intuition.ivepos.Checking_Store.receiverapp");
        sendBroadcast(intent);
    }



    public interface OnProgressUpdateListener {

        void onProgressUpdate(int progress);
    }


    public void downloadAppdataitems(final String table){
        Log.e("myserviceapp",getString(R.string.Button9));

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
            params.put("table",table);
            params.put("page",page);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(table.equalsIgnoreCase("Ingredient_sold_details")){
            Log.e("Ingredient_sold_details",getString(R.string.Button9));
        }

        if(queue==null){
            queue = Volley.newRequestQueue(com.intuition.ivepos.syncdb.MyServiceApp.this);
        }

        JsonObjectRequest sr = new JsonObjectRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"pagination.php",params,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responseString) {
                        Log.e("myserviceapp",getString(R.string.Button10));
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

                            parseJsonItems(jsonObject,table);

                        }else if(response.equalsIgnoreCase("over")){
                            progressListener.onProgressUpdate((1));
                            tableIndex++;
                            if(tableIndex<tableList.size()){
                                Log.e("tablename",tableList.get(tableIndex));
                                page=1;
                                downloadAppdataitems(tableList.get(tableIndex));
                            }else{
                                Log.e("myserviceapp","publishresults");
                                publishResults();
                                stopSelf();
                            }

                        }else{
                            Toast.makeText(com.intuition.ivepos.syncdb.MyServiceApp.this, "download failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm1", "Error: " + error.getMessage());
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


    public void parseJsonItems(JSONObject jsonObject,String table){

        Log.e("myserviceapp",getString(R.string.Button11));
        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        try {
            JSONArray jsonArray=jsonObject.getJSONArray("appdata");
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


                        if ((tableName.equalsIgnoreCase("items"))&&(key.equalsIgnoreCase("image"))){


                            String encodedImage=rowObject.getString(key) ;
                            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            if(decodedByte==null){
                                cv.put("image", "");
                            }else{
                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, bos);
                                byte[]  img = bos.toByteArray();
                                cv.put("image", img);
                            }


                        }else   if ((tableName.equalsIgnoreCase("items1"))&&(key.equalsIgnoreCase("image"))){


                            String encodedImage=rowObject.getString(key) ;
                            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            if(decodedByte==null){
                                cv.put("image", "");
                            }else{
                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, bos);
                                byte[]  img = bos.toByteArray();
                                cv.put("image", img);
                            }


                        }else   if ((tableName.equalsIgnoreCase("Logo"))&&(key.equalsIgnoreCase("companylogo"))){


                            String encodedImage=rowObject.getString(key) ;
                            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            if(decodedByte==null){
                                cv.put("companylogo", "");
                            }else{
                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, bos);
                                byte[]  img = bos.toByteArray();
                                cv.put("companylogo", img);
                            }


                        }else   if ((tableName.equalsIgnoreCase("Modifiers"))&&(key.equalsIgnoreCase("modimage"))){


                            String encodedImage=rowObject.getString(key) ;
                            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            if(decodedByte==null){
                                cv.put("modimage", "");
                            }else{
                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, bos);
                                byte[]  img = bos.toByteArray();
                                cv.put("modimage", img);
                            }


                        }else   if ((tableName.equalsIgnoreCase("asd1"))&&(key.equalsIgnoreCase("image"))){


                            String encodedImage=rowObject.getString(key) ;
                            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            if(decodedByte==null){
                                cv.put("image", "");
                            }else{
                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, bos);
                                byte[]  img = bos.toByteArray();
                                cv.put("image", img);
                            }


                        }else{
                            if (!rowObject.isNull(key)){
                                cv.put(key,rowObject.getString(key));
                            }
                        }
                    }

                    db.insert(tableName, null, cv);
                }

                progressListener.onProgressUpdate((1));


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            db.close();
            page=page+1;
            Log.e("page","page-"+table);
            downloadAppdataitems(table);

        }

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


}