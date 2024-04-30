package com.intuition.ivepos;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.amplifyframework.core.Amplify;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.intuition.ivepos.languagelocale.LocaleUtils;
import com.intuition.ivepos.syncapp.StubProviderApp;
import com.intuition.ivepos.syncdb.MyServiceApp_swiperefresh_sync;
import com.intuition.ivepos.syncdb.SyncDatabase;

import org.json.JSONException;
import org.json.JSONObject;


public class SettingsActivity extends AppCompatActivity implements MyServiceApp_swiperefresh_sync.OnProgressUpdateListener {

    Spinner auto_connect;
    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;
    Uri contentUri,resultUri;

    ProgressDialog progressDialog;
    ProgressBar updateBar;
    boolean reachable;
    RequestQueue queue;
    AlertDialog alertDialogItems;
    TextView tv_perc;
    LinearLayout action;
    CardView progressBar_license;

    String WebserviceUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        LocaleUtils.initialize(this, LocaleUtils.getSelectedLanguageId(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_home_layout);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(SettingsActivity.this);
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

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);

        tv_perc=findViewById(R.id.tv_perc);

        MyServiceApp_swiperefresh_sync.setOnProgressChangedListener(this);

        ImageView leftarrow = (ImageView) findViewById(R.id.leftarrow);
        leftarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        action = (LinearLayout) findViewById(R.id.action);
        progressBar_license = (CardView) findViewById(R.id.progressbar1);

        auto_connect = (Spinner) findViewById(R.id.auto_connect);
        Cursor aallrowsws = db.rawQuery("SELECT * FROM Auto_Connect WHERE _id = '1'", null);
        if (aallrowsws.moveToFirst()) {
            do {
                String NAME = aallrowsws.getString(1);
                auto_connect.setSelection(getIndex(auto_connect, NAME));

            } while (aallrowsws.moveToNext());
        }
        final int selectionCurrentt11 = auto_connect.getSelectedItemPosition();
        auto_connect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ContentValues newValues = new ContentValues();
                newValues.put("auto_connect_status", String.valueOf(auto_connect.getSelectedItem().toString()));
                String where = "_id = '1'";
//                db.update("Auto_Connect", newValues, where, new String[]{});

                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Auto_Connect");
                getContentResolver().update(contentUri, newValues,where,new String[]{});
                resultUri = new Uri.Builder()
                        .scheme("content")
                        .authority(StubProviderApp.AUTHORITY)
                        .path("Auto_Connect")
                        .appendQueryParameter("operation", "update")
                        .appendQueryParameter("_id","1")
                        .build();
                getContentResolver().notifyChange(resultUri, null);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        RelativeLayout syncnow = (RelativeLayout) findViewById(R.id.syncnow);
        syncnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateBar= findViewById(R.id.updatebar);
                updateBar.setMax(426);

                updateBar.setProgress(0);

                final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                if (cm != null) {
                    if (Build.VERSION.SDK_INT < 23) {
                        try {
                            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
                            int returnVal = p1.waitFor();
                            reachable = (returnVal==0);
                            System.out.println(""+reachable);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
//                            return result;
                    } else {
                        boolean isOnline = false;
                        try {
                            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkCapabilities capabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());  // need ACCESS_NETWORK_STATE permission
                            isOnline = capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (isOnline){
                            System.out.println("internet");
                            reachable = true;
                        }else {
                            System.out.println("no internet");
                            reachable = false;
                        }



//                    return isOnline;
                    }

                    if(reachable){


                        action.setVisibility(View.GONE);

                        progressBar_license.setVisibility(View.VISIBLE);

                        db1.execSQL("delete from Table1");
                        db1.execSQL("delete from Table2");
                        db1.execSQL("delete from Table3");

                        new DeleteData_sync().execute();

                    }else {
                        AlertDialog alertDialog2 = new AlertDialog.Builder(SettingsActivity.this).create();

                        alertDialog2.setTitle(getString(R.string.title10));
                        alertDialog2.setMessage(getString(R.string.setmessage19));
                        alertDialog2.setIcon(android.R.drawable.ic_dialog_alert);
                        alertDialog2.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // finish();
                            }
                        });

                        alertDialog2.show();

//                            pullToRefresh.setRefreshing(false);

                    }

                }
            }
        });

        RelativeLayout signout = (RelativeLayout) findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle(getString(R.string.title20))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {

                                    SharedPreferences preferences = Stock_Transfer_Processing.getDefaultSharedPreferencesMultiProcess(SettingsActivity.this);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear();
                                    editor.apply();


                                    SharedPreferences preferences1 = SplashScreenActivity.getDefaultSharedPreferencesMultiProcess(SettingsActivity.this);
                                    SharedPreferences.Editor editor1 = preferences1.edit();
                                    editor1.clear();
                                    editor1.apply();

                                R_NewLogIn_BusinessType.storeList.clear();
                                MainActivity_Signin_OTPbased.storeList.clear();


                                    if(SettingsActivity.this.deleteDatabase("amazoninapp")){
                                        if(SettingsActivity.this.deleteDatabase("syncdb")){
                                            if(SettingsActivity.this.deleteDatabase("syncdbapp")){

                                                progressDialog=new ProgressDialog(SettingsActivity.this);
                                                progressDialog.setMessage("loading");
                                                progressDialog.show();
                                                new DeleteSalesData().execute();

                                            }else{
                                                Toast.makeText(SettingsActivity.this,"Please try again",Toast.LENGTH_LONG).show();
                                            }
                                        }else{
                                            Toast.makeText(SettingsActivity.this,"Please try again",Toast.LENGTH_LONG).show();
                                        }
                                    }else{
                                        Toast.makeText(SettingsActivity.this,"Please try again",Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                MainActivity_Signin_OTPbased.storeList.clear();
                R_NewLogIn_BusinessType.storeList.clear();
            }
        });

    }

    private int getIndex(Spinner spinner, String myString) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }

    class DeleteSalesData extends AsyncTask<String, Void, Integer> {




        @Override
        protected Integer doInBackground(String... strings) {

            // db1=  SQLiteDatabase.openDatabase("mydb_Appdata", null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);

            Cursor c = db1.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            if (c.moveToFirst()) {
                while ( !c.isAfterLast() ) {
                    String tablename=c.getString(0);
                    db1.execSQL("delete from "+ tablename);
                    c.moveToNext();
                }
            }
            c.close();

            db1.execSQL("vacuum");
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            new DeleteData().execute();

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
                    db.execSQL("delete from "+ tablename);
                    c.moveToNext();
                }
            }
            c.close();

            db.execSQL("vacuum");
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            // updateloginstatus();

            Intent i=new Intent(SettingsActivity.this, MainActivity_Signin_OTPbased.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

            Amplify.Auth.signOut(
                    () -> Log.i("AuthQuickstart", "Signed out successfully"),
                    error -> Log.e("AuthQuickstart", error.toString())
            );

            SharedPreferenceManager.getInstance().setDataDownloadValue(false);
        }
    }


    class DeleteData_sync extends AsyncTask<String, Void, Integer> {


        @Override
        protected Integer doInBackground(String... strings) {
//            db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            if (c.moveToFirst()) {
                while ( !c.isAfterLast() ) {
                    String tablename=c.getString(0);
                    if (tablename.equalsIgnoreCase("items") || tablename.equalsIgnoreCase("hotel")
                            || tablename.equalsIgnoreCase("taxes") || tablename.equalsIgnoreCase("discount_details")) {
                        db.execSQL("delete from " + tablename);
                    }
                    c.moveToNext();
                }
            }
            c.close();
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            startDownload();
//            Intent serviceIntent = new Intent(getActivity(), ForegroundService.class);
//            serviceIntent.putExtra("inputExtra", "Processing");
//
//            ContextCompat.startForegroundService(getActivity(), serviceIntent);
        }
    }

    private void startDownload() {



        SharedPreferences sharedpreferences=getDefaultSharedPreferencesMultiProcess(SettingsActivity.this);
        String company= sharedpreferences.getString("companyname", null);
        String store= sharedpreferences.getString("storename", null);
        String device= sharedpreferences.getString("devicename", null);

        JSONObject params = new JSONObject();

        try {
            params.put("device",device);
            params.put("store",store);
            params.put("company",company);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  if(queue==null){
        queue = Volley.newRequestQueue(SettingsActivity.this);
        // }

        JsonObjectRequest sr = new JsonObjectRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"countrows_manual_sync.php",params,
                new com.android.volley.Response.Listener<JSONObject>() {
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
                        Log.e("myserviceapp","2"+response);
                        if(response.equalsIgnoreCase("success")){

                            try {
                                db.execSQL("delete from Items_Virtual");
                                int total= jsonObject.getInt("total");
                                int maxcount=(total/20);
                                updateBar.setMax(maxcount+4);
                                Intent intent = new Intent(SettingsActivity.this, MyServiceApp_swiperefresh_sync.class);
                                ContextCompat.startForegroundService(SettingsActivity.this, intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else{
                            Toast.makeText(SettingsActivity.this, "download failed", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(SettingsActivity.this).registerReceiver(mMessageReceiver,
                new IntentFilter("myFunction"));
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiverapp, new IntentFilter(
                    "com.intuition.ivepos.SettingsActivity.receiverapp"), RECEIVER_EXPORTED);
        }else {
            registerReceiver(receiverapp, new IntentFilter(
                    "com.intuition.ivepos.SettingsActivity.receiverapp"));
        }

    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(SettingsActivity.this).unregisterReceiver(mMessageReceiver);
        super.onPause();

        unregisterReceiver(receiverapp);//manual Sync

    }

    private BroadcastReceiver receiverapp = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            new syncTaskApp().execute();

        }
    };

    class syncTaskApp extends AsyncTask<String, Void, Integer> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SyncDatabase syncdatabase=new SyncDatabase();
            syncdatabase.updateSyncDbApp(SettingsActivity.this);

        }

        @Override
        protected Integer doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);


            action.setVisibility(View.VISIBLE);



//            pullToRefresh.setRefreshing(false);

        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getExtras().getString("items")!=null){
                alertDialogItems = new AlertDialog.Builder(SettingsActivity.this).create();
                alertDialogItems.setTitle("Items Updating");
                alertDialogItems.setMessage(getString(R.string.setmessage12));
                alertDialogItems.show();

                db.execSQL("delete from Items_Virtual");

            }else if(intent.getExtras().getString("stop")!=null){
                alertDialogItems.dismiss();
            }


        }
    };

    @Override
    public void onProgressUpdate(int progress) {
        // Do update your progress...

        updateBar.setVisibility(View.VISIBLE);
        updateBar.setProgress(updateBar.getProgress()+progress);

        float perc= ((float)updateBar.getProgress()/(float)updateBar.getMax())*100;
        int p=(int) perc;
        tv_perc.setText(p+"%");
    }

}
