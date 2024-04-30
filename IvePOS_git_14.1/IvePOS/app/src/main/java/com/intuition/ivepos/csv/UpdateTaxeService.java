package com.intuition.ivepos.csv;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.intuition.ivepos.MultiFragDatabaseActivity;
import com.intuition.ivepos.R;
import com.intuition.ivepos.SplashScreenActivity_Selection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

public class UpdateTaxeService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private SQLiteDatabase mydbappdata;

    String WebserviceUrl;

    @Override
    public void onCreate() {
        super.onCreate();

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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MultiFragDatabaseActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
        }
        else {
            pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        }

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Item taxes updating")
                .setContentText(input)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        Runnable r = new Runnable() {
            public void run() {
                downloadItems();

            }
        };

        Thread t = new Thread(r);
        t.start();


        // stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    public void downloadItems(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        String company = sharedpreferences.getString("companyname", null);
        String store = sharedpreferences.getString("storename", null);
        String device = sharedpreferences.getString("devicename", null);


        JSONObject params = new JSONObject();
        try {
            params.put("company", company);
            params.put("store", store);
            params.put("device", device);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, WebserviceUrl + "getcsvitems.php", params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        String response = null;
                        try {
                            response = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (response.equalsIgnoreCase("success")) {

                            try {
                                JSONArray jsonArray=jsonObject.getJSONArray("items");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    String query="Update Items set itemtax='"+jsonObject1.getString("itemtax")+"', tax_value='"+jsonObject1.getString("tax_value")+"', " +
                                            "itemtax2='"+jsonObject1.getString("itemtax2")+"', tax_value2='"+jsonObject1.getString("tax_value2")+"'," +
                                            "itemtax3='"+jsonObject1.getString("itemtax3")+"', tax_value3='"+jsonObject1.getString("tax_value3")+"', " +
                                            "itemtax4='"+jsonObject1.getString("itemtax4")+"'," +
                                            "tax_value4='"+jsonObject1.getString("tax_value4")+"'," +
                                            "itemtax5='"+jsonObject1.getString("itemtax5")+"', tax_value5='"+jsonObject1.getString("tax_value5")+"' where _id="+jsonObject1.getString("_id");




//                                    String query2="VALUES("+jsonObject1.getString("_id")+",'"
//                                            +jsonObject1.getString("itemname")+"',"
//                                            +jsonObject1.getString("price")+","
//                                            +jsonObject1.getString("stockquan")+","
//                                            +"'"+jsonObject1.getString("category")+"',"
//                                            +"'"+jsonObject1.getString("itemtax")+"',"
//                                            +"'"+jsonObject1.getString("image")+"',"
//                                            +"'"+jsonObject1.getString("weekdaysvalue")+"',"
//                                            +"'"+jsonObject1.getString("weekendsvalue")+"',"
//                                            +"'"+jsonObject1.getString("manualstockvalue")+"',"
//                                            +"'"+jsonObject1.getString("automaticstockresetvalue")+"',"
//                                            +"'"+jsonObject1.getString("clickcount")+"',"
//                                            +"'"+jsonObject1.getString("favourites")+"',"
//                                            +"'"+jsonObject1.getString("disc_type")+"',"
//                                            +"'"+jsonObject1.getString("disc_value")+"',"
//                                            +"'"+jsonObject1.getString("image_text")+"',"
//                                            +"'"+jsonObject1.getString("barcode_value")+"',"
//                                            +"'"+jsonObject1.getString("checked")+"',"
//                                            +"'"+jsonObject1.getString("print_value")+"',"
//                                            +"'"+jsonObject1.getString("quantity_sold")+"',"
//                                            +"'"+jsonObject1.getString("minimum_qty")+"',"
//                                            +"'"+jsonObject1.getString("minimum_qty_copy")+"',"
//                                            +"'"+jsonObject1.getString("add_qty")+"',"
//                                            +"'"+jsonObject1.getString("status_low")+"',"
//                                            +"'"+jsonObject1.getString("status_qty_updated")+"',"
//                                            +"'"+jsonObject1.getString("individual_price")+"',"
//                                            +"'"+jsonObject1.getString("unit_type")+"',"
//                                            +"'"+jsonObject1.getString("tax_value")+"',"
//                                            +"'"+jsonObject1.getString("itemtax2")+"',"
//                                            +"'"+jsonObject1.getString("tax_value2")+"',"
//                                            +"'"+jsonObject1.getString("itemtax3")+"',"
//                                            +"'"+jsonObject1.getString("tax_value3")+"',"
//                                            +"'"+jsonObject1.getString("itemtax4")+"',"
//                                            +"'"+jsonObject1.getString("tax_value4")+"',"
//                                            +"'"+jsonObject1.getString("itemtax5")+"',"
//                                            +"'"+jsonObject1.getString("tax_value5")+"',"
//                                            +"'"+jsonObject1.getString("status_temp")+"',"
//                                            +"'"+jsonObject1.getString("status_perm")+"',"
//                                            +"'"+jsonObject1.getString("variant1")+"',"
//                                            +"'"+jsonObject1.getString("variant_price1")+"',"
//                                            +"'"+jsonObject1.getString("variant2")+"',"
//                                            +"'"+jsonObject1.getString("variant_price2")+"',"
//
//                                            +"'"+jsonObject1.getString("variant3")+"',"
//                                            +"'"+jsonObject1.getString("variant_price3")+"',"
//                                            +"'"+jsonObject1.getString("variant4")+"',"
//                                            +"'"+jsonObject1.getString("variant_price4")+"',"
//                                            +"'"+jsonObject1.getString("variant5")+"',"
//                                            +"'"+jsonObject1.getString("variant_price5")+"',"
//                                            +"'"+jsonObject1.getString("dept_name")+"')";
                                    //  Log.e("query1",query);
                                    mydbappdata = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                    mydbappdata.execSQL(query);
                                    // mydbappdata.insert("Items", null, cv);
                                    mydbappdata.close();

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } else {

                        }


//                        Cursor synCursor=null;
//                        String lastid1 ="";
//                        mydbappdata = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//                        try {
//                            synCursor = mydbappdata.rawQuery("SELECT * FROM items WHERE   _id = (SELECT MAX(_id)  FROM items)", null);
//                            if (synCursor.moveToFirst()) {
//                                lastid1 = synCursor.getString(0);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        } finally {
//                            if(synCursor!=null)
//                                synCursor.close();
//                        }
//
//
//                        final SQLiteDatabase syncdbapp=openOrCreateDatabase("syncdbapp", Context.MODE_PRIVATE,null);
//                        ContentValues cv = new ContentValues();
//
//                        cv.put("tablename", "items");
//                        cv.put("lastsyncedid", lastid1);
//                        String where1 = "tablename = " + "items";
//                        syncdbapp.update("appdata", cv, where1, new String[]{});
//                        syncdbapp.close();
//
//                        SyncHelperApp syncHelperapp=new SyncHelperApp();
//                        syncHelperapp.beginPeriodicSync(20,getApplicationContext(),null);
                        Intent intent = new Intent("myFunction");
                        intent.putExtra("stop","stop");
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                        stopSelf();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });

        queue.add(jobReq);




    }
}

