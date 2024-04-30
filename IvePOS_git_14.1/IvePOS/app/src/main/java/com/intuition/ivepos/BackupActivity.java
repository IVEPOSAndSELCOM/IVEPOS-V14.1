package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 1/6/2015.
 */

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.intuition.ivepos.signup.SyncHelperService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Rohithkumar on 1/6/2015.
 */
public class BackupActivity extends Fragment {

    private static final String TAG = "1";
    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;

    byte[] img;

    Switch mySwitch;
    LinearLayout linearLayout1;
    String NAME, NAMEEE, replacecolumnvalue, replacemodifiervalue, replacetaxvalue, replacecategoryvalue;

    TextView mon, tue, wed, thurs, fri, sat, sun;
    TextView twomon, threetue, fourwed, fivethurs, sixfri, sevensat, onesun;
    TextView timeset;
    int year1, month1, day1;


    SimpleDateFormat sdff2, sdff1;
    String currentDateandTimee1;
    String timee1;

    final static int RQS_1 = 1;

    ArrayAdapter<String> arrayAdapter;
    ListView lv;
    TextView backupname;


    LinearLayout linearLayout;


    private int hour;
    private int minute;

    ImageView circle1, tick1, error1;
    String one_one;

    //CoordinatorLayout coordinatorLayout;



    public static final String PACKAGE_NAME = "com.intuition.ivepos";
    public static final String DATABASE_NAME = "mydb_Appdata";
    public static final String DATABASE_NAME1 = "mydb_Salesdata";
    public static final String DATABASE_TABLE = "entryTable";

    /** Contains: /data/data/com.example.app/databases/example.db **/
    private static final File DATA_DIRECTORY_DATABASE =
            new File(Environment.getDataDirectory() +
                    "/data/" + PACKAGE_NAME +
                    "/databases/" + DATABASE_NAME );

    private static final File DATA_DIRECTORY_DATABASE1 =
            new File(Environment.getDataDirectory() +
                    "/data/" + PACKAGE_NAME +
                    "/databases/" + DATABASE_NAME1 );

    String WebserviceUrl;
    String account_selection;


    public BackupActivity(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        final View rootview = inflater.inflate(R.layout.backup, null);


        final Calendar c = Calendar.getInstance();
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);


        linearLayout = (LinearLayout)rootview.findViewById(R.id.restore);

        lv = (ListView)rootview.findViewById(R.id.listView);

        //coordinatorLayout = (CoordinatorLayout)rootview.findViewById(R.id.myCoordinatorLayout);


        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);


        circle1 = (ImageView)rootview.findViewById(R.id.circle);
        tick1 = (ImageView)rootview.findViewById(R.id.tick);
        error1 = (ImageView)rootview.findViewById(R.id.error);

        final CheckBox app_sett_data = (CheckBox)rootview.findViewById(R.id.app_settingsdata);
        final CheckBox sales_data = (CheckBox)rootview.findViewById(R.id.salesdata);

        Button restore1 = (Button)rootview.findViewById(R.id.btnrestore);
        restore1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sdff2 = new SimpleDateFormat("ddMMMyy");
                currentDateandTimee1 = sdff2.format(new Date());

                Date dt = new Date();
                sdff1 = new SimpleDateFormat("hhmmssaa");
                timee1 = sdff1.format(dt);


                if (!app_sett_data.isChecked() && !sales_data.isChecked()){
//                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Select App data or Sales data", Snackbar.LENGTH_LONG).setAction("Ok", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    });
//                    //Snackbar snackbar = Snackbar.make(coordinatorLayout, "Select App data or Sales data", Snackbar.LENGTH_LONG);
//                    snackbar.setActionTextColor(Color.YELLOW);
//                    View snackbarView = snackbar.getView();
//                    TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
//                    textView.setTextColor(Color.WHITE);
//                    snackbar.show();
//                    circle1.setVisibility(View.VISIBLE);
//                    tick1.setVisibility(View.INVISIBLE);
//                    error1.setVisibility(View.INVISIBLE);
//                    Toast.makeText(getActivity(), "Select options", Toast.LENGTH_SHORT).show();

                }else {
                    if (app_sett_data.isChecked() && sales_data.isChecked()){
                        if (!SdIsPresent()) ;

                        DownloadMusicfromInternet3 downloadMusicfromInternet = new DownloadMusicfromInternet3();
                        downloadMusicfromInternet.execute();


                    }else {

                        if (app_sett_data.isChecked()) {
                            if (!SdIsPresent()) ;

                            DownloadMusicfromInternet4 downloadMusicfromInternet = new DownloadMusicfromInternet4();
                            downloadMusicfromInternet.execute();


                        } else {
                            if (!SdIsPresent()) ;

                            DownloadMusicfromInternet5 downloadMusicfromInternet = new DownloadMusicfromInternet5();
                            downloadMusicfromInternet.execute();


                        }
                    }
                }


                List<String> your_array_list = new ArrayList<String>();
//                String path = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_backup/";
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/IVEPOS_backup/";

                File f = new File(path);
                File[] files = f.listFiles();
                for (File inFile : files) {
                    if (inFile.isDirectory()) {
                        // in here, you can add directory names into an ArrayList and populate your ListView.
                        your_array_list.add(inFile.getName());
                        //Toast.makeText(getActivity(), "file anmem is "+inFile.getName(), Toast.LENGTH_SHORT).show();
                    }
                }

                arrayAdapter = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        your_array_list );

                lv.setAdapter(arrayAdapter);

            }
        });


        final LinearLayout restorelayout = (LinearLayout)rootview.findViewById(R.id.restore);


        Button cancel = (Button)rootview.findViewById(R.id.cancelside);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.INVISIBLE);
            }
        });

        ImageView into = (ImageView)rootview.findViewById(R.id.closetext);
        into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.INVISIBLE);
            }
        });


        lv = (ListView)rootview.findViewById(R.id.listView);
        backupname = (TextView)rootview.findViewById(R.id.editText1);

        final List<String> your_array_list = new ArrayList<String>();
//        String path = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_backup/";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/IVEPOS_backup/";


        File f = new File(path);

        if (!f.exists()) {
            f.mkdirs();
        }

        File[] files = f.listFiles();
        for (File inFile : files) {
            if (inFile.isDirectory()) {
                // in here, you can add directory names into an ArrayList and populate your ListView.
                your_array_list.add(inFile.getName());
                Date lastmodify = new Date(inFile.lastModified());
                //Toast.makeText(getActivity(), "file anmem issssssssss "+inFile.getName()+" modified "+lastmodify, Toast.LENGTH_SHORT).show();
            }else {
                //Toast.makeText(getActivity(), "no files", Toast.LENGTH_SHORT).show();
            }
        }

        arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                your_array_list );

        lv.setAdapter(arrayAdapter);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String selectedFromList = (String) lv.getItemAtPosition(position);
                restorelayout.setVisibility(View.VISIBLE);
                //Toast.makeText(getActivity(), "selected " + selectedFromList, Toast.LENGTH_SHORT).show();
                backupname.setText(selectedFromList);

                List<String> your_array_list1 = new ArrayList<String>();

//                String path = Environment.getExternalStorageDirectory().toString() + "/IVEPOS_backup/" + selectedFromList;
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/IVEPOS_backup/" + selectedFromList;
                //Toast.makeText(MainActivity1.this, " "+path, Toast.LENGTH_LONG).show();

                File f = new File(path);
                File[] files = f.listFiles();
                int count = 0;
                for (final File inFile : files) {
                    String filenamee = inFile.getName();
                    count++;
                    your_array_list1.add(inFile.getName());


//                    Toast.makeText(getActivity(), " hii " + your_array_list1, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity(), "count is "+count , Toast.LENGTH_SHORT).show();
//                    TextView filename = (TextView) rootview.findViewById(R.id.filename);
//                    filename.setText(your_array_list1.toString());
                    if (count == 2) {
                        if (your_array_list1.toString().equals("[mydb_Appdata, mydb_Salesdata]") || your_array_list1.toString().equals("[mydb_Salesdata, mydb_Appdata]")) {
//                            Toast.makeText(getActivity(), " 2 file exists", Toast.LENGTH_SHORT).show();
                            final CheckBox app_sett_data1 = (CheckBox) rootview.findViewById(R.id.app_settingsdata1);
                            final CheckBox sales_data1 = (CheckBox) rootview.findViewById(R.id.salesdata1);
                            app_sett_data1.setChecked(true);
                            sales_data1.setChecked(true);
//                            sales_data1.setClickable(true);
//                            app_sett_data1.setClickable(true);
                            sales_data1.setVisibility(View.VISIBLE);
                            app_sett_data1.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (your_array_list1.toString().equals("[mydb_Salesdata]")) {
//                            Toast.makeText(getActivity(), "sales data exists", Toast.LENGTH_SHORT).show();
                            final CheckBox app_sett_data1 = (CheckBox) rootview.findViewById(R.id.app_settingsdata1);
                            final CheckBox sales_data1 = (CheckBox) rootview.findViewById(R.id.salesdata1);
                            sales_data1.setChecked(true);
                            app_sett_data1.setChecked(false);
//                            sales_data1.setClickable(true);
//                            app_sett_data1.setClickable(false);
                            sales_data1.setVisibility(View.VISIBLE);
                            app_sett_data1.setVisibility(View.GONE);
                        } else {
                            if (your_array_list1.toString().equals("[mydb_Appdata]")) {
//                                Toast.makeText(getActivity(), "App data exists", Toast.LENGTH_SHORT).show();
                                final CheckBox app_sett_data1 = (CheckBox) rootview.findViewById(R.id.app_settingsdata1);
                                final CheckBox sales_data1 = (CheckBox) rootview.findViewById(R.id.salesdata1);
                                app_sett_data1.setChecked(true);
                                sales_data1.setChecked(false);
//                                app_sett_data1.setClickable(true);
//                                sales_data1.setClickable(false);
                                app_sett_data1.setVisibility(View.VISIBLE);
                                sales_data1.setVisibility(View.GONE);
                            }
                        }
                    }

                    Button restore = (Button) rootview.findViewById(R.id.restorenow);
                    restore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final CheckBox app_sett_data11 = (CheckBox) rootview.findViewById(R.id.app_settingsdata1);
                            final CheckBox sales_data11 = (CheckBox) rootview.findViewById(R.id.salesdata1);
                            if (!app_sett_data11.isChecked() && !sales_data11.isChecked()){
                                // Toast.makeText(getActivity(), "not selected", Toast.LENGTH_SHORT).show();
                                final Dialog dialog = new Dialog(getActivity(), R.style.notitle);
                                dialog.setContentView(R.layout.dialog_restore_checkboxnot_sele);
                                dialog.show();

                                Button cancelq = (Button)dialog.findViewById(R.id.cancel);
                                cancelq.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                            }else {
                                final Dialog dialog = new Dialog(getActivity(), R.style.notitle);
                                dialog.setContentView(R.layout.dialog_restore_warning);
                                dialog.show();

                                TextView filenameq = (TextView)dialog.findViewById(R.id.filename);
                                filenameq.setText("'"+backupname.getText().toString()+"'");

                                Button cancelq = (Button)dialog.findViewById(R.id.cancel);
                                cancelq.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                Button restoreq = (Button)dialog.findViewById(R.id.ok);
                                restoreq.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final CheckBox app_sett_data1 = (CheckBox) rootview.findViewById(R.id.app_settingsdata1);
                                        final CheckBox sales_data1 = (CheckBox) rootview.findViewById(R.id.salesdata1);
                                        if (app_sett_data1.isChecked() && sales_data1.isChecked()) {
                                            //Toast.makeText(getActivity(), "Both data replace", Toast.LENGTH_SHORT).show();

                                            DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
                                            downloadMusicfromInternet.execute();


                                            linearLayout.setVisibility(View.INVISIBLE);

                                        } else {
                                            if (app_sett_data1.isChecked()) {

                                                DownloadMusicfromInternet1 downloadMusicfromInternet = new DownloadMusicfromInternet1();
                                                downloadMusicfromInternet.execute();

                                                Toast.makeText(getActivity(), "Only app data replace", Toast.LENGTH_SHORT).show();

                                                //importDB();



                                                linearLayout.setVisibility(View.INVISIBLE);

                                            } else {

                                                DownloadMusicfromInternet2 downloadMusicfromInternet = new DownloadMusicfromInternet2();
                                                downloadMusicfromInternet.execute();

                                                if (sales_data1.isChecked()) {
                                                    //Toast.makeText(getActivity(), "Only sales data replace", Toast.LENGTH_SHORT).show();


                                                    linearLayout.setVisibility(View.INVISIBLE);
                                                } else {
                                                    //Toast.makeText(getActivity(), "no data to replace", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                        dialog.dismiss();

//                                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//                                        Cursor items = db.rawQuery("SELECT * FROM Items", null);
//                                        if (items.moveToFirst()){
//                                            do {
//                                                String id = items.getString(0);
//                                                String name = items.getString(1);
//                                                byte[] image = items.getBlob(6);
//                                                String cate = items.getString(4);
//                                                String tax = items.getString(5);
//
//                                                if (image == null) {
//                                                    //Toast.makeText(getActivity(), "no image for "+id, Toast.LENGTH_SHORT).show();
//                                                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.item_bg_image2);
//                                                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                                                    b.compress(Bitmap.CompressFormat.PNG, 100, bos);
//                                                    img = bos.toByteArray();
//                                                    ContentValues contentValues = new ContentValues();
//                                                    contentValues.put("image", img);
//                                                    String where = "_id = '" + id + "'";
//                                                    db.update("Items", contentValues, where, new String[]{});
//
//                                                }
//                                            }while (items.moveToNext());
//
//                                        }
//
//                                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//                                        Cursor items1 = db.rawQuery("SELECT * FROM Items", null);
//                                        if (items1.moveToFirst()){
//                                            do {
//                                                String id = items1.getString(0);
//                                                String name = items1.getString(1);
//                                                byte[] image = items1.getBlob(6);
//                                                String cate = items1.getString(4);
//                                                String tax = items1.getString(5);
//
//                                                TextView textView = new TextView(getActivity());
//                                                textView.setText(cate);
//                                                TextView textView1 = new TextView(getActivity());
//                                                textView1.setText(tax);
//
//                                                //Toast.makeText(getActivity(), "category "+cate+" tax is "+tax, Toast.LENGTH_SHORT).show();
//
//                                                if (textView.getText().toString().equals("null") || textView.getText().toString().equals("")){
//                                                    //Toast.makeText(getActivity(), "no category for "+id, Toast.LENGTH_SHORT).show();
//                                                    ContentValues contentValues1 = new ContentValues();
//                                                    contentValues1.put("category", "None");
//                                                    String where1 = "_id = '" + id + "'";
//                                                    db.update("Items", contentValues1, where1, new String[]{});
//                                                }else {
//                                                    //Toast.makeText(getActivity(), "category for "+id+" is "+cate, Toast.LENGTH_SHORT).show();
//                                                }
//                                                if (textView1.getText().toString().equals("null") || textView1.getText().toString().equals("")){
//                                                    //Toast.makeText(getActivity(), "no tax for "+id, Toast.LENGTH_SHORT).show();
//                                                    ContentValues contentValues2 = new ContentValues();
//                                                    contentValues2.put("itemtax", "None");
//                                                    String where2 = "_id = '"+id+"'";
//                                                    db.update("Items", contentValues2, where2, new String[]{});
//                                                }else {
//                                                    //Toast.makeText(getActivity(), "tax for "+id+" is "+tax, Toast.LENGTH_SHORT).show();
//                                                }
//
//
//                                            }while (items1.moveToNext());
//
//                                        }


                                    }
                                });
                            }


                        }
                    });
                }

            }
        });


        return rootview;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.backup_schedule_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_schedule:
                linearLayout.setVisibility(View.INVISIBLE);
                final Dialog dialog = new Dialog(getActivity(), R.style.notitle);
                dialog.setContentView(R.layout.dialog_backup_schedule_freq_time);
                dialog.show();

                mySwitch = (Switch)dialog.findViewById(R.id.mySwitch);
                linearLayout1 = (LinearLayout)dialog.findViewById(R.id.linearLayout);

                timeset = (TextView)dialog.findViewById(R.id.timeget);

                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                Cursor allrowss = db.rawQuery("SELECT * FROM Alaramon_off WHERE _id = '1'", null);
                if (allrowss.moveToFirst()) {
                    do {
                        NAME = allrowss.getString(1);
                    } while (allrowss.moveToNext());
                }
                allrowss.close();

                if (NAME.toString().equals("On")){
                    mySwitch.setChecked(true);
                }

                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                Cursor aallrowss = db.rawQuery("SELECT * FROM Alaramtime WHERE _id = '1'", null);
                if (aallrowss.moveToFirst()) {
                    do {
                        NAMEEE = aallrowss.getString(1);
                    } while (aallrowss.moveToNext());
                }
                aallrowss.close();
                timeset.setText(NAMEEE);




                mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        //Toast.makeText(getActivity(), "12", Toast.LENGTH_SHORT).show();

                        if (isChecked) {

                            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                            Cursor allrowss = db.rawQuery("SELECT * FROM Alaramon_off WHERE _id = '1'", null);
                            if (allrowss.moveToFirst()) {
                                do {
                                    NAME = allrowss.getString(1);
                                } while (allrowss.moveToNext());
                            }
                            allrowss.close();

                            if (NAME.toString().equals("On")) {
                                SQLiteDatabase myyDb1 = getActivity().openOrCreateDatabase("mydb_Appdata",
                                        Context.MODE_PRIVATE, null);
                                ContentValues newValues = new ContentValues();
                                newValues.put("status", "Off");
                                String where = "_id = '1'";
                                myyDb1.update("Alaramon_off", newValues, where, new String[]{});
                                myyDb1.close();
                            } else {
                                SQLiteDatabase myyDb1 = getActivity().openOrCreateDatabase("mydb_Appdata",
                                        Context.MODE_PRIVATE, null);
                                ContentValues newValues = new ContentValues();
                                newValues.put("status", "On");
                                String where = "_id = '1'";
                                myyDb1.update("Alaramon_off", newValues, where, new String[]{});
                                myyDb1.close();
                            }


                        } else {
                            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                            Cursor allrowss = db.rawQuery("SELECT * FROM Alaramon_off WHERE _id = '1'", null);
                            if (allrowss.moveToFirst()) {
                                do {
                                    NAME = allrowss.getString(1);
                                } while (allrowss.moveToNext());
                            }
                            allrowss.close();

                            if (NAME.toString().equals("On")) {
                                SQLiteDatabase myyDb1 = getActivity().openOrCreateDatabase("mydb_Appdata",
                                        Context.MODE_PRIVATE, null);
                                ContentValues newValues = new ContentValues();
                                newValues.put("status", "Off");
                                String where = "_id = '1'";
                                myyDb1.update("Alaramon_off", newValues, where, new String[]{});
                                myyDb1.close();
                            } else {
                                SQLiteDatabase myyDb1 = getActivity().openOrCreateDatabase("mydb_Appdata",
                                        Context.MODE_PRIVATE, null);
                                ContentValues newValues = new ContentValues();
                                newValues.put("status", "On");
                                String where = "_id = '1'";
                                myyDb1.update("Alaramon_off", newValues, where, new String[]{});
                                myyDb1.close();
                            }
                        }


                    }
                });


                sunday(dialog);
                monday(dialog);
                tuesday(dialog);
                wednesday(dialog);
                thursday(dialog);
                friday(dialog);
                saturday(dialog);



                timeset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Calendar c = Calendar.getInstance();
//                        int hours = c.get(Calendar.HOUR);
//                        int minutes = c.get(Calendar.MINUTE);
//                        int seconds = c.get(Calendar.SECOND);
//                        Toast.makeText(getActivity(), " date is "+hours+":"+minutes+":"+seconds, Toast.LENGTH_LONG).show();
//                        com.wdullaer.materialdatetimepicker.time.TimePickerDialog dpd = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
//                                datePickerListener,c.get(Calendar.HOUR), c.get(Calendar.MINUTE), false
//                        );
//
//                        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");


                        android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(getActivity(), R.style.timepicker_date_dialog, timePickerListener, hour, minute,
                                false);

                        timePickerDialog.show();


//                        Calendar now = Calendar.getInstance();
//                        TimePickerDialog tpd = TimePickerDialog.newInstance(
//                                datePickerListener1,
//                                now.get(Calendar.HOUR_OF_DAY),
//                                now.get(Calendar.MINUTE),
//                                false
//                        );
//
//                        tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
                    }
                });


                Button cancel1 = (Button)dialog.findViewById(R.id.cancel);
                cancel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Button timesave = (Button)dialog.findViewById(R.id.savetime);
                timesave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String time = timeset.getText().toString();
                        //String time =  datePickerListener.getTime();
                        //Toast.makeText(getActivity(), "time is "+time, Toast.LENGTH_SHORT).show();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("time", time);
                        String where1 = "_id = '1' ";
                        db.update("Alaramtime", contentValues, where1, new String[]{});




                        dialog.dismiss();
                    }
                });

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        getActivity().finish();
//    }

    private android.app.TimePickerDialog.OnTimeSetListener timePickerListener = new android.app.TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour   = hourOfDay;
            minute = minutes;

            updateTime(hour,minute);

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minutes);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);



            final Cursor one = db.rawQuery("SELECT * FROM Alaramdays where _id ='1' ;", null);
            if (one.moveToFirst()){
                String onee = one.getString(1);
                if (onee.toString().equals("Monday")){
                    if(calSet.compareTo(calNow) <= 0){
                        //Today Set time passed, count to tomorrow
                        calSet.add(Calendar.DATE, 1);

                        //Toast.makeText(getActivity(), " monday", Toast.LENGTH_SHORT).show();
                    }
                    setAlarm(calSet);

                    //Toast.makeText(getActivity(), " monday is selected ", Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getActivity(), " monday is not selected ", Toast.LENGTH_SHORT).show();
                }
            }
            one.close();

            final Cursor two = db.rawQuery("SELECT * FROM Alaramdays where _id ='2' ;", null);
            if (two.moveToFirst()){
                String twoo = two.getString(1);
                if (twoo.toString().equals("Tuesday")){
                    if(calSet.compareTo(calNow) <= 0){
                        //Today Set time passed, count to tomorrow
                        calSet.add(Calendar.DATE, 1);

                        //Toast.makeText(getActivity(), " Tuesday", Toast.LENGTH_SHORT).show();
                    }
                    setAlarm(calSet);

                    //Toast.makeText(getActivity(), " Tuesday is selected ", Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getActivity(), " Tuesday is not selected ", Toast.LENGTH_SHORT).show();
                }
            }
            two.close();

            final Cursor three = db.rawQuery("SELECT * FROM Alaramdays where _id ='3' ;", null);
            if (three.moveToFirst()){
                String threeo = three.getString(1);
                if (threeo.toString().equals("Wednesday")){
                    if(calSet.compareTo(calNow) <= 0){
                        //Today Set time passed, count to tomorrow
                        calSet.add(Calendar.DATE, 1);

                        //Toast.makeText(getActivity(), " Wednesday", Toast.LENGTH_SHORT).show();
                    }
                    setAlarm(calSet);

                    //Toast.makeText(getActivity(), " Wednesday is selected ", Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getActivity(), " Wednesday is not selected ", Toast.LENGTH_SHORT).show();
                }
            }
            three.close();

            final Cursor four = db.rawQuery("SELECT * FROM Alaramdays where _id ='4' ;", null);
            if (four.moveToFirst()){
                String fouro = four.getString(1);
                if (fouro.toString().equals("Thursday")){
                    if(calSet.compareTo(calNow) <= 0){
                        //Today Set time passed, count to tomorrow
                        calSet.add(Calendar.DATE, 1);

                        //Toast.makeText(getActivity(), " Thursday", Toast.LENGTH_SHORT).show();
                    }
                    setAlarm(calSet);

                    //Toast.makeText(getActivity(), " Thursday is selected ", Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getActivity(), " Thursday is not selected ", Toast.LENGTH_SHORT).show();
                }
            }
            four.close();

            final Cursor five = db.rawQuery("SELECT * FROM Alaramdays where _id ='5' ;", null);
            if (five.moveToFirst()){
                String fivee = five.getString(1);
                if (fivee.toString().equals("Friday")){
                    if(calSet.compareTo(calNow) <= 0){
                        //Today Set time passed, count to tomorrow
                        calSet.add(Calendar.DATE, 1);

                        // Toast.makeText(getActivity(), " Friday", Toast.LENGTH_SHORT).show();
                    }
                    setAlarm(calSet);

                    //Toast.makeText(getActivity(), " Friday is selected ", Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getActivity(), " Friday is not selected ", Toast.LENGTH_SHORT).show();
                }
            }
            five.close();

            final Cursor six = db.rawQuery("SELECT * FROM Alaramdays where _id ='6' ;", null);
            if (six.moveToFirst()){
                String sixx = six.getString(1);
                if (sixx.toString().equals("Saturday")){
                    if(calSet.compareTo(calNow) <= 0){
                        //Today Set time passed, count to tomorrow
                        calSet.add(Calendar.DATE, 1);

                        //Toast.makeText(getActivity(), " Saturday", Toast.LENGTH_SHORT).show();
                    }
                    setAlarm(calSet);

                    //Toast.makeText(getActivity(), " Saturday is selected ", Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getActivity(), " Saturday is not selected ", Toast.LENGTH_SHORT).show();
                }
            }
            six.close();

            final Cursor seven = db.rawQuery("SELECT * FROM Alaramdays where _id ='7' ;", null);
            if (seven.moveToFirst()){
                String sevenn = seven.getString(1);
                if (sevenn.toString().equals("Sunday")){
                    if(calSet.compareTo(calNow) <= 0){
                        //Today Set time passed, count to tomorrow
                        calSet.add(Calendar.DATE, 1);

                        //Toast.makeText(getActivity(), " Sunday", Toast.LENGTH_SHORT).show();
                    }
                    setAlarm(calSet);

                    //Toast.makeText(getActivity(), " Sunday is selected ", Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getActivity(), " Sunday is not selected ", Toast.LENGTH_SHORT).show();
                }
            }
            seven.close();

        }

    };

    private void setAlarm(Calendar targetCal){

        //Toast.makeText(getActivity(), " 1", Toast.LENGTH_SHORT).show();

//        JobScheduler jobScheduler = (JobScheduler) getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        ComponentName componentName = new ComponentName(getActivity(), Upload_Task.class);
//        JobInfo jobInfo = new JobInfo.Builder(1, componentName).setOverrideDeadline(10).setRequiresCharging(true).build();
//        jobScheduler.schedule(jobInfo);

        Intent intent = new Intent(getActivity(), AlarmReceiver1.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), RQS_1, intent, 0);
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(getActivity(), RQS_1, intent, PendingIntent.FLAG_MUTABLE);
        }
        else {
            pendingIntent = PendingIntent.getActivity(getActivity(), RQS_1, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

//        Intent intent = new Intent(getActivity(), AlarmReceiver1.class);
//        intent.setAction("packagename.ACTION");
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),
//                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
//        alarm.cancel(pendingIntent);
//        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }


    private void setAlarm11(Calendar targetCal){

        //Toast.makeText(getActivity(), " 1", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), AlarmReceiver111.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), RQS_1, intent, 0);
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(getActivity(), RQS_1, intent, PendingIntent.FLAG_MUTABLE);
        }
        else {
            pendingIntent = PendingIntent.getActivity(getActivity(), RQS_1, intent, PendingIntent.FLAG_MUTABLE);
        }
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), 0, pendingIntent);

    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins) {

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

        timeset.setText(aTime);
    }

    private void sunday(final Dialog dialog) {
        sun = (TextView)dialog.findViewById(R.id.selectsunday);
        onesun = (TextView)dialog.findViewById(R.id.one);
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        final Cursor allrows = db.rawQuery("SELECT * FROM Alaramdays where _id ='7' ;", null);
        allrows.moveToFirst();
        while (allrows.isAfterLast() == false) {
            if (allrows.getString(1).equals("Sunday")) {
                sun.setBackgroundColor(getResources().getColor(R.color.green));
                sun.setTextColor(getResources().getColor(R.color.white));
                onesun.setText(" Sun");
                sun.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sun.setBackgroundColor(getResources().getColor(R.color.white));
                        sun.setTextColor(getResources().getColor(R.color.black));
                        //mon.setBackgroundResource(R.drawable.category_square);
                        //check how many times clicked and so on
                        //Toast.makeText(getActivity(), "Sunday is Weekend", Toast.LENGTH_SHORT).show();
                        onesun.setText("");
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Alaramdays SET swap =(SELECT selecteddays FROM Alaramdays WHERE _id = '7') where _id ='7' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Alaramdays SET selecteddays =(SELECT unselecteddays FROM Alaramdays WHERE _id = '7') where _id ='7' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Alaramdays SET unselecteddays =(SELECT swap FROM Alaramdays WHERE _id = '7') where _id ='7' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows.close();
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows1.close();
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();
                        }
                        allrows2.close();
                        sunday(dialog);
                    }
                });



            }
            else{
                sun.setBackgroundColor(getResources().getColor(R.color.white));
                sun.setTextColor(getResources().getColor(R.color.black));
                onesun.setText("");
                sun.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sun.setBackgroundColor(getResources().getColor(R.color.green));
                        //Toast.makeText(getActivity(), "Sunday is Weekday!", Toast.LENGTH_SHORT).show();
                        onesun.setText(" Sun");
                        sun.setTextColor(getResources().getColor(R.color.white));
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Alaramdays SET swap =(SELECT selecteddays FROM Alaramdays WHERE _id = '7') where _id ='7' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Alaramdays SET selecteddays =(SELECT unselecteddays FROM Alaramdays WHERE _id = '7') where _id ='7' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Alaramdays SET unselecteddays =(SELECT swap FROM Alaramdays WHERE _id = '7') where _id ='7' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows.close();
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows1.close();
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();

                        }
                        allrows2.close();
                        sunday(dialog);
                    }
                });
            }
            allrows.moveToNext();
        }

    }

    private void monday(final Dialog dialog) {
        mon = (TextView)dialog.findViewById(R.id.selectmonday);
        twomon = (TextView)dialog.findViewById(R.id.two);
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        final Cursor allrows = db.rawQuery("SELECT * FROM Alaramdays where _id ='1' ;", null);
        allrows.moveToFirst();
        while (allrows.isAfterLast() == false) {
            if (allrows.getString(1).equals("Monday")) {
                mon.setBackgroundColor(getResources().getColor(R.color.green));
                mon.setTextColor(getResources().getColor(R.color.white));
                twomon.setText(" Mon");
                mon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mon.setBackgroundColor(getResources().getColor(R.color.white));
                        //mon.setBackgroundResource(R.drawable.category_square);
                        //check how many times clicked and so on
                        mon.setTextColor(getResources().getColor(R.color.black));
                        //Toast.makeText(getActivity(), "Monday is Weekend", Toast.LENGTH_SHORT).show();
                        twomon.setText("");
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Alaramdays SET swap =(SELECT selecteddays FROM Alaramdays WHERE _id = '1') where _id ='1' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Alaramdays SET selecteddays =(SELECT unselecteddays FROM Alaramdays WHERE _id = '1') where _id ='1' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Alaramdays SET unselecteddays =(SELECT swap FROM Alaramdays WHERE _id = '1') where _id ='1' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows.close();
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows1.close();
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();
                        }
                        allrows2.close();
                        monday(dialog);
                    }
                });



            }
            else{
                mon.setBackgroundColor(getResources().getColor(R.color.white));
                twomon.setText("");
                mon.setTextColor(getResources().getColor(R.color.black));
                mon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mon.setBackgroundColor(getResources().getColor(R.color.green));
                        //Toast.makeText(getActivity(), "Monday is Weekday!", Toast.LENGTH_SHORT).show();
                        twomon.setText(" Mon");
                        mon.setTextColor(getResources().getColor(R.color.white));
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Alaramdays SET swap =(SELECT selecteddays FROM Alaramdays WHERE _id = '1') where _id ='1' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Alaramdays SET selecteddays =(SELECT unselecteddays FROM Alaramdays WHERE _id = '1') where _id ='1' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Alaramdays SET unselecteddays =(SELECT swap FROM Alaramdays WHERE _id = '1') where _id ='1' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows.close();
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows1.close();
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();

                        }
                        allrows2.close();
                        monday(dialog);
                    }
                });
            }
            allrows.moveToNext();
        }

    }


    private void tuesday(final Dialog dialog) {
        tue = (TextView)dialog.findViewById(R.id.selecttuesday);
        threetue = (TextView)dialog.findViewById(R.id.three);
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        final Cursor allrows = db.rawQuery("SELECT * FROM Alaramdays where _id ='2' ;", null);
        allrows.moveToFirst();
        while (allrows.isAfterLast() == false) {
            if (allrows.getString(1).equals("Tuesday")) {
                tue.setBackgroundColor(getResources().getColor(R.color.green));
                threetue.setText(" Tues");
                tue.setTextColor(getResources().getColor(R.color.white));
                tue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tue.setBackgroundColor(getResources().getColor(R.color.white));
                        //tue.setBackgroundResource(R.drawable.category_square);
                        //check how many times clicked and so on
                        tue.setTextColor(getResources().getColor(R.color.black));
                        //Toast.makeText(getActivity(), "Tuesday is Weekend", Toast.LENGTH_SHORT).show();
                        threetue.setText("");
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Alaramdays SET swap =(SELECT selecteddays FROM Alaramdays WHERE _id = '2') where _id ='2' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Alaramdays SET selecteddays =(SELECT unselecteddays FROM Alaramdays WHERE _id = '2') where _id ='2' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Alaramdays SET unselecteddays =(SELECT swap FROM Alaramdays WHERE _id = '2') where _id ='2' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows.close();
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows1.close();
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();
                        }
                        allrows2.close();
                        tuesday(dialog);
                    }
                });



            }
            else{
                tue.setBackgroundColor(getResources().getColor(R.color.white));
                tue.setTextColor(getResources().getColor(R.color.black));
                threetue.setText("");
                tue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tue.setBackgroundColor(getResources().getColor(R.color.green));
                        tue.setTextColor(getResources().getColor(R.color.white));
                        //Toast.makeText(getActivity(), "Tuesday is Weekday!", Toast.LENGTH_SHORT).show();
                        threetue.setText(" Tues");
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Alaramdays SET swap =(SELECT selecteddays FROM Alaramdays WHERE _id = '2') where _id ='2' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Alaramdays SET selecteddays =(SELECT unselecteddays FROM Alaramdays WHERE _id = '2') where _id ='2' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Alaramdays SET unselecteddays =(SELECT swap FROM Alaramdays WHERE _id = '2') where _id ='2' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows.close();
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows1.close();
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();

                        }
                        allrows2.close();
                        tuesday(dialog);
                    }
                });
            }
            allrows.moveToNext();
        }

    }


    private void wednesday(final Dialog dialog) {
        wed = (TextView)dialog.findViewById(R.id.selectwednesday);
        fourwed = (TextView)dialog.findViewById(R.id.four);
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        final Cursor allrows = db.rawQuery("SELECT * FROM Alaramdays where _id ='3' ;", null);
        allrows.moveToFirst();
        while (allrows.isAfterLast() == false) {
            if (allrows.getString(1).equals("Wednesday")) {
                wed.setBackgroundColor(getResources().getColor(R.color.green));
                fourwed.setText(" Wed");
                wed.setTextColor(getResources().getColor(R.color.white));
                wed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wed.setBackgroundColor(getResources().getColor(R.color.white));
                        //tue.setBackgroundResource(R.drawable.category_square);
                        //check how many times clicked and so on
                        wed.setTextColor(getResources().getColor(R.color.black));
                        //Toast.makeText(getActivity(), "Wednesday is Weekend", Toast.LENGTH_SHORT).show();
                        fourwed.setText("");
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Alaramdays SET swap =(SELECT selecteddays FROM Alaramdays WHERE _id = '3') where _id ='3' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Alaramdays SET selecteddays =(SELECT unselecteddays FROM Alaramdays WHERE _id = '3') where _id ='3' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Alaramdays SET unselecteddays =(SELECT swap FROM Alaramdays WHERE _id = '3') where _id ='3' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows.close();
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows1.close();
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();
                        }
                        allrows2.close();
                        wednesday(dialog);
                    }
                });



            }
            else{
                wed.setBackgroundColor(getResources().getColor(R.color.white));
                wed.setTextColor(getResources().getColor(R.color.black));
                fourwed.setText("");
                wed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wed.setBackgroundColor(getResources().getColor(R.color.green));
                        wed.setTextColor(getResources().getColor(R.color.white));
                        //Toast.makeText(getActivity(), "Wednesday is Weekday!", Toast.LENGTH_SHORT).show();
                        fourwed.setText(" Wed");
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Alaramdays SET swap =(SELECT selecteddays FROM Alaramdays WHERE _id = '3') where _id ='3' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Alaramdays SET selecteddays =(SELECT unselecteddays FROM Alaramdays WHERE _id = '3') where _id ='3' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Alaramdays SET unselecteddays =(SELECT swap FROM Alaramdays WHERE _id = '3') where _id ='3' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows.close();
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows1.close();
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();

                        }
                        allrows2.close();
                        wednesday(dialog);
                    }
                });
            }
            allrows.moveToNext();
        }

    }

    private void thursday(final Dialog dialog) {
        thurs = (TextView)dialog.findViewById(R.id.selectthursday);
        fivethurs = (TextView)dialog.findViewById(R.id.five);
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        final Cursor allrows = db.rawQuery("SELECT * FROM Alaramdays where _id ='4' ;", null);
        allrows.moveToFirst();
        while (allrows.isAfterLast() == false) {
            if (allrows.getString(1).equals("Thursday")) {
                thurs.setBackgroundColor(getResources().getColor(R.color.green));
                fivethurs.setText(" Thurs");
                thurs.setTextColor(getResources().getColor(R.color.white));
                thurs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        thurs.setBackgroundColor(getResources().getColor(R.color.white));
                        //tue.setBackgroundResource(R.drawable.category_square);
                        //check how many times clicked and so on
                        thurs.setTextColor(getResources().getColor(R.color.black));
                        //Toast.makeText(getActivity(), "Thursday is Weekend", Toast.LENGTH_SHORT).show();
                        fivethurs.setText("");
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Alaramdays SET swap =(SELECT selecteddays FROM Alaramdays WHERE _id = '4') where _id ='4' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Alaramdays SET selecteddays =(SELECT unselecteddays FROM Alaramdays WHERE _id = '4') where _id ='4' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Alaramdays SET unselecteddays =(SELECT swap FROM Alaramdays WHERE _id = '4') where _id ='4' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows.close();
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows1.close();
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();
                        }
                        allrows2.close();
                        thursday(dialog);
                    }
                });



            }
            else{
                thurs.setBackgroundColor(getResources().getColor(R.color.white));
                thurs.setTextColor(getResources().getColor(R.color.black));
                fivethurs.setText("");
                thurs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        thurs.setBackgroundColor(getResources().getColor(R.color.green));
                        thurs.setTextColor(getResources().getColor(R.color.white));
                        //Toast.makeText(getActivity(), "Wednesday is Weekday!", Toast.LENGTH_SHORT).show();
                        fivethurs.setText(" Thurs");
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Alaramdays SET swap =(SELECT selecteddays FROM Alaramdays WHERE _id = '4') where _id ='4' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Alaramdays SET selecteddays =(SELECT unselecteddays FROM Alaramdays WHERE _id = '4') where _id ='4' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Alaramdays SET unselecteddays =(SELECT swap FROM Alaramdays WHERE _id = '4') where _id ='4' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows.close();
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows1.close();
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();

                        }
                        allrows2.close();
                        thursday(dialog);
                    }
                });
            }
            allrows.moveToNext();
        }

    }

    private void friday(final Dialog dialog) {
        fri = (TextView)dialog.findViewById(R.id.selectfriday);
        sixfri = (TextView)dialog.findViewById(R.id.six);
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        final Cursor allrows = db.rawQuery("SELECT * FROM Alaramdays where _id ='5' ;", null);
        allrows.moveToFirst();
        while (allrows.isAfterLast() == false) {
            if (allrows.getString(1).equals("Friday")) {
                fri.setBackgroundColor(getResources().getColor(R.color.green));
                sixfri.setText(" Fri");
                fri.setTextColor(getResources().getColor(R.color.white));
                fri.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fri.setBackgroundColor(getResources().getColor(R.color.white));
                        //tue.setBackgroundResource(R.drawable.category_square);
                        //check how many times clicked and so on
                        fri.setTextColor(getResources().getColor(R.color.black));
                        //Toast.makeText(getActivity(), "Friday is Weekend", Toast.LENGTH_SHORT).show();
                        sixfri.setText("");
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Alaramdays SET swap =(SELECT selecteddays FROM Alaramdays WHERE _id = '5') where _id ='5' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Alaramdays SET selecteddays =(SELECT unselecteddays FROM Alaramdays WHERE _id = '5') where _id ='5' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Alaramdays SET unselecteddays =(SELECT swap FROM Alaramdays WHERE _id = '5') where _id ='5' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows.close();
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows1.close();
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();
                        }
                        allrows2.close();
                        friday(dialog);
                    }
                });



            }
            else{
                fri.setBackgroundColor(getResources().getColor(R.color.white));
                fri.setTextColor(getResources().getColor(R.color.black));
                sixfri.setText("");
                fri.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fri.setBackgroundColor(getResources().getColor(R.color.green));
                        fri.setTextColor(getResources().getColor(R.color.white));
                        //Toast.makeText(getActivity(), "Friday is Weekday!", Toast.LENGTH_SHORT).show();
                        sixfri.setText(" Fri");
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Alaramdays SET swap =(SELECT selecteddays FROM Alaramdays WHERE _id = '5') where _id ='5' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Alaramdays SET selecteddays =(SELECT unselecteddays FROM Alaramdays WHERE _id = '5') where _id ='5' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Alaramdays SET unselecteddays =(SELECT swap FROM Alaramdays WHERE _id = '5') where _id ='5' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows.close();
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows1.close();
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();

                        }
                        allrows2.close();
                        friday(dialog);
                    }
                });
            }
            allrows.moveToNext();
        }

    }

    private void saturday(final Dialog dialog) {
        sat = (TextView)dialog.findViewById(R.id.selectsatuday);
        sevensat = (TextView)dialog.findViewById(R.id.seven);
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        final Cursor allrows = db.rawQuery("SELECT * FROM Alaramdays where _id ='6' ;", null);
        allrows.moveToFirst();
        while (allrows.isAfterLast() == false) {
            if (allrows.getString(1).equals("Saturday")) {
                sat.setBackgroundColor(getResources().getColor(R.color.green));
                sevensat.setText(" Sat");
                sat.setTextColor(getResources().getColor(R.color.white));
                sat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sat.setBackgroundColor(getResources().getColor(R.color.white));
                        //tue.setBackgroundResource(R.drawable.category_square);
                        //check how many times clicked and so on
                        sat.setTextColor(getResources().getColor(R.color.black));
                        //Toast.makeText(getActivity(), "Saturday is Weekend", Toast.LENGTH_SHORT).show();
                        sevensat.setText("");
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Alaramdays SET swap =(SELECT selecteddays FROM Alaramdays WHERE _id = '6') where _id ='6' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Alaramdays SET selecteddays =(SELECT unselecteddays FROM Alaramdays WHERE _id = '6') where _id ='6' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Alaramdays SET unselecteddays =(SELECT swap FROM Alaramdays WHERE _id = '6') where _id ='6' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows.close();
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows1.close();
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();
                        }
                        allrows2.close();
                        saturday(dialog);
                    }
                });



            }
            else{
                sat.setBackgroundColor(getResources().getColor(R.color.white));
                sat.setTextColor(getResources().getColor(R.color.black));
                sevensat.setText("");
                sat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sat.setBackgroundColor(getResources().getColor(R.color.green));
                        sat.setTextColor(getResources().getColor(R.color.white));
                        // Toast.makeText(getActivity(), "Saturday is Weekday!", Toast.LENGTH_SHORT).show();
                        sevensat.setText(" Sat");
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Alaramdays SET swap =(SELECT selecteddays FROM Alaramdays WHERE _id = '6') where _id ='6' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Alaramdays SET selecteddays =(SELECT unselecteddays FROM Alaramdays WHERE _id = '6') where _id ='6' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Alaramdays SET unselecteddays =(SELECT swap FROM Alaramdays WHERE _id = '6') where _id ='6' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows.close();
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows1.close();
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();

                        }
                        allrows2.close();
                        saturday(dialog);
                    }
                });
            }
            allrows.moveToNext();
        }

    }


    public static boolean SdIsPresent() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    private static void copyFile(File src, File dst) throws IOException {
        Log.e("1", "111hi1");
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        Log.e("1", "111hi2");
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
            Log.e("1", "111hi3");
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
            Log.e("1", "111hi4");
        }
    }


    class DownloadMusicfromInternet extends AsyncTask<String, Void, Integer> {
        private ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);

        @Override
        protected Integer doInBackground(String... params) {

            try {
//                File sd = Environment.getExternalStorageDirectory();
                File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File data = Environment.getDataDirectory();

                if (sd.canWrite()) {
                    String currentDBPath = "//data//" + "com.intuition.ivepos"
                            + "//databases//" + "mydb_Appdata";
                    String backupDBPath = "/IVEPOS_backup/" + backupname.getText().toString() + "/" + "mydb_Appdata";

//                                                Toast.makeText(getActivity(), "1 " + currentDBPath, Toast.LENGTH_LONG)
//                                                        .show();

//                                                Toast.makeText(getActivity(), "11 " + backupDBPath, Toast.LENGTH_LONG)
//                                                        .show();

//                                            boolean dbexist = DATA_DIRECTORY_DATABASE;
//                                            if (dbexist) {
//
//                                            }
//                                            DATA_DIRECTORY_DATABASE


                    File file = new File("/data/data/com.intuition.ivepos/databases/mydb_Appdata");
                    if(file.exists()){
//                                                    Toast.makeText(getActivity(), "exists", Toast.LENGTH_LONG)
//                                                            .show();
                    }
                    if (DATA_DIRECTORY_DATABASE.exists()){
//                                                    Toast.makeText(getActivity(), "existsssss", Toast.LENGTH_LONG)
//                                                            .show();
                    }else {
//                                                    Toast.makeText(getActivity(), "not exists", Toast.LENGTH_LONG)
//                                                            .show();
                    }

                    File backupDB = new File(data, currentDBPath);
                    File currentDB = new File(sd, backupDBPath);

                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
//                                                Toast.makeText(getActivity(),"111 "+ backupDB.toString(),
//                                                        Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {

                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG)
                        .show();

            }

            try {
//                File sd = Environment.getExternalStorageDirectory();
                File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File data = Environment.getDataDirectory();

                if (sd.canWrite()) {
                    String currentDBPath = "//data//" + "com.intuition.ivepos"
                            + "//databases//" + "mydb_Salesdata";
                    String backupDBPath = "/IVEPOS_backup/" + backupname.getText().toString() + "/" + "mydb_Salesdata";

//                                                Toast.makeText(getActivity(), "1 " + currentDBPath, Toast.LENGTH_LONG)
//                                                        .show();
//
//                                                Toast.makeText(getActivity(), "11 " + backupDBPath, Toast.LENGTH_LONG)
//                                                        .show();

//                                            boolean dbexist = DATA_DIRECTORY_DATABASE;
//                                            if (dbexist) {
//
//                                            }
//                                            DATA_DIRECTORY_DATABASE


                    File file = new File("/data/data/com.intuition.ivepos/databases/mydb_Salesdata");
                    if(file.exists()){
//                                                    Toast.makeText(getActivity(), "exists", Toast.LENGTH_LONG)
//                                                            .show();
                    }
                    if (DATA_DIRECTORY_DATABASE.exists()){
//                                                    Toast.makeText(getActivity(), "existsssss", Toast.LENGTH_LONG)
//                                                            .show();
                    }else {
//                                                    Toast.makeText(getActivity(), "not exists", Toast.LENGTH_LONG)
//                                                            .show();
                    }

                    File backupDB = new File(data, currentDBPath);
                    File currentDB = new File(sd, backupDBPath);

                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
//                                                Toast.makeText(getActivity(),"111 "+ backupDB.toString(),
//                                                        Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {

                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG)
                        .show();

            }



            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);


            db.execSQL("CREATE TABLE if not exists Adminrights (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, username text, password text);");
            db.execSQL("CREATE TABLE if not exists BIllingmode (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, billingtype text);");
            db.execSQL("CREATE TABLE if not exists Barcodescannerconnectivity (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, barcodescannercontype text);");
            db.execSQL("CREATE TABLE if not exists CATEGORY (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, ALL_CATEGORY text);");
            db.execSQL("CREATE TABLE if not exists Cashdrawerconnectivity (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, cashdrawercontype text);");
            db.execSQL("CREATE TABLE if not exists Companydetailss (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, companyname text, doorno text," +
                    "substreet text, street text, city text, state text, country text, pincode INTEGER, phoneno INTEGER, taxone text, taxtwo text, footerone text, footertwo text," +
                    "address1 text, email text, website text, address2 text, address3 text );");
            db.execSQL("CREATE TABLE if not exists Hotel (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, direccion text, telefono text, email text);");
            db.execSQL("CREATE TABLE if not exists Items (_id integer PRIMARY KEY AUTOINCREMENT, itemname text, price NUMERIC, stockquan NUMERIC, category text, itemtax text," +
                    "image blob, weekdaysvalue text, weekendsvalue text, manualstockvalue text, automaticstockresetvalue text, clickcount text, favourites text," +
                    "disc_type text, disc_value text, image_text text, barcode_value text, checked text, print_value text);");
            db.execSQL("CREATE TABLE if not exists Items1 (_id integer PRIMARY KEY AUTOINCREMENT, itemname text, price NUMERIC, stockquan NUMERIC, category text, itemtax text," +
                    "image blob, weekdaysvalue TEXT, weekendsvalue TEXT, manualstockvalue TEXT, automaticstockresetvalue TEXT, clickcount TEXT, favourites TEXT);");
            db.execSQL("CREATE TABLE if not exists Itemsstockvalue (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, weekdaysvalue text, weekendsvalue text);");
            db.execSQL("CREATE TABLE if not exists LAdmin (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, username text, password text, name text);");
            db.execSQL("CREATE TABLE if not exists LOGIN (ID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, USERNAME text, PASSWORD textpublic, name text);");
            db.execSQL("CREATE TABLE if not exists Logo (_id INTEGER PRIMARY KEY UNIQUE, companylogo blob);");
            db.execSQL("CREATE TABLE if not exists Modifiers (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, modifiername text, modprice numeric, modstockquan numeric, " +
                    "modcategory text, moditemtax text, modimage BLOB, mod_image_text text);");
            db.execSQL("CREATE TABLE if not exists Printerconnectivity (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, printercontype text);");
            db.execSQL("CREATE TABLE if not exists Printerreceiptsize(_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, papersize text)");
            db.execSQL("CREATE TABLE if not exists Quickaccess (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text);");
            db.execSQL("CREATE TABLE if not exists Quickedit (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, quickedittype text);");
            db.execSQL("CREATE TABLE if not exists ResetFrequencyRestaurant (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, resetfrequencyrestaurant text);");
            db.execSQL("CREATE TABLE if not exists ResetFrequencyRetail (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, resetfrequencyretail text);");
            db.execSQL("CREATE TABLE if not exists Stockcontrol (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, stockcontroltype text);");
            db.execSQL("CREATE TABLE if not exists Itemsort (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, itemsorttype text);");
            db.execSQL("CREATE TABLE if not exists Stockreset (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, stockresettype text);");
            db.execSQL("CREATE TABLE if not exists Stockresetmode (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, stockresetoptionsmode text);");
            db.execSQL("CREATE TABLE if not exists Storedays (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, weekdays text, weekends text, swap text);");
            db.execSQL("CREATE TABLE if not exists Universalcredentials (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, username text, password text);");
            db.execSQL("CREATE TABLE if not exists User1 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
            db.execSQL("CREATE TABLE if not exists User2 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
            db.execSQL("CREATE TABLE if not exists User3 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
            db.execSQL("CREATE TABLE if not exists User4 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
            db.execSQL("CREATE TABLE if not exists User5 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
            db.execSQL("CREATE TABLE if not exists User6 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
            db.execSQL("CREATE TABLE if not exists Taxes (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, taxname text, value numeric, taxtype text);");
            db.execSQL("CREATE TABLE if not exists Discount_types (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, discountname text, discountvalue numeric);");
            db.execSQL("CREATE TABLE if not exists Totaltables (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, nooftables text);");
            db.execSQL("CREATE TABLE if not exists asd1 (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, pName text, pDate text, image blob);");

            db.execSQL("CREATE TABLE if not exists LoginUser (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, username text, password text);");
            db.execSQL("CREATE TABLE if not exists UserLogin_Checking (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, username text, password text, name text);");


            db.execSQL("CREATE TABLE if not exists Alaramon_off (_id integer PRIMARY KEY UNIQUE, status text);");
            db.execSQL("CREATE TABLE if not exists Alaramdays (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, selecteddays TEXT, unselecteddays text, swap TEXT);");
            db.execSQL("CREATE TABLE if not exists Alaramtime (_id integer PRIMARY KEY UNIQUE, time text);");

            db.execSQL("CREATE TABLE if not exists BTConn (_id integer PRIMARY KEY UNIQUE, name text, address text, status text, device text);");
            db.execSQL("CREATE TABLE if not exists IPConn (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");

            db.execSQL("CREATE TABLE if not exists Menulogin_checking (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, status text);");
            db.execSQL("CREATE TABLE if not exists Home_check (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, login_status text);");
            //dbllega.execSQL("CREATE TABLE if not exists asd2 (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, pName text, pDate text, image blob);");

            db.execSQL("CREATE TABLE if not exists DeleteDBon_off (_id integer PRIMARY KEY UNIQUE, status text);");
            db.execSQL("CREATE TABLE if not exists Auto_generate_barcode (_id integer PRIMARY KEY UNIQUE, generate text);");
            db.execSQL("CREATE TABLE if not exists DeleteDB_time (_id integer PRIMARY KEY UNIQUE, time text);");
            db.execSQL("CREATE TABLE if not exists Email_setup (_id integer PRIMARY KEY UNIQUE, username text, password text, client text);");
            db.execSQL("CREATE TABLE if not exists Default_credit (_id integer PRIMARY KEY UNIQUE, status text);");
            db.execSQL("CREATE TABLE if not exists Working_hours (_id integer PRIMARY KEY UNIQUE, opening text, opening_time text, closing text, closing_time text," +
                    "opening_time_system text, closing_time_system text);");
            db.execSQL("CREATE TABLE if not exists Printer_text_size (_id integer PRIMARY KEY UNIQUE, type text);");
            db.execSQL("CREATE TABLE if not exists Change_time_format (_id integer PRIMARY KEY UNIQUE, status text);");
            db.execSQL("CREATE TABLE if not exists Hotel1 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, value int);");
            db.execSQL("CREATE TABLE if not exists Discount_details (_id INTEGER PRIMARY KEY UNIQUE, disc_code text, disc_value text, disc_type text);");
            db.execSQL("CREATE TABLE if not exists Email_recipient (_id integer PRIMARY KEY UNIQUE, name text, ph_no text, email text);");

            db.execSQL("CREATE TABLE if not exists Schedule_mail_on_off (_id integer PRIMARY KEY UNIQUE, status text);");
            db.execSQL("CREATE TABLE if not exists Schedule_mail_time (_id integer PRIMARY KEY UNIQUE, time text);");
            db.execSQL("CREATE TABLE if not exists promotions (_id integer PRIMARY KEY UNIQUE, email text);");

            db.execSQL("CREATE TABLE if not exists User_privilege (_id integer PRIMARY KEY UNIQUE, username text, returns_refunds text, product_tax text, reports text," +
                    "settings text, backup text, customer text);");

            db.execSQL("CREATE TABLE if not exists Tax_selec (_id integer PRIMARY KEY UNIQUE, tax_amount text, tax_per text, selected_but text);");
            db.execSQL("CREATE TABLE if not exists Discount_selec (_id integer PRIMARY KEY UNIQUE, discount_amount text, discount_per text, selected_but text);");
            db.execSQL("CREATE TABLE if not exists Vendor_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text, " +
                    "vendor_email text, vendor_address text, vendor_gst text);");
            db.execSQL("CREATE TABLE if not exists Vendor_sold_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text," +
                    "invoice text, billamount text, tax_percent text, tax_amount text, disc_percent text, disc_amount text, total_bill_amount text, from_time text," +
                    "from_date text, due_date text, datetimee_new_from text, datetimee_new_due text, pay text, pending text, total_pay text, pay_date text, pay_time text, pay_datetimeemew text, not_required text);");
            db.execSQL("CREATE TABLE if not exists Vendor_sold_item_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text," +
                    "itemname text, qty_add text, individual_price text, total_price text, invoice text, billamount text, tax_percent text, tax_amount text, disc_percent text," +
                    "disc_amount text, total_bill_amount text, from_time text, from_date text, due_date text, datetimee_new_from text, datetimee_new_due text, pay text, pending text," +
                    "tax1 text, tax2 text, tax3 text, tax4 text, tax5 text, tax6 text, tax7 text, tax8 text, tax9 text, tax10 text, tax11 text, tax12 text, tax13 text," +
                    "tax14 text, tax15 text, tax1_value text, tax2_value text, tax3_value text, tax4_value text, tax5_value text, tax6_value text, tax7_value text," +
                    "tax8_value text, tax9_value text, tax10_value text, tax11_value text, tax12_value text, tax13_value text, tax14_value text, tax15_value text);");
            db.execSQL("CREATE TABLE if not exists Ingredient_items_list (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, ingredient_name text, itemname text, item_qyt_used text," +
                    "currnet_stock text, date1 text, date text, time1 text, time text, modified_datetimee_new text, qty_unit text, required text);");
            db.execSQL("CREATE TABLE if not exists Vendor_temp_list (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vend_phon text, vend_email text," +
                    "vend_gst text, vend_address text, vend_total_bill_amount text, paid text, pending text, bill_no text);");
            db.execSQL("CREATE TABLE if not exists Items_temp_list (_id integer PRIMARY KEY UNIQUE, itemname text, avg_price text, min_price text," +
                    "max_price text, total_qty text, total_price text, barcode text, not_required text);");

            db.execSQL("CREATE TABLE if not exists Ingredient_Vendor_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text, " +
                    "vendor_email text, vendor_address text, vendor_gst text);");
            db.execSQL("CREATE TABLE if not exists Ingredient_sold_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text," +
                    "invoice text, billamount text, tax_percent text, tax_amount text, disc_percent text, disc_amount text, total_bill_amount text, from_time text," +
                    "from_date text, due_date text, datetimee_new_from text, datetimee_new_due text, pay text, pending text, total_pay text, pay_date text, pay_time text, pay_datetimeemew text, not_required text);");
            db.execSQL("CREATE TABLE if not exists Ingredients (_id integer PRIMARY KEY UNIQUE, ingredient_name text, min_req text, max_req text, current_stock text," +
                    "unit text, indiv_price text, total_price text, date text, date1 text, time text, time1 text, datetimee_new text, avg_price text, required text, barcode text," +
                    "status_low text, status_qty_updated text, add_qty text, indiv_price_copy text, adjusted_stock text, diff_stock text, indiv_price_temp text," +
                    "total_price_temp text);");
            db.execSQL("CREATE TABLE if not exists Ingredient_sold_item_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text," +
                    "itemname text, qty_add text, individual_price text, total_price text, invoice text, billamount text, tax_percent text, tax_amount text, disc_percent text," +
                    "disc_amount text, total_bill_amount text, from_time text, from_date text, due_date text, datetimee_new_from text, datetimee_new_due text, pay text, pending text," +
                    "tax1 text, tax2 text, tax3 text, tax4 text, tax5 text, tax6 text, tax7 text, tax8 text, tax9 text, tax10 text, tax11 text, tax12 text, tax13 text," +
                    "tax14 text, tax15 text, tax1_value text, tax2_value text, tax3_value text, tax4_value text, tax5_value text, tax6_value text, tax7_value text," +
                    "tax8_value text, tax9_value text, tax10_value text, tax11_value text, tax12_value text, tax13_value text, tax14_value text, tax15_value text," +
                    "wastage text, unit text);");
            db.execSQL("CREATE TABLE if not exists Ingredients_item_selection_temp (_id integer PRIMARY KEY UNIQUE, itemname text, qty_temp text, qty_temp_unit text, qty text);");
            db.execSQL("CREATE TABLE if not exists Vendor_details_micro (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text, " +
                    "vendor_email text, vendor_address text, vendor_gst text);");
            db.execSQL("CREATE TABLE if not exists Vendor_temp_list_Ingredient (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vend_phon text, vend_email text," +
                    "vend_gst text, vend_address text, vend_total_bill_amount text, paid text, pending text, bill_no text);");
            db.execSQL("CREATE TABLE if not exists Ingredients_temp_list (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, avg_price text, min_price text," +
                    "max_price text, total_qty text, total_price text, barcode text, unit text, wastage_qty text, wastage_cost text, not_required text);");
            db.execSQL("CREATE TABLE if not exists Printer_type (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, printer_type text);");

            db.execSQL("CREATE TABLE if not exists KOT_print (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, kot_print_status text);");
            db.execSQL("CREATE TABLE if not exists Auto_Connect (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, auto_connect_status text);");
            db.execSQL("CREATE TABLE if not exists Weighing_Scale_status (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, Weighing_Scale_onoff text);");
            db.execSQL("CREATE TABLE if not exists Weighing_Scale_name (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, scale_name text);");
            db.execSQL("CREATE TABLE if not exists Sync_time (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, last_time text);");
            db.execSQL("CREATE TABLE if not exists variants_temp (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vari1 text, varprice1 text, vari2 text, varprice2 text," +
                    "vari3 text, varprice3 text, vari4 text, varprice4 text, vari5 text, varprice5 text);");
            db.execSQL("CREATE TABLE if not exists PaytmMerchantReg(_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, Account text, MerchantName text, " +
                    "guid text, MID text, Merchant_key text, PosID text)");
            db.execSQL("CREATE TABLE IF NOT EXISTS MobikwikMerchantReg(_id integer primary key autoincrement unique, Account text, Merchant_name text, " +
                    "Mid_otp text, Secretkey_otp text, Mid_debit text, Secretkey_debit text)");
            db.execSQL("CREATE TABLE IF NOT EXISTS all_transactions" +
                    "(_id integer primary key autoincrement unique, Payment_medium text, merchantRefInvoiceNo text, amount text, cardHolderName text," +
                    " cardBrand text, cardType text, cardNumber text, paymentId text, transactionId text, tdrPercentage text, approved text)");
            db.execSQL("CREATE TABLE IF NOT EXISTS CardSwiperActivation" +
                    "(_id integer primary key autoincrement unique, CardSwiperName text, merchantKey text, partnerkey text, Config_status text)");
            db.execSQL("CREATE TABLE if not exists IPConn_Counter (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");
            db.execSQL("CREATE TABLE if not exists Country_Selection (_id integer PRIMARY KEY UNIQUE, country text);");
            db.execSQL("CREATE TABLE if not exists Round_off (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, round_off_status text);");
            db.execSQL("CREATE VIRTUAL TABLE if not exists Items_Virtual USING fts3(itemname , price , stockquan , category , itemtax ," +
                    "image , weekdaysvalue , weekendsvalue , manualstockvalue , automaticstockresetvalue , clickcount , favourites ," +
                    "disc_type , disc_value , image_text , barcode_value , checked , print_value , quantity_sold , minimum_qty , minimum_qty_copy , add_qty ," +
                    "status_low , status_qty_updated , individual_price , unit_type , tax_value , itemtax2 , tax_value2 , itemtax3 ,tax_value3 ," +
                    "itemtax4 ,tax_value4 ,itemtax5 ,tax_value5 , status_perm , status_temp , variant1 , variant_price1 , variant2 , variant_price2 , variant3 ," +
                    "variant_price3 , variant4 , variant_price4 , variant5 , variant_price5)");
            db.execSQL("CREATE TABLE if not exists BIllingtype (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, billingtype_type text);");
            db.execSQL("CREATE TABLE if not exists Estimate_print (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, status text);");
            db.execSQL("CREATE TABLE if not exists IPConn_KOT1 (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");
            db.execSQL("CREATE TABLE if not exists IPConn_KOT2 (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");
            db.execSQL("CREATE TABLE if not exists IPConn_KOT3 (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");
            db.execSQL("CREATE TABLE if not exists IPConn_KOT4 (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");

            db.execSQL("CREATE TABLE if not exists Name_Dept1 (_id integer PRIMARY KEY UNIQUE, dept1_name text);");
            db.execSQL("CREATE TABLE if not exists Name_Dept2 (_id integer PRIMARY KEY UNIQUE, dept2_name text);");
            db.execSQL("CREATE TABLE if not exists Name_Dept3 (_id integer PRIMARY KEY UNIQUE, dept3_name text);");
            db.execSQL("CREATE TABLE if not exists Name_Dept4 (_id integer PRIMARY KEY UNIQUE, dept4_name text);");

            db.execSQL("CREATE TABLE if not exists Ordertaking_server_ip (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");

            db.execSQL("CREATE TABLE if not exists HomeDelivery_prints (_id integer PRIMARY KEY UNIQUE, companycopy text, customercopy text);");
            db.execSQL("CREATE TABLE if not exists Billcount_tag (_id integer PRIMARY KEY UNIQUE, tag_name text);");

            db.execSQL("CREATE TABLE if not exists Stock_transfer_item_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, qty_add text," +
                    "company text, from_store text, from_device text, to_store text, to_device text, from_time text, from_date text, datetimee_new_from text);");


            db1.execSQL("CREATE TABLE if not exists All_Sales (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, quantity text, price text, total text," +
                    "type text, parent text, parentid text, mod_assigned text, tax text, taxname text, bill_no text, time text, date text, user text, table_id text, billtype text," +
                    "paymentmethod text, billamount_disapply text, billamount_disnotapply text, _idd text, deleted_not text, modifiedquantity text, quantitycopy text, " +
                    "modifiedtotal text, date1 text, datetimee text, disc_type text, disc_value text, newtotal text, disc_thereornot text," +
                    " disc_indiv_total text, new_modified_total text);");

            db1.execSQL("CREATE TABLE if not exists Itemwiseorderlistitems (_id integer PRIMARY KEY UNIQUE, itemno text, itemname text, sales integer, salespercentage integer," +
                    "itemtotalquan text);");
            db1.execSQL("CREATE TABLE if not exists Itemwiseorderlistmodifiers (_id integer PRIMARY KEY UNIQUE, modno text, modname text, sales integer, salespercentage integer," +
                    "modtotalquan text);");
            db1.execSQL("CREATE TABLE if not exists Userwiseorderlistitems (_id integer PRIMARY KEY UNIQUE, username text, sales integer, salespercentage integer);");
            db1.execSQL("CREATE TABLE if not exists Generalorderlistascdesc (_id integer PRIMARY KEY UNIQUE, date text, time text, user text, billno text, " +
                    "billdetails text, sales integer, discountamount text, paymentmethod text, billtype text, itemname text, quan text);");
            db1.execSQL("CREATE TABLE if not exists Generalorderlistascdesc1 (_id integer PRIMARY KEY UNIQUE, date text, time text, user text, billno text, " +
                    "billdetails text, sales integer, discountamount text, paymentmethod text, billtype text, itemname text, quan text, tableid text, individualprice text" +
                    ", date1 text, datetimee text);");
            db1.execSQL("CREATE TABLE if not exists userdata (_id integer PRIMARY KEY UNIQUE, username text, total integer);");
            db1.execSQL("CREATE TABLE if not exists itemdata (_id integer PRIMARY KEY UNIQUE, itemname text, total integer);");

            db1.execSQL("CREATE TABLE if not exists All_Sales_Cancelled (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, quantity text, price text, total text," +
                    "type text, parent text, parentid text, mod_assigned text, tax text, taxname text, bill_no text, time text, date text, user text, billtype text," +
                    " paymentmethod text, billamount_disapply text, billamount_disnotapply text, _idd text, reason text, " +
                    "billamount_cancelled text, date1 text, billamount_cancelled_user text, disc_type text, disc_value text, newtotal text, disc_thereornot text," +
                    " disc_indiv_total text);");

            db1.execSQL("CREATE TABLE if not exists Cancelwiseorderlistitems (_id integer PRIMARY KEY UNIQUE, date text, time text, user text, billno text, sale text, " +
                    "refund text, reason text );");

            db1.execSQL("CREATE TABLE if not exists usercancelleddata (_id integer PRIMARY KEY UNIQUE, username text, total integer);");
            db1.execSQL("CREATE TABLE if not exists Customerdetails (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, name text, phoneno text, emailid text, address text, " +
                    "rupees text, billnumber text);");
            db1.execSQL("CREATE TABLE if not exists Tablepayment (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, tablename text, tableid text, price text, print text);");
            db1.execSQL("CREATE TABLE if not exists Billnumber (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, billnumber text, total text, user text, date text," +
                    " paymentmethod text, billtype text, subtotal text, taxtotal text, roundoff text, globaltaxtotal text);");
            db1.execSQL("CREATE TABLE if not exists Discountdetails (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, date text, time text, billno text, Discountcodeno text, " +
                    "Discount_percent text, Billamount_rupess text, Discount_rupees text, date1 text, original_amount text);");
            db1.execSQL("CREATE TABLE if not exists Cardnumber (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, cardnumber text);");
            db1.execSQL("CREATE TABLE if not exists Splitdata (_id integer PRIMARY KEY UNIQUE, billnum text, total text, splittype text, split1 text, split2 text, split3 text);");
            db1.execSQL("CREATE TABLE if not exists Cust_feedback (_id integer PRIMARY KEY UNIQUE, cust_name text, date text, time text, ambience_rating text, pro_qual_rating text," +
                    " service_rating text, overall_exp_rating text, comments text, percentage text, cust_phoneno text);");
            db1.execSQL("CREATE TABLE if not exists Clicked_cust_name (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, name text);");
            db1.execSQL("CREATE TABLE if not exists Customerdetails_temporary (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, commission_charges text, commission_charges_status text, " +
                    "commission_charges_type text, phoneno text, name text);");
            db1.execSQL("CREATE TABLE if not exists Cusotmer_activity_temp (_id integer PRIMARY KEY UNIQUE, name text, phone_no text, " +
                    "email text, addr text, total_amount text, balance text, discount_value, text, discount_type text, approval_rate text);");
            db1.execSQL("CREATE TABLE if not exists Cusotmer_activity_temp_top3 (_id integer PRIMARY KEY UNIQUE, name text, phone_no text, " +
                    "email text, addr text, total_amount integer, balance text, discount_value, text, discount_type text, approval_rate text);");

            for (int i=1;i<=100;i++ ){
                db1.execSQL("CREATE TABLE if not exists Table" + i + " (_id integer PRIMARY KEY AUTOINCREMENT,quantity text, itemname text, price text, total text, type text," +
                        " parent text, parentid text, modassigned text, tax text, taxname text, disc_type text, disc_value text, newtotal text, disc_thereornot text," +
                        " disc_indiv_total text);");

                db1.execSQL("CREATE TABLE if not exists Table" + i + "payment (_id integer PRIMARY KEY AUTOINCREMENT, tableid text, price text, type text, paymentmethod text, " +
                        " discount text, discounttype text, discountcodenum text, cust_name text, cust_phone_no text, cust_emailid text, cust_address text, due_amount text," +
                        " cardnumber text, amounttendered text, dialog_round text, hometotal text, disc_type text, disc_value text, newtotal text, disc_thereornot text," +
                        " disc_indiv_total text);");

                db1.execSQL("CREATE TABLE if not exists Table" + i + "management (_id integer PRIMARY KEY AUTOINCREMENT, itemname text, qty text, tagg integer, date text, " +
                        " time text, par_id text, itemtype text);");
            }

            db1.execSQL("CREATE TABLE if not exists Top_Reason (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, reason text, value integer);");
            db1.execSQL("CREATE TABLE if not exists Top_Category (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, category text, value integer);");
            db1.execSQL("CREATE TABLE if not exists Itemwiseorderlistcategory (_id integer PRIMARY KEY UNIQUE, itemno text, categoryname text, sales integer, salespercentage integer," +
                    "itemtotalquan text);");

            db1.execSQL("CREATE TABLE if not exists BillCount (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, value text);");

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

            Cursor co4 = db.rawQuery("SELECT * FROM Modifiers", null);
            int cou4 = co4.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou4).equals("7")){
                db.execSQL("ALTER TABLE Modifiers ADD COLUMN mod_image_text text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co4.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co4_1 = db.rawQuery("SELECT * FROM Hotel", null);
            int cou4_1 = co4_1.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou4_1).equals("5")){
                db.execSQL("ALTER TABLE Hotel ADD COLUMN value int");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co4_1.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co1 = db.rawQuery("SELECT * FROM Items", null);
            int cou1 = co1.getColumnCount();
            //Toast.makeText(getActivity(), "2 "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou1).equals("13") || cou1 == 13){
                db.execSQL("ALTER TABLE Items ADD COLUMN disc_type text DEFAULT 0");
                db.execSQL("ALTER TABLE Items ADD COLUMN disc_value text DEFAULT 0");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co1.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co3 = db.rawQuery("SELECT * FROM Items", null);
            int cou3 = co3.getColumnCount();
            //Toast.makeText(getActivity(), "2 "+String.valueOf(cou3), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou3).equals("15")){
                db.execSQL("ALTER TABLE Items ADD COLUMN image_text text");
//                Toast.makeText(getActivity(), " image_text created 2", Toast.LENGTH_SHORT).show();
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co3.close();

            Cursor threete = db.rawQuery("SELECT * FROM DeleteDB_time ", null);
            if (threete.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("time", "11:30 PM");
                db.insert("DeleteDB_time", null, contentValues);
            }
            threete.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor threet = db.rawQuery("SELECT * FROM DeleteDBon_off ", null);
            if (threet.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("status", "off");
                db.insert("DeleteDBon_off", null, contentValues);
            }
            threet.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor threetf = db.rawQuery("SELECT * FROM Auto_generate_barcode ", null);
            if (threetf.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("generate", "off");
                db.insert("Auto_generate_barcode", null, contentValues);
            }
            threetf.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor twleve = db1.rawQuery("SELECT * FROM Clicked_cust_name", null);
            if (twleve.moveToFirst()){

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", "");
                db1.insert("Clicked_cust_name", null, contentValues);
            }
            twleve.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor threqe = db.rawQuery("SELECT * FROM KOT_print ", null);
            if (threqe.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("kot_print_status", "Yes");
                db.insert("KOT_print", null, contentValues);
            }
            threqe.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor ttthreqe = db.rawQuery("SELECT * FROM Auto_Connect ", null);
            if (ttthreqe.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("auto_connect_status", "No");
                db.insert("Auto_Connect", null, contentValues);
            }
            ttthreqe.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor tathreqee = db.rawQuery("SELECT * FROM Sync_time ", null);
            if (tathreqee.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("last_time", "05 Apr 18, 05:22 PM");
                db.insert("Sync_time", null, contentValues);
            }
            tathreqee.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor tathreqeee = db.rawQuery("SELECT * FROM Round_off ", null);
            if (tathreqeee.moveToFirst()) {

            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("round_off_status", "No");
                db.insert("Round_off", null, contentValues);
            }
            tathreqeee.close();

            Cursor twelvw = db.rawQuery("SELECT * FROM Default_credit", null);
            if (twelvw.moveToFirst()){

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("status", "off");
                db.insert("Default_credit", null, contentValues);
            }
            twelvw.close();

            Cursor thirteen = db.rawQuery("SELECT * FROM Working_hours", null);
            if (thirteen.moveToFirst()){

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("opening", "Today");
                contentValues.put("opening_time", "12:01 AM");
                contentValues.put("closing", "Today");
                contentValues.put("closing_time", "11:59 PM");
                contentValues.put("opening_time_system", "00:01");
                contentValues.put("closing_time_system", "23:59");
                db.insert("Working_hours", null, contentValues);
            }
            thirteen.close();

            Cursor fourteen_1 = db.rawQuery("SELECT * FROM Printer_text_size", null);
            if (fourteen_1.moveToFirst()){

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("type", "Standard");
                db.insert("Printer_text_size", null, contentValues);
            }
            fourteen_1.close();

            Cursor fourteeen_1 = db.rawQuery("SELECT * FROM Change_time_format", null);
            if (fourteeen_1.moveToFirst()){

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("status", "not changed");
                db.insert("Change_time_format", null, contentValues);
            }
            fourteeen_1.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor coo5 = db.rawQuery("SELECT * FROM Items", null);
            int couu5 = coo5.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(couu5).equals("16")){
                db.execSQL("ALTER TABLE Items ADD COLUMN barcode_value text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            coo5.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor coo6 = db.rawQuery("SELECT * FROM Items", null);
            int couu6 = coo6.getColumnCount();
            Log.i(TAG, "Added"+couu6);
            Log.i(TAG, "Added"+couu6);
            Log.i(TAG, "Added"+couu6);
            Log.i(TAG, "Added"+couu6);
            if (String.valueOf(couu6).equals("17")){
                db.execSQL("ALTER TABLE Items ADD COLUMN checked text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                Log.i(TAG, "Added");
                Log.i(TAG, "Added");
                Log.i(TAG, "Added");
                Log.i(TAG, "Added");
            }
            coo6.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor oco7 = db.rawQuery("SELECT * FROM Items", null);
            int ocou7 = oco7.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(ocou7).equals("18")){
                db.execSQL("ALTER TABLE Items ADD COLUMN print_value text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            oco7.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co87 = db.rawQuery("SELECT * FROM User_privilege", null);
            int cou87 = co87.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou87).equals("8")){
                db.execSQL("ALTER TABLE User_privilege ADD COLUMN ingredients text DEFAULT no");
                db.execSQL("ALTER TABLE User_privilege ADD COLUMN subscriptions text DEFAULT no");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co87.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co88 = db.rawQuery("SELECT * FROM Items", null);
            int cou88 = co88.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou88).equals("19")){
                db.execSQL("ALTER TABLE Items ADD COLUMN quantity_sold INTEGER DEFAULT 0");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co88.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co881 = db.rawQuery("SELECT * FROM Items", null);
            int cou881 = co881.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou881).equals("20")){
                db.execSQL("ALTER TABLE Items ADD COLUMN minimum_qty text DEFAULT 0");
                db.execSQL("ALTER TABLE Items ADD COLUMN minimum_qty_copy text DEFAULT 0");
                db.execSQL("ALTER TABLE Items ADD COLUMN add_qty text DEFAULT 0");
                db.execSQL("ALTER TABLE Items ADD COLUMN status_low text");
                db.execSQL("ALTER TABLE Items ADD COLUMN status_qty_updated text");
                db.execSQL("ALTER TABLE Items ADD COLUMN individual_price text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co881.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co882 = db.rawQuery("SELECT * FROM Items", null);
            int cou882 = co882.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou882).equals("26")){
                db.execSQL("ALTER TABLE Items ADD COLUMN unit_type text DEFAULT Unit");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co883 = db.rawQuery("SELECT * FROM Items", null);
            int cou883 = co883.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou883).equals("27")) {
                db.execSQL("ALTER TABLE Items ADD COLUMN tax_value text");
                db.execSQL("ALTER TABLE Items ADD COLUMN itemtax2 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN tax_value2 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN itemtax3 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN tax_value3 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN itemtax4 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN tax_value4 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN itemtax5 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN tax_value5 text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co884 = db.rawQuery("SELECT * FROM Items", null);
            int cou884 = co884.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou884).equals("36")) {
                db.execSQL("ALTER TABLE Items ADD COLUMN status_temp text");
                db.execSQL("ALTER TABLE Items ADD COLUMN status_perm text");
            }

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co885 = db.rawQuery("SELECT * FROM Items", null);
            int cou885 = co885.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou885).equals("38")) {
                db.execSQL("ALTER TABLE Items ADD COLUMN variant1 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant_price1 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant2 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant_price2 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant3 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant_price3 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant4 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant_price4 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant5 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant_price5 text");
            }

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co886 = db.rawQuery("SELECT * FROM Items", null);
            int cou886 = co886.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou886).equals("48")) {
                db.execSQL("ALTER TABLE Items ADD COLUMN dept_name text");
            }

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co887 = db.rawQuery("SELECT * FROM Items", null);
            int cou887 = co887.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou887).equals("49")) {
                db.execSQL("ALTER TABLE Items ADD COLUMN add_qty_st text");
                db.execSQL("ALTER TABLE Items ADD COLUMN status_qty_updated_st text");
            }
            co887.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co89 = db.rawQuery("SELECT * FROM Taxes", null);
            int cou89 = co89.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou89).equals("4")){
                db.execSQL("ALTER TABLE Taxes ADD COLUMN checked text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co89.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co90 = db.rawQuery("SELECT * FROM Taxes", null);
            int cou90 = co90.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou90).equals("5")){
                db.execSQL("ALTER TABLE Taxes ADD COLUMN tax1 text DEFAULT dine_in");
                db.execSQL("ALTER TABLE Taxes ADD COLUMN tax2 text DEFAULT takeaway");
                db.execSQL("ALTER TABLE Taxes ADD COLUMN tax3 text DEFAULT homedelivery");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co90.close();


            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co901 = db.rawQuery("SELECT * FROM Taxes", null);
            int cou901 = co901.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou901).equals("8")){
                db.execSQL("ALTER TABLE Taxes ADD COLUMN hsn_code text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co901.close();


            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co91 = db.rawQuery("SELECT * FROM IPConn", null);
            int cou91 = co91.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou91).equals("4")){
                db.execSQL("ALTER TABLE IPConn ADD COLUMN printer_name text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co91.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co92 = db.rawQuery("SELECT * FROM IPConn_Counter", null);
            int cou92 = co92.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou92).equals("4")){
                db.execSQL("ALTER TABLE IPConn_Counter ADD COLUMN printer_name text");
//                Toast.makeText(MainActivity.this, "altered", Toast.LENGTH_SHORT).show();
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co92.close();

            Cursor fgh = db.rawQuery("SELECT * FROM IPConn ", null);
            if (fgh.moveToFirst()) {
                String id = fgh.getString(0);
                ContentValues contentValues = new ContentValues();
                contentValues.put("printer_name", "TM Printer");
                String wherecu1 = "_id = '" + id + "'";
                db.update("IPConn", contentValues, wherecu1, new String[]{});

            }
            fgh.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor sixx = db.rawQuery("SELECT * FROM IPConn_Counter ", null);
            if (sixx.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("ipname", "192.168.1.87");
                contentValues.put("port", "9100");
                contentValues.put("status", "");
                db.insert("IPConn_Counter", null, contentValues);
            }
            sixx.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor fghh = db.rawQuery("SELECT * FROM IPConn_Counter", null);
            if (fghh.moveToFirst()) {
                String id = fghh.getString(0);
                ContentValues contentValues = new ContentValues();
                contentValues.put("printer_name", "TM Printer");
                String wherecu1 = "_id = '" + id + "'";
                db.update("IPConn_Counter", contentValues, wherecu1, new String[]{});
//                Toast.makeText(MainActivity.this, "updated", Toast.LENGTH_SHORT).show();
            }else {
//                Toast.makeText(MainActivity.this, "not updated", Toast.LENGTH_SHORT).show();
            }
            fghh.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor sixxx = db.rawQuery("SELECT * FROM Country_Selection ", null);
            if (sixxx.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("country", "India");
                db.insert("Country_Selection", null, contentValues);
            }
            sixxx.close();

            Cursor cursorrr = db.rawQuery("SELECT * FROM PaytmMerchantReg", null);
            if (cursorrr.moveToFirst()) {

            } else {
                ContentValues contentValuess = new ContentValues();
                contentValuess.put("Account", "Not_Registered");
                contentValuess.put("MerchantName", "");
                contentValuess.put("guid", "");
                contentValuess.put("MID", "");
                contentValuess.put("Merchant_key", "");
                contentValuess.put("PosID", "");
                db.insert("PaytmMerchantReg", null, contentValuess);
            }

            Cursor cur = db.rawQuery("SELECT * FROM MobikwikMerchantReg", null);
            if (cur.moveToFirst()) {

            } else {
                ContentValues contentValuees = new ContentValues();
                contentValuees.put("Account", "Not_Registered");
                contentValuees.put("Merchant_name", "");
                contentValuees.put("Mid_otp", "");
                contentValuees.put("Secretkey_otp", "");
                contentValuees.put("Mid_debit", "");
                contentValuees.put("Secretkey_debit", "");
                db.insert("MobikwikMerchantReg", null, contentValuees);
            }

            Cursor c = db.rawQuery("SELECT * FROM CardSwiperActivation", null);
            if (c.moveToFirst()) {
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("CardSwiperName", "PaySwiff");
                contentValues.put("merchantKey", "");
                contentValues.put("partnerkey", "");
                contentValues.put("Config_status", "Not Activated");
                db.insert("CardSwiperActivation", null, contentValues);

                ContentValues contentValues1 = new ContentValues();
                contentValues1.put("CardSwiperName", "mSwipe");
                contentValues1.put("merchantKey", "");
                contentValues1.put("partnerkey", "");
                contentValues1.put("Config_status", "Not Activated");
                db.insert("CardSwiperActivation", null, contentValues1);
            }



            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co2 = db1.rawQuery("SELECT * FROM Cardnumber", null);
            int cou2 = co2.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou2).equals("2")){
                db1.execSQL("ALTER TABLE Cardnumber ADD COLUMN billnumber text DEFAULT 0");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co2.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co = db1.rawQuery("SELECT * FROM Generalorderlistascdesc1", null);
            int cou = co.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou).equals("16")){
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN cardnumber text DEFAULT 0");
            }
            co.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co5 = db1.rawQuery("SELECT * FROM All_Sales", null);
            int cou5 = co5.getColumnCount();
            if (String.valueOf(cou5).equals("27")){
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN disc_type text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN disc_value text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN newtotal text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN disc_thereornot text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN disc_indiv_total text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN new_modified_total text DEFAULT 0");
            }
            co5.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co6 = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
            int cou6 = co6.getColumnCount();
            if (String.valueOf(cou6).equals("24")){
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_type text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_value text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN newtotal text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_thereornot text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_indiv_total text DEFAULT 0");
            }
            co6.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co7 = db1.rawQuery("SELECT * FROM Table1", null);
            int cou7 = co7.getColumnCount();
            if (String.valueOf(cou7).equals("11")){
                for (int i=1;i<=100;i++ ){
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN disc_type text DEFAULT 0");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN disc_value text DEFAULT 0");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN newtotal text DEFAULT 0");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN disc_thereornot text DEFAULT 0");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN disc_indiv_total text DEFAULT 0");
                }
            }
            co7.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo7 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou7 = coo7.getColumnCount();
            if (String.valueOf(coou7).equals("16")){
                for (int i=1;i<=100;i++ ){
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN status text");
                }
            }
            coo7.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo8 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou8 = coo8.getColumnCount();
            if (String.valueOf(coou8).equals("17")){
                for (int i=1;i<=100;i++ ){
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN tagg integer");
                }
            }
            coo8.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo9 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou9 = coo9.getColumnCount();
            if (String.valueOf(coou9).equals("18")){
                for (int i=1;i<=100;i++ ){
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN date text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN time text");
                }
            }
            coo9.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo10 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou10 = coo10.getColumnCount();
            if (String.valueOf(coou10).equals("20")){
                for (int i=1;i<=100;i++ ){
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN updated_quantity text");
                }
            }
            coo10.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo11 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou11 = coo11.getColumnCount();
            if (String.valueOf(coou11).equals("21")) {
                for (int i = 1; i <= 100; i++) {
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN taxname2 text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN tax2 text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN taxname3 text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN tax3 text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN taxname4 text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN tax4 text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN taxname5 text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN tax5 text");
                }
            }
            coo11.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo12 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou12 = coo12.getColumnCount();
            if (String.valueOf(coou12).equals("29")) {
                for (int i = 1; i <= 100; i++) {
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN category text");
                }
            }
            coo12.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo13 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou13 = coo13.getColumnCount();
            if (String.valueOf(coou13).equals("30")) {
                for (int i = 1; i <= 100; i++) {
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN add_note text");
                }
            }
            coo13.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo14 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou14 = coo14.getColumnCount();
            if (String.valueOf(coou14).equals("31")) {
                for (int i = 1; i <= 100; i++) {
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN dept_name text");
                }
            }
            coo14.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo15 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou15 = coo15.getColumnCount();
            if (String.valueOf(coou15).equals("32")) {
                for (int i = 1; i <= 100; i++) {
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN discount_value text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN discount_code text");
                }
            }
            coo15.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo16 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou16 = coo16.getColumnCount();
            if (String.valueOf(coou16).equals("34")) {
                for (int i = 1; i <= 100; i++) {
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN discount_type text");
                }
            }
            coo16.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo17 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou17 = coo17.getColumnCount();
            if (String.valueOf(coou17).equals("35")) {
                for (int i = 1; i <= 100; i++) {
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN barcode_get text");
                }
            }
            coo17.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co8 = db1.rawQuery("SELECT * FROM Billnumber", null);
            int cou8 = co8.getColumnCount();
            if (String.valueOf(cou8).equals("11")){
                db1.execSQL("ALTER TABLE Billnumber ADD COLUMN billcount text");
            }
            co8.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co9 = db1.rawQuery("SELECT * FROM Generalorderlistascdesc1", null);
            int cou9 = co9.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou9).equals("17")){
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN individualtotal text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN billcount text");
            }
            co9.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo91 = db1.rawQuery("SELECT * FROM Generalorderlistascdesc1", null);
            int coou91 = coo91.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
            if (String.valueOf(coou91).equals("19")){
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN hsn_code text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_per text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN globaltax_rs text");
            }
            coo91.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo911 = db1.rawQuery("SELECT * FROM Generalorderlistascdesc1", null);
            int coou911 = coo911.getColumnCount();
            if (String.valueOf(coou911).equals("24")){
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name2 text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs2 text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name3 text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs3 text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name4 text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs4 text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name5 text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs5 text");
            }
            coo911.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co10 = db1.rawQuery("SELECT * FROM Discountdetails", null);
            int cou10 = co10.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou10).equals("10")){
                db1.execSQL("ALTER TABLE Discountdetails ADD COLUMN billcount text");
            }
            co10.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co11 = db1.rawQuery("SELECT * FROM Cancelwiseorderlistitems", null);
            int cou11 = co11.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou11).equals("8")){
                db1.execSQL("ALTER TABLE Cancelwiseorderlistitems ADD COLUMN billcount text");
            }
            co11.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co12 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou12 = co12.getColumnCount();
            if (String.valueOf(cou12).equals("7")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN date1 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN time1 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN date text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN total text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN deposit text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cashout text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN credit text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN charges text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN authentication_pin text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN otp text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN dob text");
            }
            co12.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co13 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou13 = co13.getColumnCount();
            if (String.valueOf(cou13).equals("18")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN refunds text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN total_amount text");
            }
            co13.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co14 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou14 = co14.getColumnCount();
            if (String.valueOf(cou14).equals("20")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cashout_type text");
            }
            co14.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co15 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou15 = co15.getColumnCount();
            if (String.valueOf(cou15).equals("21")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN credit_default text");
            }
            co15.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co16 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou16 = co16.getColumnCount();
            if (String.valueOf(cou16).equals("22")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN commission_charges text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN commission_charges_type text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN commission_charges_status text");
            }
            co16.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co17 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou17 = co17.getColumnCount();
            if (String.valueOf(cou17).equals("25")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN authentication_pin_status text");
            }
            co17.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co18 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou18 = co18.getColumnCount();
            if (String.valueOf(cou18).equals("26")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN dob_alaram text");
            }
            co18.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co19 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou19 = co19.getColumnCount();
            if (String.valueOf(cou19).equals("27")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN default_discount_status text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN default_discount_amount text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN default_discount_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN default_discount_type text");
            }
            co19.close();


            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co20 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou20 = co20.getColumnCount();
            if (String.valueOf(cou20).equals("31")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN notes text");
            }
            co20.close();


            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co21 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou21 = co21.getColumnCount();
            if (String.valueOf(cou21).equals("32")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cust_account_no text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cust_ifsc_code text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cust_account_holder_name text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cust_bank_name text");
            }
            co21.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co22 = db1.rawQuery("SELECT * FROM All_Sales", null);
            int cou22 = co22.getColumnCount();
            if (String.valueOf(cou22).equals("33")){
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN datetimee_new text");
            }
            co22.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co221 = db1.rawQuery("SELECT * FROM All_Sales", null);
            int cou221 = co221.getColumnCount();
            if (String.valueOf(cou221).equals("34")){
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN hsn_code text");
            }
            co221.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co222 = db1.rawQuery("SELECT * FROM All_Sales", null);
            int cou222 = co222.getColumnCount();
            if (String.valueOf(cou222).equals("35")){
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN taxname2 text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN tax2 text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN taxname3 text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN tax3 text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN taxname4 text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN tax4 text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN taxname5 text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN tax5 text");
            }
            co222.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co223 = db1.rawQuery("SELECT * FROM All_Sales", null);
            int cou223 = co223.getColumnCount();
            if (String.valueOf(cou223).equals("43")){
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN category text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN counterperson_username text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN counterperson_name text");
            }
            co223.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co224 = db1.rawQuery("SELECT * FROM All_Sales", null);
            int cou224 = co224.getColumnCount();
            if (String.valueOf(cou224).equals("46")) {
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN credit text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN Phone_num text");
            }
            co224.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co225 = db1.rawQuery("SELECT * FROM All_Sales", null);
            int cou225 = co225.getColumnCount();
            if (String.valueOf(cou225).equals("48")) {
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN barcode_get text");
            }
            co225.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co25 = db1.rawQuery("SELECT * FROM Billnumber", null);
            int cou25 = co25.getColumnCount();
            if (String.valueOf(cou25).equals("12")){
                db1.execSQL("ALTER TABLE Billnumber ADD COLUMN datetimee_new text");
            }
            co25.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co23 = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
            int cou23 = co23.getColumnCount();
            if (String.valueOf(cou23).equals("29")){
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN datetimee_new text");
            }
            co23.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co231 = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
            int cou231 = co231.getColumnCount();
            if (String.valueOf(cou231).equals("30")) {
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN taxname2 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN tax2 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN taxname3 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN tax3 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN taxname4 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN tax4 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN taxname5 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN tax5 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN hsn_code text");
            }
            co231.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co24 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou24 = co24.getColumnCount();
            if (String.valueOf(cou24).equals("36")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN datetimee_new text");
            }
            co24.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co26 = db1.rawQuery("SELECT * FROM Discountdetails", null);
            int cou26 = co26.getColumnCount();
            if (String.valueOf(cou26).equals("11")){
                db1.execSQL("ALTER TABLE Discountdetails ADD COLUMN datetimee_new text");
            }
            co26.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co27 = db1.rawQuery("SELECT * FROM Cardnumber", null);
            int cou27 = co27.getColumnCount();
            if (String.valueOf(cou27).equals("3")){
                db1.execSQL("ALTER TABLE Cardnumber ADD COLUMN datetimee_new text");
            }
            co27.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co241 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou241 = co241.getColumnCount();
            if (String.valueOf(cou241).equals("37")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN user_id text");
            }
            co241.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co242 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou242 = co242.getColumnCount();
            if (String.valueOf(cou242).equals("38")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax1 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax2 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax3 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax4 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax5 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax6 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax7 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax8 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax9 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax10 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax11 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax12 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax13 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax14 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax15 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax_selection text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax1_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax2_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax3_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax4_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax5_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax6_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax7_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax8_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax9_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax10_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax11_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax12_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax13_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax14_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax15_value text");


                Cursor cf = db1.rawQuery("SELECT * FROM Customerdetails", null);
                if (cf.moveToFirst()) {
                    do {
                        String id = cf.getString(0);
                        int i = 1;
                        Cursor c1_11 = db.rawQuery("SELECT * FROM Taxes WHERE taxtype = 'Globaltax'", null);
                        if (c1_11.moveToFirst()) {
                            do {
                                String tn = c1_11.getString(1);
                                String tn_value = c1_11.getString(2);
                                ContentValues contentValues1 = new ContentValues();
                                contentValues1.put("tax" + i, tn);
                                contentValues1.put("tax" + i + "_value", tn_value);
                                contentValues1.put("tax_selection", "All");
                                String wherecu1 = "_id = '" + id + "'";
                                db1.update("Customerdetails", contentValues1, wherecu1, new String[]{});
                                i++;
                            } while (c1_11.moveToNext());
                        }
                        c1_11.close();
                    }while (cf.moveToNext());
                }
                cf.close();

            }
            co242.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co251 = db1.rawQuery("SELECT * FROM Billnumber", null);
            int cou251 = co251.getColumnCount();
            if (String.valueOf(cou251).equals("13")){
                db1.execSQL("ALTER TABLE Billnumber ADD COLUMN comments_sales text");
            }
            co251.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co28 = db1.rawQuery("SELECT * FROM Cusotmer_activity_temp", null);
            int cou28 = co28.getColumnCount();
            if (String.valueOf(cou28).equals("11")){
                db1.execSQL("ALTER TABLE Cusotmer_activity_temp ADD COLUMN cust_id text");
            }
            co28.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co29 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou29 = co29.getColumnCount();
            if (String.valueOf(cou29).equals("69")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN proceedings text DEFAULT off");
            }
            co29.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co290 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou290 = co290.getColumnCount();
            if (String.valueOf(cou290).equals("70")) {
//                    SaleType text, Cheque_num text,CreditAmount text, SaleTime text, SaleDate text
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN SaleType text DEFAULT off");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN Cheque_num text DEFAULT off");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN CreditAmount text DEFAULT off");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN SaleTime text DEFAULT off");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN SaleDate text DEFAULT off");
            }
            co290.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co291 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou291 = co291.getColumnCount();
            if (String.valueOf(cou291).equals("75")) {
//                    SaleType text, Cheque_num text,CreditAmount text, SaleTime text, SaleDate text
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN Transaction_ID text DEFAULT off");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN Card_Type text DEFAULT off");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN Card_Num text DEFAULT off");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN RRN text DEFAULT off");
            }
            co291.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co292 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou292 = co292.getColumnCount();
            if (String.valueOf(cou292).equals("79")) {
//                    SaleType text, Cheque_num text,CreditAmount text, SaleTime text, SaleDate text
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN pincode text");
            }
            co292.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co30 = db1.rawQuery("SELECT * FROM Itemwiseorderlistitems", null);
            int cou30 = co30.getColumnCount();
            if (String.valueOf(cou30).equals("6")){
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN category");
            }
            co30.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co301 = db1.rawQuery("SELECT * FROM Itemwiseorderlistitems", null);
            int cou301 = co301.getColumnCount();
            if (String.valueOf(cou301).equals("7")){
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN i_buying_price");
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN mi_buying_price");
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN i_cost_value");
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN i_cost_percent");
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN mi_cost_value");
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN mi_cost_percent");
            }
            co301.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co302 = db1.rawQuery("SELECT * FROM Itemwiseorderlistitems", null);
            int cou302 = co302.getColumnCount();
            if (String.valueOf(cou302).equals("13")){
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN barcode_value");
            }
            co302.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co31 = db1.rawQuery("SELECT * FROM Itemwiseorderlistmodifiers", null);
            int cou31 = co31.getColumnCount();
            if (String.valueOf(cou31).equals("6")){
                db1.execSQL("ALTER TABLE Itemwiseorderlistmodifiers ADD COLUMN category");
            }
            co31.close();



            Cursor items1 = db.rawQuery("SELECT * FROM Items WHERE image is null", null);
            if (items1.moveToFirst()){
                do {
                    String id = items1.getString(0);
                    String name = items1.getString(1);
                    //Toast.makeText(MainActivity.this, "image not hter "+name, Toast.LENGTH_SHORT).show();
                    String str1 = name.substring(0, 2);
                    String str2 = str1.toUpperCase();
                    ContentValues contentValues5 = new ContentValues();
                    contentValues5.put("image", "");
                    contentValues5.put("image_text", str2);
                    String where = "_id = '" + id + "'";
                    db.update("Items", contentValues5, where, new String[]{});
                    String where1_v1 = "docid = '" + id + "'";
                    db.update("Items_Virtual", contentValues5, where1_v1, new String[]{});
                }while (items1.moveToNext());
            }
            items1.close();

            Cursor items2 = db.rawQuery("SELECT * FROM Items WHERE image = '' ", null);
            if (items2.moveToFirst()){
                do {
                    String id = items2.getString(0);
                    String name = items2.getString(1);
                    //Toast.makeText(MainActivity.this, "image not hter "+name, Toast.LENGTH_SHORT).show();
                    String str1 = name.substring(0, 2);
                    String str2 = str1.toUpperCase();
                    ContentValues contentValues5 = new ContentValues();
                    contentValues5.put("image", "");
                    contentValues5.put("image_text", str2);
                    String where = "_id = '" + id + "'";
                    db.update("Items", contentValues5, where, new String[]{});
                    String where1_v1 = "docid = '" + id + "'";
                    db.update("Items_Virtual", contentValues5, where1_v1, new String[]{});
                }while (items2.moveToNext());
            }
            items2.close();


            Cursor items3 = db.rawQuery("SELECT * FROM Items", null);
            if (items3.moveToFirst()){
                do {
                    String id = items3.getString(0);
                    String name = items3.getString(1);

                    if (name.contains("'")) {
                        replacecolumnvalue = name.replace("'", " ");

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("itemname", replacecolumnvalue);

                        String where1 = "_id = '" + id + "' ";
                        db.update("Items", contentValues, where1, new String[]{});
                        String where1_v1 = "docid = '" + id + "'";
                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
                    }

                }while (items3.moveToNext());
            }
            items3.close();


            Cursor items4 = db.rawQuery("SELECT * FROM Modifiers", null);
            if (items4.moveToFirst()){
                do {
                    String id = items4.getString(0);
                    String name = items4.getString(1);

                    if (name.contains("'")) {
                        replacemodifiervalue = name.replace("'", " ");

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("modifiername", replacemodifiervalue);

                        String where1 = "_id = '" + id + "' ";
                        db.update("Modifiers", contentValues, where1, new String[]{});
                    }

                }while (items4.moveToNext());
            }
            items4.close();


            Cursor items5 = db.rawQuery("SELECT * FROM Taxes", null);
            if (items5.moveToFirst()){
                do {
                    String id = items5.getString(0);
                    String name = items5.getString(1);

                    if (name.contains("'")) {
                        replacetaxvalue = name.replace("'", " ");

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("taxname", replacetaxvalue);

                        String where1 = "_id = '" + id + "' ";
                        db.update("Taxes", contentValues, where1, new String[]{});
                    }

                }while (items5.moveToNext());
            }
            items5.close();


            Cursor items6 = db.rawQuery("SELECT * FROM Hotel", null);
            if (items6.moveToFirst()){
                do {
                    String id = items6.getString(0);
                    String name = items6.getString(1);

                    if (name.contains("'")) {
                        replacecategoryvalue = name.replace("'", " ");

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("name", replacecategoryvalue);

                        String where1 = "_id = '" + id + "' ";
                        db.update("Hotel", contentValues, where1, new String[]{});
                    }

                }while (items6.moveToNext());
            }
            items6.close();


            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor fourteen = db1.rawQuery("SELECT * FROM All_Sales", null);
            if (fourteen.moveToFirst()){
                do {
                    String id = fourteen.getString(0);
                    String dt = fourteen.getString(26);
                    String datetimee_new = fourteen.getString(33);

                    TextView tv = new TextView(getActivity());
                    tv.setText(datetimee_new);

                    if (tv.getText().toString().equals("")) {

                        if (dt.toString().contains(":")) {
                            dt = dt.replace(":", "");
                        }

                        dt = dt.substring(0, 12);
//                Toast.makeText(MainActivity.this, " "+dt, Toast.LENGTH_SHORT).show();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("datetimee_new", dt);
                        String where11 = "_id = '" + id + "' ";
                        db1.update("All_Sales", contentValues, where11, new String[]{});
                    }
                }while (fourteen.moveToNext());
            }
            fourteen.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor fifteen = db1.rawQuery("SELECT * FROM Billnumber", null);
            if (fifteen.moveToFirst()){
                do {
                    String id = fifteen.getString(0);
                    String billno = fifteen.getString(1);
                    String datetimee_new = fifteen.getString(12);

                    TextView tv = new TextView(getActivity());
                    tv.setText(datetimee_new);

                    if (tv.getText().toString().equals("")) {
                        Cursor fifteen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '"+billno+"'", null);
                        if (fifteen_1.moveToFirst()){
                            String a = fifteen_1.getString(33);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '"+id+"' ";
                            db1.update("Billnumber", contentValues, where11, new String[]{});
                        }else {
                            Cursor fifteen_11 = db1.rawQuery("SELECT * FROM All_Sales_Cancelled WHERE bill_no = '"+billno+"'", null);
                            if (fifteen_11.moveToFirst()){
                                String a = fifteen_11.getString(29);

                                ContentValues contentValues = new ContentValues();
                                contentValues.put("datetimee_new", a);
                                String where11 = "_id = '"+id+"' ";
                                db1.update("Billnumber", contentValues, where11, new String[]{});
                            }
                            fifteen_11.close();
                        }
                        fifteen_1.close();
                    }


                }while (fifteen.moveToNext());
            }
            fifteen.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor sixteen = db1.rawQuery("SELECT * FROM Customerdetails", null);
            if (sixteen.moveToFirst()){
                do {
                    String id = sixteen.getString(0);
                    String billno = sixteen.getString(6);
                    String datetimee_new = sixteen.getString(36);

                    TextView tv = new TextView(getActivity());
                    tv.setText(datetimee_new);

                    if (tv.getText().toString().equals("")) {
                        Cursor sixteen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                        if (sixteen_1.moveToFirst()) {
                            String a = sixteen_1.getString(33);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("Customerdetails", contentValues, where11, new String[]{});
                        }
                        sixteen_1.close();
                    }
                }while (sixteen.moveToNext());
            }
            sixteen.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor seventeen = db1.rawQuery("SELECT * FROM Discountdetails", null);
            if (seventeen.moveToFirst()){
                do {
                    String id = seventeen.getString(0);
                    String billno = seventeen.getString(3);
                    String datetimee_new = seventeen.getString(11);

                    TextView tv = new TextView(getActivity());
                    tv.setText(datetimee_new);

                    if (tv.getText().toString().equals("")) {
                        Cursor seventeen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                        if (seventeen_1.moveToFirst()) {
                            String a = seventeen_1.getString(33);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("Discountdetails", contentValues, where11, new String[]{});
                        }
                        seventeen_1.close();
                    }
                }while (seventeen.moveToNext());
            }
            seventeen.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor eightteen = db1.rawQuery("SELECT * FROM Cardnumber", null);
            if (eightteen.moveToFirst()){
                do {
                    String id = eightteen.getString(0);
                    String billno = eightteen.getString(2);
                    String datetimee_new = eightteen.getString(3);

                    TextView tv = new TextView(getActivity());
                    tv.setText(datetimee_new);

                    if (tv.getText().toString().equals("")) {
                        Cursor eightteen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                        if (eightteen_1.moveToFirst()) {
                            String a = eightteen_1.getString(33);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("Cardnumber", contentValues, where11, new String[]{});
                        }
                        eightteen_1.close();
                    }
                }while (eightteen.moveToNext());
            }
            eightteen.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor nineteen = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
            if (nineteen.moveToFirst()){
                do {
                    String id = nineteen.getString(0);
                    String billno = nineteen.getString(11);
                    String datetimee_new = nineteen.getString(29);

                    TextView tv = new TextView(getActivity());
                    tv.setText(datetimee_new);

                    if (tv.getText().toString().equals("")) {
                        Cursor nineteen_1 = db1.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billno + "'", null);
                        if (nineteen_1.moveToFirst()) {
                            String a = nineteen_1.getString(12);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("All_Sales_Cancelled", contentValues, where11, new String[]{});
                        }
                        nineteen_1.close();
                    }
                }while (nineteen.moveToNext());
            }
            nineteen.close();



            Cursor change_time = db.rawQuery("SELECT * FROM Change_time_format", null);
            if (change_time.moveToFirst()) {
                String zero = change_time.getString(0);
                one_one = change_time.getString(1);
            }
            change_time.close();

            if (one_one.toString().equals("not changed")) {
                db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor fourteen_11 = db1.rawQuery("SELECT * FROM All_Sales", null);
                if (fourteen_11.moveToFirst()) {
                    do {
                        String id = fourteen_11.getString(0);
                        String dt = fourteen_11.getString(26);
                        String dt_new = fourteen_11.getString(33);

                        String dt1 = dt.substring(8, 10);

                        if (dt1.toString().equals("24")){
                            dt = dt.replace(dt1, "00");

                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("datetimee", dt);
                            String where11 = "_id = '"+id+"' ";
                            db1.update("All_Sales", contentValues1, where11, new String[]{});
                        }

                        String dt2 = dt_new.substring(8, 10);

                        if (dt2.toString().equals("24")){
                            dt_new = dt_new.replace(dt2, "00");

                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("datetimee_new", dt_new);
                            String where11 = "_id = '"+id+"' ";
                            db1.update("All_Sales", contentValues1, where11, new String[]{});
                        }



                    } while (fourteen_11.moveToNext());
                }
                fourteen_11.close();

                db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor fifteenn = db1.rawQuery("SELECT * FROM Billnumber", null);
                if (fifteenn.moveToFirst()) {
                    do {
                        String id = fifteenn.getString(0);
                        String billno = fifteenn.getString(1);
                        String dt_new = fifteenn.getString(12);
                        TextView tv1 = new TextView(getActivity());
                        tv1.setText(billno);

                        String dt2 = dt_new.substring(8, 10);
                        if (dt2.toString().equals("24")){
                            dt_new = dt_new.replace(dt2, "00");
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", dt_new);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("Billnumber", contentValues, where11, new String[]{});
                        }

//                    Cursor fifteen_11 = null;
//                    try {
//                        Cursor fifteen_11 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + tv1.getText().toString() + "'", null);
//                        if (fifteen_11.moveToFirst()) {
//                            String a = fifteen_11.getString(33);
//
//
//                        }
//                    }finally {
//                        if(fifteen_11 != null)
//                            fifteen_11.close();
//                    }

                    } while (fifteenn.moveToNext());
                }
                fifteenn.close();

                db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor sixteenn = db1.rawQuery("SELECT * FROM Customerdetails", null);
                if (sixteenn.moveToFirst()) {
                    do {
                        String id = sixteenn.getString(0);
                        String billno = sixteenn.getString(6);

                        Cursor sixteen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                        if (sixteen_1.moveToFirst()) {
                            String a = sixteen_1.getString(33);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("Customerdetails", contentValues, where11, new String[]{});
                        }
                        sixteen_1.close();
                    } while (sixteenn.moveToNext());
                }
                sixteenn.close();

                db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor seventeenn = db1.rawQuery("SELECT * FROM Discountdetails", null);
                if (seventeenn.moveToFirst()) {
                    do {
                        String id = seventeenn.getString(0);
                        String billno = seventeenn.getString(3);

                        Cursor seventeen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                        if (seventeen_1.moveToFirst()) {
                            String a = seventeen_1.getString(33);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("Discountdetails", contentValues, where11, new String[]{});
                        }
                        seventeen_1.close();
                    } while (seventeenn.moveToNext());
                }
                seventeenn.close();

                db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor eightteenn = db1.rawQuery("SELECT * FROM Cardnumber", null);
                if (eightteenn.moveToFirst()) {
                    do {
                        String id = eightteenn.getString(0);
                        String billno = eightteenn.getString(2);

                        Cursor eightteen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                        if (eightteen_1.moveToFirst()) {
                            String a = eightteen_1.getString(33);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("Cardnumber", contentValues, where11, new String[]{});
                        }
                        eightteen_1.close();
                    } while (eightteenn.moveToNext());
                }
                eightteenn.close();

                db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor nineteenn = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
                if (nineteenn.moveToFirst()) {
                    do {
                        String id = nineteenn.getString(0);
                        String billno = nineteenn.getString(11);

                        Cursor nineteen_1 = db1.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billno + "'", null);
                        if (nineteen_1.moveToFirst()) {
                            String a = nineteen_1.getString(12);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("All_Sales_Cancelled", contentValues, where11, new String[]{});
                        }
                        nineteen_1.close();
                    } while (nineteenn.moveToNext());
                }
                nineteenn.close();
            }

            Cursor fifteen_1 = db.rawQuery("SELECT * FROM Change_time_format", null);
            if (fifteen_1.moveToFirst()) {
                String zero = fifteen_1.getString(0);
                ContentValues contentValues = new ContentValues();
                contentValues.put("status", "changed");

                String where11 = "_id = '"+zero+"' ";
                db.update("Change_time_format", contentValues, where11, new String[]{});
            }
            fifteen_1.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor usr_id_null = db1.rawQuery("SELECT * FROM Customerdetails WHERE user_id is null", null);
            if (usr_id_null.moveToFirst()){
                do {
                    String id = usr_id_null.getString(0);
                    ContentValues contentValues5 = new ContentValues();
                    contentValues5.put("user_id", "");
                    String where = "_id = '" + id + "'";
                    db1.update("Customerdetails", contentValues5, where, new String[]{});
                }while (usr_id_null.moveToNext());
            }
            usr_id_null.close();


            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor mail_schedule = db.rawQuery("SELECT * FROM Schedule_mail_on_off ", null);
            if (mail_schedule.moveToFirst()) {

            }else {

                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", "1");
                contentValues.put("status", "Off");
                db.insert("Schedule_mail_on_off", null, contentValues);

            }
            mail_schedule.close();


            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor printer_type = db.rawQuery("SELECT * FROM Printer_type", null);
            if (printer_type.moveToFirst()) {

            }else {

                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", "1");
                contentValues.put("printer_type", "Generic");
                db.insert("Printer_type", null, contentValues);

            }
            printer_type.close();


            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor user101 = db.rawQuery("SELECT * FROM Schedule_mail_time ", null);
            if (user101.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("time", "11:30 PM");
                db.insert("Schedule_mail_time", null, contentValues);
            }
            user101.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor user102 = db.rawQuery("SELECT * FROM BIllingmode ", null);
            if (user102.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", "1");
                contentValues.put("billingtype", "Fine dine");
                db.insert("BIllingmode", null, contentValues);
            }
            user102.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor billing_type = db.rawQuery("SELECT * FROM BIllingtype ", null);
            if (billing_type.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", "1");
                contentValues.put("billingtype_type", "Dine-in");
                db.insert("BIllingtype", null, contentValues);
            }
            billing_type.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor estimate_bill = db.rawQuery("SELECT * FROM Estimate_print ", null);
            if (estimate_bill.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", "1");
                contentValues.put("status", "Yes");
                db.insert("Estimate_print", null, contentValues);
            }
            estimate_bill.close();

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);

            dialog.setMessage(getString(R.string.setmessage2));
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


        @Override
        protected void onPostExecute(Integer file_url) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();


            Intent intent = new Intent(getActivity(), SyncHelperService.class);
            intent.putExtra("backup","both");
            getActivity().startService(intent);


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();

                    final Dialog dialog1 = new Dialog(getActivity(), R.style.notitle);
                    dialog1.setContentView(R.layout.restore_confirmation);
                    dialog1.show();

                    TextView successfilename = (TextView)dialog1.findViewById(R.id.restore_filename);
                    successfilename.setText("'" + backupname.getText().toString() + "'");

                    Button done = (Button)dialog1.findViewById(R.id.gohome);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                        }
                    });

                    Button gotohome = (Button)dialog1.findViewById(R.id.gotohome);
                    gotohome.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (account_selection.toString().equals("Dine")) {
                                Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                                if (cursor3.moveToFirst()) {
                                    String lite_pro = cursor3.getString(1);

                                    TextView tv = new TextView(getActivity());
                                    tv.setText(lite_pro);

                                    if (tv.getText().toString().equals("Lite")) {
                                        Intent intent = new Intent(getActivity(), BeveragesMenuFragment_Dine_l.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(getActivity(), BeveragesMenuFragment_Dine.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                }else {
                                    Intent intent = new Intent(getActivity(), BeveragesMenuFragment_Dine_l.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }else {
                                if (account_selection.toString().equals("Qsr")) {
                                    Intent intent = new Intent(getActivity(), BeveragesMenuFragment_Qsr.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(getActivity(), BeveragesMenuFragment_Retail.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }
//                            Intent intent = new Intent(getActivity(), BeveragesMenuFragment_Dine.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
                        }
                    });
                }
            }, 10000); //3000 L = 3 detik




        }

    }

    class DownloadMusicfromInternet1 extends AsyncTask<String, Void, Integer> {
        private ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);

        @Override
        protected Integer doInBackground(String... params) {

            try {
//                File sd = Environment.getExternalStorageDirectory();
                File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File data = Environment.getDataDirectory();

                if (sd.canWrite()) {
                    String currentDBPath = "//data//" + "com.intuition.ivepos"
                            + "//databases//" + "mydb_Appdata";
                    String backupDBPath = "/IVEPOS_backup/" + backupname.getText().toString() + "/" + "mydb_Appdata";

//                                                    Toast.makeText(getActivity(), "1 " + currentDBPath, Toast.LENGTH_LONG)
//                                                            .show();
//
//                                                    Toast.makeText(getActivity(), "11 " + backupDBPath, Toast.LENGTH_LONG)
//                                                            .show();

//                                            boolean dbexist = DATA_DIRECTORY_DATABASE;
//                                            if (dbexist) {
//
//                                            }
//                                            DATA_DIRECTORY_DATABASE


                    File file = new File("/data/data/com.intuition.ivepos/databases/mydb_Appdata");
                    if(file.exists()){
//                                                        Toast.makeText(getActivity(), "exists", Toast.LENGTH_LONG)
//                                                                .show();
                    }
                    if (DATA_DIRECTORY_DATABASE.exists()){
//                                                        Toast.makeText(getActivity(), "existsssss", Toast.LENGTH_LONG)
//                                                                .show();
                    }else {
//                                                        Toast.makeText(getActivity(), "not exists", Toast.LENGTH_LONG)
//                                                                .show();
                    }

                    File backupDB = new File(data, currentDBPath);
                    File currentDB = new File(sd, backupDBPath);

                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
//                                                    Toast.makeText(getActivity(),"111 "+ backupDB.toString(),
//                                                            Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {

                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG)
                        .show();

            }



            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);


            db.execSQL("CREATE TABLE if not exists Adminrights (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, username text, password text);");
            db.execSQL("CREATE TABLE if not exists BIllingmode (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, billingtype text);");
            db.execSQL("CREATE TABLE if not exists Barcodescannerconnectivity (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, barcodescannercontype text);");
            db.execSQL("CREATE TABLE if not exists CATEGORY (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, ALL_CATEGORY text);");
            db.execSQL("CREATE TABLE if not exists Cashdrawerconnectivity (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, cashdrawercontype text);");
            db.execSQL("CREATE TABLE if not exists Companydetailss (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, companyname text, doorno text," +
                    "substreet text, street text, city text, state text, country text, pincode INTEGER, phoneno INTEGER, taxone text, taxtwo text, footerone text, footertwo text," +
                    "address1 text, email text, website text, address2 text, address3 text );");
            db.execSQL("CREATE TABLE if not exists Hotel (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, direccion text, telefono text, email text);");
            db.execSQL("CREATE TABLE if not exists Items (_id integer PRIMARY KEY AUTOINCREMENT, itemname text, price NUMERIC, stockquan NUMERIC, category text, itemtax text," +
                    "image blob, weekdaysvalue text, weekendsvalue text, manualstockvalue text, automaticstockresetvalue text, clickcount text, favourites text," +
                    "disc_type text, disc_value text, image_text text, barcode_value text, checked text, print_value text);");
            db.execSQL("CREATE TABLE if not exists Items1 (_id integer PRIMARY KEY AUTOINCREMENT, itemname text, price NUMERIC, stockquan NUMERIC, category text, itemtax text," +
                    "image blob, weekdaysvalue TEXT, weekendsvalue TEXT, manualstockvalue TEXT, automaticstockresetvalue TEXT, clickcount TEXT, favourites TEXT);");
            db.execSQL("CREATE TABLE if not exists Itemsstockvalue (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, weekdaysvalue text, weekendsvalue text);");
            db.execSQL("CREATE TABLE if not exists LAdmin (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, username text, password text, name text);");
            db.execSQL("CREATE TABLE if not exists LOGIN (ID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, USERNAME text, PASSWORD textpublic, name text);");
            db.execSQL("CREATE TABLE if not exists Logo (_id INTEGER PRIMARY KEY UNIQUE, companylogo blob);");
            db.execSQL("CREATE TABLE if not exists Modifiers (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, modifiername text, modprice numeric, modstockquan numeric, " +
                    "modcategory text, moditemtax text, modimage BLOB, mod_image_text text);");
            db.execSQL("CREATE TABLE if not exists Printerconnectivity (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, printercontype text);");
            db.execSQL("CREATE TABLE if not exists Printerreceiptsize(_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, papersize text)");
            db.execSQL("CREATE TABLE if not exists Quickaccess (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text);");
            db.execSQL("CREATE TABLE if not exists Quickedit (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, quickedittype text);");
            db.execSQL("CREATE TABLE if not exists ResetFrequencyRestaurant (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, resetfrequencyrestaurant text);");
            db.execSQL("CREATE TABLE if not exists ResetFrequencyRetail (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, resetfrequencyretail text);");
            db.execSQL("CREATE TABLE if not exists Stockcontrol (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, stockcontroltype text);");
            db.execSQL("CREATE TABLE if not exists Itemsort (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, itemsorttype text);");
            db.execSQL("CREATE TABLE if not exists Stockreset (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, stockresettype text);");
            db.execSQL("CREATE TABLE if not exists Stockresetmode (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, stockresetoptionsmode text);");
            db.execSQL("CREATE TABLE if not exists Storedays (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, weekdays text, weekends text, swap text);");
            db.execSQL("CREATE TABLE if not exists Universalcredentials (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, username text, password text);");
            db.execSQL("CREATE TABLE if not exists User1 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
            db.execSQL("CREATE TABLE if not exists User2 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
            db.execSQL("CREATE TABLE if not exists User3 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
            db.execSQL("CREATE TABLE if not exists User4 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
            db.execSQL("CREATE TABLE if not exists User5 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
            db.execSQL("CREATE TABLE if not exists User6 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
            db.execSQL("CREATE TABLE if not exists Taxes (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, taxname text, value numeric, taxtype text);");
            db.execSQL("CREATE TABLE if not exists Discount_types (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, discountname text, discountvalue numeric);");
            db.execSQL("CREATE TABLE if not exists Totaltables (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, nooftables text);");
            db.execSQL("CREATE TABLE if not exists asd1 (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, pName text, pDate text, image blob);");

            db.execSQL("CREATE TABLE if not exists LoginUser (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, username text, password text);");
            db.execSQL("CREATE TABLE if not exists UserLogin_Checking (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, username text, password text, name text);");


            db.execSQL("CREATE TABLE if not exists Alaramon_off (_id integer PRIMARY KEY UNIQUE, status text);");
            db.execSQL("CREATE TABLE if not exists Alaramdays (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, selecteddays TEXT, unselecteddays text, swap TEXT);");
            db.execSQL("CREATE TABLE if not exists Alaramtime (_id integer PRIMARY KEY UNIQUE, time text);");

            db.execSQL("CREATE TABLE if not exists BTConn (_id integer PRIMARY KEY UNIQUE, name text, address text, status text, device text);");
            db.execSQL("CREATE TABLE if not exists IPConn (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");

            db.execSQL("CREATE TABLE if not exists Menulogin_checking (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, status text);");
            db.execSQL("CREATE TABLE if not exists Home_check (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, login_status text);");
            //dbllega.execSQL("CREATE TABLE if not exists asd2 (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, pName text, pDate text, image blob);");

            db.execSQL("CREATE TABLE if not exists DeleteDBon_off (_id integer PRIMARY KEY UNIQUE, status text);");
            db.execSQL("CREATE TABLE if not exists Auto_generate_barcode (_id integer PRIMARY KEY UNIQUE, generate text);");
            db.execSQL("CREATE TABLE if not exists DeleteDB_time (_id integer PRIMARY KEY UNIQUE, time text);");
            db.execSQL("CREATE TABLE if not exists Email_setup (_id integer PRIMARY KEY UNIQUE, username text, password text, client text);");
            db.execSQL("CREATE TABLE if not exists Default_credit (_id integer PRIMARY KEY UNIQUE, status text);");
            db.execSQL("CREATE TABLE if not exists Working_hours (_id integer PRIMARY KEY UNIQUE, opening text, opening_time text, closing text, closing_time text," +
                    "opening_time_system text, closing_time_system text);");
            db.execSQL("CREATE TABLE if not exists Printer_text_size (_id integer PRIMARY KEY UNIQUE, type text);");
            db.execSQL("CREATE TABLE if not exists Change_time_format (_id integer PRIMARY KEY UNIQUE, status text);");
            db.execSQL("CREATE TABLE if not exists Hotel1 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, value int);");
            db.execSQL("CREATE TABLE if not exists Discount_details (_id INTEGER PRIMARY KEY UNIQUE, disc_code text, disc_value text, disc_type text);");
            db.execSQL("CREATE TABLE if not exists Email_recipient (_id integer PRIMARY KEY UNIQUE, name text, ph_no text, email text);");
            db.execSQL("CREATE TABLE if not exists Schedule_mail_on_off (_id integer PRIMARY KEY UNIQUE, status text);");
            db.execSQL("CREATE TABLE if not exists Schedule_mail_time (_id integer PRIMARY KEY UNIQUE, time text);");
            db.execSQL("CREATE TABLE if not exists promotions (_id integer PRIMARY KEY UNIQUE, email text);");
            db.execSQL("CREATE TABLE if not exists User_privilege (_id integer PRIMARY KEY UNIQUE, username text, returns_refunds text, product_tax text, reports text," +
                    "settings text, backup text, customer text);");
            db.execSQL("CREATE TABLE if not exists Tax_selec (_id integer PRIMARY KEY UNIQUE, tax_amount text, tax_per text, selected_but text);");
            db.execSQL("CREATE TABLE if not exists Discount_selec (_id integer PRIMARY KEY UNIQUE, discount_amount text, discount_per text, selected_but text);");
            db.execSQL("CREATE TABLE if not exists Vendor_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text, " +
                    "vendor_email text, vendor_address text, vendor_gst text);");
            db.execSQL("CREATE TABLE if not exists Vendor_sold_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text," +
                    "invoice text, billamount text, tax_percent text, tax_amount text, disc_percent text, disc_amount text, total_bill_amount text, from_time text," +
                    "from_date text, due_date text, datetimee_new_from text, datetimee_new_due text, pay text, pending text, total_pay text, pay_date text, pay_time text, pay_datetimeemew text, not_required text);");
            db.execSQL("CREATE TABLE if not exists Vendor_sold_item_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text," +
                    "itemname text, qty_add text, individual_price text, total_price text, invoice text, billamount text, tax_percent text, tax_amount text, disc_percent text," +
                    "disc_amount text, total_bill_amount text, from_time text, from_date text, due_date text, datetimee_new_from text, datetimee_new_due text, pay text, pending text," +
                    "tax1 text, tax2 text, tax3 text, tax4 text, tax5 text, tax6 text, tax7 text, tax8 text, tax9 text, tax10 text, tax11 text, tax12 text, tax13 text," +
                    "tax14 text, tax15 text, tax1_value text, tax2_value text, tax3_value text, tax4_value text, tax5_value text, tax6_value text, tax7_value text," +
                    "tax8_value text, tax9_value text, tax10_value text, tax11_value text, tax12_value text, tax13_value text, tax14_value text, tax15_value text);");
            db.execSQL("CREATE TABLE if not exists Ingredient_items_list (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, ingredient_name text, itemname text, item_qyt_used text," +
                    "currnet_stock text, date1 text, date text, time1 text, time text, modified_datetimee_new text, qty_unit text, required text);");
            db.execSQL("CREATE TABLE if not exists Vendor_temp_list (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vend_phon text, vend_email text," +
                    "vend_gst text, vend_address text, vend_total_bill_amount text, paid text, pending text, bill_no text);");
            db.execSQL("CREATE TABLE if not exists Items_temp_list (_id integer PRIMARY KEY UNIQUE, itemname text, avg_price text, min_price text," +
                    "max_price text, total_qty text, total_price text, barcode text, not_required text);");

            db.execSQL("CREATE TABLE if not exists Ingredient_Vendor_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text, " +
                    "vendor_email text, vendor_address text, vendor_gst text);");
            db.execSQL("CREATE TABLE if not exists Ingredient_sold_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text," +
                    "invoice text, billamount text, tax_percent text, tax_amount text, disc_percent text, disc_amount text, total_bill_amount text, from_time text," +
                    "from_date text, due_date text, datetimee_new_from text, datetimee_new_due text, pay text, pending text, total_pay text, pay_date text, pay_time text, pay_datetimeemew text, not_required text);");
            db.execSQL("CREATE TABLE if not exists Ingredients (_id integer PRIMARY KEY UNIQUE, ingredient_name text, min_req text, max_req text, current_stock text," +
                    "unit text, indiv_price text, total_price text, date text, date1 text, time text, time1 text, datetimee_new text, avg_price text, required text, barcode text," +
                    "status_low text, status_qty_updated text, add_qty text, indiv_price_copy text, adjusted_stock text, diff_stock text, indiv_price_temp text," +
                    "total_price_temp text);");
            db.execSQL("CREATE TABLE if not exists Ingredient_sold_item_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text," +
                    "itemname text, qty_add text, individual_price text, total_price text, invoice text, billamount text, tax_percent text, tax_amount text, disc_percent text," +
                    "disc_amount text, total_bill_amount text, from_time text, from_date text, due_date text, datetimee_new_from text, datetimee_new_due text, pay text, pending text," +
                    "tax1 text, tax2 text, tax3 text, tax4 text, tax5 text, tax6 text, tax7 text, tax8 text, tax9 text, tax10 text, tax11 text, tax12 text, tax13 text," +
                    "tax14 text, tax15 text, tax1_value text, tax2_value text, tax3_value text, tax4_value text, tax5_value text, tax6_value text, tax7_value text," +
                    "tax8_value text, tax9_value text, tax10_value text, tax11_value text, tax12_value text, tax13_value text, tax14_value text, tax15_value text," +
                    "wastage text, unit text);");
            db.execSQL("CREATE TABLE if not exists Ingredients_item_selection_temp (_id integer PRIMARY KEY UNIQUE, itemname text, qty_temp text, qty_temp_unit text, qty text);");
            db.execSQL("CREATE TABLE if not exists Vendor_details_micro (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text, " +
                    "vendor_email text, vendor_address text, vendor_gst text);");
            db.execSQL("CREATE TABLE if not exists Vendor_temp_list_Ingredient (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vend_phon text, vend_email text," +
                    "vend_gst text, vend_address text, vend_total_bill_amount text, paid text, pending text, bill_no text);");
            db.execSQL("CREATE TABLE if not exists Ingredients_temp_list (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, avg_price text, min_price text," +
                    "max_price text, total_qty text, total_price text, barcode text, unit text, wastage_qty text, wastage_cost text, not_required text);");
            db.execSQL("CREATE TABLE if not exists Printer_type (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, printer_type text);");

            db.execSQL("CREATE TABLE if not exists KOT_print (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, kot_print_status text);");
            db.execSQL("CREATE TABLE if not exists Auto_Connect (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, auto_connect_status text);");
            db.execSQL("CREATE TABLE if not exists Weighing_Scale_status (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, Weighing_Scale_onoff text);");
            db.execSQL("CREATE TABLE if not exists Weighing_Scale_name (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, scale_name text);");
            db.execSQL("CREATE TABLE if not exists Sync_time (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, last_time text);");
            db.execSQL("CREATE TABLE if not exists variants_temp (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vari1 text, varprice1 text, vari2 text, varprice2 text," +
                    "vari3 text, varprice3 text, vari4 text, varprice4 text, vari5 text, varprice5 text);");
            db.execSQL("CREATE TABLE if not exists PaytmMerchantReg(_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, Account text, MerchantName text, " +
                    "guid text, MID text, Merchant_key text, PosID text)");
            db.execSQL("CREATE TABLE IF NOT EXISTS MobikwikMerchantReg(_id integer primary key autoincrement unique, Account text, Merchant_name text, " +
                    "Mid_otp text, Secretkey_otp text, Mid_debit text, Secretkey_debit text)");
            db.execSQL("CREATE TABLE IF NOT EXISTS all_transactions" +
                    "(_id integer primary key autoincrement unique, Payment_medium text, merchantRefInvoiceNo text, amount text, cardHolderName text," +
                    " cardBrand text, cardType text, cardNumber text, paymentId text, transactionId text, tdrPercentage text, approved text)");
            db.execSQL("CREATE TABLE IF NOT EXISTS CardSwiperActivation" +
                    "(_id integer primary key autoincrement unique, CardSwiperName text, merchantKey text, partnerkey text, Config_status text)");
            db.execSQL("CREATE TABLE if not exists IPConn_Counter (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");
            db.execSQL("CREATE TABLE if not exists Country_Selection (_id integer PRIMARY KEY UNIQUE, country text);");
            db.execSQL("CREATE TABLE if not exists Round_off (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, round_off_status text);");
            db.execSQL("CREATE VIRTUAL TABLE if not exists Items_Virtual USING fts3(itemname , price , stockquan , category , itemtax ," +
                    "image , weekdaysvalue , weekendsvalue , manualstockvalue , automaticstockresetvalue , clickcount , favourites ," +
                    "disc_type , disc_value , image_text , barcode_value , checked , print_value , quantity_sold , minimum_qty , minimum_qty_copy , add_qty ," +
                    "status_low , status_qty_updated , individual_price , unit_type , tax_value , itemtax2 , tax_value2 , itemtax3 ,tax_value3 ," +
                    "itemtax4 ,tax_value4 ,itemtax5 ,tax_value5 , status_perm , status_temp , variant1 , variant_price1 , variant2 , variant_price2 , variant3 ," +
                    "variant_price3 , variant4 , variant_price4 , variant5 , variant_price5)");
            db.execSQL("CREATE TABLE if not exists BIllingtype (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, billingtype_type text);");
            db.execSQL("CREATE TABLE if not exists Estimate_print (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, status text);");
            db.execSQL("CREATE TABLE if not exists IPConn_KOT1 (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");
            db.execSQL("CREATE TABLE if not exists IPConn_KOT2 (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");
            db.execSQL("CREATE TABLE if not exists IPConn_KOT3 (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");
            db.execSQL("CREATE TABLE if not exists IPConn_KOT4 (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");

            db.execSQL("CREATE TABLE if not exists Name_Dept1 (_id integer PRIMARY KEY UNIQUE, dept1_name text);");
            db.execSQL("CREATE TABLE if not exists Name_Dept2 (_id integer PRIMARY KEY UNIQUE, dept2_name text);");
            db.execSQL("CREATE TABLE if not exists Name_Dept3 (_id integer PRIMARY KEY UNIQUE, dept3_name text);");
            db.execSQL("CREATE TABLE if not exists Name_Dept4 (_id integer PRIMARY KEY UNIQUE, dept4_name text);");

            db.execSQL("CREATE TABLE if not exists Ordertaking_server_ip (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");

            db.execSQL("CREATE TABLE if not exists HomeDelivery_prints (_id integer PRIMARY KEY UNIQUE, companycopy text, customercopy text);");
            db.execSQL("CREATE TABLE if not exists Billcount_tag (_id integer PRIMARY KEY UNIQUE, tag_name text);");

            db.execSQL("CREATE TABLE if not exists Stock_transfer_item_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, qty_add text," +
                    "company text, from_store text, from_device text, to_store text, to_device text, from_time text, from_date text, datetimee_new_from text);");


            db1.execSQL("CREATE TABLE if not exists All_Sales (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, quantity text, price text, total text," +
                    "type text, parent text, parentid text, mod_assigned text, tax text, taxname text, bill_no text, time text, date text, user text, table_id text, billtype text," +
                    "paymentmethod text, billamount_disapply text, billamount_disnotapply text, _idd text, deleted_not text, modifiedquantity text, quantitycopy text, " +
                    "modifiedtotal text, date1 text, datetimee text, disc_type text, disc_value text, newtotal text, disc_thereornot text," +
                    " disc_indiv_total text, new_modified_total text);");

            db1.execSQL("CREATE TABLE if not exists Itemwiseorderlistitems (_id integer PRIMARY KEY UNIQUE, itemno text, itemname text, sales integer, salespercentage integer," +
                    "itemtotalquan text);");
            db1.execSQL("CREATE TABLE if not exists Itemwiseorderlistmodifiers (_id integer PRIMARY KEY UNIQUE, modno text, modname text, sales integer, salespercentage integer," +
                    "modtotalquan text);");
            db1.execSQL("CREATE TABLE if not exists Userwiseorderlistitems (_id integer PRIMARY KEY UNIQUE, username text, sales integer, salespercentage integer);");
            db1.execSQL("CREATE TABLE if not exists Generalorderlistascdesc (_id integer PRIMARY KEY UNIQUE, date text, time text, user text, billno text, " +
                    "billdetails text, sales integer, discountamount text, paymentmethod text, billtype text, itemname text, quan text);");
            db1.execSQL("CREATE TABLE if not exists Generalorderlistascdesc1 (_id integer PRIMARY KEY UNIQUE, date text, time text, user text, billno text, " +
                    "billdetails text, sales integer, discountamount text, paymentmethod text, billtype text, itemname text, quan text, tableid text, individualprice text" +
                    ", date1 text, datetimee text);");
            db1.execSQL("CREATE TABLE if not exists userdata (_id integer PRIMARY KEY UNIQUE, username text, total integer);");
            db1.execSQL("CREATE TABLE if not exists itemdata (_id integer PRIMARY KEY UNIQUE, itemname text, total integer);");

            db1.execSQL("CREATE TABLE if not exists All_Sales_Cancelled (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, quantity text, price text, total text," +
                    "type text, parent text, parentid text, mod_assigned text, tax text, taxname text, bill_no text, time text, date text, user text, billtype text," +
                    " paymentmethod text, billamount_disapply text, billamount_disnotapply text, _idd text, reason text, " +
                    "billamount_cancelled text, date1 text, billamount_cancelled_user text, disc_type text, disc_value text, newtotal text, disc_thereornot text," +
                    " disc_indiv_total text);");

            db1.execSQL("CREATE TABLE if not exists Cancelwiseorderlistitems (_id integer PRIMARY KEY UNIQUE, date text, time text, user text, billno text, sale text, " +
                    "refund text, reason text );");

            db1.execSQL("CREATE TABLE if not exists usercancelleddata (_id integer PRIMARY KEY UNIQUE, username text, total integer);");
            db1.execSQL("CREATE TABLE if not exists Customerdetails (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, name text, phoneno text, emailid text, address text, " +
                    "rupees text, billnumber text);");
            db1.execSQL("CREATE TABLE if not exists Tablepayment (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, tablename text, tableid text, price text, print text);");
            db1.execSQL("CREATE TABLE if not exists Billnumber (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, billnumber text, total text, user text, date text," +
                    " paymentmethod text, billtype text, subtotal text, taxtotal text, roundoff text, globaltaxtotal text);");
            db1.execSQL("CREATE TABLE if not exists Discountdetails (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, date text, time text, billno text, Discountcodeno text, " +
                    "Discount_percent text, Billamount_rupess text, Discount_rupees text, date1 text, original_amount text);");
            db1.execSQL("CREATE TABLE if not exists Cardnumber (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, cardnumber text);");
            db1.execSQL("CREATE TABLE if not exists Splitdata (_id integer PRIMARY KEY UNIQUE, billnum text, total text, splittype text, split1 text, split2 text, split3 text);");
            db1.execSQL("CREATE TABLE if not exists Cust_feedback (_id integer PRIMARY KEY UNIQUE, cust_name text, date text, time text, ambience_rating text, pro_qual_rating text," +
                    " service_rating text, overall_exp_rating text, comments text, percentage text, cust_phoneno text);");
            db1.execSQL("CREATE TABLE if not exists Clicked_cust_name (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, name text);");
            db1.execSQL("CREATE TABLE if not exists Customerdetails_temporary (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, commission_charges text, commission_charges_status text, " +
                    "commission_charges_type text, phoneno text, name text);");
            db1.execSQL("CREATE TABLE if not exists Cusotmer_activity_temp (_id integer PRIMARY KEY UNIQUE, name text, phone_no text, " +
                    "email text, addr text, total_amount text, balance text, discount_value, text, discount_type text, approval_rate text);");
            db1.execSQL("CREATE TABLE if not exists Cusotmer_activity_temp_top3 (_id integer PRIMARY KEY UNIQUE, name text, phone_no text, " +
                    "email text, addr text, total_amount integer, balance text, discount_value, text, discount_type text, approval_rate text);");

            for (int i=1;i<=100;i++ ){
                db1.execSQL("CREATE TABLE if not exists Table" + i + " (_id integer PRIMARY KEY AUTOINCREMENT,quantity text, itemname text, price text, total text, type text," +
                        " parent text, parentid text, modassigned text, tax text, taxname text, disc_type text, disc_value text, newtotal text, disc_thereornot text," +
                        " disc_indiv_total text);");

                db1.execSQL("CREATE TABLE if not exists Table" + i + "payment (_id integer PRIMARY KEY AUTOINCREMENT, tableid text, price text, type text, paymentmethod text, " +
                        " discount text, discounttype text, discountcodenum text, cust_name text, cust_phone_no text, cust_emailid text, cust_address text, due_amount text," +
                        " cardnumber text, amounttendered text, dialog_round text, hometotal text, disc_type text, disc_value text, newtotal text, disc_thereornot text," +
                        " disc_indiv_total text);");

                db1.execSQL("CREATE TABLE if not exists Table" + i + "management (_id integer PRIMARY KEY AUTOINCREMENT, itemname text, qty text, tagg integer, date text, " +
                        " time text, par_id text, itemtype text);");
            }

            db1.execSQL("CREATE TABLE if not exists Top_Reason (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, reason text, value integer);");
            db1.execSQL("CREATE TABLE if not exists Top_Category (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, category text, value integer);");
            db1.execSQL("CREATE TABLE if not exists Itemwiseorderlistcategory (_id integer PRIMARY KEY UNIQUE, itemno text, categoryname text, sales integer, salespercentage integer," +
                    "itemtotalquan text);");

            db1.execSQL("CREATE TABLE if not exists BillCount (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, value text);");

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

            Cursor co4 = db.rawQuery("SELECT * FROM Modifiers", null);
            int cou4 = co4.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou4).equals("7")){
                db.execSQL("ALTER TABLE Modifiers ADD COLUMN mod_image_text text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co4.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co4_1 = db.rawQuery("SELECT * FROM Hotel", null);
            int cou4_1 = co4_1.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou4_1).equals("5")){
                db.execSQL("ALTER TABLE Hotel ADD COLUMN value int");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co4_1.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co1 = db.rawQuery("SELECT * FROM Items", null);
            int cou1 = co1.getColumnCount();
            //Toast.makeText(getActivity(), "2 "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou1).equals("13") || cou1 == 13){
                db.execSQL("ALTER TABLE Items ADD COLUMN disc_type text DEFAULT 0");
                db.execSQL("ALTER TABLE Items ADD COLUMN disc_value text DEFAULT 0");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co1.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co3 = db.rawQuery("SELECT * FROM Items", null);
            int cou3 = co3.getColumnCount();
            //Toast.makeText(getActivity(), "2 "+String.valueOf(cou3), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou3).equals("15")){
                db.execSQL("ALTER TABLE Items ADD COLUMN image_text text");
//                Toast.makeText(getActivity(), " image_text created 2", Toast.LENGTH_SHORT).show();
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co3.close();

            Cursor threete = db.rawQuery("SELECT * FROM DeleteDB_time ", null);
            if (threete.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("time", "11:30 PM");
                db.insert("DeleteDB_time", null, contentValues);
            }
            threete.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor threet = db.rawQuery("SELECT * FROM DeleteDBon_off ", null);
            if (threet.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("status", "off");
                db.insert("DeleteDBon_off", null, contentValues);
            }
            threet.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor threetf = db.rawQuery("SELECT * FROM Auto_generate_barcode ", null);
            if (threetf.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("generate", "off");
                db.insert("Auto_generate_barcode", null, contentValues);
            }
            threetf.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor twleve = db1.rawQuery("SELECT * FROM Clicked_cust_name", null);
            if (twleve.moveToFirst()){

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", "");
                db1.insert("Clicked_cust_name", null, contentValues);
            }
            twleve.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor threqe = db.rawQuery("SELECT * FROM KOT_print ", null);
            if (threqe.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("kot_print_status", "Yes");
                db.insert("KOT_print", null, contentValues);
            }
            threqe.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor ttthreqe = db.rawQuery("SELECT * FROM Auto_Connect ", null);
            if (ttthreqe.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("auto_connect_status", "No");
                db.insert("Auto_Connect", null, contentValues);
            }
            ttthreqe.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor tathreqee = db.rawQuery("SELECT * FROM Sync_time ", null);
            if (tathreqee.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("last_time", "05 Apr 18, 05:22 PM");
                db.insert("Sync_time", null, contentValues);
            }
            tathreqee.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor tathreqeee = db.rawQuery("SELECT * FROM Round_off ", null);
            if (tathreqeee.moveToFirst()) {

            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("round_off_status", "No");
                db.insert("Round_off", null, contentValues);
            }
            tathreqeee.close();

            Cursor twelvw = db.rawQuery("SELECT * FROM Default_credit", null);
            if (twelvw.moveToFirst()){

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("status", "off");
                db.insert("Default_credit", null, contentValues);
            }
            twelvw.close();

            Cursor thirteen = db.rawQuery("SELECT * FROM Working_hours", null);
            if (thirteen.moveToFirst()){

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("opening", "Today");
                contentValues.put("opening_time", "12:01 AM");
                contentValues.put("closing", "Today");
                contentValues.put("closing_time", "11:59 PM");
                contentValues.put("opening_time_system", "00:00");
                contentValues.put("closing_time_system", "23:59");
                db.insert("Working_hours", null, contentValues);
            }
            thirteen.close();

            Cursor fourteen_1 = db.rawQuery("SELECT * FROM Printer_text_size", null);
            if (fourteen_1.moveToFirst()){

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("type", "Standard");
                db.insert("Printer_text_size", null, contentValues);
            }
            fourteen_1.close();

            Cursor fourteeen_1 = db.rawQuery("SELECT * FROM Change_time_format", null);
            if (fourteeen_1.moveToFirst()){

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("status", "not changed");
                db.insert("Change_time_format", null, contentValues);
            }
            fourteeen_1.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor coo5 = db.rawQuery("SELECT * FROM Items", null);
            int couu5 = coo5.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(couu5).equals("16")){
                db.execSQL("ALTER TABLE Items ADD COLUMN barcode_value text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            coo5.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor coo6 = db.rawQuery("SELECT * FROM Items", null);
            int couu6 = coo6.getColumnCount();
            Log.i(TAG, "Added"+couu6);
            Log.i(TAG, "Added"+couu6);
            Log.i(TAG, "Added"+couu6);
            Log.i(TAG, "Added"+couu6);
            if (String.valueOf(couu6).equals("17")){
                db.execSQL("ALTER TABLE Items ADD COLUMN checked text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                Log.i(TAG, "Added");
                Log.i(TAG, "Added");
                Log.i(TAG, "Added");
                Log.i(TAG, "Added");
            }
            coo6.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor oco7 = db.rawQuery("SELECT * FROM Items", null);
            int ocou7 = oco7.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(ocou7).equals("18")){
                db.execSQL("ALTER TABLE Items ADD COLUMN print_value text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            oco7.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co87 = db.rawQuery("SELECT * FROM User_privilege", null);
            int cou87 = co87.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou87).equals("8")){
                db.execSQL("ALTER TABLE User_privilege ADD COLUMN ingredients text DEFAULT no");
                db.execSQL("ALTER TABLE User_privilege ADD COLUMN subscriptions text DEFAULT no");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co87.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co88 = db.rawQuery("SELECT * FROM Items", null);
            int cou88 = co88.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou88).equals("19")){
                db.execSQL("ALTER TABLE Items ADD COLUMN quantity_sold INTEGER DEFAULT 0");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co88.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co881 = db.rawQuery("SELECT * FROM Items", null);
            int cou881 = co881.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou881).equals("20")){
                db.execSQL("ALTER TABLE Items ADD COLUMN minimum_qty text DEFAULT 0");
                db.execSQL("ALTER TABLE Items ADD COLUMN minimum_qty_copy text DEFAULT 0");
                db.execSQL("ALTER TABLE Items ADD COLUMN add_qty text DEFAULT 0");
                db.execSQL("ALTER TABLE Items ADD COLUMN status_low text");
                db.execSQL("ALTER TABLE Items ADD COLUMN status_qty_updated text");
                db.execSQL("ALTER TABLE Items ADD COLUMN individual_price text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co881.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co882 = db.rawQuery("SELECT * FROM Items", null);
            int cou882 = co882.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou882).equals("26")){
                db.execSQL("ALTER TABLE Items ADD COLUMN unit_type text DEFAULT Unit");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co883 = db.rawQuery("SELECT * FROM Items", null);
            int cou883 = co883.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou883).equals("27")) {
                db.execSQL("ALTER TABLE Items ADD COLUMN tax_value text");
                db.execSQL("ALTER TABLE Items ADD COLUMN itemtax2 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN tax_value2 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN itemtax3 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN tax_value3 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN itemtax4 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN tax_value4 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN itemtax5 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN tax_value5 text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co884 = db.rawQuery("SELECT * FROM Items", null);
            int cou884 = co884.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou884).equals("36")) {
                db.execSQL("ALTER TABLE Items ADD COLUMN status_temp text");
                db.execSQL("ALTER TABLE Items ADD COLUMN status_perm text");
            }

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co885 = db.rawQuery("SELECT * FROM Items", null);
            int cou885 = co885.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou885).equals("38")) {
                db.execSQL("ALTER TABLE Items ADD COLUMN variant1 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant_price1 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant2 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant_price2 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant3 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant_price3 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant4 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant_price4 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant5 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant_price5 text");
            }

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co886 = db.rawQuery("SELECT * FROM Items", null);
            int cou886 = co886.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou886).equals("48")) {
                db.execSQL("ALTER TABLE Items ADD COLUMN dept_name text");
            }

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co887 = db.rawQuery("SELECT * FROM Items", null);
            int cou887 = co887.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou887).equals("49")) {
                db.execSQL("ALTER TABLE Items ADD COLUMN add_qty_st text");
                db.execSQL("ALTER TABLE Items ADD COLUMN status_qty_updated_st text");
            }
            co887.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co89 = db.rawQuery("SELECT * FROM Taxes", null);
            int cou89 = co89.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou89).equals("4")){
                db.execSQL("ALTER TABLE Taxes ADD COLUMN checked text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co89.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co90 = db.rawQuery("SELECT * FROM Taxes", null);
            int cou90 = co90.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou90).equals("5")){
                db.execSQL("ALTER TABLE Taxes ADD COLUMN tax1 text DEFAULT dine_in");
                db.execSQL("ALTER TABLE Taxes ADD COLUMN tax2 text DEFAULT takeaway");
                db.execSQL("ALTER TABLE Taxes ADD COLUMN tax3 text DEFAULT homedelivery");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co90.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co901 = db.rawQuery("SELECT * FROM Taxes", null);
            int cou901 = co901.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou901).equals("8")){
                db.execSQL("ALTER TABLE Taxes ADD COLUMN hsn_code text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co901.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co91 = db.rawQuery("SELECT * FROM IPConn", null);
            int cou91 = co91.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou91).equals("4")){
                db.execSQL("ALTER TABLE IPConn ADD COLUMN printer_name text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co91.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co92 = db.rawQuery("SELECT * FROM IPConn_Counter", null);
            int cou92 = co92.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou92).equals("4")){
                db.execSQL("ALTER TABLE IPConn_Counter ADD COLUMN printer_name text");
//                Toast.makeText(MainActivity.this, "altered", Toast.LENGTH_SHORT).show();
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co92.close();

            Cursor fgh = db.rawQuery("SELECT * FROM IPConn ", null);
            if (fgh.moveToFirst()) {
                String id = fgh.getString(0);
                ContentValues contentValues = new ContentValues();
                contentValues.put("printer_name", "TM Printer");
                String wherecu1 = "_id = '" + id + "'";
                db.update("IPConn", contentValues, wherecu1, new String[]{});

            }
            fgh.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor sixx = db.rawQuery("SELECT * FROM IPConn_Counter ", null);
            if (sixx.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("ipname", "192.168.1.87");
                contentValues.put("port", "9100");
                contentValues.put("status", "");
                db.insert("IPConn_Counter", null, contentValues);
            }
            sixx.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor fghh = db.rawQuery("SELECT * FROM IPConn_Counter", null);
            if (fghh.moveToFirst()) {
                String id = fghh.getString(0);
                ContentValues contentValues = new ContentValues();
                contentValues.put("printer_name", "TM Printer");
                String wherecu1 = "_id = '" + id + "'";
                db.update("IPConn_Counter", contentValues, wherecu1, new String[]{});
//                Toast.makeText(MainActivity.this, "updated", Toast.LENGTH_SHORT).show();
            }else {
//                Toast.makeText(MainActivity.this, "not updated", Toast.LENGTH_SHORT).show();
            }
            fghh.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor sixxx = db.rawQuery("SELECT * FROM Country_Selection ", null);
            if (sixxx.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("country", "India");
                db.insert("Country_Selection", null, contentValues);
            }
            sixxx.close();

            Cursor cursorrr = db.rawQuery("SELECT * FROM PaytmMerchantReg", null);
            if (cursorrr.moveToFirst()) {

            } else {
                ContentValues contentValuess = new ContentValues();
                contentValuess.put("Account", "Not_Registered");
                contentValuess.put("MerchantName", "");
                contentValuess.put("guid", "");
                contentValuess.put("MID", "");
                contentValuess.put("Merchant_key", "");
                contentValuess.put("PosID", "");
                db.insert("PaytmMerchantReg", null, contentValuess);
            }

            Cursor cur = db.rawQuery("SELECT * FROM MobikwikMerchantReg", null);
            if (cur.moveToFirst()) {

            } else {
                ContentValues contentValuees = new ContentValues();
                contentValuees.put("Account", "Not_Registered");
                contentValuees.put("Merchant_name", "");
                contentValuees.put("Mid_otp", "");
                contentValuees.put("Secretkey_otp", "");
                contentValuees.put("Mid_debit", "");
                contentValuees.put("Secretkey_debit", "");
                db.insert("MobikwikMerchantReg", null, contentValuees);
            }

            Cursor c = db.rawQuery("SELECT * FROM CardSwiperActivation", null);
            if (c.moveToFirst()) {
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("CardSwiperName", "PaySwiff");
                contentValues.put("merchantKey", "");
                contentValues.put("partnerkey", "");
                contentValues.put("Config_status", "Not Activated");
                db.insert("CardSwiperActivation", null, contentValues);

                ContentValues contentValues1 = new ContentValues();
                contentValues1.put("CardSwiperName", "mSwipe");
                contentValues1.put("merchantKey", "");
                contentValues1.put("partnerkey", "");
                contentValues1.put("Config_status", "Not Activated");
                db.insert("CardSwiperActivation", null, contentValues1);
            }


            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co2 = db1.rawQuery("SELECT * FROM Cardnumber", null);
            int cou2 = co2.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou2).equals("2")){
                db1.execSQL("ALTER TABLE Cardnumber ADD COLUMN billnumber text DEFAULT 0");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co2.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co = db1.rawQuery("SELECT * FROM Generalorderlistascdesc1", null);
            int cou = co.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou).equals("16")){
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN cardnumber text DEFAULT 0");
            }
            co.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co5 = db1.rawQuery("SELECT * FROM All_Sales", null);
            int cou5 = co5.getColumnCount();
            if (String.valueOf(cou5).equals("27")){
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN disc_type text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN disc_value text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN newtotal text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN disc_thereornot text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN disc_indiv_total text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN new_modified_total text DEFAULT 0");
            }
            co5.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co6 = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
            int cou6 = co6.getColumnCount();
            if (String.valueOf(cou6).equals("24")){
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_type text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_value text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN newtotal text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_thereornot text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_indiv_total text DEFAULT 0");
            }
            co6.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co7 = db1.rawQuery("SELECT * FROM Table1", null);
            int cou7 = co7.getColumnCount();
            if (String.valueOf(cou7).equals("11")){
                for (int i=1;i<=100;i++ ){
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN disc_type text DEFAULT 0");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN disc_value text DEFAULT 0");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN newtotal text DEFAULT 0");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN disc_thereornot text DEFAULT 0");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN disc_indiv_total text DEFAULT 0");
                }
            }
            co7.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo7 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou7 = coo7.getColumnCount();
            if (String.valueOf(coou7).equals("16")){
                for (int i=1;i<=100;i++ ){
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN status text");
                }
            }
            coo7.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo8 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou8 = coo8.getColumnCount();
            if (String.valueOf(coou8).equals("17")){
                for (int i=1;i<=100;i++ ){
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN tagg integer");
                }
            }
            coo8.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo9 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou9 = coo9.getColumnCount();
            if (String.valueOf(coou9).equals("18")){
                for (int i=1;i<=100;i++ ){
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN date text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN time text");
                }
            }
            coo9.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo10 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou10 = coo10.getColumnCount();
            if (String.valueOf(coou10).equals("20")){
                for (int i=1;i<=100;i++ ){
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN updated_quantity text");
                }
            }
            coo10.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo11 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou11 = coo11.getColumnCount();
            if (String.valueOf(coou11).equals("21")) {
                for (int i = 1; i <= 100; i++) {
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN taxname2 text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN tax2 text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN taxname3 text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN tax3 text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN taxname4 text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN tax4 text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN taxname5 text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN tax5 text");
                }
            }
            coo11.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo12 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou12 = coo12.getColumnCount();
            if (String.valueOf(coou12).equals("29")) {
                for (int i = 1; i <= 100; i++) {
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN category text");
                }
            }
            coo12.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo13 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou13 = coo13.getColumnCount();
            if (String.valueOf(coou13).equals("30")) {
                for (int i = 1; i <= 100; i++) {
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN add_note text");
                }
            }
            coo13.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo14 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou14 = coo14.getColumnCount();
            if (String.valueOf(coou14).equals("31")) {
                for (int i = 1; i <= 100; i++) {
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN dept_name text");
                }
            }
            coo14.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo15 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou15 = coo15.getColumnCount();
            if (String.valueOf(coou15).equals("32")) {
                for (int i = 1; i <= 100; i++) {
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN discount_value text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN discount_code text");
                }
            }
            coo15.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo16 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou16 = coo16.getColumnCount();
            if (String.valueOf(coou16).equals("34")) {
                for (int i = 1; i <= 100; i++) {
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN discount_type text");
                }
            }
            coo16.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo17 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou17 = coo17.getColumnCount();
            if (String.valueOf(coou17).equals("35")) {
                for (int i = 1; i <= 100; i++) {
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN barcode_get text");
                }
            }
            coo17.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co8 = db1.rawQuery("SELECT * FROM Billnumber", null);
            int cou8 = co8.getColumnCount();
            if (String.valueOf(cou8).equals("11")){
                db1.execSQL("ALTER TABLE Billnumber ADD COLUMN billcount text");
            }
            co8.close();

            Cursor co9 = db1.rawQuery("SELECT * FROM Generalorderlistascdesc1", null);
            int cou9 = co9.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou9).equals("17")){
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN individualtotal text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN billcount text");
            }
            co9.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo91 = db1.rawQuery("SELECT * FROM Generalorderlistascdesc1", null);
            int coou91 = coo91.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
            if (String.valueOf(coou91).equals("19")){
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN hsn_code text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_per text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN globaltax_rs text");
            }
            coo91.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo911 = db1.rawQuery("SELECT * FROM Generalorderlistascdesc1", null);
            int coou911 = coo911.getColumnCount();
            if (String.valueOf(coou911).equals("24")){
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name2 text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs2 text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name3 text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs3 text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name4 text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs4 text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name5 text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs5 text");
            }
            coo911.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co10 = db1.rawQuery("SELECT * FROM Discountdetails", null);
            int cou10 = co10.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou10).equals("10")){
                db1.execSQL("ALTER TABLE Discountdetails ADD COLUMN billcount text");
            }
            co10.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co11 = db1.rawQuery("SELECT * FROM Cancelwiseorderlistitems", null);
            int cou11 = co11.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou11).equals("8")){
                db1.execSQL("ALTER TABLE Cancelwiseorderlistitems ADD COLUMN billcount text");
            }
            co11.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co12 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou12 = co12.getColumnCount();
            if (String.valueOf(cou12).equals("7")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN date1 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN time1 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN date text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN total text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN deposit text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cashout text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN credit text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN charges text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN authentication_pin text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN otp text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN dob text");
            }
            co12.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co13 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou13 = co13.getColumnCount();
            if (String.valueOf(cou13).equals("18")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN refunds text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN total_amount text");
            }
            co13.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co14 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou14 = co14.getColumnCount();
            if (String.valueOf(cou14).equals("20")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cashout_type text");
            }
            co14.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co15 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou15 = co15.getColumnCount();
            if (String.valueOf(cou15).equals("21")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN credit_default text");
            }
            co15.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co16 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou16 = co16.getColumnCount();
            if (String.valueOf(cou16).equals("22")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN commission_charges text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN commission_charges_type text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN commission_charges_status text");
            }
            co16.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co17 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou17 = co17.getColumnCount();
            if (String.valueOf(cou17).equals("25")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN authentication_pin_status text");
            }
            co17.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co18 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou18 = co18.getColumnCount();
            if (String.valueOf(cou18).equals("26")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN dob_alaram text");
            }
            co18.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co19 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou19 = co19.getColumnCount();
            if (String.valueOf(cou19).equals("27")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN default_discount_status text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN default_discount_amount text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN default_discount_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN default_discount_type text");
            }
            co19.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co20 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou20 = co20.getColumnCount();
            if (String.valueOf(cou20).equals("31")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN notes text");
            }
            co20.close();


            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co21 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou21 = co21.getColumnCount();
            if (String.valueOf(cou21).equals("32")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cust_account_no text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cust_ifsc_code text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cust_account_holder_name text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cust_bank_name text");
            }
            co21.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co22 = db1.rawQuery("SELECT * FROM All_Sales", null);
            int cou22 = co22.getColumnCount();
            if (String.valueOf(cou22).equals("33")){
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN datetimee_new text");
            }
            co22.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co221 = db1.rawQuery("SELECT * FROM All_Sales", null);
            int cou221 = co221.getColumnCount();
            if (String.valueOf(cou221).equals("34")){
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN hsn_code text");
            }
            co221.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co222 = db1.rawQuery("SELECT * FROM All_Sales", null);
            int cou222 = co222.getColumnCount();
            if (String.valueOf(cou222).equals("35")){
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN taxname2 text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN tax2 text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN taxname3 text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN tax3 text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN taxname4 text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN tax4 text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN taxname5 text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN tax5 text");
            }
            co222.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co223 = db1.rawQuery("SELECT * FROM All_Sales", null);
            int cou223 = co223.getColumnCount();
            if (String.valueOf(cou223).equals("43")){
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN category text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN counterperson_username text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN counterperson_name text");
            }
            co223.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co224 = db1.rawQuery("SELECT * FROM All_Sales", null);
            int cou224 = co224.getColumnCount();
            if (String.valueOf(cou224).equals("46")) {
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN credit text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN Phone_num text");
            }
            co224.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co225 = db1.rawQuery("SELECT * FROM All_Sales", null);
            int cou225 = co225.getColumnCount();
            if (String.valueOf(cou225).equals("48")) {
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN barcode_get text");
            }
            co225.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co25 = db1.rawQuery("SELECT * FROM Billnumber", null);
            int cou25 = co25.getColumnCount();
            if (String.valueOf(cou25).equals("12")){
                db1.execSQL("ALTER TABLE Billnumber ADD COLUMN datetimee_new text");
            }
            co25.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co23 = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
            int cou23 = co23.getColumnCount();
            if (String.valueOf(cou23).equals("29")){
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN datetimee_new text");
            }
            co23.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co231 = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
            int cou231 = co231.getColumnCount();
            if (String.valueOf(cou231).equals("30")) {
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN taxname2 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN tax2 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN taxname3 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN tax3 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN taxname4 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN tax4 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN taxname5 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN tax5 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN hsn_code text");
            }
            co231.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co24 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou24 = co24.getColumnCount();
            if (String.valueOf(cou24).equals("36")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN datetimee_new text");
            }
            co24.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co26 = db1.rawQuery("SELECT * FROM Discountdetails", null);
            int cou26 = co26.getColumnCount();
            if (String.valueOf(cou26).equals("11")){
                db1.execSQL("ALTER TABLE Discountdetails ADD COLUMN datetimee_new text");
            }
            co26.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co27 = db1.rawQuery("SELECT * FROM Cardnumber", null);
            int cou27 = co27.getColumnCount();
            if (String.valueOf(cou27).equals("3")){
                db1.execSQL("ALTER TABLE Cardnumber ADD COLUMN datetimee_new text");
            }
            co27.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co241 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou241 = co241.getColumnCount();
            if (String.valueOf(cou241).equals("37")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN user_id text");
            }
            co241.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co242 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou242 = co242.getColumnCount();
            if (String.valueOf(cou242).equals("38")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax1 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax2 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax3 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax4 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax5 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax6 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax7 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax8 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax9 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax10 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax11 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax12 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax13 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax14 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax15 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax_selection text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax1_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax2_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax3_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax4_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax5_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax6_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax7_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax8_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax9_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax10_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax11_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax12_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax13_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax14_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax15_value text");


                Cursor cf = db1.rawQuery("SELECT * FROM Customerdetails", null);
                if (cf.moveToFirst()) {
                    do {
                        String id = cf.getString(0);
                        int i = 1;
                        Cursor c1_11 = db.rawQuery("SELECT * FROM Taxes WHERE taxtype = 'Globaltax'", null);
                        if (c1_11.moveToFirst()) {
                            do {
                                String tn = c1_11.getString(1);
                                String tn_value = c1_11.getString(2);
                                ContentValues contentValues1 = new ContentValues();
                                contentValues1.put("tax" + i, tn);
                                contentValues1.put("tax" + i + "_value", tn_value);
                                contentValues1.put("tax_selection", "All");
                                String wherecu1 = "_id = '" + id + "'";
                                db1.update("Customerdetails", contentValues1, wherecu1, new String[]{});
                                i++;
                            } while (c1_11.moveToNext());
                        }
                        c1_11.close();
                    }while (cf.moveToNext());
                }
                cf.close();

            }
            co242.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co251 = db1.rawQuery("SELECT * FROM Billnumber", null);
            int cou251 = co251.getColumnCount();
            if (String.valueOf(cou251).equals("13")){
                db1.execSQL("ALTER TABLE Billnumber ADD COLUMN comments_sales text");
            }
            co251.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co28 = db1.rawQuery("SELECT * FROM Cusotmer_activity_temp", null);
            int cou28 = co28.getColumnCount();
            if (String.valueOf(cou28).equals("11")){
                db1.execSQL("ALTER TABLE Cusotmer_activity_temp ADD COLUMN cust_id text DEFAULT off");
            }
            co28.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co29 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou29 = co29.getColumnCount();
            if (String.valueOf(cou29).equals("69")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN proceedings text DEFAULT off");
            }
            co29.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co290 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou290 = co290.getColumnCount();
            if (String.valueOf(cou290).equals("70")) {
//                    SaleType text, Cheque_num text,CreditAmount text, SaleTime text, SaleDate text
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN SaleType text DEFAULT off");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN Cheque_num text DEFAULT off");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN CreditAmount text DEFAULT off");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN SaleTime text DEFAULT off");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN SaleDate text DEFAULT off");
            }
            co290.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co291 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou291 = co291.getColumnCount();
            if (String.valueOf(cou291).equals("75")) {
//                    SaleType text, Cheque_num text,CreditAmount text, SaleTime text, SaleDate text
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN Transaction_ID text DEFAULT off");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN Card_Type text DEFAULT off");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN Card_Num text DEFAULT off");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN RRN text DEFAULT off");
            }
            co291.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co292 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou292 = co292.getColumnCount();
            if (String.valueOf(cou292).equals("79")) {
//                    SaleType text, Cheque_num text,CreditAmount text, SaleTime text, SaleDate text
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN pincode text");
            }
            co292.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co30 = db1.rawQuery("SELECT * FROM Itemwiseorderlistitems", null);
            int cou30 = co30.getColumnCount();
            if (String.valueOf(cou30).equals("6")){
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN category");
            }
            co30.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co301 = db1.rawQuery("SELECT * FROM Itemwiseorderlistitems", null);
            int cou301 = co301.getColumnCount();
            if (String.valueOf(cou301).equals("7")){
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN i_buying_price");
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN mi_buying_price");
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN i_cost_value");
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN i_cost_percent");
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN mi_cost_value");
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN mi_cost_percent");
            }
            co301.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co302 = db1.rawQuery("SELECT * FROM Itemwiseorderlistitems", null);
            int cou302 = co302.getColumnCount();
            if (String.valueOf(cou302).equals("13")){
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN barcode_value");
            }
            co302.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co31 = db1.rawQuery("SELECT * FROM Itemwiseorderlistmodifiers", null);
            int cou31 = co31.getColumnCount();
            if (String.valueOf(cou31).equals("6")){
                db1.execSQL("ALTER TABLE Itemwiseorderlistmodifiers ADD COLUMN category");
            }
            co31.close();



            Cursor items1 = db.rawQuery("SELECT * FROM Items WHERE image is null", null);
            if (items1.moveToFirst()){
                do {
                    String id = items1.getString(0);
                    String name = items1.getString(1);
                    //Toast.makeText(MainActivity.this, "image not hter "+name, Toast.LENGTH_SHORT).show();
                    String str1 = name.substring(0, 2);
                    String str2 = str1.toUpperCase();
                    ContentValues contentValues5 = new ContentValues();
                    contentValues5.put("image", "");
                    contentValues5.put("image_text", str2);
                    String where = "_id = '" + id + "'";
                    db.update("Items", contentValues5, where, new String[]{});
                    String where1_v1 = "docid = '" + id + "'";
                    db.update("Items_Virtual", contentValues5, where1_v1, new String[]{});
                }while (items1.moveToNext());
            }
            items1.close();

            Cursor items2 = db.rawQuery("SELECT * FROM Items WHERE image = '' ", null);
            if (items2.moveToFirst()){
                do {
                    String id = items2.getString(0);
                    String name = items2.getString(1);
                    //Toast.makeText(MainActivity.this, "image not hter "+name, Toast.LENGTH_SHORT).show();
                    String str1 = name.substring(0, 2);
                    String str2 = str1.toUpperCase();
                    ContentValues contentValues5 = new ContentValues();
                    contentValues5.put("image", "");
                    contentValues5.put("image_text", str2);
                    String where = "_id = '" + id + "'";
                    db.update("Items", contentValues5, where, new String[]{});
                    String where1_v1 = "docid = '" + id + "'";
                    db.update("Items_Virtual", contentValues5, where1_v1, new String[]{});
                }while (items2.moveToNext());
            }
            items2.close();


            Cursor items3 = db.rawQuery("SELECT * FROM Items", null);
            if (items3.moveToFirst()){
                do {
                    String id = items3.getString(0);
                    String name = items3.getString(1);

                    if (name.contains("'")) {
                        replacecolumnvalue = name.replace("'", " ");

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("itemname", replacecolumnvalue);

                        String where1 = "_id = '" + id + "' ";
                        db.update("Items", contentValues, where1, new String[]{});
                        String where1_v1 = "docid = '" + id + "'";
                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
                    }

                }while (items3.moveToNext());
            }
            items3.close();


            Cursor items4 = db.rawQuery("SELECT * FROM Modifiers", null);
            if (items4.moveToFirst()){
                do {
                    String id = items4.getString(0);
                    String name = items4.getString(1);

                    if (name.contains("'")) {
                        replacemodifiervalue = name.replace("'", " ");

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("modifiername", replacemodifiervalue);

                        String where1 = "_id = '" + id + "' ";
                        db.update("Modifiers", contentValues, where1, new String[]{});
                    }

                }while (items4.moveToNext());
            }
            items4.close();


            Cursor items5 = db.rawQuery("SELECT * FROM Taxes", null);
            if (items5.moveToFirst()){
                do {
                    String id = items5.getString(0);
                    String name = items5.getString(1);

                    if (name.contains("'")) {
                        replacetaxvalue = name.replace("'", " ");

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("taxname", replacetaxvalue);

                        String where1 = "_id = '" + id + "' ";
                        db.update("Taxes", contentValues, where1, new String[]{});
                    }

                }while (items5.moveToNext());
            }
            items5.close();


            Cursor items6 = db.rawQuery("SELECT * FROM Hotel", null);
            if (items6.moveToFirst()){
                do {
                    String id = items6.getString(0);
                    String name = items6.getString(1);

                    if (name.contains("'")) {
                        replacecategoryvalue = name.replace("'", " ");

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("name", replacecategoryvalue);

                        String where1 = "_id = '" + id + "' ";
                        db.update("Hotel", contentValues, where1, new String[]{});
                    }

                }while (items6.moveToNext());
            }
            items6.close();


            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor fourteen = db1.rawQuery("SELECT * FROM All_Sales", null);
            if (fourteen.moveToFirst()){
                do {
                    String id = fourteen.getString(0);
                    String dt = fourteen.getString(26);
                    String datetimee_new = fourteen.getString(33);

                    TextView tv = new TextView(getActivity());
                    tv.setText(datetimee_new);

                    if (tv.getText().toString().equals("")) {

                        if (dt.contains(":")) {
                            dt = dt.replace(":", "");
                        }

                        dt = dt.substring(0, 12);
//                Toast.makeText(MainActivity.this, " "+dt, Toast.LENGTH_SHORT).show();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("datetimee_new", dt);
                        String where11 = "_id = '" + id + "' ";
                        db1.update("All_Sales", contentValues, where11, new String[]{});
                    }
                }while (fourteen.moveToNext());
            }
            fourteen.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor fifteen = db1.rawQuery("SELECT * FROM Billnumber", null);
            if (fifteen.moveToFirst()){
                do {
                    String id = fifteen.getString(0);
                    String billno = fifteen.getString(1);
                    String datetimee_new = fifteen.getString(12);

                    TextView tv = new TextView(getActivity());
                    tv.setText(datetimee_new);

                    if (tv.getText().toString().equals("")) {
                        Cursor fifteen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '"+billno+"'", null);
                        if (fifteen_1.moveToFirst()){
                            String a = fifteen_1.getString(33);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '"+id+"' ";
                            db1.update("Billnumber", contentValues, where11, new String[]{});
                        }else {
                            Cursor fifteen_11 = db1.rawQuery("SELECT * FROM All_Sales_Cancelled WHERE bill_no = '"+billno+"'", null);
                            if (fifteen_11.moveToFirst()){
                                String a = fifteen_11.getString(29);

                                ContentValues contentValues = new ContentValues();
                                contentValues.put("datetimee_new", a);
                                String where11 = "_id = '"+id+"' ";
                                db1.update("Billnumber", contentValues, where11, new String[]{});
                            }
                            fifteen_11.close();
                        }
                        fifteen_1.close();
                    }


                }while (fifteen.moveToNext());
            }
            fifteen.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor sixteen = db1.rawQuery("SELECT * FROM Customerdetails", null);
            if (sixteen.moveToFirst()){
                do {
                    String id = sixteen.getString(0);
                    String billno = sixteen.getString(6);
                    String datetimee_new = sixteen.getString(36);

                    TextView tv = new TextView(getActivity());
                    tv.setText(datetimee_new);

                    if (tv.getText().toString().equals("")) {
                        Cursor sixteen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                        if (sixteen_1.moveToFirst()) {
                            String a = sixteen_1.getString(33);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("Customerdetails", contentValues, where11, new String[]{});
                        }
                        sixteen_1.close();
                    }
                }while (sixteen.moveToNext());
            }
            sixteen.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor seventeen = db1.rawQuery("SELECT * FROM Discountdetails", null);
            if (seventeen.moveToFirst()){
                do {
                    String id = seventeen.getString(0);
                    String billno = seventeen.getString(3);
                    String datetimee_new = seventeen.getString(11);

                    TextView tv = new TextView(getActivity());
                    tv.setText(datetimee_new);

                    if (tv.getText().toString().equals("")) {
                        Cursor seventeen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                        if (seventeen_1.moveToFirst()) {
                            String a = seventeen_1.getString(33);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("Discountdetails", contentValues, where11, new String[]{});
                        }
                        seventeen_1.close();
                    }
                }while (seventeen.moveToNext());
            }
            seventeen.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor eightteen = db1.rawQuery("SELECT * FROM Cardnumber", null);
            if (eightteen.moveToFirst()){
                do {
                    String id = eightteen.getString(0);
                    String billno = eightteen.getString(2);
                    String datetimee_new = eightteen.getString(3);

                    TextView tv = new TextView(getActivity());
                    tv.setText(datetimee_new);

                    if (tv.getText().toString().equals("")) {
                        Cursor eightteen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                        if (eightteen_1.moveToFirst()) {
                            String a = eightteen_1.getString(33);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("Cardnumber", contentValues, where11, new String[]{});
                        }
                        eightteen_1.close();
                    }
                }while (eightteen.moveToNext());
            }
            eightteen.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor nineteen = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
            if (nineteen.moveToFirst()){
                do {
                    String id = nineteen.getString(0);
                    String billno = nineteen.getString(11);
                    String datetimee_new = nineteen.getString(29);

                    TextView tv = new TextView(getActivity());
                    tv.setText(datetimee_new);

                    if (tv.getText().toString().equals("")) {
                        Cursor nineteen_1 = db1.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billno + "'", null);
                        if (nineteen_1.moveToFirst()) {
                            String a = nineteen_1.getString(12);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("All_Sales_Cancelled", contentValues, where11, new String[]{});
                        }
                        nineteen_1.close();
                    }
                }while (nineteen.moveToNext());
            }
            nineteen.close();

            Cursor change_time = db.rawQuery("SELECT * FROM Change_time_format", null);
            if (change_time.moveToFirst()) {
                String zero = change_time.getString(0);
                one_one = change_time.getString(1);
            }
            change_time.close();

            if (one_one.equals("not changed")) {
                db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor fourteen_11 = db1.rawQuery("SELECT * FROM All_Sales", null);
                if (fourteen_11.moveToFirst()) {
                    do {
                        String id = fourteen_11.getString(0);
                        String dt = fourteen_11.getString(26);
                        String dt_new = fourteen_11.getString(33);

                        String dt1 = dt.substring(8, 10);

                        if (dt1.equals("24")){
                            dt = dt.replace(dt1, "00");

                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("datetimee", dt);
                            String where11 = "_id = '"+id+"' ";
                            db1.update("All_Sales", contentValues1, where11, new String[]{});
                        }

                        String dt2 = dt_new.substring(8, 10);

                        if (dt2.equals("24")){
                            dt_new = dt_new.replace(dt2, "00");

                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("datetimee_new", dt_new);
                            String where11 = "_id = '"+id+"' ";
                            db1.update("All_Sales", contentValues1, where11, new String[]{});
                        }



                    } while (fourteen_11.moveToNext());
                }
                fourteen_11.close();

                db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor fifteenn = db1.rawQuery("SELECT * FROM Billnumber", null);
                if (fifteenn.moveToFirst()) {
                    do {
                        String id = fifteenn.getString(0);
                        String billno = fifteenn.getString(1);
                        String dt_new = fifteenn.getString(12);
                        TextView tv1 = new TextView(getActivity());
                        tv1.setText(billno);

                        String dt2 = dt_new.substring(8, 10);
                        if (dt2.equals("24")){
                            dt_new = dt_new.replace(dt2, "00");
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", dt_new);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("Billnumber", contentValues, where11, new String[]{});
                        }

//                    Cursor fifteen_11 = null;
//                    try {
//                        Cursor fifteen_11 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + tv1.getText().toString() + "'", null);
//                        if (fifteen_11.moveToFirst()) {
//                            String a = fifteen_11.getString(33);
//
//
//                        }
//                    }finally {
//                        if(fifteen_11 != null)
//                            fifteen_11.close();
//                    }

                    } while (fifteenn.moveToNext());
                }
                fifteenn.close();

                db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor sixteenn = db1.rawQuery("SELECT * FROM Customerdetails", null);
                if (sixteenn.moveToFirst()) {
                    do {
                        String id = sixteenn.getString(0);
                        String billno = sixteenn.getString(6);

                        Cursor sixteen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                        if (sixteen_1.moveToFirst()) {
                            String a = sixteen_1.getString(33);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("Customerdetails", contentValues, where11, new String[]{});
                        }
                        sixteen_1.close();
                    } while (sixteenn.moveToNext());
                }
                sixteenn.close();

                db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor seventeenn = db1.rawQuery("SELECT * FROM Discountdetails", null);
                if (seventeenn.moveToFirst()) {
                    do {
                        String id = seventeenn.getString(0);
                        String billno = seventeenn.getString(3);

                        Cursor seventeen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                        if (seventeen_1.moveToFirst()) {
                            String a = seventeen_1.getString(33);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("Discountdetails", contentValues, where11, new String[]{});
                        }
                        seventeen_1.close();
                    } while (seventeenn.moveToNext());
                }
                seventeenn.close();

                db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor eightteenn = db1.rawQuery("SELECT * FROM Cardnumber", null);
                if (eightteenn.moveToFirst()) {
                    do {
                        String id = eightteenn.getString(0);
                        String billno = eightteenn.getString(2);

                        Cursor eightteen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                        if (eightteen_1.moveToFirst()) {
                            String a = eightteen_1.getString(33);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("Cardnumber", contentValues, where11, new String[]{});
                        }
                        eightteen_1.close();
                    } while (eightteenn.moveToNext());
                }
                eightteenn.close();

                db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor nineteenn = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
                if (nineteenn.moveToFirst()) {
                    do {
                        String id = nineteenn.getString(0);
                        String billno = nineteenn.getString(11);

                        Cursor nineteen_1 = db1.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billno + "'", null);
                        if (nineteen_1.moveToFirst()) {
                            String a = nineteen_1.getString(12);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("All_Sales_Cancelled", contentValues, where11, new String[]{});
                        }
                        nineteen_1.close();
                    } while (nineteenn.moveToNext());
                }
                nineteenn.close();
            }

            Cursor fifteen_1 = db.rawQuery("SELECT * FROM Change_time_format", null);
            if (fifteen_1.moveToFirst()) {
                String zero = fifteen_1.getString(0);
                ContentValues contentValues = new ContentValues();
                contentValues.put("status", "changed");

                String where11 = "_id = '"+zero+"' ";
                db.update("Change_time_format", contentValues, where11, new String[]{});
            }
            fifteen_1.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor usr_id_null = db1.rawQuery("SELECT * FROM Customerdetails WHERE user_id is null", null);
            if (usr_id_null.moveToFirst()){
                do {
                    String id = usr_id_null.getString(0);
                    ContentValues contentValues5 = new ContentValues();
                    contentValues5.put("user_id", "");
                    String where = "_id = '" + id + "'";
                    db1.update("Customerdetails", contentValues5, where, new String[]{});
                }while (usr_id_null.moveToNext());
            }
            usr_id_null.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor mail_schedule = db.rawQuery("SELECT * FROM Schedule_mail_on_off ", null);
            if (mail_schedule.moveToFirst()) {

            }else {

                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", "1");
                contentValues.put("status", "Off");
                db.insert("Schedule_mail_on_off", null, contentValues);

            }
            mail_schedule.close();


            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor printer_type = db.rawQuery("SELECT * FROM Printer_type", null);
            if (printer_type.moveToFirst()) {

            }else {

                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", "1");
                contentValues.put("printer_type", "Generic");
                db.insert("Printer_type", null, contentValues);

            }
            printer_type.close();


            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor user101 = db.rawQuery("SELECT * FROM Schedule_mail_time ", null);
            if (user101.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("time", "11:30 PM");
                db.insert("Schedule_mail_time", null, contentValues);
            }
            user101.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor user102 = db.rawQuery("SELECT * FROM BIllingmode ", null);
            if (user102.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", "1");
                contentValues.put("billingtype", "Fine dine");
                db.insert("BIllingmode", null, contentValues);
            }
            user102.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor billing_type = db.rawQuery("SELECT * FROM BIllingtype ", null);
            if (billing_type.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", "1");
                contentValues.put("billingtype_type", "Dine-in");
                db.insert("BIllingtype", null, contentValues);
            }
            billing_type.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor estimate_bill = db.rawQuery("SELECT * FROM Estimate_print ", null);
            if (estimate_bill.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", "1");
                contentValues.put("status", "Yes");
                db.insert("Estimate_print", null, contentValues);
            }
            estimate_bill.close();

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);

            dialog.setMessage(getString(R.string.setmessage2));
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


        @Override
        protected void onPostExecute(Integer file_url) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();


            Intent intent = new Intent(getActivity(), SyncHelperService.class);
            intent.putExtra("backup","appdata");
            getActivity().startService(intent);


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();


                    final Dialog dialog1 = new Dialog(getActivity(), R.style.notitle);
                    dialog1.setContentView(R.layout.restore_confirmation);
                    dialog1.show();

                    TextView successfilename = (TextView)dialog1.findViewById(R.id.restore_filename);
                    successfilename.setText("'" + backupname.getText().toString() + "'");

                    Button done = (Button)dialog1.findViewById(R.id.gohome);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                        }
                    });

                    Button gotohome = (Button)dialog1.findViewById(R.id.gotohome);
                    gotohome.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (account_selection.toString().equals("Dine")) {
                                Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                                if (cursor3.moveToFirst()) {
                                    String lite_pro = cursor3.getString(1);

                                    TextView tv = new TextView(getActivity());
                                    tv.setText(lite_pro);

                                    if (tv.getText().toString().equals("Lite")) {
                                        Intent intent = new Intent(getActivity(), BeveragesMenuFragment_Dine_l.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(getActivity(), BeveragesMenuFragment_Dine.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                }else {
                                    Intent intent = new Intent(getActivity(), BeveragesMenuFragment_Dine_l.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }else {
                                if (account_selection.toString().equals("Qsr")) {
                                    Intent intent = new Intent(getActivity(), BeveragesMenuFragment_Qsr.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(getActivity(), BeveragesMenuFragment_Retail.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }
//                            Intent intent = new Intent(getActivity(), BeveragesMenuFragment_Dine.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
                        }
                    });
                }
            }, 10000); //3000 L = 3 detik



        }

    }

    class DownloadMusicfromInternet2 extends AsyncTask<String, Void, Integer> {
        private ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);

        @Override
        protected Integer doInBackground(String... params) {

            try {
//                File sd = Environment.getExternalStorageDirectory();
                File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File data = Environment.getDataDirectory();

                if (sd.canWrite()) {
                    String currentDBPath = "//data//" + "com.intuition.ivepos"
                            + "//databases//" + "mydb_Salesdata";
                    String backupDBPath = "/IVEPOS_backup/" + backupname.getText().toString() + "/" + "mydb_Salesdata";

//                                                        Toast.makeText(getActivity(), "1 " + currentDBPath, Toast.LENGTH_LONG)
//                                                                .show();
//
//                                                        Toast.makeText(getActivity(), "11 " + backupDBPath, Toast.LENGTH_LONG)
//                                                                .show();

//                                            boolean dbexist = DATA_DIRECTORY_DATABASE;
//                                            if (dbexist) {
//
//                                            }
//                                            DATA_DIRECTORY_DATABASE


                    File file = new File("/data/data/com.intuition.ivepos/databases/mydb_Salesdata");
                    if(file.exists()){
//                                                            Toast.makeText(getActivity(), "exists", Toast.LENGTH_LONG)
//                                                                    .show();
                    }
                    if (DATA_DIRECTORY_DATABASE.exists()){
//                                                            Toast.makeText(getActivity(), "existsssss", Toast.LENGTH_LONG)
//                                                                    .show();
                    }else {
//                                                            Toast.makeText(getActivity(), "not exists", Toast.LENGTH_LONG)
//                                                                    .show();
                    }

                    File backupDB = new File(data, currentDBPath);
                    File currentDB = new File(sd, backupDBPath);

                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
//                                                        Toast.makeText(getActivity(),"111 "+ backupDB.toString(),
//                                                                Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {

                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG)
                        .show();

            }




            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);


            db.execSQL("CREATE TABLE if not exists Adminrights (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, username text, password text);");
            db.execSQL("CREATE TABLE if not exists BIllingmode (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, billingtype text);");
            db.execSQL("CREATE TABLE if not exists Barcodescannerconnectivity (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, barcodescannercontype text);");
            db.execSQL("CREATE TABLE if not exists CATEGORY (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, ALL_CATEGORY text);");
            db.execSQL("CREATE TABLE if not exists Cashdrawerconnectivity (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, cashdrawercontype text);");
            db.execSQL("CREATE TABLE if not exists Companydetailss (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, companyname text, doorno text," +
                    "substreet text, street text, city text, state text, country text, pincode INTEGER, phoneno INTEGER, taxone text, taxtwo text, footerone text, footertwo text," +
                    "address1 text, email text, website text, address2 text, address3 text );");
            db.execSQL("CREATE TABLE if not exists Hotel (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, direccion text, telefono text, email text);");
            db.execSQL("CREATE TABLE if not exists Items (_id integer PRIMARY KEY AUTOINCREMENT, itemname text, price NUMERIC, stockquan NUMERIC, category text, itemtax text," +
                    "image blob, weekdaysvalue text, weekendsvalue text, manualstockvalue text, automaticstockresetvalue text, clickcount text, favourites text," +
                    "disc_type text, disc_value text, image_text text, barcode_value text, checked text, print_value text);");
            db.execSQL("CREATE TABLE if not exists Items1 (_id integer PRIMARY KEY AUTOINCREMENT, itemname text, price NUMERIC, stockquan NUMERIC, category text, itemtax text," +
                    "image blob, weekdaysvalue TEXT, weekendsvalue TEXT, manualstockvalue TEXT, automaticstockresetvalue TEXT, clickcount TEXT, favourites TEXT);");
            db.execSQL("CREATE TABLE if not exists Itemsstockvalue (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, weekdaysvalue text, weekendsvalue text);");
            db.execSQL("CREATE TABLE if not exists LAdmin (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, username text, password text, name text);");
            db.execSQL("CREATE TABLE if not exists LOGIN (ID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, USERNAME text, PASSWORD textpublic, name text);");
            db.execSQL("CREATE TABLE if not exists Logo (_id INTEGER PRIMARY KEY UNIQUE, companylogo blob);");
            db.execSQL("CREATE TABLE if not exists Modifiers (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, modifiername text, modprice numeric, modstockquan numeric, " +
                    "modcategory text, moditemtax text, modimage BLOB, mod_image_text text);");
            db.execSQL("CREATE TABLE if not exists Printerconnectivity (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, printercontype text);");
            db.execSQL("CREATE TABLE if not exists Printerreceiptsize(_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, papersize text)");
            db.execSQL("CREATE TABLE if not exists Quickaccess (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text);");
            db.execSQL("CREATE TABLE if not exists Quickedit (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, quickedittype text);");
            db.execSQL("CREATE TABLE if not exists ResetFrequencyRestaurant (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, resetfrequencyrestaurant text);");
            db.execSQL("CREATE TABLE if not exists ResetFrequencyRetail (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, resetfrequencyretail text);");
            db.execSQL("CREATE TABLE if not exists Stockcontrol (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, stockcontroltype text);");
            db.execSQL("CREATE TABLE if not exists Itemsort (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, itemsorttype text);");
            db.execSQL("CREATE TABLE if not exists Stockreset (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, stockresettype text);");
            db.execSQL("CREATE TABLE if not exists Stockresetmode (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, stockresetoptionsmode text);");
            db.execSQL("CREATE TABLE if not exists Storedays (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, weekdays text, weekends text, swap text);");
            db.execSQL("CREATE TABLE if not exists Universalcredentials (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, username text, password text);");
            db.execSQL("CREATE TABLE if not exists User1 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
            db.execSQL("CREATE TABLE if not exists User2 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
            db.execSQL("CREATE TABLE if not exists User3 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
            db.execSQL("CREATE TABLE if not exists User4 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
            db.execSQL("CREATE TABLE if not exists User5 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
            db.execSQL("CREATE TABLE if not exists User6 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
            db.execSQL("CREATE TABLE if not exists Taxes (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, taxname text, value numeric, taxtype text);");
            db.execSQL("CREATE TABLE if not exists Discount_types (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, discountname text, discountvalue numeric);");
            db.execSQL("CREATE TABLE if not exists Totaltables (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, nooftables text);");
            db.execSQL("CREATE TABLE if not exists asd1 (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, pName text, pDate text, image blob);");

            db.execSQL("CREATE TABLE if not exists LoginUser (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, username text, password text);");
            db.execSQL("CREATE TABLE if not exists UserLogin_Checking (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, username text, password text, name text);");


            db.execSQL("CREATE TABLE if not exists Alaramon_off (_id integer PRIMARY KEY UNIQUE, status text);");
            db.execSQL("CREATE TABLE if not exists Alaramdays (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, selecteddays TEXT, unselecteddays text, swap TEXT);");
            db.execSQL("CREATE TABLE if not exists Alaramtime (_id integer PRIMARY KEY UNIQUE, time text);");

            db.execSQL("CREATE TABLE if not exists BTConn (_id integer PRIMARY KEY UNIQUE, name text, address text, status text, device text);");
            db.execSQL("CREATE TABLE if not exists IPConn (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");

            db.execSQL("CREATE TABLE if not exists Menulogin_checking (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, status text);");
            db.execSQL("CREATE TABLE if not exists Home_check (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, login_status text);");
            //dbllega.execSQL("CREATE TABLE if not exists asd2 (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, pName text, pDate text, image blob);");

            db.execSQL("CREATE TABLE if not exists DeleteDBon_off (_id integer PRIMARY KEY UNIQUE, status text);");
            db.execSQL("CREATE TABLE if not exists Auto_generate_barcode (_id integer PRIMARY KEY UNIQUE, generate text);");
            db.execSQL("CREATE TABLE if not exists DeleteDB_time (_id integer PRIMARY KEY UNIQUE, time text);");
            db.execSQL("CREATE TABLE if not exists Email_setup (_id integer PRIMARY KEY UNIQUE, username text, password text, client text);");
            db.execSQL("CREATE TABLE if not exists Default_credit (_id integer PRIMARY KEY UNIQUE, status text);");
            db.execSQL("CREATE TABLE if not exists Working_hours (_id integer PRIMARY KEY UNIQUE, opening text, opening_time text, closing text, closing_time text," +
                    "opening_time_system text, closing_time_system text);");
            db.execSQL("CREATE TABLE if not exists Printer_text_size (_id integer PRIMARY KEY UNIQUE, type text);");
            db.execSQL("CREATE TABLE if not exists Change_time_format (_id integer PRIMARY KEY UNIQUE, status text);");
            db.execSQL("CREATE TABLE if not exists Hotel1 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, value int);");
            db.execSQL("CREATE TABLE if not exists Discount_details (_id INTEGER PRIMARY KEY UNIQUE, disc_code text, disc_value text, disc_type text);");
            db.execSQL("CREATE TABLE if not exists Email_recipient (_id integer PRIMARY KEY UNIQUE, name text, ph_no text, email text);");
            db.execSQL("CREATE TABLE if not exists Schedule_mail_on_off (_id integer PRIMARY KEY UNIQUE, status text);");
            db.execSQL("CREATE TABLE if not exists Schedule_mail_time (_id integer PRIMARY KEY UNIQUE, time text);");
            db.execSQL("CREATE TABLE if not exists promotions (_id integer PRIMARY KEY UNIQUE, email text);");
            db.execSQL("CREATE TABLE if not exists User_privilege (_id integer PRIMARY KEY UNIQUE, username text, returns_refunds text, product_tax text, reports text," +
                    "settings text, backup text, customer text);");
            db.execSQL("CREATE TABLE if not exists Tax_selec (_id integer PRIMARY KEY UNIQUE, tax_amount text, tax_per text, selected_but text);");
            db.execSQL("CREATE TABLE if not exists Discount_selec (_id integer PRIMARY KEY UNIQUE, discount_amount text, discount_per text, selected_but text);");
            db.execSQL("CREATE TABLE if not exists Vendor_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text, " +
                    "vendor_email text, vendor_address text, vendor_gst text);");
            db.execSQL("CREATE TABLE if not exists Vendor_sold_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text," +
                    "invoice text, billamount text, tax_percent text, tax_amount text, disc_percent text, disc_amount text, total_bill_amount text, from_time text," +
                    "from_date text, due_date text, datetimee_new_from text, datetimee_new_due text, pay text, pending text, total_pay text, pay_date text, pay_time text, pay_datetimeemew text, not_required text);");
            db.execSQL("CREATE TABLE if not exists Vendor_sold_item_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text," +
                    "itemname text, qty_add text, individual_price text, total_price text, invoice text, billamount text, tax_percent text, tax_amount text, disc_percent text," +
                    "disc_amount text, total_bill_amount text, from_time text, from_date text, due_date text, datetimee_new_from text, datetimee_new_due text, pay text, pending text," +
                    "tax1 text, tax2 text, tax3 text, tax4 text, tax5 text, tax6 text, tax7 text, tax8 text, tax9 text, tax10 text, tax11 text, tax12 text, tax13 text," +
                    "tax14 text, tax15 text, tax1_value text, tax2_value text, tax3_value text, tax4_value text, tax5_value text, tax6_value text, tax7_value text," +
                    "tax8_value text, tax9_value text, tax10_value text, tax11_value text, tax12_value text, tax13_value text, tax14_value text, tax15_value text);");
            db.execSQL("CREATE TABLE if not exists Ingredient_items_list (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, ingredient_name text, itemname text, item_qyt_used text," +
                    "currnet_stock text, date1 text, date text, time1 text, time text, modified_datetimee_new text, qty_unit text, required text);");
            db.execSQL("CREATE TABLE if not exists Vendor_temp_list (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vend_phon text, vend_email text," +
                    "vend_gst text, vend_address text, vend_total_bill_amount text, paid text, pending text, bill_no text);");
            db.execSQL("CREATE TABLE if not exists Items_temp_list (_id integer PRIMARY KEY UNIQUE, itemname text, avg_price text, min_price text," +
                    "max_price text, total_qty text, total_price text, barcode text, not_required text);");

            db.execSQL("CREATE TABLE if not exists Ingredient_Vendor_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text, " +
                    "vendor_email text, vendor_address text, vendor_gst text);");
            db.execSQL("CREATE TABLE if not exists Ingredient_sold_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text," +
                    "invoice text, billamount text, tax_percent text, tax_amount text, disc_percent text, disc_amount text, total_bill_amount text, from_time text," +
                    "from_date text, due_date text, datetimee_new_from text, datetimee_new_due text, pay text, pending text, total_pay text, pay_date text, pay_time text, pay_datetimeemew text, not_required text);");
            db.execSQL("CREATE TABLE if not exists Ingredients (_id integer PRIMARY KEY UNIQUE, ingredient_name text, min_req text, max_req text, current_stock text," +
                    "unit text, indiv_price text, total_price text, date text, date1 text, time text, time1 text, datetimee_new text, avg_price text, required text, barcode text," +
                    "status_low text, status_qty_updated text, add_qty text, indiv_price_copy text, adjusted_stock text, diff_stock text, indiv_price_temp text," +
                    "total_price_temp text);");
            db.execSQL("CREATE TABLE if not exists Ingredient_sold_item_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text," +
                    "itemname text, qty_add text, individual_price text, total_price text, invoice text, billamount text, tax_percent text, tax_amount text, disc_percent text," +
                    "disc_amount text, total_bill_amount text, from_time text, from_date text, due_date text, datetimee_new_from text, datetimee_new_due text, pay text, pending text," +
                    "tax1 text, tax2 text, tax3 text, tax4 text, tax5 text, tax6 text, tax7 text, tax8 text, tax9 text, tax10 text, tax11 text, tax12 text, tax13 text," +
                    "tax14 text, tax15 text, tax1_value text, tax2_value text, tax3_value text, tax4_value text, tax5_value text, tax6_value text, tax7_value text," +
                    "tax8_value text, tax9_value text, tax10_value text, tax11_value text, tax12_value text, tax13_value text, tax14_value text, tax15_value text," +
                    "wastage text, unit text);");
            db.execSQL("CREATE TABLE if not exists Ingredients_item_selection_temp (_id integer PRIMARY KEY UNIQUE, itemname text, qty_temp text, qty_temp_unit text, qty text);");
            db.execSQL("CREATE TABLE if not exists Vendor_details_micro (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text, " +
                    "vendor_email text, vendor_address text, vendor_gst text);");
            db.execSQL("CREATE TABLE if not exists Vendor_temp_list_Ingredient (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vend_phon text, vend_email text," +
                    "vend_gst text, vend_address text, vend_total_bill_amount text, paid text, pending text, bill_no text);");
            db.execSQL("CREATE TABLE if not exists Ingredients_temp_list (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, avg_price text, min_price text," +
                    "max_price text, total_qty text, total_price text, barcode text, unit text, wastage_qty text, wastage_cost text, not_required text);");
            db.execSQL("CREATE TABLE if not exists Printer_type (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, printer_type text);");

            db.execSQL("CREATE TABLE if not exists KOT_print (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, kot_print_status text);");
            db.execSQL("CREATE TABLE if not exists Auto_Connect (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, auto_connect_status text);");
            db.execSQL("CREATE TABLE if not exists Weighing_Scale_status (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, Weighing_Scale_onoff text);");
            db.execSQL("CREATE TABLE if not exists Weighing_Scale_name (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, scale_name text);");
            db.execSQL("CREATE TABLE if not exists Sync_time (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, last_time text);");
            db.execSQL("CREATE TABLE if not exists variants_temp (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vari1 text, varprice1 text, vari2 text, varprice2 text," +
                    "vari3 text, varprice3 text, vari4 text, varprice4 text, vari5 text, varprice5 text);");
            db.execSQL("CREATE TABLE if not exists PaytmMerchantReg(_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, Account text, MerchantName text, " +
                    "guid text, MID text, Merchant_key text, PosID text)");
            db.execSQL("CREATE TABLE IF NOT EXISTS MobikwikMerchantReg(_id integer primary key autoincrement unique, Account text, Merchant_name text, " +
                    "Mid_otp text, Secretkey_otp text, Mid_debit text, Secretkey_debit text)");
            db.execSQL("CREATE TABLE IF NOT EXISTS all_transactions" +
                    "(_id integer primary key autoincrement unique, Payment_medium text, merchantRefInvoiceNo text, amount text, cardHolderName text," +
                    " cardBrand text, cardType text, cardNumber text, paymentId text, transactionId text, tdrPercentage text, approved text)");
            db.execSQL("CREATE TABLE IF NOT EXISTS CardSwiperActivation" +
                    "(_id integer primary key autoincrement unique, CardSwiperName text, merchantKey text, partnerkey text, Config_status text)");
            db.execSQL("CREATE TABLE if not exists IPConn_Counter (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");
            db.execSQL("CREATE TABLE if not exists Country_Selection (_id integer PRIMARY KEY UNIQUE, country text);");
            db.execSQL("CREATE TABLE if not exists Round_off (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, round_off_status text);");
            db.execSQL("CREATE VIRTUAL TABLE if not exists Items_Virtual USING fts3(itemname , price , stockquan , category , itemtax ," +
                    "image , weekdaysvalue , weekendsvalue , manualstockvalue , automaticstockresetvalue , clickcount , favourites ," +
                    "disc_type , disc_value , image_text , barcode_value , checked , print_value , quantity_sold , minimum_qty , minimum_qty_copy , add_qty ," +
                    "status_low , status_qty_updated , individual_price , unit_type , tax_value , itemtax2 , tax_value2 , itemtax3 ,tax_value3 ," +
                    "itemtax4 ,tax_value4 ,itemtax5 ,tax_value5 , status_perm , status_temp , variant1 , variant_price1 , variant2 , variant_price2 , variant3 ," +
                    "variant_price3 , variant4 , variant_price4 , variant5 , variant_price5)");
            db.execSQL("CREATE TABLE if not exists BIllingtype (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, billingtype_type text);");
            db.execSQL("CREATE TABLE if not exists Estimate_print (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, status text);");
            db.execSQL("CREATE TABLE if not exists IPConn_KOT1 (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");
            db.execSQL("CREATE TABLE if not exists IPConn_KOT2 (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");
            db.execSQL("CREATE TABLE if not exists IPConn_KOT3 (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");
            db.execSQL("CREATE TABLE if not exists IPConn_KOT4 (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");

            db.execSQL("CREATE TABLE if not exists Name_Dept1 (_id integer PRIMARY KEY UNIQUE, dept1_name text);");
            db.execSQL("CREATE TABLE if not exists Name_Dept2 (_id integer PRIMARY KEY UNIQUE, dept2_name text);");
            db.execSQL("CREATE TABLE if not exists Name_Dept3 (_id integer PRIMARY KEY UNIQUE, dept3_name text);");
            db.execSQL("CREATE TABLE if not exists Name_Dept4 (_id integer PRIMARY KEY UNIQUE, dept4_name text);");

            db.execSQL("CREATE TABLE if not exists Ordertaking_server_ip (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");

            db.execSQL("CREATE TABLE if not exists HomeDelivery_prints (_id integer PRIMARY KEY UNIQUE, companycopy text, customercopy text);");
            db.execSQL("CREATE TABLE if not exists Billcount_tag (_id integer PRIMARY KEY UNIQUE, tag_name text);");

            db.execSQL("CREATE TABLE if not exists Stock_transfer_item_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, qty_add text," +
                    "company text, from_store text, from_device text, to_store text, to_device text, from_time text, from_date text, datetimee_new_from text);");


            db1.execSQL("CREATE TABLE if not exists All_Sales (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, quantity text, price text, total text," +
                    "type text, parent text, parentid text, mod_assigned text, tax text, taxname text, bill_no text, time text, date text, user text, table_id text, billtype text," +
                    "paymentmethod text, billamount_disapply text, billamount_disnotapply text, _idd text, deleted_not text, modifiedquantity text, quantitycopy text, " +
                    "modifiedtotal text, date1 text, datetimee text, disc_type text, disc_value text, newtotal text, disc_thereornot text," +
                    " disc_indiv_total text, new_modified_total text);");

            db1.execSQL("CREATE TABLE if not exists Itemwiseorderlistitems (_id integer PRIMARY KEY UNIQUE, itemno text, itemname text, sales integer, salespercentage integer," +
                    "itemtotalquan text);");
            db1.execSQL("CREATE TABLE if not exists Itemwiseorderlistmodifiers (_id integer PRIMARY KEY UNIQUE, modno text, modname text, sales integer, salespercentage integer," +
                    "modtotalquan text);");
            db1.execSQL("CREATE TABLE if not exists Userwiseorderlistitems (_id integer PRIMARY KEY UNIQUE, username text, sales integer, salespercentage integer);");
            db1.execSQL("CREATE TABLE if not exists Generalorderlistascdesc (_id integer PRIMARY KEY UNIQUE, date text, time text, user text, billno text, " +
                    "billdetails text, sales integer, discountamount text, paymentmethod text, billtype text, itemname text, quan text);");
            db1.execSQL("CREATE TABLE if not exists Generalorderlistascdesc1 (_id integer PRIMARY KEY UNIQUE, date text, time text, user text, billno text, " +
                    "billdetails text, sales integer, discountamount text, paymentmethod text, billtype text, itemname text, quan text, tableid text, individualprice text" +
                    ", date1 text, datetimee text);");
            db1.execSQL("CREATE TABLE if not exists userdata (_id integer PRIMARY KEY UNIQUE, username text, total integer);");
            db1.execSQL("CREATE TABLE if not exists itemdata (_id integer PRIMARY KEY UNIQUE, itemname text, total integer);");

            db1.execSQL("CREATE TABLE if not exists All_Sales_Cancelled (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, quantity text, price text, total text," +
                    "type text, parent text, parentid text, mod_assigned text, tax text, taxname text, bill_no text, time text, date text, user text, billtype text," +
                    " paymentmethod text, billamount_disapply text, billamount_disnotapply text, _idd text, reason text, " +
                    "billamount_cancelled text, date1 text, billamount_cancelled_user text, disc_type text, disc_value text, newtotal text, disc_thereornot text," +
                    " disc_indiv_total text);");

            db1.execSQL("CREATE TABLE if not exists Cancelwiseorderlistitems (_id integer PRIMARY KEY UNIQUE, date text, time text, user text, billno text, sale text, " +
                    "refund text, reason text );");

            db1.execSQL("CREATE TABLE if not exists usercancelleddata (_id integer PRIMARY KEY UNIQUE, username text, total integer);");
            db1.execSQL("CREATE TABLE if not exists Customerdetails (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, name text, phoneno text, emailid text, address text, " +
                    "rupees text, billnumber text);");
            db1.execSQL("CREATE TABLE if not exists Tablepayment (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, tablename text, tableid text, price text, print text);");
            db1.execSQL("CREATE TABLE if not exists Billnumber (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, billnumber text, total text, user text, date text," +
                    " paymentmethod text, billtype text, subtotal text, taxtotal text, roundoff text, globaltaxtotal text);");
            db1.execSQL("CREATE TABLE if not exists Discountdetails (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, date text, time text, billno text, Discountcodeno text, " +
                    "Discount_percent text, Billamount_rupess text, Discount_rupees text, date1 text, original_amount text);");
            db1.execSQL("CREATE TABLE if not exists Cardnumber (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, cardnumber text);");
            db1.execSQL("CREATE TABLE if not exists Splitdata (_id integer PRIMARY KEY UNIQUE, billnum text, total text, splittype text, split1 text, split2 text, split3 text);");
            db1.execSQL("CREATE TABLE if not exists Cust_feedback (_id integer PRIMARY KEY UNIQUE, cust_name text, date text, time text, ambience_rating text, pro_qual_rating text," +
                    " service_rating text, overall_exp_rating text, comments text, percentage text, cust_phoneno text);");
            db1.execSQL("CREATE TABLE if not exists Clicked_cust_name (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, name text);");
            db1.execSQL("CREATE TABLE if not exists Customerdetails_temporary (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, commission_charges text, commission_charges_status text, " +
                    "commission_charges_type text, phoneno text, name text);");
            db1.execSQL("CREATE TABLE if not exists Cusotmer_activity_temp (_id integer PRIMARY KEY UNIQUE, name text, phone_no text, " +
                    "email text, addr text, total_amount text, balance text, discount_value, text, discount_type text, approval_rate text);");
            db1.execSQL("CREATE TABLE if not exists Cusotmer_activity_temp_top3 (_id integer PRIMARY KEY UNIQUE, name text, phone_no text, " +
                    "email text, addr text, total_amount integer, balance text, discount_value, text, discount_type text, approval_rate text);");

            for (int i=1;i<=100;i++ ){
                db1.execSQL("CREATE TABLE if not exists Table" + i + " (_id integer PRIMARY KEY AUTOINCREMENT,quantity text, itemname text, price text, total text, type text," +
                        " parent text, parentid text, modassigned text, tax text, taxname text, disc_type text, disc_value text, newtotal text, disc_thereornot text," +
                        " disc_indiv_total text);");

                db1.execSQL("CREATE TABLE if not exists Table" + i + "payment (_id integer PRIMARY KEY AUTOINCREMENT, tableid text, price text, type text, paymentmethod text, " +
                        " discount text, discounttype text, discountcodenum text, cust_name text, cust_phone_no text, cust_emailid text, cust_address text, due_amount text," +
                        " cardnumber text, amounttendered text, dialog_round text, hometotal text, disc_type text, disc_value text, newtotal text, disc_thereornot text," +
                        " disc_indiv_total text);");

                db1.execSQL("CREATE TABLE if not exists Table" + i + "management (_id integer PRIMARY KEY AUTOINCREMENT, itemname text, qty text, tagg integer, date text, " +
                        " time text, par_id text, itemtype text);");
            }

            db1.execSQL("CREATE TABLE if not exists Top_Reason (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, reason text, value integer);");
            db1.execSQL("CREATE TABLE if not exists Top_Category (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, category text, value integer);");
            db1.execSQL("CREATE TABLE if not exists Itemwiseorderlistcategory (_id integer PRIMARY KEY UNIQUE, itemno text, categoryname text, sales integer, salespercentage integer," +
                    "itemtotalquan text);");

            db1.execSQL("CREATE TABLE if not exists BillCount (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, value text);");

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);


            Cursor co4 = db.rawQuery("SELECT * FROM Modifiers", null);
            int cou4 = co4.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou4).equals("7")){
                db.execSQL("ALTER TABLE Modifiers ADD COLUMN mod_image_text text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co4.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co4_1 = db.rawQuery("SELECT * FROM Hotel", null);
            int cou4_1 = co4_1.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou4_1).equals("5")){
                db.execSQL("ALTER TABLE Hotel ADD COLUMN value int");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co4_1.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co1 = db.rawQuery("SELECT * FROM Items", null);
            int cou1 = co1.getColumnCount();
            //Toast.makeText(getActivity(), "2 "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou1).equals("13") || cou1 == 13){
                db.execSQL("ALTER TABLE Items ADD COLUMN disc_type text DEFAULT 0");
                db.execSQL("ALTER TABLE Items ADD COLUMN disc_value text DEFAULT 0");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co1.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co3 = db.rawQuery("SELECT * FROM Items", null);
            int cou3 = co3.getColumnCount();
            //Toast.makeText(getActivity(), "2 "+String.valueOf(cou3), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou3).equals("15")){
                db.execSQL("ALTER TABLE Items ADD COLUMN image_text text");
//                Toast.makeText(getActivity(), " image_text created 2", Toast.LENGTH_SHORT).show();
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co3.close();

            Cursor threete = db.rawQuery("SELECT * FROM DeleteDB_time ", null);
            if (threete.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("time", "11:30 PM");
                db.insert("DeleteDB_time", null, contentValues);
            }
            threete.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor threet = db.rawQuery("SELECT * FROM DeleteDBon_off ", null);
            if (threet.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("status", "off");
                db.insert("DeleteDBon_off", null, contentValues);
            }
            threet.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor threetf = db.rawQuery("SELECT * FROM Auto_generate_barcode ", null);
            if (threetf.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("generate", "off");
                db.insert("Auto_generate_barcode", null, contentValues);
            }
            threetf.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor twleve = db1.rawQuery("SELECT * FROM Clicked_cust_name", null);
            if (twleve.moveToFirst()){

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", "");
                db1.insert("Clicked_cust_name", null, contentValues);
            }
            twleve.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor threqe = db.rawQuery("SELECT * FROM KOT_print ", null);
            if (threqe.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("kot_print_status", "Yes");
                db.insert("KOT_print", null, contentValues);
            }
            threqe.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor ttthreqe = db.rawQuery("SELECT * FROM Auto_Connect ", null);
            if (ttthreqe.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("auto_connect_status", "No");
                db.insert("Auto_Connect", null, contentValues);
            }
            ttthreqe.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor tathreqee = db.rawQuery("SELECT * FROM Sync_time ", null);
            if (tathreqee.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("last_time", "05 Apr 18, 05:22 PM");
                db.insert("Sync_time", null, contentValues);
            }
            tathreqee.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor tathreqeee = db.rawQuery("SELECT * FROM Round_off ", null);
            if (tathreqeee.moveToFirst()) {

            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("round_off_status", "No");
                db.insert("Round_off", null, contentValues);
            }
            tathreqeee.close();

            Cursor twelvw = db.rawQuery("SELECT * FROM Default_credit", null);
            if (twelvw.moveToFirst()){

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("status", "off");
                db.insert("Default_credit", null, contentValues);
            }
            twelvw.close();

            Cursor thirteen = db.rawQuery("SELECT * FROM Working_hours", null);
            if (thirteen.moveToFirst()){

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("opening", "Today");
                contentValues.put("opening_time", "12:01 AM");
                contentValues.put("closing", "Today");
                contentValues.put("closing_time", "11:59 PM");
                contentValues.put("opening_time_system", "00:01");
                contentValues.put("closing_time_system", "23:59");
                db.insert("Working_hours", null, contentValues);
            }
            thirteen.close();

            Cursor fourteen_1 = db.rawQuery("SELECT * FROM Printer_text_size", null);
            if (fourteen_1.moveToFirst()){

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("type", "Standard");
                db.insert("Printer_text_size", null, contentValues);
            }
            fourteen_1.close();

            Cursor fourteeen_1 = db.rawQuery("SELECT * FROM Change_time_format", null);
            if (fourteeen_1.moveToFirst()){

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("status", "not changed");
                db.insert("Change_time_format", null, contentValues);
            }
            fourteeen_1.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor coo5 = db.rawQuery("SELECT * FROM Items", null);
            int couu5 = coo5.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(couu5).equals("16")){
                db.execSQL("ALTER TABLE Items ADD COLUMN barcode_value text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            coo5.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor coo6 = db.rawQuery("SELECT * FROM Items", null);
            int couu6 = coo6.getColumnCount();
            Log.i(TAG, "Added"+couu6);
            Log.i(TAG, "Added"+couu6);
            Log.i(TAG, "Added"+couu6);
            Log.i(TAG, "Added"+couu6);
            if (String.valueOf(couu6).equals("17")){
                db.execSQL("ALTER TABLE Items ADD COLUMN checked text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                Log.i(TAG, "Added");
                Log.i(TAG, "Added");
                Log.i(TAG, "Added");
                Log.i(TAG, "Added");
            }
            coo6.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor oco7 = db.rawQuery("SELECT * FROM Items", null);
            int ocou7 = oco7.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(ocou7).equals("18")){
                db.execSQL("ALTER TABLE Items ADD COLUMN print_value text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            oco7.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co87 = db.rawQuery("SELECT * FROM User_privilege", null);
            int cou87 = co87.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou87).equals("8")){
                db.execSQL("ALTER TABLE User_privilege ADD COLUMN ingredients text DEFAULT no");
                db.execSQL("ALTER TABLE User_privilege ADD COLUMN subscriptions text DEFAULT no");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co87.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co88 = db.rawQuery("SELECT * FROM Items", null);
            int cou88 = co88.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou88).equals("19")){
                db.execSQL("ALTER TABLE Items ADD COLUMN quantity_sold INTEGER DEFAULT 0");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co88.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co881 = db.rawQuery("SELECT * FROM Items", null);
            int cou881 = co881.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou881).equals("20")){
                db.execSQL("ALTER TABLE Items ADD COLUMN minimum_qty text DEFAULT 0");
                db.execSQL("ALTER TABLE Items ADD COLUMN minimum_qty_copy text DEFAULT 0");
                db.execSQL("ALTER TABLE Items ADD COLUMN add_qty text DEFAULT 0");
                db.execSQL("ALTER TABLE Items ADD COLUMN status_low text");
                db.execSQL("ALTER TABLE Items ADD COLUMN status_qty_updated text");
                db.execSQL("ALTER TABLE Items ADD COLUMN individual_price text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co881.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co882 = db.rawQuery("SELECT * FROM Items", null);
            int cou882 = co882.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou882).equals("26")){
                db.execSQL("ALTER TABLE Items ADD COLUMN unit_type text DEFAULT Unit");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co883 = db.rawQuery("SELECT * FROM Items", null);
            int cou883 = co883.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou883).equals("27")) {
                db.execSQL("ALTER TABLE Items ADD COLUMN tax_value text");
                db.execSQL("ALTER TABLE Items ADD COLUMN itemtax2 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN tax_value2 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN itemtax3 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN tax_value3 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN itemtax4 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN tax_value4 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN itemtax5 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN tax_value5 text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co884 = db.rawQuery("SELECT * FROM Items", null);
            int cou884 = co884.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou884).equals("36")) {
                db.execSQL("ALTER TABLE Items ADD COLUMN status_temp text");
                db.execSQL("ALTER TABLE Items ADD COLUMN status_perm text");
            }

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co885 = db.rawQuery("SELECT * FROM Items", null);
            int cou885 = co885.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou885).equals("38")) {
                db.execSQL("ALTER TABLE Items ADD COLUMN variant1 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant_price1 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant2 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant_price2 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant3 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant_price3 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant4 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant_price4 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant5 text");
                db.execSQL("ALTER TABLE Items ADD COLUMN variant_price5 text");
            }

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co886 = db.rawQuery("SELECT * FROM Items", null);
            int cou886 = co886.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou886).equals("48")) {
                db.execSQL("ALTER TABLE Items ADD COLUMN dept_name text");
            }

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co887 = db.rawQuery("SELECT * FROM Items", null);
            int cou887 = co887.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou887).equals("49")) {
                db.execSQL("ALTER TABLE Items ADD COLUMN add_qty_st text");
                db.execSQL("ALTER TABLE Items ADD COLUMN status_qty_updated_st text");
            }
            co887.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co89 = db.rawQuery("SELECT * FROM Taxes", null);
            int cou89 = co89.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou89).equals("4")){
                db.execSQL("ALTER TABLE Taxes ADD COLUMN checked text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co89.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co90 = db.rawQuery("SELECT * FROM Taxes", null);
            int cou90 = co90.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou90).equals("5")){
                db.execSQL("ALTER TABLE Taxes ADD COLUMN tax1 text DEFAULT dine_in");
                db.execSQL("ALTER TABLE Taxes ADD COLUMN tax2 text DEFAULT takeaway");
                db.execSQL("ALTER TABLE Taxes ADD COLUMN tax3 text DEFAULT homedelivery");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co90.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co901 = db.rawQuery("SELECT * FROM Taxes", null);
            int cou901 = co901.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou901).equals("8")){
                db.execSQL("ALTER TABLE Taxes ADD COLUMN hsn_code text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co901.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co91 = db.rawQuery("SELECT * FROM IPConn", null);
            int cou91 = co91.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou91).equals("4")){
                db.execSQL("ALTER TABLE IPConn ADD COLUMN printer_name text");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co91.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor co92 = db.rawQuery("SELECT * FROM IPConn_Counter", null);
            int cou92 = co92.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou92).equals("4")){
                db.execSQL("ALTER TABLE IPConn_Counter ADD COLUMN printer_name text");
//                Toast.makeText(MainActivity.this, "altered", Toast.LENGTH_SHORT).show();
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co92.close();

            Cursor fgh = db.rawQuery("SELECT * FROM IPConn ", null);
            if (fgh.moveToFirst()) {
                String id = fgh.getString(0);
                ContentValues contentValues = new ContentValues();
                contentValues.put("printer_name", "TM Printer");
                String wherecu1 = "_id = '" + id + "'";
                db.update("IPConn", contentValues, wherecu1, new String[]{});

            }
            fgh.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor sixx = db.rawQuery("SELECT * FROM IPConn_Counter ", null);
            if (sixx.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("ipname", "192.168.1.87");
                contentValues.put("port", "9100");
                contentValues.put("status", "");
                db.insert("IPConn_Counter", null, contentValues);
            }
            sixx.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor fghh = db.rawQuery("SELECT * FROM IPConn_Counter", null);
            if (fghh.moveToFirst()) {
                String id = fghh.getString(0);
                ContentValues contentValues = new ContentValues();
                contentValues.put("printer_name", "TM Printer");
                String wherecu1 = "_id = '" + id + "'";
                db.update("IPConn_Counter", contentValues, wherecu1, new String[]{});
//                Toast.makeText(MainActivity.this, "updated", Toast.LENGTH_SHORT).show();
            }else {
//                Toast.makeText(MainActivity.this, "not updated", Toast.LENGTH_SHORT).show();
            }
            fghh.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor sixxx = db.rawQuery("SELECT * FROM Country_Selection ", null);
            if (sixxx.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("country", "India");
                db.insert("Country_Selection", null, contentValues);
            }
            sixxx.close();

            Cursor cursorrr = db.rawQuery("SELECT * FROM PaytmMerchantReg", null);
            if (cursorrr.moveToFirst()) {

            } else {
                ContentValues contentValuess = new ContentValues();
                contentValuess.put("Account", "Not_Registered");
                contentValuess.put("MerchantName", "");
                contentValuess.put("guid", "");
                contentValuess.put("MID", "");
                contentValuess.put("Merchant_key", "");
                contentValuess.put("PosID", "");
                db.insert("PaytmMerchantReg", null, contentValuess);
            }

            Cursor cur = db.rawQuery("SELECT * FROM MobikwikMerchantReg", null);
            if (cur.moveToFirst()) {

            } else {
                ContentValues contentValuees = new ContentValues();
                contentValuees.put("Account", "Not_Registered");
                contentValuees.put("Merchant_name", "");
                contentValuees.put("Mid_otp", "");
                contentValuees.put("Secretkey_otp", "");
                contentValuees.put("Mid_debit", "");
                contentValuees.put("Secretkey_debit", "");
                db.insert("MobikwikMerchantReg", null, contentValuees);
            }

            Cursor c = db.rawQuery("SELECT * FROM CardSwiperActivation", null);
            if (c.moveToFirst()) {
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("CardSwiperName", "PaySwiff");
                contentValues.put("merchantKey", "");
                contentValues.put("partnerkey", "");
                contentValues.put("Config_status", "Not Activated");
                db.insert("CardSwiperActivation", null, contentValues);

                ContentValues contentValues1 = new ContentValues();
                contentValues1.put("CardSwiperName", "mSwipe");
                contentValues1.put("merchantKey", "");
                contentValues1.put("partnerkey", "");
                contentValues1.put("Config_status", "Not Activated");
                db.insert("CardSwiperActivation", null, contentValues1);
            }


            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co2 = db1.rawQuery("SELECT * FROM Cardnumber", null);
            int cou2 = co2.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou2).equals("2")){
                db1.execSQL("ALTER TABLE Cardnumber ADD COLUMN billnumber text DEFAULT 0");
                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            }
            co2.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co = db1.rawQuery("SELECT * FROM Generalorderlistascdesc1", null);
            int cou = co.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou).equals("16")){
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN cardnumber text DEFAULT 0");
            }
            co.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co5 = db1.rawQuery("SELECT * FROM All_Sales", null);
            int cou5 = co5.getColumnCount();
            if (String.valueOf(cou5).equals("27")){
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN disc_type text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN disc_value text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN newtotal text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN disc_thereornot text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN disc_indiv_total text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN new_modified_total text DEFAULT 0");
            }
            co5.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co6 = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
            int cou6 = co6.getColumnCount();
            if (String.valueOf(cou6).equals("24")){
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_type text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_value text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN newtotal text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_thereornot text DEFAULT 0");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_indiv_total text DEFAULT 0");
            }
            co6.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co7 = db1.rawQuery("SELECT * FROM Table1", null);
            int cou7 = co7.getColumnCount();
            if (String.valueOf(cou7).equals("11")){
                for (int i=1;i<=100;i++ ){
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN disc_type text DEFAULT 0");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN disc_value text DEFAULT 0");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN newtotal text DEFAULT 0");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN disc_thereornot text DEFAULT 0");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN disc_indiv_total text DEFAULT 0");
                }
            }
            co7.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo7 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou7 = coo7.getColumnCount();
            if (String.valueOf(coou7).equals("16")){
                for (int i=1;i<=100;i++ ){
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN status text");
                }
            }
            coo7.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo8 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou8 = coo8.getColumnCount();
            if (String.valueOf(coou8).equals("17")){
                for (int i=1;i<=100;i++ ){
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN tagg integer");
                }
            }
            coo8.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo9 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou9 = coo9.getColumnCount();
            if (String.valueOf(coou9).equals("18")){
                for (int i=1;i<=100;i++ ){
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN date text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN time text");
                }
            }
            coo9.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo10 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou10 = coo10.getColumnCount();
            if (String.valueOf(coou10).equals("20")){
                for (int i=1;i<=100;i++ ){
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN updated_quantity text");
                }
            }
            coo10.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo11 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou11 = coo11.getColumnCount();
            if (String.valueOf(coou11).equals("21")) {
                for (int i = 1; i <= 100; i++) {
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN taxname2 text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN tax2 text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN taxname3 text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN tax3 text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN taxname4 text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN tax4 text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN taxname5 text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN tax5 text");
                }
            }
            coo11.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo12 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou12 = coo12.getColumnCount();
            if (String.valueOf(coou12).equals("29")) {
                for (int i = 1; i <= 100; i++) {
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN category text");
                }
            }
            coo12.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo13 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou13 = coo13.getColumnCount();
            if (String.valueOf(coou13).equals("30")) {
                for (int i = 1; i <= 100; i++) {
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN add_note text");
                }
            }
            coo13.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo14 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou14 = coo14.getColumnCount();
            if (String.valueOf(coou14).equals("31")) {
                for (int i = 1; i <= 100; i++) {
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN dept_name text");
                }
            }
            coo14.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo15 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou15 = coo15.getColumnCount();
            if (String.valueOf(coou15).equals("32")) {
                for (int i = 1; i <= 100; i++) {
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN discount_value text");
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN discount_code text");
                }
            }
            coo15.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo16 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou16 = coo16.getColumnCount();
            if (String.valueOf(coou16).equals("34")) {
                for (int i = 1; i <= 100; i++) {
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN discount_type text");
                }
            }
            coo16.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo17 = db1.rawQuery("SELECT * FROM Table1", null);
            int coou17 = coo17.getColumnCount();
            if (String.valueOf(coou17).equals("35")) {
                for (int i = 1; i <= 100; i++) {
                    db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN barcode_get text");
                }
            }
            coo17.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co8 = db1.rawQuery("SELECT * FROM Billnumber", null);
            int cou8 = co8.getColumnCount();
            if (String.valueOf(cou8).equals("11")){
                db1.execSQL("ALTER TABLE Billnumber ADD COLUMN billcount text");
            }
            co8.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co9 = db1.rawQuery("SELECT * FROM Generalorderlistascdesc1", null);
            int cou9 = co9.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou9).equals("17")){
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN individualtotal text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN billcount text");
            }
            co9.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo91 = db1.rawQuery("SELECT * FROM Generalorderlistascdesc1", null);
            int coou91 = coo91.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
            if (String.valueOf(coou91).equals("19")){
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN hsn_code text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_per text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN globaltax_rs text");
            }
            coo91.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor coo911 = db1.rawQuery("SELECT * FROM Generalorderlistascdesc1", null);
            int coou911 = coo911.getColumnCount();
            if (String.valueOf(coou911).equals("24")){
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name2 text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs2 text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name3 text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs3 text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name4 text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs4 text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name5 text");
                db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs5 text");
            }
            coo911.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co10 = db1.rawQuery("SELECT * FROM Discountdetails", null);
            int cou10 = co10.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou10).equals("10")){
                db1.execSQL("ALTER TABLE Discountdetails ADD COLUMN billcount text");
            }
            co10.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co11 = db1.rawQuery("SELECT * FROM Cancelwiseorderlistitems", null);
            int cou11 = co11.getColumnCount();
            //Toast.makeText(getActivity().this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
            if (String.valueOf(cou11).equals("8")){
                db1.execSQL("ALTER TABLE Cancelwiseorderlistitems ADD COLUMN billcount text");
            }
            co11.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co12 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou12 = co12.getColumnCount();
            if (String.valueOf(cou12).equals("7")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN date1 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN time1 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN date text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN total text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN deposit text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cashout text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN credit text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN charges text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN authentication_pin text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN otp text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN dob text");
            }
            co12.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co13 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou13 = co13.getColumnCount();
            if (String.valueOf(cou13).equals("18")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN refunds text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN total_amount text");
            }
            co13.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co14 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou14 = co14.getColumnCount();
            if (String.valueOf(cou14).equals("20")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cashout_type text");
            }
            co14.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co15 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou15 = co15.getColumnCount();
            if (String.valueOf(cou15).equals("21")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN credit_default text");
            }
            co15.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co16 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou16 = co16.getColumnCount();
            if (String.valueOf(cou16).equals("22")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN commission_charges text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN commission_charges_type text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN commission_charges_status text");
            }
            co16.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co17 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou17 = co17.getColumnCount();
            if (String.valueOf(cou17).equals("25")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN authentication_pin_status text");
            }
            co17.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co18 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou18 = co18.getColumnCount();
            if (String.valueOf(cou18).equals("26")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN dob_alaram text");
            }
            co18.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co19 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou19 = co19.getColumnCount();
            if (String.valueOf(cou19).equals("27")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN default_discount_status text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN default_discount_amount text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN default_discount_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN default_discount_type text");
            }
            co19.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co20 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou20 = co20.getColumnCount();
            if (String.valueOf(cou20).equals("31")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN notes text");
            }
            co20.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co21 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou21 = co21.getColumnCount();
            if (String.valueOf(cou21).equals("32")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cust_account_no text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cust_ifsc_code text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cust_account_holder_name text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cust_bank_name text");
            }
            co21.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co22 = db1.rawQuery("SELECT * FROM All_Sales", null);
            int cou22 = co22.getColumnCount();
            if (String.valueOf(cou22).equals("33")){
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN datetimee_new text");
            }
            co22.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co221 = db1.rawQuery("SELECT * FROM All_Sales", null);
            int cou221 = co221.getColumnCount();
            if (String.valueOf(cou221).equals("34")){
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN hsn_code text");
            }
            co221.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co222 = db1.rawQuery("SELECT * FROM All_Sales", null);
            int cou222 = co222.getColumnCount();
            if (String.valueOf(cou222).equals("35")){
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN taxname2 text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN tax2 text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN taxname3 text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN tax3 text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN taxname4 text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN tax4 text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN taxname5 text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN tax5 text");
            }
            co222.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co223 = db1.rawQuery("SELECT * FROM All_Sales", null);
            int cou223 = co223.getColumnCount();
            if (String.valueOf(cou223).equals("43")){
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN category text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN counterperson_username text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN counterperson_name text");
            }
            co223.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co224 = db1.rawQuery("SELECT * FROM All_Sales", null);
            int cou224 = co224.getColumnCount();
            if (String.valueOf(cou224).equals("46")) {
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN credit text");
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN Phone_num text");
            }
            co224.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co225 = db1.rawQuery("SELECT * FROM All_Sales", null);
            int cou225 = co225.getColumnCount();
            if (String.valueOf(cou225).equals("48")) {
                db1.execSQL("ALTER TABLE All_Sales ADD COLUMN barcode_get text");
            }
            co225.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co25 = db1.rawQuery("SELECT * FROM Billnumber", null);
            int cou25 = co25.getColumnCount();
            if (String.valueOf(cou25).equals("12")){
                db1.execSQL("ALTER TABLE Billnumber ADD COLUMN datetimee_new text");
            }
            co25.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co23 = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
            int cou23 = co23.getColumnCount();
            if (String.valueOf(cou23).equals("29")){
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN datetimee_new text");
            }
            co23.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co231 = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
            int cou231 = co231.getColumnCount();
            if (String.valueOf(cou231).equals("30")) {
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN taxname2 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN tax2 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN taxname3 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN tax3 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN taxname4 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN tax4 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN taxname5 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN tax5 text");
                db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN hsn_code text");
            }
            co231.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co24 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou24 = co24.getColumnCount();
            if (String.valueOf(cou24).equals("36")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN datetimee_new text");
            }
            co24.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co26 = db1.rawQuery("SELECT * FROM Discountdetails", null);
            int cou26 = co26.getColumnCount();
            if (String.valueOf(cou26).equals("11")){
                db1.execSQL("ALTER TABLE Discountdetails ADD COLUMN datetimee_new text");
            }
            co26.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co27 = db1.rawQuery("SELECT * FROM Cardnumber", null);
            int cou27 = co27.getColumnCount();
            if (String.valueOf(cou27).equals("3")){
                db1.execSQL("ALTER TABLE Cardnumber ADD COLUMN datetimee_new text");
            }
            co27.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co241 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou241 = co241.getColumnCount();
            if (String.valueOf(cou241).equals("37")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN user_id text");
            }
            co241.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co242 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou242 = co242.getColumnCount();
            if (String.valueOf(cou242).equals("38")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax1 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax2 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax3 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax4 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax5 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax6 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax7 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax8 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax9 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax10 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax11 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax12 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax13 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax14 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax15 text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax_selection text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax1_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax2_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax3_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax4_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax5_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax6_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax7_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax8_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax9_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax10_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax11_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax12_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax13_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax14_value text");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax15_value text");


                Cursor cf = db1.rawQuery("SELECT * FROM Customerdetails", null);
                if (cf.moveToFirst()) {
                    do {
                        String id = cf.getString(0);
                        int i = 1;
                        Cursor c1_11 = db.rawQuery("SELECT * FROM Taxes WHERE taxtype = 'Globaltax'", null);
                        if (c1_11.moveToFirst()) {
                            do {
                                String tn = c1_11.getString(1);
                                String tn_value = c1_11.getString(2);
                                ContentValues contentValues1 = new ContentValues();
                                contentValues1.put("tax" + i, tn);
                                contentValues1.put("tax" + i + "_value", tn_value);
                                contentValues1.put("tax_selection", "All");
                                String wherecu1 = "_id = '" + id + "'";
                                db1.update("Customerdetails", contentValues1, wherecu1, new String[]{});
                                i++;
                            } while (c1_11.moveToNext());
                        }
                        c1_11.close();
                    }while (cf.moveToNext());
                }
                cf.close();

            }
            co242.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co251 = db1.rawQuery("SELECT * FROM Billnumber", null);
            int cou251 = co251.getColumnCount();
            if (String.valueOf(cou251).equals("13")){
                db1.execSQL("ALTER TABLE Billnumber ADD COLUMN comments_sales text");
            }
            co251.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co28 = db1.rawQuery("SELECT * FROM Cusotmer_activity_temp", null);
            int cou28 = co28.getColumnCount();
            if (String.valueOf(cou28).equals("11")){
                db1.execSQL("ALTER TABLE Cusotmer_activity_temp ADD COLUMN cust_id text DEFAULT off");
            }
            co28.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co29 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou29 = co29.getColumnCount();
            if (String.valueOf(cou29).equals("69")){
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN proceedings text DEFAULT off");
            }
            co29.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co290 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou290 = co290.getColumnCount();
            if (String.valueOf(cou290).equals("70")) {
//                    SaleType text, Cheque_num text,CreditAmount text, SaleTime text, SaleDate text
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN SaleType text DEFAULT off");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN Cheque_num text DEFAULT off");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN CreditAmount text DEFAULT off");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN SaleTime text DEFAULT off");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN SaleDate text DEFAULT off");
            }
            co290.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co291 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou291 = co291.getColumnCount();
            if (String.valueOf(cou291).equals("75")) {
//                    SaleType text, Cheque_num text,CreditAmount text, SaleTime text, SaleDate text
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN Transaction_ID text DEFAULT off");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN Card_Type text DEFAULT off");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN Card_Num text DEFAULT off");
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN RRN text DEFAULT off");
            }
            co291.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co292 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            int cou292 = co292.getColumnCount();
            if (String.valueOf(cou292).equals("79")) {
//                    SaleType text, Cheque_num text,CreditAmount text, SaleTime text, SaleDate text
                db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN pincode text");
            }
            co292.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co30 = db1.rawQuery("SELECT * FROM Itemwiseorderlistitems", null);
            int cou30 = co30.getColumnCount();
            if (String.valueOf(cou30).equals("6")){
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN category");
            }
            co30.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co301 = db1.rawQuery("SELECT * FROM Itemwiseorderlistitems", null);
            int cou301 = co301.getColumnCount();
            if (String.valueOf(cou301).equals("7")){
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN i_buying_price");
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN mi_buying_price");
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN i_cost_value");
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN i_cost_percent");
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN mi_cost_value");
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN mi_cost_percent");
            }
            co301.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co302 = db1.rawQuery("SELECT * FROM Itemwiseorderlistitems", null);
            int cou302 = co302.getColumnCount();
            if (String.valueOf(cou302).equals("13")){
                db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN barcode_value");
            }
            co302.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor co31 = db1.rawQuery("SELECT * FROM Itemwiseorderlistmodifiers", null);
            int cou31 = co31.getColumnCount();
            if (String.valueOf(cou31).equals("6")){
                db1.execSQL("ALTER TABLE Itemwiseorderlistmodifiers ADD COLUMN category");
            }
            co31.close();

            Cursor items1 = db.rawQuery("SELECT * FROM Items WHERE image is null", null);
            if (items1.moveToFirst()){
                do {
                    String id = items1.getString(0);
                    String name = items1.getString(1);
                    //Toast.makeText(MainActivity.this, "image not hter "+name, Toast.LENGTH_SHORT).show();
                    String str1 = name.substring(0, 2);
                    String str2 = str1.toUpperCase();
                    ContentValues contentValues5 = new ContentValues();
                    contentValues5.put("image", "");
                    contentValues5.put("image_text", str2);
                    String where = "_id = '" + id + "'";
                    db.update("Items", contentValues5, where, new String[]{});
                    String where1_v1 = "docid = '" + id + "'";
                    db.update("Items_Virtual", contentValues5, where1_v1, new String[]{});
                }while (items1.moveToNext());
            }
            items1.close();

            Cursor items2 = db.rawQuery("SELECT * FROM Items WHERE image = '' ", null);
            if (items2.moveToFirst()){
                do {
                    String id = items2.getString(0);
                    String name = items2.getString(1);
                    //Toast.makeText(MainActivity.this, "image not hter "+name, Toast.LENGTH_SHORT).show();
                    String str1 = name.substring(0, 2);
                    String str2 = str1.toUpperCase();
                    ContentValues contentValues5 = new ContentValues();
                    contentValues5.put("image", "");
                    contentValues5.put("image_text", str2);
                    String where = "_id = '" + id + "'";
                    db.update("Items", contentValues5, where, new String[]{});
                    String where1_v1 = "docid = '" + id + "'";
                    db.update("Items_Virtual", contentValues5, where1_v1, new String[]{});
                }while (items2.moveToNext());
            }
            items2.close();


            Cursor items3 = db.rawQuery("SELECT * FROM Items", null);
            if (items3.moveToFirst()){
                do {
                    String id = items3.getString(0);
                    String name = items3.getString(1);

                    if (name.contains("'")) {
                        replacecolumnvalue = name.replace("'", " ");

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("itemname", replacecolumnvalue);

                        String where1 = "_id = '" + id + "' ";
                        db.update("Items", contentValues, where1, new String[]{});
                        String where1_v1 = "docid = '" + id + "'";
                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
                    }

                }while (items3.moveToNext());
            }
            items3.close();


            Cursor items4 = db.rawQuery("SELECT * FROM Modifiers", null);
            if (items4.moveToFirst()){
                do {
                    String id = items4.getString(0);
                    String name = items4.getString(1);

                    if (name.contains("'")) {
                        replacemodifiervalue = name.replace("'", " ");

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("modifiername", replacemodifiervalue);

                        String where1 = "_id = '" + id + "' ";
                        db.update("Modifiers", contentValues, where1, new String[]{});
                    }

                }while (items4.moveToNext());
            }
            items4.close();


            Cursor items5 = db.rawQuery("SELECT * FROM Taxes", null);
            if (items5.moveToFirst()){
                do {
                    String id = items5.getString(0);
                    String name = items5.getString(1);

                    if (name.contains("'")) {
                        replacetaxvalue = name.replace("'", " ");

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("taxname", replacetaxvalue);

                        String where1 = "_id = '" + id + "' ";
                        db.update("Taxes", contentValues, where1, new String[]{});
                    }

                }while (items5.moveToNext());
            }
            items5.close();


            Cursor items6 = db.rawQuery("SELECT * FROM Hotel", null);
            if (items6.moveToFirst()){
                do {
                    String id = items6.getString(0);
                    String name = items6.getString(1);

                    if (name.contains("'")) {
                        replacecategoryvalue = name.replace("'", " ");

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("name", replacecategoryvalue);

                        String where1 = "_id = '" + id + "' ";
                        db.update("Hotel", contentValues, where1, new String[]{});
                    }

                }while (items6.moveToNext());
            }
            items6.close();


            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor fourteen = db1.rawQuery("SELECT * FROM All_Sales", null);
            if (fourteen.moveToFirst()){
                do {
                    String id = fourteen.getString(0);
                    String dt = fourteen.getString(26);
                    String datetimee_new = fourteen.getString(33);

                    TextView tv = new TextView(getActivity());
                    tv.setText(datetimee_new);

                    if (tv.getText().toString().equals("")) {

                        if (dt.contains(":")) {
                            dt = dt.replace(":", "");
                        }

                        dt = dt.substring(0, 12);
//                Toast.makeText(MainActivity.this, " "+dt, Toast.LENGTH_SHORT).show();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("datetimee_new", dt);
                        String where11 = "_id = '" + id + "' ";
                        db1.update("All_Sales", contentValues, where11, new String[]{});
                    }
                }while (fourteen.moveToNext());
            }
            fourteen.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor fifteen = db1.rawQuery("SELECT * FROM Billnumber", null);
            if (fifteen.moveToFirst()){
                do {
                    String id = fifteen.getString(0);
                    String billno = fifteen.getString(1);
                    String datetimee_new = fifteen.getString(12);

                    TextView tv = new TextView(getActivity());
                    tv.setText(datetimee_new);

                    if (tv.getText().toString().equals("")) {
                        Cursor fifteen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '"+billno+"'", null);
                        if (fifteen_1.moveToFirst()){
                            String a = fifteen_1.getString(33);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '"+id+"' ";
                            db1.update("Billnumber", contentValues, where11, new String[]{});
                        }else {
                            Cursor fifteen_11 = db1.rawQuery("SELECT * FROM All_Sales_Cancelled WHERE bill_no = '"+billno+"'", null);
                            if (fifteen_11.moveToFirst()){
                                String a = fifteen_11.getString(29);

                                ContentValues contentValues = new ContentValues();
                                contentValues.put("datetimee_new", a);
                                String where11 = "_id = '"+id+"' ";
                                db1.update("Billnumber", contentValues, where11, new String[]{});
                            }
                            fifteen_11.close();
                        }
                        fifteen_1.close();
                    }


                }while (fifteen.moveToNext());
            }
            fifteen.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor sixteen = db1.rawQuery("SELECT * FROM Customerdetails", null);
            if (sixteen.moveToFirst()){
                do {
                    String id = sixteen.getString(0);
                    String billno = sixteen.getString(6);
                    String datetimee_new = sixteen.getString(36);

                    TextView tv = new TextView(getActivity());
                    tv.setText(datetimee_new);

                    if (tv.getText().toString().equals("")) {
                        Cursor sixteen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                        if (sixteen_1.moveToFirst()) {
                            String a = sixteen_1.getString(33);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("Customerdetails", contentValues, where11, new String[]{});
                        }
                        sixteen_1.close();
                    }
                }while (sixteen.moveToNext());
            }
            sixteen.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor seventeen = db1.rawQuery("SELECT * FROM Discountdetails", null);
            if (seventeen.moveToFirst()){
                do {
                    String id = seventeen.getString(0);
                    String billno = seventeen.getString(3);
                    String datetimee_new = seventeen.getString(11);

                    TextView tv = new TextView(getActivity());
                    tv.setText(datetimee_new);

                    if (tv.getText().toString().equals("")) {
                        Cursor seventeen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                        if (seventeen_1.moveToFirst()) {
                            String a = seventeen_1.getString(33);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("Discountdetails", contentValues, where11, new String[]{});
                        }
                        seventeen_1.close();
                    }
                }while (seventeen.moveToNext());
            }
            seventeen.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor eightteen = db1.rawQuery("SELECT * FROM Cardnumber", null);
            if (eightteen.moveToFirst()){
                do {
                    String id = eightteen.getString(0);
                    String billno = eightteen.getString(2);
                    String datetimee_new = eightteen.getString(3);

                    TextView tv = new TextView(getActivity());
                    tv.setText(datetimee_new);

                    if (tv.getText().toString().equals("")) {
                        Cursor eightteen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                        if (eightteen_1.moveToFirst()) {
                            String a = eightteen_1.getString(33);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("Cardnumber", contentValues, where11, new String[]{});
                        }
                        eightteen_1.close();
                    }
                }while (eightteen.moveToNext());
            }
            eightteen.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor nineteen = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
            if (nineteen.moveToFirst()){
                do {
                    String id = nineteen.getString(0);
                    String billno = nineteen.getString(11);
                    String datetimee_new = nineteen.getString(29);

                    TextView tv = new TextView(getActivity());
                    tv.setText(datetimee_new);

                    if (tv.getText().toString().equals("")) {
                        Cursor nineteen_1 = db1.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billno + "'", null);
                        if (nineteen_1.moveToFirst()) {
                            String a = nineteen_1.getString(12);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("All_Sales_Cancelled", contentValues, where11, new String[]{});
                        }
                        nineteen_1.close();
                    }
                }while (nineteen.moveToNext());
            }
            nineteen.close();


            Cursor change_time = db.rawQuery("SELECT * FROM Change_time_format", null);
            if (change_time.moveToFirst()) {
                String zero = change_time.getString(0);
                one_one = change_time.getString(1);
            }
            change_time.close();

            if (one_one.equals("not changed")) {
                db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor fourteen_11 = db1.rawQuery("SELECT * FROM All_Sales", null);
                if (fourteen_11.moveToFirst()) {
                    do {
                        String id = fourteen_11.getString(0);
                        String dt = fourteen_11.getString(26);
                        String dt_new = fourteen_11.getString(33);

                        String dt1 = dt.substring(8, 10);

                        if (dt1.equals("24")){
                            dt = dt.replace(dt1, "00");

                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("datetimee", dt);
                            String where11 = "_id = '"+id+"' ";
                            db1.update("All_Sales", contentValues1, where11, new String[]{});
                        }

                        String dt2 = dt_new.substring(8, 10);

                        if (dt2.equals("24")){
                            dt_new = dt_new.replace(dt2, "00");

                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("datetimee_new", dt_new);
                            String where11 = "_id = '"+id+"' ";
                            db1.update("All_Sales", contentValues1, where11, new String[]{});
                        }



                    } while (fourteen_11.moveToNext());
                }
                fourteen_11.close();

                db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor fifteenn = db1.rawQuery("SELECT * FROM Billnumber", null);
                if (fifteenn.moveToFirst()) {
                    do {
                        String id = fifteenn.getString(0);
                        String billno = fifteenn.getString(1);
                        String dt_new = fifteenn.getString(12);
                        TextView tv1 = new TextView(getActivity());
                        tv1.setText(billno);

                        String dt2 = dt_new.substring(8, 10);
                        if (dt2.equals("24")){
                            dt_new = dt_new.replace(dt2, "00");
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", dt_new);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("Billnumber", contentValues, where11, new String[]{});
                        }

//                    Cursor fifteen_11 = null;
//                    try {
//                        Cursor fifteen_11 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + tv1.getText().toString() + "'", null);
//                        if (fifteen_11.moveToFirst()) {
//                            String a = fifteen_11.getString(33);
//
//
//                        }
//                    }finally {
//                        if(fifteen_11 != null)
//                            fifteen_11.close();
//                    }

                    } while (fifteenn.moveToNext());
                }
                fifteenn.close();

                db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor sixteenn = db1.rawQuery("SELECT * FROM Customerdetails", null);
                if (sixteenn.moveToFirst()) {
                    do {
                        String id = sixteenn.getString(0);
                        String billno = sixteenn.getString(6);

                        Cursor sixteen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                        if (sixteen_1.moveToFirst()) {
                            String a = sixteen_1.getString(33);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("Customerdetails", contentValues, where11, new String[]{});
                        }
                        sixteen_1.close();
                    } while (sixteenn.moveToNext());
                }
                sixteenn.close();

                db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor seventeenn = db1.rawQuery("SELECT * FROM Discountdetails", null);
                if (seventeenn.moveToFirst()) {
                    do {
                        String id = seventeenn.getString(0);
                        String billno = seventeenn.getString(3);

                        Cursor seventeen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                        if (seventeen_1.moveToFirst()) {
                            String a = seventeen_1.getString(33);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("Discountdetails", contentValues, where11, new String[]{});
                        }
                        seventeen_1.close();
                    } while (seventeenn.moveToNext());
                }
                seventeenn.close();

                db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor eightteenn = db1.rawQuery("SELECT * FROM Cardnumber", null);
                if (eightteenn.moveToFirst()) {
                    do {
                        String id = eightteenn.getString(0);
                        String billno = eightteenn.getString(2);

                        Cursor eightteen_1 = db1.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billno + "'", null);
                        if (eightteen_1.moveToFirst()) {
                            String a = eightteen_1.getString(33);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("Cardnumber", contentValues, where11, new String[]{});
                        }
                        eightteen_1.close();
                    } while (eightteenn.moveToNext());
                }
                eightteenn.close();

                db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor nineteenn = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
                if (nineteenn.moveToFirst()) {
                    do {
                        String id = nineteenn.getString(0);
                        String billno = nineteenn.getString(11);

                        Cursor nineteen_1 = db1.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billno + "'", null);
                        if (nineteen_1.moveToFirst()) {
                            String a = nineteen_1.getString(12);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("datetimee_new", a);
                            String where11 = "_id = '" + id + "' ";
                            db1.update("All_Sales_Cancelled", contentValues, where11, new String[]{});
                        }
                        nineteen_1.close();
                    } while (nineteenn.moveToNext());
                }
                nineteenn.close();
            }


            Cursor alert2 = db1.rawQuery("SELECT * FROM All_Sales", null);
            if (alert2.moveToFirst()){
                do {
                    String id = alert2.getString(0);
                    String paym = alert2.getString(17);

                    TextView tv = new TextView(getActivity());
                    tv.setText(paym);

                    ContentValues cv = new ContentValues();
                    if (tv.getText().toString().equals("")){
                        cv.put("paymentmethod", "  Cash");
                        String where  = "bill_no = '" +id+ "' ";
                        db.update("All_Sales", cv, where, new String[]{});
                    }

                } while (alert2.moveToNext());
            }
            alert2.close();

            Cursor alert3 = db1.rawQuery("SELECT * FROM All_Sales", null);
            if (alert3.moveToFirst()){
                do {
                    String id = alert3.getString(0);
                    String billt = alert3.getString(16);

                    TextView tv1 = new TextView(getActivity());
                    tv1.setText(billt);

                    ContentValues cv = new ContentValues();
                    if (tv1.getText().toString().equals("")){
                        cv.put("billtype", "  Dine-in");
                        String where  = "bill_no = '" +id+ "' ";
                        db.update("All_Sales", cv, where, new String[]{});
                    }

                } while (alert3.moveToNext());
            }
            alert3.close();


            Cursor alert21 = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
            if (alert21.moveToFirst()){
                do {
                    String id = alert21.getString(0);
                    String paym = alert21.getString(16);

                    TextView tv = new TextView(getActivity());
                    tv.setText(paym);

                    ContentValues cv = new ContentValues();
                    if (tv.getText().toString().equals("")){
                        cv.put("paymentmethod", "  Cash");
                        String where  = "bill_no = '" +id+ "' ";
                        db.update("All_Sales_Cancelled", cv, where, new String[]{});
                    }

                } while (alert21.moveToNext());
            }
            alert21.close();

            Cursor alert31 = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
            if (alert31.moveToFirst()){
                do {
                    String id = alert31.getString(0);
                    String billt = alert31.getString(15);

                    TextView tv1 = new TextView(getActivity());
                    tv1.setText(billt);

                    ContentValues cv = new ContentValues();
                    if (tv1.getText().toString().equals("")){
                        cv.put("billtype", "  Dine-in");
                        String where  = "billnumber = '" +id+ "' ";
                        db.update("All_Sales_Cancelled", cv, where, new String[]{});
                    }

                } while (alert31.moveToNext());
            }
            alert31.close();


            Cursor alert22 = db1.rawQuery("SELECT * FROM Billnumber", null);
            if (alert22.moveToFirst()){
                do {
                    String id = alert22.getString(0);
                    String paym = alert22.getString(5);

                    TextView tv = new TextView(getActivity());
                    tv.setText(paym);

                    ContentValues cv = new ContentValues();
                    if (tv.getText().toString().equals("")){
                        cv.put("paymentmethod", "  Cash");
                        String where  = "billnumber = '" +id+ "' ";
                        db.update("Billnumber", cv, where, new String[]{});
                    }

                } while (alert22.moveToNext());
            }
            alert22.close();

            Cursor alert32 = db1.rawQuery("SELECT * FROM Billnumber", null);
            if (alert32.moveToFirst()){
                do {
                    String id = alert32.getString(0);
                    String billt = alert32.getString(6);

                    TextView tv1 = new TextView(getActivity());
                    tv1.setText(billt);

                    ContentValues cv = new ContentValues();
                    if (tv1.getText().toString().equals("")){
                        cv.put("billtype", "  Dine-in");
                        String where  = "billnumber = '" +id+ "' ";
                        db.update("Billnumber", cv, where, new String[]{});
                    }

                } while (alert32.moveToNext());
            }
            alert32.close();

            Cursor fifteen_1 = db.rawQuery("SELECT * FROM Change_time_format", null);
            if (fifteen_1.moveToFirst()) {
                String zero = fifteen_1.getString(0);
                ContentValues contentValues = new ContentValues();
                contentValues.put("status", "changed");

                String where11 = "_id = '"+zero+"' ";
                db.update("Change_time_format", contentValues, where11, new String[]{});
            }
            fifteen_1.close();

            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            Cursor usr_id_null = db1.rawQuery("SELECT * FROM Customerdetails WHERE user_id is null", null);
            if (usr_id_null.moveToFirst()){
                do {
                    String id = usr_id_null.getString(0);
                    ContentValues contentValues5 = new ContentValues();
                    contentValues5.put("user_id", "");
                    String where = "_id = '" + id + "'";
                    db1.update("Customerdetails", contentValues5, where, new String[]{});
                }while (usr_id_null.moveToNext());
            }
            usr_id_null.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor mail_schedule = db.rawQuery("SELECT * FROM Schedule_mail_on_off ", null);
            if (mail_schedule.moveToFirst()) {

            }else {

                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", "1");
                contentValues.put("status", "Off");
                db.insert("Schedule_mail_on_off", null, contentValues);

            }
            mail_schedule.close();


            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor printer_type = db.rawQuery("SELECT * FROM Printer_type", null);
            if (printer_type.moveToFirst()) {

            }else {

                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", "1");
                contentValues.put("printer_type", "Generic");
                db.insert("Printer_type", null, contentValues);

            }
            printer_type.close();


            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor user101 = db.rawQuery("SELECT * FROM Schedule_mail_time ", null);
            if (user101.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("time", "11:30 PM");
                db.insert("Schedule_mail_time", null, contentValues);
            }
            user101.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor user102 = db.rawQuery("SELECT * FROM BIllingmode ", null);
            if (user102.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", "1");
                contentValues.put("billingtype", "Fine dine");
                db.insert("BIllingmode", null, contentValues);
            }
            user102.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor billing_type = db.rawQuery("SELECT * FROM BIllingtype ", null);
            if (billing_type.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", "1");
                contentValues.put("billingtype_type", "Dine-in");
                db.insert("BIllingtype", null, contentValues);
            }
            billing_type.close();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor estimate_bill = db.rawQuery("SELECT * FROM Estimate_print ", null);
            if (estimate_bill.moveToFirst()) {

            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", "1");
                contentValues.put("status", "Yes");
                db.insert("Estimate_print", null, contentValues);
            }
            estimate_bill.close();

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);

            dialog.setMessage(getString(R.string.setmessage2));
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


        @Override
        protected void onPostExecute(Integer file_url) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();


            Intent intent = new Intent(getActivity(), SyncHelperService.class);
            intent.putExtra("backup","salesdata");
            getActivity().startService(intent);





            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    dialog.dismiss();



                    final Dialog dialog1 = new Dialog(getActivity(), R.style.notitle);
                    dialog1.setContentView(R.layout.restore_confirmation);
                    dialog1.show();

                    TextView successfilename = (TextView)dialog1.findViewById(R.id.restore_filename);
                    successfilename.setText("'" + backupname.getText().toString() + "'");

                    Button done = (Button)dialog1.findViewById(R.id.gohome);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                        }
                    });

                    Button gotohome = (Button)dialog1.findViewById(R.id.gotohome);
                    gotohome.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (account_selection.toString().equals("Dine")) {
                                Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                                if (cursor3.moveToFirst()) {
                                    String lite_pro = cursor3.getString(1);

                                    TextView tv = new TextView(getActivity());
                                    tv.setText(lite_pro);

                                    if (tv.getText().toString().equals("Lite")) {
                                        Intent intent = new Intent(getActivity(), BeveragesMenuFragment_Dine_l.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(getActivity(), BeveragesMenuFragment_Dine.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                }else {
                                    Intent intent = new Intent(getActivity(), BeveragesMenuFragment_Dine_l.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }else {
                                if (account_selection.toString().equals("Qsr")) {
                                    Intent intent = new Intent(getActivity(), BeveragesMenuFragment_Qsr.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(getActivity(), BeveragesMenuFragment_Retail.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }
//                            Intent intent = new Intent(getActivity(), BeveragesMenuFragment_Dine.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
                        }
                    });
                }
            }, 10000); //3000 L = 3 detik


        }

    }

    class DownloadMusicfromInternet3 extends AsyncTask<String, Void, Integer> {
        private ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);

        @Override
        protected Integer doInBackground(String... params) {

            File dbFile1 = DATA_DIRECTORY_DATABASE;
            String filename1 = "mydb_Appdata";

//            File exportDir1 = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
//            if (!exportDir1.exists()) {
//                exportDir1.mkdirs();
//            }
            File exportDir1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
            if (!exportDir1.exists()) {
                exportDir1.mkdirs();
            }


            //File exportDir = DATABASE_DIRECTORY;
            File file1 = new File(exportDir1, filename1);

            if (!exportDir1.exists()) {
                exportDir1.mkdirs();
            }

            try {
//                    file.createNewFile();
                copyFile(dbFile1, file1);
                Log.e("1", "111");
//                circle1.setVisibility(View.INVISIBLE);
//                tick1.setVisibility(View.VISIBLE);
//                error1.setVisibility(View.INVISIBLE);

                //return true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("2", "22");
//                circle1.setVisibility(View.INVISIBLE);
//                tick1.setVisibility(View.INVISIBLE);
//                error1.setVisibility(View.VISIBLE);
                //return false;
            }

            File dbFile = DATA_DIRECTORY_DATABASE1;
            String filename = "mydb_Salesdata";

//            File exportDir = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
//            if (!exportDir.exists()) {
//                exportDir.mkdirs();
//            }
            File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }


            //File exportDir = DATABASE_DIRECTORY;
            File file = new File(exportDir, filename);

            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            try {
//                    file.createNewFile();
                copyFile(dbFile, file);
                Log.e("1", "1111");
//                circle1.setVisibility(View.INVISIBLE);
//                tick1.setVisibility(View.VISIBLE);
//                error1.setVisibility(View.INVISIBLE);

                //return true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("2", "22");
//                circle1.setVisibility(View.INVISIBLE);
//                tick1.setVisibility(View.INVISIBLE);
//                error1.setVisibility(View.VISIBLE);
                //return false;
            }
            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);

            dialog.setMessage(getString(R.string.setmessage3));
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


        @Override
        protected void onPostExecute(Integer file_url) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();
            dialog.dismiss();

            circle1.setVisibility(View.INVISIBLE);
            tick1.setVisibility(View.VISIBLE);
            error1.setVisibility(View.INVISIBLE);


            List<String> your_array_list = new ArrayList<String>();
//            String path = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_backup/";
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/IVEPOS_backup/";

            File f = new File(path);
            File[] files = f.listFiles();
            for (File inFile : files) {
                if (inFile.isDirectory()) {
                    // in here, you can add directory names into an ArrayList and populate your ListView.
                    your_array_list.add(inFile.getName());
                    //Toast.makeText(getActivity(), "file anmem is "+inFile.getName(), Toast.LENGTH_SHORT).show();
                }
            }

            arrayAdapter = new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    your_array_list );

            lv.setAdapter(arrayAdapter);

        }

    }

    class DownloadMusicfromInternet4 extends AsyncTask<String, Void, Integer> {
        private ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);

        @Override
        protected Integer doInBackground(String... params) {

            File dbFile = DATA_DIRECTORY_DATABASE;
            String filename = "mydb_Appdata";

//            File exportDir = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
//            if (!exportDir.exists()) {
//                exportDir.mkdirs();
//            }
            File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }


            //File exportDir = DATABASE_DIRECTORY;
            File file = new File(exportDir, filename);

            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            try {
//                    file.createNewFile();
                copyFile(dbFile, file);
                Log.e("1", "11111");
//                circle1.setVisibility(View.INVISIBLE);
//                tick1.setVisibility(View.VISIBLE);
//                error1.setVisibility(View.INVISIBLE);

                //return true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("2", "22");
//                circle1.setVisibility(View.INVISIBLE);
//                tick1.setVisibility(View.INVISIBLE);
//                error1.setVisibility(View.VISIBLE);
                //return false;
            }
            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);

            dialog.setMessage(getString(R.string.setmessage3));
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


        @Override
        protected void onPostExecute(Integer file_url) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();
            dialog.dismiss();

            circle1.setVisibility(View.INVISIBLE);
            tick1.setVisibility(View.VISIBLE);
            error1.setVisibility(View.INVISIBLE);


            List<String> your_array_list = new ArrayList<String>();
//            String path = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_backup/";
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/IVEPOS_backup/";

            File f = new File(path);
            File[] files = f.listFiles();
            for (File inFile : files) {
                if (inFile.isDirectory()) {
                    // in here, you can add directory names into an ArrayList and populate your ListView.
                    your_array_list.add(inFile.getName());
                    //Toast.makeText(getActivity(), "file anmem is "+inFile.getName(), Toast.LENGTH_SHORT).show();
                }
            }

            arrayAdapter = new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    your_array_list );

            lv.setAdapter(arrayAdapter);

        }

    }

    class DownloadMusicfromInternet5 extends AsyncTask<String, Void, Integer> {
        private ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);

        @Override
        protected Integer doInBackground(String... params) {

            File dbFile = DATA_DIRECTORY_DATABASE1;
            String filename = "mydb_Salesdata";

//            File exportDir = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
//            if (!exportDir.exists()) {
//                exportDir.mkdirs();
//            }
            File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }


            //File exportDir = DATABASE_DIRECTORY;
            File file = new File(exportDir, filename);

            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            try {
//                    file.createNewFile();
                copyFile(dbFile, file);
                Log.e("1", "1111111");
//                circle1.setVisibility(View.INVISIBLE);
//                tick1.setVisibility(View.VISIBLE);
//                error1.setVisibility(View.INVISIBLE);

                //return true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("2", "22");
//                circle1.setVisibility(View.INVISIBLE);
//                tick1.setVisibility(View.INVISIBLE);
//                error1.setVisibility(View.VISIBLE);
                //return false;
            }
            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);

            dialog.setMessage(getString(R.string.setmessage3));
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


        @Override
        protected void onPostExecute(Integer file_url) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();
            dialog.dismiss();

            circle1.setVisibility(View.INVISIBLE);
            tick1.setVisibility(View.VISIBLE);
            error1.setVisibility(View.INVISIBLE);


            List<String> your_array_list = new ArrayList<String>();
//            String path = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_backup/";
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/IVEPOS_backup/";

            File f = new File(path);
            File[] files = f.listFiles();
            for (File inFile : files) {
                if (inFile.isDirectory()) {
                    // in here, you can add directory names into an ArrayList and populate your ListView.
                    your_array_list.add(inFile.getName());
                    //Toast.makeText(getActivity(), "file anmem is "+inFile.getName(), Toast.LENGTH_SHORT).show();
                }
            }

            arrayAdapter = new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    your_array_list );

            lv.setAdapter(arrayAdapter);

        }

    }

}
