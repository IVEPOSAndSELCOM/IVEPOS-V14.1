package com.intuition.ivepos;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    Button btn_company,btn_store,btn_device, btn_backup;
    EditText editTextUserId,editTextPassword;
    String email,password;
    Button btnlogin;
    RelativeLayout progressBar;
    RequestQueue requestQueue;
    Dialog dialog_auth;


    SimpleDateFormat sdff2, sdff1;
    String currentDateandTimee1;
    String timee1;
    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;
    }

    public static final String PACKAGE_NAME = "com.intuition.ivepos";
    public static final String DATABASE_NAME = "mydb_Appdata";
    public static final String DATABASE_NAME1 = "mydb_Salesdata";

    private static final File DATA_DIRECTORY_DATABASE =
            new File(Environment.getDataDirectory() +
                    "/data/" + PACKAGE_NAME +
                    "/databases/" + DATABASE_NAME );

    private static final File DATA_DIRECTORY_DATABASE1 =
            new File(Environment.getDataDirectory() +
                    "/data/" + PACKAGE_NAME +
                    "/databases/" + DATABASE_NAME1 );

    String WebserviceUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

//        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(SignupActivity.this);
//        String account_selection= sharedpreferences_select.getString("account_selection", null);
//
//        if (account_selection.toString().equals("Dine")) {
//            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
//        }else {
//            if (account_selection.toString().equals("Qsr")) {
//                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
//            }else {
//                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
//            }
//        }

        btn_company=findViewById(R.id.btn_company);
        btn_store=findViewById(R.id.btn_store);
        btn_device=findViewById(R.id.btn_device);
        btn_backup=findViewById(R.id.btn_backup);

        btn_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignupActivity.this,MainActivity_Register.class);
                startActivity(intent);
            }
        });

        btn_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_auth = new Dialog(SignupActivity.this, R.style.notitle);
                dialog_auth.setContentView(R.layout.signin_dialog);
                dialog_auth.setCanceledOnTouchOutside(false);
                dialog_auth.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog_auth.show();

                editTextUserId=dialog_auth.findViewById(R.id.editTextUserId);
                editTextPassword=dialog_auth.findViewById(R.id.editTextUserPassword);
                btnlogin = dialog_auth.findViewById(R.id.login1);
                btnlogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(editTextUserId.getText().toString().equalsIgnoreCase("")){
                            editTextUserId.setError("Please enter the username");
                        }else if(editTextPassword.getText().toString().equalsIgnoreCase("")){
                            editTextPassword.setError("Please enter the password");
                        }else{
                            login();
                        }
                    }
                });

            }
        });

        btn_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_auth = new Dialog(SignupActivity.this, R.style.notitle);
                dialog_auth.setContentView(R.layout.signin_dialog);
                dialog_auth.setCanceledOnTouchOutside(false);
                dialog_auth.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog_auth.show();

                editTextUserId=dialog_auth.findViewById(R.id.editTextUserId);
                editTextPassword=dialog_auth.findViewById(R.id.editTextUserPassword);
                btnlogin = dialog_auth.findViewById(R.id.login1);
                btnlogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(editTextUserId.getText().toString().equalsIgnoreCase("")){
                            editTextUserId.setError("Please enter the username");
                        }else if(editTextPassword.getText().toString().equalsIgnoreCase("")){
                            editTextPassword.setError("Please enter the password");
                        }else{
                            loginDevice();
                        }
                    }
                });

            }
        });

        btn_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sdff2 = new SimpleDateFormat("ddMMMyy");
                currentDateandTimee1 = sdff2.format(new Date());

                Date dt = new Date();
                sdff1 = new SimpleDateFormat("hhmmssaa");
                timee1 = sdff1.format(dt);

                if (ContextCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(SignupActivity.this,
                            permissions(),
                            1);
                   /* // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(SignupActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

//                        Toast.makeText(ForgotPasswordActivity.this, "111111111", Toast.LENGTH_SHORT).show();

                        ActivityCompat.requestPermissions(SignupActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1);
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
//                        Toast.makeText(ForgotPasswordActivity.this, "no permission", Toast.LENGTH_SHORT).show();

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(SignupActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }*/
                }else {
//                    Toast.makeText(ForgotPasswordActivity.this, "hiiii", Toast.LENGTH_SHORT).show();

                    if (!SdIsPresent()) ;

                    DownloadMusicfromInternet3 downloadMusicfromInternet = new DownloadMusicfromInternet3();
                    downloadMusicfromInternet.execute();
                }

            }
        });

        requestQueue = Volley.newRequestQueue(this);
    }


    private void showDialogMessage(String title, String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    //  dialog_auth.dismiss();

                } catch (Exception e) {
                    //exit();
                }
            }
        });

    }

    public void login() {
        progressBar = (RelativeLayout) dialog_auth.findViewById(R.id.progressbar1);
        progressBar.setVisibility(View.VISIBLE);
        email=editTextUserId.getText().toString();
        password=editTextPassword.getText().toString();

       /* Map<String,String> params = new HashMap<String, String>();
        params.put("email", email + "");
        params.put("password", password+ "");

        JSONObject parameters = new JSONObject(params);

        RequestClass jr=new RequestClass(Method.POST, Constant.WebserviceUrl+"login.php",parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response",response.toString());
                String status="",company="",email="",password="";
                try {
                     status=response.getString("status");
                    company=response.getString("company");
                    email=response.getString("email");
                    password=response.getString("password");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(status.equalsIgnoreCase("success")){
                    progressBar.setVisibility(View.GONE);
                    dialog_auth.dismiss();
                    Intent intent=new Intent(SignupActivity.this,NewStoreActivity.class);
                    intent.putExtra("company",company);
                    intent.putExtra("email",email);
                    intent.putExtra("password",password);
                    startActivity(intent);
                }else{
                    progressBar.setVisibility(View.GONE);
                    dialog_auth.dismiss();
                    showDialogMessage("signup failed","signup failed");
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Signup confirm", "Error: " + error.getMessage());
            }
        });
        jr.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jr);*/



        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response",response.toString());
                        JSONObject jsonObject= null;
                        String status="",company="",email="",password="";
                        try {
                            jsonObject = new JSONObject(response);
                            status=jsonObject.getString("status");
                            company=jsonObject.getString("company");
                            email=jsonObject.getString("email");
                            password=jsonObject.getString("password");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status.equalsIgnoreCase("success")){
                            progressBar.setVisibility(View.GONE);
                            dialog_auth.dismiss();
                            Intent intent=new Intent(SignupActivity.this,NewStoreActivity.class);
                            intent.putExtra("company",company);
                            intent.putExtra("email",email);
                            intent.putExtra("password",password);
                            startActivity(intent);
                        }else{
                            progressBar.setVisibility(View.GONE);
                            //   dialog_auth.dismiss();
                            showDialogMessage("signup failed","signup failed");
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
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);


    }

    public void loginDevice() {
        progressBar = (RelativeLayout) dialog_auth.findViewById(R.id.progressbar1);
        progressBar.setVisibility(View.VISIBLE);
        email=editTextUserId.getText().toString();
        password=editTextPassword.getText().toString();

  /*      Map<String,String> params = new HashMap<String, String>();
        params.put("email", email + "");
        params.put("password", password+ "");

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jr=new JsonObjectRequest(Method.POST, Constant.WebserviceUrl+"login.php",parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String status="",company="",email="",password="";
                try {
                    status=response.getString("status");
                    company=response.getString("company");
                    email=response.getString("email");
                    password=response.getString("password");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(status.equalsIgnoreCase("success")){
                    progressBar.setVisibility(View.GONE);
                    dialog_auth.dismiss();
                    Intent intent=new Intent(SignupActivity.this,NewDeviceActivity.class);
                    intent.putExtra("company",company);
                    intent.putExtra("email",email);
                    intent.putExtra("password",password);
                    startActivity(intent);
                }else{
                    progressBar.setVisibility(View.GONE);
                    dialog_auth.dismiss();
                    showDialogMessage("signup failed","signup failed");
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Signup confirm", "Error: " + error.getMessage());
            }
        });
        jr.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jr);*/





        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response",response.toString());
                        JSONObject jsonObject= null;
                        String status="",company="",email="",password="";
                        String[] stores = new String[0];
                        try {
                            jsonObject = new JSONObject(response);
                            status=jsonObject.getString("status");
                            if(status.equalsIgnoreCase("success")){
                                company=jsonObject.getString("company");
                                email=jsonObject.getString("email");
                                password=jsonObject.getString("password");

                                JSONArray jsonArray= jsonObject.getJSONArray("store");
                                stores=new String[jsonArray.length()];
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject store= (JSONObject) jsonArray.get(i);
                                    String store_name=store.getString("store_name");
                                    stores[i]=store_name;
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status.equalsIgnoreCase("success")){
                            progressBar.setVisibility(View.GONE);
                            dialog_auth.dismiss();
                            Intent intent=new Intent(SignupActivity.this,NewDeviceActivity.class);
                            intent.putExtra("company",company);
                            intent.putExtra("email",email);
                            intent.putExtra("password",password);
                            intent.putExtra("stores",stores);
                            startActivity(intent);
                        }else{
                            progressBar.setVisibility(View.GONE);
                            //   dialog_auth.dismiss();
                            showDialogMessage("signup failed","signup failed");
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
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);


    }


    public class RequestClass extends JsonObjectRequest {

        public RequestClass(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
        }

        @Override
        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

                JSONObject result = null;

                if (jsonString != null && jsonString.length() > 0)
                    result = new JSONObject(jsonString);

                return Response.success(result,
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //Toast.makeText(SignupActivity.this, "permission granted", Toast.LENGTH_SHORT).show();
                    System.out.println("permission granted");
                    if (!SdIsPresent()) ;

                    DownloadMusicfromInternet3 downloadMusicfromInternet = new DownloadMusicfromInternet3();
                    downloadMusicfromInternet.execute();


                } else {

                    Toast.makeText(SignupActivity.this, "permission denied", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static boolean SdIsPresent() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    class DownloadMusicfromInternet3 extends AsyncTask<String, Void, Integer> {
        private ProgressDialog dialog = new ProgressDialog(SignupActivity.this, R.style.timepicker_date_dialog);

        @Override
        protected Integer doInBackground(String... params) {

            File dbFile1 = DATA_DIRECTORY_DATABASE;
            String filename1 = "mydb_Appdata";

//            File exportDir1 = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
            File exportDir1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
            if (!exportDir1.exists()) {
                exportDir1.mkdirs();
            }


            //File exportDir = DATABASE_DIRECTORY;
            File file1 = new File(exportDir1, filename1);

            if (!exportDir1.exists()) {
                exportDir1.mkdirs();
            }

            try {
//                    file.createNewFile();
                copyFile(dbFile1, file1);
                Log.e("1", "111");
//                circle1.setVisibility(View.INVISIBLE);
//                tick1.setVisibility(View.VISIBLE);
//                error1.setVisibility(View.INVISIBLE);

                //return true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("2", "22");
//                circle1.setVisibility(View.INVISIBLE);
//                tick1.setVisibility(View.INVISIBLE);
//                error1.setVisibility(View.VISIBLE);
                //return false;
            }

            File dbFile = DATA_DIRECTORY_DATABASE1;
            String filename = "mydb_Salesdata";

//            File exportDir = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
            File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }


            //File exportDir = DATABASE_DIRECTORY;
            File file = new File(exportDir, filename);

            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            try {
//                    file.createNewFile();
                copyFile(dbFile, file);
                Log.e("1", "1111");
//                circle1.setVisibility(View.INVISIBLE);
//                tick1.setVisibility(View.VISIBLE);
//                error1.setVisibility(View.INVISIBLE);

                //return true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("2", "22");
//                circle1.setVisibility(View.INVISIBLE);
//                tick1.setVisibility(View.INVISIBLE);
//                error1.setVisibility(View.VISIBLE);
                //return false;
            }
            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);

            dialog.setMessage(getString(R.string.setmessage3));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
//            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                @Override
//                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        //dialog.dismiss();
//                        //row.setBackgroundResource(0);
//                        return true;
//                    }
//                    return false;
//                }
//            });
            dialog.show();
        }


        @Override
        protected void onPostExecute(Integer file_url) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();
            dialog.dismiss();

            Toast.makeText(SignupActivity.this, "Back up successfull", Toast.LENGTH_SHORT).show();

            List<String> your_array_list = new ArrayList<String>();
//            String path = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_backup/";
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/IVEPOS_backup/";

            File f = new File(path);
            File[] files = f.listFiles();
            for (File inFile : files) {
                if (inFile.isDirectory()) {
                    // in here, you can add directory names into an ArrayList and populate your ListView.
                    your_array_list.add(inFile.getName());
                    //Toast.makeText(getActivity(), "file anmem is "+inFile.getName(), Toast.LENGTH_SHORT).show();
                }
            }


        }

    }

    private static void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }

}
