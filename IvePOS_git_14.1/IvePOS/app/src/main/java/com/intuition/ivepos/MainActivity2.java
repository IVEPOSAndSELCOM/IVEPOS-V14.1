package com.intuition.ivepos;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity2 extends AppCompatActivity {

    EditText et_company,et_password, et_store,et_device, et_mcc;
    TextView tv_passwordmsg;
    String company, store, device,password,confpassword, mcc;
    TextView tv_signUp;
    int i_cfg = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_register);

        et_company = (EditText) findViewById(R.id.et_company);
        et_password = (EditText) findViewById(R.id.et_password);
        et_store= (EditText) findViewById(R.id.et_store);
        et_device= (EditText) findViewById(R.id.et_device);
//        et_mcc= (EditText) findViewById(R.id.et_mcc);

        final ProgressDialog dialog = new ProgressDialog(MainActivity2.this, R.style.timepicker_date_dialog);

        tv_signUp = (TextView) findViewById(R.id.tv_signUp);
        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i_cfg = 0;

                dialog.setMessage("Loading...");
                dialog.show();

                System.out.println("user name.....1");
                password = et_password.getText().toString();
                company = et_company.getText().toString();
                store = et_store.getText().toString();
                device = et_device.getText().toString();
                mcc = et_mcc.getText().toString();
                boolean invalid = false;

                if (company.equals("") || company.length() == 0) {
                    invalid = true;
                    et_company.setError("Enter Your CompanyName");
                } else if (password.equals("") || password.length() == 0) {
                    invalid = true;
                    et_password.setError("Enter the Password");
                } else if (store.equals("") || store.length() == 0) {
                    invalid = true;
                    et_store.setError("Enter the store");
                }else if (device.equals("") || device.length() == 0) {
                    invalid = true;
                    et_device.setError("Enter the device");
                }


                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest sr = new StringRequest(
                        Request.Method.POST,
                        "https://theandroidpos.com:8085/IVEPOSFD/IVEPOS/newcompany_fd.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equalsIgnoreCase("success")){
                                    Log.d("response",response);
                                    dialog.dismiss();

                                }else{
//                                        showDialogMessage1("signup failed","signup failed",false);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Signup confirm", "Error: " + error.getMessage());
                                dialog.dismiss();
                            }
                        })  {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String,String> params = new HashMap<String, String>();
//                            if (i_cfg == 0) {
                        params.put("password", password + "");
                        params.put("company", company + "");
                        params.put("store", store + "");
                        params.put("device", device + "");
                        params.put("mcc", mcc + "");
                        i_cfg = 1;
//                            }
                        return params;
                    }
                };
//                    sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
//                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                    requestQueue.add(sr);

                sr.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(100),0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(sr);


            }
        });

    }

}
