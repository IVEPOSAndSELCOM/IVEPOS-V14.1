package com.intuition.ivepos;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.intuition.ivepos.csv.RequestSingleton;
import com.intuition.ivepos.syncapp.StubProviderApp;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

public class Table_setup_configuration extends AppCompatActivity {

    private SQLiteDatabase db,db1;
    String position, floor, selected_section;

    ListView listView;

    String WebserviceUrl;
    String account_selection;

    Uri contentUri,resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_setup_configuration);

        hideKeyboard(Table_setup_configuration.this);
        donotshowKeyboard(Table_setup_configuration.this);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(Table_setup_configuration.this);
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

        ImageButton btncancel = (ImageButton) findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        position = extras.getString("position");
        floor = extras.getString("floor");
        selected_section = extras.getString("selected_section");

        System.out.println("settings "+position+" "+floor+" "+selected_section);

        listView = (ListView) findViewById(R.id.listview);

        Cursor cursor = db.rawQuery("Select * from asd1 WHERE position = '"+selected_section+"'", null);
        String[] fromFieldNames = {"pDate", "max"};
        int[] toViewsID = {R.id.table_name, R.id.pax_limit};
        Log.e("Checamos que hay id", String.valueOf(R.id.name));
        ImageCursorAdapter32 tableadapter = new ImageCursorAdapter32(Table_setup_configuration.this, R.layout.table_setup_listview, cursor, fromFieldNames, toViewsID);
        listView.setAdapter(tableadapter);// Assign adapter to ListView.... here... the bitch error

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String _id = cursor.getString(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("pDate"));
                String max = cursor.getString(cursor.getColumnIndex("max"));

                TextView tv = (TextView) view.findViewById(R.id.table_name);

                System.out.println("selected table is "+_id+" "+max+" "+tv.getText().toString());

                final Dialog dialog = new Dialog(Table_setup_configuration.this, R.style.notitle);
                dialog.setContentView(R.layout.tab_management_settings_dialog_tab_setup);
                dialog.show();

                EditText table_name = (EditText) dialog.findViewById(R.id.table_name);
                EditText pax_limit = (EditText) dialog.findViewById(R.id.pax_limit);

                table_name.setText(tv.getText().toString());
                pax_limit.setText(max);

                ImageButton btncancel = (ImageButton) dialog.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                ImageView btnsave = (ImageView) dialog.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (table_name.getText().toString().equals("")) {
                            table_name.setError("Enter valid table name");
                        }else {
                            if (pax_limit.getText().toString().equals("")) {
                                pax_limit.setError("Enter pax limit");
                            }else {
                                ContentValues cv = new ContentValues();
                                cv.put("pName", table_name.getText().toString());
                                cv.put("max", pax_limit.getText().toString());
                                String where = "_id = '" + _id + "' ";

                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "asd1");
                                getContentResolver().update(contentUri, cv, where,new String[]{});
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProviderApp.AUTHORITY)
                                        .path("asd1")
                                        .appendQueryParameter("operation", "update")
                                        .appendQueryParameter("_id",_id)
                                        .build();
                                getContentResolver().notifyChange(resultUri, null);

//                                db.update("asd1", cv, where, new String[]{});
                                dialog.dismiss();

                                cursor.moveToPosition(position);
                                cursor.requery();
                                tableadapter.notifyDataSetChanged();

                            }
                        }
                    }
                });



            }
        });

        EditText universal_pax_limit = (EditText) findViewById(R.id.universal_pax_limit);
        ImageView enter = (ImageView) findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (universal_pax_limit.getText().toString().equals("") || universal_pax_limit.getText().toString().equals("0")) {
                    universal_pax_limit.setError("Enter Universal Pax limit");
                }else {

                    final Dialog dialog = new Dialog(Table_setup_configuration.this, R.style.notitle);
                    dialog.setContentView(R.layout.dialog_universal_pax_limit_warning);
                    dialog.show();

                    Button cancel = (Button) dialog.findViewById(R.id.cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    Button ok = (Button) dialog.findViewById(R.id.ok);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ContentValues cv = new ContentValues();
                            cv.put("max", universal_pax_limit.getText().toString());
                            String where = "position = '" + position + "' ";

                            db.execSQL("UPDATE asd1 set max = '"+universal_pax_limit.getText().toString()+"' WHERE position = '"+position+"'");
                            webservicequery("UPDATE asd1 set max = '"+universal_pax_limit.getText().toString()+"' WHERE position = '"+position+"'");

//                            db.update("asd1", cv, where, new String[]{});
                            dialog.dismiss();

                            Cursor cursor = db.rawQuery("Select * from asd1 WHERE position = '"+selected_section+"'", null);
                            String[] fromFieldNames = {"pDate", "max"};
                            int[] toViewsID = {R.id.table_name, R.id.pax_limit};
                            Log.e("Checamos que hay id", String.valueOf(R.id.name));
                            ImageCursorAdapter32 tableadapter = new ImageCursorAdapter32(Table_setup_configuration.this, R.layout.table_setup_listview, cursor, fromFieldNames, toViewsID);
                            listView.setAdapter(tableadapter);// Assign adapter to ListView.... here... the bitch error

                        }
                    });


                }
            }
        });

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


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(Table_setup_configuration.this);
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
        RequestQueue queue;
        StringRequest sr1;
        // queue = Volley.newRequestQueue(getActivity());

        queue= RequestSingleton.getInstance(Table_setup_configuration.this).getInstance();

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
