package com.intuition.ivepos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.intuition.ivepos.syncapp.StubProviderApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Rohithkumar on 9/14/2017.
 */

public class Micro_Inventory_Indent_Ingredients_History extends AppCompatActivity {

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
    ListView list_history, list_items;
    SimpleCursorAdapter ddataAdapterr;

    String sel = "1";

    String insert1_cc = "", insert1_rs = "", str_country;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.micro_inventory_indent_ingredeints_history);

        ImageView back_pressed = (ImageView) findViewById(R.id.back_pressed);
        back_pressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        list_items = (ListView) findViewById(R.id.list_items);

        editText1_filter = new TextView(Micro_Inventory_Indent_Ingredients_History.this);
        editText2_filter = new TextView(Micro_Inventory_Indent_Ingredients_History.this);

        editText1_filter_df = new TextView(Micro_Inventory_Indent_Ingredients_History.this);
        editText2_filter_df = new TextView(Micro_Inventory_Indent_Ingredients_History.this);

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
        TextView inn3 = (TextView) findViewById(R.id.inn3);

        if (str_country.toString().equals("Rupee") || str_country.toString().equals("India")) {
            insert1_cc = "\u20B9";
            insert1_rs = "Rs.";
            inn.setText(insert1_cc);
            inn1.setText(insert1_cc);
            inn2.setText(insert1_cc);
            inn3.setText(insert1_cc);
        }else {
            if (str_country.toString().equals("Pound") || str_country.toString().equals("UK Pound")) {
                insert1_cc = "\u00a3";
                insert1_rs = "BP.";
                inn.setText(insert1_cc);
                inn1.setText(insert1_cc);
                inn2.setText(insert1_cc);
                inn3.setText(insert1_cc);
            }else {
                if (str_country.toString().equals("Euro") || str_country.toString().equals("UK Euro")) {
                    insert1_cc = "\u20ac";
                    insert1_rs = "EU.";
                    inn.setText(insert1_cc);
                    inn1.setText(insert1_cc);
                    inn2.setText(insert1_cc);
                    inn3.setText(insert1_cc);
                }else {
                    if (str_country.toString().equals("Dollar")) {
                        insert1_cc = "\u0024";
                        insert1_rs = "\u0024";
                        inn.setText(insert1_cc);
                        inn1.setText(insert1_cc);
                        inn2.setText(insert1_cc);
                        inn3.setText(insert1_cc);
                    }else {
                        if (str_country.toString().equals("Dinars")) {
                            insert1_cc = "D";
                            insert1_rs = "KD.";
                            inn.setText(insert1_cc);
                            inn1.setText(insert1_cc);
                            inn2.setText(insert1_cc);
                            inn3.setText(insert1_cc);
                        }else {
                            if (str_country.toString().equals("Shilling")) {
                                insert1_cc = "S";
                                insert1_rs = "S.";
                                inn.setText(insert1_cc);
                                inn1.setText(insert1_cc);
                                inn2.setText(insert1_cc);
                                inn3.setText(insert1_cc);
                            }else {
                                if (str_country.toString().equals("Ringitt")) {
                                    insert1_cc = "R";
                                    insert1_rs = "RM.";
                                    inn.setText(insert1_cc);
                                    inn1.setText(insert1_cc);
                                    inn2.setText(insert1_cc);
                                    inn3.setText(insert1_cc);
                                }else {
                                    if (str_country.toString().equals("Rial")) {
                                        insert1_cc = "R";
                                        insert1_rs = "OR.";
                                        inn.setText(insert1_cc);
                                        inn1.setText(insert1_cc);
                                        inn2.setText(insert1_cc);
                                        inn3.setText(insert1_cc);
                                    }else {
                                        if (str_country.toString().equals("Yen")) {
                                            insert1_cc = "\u00a5";
                                            insert1_rs = "\u00a5";
                                            inn.setText(insert1_cc);
                                            inn1.setText(insert1_cc);
                                            inn2.setText(insert1_cc);
                                            inn3.setText(insert1_cc);
                                        }else {
                                            if (str_country.toString().equals("Papua New Guinean")) {
                                                insert1_cc = "K";
                                                insert1_rs = "K.";
                                                inn.setText(insert1_cc);
                                                inn1.setText(insert1_cc);
                                                inn2.setText(insert1_cc);
                                                inn3.setText(insert1_cc);
                                            }else {
                                                if (str_country.toString().equals("UAE")) {
                                                    insert1_cc = "D";
                                                    insert1_rs = "DH.";
                                                    inn.setText(insert1_cc);
                                                    inn1.setText(insert1_cc);
                                                    inn2.setText(insert1_cc);
                                                    inn3.setText(insert1_cc);
                                                }else {
                                                    if (str_country.toString().equals("South African Rand")) {
                                                        insert1_cc = "R";
                                                        insert1_rs = "R.";
                                                        inn.setText(insert1_cc);
                                                        inn1.setText(insert1_cc);
                                                        inn2.setText(insert1_cc);
                                                        inn3.setText(insert1_cc);
                                                    }else {
                                                        if (str_country.toString().equals("Congolese Franc")) {
                                                            insert1_cc = "F";
                                                            insert1_rs = "FC.";
                                                            inn.setText(insert1_cc);
                                                            inn1.setText(insert1_cc);
                                                            inn2.setText(insert1_cc);
                                                            inn3.setText(insert1_cc);
                                                        }else {
                                                            if (str_country.toString().equals("Qatari Riyals")) {
                                                                insert1_cc = "QAR";
                                                                insert1_rs = "QAR.";
                                                                inn.setText(insert1_cc);
                                                                inn1.setText(insert1_cc);
                                                                inn2.setText(insert1_cc);
                                                                inn3.setText(insert1_cc);
                                                            }else {
                                                                if (str_country.toString().equals("Dirhams")) {
                                                                    insert1_cc = "AED";
                                                                    insert1_rs = "AED.";
                                                                    inn.setText(insert1_cc);
                                                                    inn1.setText(insert1_cc);
                                                                    inn2.setText(insert1_cc);
                                                                    inn3.setText(insert1_cc);
                                                                }else {
                                                                    if (str_country.toString().equals("Kuwait Dinar")) {
                                                                        insert1_cc = "KWD";
                                                                        insert1_rs = "KWD.";
                                                                        inn.setText(insert1_cc);
                                                                        inn1.setText(insert1_cc);
                                                                        inn2.setText(insert1_cc);
                                                                        inn3.setText(insert1_cc);
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

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items_temp_list");
        getContentResolver().delete(contentUri, null,null);
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("Items_temp_list")
                .appendQueryParameter("operation", "delete")
                .appendQueryParameter(null, null)
                .build();
        getContentResolver().notifyChange(resultUri, null);
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


        final EditText searchView = (EditText) findViewById(R.id.searchView);

        ImageView delete_icon = (ImageView) findViewById(R.id.delete_icon);
        delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setText("");
            }
        });


        DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
        downloadMusicfromInternet.execute();





        TextView no_of_vendors = (TextView) findViewById(R.id.no_of_vendors);

        Cursor ones = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' GROUP BY vendor_phoneno", null);
        int cou = ones.getCount();
//            level = one.getInt(0);
        String ttotal1 = String.valueOf(cou);
        no_of_vendors.setText(ttotal1);

        TextView paid_his_filter = (TextView) findViewById(R.id.paid_his_filter);
        Cursor cursorx1 = db.rawQuery("SELECT SUM(pay) FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"'", null);
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
        Cursor cursor1 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' GROUP BY datetimee_new_from", null);
        if (cursor1.moveToFirst()){
            do {
                String dateti = cursor1.getString(13);
//                Toast.makeText(Inventory_Indent_Items_History.this, "1 "+dateti, Toast.LENGTH_SHORT).show();

                Cursor cursor2 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from = '"+dateti+"'", null);
                if (cursor2.moveToFirst()){
                    String tot = cursor2.getString(9);
//                    Toast.makeText(Inventory_Indent_Items_History.this, "2 "+tot, Toast.LENGTH_SHORT).show();
                    co1 = co1+Float.parseFloat(tot);

                }
            }while (cursor1.moveToNext());
        }
        total_his_filter.setText(String.valueOf(co1));

        float cvb = Float.parseFloat(total_his_filter.getText().toString()) - Float.parseFloat(paid_his_filter.getText().toString());

        TextView pending_his_filter = (TextView) findViewById(R.id.pending_his_filter);
        pending_his_filter.setText(String.valueOf(cvb));


        RelativeLayout filter = (RelativeLayout) findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog_filter = new Dialog(Micro_Inventory_Indent_Ingredients_History.this, R.style.timepicker_date_dialog);
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

                        dpd.show(Micro_Inventory_Indent_Ingredients_History.this.getFragmentManager(), "Datepickerdialog");

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

                        dpd.show(Micro_Inventory_Indent_Ingredients_History.this.getFragmentManager(), "Datepickerdialog");
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
                        android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(Micro_Inventory_Indent_Ingredients_History.this, R.style.timepicker_date_dialog, timePickerListener_open_df, hour, minute,
                                false);

                        timePickerDialog.show();
                    }
                });

                editText_to_day_visible_df.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(Micro_Inventory_Indent_Ingredients_History.this, R.style.timepicker_date_dialog, timePickerListener_close_df, hour, minute,
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

                pending_list.setVisibility(View.GONE);
                paid_list.setVisibility(View.GONE);
                other_dues.setVisibility(View.GONE);
                working_hours.setChecked(true);


                if (!working_hours.isChecked() && !custom_time.isChecked() && !all_time.isChecked() && !pending_list.isChecked() && !paid_list.isChecked() && !other_dues.isChecked()){
                    working_hours.setChecked(true);
                }

                if (sel.toString().equals("1")){
                    working_hours.setChecked(true);
                }
                if (sel.toString().equals("2")){
                    custom_time.setChecked(true);
                }
                if (sel.toString().equals("3")){
                    all_time.setChecked(true);
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


                            list_items.setAdapter(null);

                            DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
                            downloadMusicfromInternet.execute();


                            TextView no_of_vendors = (TextView) findViewById(R.id.no_of_vendors);

                            Cursor ones = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' GROUP BY vendor_phoneno", null);
                            int cou = ones.getCount();
                            String ttotal1 = String.valueOf(cou);
                            no_of_vendors.setText(ttotal1);

                            TextView paid_his_filter = (TextView) findViewById(R.id.paid_his_filter);
                            Cursor cursorx1 = db.rawQuery("SELECT SUM(pay) FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"'", null);
                            if (cursorx1.moveToFirst()){
                                float cv = cursorx1.getFloat(0);
                                paid_his_filter.setText(String.valueOf(cv));
                            }

                            TextView total_his_filter = (TextView) findViewById(R.id.total_his_filter);

                            float co1 = 0;
                            Cursor cursor1 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' GROUP BY datetimee_new_from", null);
                            if (cursor1.moveToFirst()){
                                do {
                                    String dateti = cursor1.getString(13);
//                                    Toast.makeText(Inventory_Indent_Items_History.this, "1 "+dateti, Toast.LENGTH_SHORT).show();

                                    Cursor cursor2 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from = '"+dateti+"'", null);
                                    if (cursor2.moveToFirst()){
                                        String tot = cursor2.getString(9);
//                                        Toast.makeText(Inventory_Indent_Items_History.this, "2 "+tot, Toast.LENGTH_SHORT).show();
                                        co1 = co1+Float.parseFloat(tot);

                                    }
                                }while (cursor1.moveToNext());
                            }
                            total_his_filter.setText(String.valueOf(co1));

                            float cvb = Float.parseFloat(total_his_filter.getText().toString()) - Float.parseFloat(paid_his_filter.getText().toString());

                            TextView pending_his_filter = (TextView) findViewById(R.id.pending_his_filter);
                            pending_his_filter.setText(String.valueOf(cvb));

                            dialog_filter.dismiss();

                            sel = "1";

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

//                            Toast.makeText(Inventory_Indent_Items_History.this, "3 " + editText1_filter_df.getText().toString(), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(Inventory_Indent_Items_History.this, "4 " + editText2_filter_df.getText().toString(), Toast.LENGTH_SHORT).show();



                            editText1.setText(editText1_df.getText().toString());
                            editText11.setText(editText11_df.getText().toString());

                            editText2.setText(editText2_df.getText().toString());
                            editText22.setText(editText22_df.getText().toString());


                            editText_from_day_hide.setText(editText_from_day_hide_df.getText().toString());
                            editText_from_day_visible.setText(editText_from_day_visible_df.getText().toString());

                            editText_to_day_hide.setText(editText_to_day_hide_df.getText().toString());
                            editText_to_day_visible.setText(editText_to_day_visible_df.getText().toString());


                            list_items.setAdapter(null);

                            DownloadMusicfromInternet_df downloadMusicfromInternet = new DownloadMusicfromInternet_df();
                            downloadMusicfromInternet.execute();


                            TextView no_of_vendors = (TextView) findViewById(R.id.no_of_vendors);

                            Cursor ones = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter_df.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter_df.getText().toString()+"' GROUP BY vendor_phoneno", null);
                            int cou = ones.getCount();
                            String ttotal1 = String.valueOf(cou);
                            no_of_vendors.setText(ttotal1);

                            TextView paid_his_filter = (TextView) findViewById(R.id.paid_his_filter);
                            Cursor cursorx1 = db.rawQuery("SELECT SUM(pay) FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter_df.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter_df.getText().toString()+"'", null);
                            if (cursorx1.moveToFirst()){
                                float cv = cursorx1.getFloat(0);
                                paid_his_filter.setText(String.valueOf(cv));
                            }

                            TextView total_his_filter = (TextView) findViewById(R.id.total_his_filter);

                            float co1 = 0;
                            Cursor cursor1 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter_df.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter_df.getText().toString()+"' GROUP BY datetimee_new_from", null);
                            if (cursor1.moveToFirst()){
                                do {
                                    String dateti = cursor1.getString(13);
//                                    Toast.makeText(Inventory_Indent_Items_History.this, "1 "+dateti, Toast.LENGTH_SHORT).show();

                                    Cursor cursor2 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from = '"+dateti+"'", null);
                                    if (cursor2.moveToFirst()){
                                        String tot = cursor2.getString(9);
//                                        Toast.makeText(Inventory_Indent_Items_History.this, "2 "+tot, Toast.LENGTH_SHORT).show();
                                        co1 = co1+Float.parseFloat(tot);

                                    }
                                }while (cursor1.moveToNext());
                            }
                            total_his_filter.setText(String.valueOf(co1));

                            float cvb = Float.parseFloat(total_his_filter.getText().toString()) - Float.parseFloat(paid_his_filter.getText().toString());

                            TextView pending_his_filter = (TextView) findViewById(R.id.pending_his_filter);
                            pending_his_filter.setText(String.valueOf(cvb));

                            dialog_filter.dismiss();

                            sel = "2";


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

//                            Cursor cursor2 = db.rawQuery("SELECT * FROM Ingredient_sold_details GROUP BY datetimee_new_from", null);
//                            if (cursor2.moveToLast()){
//                                String dat = cursor2.getString(11);
//                                String tim = cursor2.getString(10);
//
////                                editText1.setText(editText1_df.getText().toString());
////                                editText11.setText(dat);
//
////                                editText2.setText(editText2_df.getText().toString());
//                                editText22.setText(dat);
//
////                                editText_from_day_hide.setText(editText_from_day_hide_df.getText().toString());
////                                editText_from_day_visible.setText(tim);
//
////                                editText_to_day_hide.setText(editText_to_day_hide_df.getText().toString());
//                                editText_to_day_visible.setText(tim);
//
//                            }

                            editText2.setText(currentDateandTime1);

                            editText22.setText(currentDateandTime2);


                            Cursor time_get = db.rawQuery("SELECT * FROM Working_hours", null);
                            if (time_get.moveToFirst()){
                                String two = time_get.getString(2);
                                String four = time_get.getString(4);
                                String five = time_get.getString(5);
                                String six = time_get.getString(6);
                                String three = time_get.getString(3);

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
                                                editText22.setText(tomorrow_visible);

                                                editText2.setText(tomorrow_hide);
                                            }
                                            if ((x_new.after(calendar1_new.getTime()) && x_new.before(calendar2_new.getTime()))) {
                                                editText22.setText(currentDateandTime2);

                                                editText2.setText(currentDateandTime1);
                                            }


                                        } else {

                                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                                            String tomorrow_visible = dateFormat.format(tomorrow);

                                            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                                            String tomorrow_hide = dateFormat1.format(tomorrow);

                                            editText22.setText(tomorrow_visible);

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

                                String r2;
                                r2 = editText2.getText().toString();
                                if (r2.toString().contains(" ")){
                                    r2 = r2.replace(" ", "");
                                }

                                editText2_filter.setText(r2+""+six);

                            }
                            time_get.close();

                            list_items.setAdapter(null);

                            DownloadMusicfromInternet_alltime downloadMusicfromInternet = new DownloadMusicfromInternet_alltime();
                            downloadMusicfromInternet.execute();

                            TextView no_of_vendors = (TextView) findViewById(R.id.no_of_vendors);

                            Cursor ones = db.rawQuery("SELECT * FROM Ingredient_sold_details GROUP BY vendor_phoneno", null);
                            int cou = ones.getCount();
                            String ttotal1 = String.valueOf(cou);
                            no_of_vendors.setText(ttotal1);

                            TextView paid_his_filter = (TextView) findViewById(R.id.paid_his_filter);
                            Cursor cursorx1 = db.rawQuery("SELECT SUM(pay) FROM Ingredient_sold_details", null);
                            if (cursorx1.moveToFirst()){
                                float cv = cursorx1.getFloat(0);
                                paid_his_filter.setText(String.valueOf(cv));
                            }

                            TextView total_his_filter = (TextView) findViewById(R.id.total_his_filter);

                            float co1 = 0;
                            Cursor cursorq1 = db.rawQuery("SELECT * FROM Ingredient_sold_details GROUP BY datetimee_new_from", null);
                            if (cursorq1.moveToFirst()){
                                do {
                                    String dateti = cursorq1.getString(13);
//                                    Toast.makeText(Inventory_Indent_Items_History.this, "1 "+dateti, Toast.LENGTH_SHORT).show();

                                    Cursor cursorq2 = db.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from = '"+dateti+"'", null);
                                    if (cursorq2.moveToFirst()){
                                        String tot = cursorq2.getString(9);
//                                        Toast.makeText(Inventory_Indent_Items_History.this, "2 "+tot, Toast.LENGTH_SHORT).show();
                                        co1 = co1+Float.parseFloat(tot);

                                    }
                                }while (cursorq1.moveToNext());
                            }
                            total_his_filter.setText(String.valueOf(co1));

                            float cvb = Float.parseFloat(total_his_filter.getText().toString()) - Float.parseFloat(paid_his_filter.getText().toString());

                            TextView pending_his_filter = (TextView) findViewById(R.id.pending_his_filter);
                            pending_his_filter.setText(String.valueOf(cvb));

                            dialog_filter.dismiss();

                            sel = "3";


                        }


                    }
                });

            }
        });


        list_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Cursor cursor1 = (Cursor) adapterView.getItemAtPosition(i);
                final String c_ItemID = cursor1.getString(cursor1.getColumnIndex("itemname"));
                final String c_avg_price = cursor1.getString(cursor1.getColumnIndex("avg_price"));
                final String c_tot_price = cursor1.getString(cursor1.getColumnIndex("total_price"));
                final String c_tot_qty = cursor1.getString(cursor1.getColumnIndex("total_qty"));
                final String c_wast_qty = cursor1.getString(cursor1.getColumnIndex("wastage_qty"));
                final String c_wast_cost = cursor1.getString(cursor1.getColumnIndex("wastage_cost"));


                final Dialog dialog1 = new Dialog(Micro_Inventory_Indent_Ingredients_History.this, R.style.timepicker_date_dialog);
                dialog1.setContentView(R.layout.dialog_inventory_indent_items_history);
                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog1.setCanceledOnTouchOutside(false);
                dialog1.show();

                TextView inn = (TextView) dialog1.findViewById(R.id.inn);
                TextView inn1 = (TextView) dialog1.findViewById(R.id.inn1);
                TextView inn2 = (TextView) dialog1.findViewById(R.id.inn2);

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

                ImageView closetext = (ImageView) dialog1.findViewById(R.id.closetext);
                closetext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });

                LinearLayout ingr = (LinearLayout) dialog1.findViewById(R.id.ingr);
                ingr.setVisibility(View.VISIBLE);
                TextView item_ro = (TextView) dialog1.findViewById(R.id.item_ro);
                item_ro.setText(c_ItemID);

                TextView avg_price = (TextView) dialog1.findViewById(R.id.avg_price);
                avg_price.setText(c_avg_price);

                TextView tot_price = (TextView) dialog1.findViewById(R.id.tot_price);
                tot_price.setText(c_tot_price);

                TextView tot_qty = (TextView) dialog1.findViewById(R.id.tot_qty);
                tot_qty.setText(c_tot_qty);


                TextView wast_qty = (TextView) dialog1.findViewById(R.id.wast_qty);
                wast_qty.setText(c_wast_qty);

                TextView wast_price = (TextView) dialog1.findViewById(R.id.wast_price);
                wast_price.setText(c_wast_cost);


                TextView total_listings = (TextView) dialog1.findViewById(R.id.total_listings);

                Cursor cursora = db.rawQuery("SELECT COUNT(itemname) FROM Ingredient_sold_item_details WHERE itemname = '"+c_ItemID+"'", null);
                if (cursora.moveToFirst()){
                    int a = cursora.getInt(0);
                    total_listings.setText(String.valueOf(a));
                }

                Cursor cursor = db.rawQuery("SELECT * FROM Ingredient_sold_item_details WHERE itemname = '"+c_ItemID+"'", null);
                if (cursor.moveToFirst()){
                    do {

                    }while (cursor.moveToNext());
                }

                ListView dialog_list_items = (ListView) dialog1.findViewById(R.id.dialog_list_items);

                final Cursor cursor_country1 = db.rawQuery("SELECT * FROM Country_Selection", null);
                if (cursor_country1.moveToFirst()){
                    str_country = cursor_country1.getString(1);
                }

                if (sel.toString().equals("1")){
                    dialog_list_items.setAdapter(null);
//                    Toast.makeText(Inventory_Indent_Items_History.this, "working hours", Toast.LENGTH_LONG).show();

                    Cursor cursorq = db.rawQuery("SELECT * FROM Ingredient_sold_item_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' AND itemname = '"+c_ItemID+"'", null);
                    String[] fromFieldNames = {"from_date", "from_time", "vendor_name", "qty_add", "individual_price", "total_price", "wastage", "individual_price", "unit", "unit", "unit", "unit", "unit"};
                    int[] toViewsID = {R.id.item_date, R.id.item_time, R.id.vendor_name, R.id.item_qty, R.id.item_indiv_price, R.id.item_total_price, R.id.wastage_qty, R.id.item_indiv_price1, R.id.unit, R.id.unit1, R.id.inn, R.id.x2, R.id.inn2};
                    SimpleCursorAdapter ddataAdapterr = new SimpleCursorAdapter(Micro_Inventory_Indent_Ingredients_History.this, R.layout.dialog_inventory_indents_ingredients_listview, cursorq, fromFieldNames, toViewsID, 0);
//                    dialog_list_items.setAdapter(ddataAdapterr);
                    ddataAdapterr.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                        @Override
                        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                            if (view.getId() == R.id.inn || view.getId() == R.id.x2 || view.getId() == R.id.inn2) {
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
                    dialog_list_items.setAdapter(ddataAdapterr);

                    Cursor cursoraf = db.rawQuery("SELECT COUNT(itemname) FROM Ingredient_sold_item_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' AND itemname = '"+c_ItemID+"'", null);
                    if (cursoraf.moveToFirst()){
                        int a = cursoraf.getInt(0);
                        total_listings.setText(String.valueOf(a));
                    }

                }
                if (sel.toString().equals("2")){
                    dialog_list_items.setAdapter(null);
//                    Toast.makeText(Inventory_Indent_Items_History.this, "working hours", Toast.LENGTH_LONG).show();

                    Cursor cursorq = db.rawQuery("SELECT * FROM Ingredient_sold_item_details WHERE datetimee_new_from >= '"+editText1_filter_df.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter_df.getText().toString()+"' AND itemname = '"+c_ItemID+"'", null);
                    String[] fromFieldNames = {"from_date", "from_time", "vendor_name", "qty_add", "individual_price", "total_price", "wastage", "individual_price", "unit", "unit", "unit", "unit", "unit"};
                    int[] toViewsID = {R.id.item_date, R.id.item_time, R.id.vendor_name, R.id.item_qty, R.id.item_indiv_price, R.id.item_total_price, R.id.wastage_qty, R.id.item_indiv_price1, R.id.unit, R.id.unit1, R.id.inn, R.id.x2, R.id.inn2};
                    SimpleCursorAdapter ddataAdapterr = new SimpleCursorAdapter(Micro_Inventory_Indent_Ingredients_History.this, R.layout.dialog_inventory_indents_ingredients_listview, cursorq, fromFieldNames, toViewsID, 0);
//                    dialog_list_items.setAdapter(ddataAdapterr);
                    ddataAdapterr.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                        @Override
                        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                            if (view.getId() == R.id.inn || view.getId() == R.id.x2 || view.getId() == R.id.inn2) {
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
                    dialog_list_items.setAdapter(ddataAdapterr);

                    Cursor cursoraf = db.rawQuery("SELECT COUNT(itemname) FROM Ingredient_sold_item_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' AND itemname = '"+c_ItemID+"'", null);
                    if (cursoraf.moveToFirst()){
                        int a = cursoraf.getInt(0);
                        total_listings.setText(String.valueOf(a));
                    }

                }
                if (sel.toString().equals("3")){
                    dialog_list_items.setAdapter(null);
//                    Toast.makeText(Inventory_Indent_Items_History.this, "working hours", Toast.LENGTH_LONG).show();

                    Cursor cursorq = db.rawQuery("SELECT * FROM Ingredient_sold_item_details WHERE itemname = '"+c_ItemID+"'", null);
                    String[] fromFieldNames = {"from_date", "from_time", "vendor_name", "qty_add", "individual_price", "total_price", "wastage", "individual_price", "unit", "unit", "unit", "unit", "unit"};
                    int[] toViewsID = {R.id.item_date, R.id.item_time, R.id.vendor_name, R.id.item_qty, R.id.item_indiv_price, R.id.item_total_price, R.id.wastage_qty, R.id.item_indiv_price1, R.id.unit, R.id.unit1, R.id.inn, R.id.x2, R.id.inn2};
                    SimpleCursorAdapter ddataAdapterr = new SimpleCursorAdapter(Micro_Inventory_Indent_Ingredients_History.this, R.layout.dialog_inventory_indents_ingredients_listview, cursorq, fromFieldNames, toViewsID, 0);
//                    dialog_list_items.setAdapter(ddataAdapterr);
                    ddataAdapterr.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                        @Override
                        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                            if (view.getId() == R.id.inn || view.getId() == R.id.x2 || view.getId() == R.id.inn2) {
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
                    dialog_list_items.setAdapter(ddataAdapterr);

                }

            }
        });

    }


    class DownloadMusicfromInternet extends AsyncTask<Void, Integer, Void> {
        private ProgressDialog dialog = new ProgressDialog(Micro_Inventory_Indent_Ingredients_History.this, R.style.timepicker_date_dialog);

        @Override
        protected Void doInBackground(Void... params) {
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients_temp_list");
            getContentResolver().delete(contentUri, null,null);
            resultUri = new Uri.Builder()
                    .scheme("content")
                    .authority(StubProviderApp.AUTHORITY)
                    .path("Ingredients_temp_list")
                    .appendQueryParameter("operation", "delete")
                    .appendQueryParameter(null, null)
                    .build();
            getContentResolver().notifyChange(resultUri, null);
//            db.delete("Ingredients_temp_list", null, null);

//            Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
//            if (cursor.moveToFirst()){
//                do {
//                    String id = cursor.getString(0);
//                    String prese_qty = cursor.getString(3);
//                    String min_qty = cursor.getString(20);
//
//                }while (cursor.moveToNext());
//            }

            Cursor cursor = db.rawQuery("SELECT * FROM Ingredients", null);
            if (cursor.moveToFirst()){
                do {
                    String id = cursor.getString(0);
                    String item_na = cursor.getString(1);
                    String unit = cursor.getString(5);
                    String prese_qty = cursor.getString(3);
                    String barcode = cursor.getString(15);
                    String min_qty = cursor.getString(20);


                    Log.v("barcode", barcode);

                    Cursor cursor11 = db.rawQuery("SELECT * FROM Ingredient_sold_item_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' AND itemname = '"+item_na+"'", null);
                    if (cursor11.moveToFirst()) {

                        ContentValues contentValues = new ContentValues();

                        contentValues.put("itemname", item_na);
                        contentValues.put("unit", unit);
                        Cursor cursor1 = db.rawQuery("SELECT SUM(total_price) FROM Ingredient_sold_item_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' AND itemname = '" + item_na + "'", null);
                        if (cursor1.moveToFirst()) {
                            float a = cursor1.getFloat(0);

                            Cursor cursor2 = db.rawQuery("SELECT SUM(qty_add) FROM Ingredient_sold_item_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' AND itemname = '" + item_na + "'", null);
                            if (cursor2.moveToFirst()) {
                                int a1 = cursor2.getInt(0);

                                float a2 = a / a1;

                                contentValues.put("avg_price", String.format("%.1f", a2));
                            }
                        }

                        Cursor cursor2 = db.rawQuery("SELECT MIN(individual_price) FROM Ingredient_sold_item_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' AND itemname = '" + item_na + "'", null);
                        if (cursor2.moveToFirst()) {
                            float one11 = cursor2.getFloat(0);
                            contentValues.put("min_price", String.format("%.1f", one11));
                        }

                        Cursor cursor3 = db.rawQuery("SELECT MAX(individual_price) FROM Ingredient_sold_item_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' AND itemname = '" + item_na + "'", null);
                        if (cursor3.moveToFirst()) {
                            float one11 = cursor3.getFloat(0);
                            contentValues.put("max_price", String.format("%.1f", one11));
                        }

                        Cursor cursor4 = db.rawQuery("SELECT SUM(qty_add) FROM Ingredient_sold_item_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' AND itemname = '" + item_na + "'", null);
                        if (cursor4.moveToFirst()) {
                            float one11 = cursor4.getFloat(0);
                            contentValues.put("total_qty", String.format("%.1f", one11));
                        }

                        Cursor cursor5 = db.rawQuery("SELECT SUM(total_price) FROM Ingredient_sold_item_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' AND itemname = '" + item_na + "'", null);
                        if (cursor5.moveToFirst()) {
                            float one11 = cursor5.getFloat(0);
                            contentValues.put("total_price", String.format("%.1f", one11));
                        }



                        Cursor cursor41 = db.rawQuery("SELECT SUM(wastage) FROM Ingredient_sold_item_details WHERE itemname = '" + item_na + "'", null);
                        if (cursor41.moveToFirst()) {
                            float one11 = cursor41.getFloat(0);
                            contentValues.put("wastage_qty", String.format("%.1f", one11));
                            if (String.valueOf(one11).toString().equals("0.0") || String.valueOf(one11).toString().equals("0") || String.valueOf(one11).toString().equals("")){
                                contentValues.put("wastage_cost", "0");
                            }else {
                                float a1 = 0;
                                Cursor cursor51 = db.rawQuery("SELECT * FROM Ingredient_sold_item_details WHERE itemname = '" + item_na + "' AND (wastage != '0' OR wastage != '0.0') ", null);
                                if (cursor51.moveToFirst()) {
                                    do {
                                        String wast = cursor51.getString(51);
                                        String ind_pr = cursor51.getString(5);


                                        float a = Float.parseFloat(wast) * Float.parseFloat(ind_pr);
                                        a1 = a1 + a;
                                        contentValues.put("wastage_cost", String.format("%.1f", a1));

                                    } while (cursor51.moveToNext());
                                } else {
                                    contentValues.put("wastage_cost", "0");
                                }
                            }
                        }


                        contentValues.put("barcode", barcode);
                        contentValues.put("not_required", "");
                        Log.v("barcode1", barcode);
                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients_temp_list");
                        resultUri = getContentResolver().insert(contentUri, contentValues);
                        getContentResolver().notifyChange(resultUri, null);
//                        db.insert("Ingredients_temp_list", null, contentValues);

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
            //Set the current progress to zero
            dialog.setProgress(0);
            //Display the progress dialog
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
        protected void onPostExecute(Void result) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();

            dialog.dismiss();

            final Cursor cursor_country1 = db.rawQuery("SELECT * FROM Country_Selection", null);
            if (cursor_country1.moveToFirst()){
                str_country = cursor_country1.getString(1);
            }

            Cursor cursor = db.rawQuery("SELECT * FROM Ingredients_temp_list WHERE total_price != '0' OR total_price != '0.0'", null);
            String[] fromFieldNames = {"itemname", "avg_price", "unit", "min_price", "max_price", "total_qty", "total_price", "wastage_qty", "wastage_cost", "wastage_cost", "wastage_cost", "wastage_cost", "wastage_cost", "wastage_cost"};
            int[] toViewsID = {R.id.itemname, R.id.avg_price, R.id.unit, R.id.min_price, R.id.max_price, R.id.total_qty, R.id.total_price, R.id.wastage_qty, R.id.wastage_price, R.id.inn, R.id.inn1, R.id.inn2, R.id.inn3, R.id.inn4};
            ddataAdapterr = new SimpleCursorAdapter(Micro_Inventory_Indent_Ingredients_History.this, R.layout.micro_inventory_ingredients_history_listview, cursor, fromFieldNames, toViewsID, 0);
//            list_items.setAdapter(ddataAdapterr);
            ddataAdapterr.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                    if (view.getId() == R.id.inn || view.getId() == R.id.inn1 || view.getId() == R.id.inn2 || view.getId() == R.id.inn3 || view.getId() == R.id.inn4) {
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
            list_items.setAdapter(ddataAdapterr);

            final EditText myFilter = (EditText) findViewById(R.id.searchView);
            myFilter.addTextChangedListener(new TextWatcher() {

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


            TextView wastage_his_filter = (TextView) findViewById(R.id.wastage_his_filter);
            TextView wastage_his_filter_percent = (TextView) findViewById(R.id.wastage_his_filter_percent);

            Cursor cursor11 = db.rawQuery("SELECT SUM(wastage_cost) FROM Ingredients_temp_list", null);
            if (cursor11.moveToFirst()) {
                float one11 = cursor11.getFloat(0);
                wastage_his_filter.setText(String.valueOf(one11));
            }

            TextView total_his_filter = (TextView) findViewById(R.id.total_his_filter);
            float a = (Float.parseFloat(wastage_his_filter.getText().toString()) *100) / Float.parseFloat(total_his_filter.getText().toString());
            wastage_his_filter_percent.setText(String.format("%.1f", a));

        }
    }



    class DownloadMusicfromInternet_df extends AsyncTask<Void, Integer, Void> {
        private ProgressDialog dialog = new ProgressDialog(Micro_Inventory_Indent_Ingredients_History.this, R.style.timepicker_date_dialog);

        @Override
        protected Void doInBackground(Void... params) {
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients_temp_list");
            getContentResolver().delete(contentUri, null,null);
            resultUri = new Uri.Builder()
                    .scheme("content")
                    .authority(StubProviderApp.AUTHORITY)
                    .path("Ingredients_temp_list")
                    .appendQueryParameter("operation", "delete")
                    .appendQueryParameter(null, null)
                    .build();
            getContentResolver().notifyChange(resultUri, null);
//            db.delete("Ingredients_temp_list", null, null);

//            Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
//            if (cursor.moveToFirst()){
//                do {
//                    String id = cursor.getString(0);
//                    String prese_qty = cursor.getString(3);
//                    String min_qty = cursor.getString(20);
//
//                }while (cursor.moveToNext());
//            }

            Cursor cursor = db.rawQuery("SELECT * FROM Ingredients", null);
            if (cursor.moveToFirst()){
                do {
                    String id = cursor.getString(0);
                    String item_na = cursor.getString(1);
                    String unit = cursor.getString(5);
                    String prese_qty = cursor.getString(3);
                    String barcode = cursor.getString(15);
                    String min_qty = cursor.getString(20);


                    Log.v("barcode", barcode);

                    Cursor cursor11 = db.rawQuery("SELECT * FROM Ingredient_sold_item_details WHERE datetimee_new_from >= '"+editText1_filter_df.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter_df.getText().toString()+"' AND itemname = '"+item_na+"'", null);
                    if (cursor11.moveToFirst()) {

                        ContentValues contentValues = new ContentValues();

                        contentValues.put("itemname", item_na);
                        contentValues.put("unit", unit);
                        Cursor cursor1 = db.rawQuery("SELECT SUM(total_price) FROM Ingredient_sold_item_details WHERE datetimee_new_from >= '"+editText1_filter_df.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter_df.getText().toString()+"' AND itemname = '" + item_na + "'", null);
                        if (cursor1.moveToFirst()) {
                            float a = cursor1.getFloat(0);

                            Cursor cursor2 = db.rawQuery("SELECT SUM(qty_add) FROM Ingredient_sold_item_details WHERE datetimee_new_from >= '"+editText1_filter_df.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter_df.getText().toString()+"' AND itemname = '" + item_na + "'", null);
                            if (cursor2.moveToFirst()) {
                                int a1 = cursor2.getInt(0);

                                float a2 = a / a1;

                                contentValues.put("avg_price", String.format("%.1f", a2));
                            }
                        }

                        Cursor cursor2 = db.rawQuery("SELECT MIN(individual_price) FROM Ingredient_sold_item_details WHERE datetimee_new_from >= '"+editText1_filter_df.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter_df.getText().toString()+"' AND itemname = '" + item_na + "'", null);
                        if (cursor2.moveToFirst()) {
                            float one11 = cursor2.getFloat(0);
                            contentValues.put("min_price", String.format("%.1f", one11));
                        }

                        Cursor cursor3 = db.rawQuery("SELECT MAX(individual_price) FROM Ingredient_sold_item_details WHERE datetimee_new_from >= '"+editText1_filter_df.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter_df.getText().toString()+"' AND itemname = '" + item_na + "'", null);
                        if (cursor3.moveToFirst()) {
                            float one11 = cursor3.getFloat(0);
                            contentValues.put("max_price", String.format("%.1f", one11));
                        }

                        Cursor cursor4 = db.rawQuery("SELECT SUM(qty_add) FROM Ingredient_sold_item_details WHERE datetimee_new_from >= '"+editText1_filter_df.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter_df.getText().toString()+"' AND itemname = '" + item_na + "'", null);
                        if (cursor4.moveToFirst()) {
                            float one11 = cursor4.getFloat(0);
                            contentValues.put("total_qty", String.format("%.1f", one11));
                        }

                        Cursor cursor5 = db.rawQuery("SELECT SUM(total_price) FROM Ingredient_sold_item_details WHERE datetimee_new_from >= '"+editText1_filter_df.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter_df.getText().toString()+"' AND itemname = '" + item_na + "'", null);
                        if (cursor5.moveToFirst()) {
                            float one11 = cursor5.getFloat(0);
                            contentValues.put("total_price", String.format("%.1f", one11));
                        }



                        Cursor cursor41 = db.rawQuery("SELECT SUM(wastage) FROM Ingredient_sold_item_details WHERE datetimee_new_from >= '"+editText1_filter_df.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter_df.getText().toString()+"' AND itemname = '" + item_na + "'", null);
                        if (cursor41.moveToFirst()) {
                            float one11 = cursor41.getFloat(0);
                            contentValues.put("wastage_qty", String.format("%.1f", one11));
                            if (String.valueOf(one11).toString().equals("0.0") || String.valueOf(one11).toString().equals("0") || String.valueOf(one11).toString().equals("")){
                                contentValues.put("wastage_cost", "0");
                            }else {
                                float a1 = 0;
                                Cursor cursor51 = db.rawQuery("SELECT * FROM Ingredient_sold_item_details WHERE itemname = '" + item_na + "' AND (wastage != '0' OR wastage != '0.0') ", null);
                                if (cursor51.moveToFirst()) {
                                    do {
                                        String wast = cursor51.getString(51);
                                        String ind_pr = cursor51.getString(5);


                                        float a = Float.parseFloat(wast) * Float.parseFloat(ind_pr);
                                        a1 = a1 + a;
                                        contentValues.put("wastage_cost", String.format("%.1f", a1));

                                    } while (cursor51.moveToNext());
                                } else {
                                    contentValues.put("wastage_cost", "0");
                                }
                            }
                        }


                        contentValues.put("barcode", barcode);
                        contentValues.put("not_required", "");
                        Log.v("barcode1", barcode);
                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients_temp_list");
                        resultUri = getContentResolver().insert(contentUri, contentValues);
                        getContentResolver().notifyChange(resultUri, null);
//                        db.insert("Ingredients_temp_list", null, contentValues);

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
            //Set the current progress to zero
            dialog.setProgress(0);
            //Display the progress dialog
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
        protected void onPostExecute(Void result) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();

            dialog.dismiss();

            final Cursor cursor_country1 = db.rawQuery("SELECT * FROM Country_Selection", null);
            if (cursor_country1.moveToFirst()){
                str_country = cursor_country1.getString(1);
            }

            Cursor cursor = db.rawQuery("SELECT * FROM Ingredients_temp_list WHERE total_price != '0' OR total_price != '0.0'", null);
            String[] fromFieldNames = {"itemname", "avg_price", "unit", "min_price", "max_price", "total_qty", "total_price", "wastage_qty", "wastage_cost", "wastage_cost", "wastage_cost", "wastage_cost", "wastage_cost", "wastage_cost"};
            int[] toViewsID = {R.id.itemname, R.id.avg_price, R.id.unit, R.id.min_price, R.id.max_price, R.id.total_qty, R.id.total_price, R.id.wastage_qty, R.id.wastage_price, R.id.inn, R.id.inn1, R.id.inn2, R.id.inn3, R.id.inn4};
            ddataAdapterr = new SimpleCursorAdapter(Micro_Inventory_Indent_Ingredients_History.this, R.layout.micro_inventory_ingredients_history_listview, cursor, fromFieldNames, toViewsID, 0);
//            list_items.setAdapter(ddataAdapterr);
            ddataAdapterr.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                    if (view.getId() == R.id.inn || view.getId() == R.id.inn1 || view.getId() == R.id.inn2 || view.getId() == R.id.inn3 || view.getId() == R.id.inn4) {
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
            list_items.setAdapter(ddataAdapterr);

            final EditText myFilter = (EditText) findViewById(R.id.searchView);
            myFilter.addTextChangedListener(new TextWatcher() {

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


            TextView wastage_his_filter = (TextView) findViewById(R.id.wastage_his_filter);
            TextView wastage_his_filter_percent = (TextView) findViewById(R.id.wastage_his_filter_percent);

            Cursor cursor11 = db.rawQuery("SELECT SUM(wastage_cost) FROM Ingredients_temp_list", null);
            if (cursor11.moveToFirst()) {
                float one11 = cursor11.getFloat(0);
                wastage_his_filter.setText(String.valueOf(one11));
            }

            TextView total_his_filter = (TextView) findViewById(R.id.total_his_filter);
            float a = (Float.parseFloat(wastage_his_filter.getText().toString()) *100) / Float.parseFloat(total_his_filter.getText().toString());
            wastage_his_filter_percent.setText(String.format("%.1f", a));

        }
    }


    class DownloadMusicfromInternet_alltime extends AsyncTask<Void, Integer, Void> {
        private ProgressDialog dialog = new ProgressDialog(Micro_Inventory_Indent_Ingredients_History.this, R.style.timepicker_date_dialog);

        @Override
        protected Void doInBackground(Void... params) {

            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients_temp_list");
            getContentResolver().delete(contentUri, null,null);
            resultUri = new Uri.Builder()
                    .scheme("content")
                    .authority(StubProviderApp.AUTHORITY)
                    .path("Ingredients_temp_list")
                    .appendQueryParameter("operation", "delete")
                    .appendQueryParameter(null, null)
                    .build();
            getContentResolver().notifyChange(resultUri, null);

//            db.delete("Ingredients_temp_list", null, null);

//            Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
//            if (cursor.moveToFirst()){
//                do {
//                    String id = cursor.getString(0);
//                    String prese_qty = cursor.getString(3);
//                    String min_qty = cursor.getString(20);
//
//                }while (cursor.moveToNext());
//            }

            Cursor cursor = db.rawQuery("SELECT * FROM Ingredients", null);
            if (cursor.moveToFirst()){
                do {
                    String id = cursor.getString(0);
                    String item_na = cursor.getString(1);
                    String unit = cursor.getString(5);
                    String prese_qty = cursor.getString(3);
                    String barcode = cursor.getString(15);
                    String min_qty = cursor.getString(20);


                    Log.v("barcode", barcode);

                    Cursor cursor11 = db.rawQuery("SELECT * FROM Ingredient_sold_item_details WHERE itemname = '"+item_na+"'", null);
                    if (cursor11.moveToFirst()) {

                        ContentValues contentValues = new ContentValues();

                        contentValues.put("itemname", item_na);
                        contentValues.put("unit", unit);
                        Cursor cursor1 = db.rawQuery("SELECT SUM(total_price) FROM Ingredient_sold_item_details WHERE itemname = '" + item_na + "'", null);
                        if (cursor1.moveToFirst()) {
                            float a = cursor1.getFloat(0);

                            Cursor cursor2 = db.rawQuery("SELECT SUM(qty_add) FROM Ingredient_sold_item_details WHERE itemname = '" + item_na + "'", null);
                            if (cursor2.moveToFirst()) {
                                int a1 = cursor2.getInt(0);

                                float a2 = a / a1;

                                contentValues.put("avg_price", String.format("%.1f", a2));
                            }
                        }

                        Cursor cursor2 = db.rawQuery("SELECT MIN(individual_price) FROM Ingredient_sold_item_details WHERE itemname = '" + item_na + "'", null);
                        if (cursor2.moveToFirst()) {
                            float one11 = cursor2.getFloat(0);
                            contentValues.put("min_price", String.format("%.1f", one11));
                        }

                        Cursor cursor3 = db.rawQuery("SELECT MAX(individual_price) FROM Ingredient_sold_item_details WHERE itemname = '" + item_na + "'", null);
                        if (cursor3.moveToFirst()) {
                            float one11 = cursor3.getFloat(0);
                            contentValues.put("max_price", String.format("%.1f", one11));
                        }

                        Cursor cursor4 = db.rawQuery("SELECT SUM(qty_add) FROM Ingredient_sold_item_details WHERE itemname = '" + item_na + "'", null);
                        if (cursor4.moveToFirst()) {
                            float one11 = cursor4.getFloat(0);
                            contentValues.put("total_qty", String.format("%.1f", one11));
                        }

                        Cursor cursor5 = db.rawQuery("SELECT SUM(total_price) FROM Ingredient_sold_item_details WHERE itemname = '" + item_na + "'", null);
                        if (cursor5.moveToFirst()) {
                            float one11 = cursor5.getFloat(0);
                            contentValues.put("total_price", String.format("%.1f", one11));
                        }



                        Cursor cursor41 = db.rawQuery("SELECT SUM(wastage) FROM Ingredient_sold_item_details WHERE itemname = '" + item_na + "'", null);
                        if (cursor41.moveToFirst()) {
                            float one11 = cursor41.getFloat(0);
                            contentValues.put("wastage_qty", String.format("%.1f", one11));
                            if (String.valueOf(one11).toString().equals("0.0") || String.valueOf(one11).toString().equals("0") || String.valueOf(one11).toString().equals("")){
                                contentValues.put("wastage_cost", "0");
                            }else {
                                float a1 = 0;
                                Cursor cursor51 = db.rawQuery("SELECT * FROM Ingredient_sold_item_details WHERE itemname = '" + item_na + "' AND (wastage != '0' OR wastage != '0.0') ", null);
                                if (cursor51.moveToFirst()) {
                                    do {
                                        String wast = cursor51.getString(51);
                                        String ind_pr = cursor51.getString(5);


                                        float a = Float.parseFloat(wast) * Float.parseFloat(ind_pr);
                                        a1 = a1 + a;
                                        contentValues.put("wastage_cost", String.format("%.1f", a1));

                                    } while (cursor51.moveToNext());
                                } else {
                                    contentValues.put("wastage_cost", "0");
                                }
                            }
                        }




                        contentValues.put("barcode", barcode);
                        contentValues.put("not_required", "");
                        Log.v("barcode1", barcode);
                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients_temp_list");
                        resultUri = getContentResolver().insert(contentUri, contentValues);
                        getContentResolver().notifyChange(resultUri, null);
//                        db.insert("Ingredients_temp_list", null, contentValues);

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
            //Set the current progress to zero
            dialog.setProgress(0);
            //Display the progress dialog
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
        protected void onPostExecute(Void result) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();

            dialog.dismiss();

            final Cursor cursor_country1 = db.rawQuery("SELECT * FROM Country_Selection", null);
            if (cursor_country1.moveToFirst()){
                str_country = cursor_country1.getString(1);
            }

            Cursor cursor = db.rawQuery("SELECT * FROM Ingredients_temp_list WHERE total_price != '0' OR total_price != '0.0'", null);
            String[] fromFieldNames = {"itemname", "avg_price", "unit", "min_price", "max_price", "total_qty", "total_price", "wastage_qty", "wastage_cost", "wastage_cost", "wastage_cost", "wastage_cost", "wastage_cost", "wastage_cost"};
            int[] toViewsID = {R.id.itemname, R.id.avg_price, R.id.unit, R.id.min_price, R.id.max_price, R.id.total_qty, R.id.total_price, R.id.wastage_qty, R.id.wastage_price, R.id.inn, R.id.inn1, R.id.inn2, R.id.inn3, R.id.inn4};
            ddataAdapterr = new SimpleCursorAdapter(Micro_Inventory_Indent_Ingredients_History.this, R.layout.micro_inventory_ingredients_history_listview, cursor, fromFieldNames, toViewsID, 0);
//            list_items.setAdapter(ddataAdapterr);
            ddataAdapterr.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                    if (view.getId() == R.id.inn || view.getId() == R.id.inn1 || view.getId() == R.id.inn2 || view.getId() == R.id.inn3 || view.getId() == R.id.inn4) {
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
            list_items.setAdapter(ddataAdapterr);

            final EditText myFilter = (EditText) findViewById(R.id.searchView);
            myFilter.addTextChangedListener(new TextWatcher() {

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


            TextView wastage_his_filter = (TextView) findViewById(R.id.wastage_his_filter);
            TextView wastage_his_filter_percent = (TextView) findViewById(R.id.wastage_his_filter_percent);

            Cursor cursor11 = db.rawQuery("SELECT SUM(wastage_cost) FROM Ingredients_temp_list", null);
            if (cursor11.moveToFirst()) {
                float one11 = cursor11.getFloat(0);
                wastage_his_filter.setText(String.valueOf(one11));
            }

            TextView total_his_filter = (TextView) findViewById(R.id.total_his_filter);
            float a = (Float.parseFloat(wastage_his_filter.getText().toString()) *100) / Float.parseFloat(total_his_filter.getText().toString());
            wastage_his_filter_percent.setText(String.format("%.1f", a));

        }
    }



    public Cursor fetchCountriesByName1(String inputtext) throws SQLException {

        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
            mCursor = db.rawQuery("SELECT * FROM Ingredients_temp_list", null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Ingredients_temp_list WHERE itemname LIKE '%" + inputtext + "%' OR barcode LIKE '%" + inputtext + "%'", null);
        }

        return mCursor;
    }


    public Cursor fetchCountriesByName1_customtime(String inputtext) throws SQLException {

        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
            mCursor = db.rawQuery("SELECT * FROM Ingredients_temp_list", null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Ingredients_temp_list WHERE itemname LIKE '%" + inputtext + "%' OR barcode LIKE '%" + inputtext + "%'", null);
        }

        return mCursor;
    }


    public Cursor fetchCountriesByName1_alltime(String inputtext) throws SQLException {

        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
            mCursor = db.rawQuery("SELECT * FROM Ingredients_temp_list", null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Ingredients_temp_list WHERE itemname LIKE '%" + inputtext + "%' OR barcode LIKE '%" + inputtext + "%'", null);
        }

        return mCursor;
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
