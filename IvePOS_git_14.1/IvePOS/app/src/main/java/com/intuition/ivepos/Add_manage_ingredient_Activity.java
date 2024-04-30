package com.intuition.ivepos;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

/**
 * Created by Rohithkumar on 1/5/2017.
 */

public class Add_manage_ingredient_Activity extends AppCompatActivity {

    public SQLiteDatabase db = null;
    Uri contentUri,resultUri;


    EditText editText1, editText2, editText4, editText5, editText_indiv, editText_barcode;
    TextView item_selection;
    Spinner unit_type;

    TextInputLayout editText1_t, editText2_t, editText4_t, editText5_t;
    Object position1;

    RelativeLayout linearLayout;
    FloatingActionButton addingredient;

    MyCustomAdapter dataAdapterr;
    ArrayList<Ingredient_activity_listitems> countryList;
    ListView listView;


    EditText editText1_edit, editText2_edit, editText4_edit, editText_barcode_edit, editText5_edit;
    TextView unit_price_value1;
    TextInputLayout editText1_t_edit, editText2_t_edit, editText4_t_edit, editText5_t_edit;

    String ingr_name;
    Object position1_edit;
    Spinner unit_type_edit;

    String seli = "1";



    ArrayAdapter<Country_Ingredient> adapter;
    ArrayAdapter<Country_Ingredient> adapter1;
    ArrayList<Country_Ingredient> list = new ArrayList<Country_Ingredient>();

    String getday, req, act_st = "";

    private int year, year1;
    private int month, month1;
    private int day, day1;
    String onee, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve;
    String onee1, two1, three1, four1, five1, six1, seven1, eight1, nine1, ten1, eleven1, twelve1;


    TextView item_used_value;

    String insert1_cc = "", insert1_rs = "", str_country;

    String WebserviceUrl;
    String account_selection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_main_ingredient_manager);



        donotshowKeyboard(this);

        item_used_value = new TextView(Add_manage_ingredient_Activity.this);
        item_selection = new TextView(Add_manage_ingredient_Activity.this);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(Add_manage_ingredient_Activity.this);
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

        final EditText search = (EditText) findViewById(R.id.searchView);

        linearLayout = (RelativeLayout) findViewById(R.id.add_item);

        addingredient = (FloatingActionButton) findViewById(R.id.add_button1);

        LinearLayout closeadd = (LinearLayout) findViewById(R.id.closeadd);
        closeadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(Add_manage_ingredient_Activity.this, "hii", Toast.LENGTH_SHORT).show();
                linearLayout.setVisibility(View.GONE);
                addingredient.setVisibility(View.VISIBLE);

//                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                hideKeyboard(Add_manage_ingredient_Activity.this);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    donotshowKeyboard(Add_manage_ingredient_Activity.this);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }

            }
        });

        Button btndelete = (Button) findViewById(R.id.btndelete);
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.GONE);
                addingredient.setVisibility(View.VISIBLE);

//                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                hideKeyboard(Add_manage_ingredient_Activity.this);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    donotshowKeyboard(Add_manage_ingredient_Activity.this);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }

            }
        });


        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText4 = (EditText) findViewById(R.id.editText4);
        editText5 = (EditText) findViewById(R.id.editText5);
        editText_indiv = (EditText) findViewById(R.id.editText_indiv);
        unit_type = (Spinner) findViewById(R.id.unit_selection);
        editText_barcode = (EditText) findViewById(R.id.editText_barcode);




        editText1_t = (TextInputLayout) findViewById(R.id.layout_ingredientname);
        editText2_t = (TextInputLayout) findViewById(R.id.editText2_t);
        editText4_t = (TextInputLayout) findViewById(R.id.editText4_t);

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editText1_t.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editText2_t.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editText4_t.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        addingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor del = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp", null);
                if (del.moveToFirst()){
                    do {
                        String id = del.getString(0);
                        String wherecu = "_id = '" + id + "'";


                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients_item_selection_temp");
                        getContentResolver().delete(contentUri, wherecu,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("Ingredients_item_selection_temp")
                                .appendQueryParameter("operation", "delete")
                                .appendQueryParameter("_id", id)
                                .build();
                        getContentResolver().notifyChange(resultUri, null);





               //         db.delete("Ingredients_item_selection_temp", wherecu, new String[]{});
                    }while (del.moveToNext());
                }

                linearLayout.setVisibility(View.VISIBLE);
                addingredient.setVisibility(View.GONE);

                final EditText one = (EditText) findViewById(R.id.editText1);
                final TextInputLayout layoutingredientname = (TextInputLayout) findViewById(R.id.layout_ingredientname);
                one.setText("");
                layoutingredientname.setError(null);

//                selectedImagePath = null;
//                selectedImageUri = null;
                //hideKeyboard(getContext());
                one.requestFocus();

                editText1.setText("");
                editText2.setText("");
                editText4.setText("");
                editText5.setText("");
                editText_indiv.setText("");
                editText_barcode.setText("");
                unit_type.setSelection(0);


                final EditText five = (EditText) findViewById(R.id.editText5);

//                final Spinner unit_type = (Spinner) findViewById(R.id.unit_selection);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(one, InputMethodManager.SHOW_IMPLICIT);

                item_selection = (TextView) findViewById(R.id.items_multi_selection);
                item_selection.setText("");
                item_selection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (one.getText().toString().equals("") || one.getText().toString().equals(" ")){
                            Toast.makeText(Add_manage_ingredient_Activity.this, "Enter ingredient name", Toast.LENGTH_SHORT).show();
                        }else {

                            Intent intent = new Intent(Add_manage_ingredient_Activity.this, Add_Items_List1.class);
                            intent.putExtra("PLAYER1NAME", one.getText().toString());
                            intent.putExtra("PLAYER2NAME", five.getText().toString());
                            if (editText4.getText().toString().equals("")){
                                intent.putExtra("PLAYER3NAME", "0");
                            }else {
                                intent.putExtra("PLAYER3NAME", editText4.getText().toString());
                            }
                            position1 = unit_type.getSelectedItem();
                            intent.putExtra("position", position1.toString());
                            startActivityForResult(intent, 1);
//                            startActivity(intent);
                        }
                    }
                });


                Button save = (Button) findViewById(R.id.btnsave);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor ingredient_unique = db.rawQuery("SELECT * FROM Ingredients WHERE ingredient_name == '"+editText1.getText().toString()+"'", null);
                        if (ingredient_unique.moveToFirst()){
                            editText1_t.setError("Ingredient name already in use");
                        }else {
                            saveInDB();
                        }

                    }
                });

            }
        });


        ImageView back_pressed = (ImageView) findViewById(R.id.back_pressed);
        back_pressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(Add_manage_ingredient_Activity.this);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    donotshowKeyboard(Add_manage_ingredient_Activity.this);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }

                if (account_selection.toString().equals("Dine")) {
                    Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                    if (cursor3.moveToFirst()) {
                        String lite_pro = cursor3.getString(1);

                        TextView tv = new TextView(Add_manage_ingredient_Activity.this);
                        tv.setText(lite_pro);

                        if (tv.getText().toString().equals("Lite")) {
                            Intent intent = new Intent(Add_manage_ingredient_Activity.this, BeveragesMenuFragment_Dine_l.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(Add_manage_ingredient_Activity.this, BeveragesMenuFragment_Dine.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }else {
                        Intent intent = new Intent(Add_manage_ingredient_Activity.this, BeveragesMenuFragment_Dine_l.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }else {
                    if (account_selection.toString().equals("Qsr")) {
                        Intent intent = new Intent(Add_manage_ingredient_Activity.this, BeveragesMenuFragment_Qsr.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(Add_manage_ingredient_Activity.this, BeveragesMenuFragment_Retail.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
//                Intent intent = new Intent(Add_manage_ingredient_Activity.this, BeveragesMenuFragment_Dine.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
            }
        });



        listView = (ListView) findViewById(R.id.listView);

        countryList = new ArrayList<Ingredient_activity_listitems>();

        try {
            Cursor allrows = db.rawQuery("Select * from Ingredients ORDER BY ingredient_name ASC", null);
            System.out.println("COUNT : " + allrows.getCount());


            //Country_items country = new Country_items(name, name, name, name);

            if (allrows.moveToFirst()) {
                do {
                    String ID = allrows.getString(0);
                    String NAme = allrows.getString(1);
                    String BArCOde = allrows.getString(15);
                    String MIn = allrows.getString(2);
                    String MaX = allrows.getString(3);
                    String CuRReNt = allrows.getString(4);
                    String PRiCe = allrows.getString(13);
                    String UNiT = allrows.getString(5);
                    Ingredient_activity_listitems NAME = new Ingredient_activity_listitems(NAme,BArCOde, UNiT, MIn, MaX, CuRReNt, PRiCe);
                    //Country_items PLACE = new Country_items(PlaCe, PlaCe, PlaCe, PlaCe);
                    countryList.add(NAME);
                    //countryList.add(PLACE);
                } while (allrows.moveToNext());
            }
            allrows.close();
//                db.close();
        } catch (Exception e) {
            Toast.makeText(Add_manage_ingredient_Activity.this, "Error encountered.",
                    Toast.LENGTH_LONG);
        }

        dataAdapterr = new MyCustomAdapter(Add_manage_ingredient_Activity.this,
                R.layout.ingredient_listview, countryList);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapterr);

        final EditText myFilter = (EditText) findViewById(R.id.searchView);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapterr.getFilter().filter(s.toString());
            }
        });

        ImageView deleteicon = (ImageView) findViewById(R.id.delete_icon);
        deleteicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFilter.setText("");
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                final Ingredient_activity_listitems country = (Ingredient_activity_listitems) parent.getItemAtPosition(position);

                Cursor del = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp", null);
                if (del.moveToFirst()){
                    do {
                        String id = del.getString(0);
                        String wherecu = "_id = '" + id + "'";


                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients_item_selection_temp");
                        getContentResolver().delete(contentUri, wherecu,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("Ingredients_item_selection_temp")
                                .appendQueryParameter("operation", "delete")
                                .appendQueryParameter("_id", id)
                                .build();
                        getContentResolver().notifyChange(resultUri, null);



                 //       db.delete("Ingredients_item_selection_temp", wherecu, new String[]{});
                    }while (del.moveToNext());
                }

                final String ingr_id = country.getCode();
                ingr_name = country.getName();
                String ingr_min = country.getmin();
                String ingr_opt = country.getopt();
                String ingr_max= country.getmax();
                final String ingr_current_st = country.getcurrent();
                final String ingr_unit = country.getunit();

                Cursor cursor = db.rawQuery("SELECT * FROM Ingredient_items_list WHERE ingredient_name = '"+ingr_name+"'", null);
                if (cursor.moveToFirst()){
                    do {
                        String ingr_na = cursor.getString(2);
                        String qt = cursor.getString(3);
                        String uni = cursor.getString(10);

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("itemname", ingr_na);
                        contentValues.put("qty_temp", qt);
                        contentValues.put("qty_temp_unit", uni);
                        contentValues.put("qty", "yes");

                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients_item_selection_temp");
                        resultUri = getContentResolver().insert(contentUri, contentValues);
                        getContentResolver().notifyChange(resultUri, null);


               //         db.insert("Ingredients_item_selection_temp", null, contentValues);

                    }while (cursor.moveToNext());
                }

//                Toast.makeText(Add_manage_ingredient_Activity.this, " "+ingr_name, Toast.LENGTH_SHORT).show();

                final Dialog dialog_ingredient_manager_edit = new Dialog(Add_manage_ingredient_Activity.this, R.style.timepicker_date_dialog);
                dialog_ingredient_manager_edit.setContentView(R.layout.dialog_ingredient_manager_edit);
                dialog_ingredient_manager_edit.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog_ingredient_manager_edit.show();

                TextView inn = (TextView) dialog_ingredient_manager_edit.findViewById(R.id.inn);
                inn.setText(insert1_cc);

                ImageButton btncancel = (ImageButton) dialog_ingredient_manager_edit.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_ingredient_manager_edit.dismiss();
                    }
                });

                unit_type_edit = (Spinner) dialog_ingredient_manager_edit.findViewById(R.id.unit_selection);

                editText1_edit = (EditText) dialog_ingredient_manager_edit.findViewById(R.id.editText1_edit);
                editText2_edit = (EditText) dialog_ingredient_manager_edit.findViewById(R.id.editText2_edit);
                editText4_edit = (EditText) dialog_ingredient_manager_edit.findViewById(R.id.editText4_edit);
                editText5_edit = (EditText) dialog_ingredient_manager_edit.findViewById(R.id.editText5_edit);
                unit_price_value1 = (TextView) dialog_ingredient_manager_edit.findViewById(R.id.unit_price_value1);
                editText_barcode_edit = (EditText) dialog_ingredient_manager_edit.findViewById(R.id.editText_barcode_edit);


                editText1_t_edit = (TextInputLayout) dialog_ingredient_manager_edit.findViewById(R.id.editText1_t_edit);
                editText2_t_edit = (TextInputLayout) dialog_ingredient_manager_edit.findViewById(R.id.editText2_t_edit);
                editText4_t_edit = (TextInputLayout) dialog_ingredient_manager_edit.findViewById(R.id.editText4_t_edit);



                editText1_edit.setText(ingr_name);
                editText2_edit.setText(ingr_min);
                editText4_edit.setText(ingr_max);

                TextView vg = new TextView(Add_manage_ingredient_Activity.this);
                vg.setText(ingr_current_st);
                if (vg.getText().toString().equals("")){
                    editText5_edit.setText("0");
                }else {
                    editText5_edit.setText(ingr_current_st);
                }
                Cursor ccom = db.rawQuery("SELECT * FROM Ingredients WHERE ingredient_name = '"+ingr_name+"'", null);
                if (ccom.moveToFirst()){
                    String sc = ccom.getString(13);
                    String barc = ccom.getString(15);
                    editText_barcode_edit.setText(barc);
                    unit_price_value1.setText(sc);
                }
//                unit_price_value1.setText();
                if (ingr_unit.toString().equals("Kg")) {
                    unit_type_edit.setSelection(0);
                }
                if (ingr_unit.toString().equals("gm")) {
                    unit_type_edit.setSelection(1);
                }
                if (ingr_unit.toString().equals("mg")) {
                    unit_type_edit.setSelection(2);
                }
                if (ingr_unit.toString().equals("pound")) {
                    unit_type_edit.setSelection(3);
                }
                if (ingr_unit.toString().equals("L")) {
                    unit_type_edit.setSelection(4);
                }
                if (ingr_unit.toString().equals("ml")) {
                    unit_type_edit.setSelection(5);
                }
                if (ingr_unit.toString().equals("galloon")) {
                    unit_type_edit.setSelection(6);
                }
                if (ingr_unit.toString().equals("no.")) {
                    unit_type_edit.setSelection(7);
                }
                if (ingr_unit.toString().equals("teaspoon")) {
                    unit_type_edit.setSelection(8);
                }
                if (ingr_unit.toString().equals("tablespoon")) {
                    unit_type_edit.setSelection(9);
                }
                if (ingr_unit.toString().equals("dozen")) {
                    unit_type_edit.setSelection(10);
                }

                final float ca = (Float.parseFloat(editText4_edit.getText().toString())) - (Float.parseFloat(editText5_edit.getText().toString()));

                TextView unit_price_value = (TextView) dialog_ingredient_manager_edit.findViewById(R.id.unit_price_value);
                unit_price_value.setText(String.valueOf(ca));

                final ImageView im_req_ok_green = (ImageView) dialog_ingredient_manager_edit.findViewById(R.id.im_req_ok);
                final ImageView im_req_fill_red = (ImageView) dialog_ingredient_manager_edit.findViewById(R.id.im_req_fill);

                if (Float.parseFloat(editText5_edit.getText().toString()) < Float.parseFloat(editText2_edit.getText().toString())){
                    im_req_fill_red.setVisibility(View.VISIBLE);
                    im_req_ok_green.setVisibility(View.GONE);
                }else {
                    im_req_fill_red.setVisibility(View.GONE);
                    im_req_ok_green.setVisibility(View.VISIBLE);
                }

                item_used_value = (TextView) dialog_ingredient_manager_edit.findViewById(R.id.item_used_value);
                Cursor cursor2 = db.rawQuery("SELECT COUNT(itemname) FROM Ingredient_items_list WHERE ingredient_name = '"+ingr_name+"'", null);
//                final int co = cursor2.getCount();
                if (cursor2.moveToFirst()) {
                    int leveliss = cursor2.getInt(0);
                    String totalbillis = String.valueOf(leveliss);
                    item_used_value.setText(totalbillis);
                }


//                item_used_value.setText(String.valueOf(co));

                editText1_edit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        editText1_t_edit.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                editText2_edit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        editText2_t_edit.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                editText4_edit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        editText4_t_edit.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                Button item_usage = (Button) dialog_ingredient_manager_edit.findViewById(R.id.item_usage);
                item_usage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Add_manage_ingredient_Activity.this, Add_Items_List1.class);
                        intent.putExtra("PLAYER1NAME", editText1_edit.getText().toString());
                        intent.putExtra("PLAYER2NAME", editText5_edit.getText().toString());
                        if (editText4_edit.getText().toString().equals("")){
                            intent.putExtra("PLAYER3NAME", "0");
                        }else {
                            intent.putExtra("PLAYER3NAME", editText4_edit.getText().toString());
                        }
                        position1 = unit_type.getSelectedItem();
                        intent.putExtra("position", position1.toString());
                        startActivityForResult(intent, 1);
                    }
                });


                ImageButton update = (ImageButton) dialog_ingredient_manager_edit.findViewById(R.id.btnsave);
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (editText1_edit.getText().toString().equals("")){
                            editText1_t_edit.setError("Enter ingredient name");
                        }else {
                            if (editText1_edit.getText().toString().equals(ingr_name)){
                                updateInDB(dialog_ingredient_manager_edit);
                            }else {
                                Cursor ingredient_unique = db.rawQuery("SELECT * FROM Ingredients WHERE ingredient_name == '"+editText1_edit.getText().toString()+"'", null);
                                if (ingredient_unique.moveToFirst()){
                                    editText1_t_edit.setError("Ingredient name already in use");
                                }else {
                                    updateInDB(dialog_ingredient_manager_edit);
                                }
                            }
                        }

                    }
                });


                Button btndelete = (Button) dialog_ingredient_manager_edit.findViewById(R.id.btndelete);
                btndelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog = new Dialog(Add_manage_ingredient_Activity.this, R.style.timepicker_date_dialog);
                        dialog.setContentView(R.layout.dialog_ingredient_delete_warning);
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialog.show();

                        ImageView close = (ImageView) dialog.findViewById(R.id.closetext);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        Button cancel = (Button) dialog.findViewById(R.id.cancel);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        Button ok = (Button) dialog.findViewById(R.id.ok);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

//                                webservicequery("delete from Ingredients WHERE ingredient_name = '" + ingr_name + "' OR ingredient_name = '"+editText1_edit.getText().toString()+"'");
//                                db.execSQL("delete from Ingredients WHERE ingredient_name = '" + ingr_name + "' OR ingredient_name = '"+editText1_edit.getText().toString()+"'");

                                Cursor cursor21 = db.rawQuery("SELECT * FROM Ingredients WHERE ingredient_name = '"+ingr_name+"' OR ingredient_name = '"+editText1_edit.getText().toString()+"'", null);
                                if (cursor21.moveToFirst()){
                                    do {
                                        String id = cursor21.getString(0);
                                        String wherecu = "_id = '" + id + "'";


                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients");
                                        getContentResolver().delete(contentUri, wherecu,new String[]{});
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProviderApp.AUTHORITY)
                                                .path("Ingredients")
                                                .appendQueryParameter("operation", "delete")
                                                .appendQueryParameter("_id", id)
                                                .build();
                                        getContentResolver().notifyChange(resultUri, null);




                                    //    db.delete("Ingredients", wherecu, new String[]{});
                                    }while (cursor21.moveToNext());
                                }

//                                webservicequery("delete from Ingredient_items_list WHERE ingredient_name = '" + ingr_name + "' OR ingredient_name = '"+editText1_edit.getText().toString()+"'");
//                                db.execSQL("delete from Ingredient_items_list WHERE ingredient_name = '" + ingr_name + "' OR ingredient_name = '"+editText1_edit.getText().toString()+"'");

                                Cursor cursor22 = db.rawQuery("SELECT * FROM Ingredient_items_list WHERE ingredient_name = '"+ingr_name+"' OR ingredient_name = '"+editText1_edit.getText().toString()+"'", null);
                                if (cursor22.moveToFirst()){
                                    do {
                                        String id = cursor22.getString(0);
                                        String wherecu = "_id = '" + id + "'";


                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients");
                                        getContentResolver().delete(contentUri, wherecu,new String[]{});
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProviderApp.AUTHORITY)
                                                .path("Ingredients")
                                                .appendQueryParameter("operation", "delete")
                                                .appendQueryParameter("_id", id)
                                                .build();
                                        getContentResolver().notifyChange(resultUri, null);



                             //           db.delete("Ingredients", wherecu, new String[]{});
                                    }while (cursor22.moveToNext());
                                }

                                countryList = new ArrayList<Ingredient_activity_listitems>();

                                if (seli.toString().equals("1")) {
                                    try {
                                        Cursor allrows = db.rawQuery("Select * from Ingredients ORDER BY ingredient_name ASC", null);
                                        System.out.println("COUNT : " + allrows.getCount());


                                        //Country_items country = new Country_items(name, name, name, name);

                                        if (allrows.moveToFirst()) {
                                            do {
                                                String ID = allrows.getString(0);
                                                String NAme = allrows.getString(1);
                                                String BArCOde = allrows.getString(15);
                                                String MIn = allrows.getString(2);
                                                String MaX = allrows.getString(3);
                                                String CuRReNt = allrows.getString(4);
                                                String PRiCe = allrows.getString(13);
                                                String UNiT = allrows.getString(5);
                                                Ingredient_activity_listitems NAME = new Ingredient_activity_listitems(NAme, BArCOde, UNiT, MIn, MaX, CuRReNt, PRiCe);
                                                //Country_items PLACE = new Country_items(PlaCe, PlaCe, PlaCe, PlaCe);
                                                countryList.add(NAME);
                                                //countryList.add(PLACE);
                                            } while (allrows.moveToNext());
                                        }
                                        allrows.close();
//                db.close();
                                    } catch (Exception e) {
                                        Toast.makeText(Add_manage_ingredient_Activity.this, "Error encountered.",
                                                Toast.LENGTH_LONG);
                                    }

                                    dataAdapterr = new MyCustomAdapter(Add_manage_ingredient_Activity.this,
                                            R.layout.ingredient_listview, countryList);
                                    // Assign adapter to ListView
                                    listView.setAdapter(dataAdapterr);
                                }
                                if (seli.toString().equals("2")){
                                    try {
                                        Cursor allrows = db.rawQuery("Select * from Ingredients ORDER BY date ASC", null);
                                        System.out.println("COUNT : " + allrows.getCount());


                                        //Country_items country = new Country_items(name, name, name, name);

                                        if (allrows.moveToFirst()) {
                                            do {
                                                String ID = allrows.getString(0);
                                                String NAme = allrows.getString(1);
                                                String BArCOde = allrows.getString(15);
                                                String MIn = allrows.getString(2);
                                                String MaX = allrows.getString(3);
                                                String CuRReNt = allrows.getString(4);
                                                String PRiCe = allrows.getString(13);
                                                String UNiT = allrows.getString(5);
                                                Ingredient_activity_listitems NAME = new Ingredient_activity_listitems(NAme, BArCOde, UNiT, MIn, MaX, CuRReNt, PRiCe);
                                                //Country_items PLACE = new Country_items(PlaCe, PlaCe, PlaCe, PlaCe);
                                                countryList.add(NAME);
                                                //countryList.add(PLACE);
                                            } while (allrows.moveToNext());
                                        }
                                        allrows.close();
//                db.close();
                                    } catch (Exception e) {
                                        Toast.makeText(Add_manage_ingredient_Activity.this, "Error encountered.",
                                                Toast.LENGTH_LONG);
                                    }

                                    dataAdapterr = new MyCustomAdapter(Add_manage_ingredient_Activity.this,
                                            R.layout.ingredient_listview, countryList);
                                    // Assign adapter to ListView
                                    listView.setAdapter(dataAdapterr);
                                }
                                if (seli.toString().equals("3")){
                                    try {
                                        Cursor allrows = db.rawQuery("Select * from Ingredients ORDER BY _id ASC", null);
                                        System.out.println("COUNT : " + allrows.getCount());


                                        //Country_items country = new Country_items(name, name, name, name);

                                        if (allrows.moveToFirst()) {
                                            do {
                                                String ID = allrows.getString(0);
                                                String NAme = allrows.getString(1);
                                                String BArCOde = allrows.getString(15);
                                                String MIn = allrows.getString(2);
                                                String MaX = allrows.getString(3);
                                                String CuRReNt = allrows.getString(4);
                                                String PRiCe = allrows.getString(13);
                                                String UNiT = allrows.getString(5);
                                                Ingredient_activity_listitems NAME = new Ingredient_activity_listitems(NAme, BArCOde, UNiT, MIn, MaX, CuRReNt, PRiCe);
                                                //Country_items PLACE = new Country_items(PlaCe, PlaCe, PlaCe, PlaCe);
                                                countryList.add(NAME);
                                                //countryList.add(PLACE);
                                            } while (allrows.moveToNext());
                                        }
                                        allrows.close();
//                db.close();
                                    } catch (Exception e) {
                                        Toast.makeText(Add_manage_ingredient_Activity.this, "Error encountered.",
                                                Toast.LENGTH_LONG);
                                    }

                                    dataAdapterr = new MyCustomAdapter(Add_manage_ingredient_Activity.this,
                                            R.layout.ingredient_listview, countryList);
                                    // Assign adapter to ListView
                                    listView.setAdapter(dataAdapterr);
                                }

                                dialog_ingredient_manager_edit.dismiss();
                                dialog.dismiss();
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
                final Dialog dialog_filter = new Dialog(Add_manage_ingredient_Activity.this, R.style.timepicker_date_dialog);
                dialog_filter.setContentView(R.layout.dialog_ingredient_report_filter);
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

                final RadioGroup filter_selection = (RadioGroup) dialog_filter.findViewById(R.id.filter_selection);

                final RadioButton alphbetically = (RadioButton) dialog_filter.findViewById(R.id.working_hours);
                final RadioButton date_added = (RadioButton) dialog_filter.findViewById(R.id.all_time);
                final RadioButton date_modified = (RadioButton) dialog_filter.findViewById(R.id.pending_list);

                if (seli.toString().equals("1")) {
                    alphbetically.setChecked(true);
                }
                if (seli.toString().equals("2")) {
                    date_added.setChecked(true);
                }
                if (seli.toString().equals("3")) {
                    date_modified.setChecked(true);
                }


                if (!alphbetically.isChecked() && !date_added.isChecked() && !date_modified.isChecked()){
                    alphbetically.setChecked(true);
                }

                Button quantityapply_filter = (Button) dialog_filter.findViewById(R.id.quantityapply_filter);
                quantityapply_filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (alphbetically.isChecked()){
                            seli = getString(R.string.Button9);
                            countryList = new ArrayList<Ingredient_activity_listitems>();

                            try {
                                Cursor allrows = db.rawQuery("Select * from Ingredients ORDER BY ingredient_name ASC", null);
                                System.out.println("COUNT : " + allrows.getCount());


                                //Country_items country = new Country_items(name, name, name, name);

                                if (allrows.moveToFirst()) {
                                    do {
                                        String ID = allrows.getString(0);
                                        String NAme = allrows.getString(1);
                                        String BArCOde = allrows.getString(15);
                                        String MIn = allrows.getString(2);
                                        String MaX = allrows.getString(3);
                                        String CuRReNt = allrows.getString(4);
                                        String PRiCe = allrows.getString(13);
                                        String UNiT = allrows.getString(5);
                                        Ingredient_activity_listitems NAME = new Ingredient_activity_listitems(NAme,BArCOde, UNiT, MIn, MaX, CuRReNt, PRiCe);
                                        //Country_items PLACE = new Country_items(PlaCe, PlaCe, PlaCe, PlaCe);
                                        countryList.add(NAME);
                                        //countryList.add(PLACE);
                                    } while (allrows.moveToNext());
                                }
                                allrows.close();
                            } catch (Exception e) {
                                Toast.makeText(Add_manage_ingredient_Activity.this, "Error encountered.",
                                        Toast.LENGTH_LONG);
                            }

                            dataAdapterr = new MyCustomAdapter(Add_manage_ingredient_Activity.this,
                                    R.layout.ingredient_listview, countryList);
                            // Assign adapter to ListView
                            listView.setAdapter(dataAdapterr);
                            dialog_filter.dismiss();
                        }
                        if (date_added.isChecked()){
                            seli = getString(R.string.Button10);
                            countryList = new ArrayList<Ingredient_activity_listitems>();

                            try {
                                Cursor allrows = db.rawQuery("Select * from Ingredients ORDER BY date ASC", null);
                                System.out.println("COUNT : " + allrows.getCount());


                                //Country_items country = new Country_items(name, name, name, name);

                                if (allrows.moveToFirst()) {
                                    do {
                                        String ID = allrows.getString(0);
                                        String NAme = allrows.getString(1);
                                        String BArCOde = allrows.getString(15);
                                        String MIn = allrows.getString(2);
                                        String MaX = allrows.getString(3);
                                        String CuRReNt = allrows.getString(4);
                                        String PRiCe = allrows.getString(13);
                                        String UNiT = allrows.getString(5);
                                        Ingredient_activity_listitems NAME = new Ingredient_activity_listitems(NAme,BArCOde, UNiT, MIn, MaX, CuRReNt, PRiCe);
                                        //Country_items PLACE = new Country_items(PlaCe, PlaCe, PlaCe, PlaCe);
                                        countryList.add(NAME);
                                        //countryList.add(PLACE);
                                    } while (allrows.moveToNext());
                                }
                                allrows.close();
                            } catch (Exception e) {
                                Toast.makeText(Add_manage_ingredient_Activity.this, "Error encountered.",
                                        Toast.LENGTH_LONG);
                            }

                            dataAdapterr = new MyCustomAdapter(Add_manage_ingredient_Activity.this,
                                    R.layout.ingredient_listview, countryList);
                            // Assign adapter to ListView
                            listView.setAdapter(dataAdapterr);
                            dialog_filter.dismiss();
                        }
                        if (date_modified.isChecked()){
                            seli = getString(R.string.Button11);
                            countryList = new ArrayList<Ingredient_activity_listitems>();

                            try {
                                Cursor allrows = db.rawQuery("Select * from Ingredients ORDER BY _id ASC", null);
                                System.out.println("COUNT : " + allrows.getCount());


                                //Country_items country = new Country_items(name, name, name, name);

                                if (allrows.moveToFirst()) {
                                    do {
                                        String ID = allrows.getString(0);
                                        String NAme = allrows.getString(1);
                                        String BArCOde = allrows.getString(15);
                                        String MIn = allrows.getString(2);
                                        String MaX = allrows.getString(3);
                                        String CuRReNt = allrows.getString(4);
                                        String PRiCe = allrows.getString(13);
                                        String UNiT = allrows.getString(5);
                                        Ingredient_activity_listitems NAME = new Ingredient_activity_listitems(NAme,BArCOde, UNiT, MIn, MaX, CuRReNt, PRiCe);
                                        //Country_items PLACE = new Country_items(PlaCe, PlaCe, PlaCe, PlaCe);
                                        countryList.add(NAME);
                                        //countryList.add(PLACE);
                                    } while (allrows.moveToNext());
                                }
                                allrows.close();
                            } catch (Exception e) {
                                Toast.makeText(Add_manage_ingredient_Activity.this, "Error encountered.",
                                        Toast.LENGTH_LONG);
                            }

                            dataAdapterr = new MyCustomAdapter(Add_manage_ingredient_Activity.this,
                                    R.layout.ingredient_listview, countryList);
                            // Assign adapter to ListView
                            listView.setAdapter(dataAdapterr);
                            dialog_filter.dismiss();
                        }
                    }
                });

            }
        });


        LinearLayout linearLayout_ingredient_report = (LinearLayout) findViewById(R.id.linearLayout_ingredient_report);
        linearLayout_ingredient_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_manage_ingredient_Activity.this, Micro_Inventory_Indent_Ingredients_History.class);
                startActivity(intent);
            }
        });


        LinearLayout linearLayout_hist = (LinearLayout) findViewById(R.id.linearLayout_hist);
        linearLayout_hist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_manage_ingredient_Activity.this, Micro_Inventory_Indent_History.class);
                startActivity(intent);
            }
        });

        LinearLayout linearLayout_vend = (LinearLayout) findViewById(R.id.linearLayout_vend);
        linearLayout_vend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_manage_ingredient_Activity.this, Micro_Inventory_Indent_Vendor_list.class);
                startActivity(intent);
            }
        });

        LinearLayout linearLayout_indent = (LinearLayout) findViewById(R.id.linearLayout_indent);
        linearLayout_indent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_manage_ingredient_Activity.this, Micro_inventory_indent.class);
                intent.putExtra("hii", "2");
                startActivity(intent);
            }
        });

        LinearLayout linearLayout_overflow = findViewById(R.id.linearLayout_overflow);
        final PopupMenu popup = new PopupMenu(this, linearLayout_overflow);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.ingredient_menu, popup.getMenu());
        linearLayout_overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.linearLayout_vend) {
                            Intent intent = new Intent(Add_manage_ingredient_Activity.this, Micro_Inventory_Indent_Vendor_list.class);
                            startActivity(intent);
                        }
                        if (id == R.id.linearLayout_indent) {
                            Intent intent = new Intent(Add_manage_ingredient_Activity.this, Micro_inventory_indent.class);
                            intent.putExtra("hii", "2");
                            startActivity(intent);
                        }
                        return true;
                    }
                });
            }
        });

        webservicequery("ALTER TABLE `ingredients` CHANGE `_id` `_id` INT(11) NOT NULL AUTO_INCREMENT;");

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Add_manage_ingredient_Activity.this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public static void donotshowKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    void saveInDB() {
        Cursor cursor_ingr = db.rawQuery("SELECT COUNT(*) FROM Ingredients", null);
        cursor_ingr.moveToFirst();
        int t_count = cursor_ingr.getInt(0);
        int t_count1 = t_count+1;

        position1 = unit_type.getSelectedItem();
        if (editText1.getText().toString().equals("")){
            if (editText1.getText().toString().equals("")){
                editText1_t.setError("Enter ingredient name");
            }
        }else {
//            Toast.makeText(Add_manage_ingredient_Activity.this, "can save", Toast.LENGTH_SHORT).show();

            String min_req = "0", max_req = "0", current_stock = "0", indiv_price = "0", indiv_price_temp = "0", avg_price = "0", required = "0";
            if (editText4.getText().toString().equals("")){
                editText4.setText("0");
            }
            if (editText5.getText().toString().equals("")){
                editText5.setText("0");
            }
            ContentValues contentValues = new ContentValues();
            webservicequery("ALTER TABLE Ingredients AUTO_INCREMENT = "+(t_count1));
//            contentValues.put("_id", (t_count+1));
            contentValues.put("ingredient_name", editText1.getText().toString());
            contentValues.put("barcode", editText_barcode.getText().toString());
            if (editText2.getText().toString().equals("")){
                contentValues.put("min_req", "0");
                min_req = "0";
            }else {
                contentValues.put("min_req", editText2.getText().toString());
                min_req = editText2.getText().toString();
            }
            if (editText4.getText().toString().equals("")){
                contentValues.put("max_req", "0");
                max_req = "0";
            }else {
                contentValues.put("max_req", editText4.getText().toString());
                max_req = editText4.getText().toString();
            }

            if (editText5.getText().toString().equals("")){
                contentValues.put("current_stock", "0");
                current_stock = "0";
            }else {
                contentValues.put("current_stock", editText5.getText().toString());
                current_stock = editText5.getText().toString();
            }
            contentValues.put("unit", position1.toString());
            if (editText_indiv.getText().toString().equals("")){
                contentValues.put("indiv_price", "0");
                contentValues.put("indiv_price_temp", "0");
                contentValues.put("avg_price", "0");
                indiv_price = "0";
                indiv_price_temp = "0";
                avg_price = "0";

            }else {
                contentValues.put("indiv_price", editText_indiv.getText().toString());
                contentValues.put("indiv_price_temp", editText_indiv.getText().toString());
                contentValues.put("avg_price", editText_indiv.getText().toString());
                indiv_price = editText_indiv.getText().toString();
                indiv_price_temp = editText_indiv.getText().toString();
                avg_price = editText_indiv.getText().toString();
            }



            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy MM dd");
            final String currentDateandTime1 = sdf2.format(new Date());

            SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM yyyy");
            final String currentDateandTime2 = sdf3.format(new Date());

            Date dt = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
            final String time1 = sdf1.format(dt);

            Date dt1 = new Date();
            SimpleDateFormat sdf11 = new SimpleDateFormat("kkmm");
            final String time11 = sdf11.format(dt1);

            Date dtt_new = new Date();
            SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");
            final String time24_new = sdf1t_new.format(dtt_new);

            contentValues.put("date1", currentDateandTime2);
            contentValues.put("time1", time1);
            contentValues.put("time", time11);
            contentValues.put("date", time24_new);
            contentValues.put("datetimee_new", time24_new);


            if (editText5.getText().toString().equals("")){
                contentValues.put("required", editText4.getText().toString());
                required = editText4.getText().toString();
            }else {
                float a = (Float.parseFloat(editText4.getText().toString()) - Float.parseFloat(editText5.getText().toString()));
                contentValues.put("required", String.valueOf(a));
                required = String.valueOf(a);
            }

//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients");
//            resultUri = getContentResolver().insert(contentUri, contentValues);
//            getContentResolver().notifyChange(resultUri, null);

            final String webserviceQuery1="INSERT INTO `Ingredients`(`ingredient_name`, `barcode`, `min_req`, `max_req`, `current_stock`, `unit`, `indiv_price`, `indiv_price_temp`, `avg_price`, `date1`, `time1`, `time`, `date`, `datetimee_new`, `required`) " +
                    "VALUES ('"+editText1.getText().toString()+"','"+editText_barcode.getText().toString()+"','"+min_req+"','"+max_req+"','"+current_stock+"','"+position1.toString()+"','"+indiv_price+"','"+indiv_price_temp+"','"+avg_price+"','"+currentDateandTime2+"','"+time1+"','"+time11+"','"+time24_new+"','"+time24_new+"','"+required+"')";
            webservicequery(webserviceQuery1);

            System.out.println("data is "+webserviceQuery1);

            db.insert("Ingredients", null, contentValues);


            linearLayout.setVisibility(View.GONE);
            addingredient.setVisibility(View.VISIBLE);

            countryList = new ArrayList<Ingredient_activity_listitems>();

            try {
                Cursor allrows = db.rawQuery("Select * from Ingredients ORDER BY ingredient_name ASC", null);
                System.out.println("COUNT : " + allrows.getCount());


                //Country_items country = new Country_items(name, name, name, name);

                if (allrows.moveToFirst()) {
                    do {
                        String ID = allrows.getString(0);
                        String NAme = allrows.getString(1);
                        String BArCOde = allrows.getString(15);
                        String MIn = allrows.getString(2);
                        String MaX = allrows.getString(3);
                        String CuRReNt = allrows.getString(4);
                        String PRiCe = allrows.getString(13);
                        String UNiT = allrows.getString(5);
                        Ingredient_activity_listitems NAME = new Ingredient_activity_listitems(NAme, BArCOde, UNiT, MIn, MaX, CuRReNt, PRiCe);
                        //Country_items PLACE = new Country_items(PlaCe, PlaCe, PlaCe, PlaCe);
                        countryList.add(NAME);
                        //countryList.add(PLACE);
                    } while (allrows.moveToNext());
                }
                allrows.close();
//                db.close();
            } catch (Exception e) {
                Toast.makeText(Add_manage_ingredient_Activity.this, "Error encountered.",
                        Toast.LENGTH_LONG);
            }

            dataAdapterr = new MyCustomAdapter(Add_manage_ingredient_Activity.this,
                    R.layout.ingredient_listview, countryList);
            // Assign adapter to ListView
            listView.setAdapter(dataAdapterr);


            Cursor c = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp", null);
            if (c.moveToFirst()){
                do {
                    String id = c.getString(0);
                    String item_name = c.getString(1);
                    String qty_temp = c.getString(2);
                    String qty_unit = c.getString(3);

//                    Cursor i = db.rawQuery("SELECT * FROM Items WHERE itemname = '"+item_name+"'", null);
//                    if (i.moveToFirst()){
//                        String id_i = i.getString(0);

                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("ingredient_name", editText1.getText().toString());
                    contentValues1.put("itemname", item_name);
                    contentValues1.put("item_qyt_used", qty_temp);
                    contentValues1.put("currnet_stock", editText5.getText().toString());
                    contentValues1.put("date1", currentDateandTime2);
                    contentValues1.put("date", time24_new);
                    contentValues1.put("time1", time1);
                    contentValues1.put("time", time11);
                    contentValues1.put("modified_datetimee_new", time24_new);
                    contentValues1.put("qty_unit", qty_unit);
                    if (editText5.getText().toString().equals("")){
                        contentValues1.put("required", editText4.getText().toString());
                    }else {
                        float a = (Float.parseFloat(editText4.getText().toString()) - Float.parseFloat(editText5.getText().toString()));
                        contentValues1.put("required", String.valueOf(a));
                    }

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_items_list");
                    resultUri = getContentResolver().insert(contentUri, contentValues1);
                    getContentResolver().notifyChange(resultUri, null);

//                        String wherecu = "_id = '" + id_i + "'";
               //     db.insert("Ingredient_items_list", null, contentValues1);
//                    }



                }while (c.moveToNext());
            }

//            hideKeyboard(getContext());
//            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//            seli = getString(R.string.Button9);

            hideKeyboard(Add_manage_ingredient_Activity.this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                donotshowKeyboard(Add_manage_ingredient_Activity.this);

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }

            countryList = new ArrayList<Ingredient_activity_listitems>();

            if (seli.toString().equals("1")) {
                try {
                    Cursor allrows = db.rawQuery("Select * from Ingredients ORDER BY ingredient_name ASC", null);
                    System.out.println("COUNT : " + allrows.getCount());


                    //Country_items country = new Country_items(name, name, name, name);

                    if (allrows.moveToFirst()) {
                        do {
                            String ID = allrows.getString(0);
                            String NAme = allrows.getString(1);
                            String BArCOde = allrows.getString(15);
                            String MIn = allrows.getString(2);
                            String MaX = allrows.getString(3);
                            String CuRReNt = allrows.getString(4);
                            String PRiCe = allrows.getString(13);
                            String UNiT = allrows.getString(5);
                            Ingredient_activity_listitems NAME = new Ingredient_activity_listitems(NAme, BArCOde, UNiT, MIn, MaX, CuRReNt, PRiCe);
                            //Country_items PLACE = new Country_items(PlaCe, PlaCe, PlaCe, PlaCe);
                            countryList.add(NAME);
                            //countryList.add(PLACE);
                        } while (allrows.moveToNext());
                    }
                    allrows.close();
//                db.close();
                } catch (Exception e) {
                    Toast.makeText(Add_manage_ingredient_Activity.this, "Error encountered.",
                            Toast.LENGTH_LONG);
                }

                dataAdapterr = new MyCustomAdapter(Add_manage_ingredient_Activity.this,
                        R.layout.ingredient_listview, countryList);
                // Assign adapter to ListView
                listView.setAdapter(dataAdapterr);
            }
            if (seli.toString().equals("2")){
                try {
                    Cursor allrows = db.rawQuery("Select * from Ingredients ORDER BY date ASC", null);
                    System.out.println("COUNT : " + allrows.getCount());


                    //Country_items country = new Country_items(name, name, name, name);

                    if (allrows.moveToFirst()) {
                        do {
                            String ID = allrows.getString(0);
                            String NAme = allrows.getString(1);
                            String BArCOde = allrows.getString(15);
                            String MIn = allrows.getString(2);
                            String MaX = allrows.getString(3);
                            String CuRReNt = allrows.getString(4);
                            String PRiCe = allrows.getString(13);
                            String UNiT = allrows.getString(5);
                            Ingredient_activity_listitems NAME = new Ingredient_activity_listitems(NAme, BArCOde, UNiT, MIn, MaX, CuRReNt, PRiCe);
                            //Country_items PLACE = new Country_items(PlaCe, PlaCe, PlaCe, PlaCe);
                            countryList.add(NAME);
                            //countryList.add(PLACE);
                        } while (allrows.moveToNext());
                    }
                    allrows.close();
//                db.close();
                } catch (Exception e) {
                    Toast.makeText(Add_manage_ingredient_Activity.this, "Error encountered.",
                            Toast.LENGTH_LONG);
                }

                dataAdapterr = new MyCustomAdapter(Add_manage_ingredient_Activity.this,
                        R.layout.ingredient_listview, countryList);
                // Assign adapter to ListView
                listView.setAdapter(dataAdapterr);
            }
            if (seli.toString().equals("3")){
                try {
                    Cursor allrows = db.rawQuery("Select * from Ingredients ORDER BY _id ASC", null);
                    System.out.println("COUNT : " + allrows.getCount());


                    //Country_items country = new Country_items(name, name, name, name);

                    if (allrows.moveToFirst()) {
                        do {
                            String ID = allrows.getString(0);
                            String NAme = allrows.getString(1);
                            String BArCOde = allrows.getString(15);
                            String MIn = allrows.getString(2);
                            String MaX = allrows.getString(3);
                            String CuRReNt = allrows.getString(4);
                            String PRiCe = allrows.getString(13);
                            String UNiT = allrows.getString(5);
                            Ingredient_activity_listitems NAME = new Ingredient_activity_listitems(NAme, BArCOde, UNiT, MIn, MaX, CuRReNt, PRiCe);
                            //Country_items PLACE = new Country_items(PlaCe, PlaCe, PlaCe, PlaCe);
                            countryList.add(NAME);
                            //countryList.add(PLACE);
                        } while (allrows.moveToNext());
                    }
                    allrows.close();
//                db.close();
                } catch (Exception e) {
                    Toast.makeText(Add_manage_ingredient_Activity.this, "Error encountered.",
                            Toast.LENGTH_LONG);
                }

                dataAdapterr = new MyCustomAdapter(Add_manage_ingredient_Activity.this,
                        R.layout.ingredient_listview, countryList);
                // Assign adapter to ListView
                listView.setAdapter(dataAdapterr);
            }

        }
    }


    void updateInDB(Dialog dialog) {
        position1_edit = unit_type_edit.getSelectedItem();
//        Toast.makeText(Add_manage_ingredient_Activity.this, "can update", Toast.LENGTH_SHORT).show();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ingredient_name", editText1_edit.getText().toString());
        contentValues.put("barcode", editText_barcode_edit.getText().toString());
        contentValues.put("min_req", editText2_edit.getText().toString());
        contentValues.put("max_req", editText4_edit.getText().toString());
        if (editText5_edit.getText().toString().equals("")){
            contentValues.put("current_stock", "0");
        }else {
            contentValues.put("current_stock", editText5_edit.getText().toString());
        }
        contentValues.put("unit", position1_edit.toString());


        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy MM dd");
        final String currentDateandTime1 = sdf2.format(new Date());

        SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM yyyy");
        final String currentDateandTime2 = sdf3.format(new Date());

        Date dt = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
        final String time1 = sdf1.format(dt);

        Date dt1 = new Date();
        SimpleDateFormat sdf11 = new SimpleDateFormat("kkmm");
        final String time11 = sdf11.format(dt1);

        Date dtt_new = new Date();
        SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");
        final String time24_new = sdf1t_new.format(dtt_new);

        contentValues.put("datetimee_new", time24_new);

        float a = (Float.parseFloat(editText4_edit.getText().toString()) - Float.parseFloat(editText5_edit.getText().toString()));
        contentValues.put("required", String.valueOf(a));

        String wherecu = "ingredient_name = '" + ingr_name + "'";

        if (editText5_edit.getText().toString().equals("")){
            webservicequery("UPDATE Ingredients SET ingredient_name = '" + editText1_edit.getText().toString() + "', barcode = '"+editText_barcode_edit.getText().toString()+"', min_req ='"+editText2_edit.getText().toString()+"', max_req ='"+editText4_edit.getText().toString()+"', unit = '"+position1_edit.toString()+"', datetimee_new = '"+time24_new+"', required = '"+String.valueOf(a)+"', current_stock = '0'  WHERE ingredient_name = '" + ingr_name + "'");
            db.execSQL("UPDATE Ingredients SET ingredient_name = '" + editText1_edit.getText().toString() + "', barcode = '"+editText_barcode_edit.getText().toString()+"', min_req ='"+editText2_edit.getText().toString()+"', max_req ='"+editText4_edit.getText().toString()+"', unit = '"+position1_edit.toString()+"', datetimee_new = '"+time24_new+"', required = '"+String.valueOf(a)+"', current_stock = '0'  WHERE ingredient_name = '" + ingr_name + "'");
        }else {
            webservicequery("UPDATE Ingredients SET ingredient_name = '" + editText1_edit.getText().toString() + "', barcode = '"+editText_barcode_edit.getText().toString()+"', min_req ='"+editText2_edit.getText().toString()+"', max_req ='"+editText4_edit.getText().toString()+"', unit = '"+position1_edit.toString()+"', datetimee_new = '"+time24_new+"', required = '"+String.valueOf(a)+"', current_stock = '"+editText5_edit.getText().toString()+"'  WHERE ingredient_name = '" + ingr_name + "'");
            db.execSQL("UPDATE Ingredients SET ingredient_name = '" + editText1_edit.getText().toString() + "', barcode = '"+editText_barcode_edit.getText().toString()+"', min_req ='"+editText2_edit.getText().toString()+"', max_req ='"+editText4_edit.getText().toString()+"', unit = '"+position1_edit.toString()+"', datetimee_new = '"+time24_new+"', required = '"+String.valueOf(a)+"', current_stock = '"+editText5_edit.getText().toString()+"'  WHERE ingredient_name = '" + ingr_name + "'");
        }


//        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients");
//        getContentResolver().update(contentUri, contentValues,wherecu,new String[]{});
//        resultUri = new Uri.Builder()
//                .scheme("content")
//                .authority(StubProviderApp.AUTHORITY)
//                .path("Ingredients")
//                .appendQueryParameter("operation", "update")
//                .appendQueryParameter("ingredient_name",ingr_name)
//                .build();
//        getContentResolver().notifyChange(resultUri, null);



     //   db.update("Ingredients", contentValues, wherecu, new String[]{});


        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("ingredient_name", editText1_edit.getText().toString());
        String wherecu1 = "ingredient_name = '" + ingr_name + "'";

        webservicequery("UPDATE Ingredient_items_list SET ingredient_name = '" + editText1_edit.getText().toString() + "' WHERE ingredient_name = '" + ingr_name + "'");
        db.execSQL("UPDATE Ingredient_items_list SET ingredient_name = '" + editText1_edit.getText().toString() + "' WHERE ingredient_name = '" + ingr_name + "'");

//        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_items_list");
//        getContentResolver().update(contentUri, contentValues1,wherecu1,new String[]{});
//        resultUri = new Uri.Builder()
//                .scheme("content")
//                .authority(StubProviderApp.AUTHORITY)
//                .path("Ingredient_items_list")
//                .appendQueryParameter("operation", "update")
//                .appendQueryParameter("ingredient_name",ingr_name)
//                .build();
//        getContentResolver().notifyChange(resultUri, null);



 //       db.update("Ingredient_items_list", contentValues1, wherecu1, new String[]{});

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("itemname", editText1_edit.getText().toString());
        String wherecu2 = "itemname = '" + ingr_name + "'";

        webservicequery("UPDATE Ingredient_sold_item_details SET itemname = '" + editText1_edit.getText().toString() + "' WHERE itemname = '" + ingr_name + "'");
        db.execSQL("UPDATE Ingredient_sold_item_details SET itemname = '" + editText1_edit.getText().toString() + "' WHERE itemname = '" + ingr_name + "'");

//        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_sold_item_details");
//        getContentResolver().update(contentUri, contentValues2,wherecu2,new String[]{});
//        resultUri = new Uri.Builder()
//                .scheme("content")
//                .authority(StubProviderApp.AUTHORITY)
//                .path("Ingredient_sold_item_details")
//                .appendQueryParameter("operation", "update")
//                .appendQueryParameter("itemname",ingr_name)
//                .build();
//        getContentResolver().notifyChange(resultUri, null);




    //    db.update("Ingredient_sold_item_details", contentValues2, wherecu2, new String[]{});

        linearLayout.setVisibility(View.GONE);
        addingredient.setVisibility(View.VISIBLE);

        countryList = new ArrayList<Ingredient_activity_listitems>();

        if (seli.toString().equals("1")) {
            try {
                Cursor allrows = db.rawQuery("Select * from Ingredients ORDER BY ingredient_name ASC", null);
                System.out.println("COUNT : " + allrows.getCount());


                //Country_items country = new Country_items(name, name, name, name);

                if (allrows.moveToFirst()) {
                    do {
                        String ID = allrows.getString(0);
                        String NAme = allrows.getString(1);
                        String BArCOde = allrows.getString(15);
                        String MIn = allrows.getString(2);
                        String MaX = allrows.getString(3);
                        String CuRReNt = allrows.getString(4);
                        String PRiCe = allrows.getString(13);
                        String UNiT = allrows.getString(5);
                        Ingredient_activity_listitems NAME = new Ingredient_activity_listitems(NAme, BArCOde, UNiT, MIn, MaX, CuRReNt, PRiCe);
                        //Country_items PLACE = new Country_items(PlaCe, PlaCe, PlaCe, PlaCe);
                        countryList.add(NAME);
                        //countryList.add(PLACE);
                    } while (allrows.moveToNext());
                }
                allrows.close();
//                db.close();
            } catch (Exception e) {
                Toast.makeText(Add_manage_ingredient_Activity.this, "Error encountered.",
                        Toast.LENGTH_LONG);
            }

            dataAdapterr = new MyCustomAdapter(Add_manage_ingredient_Activity.this,
                    R.layout.ingredient_listview, countryList);
            // Assign adapter to ListView
            listView.setAdapter(dataAdapterr);
        }
        if (seli.toString().equals("2")){
            try {
                Cursor allrows = db.rawQuery("Select * from Ingredients ORDER BY date ASC", null);
                System.out.println("COUNT : " + allrows.getCount());


                //Country_items country = new Country_items(name, name, name, name);

                if (allrows.moveToFirst()) {
                    do {
                        String ID = allrows.getString(0);
                        String NAme = allrows.getString(1);
                        String BArCOde = allrows.getString(15);
                        String MIn = allrows.getString(2);
                        String MaX = allrows.getString(3);
                        String CuRReNt = allrows.getString(4);
                        String PRiCe = allrows.getString(13);
                        String UNiT = allrows.getString(5);
                        Ingredient_activity_listitems NAME = new Ingredient_activity_listitems(NAme, BArCOde, UNiT, MIn, MaX, CuRReNt, PRiCe);
                        //Country_items PLACE = new Country_items(PlaCe, PlaCe, PlaCe, PlaCe);
                        countryList.add(NAME);
                        //countryList.add(PLACE);
                    } while (allrows.moveToNext());
                }
                allrows.close();
//                db.close();
            } catch (Exception e) {
                Toast.makeText(Add_manage_ingredient_Activity.this, "Error encountered.",
                        Toast.LENGTH_LONG);
            }

            dataAdapterr = new MyCustomAdapter(Add_manage_ingredient_Activity.this,
                    R.layout.ingredient_listview, countryList);
            // Assign adapter to ListView
            listView.setAdapter(dataAdapterr);
        }
        if (seli.toString().equals("3")){
            try {
                Cursor allrows = db.rawQuery("Select * from Ingredients ORDER BY _id ASC", null);
                System.out.println("COUNT : " + allrows.getCount());


                //Country_items country = new Country_items(name, name, name, name);

                if (allrows.moveToFirst()) {
                    do {
                        String ID = allrows.getString(0);
                        String NAme = allrows.getString(1);
                        String BArCOde = allrows.getString(15);
                        String MIn = allrows.getString(2);
                        String MaX = allrows.getString(3);
                        String CuRReNt = allrows.getString(4);
                        String PRiCe = allrows.getString(13);
                        String UNiT = allrows.getString(5);
                        Ingredient_activity_listitems NAME = new Ingredient_activity_listitems(NAme, BArCOde, UNiT, MIn, MaX, CuRReNt, PRiCe);
                        //Country_items PLACE = new Country_items(PlaCe, PlaCe, PlaCe, PlaCe);
                        countryList.add(NAME);
                        //countryList.add(PLACE);
                    } while (allrows.moveToNext());
                }
                allrows.close();
//                db.close();
            } catch (Exception e) {
                Toast.makeText(Add_manage_ingredient_Activity.this, "Error encountered.",
                        Toast.LENGTH_LONG);
            }

            dataAdapterr = new MyCustomAdapter(Add_manage_ingredient_Activity.this,
                    R.layout.ingredient_listview, countryList);
            // Assign adapter to ListView
            listView.setAdapter(dataAdapterr);
        }


//        Cursor cursor = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp", null);
//        if (cursor.moveToFirst()){
//            do {
//                String item_name = cursor.getString(1);
//                String qty_temp = cursor.getString(2);
//                String qty_unit = cursor.getString(3);
//
//                Cursor cursor2 = db.rawQuery("SELECT * FROM Ingredient_items_list WHERE itemname = '"+item_name+"' AND ingredient_name = '"+editText1_edit.getText().toString()+"'", null);
//                if (cursor2.moveToFirst()){
//                    String id = cursor2.getString(0);
//                    ContentValues contentValues1 = new ContentValues();
//                    contentValues1.put("ingredient_name", editText1_edit.getText().toString());
//                    contentValues1.put("itemname", item_name);
//                    contentValues1.put("item_qyt_used", qty_temp);
//                    contentValues1.put("currnet_stock", editText5_edit.getText().toString());
//                    contentValues1.put("modified_datetimee_new", time24_new);
//                    contentValues1.put("qty_unit", qty_unit);
//                    float a1 = (Float.parseFloat(editText4_edit.getText().toString()) - Float.parseFloat(editText5_edit.getText().toString()));
//                    contentValues1.put("required", String.valueOf(a1));
//                    String where1 = "_id = '"+id+"'";
//                    db.update("Ingredient_items_list", contentValues1, where1, new String[]{});
////                    Toast.makeText(Add_manage_ingredient_Activity.this, "update "+editText1_edit.getText().toString()+" "+item_name, Toast.LENGTH_LONG).show();
//                }else {
//                    ContentValues contentValues1 = new ContentValues();
//                    contentValues1.put("ingredient_name", editText1_edit.getText().toString());
//                    contentValues1.put("itemname", item_name);
//                    contentValues1.put("item_qyt_used", qty_temp);
//                    contentValues1.put("currnet_stock", editText5_edit.getText().toString());
//                    contentValues1.put("date1", currentDateandTime2);
//                    contentValues1.put("date", time24_new);
//                    contentValues1.put("time1", time1);
//                    contentValues1.put("time", time11);
//                    contentValues1.put("modified_datetimee_new", time24_new);
//                    contentValues1.put("qty_unit", qty_unit);
//                    float a1 = (Float.parseFloat(editText4_edit.getText().toString()) - Float.parseFloat(editText5_edit.getText().toString()));
//                    contentValues1.put("required", String.valueOf(a1));
//                    db.insert("Ingredient_items_list", null, contentValues1);
////                    Toast.makeText(Add_manage_ingredient_Activity.this, "insert "+editText1_edit.getText().toString()+" "+item_name, Toast.LENGTH_LONG).show();
//                }
//
//            }while (cursor.moveToNext());
//        }
//
//        Cursor cursor21 = db.rawQuery("SELECT * FROM Ingredient_items_list WHERE ingredient_name = '"+editText1_edit.getText().toString()+"' OR ingredient_name = '"+ingr_name+"'", null);
//        if (cursor21.moveToFirst()){
//            do {
//                String id = cursor21.getString(0);
//                String ingr_na = cursor21.getString(2);
//
//                Cursor cursor211 = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp WHERE itemname = '"+ingr_na+"'", null);
//                if (cursor211.moveToFirst()){
//
//                }else {
//                    String where1 = "_id = '"+id+"'";
//                    db.delete("Ingredient_items_list", where1, new String[]{});
//                }
//            }while (cursor21.moveToNext());
//        }
//
//        db.delete("Ingredients_item_selection_temp", null, null);
//
//        Cursor cursor2 = db.rawQuery("SELECT * FROM Ingredient_items_list WHERE ingredient_name = '"+ingr_name+"'", null);
//        if (cursor2.moveToFirst()){
//            do {
//                String id = cursor2.getString(0);
//                ContentValues contentValues11 = new ContentValues();
//                contentValues11.put("ingredient_name", editText1_edit.getText().toString());
//                String where1 = "_id = '"+id+"'";
//                db.update("Ingredient_items_list", contentValues11, where1, new String[]{});
//            }while (cursor2.moveToNext());
//        }

        dialog.dismiss();

    }


    private class MyCustomAdapter extends ArrayAdapter<Ingredient_activity_listitems> {

        private ArrayList<Ingredient_activity_listitems> originalList;
        private ArrayList<Ingredient_activity_listitems> countryList;
        private CountryFilter filter;

        private Cursor c;
        private Context context;

        private SparseBooleanArray mSelectedItemsIds;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Ingredient_activity_listitems> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<Ingredient_activity_listitems>();
            this.countryList.addAll(countryList);
            this.originalList = new ArrayList<Ingredient_activity_listitems>();
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
            TextView barcode;
            TextView min;
            TextView max;
            TextView current;
            TextView price;
            TextView unit;
            TextView inn;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));
            if (convertView == null) {

                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.ingredient_listview, null);

                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.ingredient_name);
                holder.barcode = (TextView) convertView.findViewById(R.id.ingredient_barcode);
                holder.min = (TextView) convertView.findViewById(R.id.min);
                holder.max = (TextView) convertView.findViewById(R.id.max);
                holder.current = (TextView) convertView.findViewById(R.id.current);
                holder.price = (TextView) convertView.findViewById(R.id.avg_unit_price);
                holder.unit = (TextView) convertView.findViewById(R.id.unit);
                holder.inn = (TextView) convertView.findViewById(R.id.inn);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Ingredient_activity_listitems country = countryList.get(position);
            holder.name.setText(country.getName());
            holder.barcode.setText(country.getBarcode());
            holder.min.setText(country.getmin());
            holder.max.setText(country.getmax());
            holder.current.setText(country.getcurrent());
            holder.price.setText(country.getprice());
            holder.unit.setText(country.getunit());

            if (holder.price.getText().toString().equals("")){
                holder.price.setText("-");
            }
            if (holder.current.getText().toString().equals("")){
                holder.current.setText("-");
            }

            if (str_country.toString().equals("Rupee") || str_country.toString().equals("India")) {
                insert1_cc = "\u20B9";
                insert1_rs = "Rs.";
                holder.inn.setText(insert1_cc);
            }else {
                if (str_country.toString().equals("Pound") || str_country.toString().equals("UK Pound")) {
                    insert1_cc = "\u00a3";
                    insert1_rs = "BP.";
                    holder.inn.setText(insert1_cc);
                }else {
                    if (str_country.toString().equals("Euro") || str_country.toString().equals("UK Euro")) {
                        insert1_cc = "\u20ac";
                        insert1_rs = "EU.";
                        holder.inn.setText(insert1_cc);
                    }else {
                        if (str_country.toString().equals("Dollar")) {
                            insert1_cc = "\u0024";
                            insert1_rs = "\u0024";
                            holder.inn.setText(insert1_cc);
                        }else {
                            if (str_country.toString().equals("Dinar")) {
                                insert1_cc = "D";
                                insert1_rs = "KD.";
                                holder.inn.setText(insert1_cc);
                            }else {
                                if (str_country.toString().equals("Shilling")) {
                                    insert1_cc = "S";
                                    insert1_rs = "S.";
                                    holder.inn.setText(insert1_cc);
                                }else {
                                    if (str_country.toString().equals("Ringitt")) {
                                        insert1_cc = "R";
                                        insert1_rs = "RM.";
                                        holder.inn.setText(insert1_cc);
                                    }else {
                                        if (str_country.toString().equals("Rial")) {
                                            insert1_cc = "R";
                                            insert1_rs = "OR.";
                                            holder.inn.setText(insert1_cc);
                                        }else {
                                            if (str_country.toString().equals("Yen")) {
                                                insert1_cc = "\u00a5";
                                                insert1_rs = "\u00a5";
                                                holder.inn.setText(insert1_cc);
                                            }else {
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

//            holder.name.setText(country);
//            holder.continent.setText(country.getContinent());
//            holder.region.setText(country.getRegion());

            return convertView;

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
                    ArrayList<Ingredient_activity_listitems> filteredItems = new ArrayList<Ingredient_activity_listitems>();

                    for(int i = 0, l = originalList.size(); i < l; i++)
                    {
                        Ingredient_activity_listitems country = originalList.get(i);
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

                countryList = (ArrayList<Ingredient_activity_listitems>)results.values;
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(Add_manage_ingredient_Activity.this, "1", Toast.LENGTH_LONG).show();
        if (requestCode == 1) {
//            Toast.makeText(Add_manage_ingredient_Activity.this, "2", Toast.LENGTH_LONG).show();
            if(resultCode == RESULT_OK) {
//                Toast.makeText(Add_manage_ingredient_Activity.this, "3", Toast.LENGTH_LONG).show();
                String strEditText = data.getStringExtra("editTextValue");
//                Toast.makeText(Add_manage_ingredient_Activity.this, "2q "+strEditText, Toast.LENGTH_LONG).show();
                item_selection.setText(strEditText);
                item_used_value.setText(strEditText);
            }
        }
    }

    public void webservicequery(final String webserviceQuery){


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(Add_manage_ingredient_Activity.this);
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
        RequestQueue queue;
        StringRequest sr1;
        // queue = Volley.newRequestQueue(getActivity());

        queue= RequestSingleton.getInstance(this).getInstance();

        sr1 = new StringRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl + "webservicequery.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        System.out.println("webservice "+responseString);
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
