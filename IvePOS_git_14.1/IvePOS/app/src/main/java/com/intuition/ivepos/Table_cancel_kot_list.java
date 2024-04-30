package com.intuition.ivepos;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.intuition.ivepos.csv.RequestSingleton;
import com.intuition.ivepos.sync.StubProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

public class Table_cancel_kot_list extends AppCompatActivity {

    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;

    ListView listView;

    EditText editText1_filter, editText2_filter;
    TextView editText1, editText2, editText11, editText22;
    TextView editText_from_day_hide, editText_from_day_visible, editText_to_day_hide, editText_to_day_visible;

    LinearLayout user_click, transactions_click, amount_click;

    ImageView arrow_user, arrow_transactions, arrow_amount;
    int i = 1, i1 = 1;

    ExtendedFloatingActionButton additem;
    RelativeLayout linearLayout;
    TextView spinnercat, spinneruser;
    EditText myFilter;
    ListView popupSpinner;
    List<String> myList;
    EditText myFilter1;
    String timeget;
    Uri contentUri,resultUri;

    TextView editText1_dialog,editText2_dialog,editText11_dialog,editText22_dialog,editText_from_day_hide_dialog,editText_from_day_visible_dialog;
    TextView editText_to_day_hide_dialog,editText_to_day_visible_dialog;
    String str_editText11_dialog="",str_editText22_dialog="",str_editText_from_day_visible_dialog="",str_editText_to_day_visible_dialog="";
    String onee, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve;
    String onee1, two1, three1, four1, five1, six1, seven1, eight1, nine1, ten1, eleven1, twelve1;
    private int year, year1;
    private int month, month1;
    private int day, day1;
    private int hour;
    private int minute;
    TextView tv_dateselecter;

    String date1 = "201707210001";
    String date2 = "201707212359";

    Dialog dialog;
    String dialog_date, dialog_datetimee_new;

    String u_name, u_username, username, password;
    String insert1_cc = "", insert1_rs = "", str_country;

    String player1name, player2name;
    String WebserviceUrl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_kot_cancel_listview);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE,null);

        Bundle extras = getIntent().getExtras();
        player1name = extras.getString("PLAYER1NAME");
        player2name = extras.getString("PLAYER2NAME");
        String str_edittext1 = extras.getString("edittext1");
        String str_edittext11 = extras.getString("edittext11");
        String str_edittext2 = extras.getString("edittext2");
        String str_edittext22 = extras.getString("edittext22");
        String str_edittext_from_day_visible = extras.getString("edittext_from_day_visible");
        String str_edittext_from_day_hide = extras.getString("edittext_from_day_hide");
        String str_edittext_to_day_visible = extras.getString("edittext_to_day_visible");
        String str_edittext_to_day_hide = extras.getString("edittext_to_day_hide");

        TextView editText1_filter = new TextView(Table_cancel_kot_list.this);
        TextView editText2_filter = new TextView(Table_cancel_kot_list.this);
        TextView editText1 = new TextView(Table_cancel_kot_list.this);
        TextView editText11 = new TextView(Table_cancel_kot_list.this);
        TextView editText2 = new TextView(Table_cancel_kot_list.this);
        TextView editText22 = new TextView(Table_cancel_kot_list.this);
        TextView editText_from_day_visible = new TextView(Table_cancel_kot_list.this);
        TextView editText_from_day_hide = new TextView(Table_cancel_kot_list.this);
        TextView editText_to_day_visible = new TextView(Table_cancel_kot_list.this);
        TextView editText_to_day_hide = new TextView(Table_cancel_kot_list.this);

        editText1_filter.setText(player1name);
        editText2_filter.setText(player2name);

        editText1.setText(str_edittext1);
        editText11.setText(str_edittext11);
        editText2.setText(str_edittext2);
        editText22.setText(str_edittext22);
        editText_from_day_visible.setText(str_edittext_from_day_visible);
        editText_from_day_hide.setText(str_edittext_from_day_hide);
        editText_to_day_visible.setText(str_edittext_to_day_visible);
        editText_to_day_hide.setText(str_edittext_to_day_hide);


        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(Table_cancel_kot_list.this);
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

        Cursor cursor_user = db.rawQuery("SELECT * FROM LoginUser", null);
        if (cursor_user.moveToFirst()) {
            u_username = cursor_user.getString(1);
            username = cursor_user.getString(1);
            password = cursor_user.getString(2);
            Cursor ladmin = db.rawQuery("SELECT * FROM LAdmin WHERE username = '" + u_username + "'", null);
            if (ladmin.moveToFirst()) {
                u_name = ladmin.getString(3);
            } else {
                Cursor madmin = db.rawQuery("SELECT * FROM LOGIN WHERE username = '" + u_username + "'", null);
                if (madmin.moveToFirst()) {
                    u_name = madmin.getString(3);
                } else {
                    Cursor user1 = db.rawQuery("SELECT * FROM User1 WHERE username = '" + u_username + "'", null);
                    if (user1.moveToFirst()) {
                        u_name = user1.getString(1);
                    } else {
                        Cursor user2 = db.rawQuery("SELECT * FROM User2 WHERE username = '" + u_username + "'", null);
                        if (user2.moveToFirst()) {
                            u_name = user2.getString(1);
                        } else {
                            Cursor user3 = db.rawQuery("SELECT * FROM User3 WHERE username = '" + u_username + "'", null);
                            if (user3.moveToFirst()) {
                                u_name = user3.getString(1);
                            } else {
                                Cursor user4 = db.rawQuery("SELECT * FROM User4 WHERE username = '" + u_username + "'", null);
                                if (user4.moveToFirst()) {
                                    u_name = user4.getString(1);
                                } else {
                                    Cursor user5 = db.rawQuery("SELECT * FROM User5 WHERE username = '" + u_username + "'", null);
                                    if (user5.moveToFirst()) {
                                        u_name = user5.getString(1);
                                    } else {
                                        Cursor user6 = db.rawQuery("SELECT * FROM User6 WHERE username = '" + u_username + "'", null);
                                        if (user6.moveToFirst()) {
                                            u_name = user6.getString(1);
                                        } else {

                                        }
                                        user6.close();
                                    }
                                    user5.close();
                                }
                                user4.close();
                            }
                            user3.close();
                        }
                        user2.close();
                    }
                    user1.close();
                }
                madmin.close();
            }
            ladmin.close();
        }
        cursor_user.close();

        donotshowKeyboard(Table_cancel_kot_list.this);

        LinearLayout back_activity = (LinearLayout) findViewById(R.id.back_activity);
        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*
//        DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
//        downloadMusicfromInternet.execute(editText1_filter.getText().toString() + editText2_filter.getText().toString());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                System.out.println("5 seconds2");
                DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
                downloadMusicfromInternet.execute(editText1_filter.getText().toString() + editText2_filter.getText().toString());
            }
        }, 500);

         */

        db1.execSQL("Create table if not exists dummy_table_kot (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, tableid text, transactions text, amount integer);");

        listView = (ListView) findViewById(R.id.listView);

        DownloadMusicfromInternet1 downloadMusicfromInternet = new DownloadMusicfromInternet1();
        downloadMusicfromInternet.execute(editText1_filter.getText().toString() + editText2_filter.getText().toString());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView onee = (TextView) view.findViewById(R.id.tableid);
//                Toast.makeText(Category_list.this, onee.getText().toString(), Toast.LENGTH_LONG).show();

                Dialog dialog = new Dialog(Table_cancel_kot_list.this, R.style.timepicker_date_dialog);
                dialog.setContentView(R.layout.dialog_table);
                dialog.show();

                ImageButton btncancel = (ImageButton) dialog.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                TextView tableid = (TextView) dialog.findViewById(R.id.tableid);
                tableid.setText(onee.getText().toString());

                ListView listView = (ListView) dialog.findViewById(R.id.list);

                final String selectQuery = "SELECT * FROM Kotcancelled WHERE datetimee_new >= '"+player1name+"' AND datetimee_new <='"+player2name+"' AND tableid = '"+onee.getText().toString()+"'";

                Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                // The desired columns to be bound
                final String[] fromFieldNames = {"date1", "time", "itemname", "qty", "price"};
                final int[] toViewsID = {R.id.date, R.id.time, R.id.itemname, R.id.qty, R.id.price};
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(Table_cancel_kot_list.this, R.layout.dialog_table_cancel_kot_listview, cursor1, fromFieldNames, toViewsID, 0);
                listView.setAdapter(adapter);

            }
        });

        arrow_user = (ImageView) findViewById(R.id.arrow_user);
        arrow_transactions = (ImageView) findViewById(R.id.arrow_transactions);
        arrow_amount = (ImageView) findViewById(R.id.arrow_amount);

        user_click = (LinearLayout) findViewById(R.id.user_click);
        transactions_click = (LinearLayout) findViewById(R.id.transactions_click);
        amount_click = (LinearLayout) findViewById(R.id.amount_click);

        user_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrow_transactions.setRotation(360);
                arrow_amount.setRotation(360);
                if (i ==1) {
                    arrow_user.setRotation(180);

                    final String selectQuery = "SELECT * FROM dummy_table_kot ORDER BY tableid DESC";
                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"tableid", "transactions", "amount"};
                    final int[] toViewsID = {R.id.tableid, R.id.no_of_transactions, R.id.amount};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(Table_cancel_kot_list.this, R.layout.table_cancel_kot_layout, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);

                    i = 2;

                }else {
                    arrow_user.setRotation(360);

                    final String selectQuery = "SELECT * FROM dummy_table_kot ORDER BY tableid  ASC";
                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"tableid", "transactions", "amount"};
                    final int[] toViewsID = {R.id.tableid, R.id.no_of_transactions, R.id.amount};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(Table_cancel_kot_list.this, R.layout.table_cancel_kot_layout, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);

                    i = 1;

                }
            }
        });

        transactions_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrow_user.setRotation(360);
                arrow_amount.setRotation(360);
                if (i ==1) {
                    arrow_transactions.setRotation(180);

                    final String selectQuery = "SELECT * FROM dummy_table_kot ORDER BY CAST(transactions AS unsigned) DESC";
                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"tableid", "transactions", "amount"};
                    final int[] toViewsID = {R.id.tableid, R.id.no_of_transactions, R.id.amount};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(Table_cancel_kot_list.this, R.layout.table_cancel_kot_layout, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);

                    i = 2;

                }else {
                    arrow_transactions.setRotation(360);

                    final String selectQuery = "SELECT * FROM dummy_table_kot ORDER BY CAST(transactions AS unsigned) ASC";
                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"tableid", "transactions", "amount"};
                    final int[] toViewsID = {R.id.tableid, R.id.no_of_transactions, R.id.amount};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(Table_cancel_kot_list.this, R.layout.table_cancel_kot_layout, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);

                    i = 1;

                }
            }
        });

        amount_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrow_user.setRotation(360);
                arrow_transactions.setRotation(360);
                if (i ==1) {
                    arrow_amount.setRotation(180);

                    final String selectQuery = "SELECT * FROM dummy_table_kot ORDER BY CAST(amount AS unsigned) DESC";
                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"tableid", "transactions", "amount"};
                    final int[] toViewsID = {R.id.tableid, R.id.no_of_transactions, R.id.amount};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(Table_cancel_kot_list.this, R.layout.table_cancel_kot_layout, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);

                    i = 2;

                }else {
                    arrow_amount.setRotation(360);

                    final String selectQuery = "SELECT * FROM dummy_table_kot ORDER BY CAST(amount AS unsigned) ASC";
                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"tableid", "transactions", "amount"};
                    final int[] toViewsID = {R.id.tableid, R.id.no_of_transactions, R.id.amount};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(Table_cancel_kot_list.this, R.layout.table_cancel_kot_layout, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);

                    i = 1;

                }
            }
        });

    }

    public ArrayList<String> getTableValuesall() {
        ArrayList<String> my_array = new ArrayList<String>();
        try {
            my_array.add("Rent");
            my_array.add("Transportation");
            my_array.add("Utilities");
            my_array.add("Maintenance");
            my_array.add("Miscellaneous");
            my_array.add("Medical");
            //db.close();
        } catch (Exception e) {
            Toast.makeText(Table_cancel_kot_list.this, "Error encountered.",
                    Toast.LENGTH_LONG);
        }
        return my_array;
    }

    public ArrayList<String> getUserValuesall() {
        ArrayList<String> my_array = new ArrayList<String>();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM LOGIN", null);
            if (cursor.moveToFirst()) {
                String user = cursor.getString(3);
                my_array.add(user);
            }
            cursor = db.rawQuery("SELECT * FROM LAdmin", null);
            if (cursor.moveToFirst()) {
                String user = cursor.getString(3);
                my_array.add(user);
            }
            cursor = db.rawQuery("SELECT * FROM User1", null);
            if (cursor.moveToFirst()) {
                String user = cursor.getString(1);
                my_array.add(user);
            }
            cursor = db.rawQuery("SELECT * FROM User2", null);
            if (cursor.moveToFirst()) {
                String user = cursor.getString(1);
                my_array.add(user);
            }
            cursor = db.rawQuery("SELECT * FROM User3", null);
            if (cursor.moveToFirst()) {
                String user = cursor.getString(1);
                my_array.add(user);
            }
            cursor = db.rawQuery("SELECT * FROM User4", null);
            if (cursor.moveToFirst()) {
                String user = cursor.getString(1);
                my_array.add(user);
            }
            cursor = db.rawQuery("SELECT * FROM User5", null);
            if (cursor.moveToFirst()) {
                String user = cursor.getString(1);
                my_array.add(user);
            }
            cursor = db.rawQuery("SELECT * FROM User6", null);
            if (cursor.moveToFirst()) {
                String user = cursor.getString(1);
                my_array.add(user);
            }
            //db.close();
        } catch (Exception e) {
            Toast.makeText(Table_cancel_kot_list.this, "Error encountered.",
                    Toast.LENGTH_LONG);
        }
        return my_array;
    }

    public static void donotshowKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void webservicequery_sales(final String webserviceQuery){


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(Table_cancel_kot_list.this);
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
        RequestQueue queue;
        StringRequest sr1;
        // queue = Volley.newRequestQueue(Table_cancel_kot_list.this);

        queue= RequestSingleton.getInstance(Table_cancel_kot_list.this).getInstance();

        sr1 = new StringRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl + "webservicequery_sales.php",
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


    class DownloadMusicfromInternet1 extends AsyncTask<String, Void, Integer> {

        private ProgressDialog dialog = new ProgressDialog(Table_cancel_kot_list.this, R.style.timepicker_date_dialog);


        @Override
        protected Integer doInBackground(String... params) {

            db1.execSQL("delete from dummy_table_kot");

            float icount1 = 0, icount2 = 0;
            final String selectQuery = "SELECT * FROM Kotcancelled WHERE datetimee_new >= '"+player1name+"' AND datetimee_new <='"+player2name+"' GROUP BY tableid";

            Cursor cursor = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
            if (cursor.moveToFirst()) {
                do {
                    String tableid = cursor.getString(3);


                    Cursor cursor1 = db1.rawQuery("SELECT SUM(total) FROM Kotcancelled WHERE tableid = '"+tableid+"' AND datetimee_new >= '"+player1name+"' AND datetimee_new <='"+player2name+"' GROUP BY tableid", null);
                    if (cursor1.moveToFirst()) {
                        icount1 = cursor1.getFloat(0);
                    }
                    cursor1.close();

                    Cursor cursor2 = db1.rawQuery("SELECT Count(*) FROM Kotcancelled WHERE tableid = '"+tableid+"' AND datetimee_new >= '"+player1name+"' AND datetimee_new <='"+player2name+"' GROUP BY tableid", null);
                    if (cursor2.moveToFirst()) {
                        icount2 = cursor2.getFloat(0);
                    }
                    cursor2.close();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("tableid", tableid);
                    contentValues.put("amount", String.valueOf(icount1));
                    contentValues.put("transactions", String.valueOf(icount2));
                    db1.insert("dummy_table_kot", null, contentValues);
                }while (cursor.moveToNext());
            }
            cursor.close();

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


        // Once Music File is downloaded
        @Override
        protected void onPostExecute(Integer file_url) {
            dialog.dismiss();

            final String selectQuery = "SELECT * FROM dummy_table_kot";

            Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
            // The desired columns to be bound
            final String[] fromFieldNames = {"tableid", "transactions", "amount"};
            final int[] toViewsID = {R.id.tableid, R.id.no_of_transactions, R.id.amount};
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(Table_cancel_kot_list.this, R.layout.table_cancel_kot_layout, cursor1, fromFieldNames, toViewsID, 0);
            listView.setAdapter(adapter);

        }

    }

}
