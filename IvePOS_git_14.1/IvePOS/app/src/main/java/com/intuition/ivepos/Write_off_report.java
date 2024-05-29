package com.intuition.ivepos;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Write_off_report extends AppCompatActivity {

    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;

    ListView listView;
    EditText search;

    Cursor cursor;

    String insert1_cc = "", insert1_rs = "", str_country;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_off_report);

        LinearLayout back_activity = (LinearLayout) findViewById(R.id.back_activity);
        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.listView11);

        search = (EditText) findViewById(R.id.searchView);

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search.getWindowToken(), 0);

        ImageView deleteicon = (ImageView) findViewById(R.id.delete_icon);
        deleteicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
            }
        });


        db = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        db1 = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        Cursor cursor_country = db1.rawQuery("SELECT * FROM Country_Selection", null);
        if (cursor_country.moveToFirst()){
            str_country = cursor_country.getString(1);
        }

        if (str_country.toString().equals("Rupee") || str_country.toString().equals("India")) {
            insert1_cc = "\u20B9";
            insert1_rs = "Rs.";
        }else {
            if (str_country.toString().equals("Pound") || str_country.toString().equals("UK Pound")) {
                insert1_cc = "\u00a3";
                insert1_rs = "BP.";
            }else {
                if (str_country.toString().equals("Euro") || str_country.toString().equals("UK Euro")) {
                    insert1_cc = "\u20ac";
                    insert1_rs = "EU.";
                }else {
                    if (str_country.toString().equals("Dollar")) {
                        insert1_cc = "\u0024";
                        insert1_rs = "\u0024";
                    }else {
                        if (str_country.toString().equals("Dinars")) {
                            insert1_cc = "D";
                            insert1_rs = "KD.";
                        }else {
                            if (str_country.toString().equals("Shilling")) {
                                insert1_cc = "S";
                                insert1_rs = "S.";
                            }else {
                                if (str_country.toString().equals("Ringitt")) {
                                    insert1_cc = "R";
                                    insert1_rs = "RM.";
                                }else {
                                    if (str_country.toString().equals("Rial")) {
                                        insert1_cc = "R";
                                        insert1_rs = "OR.";
                                    }else {
                                        if (str_country.toString().equals("Yen")) {
                                            insert1_cc = "\u00a5";
                                            insert1_rs = "\u00a5";
                                        }else {
                                            if (str_country.toString().equals("Papua New Guinean")) {
                                                insert1_cc = "K";
                                                insert1_rs = "K.";
                                            }else {
                                                if (str_country.toString().equals("UAE")) {
                                                    insert1_cc = "D";
                                                    insert1_rs = "DH.";
                                                }else {
                                                    if (str_country.toString().equals("South African Rand")) {
                                                        insert1_cc = "R";
                                                        insert1_rs = "R.";
                                                    }else {
                                                        if (str_country.toString().equals("Congolese Franc")) {
                                                            insert1_cc = "F";
                                                            insert1_rs = "FC.";
                                                        }else {
                                                            if (str_country.toString().equals("Qatari Riyals")) {
                                                                insert1_cc = "QAR";
                                                                insert1_rs = "QAR.";
                                                            }else {
                                                                if (str_country.toString().equals("Dirhams")) {
                                                                    insert1_cc = "AED";
                                                                    insert1_rs = "AED.";
                                                                }else {
                                                                    if (str_country.toString().equals("Kuwait Dinar")) {
                                                                        insert1_cc = "KWD";
                                                                        insert1_rs = "KWD.";
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
        downloadMusicfromInternet.execute();

    }


    class DownloadMusicfromInternet extends AsyncTask<Void, Integer, Void> {
        private ProgressDialog dialog = new ProgressDialog(Write_off_report.this, R.style.timepicker_date_dialog);

        @Override
        protected Void doInBackground(Void... params) {

            db.execSQL("delete from Write_off");


            Cursor cursor = db.rawQuery("SELECT * FROM Customerdetails GROUP BY phoneno", null);
            if (cursor.moveToFirst()){
                do {
                    String date = cursor.getString(7);
                    String time = cursor.getString(8);
                    String name = cursor.getString(1);
                    String phoneno = cursor.getString(2);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("date", date);
                    contentValues.put("time", time);
                    contentValues.put("name", name);
                    contentValues.put("phoneno", phoneno);
                    float dsirsq5 = 0;
                    Cursor write_off_t = db.rawQuery("SELECT SUM(write_off) FROM Customerdetails WHERE phoneno = '"+phoneno+"'", null);
                    if (write_off_t.moveToFirst()){
                        dsirsq5 = write_off_t.getFloat(0);
                        contentValues.put("write_off", String.valueOf(dsirsq5));
                    }
                    write_off_t.close();

                    TextView tv = new TextView(Write_off_report.this);
                    tv.setText(String.valueOf(dsirsq5));

                    if (Float.parseFloat(tv.getText().toString()) > 0) {
                        db.insert("Write_off", null, contentValues);
                    }

                }while (cursor.moveToNext());
            }

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);

            dialog.setMessage("Loading");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            dialog.setMax(1000);

            dialog.setProgress(0);

            dialog.show();
        }


        @Override
        protected void onPostExecute(Void result) {

            dialog.dismiss();

            cursor = db.rawQuery("SELECT * FROM Write_off", null);
            final String[] fromFieldNames = {"date", "time", "name", "phoneno", "write_off"};
            // the XML defined views which the data will be bound to
            //final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.billno, R.id.sales, R.id.billdetails, R.id.itemqqau};
            final int[] toViewsID = {R.id.date, R.id.time, R.id.name, R.id.phoneno, R.id.write_off};
            //Log.e("Checamos que hay id", String.valueOf(R.id.name));
            final SimpleCursorAdapter adapter = new SimpleCursorAdapter(Write_off_report.this, R.layout.write_off_listview, cursor, fromFieldNames, toViewsID);
            listView.setAdapter(adapter);

            search.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    adapter.getFilter().filter(s.toString());
                }
            });

            adapter.setFilterQueryProvider(new FilterQueryProvider() {
                public Cursor runQuery(CharSequence constraint) {

                    return fetchCountriesByName(constraint.toString());
                }
            });

        }
    }


    public Cursor fetchCountriesByName(String inputtext) throws SQLException {
//        Toast.makeText(Dinein_Takeawa_Homedeli_Sales.this, "11 "+inputtext, Toast.LENGTH_SHORT).show();

        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
            mCursor = db.rawQuery("SELECT * FROM Write_off", null);
//            mCursor = db.query("Generalorderlistascdesc1", new String[] {"_id", "date", "time", "user", "billcount", "billno", "sales"},
//                    null, null, null, null, null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Write_off WHERE phoneno LIKE '%" + inputtext + "%' OR name LIKE '%" + inputtext + "%'", null);
//            mCursor = db.query(true, "Generalorderlistascdesc1", new String[] {"date", "time", "user", "billcount", "billno", "sales"},
//                    "billno" + " like" + " '%" + inputtext + "%'", null,
//                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

}
