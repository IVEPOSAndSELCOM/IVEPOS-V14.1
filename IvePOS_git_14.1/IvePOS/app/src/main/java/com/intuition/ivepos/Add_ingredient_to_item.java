package com.intuition.ivepos;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.intuition.ivepos.syncapp.StubProviderApp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

/**
 * Created by Rohithkumar on 9/12/2017.
 */

public class Add_ingredient_to_item extends AppCompatActivity {

    public SQLiteDatabase db = null;

    ArrayList<Country_Ingredient> list = new ArrayList<Country_Ingredient>();
    ArrayAdapter<Country_Ingredient> adapter;
    String insert1_cc = "", insert1_rs = "", str_country;
    Uri contentUri,resultUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ingredeint_to_item);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

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

        Bundle extras = getIntent().getExtras();
        final String player1name = extras.getString("PLAYER1NAME");
        String player2name = extras.getString("PLAYER2NAME");

        final TextView item_name = (TextView) findViewById(R.id.item_name);
        item_name.setText(player1name);

        final TextView usage_unit = (TextView) findViewById(R.id.usage_unit);

        float b = 0;
        Cursor ing_r1 = db.rawQuery("SELECT * FROM Ingredient_items_list WHERE itemname = '" + player1name + "'", null);
        if (ing_r1.moveToFirst()) {
            do {
                String ingid_r = ing_r1.getString(0);
                String ingname_r = ing_r1.getString(1);
                String quan_used_r = ing_r1.getString(3);

                Cursor ing_r111 = db.rawQuery("SELECT * FROM Ingredients WHERE ingredient_name = '"+ingname_r+"'", null);
                if (ing_r111.moveToFirst()) {
                    do {
                        String ind_pri = ing_r111.getString(13);

                        float a = Float.parseFloat(quan_used_r) * Float.parseFloat(ind_pri);
                        b = b+a;

                    } while (ing_r111.moveToNext());
                }

            } while (ing_r1.moveToNext());
        }
        usage_unit.setText("("+insert1_rs+" "+String.format("%.2f", b)+")");


        final TextView current_stock_qty = (TextView) findViewById(R.id.current_stock_qty);
        ArrayList<Float> d = new ArrayList<>();

        float b1 = 0;
        Cursor ing_r2 = db.rawQuery("SELECT * FROM Ingredient_items_list WHERE itemname = '" + player1name + "'", null);
        if (ing_r2.moveToFirst()) {
            do {
                String ingid_r = ing_r2.getString(0);
                String ingname_r = ing_r2.getString(1);
                String quan_used_r = ing_r2.getString(3);
                String curr_sto = ing_r2.getString(4);

                float a1 = Float.parseFloat(curr_sto)/Float.parseFloat(quan_used_r);
//                b1 = a1;
                d.add(a1);

            } while (ing_r2.moveToNext());

            float min = Collections.min(d);
            Math.floor(min);
            current_stock_qty.setText(String.valueOf(Math.floor(min)));
        }

//        List<Integer> list = Arrays.asList(d);

//        System.out.println(min);
//        Toast.makeText(Add_ingredient_to_item.this, ""+min, Toast.LENGTH_LONG).show();

        ImageButton btncancel = (ImageButton) findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        final TextView items_count = (TextView) findViewById(R.id.items_count);
        items_count.setText(player2name);

        final TextView unit = (TextView) findViewById(R.id.unit);


        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients_item_selection_temp");
        getContentResolver().delete(contentUri, null,null);
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("Ingredients_item_selection_temp")
                .appendQueryParameter("operation", "delete")
                .appendQueryParameter("1", "1")
                .build();
        getContentResolver().notifyChange(resultUri, null);




     //   db.delete("Ingredients_item_selection_temp", null, null);

        Cursor cursor = db.rawQuery("SELECT * FROM Ingredient_items_list WHERE itemname = '"+player1name+"'", null);
        if (cursor.moveToFirst()){
            do {
                String ingr_na = cursor.getString(1);
                String qt = cursor.getString(3);
                String uni = cursor.getString(10);

                unit.setText(uni);

                ContentValues contentValues = new ContentValues();
                contentValues.put("itemname", ingr_na);
                contentValues.put("qty_temp", qt);
                contentValues.put("qty_temp_unit", uni);
                contentValues.put("qty", "yes");


                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients_item_selection_temp");
                resultUri = getContentResolver().insert(contentUri, contentValues);
                getContentResolver().notifyChange(resultUri, null);


            //    db.insert("Ingredients_item_selection_temp", null, contentValues);

            }while (cursor.moveToNext());
        }




        final ListView listView = (ListView) findViewById(R.id.list);
        Cursor cursor_i = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp", null);
        String[] fromFieldNames = {"itemname", "qty_temp", "qty_temp_unit"};
        // the XML defined views which the data will be bound to
        int[] toViewsID = {R.id.current_stock, R.id.current_stock_value, R.id.unit1};

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(Add_ingredient_to_item.this, R.layout.dialog_ingredients_listitems_with_qty, cursor_i, fromFieldNames, toViewsID, 0);
        listView.setAdapter(simpleCursorAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView qt = (TextView) view.findViewById(R.id.current_stock_value);
                Cursor cu = (Cursor) adapterView.getItemAtPosition(i);
                final String n = cu.getString(cu.getColumnIndex("itemname"));
                final String unn = cu.getString(cu.getColumnIndex("qty_temp_unit"));
                final Dialog dialog_ingredient_manager_usage_item = new Dialog(Add_ingredient_to_item.this, R.style.timepicker_date_dialog);
                dialog_ingredient_manager_usage_item.setContentView(R.layout.dialog_ingredient_manager_usage_item);
                dialog_ingredient_manager_usage_item.show();

                ImageButton cancel = (ImageButton) dialog_ingredient_manager_usage_item.findViewById(R.id.btncancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_ingredient_manager_usage_item.dismiss();
                    }
                });


                TextView ingredient_name = (TextView) dialog_ingredient_manager_usage_item.findViewById(R.id.ingredient_name);
                ingredient_name.setText(player1name);

                TextView itemname = (TextView) dialog_ingredient_manager_usage_item.findViewById(R.id.itemname);
                itemname.setText(n);

//                position1 = unit.getText().toString();

                TextView unit2 = (TextView) dialog_ingredient_manager_usage_item.findViewById(R.id.unit2);
                unit2.setText(unn);

                final TextInputLayout qtii = (TextInputLayout) dialog_ingredient_manager_usage_item.findViewById(R.id.qtii);
                final EditText qt1 = (EditText) dialog_ingredient_manager_usage_item.findViewById(R.id.adjust_current_stock_value);
                qt1.setText(qt.getText().toString());

                final ImageButton save = (ImageButton) dialog_ingredient_manager_usage_item.findViewById(R.id.btnsave);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (qt1.getText().toString().equals("")){
                            qtii.setError("Enter value");
                        }else {
                            qt.setText(qt1.getText().toString());
                            dialog_ingredient_manager_usage_item.dismiss();
                            ContentValues c = new ContentValues();
                            c.put("qty_temp", qt.getText().toString());
                            String wherecu = "itemname = '" + n + "'";


                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients_item_selection_temp");
                            getContentResolver().update(contentUri, c,wherecu,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Ingredients_item_selection_temp")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("itemname", n)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);




                       //     db.update("Ingredients_item_selection_temp", c, wherecu, new String[]{});

                            Cursor cursor_i = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp", null);
                            String[] fromFieldNames = {"itemname", "qty_temp", "qty_temp_unit"};
                            // the XML defined views which the data will be bound to
                            int[] toViewsID = {R.id.current_stock, R.id.current_stock_value, R.id.unit1};

                            android.widget.SimpleCursorAdapter simpleCursorAdapter = new android.widget.SimpleCursorAdapter(Add_ingredient_to_item.this, R.layout.dialog_ingredients_listitems_with_qty, cursor_i, fromFieldNames, toViewsID, 0);
                            listView.setAdapter(simpleCursorAdapter);


                            float b = 0;
                            Cursor ing_r1 = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp", null);
                            if (ing_r1.moveToFirst()) {
                                do {
                                    String ingid_r = ing_r1.getString(0);
                                    String ingname_r = ing_r1.getString(1);
                                    String quan_used_r = ing_r1.getString(2);

                                    Cursor ing_r111 = db.rawQuery("SELECT * FROM Ingredients WHERE ingredient_name = '"+ingname_r+"'", null);
                                    if (ing_r111.moveToFirst()) {
                                        do {
                                            String ind_pri = ing_r111.getString(13);

                                            float a = Float.parseFloat(quan_used_r) * Float.parseFloat(ind_pri);
                                            b = b+a;

                                        } while (ing_r111.moveToNext());
                                    }

                                } while (ing_r1.moveToNext());
                            }

                            usage_unit.setText("("+insert1_rs+" "+String.format("%.2f", b)+")");


                            ArrayList<Float> d = new ArrayList<>();

                            float b1 = 0;
                            Cursor ing_r11 = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp", null);
                            if (ing_r11.moveToFirst()) {
                                do {
                                    String ingname_r = ing_r11.getString(1);
                                    String quan_used_r = ing_r11.getString(2);

                                    Cursor ing_r2 = db.rawQuery("SELECT * FROM Ingredient_items_list WHERE ingredient_name = '" + ingname_r + "'", null);
                                    if (ing_r2.moveToFirst()) {
                                        do {
                                            String curr_sto1 = ing_r2.getString(4);

                                            float a1 = Float.parseFloat(curr_sto1)/Float.parseFloat(quan_used_r);
                                            d.add(a1);

                                        } while (ing_r2.moveToNext());

                                        float min = Collections.min(d);
                                        Math.floor(min);
                                        current_stock_qty.setText(String.valueOf(Math.floor(min)));
                                    }
                                } while (ing_r11.moveToNext());
                            }
                        }
                    }
                });

                qt1.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_GO) {
                            save.performClick();
                            return true;
                        }
                        return false;
                    }
                });
            }
        });


        Button add_delete_item_selection = (Button) findViewById(R.id.add_delete_item_selection);
        add_delete_item_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Cursor del = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp WHERE qty = '' OR qty is null", null);
//                if (del.moveToFirst()){
//                    do {
//                        String id = del.getString(0);
//                        String wherecu = "_id = '" + id + "'";
//                        db.delete("Ingredients_item_selection_temp", wherecu, new String[]{});
//                    }while (del.moveToNext());
//                }

                final Dialog dialog_ingredient_manager_add_del = new Dialog(Add_ingredient_to_item.this, R.style.timepicker_date_dialog);
                dialog_ingredient_manager_add_del.setContentView(R.layout.dialog_ingredient_manager_usage_add_del_ingredient);
                dialog_ingredient_manager_add_del.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog_ingredient_manager_add_del.show();
                dialog_ingredient_manager_add_del.setCanceledOnTouchOutside(false);

                ImageButton btncancel = (ImageButton) dialog_ingredient_manager_add_del.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_ingredient_manager_add_del.dismiss();

                        Cursor delq = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp WHERE qty = 'yes'", null);
                        int co = delq.getCount();


                        items_count.setText(String.valueOf(co));
                    }
                });


                ImageButton btn_redirect = (ImageButton) dialog_ingredient_manager_add_del.findViewById(R.id.btn_redirect);
                btn_redirect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Add_ingredient_to_item.this, MultiFragDatabaseActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

                TextView ingredient_name1 = (TextView) dialog_ingredient_manager_add_del.findViewById(R.id.ingredient_name);
                ingredient_name1.setText(item_name.getText().toString());

                final TextView usage_count = (TextView) dialog_ingredient_manager_add_del.findViewById(R.id.usage_count);
                Cursor sel1 = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp", null);
                int co = sel1.getCount();
                usage_count.setText(String.valueOf(co));
                final ListView myListView = (ListView) dialog_ingredient_manager_add_del.findViewById(R.id.list);

                list = new ArrayList<Country_Ingredient>();

                String statement = "SELECT * FROM Ingredients";
                //Execute the query
                Cursor aallrows = db.rawQuery(statement, null);
                System.out.println("COUNT : " + aallrows.getCount());
                ////Toast.makeText(Add_ingredient_to_item.this, "limit is a " + limit, Toast.LENGTH_SHORT).show();
                if (aallrows.moveToFirst()) {
                    do {
                        String ID = aallrows.getString(0);
                        String NAme = aallrows.getString(1);

                        Country_Ingredient NAME = new Country_Ingredient(NAme);
                        list.add(NAME);

                    } while (aallrows.moveToNext());
                }

                adapter = new MyAdapter_Ingredient(Add_ingredient_to_item.this,list);
                myListView.setAdapter(adapter);

                myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                        final CheckBox checkbox = (CheckBox) view.getTag(R.id.check);
                        final TextView checkbox1 = (TextView) view.getTag(R.id.label);

                        if (checkbox.isChecked()){
                            checkbox.setChecked(false);
//                            Toast.makeText(Add_ingredient_to_item.this, "no", Toast.LENGTH_SHORT).show();
                            int one = Integer.parseInt(usage_count.getText().toString());
                            int two = one-1;
                            usage_count.setText(String.valueOf(two));
                            Cursor del = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp WHERE itemname = '"+checkbox1.getText().toString()+"'", null);
                            if (del.moveToFirst()){
                                do {
                                    String na = del.getString(1);
                                    String id = del.getString(0);
                                    String wherecu = "_id = '" + id + "'";


                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients_item_selection_temp");
                                    getContentResolver().delete(contentUri, wherecu,new String[]{});
                                    resultUri = new Uri.Builder()
                                            .scheme("content")
                                            .authority(StubProviderApp.AUTHORITY)
                                            .path("Ingredients_item_selection_temp")
                                            .appendQueryParameter("operation", "delete")
                                            .appendQueryParameter("_id",id)
                                            .build();
                                    getContentResolver().notifyChange(resultUri, null);





                              //      db.delete("Ingredients_item_selection_temp", wherecu, new String[]{});
//                                                    db.del
                                }while (del.moveToNext());
                            }
                        }else {
                            String un = "";
                            Cursor del = db.rawQuery("SELECT * FROM Ingredients WHERE ingredient_name = '"+checkbox1.getText().toString()+"'", null);
                            if (del.moveToFirst()){
                                un = del.getString(5);
                            }
                            checkbox.setChecked(true);
//                            Toast.makeText(Add_ingredient_to_item.this, "yes", Toast.LENGTH_SHORT).show();
                            int one = Integer.parseInt(usage_count.getText().toString());
                            int two = one+1;
                            usage_count.setText(String.valueOf(two));
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("itemname", checkbox1.getText().toString());
                            contentValues.put("qty_temp", "0");
                            contentValues.put("qty_temp_unit", un);



                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients_item_selection_temp");
                            resultUri = getContentResolver().insert(contentUri, contentValues);
                            getContentResolver().notifyChange(resultUri, null);


                         //   db.insert("Ingredients_item_selection_temp", null, contentValues);
                        }
                    }
                });

                adapter.notifyDataSetChanged();

                Cursor sel = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp WHERE qty = 'yes'", null);
                if (sel.moveToFirst()){
                    View v;
                    do {
                        String na = sel.getString(1);
                        for(int i=0; i < myListView.getCount(); i++){
                            v = myListView.getAdapter().getView(i, null, null);
//                                              LinearLayout itemLayout = (LinearLayout)listView.getChildAt(i);
                            TextView cb = (TextView) v.findViewById(R.id.label);
                            CheckBox cb1 = (CheckBox)v.findViewById(R.id.check);
                            if (cb.getText().toString().equals(na)) {
                                cb1.setChecked(true);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }while (sel.moveToNext());
                }



                final EditText myFilter = (EditText) dialog_ingredient_manager_add_del.findViewById(R.id.custombar_text);
                myFilter.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s.toString());
                    }
                });

                ImageButton save = (ImageButton) dialog_ingredient_manager_add_del.findViewById(R.id.btnsave);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myFilter.setText("");
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(myFilter.getWindowToken(), 0);
                        dialog_ingredient_manager_add_del.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        myFilter.addTextChangedListener(new TextWatcher() {

                            public void afterTextChanged(Editable s) {
                            }

                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                adapter.getFilter().filter(s.toString());
                            }
                        });

                        dialog_ingredient_manager_add_del.dismiss();
                        Cursor del = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp", null);
                        if (del.moveToFirst()) {
                            do {
                                String id = del.getString(0);
                                String wherecu = "_id = '" + id + "'";
                                ContentValues c = new ContentValues();
                                c.put("qty", "yes");


                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients_item_selection_temp");
                                getContentResolver().update(contentUri, c,wherecu,new String[]{});
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProviderApp.AUTHORITY)
                                        .path("Ingredients_item_selection_temp")
                                        .appendQueryParameter("operation", "update")
                                        .appendQueryParameter("_id", id)
                                        .build();
                                getContentResolver().notifyChange(resultUri, null);



                            //    db.update("Ingredients_item_selection_temp", c, wherecu, new String[]{});
                            } while (del.moveToNext());
                        }

                        Cursor delq = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp WHERE qty = 'yes'", null);
                        int co = delq.getCount();

                        items_count.setText(String.valueOf(co));

                        Cursor cursor_i = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp", null);
                        String[] fromFieldNames = {"itemname", "qty_temp", "qty_temp_unit"};
                        // the XML defined views which the data will be bound to
                        int[] toViewsID = {R.id.current_stock, R.id.current_stock_value, R.id.unit1};

                        android.widget.SimpleCursorAdapter simpleCursorAdapter = new android.widget.SimpleCursorAdapter(Add_ingredient_to_item.this, R.layout.dialog_ingredients_listitems_with_qty, cursor_i, fromFieldNames, toViewsID, 0);
                        listView.setAdapter(simpleCursorAdapter);

                    }
                });
            }
        });

        ImageButton btnsave = (ImageButton) findViewById(R.id.btnsave);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("editTextValue", items_count.getText().toString());
                intent.putExtra("image", "10");
//                Toast.makeText(Add_ingredient_to_item.this, "1q "+items_count.getText().toString(), Toast.LENGTH_LONG).show();
                setResult(RESULT_OK, intent);
                finish();

                String dialog_columnvalue  = item_name.getText().toString();
                String NAme1  = item_name.getText().toString();

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

                Cursor cursor = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp", null);
                if (cursor.moveToFirst()){
                    do {
                        String ingr_na = cursor.getString(1);
                        String qty = cursor.getString(2);
                        String unit = cursor.getString(3);

                        Cursor cursor2 = db.rawQuery("SELECT * FROM Ingredient_items_list WHERE ingredient_name = '"+ingr_na+"' AND itemname = '"+NAme1+"'", null);
                        if (cursor2.moveToFirst()){
                            String id = cursor2.getString(0);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("ingredient_name", ingr_na);
                            contentValues.put("itemname", dialog_columnvalue);
                            contentValues.put("item_qyt_used", qty);
//                        contentValues.put("currnet_stock", "");
                            contentValues.put("modified_datetimee_new", time24_new);
                            contentValues.put("qty_unit", unit);
                            String where1 = "_id = '"+id+"'";



                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_items_list");
                            getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Ingredient_items_list")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id", id)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);



                        //    db.update("Ingredient_items_list", contentValues, where1, new String[]{});
                        }else {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("ingredient_name", ingr_na);
                            contentValues.put("itemname", dialog_columnvalue);
                            contentValues.put("item_qyt_used", qty);
//                        contentValues.put("currnet_stock", "");
                            contentValues.put("date1", currentDateandTime2);
                            contentValues.put("date", currentDateandTime1);
                            contentValues.put("time1", time1);
                            contentValues.put("time", time11);
                            contentValues.put("modified_datetimee_new", time24_new);
                            contentValues.put("qty_unit", unit);

                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_items_list");
                            resultUri = getContentResolver().insert(contentUri, contentValues);
                            getContentResolver().notifyChange(resultUri, null);


                       //     db.insert("Ingredient_items_list", null, contentValues);
                        }
                    }while (cursor.moveToNext());
                }

                Cursor cv = db.rawQuery("SELECT * FROM Ingredient_items_list WHERE itemname = '"+NAme1+"' OR itemname = '"+dialog_columnvalue+"'", null);
                if (cv.moveToFirst()){
                    do {
                        String id = cv.getString(0);
                        String ingr_na = cv.getString(1);
                        Cursor cursor2 = db.rawQuery("SELECT * FROM Ingredients WHERE ingredient_name = '"+ingr_na+"'", null);
                        if (cursor2.moveToFirst()){
                            String cur_qty = cursor2.getString(4);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("currnet_stock", cur_qty);
                            String where1 = "_id = '"+id+"'";




                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_items_list");
                            getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Ingredient_items_list")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id", id)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);



                      //      db.update("Ingredient_items_list", contentValues, where1, new String[]{});
                        }
                    }while (cv.moveToNext());
                }

                Cursor cursor21 = db.rawQuery("SELECT * FROM Ingredient_items_list WHERE itemname = '"+NAme1+"' OR itemname = '"+dialog_columnvalue+"'", null);
                if (cursor21.moveToFirst()){
                    do {
                        String id = cursor21.getString(0);
                        String ingr_na = cursor21.getString(1);

                        Cursor cursor2 = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp WHERE itemname = '"+ingr_na+"'", null);
                        if (cursor2.moveToFirst()){

                        }else {
                            String where1 = "_id = '"+id+"'";


                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_items_list");
                            getContentResolver().delete(contentUri, where1,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Ingredient_items_list")
                                    .appendQueryParameter("operation", "delete")
                                    .appendQueryParameter("_id", id)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);



                       //     db.delete("Ingredient_items_list", where1, new String[]{});
                        }
                    }while (cursor21.moveToNext());
                }



                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients_item_selection_temp");
                getContentResolver().delete(contentUri, null,null);
                resultUri = new Uri.Builder()
                        .scheme("content")
                        .authority(StubProviderApp.AUTHORITY)
                        .path("Ingredients_item_selection_temp")
                        .appendQueryParameter("operation", "delete")
                        .appendQueryParameter("1", "1")
                        .build();
                getContentResolver().notifyChange(resultUri, null);



             //   db.delete("Ingredients_item_selection_temp", null, null);

                Cursor cursor2 = db.rawQuery("SELECT * FROM Ingredient_items_list WHERE itemname = '"+NAme1+"'", null);
                if (cursor2.moveToFirst()){
                    do {
                        String id = cursor2.getString(0);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("itemname", dialog_columnvalue);
                        String where1 = "_id = '"+id+"'";


                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_items_list");
                        getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("Ingredient_items_list")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id", id)
                                .build();
                        getContentResolver().notifyChange(resultUri, null);





                   //     db.update("Ingredient_items_list", contentValues, where1, new String[]{});
                    }while (cursor2.moveToNext());
                }

            }
        });
    }
}
