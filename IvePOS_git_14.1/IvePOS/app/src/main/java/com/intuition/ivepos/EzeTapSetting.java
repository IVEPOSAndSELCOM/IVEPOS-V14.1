package com.intuition.ivepos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.intuition.ivepos.csv.RequestSingleton;
import com.intuition.ivepos.mSwipe.LoginView;
import com.intuition.ivepos.paytm.Card_Wallets_Settings;
import com.intuition.ivepos.paytm.Card_Wallets_Settings1;
import com.intuition.ivepos.paytm.PaytmSetting;
import com.intuition.ivepos.razorpay.RazorPaySetting;
import com.intuition.ivepos.syncapp.StubProviderApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

public class EzeTapSetting extends AppCompatActivity {
    TextView register;
    ProgressDialog progress;
    LinearLayout reset;
    ImageView back;
    EditText login_TXT_merchantid, login_TXT_merchantpassword, orgcode, prod_key;

    public static final String PACKAGE_NAME = "com.intuition.ivepos.razorpay";
    public static final String DATABASE_NAME = "mydb_Appdata";
    public static final String TABLE_NAME = "RazorpayMerchantReg";
    public SQLiteDatabase db = null;
    private static final File DATA_DIRECTORY_DATABASE =
            new File(Environment.getDataDirectory() + "/data/" + PACKAGE_NAME + "/databases/" + DATABASE_NAME);
    Uri contentUri,resultUri;
    RequestQueue queue;
    ProgressDialog statusDialog;

    String WebserviceUrl;
    String account_selection;

    String companyid, storeid, deviceid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ezetap_setting_layout);

        hideKeyboard(EzeTapSetting.this);
        donotshowKeyboard(this);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(EzeTapSetting.this);
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

        SharedPreferences sh = getSharedPreferences("MySharedPref_ids", MODE_PRIVATE);
        companyid = sh.getString("companyid", "");
        storeid = sh.getString("storeid", "");
        deviceid = sh.getString("deviceid", "");

        queue = Volley.newRequestQueue(EzeTapSetting.this);

        login_TXT_merchantid = (EditText) findViewById(R.id.login_TXT_merchantid);
        login_TXT_merchantpassword = (EditText) findViewById(R.id.login_TXT_merchantpassword);
        prod_key = (EditText) findViewById(R.id.prod_key);
        orgcode = (EditText) findViewById(R.id.orgcode);


        SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(EzeTapSetting.this);
        final String company= sharedpreferences.getString("companyname", null);
        final String store= sharedpreferences.getString("storename", null);
        final String device= sharedpreferences.getString("devicename", null);


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(login_TXT_merchantid.getWindowToken(), 0);

        reset = (LinearLayout) findViewById(R.id.clear);
        register = (TextView) findViewById(R.id.login_BTN_signin);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* startActivity(new Intent(EzeTapSetting.this, Card_Wallets_Settings.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                return;*/
                finish();
            }
        });

        db = openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS EzetapMerchantReg (_id integer primary key autoincrement unique, account text, userid text, password text)");

        Cursor c1 = db.rawQuery("SELECT * FROM EzetapMerchantReg WHERE account = 'Registered'", null);
        if (c1.moveToFirst()) {
            do {
                String account = c1.getString(1);
                String userid = c1.getString(2);
                String password = c1.getString(3);
                String prkey = c1.getString(4);
                String orcode = c1.getString(5);
//                    Thread.sleep(1000);
                if (account.equals("Registered")) {
                    login_TXT_merchantid.setText(userid);
                    login_TXT_merchantpassword.setText(password);
                    prod_key.setText(prkey);
                    orgcode.setText(orcode);

                    register.setVisibility(View.GONE);
                    reset.setVisibility(View.VISIBLE);
                } else {
                    register.setVisibility(View.VISIBLE);
                    reset.setVisibility(View.GONE);

                }
            } while (c1.moveToNext());

        }
        c1.close();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progress = new ProgressDialog(PaytmSetting.this);
//                progress.setTitle("Please Wait!!");
//                progress.setMessage("Registering...");
//                progress.setCancelable(true);
//                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                progress.show();


                if (login_TXT_merchantid.getText().toString().equals("")) {
                    login_TXT_merchantid.setError("Enter valid Account ID");
                }else {
                    if (login_TXT_merchantpassword.getText().toString().equals("")) {
                        login_TXT_merchantpassword.setError("Enter valid Password");
                    }else {

                    }
                    Cursor cursor = db.rawQuery("SELECT * FROM EzetapMerchantReg", null);
                    if (cursor.moveToFirst()) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("account", "Registered");
                        contentValues.put("userid", login_TXT_merchantid.getText().toString());
                        contentValues.put("password", login_TXT_merchantpassword.getText().toString());
                        contentValues.put("prodAppKey", prod_key.getText().toString());
                        contentValues.put("orgCode", orgcode.getText().toString());


                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "EzetapMerchantReg");
                        getContentResolver().update(contentUri, contentValues, "account = 'Not_Registered'",null);
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("EzetapMerchantReg")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("account","Not_Registered")
                                .build();
                        getContentResolver().notifyChange(resultUri, null);




                        new AlertDialog.Builder(EzeTapSetting.this)
                                .setTitle("Success")
                                .setMessage("Account Registered successfully")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continue with delete operation
                                        dialog.dismiss();
                                        startActivity(new Intent(EzeTapSetting.this, Card_Wallets_Settings.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    }
                                })

                                .show();


                        //      db.update("EzetapMerchantReg", contentValues, "Account = 'Not_Registered'", null);
                        //Toast.makeText(EzeTapSetting.this, "Success", Toast.LENGTH_SHORT).show();
                        reset.setVisibility(View.VISIBLE);
                        register.setVisibility(View.GONE);


                        return;

                    }
                    cursor.close();
                }
            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(EzeTapSetting.this);
                builder.setTitle("Are you sure, you want to delete account?");
                builder.setMessage("Existing Data will be deleted.");
                builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        progress = new ProgressDialog(EzeTapSetting.this);
                                        progress.setTitle("Please Wait!!");
                                        progress.setMessage("Deleting Account...");
                                        progress.setCancelable(true);
                                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        progress.show();

                                        Timer timer = new Timer();
                                        timer.schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                progress.dismiss();

                                            }
                                        }, 1000);

                                        // Delete Table data...
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("account", "Not_Registered");
                                        contentValues.put("userid", "");
                                        contentValues.put("password", "");


                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "EzetapMerchantReg");
                                        getContentResolver().update(contentUri, contentValues, "account = 'Registered'",null);
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProviderApp.AUTHORITY)
                                                .path("EzetapMerchantReg")
                                                .appendQueryParameter("operation", "update")
                                                .appendQueryParameter("account","Registered")
                                                .build();
                                        getContentResolver().notifyChange(resultUri, null);

                                        login_TXT_merchantid.setText("");
                                        login_TXT_merchantpassword.setText("");

                                        Toast.makeText(EzeTapSetting.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                                        reset.setVisibility(View.GONE);
                                        register.setVisibility(View.VISIBLE);
                                    }
                                }
                        )
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                }
                        );
                builder.show();
//                register.setVisibility(View.VISIBLE);
//                reset.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Card_Wallets_Settings.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        return;
    }



    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void donotshowKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

}
