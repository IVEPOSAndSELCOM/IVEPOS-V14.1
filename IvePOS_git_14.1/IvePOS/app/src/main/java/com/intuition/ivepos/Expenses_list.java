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

public class Expenses_list extends AppCompatActivity {

    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;

    ListView listView;

    EditText editText1_filter, editText2_filter;
    TextView editText1, editText2, editText11, editText22;
    TextView editText_from_day_hide, editText_from_day_visible, editText_to_day_hide, editText_to_day_visible;

    LinearLayout date_time_click, user_click, expense_click, amount_click;

    ImageView arrow_date_time, arrow_user, arrow_expense, arrow_amount;
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

    String WebserviceUrl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expenses_listview);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE,null);

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

        TextView editText1_filter = new TextView(Expenses_list.this);
        TextView editText2_filter = new TextView(Expenses_list.this);
        TextView editText1 = new TextView(Expenses_list.this);
        TextView editText11 = new TextView(Expenses_list.this);
        TextView editText2 = new TextView(Expenses_list.this);
        TextView editText22 = new TextView(Expenses_list.this);
        TextView editText_from_day_visible = new TextView(Expenses_list.this);
        TextView editText_from_day_hide = new TextView(Expenses_list.this);
        TextView editText_to_day_visible = new TextView(Expenses_list.this);
        TextView editText_to_day_hide = new TextView(Expenses_list.this);

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


        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(Expenses_list.this);
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

        donotshowKeyboard(Expenses_list.this);

        LinearLayout back_activity = (LinearLayout) findViewById(R.id.back_activity);
        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        final String selectQuery = "SELECT * FROM Expenses_sales WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"'";

        Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
        // The desired columns to be bound
        final String[] fromFieldNames = {"date", "time", "counterperson_name", "expensename", "price", "_id"};
        final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.expensename, R.id.price, R.id.id};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(Expenses_list.this, R.layout.expense_layout, cursor1, fromFieldNames, toViewsID, 0);
        listView.setAdapter(adapter);

        EditText editText_expense_name = (EditText) findViewById(R.id.editText_expense_name);
        EditText editText_expense_price = (EditText) findViewById(R.id.editText_expense_price);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Cursor cursor2 = (Cursor) parent.getItemAtPosition(position);
                String id1 = cursor2.getString(cursor2.getColumnIndex("_id"));

//                Toast.makeText(Expenses_list.this, "id is "+id1, Toast.LENGTH_LONG).show();

                dialog = new Dialog(Expenses_list.this, R.style.timepicker_date_dialog);
                dialog.setContentView(R.layout.fragment_update_expense);
//                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog.show();

                ImageButton btncancel = (ImageButton) dialog.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                TextInputLayout dialog_layout_expensename = (TextInputLayout) dialog.findViewById(R.id.dialog_layout_expensename);
                TextInputLayout dialog_layout_expenseprice = (TextInputLayout) dialog.findViewById(R.id.dialog_layout_expenseprice);

                EditText dialog_editText_expense_name = (EditText) dialog.findViewById(R.id.dialog_editText_expense_name);
                EditText dialog_editText_expense_price = (EditText) dialog.findViewById(R.id.dialog_editText_expense_price);
                TextView chocolate_spinner = (TextView) dialog.findViewById(R.id.chocolate_spinner);
                TextView user_spinner = (TextView) dialog.findViewById(R.id.user_spinner);

                Cursor cursor = db1.rawQuery("SELECT * FROM Expenses_sales WHERE _id = '"+id1+"'", null);
                if (cursor.moveToFirst()) {
                    String str_dialog_expense_name = cursor.getString(1);
                    String str_dialog_expense_price = cursor.getString(2);
                    String str_chocolate_spinner = cursor.getString(3);
                    String str_user_spinner = cursor.getString(4);

                    dialog_date = cursor.getString(6);
                    dialog_datetimee_new = cursor.getString(8);

                    dialog_editText_expense_name.setText(str_dialog_expense_name);
                    dialog_editText_expense_price.setText(str_dialog_expense_price);

                    chocolate_spinner.setText(str_chocolate_spinner);
                    user_spinner.setText(str_user_spinner);
                }
                cursor.close();

                chocolate_spinner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideKeyboard(Expenses_list.this);

                        final Dialog dialog1 = new Dialog(Expenses_list.this, R.style.notitle);
                        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialog1.setContentView(R.layout.spinnerlist);

                        popupSpinner = (ListView) dialog1.findViewById(R.id.listView5);
                        ArrayList<String> my_arrayy = getTableValuesall();
                        final ArrayAdapter my_Adapterr = new ArrayAdapter(Expenses_list.this, R.layout.spinner_row,
                                my_arrayy);
                        popupSpinner.setAdapter(my_Adapterr);

                        myFilter1 = (EditText) dialog1.findViewById(R.id.searchView);
                        myFilter1.addTextChangedListener(new TextWatcher() {

                            public void afterTextChanged(Editable s) {
                            }

                            public void beforeTextChanged(CharSequence s, int start,
                                                          int count, int after) {
                            }

                            public void onTextChanged(CharSequence s, int start,
                                                      int before, int count) {
                                my_Adapterr.getFilter().filter(s.toString());
                            }
                        });

                        LinearLayout cancelletter = (LinearLayout) dialog1.findViewById(R.id.custombar_del_wrapper);
                        cancelletter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                if (one.hasFocus() || two.hasFocus() || three.hasFocus() || text.hasFocus() || editText.hasFocus() || textquan.hasFocus() ||
//                                        one.hasFocus() || two.hasFocus() || three.hasFocus() || text.hasFocus() || editText.hasFocus() || textquan.hasFocus()){
//                                    hideKeyboard(getContext());
//
//                                }

                                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                myFilter1.setText("");
                                hideKeyboard(Expenses_list.this);
                                donotshowKeyboard(Expenses_list.this);
                            }
                        });

                        LinearLayout cancelletter1 = (LinearLayout) dialog1.findViewById(R.id.custombar_return_wrapper);
                        cancelletter1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                dialog1.dismiss();
                                hideKeyboard(Expenses_list.this);
                                donotshowKeyboard(Expenses_list.this);
                            }
                        });


                        //selectionCurrent = String.valueOf(popupSpinner.getSelectedItemPosition());

                        popupSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String selectedSweet = popupSpinner.getItemAtPosition(position).toString();

                                chocolate_spinner.setText(selectedSweet);
                                hideKeyboard(Expenses_list.this);
                                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                dialog1.dismiss();
                                donotshowKeyboard(Expenses_list.this);
                                //String text = dialogC4_id.getText().toString();
                                //Toast.makeText(Expenses_list.this, "Selected item: " + selectedSweet + " - " + position, Toast.LENGTH_SHORT).show();
                            }
                        });

                        dialog1.show();
                    }
                });

                user_spinner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideKeyboard(Expenses_list.this);

                        final Dialog dialog1 = new Dialog(Expenses_list.this, R.style.notitle);
                        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialog1.setContentView(R.layout.spinnerlist);

                        popupSpinner = (ListView) dialog1.findViewById(R.id.listView5);
                        ArrayList<String> my_arrayy = getUserValuesall();
                        final ArrayAdapter my_Adapterr = new ArrayAdapter(Expenses_list.this, R.layout.spinner_row,
                                my_arrayy);
                        popupSpinner.setAdapter(my_Adapterr);

                        myFilter1 = (EditText) dialog1.findViewById(R.id.searchView);
                        myFilter1.addTextChangedListener(new TextWatcher() {

                            public void afterTextChanged(Editable s) {
                            }

                            public void beforeTextChanged(CharSequence s, int start,
                                                          int count, int after) {
                            }

                            public void onTextChanged(CharSequence s, int start,
                                                      int before, int count) {
                                my_Adapterr.getFilter().filter(s.toString());
                            }
                        });

                        LinearLayout cancelletter = (LinearLayout) dialog1.findViewById(R.id.custombar_del_wrapper);
                        cancelletter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                if (one.hasFocus() || two.hasFocus() || three.hasFocus() || text.hasFocus() || editText.hasFocus() || textquan.hasFocus() ||
//                                        one.hasFocus() || two.hasFocus() || three.hasFocus() || text.hasFocus() || editText.hasFocus() || textquan.hasFocus()){
//                                    hideKeyboard(getContext());
//
//                                }

                                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                myFilter1.setText("");
                                hideKeyboard(Expenses_list.this);
                                donotshowKeyboard(Expenses_list.this);
                            }
                        });

                        LinearLayout cancelletter1 = (LinearLayout) dialog1.findViewById(R.id.custombar_return_wrapper);
                        cancelletter1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                dialog1.dismiss();
                                hideKeyboard(Expenses_list.this);
                                donotshowKeyboard(Expenses_list.this);
                            }
                        });


                        //selectionCurrent = String.valueOf(popupSpinner.getSelectedItemPosition());

                        popupSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String selectedSweet = popupSpinner.getItemAtPosition(position).toString();

                                user_spinner.setText(selectedSweet);
                                hideKeyboard(Expenses_list.this);
                                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                dialog1.dismiss();
                                donotshowKeyboard(Expenses_list.this);
                                //String text = dialogC4_id.getText().toString();
                                //Toast.makeText(Expenses_list.this, "Selected item: " + selectedSweet + " - " + position, Toast.LENGTH_SHORT).show();
                            }
                        });

                        dialog1.show();
                    }
                });


                Button btndelete = (Button) dialog.findViewById(R.id.btndelete);
                btndelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dd = new Dialog(Expenses_list.this, R.style.notitle);
                        dd.setContentView(R.layout.delete_expense_selected);


                        ImageView imageVieww = (ImageView) dd.findViewById(R.id.closetext);
                        imageVieww.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dd.dismiss();
                            }
                        });

                        Button buttonn = (Button) dd.findViewById(R.id.cancel);
                        buttonn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dd.dismiss();
                            }
                        });

                        Button buttonnn = (Button) dd.findViewById(R.id.ok);
                        buttonnn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String where = "_id = '" + id1 + "' ";

                                contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Expenses_sales");
                                getContentResolver().delete(contentUri, where, new String[]{});
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProvider.AUTHORITY)
                                        .path("Expenses_sales")
                                        .appendQueryParameter("operation", "delete")
                                        .appendQueryParameter("_id", id1)
                                        .build();
                                getContentResolver().notifyChange(resultUri, null);
//                                                db.delete("Items", where, new String[]{});


                                dd.dismiss();
                                dialog.dismiss();

//                                                country.moveToPosition(position);
//                                                //String rowId = cursor.getString(0); //Column 0 of the cursor is the id
//                                                country.requery();
//                                                dataAdapter.notifyDataSetChanged();

                                listView.setAdapter(null);
                                final String selectQuery = "SELECT * FROM Expenses_sales WHERE datetimee_new >= '" + player1name + "' AND datetimee_new <='" + player2name + "'";

                                Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                                // The desired columns to be bound
                                final String[] fromFieldNames = {"date", "time", "counterperson_name", "expensename", "price", "_id"};
                                final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.expensename, R.id.price, R.id.id};
                                SimpleCursorAdapter adapter = new SimpleCursorAdapter(Expenses_list.this, R.layout.expense_layout, cursor1, fromFieldNames, toViewsID, 0);
                                listView.setAdapter(adapter);

                            }
                        });

                        dd.show();

                    }
                });

                initCustom2(dialog, dialog_date, dialog_datetimee_new);

                Button btnsave = (Button) dialog.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Date dt = new Date();
                        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
                        final String time1 = sdf1.format(dt);

                        SimpleDateFormat normal = new SimpleDateFormat("dd MMM yyyy");
                        final String normal1 = normal.format(new Date());

                        Date dtt_new = new Date();
                        SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");
                        String time24_new = sdf1t_new.format(dtt_new);

                        str_editText11_dialog= editText11_dialog.getText().toString();
                        str_editText_from_day_visible_dialog= editText_from_day_visible_dialog.getText().toString();


                        System.out.println(str_editText11_dialog);
                        System.out.println(str_editText_from_day_visible_dialog);

                        String[] date_start=str_editText11_dialog.split(" ");
                        String date_dialog1=date_start[2];
                        if(date_start[1].equalsIgnoreCase("Jan")){
                            date_dialog1=date_dialog1+"01";
                        }else if(date_start[1].equalsIgnoreCase("Feb")){
                            date_dialog1=date_dialog1+"02";
                        }else if(date_start[1].equalsIgnoreCase("Mar")){
                            date_dialog1=date_dialog1+"03";
                        }else if(date_start[1].equalsIgnoreCase("Apr")){
                            date_dialog1=date_dialog1+"04";
                        }else if(date_start[1].equalsIgnoreCase("May")){
                            date_dialog1=date_dialog1+"05";
                        }else if(date_start[1].equalsIgnoreCase("Jun")){
                            date_dialog1=date_dialog1+"06";
                        }else if(date_start[1].equalsIgnoreCase("Jul")){
                            date_dialog1=date_dialog1+"07";
                        }else if(date_start[1].equalsIgnoreCase("Aug")){
                            date_dialog1=date_dialog1+"08";
                        }else if(date_start[1].equalsIgnoreCase("Sep")){
                            date_dialog1=date_dialog1+"09";
                        }else if(date_start[1].equalsIgnoreCase("Oct")){
                            date_dialog1=date_dialog1+"10";
                        }else if(date_start[1].equalsIgnoreCase("Nov")){
                            date_dialog1=date_dialog1+"11";
                        }else if(date_start[1].equalsIgnoreCase("Dec")){
                            date_dialog1=date_dialog1+"12";
                        }
                        date_dialog1=date_dialog1+date_start[0];
                        String time_dialog1="";

                        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
                        Date date = null;
                        try {
                            date = parseFormat.parse(str_editText_from_day_visible_dialog);
                            time_dialog1=displayFormat.format(date);
                            time_dialog1=time_dialog1.replace(":","");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        date_dialog1=date_dialog1+time_dialog1;


                        TextView editText1_filter = new TextView(Expenses_list.this);

                        editText1_filter.setText(date_dialog1);


                        date1 = editText1_filter.getText().toString();

                        if (date1.length() == 11) {
//                    Toast.makeText(Expenses_list.this, "11 "+date1, Toast.LENGTH_LONG).show();
//                    Toast.makeText(Expenses_list.this, "11 "+date1, Toast.LENGTH_LONG).show();
                            String onetoeight = date1.substring(0, 13);
                            date1 = onetoeight + "1";
                        }else {
//                    Toast.makeText(Expenses_list.this, "12 "+date1, Toast.LENGTH_LONG).show();
//                    Toast.makeText(Expenses_list.this, "12 "+date1, Toast.LENGTH_LONG).show();
                        }

                        System.out.println(date1);

                        if (dialog_editText_expense_name.getText().toString().equals("") || dialog_editText_expense_price.getText().toString().equals("")) {
                            if (dialog_editText_expense_name.getText().toString().equals("")) {
                                dialog_layout_expensename.setError("Fill expense name");
                            }
                            if (dialog_editText_expense_price.getText().toString().equals("")) {
                                dialog_layout_expenseprice.setError("Fill expense price");
                            }
                        }else {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("expensename", dialog_editText_expense_name.getText().toString());
                            contentValues.put("price", dialog_editText_expense_price.getText().toString());
                            contentValues.put("category", chocolate_spinner.getText().toString());
                            contentValues.put("counterperson_username", user_spinner.getText().toString());

                            Cursor ladmin = db.rawQuery("SELECT * FROM LAdmin WHERE name = '" + user_spinner.getText().toString() + "'", null);
                            if (ladmin.moveToFirst()) {
                                u_name = ladmin.getString(3);
                            } else {
                                Cursor madmin = db.rawQuery("SELECT * FROM LOGIN WHERE name = '" + user_spinner.getText().toString() + "'", null);
                                if (madmin.moveToFirst()) {
                                    u_name = madmin.getString(3);
                                } else {
                                    Cursor user1 = db.rawQuery("SELECT * FROM User1 WHERE name = '" + user_spinner.getText().toString() + "'", null);
                                    if (user1.moveToFirst()) {
                                        u_name = user1.getString(2);
                                    } else {
                                        Cursor user2 = db.rawQuery("SELECT * FROM User2 WHERE name = '" + user_spinner.getText().toString() + "'", null);
                                        if (user2.moveToFirst()) {
                                            u_name = user2.getString(2);
                                        } else {
                                            Cursor user3 = db.rawQuery("SELECT * FROM User3 WHERE name = '" + user_spinner.getText().toString() + "'", null);
                                            if (user3.moveToFirst()) {
                                                u_name = user3.getString(2);
                                            } else {
                                                Cursor user4 = db.rawQuery("SELECT * FROM User4 WHERE name = '" + user_spinner.getText().toString() + "'", null);
                                                if (user4.moveToFirst()) {
                                                    u_name = user4.getString(2);
                                                } else {
                                                    Cursor user5 = db.rawQuery("SELECT * FROM User5 WHERE name = '" + user_spinner.getText().toString() + "'", null);
                                                    if (user5.moveToFirst()) {
                                                        u_name = user5.getString(2);
                                                    } else {
                                                        Cursor user6 = db.rawQuery("SELECT * FROM User6 WHERE name = '" + user_spinner.getText().toString() + "'", null);
                                                        if (user6.moveToFirst()) {
                                                            u_name = user6.getString(2);
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

                            contentValues.put("counterperson_name", u_name);
                            contentValues.put("date", str_editText11_dialog);
                            contentValues.put("time", str_editText_from_day_visible_dialog);
                            contentValues.put("datetimee_new", date1);

                            webservicequery_sales("UPDATE Expenses_sales set expensename = '"+dialog_editText_expense_name.getText().toString()+"', " +
                                    "price = '"+dialog_editText_expense_price.getText().toString()+"', category = '"+chocolate_spinner.getText().toString()+"', " +
                                    "counterperson_username = '"+user_spinner.getText().toString()+"', counterperson_name = '"+u_name+"', date = '"+str_editText11_dialog+"', " +
                                    "time = '"+str_editText_from_day_visible_dialog+"', datetimee_new = '"+date1+"' WHERE _id = '"+id+"'");

                            db1.execSQL("UPDATE Expenses_sales set expensename = '"+dialog_editText_expense_name.getText().toString()+"', " +
                                    "price = '"+dialog_editText_expense_price.getText().toString()+"', category = '"+chocolate_spinner.getText().toString()+"', " +
                                    "counterperson_username = '"+user_spinner.getText().toString()+"', counterperson_name = '"+u_name+"', date = '"+str_editText11_dialog+"', " +
                                    "time = '"+str_editText_from_day_visible_dialog+"', datetimee_new = '"+date1+"' WHERE _id = '"+id+"'");
                            listView.setAdapter(null);
                            final String selectQuery = "SELECT * FROM Expenses_sales WHERE datetimee_new >= '"+player1name+"' AND datetimee_new <='"+player2name+"'";

                            Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                            // The desired columns to be bound
                            final String[] fromFieldNames = {"date", "time", "counterperson_name", "expensename", "price", "_id"};
                            final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.expensename, R.id.price, R.id.id};
                            SimpleCursorAdapter adapter = new SimpleCursorAdapter(Expenses_list.this, R.layout.expense_layout, cursor1, fromFieldNames, toViewsID, 0);
                            listView.setAdapter(adapter);

//                            String where = "_id = ?";
//
//                            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Expenses_sales");
//                            getContentResolver().update(contentUri, contentValues,where,new String[]{id1});
//                            resultUri = new Uri.Builder()
//                                    .scheme("content")
//                                    .authority(StubProvider.AUTHORITY)
//                                    .path("Expenses_sales")
//                                    .appendQueryParameter("operation", "update")
//                                    .appendQueryParameter("_id", id1)
//                                    .build();
//                            getContentResolver().notifyChange(resultUri, null);

//                            String where  = "_id = '" +id1+ "' ";
//
//                            contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Expenses_sales");
//                            getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                            resultUri = new Uri.Builder()
//                                    .scheme("content")
//                                    .authority(StubProvider.AUTHORITY)
//                                    .path("Expenses_sales")
//                                    .appendQueryParameter("operation", "update")
//                                    .appendQueryParameter("_id",id1)
//                                    .build();
//                            getContentResolver().notifyChange(resultUri, null);

//                            Toast.makeText(Expenses_list.this.getBaseContext(),
//                                    getString(R.string.expense_added), Toast.LENGTH_SHORT).show();
                            additem.setVisibility(View.VISIBLE);
                            editText_expense_name.setText("");
                            editText_expense_price.setText("");
                            hideKeyboard(Expenses_list.this);
                            donotshowKeyboard(Expenses_list.this);

                            dialog.dismiss();

                        }

                    }
                });
            }
        });

        arrow_date_time = (ImageView) findViewById(R.id.arrow_date_time);
        arrow_user = (ImageView) findViewById(R.id.arrow_user);
        arrow_expense = (ImageView) findViewById(R.id.arrow_expense);
        arrow_amount = (ImageView) findViewById(R.id.arrow_amount);

        date_time_click = (LinearLayout) findViewById(R.id.date_time_click);
        user_click = (LinearLayout) findViewById(R.id.user_click);
        expense_click = (LinearLayout) findViewById(R.id.expense_click);
        amount_click = (LinearLayout) findViewById(R.id.amount_click);

        date_time_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrow_user.setRotation(360);
                arrow_expense.setRotation(360);
                arrow_amount.setRotation(360);

                if (i ==1) {
                    arrow_date_time.setRotation(180);
                    final String selectQuery = "SELECT * FROM Expenses_sales WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ORDER BY datetimee_new DESC";

                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"date", "time", "counterperson_name", "expensename", "price", "_id"};
                    final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.expensename, R.id.price, R.id.id};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(Expenses_list.this, R.layout.expense_layout, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);
                    i = 2;

                }else {
                    arrow_date_time.setRotation(360);
                    final String selectQuery = "SELECT * FROM Expenses_sales WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ORDER BY datetimee_new ASC";

                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"date", "time", "counterperson_name", "expensename", "price", "_id"};
                    final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.expensename, R.id.price, R.id.id};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(Expenses_list.this, R.layout.expense_layout, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);
                    i = 1;

                }
            }
        });

        user_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrow_date_time.setRotation(360);
                arrow_expense.setRotation(360);
                arrow_amount.setRotation(360);

                if (i ==1) {
                    arrow_user.setRotation(180);
                    final String selectQuery = "SELECT * FROM Expenses_sales WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ORDER BY counterperson_name DESC";

                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"date", "time", "counterperson_name", "expensename", "price", "_id"};
                    final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.expensename, R.id.price, R.id.id};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(Expenses_list.this, R.layout.expense_layout, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);
                    i = 2;

                }else {
                    arrow_user.setRotation(360);
                    final String selectQuery = "SELECT * FROM Expenses_sales WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ORDER BY counterperson_name ASC";

                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"date", "time", "counterperson_name", "expensename", "price", "_id"};
                    final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.expensename, R.id.price, R.id.id};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(Expenses_list.this, R.layout.expense_layout, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);
                    i = 1;

                }
            }
        });

        expense_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrow_date_time.setRotation(360);
                arrow_user.setRotation(360);
                arrow_amount.setRotation(360);

                if (i1 ==1) {
                    arrow_expense.setRotation(180);
                    final String selectQuery = "SELECT * FROM Expenses_sales WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ORDER BY expensename DESC";

                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"date", "time", "counterperson_name", "expensename", "price", "_id"};
                    final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.expensename, R.id.price, R.id.id};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(Expenses_list.this, R.layout.expense_layout, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);
                    i1 = 2;

                }else {
                    arrow_expense.setRotation(360);
                    final String selectQuery = "SELECT * FROM Expenses_sales WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ORDER BY expensename ASC";

                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"date", "time", "counterperson_name", "expensename", "price", "_id"};
                    final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.expensename, R.id.price, R.id.id};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(Expenses_list.this, R.layout.expense_layout, cursor1, fromFieldNames, toViewsID, 0);
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
                arrow_expense.setRotation(360);

                if (i1 ==1) {
                    arrow_amount.setRotation(180);
                    final String selectQuery = "SELECT * FROM Expenses_sales WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' " +
                            "AND datetimee_new <='"+editText2_filter.getText().toString()+"' ORDER BY CAST(price AS unsigned) DESC";

                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"date", "time", "counterperson_name", "expensename", "price", "_id"};
                    final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.expensename, R.id.price, R.id.id};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(Expenses_list.this, R.layout.expense_layout, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);
                    i1 = 2;

                }else {
                    arrow_amount.setRotation(360);
                    final String selectQuery = "SELECT * FROM Expenses_sales WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ORDER BY CAST(price AS unsigned) ASC";

                    Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"date", "time", "counterperson_name", "expensename", "price", "_id"};
                    final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.expensename, R.id.price, R.id.id};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(Expenses_list.this, R.layout.expense_layout, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);
                    i1 = 1;

                }
            }
        });

        additem = (ExtendedFloatingActionButton) findViewById(R.id.add_button);
        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout = (RelativeLayout) findViewById(R.id.add_item);
                linearLayout.setVisibility(View.VISIBLE);
                additem.setVisibility(View.GONE);

                initCustom();

                editText_expense_name.requestFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText_expense_name, InputMethodManager.SHOW_IMPLICIT);

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

                spinnercat = (TextView) findViewById(R.id.chocolate_spinner);
                spinnercat.setText("Rent");
                spinnercat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideKeyboard(Expenses_list.this);

                        final Dialog dialog1 = new Dialog(Expenses_list.this, R.style.notitle);
                        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialog1.setContentView(R.layout.spinnerlist);

                        popupSpinner = (ListView) dialog1.findViewById(R.id.listView5);
                        ArrayList<String> my_arrayy = getTableValuesall();
                        final ArrayAdapter my_Adapterr = new ArrayAdapter(Expenses_list.this, R.layout.spinner_row,
                                my_arrayy);
                        popupSpinner.setAdapter(my_Adapterr);

                        myFilter1 = (EditText) dialog1.findViewById(R.id.searchView);
                        myFilter1.addTextChangedListener(new TextWatcher() {

                            public void afterTextChanged(Editable s) {
                            }

                            public void beforeTextChanged(CharSequence s, int start,
                                                          int count, int after) {
                            }

                            public void onTextChanged(CharSequence s, int start,
                                                      int before, int count) {
                                my_Adapterr.getFilter().filter(s.toString());
                            }
                        });

                        LinearLayout cancelletter = (LinearLayout) dialog1.findViewById(R.id.custombar_del_wrapper);
                        cancelletter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                if (one.hasFocus() || two.hasFocus() || three.hasFocus() || text.hasFocus() || editText.hasFocus() || textquan.hasFocus() ||
//                                        one.hasFocus() || two.hasFocus() || three.hasFocus() || text.hasFocus() || editText.hasFocus() || textquan.hasFocus()){
//                                    hideKeyboard(getContext());
//
//                                }
                                editText_expense_name.clearFocus();
                                editText_expense_price.clearFocus();
                                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                myFilter1.setText("");
                                hideKeyboard(Expenses_list.this);
                                donotshowKeyboard(Expenses_list.this);
                            }
                        });

                        LinearLayout cancelletter1 = (LinearLayout) dialog1.findViewById(R.id.custombar_return_wrapper);
                        cancelletter1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editText_expense_name.clearFocus();
                                editText_expense_price.clearFocus();
                                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                dialog1.dismiss();
                                hideKeyboard(Expenses_list.this);
                                donotshowKeyboard(Expenses_list.this);
                            }
                        });


                        //selectionCurrent = String.valueOf(popupSpinner.getSelectedItemPosition());

                        popupSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String selectedSweet = popupSpinner.getItemAtPosition(position).toString();

                                editText_expense_name.clearFocus();
                                editText_expense_price.clearFocus();
                                spinnercat.setText(selectedSweet);
                                hideKeyboard(Expenses_list.this);
                                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                dialog1.dismiss();
                                donotshowKeyboard(Expenses_list.this);
                                //String text = dialogC4_id.getText().toString();
                                //Toast.makeText(Expenses_list.this, "Selected item: " + selectedSweet + " - " + position, Toast.LENGTH_SHORT).show();
                            }
                        });

                        dialog1.show();

                    }
                });

                spinneruser = (TextView) findViewById(R.id.user_spinner);

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

                spinneruser.setText(u_name);
                spinneruser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideKeyboard(Expenses_list.this);

                        final Dialog dialog1 = new Dialog(Expenses_list.this, R.style.notitle);
                        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialog1.setContentView(R.layout.spinnerlist);

                        popupSpinner = (ListView) dialog1.findViewById(R.id.listView5);
                        ArrayList<String> my_arrayy = getUserValuesall();
                        final ArrayAdapter my_Adapterr = new ArrayAdapter(Expenses_list.this, R.layout.spinner_row,
                                my_arrayy);
                        popupSpinner.setAdapter(my_Adapterr);

                        myFilter1 = (EditText) dialog1.findViewById(R.id.searchView);
                        myFilter1.addTextChangedListener(new TextWatcher() {

                            public void afterTextChanged(Editable s) {
                            }

                            public void beforeTextChanged(CharSequence s, int start,
                                                          int count, int after) {
                            }

                            public void onTextChanged(CharSequence s, int start,
                                                      int before, int count) {
                                my_Adapterr.getFilter().filter(s.toString());
                            }
                        });

                        LinearLayout cancelletter = (LinearLayout) dialog1.findViewById(R.id.custombar_del_wrapper);
                        cancelletter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                if (one.hasFocus() || two.hasFocus() || three.hasFocus() || text.hasFocus() || editText.hasFocus() || textquan.hasFocus() ||
//                                        one.hasFocus() || two.hasFocus() || three.hasFocus() || text.hasFocus() || editText.hasFocus() || textquan.hasFocus()){
//                                    hideKeyboard(getContext());
//
//                                }
                                editText_expense_name.clearFocus();
                                editText_expense_price.clearFocus();
                                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                myFilter1.setText("");
                                hideKeyboard(Expenses_list.this);
                                donotshowKeyboard(Expenses_list.this);
                            }
                        });

                        LinearLayout cancelletter1 = (LinearLayout) dialog1.findViewById(R.id.custombar_return_wrapper);
                        cancelletter1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editText_expense_name.clearFocus();
                                editText_expense_price.clearFocus();
                                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                dialog1.dismiss();
                                hideKeyboard(Expenses_list.this);
                                donotshowKeyboard(Expenses_list.this);
                            }
                        });


                        //selectionCurrent = String.valueOf(popupSpinner.getSelectedItemPosition());

                        popupSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String selectedSweet = popupSpinner.getItemAtPosition(position).toString();

                                editText_expense_name.clearFocus();
                                editText_expense_price.clearFocus();
                                spinneruser.setText(selectedSweet);
                                hideKeyboard(Expenses_list.this);
                                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                dialog1.dismiss();
                                donotshowKeyboard(Expenses_list.this);
                                //String text = dialogC4_id.getText().toString();
                                //Toast.makeText(getActivity(), "Selected item: " + selectedSweet + " - " + position, Toast.LENGTH_SHORT).show();
                            }
                        });

                        dialog1.show();

                    }
                });

                Button btn = (Button) findViewById(R.id.btndelete);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearLayout.setVisibility(View.GONE);
                        additem.setVisibility(View.VISIBLE);
                        editText_expense_name.setText("");
                        editText_expense_price.setText("");
                        hideKeyboard(Expenses_list.this);
                    }
                });

                TextInputLayout layout_expensename = (TextInputLayout) findViewById(R.id.layout_expensename);
                TextInputLayout layout_expenseprice = (TextInputLayout) findViewById(R.id.layout_expenseprice);

                Button btnsave = (Button) findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (editText_expense_price.getText().toString().equals(".") || editText_expense_price.getText().toString().equals(".0")
                                || editText_expense_price.getText().toString().equals(".00")) {
                            layout_expenseprice.setError("Enter valid price");
                        }else {
                            Date dt = new Date();
                            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
                            final String time1 = sdf1.format(dt);

                            SimpleDateFormat normal = new SimpleDateFormat("dd MMM yyyy");
                            final String normal1 = normal.format(new Date());

                            Date dtt_new = new Date();
                            SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");
                            String time24_new = sdf1t_new.format(dtt_new);

                            str_editText11_dialog = editText11_dialog.getText().toString();
                            str_editText_from_day_visible_dialog = editText_from_day_visible_dialog.getText().toString();


                            System.out.println(str_editText11_dialog);
                            System.out.println(str_editText_from_day_visible_dialog);

                            String[] date_start = str_editText11_dialog.split(" ");
                            String date_dialog1 = date_start[2];
                            if (date_start[1].equalsIgnoreCase("Jan")) {
                                date_dialog1 = date_dialog1 + "01";
                            } else if (date_start[1].equalsIgnoreCase("Feb")) {
                                date_dialog1 = date_dialog1 + "02";
                            } else if (date_start[1].equalsIgnoreCase("Mar")) {
                                date_dialog1 = date_dialog1 + "03";
                            } else if (date_start[1].equalsIgnoreCase("Apr")) {
                                date_dialog1 = date_dialog1 + "04";
                            } else if (date_start[1].equalsIgnoreCase("May")) {
                                date_dialog1 = date_dialog1 + "05";
                            } else if (date_start[1].equalsIgnoreCase("Jun")) {
                                date_dialog1 = date_dialog1 + "06";
                            } else if (date_start[1].equalsIgnoreCase("Jul")) {
                                date_dialog1 = date_dialog1 + "07";
                            } else if (date_start[1].equalsIgnoreCase("Aug")) {
                                date_dialog1 = date_dialog1 + "08";
                            } else if (date_start[1].equalsIgnoreCase("Sep")) {
                                date_dialog1 = date_dialog1 + "09";
                            } else if (date_start[1].equalsIgnoreCase("Oct")) {
                                date_dialog1 = date_dialog1 + "10";
                            } else if (date_start[1].equalsIgnoreCase("Nov")) {
                                date_dialog1 = date_dialog1 + "11";
                            } else if (date_start[1].equalsIgnoreCase("Dec")) {
                                date_dialog1 = date_dialog1 + "12";
                            }
                            date_dialog1 = date_dialog1 + date_start[0];
                            String time_dialog1 = "";

                            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
                            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
                            Date date = null;
                            try {
                                date = parseFormat.parse(str_editText_from_day_visible_dialog);
                                time_dialog1 = displayFormat.format(date);
                                time_dialog1 = time_dialog1.replace(":", "");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            date_dialog1 = date_dialog1 + time_dialog1;


                            TextView editText1_filter = new TextView(Expenses_list.this);

                            editText1_filter.setText(date_dialog1);


                            date1 = editText1_filter.getText().toString();

                            if (date1.length() == 11) {
//                    Toast.makeText(Expenses_list.this, "11 "+date1, Toast.LENGTH_LONG).show();
//                    Toast.makeText(Expenses_list.this, "11 "+date1, Toast.LENGTH_LONG).show();
                                String onetoeight = date1.substring(0, 13);
                                date1 = onetoeight + "1";
                            } else {
//                    Toast.makeText(Expenses_list.this, "12 "+date1, Toast.LENGTH_LONG).show();
//                    Toast.makeText(Expenses_list.this, "12 "+date1, Toast.LENGTH_LONG).show();
                            }

                            if (editText_expense_name.getText().toString().equals("") || editText_expense_price.getText().toString().equals("")) {
                                if (editText_expense_name.getText().toString().equals("")) {
                                    layout_expensename.setError("Fill expense name");
                                }
                                if (editText_expense_price.getText().toString().equals("")) {
                                    layout_expenseprice.setError("Fill expense price");
                                }
                            } else {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("expensename", editText_expense_name.getText().toString());
                                contentValues.put("price", editText_expense_price.getText().toString());
                                contentValues.put("category", spinnercat.getText().toString());
                                contentValues.put("counterperson_username", spinneruser.getText().toString());

                                Cursor ladmin = db.rawQuery("SELECT * FROM LAdmin WHERE name = '" + spinneruser.getText().toString() + "'", null);
                                if (ladmin.moveToFirst()) {
                                    u_name = ladmin.getString(3);
                                } else {
                                    Cursor madmin = db.rawQuery("SELECT * FROM LOGIN WHERE name = '" + spinneruser.getText().toString() + "'", null);
                                    if (madmin.moveToFirst()) {
                                        u_name = madmin.getString(3);
                                    } else {
                                        Cursor user1 = db.rawQuery("SELECT * FROM User1 WHERE name = '" + spinneruser.getText().toString() + "'", null);
                                        if (user1.moveToFirst()) {
                                            u_name = user1.getString(2);
                                        } else {
                                            Cursor user2 = db.rawQuery("SELECT * FROM User2 WHERE name = '" + spinneruser.getText().toString() + "'", null);
                                            if (user2.moveToFirst()) {
                                                u_name = user2.getString(2);
                                            } else {
                                                Cursor user3 = db.rawQuery("SELECT * FROM User3 WHERE name = '" + spinneruser.getText().toString() + "'", null);
                                                if (user3.moveToFirst()) {
                                                    u_name = user3.getString(2);
                                                } else {
                                                    Cursor user4 = db.rawQuery("SELECT * FROM User4 WHERE name = '" + spinneruser.getText().toString() + "'", null);
                                                    if (user4.moveToFirst()) {
                                                        u_name = user4.getString(2);
                                                    } else {
                                                        Cursor user5 = db.rawQuery("SELECT * FROM User5 WHERE name = '" + spinneruser.getText().toString() + "'", null);
                                                        if (user5.moveToFirst()) {
                                                            u_name = user5.getString(2);
                                                        } else {
                                                            Cursor user6 = db.rawQuery("SELECT * FROM User6 WHERE name = '" + spinneruser.getText().toString() + "'", null);
                                                            if (user6.moveToFirst()) {
                                                                u_name = user6.getString(2);
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

                                contentValues.put("counterperson_name", u_name);
                                contentValues.put("date", str_editText11_dialog);
                                contentValues.put("time", str_editText_from_day_visible_dialog);
                                contentValues.put("datetimee_new", date1);

                                contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Expenses_sales");
                                resultUri = getContentResolver().insert(contentUri, contentValues);
                                getContentResolver().notifyChange(resultUri, null);

                                Toast.makeText(Expenses_list.this,
                                        getString(R.string.expense_added), Toast.LENGTH_SHORT).show();
                                linearLayout.setVisibility(View.GONE);
                                additem.setVisibility(View.VISIBLE);
                                editText_expense_name.setText("");
                                editText_expense_price.setText("");
                                hideKeyboard(Expenses_list.this);
                                donotshowKeyboard(Expenses_list.this);

                                listView.setAdapter(null);
                                final String selectQuery = "SELECT * FROM Expenses_sales WHERE datetimee_new >= '" + player1name + "' AND datetimee_new <='" + player2name + "'";

                                Cursor cursor1 = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                                // The desired columns to be bound
                                final String[] fromFieldNames = {"date", "time", "counterperson_name", "expensename", "price", "_id"};
                                final int[] toViewsID = {R.id.date, R.id.time, R.id.user, R.id.expensename, R.id.price, R.id.id};
                                SimpleCursorAdapter adapter = new SimpleCursorAdapter(Expenses_list.this, R.layout.expense_layout, cursor1, fromFieldNames, toViewsID, 0);
                                listView.setAdapter(adapter);

                            }
                        }
                    }
                });
            }
        });

//        listView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                // the delay of the extension of the FAB is set for 12 items
//                if (scrollY > oldScrollY + 12 && additem.isExtended()) {
//                    additem.shrink();
//                }
//
//                // the delay of the extension of the FAB is set for 12 items
//                if (scrollY < oldScrollY - 12 && !additem.isExtended()) {
//                    additem.extend();
//                }
//
//                // if the nestedScrollView is at the first item of the list then the
//                // extended floating action should be in extended state
//                if (scrollY == 0) {
//                    additem.extend();
//                }
//            }
//        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener(){
            private int lastFirstVisibleItem;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(lastFirstVisibleItem<firstVisibleItem){
//                    Toast.makeText(getApplicationContext(), "Scrolling down the listView",
//                            Toast.LENGTH_SHORT).show();
                    additem.shrink();
                }
                if(lastFirstVisibleItem>firstVisibleItem){
//                    Toast.makeText(getApplicationContext(), "Scrolling up the listView",
//                            Toast.LENGTH_SHORT).show();
                    additem.extend();
                }
                lastFirstVisibleItem=firstVisibleItem;
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
            Toast.makeText(Expenses_list.this, "Error encountered.",
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
            Toast.makeText(Expenses_list.this, "Error encountered.",
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

    private void initCustom() {

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        final String currentDateandTime1 = sdf2.format(new Date());

        SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM yyyy");
        final String currentDateandTime2 = sdf3.format(new Date());


        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time_hide = sdf.format(new Date());

        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm aa");
        String time_visible = sdf1.format(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        Calendar calendar11 = Calendar.getInstance();
        calendar11.add(Calendar.DAY_OF_YEAR, -1);
        Date yesterday = calendar11.getTime();

        editText1_dialog = (TextView) findViewById(R.id.editText1_dialog);
        editText1_dialog.setText(currentDateandTime1);
    /*    editText1 = (TextView) findViewById(R.id.editText1);
        editText1.setText(currentDateandTime1);*/

        editText11_dialog = (TextView) findViewById(R.id.editText11_dialog);
        editText11_dialog.setText(currentDateandTime2);
/*        editText11 = (TextView) findViewById(R.id.editText11);
        editText11.setText(currentDateandTime2);*/


        editText_from_day_hide_dialog = (TextView) findViewById(R.id.editText_from_day_hide_dialog);
        editText_from_day_visible_dialog = (TextView) findViewById(R.id.editText_from_day_visible_dialog);


/*
        editText_from_day_hide = (TextView) findViewById(R.id.editText_from_day_hide);
        editText_from_day_visible = (TextView) findViewById(R.id.editText_from_day_visible);
        editText_to_day_hide = (TextView) findViewById(R.id.editText_to_day_hide);
        editText_to_day_visible = (TextView) findViewById(R.id.editText_to_day_visible);
*/


        String hourr = time_hide.substring(0, 2);
        String minn = time_hide.substring(3, 5);
        updateTime_open_dialog(Integer.parseInt(hourr), Integer.parseInt(minn));

        editText11_dialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Calendar now = Calendar.getInstance();
//                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
//                        datePickerListener,
//                        now.get(Calendar.YEAR),
//                        now.get(Calendar.MONTH),
//                        now.get(Calendar.DAY_OF_MONTH)
//
//
//                );
//
//                dpd.show(HomeActivity.this.getFragmentManager(), "Datepickerdialog");

                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        datePickerListener,now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMaxDate(Calendar.getInstance());
                dpd.show(getFragmentManager(), "Datepickerdialog");
                //clickcounts++;




            }

            com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener datePickerListener
                    = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog, int selectedYear1, int selectedMonth1, int selectedDay1) {
                    year1 = selectedYear1;
                    month1 = selectedMonth1;
                    day1 = selectedDay1;

                    // set selected date into textview
                    populateSetDate(year1, month1 + 1, day1);
                }
            };





            public void populateSetDate(int year, int month, int day) {
        /*        TextView mEdit = (TextView) findViewById(R.id.editText1);
                TextView mEdit1  = (TextView)findViewById(R.id.editText11);*/
                TextView mEdit_dialog = (TextView) findViewById(R.id.editText1_dialog);
                TextView mEdit1_dialog  = (TextView)findViewById(R.id.editText11_dialog);

                if (month == 1 && day < 10) {
                    //   mEdit.setText(year + " " + "0" + 1 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + "0" + 1 + " " + "0" + day);
                    onee1 = "0" + day + " " + "Jan" + " " + year;
                    //   mEdit1.setText(onee1);
                    mEdit1_dialog.setText(onee1);
                } else {
                    if (month == 1) {
                        mEdit_dialog.setText(year + " " + "0" + 1 + " " + day);
                        //     mEdit.setText(year + " " + "0" + 1 + " " + day);
                        onee = day + " " + "Jan" + " " + year;
                        //   mEdit1.setText(onee);
                        mEdit1_dialog.setText(onee);
                    }
                }

                if (month == 2 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 2 + " " + "0" + day);
                    //  mEdit.setText(year + " " + "0" + 2 + " " + "0" + day);
                    two1 = "0" + day + " " + "Feb" + " " + year;
                    //  mEdit1.setText(two1);
                    mEdit1_dialog.setText(two1);
                } else {
                    if (month == 2) {
                        mEdit_dialog.setText(year + " " + "0" + 2 + " " + day);
                        //    mEdit.setText(year + " " + "0" + 2 + " " + day);
                        two = day + " " + "Feb" + " " + year;
                        //     mEdit1.setText(two);
                        mEdit1_dialog.setText(two);
                    }
                }

                if (month == 3 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 3 + " " + "0" + day);
                    //      mEdit.setText(year + " " + "0" + 3 + " " + "0" + day);
                    three1 = "0" + day + " " + "Mar" + " " + year;
                    //    mEdit1.setText(three1);
                    mEdit1_dialog.setText(three1);
                } else {
                    if (month == 3) {
                        mEdit_dialog.setText(year + " " + "0" + 3 + " " + day);
                        //      mEdit.setText(year + " " + "0" + 3 + " " + day);
                        three = day + " " + "Mar" + " " + year;
                        //      mEdit1.setText(three);
                        mEdit1_dialog.setText(three);
                    }
                }

                if (month == 4 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 4 + " " + "0" + day);
                    //       mEdit.setText(year + " " + "0" + 4 + " " + "0" + day);
                    four1 = "0" + day + " " + "Apr" + " " + year;
                    //     mEdit1.setText(four1);
                    mEdit1_dialog.setText(four1);
                } else {
                    if (month == 4) {
                        mEdit_dialog.setText(year + " " + "0" + 4 + " " + day);
                        //       mEdit.setText(year + " " + "0" + 4 + " " + day);
                        four = day + " " + "Apr" + " " + year;
                        //      mEdit1.setText(four);
                        mEdit1_dialog.setText(four);
                    }
                }

                if (month == 5 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 5 + " " + "0" + day);
                    //     mEdit.setText(year + " " + "0" + 5 + " " + "0" + day);
                    five1 = "0" + day + " " + "May" + " " + year;
                    //     mEdit1.setText(five1);
                    mEdit1_dialog.setText(five1);
                } else {
                    if (month == 5) {
                        mEdit_dialog.setText(year + " " + "0" + 5 + " " + day);
                        //      mEdit.setText(year + " " + "0" + 5 + " " + day);
                        five = day + " " + "May" + " " + year;
                        //       mEdit1.setText(five);
                        mEdit1_dialog.setText(five);
                    }
                }

                if (month == 6 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 6 + " " + "0" + day);
                    //    mEdit.setText(year + " " + "0" + 6 + " " + "0" + day);
                    six1 = "0" + day + " " + "Jun" + " " + year;
                    //      mEdit1.setText(six1);
                    mEdit1_dialog.setText(six1);
                } else {
                    if (month == 6) {
                        mEdit_dialog.setText(year + " " + "0" + 6 + " " + day);
                        //        mEdit.setText(year + " " + "0" + 6 + " " + day);
                        six = day + " " + "Jun" + " " + year;
                        //        mEdit1.setText(six);
                        mEdit1_dialog.setText(six);
                    }
                }

                if (month == 7 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 7 + " " + "0" + day);
                    //       mEdit.setText(year + " " + "0" + 7 + " " + "0" + day);
                    seven1 = "0" + day + " " + "Jul" + " " + year;
                    //     mEdit1.setText(seven1);
                    mEdit1_dialog.setText(seven1);
                } else {
                    if (month == 7) {
                        mEdit_dialog.setText(year + " " + "0" + 7 + " " + day);
                        //         mEdit.setText(year + " " + "0" + 7 + " " + day);
                        seven = day + " " + "Jul" + " " + year;
                        //        mEdit1.setText(seven);
                        mEdit1_dialog.setText(seven);
                    }
                }

                if (month == 8 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 8 + " " + "0" + day);
                    //      mEdit.setText(year + " " + "0" + 8 + " " + "0" + day);
                    eight1 = "0" + day + " " + "Aug" + " " + year;
                    //     mEdit1.setText(eight1);
                    mEdit1_dialog.setText(eight1);
                } else {
                    if (month == 8) {
                        mEdit_dialog.setText(year + " " + "0" + 8 + " " + day);
                        //         mEdit.setText(year + " " + "0" + 8 + " " + day);
                        eight = day + " " + "Aug" + " " + year;
                        //          mEdit1.setText(eight);
                        mEdit1_dialog.setText(eight);
                    }
                }

                if (month == 9 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 9 + " " + "0" + day);
                    //      mEdit.setText(year + " " + "0" + 9 + " " + "0" + day);
                    nine1 = "0" + day + " " + "Sep" + " " + year;
                    //       mEdit1.setText(nine1);
                    mEdit1_dialog.setText(nine1);
                } else {
                    if (month == 9) {
                        mEdit_dialog.setText(year + " " + "0" + 9 + " " + day);
                        //          mEdit.setText(year + " " + "0" + 9 + " " + day);
                        nine = day + " " + "Sep" + " " + year;
                        //          mEdit1.setText(nine);
                        mEdit1_dialog.setText(nine);
                    }
                }

                if (month == 10 && day < 10) {
                    mEdit_dialog.setText(year + " " + 10 + " " + "0" + day);
                    //       mEdit.setText(year + " " + 10 + " " + "0" + day);
                    ten1 = "0" + day + " " + "Oct" + " " + year;
                    //       mEdit1.setText(ten1);
                    mEdit1_dialog.setText(ten1);
                } else {
                    if (month == 10) {
                        mEdit_dialog.setText(year + " " + 10 + " " + day);
                        ten = day + " " + "Oct" + " " + year;
                        //          mEdit1.setText(ten);
                        mEdit1_dialog.setText(ten);
                    }
                }

                if (month == 11 && day < 10) {
                    mEdit_dialog.setText(year + " " + 11 + " " + "0" + day);
                    //        mEdit.setText(year + " " + 11 + " " + "0" + day);
                    eleven1 = "0" + day + " " + "Nov" + " " + year;
                    //        mEdit1.setText(eleven1);
                    mEdit1_dialog.setText(eleven1);
                } else {
                    if (month == 11) {
                        mEdit_dialog.setText(year + " " + 11 + " " + day);
                        //          mEdit.setText(year + " " + 11 + " " + day);
                        eleven = day + " " + "Nov" + " " + year;
                        //        mEdit1.setText(eleven);
                        mEdit1_dialog.setText(eleven);
                    }
                }

                if (month == 12 && day < 10) {
                    mEdit_dialog.setText(year + " " + 12 + " " + "0" + day);
                    //       mEdit.setText(year + " " + 12 + " " + "0" + day);
                    twelve1 = "0" + day + " " + "Dec" + " " + year;
                    //    mEdit1.setText(twelve1);
                    mEdit1_dialog.setText(twelve1);
                } else {
                    if (month == 12) {
                        mEdit_dialog.setText(year + " " + 12 + " " + day);
                        //         mEdit.setText(year + " " + 12 + " " + day);
                        twelve = day + " " + "Dec" + " " + year;
                        //        mEdit1.setText(twelve);
                        mEdit1_dialog.setText(twelve);
                    }
                }

            }

        });

        editText_from_day_visible_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(Expenses_list.this, R.style.timepicker_date_dialog, timePickerListener_open_dialogue, hour, minute,
                        false);

                timePickerDialog.show();
            }
        });

    }

    private void updateTime_open_dialog(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        String hour1 = "";
        if (hours < 10)
            hour1 = "0" + hours;
        else
            hour1 = String.valueOf(hours);


        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hour1).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        editText_from_day_visible_dialog.setText(aTime);
        editText_from_day_hide_dialog.setText(aTime);
        //  editText_from_day_visible.setText(aTime);
    }

    private void updateTime_close_dialog(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        String hour1 = "";
        if (hours < 10)
            hour1 = "0" + hours;
        else
            hour1 = String.valueOf(hours);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hour1).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        editText_to_day_visible_dialog.setText(aTime);
        editText_to_day_hide_dialog.setText(aTime);
        // editText_to_day_visible.setText(aTime);
    }

    private android.app.TimePickerDialog.OnTimeSetListener timePickerListener_open_dialogue = new android.app.TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime_open_dialog(hour, minute);

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minutes);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);


            String hour1 = "";
            if (hour < 10)
                hour1 = "0" + hour;
            else
                hour1 = String.valueOf(hour);

            String minutes1 = "";
            if (minute < 10)
                minutes1 = "0" + minute;
            else
                minutes1 = String.valueOf(minute);

            // editText_from_day_hide.setText(hour1 + "" + minutes1);
            editText_from_day_hide_dialog.setText(hour1 + "" + minutes1);


        }
    };

    private android.app.TimePickerDialog.OnTimeSetListener timePickerListener_close_dialogue = new android.app.TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime_close_dialog(hour, minute);

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minutes);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            String hour1 = "";
            if (hour < 10)
                hour1 = "0" + hour;
            else
                hour1 = String.valueOf(hour);

            String minutes1 = "";
            if (minute < 10)
                minutes1 = "0" + minute;
            else
                minutes1 = String.valueOf(minute);

            //editText_to_day_hide.setText(hour1 + "" + minutes1);
            editText_to_day_hide_dialog.setText(hour1 + "" + minutes1);
        }
    };

    private void initCustom2(Dialog dialog, String dialog_date, String dialog_datetimee_new) {

//        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        final String currentDateandTime1 = dialog_datetimee_new.substring(0, 8);

//        SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM yyyy");
        final String currentDateandTime2 = dialog_date;


//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time_hide1 = dialog_datetimee_new.substring(8, 10);
        String time_hide2 = dialog_datetimee_new.substring(10, 12);
        String time_hide = time_hide1+":"+time_hide2;

        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm aa");
        String time_visible = sdf1.format(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        Calendar calendar11 = Calendar.getInstance();
        calendar11.add(Calendar.DAY_OF_YEAR, -1);
        Date yesterday = calendar11.getTime();

        editText1_dialog = (TextView) dialog.findViewById(R.id.editText1_dialog);
        editText1_dialog.setText(currentDateandTime1);
    /*    editText1 = (TextView) findViewById(R.id.editText1);
        editText1.setText(currentDateandTime1);*/

        editText11_dialog = (TextView) dialog.findViewById(R.id.editText11_dialog);
        editText11_dialog.setText(currentDateandTime2);
/*        editText11 = (TextView) findViewById(R.id.editText11);
        editText11.setText(currentDateandTime2);*/


        editText_from_day_hide_dialog = (TextView) dialog.findViewById(R.id.editText_from_day_hide_dialog);
        editText_from_day_visible_dialog = (TextView) dialog.findViewById(R.id.editText_from_day_visible_dialog);


/*
        editText_from_day_hide = (TextView) findViewById(R.id.editText_from_day_hide);
        editText_from_day_visible = (TextView) findViewById(R.id.editText_from_day_visible);
        editText_to_day_hide = (TextView) findViewById(R.id.editText_to_day_hide);
        editText_to_day_visible = (TextView) findViewById(R.id.editText_to_day_visible);
*/


        String hourr = time_hide.substring(0, 2);
        String minn = time_hide.substring(3, 5);
        updateTime_open_dialog(Integer.parseInt(hourr), Integer.parseInt(minn));

        editText11_dialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Calendar now = Calendar.getInstance();
//                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
//                        datePickerListener,
//                        now.get(Calendar.YEAR),
//                        now.get(Calendar.MONTH),
//                        now.get(Calendar.DAY_OF_MONTH)
//
//
//                );
//
//                dpd.show(HomeActivity.this.getFragmentManager(), "Datepickerdialog");

                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        datePickerListener,now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMaxDate(Calendar.getInstance());
                dpd.show(Expenses_list.this.getFragmentManager(), "Datepickerdialog");
                //clickcounts++;




            }

            com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener datePickerListener
                    = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog, int selectedYear1, int selectedMonth1, int selectedDay1) {
                    year1 = selectedYear1;
                    month1 = selectedMonth1;
                    day1 = selectedDay1;

                    // set selected date into textview
                    populateSetDate(year1, month1 + 1, day1);
                }
            };





            public void populateSetDate(int year, int month, int day) {
        /*        TextView mEdit = (TextView) dialog.findViewById(R.id.editText1);
                TextView mEdit1  = (TextView)dialog.findViewById(R.id.editText11);*/
                TextView mEdit_dialog = (TextView) dialog.findViewById(R.id.editText1_dialog);
                TextView mEdit1_dialog  = (TextView)dialog.findViewById(R.id.editText11_dialog);

                if (month == 1 && day < 10) {
                    //   mEdit.setText(year + " " + "0" + 1 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + "0" + 1 + " " + "0" + day);
                    onee1 = "0" + day + " " + "Jan" + " " + year;
                    //   mEdit1.setText(onee1);
                    mEdit1_dialog.setText(onee1);
                } else {
                    if (month == 1) {
                        mEdit_dialog.setText(year + " " + "0" + 1 + " " + day);
                        //     mEdit.setText(year + " " + "0" + 1 + " " + day);
                        onee = day + " " + "Jan" + " " + year;
                        //   mEdit1.setText(onee);
                        mEdit1_dialog.setText(onee);
                    }
                }

                if (month == 2 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 2 + " " + "0" + day);
                    //  mEdit.setText(year + " " + "0" + 2 + " " + "0" + day);
                    two1 = "0" + day + " " + "Feb" + " " + year;
                    //  mEdit1.setText(two1);
                    mEdit1_dialog.setText(two1);
                } else {
                    if (month == 2) {
                        mEdit_dialog.setText(year + " " + "0" + 2 + " " + day);
                        //    mEdit.setText(year + " " + "0" + 2 + " " + day);
                        two = day + " " + "Feb" + " " + year;
                        //     mEdit1.setText(two);
                        mEdit1_dialog.setText(two);
                    }
                }

                if (month == 3 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 3 + " " + "0" + day);
                    //      mEdit.setText(year + " " + "0" + 3 + " " + "0" + day);
                    three1 = "0" + day + " " + "Mar" + " " + year;
                    //    mEdit1.setText(three1);
                    mEdit1_dialog.setText(three1);
                } else {
                    if (month == 3) {
                        mEdit_dialog.setText(year + " " + "0" + 3 + " " + day);
                        //      mEdit.setText(year + " " + "0" + 3 + " " + day);
                        three = day + " " + "Mar" + " " + year;
                        //      mEdit1.setText(three);
                        mEdit1_dialog.setText(three);
                    }
                }

                if (month == 4 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 4 + " " + "0" + day);
                    //       mEdit.setText(year + " " + "0" + 4 + " " + "0" + day);
                    four1 = "0" + day + " " + "Apr" + " " + year;
                    //     mEdit1.setText(four1);
                    mEdit1_dialog.setText(four1);
                } else {
                    if (month == 4) {
                        mEdit_dialog.setText(year + " " + "0" + 4 + " " + day);
                        //       mEdit.setText(year + " " + "0" + 4 + " " + day);
                        four = day + " " + "Apr" + " " + year;
                        //      mEdit1.setText(four);
                        mEdit1_dialog.setText(four);
                    }
                }

                if (month == 5 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 5 + " " + "0" + day);
                    //     mEdit.setText(year + " " + "0" + 5 + " " + "0" + day);
                    five1 = "0" + day + " " + "May" + " " + year;
                    //     mEdit1.setText(five1);
                    mEdit1_dialog.setText(five1);
                } else {
                    if (month == 5) {
                        mEdit_dialog.setText(year + " " + "0" + 5 + " " + day);
                        //      mEdit.setText(year + " " + "0" + 5 + " " + day);
                        five = day + " " + "May" + " " + year;
                        //       mEdit1.setText(five);
                        mEdit1_dialog.setText(five);
                    }
                }

                if (month == 6 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 6 + " " + "0" + day);
                    //    mEdit.setText(year + " " + "0" + 6 + " " + "0" + day);
                    six1 = "0" + day + " " + "Jun" + " " + year;
                    //      mEdit1.setText(six1);
                    mEdit1_dialog.setText(six1);
                } else {
                    if (month == 6) {
                        mEdit_dialog.setText(year + " " + "0" + 6 + " " + day);
                        //        mEdit.setText(year + " " + "0" + 6 + " " + day);
                        six = day + " " + "Jun" + " " + year;
                        //        mEdit1.setText(six);
                        mEdit1_dialog.setText(six);
                    }
                }

                if (month == 7 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 7 + " " + "0" + day);
                    //       mEdit.setText(year + " " + "0" + 7 + " " + "0" + day);
                    seven1 = "0" + day + " " + "Jul" + " " + year;
                    //     mEdit1.setText(seven1);
                    mEdit1_dialog.setText(seven1);
                } else {
                    if (month == 7) {
                        mEdit_dialog.setText(year + " " + "0" + 7 + " " + day);
                        //         mEdit.setText(year + " " + "0" + 7 + " " + day);
                        seven = day + " " + "Jul" + " " + year;
                        //        mEdit1.setText(seven);
                        mEdit1_dialog.setText(seven);
                    }
                }

                if (month == 8 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 8 + " " + "0" + day);
                    //      mEdit.setText(year + " " + "0" + 8 + " " + "0" + day);
                    eight1 = "0" + day + " " + "Aug" + " " + year;
                    //     mEdit1.setText(eight1);
                    mEdit1_dialog.setText(eight1);
                } else {
                    if (month == 8) {
                        mEdit_dialog.setText(year + " " + "0" + 8 + " " + day);
                        //         mEdit.setText(year + " " + "0" + 8 + " " + day);
                        eight = day + " " + "Aug" + " " + year;
                        //          mEdit1.setText(eight);
                        mEdit1_dialog.setText(eight);
                    }
                }

                if (month == 9 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 9 + " " + "0" + day);
                    //      mEdit.setText(year + " " + "0" + 9 + " " + "0" + day);
                    nine1 = "0" + day + " " + "Sep" + " " + year;
                    //       mEdit1.setText(nine1);
                    mEdit1_dialog.setText(nine1);
                } else {
                    if (month == 9) {
                        mEdit_dialog.setText(year + " " + "0" + 9 + " " + day);
                        //          mEdit.setText(year + " " + "0" + 9 + " " + day);
                        nine = day + " " + "Sep" + " " + year;
                        //          mEdit1.setText(nine);
                        mEdit1_dialog.setText(nine);
                    }
                }

                if (month == 10 && day < 10) {
                    mEdit_dialog.setText(year + " " + 10 + " " + "0" + day);
                    //       mEdit.setText(year + " " + 10 + " " + "0" + day);
                    ten1 = "0" + day + " " + "Oct" + " " + year;
                    //       mEdit1.setText(ten1);
                    mEdit1_dialog.setText(ten1);
                } else {
                    if (month == 10) {
                        mEdit_dialog.setText(year + " " + 10 + " " + day);
                        ten = day + " " + "Oct" + " " + year;
                        //          mEdit1.setText(ten);
                        mEdit1_dialog.setText(ten);
                    }
                }

                if (month == 11 && day < 10) {
                    mEdit_dialog.setText(year + " " + 11 + " " + "0" + day);
                    //        mEdit.setText(year + " " + 11 + " " + "0" + day);
                    eleven1 = "0" + day + " " + "Nov" + " " + year;
                    //        mEdit1.setText(eleven1);
                    mEdit1_dialog.setText(eleven1);
                } else {
                    if (month == 11) {
                        mEdit_dialog.setText(year + " " + 11 + " " + day);
                        //          mEdit.setText(year + " " + 11 + " " + day);
                        eleven = day + " " + "Nov" + " " + year;
                        //        mEdit1.setText(eleven);
                        mEdit1_dialog.setText(eleven);
                    }
                }

                if (month == 12 && day < 10) {
                    mEdit_dialog.setText(year + " " + 12 + " " + "0" + day);
                    //       mEdit.setText(year + " " + 12 + " " + "0" + day);
                    twelve1 = "0" + day + " " + "Dec" + " " + year;
                    //    mEdit1.setText(twelve1);
                    mEdit1_dialog.setText(twelve1);
                } else {
                    if (month == 12) {
                        mEdit_dialog.setText(year + " " + 12 + " " + day);
                        //         mEdit.setText(year + " " + 12 + " " + day);
                        twelve = day + " " + "Dec" + " " + year;
                        //        mEdit1.setText(twelve);
                        mEdit1_dialog.setText(twelve);
                    }
                }

            }

        });

        editText_from_day_visible_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(Expenses_list.this, R.style.timepicker_date_dialog, timePickerListener_open_dialogue, hour, minute,
                        false);

                timePickerDialog.show();
            }
        });

    }

    public void webservicequery_sales(final String webserviceQuery){


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(Expenses_list.this);
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
        RequestQueue queue;
        StringRequest sr1;
        // queue = Volley.newRequestQueue(Expenses_list.this);

        queue= RequestSingleton.getInstance(Expenses_list.this).getInstance();

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

}
