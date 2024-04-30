package com.intuition.ivepos;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputLayout;
import com.intuition.ivepos.csv.RequestSingleton;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

/**
 * Created by Rohithkumar on 5/27/2017.
 */

public class EmailSetup_Yahoo extends AppCompatActivity {

    public SQLiteDatabase db = null;

    String WebserviceUrl;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emailsetup_yahoo);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(EmailSetup_Yahoo.this);
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

        final Dialog dialog_credentials = new Dialog(EmailSetup_Yahoo.this, R.style.timepicker_date_dialog);
        dialog_credentials.setContentView(R.layout.dialog_settings_email_setup);
        dialog_credentials.setCanceledOnTouchOutside(false);
        dialog_credentials.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog_credentials.show();


        final Spinner email_client = (Spinner) dialog_credentials.findViewById(R.id.email_client);
        email_client.setSelection(1);
        email_client.setEnabled(false);
        email_client.setClickable(false);


        final TextInputLayout username_layout = (TextInputLayout) dialog_credentials.findViewById(R.id.username_layout);
        final TextInputLayout password_layout = (TextInputLayout) dialog_credentials.findViewById(R.id.password_layout);


        final EditText username = (EditText) dialog_credentials.findViewById(R.id.username);
        final EditText password = (EditText) dialog_credentials.findViewById(R.id.password);

        ImageButton btnsave = (ImageButton) dialog_credentials.findViewById(R.id.btnsave);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dialog_credentials.dismiss();
//                                getResultsFromApi();
////                                new MakeRequestTask1(mCredential).execute();
//
//                                new MakeRequestTask(mCredential).execute();

                if (username.getText().toString().equals("") || password.getText().toString().equals("")){
                    if (username.getText().toString().equals("")){
                        username_layout.setError("Enter username");
                    }
                    if (password.getText().toString().equals("")){
                        password_layout.setError("Enter password");
                    }
                }else {
                    if (username.getText().toString().matches(emailPattern)) {
                        final Object position1 = email_client.getSelectedItem();
                        Cursor cursor2 = db.rawQuery("SELECT * FROM Email_setup", null);
                        if (cursor2.moveToFirst()) {
                            do {
                                String id = cursor2.getString(0);
                                String where = "_id = '" + id + "' ";
                                db.delete("Email_setup", where, new String[]{});

                                webservicequery("delete from Email_setup");

                            } while (cursor2.moveToNext());
                        }
//                            Toast.makeText(getActivity(), "entered", Toast.LENGTH_LONG).show();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("username", username.getText().toString());
                        contentValues.put("password", password.getText().toString());
                        contentValues.put("client", "Yahoo");
                        db.insert("Email_setup", null, contentValues);

                        webservicequery("INSERT INTO Email_setup (_id, username, password, client) VALUES ('1', '" + username.getText().toString() + "', '" + password.getText().toString() + "', 'Yahoo')");

                        dialog_credentials.dismiss();

                        Intent intent = new Intent(EmailSetup_Yahoo.this, EmailSetup.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else {
                        username_layout.setError("Enter valid username");
                    }

                }
            }
        });

        ImageButton btncancel = (ImageButton) dialog_credentials.findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_credentials.dismiss();

                Intent intent = new Intent(EmailSetup_Yahoo.this, EmailSetup.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                username_layout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password_layout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dialog_credentials.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Intent intent = new Intent(EmailSetup_Yahoo.this, EmailSetup.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    public void webservicequery(final String webserviceQuery){


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(EmailSetup_Yahoo.this);
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
        RequestQueue queue;
        StringRequest sr1;
        // queue = Volley.newRequestQueue(getActivity());

        queue= RequestSingleton.getInstance(EmailSetup_Yahoo.this).getInstance();

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
