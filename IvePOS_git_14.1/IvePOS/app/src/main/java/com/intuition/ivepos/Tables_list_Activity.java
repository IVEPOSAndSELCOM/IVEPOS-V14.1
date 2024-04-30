package com.intuition.ivepos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Table;
import com.intuition.ivepos.syncapp.StubProviderApp;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Tables_list_Activity extends AppCompatActivity {

    private SQLiteDatabase db,db1;
    String floorname;
    String table_iddd, get_floor, get_position;
    GridView gridView;
    ImageCursorAdapter31 tableadapter;

    Cursor cursor;
    DynamicFragment.DownloadMusicfromInternet2 downloadMusicfromInternet;
    View view;

    int position;

    String position1, from;
    Uri contentUri,resultUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tables_list_activity);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        Bundle extras = getIntent().getExtras();
        position1 = extras.getString("position");
        from = extras.getString("from");
//        floorname = extras.getString("floorname");
//        table_iddd = extras.getString("table_iddd");

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        table_iddd = sh.getString("table", "");
        get_floor = sh.getString("floor", "");
        get_position = sh.getString("position", "");

        TextView tv = new TextView(Tables_list_Activity.this);
        tv.setText(get_floor);


        System.out.println("position tables_list_activity "+table_iddd+" "+get_floor);

        Spinner spinner = (Spinner) findViewById(R.id.chocolate_category);
        ArrayList<String> my_arrayy = getTableValues2();
        final ArrayAdapter my_Adapterr = new ArrayAdapter(this, R.layout.spinner_row,
                my_arrayy);
        spinner.setAdapter(my_Adapterr);


        if (from.toString().equals("Settings")) {
            spinner.setSelection(Integer.parseInt(position1));
            System.out.println("position tables_list_activity1 "+String.valueOf(Integer.parseInt(position1)));
        }else {
            if (tv.getText().toString().equals("")) {
                spinner.setSelection(Integer.parseInt(position1));
            }else {
                spinner.setSelection(Integer.parseInt(get_position));
            }
        }


        gridView = (GridView) findViewById(R.id.gridviewitems);

//        System.out.println("table is "+floorname);
//        cursor = db.rawQuery("Select * from asd1 WHERE position = '"+position1+"'", null);
//        String[] fromFieldNames = {"pName", "_id", "floor", "max"};
//        int[] toViewsID = {R.id.name};
//        Log.e("Checamos que hay id", String.valueOf(R.id.name));
//        ImageCursorAdapter31 tableadapter = new ImageCursorAdapter31(Tables_list_Activity.this, R.layout.tables_management_listview, cursor, fromFieldNames, toViewsID, table_iddd);
//
//        gridView.setAdapter(tableadapter);// Assign adapter to ListView.... here... the bitch error

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor5 = (Cursor) parent.getItemAtPosition(position);
                int item_content_id = cursor5.getInt(cursor5.getColumnIndex("_id"));
                String floorname = cursor5.getString(cursor5.getColumnIndex("floor"));
                String pDate = cursor5.getString(cursor5.getColumnIndex("pDate"));
                String present = cursor5.getString(cursor5.getColumnIndex("present"));

                TextView name = (TextView) view.findViewById(R.id.name);

//                Toast.makeText(Tables_list_Activity.this, "text "+floorname+" "+String.valueOf(item_content_id)+" present "+present, Toast.LENGTH_LONG).show();

                TextView tv = new TextView(Tables_list_Activity.this);
                tv.setText(present);

                if (tv.getText().toString().equals("") || tv.getText().toString().equals("0")) {
                    final Dialog dialog = new Dialog(Tables_list_Activity.this, R.style.notitle);
                    dialog.setContentView(R.layout.tab_management_dialog_empty_click);
                    dialog.show();

                    EditText pax_occupied = (EditText) dialog.findViewById(R.id.pax_occupied);

                    Cursor cursor1 = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + item_content_id + "'", null);
                    if (cursor1.moveToFirst()) {
                        String pax = cursor1.getString(7);
                        pax_occupied.setText(pax);
                    }
                    cursor1.close();

                    TextView table = (TextView) dialog.findViewById(R.id.table);
                    table.setText("Tab" + pDate);

                    TextView table_name = (TextView) dialog.findViewById(R.id.table_name);
                    table_name.setText(name.getText().toString());

                    ImageButton btncancel = (ImageButton) dialog.findViewById(R.id.btncancel);
                    btncancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hideKeyboard(Tables_list_Activity.this);
                            donotshowKeyboard(Tables_list_Activity.this);
                            dialog.dismiss();
                        }
                    });

                    ImageView btnsave = (ImageView) dialog.findViewById(R.id.btnsave);
                    btnsave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (pax_occupied.getText().toString().equals("") || pax_occupied.getText().toString().equals("0")) {
                                pax_occupied.setError("Enter valid number");
                            } else {
                                db.execSQL("UPDATE asd1 SET present = '" + pax_occupied.getText().toString() + "' WHERE _id = '" + item_content_id + "'");
                                dialog.dismiss();

//                            cursor5.moveToPosition(position);
//                            cursor5.requery();
//                            tableadapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putString("table", String.valueOf(item_content_id));
                                myEdit.putString("floor", spinner.getSelectedItem().toString());
                                myEdit.putString("position", String.valueOf(spinner.getSelectedItemPosition()));
                                myEdit.putString("table_number", pDate);
                                myEdit.apply();

                                Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                                if (cursor3.moveToFirst()) {
                                    String lite_pro = cursor3.getString(1);

                                    TextView tv = new TextView(Tables_list_Activity.this);
                                    tv.setText(lite_pro);

                                    if (tv.getText().toString().equals("Lite")) {
                                        Intent intent = new Intent(Tables_list_Activity.this, BeveragesMenuFragment_Dine_l.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(Tables_list_Activity.this, BeveragesMenuFragment_Dine.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                }else {
                                    Intent intent = new Intent(Tables_list_Activity.this, BeveragesMenuFragment_Dine_l.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }


                            }
                        }
                    });
                }else {
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("table", String.valueOf(item_content_id));
                    myEdit.putString("floor", spinner.getSelectedItem().toString());
                    myEdit.putString("position", String.valueOf(spinner.getSelectedItemPosition()));
                    myEdit.putString("table_number", pDate);
                    myEdit.apply();

                    Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                    if (cursor3.moveToFirst()) {
                        String lite_pro = cursor3.getString(1);

                        TextView ttv = new TextView(Tables_list_Activity.this);
                        ttv.setText(lite_pro);

                        if (ttv.getText().toString().equals("Lite")) {
                            Intent intent = new Intent(Tables_list_Activity.this, BeveragesMenuFragment_Dine_l.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(Tables_list_Activity.this, BeveragesMenuFragment_Dine.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }else {
                        Intent intent = new Intent(Tables_list_Activity.this, BeveragesMenuFragment_Dine_l.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }

            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                System.out.println("Selected "+selected);

                cursor = db.rawQuery("Select * from asd1 WHERE floor = '"+selected+"'", null);
                String[] fromFieldNames = {"pName", "_id", "floor", "max"};
                int[] toViewsID = {R.id.name};
                Log.e("Checamos que hay id", String.valueOf(R.id.name));
                ImageCursorAdapter31 tableadapter = new ImageCursorAdapter31(Tables_list_Activity.this, R.layout.tables_management_listview, cursor, fromFieldNames, toViewsID, table_iddd);

                gridView.setAdapter(tableadapter);// Assign adapter to ListView.... here... the bitch error

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ImageView linearLayout_settings = (ImageView) findViewById(R.id.linearLayout_settings);
        linearLayout_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    // only for Lollipo and newer versions
                    Intent intent = new Intent(Tables_list_Activity.this, Table_settings.class);
                    intent.putExtra("position", String.valueOf(spinner.getSelectedItemPosition()));
                    intent.putExtra("floor", spinner.getSelectedItem().toString());
                    intent.putExtra("selected_section", String.valueOf(spinner.getSelectedItemPosition()));
                    startActivity(intent);

//                    Toast.makeText(Tables_list_Activity.this, "title "+spinner.getSelectedItem().toString()+" "+String.valueOf(spinner.getSelectedItemPosition())+" "+String.valueOf(spinner.getSelectedItemPosition()), Toast.LENGTH_LONG).show();
                }else {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(Tables_list_Activity.this);
                    //Setting message manually and performing action on button click
                    builder.setMessage("Your device android version won't support this feature.\nKindly upgrade it to enjoy this feature.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle(getString(R.string.title9));
                    alert.show();
                }

            }
        });


        ImageView linearLayout_overflow = findViewById(R.id.linearLayout_overflow);
        final PopupMenu popup = new PopupMenu(this, linearLayout_overflow);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.table_menu, popup.getMenu());
        linearLayout_overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int id = item.getItemId();
                        if (id == R.id.linearLayout_addfloor) {

//                            linearLayout.setVisibility(View.VISIBLE);
//                            add_floor.setVisibility(View.GONE);

                            final Dialog dialog = new Dialog(Tables_list_Activity.this, R.style.notitle);
                            dialog.setContentView(R.layout.dialog_add_section);
                            dialog.show();

                            final EditText editText_section_name = (EditText) dialog.findViewById(R.id.editText_section_name);
                            final EditText enter_no_of_tables = (EditText) dialog.findViewById(R.id.enter_no_of_tables);

                            ImageButton btncancel = (ImageButton) dialog.findViewById(R.id.btncancel);
                            btncancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            ImageView btnsave = (ImageView) dialog.findViewById(R.id.btnsave);
                            btnsave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (editText_section_name.getText().toString().equals("")) {
                                        editText_section_name.setError("Enter valid Section name");
                                    }else {
                                        if (enter_no_of_tables.getText().toString().equals("") || enter_no_of_tables.getText().toString().equals("0")) {
                                            enter_no_of_tables.setError("Enter minimum 1 table");
                                        }else {
                                            Cursor cursor1 = db.rawQuery("SELECT * FROM Floors WHERE floorname = '"+editText_section_name.getText().toString()+"'", null);
                                            if (cursor1.moveToFirst()) {
                                                editText_section_name.setError("Section name already used");
                                            }else {
                                                int pos = 0;
                                                Cursor cursor2 = db.rawQuery("SELECT MAX(position) FROM Floors", null);
                                                if (cursor2.moveToFirst()) {
                                                    pos = cursor2.getInt(0);
                                                }
                                                cursor2.close();
                                                pos = pos+1;
                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("floorname", editText_section_name.getText().toString());
                                                contentValues.put("position", String.valueOf(pos));
                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "floors");
                                                resultUri = getContentResolver().insert(contentUri, contentValues);
                                                getContentResolver().notifyChange(resultUri, null);
//                                                db.insert("Floors", null, cv);


//                                                for (int i=0; i<Integer.parseInt(enter_no_of_tables.getText().toString()); i++) {
                                                for (int i = 1; i <= Integer.parseInt(enter_no_of_tables.getText().toString()); i++) {
                                                    Cursor cursorr = db.rawQuery("SELECT _id FROM asd1", null);
                                                    int count = cursorr.getCount();
                                                    int countt = count + 1;
                                                    String count2 = String.valueOf(countt);

                                                    Cursor cursorr1 = db.rawQuery("SELECT _id FROM asd1 WHERE position = '"+String.valueOf(pos)+"'", null);
                                                    int count1 = cursorr1.getCount();
                                                    int countt1 = count1 + 1;
                                                    String count21 = String.valueOf(countt1);

                                                    byte[] byteImage1;
                                                    byte[] img;
                                                    ContentValues newValues = new ContentValues();
                                                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.c_table_empty_normal_6d6e71);
                                                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                                    b.compress(Bitmap.CompressFormat.PNG, 100, bos);
                                                    img = bos.toByteArray();
                                                    newValues.put("image", img);
//                                                        newValues.put("_id", count2);
                                                    newValues.put("pName", "");
                                                    newValues.put("pDate", count21);
                                                    newValues.put("floor", editText_section_name.getText().toString());
                                                    newValues.put("position", String.valueOf(pos));
                                                    newValues.put("max", "4");
                                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "asd1");
                                                    resultUri = getContentResolver().insert(contentUri, newValues);
                                                    getContentResolver().notifyChange(resultUri, null);
//                                                        db.insert("asd1", null, newValues);

                                                }
//                                                }


//                                                initViews();



                                                dialog.dismiss();

                                                ProgressDialog dialog1 = new ProgressDialog(Tables_list_Activity.this, R.style.timepicker_date_dialog);
                                                dialog1.setMessage("Loading....");
                                                dialog1.setCanceledOnTouchOutside(false);
                                                dialog1.setCancelable(false);
                                                dialog1.show();

                                                final Handler handler = new Handler();
                                                int finalPos = pos;
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dialog1.dismiss();

                                                        finish();
                                                        Intent intent_table = new Intent(Tables_list_Activity.this, Tables_list_Activity.class);
                                                        intent_table.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        intent_table.putExtra("position", String.valueOf(finalPos));
                                                        intent_table.putExtra("from", "Same");
                                                        startActivity(intent_table);
                                                    }
                                                }, 1000); //3000 L = 3 detik


                                            }
                                            cursor1.close();
                                        }
                                    }


                                }
                            });

                        }

                        return true;
                    }
                });
            }
        });

        ImageButton btncancel = (ImageButton) findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                if (cursor3.moveToFirst()) {
                    String lite_pro = cursor3.getString(1);

                    TextView tv = new TextView(Tables_list_Activity.this);
                    tv.setText(lite_pro);

                    if (tv.getText().toString().equals("Lite")) {
                        Intent intent = new Intent(Tables_list_Activity.this, BeveragesMenuFragment_Dine_l.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(Tables_list_Activity.this, BeveragesMenuFragment_Dine.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }else {
                    Intent intent = new Intent(Tables_list_Activity.this, BeveragesMenuFragment_Dine_l.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

    }

    public ArrayList<String> getTableValues2() {
        ArrayList<String> my_array = new ArrayList<String>();
        try {
//            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor allrows = db.rawQuery("SELECT * FROM floors", null);
            System.out.println("COUNT : " + allrows.getCount());
            if (allrows.moveToFirst()) {
                do {
                    String ID = allrows.getString(0);
                    String NAME = allrows.getString(1);

                    my_array.add(NAME);

                } while (allrows.moveToNext());
            }
            allrows.close();
            //db.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error encountered.",
                    Toast.LENGTH_LONG);
        }
        return my_array;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void donotshowKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
        if (cursor3.moveToFirst()) {
            String lite_pro = cursor3.getString(1);

            TextView tv = new TextView(Tables_list_Activity.this);
            tv.setText(lite_pro);

            if (tv.getText().toString().equals("Lite")) {
                Intent intent = new Intent(Tables_list_Activity.this, BeveragesMenuFragment_Dine_l.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else {
                Intent intent = new Intent(Tables_list_Activity.this, BeveragesMenuFragment_Dine.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }else {
            Intent intent = new Intent(Tables_list_Activity.this, BeveragesMenuFragment_Dine_l.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
