package com.intuition.ivepos;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.intuition.ivepos.csv.RequestSingleton;
import com.intuition.ivepos.syncapp.StubProviderApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;
import static com.intuition.ivepos.sync.SyncHelper.mAccount;
import static com.intuition.ivepos.syncapp.SyncHelperApp.AUTHORITY;

public class DatabaseModifiersActivity extends Fragment {

    public SQLiteDatabase db = null;
    RelativeLayout linearLayout;
    FloatingActionButton additem;
    EditText search, editText1_set;
    LinearLayout holder, dialog_holder;

    String insert1_cc = "", insert1_rs = "", str_country;

    private Button btn;
    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    public ArrayList<EditModel> editModelArrayList;

    ListView listView;
    MyCustomAdapter dataAdapter, dataadapter;
    ArrayList<Country_modifiers> countryList;

    Dialog dialog;
    EditText dialog_editText1_set;

    Uri contentUri,resultUri;

    View rootview;
    RelativeLayout item,category,modifier,tax1, discount1;

    String WebserviceUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        rootview = inflater.inflate(R.layout.fragment_multi_modifier1, null);

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

        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);

        Cursor cursor_country = db.rawQuery("SELECT * FROM Country_Selection", null);
        if (cursor_country.moveToFirst()){
            str_country = cursor_country.getString(1);
        }

        hideKeyboard(getActivity());
        donotshowKeyboard(getActivity());

        search = (EditText) rootview.findViewById(R.id.searchView);

        linearLayout = (RelativeLayout) rootview.findViewById(R.id.add_item);
        additem = (FloatingActionButton) rootview.findViewById(R.id.add_button);

        editText1_set = (EditText) rootview.findViewById(R.id.editText1_set);

        Button btn = (Button) rootview.findViewById(R.id.btndelete);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.GONE);
                additem.setVisibility(View.VISIBLE);
                search.setEnabled(true);
                hideKeyboard(getActivity());
            }
        });

        holder = (LinearLayout) rootview.findViewById(R.id.holder);
        holder.removeAllViews();
//        recyclerView = (RecyclerView) findViewById(R.id.recycler);
//
//        editModelArrayList = populateList();
//        customAdapter = new CustomAdapter(this,editModelArrayList);
//        recyclerView.setAdapter(customAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        final RelativeLayout newEdit = (RelativeLayout) getLayoutInflater().inflate(R.layout.rv_item, holder, false);
        final EditText myEditText1 = newEdit.findViewById(R.id.editText1);
        final EditText myEditText2 = newEdit.findViewById(R.id.editText2);
        //add it to the holder
        holder.addView(newEdit);
        //set the text change lisnter
        myEditText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //here we decide if we have to add a new EditText view or to
                //remove the current
                if ((s.length() == 0 && holder.getChildCount() > 1) || myEditText1.getText().toString().equals("") || myEditText2.getText().toString().equals("")) {
                    holder.removeView(newEdit);
                } else if (s.length() > 0 && ((before + start) == 0)) {
                    addNewEdit();
                    myEditText1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        countryList = new ArrayList<Country_modifiers>();
        try {
            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor allrows = db.rawQuery("SELECT * FROM Modifiers_new GROUP BY modifierset", null);
            System.out.println("COUNT : " + allrows.getCount());

            if (allrows.moveToFirst()) {
                do {
                    String ID = allrows.getString(0);
                    String NAme = allrows.getString(1);
                    String options = allrows.getString(2);
                    Country_modifiers NAME = new Country_modifiers(ID, NAme);
                    //Country_modifiers PLACE = new Country_modifiers(PlaCe, PlaCe, PlaCe, PlaCe);
                    countryList.add(NAME);
                    //countryList.add(PLACE);
                } while (allrows.moveToNext());
            }
            allrows.close();
//                        db.close();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error encountered.",
                    Toast.LENGTH_LONG);
        }




        dataAdapter = new MyCustomAdapter(getActivity(),
                R.layout.modifiers_list_details, countryList);
        listView = (ListView) rootview.findViewById(R.id.listView);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                final Country_modifiers country = (Country_modifiers) parent.getItemAtPosition(position);

                dialog = new Dialog(getActivity(), R.style.timepicker_date_dialog);
                dialog.setContentView(R.layout.fragment_update_modifiernew);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog.show();

                final CheckBox dialog_single_select = (CheckBox) dialog.findViewById(R.id.dialog_single_select);

                final String NAme = country.getName();

                ImageButton frameLayout1 = (ImageButton) dialog.findViewById(R.id.btncancel);
                frameLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        hideKeyboard(getActivity());
                        donotshowKeyboard(getActivity());
                    }
                });

                dialog_editText1_set = (EditText) dialog.findViewById(R.id.dialog_editText1_set);
                dialog_editText1_set.setText(country.getName());

                dialog_holder = (LinearLayout) dialog.findViewById(R.id.holder);



                Cursor cursor = db.rawQuery("SELECT * FROM Modifiers_new WHERE modifierset = '"+NAme+"'", null);
                if (cursor.moveToFirst()){
                    do {
                        final RelativeLayout newEdit = (RelativeLayout) dialog.getLayoutInflater().inflate(R.layout.rv_item, dialog_holder, false);

                        String name = cursor.getString(2);
                        String pr = cursor.getString(3);
                        String qu = cursor.getString(4);
                        String sing_select = cursor.getString(5);

                        TextView tv = new TextView(getActivity());
                        tv.setText(sing_select);

                        if (tv.getText().toString().toString().equals("yes")){
                            dialog_single_select.setChecked(true);
                        }else {
                            dialog_single_select.setChecked(false);
                        }

                        final EditText myEditText1 = newEdit.findViewById(R.id.editText1);
                        final EditText myEditText2 = newEdit.findViewById(R.id.editText2);

                        myEditText1.setText(name);
                        myEditText2.setText(pr);

                        dialog_holder.addView(newEdit);


                        final ImageView remove_modifier = newEdit.findViewById(R.id.remove_modifier);
                        remove_modifier.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getActivity(), myEditText1.getText().toString(), Toast.LENGTH_LONG).show();
                                dialog_holder.removeView(newEdit);
                            }
                        });

                    }while (cursor.moveToNext());
                }

                final RelativeLayout newEdit = (RelativeLayout) dialog.getLayoutInflater().inflate(R.layout.rv_item, dialog_holder, false);

                final EditText myEditText1 = newEdit.findViewById(R.id.editText1);
                final EditText myEditText2 = newEdit.findViewById(R.id.editText2);
                //add it to the holder
                dialog_holder.addView(newEdit);
                //set the text change lisnter
                myEditText2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //here we decide if we have to add a new EditText view or to
                        //remove the current
                        if ((s.length() == 0 && dialog_holder.getChildCount() > 1) || myEditText1.getText().toString().equals("") || myEditText2.getText().toString().equals("")) {
                            dialog_holder.removeView(newEdit);
                        } else if (s.length() > 0 && ((before + start) == 0)) {
                            addNewEdit_dialog();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


                Button btndelete = (Button) dialog.findViewById(R.id.btndelete);
                btndelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dd = new Dialog(getActivity(), R.style.notitle);
                        dd.setContentView(R.layout.deleted_modifier_selected);
                        dd.show();

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

                                db.execSQL("delete from Modifiers_new WHERE modifierset = '"+NAme+"'");
                                if (isDeviceOnline()) {
                                    webservicequery("delete from Modifiers_new WHERE modifierset = '" + NAme + "'");
                                }else {

                                    Bundle extras=new Bundle();
                                    extras.putString("query","delete from Modifiers_new WHERE modifierset = '"+NAme+"'");

                                    extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL,true);
                                    ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
                                    ContentResolver.requestSync(null, AUTHORITY, extras);
                                }

//                                String where = "_id = '" + ID + "' ";
//                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers_new");
//                                getContentResolver().delete(contentUri, where, new String[]{});
//                                resultUri = new Uri.Builder()
//                                        .scheme("content")
//                                        .authority(StubProviderApp.AUTHORITY)
//                                        .path("Modifiers_new")
//                                        .appendQueryParameter("operation", "delete")
//                                        .appendQueryParameter("_id", ID)
//                                        .build();
//                                getContentResolver().notifyChange(resultUri, null);

//                                String where1_v1 = "modifierset = '" + NAme + "' ";
//                                db.delete("Modifiers_new", where1_v1, new String[]{});

                                countryList = new ArrayList<Country_modifiers>();
                                try {
                                    db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                    Cursor allrows = db.rawQuery("SELECT * FROM Modifiers_new GROUP BY modifierset", null);
                                    System.out.println("COUNT : " + allrows.getCount());

                                    if (allrows.moveToFirst()) {
                                        do {
                                            String ID = allrows.getString(0);
                                            String NAme = allrows.getString(1);
                                            Country_modifiers NAME = new Country_modifiers(ID, NAme);
                                            //Country_modifiers PLACE = new Country_modifiers(PlaCe, PlaCe, PlaCe, PlaCe);
                                            countryList.add(NAME);
                                            //countryList.add(PLACE);
                                        } while (allrows.moveToNext());
                                    }
                                    allrows.close();
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), "Error encountered.",
                                            Toast.LENGTH_LONG);
                                }

                                dataAdapter = new MyCustomAdapter(getActivity(),
                                        R.layout.modifiers_list_details, countryList);
                                listView = (ListView) rootview.findViewById(R.id.listView);
                                listView.setAdapter(dataAdapter);
                                dd.dismiss();
                                dialog.dismiss();

                            }
                        });

                    }
                });


                Button btnsave = (Button) dialog.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (NAme.toString().equals(dialog_editText1_set.getText().toString())){

                            db.execSQL("delete from Modifiers_new WHERE modifierset = '"+NAme+"'");
                            webservicequery("delete from Modifiers_new WHERE modifierset = '" + NAme + "'");

                            ContentValues contentValues = new ContentValues();
                            int count = dialog_holder.getChildCount();
                            for (int i = 0; i < count - 1; i++) {
                                final View row = dialog_holder.getChildAt(i);
                                EditText editText1 = (EditText) row.findViewById(R.id.editText1);
                                EditText editText2 = (EditText) row.findViewById(R.id.editText2);
                                String data = editText1.getText().toString();
                                System.out.println("edittext valure are " + data);

                                contentValues.put("modifierset", NAme);
                                contentValues.put("modifiername", editText1.getText().toString());
                                contentValues.put("modprice", editText2.getText().toString());
                                if (dialog_single_select.isChecked()){
                                    contentValues.put("items", "yes");
                                }else {
                                    contentValues.put("items", "no");
                                }
//                              db.insert("Modifiers_new", null, contentValues);

                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers_new");
                                resultUri = getActivity().getContentResolver().insert(contentUri, contentValues);
                                getActivity().getContentResolver().notifyChange(resultUri, null);
                            }
                            dialog.dismiss();


                            countryList = new ArrayList<Country_modifiers>();
                            try {
                                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                Cursor allrows = db.rawQuery("SELECT * FROM Modifiers_new GROUP BY modifierset", null);
                                System.out.println("COUNT : " + allrows.getCount());

                                if (allrows.moveToFirst()) {
                                    do {
                                        String ID = allrows.getString(0);
                                        String NAme = allrows.getString(1);
                                        Country_modifiers NAME = new Country_modifiers(ID, NAme);
                                        //Country_modifiers PLACE = new Country_modifiers(PlaCe, PlaCe, PlaCe, PlaCe);
                                        countryList.add(NAME);
                                        //countryList.add(PLACE);
                                    } while (allrows.moveToNext());
                                }
                                allrows.close();
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), "Error encountered.",
                                        Toast.LENGTH_LONG);
                            }

                            dataAdapter = new MyCustomAdapter(getActivity(),
                                    R.layout.modifiers_list_details, countryList);
                            listView = (ListView) rootview.findViewById(R.id.listView);
                            listView.setAdapter(dataAdapter);


                        }else {
                            Cursor cursor1 = db.rawQuery("SELECT * FROM Modifiers_new WHERE modifierset = '"+dialog_editText1_set.getText().toString()+"'", null);
                            if (cursor1.moveToFirst()) {
                                dialog_editText1_set.setError("Modifier set already used");
                            }else {

                                db.execSQL("UPDATE Items SET mod_set1 = '"+dialog_editText1_set.getText().toString()+"' WHERE mod_set1 = '"+NAme+"'");
                                db.execSQL("UPDATE Items SET mod_set2 = '"+dialog_editText1_set.getText().toString()+"' WHERE mod_set1 = '"+NAme+"'");
                                db.execSQL("UPDATE Items SET mod_set3 = '"+dialog_editText1_set.getText().toString()+"' WHERE mod_set1 = '"+NAme+"'");
                                db.execSQL("UPDATE Items SET mod_set4 = '"+dialog_editText1_set.getText().toString()+"' WHERE mod_set1 = '"+NAme+"'");
                                db.execSQL("UPDATE Items SET mod_set5 = '"+dialog_editText1_set.getText().toString()+"' WHERE mod_set1 = '"+NAme+"'");

                                webservicequery("UPDATE Items SET mod_set1 = '"+dialog_editText1_set.getText().toString()+"' WHERE mod_set1 = '"+NAme+"'");
                                webservicequery("UPDATE Items SET mod_set2 = '"+dialog_editText1_set.getText().toString()+"' WHERE mod_set1 = '"+NAme+"'");
                                webservicequery("UPDATE Items SET mod_set3 = '"+dialog_editText1_set.getText().toString()+"' WHERE mod_set1 = '"+NAme+"'");
                                webservicequery("UPDATE Items SET mod_set4 = '"+dialog_editText1_set.getText().toString()+"' WHERE mod_set1 = '"+NAme+"'");
                                webservicequery("UPDATE Items SET mod_set5 = '"+dialog_editText1_set.getText().toString()+"' WHERE mod_set1 = '"+NAme+"'");

                                db.execSQL("delete from Modifiers_new WHERE modifierset = '"+NAme+"'");
                                webservicequery("delete from Modifiers_new WHERE modifierset = '" + NAme + "'");

                                ContentValues contentValues = new ContentValues();
                                int count = dialog_holder.getChildCount();
                                for (int i = 0; i < count - 1; i++) {
                                    final View row = dialog_holder.getChildAt(i);
                                    EditText editText1 = (EditText) row.findViewById(R.id.editText1);
                                    EditText editText2 = (EditText) row.findViewById(R.id.editText2);
                                    String data = editText1.getText().toString();
                                    System.out.println("edittext valure are " + data);

                                    contentValues.put("modifierset", dialog_editText1_set.getText().toString());
                                    contentValues.put("modifiername", editText1.getText().toString());
                                    contentValues.put("modprice", editText2.getText().toString());
//                                  db.insert("Modifiers_new", null, contentValues);

                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers_new");
                                    resultUri = getActivity().getContentResolver().insert(contentUri, contentValues);
                                    getActivity().getContentResolver().notifyChange(resultUri, null);
                                }
                                dialog.dismiss();

                                countryList = new ArrayList<Country_modifiers>();
                                try {
                                    db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                    Cursor allrows = db.rawQuery("SELECT * FROM Modifiers_new GROUP BY modifierset", null);
                                    System.out.println("COUNT : " + allrows.getCount());

                                    if (allrows.moveToFirst()) {
                                        do {
                                            String ID = allrows.getString(0);
                                            String NAme = allrows.getString(1);
                                            Country_modifiers NAME = new Country_modifiers(ID, NAme);
                                            //Country_modifiers PLACE = new Country_modifiers(PlaCe, PlaCe, PlaCe, PlaCe);
                                            countryList.add(NAME);
                                            //countryList.add(PLACE);
                                        } while (allrows.moveToNext());
                                    }
                                    allrows.close();
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), "Error encountered.",
                                            Toast.LENGTH_LONG);
                                }

                                dataAdapter = new MyCustomAdapter(getActivity(),
                                        R.layout.modifiers_list_details, countryList);
                                listView = (ListView) rootview.findViewById(R.id.listView);
                                listView.setAdapter(dataAdapter);
                            }

                        }
                    }
                });




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

        final CheckBox single_select = (CheckBox) rootview.findViewById(R.id.single_select);

        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.VISIBLE);
                additem.setVisibility(View.GONE);
                search.setEnabled(false);

                holder.removeAllViews();
                single_select.setChecked(false);

                editText1_set.setText("");

                final RelativeLayout newEdit = (RelativeLayout) getLayoutInflater().inflate(R.layout.rv_item, holder, false);
                final EditText myEditText1 = newEdit.findViewById(R.id.editText1);
                final EditText myEditText2 = newEdit.findViewById(R.id.editText2);
                //add it to the holder
                holder.addView(newEdit);
                //set the text change lisnter
                myEditText2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //here we decide if we have to add a new EditText view or to
                        //remove the current
                        if ((s.length() == 0 && holder.getChildCount() > 1) || myEditText1.getText().toString().equals("") || myEditText2.getText().toString().equals("")) {
                            holder.removeView(newEdit);
                        } else if (s.length() > 0 && ((before + start) == 0)) {
                            addNewEdit();
                            myEditText1.requestFocus();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        });

        Button btnsave = (Button) rootview.findViewById(R.id.btnsave);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor cursor = db.rawQuery("SELECT * FROM Modifiers_new WHERE modifierset = '"+editText1_set.getText().toString()+"'", null);
                if (cursor.moveToFirst()){
                    editText1_set.setError("Modifier set already used");
                }else {
                    linearLayout.setVisibility(View.GONE);
                    additem.setVisibility(View.VISIBLE);
                    search.setEnabled(true);
                    hideKeyboard(getActivity());

                    ContentValues contentValues = new ContentValues();
                    int count = holder.getChildCount();
                    for (int i = 0; i < count - 1; i++) {
                        final View row = holder.getChildAt(i);
                        EditText editText1 = (EditText) row.findViewById(R.id.editText1);
                        EditText editText2 = (EditText) row.findViewById(R.id.editText2);
                        String data = editText1.getText().toString();
                        System.out.println("edittext valure are " + data);

                        contentValues.put("modifierset", editText1_set.getText().toString());
                        contentValues.put("modifiername", editText1.getText().toString());
                        contentValues.put("modprice", editText2.getText().toString());
                        if (single_select.isChecked()){
                            contentValues.put("items", "yes");
                        }else {
                            contentValues.put("items", "no");
                        }
//                        db.insert("Modifiers_new", null, contentValues);

                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers_new");
                        resultUri = getActivity().getContentResolver().insert(contentUri, contentValues);
                        getActivity().getContentResolver().notifyChange(resultUri, null);
                    }

                    countryList = new ArrayList<Country_modifiers>();
                    try {
                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor allrows = db.rawQuery("SELECT * FROM Modifiers_new GROUP BY modifierset", null);
                        System.out.println("COUNT : " + allrows.getCount());

                        if (allrows.moveToFirst()) {
                            do {
                                String ID = allrows.getString(0);
                                String NAme = allrows.getString(1);
                                Country_modifiers NAME = new Country_modifiers(ID, NAme);
                                //Country_modifiers PLACE = new Country_modifiers(PlaCe, PlaCe, PlaCe, PlaCe);
                                countryList.add(NAME);
                                //countryList.add(PLACE);
                            } while (allrows.moveToNext());
                        }
                        allrows.close();
//                        db.close();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Error encountered.",
                                Toast.LENGTH_LONG);
                    }

                    dataAdapter = new MyCustomAdapter(getActivity(),
                            R.layout.modifiers_list_details, countryList);
                    listView = (ListView) rootview.findViewById(R.id.listView);
                    // Assign adapter to ListView
                    listView.setAdapter(dataAdapter);


                }
                cursor.close();

            }
        });

        return rootview;
    }

    private void addNewEdit() {
        //inflate a new EditText from the layout
        final RelativeLayout newEdit = (RelativeLayout) getLayoutInflater().inflate(R.layout.rv_item, holder, false);
        final EditText myEditText1 = newEdit.findViewById(R.id.editText1);
        final EditText myEditText2 = newEdit.findViewById(R.id.editText2);
        //add it to the holder
        holder.addView(newEdit);
        //set the text change lisnter
        myEditText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //here we decide if we have to add a new EditText view or to
                //remove the current
                if ((s.length() == 0 && holder.getChildCount() > 1) || myEditText1.getText().toString().equals("") || myEditText2.getText().toString().equals("")) {
                    holder.removeView(newEdit);
                } else if (s.length() > 0 && ((before + start) == 0)) {
                    addNewEdit();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void addNewEdit_dialog() {
        //inflate a new EditText from the layout
        final RelativeLayout newEdit = (RelativeLayout) dialog.getLayoutInflater().inflate(R.layout.rv_item, dialog_holder, false);
        final EditText myEditText1 = newEdit.findViewById(R.id.editText1);
        final EditText myEditText2 = newEdit.findViewById(R.id.editText2);
        //add it to the holder
        dialog_holder.addView(newEdit);
        //set the text change lisnter
        myEditText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //here we decide if we have to add a new EditText view or to
                //remove the current
                if ((s.length() == 0 && dialog_holder.getChildCount() > 1) || myEditText1.getText().toString().equals("") || myEditText2.getText().toString().equals("")) {
                    dialog_holder.removeView(newEdit);
                } else if (s.length() > 0 && ((before + start) == 0)) {
                    addNewEdit_dialog();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    private ArrayList<EditModel> populateList(){

        ArrayList<EditModel> list = new ArrayList<>();

//        for(int i = 0; i < 8; i++){
        EditModel editModel = new EditModel();
        list.add(editModel);
//        }

        return list;
    }

    private class MyCustomAdapter extends ArrayAdapter<Country_modifiers> {

        private ArrayList<Country_modifiers> originalList;
        private ArrayList<Country_modifiers> countryList;
        private CountryFilter filter;

        private Cursor c;
        private Context context;

        private SparseBooleanArray mSelectedItemsIds;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Country_modifiers> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<Country_modifiers>();
            this.countryList.addAll(countryList);
            this.originalList = new ArrayList<Country_modifiers>();
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
            TextView name;
            TextView options;
            Button item_sel;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));
            if (convertView == null) {

                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.modifiers_list_details, null);

                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.options = (TextView) convertView.findViewById(R.id.options);
                holder.item_sel = (Button) convertView.findViewById(R.id.item_selection);

                final ViewHolder finalHolder = holder;
                holder.item_sel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE mod_set1 = '"+finalHolder.name.getText().toString()+"' OR mod_set2 = '"+finalHolder.name.getText().toString()+"'" +
                                "OR mod_set3 = '"+finalHolder.name.getText().toString()+"' OR mod_set4 = '"+finalHolder.name.getText().toString()+"'" +
                                "OR mod_set5 = '"+finalHolder.name.getText().toString()+"'", null);
                        if (cursor.moveToFirst()){
                            System.out.println("clicked item is there");
                            Intent intent = new Intent(getActivity(), Modifier_Multiselection_Items.class);
                            intent.putExtra("PLAYER1NAME", "update");
                            intent.putExtra("modifier_option_name", finalHolder.name.getText().toString());
                            startActivityForResult(intent, 1);
                            System.out.println("clicked items is "+finalHolder.name.getText().toString());
                        }else {
                            System.out.println("clicked item is not there");
                            Intent intent = new Intent(getActivity(), Modifier_Multiselection_Items.class);
                            intent.putExtra("PLAYER1NAME", "insert");
                            intent.putExtra("modifier_option_name", finalHolder.name.getText().toString());
                            startActivityForResult(intent, 1);
                            System.out.println("clicked items is "+finalHolder.name.getText().toString());
                        }
                    }
                });

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Country_modifiers country = countryList.get(position);
            holder.name.setText(country.getName());

            List<String> list = new ArrayList<>();

            Cursor cursor = db.rawQuery("SELECT * FROM Modifiers_new WHERE modifierset = '"+country.getName()+"'", null);
            if (cursor.moveToFirst()){
                do {
                    String opti = cursor.getString(2);
                    list.add(opti);
                }while (cursor.moveToNext());
            }

            String s = TextUtils.join(", ", list);
            holder.options.setText(s);
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
//            holder.name = (TextView) v.findViewById(R.id.name);
//

//            String firstName = this.c.getString(this.c.getColumnIndex("name"));
//            int price = c.getInt(c.getColumnIndex("price"));
//
//            TextView fname = (TextView) v.findViewById(R.id.name);
//            fname.setText(firstName);
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
                    ArrayList<Country_modifiers> filteredItems = new ArrayList<Country_modifiers>();

                    for(int i = 0, l = originalList.size(); i < l; i++)
                    {
                        Country_modifiers country = originalList.get(i);
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

                countryList = (ArrayList<Country_modifiers>)results.values;
                notifyDataSetChanged();
                clear();
                for(int i = 0, l = countryList.size(); i < l; i++)
                    add(countryList.get(i));
                notifyDataSetInvalidated();
            }
        }


    }

    public void webservicequery(final String webserviceQuery){


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getActivity());
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

//    public void itemselection(View view) {
//
//        RelativeLayout parentRow = (RelativeLayout) view.getParent();
//
//        TextView quantityView = (TextView) parentRow.findViewById(R.id.name);
//        String quantityString = quantityView.getText().toString();
//            Toast.makeText(getActivity(), ""+quantityString,
//                    Toast.LENGTH_SHORT).show();
//    }

}
