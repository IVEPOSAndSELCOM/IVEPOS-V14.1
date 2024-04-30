package com.intuition.ivepos;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.intuition.ivepos.syncapp.StubProviderApp;

import java.io.ByteArrayOutputStream;

public class Tables_list extends AppCompatActivity {

    private SQLiteDatabase db,db1;
    int count;

    FloatingActionButton add_floor;

    RelativeLayout linearLayout;

    private ViewPager viewPager;
    private TabLayout mTabLayout;

    String title;
    int position;
    String position1;

    ProgressDialog dialog1;
    String table_iddd;

    Uri contentUri,resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        Bundle extras = getIntent().getExtras();
        position1 = extras.getString("position");

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        table_iddd = sh.getString("table", "");

        System.out.println("position tables_list "+table_iddd);

//        db.execSQL("CREATE TABLE if not exists Floors (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, floorname text, position text);");
        db.execSQL("CREATE TABLE if not exists asd1 (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, pName text, pDate text, image blob, floor text, position text, max text);");

//        Cursor cursor = db.rawQuery("SELECT * FROM floors", null);
//        if (cursor.moveToFirst()) {
//
//        }else {
//            ContentValues cv = new ContentValues();
//            cv.put("floorname", "first");
//            cv.put("position", "0");
//            db.insert("Floors", null, cv);
//
//            ContentValues cv1 = new ContentValues();
//            cv1.put("floorname", "second");
//            cv1.put("position", "1");
//            db.insert("Floors", null, cv1);
//        }

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//        Cursor nine = db.rawQuery("SELECT * FROM asd1 ", null);
//        if (nine.moveToFirst()) {
//
//        } else {
//            byte[] img;
//            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.table_rr);
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            b.compress(Bitmap.CompressFormat.PNG, 100, bos);
//            img = bos.toByteArray();
//
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("pName", "");
//            contentValues.put("pDate", "1");
//            contentValues.put("image", img);
//            contentValues.put("floor", "first");
//            contentValues.put("position", "0");
//            contentValues.put("max", "2");
//            db.insert("asd1", null, contentValues);
//
//            ContentValues contentValues1 = new ContentValues();
//            contentValues1.put("pName", "");
//            contentValues1.put("pDate", "2");
//            contentValues1.put("image", img);
//            contentValues1.put("floor", "first");
//            contentValues1.put("position", "0");
//            contentValues1.put("max", "4");
//            db.insert("asd1", null, contentValues1);
//
//            ContentValues contentValues11 = new ContentValues();
//            contentValues11.put("pName", "");
//            contentValues11.put("pDate", "3");
//            contentValues11.put("image", img);
//            contentValues11.put("floor", "first");
//            contentValues11.put("position", "0");
//            contentValues11.put("max", "6");
//            db.insert("asd1", null, contentValues11);
//
//            ContentValues contentValues2 = new ContentValues();
//            contentValues2.put("pName", "");
//            contentValues2.put("pDate", "4");
//            contentValues2.put("image", img);
//            contentValues2.put("floor", "second");
//            contentValues2.put("position", "1");
//            contentValues2.put("max", "6");
//            db.insert("asd1", null, contentValues2);
//        }
//        nine.close();

        linearLayout = (RelativeLayout) findViewById(R.id.add_item);

        ImageView linearLayout_settings = (ImageView) findViewById(R.id.linearLayout_settings);
        linearLayout_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    // only for Lollipo and newer versions
                    Intent intent = new Intent(Tables_list.this, Table_settings.class);
                    intent.putExtra("position", String.valueOf(position));
                    intent.putExtra("floor", title);
                    intent.putExtra("selected_section", String.valueOf(viewPager.getCurrentItem()));
                    startActivity(intent);

                    Toast.makeText(Tables_list.this, "title "+title+" "+position+" "+String.valueOf(viewPager.getCurrentItem()), Toast.LENGTH_LONG).show();
                }else {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(Tables_list.this);
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

                            final Dialog dialog = new Dialog(Tables_list.this, R.style.notitle);
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

                                                ProgressDialog dialog1 = new ProgressDialog(Tables_list.this, R.style.timepicker_date_dialog);
                                                dialog1.setMessage("Loading....");
                                                dialog1.setCanceledOnTouchOutside(false);
                                                dialog1.setCancelable(false);
                                                dialog1.show();

                                                final Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dialog1.dismiss();
                                                    }
                                                }, 1000); //3000 L = 3 detik

                                                finish();
                                                Intent intent_table = new Intent(Tables_list.this, Tables_list.class);
                                                intent_table.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent_table.putExtra("position", String.valueOf(pos));
                                                startActivity(intent_table);
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

        initViews();;

    }


    private void initViews() {
        System.out.println("tables_list1 initviews");

        // initialise the layout
        viewPager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tabs);

        // setOffscreenPageLimit means number
        // of tabs to be shown in one page
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // setCurrentItem as the tab position
                viewPager.setCurrentItem(tab.getPosition());
                position = tab.getPosition();
                title = tab.getText().toString();
//                Toast.makeText(Tables_list.this, "text "+title, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setDynamicFragmentToTabLayout();
    }

    // show all the tab using DynamicFragmnetAdapter
    private void setDynamicFragmentToTabLayout() {
        // here we have given 10 as the tab number
        // you can give any number here

        System.out.println("tables_list1 setDynamicFragmentToTabLayout");

        String floorname = "";
        Cursor cursor = db.rawQuery("SELECT * FROM Floors", null);
        if (cursor.moveToFirst()) {
            do {
                floorname = cursor.getString(1);
                mTabLayout.addTab(mTabLayout.newTab().setText(floorname));
            }while (cursor.moveToNext());
        }
        cursor.close();

//        Cursor cursor = db.rawQuery("SELECT Count(*) FROM Floors", null);
//        if (cursor.moveToFirst()) {
//            count = cursor.getInt(0);
//        }
//        cursor.close();
//
//        for (int i = 1; i <= count; i++) {
//            // set the tab name as "Page: " + i
//            mTabLayout.addTab(mTabLayout.newTab().setText("Page: " + i));
//        }

        DynamicFragmentAdapter mDynamicFragmentAdapter = new DynamicFragmentAdapter(getSupportFragmentManager(), mTabLayout.getTabCount(), title, table_iddd);

        // set the adapter
        viewPager.setAdapter(mDynamicFragmentAdapter);

        // set the current item as 0 (when app opens for first time)
        viewPager.setCurrentItem(Integer.parseInt(position1));

        System.out.println("tables_list1 setDynamicFragmentToTabLayout1");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("Tables list destroy");
        db.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("Tables list onstop");
        mTabLayout.removeAllTabs();
        db.close();
    }
}
