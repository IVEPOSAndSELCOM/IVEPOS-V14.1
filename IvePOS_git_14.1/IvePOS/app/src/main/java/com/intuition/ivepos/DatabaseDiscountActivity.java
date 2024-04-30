package com.intuition.ivepos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

/**
 * Created by Rohithkumar on 4/25/2016.
 */
public class DatabaseDiscountActivity extends Fragment{

    Fragment frag;
    FragmentTransaction fragTransaction;

    public SQLiteDatabase db = null;
    public Cursor cursor;
    SimpleCursorAdapter dataadapter, dataAdapterr;
    ListView listview;
    EditText dialogC1_id, dialogC2_id; Spinner dialogC3_id;
    String VAluE;
    private RadioButton radioBtn1;
    private RadioGroup radioGroupWebsite;
    Spinner spinner;
    Dialog dialog;
    RelativeLayout progressbar;
    ProgressBar circle;
    TextView progress_text;


    ArrayList<Country_discount> countryList;
    MyCustomAdapter dataAdapter;
    RelativeLayout linearLayout;
    EditText text, editText, dis_val; String columnvalue, dialog_columnvalue;
    Spinner textViewrspercent;
    String rr;
    ListView listView1, listView;
    EditText search;

    RelativeLayout item, category, modifier, tax1, discount1;

    FloatingActionButton additem;

    TextInputLayout dis_val_layout, layoutdisname;
    TextInputLayout layoutdisname_dialog, layoutdisvalue_dialog;
    Uri contentUri,resultUri;

    String insert1_cc = "", insert1_rs = "", str_country;

    String WebserviceUrl;

    public DatabaseDiscountActivity() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View rootview = inflater.inflate(R.layout.fragment_multi_discount1, null);

        if (getActivity() instanceof AppCompatActivity){
            androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionbar.setSubtitle("Item");
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
        dis_val = (EditText) rootview.findViewById(R.id.editText2);

        textViewrspercent = (Spinner) rootview.findViewById(R.id.rspercent);


        layoutdisname = (TextInputLayout) rootview.findViewById(R.id.layoutcatname);
        dis_val_layout = (TextInputLayout) rootview.findViewById(R.id.layoutpriorityname);

        linearLayout = (RelativeLayout)rootview.findViewById(R.id.add_item);

        progressbar = rootview.findViewById(R.id.progressbar);
        circle = rootview.findViewById(R.id.circle);
        progress_text = rootview.findViewById(R.id.progress_text);
//        circle.getIndeterminateDrawable().setColorFilter(0xFF5D5D5D, android.graphics.PorterDuff.Mode.MULTIPLY);



        text.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                layoutdisname.setError(null);
            }
        });

        dis_val.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dis_val_layout.setError(null);
            }
        });



        try {

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);

            //displayListView(rootview);//Generate ListView from SQLite Database

        }catch (SQLiteException e){
            alertas("Error inesperado: " + e.getMessage());
        }

        Cursor cursor_country = db.rawQuery("SELECT * FROM Country_Selection", null);
        if (cursor_country.moveToFirst()){
            str_country = cursor_country.getString(1);
        }

        if (str_country.toString().equals("Rupee") || str_country.toString().equals("India")) {
            insert1_cc = "\u20B9";
            insert1_rs = "Rs.";
        }else {
            if (str_country.toString().equals("Pound") || str_country.toString().equals("UK Pound")) {
                insert1_cc = "\u00a3";
                insert1_rs = "BP.";
            }else {
                if (str_country.toString().equals("Euro") || str_country.toString().equals("UK Euro")) {
                    insert1_cc = "\u20ac";
                    insert1_rs = "EU.";
                }else {
                    if (str_country.toString().equals("Dollar")) {
                        insert1_cc = "\u0024";
                        insert1_rs = "\u0024";
                    }else {
                        if (str_country.toString().equals("Dinar")) {
                            insert1_cc = "D";
                            insert1_rs = "KD.";
                        }else {
                            if (str_country.toString().equals("Shilling")) {
                                insert1_cc = "S";
                                insert1_rs = "S.";
                            }else {
                                if (str_country.toString().equals("Ringitt")) {
                                    insert1_cc = "R";
                                    insert1_rs = "RM.";
                                }else {
                                    if (str_country.toString().equals("Rial")) {
                                        insert1_cc = "R";
                                        insert1_rs = "OR.";
                                    }else {
                                        if (str_country.toString().equals("Yen")) {
                                            insert1_cc = "\u00a5";
                                            insert1_rs = "\u00a5";
                                        }else {
                                            if (str_country.toString().equals("Papua New Guinean")) {
                                                insert1_cc = "K";
                                                insert1_rs = "K.";
                                            }else {
                                                if (str_country.toString().equals("UAE")) {
                                                    insert1_cc = "D";
                                                    insert1_rs = "DH.";
                                                }else {
                                                    if (str_country.toString().equals("South African Rand")) {
                                                        insert1_cc = "R";
                                                        insert1_rs = "R.";
                                                    }else {
                                                        if (str_country.toString().equals("Congolese Franc")) {
                                                            insert1_cc = "F";
                                                            insert1_rs = "FC.";
                                                        }else {
                                                            if (str_country.toString().equals("Qatari Riyals")) {
                                                                insert1_cc = "QAR";
                                                                insert1_rs = "QAR.";
                                                            }else {
                                                                if (str_country.toString().equals("Dirhams")) {
                                                                    insert1_cc = "AED";
                                                                    insert1_rs = "AED.";
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

        ArrayList<String> my_array = new ArrayList<String>();
        my_array.add("%");
        my_array.add(insert1_cc);
        final ArrayAdapter my_Adapterr1 = new ArrayAdapter(getActivity(), R.layout.spinner_row,
                my_array);
        textViewrspercent.setAdapter(my_Adapterr1);

        countryList = new ArrayList<Country_discount>();
        try {
            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor allrows = db.rawQuery("SELECT * FROM Discount_details", null);
            System.out.println("COUNT : " + allrows.getCount());

            if (allrows.moveToFirst()) {
                do {
                    String ID = allrows.getString(0);
                    String NAme = allrows.getString(1);
                    String PlaCe = allrows.getString(2);
                    String vaLuE = allrows.getString(3);
                    TextView tv = new TextView(getActivity());
                    tv.setText(vaLuE);
                    Country_discount NAME = new Country_discount(ID, NAme, PlaCe, vaLuE);
                    countryList.add(NAME);
                } while (allrows.moveToNext());
            }
            allrows.close();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error encountered.",
                    Toast.LENGTH_LONG);
        }

        dataAdapter = new MyCustomAdapter(getActivity(),
                R.layout.discount_listview, countryList);
        listView = (ListView) rootview.findViewById(R.id.listView);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setTextFilterEnabled(true);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Country_discount country = (Country_discount) parent.getItemAtPosition(position);




                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                Cursor allrows1 = db.rawQuery("SELECT * FROM Discount_details WHERE disc_code = '" + country.getName() + "' AND _id = '" + country.getCode() + "' ", null);
                if (allrows1.moveToFirst()) {
                    do {
                        final String ID = allrows1.getString(0);
                        final String NAme = allrows1.getString(1);
                        VAluE = allrows1.getString(2);
                        String TyPe = allrows1.getString(3);

                        TextView value_1 = new TextView(getActivity());
                        value_1.setText(country.getvalue());
                        TextView value_11 = new TextView(getActivity());
                        value_11.setText(VAluE);
                        TextView value_2 = new TextView(getActivity());
                        value_2.setText(country.getType());
                        TextView value_22 = new TextView(getActivity());
                        value_22.setText(TyPe);

                        if (!value_11.getText().toString().equals(value_1.getText().toString()) ||
                                !value_22.getText().toString().equals(value_2.getText().toString())) {

                            listView.invalidateViews();

                            countryList = new ArrayList<Country_discount>();
                            try {
                                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                Cursor allrows = db.rawQuery("SELECT * FROM Discount_details", null);
                                System.out.println("COUNT : " + allrows.getCount());

                                if (allrows.moveToFirst()) {
                                    do {
                                        String ID1 = allrows.getString(0);
                                        String NAme1 = allrows.getString(1);
                                        String PlaCe = allrows.getString(2);
                                        String vaLuE = allrows.getString(3);
                                        TextView tv = new TextView(getActivity());
                                        tv.setText(vaLuE);
                                        Country_discount NAME = new Country_discount(ID1, NAme1, PlaCe, vaLuE);
                                        countryList.add(NAME);
                                    } while (allrows.moveToNext());
                                }
                                allrows.close();
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), "Error encountered.",
                                        Toast.LENGTH_LONG);
                            }

                            dataAdapter = new MyCustomAdapter(getActivity(),
                                    R.layout.discount_listview, countryList);
                            listView = (ListView) rootview.findViewById(R.id.listView);
                            // Assign adapter to ListView
                            listView.setAdapter(dataAdapter);

                        }else {
                            dialog = new Dialog(getActivity(), R.style.timepicker_date_dialog);
                            dialog.setContentView(R.layout.fragment_update_discount);
                            dialog.setTitle(getString(R.string.title16));
                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            dialog.show();

                            dialogC1_id = (EditText) dialog.findViewById(R.id.editText1);
                            layoutdisname_dialog = (TextInputLayout) dialog.findViewById(R.id.layoutcatname);
                            dialogC1_id.setText(NAme);

                            layoutdisvalue_dialog = (TextInputLayout) dialog.findViewById(R.id.layoutpriorityname);

                            dialogC2_id = (EditText) dialog.findViewById(R.id.editText2);
                            dialogC2_id.setText(VAluE);

                            dialogC3_id = (Spinner) dialog.findViewById(R.id.rspercent);


                            ArrayList<String> my_array = new ArrayList<String>();
                            my_array.add("%");
                            my_array.add(insert1_cc);
                            final ArrayAdapter my_Adapterr1 = new ArrayAdapter(getActivity(), R.layout.spinner_row,
                                    my_array);
                            dialogC3_id.setAdapter(my_Adapterr1);

                            if (TyPe.toString().equals("%")){
                                dialogC3_id.setSelection(0);
                            }else {
                                dialogC3_id.setSelection(1);
                            }

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
                                    layoutdisname_dialog.setError(null);
                                }
                            });

                            dialogC2_id.addTextChangedListener(new TextWatcher() {

                                public void afterTextChanged(Editable s) {
                                }

                                public void beforeTextChanged(CharSequence s, int start,
                                                              int count, int after) {
                                }

                                public void onTextChanged(CharSequence s, int start,
                                                          int before, int count) {
                                    layoutdisvalue_dialog.setError(null);
                                }
                            });

                            Button delete = (Button) dialog.findViewById(R.id.btndelete);
                            delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    hideKeyboard(getContext());

                                    final Dialog dd = new Dialog(getActivity(), R.style.notitle);
                                    dd.setContentView(R.layout.deleted_discount_selected);


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


//                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Discount_details");
//                                            getActivity().getContentResolver().delete(contentUri, where,new String[]{});
//                                            resultUri = new Uri.Builder()
//                                                    .scheme("content")
//                                                    .authority(StubProviderApp.AUTHORITY)
//                                                    .path("Discount_details")
//                                                    .appendQueryParameter("operation", "delete")
//                                                    .appendQueryParameter("_id", ID)
//                                                    .build();
//                                            getActivity().getContentResolver().notifyChange(resultUri, null);


                                            db.delete("Discount_details", where, new String[]{});
                                            webservicequery("delete from Discount_details WHERE _id = '" + ID + "'");

                                            countryList = new ArrayList<Country_discount>();
                                            try {
                                                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                                Cursor allrows = db.rawQuery("SELECT * FROM Discount_details", null);
                                                System.out.println("COUNT : " + allrows.getCount());

                                                if (allrows.moveToFirst()) {
                                                    do {
                                                        String ID = allrows.getString(0);
                                                        String NAme = allrows.getString(1);
                                                        String PlaCe = allrows.getString(2);
                                                        String vaLuE = allrows.getString(3);
                                                        TextView tv = new TextView(getActivity());
                                                        tv.setText(vaLuE);
                                                        Country_discount NAME = new Country_discount(ID, NAme, PlaCe, vaLuE);
                                                        countryList.add(NAME);
                                                    } while (allrows.moveToNext());
                                                }
                                                allrows.close();
                                            } catch (Exception e) {
                                                Toast.makeText(getActivity(), "Error encountered.",
                                                        Toast.LENGTH_LONG);
                                            }

                                            dataAdapter = new MyCustomAdapter(getActivity(),
                                                    R.layout.discount_listview, countryList);
                                            listView = (ListView) rootview.findViewById(R.id.listView);
                                            // Assign adapter to ListView
                                            listView.setAdapter(dataAdapter);

                                            dd.dismiss();
                                            dialog.dismiss();
                                        }
                                    });


                                    dd.show();
                                }
                            });


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
                                            layoutdisname_dialog.setError("Discount code already in use");
                                        }else {
                                            if(dialogC1_id.getText().toString().equals("") || dialogC2_id.getText().toString().equals("")){
                                                //Toast.makeText(getActivity(),"Fill Category name", Toast.LENGTH_SHORT).show();
                                                if (dialogC1_id.getText().toString().equals("")) {
                                                    layoutdisname_dialog.setError("Fill Discount code");
                                                }
                                                if (dialogC2_id.getText().toString().equals("")){
                                                    layoutdisvalue_dialog.setError("Fill Discount value");
                                                }
                                            }
                                            else {

                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("category", dialog_columnvalue);
                                                String where1 = "category = '" + NAme + "' ";
                                                //     db.update("Items", contentValues, where1, new String[]{});

                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                getActivity().getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                                                resultUri = new Uri.Builder()
                                                        .scheme("content")
                                                        .authority(StubProviderApp.AUTHORITY)
                                                        .path("Items")
                                                        .appendQueryParameter("operation", "update")
                                                        .appendQueryParameter("category",NAme)
                                                        .build();
                                                getActivity().getContentResolver().notifyChange(resultUri, null);

                                                String where1_v1 = "category = '" + NAme + "' ";
                                                db.update("Items_Virtual", contentValues, where1_v1, new String[]{});



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

                                    countryList = new ArrayList<Country_discount>();
                                    try {
                                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                        Cursor allrows = db.rawQuery("SELECT * FROM Discount_details", null);
                                        System.out.println("COUNT : " + allrows.getCount());

                                        if (allrows.moveToFirst()) {
                                            do {
                                                String ID = allrows.getString(0);
                                                String NAme = allrows.getString(1);
                                                String PlaCe = allrows.getString(2);
                                                String vaLuE = allrows.getString(3);
                                                TextView tv = new TextView(getActivity());
                                                tv.setText(vaLuE);
                                                Country_discount NAME = new Country_discount(ID, NAme, PlaCe, vaLuE);
                                                countryList.add(NAME);
                                            } while (allrows.moveToNext());
                                        }
                                        allrows.close();
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), "Error encountered.",
                                                Toast.LENGTH_LONG);
                                    }

                                    dataAdapter = new MyCustomAdapter(getActivity(),
                                            R.layout.discount_listview, countryList);
                                    listView = (ListView) rootview.findViewById(R.id.listView);
                                    // Assign adapter to ListView
                                    listView.setAdapter(dataAdapter);

                                }
                            });
                        }


                    } while (allrows1.moveToNext());
                }else {
                    listView.invalidateViews();

                    countryList = new ArrayList<Country_discount>();
                    try {
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("SELECT * FROM Discount_details", null);
                        System.out.println("COUNT : " + allrows.getCount());

                        if (allrows.moveToFirst()) {
                            do {
                                String ID = allrows.getString(0);
                                String NAme = allrows.getString(1);
                                String PlaCe = allrows.getString(2);
                                String vaLuE = allrows.getString(3);
                                TextView tv = new TextView(getActivity());
                                tv.setText(vaLuE);
                                Country_discount NAME = new Country_discount(ID, NAme, PlaCe, vaLuE);
                                countryList.add(NAME);
                            } while (allrows.moveToNext());
                        }
                        allrows.close();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Error encountered.",
                                Toast.LENGTH_LONG);
                    }

                    dataAdapter = new MyCustomAdapter(getActivity(),
                            R.layout.discount_listview, countryList);
                    listView = (ListView) rootview.findViewById(R.id.listView);
                    // Assign adapter to ListView
                    listView.setAdapter(dataAdapter);
                }

//                dialog.setCanceledOnTouchOutside(false);


            }
        });


//        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
//            @Override
//            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
//                final int checkedCount = listView.getCheckedItemCount();
//                String st = dataAdapter.toString();
//                // Set the  CAB title according to total checked items
//                //Toast.makeText(getActivity(), "  Selected44", Toast.LENGTH_SHORT).show();
//                if (listView.isItemChecked(position)) {
//
//                }
//                mode.setTitle(checkedCount + " Selected items ");
//            }
//
//            @Override
//            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                mode.getMenuInflater().inflate(R.menu.multiple_selectall_delete, menu);
//                return true;
//            }
//
//            @Override
//            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            @Override
//            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
//
//                switch (item.getItemId()) {
//                    case R.id.select_all:
//                        final int checkedCount = countryList.size();
//                        for (int i = 0; i < checkedCount; i++) {
//                            listView.setItemChecked(i, true);
//                        }
//                        mode.setTitle(checkedCount + " Selected items ");
//                        return true;
//
//                    case R.id.delete:
//                        final Dialog dialogq = new Dialog(getActivity(), R.style.notitle);
//                        dialogq.setContentView(R.layout.deleted_discount_selected);
//
//                        ImageView imageVieww = (ImageView) dialogq.findViewById(R.id.closetext);
//                        imageVieww.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialogq.dismiss();
//                            }
//                        });
//
//                        Button buttonn = (Button) dialogq.findViewById(R.id.cancel);
//                        buttonn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialogq.dismiss();
//                            }
//                        });
//
//                        Button buttonnn = (Button) dialogq.findViewById(R.id.ok);
//                        buttonnn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
//                                int len = listView.getCount();
//                                SparseBooleanArray checked = listView.getCheckedItemPositions();
//                                for (int i = 0; i < len; i++) {
//                                    if (checked.get(i)) {
//                                        //Country_items item1 = countryList.get(i);
//                                        //Country_items NAME = new Country_items(NAme, PlaCe);
//                                        Country_discount country = countryList.get(i);
//                                        String na = country.getName();
//                                        String an = country.getCode();
//                                        //Toast.makeText(AndroidListViewCustomLayoutActivity.this, " " + item, Toast.LENGTH_SHORT).show();
//                                        //Toast.makeText(getActivity(), "name is " + na + " id is " + an, Toast.LENGTH_SHORT).show();
//  /* do whatever you want with the checked item */
//                                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//                                        String where = "_id = '" + an + "' ";
//                                        db.delete("Discount_details", where, new String[]{});
//
//                                    }
//                                }
//                                listView.invalidateViews();
//
//                                countryList = new ArrayList<Country_discount>();
//                                try {
//                                    db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//                                    Cursor allrows = db.rawQuery("SELECT * FROM Discount_details", null);
//                                    System.out.println("COUNT : " + allrows.getCount());
//
//                                    if (allrows.moveToFirst()) {
//                                        do {
//                                            String ID = allrows.getString(0);
//                                            String NAme = allrows.getString(1);
//                                            String PlaCe = allrows.getString(2);
//                                            String vaLuE = allrows.getString(3);
//                                            TextView tv = new TextView(getActivity());
//                                            tv.setText(vaLuE);
//                                            Country_discount NAME = new Country_discount(ID, NAme, PlaCe, vaLuE);
//                                            countryList.add(NAME);
//                                        } while (allrows.moveToNext());
//                                    }
//                                    allrows.close();
//                                } catch (Exception e) {
//                                    Toast.makeText(getActivity(), "Error encountered.",
//                                            Toast.LENGTH_LONG);
//                                }
//
//                                dataAdapter = new MyCustomAdapter(getActivity(),
//                                        R.layout.discount_listview, countryList);
//                                listView = (ListView) rootview.findViewById(R.id.listView);
//                                // Assign adapter to ListView
//                                listView.setAdapter(dataAdapter);
//
//                                dialogq.dismiss();
//                                mode.finish();
//                            }
//                        });
//
//                        dialogq.show();
//
//
//                        return true;
//                    default:
//                        return false;
//                }
//            }
//
//            @Override
//            public void onDestroyActionMode(ActionMode mode) {
//                mode.invalidate();
//            }
//        });

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO  Auto-generated method stub
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
                mode.getMenuInflater().inflate(R.menu.multiple_selectall_delete, menu);
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
                        mode.setTitle(checkedCount + " Selected items ");
                        return true;

                    case R.id.delete:
                        final Dialog dialogq = new Dialog(getActivity(), R.style.notitle);
                        dialogq.setContentView(R.layout.deleted_discount_selected);

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

                                            Country_discount country = countryList.get(i);
                                            String an = country.getCode();
  /* do whatever you want with the checked item */
                                            String where = "_id = '" + an + "' ";

//                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Discount_details");
//                                            getActivity().getContentResolver().delete(contentUri, where, new String[]{});
//                                            resultUri = new Uri.Builder()
//                                                    .scheme("content")
//                                                    .authority(StubProviderApp.AUTHORITY)
//                                                    .path("Discount_details")
//                                                    .appendQueryParameter("operation", "delete")
//                                                    .appendQueryParameter("_id", an)
//                                                    .build();
//                                            getActivity().getContentResolver().notifyChange(resultUri, null);

                                            db.delete("Discount_details", where, new String[]{});
                                            webservicequery("delete from Discount_details WHERE _id = '" + an + "'");

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

                                countryList = new ArrayList<Country_discount>();
                                try {
                                    db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                    Cursor allrows = db.rawQuery("SELECT * FROM Discount_details", null);
                                    System.out.println("COUNT : " + allrows.getCount());

                                    if (allrows.moveToFirst()) {
                                        do {
                                            String ID = allrows.getString(0);
                                            String NAme = allrows.getString(1);
                                            String PlaCe = allrows.getString(2);
                                            String vaLuE = allrows.getString(3);
                                            TextView tv = new TextView(getActivity());
                                            tv.setText(vaLuE);
                                            Country_discount NAME = new Country_discount(ID, NAme, PlaCe, vaLuE);
                                            countryList.add(NAME);
                                        } while (allrows.moveToNext());
                                    }
                                    allrows.close();
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), "Error encountered.",
                                            Toast.LENGTH_LONG);
                                }

                                dataAdapter = new MyCustomAdapter(getActivity(),
                                        R.layout.discount_listview, countryList);
                                listView = (ListView) rootview.findViewById(R.id.listView);
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
                mode.setTitle(checkedCount + " Selected items ");
                // Calls  toggleSelection method from ListViewAdapter Class
                //adapter.toggleSelection(position);
            }
        });


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
            public void onClick(View view) {
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

                dis_val_layout = (TextInputLayout) rootview.findViewById(R.id.layoutpriorityname);
                dis_val.setError(null);
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

                        countryList = new ArrayList<Country_discount>();
                        try {
                            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                            Cursor allrows = db.rawQuery("SELECT * FROM Discount_details", null);
                            System.out.println("COUNT : " + allrows.getCount());

                            if (allrows.moveToFirst()) {
                                do {
                                    String ID = allrows.getString(0);
                                    String NAme = allrows.getString(1);
                                    String PlaCe = allrows.getString(2);
                                    String vaLuE = allrows.getString(3);
                                    TextView tv = new TextView(getActivity());
                                    tv.setText(vaLuE);
                                    Country_discount NAME = new Country_discount(ID, NAme, PlaCe, vaLuE);
                                    countryList.add(NAME);
                                } while (allrows.moveToNext());
                            }
                            allrows.close();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Error encountered.",
                                    Toast.LENGTH_LONG);
                        }

                        dataAdapter = new MyCustomAdapter(getActivity(),
                                R.layout.discount_listview, countryList);
                        listView = (ListView) rootview.findViewById(R.id.listView);
                        // Assign adapter to ListView
                        listView.setAdapter(dataAdapter);

                        listView.setTextFilterEnabled(true);

                    }
                });
            }
        });


        return rootview;
    }


    void saveInDB() {

        int t_count1 = 1;

        Cursor cursor = db.rawQuery("SELECT * FROM Discount_details", null);
        if (cursor.moveToFirst()) {
            Cursor cursor_ingr = db.rawQuery("SELECT * FROM Discount_details ORDER BY _id DESC LIMIT 0, 1", null);
            cursor_ingr.moveToFirst();
            int t_count = cursor_ingr.getInt(0);
            t_count1 = t_count + 1;
        }

//        webservicequery("ALTER TABLE Discount_details AUTO_INCREMENT = '"+t_count1+"'");

        SQLiteDatabase db = getActivity().openOrCreateDatabase("mydb_Appdata",
                Context.MODE_PRIVATE, null);
        String s = db.getPath();

//        Object position1 = textViewrspercent.getSelectedItem();
//
//        TextView hir = new TextView(getActivity());
//        if (position1.equals("%")) {
//            hir.setText("%");
//        }else {
//
//
//        }

        columnvalue  = text.getText().toString();
        if (text.getText().toString().contains("'")) {
            columnvalue  = text.getText().toString().replaceAll("'", " ");
        }

        ContentValues newValues = new ContentValues();
        Cursor itemnamecheck = db.rawQuery("SELECT * FROM Discount_details WHERE disc_code = '"+columnvalue+"'", null);
        if (itemnamecheck.moveToFirst()){
            layoutdisname.setError("Discount code already in use");
        }else {
            if (text.getText().toString().equals("") || dis_val.getText().toString().equals("")) {
                //Toast.makeText(getActivity(),"Fill Category name", Toast.LENGTH_SHORT).show();
                if (text.getText().toString().equals("")) {
                    layoutdisname.setError("Fill Discount code");
                }
                if (dis_val.getText().toString().equals("")){
                    dis_val_layout.setError("Fill Discount vaue");
                }
            } else {
//                int cou3 = 1;
//                Cursor cursor1 = db.rawQuery("SELECT * FROM Discount_details", null);
//                if (cursor1.moveToFirst()){
//                    do {
//                        String va = cursor1.getString(5);
//
//                        TextView tv = new TextView(getActivity());
//                        tv.setText(va);
//
//                        if (tv.getText().toString().equals("")){
//
//                        }else {
//                            cou3 = cou3+1;
//                        }
//                    }while (cursor1.moveToNext());
//                }

//                Cursor cursor1 = db.rawQuery("SELECT * FROM Hotel WHERE value is not null or value != ''", null);
//                int cou2 = cursor1.getColumnCount();
//                int cou3 = cou2+1;
//                if (dis_val.getText().toString().equals("")){
//                    newValues.put("disc_code", columnvalue);
//                    newValues.put("disc_value", dis_val.getText().toString());
//                    newValues.put("disc_type", )
//                    db.insert("Discount_details", null, newValues);
//                    Toast.makeText(getActivity().getBaseContext(),
//                            "Discount saved", Toast.LENGTH_SHORT).show();
//                    linearLayout.setVisibility(View.GONE);
//                    additem.setVisibility(View.VISIBLE);
//                    EditText one = (EditText) getActivity().findViewById(R.id.editText1);
//                    one.setText("");
//                    hideKeyboard(getContext());
//                }else {
//                    Cursor cursor = db.rawQuery("SELECT * FROM Discount_details WHERE disc_value = '" + dis_val.getText().toString() + "'", null);
//                    if (cursor.moveToFirst()) {
//                        dis_val_layout.setError("proceed with priority " + cou3);
//                    } else {
                newValues.put("disc_code", columnvalue);
                newValues.put("disc_value", dis_val.getText().toString());
                newValues.put("disc_type", textViewrspercent.getSelectedItem().toString());

//                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Discount_details");
//                resultUri = getActivity().getContentResolver().insert(contentUri, newValues);
//                getActivity().getContentResolver().notifyChange(resultUri, null);



                db.insert("Discount_details", null, newValues);

                final String webserviceQuery1="INSERT INTO `Discount_details`(`_id`, `disc_code`, `disc_value`, `disc_type`) " +
                        "VALUES ('"+t_count1+"', '"+columnvalue+"', '"+dis_val.getText().toString()+"', '"+textViewrspercent.getSelectedItem().toString()+"')";

                System.out.println("discount added is "+webserviceQuery1);

                webservicequery(webserviceQuery1);

                Toast.makeText(getActivity().getBaseContext(),
                        "Discount saved", Toast.LENGTH_SHORT).show();
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
//                    }
//                }

            }
        }
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
        if(dialogC1_id.getText().toString().equals("") || dialogC2_id.getText().toString().equals("")){
            //Toast.makeText(getActivity(),"Fill Category name", Toast.LENGTH_SHORT).show();
            if(dialogC1_id.getText().toString().equals("")) {
                layoutdisname_dialog.setError("Fill Discount code");
            }
            if(dialogC2_id.getText().toString().equals("")) {
                layoutdisvalue_dialog.setError("Fill Discount value");
            }
        }
        else{
            if (dialogC2_id.getText().toString().equals(VAluE)) {
                newValues.put("disc_code", dialog_columnvalue);
                newValues.put("disc_value", dialogC2_id.getText().toString());
                newValues.put("disc_type", dialogC3_id.getSelectedItem().toString());
                String where = "_id = ?";

                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Discount_details");
                getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{Integer.toString(_id)});
                resultUri = new Uri.Builder()
                        .scheme("content")
                        .authority(StubProviderApp.AUTHORITY)
                        .path("Discount_details")
                        .appendQueryParameter("operation", "update")
                        .appendQueryParameter("_id", Integer.toString(_id))
                        .build();
                getActivity().getContentResolver().notifyChange(resultUri, null);


//                db.update("Discount_details", newValues, where, new String[]{Integer.toString(_id)});
                Toast.makeText(getActivity().getBaseContext(),
                        "Discount saved", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                hideKeyboard(getContext());
                donotshowKeyboard(getActivity());
                hideKeyboard(getContext());
            }else {
//                int cou3 = 1;
//                Cursor cursor1 = db.rawQuery("SELECT * FROM Hotel", null);
//                if (cursor1.moveToFirst()){
//                    do {
//                        String va = cursor1.getString(5);
//
//                        TextView tv = new TextView(getActivity());
//                        tv.setText(va);
//
//                        if (tv.getText().toString().equals("")){
//
//                        }else {
//                            cou3 = cou3+1;
//                        }
//                    }while (cursor1.moveToNext());
//                }

//                    int cou2 = cursor1.getColumnCount();
//                    int cou3 = cou2 + 1;
//                Cursor cursor = db.rawQuery("SELECT * FROM Hotel WHERE value = '" + dialogC2_id.getText().toString() + "'", null);
//                if (cursor.moveToFirst()) {
//                    layoutdisvalue_dialog.setError("proceed with priority " + cou3);
//                } else {
                newValues.put("disc_code", dialog_columnvalue);
                newValues.put("disc_value", dialogC2_id.getText().toString());
                newValues.put("disc_type", dialogC3_id.getSelectedItem().toString());
                String where = "_id = ?";

                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Discount_details");
                getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{Integer.toString(_id)});
                resultUri = new Uri.Builder()
                        .scheme("content")
                        .authority(StubProviderApp.AUTHORITY)
                        .path("Discount_details")
                        .appendQueryParameter("operation", "update")
                        .appendQueryParameter("_id", Integer.toString(_id))
                        .build();
                getActivity().getContentResolver().notifyChange(resultUri, null);


//                db.update("Discount_details", newValues, where, new String[]{Integer.toString(_id)});
                Toast.makeText(getActivity().getBaseContext(),
                        "Discount saved", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                hideKeyboard(getContext());
                donotshowKeyboard(getActivity());
                hideKeyboard(getContext());
//                }
            }




//                    linearLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void alertas(String alerta) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme);
        AlertDialog.Builder builder = new AlertDialog.Builder(wrapper);
        builder.setIcon(R.drawable.icon);
        builder.setTitle(R.string.app_name);
        builder.setMessage(alerta);
        builder.create().show();
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

    public static void donotshowKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    private class MyCustomAdapter extends ArrayAdapter<Country_discount> {

        private ArrayList<Country_discount> originalList;
        private ArrayList<Country_discount> countryList;
        private CountryFilter filter;

        private Cursor c;
        private Context context;

        private SparseBooleanArray mSelectedItemsIds;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Country_discount> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<Country_discount>();
            this.countryList.addAll(countryList);
            this.originalList = new ArrayList<Country_discount>();
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
//            TextView type;
            TextView inn;
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
                convertView = vi.inflate(R.layout.discount_listview, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.value = (TextView) convertView.findViewById(R.id.prio_value);
                holder.inn = (TextView) convertView.findViewById(R.id.disc_type);
//                holder.inn = (TextView) convertView.findViewById(R.id.inn);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Country_discount country = countryList.get(position);
            holder.code.setText(country.getCode());
            holder.name.setText(country.getName());
            holder.value.setText(country.getvalue());
            holder.inn.setText(country.getType());
            System.out.println("discount type "+country.getName()+" "+country.getType());
//            holder.name.setText(country);
//            holder.continent.setText(country.getContinent());
//            holder.region.setText(country.getRegion());

            if (holder.inn.getText().toString().equals("%")) {

            }else {
                if (str_country.toString().equals("Rupee") || str_country.toString().equals("India")) {
                    insert1_cc = "\u20B9";
                    insert1_rs = "Rs.";
                    holder.inn.setText(insert1_cc);
                } else {
                    if (str_country.toString().equals("Pound") || str_country.toString().equals("UK Pound")) {
                        insert1_cc = "\u00a3";
                        insert1_rs = "BP.";
                        holder.inn.setText(insert1_cc);
                    } else {
                        if (str_country.toString().equals("Euro") || str_country.toString().equals("UK Euro")) {
                            insert1_cc = "\u20ac";
                            insert1_rs = "EU.";
                            holder.inn.setText(insert1_cc);
                        } else {
                            if (str_country.toString().equals("Dollar")) {
                                insert1_cc = "\u0024";
                                insert1_rs = "\u0024";
                                holder.inn.setText(insert1_cc);
                            } else {
                                if (str_country.toString().equals("Dinar")) {
                                    insert1_cc = "D";
                                    insert1_rs = "KD.";
                                    holder.inn.setText(insert1_cc);
                                } else {
                                    if (str_country.toString().equals("Shilling")) {
                                        insert1_cc = "S";
                                        insert1_rs = "S.";
                                        holder.inn.setText(insert1_cc);
                                    } else {
                                        if (str_country.toString().equals("Ringitt")) {
                                            insert1_cc = "R";
                                            insert1_rs = "RM.";
                                            holder.inn.setText(insert1_cc);
                                        } else {
                                            if (str_country.toString().equals("Rial")) {
                                                insert1_cc = "R";
                                                insert1_rs = "OR.";
                                                holder.inn.setText(insert1_cc);
                                            } else {
                                                if (str_country.toString().equals("Yen")) {
                                                    insert1_cc = "\u00a5";
                                                    insert1_rs = "\u00a5";
                                                    holder.inn.setText(insert1_cc);
                                                } else {
                                                    if (str_country.toString().equals("Papua New Guinean")) {
                                                        insert1_cc = "K";
                                                        insert1_rs = "K.";
                                                        holder.inn.setText(insert1_cc);
                                                    }else {
                                                        if (str_country.toString().equals("UAE")) {
                                                            insert1_cc = "D";
                                                            insert1_rs = "DH.";
                                                            holder.inn.setText(insert1_cc);
                                                        }else {
                                                            if (str_country.toString().equals("South African Rand")) {
                                                                insert1_cc = "R";
                                                                insert1_rs = "R.";
                                                                holder.inn.setText(insert1_cc);
                                                            }else {
                                                                if (str_country.toString().equals("Congolese Franc")) {
                                                                    insert1_cc = "F";
                                                                    insert1_rs = "FC.";
                                                                    holder.inn.setText(insert1_cc);
                                                                }else {
                                                                    if (str_country.toString().equals("Qatari Riyals")) {
                                                                        insert1_cc = "QAR";
                                                                        insert1_rs = "QAR.";
                                                                        holder.inn.setText(insert1_cc);
                                                                    }else {
                                                                        if (str_country.toString().equals("Dirhams")) {
                                                                            insert1_cc = "AED";
                                                                            insert1_rs = "AED.";
                                                                            holder.inn.setText(insert1_cc);
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
                    ArrayList<Country_discount> filteredItems = new ArrayList<Country_discount>();

                    for(int i = 0, l = originalList.size(); i < l; i++)
                    {
                        Country_discount country = originalList.get(i);
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

                countryList = (ArrayList<Country_discount>)results.values;
                notifyDataSetChanged();
                clear();
                for(int i = 0, l = countryList.size(); i < l; i++)
                    add(countryList.get(i));
                notifyDataSetInvalidated();
            }
        }


    }


    @Override
    public void onDestroy() {

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


}
