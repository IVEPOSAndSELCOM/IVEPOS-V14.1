package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 1/6/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.intuition.ivepos.csv.RequestSingleton;
import com.intuition.ivepos.syncapp.StubProviderApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;
import static com.intuition.ivepos.sync.SyncHelper.mAccount;
import static com.intuition.ivepos.syncapp.SyncHelperApp.AUTHORITY;


/**
 * Created by Rohithkumar on 1/6/2015.
 */
public class DatabasecategoryActivity extends Fragment {


    Fragment frag;
    FragmentTransaction fragTransaction;

    LoginDataBaseAdapter loginDataBaseAdapter;
    SimpleCursorAdapter dataadapter;
    ListView listView;
    public SQLiteDatabase db = null;
    public Cursor cursor, cursors;
    private EditText empNameEtxt;
    private LinearLayout submitLayout;
    protected SQLiteDatabase database;
    private static final String WHERE_ID_EQUALS = "_id"+ " =?";
    DatabasecategoryActivity databasecategorycativity;
    private String name;
    ContentValues cv;
    DataBaseHelper dbhelper;
    public Spinner spinner;
    TextView textView;
    RelativeLayout progressbar;
    ProgressBar circle;
    TextView progress_text;

    ArrayList<String> list = new ArrayList<String>();
    String path[] = {"Laptops","DesktopPC","Tablets","Add-Ons","Gaming"};

    public static ArrayList<String> ArrayofName = new ArrayList<String>();
    public static final String ARG_ITEM_ID = "Create_category_fragment";


    ArrayList<Country_category> countryList;
    MyCustomAdapter dataAdapter;
    Dialog dialog;
    EditText dialogC1_id, priority_edittext; String VAL_prIO;
    TextInputLayout layoutcatname_dialog, layoutcatname, layout_prior_name, layoutpriority_dialog ;
    RelativeLayout linearLayout;
    FloatingActionButton additem;
    EditText text, priority_one; String columnvalue, dialog_columnvalue;
    EditText search;
    RelativeLayout item,category,modifier,tax1, discount1;
    Uri contentUri,resultUri;

    String WebserviceUrl;


    public DatabasecategoryActivity(){
        super();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View rootview = inflater.inflate(R.layout.fragment_multi_category1, null);
//        final ActionBar actionBar = getActivity().getActionBar();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (getActivity() instanceof AppCompatActivity){
            androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionbar.setSubtitle("Category");
        }

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
                        if (account_selection.toString().equals("Dine")) {
                            Fragment i = new DatabasetaxesActivity();
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.container, i);
                            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            ft.commit();
                        }else {
                            if (account_selection.toString().equals("Qsr")) {
                                Fragment i = new DatabasetaxesActivity_Qsr();
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.container, i);
                                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                ft.commit();
                            }else {
                                Fragment i = new DatabasetaxesActivity_Retail();
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.container, i);
                                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                ft.commit();
                            }
                        }
//                        Fragment i = new DatabasetaxesActivity();
//                        FragmentTransaction ft = getFragmentManager().beginTransaction();
//                        ft.replace(R.id.container, i);
//                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                        ft.commit();
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
        text = (EditText) rootview.findViewById(R.id.editText1);
        priority_one = (EditText) rootview.findViewById(R.id.editText2);
        layoutcatname = (TextInputLayout) rootview.findViewById(R.id.layoutcatname);

        layout_prior_name = (TextInputLayout) rootview.findViewById(R.id.layoutpriorityname);

        priority_one.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                layout_prior_name.setError(null);
            }
        });

        columnvalue  = text.getText().toString();
        if (text.getText().toString().contains("'")) {
            columnvalue  = text.getText().toString().replaceAll("'", " ");
        }


        linearLayout = (RelativeLayout)rootview.findViewById(R.id.add_item);
        additem = (FloatingActionButton)rootview.findViewById(R.id.add_button);

        progressbar = rootview.findViewById(R.id.progressbar);
        circle = rootview.findViewById(R.id.circle);
        progress_text = rootview.findViewById(R.id.progress_text);
//        circle.getIndeterminateDrawable().setColorFilter(0xFF5D5D5D, android.graphics.PorterDuff.Mode.MULTIPLY);

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


        //final EditText one = (EditText)rootview.findViewById(R.id.editText1);
        //one.setText("");
        text.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                layoutcatname.setError(null);
            }
        });

//        final LinearLayout btncategory = (LinearLayout)rootview.findViewById(R.id.buttoncategory);
//        final LinearLayout btnitems = (LinearLayout)rootview.findViewById(R.id.buttonitems);
//        final LinearLayout btnmodifiers = (LinearLayout)rootview.findViewById(R.id.buttonmodifiers);
//        final LinearLayout btntaxes = (LinearLayout)rootview.findViewById(R.id.buttontaxes);


//        final ImageButton btnaddcategory = (ImageButton)rootview.findViewById(R.id.btnaddcategory);
//
//        btnaddcategory.setOnClickListener(new View.OnClickListener() {
//
//
//            public void onClick(View v) {
//
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                CreateCategoryActivity hello = new CreateCategoryActivity();
//                fragmentTransaction.add(R.id.fragment_container, hello, "HELLO");
//                fragmentTransaction.commit();
//            }
//        });

//        btnmodifiers.setOnClickListener(new View.OnClickListener() {
//
//
//            public void onClick(View v) {
//
//                actionBar.setTitle(" Modifiers");
//                frag = new DatabaseModifiersActivity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });
//
//        btnitems.setOnClickListener(new View.OnClickListener() {
//
//
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
//
//            public void onClick(View v) {
//
//                actionBar.setTitle(" Category");
//                frag = new DatabasecategoryActivity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });
//
//        btntaxes.setOnClickListener(new View.OnClickListener() {
//
//
//            public void onClick(View v) {
//
//                actionBar.setTitle(" Taxes");
//                frag = new DatabasetaxesActivity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });


        try {

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);

            //displayListView(rootview);//Generate ListView from SQLite Database

        }catch (SQLiteException e){
            alertas("Error inesperado: " + e.getMessage());
        }

        db.execSQL("UPDATE Hotel set value = '' WHERE value = '0'");




        countryList = new ArrayList<Country_category>();
        try {
            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor allrows = db.rawQuery("SELECT * FROM Hotel", null);
            System.out.println("COUNT : " + allrows.getCount());

            if (allrows.moveToFirst()) {
                do {
                    String ID = allrows.getString(0);
                    String NAme = allrows.getString(1);
                    String PlaCe = allrows.getString(2);
                    String vaLuE = allrows.getString(5);
                    TextView tv = new TextView(getActivity());
                    tv.setText(vaLuE);
                    if (!tv.getText().toString().equals("")) {
                        if (NAme.toString().equals("Favourites") || NAme.toString().equals("All")) {

                        } else {
                            Country_category NAME = new Country_category(ID, NAme, vaLuE);
                            countryList.add(NAME);
                        }
                    }


                } while (allrows.moveToNext());
            }
            allrows.close();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error encountered.",
                    Toast.LENGTH_LONG);
        }

        try {
            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor allrows = db.rawQuery("SELECT * FROM Hotel", null);
            System.out.println("COUNT : " + allrows.getCount());

            if (allrows.moveToFirst()) {
                do {
                    String ID = allrows.getString(0);
                    String NAme = allrows.getString(1);
                    String PlaCe = allrows.getString(2);
                    String vaLuE = allrows.getString(5);
                    TextView tv = new TextView(getActivity());
                    tv.setText(vaLuE);
                    if (tv.getText().toString().equals("")) {
                        if (NAme.toString().equals("Favourites") || NAme.toString().equals("All")) {

                        } else {
                            Country_category NAME = new Country_category(ID, NAme, vaLuE);
                            countryList.add(NAME);
                        }
                    }

                } while (allrows.moveToNext());
            }
            allrows.close();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error encountered.",
                    Toast.LENGTH_LONG);
        }

        dataAdapter = new MyCustomAdapter(getActivity(),
                R.layout.categories_listview, countryList);
        listView = (ListView) rootview.findViewById(R.id.listView);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Country_category country = (Country_category) parent.getItemAtPosition(position);
                //Toast.makeText(getActivity(), country.getName(), Toast.LENGTH_SHORT).show();



                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                Cursor allrows1 = db.rawQuery("SELECT * FROM Hotel WHERE name = '" + country.getName() + "' AND _id = '" + country.getCode() + "' ", null);
                if (allrows1.moveToFirst()) {
                    do {
                        final String ID = allrows1.getString(0);
                        final String NAme = allrows1.getString(1);
                        VAL_prIO = allrows1.getString(5);

                        TextView value_1 = new TextView(getActivity());
                        value_1.setText(VAL_prIO);
                        TextView value_2 = new TextView(getActivity());
                        value_2.setText(country.getvalue());

                        if (value_2.getText().toString().equals(value_1.getText().toString())) {
                            dialog = new Dialog(getActivity(), R.style.timepicker_date_dialog);
                            dialog.setContentView(R.layout.fragment_update_category);
                            dialog.setTitle(getString(R.string.title16));
                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            dialog.show();

                            final TextView dialogTxt_id = (TextView) dialog.findViewById(R.id.editText4);
                            dialogTxt_id.setText(String.valueOf(ID));

                            dialogC1_id = (EditText) dialog.findViewById(R.id.editText1);
                            layoutcatname_dialog = (TextInputLayout) dialog.findViewById(R.id.layoutcatname_dialog);
                            dialogC1_id.setText(NAme);

                            layoutpriority_dialog = (TextInputLayout) dialog.findViewById(R.id.layoutpriority_dialog);

                            priority_edittext = (EditText) dialog.findViewById(R.id.editText2);
                            priority_edittext.setText(VAL_prIO);

                            dialog_columnvalue = NAme;
                            if (NAme.toString().contains("'")) {
                                dialog_columnvalue  = NAme.toString().replaceAll("'", " ");
                            }


                            dialogC1_id.addTextChangedListener(new TextWatcher() {

                                public void afterTextChanged(Editable s) {
                                }

                                public void beforeTextChanged(CharSequence s, int start,
                                                              int count, int after) {
                                }

                                public void onTextChanged(CharSequence s, int start,
                                                          int before, int count) {
                                    layoutcatname_dialog.setError(null);
                                }
                            });


                            Button delete = (Button) dialog.findViewById(R.id.btndelete);
                            delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    hideKeyboard(getContext());

                                    final Dialog dd = new Dialog(getActivity(), R.style.notitle);
                                    dd.setContentView(R.layout.deleted_category_selected);


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

                                            String where = "_id = '" + ID + "' ";


                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Hotel");
                                            getActivity().getContentResolver().delete(contentUri, where,new String[]{});
                                            resultUri = new Uri.Builder()
                                                    .scheme("content")
                                                    .authority(StubProviderApp.AUTHORITY)
                                                    .path("Hotel")
                                                    .appendQueryParameter("operation", "delete")
                                                    .appendQueryParameter("_id", ID)
                                                    .build();
                                            getActivity().getContentResolver().notifyChange(resultUri, null);

//                                        db.delete("Hotel", where, new String[]{});

                                            countryList = new ArrayList<Country_category>();
                                            try {
                                                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                                Cursor allrows = db.rawQuery("SELECT * FROM Hotel", null);
                                                System.out.println("COUNT : " + allrows.getCount());


                                                //Country_category country = new Country_category(name, name, name, name);

                                                if (allrows.moveToFirst()) {
                                                    do {
                                                        String ID = allrows.getString(0);
                                                        String NAme = allrows.getString(1);
                                                        String PlaCe = allrows.getString(2);
                                                        String vaLuE = allrows.getString(5);
                                                        TextView tv = new TextView(getActivity());
                                                        tv.setText(vaLuE);
                                                        if (!tv.getText().toString().equals("")) {
                                                            if (NAme.toString().equals("Favourites") || NAme.toString().equals("All")) {

                                                            } else {
                                                                Country_category NAME = new Country_category(ID, NAme, vaLuE);
                                                                //Country_category PLACE = new Country_category(PlaCe, PlaCe, PlaCe, PlaCe);
                                                                countryList.add(NAME);
                                                                //countryList.add(PLACE);
                                                            }
                                                        }

                                                    } while (allrows.moveToNext());
                                                }
                                                allrows.close();
//                                            db.close();
                                            } catch (Exception e) {
                                                Toast.makeText(getActivity(), "Error encountered.",
                                                        Toast.LENGTH_LONG);
                                            }

                                            try {
                                                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                                Cursor allrows = db.rawQuery("SELECT * FROM Hotel", null);
                                                System.out.println("COUNT : " + allrows.getCount());

                                                if (allrows.moveToFirst()) {
                                                    do {
                                                        String ID = allrows.getString(0);
                                                        String NAme = allrows.getString(1);
                                                        String PlaCe = allrows.getString(2);
                                                        String vaLuE = allrows.getString(5);
                                                        TextView tv = new TextView(getActivity());
                                                        tv.setText(vaLuE);
                                                        if (tv.getText().toString().equals("")) {
                                                            if (NAme.toString().equals("Favourites") || NAme.toString().equals("All")) {

                                                            } else {
                                                                Country_category NAME = new Country_category(ID, NAme, vaLuE);
                                                                countryList.add(NAME);
                                                            }
                                                        }

                                                    } while (allrows.moveToNext());
                                                }
                                                allrows.close();
                                            } catch (Exception e) {
                                                Toast.makeText(getActivity(), "Error encountered.",
                                                        Toast.LENGTH_LONG);
                                            }

                                            dataAdapter = new MyCustomAdapter(getActivity(),
                                                    R.layout.categories_listview, countryList);
                                            final ListView listView = (ListView) rootview.findViewById(R.id.listView);
                                            // Assign adapter to ListView
                                            listView.setAdapter(dataAdapter);
                                            dd.dismiss();
                                            dialog.dismiss();
                                        }
                                    });


                                    dd.show();


                                }
                            });

//                        ImageView frameLayout = (ImageView) dialog.findViewById(R.id.closetext);
//                        frameLayout.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog.dismiss();
//                            }
//                        });

                            ImageButton frameLayout = (ImageButton) dialog.findViewById(R.id.btncancel);
                            frameLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    hideKeyboard(getContext());
                                    dialog.dismiss();
                                    donotshowKeyboard(getActivity());
                                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                }
                            });

                            final Button save = (Button) dialog.findViewById(R.id.btnsave);

                            dialogC1_id.setOnEditorActionListener(new EditText.OnEditorActionListener() {
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
                                    hideKeyboard(getContext());
                                    //Toast.makeText(getActivity(), "Name is changed from "+NAme +" "+dialogC1_id.getText().toString(), Toast.LENGTH_SHORT).show();

//                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                                hideKeyboard(getContext());

                                    SQLiteDatabase db = getActivity().openOrCreateDatabase("mydb_Appdata",
                                            Context.MODE_PRIVATE, null);
                                    String s = db.getPath();
                                    ContentValues newValues = new ContentValues();
                                    if (NAme.toString().equals(dialogC1_id.getText().toString())){
                                        saveInDBup(Integer.parseInt(ID));
                                    }else {

                                        dialog_columnvalue = dialogC1_id.getText().toString();
                                        if (dialog_columnvalue.toString().contains("'")) {
                                            dialog_columnvalue  = dialog_columnvalue.toString().replaceAll("'", " ");
                                        }

                                        Cursor itemnamecheck = db.rawQuery("SELECT * FROM Hotel WHERE name = '"+dialog_columnvalue+"'", null);
                                        if (itemnamecheck.moveToFirst()){
                                            layoutcatname_dialog.setError("Category name already in use");
                                        }else {
                                            if(dialogC1_id.getText().toString().equals("")){
                                                //Toast.makeText(getActivity(),"Fill Category name", Toast.LENGTH_SHORT).show();
                                                layoutcatname_dialog.setError("Fill Category name");
                                            }
                                            else {

                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("category", dialog_columnvalue);
                                                String where1 = "category = '" + NAme + "' ";
                                                    db.update("Items", contentValues, where1, new String[]{});

                                                String where1_v1 = "category = '" + NAme + "' ";
                                                db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

//                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                                                getActivity().getContentResolver().update(contentUri, contentValues,where1,new String[]{});
//                                                resultUri = new Uri.Builder()
//                                                        .scheme("content")
//                                                        .authority(StubProviderApp.AUTHORITY)
//                                                        .path("Items")
//                                                        .appendQueryParameter("operation", "update")
//                                                        .appendQueryParameter("category",NAme)
//                                                        .build();
//                                                getActivity().getContentResolver().notifyChange(resultUri, null);

                                                webservicequery("UPDATE items set category = '"+dialog_columnvalue+"' WHERE category = '"+NAme+"'");

//                                            db.update("Items", contentValues, where1, new String[]{});
                                                donotshowKeyboard(getActivity());

                                                ContentValues contentValues1 = new ContentValues();
                                                contentValues1.put("modcategory", dialog_columnvalue);
                                                String where11 = "modcategory = '" + NAme + "' ";


                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers");
                                                getActivity().getContentResolver().update(contentUri, contentValues1,where11,new String[]{});
                                                resultUri = new Uri.Builder()
                                                        .scheme("content")
                                                        .authority(StubProviderApp.AUTHORITY)
                                                        .path("Modifiers")
                                                        .appendQueryParameter("operation", "update")
                                                        .appendQueryParameter("modcategory",NAme)
                                                        .build();
                                                getActivity().getContentResolver().notifyChange(resultUri, null);

//                                            db.update("Modifiers", contentValues1, where11, new String[]{});
                                                donotshowKeyboard(getActivity());

//                                    newValues.put("name", dialogC1_id.getText().toString());
//                                    String where = "_id = ?";
//                                    db.update("Hotel", newValues, where, new String[]{Integer.toString(_id)});
//                                    Toast.makeText(getActivity().getBaseContext(),
//                                            "Category name saved", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                                hideKeyboard(getContext());
                                            }
                                            saveInDBup(Integer.parseInt(ID));
                                        }
                                    }

//                                if(dialogC1_id.getText().toString().equals("")){
//                                    //Toast.makeText(getActivity(),"Fill Category name", Toast.LENGTH_SHORT).show();
//                                    dialogC1_id.setError("Fill Category name");
//                                }
//                                else {
//
//                                    ContentValues contentValues = new ContentValues();
//                                    contentValues.put("category", dialogC1_id.getText().toString());
//                                    String where1 = "category = '" + NAme + "' ";
//                                    db.update("Items", contentValues, where1, new String[]{});
//                                    donotshowKeyboard(getActivity());
//
////                                    newValues.put("name", dialogC1_id.getText().toString());
////                                    String where = "_id = ?";
////                                    db.update("Hotel", newValues, where, new String[]{Integer.toString(_id)});
////                                    Toast.makeText(getActivity().getBaseContext(),
////                                            "Category name saved", Toast.LENGTH_SHORT).show();
//                                    dialog.dismiss();
//                                    hideKeyboard(getContext());
//                                }
//                                saveInDBup(Integer.parseInt(ID));

                                    countryList = new ArrayList<Country_category>();
                                    try {
                                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                        Cursor allrows = db.rawQuery("SELECT * FROM Hotel", null);
                                        System.out.println("COUNT : " + allrows.getCount());


                                        //Country_category country = new Country_category(name, name, name, name);

                                        if (allrows.moveToFirst()) {
                                            do {
                                                String ID = allrows.getString(0);
                                                String NAme = allrows.getString(1);
                                                String PlaCe = allrows.getString(2);
                                                String vaLuE = allrows.getString(5);
                                                TextView tv = new TextView(getActivity());
                                                tv.setText(vaLuE);
                                                if (!tv.getText().toString().equals("")) {
                                                    if (NAme.toString().equals("Favourites") || NAme.toString().equals("All")) {

                                                    } else {
                                                        Country_category NAME = new Country_category(ID, NAme, vaLuE);
                                                        //Country_category PLACE = new Country_category(PlaCe, PlaCe, PlaCe, PlaCe);
                                                        countryList.add(NAME);
                                                        //countryList.add(PLACE);
                                                    }
                                                }

                                            } while (allrows.moveToNext());
                                        }
                                        allrows.close();
//                                    db.close();
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), "Error encountered.",
                                                Toast.LENGTH_LONG);
                                    }

                                    try {
                                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                        Cursor allrows = db.rawQuery("SELECT * FROM Hotel", null);
                                        System.out.println("COUNT : " + allrows.getCount());

                                        if (allrows.moveToFirst()) {
                                            do {
                                                String ID = allrows.getString(0);
                                                String NAme = allrows.getString(1);
                                                String PlaCe = allrows.getString(2);
                                                String vaLuE = allrows.getString(5);
                                                TextView tv = new TextView(getActivity());
                                                tv.setText(vaLuE);
                                                if (tv.getText().toString().equals("")) {
                                                    if (NAme.toString().equals("Favourites") || NAme.toString().equals("All")) {

                                                    } else {
                                                        Country_category NAME = new Country_category(ID, NAme, vaLuE);
                                                        countryList.add(NAME);
                                                    }
                                                }

                                            } while (allrows.moveToNext());
                                        }
                                        allrows.close();
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), "Error encountered.",
                                                Toast.LENGTH_LONG);
                                    }

                                    dataAdapter = new MyCustomAdapter(getActivity(),
                                            R.layout.categories_listview, countryList);
                                    final ListView listView = (ListView) rootview.findViewById(R.id.listView);
                                    // Assign adapter to ListView
                                    listView.setAdapter(dataAdapter);

                                }
                            });
                            //dialogC4_id.setSelection(getIndex(dialogC4_id, CAte));
                        }else {
                            listView.invalidateViews();

                            countryList = new ArrayList<Country_category>();
                            try {
                                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                Cursor allrows = db.rawQuery("SELECT * FROM Hotel", null);
                                System.out.println("COUNT : " + allrows.getCount());


                                //Country_category country = new Country_category(name, name, name, name);

                                if (allrows.moveToFirst()) {
                                    do {
                                        String ID1 = allrows.getString(0);
                                        String NAme1 = allrows.getString(1);
                                        String PlaCe = allrows.getString(2);
                                        String vaLuE = allrows.getString(5);
                                        TextView tv = new TextView(getActivity());
                                        tv.setText(vaLuE);
                                        if (!tv.getText().toString().equals("")) {
                                            if (NAme1.toString().equals("Favourites") || NAme1.toString().equals("All")) {

                                            } else {
                                                Country_category NAME = new Country_category(ID1, NAme1, vaLuE);
                                                //Country_category PLACE = new Country_category(PlaCe, PlaCe, PlaCe, PlaCe);
                                                countryList.add(NAME);
                                                //countryList.add(PLACE);
                                            }
                                        }

                                    } while (allrows.moveToNext());
                                }
                                allrows.close();
//                                    db.close();
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), "Error encountered.",
                                        Toast.LENGTH_LONG);
                            }

                            try {
                                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                Cursor allrows = db.rawQuery("SELECT * FROM Hotel", null);
                                System.out.println("COUNT : " + allrows.getCount());

                                if (allrows.moveToFirst()) {
                                    do {
                                        String ID1 = allrows.getString(0);
                                        String NAme1 = allrows.getString(1);
                                        String PlaCe = allrows.getString(2);
                                        String vaLuE = allrows.getString(5);
                                        TextView tv = new TextView(getActivity());
                                        tv.setText(vaLuE);
                                        if (tv.getText().toString().equals("")) {
                                            if (NAme1.toString().equals("Favourites") || NAme1.toString().equals("All")) {

                                            } else {
                                                Country_category NAME = new Country_category(ID1, NAme1, vaLuE);
                                                countryList.add(NAME);
                                            }
                                        }

                                    } while (allrows.moveToNext());
                                }
                                allrows.close();
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), "Error encountered.",
                                        Toast.LENGTH_LONG);
                            }

                            dataAdapter = new MyCustomAdapter(getActivity(),
                                    R.layout.categories_listview, countryList);
                            final ListView listView = (ListView) rootview.findViewById(R.id.listView);
                            // Assign adapter to ListView
                            listView.setAdapter(dataAdapter);
                        }




                    } while (allrows1.moveToNext());
                }else {
                    listView.invalidateViews();

                    countryList = new ArrayList<Country_category>();
                    try {
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("SELECT * FROM Hotel", null);
                        System.out.println("COUNT : " + allrows.getCount());


                        //Country_category country = new Country_category(name, name, name, name);

                        if (allrows.moveToFirst()) {
                            do {
                                String ID = allrows.getString(0);
                                String NAme = allrows.getString(1);
                                String PlaCe = allrows.getString(2);
                                String vaLuE = allrows.getString(5);
                                TextView tv = new TextView(getActivity());
                                tv.setText(vaLuE);
                                if (!tv.getText().toString().equals("")) {
                                    if (NAme.toString().equals("Favourites") || NAme.toString().equals("All")) {

                                    } else {
                                        Country_category NAME = new Country_category(ID, NAme, vaLuE);
                                        //Country_category PLACE = new Country_category(PlaCe, PlaCe, PlaCe, PlaCe);
                                        countryList.add(NAME);
                                        //countryList.add(PLACE);
                                    }
                                }

                            } while (allrows.moveToNext());
                        }
                        allrows.close();
//                                    db.close();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Error encountered.",
                                Toast.LENGTH_LONG);
                    }

                    try {
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("SELECT * FROM Hotel", null);
                        System.out.println("COUNT : " + allrows.getCount());

                        if (allrows.moveToFirst()) {
                            do {
                                String ID = allrows.getString(0);
                                String NAme = allrows.getString(1);
                                String PlaCe = allrows.getString(2);
                                String vaLuE = allrows.getString(5);
                                TextView tv = new TextView(getActivity());
                                tv.setText(vaLuE);
                                if (tv.getText().toString().equals("")) {
                                    if (NAme.toString().equals("Favourites") || NAme.toString().equals("All")) {

                                    } else {
                                        Country_category NAME = new Country_category(ID, NAme, vaLuE);
                                        countryList.add(NAME);
                                    }
                                }

                            } while (allrows.moveToNext());
                        }
                        allrows.close();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Error encountered.",
                                Toast.LENGTH_LONG);
                    }

                    dataAdapter = new MyCustomAdapter(getActivity(),
                            R.layout.categories_listview, countryList);
                    final ListView listView = (ListView) rootview.findViewById(R.id.listView);
                    // Assign adapter to ListView
                    listView.setAdapter(dataAdapter);
                }

//                dialog.setCanceledOnTouchOutside(false);




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
                        mode.setTitle(checkedCount+ " Selected category");
                        return true;
                    case R.id.delete:
                        // Add  dialog for confirmation to delete selected item
                        // record.

                        final Dialog dialogq = new Dialog(getActivity(), R.style.notitle);
                        dialogq.setContentView(R.layout.deleted_category_selected);

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
                                            Country_category country = countryList.get(i);
                                            String na = country.getName();
                                            String an = country.getCode();

                                            System.out.println("category selected is "+na);

                                            db.execSQL("delete from Hotel WHERE name = '"+na+"'");
                                            if (isDeviceOnline()) {
                                                webservicequery("delete from Hotel WHERE name = '" + na + "'");
                                            }else {

                                                Bundle extras=new Bundle();
                                                extras.putString("query","delete from Hotel WHERE name = '"+na+"'");

                                                extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL,true);
                                                ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
                                                ContentResolver.requestSync(null, AUTHORITY, extras);
                                            }

                                            db.execSQL("delete from items WHERE category = '"+na+"'");
                                            if (isDeviceOnline()) {
                                                webservicequery("delete from items WHERE category = '" + na + "'");
                                            }else {

                                                Bundle extras=new Bundle();
                                                extras.putString("query","delete from items WHERE category = '"+na+"'");

                                                extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL,true);
                                                ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
                                                ContentResolver.requestSync(null, AUTHORITY, extras);
                                            }

//                                            /* do whatever you want with the checked item */
//                                            String where = "_id = '" + an + "' ";
//
//                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Hotel");
//                                            getActivity().getContentResolver().delete(contentUri, where, new String[]{});
//                                            resultUri = new Uri.Builder()
//                                                    .scheme("content")
//                                                    .authority(StubProviderApp.AUTHORITY)
//                                                    .path("Hotel")
//                                                    .appendQueryParameter("operation", "delete")
//                                                    .appendQueryParameter("_id", an)
//                                                    .build();
//                                            getActivity().getContentResolver().notifyChange(resultUri, null);
//
////                                            db.delete("Hotel", where, new String[]{});
//
//                                            String where1 = "category = '" + na + "' ";
//
//                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                                            getActivity().getContentResolver().delete(contentUri, where1, new String[]{});
//                                            resultUri = new Uri.Builder()
//                                                    .scheme("content")
//                                                    .authority(StubProviderApp.AUTHORITY)
//                                                    .path("Items")
//                                                    .appendQueryParameter("operation", "delete")
//                                                    .appendQueryParameter("category", na)
//                                                    .build();
//                                            getActivity().getContentResolver().notifyChange(resultUri, null);

//                                            db.delete("Items", where1, new String[]{});

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

                                mode.finish();
                                listView.invalidateViews();
                                progressbar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();

                                countryList = new ArrayList<Country_category>();
                                try {
                                    db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                    Cursor allrows = db.rawQuery("SELECT * FROM Hotel", null);
                                    System.out.println("COUNT : " + allrows.getCount());


                                    //Country_category country = new Country_category(name, name, name, name);

                                    if (allrows.moveToFirst()) {
                                        do {
                                            String ID = allrows.getString(0);
                                            String NAme = allrows.getString(1);
                                            String PlaCe = allrows.getString(2);
                                            String vaLuE = allrows.getString(5);
                                            TextView tv = new TextView(getActivity());
                                            tv.setText(vaLuE);
                                            if (!tv.getText().toString().equals("")) {
                                                if (NAme.toString().equals("Favourites") || NAme.toString().equals("All")) {

                                                } else {
                                                    Country_category NAME = new Country_category(ID, NAme, vaLuE);
                                                    //Country_category PLACE = new Country_category(PlaCe, PlaCe, PlaCe, PlaCe);
                                                    countryList.add(NAME);
                                                    //countryList.add(PLACE);
                                                }
                                            }

                                        } while (allrows.moveToNext());
                                    }
                                    allrows.close();
//                                    db.close();
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), "Error encountered.",
                                            Toast.LENGTH_LONG);
                                }

                                try {
                                    db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                    Cursor allrows = db.rawQuery("SELECT * FROM Hotel", null);
                                    System.out.println("COUNT : " + allrows.getCount());

                                    if (allrows.moveToFirst()) {
                                        do {
                                            String ID = allrows.getString(0);
                                            String NAme = allrows.getString(1);
                                            String PlaCe = allrows.getString(2);
                                            String vaLuE = allrows.getString(5);
                                            TextView tv = new TextView(getActivity());
                                            tv.setText(vaLuE);
                                            if (tv.getText().toString().equals("")) {
                                                if (NAme.toString().equals("Favourites") || NAme.toString().equals("All")) {

                                                } else {
                                                    Country_category NAME = new Country_category(ID, NAme, vaLuE);
                                                    countryList.add(NAME);
                                                }
                                            }

                                        } while (allrows.moveToNext());
                                    }
                                    allrows.close();
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), "Error encountered.",
                                            Toast.LENGTH_LONG);
                                }

                                dataAdapter = new MyCustomAdapter(getActivity(),
                                        R.layout.categories_listview, countryList);
                                final ListView listView = (ListView) rootview.findViewById(R.id.listView);
                                // Assign adapter to ListView
                                listView.setAdapter(dataAdapter);


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
                mode.setTitle(checkedCount+ " Selected category");
                // Calls  toggleSelection method from ListViewAdapter Class
                //adapter.toggleSelection(position);
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
                linearLayout = (RelativeLayout)rootview.findViewById(R.id.add_item);
                linearLayout.setVisibility(View.VISIBLE);
                additem.setVisibility(View.GONE);
                search.setEnabled(false);

                text = (EditText) rootview.findViewById(R.id.editText1);


                columnvalue  = text.getText().toString();
                if (text.getText().toString().contains("'")) {
                    columnvalue  = text.getText().toString().replaceAll("'", " ");
                }

                EditText one = (EditText)rootview.findViewById(R.id.editText1);
                one.setText("");

                EditText two = (EditText) rootview.findViewById(R.id.editText2);
                two.setText("");

                layout_prior_name = (TextInputLayout) rootview.findViewById(R.id.layoutpriorityname);
                layout_prior_name.setError(null);
                one.requestFocus();
                //showKeyboard(getActivity());
                //displayKeyboard();

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(one, InputMethodManager.SHOW_IMPLICIT);

                InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.showSoftInput(text, InputMethodManager.SHOW_IMPLICIT);
//                ImageView into = (ImageView)rootview.findViewById(R.id.closetext);
//                into.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        linearLayout.setVisibility(View.INVISIBLE);
//                        EditText one = (EditText) rootview.findViewById(R.id.editText1);
//                        one.setText("");
//                        hideKeyboard(getContext());
//                    }
//                });

                Button btn = (Button)rootview.findViewById(R.id.btndelete);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearLayout.setVisibility(View.GONE);
                        additem.setVisibility(View.VISIBLE);
                        EditText one = (EditText) rootview.findViewById(R.id.editText1);
                        one.setText("");
                        search.setEnabled(true);
                        hideKeyboard(getContext());


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            donotshowKeyboard(getActivity());

                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        }
                    }
                });

                final Button save = (Button)rootview.findViewById(R.id.btnsave);

                one.setOnEditorActionListener(new EditText.OnEditorActionListener() {
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
                        saveInDB();
                        //linearLayout.setVisibility(View.INVISIBLE);
//                        EditText one = (EditText)rootview.findViewById(R.id.editText1);
//                        one.setText("");
                        search.setEnabled(true);
                        //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        //hideKeyboard(getContext());

                        countryList = new ArrayList<Country_category>();
                        try {
                            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                            Cursor allrows = db.rawQuery("SELECT * FROM Hotel", null);
                            System.out.println("COUNT : " + allrows.getCount());
                            if (allrows.moveToFirst()) {
                                do {
                                    String ID = allrows.getString(0);
                                    String NAme = allrows.getString(1);
                                    String PlaCe = allrows.getString(2);
                                    String vaLuE = allrows.getString(5);
                                    TextView tv = new TextView(getActivity());
                                    tv.setText(vaLuE);
                                    if (!tv.getText().toString().equals("")) {
                                        if (NAme.toString().equals("Favourites") || NAme.toString().equals("All")) {

                                        } else {
                                            Country_category NAME = new Country_category(ID, NAme, vaLuE);
                                            countryList.add(NAME);
                                        }
                                    }

                                } while (allrows.moveToNext());
                            }
                            allrows.close();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Error encountered.",
                                    Toast.LENGTH_LONG);
                        }

                        try {
                            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                            Cursor allrows = db.rawQuery("SELECT * FROM Hotel", null);
                            System.out.println("COUNT : " + allrows.getCount());

                            if (allrows.moveToFirst()) {
                                do {
                                    String ID = allrows.getString(0);
                                    String NAme = allrows.getString(1);
                                    String PlaCe = allrows.getString(2);
                                    String vaLuE = allrows.getString(5);
                                    TextView tv = new TextView(getActivity());
                                    tv.setText(vaLuE);
                                    if (tv.getText().toString().equals("")) {
                                        if (NAme.toString().equals("Favourites") || NAme.toString().equals("All")) {

                                        } else {
                                            Country_category NAME = new Country_category(ID, NAme, vaLuE);
                                            countryList.add(NAME);
                                        }
                                    }

                                } while (allrows.moveToNext());
                            }
                            allrows.close();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Error encountered.",
                                    Toast.LENGTH_LONG);
                        }

                        dataAdapter = new MyCustomAdapter(getActivity(),
                                R.layout.categories_listview, countryList);
                        final ListView listView = (ListView) rootview.findViewById(R.id.listView);
                        // Assign adapter to ListView
                        listView.setAdapter(dataAdapter);

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
//        inflater.inflate(R.menu.category_menu, menu);
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
//            case R.id.action_modifier:
//                frag = new DatabaseModifiersActivity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//                hideKeyboard(getContext());
//                break;
//
//
//            case R.id.action_tax:
//                frag = new DatabasetaxesActivity();
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


    private class MyCustomAdapter extends ArrayAdapter<Country_category> {

        private ArrayList<Country_category> originalList;
        private ArrayList<Country_category> countryList;
        private CountryFilter filter;

        private Cursor c;
        private Context context;

        private SparseBooleanArray mSelectedItemsIds;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Country_category> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<Country_category>();
            this.countryList.addAll(countryList);
            this.originalList = new ArrayList<Country_category>();
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
            TextView value;
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
                convertView = vi.inflate(R.layout.categories_listview, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.value = (TextView) convertView.findViewById(R.id.prio_value);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Country_category country = countryList.get(position);
            holder.code.setText(country.getCode());
            holder.name.setText(country.getName());
            holder.value.setText(country.getvalue());
//            holder.name.setText(country);
//            holder.continent.setText(country.getContinent());
//            holder.region.setText(country.getRegion());

            return convertView;

        }

        private class CountryFilter extends Filter
        {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();
                if(constraint != null && constraint.toString().length() > 0)
                {
                    ArrayList<Country_category> filteredItems = new ArrayList<Country_category>();

                    for(int i = 0, l = originalList.size(); i < l; i++)
                    {
                        Country_category country = originalList.get(i);
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

                countryList = (ArrayList<Country_category>)results.values;
                notifyDataSetChanged();
                clear();
                for(int i = 0, l = countryList.size(); i < l; i++)
                    add(countryList.get(i));
                notifyDataSetInvalidated();
            }
        }


    }

//    private void displayListView(final View root) {
//        cursor = db.rawQuery("Select * from Hotel;", null);//replace to cursor = dbHelper.fetchAllHotels();
//        // The desired columns to be bound
//        String[] fromFieldNames = {"name", "_id"};
//        // the XML defined views which the data will be bound to
//        int[] toViewsID = {R.id.name, R.id.code};
//        Log.e("Checamos que hay id", String.valueOf(R.id.name));
//        dataAdapter = new SimpleCursorAdapter(getActivity(), R.layout.country_info, cursor, fromFieldNames, toViewsID, 0);
//        listView = (ListView) root.findViewById(R.id.list);
//        listView.setAdapter(dataAdapter);// Assign adapter to ListView.... here... the bitch error
//
//
//
//        EditText myFilter = (EditText)root.findViewById(R.id.searchView);
//        myFilter.addTextChangedListener(new TextWatcher() {
//
//            public void afterTextChanged(Editable s) {
//            }
//            public void beforeTextChanged(CharSequence s, int start,
//                                          int count, int after) {
//            }
//            public void onTextChanged(CharSequence s, int start,
//                                      int before, int count) {
//                dataAdapter.getFilter().filter(s.toString());
//            }
//        });
//
//        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
//            public Cursor runQuery(CharSequence constraint) {
//                return fetchCountriesByName(constraint.toString());
//            }
//        });
//
//
//
//        listView.setClickable(true);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
//                /** Creating a bundle object to pass some data to the fragment */
//                final Cursor cursor = (Cursor) parent.getItemAtPosition(position);
//                final int item_id = cursor.getInt(cursor.getColumnIndex("_id"));
//                final String item_content1 = cursor.getString(cursor.getColumnIndex("name"));
//
//                AlertDialog.Builder myDialog
//                        = new AlertDialog.Builder(getActivity());
//
//                myDialog.setTitle("Edit? (or) Delete?");
//
//                final TextView dialogTxt_id = new TextView(getActivity());
//                LayoutParams dialogTxt_idLayoutParams
//                        = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//                dialogTxt_id.setLayoutParams(dialogTxt_idLayoutParams);
//                dialogTxt_id.setText("#" + String.valueOf(item_id));
//
//                final EditText dialogC1_id = new EditText(getActivity());
//                LayoutParams dialogC1_idLayoutParams
//                        = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//                dialogC1_id.setLayoutParams(dialogC1_idLayoutParams);
//                dialogC1_id.setText(item_content1);
//
//                LinearLayout layout = new LinearLayout(getActivity());
//                layout.setOrientation(LinearLayout.VERTICAL);
//                layout.addView(dialogTxt_id);
//                layout.addView(dialogC1_id);
//
//                myDialog.setView(layout);
//
//                myDialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
//                    // do something when the button is clicked
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        if (item_content1.equals("Default")){
//                            Toast.makeText(getActivity(), "Not Editable / Deletable", Toast.LENGTH_SHORT).show();
//                        }else {
//                            updatee(item_id, dialogC1_id.getText().toString());
//                        }
//                        displayListView(root);
//                    }
//                });
//
//                myDialog.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
//                    // do something when the button is clicked
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        if (item_content1.equals("Default")){
//                            Toast.makeText(getActivity(), "Not Editable / Deletable", Toast.LENGTH_SHORT).show();
//                        }else {
//                            delete_byID(item_id);
//                        }
//                        displayListView(root);
//                    }
//                });
//
//                myDialog.show();
//
//            }
//
//        });
//
//
//
//        spinner = (Spinner)root.findViewById(R.id.chocolate_category);
//        cursor = db.rawQuery("Select * from Hotel ORDER BY name;", null);
//        dataadapter = new SimpleCursorAdapter(getActivity(), R.layout.country_info, cursor, fromFieldNames, toViewsID, 0);
//
//        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View arg1,
//                                       int position, long arg3) {
//                String item = parent.getItemAtPosition(position).toString();
//                if(item.equals("Numerically")) {
//                    listView.setAdapter(dataAdapter);
//                }
//                else if(item.equals("Alphabatically")){
//
//                    listView.setAdapter(dataadapter);
//
//                    return;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//
//        myFilter = (EditText)root.findViewById(R.id.searchView);
//        myFilter.addTextChangedListener(new TextWatcher() {
//
//            public void afterTextChanged(Editable s) {
//            }
//            public void beforeTextChanged(CharSequence s, int start,
//                                          int count, int after) {
//            }
//            public void onTextChanged(CharSequence s, int start,
//                                      int before, int count) {
//                dataadapter.getFilter().filter(s.toString());
//            }
//        });
//
//        dataadapter.setFilterQueryProvider(new FilterQueryProvider() {
//            public Cursor runQuery(CharSequence constraint) {
//                return fetchCountriesByName(constraint.toString());
//            }
//        });
//    }


    public void delete_byID(int _id){
        String where = "_id = '" + _id + "' ";

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Hotel");
        getActivity().getContentResolver().delete(contentUri, "_id" + "=" + _id,null);
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("Hotel")
                .appendQueryParameter("operation", "delete")
                .appendQueryParameter("_id", String.valueOf(_id))
                .build();
        getActivity().getContentResolver().notifyChange(resultUri, null);

//        db.delete("Hotel", "_id" + "=" + _id, null);
        Toast.makeText(getActivity().getBaseContext(),
                "Category deleted", Toast.LENGTH_SHORT).show();
    }

    public void updatee(Integer _id,String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        String where="_id = ?";

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Hotel");
        getActivity().getContentResolver().update(contentUri, values,where,new String[]{Integer.toString(_id)});
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("Hotel")
                .appendQueryParameter("operation", "update")
                .appendQueryParameter("_id", Integer.toString(_id))
                .build();
        getActivity().getContentResolver().notifyChange(resultUri, null);

//        db.update("Hotel", values, where, new String[]{Integer.toString(_id)});
        //Toast.makeText(getActivity().getBaseContext(),
        //"Category name updated", Toast.LENGTH_SHORT).show();
    }

    private void updateList(){
        cursor.requery();
    }

    public Cursor fetchCountriesByName(String inputText) throws SQLException {

        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = db.query("Hotel", new String[] {"_id","name"},
                    null, null, null, null, null);

        }
        else {
            mCursor = db.query(true, "Hotel", new String[] {"_id","name"},
                    "name" + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void alertas(String alerta) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme);
        AlertDialog.Builder builder = new AlertDialog.Builder(wrapper);
        builder.setIcon(R.drawable.icon);
        builder.setTitle(R.string.app_name);
        builder.setMessage(alerta);
        builder.create().show();
    }


    void saveInDBup(Integer _id) {
        SQLiteDatabase db = getActivity().openOrCreateDatabase("mydb_Appdata",
                Context.MODE_PRIVATE, null);
        String s = db.getPath();


        dialog_columnvalue  = dialogC1_id.getText().toString();
        if (dialogC1_id.getText().toString().contains("'")) {
            dialog_columnvalue  = dialogC1_id.getText().toString().replaceAll("'", " ");
        }


        ContentValues newValues = new ContentValues();
        if(dialogC1_id.getText().toString().equals("")){
            //Toast.makeText(getActivity(),"Fill Category name", Toast.LENGTH_SHORT).show();
            layoutcatname_dialog.setError("Fill Category name");
        }
        else{

            if (priority_edittext.getText().toString().equals("")){

                webservicequery("UPDATE Hotel set name = '"+dialog_columnvalue+"', value = NULL WHERE _id = '"+_id+"'");

                newValues.put("name", dialog_columnvalue);
//                if (priority_edittext.getText().toString().equals("")){
//
//                }else {
//                    newValues.put("value", "");
//                }
                                newValues.put("value", "");
                String where = "_id = ?";
//
//                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Hotel");
//                getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{Integer.toString(_id)});
//                resultUri = new Uri.Builder()
//                        .scheme("content")
//                        .authority(StubProviderApp.AUTHORITY)
//                        .path("Hotel")
//                        .appendQueryParameter("operation", "update")
//                        .appendQueryParameter("_id", Integer.toString(_id))
//                        .build();
//                getActivity().getContentResolver().notifyChange(resultUri, null);

                db.update("Hotel", newValues, where, new String[]{Integer.toString(_id)});
                Toast.makeText(getActivity().getBaseContext(),
                        getString(R.string.category_saved), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                hideKeyboard(getContext());
                donotshowKeyboard(getActivity());
                hideKeyboard(getContext());
            }else {
                if (priority_edittext.getText().toString().equals(VAL_prIO)) {
                    newValues.put("name", dialog_columnvalue);
                    newValues.put("value", priority_edittext.getText().toString());
                    String where = "_id = ?";

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Hotel");
                    getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{Integer.toString(_id)});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Hotel")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id", Integer.toString(_id))
                            .build();
                    getActivity().getContentResolver().notifyChange(resultUri, null);

//                    db.update("Hotel", newValues, where, new String[]{Integer.toString(_id)});
                    Toast.makeText(getActivity().getBaseContext(),
                            getString(R.string.category_saved), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    hideKeyboard(getContext());
                    donotshowKeyboard(getActivity());
                    hideKeyboard(getContext());
                }else {
                    int cou3 = 1;
                    Cursor cursor1 = db.rawQuery("SELECT * FROM Hotel", null);
                    if (cursor1.moveToFirst()){
                        do {
                            String va = cursor1.getString(5);

                            TextView tv = new TextView(getActivity());
                            tv.setText(va);

                            if (tv.getText().toString().equals("")){

                            }else {
                                cou3 = cou3+1;
                            }
                        }while (cursor1.moveToNext());
                    }
//                    int cou2 = cursor1.getColumnCount();
//                    int cou3 = cou2 + 1;
                    Cursor cursor = db.rawQuery("SELECT * FROM Hotel WHERE value = '" + priority_edittext.getText().toString() + "'", null);
                    if (cursor.moveToFirst()) {
                        layoutpriority_dialog.setError("proceed with priority " + cou3);
                    } else {
                        newValues.put("name", dialog_columnvalue);
                        newValues.put("value", priority_edittext.getText().toString());
                        String where = "_id = ?";

                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Hotel");
                        getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{Integer.toString(_id)});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("Hotel")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id", Integer.toString(_id))
                                .build();
                        getActivity().getContentResolver().notifyChange(resultUri, null);

//                        db.update("Hotel", newValues, where, new String[]{Integer.toString(_id)});
                        Toast.makeText(getActivity().getBaseContext(),
                                getString(R.string.category_saved), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        hideKeyboard(getContext());
                        donotshowKeyboard(getActivity());
                        hideKeyboard(getContext());
                    }
                }
            }



//                    linearLayout.setVisibility(View.INVISIBLE);
        }
    }

    void saveInDB() {
        SQLiteDatabase db = getActivity().openOrCreateDatabase("mydb_Appdata",
                Context.MODE_PRIVATE, null);
        String s = db.getPath();

        columnvalue  = text.getText().toString();
        if (text.getText().toString().contains("'")) {
            columnvalue  = text.getText().toString().replaceAll("'", " ");
        }

        ContentValues newValues = new ContentValues();
        Cursor itemnamecheck = db.rawQuery("SELECT * FROM Hotel WHERE name = '"+columnvalue+"'", null);
        if (itemnamecheck.moveToFirst()){
            layoutcatname.setError("Category name already in use");
        }else {
            if (text.getText().toString().equals("")) {
                //Toast.makeText(getActivity(),"Fill Category name", Toast.LENGTH_SHORT).show();
                layoutcatname.setError("Fill Category name");
            } else {
                int cou3 = 1;
                Cursor cursor1 = db.rawQuery("SELECT * FROM Hotel", null);
                if (cursor1.moveToFirst()){
                    do {
                        String va = cursor1.getString(5);

                        TextView tv = new TextView(getActivity());
                        tv.setText(va);

                        if (tv.getText().toString().equals("")){

                        }else {
                            cou3 = cou3+1;
                        }
                    }while (cursor1.moveToNext());
                }
//                Cursor cursor1 = db.rawQuery("SELECT * FROM Hotel WHERE value is not null or value != ''", null);
//                int cou2 = cursor1.getColumnCount();
//                int cou3 = cou2+1;
                if (priority_one.getText().toString().equals("")){
                    newValues.put("name", columnvalue);
                    if (priority_one.getText().toString().equals("")){

                    }else {
                        newValues.put("value", "");
                    }

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Hotel");
                    resultUri = getActivity().getContentResolver().insert(contentUri, newValues);
                    getActivity().getContentResolver().notifyChange(resultUri, null);

//                    db.insert("Hotel", null, newValues);
                    Toast.makeText(getActivity().getBaseContext(),
                            getString(R.string.category_saved), Toast.LENGTH_SHORT).show();
                    linearLayout.setVisibility(View.GONE);
                    additem.setVisibility(View.VISIBLE);
                    EditText one = (EditText) getActivity().findViewById(R.id.editText1);
                    one.setText("");
                    hideKeyboard(getContext());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        donotshowKeyboard(getActivity());

                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    }
                }else {
                    Cursor cursor = db.rawQuery("SELECT * FROM Hotel WHERE value = '" + priority_one.getText().toString() + "'", null);
                    if (cursor.moveToFirst()) {
                        layout_prior_name.setError("proceed with priority " + cou3);
                    } else {
                        newValues.put("name", columnvalue);
                        newValues.put("value", priority_one.getText().toString());

                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Hotel");
                        resultUri = getActivity().getContentResolver().insert(contentUri, newValues);
                        getActivity().getContentResolver().notifyChange(resultUri, null);

//                        db.insert("Hotel", null, newValues);
                        Toast.makeText(getActivity().getBaseContext(),
                                getString(R.string.category_saved), Toast.LENGTH_SHORT).show();
                        linearLayout.setVisibility(View.GONE);
                        additem.setVisibility(View.VISIBLE);
                        EditText one = (EditText) getActivity().findViewById(R.id.editText1);
                        one.setText("");
                        hideKeyboard(getContext());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            donotshowKeyboard(getActivity());

                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        }
                    }
                }

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
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
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


//    /** A callback method invoked by the loader when initLoader() is called */
//
//    @Override
//    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
//        Uri uri = CategoryProvider.CONTENT_URI;
//        return new CursorLoader(getActivity(), uri, null, null, null, null);
//    }
//
//    /** A callback method, invoked after the requested content provider returned all the data */
//    @Override
//    public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
//        dataAdapter.swapCursor(arg1);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> arg0) {
//        dataAdapter.swapCursor(null);
//    }
//
//    class Cat extends Activity{
//       String name;
//        public Cat() {
//            super();
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//    }
//
//    public String getType(Cursor c) {
//        return(c.getString(3));
//    }


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

}
