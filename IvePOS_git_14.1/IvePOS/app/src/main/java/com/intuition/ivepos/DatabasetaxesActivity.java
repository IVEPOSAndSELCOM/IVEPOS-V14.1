package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 1/6/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.intuition.ivepos.csv.RequestSingleton;
import com.intuition.ivepos.syncapp.StubProviderApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static android.app.Activity.RESULT_OK;
import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;
import static com.intuition.ivepos.sync.SyncHelper.mAccount;
import static com.intuition.ivepos.syncapp.SyncHelperApp.AUTHORITY;

/**
 * Created by Rohithkumar on 1/6/2015.
 */

public class DatabasetaxesActivity extends Fragment implements ReceiveListener {

    Fragment frag;
    FragmentTransaction fragTransaction;

    public SQLiteDatabase db = null, db1 = null;
    public Cursor cursor;
    SimpleCursorAdapter dataadapter, dataAdapterr;
    ListView listview;
    EditText dialogC1_id, dialogC2_id, dialogC1_id_hsn;
    RadioGroup dialogC3_id;
    TextInputLayout layouttaxname_dialog, layoutvalue_dialog, layoutHSNname_dialog, layouttaxname, layoutvalue, layoutHSNname ;
    LinearLayout select_items, dialog_select_items;
    private RadioButton radioBtn1;
    private RadioGroup radioGroupWebsite;
    Spinner spinner; String selected;
    Dialog dialog;
    RelativeLayout progressbar;
    ProgressBar circle;
    TextView progress_text;
    Uri contentUri,resultUri;


    RelativeLayout progressbar_dialog, header_dialog;
    LinearLayout content_dialog;


    ArrayList<Country1> countryList;
    MyCustomAdapter dataAdapter;
    RelativeLayout linearLayout;
    FloatingActionButton additem;
    EditText text, editText, editText1_HSN; String columnvalue, dialog_columnvalue;
    String rr;
    ListView listView1, listView;
    Country1 country_dialog;
    EditText search;
    RelativeLayout item,category,modifier,tax1, discount1;

    TextView spinneritem;

    CheckBox dialog_select_dine_in, dialog_select_take_away, dialog_select_home_delivery;
    TextView dialog_spinneritem;

    private LinkedHashMap<Item, ArrayList<Item>> groupList;
    ArrayList<String> checking;
    String groupName, select;

    CheckBox select_dine_in1, select_take_away1, select_home_delivery1;
    View rootview;

    String NAme, ID, QUan, NAme_HSN, PRice; int n4;

    AlertDialog alertDialogItems;
    private Printer mPrinter = null;

    ActionMode mode,mode1;

    String WebserviceUrl;

    public DatabasetaxesActivity() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        rootview = inflater.inflate(R.layout.fragment_multi_taxes1, null);

        //final ActionBar actionBar = getActivity().getActionBar();

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(getActivity());
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

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (getActivity() instanceof AppCompatActivity){
            androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionbar.setSubtitle("Tax");

//            actionbar.setSubtitle("Tax");
        }

//        rootview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                LinearLayout options_frame = (LinearLayout) rootview.findViewById(R.id.options_frame);
//                Rect r = new Rect();
//                rootview.getWindowVisibleDisplayFrame(r);
//                int heightDiff = rootview.getRootView().getHeight() - (r.bottom - r.top);
//
//                if (heightDiff > 100) {
//                    options_frame.setVisibility(View.GONE);
//                }else {
//                    options_frame.setVisibility(View.VISIBLE);
//                }
//            }
//        });

        //actionBar.setTitle("Tax");

        item = (RelativeLayout) rootview.findViewById(R.id.item);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.item:
                        Fragment i = new DatabaseitemActivity();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.container, i);
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        ft.commit();
                        break;
                }
            }
        });

        category = (RelativeLayout) rootview.findViewById(R.id.category);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.category:
                        Fragment i = new DatabasecategoryActivity();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.container, i);
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        ft.commit();
                        break;
                }
            }
        });

        modifier = (RelativeLayout) rootview.findViewById(R.id.modifier);
        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.modifier:
                        Fragment i = new DatabaseModifiersActivity();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.container, i);
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        ft.commit();
                        break;
                }
            }
        });

        tax1 = (RelativeLayout) rootview.findViewById(R.id.tax1);
        tax1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.tax1:
                        Fragment i = new DatabasetaxesActivity();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.container, i);
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        ft.commit();
                        break;
                }
            }
        });

        discount1 = (RelativeLayout) rootview.findViewById(R.id.discount1);
        discount1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.discount1:
                        Fragment i = new DatabaseDiscountActivity();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.container, i);
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        ft.commit();
                        break;
                }
            }
        });

        search = (EditText)rootview.findViewById(R.id.searchView);

        linearLayout = (RelativeLayout)rootview.findViewById(R.id.add_item);
        additem = (FloatingActionButton)rootview.findViewById(R.id.add_button);

        progressbar = rootview.findViewById(R.id.progressbar);
        circle = rootview.findViewById(R.id.circle);
        progress_text = rootview.findViewById(R.id.progress_text);
//        circle.getIndeterminateDrawable().setColorFilter(0xFF5D5D5D, android.graphics.PorterDuff.Mode.MULTIPLY);
        listView = (ListView) rootview.findViewById(R.id.listView);
        LinearLayout closeadd = (LinearLayout)rootview.findViewById(R.id.closeadd);
        closeadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "hii", Toast.LENGTH_SHORT).show();
                linearLayout.setVisibility(View.GONE);
                additem.setVisibility(View.VISIBLE);
                hideKeyboard(getContext());
                search.setEnabled(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }
        });

        final EditText one = (EditText)rootview.findViewById(R.id.editText1);
        layouttaxname = (TextInputLayout) rootview.findViewById(R.id.layouttaxname);
        //one.setText("");
        one.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                layouttaxname.setError(null);
            }
        });

        final EditText two = (EditText)rootview.findViewById(R.id.editText2);
        layoutvalue = (TextInputLayout) rootview.findViewById(R.id.layoutvalue);
        //one.setText("");
        two.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                layoutvalue.setError(null);
            }
        });

//        final LinearLayout btncategory = (LinearLayout) rootview.findViewById(R.id.buttoncategory);
//        final LinearLayout btnitems = (LinearLayout) rootview.findViewById(R.id.buttonitems);
//        final LinearLayout btnmodifiers = (LinearLayout) rootview.findViewById(R.id.buttonmodifiers);
//        final LinearLayout btntaxes = (LinearLayout) rootview.findViewById(R.id.buttontaxes);
//        final ImageButton btnaddtax = (ImageButton) rootview.findViewById(R.id.btnaddtax);
//
//
//        btnaddtax.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                CreateTaxActivity hello = new CreateTaxActivity();
//                fragmentTransaction.add(R.id.fragment_container, hello, "HELLO");
//                fragmentTransaction.commit();
//            }
//        });


//        btntaxes.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                actionBar.setTitle(" Taxes");
//                frag = new DatabasetaxesActivity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });
//
//        btnitems.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                actionBar.setTitle(" Items");
//                frag = new DatabaseitemActivity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });
//
//        btncategory.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                actionBar.setTitle(" Category");
//                frag = new DatabasecategoryActivity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });
//
//        btnmodifiers.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                actionBar.setTitle(" Modifiers");
//                frag = new DatabaseModifiersActivity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });

        try {

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);

            //displayListView(rootview);//Generate ListView from SQLite Database

        } catch (SQLiteException e) {
            alertas("Error inesperado: " + e.getMessage());
        }


        //DownloadMusicfromInternetde downloadMusicfromInternetde = new DownloadMusicfromInternetde();
        // downloadMusicfromInternetde.execute();

        db.execSQL("UPDATE Items SET status_temp = '' WHERE status_temp = 'yes'");
        db.execSQL("UPDATE Items SET status_perm = '' WHERE status_temp = 'yes'");

        webservicequery("UPDATE Items set status_temp = '' , status_perm = '' WHERE status_temp = 'yes'");

//        new Thread(new Runnable() {
//            public void run() {
////                    DownloadMusicfromInternetde downloadMusicfromInternetde = new DownloadMusicfromInternetde();
////                    downloadMusicfromInternetde.execute();
//
//
//                Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE status_temp = 'yes'", null);
//                if (cursor.moveToFirst()){
//                    do {
////                            String id = cursor.getString(1);
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("status_temp", "");
//                        contentValues.put("status_perm", "");
//                        String where = " status_temp = 'yes' ";
//
//                        String where1_v1 = " status_temp = 'yes' ";
//                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//
//                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                            getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                            resultUri = new Uri.Builder()
//                                    .scheme("content")
//                                    .authority(StubProviderApp.AUTHORITY)
//                                    .path("Items")
//                                    .appendQueryParameter("operation", "update")
//                                    .appendQueryParameter("status_temp","yes")
//                                    .build();
//                            getActivity().getContentResolver().notifyChange(resultUri, null);
////                            db.update("Items", contentValues, where, new String[]{});
//                        }while (cursor.moveToNext());
//                    }
//                    cursor.close();
//
//            }
//        }).start();


        Cursor c_test = db.rawQuery("SELECT * FROM Taxes", null);
        if (c_test.moveToFirst()) {

        }else {
            db.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE NAME = 'Taxes'");
            webservicequery("ALTER TABLE Taxes AUTO_INCREMENT = 1");
        }





        spinner = (Spinner)rootview.findViewById(R.id.chocolate_category);
        //ArrayList<String> my_arrayy = getTableValues2();
        final ArrayAdapter my_Adapterr = new ArrayAdapter(getActivity(), R.layout.spinner_row, getResources().getStringArray(R.array.taxesss));
        spinner.setAdapter(my_Adapterr);


        if (spinner.getSelectedItem().toString().equals("All")){
            countryList = new ArrayList<Country1>();
            try {
                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                Cursor allrows = db.rawQuery("SELECT * FROM Taxes ", null);
                System.out.println("COUNT : " + allrows.getCount());


                //Country1 country = new Country1(name, name, name, name);

                if (allrows.moveToFirst()) {
                    do {
                        String ID = allrows.getString(0);
                        String NAme = allrows.getString(1);
                        String PlaCe = allrows.getString(2);
                        if (NAme.toString().equals("None")){

                        }else {
                            Country1 NAME = new Country1(ID, NAme);
                            //Country1 PLACE = new Country1(PlaCe, PlaCe, PlaCe, PlaCe);
                            countryList.add(NAME);
                            //countryList.add(PLACE);
                        }

                    } while (allrows.moveToNext());
                }
                allrows.close();
                //db.close();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Error encountered.",
                        Toast.LENGTH_LONG);
            }
        }


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                selected = parent.getItemAtPosition(position).toString();
                if (!selected.equals("All")) {
                    //Toast.makeText(getActivity(), " " + selected, Toast.LENGTH_SHORT).show();
                    countryList = new ArrayList<Country1>();
                    try {
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("SELECT * FROM Taxes WHERE taxtype = '" + selected + "'", null);
                        System.out.println("COUNT : " + allrows.getCount());


                        //Country1 country = new Country1(name, name, name, name);

                        if (allrows.moveToFirst()) {
                            do {
                                String ID = allrows.getString(0);
                                String NAme = allrows.getString(1);
                                String PlaCe = allrows.getString(2);
                                if (NAme.toString().equals("None")){

                                }else {
                                    Country1 NAME = new Country1(ID, NAme);
                                    //Country1 PLACE = new Country1(PlaCe, PlaCe, PlaCe, PlaCe);
                                    countryList.add(NAME);
                                    //countryList.add(PLACE);
                                }

                            } while (allrows.moveToNext());
                        }
                        allrows.close();
                        //db.close();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Error encountered.",
                                Toast.LENGTH_LONG);
                    }
                }
                if (selected.equals("All")) {
                    //Toast.makeText(getActivity(), " " + selected, Toast.LENGTH_SHORT).show();
                    countryList = new ArrayList<Country1>();
                    try {
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("SELECT * FROM Taxes ", null);
                        System.out.println("COUNT : " + allrows.getCount());


                        //Country1 country = new Country1(name, name, name, name);

                        if (allrows.moveToFirst()) {
                            do {
                                String ID = allrows.getString(0);
                                String NAme = allrows.getString(1);
                                String PlaCe = allrows.getString(2);
                                if (NAme.toString().equals("None")){

                                }else {
                                    Country1 NAME = new Country1(ID, NAme);
                                    //Country1 PLACE = new Country1(PlaCe, PlaCe, PlaCe, PlaCe);
                                    countryList.add(NAME);
                                    //countryList.add(PLACE);
                                }
                            } while (allrows.moveToNext());
                        }
                        allrows.close();
                        //db.close();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Error encountered.",
                                Toast.LENGTH_LONG);
                    }
                }


                dataAdapter = new MyCustomAdapter(getActivity(),
                        R.layout.items_deletionbox, countryList);
                listView = (ListView) rootview.findViewById(R.id.listView);
                // Assign adapter to ListView
                listView.setAdapter(dataAdapter);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // When clicked, show a toast with the TextView text
                        country_dialog = (Country1) parent.getItemAtPosition(position);
                        //Toast.makeText(getActivity(), country.getName(), Toast.LENGTH_SHORT).show();


//                        final Cursor cursor = (Cursor) parent.getItemAtPosition(position);
//                        final int item_id = cursor.getInt(cursor.getColumnIndex("_id"));
//                        final String item_content1 = cursor.getString(cursor.getColumnIndex("itemname"));
//                        final String item_content2 = cursor.getString(cursor.getColumnIndex("price"));
//                        final int item_content3 = cursor.getInt(cursor.getColumnIndex("stockquan"));
//                        final String item_content4 = cursor.getString(cursor.getColumnIndexOrThrow("category"));
//                        String myContinent = cursor.getString(cursor.getColumnIndexOrThrow("category"));
//                        final String item_content5 = cursor.getString(cursor.getColumnIndexOrThrow("itemtax"));
//                        String myTax = cursor.getString(cursor.getColumnIndexOrThrow("itemtax"));
//                        final byte[] item_content6 = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                        Cursor allrows2 = db.rawQuery("SELECT * FROM Taxes WHERE taxname = '" + country_dialog.getName() + "' AND _id = '" + country_dialog.getCode() + "' ", null);
                        if (allrows2.moveToFirst()) {
                            dialog = new Dialog(getActivity(), R.style.timepicker_date_dialog);
                            //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.fragment_update_tax);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                            dialog.show();

                            final ImageButton frameLayout = (ImageButton) dialog.findViewById(R.id.btncancel);
                            frameLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    //Toast.makeText(getActivity(), "q", Toast.LENGTH_SHORT).show();
                                    donotshowKeyboard(getActivity());
                                    //dialog.windowSoftInputMode="stateAlwaysHidden"
                                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                                }
                            });

                            progressbar_dialog = dialog.findViewById(R.id.progressbar);
                            header_dialog = dialog.findViewById(R.id.header);
                            content_dialog = dialog.findViewById(R.id.content);

                            Cursor allrows1 = db.rawQuery("SELECT * FROM Taxes WHERE taxname = '" + country_dialog.getName() + "' AND _id = '" + country_dialog.getCode() + "' ", null);
                            if (allrows1.moveToFirst()) {
                                do {
                                    ID = allrows1.getString(0);
                                    NAme = allrows1.getString(1);
                                }while (allrows1.moveToNext());
                            }
                            allrows1.close();

                            DownloadMusicfromInternetde_update01 downloadMusicfromInternetde_update01 = new DownloadMusicfromInternetde_update01();
                            downloadMusicfromInternetde_update01.execute();
                        }else {
                            listView.invalidateViews();

                            if (!selected.equals("All")) {
//                                            //Toast.makeText(getActivity(), " " + selected, Toast.LENGTH_SHORT).show();
                                countryList = new ArrayList<Country1>();
                                try {
//                                                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                    Cursor allrows = db.rawQuery("SELECT * FROM Taxes WHERE taxtype = '" + selected + "'", null);
                                    System.out.println("COUNT : " + allrows.getCount());


                                    //Country1 country = new Country1(name, name, name, name);

                                    if (allrows.moveToFirst()) {
                                        do {
                                            String ID = allrows.getString(0);
                                            String NAme = allrows.getString(1);
                                            String PlaCe = allrows.getString(2);
                                            if (NAme.toString().equals("None")){

                                            }else {
                                                Country1 NAME = new Country1(ID, NAme);
                                                //Country1 PLACE = new Country1(PlaCe, PlaCe, PlaCe, PlaCe);
                                                countryList.add(NAME);
                                                //countryList.add(PLACE);
                                            }
                                        } while (allrows.moveToNext());
                                    }
                                    allrows.close();
                                    //db.close();
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), "Error encountered.",
                                            Toast.LENGTH_LONG);
                                }
                            }
                            if (selected.equals("All")) {
                                //Toast.makeText(getActivity(), " " + selected, Toast.LENGTH_SHORT).show();
                                countryList = new ArrayList<Country1>();
                                try {
//                                                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                    Cursor allrows = db.rawQuery("SELECT * FROM Taxes ", null);
                                    System.out.println("COUNT : " + allrows.getCount());


                                    //Country1 country = new Country1(name, name, name, name);

                                    if (allrows.moveToFirst()) {
                                        do {
                                            String ID = allrows.getString(0);
                                            String NAme = allrows.getString(1);
                                            String PlaCe = allrows.getString(2);
                                            if (NAme.toString().equals("None")){

                                            }else {
                                                Country1 NAME = new Country1(ID, NAme);
                                                //Country1 PLACE = new Country1(PlaCe, PlaCe, PlaCe, PlaCe);
                                                countryList.add(NAME);
                                                //countryList.add(PLACE);
                                            }
                                        } while (allrows.moveToNext());
                                    }
                                    allrows.close();
                                    //db.close();
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), "Error encountered.",
                                            Toast.LENGTH_LONG);
                                }
                            }


                            dataAdapter = new MyCustomAdapter(getActivity(),
                                    R.layout.items_deletionbox, countryList);
                            final ListView listView = (ListView) rootview.findViewById(R.id.listView);
                            // Assign adapter to ListView
                            listView.setAdapter(dataAdapter);
                        }
                        allrows2.close();



//                        DownloadMusicfromInternetde_update0 downloadMusicfromInternetde_update0 = new DownloadMusicfromInternetde_update0();
//                        downloadMusicfromInternetde_update0.execute();

//                        dialog.setCanceledOnTouchOutside(false);


                    }
                });

                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        // TODO  Auto-generated method stub
                        return false;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        // TODO  Auto-generated method stub
                    }

                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        // TODO  Auto-generated method stub
                        mode.getMenuInflater().inflate(R.menu.multiple_delete_category, menu);
                        return true;

                    }

                    @Override
                    public boolean onActionItemClicked(final ActionMode mode,
                                                       MenuItem item) {
//                        mode1=mode;
                        // TODO  Auto-generated method stub
                        switch (item.getItemId()) {
                            case R.id.selectAll:
                                //
                                final int checkedCount = countryList.size();
                                // If item  is already selected or checked then remove or
                                // unchecked  and again select all
                                //adapter.removeSelection();
                                for (int i = 0; i < checkedCount; i++) {
                                    listView.setItemChecked(i, true);
                                    //  listviewadapter.toggleSelection(i);
                                }
                                // Set the  CAB title according to total checked items

                                // Calls  toggleSelection method from ListViewAdapter Class

                                // Count no.  of selected item and print it
                                mode.setTitle(checkedCount+" Selected tax items");
                                return true;
                            case R.id.delete:
                                // Add  dialog for confirmation to delete selected item
                                // record.

                                final Dialog dialogq = new Dialog(getActivity(), R.style.notitle);
                                dialogq.setContentView(R.layout.deleted_tax_selected);

                                ImageView imageVieww = (ImageView) dialogq.findViewById(R.id.closetext);
                                imageVieww.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogq.dismiss();
                                    }
                                });

                                Button buttonn = (Button) dialogq.findViewById(R.id.cancel);
                                buttonn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogq.dismiss();
                                    }
                                });

                                class DownloadMusicfromInternetdel extends AsyncTask<String, Void, Integer> {

                                    @Override
                                    protected Integer doInBackground(String... params) {
                                        try {

                                            int len = listView.getCount();
                                            SparseBooleanArray checked = listView.getCheckedItemPositions();
                                            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                            for (int i = 0; i < len; i++) {
                                                if (checked.get(i)) {

                                                    Country1 country = countryList.get(i);
                                                    String na = country.getName();
                                                    String an = country.getCode();
                                                    /* do whatever you want with the checked item */

//                                                    Cursor taxeitem = db.rawQuery("SELECT * FROM Items", null);
//                                                    //Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
//                                                    if (taxeitem.moveToFirst()){
//                                                        //Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
//                                                        String five = taxeitem.getString(5);
//                                                        String one = taxeitem.getString(1);
//                                                        ContentValues contentValues = new ContentValues();
//                                                        contentValues.put("itemtax", "None");
//                                                        String where = "itemtax = '" + na + "' ";
//
//                                                        String where1_v1 = "itemtax = '" + na + "' ";
//                                                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//
//                                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                                                        getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                                                        resultUri = new Uri.Builder()
//                                                                .scheme("content")
//                                                                .authority(StubProviderApp.AUTHORITY)
//                                                                .path("Items")
//                                                                .appendQueryParameter("operation", "update")
//                                                                .appendQueryParameter("itemtax",na)
//                                                                .build();
//                                                        getActivity().getContentResolver().notifyChange(resultUri, null);
////                                                        db.update("Items", contentValues, where, new String[]{});
//                                                        //}
//                                                    }
//                                                    taxeitem.close();


                                                    db.execSQL("UPDATE Items SET itemtax = 'None' , tax_value = '' WHERE itemtax = '"+na+"'");
                                                    db.execSQL("UPDATE Items SET itemtax2 = 'None' , tax_value2 = '' WHERE itemtax2 = '"+na+"'");
                                                    db.execSQL("UPDATE Items SET itemtax3 = 'None' , tax_value3 = '' WHERE itemtax3 = '"+na+"'");
                                                    db.execSQL("UPDATE Items SET itemtax4 = 'None' , tax_value4 = '' WHERE itemtax4 = '"+na+"'");
                                                    db.execSQL("UPDATE Items SET itemtax5 = 'None' , tax_value5 = '' WHERE itemtax5 = '"+na+"'");
                                                    db.execSQL("UPDATE Items SET status_temp = '' , status_perm = ''");

                                                    if (isDeviceOnline()) {
                                                        webservicequery("UPDATE Items SET itemtax = 'None' , tax_value = '' WHERE itemtax = '"+na+"'");
                                                        webservicequery("UPDATE Items SET itemtax2 = 'None' , tax_value2 = '' WHERE itemtax2 = '"+na+"'");
                                                        webservicequery("UPDATE Items SET itemtax3 = 'None' , tax_value3 = '' WHERE itemtax3 = '"+na+"'");
                                                        webservicequery("UPDATE Items SET itemtax4 = 'None' , tax_value4 = '' WHERE itemtax4 = '"+na+"'");
                                                        webservicequery("UPDATE Items SET itemtax5 = 'None' , tax_value5 = '' WHERE itemtax5 = '"+na+"'");
                                                        webservicequery("UPDATE Items SET status_temp = '' , status_perm = ''");
                                                    }else {

                                                        Bundle extras=new Bundle();
                                                        extras.putString("query","UPDATE Items SET itemtax = 'None' , tax_value = '' WHERE itemtax = '"+na+"'");
                                                        extras.putString("query","UPDATE Items SET itemtax2 = 'None' , tax_value2 = '' WHERE itemtax2 = '"+na+"'");
                                                        extras.putString("query","UPDATE Items SET itemtax3 = 'None' , tax_value3 = '' WHERE itemtax3 = '"+na+"'");
                                                        extras.putString("query","UPDATE Items SET itemtax4 = 'None' , tax_value4 = '' WHERE itemtax4 = '"+na+"'");
                                                        extras.putString("query","UPDATE Items SET itemtax5 = 'None' , tax_value5 = '' WHERE itemtax5 = '"+na+"'");

                                                        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL,true);
                                                        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
                                                        ContentResolver.requestSync(null, AUTHORITY, extras);
                                                    }


                                                    db.execSQL("delete from Taxes WHERE taxname = '"+na+"'");
                                                    if (isDeviceOnline()) {
                                                        webservicequery("delete from Taxes WHERE taxname = '"+na+"'");
                                                    }else {

                                                        Bundle extras=new Bundle();
                                                        extras.putString("query","delete from Taxes WHERE taxname = '"+na+"'");

                                                        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL,true);
                                                        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
                                                        ContentResolver.requestSync(null, AUTHORITY, extras);
                                                    }

                                                    Cursor tax_selection = db1.rawQuery("SELECT * FROM Customerdetails", null);
                                                    if (tax_selection.moveToLast()){
                                                        do {
                                                            ContentValues contentValues1 = new ContentValues();
                                                            contentValues1.put("tax1", "");
                                                            contentValues1.put("tax1_value", "");
                                                            String where1 = "tax1 = '" + na + "' ";
                                                            db1.update("Customerdetails", contentValues1, where1, new String[]{});

                                                            ContentValues contentValues2 = new ContentValues();
                                                            contentValues2.put("tax2", "");
                                                            contentValues2.put("tax2_value", "");
                                                            String where2 = "tax2 = '" + na + "' ";
                                                            db1.update("Customerdetails", contentValues2, where2, new String[]{});

                                                            ContentValues contentValues3 = new ContentValues();
                                                            contentValues3.put("tax3", "");
                                                            contentValues3.put("tax3_value", "");
                                                            String where3 = "tax3 = '" + na + "' ";
                                                            db1.update("Customerdetails", contentValues3, where3, new String[]{});

                                                            ContentValues contentValues4 = new ContentValues();
                                                            contentValues4.put("tax4", "");
                                                            contentValues4.put("tax4_value", "");
                                                            String where4 = "tax4 = '" + na + "' ";
                                                            db1.update("Customerdetails", contentValues4, where4, new String[]{});

                                                            ContentValues contentValues5 = new ContentValues();
                                                            contentValues5.put("tax5", "");
                                                            contentValues5.put("tax5_value", "");
                                                            String where5 = "tax5 = '" + na + "' ";
                                                            db1.update("Customerdetails", contentValues5, where5, new String[]{});

                                                            ContentValues contentValues6 = new ContentValues();
                                                            contentValues6.put("tax6", "");
                                                            contentValues6.put("tax6_value", "");
                                                            String where6 = "tax6 = '" + na + "' ";
                                                            db1.update("Customerdetails", contentValues6, where6, new String[]{});

                                                            ContentValues contentValues7 = new ContentValues();
                                                            contentValues7.put("tax7", "");
                                                            contentValues7.put("tax7_value", "");
                                                            String where7 = "tax7 = '" + na + "' ";
                                                            db1.update("Customerdetails", contentValues7, where7, new String[]{});

                                                            ContentValues contentValues8 = new ContentValues();
                                                            contentValues8.put("tax8", "");
                                                            contentValues8.put("tax8_value", "");
                                                            String where8 = "tax8 = '" + na + "' ";
                                                            db1.update("Customerdetails", contentValues8, where8, new String[]{});

                                                            ContentValues contentValues9 = new ContentValues();
                                                            contentValues9.put("tax9", "");
                                                            contentValues9.put("tax9_value", "");
                                                            String where9 = "tax9 = '" + na + "' ";
                                                            db1.update("Customerdetails", contentValues9, where9, new String[]{});

                                                            ContentValues contentValues10 = new ContentValues();
                                                            contentValues10.put("tax10", "");
                                                            contentValues10.put("tax10_value", "");
                                                            String where10 = "tax10 = '" + na + "' ";
                                                            db1.update("Customerdetails", contentValues10, where10, new String[]{});

                                                            ContentValues contentValues11 = new ContentValues();
                                                            contentValues11.put("tax11", "");
                                                            contentValues11.put("tax11_value", "");
                                                            String where11 = "tax11 = '" + na + "' ";
                                                            db1.update("Customerdetails", contentValues11, where11, new String[]{});

                                                            ContentValues contentValues12 = new ContentValues();
                                                            contentValues12.put("tax12", "");
                                                            contentValues12.put("tax12_value", "");
                                                            String where12 = "tax12 = '" + na + "' ";
                                                            db1.update("Customerdetails", contentValues12, where12, new String[]{});

                                                            ContentValues contentValues13 = new ContentValues();
                                                            contentValues13.put("tax13", "");
                                                            contentValues13.put("tax13_value", "");
                                                            String where13 = "tax13 = '" + na + "' ";
                                                            db1.update("Customerdetails", contentValues13, where13, new String[]{});

                                                            ContentValues contentValues14 = new ContentValues();
                                                            contentValues14.put("tax14", "");
                                                            contentValues14.put("tax14_value", "");
                                                            String where14 = "tax14 = '" + na + "' ";
                                                            db1.update("Customerdetails", contentValues14, where14, new String[]{});

                                                            ContentValues contentValues15 = new ContentValues();
                                                            contentValues15.put("tax15", "");
                                                            contentValues15.put("tax15_value", "");
                                                            String where15 = "tax15 = '" + na + "' ";
                                                            db1.update("Customerdetails", contentValues15, where15, new String[]{});
                                                        }while (tax_selection.moveToNext());
                                                    }
                                                    tax_selection.close();

                                                    String where = "_id = '" + an + "' ";
                                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Taxes");
                                                    getActivity().getContentResolver().delete(contentUri, where, new String[]{});
                                                    resultUri = new Uri.Builder()
                                                            .scheme("content")
                                                            .authority(StubProviderApp.AUTHORITY)
                                                            .path("Taxes")
                                                            .appendQueryParameter("operation", "delete")
                                                            .appendQueryParameter("_id", an)
                                                            .build();
                                                    getActivity().getContentResolver().notifyChange(resultUri, null);
//                                                    db.delete("Taxes", where, new String[]{});


                                                }
                                            }

                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        } catch(Exception e){
                                            e.printStackTrace();
                                        }

                                        return null;
                                    }

                                    // Show Progress bar before downloading Music
                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();

                                        dialogq.dismiss();
                                        progressbar.setVisibility(View.VISIBLE);
                                        progress_text.setText("Deleting...");

                                    }


                                    @Override
                                    protected void onPostExecute(Integer file_url) {

                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressbar.setVisibility(View.GONE);

                                                mode.finish();
                                                listView.invalidateViews();

                                                progress_text.setText("");
                                                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();

                                                if (!selected.equals("All")) {
//                                            //Toast.makeText(getActivity(), " " + selected, Toast.LENGTH_SHORT).show();
                                                    countryList = new ArrayList<Country1>();
                                                    try {
//                                                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                                        Cursor allrows = db.rawQuery("SELECT * FROM Taxes WHERE taxtype = '" + selected + "'", null);
                                                        System.out.println("COUNT : " + allrows.getCount());


                                                        //Country1 country = new Country1(name, name, name, name);

                                                        if (allrows.moveToFirst()) {
                                                            do {
                                                                String ID = allrows.getString(0);
                                                                String NAme = allrows.getString(1);
                                                                String PlaCe = allrows.getString(2);
                                                                if (NAme.toString().equals("None")){

                                                                }else {
                                                                    Country1 NAME = new Country1(ID, NAme);
                                                                    //Country1 PLACE = new Country1(PlaCe, PlaCe, PlaCe, PlaCe);
                                                                    countryList.add(NAME);
                                                                    //countryList.add(PLACE);
                                                                }
                                                            } while (allrows.moveToNext());
                                                        }
                                                        allrows.close();
                                                        //db.close();
                                                    } catch (Exception e) {
                                                        Toast.makeText(getActivity(), "Error encountered.",
                                                                Toast.LENGTH_LONG);
                                                    }
                                                }
                                                if (selected.equals("All")) {
                                                    //Toast.makeText(getActivity(), " " + selected, Toast.LENGTH_SHORT).show();
                                                    countryList = new ArrayList<Country1>();
                                                    try {
//                                                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                                        Cursor allrows = db.rawQuery("SELECT * FROM Taxes ", null);
                                                        System.out.println("COUNT : " + allrows.getCount());


                                                        //Country1 country = new Country1(name, name, name, name);

                                                        if (allrows.moveToFirst()) {
                                                            do {
                                                                String ID = allrows.getString(0);
                                                                String NAme = allrows.getString(1);
                                                                String PlaCe = allrows.getString(2);
                                                                if (NAme.toString().equals("None")){

                                                                }else {
                                                                    Country1 NAME = new Country1(ID, NAme);
                                                                    //Country1 PLACE = new Country1(PlaCe, PlaCe, PlaCe, PlaCe);
                                                                    countryList.add(NAME);
                                                                    //countryList.add(PLACE);
                                                                }
                                                            } while (allrows.moveToNext());
                                                        }
                                                        allrows.close();
                                                        //db.close();
                                                    } catch (Exception e) {
                                                        Toast.makeText(getActivity(), "Error encountered.",
                                                                Toast.LENGTH_LONG);
                                                    }
                                                }


                                                dataAdapter = new MyCustomAdapter(getActivity(),
                                                        R.layout.items_deletionbox, countryList);
                                                final ListView listView = (ListView) rootview.findViewById(R.id.listView);
                                                // Assign adapter to ListView
                                                listView.setAdapter(dataAdapter);

                                            }
                                        }, 20000); //3000 L = 3 detik

                                    }
                                }

                                Button buttonnn = (Button) dialogq.findViewById(R.id.ok);
                                buttonnn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        DownloadMusicfromInternetdel DownloadMusicfromInternetdel = new DownloadMusicfromInternetdel();
                                        DownloadMusicfromInternetdel.execute();

                                    }
                                });

                                dialogq.show();


                                return true;
                            default:
                                return false;
                        }

                    }

                    @Override
                    public void onItemCheckedStateChanged(ActionMode mode,
                                                          int position, long id, boolean checked) {
                        // TODO  Auto-generated method stub
                        final int checkedCount = listView.getCheckedItemCount();
                        String st = dataAdapter.toString();
                        // Set the  CAB title according to total checked items
                        //Toast.makeText(getActivity(), "  Selected4", Toast.LENGTH_SHORT).show();
                        if (listView.isItemChecked(position)) {

                        }
                        mode.setTitle(checkedCount+" Selected tax items");
                        // Calls  toggleSelection method from ListViewAdapter Class
                        //adapter.toggleSelection(position);
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final EditText myFilter = (EditText) rootview.findViewById(R.id.searchView);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        ImageView deleteicon = (ImageView)rootview.findViewById(R.id.delete_icon);
        deleteicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFilter.setText("");
            }
        });



        additem = (FloatingActionButton) rootview.findViewById(R.id.add_button);
        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                CreateItemActivity hello = new CreateItemActivity();
//                fragmentTransaction.add(R.id.add_item, hello, "HELLO");
//                fragmentTransaction.commit();

//                Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE status_temp = 'yes'", null);
//                if (cursor.moveToFirst()){
//                    do {
//                        String id = cursor.getString(0);
//
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("status_temp", "");
//                        contentValues.put("status_perm", "");
//                        String where = " _id ='" + id + "'";
//                        db.update("Items", contentValues, where, new String[]{});
//                    }while (cursor.moveToNext());
//                }
//                cursor.close();

                new Thread(new Runnable() {
                    public void run() {
//                    DownloadMusicfromInternetde downloadMusicfromInternetde = new DownloadMusicfromInternetde();
//                    downloadMusicfromInternetde.execute();

//                        Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE status_temp = 'yes'", null);
//                        if (cursor.moveToFirst()){
//                            do {
////                                String id = cursor.getString(0);
//                                ContentValues contentValues = new ContentValues();
//                                contentValues.put("status_temp", "");
//                                contentValues.put("status_perm", "");
//                                String where = " status_temp = 'yes' ";
//
//                                //   db.update("Items", contentValues, where, new String[]{});
//
//                                String where1_v1 = " status_temp = 'yes' ";
//                                db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//
//                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                                getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                                resultUri = new Uri.Builder()
//                                        .scheme("content")
//                                        .authority(StubProviderApp.AUTHORITY)
//                                        .path("Items")
//                                        .appendQueryParameter("operation", "update")
//                                        .appendQueryParameter("status_temp","yes")
//                                        .build();
//                                getActivity().getContentResolver().notifyChange(resultUri, null);
////                                db.update("Items", contentValues, where, new String[]{});
//                            }while (cursor.moveToNext());
//                        }
//                        cursor.close();

                        db.execSQL("UPDATE Items set status_temp = '' , status_perm = ''");
                        webservicequery("UPDATE Items set status_temp = '' , status_perm = ''");

                    }
                }).start();

                linearLayout = (RelativeLayout)rootview.findViewById(R.id.add_item);
                linearLayout.setVisibility(View.VISIBLE);
                additem.setVisibility(View.GONE);
                search.setEnabled(false);

                text = (EditText) rootview.findViewById(R.id.editText1);
                layouttaxname = (TextInputLayout) rootview.findViewById(R.id.layouttaxname);


                layoutHSNname = (TextInputLayout) rootview.findViewById(R.id.layoutHSNname);
                select_items = (LinearLayout) rootview.findViewById(R.id.select_items);


                columnvalue  = text.getText().toString();
                if (text.getText().toString().contains("'")) {
                    columnvalue  = text.getText().toString().replaceAll("'", " ");
                }


                editText = (EditText) rootview.findViewById(R.id.editText2);
                layoutvalue = (TextInputLayout) rootview.findViewById(R.id.layoutvalue);

                editText1_HSN = (EditText) rootview.findViewById(R.id.editText1_HSN);

                radioGroupWebsite = (RadioGroup) rootview.findViewById
                        (R.id.radioGroup1);

                final int selected1 =
                        radioGroupWebsite.getCheckedRadioButtonId();
                radioBtn1 = (RadioButton) rootview.findViewById(selected1);

                final TextView tax_value_watcher = (TextView) rootview.findViewById(R.id.tax_value_watcher);
                editText.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        tax_value_watcher.setText(s);
                    }
                });


                radioGroupWebsite.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
                    public void onCheckedChanged(RadioGroup group, int checkedId)
                    {
                        // This will get the radiobutton that has changed in its check state
                        RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                        // This puts the value (true/false) into the variable
                        if (checkedRadioButton.getText().toString().equals("Globaltax")){
                            layoutHSNname.setVisibility(View.GONE);
                            select_items.setVisibility(View.GONE);
                        }else {
                            layoutHSNname.setVisibility(View.VISIBLE);
                            select_items.setVisibility(View.VISIBLE);
                        }
                    }
                });

                EditText one = (EditText)rootview.findViewById(R.id.editText1);
                one.setText("");
                EditText two = (EditText)rootview.findViewById(R.id.editText2);
                two.setText("");
                EditText three = (EditText)rootview.findViewById(R.id.editText1_HSN);
                three.setText("");
                one.requestFocus();
                //hideKeyboard(getContext());
                //showKeyboard(getActivity());
                //displayKeyboard();

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(one, InputMethodManager.SHOW_IMPLICIT);

                InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.showSoftInput(text, InputMethodManager.SHOW_IMPLICIT);

                final RadioButton global = (RadioButton)rootview.findViewById(R.id.btnglobal);
                RadioButton item = (RadioButton)rootview.findViewById(R.id.btnitem);
                if (!global.isChecked() && !item.isChecked()){
                    global.setChecked(true);
                }

                final LinearLayout tax_seletion = (LinearLayout) rootview.findViewById(R.id.tax_seletion);

                final CheckBox select_dine_in = (CheckBox)rootview.findViewById(R.id.select_dine_in);
                final CheckBox select_take_away = (CheckBox)rootview.findViewById(R.id.select_take_away);
                final CheckBox select_home_delivery = (CheckBox)rootview.findViewById(R.id.select_home_delivery);

                if (global.isChecked()){
                    tax_seletion.setVisibility(View.VISIBLE);
                }else {
                    tax_seletion.setVisibility(View.GONE);
                }

                select_dine_in.setChecked(true);
                select_take_away.setChecked(true);
                select_home_delivery.setChecked(true);


                final RadioButton itemtaxx = (RadioButton)rootview.findViewById(R.id.btnitem);
                itemtaxx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                        if (ischecked){
                            tax_seletion.setVisibility(View.GONE);
                        }else {
                            tax_seletion.setVisibility(View.VISIBLE);
                        }
                    }
                });


                spinneritem = (TextView) rootview.findViewById(R.id.tax);
                spinneritem.setText("0 selected");
                spinneritem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getActivity(), Taxes_Multiselection_Items.class);
                        intent.putExtra("PLAYER1NAME", "insert");
                        intent.putExtra("taxname", "");
                        startActivityForResult(intent, 1);

//                        final Dialog dialog1 = new Dialog(getActivity(), R.style.notitle);
//                        dialog1.setContentView(R.layout.expandablelist_items_categories);
//                        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                        dialog1.show();
//
//                        ImageButton btncancel = (ImageButton) dialog1.findViewById(R.id.btncancel);
//                        btncancel.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialog1.dismiss();
//                            }
//                        });
//
//                        groupList = new LinkedHashMap<Item, ArrayList<Item>>();
//
//                        ArrayList<Item> groupsList = fetchGroups();
//                        Log.i("GroupsListSize", String.valueOf(groupsList.size()));
//
//                        for (Item item : groupsList) {
//                            String[] ids = item.id.split(",");
//                            ArrayList<Item> groupMembers = new ArrayList<Item>();
//                            for (int i = 0; i < ids.length; i++) {
//                                String groupId = ids[i];
//                                Log.i("GroupsListSize", groupId);
//                                groupMembers.addAll(fetchGroupMembers(groupId));
//                            }
//
//                            item.name = item.name + "";
//                            groupList.put(item, groupMembers);
//                        }
//
//                        ExpandableListView expandableListView = (ExpandableListView) dialog1.findViewById(R.id.expandablelist);
//                        ExpandableAdapter adapter = new ExpandableAdapter(getActivity(), expandableListView, groupList);
//                        expandableListView.setAdapter(adapter);

                    }
                });

//                ImageView into = (ImageView)rootview.findViewById(R.id.closetext);
//                into.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        linearLayout.setVisibility(View.INVISIBLE);
//                        EditText one = (EditText)rootview.findViewById(R.id.editText1);
//                        one.setText("");
//                        EditText two = (EditText)rootview.findViewById(R.id.editText2);
//                        two.setText("");
//                        hideKeyboard(getContext());
//                    }
//                });

                Button btn = (Button)rootview.findViewById(R.id.btndelete);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearLayout.setVisibility(View.GONE);
                        additem.setVisibility(View.VISIBLE);
                        EditText one = (EditText)rootview.findViewById(R.id.editText1);
                        one.setText("");
                        EditText two = (EditText)rootview.findViewById(R.id.editText2);
                        two.setText("");
                        EditText three = (EditText)rootview.findViewById(R.id.editText1_HSN);
                        three.setText("");
                        search.setEnabled(true);
                        hideKeyboard(getContext());


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            donotshowKeyboard(getActivity());

                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        }
                    }
                });

                Button save = (Button)rootview.findViewById(R.id.btnsave);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
//                                Context.MODE_PRIVATE, null);
                        //saveInDBB();
                        search.setEnabled(true);

                        //linearLayout.setVisibility(View.INVISIBLE);

//                        if (select_dine_in.isChecked()){
//                            Toast.makeText(getActivity(), "selected dine-in", Toast.LENGTH_SHORT).show();
//                        }
//                        if (select_take_away.isChecked()){
//                            Toast.makeText(getActivity(), "selected take away", Toast.LENGTH_SHORT).show();
//                        }
//                        if (select_home_delivery.isChecked()){
//                            Toast.makeText(getActivity(), "selected home delivery", Toast.LENGTH_SHORT).show();
//                        }


                        columnvalue  = text.getText().toString();
                        if (text.getText().toString().contains("'")) {
                            columnvalue  = text.getText().toString().replaceAll("'", " ");
                        }

                        Cursor itemnamecheck = db.rawQuery("SELECT * FROM Taxes WHERE taxname = '"+columnvalue+"'", null);
                        if (itemnamecheck.moveToFirst()){
                            layouttaxname.setError("Tax name already in use");
                        }else {



                            if (global.isChecked()) {

                                select_dine_in1 = (CheckBox)rootview.findViewById(R.id.select_dine_in);
                                select_take_away1 = (CheckBox)rootview.findViewById(R.id.select_take_away);
                                select_home_delivery1 = (CheckBox)rootview.findViewById(R.id.select_home_delivery);

                                if (!editText.getText().toString().equals("") && !text.getText().toString().equals("")) {

                                    DownloadMusicfromInternetde_insert_global downloadMusicfromInternetde_insert = new DownloadMusicfromInternetde_insert_global();
                                    downloadMusicfromInternetde_insert.execute();

                                } else {
                                    if (text.getText().toString().equals("")) {
                                        //Toast.makeText(getActivity(),"Fill Tax name", Toast.LENGTH_SHORT).show();
                                        layouttaxname.setError("Fill Tax name");
                                    }
                                    if (editText.getText().toString().equals("")) {
                                        //Toast.makeText(getActivity(),"Fill Value", Toast.LENGTH_SHORT).show();
                                        layoutvalue.setError("Fill Tax value");
                                    } else {
                                        //Toast.makeText(getActivity(), "Fill Tax Type", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
//                                SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
//                                        Context.MODE_PRIVATE, null);

                                if (!editText.getText().toString().equals("") && !text.getText().toString().equals("")) {

                                    DownloadMusicfromInternetde_insert_item downloadMusicfromInternetde_insert = new DownloadMusicfromInternetde_insert_item();
                                    downloadMusicfromInternetde_insert.execute();

                                } else {
                                    if (text.getText().toString().equals("")) {
                                        //Toast.makeText(getActivity(),"Fill Tax name", Toast.LENGTH_SHORT).show();
                                        layouttaxname.setError("Fill Tax name");
                                    }
                                    if (editText.getText().toString().equals("")) {
                                        //Toast.makeText(getActivity(),"Fill Value", Toast.LENGTH_SHORT).show();
                                        layoutvalue.setError("Fill Tax value");
                                    } else {
                                        //Toast.makeText(getActivity(), "Fill Tax Type", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                        itemnamecheck.close();


                    }
                });

            }
        });



        return rootview;
    }


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//
//        inflater.inflate(R.menu.taxes_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.action_item:
//                frag = new DatabaseitemActivity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//                hideKeyboard(getContext());
//                break;
//
//            case R.id.action_category:
//                frag = new DatabasecategoryActivity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//                hideKeyboard(getContext());
//                break;
//
//
//            case R.id.action_modifier:
//                frag = new DatabaseModifiersActivity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//                hideKeyboard(getContext());
//                break;
//
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    public void alertas(String alerta) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme);
        AlertDialog.Builder builder = new AlertDialog.Builder(wrapper);
        builder.setIcon(R.drawable.icon);
        builder.setTitle(R.string.app_name);
        builder.setMessage(alerta);
        builder.create().show();
    }

    void saveInDBup(Integer _id) {
//        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb",
//                Context.MODE_PRIVATE, null);
//        ContentValues newValues = new ContentValues();
//        if (radioBtn1 != null) {
//            newValues.put("taxname", dialogC1_id.getText().toString());
//            newValues.put("value", dialogC2_id.getText().toString());
//            newValues.put("taxtype", radioBtn1.getText().toString());
//            String where = "_id = ?";
//            myDb.update("Taxes", newValues, where, new String[]{Integer.toString(_id)});
//            myDb.close();
//            Toast.makeText(getActivity().getBaseContext(),
//                    "Tax Saved in Data Base successfully.", Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
//        }else {
//            Toast.makeText(getActivity(), "Fill all the details", Toast.LENGTH_SHORT).show();
//        }


        dialog_columnvalue  = dialogC1_id.getText().toString();
        if (dialogC1_id.getText().toString().contains("'")) {
            dialog_columnvalue  = dialogC1_id.getText().toString().replaceAll("'", " ");
        }


//        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
//                Context.MODE_PRIVATE, null);
        ContentValues newValues = new ContentValues();
        if (!dialogC2_id.getText().toString().equals("") && !dialogC1_id.getText().toString().equals("")) {
            newValues.put("taxname", String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)");

            System.out.println("taxes name1 "+dialog_columnvalue);
            System.out.println("taxes name2 "+dialogC2_id.getText().toString());
            System.out.println("taxes name3 "+String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)");


            final RadioButton global = (RadioButton)dialog.findViewById(R.id.btnglobal);
            if (global.isChecked()){
                newValues.put("hsn_code", "");
            }else {
                newValues.put("hsn_code", dialogC1_id_hsn.getText().toString());
            }

            newValues.put("value", Float.valueOf(dialogC2_id.getText().toString()));

            newValues.put("taxtype", radioBtn1.getText().toString());
            if (dialog_select_dine_in.isChecked()){
                newValues.put("tax1", "dine_in");
            }else {
                newValues.put("tax1", "");
            }
            if (dialog_select_take_away.isChecked()){
                newValues.put("tax2", "takeaway");
            }else {
                newValues.put("tax2", "");
            }
            if (dialog_select_home_delivery.isChecked()){
                newValues.put("tax3", "homedelivery");
            }else {
                newValues.put("tax3", "");
            }
            String where = "_id = ?";
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Taxes");
            getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{Integer.toString(_id)});
            resultUri = new Uri.Builder()
                    .scheme("content")
                    .authority(StubProviderApp.AUTHORITY)
                    .path("Taxes")
                    .appendQueryParameter("operation", "update")
                    .appendQueryParameter("_id", Integer.toString(_id))
                    .build();
            getActivity().getContentResolver().notifyChange(resultUri, null);
//            myDb.update("Taxes", newValues, where, new String[]{Integer.toString(_id)});
//            myDb.close();
//            Toast.makeText(getActivity().getBaseContext(),
//                    "Tax Saved in Data Base successfully.", Toast.LENGTH_SHORT).show();

            webservicequery("update taxes set taxname='"+String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)"+"', " +
                    "value = '"+String.valueOf(Float.valueOf(dialogC2_id.getText().toString()))+"', hsn_code = '"+dialogC1_id_hsn.getText().toString()+"' where status_temp = ''");

            if (radioBtn1.getText().toString().equals("Globaltax")){
                if (dialog_select_dine_in.isChecked()) {
                    webservicequery("update Taxes set tax1 = 'dine_in' WHERE _id = '"+_id+"'");
                    db.execSQL("update Taxes set tax1 = 'dine_in' WHERE _id = '"+_id+"'");
                }else {
                    webservicequery("update Taxes set tax1 = '' WHERE _id = '"+_id+"'");
                    db.execSQL("update Taxes set tax1 = '' WHERE _id = '"+_id+"'");
                }

                if (dialog_select_take_away.isChecked()) {
                    webservicequery("update Taxes set tax2 = 'takeaway' WHERE _id = '"+_id+"'");
                    db.execSQL("update Taxes set tax2 = 'takeaway' WHERE _id = '"+_id+"'");
                }else {
                    webservicequery("update Taxes set tax2 = '' WHERE _id = '"+_id+"'");
                    db.execSQL("update Taxes set tax2 = '' WHERE _id = '"+_id+"'");
                }

                if (dialog_select_home_delivery.isChecked()) {
                    webservicequery("update Taxes set tax3 = 'homedelivery' WHERE _id = '"+_id+"'");
                    db.execSQL("update Taxes set tax3 = 'homedelivery' WHERE _id = '"+_id+"'");
                }else {
                    webservicequery("update Taxes set tax3 = '' WHERE _id = '"+_id+"'");
                    db.execSQL("update Taxes set tax3 = '' WHERE _id = '"+_id+"'");
                }

            }

        }else {
            if (dialogC1_id.getText().toString().equals("")){
                //Toast.makeText(getActivity(),"Fill Tax name", Toast.LENGTH_SHORT).show();
                layouttaxname_dialog.setError("Fill Tax name");
            }
            if (dialogC2_id.getText().toString().equals("")){
                //Toast.makeText(getActivity(),"Fill Value", Toast.LENGTH_SHORT).show();
                layoutvalue_dialog.setError("Fill Tax value");
            }
            else {
                //Toast.makeText(getActivity(), "Fill Tax Type", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public Cursor fetchCountriesByName(String inputtext) throws SQLException {

        Cursor mCursor = null;
        if (inputtext == null || inputtext.length() == 0) {
            mCursor = db.query("Taxes", new String[]{"_id", "taxname", "value", "taxtype"},
                    null, null, null, null, null);

        } else {
            mCursor = db.query(true, "Taxes", new String[]{"_id", "taxname", "value", "taxtype"},
                    "taxname" + " like" + " '%" + inputtext + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;


    }
    public void delete_byID(int _id){
        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Taxes");
        getActivity().getContentResolver().delete(contentUri,"_id"+"="+_id, null);
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("Taxes")
                .appendQueryParameter("operation", "delete")
                .appendQueryParameter("_id", ID)
                .build();
        getActivity().getContentResolver().notifyChange(resultUri, null);
//        db.delete("Taxes", "_id"+"="+_id, null);
        Toast.makeText(getActivity().getBaseContext(),
                "Tax deleted", Toast.LENGTH_SHORT).show();
    }

    public String getType(Cursor c) {
        return(c.getString(3));
    }

///////////////////////////////////////////////


    private class MyCustomAdapter extends ArrayAdapter<Country1> {

        private ArrayList<Country1> originalList;
        private ArrayList<Country1> countryList;
        private CountryFilter filter;

        private Cursor c;
        private Context context;

        private SparseBooleanArray mSelectedItemsIds;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Country1> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<Country1>();
            this.countryList.addAll(countryList);
            this.originalList = new ArrayList<Country1>();
            this.originalList.addAll(countryList);
        }

        @Override
        public Filter getFilter() {
            if (filter == null){
                filter  = new CountryFilter();
            }
            return filter;
        }


        private class ViewHolder {
            TextView code;
            TextView name;
            TextView continent;
            TextView region;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));
            if (convertView == null) {

                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.items_deletionbox, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (TextView) convertView.findViewById(R.id.name);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Country1 country = countryList.get(position);
            holder.code.setText(country.getCode());
            holder.name.setText(country.getName());
//            holder.name.setText(country);
//            holder.continent.setText(country.getContinent());
//            holder.region.setText(country.getRegion());

            return convertView;

//            ViewHolder holder = null;
//            View v = convertView;
//            if (v == null) {
//                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                v = inflater.inflate(R.layout.country_info, null);
//            }
//            holder = new ViewHolder();
//            holder.code = (TextView) v.findViewById(R.id.code);
//            holder.name = (TextView) v.findViewById(R.id.name);
//
//            //this.code.moveToPosition(pos);
//            String firstName = this.c.getString(this.c.getColumnIndex("name"));
//            int price = c.getInt(c.getColumnIndex("price"));
//
//            TextView fname = (TextView) v.findViewById(R.id.name);
//            fname.setText(firstName);
//
//            TextView lname = (TextView) v.findViewById(R.id.code);
//            lname.setText(String.valueOf(price));
//
////            TextView title = (TextView) v.findViewById(R.id.inventory);
////            title.setText(String.valueOf(quantity));
//            return (v);

        }

//        public  SparseBooleanArray getSelectedIds() {
//            return mSelectedItemsIds;
//        }

        private class CountryFilter extends Filter
        {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();
                if(constraint != null && constraint.toString().length() > 0)
                {
                    ArrayList<Country1> filteredItems = new ArrayList<Country1>();

                    for(int i = 0, l = originalList.size(); i < l; i++)
                    {
                        Country1 country = originalList.get(i);
                        if(country.toString().toLowerCase().contains(constraint))
                            filteredItems.add(country);
                    }
                    result.count = filteredItems.size();
                    result.values = filteredItems;
                }
                else
                {
                    synchronized(this)
                    {
                        result.values = originalList;
                        result.count = originalList.size();
                    }
                }
                return result;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                countryList = (ArrayList<Country1>)results.values;
                notifyDataSetChanged();
                clear();
                for(int i = 0, l = countryList.size(); i < l; i++)
                    add(countryList.get(i));
                notifyDataSetInvalidated();
            }
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

    public static void showKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static void donotshowKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    public void displayKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(getActivity(), "cloedddddddd", Toast.LENGTH_SHORT).show();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO  Auto-generated method stub
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mode.invalidate();
                // TODO  Auto-generated method stub
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // TODO  Auto-generated method stub
                //mode.getMenuInflater().inflate(R.menu.multiple_delete, menu);
                return true;

            }
//            @Override
//            public boolean onActionItemClicked(final ActionMode mode,
//                                               MenuItem item) {
//
//            }
        });
        //mode.invalidate();
        super.onDestroy();
    }

    private ArrayList<Item> fetchGroups() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //coding 29/1/2018......
//        Bundle extras = getIntent().getExtras();
        String hi = "";

        ArrayList<Item> groupList = new ArrayList<Item>();

//        Cursor cursor = db.rawQuery("SELECT * FROM Hotel1", null);//removed after hotel1
        Cursor cursor = db.rawQuery("SELECT * FROM Hotel", null);
        if (cursor.moveToFirst()) {
            do {
                hi = cursor.getString(1);

                if (hi.toString().equals("All") || hi.toString().equals("Favourites")){

                }else {

                    checking = new ArrayList<String>();

//                  i2 = i2 + 1;
//                  categories.add("" + hi);
                    Item item = new Item();

                    item.id = groupName = "" + hi;
                    item.name = groupName;
                    groupList.add(item);

                    checking.add(groupName);

//                  st = " " + i2;

                    Collections.sort(groupList, new Comparator<Item>() {

                        public int compare(Item item1, Item item2) {

                            return item2.name.compareTo(item1.name) < 0
                                    ? 0 : -1;
                        }
                    });

                }
            }while (cursor.moveToNext());
        }
        cursor.close();


        return groupList;

    }


    private ArrayList<Item> fetchGroupMembers(String groupId) {

        StrictMode.ThreadPolicy policy1 = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy1);
        String hi1 = "";

//        String[] toAdd = new String[0];

        ArrayList<Item> groupMembers = new ArrayList<Item>();

        Log.i("GroupsListSizehi ", "");

        Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE category = '"+groupId+"'", null);
        if (cursor.moveToFirst()){
            do {
                hi1 = cursor.getString(1);

                Log.i("GroupsListSizehi2 ", "");

                for (int i = 0; i < checking.size(); i++) {
                    String s = checking.get(i);

                    Log.i("GroupsListSizehi3 ", hi1);

//                    if (hi1.contains(s)) {
                    hi1 = hi1.replace(s, "");

                    Log.i("GroupsListSizehi4 ", hi1);


                    Item item = new Item();
                    String groupName;
                    item.id = groupName = "" + hi1;
                    item.name = groupName;

                    groupMembers.add(item);
//                    }
                }

            }while (cursor.moveToNext());
        }
        cursor.close();


//        System.out.println("listaaa aaa" + yyyy);
//
//        Object[] set = yyyy.toArray();
//        for (Object s1 : set) {
//            if (yyyy.indexOf(s1) != yyyy.lastIndexOf(s1)) {
//                yyyy.remove(yyyy.lastIndexOf(s1));
//            }
//        }
//
//        System.out.println("listbbbbbbbbbbb" + yyyy);

        return groupMembers;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(Add_manage_ingredient_Activity.this, "1", Toast.LENGTH_LONG).show();
        if (requestCode == 1) {
//            Toast.makeText(Add_manage_ingredient_Activity.this, "2", Toast.LENGTH_LONG).show();
            if(resultCode == RESULT_OK) {
//                Toast.makeText(Add_manage_ingredient_Activity.this, "3", Toast.LENGTH_LONG).show();
                String strEditText = data.getStringExtra("editTextValue");
                String player1name = data.getStringExtra("player1name");
//                Toast.makeText(Add_manage_ingredient_Activity.this, "2q "+strEditText, Toast.LENGTH_LONG).show();
                if (player1name.equals("insert")) {
                    spinneritem.setText(strEditText+" selected");
                }else {
                    dialog_spinneritem.setText(strEditText+" selected");
                }
            }
        }
    }

    class DownloadMusicfromInternetde_insert_global extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            ContentValues newValues = new ContentValues();
            newValues.put("taxname", String.valueOf(columnvalue)+"("+editText.getText().toString()+"%)");
            newValues.put("hsn_code", editText1_HSN.getText().toString());
            newValues.put("value", Float.valueOf(editText.getText().toString()));

            newValues.put("taxtype", "Globaltax");
//                                    if (select_dine_in.isChecked()){
//                                        newValues.put("tax1", "dine_in");
//                                    }
//                                    if (select_take_away.isChecked()){
//                                        newValues.put("tax2", "takeaway");
//                                    }
//                                    if (select_home_delivery.isChecked()){
//                                        newValues.put("tax3", "homedelivery");
//                                    }
            if (select_dine_in1.isChecked()){
                newValues.put("tax1", "dine_in");
//                                        Toast.makeText(getActivity(), "selected globaltax dine-in", Toast.LENGTH_SHORT).show();
            }else {
                newValues.put("tax1", "");
            }
            if (select_take_away1.isChecked()){
                newValues.put("tax2", "takeaway");
//                                        Toast.makeText(getActivity(), "selected globaltax take away", Toast.LENGTH_SHORT).show();
            }else {
                newValues.put("tax2", "");
            }
            if (select_home_delivery1.isChecked()){
                newValues.put("tax3", "homedelivery");
//                                        Toast.makeText(getActivity(), "selected globaltax home delivery", Toast.LENGTH_SHORT).show();
            }else {
                newValues.put("tax3", "");
            }
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Taxes");
            resultUri = getActivity().getContentResolver().insert(contentUri, newValues);
            getActivity().getContentResolver().notifyChange(resultUri, null);
//            db.insert("Taxes", null, newValues);

            Cursor cursor_new = db1.rawQuery("SELECT * FROM Customerdetails", null);
            if (cursor_new.moveToFirst()){
                do {
                    String last = cursor_new.getString(53);
                    TextView hi = new TextView(getActivity());
                    hi.setText(last);
                    if (hi.getText().toString().equals("All")){
                        int i = 1;
                        Cursor c = db.rawQuery("SELECT * FROM Taxes WHERE taxtype = 'Globaltax'", null);
                        if (c.moveToFirst()) {
                            do {
                                String tn = c.getString(1);
                                String tn1 = c.getString(2);
                                ContentValues contentValues1 = new ContentValues();
                                contentValues1.put("tax"+i, tn);
                                contentValues1.put("tax"+i+"_value", tn1);
                                String wherecu = "tax_selection = '" + last + "'";
                                db1.update("Customerdetails", contentValues1, wherecu, new String[]{});
                                i++;
                            }while (c.moveToNext());
                        }
                        c.close();
                    }else {
                        if (hi.getText().toString().equals("partial")){

                        }
                    }
                }while (cursor_new.moveToNext());
            }
            cursor_new.close();

            Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE status_temp = 'yes'", null);
            if (cursor.moveToFirst()){
                do {
                    String id = cursor.getString(0);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("status_temp", "");
                    contentValues.put("status_perm", "");
                    String where = " _id ='" + id + "'";
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                    getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Items")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id",id)
                            .build();
                    getActivity().getContentResolver().notifyChange(resultUri, null);
//                    db.update("Items", contentValues, where, new String[]{});


                    String where1_v1 = "docid = '" + id + "'";
                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});


                }while (cursor.moveToNext());
            }
            cursor.close();

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
//            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Integer file_url) {
            progressbar.setVisibility(View.GONE);
            Toast.makeText(getActivity().getBaseContext(),
                    "Tax added", Toast.LENGTH_SHORT).show();
            linearLayout.setVisibility(View.GONE);
            additem.setVisibility(View.VISIBLE);
            hideKeyboard(getContext());

            if (!spinner.getSelectedItem().toString().equals("All")) {
                //Toast.makeText(getActivity(), " " + spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                countryList = new ArrayList<Country1>();
                try {
                    db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor allrows = db.rawQuery("SELECT * FROM Taxes WHERE taxtype = '" + spinner.getSelectedItem().toString() + "'", null);
                    System.out.println("COUNT : " + allrows.getCount());


                    //Country1 country = new Country1(name, name, name, name);

                    if (allrows.moveToFirst()) {
                        do {
                            String ID = allrows.getString(0);
                            String NAme = allrows.getString(1);
                            String PlaCe = allrows.getString(2);
                            if (NAme.toString().equals("None")){

                            }else {
                                Country1 NAME = new Country1(ID, NAme);
                                //Country1 PLACE = new Country1(PlaCe, PlaCe, PlaCe, PlaCe);
                                countryList.add(NAME);
                                //countryList.add(PLACE);
                            }
                        } while (allrows.moveToNext());
                    }
                    allrows.close();
                    //db.close();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error encountered.",
                            Toast.LENGTH_LONG);
                }
            }
            if (spinner.getSelectedItem().toString().equals("All")) {
                //Toast.makeText(getActivity(), " " + spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                countryList = new ArrayList<Country1>();
                try {
                    db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor allrows = db.rawQuery("SELECT * FROM Taxes ", null);
                    System.out.println("COUNT : " + allrows.getCount());


                    //Country1 country = new Country1(name, name, name, name);

                    if (allrows.moveToFirst()) {
                        do {
                            String ID = allrows.getString(0);
                            String NAme = allrows.getString(1);
                            String PlaCe = allrows.getString(2);
                            if (NAme.toString().equals("None")){

                            }else {
                                Country1 NAME = new Country1(ID, NAme);
                                //Country1 PLACE = new Country1(PlaCe, PlaCe, PlaCe, PlaCe);
                                countryList.add(NAME);
                                //countryList.add(PLACE);
                            }
                        } while (allrows.moveToNext());
                    }
                    allrows.close();
                    //db.close();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error encountered.",
                            Toast.LENGTH_LONG);
                }
            }


            dataAdapter = new MyCustomAdapter(getActivity(),
                    R.layout.items_deletionbox, countryList);
            final ListView listView = (ListView) rootview.findViewById(R.id.listView);
            // Assign adapter to ListView
            listView.setAdapter(dataAdapter);

        }
    }


    class DownloadMusicfromInternetde_insert_item extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {


            ContentValues newValues = new ContentValues();
            newValues.put("taxname", String.valueOf(columnvalue)+"("+editText.getText().toString()+"%)");
            newValues.put("hsn_code", editText1_HSN.getText().toString());
            newValues.put("value", Float.valueOf(editText.getText().toString()));

            newValues.put("taxtype", "Itemtax");

            newValues.put("tax1", "dine_in");
            newValues.put("tax2", "takeaway");
            newValues.put("tax3", "homedelivery");



            final String webserviceQuery1="INSERT INTO `taxes`(`taxname`, `value`, `taxtype`, `tax1`, `tax2`, `tax3`, `hsn_code`) " +
                    "VALUES ('"+String.valueOf(columnvalue)+"("+editText.getText().toString()+"%)"+"',"+Float.valueOf(editText.getText().toString())+",'Itemtax','dine_in','takeaway','homedelivery','"+ editText1_HSN.getText().toString()+"')";
////
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Taxes");
//            resultUri = getActivity().getContentResolver().insert(contentUri, newValues);
//            getActivity().getContentResolver().notifyChange(resultUri, null);
//
            db.execSQL(webserviceQuery1);

            webservicequery(webserviceQuery1);
            tax_insert_server(String.valueOf(columnvalue)+"("+editText.getText().toString()+"%)", String.valueOf(Float.valueOf(editText.getText().toString())));

//
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Taxes");
//            resultUri = getActivity().getContentResolver().insert(contentUri, newValues);
//            getActivity().getContentResolver().notifyChange(resultUri, null);

//            db.insert("Taxes", null, newValues);

            Cursor taxeitem_all = db.rawQuery("SELECT * FROM Items", null);
            if (taxeitem_all.moveToFirst()){
                do {
                    String id = taxeitem_all.getString(0);
                    String itemname_t=taxeitem_all.getString(1);
                    String one = taxeitem_all.getString(5);
                    String two = taxeitem_all.getString(28);
                    String three = taxeitem_all.getString(30);
                    String four = taxeitem_all.getString(32);
                    String five = taxeitem_all.getString(34);
                    String st = taxeitem_all.getString(37);

                    TextView tv = new TextView(getActivity());
                    tv.setText(one);
                    TextView tv2 = new TextView(getActivity());
                    tv2.setText(two);
                    TextView tv3 = new TextView(getActivity());
                    tv3.setText(three);
                    TextView tv4 = new TextView(getActivity());
                    tv4.setText(four);
                    TextView tv5 = new TextView(getActivity());
                    tv5.setText(five);

                    TextView stt = new TextView(getActivity());
                    stt.setText(st);

                    if (stt.getText().toString().equals("yes")) {
                        if (tv.getText().toString().equals(String.valueOf(columnvalue)+"("+editText.getText().toString()+"%)") ||
                                tv2.getText().toString().equals(String.valueOf(columnvalue)+"("+editText.getText().toString()+"%)") ||
                                tv3.getText().toString().equals(String.valueOf(columnvalue)+"("+editText.getText().toString()+"%)") ||
                                tv4.getText().toString().equals(String.valueOf(columnvalue)+"("+editText.getText().toString()+"%)") ||
                                tv5.getText().toString().equals(String.valueOf(columnvalue)+"("+editText.getText().toString()+"%)")){

                        }else {
//                        Toast.makeText(getActivity(), "id "+id, Toast.LENGTH_LONG).show();
                            if (tv.getText().toString().equals("") || tv.getText().toString().equals("None")) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("itemtax", String.valueOf(columnvalue) + "(" + editText.getText().toString() + "%)");
                                contentValues.put("tax_value", Float.valueOf(editText.getText().toString()));
                                String where = "_id = '" + id + "' ";

//                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                                getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                                resultUri = new Uri.Builder()
//                                        .scheme("content")
//                                        .authority(StubProviderApp.AUTHORITY)
//                                        .path("Items")
//                                        .appendQueryParameter("operation", "update")
//                                        .appendQueryParameter("_id",id)
//                                        .build();
//                                getActivity().getContentResolver().notifyChange(resultUri, null);
//                                String where1_v1 = "docid = '" + id + "'";
//                                db.update("Items_Virtual", contentValues, where1_v1, new String[]{});



                                final String webserviceQuery ="update Items set itemtax = '"+String.valueOf(columnvalue) + "(" + editText.getText().toString() + "%)"+"'"+",tax_value ="+Float.valueOf(editText.getText().toString())+" where itemname='"+itemname_t+"'";
                                //db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                                db.execSQL(webserviceQuery);
//                                webservicequery(webserviceQuery);




//                            Toast.makeText(getActivity(), "itemtax "+String.valueOf(columnvalue), Toast.LENGTH_LONG).show();
                            } else {
                                if (tv2.getText().toString().equals("") || tv2.getText().toString().equals("None")) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("itemtax2", String.valueOf(columnvalue) + "(" + editText.getText().toString() + "%)");
                                    contentValues.put("tax_value2", Float.valueOf(editText.getText().toString()));
                                    String where = "_id = '" + id + "' ";

//                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                                    getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                                    resultUri = new Uri.Builder()
//                                            .scheme("content")
//                                            .authority(StubProviderApp.AUTHORITY)
//                                            .path("Items")
//                                            .appendQueryParameter("operation", "update")
//                                            .appendQueryParameter("_id",id)
//                                            .build();
//                                    getActivity().getContentResolver().notifyChange(resultUri, null);
//
//                                    String where1_v1 = "docid = '" + id + "'";
//                                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});


                                    final String webserviceQuery ="update Items set itemtax2 = '"+String.valueOf(columnvalue) + "(" + editText.getText().toString() + "%)"+"'"+",tax_value2 ="+Float.valueOf(editText.getText().toString())+" where itemname='"+itemname_t+"'";
                                    db.execSQL(webserviceQuery);

//                                    webservicequery(webserviceQuery);


                                    //                                    db.update("Items", contentValues, where, new String[]{});
//                                Toast.makeText(getActivity(), "itemtax2 "+String.valueOf(columnvalue), Toast.LENGTH_LONG).show();
                                } else {
                                    if (tv3.getText().toString().equals("") || tv3.getText().toString().equals("None")) {
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("itemtax3", String.valueOf(columnvalue) + "(" + editText.getText().toString() + "%)");
                                        contentValues.put("tax_value3", Float.valueOf(editText.getText().toString()));
                                        String where = "_id = '" + id + "' ";

                                        db.update("Items", contentValues, where, new String[]{});

                                        String where1_v1 = "docid = '" + id + "'";
                                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});


//                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                                        getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                                        resultUri = new Uri.Builder()
//                                                .scheme("content")
//                                                .authority(StubProviderApp.AUTHORITY)
//                                                .path("Items")
//                                                .appendQueryParameter("operation", "update")
//                                                .appendQueryParameter("_id",id)
//                                                .build();
//                                        getActivity().getContentResolver().notifyChange(resultUri, null);
//                                        db.update("Items", contentValues, where, new String[]{});
//                                    Toast.makeText(getActivity(), "itemtax3 "+String.valueOf(columnvalue), Toast.LENGTH_LONG).show();
                                    } else {
                                        if (tv4.getText().toString().equals("") || tv4.getText().toString().equals("None")) {
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("itemtax4", String.valueOf(columnvalue) + "(" + editText.getText().toString() + "%)");
                                            contentValues.put("tax_value4", Float.valueOf(editText.getText().toString()));
                                            String where = "_id = '" + id + "' ";

                                            db.update("Items", contentValues, where, new String[]{});

                                            String where1_v1 = "docid = '" + id + "'";
                                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});


//                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                                            getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                                            resultUri = new Uri.Builder()
//                                                    .scheme("content")
//                                                    .authority(StubProviderApp.AUTHORITY)
//                                                    .path("Items")
//                                                    .appendQueryParameter("operation", "update")
//                                                    .appendQueryParameter("_id",id)
//                                                    .build();
//                                            getActivity().getContentResolver().notifyChange(resultUri, null);
//                                            db.update("Items", contentValues, where, new String[]{});
//                                        Toast.makeText(getActivity(), "itemtax4 "+String.valueOf(columnvalue), Toast.LENGTH_LONG).show();
                                        } else {
                                            if (tv5.getText().toString().equals("") || tv5.getText().toString().equals("None")) {
                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("itemtax5", String.valueOf(columnvalue) + "(" + editText.getText().toString() + "%)");
                                                contentValues.put("tax_value5", Float.valueOf(editText.getText().toString()));
                                                String where = "_id = '" + id + "' ";
                                                //  db.update("Items", contentValues, where, new String[]{});

                                                db.update("Items", contentValues, where, new String[]{});

                                                String where1_v1 = "docid = '" + id + "'";
                                                db.update("Items_Virtual", contentValues, where1_v1, new String[]{});



//                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                                                getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                                                resultUri = new Uri.Builder()
//                                                        .scheme("content")
//                                                        .authority(StubProviderApp.AUTHORITY)
//                                                        .path("Items")
//                                                        .appendQueryParameter("operation", "update")
//                                                        .appendQueryParameter("_id",id)
//                                                        .build();
//                                                getActivity().getContentResolver().notifyChange(resultUri, null);
//                                                db.update("Items", contentValues, where, new String[]{});
//                                            Toast.makeText(getActivity(), "itemtax5 "+String.valueOf(columnvalue), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }else {
                        if (tv.getText().toString().equals(String.valueOf(columnvalue)+"("+editText.getText().toString()+"%)")){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("itemtax", "");
                            contentValues.put("tax_value", "");
                            String where = "_id = '" + id + "' ";

//                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                            getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                            resultUri = new Uri.Builder()
//                                    .scheme("content")
//                                    .authority(StubProviderApp.AUTHORITY)
//                                    .path("Items")
//                                    .appendQueryParameter("operation", "update")
//                                    .appendQueryParameter("_id",id)
//                                    .build();
//                            getActivity().getContentResolver().notifyChange(resultUri, null);
                            db.update("Items", contentValues, where, new String[]{});
                        }
                        if (tv2.getText().toString().equals(String.valueOf(columnvalue)+"("+editText.getText().toString()+"%)")){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("itemtax2", "");
                            contentValues.put("tax_value2", "");
                            String where = "_id = '" + id + "' ";
                            db.update("Items", contentValues, where, new String[]{});

                            String where1_v1 = "docid = '" + id + "'";
                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                        }
                        if (tv3.getText().toString().equals(String.valueOf(columnvalue)+"("+editText.getText().toString()+"%)")){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("itemtax3", "");
                            contentValues.put("tax_value3", "");
                            String where = "_id = '" + id + "' ";

//                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                            getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                            resultUri = new Uri.Builder()
//                                    .scheme("content")
//                                    .authority(StubProviderApp.AUTHORITY)
//                                    .path("Items")
//                                    .appendQueryParameter("operation", "update")
//                                    .appendQueryParameter("_id",id)
//                                    .build();
//                            getActivity().getContentResolver().notifyChange(resultUri, null);
                            db.update("Items", contentValues, where, new String[]{});

                            String where1_v1 = "docid = '" + id + "'";
                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                        }
                        if (tv4.getText().toString().equals(String.valueOf(columnvalue)+"("+editText.getText().toString()+"%)")){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("itemtax4", "");
                            contentValues.put("tax_value4", "");
                            String where = "_id = '" + id + "' ";
                            db.update("Items", contentValues, where, new String[]{});

//                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                            getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                            resultUri = new Uri.Builder()
//                                    .scheme("content")
//                                    .authority(StubProviderApp.AUTHORITY)
//                                    .path("Items")
//                                    .appendQueryParameter("operation", "update")
//                                    .appendQueryParameter("_id",id)
//                                    .build();
//                            getActivity().getContentResolver().notifyChange(resultUri, null);
//                            db.update("Items", contentValues, where, new String[]{});
                            String where1_v1 = "docid = '" + id + "'";
                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});


                        }
                        if (tv5.getText().toString().equals(String.valueOf(columnvalue)+"("+editText.getText().toString()+"%)")){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("itemtax5", "");
                            contentValues.put("tax_value5", "");
                            String where = "_id = '" + id + "' ";
                            db.update("Items", contentValues, where, new String[]{});

                            String where1_v1 = "docid = '" + id + "'";
                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

//                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                            getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                            resultUri = new Uri.Builder()
//                                    .scheme("content")
//                                    .authority(StubProviderApp.AUTHORITY)
//                                    .path("Items")
//                                    .appendQueryParameter("operation", "update")
//                                    .appendQueryParameter("_id",id)
//                                    .build();
//                            getActivity().getContentResolver().notifyChange(resultUri, null);
                            db.update("Items", contentValues, where, new String[]{});
                        }
                    }
                }while (taxeitem_all.moveToNext());
            }
            taxeitem_all.close();

            Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
            if (cursor.moveToFirst()){
                do {
                    String id = cursor.getString(0);
//                    String temp = cursor.getString(36);
//
//                    TextView tv = new TextView(getActivity());
//                    tv.setText(temp);

//                    if (tv.getText().toString().equals("yes")) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("status_temp", "");
                    contentValues.put("status_perm", "");
                    String where = " _id ='" + id + "'";
                    db.update("Items", contentValues, where, new String[]{});

                    String where1_v1 = "docid = '" + id + "'";
                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

//                    }
                }while (cursor.moveToNext());
            }
            cursor.close();

            webservicequery("UPDATE Items set status_temp = '' , status_perm = '' WHERE status_temp = 'yes'");

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
//            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Integer file_url) {
            progressbar.setVisibility(View.GONE);

            Toast.makeText(getActivity().getBaseContext(),
                    "Tax added", Toast.LENGTH_SHORT).show();
            linearLayout.setVisibility(View.GONE);
            additem.setVisibility(View.VISIBLE);
            hideKeyboard(getContext());

            if (!spinner.getSelectedItem().toString().equals("All")) {
                //Toast.makeText(getActivity(), " " + spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                countryList = new ArrayList<Country1>();
                try {
                    db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor allrows = db.rawQuery("SELECT * FROM Taxes WHERE taxtype = '" + spinner.getSelectedItem().toString() + "'", null);
                    System.out.println("COUNT : " + allrows.getCount());


                    //Country1 country = new Country1(name, name, name, name);

                    if (allrows.moveToFirst()) {
                        do {
                            String ID = allrows.getString(0);
                            String NAme = allrows.getString(1);
                            String PlaCe = allrows.getString(2);
                            if (NAme.toString().equals("None")){

                            }else {
                                Country1 NAME = new Country1(ID, NAme);
                                //Country1 PLACE = new Country1(PlaCe, PlaCe, PlaCe, PlaCe);
                                countryList.add(NAME);
                                //countryList.add(PLACE);
                            }
                        } while (allrows.moveToNext());
                    }
                    allrows.close();
                    //db.close();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error encountered.",
                            Toast.LENGTH_LONG);
                }
            }
            if (spinner.getSelectedItem().toString().equals("All")) {
                //Toast.makeText(getActivity(), " " + spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                countryList = new ArrayList<Country1>();
                try {
                    db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor allrows = db.rawQuery("SELECT * FROM Taxes ", null);
                    System.out.println("COUNT : " + allrows.getCount());


                    //Country1 country = new Country1(name, name, name, name);

                    if (allrows.moveToFirst()) {
                        do {
                            String ID = allrows.getString(0);
                            String NAme = allrows.getString(1);
                            String PlaCe = allrows.getString(2);
                            if (NAme.toString().equals("None")){

                            }else {
                                Country1 NAME = new Country1(ID, NAme);
                                //Country1 PLACE = new Country1(PlaCe, PlaCe, PlaCe, PlaCe);
                                countryList.add(NAME);
                                //countryList.add(PLACE);
                            }
                        } while (allrows.moveToNext());
                    }
                    allrows.close();
                    //db.close();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error encountered.",
                            Toast.LENGTH_LONG);
                }
            }


            dataAdapter = new MyCustomAdapter(getActivity(),
                    R.layout.items_deletionbox, countryList);
            final ListView listView = (ListView) rootview.findViewById(R.id.listView);
            // Assign adapter to ListView
            listView.setAdapter(dataAdapter);

        }
    }

    class DownloadMusicfromInternetde_update_global extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Cursor taxeitem = db.rawQuery("SELECT * FROM Items WHERE itemtax = '"+NAme+"'", null);
            if (taxeitem.moveToFirst()){
                do {
                    String five = taxeitem.getString(0);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("itemtax", String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)");
                    contentValues.put("tax_value", dialogC2_id.getText().toString());
                    String where = "_id = '" + five + "' ";

                    String where1_v1 = "docid = '" + five + "'";
                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                    getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Items")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id",five)
                            .build();
                    getActivity().getContentResolver().notifyChange(resultUri, null);
//                    db.update("Items", contentValues, where, new String[]{});
                }while (taxeitem.moveToNext());
            }
            taxeitem.close();

            Cursor taxeitem2 = db.rawQuery("SELECT * FROM Items WHERE itemtax2 = '"+NAme+"'", null);
            if (taxeitem2.moveToFirst()){
                do {
                    String five = taxeitem2.getString(0);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("itemtax2", String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)");
                    contentValues.put("tax_value2", dialogC2_id.getText().toString());
                    String where = "_id = '" + five + "' ";

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                    getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Items")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id",five)
                            .build();
                    getActivity().getContentResolver().notifyChange(resultUri, null);
//                    db.update("Items", contentValues, where, new String[]{});

                    String where1_v1 = "docid = '" + five + "'";
                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});


                }while (taxeitem2.moveToNext());
            }
            taxeitem2.close();

            Cursor taxeitem3 = db.rawQuery("SELECT * FROM Items WHERE itemtax3 = '"+NAme+"'", null);
            if (taxeitem3.moveToFirst()){
                do {
                    String five = taxeitem3.getString(0);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("itemtax3", String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)");
                    contentValues.put("tax_value3", dialogC2_id.getText().toString());
                    String where = "_id = '" + five + "' ";

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                    getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Items")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id",five)
                            .build();
                    getActivity().getContentResolver().notifyChange(resultUri, null);
//                    db.update("Items", contentValues, where, new String[]{});

                    String where1_v1 = "docid = '" + five + "'";
                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});


                }while (taxeitem3.moveToNext());
            }
            taxeitem3.close();

            Cursor taxeitem4 = db.rawQuery("SELECT * FROM Items WHERE itemtax4 = '"+NAme+"'", null);
            if (taxeitem4.moveToFirst()){
                do {
                    String five = taxeitem4.getString(0);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("itemtax4", String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)");
                    contentValues.put("tax_value4", dialogC2_id.getText().toString());
                    String where = "_id = '" + five + "' ";
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                    getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Items")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id",five)
                            .build();
                    getActivity().getContentResolver().notifyChange(resultUri, null);

                    String where1_v1 = "docid = '" + five + "'";
                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                    db.update("Items", contentValues, where, new String[]{});

                    //   String where1_v1 = "docid = '" + five + "'";
                    //  db.update("Items_Virtual", contentValues, where1_v1, new String[]{});


                }while (taxeitem4.moveToNext());
            }
            taxeitem4.close();

            Cursor taxeitem5 = db.rawQuery("SELECT * FROM Items WHERE itemtax5 = '"+NAme+"'", null);
            if (taxeitem5.moveToFirst()){
                do {
                    String five = taxeitem5.getString(0);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("itemtax5", String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)");
                    contentValues.put("tax_value5", dialogC2_id.getText().toString());
                    String where = "_id = '" + five + "' ";

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                    getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Items")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id",five)
                            .build();
                    getActivity().getContentResolver().notifyChange(resultUri, null);

                    // db.update("Items", contentValues, where, new String[]{});

                    String where1_v1 = "docid = '" + five + "'";
                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});



                }while (taxeitem5.moveToNext());
            }
            taxeitem5.close();

            Cursor taxeitem_all = db.rawQuery("SELECT * FROM Items", null);
            if (taxeitem_all.moveToFirst()){
                do {
                    String id = taxeitem_all.getString(0);
                    String one = taxeitem_all.getString(5);
                    String two = taxeitem_all.getString(28);
                    String three = taxeitem_all.getString(30);
                    String four = taxeitem_all.getString(32);
                    String five = taxeitem_all.getString(34);
                    String st = taxeitem_all.getString(37);

                    TextView tv = new TextView(getActivity());
                    tv.setText(one);
                    TextView tv2 = new TextView(getActivity());
                    tv2.setText(two);
                    TextView tv3 = new TextView(getActivity());
                    tv3.setText(three);
                    TextView tv4 = new TextView(getActivity());
                    tv4.setText(four);
                    TextView tv5 = new TextView(getActivity());
                    tv5.setText(five);

                    TextView stt = new TextView(getActivity());
                    stt.setText(st);

                    if (stt.getText().toString().equals("yes")) {
                        if (tv.getText().toString().equals(String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)") ||
                                tv2.getText().toString().equals(String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)") ||
                                tv3.getText().toString().equals(String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)") ||
                                tv4.getText().toString().equals(String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)") ||
                                tv5.getText().toString().equals(String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)")){

                        }else {
//                        Toast.makeText(getActivity(), "id "+id, Toast.LENGTH_LONG).show();
                            if (tv.getText().toString().equals("") || tv.getText().toString().equals("None")) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("itemtax", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                                contentValues.put("tax_value", dialogC2_id.getText().toString());
                                String where = "_id = '" + id + "' ";
                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProviderApp.AUTHORITY)
                                        .path("Items")
                                        .appendQueryParameter("operation", "update")
                                        .appendQueryParameter("_id",id)
                                        .build();
                                getActivity().getContentResolver().notifyChange(resultUri, null);

                                String where1_v1 = "docid = '" + id + "'";
                                db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

//                            Toast.makeText(getActivity(), "itemtax "+String.valueOf(dialog_columnvalue), Toast.LENGTH_LONG).show();
                            } else {
                                if (tv2.getText().toString().equals("") || tv2.getText().toString().equals("None")) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("itemtax2", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                                    contentValues.put("tax_value2", dialogC2_id.getText().toString());
                                    String where = "_id = '" + id + "' ";

                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                    getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                                    resultUri = new Uri.Builder()
                                            .scheme("content")
                                            .authority(StubProviderApp.AUTHORITY)
                                            .path("Items")
                                            .appendQueryParameter("operation", "update")
                                            .appendQueryParameter("_id",id)
                                            .build();
                                    getActivity().getContentResolver().notifyChange(resultUri, null);
                                    String where1_v1 = "docid = '" + id + "'";
                                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

//                                Toast.makeText(getActivity(), "itemtax2 "+String.valueOf(dialog_columnvalue), Toast.LENGTH_LONG).show();
                                } else {
                                    if (tv3.getText().toString().equals("") || tv3.getText().toString().equals("None")) {
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("itemtax3", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                                        contentValues.put("tax_value3", dialogC2_id.getText().toString());
                                        String where = "_id = '" + id + "' ";
                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                        getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProviderApp.AUTHORITY)
                                                .path("Items")
                                                .appendQueryParameter("operation", "update")
                                                .appendQueryParameter("_id",id)
                                                .build();
                                        getActivity().getContentResolver().notifyChange(resultUri, null);

                                        String where1_v1 = "docid = '" + id + "'";
                                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

//                                    Toast.makeText(getActivity(), "itemtax3 "+String.valueOf(dialog_columnvalue), Toast.LENGTH_LONG).show();
                                    } else {
                                        if (tv4.getText().toString().equals("") || tv4.getText().toString().equals("None")) {
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("itemtax4", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                                            contentValues.put("tax_value4", dialogC2_id.getText().toString());
                                            String where = "_id = '" + id + "' ";
                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                            getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                                            resultUri = new Uri.Builder()
                                                    .scheme("content")
                                                    .authority(StubProviderApp.AUTHORITY)
                                                    .path("Items")
                                                    .appendQueryParameter("operation", "update")
                                                    .appendQueryParameter("_id",id)
                                                    .build();
                                            getActivity().getContentResolver().notifyChange(resultUri, null);

                                            String where1_v1 = "docid = '" + id + "'";
                                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

//                                        Toast.makeText(getActivity(), "itemtax4 "+String.valueOf(dialog_columnvalue), Toast.LENGTH_LONG).show();
                                        } else {
                                            if (tv5.getText().toString().equals("") || tv5.getText().toString().equals("None")) {
                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("itemtax5", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                                                contentValues.put("tax_value5", dialogC2_id.getText().toString());
                                                String where = "_id = '" + id + "' ";

                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                                                resultUri = new Uri.Builder()
                                                        .scheme("content")
                                                        .authority(StubProviderApp.AUTHORITY)
                                                        .path("Items")
                                                        .appendQueryParameter("operation", "update")
                                                        .appendQueryParameter("_id",id)
                                                        .build();
                                                getActivity().getContentResolver().notifyChange(resultUri, null);
                                                String where1_v1 = "docid = '" + id + "'";
                                                db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

//                                            Toast.makeText(getActivity(), "itemtax5 "+String.valueOf(dialog_columnvalue), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }else {
                        if (tv.getText().toString().equals(String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)")){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("itemtax", "");
                            contentValues.put("tax_value", "");
                            String where = "_id = '" + id + "' ";
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                            getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Items")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id",id)
                                    .build();
                            getActivity().getContentResolver().notifyChange(resultUri, null);

                            String where1_v1 = "docid = '" + id + "'";
                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                        }
                        if (tv2.getText().toString().equals(String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)")){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("itemtax2", "");
                            contentValues.put("tax_value2", "");
                            String where = "_id = '" + id + "' ";
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                            getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Items")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id",id)
                                    .build();
                            getActivity().getContentResolver().notifyChange(resultUri, null);

                            String where1_v1 = "docid = '" + id + "'";
                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                        }
                        if (tv3.getText().toString().equals(String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)")){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("itemtax3", "");
                            contentValues.put("tax_value3", "");
                            String where = "_id = '" + id + "' ";
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                            getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Items")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id",id)
                                    .build();
                            getActivity().getContentResolver().notifyChange(resultUri, null);

                            String where1_v1 = "docid = '" + id + "'";
                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                        }
                        if (tv4.getText().toString().equals(String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)")){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("itemtax4", "");
                            contentValues.put("tax_value4", "");
                            String where = "_id = '" + id + "' ";
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                            getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Items")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id",id)
                                    .build();
                            getActivity().getContentResolver().notifyChange(resultUri, null);

                            String where1_v1 = "docid = '" + id + "'";
                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                        }
                        if (tv5.getText().toString().equals(String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)")){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("itemtax5", "");
                            contentValues.put("tax_value5", "");
                            String where = "_id = '" + id + "' ";
                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                            getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Items")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id",id)
                                    .build();
                            getActivity().getContentResolver().notifyChange(resultUri, null);

                            String where1_v1 = "docid = '" + id + "'";
                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                        }
                    }
                }while (taxeitem_all.moveToNext());
            }
            taxeitem_all.close();

            Cursor tax_selection = db1.rawQuery("SELECT * FROM Customerdetails", null);
            if (tax_selection.moveToLast()){
                do {
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("tax1", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues1.put("tax1_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where1 = "tax1 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues1, where1, new String[]{});

                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put("tax2", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues2.put("tax2_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where2 = "tax2 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues2, where2, new String[]{});

                    ContentValues contentValues3 = new ContentValues();
                    contentValues3.put("tax3", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues3.put("tax3_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where3 = "tax3 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues3, where3, new String[]{});

                    ContentValues contentValues4 = new ContentValues();
                    contentValues4.put("tax4", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues4.put("tax4_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where4 = "tax4 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues4, where4, new String[]{});

                    ContentValues contentValues5 = new ContentValues();
                    contentValues5.put("tax5", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues5.put("tax5_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where5 = "tax5 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues5, where5, new String[]{});

                    ContentValues contentValues6 = new ContentValues();
                    contentValues6.put("tax6", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues6.put("tax6_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where6 = "tax6 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues6, where6, new String[]{});

                    ContentValues contentValues7 = new ContentValues();
                    contentValues7.put("tax7", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues7.put("tax7_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where7 = "tax7 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues7, where7, new String[]{});

                    ContentValues contentValues8 = new ContentValues();
                    contentValues8.put("tax8", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues8.put("tax8_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where8 = "tax8 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues8, where8, new String[]{});

                    ContentValues contentValues9 = new ContentValues();
                    contentValues9.put("tax9", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues9.put("tax9_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where9 = "tax9 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues9, where9, new String[]{});

                    ContentValues contentValues10 = new ContentValues();
                    contentValues10.put("tax10", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues10.put("tax10_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where10 = "tax10 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues10, where10, new String[]{});

                    ContentValues contentValues11 = new ContentValues();
                    contentValues11.put("tax11", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues11.put("tax11_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where11 = "tax11 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues11, where11, new String[]{});

                    ContentValues contentValues12 = new ContentValues();
                    contentValues12.put("tax12", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues12.put("tax12_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where12 = "tax12 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues12, where12, new String[]{});

                    ContentValues contentValues13 = new ContentValues();
                    contentValues13.put("tax13", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues13.put("tax13_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where13 = "tax13 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues13, where13, new String[]{});

                    ContentValues contentValues14 = new ContentValues();
                    contentValues14.put("tax14", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues14.put("tax14_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where14 = "tax14 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues14, where14, new String[]{});

                    ContentValues contentValues15 = new ContentValues();
                    contentValues15.put("tax15", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues15.put("tax15_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where15 = "tax15 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues15, where15, new String[]{});
                }while (tax_selection.moveToNext());
            }
            tax_selection.close();

            Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
            if (cursor.moveToFirst()){
                do {
                    String id = cursor.getString(0);
                    String temp = cursor.getString(36);

                    TextView tv = new TextView(getActivity());
                    tv.setText(temp);

                    if (tv.getText().toString().equals("yes")) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("status_temp", "");
                        contentValues.put("status_perm", "");
                        String where = " _id ='" + id + "'";
//                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                        getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                        resultUri = new Uri.Builder()
//                                .scheme("content")
//                                .authority(StubProviderApp.AUTHORITY)
//                                .path("Items")
//                                .appendQueryParameter("operation", "update")
//                                .appendQueryParameter("_id",id)
//                                .build();
//                        getActivity().getContentResolver().notifyChange(resultUri, null);

                        String where1_v1 = "docid = '" + id + "'";
                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                        db.update("Items", contentValues, where, new String[]{});

                    }
                }while (cursor.moveToNext());
            }
            cursor.close();

            webservicequery("UPDATE Items set status_temp = '' , status_perm = '' WHERE status_temp = 'yes'");

            int selected1 =
                    radioGroupWebsite.getCheckedRadioButtonId();
            radioBtn1 = (RadioButton) dialog.findViewById(selected1);
            saveInDBup(Integer.parseInt(ID));

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Integer file_url) {
            //hideKeyboard(getContext());

            dialog.dismiss();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            hideKeyboard(getContext());
            donotshowKeyboard(getActivity());
            Toast.makeText(getActivity(),getString(R.string.tax_saved), Toast.LENGTH_SHORT).show();

            if (radioBtn1.getText().toString().equals("Globaltax")){
                Cursor taxeitem1 = db.rawQuery("SELECT * FROM Items", null);
                //Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
                if (taxeitem1.moveToFirst()){
                    //Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
                    String five = taxeitem1.getString(5);
                    String one = taxeitem1.getString(1);
                    //if (five.toString().equals(NAme)){
//                                                    Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();
//                                                    Toast.makeText(getActivity(), " "+one, Toast.LENGTH_SHORT).show();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("itemtax", "None");
                    String where = "itemtax = '" + NAme + "' ";

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                    getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Items")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("itemtax",NAme)
                            .build();
                    getActivity().getContentResolver().notifyChange(resultUri, null);
                    String where1_v1 = "itemtax = '" + NAme + "' ";
                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                    //}
                }
                taxeitem1.close();

            }else {

            }

            if (!selected.equals("All")) {
                //Toast.makeText(getActivity(), " " + selected, Toast.LENGTH_SHORT).show();
                countryList = new ArrayList<Country1>();
                try {
                    db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor allrows = db.rawQuery("SELECT * FROM Taxes WHERE taxtype = '" + selected + "'", null);
                    System.out.println("COUNT : " + allrows.getCount());


                    //Country1 country = new Country1(name, name, name, name);

                    if (allrows.moveToFirst()) {
                        do {
                            String ID = allrows.getString(0);
                            String NAme = allrows.getString(1);
                            String PlaCe = allrows.getString(2);
                            if (NAme.toString().equals("None")){

                            }else {
                                Country1 NAME = new Country1(ID, NAme);
                                //Country1 PLACE = new Country1(PlaCe, PlaCe, PlaCe, PlaCe);
                                countryList.add(NAME);
                                //countryList.add(PLACE);
                            }
                        } while (allrows.moveToNext());
                    }
                    allrows.close();
                    //db.close();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error encountered.",
                            Toast.LENGTH_LONG);
                }
            }
            if (selected.equals("All")) {
                //Toast.makeText(getActivity(), " " + selected, Toast.LENGTH_SHORT).show();
                countryList = new ArrayList<Country1>();
                try {
                    db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor allrows = db.rawQuery("SELECT * FROM Taxes ", null);
                    System.out.println("COUNT : " + allrows.getCount());


                    //Country1 country = new Country1(name, name, name, name);

                    if (allrows.moveToFirst()) {
                        do {
                            String ID = allrows.getString(0);
                            String NAme = allrows.getString(1);
                            String PlaCe = allrows.getString(2);
                            if (NAme.toString().equals("None")){

                            }else {
                                Country1 NAME = new Country1(ID, NAme);
                                //Country1 PLACE = new Country1(PlaCe, PlaCe, PlaCe, PlaCe);
                                countryList.add(NAME);
                                //countryList.add(PLACE);
                            }
                        } while (allrows.moveToNext());
                    }
                    allrows.close();
                    //db.close();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error encountered.",
                            Toast.LENGTH_LONG);
                }
            }


            dataAdapter = new MyCustomAdapter(getActivity(),
                    R.layout.items_deletionbox, countryList);
            final ListView listView = (ListView) rootview.findViewById(R.id.listView);
            // Assign adapter to ListView
            listView.setAdapter(dataAdapter);

            progressbar.setVisibility(View.GONE);
        }
    }


    class DownloadMusicfromInternetde_delete extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

//            Cursor taxitem_del = db.rawQuery("SELECT * FROM Items", null);
//            if (taxitem_del.moveToFirst()){
//                do {
//                    String id = taxitem_del.getString(0);
//                    String tax1 = taxitem_del.getString(5);
//                    String tax2 = taxitem_del.getString(28);
//                    String tax3 = taxitem_del.getString(30);
//                    String tax4 = taxitem_del.getString(32);
//                    String tax5 = taxitem_del.getString(34);
//
//                    TextView tv = new TextView(getActivity());
//                    tv.setText(tax1);
//                    TextView tv2 = new TextView(getActivity());
//                    tv2.setText(tax2);
//                    TextView tv3 = new TextView(getActivity());
//                    tv3.setText(tax3);
//                    TextView tv4 = new TextView(getActivity());
//                    tv4.setText(tax4);
//                    TextView tv5 = new TextView(getActivity());
//                    tv5.setText(tax5);
//
//                    if (tv.getText().toString().equals(NAme)){
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("itemtax", "None");
//                        contentValues.put("tax_value", "");
//                        String where = "_id = '" + id + "' ";
//                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                        getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                        resultUri = new Uri.Builder()
//                                .scheme("content")
//                                .authority(StubProviderApp.AUTHORITY)
//                                .path("Items")
//                                .appendQueryParameter("operation", "update")
//                                .appendQueryParameter("_id",id)
//                                .build();
//                        getActivity().getContentResolver().notifyChange(resultUri, null);
////                        db.update("Items", contentValues, where, new String[]{});
//
//                        String where1_v1 = "docid = '" + id + "'";
//                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//
//                    }
//                    if (tv2.getText().toString().equals(NAme)){
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("itemtax2", "None");
//                        contentValues.put("tax_value2", "");
//                        String where = "_id = '" + id + "' ";
//                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                        getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                        resultUri = new Uri.Builder()
//                                .scheme("content")
//                                .authority(StubProviderApp.AUTHORITY)
//                                .path("Items")
//                                .appendQueryParameter("operation", "update")
//                                .appendQueryParameter("_id",id)
//                                .build();
//                        getActivity().getContentResolver().notifyChange(resultUri, null);
////                        db.update("Items", contentValues, where, new String[]{});
//
//                        String where1_v1 = "docid = '" + id + "'";
//                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//                    }
//                    if (tv3.getText().toString().equals(NAme)){
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("itemtax3", "None");
//                        contentValues.put("tax_value3", "");
//                        String where = "_id = '" + id + "' ";
//                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                        getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                        resultUri = new Uri.Builder()
//                                .scheme("content")
//                                .authority(StubProviderApp.AUTHORITY)
//                                .path("Items")
//                                .appendQueryParameter("operation", "update")
//                                .appendQueryParameter("_id",id)
//                                .build();
//                        getActivity().getContentResolver().notifyChange(resultUri, null);
////                        db.update("Items", contentValues, where, new String[]{});
//                        String where1_v1 = "docid = '" + id + "'";
//                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//
//
//                    }
//                    if (tv4.getText().toString().equals(NAme)){
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("itemtax4", "None");
//                        contentValues.put("tax_value4", "");
//                        String where = "_id = '" + id + "' ";
//                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                        getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                        resultUri = new Uri.Builder()
//                                .scheme("content")
//                                .authority(StubProviderApp.AUTHORITY)
//                                .path("Items")
//                                .appendQueryParameter("operation", "update")
//                                .appendQueryParameter("_id",id)
//                                .build();
//                        getActivity().getContentResolver().notifyChange(resultUri, null);
////                        db.update("Items", contentValues, where, new String[]{});
//
//                        String where1_v1 = "docid = '" + id + "'";
//                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//
//
//                    }
//                    if (tv5.getText().toString().equals(NAme)){
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("itemtax5", "None");
//                        contentValues.put("tax_value5", "");
//                        String where = "_id = '" + id + "' ";
//                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                        getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                        resultUri = new Uri.Builder()
//                                .scheme("content")
//                                .authority(StubProviderApp.AUTHORITY)
//                                .path("Items")
//                                .appendQueryParameter("operation", "update")
//                                .appendQueryParameter("_id",id)
//                                .build();
//                        getActivity().getContentResolver().notifyChange(resultUri, null);
////                        db.update("Items", contentValues, where, new String[]{});
//
//                        String where1_v1 = "docid = '" + id + "'";
//                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//
//                    }
//                }while (taxitem_del.moveToNext());
//            }
//            taxitem_del.close();

            db.execSQL("UPDATE Items set itemtax = '' , tax_value = '' WHERE itemtax = '"+NAme+"'");
            db.execSQL("UPDATE Items set itemtax2 = '' , tax_value2 = '' WHERE itemtax2 = '"+NAme+"'");
            db.execSQL("UPDATE Items set itemtax3 = '' , tax_value3 = '' WHERE itemtax3 = '"+NAme+"'");
            db.execSQL("UPDATE Items set itemtax4 = '' , tax_value4 = '' WHERE itemtax4 = '"+NAme+"'");
            db.execSQL("UPDATE Items set itemtax5 = '' , tax_value5 = '' WHERE itemtax5 = '"+NAme+"'");


            webservicequery("UPDATE Items set itemtax = '' , tax_value = '' WHERE itemtax = '"+NAme+"'");
            webservicequery("UPDATE Items set itemtax2 = '' , tax_value2 = '' WHERE itemtax2 = '"+NAme+"'");
            webservicequery("UPDATE Items set itemtax3 = '' , tax_value3 = '' WHERE itemtax3 = '"+NAme+"'");
            webservicequery("UPDATE Items set itemtax4 = '' , tax_value4 = '' WHERE itemtax4 = '"+NAme+"'");
            webservicequery("UPDATE Items set itemtax5 = '' , tax_value5 = '' WHERE itemtax5 = '"+NAme+"'");

//            Cursor taxeitem = db.rawQuery("SELECT * FROM Items WHERE itemtax = '"+NAme+"'", null);
//            if (taxeitem.moveToFirst()){
//                do {
//                    String five = taxeitem.getString(0);
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("itemtax", "None");
//                    contentValues.put("tax_value", "");
//                    String where = "_id = '" + five + "' ";
//                    db.update("Items", contentValues, where, new String[]{});
//                }while (taxeitem.moveToNext());
//            }
//            taxeitem.close();
//
//            Cursor taxeitem2 = db.rawQuery("SELECT * FROM Items WHERE itemtax2 = '"+NAme+"'", null);
//            if (taxeitem2.moveToFirst()){
//                do {
//                    String five = taxeitem2.getString(0);
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("itemtax2", "None");
//                    contentValues.put("tax_value2", "");
//                    String where = "_id = '" + five + "' ";
//                    db.update("Items", contentValues, where, new String[]{});
//                }while (taxeitem2.moveToNext());
//            }
//            taxeitem2.close();
//
//            Cursor taxeitem3 = db.rawQuery("SELECT * FROM Items WHERE itemtax3 = '"+NAme+"'", null);
//            if (taxeitem3.moveToFirst()){
//                do {
//                    String five = taxeitem3.getString(0);
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("itemtax3", "None");
//                    contentValues.put("tax_value3", "");
//                    String where = "_id = '" + five + "' ";
//                    db.update("Items", contentValues, where, new String[]{});
//                }while (taxeitem3.moveToNext());
//            }
//            taxeitem3.close();
//
//            Cursor taxeitem4 = db.rawQuery("SELECT * FROM Items WHERE itemtax4 = '"+NAme+"'", null);
//            if (taxeitem4.moveToFirst()){
//                do {
//                    String five = taxeitem4.getString(0);
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("itemtax4", "None");
//                    contentValues.put("tax_value4", "");
//                    String where = "_id = '" + five + "' ";
//                    db.update("Items", contentValues, where, new String[]{});
//                }while (taxeitem4.moveToNext());
//            }
//            taxeitem4.close();
//
//            Cursor taxeitem5 = db.rawQuery("SELECT * FROM Items WHERE itemtax5 = '"+NAme+"'", null);
//            if (taxeitem5.moveToFirst()){
//                do {
//                    String five = taxeitem5.getString(0);
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("itemtax5", "None");
//                    contentValues.put("tax_value5", "");
//                    String where = "_id = '" + five + "' ";
//                    db.update("Items", contentValues, where, new String[]{});
//                }while (taxeitem5.moveToNext());
//            }
//            taxeitem5.close();

            Cursor tax_selection = db1.rawQuery("SELECT * FROM Customerdetails", null);
            if (tax_selection.moveToLast()){
                do {
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("tax1", "");
                    contentValues1.put("tax1_value", "");
                    String where1 = "tax1 = '" + dialog_columnvalue + "' ";
                    db1.update("Customerdetails", contentValues1, where1, new String[]{});

                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put("tax2", "");
                    contentValues2.put("tax2_value", "");
                    String where2 = "tax2 = '" + dialog_columnvalue + "' ";
                    db1.update("Customerdetails", contentValues2, where2, new String[]{});

                    ContentValues contentValues3 = new ContentValues();
                    contentValues3.put("tax3", "");
                    contentValues3.put("tax3_value", "");
                    String where3 = "tax3 = '" + dialog_columnvalue + "' ";
                    db1.update("Customerdetails", contentValues3, where3, new String[]{});

                    ContentValues contentValues4 = new ContentValues();
                    contentValues4.put("tax4", "");
                    contentValues4.put("tax4_value", "");
                    String where4 = "tax4 = '" + dialog_columnvalue + "' ";
                    db1.update("Customerdetails", contentValues4, where4, new String[]{});

                    ContentValues contentValues5 = new ContentValues();
                    contentValues5.put("tax5", "");
                    contentValues5.put("tax5_value", "");
                    String where5 = "tax5 = '" + dialog_columnvalue + "' ";
                    db1.update("Customerdetails", contentValues5, where5, new String[]{});

                    ContentValues contentValues6 = new ContentValues();
                    contentValues6.put("tax6", "");
                    contentValues6.put("tax6_value", "");
                    String where6 = "tax6 = '" + dialog_columnvalue + "' ";
                    db1.update("Customerdetails", contentValues6, where6, new String[]{});

                    ContentValues contentValues7 = new ContentValues();
                    contentValues7.put("tax7", "");
                    contentValues7.put("tax7_value", "");
                    String where7 = "tax7 = '" + dialog_columnvalue + "' ";
                    db1.update("Customerdetails", contentValues7, where7, new String[]{});

                    ContentValues contentValues8 = new ContentValues();
                    contentValues8.put("tax8", "");
                    contentValues8.put("tax8_value", "");
                    String where8 = "tax8 = '" + dialog_columnvalue + "' ";
                    db1.update("Customerdetails", contentValues8, where8, new String[]{});

                    ContentValues contentValues9 = new ContentValues();
                    contentValues9.put("tax9", "");
                    contentValues9.put("tax9_value", "");
                    String where9 = "tax9 = '" + dialog_columnvalue + "' ";
                    db1.update("Customerdetails", contentValues9, where9, new String[]{});

                    ContentValues contentValues10 = new ContentValues();
                    contentValues10.put("tax10", "");
                    contentValues10.put("tax10_value", "");
                    String where10 = "tax10 = '" + dialog_columnvalue + "' ";
                    db1.update("Customerdetails", contentValues10, where10, new String[]{});

                    ContentValues contentValues11 = new ContentValues();
                    contentValues11.put("tax11", "");
                    contentValues11.put("tax11_value", "");
                    String where11 = "tax11 = '" + dialog_columnvalue + "' ";
                    db1.update("Customerdetails", contentValues11, where11, new String[]{});

                    ContentValues contentValues12 = new ContentValues();
                    contentValues12.put("tax12", "");
                    contentValues12.put("tax12_value", "");
                    String where12 = "tax12 = '" + dialog_columnvalue + "' ";
                    db1.update("Customerdetails", contentValues12, where12, new String[]{});

                    ContentValues contentValues13 = new ContentValues();
                    contentValues13.put("tax13", "");
                    contentValues13.put("tax13_value", "");
                    String where13 = "tax13 = '" + dialog_columnvalue + "' ";
                    db1.update("Customerdetails", contentValues13, where13, new String[]{});

                    ContentValues contentValues14 = new ContentValues();
                    contentValues14.put("tax14", "");
                    contentValues14.put("tax14_value", "");
                    String where14 = "tax14 = '" + dialog_columnvalue + "' ";
                    db1.update("Customerdetails", contentValues14, where14, new String[]{});

                    ContentValues contentValues15 = new ContentValues();
                    contentValues15.put("tax15", "");
                    contentValues15.put("tax15_value", "");
                    String where15 = "tax15 = '" + dialog_columnvalue + "' ";
                    db1.update("Customerdetails", contentValues15, where15, new String[]{});
                }while (tax_selection.moveToNext());
            }
            tax_selection.close();

            String where = "_id = '" + ID + "' ";
            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Taxes");
            getActivity().getContentResolver().delete(contentUri, where, new String[]{});
            resultUri = new Uri.Builder()
                    .scheme("content")
                    .authority(StubProviderApp.AUTHORITY)
                    .path("Taxes")
                    .appendQueryParameter("operation", "delete")
                    .appendQueryParameter("_id", ID)
                    .build();
            getActivity().getContentResolver().notifyChange(resultUri, null);

            webservicequery("DELETE FROM Taxes WHERE taxname = '"+NAme+"'");

//            db.delete("Taxes", where, new String[]{});

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Integer file_url) {

            if (!selected.equals("All")) {
                //Toast.makeText(getActivity(), " " + selected, Toast.LENGTH_SHORT).show();
                countryList = new ArrayList<Country1>();
                try {
                    db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor allrows = db.rawQuery("SELECT * FROM Taxes WHERE taxtype = '" + selected + "'", null);
                    System.out.println("COUNT : " + allrows.getCount());


                    //Country1 country = new Country1(name, name, name, name);

                    if (allrows.moveToFirst()) {
                        do {
                            String ID = allrows.getString(0);
                            String NAme = allrows.getString(1);
                            String PlaCe = allrows.getString(2);
                            if (NAme.toString().equals("None")){

                            }else {
                                Country1 NAME = new Country1(ID, NAme);
                                //Country1 PLACE = new Country1(PlaCe, PlaCe, PlaCe, PlaCe);
                                countryList.add(NAME);
                                //countryList.add(PLACE);
                            }
                        } while (allrows.moveToNext());
                    }
                    allrows.close();
                    //db.close();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error encountered.",
                            Toast.LENGTH_LONG);
                }
            }
            if (selected.equals("All")) {
                //Toast.makeText(getActivity(), " " + selected, Toast.LENGTH_SHORT).show();
                countryList = new ArrayList<Country1>();
                try {
                    db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor allrows = db.rawQuery("SELECT * FROM Taxes ", null);
                    System.out.println("COUNT : " + allrows.getCount());


                    //Country1 country = new Country1(name, name, name, name);

                    if (allrows.moveToFirst()) {
                        do {
                            String ID = allrows.getString(0);
                            String NAme = allrows.getString(1);
                            String PlaCe = allrows.getString(2);
                            if (NAme.toString().equals("None")){

                            }else {
                                Country1 NAME = new Country1(ID, NAme);
                                //Country1 PLACE = new Country1(PlaCe, PlaCe, PlaCe, PlaCe);
                                countryList.add(NAME);
                                //countryList.add(PLACE);
                            }
                        } while (allrows.moveToNext());
                    }
                    allrows.close();
                    //db.close();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error encountered.",
                            Toast.LENGTH_LONG);
                }
            }


            dataAdapter = new MyCustomAdapter(getActivity(),
                    R.layout.items_deletionbox, countryList);
            final ListView listView = (ListView) rootview.findViewById(R.id.listView);
            // Assign adapter to ListView
            listView.setAdapter(dataAdapter);

            progressbar.setVisibility(View.GONE);
        }
    }


    class DownloadMusicfromInternetde_update_item extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            db.close();
            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

            System.out.println("taxes name "+dialog_columnvalue);
            System.out.println("taxes name "+dialogC2_id.getText().toString());

            tax_insert_server(String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)", dialogC2_id.getText().toString());

            final String webserviceQuery0 ="update Items set itemtax = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"',tax_value ='"+Float.valueOf(dialogC2_id.getText().toString())+"' where itemtax = '"+NAme+"'";
            final String webserviceQuery2 ="update Items set itemtax2 = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"',tax_value2 ='"+Float.valueOf(dialogC2_id.getText().toString())+"' where itemtax2 = '"+NAme+"'";
            final String webserviceQuery3 ="update Items set itemtax3 = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"',tax_value3 ='"+Float.valueOf(dialogC2_id.getText().toString())+"' where itemtax3 = '"+NAme+"'";
            final String webserviceQuery4 ="update Items set itemtax4 = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"',tax_value4 ='"+Float.valueOf(dialogC2_id.getText().toString())+"' where itemtax4 = '"+NAme+"'";
            final String webserviceQuery5 ="update Items set itemtax5 = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"',tax_value5 ='"+Float.valueOf(dialogC2_id.getText().toString())+"' where itemtax5 = '"+NAme+"'";
            //db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

            db.execSQL(webserviceQuery0);
            db.execSQL(webserviceQuery2);
            db.execSQL(webserviceQuery3);
            db.execSQL(webserviceQuery4);
            db.execSQL(webserviceQuery5);

//            Cursor taxeitem = db.rawQuery("SELECT * FROM Items WHERE itemtax = '"+NAme+"'", null);
//            if (taxeitem.moveToFirst()){
//                do {
//                    String five = taxeitem.getString(0);
//                    String itemname_t=taxeitem.getString(1);
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("itemtax", String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)");
//                    contentValues.put("tax_value", dialogC2_id.getText().toString());
//                    String where = "_id = '" + five + "' ";
////                     db.update("Items", contentValues, where, new String[]{});
//
////                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
////                    getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
////                    resultUri = new Uri.Builder()
////                            .scheme("content")
////                            .authority(StubProviderApp.AUTHORITY)
////                            .path("Items")
////                            .appendQueryParameter("operation", "update")
////                            .appendQueryParameter("_id",five)
////                            .build();
////                    getActivity().getContentResolver().notifyChange(resultUri, null);
//////                    db.update("Items", contentValues, where, new String[]{});
////
////                    String where1_v1 = "docid = '" + five + "'";
////                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
////                    final String webserviceQuery ="update Items set itemtax = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"'"+",tax_value ="+Float.valueOf(dialogC2_id.getText().toString())+" where itemname='"+itemname_t+"'";
//                    final String webserviceQuery ="update Items set itemtax = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"',tax_value ='"+Float.valueOf(dialogC2_id.getText().toString())+"' where _id='"+five+"'";
//                    //db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//                    db.execSQL(webserviceQuery);
//
////                    webservicequery(webserviceQuery);
//                    //System.out.println("query1a "+webserviceQuery);
//
//                }while (taxeitem.moveToNext());
//            }
//            taxeitem.close();
//
//            Cursor taxeitem2 = db.rawQuery("SELECT * FROM Items WHERE itemtax2 = '"+NAme+"'", null);
//            if (taxeitem2.moveToFirst()){
//                do {
//                    String five = taxeitem2.getString(0);
//                    String itemname_t = taxeitem2.getString(1);
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("itemtax2", String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)");
//                    contentValues.put("tax_value2", dialogC2_id.getText().toString());
//                    String where = "_id = '" + five + "' ";
//
////                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
////                    getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
////                    resultUri = new Uri.Builder()
////                            .scheme("content")
////                            .authority(StubProviderApp.AUTHORITY)
////                            .path("Items")
////                            .appendQueryParameter("operation", "update")
////                            .appendQueryParameter("_id",five)
////                            .build();
////                    getActivity().getContentResolver().notifyChange(resultUri, null);
////                    String where1_v1 = "docid = '" + five + "'";
////                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//
//
////                    final String webserviceQuery ="update Items set itemtax2 = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"'"+",tax_value2 ="+Float.valueOf(dialogC2_id.getText().toString())+" where itemname='"+itemname_t+"'";
//                    final String webserviceQuery ="update Items set itemtax2 = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"',tax_value2 ='"+Float.valueOf(dialogC2_id.getText().toString())+"' where _id='"+five+"'";
//                    //db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//                    db.execSQL(webserviceQuery);
//
////                    webservicequery(webserviceQuery);
//                    //System.out.println("query1b "+webserviceQuery);
//
//
//                }while (taxeitem2.moveToNext());
//            }
//            taxeitem2.close();
//
//            Cursor taxeitem3 = db.rawQuery("SELECT * FROM Items WHERE itemtax3 = '"+NAme+"'", null);
//            if (taxeitem3.moveToFirst()){
//                do {
//                    String five = taxeitem3.getString(0);
//                    String itemname_t = taxeitem3.getString(1);
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("itemtax3", String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)");
//                    contentValues.put("tax_value3", dialogC2_id.getText().toString());
//                    String where = "_id = '" + five + "' ";
//                    db.update("Items", contentValues, where, new String[]{});
////                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
////                    getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
////                    resultUri = new Uri.Builder()
////                            .scheme("content")
////                            .authority(StubProviderApp.AUTHORITY)
////                            .path("Items")
////                            .appendQueryParameter("operation", "update")
////                            .appendQueryParameter("_id",five)
////                            .build();
////                    getActivity().getContentResolver().notifyChange(resultUri, null);
//
////                    final String webserviceQuery ="update Items set itemtax3 = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"'"+",tax_value3 ="+Float.valueOf(dialogC2_id.getText().toString())+" where itemname='"+itemname_t+"'";
//                    final String webserviceQuery ="update Items set itemtax3 = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"',tax_value3 ='"+Float.valueOf(dialogC2_id.getText().toString())+"' where _id='"+five+"'";
//
//                    String where1_v1 = "docid = '" + five + "'";
//                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
////                    webservicequery(webserviceQuery);
//                    //System.out.println("query1c "+webserviceQuery);
//
//                }while (taxeitem3.moveToNext());
//            }
//            taxeitem3.close();
//
//            Cursor taxeitem4 = db.rawQuery("SELECT * FROM Items WHERE itemtax4 = '"+NAme+"'", null);
//            if (taxeitem4.moveToFirst()){
//                do {
//                    String five = taxeitem4.getString(0);
//                    String itemname_t = taxeitem4.getString(1);
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("itemtax4", String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)");
//                    contentValues.put("tax_value4", dialogC2_id.getText().toString());
//                    String where = "_id = '" + five + "' ";
//
//                    db.update("Items", contentValues, where, new String[]{});
//
////                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
////                    getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
////                    resultUri = new Uri.Builder()
////                            .scheme("content")
////                            .authority(StubProviderApp.AUTHORITY)
////                            .path("Items")
////                            .appendQueryParameter("operation", "update")
////                            .appendQueryParameter("_id",five)
////                            .build();
////                    getActivity().getContentResolver().notifyChange(resultUri, null);
//
////                    final String webserviceQuery ="update Items set itemtax4 = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"'"+",tax_value4 ="+Float.valueOf(dialogC2_id.getText().toString())+" where itemname='"+itemname_t+"'";
//                    final String webserviceQuery ="update Items set itemtax4 = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"',tax_value4 ='"+Float.valueOf(dialogC2_id.getText().toString())+"' where _id='"+five+"'";
//
//                    String where1_v1 = "docid = '" + five + "'";
//                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
////                    webservicequery(webserviceQuery);
//                    //System.out.println("query1d "+webserviceQuery);
//
//                }while (taxeitem4.moveToNext());
//            }
//            taxeitem4.close();
//
//            Cursor taxeitem5 = db.rawQuery("SELECT * FROM Items WHERE itemtax5 = '"+NAme+"'", null);
//            if (taxeitem5.moveToFirst()){
//                do {
//                    String five = taxeitem5.getString(0);
//                    String itemname_t = taxeitem5.getString(1);
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("itemtax5", String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)");
//                    contentValues.put("tax_value5", dialogC2_id.getText().toString());
//                    String where = "_id = '" + five + "' ";
//
//                    db.update("Items", contentValues, where, new String[]{});
//
////                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
////                    getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
////                    resultUri = new Uri.Builder()
////                            .scheme("content")
////                            .authority(StubProviderApp.AUTHORITY)
////                            .path("Items")
////                            .appendQueryParameter("operation", "update")
////                            .appendQueryParameter("_id",five)
////                            .build();
////                    getActivity().getContentResolver().notifyChange(resultUri, null);
//
////                    final String webserviceQuery ="update Items set itemtax5 = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"'"+",tax_value5 ="+Float.valueOf(dialogC2_id.getText().toString())+" where itemname='"+itemname_t+"'";
//                    final String webserviceQuery ="update Items set itemtax5 = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"',tax_value5 ='"+Float.valueOf(dialogC2_id.getText().toString())+"' where _id='"+five+"'";
//
//                    String where1_v1 = "docid = '" + five + "'";
//                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
////                    webservicequery(webserviceQuery);
//                    //System.out.println("query1e "+webserviceQuery);
//
//                }while (taxeitem5.moveToNext());
//            }
//            taxeitem5.close();

            Cursor taxeitem_all = db.rawQuery("SELECT * FROM Items", null);
            if (taxeitem_all.moveToFirst()){
                do {
                    String id = taxeitem_all.getString(0);
                    String one = taxeitem_all.getString(5);
                    String two = taxeitem_all.getString(28);
                    String three = taxeitem_all.getString(30);
                    String four = taxeitem_all.getString(32);
                    String five = taxeitem_all.getString(34);
                    String st = taxeitem_all.getString(37);

                    TextView tv = new TextView(getActivity());
                    tv.setText(one);
                    TextView tv2 = new TextView(getActivity());
                    tv2.setText(two);
                    TextView tv3 = new TextView(getActivity());
                    tv3.setText(three);
                    TextView tv4 = new TextView(getActivity());
                    tv4.setText(four);
                    TextView tv5 = new TextView(getActivity());
                    tv5.setText(five);

                    TextView stt = new TextView(getActivity());
                    stt.setText(st);

                    if (stt.getText().toString().equals("yes")) {
                        if (tv.getText().toString().equals(String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)") ||
                                tv2.getText().toString().equals(String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)") ||
                                tv3.getText().toString().equals(String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)") ||
                                tv4.getText().toString().equals(String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)") ||
                                tv5.getText().toString().equals(String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)")){

                        }else {
//                        Toast.makeText(getActivity(), "id "+id, Toast.LENGTH_LONG).show();
                            if (tv.getText().toString().equals("") || tv.getText().toString().equals("None")) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("itemtax", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                                contentValues.put("tax_value", dialogC2_id.getText().toString());
                                String where = "_id = '" + id + "' ";

                                db.update("Items", contentValues, where, new String[]{});

//                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                                getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                                resultUri = new Uri.Builder()
//                                        .scheme("content")
//                                        .authority(StubProviderApp.AUTHORITY)
//                                        .path("Items")
//                                        .appendQueryParameter("operation", "update")
//                                        .appendQueryParameter("_id",id)
//                                        .build();
//                                getActivity().getContentResolver().notifyChange(resultUri, null);

//                                final String webserviceQuery ="update Items set itemtax = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"'"+",tax_value ="+Float.valueOf(dialogC2_id.getText().toString())+" where _id='"+id+"'";
                                final String webserviceQuery ="update Items set itemtax = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"',tax_value ='"+Float.valueOf(dialogC2_id.getText().toString())+"' where _id='"+id+"'";
//                                webservicequery(webserviceQuery);

                                String where1_v1 = "docid = '" + id + "'";
                                db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                                //System.out.println("query2a "+webserviceQuery);

//                            Toast.makeText(getActivity(), "itemtax "+String.valueOf(dialog_columnvalue), Toast.LENGTH_LONG).show();
                            } else {
                                if (tv2.getText().toString().equals("") || tv2.getText().toString().equals("None")) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("itemtax2", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                                    contentValues.put("tax_value2", dialogC2_id.getText().toString());
                                    String where = "_id = '" + id + "' ";

                                    db.update("Items", contentValues, where, new String[]{});

//                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                                    getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                                    resultUri = new Uri.Builder()
//                                            .scheme("content")
//                                            .authority(StubProviderApp.AUTHORITY)
//                                            .path("Items")
//                                            .appendQueryParameter("operation", "update")
//                                            .appendQueryParameter("_id",id)
//                                            .build();
//                                    getActivity().getContentResolver().notifyChange(resultUri, null);
//                                    db.update("Items", contentValues, where, new String[]{});
//                                Toast.makeText(getActivity(), "itemtax2 "+String.valueOf(dialog_columnvalue), Toast.LENGTH_LONG).show();

//                                    final String webserviceQuery ="update Items set itemtax2 = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"'"+",tax_value2 ="+Float.valueOf(dialogC2_id.getText().toString())+" where _id='"+id+"'";
                                    final String webserviceQuery ="update Items set itemtax2 = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"',tax_value2 ='"+Float.valueOf(dialogC2_id.getText().toString())+"' where _id='"+id+"'";
//                                    webservicequery(webserviceQuery);

                                    String where1_v1 = "docid = '" + id + "'";
                                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                                    //System.out.println("query2b "+webserviceQuery);

                                } else {
                                    if (tv3.getText().toString().equals("") || tv3.getText().toString().equals("None")) {
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("itemtax3", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                                        contentValues.put("tax_value3", dialogC2_id.getText().toString());
                                        String where = "_id = '" + id + "' ";

                                        db.update("Items", contentValues, where, new String[]{});

//                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                                        getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                                        resultUri = new Uri.Builder()
//                                                .scheme("content")
//                                                .authority(StubProviderApp.AUTHORITY)
//                                                .path("Items")
//                                                .appendQueryParameter("operation", "update")
//                                                .appendQueryParameter("_id",id)
//                                                .build();
//                                        getActivity().getContentResolver().notifyChange(resultUri, null);
//                                        db.update("Items", contentValues, where, new String[]{});
//                                    Toast.makeText(getActivity(), "itemtax3 "+String.valueOf(dialog_columnvalue), Toast.LENGTH_LONG).show();

//                                        final String webserviceQuery ="update Items set itemtax3 = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"'"+",tax_value3 ="+Float.valueOf(dialogC2_id.getText().toString())+" where _id='"+id+"'";
                                        final String webserviceQuery ="update Items set itemtax3 = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"',tax_value3 ='"+Float.valueOf(dialogC2_id.getText().toString())+"' where _id='"+id+"'";
//                                        webservicequery(webserviceQuery);

                                        String where1_v1 = "docid = '" + id + "'";
                                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                                        //System.out.println("query2c "+webserviceQuery);

                                    } else {
                                        if (tv4.getText().toString().equals("") || tv4.getText().toString().equals("None")) {
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("itemtax4", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                                            contentValues.put("tax_value4", dialogC2_id.getText().toString());
                                            String where = "_id = '" + id + "' ";

                                            db.update("Items", contentValues, where, new String[]{});

//                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                                            getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                                            resultUri = new Uri.Builder()
//                                                    .scheme("content")
//                                                    .authority(StubProviderApp.AUTHORITY)
//                                                    .path("Items")
//                                                    .appendQueryParameter("operation", "update")
//                                                    .appendQueryParameter("_id",id)
//                                                    .build();
//                                            getActivity().getContentResolver().notifyChange(resultUri, null);
//                                            db.update("Items", contentValues, where, new String[]{});
//                                        Toast.makeText(getActivity(), "itemtax4 "+String.valueOf(dialog_columnvalue), Toast.LENGTH_LONG).show();

//                                            final String webserviceQuery ="update Items set itemtax4 = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"'"+",tax_value4 ="+Float.valueOf(dialogC2_id.getText().toString())+" where _id='"+id+"'";
                                            final String webserviceQuery ="update Items set itemtax4 = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"',tax_value4 ='"+Float.valueOf(dialogC2_id.getText().toString())+"' where _id='"+id+"'";
//                                            webservicequery(webserviceQuery);

                                            String where1_v1 = "docid = '" + id + "'";
                                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                                            //System.out.println("query2d "+webserviceQuery);

                                        } else {
                                            if (tv5.getText().toString().equals("") || tv5.getText().toString().equals("None")) {
                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("itemtax5", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                                                contentValues.put("tax_value5", dialogC2_id.getText().toString());
                                                String where = "_id = '" + id + "' ";

                                                db.update("Items", contentValues, where, new String[]{});

//                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                                                getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                                                resultUri = new Uri.Builder()
//                                                        .scheme("content")
//                                                        .authority(StubProviderApp.AUTHORITY)
//                                                        .path("Items")
//                                                        .appendQueryParameter("operation", "update")
//                                                        .appendQueryParameter("_id",id)
//                                                        .build();
//                                                getActivity().getContentResolver().notifyChange(resultUri, null);

//                                                final String webserviceQuery ="update Items set itemtax5 = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"'"+",tax_value5 ="+Float.valueOf(dialogC2_id.getText().toString())+" where _id='"+id+"'";
                                                final String webserviceQuery ="update Items set itemtax5 = '"+String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)"+"',tax_value5 ='"+Float.valueOf(dialogC2_id.getText().toString())+"' where _id='"+id+"'";
//                                                webservicequery(webserviceQuery);

                                                String where1_v1 = "docid = '" + id + "'";
                                                db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                                                //System.out.println("query2e "+webserviceQuery);

                                                //                                                db.update("Items", contentValues, where, new String[]{});
//                                            Toast.makeText(getActivity(), "itemtax5 "+String.valueOf(dialog_columnvalue), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }else {
                        if (tv.getText().toString().equals(String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)")){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("itemtax", "");
                            contentValues.put("tax_value", "");
                            String where = "_id = '" + id + "' ";
//                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                            getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                            resultUri = new Uri.Builder()
//                                    .scheme("content")
//                                    .authority(StubProviderApp.AUTHORITY)
//                                    .path("Items")
//                                    .appendQueryParameter("operation", "update")
//                                    .appendQueryParameter("_id",id)
//                                    .build();
//                            getActivity().getContentResolver().notifyChange(resultUri, null);
                            db.update("Items", contentValues, where, new String[]{});

                            final String webserviceQuery ="update Items set itemtax = '',tax_value ='' where _id='"+id+"'";
//                            webservicequery(webserviceQuery);

                            String where1_v1 = "docid = '" + id + "'";
                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                            //System.out.println("query3a "+webserviceQuery);

                        }
                        if (tv2.getText().toString().equals(String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)")){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("itemtax2", "");
                            contentValues.put("tax_value2", "");
                            String where = "_id = '" + id + "' ";
//                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                            getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                            resultUri = new Uri.Builder()
//                                    .scheme("content")
//                                    .authority(StubProviderApp.AUTHORITY)
//                                    .path("Items")
//                                    .appendQueryParameter("operation", "update")
//                                    .appendQueryParameter("_id",id)
//                                    .build();
//                            getActivity().getContentResolver().notifyChange(resultUri, null);
                            db.update("Items", contentValues, where, new String[]{});

                            final String webserviceQuery ="update Items set itemtax2 = '',tax_value2 ='' where _id='"+id+"'";
//                            webservicequery(webserviceQuery);

                            String where1_v1 = "docid = '" + id + "'";
                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                            //System.out.println("query3b "+webserviceQuery);

                        }
                        if (tv3.getText().toString().equals(String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)")){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("itemtax3", "");
                            contentValues.put("tax_value3", "");
                            String where = "_id = '" + id + "' ";
//                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                            getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                            resultUri = new Uri.Builder()
//                                    .scheme("content")
//                                    .authority(StubProviderApp.AUTHORITY)
//                                    .path("Items")
//                                    .appendQueryParameter("operation", "update")
//                                    .appendQueryParameter("_id",id)
//                                    .build();
//                            getActivity().getContentResolver().notifyChange(resultUri, null);
                            db.update("Items", contentValues, where, new String[]{});

                            final String webserviceQuery ="update Items set itemtax3 = '',tax_value3 ='' where _id='"+id+"'";
//                            webservicequery(webserviceQuery);

                            String where1_v1 = "docid = '" + id + "'";
                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                            //System.out.println("query3c "+webserviceQuery);

                        }
                        if (tv4.getText().toString().equals(String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)")){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("itemtax4", "");
                            contentValues.put("tax_value4", "");
                            String where = "_id = '" + id + "' ";
//                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                            getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                            resultUri = new Uri.Builder()
//                                    .scheme("content")
//                                    .authority(StubProviderApp.AUTHORITY)
//                                    .path("Items")
//                                    .appendQueryParameter("operation", "update")
//                                    .appendQueryParameter("_id",id)
//                                    .build();
//                            getActivity().getContentResolver().notifyChange(resultUri, null);
                            db.update("Items", contentValues, where, new String[]{});

                            final String webserviceQuery ="update Items set itemtax4 = '',tax_value4 ='' where _id='"+id+"'";
//                            webservicequery(webserviceQuery);

                            String where1_v1 = "docid = '" + id + "'";
                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                            //System.out.println("query3d "+webserviceQuery);

                        }
                        if (tv5.getText().toString().equals(String.valueOf(dialog_columnvalue)+"("+dialogC2_id.getText().toString()+"%)")){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("itemtax5", "");
                            contentValues.put("tax_value5", "");
                            String where = "_id = '" + id + "' ";
//                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                            getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                            resultUri = new Uri.Builder()
//                                    .scheme("content")
//                                    .authority(StubProviderApp.AUTHORITY)
//                                    .path("Items")
//                                    .appendQueryParameter("operation", "update")
//                                    .appendQueryParameter("_id",id)
//                                    .build();
//                            getActivity().getContentResolver().notifyChange(resultUri, null);
                            db.update("Items", contentValues, where, new String[]{});

                            final String webserviceQuery ="update Items set itemtax5 = '',tax_value5 ='' where _id='"+id+"'";
//                            webservicequery(webserviceQuery);

                            String where1_v1 = "docid = '" + id + "'";
                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                            //System.out.println("query3e "+webserviceQuery);

                        }
                    }
                }while (taxeitem_all.moveToNext());
            }
            taxeitem_all.close();

            Cursor tax_selection = db1.rawQuery("SELECT * FROM Customerdetails", null);
            if (tax_selection.moveToLast()){
                do {
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("tax1", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues1.put("tax1_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where1 = "tax1 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues1, where1, new String[]{});

                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put("tax2", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues2.put("tax2_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where2 = "tax2 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues2, where2, new String[]{});

                    ContentValues contentValues3 = new ContentValues();
                    contentValues3.put("tax3", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues3.put("tax3_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where3 = "tax3 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues3, where3, new String[]{});

                    ContentValues contentValues4 = new ContentValues();
                    contentValues4.put("tax4", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues4.put("tax4_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where4 = "tax4 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues4, where4, new String[]{});

                    ContentValues contentValues5 = new ContentValues();
                    contentValues5.put("tax5", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues5.put("tax5_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where5 = "tax5 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues5, where5, new String[]{});

                    ContentValues contentValues6 = new ContentValues();
                    contentValues6.put("tax6", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues6.put("tax6_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where6 = "tax6 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues6, where6, new String[]{});

                    ContentValues contentValues7 = new ContentValues();
                    contentValues7.put("tax7", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues7.put("tax7_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where7 = "tax7 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues7, where7, new String[]{});

                    ContentValues contentValues8 = new ContentValues();
                    contentValues8.put("tax8", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues8.put("tax8_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where8 = "tax8 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues8, where8, new String[]{});

                    ContentValues contentValues9 = new ContentValues();
                    contentValues9.put("tax9", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues9.put("tax9_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where9 = "tax9 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues9, where9, new String[]{});

                    ContentValues contentValues10 = new ContentValues();
                    contentValues10.put("tax10", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues10.put("tax10_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where10 = "tax10 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues10, where10, new String[]{});

                    ContentValues contentValues11 = new ContentValues();
                    contentValues11.put("tax11", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues11.put("tax11_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where11 = "tax11 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues11, where11, new String[]{});

                    ContentValues contentValues12 = new ContentValues();
                    contentValues12.put("tax12", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues12.put("tax12_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where12 = "tax12 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues12, where12, new String[]{});

                    ContentValues contentValues13 = new ContentValues();
                    contentValues13.put("tax13", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues13.put("tax13_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where13 = "tax13 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues13, where13, new String[]{});

                    ContentValues contentValues14 = new ContentValues();
                    contentValues14.put("tax14", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues14.put("tax14_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where14 = "tax14 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues14, where14, new String[]{});

                    ContentValues contentValues15 = new ContentValues();
                    contentValues15.put("tax15", String.valueOf(dialog_columnvalue) + "(" + dialogC2_id.getText().toString() + "%)");
                    contentValues15.put("tax15_value", String.valueOf(dialogC2_id.getText().toString()));
                    String where15 = "tax15 = '" + NAme + "' ";
                    db1.update("Customerdetails", contentValues15, where15, new String[]{});
                }while (tax_selection.moveToNext());
            }
            tax_selection.close();

//            webservicequery("update Items set status_perm='' where status_temp = ''");
            db.execSQL("update Items set status_perm='' where status_temp = ''");
            webservicequery("UPDATE Items set status_temp = '' , status_perm = ''");

//            Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
//            if (cursor.moveToFirst()){
//                do {
//                    String id = cursor.getString(0);
//                    String temp = cursor.getString(36);
//
//                    TextView tv = new TextView(getActivity());
//                    tv.setText(temp);
//
//                    if (tv.getText().toString().equals("yes")) {
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("status_temp", "");
//                        contentValues.put("status_perm", "");
//                        String where = " _id ='" + id + "'";
//                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                        getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                        resultUri = new Uri.Builder()
//                                .scheme("content")
//                                .authority(StubProviderApp.AUTHORITY)
//                                .path("Items")
//                                .appendQueryParameter("operation", "update")
//                                .appendQueryParameter("_id",id)
//                                .build();
//                        getActivity().getContentResolver().notifyChange(resultUri, null);
////                        db.update("Items", contentValues, where, new String[]{});
//
//
//                        String where1_v1 = "docid = '" + id + "'";
//                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//                    }
//                }while (cursor.moveToNext());
//            }
//            cursor.close();

            int selected1 =
                    radioGroupWebsite.getCheckedRadioButtonId();
            radioBtn1 = (RadioButton) dialog.findViewById(selected1);
            saveInDBup(Integer.parseInt(ID));

//            Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE status_temp = 'yes'", null);
//            if (cursor.moveToFirst()){
//                do {
//                    String id = cursor.getString(0);
//
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("status_temp", "");
//                    contentValues.put("status_perm", "");
//                    String where = " _id ='" + id + "'";
//                    db.update("Items", contentValues, where, new String[]{});
//                }while (cursor.moveToNext());
//            }
//            cursor.close();

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Integer file_url) {

            //hideKeyboard(getContext());

            dialog.dismiss();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            hideKeyboard(getContext());
            donotshowKeyboard(getActivity());
            Toast.makeText(getActivity(),getString(R.string.tax_saved), Toast.LENGTH_SHORT).show();

            if (radioBtn1.getText().toString().equals("Globaltax")){
                Cursor taxeitem1 = db.rawQuery("SELECT * FROM Items", null);
                //Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
                if (taxeitem1.moveToFirst()){
                    //Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
                    String five = taxeitem1.getString(5);
                    String one = taxeitem1.getString(1);
                    //if (five.toString().equals(NAme)){
//                                                        Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();
//                                                        Toast.makeText(getActivity(), " "+one, Toast.LENGTH_SHORT).show();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("itemtax", "None");
                    String where = "itemtax = '" + NAme + "' ";
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                    getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Items")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id",NAme)
                            .build();
                    getActivity().getContentResolver().notifyChange(resultUri, null);
//                    db.update("Items", contentValues, where, new String[]{});
                    //}
                    String where1_v1 = "itemtax = '" + NAme + "' ";
                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});


                }
                taxeitem1.close();
            }else {

            }

            if (!selected.equals("All")) {
                //Toast.makeText(getActivity(), " " + selected, Toast.LENGTH_SHORT).show();
                countryList = new ArrayList<Country1>();
                try {
                    db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor allrows = db.rawQuery("SELECT * FROM Taxes WHERE taxtype = '" + selected + "'", null);
                    System.out.println("COUNT : " + allrows.getCount());


                    //Country1 country = new Country1(name, name, name, name);

                    if (allrows.moveToFirst()) {
                        do {
                            String ID = allrows.getString(0);
                            String NAme = allrows.getString(1);
                            String PlaCe = allrows.getString(2);
                            if (NAme.toString().equals("None")){

                            }else {
                                Country1 NAME = new Country1(ID, NAme);
                                //Country1 PLACE = new Country1(PlaCe, PlaCe, PlaCe, PlaCe);
                                countryList.add(NAME);
                                //countryList.add(PLACE);
                            }
                        } while (allrows.moveToNext());
                    }
                    allrows.close();
                    //db.close();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error encountered.",
                            Toast.LENGTH_LONG);
                }
            }
            if (selected.equals("All")) {
                //Toast.makeText(getActivity(), " " + selected, Toast.LENGTH_SHORT).show();
                countryList = new ArrayList<Country1>();
                try {
                    db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor allrows = db.rawQuery("SELECT * FROM Taxes ", null);
                    System.out.println("COUNT : " + allrows.getCount());


                    //Country1 country = new Country1(name, name, name, name);

                    if (allrows.moveToFirst()) {
                        do {
                            String ID = allrows.getString(0);
                            String NAme = allrows.getString(1);
                            String PlaCe = allrows.getString(2);
                            if (NAme.toString().equals("None")){

                            }else {
                                Country1 NAME = new Country1(ID, NAme);
                                //Country1 PLACE = new Country1(PlaCe, PlaCe, PlaCe, PlaCe);
                                countryList.add(NAME);
                                //countryList.add(PLACE);
                            }
                        } while (allrows.moveToNext());
                    }
                    allrows.close();
                    //db.close();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error encountered.",
                            Toast.LENGTH_LONG);
                }
            }


            dataAdapter = new MyCustomAdapter(getActivity(),
                    R.layout.items_deletionbox, countryList);
            final ListView listView = (ListView) rootview.findViewById(R.id.listView);
            // Assign adapter to ListView
            listView.setAdapter(dataAdapter);

            progressbar.setVisibility(View.GONE);
        }
    }

    class DownloadMusicfromInternetde_update01 extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            try {
//                Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
//                if (cursor.moveToFirst()) {
//                    do {
//                        String id = cursor.getString(0);
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("status_temp", "");
//                        contentValues.put("status_perm", "");
//                        String where = "_id = '"+id+"' ";
//                        db.update("Items", contentValues, where, new String[]{});
//                    } while (cursor.moveToNext());
//                }
//
//                cursor.close();

                webservicequery("UPDATE Items SET status_temp = '', status_perm = ''");
                db.execSQL("UPDATE Items SET status_temp = '' WHERE status_temp = 'yes'");
                db.execSQL("UPDATE Items SET status_perm = '' WHERE status_temp = 'yes'");

//                Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE status_temp = 'yes'", null);
//                if (cursor.moveToFirst()){
//                    do {
////                            String id = cursor.getString(1);
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("status_temp", "");
//                        contentValues.put("status_perm", "");
//                        String where = "status_temp = 'yes' ";
//                        db.update("Items", contentValues, where, new String[]{});
//
//                        String where1_v1 = "status_temp = 'yes' ";
//                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//                    }while (cursor.moveToNext());
//                }
//                cursor.close();

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

            progressbar_dialog.setVisibility(View.VISIBLE);
            header_dialog.setVisibility(View.INVISIBLE);
            content_dialog.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(Integer file_url) {
            DownloadMusicfromInternetde_update0 downloadMusicfromInternetde_update0 = new DownloadMusicfromInternetde_update0();
            downloadMusicfromInternetde_update0.execute();
        }
    }


    class DownloadMusicfromInternetde_update0 extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            try {

                webservicequery("UPDATE Items SET status_temp = 'yes', status_perm = 'yes' WHERE itemtax = '"+NAme+"' OR itemtax2 = '"+NAme+"' OR itemtax3 = '"+NAme+"' OR itemtax4 = '"+NAme+"' OR itemtax5 = '"+NAme+"'");
                db.execSQL("UPDATE Items SET status_temp = 'yes' WHERE itemtax = '"+NAme+"'");
                db.execSQL("UPDATE Items SET status_perm = 'yes' WHERE itemtax = '"+NAme+"'");

                db.execSQL("UPDATE Items SET status_temp = 'yes' WHERE itemtax2 = '"+NAme+"'");
                db.execSQL("UPDATE Items SET status_perm = 'yes' WHERE itemtax2 = '"+NAme+"'");

                db.execSQL("UPDATE Items SET status_temp = 'yes' WHERE itemtax3 = '"+NAme+"'");
                db.execSQL("UPDATE Items SET status_perm = 'yes' WHERE itemtax3 = '"+NAme+"'");

                db.execSQL("UPDATE Items SET status_temp = 'yes' WHERE itemtax4 = '"+NAme+"'");
                db.execSQL("UPDATE Items SET status_perm = 'yes' WHERE itemtax4 = '"+NAme+"'");

                db.execSQL("UPDATE Items SET status_temp = 'yes' WHERE itemtax5 = '"+NAme+"'");
                db.execSQL("UPDATE Items SET status_perm = 'yes' WHERE itemtax5 = '"+NAme+"'");

//                Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE itemtax = '"+NAme+"'", null);
//                if (cursor.moveToFirst()){
//                    do {
//                        String id = cursor.getString(0);
//                        System.out.println("itemname is "+id);
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("status_temp", "yes");
//                        contentValues.put("status_perm", "yes");
//                        String where = " _id ='" + id + "'";
//                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                        getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                        resultUri = new Uri.Builder()
//                                .scheme("content")
//                                .authority(StubProviderApp.AUTHORITY)
//                                .path("Items")
//                                .appendQueryParameter("operation", "update")
//                                .appendQueryParameter("_id",id)
//                                .build();
//                        getActivity().getContentResolver().notifyChange(resultUri, null);
////                        db.update("Items", contentValues, where, new String[]{});
//
//                        String where1_v1 = "docid = '" + id + "'";
//                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//
//                    }while (cursor.moveToNext());
//                }
//                cursor.close();
//
//                Cursor cursor2 = db.rawQuery("SELECT * FROM Items WHERE itemtax2 = '"+NAme+"'", null);
//                if (cursor2.moveToFirst()){
//                    do {
//                        String id = cursor2.getString(0);
//                        System.out.println("itemname is2 "+id);
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("status_temp", "yes");
//                        contentValues.put("status_perm", "yes");
//                        String where = " _id ='" + id + "'";
//                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                        getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                        resultUri = new Uri.Builder()
//                                .scheme("content")
//                                .authority(StubProviderApp.AUTHORITY)
//                                .path("Items")
//                                .appendQueryParameter("operation", "update")
//                                .appendQueryParameter("_id",id)
//                                .build();
//                        getActivity().getContentResolver().notifyChange(resultUri, null);
////                        db.update("Items", contentValues, where, new String[]{});
//
//                        String where1_v1 = "docid = '" + id + "'";
//                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//
//                    }while (cursor2.moveToNext());
//                }
//                cursor2.close();
//
//                Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE itemtax3 = '"+NAme+"'", null);
//                if (cursor3.moveToFirst()){
//                    do {
//                        String id = cursor3.getString(0);
//                        System.out.println("itemname is3 "+id);
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("status_temp", "yes");
//                        contentValues.put("status_perm", "yes");
//                        String where = " _id ='" + id + "'";
//                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                        getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                        resultUri = new Uri.Builder()
//                                .scheme("content")
//                                .authority(StubProviderApp.AUTHORITY)
//                                .path("Items")
//                                .appendQueryParameter("operation", "update")
//                                .appendQueryParameter("_id",id)
//                                .build();
//                        getActivity().getContentResolver().notifyChange(resultUri, null);
////                        db.update("Items", contentValues, where, new String[]{});
//                        String where1_v1 = "docid = '" + id + "'";
//                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//
//                    }while (cursor3.moveToNext());
//                }
//                cursor3.close();
//
//                Cursor cursor4 = db.rawQuery("SELECT * FROM Items WHERE itemtax4 = '"+NAme+"'", null);
//                if (cursor4.moveToFirst()){
//                    do {
//                        String id = cursor4.getString(0);
//                        System.out.println("itemname is4 "+id);
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("status_temp", "yes");
//                        contentValues.put("status_perm", "yes");
//                        String where = " _id ='" + id + "'";
//                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                        getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                        resultUri = new Uri.Builder()
//                                .scheme("content")
//                                .authority(StubProviderApp.AUTHORITY)
//                                .path("Items")
//                                .appendQueryParameter("operation", "update")
//                                .appendQueryParameter("_id",id)
//                                .build();
//                        getActivity().getContentResolver().notifyChange(resultUri, null);
////                        db.update("Items", contentValues, where, new String[]{});
//
//                        String where1_v1 = "docid = '" + id + "'";
//                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//                    }while (cursor4.moveToNext());
//                }
//                cursor4.close();
//
//                Cursor cursor5 = db.rawQuery("SELECT * FROM Items WHERE itemtax5 = '"+NAme+"'", null);
//                if (cursor5.moveToFirst()){
//                    do {
//                        String id = cursor5.getString(0);
//                        System.out.println("itemname is5 "+id);
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("status_temp", "yes");
//                        contentValues.put("status_perm", "yes");
//                        String where = " _id ='" + id + "'";
//                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                        getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                        resultUri = new Uri.Builder()
//                                .scheme("content")
//                                .authority(StubProviderApp.AUTHORITY)
//                                .path("Items")
//                                .appendQueryParameter("operation", "update")
//                                .appendQueryParameter("_id",id)
//                                .build();
//                        getActivity().getContentResolver().notifyChange(resultUri, null);
////                        db.update("Items", contentValues, where, new String[]{});
//
//                        String where1_v1 = "docid = '" + id + "'";
//                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//
//                    }while (cursor5.moveToNext());
//                }
//                cursor5.close();

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

//            progressbar_dialog.setVisibility(View.VISIBLE);
//            header_dialog.setVisibility(View.INVISIBLE);
//            content_dialog.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(Integer file_url) {
            DownloadMusicfromInternetde_update downloadMusicfromInternetde_update = new DownloadMusicfromInternetde_update();
            downloadMusicfromInternetde_update.execute();
        }
    }

    class DownloadMusicfromInternetde_update extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            try {
//                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                Cursor allrows1 = db.rawQuery("SELECT * FROM Taxes WHERE taxname = '" + country_dialog.getName() + "' AND _id = '" + country_dialog.getCode() + "' ", null);
                if (allrows1.moveToFirst()) {
                    do {
                        ID = allrows1.getString(0);
                        NAme = allrows1.getString(1);
                        PRice = allrows1.getString(2);
                        QUan = allrows1.getString(3);
                        NAme_HSN = allrows1.getString(8);

//                                Toast.makeText(getActivity(), " "+NAme+" "+PRice+" "+NAme_HSN, Toast.LENGTH_LONG).show();

                        String tax1 = allrows1.getString(5);
                        String tax2 = allrows1.getString(6);
                        String tax3 = allrows1.getString(7);

                        TextView tv_tax1 = new TextView(getActivity());
                        tv_tax1.setText(tax1);
                        TextView tv_tax2 = new TextView(getActivity());
                        tv_tax2.setText(tax2);
                        TextView tv_tax3 = new TextView(getActivity());
                        tv_tax3.setText(tax3);

                        dialog_select_dine_in = (CheckBox) dialog.findViewById(R.id.select_dine_in);
                        dialog_select_take_away = (CheckBox) dialog.findViewById(R.id.select_take_away);
                        dialog_select_home_delivery = (CheckBox) dialog.findViewById(R.id.select_home_delivery);
                        if (tv_tax1.getText().toString().equals("dine_in")) {
                            dialog_select_dine_in.setChecked(true);
                        } else {
                            dialog_select_dine_in.setChecked(false);
                        }

                        if (tv_tax2.getText().toString().equals("takeaway")) {
                            dialog_select_take_away.setChecked(true);
                        } else {
                            dialog_select_take_away.setChecked(false);
                        }

                        if (tv_tax3.getText().toString().equals("homedelivery")) {
                            dialog_select_home_delivery.setChecked(true);
                        } else {
                            dialog_select_home_delivery.setChecked(false);
                        }

//                                Id.setText(String.valueOf(ID));
//                                iname.setText(String.valueOf(NAme));
//                                iprice.setText(String.valueOf(PRice));
//                                iquan.setText(String.valueOf(QUan));

//                        dialogC1_id = (EditText) dialog.findViewById(R.id.editText1);
//                        layouttaxname_dialog = (TextInputLayout) dialog.findViewById(R.id.layouttaxname_dialog);
//
//                        if (NAme.contains("(")) {
//                            String match = "(";
//                            int position_taxname = NAme.indexOf(match);
//                            String mod2 = NAme.substring(0, position_taxname);
//                            dialogC1_id.setText(mod2);
//                        } else {
//                            dialogC1_id.setText(NAme);
//                        }
//
//                        dialogC1_id_hsn = (EditText) dialog.findViewById(R.id.editText1_HSN);
//                        dialogC1_id_hsn.setText(NAme_HSN);
//
//                        layoutHSNname_dialog = (TextInputLayout) dialog.findViewById(R.id.layoutHSNname_dialog);
//
//                        dialog_columnvalue = NAme;
//                        if (NAme.toString().contains("'")) {
//                            dialog_columnvalue = NAme.toString().replaceAll("'", " ");
//                        }
//
//                        dialog_select_items = (LinearLayout) dialog.findViewById(R.id.select_items);
//
////                        dialogC1_id.addTextChangedListener(new TextWatcher() {
////
////                            public void afterTextChanged(Editable s) {
////                            }
////
////                            public void beforeTextChanged(CharSequence s, int start,
////                                                          int count, int after) {
////                            }
////
////                            public void onTextChanged(CharSequence s, int start,
////                                                      int before, int count) {
////                                layouttaxname_dialog.setError(null);
////                            }
////                        });
//
//                        dialogC2_id = (EditText) dialog.findViewById(R.id.editText2);
//                        layoutvalue_dialog = (TextInputLayout) dialog.findViewById(R.id.layoutvalue_dialog);
//                        dialogC2_id.setText(String.valueOf(PRice));
//
////                        dialogC2_id.addTextChangedListener(new TextWatcher() {
////
////                            public void afterTextChanged(Editable s) {
////                            }
////
////                            public void beforeTextChanged(CharSequence s, int start,
////                                                          int count, int after) {
////                            }
////
////                            public void onTextChanged(CharSequence s, int start,
////                                                      int before, int count) {
////                                layoutvalue_dialog.setError(null);
////                            }
////                        });
//
//                        radioGroupWebsite = (RadioGroup) dialog.findViewById
//                                (R.id.radioGroup1);
//                        if (QUan.equals("Globaltax")) {
//                            radioGroupWebsite.check(R.id.btnglobal);
//                            layoutHSNname_dialog.setVisibility(View.GONE);
//                            dialog_select_items.setVisibility(View.GONE);
//                        } else {
//                            radioGroupWebsite.check(R.id.btnitem);
//                            layoutHSNname_dialog.setVisibility(View.VISIBLE);
//                            dialog_select_items.setVisibility(View.VISIBLE);
//                        }
//
//                        final RadioButton global = (RadioButton) dialog.findViewById(R.id.btnglobal);
//                        final LinearLayout tax_seletion = (LinearLayout) dialog.findViewById(R.id.tax_seletion);
//
//
//
////                        if (global.isChecked()) {
////                            tax_seletion.setVisibility(View.VISIBLE);
////                            layoutHSNname_dialog.setVisibility(View.GONE);
////                        } else {
////                            tax_seletion.setVisibility(View.GONE);
////                            layoutHSNname_dialog.setVisibility(View.VISIBLE);
////                        }
//
//                        final RadioButton itemtaxx = (RadioButton) dialog.findViewById(R.id.btnitem);
//                        itemtaxx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                            @Override
//                            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
//                                if (ischecked) {
//                                    tax_seletion.setVisibility(View.GONE);
//                                    layoutHSNname_dialog.setVisibility(View.VISIBLE);
//                                } else {
//                                    tax_seletion.setVisibility(View.VISIBLE);
//                                    layoutHSNname_dialog.setVisibility(View.GONE);
//                                }
//                            }
//                        });
//
//
//
//
//                        dialog_spinneritem = (TextView) dialog.findViewById(R.id.tax);
//
////                                Cursor cursor1 = db.rawQuery("SELECT COUNT(taxname) FROM Items WHERE taxname = '"+mod2+"'", null);
//                        String qty = "0", qty2 = "0", qty3 = "0", qty4 = "0", qty5 = "0";
//                        Cursor cursor22 = db.rawQuery("SELECT COUNT(itemtax) from Items WHERE itemtax = '" + NAme + "'", null);
//                        if (cursor22.moveToFirst()) {
//                            int leveliss = cursor22.getInt(0);
//                            qty = String.valueOf(leveliss);
//                        }
//                        cursor22.close();
//
//                        Cursor cursor222 = db.rawQuery("SELECT COUNT(itemtax2) from Items WHERE itemtax2 = '" + NAme + "'", null);
//                        if (cursor222.moveToFirst()) {
//                            int leveliss = cursor222.getInt(0);
//                            qty2 = String.valueOf(leveliss);
//                        }
//                        cursor222.close();
//
//                        Cursor cursor223 = db.rawQuery("SELECT COUNT(itemtax3) from Items WHERE itemtax3 = '" + NAme + "'", null);
//                        if (cursor223.moveToFirst()) {
//                            int leveliss = cursor223.getInt(0);
//                            qty3 = String.valueOf(leveliss);
//                        }
//                        cursor223.close();
//
//                        Cursor cursor224 = db.rawQuery("SELECT COUNT(itemtax4) from Items WHERE itemtax4 = '" + NAme + "'", null);
//                        if (cursor224.moveToFirst()) {
//                            int leveliss = cursor224.getInt(0);
//                            qty4 = String.valueOf(leveliss);
//                        }
//                        cursor224.close();
//
//                        Cursor cursor225 = db.rawQuery("SELECT COUNT(itemtax5) from Items WHERE itemtax5 = '" + NAme + "'", null);
//                        if (cursor225.moveToFirst()) {
//                            int leveliss = cursor225.getInt(0);
//                            qty5 = String.valueOf(leveliss);
//                        }
//                        cursor225.close();
//
//                        float a3 = Float.parseFloat(qty) + Float.parseFloat(qty2) + Float.parseFloat(qty3) + Float.parseFloat(qty4) + Float.parseFloat(qty5);
//                        n4 = Math.round(a3);
//
//                        dialog_spinneritem.setText(String.valueOf(n4)+" selected");
//
//                        //dialogC4_id.setSelection(getIndex(dialogC4_id, CAte));


                    } while (allrows1.moveToNext());
                }

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

//            progressbar_dialog.setVisibility(View.VISIBLE);
//            header_dialog.setVisibility(View.INVISIBLE);
//            content_dialog.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(Integer file_url) {

            progressbar_dialog.setVisibility(View.GONE);
            header_dialog.setVisibility(View.VISIBLE);
            content_dialog.setVisibility(View.VISIBLE);

            dialogC1_id = (EditText) dialog.findViewById(R.id.editText1);
            dialogC2_id = (EditText) dialog.findViewById(R.id.editText2);
            layoutvalue_dialog = (TextInputLayout) dialog.findViewById(R.id.layoutvalue_dialog);
            layouttaxname_dialog = (TextInputLayout) dialog.findViewById(R.id.layouttaxname_dialog);
            dialogC1_id_hsn = (EditText) dialog.findViewById(R.id.editText1_HSN);
            layoutHSNname_dialog = (TextInputLayout) dialog.findViewById(R.id.layoutHSNname_dialog);
            dialog_select_items = (LinearLayout) dialog.findViewById(R.id.select_items);
            dialog_spinneritem = (TextView) dialog.findViewById(R.id.tax);

            dialog_select_dine_in = (CheckBox)dialog.findViewById(R.id.select_dine_in);
            dialog_select_take_away = (CheckBox)dialog.findViewById(R.id.select_take_away);
            dialog_select_home_delivery = (CheckBox)dialog.findViewById(R.id.select_home_delivery);

            if (NAme.contains("(")) {
                String match = "(";
                int position_taxname = NAme.indexOf(match);
                String mod2 = NAme.substring(0, position_taxname);
                dialogC1_id.setText(mod2);
            } else {
                dialogC1_id.setText(NAme);
            }

            String qty = "0", qty2 = "0", qty3 = "0", qty4 = "0", qty5 = "0";
            Cursor cursor22 = db.rawQuery("SELECT COUNT(itemtax) from Items WHERE itemtax = '" + NAme + "'", null);
            if (cursor22.moveToFirst()) {
                int leveliss = cursor22.getInt(0);
                qty = String.valueOf(leveliss);
            }
            cursor22.close();

            Cursor cursor222 = db.rawQuery("SELECT COUNT(itemtax2) from Items WHERE itemtax2 = '" + NAme + "'", null);
            if (cursor222.moveToFirst()) {
                int leveliss = cursor222.getInt(0);
                qty2 = String.valueOf(leveliss);
            }
            cursor222.close();

            Cursor cursor223 = db.rawQuery("SELECT COUNT(itemtax3) from Items WHERE itemtax3 = '" + NAme + "'", null);
            if (cursor223.moveToFirst()) {
                int leveliss = cursor223.getInt(0);
                qty3 = String.valueOf(leveliss);
            }
            cursor223.close();

            Cursor cursor224 = db.rawQuery("SELECT COUNT(itemtax4) from Items WHERE itemtax4 = '" + NAme + "'", null);
            if (cursor224.moveToFirst()) {
                int leveliss = cursor224.getInt(0);
                qty4 = String.valueOf(leveliss);
            }
            cursor224.close();

            Cursor cursor225 = db.rawQuery("SELECT COUNT(itemtax5) from Items WHERE itemtax5 = '" + NAme + "'", null);
            if (cursor225.moveToFirst()) {
                int leveliss = cursor225.getInt(0);
                qty5 = String.valueOf(leveliss);
            }
            cursor225.close();

            float a3 = Float.parseFloat(qty)+Float.parseFloat(qty2)+Float.parseFloat(qty3)+Float.parseFloat(qty4)+Float.parseFloat(qty5);
            n4 = Math.round(a3);
            System.out.println("n4 value is "+String.valueOf(n4));
            dialog_spinneritem.setText(String.valueOf(n4)+" selected");

            dialogC1_id_hsn.setText(NAme_HSN);
            dialogC2_id.setText(String.valueOf(PRice));

            radioGroupWebsite = (RadioGroup) dialog.findViewById
                    (R.id.radioGroup1);
            if (QUan.equals("Globaltax")) {
                radioGroupWebsite.check(R.id.btnglobal);
                layoutHSNname_dialog.setVisibility(View.GONE);
                dialog_select_items.setVisibility(View.GONE);
            }
            else {
                radioGroupWebsite.check(R.id.btnitem);
                layoutHSNname_dialog.setVisibility(View.VISIBLE);
                dialog_select_items.setVisibility(View.VISIBLE);
            }

            radioGroupWebsite.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
                public void onCheckedChanged(RadioGroup group, int checkedId)
                {
                    // This will get the radiobutton that has changed in its check state
                    RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                    // This puts the value (true/false) into the variable
                    if (checkedRadioButton.getText().toString().equals("Globaltax")){
                        layoutHSNname_dialog.setVisibility(View.GONE);
                        dialog_select_items.setVisibility(View.GONE);
                    }else {
                        layoutHSNname_dialog.setVisibility(View.VISIBLE);
                        dialog_select_items.setVisibility(View.VISIBLE);
                    }
                }
            });

            final RadioButton global = (RadioButton)dialog.findViewById(R.id.btnglobal);
            final LinearLayout tax_seletion = (LinearLayout) dialog.findViewById(R.id.tax_seletion);

            if (global.isChecked()){
                tax_seletion.setVisibility(View.VISIBLE);
                layoutHSNname_dialog.setVisibility(View.GONE);
            }else {
                tax_seletion.setVisibility(View.GONE);
                layoutHSNname_dialog.setVisibility(View.VISIBLE);
            }

            final RadioButton itemtaxx = (RadioButton)dialog.findViewById(R.id.btnitem);
            itemtaxx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                    if (ischecked){
                        tax_seletion.setVisibility(View.GONE);
                        layoutHSNname_dialog.setVisibility(View.VISIBLE);
                    }else {
                        tax_seletion.setVisibility(View.VISIBLE);
                        layoutHSNname_dialog.setVisibility(View.GONE);
                    }
                }
            });

            dialog_spinneritem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getActivity(), Taxes_Multiselection_Items.class);
                    intent.putExtra("PLAYER1NAME", "update");
                    intent.putExtra("taxname", NAme);
                    startActivityForResult(intent, 1);

                }
            });

            Button delete = (Button) dialog.findViewById(R.id.btndelete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog dd = new Dialog(getActivity(), R.style.notitle);
                    dd.setContentView(R.layout.deleted_tax_selected);


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
                            dd.dismiss();
                            dialog.dismiss();

                            DownloadMusicfromInternetde_delete downloadMusicfromInternetde_delete = new DownloadMusicfromInternetde_delete();
                            downloadMusicfromInternetde_delete.execute();
                        }
                    });

                    dd.show();


                }
            });

//                                final ImageView frameLayout = (ImageView) dialog.findViewById(R.id.closetext);
//                                frameLayout.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog.dismiss();
//                                        Toast.makeText(getActivity(), "q", Toast.LENGTH_SHORT).show();
//                                        //dialog.windowSoftInputMode="stateAlwaysHidden"
//                                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//
//                                    }
//                                });

//            DownloadMusicfromInternetde_a downloadMusicfromInternetde_a = new DownloadMusicfromInternetde_a();
//            downloadMusicfromInternetde_a.execute();

//                                Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE itemtax = '"+NAme+"' OR itemtax2 = '"+NAme+"' OR itemtax3 = '"+NAme+"' OR itemtax4 = '"+NAme+"' OR itemtax5 = '"+NAme+"'", null);
//                                if (cursor.moveToFirst()){
//                                    do {
//                                        String id1 = cursor.getString(0);
//                                        System.out.println("itemname is2 "+id);
//                                        ContentValues contentValues = new ContentValues();
//                                        contentValues.put("status_temp", "yes");
//                                        contentValues.put("status_perm", "yes");
//                                        String where = " _id ='" + id1 + "'";
//                                        db.update("Items", contentValues, where, new String[]{});
//                                    }while (cursor.moveToNext());
//                                }

            Button save = (Button) dialog.findViewById(R.id.btnsave);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getActivity(), "image1", Toast.LENGTH_SHORT).show();

                    dialog_columnvalue = dialogC1_id.getText().toString();
                    if (dialog_columnvalue.toString().contains("'")) {
                        dialog_columnvalue  = dialogC1_id.getText().toString().replaceAll("'", " ");
                    }

                    if (NAme.toString().equals(dialogC1_id.getText().toString())) {
//                                            Cursor taxeitem1 = db.rawQuery("SELECT * FROM Items", null);
//                                            //Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
//                                            if (taxeitem1.moveToFirst()){
//                                                //Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
//                                                String five = taxeitem1.getString(5);
//                                                String one = taxeitem1.getString(1);
//                                                //if (five.toString().equals(NAme)){
////                                                Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();
////                                                Toast.makeText(getActivity(), " "+one, Toast.LENGTH_SHORT).show();
//                                                ContentValues contentValues = new ContentValues();
//                                                contentValues.put("itemtax", String.valueOf(dialog_columnvalue));
//                                                String where = "itemtax = '" + NAme + "' ";
//                                                db.update("Items", contentValues, where, new String[]{});
//                                                //}
//                                            }

                        dialog.dismiss();

                        DownloadMusicfromInternetde_update_global downloadMusicfromInternetde_insert = new DownloadMusicfromInternetde_update_global();
                        downloadMusicfromInternetde_insert.execute();

                    }else {
                        Cursor itemnamecheck = db.rawQuery("SELECT * FROM Taxes WHERE taxname = '"+dialog_columnvalue+"'", null);
                        if (itemnamecheck.moveToFirst()){
                            layouttaxname_dialog.setError("Tax name already in use");
                        }else {
//                                                Cursor taxeitem1 = db.rawQuery("SELECT * FROM Items", null);
//                                                //Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
//                                                if (taxeitem1.moveToFirst()){
//                                                    //Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
//                                                    String five = taxeitem1.getString(5);
//                                                    String one = taxeitem1.getString(1);
//                                                    //if (five.toString().equals(NAme)){
////                                                    Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();
////                                                    Toast.makeText(getActivity(), " "+one, Toast.LENGTH_SHORT).show();
//                                                    ContentValues contentValues = new ContentValues();
//                                                    contentValues.put("itemtax", String.valueOf(dialog_columnvalue));
//                                                    String where = "itemtax = '" + NAme + "' ";
//                                                    db.update("Items", contentValues, where, new String[]{});
//                                                    //}
//                                                }

                            dialog.dismiss();

                            webservicequery("UPDATE Items set itemtax = '' , tax_value = '' WHERE itemtax = '"+NAme+"'");
                            webservicequery("UPDATE Items set itemtax2 = '' , tax_value2 = '' WHERE itemtax2 = '"+NAme+"'");
                            webservicequery("UPDATE Items set itemtax3 = '' , tax_value3 = '' WHERE itemtax3 = '"+NAme+"'");
                            webservicequery("UPDATE Items set itemtax4 = '' , tax_value4 = '' WHERE itemtax4 = '"+NAme+"'");
                            webservicequery("UPDATE Items set itemtax5 = '' , tax_value5 = '' WHERE itemtax5 = '"+NAme+"'");

                            DownloadMusicfromInternetde_update_item downloadMusicfromInternetde_insert = new DownloadMusicfromInternetde_update_item();
                            downloadMusicfromInternetde_insert.execute();

//                            progressbar.setVisibility(View.VISIBLE);
//                            progress_text.setText("Updating Tax...");
//
////                            SparseBooleanArray checked = listView.getCheckedItemPositions();
//
//                            ForegroundService1_update_tax foreground  = new ForegroundService1_update_tax();
//                            Intent serviceIntent  = new Intent(getActivity(), foreground.getClass());
//
//                            if (!isMyServiceRunning(foreground.getClass())) {
//
//                                serviceIntent.putExtra("inputExtra", "taxes update service");
////                                serviceIntent.putExtra("count",listView.getCount());
////                                serviceIntent.putExtra("checked",new SparseBooleanArrayParcelable(checked));
////                                serviceIntent.putExtra("country",countryList);
//                                Log.e("before service start","before service start1");
//                                ContextCompat.startForegroundService(getActivity(), serviceIntent);
//
//                            }

                        }
                        itemnamecheck.close();
                    }

                }
            });
        }
    }


    public void webservicequery(final String webserviceQuery){


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getContext());
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
        RequestQueue queue;
        StringRequest sr1;
        // queue = Volley.newRequestQueue(getActivity());

        queue= RequestSingleton.getInstance(getActivity()).getInstance();

        sr1 = new StringRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl + "webservicequery.php",
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
                System.out.println("query1 "+webserviceQuery);
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


    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void tax_insert_server(String taxna, String taxva) {


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getContext());
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
//        JSONObject params = new JSONObject();
//
//        try {
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"insert_tax_multiple.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("response tax "+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                               /*     params.put("email", email + "");
                                    params.put("password", password + "");*/
                params.put("device",device);
                params.put("store",store);
                params.put("company",company);
                params.put("taxna",taxna);
                params.put("taxva",taxva);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

    }

    public void tax_update_server(String taxna, String taxva, String NAme) {


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getContext());
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
//        JSONObject params = new JSONObject();
//
//        try {
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"update_tax_multiple1.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("response tax "+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                               /*     params.put("email", email + "");
                                    params.put("password", password + "");*/
                params.put("device",device);
                params.put("store",store);
                params.put("company",company);
                params.put("taxna",taxna);
                params.put("taxva",taxva);
                params.put("tax_old",NAme);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getExtras().getString("items")!=null){
                alertDialogItems = new AlertDialog.Builder(getActivity()).create();
                alertDialogItems.setTitle("Items Updating");
                alertDialogItems.setMessage(getString(R.string.setmessage12));
                alertDialogItems.show();

                db.execSQL("delete from Items_Virtual");

            }else if(intent.getExtras().getString("stop")!=null){
                alertDialogItems.dismiss();
            }


        }
    };

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("myFunction"));
        super.onResume();

    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onPause();

    }

    @Override
    public void onPtrReceive(final Printer printerObj, final int code, final PrinterStatusInfo status, final String printJobId) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public synchronized void run() {
//                ShowMsg.showResult(code, makeErrorMessage(status), mContext);

                dispPrinterWarnings(status);

//                updateButtonState(true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        disconnectPrinter();
                    }
                }).start();
            }
        });
    }

    private String makeErrorMessage(PrinterStatusInfo status) {
        String msg = "";

        if (status.getOnline() == Printer.FALSE) {
            msg += getString(R.string.handlingmsg_err_offline);
        }
        if (status.getConnection() == Printer.FALSE) {
            msg += getString(R.string.handlingmsg_err_no_response);
        }
        if (status.getCoverOpen() == Printer.TRUE) {
            msg += getString(R.string.handlingmsg_err_cover_open);
        }
        if (status.getPaper() == Printer.PAPER_EMPTY) {
            msg += getString(R.string.handlingmsg_err_receipt_end);
        }
        if (status.getPaperFeed() == Printer.TRUE || status.getPanelSwitch() == Printer.SWITCH_ON) {
            msg += getString(R.string.handlingmsg_err_paper_feed);
        }
        if (status.getErrorStatus() == Printer.MECHANICAL_ERR || status.getErrorStatus() == Printer.AUTOCUTTER_ERR) {
            msg += getString(R.string.handlingmsg_err_autocutter);
            msg += getString(R.string.handlingmsg_err_need_recover);
        }
        if (status.getErrorStatus() == Printer.UNRECOVER_ERR) {
            msg += getString(R.string.handlingmsg_err_unrecover);
        }
        if (status.getErrorStatus() == Printer.AUTORECOVER_ERR) {
            if (status.getAutoRecoverError() == Printer.HEAD_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_head);
            }
            if (status.getAutoRecoverError() == Printer.MOTOR_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_motor);
            }
            if (status.getAutoRecoverError() == Printer.BATTERY_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_battery);
            }
            if (status.getAutoRecoverError() == Printer.WRONG_PAPER) {
                msg += getString(R.string.handlingmsg_err_wrong_paper);
            }
        }
        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_0) {
            msg += getString(R.string.handlingmsg_err_battery_real_end);
        }

        return msg;
    }

    private void dispPrinterWarnings(PrinterStatusInfo status) {
//        EditText edtWarnings = (EditText) findViewById(R.id.edtWarnings);
        String warningsMsg = "";

        if (status == null) {
            return;
        }

        if (status.getPaper() == Printer.PAPER_NEAR_END) {
            warningsMsg += getString(R.string.handlingmsg_warn_receipt_near_end);
        }

        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_1) {
            warningsMsg += getString(R.string.handlingmsg_warn_battery_near_end);
        }

//        edtWarnings.setText(warningsMsg);
    }

    private void disconnectPrinter() {
//        if (mPrinter == null) {
//            return;
//        }

        try {
            mPrinter.endTransaction();
        } catch (final Exception e) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
//                    Toast.makeText(getActivity(), "Here6", Toast.LENGTH_SHORT).show();
//                    ShowMsg.showException(e, "endTransaction", mContext);
                }
            });
        }

//        try {
//            mPrinter.disconnect();
//        } catch (final Exception e) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public synchronized void run() {
////                    Toast.makeText(getActivity(), "Here7", Toast.LENGTH_SHORT).show();
//                    ShowMsg.showException(e, "disconnect", mContext);
//                }
//            });
//        }

        finalizeObject();
    }

    private void finalizeObject() {
//        if (mPrinter == null) {
//            return;
//        }
//
//        mPrinter.clearCommandBuffer();
//
//        mPrinter.setReceiveEventListener(null);
//
//        mPrinter = null;
    }

}