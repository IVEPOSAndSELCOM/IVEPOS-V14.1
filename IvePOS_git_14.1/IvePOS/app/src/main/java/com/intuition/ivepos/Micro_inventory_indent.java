package com.intuition.ivepos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.intuition.ivepos.syncapp.StubProviderApp;

import java.util.ArrayList;

/**
 * Created by Rohithkumar on 9/13/2017.
 */

public class Micro_inventory_indent extends AppCompatActivity {

    public SQLiteDatabase db = null;

    ListView listView;

    Cursor cursor;

    SimpleCursorAdapter ddataAdapterr;

    TextView text2;
    TextView item_ro, qty_ro, rs_ro;

    String act_st = "";

    Uri contentUri,resultUri;
    String player1name;

    String insert1_cc = "", insert1_rs = "", str_country;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.micro_inventory_indent);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);

        Cursor cursor_country = db.rawQuery("SELECT * FROM Country_Selection", null);
        if (cursor_country.moveToFirst()){
            str_country = cursor_country.getString(1);
        }

        TextView inn = (TextView) findViewById(R.id.inn);

        if (str_country.toString().equals("Rupee") || str_country.toString().equals("India")) {
            insert1_cc = "\u20B9";
            insert1_rs = "Rs.";
            inn.setText(insert1_cc);
        }else {
            if (str_country.toString().equals("Pound") || str_country.toString().equals("UK Pound")) {
                insert1_cc = "\u00a3";
                insert1_rs = "BP.";
                inn.setText(insert1_cc);
            }else {
                if (str_country.toString().equals("Euro") || str_country.toString().equals("UK Euro")) {
                    insert1_cc = "\u20ac";
                    insert1_rs = "EU.";
                    inn.setText(insert1_cc);
                }else {
                    if (str_country.toString().equals("Dollar")) {
                        insert1_cc = "\u0024";
                        insert1_rs = "\u0024";
                        inn.setText(insert1_cc);
                    }else {
                        if (str_country.toString().equals("Dinars")) {
                            insert1_cc = "D";
                            insert1_rs = "KD.";
                            inn.setText(insert1_cc);
                        }else {
                            if (str_country.toString().equals("Shilling")) {
                                insert1_cc = "S";
                                insert1_rs = "S.";
                                inn.setText(insert1_cc);
                            }else {
                                if (str_country.toString().equals("Ringitt")) {
                                    insert1_cc = "R";
                                    insert1_rs = "RM.";
                                    inn.setText(insert1_cc);
                                }else {
                                    if (str_country.toString().equals("Rial")) {
                                        insert1_cc = "R";
                                        insert1_rs = "OR.";
                                        inn.setText(insert1_cc);
                                    }else {
                                        if (str_country.toString().equals("Yen")) {
                                            insert1_cc = "\u00a5";
                                            insert1_rs = "\u00a5";
                                            inn.setText(insert1_cc);
                                        }else {
                                            if (str_country.toString().equals("Papua New Guinean")) {
                                                insert1_cc = "K";
                                                insert1_rs = "K.";
                                                inn.setText(insert1_cc);
                                            }else {
                                                if (str_country.toString().equals("UAE")) {
                                                    insert1_cc = "D";
                                                    insert1_rs = "DH.";
                                                    inn.setText(insert1_cc);
                                                }else {
                                                    if (str_country.toString().equals("South African Rand")) {
                                                        insert1_cc = "R";
                                                        insert1_rs = "R.";
                                                        inn.setText(insert1_cc);
                                                    }else {
                                                        if (str_country.toString().equals("Congolese Franc")) {
                                                            insert1_cc = "F";
                                                            insert1_rs = "FC.";
                                                            inn.setText(insert1_cc);
                                                        }else {
                                                            if (str_country.toString().equals("Qatari Riyals")) {
                                                                insert1_cc = "QAR";
                                                                insert1_rs = "QAR.";
                                                                inn.setText(insert1_cc);
                                                            }else {
                                                                if (str_country.toString().equals("Dirhams")) {
                                                                    insert1_cc = "AED";
                                                                    insert1_rs = "AED.";
                                                                    inn.setText(insert1_cc);
                                                                }else {
                                                                    if (str_country.toString().equals("Kuwait Dinar")) {
                                                                        insert1_cc = "KWD";
                                                                        insert1_rs = "KWD.";
                                                                        inn.setText(insert1_cc);
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

        Bundle extras = getIntent().getExtras();
        player1name = extras.getString("hii");

        ImageView back_pressed = (ImageView) findViewById(R.id.back_pressed);
        back_pressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
                if (player1name.toString().equals("1")){
                    finish();
                }else {
                    Intent intent = new Intent(Micro_inventory_indent.this, Add_manage_ingredient_Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        listView = (ListView) findViewById(R.id.list);


        DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
        downloadMusicfromInternet.execute();


        final EditText myFilter = (EditText) findViewById(R.id.searchView);

        ImageView delete_icon = (ImageView) findViewById(R.id.delete_icon);
        delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFilter.setText("");
            }
        });


        item_ro = (TextView) findViewById(R.id.item_ro);
        qty_ro = (TextView) findViewById(R.id.qty_ro);
        rs_ro = (TextView) findViewById(R.id.rs_ro);

        text2 = (TextView) findViewById(R.id.text2);

        Cursor cursor_qw = db.rawQuery("SELECT * FROM Ingredients WHERE status_low = 'Low'", null);
        int co = cursor_qw.getCount();
        text2.setText(String.valueOf(co));


        LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);
        Cursor cd = db.rawQuery("SELECT * FROM Ingredients WHERE status_qty_updated = 'Add'", null);
        if (cd.moveToFirst()){
            bottom.setVisibility(View.VISIBLE);
            Cursor cursor_qw1 = db.rawQuery("SELECT * FROM Ingredients WHERE status_qty_updated = 'Add'", null);
            int co1 = cursor_qw1.getCount();
            item_ro.setText(String.valueOf(co1));

            float co2 = 0;
            Cursor cursor_qw2 = db.rawQuery("SELECT SUM(add_qty) FROM Ingredients WHERE status_qty_updated = 'Add'", null);
            if (cursor_qw2.moveToFirst()){
                co2 = cursor_qw2.getFloat(0);
            }
            qty_ro.setText(String.valueOf(co2));

            float co3 = 0;
            Cursor cursor_qw3 = db.rawQuery("SELECT * FROM Ingredients WHERE status_qty_updated = 'Add'", null);
            if (cursor_qw3.moveToFirst()){
                do {
                    String add_q = cursor_qw3.getString(22);
                    String ind_p = cursor_qw3.getString(18);

                    TextView tv = new TextView(Micro_inventory_indent.this);
                    tv.setText(add_q);

                    if (tv.getText().toString().equals("")){
                        float co4 = Float.parseFloat(ind_p);
                        co3 = co3 + co4;
                    }else {
                        float co4 = Float.parseFloat(add_q) * Float.parseFloat(ind_p);
                        co3 = co3 + co4;
                    }

                }while (cursor_qw3.moveToNext());
            }
            rs_ro.setText(String.format("%.1f", co3));
        }else {
            bottom.setVisibility(View.GONE);
        }

        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Micro_inventory_indent.this, Micro_Inventory_Indent_Processing.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                act_st = "";
                final Cursor cursor1 = (Cursor) adapterView.getItemAtPosition(i);
                final String ItemID = cursor1.getString(cursor1.getColumnIndex("ingredient_name"));
                final String min_q = cursor1.getString(cursor1.getColumnIndex("min_req"));
                final String max_q = cursor1.getString(cursor1.getColumnIndex("max_req"));
                final String curr_q = cursor1.getString(cursor1.getColumnIndex("current_stock"));
                final String req_q = cursor1.getString(cursor1.getColumnIndex("required"));
                final String unit = cursor1.getString(cursor1.getColumnIndex("unit"));


                final Dialog dialog_ingredient_manager_refill = new Dialog(Micro_inventory_indent.this, R.style.timepicker_date_dialog);
                dialog_ingredient_manager_refill.setContentView(R.layout.dialog_ingredient_manager_refill);
                dialog_ingredient_manager_refill.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog_ingredient_manager_refill.show();

                final ImageButton btncancel = (ImageButton) dialog_ingredient_manager_refill.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_ingredient_manager_refill.dismiss();
                    }
                });

                ImageView im_req_ok_green_refill = (ImageView) dialog_ingredient_manager_refill.findViewById(R.id.im_req_ok);
                ImageView im_req_fill_red_refill = (ImageView) dialog_ingredient_manager_refill.findViewById(R.id.im_req_fill);

                if (Float.parseFloat(curr_q) < Float.parseFloat(min_q)){
                    im_req_fill_red_refill.setVisibility(View.VISIBLE);
                    im_req_ok_green_refill.setVisibility(View.GONE);
                }else {
                    im_req_fill_red_refill.setVisibility(View.GONE);
                    im_req_ok_green_refill.setVisibility(View.VISIBLE);
                }

                TextView tv = new TextView(Micro_inventory_indent.this);
                tv.setText(req_q);

                TextView qty_refill = (TextView) dialog_ingredient_manager_refill.findViewById(R.id.qty);
                if (tv.getText().toString().equals("")){
                    qty_refill.setText("0");
                }else {
                    qty_refill.setText(String.valueOf(req_q));
                }

                final TextView current_stock_value_refill = (TextView) dialog_ingredient_manager_refill.findViewById(R.id.current_stock_value);
                current_stock_value_refill.setText(curr_q);

                TextView ingredient_name = (TextView) dialog_ingredient_manager_refill.findViewById(R.id.ingredient_name);
                ingredient_name.setText(ItemID);


                final EditText refill_qty_value = (EditText) dialog_ingredient_manager_refill.findViewById(R.id.refill_qty_value);
                final EditText unit_price_value_r = (EditText) dialog_ingredient_manager_refill.findViewById(R.id.unit_price_value);


                final TextInputLayout unit_price_value_frame = (TextInputLayout) dialog_ingredient_manager_refill.findViewById(R.id.unit_price_value_frame);

                final TextView items_refill_total = (TextView) dialog_ingredient_manager_refill.findViewById(R.id.items_refill_total);


                Cursor cursor2 = db.rawQuery("SELECT * FROM Ingredients WHERE ingredient_name = '"+ItemID+"'", null);
                if (cursor2.moveToFirst()){
                    String i_p = cursor2.getString(22);
                    String r_p = cursor2.getString(18);
                    String it_r_t = cursor2.getString(23);
                    unit_price_value_r.setText(i_p);
                    refill_qty_value.setText(r_p);
                    items_refill_total.setText(it_r_t);
                }

                unit_price_value_r.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        unit_price_value_frame.setError(null);
                        if (unit_price_value_r.getText().toString().equals("") || refill_qty_value.getText().toString().equals("")){
                            items_refill_total.setText("0");
                        }else {
                            float v = Float.parseFloat(unit_price_value_r.getText().toString()) * Float.parseFloat(refill_qty_value.getText().toString());
                            items_refill_total.setText(String.valueOf(v));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                final TextView unit3 = (TextView) dialog_ingredient_manager_refill.findViewById(R.id.unit);
                unit3.setText(unit);

                final TextView adjust_quan_q = (TextView) dialog_ingredient_manager_refill.findViewById(R.id.adjust_quan_q);

                final RelativeLayout exact_cost = (RelativeLayout) dialog_ingredient_manager_refill.findViewById(R.id.exact_cost);
                exact_cost.setVisibility(View.GONE);


                final RadioButton no = (RadioButton) dialog_ingredient_manager_refill.findViewById(R.id.no);
                final RadioButton yes = (RadioButton) dialog_ingredient_manager_refill.findViewById(R.id.yes);

                Cursor cursor = db.rawQuery("SELECT * FROM Ingredients WHERE ingredient_name = '"+ItemID+"'", null);
                if (cursor.moveToFirst()){
                    String adj_st = cursor.getString(20);

                    TextView adj = new TextView(Micro_inventory_indent.this);
                    adj.setText(adj_st);

                    if (adj.getText().toString().equals("") || adj.getText().toString().equals("0")){

                    }else {
                        no.setChecked(true);
                        exact_cost.setVisibility(View.VISIBLE);
                        adjust_quan_q.setText(adj.getText().toString());

                    }
                }

                no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            final Dialog dialog_ingredient_manager_refill_adjust = new Dialog(Micro_inventory_indent.this, R.style.timepicker_date_dialog);
                            dialog_ingredient_manager_refill_adjust.setContentView(R.layout.dialog_ingredient_manager_refill_adjust_current_stock);
                            dialog_ingredient_manager_refill_adjust.show();

                            TextView unit2 = (TextView) dialog_ingredient_manager_refill_adjust.findViewById(R.id.unit2);
                            unit2.setText(unit);

                            final EditText adjust_current_stock_value = (EditText) dialog_ingredient_manager_refill_adjust.findViewById(R.id.adjust_current_stock_value);
                            final TextInputLayout adjust_current_stock_value_la = (TextInputLayout) dialog_ingredient_manager_refill_adjust.findViewById(R.id.adjust_current_stock_value_la);


                            TextView ingredient_name = (TextView) dialog_ingredient_manager_refill_adjust.findViewById(R.id.ingredient_name);
                            ingredient_name.setText(ItemID);

                            TextView th_current_stock = (TextView) dialog_ingredient_manager_refill_adjust.findViewById(R.id.th_current_stock);
                            th_current_stock.setText(current_stock_value_refill.getText().toString());

                            adjust_current_stock_value.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    adjust_current_stock_value_la.setError(null);
                                }

                                @Override
                                public void afterTextChanged(Editable editable) {

                                }
                            });

                            ImageButton btncancel = (ImageButton) dialog_ingredient_manager_refill_adjust.findViewById(R.id.btncancel);
                            btncancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog_ingredient_manager_refill_adjust.dismiss();
                                    yes.setChecked(true);
                                    no.setChecked(false);
                                }
                            });

                            Cursor cursor = db.rawQuery("SELECT * FROM Ingredients WHERE ingredient_name = '"+ItemID+"'", null);
                            if (cursor.moveToFirst()){
                                String adj_st = cursor.getString(20);

                                TextView adj = new TextView(Micro_inventory_indent.this);
                                adj.setText(adj_st);

                                if (adj.getText().toString().equals("") || adj.getText().toString().equals("0")){

                                }else {
                                    adjust_current_stock_value.setText(adj.getText().toString());
                                }
                            }

                            final ImageButton btnsave = (ImageButton) dialog_ingredient_manager_refill_adjust.findViewById(R.id.btnsave);
                            btnsave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (adjust_current_stock_value.getText().toString().equals("")) {
                                        adjust_current_stock_value_la.setError("Enter current stock");
                                    } else {
                                        act_st = adjust_current_stock_value.getText().toString();
                                        adjust_quan_q.setText(adjust_current_stock_value.getText().toString());
                                        dialog_ingredient_manager_refill_adjust.dismiss();
                                        exact_cost.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                            adjust_current_stock_value.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                                @Override
                                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                    if (actionId == EditorInfo.IME_ACTION_GO) {
                                        btnsave.performClick();
                                        return true;
                                    }
                                    return false;
                                }
                            });
                        }else {
                            //not checked don't display dialog box
                            exact_cost.setVisibility(View.GONE);
                        }
                    }
                });


                final ImageButton btnsave = (ImageButton) dialog_ingredient_manager_refill.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (refill_qty_value.getText().toString().equals("") || unit_price_value_r.getText().toString().equals("")){
                            if (refill_qty_value.getText().toString().equals("")){
                                refill_qty_value.setError("Enter qty.");
                            }
                            if (unit_price_value_r.getText().toString().equals("")){
                                unit_price_value_r.setError("Enter price");
                            }
                        }else {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("status_qty_updated", "Add");
                            contentValues.put("add_qty", refill_qty_value.getText().toString());
                            contentValues.put("indiv_price_temp", unit_price_value_r.getText().toString());
                            contentValues.put("adjusted_stock", adjust_quan_q.getText().toString());
                            if (adjust_quan_q.getText().toString().equals("0") || adjust_quan_q.getText().toString().equals("")){
                                contentValues.put("diff_stock", "0");
                            }else {
                                float a = Float.parseFloat(current_stock_value_refill.getText().toString()) - Float.parseFloat(adjust_quan_q.getText().toString());
                                String a1 = String.format("%.1f", a);
                                contentValues.put("diff_stock", a1);
                            }
                            contentValues.put("total_price_temp", items_refill_total.getText().toString());
                            String where = "ingredient_name = '"+ItemID+"'";

                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients");
                            getContentResolver().update(contentUri, contentValues,where,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Ingredients")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("ingredient_name", ItemID)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);

//                            db.update("Ingredients", contentValues, where, new String[]{});
                            Toast.makeText(Micro_inventory_indent.this, "updated "+ItemID, Toast.LENGTH_LONG).show();

                            cursor1.moveToPosition(i);
                            cursor1.requery();
                            ddataAdapterr.notifyDataSetChanged();



                            LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);
                            Cursor cd = db.rawQuery("SELECT * FROM Ingredients WHERE status_qty_updated = 'Add'", null);
                            if (cd.moveToFirst()){
                                bottom.setVisibility(View.VISIBLE);
                                Cursor cursor_qw1 = db.rawQuery("SELECT * FROM Ingredients WHERE status_qty_updated = 'Add'", null);
                                int co1 = cursor_qw1.getCount();
                                item_ro.setText(String.valueOf(co1));

                                float co2 = 0;
                                Cursor cursor_qw2 = db.rawQuery("SELECT SUM(add_qty) FROM Ingredients WHERE status_qty_updated = 'Add'", null);
                                if (cursor_qw2.moveToFirst()){
                                    co2 = cursor_qw2.getFloat(0);
                                }
                                qty_ro.setText(String.valueOf(co2));

                                float co3 = 0;
                                Cursor cursor_qw3 = db.rawQuery("SELECT * FROM Ingredients WHERE status_qty_updated = 'Add'", null);
                                if (cursor_qw3.moveToFirst()){
                                    do {
                                        String add_q = cursor_qw3.getString(22);
                                        String ind_p = cursor_qw3.getString(18);

                                        TextView tv = new TextView(Micro_inventory_indent.this);
                                        tv.setText(add_q);

                                        if (tv.getText().toString().equals("")){
                                            float co4 = Float.parseFloat(ind_p);
                                            co3 = co3 + co4;
                                        }else {
                                            float co4 = Float.parseFloat(add_q) * Float.parseFloat(ind_p);
                                            co3 = co3 + co4;
                                        }

                                    }while (cursor_qw3.moveToNext());
                                }
                                rs_ro.setText(String.format("%.1f", co3));
                            }else {
                                bottom.setVisibility(View.GONE);
                            }

                            TextView inn = (TextView) findViewById(R.id.inn);

                            if (str_country.toString().equals("Rupee") || str_country.toString().equals("India")) {
                                insert1_cc = "\u20B9";
                                insert1_rs = "Rs.";
                                inn.setText(insert1_cc);
                            }else {
                                if (str_country.toString().equals("Pound") || str_country.toString().equals("UK Pound")) {
                                    insert1_cc = "\u00a3";
                                    insert1_rs = "BP.";
                                    inn.setText(insert1_cc);
                                }else {
                                    if (str_country.toString().equals("Euro") || str_country.toString().equals("UK Euro")) {
                                        insert1_cc = "\u20ac";
                                        insert1_rs = "EU.";
                                        inn.setText(insert1_cc);
                                    }else {
                                        if (str_country.toString().equals("Dollar")) {
                                            insert1_cc = "\u0024";
                                            insert1_rs = "\u0024";
                                            inn.setText(insert1_cc);
                                        }else {
                                            if (str_country.toString().equals("Dinars")) {
                                                insert1_cc = "D";
                                                insert1_rs = "KD.";
                                                inn.setText(insert1_cc);
                                            }else {
                                                if (str_country.toString().equals("Shilling")) {
                                                    insert1_cc = "S";
                                                    insert1_rs = "S.";
                                                    inn.setText(insert1_cc);
                                                }else {
                                                    if (str_country.toString().equals("Ringitt")) {
                                                        insert1_cc = "R";
                                                        insert1_rs = "RM.";
                                                        inn.setText(insert1_cc);
                                                    }else {
                                                        if (str_country.toString().equals("Rial")) {
                                                            insert1_cc = "R";
                                                            insert1_rs = "OR.";
                                                            inn.setText(insert1_cc);
                                                        }else {
                                                            if (str_country.toString().equals("Yen")) {
                                                                insert1_cc = "\u00a5";
                                                                insert1_rs = "\u00a5";
                                                                inn.setText(insert1_cc);
                                                            }else {
                                                                if (str_country.toString().equals("Papua New Guinean")) {
                                                                    insert1_cc = "K";
                                                                    insert1_rs = "K.";
                                                                    inn.setText(insert1_cc);
                                                                }else {
                                                                    if (str_country.toString().equals("UAE")) {
                                                                        insert1_cc = "D";
                                                                        insert1_rs = "DH.";
                                                                        inn.setText(insert1_cc);
                                                                    }else {
                                                                        if (str_country.toString().equals("South African Rand")) {
                                                                            insert1_cc = "R";
                                                                            insert1_rs = "R.";
                                                                            inn.setText(insert1_cc);
                                                                        }else {
                                                                            if (str_country.toString().equals("Congolese Franc")) {
                                                                                insert1_cc = "F";
                                                                                insert1_rs = "FC.";
                                                                                inn.setText(insert1_cc);
                                                                            }else {
                                                                                if (str_country.toString().equals("Qatari Riyals")) {
                                                                                    insert1_cc = "QAR";
                                                                                    insert1_rs = "QAR.";
                                                                                    inn.setText(insert1_cc);
                                                                                }else {
                                                                                    if (str_country.toString().equals("Dirhams")) {
                                                                                        insert1_cc = "AED";
                                                                                        insert1_rs = "AED.";
                                                                                        inn.setText(insert1_cc);
                                                                                    }else {
                                                                                        if (str_country.toString().equals("Kuwait Dinar")) {
                                                                                            insert1_cc = "KWD";
                                                                                            insert1_rs = "KWD.";
                                                                                            inn.setText(insert1_cc);
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

                            dialog_ingredient_manager_refill.dismiss();


                        }
                    }
                });


                unit_price_value_r.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_GO) {
                            btnsave.performClick();
                            return true;
                        }
                        return false;
                    }
                });

//                final TextView unit_price_value_enter = (TextView) dialog_ingredient_manager_refill.findViewById(R.id.unit_price_value);

            }
        });

        final TextView text1 = (TextView) findViewById(R.id.text1);
        final TextView text2 = (TextView) findViewById(R.id.text2);
        RelativeLayout rel = (RelativeLayout) findViewById(R.id.rel);
        rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!text1.getText().toString().equals("Low stock")){
                    text1.setText("Low stock");

                    cursor = db.rawQuery("SELECT * FROM Ingredients ORDER BY ingredient_name ASC", null);
                    String[] fromFieldNames = {"ingredient_name", "barcode", "unit", "min_req", "max_req", "current_stock", "required", "add_qty", "status_low", "status_qty_updated"};
                    int[] toViewsID = {R.id.ingredient_name, R.id.barcode_value, R.id.unit, R.id.min, R.id.max, R.id.current, R.id.requ, R.id.add_qty, R.id.image, R.id.image1};
                    ddataAdapterr = new SimpleCursorAdapter(Micro_inventory_indent.this, R.layout.micro_inventory_ingredient_listview, cursor, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(ddataAdapterr);


                    LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);
                    Cursor cd = db.rawQuery("SELECT * FROM Ingredients WHERE status_qty_updated = 'Add'", null);
                    if (cd.moveToFirst()){
                        bottom.setVisibility(View.VISIBLE);
                        Cursor cursor_qw1 = db.rawQuery("SELECT * FROM Ingredients WHERE status_qty_updated = 'Add'", null);
                        int co1 = cursor_qw1.getCount();
                        item_ro.setText(String.valueOf(co1));

                        float co2 = 0;
                        Cursor cursor_qw2 = db.rawQuery("SELECT SUM(add_qty) FROM Ingredients WHERE status_qty_updated = 'Add'", null);
                        if (cursor_qw2.moveToFirst()){
                            co2 = cursor_qw2.getFloat(0);
                        }
                        qty_ro.setText(String.valueOf(co2));

                        float co3 = 0;
                        Cursor cursor_qw3 = db.rawQuery("SELECT * FROM Ingredients WHERE status_qty_updated = 'Add'", null);
                        if (cursor_qw3.moveToFirst()){
                            do {
                                String add_q = cursor_qw3.getString(22);
                                String ind_p = cursor_qw3.getString(18);

                                TextView tv = new TextView(Micro_inventory_indent.this);
                                tv.setText(add_q);

                                if (tv.getText().toString().equals("")){
                                    float co4 = Float.parseFloat(ind_p);
                                    co3 = co3 + co4;
                                }else {
                                    float co4 = Float.parseFloat(add_q) * Float.parseFloat(ind_p);
                                    co3 = co3 + co4;
                                }

                            }while (cursor_qw3.moveToNext());
                        }
                        rs_ro.setText(String.format("%.1f", co3));
                    }else {
                        bottom.setVisibility(View.GONE);
                    }

                    TextView inn = (TextView) findViewById(R.id.inn);

                    if (str_country.toString().equals("Rupee") || str_country.toString().equals("India")) {
                        insert1_cc = "\u20B9";
                        insert1_rs = "Rs.";
                        inn.setText(insert1_cc);
                    }else {
                        if (str_country.toString().equals("Pound") || str_country.toString().equals("UK Pound")) {
                            insert1_cc = "\u00a3";
                            insert1_rs = "BP.";
                            inn.setText(insert1_cc);
                        }else {
                            if (str_country.toString().equals("Euro") || str_country.toString().equals("UK Euro")) {
                                insert1_cc = "\u20ac";
                                insert1_rs = "EU.";
                                inn.setText(insert1_cc);
                            }else {
                                if (str_country.toString().equals("Dollar")) {
                                    insert1_cc = "\u0024";
                                    insert1_rs = "\u0024";
                                    inn.setText(insert1_cc);
                                }else {
                                    if (str_country.toString().equals("Dinars")) {
                                        insert1_cc = "D";
                                        insert1_rs = "KD.";
                                        inn.setText(insert1_cc);
                                    }else {
                                        if (str_country.toString().equals("Shilling")) {
                                            insert1_cc = "S";
                                            insert1_rs = "S.";
                                            inn.setText(insert1_cc);
                                        }else {
                                            if (str_country.toString().equals("Ringitt")) {
                                                insert1_cc = "R";
                                                insert1_rs = "RM.";
                                                inn.setText(insert1_cc);
                                            }else {
                                                if (str_country.toString().equals("Rial")) {
                                                    insert1_cc = "R";
                                                    insert1_rs = "OR.";
                                                    inn.setText(insert1_cc);
                                                }else {
                                                    if (str_country.toString().equals("Yen")) {
                                                        insert1_cc = "\u00a5";
                                                        insert1_rs = "\u00a5";
                                                        inn.setText(insert1_cc);
                                                    }else {
                                                        if (str_country.toString().equals("Papua New Guinean")) {
                                                            insert1_cc = "K";
                                                            insert1_rs = "K.";
                                                            inn.setText(insert1_cc);
                                                        }else {
                                                            if (str_country.toString().equals("UAE")) {
                                                                insert1_cc = "D";
                                                                insert1_rs = "DH.";
                                                                inn.setText(insert1_cc);
                                                            }else {
                                                                if (str_country.toString().equals("South African Rand")) {
                                                                    insert1_cc = "R";
                                                                    insert1_rs = "R.";
                                                                    inn.setText(insert1_cc);
                                                                }else {
                                                                    if (str_country.toString().equals("Congolese Franc")) {
                                                                        insert1_cc = "F";
                                                                        insert1_rs = "FC.";
                                                                        inn.setText(insert1_cc);
                                                                    }else {
                                                                        if (str_country.toString().equals("Qatari Riyals")) {
                                                                            insert1_cc = "QAR";
                                                                            insert1_rs = "QAR.";
                                                                            inn.setText(insert1_cc);
                                                                        }else {
                                                                            if (str_country.toString().equals("Dirhams")) {
                                                                                insert1_cc = "AED";
                                                                                insert1_rs = "AED.";
                                                                                inn.setText(insert1_cc);
                                                                            }else {
                                                                                if (str_country.toString().equals("Kuwait Dinar")) {
                                                                                    insert1_cc = "KWD";
                                                                                    insert1_rs = "KWD.";
                                                                                    inn.setText(insert1_cc);
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

                    bottom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Micro_inventory_indent.this, Inventory_Indent_Processing.class);
                            startActivity(intent);
                        }
                    });

                    final EditText myFilter = (EditText) findViewById(R.id.searchView);
                    myFilter.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {
                        }

                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            ddataAdapterr.getFilter().filter(s.toString());
                        }
                    });

                    ddataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
                        public Cursor runQuery(CharSequence constraint) {
                            return fetchCountriesByName1(constraint.toString());
                        }
                    });

                    Cursor cursor_qw = db.rawQuery("SELECT * FROM Ingredients WHERE status_low = 'Low'", null);
                    int co = cursor_qw.getCount();
                    text2.setText(String.valueOf(co));

                }else {
                    text1.setText("All stock");
                    text2.setText("");

                    cursor = db.rawQuery("SELECT * FROM Ingredients WHERE status_low = 'Low' ORDER BY ingredient_name ASC", null);
                    String[] fromFieldNames = {"ingredient_name", "barcode", "unit", "min_req", "max_req", "current_stock", "required", "add_qty", "status_low", "status_qty_updated"};
                    int[] toViewsID = {R.id.ingredient_name, R.id.barcode_value, R.id.unit, R.id.min, R.id.max, R.id.current, R.id.requ, R.id.add_qty, R.id.image, R.id.image1};
                    ddataAdapterr = new SimpleCursorAdapter(Micro_inventory_indent.this, R.layout.micro_inventory_ingredient_listview, cursor, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(ddataAdapterr);


                    LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);
                    Cursor cd = db.rawQuery("SELECT * FROM Ingredients WHERE status_qty_updated = 'Add'", null);
                    if (cd.moveToFirst()){
                        bottom.setVisibility(View.VISIBLE);
                        Cursor cursor_qw1 = db.rawQuery("SELECT * FROM Ingredients WHERE status_qty_updated = 'Add'", null);
                        int co1 = cursor_qw1.getCount();
                        item_ro.setText(String.valueOf(co1));

                        float co2 = 0;
                        Cursor cursor_qw2 = db.rawQuery("SELECT SUM(add_qty) FROM Ingredients WHERE status_qty_updated = 'Add'", null);
                        if (cursor_qw2.moveToFirst()){
                            co2 = cursor_qw2.getFloat(0);
                        }
                        qty_ro.setText(String.valueOf(co2));

                        float co3 = 0;
                        Cursor cursor_qw3 = db.rawQuery("SELECT * FROM Ingredients WHERE status_qty_updated = 'Add'", null);
                        if (cursor_qw3.moveToFirst()){
                            do {
                                String add_q = cursor_qw3.getString(22);
                                String ind_p = cursor_qw3.getString(18);

                                TextView tv = new TextView(Micro_inventory_indent.this);
                                tv.setText(add_q);

                                if (tv.getText().toString().equals("")){
                                    float co4 = Float.parseFloat(ind_p);
                                    co3 = co3 + co4;
                                }else {
                                    float co4 = Float.parseFloat(add_q) * Float.parseFloat(ind_p);
                                    co3 = co3 + co4;
                                }

                            }while (cursor_qw3.moveToNext());
                        }
                        rs_ro.setText(String.format("%.1f", co3));
                    }else {
                        bottom.setVisibility(View.GONE);
                    }

                    TextView inn = (TextView) findViewById(R.id.inn);

                    if (str_country.toString().equals("Rupee") || str_country.toString().equals("India")) {
                        insert1_cc = "\u20B9";
                        insert1_rs = "Rs.";
                        inn.setText(insert1_cc);
                    }else {
                        if (str_country.toString().equals("Pound") || str_country.toString().equals("UK Pound")) {
                            insert1_cc = "\u00a3";
                            insert1_rs = "BP.";
                            inn.setText(insert1_cc);
                        }else {
                            if (str_country.toString().equals("Euro") || str_country.toString().equals("UK Euro")) {
                                insert1_cc = "\u20ac";
                                insert1_rs = "EU.";
                                inn.setText(insert1_cc);
                            }else {
                                if (str_country.toString().equals("Dollar")) {
                                    insert1_cc = "\u0024";
                                    insert1_rs = "\u0024";
                                    inn.setText(insert1_cc);
                                }else {
                                    if (str_country.toString().equals("Dinars")) {
                                        insert1_cc = "D";
                                        insert1_rs = "KD.";
                                        inn.setText(insert1_cc);
                                    }else {
                                        if (str_country.toString().equals("Shilling")) {
                                            insert1_cc = "S";
                                            insert1_rs = "S.";
                                            inn.setText(insert1_cc);
                                        }else {
                                            if (str_country.toString().equals("Ringitt")) {
                                                insert1_cc = "R";
                                                insert1_rs = "RM.";
                                                inn.setText(insert1_cc);
                                            }else {
                                                if (str_country.toString().equals("Rial")) {
                                                    insert1_cc = "R";
                                                    insert1_rs = "OR.";
                                                    inn.setText(insert1_cc);
                                                }else {
                                                    if (str_country.toString().equals("Yen")) {
                                                        insert1_cc = "\u00a5";
                                                        insert1_rs = "\u00a5";
                                                        inn.setText(insert1_cc);
                                                    }else {
                                                        if (str_country.toString().equals("Papua New Guinean")) {
                                                            insert1_cc = "K";
                                                            insert1_rs = "K.";
                                                            inn.setText(insert1_cc);
                                                        }else {
                                                            if (str_country.toString().equals("UAE")) {
                                                                insert1_cc = "D";
                                                                insert1_rs = "DH.";
                                                                inn.setText(insert1_cc);
                                                            }else {
                                                                if (str_country.toString().equals("South African Rand")) {
                                                                    insert1_cc = "R";
                                                                    insert1_rs = "R.";
                                                                    inn.setText(insert1_cc);
                                                                }else {
                                                                    if (str_country.toString().equals("Congolese Franc")) {
                                                                        insert1_cc = "F";
                                                                        insert1_rs = "FC.";
                                                                        inn.setText(insert1_cc);
                                                                    }else {
                                                                        if (str_country.toString().equals("Qatari Riyals")) {
                                                                            insert1_cc = "QAR";
                                                                            insert1_rs = "QAR.";
                                                                            inn.setText(insert1_cc);
                                                                        }else {
                                                                            if (str_country.toString().equals("Dirhams")) {
                                                                                insert1_cc = "AED";
                                                                                insert1_rs = "AED.";
                                                                                inn.setText(insert1_cc);
                                                                            }else {
                                                                                if (str_country.toString().equals("Kuwait Dinar")) {
                                                                                    insert1_cc = "KWD";
                                                                                    insert1_rs = "KWD.";
                                                                                    inn.setText(insert1_cc);
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

                    bottom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Micro_inventory_indent.this, Inventory_Indent_Processing.class);
                            startActivity(intent);
                        }
                    });

                    final EditText myFilter = (EditText) findViewById(R.id.searchView);
                    myFilter.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {
                        }

                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            ddataAdapterr.getFilter().filter(s.toString());
                        }
                    });

                    ddataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
                        public Cursor runQuery(CharSequence constraint) {
                            return fetchCountriesByName1(constraint.toString());
                        }
                    });

                }
            }
        });

    }


    public Cursor fetchCountriesByName1(String inputtext) throws SQLException {

        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
            mCursor = db.rawQuery("SELECT * FROM Ingredients", null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Ingredients WHERE ingredient_name LIKE '%" + inputtext + "%' OR barcode LIKE '%" + inputtext + "%'", null);
        }

        return mCursor;
    }

    class DownloadMusicfromInternet extends AsyncTask<Void, Integer, Void> {
        private ProgressDialog dialog = new ProgressDialog(Micro_inventory_indent.this, R.style.timepicker_date_dialog);

        @Override
        protected Void doInBackground(Void... params) {


            Cursor cursor = db.rawQuery("SELECT * FROM Ingredients", null);
            if (cursor.moveToFirst()){
                do {
                    String id = cursor.getString(0);
                    String cur_qty = cursor.getString(4);
                    String min_qty = cursor.getString(2);


                    ContentValues contentValues = new ContentValues();
                    contentValues.put("status_low", "");
                    String where = "_id = '"+id+"'";

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients");
                    getContentResolver().update(contentUri, contentValues,where,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Ingredients")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id", id)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);


//                    db.update("Ingredients", contentValues, where, new String[]{});

                    TextView tv = new TextView(Micro_inventory_indent.this);
                    tv.setText(min_qty);

//                    TextView tv1 = new TextView(Micro_inventory_indent.this);
//                    tv.setText(cur_qty);

                    if (tv.getText().toString().equals("")){

                    }else {
                        if (Float.parseFloat(cur_qty) <= Float.parseFloat(tv.getText().toString())){
                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("status_low", "Low");
                            String where1 = "_id = '"+id+"'";
                            System.out.println("ingredient qty is "+id+" current qty "+cur_qty+" min qty "+tv.getText().toString());



                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients");
                            getContentResolver().update(contentUri, contentValues1,where1,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Ingredients")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id", id)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);

//                            db.update("Ingredients", contentValues1, where1, new String[]{});
                        }
                    }
                }while (cursor.moveToNext());
            }


            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);

            dialog.setMessage("Loading");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            dialog.setMax(1000);
            //Set the current progress to zero
            dialog.setProgress(0);
            //Display the progress dialog
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
        protected void onPostExecute(Void result) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();

            dialog.dismiss();

            text2 = (TextView) findViewById(R.id.text2);

            Cursor cursor_qw = db.rawQuery("SELECT * FROM Ingredients WHERE status_low = 'Low'", null);
            int co = cursor_qw.getCount();
            text2.setText(String.valueOf(co));


            LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);
            Cursor cd = db.rawQuery("SELECT * FROM Ingredients WHERE status_qty_updated = 'Add'", null);
            if (cd.moveToFirst()){
                bottom.setVisibility(View.VISIBLE);
                Cursor cursor_qw1 = db.rawQuery("SELECT * FROM Ingredients WHERE status_qty_updated = 'Add'", null);
                int co1 = cursor_qw1.getCount();
                item_ro.setText(String.valueOf(co1));

                float co2 = 0;
                Cursor cursor_qw2 = db.rawQuery("SELECT SUM(add_qty) FROM Ingredients WHERE status_qty_updated = 'Add'", null);
                if (cursor_qw2.moveToFirst()){
                    co2 = cursor_qw2.getFloat(0);
                }
                qty_ro.setText(String.valueOf(co2));

                float co3 = 0;
                Cursor cursor_qw3 = db.rawQuery("SELECT * FROM Ingredients WHERE status_qty_updated = 'Add'", null);
                if (cursor_qw3.moveToFirst()){
                    do {
                        String add_q = cursor_qw3.getString(22);
                        String ind_p = cursor_qw3.getString(18);

                        TextView tv = new TextView(Micro_inventory_indent.this);
                        tv.setText(add_q);

                        if (tv.getText().toString().equals("")){
                            float co4 = Float.parseFloat(ind_p);
                            co3 = co3 + co4;
                        }else {
                            float co4 = Float.parseFloat(add_q) * Float.parseFloat(ind_p);
                            co3 = co3 + co4;
                        }

                    }while (cursor_qw3.moveToNext());
                }
                rs_ro.setText(String.format("%.1f", co3));
            }else {
                bottom.setVisibility(View.GONE);
            }

            TextView inn = (TextView) findViewById(R.id.inn);

            if (str_country.toString().equals("Rupee") || str_country.toString().equals("India")) {
                insert1_cc = "\u20B9";
                insert1_rs = "Rs.";
                inn.setText(insert1_cc);
            }else {
                if (str_country.toString().equals("Pound") || str_country.toString().equals("UK Pound")) {
                    insert1_cc = "\u00a3";
                    insert1_rs = "BP.";
                    inn.setText(insert1_cc);
                }else {
                    if (str_country.toString().equals("Euro") || str_country.toString().equals("UK Euro")) {
                        insert1_cc = "\u20ac";
                        insert1_rs = "EU.";
                        inn.setText(insert1_cc);
                    }else {
                        if (str_country.toString().equals("Dollar")) {
                            insert1_cc = "\u0024";
                            insert1_rs = "\u0024";
                            inn.setText(insert1_cc);
                        }else {
                            if (str_country.toString().equals("Dinars")) {
                                insert1_cc = "D";
                                insert1_rs = "KD.";
                                inn.setText(insert1_cc);
                            }else {
                                if (str_country.toString().equals("Shilling")) {
                                    insert1_cc = "S";
                                    insert1_rs = "S.";
                                    inn.setText(insert1_cc);
                                }else {
                                    if (str_country.toString().equals("Ringitt")) {
                                        insert1_cc = "R";
                                        insert1_rs = "RM.";
                                        inn.setText(insert1_cc);
                                    }else {
                                        if (str_country.toString().equals("Rial")) {
                                            insert1_cc = "R";
                                            insert1_rs = "OR.";
                                            inn.setText(insert1_cc);
                                        }else {
                                            if (str_country.toString().equals("Yen")) {
                                                insert1_cc = "\u00a5";
                                                insert1_rs = "\u00a5";
                                                inn.setText(insert1_cc);
                                            }else {
                                                if (str_country.toString().equals("Papua New Guinean")) {
                                                    insert1_cc = "K";
                                                    insert1_rs = "K.";
                                                    inn.setText(insert1_cc);
                                                }else {
                                                    if (str_country.toString().equals("UAE")) {
                                                        insert1_cc = "D";
                                                        insert1_rs = "DH.";
                                                        inn.setText(insert1_cc);
                                                    }else {
                                                        if (str_country.toString().equals("South African Rand")) {
                                                            insert1_cc = "R";
                                                            insert1_rs = "R.";
                                                            inn.setText(insert1_cc);
                                                        }else {
                                                            if (str_country.toString().equals("Congolese Franc")) {
                                                                insert1_cc = "F";
                                                                insert1_rs = "FC.";
                                                                inn.setText(insert1_cc);
                                                            }else {
                                                                if (str_country.toString().equals("Qatari Riyals")) {
                                                                    insert1_cc = "QAR";
                                                                    insert1_rs = "QAR.";
                                                                    inn.setText(insert1_cc);
                                                                }else {
                                                                    if (str_country.toString().equals("Dirhams")) {
                                                                        insert1_cc = "AED";
                                                                        insert1_rs = "AED.";
                                                                        inn.setText(insert1_cc);
                                                                    }else {
                                                                        if (str_country.toString().equals("Kuwait Dinar")) {
                                                                            insert1_cc = "KWD";
                                                                            insert1_rs = "KWD.";
                                                                            inn.setText(insert1_cc);
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

            cursor = db.rawQuery("SELECT * FROM Ingredients ORDER BY ingredient_name ASC", null);
            String[] fromFieldNames = {"ingredient_name", "barcode", "unit", "min_req", "max_req", "current_stock", "required", "add_qty", "status_low", "status_qty_updated"};
            int[] toViewsID = {R.id.ingredient_name, R.id.barcode_value, R.id.unit, R.id.min, R.id.max, R.id.current, R.id.requ, R.id.add_qty, R.id.image, R.id.image1};
            ddataAdapterr = new SimpleCursorAdapter(Micro_inventory_indent.this, R.layout.micro_inventory_ingredient_listview, cursor, fromFieldNames, toViewsID, 0);
            listView.setAdapter(ddataAdapterr);

            final EditText myFilter = (EditText) findViewById(R.id.searchView);
            myFilter.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ddataAdapterr.getFilter().filter(s.toString());
                }
            });

            ddataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
                public Cursor runQuery(CharSequence constraint) {
                    return fetchCountriesByName1(constraint.toString());
                }
            });


//            LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);
//            Cursor cd = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
//            if (cd.moveToFirst()){
//                bottom.setVisibility(View.VISIBLE);
//                Cursor cursor_qw1 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
//                int co1 = cursor_qw1.getCount();
//                item_ro.setText(String.valueOf(co1));
//
//                float co2 = 0;
//                Cursor cursor_qw2 = db.rawQuery("SELECT SUM(add_qty) FROM Items WHERE status_qty_updated = 'Add'", null);
//                if (cursor_qw2.moveToFirst()){
//                    co2 = cursor_qw2.getFloat(0);
//                }
//                qty_ro.setText(String.valueOf(co2));
//
//                float co3 = 0;
//                Cursor cursor_qw3 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated = 'Add'", null);
//                if (cursor_qw3.moveToFirst()){
//                    do {
//                        String add_q = cursor_qw3.getString(22);
//                        String ind_p = cursor_qw3.getString(25);
//
//                        float co4 = Float.parseFloat(add_q) * Float.parseFloat(ind_p);
//                        co3 = co3+co4;
//                    }while (cursor_qw3.moveToNext());
//                }
//                rs_ro.setText(String.format("%.1f", co3));
//            }else {
//                bottom.setVisibility(View.GONE);
//            }
//
//
//            bottom.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(Inventory_Indent.this, Inventory_Indent_Processing.class);
//                    startActivity(intent);
//                }
//            });
//
//            text2 = (TextView) findViewById(R.id.text2);
//
//            Cursor cursor_qw = db.rawQuery("SELECT * FROM Items WHERE status_low = 'Low'", null);
//            int co = cursor_qw.getCount();
//            text2.setText(String.valueOf(co));

        }
    }



    private class MyCustomAdapter extends ArrayAdapter<Micro_inventorey_listitems> {

        private ArrayList<Micro_inventorey_listitems> originalList;
        private ArrayList<Micro_inventorey_listitems> countryList;
        private CountryFilter filter;

        private Cursor c;
        private Context context;

        private SparseBooleanArray mSelectedItemsIds;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Micro_inventorey_listitems> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<Micro_inventorey_listitems>();
            this.countryList.addAll(countryList);
            this.originalList = new ArrayList<Micro_inventorey_listitems>();
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
            TextView req;
            TextView add_qty;
            TextView unit;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));
            if (convertView == null) {

                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.micro_inventory_ingredient_listview, null);

                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.ingredient_name);
                holder.barcode = (TextView) convertView.findViewById(R.id.ingredient_barcode);
                holder.min = (TextView) convertView.findViewById(R.id.min);
                holder.max = (TextView) convertView.findViewById(R.id.max);
                holder.current = (TextView) convertView.findViewById(R.id.current);
                holder.req = (TextView) convertView.findViewById(R.id.requ);
                holder.add_qty = (TextView) convertView.findViewById(R.id.add_qty);
                holder.unit = (TextView) convertView.findViewById(R.id.unit);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Micro_inventorey_listitems country = countryList.get(position);
            holder.name.setText(country.getName());
            holder.barcode.setText(country.getBarcode());
            holder.min.setText(country.getmin());
            holder.max.setText(country.getmax());
            holder.current.setText(country.getcurrent());
            holder.req.setText(country.getReq());
            holder.add_qty.setText(country.getAdd());
            holder.unit.setText(country.getunit());

//            if (holder.price.getText().toString().equals("")){
//                holder.price.setText("-");
//            }
//            if (holder.current.getText().toString().equals("")){
//                holder.current.setText("-");
//            }

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
                    ArrayList<Micro_inventorey_listitems> filteredItems = new ArrayList<Micro_inventorey_listitems>();

                    for(int i = 0, l = originalList.size(); i < l; i++)
                    {
                        Micro_inventorey_listitems country = originalList.get(i);
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

                countryList = (ArrayList<Micro_inventorey_listitems>)results.values;
                notifyDataSetChanged();
                clear();
                for(int i = 0, l = countryList.size(); i < l; i++)
                    add(countryList.get(i));
                notifyDataSetInvalidated();
            }
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (player1name.toString().equals("1")){
            finish();
        }else {
            Intent intent = new Intent(Micro_inventory_indent.this, Add_manage_ingredient_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
