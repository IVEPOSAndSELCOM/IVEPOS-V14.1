package com.intuition.ivepos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.intuition.ivepos.syncapp.StubProviderApp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Rohithkumar on 9/6/2017.
 */

public class Inventory_Indent_Vendor_list extends AppCompatActivity {


    public SQLiteDatabase db = null;

    ListView vendor_list;
    EditText searchView;
    TextView text2;

    Uri contentUri, resultUri;
    TextView paid_sum, pending_sum, total_sum;

    String insert1_cc = "", insert1_rs = "", str_country;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_indent_vendor_list);


        ImageView back_pressed = (ImageView) findViewById(R.id.back_pressed);
        back_pressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        Cursor cursor_country = db.rawQuery("SELECT * FROM Country_Selection", null);
        if (cursor_country.moveToFirst()){
            str_country = cursor_country.getString(1);
        }

        TextView inn = (TextView) findViewById(R.id.inn);
        TextView inn1 = (TextView) findViewById(R.id.inn1);
        TextView inn2 = (TextView) findViewById(R.id.inn2);

        if (str_country.toString().equals("Rupee") || str_country.toString().equals("India")) {
            insert1_cc = "\u20B9";
            insert1_rs = "Rs.";
            inn.setText(insert1_cc);
            inn1.setText(insert1_cc);
            inn2.setText(insert1_cc);
        }else {
            if (str_country.toString().equals("Pound") || str_country.toString().equals("UK Pound")) {
                insert1_cc = "\u00a3";
                insert1_rs = "BP.";
                inn.setText(insert1_cc);
                inn1.setText(insert1_cc);
                inn2.setText(insert1_cc);
            }else {
                if (str_country.toString().equals("Euro") || str_country.toString().equals("UK Euro")) {
                    insert1_cc = "\u20ac";
                    insert1_rs = "EU.";
                    inn.setText(insert1_cc);
                    inn1.setText(insert1_cc);
                    inn2.setText(insert1_cc);
                }else {
                    if (str_country.toString().equals("Dollar")) {
                        insert1_cc = "\u0024";
                        insert1_rs = "\u0024";
                        inn.setText(insert1_cc);
                        inn1.setText(insert1_cc);
                        inn2.setText(insert1_cc);
                    }else {
                        if (str_country.toString().equals("Dinar")) {
                            insert1_cc = "D";
                            insert1_rs = "KD.";
                            inn.setText(insert1_cc);
                            inn1.setText(insert1_cc);
                            inn2.setText(insert1_cc);
                        }else {
                            if (str_country.toString().equals("Shilling")) {
                                insert1_cc = "S";
                                insert1_rs = "S.";
                                inn.setText(insert1_cc);
                                inn1.setText(insert1_cc);
                                inn2.setText(insert1_cc);
                            }else {
                                if (str_country.toString().equals("Ringitt")) {
                                    insert1_cc = "R";
                                    insert1_rs = "RM.";
                                    inn.setText(insert1_cc);
                                    inn1.setText(insert1_cc);
                                    inn2.setText(insert1_cc);
                                }else {
                                    if (str_country.toString().equals("Rial")) {
                                        insert1_cc = "R";
                                        insert1_rs = "OR.";
                                        inn.setText(insert1_cc);
                                        inn1.setText(insert1_cc);
                                        inn2.setText(insert1_cc);
                                    }else {
                                        if (str_country.toString().equals("Yen")) {
                                            insert1_cc = "\u00a5";
                                            insert1_rs = "\u00a5";
                                            inn.setText(insert1_cc);
                                            inn1.setText(insert1_cc);
                                            inn2.setText(insert1_cc);
                                        }else {
                                            if (str_country.toString().equals("Papua New Guinean")) {
                                                insert1_cc = "K";
                                                insert1_rs = "K.";
                                                inn.setText(insert1_cc);
                                                inn1.setText(insert1_cc);
                                                inn2.setText(insert1_cc);
                                            }else {
                                                if (str_country.toString().equals("UAE")) {
                                                    insert1_cc = "D";
                                                    insert1_rs = "DH.";
                                                    inn.setText(insert1_cc);
                                                    inn1.setText(insert1_cc);
                                                    inn2.setText(insert1_cc);
                                                }else {
                                                    if (str_country.toString().equals("South African Rand")) {
                                                        insert1_cc = "R";
                                                        insert1_rs = "R.";
                                                        inn.setText(insert1_cc);
                                                        inn1.setText(insert1_cc);
                                                        inn2.setText(insert1_cc);
                                                    }else {
                                                        if (str_country.toString().equals("Congolese Franc")) {
                                                            insert1_cc = "F";
                                                            insert1_rs = "FC.";
                                                            inn.setText(insert1_cc);
                                                            inn1.setText(insert1_cc);
                                                            inn2.setText(insert1_cc);
                                                        }else {
                                                            if (str_country.toString().equals("Qatari Riyals")) {
                                                                insert1_cc = "QAR";
                                                                insert1_rs = "QAR.";
                                                                inn.setText(insert1_cc);
                                                                inn1.setText(insert1_cc);
                                                                inn2.setText(insert1_cc);
                                                            }else {
                                                                if (str_country.toString().equals("Dirhams")) {
                                                                    insert1_cc = "AED";
                                                                    insert1_rs = "AED.";
                                                                    inn.setText(insert1_cc);
                                                                    inn1.setText(insert1_cc);
                                                                    inn2.setText(insert1_cc);
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

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Vendor_temp_list");
        getContentResolver().delete(contentUri, null,null);
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("Vendor_temp_list")
                .appendQueryParameter("operation", "delete")
                .appendQueryParameter(null, null)
                .build();
        getContentResolver().notifyChange(resultUri, null);

        vendor_list = (ListView) findViewById(R.id.vendor_list);
        searchView = (EditText) findViewById(R.id.searchView);
        text2 = (TextView) findViewById(R.id.text2);

        paid_sum = (TextView) findViewById(R.id.paid_sum);
        pending_sum = (TextView) findViewById(R.id.pending_sum);
        total_sum = (TextView) findViewById(R.id.total_sum);


        DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
        downloadMusicfromInternet.execute();


        ImageView delete_icon = (ImageView) findViewById(R.id.delete_icon);
        delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setText("");
            }
        });

        LinearLayout linearLayout_hist = (LinearLayout) findViewById(R.id.linearLayout_hist);
        linearLayout_hist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inventory_Indent_Vendor_list.this, Inventory_Indent_History.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        LinearLayout linearLayout_add_vendor = (LinearLayout) findViewById(R.id.linearLayout_add_vendor);
        linearLayout_add_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog_add_vendor = new Dialog(Inventory_Indent_Vendor_list.this, R.style.timepicker_date_dialog);
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
                            Cursor cursor2 = db.rawQuery("SELECT * FROM Vendor_details WHERE vendor_phoneno = '"+phonenoedit.getText().toString()+"'", null);
                            if (cursor2.moveToFirst()){
                                phonenoedit_layout.setError("Phone no. already entered");
                            }else {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("vendor_name", nameedit.getText().toString());
                                contentValues.put("vendor_phoneno", phonenoedit.getText().toString());
                                contentValues.put("vendor_email", emailidedit.getText().toString());
                                contentValues.put("vendor_address", addressedit.getText().toString());
                                contentValues.put("vendor_gst", gsttinedit.getText().toString());
                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Vendor_details");
                                resultUri = getContentResolver().insert(contentUri, contentValues);
                                getContentResolver().notifyChange(resultUri, null);

//                                db.insert("Vendor_details", null, contentValues);

                                dialog_add_vendor.dismiss();
                            }
                        }
                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Vendor_temp_list");
                        getContentResolver().delete(contentUri, null,null);
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("Vendor_temp_list")
                                .appendQueryParameter("operation", "delete")
                                .appendQueryParameter(null, null)
                                .build();
                        getContentResolver().notifyChange(resultUri, null);

//                        db.delete("Vendor_temp_list", null, null);


                        DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
                        downloadMusicfromInternet.execute();


                    }
                });
            }
        });


        vendor_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Cursor cursor1 = (Cursor) adapterView.getItemAtPosition(i);
                final String c_ItemID = cursor1.getString(cursor1.getColumnIndex("vendor_name"));
                final String c_phoneno = cursor1.getString(cursor1.getColumnIndex("vend_phon"));
                final String c_email = cursor1.getString(cursor1.getColumnIndex("vend_email"));
                final String c_addr = cursor1.getString(cursor1.getColumnIndex("vend_address"));
                final String c_gst = cursor1.getString(cursor1.getColumnIndex("vend_gst"));

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Inventory_Indent_Vendor_list.this);
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Inventory_Indent_Vendor_list.this, android.R.layout.simple_selectable_list_item);
                arrayAdapter.add(getString(R.string.title16));
                arrayAdapter.add("Vendor history");

                alertDialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        final String strName = arrayAdapter.getItem(which);

                        if (strName.toString().equals(getString(R.string.title16))){
                            final Dialog dialog_add_vendor = new Dialog(Inventory_Indent_Vendor_list.this, R.style.timepicker_date_dialog);
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

                            nameedit.setText(c_ItemID);
                            phonenoedit.setText(c_phoneno);
                            emailidedit.setText(c_email);
                            addressedit.setText(c_addr);
                            gsttinedit.setText(c_gst);


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
                                        if (c_phoneno.toString().equals(phonenoedit.getText().toString())){
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("vendor_name", nameedit.getText().toString());
                                            contentValues.put("vendor_phoneno", phonenoedit.getText().toString());
                                            contentValues.put("vendor_email", emailidedit.getText().toString());
                                            contentValues.put("vendor_address", addressedit.getText().toString());
                                            contentValues.put("vendor_gst", gsttinedit.getText().toString());
                                            String where = "vendor_phoneno = '" + c_phoneno + "' ";

                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Vendor_details");
                                            getContentResolver().update(contentUri, contentValues, where, new String[]{});
                                            resultUri = new Uri.Builder()
                                                    .scheme("content")
                                                    .authority(StubProviderApp.AUTHORITY)
                                                    .path("Vendor_details")
                                                    .appendQueryParameter("operation", "update")
                                                    .appendQueryParameter("vendor_phoneno", c_phoneno)
                                                    .build();
                                            getContentResolver().notifyChange(resultUri, null);

//                                            db.update("Vendor_details", contentValues, where, new String[]{});

                                            Cursor cursor = db.rawQuery("SELECT * FROM Vendor_sold_details WHERE vendor_name = '"+c_ItemID+"' AND vendor_phoneno = '"+c_phoneno+"'", null);
                                            if (cursor.moveToFirst()){
                                                do {
                                                    String id = cursor.getString(0);

                                                    ContentValues contentValues1 = new ContentValues();
                                                    contentValues1.put("vendor_name", nameedit.getText().toString());
                                                    contentValues1.put("vendor_phoneno", phonenoedit.getText().toString());

                                                    String where1 = "_id = '" + id + "' ";

                                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Vendor_sold_details");
                                                    getContentResolver().update(contentUri, contentValues, where, new String[]{});
                                                    resultUri = new Uri.Builder()
                                                            .scheme("content")
                                                            .authority(StubProviderApp.AUTHORITY)
                                                            .path("Vendor_sold_details")
                                                            .appendQueryParameter("operation", "update")
                                                            .appendQueryParameter("_id", id)
                                                            .build();
                                                    getContentResolver().notifyChange(resultUri, null);

//                                                    db.update("Vendor_sold_details", contentValues1, where1, new String[]{});

                                                }while (cursor.moveToNext());
                                            }

                                            Cursor cursor1 = db.rawQuery("SELECT * FROM Vendor_sold_item_details WHERE vendor_name = '"+c_ItemID+"' AND vendor_phoneno = '"+c_phoneno+"'", null);
                                            if (cursor1.moveToFirst()){
                                                do {
                                                    String id = cursor1.getString(0);

                                                    ContentValues contentValues1 = new ContentValues();
                                                    contentValues1.put("vendor_name", nameedit.getText().toString());
                                                    contentValues1.put("vendor_phoneno", phonenoedit.getText().toString());

                                                    String where1 = "_id = '" + id + "' ";
                                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Vendor_sold_item_details");
                                                    getContentResolver().update(contentUri, contentValues, where, new String[]{});
                                                    resultUri = new Uri.Builder()
                                                            .scheme("content")
                                                            .authority(StubProviderApp.AUTHORITY)
                                                            .path("Vendor_sold_item_details")
                                                            .appendQueryParameter("operation", "update")
                                                            .appendQueryParameter("_id", id)
                                                            .build();
                                                    getContentResolver().notifyChange(resultUri, null);
//                                                    db.update("Vendor_sold_item_details", contentValues1, where1, new String[]{});

                                                }while (cursor1.moveToNext());
                                            }


                                            dialog_add_vendor.dismiss();
                                        }else {
                                            Cursor cursor2 = db.rawQuery("SELECT * FROM Vendor_details WHERE vendor_phoneno = '" + phonenoedit.getText().toString() + "'", null);
                                            if (cursor2.moveToFirst()) {
                                                phonenoedit_layout.setError("Phone no. already entered");
                                            } else {
                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("vendor_name", nameedit.getText().toString());
                                                contentValues.put("vendor_phoneno", phonenoedit.getText().toString());
                                                contentValues.put("vendor_email", emailidedit.getText().toString());
                                                contentValues.put("vendor_address", addressedit.getText().toString());
                                                contentValues.put("vendor_gst", gsttinedit.getText().toString());
                                                String where = "vendor_phoneno = '" + c_phoneno + "' ";
                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Vendor_details");
                                                getContentResolver().update(contentUri, contentValues, where, new String[]{});
                                                resultUri = new Uri.Builder()
                                                        .scheme("content")
                                                        .authority(StubProviderApp.AUTHORITY)
                                                        .path("Vendor_details")
                                                        .appendQueryParameter("operation", "update")
                                                        .appendQueryParameter("vendor_phoneno", c_phoneno)
                                                        .build();
                                                getContentResolver().notifyChange(resultUri, null);
//                                                db.update("Vendor_details", contentValues, where, new String[]{});


                                                Cursor cursor = db.rawQuery("SELECT * FROM Vendor_sold_details WHERE vendor_name = '"+c_ItemID+"' AND vendor_phoneno = '"+c_phoneno+"'", null);
                                                if (cursor.moveToFirst()){
                                                    do {
                                                        String id = cursor.getString(0);

                                                        ContentValues contentValues1 = new ContentValues();
                                                        contentValues1.put("vendor_name", nameedit.getText().toString());
                                                        contentValues1.put("vendor_phoneno", phonenoedit.getText().toString());

                                                        String where1 = "_id = '" + id + "' ";
                                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Vendor_sold_details");
                                                        getContentResolver().update(contentUri, contentValues, where, new String[]{});
                                                        resultUri = new Uri.Builder()
                                                                .scheme("content")
                                                                .authority(StubProviderApp.AUTHORITY)
                                                                .path("Vendor_sold_details")
                                                                .appendQueryParameter("operation", "update")
                                                                .appendQueryParameter("_id", id)
                                                                .build();
                                                        getContentResolver().notifyChange(resultUri, null);
//                                                        db.update("Vendor_sold_details", contentValues1, where1, new String[]{});

                                                    }while (cursor.moveToNext());
                                                }

                                                Cursor cursor1 = db.rawQuery("SELECT * FROM Vendor_sold_item_details WHERE vendor_name = '"+c_ItemID+"' AND vendor_phoneno = '"+c_phoneno+"'", null);
                                                if (cursor1.moveToFirst()){
                                                    do {
                                                        String id = cursor1.getString(0);

                                                        ContentValues contentValues1 = new ContentValues();
                                                        contentValues1.put("vendor_name", nameedit.getText().toString());
                                                        contentValues1.put("vendor_phoneno", phonenoedit.getText().toString());

                                                        String where1 = "_id = '" + id + "' ";
                                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Vendor_sold_item_details");
                                                        getContentResolver().update(contentUri, contentValues, where, new String[]{});
                                                        resultUri = new Uri.Builder()
                                                                .scheme("content")
                                                                .authority(StubProviderApp.AUTHORITY)
                                                                .path("Vendor_sold_item_details")
                                                                .appendQueryParameter("operation", "update")
                                                                .appendQueryParameter("_id", id)
                                                                .build();
                                                        getContentResolver().notifyChange(resultUri, null);
//                                                        db.update("Vendor_sold_item_details", contentValues1, where1, new String[]{});

                                                    }while (cursor1.moveToNext());
                                                }


                                                dialog_add_vendor.dismiss();
                                            }
                                        }
                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Vendor_temp_list");
                                        getContentResolver().delete(contentUri, null,null);
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProviderApp.AUTHORITY)
                                                .path("Vendor_temp_list")
                                                .appendQueryParameter("operation", "delete")
                                                .appendQueryParameter(null, null)
                                                .build();
                                        getContentResolver().notifyChange(resultUri, null);
//                                        db.delete("Vendor_temp_list", null, null);


                                        DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
                                        downloadMusicfromInternet.execute();

                                    }




                                }
                            });
                        }else {
                            Intent intent = new Intent(Inventory_Indent_Vendor_list.this, Inventory_Indent_Vendor_History.class);
                            intent.putExtra("PLAYER1NAME", c_ItemID);
                            intent.putExtra("PLAYER2NAME", c_phoneno);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }

                    }
                });

                alertDialog.show();


            }
        });


    }

    class DownloadMusicfromInternet extends AsyncTask<Void, Integer, Void> {
        private ProgressDialog dialog = new ProgressDialog(Inventory_Indent_Vendor_list.this, R.style.timepicker_date_dialog);

        @Override
        protected Void doInBackground(Void... params) {


            Cursor cursor = db.rawQuery("SELECT * FROM Vendor_details", null);
            if (cursor.moveToFirst()){
                do {
                    String name = cursor.getString(1);
                    String phon = cursor.getString(2);
                    String emai = cursor.getString(3);
                    String addr = cursor.getString(4);
                    String gsti = cursor.getString(5);


                    float co1 = 0, co2 = 0;
                    int co3 = 0;
                    Cursor cursor1 = db.rawQuery("SELECT * FROM Vendor_sold_details WHERE vendor_name = '"+name+"' AND vendor_phoneno = '"+phon+"' GROUP BY datetimee_new_from", null);
                    if (cursor1.moveToFirst()){
                        do {
                            String dateti = cursor1.getString(13);

                            Cursor cursor2 = db.rawQuery("SELECT * FROM Vendor_sold_details WHERE datetimee_new_from = '"+dateti+"'", null);
                            if (cursor2.moveToFirst()){
                                String tot = cursor2.getString(9);

                                co1 = co1+Float.parseFloat(tot);

                            }
                        }while (cursor1.moveToNext());
                    }

                    Cursor cursor2 = db.rawQuery("SELECT SUM(pay) FROM Vendor_sold_details WHERE vendor_name = '"+name+"' AND vendor_phoneno = '"+phon+"'", null);
                    if (cursor2.moveToFirst()){
                        co2 = cursor2.getFloat(0);
                    }

                    Cursor cursor3 = db.rawQuery("SELECT * FROM Vendor_sold_details WHERE vendor_name = '"+name+"' AND vendor_phoneno = '"+phon+"' GROUP BY datetimee_new_from", null);
                    if (cursor3.moveToFirst()){
                        do {
                            String dateti = cursor3.getString(13);

                            Cursor cursor4 = db.rawQuery("SELECT * FROM Vendor_sold_details WHERE datetimee_new_from = '"+dateti+"'", null);
                            if (cursor4.moveToFirst()){
                                String tot = cursor4.getString(9);

                                co3 = co3+1;

                            }
                        }while (cursor3.moveToNext());
                    }

                    float ac = co1 - co2;
                    String ac1 = String.format("%.1f", ac);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("vendor_name", name);
                    contentValues.put("vend_phon", phon);
                    contentValues.put("vend_email", emai);
                    contentValues.put("vend_gst", gsti);
                    contentValues.put("vend_address", addr);
                    contentValues.put("vend_total_bill_amount", String.valueOf(co1));
                    contentValues.put("paid", String.valueOf(co2));
                    contentValues.put("pending", ac1);
                    contentValues.put("bill_no", String.valueOf(co3));
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Vendor_temp_list");
                    resultUri = getContentResolver().insert(contentUri, contentValues);
                    getContentResolver().notifyChange(resultUri, null);



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

            vendor_list.setAdapter(null);

            final Cursor cursor_country1 = db.rawQuery("SELECT * FROM Country_Selection", null);
            if (cursor_country1.moveToFirst()){
                str_country = cursor_country1.getString(1);
            }

            Cursor cursor = db.rawQuery("SELECT * FROM Vendor_temp_list", null);
            String[] fromFieldNames = {"vendor_name", "vend_phon", "vend_email", "vend_gst", "vend_address", "vend_total_bill_amount", "bill_no", "paid", "pending", "pending", "pending", "pending"};
            int[] toViewsID = {R.id.itemname, R.id.phon, R.id.emai, R.id.gstin, R.id.addr, R.id.total_am, R.id.bill_nos, R.id.paid_am, R.id.pend_am, R.id.inn, R.id.inn1, R.id.inn2};
            final SimpleCursorAdapter ddataAdapterr = new SimpleCursorAdapter(Inventory_Indent_Vendor_list.this, R.layout.inventory_indent_vendor_listview, cursor, fromFieldNames, toViewsID, 0);
//            vendor_list.setAdapter(ddataAdapterr);
            ddataAdapterr.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                    if (view.getId() == R.id.inn || view.getId() == R.id.inn1 || view.getId() == R.id.inn2) {
                        final String tadl_id = cursor_country1.getString(cursor_country1.getColumnIndex("country"));
                        TextView dateTextView = (TextView) view;
                        if (tadl_id.toString().equals("India")){
                            dateTextView.setText(insert1_cc);
                        }else {
                            dateTextView.setText(insert1_cc);
                        }
                        return true;
                    }
                    return false;
                }
            });
            vendor_list.setAdapter(ddataAdapterr);

            searchView.addTextChangedListener(new TextWatcher() {

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


            Cursor cursor1 = db.rawQuery("SELECT COUNT(vendor_name) FROM Vendor_temp_list", null);
            if (cursor1.moveToFirst()){
                int level = cursor1.getInt(0);
                String total1 = String.valueOf(level);

                text2.setText(total1);
            }


            Cursor cursor2 = db.rawQuery("SELECT SUM(paid) FROM Vendor_temp_list", null);
            if (cursor2.moveToFirst()){
                float level = cursor2.getFloat(0);
                String total1 = String.valueOf(level);

                paid_sum.setText(total1);
            }

            Cursor cursor3 = db.rawQuery("SELECT SUM(pending) FROM Vendor_temp_list", null);
            if (cursor3.moveToFirst()){
                float level = cursor3.getFloat(0);
                String total1 = String.valueOf(level);

                pending_sum.setText(total1);
            }

            Cursor cursor4 = db.rawQuery("SELECT SUM(vend_total_bill_amount) FROM Vendor_temp_list", null);
            if (cursor4.moveToFirst()){
                float level = cursor4.getFloat(0);
                String total1 = String.valueOf(level);

                total_sum.setText(total1);
            }




        }
    }


    public Cursor fetchCountriesByName1(String inputtext) throws SQLException {

        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
            mCursor = db.rawQuery("SELECT * FROM Vendor_temp_list", null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Vendor_temp_list WHERE vendor_name LIKE '%" + inputtext + "%' OR vend_phon LIKE '%" + inputtext + "%'", null);
        }

        return mCursor;
    }

}
