package com.intuition.ivepos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.intuition.ivepos.signup.DownloadService;

import java.util.HashMap;
import java.util.Map;

public class NewDeviceActivity extends AppCompatActivity {

    EditText et_device;
    ImageView leftarrow;
    Spinner sp_stores;
    TextView tv_signUp;
    String company,email,password;
    String[] stores;
    String selectedItem="";
    RelativeLayout progressbar_register;
    RequestQueue requestQueue;

    String WebserviceUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_device);
        leftarrow = findViewById(R.id.leftarrow);
        leftarrow.setOnClickListener(View ->{
            finish();
        });

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(NewDeviceActivity.this);
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

        et_device=findViewById(R.id.et_device);
        sp_stores=findViewById(R.id.sp_stores);
        tv_signUp=findViewById(R.id.tv_signUp);

        company=getIntent().getStringExtra("company");
    /*    email=getIntent().getStringExtra("email");
        password=getIntent().getStringExtra("password");*/
        stores=getIntent().getStringArrayExtra("stores");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,stores);
        sp_stores.setAdapter(adapter);
        sp_stores.setSelection(0);

        sp_stores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem=stores[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_device.getText().toString().length()==0){
                  et_device.setError("Please enter the device name");

                }else if(selectedItem.equalsIgnoreCase("")){
                    Toast.makeText(NewDeviceActivity.this, "Please select the store", Toast.LENGTH_SHORT).show();
                }else{
                     newdevice();
                }

            }
        });
        requestQueue = Volley.newRequestQueue(this);
    }

    public void newdevice() {
        progressbar_register = (RelativeLayout) findViewById(R.id.progressbar_register);
        progressbar_register.setVisibility(View.VISIBLE);

        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"newdevice.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")){



                            Intent intent = new Intent(NewDeviceActivity.this, DownloadService.class);
                            // add infos for the service which file to download and where to store
                            intent.putExtra("company", company);
                            intent.putExtra("store", selectedItem);
                            intent.putExtra("device", et_device.getText().toString());
                            startService(intent);


                           // createappdata();

                        }else{
                            progressbar_register.setVisibility(View.GONE);
                            showDialogMessage(getString(R.string.sdm7),getString(R.string.sdm7));
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
                params.put("store", selectedItem+ "");
                params.put("device", et_device.getText().toString());
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);


    }


    private void createappdata() {

        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"createappdata.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")){
                            Log.d("response",response);
                            createsalesdata();
                        }else{
                            progressbar_register.setVisibility(View.GONE);
                            showDialogMessage("signup failed","signup failed");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                })  {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                               /*     params.put("email", email + "");
                                    params.put("password", password + "");*/
                params.put("company", company + "");
                params.put("store", selectedItem + "");
                params.put("device", et_device.getText().toString() + "");
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
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"createsalesdata.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("success")){
                            Log.d("response",response);
                  /*          SyncDatabase syncDatabase=new SyncDatabase();
                            syncDatabase.createSyncDb(SignUpConfirmActivity.this);*/
                            progressbar_register.setVisibility(View.GONE);


                            showDialogMessage(getString(R.string.sdm8),getString(R.string.sdm9));
                        }else{
                            progressbar_register.setVisibility(View.GONE);
                            showDialogMessage("signup failed","signup failed");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                })  {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                                                    /*    params.put("email", email + "");
                                                        params.put("password", password + "");*/
                params.put("company", company + "");
                params.put("store", selectedItem + "");
                params.put("device", et_device.getText().toString() + "");
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(0,-1,
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


    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            showDialogMessage(getString(R.string.sdm8),getString(R.string.sdm9));

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, new IntentFilter(
                    "com.intuition.ivepos.createdata.receiver"), RECEIVER_EXPORTED);
        }else {
            registerReceiver(receiver, new IntentFilter(
                    "com.intuition.ivepos.createdata.receiver"));
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}
