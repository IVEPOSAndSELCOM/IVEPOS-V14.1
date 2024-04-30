package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 1/6/2015.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.intuition.ivepos.syncapp.StubProviderApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Rohithkumar on 1/6/2015.
 */
public class InventoryActivity extends Fragment {

    Fragment frag;
    FragmentTransaction fragTransaction;

    LinearLayout stockcon, control, reset, frequencyreset, normalstock, daysends, resetnew;
    TextView mon, tue, wed, thurs, fri, sat, sun;
    TextView twomon, threetue, fourwed, fivethurs, sixfri, sevensat, onesun;
    Spinner stockcontrol, stockreset, mode, resetfrequency, resetfrequencyretail;
    String On, Off, NAME, selectedSweet, NAME1;
    public SQLiteDatabase db = null;
    int frequency, days;
    int clickcount=0;
    TextView opendialog, opendialogs;
    ListView popupSpinner;
    EditText myFilter, weekdaystock, weekendstock, manualstock;
    ImageView submit;


    Switch mySwitch;
    InventoryActivity inventoryActivity;
    TextView selectitems;
    ListView listView;
    SimpleCursorAdapter adapter;
    Cursor cursor1;

    Uri contentUri,resultUri;

    String WebserviceUrl;
    String account_selection;

    public InventoryActivity(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View rootview = inflater.inflate(R.layout.fragment_multi_inventorypref1, null);
        if (getActivity() instanceof AppCompatActivity){
            androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionbar.setSubtitle("Smart inventory");
        }

        //final ActionBar actionBar = getActivity().getActionBar();


        //actionBar.setTitle("Smart inventory");

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(getActivity());
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

        onesun = (TextView)rootview.findViewById(R.id.one);
        twomon = (TextView)rootview.findViewById(R.id.two);
        threetue = (TextView)rootview.findViewById(R.id.three);
        fourwed = (TextView)rootview.findViewById(R.id.four);
        fivethurs = (TextView)rootview.findViewById(R.id.five);
        sixfri = (TextView)rootview.findViewById(R.id.six);
        sevensat = (TextView)rootview.findViewById(R.id.seven);


        try {
            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            crearYasignar(db);
            //displayListView(rootview);//Generate ListView from SQLite Database
            displaysmartinventory(rootview);
            sunday(rootview);
            monday(rootview);
            tuesday(rootview);
            wednesday(rootview);
            thursday(rootview);
            friday(rootview);
            saturday(rootview);


        }catch (SQLiteException e){

        }
        stockcontrol = (Spinner)rootview.findViewById(R.id.controlstock);

        return rootview;
    }






    public void crearYasignar(SQLiteDatabase dbllega){
        try {

            dbllega.execSQL("CREATE TABLE if not exists Stockreset (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, stockresettype text);");
            dbllega.execSQL("CREATE TABLE if not exists Stockresetmode (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, stockresetoptionsmode text);");
            dbllega.execSQL("CREATE TABLE if not exists Storedays (_id integer PRIMARY KEY UNIQUE, weekdays text, weekends text, swap text);");
            dbllega.execSQL("CREATE TABLE if not exists Itemsstockvalue (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE,itemname text, weekdaysvalue text, weekendsvalue text);");
            dbllega.execSQL("CREATE TABLE if not exists ResetFrequencyRetail (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, resetfrequencyretail text);");
            dbllega.execSQL("CREATE TABLE if not exists ResetFrequencyRestaurant (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, resetfrequencyrestaurant text);");
            //dbllega.execSQL("INSERT INTO Itemsstockvalue (_id, itemname) SELECT _id, itemname FROM Items;");


        }catch (SQLiteException e){

        }
    }



    private void displaysmartinventory(final View rootview){

        mySwitch = (Switch)rootview.findViewById(R.id.mySwitch);


        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor allrowss = db.rawQuery("SELECT * FROM Stockreset WHERE _id = '1'", null);
        if (allrowss.moveToFirst()) {
            do {
                NAME = allrowss.getString(1);
            } while (allrowss.moveToNext());
        }

        Cursor allrows = db.rawQuery("SELECT * FROM Stockcontrol WHERE _id = '1'", null);
        if (allrows.moveToFirst()) {
            do {
                NAME1 = allrows.getString(1);
            } while (allrows.moveToNext());
        }

        if (NAME1.toString().equals("On") && NAME.toString().equals("On")){
            mySwitch.setChecked(true);
            LinearLayout linearLayout = (LinearLayout)rootview.findViewById(R.id.newsmart);
            linearLayout.setVisibility(View.VISIBLE);
            //Toast.makeText(getActivity(), "on", Toast.LENGTH_SHORT).show();
        }else {
            mySwitch.setChecked(false);
            LinearLayout linearLayout = (LinearLayout)rootview.findViewById(R.id.newsmart);
            linearLayout.setVisibility(View.INVISIBLE);
            //Toast.makeText(getActivity(), "off", Toast.LENGTH_SHORT).show();
        }



        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                //Toast.makeText(getActivity(), "12", Toast.LENGTH_SHORT).show();

                if (isChecked) {

                    db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor allrowss = db.rawQuery("SELECT * FROM Stockreset WHERE _id = '1'", null);
                    if (allrowss.moveToFirst()) {
                        do {
                            NAME = allrowss.getString(1);
                        } while (allrowss.moveToNext());
                    }

                    Cursor allrows = db.rawQuery("SELECT * FROM Stockcontrol WHERE _id = '1'", null);
                    if (allrows.moveToFirst()) {
                        do {
                            NAME1 = allrows.getString(1);
                        } while (allrows.moveToNext());
                    }

                    //Toast.makeText(getActivity(), "Checked", Toast.LENGTH_SHORT).show();
                    if (NAME1.toString().equals("On") && NAME.toString().equals("On")) {
                        //Toast.makeText(getActivity(), "Stock is ON, switch is ON", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getActivity(), "Stock is ON, switch is OFF", Toast.LENGTH_SHORT).show();
                        SQLiteDatabase myyDb1 = getActivity().openOrCreateDatabase("mydb_Appdata",
                                Context.MODE_PRIVATE, null);
                        ContentValues newValues = new ContentValues();
                        newValues.put("stockresettype", "Off");
                        String where = "_id = '1'";

                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Stockreset");
                        getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("Stockreset")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id","1")
                                .build();
                        getActivity().getContentResolver().notifyChange(resultUri, null);
//                        myyDb1.update("Stockreset", newValues, where, new String[]{});
                        myyDb1.close();

                        LinearLayout linearLayout = (LinearLayout) rootview.findViewById(R.id.newsmart);
                        linearLayout.setVisibility(View.INVISIBLE);

                    } else {
                        if (NAME1.toString().equals("On") && NAME.toString().equals("Off")) {
                            //Toast.makeText(getActivity(), "Stock is ON, switch is OFF", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getActivity(), "Stock is ON, switch is ON", Toast.LENGTH_SHORT).show();
                            mySwitch.setChecked(true);
                            SQLiteDatabase myyDb1 = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                            ContentValues newValues = new ContentValues();
                            newValues.put("stockresettype", "On");
                            String where = "_id = '1'";
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Stockreset");
                            getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Stockreset")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id","1")
                                    .build();
                            getActivity().getContentResolver().notifyChange(resultUri, null);

//                            myyDb1.update("Stockreset", newValues, where, new String[]{});
                            myyDb1.close();

                            LinearLayout linearLayout = (LinearLayout) rootview.findViewById(R.id.newsmart);
                            linearLayout.setVisibility(View.VISIBLE);

                        } else {
                            mySwitch.setChecked(true);
                            if (NAME1.toString().equals("Off") && NAME.toString().equals("Off")) {
                                //Toast.makeText(getActivity(), "Stock is OFF, switch is OFF", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getActivity(), "Stock is ON, switch is ON", Toast.LENGTH_SHORT).show();
                                final Dialog dialog = new Dialog(getActivity(), R.style.notitle);
                                dialog.setContentView(R.layout.dialog_smartinventory_on);
                                dialog.setCanceledOnTouchOutside(false);

                                dialog.show();
                                //Toast.makeText(getActivity(), "11", Toast.LENGTH_SHORT).show();

                                Button cancel = (Button) dialog.findViewById(R.id.canceldetais);
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //mySwitch.setChecked(false);
                                        dialog.dismiss();
                                        mySwitch.setChecked(false);
                                    }
                                });

                                ImageView cancel1 = (ImageView) dialog.findViewById(R.id.closetext);
                                cancel1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //mySwitch.setChecked(false);
                                        dialog.dismiss();
                                        mySwitch.setChecked(false);
                                    }
                                });

                                Button save = (Button) dialog.findViewById(R.id.savedetais);
                                save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        mySwitch.setChecked(true);


                                        SQLiteDatabase myyDb1 = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                        ContentValues newValues1 = new ContentValues();
                                        newValues1.put("stockresettype", "On");
                                        String where1 = "_id = '1'";
                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Stockreset");
                                        getActivity().getContentResolver().update(contentUri, newValues1,where1,new String[]{});
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProviderApp.AUTHORITY)
                                                .path("Stockreset")
                                                .appendQueryParameter("operation", "update")
                                                .appendQueryParameter("_id","1")
                                                .build();
                                        getActivity().getContentResolver().notifyChange(resultUri, null);
//                                        myyDb1.update("Stockreset", newValues1, where1, new String[]{});


                                        ContentValues newValues = new ContentValues();
                                        newValues.put("stockcontroltype", "On");
                                        String where = "_id = '1'";
                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Stockcontrol");
                                        getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProviderApp.AUTHORITY)
                                                .path("Stockcontrol")
                                                .appendQueryParameter("operation", "update")
                                                .appendQueryParameter("_id","1")
                                                .build();
                                        getActivity().getContentResolver().notifyChange(resultUri, null);
//                                        myyDb1.update("Stockcontrol", newValues, where, new String[]{});

                                        myyDb1.close();

                                        LinearLayout linearLayout = (LinearLayout) rootview.findViewById(R.id.newsmart);
                                        linearLayout.setVisibility(View.VISIBLE);

                                    }
                                });

                                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                    @Override
                                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                        if (keyCode == KeyEvent.KEYCODE_BACK) {

                                            dialog.dismiss();
                                            frag = new InventoryActivity();
//                                            hideKeyboard(getContext());
                                            fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                            fragTransaction.commit();
                                        }
                                        return true;
                                    }
                                });

                            }
                        }
                    }


                } else {


                    db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor allrowss = db.rawQuery("SELECT * FROM Stockreset WHERE _id = '1'", null);
                    if (allrowss.moveToFirst()) {
                        do {
                            NAME = allrowss.getString(1);
                        } while (allrowss.moveToNext());
                    }

                    Cursor allrows = db.rawQuery("SELECT * FROM Stockcontrol WHERE _id = '1'", null);
                    if (allrows.moveToFirst()) {
                        do {
                            NAME1 = allrows.getString(1);
                        } while (allrows.moveToNext());
                    }


                    //Toast.makeText(getActivity(), "Not Checked", Toast.LENGTH_SHORT).show();
                    if (NAME1.toString().equals("On") && NAME.toString().equals("On")) {
                        //Toast.makeText(getActivity(), "Stock is ON, switch is ON", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getActivity(), "Stock is ON, switch is OFF", Toast.LENGTH_SHORT).show();
                        SQLiteDatabase myyDb1 = getActivity().openOrCreateDatabase("mydb_Appdata",
                                Context.MODE_PRIVATE, null);
                        ContentValues newValues = new ContentValues();
                        newValues.put("stockresettype", "Off");
                        String where = "_id = '1'";
                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Stockreset");
                        getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("Stockreset")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id","1")
                                .build();
                        getActivity().getContentResolver().notifyChange(resultUri, null);
//                        myyDb1.update("Stockreset", newValues, where, new String[]{});
                        myyDb1.close();

                        LinearLayout linearLayout = (LinearLayout) rootview.findViewById(R.id.newsmart);
                        linearLayout.setVisibility(View.INVISIBLE);

                    } else {
                        if (NAME1.toString().equals("On") && NAME.toString().equals("Off")) {
                            //Toast.makeText(getActivity(), "Stock is ON, switch is OFF", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getActivity(), "Stock is ON, switch is ON", Toast.LENGTH_SHORT).show();
                            mySwitch.setChecked(true);
                            SQLiteDatabase myyDb1 = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                            ContentValues newValues = new ContentValues();
                            newValues.put("stockresettype", "On");
                            String where = "_id = '1'";
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Stockreset");
                            getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Stockreset")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id","1")
                                    .build();
                            getActivity().getContentResolver().notifyChange(resultUri, null);
//                            myyDb1.update("Stockreset", newValues, where, new String[]{});
                            myyDb1.close();

                            LinearLayout linearLayout = (LinearLayout) rootview.findViewById(R.id.newsmart);
                            linearLayout.setVisibility(View.VISIBLE);

                        } else {
//                            mySwitch.setChecked(false);
//                            if (NAME1.toString().equals("Off") && NAME.toString().equals("Off")){
//                                //Toast.makeText(getActivity(), "Stock is OFF, switch is OFF", Toast.LENGTH_SHORT).show();
//                                //Toast.makeText(getActivity(), "Stock is ON, switch is ON", Toast.LENGTH_SHORT).show();
//                                final Dialog dialog = new Dialog(getActivity());
//                                dialog.setContentView(R.layout.dialog_smartinventory_on);
//                                dialog.show();
//                                //Toast.makeText(getActivity(), "11", Toast.LENGTH_SHORT).show();
//
//                                Button cancel = (Button)dialog.findViewById(R.id.canceldetais);
//                                cancel.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        //mySwitch.setChecked(false);
//                                        dialog.dismiss();
//                                        mySwitch.setChecked(false);
//                                    }
//                                });
//
//                                Button save = (Button)dialog.findViewById(R.id.savedetais);
//                                save.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog.dismiss();
//                                        mySwitch.setChecked(true);
//
//
//                                        SQLiteDatabase myyDb1 =getActivity().openOrCreateDatabase("mydb", Context.MODE_PRIVATE, null);
//                                        ContentValues newValues1 = new ContentValues();
//                                        newValues1.put("stockresettype", "On");
//                                        String where1 = "_id = '1'";
//                                        myyDb1.update("Stockreset", newValues1, where1, new String[]{});
//
//
//                                        ContentValues newValues = new ContentValues();
//                                        newValues.put("stockcontroltype", "On");
//                                        String where = "_id = '1'";
//                                        myyDb1.update("Stockcontrol", newValues, where, new String[]{});
//
//                                        myyDb1.close();
//
//                                        LinearLayout linearLayout = (LinearLayout)rootview.findViewById(R.id.newsmart);
//                                        linearLayout.setVisibility(View.VISIBLE);
//
//                                    }
//                                });
//
//
//                            }
                        }
                    }
                }


            }
        });


        listView = (ListView)rootview.findViewById(R.id.listView);

        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        final String selectQuery = "SELECT * FROM Items ORDER BY itemname";

        final Cursor cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
        // The desired columns to be bound
        final String[] fromFieldNames = {"itemname", "weekdaysvalue", "weekendsvalue"};
        // the XML defined views which the data will be bound to
        final int[] toViewsID = {R.id.name11, R.id.weekdaysvalue1, R.id.weekendsvalue1};
        //Log.e("Checamos que hay id", String.valueOf(R.id.name));
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.smartinventory_row, cursor1, fromFieldNames, toViewsID, 0);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String itemname =  cursor.getString(cursor.getColumnIndex("itemname"));
                String weekdaysv =  cursor.getString(cursor.getColumnIndex("weekdaysvalue"));
                String weekendsv =  cursor.getString(cursor.getColumnIndex("weekendsvalue"));
                final String numitem =  cursor.getString(cursor.getColumnIndex("_id"));
                String catitem =  cursor.getString(cursor.getColumnIndex("category"));
                //Toast.makeText(getActivity(), " " + itemname + " " + weekdaysv + " " + weekendsv, Toast.LENGTH_SHORT).show();

                final Dialog dialog = new Dialog(getActivity(), R.style.notitle);
                dialog.setContentView(R.layout.dialog_smartinventory_edit);
                //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                TextView itemna = (TextView)dialog.findViewById(R.id.itemnameget);
                itemna.setText(itemname);

                final EditText weekdayv = (EditText)dialog.findViewById(R.id.editText1);
                weekdayv.setText(weekdaysv);

                final EditText weekendv = (EditText)dialog.findViewById(R.id.editText2);
                weekendv.setText(weekendsv);

                TextView itemnum = (TextView)dialog.findViewById(R.id.categoryget);
                itemnum.setText(catitem);

                TextView catnum = (TextView)dialog.findViewById(R.id.editText4);
                catnum.setText(numitem);

                ImageButton close1 = (ImageButton)dialog.findViewById(R.id.btndelete);
                close1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                final ImageButton save = (ImageButton)dialog.findViewById(R.id.btnsave);

                weekendv.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_GO) {
                            save.performClick();
                            return true;
                        }
                        return false;
                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("weekdaysvalue", weekdayv.getText().toString());
                        contentValues.put("weekendsvalue", weekendv.getText().toString());
                        String where11 = "_id = '" + numitem + "' ";

                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                        getActivity().getContentResolver().update(contentUri, contentValues,where11,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("Items")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id",numitem)
                                .build();
                        getActivity().getContentResolver().notifyChange(resultUri, null);
//                        db.update("Items", contentValues, where11, new String[]{});
                        String where1_v1 = "docid = '" + numitem + "' ";
                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});


                        dialog.dismiss();


                        listView = (ListView)rootview.findViewById(R.id.listView);

                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        final String selectQuery = "SELECT * FROM Items ORDER BY itemname";

                        final Cursor cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                        // The desired columns to be bound
                        final String[] fromFieldNames = {"itemname", "weekdaysvalue", "weekendsvalue"};
                        // the XML defined views which the data will be bound to
                        final int[] toViewsID = {R.id.name11, R.id.weekdaysvalue1, R.id.weekendsvalue1};
                        //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                        adapter = new SimpleCursorAdapter(getActivity(), R.layout.smartinventory_row, cursor1, fromFieldNames, toViewsID, 0);
                        listView.setAdapter(adapter);

                    }
                });



                dialog.show();

            }
        });



    }


    private void sunday(final View root) {
        sun = (TextView)root.findViewById(R.id.selectsunday);
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        final Cursor allrows = db.rawQuery("SELECT * FROM Storedays where _id ='7' ;", null);
        allrows.moveToFirst();
        while (allrows.isAfterLast() == false) {
            if (allrows.getString(1).equals("Sunday")) {
                sun.setBackgroundColor(getResources().getColor(R.color.white));
                sun.setTextColor(getResources().getColor(R.color.black));
                onesun.setText("");
                sun.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sun.setBackgroundColor(getResources().getColor(R.color.green));
                        sun.setTextColor(getResources().getColor(R.color.white));
                        //mon.setBackgroundResource(R.drawable.category_square);
                        //check how many times clicked and so on
                        //Toast.makeText(getActivity(), "Sunday is Weekend", Toast.LENGTH_SHORT).show();
                        onesun.setText("Sun");
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Storedays SET swap =(SELECT weekdays FROM Storedays WHERE _id = '7') where _id ='7' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Storedays SET weekdays =(SELECT weekends FROM Storedays WHERE _id = '7') where _id ='7' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Storedays SET weekends =(SELECT swap FROM Storedays WHERE _id = '7') where _id ='7' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();
                        }
                        sunday(root);
                    }
                });



            }
            else{
                sun.setBackgroundColor(getResources().getColor(R.color.green));
                sun.setTextColor(getResources().getColor(R.color.white));
                onesun.setText("Sun");
                sun.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sun.setBackgroundColor(getResources().getColor(R.color.white));
                        //Toast.makeText(getActivity(), "Sunday is Weekday!", Toast.LENGTH_SHORT).show();
                        onesun.setText("");
                        sun.setTextColor(getResources().getColor(R.color.black));
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Storedays SET swap =(SELECT weekdays FROM Storedays WHERE _id = '7') where _id ='7' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Storedays SET weekdays =(SELECT weekends FROM Storedays WHERE _id = '7') where _id ='7' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Storedays SET weekends =(SELECT swap FROM Storedays WHERE _id = '7') where _id ='7' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();

                        }
                        sunday(root);
                    }
                });
            }
            allrows.moveToNext();
        }

    }


    private void monday(final View root) {
        mon = (TextView)root.findViewById(R.id.selectmonday);
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        final Cursor allrows = db.rawQuery("SELECT * FROM Storedays where _id ='1' ;", null);
        allrows.moveToFirst();
        while (allrows.isAfterLast() == false) {
            if (allrows.getString(1).equals("Monday")) {
                mon.setBackgroundColor(getResources().getColor(R.color.white));
                mon.setTextColor(getResources().getColor(R.color.black));
                twomon.setText("");
                mon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mon.setBackgroundColor(getResources().getColor(R.color.green));
                        //mon.setBackgroundResource(R.drawable.category_square);
                        //check how many times clicked and so on
                        mon.setTextColor(getResources().getColor(R.color.white));
                        //Toast.makeText(getActivity(), "Monday is Weekend", Toast.LENGTH_SHORT).show();
                        twomon.setText(" Mon");
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Storedays SET swap =(SELECT weekdays FROM Storedays WHERE _id = '1') where _id ='1' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Storedays SET weekdays =(SELECT weekends FROM Storedays WHERE _id = '1') where _id ='1' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Storedays SET weekends =(SELECT swap FROM Storedays WHERE _id = '1') where _id ='1' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();
                        }
                        monday(root);
                    }
                });



            }
            else{
                mon.setBackgroundColor(getResources().getColor(R.color.green));
                twomon.setText(" Mon");
                mon.setTextColor(getResources().getColor(R.color.white));
                mon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mon.setBackgroundColor(getResources().getColor(R.color.white));
                        //Toast.makeText(getActivity(), "Monday is Weekday!", Toast.LENGTH_SHORT).show();
                        twomon.setText("");
                        mon.setTextColor(getResources().getColor(R.color.black));
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Storedays SET swap =(SELECT weekdays FROM Storedays WHERE _id = '1') where _id ='1' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Storedays SET weekdays =(SELECT weekends FROM Storedays WHERE _id = '1') where _id ='1' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Storedays SET weekends =(SELECT swap FROM Storedays WHERE _id = '1') where _id ='1' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();

                        }
                        monday(root);
                    }
                });
            }
            allrows.moveToNext();
        }

    }


    private void tuesday(final View root) {
        tue = (TextView)root.findViewById(R.id.selecttuesday);
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        final Cursor allrows = db.rawQuery("SELECT * FROM Storedays where _id ='2' ;", null);
        allrows.moveToFirst();
        while (allrows.isAfterLast() == false) {
            if (allrows.getString(1).equals("Tuesday")) {
                tue.setBackgroundColor(getResources().getColor(R.color.white));
                threetue.setText("");
                tue.setTextColor(getResources().getColor(R.color.black));
                tue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tue.setBackgroundColor(getResources().getColor(R.color.green));
                        //tue.setBackgroundResource(R.drawable.category_square);
                        //check how many times clicked and so on
                        tue.setTextColor(getResources().getColor(R.color.white));
                        //Toast.makeText(getActivity(), "Tuesday is Weekend", Toast.LENGTH_SHORT).show();
                        threetue.setText(" Tue");
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Storedays SET swap =(SELECT weekdays FROM Storedays WHERE _id = '2') where _id ='2' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Storedays SET weekdays =(SELECT weekends FROM Storedays WHERE _id = '2') where _id ='2' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Storedays SET weekends =(SELECT swap FROM Storedays WHERE _id = '2') where _id ='2' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();
                        }
                        tuesday(root);
                    }
                });



            }
            else{
                tue.setBackgroundColor(getResources().getColor(R.color.green));
                tue.setTextColor(getResources().getColor(R.color.white));
                threetue.setText(" Tue");
                tue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tue.setBackgroundColor(getResources().getColor(R.color.white));
                        tue.setTextColor(getResources().getColor(R.color.black));
                        //Toast.makeText(getActivity(), "Tuesday is Weekday!", Toast.LENGTH_SHORT).show();
                        threetue.setText("");
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Storedays SET swap =(SELECT weekdays FROM Storedays WHERE _id = '2') where _id ='2' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Storedays SET weekdays =(SELECT weekends FROM Storedays WHERE _id = '2') where _id ='2' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Storedays SET weekends =(SELECT swap FROM Storedays WHERE _id = '2') where _id ='2' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();

                        }
                        tuesday(root);
                    }
                });
            }
            allrows.moveToNext();
        }

    }

    private void wednesday(final View root) {
        wed = (TextView)root.findViewById(R.id.selectwednesday);
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        final Cursor allrows = db.rawQuery("SELECT * FROM Storedays where _id ='3' ;", null);
        allrows.moveToFirst();
        while (allrows.isAfterLast() == false) {
            if (allrows.getString(1).equals("Wednesday")) {
                wed.setBackgroundColor(getResources().getColor(R.color.white));
                wed.setTextColor(getResources().getColor(R.color.black));
                fourwed.setText("");
                wed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wed.setBackgroundColor(getResources().getColor(R.color.green));
                        //wed.setBackgroundResource(R.drawable.category_square);
                        //check how many times clicked and so on
                        fourwed.setText(" Wed");
                        wed.setTextColor(getResources().getColor(R.color.white));
                        //Toast.makeText(getActivity(), "Wednesday is Weekend", Toast.LENGTH_SHORT).show();
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Storedays SET swap =(SELECT weekdays FROM Storedays WHERE _id = '3') where _id ='3' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Storedays SET weekdays =(SELECT weekends FROM Storedays WHERE _id = '3') where _id ='3' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Storedays SET weekends =(SELECT swap FROM Storedays WHERE _id = '3') where _id ='3' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();
                        }
                        wednesday(root);
                    }
                });



            }
            else{
                wed.setBackgroundColor(getResources().getColor(R.color.green));
                fourwed.setText(" Wed");
                wed.setTextColor(getResources().getColor(R.color.white));
                wed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wed.setBackgroundColor(getResources().getColor(R.color.white));
                        fourwed.setText("");
                        wed.setTextColor(getResources().getColor(R.color.black));
                        //Toast.makeText(getActivity(), "Wednesday is Weekday!", Toast.LENGTH_SHORT).show();
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Storedays SET swap =(SELECT weekdays FROM Storedays WHERE _id = '3') where _id ='3' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Storedays SET weekdays =(SELECT weekends FROM Storedays WHERE _id = '3') where _id ='3' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Storedays SET weekends =(SELECT swap FROM Storedays WHERE _id = '3') where _id ='3' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();

                        }
                        wednesday(root);
                    }
                });
            }
            allrows.moveToNext();
        }

    }

    private void thursday(final View root) {
        thurs = (TextView)root.findViewById(R.id.selectthursday);
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        final Cursor allrows = db.rawQuery("SELECT * FROM Storedays where _id ='4' ;", null);
        allrows.moveToFirst();
        while (allrows.isAfterLast() == false) {
            if (allrows.getString(1).equals("Thursday")) {
                thurs.setBackgroundColor(getResources().getColor(R.color.white));
                thurs.setTextColor(getResources().getColor(R.color.black));
                fivethurs.setText("");
                thurs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        thurs.setBackgroundColor(getResources().getColor(R.color.green));
                        //thurs.setBackgroundResource(R.drawable.category_square);
                        //check how many times clicked and so on
                        thurs.setTextColor(getResources().getColor(R.color.white));
                        fivethurs.setText(" Thu");
                        //Toast.makeText(getActivity(), "Thursday is Weekend", Toast.LENGTH_SHORT).show();
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Storedays SET swap =(SELECT weekdays FROM Storedays WHERE _id = '4') where _id ='4' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Storedays SET weekdays =(SELECT weekends FROM Storedays WHERE _id = '4') where _id ='4' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Storedays SET weekends =(SELECT swap FROM Storedays WHERE _id = '4') where _id ='4' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();
                        }
                        thursday(root);
                    }
                });



            }
            else{
                thurs.setBackgroundColor(getResources().getColor(R.color.green));
                fivethurs.setText(" Thu");
                thurs.setTextColor(getResources().getColor(R.color.white));
                thurs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        thurs.setBackgroundColor(getResources().getColor(R.color.white));
                        fivethurs.setText("");
                        thurs.setTextColor(getResources().getColor(R.color.black));
                        //Toast.makeText(getActivity(), "Thursday is Weekday!", Toast.LENGTH_SHORT).show();
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Storedays SET swap =(SELECT weekdays FROM Storedays WHERE _id = '4') where _id ='4' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Storedays SET weekdays =(SELECT weekends FROM Storedays WHERE _id = '4') where _id ='4' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Storedays SET weekends =(SELECT swap FROM Storedays WHERE _id = '4') where _id ='4' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();

                        }
                        thursday(root);
                    }
                });
            }
            allrows.moveToNext();
        }

    }

    private void friday(final View root) {
        fri = (TextView)root.findViewById(R.id.selectfriday);
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        final Cursor allrows = db.rawQuery("SELECT * FROM Storedays where _id ='5' ;", null);
        allrows.moveToFirst();
        while (allrows.isAfterLast() == false) {
            if (allrows.getString(1).equals("Friday")) {
                fri.setBackgroundColor(getResources().getColor(R.color.white));
                sixfri.setText("");
                fri.setTextColor(getResources().getColor(R.color.black));
                fri.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fri.setBackgroundColor(getResources().getColor(R.color.green));
                        //fri.setBackgroundResource(R.drawable.category_square);
                        //check how many times clicked and so on
                        sixfri.setText(" Fri");
                        fri.setTextColor(getResources().getColor(R.color.white));
                        //Toast.makeText(getActivity(), "Friday is Weekend", Toast.LENGTH_SHORT).show();
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Storedays SET swap =(SELECT weekdays FROM Storedays WHERE _id = '5') where _id ='5' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Storedays SET weekdays =(SELECT weekends FROM Storedays WHERE _id = '5') where _id ='5' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Storedays SET weekends =(SELECT swap FROM Storedays WHERE _id = '5') where _id ='5' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();
                        }
                        friday(root);
                    }
                });



            }
            else{
                fri.setBackgroundColor(getResources().getColor(R.color.green));
                sixfri.setText(" Fri");
                fri.setTextColor(getResources().getColor(R.color.white));
                fri.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fri.setBackgroundColor(getResources().getColor(R.color.white));
                        sixfri.setText("");
                        fri.setTextColor(getResources().getColor(R.color.black));
                        //Toast.makeText(getActivity(), "Friday is Weekday!", Toast.LENGTH_SHORT).show();
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Storedays SET swap =(SELECT weekdays FROM Storedays WHERE _id = '5') where _id ='5' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Storedays SET weekdays =(SELECT weekends FROM Storedays WHERE _id = '5') where _id ='5' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Storedays SET weekends =(SELECT swap FROM Storedays WHERE _id = '5') where _id ='5' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();

                        }
                        friday(root);
                    }
                });
            }
            allrows.moveToNext();
        }

    }

    private void saturday(final View root) {
        sat = (TextView)root.findViewById(R.id.selectsatuday);
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        final Cursor allrows = db.rawQuery("SELECT * FROM Storedays where _id ='6' ;", null);
        allrows.moveToFirst();
        while (allrows.isAfterLast() == false) {
            if (allrows.getString(1).equals("Saturday")) {
                sat.setBackgroundColor(getResources().getColor(R.color.white));
                sat.setTextColor(getResources().getColor(R.color.black));
                sevensat.setText("");
                sat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sat.setBackgroundColor(getResources().getColor(R.color.green));
                        //sat.setBackgroundResource(R.drawable.category_square);
                        //check how many times clicked and so on
                        sat.setTextColor(getResources().getColor(R.color.white));
                        sevensat.setText(" Sat");
                        //Toast.makeText(getActivity(), "Saturday is Weekend", Toast.LENGTH_SHORT).show();
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Storedays SET swap =(SELECT weekdays FROM Storedays WHERE _id = '6') where _id ='6' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Storedays SET weekdays =(SELECT weekends FROM Storedays WHERE _id = '6') where _id ='6' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Storedays SET weekends =(SELECT swap FROM Storedays WHERE _id = '6') where _id ='6' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();
                        }
                        saturday(root);
                    }
                });



            }
            else{
                sat.setBackgroundColor(getResources().getColor(R.color.green));
                sat.setTextColor(getResources().getColor(R.color.white));
                sevensat.setText(" Sat");
                sat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sat.setBackgroundColor(getResources().getColor(R.color.white));
                        sevensat.setText("");
                        sat.setTextColor(getResources().getColor(R.color.black));
                        //Toast.makeText(getActivity(), "Saturday is Weekday!", Toast.LENGTH_SHORT).show();
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("UPDATE Storedays SET swap =(SELECT weekdays FROM Storedays WHERE _id = '6') where _id ='6' ;", null);
                        Cursor allrows1 = db.rawQuery("UPDATE Storedays SET weekdays =(SELECT weekends FROM Storedays WHERE _id = '6') where _id ='6' ;", null);
                        Cursor allrows2 = db.rawQuery("UPDATE Storedays SET weekends =(SELECT swap FROM Storedays WHERE _id = '6') where _id ='6' ;", null);
                        allrows.moveToFirst();
                        while (allrows.isAfterLast() == false) {
                            allrows.moveToNext();
                        }
                        allrows1.moveToFirst();
                        while (allrows1.isAfterLast() == false) {
                            allrows1.moveToNext();
                        }
                        allrows2.moveToFirst();
                        while (allrows2.isAfterLast() == false) {
                            allrows2.moveToNext();

                        }
                        saturday(root);
                    }
                });
            }
            allrows.moveToNext();
        }

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.

        inflater.inflate(R.menu.inventory_pref, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_general:
                if (account_selection.toString().equals("Dine")) {
                    frag = new GlobalPreferenceActivity();
                    hideKeyboard(getContext());
                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                    fragTransaction.commit();
                }else {
                    if (account_selection.toString().equals("Qsr")) {
                        frag = new GlobalPreferenceActivity_Qsr();
                        hideKeyboard(getContext());
                        fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        fragTransaction.commit();
                    }else {
                        frag = new GlobalPreferenceActivity_Retail();
                        hideKeyboard(getContext());
                        fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        fragTransaction.commit();
                    }
                }
//                frag = new GlobalPreferenceActivity();
//                hideKeyboard(getContext());
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
                break;

            case R.id.action_billsettings:
                frag = new DevicePreferenceActivity();
                hideKeyboard(getContext());
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
                break;


//            case R.id.action_speeddial:
//                frag = new QuickAccessActivity();
//                hideKeyboard(getContext());
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//                break;
//
//            case R.id.action_network:
//                frag = new NetworkPreferenceActivity();
//                hideKeyboard(getContext());
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
