package com.intuition.ivepos;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.intuition.ivepos.syncapp.StubProviderApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Rohithkumar on 9/14/2017.
 */

public class Micro_Inventory_Indent_Vendor_History extends AppCompatActivity {

    Uri contentUri,resultUri;
    public SQLiteDatabase db = null;

    TextView editText_from_day_visible, editText_from_day_hide, editText_to_day_visible, editText_to_day_hide;

    TextView editText1, editText2, editText11, editText22, editText1_filter, editText2_filter;

    TextView editText_from_day_visible_df, editText_from_day_hide_df, editText_to_day_visible_df, editText_to_day_hide_df;
    TextView editText1_df, editText2_df, editText11_df, editText22_df, editText1_filter_df, editText2_filter_df;

    TextView editText_from_day_visible_dv, editText_from_day_hide_dv;
    TextView editText1_dv, editText2_dv, editText11_dv, editText22_dv;

    TextView editText_from_day_visible_dp, editText_from_day_hide_dp;
    TextView editText1_dp, editText11_dp;

    String date1, date2;


    String onee, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve;
    String onee1, two1, three1, four1, five1, six1, seven1, eight1, nine1, ten1, eleven1, twelve1;

    private int year, year1;
    private int month, month1;
    private int day, day1;


    private int hour;
    private int minute;
    ListView list_history;

    SimpleCursorAdapter ddataAdapterr;

    String player1name, player2name;

    String selec = "1";

    String insert1_cc = "", insert1_rs = "", str_country;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.micro_inventory_indent_vendor_history);


        Bundle extras = getIntent().getExtras();
        player1name = extras.getString("PLAYER1NAME");
        player2name = extras.getString("PLAYER2NAME");


//        Toast.makeText(Inventory_Indent_Vendor_History.this, "1 "+player1name, Toast.LENGTH_SHORT).show();
//        Toast.makeText(Inventory_Indent_Vendor_History.this, "2 "+player2name, Toast.LENGTH_SHORT).show();


        TextView textView14 = (TextView) findViewById(R.id.textView14);
        textView14.setText(player1name);

        ImageView back_pressed = (ImageView) findViewById(R.id.back_pressed);
        back_pressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        editText1_filter = new TextView(Micro_Inventory_Indent_Vendor_History.this);
        editText2_filter = new TextView(Micro_Inventory_Indent_Vendor_History.this);

        editText1_filter_df = new TextView(Micro_Inventory_Indent_Vendor_History.this);
        editText2_filter_df = new TextView(Micro_Inventory_Indent_Vendor_History.this);

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

        editText1 = (TextView) findViewById(R.id.editText1);
        editText1.setText(currentDateandTime1);
        editText2 = (TextView) findViewById(R.id.editText2);
        editText2.setText(currentDateandTime1);

        editText11 = (TextView) findViewById(R.id.editText11);
        editText11.setText(currentDateandTime2);


        editText22 = (TextView) findViewById(R.id.editText22);
        editText22.setText(currentDateandTime2);


        editText_from_day_hide = (TextView) findViewById(R.id.editText_from_day_hide);
        editText_from_day_visible = (TextView) findViewById(R.id.editText_from_day_visible);


        editText_to_day_hide = (TextView) findViewById(R.id.editText_to_day_hide);
        editText_to_day_visible = (TextView) findViewById(R.id.editText_to_day_visible);


        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        Cursor cursor_country = db.rawQuery("SELECT * FROM Country_Selection", null);
        if (cursor_country.moveToFirst()){
            str_country = cursor_country.getString(1);
        }

        TextView inn = (TextView) findViewById(R.id.inn);
        TextView inn1 = (TextView) findViewById(R.id.inn1);
        TextView inn2 = (TextView) findViewById(R.id.inn2);

        if (str_country.toString().equals("Rupee") || str_country.toString().equals("India")) {
            insert1_cc = "\u20B9";
            insert1_rs = "Rs.";
            inn.setText(insert1_cc);
            inn1.setText(insert1_cc);
            inn2.setText(insert1_cc);
        }else {
            if (str_country.toString().equals("Pound") || str_country.toString().equals("UK Pound")) {
                insert1_cc = "\u00a3";
                insert1_rs = "BP.";
                inn.setText(insert1_cc);
                inn1.setText(insert1_cc);
                inn2.setText(insert1_cc);
            }else {
                if (str_country.toString().equals("Euro") || str_country.toString().equals("UK Euro")) {
                    insert1_cc = "\u20ac";
                    insert1_rs = "EU.";
                    inn.setText(insert1_cc);
                    inn1.setText(insert1_cc);
                    inn2.setText(insert1_cc);
                }else {
                    if (str_country.toString().equals("Dollar")) {
                        insert1_cc = "\u0024";
                        insert1_rs = "\u0024";
                        inn.setText(insert1_cc);
                        inn1.setText(insert1_cc);
                        inn2.setText(insert1_cc);
                    }else {
                        if (str_country.toString().equals("Dinars")) {
                            insert1_cc = "D";
                            insert1_rs = "KD.";
                            inn.setText(insert1_cc);
                            inn1.setText(insert1_cc);
                            inn2.setText(insert1_cc);
                        }else {
                            if (str_country.toString().equals("Shilling")) {
                                insert1_cc = "S";
                                insert1_rs = "S.";
                                inn.setText(insert1_cc);
                                inn1.setText(insert1_cc);
                                inn2.setText(insert1_cc);
                            }else {
                                if (str_country.toString().equals("Ringitt")) {
                                    insert1_cc = "R";
                                    insert1_rs = "RM.";
                                    inn.setText(insert1_cc);
                                    inn1.setText(insert1_cc);
                                    inn2.setText(insert1_cc);
                                }else {
                                    if (str_country.toString().equals("Rial")) {
                                        insert1_cc = "R";
                                        insert1_rs = "OR.";
                                        inn.setText(insert1_cc);
                                        inn1.setText(insert1_cc);
                                        inn2.setText(insert1_cc);
                                    }else {
                                        if (str_country.toString().equals("Yen")) {
                                            insert1_cc = "\u00a5";
                                            insert1_rs = "\u00a5";
                                            inn.setText(insert1_cc);
                                            inn1.setText(insert1_cc);
                                            inn2.setText(insert1_cc);
                                        }else {
                                            if (str_country.toString().equals("Papua New Guinean")) {
                                                insert1_cc = "K";
                                                insert1_rs = "K.";
                                                inn.setText(insert1_cc);
                                                inn1.setText(insert1_cc);
                                                inn2.setText(insert1_cc);
                                            }else {
                                                if (str_country.toString().equals("UAE")) {
                                                    insert1_cc = "D";
                                                    insert1_rs = "DH.";
                                                    inn.setText(insert1_cc);
                                                    inn1.setText(insert1_cc);
                                                    inn2.setText(insert1_cc);
                                                }else {
                                                    if (str_country.toString().equals("South African Rand")) {
                                                        insert1_cc = "R";
                                                        insert1_rs = "R.";
                                                        inn.setText(insert1_cc);
                                                        inn1.setText(insert1_cc);
                                                        inn2.setText(insert1_cc);
                                                    }else {
                                                        if (str_country.toString().equals("Congolese Franc")) {
                                                            insert1_cc = "F";
                                                            insert1_rs = "FC.";
                                                            inn.setText(insert1_cc);
                                                            inn1.setText(insert1_cc);
                                                            inn2.setText(insert1_cc);
                                                        }else {
                                                            if (str_country.toString().equals("Qatari Riyals")) {
                                                                insert1_cc = "QAR";
                                                                insert1_rs = "QAR.";
                                                                inn.setText(insert1_cc);
                                                                inn1.setText(insert1_cc);
                                                                inn2.setText(insert1_cc);
                                                            }else {
                                                                if (str_country.toString().equals("Dirhams")) {
                                                                    insert1_cc = "AED";
                                                                    insert1_rs = "AED.";
                                                                    inn.setText(insert1_cc);
                                                                    inn1.setText(insert1_cc);
                                                                    inn2.setText(insert1_cc);
                                                                }else {
                                                                    if (str_country.toString().equals("Kuwait Dinar")) {
                                                                        insert1_cc = "KWD";
                                                                        insert1_rs = "KWD.";
                                                                        inn.setText(insert1_cc);
                                                                        inn1.setText(insert1_cc);
                                                                        inn2.setText(insert1_cc);
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

        Cursor time_get = db.rawQuery("SELECT * FROM Working_hours", null);
        if (time_get.moveToFirst()){
            String two = time_get.getString(2);
            String four = time_get.getString(4);
            String five = time_get.getString(5);
            String six = time_get.getString(6);
            String three = time_get.getString(3);

            editText_from_day_hide.setText(five);
            editText_from_day_visible.setText(two);
            editText_to_day_hide.setText(six);
            editText_to_day_visible.setText(four);


            if (three.toString().equals("Tomorrow")) {

                try {
                    String string1 = five;
                    Date time1 = new SimpleDateFormat("HH:mm").parse(string1);
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.add(Calendar.DAY_OF_YEAR, 0);
                    calendar1.setTime(time1);

                    String string2 = "23:59";
                    Date time2 = new SimpleDateFormat("HH:mm").parse(string2);
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(time2);

                    String string1_new = "00:00";
                    Date time1_new = new SimpleDateFormat("HH:mm").parse(string1_new);
                    Calendar calendar1_new = Calendar.getInstance();
                    calendar1_new.setTime(time1_new);
                    calendar1_new.add(Calendar.DATE, 1);

                    String string2_new = five;
                    Date time2_new = new SimpleDateFormat("HH:mm").parse(string2_new);
                    Calendar calendar2_new = Calendar.getInstance();
                    calendar2_new.setTime(time2_new);
                    calendar2_new.add(Calendar.DATE, 1);


                    String someRandomTime = time_hide;
                    Date d = new SimpleDateFormat("HH:mm").parse(someRandomTime);
                    Calendar calendar3 = Calendar.getInstance();
                    calendar3.setTime(d);
//                    calendar3.add(Calendar.DATE, 1);

                    String someRandomTime_new = time_hide;
                    Date d_new = new SimpleDateFormat("HH:mm").parse(someRandomTime_new);
                    Calendar calendar3_new = Calendar.getInstance();
                    calendar3_new.setTime(d_new);
                    calendar3_new.add(Calendar.DATE, 1);

                    Date x = calendar3.getTime();
                    Date x_new = calendar3_new.getTime();
                    if ((x.after(calendar1.getTime()) && x.before(calendar2.getTime())) || (x_new.after(calendar1_new.getTime()) && x_new.before(calendar2_new.getTime()))) {
                        //checkes whether the current time is between 14:49:00 and 20:11:13.
                        System.out.println(true);
//                    Toast.makeText(getActivity(),"yes", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity(),"time is "+time_hide, Toast.LENGTH_SHORT).show();


                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                        String yesterday_visible = dateFormat.format(yesterday);

                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                        String yesterday_hide = dateFormat1.format(yesterday);


                        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy");
                        String tomorrow_visible = dateFormat2.format(tomorrow);

                        SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyyMMdd");
                        String tomorrow_hide = dateFormat3.format(tomorrow);

                        if ((x.after(calendar1.getTime()) && x.before(calendar2.getTime()))) {
                            editText11.setText(currentDateandTime2);
                            editText22.setText(tomorrow_visible);

                            editText1.setText(currentDateandTime1);
                            editText2.setText(tomorrow_hide);
                        }
                        if ((x_new.after(calendar1_new.getTime()) && x_new.before(calendar2_new.getTime()))) {
                            editText11.setText(yesterday_visible);
                            editText22.setText(currentDateandTime2);

                            editText1.setText(yesterday_hide);
                            editText2.setText(currentDateandTime1);
                        }


                    } else {
//                    Toast.makeText(getActivity(),"no", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity(),"time is "+time_hide, Toast.LENGTH_SHORT).show();

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                        String tomorrow_visible = dateFormat.format(tomorrow);

                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                        String tomorrow_hide = dateFormat1.format(tomorrow);

                        editText11.setText(currentDateandTime2);
                        editText22.setText(tomorrow_visible);

                        editText1.setText(currentDateandTime1);
                        editText2.setText(tomorrow_hide);

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


            if (five.toString().contains(":")){
                five = five.replace(":", "");
            }

            if (six.toString().contains(":")){
                six = six.replace(":", "");
            }

            String r1, r2;
            r1 = editText1.getText().toString();
            r2 = editText2.getText().toString();
            if (r1.toString().contains(" ")){
                r1 = r1.replace(" ", "");
            }
            if (r2.toString().contains(" ")){
                r2 = r2.replace(" ", "");
            }

            editText1_filter.setText(r1+""+five);
            editText2_filter.setText(r2+""+six);

        }
        time_get.close();


        date1 = editText1_filter.getText().toString();
        date2 = editText2_filter.getText().toString();

        final Cursor cursor_country1 = db.rawQuery("SELECT * FROM Country_Selection", null);
        if (cursor_country1.moveToFirst()){
            str_country = cursor_country1.getString(1);
        }

        list_history = (ListView) findViewById(R.id.list_history);
        Cursor cursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' GROUP BY datetimee_new_from", null);
        String[] fromFieldNames = {"from_date", "from_time", "vendor_name", "invoice", "total_bill_amount", "total_pay", "pending", "pending", "pending", "pending"};
        int[] toViewsID = {R.id.date_his, R.id.time_his, R.id.supplier_his, R.id.invoice_his, R.id.total_amount_his, R.id.paid_his, R.id.pending_his, R.id.inn, R.id.inn1, R.id.inn2};
        ddataAdapterr = new SimpleCursorAdapter(Micro_Inventory_Indent_Vendor_History.this, R.layout.inventory_indent_history_listview, cursor, fromFieldNames, toViewsID, 0);
//        list_history.setAdapter(ddataAdapterr);
        ddataAdapterr.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.inn || view.getId() == R.id.inn1 || view.getId() == R.id.inn2) {
                    final String tadl_id = cursor_country1.getString(cursor_country1.getColumnIndex("country"));
                    TextView dateTextView = (TextView) view;
                    if (tadl_id.toString().equals("India")){
                        dateTextView.setText(insert1_cc);
                    }else {
                        dateTextView.setText(insert1_cc);
                    }
                    return true;
                }
                return false;
            }
        });
        list_history.setAdapter(ddataAdapterr);

        final EditText searchView = (EditText) findViewById(R.id.searchView);
        searchView.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ddataAdapterr.getFilter().filter(s.toString());
            }
        });

        ddataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return fetchCountriesByName1(constraint.toString());
            }
        });


        ImageView delete_icon = (ImageView) findViewById(R.id.delete_icon);
        delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setText("");
            }
        });


        list_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final Cursor cursor1 = (Cursor) adapterView.getItemAtPosition(i);
                final String c_ItemID = cursor1.getString(cursor1.getColumnIndex("vendor_name"));
                final String c_phoneno = cursor1.getString(cursor1.getColumnIndex("vendor_phoneno"));
                final String c_invoice = cursor1.getString(cursor1.getColumnIndex("invoice"));
                final String c_bill_amount = cursor1.getString(cursor1.getColumnIndex("billamount"));
                final String c_tax_amount = cursor1.getString(cursor1.getColumnIndex("tax_amount"));
                final String c_percent_tax = cursor1.getString(cursor1.getColumnIndex("tax_percent"));
                final String c_disc_amount = cursor1.getString(cursor1.getColumnIndex("disc_amount"));
                final String c_percent_disc = cursor1.getString(cursor1.getColumnIndex("disc_percent"));
                final String c_total_amount = cursor1.getString(cursor1.getColumnIndex("total_bill_amount"));
                final String c_date = cursor1.getString(cursor1.getColumnIndex("from_date"));
                final String c_time = cursor1.getString(cursor1.getColumnIndex("from_time"));
                final String c_due_date = cursor1.getString(cursor1.getColumnIndex("due_date"));

                final String c_from_date_hide = cursor1.getString(cursor1.getColumnIndex("datetimee_new_from"));
                final String c_due_date_hide = cursor1.getString(cursor1.getColumnIndex("datetimee_new_due"));

                final Dialog dialog1 = new Dialog(Micro_Inventory_Indent_Vendor_History.this, R.style.timepicker_date_dialog);
                dialog1.setContentView(R.layout.dialog_inventory_indent_report_itemclick);
                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog1.setCanceledOnTouchOutside(false);
                dialog1.show();


                Cursor cursor21 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND from_date = '"+c_date+"' AND from_time = '"+c_time+"'", null);
                if (cursor21.moveToFirst()){
                    do {
                        String pend = cursor21.getString(16);
                        String dateti = cursor21.getString(13);
                        if (pend.toString().equals("0.0") || pend.toString().equals("0")){
                            Cursor cursor22 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from = '"+dateti+"'", null);
                            if (cursor22.moveToFirst()){
                                do {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("pending", "0.0");
                                    String where1 = "datetimee_new_from = '"+dateti+"' ";

                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_sold_details");
                                    getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                                    resultUri = new Uri.Builder()
                                            .scheme("content")
                                            .authority(StubProviderApp.AUTHORITY)
                                            .path("Menulogin_checking")
                                            .appendQueryParameter("operation", "update")
                                            .appendQueryParameter("datetimee_new_from", dateti)
                                            .build();
                                    getContentResolver().notifyChange(resultUri, null);

//                                    db.update("Ingredient_sold_details", contentValues, where1, new String[]{});
                                }while (cursor22.moveToNext());
                            }
                        }
                    }while (cursor21.moveToNext());
                }

                ImageView closetext = (ImageView) dialog1.findViewById(R.id.closetext);
                closetext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });


                final TextView date = (TextView) dialog1.findViewById(R.id.date);
                date.setText(c_date);

                final TextView time = (TextView) dialog1.findViewById(R.id.time);
                time.setText(c_time);

                final TextView vendor_name = (TextView) dialog1.findViewById(R.id.vendor_name);
                vendor_name.setText(c_ItemID);

                TextView invoice = (TextView) dialog1.findViewById(R.id.invoice);
                invoice.setText(c_invoice);

                TextView bill_amount = (TextView) dialog1.findViewById(R.id.bill_amount);
                bill_amount.setText(c_bill_amount);

                TextView tax_amount = (TextView) dialog1.findViewById(R.id.tax_amount);
                tax_amount.setText(c_tax_amount);

                TextView percent_tax = (TextView) dialog1.findViewById(R.id.percent_tax);
                percent_tax.setText(c_percent_tax);

                TextView disc_amount = (TextView) dialog1.findViewById(R.id.disc_amount);
                disc_amount.setText(c_disc_amount);

                TextView percent_disc = (TextView) dialog1.findViewById(R.id.percent_disc);
                percent_disc.setText(c_percent_disc);

                final TextView total_amount = (TextView) dialog1.findViewById(R.id.total_amount);
                total_amount.setText(c_total_amount);

                final TextView pending_amount = (TextView) dialog1.findViewById(R.id.pending_amount);
                Cursor cursor2 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND from_time = '"+time.getText().toString()+"' AND vendor_name = '"+c_ItemID+"'", null);
                if (cursor2.moveToLast()){
                    String tot_pend = cursor2.getString(16);
                    pending_amount.setText(tot_pend);
                }

                Button quantitypay = (Button) dialog1.findViewById(R.id.quantitypay);

                if (pending_amount.getText().toString().equals("0")){
                    quantitypay.setVisibility(View.GONE);
                }

                final TextView due_date = (TextView) dialog1.findViewById(R.id.due_date);
                due_date.setText(c_due_date);

                final TextView total_paid = (TextView) dialog1.findViewById(R.id.total_paid);
                Cursor cursor3 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND from_time = '"+time.getText().toString()+"' AND vendor_name = '"+c_ItemID+"'", null);
                if (cursor3.moveToLast()){
                    String tot_paid = cursor3.getString(17);
                    total_paid.setText(tot_paid);
                }

                final ListView list_amount_paid = (ListView) dialog1.findViewById(R.id.listView);
                Cursor cursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND from_date = '"+c_date+"' AND from_time = '"+c_time+"' AND pay != '0'", null);
                String[] fromFieldNames = {"pay_date", "pay_time", "pay"};
                int[] toViewsID = {R.id.sublist_paid, R.id.sublist_paid2, R.id.sublist_paid3};
                final SimpleCursorAdapter ddataAdapterra = new SimpleCursorAdapter(Micro_Inventory_Indent_Vendor_History.this, R.layout.dialog_paid_listview, cursor, fromFieldNames, toViewsID, 0);
                list_amount_paid.setAdapter(ddataAdapterra);


                ListView listView1 = (ListView) dialog1.findViewById(R.id.listView1);
                final Cursor cursor4 = db.rawQuery("SELECT * FROM Ingredient_sold_item_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND from_time = '"+time.getText().toString()+"' AND vendor_name = '"+c_ItemID+"'", null);
                String[] fromFieldNames4 = {"itemname", "qty_add", "individual_price"};
                int[] toViewsID4 = {R.id.itemname, R.id.qty, R.id.indiv_price};
                BaseAdapter ddataAdapterr4 = new ImageCursorAdapter_Micro_Inventory_Itemslist1(Micro_Inventory_Indent_Vendor_History.this, R.layout.dialog_inventory_indent_itemslist, cursor4, fromFieldNames4, toViewsID4);
                listView1.setAdapter(ddataAdapterr4);


                ImageView edit_vendor_details = (ImageView) dialog1.findViewById(R.id.edit_vendor_details);
                edit_vendor_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog_vendor = new Dialog(Micro_Inventory_Indent_Vendor_History.this, R.style.timepicker_date_dialog);
                        dialog_vendor.setContentView(R.layout.dialog_inventory_intent_reports_listitem_edit_add);
                        dialog_vendor.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        dialog_vendor.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialog_vendor.setCanceledOnTouchOutside(false);
                        dialog_vendor.show();

                        TextView bill_totoal_rs = (TextView) dialog_vendor.findViewById(R.id.bill_totoal_rs);
                        TextView inn = (TextView) dialog_vendor.findViewById(R.id.inn);
                        TextView inn1 = (TextView) dialog_vendor.findViewById(R.id.inn1);
                        TextView inn2 = (TextView) dialog_vendor.findViewById(R.id.inn2);
                        TextView inn3 = (TextView) dialog_vendor.findViewById(R.id.inn3);
                        bill_totoal_rs.setText(insert1_cc);
                        inn.setText(insert1_cc);
                        inn1.setText(insert1_cc);
                        inn2.setText(insert1_cc);
                        inn3.setText(insert1_cc);

                        TextInputLayout payyy = (TextInputLayout) dialog_vendor.findViewById(R.id.payyy);
                        payyy.setVisibility(View.GONE);

                        ImageButton canceledit = (ImageButton) dialog_vendor.findViewById(R.id.canceledit);
                        canceledit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog_vendor.dismiss();
                            }
                        });

                        final TextView no_of_items = (TextView) dialog_vendor.findViewById(R.id.no_of_items);

                        Cursor cursor5 = db.rawQuery("SELECT COUNT(itemname) FROM Ingredient_sold_item_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND from_time = '"+time.getText().toString()+"' AND vendor_name = '"+c_ItemID+"'", null);
                        if (cursor5.moveToFirst()) {
                            int level = cursor5.getInt(0);
                            String total1 = String.valueOf(level);

                            no_of_items.setText(total1);
                        }

                        TextView bill_totoal_amount = (TextView) dialog_vendor.findViewById(R.id.bill_totoal_amount);
                        bill_totoal_amount.setText(total_amount.getText().toString());

                        ImageView image = (ImageView) dialog_vendor.findViewById(R.id.image);
                        image.setVisibility(View.GONE);


                        final EditText invoice_no = (EditText) dialog_vendor.findViewById(R.id.invoice_no);
                        invoice_no.setText(c_invoice);

                        final TextView vend_name = (TextView) dialog_vendor.findViewById(R.id.vend_name);
                        final TextView vend_phoneno = (TextView) dialog_vendor.findViewById(R.id.vend_phoneno);
                        final TextView vend_email = (TextView) dialog_vendor.findViewById(R.id.vend_email);
                        final TextView vend_address = (TextView) dialog_vendor.findViewById(R.id.vend_address);
                        final TextView vend_gstin = (TextView) dialog_vendor.findViewById(R.id.vend_gstin);

                        final LinearLayout vendor_det_lay = (LinearLayout) dialog_vendor.findViewById(R.id.vendor_det_lay);

                        Cursor cursor2 = db.rawQuery("SELECT * FROM Vendor_details WHERE vendor_name = '"+c_ItemID+"' AND vendor_phoneno = '"+c_phoneno+"'", null);
                        if (cursor2.moveToFirst()){
                            String name = cursor2.getString(1);
                            String ph = cursor2.getString(2);
                            String ema = cursor2.getString(3);
                            String add = cursor2.getString(4);
                            String gst = cursor2.getString(5);

                            vend_name.setText(name);
                            vend_phoneno.setText(ph);
                            vend_email.setText(ema);
                            vend_address.setText(add);
                            vend_gstin.setText(gst);
                            vendor_det_lay.setVisibility(View.VISIBLE);
                        }


                        vend_name.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Dialog dialog1 = new Dialog(Micro_Inventory_Indent_Vendor_History.this, R.style.notitle);
                                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                dialog1.setContentView(R.layout.dialog_vendor_list);

                                final ListView popupSpinner = (ListView) dialog1.findViewById(R.id.listView5);
                                ArrayList<String> my_arrayy = getTableValuesall();
                                final ArrayAdapter my_Adapterr = new ArrayAdapter(Micro_Inventory_Indent_Vendor_History.this, R.layout.spinner_row,
                                        my_arrayy);
                                popupSpinner.setAdapter(my_Adapterr);

                                EditText myFilter1 = (EditText) dialog1.findViewById(R.id.searchView);
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


                                LinearLayout cancelletter1 = (LinearLayout) dialog1.findViewById(R.id.custombar_return_wrapper);
                                cancelletter1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                        dialog1.dismiss();
                                    }
                                });


                                //selectionCurrent = String.valueOf(popupSpinner.getSelectedItemPosition());

                                popupSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String selectedSweet = popupSpinner.getItemAtPosition(position).toString();

                                        if (selectedSweet.toString().contains(" - ")){
                                            selectedSweet = selectedSweet.replace(" - ", "");
                                        }

                                        Cursor cursor2 = db.rawQuery("SELECT * FROM Vendor_details_micro", null);
                                        if (cursor2.moveToFirst()){
                                            do {
                                                String name = cursor2.getString(1);
                                                String ph = cursor2.getString(2);

                                                String name_ph = name+""+ph;

                                                if (name_ph.toString().equals(selectedSweet)){
                                                    vend_name.setText(name);
                                                }
                                            }while (cursor2.moveToNext());
                                        }
                                        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                        dialog1.dismiss();
                                        //String text = dialogC4_id.getText().toString();
//                                        Toast.makeText(Inventory_Indent_History.this, "Selected item: " + selectedSweet + " - " + position, Toast.LENGTH_SHORT).show();

                                        Cursor cursor3 = db.rawQuery("SELECT * FROM Vendor_details_micro", null);
                                        if (cursor3.moveToFirst()){
                                            do {
                                                String name = cursor3.getString(1);
                                                String ph = cursor3.getString(2);
                                                String ema = cursor3.getString(3);
                                                String add = cursor3.getString(4);
                                                String gst = cursor3.getString(5);

                                                String name_ph = name+""+ph;

                                                if (name_ph.toString().equals(selectedSweet)){
                                                    vend_phoneno.setText(ph);
                                                    vend_email.setText(ema);
                                                    vend_address.setText(add);
                                                    vend_gstin.setText(gst);
                                                    vendor_det_lay.setVisibility(View.VISIBLE);
                                                }
                                            }while (cursor3.moveToNext());
                                        }
                                    }
                                });

                                dialog1.show();

                            }
                        });


                        Button okedit_delete = (Button) dialog_vendor.findViewById(R.id.okedit_delete);
                        okedit_delete.setVisibility(View.GONE);


                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                        final String currentDateandTime1 = sdf2.format(new Date());

                        SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM yyyy");
                        final String currentDateandTime2 = sdf3.format(new Date());


                        editText1_dv = (TextView) dialog_vendor.findViewById(R.id.editText1);
                        editText1_dv.setText(currentDateandTime1);

                        editText11_dv = (TextView) dialog_vendor.findViewById(R.id.editText11);
                        editText11_dv.setText(currentDateandTime2);


                        editText2_dv = (TextView) dialog_vendor.findViewById(R.id.editText2);
                        editText2_dv.setText(currentDateandTime1);

                        editText22_dv = (TextView) dialog_vendor.findViewById(R.id.editText22);
                        editText22_dv.setText(currentDateandTime2);

                        Date dtt_new = new Date();
                        SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");
                        final String time24_new = sdf1t_new.format(dtt_new);

                        editText_from_day_hide_dv = (TextView) dialog_vendor.findViewById(R.id.editText_from_day_hide);
                        editText_from_day_visible_dv = (TextView) dialog_vendor.findViewById(R.id.editText_from_day_visible);

                        Date dt = new Date();
                        SimpleDateFormat sdff1 = new SimpleDateFormat("HH:MM aa");
                        String timee1 = sdff1.format(dt);

                        Date dt1 = new Date();
                        SimpleDateFormat sddff1 = new SimpleDateFormat("kkmm");
                        String time1 = sddff1.format(dt1);

                        editText_from_day_visible_dv.setText(timee1);
                        editText_from_day_hide_dv.setText(time1);

                        editText11_dv.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                Calendar now = Calendar.getInstance();
                                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                                        datePickerListener,now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                                );

                                dpd.show(Micro_Inventory_Indent_Vendor_History.this.getFragmentManager(), "Datepickerdialog");

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
                                TextView mEdit = (TextView) dialog_vendor.findViewById(R.id.editText1);
                                TextView mEdit1  = (TextView) dialog_vendor.findViewById(R.id.editText11);
                                if (month == 1 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 1 + " " + "0" + day);
                                    onee1 = "0" + day + " " + "Jan" + " " + year;
                                    mEdit1.setText(onee1);
                                } else {
                                    if (month == 1) {
                                        mEdit.setText(year + " " + "0" + 1 + " " + day);
                                        onee = day + " " + "Jan" + " " + year;
                                        mEdit1.setText(onee);
                                    }
                                }

                                if (month == 2 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 2 + " " + "0" + day);
                                    two1 = "0" + day + " " + "Feb" + " " + year;
                                    mEdit1.setText(two1);
                                } else {
                                    if (month == 2) {
                                        mEdit.setText(year + " " + "0" + 2 + " " + day);
                                        two = day + " " + "Feb" + " " + year;
                                        mEdit1.setText(two);
                                    }
                                }

                                if (month == 3 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 3 + " " + "0" + day);
                                    three1 = "0" + day + " " + "Mar" + " " + year;
                                    mEdit1.setText(three1);
                                } else {
                                    if (month == 3) {
                                        mEdit.setText(year + " " + "0" + 3 + " " + day);
                                        three = day + " " + "Mar" + " " + year;
                                        mEdit1.setText(three);
                                    }
                                }

                                if (month == 4 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 4 + " " + "0" + day);
                                    four1 = "0" + day + " " + "Apr" + " " + year;
                                    mEdit1.setText(four1);
                                } else {
                                    if (month == 4) {
                                        mEdit.setText(year + " " + "0" + 4 + " " + day);
                                        four = day + " " + "Apr" + " " + year;
                                        mEdit1.setText(four);
                                    }
                                }

                                if (month == 5 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 5 + " " + "0" + day);
                                    five1 = "0" + day + " " + "May" + " " + year;
                                    mEdit1.setText(five1);
                                } else {
                                    if (month == 5) {
                                        mEdit.setText(year + " " + "0" + 5 + " " + day);
                                        five = day + " " + "May" + " " + year;
                                        mEdit1.setText(five);
                                    }
                                }

                                if (month == 6 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 6 + " " + "0" + day);
                                    six1 = "0" + day + " " + "Jun" + " " + year;
                                    mEdit1.setText(six1);
                                } else {
                                    if (month == 6) {
                                        mEdit.setText(year + " " + "0" + 6 + " " + day);
                                        six = day + " " + "Jun" + " " + year;
                                        mEdit1.setText(six);
                                    }
                                }

                                if (month == 7 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 7 + " " + "0" + day);
                                    seven1 = "0" + day + " " + "Jul" + " " + year;
                                    mEdit1.setText(seven1);
                                } else {
                                    if (month == 7) {
                                        mEdit.setText(year + " " + "0" + 7 + " " + day);
                                        seven = day + " " + "Jul" + " " + year;
                                        mEdit1.setText(seven);
                                    }
                                }

                                if (month == 8 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 8 + " " + "0" + day);
                                    eight1 = "0" + day + " " + "Aug" + " " + year;
                                    mEdit1.setText(eight1);
                                } else {
                                    if (month == 8) {
                                        mEdit.setText(year + " " + "0" + 8 + " " + day);
                                        eight = day + " " + "Aug" + " " + year;
                                        mEdit1.setText(eight);
                                    }
                                }

                                if (month == 9 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 9 + " " + "0" + day);
                                    nine1 = "0" + day + " " + "Sep" + " " + year;
                                    mEdit1.setText(nine1);
                                } else {
                                    if (month == 9) {
                                        mEdit.setText(year + " " + "0" + 9 + " " + day);
                                        nine = day + " " + "Sep" + " " + year;
                                        mEdit1.setText(nine);
                                    }
                                }

                                if (month == 10 && day < 10) {
                                    mEdit.setText(year + " " + 10 + " " + "0" + day);
                                    ten1 = "0" + day + " " + "Oct" + " " + year;
                                    mEdit1.setText(ten1);
                                } else {
                                    if (month == 10) {
                                        mEdit.setText(year + " " + 10 + " " + day);
                                        ten = day + " " + "Oct" + " " + year;
                                        mEdit1.setText(ten);
                                    }
                                }

                                if (month == 11 && day < 10) {
                                    mEdit.setText(year + " " + 11 + " " + "0" + day);
                                    eleven1 = "0" + day + " " + "Nov" + " " + year;
                                    mEdit1.setText(eleven1);
                                } else {
                                    if (month == 11) {
                                        mEdit.setText(year + " " + 11 + " " + day);
                                        eleven = day + " " + "Nov" + " " + year;
                                        mEdit1.setText(eleven);
                                    }
                                }

                                if (month == 12 && day < 10) {
                                    mEdit.setText(year + " " + 12 + " " + "0" + day);
                                    twelve1 = "0" + day + " " + "Dec" + " " + year;
                                    mEdit1.setText(twelve1);
                                } else {
                                    if (month == 12) {
                                        mEdit.setText(year + " " + 12 + " " + day);
                                        twelve = day + " " + "Dec" + " " + year;
                                        mEdit1.setText(twelve);
                                    }
                                }

                            }

                        });


                        editText22_dv.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Calendar now = Calendar.getInstance();
                                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                                        datePickerListener,now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                                );

                                dpd.show(Micro_Inventory_Indent_Vendor_History.this.getFragmentManager(), "Datepickerdialog");
                            }

                            com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener datePickerListener
                                    = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog, int selectedYear, int selectedMonth, int selectedDay) {
                                    year = selectedYear;
                                    month = selectedMonth;
                                    day = selectedDay;

                                    // set selected date into textview
                                    populateSetDate(year, month + 1, day);
                                }
                            };

                            public void populateSetDate(int year, int month, int day) {
                                TextView mEdit = (TextView) dialog_vendor.findViewById(R.id.editText2);
                                TextView mEdit1  = (TextView) dialog_vendor.findViewById(R.id.editText22);
                                if (month == 1 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 1 + " " + "0" + day);
                                    onee1 = "0" + day + " " + "Jan" + " " + year;
                                    mEdit1.setText(onee1);
                                } else {
                                    if (month == 1) {
                                        mEdit.setText(year + " " + "0" + 1 + " " + day);
                                        onee = day + " " + "Jan" + " " + year;
                                        mEdit1.setText(onee);
                                    }
                                }

                                if (month == 2 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 2 + " " + "0" + day);
                                    two1 = "0" + day + " " + "Feb" + " " + year;
                                    mEdit1.setText(two1);
                                } else {
                                    if (month == 2) {
                                        mEdit.setText(year + " " + "0" + 2 + " " + day);
                                        two = day + " " + "Feb" + " " + year;
                                        mEdit1.setText(two);
                                    }
                                }

                                if (month == 3 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 3 + " " + "0" + day);
                                    three1 = "0" + day + " " + "Mar" + " " + year;
                                    mEdit1.setText(three1);
                                } else {
                                    if (month == 3) {
                                        mEdit.setText(year + " " + "0" + 3 + " " + day);
                                        three = day + " " + "Mar" + " " + year;
                                        mEdit1.setText(three);
                                    }
                                }

                                if (month == 4 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 4 + " " + "0" + day);
                                    four1 = "0" + day + " " + "Apr" + " " + year;
                                    mEdit1.setText(four1);
                                } else {
                                    if (month == 4) {
                                        mEdit.setText(year + " " + "0" + 4 + " " + day);
                                        four = day + " " + "Apr" + " " + year;
                                        mEdit1.setText(four);
                                    }
                                }

                                if (month == 5 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 5 + " " + "0" + day);
                                    five1 = "0" + day + " " + "May" + " " + year;
                                    mEdit1.setText(five1);
                                } else {
                                    if (month == 5) {
                                        mEdit.setText(year + " " + "0" + 5 + " " + day);
                                        five = day + " " + "May" + " " + year;
                                        mEdit1.setText(five);
                                    }
                                }

                                if (month == 6 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 6 + " " + "0" + day);
                                    six1 = "0" + day + " " + "Jun" + " " + year;
                                    mEdit1.setText(six1);
                                } else {
                                    if (month == 6) {
                                        mEdit.setText(year + " " + "0" + 6 + " " + day);
                                        six = day + " " + "Jun" + " " + year;
                                        mEdit1.setText(six);
                                    }
                                }

                                if (month == 7 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 7 + " " + "0" + day);
                                    seven1 = "0" + day + " " + "Jul" + " " + year;
                                    mEdit1.setText(seven1);
                                } else {
                                    if (month == 7) {
                                        mEdit.setText(year + " " + "0" + 7 + " " + day);
                                        seven = day + " " + "Jul" + " " + year;
                                        mEdit1.setText(seven);
                                    }
                                }

                                if (month == 8 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 8 + " " + "0" + day);
                                    eight1 = "0" + day + " " + "Aug" + " " + year;
                                    mEdit1.setText(eight1);
                                } else {
                                    if (month == 8) {
                                        mEdit.setText(year + " " + "0" + 8 + " " + day);
                                        eight = day + " " + "Aug" + " " + year;
                                        mEdit1.setText(eight);
                                    }
                                }

                                if (month == 9 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 9 + " " + "0" + day);
                                    nine1 = "0" + day + " " + "Sep" + " " + year;
                                    mEdit1.setText(nine1);
                                } else {
                                    if (month == 9) {
                                        mEdit.setText(year + " " + "0" + 9 + " " + day);
                                        nine = day + " " + "Sep" + " " + year;
                                        mEdit1.setText(nine);
                                    }
                                }

                                if (month == 10 && day < 10) {
                                    mEdit.setText(year + " " + 10 + " " + "0" + day);
                                    ten1 = "0" + day + " " + "Oct" + " " + year;
                                    mEdit1.setText(ten1);
                                } else {
                                    if (month == 10) {
                                        mEdit.setText(year + " " + 10 + " " + day);
                                        ten = day + " " + "Oct" + " " + year;
                                        mEdit1.setText(ten);
                                    }
                                }

                                if (month == 11 && day < 10) {
                                    mEdit.setText(year + " " + 11 + " " + "0" + day);
                                    eleven1 = "0" + day + " " + "Nov" + " " + year;
                                    mEdit1.setText(eleven1);
                                } else {
                                    if (month == 11) {
                                        mEdit.setText(year + " " + 11 + " " + day);
                                        eleven = day + " " + "Nov" + " " + year;
                                        mEdit1.setText(eleven);
                                    }
                                }

                                if (month == 12 && day < 10) {
                                    mEdit.setText(year + " " + 12 + " " + "0" + day);
                                    twelve1 = "0" + day + " " + "Dec" + " " + year;
                                    mEdit1.setText(twelve1);
                                } else {
                                    if (month == 12) {
                                        mEdit.setText(year + " " + 12 + " " + day);
                                        twelve = day + " " + "Dec" + " " + year;
                                        mEdit1.setText(twelve);
                                    }
                                }

                            }
                        });

                        editText_from_day_visible_dv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(Micro_Inventory_Indent_Vendor_History.this, R.style.timepicker_date_dialog, timePickerListener_open_dv, hour, minute,
                                        false);

                                timePickerDialog.show();
                            }
                        });

                        String av = c_from_date_hide.substring(0, 8);


                        editText1_dv.setText(av);
                        editText11_dv.setText(date.getText().toString());

                        editText2_dv.setText(c_due_date_hide);
                        editText22_dv.setText(due_date.getText().toString());

//                        editText_from_day_hide_dv.setText();
                        editText_from_day_visible_dv.setText(time.getText().toString());
                        String dtt = c_from_date_hide.substring(8, 12);
                        editText_from_day_hide_dv.setText(dtt);


                        ImageButton vendor_add = (ImageButton) dialog_vendor.findViewById(R.id.vendor_add);
                        vendor_add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Dialog dialog_add_vendor = new Dialog(Micro_Inventory_Indent_Vendor_History.this, R.style.timepicker_date_dialog);
                                dialog_add_vendor.setContentView(R.layout.dialog_inventory_intent_add_vendor);
                                dialog_add_vendor.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog_add_vendor.setCanceledOnTouchOutside(false);
                                dialog_add_vendor.show();

                                ImageButton canceledit = (ImageButton) dialog_add_vendor.findViewById(R.id.canceledit);
                                canceledit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog_add_vendor.dismiss();
                                    }
                                });

                                final EditText nameedit = (EditText) dialog_add_vendor.findViewById(R.id.nameedit);
                                final EditText phonenoedit = (EditText) dialog_add_vendor.findViewById(R.id.phonenoedit);
                                final EditText emailidedit = (EditText) dialog_add_vendor.findViewById(R.id.emailidedit);
                                final EditText addressedit = (EditText) dialog_add_vendor.findViewById(R.id.addressedit);
                                final EditText gsttinedit = (EditText) dialog_add_vendor.findViewById(R.id.gsttinedit);


                                final TextInputLayout nameedit_layout = (TextInputLayout) dialog_add_vendor.findViewById(R.id.nameedit_layout);
                                final TextInputLayout phonenoedit_layout = (TextInputLayout) dialog_add_vendor.findViewById(R.id.phonenoedit_layout);
                                final TextInputLayout emailidedit_layout = (TextInputLayout) dialog_add_vendor.findViewById(R.id.emailidedit_layout);

                                nameedit.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                        nameedit_layout.setError(null);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {

                                    }
                                });

                                phonenoedit.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                        phonenoedit_layout.setError(null);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {

                                    }
                                });


                                ImageButton okedit = (ImageButton) dialog_add_vendor.findViewById(R.id.okedit);
                                okedit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (nameedit.getText().toString().equals("") || phonenoedit.getText().toString().equals("")){
                                            if (nameedit.getText().toString().equals("")){
                                                nameedit_layout.setError("Enter name");
                                            }
                                            if (phonenoedit.getText().toString().equals("")){
                                                phonenoedit_layout.setError("Enter Phone no.");
                                            }
                                        }else {
                                            Cursor cursor2 = db.rawQuery("SELECT * FROM Vendor_details_micro WHERE vendor_phoneno = '"+phonenoedit.getText().toString()+"'", null);
                                            if (cursor2.moveToFirst()){
                                                phonenoedit_layout.setError("Phone no. already entered");
                                            }else {
                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("vendor_name", nameedit.getText().toString());
                                                contentValues.put("vendor_phoneno", phonenoedit.getText().toString());
                                                contentValues.put("vendor_email", emailidedit.getText().toString());
                                                contentValues.put("vendor_address", addressedit.getText().toString());
                                                contentValues.put("vendor_gst", gsttinedit.getText().toString());
                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Vendor_details_micro");
                                                resultUri = getContentResolver().insert(contentUri, contentValues);
                                                getContentResolver().notifyChange(resultUri, null);
//                                                db.insert("Vendor_details_micro", null, contentValues);

                                                dialog_add_vendor.dismiss();
                                            }
                                        }
                                    }
                                });

                            }
                        });


                        ImageButton okedit = (ImageButton) dialog_vendor.findViewById(R.id.okedit);
                        okedit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Cursor cursor6 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND from_date = '"+c_date+"' AND from_time = '"+c_time+"'", null);
                                if (cursor6.moveToFirst()){
                                    do {
                                        String id = cursor6.getString(0);
                                        String vend_na = cursor6.getString(1);
                                        String vend_ph = cursor6.getString(2);

//                                        Toast.makeText(Inventory_Indent_Vendor_History.this, "id is "+id, Toast.LENGTH_SHORT).show();

                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("vendor_name", vend_name.getText().toString());
                                        contentValues.put("vendor_phoneno", vend_phoneno.getText().toString());
                                        contentValues.put("invoice", invoice_no.getText().toString());
                                        contentValues.put("from_time", editText_from_day_visible_dv.getText().toString());
                                        contentValues.put("from_date", editText11_dv.getText().toString());
                                        contentValues.put("due_date", editText22_dv.getText().toString());
                                        contentValues.put("datetimee_new_from", editText1_dv.getText().toString()+""+editText_from_day_hide_dv.getText().toString());
                                        contentValues.put("datetimee_new_due", editText2_dv.getText().toString());

                                        String wherecu1 = "_id = '" + id + "'";

                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_sold_details");
                                        getContentResolver().update(contentUri, contentValues,wherecu1,new String[]{});
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProviderApp.AUTHORITY)
                                                .path("Ingredient_sold_details")
                                                .appendQueryParameter("operation", "update")
                                                .appendQueryParameter("_id", id)
                                                .build();
                                        getContentResolver().notifyChange(resultUri, null);

//                                        db.update("Ingredient_sold_details", contentValues, wherecu1, new String[]{});
                                    }while (cursor6.moveToNext());
                                }

                                Cursor cursor7 = db.rawQuery("SELECT * FROM Ingredient_sold_item_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND from_date = '"+c_date+"' AND from_time = '"+c_time+"'", null);
                                if (cursor7.moveToFirst()){
                                    do {
                                        String id = cursor7.getString(0);
                                        String vend_na = cursor7.getString(1);
                                        String vend_ph = cursor7.getString(2);

//                                        Toast.makeText(Inventory_Indent_Vendor_History.this, "id is "+id, Toast.LENGTH_SHORT).show();

                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("vendor_name", vend_name.getText().toString());
                                        contentValues.put("vendor_phoneno", vend_phoneno.getText().toString());
                                        contentValues.put("invoice", invoice_no.getText().toString());
                                        contentValues.put("from_time", editText_from_day_visible_dv.getText().toString());
                                        contentValues.put("from_date", editText11_dv.getText().toString());
                                        contentValues.put("due_date", editText22_dv.getText().toString());
                                        contentValues.put("datetimee_new_from", editText1_dv.getText().toString()+""+editText_from_day_hide_dv.getText().toString());
                                        contentValues.put("datetimee_new_due", editText2_dv.getText().toString());

                                        String wherecu1 = "_id = '" + id + "'";
                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_sold_item_details");
                                        getContentResolver().update(contentUri, contentValues,wherecu1,new String[]{});
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProviderApp.AUTHORITY)
                                                .path("Ingredient_sold_item_details")
                                                .appendQueryParameter("operation", "update")
                                                .appendQueryParameter("_id", id)
                                                .build();
                                        getContentResolver().notifyChange(resultUri, null);
//                                        db.update("Ingredient_sold_item_details", contentValues, wherecu1, new String[]{});
                                    }while (cursor6.moveToNext());
                                }


                                Cursor cursor8 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+vend_name.getText().toString()+"'", null);
                                if (cursor8.moveToFirst()){
                                    do {
                                        String id = cursor8.getString(0);
                                        String datetimee_new_from = cursor8.getString(13);
                                        String datetimee_new_due = cursor8.getString(14);

                                        if (datetimee_new_from.contains(" ")){
                                            datetimee_new_from = datetimee_new_from.replace(" ", "");
//                                            Toast.makeText(Inventory_Indent_Vendor_History.this, "1 "+datetimee_new_from, Toast.LENGTH_SHORT).show();
                                        }

                                        if (datetimee_new_due.contains(" ")){
                                            datetimee_new_due = datetimee_new_due.replace(" ", "");
//                                            Toast.makeText(Inventory_Indent_Vendor_History.this, "2 "+datetimee_new_due, Toast.LENGTH_SHORT).show();
                                        }

                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("datetimee_new_from", datetimee_new_from);
                                        contentValues.put("datetimee_new_due", datetimee_new_due);
                                        String wherecu1 = "_id = '" + id + "'";

                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_sold_details");
                                        getContentResolver().update(contentUri, contentValues,wherecu1,new String[]{});
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProviderApp.AUTHORITY)
                                                .path("Ingredient_sold_details")
                                                .appendQueryParameter("operation", "update")
                                                .appendQueryParameter("_id", id)
                                                .build();
                                        getContentResolver().notifyChange(resultUri, null);
//                                        db.update("Ingredient_sold_details", contentValues, wherecu1, new String[]{});
                                    }while (cursor8.moveToNext());
                                }

                                Cursor cursor9 = db.rawQuery("SELECT * FROM Ingredeint_sold_item_details WHERE vendor_name = '"+vend_name.getText().toString()+"'", null);
                                if (cursor9.moveToFirst()){
                                    do {
                                        String id = cursor9.getString(0);
                                        String datetimee_new_from = cursor9.getString(17);
                                        String datetimee_new_due = cursor9.getString(18);

                                        if (datetimee_new_from.toString().contains(" ")){
                                            datetimee_new_from = datetimee_new_from.replace(" ", "");
//                                            Toast.makeText(Inventory_Indent_History.this, "3 "+datetimee_new_from, Toast.LENGTH_SHORT).show();
                                        }

                                        if (datetimee_new_due.contains(" ")){
                                            datetimee_new_due = datetimee_new_due.replace(" ", "");
//                                            Toast.makeText(Inventory_Indent_History.this, "4 "+datetimee_new_due, Toast.LENGTH_SHORT).show();
                                        }

                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("datetimee_new_from", datetimee_new_from);
                                        contentValues.put("datetimee_new_due", datetimee_new_due);
                                        String wherecu1 = "_id = '" + id + "'";

                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_sold_item_details");
                                        getContentResolver().update(contentUri, contentValues,wherecu1,new String[]{});
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProviderApp.AUTHORITY)
                                                .path("Ingredient_sold_item_details")
                                                .appendQueryParameter("operation", "update")
                                                .appendQueryParameter("_id", id)
                                                .build();
                                        getContentResolver().notifyChange(resultUri, null);

//                                        db.update("Ingredient_sold_item_details", contentValues, wherecu1, new String[]{});
                                    }while (cursor9.moveToNext());
                                }

                                dialog_vendor.dismiss();

                                dialog1.dismiss();

                                Intent intent = new Intent(Micro_Inventory_Indent_Vendor_History.this, Micro_Inventory_Indent_History.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }
                        });

                    }
                });



                quantitypay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog_pay = new Dialog(Micro_Inventory_Indent_Vendor_History.this, R.style.timepicker_date_dialog);
                        dialog_pay.setContentView(R.layout.dialog_inventory_indent_pay);
                        dialog_pay.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        dialog_pay.setCanceledOnTouchOutside(false);
                        dialog_pay.show();

                        ImageButton canceledit = (ImageButton) dialog_pay.findViewById(R.id.canceledit);
                        canceledit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog_pay.dismiss();
                            }
                        });

                        editText1_dp = (TextView) dialog_pay.findViewById(R.id.editText1);//hide
                        editText11_dp = (TextView) dialog_pay.findViewById(R.id.editText11);//visible

                        editText_from_day_hide_dp = (TextView) dialog_pay.findViewById(R.id.editText_from_day_hide);
                        editText_from_day_visible_dp = (TextView) dialog_pay.findViewById(R.id.editText_from_day_visible);


                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                        final String currentDateandTime1 = sdf2.format(new Date());

                        SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM yyyy");
                        final String currentDateandTime2 = sdf3.format(new Date());


                        editText1_dp = (TextView) dialog_pay.findViewById(R.id.editText1);
                        editText1_dp.setText(currentDateandTime1);

                        editText11_dp = (TextView) dialog_pay.findViewById(R.id.editText11);
                        editText11_dp.setText(currentDateandTime2);


                        Date dtt_new = new Date();
                        SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");
                        final String time24_new = sdf1t_new.format(dtt_new);

                        editText_from_day_hide_dp = (TextView) dialog_pay.findViewById(R.id.editText_from_day_hide);
                        editText_from_day_visible_dp = (TextView) dialog_pay.findViewById(R.id.editText_from_day_visible);

                        Date dt = new Date();
                        SimpleDateFormat sdff1 = new SimpleDateFormat("hh:mm aa");
                        String timee1 = sdff1.format(dt);

                        Date dt1 = new Date();
                        SimpleDateFormat sddff1 = new SimpleDateFormat("kkmm");
                        final String time1 = sddff1.format(dt1);

                        editText_from_day_visible_dp.setText(timee1);
                        editText_from_day_hide_dp.setText(time1);

                        editText11_dp.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                Calendar now = Calendar.getInstance();
                                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                                        datePickerListener,now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                                );

                                dpd.show(Micro_Inventory_Indent_Vendor_History.this.getFragmentManager(), "Datepickerdialog");

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
                                TextView mEdit = (TextView) dialog_pay.findViewById(R.id.editText1);
                                TextView mEdit1  = (TextView) dialog_pay.findViewById(R.id.editText11);
                                if (month == 1 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 1 + " " + "0" + day);
                                    onee1 = "0" + day + " " + "Jan" + " " + year;
                                    mEdit1.setText(onee1);
                                } else {
                                    if (month == 1) {
                                        mEdit.setText(year + " " + "0" + 1 + " " + day);
                                        onee = day + " " + "Jan" + " " + year;
                                        mEdit1.setText(onee);
                                    }
                                }

                                if (month == 2 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 2 + " " + "0" + day);
                                    two1 = "0" + day + " " + "Feb" + " " + year;
                                    mEdit1.setText(two1);
                                } else {
                                    if (month == 2) {
                                        mEdit.setText(year + " " + "0" + 2 + " " + day);
                                        two = day + " " + "Feb" + " " + year;
                                        mEdit1.setText(two);
                                    }
                                }

                                if (month == 3 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 3 + " " + "0" + day);
                                    three1 = "0" + day + " " + "Mar" + " " + year;
                                    mEdit1.setText(three1);
                                } else {
                                    if (month == 3) {
                                        mEdit.setText(year + " " + "0" + 3 + " " + day);
                                        three = day + " " + "Mar" + " " + year;
                                        mEdit1.setText(three);
                                    }
                                }

                                if (month == 4 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 4 + " " + "0" + day);
                                    four1 = "0" + day + " " + "Apr" + " " + year;
                                    mEdit1.setText(four1);
                                } else {
                                    if (month == 4) {
                                        mEdit.setText(year + " " + "0" + 4 + " " + day);
                                        four = day + " " + "Apr" + " " + year;
                                        mEdit1.setText(four);
                                    }
                                }

                                if (month == 5 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 5 + " " + "0" + day);
                                    five1 = "0" + day + " " + "May" + " " + year;
                                    mEdit1.setText(five1);
                                } else {
                                    if (month == 5) {
                                        mEdit.setText(year + " " + "0" + 5 + " " + day);
                                        five = day + " " + "May" + " " + year;
                                        mEdit1.setText(five);
                                    }
                                }

                                if (month == 6 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 6 + " " + "0" + day);
                                    six1 = "0" + day + " " + "Jun" + " " + year;
                                    mEdit1.setText(six1);
                                } else {
                                    if (month == 6) {
                                        mEdit.setText(year + " " + "0" + 6 + " " + day);
                                        six = day + " " + "Jun" + " " + year;
                                        mEdit1.setText(six);
                                    }
                                }

                                if (month == 7 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 7 + " " + "0" + day);
                                    seven1 = "0" + day + " " + "Jul" + " " + year;
                                    mEdit1.setText(seven1);
                                } else {
                                    if (month == 7) {
                                        mEdit.setText(year + " " + "0" + 7 + " " + day);
                                        seven = day + " " + "Jul" + " " + year;
                                        mEdit1.setText(seven);
                                    }
                                }

                                if (month == 8 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 8 + " " + "0" + day);
                                    eight1 = "0" + day + " " + "Aug" + " " + year;
                                    mEdit1.setText(eight1);
                                } else {
                                    if (month == 8) {
                                        mEdit.setText(year + " " + "0" + 8 + " " + day);
                                        eight = day + " " + "Aug" + " " + year;
                                        mEdit1.setText(eight);
                                    }
                                }

                                if (month == 9 && day < 10) {
                                    mEdit.setText(year + " " + "0" + 9 + " " + "0" + day);
                                    nine1 = "0" + day + " " + "Sep" + " " + year;
                                    mEdit1.setText(nine1);
                                } else {
                                    if (month == 9) {
                                        mEdit.setText(year + " " + "0" + 9 + " " + day);
                                        nine = day + " " + "Sep" + " " + year;
                                        mEdit1.setText(nine);
                                    }
                                }

                                if (month == 10 && day < 10) {
                                    mEdit.setText(year + " " + 10 + " " + "0" + day);
                                    ten1 = "0" + day + " " + "Oct" + " " + year;
                                    mEdit1.setText(ten1);
                                } else {
                                    if (month == 10) {
                                        mEdit.setText(year + " " + 10 + " " + day);
                                        ten = day + " " + "Oct" + " " + year;
                                        mEdit1.setText(ten);
                                    }
                                }

                                if (month == 11 && day < 10) {
                                    mEdit.setText(year + " " + 11 + " " + "0" + day);
                                    eleven1 = "0" + day + " " + "Nov" + " " + year;
                                    mEdit1.setText(eleven1);
                                } else {
                                    if (month == 11) {
                                        mEdit.setText(year + " " + 11 + " " + day);
                                        eleven = day + " " + "Nov" + " " + year;
                                        mEdit1.setText(eleven);
                                    }
                                }

                                if (month == 12 && day < 10) {
                                    mEdit.setText(year + " " + 12 + " " + "0" + day);
                                    twelve1 = "0" + day + " " + "Dec" + " " + year;
                                    mEdit1.setText(twelve1);
                                } else {
                                    if (month == 12) {
                                        mEdit.setText(year + " " + 12 + " " + day);
                                        twelve = day + " " + "Dec" + " " + year;
                                        mEdit1.setText(twelve);
                                    }
                                }

                            }

                        });


                        editText_from_day_visible_dp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(Micro_Inventory_Indent_Vendor_History.this, R.style.timepicker_date_dialog, timePickerListener_open_dp, hour, minute,
                                        false);

                                timePickerDialog.show();
                            }
                        });


                        final EditText pay_amount = (EditText) dialog_pay.findViewById(R.id.pay_amount);

                        ImageButton okedit_pay = (ImageButton) dialog_pay.findViewById(R.id.okedit_pay);
                        okedit_pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (pay_amount.getText().toString().equals("")){
                                    pay_amount.setError("Enter number");
                                }else {
                                    if (Float.parseFloat(pay_amount.getText().toString()) > Float.parseFloat(pending_amount.getText().toString())){
                                        pay_amount.setError("Enter less than pending amount");
                                    }else {
                                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                                        final String currentDateandTime1 = sdf2.format(new Date());

                                        String aq = editText1_dp.getText().toString();
                                        if (aq.contains(" ")) {
                                            aq = aq.replace(" ", "");
                                        }
                                        if (Float.parseFloat(aq) > Float.parseFloat(currentDateandTime1)) {
//                                            Toast.makeText(Inventory_Indent_Vendor_History.this, "greater", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Cursor cursor5 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '" + vendor_name.getText().toString() + "' AND vendor_phoneno = '" + c_phoneno + "' AND from_time = '" + time.getText().toString() + "' AND from_date = '" + date.getText().toString() + "'", null);
                                            if (cursor5.moveToFirst()) {
//                                            do {
                                                String id = cursor5.getString(0);
                                                String ven_name = cursor5.getString(1);
                                                String ven_phon = cursor5.getString(2);
                                                String invoi = cursor5.getString(3);
                                                String bill_amo = cursor5.getString(4);
                                                String tax_p = cursor5.getString(5);
                                                String tax_a = cursor5.getString(6);
                                                String disc_p = cursor5.getString(7);
                                                String disc_a = cursor5.getString(8);
                                                String tot_bill_amo = cursor5.getString(9);
                                                String from_tim = cursor5.getString(10);
                                                String from_dat = cursor5.getString(11);
                                                String due_dat = cursor5.getString(12);
                                                String dat_tim_new_from = cursor5.getString(13);
                                                String dat_tim_new_due = cursor5.getString(14);
                                                String pay = cursor5.getString(15);
                                                String pendi = cursor5.getString(16);
                                                String tot_pay = cursor5.getString(17);
                                                String pay_dat = cursor5.getString(18);
                                                String pay_tim = cursor5.getString(19);
                                                String pay_dat_tim_new = cursor5.getString(20);
                                                String not_requ = cursor5.getString(21);

                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("vendor_name", ven_name);
                                                contentValues.put("vendor_phoneno", ven_phon);
                                                contentValues.put("invoice", invoi);
                                                contentValues.put("billamount", bill_amo);
                                                contentValues.put("tax_percent", tax_p);
                                                contentValues.put("tax_amount", tax_a);
                                                contentValues.put("disc_percent", disc_p);
                                                contentValues.put("disc_amount", disc_a);
                                                contentValues.put("total_bill_amount", tot_bill_amo);
                                                contentValues.put("from_time", from_tim);
                                                contentValues.put("from_date", from_dat);
                                                contentValues.put("due_date", due_dat);
                                                contentValues.put("datetimee_new_from", dat_tim_new_from);
                                                contentValues.put("datetimee_new_due", dat_tim_new_due);
                                                contentValues.put("pay", pay_amount.getText().toString());


                                                Cursor cursor6 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '" + vendor_name.getText().toString() + "' AND vendor_phoneno = '" + c_phoneno + "' AND from_time = '" + time.getText().toString() + "' AND from_date = '" + date.getText().toString() + "'", null);
                                                if (cursor6.moveToLast()) {
                                                    String pendi1 = cursor6.getString(16);
                                                    float a = Float.parseFloat(pendi1) - Float.parseFloat(pay_amount.getText().toString());
                                                    String a1 = String.format("%.1f", a);
                                                    contentValues.put("pending", a1);
                                                }

                                                Cursor cursor7 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '" + vendor_name.getText().toString() + "' AND vendor_phoneno = '" + c_phoneno + "' AND from_time = '" + time.getText().toString() + "' AND from_date = '" + date.getText().toString() + "'", null);
                                                if (cursor7.moveToLast()) {
                                                    String tot_pay1 = cursor7.getString(17);
                                                    float b = Float.parseFloat(tot_pay1) + Float.parseFloat(pay_amount.getText().toString());
                                                    String b1 = String.format("%.1f", b);
                                                    contentValues.put("total_pay", b1);
                                                }


                                                contentValues.put("pay_date", editText11_dp.getText().toString());
                                                contentValues.put("pay_time", editText_from_day_visible_dp.getText().toString());
                                                contentValues.put("pay_datetimeemew", aq + editText_from_day_hide_dp.getText().toString());
                                                contentValues.put("not_required", "");
                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_sold_details");
                                                resultUri = getContentResolver().insert(contentUri, contentValues);
                                                getContentResolver().notifyChange(resultUri, null);
//                                                db.insert("Ingredient_sold_details", null, contentValues);
//                                            }while (cursor5.moveToNext());
                                            }

                                            dialog_pay.dismiss();

                                            list_amount_paid.setAdapter(null);

                                            Cursor cursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND from_date = '" + c_date + "' AND from_time ='" + c_time + "' AND pay != '0'", null);
                                            String[] fromFieldNames = {"pay_date", "pay_time", "pay"};
                                            int[] toViewsID = {R.id.sublist_paid, R.id.sublist_paid2, R.id.sublist_paid3};
                                            final SimpleCursorAdapter ddataAdapterrt = new SimpleCursorAdapter(Micro_Inventory_Indent_Vendor_History.this, R.layout.dialog_paid_listview, cursor, fromFieldNames, toViewsID, 0);
                                            list_amount_paid.setAdapter(ddataAdapterrt);


                                            Cursor cursor3 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND from_time = '" + time.getText().toString() + "' AND vendor_name = '" + c_ItemID + "'", null);
                                            if (cursor3.moveToLast()) {
                                                String tot_paid = cursor3.getString(17);
                                                total_paid.setText(tot_paid);
                                            }

                                            Cursor cursor2 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND from_time = '" + time.getText().toString() + "' AND vendor_name = '" + c_ItemID + "'", null);
                                            if (cursor2.moveToLast()) {
                                                String tot_pend = cursor2.getString(16);
                                                pending_amount.setText(tot_pend);
                                            }

                                            cursor1.moveToPosition(i);
                                            cursor1.requery();
                                            ddataAdapterr.notifyDataSetChanged();



                                            if (selec.toString().equals("1")){
                                                TextView paid_his_filter = (TextView) findViewById(R.id.paid_his_filter);
                                                Cursor cursorx1 = db.rawQuery("SELECT SUM(pay) FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"'", null);
                                                if (cursorx1.moveToFirst()){
                                                    float cv = cursorx1.getFloat(0);
                                                    paid_his_filter.setText(String.valueOf(cv));
                                                }

                                                TextView total_his_filter = (TextView) findViewById(R.id.total_his_filter);

                                                float co1 = 0;
                                                Cursor cursor1 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' GROUP BY datetimee_new_from", null);
                                                if (cursor1.moveToFirst()){
                                                    do {
                                                        String dateti = cursor1.getString(13);
//                                    Toast.makeText(Inventory_Indent_Vendor_History.this, "1 "+dateti, Toast.LENGTH_SHORT).show();

                                                        Cursor ccursor2 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from = '"+dateti+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' ", null);
                                                        if (ccursor2.moveToFirst()){
                                                            String tot = ccursor2.getString(9);
//                                        Toast.makeText(Inventory_Indent_Vendor_History.this, "2 "+tot, Toast.LENGTH_SHORT).show();
                                                            co1 = co1+Float.parseFloat(tot);

                                                        }
                                                    }while (cursor1.moveToNext());
                                                }
                                                total_his_filter.setText(String.valueOf(co1));

                                                float cvb = Float.parseFloat(total_his_filter.getText().toString()) - Float.parseFloat(paid_his_filter.getText().toString());

                                                TextView pending_his_filter = (TextView) findViewById(R.id.pending_his_filter);
                                                pending_his_filter.setText(String.valueOf(cvb));

                                            }
                                            if (selec.toString().equals("2")){
                                                TextView paid_his_filter = (TextView) findViewById(R.id.paid_his_filter);
                                                Cursor cursorx1 = db.rawQuery("SELECT SUM(pay) FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter_df.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter_df.getText().toString()+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"'", null);
                                                if (cursorx1.moveToFirst()){
                                                    float cv = cursorx1.getFloat(0);
                                                    paid_his_filter.setText(String.valueOf(cv));
                                                }

                                                TextView total_his_filter = (TextView) findViewById(R.id.total_his_filter);

                                                float co1 = 0;
                                                Cursor cursor1 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter_df.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter_df.getText().toString()+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' GROUP BY datetimee_new_from", null);
                                                if (cursor1.moveToFirst()){
                                                    do {
                                                        String dateti = cursor1.getString(13);
//                                    Toast.makeText(Inventory_Indent_Vendor_History.this, "1 "+dateti, Toast.LENGTH_SHORT).show();

                                                        Cursor ccursor2 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from = '"+dateti+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' ", null);
                                                        if (ccursor2.moveToFirst()){
                                                            String tot = ccursor2.getString(9);
//                                        Toast.makeText(Inventory_Indent_Vendor_History.this, "2 "+tot, Toast.LENGTH_SHORT).show();
                                                            co1 = co1+Float.parseFloat(tot);

                                                        }
                                                    }while (cursor1.moveToNext());
                                                }
                                                total_his_filter.setText(String.valueOf(co1));

                                                float cvb = Float.parseFloat(total_his_filter.getText().toString()) - Float.parseFloat(paid_his_filter.getText().toString());

                                                TextView pending_his_filter = (TextView) findViewById(R.id.pending_his_filter);
                                                pending_his_filter.setText(String.valueOf(cvb));
                                            }
                                            if (selec.toString().equals("3")){
                                                TextView paid_his_filter = (TextView) findViewById(R.id.paid_his_filter);
                                                Cursor cursorx1 = db.rawQuery("SELECT SUM(pay) FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"'", null);
                                                if (cursorx1.moveToFirst()){
                                                    float cv = cursorx1.getFloat(0);
                                                    paid_his_filter.setText(String.valueOf(cv));
                                                }

                                                TextView total_his_filter = (TextView) findViewById(R.id.total_his_filter);
//        }

                                                float co1 = 0;
                                                Cursor cursorq1 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' GROUP BY datetimee_new_from", null);
                                                if (cursorq1.moveToFirst()){
                                                    do {
                                                        String dateti = cursorq1.getString(13);
//                                    Toast.makeText(Inventory_Indent_Vendor_History.this, "1 "+dateti, Toast.LENGTH_SHORT).show();

                                                        Cursor cursorq2 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from = '"+dateti+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' ", null);
                                                        if (cursorq2.moveToFirst()){
                                                            String tot = cursorq2.getString(9);
//                                        Toast.makeText(Inventory_Indent_Vendor_History.this, "2 "+tot, Toast.LENGTH_SHORT).show();
                                                            co1 = co1+Float.parseFloat(tot);

                                                        }
                                                    }while (cursorq1.moveToNext());
                                                }
                                                total_his_filter.setText(String.valueOf(co1));

                                                float cvb = Float.parseFloat(total_his_filter.getText().toString()) - Float.parseFloat(paid_his_filter.getText().toString());

                                                TextView pending_his_filter = (TextView) findViewById(R.id.pending_his_filter);
                                                pending_his_filter.setText(String.valueOf(cvb));
                                            }
                                            if (selec.toString().equals("4")){
                                                TextView paid_his_filter = (TextView) findViewById(R.id.paid_his_filter);
                                                Cursor cursorx1 = db.rawQuery("SELECT SUM(pay) FROM Ingredient_sold_details WHERE  vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND pending != '0.0'", null);
                                                if (cursorx1.moveToFirst()){
                                                    float cv = cursorx1.getFloat(0);
                                                    paid_his_filter.setText(String.valueOf(cv));
                                                }

                                                TextView total_his_filter = (TextView) findViewById(R.id.total_his_filter);

                                                float co1 = 0;
                                                Cursor cursor1 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND pending != '0.0' GROUP BY datetimee_new_from", null);
                                                if (cursor1.moveToFirst()){
                                                    do {
                                                        String dateti = cursor1.getString(13);
//                                    Toast.makeText(Inventory_Indent_Vendor_History.this, "1 "+dateti, Toast.LENGTH_SHORT).show();

                                                        Cursor ccursor2 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from = '"+dateti+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND pending != '0.0'", null);
                                                        if (ccursor2.moveToFirst()){
                                                            String tot = ccursor2.getString(9);
//                                        Toast.makeText(Inventory_Indent_Vendor_History.this, "2 "+tot, Toast.LENGTH_SHORT).show();
                                                            co1 = co1+Float.parseFloat(tot);

                                                        }
                                                    }while (cursor1.moveToNext());
                                                }
                                                total_his_filter.setText(String.valueOf(co1));

                                                float cvb = Float.parseFloat(total_his_filter.getText().toString()) - Float.parseFloat(paid_his_filter.getText().toString());

                                                TextView pending_his_filter = (TextView) findViewById(R.id.pending_his_filter);
                                                pending_his_filter.setText(String.valueOf(cvb));
                                            }
                                            if (selec.toString().equals("5")){
                                                TextView paid_his_filter = (TextView) findViewById(R.id.paid_his_filter);
                                                Cursor cursorx1 = db.rawQuery("SELECT SUM(pay) FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND pending = '0' OR pending = '0.0'", null);
                                                if (cursorx1.moveToFirst()){
                                                    float cv = cursorx1.getFloat(0);
                                                    paid_his_filter.setText(String.valueOf(cv));
                                                }

                                                TextView total_his_filter = (TextView) findViewById(R.id.total_his_filter);

                                                float co1 = 0;
                                                Cursor cursorq1 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND pending = '0' OR pending = '0.0' GROUP BY datetimee_new_from", null);
                                                if (cursorq1.moveToFirst()){
                                                    do {
                                                        String dateti = cursorq1.getString(13);
//                                    Toast.makeText(Inventory_Indent_Vendor_History.this, "1 "+dateti, Toast.LENGTH_SHORT).show();

                                                        Cursor cursorq2 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from = '"+dateti+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND pending = '0' OR pending = '0.0' ", null);
                                                        if (cursorq2.moveToFirst()){
                                                            String tot = cursorq2.getString(9);
//                                        Toast.makeText(Inventory_Indent_Vendor_History.this, "2 "+tot, Toast.LENGTH_SHORT).show();
                                                            co1 = co1+Float.parseFloat(tot);

                                                        }
                                                    }while (cursorq1.moveToNext());
                                                }
                                                total_his_filter.setText(String.valueOf(co1));

                                                float cvb = Float.parseFloat(total_his_filter.getText().toString()) - Float.parseFloat(paid_his_filter.getText().toString());

                                                TextView pending_his_filter = (TextView) findViewById(R.id.pending_his_filter);
                                                pending_his_filter.setText(String.valueOf(cvb));
                                            }
                                            if (selec.toString().equals("6")){
                                                TextView paid_his_filter = (TextView) findViewById(R.id.paid_his_filter);
                                                Cursor cursorx1 = db.rawQuery("SELECT SUM(pay) FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND datetimee_new_due < '"+currentDateandTime1+"' AND pending != '0'", null);
                                                if (cursorx1.moveToFirst()){
                                                    float cv = cursorx1.getFloat(0);
                                                    paid_his_filter.setText(String.valueOf(cv));
                                                }

                                                TextView total_his_filter = (TextView) findViewById(R.id.total_his_filter);

                                                float co1 = 0;
                                                Cursor cursorq1 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND datetimee_new_due < '"+currentDateandTime1+"' AND pending != '0' GROUP BY datetimee_new_from", null);
                                                if (cursorq1.moveToFirst()){
                                                    do {
                                                        String dateti = cursorq1.getString(13);
//                                    Toast.makeText(Inventory_Indent_Vendor_History.this, "1 "+dateti, Toast.LENGTH_SHORT).show();

                                                        Cursor cursorq2 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from = '"+dateti+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND datetimee_new_due < '"+currentDateandTime1+"' AND pending != '0'", null);
                                                        if (cursorq2.moveToFirst()){
                                                            String tot = cursorq2.getString(9);
//                                        Toast.makeText(Inventory_Indent_Vendor_History.this, "2 "+tot, Toast.LENGTH_SHORT).show();
                                                            co1 = co1+Float.parseFloat(tot);

                                                        }
                                                    }while (cursorq1.moveToNext());
                                                }
                                                total_his_filter.setText(String.valueOf(co1));

                                                float cvb = Float.parseFloat(total_his_filter.getText().toString()) - Float.parseFloat(paid_his_filter.getText().toString());

                                                TextView pending_his_filter = (TextView) findViewById(R.id.pending_his_filter);
                                                pending_his_filter.setText(String.valueOf(cvb));
                                            }




                                        }
                                    }
                                }
                            }
                        });

                    }
                });

            }
        });


        RelativeLayout filter = (RelativeLayout) findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog_filter = new Dialog(Micro_Inventory_Indent_Vendor_History.this, R.style.timepicker_date_dialog);
                dialog_filter.setContentView(R.layout.dialog_inventory_indent_report_filter);
                dialog_filter.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                dialog_filter.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog_filter.setCanceledOnTouchOutside(false);
                dialog_filter.show();


                ImageView closetext = (ImageView) dialog_filter.findViewById(R.id.closetext);
                closetext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_filter.dismiss();
                    }
                });

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                final String currentDateandTime1 = sdf2.format(new Date());

                SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM yyyy");
                final String currentDateandTime2 = sdf3.format(new Date());


                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                final String time_hide = sdf.format(new Date());

                SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm aa");
                String time_visible = sdf1.format(new Date());

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                final Date tomorrow = calendar.getTime();

                Calendar calendar11 = Calendar.getInstance();
                calendar11.add(Calendar.DAY_OF_YEAR, -1);
                final Date yesterday = calendar11.getTime();

                editText1_df = (TextView) dialog_filter.findViewById(R.id.editText1);
                editText1_df.setText(currentDateandTime1);
                editText2_df = (TextView) dialog_filter.findViewById(R.id.editText2);
                editText2_df.setText(currentDateandTime1);

                editText11_df = (TextView) dialog_filter.findViewById(R.id.editText11);
                editText11_df.setText(currentDateandTime2);


                editText22_df = (TextView) dialog_filter.findViewById(R.id.editText22);
                editText22_df.setText(currentDateandTime2);


                editText_from_day_hide_df = (TextView) dialog_filter.findViewById(R.id.editText_from_day_hide);
                editText_from_day_visible_df = (TextView) dialog_filter.findViewById(R.id.editText_from_day_visible);


                editText_to_day_hide_df = (TextView) dialog_filter.findViewById(R.id.editText_to_day_hide);
                editText_to_day_visible_df = (TextView) dialog_filter.findViewById(R.id.editText_to_day_visible);

                Date dt = new Date();
                SimpleDateFormat sdff1 = new SimpleDateFormat("hh:mm aa");
                String timee1 = sdff1.format(dt);

                Date dt1 = new Date();
                SimpleDateFormat sddff1 = new SimpleDateFormat("kkmm");
                String time1 = sddff1.format(dt1);

                editText_from_day_visible_df.setText(timee1);
                editText_from_day_hide_df.setText(time1);

                editText_to_day_visible_df.setText(timee1);
                editText_to_day_hide_df.setText(time1);


                editText11_df.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Calendar now = Calendar.getInstance();
                        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                                datePickerListener,now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                        );

                        dpd.show(Micro_Inventory_Indent_Vendor_History.this.getFragmentManager(), "Datepickerdialog");

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
                        TextView mEdit = (TextView) dialog_filter.findViewById(R.id.editText1);
                        TextView mEdit1  = (TextView) dialog_filter.findViewById(R.id.editText11);
                        if (month == 1 && day < 10) {
                            mEdit.setText(year + " " + "0" + 1 + " " + "0" + day);
                            onee1 = "0" + day + " " + "Jan" + " " + year;
                            mEdit1.setText(onee1);
                        } else {
                            if (month == 1) {
                                mEdit.setText(year + " " + "0" + 1 + " " + day);
                                onee = day + " " + "Jan" + " " + year;
                                mEdit1.setText(onee);
                            }
                        }

                        if (month == 2 && day < 10) {
                            mEdit.setText(year + " " + "0" + 2 + " " + "0" + day);
                            two1 = "0" + day + " " + "Feb" + " " + year;
                            mEdit1.setText(two1);
                        } else {
                            if (month == 2) {
                                mEdit.setText(year + " " + "0" + 2 + " " + day);
                                two = day + " " + "Feb" + " " + year;
                                mEdit1.setText(two);
                            }
                        }

                        if (month == 3 && day < 10) {
                            mEdit.setText(year + " " + "0" + 3 + " " + "0" + day);
                            three1 = "0" + day + " " + "Mar" + " " + year;
                            mEdit1.setText(three1);
                        } else {
                            if (month == 3) {
                                mEdit.setText(year + " " + "0" + 3 + " " + day);
                                three = day + " " + "Mar" + " " + year;
                                mEdit1.setText(three);
                            }
                        }

                        if (month == 4 && day < 10) {
                            mEdit.setText(year + " " + "0" + 4 + " " + "0" + day);
                            four1 = "0" + day + " " + "Apr" + " " + year;
                            mEdit1.setText(four1);
                        } else {
                            if (month == 4) {
                                mEdit.setText(year + " " + "0" + 4 + " " + day);
                                four = day + " " + "Apr" + " " + year;
                                mEdit1.setText(four);
                            }
                        }

                        if (month == 5 && day < 10) {
                            mEdit.setText(year + " " + "0" + 5 + " " + "0" + day);
                            five1 = "0" + day + " " + "May" + " " + year;
                            mEdit1.setText(five1);
                        } else {
                            if (month == 5) {
                                mEdit.setText(year + " " + "0" + 5 + " " + day);
                                five = day + " " + "May" + " " + year;
                                mEdit1.setText(five);
                            }
                        }

                        if (month == 6 && day < 10) {
                            mEdit.setText(year + " " + "0" + 6 + " " + "0" + day);
                            six1 = "0" + day + " " + "Jun" + " " + year;
                            mEdit1.setText(six1);
                        } else {
                            if (month == 6) {
                                mEdit.setText(year + " " + "0" + 6 + " " + day);
                                six = day + " " + "Jun" + " " + year;
                                mEdit1.setText(six);
                            }
                        }

                        if (month == 7 && day < 10) {
                            mEdit.setText(year + " " + "0" + 7 + " " + "0" + day);
                            seven1 = "0" + day + " " + "Jul" + " " + year;
                            mEdit1.setText(seven1);
                        } else {
                            if (month == 7) {
                                mEdit.setText(year + " " + "0" + 7 + " " + day);
                                seven = day + " " + "Jul" + " " + year;
                                mEdit1.setText(seven);
                            }
                        }

                        if (month == 8 && day < 10) {
                            mEdit.setText(year + " " + "0" + 8 + " " + "0" + day);
                            eight1 = "0" + day + " " + "Aug" + " " + year;
                            mEdit1.setText(eight1);
                        } else {
                            if (month == 8) {
                                mEdit.setText(year + " " + "0" + 8 + " " + day);
                                eight = day + " " + "Aug" + " " + year;
                                mEdit1.setText(eight);
                            }
                        }

                        if (month == 9 && day < 10) {
                            mEdit.setText(year + " " + "0" + 9 + " " + "0" + day);
                            nine1 = "0" + day + " " + "Sep" + " " + year;
                            mEdit1.setText(nine1);
                        } else {
                            if (month == 9) {
                                mEdit.setText(year + " " + "0" + 9 + " " + day);
                                nine = day + " " + "Sep" + " " + year;
                                mEdit1.setText(nine);
                            }
                        }

                        if (month == 10 && day < 10) {
                            mEdit.setText(year + " " + 10 + " " + "0" + day);
                            ten1 = "0" + day + " " + "Oct" + " " + year;
                            mEdit1.setText(ten1);
                        } else {
                            if (month == 10) {
                                mEdit.setText(year + " " + 10 + " " + day);
                                ten = day + " " + "Oct" + " " + year;
                                mEdit1.setText(ten);
                            }
                        }

                        if (month == 11 && day < 10) {
                            mEdit.setText(year + " " + 11 + " " + "0" + day);
                            eleven1 = "0" + day + " " + "Nov" + " " + year;
                            mEdit1.setText(eleven1);
                        } else {
                            if (month == 11) {
                                mEdit.setText(year + " " + 11 + " " + day);
                                eleven = day + " " + "Nov" + " " + year;
                                mEdit1.setText(eleven);
                            }
                        }

                        if (month == 12 && day < 10) {
                            mEdit.setText(year + " " + 12 + " " + "0" + day);
                            twelve1 = "0" + day + " " + "Dec" + " " + year;
                            mEdit1.setText(twelve1);
                        } else {
                            if (month == 12) {
                                mEdit.setText(year + " " + 12 + " " + day);
                                twelve = day + " " + "Dec" + " " + year;
                                mEdit1.setText(twelve);
                            }
                        }

                    }

                });


                editText22_df.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Calendar now = Calendar.getInstance();
                        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                                datePickerListener,now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                        );

                        dpd.show(Micro_Inventory_Indent_Vendor_History.this.getFragmentManager(), "Datepickerdialog");
                    }

                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener datePickerListener
                            = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog, int selectedYear, int selectedMonth, int selectedDay) {
                            year = selectedYear;
                            month = selectedMonth;
                            day = selectedDay;

                            // set selected date into textview
                            populateSetDate(year, month + 1, day);
                        }
                    };

                    public void populateSetDate(int year, int month, int day) {
                        TextView mEdit = (TextView) dialog_filter.findViewById(R.id.editText2);
                        TextView mEdit1  = (TextView) dialog_filter.findViewById(R.id.editText22);
                        if (month == 1 && day < 10) {
                            mEdit.setText(year + " " + "0" + 1 + " " + "0" + day);
                            onee1 = "0" + day + " " + "Jan" + " " + year;
                            mEdit1.setText(onee1);
                        } else {
                            if (month == 1) {
                                mEdit.setText(year + " " + "0" + 1 + " " + day);
                                onee = day + " " + "Jan" + " " + year;
                                mEdit1.setText(onee);
                            }
                        }

                        if (month == 2 && day < 10) {
                            mEdit.setText(year + " " + "0" + 2 + " " + "0" + day);
                            two1 = "0" + day + " " + "Feb" + " " + year;
                            mEdit1.setText(two1);
                        } else {
                            if (month == 2) {
                                mEdit.setText(year + " " + "0" + 2 + " " + day);
                                two = day + " " + "Feb" + " " + year;
                                mEdit1.setText(two);
                            }
                        }

                        if (month == 3 && day < 10) {
                            mEdit.setText(year + " " + "0" + 3 + " " + "0" + day);
                            three1 = "0" + day + " " + "Mar" + " " + year;
                            mEdit1.setText(three1);
                        } else {
                            if (month == 3) {
                                mEdit.setText(year + " " + "0" + 3 + " " + day);
                                three = day + " " + "Mar" + " " + year;
                                mEdit1.setText(three);
                            }
                        }

                        if (month == 4 && day < 10) {
                            mEdit.setText(year + " " + "0" + 4 + " " + "0" + day);
                            four1 = "0" + day + " " + "Apr" + " " + year;
                            mEdit1.setText(four1);
                        } else {
                            if (month == 4) {
                                mEdit.setText(year + " " + "0" + 4 + " " + day);
                                four = day + " " + "Apr" + " " + year;
                                mEdit1.setText(four);
                            }
                        }

                        if (month == 5 && day < 10) {
                            mEdit.setText(year + " " + "0" + 5 + " " + "0" + day);
                            five1 = "0" + day + " " + "May" + " " + year;
                            mEdit1.setText(five1);
                        } else {
                            if (month == 5) {
                                mEdit.setText(year + " " + "0" + 5 + " " + day);
                                five = day + " " + "May" + " " + year;
                                mEdit1.setText(five);
                            }
                        }

                        if (month == 6 && day < 10) {
                            mEdit.setText(year + " " + "0" + 6 + " " + "0" + day);
                            six1 = "0" + day + " " + "Jun" + " " + year;
                            mEdit1.setText(six1);
                        } else {
                            if (month == 6) {
                                mEdit.setText(year + " " + "0" + 6 + " " + day);
                                six = day + " " + "Jun" + " " + year;
                                mEdit1.setText(six);
                            }
                        }

                        if (month == 7 && day < 10) {
                            mEdit.setText(year + " " + "0" + 7 + " " + "0" + day);
                            seven1 = "0" + day + " " + "Jul" + " " + year;
                            mEdit1.setText(seven1);
                        } else {
                            if (month == 7) {
                                mEdit.setText(year + " " + "0" + 7 + " " + day);
                                seven = day + " " + "Jul" + " " + year;
                                mEdit1.setText(seven);
                            }
                        }

                        if (month == 8 && day < 10) {
                            mEdit.setText(year + " " + "0" + 8 + " " + "0" + day);
                            eight1 = "0" + day + " " + "Aug" + " " + year;
                            mEdit1.setText(eight1);
                        } else {
                            if (month == 8) {
                                mEdit.setText(year + " " + "0" + 8 + " " + day);
                                eight = day + " " + "Aug" + " " + year;
                                mEdit1.setText(eight);
                            }
                        }

                        if (month == 9 && day < 10) {
                            mEdit.setText(year + " " + "0" + 9 + " " + "0" + day);
                            nine1 = "0" + day + " " + "Sep" + " " + year;
                            mEdit1.setText(nine1);
                        } else {
                            if (month == 9) {
                                mEdit.setText(year + " " + "0" + 9 + " " + day);
                                nine = day + " " + "Sep" + " " + year;
                                mEdit1.setText(nine);
                            }
                        }

                        if (month == 10 && day < 10) {
                            mEdit.setText(year + " " + 10 + " " + "0" + day);
                            ten1 = "0" + day + " " + "Oct" + " " + year;
                            mEdit1.setText(ten1);
                        } else {
                            if (month == 10) {
                                mEdit.setText(year + " " + 10 + " " + day);
                                ten = day + " " + "Oct" + " " + year;
                                mEdit1.setText(ten);
                            }
                        }

                        if (month == 11 && day < 10) {
                            mEdit.setText(year + " " + 11 + " " + "0" + day);
                            eleven1 = "0" + day + " " + "Nov" + " " + year;
                            mEdit1.setText(eleven1);
                        } else {
                            if (month == 11) {
                                mEdit.setText(year + " " + 11 + " " + day);
                                eleven = day + " " + "Nov" + " " + year;
                                mEdit1.setText(eleven);
                            }
                        }

                        if (month == 12 && day < 10) {
                            mEdit.setText(year + " " + 12 + " " + "0" + day);
                            twelve1 = "0" + day + " " + "Dec" + " " + year;
                            mEdit1.setText(twelve1);
                        } else {
                            if (month == 12) {
                                mEdit.setText(year + " " + 12 + " " + day);
                                twelve = day + " " + "Dec" + " " + year;
                                mEdit1.setText(twelve);
                            }
                        }

                    }
                });

                editText_from_day_visible_df.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(Micro_Inventory_Indent_Vendor_History.this, R.style.timepicker_date_dialog, timePickerListener_open_df, hour, minute,
                                false);

                        timePickerDialog.show();
                    }
                });

                editText_to_day_visible_df.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(Micro_Inventory_Indent_Vendor_History.this, R.style.timepicker_date_dialog, timePickerListener_close_df, hour, minute,
                                false);

                        timePickerDialog.show();
                    }
                });


                final RadioGroup filter_selection = (RadioGroup) dialog_filter.findViewById(R.id.filter_selection);

                final RadioButton working_hours = (RadioButton) dialog_filter.findViewById(R.id.working_hours);
                final RadioButton custom_time = (RadioButton) dialog_filter.findViewById(R.id.custom_time);
                final RadioButton all_time = (RadioButton) dialog_filter.findViewById(R.id.all_time);
                final RadioButton pending_list = (RadioButton) dialog_filter.findViewById(R.id.pending_list);
                final RadioButton paid_list = (RadioButton) dialog_filter.findViewById(R.id.paid_list);
                final RadioButton other_dues = (RadioButton) dialog_filter.findViewById(R.id.other_dues);

                working_hours.setChecked(true);

                if (!working_hours.isChecked() && !custom_time.isChecked() && !all_time.isChecked() && !pending_list.isChecked() && !paid_list.isChecked() && !other_dues.isChecked()){
                    working_hours.setChecked(true);
                }

                if (selec.toString().equals("1")){
                    working_hours.setChecked(true);
                }
                if (selec.toString().equals("2")){
                    custom_time.setChecked(true);
                }
                if (selec.toString().equals("3")){
                    all_time.setChecked(true);
                }
                if (selec.toString().equals("4")){
                    paid_list.setChecked(true);
                }
                if (selec.toString().equals("5")){
                    paid_list.setChecked(true);
                }
                if (selec.toString().equals("6")){
                    other_dues.setChecked(true);
                }


                Button quantityapply_filter = (Button) dialog_filter.findViewById(R.id.quantityapply_filter);
                quantityapply_filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (working_hours.isChecked()){
                            editText1.setText(currentDateandTime1);
                            editText2.setText(currentDateandTime1);


                            editText11.setText(currentDateandTime2);
                            editText22.setText(currentDateandTime2);


                            Cursor time_get = db.rawQuery("SELECT * FROM Working_hours", null);
                            if (time_get.moveToFirst()){
                                String two = time_get.getString(2);
                                String four = time_get.getString(4);
                                String five = time_get.getString(5);
                                String six = time_get.getString(6);
                                String three = time_get.getString(3);

                                editText_from_day_hide.setText(five);
                                editText_from_day_visible.setText(two);
                                editText_to_day_hide.setText(six);
                                editText_to_day_visible.setText(four);


                                if (three.toString().equals("Tomorrow")) {

                                    try {
                                        String string1 = five;
                                        Date time1 = new SimpleDateFormat("HH:mm").parse(string1);
                                        Calendar calendar1 = Calendar.getInstance();
                                        calendar1.add(Calendar.DAY_OF_YEAR, 0);
                                        calendar1.setTime(time1);

                                        String string2 = "23:59";
                                        Date time2 = new SimpleDateFormat("HH:mm").parse(string2);
                                        Calendar calendar2 = Calendar.getInstance();
                                        calendar2.setTime(time2);

                                        String string1_new = "00:00";
                                        Date time1_new = new SimpleDateFormat("HH:mm").parse(string1_new);
                                        Calendar calendar1_new = Calendar.getInstance();
                                        calendar1_new.setTime(time1_new);
                                        calendar1_new.add(Calendar.DATE, 1);

                                        String string2_new = five;
                                        Date time2_new = new SimpleDateFormat("HH:mm").parse(string2_new);
                                        Calendar calendar2_new = Calendar.getInstance();
                                        calendar2_new.setTime(time2_new);
                                        calendar2_new.add(Calendar.DATE, 1);


                                        String someRandomTime = time_hide;
                                        Date d = new SimpleDateFormat("HH:mm").parse(someRandomTime);
                                        Calendar calendar3 = Calendar.getInstance();
                                        calendar3.setTime(d);
//                    calendar3.add(Calendar.DATE, 1);

                                        String someRandomTime_new = time_hide;
                                        Date d_new = new SimpleDateFormat("HH:mm").parse(someRandomTime_new);
                                        Calendar calendar3_new = Calendar.getInstance();
                                        calendar3_new.setTime(d_new);
                                        calendar3_new.add(Calendar.DATE, 1);

                                        Date x = calendar3.getTime();
                                        Date x_new = calendar3_new.getTime();
                                        if ((x.after(calendar1.getTime()) && x.before(calendar2.getTime())) || (x_new.after(calendar1_new.getTime()) && x_new.before(calendar2_new.getTime()))) {
                                            //checkes whether the current time is between 14:49:00 and 20:11:13.
                                            System.out.println(true);
//                    Toast.makeText(getActivity(),"yes", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity(),"time is "+time_hide, Toast.LENGTH_SHORT).show();


                                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                                            String yesterday_visible = dateFormat.format(yesterday);

                                            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                                            String yesterday_hide = dateFormat1.format(yesterday);


                                            SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy");
                                            String tomorrow_visible = dateFormat2.format(tomorrow);

                                            SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyyMMdd");
                                            String tomorrow_hide = dateFormat3.format(tomorrow);

                                            if ((x.after(calendar1.getTime()) && x.before(calendar2.getTime()))) {
                                                editText11.setText(currentDateandTime2);
                                                editText22.setText(tomorrow_visible);

                                                editText1.setText(currentDateandTime1);
                                                editText2.setText(tomorrow_hide);
                                            }
                                            if ((x_new.after(calendar1_new.getTime()) && x_new.before(calendar2_new.getTime()))) {
                                                editText11.setText(yesterday_visible);
                                                editText22.setText(currentDateandTime2);

                                                editText1.setText(yesterday_hide);
                                                editText2.setText(currentDateandTime1);
                                            }


                                        } else {

                                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                                            String tomorrow_visible = dateFormat.format(tomorrow);

                                            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                                            String tomorrow_hide = dateFormat1.format(tomorrow);

                                            editText11.setText(currentDateandTime2);
                                            editText22.setText(tomorrow_visible);

                                            editText1.setText(currentDateandTime1);
                                            editText2.setText(tomorrow_hide);

                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }


                                if (five.toString().contains(":")){
                                    five = five.replace(":", "");
                                }

                                if (six.toString().contains(":")){
                                    six = six.replace(":", "");
                                }

                                String r1, r2;
                                r1 = editText1.getText().toString();
                                r2 = editText2.getText().toString();
                                if (r1.toString().contains(" ")){
                                    r1 = r1.replace(" ", "");
                                }
                                if (r2.toString().contains(" ")){
                                    r2 = r2.replace(" ", "");
                                }

                                editText1_filter.setText(r1+""+five);
                                editText2_filter.setText(r2+""+six);

                            }
                            time_get.close();

                            list_history.setAdapter(null);

                            Cursor cursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' GROUP BY datetimee_new_from", null);
                            String[] fromFieldNames = {"from_date", "from_time", "vendor_name", "invoice", "total_bill_amount", "total_pay", "pending", "pending", "pending", "pending"};
                            int[] toViewsID = {R.id.date_his, R.id.time_his, R.id.supplier_his, R.id.invoice_his, R.id.total_amount_his, R.id.paid_his, R.id.pending_his, R.id.inn, R.id.inn1, R.id.inn2};
                            ddataAdapterr = new SimpleCursorAdapter(Micro_Inventory_Indent_Vendor_History.this, R.layout.inventory_indent_history_listview, cursor, fromFieldNames, toViewsID, 0);
//                            list_history.setAdapter(ddataAdapterr);
                            ddataAdapterr.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                                @Override
                                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                                    if (view.getId() == R.id.inn || view.getId() == R.id.inn1 || view.getId() == R.id.inn2) {
                                        final String tadl_id = cursor_country1.getString(cursor_country1.getColumnIndex("country"));
                                        TextView dateTextView = (TextView) view;
                                        if (tadl_id.toString().equals("India")){
                                            dateTextView.setText(insert1_cc);
                                        }else {
                                            dateTextView.setText(insert1_cc);
                                        }
                                        return true;
                                    }
                                    return false;
                                }
                            });
                            list_history.setAdapter(ddataAdapterr);

                            searchView.addTextChangedListener(new TextWatcher() {

                                public void afterTextChanged(Editable s) {
                                }

                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    ddataAdapterr.getFilter().filter(s.toString());
                                }
                            });

                            ddataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
                                public Cursor runQuery(CharSequence constraint) {
                                    return fetchCountriesByName1_workinghours(constraint.toString());
                                }
                            });

                            TextView paid_his_filter = (TextView) findViewById(R.id.paid_his_filter);
                            Cursor cursorx1 = db.rawQuery("SELECT SUM(pay) FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"'", null);
                            if (cursorx1.moveToFirst()){
                                float cv = cursorx1.getFloat(0);
                                paid_his_filter.setText(String.valueOf(cv));
                            }

                            TextView total_his_filter = (TextView) findViewById(R.id.total_his_filter);

                            float co1 = 0;
                            Cursor cursor1 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' GROUP BY datetimee_new_from", null);
                            if (cursor1.moveToFirst()){
                                do {
                                    String dateti = cursor1.getString(13);
//                                    Toast.makeText(Inventory_Indent_Vendor_History.this, "1 "+dateti, Toast.LENGTH_SHORT).show();

                                    Cursor cursor2 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from = '"+dateti+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' ", null);
                                    if (cursor2.moveToFirst()){
                                        String tot = cursor2.getString(9);
//                                        Toast.makeText(Inventory_Indent_Vendor_History.this, "2 "+tot, Toast.LENGTH_SHORT).show();
                                        co1 = co1+Float.parseFloat(tot);

                                    }
                                }while (cursor1.moveToNext());
                            }
                            total_his_filter.setText(String.valueOf(co1));

                            float cvb = Float.parseFloat(total_his_filter.getText().toString()) - Float.parseFloat(paid_his_filter.getText().toString());

                            TextView pending_his_filter = (TextView) findViewById(R.id.pending_his_filter);
                            pending_his_filter.setText(String.valueOf(cvb));

                            dialog_filter.dismiss();
                            selec = "1";

                        }
                        if (custom_time.isChecked()) {
                            String r1 = editText1_df.getText().toString();
                            String r2 = editText2_df.getText().toString();
                            if (r1.toString().contains(" ")) {
                                r1 = r1.replace(" ", "");
                            }
                            if (r2.toString().contains(" ")) {
                                r2 = r2.replace(" ", "");
                            }

                            editText1_filter_df.setText(r1 + "" + editText_from_day_hide_df.getText().toString());
                            editText2_filter_df.setText(r2 + "" + editText_to_day_hide_df.getText().toString());

//                            Toast.makeText(Inventory_Indent_Vendor_History.this, "3 " + editText1_filter_df.getText().toString(), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(Inventory_Indent_Vendor_History.this, "4 " + editText2_filter_df.getText().toString(), Toast.LENGTH_SHORT).show();



                            editText1.setText(editText1_df.getText().toString());
                            editText11.setText(editText11_df.getText().toString());

                            editText2.setText(editText2_df.getText().toString());
                            editText22.setText(editText22_df.getText().toString());


                            editText_from_day_hide.setText(editText_from_day_hide_df.getText().toString());
                            editText_from_day_visible.setText(editText_from_day_visible_df.getText().toString());

                            editText_to_day_hide.setText(editText_to_day_hide_df.getText().toString());
                            editText_to_day_visible.setText(editText_to_day_visible_df.getText().toString());

                            list_history.setAdapter(null);

                            Cursor cursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND datetimee_new_from >= '"+editText1_filter_df.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter_df.getText().toString()+"' GROUP BY datetimee_new_from", null);
                            String[] fromFieldNames = {"from_date", "from_time", "vendor_name", "invoice", "total_bill_amount", "total_pay", "pending", "pending", "pending", "pending"};
                            int[] toViewsID = {R.id.date_his, R.id.time_his, R.id.supplier_his, R.id.invoice_his, R.id.total_amount_his, R.id.paid_his, R.id.pending_his, R.id.inn, R.id.inn1, R.id.inn2};
                            ddataAdapterr = new SimpleCursorAdapter(Micro_Inventory_Indent_Vendor_History.this, R.layout.inventory_indent_history_listview, cursor, fromFieldNames, toViewsID, 0);
//                            list_history.setAdapter(ddataAdapterr);
                            ddataAdapterr.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                                @Override
                                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                                    if (view.getId() == R.id.inn || view.getId() == R.id.inn1 || view.getId() == R.id.inn2) {
                                        final String tadl_id = cursor_country1.getString(cursor_country1.getColumnIndex("country"));
                                        TextView dateTextView = (TextView) view;
                                        if (tadl_id.toString().equals("India")){
                                            dateTextView.setText(insert1_cc);
                                        }else {
                                            dateTextView.setText(insert1_cc);
                                        }
                                        return true;
                                    }
                                    return false;
                                }
                            });
                            list_history.setAdapter(ddataAdapterr);

                            searchView.addTextChangedListener(new TextWatcher() {

                                public void afterTextChanged(Editable s) {
                                }

                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    ddataAdapterr.getFilter().filter(s.toString());
                                }
                            });

                            ddataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
                                public Cursor runQuery(CharSequence constraint) {
                                    return fetchCountriesByName1_customtime(constraint.toString());
                                }
                            });

                            TextView paid_his_filter = (TextView) findViewById(R.id.paid_his_filter);
                            Cursor cursorx1 = db.rawQuery("SELECT SUM(pay) FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter_df.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter_df.getText().toString()+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"'", null);
                            if (cursorx1.moveToFirst()){
                                float cv = cursorx1.getFloat(0);
                                paid_his_filter.setText(String.valueOf(cv));
                            }

                            TextView total_his_filter = (TextView) findViewById(R.id.total_his_filter);

                            float co1 = 0;
                            Cursor cursor1 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter_df.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter_df.getText().toString()+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' GROUP BY datetimee_new_from", null);
                            if (cursor1.moveToFirst()){
                                do {
                                    String dateti = cursor1.getString(13);
//                                    Toast.makeText(Inventory_Indent_Vendor_History.this, "1 "+dateti, Toast.LENGTH_SHORT).show();

                                    Cursor cursor2 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from = '"+dateti+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' ", null);
                                    if (cursor2.moveToFirst()){
                                        String tot = cursor2.getString(9);
//                                        Toast.makeText(Inventory_Indent_Vendor_History.this, "2 "+tot, Toast.LENGTH_SHORT).show();
                                        co1 = co1+Float.parseFloat(tot);

                                    }
                                }while (cursor1.moveToNext());
                            }
                            total_his_filter.setText(String.valueOf(co1));

                            float cvb = Float.parseFloat(total_his_filter.getText().toString()) - Float.parseFloat(paid_his_filter.getText().toString());

                            TextView pending_his_filter = (TextView) findViewById(R.id.pending_his_filter);
                            pending_his_filter.setText(String.valueOf(cvb));

                            dialog_filter.dismiss();
                            selec = "2";

                        }

                        if (all_time.isChecked()){

                            Cursor cursor1 = db.rawQuery("SELECT * FROM Ingredient_sold_details GROUP BY datetimee_new_from", null);
                            if (cursor1.moveToFirst()){
                                String dat = cursor1.getString(11);
                                String tim = cursor1.getString(10);

//                                editText1.setText(editText1_df.getText().toString());
                                editText11.setText(dat);

//                                editText2.setText(editText2_df.getText().toString());
//                                editText22.setText(editText22_df.getText().toString());

//                                editText_from_day_hide.setText(editText_from_day_hide_df.getText().toString());
                                editText_from_day_visible.setText(tim);

//                                editText_to_day_hide.setText(editText_to_day_hide_df.getText().toString());
//                                editText_to_day_visible.setText(editText_to_day_visible_df.getText().toString());

                            }

                            Cursor cursor2 = db.rawQuery("SELECT * FROM Ingredient_sold_details GROUP BY datetimee_new_from", null);
                            if (cursor2.moveToLast()){
                                String dat = cursor2.getString(11);
                                String tim = cursor2.getString(10);

//                                editText1.setText(editText1_df.getText().toString());
//                                editText11.setText(dat);

//                                editText2.setText(editText2_df.getText().toString());
                                editText22.setText(dat);

//                                editText_from_day_hide.setText(editText_from_day_hide_df.getText().toString());
//                                editText_from_day_visible.setText(tim);

//                                editText_to_day_hide.setText(editText_to_day_hide_df.getText().toString());
                                editText_to_day_visible.setText(tim);

                            }

                            list_history.setAdapter(null);

                            Cursor cursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' GROUP BY datetimee_new_from", null);
                            String[] fromFieldNames = {"from_date", "from_time", "vendor_name", "invoice", "total_bill_amount", "total_pay", "pending", "pending", "pending", "pending"};
                            int[] toViewsID = {R.id.date_his, R.id.time_his, R.id.supplier_his, R.id.invoice_his, R.id.total_amount_his, R.id.paid_his, R.id.pending_his, R.id.inn, R.id.inn1, R.id.inn2};
                            ddataAdapterr = new SimpleCursorAdapter(Micro_Inventory_Indent_Vendor_History.this, R.layout.inventory_indent_history_listview, cursor, fromFieldNames, toViewsID, 0);
//                            list_history.setAdapter(ddataAdapterr);
                            ddataAdapterr.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                                @Override
                                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                                    if (view.getId() == R.id.inn || view.getId() == R.id.inn1 || view.getId() == R.id.inn2) {
                                        final String tadl_id = cursor_country1.getString(cursor_country1.getColumnIndex("country"));
                                        TextView dateTextView = (TextView) view;
                                        if (tadl_id.toString().equals("India")){
                                            dateTextView.setText(insert1_cc);
                                        }else {
                                            dateTextView.setText(insert1_cc);
                                        }
                                        return true;
                                    }
                                    return false;
                                }
                            });
                            list_history.setAdapter(ddataAdapterr);

                            searchView.addTextChangedListener(new TextWatcher() {

                                public void afterTextChanged(Editable s) {
                                }

                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    ddataAdapterr.getFilter().filter(s.toString());
                                }
                            });

                            ddataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
                                public Cursor runQuery(CharSequence constraint) {
                                    return fetchCountriesByName1_alltime(constraint.toString());
                                }
                            });


                            TextView paid_his_filter = (TextView) findViewById(R.id.paid_his_filter);
                            Cursor cursorx1 = db.rawQuery("SELECT SUM(pay) FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"'", null);
                            if (cursorx1.moveToFirst()){
                                float cv = cursorx1.getFloat(0);
                                paid_his_filter.setText(String.valueOf(cv));
                            }

                            TextView total_his_filter = (TextView) findViewById(R.id.total_his_filter);
//        }

                            float co1 = 0;
                            Cursor cursorq1 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' GROUP BY datetimee_new_from", null);
                            if (cursorq1.moveToFirst()){
                                do {
                                    String dateti = cursorq1.getString(13);
//                                    Toast.makeText(Inventory_Indent_Vendor_History.this, "1 "+dateti, Toast.LENGTH_SHORT).show();

                                    Cursor cursorq2 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from = '"+dateti+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' ", null);
                                    if (cursorq2.moveToFirst()){
                                        String tot = cursorq2.getString(9);
//                                        Toast.makeText(Inventory_Indent_Vendor_History.this, "2 "+tot, Toast.LENGTH_SHORT).show();
                                        co1 = co1+Float.parseFloat(tot);

                                    }
                                }while (cursorq1.moveToNext());
                            }
                            total_his_filter.setText(String.valueOf(co1));

                            float cvb = Float.parseFloat(total_his_filter.getText().toString()) - Float.parseFloat(paid_his_filter.getText().toString());

                            TextView pending_his_filter = (TextView) findViewById(R.id.pending_his_filter);
                            pending_his_filter.setText(String.valueOf(cvb));

                            dialog_filter.dismiss();
                            selec = "3";

                        }

                        if (pending_list.isChecked()){

                            Cursor cursor21 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE pending = '0.0' OR pending = '0'", null);
                            if (cursor21.moveToFirst()){
                                do {
                                    String pend = cursor21.getString(16);
                                    String dateti = cursor21.getString(13);
                                    if (pend.toString().equals("0.0") || pend.toString().equals("0")){
                                        Cursor cursor22 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from = '"+dateti+"'", null);
                                        if (cursor22.moveToFirst()){
                                            do {
                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("pending", "0.0");
                                                String where1 = "datetimee_new_from = '"+dateti+"' ";

                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_sold_details");
                                                getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                                                resultUri = new Uri.Builder()
                                                        .scheme("content")
                                                        .authority(StubProviderApp.AUTHORITY)
                                                        .path("Menulogin_checking")
                                                        .appendQueryParameter("operation", "update")
                                                        .appendQueryParameter("datetimee_new_from", dateti)
                                                        .build();
                                                getContentResolver().notifyChange(resultUri, null);

//                                                db.update("Ingredient_sold_details", contentValues, where1, new String[]{});
                                            }while (cursor22.moveToNext());
                                        }
                                    }
                                }while (cursor21.moveToNext());

                            }

                            list_history.setAdapter(null);


                            Cursor cursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND pending != '0.0' GROUP BY datetimee_new_from", null);
                            String[] fromFieldNames = {"from_date", "from_time", "vendor_name", "invoice", "total_bill_amount", "total_pay", "pending", "pending", "pending", "pending"};
                            int[] toViewsID = {R.id.date_his, R.id.time_his, R.id.supplier_his, R.id.invoice_his, R.id.total_amount_his, R.id.paid_his, R.id.pending_his, R.id.inn, R.id.inn1, R.id.inn2};
                            ddataAdapterr = new SimpleCursorAdapter(Micro_Inventory_Indent_Vendor_History.this, R.layout.inventory_indent_history_listview, cursor, fromFieldNames, toViewsID, 0);
//                            list_history.setAdapter(ddataAdapterr);
                            ddataAdapterr.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                                @Override
                                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                                    if (view.getId() == R.id.inn || view.getId() == R.id.inn1 || view.getId() == R.id.inn2) {
                                        final String tadl_id = cursor_country1.getString(cursor_country1.getColumnIndex("country"));
                                        TextView dateTextView = (TextView) view;
                                        if (tadl_id.toString().equals("India")){
                                            dateTextView.setText(insert1_cc);
                                        }else {
                                            dateTextView.setText(insert1_cc);
                                        }
                                        return true;
                                    }
                                    return false;
                                }
                            });
                            list_history.setAdapter(ddataAdapterr);

                            searchView.addTextChangedListener(new TextWatcher() {

                                public void afterTextChanged(Editable s) {
                                }

                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    ddataAdapterr.getFilter().filter(s.toString());
                                }
                            });

                            ddataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
                                public Cursor runQuery(CharSequence constraint) {
                                    return fetchCountriesByName1_pending(constraint.toString());
                                }
                            });

                            TextView paid_his_filter = (TextView) findViewById(R.id.paid_his_filter);
                            Cursor cursorx1 = db.rawQuery("SELECT SUM(pay) FROM Ingredient_sold_details WHERE  vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND pending != '0.0'", null);
                            if (cursorx1.moveToFirst()){
                                float cv = cursorx1.getFloat(0);
                                paid_his_filter.setText(String.valueOf(cv));
                            }

                            TextView total_his_filter = (TextView) findViewById(R.id.total_his_filter);

                            float co1 = 0;
                            Cursor cursor1 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND pending != '0.0' GROUP BY datetimee_new_from", null);
                            if (cursor1.moveToFirst()){
                                do {
                                    String dateti = cursor1.getString(13);
//                                    Toast.makeText(Inventory_Indent_Vendor_History.this, "1 "+dateti, Toast.LENGTH_SHORT).show();

                                    Cursor cursor2 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from = '"+dateti+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND pending != '0.0'", null);
                                    if (cursor2.moveToFirst()){
                                        String tot = cursor2.getString(9);
//                                        Toast.makeText(Inventory_Indent_Vendor_History.this, "2 "+tot, Toast.LENGTH_SHORT).show();
                                        co1 = co1+Float.parseFloat(tot);

                                    }
                                }while (cursor1.moveToNext());
                            }
                            total_his_filter.setText(String.valueOf(co1));

                            float cvb = Float.parseFloat(total_his_filter.getText().toString()) - Float.parseFloat(paid_his_filter.getText().toString());

                            TextView pending_his_filter = (TextView) findViewById(R.id.pending_his_filter);
                            pending_his_filter.setText(String.valueOf(cvb));

                            dialog_filter.dismiss();
                            selec = "4";
                        }

                        if (paid_list.isChecked()){

                            list_history.setAdapter(null);

                            Cursor cursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND pending = '0' OR pending = '0.0' GROUP BY datetimee_new_from", null);
                            String[] fromFieldNames = {"from_date", "from_time", "vendor_name", "invoice", "total_bill_amount", "total_pay", "pending", "pending", "pending", "pending"};
                            int[] toViewsID = {R.id.date_his, R.id.time_his, R.id.supplier_his, R.id.invoice_his, R.id.total_amount_his, R.id.paid_his, R.id.pending_his, R.id.inn, R.id.inn1, R.id.inn2};
                            ddataAdapterr = new SimpleCursorAdapter(Micro_Inventory_Indent_Vendor_History.this, R.layout.inventory_indent_history_listview, cursor, fromFieldNames, toViewsID, 0);
//                            list_history.setAdapter(ddataAdapterr);
                            ddataAdapterr.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                                @Override
                                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                                    if (view.getId() == R.id.inn || view.getId() == R.id.inn1 || view.getId() == R.id.inn2) {
                                        final String tadl_id = cursor_country1.getString(cursor_country1.getColumnIndex("country"));
                                        TextView dateTextView = (TextView) view;
                                        if (tadl_id.toString().equals("India")){
                                            dateTextView.setText(insert1_cc);
                                        }else {
                                            dateTextView.setText(insert1_cc);
                                        }
                                        return true;
                                    }
                                    return false;
                                }
                            });
                            list_history.setAdapter(ddataAdapterr);

                            searchView.addTextChangedListener(new TextWatcher() {

                                public void afterTextChanged(Editable s) {
                                }

                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    ddataAdapterr.getFilter().filter(s.toString());
                                }
                            });

                            ddataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
                                public Cursor runQuery(CharSequence constraint) {
                                    return fetchCountriesByName1_paidlist(constraint.toString());
                                }
                            });

                            TextView paid_his_filter = (TextView) findViewById(R.id.paid_his_filter);
                            Cursor cursorx1 = db.rawQuery("SELECT SUM(pay) FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND pending = '0' OR pending = '0.0'", null);
                            if (cursorx1.moveToFirst()){
                                float cv = cursorx1.getFloat(0);
                                paid_his_filter.setText(String.valueOf(cv));
                            }

                            TextView total_his_filter = (TextView) findViewById(R.id.total_his_filter);

                            float co1 = 0;
                            Cursor cursorq1 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND pending = '0' OR pending = '0.0' GROUP BY datetimee_new_from", null);
                            if (cursorq1.moveToFirst()){
                                do {
                                    String dateti = cursorq1.getString(13);
//                                    Toast.makeText(Inventory_Indent_Vendor_History.this, "1 "+dateti, Toast.LENGTH_SHORT).show();

                                    Cursor cursorq2 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from = '"+dateti+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND pending = '0' OR pending = '0.0' ", null);
                                    if (cursorq2.moveToFirst()){
                                        String tot = cursorq2.getString(9);
//                                        Toast.makeText(Inventory_Indent_Vendor_History.this, "2 "+tot, Toast.LENGTH_SHORT).show();
                                        co1 = co1+Float.parseFloat(tot);

                                    }
                                }while (cursorq1.moveToNext());
                            }
                            total_his_filter.setText(String.valueOf(co1));

                            float cvb = Float.parseFloat(total_his_filter.getText().toString()) - Float.parseFloat(paid_his_filter.getText().toString());

                            TextView pending_his_filter = (TextView) findViewById(R.id.pending_his_filter);
                            pending_his_filter.setText(String.valueOf(cvb));

                            dialog_filter.dismiss();
                            selec = "5";

                        }
                        if (other_dues.isChecked()){

                            list_history.setAdapter(null);

                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                            final String currentDateandTime1 = sdf2.format(new Date());



                            Cursor cursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND datetimee_new_due < '"+currentDateandTime1+"' AND pending != '0' GROUP BY datetimee_new_from", null);
                            String[] fromFieldNames = {"from_date", "from_time", "vendor_name", "invoice", "total_bill_amount", "total_pay", "pending", "pending", "pending", "pending"};
                            int[] toViewsID = {R.id.date_his, R.id.time_his, R.id.supplier_his, R.id.invoice_his, R.id.total_amount_his, R.id.paid_his, R.id.pending_his, R.id.inn, R.id.inn1, R.id.inn2};
                            ddataAdapterr = new SimpleCursorAdapter(Micro_Inventory_Indent_Vendor_History.this, R.layout.inventory_indent_history_listview, cursor, fromFieldNames, toViewsID, 0);
//                            list_history.setAdapter(ddataAdapterr);
                            ddataAdapterr.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                                @Override
                                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                                    if (view.getId() == R.id.inn || view.getId() == R.id.inn1 || view.getId() == R.id.inn2) {
                                        final String tadl_id = cursor_country1.getString(cursor_country1.getColumnIndex("country"));
                                        TextView dateTextView = (TextView) view;
                                        if (tadl_id.toString().equals("India")){
                                            dateTextView.setText(insert1_cc);
                                        }else {
                                            dateTextView.setText(insert1_cc);
                                        }
                                        return true;
                                    }
                                    return false;
                                }
                            });
                            list_history.setAdapter(ddataAdapterr);

                            searchView.addTextChangedListener(new TextWatcher() {

                                public void afterTextChanged(Editable s) {
                                }

                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    ddataAdapterr.getFilter().filter(s.toString());
                                }
                            });

                            ddataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
                                public Cursor runQuery(CharSequence constraint) {
                                    return fetchCountriesByName1_otherdues(constraint.toString(), currentDateandTime1);
                                }
                            });

                            TextView paid_his_filter = (TextView) findViewById(R.id.paid_his_filter);
                            Cursor cursorx1 = db.rawQuery("SELECT SUM(pay) FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND datetimee_new_due < '"+currentDateandTime1+"' AND pending != '0'", null);
                            if (cursorx1.moveToFirst()){
                                float cv = cursorx1.getFloat(0);
                                paid_his_filter.setText(String.valueOf(cv));
                            }

                            TextView total_his_filter = (TextView) findViewById(R.id.total_his_filter);

                            float co1 = 0;
                            Cursor cursorq1 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND datetimee_new_due < '"+currentDateandTime1+"' AND pending != '0' GROUP BY datetimee_new_from", null);
                            if (cursorq1.moveToFirst()){
                                do {
                                    String dateti = cursorq1.getString(13);
//                                    Toast.makeText(Inventory_Indent_Vendor_History.this, "1 "+dateti, Toast.LENGTH_SHORT).show();

                                    Cursor cursorq2 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from = '"+dateti+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND datetimee_new_due < '"+currentDateandTime1+"' AND pending != '0'", null);
                                    if (cursorq2.moveToFirst()){
                                        String tot = cursorq2.getString(9);
//                                        Toast.makeText(Inventory_Indent_Vendor_History.this, "2 "+tot, Toast.LENGTH_SHORT).show();
                                        co1 = co1+Float.parseFloat(tot);

                                    }
                                }while (cursorq1.moveToNext());
                            }
                            total_his_filter.setText(String.valueOf(co1));

                            float cvb = Float.parseFloat(total_his_filter.getText().toString()) - Float.parseFloat(paid_his_filter.getText().toString());

                            TextView pending_his_filter = (TextView) findViewById(R.id.pending_his_filter);
                            pending_his_filter.setText(String.valueOf(cvb));

                            dialog_filter.dismiss();
                            selec = "6";
                        }

                    }
                });



            }
        });


        TextView paid_his_filter = (TextView) findViewById(R.id.paid_his_filter);
        Cursor cursorx1 = db.rawQuery("SELECT SUM(pay) FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"'", null);
        if (cursorx1.moveToFirst()){
            float cv = cursorx1.getFloat(0);
            paid_his_filter.setText(String.valueOf(cv));
        }

        TextView total_his_filter = (TextView) findViewById(R.id.total_his_filter);
//        Cursor cursorx2 = db.rawQuery("SELECT SUM(total_bill_amount) FROM Vendor_sold_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' GROUP BY datetimee_new_from", null);
//        if (cursorx2.moveToFirst()){
//            float cv = cursorx2.getFloat(0);
//            total_his_filter.setText(String.valueOf(cv));
//        }

        float co1 = 0;
        Cursor cursor1 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' GROUP BY datetimee_new_from", null);
        if (cursor1.moveToFirst()){
            do {
                String dateti = cursor1.getString(13);
//                Toast.makeText(Inventory_Indent_Vendor_History.this, "1 "+dateti, Toast.LENGTH_SHORT).show();

                Cursor cursor2 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from = '"+dateti+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' ", null);
                if (cursor2.moveToFirst()){
                    String tot = cursor2.getString(9);
//                    Toast.makeText(Inventory_Indent_Vendor_History.this, "2 "+tot, Toast.LENGTH_SHORT).show();
                    co1 = co1+Float.parseFloat(tot);

                }
            }while (cursor1.moveToNext());
        }
        total_his_filter.setText(String.valueOf(co1));

        float cvb = Float.parseFloat(total_his_filter.getText().toString()) - Float.parseFloat(paid_his_filter.getText().toString());

        TextView pending_his_filter = (TextView) findViewById(R.id.pending_his_filter);
        pending_his_filter.setText(String.valueOf(cvb));


    }

    public Cursor fetchCountriesByName1(String inputtext) throws SQLException {

        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
            mCursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"'  AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' GROUP BY datetimee_new_from", null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND invoice LIKE '%" + inputtext + "%' GROUP BY datetimee_new_from", null);
        }

        return mCursor;
    }



    public Cursor fetchCountriesByName1_workinghours(String inputtext) throws SQLException {

        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
//            Cursor cursor = db.rawQuery("SELECT * FROM Vendor_sold_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' GROUP BY datetimee_new_from", null);
            mCursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' GROUP BY datetimee_new_from", null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"'AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND invoice LIKE '%" + inputtext + "%' GROUP BY datetimee_new_from", null);
        }

        return mCursor;
    }

    public Cursor fetchCountriesByName1_customtime(String inputtext) throws SQLException {

        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
//            Cursor cursor = db.rawQuery("SELECT * FROM Vendor_sold_details WHERE datetimee_new_from >= '"+editText1_filter_df.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter_df.getText().toString()+"' GROUP BY datetimee_new_from", null);
            mCursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter_df.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter_df.getText().toString()+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' GROUP BY datetimee_new_from", null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter_df.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter_df.getText().toString()+"' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND invoice LIKE '%" + inputtext + "%' GROUP BY datetimee_new_from", null);
        }

        return mCursor;
    }

    public Cursor fetchCountriesByName1_alltime(String inputtext) throws SQLException {

        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
//            Cursor cursor = db.rawQuery("SELECT * FROM Vendor_sold_details GROUP BY datetimee_new_from", null);
            mCursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' GROUP BY datetimee_new_from", null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND invoice LIKE '%" + inputtext + "%' GROUP BY datetimee_new_from", null);
        }

        return mCursor;
    }

    public Cursor fetchCountriesByName1_pending(String inputtext) throws SQLException {

        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
//            Cursor cursor = db.rawQuery("SELECT * FROM Vendor_sold_details WHERE pending != '0.0' GROUP BY datetimee_new_from", null);
            mCursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND pending != '0.0' GROUP BY datetimee_new_from", null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND pending != '0.0' AND invoice LIKE '%" + inputtext + "%' GROUP BY datetimee_new_from", null);
        }

        return mCursor;
    }

    public Cursor fetchCountriesByName1_paidlist(String inputtext) throws SQLException {

        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
//            Cursor cursor = db.rawQuery("SELECT * FROM Vendor_sold_details WHERE pending = '0' GROUP BY datetimee_new_from", null);
            mCursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND pending = '0' OR pending = '0.0' GROUP BY datetimee_new_from", null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND pending = '0' OR pending = '0.0' AND invoice LIKE '%" + inputtext + "%' GROUP BY datetimee_new_from", null);
        }

        return mCursor;
    }


    public Cursor fetchCountriesByName1_otherdues(String inputtext, String currentDateandTime1) throws SQLException {

        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
//            Cursor cursor = db.rawQuery("SELECT * FROM Vendor_sold_details WHERE datetimee_new_due <= '"+currentDateandTime1+"' GROUP BY datetimee_new_from", null);
            mCursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_due <= '"+currentDateandTime1+"' AND pending != '0' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' GROUP BY datetimee_new_from", null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_due <= '"+currentDateandTime1+"' AND pending != '0' AND vendor_name = '"+player1name+"' AND vendor_phoneno = '"+player2name+"' AND invoice LIKE '%" + inputtext + "%' GROUP BY datetimee_new_from", null);
        }

        return mCursor;
    }


    public ArrayList<String> getTableValuesall() {
        ArrayList<String> my_array = new ArrayList<String>();
        try {
            db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor allrows = db.rawQuery("SELECT * FROM Vendor_details_micro", null);
            System.out.println("COUNT : " + allrows.getCount());

            if (allrows.moveToFirst()) {
                do {
                    String ID = allrows.getString(0);
                    String NAME = allrows.getString(1);
                    String PLACE = allrows.getString(2);

                    my_array.add(NAME+" - "+PLACE);

                } while (allrows.moveToNext());
            }
            allrows.close();
            //db.close();
        } catch (Exception e) {
            Toast.makeText(Micro_Inventory_Indent_Vendor_History.this, "Error encountered.",
                    Toast.LENGTH_LONG);
        }
        return my_array;
    }


    private android.app.TimePickerDialog.OnTimeSetListener timePickerListener_open_dv = new android.app.TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime_open_dv(hour, minute);

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

            editText_from_day_hide_dv.setText(hour1 + "" + minutes1);


        }
    };

    private void updateTime_open_dv(int hours, int mins) {

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

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        editText_from_day_visible_dv.setText(aTime);
    }

    private android.app.TimePickerDialog.OnTimeSetListener timePickerListener_open_dp = new android.app.TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime_open_dp(hour, minute);

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

            editText_from_day_hide_dp.setText(hour1 + "" + minutes1);


        }
    };

    private void updateTime_open_dp(int hours, int mins) {

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

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        editText_from_day_visible_dp.setText(aTime);
    }


    private android.app.TimePickerDialog.OnTimeSetListener timePickerListener_open_df = new android.app.TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime_open_df(hour, minute);

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

            editText_from_day_hide_df.setText(hour1 + "" + minutes1);


        }
    };

    private void updateTime_open_df(int hours, int mins) {

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

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        editText_from_day_visible_df.setText(aTime);
    }

    private android.app.TimePickerDialog.OnTimeSetListener timePickerListener_close_df = new android.app.TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime_close_df(hour, minute);

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

            editText_to_day_hide_df.setText(hour1 + "" + minutes1);


        }
    };

    private void updateTime_close_df(int hours, int mins) {

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

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        editText_to_day_visible_df.setText(aTime);
    }

}
