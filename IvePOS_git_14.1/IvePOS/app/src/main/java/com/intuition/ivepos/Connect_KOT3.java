package com.intuition.ivepos;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intuition.ivepos.syncapp.StubProviderApp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Connect_KOT3 extends AppCompatActivity {

    public SQLiteDatabase db = null;
    String nameget, addget, statusget, deviceget;
    String ipget, portget, p_statusget, p_name_get;
    TextView item_selec_number;
    String name;
    Uri contentUri,resultUri;

    EditText editText1;

    String WebserviceUrl;
    String account_selection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kot_slot_settings);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(Connect_KOT3.this);
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

        TextView user = (TextView) findViewById(R.id.user);
        user.setText("KOT3");
        final TextView dept_name = (TextView) findViewById(R.id.dept_name);
        dept_name.setText("dept. name3");
        TextView conn_status = (TextView) findViewById(R.id.conn_status);
        TextView printer_name = (TextView) findViewById(R.id.printer_name);
        item_selec_number = (TextView) findViewById(R.id.item_selec_number);

        Cursor cursor_dept_name = db.rawQuery("SELECT * FROM Name_Dept3", null);
        if (cursor_dept_name.moveToFirst()){
//            Toast.makeText(Connect_KOT3.this, "data si there", Toast.LENGTH_LONG).show();
            String name = cursor_dept_name.getString(1);
            dept_name.setText(name);
        }else {
//            Toast.makeText(Connect_KOT3.this, "no data", Toast.LENGTH_LONG).show();
        }


        Cursor cursor = db.rawQuery("SELECT * FROM IPConn_KOT3", null);
        if (cursor.moveToFirst()) {
            ipget = cursor.getString(1);
            portget = cursor.getString(2);
            p_statusget = cursor.getString(3);
            p_name_get = cursor.getString(4);
            if (p_statusget.toString().equals("ok")) {
                conn_status.setText("Connected");
                printer_name.setText(p_name_get);
            }
        }

        Cursor cursor1 = db.rawQuery("SELECT COUNT(dept_name) FROM Items WHERE dept_name = '"+dept_name.getText().toString()+"'", null);
        if (cursor1.moveToFirst()){
            int l = cursor1.getInt(0);
            String co = String.valueOf(l);
            item_selec_number.setText(co+" selected");
        }
        cursor1.close();

        ImageButton btncancel = (ImageButton) findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (account_selection.toString().equals("Dine")) {
                    Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                    if (cursor3.moveToFirst()) {
                        String lite_pro = cursor3.getString(1);

                        TextView tv = new TextView(Connect_KOT3.this);
                        tv.setText(lite_pro);

                        if (tv.getText().toString().equals("Lite")) {
                            Intent intent = new Intent(Connect_KOT3.this, BeveragesMenuFragment_Dine_l.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(Connect_KOT3.this, BeveragesMenuFragment_Dine.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }else {
                        Intent intent = new Intent(Connect_KOT3.this, BeveragesMenuFragment_Dine_l.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }else {
                    if (account_selection.toString().equals("Qsr")) {
                        Intent intent = new Intent(Connect_KOT3.this, BeveragesMenuFragment_Qsr.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                LoginActivity.this.finish();
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(Connect_KOT3.this, BeveragesMenuFragment_Retail.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                LoginActivity.this.finish();
                        startActivity(intent);
                    }
                }
//                Intent intent = new Intent(Connect_KOT3.this, BeveragesMenuFragment_Dine.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                LoginActivity.this.finish();
//                startActivity(intent);
            }
        });

        RelativeLayout department_name = (RelativeLayout) findViewById(R.id.department_name);
        department_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialogauth = new Dialog(Connect_KOT3.this, R.style.timepicker_date_dialog);
                dialogauth.setContentView(R.layout.dialog_department_name);
                dialogauth.show();

                final EditText editText1 = (EditText) dialogauth.findViewById(R.id.editText1);

                Cursor cursor_dept_name = db.rawQuery("SELECT * FROM Name_Dept3", null);
                if (cursor_dept_name.moveToFirst()){
//                    Toast.makeText(Connect_KOT3.this, "data si there", Toast.LENGTH_LONG).show();
                    name = cursor_dept_name.getString(1);
                    editText1.setText(name);
                }else {
//                    Toast.makeText(Connect_KOT3.this, "no data", Toast.LENGTH_LONG).show();
                }

                ImageButton btncancel = (ImageButton) dialogauth.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogauth.dismiss();

                        donotshowKeyboard(Connect_KOT3.this);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        }

                    }
                });

                Button btnsave = (Button) dialogauth.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ContentValues cv = new ContentValues();
                        cv.put("dept3_name", editText1.getText().toString());
                        String where1 = "_id = '1' ";
//                        db.update("Name_Dept3", cv, where1, new String[]{});
                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Name_Dept3");
                        getContentResolver().update(contentUri, cv,where1,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("Name_Dept3")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id","1")
                                .build();
                        getContentResolver().notifyChange(resultUri, null);

                        DownloadMusicfromInternetde downloadMusicfromInternetde_update01 = new DownloadMusicfromInternetde();
                        downloadMusicfromInternetde_update01.execute();



                        dept_name.setText(editText1.getText().toString());

                        dialogauth.dismiss();
                        donotshowKeyboard(Connect_KOT3.this);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        }
                    }
                });
            }
        });

        RelativeLayout printer_selec = (RelativeLayout) findViewById(R.id.printer_selec);
        printer_selec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intenttt = new Intent(Connect_KOT3.this, SearchIPActivity_KOT3.class);
                intenttt.putExtra("dept_name_get", dept_name.getText().toString());
                startActivity(intenttt);
                donotshowKeyboard(Connect_KOT3.this);
            }
        });

        RelativeLayout item_selec = (RelativeLayout) findViewById(R.id.item_selec);
        item_selec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Connect_KOT3.this, KOT_MultiSelection_items.class);
                intent.putExtra("dept_name_get", dept_name.getText().toString());
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (account_selection.toString().equals("Dine")) {
            Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
            if (cursor3.moveToFirst()) {
                String lite_pro = cursor3.getString(1);

                TextView tv = new TextView(Connect_KOT3.this);
                tv.setText(lite_pro);

                if (tv.getText().toString().equals("Lite")) {
                    Intent intent = new Intent(Connect_KOT3.this, BeveragesMenuFragment_Dine_l.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(Connect_KOT3.this, BeveragesMenuFragment_Dine.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }else {
                Intent intent = new Intent(Connect_KOT3.this, BeveragesMenuFragment_Dine_l.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }else {
            if (account_selection.toString().equals("Qsr")) {
                Intent intent = new Intent(Connect_KOT3.this, BeveragesMenuFragment_Qsr.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                LoginActivity.this.finish();
                startActivity(intent);
            }else {
                Intent intent = new Intent(Connect_KOT3.this, BeveragesMenuFragment_Retail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                LoginActivity.this.finish();
                startActivity(intent);
            }
        }

//        Intent intent = new Intent(Connect_KOT3.this, BeveragesMenuFragment_Dine.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                LoginActivity.this.finish();
//        startActivity(intent);
        super.onBackPressed();
    }

    public static void donotshowKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(Add_manage_ingredient_Activity.this, "1", Toast.LENGTH_LONG).show();
        if (requestCode == 1) {
//            Toast.makeText(Add_manage_ingredient_Activity.this, "2", Toast.LENGTH_LONG).show();
            if(resultCode == RESULT_OK) {
//                Toast.makeText(Add_manage_ingredient_Activity.this, "3", Toast.LENGTH_LONG).show();
                String strEditText = data.getStringExtra("editTextValue");
                item_selec_number.setText(strEditText+" selected");
            }
        }
    }



    class DownloadMusicfromInternetde extends AsyncTask<String, Void, Integer> {
        private final ProgressDialog dialog1_c = new ProgressDialog(Connect_KOT3.this, R.style.timepicker_date_dialog);

        @Override
        protected Integer doInBackground(String... params) {
            try {

                Cursor cursor2 = db.rawQuery("SELECT * FROM Items WHERE dept_name = '"+name+"'", null);
                if (cursor2.moveToFirst()){
                    do {
                        String id = cursor2.getString(0);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("dept_name", editText1.getText().toString());
                        String where = " _id ='" + id + "'";
                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                        getContentResolver().update(contentUri, contentValues,where,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("Items")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id",id)
                                .build();
                        getContentResolver().notifyChange(resultUri, null);
//                        db.update("Items", contentValues, where, new String[]{});
                    }while (cursor2.moveToNext());
                }

                Cursor cursor3 =  db.rawQuery("SELECT * FROM IPConn_KOT1 WHERE kot_name = '"+name+"'", null);
                if (cursor3.moveToFirst()){
                    String id = cursor3.getString(0);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("kot_name", editText1.getText().toString());
                    String where = " _id ='" + id + "'";
//                    db.update("IPConn_KOT1", contentValues, where, new String[]{});
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "IPConn_KOT1");
                    getContentResolver().update(contentUri, contentValues,where,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("IPConn_KOT1")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id",id)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
                }
                cursor3.close();

                Cursor cursor4 =  db.rawQuery("SELECT * FROM IPConn_KOT2 WHERE kot_name = '"+name+"'", null);
                if (cursor4.moveToFirst()){
                    String id = cursor4.getString(0);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("kot_name", editText1.getText().toString());
                    String where = " _id ='" + id + "'";
//                    db.update("IPConn_KOT2", contentValues, where, new String[]{});
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "IPConn_KOT2");
                    getContentResolver().update(contentUri, contentValues,where,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("IPConn_KOT2")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id",id)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
                }
                cursor4.close();

                Cursor cursor5 =  db.rawQuery("SELECT * FROM IPConn_KOT3 WHERE kot_name = '"+name+"'", null);
                if (cursor5.moveToFirst()){
                    String id = cursor5.getString(0);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("kot_name", editText1.getText().toString());
                    String where = " _id ='" + id + "'";
//                    db.update("IPConn_KOT3", contentValues, where, new String[]{});
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "IPConn_KOT3");
                    getContentResolver().update(contentUri, contentValues,where,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("IPConn_KOT3")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id",id)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
                }
                cursor5.close();

                Cursor cursor6 =  db.rawQuery("SELECT * FROM IPConn_KOT4 WHERE kot_name = '"+name+"'", null);
                if (cursor6.moveToFirst()){
                    String id = cursor6.getString(0);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("kot_name", editText1.getText().toString());
                    String where = " _id ='" + id + "'";
//                    db.update("IPConn_KOT4", contentValues, where, new String[]{});
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "IPConn_KOT4");
                    getContentResolver().update(contentUri, contentValues,where,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("IPConn_KOT4")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id",id)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);
                }
                cursor6.close();

            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.dialog1_c.setMessage("Loading...");
            this.dialog1_c.show();

//            progressbar_dialog.setVisibility(View.VISIBLE);
//            header_dialog.setVisibility(View.INVISIBLE);
//            content_dialog.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(Integer file_url) {

            dialog1_c.dismiss();
        }
    }

}
