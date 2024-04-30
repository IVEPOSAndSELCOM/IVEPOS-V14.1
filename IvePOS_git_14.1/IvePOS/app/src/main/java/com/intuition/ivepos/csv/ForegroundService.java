package com.intuition.ivepos.csv;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

public class ForegroundService extends Service {
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
                .setContentTitle("Items Updating")
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

        int lev = 0;
        mydbappdata = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor count = mydbappdata.rawQuery("SELECT COUNT(*) FROM Items", null);
        if (count.moveToFirst()) {
            lev = count.getInt(0);
        }
        mydbappdata.execSQL("UPDATE sqlite_sequence SET seq = '"+lev+"' WHERE NAME = 'Items'");
        mydbappdata.execSQL("delete from items_virtual");

        JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, WebserviceUrl + "getcsvitems.php", params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        String response = null;
                        try {
                            response = jsonObject.getString(getString(R.string.creditsaleswiperfragment_status));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (response.equalsIgnoreCase("success")) {

                            try {
                                JSONArray jsonArray=jsonObject.getJSONArray("items");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                    ContentValues cv =new ContentValues();
                                    cv.put("_id",jsonObject1.getString("_id"));
                                    cv.put("itemname",jsonObject1.getString("itemname"));

                                    Log.e("data",String.valueOf(jsonArray.length()));
                                    Log.e("itemname",jsonObject1.getString("itemname"));

                                    if(!jsonObject1.isNull("price")){
                                        cv.put("price",jsonObject1.getString("price"));
                                    }else{
                                        cv.put("price",0);
                                    }

                                    if(!jsonObject1.isNull("stockquan")){
                                        cv.put("stockquan",jsonObject1.getString("stockquan"));
                                    }else{
                                        cv.put("stockquan",0);
                                    }

                                    if((!jsonObject1.isNull("category"))&&(!jsonObject1.getString("category").equalsIgnoreCase(""))){
                                        cv.put("category",jsonObject1.getString("category"));
                                    }else{
                                        cv.put("category","None");
                                    }

                                    if((!jsonObject1.isNull("itemtax"))&&(!jsonObject1.getString("itemtax").equalsIgnoreCase(""))){
                                        cv.put("itemtax",jsonObject1.getString("itemtax"));
                                        Log.e("itemtax",jsonObject1.getString("itemtax"));
                                    }else{
                                        cv.put("itemtax","None");
                                    }

                                    if((!jsonObject1.isNull("image"))&&(!jsonObject1.getString("image").equalsIgnoreCase(""))){
                                        cv.put("image",jsonObject1.getString("image"));
                                    }else{
                                        cv.put("image","");
                                    }

                                    if((!jsonObject1.isNull("weekdaysvalue"))&&(!jsonObject1.getString("weekdaysvalue").equalsIgnoreCase(""))){
                                        cv.put("weekdaysvalue",jsonObject1.getString("weekdaysvalue"));
                                    }else{
                                        cv.put("weekdaysvalue","0");
                                    }


                                    if((!jsonObject1.isNull("weekendsvalue"))&&(!jsonObject1.getString("weekendsvalue").equalsIgnoreCase(""))){
                                        cv.put("weekendsvalue",jsonObject1.getString("weekendsvalue"));
                                    }else{
                                        cv.put("weekendsvalue","0");
                                    }


                                    if((!jsonObject1.isNull("manualstockvalue"))&&(!jsonObject1.getString("manualstockvalue").equalsIgnoreCase(""))){
                                        cv.put("manualstockvalue",jsonObject1.getString("manualstockvalue"));
                                    }else{
                                        cv.put("manualstockvalue","");
                                    }


                                    if((!jsonObject1.isNull("automaticstockresetvalue"))&&(!jsonObject1.getString("automaticstockresetvalue").equalsIgnoreCase(""))){
                                        cv.put("automaticstockresetvalue",jsonObject1.getString("automaticstockresetvalue"));
                                    }else{
                                        cv.put("automaticstockresetvalue","");
                                    }

                                    if((!jsonObject1.isNull("clickcount"))&&(!jsonObject1.getString("clickcount").equalsIgnoreCase(""))){
                                        cv.put("clickcount",jsonObject1.getString("clickcount"));
                                    }else{
                                        cv.put("clickcount","");
                                    }

                                    if((!jsonObject1.isNull("favourites"))&&(!jsonObject1.getString("favourites").equalsIgnoreCase(""))){
                                        cv.put("favourites",jsonObject1.getString("favourites"));
                                    }else{
                                        cv.put("favourites","no");
                                    }

                                    if((!jsonObject1.isNull("disc_type"))&&(!jsonObject1.getString("disc_type").equalsIgnoreCase(""))){
                                        cv.put("disc_type",jsonObject1.getString("disc_type"));
                                    }else{
                                        cv.put("disc_type","%");
                                    }

                                    if((!jsonObject1.isNull("disc_value"))&&(!jsonObject1.getString("disc_value").equalsIgnoreCase(""))){
                                        cv.put("disc_value",jsonObject1.getString("disc_value"));
                                    }else{
                                        cv.put("disc_value","0");
                                    }

                                    if((!jsonObject1.isNull("image_text"))&&(!jsonObject1.getString("image_text").equalsIgnoreCase(""))){
                                        cv.put("image_text",jsonObject1.getString("image_text"));
                                    }else{
                                        cv.put("image_text","");
                                    }

                                    if((!jsonObject1.isNull("barcode_value"))&&(!jsonObject1.getString("barcode_value").equalsIgnoreCase(""))){
                                        cv.put("barcode_value",jsonObject1.getString("barcode_value"));
                                    }else{
                                        cv.put("barcode_value","");
                                    }


                                    if((!jsonObject1.isNull("checked"))&&(!jsonObject1.getString("checked").equalsIgnoreCase(""))){
                                        cv.put("checked",jsonObject1.getString("checked"));
                                    }else{
                                        cv.put("checked","");
                                    }

                                    if((!jsonObject1.isNull("print_value"))&&(!jsonObject1.getString("print_value").equalsIgnoreCase(""))){
                                        cv.put("print_value",jsonObject1.getString("print_value"));
                                    }else{
                                        cv.put("print_value","");
                                    }


                                    if((!jsonObject1.isNull("quantity_sold"))&&(!jsonObject1.getString("quantity_sold").equalsIgnoreCase(""))){
                                        cv.put("quantity_sold",jsonObject1.getString("quantity_sold"));
                                    }else{
                                        cv.put("quantity_sold",0);
                                    }

                                    if((!jsonObject1.isNull("minimum_qty"))&&(!jsonObject1.getString("minimum_qty").equalsIgnoreCase(""))){
                                        cv.put("minimum_qty",jsonObject1.getString("minimum_qty"));
                                    }else{
                                        cv.put("minimum_qty","0");
                                    }

                                    if((!jsonObject1.isNull("minimum_qty_copy"))&&(!jsonObject1.getString("minimum_qty_copy").equalsIgnoreCase(""))){
                                        cv.put("minimum_qty_copy",jsonObject1.getString("minimum_qty_copy"));
                                    }else{
                                        cv.put("minimum_qty_copy","0");
                                    }

                                    if((!jsonObject1.isNull("add_qty"))&&(!jsonObject1.getString("add_qty").equalsIgnoreCase(""))){
                                        cv.put("add_qty",jsonObject1.getString("add_qty"));
                                    }else{
                                        cv.put("add_qty","0");
                                    }

                                    if((!jsonObject1.isNull("status_low"))&&(!jsonObject1.getString("status_low").equalsIgnoreCase(""))){
                                        cv.put("status_low",jsonObject1.getString("status_low"));
                                    }else{
                                        cv.put("status_low","");
                                    }

                                    if((!jsonObject1.isNull("status_qty_updated"))&&(!jsonObject1.getString("status_qty_updated").equalsIgnoreCase(""))){
                                        cv.put("status_qty_updated",jsonObject1.getString("status_qty_updated"));
                                    }else{
                                        cv.put("status_qty_updated","");
                                    }

                                    if((!jsonObject1.isNull("individual_price"))&&(!jsonObject1.getString("individual_price").equalsIgnoreCase(""))){
                                        cv.put("individual_price",jsonObject1.getString("individual_price"));
                                    }else{
                                        cv.put("individual_price","");
                                    }

                                    if((!jsonObject1.isNull("unit_type"))&&(!jsonObject1.getString("unit_type").equalsIgnoreCase(""))){
                                        cv.put("unit_type",jsonObject1.getString("unit_type"));
                                    }else{
                                        cv.put("unit_type","Unit");
                                    }

                                    if((!jsonObject1.isNull("tax_value"))&&(!jsonObject1.getString("tax_value").equalsIgnoreCase(""))){
                                        cv.put("tax_value",jsonObject1.getString("tax_value"));
                                        Log.e("tax_value",jsonObject1.getString("tax_value"));
                                    }else{
                                        cv.put("tax_value","");
                                    }

                                    if((!jsonObject1.isNull("itemtax2"))&&(!jsonObject1.getString("itemtax2").equalsIgnoreCase(""))){
                                        cv.put("itemtax2",jsonObject1.getString("itemtax2"));
                                        Log.e("itemtax2",jsonObject1.getString("itemtax2"));
                                    }else{
                                        cv.put("itemtax2","");
                                    }

                                    if((!jsonObject1.isNull("tax_value2"))&&(!jsonObject1.getString("tax_value2").equalsIgnoreCase(""))){
                                        cv.put("tax_value2",jsonObject1.getString("tax_value2"));
                                        Log.e("tax_value2",jsonObject1.getString("tax_value2"));
                                    }else{
                                        cv.put("tax_value2","");
                                    }

                                    if((!jsonObject1.isNull("itemtax3"))&&(!jsonObject1.getString("itemtax3").equalsIgnoreCase(""))){
                                        cv.put("itemtax3",jsonObject1.getString("itemtax3"));
                                    }else{
                                        cv.put("itemtax3","");
                                    }

                                    if((!jsonObject1.isNull("tax_value3"))&&(!jsonObject1.getString("tax_value3").equalsIgnoreCase(""))){
                                        cv.put("tax_value3",jsonObject1.getString("tax_value3"));
                                    }else{
                                        cv.put("tax_value3","");
                                    }

                                    if((!jsonObject1.isNull("itemtax4"))&&(!jsonObject1.getString("itemtax4").equalsIgnoreCase(""))){
                                        cv.put("itemtax4",jsonObject1.getString("itemtax4"));
                                    }else{
                                        cv.put("itemtax4","");
                                    }

                                    if((!jsonObject1.isNull("tax_value4"))&&(!jsonObject1.getString("tax_value4").equalsIgnoreCase(""))){
                                        cv.put("tax_value4",jsonObject1.getString("tax_value4"));
                                    }else{
                                        cv.put("tax_value4","");
                                    }


                                    if((!jsonObject1.isNull("itemtax5"))&&(!jsonObject1.getString("itemtax5").equalsIgnoreCase(""))){
                                        cv.put("itemtax5",jsonObject1.getString("itemtax5"));
                                    }else{
                                        cv.put("itemtax5","");
                                    }

                                    if((!jsonObject1.isNull("tax_value5"))&&(!jsonObject1.getString("tax_value5").equalsIgnoreCase(""))){
                                        cv.put("tax_value5",jsonObject1.getString("tax_value5"));
                                    }else{
                                        cv.put("tax_value5","");
                                    }

                                    if((!jsonObject1.isNull("status_temp"))&&(!jsonObject1.getString("status_temp").equalsIgnoreCase(""))){
                                        cv.put("status_temp",jsonObject1.getString("status_temp"));
                                    }else{
                                        cv.put("status_temp","");
                                    }

                                    if((!jsonObject1.isNull("status_perm"))&&(!jsonObject1.getString("status_perm").equalsIgnoreCase(""))){
                                        cv.put("status_perm",jsonObject1.getString("status_perm"));
                                    }else{
                                        cv.put("status_perm","");
                                    }

                                    if((!jsonObject1.isNull("variant1"))&&(!jsonObject1.getString("variant1").equalsIgnoreCase(""))){
                                        cv.put("variant1",jsonObject1.getString("variant1"));
                                    }else{
                                        cv.put("variant1","");
                                    }

                                    if((!jsonObject1.isNull("variant_price1"))&&(!jsonObject1.getString("variant_price1").equalsIgnoreCase(""))){
                                        cv.put("variant_price1",jsonObject1.getString("variant_price1"));
                                    }else{
                                        cv.put("variant_price1","");
                                    }

                                    if((!jsonObject1.isNull("variant2"))&&(!jsonObject1.getString("variant2").equalsIgnoreCase(""))){
                                        cv.put("variant2",jsonObject1.getString("variant2"));
                                    }else{
                                        cv.put("variant2","");
                                    }

                                    if((!jsonObject1.isNull("variant_price2"))&&(!jsonObject1.getString("variant_price2").equalsIgnoreCase(""))){
                                        cv.put("variant_price2",jsonObject1.getString("variant_price2"));
                                    }else{
                                        cv.put("variant_price2","");
                                    }

                                    if((!jsonObject1.isNull("variant3"))&&(!jsonObject1.getString("variant3").equalsIgnoreCase(""))){
                                        cv.put("variant3",jsonObject1.getString("variant3"));
                                    }else{
                                        cv.put("variant3","");
                                    }

                                    if((!jsonObject1.isNull("variant_price3"))&&(!jsonObject1.getString("variant_price3").equalsIgnoreCase(""))){
                                        cv.put("variant_price3",jsonObject1.getString("variant_price3"));
                                    }else{
                                        cv.put("variant_price3","");
                                    }

                                    if((!jsonObject1.isNull("variant4"))&&(!jsonObject1.getString("variant4").equalsIgnoreCase(""))){
                                        cv.put("variant4",jsonObject1.getString("variant4"));
                                    }else{
                                        cv.put("variant4","");
                                    }

                                    if((!jsonObject1.isNull("variant_price4"))&&(!jsonObject1.getString("variant_price4").equalsIgnoreCase(""))){
                                        cv.put("variant_price4",jsonObject1.getString("variant_price4"));
                                    }else{
                                        cv.put("variant_price4","");
                                    }

                                    if((!jsonObject1.isNull("variant5"))&&(!jsonObject1.getString("variant5").equalsIgnoreCase(""))){
                                        cv.put("variant5",jsonObject1.getString("variant5"));
                                    }else{
                                        cv.put("variant5","");
                                    }

                                    if((!jsonObject1.isNull("variant_price5"))&&(!jsonObject1.getString("variant_price5").equalsIgnoreCase(""))){
                                        cv.put("variant_price5",jsonObject1.getString("variant_price5"));
                                    }else{
                                        cv.put("variant_price5","");
                                    }


                                    if((!jsonObject1.isNull("dept_name"))&&(!jsonObject1.getString("dept_name").equalsIgnoreCase(""))){
                                        cv.put("dept_name",jsonObject1.getString("dept_name"));
                                    }else{
                                        cv.put("dept_name","");
                                    }



//
//     String query="INSERT INTO Items(_id, itemname, price, stockquan, category, itemtax, image, weekdaysvalue, weekendsvalue, " +
//                                            "manualstockvalue, automaticstockresetvalue, clickcount, " +
//                                            "favourites, disc_type, disc_value, image_text, barcode_value," +
//             " checked, print_value, quantity_sold, minimum_qty, " +
//                                            "minimum_qty_copy, add_qty, status_low, status_qty_updated, " +
//             "individual_price, unit_type, tax_value, itemtax2, tax_value2, " +
//                                            "itemtax3, tax_value3, itemtax4, tax_value4, itemtax5, " +
//             "tax_value5, status_temp, status_perm, variant1, " +
//                                            "variant_price1, variant2," +
//                                            " variant_price2, variant3, variant_price3, variant4, " +
//             "variant_price4, variant5, variant_price5, dept_name)";
//
//     String query2="VALUES("+jsonObject1.getString("_id")+",'"
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
//                                            +""+jsonObject1.getString("quantity_sold")+","
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
                                    mydbappdata = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                    //  mydbappdata.insert(query+query2);
                                    mydbappdata.insert("Items", null, cv);
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
