package com.intuition.ivepos;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class KOT_Cancel_list extends AppCompatActivity {

    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;

    ListView listView;

    EditText editText1_filter, editText2_filter;
    TextView editText1, editText2, editText11, editText22;
    TextView editText_from_day_hide, editText_from_day_visible, editText_to_day_hide, editText_to_day_visible;

    String u_name, u_username, username, password;
    String insert1_cc = "", insert1_rs = "", str_country;

    LinearLayout date_time_click, user_click, reason_click, amount_click;

    ImageView arrow_date_time, arrow_user, arrow_reason, arrow_amount;
    int i = 1, i1 = 1;

    String WebserviceUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kot_cancel_listview);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);

        Bundle extras = getIntent().getExtras();
        String player1name = extras.getString("PLAYER1NAME");
        String player2name = extras.getString("PLAYER2NAME");
        String str_edittext1 = extras.getString("edittext1");
        String str_edittext11 = extras.getString("edittext11");
        String str_edittext2 = extras.getString("edittext2");
        String str_edittext22 = extras.getString("edittext22");
        String str_edittext_from_day_visible = extras.getString("edittext_from_day_visible");
        String str_edittext_from_day_hide = extras.getString("edittext_from_day_hide");
        String str_edittext_to_day_visible = extras.getString("edittext_to_day_visible");
        String str_edittext_to_day_hide = extras.getString("edittext_to_day_hide");

        TextView editText1_filter = new TextView(KOT_Cancel_list.this);
        TextView editText2_filter = new TextView(KOT_Cancel_list.this);
        TextView editText1 = new TextView(KOT_Cancel_list.this);
        TextView editText11 = new TextView(KOT_Cancel_list.this);
        TextView editText2 = new TextView(KOT_Cancel_list.this);
        TextView editText22 = new TextView(KOT_Cancel_list.this);
        TextView editText_from_day_visible = new TextView(KOT_Cancel_list.this);
        TextView editText_from_day_hide = new TextView(KOT_Cancel_list.this);
        TextView editText_to_day_visible = new TextView(KOT_Cancel_list.this);
        TextView editText_to_day_hide = new TextView(KOT_Cancel_list.this);

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


        SharedPreferences sharedpreferences_select = SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(KOT_Cancel_list.this);
        String account_selection = sharedpreferences_select.getString("account_selection", null);

        if (account_selection.toString().equals("Dine")) {
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        } else {
            if (account_selection.toString().equals("Qsr")) {
                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
            } else {
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

        donotshowKeyboard(KOT_Cancel_list.this);

        LinearLayout back_activity = (LinearLayout) findViewById(R.id.back_activity);
        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        final String selectQuery = "SELECT * FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"'";

        Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
        // The desired columns to be bound
        final String[] fromFieldNames = {"date1", "time", "user", "reason", "total", "_id"};
        final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.reason, R.id.total, R.id.id};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(KOT_Cancel_list.this, R.layout.kot_cancel_layout, cursor1, fromFieldNames, toViewsID, 0);
        listView.setAdapter(adapter);



        arrow_date_time = (ImageView) findViewById(R.id.arrow_date_time);
        arrow_user = (ImageView) findViewById(R.id.arrow_user);
        arrow_reason = (ImageView) findViewById(R.id.arrow_reason);
        arrow_amount = (ImageView) findViewById(R.id.arrow_amount);

        date_time_click = (LinearLayout) findViewById(R.id.date_time_click);
        user_click = (LinearLayout) findViewById(R.id.user_click);
        reason_click = (LinearLayout) findViewById(R.id.reason_click);
        amount_click = (LinearLayout) findViewById(R.id.amount_click);

        date_time_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrow_user.setRotation(360);
                arrow_reason.setRotation(360);
                arrow_amount.setRotation(360);

                if (i ==1) {
                    arrow_date_time.setRotation(180);
                    final String selectQuery = "SELECT * FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ORDER BY datetimee_new DESC";

                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"date1", "time", "user", "reason", "total", "_id"};
                    final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.reason, R.id.total, R.id.id};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(KOT_Cancel_list.this, R.layout.kot_cancel_layout, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);
                    i = 2;

                }else {
                    arrow_date_time.setRotation(360);
                    final String selectQuery = "SELECT * FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ORDER BY datetimee_new ASC";

                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"date1", "time", "user", "reason", "total", "_id"};
                    final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.reason, R.id.total, R.id.id};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(KOT_Cancel_list.this, R.layout.kot_cancel_layout, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);
                    i = 1;

                }
            }
        });

        user_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrow_date_time.setRotation(360);
                arrow_reason.setRotation(360);
                arrow_amount.setRotation(360);

                if (i ==1) {
                    arrow_user.setRotation(180);
                    final String selectQuery = "SELECT * FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ORDER BY user DESC";

                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"date1", "time", "user", "reason", "total", "_id"};
                    final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.reason, R.id.total, R.id.id};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(KOT_Cancel_list.this, R.layout.kot_cancel_layout, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);
                    i = 2;

                }else {
                    arrow_user.setRotation(360);
                    final String selectQuery = "SELECT * FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ORDER BY user ASC";

                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"date1", "time", "user", "reason", "total", "_id"};
                    final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.reason, R.id.total, R.id.id};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(KOT_Cancel_list.this, R.layout.kot_cancel_layout, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);
                    i = 1;

                }
            }
        });

        reason_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrow_date_time.setRotation(360);
                arrow_user.setRotation(360);
                arrow_amount.setRotation(360);

                if (i1 ==1) {
                    arrow_reason.setRotation(180);
                    final String selectQuery = "SELECT * FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ORDER BY reason DESC";

                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"date1", "time", "user", "reason", "total", "_id"};
                    final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.reason, R.id.total, R.id.id};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(KOT_Cancel_list.this, R.layout.kot_cancel_layout, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);
                    i1 = 2;

                }else {
                    arrow_reason.setRotation(360);
                    final String selectQuery = "SELECT * FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ORDER BY reason ASC";

                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"date1", "time", "user", "reason", "total", "_id"};
                    final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.reason, R.id.total, R.id.id};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(KOT_Cancel_list.this, R.layout.kot_cancel_layout, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);
                    i1 = 1;

                }
            }
        });

        amount_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrow_date_time.setRotation(360);
                arrow_user.setRotation(360);
                arrow_reason.setRotation(360);

                if (i1 ==1) {
                    arrow_amount.setRotation(180);
                    final String selectQuery = "SELECT * FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' " +
                            "AND datetimee_new <='"+editText2_filter.getText().toString()+"' ORDER BY CAST(total AS unsigned) DESC";

                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"date1", "time", "user", "reason", "total", "_id"};
                    final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.reason, R.id.total, R.id.id};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(KOT_Cancel_list.this, R.layout.kot_cancel_layout, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);
                    i1 = 2;

                }else {
                    arrow_amount.setRotation(360);
                    final String selectQuery = "SELECT * FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ORDER BY CAST(total AS unsigned) ASC";

                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"date1", "time", "user", "reason", "total", "_id"};
                    final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.reason, R.id.total, R.id.id};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(KOT_Cancel_list.this, R.layout.kot_cancel_layout, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);
                    i1 = 1;

                }
            }
        });
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
}
