package com.intuition.ivepos;

import static com.intuition.ivepos.Constants.base64EncodedPublicKey;
import static com.intuition.ivepos.Constants_Inventory.SKU_DELAROY_PRO_UPGRADE;
import static com.intuition.ivepos.Constants_Inventory.SKU_DELAROY_PRO_UPGRADE_DEMO;
import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.intuition.ivepos.languagelocale.LocaleUtils;
import com.intuition.ivepos.syncapp.StubProviderApp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MenuActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    Uri contentUri,resultUri;
    SQLiteDatabase db = null;
    SQLiteDatabase db_inapp = null;
    static final String TAG = "MainActivity";
    private static final String SUFFIX = "/";
    int count;
    String checking;
    String payload, bucketName;
    int state;
    int i;
    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;

    SQLiteDatabase db_subs = null;

    private Context context;

    String textcompanyname, storeitem, deviceitem, email, from, vaild_app1;
    String validity = "00000000";
    TextView textvaluecount;
    String value;
    int count1 = 0;
    int count_inside1 = 0;

    TextView valid_app, cloud_buy, valid;

    ProgressBar progressBar;
    ScrollView scroll;

    public static final String PACKAGE_NAME = "com.intuition.ivepos";
    IabHelper mHelperPro;
    String mSelectedProSubscription="";
    String company,store, device;
    private static int REQ_CODE = 1000;
    RequestQueue requestQueue;

    String currentDateandTime1_pro;
    int i_pro_sel;
    Dialog dialog_pro;

    String WebserviceUrl;

    String date;
    int intdate;
    String da;

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
    protected void onCreate(Bundle savedInstanceState) {

        LocaleUtils.initialize(this, LocaleUtils.getSelectedLanguageId(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menus_landscape);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(MenuActivity.this);
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

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle((R.string.app_name));
        db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
        db_inapp.execSQL("CREATE TABLE if not exists Pro_upgrade (_id integer PRIMARY KEY UNIQUE, status text, orderid text);");
        db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
        Cursor co31 = db_inapp.rawQuery("SELECT * FROM Pro_upgrade", null);
        int cou31 = co31.getColumnCount();
        if (String.valueOf(cou31).equals("3")) {
            db_inapp.execSQL("ALTER TABLE Pro_upgrade ADD COLUMN pro_expiry");
        }
        co31.close();
        Log.d(TAG, "Starting setup.");

        Cursor cursor1_1 = db_inapp.rawQuery("SELECT * FROM Pro_upgrade", null);
        if (cursor1_1.moveToFirst()){

        }else {
            ContentValues cv = new ContentValues();
            cv.put("status", "Not Activated");
            db_inapp.insert("Pro_upgrade", null, cv);
        }
        cursor1_1.close();

        progressBar = (ProgressBar) findViewById(R.id.proceed_button);


        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        final String currentDateandTime1 = sdf2.format(new Date());

        Cursor cursor1 = db_inapp.rawQuery("SELECT * FROM credentialstime", null);
        if (cursor1.moveToFirst()) {
            date = cursor1.getString(9);//22mar2018   }
        }
        cursor1.close();

        da = date; //yyyymmdd
        intdate = Integer.parseInt(currentDateandTime1);


        mHelperPro = new IabHelper(this,  base64EncodedPublicKey);
        mHelperPro.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result)
            {
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app Billing setup failed: " +
                            result);
                } else {
                    Log.d(TAG, "In-app Billing is set up OK");
                    try {
                        mHelperPro.queryInventoryAsync(mReceivedInventoryListenerPro);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                    }
                }

            }
        });


        SimpleDateFormat sdf2_pro = new SimpleDateFormat("yyyyMMdd");
        currentDateandTime1_pro = sdf2_pro.format(new Date());

    }





    public void credentialsProvider() {

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-south-1:b52ad317-b530-427e-9a1e-f602db8bf35a", // Identity pool ID
                Regions.AP_SOUTH_1 // Region
        );

    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListenerPro
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {


            if (result.isFailure()) {
                // Handle failure
            } else {

                if(mSelectedProSubscription.equalsIgnoreCase("")){

                    List<Purchase> list=inventory.getAllPurchases();

                    for(int k=0;k<list.size();k++){
                        Purchase mPurchase = list.get(k);
                        if (mPurchase != null && verifyDeveloperPayload(mPurchase)) {
                            Log.d(TAG, "We have gas. Consuming it.");
                            String token=mPurchase.getSku();
                            if((token.equalsIgnoreCase(SKU_DELAROY_PRO_UPGRADE))){

                                try {
                                    mHelperPro.consumeAsync(mPurchase,
                                            mConsumeFinishedListenerPro);
                                } catch (IabHelper.IabAsyncInProgressException e) {
                                    e.printStackTrace();
                                }

                            }

                            return;
                        }

                    }


                }else{
                    Purchase purchase = inventory.getPurchase(mSelectedProSubscription);
                    checking=purchase.getOrderId();
                    try {
                        mHelperPro.consumeAsync(inventory.getPurchase(mSelectedProSubscription),
                                mConsumeFinishedListenerPro);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                    }
                }


            }
        }
    };


    IabHelper.OnConsumeFinishedListener mConsumeFinishedListenerPro =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {

                        if(mSelectedProSubscription.equalsIgnoreCase("")){

                        }else{
                            String orderid=purchase.getOrderId();
                            updateProCloud(orderid);
                        }


                    } else {
                        // handle error
                    }
                }
            };


    private void updateProCloud(String orderid) {

        db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
        Cursor cursor_pro=db_inapp.rawQuery("SELECT * FROM Pro_upgrade", null);
        if(cursor_pro.moveToFirst()){

            int id=cursor_pro.getInt(0);
            String status=cursor_pro.getString(1);
            if(!status.equalsIgnoreCase("Activated")){
                ContentValues contentValues =new ContentValues();
                contentValues.put("status","Activated");
                contentValues.put("orderid",orderid);
                if (i_pro_sel == 1){
                    checking="123";
                    mSelectedProSubscription = SKU_DELAROY_PRO_UPGRADE_DEMO;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                    Calendar currentCal = Calendar.getInstance();
                    String currentdate = dateFormat.format(currentCal.getTime());
                    currentCal.add(Calendar.DATE, 7);
                    String toDate = dateFormat.format(currentCal.getTime());
                    contentValues.put("pro_expiry",toDate);
                }else {
                    if (i_pro_sel == 2){
                        checking="123";
                        mSelectedProSubscription = SKU_DELAROY_PRO_UPGRADE;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                        Calendar currentCal = Calendar.getInstance();
                        String currentdate = dateFormat.format(currentCal.getTime());
                        currentCal.add(Calendar.DATE, 365);
                        String toDate = dateFormat.format(currentCal.getTime());
                        contentValues.put("pro_expiry",toDate);
                    }else {
                        checking=orderid;
                        mSelectedProSubscription = SKU_DELAROY_PRO_UPGRADE;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                        Calendar currentCal = Calendar.getInstance();
                        String currentdate = dateFormat.format(currentCal.getTime());
                        currentCal.add(Calendar.DATE, 365);
                        String toDate = dateFormat.format(currentCal.getTime());
                        contentValues.put("pro_expiry",toDate);
                    }
                }
                String where = "_id = '" + id + "'";
                db_inapp.update("Pro_upgrade", contentValues, where, new String[]{});
            }

        }else{

            ContentValues contentValues =new ContentValues();
            contentValues.put("status","Activated");
            contentValues.put("orderid",orderid);
            if (i_pro_sel == 1){
                checking="123";
                mSelectedProSubscription = SKU_DELAROY_PRO_UPGRADE_DEMO;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                Calendar currentCal = Calendar.getInstance();
                String currentdate = dateFormat.format(currentCal.getTime());
                currentCal.add(Calendar.DATE, 7);
                String toDate = dateFormat.format(currentCal.getTime());
                contentValues.put("pro_expiry",toDate);
            }else {
                if (i_pro_sel == 2){
                    checking="123";
                    mSelectedProSubscription = SKU_DELAROY_PRO_UPGRADE;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                    Calendar currentCal = Calendar.getInstance();
                    String currentdate = dateFormat.format(currentCal.getTime());
                    currentCal.add(Calendar.DATE, 365);
                    String toDate = dateFormat.format(currentCal.getTime());
                    contentValues.put("pro_expiry",toDate);
                }else {
                    checking=orderid;
                    mSelectedProSubscription = SKU_DELAROY_PRO_UPGRADE;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                    Calendar currentCal = Calendar.getInstance();
                    String currentdate = dateFormat.format(currentCal.getTime());
                    currentCal.add(Calendar.DATE, 365);
                    String toDate = dateFormat.format(currentCal.getTime());
                    contentValues.put("pro_expiry",toDate);
                }
            }
            db_inapp.insert("Pro_upgrade", null, contentValues);
        }
        cursor_pro.close();

        uploadPro();
    }


    private void uploadPro() {
        if (i_pro_sel == 3){
            RequestQueue requestQueue;
            requestQueue = Volley.newRequestQueue(this);
            StringRequest sr = new StringRequest(
                    Request.Method.POST,
                    WebserviceUrl+"coupon_pr.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("success")) {
                                prolicense(checking);

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
                    params.put("coupon", checking+ "");
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(sr);
        }else {
            prolicense(checking);
        }
    }


    public void prolicense(final String orderid){

        SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        final String company= sharedpreferences.getString("companyname", null);
        final String store= sharedpreferences.getString("storename", null);
        final String device= sharedpreferences.getString("devicename", null);

        progressBar.setVisibility(View.VISIBLE);
        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"propurchase.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")){

                            progressBar.setVisibility(View.GONE);
                            updateUi();

                            dialog_pro.dismiss();

                        }else{

                            progressBar.setVisibility(View.GONE);
                            updateUi();

                            dialog_pro.dismiss();
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
                params.put("orderid",orderid);
                params.put("subscription",mSelectedProSubscription);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void gotoAdmin(View v) {

//        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);

        if (intdate <= Integer.parseInt(da)) {
            ContentValues contentValueses = new ContentValues();
            contentValueses.put("Status", "no");
            String where = "_id = '1'";

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Menulogin_checking");
            getContentResolver().update(contentUri, contentValueses, where, new String[]{});
            resultUri = new Uri.Builder()
                    .scheme("content")
                    .authority(StubProviderApp.AUTHORITY)
                    .path("Menulogin_checking")
                    .appendQueryParameter("operation", "update")
                    .appendQueryParameter("_id", "1")
                    .build();
            getContentResolver().notifyChange(resultUri, null);

//        db.update("Menulogin_checking", contentValueses, where, new String[]{});
            Intent intent = new Intent(this, MultiFragAdminActivity.class);
            startActivity(intent);
        }else {
            android.app.AlertDialog.Builder builder;
            builder = new android.app.AlertDialog.Builder(this);
            //Setting message manually and performing action on button click
            builder.setMessage("Your IVEPOS app license is expired.\nBut you can create upto 1000 bills free. Kindly renew it to enjoy complete features.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            //Creating dialog box
            android.app.AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle(getString(R.string.title9));
            alert.show();
        }

    }

    public void gotoUser(View v) {
//        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);

        if (intdate <= Integer.parseInt(da)) {
            ContentValues contentValueses = new ContentValues();
            contentValueses.put("Status", "no");
            String where = "_id = '1'";
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Menulogin_checking");
            getContentResolver().update(contentUri, contentValueses, where, new String[]{});
            resultUri = new Uri.Builder()
                    .scheme("content")
                    .authority(StubProviderApp.AUTHORITY)
                    .path("Menulogin_checking")
                    .appendQueryParameter("operation", "update")
                    .appendQueryParameter("_id", "1")
                    .build();
            getContentResolver().notifyChange(resultUri, null);
//        db.update("Menulogin_checking", contentValueses, where, new String[]{});

            Intent intent = new Intent(this, MultiFragUserActivity.class);
            startActivity(intent);
        }else {
            android.app.AlertDialog.Builder builder;
            builder = new android.app.AlertDialog.Builder(this);
            //Setting message manually and performing action on button click
            builder.setMessage("Your IVEPOS app license is expired.\nBut you can create upto 1000 bills free. Kindly renew it to enjoy complete features.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            //Creating dialog box
            android.app.AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle(getString(R.string.title9));
            alert.show();
        }

    }

    public void gotoDatabase(View v) {
//        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        ContentValues contentValueses = new ContentValues();
        contentValueses.put("Status", "no");
        String where = "_id = '1'";
        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Menulogin_checking");
        getContentResolver().update(contentUri, contentValueses,where,new String[]{});
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("Menulogin_checking")
                .appendQueryParameter("operation", "update")
                .appendQueryParameter("_id", "1")
                .build();
        getContentResolver().notifyChange(resultUri, null);
//        db.update("Menulogin_checking", contentValueses, where, new String[]{});

        Intent intent = new Intent(this, MultiFragDatabaseActivity.class);
        startActivity(intent);

    }

    public void gotoOrderlist(View v) {
        if (intdate <= Integer.parseInt(da)) {
            if (ContextCompat.checkSelfPermission(MenuActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MenuActivity.this,
                        permissions(),
                        2);
               /* // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(MenuActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

//                        Toast.makeText(ForgotPasswordActivity.this, "111111111", Toast.LENGTH_SHORT).show();

                    ActivityCompat.requestPermissions(MenuActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            2);
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
//                        Toast.makeText(ForgotPasswordActivity.this, "no permission", Toast.LENGTH_SHORT).show();

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(MenuActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            2);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }*/
            } else {
//                    Toast.makeText(ForgotPasswordActivity.this, "hiiii", Toast.LENGTH_SHORT).show();

                if (!SdIsPresent()) ;


                ContentValues contentValueses = new ContentValues();
                contentValueses.put("Status", "no");
                String where = "_id = '1'";
                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Menulogin_checking");
                getContentResolver().update(contentUri, contentValueses, where, new String[]{});
                resultUri = new Uri.Builder()
                        .scheme("content")
                        .authority(StubProviderApp.AUTHORITY)
                        .path("Menulogin_checking")
                        .appendQueryParameter("operation", "update")
                        .appendQueryParameter("_id", "1")
                        .build();
                getContentResolver().notifyChange(resultUri, null);
//            db.update("Menulogin_checking", contentValueses, where, new String[]{});

                Intent intent = new Intent(this, MultiFragOrderlistActivity.class);
                startActivity(intent);
            }
        }else {
            android.app.AlertDialog.Builder builder;
            builder = new android.app.AlertDialog.Builder(this);
            //Setting message manually and performing action on button click
            builder.setMessage("Your IVEPOS app license is expired.\nBut you can create upto 1000 bills free. Kindly renew it to enjoy complete features.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            //Creating dialog box
            android.app.AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle(getString(R.string.title9));
            alert.show();
        }



    }

    public void gotoPreference(View v) {

//        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        if (intdate <= Integer.parseInt(da)) {
            ContentValues contentValueses = new ContentValues();
            contentValueses.put("Status", "no");
            String where = "_id = '1'";
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Menulogin_checking");
            getContentResolver().update(contentUri, contentValueses, where, new String[]{});
            resultUri = new Uri.Builder()
                    .scheme("content")
                    .authority(StubProviderApp.AUTHORITY)
                    .path("Menulogin_checking")
                    .appendQueryParameter("operation", "update")
                    .appendQueryParameter("_id", "1")
                    .build();
            getContentResolver().notifyChange(resultUri, null);
//        db.update("Menulogin_checking", contentValueses, where, new String[]{});

            Intent intent = new Intent(this, MultiFragPreferenceActivity.class);
            startActivity(intent);
        }else {
            android.app.AlertDialog.Builder builder;
            builder = new android.app.AlertDialog.Builder(this);
            //Setting message manually and performing action on button click
            builder.setMessage("Your IVEPOS app license is expired.\nBut you can create upto 1000 bills free. Kindly renew it to enjoy complete features.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            //Creating dialog box
            android.app.AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle(getString(R.string.title9));
            alert.show();
        }

    }



    public void gotoBackup(View v) {

        if (intdate <= Integer.parseInt(da)) {
            if (ContextCompat.checkSelfPermission(MenuActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MenuActivity.this,
                        permissions(),
                        1);
                /*// Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(MenuActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

//                        Toast.makeText(ForgotPasswordActivity.this, "111111111", Toast.LENGTH_SHORT).show();

                    ActivityCompat.requestPermissions(MenuActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
//                        Toast.makeText(ForgotPasswordActivity.this, "no permission", Toast.LENGTH_SHORT).show();

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(MenuActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }*/
            } else {
//                    Toast.makeText(ForgotPasswordActivity.this, "hiiii", Toast.LENGTH_SHORT).show();

                if (!SdIsPresent()) ;

//            db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                ContentValues contentValueses = new ContentValues();
                contentValueses.put("Status", "no");
                String where = "_id = '1'";
                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Menulogin_checking");
                getContentResolver().update(contentUri, contentValueses, where, new String[]{});
                resultUri = new Uri.Builder()
                        .scheme("content")
                        .authority(StubProviderApp.AUTHORITY)
                        .path("Menulogin_checking")
                        .appendQueryParameter("operation", "update")
                        .appendQueryParameter("_id", "1")
                        .build();
                getContentResolver().notifyChange(resultUri, null);
//            db.update("Menulogin_checking", contentValueses, where, new String[]{});

                Intent intent = new Intent(this, MultiFragBackupActivity.class);
                startActivity(intent);
            }
        }else {
            android.app.AlertDialog.Builder builder;
            builder = new android.app.AlertDialog.Builder(this);
            //Setting message manually and performing action on button click
            builder.setMessage("Your IVEPOS app license is expired.\nBut you can create upto 1000 bills free. Kindly renew it to enjoy complete features.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            //Creating dialog box
            android.app.AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle(getString(R.string.title9));
            alert.show();
        }




    }

    public void gotoCustomer(View v) {

        if (intdate <= Integer.parseInt(da)) {
            if (ContextCompat.checkSelfPermission(MenuActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MenuActivity.this,
                        permissions(),
                        3);
               /* // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(MenuActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

//                        Toast.makeText(ForgotPasswordActivity.this, "111111111", Toast.LENGTH_SHORT).show();

                    ActivityCompat.requestPermissions(MenuActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            3);
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
//                        Toast.makeText(ForgotPasswordActivity.this, "no permission", Toast.LENGTH_SHORT).show();

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(MenuActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            3);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }*/
            } else {
//                    Toast.makeText(ForgotPasswordActivity.this, "hiiii", Toast.LENGTH_SHORT).show();

                if (!SdIsPresent()) ;

//            db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                ContentValues contentValueses = new ContentValues();
                contentValueses.put("Status", "no");
                String where = "_id = '1'";
                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Menulogin_checking");
                getContentResolver().update(contentUri, contentValueses, where, new String[]{});
                resultUri = new Uri.Builder()
                        .scheme("content")
                        .authority(StubProviderApp.AUTHORITY)
                        .path("Menulogin_checking")
                        .appendQueryParameter("operation", "update")
                        .appendQueryParameter("_id", "1")
                        .build();
                getContentResolver().notifyChange(resultUri, null);

//            db.update("Menulogin_checking", contentValueses, where, new String[]{});

                Intent intent = new Intent(this, MultiFragCustomerActivity.class);
                startActivity(intent);
            }
        }else {
            android.app.AlertDialog.Builder builder;
            builder = new android.app.AlertDialog.Builder(this);
            //Setting message manually and performing action on button click
            builder.setMessage("Your IVEPOS app license is expired.\nBut you can create upto 1000 bills free. Kindly renew it to enjoy complete features.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            //Creating dialog box
            android.app.AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle(getString(R.string.title9));
            alert.show();
        }


    }

    public void gotoIngredients(View v) {

        if (intdate <= Integer.parseInt(da)) {
            if (databaseExist()) {
                db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);


                Cursor cursor1_1 = db_inapp.rawQuery("SELECT * FROM Pro_upgrade", null);
                if (cursor1_1.moveToFirst()) {

                } else {
                    ContentValues cv = new ContentValues();
                    cv.put("status", "Not Activated");
                    db_inapp.insert("Pro_upgrade", null, cv);
                }
                cursor1_1.close();

                SimpleDateFormat sdf2_pro = new SimpleDateFormat("yyyyMMdd");
                String currentDateandTime1_pro = sdf2_pro.format(new Date());

                Cursor cursor1 = db_inapp.rawQuery("SELECT * FROM Pro_upgrade", null);
                if (cursor1.moveToFirst()) {
                    String st = cursor1.getString(1);
                    String st_da = cursor1.getString(3);

                    TextView tv = new TextView(MenuActivity.this);
                    tv.setText(st);

                    TextView tv_da = new TextView(MenuActivity.this);
                    tv_da.setText(st_da);

                    if (tv.getText().toString().equals("Activated")) {
                        if (Integer.parseInt(tv_da.getText().toString()) > Integer.parseInt(currentDateandTime1_pro)) {
                            if (ContextCompat.checkSelfPermission(MenuActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(MenuActivity.this,
                                        permissions(),
                                        4);
                               /* // Should we show an explanation?
                                if (ActivityCompat.shouldShowRequestPermissionRationale(MenuActivity.this,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

//                        Toast.makeText(ForgotPasswordActivity.this, "111111111", Toast.LENGTH_SHORT).show();

                                    ActivityCompat.requestPermissions(MenuActivity.this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            4);
                                    // Show an explanation to the user *asynchronously* -- don't block
                                    // this thread waiting for the user's response! After the user
                                    // sees the explanation, try again to request the permission.
//                        Toast.makeText(ForgotPasswordActivity.this, "no permission", Toast.LENGTH_SHORT).show();

                                } else {

                                    // No explanation needed, we can request the permission.

                                    ActivityCompat.requestPermissions(MenuActivity.this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            4);

                                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                    // app-defined int constant. The callback method gets the
                                    // result of the request.
                                }*/
                            } else {
//                    Toast.makeText(ForgotPasswordActivity.this, "hiiii", Toast.LENGTH_SHORT).show();

                                if (!SdIsPresent()) ;

//                            db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                ContentValues contentValueses = new ContentValues();
                                contentValueses.put("Status", "no");
                                String where = "_id = '1'";
                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Menulogin_checking");
                                getContentResolver().update(contentUri, contentValueses, where, new String[]{});
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProviderApp.AUTHORITY)
                                        .path("Menulogin_checking")
                                        .appendQueryParameter("operation", "update")
                                        .appendQueryParameter("_id", "1")
                                        .build();
                                getContentResolver().notifyChange(resultUri, null);
//                        db.update("Menulogin_checking", contentValueses, where, new String[]{});

//                        Intent intent = new Intent(this, Add_manage_ingredient_Activity.class);
//                        startActivity(intent);
                                Intent intent = new Intent(this, MultiFragIngredientsActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MenuActivity.this);
                            builder1.setTitle(getString(R.string.title7));
                            builder1.setMessage(getString(R.string.setmessage1));
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            builder1.setNegativeButton(
                                    "Buy now",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
//                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=1hIGJVeLjgI&index=2&list=PL6YNztMURCKTRsqbTbqFN10yDhrthtSY3")));

                                            check();

//                                        Log.d(TAG, "Buy gas button clicked.");
//
//
//                                        int titleResId;
//                                        titleResId = R.string.subscription_period_prompt;
//
//                                        String[] options = new String[]{"Upgrade To Pro(Rs.4999)"};
//                                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MenuActivity.this);
//                                        builder.setTitle(titleResId)
//                                                .setSingleChoiceItems(options, 0 /* checkedItem */, MenuActivity.this)
//                                                .setPositiveButton(R.string.subscription_prompt_continue, MenuActivity.this)
//                                                .setNegativeButton(R.string.subscription_prompt_cancel, MenuActivity.this);
//                                        android.app.AlertDialog dialog1 = builder.create();
//                                        dialog1.show();
//
//                                        // launch the gas purchase UI flow.
//                                        // We will be notified of completion via mPurchaseFinishedListener
//                                        setWaitScreen(true);
//                                        Log.d(TAG, "Launching purchase flow for gas.");
//
//                                        /* TODO: for security, generate your payload here for verification. See the comments on
//                                         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
//                                         *        an empty string, but on a production app you should carefully generate this. */
//                                        String payload = "";
                                        }
                                    });

                            builder1.setNeutralButton(
                                    "Know more",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=1hIGJVeLjgI&index=2&list=PL6YNztMURCKTRsqbTbqFN10yDhrthtSY3")));
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
                    } else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MenuActivity.this);
                        builder1.setTitle(getString(R.string.title7));
                        builder1.setMessage(getString(R.string.setmessage1));
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        builder1.setNegativeButton(
                                "Buy now",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
//                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=1hIGJVeLjgI&index=2&list=PL6YNztMURCKTRsqbTbqFN10yDhrthtSY3")));
                                        /*      Log.d(TAG, "Buy gas button clicked.");*/

                                        check();

//                                    int titleResId;
//                                    titleResId = R.string.subscription_period_prompt;
//
//                                    String[] options=new String[]{"Upgrade To Pro(Rs.4999)"};
//
//
//                                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MenuActivity.this);
//                                    builder.setTitle(titleResId)
//                                            .setSingleChoiceItems(options, 0 /* checkedItem */, MenuActivity.this)
//                                            .setPositiveButton(R.string.subscription_prompt_continue, MenuActivity.this)
//                                            .setNegativeButton(R.string.subscription_prompt_cancel, MenuActivity.this);
//                                    android.app.AlertDialog dialog1 = builder.create();
//                                    dialog1.show();
//
//                                    // launch the gas purchase UI flow.
//                                    // We will be notified of completion via mPurchaseFinishedListener
//                                    setWaitScreen(true);
//                                    Log.d(TAG, "Launching purchase flow for gas.");
//
//                                    /* TODO: for security, generate your payload here for verification. See the comments on
//                                     *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
//                                     *        an empty string, but on a production app you should carefully generate this. */
//                                    String payload = "";
                                    }
                                });

                        builder1.setNeutralButton(
                                "Know more",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=1hIGJVeLjgI&index=2&list=PL6YNztMURCKTRsqbTbqFN10yDhrthtSY3")));
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                } else {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MenuActivity.this);
                    builder1.setTitle(getString(R.string.title7));
                    builder1.setMessage(getString(R.string.setmessage1));
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "Buy now",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
//                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=1hIGJVeLjgI&index=2&list=PL6YNztMURCKTRsqbTbqFN10yDhrthtSY3")));
                                    /*      Log.d(TAG, "Buy gas button clicked.");*/

                                    check();

//                                int titleResId;
//                                titleResId = R.string.subscription_period_prompt;
//
//                                String[] options=new String[]{"Upgrade To Pro(Rs.4999)"};
//
//
//                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MenuActivity.this);
//                                builder.setTitle(titleResId)
//                                        .setSingleChoiceItems(options, 0 /* checkedItem */, MenuActivity.this)
//                                        .setPositiveButton(R.string.subscription_prompt_continue, MenuActivity.this)
//                                        .setNegativeButton(R.string.subscription_prompt_cancel, MenuActivity.this);
//                                android.app.AlertDialog dialog1 = builder.create();
//                                dialog1.show();
//
//                                // launch the gas purchase UI flow.
//                                // We will be notified of completion via mPurchaseFinishedListener
//                                setWaitScreen(true);
//                                Log.d(TAG, "Launching purchase flow for gas.");
//
//                                    /* TODO: for security, generate your payload here for verification. See the comments on
//                                     *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
//                                     *        an empty string, but on a production app you should carefully generate this. */
//                                String payload = "";
                                }
                            });

                    builder1.setNeutralButton(
                            "Know more",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=1hIGJVeLjgI&index=2&list=PL6YNztMURCKTRsqbTbqFN10yDhrthtSY3")));
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();


                }
                cursor1.close();
            } else {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(MenuActivity.this);
                builder1.setTitle(getString(R.string.title7));
                builder1.setMessage(getString(R.string.setmessage1));
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Buy now",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=1hIGJVeLjgI&index=2&list=PL6YNztMURCKTRsqbTbqFN10yDhrthtSY3")));
                                /*      Log.d(TAG, "Buy gas button clicked.");*/

                                check();

//                            int titleResId;
//                            titleResId = R.string.subscription_period_prompt;
//
//                            String[] options=new String[]{"Upgrade To Pro(Rs.4999)"};
//
//
//                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MenuActivity.this);
//                            builder.setTitle(titleResId)
//                                    .setSingleChoiceItems(options, 0 /* checkedItem */, MenuActivity.this)
//                                    .setPositiveButton(R.string.subscription_prompt_continue, MenuActivity.this)
//                                    .setNegativeButton(R.string.subscription_prompt_cancel, MenuActivity.this);
//                            android.app.AlertDialog dialog1 = builder.create();
//                            dialog1.show();
//
//                            // launch the gas purchase UI flow.
//                            // We will be notified of completion via mPurchaseFinishedListener
//                            setWaitScreen(true);
//                            Log.d(TAG, "Launching purchase flow for gas.");
//
//                                    /* TODO: for security, generate your payload here for verification. See the comments on
//                                     *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
//                                     *        an empty string, but on a production app you should carefully generate this. */
//                            String payload = "";
                            }
                        });

                builder1.setNeutralButton(
                        "Know more",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=1hIGJVeLjgI&index=2&list=PL6YNztMURCKTRsqbTbqFN10yDhrthtSY3")));
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


            }
        }else {
            android.app.AlertDialog.Builder builder;
            builder = new android.app.AlertDialog.Builder(this);
            //Setting message manually and performing action on button click
            builder.setMessage("Your IVEPOS app license is expired.\nBut you can create upto 1000 bills free. Kindly renew it to enjoy complete features.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            //Creating dialog box
            android.app.AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle(getString(R.string.title9));
            alert.show();
        }

    }


    @Override
    public void onClick(DialogInterface dialog, int id) {
//        Log.d(TAG, "Launching purchase flow for gas1.");
//        if (id == 0 /* First choice item */) {
//
//            Log.d(TAG, "Launching purchase flow for gas2.");
//        } else if (id == DialogInterface.BUTTON_POSITIVE /* continue button */) {
//
//            Log.d(TAG, "Launching purchase flow for gas3.");
//            /* TODO: for security, generate your payload here for verification. See the comments on
//             *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
//             *        an empty string, but on a production app you should carefully generate
//             *        this. */
//
//            i = 0;
//            String payload = "";
//
//
//            mSelectedProSubscription=SKU_DELAROY_PRO_UPGRADE;
//
//
//            try {
//                mHelperPro.launchPurchaseFlow(MenuActivity.this, mSelectedProSubscription, REQ_CODE,
//                        mPurchaseFinishedListenerPro, mSelectedProSubscription);
//            } catch (IabHelper.IabAsyncInProgressException e) {
//                //  complain("Error launching purchase flow. Another async operation in progress.");
//                // setWaitScreen(false);
//            }
//
//
//        } else if (id != DialogInterface.BUTTON_NEGATIVE) {
//
//
//
///*
//            Cursor cursor = db_inapp.rawQuery("SELECT * FROM pro_upgrade", null);
//            if (cursor.moveToFirst()) {
//                do {
//                    String idss = cursor.getString(0);
//                    String countval = cursor.getString(1);
//
//                    Cursor cursor1 = db_inapp.rawQuery("SELECT * FROM pro_upgrade WHERE status = '" + countval + "'", null);
//                    if (cursor1.moveToFirst()) {
//                        String id1 = cursor1.getString(0);
//                        String limit = cursor1.getString(1);
//                        ContentValues contentValues = new ContentValues();
//
//                        contentValues.put("status", "Activated");
//
//                        String where = "_id = '" + id1 + "'";
//                        db_inapp.update("pro_upgrade", contentValues, where, new String[]{});
//                    }
//
//                } while (cursor.moveToNext());
//            }*/
//
//
//
//        }


    }


    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListenerPro
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(mSelectedProSubscription)) {
                consumeItemPro();

            }

        }
    };

    public void consumeItemPro() {
        try {
            mHelperPro.queryInventoryAsync(mReceivedInventoryListenerPro);
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (!mHelperPro.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void gotocloud_subscription(View v) {

//        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        ContentValues contentValueses = new ContentValues();
        contentValueses.put("Status", "no");
        String where = "_id = '1'";

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Menulogin_checking");
        getContentResolver().update(contentUri, contentValueses,where,new String[]{});
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("Menulogin_checking")
                .appendQueryParameter("operation", "update")
                .appendQueryParameter("_id", "1")
                .build();
        getContentResolver().notifyChange(resultUri, null);
//        db.update("Menulogin_checking", contentValueses, where, new String[]{});

        Intent intent = new Intent(this, MultiFragCloudSubscrition.class);
        startActivity(intent);



    }

    public void gotoExpenses(View v) {

//        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);

        if (intdate <= Integer.parseInt(da)) {
            ContentValues contentValueses = new ContentValues();
            contentValueses.put("Status", "no");
            String where = "_id = '1'";

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Menulogin_checking");
            getContentResolver().update(contentUri, contentValueses, where, new String[]{});
            resultUri = new Uri.Builder()
                    .scheme("content")
                    .authority(StubProviderApp.AUTHORITY)
                    .path("Menulogin_checking")
                    .appendQueryParameter("operation", "update")
                    .appendQueryParameter("_id", "1")
                    .build();
            getContentResolver().notifyChange(resultUri, null);

//        db.update("Menulogin_checking", contentValueses, where, new String[]{});
            Intent intent = new Intent(this, MultiFragExpensesActivity.class);
            startActivity(intent);
        }else {
            android.app.AlertDialog.Builder builder;
            builder = new android.app.AlertDialog.Builder(this);
            //Setting message manually and performing action on button click
            builder.setMessage("Your IVEPOS app license is expired.\nBut you can create upto 1000 bills free. Kindly renew it to enjoy complete features.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            //Creating dialog box
            android.app.AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle(getString(R.string.title9));
            alert.show();
        }

    }

    public void gotoKot(View v) {

//        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);

        if (intdate <= Integer.parseInt(da)) {
            ContentValues contentValueses = new ContentValues();
            contentValueses.put("Status", "no");
            String where = "_id = '1'";

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Menulogin_checking");
            getContentResolver().update(contentUri, contentValueses, where, new String[]{});
            resultUri = new Uri.Builder()
                    .scheme("content")
                    .authority(StubProviderApp.AUTHORITY)
                    .path("Menulogin_checking")
                    .appendQueryParameter("operation", "update")
                    .appendQueryParameter("_id", "1")
                    .build();
            getContentResolver().notifyChange(resultUri, null);

//        db.update("Menulogin_checking", contentValueses, where, new String[]{});
            Intent intent = new Intent(this, MultiFragKotActivity.class);
            startActivity(intent);
        }else {
            android.app.AlertDialog.Builder builder;
            builder = new android.app.AlertDialog.Builder(this);
            //Setting message manually and performing action on button click
            builder.setMessage("Your IVEPOS app license is expired.\nBut you can create upto 1000 bills free. Kindly renew it to enjoy complete features.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            //Creating dialog box
            android.app.AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle(getString(R.string.title9));
            alert.show();
        }

    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(MenuActivity.this, MenuActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                LoginActivity.this.finish();
//        startActivity(intent);
        super.onBackPressed();
        this.finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //Toast.makeText(MenuActivity.this, "permission granted", Toast.LENGTH_SHORT).show();
                    System.out.println("permission granted");
                    if (!SdIsPresent()) ;

//                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                    ContentValues contentValueses = new ContentValues();
                    contentValueses.put("Status", "no");
                    String where = "_id = '1'";

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Menulogin_checking");
                    getContentResolver().update(contentUri, contentValueses,where,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Menulogin_checking")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id", "1")
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.update("Menulogin_checking", contentValueses, where, new String[]{});

                    Intent intent = new Intent(this, MultiFragBackupActivity.class);
                    startActivity(intent);


                } else {

                    Toast.makeText(MenuActivity.this, "permission denied", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //Toast.makeText(MenuActivity.this, "permission granted", Toast.LENGTH_SHORT).show();
                    System.out.println("permission granted");
                    if (!SdIsPresent()) ;

//                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                    ContentValues contentValueses = new ContentValues();
                    contentValueses.put("Status", "no");
                    String where = "_id = '1'";

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Menulogin_checking");
                    getContentResolver().update(contentUri, contentValueses,where,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Menulogin_checking")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id", "1")
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.update("Menulogin_checking", contentValueses, where, new String[]{});

                    Intent intent = new Intent(this, MultiFragOrderlistActivity.class);
                    startActivity(intent);


                } else {

                    Toast.makeText(MenuActivity.this, "permission denied", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case 3: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //Toast.makeText(MenuActivity.this, "permission granted", Toast.LENGTH_SHORT).show();
                    System.out.println("permission granted");
                    if (!SdIsPresent()) ;

//                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                    ContentValues contentValueses = new ContentValues();
                    contentValueses.put("Status", "no");
                    String where = "_id = '1'";

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Menulogin_checking");
                    getContentResolver().update(contentUri, contentValueses,where,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Menulogin_checking")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id", "1")
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//                    db.update("Menulogin_checking", contentValueses, where, new String[]{});

                    Intent intent = new Intent(this, MultiFragCustomerActivity.class);
                    startActivity(intent);


                } else {

                    Toast.makeText(MenuActivity.this, "permission denied", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case 4: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //Toast.makeText(MenuActivity.this, "permission granted", Toast.LENGTH_SHORT).show();
                    System.out.println("permission granted");
                    if (!SdIsPresent()) ;

//                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    ContentValues contentValueses = new ContentValues();
                    contentValueses.put("Status", "no");
                    String where = "_id = '1'";
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Menulogin_checking");
                    getContentResolver().update(contentUri, contentValueses, where, new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Menulogin_checking")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id", "1")
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
//        db.update("Menulogin_checking", contentValueses, where, new String[]{});

                    Intent intent = new Intent(this, MultiFragIngredientsActivity.class);
                    startActivity(intent);


                } else {

                    Toast.makeText(MenuActivity.this, "permission denied", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static boolean SdIsPresent() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * Verifies the developer payload of a purchase.
     */
    boolean verifyDeveloperPayload(final Purchase p) {
        payload = p.getDeveloperPayload();
        checking = p.getOrderId();
        state = p.getPurchaseState();


        return true;

    }

    boolean verifyDeveloperPayload1(Purchase p) {
        Cursor cursor = db_inapp.rawQuery("SELECT * FROM pro_upgrade", null);
        if (cursor.moveToFirst()) {
            do {
                String idss = cursor.getString(0);

                payload = p.getDeveloperPayload();
                checking = p.getOrderId();
                state = p.getPurchaseState();


                ContentValues contentValues = new ContentValues();
                contentValues.put("orderid", checking);

                String where = "_id = '" + idss + "'";
                db_inapp.update("pro_upgrade", contentValues, where, new String[]{});
//                Cursor cursor1 = db_inapp.rawQuery("SELECT * FROM Messaginglicense WHERE Messagessent = '" + countval + "'", null);
//                if (cursor1.moveToFirst()) {
//                    String id1 = cursor1.getString(0);
//
//                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }


    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {


            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            updateUi();
            setWaitScreen(false);
            Log.d(TAG, "End consumption flow.");


        }
    };


    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();



    }

    // updates UI to reflect model
    public void updateUi() {

      /*      Intent intent = new Intent(this, MenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            finish();
*/

        Intent intent = new Intent(this, Add_manage_ingredient_Activity.class);
        startActivity(intent);


    }

    // Enables or disables the "please wait" screen.
    void setWaitScreen(boolean set) {
//        findViewById(R.id.screen_main).setVisibility(set ? View.GONE : View.VISIBLE);
    }


    // Enables or disables the "please wait" screen.
    void setWaitScreen1(boolean set) {
//        findViewById(R.id.screen_main).setVisibility(set ? View.GONE : View.VISIBLE);
    }

    void complain(String message) {
        Log.e(TAG, "**** Delaroy Error: " + message);
        alert("Error: " + message);
    }

    void complain1(String message) {
        Log.e(TAG, "**** Delaroy Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        android.app.AlertDialog.Builder bld = new android.app.AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();

    }


    public boolean databaseExist() {
        File DATA_DIRECTORY_DATABASE =
                new File(Environment.getDataDirectory() +
                        "/data/" + "com.intuition.ivepos" +
                        "/databases/" + "amazoninapp");

        return DATA_DIRECTORY_DATABASE.exists();
    }


    public void check() {


        dialog_pro = new Dialog(MenuActivity.this, R.style.notitle);
        dialog_pro.setContentView(R.layout.dialog_pro_purchase);
        dialog_pro.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog_pro.show();

        final RadioGroup radioGroupSplit = (RadioGroup) dialog_pro.findViewById(R.id.splitgroup);

        final RadioButton one_demo = (RadioButton) dialog_pro.findViewById(R.id.btnone);
        final RadioButton two_buy = (RadioButton) dialog_pro.findViewById(R.id.btntwo);
        final RadioButton three_coupon = (RadioButton) dialog_pro.findViewById(R.id.btnthree);



        Cursor cursor_pro=db_inapp.rawQuery("SELECT * FROM Pro_upgrade", null);
        if(cursor_pro.moveToFirst()){
            String status=cursor_pro.getString(1);
            String status_da=cursor_pro.getString(3);
            if(status.equalsIgnoreCase("Activated")){
                if (Integer.parseInt(status_da) > Integer.parseInt(currentDateandTime1_pro)) {
                    one_demo.setVisibility(View.GONE);
                    two_buy.setChecked(true);
                }else {
                    one_demo.setVisibility(View.GONE);
                    two_buy.setChecked(true);
                }
            }else{
                one_demo.setVisibility(View.VISIBLE);
                one_demo.setChecked(true);
            }
        }
        cursor_pro.close();

        Button procancel = (Button) dialog_pro.findViewById(R.id.procancel);
        procancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_pro.dismiss();
            }
        });

        ImageView closetext = (ImageView) dialog_pro.findViewById(R.id.closetext);
        closetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_pro.dismiss();
            }
        });

        Button proapply = (Button) dialog_pro.findViewById(R.id.proapply);
        proapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int selected1 = radioGroupSplit.getCheckedRadioButtonId();
                RadioButton radioBtnSplit = (RadioButton) dialog_pro.findViewById(selected1);

                if (one_demo.isChecked()) {
//                            Toast.makeText(CloudSubscritionActivity.this, "demo", Toast.LENGTH_LONG).show();

                    i_pro_sel = 1;
                    checking="123";
                    updateProCloud("");

                }else {
                    if (two_buy.isChecked()){
//                                Toast.makeText(CloudSubscritionActivity.this, "buy", Toast.LENGTH_LONG).show();
                        dialog_pro.dismiss();

                        mSelectedProSubscription=SKU_DELAROY_PRO_UPGRADE;


                        try {
                            mHelperPro.launchPurchaseFlow(MenuActivity.this, mSelectedProSubscription, REQ_CODE,
                                    mPurchaseFinishedListenerPro, mSelectedProSubscription);
                        } catch (IabHelper.IabAsyncInProgressException e) {
                            //  complain("Error launching purchase flow. Another async operation in progress.");
                            // setWaitScreen(false);
                        }


                    }else {
                        if (three_coupon.isChecked()){
//                                    Toast.makeText(CloudSubscritionActivity.this, "coupon", Toast.LENGTH_LONG).show();

                            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(MenuActivity.this);
                            alertDialog.setTitle(getString(R.string.title13));
                            alertDialog.setMessage(getString(R.string.setmessage15));
                            final EditText input = new EditText(MenuActivity.this);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                            input.setLayoutParams(lp);
                            alertDialog.setView(input);

                            alertDialog.setPositiveButton("Submit",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            String code = input.getText().toString();

                                            i_pro_sel = 3;
                                            checking=code;
                                            updateProCloud(code);

                                        }
                                    });

                            alertDialog.setNegativeButton("NO",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            alertDialog.show();

                        }
                    }
                }
            }
        });

    }


}