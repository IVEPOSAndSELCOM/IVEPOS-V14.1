package com.intuition.ivepos;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.intuition.ivepos.syncapp.StubProviderApp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Rohithkumar on 9/14/2017.
 */

public class Micro_Inventory_Indent_Processing extends AppCompatActivity {

    public SQLiteDatabase db = null;
    Uri contentUri, resultUri;
    TextView item_ro, billamount, total_amount;

    TextView editText_from_day_visible, editText_from_day_hide, editText_to_day_visible, editText_to_day_hide;
    TextView editText1, editText11, editText2, editText22;

    String onee, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve;
    String onee1, two1, three1, four1, five1, six1, seven1, eight1, nine1, ten1, eleven1, twelve1;

    private int year, year1;
    private int month, month1;
    private int day, day1;


    private int hour;
    private int minute;

    ListView popupSpinner;

    String insert1_cc = "", insert1_rs = "", str_country;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.micro_inventory_indent_process);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);

        Cursor cursor_country = db.rawQuery("SELECT * FROM Country_Selection", null);
        if (cursor_country.moveToFirst()){
            str_country = cursor_country.getString(1);
        }

        TextView inn = (TextView) findViewById(R.id.inn);
        TextView inn1 = (TextView) findViewById(R.id.inn1);
        TextView inn2 = (TextView) findViewById(R.id.inn2);
        TextView inn3 = (TextView) findViewById(R.id.inn3);

        if (str_country.toString().equals("Rupee") || str_country.toString().equals("India")) {
            insert1_cc = "\u20B9";
            insert1_rs = "Rs.";
            inn.setText(insert1_cc);
            inn1.setText(insert1_cc);
            inn2.setText(insert1_cc);
            inn3.setText(insert1_cc);
        }else {
            if (str_country.toString().equals("Pound") || str_country.toString().equals("UK Pound")) {
                insert1_cc = "\u00a3";
                insert1_rs = "BP.";
                inn.setText(insert1_cc);
                inn1.setText(insert1_cc);
                inn2.setText(insert1_cc);
                inn3.setText(insert1_cc);
            }else {
                if (str_country.toString().equals("Euro") || str_country.toString().equals("UK Euro")) {
                    insert1_cc = "\u20ac";
                    insert1_rs = "EU.";
                    inn.setText(insert1_cc);
                    inn1.setText(insert1_cc);
                    inn2.setText(insert1_cc);
                    inn3.setText(insert1_cc);
                }else {
                    if (str_country.toString().equals("Dollar")) {
                        insert1_cc = "\u0024";
                        insert1_rs = "\u0024";
                        inn.setText(insert1_cc);
                        inn1.setText(insert1_cc);
                        inn2.setText(insert1_cc);
                        inn3.setText(insert1_cc);
                    }else {
                        if (str_country.toString().equals("Dinar")) {
                            insert1_cc = "D";
                            insert1_rs = "KD.";
                            inn.setText(insert1_cc);
                            inn1.setText(insert1_cc);
                            inn2.setText(insert1_cc);
                            inn3.setText(insert1_cc);
                        }else {
                            if (str_country.toString().equals("Shilling")) {
                                insert1_cc = "S";
                                insert1_rs = "S.";
                                inn.setText(insert1_cc);
                                inn1.setText(insert1_cc);
                                inn2.setText(insert1_cc);
                                inn3.setText(insert1_cc);
                            }else {
                                if (str_country.toString().equals("Ringitt")) {
                                    insert1_cc = "R";
                                    insert1_rs = "RM.";
                                    inn.setText(insert1_cc);
                                    inn1.setText(insert1_cc);
                                    inn2.setText(insert1_cc);
                                    inn3.setText(insert1_cc);
                                }else {
                                    if (str_country.toString().equals("Rial")) {
                                        insert1_cc = "R";
                                        insert1_rs = "OR.";
                                        inn.setText(insert1_cc);
                                        inn1.setText(insert1_cc);
                                        inn2.setText(insert1_cc);
                                        inn3.setText(insert1_cc);
                                    }else {
                                        if (str_country.toString().equals("Yen")) {
                                            insert1_cc = "\u00a5";
                                            insert1_rs = "\u00a5";
                                            inn.setText(insert1_cc);
                                            inn1.setText(insert1_cc);
                                            inn2.setText(insert1_cc);
                                            inn3.setText(insert1_cc);
                                        }else {
                                            if (str_country.toString().equals("Papua New Guinean")) {
                                                insert1_cc = "K";
                                                insert1_rs = "K.";
                                                inn.setText(insert1_cc);
                                                inn1.setText(insert1_cc);
                                                inn2.setText(insert1_cc);
                                                inn3.setText(insert1_cc);
                                            }else {
                                                if (str_country.toString().equals("UAE")) {
                                                    insert1_cc = "D";
                                                    insert1_rs = "DH.";
                                                    inn.setText(insert1_cc);
                                                    inn1.setText(insert1_cc);
                                                    inn2.setText(insert1_cc);
                                                    inn3.setText(insert1_cc);
                                                }else {
                                                    if (str_country.toString().equals("South African Rand")) {
                                                        insert1_cc = "R";
                                                        insert1_rs = "R.";
                                                        inn.setText(insert1_cc);
                                                        inn1.setText(insert1_cc);
                                                        inn2.setText(insert1_cc);
                                                        inn3.setText(insert1_cc);
                                                    }else {
                                                        if (str_country.toString().equals("Congolese Franc")) {
                                                            insert1_cc = "F";
                                                            insert1_rs = "FC.";
                                                            inn.setText(insert1_cc);
                                                            inn1.setText(insert1_cc);
                                                            inn2.setText(insert1_cc);
                                                            inn3.setText(insert1_cc);
                                                        }else {
                                                            if (str_country.toString().equals("Qatari Riyals")) {
                                                                insert1_cc = "QAR";
                                                                insert1_rs = "QAR.";
                                                                inn.setText(insert1_cc);
                                                                inn1.setText(insert1_cc);
                                                                inn2.setText(insert1_cc);
                                                                inn3.setText(insert1_cc);
                                                            }else {
                                                                if (str_country.toString().equals("Dirhams")) {
                                                                    insert1_cc = "AED";
                                                                    insert1_rs = "AED.";
                                                                    inn.setText(insert1_cc);
                                                                    inn1.setText(insert1_cc);
                                                                    inn2.setText(insert1_cc);
                                                                    inn3.setText(insert1_cc);
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

        Cursor cursor1 = db.rawQuery("SELECT * FROM Taxes", null);
        if (cursor1.moveToFirst()){
            do {
                String id = cursor1.getString(0);
                ContentValues contentValues = new ContentValues();
                contentValues.put("checked", "");
                String where1 = "_id = '" + id + "' ";

                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Taxes");
                getContentResolver().update(contentUri, contentValues, where1, new String[]{});
                resultUri = new Uri.Builder()
                        .scheme("content")
                        .authority(StubProviderApp.AUTHORITY)
                        .path("Taxes")
                        .appendQueryParameter("operation", "update")
                        .appendQueryParameter("_id", id)
                        .build();
                getContentResolver().notifyChange(resultUri, null);

//                db.update("Taxes", contentValues, where1, new String[]{});
            } while (cursor1.moveToNext());
        }


        final Calendar c = Calendar.getInstance();
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Tax_selec");
        getContentResolver().delete(contentUri, null, null);
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("Tax_selec")
                .appendQueryParameter("operation", "delete")
                .appendQueryParameter("1", "1")
                .build();
        getContentResolver().notifyChange(resultUri, null);

//                                    db.delete("Tax_selec", null, null);


        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Discount_selec");
        getContentResolver().delete(contentUri, null, null);
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("Discount_selec")
                .appendQueryParameter("operation", "delete")
                .appendQueryParameter(null, null)
                .build();
        getContentResolver().notifyChange(resultUri, null);

       // db.delete("Discount_selec", null, null);

        ImageView closetext = (ImageView) findViewById(R.id.closetext);
        closetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        item_ro = (TextView) findViewById(R.id.item_ro);

        Cursor cursor_qw1 = db.rawQuery("SELECT * FROM Ingredients WHERE status_qty_updated = 'Add'", null);
        int co1 = cursor_qw1.getCount();
        item_ro.setText(String.valueOf(co1));

        total_amount = (TextView) findViewById(R.id.total_amount);
        billamount = (TextView) findViewById(R.id.billamount);

        float co3 = 0;
        Cursor cursor_qw3 = db.rawQuery("SELECT * FROM Ingredients WHERE status_qty_updated = 'Add'", null);
        if (cursor_qw3.moveToFirst()){
            do {
                String add_q = cursor_qw3.getString(22);
                String ind_p = cursor_qw3.getString(18);

                float co4 = Float.parseFloat(add_q) * Float.parseFloat(ind_p);
                co3 = co3+co4;
            }while (cursor_qw3.moveToNext());
        }
        billamount.setText(String.format("%.1f", co3));


        final TextView percent_tax = (TextView) findViewById(R.id.percent_tax);
        final TextView amount_tax = (TextView) findViewById(R.id.amount_tax);

        final TextView percent_discount = (TextView) findViewById(R.id.percent_discount);
        final TextView amount_discount = (TextView) findViewById(R.id.amount_discount);

        final float one = Float.parseFloat(billamount.getText().toString()) + Float.parseFloat(amount_tax.getText().toString()) - Float.parseFloat(amount_discount.getText().toString());
        total_amount.setText(String.format("%.1f", one));


        RelativeLayout tax_layout = (RelativeLayout) findViewById(R.id.tax_layout);
        tax_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog_tax = new Dialog(Micro_Inventory_Indent_Processing.this, R.style.timepicker_date_dialog);
                dialog_tax.setContentView(R.layout.dialog_tax_inventory_indent);
                dialog_tax.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                dialog_tax.setCanceledOnTouchOutside(false);
                dialog_tax.show();

                TextView inn = (TextView) dialog_tax.findViewById(R.id.inn);
                TextView inn1 = (TextView) dialog_tax.findViewById(R.id.inn1);
                TextView inn2 = (TextView) dialog_tax.findViewById(R.id.inn2);
                inn.setText(insert1_cc);
                inn1.setText(insert1_cc);
                inn2.setText(insert1_cc);

                final TextView bill_amount = (TextView) dialog_tax.findViewById(R.id.bill_amount);
                bill_amount.setText(billamount.getText().toString());

                final TextView taxedit_textview_tax = (TextView) dialog_tax.findViewById(R.id.taxedit_textview_tax);
                final TextView tax_amount_edit_textview_tax = (TextView) dialog_tax.findViewById(R.id.tax_amount_edit_textview_tax);

                final EditText taxedit_edittext = (EditText) dialog_tax.findViewById(R.id.taxedit_edittext);
                final TextView tax_amount_edit_textview = (TextView) dialog_tax.findViewById(R.id.tax_amount_edit_textview);

                final EditText tax_amount_edit_edittext = (EditText) dialog_tax.findViewById(R.id.tax_amount_edit_edittext);
                final TextView taxedit_textview = (TextView) dialog_tax.findViewById(R.id.taxedit_textview);

                final TextInputLayout taxedit_layout = (TextInputLayout) dialog_tax.findViewById(R.id.taxedit_layout);
                final TextInputLayout tax_amount_edit_layout = (TextInputLayout) dialog_tax.findViewById(R.id.tax_amount_edit_layout);

                final LinearLayout taxes = (LinearLayout) dialog_tax.findViewById(R.id.taxes);
                final LinearLayout bypercent = (LinearLayout) dialog_tax.findViewById(R.id.bypercent);
                final LinearLayout byamount = (LinearLayout) dialog_tax.findViewById(R.id.byamount);

                final RadioGroup radioGroupSplit = (RadioGroup) dialog_tax.findViewById(R.id.splitgroup);

                final RadioButton twoWay = (RadioButton) dialog_tax.findViewById(R.id.btntwo);
                final RadioButton threeWay = (RadioButton) dialog_tax.findViewById(R.id.btnthree);
                final RadioButton fourWay = (RadioButton) dialog_tax.findViewById(R.id.btnfour);

                final ListView tax_list = (ListView) dialog_tax.findViewById(R.id.tax_list);

                radioGroupSplit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        final int selected1 = radioGroupSplit.getCheckedRadioButtonId();

                        if (selected1 == twoWay.getId()) {
//                            Toast.makeText(Inventory_Indent_Processing.this, "1 ", Toast.LENGTH_SHORT).show();
//                            displaysplit2(dialogspl);
//                            donotdisplaysplit3(dialogspl);
                            taxes.setVisibility(View.VISIBLE);
                            bypercent.setVisibility(View.GONE);
                            byamount.setVisibility(View.GONE);

                            taxedit_edittext.setText("");
                            tax_amount_edit_textview.setText("");

                            tax_amount_edit_edittext.setText("");
                            taxedit_textview.setText("");



                            Cursor cursor1 = db.rawQuery("SELECT * FROM Taxes", null);
                            if (cursor1.moveToFirst()) {
                                taxes.setVisibility(View.VISIBLE);
                                Cursor cursor = db.rawQuery("SELECT * FROM Taxes", null);
                                String[] fromFieldNames = {"taxname", "value"};
                                int[] toViewsID = {R.id.label, R.id.value};
                                BaseAdapter ddataAdapterr = new ImageCursorAdapter_Indent_tax(Micro_Inventory_Indent_Processing.this, R.layout.dialof_listview_taxlist, cursor, fromFieldNames, toViewsID);
                                tax_list.setAdapter(ddataAdapterr);
                            }else {
                                taxes.setVisibility(View.GONE);
                            }

                            final float[] val = {0};

                            tax_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    CheckBox cb = (CheckBox) view.findViewById(R.id.check);

                                    if (cb.isChecked()){
                                        cb.setChecked(false);
                                        TextView tax_value = (TextView) view.findViewById(R.id.value);
                                        TextView label = (TextView) view.findViewById(R.id.label);
                                        val[0] = val[0] - Float.parseFloat(tax_value.getText().toString());
                                        Cursor cursor1 = db.rawQuery("SELECT * FROM Taxes WHERE taxname = '"+label.getText().toString()+"'", null);
                                        if (cursor1.moveToFirst()){
                                            String id = cursor1.getString(0);
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("checked", "");
                                            String where1 = "_id = '" + id + "' ";

                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Taxes");
                                            getContentResolver().update(contentUri, contentValues, where1, new String[]{});
                                            resultUri = new Uri.Builder()
                                                    .scheme("content")
                                                    .authority(StubProviderApp.AUTHORITY)
                                                    .path("Taxes")
                                                    .appendQueryParameter("operation", "update")
                                                    .appendQueryParameter("_id", id)
                                                    .build();
                                            getContentResolver().notifyChange(resultUri, null);
//                                            db.update("Taxes", contentValues, where1, new String[]{});
                                        }
                                        cursor1.close();
                                    }else {
                                        cb.setChecked(true);
                                        TextView label = (TextView) view.findViewById(R.id.label);
                                        TextView tax_value = (TextView) view.findViewById(R.id.value);
                                        val[0] = val[0] + Float.parseFloat(tax_value.getText().toString());
                                        Cursor cursor1 = db.rawQuery("SELECT * FROM Taxes WHERE taxname = '"+label.getText().toString()+"'", null);
                                        if (cursor1.moveToFirst()){
                                            String id = cursor1.getString(0);
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("checked", "checked");
                                            String where1 = "_id = '" + id + "' ";

                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Taxes");
                                            getContentResolver().update(contentUri, contentValues, where1, new String[]{});
                                            resultUri = new Uri.Builder()
                                                    .scheme("content")
                                                    .authority(StubProviderApp.AUTHORITY)
                                                    .path("Taxes")
                                                    .appendQueryParameter("operation", "update")
                                                    .appendQueryParameter("_id", id)
                                                    .build();
                                            getContentResolver().notifyChange(resultUri, null);

//                                            db.update("Taxes", contentValues, where1, new String[]{});
                                        }
//                                        Toast.makeText(Inventory_Indent_Processing.this, "2 "+val[0], Toast.LENGTH_LONG).show();
                                    }

//                                    Toast.makeText(Inventory_Indent_Processing.this, "3 "+val[0], Toast.LENGTH_LONG).show();

                                    taxedit_textview_tax.setText(String.valueOf(val[0]));
                                    float one = (Float.parseFloat(billamount.getText().toString()) / 100) * Float.parseFloat(taxedit_textview_tax.getText().toString());
                                    tax_amount_edit_textview_tax.setText(String.valueOf(one));

                                }
                            });

                        }

                        if (selected1 == threeWay.getId()){
//                            Toast.makeText(Inventory_Indent_Processing.this, "2 ", Toast.LENGTH_SHORT).show();
                            taxes.setVisibility(View.GONE);
                            bypercent.setVisibility(View.VISIBLE);
                            byamount.setVisibility(View.GONE);

                            taxedit_textview_tax.setText("");
                            tax_amount_edit_textview_tax.setText("");

                            tax_amount_edit_edittext.setText("");
                            taxedit_textview.setText("");
                        }

                        if (selected1 == fourWay.getId()){
//                            Toast.makeText(Inventory_Indent_Processing.this, "3 ", Toast.LENGTH_SHORT).show();
                            taxes.setVisibility(View.GONE);
                            bypercent.setVisibility(View.GONE);
                            byamount.setVisibility(View.VISIBLE);

                            taxedit_textview_tax.setText("");
                            tax_amount_edit_textview_tax.setText("");

                            taxedit_edittext.setText("");
                            tax_amount_edit_textview.setText("");
                        }
                    }
                });

                taxedit_edittext.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        ddataAdapterr.getFilter().filter(s.toString());

                        if (taxedit_edittext.getText().toString().equals("") || taxedit_edittext.getText().toString().equals("0")){
                            tax_amount_edit_textview.setText("0");
                        }else {
                            float one = (Float.parseFloat(billamount.getText().toString()) / 100) * Float.parseFloat(taxedit_edittext.getText().toString());
                            tax_amount_edit_textview.setText(String.valueOf(one));
                        }
                    }
                });

                tax_amount_edit_edittext.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        ddataAdapterr.getFilter().filter(s.toString());
                        if (tax_amount_edit_edittext.getText().toString().equals("") || tax_amount_edit_edittext.getText().toString().equals("0")){
                            taxedit_textview.setText("0");
                        }else {
                            float one = (Float.parseFloat(tax_amount_edit_edittext.getText().toString()) * 100) / Float.parseFloat(billamount.getText().toString());
                            taxedit_textview.setText(String.valueOf(one));
                        }
                    }
                });


                ImageView closetext = (ImageView) dialog_tax.findViewById(R.id.closetext);
                closetext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_tax.dismiss();
                    }
                });


                Button quantitycontinue = (Button) dialog_tax.findViewById(R.id.quantitycontinue);
                quantitycontinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(Inventory_Indent_Processing.this, "1", Toast.LENGTH_LONG).show();
                        View v;
                        if (twoWay.isChecked()){
//                            for(int i=0; i < tax_list.getCount(); i++){
//                                Toast.makeText(Inventory_Indent_Processing.this, "3", Toast.LENGTH_LONG).show();
//                                v = tax_list.getAdapter().getView(i, null, null);
//                                CheckBox cb = (CheckBox)v.findViewById(R.id.check);
//                                TextView label = (TextView) v.findViewById(R.id.label);
//                                Toast.makeText(Inventory_Indent_Processing.this, "4 "+label.getText().toString(), Toast.LENGTH_LONG).show();
//                                if(cb.isChecked()){
//                                    Toast.makeText(Inventory_Indent_Processing.this, "qw "+label.getText().toString(), Toast.LENGTH_SHORT).show();
//                                }
//                            }

                            if (tax_amount_edit_textview_tax.getText().toString().equals("")){
//                                Toast.makeText(Inventory_Indent_Processing.this, "Select any value", Toast.LENGTH_SHORT).show();
                            } else {

                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Tax_selec");
                                getContentResolver().delete(contentUri, null, null);
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProviderApp.AUTHORITY)
                                        .path("Tax_selec")
                                        .appendQueryParameter("operation", "delete")
                                        .appendQueryParameter(null, null)
                                        .build();
                                getContentResolver().notifyChange(resultUri, null);

//                                    db.delete("Tax_selec", null, null);



                                Cursor cursor = db.rawQuery("SELECT * FROM Taxes WHERE checked = 'checked'", null);
                                if (cursor.moveToFirst()) {
                                    do {
                                        String id = cursor.getString(0);

                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("Checked", "checked_s");
                                        String where = "_id = '" + id + "'";

                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Taxes");
                                        getContentResolver().update(contentUri, contentValues, where, new String[]{});
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProviderApp.AUTHORITY)
                                                .path("Taxes")
                                                .appendQueryParameter("operation", "update")
                                                .appendQueryParameter("_id", id)
                                                .build();
                                        getContentResolver().notifyChange(resultUri, null);

//                                        db.update("Taxes", contentValues, where, new String[]{});
                                    } while (cursor.moveToNext());
                                }

                                ContentValues contentValues = new ContentValues();
                                contentValues.put("selected_but", "Default");
                                contentValues.put("tax_amount", tax_amount_edit_textview_tax.getText().toString());
                                contentValues.put("tax_per", taxedit_textview_tax.getText().toString());

                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Tax_selec");
                                resultUri = getContentResolver().insert(contentUri, contentValues);
                                getContentResolver().notifyChange(resultUri, null);

//                                db.insert("Tax_selec", null, contentValues);
                                dialog_tax.dismiss();

                                percent_tax.setText(taxedit_textview_tax.getText().toString());
                                amount_tax.setText(tax_amount_edit_textview_tax.getText().toString());

                                float one = Float.parseFloat(billamount.getText().toString()) + Float.parseFloat(amount_tax.getText().toString()) - Float.parseFloat(amount_discount.getText().toString());
                                total_amount.setText(String.format("%.1f", one));

                            }
                        }
                        if (threeWay.isChecked()){

                            if (taxedit_edittext.getText().toString().equals("")){
                                taxedit_edittext.setError("Enter valid number");
                            } else {

                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Tax_selec");
                                getContentResolver().delete(contentUri, null, null);
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProviderApp.AUTHORITY)
                                        .path("Tax_selec")
                                        .appendQueryParameter("operation", "delete")
                                        .appendQueryParameter(null, null)
                                        .build();
                                getContentResolver().notifyChange(resultUri, null);

//                                    db.delete("Tax_selec", null, null);


                                ContentValues contentValues = new ContentValues();
                                contentValues.put("selected_but", "By %");
                                contentValues.put("tax_amount", tax_amount_edit_textview.getText().toString());
                                contentValues.put("tax_per", taxedit_edittext.getText().toString());


                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Tax_selec");
                                resultUri = getContentResolver().insert(contentUri, contentValues);
                                getContentResolver().notifyChange(resultUri, null);

//                                db.insert("Tax_selec", null, contentValues);
                                dialog_tax.dismiss();

                                percent_tax.setText(taxedit_edittext.getText().toString());
                                amount_tax.setText(tax_amount_edit_textview.getText().toString());

                                float one = Float.parseFloat(billamount.getText().toString()) + Float.parseFloat(amount_tax.getText().toString()) - Float.parseFloat(amount_discount.getText().toString());
                                total_amount.setText(String.format("%.1f", one));

                            }

                        }
                        if (fourWay.isChecked()){
                            if (tax_amount_edit_edittext.getText().toString().equals("")){
                                tax_amount_edit_edittext.setError("Enter valid number");
                            } else {

                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Tax_selec");
                                getContentResolver().delete(contentUri, null, null);
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProviderApp.AUTHORITY)
                                        .path("Tax_selec")
                                        .appendQueryParameter("operation", "delete")
                                        .appendQueryParameter(null, null)
                                        .build();
                                getContentResolver().notifyChange(resultUri, null);

//                                db.delete("Tax_selec", null, null);
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("selected_but", "By amount");
                                contentValues.put("tax_amount", tax_amount_edit_edittext.getText().toString());
                                contentValues.put("tax_per", taxedit_textview.getText().toString());

                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Tax_selec");
                                resultUri = getContentResolver().insert(contentUri, contentValues);
                                getContentResolver().notifyChange(resultUri, null);

//                                db.insert("Tax_selec", null, contentValues);
                                dialog_tax.dismiss();

                                percent_tax.setText(taxedit_textview.getText().toString());
                                amount_tax.setText(tax_amount_edit_edittext.getText().toString());

                                float one = Float.parseFloat(billamount.getText().toString()) + Float.parseFloat(amount_tax.getText().toString()) - Float.parseFloat(amount_discount.getText().toString());
                                total_amount.setText(String.format("%.1f", one));

                            }
                        }
                    }
                });

                Cursor cursor2 = db.rawQuery("SELECT * FROM Tax_selec", null);
                if (cursor2.moveToFirst()){
                    String selected_but = cursor2.getString(3);
                    String tax_amount = cursor2.getString(1);
                    String tax_per = cursor2.getString(2);

                    if (selected_but.toString().equals("Default")){
                        twoWay.setChecked(true);

                        tax_amount_edit_textview_tax.setText(tax_amount);
                        taxedit_textview_tax.setText(tax_per);

                        Cursor cursor1 = db.rawQuery("SELECT * FROM Taxes", null);
                        if (cursor1.moveToFirst()) {
                            taxes.setVisibility(View.VISIBLE);
                            Cursor cursor = db.rawQuery("SELECT * FROM Taxes", null);
                            String[] fromFieldNames = {"taxname", "value"};
                            int[] toViewsID = {R.id.label, R.id.value};
                            BaseAdapter ddataAdapterr = new ImageCursorAdapter_Indent_tax(Micro_Inventory_Indent_Processing.this, R.layout.dialof_listview_taxlist, cursor, fromFieldNames, toViewsID);
                            tax_list.setAdapter(ddataAdapterr);
                        }else {
                            taxes.setVisibility(View.GONE);
                        }

                        final float[] val = {Float.parseFloat(tax_per)};

                        tax_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                CheckBox cb = (CheckBox) view.findViewById(R.id.check);

                                if (cb.isChecked()){
                                    cb.setChecked(false);
                                    TextView tax_value = (TextView) view.findViewById(R.id.value);
                                    TextView label = (TextView) view.findViewById(R.id.label);
                                    val[0] = val[0] - Float.parseFloat(tax_value.getText().toString());
                                    Cursor cursor1 = db.rawQuery("SELECT * FROM Taxes WHERE taxname = '"+label.getText().toString()+"'", null);
                                    if (cursor1.moveToFirst()){
                                        String id = cursor1.getString(0);
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("checked", "");
                                        String where1 = "_id = '" + id + "' ";

                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Taxes");
                                        getContentResolver().update(contentUri, contentValues, where1, new String[]{});
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProviderApp.AUTHORITY)
                                                .path("Taxes")
                                                .appendQueryParameter("operation", "update")
                                                .appendQueryParameter("_id", id)
                                                .build();
                                        getContentResolver().notifyChange(resultUri, null);
//                                        db.update("Taxes", contentValues, where1, new String[]{});
                                    }
                                    cursor1.close();
                                }else {
                                    cb.setChecked(true);
                                    TextView label = (TextView) view.findViewById(R.id.label);
                                    TextView tax_value = (TextView) view.findViewById(R.id.value);
                                    val[0] = val[0] + Float.parseFloat(tax_value.getText().toString());
                                    Cursor cursor1 = db.rawQuery("SELECT * FROM Taxes WHERE taxname = '"+label.getText().toString()+"'", null);
                                    if (cursor1.moveToFirst()){
                                        String id = cursor1.getString(0);
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("checked", "checked");
                                        String where1 = "_id = '" + id + "' ";

                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Taxes");
                                        getContentResolver().update(contentUri, contentValues, where1, new String[]{});
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProviderApp.AUTHORITY)
                                                .path("Taxes")
                                                .appendQueryParameter("operation", "update")
                                                .appendQueryParameter("_id", id)
                                                .build();
                                        getContentResolver().notifyChange(resultUri, null);
//                                        db.update("Taxes", contentValues, where1, new String[]{});
                                    }
//                                        Toast.makeText(Inventory_Indent_Processing.this, "2 "+val[0], Toast.LENGTH_LONG).show();
                                }

//                                    Toast.makeText(Inventory_Indent_Processing.this, "3 "+val[0], Toast.LENGTH_LONG).show();

                                taxedit_textview_tax.setText(String.valueOf(val[0]));
                                float one = (Float.parseFloat(billamount.getText().toString()) / 100) * Float.parseFloat(taxedit_textview_tax.getText().toString());
                                tax_amount_edit_textview_tax.setText(String.valueOf(one));

                            }
                        });

                    }
                    if (selected_but.toString().equals("By %")){
                        threeWay.setChecked(true);

                        taxedit_edittext.setText(tax_per);
                        tax_amount_edit_textview.setText(tax_amount);
                    }
                    if (selected_but.toString().equals("By amount")){
                        fourWay.setChecked(true);

                        taxedit_textview.setText(tax_per);
                        tax_amount_edit_edittext.setText(tax_amount);
                    }
                }

                Button cleartext = (Button) dialog_tax.findViewById(R.id.cleartext);
                cleartext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_tax.dismiss();

                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Tax_selec");
                        getContentResolver().delete(contentUri, null, null);
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("Tax_selec")
                                .appendQueryParameter("operation", "delete")
                                .appendQueryParameter(null, null)
                                .build();
                        getContentResolver().notifyChange(resultUri, null);
//                        db.delete("Tax_selec", null, null);

                        taxedit_textview.setText("");
                        tax_amount_edit_edittext.setText("");

                        percent_tax.setText("0");
                        amount_tax.setText("0");

                        float one = Float.parseFloat(billamount.getText().toString()) + Float.parseFloat(amount_tax.getText().toString()) - Float.parseFloat(amount_discount.getText().toString());
                        total_amount.setText(String.format("%.1f", one));

                    }
                });


            }
        });

        RelativeLayout discount_layout = (RelativeLayout) findViewById(R.id.discount_layout);
        discount_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog_discount = new Dialog(Micro_Inventory_Indent_Processing.this, R.style.timepicker_date_dialog);
                dialog_discount.setContentView(R.layout.dialog_discount_inventory_indent);
                dialog_discount.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                dialog_discount.setCanceledOnTouchOutside(false);
                dialog_discount.show();

                TextView inn = (TextView) dialog_discount.findViewById(R.id.inn);
                TextView inn1 = (TextView) dialog_discount.findViewById(R.id.inn1);
                TextView inn2 = (TextView) dialog_discount.findViewById(R.id.inn2);
                inn.setText(insert1_cc);
                inn1.setText(insert1_cc);
                inn2.setText(insert1_cc);

                final TextView bill_amount = (TextView) dialog_discount.findViewById(R.id.bill_amount);
                bill_amount.setText(billamount.getText().toString());

                final EditText discountedit_edittext = (EditText) dialog_discount.findViewById(R.id.discountedit_edittext);
                final TextView discount_amount_edit_textview = (TextView) dialog_discount.findViewById(R.id.discount_amount_edit_textview);

                final EditText discount_amount_edit_edittext = (EditText) dialog_discount.findViewById(R.id.discount_amount_edit_edittext);
                final TextView discountedit_textview = (TextView) dialog_discount.findViewById(R.id.discountedit_textview);

                final TextInputLayout discountedit_layout = (TextInputLayout) dialog_discount.findViewById(R.id.discountedit_layout);
                final TextInputLayout discount_amount_edit_layout = (TextInputLayout) dialog_discount.findViewById(R.id.discount_amount_edit_layout);

                final LinearLayout bypercent = (LinearLayout) dialog_discount.findViewById(R.id.bypercent);
                final LinearLayout byamount = (LinearLayout) dialog_discount.findViewById(R.id.byamount);

                final RadioGroup radioGroupSplit = (RadioGroup) dialog_discount.findViewById(R.id.splitgroup);

                final RadioButton threeWay = (RadioButton) dialog_discount.findViewById(R.id.btnthree);
                final RadioButton fourWay = (RadioButton) dialog_discount.findViewById(R.id.btnfour);

                radioGroupSplit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        final int selected1 = radioGroupSplit.getCheckedRadioButtonId();


                        if (selected1 == threeWay.getId()){
//                            Toast.makeText(Inventory_Indent_Processing.this, "2 ", Toast.LENGTH_SHORT).show();
                            bypercent.setVisibility(View.VISIBLE);
                            byamount.setVisibility(View.GONE);

                            discount_amount_edit_edittext.setText("");
                            discountedit_textview.setText("");
                        }

                        if (selected1 == fourWay.getId()){
//                            Toast.makeText(Inventory_Indent_Processing.this, "3 ", Toast.LENGTH_SHORT).show();
                            bypercent.setVisibility(View.GONE);
                            byamount.setVisibility(View.VISIBLE);

                            discountedit_edittext.setText("");
                            discount_amount_edit_textview.setText("");
                        }
                    }
                });

                discountedit_edittext.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        ddataAdapterr.getFilter().filter(s.toString());

                        if (discountedit_edittext.getText().toString().equals("") || discountedit_edittext.getText().toString().equals("0")){
                            discount_amount_edit_textview.setText("0");
                        }else {
                            float one = (Float.parseFloat(billamount.getText().toString()) / 100) * Float.parseFloat(discountedit_edittext.getText().toString());
                            discount_amount_edit_textview.setText(String.valueOf(one));
                        }
                    }
                });

                discount_amount_edit_edittext.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        ddataAdapterr.getFilter().filter(s.toString());
                        if (discount_amount_edit_edittext.getText().toString().equals("") || discount_amount_edit_edittext.getText().toString().equals("0")){
                            discountedit_textview.setText("0");
                        }else {
                            float one = (Float.parseFloat(discount_amount_edit_edittext.getText().toString()) * 100) / Float.parseFloat(billamount.getText().toString());
                            discountedit_textview.setText(String.valueOf(one));
                        }
                    }
                });


                ImageView closetext = (ImageView) dialog_discount.findViewById(R.id.closetext);
                closetext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_discount.dismiss();
                    }
                });

                Button quantitycontinue = (Button) dialog_discount.findViewById(R.id.quantitycontinue);
                quantitycontinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(Inventory_Indent_Processing.this, "1", Toast.LENGTH_LONG).show();
                        View v;

                        if (threeWay.isChecked()){

                            if (discountedit_edittext.getText().toString().equals("")){
                                discountedit_edittext.setError("Enter valid number");
                            } else {

                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Discount_selec");
                                getContentResolver().delete(contentUri, null, null);
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProviderApp.AUTHORITY)
                                        .path("Discount_selec")
                                        .appendQueryParameter("operation", "delete")
                                        .appendQueryParameter(null, null)
                                        .build();
                                getContentResolver().notifyChange(resultUri, null);
//                                db.delete("Discount_selec", null, null);
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("selected_but", "By %");
                                contentValues.put("discount_amount", discount_amount_edit_textview.getText().toString());
                                contentValues.put("discount_per", discountedit_edittext.getText().toString());
                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Discount_selec");
                                resultUri = getContentResolver().insert(contentUri, contentValues);
                                getContentResolver().notifyChange(resultUri, null);
//                                db.insert("Discount_selec", null, contentValues);
                                dialog_discount.dismiss();

                                percent_discount.setText(discountedit_edittext.getText().toString());
                                amount_discount.setText(discount_amount_edit_textview.getText().toString());

                                float one = Float.parseFloat(billamount.getText().toString()) + Float.parseFloat(amount_tax.getText().toString()) - Float.parseFloat(amount_discount.getText().toString());
                                total_amount.setText(String.format("%.1f", one));

                            }

                        }
                        if (fourWay.isChecked()){
                            if (discount_amount_edit_edittext.getText().toString().equals("")){
                                discount_amount_edit_edittext.setError("Enter valid number");
                            } else {
                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Discount_selec");
                                getContentResolver().delete(contentUri, null, null);
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProviderApp.AUTHORITY)
                                        .path("Discount_selec")
                                        .appendQueryParameter("operation", "delete")
                                        .appendQueryParameter(null, null)
                                        .build();
                                getContentResolver().notifyChange(resultUri, null);
//                                db.delete("Discount_selec", null, null);
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("selected_but", "By amount");
                                contentValues.put("discount_amount", discount_amount_edit_edittext.getText().toString());
                                contentValues.put("discount_per", discountedit_textview.getText().toString());

                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Discount_selec");
                                resultUri = getContentResolver().insert(contentUri, contentValues);
                                getContentResolver().notifyChange(resultUri, null);
//                                db.insert("Discount_selec", null, contentValues);
                                dialog_discount.dismiss();

                                percent_discount.setText(discountedit_textview.getText().toString());
                                amount_discount.setText(discount_amount_edit_edittext.getText().toString());


                                float one = Float.parseFloat(billamount.getText().toString()) + Float.parseFloat(amount_tax.getText().toString()) - Float.parseFloat(amount_discount.getText().toString());
                                total_amount.setText(String.format("%.1f", one));

                            }
                        }

                    }
                });


                Cursor cursor2 = db.rawQuery("SELECT * FROM Discount_selec", null);
                if (cursor2.moveToFirst()){
                    String selected_but = cursor2.getString(3);
                    String discount_amount = cursor2.getString(1);
                    String discount_per = cursor2.getString(2);

                    if (selected_but.toString().equals("By %")){
                        threeWay.setChecked(true);

                        discountedit_edittext.setText(discount_per);
                        discount_amount_edit_textview.setText(discount_amount);
                    }
                    if (selected_but.toString().equals("By amount")){
                        fourWay.setChecked(true);

                        discountedit_textview.setText(discount_per);
                        discount_amount_edit_edittext.setText(discount_amount);
                    }
                }


                Button cleartext = (Button) dialog_discount.findViewById(R.id.cleartext);
                cleartext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_discount.dismiss();

                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Discount_selec");
                        getContentResolver().delete(contentUri, null, null);
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("Discount_selec")
                                .appendQueryParameter("operation", "delete")
                                .appendQueryParameter(null, null)
                                .build();
                        getContentResolver().notifyChange(resultUri, null);
//                        db.delete("Discount_selec", null, null);

                        discountedit_textview.setText("");
                        discount_amount_edit_edittext.setText("");

                        percent_discount.setText("0");
                        amount_discount.setText("0");

                        float one = Float.parseFloat(billamount.getText().toString()) + Float.parseFloat(amount_tax.getText().toString()) - Float.parseFloat(amount_discount.getText().toString());
                        total_amount.setText(String.format("%.1f", one));
                    }
                });

            }
        });


        ListView items_list = (ListView) findViewById(R.id.items_list);

        final Cursor cursor = db.rawQuery("SELECT * FROM Ingredients WHERE status_qty_updated = 'Add'", null);
        String[] fromFieldNames = {"ingredient_name", "add_qty", "indiv_price_temp", "unit"};
        int[] toViewsID = {R.id.itemname, R.id.qty, R.id.indiv_price, R.id.unit};
        BaseAdapter ddataAdapterr = new ImageCursorAdapter_Micro_Inventory_Itemslist(Micro_Inventory_Indent_Processing.this, R.layout.dialog_micro_inventory_indent_itemslist, cursor, fromFieldNames, toViewsID);
        items_list.setAdapter(ddataAdapterr);

        Button quantitycontinue = (Button) findViewById(R.id.quantitycontinue);
        quantitycontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog_vendor = new Dialog(Micro_Inventory_Indent_Processing.this, R.style.timepicker_date_dialog);
                dialog_vendor.setContentView(R.layout.dialog_inventory_intent_reports_listitem_edit_add);
                dialog_vendor.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                dialog_vendor.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog_vendor.setCanceledOnTouchOutside(false);
                dialog_vendor.show();

                TextView bill_totoal_rs = (TextView) dialog_vendor.findViewById(R.id.bill_totoal_rs);
                TextView inn = (TextView) dialog_vendor.findViewById(R.id.inn);
                TextView inn1 = (TextView) dialog_vendor.findViewById(R.id.inn1);
                TextView inn2 = (TextView) dialog_vendor.findViewById(R.id.inn2);
                TextView inn3 = (TextView) dialog_vendor.findViewById(R.id.inn3);
                bill_totoal_rs.setText(insert1_cc);
                inn.setText(insert1_cc);
                inn1.setText(insert1_cc);
                inn2.setText(insert1_cc);
                inn3.setText(insert1_cc);

                ImageButton canceledit = (ImageButton) dialog_vendor.findViewById(R.id.canceledit);
                canceledit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_vendor.dismiss();
                    }
                });


                final TextView dialog_billamount = (TextView) dialog_vendor.findViewById(R.id.dialog_billamount);
                dialog_billamount.setText(billamount.getText().toString());

                final TextView dialog_percent_tax = (TextView) dialog_vendor.findViewById(R.id.dialog_percent_tax);
                dialog_percent_tax.setText(percent_tax.getText().toString());

                final TextView dialog_amount_tax = (TextView) dialog_vendor.findViewById(R.id.dialog_amount_tax);
                dialog_amount_tax.setText(amount_tax.getText().toString());

                final TextView dialog_percent_disc = (TextView) dialog_vendor.findViewById(R.id.dialog_percent_disc);
                dialog_percent_disc.setText(percent_discount.getText().toString());

                final TextView dialog_amount_discount = (TextView) dialog_vendor.findViewById(R.id.dialog_amount_discount);
                dialog_amount_discount.setText(amount_discount.getText().toString());

                final TextView no_of_items = (TextView) dialog_vendor.findViewById(R.id.no_of_items);
                no_of_items.setText(item_ro.getText().toString());

                final TextView bill_totoal_amount = (TextView) dialog_vendor.findViewById(R.id.bill_totoal_amount);
                bill_totoal_amount.setText(total_amount.getText().toString());

                final EditText pay_amount = (EditText) dialog_vendor.findViewById(R.id.pay_amount);

                final LinearLayout total_pane = (LinearLayout) dialog_vendor.findViewById(R.id.total_pane);

                final ImageView image = (ImageView) dialog_vendor.findViewById(R.id.image);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (total_pane.getVisibility() == View.VISIBLE) {
                            image.setRotation(180);
                            total_pane.setVisibility(View.GONE);
                        } else {
                            image.setRotation(360);
                            total_pane.setVisibility(View.VISIBLE);
                        }
                    }
                });


                final TextView invoice_no = (TextView) dialog_vendor.findViewById(R.id.invoice_no);

                final TextView vend_name = (TextView) dialog_vendor.findViewById(R.id.vend_name);
                final TextView vend_phoneno = (TextView) dialog_vendor.findViewById(R.id.vend_phoneno);
                final TextView vend_email = (TextView) dialog_vendor.findViewById(R.id.vend_email);
                final TextView vend_address = (TextView) dialog_vendor.findViewById(R.id.vend_address);
                final TextView vend_gstin = (TextView) dialog_vendor.findViewById(R.id.vend_gstin);

                final LinearLayout vendor_det_lay = (LinearLayout) dialog_vendor.findViewById(R.id.vendor_det_lay);

                vend_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog1 = new Dialog(Micro_Inventory_Indent_Processing.this, R.style.notitle);
                        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialog1.setContentView(R.layout.dialog_vendor_list);

                        popupSpinner = (ListView) dialog1.findViewById(R.id.listView5);
                        ArrayList<String> my_arrayy = getTableValuesall();
                        final ArrayAdapter my_Adapterr = new ArrayAdapter(Micro_Inventory_Indent_Processing.this, R.layout.spinner_row,
                                my_arrayy);
                        popupSpinner.setAdapter(my_Adapterr);

                        EditText myFilter1 = (EditText) dialog1.findViewById(R.id.searchView);
                        myFilter1.addTextChangedListener(new TextWatcher() {

                            public void afterTextChanged(Editable s) {
                            }

                            public void beforeTextChanged(CharSequence s, int start,
                                                          int count, int after) {
                            }

                            public void onTextChanged(CharSequence s, int start,
                                                      int before, int count) {
                                my_Adapterr.getFilter().filter(s.toString());
                            }
                        });


                        LinearLayout cancelletter1 = (LinearLayout) dialog1.findViewById(R.id.custombar_return_wrapper);
                        cancelletter1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                dialog1.dismiss();
                            }
                        });


                        //selectionCurrent = String.valueOf(popupSpinner.getSelectedItemPosition());

                        popupSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String selectedSweet = popupSpinner.getItemAtPosition(position).toString();

                                if (selectedSweet.toString().contains(" - ")){
                                    selectedSweet = selectedSweet.replace(" - ", "");
                                }

                                Cursor cursor2 = db.rawQuery("SELECT * FROM Vendor_details_micro", null);
                                if (cursor2.moveToFirst()){
                                    do {
                                        String name = cursor2.getString(1);
                                        String ph = cursor2.getString(2);

                                        String name_ph = name+""+ph;

                                        if (name_ph.toString().equals(selectedSweet)){
                                            vend_name.setText(name);
                                        }
                                    }while (cursor2.moveToNext());
                                }
                                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                dialog1.dismiss();
                                //String text = dialogC4_id.getText().toString();
                                //Toast.makeText(getActivity(), "Selected item: " + selectedSweet + " - " + position, Toast.LENGTH_SHORT).show();

                                Cursor cursor3 = db.rawQuery("SELECT * FROM Vendor_details_micro", null);
                                if (cursor3.moveToFirst()){
                                    do {
                                        String name = cursor3.getString(1);
                                        String ph = cursor3.getString(2);
                                        String ema = cursor3.getString(3);
                                        String add = cursor3.getString(4);
                                        String gst = cursor3.getString(5);

                                        String name_ph = name+""+ph;

                                        if (name_ph.toString().equals(selectedSweet)){
                                            vend_phoneno.setText(ph);
                                            vend_email.setText(ema);
                                            vend_address.setText(add);
                                            vend_gstin.setText(gst);
                                            vendor_det_lay.setVisibility(View.VISIBLE);
                                        }
                                    }while (cursor3.moveToNext());
                                }
                            }
                        });

                        dialog1.show();

                    }
                });


                Button okedit_delete = (Button) dialog_vendor.findViewById(R.id.okedit_delete);
                okedit_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        vend_name.setText("");
                        vendor_det_lay.setVisibility(View.GONE);
                        invoice_no.setText("");
                    }
                });


                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                final String currentDateandTime1 = sdf2.format(new Date());

                SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM yyyy");
                final String currentDateandTime2 = sdf3.format(new Date());


                editText1 = (TextView) dialog_vendor.findViewById(R.id.editText1);
                editText1.setText(currentDateandTime1);

                editText11 = (TextView) dialog_vendor.findViewById(R.id.editText11);
                editText11.setText(currentDateandTime2);


                editText2 = (TextView) dialog_vendor.findViewById(R.id.editText2);
                editText2.setText(currentDateandTime1);

                editText22 = (TextView) dialog_vendor.findViewById(R.id.editText22);
                editText22.setText(currentDateandTime2);

                Date dtt_new = new Date();
                SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");
                final String time24_new = sdf1t_new.format(dtt_new);

                editText_from_day_hide = (TextView) dialog_vendor.findViewById(R.id.editText_from_day_hide);
                editText_from_day_visible = (TextView) dialog_vendor.findViewById(R.id.editText_from_day_visible);

//                Date dt = new Date();
//                SimpleDateFormat sdff1 = new SimpleDateFormat("HH:MM aa");
//                String timee1 = sdff1.format(dt);

                Date dt = new Date();
                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aa");
                final String time1 = sdf1.format(dt);

                Date dt1 = new Date();
                SimpleDateFormat sddff1 = new SimpleDateFormat("kkmm");
                String timee1 = sddff1.format(dt1);

                editText_from_day_visible.setText(time1);
                editText_from_day_hide.setText(timee1);

                editText11.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                Calendar now = Calendar.getInstance();
//                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
//                        datePickerListener,
//                        now.get(Calendar.YEAR),
//                        now.get(Calendar.MONTH),
//                        now.get(Calendar.DAY_OF_MONTH)
//
//
//                );
//
//                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");

                        Calendar now = Calendar.getInstance();
                        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                                datePickerListener,now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                        );

                        dpd.show(Micro_Inventory_Indent_Processing.this.getFragmentManager(), "Datepickerdialog");
                        //clickcounts++;




                    }

                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener datePickerListener
                            = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog, int selectedYear1, int selectedMonth1, int selectedDay1) {
                            year1 = selectedYear1;
                            month1 = selectedMonth1;
                            day1 = selectedDay1;

                            // set selected date into textview
                            populateSetDate(year1, month1 + 1, day1);
                        }
                    };





                    public void populateSetDate(int year, int month, int day) {
                        TextView mEdit = (TextView) dialog_vendor.findViewById(R.id.editText1);
                        TextView mEdit1  = (TextView) dialog_vendor.findViewById(R.id.editText11);
                        if (month == 1 && day < 10) {
                            mEdit.setText(year + " " + "0" + 1 + " " + "0" + day);
                            onee1 = "0" + day + " " + "Jan" + " " + year;
                            mEdit1.setText(onee1);
                        } else {
                            if (month == 1) {
                                mEdit.setText(year + " " + "0" + 1 + " " + day);
                                onee = day + " " + "Jan" + " " + year;
                                mEdit1.setText(onee);
                            }
                        }

                        if (month == 2 && day < 10) {
                            mEdit.setText(year + " " + "0" + 2 + " " + "0" + day);
                            two1 = "0" + day + " " + "Feb" + " " + year;
                            mEdit1.setText(two1);
                        } else {
                            if (month == 2) {
                                mEdit.setText(year + " " + "0" + 2 + " " + day);
                                two = day + " " + "Feb" + " " + year;
                                mEdit1.setText(two);
                            }
                        }

                        if (month == 3 && day < 10) {
                            mEdit.setText(year + " " + "0" + 3 + " " + "0" + day);
                            three1 = "0" + day + " " + "Mar" + " " + year;
                            mEdit1.setText(three1);
                        } else {
                            if (month == 3) {
                                mEdit.setText(year + " " + "0" + 3 + " " + day);
                                three = day + " " + "Mar" + " " + year;
                                mEdit1.setText(three);
                            }
                        }

                        if (month == 4 && day < 10) {
                            mEdit.setText(year + " " + "0" + 4 + " " + "0" + day);
                            four1 = "0" + day + " " + "Apr" + " " + year;
                            mEdit1.setText(four1);
                        } else {
                            if (month == 4) {
                                mEdit.setText(year + " " + "0" + 4 + " " + day);
                                four = day + " " + "Apr" + " " + year;
                                mEdit1.setText(four);
                            }
                        }

                        if (month == 5 && day < 10) {
                            mEdit.setText(year + " " + "0" + 5 + " " + "0" + day);
                            five1 = "0" + day + " " + "May" + " " + year;
                            mEdit1.setText(five1);
                        } else {
                            if (month == 5) {
                                mEdit.setText(year + " " + "0" + 5 + " " + day);
                                five = day + " " + "May" + " " + year;
                                mEdit1.setText(five);
                            }
                        }

                        if (month == 6 && day < 10) {
                            mEdit.setText(year + " " + "0" + 6 + " " + "0" + day);
                            six1 = "0" + day + " " + "Jun" + " " + year;
                            mEdit1.setText(six1);
                        } else {
                            if (month == 6) {
                                mEdit.setText(year + " " + "0" + 6 + " " + day);
                                six = day + " " + "Jun" + " " + year;
                                mEdit1.setText(six);
                            }
                        }

                        if (month == 7 && day < 10) {
                            mEdit.setText(year + " " + "0" + 7 + " " + "0" + day);
                            seven1 = "0" + day + " " + "Jul" + " " + year;
                            mEdit1.setText(seven1);
                        } else {
                            if (month == 7) {
                                mEdit.setText(year + " " + "0" + 7 + " " + day);
                                seven = day + " " + "Jul" + " " + year;
                                mEdit1.setText(seven);
                            }
                        }

                        if (month == 8 && day < 10) {
                            mEdit.setText(year + " " + "0" + 8 + " " + "0" + day);
                            eight1 = "0" + day + " " + "Aug" + " " + year;
                            mEdit1.setText(eight1);
                        } else {
                            if (month == 8) {
                                mEdit.setText(year + " " + "0" + 8 + " " + day);
                                eight = day + " " + "Aug" + " " + year;
                                mEdit1.setText(eight);
                            }
                        }

                        if (month == 9 && day < 10) {
                            mEdit.setText(year + " " + "0" + 9 + " " + "0" + day);
                            nine1 = "0" + day + " " + "Sep" + " " + year;
                            mEdit1.setText(nine1);
                        } else {
                            if (month == 9) {
                                mEdit.setText(year + " " + "0" + 9 + " " + day);
                                nine = day + " " + "Sep" + " " + year;
                                mEdit1.setText(nine);
                            }
                        }

                        if (month == 10 && day < 10) {
                            mEdit.setText(year + " " + 10 + " " + "0" + day);
                            ten1 = "0" + day + " " + "Oct" + " " + year;
                            mEdit1.setText(ten1);
                        } else {
                            if (month == 10) {
                                mEdit.setText(year + " " + 10 + " " + day);
                                ten = day + " " + "Oct" + " " + year;
                                mEdit1.setText(ten);
                            }
                        }

                        if (month == 11 && day < 10) {
                            mEdit.setText(year + " " + 11 + " " + "0" + day);
                            eleven1 = "0" + day + " " + "Nov" + " " + year;
                            mEdit1.setText(eleven1);
                        } else {
                            if (month == 11) {
                                mEdit.setText(year + " " + 11 + " " + day);
                                eleven = day + " " + "Nov" + " " + year;
                                mEdit1.setText(eleven);
                            }
                        }

                        if (month == 12 && day < 10) {
                            mEdit.setText(year + " " + 12 + " " + "0" + day);
                            twelve1 = "0" + day + " " + "Dec" + " " + year;
                            mEdit1.setText(twelve1);
                        } else {
                            if (month == 12) {
                                mEdit.setText(year + " " + 12 + " " + day);
                                twelve = day + " " + "Dec" + " " + year;
                                mEdit1.setText(twelve);
                            }
                        }

                    }

                });


                editText22.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                Calendar now = Calendar.getInstance();
//                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
//                        datePickerListener,
//                        now.get(Calendar.YEAR),
//                        now.get(Calendar.MONTH),
//                        now.get(Calendar.DAY_OF_MONTH)
//
//
//                );
//
//                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                        //if (clickcount == 1){
                        Calendar now = Calendar.getInstance();
                        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                                datePickerListener,now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                        );

                        dpd.show(Micro_Inventory_Indent_Processing.this.getFragmentManager(), "Datepickerdialog");
//                }else {
//                    Calendar now = Calendar.getInstance();
//                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
//                            datePickerListener, year, month, day
//                    );
//
//                    dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
//                }

                    }

                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener datePickerListener
                            = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog, int selectedYear, int selectedMonth, int selectedDay) {
                            year = selectedYear;
                            month = selectedMonth;
                            day = selectedDay;

                            // set selected date into textview
                            populateSetDate(year, month + 1, day);
                        }
                    };

//                // when dialog box is closed, below method will be called.
//                public void onDateSet(DatePicker view, int selectedYear,
//                                      int selectedMonth, int selectedDay) {
//
//
//
//
//                }
//            };


                    public void populateSetDate(int year, int month, int day) {
                        TextView mEdit = (TextView) dialog_vendor.findViewById(R.id.editText2);
                        TextView mEdit1  = (TextView) dialog_vendor.findViewById(R.id.editText22);
                        if (month == 1 && day < 10) {
                            mEdit.setText(year + " " + "0" + 1 + " " + "0" + day);
                            onee1 = "0" + day + " " + "Jan" + " " + year;
                            mEdit1.setText(onee1);
                        } else {
                            if (month == 1) {
                                mEdit.setText(year + " " + "0" + 1 + " " + day);
                                onee = day + " " + "Jan" + " " + year;
                                mEdit1.setText(onee);
                            }
                        }

                        if (month == 2 && day < 10) {
                            mEdit.setText(year + " " + "0" + 2 + " " + "0" + day);
                            two1 = "0" + day + " " + "Feb" + " " + year;
                            mEdit1.setText(two1);
                        } else {
                            if (month == 2) {
                                mEdit.setText(year + " " + "0" + 2 + " " + day);
                                two = day + " " + "Feb" + " " + year;
                                mEdit1.setText(two);
                            }
                        }

                        if (month == 3 && day < 10) {
                            mEdit.setText(year + " " + "0" + 3 + " " + "0" + day);
                            three1 = "0" + day + " " + "Mar" + " " + year;
                            mEdit1.setText(three1);
                        } else {
                            if (month == 3) {
                                mEdit.setText(year + " " + "0" + 3 + " " + day);
                                three = day + " " + "Mar" + " " + year;
                                mEdit1.setText(three);
                            }
                        }

                        if (month == 4 && day < 10) {
                            mEdit.setText(year + " " + "0" + 4 + " " + "0" + day);
                            four1 = "0" + day + " " + "Apr" + " " + year;
                            mEdit1.setText(four1);
                        } else {
                            if (month == 4) {
                                mEdit.setText(year + " " + "0" + 4 + " " + day);
                                four = day + " " + "Apr" + " " + year;
                                mEdit1.setText(four);
                            }
                        }

                        if (month == 5 && day < 10) {
                            mEdit.setText(year + " " + "0" + 5 + " " + "0" + day);
                            five1 = "0" + day + " " + "May" + " " + year;
                            mEdit1.setText(five1);
                        } else {
                            if (month == 5) {
                                mEdit.setText(year + " " + "0" + 5 + " " + day);
                                five = day + " " + "May" + " " + year;
                                mEdit1.setText(five);
                            }
                        }

                        if (month == 6 && day < 10) {
                            mEdit.setText(year + " " + "0" + 6 + " " + "0" + day);
                            six1 = "0" + day + " " + "Jun" + " " + year;
                            mEdit1.setText(six1);
                        } else {
                            if (month == 6) {
                                mEdit.setText(year + " " + "0" + 6 + " " + day);
                                six = day + " " + "Jun" + " " + year;
                                mEdit1.setText(six);
                            }
                        }

                        if (month == 7 && day < 10) {
                            mEdit.setText(year + " " + "0" + 7 + " " + "0" + day);
                            seven1 = "0" + day + " " + "Jul" + " " + year;
                            mEdit1.setText(seven1);
                        } else {
                            if (month == 7) {
                                mEdit.setText(year + " " + "0" + 7 + " " + day);
                                seven = day + " " + "Jul" + " " + year;
                                mEdit1.setText(seven);
                            }
                        }

                        if (month == 8 && day < 10) {
                            mEdit.setText(year + " " + "0" + 8 + " " + "0" + day);
                            eight1 = "0" + day + " " + "Aug" + " " + year;
                            mEdit1.setText(eight1);
                        } else {
                            if (month == 8) {
                                mEdit.setText(year + " " + "0" + 8 + " " + day);
                                eight = day + " " + "Aug" + " " + year;
                                mEdit1.setText(eight);
                            }
                        }

                        if (month == 9 && day < 10) {
                            mEdit.setText(year + " " + "0" + 9 + " " + "0" + day);
                            nine1 = "0" + day + " " + "Sep" + " " + year;
                            mEdit1.setText(nine1);
                        } else {
                            if (month == 9) {
                                mEdit.setText(year + " " + "0" + 9 + " " + day);
                                nine = day + " " + "Sep" + " " + year;
                                mEdit1.setText(nine);
                            }
                        }

                        if (month == 10 && day < 10) {
                            mEdit.setText(year + " " + 10 + " " + "0" + day);
                            ten1 = "0" + day + " " + "Oct" + " " + year;
                            mEdit1.setText(ten1);
                        } else {
                            if (month == 10) {
                                mEdit.setText(year + " " + 10 + " " + day);
                                ten = day + " " + "Oct" + " " + year;
                                mEdit1.setText(ten);
                            }
                        }

                        if (month == 11 && day < 10) {
                            mEdit.setText(year + " " + 11 + " " + "0" + day);
                            eleven1 = "0" + day + " " + "Nov" + " " + year;
                            mEdit1.setText(eleven1);
                        } else {
                            if (month == 11) {
                                mEdit.setText(year + " " + 11 + " " + day);
                                eleven = day + " " + "Nov" + " " + year;
                                mEdit1.setText(eleven);
                            }
                        }

                        if (month == 12 && day < 10) {
                            mEdit.setText(year + " " + 12 + " " + "0" + day);
                            twelve1 = "0" + day + " " + "Dec" + " " + year;
                            mEdit1.setText(twelve1);
                        } else {
                            if (month == 12) {
                                mEdit.setText(year + " " + 12 + " " + day);
                                twelve = day + " " + "Dec" + " " + year;
                                mEdit1.setText(twelve);
                            }
                        }

                    }

//            class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
//                @Override
//                public Dialog onCreateDialog(Bundle savedInstanceState) {
//                    final Calendar calendar = Calendar.getInstance();
//                    int yy = calendar.get(Calendar.YEAR);
//                    int mm = calendar.get(Calendar.MONTH);
//                    int dd = calendar.get(Calendar.DAY_OF_MONTH);
//                    return new DatePickerDialog(getActivity(), this, yy, mm, dd);
//                }
//
//
//                @Override
//                public void onDateSet(DatePicker view, int yy, int mm, int dd) {
//                    populateSetDate(yy, mm + 1, dd);
//                }
//            }

                });

                editText_from_day_visible.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(Micro_Inventory_Indent_Processing.this, R.style.timepicker_date_dialog, timePickerListener_open, hour, minute,
                                false);

                        timePickerDialog.show();
                    }
                });


                ImageButton okedit = (ImageButton) dialog_vendor.findViewById(R.id.okedit);
                okedit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (vend_name.getText().toString().equals("")){
//                            Toast.makeText(Inventory_Indent_Processing.this, "Select vendor", Toast.LENGTH_SHORT).show();
                        }else {
                            final Dialog dialog_vendor_save_confirmation = new Dialog(Micro_Inventory_Indent_Processing.this, R.style.timepicker_date_dialog);
                            dialog_vendor_save_confirmation.setContentView(R.layout.dialog_inventory_indent_confirmation_save);
                            dialog_vendor_save_confirmation.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                            dialog_vendor_save_confirmation.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            dialog_vendor_save_confirmation.setCanceledOnTouchOutside(false);
                            dialog_vendor_save_confirmation.show();

                            Button cancel = (Button) dialog_vendor_save_confirmation.findViewById(R.id.cancel);
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog_vendor_save_confirmation.dismiss();
                                }
                            });

                            ImageView closetext = (ImageView) dialog_vendor_save_confirmation.findViewById(R.id.closetext);
                            closetext.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog_vendor_save_confirmation.dismiss();
                                }
                            });

                            Button ok = (Button) dialog_vendor_save_confirmation.findViewById(R.id.ok);
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String time24_new = editText1.getText().toString()+editText_from_day_hide.getText().toString();

                                    if (time24_new.toString().contains(" ")){
                                        time24_new = time24_new.replace(" ", "");
                                    }
                                    String selectedSweet = editText2.getText().toString();
                                    if (selectedSweet.toString().contains(" ")){
                                        selectedSweet = selectedSweet.replace(" ", "");
                                    }

                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("vendor_name", vend_name.getText().toString());
                                    contentValues.put("vendor_phoneno", vend_phoneno.getText().toString());
                                    contentValues.put("invoice", invoice_no.getText().toString());
                                    contentValues.put("billamount", dialog_billamount.getText().toString());
                                    contentValues.put("tax_percent", dialog_percent_tax.getText().toString());
                                    contentValues.put("tax_amount", dialog_amount_tax.getText().toString());
                                    contentValues.put("disc_percent", dialog_percent_disc.getText().toString());
                                    contentValues.put("disc_amount", dialog_amount_discount.getText().toString());
                                    contentValues.put("total_bill_amount", total_amount.getText().toString());
                                    contentValues.put("from_time", editText_from_day_visible.getText().toString());
                                    contentValues.put("from_date", editText11.getText().toString());
                                    contentValues.put("due_date", editText22.getText().toString());
                                    contentValues.put("datetimee_new_from", time24_new);
                                    contentValues.put("datetimee_new_due", selectedSweet);
                                    contentValues.put("pay_date", editText11.getText().toString());
                                    contentValues.put("pay_time", editText_from_day_visible.getText().toString());
                                    contentValues.put("pay_datetimeemew", time24_new);
                                    if (pay_amount.getText().toString().equals("")){
                                        contentValues.put("pay", "0");
                                        contentValues.put("total_pay", "0");
                                        contentValues.put("pending", bill_totoal_amount.getText().toString());
//                                        Toast.makeText(Inventory_Indent_Processing.this, "empty1 "+bill_totoal_amount.getText().toString(), Toast.LENGTH_SHORT).show();
                                    }else {
                                        contentValues.put("pay", pay_amount.getText().toString());
                                        contentValues.put("total_pay", pay_amount.getText().toString());
                                        float za = Float.parseFloat(bill_totoal_amount.getText().toString()) - Float.parseFloat(pay_amount.getText().toString());
                                        String za1 = String.format("%.1f", za);
                                        contentValues.put("pending", za1);
//                                        Toast.makeText(Inventory_Indent_Processing.this, "empty2 "+za1, Toast.LENGTH_SHORT).show();
                                    }
                                    contentValues.put("not_required", "");
                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_sold_details");
                                    resultUri = getContentResolver().insert(contentUri, contentValues);
                                    getContentResolver().notifyChange(resultUri, null);
//                                    db.insert("Ingredient_sold_details", null, contentValues);


                                    Cursor cursor2 = db.rawQuery("SELECT * FROM Ingredients WHERE status_qty_updated = 'Add'", null);
                                    if (cursor2.moveToFirst()){
                                        do {
                                            String iname = cursor2.getString(1);
                                            String qty_add = cursor2.getString(18);
                                            String indi_pri = cursor2.getString(22);
                                            String was = cursor2.getString(21);
                                            String unit = cursor2.getString(5);


                                            float az = Float.parseFloat(qty_add) * Float.parseFloat(indi_pri);
                                            String tot = String.format("%.1f", az);

                                            ContentValues contentValues1 = new ContentValues();
                                            contentValues1.put("vendor_name", vend_name.getText().toString());
                                            contentValues1.put("vendor_phoneno", vend_phoneno.getText().toString());
                                            contentValues1.put("itemname", iname);
                                            contentValues1.put("qty_add", qty_add);
                                            contentValues1.put("individual_price", indi_pri);
                                            contentValues1.put("total_price", tot);
                                            contentValues1.put("invoice", invoice_no.getText().toString());
                                            contentValues1.put("billamount", dialog_billamount.getText().toString());
                                            contentValues1.put("tax_percent", dialog_percent_tax.getText().toString());
                                            contentValues1.put("tax_amount", dialog_amount_tax.getText().toString());
                                            contentValues1.put("disc_percent", dialog_percent_disc.getText().toString());
                                            contentValues1.put("disc_amount", dialog_amount_discount.getText().toString());
                                            contentValues1.put("total_bill_amount", total_amount.getText().toString());
                                            contentValues1.put("from_time", editText_from_day_visible.getText().toString());
                                            contentValues1.put("from_date", editText11.getText().toString());
                                            contentValues1.put("due_date", editText22.getText().toString());
                                            contentValues1.put("datetimee_new_from", time24_new);
                                            contentValues1.put("datetimee_new_due", selectedSweet);
                                            contentValues1.put("pay", pay_amount.getText().toString());

                                            contentValues1.put("wastage", was);
                                            contentValues1.put("unit", unit);


                                            if (pay_amount.getText().toString().equals("")){
                                                contentValues1.put("pending", bill_totoal_amount.getText().toString());
//                                                Toast.makeText(Inventory_Indent_Processing.this, "empty1 "+bill_totoal_amount.getText().toString(), Toast.LENGTH_SHORT).show();
                                            }else {
                                                float za = Float.parseFloat(bill_totoal_amount.getText().toString()) - Float.parseFloat(pay_amount.getText().toString());
                                                String za1 = String.format("%.1f", za);
                                                contentValues1.put("pending", za1);
//                                                Toast.makeText(Inventory_Indent_Processing.this, "empty2 "+za1, Toast.LENGTH_SHORT).show();
                                            }
                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_sold_item_details");
                                            resultUri = getContentResolver().insert(contentUri, contentValues1);
                                            getContentResolver().notifyChange(resultUri, null);
//                                            db.insert("Ingredient_sold_item_details", null, contentValues1);

                                            dialog_vendor_save_confirmation.dismiss();
                                            dialog_vendor.dismiss();
                                        }while (cursor2.moveToNext());
                                    }

                                    int i = 1;
                                    Cursor c1_11 = db.rawQuery("SELECT * FROM Taxes WHERE checked = 'checked_s'", null);
                                    if (c1_11.moveToFirst()) {
                                        do {
                                            String tn = c1_11.getString(1);
                                            String tn_value = c1_11.getString(2);

                                            ContentValues contentValues1 = new ContentValues();
                                            contentValues1.put("tax"+i, tn);
                                            contentValues1.put("tax"+i+"_value", tn_value);
                                            String wherecu1 = "datetimee_new_from = '" + time24_new + "'";

                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_sold_item_details");
                                            getContentResolver().update(contentUri, contentValues1, wherecu1, new String[]{});
                                            resultUri = new Uri.Builder()
                                                    .scheme("content")
                                                    .authority(StubProviderApp.AUTHORITY)
                                                    .path("Ingredient_sold_item_details")
                                                    .appendQueryParameter("operation", "update")
                                                    .appendQueryParameter("datetimee_new_from", time24_new)
                                                    .build();
                                            getContentResolver().notifyChange(resultUri, null);
//                                            db.update("Ingredient_sold_item_details", contentValues1, wherecu1, new String[]{});
                                            i++;
                                        }while (c1_11.moveToNext());
                                    }

                                    Cursor cursor3 = db.rawQuery("SELECT * FROM Taxes", null);
                                    if (cursor3.moveToFirst()){
                                        do {
                                            String id = cursor3.getString(0);

                                            ContentValues contentValues1 = new ContentValues();
                                            contentValues1.put("checked", "");
                                            String where = "_id = '" + id + "'";

                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Taxes");
                                            getContentResolver().update(contentUri, contentValues1, where, new String[]{});
                                            resultUri = new Uri.Builder()
                                                    .scheme("content")
                                                    .authority(StubProviderApp.AUTHORITY)
                                                    .path("Taxes")
                                                    .appendQueryParameter("operation", "update")
                                                    .appendQueryParameter("_id", id)
                                                    .build();
                                            getContentResolver().notifyChange(resultUri, null);
//                                            db.update("Taxes", contentValues1, where, new String[]{});
                                        } while (cursor3.moveToNext());
                                    }

                                    Cursor cursor4 = db.rawQuery("SELECT * FROM Ingredients WHERE status_qty_updated = 'Add'", null);
                                    if (cursor4.moveToFirst()){
                                        do {
                                            String id = cursor4.getString(0);
                                            String add_qt = cursor4.getString(18);
                                            String cur_qty = cursor4.getString(4);
                                            String max_qty = cursor4.getString(3);
                                            String ori_ind_pri = cursor4.getString(13);
                                            String ori_ind_pri1 = cursor4.getString(6);
                                            String curr_ind_price = cursor4.getString(22);
                                            String adj = cursor4.getString(21);


                                            ContentValues contentValues1 = new ContentValues();

                                            TextView tv = new TextView(Micro_Inventory_Indent_Processing.this);
                                            tv.setText(adj);
                                            if (tv.getText().toString().equals("")){
                                                float za = Float.parseFloat(cur_qty) + Float.parseFloat(add_qt);
                                                String za1 = String.format("%.1f", za);
                                                contentValues1.put("current_stock", za1);

                                                float zaa = Float.parseFloat(max_qty) - za;
                                                String zaa1 = String.format("%.1f", zaa);
                                                contentValues1.put("required", String.valueOf(zaa1));
                                            }else {
                                                float za = Float.parseFloat(cur_qty) + Float.parseFloat(add_qt) - Float.parseFloat(adj);
                                                String za1 = String.format("%.1f", za);
                                                contentValues1.put("current_stock", za1);

                                                float zaa = Float.parseFloat(max_qty) - za;
                                                String zaa1 = String.format("%.1f", zaa);
                                                contentValues1.put("required", String.valueOf(zaa1));
                                            }
//                                            Toast.makeText(Inventory_Indent_Processing.this, "present quan "+za1+" "+id, Toast.LENGTH_SHORT).show();

                                            contentValues1.put("add_qty", "0");
                                            contentValues1.put("status_qty_updated", "");
                                            contentValues1.put("indiv_price", curr_ind_price);


//                                            if (ori_ind_pri.toString().equals("0") || ori_ind_pri.toString().equals("0.0") || ori_ind_pri.toString().equals("")){
////                                                float a = (Float.parseFloat(ori_ind_pri) + Float.parseFloat(curr_ind_price)) / 2;
//                                                contentValues1.put("avg_price", curr_ind_price);
//                                            }else {
//                                                float a = ((Float.parseFloat(ori_ind_pri)) + (Float.parseFloat(curr_ind_price)) / Float.parseFloat(add_qt));
//                                                contentValues1.put("avg_price", String.valueOf(a));
//                                            }


                                            contentValues1.put("adjusted_stock", "");
                                            contentValues1.put("total_price_temp", "");
                                            contentValues1.put("diff_stock", "");

                                            String where = "_id = '" + id + "'";

                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients");
                                            getContentResolver().update(contentUri, contentValues1, where, new String[]{});
                                            resultUri = new Uri.Builder()
                                                    .scheme("content")
                                                    .authority(StubProviderApp.AUTHORITY)
                                                    .path("Ingredients")
                                                    .appendQueryParameter("operation", "update")
                                                    .appendQueryParameter("_id", id)
                                                    .build();
                                            getContentResolver().notifyChange(resultUri, null);

//                                            db.update("Ingredients", contentValues1, where, new String[]{});
                                        } while (cursor4.moveToNext());
                                    }

                                    Cursor cursor41 = db.rawQuery("SELECT * FROM Ingredients", null);
                                    if (cursor41.moveToFirst()){
                                        do {
                                            String id = cursor41.getString(0);
                                            String na = cursor41.getString(1);
                                            ContentValues contentValues1 = new ContentValues();

                                            Cursor cursor1 = db.rawQuery("SELECT SUM(total_price) FROM Ingredient_sold_item_details WHERE itemname = '" + na + "'", null);
                                            if (cursor1.moveToFirst()) {
                                                float a = cursor1.getFloat(0);

                                                Cursor cursor21 = db.rawQuery("SELECT SUM(qty_add) FROM Ingredient_sold_item_details WHERE itemname = '" + na + "'", null);
                                                if (cursor21.moveToFirst()) {
                                                    int a1 = cursor21.getInt(0);

                                                    float a2 = a / a1;

                                                    contentValues1.put("avg_price", String.format("%.1f", a2));
                                                    String where = "_id = '" + id + "'";

                                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients");
                                                    getContentResolver().update(contentUri, contentValues1, where, new String[]{});
                                                    resultUri = new Uri.Builder()
                                                            .scheme("content")
                                                            .authority(StubProviderApp.AUTHORITY)
                                                            .path("Ingredients")
                                                            .appendQueryParameter("operation", "update")
                                                            .appendQueryParameter("_id", id)
                                                            .build();
                                                    getContentResolver().notifyChange(resultUri, null);
//                                                    db.update("Ingredients", contentValues1, where, new String[]{});
                                                }
                                            }

                                        }while (cursor41.moveToNext());
                                    }

                                    Cursor cursor411 = db.rawQuery("SELECT * FROM Ingredient_items_list", null);
                                    if (cursor411.moveToFirst()){
                                        do {
                                            String id = cursor411.getString(0);
                                            String ingr_name = cursor411.getString(1);

                                            Cursor cursor5 = db.rawQuery("SELECT * FROM Ingredients WHERE ingredient_name = '"+ingr_name+"'", null);
                                            if (cursor5.moveToFirst()){
                                                do {
                                                    String cur_qty = cursor5.getString(4);

                                                    ContentValues contentValues1 = new ContentValues();
                                                    contentValues1.put("currnet_stock", cur_qty);

                                                    String where = "_id = '" + id + "'";
                                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredient_items_list");
                                                    getContentResolver().update(contentUri, contentValues1, where, new String[]{});
                                                    resultUri = new Uri.Builder()
                                                            .scheme("content")
                                                            .authority(StubProviderApp.AUTHORITY)
                                                            .path("Ingredient_items_list")
                                                            .appendQueryParameter("operation", "update")
                                                            .appendQueryParameter("_id", id)
                                                            .build();
                                                    getContentResolver().notifyChange(resultUri, null);
//                                                    db.update("Ingredient_items_list", contentValues1, where, new String[]{});
                                                } while (cursor5.moveToNext());
                                            }

                                        }while (cursor411.moveToNext());
                                    }

                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Tax_selec");
                                    getContentResolver().delete(contentUri, null, null);
                                    resultUri = new Uri.Builder()
                                            .scheme("content")
                                            .authority(StubProviderApp.AUTHORITY)
                                            .path("Tax_selec")
                                            .appendQueryParameter("operation", "delete")
                                            .appendQueryParameter(null, null)
                                            .build();
                                    getContentResolver().notifyChange(resultUri, null);

//                                    db.delete("Tax_selec", null, null);


                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Discount_selec");
                                    getContentResolver().delete(contentUri, null, null);
                                    resultUri = new Uri.Builder()
                                            .scheme("content")
                                            .authority(StubProviderApp.AUTHORITY)
                                            .path("Discount_selec")
                                            .appendQueryParameter("operation", "delete")
                                            .appendQueryParameter(null, null)
                                            .build();
                                    getContentResolver().notifyChange(resultUri, null);

//                                    db.delete("Discount_selec", null, null);

                                    final Dialog dialog_invetory_indent_confirmation = new Dialog(Micro_Inventory_Indent_Processing.this, R.style.timepicker_date_dialog);
                                    dialog_invetory_indent_confirmation.setContentView(R.layout.dialog_invetory_indent_confirmation);
                                    dialog_invetory_indent_confirmation.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                    dialog_invetory_indent_confirmation.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                    dialog_invetory_indent_confirmation.setCanceledOnTouchOutside(false);
                                    dialog_invetory_indent_confirmation.show();

                                    TextView inn = (TextView) dialog_invetory_indent_confirmation.findViewById(R.id.inn);
                                    TextView inn1 = (TextView) dialog_invetory_indent_confirmation.findViewById(R.id.inn1);
                                    inn.setText(insert1_cc);
                                    inn1.setText(insert1_cc);

                                    TextView vendor_name_confirm = (TextView) dialog_invetory_indent_confirmation.findViewById(R.id.vendor_name_confirm);
                                    vendor_name_confirm.setText(vend_name.getText().toString());

                                    TextView vendor_invoice_confirm = (TextView) dialog_invetory_indent_confirmation.findViewById(R.id.vendor_invoice_confirm);
                                    vendor_invoice_confirm.setText(invoice_no.getText().toString());

                                    TextView vendor_bill_confirm = (TextView) dialog_invetory_indent_confirmation.findViewById(R.id.vendor_bill_confirm);
                                    vendor_bill_confirm.setText(bill_totoal_amount.getText().toString());

                                    TextView vendor_paid_confirm = (TextView) dialog_invetory_indent_confirmation.findViewById(R.id.vendor_paid_confirm);
                                    if (pay_amount.getText().toString().equals("")){
                                        vendor_paid_confirm.setText("0");
                                    }else {
                                        vendor_paid_confirm.setText(pay_amount.getText().toString());
                                    }

                                    Button closetext = (Button) dialog_invetory_indent_confirmation.findViewById(R.id.closetext);
                                    closetext.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(Micro_Inventory_Indent_Processing.this, Micro_inventory_indent.class);
                                            intent.putExtra("hii", "2");
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }
                                    });


                                    dialog_invetory_indent_confirmation.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                        @Override
                                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                                dialog_invetory_indent_confirmation.dismiss();
                                                Intent intent = new Intent(Micro_Inventory_Indent_Processing.this, Micro_inventory_indent.class);
                                                intent.putExtra("hii", "2");
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                //row.setBackgroundResource(0);
                                                return true;
                                            }
                                            return false;
                                        }
                                    });



                                }
                            });
                        }




                    }
                });


                ImageButton vendor_add = (ImageButton) dialog_vendor.findViewById(R.id.vendor_add);
                vendor_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog_add_vendor = new Dialog(Micro_Inventory_Indent_Processing.this, R.style.timepicker_date_dialog);
                        dialog_add_vendor.setContentView(R.layout.dialog_inventory_intent_add_vendor);
                        dialog_add_vendor.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        dialog_add_vendor.setCanceledOnTouchOutside(false);
                        dialog_add_vendor.show();

                        ImageButton canceledit = (ImageButton) dialog_add_vendor.findViewById(R.id.canceledit);
                        canceledit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog_add_vendor.dismiss();
                            }
                        });

                        final EditText nameedit = (EditText) dialog_add_vendor.findViewById(R.id.nameedit);
                        final EditText phonenoedit = (EditText) dialog_add_vendor.findViewById(R.id.phonenoedit);
                        final EditText emailidedit = (EditText) dialog_add_vendor.findViewById(R.id.emailidedit);
                        final EditText addressedit = (EditText) dialog_add_vendor.findViewById(R.id.addressedit);
                        final EditText gsttinedit = (EditText) dialog_add_vendor.findViewById(R.id.gsttinedit);


                        final TextInputLayout nameedit_layout = (TextInputLayout) dialog_add_vendor.findViewById(R.id.nameedit_layout);
                        final TextInputLayout phonenoedit_layout = (TextInputLayout) dialog_add_vendor.findViewById(R.id.phonenoedit_layout);
                        final TextInputLayout emailidedit_layout = (TextInputLayout) dialog_add_vendor.findViewById(R.id.emailidedit_layout);

                        nameedit.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                nameedit_layout.setError(null);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });

                        phonenoedit.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                phonenoedit_layout.setError(null);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });


                        ImageButton okedit = (ImageButton) dialog_add_vendor.findViewById(R.id.okedit);
                        okedit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (nameedit.getText().toString().equals("") || phonenoedit.getText().toString().equals("")){
                                    if (nameedit.getText().toString().equals("")){
                                        nameedit_layout.setError("Enter name");
                                    }
                                    if (phonenoedit.getText().toString().equals("")){
                                        phonenoedit_layout.setError("Enter Phone no.");
                                    }
                                }else {
                                    Cursor cursor2 = db.rawQuery("SELECT * FROM Vendor_details_micro WHERE vendor_phoneno = '"+phonenoedit.getText().toString()+"'", null);
                                    if (cursor2.moveToFirst()){
                                        phonenoedit_layout.setError("Phone no. already entered");
                                    }else {
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("vendor_name", nameedit.getText().toString());
                                        contentValues.put("vendor_phoneno", phonenoedit.getText().toString());
                                        contentValues.put("vendor_email", emailidedit.getText().toString());
                                        contentValues.put("vendor_address", addressedit.getText().toString());
                                        contentValues.put("vendor_gst", gsttinedit.getText().toString());
                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Vendor_details_micro");
                                        resultUri = getContentResolver().insert(contentUri, contentValues);
                                        getContentResolver().notifyChange(resultUri, null);
//                                        db.insert("Vendor_details_micro", null, contentValues);

                                        dialog_add_vendor.dismiss();
                                    }
                                }
                            }
                        });


                    }
                });





            }
        });


        Button clear_all = (Button) findViewById(R.id.clear_all);
        clear_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog_clear_warning = new Dialog(Micro_Inventory_Indent_Processing.this, R.style.timepicker_date_dialog);
                dialog_clear_warning.setContentView(R.layout.dialog_inventory_intent_clear_warning);
                dialog_clear_warning.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                dialog_clear_warning.setCanceledOnTouchOutside(false);
                dialog_clear_warning.show();

                ImageView closetext = (ImageView) dialog_clear_warning.findViewById(R.id.closetext);
                closetext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_clear_warning.dismiss();
                    }
                });

                Button cancel = (Button) dialog_clear_warning.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_clear_warning.dismiss();
                    }
                });

                Button ok = (Button) dialog_clear_warning.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Cursor cursor3 = db.rawQuery("SELECT * FROM Taxes", null);
                        if (cursor3.moveToFirst()){
                            do {
                                String id = cursor3.getString(0);

                                ContentValues contentValues1 = new ContentValues();
                                contentValues1.put("checked", "");
                                String where = "_id = '" + id + "'";

                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Taxes");
                                getContentResolver().update(contentUri, contentValues1, where, new String[]{});
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProviderApp.AUTHORITY)
                                        .path("Taxes")
                                        .appendQueryParameter("operation", "update")
                                        .appendQueryParameter("_id", id)
                                        .build();
                                getContentResolver().notifyChange(resultUri, null);
//                                db.update("Taxes", contentValues1, where, new String[]{});
                            } while (cursor3.moveToNext());
                        }

                        Cursor cursor4 = db.rawQuery("SELECT * FROM Ingredients WHERE status_qty_updated = 'Add'", null);
                        if (cursor4.moveToFirst()){
                            do {
                                String id = cursor4.getString(0);
                                String add_qt = cursor4.getString(22);
                                String cur_qty = cursor4.getString(3);

                                ContentValues contentValues1 = new ContentValues();
                                contentValues1.put("add_qty", "0");
                                contentValues1.put("status_qty_updated", "");
                                contentValues1.put("indiv_price_temp", "");
                                contentValues1.put("adjusted_stock", "");
                                contentValues1.put("total_price_temp", "");
                                contentValues1.put("diff_stock", "");
                                String where = "_id = '" + id + "'";

                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Ingredients");
                                getContentResolver().update(contentUri, contentValues1, where, new String[]{});
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProviderApp.AUTHORITY)
                                        .path("Ingredients")
                                        .appendQueryParameter("operation", "update")
                                        .appendQueryParameter("_id", id)
                                        .build();
                                getContentResolver().notifyChange(resultUri, null);

//                                db.update("Ingredients", contentValues1, where, new String[]{});
                            } while (cursor4.moveToNext());
                        }

                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Tax_selec");
                        getContentResolver().delete(contentUri, null, null);
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("Tax_selec")
                                .appendQueryParameter("operation", "delete")
                                .appendQueryParameter(null, null)
                                .build();
                        getContentResolver().notifyChange(resultUri, null);

//                                    db.delete("Tax_selec", null, null);


                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Discount_selec");
                        getContentResolver().delete(contentUri, null, null);
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("Discount_selec")
                                .appendQueryParameter("operation", "delete")
                                .appendQueryParameter(null, null)
                                .build();
                        getContentResolver().notifyChange(resultUri, null);
//                        db.delete("Discount_selec", null, null);

                        dialog_clear_warning.dismiss();
                        Intent intent = new Intent(Micro_Inventory_Indent_Processing.this, Micro_inventory_indent.class);
                        intent.putExtra("hii", "2");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });

    }


    public ArrayList<String> getTableValuesall() {
        ArrayList<String> my_array = new ArrayList<String>();
        try {
            db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor allrows = db.rawQuery("SELECT * FROM Vendor_details_micro", null);
            System.out.println("COUNT : " + allrows.getCount());

            if (allrows.moveToFirst()) {
                do {
                    String ID = allrows.getString(0);
                    String NAME = allrows.getString(1);
                    String PLACE = allrows.getString(2);

                    my_array.add(NAME+" - "+PLACE);

                } while (allrows.moveToNext());
            }
            allrows.close();
            //db.close();
        } catch (Exception e) {
            Toast.makeText(Micro_Inventory_Indent_Processing.this, "Error encountered.",
                    Toast.LENGTH_LONG);
        }
        return my_array;
    }


    private android.app.TimePickerDialog.OnTimeSetListener timePickerListener_open = new android.app.TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime_open(hour, minute);

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minutes);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);


            String hour1 = "";
            if (hour < 10)
                hour1 = "0" + hour;
            else
                hour1 = String.valueOf(hour);

            String minutes1 = "";
            if (minute < 10)
                minutes1 = "0" + minute;
            else
                minutes1 = String.valueOf(minute);

            editText_from_day_hide.setText(hour1 + "" + minutes1);


        }
    };

    private void updateTime_open(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        editText_from_day_visible.setText(aTime);
    }

}
