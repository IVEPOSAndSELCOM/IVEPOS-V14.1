package com.intuition.ivepos;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.intuition.ivepos.csv.RequestSingleton;
import com.intuition.ivepos.syncapp.StubProviderApp;
import com.itextpdf.text.TabSettings;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

public class Table_settings extends AppCompatActivity {

    private SQLiteDatabase db,db1;
    String position, floor, selected_section;

    int count;

    String WebserviceUrl;
    String account_selection;

    Uri contentUri,resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_management_settings);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(Table_settings.this);
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

        Bundle extras = getIntent().getExtras();
        position = extras.getString("position");
        floor = extras.getString("floor");
        selected_section = extras.getString("selected_section");

        System.out.println("settings "+position+" "+floor+" "+selected_section);

        TextView no_tabs = (TextView) findViewById(R.id.no_tabs);
        Cursor cursor = db.rawQuery("SELECT Count(*) FROM asd1 WHERE floor = '"+floor+"'", null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();

        ImageButton btncancel = (ImageButton) findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
                Intent intent_table = new Intent(Table_settings.this, Tables_list_Activity.class);
                intent_table.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent_table.putExtra("position", position);
                intent_table.putExtra("from", "Settings");
                startActivity(intent_table);
            }
        });

        no_tabs.setText(String.valueOf(count));

        RelativeLayout tabs_no = (RelativeLayout) findViewById(R.id.tabs_no);
        tabs_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Table_settings.this, R.style.notitle);
                dialog.setContentView(R.layout.tab_management_settings_dialog_notabs);
                dialog.show();

                Cursor cursor = db.rawQuery("SELECT Count(*) FROM asd1 WHERE floor = '"+floor+"'", null);
                if (cursor.moveToFirst()) {
                    count = cursor.getInt(0);
                }
                cursor.close();

                ImageButton btncancel = (ImageButton) dialog.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideKeyboard(Table_settings.this);
                        donotshowKeyboard(Table_settings.this);
                        dialog.dismiss();
                    }
                });

                EditText no_of_tabs = (EditText) dialog.findViewById(R.id.no_of_tabs);
                no_of_tabs.setText(no_tabs.getText().toString());

                ImageView btnsave = (ImageView) dialog.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (no_of_tabs.getText().toString().equals("") || no_of_tabs.getText().toString().equals("0")) {
                            no_of_tabs.setError("Enter minimum 1 table");
                        }else {
                            if (Integer.parseInt(no_of_tabs.getText().toString()) == count) {//no need to do anything

                            }else {
                                if (Integer.parseInt(no_of_tabs.getText().toString()) > count) {//add table
                                    System.out.println("add table1 "+no_of_tabs.getText().toString()+" "+count);

                                    int diff = Integer.parseInt(no_of_tabs.getText().toString()) - count;

                                    for (int i=1; i<=diff; i++) {
                                        Cursor cursorr = db.rawQuery("SELECT _id FROM asd1", null);
                                        int count = cursorr.getCount();
                                        int countt = count + 1;
                                        String count2 = String.valueOf(countt);

                                        Cursor cursorr1 = db.rawQuery("SELECT _id FROM asd1 WHERE position = '"+position+"'", null);
                                        int count1 = cursorr1.getCount();
                                        int countt1 = count1 + 1;
                                        String count21 = String.valueOf(countt1);

                                        byte[] byteImage1;
                                        byte[] img;
                                        ContentValues newValues = new ContentValues();
                                        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.c_table_empty_normal_6d6e71);
                                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                        b.compress(Bitmap.CompressFormat.PNG, 100, bos);
                                        img = bos.toByteArray();
                                        newValues.put("image", img);
//                                        newValues.put("_id", count2);
                                        newValues.put("pName", "");
                                        newValues.put("pDate", count21);
                                        newValues.put("floor",floor);
                                        newValues.put("position", position);
                                        newValues.put("max", "4");
                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "asd1");
                                        resultUri = getContentResolver().insert(contentUri, newValues);
                                        getContentResolver().notifyChange(resultUri, null);
//                                        db.insert("asd1", null, newValues);

                                        no_tabs.setText(no_of_tabs.getText().toString());
                                        count = Integer.parseInt(no_of_tabs.getText().toString());
                                        dialog.dismiss();
                                        hideKeyboard(Table_settings.this);
                                        donotshowKeyboard(Table_settings.this);

                                    }
                                }else {//delete table
                                    System.out.println("add table2 "+no_of_tabs.getText().toString()+" "+count);
                                    int diff = count - Integer.parseInt(no_of_tabs.getText().toString());
                                    int diff1 = count - diff;

                                    String id = "";
                                    Cursor cursor1 = db.rawQuery("SELECT * FROM asd1 Where pDate = '"+diff1+"' and position = '"+position+"'", null);
                                    if (cursor1.moveToFirst()) {
                                        id = cursor1.getString(0);
                                    }
                                    cursor1.close();

//                                    db.execSQL("delete from asd1 where pDate > '"+diff1+"' AND position = '"+position+"'");
//                                    webservicequery("delete from asd1 where pDate > '"+diff1+"' AND position = '"+position+"'");
                                    db.execSQL("delete from asd1 where _id > '"+id+"' AND position = '"+position+"'");
                                    webservicequery("delete from asd1 where _id > '"+id+"' AND position = '"+position+"'");
                                    System.out.println("delete from asd1 where _id > '"+id+"' AND position = '"+position+"'");

                                    no_tabs.setText(no_of_tabs.getText().toString());
                                    count = Integer.parseInt(no_of_tabs.getText().toString());
                                    dialog.dismiss();
                                    hideKeyboard(Table_settings.this);
                                    donotshowKeyboard(Table_settings.this);

//                                    for (int i=1; i<=diff; i++) {
//                                        Cursor cursorr = db.rawQuery("SELECT _id FROM asd1", null);
//                                        int count = cursorr.getCount();
//                                        int countt = count + 1;
//                                        String count2 = String.valueOf(countt);
//
//                                        Cursor cursorr1 = db.rawQuery("SELECT _id FROM asd1 WHERE position = '"+position+"'", null);
//                                        int count1 = cursorr1.getCount();
//                                        int countt1 = count1 + 1;
//                                        String count21 = String.valueOf(countt1);
//
//                                        byte[] byteImage1;
//                                        byte[] img;
//                                        ContentValues newValues = new ContentValues();
//                                        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.c_table_empty_normal_6d6e71);
//                                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                                        b.compress(Bitmap.CompressFormat.PNG, 100, bos);
//                                        img = bos.toByteArray();
//                                        newValues.put("image", img);
//                                        newValues.put("_id", count2);
//                                        newValues.put("pName", "");
//                                        newValues.put("pDate", count21);
//                                        newValues.put("floor",floor);
//                                        newValues.put("position", position);
//                                        newValues.put("max", "4");
//                                        db.insert("asd1", null, newValues);
//                                    }
                                }
                            }

                        }

                    }
                });
            }
        });

        RelativeLayout tabs_setup = (RelativeLayout) findViewById(R.id.tabs_setup);
        tabs_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Table_settings.this, Table_setup_configuration.class);
                intent.putExtra("position", position);
                intent.putExtra("floor", floor);
                intent.putExtra("selected_section", selected_section);
                startActivity(intent);
            }
        });

        TextView section_name = (TextView) findViewById(R.id.section_name);
        section_name.setText(floor);

        RelativeLayout section_edit = (RelativeLayout) findViewById(R.id.section_edit);
        section_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Table_settings.this, R.style.notitle);
                dialog.setContentView(R.layout.tab_management_dialog_edit_section);
                dialog.show();

                ImageButton btncancel = (ImageButton) dialog.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        hideKeyboard(Table_settings.this);
                        donotshowKeyboard(Table_settings.this);
                    }
                });


                EditText editText_section = (EditText) dialog.findViewById(R.id.editText_section);
                editText_section.setText(section_name.getText().toString());

                ImageView btnsave = (ImageView) dialog.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (editText_section.getText().toString().equals("")) {
                            editText_section.setError("Enter section name");
                        }else {
                            if (editText_section.getText().toString().equals(section_name.getText().toString())) {

                            }else {
                                Cursor cursor1 = db.rawQuery("SELECT * FROM floors WHERE floorname = '"+editText_section.getText().toString()+"'", null);
                                if (cursor1.moveToFirst()) {
                                    editText_section.setError("Section name already used");
                                }else {

                                    db.execSQL("UPDATE asd1 SET floor = '" + editText_section.getText().toString() + "' WHERE floor = '" + section_name.getText().toString() + "'");
                                    webservicequery("UPDATE asd1 SET floor = '" + editText_section.getText().toString() + "' WHERE floor = '" + section_name.getText().toString() + "'");

                                    db.execSQL("UPDATE floors SET floorname = '" + editText_section.getText().toString() + "' WHERE floorname = '" + section_name.getText().toString() + "'");
                                    webservicequery("UPDATE floors SET floorname = '" + editText_section.getText().toString() + "' WHERE floorname = '" + section_name.getText().toString() + "'");

                                    dialog.dismiss();

                                    section_name.setText(editText_section.getText().toString());

                                    hideKeyboard(Table_settings.this);
                                    donotshowKeyboard(Table_settings.this);

//                                ContentValues contentValues = new ContentValues();
//                                contentValues.put("floor", editText_section.getText().toString());
//                                String where = "floor = '" + floor + "'";
//
//                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "asd1");
//                                getContentResolver().update(contentUri, contentValues, where, new String[]{});
//                                resultUri = new Uri.Builder()
//                                        .scheme("content")
//                                        .authority(StubProviderApp.AUTHORITY)
//                                        .path("asd1")
//                                        .appendQueryParameter("operation", "update")
//                                        .appendQueryParameter("floor", floor)
//                                        .build();
//                                getContentResolver().notifyChange(resultUri, null);
//
//                                ContentValues contentValues1 = new ContentValues();
//                                contentValues1.put("floorname", editText_section.getText().toString());
//                                String where1 = "floorname = '" + floor + "'";
//
//                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "floors");
//                                getContentResolver().update(contentUri, contentValues1, where1, new String[]{});
//                                resultUri = new Uri.Builder()
//                                        .scheme("content")
//                                        .authority(StubProviderApp.AUTHORITY)
//                                        .path("floors")
//                                        .appendQueryParameter("operation", "update")
//                                        .appendQueryParameter("floorname", floor)
//                                        .build();
//                                getContentResolver().notifyChange(resultUri, null);
                                }
                                cursor1.close();

                            }
                        }
                    }
                });
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        db.close();
//        finish();
        Intent intent_table = new Intent(Table_settings.this, Tables_list_Activity.class);
        intent_table.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent_table.putExtra("position", position);
        intent_table.putExtra("from", "Settings");
        startActivity(intent_table);
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


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(Table_settings.this);
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
        RequestQueue queue;
        StringRequest sr1;
        // queue = Volley.newRequestQueue(getActivity());

        queue= RequestSingleton.getInstance(Table_settings.this).getInstance();

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
