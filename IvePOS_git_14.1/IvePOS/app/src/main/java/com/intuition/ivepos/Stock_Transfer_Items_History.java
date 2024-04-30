package com.intuition.ivepos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import au.com.bytecode.opencsv.CSVWriter;

public class Stock_Transfer_Items_History extends AppCompatActivity {

    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;

    private int year, year1;
    int clickcount=1, clickcounts = 1;
    private int hour;
    private int minute;
    private int month, month1;
    private int day, day1;
    String date1 = "201707210001";
    String date2 = "201707212359";
    ListView listView;
    TextView tv_dateselecter;
    TextView editText_to_day_hide_dialog,editText_to_day_visible_dialog;
    String date1_filter, date2_filter, date1_end, date2_end, date1_end1, date2_end1, date1_filter_2, date2_filter_2, date1_filter_month_2, date2_filter_month_2;
    TextView editText1_dialog,editText2_dialog,editText11_dialog,editText22_dialog,editText_from_day_hide_dialog,editText_from_day_visible_dialog;
    TextView editText1, editText2, editText11, editText22, editText1_filter, editText2_filter;
    String str_editText11_dialog="",str_editText22_dialog="",str_editText_from_day_visible_dialog="",str_editText_to_day_visible_dialog="";
    TextView editText_from_day_visible, editText_from_day_hide, editText_to_day_visible, editText_to_day_hide;
    String onee, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve;
    String onee1, two1, three1, four1, five1, six1, seven1, eight1, nine1, ten1, eleven1, twelve1;
     String store_ori;
     String device_ori;
    Button btnok;
    View rootview;
     LinearLayout ll_dateselecter;
    LinearLayout ll_custom;
    int selectedPosition=0;

    RelativeLayout sent, receivables;

    File file=null, file1=null;
    SimpleDateFormat sdff2, sdff1;
    String currentDateandTimee1;
    String timee1;

    public static SharedPreferences getDefaultSharedPreferencesMultiProcess(
            Context context) {
        return context.getSharedPreferences(
                context.getPackageName() + "_preferences",
                Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
    }
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd",Locale.US);
    final String currentDateandTime1 = sdf2.format(new Date());

    SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM yyyy",Locale.US);
    final String currentDateandTime2 = sdf3.format(new Date());


    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",Locale.US);
    String time_hide = sdf.format(new Date());

    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm aa",Locale.US);
    String time_visible = sdf1.format(new Date());

    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_transfer_items_history);
       // rootview = inflater.inflate(R.layout.stock_transfer_items_history, null);

        editText_from_day_hide = (TextView) findViewById(R.id.editText_from_day_hide);
        editText_from_day_visible = (TextView) findViewById(R.id.editText_from_day_visible);


        editText_to_day_hide = (TextView) findViewById(R.id.editText_to_day_hide);
        editText_to_day_visible = (TextView) findViewById(R.id.editText_to_day_visible);

        editText11 = (TextView) findViewById(R.id.editText11);
        editText11.setText(currentDateandTime2);


        editText22 = (TextView) findViewById(R.id.editText22);
        editText22.setText(currentDateandTime2);


        btnok = (Button) findViewById(R.id.okok);
        ll_custom=findViewById(R.id.ll_custom);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        tv_dateselecter= findViewById(R.id.tv_dateselecter);

        ImageView back_pressed = (ImageView) findViewById(R.id.back_pressed);
        back_pressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(Stock_Transfer_Items_History.this);

        db1 = Stock_Transfer_Items_History.this.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

         store_ori = sharedpreferences.getString("storename", null);
         device_ori = sharedpreferences.getString("devicename", null);

        System.out.println("store original "+store_ori);
        System.out.println("device original "+device_ori);
        ll_dateselecter= findViewById(R.id.ll_dateselecter);



        listView = (ListView) findViewById(R.id.listView);

        sent = (RelativeLayout) findViewById(R.id.sent);
        receivables = (RelativeLayout) findViewById(R.id.receivables);

        sent.setBackgroundColor(getResources().getColor(R.color.DarkBlack));
        receivables.setBackgroundColor(getResources().getColor(R.color.itemedit));
        Cursor cursor = db.rawQuery("SELECT * FROM Stock_transfer_item_details WHERE from_store = '" + store_ori + "' AND from_device = '" + device_ori + "' GROUP BY datetimee_new_from", null);
        String[] fromFieldNames = {"from_date", "from_time", "to_store", "to_device", "itemname", "qty_add"};
        int[] toViewsID = {R.id.date, R.id.time, R.id.to_store, R.id.to_device, R.id.itemname};
        ImageCursorAdapter_ST ddataAdapterr = new ImageCursorAdapter_ST(Stock_Transfer_Items_History.this, R.layout.stock_transfer_items_history_listview, cursor, fromFieldNames, toViewsID);
        listView.setAdapter(ddataAdapterr);

        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sent.setBackgroundColor(getResources().getColor(R.color.DarkBlack));
                receivables.setBackgroundColor(getResources().getColor(R.color.itemedit));
                Cursor cursor = db.rawQuery("SELECT * FROM Stock_transfer_item_details WHERE from_store = '" + store_ori + "' AND from_device = '" + device_ori + "' GROUP BY datetimee_new_from", null);
                String[] fromFieldNames = {"from_date", "from_time", "to_store", "to_device", "itemname", "qty_add"};
                int[] toViewsID = {R.id.date, R.id.time, R.id.to_store, R.id.to_device, R.id.itemname};
                ImageCursorAdapter_ST ddataAdapterr = new ImageCursorAdapter_ST(Stock_Transfer_Items_History.this, R.layout.stock_transfer_items_history_listview, cursor, fromFieldNames, toViewsID);
                listView.setAdapter(ddataAdapterr);
            }
        });

        receivables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receivables.setBackgroundColor(getResources().getColor(R.color.DarkBlack));
                sent.setBackgroundColor(getResources().getColor(R.color.itemedit));
                Cursor cursor = db.rawQuery("SELECT * FROM Stock_transfer_item_details WHERE to_device = '" + device_ori + "' GROUP BY datetimee_new_from", null);
                String[] fromFieldNames = {"from_date", "from_time", "from_store", "from_device", "itemname", "qty_add"};
                int[] toViewsID = {R.id.date, R.id.time, R.id.to_store, R.id.to_device, R.id.itemname};
                ImageCursorAdapter_ST_rec ddataAdapterr = new ImageCursorAdapter_ST_rec(Stock_Transfer_Items_History.this, R.layout.stock_transfer_items_history_listview, cursor, fromFieldNames, toViewsID);
                listView.setAdapter(ddataAdapterr);
            }
        });


        LinearLayout linearLayout_stock_transfer_export = (LinearLayout) findViewById(R.id.linearLayout_stock_transfer_export);
        linearLayout_stock_transfer_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Stock_Transfer_Items_History.this, "export", Toast.LENGTH_LONG).show();

                sdff2 = new SimpleDateFormat("ddMMMyy",Locale.US);
                currentDateandTimee1 = sdff2.format(new Date());

                Date dt = new Date();
                sdff1 = new SimpleDateFormat("hhmmssaa",Locale.US);
                timee1 = sdff1.format(dt);

                ExportDatabaseCSVTask task=new ExportDatabaseCSVTask();
                task.execute();

            }
        });


        ll_dateselecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Stock_Transfer_Items_History.this,"This is clicked",Toast.LENGTH_LONG).show();

                final String[] select = {"Working Hours \n (Today)", "This week", "This Month", "This Year","All Time","Custom"};
                AlertDialog dialog = new AlertDialog.Builder(Stock_Transfer_Items_History.this,R.style.R_CustomAlertDialogStyle)
                        .setTitle(getString(R.string.title18))
                        .setSingleChoiceItems(select, selectedPosition, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                if (selectedPosition == 0) {
                                    // new LoadDataTask().execute();
                                    Toast.makeText(Stock_Transfer_Items_History.this,"Working Hr selected",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    new Stock_Transfer_Items_History.WorkingTask().execute();
                                    ll_custom.setVisibility(View.GONE);
                                }
                                if (selectedPosition == 1) {
                                    // new LoadDataTask().execute();
                                    Toast.makeText(Stock_Transfer_Items_History.this,"This week selected",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    new Stock_Transfer_Items_History.WeekTask().execute();
                                    ll_custom.setVisibility(View.GONE);
                                }
                                if (selectedPosition == 2) {
                                    // new LoadDataTask().execute();
                                    Toast.makeText(Stock_Transfer_Items_History.this,"This month selected",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    new Stock_Transfer_Items_History.MonthTask().execute();
                                    ll_custom.setVisibility(View.GONE);
                                }
                                if (selectedPosition == 3) {
                                    // new LoadDataTask().execute();
                                    Toast.makeText(Stock_Transfer_Items_History.this,"This year selected",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    new Stock_Transfer_Items_History.YearTask().execute();
                                    ll_custom.setVisibility(View.GONE);
                                }
                                if (selectedPosition == 4) {
                                    // new LoadDataTask().execute();
                                    Toast.makeText(Stock_Transfer_Items_History.this,"All time selected",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    new Stock_Transfer_Items_History.AllTimeTask().execute();
                                    ll_custom.setVisibility(View.GONE);
                                }
                                if (selectedPosition == 5) {
                                    // new LoadDataTask().execute();
                                    Toast.makeText(Stock_Transfer_Items_History.this,"Custom selected",Toast.LENGTH_LONG).show();

                                    dialog.dismiss();
                                    final Dialog dialoge1 = new Dialog(Stock_Transfer_Items_History.this, R.style.timepicker_date_dialog);
                                    dialoge1.setContentView(R.layout.customdialog);
                                    initCustom(dialoge1);
                                    dialoge1.show();
                                    ImageView iv_cancel=dialoge1.findViewById(R.id.iv_cancel);
                                    iv_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialoge1.cancel();
                                        }
                                    });


                                    Button btn_ok=dialoge1.findViewById(R.id.okok);
                                    btn_ok.setOnClickListener(V ->{
                                        str_editText11_dialog= editText11_dialog.getText().toString();//11 Jul 2021 - from
                                        str_editText22_dialog= editText22_dialog.getText().toString();//13 Jul 2021 - to
                                        str_editText_from_day_visible_dialog= editText_from_day_visible_dialog.getText().toString();//12:01 AM
                                        str_editText_to_day_visible_dialog= editText_to_day_visible_dialog.getText().toString();//11:59 PM

                                        editText11.setText(str_editText11_dialog);
                                        editText22.setText(str_editText22_dialog);
                                        editText_from_day_visible.setText(str_editText_from_day_visible_dialog);
                                        editText_to_day_visible.setText(str_editText_to_day_visible_dialog);

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
                                        date_dialog1=date_dialog1+date_start[0];//20210711 - from
                                        String time_dialog1="";//0001

                                        editText1.setText(date_dialog1);

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

                                        String editt_fr_day_hi = time_dialog1.substring(0, 2)+":"+time_dialog1.substring(2, 4);
                                        editText_from_day_hide.setText(editt_fr_day_hi);


                                        String[] date_end=str_editText22_dialog.split(" ");
                                        String date_dialog2=date_end[2];
                                        if(date_end[1].equalsIgnoreCase("Jan")){
                                            date_dialog2=date_dialog2+"01";
                                        }else if(date_end[1].equalsIgnoreCase("Feb")){
                                            date_dialog2=date_dialog2+"02";
                                        }else if(date_end[1].equalsIgnoreCase("Mar")){
                                            date_dialog2=date_dialog2+"03";
                                        }else if(date_end[1].equalsIgnoreCase("Apr")){
                                            date_dialog2=date_dialog2+"04";
                                        }else if(date_end[1].equalsIgnoreCase("May")){
                                            date_dialog2=date_dialog2+"05";
                                        }else if(date_end[1].equalsIgnoreCase("Jun")){
                                            date_dialog2=date_dialog2+"06";
                                        }else if(date_end[1].equalsIgnoreCase("Jul")){
                                            date_dialog2=date_dialog2+"07";
                                        }else if(date_end[1].equalsIgnoreCase("Aug")){
                                            date_dialog2=date_dialog2+"08";
                                        }else if(date_end[1].equalsIgnoreCase("Sep")){
                                            date_dialog2=date_dialog2+"09";
                                        }else if(date_end[1].equalsIgnoreCase("Oct")){
                                            date_dialog2=date_dialog2+"10";
                                        }else if(date_end[1].equalsIgnoreCase("Nov")){
                                            date_dialog2=date_dialog2+"11";
                                        }else if(date_end[1].equalsIgnoreCase("Dec")){
                                            date_dialog2=date_dialog2+"12";
                                        }
                                        date_dialog2=date_dialog2+date_end[0];//20210713 - to

                                        String time_dialog2="";//2359

                                        editText2.setText(date_dialog2);

                                        try {
                                            date = parseFormat.parse(str_editText_to_day_visible_dialog);
                                            time_dialog2=displayFormat.format(date);
                                            time_dialog2=time_dialog2.replace(":","");
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        date_dialog2=date_dialog2+time_dialog2;

                                        String editt_to_day_hi = time_dialog2.substring(0, 2)+":"+time_dialog2.substring(2, 4);
                                        editText_to_day_hide.setText(editt_to_day_hi);

                                        editText1_filter.setText(date_dialog1);
                                        editText2_filter.setText(date_dialog2);


                                        date1 = editText1_filter.getText().toString();
                                        date2 = editText2_filter.getText().toString();

                                        if (date1.length() == 11) {

                                            String onetoeight = date1.substring(0, 11);
                                            date1 = onetoeight + "1";
                                        }else {

                                        }
                                        dialoge1.dismiss();

                                        tv_dateselecter.setText(str_editText11_dialog+","+str_editText_from_day_visible_dialog+" - "+str_editText22_dialog+","+str_editText_to_day_visible_dialog);

                                        String cgt1 = "0";
                                        int cgt2;
                                       // Cursor cursor = db.rawQuery("SELECT * FROM Stock_transfer_item_details WHERE to_device = '" + device_ori + "' GROUP BY datetimee_new_from", null);
                                       // String[] fromFieldNames = {"from_date", "from_time", "from_store", "from_device", "itemname", "qty_add"};
                                        Cursor cgt = db.rawQuery("SELECT * FROM Stock_transfer_item_details WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ", null);
                                        if (cgt.moveToFirst()) {
                                            cgt2 = cgt.getInt(0);
                                            cgt1 = String.valueOf(cgt2);
                                          //  Cursor cursor = db.rawQuery("SELECT * FROM Stock_transfer_item_details WHERE to_device = '" + device_ori + "' GROUP BY datetimee_new_from", null);
                                            String[] fromFieldNames = {"from_date", "from_time", "from_store", "from_device", "itemname", "qty_add"};
                                            int[] toViewsID = {R.id.date, R.id.time, R.id.to_store, R.id.to_device, R.id.itemname};
                                            ImageCursorAdapter_ST_rec ddataAdapterr = new ImageCursorAdapter_ST_rec(Stock_Transfer_Items_History.this, R.layout.stock_transfer_items_history_listview, cgt, fromFieldNames, toViewsID);
                                            listView.setAdapter(ddataAdapterr);
                                        }
                                        cgt.close();
                                    });
                                }
                            }
                        }).create();
                dialog.show();
            }
        });

    }


    
    private class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(Stock_Transfer_Items_History.this, R.style.timepicker_date_dialog);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage(getString(R.string.setmessage13));
            this.dialog.show();

        }
        protected Boolean doInBackground(final String... args){
//            File exportDir = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_reports/IVEPOS_Stock_transfer_report");
            File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_reports/IVEPOS_Stock_transfer_report");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            file = new File(exportDir, "IvePOS_Stock_Transfer_report"+currentDateandTimee1+"_"+timee1+".csv");
            try {
                file.createNewFile();

                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));


// this is the Column of the table and same for Header of CSV file
                String arrStr1[] ={"Date", "Time", "Itemname", "Qty transferred", "From store", "From device", "To store", "To device"};
                csvWrite.writeNext(arrStr1);

                Cursor curCSV = db.rawQuery("SELECT * FROM Stock_transfer_item_details",null);
                //csvWrite.writeNext(curCSV.getColumnNames());

                while(curCSV.moveToNext())  {
                    String arrStr[] ={curCSV.getString(9), curCSV.getString(8), curCSV.getString(1), curCSV.getString(2),
                            curCSV.getString(4), curCSV.getString(5), curCSV.getString(6), curCSV.getString(7)};
//	                curCSV.getString(2),curCSV.getString(3),curCSV.getString(4),
                    csvWrite.writeNext(arrStr);

                }
                curCSV.close();

                csvWrite.close();

                return true;
            }
            catch (IOException e){
                Log.e("MainActivity", e.getMessage(), e);
                return false;

            }
        }

        @Override
        protected void onPostExecute(final Boolean success)	{

            if (this.dialog.isShowing()){
                this.dialog.dismiss();
            }
            if (success){
                Toast.makeText(Stock_Transfer_Items_History.this, getString(R.string.export_successful), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(Stock_Transfer_Items_History.this, getString(R.string.export_failed), Toast.LENGTH_SHORT).show();
            }
        }
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


    class AllTimeTask extends AsyncTask<String, String, String>{
        String mon="";
        int dow;
        int day;
        int month;
        int year;
        int min;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Cursor cursor = db.rawQuery("SELECT * FROM Stock_transfer_item_details WHERE from_store = '" + store_ori + "' AND from_device = '" + device_ori + "' GROUP BY datetimee_new_from", null);
            String[] fromFieldNames = {"from_date", "from_time", "to_store", "to_device", "itemname", "qty_add"};
 //           Cursor time_get = db.rawQuery("SELECT * FROM All_Sales", null);
//            if (time_get.moveToFirst()) {
//                String date[]=time_get.getString(13).split(" ");
//                year=Integer.parseInt(date[0]);
//                month=Integer.parseInt(date[1]);
//                day=Integer.parseInt(date[2]);
//            }
//            time_get.close();
            if (cursor.moveToFirst()){
                String date[]=cursor.getString(9).split(" ");
                year=Integer.parseInt(date[0]);
                month=Integer.parseInt(date[1]);
                day=Integer.parseInt(date[2]);
            }
            cursor.close();

            populateSetDate(year,month,day);
            Calendar cal = Calendar.getInstance();
            day = cal.get(Calendar.DATE);
            month = cal.get(Calendar.MONTH) + 1;
            year = cal.get(Calendar.YEAR);

            populateSetDate_2(year,month,day);
            updateTime_open(0, 1);
            updateTime_close(23, 59);
        }

        @Override
        protected String doInBackground(String... strings) {


            if(month==1){
                mon="Jan";
            }else if(month==2){
                mon="Feb";
            }else if(month==3){
                mon="Mar";
            }else if(month==4){
                mon="Apr";
            }else if(month==5){
                mon="May";
            }else if(month==6){
                mon="Jun";
            }else if(month==7){
                mon="Jul";
            }else if(month==8){
                mon="Aug";
            }else if(month==9){
                mon="Sep";
            }else if(month==10){
                mon="Oct";
            }else if(month==11){
                mon="Nov";
            }else if(month==12){
                mon="Dec";
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Cursor cursor = db.rawQuery("SELECT * FROM Stock_transfer_item_details WHERE from_store = '" + store_ori + "' AND from_device = '" + device_ori + "' GROUP BY datetimee_new_from", null);
           // String[] fromFieldNames = {"from_date", "from_time", "to_store", "to_device", "itemname", "qty_add"};

          //  Cursor time_get = db.rawQuery("SELECT * FROM All_Sales", null);
            if (cursor.moveToFirst()) {
                String date=cursor.getString(10);
                SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM yyyy");
                final String currentDateandTime2 = sdf3.format(new Date());
                tv_dateselecter.setText(date+" - "+currentDateandTime2);
            }
            cursor.close();


            btnok.callOnClick();

        }




        public void populateSetDate(int year, int month, int day) {
            TextView mEdit = (TextView) Stock_Transfer_Items_History.this.findViewById(R.id.editText1);
            TextView mEdit1  = (TextView)Stock_Transfer_Items_History.this.findViewById(R.id.editText11);
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



        public void populateSetDate_2(int year, int month, int day) {
            TextView mEdit = (TextView) Stock_Transfer_Items_History.this.findViewById(R.id.editText2);
            TextView mEdit1  = (TextView)Stock_Transfer_Items_History.this.findViewById(R.id.editText22);
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

    private android.app.TimePickerDialog.OnTimeSetListener timePickerListener_open = new android.app.TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime_open(hour, minute);

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

            editText_from_day_hide.setText(hour1 + "" + minutes1);


        }
    };

    private android.app.TimePickerDialog.OnTimeSetListener timePickerListener_close = new android.app.TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime_close(hour, minute);

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

            editText_to_day_hide.setText(hour1 + "" + minutes1);
        }
    };


    private void initCustom(final Dialog dialoge1) {

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

        editText1_dialog = (TextView) dialoge1.findViewById(R.id.editText1_dialog);
        editText1_dialog.setText(currentDateandTime1);
    /*    editText1 = (TextView) findViewById(R.id.editText1);
        editText1.setText(currentDateandTime1);*/


        editText2_dialog = (TextView) dialoge1.findViewById(R.id.editText2_dialog);
        editText2_dialog.setText(currentDateandTime1);
/*        editText2 = (TextView) findViewById(R.id.editText2);
        editText2.setText(currentDateandTime1);*/

        editText11_dialog = (TextView) dialoge1.findViewById(R.id.editText11_dialog);
        editText11_dialog.setText(currentDateandTime2);
/*        editText11 = (TextView) findViewById(R.id.editText11);
        editText11.setText(currentDateandTime2);*/

        editText22_dialog = (TextView) dialoge1.findViewById(R.id.editText22_dialog);
        editText22_dialog.setText(currentDateandTime2);
  /*      editText22 = (TextView) findViewById(R.id.editText22);
        editText22.setText(currentDateandTime2);*/


        editText_from_day_hide_dialog = (TextView) dialoge1.findViewById(R.id.editText_from_day_hide_dialog);
        editText_from_day_visible_dialog = (TextView) dialoge1.findViewById(R.id.editText_from_day_visible_dialog);
        editText_to_day_hide_dialog = (TextView) dialoge1.findViewById(R.id.editText_to_day_hide_dialog);
        editText_to_day_visible_dialog = (TextView) dialoge1.findViewById(R.id.editText_to_day_visible_dialog);


/*
        editText_from_day_hide = (TextView) findViewById(R.id.editText_from_day_hide);
        editText_from_day_visible = (TextView) findViewById(R.id.editText_from_day_visible);
        editText_to_day_hide = (TextView) findViewById(R.id.editText_to_day_hide);
        editText_to_day_visible = (TextView) findViewById(R.id.editText_to_day_visible);
*/


        updateTime_open_dialog(0, 1);
        updateTime_close_dialog(23, 59);

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
                dpd.show(Stock_Transfer_Items_History.this.getFragmentManager(), "Datepickerdialog");
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
        /*        TextView mEdit = (TextView) dialoge1.findViewById(R.id.editText1);
                TextView mEdit1  = (TextView)dialoge1.findViewById(R.id.editText11);*/
                TextView mEdit_dialog = (TextView) dialoge1.findViewById(R.id.editText1_dialog);
                TextView mEdit1_dialog  = (TextView)dialoge1.findViewById(R.id.editText11_dialog);

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


        editText22_dialog.setOnClickListener(new View.OnClickListener() {

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
                //if (clickcount == 1){
                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        datePickerListener,now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMaxDate(Calendar.getInstance());
                dpd.show(Stock_Transfer_Items_History.this.getFragmentManager(), "Datepickerdialog");
                clickcount++;
//                }else {
//                    Calendar now = Calendar.getInstance();
//                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
//                            datePickerListener, year, month, day
//                    );
//
//                    dpd.show(HomeActivity.this.getFragmentManager(), "Datepickerdialog");
//                }

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

//                // when dialog box is closed, below method will be called.
//                public void onDateSet(DatePicker view, int selectedYear,
//                                      int selectedMonth, int selectedDay) {
//
//
//
//
//                }
//            };


            public void populateSetDate(int year, int month, int day) {
            /*    TextView mEdit = (TextView) dialoge1.findViewById(R.id.editText2);
                TextView mEdit1  = (TextView) dialoge1.findViewById(R.id.editText22);*/
                TextView mEdit_dialog = (TextView) dialoge1.findViewById(R.id.editText2_dialog);
                TextView mEdit1_dialog  = (TextView) dialoge1.findViewById(R.id.editText22_dialog);
                if (month == 1 && day < 10) {
                    //    mEdit.setText(year + " " + "0" + 1 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + "0" + 1 + " " + "0" + day);
                    onee1 = "0" + day + " " + "Jan" + " " + year;
                    //    mEdit1.setText(onee1);
                    mEdit1_dialog.setText(onee1);
                } else {
                    if (month == 1) {
                        //       mEdit.setText(year + " " + "0" + 1 + " " + day);
                        mEdit_dialog.setText(year + " " + "0" + 1 + " " + day);
                        onee = day + " " + "Jan" + " " + year;
                        //      mEdit1.setText(onee);
                        mEdit1_dialog.setText(onee);
                    }
                }

                if (month == 2 && day < 10) {
                    //    mEdit.setText(year + " " + "0" + 2 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + "0" + 2 + " " + "0" + day);
                    two1 = "0" + day + " " + "Feb" + " " + year;
                    mEdit1_dialog.setText(two1);
                } else {
                    if (month == 2) {
                        //       mEdit.setText(year + " " + "0" + 2 + " " + day);
                        mEdit_dialog.setText(year + " " + "0" + 2 + " " + day);
                        two = day + " " + "Feb" + " " + year;
                        ///      mEdit1.setText(two);
                        mEdit1_dialog.setText(two);
                    }
                }

                if (month == 3 && day < 10) {
                    //    mEdit.setText(year + " " + "0" + 3 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + "0" + 3 + " " + "0" + day);
                    three1 = "0" + day + " " + "Mar" + " " + year;
                    //    mEdit1.setText(three1);
                    mEdit1_dialog.setText(three1);
                } else {
                    if (month == 3) {
                        //        mEdit.setText(year + " " + "0" + 3 + " " + day);
                        mEdit_dialog.setText(year + " " + "0" + 3 + " " + day);
                        three = day + " " + "Mar" + " " + year;
                        //      mEdit1.setText(three);
                        mEdit1_dialog.setText(three);
                    }
                }

                if (month == 4 && day < 10) {
                    //    mEdit.setText(year + " " + "0" + 4 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + "0" + 4 + " " + "0" + day);
                    four1 = "0" + day + " " + "Apr" + " " + year;
                    //     mEdit1.setText(four1);
                    mEdit1_dialog.setText(four1);
                } else {
                    if (month == 4) {
                        //       mEdit.setText(year + " " + "0" + 4 + " " + day);
                        mEdit_dialog.setText(year + " " + "0" + 4 + " " + day);
                        four = day + " " + "Apr" + " " + year;
                        //       mEdit1.setText(four);
                        mEdit1_dialog.setText(four);
                    }
                }

                if (month == 5 && day < 10) {
                    //     mEdit.setText(year + " " + "0" + 5 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + "0" + 5 + " " + "0" + day);
                    five1 = "0" + day + " " + "May" + " " + year;
                    //     mEdit1.setText(five1);
                    mEdit1_dialog.setText(five1);
                } else {
                    if (month == 5) {
                        //         mEdit.setText(year + " " + "0" + 5 + " " + day);
                        mEdit_dialog.setText(year + " " + "0" + 5 + " " + day);
                        five = day + " " + "May" + " " + year;
                        //       mEdit1.setText(five);
                        mEdit1_dialog.setText(five);
                    }
                }

                if (month == 6 && day < 10) {
                    //     mEdit.setText(year + " " + "0" + 6 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + "0" + 6 + " " + "0" + day);
                    six1 = "0" + day + " " + "Jun" + " " + year;
                    //      mEdit1.setText(six1);
                    mEdit1_dialog.setText(six1);
                } else {
                    if (month == 6) {
                        //          mEdit.setText(year + " " + "0" + 6 + " " + day);
                        mEdit_dialog.setText(year + " " + "0" + 6 + " " + day);
                        six = day + " " + "Jun" + " " + year;
                        //        mEdit1.setText(six);
                        mEdit1_dialog.setText(six);
                    }
                }

                if (month == 7 && day < 10) {
                    //        mEdit.setText(year + " " + "0" + 7 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + "0" + 7 + " " + "0" + day);
                    seven1 = "0" + day + " " + "Jul" + " " + year;
                    //          mEdit1.setText(seven1);
                    mEdit1_dialog.setText(seven1);
                } else {
                    if (month == 7) {
                        //            mEdit.setText(year + " " + "0" + 7 + " " + day);
                        mEdit_dialog.setText(year + " " + "0" + 7 + " " + day);
                        seven = day + " " + "Jul" + " " + year;
                        //           mEdit1.setText(seven);
                        mEdit1_dialog.setText(seven);
                    }
                }

                if (month == 8 && day < 10) {
                    //       mEdit.setText(year + " " + "0" + 8 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + "0" + 8 + " " + "0" + day);
                    eight1 = "0" + day + " " + "Aug" + " " + year;
                    //        mEdit1.setText(eight1);
                    mEdit1_dialog.setText(eight1);
                } else {
                    if (month == 8) {
                        //             mEdit.setText(year + " " + "0" + 8 + " " + day);
                        mEdit_dialog.setText(year + " " + "0" + 8 + " " + day);
                        eight = day + " " + "Aug" + " " + year;
                        //           mEdit1.setText(eight);
                        mEdit1_dialog.setText(eight);
                    }
                }

                if (month == 9 && day < 10) {
                    //         mEdit.setText(year + " " + "0" + 9 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + "0" + 9 + " " + "0" + day);
                    nine1 = "0" + day + " " + "Sep" + " " + year;
                    //        mEdit1.setText(nine1);
                    mEdit1_dialog.setText(nine1);
                } else {
                    if (month == 9) {
                        //          mEdit.setText(year + " " + "0" + 9 + " " + day);
                        mEdit_dialog.setText(year + " " + "0" + 9 + " " + day);
                        nine = day + " " + "Sep" + " " + year;
                        //         mEdit1.setText(nine);
                        mEdit1_dialog.setText(nine);
                    }
                }

                if (month == 10 && day < 10) {
                    //        mEdit.setText(year + " " + 10 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + 10 + " " + "0" + day);
                    ten1 = "0" + day + " " + "Oct" + " " + year;
                    //       mEdit1.setText(ten1);
                    mEdit1_dialog.setText(ten1);
                } else {
                    if (month == 10) {
                        //            mEdit.setText(year + " " + 10 + " " + day);
                        mEdit_dialog.setText(year + " " + 10 + " " + day);
                        ten = day + " " + "Oct" + " " + year;
                        //           mEdit1.setText(ten);
                        mEdit1_dialog.setText(ten);
                    }
                }

                if (month == 11 && day < 10) {
                    //        mEdit.setText(year + " " + 11 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + 11 + " " + "0" + day);
                    eleven1 = "0" + day + " " + "Nov" + " " + year;
                    //       mEdit1.setText(eleven1);
                    mEdit1_dialog.setText(eleven1);
                } else {
                    if (month == 11) {
                        //          mEdit.setText(year + " " + 11 + " " + day);
                        mEdit_dialog.setText(year + " " + 11 + " " + day);
                        eleven = day + " " + "Nov" + " " + year;
                        //          mEdit1.setText(eleven);
                        mEdit1_dialog.setText(eleven);
                    }
                }

                if (month == 12 && day < 10) {
                    //      mEdit.setText(year + " " + 12 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + 12 + " " + "0" + day);
                    twelve1 = "0" + day + " " + "Dec" + " " + year;
                    //      mEdit1.setText(twelve1);
                    mEdit1_dialog.setText(twelve1);
                } else {
                    if (month == 12) {
                        //         mEdit.setText(year + " " + 12 + " " + day);
                        mEdit_dialog.setText(year + " " + 12 + " " + day);
                        twelve = day + " " + "Dec" + " " + year;
                        //       mEdit1.setText(twelve);
                        mEdit1_dialog.setText(twelve);
                    }
                }

            }

//            class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
//                @Override
//                public Dialog onCreateDialog(Bundle savedInstanceState) {
//                    final Calendar calendar = Calendar.getInstance();
//                    int yy = calendar.get(Calendar.YEAR);
//                    int mm = calendar.get(Calendar.MONTH);
//                    int dd = calendar.get(Calendar.DAY_OF_MONTH);
//                    return new DatePickerDialog(HomeActivity.this, this, yy, mm, dd);
//                }
//
//
//                @Override
//                public void onDateSet(DatePicker view, int yy, int mm, int dd) {
//                    populateSetDate(yy, mm + 1, dd);
//                }
//            }

        });

        editText_from_day_visible_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(Stock_Transfer_Items_History.this, R.style.timepicker_date_dialog, timePickerListener_open_dialogue, hour, minute,
                        false);

                timePickerDialog.show();
            }
        });

        editText_to_day_visible_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(Stock_Transfer_Items_History.this, R.style.timepicker_date_dialog, timePickerListener_close_dialogue, hour, minute,
                        false);

                timePickerDialog.show();
            }
        });

    }


    class MonthTask extends AsyncTask<String, String, String>{

        int year,month,day;
        String mon="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Calendar cal = Calendar.getInstance();
            day = cal.get(Calendar.DATE);
            month = cal.get(Calendar.MONTH) + 1;
            year = cal.get(Calendar.YEAR);
            populateSetDate(year,month,01);
            populateSetDate_2(year,month,31);
            updateTime_open(0, 1);
            updateTime_close(23, 59);
        }

        @Override
        protected String doInBackground(String... strings) {


            if(month==1){
                mon="Jan";
            }else if(month==2){
                mon="Feb";
            }else if(month==3){
                mon="Mar";
            }else if(month==4){
                mon="Apr";
            }else if(month==5){
                mon="May";
            }else if(month==6){
                mon="Jun";
            }else if(month==7){
                mon="Jul";
            }else if(month==8){
                mon="Aug";
            }else if(month==9){
                mon="Sep";
            }else if(month==10){
                mon="Oct";
            }else if(month==11){
                mon="Nov";
            }else if(month==12){
                mon="Dec";
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tv_dateselecter.setText(1+" "+mon+" - "+day+" "+mon);
            btnok.callOnClick();
        }

        public void populateSetDate(int year, int month, int day) {
            TextView mEdit = (TextView) Stock_Transfer_Items_History.this.findViewById(R.id.editText1);
            TextView mEdit1  = (TextView)Stock_Transfer_Items_History.this.findViewById(R.id.editText11);
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

        public void populateSetDate_2(int year, int month, int day) {
            TextView mEdit = (TextView) Stock_Transfer_Items_History.this.findViewById(R.id.editText2);
            TextView mEdit1  = (TextView)Stock_Transfer_Items_History.this.findViewById(R.id.editText22);
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


    }

    class YearTask extends AsyncTask<String, String, String>{
        String mon="";
        int day,month,year;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Calendar cal = Calendar.getInstance();
            day = cal.get(Calendar.DATE);
            month = cal.get(Calendar.MONTH) + 1;
            year = cal.get(Calendar.YEAR);
            populateSetDate(year,01,01);
            populateSetDate_2(year,month,day);
            updateTime_open(0, 1);
            updateTime_close(23, 59);
        }

        @Override
        protected String doInBackground(String... strings) {

            if(month==1){
                mon="Jan";
            }else if(month==2){
                mon="Feb";
            }else if(month==3){
                mon="Mar";
            }else if(month==4){
                mon="Apr";
            }else if(month==5){
                mon="May";
            }else if(month==6){
                mon="Jun";
            }else if(month==7){
                mon="Jul";
            }else if(month==8){
                mon="Aug";
            }else if(month==9){
                mon="Sep";
            }else if(month==10){
                mon="Oct";
            }else if(month==11){
                mon="Nov";
            }else if(month==12){
                mon="Dec";
            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tv_dateselecter.setText(1+"Jan - "+day+" "+mon);

            btnok.callOnClick();
        }

        public void populateSetDate(int year, int month, int day) {
            TextView mEdit = (TextView) Stock_Transfer_Items_History.this.findViewById(R.id.editText1);
            TextView mEdit1  = (TextView)Stock_Transfer_Items_History.this.findViewById(R.id.editText11);
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

        public void populateSetDate_2(int year, int month, int day) {
            TextView mEdit = (TextView) Stock_Transfer_Items_History.this.findViewById(R.id.editText2);
            TextView mEdit1  = (TextView) Stock_Transfer_Items_History.this.findViewById(R.id.editText22);
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

    }

    class WeekTask extends AsyncTask<String, String, String>{
        String mon="";
        String mon1="";
        //  int dow;
        int day1,day2;
        int month1,month2;
        int year1;
        // int min;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Calendar myDate = Calendar.getInstance(); // set this up however you need it.

            //first day of week
            myDate.set(Calendar.DAY_OF_WEEK, 1);

            year1 = myDate.get(Calendar.YEAR);
            month1 = myDate.get(Calendar.MONTH)+1;
            day1 = myDate.get(Calendar.DAY_OF_MONTH);

          /*  //last day of week
            myDate.set(Calendar.DAY_OF_WEEK, 7);


             month2 = myDate.get(Calendar.MONTH)+1;
             day2 = myDate.get(Calendar.DAY_OF_MONTH);*/

            //dow = myDate.get (Calendar.DAY_OF_WEEK);

            Calendar cal = Calendar.getInstance();
            day2 = cal.get(Calendar.DATE);
            month2 = cal.get(Calendar.MONTH) + 1;
            //year = cal.get(Calendar.YEAR);
            // min=day-dow;

            populateSetDate(year1,month1,day1);
            populateSetDate_2(year1,month2,day2);
            updateTime_open(0, 1);
            updateTime_close(23, 59);
        }

        @Override
        protected String doInBackground(String... strings) {


            if(month1==1){
                mon="Jan";
            }else if(month1==2){
                mon="Feb";
            }else if(month1==3){
                mon="Mar";
            }else if(month1==4){
                mon="Apr";
            }else if(month1==5){
                mon="May";
            }else if(month1==6){
                mon="Jun";
            }else if(month1==7){
                mon="Jul";
            }else if(month1==8){
                mon="Aug";
            }else if(month1==9){
                mon="Sep";
            }else if(month1==10){
                mon="Oct";
            }else if(month1==11){
                mon="Nov";
            }else if(month1==12){
                mon="Dec";
            }


            if(month2==1){
                mon1="Jan";
            }else if(month2==2){
                mon1="Feb";
            }else if(month2==3){
                mon1="Mar";
            }else if(month2==4){
                mon1="Apr";
            }else if(month2==5){
                mon1="May";
            }else if(month2==6){
                mon1="Jun";
            }else if(month2==7){
                mon1="Jul";
            }else if(month2==8){
                mon1="Aug";
            }else if(month2==9){
                mon1="Sep";
            }else if(month2==10){
                mon1="Oct";
            }else if(month2==11){
                mon1="Nov";
            }else if(month2==12){
                mon1="Dec";
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            tv_dateselecter.setText((day1)+" "+mon+" - "+day2+" "+mon1);
            btnok.callOnClick();

        }




        public void populateSetDate(int year, int month, int day) {
            TextView mEdit = (TextView) Stock_Transfer_Items_History.this.findViewById(R.id.editText1);
            TextView mEdit1  = (TextView)Stock_Transfer_Items_History.this.findViewById(R.id.editText11);
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



        public void populateSetDate_2(int year, int month, int day) {
            TextView mEdit = (TextView) Stock_Transfer_Items_History.this.findViewById(R.id.editText2);
            TextView mEdit1  = (TextView)Stock_Transfer_Items_History.this.findViewById(R.id.editText22);
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

    }

    class WorkingTask extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            populateSetDate(year,month,day);
            populateSetDate_2(year,month,day);



            Cursor time_get = db1.rawQuery("SELECT * FROM Working_hours", null);
            if (time_get.moveToFirst()) {

                String five = time_get.getString(5);
                String six = time_get.getString(6);

                String two= time_get.getString(2);
                String four=time_get.getString(4);

                String[] h=five.split(":");
                String[] m=six.split(":");

                updateTime_open(Integer.parseInt(h[0]), Integer.parseInt(h[1]));
                updateTime_close(Integer.parseInt(m[0]), Integer.parseInt(m[1]));

                SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM yyyy");
                final String currentDateandTime2 = sdf3.format(new Date());
                tv_dateselecter.setText(currentDateandTime2+","+two+" - "+currentDateandTime2+","+four);

            }
            time_get.close();

        }

        @Override
        protected String doInBackground(String... strings) {

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            btnok.callOnClick();
        }

        public void populateSetDate(int year, int month, int day) {
            TextView mEdit = (TextView) Stock_Transfer_Items_History.this.findViewById(R.id.editText1);
            TextView mEdit1  = (TextView) Stock_Transfer_Items_History.this.findViewById(R.id.editText11);
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



        public void populateSetDate_2(int year, int month, int day) {
            TextView mEdit = (TextView) Stock_Transfer_Items_History.this.findViewById(R.id.editText2);
            TextView mEdit1  = (TextView)Stock_Transfer_Items_History.this.findViewById(R.id.editText22);
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

    }
    private void updateTime_open(int hours, int mins) {

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

        editText_from_day_visible.setText(aTime);
    }

    private void updateTime_close(int hours, int mins) {

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

        editText_to_day_visible.setText(aTime);
    }


}
