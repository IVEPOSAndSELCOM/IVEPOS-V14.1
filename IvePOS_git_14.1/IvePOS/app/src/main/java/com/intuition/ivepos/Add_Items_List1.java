package com.intuition.ivepos;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.appcompat.app.AppCompatActivity;
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

import com.intuition.ivepos.syncapp.StubProviderApp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rohithkumar on 9/11/2017.
 */

public class Add_Items_List1 extends AppCompatActivity {

    public SQLiteDatabase db = null;
    Uri contentUri,resultUri;

    TextView items_count, current_stock_qty;

    ArrayList<Country_Ingredient> list = new ArrayList<Country_Ingredient>();
    ArrayAdapter<Country_Ingredient> adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ingredient_manager_usage);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        Bundle extras = getIntent().getExtras();
        final String player1name = extras.getString("PLAYER1NAME");
        final String player2name = extras.getString("PLAYER2NAME");
        final String player3name = extras.getString("PLAYER3NAME");
        final String position1 = extras.getString("position");

        final TextView ingredient_name = (TextView) findViewById(R.id.ingredient_name);
        ingredient_name.setText(player1name.toString());

        final TextView ingredient_name1 = (TextView) findViewById(R.id.ingredient_name1);
        ingredient_name1.setText(player1name.toString());

//        final TextView items_count = (TextView) findViewById(R.id.items_count);

        items_count = (TextView) findViewById(R.id.items_count);

        Cursor delq = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp WHERE qty = 'yes'", null);
        int co = delq.getCount();

        items_count.setText(String.valueOf(co));

        current_stock_qty = (TextView) findViewById(R.id.current_stock_qty);
        TextView tv = new TextView(Add_Items_List1.this);
        tv.setText(player2name);
        if (tv.getText().toString().equals("")){
            current_stock_qty.setText("0");
        }else {
            current_stock_qty.setText(player2name.toString());
        }

        final TextView unit = (TextView) findViewById(R.id.unit);
        unit.setText(position1.toString());


        ImageButton btncancel = (ImageButton) findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




        final ListView listView = (ListView) findViewById(R.id.list);
        Cursor cursor_i = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp", null);
        String[] fromFieldNames = {"itemname", "qty_temp", "qty_temp_unit"};
        // the XML defined views which the data will be bound to
        int[] toViewsID = {R.id.current_stock, R.id.current_stock_value, R.id.unit1};

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(Add_Items_List1.this, R.layout.dialog_ingredients_listitems_with_qty, cursor_i, fromFieldNames, toViewsID, 0);
        listView.setAdapter(simpleCursorAdapter);

        Button add_delete_item_selection = (Button) findViewById(R.id.add_delete_item_selection);
        add_delete_item_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor del = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp WHERE qty = '' OR qty is null", null);
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




                    //    db.delete("Ingredients_item_selection_temp", wherecu, new String[]{});
                    }while (del.moveToNext());
                }

                final Dialog dialog_ingredient_manager_add_del = new Dialog(Add_Items_List1.this, R.style.timepicker_date_dialog);
                dialog_ingredient_manager_add_del.setContentView(R.layout.dialog_ingredient_manager_usage_add_del_items);
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
                        Intent intent = new Intent(Add_Items_List1.this, MultiFragDatabaseActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

                TextView ingredient_name1 = (TextView) dialog_ingredient_manager_add_del.findViewById(R.id.ingredient_name);
                ingredient_name1.setText(ingredient_name.getText().toString());

                final TextView usage_count = (TextView) dialog_ingredient_manager_add_del.findViewById(R.id.usage_count);
                Cursor sel1 = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp", null);
                int co = sel1.getCount();
                usage_count.setText(String.valueOf(co));
                final ListView myListView = (ListView) dialog_ingredient_manager_add_del.findViewById(R.id.list);

                list = new ArrayList<Country_Ingredient>();

                String statement = "SELECT * FROM Items";
                //Execute the query
                Cursor aallrows = db.rawQuery(statement, null);
                System.out.println("COUNT : " + aallrows.getCount());
                ////Toast.makeText(Add_Items_List1.this, "limit is a " + limit, Toast.LENGTH_SHORT).show();
                if (aallrows.moveToFirst()) {
                    do {
                        String ID = aallrows.getString(0);
                        String NAme = aallrows.getString(1);
                        String BAr = aallrows.getString(16);

                        Country_Ingredient NAME = new Country_Ingredient(NAme);
                        list.add(NAME);

                    } while (aallrows.moveToNext());
                }

                adapter = new MyAdapter_Ingredient(Add_Items_List1.this,list);
                myListView.setAdapter(adapter);

                myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                        final CheckBox checkbox = (CheckBox) view.getTag(R.id.check);
                        final TextView checkbox1 = (TextView) view.getTag(R.id.label);

                        if (checkbox.isChecked()){
                            checkbox.setChecked(false);
//                            Toast.makeText(Add_Items_List1.this, "no", Toast.LENGTH_SHORT).show();
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
                                            .appendQueryParameter("_id", id)
                                            .build();
                                    getContentResolver().notifyChange(resultUri, null);



                            //        db.delete("Ingredients_item_selection_temp", wherecu, new String[]{});
//                                                    db.del
                                }while (del.moveToNext());
                            }
                        }else {
                            checkbox.setChecked(true);
//                            Toast.makeText(Add_Items_List1.this, "yes", Toast.LENGTH_SHORT).show();
                            int one = Integer.parseInt(usage_count.getText().toString());
                            int two = one+1;
                            usage_count.setText(String.valueOf(two));
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("itemname", checkbox1.getText().toString());
                            contentValues.put("qty_temp", "0");
                            contentValues.put("qty_temp_unit", position1.toString());


                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients_item_selection_temp");
                            resultUri = getContentResolver().insert(contentUri, contentValues);
                            getContentResolver().notifyChange(resultUri, null);



                          //  db.insert("Ingredients_item_selection_temp", null, contentValues);
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



                         //       db.update("Ingredients_item_selection_temp", c, wherecu, new String[]{});
                            } while (del.moveToNext());
                        }

                        Cursor delq = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp WHERE qty = 'yes'", null);
                        int co = delq.getCount();

                        items_count.setText(String.valueOf(co));

                        Cursor cursor_i = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp", null);
                        String[] fromFieldNames = {"itemname", "qty_temp", "qty_temp_unit"};
                        // the XML defined views which the data will be bound to
                        int[] toViewsID = {R.id.current_stock, R.id.current_stock_value, R.id.unit1};

                        android.widget.SimpleCursorAdapter simpleCursorAdapter = new android.widget.SimpleCursorAdapter(Add_Items_List1.this, R.layout.dialog_ingredients_listitems_with_qty, cursor_i, fromFieldNames, toViewsID, 0);
                        listView.setAdapter(simpleCursorAdapter);

                    }
                });

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView qt = (TextView) view.findViewById(R.id.current_stock_value);
                Cursor cu = (Cursor) adapterView.getItemAtPosition(i);
                final String n = cu.getString(cu.getColumnIndex("itemname"));

                final Dialog dialog_ingredient_manager_usage_item = new Dialog(Add_Items_List1.this, R.style.timepicker_date_dialog);
                dialog_ingredient_manager_usage_item.setContentView(R.layout.dialog_ingredient_manager_usage_item);
                dialog_ingredient_manager_usage_item.show();

                ImageButton cancel = (ImageButton) dialog_ingredient_manager_usage_item.findViewById(R.id.btncancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_ingredient_manager_usage_item.dismiss();
                    }
                });


                TextView d_ingredient_name = (TextView) dialog_ingredient_manager_usage_item.findViewById(R.id.ingredient_name);
                d_ingredient_name.setText(ingredient_name.getText().toString());

                TextView itemname = (TextView) dialog_ingredient_manager_usage_item.findViewById(R.id.itemname);
                itemname.setText(n);

//                position1 = unit.getText().toString();

                TextView unit2 = (TextView) dialog_ingredient_manager_usage_item.findViewById(R.id.unit2);
                unit2.setText(unit.getText().toString());

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




                     //       db.update("Ingredients_item_selection_temp", c, wherecu, new String[]{});

                            Cursor cursor_i = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp", null);
                            String[] fromFieldNames = {"itemname", "qty_temp", "qty_temp_unit"};
                            // the XML defined views which the data will be bound to
                            int[] toViewsID = {R.id.current_stock, R.id.current_stock_value, R.id.unit1};

                            android.widget.SimpleCursorAdapter simpleCursorAdapter = new android.widget.SimpleCursorAdapter(Add_Items_List1.this, R.layout.dialog_ingredients_listitems_with_qty, cursor_i, fromFieldNames, toViewsID, 0);
                            listView.setAdapter(simpleCursorAdapter);
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


        ImageButton btnsave = (ImageButton) findViewById(R.id.btnsave);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                        String item_name = cursor.getString(1);
                        String qty_temp = cursor.getString(2);
                        String qty_unit = cursor.getString(3);

                        Cursor cursor2 = db.rawQuery("SELECT * FROM Ingredient_items_list WHERE itemname = '"+item_name+"' AND ingredient_name = '"+player1name+"'", null);
                        if (cursor2.moveToFirst()){
                            String id = cursor2.getString(0);
                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("ingredient_name", player1name);
                            contentValues1.put("itemname", item_name);
                            contentValues1.put("item_qyt_used", qty_temp);
                            contentValues1.put("currnet_stock", current_stock_qty.getText().toString());
                            contentValues1.put("modified_datetimee_new", time24_new);
                            contentValues1.put("qty_unit", qty_unit);
                            if (current_stock_qty.getText().toString().equals("")){
                                float a1 = (Float.parseFloat(player3name));
                                contentValues1.put("required", String.valueOf(a1));
                            }else {
                                float a1 = (Float.parseFloat(player3name) - Float.parseFloat(current_stock_qty.getText().toString()));
                                contentValues1.put("required", String.valueOf(a1));
                            }
                            String where1 = "_id = '"+id+"'";


                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_items_list");
                            getContentResolver().update(contentUri, contentValues1,where1,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Ingredient_items_list")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id", id)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);




                  //          db.update("Ingredient_items_list", contentValues1, where1, new String[]{});
//                            Toast.makeText(Add_Items_List1.this, "update "+player1name+" "+item_name, Toast.LENGTH_LONG).show();
                        }else {
                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("ingredient_name", player1name);
                            contentValues1.put("itemname", item_name);
                            contentValues1.put("item_qyt_used", qty_temp);
                            contentValues1.put("currnet_stock", current_stock_qty.getText().toString());
                            contentValues1.put("date1", currentDateandTime2);
                            contentValues1.put("date", time24_new);
                            contentValues1.put("time1", time1);
                            contentValues1.put("time", time11);
                            contentValues1.put("modified_datetimee_new", time24_new);
                            contentValues1.put("qty_unit", qty_unit);
                            if (current_stock_qty.getText().toString().equals("")){
                                float a1 = (Float.parseFloat(player3name));
                                contentValues1.put("required", String.valueOf(a1));
                            }else {
                                float a1 = (Float.parseFloat(player3name) - Float.parseFloat(current_stock_qty.getText().toString()));
                                contentValues1.put("required", String.valueOf(a1));
                            }


                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_items_list");
                            resultUri = getContentResolver().insert(contentUri, contentValues1);
                            getContentResolver().notifyChange(resultUri, null);


                        //    db.insert("Ingredient_items_list", null, contentValues1);
//                            Toast.makeText(Add_Items_List1.this, "insert "+player1name+" "+item_name, Toast.LENGTH_LONG).show();
                        }

                    }while (cursor.moveToNext());
                }

                Cursor cursor21 = db.rawQuery("SELECT * FROM Ingredient_items_list WHERE ingredient_name = '"+player1name+"' OR ingredient_name = '"+player1name+"'", null);
                if (cursor21.moveToFirst()){
                    do {
                        String id = cursor21.getString(0);
                        String ingr_na = cursor21.getString(2);

                        Cursor cursor211 = db.rawQuery("SELECT * FROM Ingredients_item_selection_temp WHERE itemname = '"+ingr_na+"'", null);
                        if (cursor211.moveToFirst()){

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



                      //      db.delete("Ingredient_items_list", where1, new String[]{});
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




           //     db.delete("Ingredients_item_selection_temp", null, null);

                Cursor cursor2 = db.rawQuery("SELECT * FROM Ingredient_items_list WHERE ingredient_name = '"+player1name+"'", null);
                if (cursor2.moveToFirst()){
                    do {
                        String id = cursor2.getString(0);
                        ContentValues contentValues11 = new ContentValues();
                        contentValues11.put("ingredient_name", player1name);
                        String where1 = "_id = '"+id+"'";



                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_items_list");
                        getContentResolver().update(contentUri, contentValues11,where1,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("Ingredient_items_list")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id", id)
                                .build();
                        getContentResolver().notifyChange(resultUri, null);



                 //       db.update("Ingredient_items_list", contentValues11, where1, new String[]{});
                    }while (cursor2.moveToNext());
                }

                Intent intent = new Intent();
                intent.putExtra("editTextValue", items_count.getText().toString());
//                Toast.makeText(Add_Items_List1.this, "1q "+items_count.getText().toString(), Toast.LENGTH_LONG).show();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
