package com.intuition.ivepos;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.intuition.ivepos.syncdb.MyServiceApp;
import com.intuition.ivepos.syncdb.MyServiceAppData;
import com.intuition.ivepos.syncdb.MyServiceSales;
import com.intuition.ivepos.syncdb.SyncDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 3/17/2018.
 */

public class Checking_Store extends AppCompatActivity implements MyServiceAppData.OnProgressUpdateListener,MyServiceSales.OnProgressUpdateListener{
    Spinner spinner_store;
    Spinner spinner_device;
    String  deviceitem;
    List<String> categories1, categories;
    String categories2;
    String company,email;
    ArrayAdapter<String> dataAdapter1, dataAdapter;
   // custom_arrayadapter<String> dataAdapter,dataAdapter1;
    LinearLayout btn;
    CardView progressBar_license;
    RelativeLayout progressBar;
    TextView textView_license;
    LinearLayout layout;
    RelativeLayout action;
    public static final String PACKAGE_NAME = "com.intuition.ivepos";
    public String DATA_DIRECTORY_DATABASEAPP =
            Environment.getDataDirectory() +
                    "/data/" + PACKAGE_NAME +
                    "/databases/" + "mydb_Appdata" ;


    public String DATA_DIRECTORY_DATABASESALES =
            Environment.getDataDirectory() +
                    "/data/" + PACKAGE_NAME +
                    "/databases/" + "mydb_Salesdata" ;


    public String DATA_DIRECTORY_DATABASE =
            Environment.getDataDirectory() +
                    "/data/" + PACKAGE_NAME +
                    "/databases/" + "syncdb" ;

    StoreBean storeBean;
    public static final String AUTHORITY = "com.intuition.ivepos.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "intuitiondine.com";
    // The account name
    public static final String ACCOUNT = "default_account_dine";
    public static final String SCHEME = "content";
    // Instance fields
    public static  Account mAccount;


    public static final String AUTHORITY1 = "com.intuition.ivepos.syncapp.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE1 = "intuitionsdine.com";
    // The account name
    public static final String ACCOUNT1 = "dummy_account_dine";
    public static final String SCHEME1 = "content://";
    // Instance fields
    public static  Account mAccount1;
    public SQLiteDatabase db_inapp = null;

    String finalTextcompanyname;
    String finalStoreitem;
    String finalDeviceitem;
    String finalCompemailid;
    RequestQueue requestQueue;

    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;

    ProgressBar updateBar,updatebarsales;
    TextView tv_perc;
    public static boolean appdataBool=false,salesdataBool=false;
    RequestQueue queue;
    public static int prog=0;
    public static int progSales=0;

    String WebserviceUrl;
    String account_selection;

    String[] devices;

    int i_close = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checking_store_selection);
        prog=0;
        progSales=0;
        appdataBool=false;
        salesdataBool=false;

     //   MyServiceApp.setOnProgressChangedListener(this);

     //   MyService.setOnProgressChangedListener(this);
        progressBar = findViewById(R.id.progressbar_checkingstore);



        SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(Checking_Store.this);
        account_selection= sharedpreferences.getString("account_selection", null);

        if ((account_selection.toString().equals("Dine")) || (account_selection.toString().equals(getString(R.string.app_name)))) {
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }else {
            if (account_selection.toString().equals("Qsr")) {
                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
            }else {
                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
            }
        }

        MyServiceAppData.setOnProgressChangedListener(this);

        MyServiceSales.setOnProgressChangedListener(this);


        tv_perc=findViewById(R.id.tv_perc);
        updateBar= findViewById(R.id.updatebar);
        //updateBar.setMax(426);
        updatebarsales=findViewById(R.id.updatebarsales);
        updatebarsales.setMax(320);

        final Bundle extras = getIntent().getExtras();

        company = extras.getString("company");
        email = extras.getString("email");

        ImageView leftarrow = (ImageView) findViewById(R.id.leftarrow);
        leftarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                R_NewLogIn_BusinessType.storeList.clear();
                MainActivity_Signin_OTPbased.storeList.clear();
                finish();
            }
        });


        spinner_store = (Spinner) findViewById(R.id.store_spinner);
        spinner_device = (Spinner) findViewById(R.id.device_spinner);
        btn = (LinearLayout) findViewById(R.id.signUp_confirm);
        categories = new ArrayList<String>();
//changed to   MainActivity_Signin_OTPbased to R_NewLogIn_BusinessType
        List bb1;

        categories1 = new ArrayList<String>();
        for(int i=0;i<R_NewLogIn_BusinessType.storeList.size();i++){
            categories.add(R_NewLogIn_BusinessType.storeList.get(i).getStoreName());
          //  bb1=categories;
        }
        bb1=categories;

        dataAdapter = new ArrayAdapter<String>(Checking_Store.this, android.R.layout.simple_spinner_item, categories);
       // dataAdapter= new custom_arrayadapter<>(Checking_Store.this,android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_store.setAdapter(dataAdapter);


            spinner_store.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
// Risi code   MainActivity_Signin_OTPbased to R_NewLogIn_BusinessType
                    categories1 = new ArrayList<String>();
                    storeBean=R_NewLogIn_BusinessType.storeList.get(position);
                    String[] devices=storeBean.getDevices();

                    categories1.addAll(Arrays.asList(devices));

                    dataAdapter1 = new ArrayAdapter<String>(Checking_Store.this, android.R.layout.simple_spinner_item, categories1);
                  //  dataAdapter1 = new custom_arrayadapter<>(Checking_Store.this, android.R.layout.simple_spinner_item, categories1);
                    dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter1.notifyDataSetChanged();
                    spinner_device.setAdapter(dataAdapter1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinner_device.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    deviceitem = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinner_store.setSelection(0);

            progressBar_license = (CardView) findViewById(R.id.progressbar1);
            textView_license = (TextView) findViewById(R.id.progress_text1);
            layout = (LinearLayout) findViewById(R.id.layout);
            action = (RelativeLayout) findViewById(R.id.action);



        //r_new login


        if (bb1.size()==1){
//            dataAdapter = new ArrayAdapter<String>(Checking_Store.this, android.R.layout.simple_spinner_item, categories);
//            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinner_store.setAdapter(dataAdapter);

            StringBuffer ca = new StringBuffer();
            for (int k=0;k<R_NewLogIn_BusinessType.storeList.size();k++){
                storeBean=R_NewLogIn_BusinessType.storeList.get(k);
                String[] devices= storeBean.getDevices();
                categories1.add(devices[k]);
                ca.append(devices[k]);
                //  categories1 = Arrays.asList(devices);
                // Arrays.toString(devices);
                //  categories1.add(Arrays.toString(devices));
            }
            categories2 = ca.toString();
            dataAdapter1 = new ArrayAdapter<String>(Checking_Store.this, android.R.layout.simple_spinner_item, categories1);
            //   dataAdapter1 = new custom_arrayadapter<>(Checking_Store.this, android.R.layout.simple_spinner_item, categories1);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter1.notifyDataSetChanged();
            spinner_device.setAdapter(dataAdapter1);
            //   spinner_device.getSelectedItem();

            autoregi();

        }


        //r_new login

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i_close = 0;
                btn.setVisibility(View.GONE);
                layout.setVisibility(View.GONE);
                action.setVisibility(View.GONE);
                progressBar_license.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
                db_inapp.disableWriteAheadLogging();
                Checking_Store.this.deleteDatabase("amazoninapp");

                if(databaseExist()){

                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                    final String currentDateandTime1 = sdf2.format(new Date());
                    String date = "00", year = "0000", month = "00";

                    db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
                    db_inapp.disableWriteAheadLogging();
                    Cursor cursor = db_inapp.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'credentialstime'", null);
                    if(cursor!=null) {
                        if(cursor.getCount()>0) {
                            cursor.close();

                            String textcompanyname = "", storeitem = "", deviceitem = "", compemailid = "";

                            Cursor cursor_cred = db_inapp.rawQuery("SELECT * FROM credentialstime", null);
                            if (cursor_cred.moveToFirst()) {
                                textcompanyname = cursor_cred.getString(6);
                                storeitem = cursor_cred.getString(7);
                                deviceitem = cursor_cred.getString(8);
                                compemailid = cursor_cred.getString(5);
                            }
                            cursor_cred.close();

                            finalTextcompanyname = textcompanyname;
                            finalStoreitem = storeitem;
                            finalDeviceitem = deviceitem;
                            finalCompemailid = compemailid;

                            Cursor cursor1 = db_inapp.rawQuery("SELECT * FROM credentialstime", null);
                            if (cursor1.moveToFirst()) {
                                date = cursor1.getString(9);//22mar2018   }
                            }
                            cursor1.close();

                            // final String da = "20181020"; //yyyymmdd
                            if (date != null) {
                                final String da = date; //yyyymmdd
                                final int intdate = Integer.parseInt(currentDateandTime1);

                                if (intdate <= Integer.parseInt(da)) {

                                    if (account_selection.toString().equals("Dine") || account_selection.toString().equals(getString(R.string.app_name))) {
                                        Intent intent = new Intent(Checking_Store.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        intent.putExtra("from","checking");
                                     //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        if (account_selection.toString().equals("Qsr")) {
                                            Intent intent = new Intent(Checking_Store.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            intent.putExtra("from","checking");
                                         //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            Intent intent = new Intent(Checking_Store.this, MainActivity_Retail.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            intent.putExtra("from","checking");
                                         //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

//                                    Intent intent = new Intent(Checking_Store.this, MainActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                    intent.putExtra("from","checking");
//                                    startActivity(intent);
//                                    finish();

                                } else {


                                    Intent intent = new Intent(Checking_Store.this, MainActivity_subscription.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    intent.putExtra("emailid", finalCompemailid);
                                    intent.putExtra("storename", finalStoreitem);
                                    intent.putExtra("devicename", finalDeviceitem);
                                    intent.putExtra("companyname", finalTextcompanyname);
                                    intent.putExtra("from", "checking");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);


                                }
                            }

                        }
                        cursor.close();
                    }


                }else{



                    SharedPreferences pref= getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("emailid", email); // Storing boolean - true/false
                    editor.putString("storename", storeBean.getStoreName()); // Storing string
                    editor.putString("devicename", deviceitem); // Storing integer
                    editor.putString("companyname", company); // Storing float// Storing long
                    editor.commit();

//                    getlicenses();
                    updateBar.setMax(200);
                    new DeleteData().execute();
                    new DeleteSalesData().execute();


                 /*       Intent intent = new Intent(Checking_Store.this, MainActivity_subscription.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("emailid", email);
                        intent.putExtra("storename", storeBean.getStoreName());
                        intent.putExtra("devicename", deviceitem);
                        intent.putExtra("companyname", company);
                        intent.putExtra("from", "checking");
                        startActivity(intent);*/
                }





//                boolean isDbPresent=checkDataBase(DATA_DIRECTORY_DATABASE);
//                boolean isDbPresentapp=checkDataBase(DATA_DIRECTORY_DATABASEAPP);
//                boolean isDbPresentsales=checkDataBase(DATA_DIRECTORY_DATABASESALES);
//
//
//                getDefaultSharedPreferencesMultiProcess(getApplicationContext()) // 0 - for private mode
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putString("emailid", email); // Storing boolean - true/false
//                editor.putString("storename", storeBean.getStoreName()); // Storing string
//                editor.putString("devicename", deviceitem); // Storing integer
//                editor.putString("companyname", company); // Storing float// Storing long
//                editor.commit();
//
//
//                if(isDbPresent){
//
//                    Bundle extras1=new Bundle();
//                    extras1.putString("download","download");
//
//                    SyncHelper syncHelper=new SyncHelper();
//                    syncHelper.tablesync(getApplicationContext());
//                    SyncHelperApp syncHelperapp=new SyncHelperApp();
//                    syncHelperapp.tablesync(getApplicationContext());
//
//
//                    mAccount = new Account(
//                            ACCOUNT, ACCOUNT_TYPE);
//
//                    final AccountManager accountManager = (AccountManager) getApplicationContext()
//                            .getSystemService(ACCOUNT_SERVICE);
//
//                    if (!accountManager.addAccountExplicitly(mAccount, null, null)) {
//                        mAccount = accountManager.getAccountsByType(ACCOUNT_TYPE)[0];
//                    }
//
//
//
//                    mAccount1 = new Account(
//                            ACCOUNT1, ACCOUNT_TYPE1);
//
//                    final AccountManager accountManager1 = (AccountManager) getApplicationContext()
//                            .getSystemService(ACCOUNT_SERVICE);
//
//                    if (!accountManager1.addAccountExplicitly(mAccount1, null, null)) {
//                        mAccount1 = accountManager1.getAccountsByType(ACCOUNT_TYPE1)[0];
//                    }
//
//
//                    setAccountSyncable();
//                    setAccountSyncable1();
//
//                    ContentResolver.requestSync(mAccount, AUTHORITY, extras1);
//                    ContentResolver.requestSync(mAccount1, AUTHORITY1, extras1);
//
//
//                    Intent intent = new Intent(Checking_Store.this, MainActivity.class);
//                    intent.putExtra("from","checking");
//                    startActivity(intent);
//                    finish();
//
//                }else{
//
//                    Bundle extras1=new Bundle();
//                    extras1.putString("download","download");
//
//                    SyncDatabase syncdatabase=new SyncDatabase();
//                    syncdatabase.createSyncDb(getApplicationContext());
//
//                    SyncHelper syncHelper=new SyncHelper();
//                    syncHelper.tablesync(getApplicationContext());
//                    SyncHelperApp syncHelperapp=new SyncHelperApp();
//                    syncHelperapp.tablesync(getApplicationContext());
//
//
//                    mAccount = new Account(
//                            ACCOUNT, ACCOUNT_TYPE);
//
//                    final AccountManager accountManager = (AccountManager) getApplicationContext()
//                            .getSystemService(ACCOUNT_SERVICE);
//
//                    if (!accountManager.addAccountExplicitly(mAccount, null, null)) {
//                        mAccount = accountManager.getAccountsByType(ACCOUNT_TYPE)[0];
//                    }
//
//
//
//                    mAccount1 = new Account(
//                            ACCOUNT1, ACCOUNT_TYPE1);
//
//                    final AccountManager accountManager1 = (AccountManager) getApplicationContext()
//                            .getSystemService(ACCOUNT_SERVICE);
//
//                    if (!accountManager1.addAccountExplicitly(mAccount1, null, null)) {
//                        mAccount1 = accountManager1.getAccountsByType(ACCOUNT_TYPE1)[0];
//                    }
//
//                    setAccountSyncable();
//                    setAccountSyncable1();
//
//                    ContentResolver.requestSync(mAccount, AUTHORITY, extras1);
//                    ContentResolver.requestSync(mAccount1, AUTHORITY1, extras1);
//
//
//                    Intent intent = new Intent(Checking_Store.this, MainActivity.class);
//                    intent.putExtra("from","checking");
//                    startActivity(intent);
//                    finish();
//                }

                //   new FetchDataTask().execute();
            }

        });


    }
    public void autoregi(){
        // new r_login code


//        dataAdapter = new ArrayAdapter<String>(Checking_Store.this, android.R.layout.simple_spinner_item, categories);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner_store.setAdapter(dataAdapter);
//
//        StringBuffer ca = new StringBuffer();
//        for (int k=0;k<R_NewLogIn_BusinessType.storeList.size();k++){
//            storeBean=R_NewLogIn_BusinessType.storeList.get(k);
//            String[] devices= storeBean.getDevices();
//            //   categories1.add(devices[k]);
//            ca.append(devices[k]);
//            //  categories1 = Arrays.asList(devices);
//            // Arrays.toString(devices);
//            //  categories1.add(Arrays.toString(devices));
//        }
//        categories2 = ca.toString();
//        dataAdapter1 = new ArrayAdapter<String>(Checking_Store.this, android.R.layout.simple_spinner_item, categories1);
//        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dataAdapter1.notifyDataSetChanged();
//        spinner_device.setAdapter(dataAdapter1);



        int storecount,devicecount;
        storecount= categories1.size();

      //  progressBar.setVisibility(View.VISIBLE);

        //     if (storecount==1){
        //  progressBar_license.setVisibility(View.VISIBLE);
        i_close = 0;

        db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
        db_inapp.disableWriteAheadLogging();
        Checking_Store.this.deleteDatabase("amazoninapp");

        if(databaseExist()){
           // Toast.makeText(Checking_Store.this,"mera wala",Toast.LENGTH_LONG).show();
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
            final String currentDateandTime1 = sdf2.format(new Date());
            String date = "00", year = "0000", month = "00";

            db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
            db_inapp.disableWriteAheadLogging();
            Cursor cursor = db_inapp.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'credentialstime'", null);
            if(cursor!=null) {
                if(cursor.getCount()>0) {
                    cursor.close();

                    String textcompanyname = "", storeitem = "", deviceitem = "", compemailid = "";

                    Cursor cursor_cred = db_inapp.rawQuery("SELECT * FROM credentialstime", null);
                    if (cursor_cred.moveToFirst()) {
                        textcompanyname = cursor_cred.getString(6);
                        storeitem = cursor_cred.getString(7);
                        deviceitem = cursor_cred.getString(8);
                        compemailid = cursor_cred.getString(5);
                    }
                    cursor_cred.close();

                    finalTextcompanyname = textcompanyname;
                    finalStoreitem = storeitem;
                    finalDeviceitem = deviceitem;
                    finalCompemailid = compemailid;

                    Cursor cursor1 = db_inapp.rawQuery("SELECT * FROM credentialstime", null);
                    if (cursor1.moveToFirst()) {
                        date = cursor1.getString(9);//22mar2018   }
                    }
                    cursor1.close();

                    // final String da = "20181020"; //yyyymmdd
                    if (date != null) {
                        final String da = date; //yyyymmdd
                        final int intdate = Integer.parseInt(currentDateandTime1);

                        if (intdate <= Integer.parseInt(da)) {

                            if (account_selection.toString().equals("Dine") || account_selection.toString().equals(getString(R.string.app_name))) {
                                Intent intent = new Intent(Checking_Store.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.putExtra("from","checking");
                                startActivity(intent);
                             //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                finish();
                            }else {
                                if (account_selection.toString().equals("Qsr")) {
                                    Intent intent = new Intent(Checking_Store.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    intent.putExtra("from","checking");
                                    startActivity(intent);
                                 //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    finish();
                                }else {
                                    Intent intent = new Intent(Checking_Store.this, MainActivity_Retail.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    intent.putExtra("from","checking");
                                    startActivity(intent);
                                //    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    finish();
                                }
                            }

//                                    Intent intent = new Intent(Checking_Store.this, MainActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                    intent.putExtra("from","checking");
//                                    startActivity(intent);
//                                    finish();

                        } else {


                            Intent intent = new Intent(Checking_Store.this, MainActivity_subscription.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("emailid", finalCompemailid);
                            intent.putExtra("storename", finalStoreitem);
                            intent.putExtra("devicename", finalDeviceitem);
                            intent.putExtra("companyname", finalTextcompanyname);
                            intent.putExtra("from", "checking");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);


                        }
                    }

                }
                cursor.close();
            }


        }else{
            layout = (LinearLayout) findViewById(R.id.layout);
            action = (RelativeLayout) findViewById(R.id.action);
            btn.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
            action.setVisibility(View.GONE);
            progressBar_license = (CardView) findViewById(R.id.progressbar1);
            progressBar_license.setVisibility(View.VISIBLE);

           // Toast.makeText(Checking_Store.this,"Yes else pe",Toast.LENGTH_LONG).show();
            SharedPreferences pref= getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("emailid", email); // Storing boolean - true/false
            editor.putString("storename", storeBean.getStoreName()); // Storing string
        //    editor.putString("devicename", deviceitem); // Storing integer
         //   editor.putString("devicename", String.valueOf(storeBean.getDevices()));
            editor.putString("companyname", company); // Storing float// Storing long
        //    editor.putString("storename",categories.toString());
            editor.putString("devicename", categories2);
            editor.commit();



//                    getlicenses();
            updateBar.setMax(200);
            new DeleteData().execute();
            new DeleteSalesData().execute();


                 /*       Intent intent = new Intent(Checking_Store.this, MainActivity_subscription.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("emailid", email);
                        intent.putExtra("storename", storeBean.getStoreName());
                        intent.putExtra("devicename", deviceitem);
                        intent.putExtra("companyname", company);
                        intent.putExtra("from", "checking");
                        startActivity(intent);*/
        }

        //   }
        // new r_login code
    }


    @Override
    public void onProgressUpdate(int progress) {
        // Do update your progress...

        updateBar.setVisibility(View.VISIBLE);
        Log.e("appdata progress",progress+"");
      //  updateBar.setProgress(progress);
        prog = progress;
        updateBar.setProgress(prog+progSales);

        float perc= ((float)updateBar.getProgress()/(float)updateBar.getMax())*100;
        int p=(int) perc;
        tv_perc.setText(p+"%");
    }



    public void getlicenses(){

        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        final String company= sharedpreferences.getString("companyname", null);
        final String store= sharedpreferences.getString("storename", null);
        final String device= sharedpreferences.getString("devicename", null);
        final String email=sharedpreferences.getString("emailid",null);

        btn.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        action.setVisibility(View.GONE);
        progressBar_license.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        requestQueue = Volley.newRequestQueue(this);
//        System.out.println("checking store1 "+WebserviceUrl);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"getlicense.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("checking store "+response);

                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            String status=jsonObject.getString("status");

                            if(status.equalsIgnoreCase("success")){
                                System.out.println("checking store 2");

                                //    progressBar_license.setVisibility(View.GONE);

                                String postodate=jsonObject.getString("postodate");
                                String dashboardexpiry=jsonObject.getString("dashboardexpiry");
                                String remaining_msgs=jsonObject.getString("remaining_msgs");
                                String remaining_orders=jsonObject.getString("remaining_orders");
                                String proactivated=jsonObject.getString("proactivated");
                                String pro_expiry=jsonObject.getString("pro_expiry");

                                String match = " ";
                                int position = pro_expiry.toString().indexOf(match);
                                String mod2 = pro_expiry.toString().substring(0, position);
                                mod2 = mod2.replace("-", "");

                                System.out.println("checking store 3");

                                db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
                                db_inapp.disableWriteAheadLogging();

                                db_inapp.execSQL("CREATE TABLE if not exists credentialstime (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, orderid text,time text,date text,trial_time text, " +
                                        "email_id text, company_name text, store_name text, dev_name text, todate text);");

                                ContentValues contentValues=new ContentValues();
                                contentValues.put("email_id", email);
                                contentValues.put("company_name", company);
                                contentValues.put("store_name", store);
                                contentValues.put("dev_name", deviceitem);

                                String[] separated = postodate.split(" ");
                                String convertedDate=separated[0].replace("-","");

                                System.out.println("checking store 4");

                                Log.e("converted pos date",convertedDate);
                                contentValues.put("todate", convertedDate);
                                db_inapp.insert("credentialstime", null, contentValues);

                                db_inapp.execSQL("CREATE TABLE if not exists Pro_upgrade (_id integer PRIMARY KEY UNIQUE, status text, orderid text);");
//                                db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
                                Cursor co31 = db_inapp.rawQuery("SELECT * FROM Pro_upgrade", null);
                                int cou31 = co31.getColumnCount();
                                if (String.valueOf(cou31).equals("3")) {
                                    db_inapp.execSQL("ALTER TABLE Pro_upgrade ADD COLUMN pro_expiry");

                                    ContentValues cv = new ContentValues();
                                    if(proactivated.equalsIgnoreCase("NO")){
                                        cv.put("status", "Not Activated");
                                        cv.put("pro_expiry", mod2);
                                        db_inapp.insert("Pro_upgrade", null, cv);
                                    }else{
                                        cv.put("status", "Activated");
                                        cv.put("pro_expiry", mod2);
                                        db_inapp.insert("Pro_upgrade", null, cv);
                                    }
                                }else {
                                    ContentValues cv = new ContentValues();
                                    if(proactivated.equalsIgnoreCase("NO")){
                                        cv.put("status", "Not Activated");
                                        cv.put("pro_expiry", mod2);
                                        db_inapp.insert("Pro_upgrade", null, cv);
                                    }else{
                                        cv.put("status", "Activated");
                                        cv.put("pro_expiry", mod2);
                                        db_inapp.insert("Pro_upgrade", null, cv);
                                    }
                                }
                                co31.close();

                                System.out.println("checking store 5");

                                db_inapp.execSQL("CREATE TABLE if not exists Messaginglicense (_id integer PRIMARY KEY UNIQUE, remainingmessages text, Messagessent text,orderid text,time text,date text, package text);");
                                ContentValues cv1 = new ContentValues();
                                cv1.put("Messagessent", "0");
                                cv1.put("remainingmessages", remaining_msgs);
                                cv1.put("date", "");
                                cv1.put("time", "");
                                db_inapp.insert("Messaginglicense", null, cv1);



                                db_inapp.execSQL("CREATE TABLE if not exists Orderlicense (_id integer PRIMARY KEY UNIQUE, remainingorders text, ordersrece text,orderid text,time text,date text, package text);");
                                ContentValues cv2 = new ContentValues();
                                cv2.put("ordersrece", "0");
                                cv2.put("remainingorders", remaining_orders);
                                cv2.put("date", "");
                                cv2.put("time", "");
                                db_inapp.insert("Orderlicense", null, cv2);

                                System.out.println("checking store 6");

                                SQLiteDatabase db_subs = openOrCreateDatabase("dashboard_subscription", Context.MODE_PRIVATE, null);
                                db_subs.execSQL("CREATE TABLE if not exists subscription (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, orderid text,time text,date text,trial_time text, " +
                                        "email_id text, company_name text, store_name text, dev_name text, todate text);");

                                String[] separateds = dashboardexpiry.split(" ");
                                String dashboardexpiryDate=separateds[0].replace("-","");

                                ContentValues cv11=new ContentValues();
                                cv11.put("todate", dashboardexpiryDate);
                                cv11.put("email_id", email);
                                cv11.put("company_name", company);
                                cv11.put("store_name", store);
                                cv11.put("dev_name", deviceitem);
                                db_subs.insert("subscription", null, cv11);

                                System.out.println("checking store 7");

//                                db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//                                Cursor c = db.rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table'", null);
//                                if (c.moveToFirst()) {
//                                    int co = c.getInt(0);
//                                    Toast.makeText(Checking_Store.this, "count1 "+co, Toast.LENGTH_SHORT).show();
//                                }
//                                c.close();
//
//                                db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
//                                Cursor c1 = db1.rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table'", null);
//                                if (c1.moveToFirst()) {
//                                    int co = c1.getInt(0);
//                                    Toast.makeText(Checking_Store.this, "count2 "+co, Toast.LENGTH_SHORT).show();
//                                }
//                                c1.close();

                                if(databaseExist()){

                                    System.out.println("checking store 8");

                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                                    final String currentDateandTime1 = sdf2.format(new Date());
                                    String date = "00", year = "0000", month = "00";

//                                    db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
                                    Cursor cursor = db_inapp.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'credentialstime'", null);
                                    if(cursor!=null) {
                                        if(cursor.getCount()>0) {


                                            System.out.println("checking store 9");

                                            String textcompanyname = "", storeitem = "", deviceitem = "", compemailid = "";

                                            Cursor cursor_cred = db_inapp.rawQuery("SELECT * FROM credentialstime", null);
                                            if (cursor_cred.moveToFirst()) {
                                                textcompanyname = cursor_cred.getString(6);
                                                storeitem = cursor_cred.getString(7);
                                                deviceitem = cursor_cred.getString(8);
                                                compemailid = cursor_cred.getString(5);
                                            }
                                            cursor_cred.close();

                                            finalTextcompanyname = textcompanyname;
                                            finalStoreitem = storeitem;
                                            finalDeviceitem = deviceitem;
                                            finalCompemailid = compemailid;

                                            Cursor cursor1 = db_inapp.rawQuery("SELECT * FROM credentialstime", null);
                                            if (cursor1.moveToFirst()) {
                                                date = cursor1.getString(9);//22mar2018   }
                                            }
                                            cursor1.close();

                                            System.out.println("checking store 10 "+account_selection);


//                                            Cursor cursor2 = db_inapp.rawQuery("SELECT * FROM Messaginglicense ", null);
//                                            if (cursor2.moveToFirst()) {
//                                                String one = cursor2.getString(1);
//                                                System.out.println("checking store 11 "+one);
//                                            }
//                                            cursor2.close();

                                            // final String da = "20181020"; //yyyymmdd
                                            if (date != null) {
                                                final String da = date; //yyyymmdd
                                                final int intdate = Integer.parseInt(currentDateandTime1);

                                                if (intdate <= Integer.parseInt(da)) {

                                                    if (account_selection.toString().equals("Dine") || account_selection.toString().equals(getString(R.string.app_name))) {
                                                        Intent intent = new Intent(Checking_Store.this, MainActivity.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                        intent.putExtra("from","checking");
                                                     //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                        finish();
                                                    }else {
                                                        if (account_selection.toString().equals("Qsr")) {
                                                            Intent intent = new Intent(Checking_Store.this, MainActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                            intent.putExtra("from","checking");
                                                        //    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                        }else {
                                                            Intent intent = new Intent(Checking_Store.this, MainActivity_Retail.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                            intent.putExtra("from","checking");
                                                          //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }

//                                                    Intent intent = new Intent(Checking_Store.this, MainActivity.class);
//                                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                                    intent.putExtra("from","checking");
//                                                    startActivity(intent);
//                                                    finish();

                                                } else {

                                                    String id = "0";

                                                    Cursor cursor2 = db1.rawQuery("SELECT * FROM billnumber", null);
                                                    if (cursor2.moveToLast()) {
                                                        id = cursor2.getString(0);
                                                        System.out.println("billnumber count is "+id);
//                                                        Toast.makeText(Checking_Store.this, "billnumber count is "+id, Toast.LENGTH_SHORT).show();
                                                    }
                                                    cursor2.close();

                                                    if (Integer.parseInt(id) < 1000) {
                                                        if (account_selection.toString().equals("Dine") || account_selection.toString().equals(getString(R.string.app_name))) {
                                                            Intent intent = new Intent(Checking_Store.this, MainActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                            intent.putExtra("from","splash");
                                                            startActivity(intent);
                                                        }else {
                                                            if (account_selection.toString().equals("Qsr")) {
                                                                Intent intent = new Intent(Checking_Store.this, MainActivity.class);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                                intent.putExtra("from","splash");
                                                                startActivity(intent);
                                                            }else {
                                                                Intent intent = new Intent(Checking_Store.this, MainActivity_Retail.class);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                                intent.putExtra("from","splash");
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    }else {
                                                        Intent intent = new Intent(Checking_Store.this, MainActivity_subscription.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                        intent.putExtra("emailid", finalCompemailid);
                                                        intent.putExtra("storename", finalStoreitem);
                                                        intent.putExtra("devicename", finalDeviceitem);
                                                        intent.putExtra("companyname", finalTextcompanyname);
                                                        intent.putExtra("from", "checking");
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);

                                                    }
                                                }
                                            }

                                        }
                                        cursor.close();
                                    }


                                }else{

                                    Intent intent = new Intent(Checking_Store.this, MainActivity_subscription.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    intent.putExtra("emailid", email);
                                    intent.putExtra("storename", storeBean.getStoreName());
                                    intent.putExtra("devicename", deviceitem);
                                    intent.putExtra("companyname", company);
                                    intent.putExtra("from", "checking");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                }






                            }else{
                                //    progressBar_license.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            System.out.println("checking store "+e);
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm checking store", "Error: " + error.getMessage());
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

    private void setAccountSyncable() {
        if (ContentResolver.getIsSyncable(mAccount, AUTHORITY) == 0) {
            ContentResolver.setIsSyncable(mAccount, ACCOUNT, 1);
        }
    }
    private void setAccountSyncable1() {
        if (ContentResolver.getIsSyncable(mAccount1, AUTHORITY1) == 0) {
            ContentResolver.setIsSyncable(mAccount1, ACCOUNT1, 1);
        }
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
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

    public void onBackPressed() {
        // Navigate back to the main activity
        Intent intent = new Intent(this, MainActivity_Signin_OTPbased.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        R_NewLogIn_BusinessType.storeList.clear();
        MainActivity_Signin_OTPbased.storeList.clear();


        finish();
    }

    @Override
    public void onProgressUpdateSales(int progress) {

        updateBar.setVisibility(View.VISIBLE);
        Log.e("salesdata progress",progress+"");
        progSales=progress;
        updateBar.setProgress(prog+progSales);

        float perc= ((float)updateBar.getProgress()/(float)updateBar.getMax())*100;
        int p=(int) perc;
        tv_perc.setText(p+"%");


    }

    public class FetchDataTask extends AsyncTask<String, Void, Boolean> {


        @Override
        protected void onPreExecute() {

        }
        protected Boolean doInBackground(final String... args){

            return  null;

        }

        @Override
        protected void onPostExecute(final Boolean success)	{

        }
    }


    public boolean databaseExist() {
        File DATA_DIRECTORY_DATABASE =
                new File(Environment.getDataDirectory() +
                        "/data/" + "com.intuition.ivepos" +
                        "/databases/" + "amazoninapp");

        return DATA_DIRECTORY_DATABASE.exists();
    }

    class DeleteSalesData extends AsyncTask<String, Void, Integer> {




        @Override
        protected Integer doInBackground(String... strings) {
            if(db1!=null){
                db1.close();
            }
            if(db!=null){
                db.close();
            }
            db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            //   db1=  SQLiteDatabase.openDatabase("mydb_Salesdata", null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);

            Cursor c = db1.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            if (c.moveToFirst()) {
                while ( !c.isAfterLast() ) {
                    String tablename=c.getString(0);
                    db1.execSQL("delete from "+ tablename);
                    c.moveToNext();
                }
            }
            c.close();
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            Intent intent1 = new Intent(Checking_Store.this, MyServiceSales.class);
            ContextCompat.startForegroundService(Checking_Store.this, intent1);

            // startService(intent);
        }
    }

    class syncTask extends AsyncTask<String, Void, Integer> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SyncDatabase syncdatabase=new SyncDatabase();
            syncdatabase.updateSyncDb(getApplicationContext());

        }

        @Override
        protected Integer doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            salesdataBool=true;
            //  while (true){
            if(salesdataBool&&appdataBool){
                getlicenses();
                //   break;
            }

            //  }


            // getlicenses();

        }
    }


    class syncTaskApp extends AsyncTask<String, Void, Integer> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SyncDatabase syncdatabase=new SyncDatabase();
            syncdatabase.updateSyncDbApp(getApplicationContext());

        }

        @Override
        protected Integer doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            appdataBool=true;

            //  while (true){
            if(salesdataBool&&appdataBool){
                getlicenses();
                //   break;
            }




        }
    }


    class DeleteData extends AsyncTask<String, Void, Integer> {


        @Override
        protected Integer doInBackground(String... strings) {
            db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            if (c.moveToFirst()) {
                while ( !c.isAfterLast() ) {
                    String tablename=c.getString(0);
//                    runOnUiThread(new Runnable(){
//
//                        @Override
//                        public void run(){
//                            Toast.makeText(Checking_Store.this, " "+tablename, Toast.LENGTH_SHORT).show();
//                        }
//                    });
                    db.execSQL("delete from "+ tablename);
                    c.moveToNext();
                }
            }
            c.close();
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            appdataBool=false;

            Intent intent = new Intent(Checking_Store.this, MyServiceAppData.class);
            ContextCompat.startForegroundService(Checking_Store.this, intent);



            //startDownload();
           //  dump();


            //  startService(intent);
        }
    }

    private void startDownload() {

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
        String store= sharedpreferences.getString("storename", null);
        String device= sharedpreferences.getString("devicename", null);

        JSONObject params = new JSONObject();

        try {
            params.put("device",device);
            params.put("store",store);
            params.put("company",company);
            params.put("date1",newDate1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  if(queue==null){
        queue = Volley.newRequestQueue(Checking_Store.this);
        // }

        JsonObjectRequest sr = new JsonObjectRequest(
                Request.Method.POST,
                WebserviceUrl+"countrows.php",params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responseString) {
                        Log.e("myserviceapp","2");
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

                            try {
                                int total= jsonObject.getInt("total");
                                int maxcount=(total/10);
                              //  updateBar.setMax(maxcount+140);
                                Intent intent = new Intent(Checking_Store.this, MyServiceApp.class);
                                ContextCompat.startForegroundService(Checking_Store.this, intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else{
                            Toast.makeText(Checking_Store.this, "download failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
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

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            SharedPreferences sharedpreferences = getSharedPreferences("downloadpref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
           // editor.putString("download", "");
            editor.putString("downloadsales", "");
            editor.commit();

            new syncTask().execute();

        }
    };


    private BroadcastReceiver receiverapp = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            SharedPreferences sharedpreferences = getSharedPreferences("downloadpref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("download", "");
          //  editor.putString("downloadsales", "");
            editor.commit();
            new syncTaskApp().execute();

        }
    };



    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, new IntentFilter(
                    "com.intuition.ivepos.Checking_Store.receiver"), RECEIVER_EXPORTED);
        }else {
            registerReceiver(receiver, new IntentFilter(
                    "com.intuition.ivepos.Checking_Store.receiver"));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiverapp, new IntentFilter(
                    "com.intuition.ivepos.Checking_Store.receiverapp"), RECEIVER_EXPORTED);
        }else {
            registerReceiver(receiverapp, new IntentFilter(
                    "com.intuition.ivepos.Checking_Store.receiverapp"));
        }

        SharedPreferences sharedpreferences = getSharedPreferences("downloadpref", Context.MODE_PRIVATE);
       if(sharedpreferences.contains("download")){

           String download = sharedpreferences.getString("download", null);
           if(download.equalsIgnoreCase("complete")){

               SharedPreferences.Editor editor = sharedpreferences.edit();
               editor.putString("download", "");
               editor.commit();
               new syncTaskApp().execute();

           }
           String downloadsales = sharedpreferences.getString("downloadsales", null);
           if(downloadsales.equalsIgnoreCase("complete")){

               SharedPreferences.Editor editor = sharedpreferences.edit();
               editor.putString("downloadsales", "");
               editor.commit();
               new syncTask().execute();

           }


       }else{
           SharedPreferences.Editor editor = sharedpreferences.edit();
           editor.putString("download", "");
           editor.putString("downloadsales", "");
           editor.commit();
       }



    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        unregisterReceiver(receiverapp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyServiceApp.setOnProgressChangedListener(null);
        if (i_close == 1){

        }else {
            db.close();
            db_inapp.close();
        }
        System.out.println("checking store 12");
    }
}

