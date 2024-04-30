package com.intuition.ivepos.paytm;

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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.intuition.ivepos.R;
import com.intuition.ivepos.syncapp.StubProviderApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

/**
 * Created by intuition on 12-10-2017.
 */

public class PaytmSetting extends Activity {
    TextView register;
    ProgressDialog progress;
    LinearLayout reset;
    ImageView back;
    EditText Merchant_Name, M_guid, M_ID, Merchantkey, Industry_Type, Platform_Name, Pos_ID, Currency_Code, operationType_otp, channell, ip_Address, versionn;

    public static final String PACKAGE_NAME = "com.intuition.ivepos.paytm";
    public static final String DATABASE_NAME = "mydb_Appdata";
    public static final String TABLE_NAME = "PaytmMerchantReg";
    public SQLiteDatabase db = null;
    private static final File DATA_DIRECTORY_DATABASE =
            new File(Environment.getDataDirectory() + "/data/" + PACKAGE_NAME + "/databases/" + DATABASE_NAME);
    Uri contentUri,resultUri;
    RequestQueue queue;
    ProgressDialog statusDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paytm_setting_layout);

        hideKeyboard(PaytmSetting.this);
        donotshowKeyboard(this);

        queue = Volley.newRequestQueue(PaytmSetting.this);

        Merchant_Name = (EditText) findViewById(R.id.merchantName);
        M_guid = (EditText) findViewById(R.id.guid);
        M_ID = (EditText) findViewById(R.id.MID);
        Merchantkey = (EditText) findViewById(R.id.Merchant_key);
        Pos_ID = (EditText) findViewById(R.id.PosID);


        SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(PaytmSetting.this);
        final String company= sharedpreferences.getString("companyname", null);
        final String store= sharedpreferences.getString("storename", null);
        final String device= sharedpreferences.getString("devicename", null);

        Merchant_Name.setText(company);
        Pos_ID.setText(company+"-"+store+"-"+device);


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(Merchant_Name.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(M_guid.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(M_ID.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(Merchantkey.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(Pos_ID.getWindowToken(), 0);

        reset = (LinearLayout) findViewById(R.id.btnreset_pane);
        register = (TextView) findViewById(R.id.btnreg);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PaytmSetting.this, Card_Wallets_Settings.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
            }
        });

        db = openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS PaytmMerchantReg" +
                "(_id integer primary key autoincrement unique, Account text, MerchantName text, " +
                "guid text, MID text, Merchant_key text, PosID text)");

        Cursor c1 = db.rawQuery("SELECT * FROM PaytmMerchantReg WHERE Account = 'Registered'", null);
        if (c1.moveToFirst()) {
            do {
                String account = c1.getString(1);
                String MerchantName = c1.getString(2);
              //  String guid = c1.getString(3);
                String MID = c1.getString(4);
                String Merchant_key = c1.getString(5);
                String PosID = c1.getString(6);
//                    Thread.sleep(1000);
                if (account.equals("Registered")) {
                    Merchant_Name.setText(MerchantName);
                    //M_guid.setText(guid);
                    M_ID.setText(MID);
                    Merchantkey.setText(Merchant_key);
                    Pos_ID.setText(PosID);

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


                if (Merchant_Name.getText().toString().equals("")  ||
                       M_ID.getText().toString().equals("") || M_ID.getText().toString().length() < 20
                        || Merchantkey.getText().toString().equals("") || Merchantkey.getText().toString().length() < 16) {
                    if (Merchant_Name.getText().toString().equals("") ) {
                        Merchant_Name.setError("Enter valid Merchat Name");
                    }
//                    if (M_guid.getText().toString().equals("") || M_guid.getText().toString().length() < 36) {
//                        M_guid.setError("Enter valid Merchant Guid");
//                    }
                    if (M_ID.getText().toString().equals("") || M_ID.getText().toString().length() < 20) {
                        M_ID.setError("Enter valid MID");
                    }
                    if (Merchantkey.getText().toString().equals("") || Merchantkey.getText().toString().length() < 16) {
                        Merchantkey.setError("Enter valid Merchant/AES Key");
                    }
                    if (Pos_ID.getText().toString().equals("") ) {
                        Pos_ID.setError("Enter valid POS Id");
                    }
                }else {
                    getchecksum();
                }
            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(PaytmSetting.this);
                builder.setTitle("Are you sure, you want to delete account?");
                builder.setMessage(getString(R.string.setmessage42));
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                progress = new ProgressDialog(PaytmSetting.this);
                                progress.setTitle("Please Wait!!");
                                progress.setMessage(getString(R.string.setmessage43));
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
                                contentValues.put("Account", "Not_Registered");
                                contentValues.put("MerchantName", "");
                                contentValues.put("guid", "");
                                contentValues.put("MID", "");
                                contentValues.put("Merchant_key", "");
                                contentValues.put("PosID", "");


                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "PaytmMerchantReg");
                                getContentResolver().update(contentUri, contentValues, "Account = 'Registered'",null);
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProviderApp.AUTHORITY)
                                        .path("PaytmMerchantReg")
                                        .appendQueryParameter("operation", "update")
                                        .appendQueryParameter("Account","Registered")
                                        .build();
                                getContentResolver().notifyChange(resultUri, null);





                       //         db.update("PaytmMerchantReg", contentValues, "Account = 'Registered'", null);

                               // Merchant_Name.setText("");
                                M_guid.setText("");
                                M_ID.setText("");
                                Merchantkey.setText("");
                              //  Pos_ID.setText("");

                                Toast.makeText(PaytmSetting.this, "Data Deleted", Toast.LENGTH_SHORT).show();
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


    public void getchecksum(){

        statusDialog = new ProgressDialog(PaytmSetting.this);
        statusDialog.setMessage(getString(R.string.setmessage44));
        statusDialog.setIndeterminate(false);
        statusDialog.setCancelable(false);
        statusDialog.show();

        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                "https://theandroidpos.com/testapi/Paytm_PHP/sample1.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        statusDialog.dismiss();

                        Log.e("checksum response",responseString);

                        JSONObject jsonObject=null;
                        try {
                            jsonObject=new JSONObject(responseString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {

                            if(jsonObject.has("body")){
                                String response = jsonObject.getJSONObject("body").getJSONObject("resultInfo").getString("resultStatus");
                                if(response.equalsIgnoreCase("SUCCESS")){
                                    Cursor cursor = db.rawQuery("SELECT * FROM PaytmMerchantReg", null);
                                    if (cursor.moveToFirst()) {
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("Account", "Registered");
                                        contentValues.put("MerchantName", Merchant_Name.getText().toString());
                                        // contentValues.put("guid", M_guid.getText().toString());
                                        contentValues.put("MID", M_ID.getText().toString());
                                        contentValues.put("Merchant_key", Merchantkey.getText().toString());
                                        contentValues.put("PosID", Pos_ID.getText().toString());


                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "PaytmMerchantReg");
                                        getContentResolver().update(contentUri, contentValues, "Account = 'Not_Registered'",null);
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProviderApp.AUTHORITY)
                                                .path("PaytmMerchantReg")
                                                .appendQueryParameter("operation", "update")
                                                .appendQueryParameter("Account","Not_Registered")
                                                .build();
                                        getContentResolver().notifyChange(resultUri, null);




                                        new AlertDialog.Builder(PaytmSetting.this)
                                                .setTitle(getString(R.string.title41))
                                                .setMessage(getString(R.string.setmessage45))

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Continue with delete operation
                                                        dialog.dismiss();
                                                        startActivity(new Intent(PaytmSetting.this, Card_Wallets_Settings.class)
                                                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                                    }
                                                })

                                                .show();


                                        //      db.update("PaytmMerchantReg", contentValues, "Account = 'Not_Registered'", null);
                                        //Toast.makeText(PaytmSetting.this, getString(R.string.title41), Toast.LENGTH_SHORT).show();
                                        reset.setVisibility(View.VISIBLE);
                                        register.setVisibility(View.GONE);


                                        return;

                                    }
                                    cursor.close();
                                }
                            }else{
                                String message = jsonObject.getJSONObject("resultInfo").getString("resultMsg");

                                new AlertDialog.Builder(PaytmSetting.this)
                                        .setTitle(getString(R.string.title8))
                                        .setMessage(getString(R.string.setmessage46))

                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Continue with delete operation
                                                dialog.dismiss();
                                            }
                                        })

                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();

                               // Toast.makeText(PaytmSetting.this,message,Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        statusDialog.dismiss();
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                })  {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                               /*     params.put("email", email + "");
                                    params.put("password", password + "");*/
//                Log.e("amount","1.00");
//                Log.e("mid",MID);
//                Log.e("posId",PosID);
//                Log.e("mkey",Merchant_key);


                params.put("amount","1.00");
                params.put("mid",M_ID.getText().toString());
                params.put("posId",Pos_ID.getText().toString());
                params.put("mkey",Merchantkey.getText().toString());
                return params;
            }
        };
    /*    sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        sr.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        queue.add(sr);
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
