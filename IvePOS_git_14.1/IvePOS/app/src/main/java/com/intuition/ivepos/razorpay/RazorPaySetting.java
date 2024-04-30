package com.intuition.ivepos.razorpay;

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
import com.intuition.ivepos.BeveragesMenuFragment_Dine;
import com.intuition.ivepos.R;
import com.intuition.ivepos.SplashScreenActivity_Selection;
import com.intuition.ivepos.csv.RequestSingleton;
import com.intuition.ivepos.paytm.Card_Wallets_Settings;
import com.intuition.ivepos.paytm.PaytmSetting;
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

public class RazorPaySetting extends Activity {
    TextView register;
    ProgressDialog progress;
    LinearLayout reset;
    ImageView back;
    EditText account_id;

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
        setContentView(R.layout.razorpay_setting_layout);

        hideKeyboard(RazorPaySetting.this);
        donotshowKeyboard(this);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(RazorPaySetting.this);
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

        queue = Volley.newRequestQueue(RazorPaySetting.this);

        account_id = (EditText) findViewById(R.id.account_id);


        SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(RazorPaySetting.this);
        final String company= sharedpreferences.getString("companyname", null);
        final String store= sharedpreferences.getString("storename", null);
        final String device= sharedpreferences.getString("devicename", null);


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(account_id.getWindowToken(), 0);

        reset = (LinearLayout) findViewById(R.id.btnreset_pane);
        register = (TextView) findViewById(R.id.btnreg);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RazorPaySetting.this, Card_Wallets_Settings.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
            }
        });

        db = openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS RazorpayMerchantReg (_id integer primary key autoincrement unique, account text, account_id text)");

        Cursor c1 = db.rawQuery("SELECT * FROM RazorpayMerchantReg WHERE account = 'Registered'", null);
        if (c1.moveToFirst()) {
            do {
                String account = c1.getString(1);
                String MerchantName = c1.getString(2);
//                    Thread.sleep(1000);
                if (account.equals("Registered")) {
                    account_id.setText(MerchantName);

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


                if (account_id.getText().toString().equals("")) {
                    account_id.setError("Enter valid Account ID");
                }else {
//                    getchecksum();

                    boolean tr = true;
                    int amount = 100;
                    try {
//            JSONArray jsonArray = new JSONArray();
                        JSONObject json1 = new JSONObject();
                        json1.put("purpose", "Test UPI QR code notes");
//            jsonArray.put(json1);

                        JSONObject bodyjson = new JSONObject();
                        try {
                            bodyjson.put("type", "upi_qr");
                            bodyjson.put("name", "Store_1");
                            bodyjson.put("usage", "single_use");
                            bodyjson.put("fixed_amount", true);
                            bodyjson.put("payment_amount", amount);
                            bodyjson.put("description", "For Store 1");
//                bodyjson.put("customer_id", "");//end customer user id
//                bodyjson.put("close_by", "");
                            bodyjson.put("notes", json1);
                            bodyjson.put("account_id", account_id.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        RequestQueue mQueue = Volley.newRequestQueue(RazorPaySetting.this);

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://api.razorpay.com/v1/payments/qr_codes", bodyjson,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("TAG", response.toString());//401 error

                                        JSONObject json= null;  //your response
                                        try {
                                            json = new JSONObject(response.toString());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        webservicequery("INSERT INTO razorpay_acc_id (`account_id`, `device_id`) VALUES ('"+account_id.getText().toString()+"', '"+deviceid+"')");

                                        Cursor cursor = db.rawQuery("SELECT * FROM RazorpayMerchantReg", null);
                                        if (cursor.moveToFirst()) {
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("account", "Registered");
                                            contentValues.put("account_id", account_id.getText().toString());


                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "RazorpayMerchantReg");
                                            getContentResolver().update(contentUri, contentValues, "account = 'Not_Registered'",null);
                                            resultUri = new Uri.Builder()
                                                    .scheme("content")
                                                    .authority(StubProviderApp.AUTHORITY)
                                                    .path("RazorpayMerchantReg")
                                                    .appendQueryParameter("operation", "update")
                                                    .appendQueryParameter("account","Not_Registered")
                                                    .build();
                                            getContentResolver().notifyChange(resultUri, null);




                                            new AlertDialog.Builder(RazorPaySetting.this)
                                                    .setTitle(getString(R.string.title41))
                                                    .setMessage(getString(R.string.setmessage45))

                                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // Continue with delete operation
                                                            dialog.dismiss();
                                                            startActivity(new Intent(RazorPaySetting.this, Card_Wallets_Settings.class)
                                                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                                        }
                                                    })

                                                    .show();


                                            //      db.update("RazorpayMerchantReg", contentValues, "Account = 'Not_Registered'", null);
                                            //Toast.makeText(PaytmSetting.this, getString(R.string.title41), Toast.LENGTH_SHORT).show();
                                            reset.setVisibility(View.VISIBLE);
                                            register.setVisibility(View.GONE);


                                            return;

                                        }
                                        cursor.close();

//                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getText()));
//                            startActivity(browserIntent);

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("TAG", error.getMessage(), error);
                                Toast.makeText(RazorPaySetting.this, "Enter valid Account ID", Toast.LENGTH_SHORT).show();
                            }
                        }) { //no semicolon or coma

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> params = new HashMap<String, String>();
                                params.put("Content-Type", "application/json");
//                    String credential = String.format("%s:%s","rzp_live_HihBWMWe8XMg2u","23kSbMGetjOK19EwJ2AT9D46");//Intuition KeyID and Key secret
//                    String credential = String.format("%s:%s","rzp_test_partner_JTUVRgaFGeSKf6","l0YWOJTpBMjq4QafMF5Q82SI");//Partner test
                                String credential = String.format("%s:%s","rzp_live_partner_JTUVSDw3dEObyw","t3EM0ZH6977lXb78CHGqPiWz");//Partner live
                                String authorization = "Basic " + Base64.encodeToString(credential.getBytes(), Base64.DEFAULT);
                                params.put("Authorization", authorization);
                                return params;
                            }

                        };
                        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        mQueue.add(jsonObjectRequest);

                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(RazorPaySetting.this);
                builder.setTitle("Are you sure, you want to delete account?");
                builder.setMessage(getString(R.string.setmessage42));
                builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        progress = new ProgressDialog(RazorPaySetting.this);
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

                                        webservicequery("DELETE FROM razorpay_acc_id WHERE account_id = '"+account_id.getText().toString()+"'");

                                        // Delete Table data...
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("account", "Not_Registered");
                                        contentValues.put("account_id", "");


                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "RazorpayMerchantReg");
                                        getContentResolver().update(contentUri, contentValues, "account = 'Registered'",null);
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProviderApp.AUTHORITY)
                                                .path("RazorpayMerchantReg")
                                                .appendQueryParameter("operation", "update")
                                                .appendQueryParameter("account","Registered")
                                                .build();
                                        getContentResolver().notifyChange(resultUri, null);

                                        account_id.setText("");

                                        Toast.makeText(RazorPaySetting.this, "Data Deleted", Toast.LENGTH_SHORT).show();
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

    public void webservicequery(final String webserviceQuery){


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(RazorPaySetting.this);
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
        RequestQueue queue;
        StringRequest sr1;
        // queue = Volley.newRequestQueue(getActivity());

        queue= RequestSingleton.getInstance(RazorPaySetting.this).getInstance();

        sr1 = new StringRequest(
                Request.Method.POST,
                WebserviceUrl + "insert_razp_accid.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                               /*     params.put("email", email + "");
                                    params.put("password", password + "");*/


//                            final String email = prefs.getString("emailid", "");
//                            final String pwd = prefs.getString("password", "");
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
