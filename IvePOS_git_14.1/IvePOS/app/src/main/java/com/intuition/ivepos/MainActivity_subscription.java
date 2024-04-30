package com.intuition.ivepos;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import static com.intuition.ivepos.Constants.SKU_DELAROY_MONTHLY;
import static com.intuition.ivepos.Constants.SKU_DELAROY_SIXMONTH;
import static com.intuition.ivepos.Constants.SKU_DELAROY_YEARLY;
import static com.intuition.ivepos.Constants.base64EncodedPublicKey;
import static com.intuition.ivepos.Constants_Inventory.SKU_DELAROY_PRO_UPGRADE;
import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

public class MainActivity_subscription extends AppCompatActivity{
    static final String TAG = "MainActivity";
    String checking;
    int i;
    String mSelectedSubscriptionPeriod = "";
    static final int RC_REQUEST = 10001;
    IabHelper mHelper;
    SQLiteDatabase db = null;
    SQLiteDatabase db_appda = null;
    String  textcompanyname, storeitem, deviceitem, email, from;
    private boolean mSubscribedToDelaroy=false;
    RequestQueue requestQueue;
    RelativeLayout progressbar1;
    boolean isCouponValid=false;

    String WebserviceUrl;
    String account_selection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.subscribe_layout);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(MainActivity_subscription.this);
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

        progressbar1=findViewById(R.id.progressbar1);
        db = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
        db_appda = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE if not exists credentialstime (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, orderid text,time text,date text,trial_time text, " +
                "email_id text, company_name text, store_name text, dev_name text, todate text);");
        db.execSQL("CREATE TABLE if not exists Pro_upgrade (_id integer PRIMARY KEY UNIQUE, status text, orderid text);");
        db = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
        Cursor co31 = db.rawQuery("SELECT * FROM Pro_upgrade", null);
        int cou31 = co31.getColumnCount();
        if (String.valueOf(cou31).equals("3")) {
            db.execSQL("ALTER TABLE Pro_upgrade ADD COLUMN pro_expiry");
        }
        co31.close();
        Bundle extras = getIntent().getExtras();
        from = extras.getString("from");
        if((from!=null)&&(from.equalsIgnoreCase("checking"))){

            textcompanyname = extras.getString("companyname");
            email = extras.getString("emailid");
            storeitem = extras.getString("storename");
            deviceitem = extras.getString("devicename");

        }else if((from!=null)&&(from.equalsIgnoreCase("register"))){

            textcompanyname = extras.getString("companyname");
            email = extras.getString("emailid");
            storeitem = extras.getString("storename");
            deviceitem = extras.getString("devicename");

        }
        System.out.println("textcompanyname11...." + textcompanyname);
        System.out.println("email11...." + email);
        System.out.println("storeitem11...." + storeitem);
        System.out.println("deviceitem11...." + deviceitem);

        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.enableDebugLogging(true);
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result)
            {
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app Billing setup failed: " +
                            result);
                } else {
                    Log.d(TAG, "In-app Billing is set up OK");
                }
            }
        });

        ImageView refresh = (ImageView) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getlicenses();
                Toast.makeText(MainActivity_subscription.this, "click", Toast.LENGTH_LONG).show();
            }
        });

        ImageView contact_support = (ImageView) findViewById(R.id.contact_support);
        contact_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity_subscription.this);
                alertDialog.setTitle(getString(R.string.title24));
                alertDialog.setMessage("For renewal/purchase contact \n+91 80-4719 0010\n+91 80-4300 8549\n+91-9986688896\n+91-8971030828");

                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });

                alertDialog.show();
            }
        });

        Button free_trail_button = (Button) findViewById(R.id.free_trail_button);

//        Cursor cursor = db_appda.rawQuery("select DISTINCT name from sqlite_sequence where name = 'Alaramdays'", null);
////        if(cursor!=null) {
//            if (cursor.getCount() > 0) {
//                cursor.close();
//
//                Cursor cursor_cred = db.rawQuery("SELECT * FROM credentialstime", null);
//                if (cursor_cred.moveToFirst()){
//                    free_trail_button.setVisibility(View.GONE);
//                }else {
//                    free_trail_button.setVisibility(View.VISIBLE);
//                }
//            }else {
//                free_trail_button.setVisibility(View.VISIBLE);
//            }
////        }else {
////            free_trail_button.setVisibility(View.VISIBLE);
////        }

        db_appda.execSQL("CREATE TABLE if not exists Freetrail (_id integer PRIMARY KEY UNIQUE, status text);");

        Cursor cursor = db_appda.rawQuery("select * from Freetrail", null);
        if (cursor.moveToFirst()){
            String st = cursor.getString(1);
//            Toast.makeText(MainActivity_subscription.this, "click0", Toast.LENGTH_LONG).show();
            if (st.toString().equals("Activated")) {
                free_trail_button.setVisibility(View.GONE);
            }else {
//                Toast.makeText(MainActivity_subscription.this, "click1", Toast.LENGTH_LONG).show();
            }
        }else {
//            Toast.makeText(MainActivity_subscription.this, "click2", Toast.LENGTH_LONG).show();
        }
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {
            if (result.isFailure()) {

            } else {
                Purchase purchase = inventory.getPurchase(mSelectedSubscriptionPeriod);
                mSubscribedToDelaroy=true;
                checking=purchase.getOrderId();
                updateSubscription();
            }
        }
    };

    private void updateSubscription() {
        Cursor cursor_cred = db.rawQuery("SELECT * FROM credentialstime", null);
        if (cursor_cred.moveToFirst()) {

            textcompanyname = cursor_cred.getString(6);
            storeitem = cursor_cred.getString(7);
            deviceitem = cursor_cred.getString(8);
            email =  cursor_cred.getString(5);

        }
        SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyyyy");
        final String normal1 = normal2.format(new Date());

        Date dt = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
        final String time1 = sdf1.format(dt);

        ContentValues contentValues = new ContentValues();
        contentValues.put("orderid", checking);
        contentValues.put("time", time1);
        contentValues.put("date", normal1);
        if (i == 0) {
            contentValues.put("trial_time", "30days");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Calendar currentCal = Calendar.getInstance();
            String currentdate = dateFormat.format(currentCal.getTime());
            currentCal.add(Calendar.DATE, 30);
            String toDate = dateFormat.format(currentCal.getTime());

            contentValues.put("todate", toDate);

            Cursor cursor_pro=db.rawQuery("SELECT * FROM Pro_upgrade", null);
            if(cursor_pro.moveToFirst()){

                int id=cursor_pro.getInt(0);
                String status=cursor_pro.getString(1);
                if(!status.equalsIgnoreCase("Activated")){
                    ContentValues contentValues1 =new ContentValues();
                    contentValues1.put("status","Activated");
                    contentValues1.put("orderid","");
                    contentValues1.put("pro_expiry", toDate);
                    String where = "_id = '" + id + "'";
                    db.update("Pro_upgrade", contentValues1, where, new String[]{});
                }

            }else{

                ContentValues contentValues1 =new ContentValues();
                contentValues1.put("status","Activated");
                contentValues1.put("orderid","");
                contentValues1.put("pro_expiry", toDate);
                db.insert("Pro_upgrade", null, contentValues1);
            }
            cursor_pro.close();

        } else if (i == 1) {
            contentValues.put("trial_time", "6months");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Calendar currentCal = Calendar.getInstance();
            String currentdate = dateFormat.format(currentCal.getTime());
            currentCal.add(Calendar.DATE, 183);
            String toDate = dateFormat.format(currentCal.getTime());

            contentValues.put("todate", toDate);

            Cursor cursor_pro=db.rawQuery("SELECT * FROM Pro_upgrade", null);
            if(cursor_pro.moveToFirst()){

                int id=cursor_pro.getInt(0);
                String status=cursor_pro.getString(1);
                if(!status.equalsIgnoreCase("Activated")){
                    ContentValues contentValues1 =new ContentValues();
                    contentValues1.put("status","Activated");
                    contentValues1.put("orderid","");
                    contentValues1.put("pro_expiry", toDate);
                    String where = "_id = '" + id + "'";
                    db.update("Pro_upgrade", contentValues1, where, new String[]{});
                }

            }else{

                ContentValues contentValues1 =new ContentValues();
                contentValues1.put("status","Activated");
                contentValues1.put("orderid","");
                contentValues1.put("pro_expiry", toDate);
                db.insert("Pro_upgrade", null, contentValues1);
            }
            cursor_pro.close();

        }
        else if (i == 2) {
            contentValues.put("trial_time", "1year");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Calendar currentCal = Calendar.getInstance();
            String currentdate = dateFormat.format(currentCal.getTime());
            currentCal.add(Calendar.DATE, 365);
            String toDate = dateFormat.format(currentCal.getTime());

            contentValues.put("todate", toDate);

            Cursor cursor_pro=db.rawQuery("SELECT * FROM Pro_upgrade", null);
            if(cursor_pro.moveToFirst()){

                int id=cursor_pro.getInt(0);
                String status=cursor_pro.getString(1);
                if(!status.equalsIgnoreCase("Activated")){
                    ContentValues contentValues1 =new ContentValues();
                    contentValues1.put("status","Activated");
                    contentValues1.put("orderid","");
                    contentValues1.put("pro_expiry", toDate);
                    String where = "_id = '" + id + "'";
                    db.update("Pro_upgrade", contentValues1, where, new String[]{});
                }

            }else{

                ContentValues contentValues1 =new ContentValues();
                contentValues1.put("status","Activated");
                contentValues1.put("orderid","");
                contentValues1.put("pro_expiry", toDate);
                db.insert("Pro_upgrade", null, contentValues1);
            }
            cursor_pro.close();


        }else if (i == 3){
            mSubscribedToDelaroy=true;
            mSelectedSubscriptionPeriod = SKU_DELAROY_YEARLY;

            contentValues.put("trial_time", "1year");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Calendar currentCal = Calendar.getInstance();
            String currentdate = dateFormat.format(currentCal.getTime());
            currentCal.add(Calendar.DATE, 365);
            String toDate = dateFormat.format(currentCal.getTime());

            contentValues.put("todate", toDate);

        }else if (i == 4){
            mSubscribedToDelaroy=true;
            mSelectedSubscriptionPeriod = SKU_DELAROY_MONTHLY;
            contentValues.put("trial_time", "7days");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Calendar currentCal = Calendar.getInstance();
            String currentdate = dateFormat.format(currentCal.getTime());
            currentCal.add(Calendar.DATE, 7);
            String toDate = dateFormat.format(currentCal.getTime());

            contentValues.put("todate", toDate);
        }else if (i == 5){

            mSelectedSubscriptionPeriod=SKU_DELAROY_PRO_UPGRADE;

            Cursor cursor_pro=db.rawQuery("SELECT * FROM Pro_upgrade", null);
            if(cursor_pro.moveToFirst()){

                int id=cursor_pro.getInt(0);
                String status=cursor_pro.getString(1);
                if(!status.equalsIgnoreCase("Activated")){
                    ContentValues contentValues1 =new ContentValues();
                    contentValues1.put("status","Activated");
                    contentValues1.put("orderid","");
                    String where = "_id = '" + id + "'";
                    db.update("Pro_upgrade", contentValues1, where, new String[]{});
                }

            }else{

                ContentValues contentValues1 =new ContentValues();
                contentValues1.put("status","Activated");
                contentValues1.put("orderid","");
                db.insert("Pro_upgrade", null, contentValues1);
            }
            cursor_pro.close();
        }

        contentValues.put("email_id", email);
        contentValues.put("company_name", textcompanyname);
        contentValues.put("store_name", storeitem);
        contentValues.put("dev_name", deviceitem);
        if(storeitem.contains("-"+email)){
            storeitem=storeitem.replace("-"+email,"");
        }

        Cursor cursor7=null;
        cursor7 = db.rawQuery("SELECT * FROM credentialstime where store_name ='"+storeitem+"'"+" and dev_name ='"+deviceitem+"'", null);
        if(cursor7!=null) {
            if (cursor7.moveToFirst()) {
                do {
                    String id = cursor7.getString(0);
                    String where = "_id = " + id;
                    db.update("credentialstime", contentValues, where, new String[]{});
                    update();
                } while (cursor7.moveToNext());
            }else{
                db.insert("credentialstime", null, contentValues);

                update();

            }
        }else{
            db.insert("credentialstime", null, contentValues);

            update();

        }
    }

    public void onCouponCodeButtonClicked(View arg0) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity_subscription.this);
        alertDialog.setTitle(getString(R.string.title13));
        alertDialog.setMessage(getString(R.string.setmessage15));
        final EditText input = new EditText(MainActivity_subscription.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Submit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String code = input.getText().toString();

                        checking=code;
                        mSubscribedToDelaroy=true;
                        checkCouponCode(code);

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


    public void onFreeTrailButtonClicked(View arg0) {
        checking="7Days";
        mSubscribedToDelaroy=true;
//        mSelectedSubscriptionPeriod = SKU_DELAROY_MONTHLY;
        Cursor cursor_cred = db.rawQuery("SELECT * FROM credentialstime", null);
        if (cursor_cred.moveToFirst()) {

            textcompanyname = cursor_cred.getString(6);
            storeitem = cursor_cred.getString(7);
            deviceitem = cursor_cred.getString(8);
            email =  cursor_cred.getString(5);

        }
        SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyyyy");
        final String normal1 = normal2.format(new Date());

        Date dt = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
        final String time1 = sdf1.format(dt);

        ContentValues contentValues = new ContentValues();
        contentValues.put("orderid", checking);
        contentValues.put("time", time1);
        contentValues.put("date", normal1);

        contentValues.put("trial_time", "7days");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar currentCal = Calendar.getInstance();
        String currentdate = dateFormat.format(currentCal.getTime());
        currentCal.add(Calendar.DATE, 7);
        String toDate = dateFormat.format(currentCal.getTime());

        contentValues.put("todate", toDate);


        contentValues.put("email_id", email);
        contentValues.put("company_name", textcompanyname);
        contentValues.put("store_name", storeitem);
        contentValues.put("dev_name", deviceitem);
        if(storeitem.contains("-"+email)){
            storeitem=storeitem.replace("-"+email,"");
        }

        Cursor cursor7=null;
        cursor7 = db.rawQuery("SELECT * FROM credentialstime where store_name ='"+storeitem+"'"+" and dev_name ='"+deviceitem+"'", null);
        if(cursor7!=null) {
            if (cursor7.moveToFirst()) {
                do {
                    String id = cursor7.getString(0);
                    String where = "_id = " + id;
                    db.update("credentialstime", contentValues, where, new String[]{});
                    update();
                } while (cursor7.moveToNext());
            }else{
                db.insert("credentialstime", null, contentValues);

                update();

            }
        }else{
            db.insert("credentialstime", null, contentValues);

            update();

        }

    }


    public void onSubscribeButtonClicked(View arg0) {

        if (!mHelper.subscriptionsSupported()) {
            complain("Subscriptions not supported on your device yet. Sorry!");
            return;
        }

        CharSequence[] options;
        options = new CharSequence[3];
        options[0] = getString(R.string.subscription_period_monthly_new);
        options[1] = getString(R.string.subscription_period_sixmonth_new);
        options[2] = getString(R.string.subscription_period_yearly_new);

        int titleResId;
        titleResId = R.string.subscription_period_prompt;

        mSelectedSubscriptionPeriod = SKU_DELAROY_MONTHLY;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titleResId)
                .setSingleChoiceItems(options, 0 /* checkedItem */, oncicklistener)
                .setPositiveButton(R.string.subscription_prompt_continue, oncicklistener)
                .setNegativeButton(R.string.subscription_prompt_cancel, oncicklistener);
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    DialogInterface.OnClickListener oncicklistener=new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface dialog, int id) {

            if (id == 0 /* First choice item */) {
                i=0;
                mSelectedSubscriptionPeriod = SKU_DELAROY_MONTHLY;
            } else if (id == 1 /* Second choice item */) {
                i=1;
                mSelectedSubscriptionPeriod = SKU_DELAROY_SIXMONTH;
            }  else if (id == 2/* Second choice item */) {
                i=2;
                mSelectedSubscriptionPeriod = SKU_DELAROY_YEARLY;
            }else if (id == DialogInterface.BUTTON_POSITIVE /* continue button */) {

//                if(i==2){
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity_subscription.this);
//                    alertDialog.setTitle(getString(R.string.title13));
//                    alertDialog.setMessage(getString(R.string.setmessage15));
//                    final EditText input = new EditText(MainActivity_subscription.this);
//                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.MATCH_PARENT,
//                            LinearLayout.LayoutParams.MATCH_PARENT);
//                    input.setLayoutParams(lp);
//                    alertDialog.setView(input);
//
//                    alertDialog.setPositiveButton("Submit",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    String code = input.getText().toString();
//
//                                    checking=code;
//                                    mSubscribedToDelaroy=true;
//                                    checkCouponCode(code);
//
//                                }
//                            });
//
//                    alertDialog.setNegativeButton("NO",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            });
//                    alertDialog.show();
//
//                }else {
                    try {
                        mHelper.launchSubscriptionPurchaseFlow(MainActivity_subscription.this, mSelectedSubscriptionPeriod, RC_REQUEST,
                                mPurchaseFinishedListener, mSelectedSubscriptionPeriod);
                    } catch (IabHelper.IabAsyncInProgressException e) {

                    }
//                }
            } else if (id == DialogInterface.BUTTON_NEGATIVE) {
                dialog.cancel();
            }
        }
    };

    private void checkCouponCode(final String code) {

        String webservice = "coupon.php";
        String add_sec = code.substring(0, 2);
        if (add_sec.toString().equalsIgnoreCase("1y")){
            i = 3;
            webservice = "coupon_1y.php";
        }else {
            if (add_sec.toString().equalsIgnoreCase("7d")) {
                i = 4;
                webservice = "coupon_7d.php";
            }else {
                if (add_sec.toString().equalsIgnoreCase("pr")) {
                    i = 5;
                    webservice = "coupon_pr.php";
                }else {
                    webservice = "coupon.php";
                }
            }
        }

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+""+webservice,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")) {
                            updateSubscription();

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
                params.put("coupon", code+ "");
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        if (mHelper == null) return;
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(mSelectedSubscriptionPeriod)) {
                consumeItem();
            }
        }
    };
    public void consumeItem() {
        try {
            mHelper.queryInventoryAsync(mReceivedInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }

    public void updateUi() {
        if (mSubscribedToDelaroy) {

            if (account_selection.toString().equals("Dine") || account_selection.toString().equals(getString(R.string.app_name))) {
                Intent intent = new Intent(MainActivity_subscription.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                if (account_selection.toString().equals("Qsr")) {
                    Intent intent = new Intent(MainActivity_subscription.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(MainActivity_subscription.this, MainActivity_Retail.class);
                    startActivity(intent);
                    finish();
                }
            }

//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            finish();
        } else {

        }
    }
    void complain(String message) {
        Log.e(TAG, "**** Delaroy Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateUi();
            }
        });
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();

    }
    private void update() {

        if (i == 5){
            prolicense("");
        }else {
            poslicense(checking);
        }
    }



    public void poslicense(final String orderid){

        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        final String company= sharedpreferences.getString("companyname", null);
        final String store= sharedpreferences.getString("storename", null);
        final String device= sharedpreferences.getString("devicename", null);

        progressbar1.setVisibility(View.VISIBLE);
        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"pospurchase.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")){



                            TextView from1 = new TextView(MainActivity_subscription.this);
                            from1.setText(from);

                            Cursor cursor_cred = db.rawQuery("SELECT * FROM credentialstime", null);
                            if (cursor_cred.moveToFirst()) {
                                do{
                                    textcompanyname = cursor_cred.getString(6);
                                    storeitem = cursor_cred.getString(7);
                                    deviceitem = cursor_cred.getString(8);
                                    email =  cursor_cred.getString(5);
                                }while (cursor_cred.moveToNext());
                            }
                            updateUi();
                            //    progressbar1.setVisibility(View.GONE);

                        }else{
                            //    progressbar1.setVisibility(View.GONE);
                            TextView from1 = new TextView(MainActivity_subscription.this);
                            from1.setText(from);

                            Cursor cursor_cred = db.rawQuery("SELECT * FROM credentialstime", null);
                            if (cursor_cred.moveToFirst()) {
                                do{
                                    textcompanyname = cursor_cred.getString(6);
                                    storeitem = cursor_cred.getString(7);
                                    deviceitem = cursor_cred.getString(8);
                                    email =  cursor_cred.getString(5);
                                }while (cursor_cred.moveToNext());
                            }
                            updateUi();

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
                params.put("subscription",mSelectedSubscriptionPeriod);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

    }


    public void prolicense(final String orderid){

        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        final String company= sharedpreferences.getString("companyname", null);
        final String store= sharedpreferences.getString("storename", null);
        final String device= sharedpreferences.getString("devicename", null);

//        bar.setVisibility(View.VISIBLE);
        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"propurchase.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")){

//                            bar.setVisibility(View.GONE);
//                            updateUi();

                        }else{

//                            bar.setVisibility(View.GONE);
//                            updateUi();
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
                params.put("orderid","");
                params.put("subscription",mSelectedSubscriptionPeriod);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void getlicenses(){

        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        final String company= sharedpreferences.getString("companyname", null);
        final String store= sharedpreferences.getString("storename", null);
        final String device= sharedpreferences.getString("devicename", null);
        final String email=sharedpreferences.getString("emailid",null);

        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"getlicense.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            String status=jsonObject.getString("status");

                            if(status.equalsIgnoreCase("success")){

                                //    progressBar_license.setVisibility(View.GONE);

                                String postodate=jsonObject.getString("postodate");
                                String dashboardexpiry=jsonObject.getString("dashboardexpiry");
                                String remaining_msgs=jsonObject.getString("remaining_msgs");
                                String remaining_orders=jsonObject.getString("remaining_orders");
                                String proactivated=jsonObject.getString("proactivated");



                                SQLiteDatabase db = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);


                                db.execSQL("CREATE TABLE if not exists credentialstime (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, orderid text,time text,date text,trial_time text, " +
                                        "email_id text, company_name text, store_name text, dev_name text, todate text);");
                                db.execSQL("delete from Messaginglicense");
                                db.execSQL("delete from Orderlicense");
                                db.execSQL("delete from Pro_upgrade");
                                db.execSQL("delete from credentialstime");
                                db.execSQL("CREATE TABLE if not exists credentialstime (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, orderid text,time text,date text,trial_time text, " +
                                        "email_id text, company_name text, store_name text, dev_name text, todate text);");

                                ContentValues contentValues=new ContentValues();
                                contentValues.put("email_id", email);
                                contentValues.put("company_name", company);
                                contentValues.put("store_name", store);
                                contentValues.put("dev_name", deviceitem);

                                String[] separated = postodate.split(" ");
                                String convertedDate=separated[0].replace("-","");


                                Log.e("converted pos date",convertedDate);
                                contentValues.put("todate", convertedDate);
                                db.insert("credentialstime", null, contentValues);

                                db.execSQL("CREATE TABLE if not exists Pro_upgrade (_id integer PRIMARY KEY UNIQUE, status text, orderid text);");
                                db = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
                                Cursor co31 = db.rawQuery("SELECT * FROM Pro_upgrade", null);
                                int cou31 = co31.getColumnCount();
                                if (String.valueOf(cou31).equals("3")) {
                                    db.execSQL("ALTER TABLE Pro_upgrade ADD COLUMN pro_expiry");
                                }
                                co31.close();
                                ContentValues cv = new ContentValues();
                                if(proactivated.equalsIgnoreCase("NO")){
                                    cv.put("status", "Not Activated");
                                    cv.put("pro_expiry", convertedDate);
                                    db.insert("Pro_upgrade", null, cv);
                                }else{
                                    cv.put("status", "Activated");
                                    cv.put("pro_expiry", convertedDate);
                                    db.insert("Pro_upgrade", null, cv);
                                }

                                db.execSQL("CREATE TABLE if not exists Messaginglicense (_id integer PRIMARY KEY UNIQUE, remainingmessages text, Messagessent text,orderid text,time text,date text, package text);");
                                ContentValues cv1 = new ContentValues();
                                cv1.put("Messagessent", "0");
                                cv1.put("remainingmessages", Integer.parseInt(remaining_msgs));
                                cv1.put("date", "");
                                cv1.put("time", "");
                                db.insert("Messaginglicense", null, cv1);


                                db.execSQL("CREATE TABLE if not exists Orderlicense (_id integer PRIMARY KEY UNIQUE, remainingorders text, ordersrece text,orderid text,time text,date text, package text);");
                                ContentValues cv2 = new ContentValues();
                                cv2.put("ordersrece", "0");
                                cv2.put("remainingorders", Integer.parseInt(remaining_orders));
                                cv2.put("date", "");
                                cv2.put("time", "");
                                db.insert("Orderlicense", null, cv2);


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



                                if(databaseExist()){

                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                                    final String currentDateandTime1 = sdf2.format(new Date());
                                    String date = "00", year = "0000", month = "00";

                                    SQLiteDatabase db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
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

                                            String finalTextcompanyname = textcompanyname;
                                            String finalStoreitem = storeitem;
                                            String finalDeviceitem = deviceitem;
                                            String finalCompemailid = compemailid;

                                            Cursor cursor1 = db_inapp.rawQuery("SELECT * FROM credentialstime", null);
                                            if (cursor1.moveToFirst()) {
                                                do {
                                                    date = cursor1.getString(9);//22mar2018   }
                                                }while (cursor1.moveToNext());
                                            }

                                            // final String da = "20181020"; //yyyymmdd
                                            if (date != null) {
                                                final String da = date; //yyyymmdd
                                                final int intdate = Integer.parseInt(currentDateandTime1);

                                                if (intdate <= Integer.parseInt(da)) {

                                                    if (account_selection.toString().equals("Dine") || account_selection.toString().equals(getString(R.string.app_name))) {
                                                        Intent intent = new Intent(MainActivity_subscription.this, MainActivity.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                        intent.putExtra("from","checking");
                                                        startActivity(intent);
                                                        finish();
                                                    }else {
                                                        if (account_selection.toString().equals("Qsr")) {
                                                            Intent intent = new Intent(MainActivity_subscription.this, MainActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                            intent.putExtra("from","checking");
                                                            startActivity(intent);
                                                            finish();
                                                        }else {
                                                            Intent intent = new Intent(MainActivity_subscription.this, MainActivity_Retail.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                            intent.putExtra("from","checking");
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }

//                                                    Intent intent = new Intent(MainActivity_subscription.this, MainActivity.class);
//                                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                                    intent.putExtra("from","checking");
//                                                    startActivity(intent);
//                                                    finish();

                                                } else {


                                                    Intent intent = new Intent(MainActivity_subscription.this, MainActivity_subscription.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                    intent.putExtra("emailid", finalCompemailid);
                                                    intent.putExtra("storename", finalStoreitem);
                                                    intent.putExtra("devicename", finalDeviceitem);
                                                    intent.putExtra("companyname", finalTextcompanyname);
                                                    intent.putExtra("from", "checking");
                                                    startActivity(intent);
                                                }
                                            }

                                        }
                                        cursor.close();
                                    }


                                }






                            }else{
                                //    progressBar_license.setVisibility(View.GONE);
                            }

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

    public boolean databaseExist() {
        File DATA_DIRECTORY_DATABASE =
                new File(Environment.getDataDirectory() +
                        "/data/" + "com.intuition.ivepos" +
                        "/databases/" + "amazoninapp");

        return DATA_DIRECTORY_DATABASE.exists();
    }

}