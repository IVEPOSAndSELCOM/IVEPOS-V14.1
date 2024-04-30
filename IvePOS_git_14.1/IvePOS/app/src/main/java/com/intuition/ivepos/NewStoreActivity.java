package com.intuition.ivepos;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

public class NewStoreActivity extends AppCompatActivity {

    EditText et_store,et_email,et_password,et_confirmpwd;
    TextView tv_signUp;
    CheckBox ch_storespecific;
    LinearLayout ll_storespecific;
    RelativeLayout progressbar_register;
    String store="";
    RequestQueue requestQueue;
    String company,email,password;

    String WebserviceUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_store);

        SharedPreferences sharedpreferences_select =  getDefaultSharedPreferencesMultiProcess(NewStoreActivity.this);
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

        et_store=findViewById(R.id.et_store);
        tv_signUp=findViewById(R.id.tv_signUp);
        //ch_storespecific=findViewById(R.id.ch_storespecific);
        ll_storespecific=findViewById(R.id.ll_storespecific);
        et_email=findViewById(R.id.et_email);
        et_password=findViewById(R.id.et_password);
        et_confirmpwd=findViewById(R.id.et_confirmpwd);
        progressbar_register=findViewById(R.id.progressbar_register);

        company=getIntent().getStringExtra("company");
        email=getIntent().getStringExtra("email");
        password=getIntent().getStringExtra("password");

        tv_signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(et_store.getText().toString().equalsIgnoreCase("")){
                    et_store.setError("Please enter store name");
                }else{
                    /*if(ch_storespecific.isChecked()){

                        if(et_email.getText().toString().equalsIgnoreCase("")){
                            et_email.setError("Please enter email");
                        }else if(et_password.getText().toString().equalsIgnoreCase("")){
                            et_password.setError("Please enter password");
                        }else if(et_confirmpwd.getText().toString().equalsIgnoreCase("")){
                            et_confirmpwd.setError("Please confirm password");
                        }else{
                            if(et_password.getText().toString().equalsIgnoreCase(et_confirmpwd.getText().toString())){
                                newStorespecific();
                            }else{
                                Toast.makeText(NewStoreActivity.this, "Confirm password is not same as password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                        newStore();
                    }*/

                }
            }
        });

        /*ch_storespecific.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked==true){
                    ll_storespecific.setVisibility(View.VISIBLE);
                }else{
                    ll_storespecific.setVisibility(View.GONE);
                }
            }
        });*/
        requestQueue = Volley.newRequestQueue(this);
    }


    public void newStorespecific() {
        password=et_password.getText().toString();
        email=et_email.getText().toString();
        store=et_store.getText().toString();
        progressbar_register = (RelativeLayout) findViewById(R.id.progressbar_register);
        progressbar_register.setVisibility(View.VISIBLE);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"newstore.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")){
                            progressbar_register.setVisibility(View.GONE);
                            showDialogMessage(getString(R.string.sdm10),getString(R.string.sdm11));
                        }else{
                            progressbar_register.setVisibility(View.GONE);
                            showDialogMessage(getString(R.string.sdm12),getString(R.string.sdm12));
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
                params.put("email", email + "");
                params.put("password", password+ "");
                params.put("company", company+ "");
                params.put("store", store+ "");
               // params.put("storespecific",  ch_storespecific.isChecked()+"");
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);


    }


    public void newStore(){
        progressbar_register = (RelativeLayout) findViewById(R.id.progressbar_register);
        progressbar_register.setVisibility(View.VISIBLE);
        store=et_store.getText().toString();
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"newstore.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        if(response.equalsIgnoreCase("success")){
                            progressbar_register.setVisibility(View.GONE);
                            showDialogMessage(getString(R.string.sdm10),getString(R.string.sdm11));
                        }else{
                            progressbar_register.setVisibility(View.GONE);
                            showDialogMessage(getString(R.string.sdm12),getString(R.string.sdm12));
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
                params.put("email", email + "");
                params.put("password", password+ "");
                params.put("company", company+ "");
                params.put("store", store+ "");
               // params.put("storespecific",  ch_storespecific.isChecked()+"");
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

    }


    private void showDialogMessage(String title, String body) {
        AlertDialog alertDialog ;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    dialog.dismiss();
                    onBackPressed();

                } catch (Exception e) {
                    //exit();
                }
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

}
