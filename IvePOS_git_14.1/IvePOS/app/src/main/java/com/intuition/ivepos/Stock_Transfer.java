package com.intuition.ivepos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Stock_Transfer extends AppCompatActivity {

    public SQLiteDatabase db = null;
    //    MyCustomAdapter dataAdapter, dataadapter;
    ArrayList<Country_items_inventory_indent> countryList = new ArrayList<Country_items_inventory_indent>();

    Cursor cursor;

    ListView list;
    SimpleCursorAdapter ddataAdapterr;


    TextView item_ro, qty_ro, rs_ro;

    String insert1_cc = "", insert1_rs = "", str_country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_transfer);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);

        Cursor cursor_country = db.rawQuery("SELECT * FROM Country_Selection", null);
        if (cursor_country.moveToFirst()){
            str_country = cursor_country.getString(1);
        }

        list = (ListView) findViewById(R.id.list);

//        list.setAdapter(null);


        item_ro = (TextView) findViewById(R.id.item_ro);
        qty_ro = (TextView) findViewById(R.id.qty_ro);
        rs_ro = (TextView) findViewById(R.id.rs_ro);

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
                        if (str_country.toString().equals("Dinar")) {
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

        DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
        downloadMusicfromInternet.execute();

        LinearLayout back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final EditText myFilter = (EditText) findViewById(R.id.searchView);

        ImageView delete_icon = (ImageView) findViewById(R.id.delete_icon);
        delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFilter.setText("");
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final Cursor cursor1 = (Cursor) adapterView.getItemAtPosition(i);
                final String ItemID = cursor1.getString(cursor1.getColumnIndex("itemname"));
                final String id = cursor1.getString(cursor1.getColumnIndex("_id"));
                final String priceget = cursor1.getString(cursor1.getColumnIndex("price"));
                final String indiv_priceget = cursor1.getString(cursor1.getColumnIndex("individual_price"));
                final String status_qty_updated_st = cursor1.getString(cursor1.getColumnIndex("status_qty_updated_st"));
                final String add_qty_st = cursor1.getString(cursor1.getColumnIndex("add_qty_st"));

                final Dialog dialog = new Dialog(Stock_Transfer.this, R.style.timepicker_date_dialog);
                dialog.setContentView(R.layout.dialog_stock_transfer_itemclick);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                final TextView pending = (TextView) dialog.findViewById(R.id.pending);
                pending.setText(ItemID);

                final EditText qtyedit = (EditText) dialog.findViewById(R.id.qtyedit);
                qtyedit.setText(add_qty_st);

                final TextInputLayout qty_edit_layout = (TextInputLayout) dialog.findViewById(R.id.qty_edit_layout);

                ImageView closetext = (ImageView) dialog.findViewById(R.id.closetext);
                closetext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm1.hideSoftInputFromWindow(qtyedit.getWindowToken(), 0);

                        dialog.dismiss();
                    }
                });

                Button cleartext = (Button) dialog.findViewById(R.id.cleartext);

                TextView tv1 = new TextView(Stock_Transfer.this);
                tv1.setText(status_qty_updated_st);
                if (tv1.getText().toString().equals("")){
                    cleartext.setVisibility(View.GONE);
                }else {
                    cleartext.setVisibility(View.VISIBLE);
                }

                cleartext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        qtyedit.setText("");

                        ContentValues cv = new ContentValues();
                        cv.put("add_qty_st", "0");
                        cv.put("status_qty_updated_st", "");
                        String where = "_id = '" + id + "'";
                        db.update("Items", cv, where, new String[]{});

//                        String where1_v1 = "docid = '" + id + "' ";
//                        db.update("Items_Virtual", cv, where1_v1, new String[]{});

                        cursor1.moveToPosition(i);
                        cursor1.requery();
                        ddataAdapterr.notifyDataSetChanged();



                        InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm1.hideSoftInputFromWindow(qtyedit.getWindowToken(), 0);

                        dialog.dismiss();

                        LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);
                        Cursor cd = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated_st = 'Add'", null);
                        if (cd.moveToFirst()){
                            bottom.setVisibility(View.VISIBLE);
                            Cursor cursor_qw1 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated_st = 'Add'", null);
                            int co1 = cursor_qw1.getCount();
                            item_ro.setText(String.valueOf(co1));

                            float co2 = 0;
                            Cursor cursor_qw2 = db.rawQuery("SELECT SUM(add_qty_st) FROM Items WHERE status_qty_updated_st = 'Add'", null);
                            if (cursor_qw2.moveToFirst()){
                                co2 = cursor_qw2.getFloat(0);
                            }
                            qty_ro.setText(String.valueOf(co2));
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
                                        if (str_country.toString().equals("Dinar")) {
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
                });

                LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);
                Cursor cd = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated_st = 'Add'", null);
                if (cd.moveToFirst()){
                    bottom.setVisibility(View.VISIBLE);
                    Cursor cursor_qw1 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated_st = 'Add'", null);
                    int co1 = cursor_qw1.getCount();
                    item_ro.setText(String.valueOf(co1));

                    float co2 = 0;
                    Cursor cursor_qw2 = db.rawQuery("SELECT SUM(add_qty_st) FROM Items WHERE status_qty_updated_st = 'Add'", null);
                    if (cursor_qw2.moveToFirst()){
                        co2 = cursor_qw2.getFloat(0);
                    }
                    qty_ro.setText(String.valueOf(co2));
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
                                if (str_country.toString().equals("Dinar")) {
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

                final Button quantitycontinue = (Button) dialog.findViewById(R.id.quantitycontinue);
                quantitycontinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (qtyedit.getText().toString().equals("") || qtyedit.getText().toString().equals("0")){
                            if (qtyedit.getText().toString().equals("")){
                                qty_edit_layout.setError("Fill quantity");
                            }
                        }else {
                            ContentValues cv = new ContentValues();
                            cv.put("add_qty_st", qtyedit.getText().toString());
                            cv.put("status_qty_updated_st", "Add");
                            String where = "_id = '" + id + "'";
                            db.update("Items", cv, where, new String[]{});

//                            String where1_v1 = "docid = '" + id + "' ";
//                            db.update("Items_Virtual", cv, where1_v1, new String[]{});

                            cursor1.moveToPosition(i);
                            cursor1.requery();
                            ddataAdapterr.notifyDataSetChanged();

                            InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm1.hideSoftInputFromWindow(qtyedit.getWindowToken(), 0);

                            dialog.dismiss();

                            LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);
                            Cursor cd = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated_st = 'Add'", null);
                            if (cd.moveToFirst()){
                                bottom.setVisibility(View.VISIBLE);
                                Cursor cursor_qw1 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated_st = 'Add'", null);
                                int co1 = cursor_qw1.getCount();
                                item_ro.setText(String.valueOf(co1));

                                float co2 = 0;
                                Cursor cursor_qw2 = db.rawQuery("SELECT SUM(add_qty_st) FROM Items WHERE status_qty_updated_st = 'Add'", null);
                                if (cursor_qw2.moveToFirst()){
                                    co2 = cursor_qw2.getFloat(0);
                                }
                                qty_ro.setText(String.valueOf(co2));
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
                                            if (str_country.toString().equals("Dinar")) {
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
                    }
                });

                qtyedit.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_GO) {
                            quantitycontinue.performClick();
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        LinearLayout linearLayout_item_report = (LinearLayout) findViewById(R.id.linearLayout_stock_transfer_report);
        linearLayout_item_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Stock_Transfer.this, Stock_Transfer_Items_History.class);
                startActivity(intent);
            }
        });

    }

    class DownloadMusicfromInternet extends AsyncTask<Void, Integer, Void> {
        private ProgressDialog dialog = new ProgressDialog(Stock_Transfer.this, R.style.timepicker_date_dialog);

        @Override
        protected Void doInBackground(Void... params) {


            Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
            if (cursor.moveToFirst()){
                do {
                    String id = cursor.getString(0);
                    String prese_qty = cursor.getString(3);
                    String min_qty = cursor.getString(20);

                    TextView tv = new TextView(Stock_Transfer.this);
                    tv.setText(min_qty);

                    TextView tv1 = new TextView(Stock_Transfer.this);
                    tv1.setText(prese_qty);

                    if (tv.getText().toString().matches(".*[a-zA-Z]+.*")){
                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("minimum_qty", "0");
                        String where1 = "_id = '"+id+"'";
                        db.update("Items", contentValues1, where1, new String[]{});

//                        String where1_v1 = "docid = '" + id + "' ";
//                        db.update("Items_Virtual", contentValues1, where1_v1, new String[]{});

                    }

                }while (cursor.moveToNext());
            }

            db.execSQL("UPDATE Items SET status_low = ''");
            db.execSQL("UPDATE Items SET minimum_qty_copy = minimum_qty");

            db.execSQL("UPDATE Items SET status_low = 'Low' WHERE minimum_qty = '' OR minimum_qty IS NULL");

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

            cursor = db.rawQuery("SELECT * FROM Items", null);
            String[] fromFieldNames = {"itemname", "barcode_value", "stockquan", "add_qty_st", "status_low", "status_qty_updated_st"};
            int[] toViewsID = {R.id.itemname, R.id.barcode_value, R.id.current_qty, R.id.add_qty, R.id.image, R.id.image1};
            ddataAdapterr = new SimpleCursorAdapter(Stock_Transfer.this, R.layout.stock_transfer_listview, cursor, fromFieldNames, toViewsID, 0);
            list.setAdapter(ddataAdapterr);


            LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);
            Cursor cd = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated_st = 'Add'", null);
            if (cd.moveToFirst()){
                bottom.setVisibility(View.VISIBLE);
                Cursor cursor_qw1 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated_st = 'Add'", null);
                int co1 = cursor_qw1.getCount();
                item_ro.setText(String.valueOf(co1));

                float co2 = 0;
                Cursor cursor_qw2 = db.rawQuery("SELECT SUM(add_qty_st) FROM Items WHERE status_qty_updated_st = 'Add'", null);
                if (cursor_qw2.moveToFirst()){
                    co2 = cursor_qw2.getFloat(0);
                }
                qty_ro.setText(String.valueOf(co2));
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
                            if (str_country.toString().equals("Dinar")) {
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
                    Intent intent = new Intent(Stock_Transfer.this, Stock_Transfer_Processing.class);
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


//            cursor = db.rawQuery("SELECT * FROM Items", null);
//            String[] fromFieldNames = {"itemname", "barcode_value", "stockquan", "add_qty_st", "minimum_qty_copy", "status_low", "status_qty_updated_st"};
//            int[] toViewsID = {R.id.itemname, R.id.barcode_value, R.id.current_qty, R.id.add_qty_st, R.id.min_qty, R.id.image, R.id.image1};
//            ddataAdapterr = new SimpleCursorAdapter(Inventory_Indent.this, R.layout.inventory_indent_listview, cursor, fromFieldNames, toViewsID, 0);
//            list.setAdapter(ddataAdapterr);


        }
    }

    public Cursor fetchCountriesByName1(String inputtext) throws SQLException {

        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
            mCursor = db.rawQuery("SELECT * FROM Items", null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Items WHERE itemname LIKE '%" + inputtext + "%' OR barcode_value LIKE '%" + inputtext + "%'", null);
        }

        return mCursor;
    }

}
