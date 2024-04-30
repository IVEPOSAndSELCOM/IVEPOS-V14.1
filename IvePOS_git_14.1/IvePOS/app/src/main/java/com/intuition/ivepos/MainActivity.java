package com.intuition.ivepos;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.intuition.ivepos.csv.RequestSingleton;
import com.intuition.ivepos.firebase.Config;
import com.intuition.ivepos.firebase.MyService;
import com.intuition.ivepos.languagelocale.LocaleUtils;
import com.intuition.ivepos.signup.DownloadService;
import com.intuition.ivepos.signup.SyncHelperService;
import com.intuition.ivepos.sync.StubProvider;
import com.intuition.ivepos.sync.SyncHelper;
import com.intuition.ivepos.syncapp.StubProviderApp;
import com.intuition.ivepos.syncapp.SyncHelperApp;
import com.intuition.ivepos.syncdb.SyncDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Uri contentUri,resultUri;
    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;
    public SQLiteDatabase db_inapp = null;
    String ladminuser, ladminpass, madminuser, madminpass;
    String useroneuser, useronepass, usertwouser, usertwopass, userthreeuser, userthreepass, userfouruser, userfourpass, userfiveuser, userfivepass, usersixuser, usersixpass;
    byte[] img;
    String combine;
    TextView wrongpin, box1, box2, box3, box4;


    public static final String PACKAGE_NAME = "com.intuition.ivepos";
    public String DATA_DIRECTORY_DATABASE =
            Environment.getDataDirectory() +
                    "/data/" + PACKAGE_NAME +
                    "/databases/" + "syncdb" ;
    public String DATA_DIRECTORY_DATABASE1 =
            Environment.getDataDirectory() +
                    "/data/" + PACKAGE_NAME +
                    "/databases/" + "syncdbapp" ;
    String getuser1_name, getuser2_name, getuser3_name, getuser4_name, getuser5_name, getuser6_name;
    String one_one;
    RequestQueue requestQueue;
    //private BroadcastReceiver mRegistrationBroadcastReceiver;
    /**
     * Called when the activity is first created.
     */
    RelativeLayout progressbar11;
    TextView tv_copy;
    ScrollView scrollView;
     String company="",store="",device="";

    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    SimpleDateFormat sdff2, sdff1;
    String currentDateandTimee1;
    String timee1;

    String WebserviceUrl;
    String account_selection;

    private BluetoothAdapter mBluetoothAdapter = null;
    private static final String TAG = "WorkThread";

    int no_items = 0;

    private static final String[] BLE_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @RequiresApi(api = Build.VERSION_CODES.S)
    private static final String[] ANDROID_12_BLE_PERMISSIONS = new String[]{
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
    };

    public static void requestBlePermissions(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            ActivityCompat.requestPermissions(activity, ANDROID_12_BLE_PERMISSIONS, requestCode);
        else
            ActivityCompat.requestPermissions(activity, BLE_PERMISSIONS, requestCode);
    }

    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        LocaleUtils.initialize(this, LocaleUtils.getSelectedLanguageId(this));

        sdff2 = new SimpleDateFormat("ddMMMyy");
        currentDateandTimee1 = sdff2.format(new Date());

        Date dt = new Date();
        sdff1 = new SimpleDateFormat("hhmmssaa");
        timee1 = sdff1.format(dt);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        SharedPreferences sharedpreferences_select =  SplashScreenActivity.getDefaultSharedPreferencesMultiProcess(MainActivity.this);
        account_selection= sharedpreferences_select.getString("account_selection", null);

        if (account_selection.toString().equals("Dine")) {
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }else {
            if (account_selection.toString().equals("Qsr")) {
                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
            }else {
                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
            }
        }




        SharedPreferences sharedpreferences = getSharedPreferences("downloadpref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("download", "");
        editor.putString("downloadsales", "");
        editor.commit();

        mContext = this;

        if(!databaseExistSyncdb()){
            Log.d("sync db","not present");
            SyncDatabase syncdatabase=new SyncDatabase();
            syncdatabase.createSyncDb(getApplicationContext());
        }else{
            Log.d("sync db","present");
            SQLiteDatabase syncdb=openOrCreateDatabase("syncdb", Context.MODE_PRIVATE,null);
            SQLiteDatabase syncdbapp=openOrCreateDatabase("syncdbapp", Context.MODE_PRIVATE,null);
            Cursor cursor = syncdb.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'salesdata'", null);
            if(cursor!=null) {
                if(cursor.getCount()>0) {
                    cursor.close();

                }else{
                    cursor.close();
                    SyncDatabase syncdatabase=new SyncDatabase();
                    syncdatabase.createSyncDb(getApplicationContext());
                }
                cursor.close();
            }



        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor1 = prefs.edit();
        editor1.putInt("ip_count", 1);
        editor1.putString("ip_address", "");
        editor1.apply();

        SharedPreferences sh = getSharedPreferences("signupconfirm", MODE_PRIVATE);
        String from = sh.getString("from", "");
        System.out.println("from page ");

        TextView textView = new TextView(MainActivity.this);
        textView.setText(from);

        TextView default_pin = (TextView) findViewById(R.id.default_pin);

        if (textView.getText().toString().equals("signupconfirmpage")) {
            default_pin.setVisibility(View.VISIBLE);

            // adding the color to be shown
            ObjectAnimator animator = ObjectAnimator.ofInt(default_pin, "backgroundColor", Color.BLUE, Color.RED, Color.GREEN);

            // duration of one color
            animator.setDuration(500);
            animator.setEvaluator(new ArgbEvaluator());

            // color will be show in reverse manner
            animator.setRepeatCount(Animation.REVERSE);

            // It will be repeated up to infinite time
            animator.setRepeatCount(Animation.INFINITE);
            animator.start();

            SharedPreferences sharedPreferences = getSharedPreferences("signupconfirm", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("from", "");
            myEdit.apply();

        }

//        if(!databaseExistSyncdb()){
//            Log.d("sync db","not present");
//            SyncDatabase syncdatabase=new SyncDatabase();
//            syncdatabase.createSyncDb(getApplicationContext());
//        }else{
//            Log.d("sync db","present");
////            SQLiteDatabase syncdb=openOrCreateDatabase("syncdbapp", Context.MODE_PRIVATE,null);
////            Cursor cursor=syncdb.rawQuery("SELECT * from syncdbapp", null);
////            boolean isPresent=false;
////            if (cursor.moveToFirst()) {
////                while (!cursor.isAfterLast()) {
////                    if(cursor.getString(1).equalsIgnoreCase("stock_transfer_item_details")){
////                       isPresent=true;
////                    }
////
////                }
////            }
////            if(!isPresent){
////                int lastid = 0;
////                ContentValues cv = new ContentValues();
////                cv.put("tablename", "stock_transfer_item_details");
////                cv.put("lastsyncedid", lastid);
////                syncdb.insert("appdata", null, cv);
////              //  Log.d("table-sales", tablename);
////            }
//        }

        SharedPreferences pref = getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
        boolean isSignedin= pref.getBoolean("signin", false);

        if(!isSignedin){//if you signout and signin or signup this will execute
            SyncHelper syncHelper=new SyncHelper();
            syncHelper.tablesync(getApplicationContext());

            SyncHelperApp syncHelperapp=new SyncHelperApp();
            syncHelperapp.tablesync(getApplicationContext());

            syncHelperapp.beginPeriodicSync(20,getApplicationContext(),null);
            syncHelper.beginPeriodicSync(20,getApplicationContext(),null);

            SharedPreferences sharedpreferences1=getDefaultSharedPreferencesMultiProcess(getApplicationContext());
            String company= sharedpreferences1.getString("companyname", null);
            String store= sharedpreferences1.getString("storename", null);
            String device= sharedpreferences1.getString("devicename", null);
            String companyname_special= sharedpreferences1.getString("companyname_special", null);

            System.out.println("hiiii MainActivity1 "+company+" "+store+" "+device+" "+companyname_special);

            Intent intent = new Intent(MainActivity.this, DownloadService.class);
            // add infos for the service which file to download and where to store
            intent.putExtra("company", company);
            intent.putExtra("store", store);
            intent.putExtra("device", device);
            intent.putExtra("companyname_special", companyname_special);
//                    intent.putExtra("emailid", emailid);
            startService(intent);

        }else{//after signin whenever app opens nd comes from splashscreen this will be executed
            System.out.println("Signed in");
            SyncHelper syncHelper=new SyncHelper();
            syncHelper.tablesync(getApplicationContext());

            SyncHelperApp syncHelperapp=new SyncHelperApp();
            syncHelperapp.tablesync(getApplicationContext());
            syncHelperapp.beginPeriodicSync(20,getApplicationContext(),null);
            syncHelper.beginPeriodicSync(20,getApplicationContext(),null);

//            Toast.makeText(MainActivity.this, "signedin",Toast.LENGTH_LONG).show();

//            if(isDeviceOnline()) {
//                check_server();
//            }

        }

//        updateloginstatus();

        Log.e("testlog","1");

       new SyncTask().execute();

    }

    private void setupcardpayments() {

      //  db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);
        db.execSQL("delete from CardSwiperActivation");



        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
        getContentResolver().delete(contentUri, null,null);
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("CardSwiperActivation")
                .appendQueryParameter("operation", "delete")
                .appendQueryParameter("1", "1")
                .build();
        getContentResolver().notifyChange(resultUri, null);

        db.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE NAME = 'CardSwiperActivation'");

        Log.e("testlog","cardswiper3");

        ContentValues contentValues = new ContentValues();
        contentValues.put("CardSwiperName", "PaySwiff");
        contentValues.put("merchantKey", "");
        contentValues.put("partnerkey", "");
        contentValues.put("Config_status", "Not Activated");
        //    db.insert("CardSwiperActivation", null, contentValues);
        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
        resultUri = getContentResolver().insert(contentUri, contentValues);
        getContentResolver().notifyChange(resultUri, null);



        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("CardSwiperName", "mSwipe");
        contentValues1.put("merchantKey", "");
        contentValues1.put("partnerkey", "");
        contentValues1.put("Config_status", "Not Activated");
        //   db.insert("CardSwiperActivation", null, contentValues1);

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
        resultUri = getContentResolver().insert(contentUri, contentValues1);
        getContentResolver().notifyChange(resultUri, null);


        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("CardSwiperName", "PineLabs");
        contentValues2.put("merchantKey", "");
        contentValues2.put("partnerkey", "");
        contentValues2.put("Config_status", "Not Activated");
        //   db.insert("CardSwiperActivation", null, contentValues1);

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
        resultUri = getContentResolver().insert(contentUri, contentValues2);
        getContentResolver().notifyChange(resultUri, null);

        ContentValues contentValues3 = new ContentValues();
        contentValues3.put("CardSwiperName", "Firstdata");
        contentValues3.put("merchantKey", "");
        contentValues3.put("partnerkey", "");
        contentValues3.put("Config_status", "Not Activated");
        //   db.insert("CardSwiperActivation", null, contentValues1);

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
        resultUri = getContentResolver().insert(contentUri, contentValues3);
        getContentResolver().notifyChange(resultUri, null);


        ContentValues contentValues4 = new ContentValues();
        contentValues4.put("CardSwiperName", "Paytm");
        contentValues4.put("merchantKey", "");
        contentValues4.put("partnerkey", "");
        contentValues4.put("Config_status", "Not Activated");
        //   db.insert("CardSwiperActivation", null, contentValues1);

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
        resultUri = getContentResolver().insert(contentUri, contentValues4);
        getContentResolver().notifyChange(resultUri, null);

        ContentValues contentValues5 = new ContentValues();
        contentValues5.put("CardSwiperName", "ezetap");
        contentValues5.put("merchantKey", "");
        contentValues5.put("partnerkey", "");
        contentValues5.put("Config_status", "Not Activated");
        //   db.insert("CardSwiperActivation", null, contentValues1);

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
        resultUri = getContentResolver().insert(contentUri, contentValues5);
        getContentResolver().notifyChange(resultUri, null);

    }

    private void updateloginstatus() {

        SharedPreferences sharedpreferences=getDefaultSharedPreferencesMultiProcess(getApplicationContext());
         company= sharedpreferences.getString("companyname", null);
         store= sharedpreferences.getString("storename", null);
         device= sharedpreferences.getString("devicename", null);
        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                    Request.Method.POST,
                    WebserviceUrl+"loginstatus.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Signup confirm1", "Error: " + error.getMessage());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("company", company+ "");
                    params.put("store", store+ "");
                    params.put("device", device);
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(sr);


    }

    private class SyncTask extends AsyncTask<Void, Void, List<String>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("testlog","2");

        }

        @Override
        protected List<String> doInBackground(Void... voids) {

            Log.e("testlog","3");
            SharedPreferences pref = getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
            boolean isSignedin= pref.getBoolean("signin", false);
//            if(!isSignedin){
            Log.e("testlog","4");
            updateAppdata();
            updateSalesdata();
//            }

            return null;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);

            Log.e("testlog","5");
            SharedPreferences prefs =getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("signin", true); // Storing boolean - true/false

            editor.commit();


            String from= getIntent().getStringExtra("from");
            SQLiteDatabase dbs;
            dbs = openOrCreateDatabase("syncdbapp", Context.MODE_PRIVATE,null);
            Cursor c = dbs.rawQuery("SELECT * FROM appdata", null);
            if (c.moveToFirst()) {
                while ( !c.isAfterLast() ) {

                    Log.e(c.getString(1),"id="+c.getInt(0)+", and lastid="+c.getString(2));

                    c.moveToNext();
                    // }
                }
            }
            c.close();

            SQLiteDatabase dbs1;
            dbs1 = openOrCreateDatabase("syncdb", Context.MODE_PRIVATE,null);
            Cursor c1 = dbs1.rawQuery("SELECT * FROM salesdata", null);
            if (c1.moveToFirst()) {
                while ( !c1.isAfterLast() ) {

                    Log.e(c1.getString(1),"id="+c1.getInt(0)+", and lastid="+c1.getString(2));

                    c1.moveToNext();
                    // }
                }
            }
            c1.close();

//            Toast.makeText(MainActivity.this, "from home page "+from, Toast.LENGTH_LONG).show();

            if(from!=null){
                if((from.equalsIgnoreCase("splash")) || (from.equalsIgnoreCase("home"))){
                    System.out.println("from home page");
                    // new MyFirebaseInstanceIDService().onTokenRefresh();
                    firebaseRegistration();
                }else{
                    //  new MyFirebaseInstanceIDService().onTokenRefresh();
//                    initApp();
                    firebaseRegistration();
                }
            }else{
                //  new MyFirebaseInstanceIDService().onTokenRefresh();
//                initApp();
                firebaseRegistration();
            }


            SharedPreferences prefss = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
            String regId = prefss.getString("regId", null);

         //   Log.e("regId",regId);

            if (no_items == 1) {

                SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(MainActivity.this);
                final String company = sharedpreferences.getString("companyname", null);
                final String store = sharedpreferences.getString("storename", null);
                final String device = sharedpreferences.getString("devicename", null);

                System.out.println("hiiii MainActivity2 " + company + " " + store + " " + device);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestQueue = Volley.newRequestQueue(MainActivity.this);
                        Log.d("downloadserv", "inside createappdata_dummy");
                        StringRequest sr = new StringRequest(
                                Request.Method.POST,
                                WebserviceUrl + "dummy_db.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        no_items = 0;
                                        Log.d("downloadserv", "inside response1 " + response);
                                        if (response.equalsIgnoreCase("success")) {
                                            Log.d("response", response);
                                            Log.d("downloadserv", "success1");
                                        } else {
                                            Log.d("downloadserv", "failed1");
                                        }

                                        final SQLiteDatabase syncdbapp = openOrCreateDatabase("syncdbapp", Context.MODE_PRIVATE, null);

                                        Cursor cursor = syncdbapp.rawQuery("SELECT * FROM appdata WHERE tablename = 'Items'", null);
                                        if (cursor.moveToFirst()) {
                                            String id = cursor.getString(0);
                                            ContentValues cv = new ContentValues();
//                                  cv.put("_id", id);
                                            cv.put("tablename", "Items");
                                            cv.put("lastsyncedid", "40");
                                            String where1 = "_id = " + id;
                                            syncdbapp.update("appdata", cv, where1, new String[]{});
                                        }
                                        cursor.close();

                                        Cursor cursor1 = syncdbapp.rawQuery("SELECT * FROM appdata WHERE tablename = 'Hotel'", null);
                                        if (cursor1.moveToFirst()) {
                                            String id = cursor1.getString(0);
                                            ContentValues cv = new ContentValues();
//                                  cv.put("_id", id);
                                            cv.put("tablename", "Hotel");
                                            cv.put("lastsyncedid", "7");
                                            String where1 = "_id = " + id;
                                            syncdbapp.update("appdata", cv, where1, new String[]{});
                                        }
                                        cursor1.close();

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("Signup confirm2", "Error: " + error.getMessage());
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                               /*     params.put("email", email + "");
                                    params.put("password", password + "");*/
                                params.put("company", company + "");
                                params.put("store", store + "");
                                params.put("device", device + "");

//                                Log.d("downloadserv", company);
//                                Log.d("downloadserv", store);
//                                Log.d("downloadserv", device);
                                return params;
                            }
                        };
                        sr.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                        requestQueue.add(sr);
                    }
                }, 8000);
            }


            SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(getApplicationContext());
            final String company= sharedpreferences.getString("companyname", null);
            final String store= sharedpreferences.getString("storename", null);
            final String device= sharedpreferences.getString("devicename", null);

            SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
            RequestQueue requestQueue;
            requestQueue = Volley.newRequestQueue(MainActivity.this);
            StringRequest sr = new StringRequest(
                    Request.Method.POST,
                    WebserviceUrl+"getcompanyid.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("ids are  "+response);

                            try {
                                JSONObject emp=(new JSONObject(response));
                                String companyid = emp.getString("companyid");
                                String storeid = emp.getString("storeid");
                                String deviceid = emp.getString("deviceid");
                                System.out.println("ids are  "+companyid+" "+storeid+" "+deviceid);

                                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref_ids", MODE_PRIVATE);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putString("companyid", companyid);
                                myEdit.putString("storeid", storeid);
                                myEdit.putString("deviceid", deviceid);
                                myEdit.apply();

                            } catch (JSONException e) {
                                e.printStackTrace();
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
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(sr);


        }
    }


    public void updateAppdata() {

        Log.e("testlog","appdata");

        SharedPreferences sharedpreferences_select =  SplashScreenActivity.getDefaultSharedPreferencesMultiProcess(MainActivity.this);
        String account_selection= sharedpreferences_select.getString("account_selection", null);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);

        webservicequery("ALTER TABLE Ingredients CHANGE _id _id INT AUTO_INCREMENT");

//        db.execSQL("ALTER TABLE Discount_details MODIFY COLUMN _id INT AUTO_INCREMENT");
//        webservicequery("ALTER TABLE Discount_details CHANGE _id _id INT AUTO_INCREMENT");


        webservicequery("Delete from logo where companylogo = '' OR companylogo IS NULL");
//        db.execSQL("Delete from logo where companylogo = '' OR companylogo IS NULL");//removed becuase of no such table: logo in crashlytics

        Cursor selec = db.rawQuery("SELECT * FROM Account_selection", null);
        if (selec.moveToFirst()) {
            db.execSQL("delete from Account_selection");
            webservicequery("delete from Account_selection");

            if (account_selection.toString().equals("Dine")) {
                db.execSQL("INSERT INTO Account_selection (_id, selection) VALUES ('1', 'Dine')");
                webservicequery("INSERT INTO Account_selection (_id, selection) VALUES ('1', 'Dine')");
            }
            if (account_selection.toString().equals("Qsr")) {
                db.execSQL("INSERT INTO Account_selection (_id, selection) VALUES ('1', 'Qsr')");
                webservicequery("INSERT INTO Account_selection (_id, selection) VALUES ('1', 'Qsr')");
            }
            if (account_selection.toString().equals("Retail")) {
                db.execSQL("INSERT INTO Account_selection (_id, selection) VALUES ('1', 'Retail')");
                webservicequery("INSERT INTO Account_selection (_id, selection) VALUES ('1', 'Retail')");
            }
//            ContentValues contentValuess = new ContentValues();
//            contentValuess.put("selection", account_selection);
//
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Account_selection");
//            resultUri = getContentResolver().insert(contentUri, contentValuess);
//            getContentResolver().notifyChange(resultUri, null);
        }else {
            ContentValues contentValuess = new ContentValues();
            contentValuess.put("selection", account_selection);

            if (account_selection.toString().equals("Dine")) {
                db.execSQL("INSERT INTO Account_selection (_id, selection) VALUES ('1', 'Dine')");
                webservicequery("INSERT INTO Account_selection (_id, selection) VALUES ('1', 'Dine')");
            }
            if (account_selection.toString().equals("Qsr")) {
                db.execSQL("INSERT INTO Account_selection (_id, selection) VALUES ('1', 'Qsr')");
                webservicequery("INSERT INTO Account_selection (_id, selection) VALUES ('1', 'Qsr')");
            }
            if (account_selection.toString().equals("Retail")) {
                db.execSQL("INSERT INTO Account_selection (_id, selection) VALUES ('1', 'Retail')");
                webservicequery("INSERT INTO Account_selection (_id, selection) VALUES ('1', 'Retail')");
            }

//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Account_selection");
//            resultUri = getContentResolver().insert(contentUri, contentValuess);
//            getContentResolver().notifyChange(resultUri, null);
        }
        selec.close();

        Cursor selec1 = db.rawQuery("SELECT * FROM Version_Control", null);
        if (selec1.moveToFirst()) {
            db.execSQL("delete from Version_Control");
            webservicequery("delete from Version_Control");

            db.execSQL("INSERT INTO Version_Control (_id, version) VALUES ('1', '12')");
            webservicequery("INSERT INTO Version_Control (_id, version) VALUES ('1', '12')");

//            ContentValues contentValuess = new ContentValues();
//            contentValuess.put("_id", "1");
//            contentValuess.put("version", "12");
//
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Version_Control");
//            resultUri = getContentResolver().insert(contentUri, contentValuess);
//            getContentResolver().notifyChange(resultUri, null);
        }else {

            db.execSQL("INSERT INTO Version_Control (_id, version) VALUES ('1', '12')");
            webservicequery("INSERT INTO Version_Control (_id, version) VALUES ('1', '12')");

//            ContentValues contentValuess = new ContentValues();
//            contentValuess.put("_id", "1");
//            contentValuess.put("version", "12");
//
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Version_Control");
//            resultUri = getContentResolver().insert(contentUri, contentValuess);
//            getContentResolver().notifyChange(resultUri, null);
        }
        selec1.close();

        Cursor add_item_1 = db.rawQuery("SELECT * FROM Items", null);
        if (add_item_1.moveToFirst()) {

        } else {

            db.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE NAME = 'Items'");
            db.execSQL("delete from hotel");
            db.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE NAME = 'Hotel'");
//            db.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE NAME = 'Hotel'");

            webservicequery("ALTER TABLE items AUTO_INCREMENT = 1");
            webservicequery("delete from hotel");
            webservicequery("ALTER TABLE hotel AUTO_INCREMENT = 1");
//            webservicequery("UPDATE sqlite_sequence SET seq = 0 WHERE NAME = 'Hotel'");
        }
        add_item_1.close();

        Cursor cursorrr = db.rawQuery("SELECT * FROM PaytmMerchantReg", null);
        if (cursorrr.moveToFirst()) {

        } else {
            ContentValues contentValuess = new ContentValues();
            contentValuess.put("Account", "Not_Registered");
            contentValuess.put("MerchantName", "");
            contentValuess.put("guid", "");
            contentValuess.put("MID", "");
            contentValuess.put("Merchant_key", "");
            contentValuess.put("PosID", "");
         //   db.insert("PaytmMerchantReg", null, contentValuess);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "PaytmMerchantReg");
            resultUri = getContentResolver().insert(contentUri, contentValuess);
            getContentResolver().notifyChange(resultUri, null);
        }
        cursorrr.close();

        Cursor cursorrr1 = db.rawQuery("SELECT * FROM RazorpayMerchantReg", null);
        if (cursorrr1.moveToFirst()) {

        } else {
            ContentValues contentValuess = new ContentValues();
            contentValuess.put("account", "Not_Registered");
            contentValuess.put("account_id", "");
         //   db.insert("RazorpayMerchantReg", null, contentValuess);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "RazorpayMerchantReg");
            resultUri = getContentResolver().insert(contentUri, contentValuess);
            getContentResolver().notifyChange(resultUri, null);
        }
        cursorrr1.close();

        Cursor cursorrr2 = db.rawQuery("SELECT * FROM EzetapMerchantReg", null);
        if (cursorrr2.moveToFirst()) {

        } else {
            ContentValues contentValuess = new ContentValues();
            contentValuess.put("account", "Not_Registered");
            contentValuess.put("userid", "");
            contentValuess.put("password", "");
            //   db.insert("EzetapMerchantReg", null, contentValuess);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "EzetapMerchantReg");
            resultUri = getContentResolver().insert(contentUri, contentValuess);
            getContentResolver().notifyChange(resultUri, null);
        }
        cursorrr2.close();

        Cursor cur = db.rawQuery("SELECT * FROM MobikwikMerchantReg", null);
        if (cur.moveToFirst()) {

        } else {
            ContentValues contentValuees = new ContentValues();
            contentValuees.put("Account", "Not_Registered");
            contentValuees.put("Merchant_name", "");
            contentValuees.put("Mid_otp", "");
            contentValuees.put("Secretkey_otp", "");
            contentValuees.put("Mid_debit", "");
            contentValuees.put("Secretkey_debit", "");
           // db.insert("MobikwikMerchantReg", null, contentValuees);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "MobikwikMerchantReg");
            resultUri = getContentResolver().insert(contentUri, contentValuees);
            getContentResolver().notifyChange(resultUri, null);
        }
        cur.close();

        Log.e("testlog","cardswiper1");

        Cursor c = db.rawQuery("SELECT * FROM CardSwiperActivation", null);
        if (c.moveToFirst()) {

//            setupcardpayments();
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM CardSwiperActivation", null);
            if (cursor.moveToFirst()) {
                int icount2 = cursor.getInt(0);

                if (icount2 == 5) {
                    ContentValues contentValues5 = new ContentValues();
                    contentValues5.put("CardSwiperName", "ezetap");
                    contentValues5.put("merchantKey", "");
                    contentValues5.put("partnerkey", "");
                    contentValues5.put("Config_status", "Not Activated");
                    //   db.insert("CardSwiperActivation", null, contentValues1);

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
                    resultUri = getContentResolver().insert(contentUri, contentValues5);
                    getContentResolver().notifyChange(resultUri, null);

                    Log.e("testlog","cardswiper6");
                }
            }

        }else {

            Log.e("testlog","cardswiper5");

            db.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE NAME = 'CardSwiperActivation'");

            ContentValues contentValues = new ContentValues();
            contentValues.put("CardSwiperName", "PaySwiff");
            contentValues.put("merchantKey", "");
            contentValues.put("partnerkey", "");
            contentValues.put("Config_status", "Not Activated");
        //    db.insert("CardSwiperActivation", null, contentValues);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);



            ContentValues contentValues1 = new ContentValues();
            contentValues1.put("CardSwiperName", "mSwipe");
            contentValues1.put("merchantKey", "");
            contentValues1.put("partnerkey", "");
            contentValues1.put("Config_status", "Not Activated");
         //   db.insert("CardSwiperActivation", null, contentValues1);

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
            resultUri = getContentResolver().insert(contentUri, contentValues1);
            getContentResolver().notifyChange(resultUri, null);


            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("CardSwiperName", "PineLabs");
            contentValues2.put("merchantKey", "");
            contentValues2.put("partnerkey", "");
            contentValues2.put("Config_status", "Not Activated");
            //   db.insert("CardSwiperActivation", null, contentValues1);

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
            resultUri = getContentResolver().insert(contentUri, contentValues2);
            getContentResolver().notifyChange(resultUri, null);

            ContentValues contentValues3 = new ContentValues();
            contentValues3.put("CardSwiperName", "Firstdata");
            contentValues3.put("merchantKey", "");
            contentValues3.put("partnerkey", "");
            contentValues3.put("Config_status", "Not Activated");
            //   db.insert("CardSwiperActivation", null, contentValues1);

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
            resultUri = getContentResolver().insert(contentUri, contentValues3);
            getContentResolver().notifyChange(resultUri, null);


            ContentValues contentValues4 = new ContentValues();
            contentValues4.put("CardSwiperName", "Paytm");
            contentValues4.put("merchantKey", "");
            contentValues4.put("partnerkey", "");
            contentValues4.put("Config_status", "Not Activated");
            //   db.insert("CardSwiperActivation", null, contentValues1);

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
            resultUri = getContentResolver().insert(contentUri, contentValues4);
            getContentResolver().notifyChange(resultUri, null);

            ContentValues contentValues5 = new ContentValues();
            contentValues5.put("CardSwiperName", "ezetap");
            contentValues5.put("merchantKey", "");
            contentValues5.put("partnerkey", "");
            contentValues5.put("Config_status", "Not Activated");
            //   db.insert("CardSwiperActivation", null, contentValues1);

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
            resultUri = getContentResolver().insert(contentUri, contentValues5);
            getContentResolver().notifyChange(resultUri, null);

            Log.e("testlog","cardswiper6");


        }
        c.close();


        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor access = db.rawQuery("SELECT * FROM LAdmin ", null);
        if (access.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", "ladmin");
            contentValues.put("password", "1234");
            contentValues.put("name", "ladmin");

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LAdmin");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("LAdmin", null, contentValues);
        }
        access.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor freetrail = db.rawQuery("SELECT * FROM Freetrail", null);
        if (freetrail.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("status", "Activated");

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Freetrail");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("LAdmin", null, contentValues);
        }
        freetrail.close();

        Cursor five = db.rawQuery("SELECT * FROM LOGIN ", null);
        if (five.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("USERNAME", "admin");
            contentValues.put("PASSWORD", "0000");
            contentValues.put("name", "madmin");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LOGIN");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("LOGIN", null, contentValues);
        }
        five.close();

        Cursor user1 = db.rawQuery("SELECT * FROM User1 ", null);
        if (user1.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", "user1");
            contentValues.put("password", "1111");
            contentValues.put("name", "User1");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User1");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("User1", null, contentValues);
        }
        user1.close();

        Cursor user2 = db.rawQuery("SELECT * FROM User2 ", null);
        if (user2.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", "user2");
            contentValues.put("password", "2222");
            contentValues.put("name", "User2");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User2");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("User2", null, contentValues);
        }
        user2.close();

        Cursor user3 = db.rawQuery("SELECT * FROM User3 ", null);
        if (user3.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", "user3");
            contentValues.put("password", "3333");
            contentValues.put("name", "User3");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User3");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("User3", null, contentValues);
        }
        user3.close();

        Cursor user4 = db.rawQuery("SELECT * FROM User4 ", null);
        if (user4.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", "user4");
            contentValues.put("password", "4444");
            contentValues.put("name", "User4");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User4");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("User4", null, contentValues);
        }
        user4.close();

        Cursor user5 = db.rawQuery("SELECT * FROM User5 ", null);
        if (user5.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", "user5");
            contentValues.put("password", "5555");
            contentValues.put("name", "User5");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User5");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("User5", null, contentValues);
        }
        user5.close();

        Cursor user6 = db.rawQuery("SELECT * FROM User6 ", null);
        if (user6.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", "user6");
            contentValues.put("password", "6666");
            contentValues.put("name", "User6");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User6");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("User6", null, contentValues);
        }
        user6.close();

        Cursor user7 = db.rawQuery("SELECT * FROM Printerreceiptsize ", null);
        if (user7.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("papersize", "2 inch");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Printerreceiptsize");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Printerreceiptsize", null, contentValues);
        }
        user7.close();

        Cursor user8 = db.rawQuery("SELECT * FROM Storedays ", null);
        if (user8.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("weekdays", "Monday");
            contentValues.put("weekends", "0");
            contentValues.put("swap", "0");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Storedays");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Storedays", null, contentValues);

            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("weekdays", "Tuesday");
            contentValues2.put("weekends", "0");
            contentValues2.put("swap", "0");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Storedays");
            resultUri = getContentResolver().insert(contentUri, contentValues2);
            getContentResolver().notifyChange(resultUri, null);
//
//                    db.insert("Storedays", null, contentValues2);

            ContentValues contentValues3 = new ContentValues();
            contentValues3.put("weekdays", "Wednesday");
            contentValues3.put("weekends", "0");
            contentValues3.put("swap", "0");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Storedays");
            resultUri = getContentResolver().insert(contentUri, contentValues3);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Storedays", null, contentValues3);

            ContentValues contentValues4 = new ContentValues();
            contentValues4.put("weekdays", "Thursday");
            contentValues4.put("weekends", "0");
            contentValues4.put("swap", "0");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Storedays");
            resultUri = getContentResolver().insert(contentUri, contentValues4);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Storedays", null, contentValues4);

            ContentValues contentValues5 = new ContentValues();
            contentValues5.put("weekdays", "0");
            contentValues5.put("weekends", "Friday");
            contentValues5.put("swap", "Friday");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Storedays");
            resultUri = getContentResolver().insert(contentUri, contentValues5);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Storedays", null, contentValues5);

            ContentValues contentValues6 = new ContentValues();
            contentValues6.put("weekdays", "0");
            contentValues6.put("weekends", "Saturday");
            contentValues6.put("swap", "Saturday");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Storedays");
            resultUri = getContentResolver().insert(contentUri, contentValues6);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Storedays", null, contentValues6);

            ContentValues contentValues7 = new ContentValues();
            contentValues7.put("weekdays", "0");
            contentValues7.put("weekends", "Sunday");
            contentValues7.put("swap", "Sunday");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Storedays");
            resultUri = getContentResolver().insert(contentUri, contentValues7);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Storedays", null, contentValues7);
        }
        user8.close();

        Cursor user9 = db.rawQuery("SELECT * FROM Alaramdays ", null);
        if (user9.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("selecteddays", "Monday");
            contentValues.put("unselecteddays", "0");
            contentValues.put("swap", "0");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Alaramdays");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Alaramdays", null, contentValues);

            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("selecteddays", "Tuesday");
            contentValues2.put("unselecteddays", "0");
            contentValues2.put("swap", "0");

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Alaramdays");
            resultUri = getContentResolver().insert(contentUri, contentValues2);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Alaramdays", null, contentValues2);

            ContentValues contentValues3 = new ContentValues();
            contentValues3.put("selecteddays", "Wednesday");
            contentValues3.put("unselecteddays", "0");
            contentValues3.put("swap", "0");

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Alaramdays");
            resultUri = getContentResolver().insert(contentUri, contentValues3);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Alaramdays", null, contentValues3);

            ContentValues contentValues4 = new ContentValues();
            contentValues4.put("selecteddays", "Thursday");
            contentValues4.put("unselecteddays", "0");
            contentValues4.put("swap", "0");

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Alaramdays");
            resultUri = getContentResolver().insert(contentUri, contentValues4);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Alaramdays", null, contentValues4);

            ContentValues contentValues5 = new ContentValues();
            contentValues5.put("selecteddays", "Friday");
            contentValues5.put("unselecteddays", "0");
            contentValues5.put("swap", "0");

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Alaramdays");
            resultUri = getContentResolver().insert(contentUri, contentValues5);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Alaramdays", null, contentValues5);

            ContentValues contentValues6 = new ContentValues();
            contentValues6.put("selecteddays", "Saturday");
            contentValues6.put("unselecteddays", "0");
            contentValues6.put("swap", "0");

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Alaramdays");
            resultUri = getContentResolver().insert(contentUri, contentValues6);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Alaramdays", null, contentValues6);

            ContentValues contentValues7 = new ContentValues();
            contentValues7.put("selecteddays", "Sunday");
            contentValues7.put("unselecteddays", "0");
            contentValues7.put("swap", "0");

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Alaramdays");
            resultUri = getContentResolver().insert(contentUri, contentValues7);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Alaramdays", null, contentValues7);
        }
        user9.close();

        Cursor user10 = db.rawQuery("SELECT * FROM Alaramtime ", null);
        if (user10.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("time", "11:30 PM");

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Alaramtime");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Alaramtime", null, contentValues);
        }
        user10.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor two = db.rawQuery("SELECT * FROM Stockreset ", null);
        if (two.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("stockresettype", "off");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Stockreset");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Stockreset", null, contentValues);
        }
        two.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor three = db.rawQuery("SELECT * FROM Stockcontrol ", null);
        if (three.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("stockcontroltype", "off");

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Stockcontrol");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Stockcontrol", null, contentValues);
        }
        three.close();


        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor threqe = db.rawQuery("SELECT * FROM KOT_print ", null);
        if (threqe.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("kot_print_status", "Yes");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "KOT_print");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("KOT_print", null, contentValues);
        }
        threqe.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor tathreqe = db.rawQuery("SELECT * FROM Auto_Connect ", null);
        if (tathreqe.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("auto_connect_status", "No");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Auto_Connect");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Auto_Connect", null, contentValues);
        }
        tathreqe.close();


        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor tathreqeee = db.rawQuery("SELECT * FROM Round_off ", null);
        if (tathreqeee.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("round_off_status", "No");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Round_off");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
        }
        tathreqeee.close();





        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor tathreqee = db.rawQuery("SELECT * FROM Sync_time ", null);
        if (tathreqee.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("last_time", "05 Apr 18, 05:22 PM");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Sync_time");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Sync_time", null, contentValues);
        }
        tathreqee.close();


        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor tthreqeee = db.rawQuery("SELECT * FROM Weighing_Scale_status ", null);
        if (tthreqeee.moveToFirst()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("Weighing_Scale_onoff", "Not Connected");
            String where = "_id = '1'";

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Weighing_Scale_status");
            getContentResolver().update(contentUri, contentValues, where,new String[]{});
            resultUri = new Uri.Builder()
                    .scheme("content")
                    .authority(StubProviderApp.AUTHORITY)
                    .path("Weighing_Scale_status")
                    .appendQueryParameter("operation", "update")
                    .appendQueryParameter("_id","1")
                    .build();
            getContentResolver().notifyChange(resultUri, null);


//                    db.update("Weighing_Scale_status", contentValues, where, new String[]{});
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("Weighing_Scale_onoff", "Not Connected");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Weighing_Scale_status");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Weighing_Scale_status", null, contentValues);
        }
        tthreqeee.close();

        Cursor threete = db.rawQuery("SELECT * FROM Itemsort ", null);
        if (threete.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("itemsorttype", "Sort by Date added");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Itemsort");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Itemsort", null, contentValues);
        }
        threete.close();

        Cursor threetee = db.rawQuery("SELECT * FROM DeleteDB_time ", null);
        if (threetee.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("time", "11:30 PM");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "DeleteDB_time");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("DeleteDB_time", null, contentValues);
        }
        threetee.close();

        Cursor threet = db.rawQuery("SELECT * FROM DeleteDBon_off ", null);
        if (threet.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("status", "off");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "DeleteDBon_off");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("DeleteDBon_off", null, contentValues);
        }
        threet.close();

        Cursor threetf = db.rawQuery("SELECT * FROM Auto_generate_barcode ", null);
        if (threetf.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("generate", "off");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Auto_generate_barcode");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Auto_generate_barcode", null, contentValues);
        }
        threetf.close();


        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor four = db.rawQuery("SELECT * FROM Totaltables ", null);
        if (four.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("nooftables", "4");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Totaltables");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Totaltables", null, contentValues);
        }
        four.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor six = db.rawQuery("SELECT * FROM IPConn ", null);
        if (six.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("ipname", "192.168.1.87");
            contentValues.put("port", "9100");
            contentValues.put("status", "");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "IPConn");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("IPConn", null, contentValues);
        }
        six.close();


        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor six1 = db.rawQuery("SELECT * FROM IPConn_KOT1 ", null);
        if (six1.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("ipname", "192.168.1.88");
            contentValues.put("port", "9100");
            contentValues.put("status", "");
            contentValues.put("kot_name", "kot1");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "IPConn_KOT1");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("IPConn_KOT1", null, contentValues);
        }
        six1.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor six2 = db.rawQuery("SELECT * FROM IPConn_KOT2 ", null);
        if (six2.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("ipname", "192.168.1.89");
            contentValues.put("port", "9100");
            contentValues.put("status", "");
            contentValues.put("kot_name", "kot2");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "IPConn_KOT2");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("IPConn_KOT2", null, contentValues);
        }
        six2.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor six3 = db.rawQuery("SELECT * FROM IPConn_KOT3 ", null);
        if (six3.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("ipname", "192.168.1.90");
            contentValues.put("port", "9100");
            contentValues.put("status", "");
            contentValues.put("kot_name", "kot3");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "IPConn_KOT3");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("IPConn_KOT3", null, contentValues);
        }
        six3.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor six4 = db.rawQuery("SELECT * FROM IPConn_KOT4 ", null);
        if (six4.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("ipname", "192.168.1.91");
            contentValues.put("port", "9100");
            contentValues.put("status", "");
            contentValues.put("kot_name", "kot4");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "IPConn_KOT4");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("IPConn_KOT4", null, contentValues);
        }
        six4.close();


        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor dept1 = db.rawQuery("SELECT * FROM Name_Dept1 ", null);
        if (dept1.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("dept1_name", "kot1");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Name_Dept1");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//            db.insert("Name_Dept1", null, contentValues);
        }
        dept1.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor dept2 = db.rawQuery("SELECT * FROM Name_Dept2 ", null);
        if (dept2.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("dept2_name", "kot2");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Name_Dept2");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//            db.insert("Name_Dept2", null, contentValues);
        }
        dept2.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor dept3 = db.rawQuery("SELECT * FROM Name_Dept3 ", null);
        if (dept3.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("dept3_name", "kot3");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Name_Dept3");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//            db.insert("Name_Dept3", null, contentValues);
        }
        dept3.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor dept4 = db.rawQuery("SELECT * FROM Name_Dept4 ", null);
        if (dept4.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("dept4_name", "kot4");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Name_Dept4");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//            db.insert("Name_Dept4", null, contentValues);
        }
        dept4.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor server_ip = db.rawQuery("SELECT * FROM Ordertaking_server_ip ", null);
        if (server_ip.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("ipname", "192.168.1.100");
            contentValues.put("port", "8080");
            contentValues.put("status", "");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ordertaking_server_ip");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Ordertaking_server_ip", null, contentValues);
        }
        server_ip.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor home_deli_no_of_copy = db.rawQuery("SELECT * FROM HomeDelivery_prints ", null);
        if (home_deli_no_of_copy.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("companycopy", "yes");
            contentValues.put("customercopy", "yes");
            db.insert("HomeDelivery_prints", null, contentValues);
        }
        home_deli_no_of_copy.close();


        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor rest_merch_id = db.rawQuery("SELECT * FROM Restaurant_id ", null);
        if (rest_merch_id.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("merchant_id", "0");
//            db.insert("Restaurant_id", null, contentValues);

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Restaurant_id");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
        }
        rest_merch_id.close();


        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor cust_loya_poin = db.rawQuery("SELECT * FROM loyalty_points ", null);
        if (cust_loya_poin.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("money", "0");
            contentValues.put("point", "0");
            contentValues.put("point2", "0");
            contentValues.put("money2", "0");
//            db.insert("loyalty_points", null, contentValues);

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "loyalty_points");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
        }
        cust_loya_poin.close();


        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor cust_loya_poin_sta = db.rawQuery("SELECT * FROM loyalty_points_status ", null);
        if (cust_loya_poin_sta.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("status", "no");
//            db.insert("loyalty_points_status", null, contentValues);

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "loyalty_points_status");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
        }
        cust_loya_poin_sta.close();


        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor fgh = db.rawQuery("SELECT * FROM IPConn", null);
        if (fgh.moveToFirst()) {
            String id = fgh.getString(0);
            ContentValues contentValues = new ContentValues();
            contentValues.put("printer_name", "TM Printer");
            String wherecu1 = "_id = '" + id + "'";

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "IPConn");
            getContentResolver().update(contentUri, contentValues, wherecu1,new String[]{});
            resultUri = new Uri.Builder()
                    .scheme("content")
                    .authority(StubProviderApp.AUTHORITY)
                    .path("IPConn")
                    .appendQueryParameter("operation", "update")
                    .appendQueryParameter("_id",id)
                    .build();
            getContentResolver().notifyChange(resultUri, null);

//                    db.update("IPConn", contentValues, wherecu1, new String[]{});
//                Toast.makeText(MainActivity.this, "updated", Toast.LENGTH_SHORT).show();
        } else {
//                Toast.makeText(MainActivity.this, "not updated", Toast.LENGTH_SHORT).show();
        }
        fgh.close();


        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor sixx = db.rawQuery("SELECT * FROM IPConn_Counter ", null);
        if (sixx.moveToFirst()) {

        }else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("ipname", "192.168.1.92");
            contentValues.put("port", "9100");
            contentValues.put("status", "");

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "IPConn_Counter");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);

          //  db.insert("IPConn_Counter", null, contentValues);
        }
        sixx.close();


        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor sixxx = db.rawQuery("SELECT * FROM Country_Selection ", null);
        if (sixxx.moveToFirst()) {

        }else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("country", "India");
       //     db.insert("Country_Selection", null, contentValues);

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Country_Selection");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
        }
        sixxx.close();










        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor one_1 = db.rawQuery("SELECT * FROM User_privilege ", null);
        if (one_1.moveToFirst()) {

        } else {
            Cursor cursor = db.rawQuery("SELECT * FROM User1", null);
            if (cursor.moveToFirst()) {
                getuser1_name = cursor.getString(2);
                ContentValues contentValues = new ContentValues();
                contentValues.put("username", getuser1_name);
                contentValues.put("returns_refunds", "no");
                contentValues.put("product_tax", "no");
                contentValues.put("reports", "no");
                contentValues.put("settings", "no");
                contentValues.put("backup", "no");
                contentValues.put("customer", "no");
                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User_privilege");
                resultUri = getContentResolver().insert(contentUri, contentValues);
                getContentResolver().notifyChange(resultUri, null);
//                        db.insert("User_privilege", null, contentValues);
            }
            cursor.close();

            Cursor cursor2 = db.rawQuery("SELECT * FROM User2", null);
            if (cursor2.moveToFirst()) {
                getuser2_name = cursor2.getString(2);
                ContentValues contentValues = new ContentValues();
                contentValues.put("username", getuser2_name);
                contentValues.put("returns_refunds", "no");
                contentValues.put("product_tax", "no");
                contentValues.put("reports", "no");
                contentValues.put("settings", "no");
                contentValues.put("backup", "no");
                contentValues.put("customer", "no");
                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User_privilege");
                resultUri = getContentResolver().insert(contentUri, contentValues);
                getContentResolver().notifyChange(resultUri, null);
//                        db.insert("User_privilege", null, contentValues);
            }
            cursor2.close();

            Cursor cursor3 = db.rawQuery("SELECT * FROM User3", null);
            if (cursor3.moveToFirst()) {
                getuser3_name = cursor3.getString(2);
                ContentValues contentValues = new ContentValues();
                contentValues.put("username", getuser3_name);
                contentValues.put("returns_refunds", "no");
                contentValues.put("product_tax", "no");
                contentValues.put("reports", "no");
                contentValues.put("settings", "no");
                contentValues.put("backup", "no");
                contentValues.put("customer", "no");
                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User_privilege");
                resultUri = getContentResolver().insert(contentUri, contentValues);
                getContentResolver().notifyChange(resultUri, null);
//                        db.insert("User_privilege", null, contentValues);
            }
            cursor3.close();

            Cursor cursor4 = db.rawQuery("SELECT * FROM User4", null);
            if (cursor4.moveToFirst()) {
                getuser4_name = cursor4.getString(2);
                ContentValues contentValues = new ContentValues();
                contentValues.put("username", getuser4_name);
                contentValues.put("returns_refunds", "no");
                contentValues.put("product_tax", "no");
                contentValues.put("reports", "no");
                contentValues.put("settings", "no");
                contentValues.put("backup", "no");
                contentValues.put("customer", "no");
                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User_privilege");
                resultUri = getContentResolver().insert(contentUri, contentValues);
                getContentResolver().notifyChange(resultUri, null);
//                        db.insert("User_privilege", null, contentValues);
            }
            cursor4.close();

            Cursor cursor5 = db.rawQuery("SELECT * FROM User5", null);
            if (cursor5.moveToFirst()) {
                getuser5_name = cursor5.getString(2);
                ContentValues contentValues = new ContentValues();
                contentValues.put("username", getuser5_name);
                contentValues.put("returns_refunds", "no");
                contentValues.put("product_tax", "no");
                contentValues.put("reports", "no");
                contentValues.put("settings", "no");
                contentValues.put("backup", "no");
                contentValues.put("customer", "no");
                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User_privilege");
                resultUri = getContentResolver().insert(contentUri, contentValues);
                getContentResolver().notifyChange(resultUri, null);
//                        db.insert("User_privilege", null, contentValues);
            }
            cursor5.close();

            Cursor cursor6 = db.rawQuery("SELECT * FROM User6", null);
            if (cursor6.moveToFirst()) {
                getuser6_name = cursor6.getString(2);
                ContentValues contentValues = new ContentValues();
                contentValues.put("username", getuser6_name);
                contentValues.put("returns_refunds", "no");
                contentValues.put("product_tax", "no");
                contentValues.put("reports", "no");
                contentValues.put("settings", "no");
                contentValues.put("backup", "no");
                contentValues.put("customer", "no");
                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User_privilege");
                resultUri = getContentResolver().insert(contentUri, contentValues);
                getContentResolver().notifyChange(resultUri, null);
//                        db.insert("User_privilege", null, contentValues);
            }
            cursor6.close();

        }
        one_1.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor seven = db.rawQuery("SELECT * FROM BTConn ", null);
        if (seven.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", "");
            contentValues.put("address", "");
            contentValues.put("status", "");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "BTConn");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("BTConn", null, contentValues);
        }
        seven.close();



        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        int limit1 = 1;
        Cursor add_item = db.rawQuery("SELECT * FROM Items LIMIT '" + limit1 + "'", null);
        if (add_item.moveToFirst()) {
            no_items = 0;
        } else {

            no_items = 1;
//            db.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE NAME = 'Items'");
//            db.execSQL("delete from hotel");
//            db.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE NAME = 'Hotel'");
//
//            webservicequery("UPDATE sqlite_sequence SET seq = 0 WHERE NAME = 'items'");
//            webservicequery("delete from hotel");
//            webservicequery("UPDATE sqlite_sequence SET seq = 0 WHERE NAME = 'Hotel'");

            //for (int i=0; i<60; i++){
            byte[] img;
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.idly);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 100, bos);
            img = bos.toByteArray();
            ContentValues contentValues = new ContentValues();
            //contentValues.put("_id", "1");
            contentValues.put("itemname", "idly");
            contentValues.put("price", "25");
            contentValues.put("stockquan", "50");
            contentValues.put("category", "Tiffin");
            contentValues.put("itemtax", "None");
            contentValues.put("image", img);
            contentValues.put("favourites", "yes");
            contentValues.put("weekdaysvalue", "0");
            contentValues.put("weekendsvalue", "0");
            contentValues.put("barcode_value", "");
            contentValues.put("disc_type", "%");
            contentValues.put("disc_value", "0");
            contentValues.put("quantity_sold", "0");
            contentValues.put("minimum_qty", "0");
            contentValues.put("minimum_qty_copy", "0");
            contentValues.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues);

            byte[] img1;
            Bitmap b1 = BitmapFactory.decodeResource(getResources(), R.drawable.dosa);
            ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
            b1.compress(Bitmap.CompressFormat.PNG, 100, bos1);
            img1 = bos1.toByteArray();
            ContentValues contentValues1 = new ContentValues();
            //contentValues1.put("_id", "2");
            contentValues1.put("itemname", "dosa");
            contentValues1.put("price", "35");
            contentValues1.put("stockquan", "50");
            contentValues1.put("category", "Tiffin");
            contentValues1.put("itemtax", "None");
            contentValues1.put("image", img1);
            contentValues1.put("favourites", "yes");
            contentValues1.put("weekdaysvalue", "0");
            contentValues1.put("weekendsvalue", "0");
            contentValues1.put("barcode_value", "");
            contentValues1.put("disc_type", "%");
            contentValues1.put("disc_value", "0");
            contentValues1.put("quantity_sold", "0");
            contentValues1.put("minimum_qty", "0");
            contentValues1.put("minimum_qty_copy", "0");
            contentValues1.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues1);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues1);

            byte[] img2;
            Bitmap b2 = BitmapFactory.decodeResource(getResources(), R.drawable.pongal);
            ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
            b2.compress(Bitmap.CompressFormat.PNG, 100, bos2);
            img2 = bos2.toByteArray();
            ContentValues contentValues2 = new ContentValues();
            //contentValues2.put("_id", "3");
            contentValues2.put("itemname", "pongal");
            contentValues2.put("price", "30");
            contentValues2.put("stockquan", "50");
            contentValues2.put("category", "Tiffin");
            contentValues2.put("itemtax", "None");
            contentValues2.put("image", img2);
            contentValues2.put("favourites", "yes");
            contentValues2.put("weekdaysvalue", "0");
            contentValues2.put("weekendsvalue", "0");
            contentValues2.put("barcode_value", "");
            contentValues2.put("disc_type", "%");
            contentValues2.put("disc_value", "0");
            contentValues2.put("quantity_sold", "0");
            contentValues2.put("minimum_qty", "0");
            contentValues2.put("minimum_qty_copy", "0");
            contentValues2.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues2);
//            getContentResolver().notifyChange(resultUri, null);

                    db.insert("Items", null, contentValues2);

            byte[] img3;
            Bitmap b3 = BitmapFactory.decodeResource(getResources(), R.drawable.upma);
            ByteArrayOutputStream bos3 = new ByteArrayOutputStream();
            b3.compress(Bitmap.CompressFormat.PNG, 100, bos3);
            img3 = bos3.toByteArray();
            ContentValues contentValues3 = new ContentValues();
            //contentValues3.put("_id", "4");
            contentValues3.put("itemname", "upma");
            contentValues3.put("price", "30");
            contentValues3.put("stockquan", "50");
            contentValues3.put("category", "Tiffin");
            contentValues3.put("itemtax", "None");
            contentValues3.put("image", img3);
            contentValues3.put("favourites", "yes");
            contentValues3.put("weekdaysvalue", "0");
            contentValues3.put("weekendsvalue", "0");
            contentValues3.put("barcode_value", "");
            contentValues3.put("disc_type", "%");
            contentValues3.put("disc_value", "0");
            contentValues3.put("quantity_sold", "0");
            contentValues3.put("minimum_qty", "0");
            contentValues3.put("minimum_qty_copy", "0");
            contentValues3.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues3);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues3);

            byte[] img4;
            Bitmap b4 = BitmapFactory.decodeResource(getResources(), R.drawable.poori);
            ByteArrayOutputStream bos4 = new ByteArrayOutputStream();
            b4.compress(Bitmap.CompressFormat.PNG, 100, bos4);
            img4 = bos4.toByteArray();
            ContentValues contentValues4 = new ContentValues();
            //contentValues4.put("_id", "5");
            contentValues4.put("itemname", "poori");
            contentValues4.put("price", "35");
            contentValues4.put("stockquan", "50");
            contentValues4.put("category", "Tiffin");
            contentValues4.put("itemtax", "None");
            contentValues4.put("image", img4);
            contentValues4.put("favourites", "no");
            contentValues4.put("weekdaysvalue", "0");
            contentValues4.put("weekendsvalue", "0");
            contentValues4.put("barcode_value", "");
            contentValues4.put("disc_type", "%");
            contentValues4.put("disc_value", "0");
            contentValues4.put("quantity_sold", "0");
            contentValues4.put("minimum_qty", "0");
            contentValues4.put("minimum_qty_copy", "0");
            contentValues4.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues4);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues4);

            byte[] img5;
            Bitmap b5 = BitmapFactory.decodeResource(getResources(), R.drawable.masal_dosa);
            ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
            b5.compress(Bitmap.CompressFormat.PNG, 100, bos5);
            img5 = bos5.toByteArray();
            ContentValues contentValues5 = new ContentValues();
            //contentValues5.put("_id", "6");
            contentValues5.put("itemname", "masal dosa");
            contentValues5.put("price", "40");
            contentValues5.put("stockquan", "50");
            contentValues5.put("category", "Tiffin");
            contentValues5.put("itemtax", "None");
            contentValues5.put("image", img5);
            contentValues5.put("favourites", "no");
            contentValues5.put("weekdaysvalue", "0");
            contentValues5.put("weekendsvalue", "0");
            contentValues5.put("barcode_value", "");
            contentValues5.put("disc_type", "%");
            contentValues5.put("disc_value", "0");
            contentValues5.put("quantity_sold", "0");
            contentValues5.put("minimum_qty", "0");
            contentValues5.put("minimum_qty_copy", "0");
            contentValues5.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues5);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues5);

            byte[] img6;
            Bitmap b6 = BitmapFactory.decodeResource(getResources(), R.drawable.bread_omlette);
            ByteArrayOutputStream bos6 = new ByteArrayOutputStream();
            b6.compress(Bitmap.CompressFormat.PNG, 100, bos6);
            img6 = bos6.toByteArray();
            ContentValues contentValues6 = new ContentValues();
            //contentValues6.put("_id", "7");
            contentValues6.put("itemname", "bread omlette");
            contentValues6.put("price", "30");
            contentValues6.put("stockquan", "20");
            contentValues6.put("category", "Tiffin");
            contentValues6.put("itemtax", "None");
            contentValues6.put("image", img6);
            contentValues6.put("favourites", "no");
            contentValues6.put("weekdaysvalue", "0");
            contentValues6.put("weekendsvalue", "0");
            contentValues6.put("barcode_value", "");
            contentValues6.put("disc_type", "%");
            contentValues6.put("disc_value", "0");
            contentValues6.put("quantity_sold", "0");
            contentValues6.put("minimum_qty", "0");
            contentValues6.put("minimum_qty_copy", "0");
            contentValues6.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues6);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues6);

            byte[] img7;
            Bitmap b7 = BitmapFactory.decodeResource(getResources(), R.drawable.onion_dosa);
            ByteArrayOutputStream bos7 = new ByteArrayOutputStream();
            b7.compress(Bitmap.CompressFormat.PNG, 100, bos7);
            img7 = bos7.toByteArray();
            ContentValues contentValues7 = new ContentValues();
            //contentValues7.put("_id", "8");
            contentValues7.put("itemname", "onion dosa");
            contentValues7.put("price", "35");
            contentValues7.put("stockquan", "30");
            contentValues7.put("category", "Tiffin");
            contentValues7.put("itemtax", "None");
            contentValues7.put("image", img7);
            contentValues7.put("favourites", "no");
            contentValues7.put("weekdaysvalue", "0");
            contentValues7.put("weekendsvalue", "0");
            contentValues7.put("barcode_value", "");
            contentValues7.put("disc_type", "%");
            contentValues7.put("disc_value", "0");
            contentValues7.put("quantity_sold", "0");
            contentValues7.put("minimum_qty", "0");
            contentValues7.put("minimum_qty_copy", "0");
            contentValues7.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues7);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues7);

            byte[] img8;
            Bitmap b8 = BitmapFactory.decodeResource(getResources(), R.drawable.rava_dosa);
            ByteArrayOutputStream bos8 = new ByteArrayOutputStream();
            b8.compress(Bitmap.CompressFormat.PNG, 100, bos8);
            img8 = bos8.toByteArray();
            ContentValues contentValues8 = new ContentValues();
            //contentValues8.put("_id", "9");
            contentValues8.put("itemname", "rava dosa");
            contentValues8.put("price", "35");
            contentValues8.put("stockquan", "30");
            contentValues8.put("category", "Tiffin");
            contentValues8.put("itemtax", "None");
            contentValues8.put("image", img8);
            contentValues8.put("favourites", "no");
            contentValues8.put("weekdaysvalue", "0");
            contentValues8.put("weekendsvalue", "0");
            contentValues8.put("barcode_value", "");
            contentValues8.put("disc_type", "%");
            contentValues8.put("disc_value", "0");
            contentValues8.put("quantity_sold", "0");
            contentValues8.put("minimum_qty", "0");
            contentValues8.put("minimum_qty_copy", "0");
            contentValues8.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues8);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues8);

            byte[] img9;
            Bitmap b9 = BitmapFactory.decodeResource(getResources(), R.drawable.uthappam);
            ByteArrayOutputStream bos9 = new ByteArrayOutputStream();
            b9.compress(Bitmap.CompressFormat.PNG, 100, bos9);
            img9 = bos9.toByteArray();
            ContentValues contentValues9 = new ContentValues();
            //contentValues9.put("_id", "10");
            contentValues9.put("itemname", "uthappam");
            contentValues9.put("price", "30");
            contentValues9.put("stockquan", "30");
            contentValues9.put("category", "Tiffin");
            contentValues9.put("itemtax", "None");
            contentValues9.put("image", img9);
            contentValues9.put("favourites", "no");
            contentValues9.put("weekdaysvalue", "0");
            contentValues9.put("weekendsvalue", "0");
            contentValues9.put("barcode_value", "");
            contentValues9.put("disc_type", "%");
            contentValues9.put("disc_value", "0");
            contentValues9.put("quantity_sold", "0");
            contentValues9.put("minimum_qty", "0");
            contentValues9.put("minimum_qty_copy", "0");
            contentValues9.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues9);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues9);

            byte[] img10;
            Bitmap b10 = BitmapFactory.decodeResource(getResources(), R.drawable.vada);
            ByteArrayOutputStream bos10 = new ByteArrayOutputStream();
            b10.compress(Bitmap.CompressFormat.PNG, 100, bos10);
            img10 = bos10.toByteArray();
            ContentValues contentValues10 = new ContentValues();
            //contentValues10.put("_id", "11");
            contentValues10.put("itemname", "vada");
            contentValues10.put("price", "20");
            contentValues10.put("stockquan", "100");
            contentValues10.put("category", "Snacks");
            contentValues10.put("itemtax", "None");
            contentValues10.put("image", img10);
            contentValues10.put("favourites", "yes");
            contentValues10.put("weekdaysvalue", "0");
            contentValues10.put("weekendsvalue", "0");
            contentValues10.put("barcode_value", "");
            contentValues10.put("disc_type", "%");
            contentValues10.put("disc_value", "0");
            contentValues10.put("quantity_sold", "0");
            contentValues10.put("minimum_qty", "0");
            contentValues10.put("minimum_qty_copy", "0");
            contentValues10.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues10);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues10);

            byte[] img11;
            Bitmap b11 = BitmapFactory.decodeResource(getResources(), R.drawable.masal_vada);
            ByteArrayOutputStream bos11 = new ByteArrayOutputStream();
            b11.compress(Bitmap.CompressFormat.PNG, 100, bos11);
            img11 = bos11.toByteArray();
            ContentValues contentValues11 = new ContentValues();
            //contentValues11.put("_id", "12");
            contentValues11.put("itemname", "masal vada");
            contentValues11.put("price", "20");
            contentValues11.put("stockquan", "50");
            contentValues11.put("category", "Snacks");
            contentValues11.put("itemtax", "None");
            contentValues11.put("image", img11);
            contentValues11.put("favourites", "yes");
            contentValues11.put("weekdaysvalue", "0");
            contentValues11.put("weekendsvalue", "0");
            contentValues11.put("barcode_value", "");
            contentValues11.put("disc_type", "%");
            contentValues11.put("disc_value", "0");
            contentValues11.put("quantity_sold", "0");
            contentValues11.put("minimum_qty", "0");
            contentValues11.put("minimum_qty_copy", "0");
            contentValues11.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues11);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues11);

            byte[] img12;
            Bitmap b12 = BitmapFactory.decodeResource(getResources(), R.drawable.samosa);
            ByteArrayOutputStream bos12 = new ByteArrayOutputStream();
            b12.compress(Bitmap.CompressFormat.PNG, 100, bos12);
            img12 = bos12.toByteArray();
            ContentValues contentValues12 = new ContentValues();
            //contentValues12.put("_id", "13");
            contentValues12.put("itemname", "samosa");
            contentValues12.put("price", "20");
            contentValues12.put("stockquan", "50");
            contentValues12.put("category", "Snacks");
            contentValues12.put("itemtax", "None");
            contentValues12.put("image", img12);
            contentValues12.put("favourites", "yes");
            contentValues12.put("weekdaysvalue", "0");
            contentValues12.put("weekendsvalue", "0");
            contentValues12.put("barcode_value", "");
            contentValues12.put("disc_type", "%");
            contentValues12.put("disc_value", "0");
            contentValues12.put("quantity_sold", "0");
            contentValues12.put("minimum_qty", "0");
            contentValues12.put("minimum_qty_copy", "0");
            contentValues12.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues12);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues12);

            byte[] img13;
            Bitmap b13 = BitmapFactory.decodeResource(getResources(), R.drawable.chicken_kabab);
            ByteArrayOutputStream bos13 = new ByteArrayOutputStream();
            b13.compress(Bitmap.CompressFormat.PNG, 100, bos13);
            img13 = bos13.toByteArray();
            ContentValues contentValues13 = new ContentValues();
            //contentValues13.put("_id", "14");
            contentValues13.put("itemname", "chicken kabab");
            contentValues13.put("price", "60");
            contentValues13.put("stockquan", "20");
            contentValues13.put("category", "Snacks");
            contentValues13.put("itemtax", "None");
            contentValues13.put("image", img13);
            contentValues13.put("favourites", "yes");
            contentValues13.put("weekdaysvalue", "0");
            contentValues13.put("weekendsvalue", "0");
            contentValues13.put("barcode_value", "");
            contentValues13.put("disc_type", "%");
            contentValues13.put("disc_value", "0");
            contentValues13.put("quantity_sold", "0");
            contentValues13.put("minimum_qty", "0");
            contentValues13.put("minimum_qty_copy", "0");
            contentValues13.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues13);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues13);

            byte[] img14;
            Bitmap b14 = BitmapFactory.decodeResource(getResources(), R.drawable.sheekh_kabab);
            ByteArrayOutputStream bos14 = new ByteArrayOutputStream();
            b14.compress(Bitmap.CompressFormat.PNG, 100, bos14);
            img14 = bos14.toByteArray();
            ContentValues contentValues14 = new ContentValues();
            //contentValues14.put("_id", "15");
            contentValues14.put("itemname", "sheekh kabab");
            contentValues14.put("price", "80");
            contentValues14.put("stockquan", "20");
            contentValues14.put("category", "Snacks");
            contentValues14.put("itemtax", "None");
            contentValues14.put("image", img14);
            contentValues14.put("favourites", "no");
            contentValues14.put("weekdaysvalue", "0");
            contentValues14.put("weekendsvalue", "0");
            contentValues14.put("barcode_value", "");
            contentValues14.put("disc_type", "%");
            contentValues14.put("disc_value", "0");
            contentValues14.put("quantity_sold", "0");
            contentValues14.put("minimum_qty", "0");
            contentValues14.put("minimum_qty_copy", "0");
            contentValues14.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues14);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues14);

            byte[] img15;
            Bitmap b15 = BitmapFactory.decodeResource(getResources(), R.drawable.pani_puri);
            ByteArrayOutputStream bos15 = new ByteArrayOutputStream();
            b15.compress(Bitmap.CompressFormat.PNG, 100, bos15);
            img15 = bos15.toByteArray();
            ContentValues contentValues15 = new ContentValues();
            //contentValues15.put("_id", "16");
            contentValues15.put("itemname", "pani puri");
            contentValues15.put("price", "35");
            contentValues15.put("stockquan", "30");
            contentValues15.put("category", "Snacks");
            contentValues15.put("itemtax", "None");
            contentValues15.put("image", img15);
            contentValues15.put("favourites", "no");
            contentValues15.put("weekdaysvalue", "0");
            contentValues15.put("weekendsvalue", "0");
            contentValues15.put("barcode_value", "");
            contentValues15.put("disc_type", "%");
            contentValues15.put("disc_value", "0");
            contentValues15.put("quantity_sold", "0");
            contentValues15.put("minimum_qty", "0");
            contentValues15.put("minimum_qty_copy", "0");
            contentValues15.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues15);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues15);

            byte[] img16;
            Bitmap b16 = BitmapFactory.decodeResource(getResources(), R.drawable.pav_bhajji);
            ByteArrayOutputStream bos16 = new ByteArrayOutputStream();
            b16.compress(Bitmap.CompressFormat.PNG, 100, bos16);
            img16 = bos16.toByteArray();
            ContentValues contentValues16 = new ContentValues();
            //contentValues16.put("_id", "17");
            contentValues16.put("itemname", "pav bhajji");
            contentValues16.put("price", "60");
            contentValues16.put("stockquan", "30");
            contentValues16.put("category", "Snacks");
            contentValues16.put("itemtax", "None");
            contentValues16.put("image", img16);
            contentValues16.put("favourites", "no");
            contentValues16.put("weekdaysvalue", "0");
            contentValues16.put("weekendsvalue", "0");
            contentValues16.put("barcode_value", "");
            contentValues16.put("disc_type", "%");
            contentValues16.put("disc_value", "0");
            contentValues16.put("quantity_sold", "0");
            contentValues16.put("minimum_qty", "0");
            contentValues16.put("minimum_qty_copy", "0");
            contentValues16.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues16);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues16);

            byte[] img17;
            Bitmap b17 = BitmapFactory.decodeResource(getResources(), R.drawable.veg_cutlet);
            ByteArrayOutputStream bos17 = new ByteArrayOutputStream();
            b17.compress(Bitmap.CompressFormat.PNG, 100, bos17);
            img17 = bos17.toByteArray();
            ContentValues contentValues17 = new ContentValues();
            //contentValues17.put("_id", "18");
            contentValues17.put("itemname", "veg cutlet");
            contentValues17.put("price", "40");
            contentValues17.put("stockquan", "30");
            contentValues17.put("category", "Snacks");
            contentValues17.put("itemtax", "None");
            contentValues17.put("image", img17);
            contentValues17.put("favourites", "no");
            contentValues17.put("weekdaysvalue", "0");
            contentValues17.put("weekendsvalue", "0");
            contentValues17.put("barcode_value", "");
            contentValues17.put("disc_type", "%");
            contentValues17.put("disc_value", "0");
            contentValues17.put("quantity_sold", "0");
            contentValues17.put("minimum_qty", "0");
            contentValues17.put("minimum_qty_copy", "0");
            contentValues17.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues17);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues17);

            byte[] img18;
            Bitmap b18 = BitmapFactory.decodeResource(getResources(), R.drawable.masal_puri);
            ByteArrayOutputStream bos18 = new ByteArrayOutputStream();
            b18.compress(Bitmap.CompressFormat.PNG, 100, bos18);
            img18 = bos18.toByteArray();
            ContentValues contentValues18 = new ContentValues();
            //contentValues18.put("_id", "19");
            contentValues18.put("itemname", "masal puri");
            contentValues18.put("price", "35");
            contentValues18.put("stockquan", "30");
            contentValues18.put("category", "Snacks");
            contentValues18.put("itemtax", "None");
            contentValues18.put("image", img18);
            contentValues18.put("favourites", "no");
            contentValues18.put("weekdaysvalue", "0");
            contentValues18.put("weekendsvalue", "0");
            contentValues18.put("barcode_value", "");
            contentValues18.put("disc_type", "%");
            contentValues18.put("disc_value", "0");
            contentValues18.put("quantity_sold", "0");
            contentValues18.put("minimum_qty", "0");
            contentValues18.put("minimum_qty_copy", "0");
            contentValues18.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues18);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues18);

            byte[] img19;
            Bitmap b19 = BitmapFactory.decodeResource(getResources(), R.drawable.chicken_nuggets);
            ByteArrayOutputStream bos19 = new ByteArrayOutputStream();
            b19.compress(Bitmap.CompressFormat.PNG, 100, bos19);
            img19 = bos19.toByteArray();
            ContentValues contentValues19 = new ContentValues();
            //contentValues19.put("_id", "20");
            contentValues19.put("itemname", "chicken nuggets");
            contentValues19.put("price", "65");
            contentValues19.put("stockquan", "10");
            contentValues19.put("category", "Snacks");
            contentValues19.put("itemtax", "None");
            contentValues19.put("image", img19);
            contentValues19.put("favourites", "no");
            contentValues19.put("weekdaysvalue", "0");
            contentValues19.put("weekendsvalue", "0");
            contentValues19.put("barcode_value", "");
            contentValues19.put("disc_type", "%");
            contentValues19.put("disc_value", "0");
            contentValues19.put("quantity_sold", "0");
            contentValues19.put("minimum_qty", "0");
            contentValues19.put("minimum_qty_copy", "0");
            contentValues19.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues19);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues19);

            byte[] img20;
            Bitmap b20 = BitmapFactory.decodeResource(getResources(), R.drawable.veg_biriyani);
            ByteArrayOutputStream bos20 = new ByteArrayOutputStream();
            b20.compress(Bitmap.CompressFormat.PNG, 100, bos20);
            img20 = bos20.toByteArray();
            ContentValues contentValues20 = new ContentValues();
            //contentValues20.put("_id", "21");
            contentValues20.put("itemname", "veg biriyani");
            contentValues20.put("price", "70");
            contentValues20.put("stockquan", "50");
            contentValues20.put("category", "Main course");
            contentValues20.put("itemtax", "None");
            contentValues20.put("image", img20);
            contentValues20.put("favourites", "yes");
            contentValues20.put("weekdaysvalue", "0");
            contentValues20.put("weekendsvalue", "0");
            contentValues20.put("barcode_value", "");
            contentValues20.put("disc_type", "%");
            contentValues20.put("disc_value", "0");
            contentValues20.put("quantity_sold", "0");
            contentValues20.put("minimum_qty", "0");
            contentValues20.put("minimum_qty_copy", "0");
            contentValues20.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues20);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues20);

            byte[] img21;
            Bitmap b21 = BitmapFactory.decodeResource(getResources(), R.drawable.chicken_biriyani);
            ByteArrayOutputStream bos21 = new ByteArrayOutputStream();
            b21.compress(Bitmap.CompressFormat.PNG, 100, bos21);
            img21 = bos21.toByteArray();
            ContentValues contentValues21 = new ContentValues();
            //contentValues21.put("_id", "22");
            contentValues21.put("itemname", "chicken biriyani");
            contentValues21.put("price", "100");
            contentValues21.put("stockquan", "50");
            contentValues21.put("category", "Main course");
            contentValues21.put("itemtax", "None");
            contentValues21.put("image", img21);
            contentValues21.put("favourites", "yes");
            contentValues21.put("weekdaysvalue", "0");
            contentValues21.put("weekendsvalue", "0");
            contentValues21.put("barcode_value", "");
            contentValues21.put("disc_type", "%");
            contentValues21.put("disc_value", "0");
            contentValues21.put("quantity_sold", "0");
            contentValues21.put("minimum_qty", "0");
            contentValues21.put("minimum_qty_copy", "0");
            contentValues21.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues21);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues21);

            byte[] img22;
            Bitmap b22 = BitmapFactory.decodeResource(getResources(), R.drawable.veg_thali);
            ByteArrayOutputStream bos22 = new ByteArrayOutputStream();
            b22.compress(Bitmap.CompressFormat.PNG, 100, bos22);
            img22 = bos22.toByteArray();
            ContentValues contentValues22 = new ContentValues();
            //contentValues22.put("_id", "23");
            contentValues22.put("itemname", "veg thali");
            contentValues22.put("price", "50");
            contentValues22.put("stockquan", "50");
            contentValues22.put("category", "Main course");
            contentValues22.put("itemtax", "None");
            contentValues22.put("image", img22);
            contentValues22.put("favourites", "yes");
            contentValues22.put("weekdaysvalue", "0");
            contentValues22.put("weekendsvalue", "0");
            contentValues22.put("barcode_value", "");
            contentValues22.put("disc_type", "%");
            contentValues22.put("disc_value", "0");
            contentValues22.put("quantity_sold", "0");
            contentValues22.put("minimum_qty", "0");
            contentValues22.put("minimum_qty_copy", "0");
            contentValues22.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues22);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues22);

            byte[] img23;
            Bitmap b23 = BitmapFactory.decodeResource(getResources(), R.drawable.non_veg_thali);
            ByteArrayOutputStream bos23 = new ByteArrayOutputStream();
            b23.compress(Bitmap.CompressFormat.PNG, 100, bos23);
            img23 = bos23.toByteArray();
            ContentValues contentValues23 = new ContentValues();
            //contentValues23.put("_id", "24");
            contentValues23.put("itemname", "non veg thali");
            contentValues23.put("price", "80");
            contentValues23.put("stockquan", "50");
            contentValues23.put("category", "Main course");
            contentValues23.put("itemtax", "None");
            contentValues23.put("image", img23);
            contentValues23.put("favourites", "yes");
            contentValues23.put("weekdaysvalue", "0");
            contentValues23.put("weekendsvalue", "0");
            contentValues23.put("barcode_value", "");
            contentValues23.put("disc_type", "%");
            contentValues23.put("disc_value", "0");
            contentValues23.put("quantity_sold", "0");
            contentValues23.put("minimum_qty", "0");
            contentValues23.put("minimum_qty_copy", "0");
            contentValues23.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues23);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues23);

            byte[] img24;
            Bitmap b24 = BitmapFactory.decodeResource(getResources(), R.drawable.mutton_biriyani);
            ByteArrayOutputStream bos24 = new ByteArrayOutputStream();
            b24.compress(Bitmap.CompressFormat.PNG, 100, bos24);
            img24 = bos24.toByteArray();
            ContentValues contentValues24 = new ContentValues();
            //contentValues24.put("_id", "25");
            contentValues24.put("itemname", "mutton biriyani");
            contentValues24.put("price", "120");
            contentValues24.put("stockquan", "50");
            contentValues24.put("category", "Main course");
            contentValues24.put("itemtax", "None");
            contentValues24.put("image", img24);
            contentValues24.put("favourites", "no");
            contentValues24.put("weekdaysvalue", "0");
            contentValues24.put("weekendsvalue", "0");
            contentValues24.put("barcode_value", "");
            contentValues24.put("disc_type", "%");
            contentValues24.put("disc_value", "0");
            contentValues24.put("quantity_sold", "0");
            contentValues24.put("minimum_qty", "0");
            contentValues24.put("minimum_qty_copy", "0");
            contentValues24.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues24);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues24);

            byte[] img25;
            Bitmap b25 = BitmapFactory.decodeResource(getResources(), R.drawable.roti);
            ByteArrayOutputStream bos25 = new ByteArrayOutputStream();
            b25.compress(Bitmap.CompressFormat.PNG, 100, bos25);
            img25 = bos25.toByteArray();
            ContentValues contentValues25 = new ContentValues();
            //contentValues25.put("_id", "26");
            contentValues25.put("itemname", "roti");
            contentValues25.put("price", "15");
            contentValues25.put("stockquan", "100");
            contentValues25.put("category", "Main course");
            contentValues25.put("itemtax", "None");
            contentValues25.put("image", img25);
            contentValues25.put("favourites", "no");
            contentValues25.put("weekdaysvalue", "0");
            contentValues25.put("weekendsvalue", "0");
            contentValues25.put("barcode_value", "");
            contentValues25.put("disc_type", "%");
            contentValues25.put("disc_value", "0");
            contentValues25.put("quantity_sold", "0");
            contentValues25.put("minimum_qty", "0");
            contentValues25.put("minimum_qty_copy", "0");
            contentValues25.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues25);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues25);

            byte[] img26;
            Bitmap b26 = BitmapFactory.decodeResource(getResources(), R.drawable.rice);
            ByteArrayOutputStream bos26 = new ByteArrayOutputStream();
            b26.compress(Bitmap.CompressFormat.PNG, 100, bos26);
            img26 = bos26.toByteArray();
            ContentValues contentValues26 = new ContentValues();
            //contentValues26.put("_id", "27");
            contentValues26.put("itemname", "rice");
            contentValues26.put("price", "30");
            contentValues26.put("stockquan", "50");
            contentValues26.put("category", "Main course");
            contentValues26.put("itemtax", "None");
            contentValues26.put("image", img26);
            contentValues26.put("favourites", "no");
            contentValues26.put("weekdaysvalue", "0");
            contentValues26.put("weekendsvalue", "0");
            contentValues26.put("barcode_value", "");
            contentValues26.put("disc_type", "%");
            contentValues26.put("disc_value", "0");
            contentValues26.put("quantity_sold", "0");
            contentValues26.put("minimum_qty", "0");
            contentValues26.put("minimum_qty_copy", "0");
            contentValues26.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues26);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues26);

            byte[] img27;
            Bitmap b27 = BitmapFactory.decodeResource(getResources(), R.drawable.paneer_butter_masala);
            ByteArrayOutputStream bos27 = new ByteArrayOutputStream();
            b27.compress(Bitmap.CompressFormat.PNG, 100, bos27);
            img27 = bos27.toByteArray();
            ContentValues contentValues27 = new ContentValues();
            //contentValues27.put("_id", "28");
            contentValues27.put("itemname", "paneer butter masala");
            contentValues27.put("price", "60");
            contentValues27.put("stockquan", "50");
            contentValues27.put("category", "Main course");
            contentValues27.put("itemtax", "None");
            contentValues27.put("image", img27);
            contentValues27.put("favourites", "no");
            contentValues27.put("weekdaysvalue", "0");
            contentValues27.put("weekendsvalue", "0");
            contentValues27.put("barcode_value", "");
            contentValues27.put("disc_type", "%");
            contentValues27.put("disc_value", "0");
            contentValues27.put("quantity_sold", "0");
            contentValues27.put("minimum_qty", "0");
            contentValues27.put("minimum_qty_copy", "0");
            contentValues27.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues27);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues27);

            byte[] img28;
            Bitmap b28 = BitmapFactory.decodeResource(getResources(), R.drawable.chicken_sukka);
            ByteArrayOutputStream bos28 = new ByteArrayOutputStream();
            b28.compress(Bitmap.CompressFormat.PNG, 100, bos28);
            img28 = bos28.toByteArray();
            ContentValues contentValues28 = new ContentValues();
            //contentValues28.put("_id", "29");
            contentValues28.put("itemname", "chicken sukka");
            contentValues28.put("price", "80");
            contentValues28.put("stockquan", "50");
            contentValues28.put("category", "Main course");
            contentValues28.put("itemtax", "None");
            contentValues28.put("image", img28);
            contentValues28.put("favourites", "no");
            contentValues28.put("weekdaysvalue", "0");
            contentValues28.put("weekendsvalue", "0");
            contentValues28.put("barcode_value", "");
            contentValues28.put("disc_type", "%");
            contentValues28.put("disc_value", "0");
            contentValues28.put("quantity_sold", "0");
            contentValues28.put("minimum_qty", "0");
            contentValues28.put("minimum_qty_copy", "0");
            contentValues28.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues28);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues28);

            byte[] img29;
            Bitmap b29 = BitmapFactory.decodeResource(getResources(), R.drawable.dhal_fry);
            ByteArrayOutputStream bos29 = new ByteArrayOutputStream();
            b29.compress(Bitmap.CompressFormat.PNG, 100, bos29);
            img29 = bos29.toByteArray();
            ContentValues contentValues29 = new ContentValues();
            //contentValues29.put("_id", "30");
            contentValues29.put("itemname", "dhal fry");
            contentValues29.put("price", "30");
            contentValues29.put("stockquan", "50");
            contentValues29.put("category", "Main course");
            contentValues29.put("itemtax", "None");
            contentValues29.put("image", img29);
            contentValues29.put("favourites", "no");
            contentValues29.put("weekdaysvalue", "0");
            contentValues29.put("weekendsvalue", "0");
            contentValues29.put("barcode_value", "");
            contentValues29.put("disc_type", "%");
            contentValues29.put("disc_value", "0");
            contentValues29.put("quantity_sold", "0");
            contentValues29.put("minimum_qty", "0");
            contentValues29.put("minimum_qty_copy", "0");
            contentValues29.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues29);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues29);

            byte[] img30;
            Bitmap b30 = BitmapFactory.decodeResource(getResources(), R.drawable.coffee);
            ByteArrayOutputStream bos30 = new ByteArrayOutputStream();
            b30.compress(Bitmap.CompressFormat.PNG, 100, bos30);
            img30 = bos30.toByteArray();
            ContentValues contentValues30 = new ContentValues();
            //contentValues30.put("_id", "31");
            contentValues30.put("itemname", "coffee");
            contentValues30.put("price", "20");
            contentValues30.put("stockquan", "100");
            contentValues30.put("category", "Drinks");
            contentValues30.put("itemtax", "None");
            contentValues30.put("image", img30);
            contentValues30.put("favourites", "yes");
            contentValues30.put("weekdaysvalue", "0");
            contentValues30.put("weekendsvalue", "0");
            contentValues30.put("barcode_value", "");
            contentValues30.put("disc_type", "%");
            contentValues30.put("disc_value", "0");
            contentValues30.put("quantity_sold", "0");
            contentValues30.put("minimum_qty", "0");
            contentValues30.put("minimum_qty_copy", "0");
            contentValues30.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues30);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues30);

            byte[] img31;
            Bitmap b31 = BitmapFactory.decodeResource(getResources(), R.drawable.tea);
            ByteArrayOutputStream bos31 = new ByteArrayOutputStream();
            b31.compress(Bitmap.CompressFormat.PNG, 100, bos31);
            img31 = bos31.toByteArray();
            ContentValues contentValues31 = new ContentValues();
            //contentValues31.put("_id", "32");
            contentValues31.put("itemname", "tea");
            contentValues31.put("price", "20");
            contentValues31.put("stockquan", "50");
            contentValues31.put("category", "Drinks");
            contentValues31.put("itemtax", "None");
            contentValues31.put("image", img31);
            contentValues31.put("favourites", "yes");
            contentValues31.put("weekdaysvalue", "0");
            contentValues31.put("weekendsvalue", "0");
            contentValues31.put("barcode_value", "");
            contentValues31.put("disc_type", "%");
            contentValues31.put("disc_value", "0");
            contentValues31.put("quantity_sold", "0");
            contentValues31.put("minimum_qty", "0");
            contentValues31.put("minimum_qty_copy", "0");
            contentValues31.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues31);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues31);

            byte[] img32;
            Bitmap b32 = BitmapFactory.decodeResource(getResources(), R.drawable.masal_chai);
            ByteArrayOutputStream bos32 = new ByteArrayOutputStream();
            b32.compress(Bitmap.CompressFormat.PNG, 100, bos32);
            img32 = bos32.toByteArray();
            ContentValues contentValues32 = new ContentValues();
            //contentValues32.put("_id", "33");
            contentValues32.put("itemname", "masala chai");
            contentValues32.put("price", "25");
            contentValues32.put("stockquan", "50");
            contentValues32.put("category", "Drinks");
            contentValues32.put("itemtax", "None");
            contentValues32.put("image", img32);
            contentValues32.put("favourites", "no");
            contentValues32.put("weekdaysvalue", "0");
            contentValues32.put("weekendsvalue", "0");
            contentValues32.put("barcode_value", "");
            contentValues32.put("disc_type", "%");
            contentValues32.put("disc_value", "0");
            contentValues32.put("quantity_sold", "0");
            contentValues32.put("minimum_qty", "0");
            contentValues32.put("minimum_qty_copy", "0");
            contentValues32.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues32);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues32);

            byte[] img33;
            Bitmap b33 = BitmapFactory.decodeResource(getResources(), R.drawable.soft_drinks);
            ByteArrayOutputStream bos33 = new ByteArrayOutputStream();
            b33.compress(Bitmap.CompressFormat.PNG, 100, bos33);
            img33 = bos33.toByteArray();
            ContentValues contentValues33 = new ContentValues();
            //contentValues33.put("_id", "34");
            contentValues33.put("itemname", "soft drinks");
            contentValues33.put("price", "10");
            contentValues33.put("stockquan", "50");
            contentValues33.put("category", "Drinks");
            contentValues33.put("itemtax", "None");
            contentValues33.put("image", img33);
            contentValues33.put("favourites", "no");
            contentValues33.put("weekdaysvalue", "0");
            contentValues33.put("weekendsvalue", "0");
            contentValues33.put("barcode_value", "");
            contentValues33.put("disc_type", "%");
            contentValues33.put("disc_value", "0");
            contentValues33.put("quantity_sold", "0");
            contentValues33.put("minimum_qty", "0");
            contentValues33.put("minimum_qty_copy", "0");
            contentValues33.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues33);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues33);

            byte[] img34;
            Bitmap b34 = BitmapFactory.decodeResource(getResources(), R.drawable.lime_juice);
            ByteArrayOutputStream bos34 = new ByteArrayOutputStream();
            b34.compress(Bitmap.CompressFormat.PNG, 100, bos34);
            img34 = bos34.toByteArray();
            ContentValues contentValues34 = new ContentValues();
            //contentValues34.put("_id", "35");
            contentValues34.put("itemname", "lime juice");
            contentValues34.put("price", "15");
            contentValues34.put("stockquan", "30");
            contentValues34.put("category", "Drinks");
            contentValues34.put("itemtax", "None");
            contentValues34.put("image", img34);
            contentValues34.put("favourites", "no");
            contentValues34.put("weekdaysvalue", "0");
            contentValues34.put("weekendsvalue", "0");
            contentValues34.put("barcode_value", "");
            contentValues34.put("disc_type", "%");
            contentValues34.put("disc_value", "0");
            contentValues34.put("quantity_sold", "0");
            contentValues34.put("minimum_qty", "0");
            contentValues34.put("minimum_qty_copy", "0");
            contentValues34.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues34);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues34);

            byte[] img35;
            Bitmap b35 = BitmapFactory.decodeResource(getResources(), R.drawable.lassi);
            ByteArrayOutputStream bos35 = new ByteArrayOutputStream();
            b35.compress(Bitmap.CompressFormat.PNG, 100, bos35);
            img35 = bos35.toByteArray();
            ContentValues contentValues35 = new ContentValues();
            //contentValues35.put("_id", "36");
            contentValues35.put("itemname", "lassi");
            contentValues35.put("price", "25");
            contentValues35.put("stockquan", "25");
            contentValues35.put("category", "Drinks");
            contentValues35.put("itemtax", "None");
            contentValues35.put("image", img35);
            contentValues35.put("favourites", "no");
            contentValues35.put("weekdaysvalue", "0");
            contentValues35.put("weekendsvalue", "0");
            contentValues35.put("barcode_value", "");
            contentValues35.put("disc_type", "%");
            contentValues35.put("disc_value", "0");
            contentValues35.put("quantity_sold", "0");
            contentValues35.put("minimum_qty", "0");
            contentValues35.put("minimum_qty_copy", "0");
            contentValues35.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues35);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues35);

            byte[] img36;
            Bitmap b36 = BitmapFactory.decodeResource(getResources(), R.drawable.grape_juice);
            ByteArrayOutputStream bos36 = new ByteArrayOutputStream();
            b36.compress(Bitmap.CompressFormat.PNG, 100, bos36);
            img36 = bos36.toByteArray();
            ContentValues contentValues36 = new ContentValues();
            //contentValues36.put("_id", "37");
            contentValues36.put("itemname", "grape juice");
            contentValues36.put("price", "25");
            contentValues36.put("stockquan", "25");
            contentValues36.put("category", "Drinks");
            contentValues36.put("itemtax", "None");
            contentValues36.put("image", img36);
            contentValues36.put("favourites", "no");
            contentValues36.put("weekdaysvalue", "0");
            contentValues36.put("weekendsvalue", "0");
            contentValues36.put("barcode_value", "");
            contentValues36.put("disc_type", "%");
            contentValues36.put("disc_value", "0");
            contentValues36.put("quantity_sold", "0");
            contentValues36.put("minimum_qty", "0");
            contentValues36.put("minimum_qty_copy", "0");
            contentValues36.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues36);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues36);

            byte[] img37;
            Bitmap b37 = BitmapFactory.decodeResource(getResources(), R.drawable.vanilla_icecream);
            ByteArrayOutputStream bos37 = new ByteArrayOutputStream();
            b37.compress(Bitmap.CompressFormat.PNG, 100, bos37);
            img37 = bos37.toByteArray();
            ContentValues contentValues37 = new ContentValues();
            //contentValues37.put("_id", "38");
            contentValues37.put("itemname", "vanilla icecream");
            contentValues37.put("price", "35");
            contentValues37.put("stockquan", "50");
            contentValues37.put("category", "Desserts");
            contentValues37.put("itemtax", "None");
            contentValues37.put("image", img37);
            contentValues37.put("favourites", "yes");
            contentValues37.put("weekdaysvalue", "0");
            contentValues37.put("weekendsvalue", "0");
            contentValues37.put("barcode_value", "");
            contentValues37.put("disc_type", "%");
            contentValues37.put("disc_value", "0");
            contentValues37.put("quantity_sold", "0");
            contentValues37.put("minimum_qty", "0");
            contentValues37.put("minimum_qty_copy", "0");
            contentValues37.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues37);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues37);

            byte[] img38;
            Bitmap b38 = BitmapFactory.decodeResource(getResources(), R.drawable.butterscotch_icecream);
            ByteArrayOutputStream bos38 = new ByteArrayOutputStream();
            b38.compress(Bitmap.CompressFormat.PNG, 100, bos38);
            img38 = bos38.toByteArray();
            ContentValues contentValues38 = new ContentValues();
            //contentValues38.put("_id", "39");
            contentValues38.put("itemname", "butterscotch icecream");
            contentValues38.put("price", "40");
            contentValues38.put("stockquan", "50");
            contentValues38.put("category", "Desserts");
            contentValues38.put("itemtax", "None");
            contentValues38.put("image", img38);
            contentValues38.put("favourites", "no");
            contentValues38.put("weekdaysvalue", "0");
            contentValues38.put("weekendsvalue", "0");
            contentValues38.put("barcode_value", "");
            contentValues38.put("disc_type", "%");
            contentValues38.put("disc_value", "0");
            contentValues38.put("quantity_sold", "0");
            contentValues38.put("minimum_qty", "0");
            contentValues38.put("minimum_qty_copy", "0");
            contentValues38.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues38);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues38);

            byte[] img39;
            Bitmap b39 = BitmapFactory.decodeResource(getResources(), R.drawable.butterscotch_cake);
            ByteArrayOutputStream bos39 = new ByteArrayOutputStream();
            b39.compress(Bitmap.CompressFormat.PNG, 100, bos39);
            img39 = bos39.toByteArray();
            ContentValues contentValues39 = new ContentValues();
            //contentValues39.put("_id", "40");
            contentValues39.put("itemname", "butterscotch cake");
            contentValues39.put("price", "35");
            contentValues39.put("stockquan", "50");
            contentValues39.put("category", "Desserts");
            contentValues39.put("itemtax", "None");
            contentValues39.put("image", img39);
            contentValues39.put("favourites", "no");
            contentValues39.put("weekdaysvalue", "0");
            contentValues39.put("weekendsvalue", "0");
            contentValues39.put("barcode_value", "");
            contentValues39.put("disc_type", "%");
            contentValues39.put("disc_value", "0");
            contentValues39.put("quantity_sold", "0");
            contentValues39.put("minimum_qty", "0");
            contentValues39.put("minimum_qty_copy", "0");
            contentValues39.put("add_qty", "0");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//            resultUri = getContentResolver().insert(contentUri, contentValues39);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Items", null, contentValues39);
            //}

//            SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(MainActivity.this);
//            final String company= sharedpreferences.getString("companyname", null);
//            final String store= sharedpreferences.getString("storename", null);
//            final String device= sharedpreferences.getString("devicename", null);
//
//            requestQueue = Volley.newRequestQueue(MainActivity.this);
//            Log.d("downloadserv","inside createappdata");
//            StringRequest sr = new StringRequest(
//                    Request.Method.POST,
//                    WebserviceUrl+"dummy_db.php",
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            Log.d("downloadserv","inside response1 "+response);
//                            if(response.equalsIgnoreCase("success")){
//                                Log.d("response",response);
//                                Log.d("downloadserv","success1");
//                            }else{
//                                Log.d("downloadserv","failed1");
//                            }
//
//                            final SQLiteDatabase syncdbapp=openOrCreateDatabase("syncdbapp", Context.MODE_PRIVATE,null);
//
//                            Cursor cursor = syncdbapp.rawQuery("SELECT * FROM appdata WHERE tablename = 'Items'", null);
//                            if (cursor.moveToFirst()){
//                                String id = cursor.getString(0);
//                                ContentValues cv = new ContentValues();
////                                  cv.put("_id", id);
//                                cv.put("tablename", "Items");
//                                cv.put("lastsyncedid", "40");
//                                String where1 = "_id = " + id;
//                                syncdbapp.update("appdata", cv, where1, new String[]{});
//                            }
//                            cursor.close();
//
//                            Cursor cursor1 = syncdbapp.rawQuery("SELECT * FROM appdata WHERE tablename = 'Hotel'", null);
//                            if (cursor1.moveToFirst()){
//                                String id = cursor1.getString(0);
//                                ContentValues cv = new ContentValues();
////                                  cv.put("_id", id);
//                                cv.put("tablename", "Hotel");
//                                cv.put("lastsyncedid", "7");
//                                String where1 = "_id = " + id;
//                                syncdbapp.update("appdata", cv, where1, new String[]{});
//                            }
//                            cursor1.close();
//
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.d("Signup confirm2", "Error: " + error.getMessage());
//                        }
//                    })  {
//                @Override
//                protected Map<String, String> getParams()
//                {
//                    Map<String,String> params = new HashMap<String, String>();
//                               /*     params.put("email", email + "");
//                                    params.put("password", password + "");*/
//                    params.put("company", company + "");
//                    params.put("store", store + "");
//                    params.put("device", device + "");
//
//                    Log.d("downloadserv",company);
//                    Log.d("downloadserv",store);
//                    Log.d("downloadserv",device);
//                    return params;
//                }
//            };
//            sr.setRetryPolicy(new DefaultRetryPolicy(0,-1,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//            requestQueue.add(sr);

        }
        add_item.close();

        Cursor cursor2 = db.rawQuery("SELECT * FROM Items_Virtual", null);
        if (cursor2.moveToFirst()) {


        } else {
            db.execSQL("INSERT INTO Items_Virtual (itemname , price , stockquan , category , itemtax , image" +
                    " , weekdaysvalue , weekendsvalue , manualstockvalue , automaticstockresetvalue , clickcount , favourites ,disc_type" +
                    " , disc_value , image_text , barcode_value , checked , print_value, quantity_sold , minimum_qty , minimum_qty_copy ,add_qty" +
                    ", status_low , status_qty_updated , individual_price , unit_type , tax_value , itemtax2 , tax_value2 , itemtax3 ,tax_value3" +
                    ", itemtax4 ,tax_value4 ,itemtax5 ,tax_value5 , status_perm , variant1 , variant_price1 , variant2 , variant_price2 , variant3" +
                    ", variant_price3 , variant4 , variant_price4 , variant5 , variant_price5 ) SELECT itemname, price , stockquan , category , itemtax,image , weekdaysvalue " +
                    ", weekendsvalue , manualstockvalue , automaticstockresetvalue , clickcount , favourites , disc_type , disc_value , image_text , barcode_value , checked" +
                    " , print_value , quantity_sold , minimum_qty , minimum_qty_copy ,add_qty , status_low , status_qty_updated , individual_price , unit_type , tax_value " +
                    ", itemtax2 , tax_value2 , itemtax3 ,tax_value3 , itemtax4 ,tax_value4 ,itemtax5 ,tax_value5 , status_perm , variant1 , variant_price1 , variant2 , variant_price2 , variant3" +
                    ", variant_price3 , variant4 , variant_price4 , variant5 , variant_price5  FROM Items");

        }
        cursor2.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor eight = db.rawQuery("SELECT * FROM Hotel ", null);
        if (eight.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", "All");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Hotel");
//            resultUri = getContentResolver().insert(contentUri, contentValues);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Hotel", null, contentValues);

            ContentValues contentValues1 = new ContentValues();
            contentValues1.put("name", "Favourites");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Hotel");
//            resultUri = getContentResolver().insert(contentUri, contentValues1);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Hotel", null, contentValues1);

            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("name", "Tiffin");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Hotel");
//            resultUri = getContentResolver().insert(contentUri, contentValues2);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Hotel", null, contentValues2);

            ContentValues contentValues3 = new ContentValues();
            contentValues3.put("name", "Snacks");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Hotel");
//            resultUri = getContentResolver().insert(contentUri, contentValues3);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Hotel", null, contentValues3);

            ContentValues contentValues4 = new ContentValues();
            contentValues4.put("name", "Main course");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Hotel");
//            resultUri = getContentResolver().insert(contentUri, contentValues4);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Hotel", null, contentValues4);

            ContentValues contentValues5 = new ContentValues();
            contentValues5.put("name", "Drinks");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Hotel");
//            resultUri = getContentResolver().insert(contentUri, contentValues5);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Hotel", null, contentValues5);

            ContentValues contentValues6 = new ContentValues();
            contentValues6.put("name", "Desserts");
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Hotel");
//            resultUri = getContentResolver().insert(contentUri, contentValues6);
//            getContentResolver().notifyChange(resultUri, null);
                    db.insert("Hotel", null, contentValues6);
        }
        eight.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor add_modifier = db.rawQuery("SELECT * FROM Modifiers ", null);
        if (add_modifier.moveToFirst()) {

        } else {
            byte[] img;
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.potato_masala);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 100, bos);
            img = bos.toByteArray();
            ContentValues contentValues = new ContentValues();
            contentValues.put("_id", "1");
            contentValues.put("modifiername", "potato masala");
            contentValues.put("modprice", "10");
            contentValues.put("modstockquan", "50");
            contentValues.put("modcategory", "Tiffin");
            contentValues.put("moditemtax", "None");
            contentValues.put("modimage", img);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Modifiers", null, contentValues);

            byte[] img1;
            Bitmap b1 = BitmapFactory.decodeResource(getResources(), R.drawable.podi);
            ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
            b1.compress(Bitmap.CompressFormat.PNG, 100, bos1);
            img1 = bos1.toByteArray();
            ContentValues contentValues1 = new ContentValues();
            contentValues1.put("_id", "2");
            contentValues1.put("modifiername", "podi");
            contentValues1.put("modprice", "0");
            contentValues1.put("modstockquan", "50");
            contentValues1.put("modcategory", "Tiffin");
            contentValues1.put("moditemtax", "None");
            contentValues1.put("modimage", img1);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers");
            resultUri = getContentResolver().insert(contentUri, contentValues1);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Modifiers", null, contentValues1);

            byte[] img2;
            Bitmap b2 = BitmapFactory.decodeResource(getResources(), R.drawable.cheese);
            ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
            b2.compress(Bitmap.CompressFormat.PNG, 100, bos2);
            img2 = bos2.toByteArray();
            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("_id", "3");
            contentValues2.put("modifiername", "cheese");
            contentValues2.put("modprice", "15");
            contentValues2.put("modstockquan", "35");
            contentValues2.put("modcategory", "Tiffin");
            contentValues2.put("moditemtax", "None");
            contentValues2.put("modimage", img2);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers");
            resultUri = getContentResolver().insert(contentUri, contentValues2);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Modifiers", null, contentValues2);

            byte[] img3;
            Bitmap b3 = BitmapFactory.decodeResource(getResources(), R.drawable.butter);
            ByteArrayOutputStream bos3 = new ByteArrayOutputStream();
            b3.compress(Bitmap.CompressFormat.PNG, 100, bos3);
            img3 = bos3.toByteArray();
            ContentValues contentValues3 = new ContentValues();
            contentValues3.put("_id", "4");
            contentValues3.put("modifiername", "butter");
            contentValues3.put("modprice", "10");
            contentValues3.put("modstockquan", "40");
            contentValues3.put("modcategory", "Tiffin");
            contentValues3.put("moditemtax", "None");
            contentValues3.put("modimage", img3);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers");
            resultUri = getContentResolver().insert(contentUri, contentValues3);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Modifiers", null, contentValues3);

            byte[] img4;
            Bitmap b4 = BitmapFactory.decodeResource(getResources(), R.drawable.omlette);
            ByteArrayOutputStream bos4 = new ByteArrayOutputStream();
            b4.compress(Bitmap.CompressFormat.PNG, 100, bos4);
            img4 = bos4.toByteArray();
            ContentValues contentValues4 = new ContentValues();
            contentValues4.put("_id", "5");
            contentValues4.put("modifiername", "omlette");
            contentValues4.put("modprice", "10");
            contentValues4.put("modstockquan", "50");
            contentValues4.put("modcategory", "Tiffin");
            contentValues4.put("moditemtax", "None");
            contentValues4.put("modimage", img4);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers");
            resultUri = getContentResolver().insert(contentUri, contentValues4);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Modifiers", null, contentValues4);

            byte[] img5;
            Bitmap b5 = BitmapFactory.decodeResource(getResources(), R.drawable.cheese);
            ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
            b5.compress(Bitmap.CompressFormat.PNG, 100, bos5);
            img5 = bos5.toByteArray();
            ContentValues contentValues5 = new ContentValues();
            contentValues5.put("_id", "6");
            contentValues5.put("modifiername", "cheese dip");
            contentValues5.put("modprice", "20");
            contentValues5.put("modstockquan", "50");
            contentValues5.put("modcategory", "Snacks");
            contentValues5.put("moditemtax", "None");
            contentValues5.put("modimage", img5);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers");
            resultUri = getContentResolver().insert(contentUri, contentValues5);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Modifiers", null, contentValues5);

            byte[] img6;
            Bitmap b6 = BitmapFactory.decodeResource(getResources(), R.drawable.potato_fry);
            ByteArrayOutputStream bos6 = new ByteArrayOutputStream();
            b6.compress(Bitmap.CompressFormat.PNG, 100, bos6);
            img6 = bos6.toByteArray();
            ContentValues contentValues6 = new ContentValues();
            contentValues6.put("_id", "7");
            contentValues6.put("modifiername", "potato fry");
            contentValues6.put("modprice", "25");
            contentValues6.put("modstockquan", "50");
            contentValues6.put("modcategory", "Main course");
            contentValues6.put("moditemtax", "None");
            contentValues6.put("modimage", img6);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers");
            resultUri = getContentResolver().insert(contentUri, contentValues6);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Modifiers", null, contentValues6);

            byte[] img7;
            Bitmap b7 = BitmapFactory.decodeResource(getResources(), R.drawable.chicken_kabab);
            ByteArrayOutputStream bos7 = new ByteArrayOutputStream();
            b7.compress(Bitmap.CompressFormat.PNG, 100, bos7);
            img7 = bos7.toByteArray();
            ContentValues contentValues7 = new ContentValues();
            contentValues7.put("_id", "8");
            contentValues7.put("modifiername", "chicken fry");
            contentValues7.put("modprice", "40");
            contentValues7.put("modstockquan", "45");
            contentValues7.put("modcategory", "Main course");
            contentValues7.put("moditemtax", "None");
            contentValues7.put("modimage", img7);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers");
            resultUri = getContentResolver().insert(contentUri, contentValues7);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Modifiers", null, contentValues7);

            byte[] img8;
            Bitmap b8 = BitmapFactory.decodeResource(getResources(), R.drawable.soft_drinks);
            ByteArrayOutputStream bos8 = new ByteArrayOutputStream();
            b8.compress(Bitmap.CompressFormat.PNG, 100, bos8);
            img8 = bos8.toByteArray();
            ContentValues contentValues8 = new ContentValues();
            contentValues8.put("_id", "9");
            contentValues8.put("modifiername", "soda");
            contentValues8.put("modprice", "6");
            contentValues8.put("modstockquan", "50");
            contentValues8.put("modcategory", "Main course");
            contentValues8.put("moditemtax", "None");
            contentValues8.put("modimage", img8);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers");
            resultUri = getContentResolver().insert(contentUri, contentValues8);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Modifiers", null, contentValues8);

            byte[] img9;
            Bitmap b9 = BitmapFactory.decodeResource(getResources(), R.drawable.butterscotch_cake);
            ByteArrayOutputStream bos9 = new ByteArrayOutputStream();
            b9.compress(Bitmap.CompressFormat.PNG, 100, bos9);
            img9 = bos9.toByteArray();
            ContentValues contentValues9 = new ContentValues();
            contentValues9.put("_id", "10");
            contentValues9.put("modifiername", "dessert");
            contentValues9.put("modprice", "15");
            contentValues9.put("modstockquan", "35");
            contentValues9.put("modcategory", "Main course");
            contentValues9.put("moditemtax", "None");
            contentValues9.put("modimage", img9);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers");
            resultUri = getContentResolver().insert(contentUri, contentValues9);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Modifiers", null, contentValues9);

            byte[] img10;
            Bitmap b10 = BitmapFactory.decodeResource(getResources(), R.drawable.soft_drinks);
            ByteArrayOutputStream bos10 = new ByteArrayOutputStream();
            b10.compress(Bitmap.CompressFormat.PNG, 100, bos10);
            img10 = bos10.toByteArray();
            ContentValues contentValues10 = new ContentValues();
            contentValues10.put("_id", "11");
            contentValues10.put("modifiername", "medium");
            contentValues10.put("modprice", "5");
            contentValues10.put("modstockquan", "50");
            contentValues10.put("modcategory", "Drinks");
            contentValues10.put("moditemtax", "None");
            contentValues10.put("modimage", img10);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers");
            resultUri = getContentResolver().insert(contentUri, contentValues10);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Modifiers", null, contentValues10);

            byte[] img11;
            Bitmap b11 = BitmapFactory.decodeResource(getResources(), R.drawable.soft_drinks);
            ByteArrayOutputStream bos11 = new ByteArrayOutputStream();
            b11.compress(Bitmap.CompressFormat.PNG, 100, bos11);
            img11 = bos11.toByteArray();
            ContentValues contentValues11 = new ContentValues();
            contentValues11.put("_id", "12");
            contentValues11.put("modifiername", "large");
            contentValues11.put("modprice", "10");
            contentValues11.put("modstockquan", "50");
            contentValues11.put("modcategory", "Drinks");
            contentValues11.put("moditemtax", "None");
            contentValues11.put("modimage", img11);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers");
            resultUri = getContentResolver().insert(contentUri, contentValues11);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Modifiers", null, contentValues11);

            byte[] img12;
            Bitmap b12 = BitmapFactory.decodeResource(getResources(), R.drawable.cream);
            ByteArrayOutputStream bos12 = new ByteArrayOutputStream();
            b12.compress(Bitmap.CompressFormat.PNG, 100, bos12);
            img12 = bos12.toByteArray();
            ContentValues contentValues12 = new ContentValues();
            contentValues12.put("_id", "13");
            contentValues12.put("modifiername", "cream");
            contentValues12.put("modprice", "20");
            contentValues12.put("modstockquan", "50");
            contentValues12.put("modcategory", "Desserts");
            contentValues12.put("moditemtax", "None");
            contentValues12.put("modimage", img12);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers");
            resultUri = getContentResolver().insert(contentUri, contentValues12);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Modifiers", null, contentValues12);

            byte[] img13;
            Bitmap b13 = BitmapFactory.decodeResource(getResources(), R.drawable.chocolate_syrup);
            ByteArrayOutputStream bos13 = new ByteArrayOutputStream();
            b13.compress(Bitmap.CompressFormat.PNG, 100, bos13);
            img13 = bos13.toByteArray();
            ContentValues contentValues13 = new ContentValues();
            contentValues13.put("_id", "14");
            contentValues13.put("modifiername", "chocolate");
            contentValues13.put("modprice", "20");
            contentValues13.put("modstockquan", "50");
            contentValues13.put("modcategory", "Desserts");
            contentValues13.put("moditemtax", "None");
            contentValues13.put("modimage", img13);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers");
            resultUri = getContentResolver().insert(contentUri, contentValues13);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Modifiers", null, contentValues13);
        }
        add_modifier.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor nine = db.rawQuery("SELECT * FROM asd1 ", null);
        if (nine.moveToFirst()) {

        } else {
            byte[] img;
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.table_rr);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 100, bos);
            img = bos.toByteArray();

            ContentValues contentValues = new ContentValues();
            contentValues.put("pName", "");
            contentValues.put("pDate", "1");
            contentValues.put("image", img);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "asd1");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("asd1", null, contentValues);

            ContentValues contentValues1 = new ContentValues();
            contentValues1.put("pName", "");
            contentValues1.put("pDate", "2");
            contentValues1.put("image", img);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "asd1");
            resultUri = getContentResolver().insert(contentUri, contentValues1);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("asd1", null, contentValues1);

            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("pName", "");
            contentValues2.put("pDate", "3");
            contentValues2.put("image", img);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "asd1");
            resultUri = getContentResolver().insert(contentUri, contentValues2);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("asd1", null, contentValues2);
        }
        nine.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor billing_mode = db.rawQuery("SELECT * FROM BIllingmode ", null);
        if (billing_mode.moveToFirst()) {

        } else {

            ContentValues contentValues = new ContentValues();
            contentValues.put("_id", "1");
            contentValues.put("billingtype", "Fine dine");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "BIllingmode");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("BIllingmode", null, contentValues);

        }
        billing_mode.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor billing_type = db.rawQuery("SELECT * FROM BIllingtype ", null);
        if (billing_type.moveToFirst()) {

        }else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_id", "1");
            contentValues.put("billingtype_type", "Dine-in");
//            db.insert("BIllingtype", null, contentValues);
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "BIllingtype");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
        }
        billing_type.close();

        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        Cursor billcount = db1.rawQuery("SELECT * FROM BillCount ", null);
        if (billcount.moveToFirst()) {

        }else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("value", "0");
//            db1.insert("BillCount", null, contentValues);
            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "BillCount");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
        }
        billcount.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor estimate_bill = db.rawQuery("SELECT * FROM Estimate_print ", null);
        if (estimate_bill.moveToFirst()) {

        }else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_id", "1");
            contentValues.put("status", "Yes");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Estimate_print");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//            db.insert("BIllingtype", null, contentValues);
        }
        estimate_bill.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor cursor_tab_kot_manag = db.rawQuery("SELECT * FROM Table_kot ", null);
        if (cursor_tab_kot_manag.moveToFirst()) {

        }else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_id", "1");
            contentValues.put("status", "Lite");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Table_kot");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//            db.insert("BIllingtype", null, contentValues);
        }
        cursor_tab_kot_manag.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor printer_type = db.rawQuery("SELECT * FROM Printer_type", null);
        if (printer_type.moveToFirst()) {

        } else {

            ContentValues contentValues = new ContentValues();
            contentValues.put("_id", "1");
            contentValues.put("printer_type", "Generic");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Printer_type");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Printer_type", null, contentValues);

        }
        printer_type.close();


////////////////////Inventory control////////////////
        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor stock_control = db.rawQuery("SELECT * FROM Stockcontrol ", null);
        if (stock_control.moveToFirst()) {

        } else {

            ContentValues contentValues = new ContentValues();
            contentValues.put("_id", "1");
            contentValues.put("stockcontroltype", "Off");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Stockcontrol");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Stockcontrol", null, contentValues);

        }
        stock_control.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor tthreqe = db.rawQuery("SELECT * FROM KOT_print ", null);
        if (tthreqe.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("kot_print_status", "Yes");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "KOT_print");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("KOT_print", null, contentValues);
        }
        tthreqe.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor ttthreqe = db.rawQuery("SELECT * FROM Auto_Connect ", null);
        if (ttthreqe.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("auto_connect_status", "No");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Auto_Connect");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Auto_Connect", null, contentValues);
        }
        ttthreqe.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor tathreqeea = db.rawQuery("SELECT * FROM Sync_time ", null);
        if (tathreqeea.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("last_time", "05 Apr 18, 05:22 PM");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Sync_time");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Sync_time", null, contentValues);
        }
        tathreqeea.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor tthreqeeea = db.rawQuery("SELECT * FROM Weighing_Scale_status ", null);
        if (tthreqeeea.moveToFirst()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("Weighing_Scale_onoff", "Not Connected");
            String where = "_id = '1'";

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Weighing_Scale_status");
            getContentResolver().update(contentUri, contentValues, where,new String[]{});
            resultUri = new Uri.Builder()
                    .scheme("content")
                    .authority(StubProviderApp.AUTHORITY)
                    .path("Weighing_Scale_status")
                    .appendQueryParameter("operation", "update")
                    .appendQueryParameter("_id","1")
                    .build();
            getContentResolver().notifyChange(resultUri, null);
//                    db.update("Weighing_Scale_status", contentValues, where, new String[]{});
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("Weighing_Scale_onoff", "Not Connected");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Weighing_Scale_status");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Weighing_Scale_status", null, contentValues);
        }
        tthreqeeea.close();

////////////////////smart inventory////////////////
        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor stock_reset = db.rawQuery("SELECT * FROM Stockreset ", null);
        if (stock_reset.moveToFirst()) {

        } else {

            ContentValues contentValues = new ContentValues();
            contentValues.put("_id", "1");
            contentValues.put("stockresettype", "Off");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Stockreset");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Stockreset", null, contentValues);

        }
        stock_reset.close();

////////////////////backup scheduling////////////////
        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor backup_schedule = db.rawQuery("SELECT * FROM Alaramon_off ", null);
        if (backup_schedule.moveToFirst()) {

        } else {

            ContentValues contentValues = new ContentValues();
            contentValues.put("_id", "1");
            contentValues.put("status", "On");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Alaramon_off");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Alaramon_off", null, contentValues);

        }
        backup_schedule.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor mail_schedule = db.rawQuery("SELECT * FROM Schedule_mail_on_off ", null);
        if (mail_schedule.moveToFirst()) {

        } else {

            ContentValues contentValues = new ContentValues();
            contentValues.put("_id", "1");
            contentValues.put("status", "Off");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Schedule_mail_on_off");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Schedule_mail_on_off", null, contentValues);

        }
        mail_schedule.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor user101 = db.rawQuery("SELECT * FROM Schedule_mail_time ", null);
        if (user101.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("time", "11:30 PM");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Schedule_mail_time");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Schedule_mail_time", null, contentValues);
        }
        user101.close();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor ten = db.rawQuery("SELECT * FROM Menulogin_checking ", null);
        if (ten.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("status", "no");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Menulogin_checking");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Menulogin_checking", null, contentValues);
        }
        ten.close();
        //Home_check (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, login_status text);");
        Cursor eleven = db.rawQuery("SELECT * FROM Home_check", null);
        if (eleven.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("login_status", "0");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Home_check");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Home_check", null, contentValues);
        }
        eleven.close();

        Cursor twelvw = db.rawQuery("SELECT * FROM Default_credit", null);
        if (twelvw.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("status", "off");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Default_credit");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Default_credit", null, contentValues);
        }
        twelvw.close();

        Cursor thirteen = db.rawQuery("SELECT * FROM Working_hours", null);
        if (thirteen.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("opening", "Today");
            contentValues.put("opening_time", "12:01 AM");
            contentValues.put("closing", "Today");
            contentValues.put("closing_time", "11:59 PM");
            contentValues.put("opening_time_system", "00:01");
            contentValues.put("closing_time_system", "23:59");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Working_hours");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Working_hours", null, contentValues);
        }
        thirteen.close();

        Cursor fourteen_1 = db.rawQuery("SELECT * FROM Printer_text_size", null);
        if (fourteen_1.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("type", "Standard");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Printer_text_size");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Printer_text_size", null, contentValues);
        }
        fourteen_1.close();

        Cursor fourteeen_1 = db.rawQuery("SELECT * FROM Change_time_format", null);
        if (fourteeen_1.moveToFirst()) {

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("status", "not changed");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Change_time_format");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Change_time_format", null, contentValues);
        }
        fourteeen_1.close();


        Cursor cursor_f = db.rawQuery("SELECT * FROM floors", null);
        if (cursor_f.moveToFirst()) {

        }else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("floorname", "first");
            contentValues.put("position", "0");
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "floors");
            resultUri = getContentResolver().insert(contentUri, contentValues);
            getContentResolver().notifyChange(resultUri, null);
//            db.insert("Floors", null, cv);



        }
        cursor_f.close();

        Cursor cursor = db.rawQuery("SELECT * FROM asd1", null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String flo = cursor.getString(4);

                TextView tv = new TextView(MainActivity.this);
                tv.setText(flo);

                if (tv.getText().toString().equals("")) {
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("floor", "first");
                    contentValues1.put("position", "0");
                    contentValues1.put("max", "4");

                    String where = "_id = '" + id + "'";

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "asd1");
                    getContentResolver().update(contentUri, contentValues1, where, new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("asd1")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id", id)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
                }

            }while (cursor.moveToNext());

        }


        Cursor cursor_and = db.rawQuery("SELECT * FROM Items WHERE itemname LIKE '%&%'", null);
        if (cursor_and.moveToFirst()){
            do {
                String id = cursor_and.getString(0);
                String old_itemna = cursor_and.getString(1);
                String itemna = cursor_and.getString(1);
                System.out.println("itemname are "+old_itemna+" "+itemna);

                itemna = itemna.replace("&", " and ");

                System.out.println("replace itemname are "+old_itemna+" "+itemna);

                ContentValues contentValues = new ContentValues();
                contentValues.put("itemname", itemna);
                String where = "_id = '" + id + "'";

                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                getContentResolver().update(contentUri, contentValues, where,new String[]{});
                resultUri = new Uri.Builder()
                        .scheme("content")
                        .authority(StubProviderApp.AUTHORITY)
                        .path("Items")
                        .appendQueryParameter("operation", "update")
                        .appendQueryParameter("_id",id)
                        .build();
                getContentResolver().notifyChange(resultUri, null);

                String where1 = "itemname = '" + old_itemna + "'";
                db.update("Items_Virtual", contentValues, where1, new String[]{});

            }while (cursor_and.moveToNext());
        }
        cursor_and.close();

        Cursor auto_c = db.rawQuery("SELECT * FROM Auto_Connect", null);
        if (auto_c.moveToFirst()) {
            String aw = auto_c.getString(1);
            if (aw.toString().equals("No")) {

            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    ActivityCompat.requestPermissions(this, ANDROID_12_BLE_PERMISSIONS, 9);
                else
                    ActivityCompat.requestPermissions(this, BLE_PERMISSIONS, 9);

//                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_SCAN)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
//                            Manifest.permission.BLUETOOTH_SCAN)) {
//                        ActivityCompat.requestPermissions(MainActivity.this,
//                                new String[]{Manifest.permission.BLUETOOTH_SCAN},
//                                9);
//                    } else {
//                        ActivityCompat.requestPermissions(MainActivity.this,
//                                new String[]{Manifest.permission.BLUETOOTH_SCAN},
//                                9);
//                    }
//                }
//
//                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
//                            Manifest.permission.BLUETOOTH)) {
//                        ActivityCompat.requestPermissions(MainActivity.this,
//                                new String[]{Manifest.permission.BLUETOOTH},
//                                9);
//                    } else {
//                        ActivityCompat.requestPermissions(MainActivity.this,
//                                new String[]{Manifest.permission.BLUETOOTH},
//                                9);
//                    }
//                }
//
//                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
//                            Manifest.permission.BLUETOOTH_CONNECT)) {
//                        ActivityCompat.requestPermissions(MainActivity.this,
//                                new String[]{Manifest.permission.BLUETOOTH_CONNECT},
//                                9);
//                    } else {
//                        ActivityCompat.requestPermissions(MainActivity.this,
//                                new String[]{Manifest.permission.BLUETOOTH_CONNECT},
//                                9);
//                    }
//                }


            }
        }
        auto_c.close();


    }


    public void updateSalesdata(){



        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);



        int i = 1;
        Cursor cf = db1.rawQuery("SELECT * FROM Customerdetails", null);
        if (cf.moveToFirst()) {
            do {
                String id = cf.getString(0);
                Cursor c1_11 = db.rawQuery("SELECT * FROM Taxes WHERE taxtype = 'Globaltax'", null);
                if (c1_11.moveToFirst()) {
                    do {
                        if (i >= 16) {

                        }else {
                            String tn = c1_11.getString(1);
                            String tn_value = c1_11.getString(2);
                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("tax" + i, tn);
                            contentValues1.put("tax" + i + "_value", tn_value);
                            contentValues1.put("tax_selection", "All");
                            String wherecu1 = "_id = '" + id + "'";

                            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
                            getContentResolver().update(contentUri, contentValues1, wherecu1, new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProvider.AUTHORITY)
                                    .path("Customerdetails")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id", id)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);

//                                    db1.update("Customerdetails", contentValues1, wherecu1, new String[]{});
                            i++;
                        }
                    } while (c1_11.moveToNext());
                }
                c1_11.close();
            } while (cf.moveToNext());
        }
        cf.close();


    Cursor fourteen = db1.rawQuery("SELECT * FROM All_Sales WHERE datetimee_new is null OR datetimee_new = ''", null);
        if (fourteen.moveToFirst()) {
            do {
                String id = fourteen.getString(0);
                String dt = fourteen.getString(26);

                if (dt.toString().contains(":")) {
                    dt = dt.replace(":", "");
                }

                dt = dt.substring(0, 12);
//                Toast.makeText(MainActivity.this, " "+dt, Toast.LENGTH_SHORT).show();
                ContentValues contentValues = new ContentValues();
                contentValues.put("datetimee_new", dt);
                String where11 = "_id = '" + id + "' ";

                contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "All_Sales");
                getContentResolver().update(contentUri, contentValues,where11,new String[]{});
                resultUri = new Uri.Builder()
                        .scheme("content")
                        .authority(StubProvider.AUTHORITY)
                        .path("All_Sales")
                        .appendQueryParameter("operation", "update")
                        .appendQueryParameter("_id",id)
                        .build();
                getContentResolver().notifyChange(resultUri, null);

//                        db1.update("All_Sales", contentValues, where11, new String[]{});
            } while (fourteen.moveToNext());
        }
        fourteen.close();

        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        Cursor fifteen = db1.rawQuery("SELECT * FROM Billnumber WHERE datetimee_new is null OR datetimee_new = ''", null);
        if (fifteen.moveToFirst()) {
            do {
                String id = fifteen.getString(0);
                String billno = fifteen.getString(1);

                Cursor ffifteen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                if (ffifteen_1.moveToFirst()) {
                    String a = ffifteen_1.getString(33);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("datetimee_new", a);
                    String where11 = "_id = '" + id + "' ";

                    contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Billnumber");
                    getContentResolver().update(contentUri, contentValues,where11,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProvider.AUTHORITY)
                            .path("Billnumber")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id",id)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);

//                            db1.update("Billnumber", contentValues, where11, new String[]{});
                }
                ffifteen_1.close();
            } while (fifteen.moveToNext());
        }
        fifteen.close();

        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        Cursor sixteen = db1.rawQuery("SELECT * FROM Customerdetails WHERE datetimee_new is null OR datetimee_new = ''", null);
        if (sixteen.moveToFirst()) {
            do {
                String id = sixteen.getString(0);
                String billno = sixteen.getString(6);

                Cursor sixteen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                if (sixteen_1.moveToFirst()) {
                    String a = sixteen_1.getString(33);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("datetimee_new", a);
                    String where11 = "_id = '" + id + "' ";
                    contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
                    getContentResolver().update(contentUri, contentValues,where11,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProvider.AUTHORITY)
                            .path("Customerdetails")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id",id)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                            db1.update("Customerdetails", contentValues, where11, new String[]{});
                }
                sixteen_1.close();
            } while (sixteen.moveToNext());
        }
        sixteen.close();

        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        Cursor seventeen = db1.rawQuery("SELECT * FROM Discountdetails WHERE datetimee_new is null OR datetimee_new = ''", null);
        if (seventeen.moveToFirst()) {
            do {
                String id = seventeen.getString(0);
                String billno = seventeen.getString(3);

                Cursor seventeen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                if (seventeen_1.moveToFirst()) {
                    String a = seventeen_1.getString(33);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("datetimee_new", a);
                    String where11 = "_id = '" + id + "' ";

                    contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Discountdetails");
                    getContentResolver().update(contentUri, contentValues,where11,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProvider.AUTHORITY)
                            .path("Discountdetails")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id",id)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);

//                            db1.update("Discountdetails", contentValues, where11, new String[]{});
                }
                seventeen_1.close();
            } while (seventeen.moveToNext());
        }
        seventeen.close();

        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        Cursor eightteen = db1.rawQuery("SELECT * FROM Cardnumber WHERE datetimee_new is null OR datetimee_new = ''", null);
        if (eightteen.moveToFirst()) {
            do {
                String id = eightteen.getString(0);
                String billno = eightteen.getString(2);

                Cursor eightteen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                if (eightteen_1.moveToFirst()) {
                    String a = eightteen_1.getString(33);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("datetimee_new", a);
                    String where11 = "_id = '" + id + "' ";


                    contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Cardnumber");
                    getContentResolver().update(contentUri, contentValues,where11,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProvider.AUTHORITY)
                            .path("Cardnumber")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id",id)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);

//                            db1.update("Cardnumber", contentValues, where11, new String[]{});
                }
                eightteen_1.close();
            } while (eightteen.moveToNext());
        }
        eightteen.close();


        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        Cursor nineteen = db1.rawQuery("SELECT * FROM All_Sales_Cancelled WHERE datetimee_new is null OR datetimee_new = ''", null);
        if (nineteen.moveToFirst()) {
            do {
                String id = nineteen.getString(0);
                String billno = nineteen.getString(11);

                Cursor nineteen_1 = db1.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billno + "'", null);
                if (nineteen_1.moveToFirst()) {
                    String a = nineteen_1.getString(12);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("datetimee_new", a);
                    String where11 = "_id = '" + id + "' ";

                    contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "All_Sales_Cancelled");
                    getContentResolver().update(contentUri, contentValues,where11,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProvider.AUTHORITY)
                            .path("All_Sales_Cancelled")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id",id)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                            db1.update("All_Sales_Cancelled", contentValues, where11, new String[]{});
                }
                nineteen_1.close();
            } while (nineteen.moveToNext());
        }
        nineteen.close();


        Cursor change_time = db.rawQuery("SELECT * FROM Change_time_format", null);
        if (change_time.moveToFirst()) {
            String zero = change_time.getString(0);
            one_one = change_time.getString(1);
        }
        change_time.close();


        if (one_one.toString().equals("not changed")) {
            db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor fourteen_11 = db1.rawQuery("SELECT * FROM All_Sales", null);
            if (fourteen_11.moveToFirst()) {
                do {
                    String id = fourteen_11.getString(0);
                    String dt = fourteen_11.getString(26);
                    String dt_new = fourteen_11.getString(33);
//                    String bill_no_s = fourteen_11.getString(11);

                    String dt1 = dt.substring(8, 10);

                    if (dt1.toString().equals("24")) {
                        dt = dt.replace(dt1, "00");

                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("datetimee", dt);
                        String where11 = "_id = '" + id + "' ";

                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "All_Sales");
                        getContentResolver().update(contentUri, contentValues1,where11,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProvider.AUTHORITY)
                                .path("All_Sales")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id",id)
                                .build();
                        getContentResolver().notifyChange(resultUri, null);

//                                db1.update("All_Sales", contentValues1, where11, new String[]{});
                    }

                    String dt2 = dt_new.substring(8, 10);

                    if (dt2.toString().equals("24")) {
                        dt_new = dt_new.replace(dt2, "00");

                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("datetimee_new", dt_new);
                        String where11 = "_id = '" + id + "' ";

                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "All_Sales");
                        getContentResolver().update(contentUri, contentValues1,where11,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProvider.AUTHORITY)
                                .path("All_Sales")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id",id)
                                .build();
                        getContentResolver().notifyChange(resultUri, null);

//                                db1.update("All_Sales", contentValues1, where11, new String[]{});
                    }


                } while (fourteen_11.moveToNext());
            }
            fourteen_11.close();

            db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor fifteenn = db1.rawQuery("SELECT * FROM Billnumber", null);
            if (fifteenn.moveToFirst()) {
                do {
                    String id = fifteenn.getString(0);
                    String billno = fifteenn.getString(1);
                    String dt_new = fifteenn.getString(12);
                    TextView tv1 = new TextView(MainActivity.this);
                    tv1.setText(billno);

                    String dt2 = dt_new.substring(8, 10);
                    if (dt2.toString().equals("24")) {
                        dt_new = dt_new.replace(dt2, "00");
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("datetimee_new", dt_new);
                        String where11 = "_id = '" + id + "' ";

                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Billnumber");
                        getContentResolver().update(contentUri, contentValues,where11,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProvider.AUTHORITY)
                                .path("Billnumber")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id",id)
                                .build();
                        getContentResolver().notifyChange(resultUri, null);


//                                db1.update("Billnumber", contentValues, where11, new String[]{});
                    }

//                    Cursor fifteen_11 = null;
//                    try {
//                        Cursor fifteen_11 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + tv1.getText().toString() + "'", null);
//                        if (fifteen_11.moveToFirst()) {
//                            String a = fifteen_11.getString(33);
//
//
//                        }
//                    }finally {
//                        if(fifteen_11 != null)
//                            fifteen_11.close();
//                    }

                } while (fifteenn.moveToNext());
            }
            fifteenn.close();

            db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor sixteenn = db1.rawQuery("SELECT * FROM Customerdetails", null);
            if (sixteenn.moveToFirst()) {
                do {
                    String id = sixteenn.getString(0);
                    String billno = sixteenn.getString(6);

                    Cursor sixteen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                    if (sixteen_1.moveToFirst()) {
                        String a = sixteen_1.getString(33);

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("datetimee_new", a);
                        String where11 = "_id = '" + id + "' ";

                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
                        getContentResolver().update(contentUri, contentValues,where11,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProvider.AUTHORITY)
                                .path("Customerdetails")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id",id)
                                .build();
                        getContentResolver().notifyChange(resultUri, null);


//                                db1.update("Customerdetails", contentValues, where11, new String[]{});
                    }
                    sixteen_1.close();
                } while (sixteenn.moveToNext());
            }
            sixteenn.close();

            db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor seventeenn = db1.rawQuery("SELECT * FROM Discountdetails", null);
            if (seventeenn.moveToFirst()) {
                do {
                    String id = seventeenn.getString(0);
                    String billno = seventeenn.getString(3);

                    Cursor seventeen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                    if (seventeen_1.moveToFirst()) {
                        String a = seventeen_1.getString(33);

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("datetimee_new", a);
                        String where11 = "_id = '" + id + "' ";

                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Discountdetails");
                        getContentResolver().update(contentUri, contentValues,where11,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProvider.AUTHORITY)
                                .path("Discountdetails")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id",id)
                                .build();
                        getContentResolver().notifyChange(resultUri, null);

//                                db1.update("Discountdetails", contentValues, where11, new String[]{});
                    }
                    seventeen_1.close();
                } while (seventeenn.moveToNext());
            }
            seventeenn.close();

            db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor eightteenn = db1.rawQuery("SELECT * FROM Cardnumber", null);
            if (eightteenn.moveToFirst()) {
                do {
                    String id = eightteenn.getString(0);
                    String billno = eightteenn.getString(2);

                    Cursor eightteen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                    if (eightteen_1.moveToFirst()) {
                        String a = eightteen_1.getString(33);

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("datetimee_new", a);
                        String where11 = "_id = '" + id + "' ";

                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Cardnumber");
                        getContentResolver().update(contentUri, contentValues,where11,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProvider.AUTHORITY)
                                .path("Cardnumber")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id",id)
                                .build();
                        getContentResolver().notifyChange(resultUri, null);

//                                db1.update("Cardnumber", contentValues, where11, new String[]{});
                    }
                    eightteen_1.close();
                } while (eightteenn.moveToNext());
            }
            eightteenn.close();

            db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor nineteenn = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
            if (nineteenn.moveToFirst()) {
                do {
                    String id = nineteenn.getString(0);
                    String billno = nineteenn.getString(11);

                    Cursor nineteen_1 = db1.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billno + "'", null);
                    if (nineteen_1.moveToFirst()) {
                        String a = nineteen_1.getString(12);

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("datetimee_new", a);
                        String where11 = "_id = '" + id + "' ";

                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "All_Sales_Cancelled");
                        getContentResolver().update(contentUri, contentValues,where11,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProvider.AUTHORITY)
                                .path("All_Sales_Cancelled")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id",id)
                                .build();
                        getContentResolver().notifyChange(resultUri, null);

//                                db1.update("All_Sales_Cancelled", contentValues, where11, new String[]{});
                    }
                    nineteen_1.close();
                } while (nineteenn.moveToNext());
            }
            nineteenn.close();
        }

        int myDays = 60;

        final Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DATE, (myDays * -1));  // number of days to subtract
        int newDate =     (c1.get(Calendar.YEAR) * 10000) +
                ((c1.get(Calendar.MONTH) + 1) * 100) +
                (c1.get(Calendar.DAY_OF_MONTH));
        String newDate1 = String.valueOf(newDate)+"0000";
        System.out.println("date is "+newDate1);

//        Cursor cursor2w = db1.rawQuery("SELECT * FROM all_sales WHERE datetimee_new <= '"+newDate1+"'", null);
//        if (cursor2w.moveToFirst()){
//            do {
//
//            }while (cursor2w.moveToNext());
//        }

        db1.close();
        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);

//        db.close();
        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        db1.execSQL("DELETE FROM all_sales WHERE datetimee_new <= '"+newDate1+"'");
        db1.execSQL("DELETE FROM billnumber WHERE datetimee_new <= '"+newDate1+"'");

        Cursor fifteen_1 = db.rawQuery("SELECT * FROM Change_time_format", null);
        if (fifteen_1.moveToFirst()) {
            String zero = fifteen_1.getString(0);
            ContentValues contentValues = new ContentValues();
            contentValues.put("status", "changed");

            String where11 = "_id = '" + zero + "' ";

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Change_time_format");
            getContentResolver().update(contentUri, contentValues, where11,new String[]{});
            resultUri = new Uri.Builder()
                    .scheme("content")
                    .authority(StubProviderApp.AUTHORITY)
                    .path("Change_time_format")
                    .appendQueryParameter("operation", "update")
                    .appendQueryParameter("_id",zero)
                    .build();
            getContentResolver().notifyChange(resultUri, null);

//                    db.update("Change_time_format", contentValues, where11, new String[]{});
        }
        fifteen_1.close();


        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        Cursor usr_id_null = db1.rawQuery("SELECT * FROM Customerdetails WHERE user_id is null", null);
        if (usr_id_null.moveToFirst()) {
            do {
                String id = usr_id_null.getString(0);
                ContentValues contentValues5 = new ContentValues();
                contentValues5.put("user_id", "");
                String where = "_id = '" + id + "'";

                contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
                getContentResolver().update(contentUri, contentValues5,where,new String[]{});
                resultUri = new Uri.Builder()
                        .scheme("content")
                        .authority(StubProvider.AUTHORITY)
                        .path("Customerdetails")
                        .appendQueryParameter("operation", "update")
                        .appendQueryParameter("_id",id)
                        .build();
                getContentResolver().notifyChange(resultUri, null);

//                        db1.update("Customerdetails", contentValues5, where, new String[]{});
            } while (usr_id_null.moveToNext());
        }
        usr_id_null.close();


        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        Cursor limit_null = db1.rawQuery("SELECT * FROM Customerdetails WHERE limit_status is null", null);
        if (limit_null.moveToFirst()) {
            do {
                String id = limit_null.getString(0);
                ContentValues contentValues5 = new ContentValues();
                contentValues5.put("limit_status", "no limit");
                contentValues5.put("limit_value", "");
                contentValues5.put("limit_present_value", "");
                String where = "_id = '" + id + "'";

                contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Customerdetails");
                getContentResolver().update(contentUri, contentValues5,where,new String[]{});
                resultUri = new Uri.Builder()
                        .scheme("content")
                        .authority(StubProvider.AUTHORITY)
                        .path("Customerdetails")
                        .appendQueryParameter("operation", "update")
                        .appendQueryParameter("_id",id)
                        .build();
                getContentResolver().notifyChange(resultUri, null);

//                        db1.update("Customerdetails", contentValues5, where, new String[]{});
            } while (limit_null.moveToNext());
        }
        limit_null.close();


        ContentValues contentValues = new ContentValues();
        contentValues.put("status", "");
        String where1 = "_id = '1' ";

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "BTConn");
        getContentResolver().update(contentUri, contentValues, where1,new String[]{});
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("BTConn")
                .appendQueryParameter("operation", "update")
                .appendQueryParameter("_id","1")
                .build();
        getContentResolver().notifyChange(resultUri, null);

//                db.update("BTConn", contentValues, where1, new String[]{});

        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("status", "");
        String where11 = "_id = '1' ";

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "IPConn");
        getContentResolver().update(contentUri, contentValues1, where11,new String[]{});
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("IPConn")
                .appendQueryParameter("operation", "update")
                .appendQueryParameter("_id","1")
                .build();
        getContentResolver().notifyChange(resultUri, null);
//                db.update("IPConn", contentValues1, where11, new String[]{});

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "IPConn_KOT1");
        getContentResolver().update(contentUri, contentValues1, where11,new String[]{});
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("IPConn_KOT1")
                .appendQueryParameter("operation", "update")
                .appendQueryParameter("_id","1")
                .build();
        getContentResolver().notifyChange(resultUri, null);
//                db.update("IPConn_KOT1", contentValues1, where11, new String[]{});

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "IPConn_KOT2");
        getContentResolver().update(contentUri, contentValues1, where11,new String[]{});
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("IPConn_KOT2")
                .appendQueryParameter("operation", "update")
                .appendQueryParameter("_id","1")
                .build();
        getContentResolver().notifyChange(resultUri, null);
//                db.update("IPConn_KOT2", contentValues1, where11, new String[]{});

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "IPConn_KOT3");
        getContentResolver().update(contentUri, contentValues1, where11,new String[]{});
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("IPConn_KOT3")
                .appendQueryParameter("operation", "update")
                .appendQueryParameter("_id","1")
                .build();
        getContentResolver().notifyChange(resultUri, null);
//                db.update("IPConn_KOT3", contentValues1, where11, new String[]{});

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "IPConn_KOT4");
        getContentResolver().update(contentUri, contentValues1, where11,new String[]{});
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("IPConn_KOT4")
                .appendQueryParameter("operation", "update")
                .appendQueryParameter("_id","1")
                .build();
        getContentResolver().notifyChange(resultUri, null);
//                db.update("IPConn_KOT4", contentValues1, where11, new String[]{});



        ContentValues contentValues11 = new ContentValues();
        contentValues11.put("status", "");
        String where112 = "_id = '1' ";

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "IPConn_Counter");
        getContentResolver().update(contentUri, contentValues11, where112,new String[]{});
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("IPConn_Counter")
                .appendQueryParameter("operation", "update")
                .appendQueryParameter("_id","1")
                .build();
        getContentResolver().notifyChange(resultUri, null);



      //  db.update("IPConn_Counter", contentValues11, where112, new String[]{});





////////////////////Items image upload////////////////
        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

//        Cursor items1 = db.rawQuery("SELECT * FROM Items WHERE image is null", null);
//        if (items1.moveToFirst()){
//            do {
//                String id = items1.getString(0);
//                String name = items1.getString(1);
//                //Toast.makeText(MainActivity.this, "image not hter "+name, Toast.LENGTH_SHORT).show();
//                String str1 = name.substring(0, 2);
//                String str2 = str1.toUpperCase();
//                ContentValues contentValues5 = new ContentValues();
//                contentValues5.put("image", "");
//                contentValues5.put("image_text", str2);
//                String where = "_id = '" + id + "'";
//                db.update("Items", contentValues5, where, new String[]{});
//            }while (items1.moveToNext());
//        }
//
//        Cursor items2 = db.rawQuery("SELECT * FROM Items WHERE image = '' ", null);
//        if (items2.moveToFirst()){
//            do {
//                String id = items2.getString(0);
//                String name = items2.getString(1);
//                //Toast.makeText(MainActivity.this, "image not hter "+name, Toast.LENGTH_SHORT).show();
//                String str1 = name.substring(0, 2);
//                String str2 = str1.toUpperCase();
//                ContentValues contentValues5 = new ContentValues();
//                contentValues5.put("image", "");
//                contentValues5.put("image_text", str2);
//                String where = "_id = '" + id + "'";
//                db.update("Items", contentValues5, where, new String[]{});
//            }while (items2.moveToNext());
//        }

//        Cursor modifiers1 = db.rawQuery("SELECT * FROM Items WHERE image is null", null);
//        if (modifiers1.moveToFirst()){
//            do {
//                String id = modifiers1.getString(0);
//                String name = modifiers1.getString(1);
//                //Toast.makeText(MainActivity.this, "image not hter "+name, Toast.LENGTH_SHORT).show();
//                String str1 = name.substring(0, 2);
//                String str2 = str1.toUpperCase();
//                ContentValues contentValues5 = new ContentValues();
//                contentValues5.put("image", "");
//                contentValues5.put("image_text", str2);
//                String where = "_id = '" + id + "'";
//                db.update("Items", contentValues5, where, new String[]{});
//            }while (modifiers1.moveToNext());
//        }
//
//        Cursor modifiers2 = db.rawQuery("SELECT * FROM Items WHERE image = '' ", null);
//        if (modifiers2.moveToFirst()){
//            do {
//                String id = modifiers2.getString(0);
//                String name = modifiers2.getString(1);
//                //Toast.makeText(MainActivity.this, "image not hter "+name, Toast.LENGTH_SHORT).show();
//                String str1 = name.substring(0, 2);
//                String str2 = str1.toUpperCase();
//                ContentValues contentValues5 = new ContentValues();
//                contentValues5.put("image", "");
//                contentValues5.put("image_text", str2);
//                String where = "_id = '" + id + "'";
//                db.update("Items", contentValues5, where, new String[]{});
//            }while (modifiers2.moveToNext());
//        }


//        Cursor items = db.rawQuery("SELECT * FROM Items", null);
//        if (items.moveToFirst()){
//            do {
//                String id = items.getString(0);
//                String name = items.getString(1);
//                byte[] image = items.getBlob(6);
//                String cate = items.getString(4);
//                String tax = items.getString(5);
//
//                if (image.length < 0){
//                    Toast.makeText(MainActivity.this, "image not hter "+name, Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(MainActivity.this, "image hter "+name, Toast.LENGTH_SHORT).show();
//                }
////                Bitmap bmPicture = BitmapFactory.decodeByteArray(image, 0, image.length);
////                if (bmPicture == null) {
////                    //Toast.makeText(MainActivity.this, "no image for "+id, Toast.LENGTH_SHORT).show();
////                    String str1 = name.substring(0, 2);
////                    String str2 = str1.toUpperCase();
////                    ContentValues contentValues5 = new ContentValues();
////                    contentValues5.put("image_text", str2);
////                    String where = "_id = '" + id + "'";
////                    db.update("Items", contentValues5, where, new String[]{});
////
////                }
////                if (image == null) {
////                    //Toast.makeText(MainActivity.this, "no image for "+id, Toast.LENGTH_SHORT).show();
////                    String str1 = name.substring(0, 2);
////                    String str2 = str1.toUpperCase();
////                    ContentValues contentValues5 = new ContentValues();
////                    contentValues5.put("image_text", str2);
////                    String where = "_id = '" + id + "'";
////                    db.update("Items", contentValues5, where, new String[]{});
////
////                }
//            }while (items.moveToNext());
//
//        }

        db.execSQL("UPDATE Items set favourites = 'no'");
        db1.execSQL("UPDATE all_sales set old_total = total WHERE old_total = '' OR old_total IS NULL");

//        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//
//        Cursor fr2 = db.rawQuery("SELECT * FROM Items", null);//Favourites removed
//        int numberOfRows2 = fr2.getCount();
//
//        int limit2 = 0;
//        if (numberOfRows2 > 100) {
//            while (limit2 + 100 < numberOfRows2) {
//                Cursor itemsc = db.rawQuery("SELECT * FROM Items WHERE favourites is null or favourites = '' LIMIT '" + limit2 + "', 100", null);
//                if (itemsc.moveToFirst()) {
//                    do {
//                        String id = itemsc.getString(0);
//                        ContentValues contentValues5 = new ContentValues();
//                        contentValues5.put("favourites", "no");
//                        String where = "_id = '" + id + "'";
//                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                        getContentResolver().update(contentUri, contentValues5, where,new String[]{});
//                        resultUri = new Uri.Builder()
//                                .scheme("content")
//                                .authority(StubProviderApp.AUTHORITY)
//                                .path("Items")
//                                .appendQueryParameter("operation", "update")
//                                .appendQueryParameter("_id",id)
//                                .build();
//                        getContentResolver().notifyChange(resultUri, null);
////                                db.update("Items", contentValues5, where, new String[]{});
//                    } while (itemsc.moveToNext());
//
//                }
//                itemsc.close();
//                limit2 += 100;
//            }
//            int news = numberOfRows2 - limit2;
//            if (news == 0) {
//                //Toast.makeText(BeveragesMenuFragment_Dine.this, "limit is b " + limit, Toast.LENGTH_SHORT).show();
//            } else {
//                Cursor itemsc = db.rawQuery("SELECT * FROM Items WHERE favourites is null or favourites = '' LIMIT '" + news + "' OFFSET '" + limit2 + "' ", null);
//                if (itemsc.moveToFirst()) {
//                    do {
//                        String id = itemsc.getString(0);
//                        ContentValues contentValues5 = new ContentValues();
//                        contentValues5.put("favourites", "no");
//                        String where = "_id = '" + id + "'";
//                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                        getContentResolver().update(contentUri, contentValues5, where,new String[]{});
//                        resultUri = new Uri.Builder()
//                                .scheme("content")
//                                .authority(StubProviderApp.AUTHORITY)
//                                .path("Items")
//                                .appendQueryParameter("operation", "update")
//                                .appendQueryParameter("_id",id)
//                                .build();
//                        getContentResolver().notifyChange(resultUri, null);
////                                db.update("Items", contentValues5, where, new String[]{});
//                    } while (itemsc.moveToNext());
//
//                }
//                itemsc.close();
//            }
//        } else {
//            Cursor itemsc = db.rawQuery("SELECT * FROM Items WHERE favourites is null or favourites = '' ", null);
//            if (itemsc.moveToFirst()) {
//                do {
//                    String id = itemsc.getString(0);
//                    ContentValues contentValues5 = new ContentValues();
//                    contentValues5.put("favourites", "no");
//                    String where = "_id = '" + id + "'";
//                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                    getContentResolver().update(contentUri, contentValues5, where,new String[]{});
//                    resultUri = new Uri.Builder()
//                            .scheme("content")
//                            .authority(StubProviderApp.AUTHORITY)
//                            .path("Items")
//                            .appendQueryParameter("operation", "update")
//                            .appendQueryParameter("_id",id)
//                            .build();
//                    getContentResolver().notifyChange(resultUri, null);
////                            db.update("Items", contentValues5, where, new String[]{});
//                } while (itemsc.moveToNext());
//
//            }
//            itemsc.close();
//        }
//        fr2.close();

//////////////////////////////////

//        Cursor fr3 = db.rawQuery("SELECT * FROM Items", null);
//        int numberOfRows3 = fr3.getCount();
//
//        int limit3 = 0;
//        if (numberOfRows3 > 100) {
//            while (limit3 + 100 < numberOfRows3) {
//                Cursor itemsc = db.rawQuery("SELECT * FROM Items WHERE weekdaysvalue is null or weekdaysvalue = '' LIMIT '" + limit3 + "', 100", null);
//                if (itemsc.moveToFirst()) {
//                    do {
//                        String id = itemsc.getString(0);
//                        ContentValues contentValues5 = new ContentValues();
//                        contentValues5.put("weekdaysvalue", "0");
//                        String where = "_id = '" + id + "'";
//                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                        getContentResolver().update(contentUri, contentValues5, where,new String[]{});
//                        resultUri = new Uri.Builder()
//                                .scheme("content")
//                                .authority(StubProviderApp.AUTHORITY)
//                                .path("Items")
//                                .appendQueryParameter("operation", "update")
//                                .appendQueryParameter("_id",id)
//                                .build();
//                        getContentResolver().notifyChange(resultUri, null);
////                                db.update("Items", contentValues5, where, new String[]{});
//                    } while (itemsc.moveToNext());
//
//                }
//                itemsc.close();
//                limit3 += 100;
//            }
//            int news = numberOfRows3 - limit3;
//            if (news == 0) {
//                //Toast.makeText(BeveragesMenuFragment_Dine.this, "limit is b " + limit, Toast.LENGTH_SHORT).show();
//            } else {
//                Cursor itemsc = db.rawQuery("SELECT * FROM Items WHERE weekdaysvalue is null or weekdaysvalue = '' LIMIT '" + news + "' OFFSET '" + limit3 + "' ", null);
//                if (itemsc.moveToFirst()) {
//                    do {
//                        String id = itemsc.getString(0);
//                        ContentValues contentValues5 = new ContentValues();
//                        contentValues5.put("weekdaysvalue", "0");
//                        String where = "_id = '" + id + "'";
//                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                        getContentResolver().update(contentUri, contentValues5, where,new String[]{});
//                        resultUri = new Uri.Builder()
//                                .scheme("content")
//                                .authority(StubProviderApp.AUTHORITY)
//                                .path("Items")
//                                .appendQueryParameter("operation", "update")
//                                .appendQueryParameter("_id",id)
//                                .build();
//                        getContentResolver().notifyChange(resultUri, null);
////                                db.update("Items", contentValues5, where, new String[]{});
//                    } while (itemsc.moveToNext());
//
//                }
//                itemsc.close();
//            }
//        } else {
//            Cursor itemsc = db.rawQuery("SELECT * FROM Items WHERE weekdaysvalue is null or weekdaysvalue = '' ", null);
//            if (itemsc.moveToFirst()) {
//                do {
//                    String id = itemsc.getString(0);
//                    ContentValues contentValues5 = new ContentValues();
//                    contentValues5.put("weekdaysvalue", "0");
//                    String where = "_id = '" + id + "'";
//                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                    getContentResolver().update(contentUri, contentValues5, where,new String[]{});
//                    resultUri = new Uri.Builder()
//                            .scheme("content")
//                            .authority(StubProviderApp.AUTHORITY)
//                            .path("Items")
//                            .appendQueryParameter("operation", "update")
//                            .appendQueryParameter("_id",id)
//                            .build();
//                    getContentResolver().notifyChange(resultUri, null);
////                            db.update("Items", contentValues5, where, new String[]{});
//                } while (itemsc.moveToNext());
//
//            }
//            itemsc.close();
//        }
//        fr3.close();
////////////////////////////////////////


///////////////////////////modifiers default

//        Cursor modifiers = db.rawQuery("SELECT * FROM Modifiers", null);
//        if (modifiers.moveToFirst()){
//            do {
//                String id = modifiers.getString(0);
//                String name = modifiers.getString(1);
//                byte[] image = modifiers.getBlob(6);
//
//                if (image == null) {
//                    //Toast.makeText(MainActivity.this, "no image for "+id, Toast.LENGTH_SHORT).show();
//                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.item_bg_image2);
//                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                    b.compress(Bitmap.CompressFormat.PNG, 100, bos);
//                    img = bos.toByteArray();
//                    ContentValues contentValues5 = new ContentValues();
//                    contentValues5.put("modimage", img);
//                    String where = "_id = '" + id + "'";
//                    db.update("Modifiers", contentValues5, where, new String[]{});
//
//                }
//            }while (modifiers.moveToNext());
//
//        }

//        Cursor modi1 = db.rawQuery("SELECT * FROM Modifiers", null);
//        if (modi1.moveToFirst()) {
//            do {
//                String id = modi1.getString(0);
//                String quan = modi1.getString(3);
//                String tax = modi1.getString(5);
//
//
//                TextView textView1 = new TextView(MainActivity.this);
//                textView1.setText(tax);
//                TextView textView2 = new TextView(MainActivity.this);
//                textView2.setText(quan);
//
//
//                //Toast.makeText(MainActivity.this, "category "+cate+" tax is "+tax, Toast.LENGTH_SHORT).show();
//                if (textView1.getText().toString().equals("null") || textView1.getText().toString().equals("")) {
//                    //Toast.makeText(MainActivity.this, "no tax for "+id, Toast.LENGTH_SHORT).show();
//                    ContentValues contentValues2 = new ContentValues();
//                    contentValues2.put("moditemtax", "None");
//                    String where2 = "_id = '" + id + "'";
//                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers");
//                    getContentResolver().update(contentUri, contentValues2, where2,new String[]{});
//                    resultUri = new Uri.Builder()
//                            .scheme("content")
//                            .authority(StubProviderApp.AUTHORITY)
//                            .path("Modifiers")
//                            .appendQueryParameter("operation", "update")
//                            .appendQueryParameter("_id",id)
//                            .build();
//                    getContentResolver().notifyChange(resultUri, null);
////                            db.update("Modifiers", contentValues2, where2, new String[]{});
//                } else {
//                    //Toast.makeText(MainActivity.this, "tax for "+id+" is "+tax, Toast.LENGTH_SHORT).show();
//                }
//
//                if (textView2.getText().toString().equals("null") || textView2.getText().toString().equals("")) {
//                    //Toast.makeText(MainActivity.this, "no tax for "+id, Toast.LENGTH_SHORT).show();
//                    ContentValues contentValues2 = new ContentValues();
//                    contentValues2.put("modstockquan", "0");
//                    String where2 = "_id = '" + id + "'";
//                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers");
//                    getContentResolver().update(contentUri, contentValues2, where2,new String[]{});
//                    resultUri = new Uri.Builder()
//                            .scheme("content")
//                            .authority(StubProviderApp.AUTHORITY)
//                            .path("Modifiers")
//                            .appendQueryParameter("operation", "update")
//                            .appendQueryParameter("_id",id)
//                            .build();
//                    getContentResolver().notifyChange(resultUri, null);
////                            db.update("Modifiers", contentValues2, where2, new String[]{});
//                } else {
//                    //Toast.makeText(MainActivity.this, "tax for "+id+" is "+tax, Toast.LENGTH_SHORT).show();
//                }
//
//
//            } while (modi1.moveToNext());
//
//        }
//        modi1.close();


        Cursor tax = db.rawQuery("SELECT * FROM Taxes", null);
        if (tax.moveToFirst()) {
            do {
                String id = tax.getString(0);
                String taxtype = tax.getString(3);

                TextView textView1 = new TextView(MainActivity.this);
                textView1.setText(taxtype);

                if (textView1.getText().toString().equals("null") || textView1.getText().toString().equals("")) {
                    //Toast.makeText(MainActivity.this, "no tax for "+id, Toast.LENGTH_SHORT).show();
                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put("taxtype", "Globaltax");
                    String where2 = "_id = '" + id + "'";
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Taxes");
                    getContentResolver().update(contentUri, contentValues2, where2,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Taxes")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id",id)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);

//                            db.update("Taxes", contentValues2, where2, new String[]{});
                } else {
                    //Toast.makeText(MainActivity.this, "tax for "+id+" is "+tax, Toast.LENGTH_SHORT).show();
                }
            } while (tax.moveToNext());

        }
        tax.close();

//        db.execSQL("INSERT INTO Items (itemname, price, stockquan, category, itemtax, weekdaysvalue, weekendsvalue) " +
//                "SELECT field2, field3, field4, field5, field6, field8, field9 FROM Items2");
////////////////////images upload//////////////////////
        //coonectBT();


        Cursor billc = db1.rawQuery("SELECT * FROM Billnumber WHERE _id = '1' ", null);
        if (billc.moveToFirst()) {
            String id = billc.getString(0);
            String bco = billc.getString(11);
            TextView c = new TextView(MainActivity.this);
            c.setText(bco);
            if (c.getText().toString().equals("") || c.getText().toString().equals(null)) {
                Cursor billc1 = db1.rawQuery("SELECT * FROM Billnumber", null);
                if (billc1.moveToFirst()) {
                    do {
                        String id1 = billc1.getString(0);
//                            String bco1 = billc1.getString(11);
                        ContentValues contentValues5 = new ContentValues();
                        contentValues5.put("billcount", id1);
                        String where = "_id = '" + id1 + "'";

                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Billnumber");
                        getContentResolver().update(contentUri, contentValues5,where,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProvider.AUTHORITY)
                                .path("Billnumber")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id",id1)
                                .build();
                        getContentResolver().notifyChange(resultUri, null);

//                                db1.update("Billnumber", contentValues5, where, new String[]{});
                    } while (billc1.moveToNext());

                }
                billc1.close();
            }

//            }while (billc.moveToNext());

        }
        billc.close();



        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        Cursor co31 = db1.rawQuery("SELECT * FROM Itemwiseorderlistmodifiers", null);
        int cou31 = co31.getColumnCount();
        if (String.valueOf(cou31).equals("6")) {
            db1.execSQL("ALTER TABLE Itemwiseorderlistmodifiers ADD COLUMN category");
        }
        co31.close();

        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        Cursor twleve = db1.rawQuery("SELECT * FROM Clicked_cust_name", null);
        if (twleve.moveToFirst()) {

        } else {
            ContentValues contentValuess = new ContentValues();
            contentValuess.put("name", "");
            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Clicked_cust_name");
            resultUri = getContentResolver().insert(contentUri, contentValuess);
            getContentResolver().notifyChange(resultUri, null);
//                    db1.insert("Clicked_cust_name", null, contentValues);
        }
        twleve.close();





    }

    private void initApp() {

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
        db_inapp.execSQL("CREATE TABLE if not exists Messaginglicense (_id integer PRIMARY KEY UNIQUE, remainingmessages text, Messagessent text,orderid text,time text,date text, package text);");
        db_inapp.execSQL("CREATE TABLE if not exists Orderlicense (_id integer PRIMARY KEY UNIQUE, remainingorders text, ordersrece text,orderid text,time text,date text, package text);");
        db_inapp.execSQL("CREATE TABLE if not exists Pro_upgrade (_id integer PRIMARY KEY UNIQUE, status text, orderid text);");
        db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);


        Cursor cursor2 = db_inapp.rawQuery("SELECT * FROM Messaginglicense ", null);
        if (cursor2.moveToFirst()) {

        } else {
            ContentValues cv = new ContentValues();
            cv.put("Messagessent", "0");
            cv.put("remainingmessages", "100");
            cv.put("date", "");
            cv.put("time", "");
            db_inapp.insert("Messaginglicense", null, cv);
        }
        cursor2.close();


        Cursor cursor21 = db_inapp.rawQuery("SELECT * FROM Orderlicense ", null);
        if (cursor21.moveToFirst()) {

        } else {
            ContentValues cv = new ContentValues();
            cv.put("ordersrece", "0");
            cv.put("remainingorders", "500");
            cv.put("date", "");
            cv.put("time", "");
            db_inapp.insert("Orderlicense", null, cv);
        }
        cursor21.close();

        progressbar11=findViewById(R.id.progressbar11);
        tv_copy=findViewById(R.id.copy);
        scrollView=findViewById(R.id.sv_scroll);

        progressbar11.setVisibility(View.GONE);
        tv_copy.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.VISIBLE);

        Cursor ladmin = db.rawQuery("SELECT * FROM LAdmin", null);
        if (ladmin.moveToFirst()){
            ladminuser = ladmin.getString(1);
            ladminpass = ladmin.getString(2);
        }
        ladmin.close();
        Cursor madmin = db.rawQuery("SELECT * FROM LOGIN", null);
        if (madmin.moveToFirst()){
            madminuser = madmin.getString(1);
            madminpass = madmin.getString(2);
        }
        madmin.close();
        Cursor user1 = db.rawQuery("SELECT * FROM User1", null);
        if (user1.moveToFirst()){
            useroneuser = user1.getString(2);
            useronepass = user1.getString(3);
        }
        user1.close();
        Cursor user2 = db.rawQuery("SELECT * FROM User2", null);
        if (user2.moveToFirst()){
            usertwouser = user2.getString(2);
            usertwopass = user2.getString(3);
        }
        user2.close();
        Cursor user3 = db.rawQuery("SELECT * FROM User3", null);
        if (user3.moveToFirst()){
            userthreeuser = user3.getString(2);
            userthreepass = user3.getString(3);
        }
        user3.close();
        Cursor user4 = db.rawQuery("SELECT * FROM User4", null);
        if (user4.moveToFirst()){
            userfouruser = user4.getString(2);
            userfourpass = user4.getString(3);
        }
        user4.close();
        Cursor user5 = db.rawQuery("SELECT * FROM User5", null);
        if (user5.moveToFirst()){
            userfiveuser = user5.getString(2);
            userfivepass = user5.getString(3);
        }
        user5.close();
        Cursor user6 = db.rawQuery("SELECT * FROM User6", null);
        if (user6.moveToFirst()){
            usersixuser = user6.getString(2);
            usersixpass = user6.getString(3);
        }
        user6.close();
        box1 = (TextView) findViewById(R.id.box1);
        box1.setFocusable(true);
        box1.setFocusableInTouchMode(true);
        box1.requestFocus();
        box2 = (TextView) findViewById(R.id.box2);
        box3 = (TextView) findViewById(R.id.box3);
        box4 = (TextView) findViewById(R.id.box4);
        final Button one = (Button)findViewById(R.id.one);
        final Button two = (Button)findViewById(R.id.two);
        final Button three = (Button)findViewById(R.id.three);
        final Button four = (Button)findViewById(R.id.four);
        final Button five = (Button)findViewById(R.id.five);
        final Button six = (Button)findViewById(R.id.six);
        final Button seven = (Button)findViewById(R.id.seven);
        final Button eight = (Button)findViewById(R.id.eight);
        final Button nine = (Button)findViewById(R.id.nine);
        final Button clear = (Button)findViewById(R.id.clear);
        final Button zero = (Button)findViewById(R.id.zero);
        final ImageButton backspace = (ImageButton)findViewById(R.id.backspace);
        wrongpin = (TextView)findViewById(R.id.wrong);
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (box1.getText().toString().equals("")){
                    box2.setFocusableInTouchMode(true);
                    box2.setFocusable(true);
                    box2.requestFocus();
                    box1.setText("1");
                }else {
                    if (box2.getText().toString().equals("")) {
                        box3.setFocusableInTouchMode(true);
                        box3.setFocusable(true);
                        box3.requestFocus();
                        box2.setText("1");
                    }else {
                        if (box3.getText().toString().equals("")) {
                            box4.setFocusableInTouchMode(true);
                            box4.setFocusable(true);
                            box4.requestFocus();
                            box3.setText("1");
                        }else {
                            if (box4.getText().toString().equals("")) {
                                box4.setText("1");
                            }
                        }
                    }
                }

            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (box1.getText().toString().equals("")){
                    box2.setFocusableInTouchMode(true);
                    box2.setFocusable(true);
                    box2.requestFocus();
                    box1.setText("2");
                }else {
                    if (box2.getText().toString().equals("")) {
                        box3.setFocusableInTouchMode(true);
                        box3.setFocusable(true);
                        box3.requestFocus();
                        box2.setText("2");
                    }else {
                        if (box3.getText().toString().equals("")) {
                            box4.setFocusableInTouchMode(true);
                            box4.setFocusable(true);
                            box4.requestFocus();
                            box3.setText("2");
                        }else {
                            if (box4.getText().toString().equals("")) {
                                box4.setText("2");
                            }
                        }
                    }
                }

            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (box1.getText().toString().equals("")){
                    box2.setFocusableInTouchMode(true);
                    box2.setFocusable(true);
                    box2.requestFocus();
                    box1.setText("3");
                }else {
                    if (box2.getText().toString().equals("")) {
                        box3.setFocusableInTouchMode(true);
                        box3.setFocusable(true);
                        box3.requestFocus();
                        box2.setText("3");
                    }else {
                        if (box3.getText().toString().equals("")) {
                            box4.setFocusableInTouchMode(true);
                            box4.setFocusable(true);
                            box4.requestFocus();
                            box3.setText("3");
                        }else {
                            if (box4.getText().toString().equals("")) {
                                box4.setText("3");
                            }
                        }
                    }
                }

            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (box1.getText().toString().equals("")){
                    box2.setFocusableInTouchMode(true);
                    box2.setFocusable(true);
                    box2.requestFocus();
                    box1.setText("4");
                }else {
                    if (box2.getText().toString().equals("")) {
                        box3.setFocusableInTouchMode(true);
                        box3.setFocusable(true);
                        box3.requestFocus();
                        box2.setText("4");
                    }else {
                        if (box3.getText().toString().equals("")) {
                            box4.setFocusableInTouchMode(true);
                            box4.setFocusable(true);
                            box4.requestFocus();
                            box3.setText("4");
                        }else {
                            if (box4.getText().toString().equals("")) {
                                box4.setText("4");
                            }
                        }
                    }
                }

            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (box1.getText().toString().equals("")){
                    box2.setFocusableInTouchMode(true);
                    box2.setFocusable(true);
                    box2.requestFocus();
                    box1.setText("5");
                }else {
                    if (box2.getText().toString().equals("")) {
                        box3.setFocusableInTouchMode(true);
                        box3.setFocusable(true);
                        box3.requestFocus();
                        box2.setText("5");
                    }else {
                        if (box3.getText().toString().equals("")) {
                            box4.setFocusableInTouchMode(true);
                            box4.setFocusable(true);
                            box4.requestFocus();
                            box3.setText("5");
                        }else {
                            if (box4.getText().toString().equals("")) {
//                                    box1.setFocusableInTouchMode(true);
//                                    box1.setFocusable(true);
//                                    box1.requestFocus();
                                box4.setText("5");
                            }
                        }
                    }
                }

            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (box1.getText().toString().equals("")){
                    box2.setFocusableInTouchMode(true);
                    box2.setFocusable(true);
                    box2.requestFocus();
                    box1.setText("6");
                }else {
                    if (box2.getText().toString().equals("")) {
                        box3.setFocusableInTouchMode(true);
                        box3.setFocusable(true);
                        box3.requestFocus();
                        box2.setText("6");
                    }else {
                        if (box3.getText().toString().equals("")) {
                            box4.setFocusableInTouchMode(true);
                            box4.setFocusable(true);
                            box4.requestFocus();
                            box3.setText("6");
                        }else {
                            if (box4.getText().toString().equals("")) {
//                                    box1.setFocusableInTouchMode(true);
//                                    box1.setFocusable(true);
//                                    box1.requestFocus();
                                box4.setText("6");
                            }
                        }
                    }
                }

            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (box1.getText().toString().equals("")){
                    box2.setFocusableInTouchMode(true);
                    box2.setFocusable(true);
                    box2.requestFocus();
                    box1.setText("7");
                }else {
                    if (box2.getText().toString().equals("")) {
                        box3.setFocusableInTouchMode(true);
                        box3.setFocusable(true);
                        box3.requestFocus();
                        box2.setText("7");
                    }else {
                        if (box3.getText().toString().equals("")) {
                            box4.setFocusableInTouchMode(true);
                            box4.setFocusable(true);
                            box4.requestFocus();
                            box3.setText("7");
                        }else {
                            if (box4.getText().toString().equals("")) {
//                                    box1.setFocusableInTouchMode(true);
//                                    box1.setFocusable(true);
//                                    box1.requestFocus();
                                box4.setText("7");
                            }
                        }
                    }
                }

            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (box1.getText().toString().equals("")){
                    box2.setFocusableInTouchMode(true);
                    box2.setFocusable(true);
                    box2.requestFocus();
                    box1.setText("8");
                }else {
                    if (box2.getText().toString().equals("")) {
                        box3.setFocusableInTouchMode(true);
                        box3.setFocusable(true);
                        box3.requestFocus();
                        box2.setText("8");
                    }else {
                        if (box3.getText().toString().equals("")) {
                            box4.setFocusableInTouchMode(true);
                            box4.setFocusable(true);
                            box4.requestFocus();
                            box3.setText("8");
                        }else {
                            if (box4.getText().toString().equals("")) {
//                                    box1.setFocusableInTouchMode(true);
//                                    box1.setFocusable(true);
//                                    box1.requestFocus();
                                box4.setText("8");
                            }
                        }
                    }
                }

            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (box1.getText().toString().equals("")){
                    box2.setFocusableInTouchMode(true);
                    box2.setFocusable(true);
                    box2.requestFocus();
                    box1.setText("9");
                }else {
                    if (box2.getText().toString().equals("")) {
                        box3.setFocusableInTouchMode(true);
                        box3.setFocusable(true);
                        box3.requestFocus();
                        box2.setText("9");
                    }else {
                        if (box3.getText().toString().equals("")) {
                            box4.setFocusableInTouchMode(true);
                            box4.setFocusable(true);
                            box4.requestFocus();
                            box3.setText("9");
                        }else {
                            if (box4.getText().toString().equals("")) {
//                                    box1.setFocusableInTouchMode(true);
//                                    box1.setFocusable(true);
//                                    box1.requestFocus();
                                box4.setText("9");
                            }
                        }
                    }
                }

            }
        });

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (box1.getText().toString().equals("")){
                    box2.setFocusableInTouchMode(true);
                    box2.setFocusable(true);
                    box2.requestFocus();
                    box1.setText("0");
                }else {
                    if (box2.getText().toString().equals("")) {
                        box3.setFocusableInTouchMode(true);
                        box3.setFocusable(true);
                        box3.requestFocus();
                        box2.setText("0");
                    }else {
                        if (box3.getText().toString().equals("")) {
                            box4.setFocusableInTouchMode(true);
                            box4.setFocusable(true);
                            box4.requestFocus();
                            box3.setText("0");
                        }else {
                            if (box4.getText().toString().equals("")) {
//                                    box1.setFocusableInTouchMode(true);
//                                    box1.setFocusable(true);
//                                    box1.requestFocus();
                                box4.setText("0");
                            }
                        }
                    }
                }

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                box1.setText("");
                box1.setFocusableInTouchMode(true);
                box1.setFocusable(true);
                box1.requestFocus();
                box2.setText("");
                box3.setText("");
                box4.setText("");
                wrongpin.setVisibility(View.INVISIBLE);
            }
        });

        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!box4.getText().toString().isEmpty()){
                    //Toast.makeText(MainActivity.this, "4 has text "+box4.getText().toString(), Toast.LENGTH_SHORT).show();
                    box4.setText("");
                    box4.setFocusableInTouchMode(true);
                    box4.setFocusable(true);
                    box4.requestFocus();
                    wrongpin.setVisibility(View.INVISIBLE);
                }else {
                    if (!box3.getText().toString().isEmpty()){
                        //Toast.makeText(MainActivity.this, "3 has text "+box3.getText().toString(), Toast.LENGTH_SHORT).show();
                        box3.setText("");
                        box3.setFocusableInTouchMode(true);
                        box3.setFocusable(true);
                        box3.requestFocus();
                    }else {
                        if (!box2.getText().toString().isEmpty()){
                            //Toast.makeText(MainActivity.this, "2 has text "+box2.getText().toString(), Toast.LENGTH_SHORT).show();
                            box2.setText("");
                            box2.setFocusableInTouchMode(true);
                            box2.setFocusable(true);
                            box2.requestFocus();
                        }else {
                            if (!box1.getText().toString().isEmpty()){
                                //Toast.makeText(MainActivity.this, "1 has text "+box1.getText().toString(), Toast.LENGTH_SHORT).show();
                                box1.setText("");
                                box1.setFocusableInTouchMode(true);
                                box1.setFocusable(true);
                                box1.requestFocus();
                            }
                        }
                    }
                }
            }
        });

        box1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                wrongpin.setVisibility(View.INVISIBLE);
            }
        });

        box4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String floor = "";
                Cursor cursor = db.rawQuery("SELECT * FROM Floors WHERE position = '0'", null);
                if (cursor.moveToFirst()) {
                    floor = cursor.getString(1);
                }
                cursor.close();

                String id = "1", pDate = "1";
                Cursor cursor1 = db.rawQuery("SELECT * FROM asd1", null);
                if (cursor1.moveToFirst()) {
                    id = cursor1.getString(0);
                }
                cursor1.close();

                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("table", id);
                myEdit.putString("floor", floor);
                myEdit.putString("position", "0");
                myEdit.putString("table_number", pDate);
                myEdit.apply();

                combine =  box1.getText().toString()+box2.getText().toString()+box3.getText().toString()+box4.getText().toString();
                if (ladminpass.toString().equals(combine) || madminpass.toString().equals(combine) || useronepass.toString().equals(combine) ||
                        usertwopass.toString().equals(combine) || userthreepass.toString().equals(combine) || userfourpass.toString().equals(combine) ||
                        userfivepass.toString().equals(combine) || usersixpass.toString().equals(combine)) {

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    if (ladminpass.toString().equals(combine)) {
                        wrongpin.setVisibility(View.INVISIBLE);
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    permissions(),
                                    1);
                            /*if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        1);
                            } else {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        1);
                            }*/
                        } else {

                            if (account_selection.toString().equals("Dine")) {
                                Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                                if (cursor3.moveToFirst()) {
                                    String lite_pro = cursor3.getString(1);

                                    TextView tv = new TextView(MainActivity.this);
                                    tv.setText(lite_pro);

                                    if (tv.getText().toString().equals("Lite")) {
                                        Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine.class);
                                        startActivity(intent);
                                    }
                                }else {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                    startActivity(intent);
                                }
                            }else {
                                if (account_selection.toString().equals("Qsr")) {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Qsr.class);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Retail.class);
                                    startActivity(intent);
                                }
                            }
//                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine.class);
//                            startActivity(intent);
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                            getContentResolver().delete(contentUri, null,null);
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("LoginUser")
                                    .appendQueryParameter("operation", "delete")
                                    .appendQueryParameter(null, null)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.delete("LoginUser", null, null);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("username", ladminuser);
                            contentValues.put("password", ladminpass);
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                            resultUri = getContentResolver().insert(contentUri, contentValues);
                            getContentResolver().notifyChange(resultUri, null);
//                            db.insert("LoginUser", null, contentValues);

                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("login_status", "1");
                            String where1 = "_id = '1' ";

                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Home_check");
                            getContentResolver().update(contentUri, contentValues1,where1,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Home_check")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id","1")
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.update("Home_check", contentValues1, where1, new String[]{});
                            Toast.makeText(MainActivity.this, getString(R.string.login_successfull), Toast.LENGTH_SHORT).show();
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                            getContentResolver().delete(contentUri, null,null);
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("UserLogin_Checking")
                                    .appendQueryParameter("operation", "delete")
                                    .appendQueryParameter(null, null)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.delete("UserLogin_Checking", null, null);
                            ContentValues contentValues3 = new ContentValues();
                            contentValues3.put("name", "ladmin");
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                            resultUri = getContentResolver().insert(contentUri, contentValues3);
                            getContentResolver().notifyChange(resultUri, null);
//                            db.insert("UserLogin_Checking", null, contentValues3);
                        }

                    }
                    if (madminpass.toString().equals(combine)) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    permissions(),
                                    2);
                            /*if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        2);
                            } else {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        2);
                            }*/
                        } else {
                            wrongpin.setVisibility(View.INVISIBLE);

                            if (account_selection.toString().equals("Dine")) {
                                Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                                if (cursor3.moveToFirst()) {
                                    String lite_pro = cursor3.getString(1);

                                    TextView tv = new TextView(MainActivity.this);
                                    tv.setText(lite_pro);

                                    if (tv.getText().toString().equals("Lite")) {
                                        Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine.class);
                                        startActivity(intent);
                                    }
                                }else {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                    startActivity(intent);
                                }
                            }else {
                                if (account_selection.toString().equals("Qsr")) {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Qsr.class);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Retail.class);
                                    startActivity(intent);
                                }
                            }
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                            getContentResolver().delete(contentUri, null,null);
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("LoginUser")
                                    .appendQueryParameter("operation", "delete")
                                    .appendQueryParameter(null, null)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.delete("LoginUser", null, null);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("username", madminuser);
                            contentValues.put("password", madminpass);
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                            resultUri = getContentResolver().insert(contentUri, contentValues);
                            getContentResolver().notifyChange(resultUri, null);
//                            db.insert("LoginUser", null, contentValues);

                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("login_status", "1");
                            String where1 = "_id = '1' ";

                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Home_check");
                            getContentResolver().update(contentUri, contentValues1,where1,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Home_check")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id","1")
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.update("Home_check", contentValues1, where1, new String[]{});
                            Toast.makeText(MainActivity.this, getString(R.string.login_successfull), Toast.LENGTH_SHORT).show();

                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                            getContentResolver().delete(contentUri, null,null);
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("UserLogin_Checking")
                                    .appendQueryParameter("operation", "delete")
                                    .appendQueryParameter(null, null)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.delete("UserLogin_Checking", null, null);
                            ContentValues contentValues3 = new ContentValues();
                            contentValues3.put("name", "madmin");
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                            resultUri = getContentResolver().insert(contentUri, contentValues3);
                            getContentResolver().notifyChange(resultUri, null);
//                            db.insert("UserLogin_Checking", null, contentValues3);
                        }
                    }
                    if (useronepass.toString().equals(combine)) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    permissions(),
                                    3);
                            /*if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        3);
                            } else {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        3);
                            }*/
                        } else {
                            wrongpin.setVisibility(View.INVISIBLE);

                            if (account_selection.toString().equals("Dine")) {
                                Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                                if (cursor3.moveToFirst()) {
                                    String lite_pro = cursor3.getString(1);

                                    TextView tv = new TextView(MainActivity.this);
                                    tv.setText(lite_pro);

                                    if (tv.getText().toString().equals("Lite")) {
                                        Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine.class);
                                        startActivity(intent);
                                    }
                                }else {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                    startActivity(intent);
                                }
                            }else {
                                if (account_selection.toString().equals("Qsr")) {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Qsr.class);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Retail.class);
                                    startActivity(intent);
                                }
                            }
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                            getContentResolver().delete(contentUri, null,null);
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("LoginUser")
                                    .appendQueryParameter("operation", "delete")
                                    .appendQueryParameter(null, null)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.delete("LoginUser", null, null);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("username", useroneuser);
                            contentValues.put("password", useronepass);
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                            resultUri = getContentResolver().insert(contentUri, contentValues);
                            getContentResolver().notifyChange(resultUri, null);
//                            db.insert("LoginUser", null, contentValues);

                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("login_status", "1");
                            String where1 = "_id = '1' ";
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Home_check");
                            getContentResolver().update(contentUri, contentValues1,where1,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Home_check")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id","1")
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.update("Home_check", contentValues1, where1, new String[]{});
                            Toast.makeText(MainActivity.this, getString(R.string.login_successfull), Toast.LENGTH_SHORT).show();
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                            getContentResolver().delete(contentUri, null,null);
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("UserLogin_Checking")
                                    .appendQueryParameter("operation", "delete")
                                    .appendQueryParameter(null, null)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.delete("UserLogin_Checking", null, null);
                            ContentValues contentValues3 = new ContentValues();
                            contentValues3.put("name", "user1");
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                            resultUri = getContentResolver().insert(contentUri, contentValues3);
                            getContentResolver().notifyChange(resultUri, null);
//                            db.insert("UserLogin_Checking", null, contentValues3);
                        }
                    }
                    if (usertwopass.toString().equals(combine)) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    permissions(),
                                    4);
                            /*if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        4);
                            } else {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        4);
                            }*/
                        } else {
                            wrongpin.setVisibility(View.INVISIBLE);

                            if (account_selection.toString().equals("Dine")) {
                                Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                                if (cursor3.moveToFirst()) {
                                    String lite_pro = cursor3.getString(1);

                                    TextView tv = new TextView(MainActivity.this);
                                    tv.setText(lite_pro);

                                    if (tv.getText().toString().equals("Lite")) {
                                        Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine.class);
                                        startActivity(intent);
                                    }
                                }else {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                    startActivity(intent);
                                }
                            }else {
                                if (account_selection.toString().equals("Qsr")) {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Qsr.class);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Retail.class);
                                    startActivity(intent);
                                }
                            }
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                            getContentResolver().delete(contentUri, null,null);
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("LoginUser")
                                    .appendQueryParameter("operation", "delete")
                                    .appendQueryParameter(null, null)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.delete("LoginUser", null, null);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("username", usertwouser);
                            contentValues.put("password", usertwopass);
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                            resultUri = getContentResolver().insert(contentUri, contentValues);
                            getContentResolver().notifyChange(resultUri, null);
//                            db.insert("LoginUser", null, contentValues);

                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("login_status", "1");
                            String where1 = "_id = '1' ";

                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Home_check");
                            getContentResolver().update(contentUri, contentValues1,where1,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Home_check")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id","1")
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.update("Home_check", contentValues1, where1, new String[]{});
                            Toast.makeText(MainActivity.this, getString(R.string.login_successfull), Toast.LENGTH_SHORT).show();
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                            getContentResolver().delete(contentUri, null,null);
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("UserLogin_Checking")
                                    .appendQueryParameter("operation", "delete")
                                    .appendQueryParameter(null, null)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.delete("UserLogin_Checking", null, null);
                            ContentValues contentValues3 = new ContentValues();
                            contentValues3.put("name", "user2");
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                            resultUri = getContentResolver().insert(contentUri, contentValues3);
                            getContentResolver().notifyChange(resultUri, null);
//                            db.insert("UserLogin_Checking", null, contentValues3);
                        }
                    }
                    if (userthreepass.toString().equals(combine)) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    permissions(),
                                    5);
                            /*if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        5);
                            } else {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        5);
                            }*/
                        } else {
                            wrongpin.setVisibility(View.INVISIBLE);

                            if (account_selection.toString().equals("Dine")) {
                                Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                                if (cursor3.moveToFirst()) {
                                    String lite_pro = cursor3.getString(1);

                                    TextView tv = new TextView(MainActivity.this);
                                    tv.setText(lite_pro);

                                    if (tv.getText().toString().equals("Lite")) {
                                        Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine.class);
                                        startActivity(intent);
                                    }
                                }else {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                    startActivity(intent);
                                }
                            }else {
                                if (account_selection.toString().equals("Qsr")) {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Qsr.class);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Retail.class);
                                    startActivity(intent);
                                }
                            }
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                            getContentResolver().delete(contentUri, null,null);
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("LoginUser")
                                    .appendQueryParameter("operation", "delete")
                                    .appendQueryParameter(null, null)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.delete("LoginUser", null, null);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("username", userthreeuser);
                            contentValues.put("password", userthreepass);
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                            resultUri = getContentResolver().insert(contentUri, contentValues);
                            getContentResolver().notifyChange(resultUri, null);
//                            db.insert("LoginUser", null, contentValues);

                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("login_status", "1");
                            String where1 = "_id = '1' ";
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Home_check");
                            getContentResolver().update(contentUri, contentValues1,where1,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Home_check")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id","1")
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.update("Home_check", contentValues1, where1, new String[]{});
                            Toast.makeText(MainActivity.this, getString(R.string.login_successfull), Toast.LENGTH_SHORT).show();

                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                            getContentResolver().delete(contentUri, null,null);
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("UserLogin_Checking")
                                    .appendQueryParameter("operation", "delete")
                                    .appendQueryParameter(null, null)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.delete("UserLogin_Checking", null, null);
                            ContentValues contentValues3 = new ContentValues();
                            contentValues3.put("name", "user3");
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                            resultUri = getContentResolver().insert(contentUri, contentValues3);
                            getContentResolver().notifyChange(resultUri, null);
//                            db.insert("UserLogin_Checking", null, contentValues3);
                        }
                    }
                    if (userfourpass.toString().equals(combine)) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    permissions(),
                                    6);
                            /*if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        6);
                            } else {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        6);
                            }*/
                        } else {
                            wrongpin.setVisibility(View.INVISIBLE);

                            if (account_selection.toString().equals("Dine")) {
                                Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                                if (cursor3.moveToFirst()) {
                                    String lite_pro = cursor3.getString(1);

                                    TextView tv = new TextView(MainActivity.this);
                                    tv.setText(lite_pro);

                                    if (tv.getText().toString().equals("Lite")) {
                                        Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine.class);
                                        startActivity(intent);
                                    }
                                }else {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                    startActivity(intent);
                                }
                            }else {
                                if (account_selection.toString().equals("Qsr")) {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Qsr.class);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Retail.class);
                                    startActivity(intent);
                                }
                            }
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                            getContentResolver().delete(contentUri, null,null);
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("LoginUser")
                                    .appendQueryParameter("operation", "delete")
                                    .appendQueryParameter(null, null)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.delete("LoginUser", null, null);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("username", userfouruser);
                            contentValues.put("password", userfourpass);
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                            resultUri = getContentResolver().insert(contentUri, contentValues);
                            getContentResolver().notifyChange(resultUri, null);
//                            db.insert("LoginUser", null, contentValues);

                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("login_status", "1");
                            String where1 = "_id = '1' ";

                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Home_check");
                            getContentResolver().update(contentUri, contentValues1,where1,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Home_check")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id","1")
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.update("Home_check", contentValues1, where1, new String[]{});
                            Toast.makeText(MainActivity.this, getString(R.string.login_successfull), Toast.LENGTH_SHORT).show();
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                            getContentResolver().delete(contentUri, null,null);
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("UserLogin_Checking")
                                    .appendQueryParameter("operation", "delete")
                                    .appendQueryParameter(null, null)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.delete("UserLogin_Checking", null, null);
                            ContentValues contentValues3 = new ContentValues();
                            contentValues3.put("name", "user4");
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                            resultUri = getContentResolver().insert(contentUri, contentValues3);
                            getContentResolver().notifyChange(resultUri, null);
//                            db.insert("UserLogin_Checking", null, contentValues3);
                        }
                    }
                    if (userfivepass.toString().equals(combine)) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    permissions(),
                                    7);
                            /*if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        7);
                            } else {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        7);
                            }*/
                        } else {
                            wrongpin.setVisibility(View.INVISIBLE);

                            if (account_selection.toString().equals("Dine")) {
                                Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                                if (cursor3.moveToFirst()) {
                                    String lite_pro = cursor3.getString(1);

                                    TextView tv = new TextView(MainActivity.this);
                                    tv.setText(lite_pro);

                                    if (tv.getText().toString().equals("Lite")) {
                                        Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine.class);
                                        startActivity(intent);
                                    }
                                }else {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                    startActivity(intent);
                                }
                            }else {
                                if (account_selection.toString().equals("Qsr")) {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Qsr.class);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Retail.class);
                                    startActivity(intent);
                                }
                            }
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                            getContentResolver().delete(contentUri, null,null);
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("LoginUser")
                                    .appendQueryParameter("operation", "delete")
                                    .appendQueryParameter(null, null)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.delete("LoginUser", null, null);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("username", userfiveuser);
                            contentValues.put("password", userfivepass);
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                            resultUri = getContentResolver().insert(contentUri, contentValues);
                            getContentResolver().notifyChange(resultUri, null);
//                            db.insert("LoginUser", null, contentValues);

                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("login_status", "1");
                            String where1 = "_id = '1' ";

                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Home_check");
                            getContentResolver().update(contentUri, contentValues1,where1,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Home_check")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id","1")
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.update("Home_check", contentValues1, where1, new String[]{});
                            Toast.makeText(MainActivity.this, getString(R.string.login_successfull), Toast.LENGTH_SHORT).show();
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                            getContentResolver().delete(contentUri, null,null);
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("UserLogin_Checking")
                                    .appendQueryParameter("operation", "delete")
                                    .appendQueryParameter(null, null)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.delete("UserLogin_Checking", null, null);
                            ContentValues contentValues3 = new ContentValues();
                            contentValues3.put("name", "user5");
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                            resultUri = getContentResolver().insert(contentUri, contentValues3);
                            getContentResolver().notifyChange(resultUri, null);
//                            db.insert("UserLogin_Checking", null, contentValues3);
                        }
                    }
                    if (usersixpass.toString().equals(combine)) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    permissions(),
                                    8);
                            /*if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        8);
                            } else {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        8);
                            }*/
                        } else {
                            wrongpin.setVisibility(View.INVISIBLE);

                            if (account_selection.toString().equals("Dine")) {
                                Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                                if (cursor3.moveToFirst()) {
                                    String lite_pro = cursor3.getString(1);

                                    TextView tv = new TextView(MainActivity.this);
                                    tv.setText(lite_pro);

                                    if (tv.getText().toString().equals("Lite")) {
                                        Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine.class);
                                        startActivity(intent);
                                    }
                                }else {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                    startActivity(intent);
                                }
                            }else {
                                if (account_selection.toString().equals("Qsr")) {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Qsr.class);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Retail.class);
                                    startActivity(intent);
                                }
                            }
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                            getContentResolver().delete(contentUri, null,null);
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("LoginUser")
                                    .appendQueryParameter("operation", "delete")
                                    .appendQueryParameter(null, null)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.delete("LoginUser", null, null);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("username", usersixuser);
                            contentValues.put("password", usersixpass);
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                            resultUri = getContentResolver().insert(contentUri, contentValues);
                            getContentResolver().notifyChange(resultUri, null);
//                            db.insert("LoginUser", null, contentValues);

                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("login_status", "1");
                            String where1 = "_id = '1' ";

                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Home_check");
                            getContentResolver().update(contentUri, contentValues1,where1,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Home_check")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id","1")
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.update("Home_check", contentValues1, where1, new String[]{});
                            Toast.makeText(MainActivity.this, getString(R.string.login_successfull), Toast.LENGTH_SHORT).show();
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                            getContentResolver().delete(contentUri, null,null);
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("UserLogin_Checking")
                                    .appendQueryParameter("operation", "delete")
                                    .appendQueryParameter(null, null)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
//                            db.delete("UserLogin_Checking", null, null);
                            ContentValues contentValues3 = new ContentValues();
                            contentValues3.put("name", "user6");
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                            resultUri = getContentResolver().insert(contentUri, contentValues3);
                            getContentResolver().notifyChange(resultUri, null);
//                            db.insert("UserLogin_Checking", null, contentValues3);
                        }
                    }
                    if (box1.toString().equals("") || box2.toString().equals("") || box3.toString().equals("") || box4.toString().equals("")) {
                        wrongpin.setVisibility(View.INVISIBLE);
                    }



                }
                else {
                    wrongpin.setVisibility(View.VISIBLE);
                    Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
                    findViewById(R.id.login_wrapper).startAnimation(shake);
                }
            }
        });



        TextView forgot = (TextView)findViewById(R.id.forgotpassword);
        forgot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        TextView settings_home = (TextView)findViewById(R.id.settings_home);
        settings_home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
        Cursor co31 = db_inapp.rawQuery("SELECT * FROM Pro_upgrade", null);
        int cou31 = co31.getColumnCount();
        if (String.valueOf(cou31).equals("3")) {
            db_inapp.execSQL("ALTER TABLE Pro_upgrade ADD COLUMN pro_expiry");
        }
        co31.close();

        db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
        Cursor cursor1 = db_inapp.rawQuery("SELECT * FROM Pro_upgrade", null);
        if (cursor1.moveToFirst()){
            String st_da = cursor1.getString(3);

            TextView tv_da = new TextView(MainActivity.this);
            tv_da.setText(st_da);

            if (tv_da.getText().toString().equals("")){
                Cursor cursor = db_inapp.rawQuery("SELECT * FROM credentialstime", null);
                if (cursor.moveToFirst()){
                    String id = cursor.getString(0);
                    String todate = cursor.getString(9);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("pro_expiry", todate);
                    String where = "_id = '" + id + "'";
                    db_inapp.update("Pro_upgrade", contentValues, where, new String[]{});
                }
                cursor.close();
            }
        }
        cursor1.close();


        String app_validity = "";
        Cursor cursorzx = db_inapp.rawQuery("SELECT * FROM credentialstime", null);
        if (cursorzx.moveToFirst()) {
            app_validity = cursorzx.getString(9);
        }

        Date dtt_new = new Date();
        SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMdd");
        String time24_new = sdf1t_new.format(dtt_new);
        System.out.println("today date is "+time24_new);

        try {
            Date startDate = sdf1t_new.parse(time24_new);
            Date endDate = sdf1t_new.parse(app_validity);

            long different = endDate.getTime() - startDate.getTime();

            System.out.println("startDate : " + startDate);
            System.out.println("endDate : "+ endDate);
            System.out.println("different : " + different);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            System.out.println("difference is "+elapsedDays);

            if (elapsedDays <= 10) {

                if (elapsedDays < 0) {

                    db.execSQL("DELETE FROM Companydetailss");
                    db.execSQL("DELETE FROM Logo");

                    webservicequery("DELETE FROM Companydetailss");
                    webservicequery("DELETE FROM Logo");

                    ContentValues cv = new ContentValues();
                    cv.put("companyname", "Intuition");
                    cv.put("website", "www.intuitionsoftwares.com");
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Companydetailss");
                    resultUri = getContentResolver().insert(contentUri, cv);
                    getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Companydetailss", null, cv);

                    byte[] img;
                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_ivepos);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    img = bos.toByteArray();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("companylogo", img);
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Logo");
                    resultUri = getContentResolver().insert(contentUri, contentValues);
                    getContentResolver().notifyChange(resultUri, null);
//                    db.insert("Logo", null, contentValues);

                    System.out.println("difference is "+elapsedDays);


                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(this);
                    //Setting message manually and performing action on button click
                    builder.setMessage("Your IVEPOS app license is expired.\nBut you can create upto 1000 bills free. Kindly renew it to enjoy complete features.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle(getString(R.string.title9));
                    alert.show();
                }else {
                    System.out.println("difference is "+elapsedDays);

                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(this);
                    //Setting message manually and performing action on button click
                    builder.setMessage("Your IVEPOS app license is going to expire in "+elapsedDays+" days.\nKindly renew it to enjoy uninterrupted service.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle(getString(R.string.title9));
                    alert.show();
                }


            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    public boolean databaseExistSyncdb() {
        File DATA_DIRECTORY_DATABASE =
                new File(Environment.getDataDirectory() +
                        "/data/" + "com.intuition.ivepos" +
                        "/databases/" + "syncdb");

        return DATA_DIRECTORY_DATABASE.exists();
    }


    public boolean databaseExistSyncdbApp() {
        File DATA_DIRECTORY_DATABASE =
                new File(Environment.getDataDirectory() +
                        "/data/" + "com.intuition.ivepos" +
                        "/databases/" + "syncdbapp");

        return DATA_DIRECTORY_DATABASE.exists();
    }


    private boolean checkDataBase(String DB_FULL_PATH) {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_FULL_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        return checkDB != null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();

//        finish();
//        System.exit(0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (account_selection.toString().equals("Dine")) {
                        Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                        if (cursor3.moveToFirst()) {
                            String lite_pro = cursor3.getString(1);

                            TextView tv = new TextView(MainActivity.this);
                            tv.setText(lite_pro);

                            if (tv.getText().toString().equals("Lite")) {
                                Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine.class);
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                            startActivity(intent);
                        }
                    } else {
                        if (account_selection.toString().equals("Qsr")) {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Qsr.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Retail.class);
                            startActivity(intent);
                        }
                    }

//                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
//                    myEdit.putString("table", "1");
//                    myEdit.apply();

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                    getContentResolver().delete(contentUri, null, null);
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("LoginUser")
                            .appendQueryParameter("operation", "delete")
                            .appendQueryParameter(null, null)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.delete("LoginUser", null, null);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("username", ladminuser);
                    contentValues.put("password", ladminpass);
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                    resultUri = getContentResolver().insert(contentUri, contentValues);
                    getContentResolver().notifyChange(resultUri, null);
//                    db.insert("LoginUser", null, contentValues);

                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("login_status", "1");
                    String where1 = "_id = '1' ";
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Home_check");
                    getContentResolver().update(contentUri, contentValues1, where1, new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Home_check")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id", "1")
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.update("Home_check", contentValues1, where1, new String[]{});
                    Toast.makeText(MainActivity.this, getString(R.string.login_successfull), Toast.LENGTH_SHORT).show();
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                    getContentResolver().delete(contentUri, null, null);
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("UserLogin_Checking")
                            .appendQueryParameter("operation", "delete")
                            .appendQueryParameter(null, null)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.delete("UserLogin_Checking", null, null);
                    ContentValues contentValues3 = new ContentValues();
                    contentValues3.put("name", "ladmin");
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                    resultUri = getContentResolver().insert(contentUri, contentValues3);
                    getContentResolver().notifyChange(resultUri, null);
//                    db.insert("UserLogin_Checking", null, contentValues3);
                } else {
                    Toast.makeText(MainActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (account_selection.toString().equals("Dine")) {
                        Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                        if (cursor3.moveToFirst()) {
                            String lite_pro = cursor3.getString(1);

                            TextView tv = new TextView(MainActivity.this);
                            tv.setText(lite_pro);

                            if (tv.getText().toString().equals("Lite")) {
                                Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine.class);
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                            startActivity(intent);
                        }
                    } else {
                        if (account_selection.toString().equals("Qsr")) {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Qsr.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Retail.class);
                            startActivity(intent);
                        }
                    }

//                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
//                    myEdit.putString("table", "1");
//                    myEdit.apply();

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                    getContentResolver().delete(contentUri, null, null);
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("LoginUser")
                            .appendQueryParameter("operation", "delete")
                            .appendQueryParameter(null, null)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.delete("LoginUser", null, null);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("username", madminuser);
                    contentValues.put("password", madminpass);
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                    resultUri = getContentResolver().insert(contentUri, contentValues);
                    getContentResolver().notifyChange(resultUri, null);
//                    db.insert("LoginUser", null, contentValues);

                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("login_status", "1");
                    String where1 = "_id = '1' ";
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Home_check");
                    getContentResolver().update(contentUri, contentValues1, where1, new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Home_check")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id", "1")
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.update("Home_check", contentValues1, where1, new String[]{});
                    Toast.makeText(MainActivity.this, getString(R.string.login_successfull), Toast.LENGTH_SHORT).show();
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                    getContentResolver().delete(contentUri, null, null);
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("UserLogin_Checking")
                            .appendQueryParameter("operation", "delete")
                            .appendQueryParameter(null, null)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.delete("UserLogin_Checking", null, null);
                    ContentValues contentValues3 = new ContentValues();
                    contentValues3.put("name", "madmin");
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                    resultUri = getContentResolver().insert(contentUri, contentValues3);
                    getContentResolver().notifyChange(resultUri, null);
//                    db.insert("UserLogin_Checking", null, contentValues3);
                } else {
                    Toast.makeText(MainActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case 3: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (account_selection.toString().equals("Dine")) {
                        Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                        if (cursor3.moveToFirst()) {
                            String lite_pro = cursor3.getString(1);

                            TextView tv = new TextView(MainActivity.this);
                            tv.setText(lite_pro);

                            if (tv.getText().toString().equals("Lite")) {
                                Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine.class);
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                            startActivity(intent);
                        }
                    } else {
                        if (account_selection.toString().equals("Qsr")) {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Qsr.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Retail.class);
                            startActivity(intent);
                        }
                    }

//                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
//                    myEdit.putString("table", "1");
//                    myEdit.apply();

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                    getContentResolver().delete(contentUri, null, null);
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("LoginUser")
                            .appendQueryParameter("operation", "delete")
                            .appendQueryParameter(null, null)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.delete("LoginUser", null, null);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("username", useroneuser);
                    contentValues.put("password", useronepass);
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                    resultUri = getContentResolver().insert(contentUri, contentValues);
                    getContentResolver().notifyChange(resultUri, null);
//                    db.insert("LoginUser", null, contentValues);

                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("login_status", "1");
                    String where1 = "_id = '1' ";
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Home_check");
                    getContentResolver().update(contentUri, contentValues1, where1, new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Home_check")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id", "1")
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.update("Home_check", contentValues1, where1, new String[]{});
                    Toast.makeText(MainActivity.this, getString(R.string.login_successfull), Toast.LENGTH_SHORT).show();
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                    getContentResolver().delete(contentUri, null, null);
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("UserLogin_Checking")
                            .appendQueryParameter("operation", "delete")
                            .appendQueryParameter(null, null)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.delete("UserLogin_Checking", null, null);
                    ContentValues contentValues3 = new ContentValues();
                    contentValues3.put("name", "user1");
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                    resultUri = getContentResolver().insert(contentUri, contentValues3);
                    getContentResolver().notifyChange(resultUri, null);
//                    db.insert("UserLogin_Checking", null, contentValues3);
                } else {
                    Toast.makeText(MainActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case 4: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (account_selection.toString().equals("Dine")) {
                        Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                        if (cursor3.moveToFirst()) {
                            String lite_pro = cursor3.getString(1);

                            TextView tv = new TextView(MainActivity.this);
                            tv.setText(lite_pro);

                            if (tv.getText().toString().equals("Lite")) {
                                Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine.class);
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                            startActivity(intent);
                        }
                    } else {
                        if (account_selection.toString().equals("Qsr")) {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Qsr.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Retail.class);
                            startActivity(intent);
                        }
                    }

//                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
//                    myEdit.putString("table", "1");
//                    myEdit.apply();

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                    getContentResolver().delete(contentUri, null, null);
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("LoginUser")
                            .appendQueryParameter("operation", "delete")
                            .appendQueryParameter(null, null)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.delete("LoginUser", null, null);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("username", usertwouser);
                    contentValues.put("password", usertwopass);
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                    resultUri = getContentResolver().insert(contentUri, contentValues);
                    getContentResolver().notifyChange(resultUri, null);
//                    db.insert("LoginUser", null, contentValues);

                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("login_status", "1");
                    String where1 = "_id = '1' ";
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Home_check");
                    getContentResolver().update(contentUri, contentValues1, where1, new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Home_check")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id", "1")
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.update("Home_check", contentValues1, where1, new String[]{});
                    Toast.makeText(MainActivity.this, getString(R.string.login_successfull), Toast.LENGTH_SHORT).show();
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                    getContentResolver().delete(contentUri, null, null);
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("UserLogin_Checking")
                            .appendQueryParameter("operation", "delete")
                            .appendQueryParameter(null, null)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.delete("UserLogin_Checking", null, null);
                    ContentValues contentValues3 = new ContentValues();
                    contentValues3.put("name", "user2");
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                    resultUri = getContentResolver().insert(contentUri, contentValues3);
                    getContentResolver().notifyChange(resultUri, null);
//                    db.insert("UserLogin_Checking", null, contentValues3);
                } else {
                    Toast.makeText(MainActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case 5: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (account_selection.toString().equals("Dine")) {
                        Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                        if (cursor3.moveToFirst()) {
                            String lite_pro = cursor3.getString(1);

                            TextView tv = new TextView(MainActivity.this);
                            tv.setText(lite_pro);

                            if (tv.getText().toString().equals("Lite")) {
                                Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine.class);
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                            startActivity(intent);
                        }
                    } else {
                        if (account_selection.toString().equals("Qsr")) {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Qsr.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Retail.class);
                            startActivity(intent);
                        }
                    }

//                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
//                    myEdit.putString("table", "1");
//                    myEdit.apply();

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                    getContentResolver().delete(contentUri, null, null);
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("LoginUser")
                            .appendQueryParameter("operation", "delete")
                            .appendQueryParameter(null, null)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.delete("LoginUser", null, null);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("username", userthreeuser);
                    contentValues.put("password", userthreepass);
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                    resultUri = getContentResolver().insert(contentUri, contentValues);
                    getContentResolver().notifyChange(resultUri, null);
//                    db.insert("LoginUser", null, contentValues);

                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("login_status", "1");
                    String where1 = "_id = '1' ";
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Home_check");
                    getContentResolver().update(contentUri, contentValues1, where1, new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Home_check")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id", "1")
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.update("Home_check", contentValues1, where1, new String[]{});
                    Toast.makeText(MainActivity.this, getString(R.string.login_successfull), Toast.LENGTH_SHORT).show();
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                    getContentResolver().delete(contentUri, null, null);
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("UserLogin_Checking")
                            .appendQueryParameter("operation", "delete")
                            .appendQueryParameter(null, null)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.delete("UserLogin_Checking", null, null);
                    ContentValues contentValues3 = new ContentValues();
                    contentValues3.put("name", "user3");
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                    resultUri = getContentResolver().insert(contentUri, contentValues3);
                    getContentResolver().notifyChange(resultUri, null);
//                    db.insert("UserLogin_Checking", null, contentValues3);
                } else {
                    Toast.makeText(MainActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case 6: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (account_selection.toString().equals("Dine")) {
                        Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                        if (cursor3.moveToFirst()) {
                            String lite_pro = cursor3.getString(1);

                            TextView tv = new TextView(MainActivity.this);
                            tv.setText(lite_pro);

                            if (tv.getText().toString().equals("Lite")) {
                                Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine.class);
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                            startActivity(intent);
                        }
                    } else {
                        if (account_selection.toString().equals("Qsr")) {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Qsr.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Retail.class);
                            startActivity(intent);
                        }
                    }

//                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
//                    myEdit.putString("table", "1");
//                    myEdit.apply();

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                    getContentResolver().delete(contentUri, null, null);
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("LoginUser")
                            .appendQueryParameter("operation", "delete")
                            .appendQueryParameter(null, null)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.delete("LoginUser", null, null);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("username", userfouruser);
                    contentValues.put("password", userfourpass);
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                    resultUri = getContentResolver().insert(contentUri, contentValues);
                    getContentResolver().notifyChange(resultUri, null);
//                    db.insert("LoginUser", null, contentValues);

                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("login_status", "1");
                    String where1 = "_id = '1' ";
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Home_check");
                    getContentResolver().update(contentUri, contentValues1, where1, new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Home_check")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id", "1")
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.update("Home_check", contentValues1, where1, new String[]{});
                    Toast.makeText(MainActivity.this, getString(R.string.login_successfull), Toast.LENGTH_SHORT).show();
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                    getContentResolver().delete(contentUri, null, null);
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("UserLogin_Checking")
                            .appendQueryParameter("operation", "delete")
                            .appendQueryParameter(null, null)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.delete("UserLogin_Checking", null, null);
                    ContentValues contentValues3 = new ContentValues();
                    contentValues3.put("name", "user4");
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                    resultUri = getContentResolver().insert(contentUri, contentValues3);
                    getContentResolver().notifyChange(resultUri, null);
//                    db.insert("UserLogin_Checking", null, contentValues3);
                } else {
                    Toast.makeText(MainActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case 7: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (account_selection.toString().equals("Dine")) {
                        Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                        if (cursor3.moveToFirst()) {
                            String lite_pro = cursor3.getString(1);

                            TextView tv = new TextView(MainActivity.this);
                            tv.setText(lite_pro);

                            if (tv.getText().toString().equals("Lite")) {
                                Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine.class);
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                            startActivity(intent);
                        }
                    } else {
                        if (account_selection.toString().equals("Qsr")) {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Qsr.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Retail.class);
                            startActivity(intent);
                        }
                    }

//                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
//                    myEdit.putString("table", "1");
//                    myEdit.apply();

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                    getContentResolver().delete(contentUri, null, null);
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("LoginUser")
                            .appendQueryParameter("operation", "delete")
                            .appendQueryParameter(null, null)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.delete("LoginUser", null, null);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("username", userfiveuser);
                    contentValues.put("password", userfivepass);
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                    resultUri = getContentResolver().insert(contentUri, contentValues);
                    getContentResolver().notifyChange(resultUri, null);
//                    db.insert("LoginUser", null, contentValues);

                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("login_status", "1");
                    String where1 = "_id = '1' ";
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Home_check");
                    getContentResolver().update(contentUri, contentValues1, where1, new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Home_check")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id", "1")
                            .build();
                    getContentResolver().notifyChange(resultUri, null);

//                    db.update("Home_check", contentValues1, where1, new String[]{});
                    Toast.makeText(MainActivity.this, getString(R.string.login_successfull), Toast.LENGTH_SHORT).show();
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                    getContentResolver().delete(contentUri, null, null);
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("UserLogin_Checking")
                            .appendQueryParameter("operation", "delete")
                            .appendQueryParameter(null, null)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.delete("UserLogin_Checking", null, null);
                    ContentValues contentValues3 = new ContentValues();
                    contentValues3.put("name", "user5");
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                    resultUri = getContentResolver().insert(contentUri, contentValues3);
                    getContentResolver().notifyChange(resultUri, null);
//                    db.insert("UserLogin_Checking", null, contentValues3);
                } else {
                    Toast.makeText(MainActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case 8: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (account_selection.toString().equals("Dine")) {
                        Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                        if (cursor3.moveToFirst()) {
                            String lite_pro = cursor3.getString(1);

                            TextView tv = new TextView(MainActivity.this);
                            tv.setText(lite_pro);

                            if (tv.getText().toString().equals("Lite")) {
                                Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine.class);
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Dine_l.class);
                            startActivity(intent);
                        }
                    } else {
                        if (account_selection.toString().equals("Qsr")) {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Qsr.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(MainActivity.this, BeveragesMenuFragment_Retail.class);
                            startActivity(intent);
                        }
                    }

//                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
//                    myEdit.putString("table", "1");
//                    myEdit.apply();

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                    getContentResolver().delete(contentUri, null, null);
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("LoginUser")
                            .appendQueryParameter("operation", "delete")
                            .appendQueryParameter(null, null)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.delete("LoginUser", null, null);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("username", usersixuser);
                    contentValues.put("password", usersixpass);
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "LoginUser");
                    resultUri = getContentResolver().insert(contentUri, contentValues);
                    getContentResolver().notifyChange(resultUri, null);
//                    db.insert("LoginUser", null, contentValues);

                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("login_status", "1");
                    String where1 = "_id = '1' ";
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Home_check");
                    getContentResolver().update(contentUri, contentValues1, where1, new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Home_check")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id", "1")
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.update("Home_check", contentValues1, where1, new String[]{});
                    Toast.makeText(MainActivity.this, getString(R.string.login_successfull), Toast.LENGTH_SHORT).show();

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                    getContentResolver().delete(contentUri, null, null);
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("UserLogin_Checking")
                            .appendQueryParameter("operation", "delete")
                            .appendQueryParameter(null, null)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.delete("UserLogin_Checking", null, null);
                    ContentValues contentValues3 = new ContentValues();
                    contentValues3.put("name", "user6");
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "UserLogin_Checking");
                    resultUri = getContentResolver().insert(contentUri, contentValues3);
                    getContentResolver().notifyChange(resultUri, null);
//                    db.insert("UserLogin_Checking", null, contentValues3);
                } else {
                    Toast.makeText(MainActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case 9: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                    if (mBluetoothAdapter == null) {

                    } else {
                        if (null == mBluetoothAdapter) {
                            finish();

                        }

                        if (!mBluetoothAdapter.isEnabled()) {
                            if (mBluetoothAdapter.enable()) {
                                while (!mBluetoothAdapter.isEnabled())
                                    ;
                                Log.v(TAG, "Enable BluetoothAdapter");
                            } else {
                                finish();

                            }
                        }

                        mBluetoothAdapter.cancelDiscovery();
                        mBluetoothAdapter.startDiscovery();

                    }

                } else {
                    Toast.makeText(MainActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    public void firebaseRegistration(){

        if(isDeviceOnline()){
            Intent intent=new Intent(MainActivity.this,MyService.class);
            startService(intent);
        }

        SharedPreferences prefs = getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("signindb", true);

        SharedPreferenceManager.getInstance().setDataDownloadValue(true);

        initApp();



    }
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }



    public void check_server() {

        SharedPreferences sharedpreferences=getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        company= sharedpreferences.getString("companyname", null);
        store= sharedpreferences.getString("storename", null);
        device= sharedpreferences.getString("devicename", null);
        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"check_server.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")){

//                            Toast.makeText(MainActivity.this, "table is there", Toast.LENGTH_SHORT).show();
                            new check_server1().execute();


                        }else{
//                            Toast.makeText(MainActivity.this, "table is not there", Toast.LENGTH_SHORT).show();
                            check_server_all_sales_empty();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm3", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put("company", company+ "");
                params.put("store", store+ "");
                params.put("device", device);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

    }


    public void check_server_all_sales_empty() {

        SharedPreferences sharedpreferences=getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        company= sharedpreferences.getString("companyname", null);
        store= sharedpreferences.getString("storename", null);
        device= sharedpreferences.getString("devicename", null);
        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"check_server_all_sales.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")){

                            Toast.makeText(MainActivity.this, "all sales is not empty", Toast.LENGTH_SHORT).show();



                        }else{
                            Toast.makeText(MainActivity.this, "all sales is empty", Toast.LENGTH_SHORT).show();
                            new check_server1().execute();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm4", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put("company", company+ "");
                params.put("store", store+ "");
                params.put("device", device);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

    }


    private class check_server1 extends AsyncTask<Void, Void, List<String>> {
        private ProgressDialog dialog = new ProgressDialog(MainActivity.this, R.style.timepicker_date_dialog);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage(getString(R.string.setmessage3));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            dialog.show();
        }

        @Override
        protected List<String> doInBackground(Void... voids) {



            File dbFile1 =  new File(Environment.getDataDirectory() +
                    "/data/" + PACKAGE_NAME +
                    "/databases/" + "mydb_Appdata" );
            String filename1 = "mydb_Appdata";

//            File exportDir1 = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
            File exportDir1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
            if (!exportDir1.exists()) {
                exportDir1.mkdirs();
            }

            File file1 = new File(exportDir1, filename1);

            if (!exportDir1.exists()) {
                exportDir1.mkdirs();
            }

            try {
                copyFile(dbFile1, file1);
                Log.e("1", "111");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("2", "22");
            }

            File dbFile = new File(Environment.getDataDirectory() +
                    "/data/" + PACKAGE_NAME +
                    "/databases/" + "mydb_Salesdata" );
            String filename = "mydb_Salesdata";

//            File exportDir = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
            File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            File file = new File(exportDir, filename);

            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            try {
//                    file.createNewFile();
                copyFile(dbFile, file);
                Log.e("1", "1111");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("2", "22");
            }

            createappdata();
            createsalesdata();




            return null;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            dialog.dismiss();

            DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
            downloadMusicfromInternet.execute();

        }
    }

    private static void copyFile(File src, File dst) throws IOException {
        Log.e("1", "111hi1");
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        Log.e("1", "111hi2");
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
            Log.e("1", "111hi3");
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
            Log.e("1", "111hi4");
        }
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
                        Log.d("Signup confirm5", "Error: " + error.getMessage());
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
                        Log.d("Signup confirm6", "Error: " + error.getMessage());
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

    class DownloadMusicfromInternet extends AsyncTask<String, Void, Integer> {
        private ProgressDialog dialog = new ProgressDialog(MainActivity.this, R.style.timepicker_date_dialog);

        @Override
        protected Integer doInBackground(String... params) {

            try {
//                File sd = Environment.getExternalStorageDirectory();
                File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File data = Environment.getDataDirectory();

                if (sd.canWrite()) {
                    String currentDBPath = "//data//" + "com.intuition.ivepos"
                            + "//databases//" + "mydb_Appdata";
                    String backupDBPath = "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1 + "/" + "mydb_Appdata";


                    File file = new File("/data/data/com.intuition.ivepos/databases/mydb_Appdata");
                    if(file.exists()){

                    }
//                    if (DATA_DIRECTORY_DATABASE.exists()){
//
//                    }else {
//
//                    }

                    File backupDB = new File(data, currentDBPath);
                    File currentDB = new File(sd, backupDBPath);

                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();

                }
            } catch (Exception e) {

                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG)
                        .show();

            }

            try {
//                File sd = Environment.getExternalStorageDirectory();
                File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File data = Environment.getDataDirectory();

                if (sd.canWrite()) {
                    String currentDBPath = "//data//" + "com.intuition.ivepos"
                            + "//databases//" + "mydb_Salesdata";
                    String backupDBPath = "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1 + "/" + "mydb_Salesdata";


                    File file = new File("/data/data/com.intuition.ivepos/databases/mydb_Salesdata");
                    if(file.exists()){

                    }
//                    if (DATA_DIRECTORY_DATABASE.exists()){
//
//                    }else {
//
//                    }

                    File backupDB = new File(data, currentDBPath);
                    File currentDB = new File(sd, backupDBPath);

                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();

                }
            } catch (Exception e) {

                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG)
                        .show();

            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage(getString(R.string.setmessage2));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            dialog.show();
        }

        @Override
        protected void onPostExecute(Integer file_url) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();





            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    dialog.dismiss();

                    service(dialog);

                }
            }, 10000); //3000 L = 3 detik

            webservicequery("DROP TABLE IF EXISTS warning");
            webservicequery_sales("DROP TABLE IF EXISTS warning");


        }

    }

    public void service(final ProgressDialog dialog) {
        Intent intent = new Intent(MainActivity.this, SyncHelperService.class);
        intent.putExtra("backup","both");
        startService(intent);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 10000*3); //3000 L = 3 detik
    }

    public void webservicequery_sales(final String webserviceQuery){


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(MainActivity.this);
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
        RequestQueue queue;
        StringRequest sr1;
        // queue = Volley.newRequestQueue(getActivity());

        queue= RequestSingleton.getInstance(MainActivity.this).getInstance();

        sr1 = new StringRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl + "webservicequery_sales.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm7", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                               /*     params.put("email", email + "");
                                    params.put("password", password + "");*/


//                            final String email = prefs.getString("emailid", "");
//                            final String pwd = prefs.getString("password", "");
                params.put("device", device);
                params.put("store", store);
                params.put("company", company);
                params.put("data", webserviceQuery);
                return params;
            }
        };
    /*    sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        sr1.setRetryPolicy(new DefaultRetryPolicy(0, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr1);
    }

    public void webservicequery(final String webserviceQuery){


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(MainActivity.this);
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
        RequestQueue queue;
        StringRequest sr1;
        // queue = Volley.newRequestQueue(getActivity());

        queue= RequestSingleton.getInstance(MainActivity.this).getInstance();

        sr1 = new StringRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl + "webservicequery.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm8", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                               /*     params.put("email", email + "");
                                    params.put("password", password + "");*/


//                            final String email = prefs.getString("emailid", "");
//                            final String pwd = prefs.getString("password", "");
                params.put("device", device);
                params.put("store", store);
                params.put("company", company);
                params.put("data", webserviceQuery);
                return params;
            }
        };
    /*    sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        sr1.setRetryPolicy(new DefaultRetryPolicy(0, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr1);
    }


}
