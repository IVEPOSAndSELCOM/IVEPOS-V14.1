package com.intuition.ivepos;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.intuition.ivepos.csv.RequestSingleton;
import com.intuition.ivepos.syncapp.StubProviderApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

public class Taxes_Multiselection_Items extends AppCompatActivity {

    public SQLiteDatabase db = null;

    private LinkedHashMap<Item, ArrayList<Item>> groupList;
    ArrayList<String> checking;
    String groupName, select;

    RelativeLayout progressbar;
    String co = "0";
    Uri contentUri,resultUri;
    String player1name, taxname;

    private ArrayList<Item> item;
    ExpandableListView expandableListView;

    TextView selection;
    LinearLayout btnsave;

    String WebserviceUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandablelist_items_categories);

        progressbar = (RelativeLayout) findViewById(R.id.progressbar);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);


        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(Taxes_Multiselection_Items.this);
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

        expandableListView = (ExpandableListView) findViewById(R.id.expandablelist);
        selection = (TextView) findViewById(R.id.selection);

        Bundle extras = getIntent().getExtras();
        player1name = extras.getString("PLAYER1NAME");
        taxname = extras.getString("taxname");

        LinearLayout back_activity = (LinearLayout) findViewById(R.id.back_activity);
        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE status_temp = 'yes' || status_perm = 'yes'", null);
                if (cursor.moveToFirst()){
                    do {
                        String id = cursor.getString(0);

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("status_temp", "");
                        contentValues.put("status_perm", "");
                        String where = " _id ='" + id + "'";

                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                        getContentResolver().update(contentUri, contentValues,where,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("Items")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id",id)
                                .build();
                        getContentResolver().notifyChange(resultUri, null);
                        String where1_v1 = "docid = '" + id + "' ";
                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

//                        db.update("Items", contentValues, where, new String[]{});
                    }while (cursor.moveToNext());
                }
                cursor.close();

                finish();
            }
        });

//        Cursor filter_cat2 = db.rawQuery("SELECT * FROM Hotel1", null);
//        if (filter_cat2.moveToFirst()){
//            do {
//                String cat_name = filter_cat2.getString(0);
//
//                String where = "_id = '" + cat_name + "' ";
//                //    db.delete("Hotel1", where, new String[]{});
//
//
//                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Hotel1");
//                getContentResolver().delete(contentUri, where, new String[]{});
//                resultUri = new Uri.Builder()
//                        .scheme("content")
//                        .authority(StubProviderApp.AUTHORITY)
//                        .path("Hotel1")
//                        .appendQueryParameter("operation", "delete")
//                        .appendQueryParameter("_id", cat_name)
//                        .build();
//                getContentResolver().notifyChange(resultUri, null);
//
//
//            }while(filter_cat2.moveToNext());
//        }
//        filter_cat2.close();
//
//
//        Cursor filter_cat3 = db.rawQuery("SELECT * FROM Hotel", null);
//        if (filter_cat3.moveToFirst()){
////            do {
//            String cat_name = filter_cat3.getString(1);
//            String cat_value = filter_cat3.getString(5);
//
//            ContentValues cv = new ContentValues();
//
//            cv.put("name", "All");
//            cv.put("value", "");
//            //  db.insert("Hotel1", null, cv);
//
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Hotel1");
//            resultUri = getContentResolver().insert(contentUri, cv);
//            getContentResolver().notifyChange(resultUri, null);
//
//
//            cv.put("name", "Favourites");
//            cv.put("value", "");
//
//            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Hotel1");
//            resultUri = getContentResolver().insert(contentUri, cv);
//            getContentResolver().notifyChange(resultUri, null);
//            //   db.insert("Hotel1", null, cv);
////            }while(filter_cat3.moveToNext());
//        }
//        filter_cat3.close();
//
//        Cursor filter_cat4 = db.rawQuery("SELECT * FROM Hotel order by value ASC", null);
//        if (filter_cat4.moveToFirst()){
//            do {
//                String cat_name = filter_cat4.getString(1);
//                String cat_value = filter_cat4.getString(5);
//
//                ContentValues cv = new ContentValues();
//                if (cat_name.toString().equals("All")){
////                    cv.put("name", "All");
//                }else {
//                    if (cat_name.toString().equals("Favourites")) {
////                    cv.put("name", "Favourites");
//                    }else {
//
//                        TextView cat_value1 = new TextView(Taxes_Multiselection_Items.this);
//                        cat_value1.setText(cat_value);
////                Cursor filter_cat5 = db.rawQuery("SELECT * FROM Hotel WHERE '"+cat_value1.getText().toString()+"' != '' ", null);
//                        if (!cat_value1.getText().toString().equals("")) {
//                            cv.put("name", cat_name);
//                            cv.put("value", cat_value1.getText().toString());
//
//                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Hotel1");
//                            resultUri = getContentResolver().insert(contentUri, cv);
//                            getContentResolver().notifyChange(resultUri, null);
//                        }
//
//                        //  db.insert("Hotel1", null, cv);
//                    }
//                }
//            }while(filter_cat4.moveToNext());
//        }
//        filter_cat4.close();
//
//        Cursor filter_cat = db.rawQuery("SELECT * FROM Hotel ORDER BY name ASC", null);
//        if (filter_cat.moveToFirst()){
//            do {
//                String cat_name = filter_cat.getString(1);
//                String cat_value = filter_cat.getString(5);
//
//                ContentValues cv = new ContentValues();
//                if (cat_name.toString().equals("All")){
////                    cv.put("name", "All");
//                }else {
//                    if (cat_name.toString().equals("Favourites")) {
////                    cv.put("name", "Favourites");
//                    }else {
//
//                        TextView cat_value1 = new TextView(Taxes_Multiselection_Items.this);
//                        cat_value1.setText(cat_value);
////                Cursor filter_cat5 = db.rawQuery("SELECT * FROM Hotel WHERE '"+cat_value1.getText().toString()+"' = '' ", null);
//                        if (cat_value1.getText().toString().equals("")) {
//                            cv.put("name", cat_name);
//                            cv.put("value", cat_value1.getText().toString());
//                        }
//                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Hotel1");
//                        resultUri = getContentResolver().insert(contentUri, cv);
//                        getContentResolver().notifyChange(resultUri, null);
//
//                        //  db.insert("Hotel1", null, cv);
//                    }
//                }
//            }while(filter_cat.moveToNext());
//        }
//        filter_cat.close();

        DownloadMusicfromInternetde_update01 downloadMusicfromInternetde_update01 = new DownloadMusicfromInternetde_update01();
        downloadMusicfromInternetde_update01.execute();

//        DownloadMusicfromInternetde downloadMusicfromInternetde = new DownloadMusicfromInternetde();
//        downloadMusicfromInternetde.execute();


        btnsave = (LinearLayout) findViewById(R.id.btnn);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadMusicfromInternetdel downloadMusicfromInternetdel = new DownloadMusicfromInternetdel();
                downloadMusicfromInternetdel.execute();
            }
        });

    }

    private ArrayList<Item> fetchGroups() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //coding 29/1/2018......
//        Bundle extras = getIntent().getExtras();
        String hi = "";

        ArrayList<Item> groupList = new ArrayList<Item>();

//        Cursor cursor = db.rawQuery("SELECT * FROM Hotel1", null);//removed after hotel1
        Cursor cursor = db.rawQuery("SELECT * FROM Hotel", null);
        if (cursor.moveToFirst()) {
            do {
                hi = cursor.getString(1);
                System.out.println("category is "+hi);

                Item item = new Item();

                if (hi.toString().equals("Favourites")){

                }else {

                    if (hi.toString().equals("All")) {

                        checking = new ArrayList<String>();


                        item.id = groupName = "" + "None";
                        item.name = groupName;
                        groupList.add(item);

                    } else {

                        checking = new ArrayList<String>();
//                    Item item = new Item();

                        item.id = groupName = "" + hi;
                        item.name = groupName;
                        groupList.add(item);

                        checking.add(groupName);

//                        Collections.sort(groupList, new Comparator<Item>() {
//
//                            public int compare(Item item1, Item item2) {
//
//                                return item2.name.compareTo(item1.name) < 0
//                                        ? 0 : -1;
//                            }
//                        });

                    }
                }
            }while (cursor.moveToNext());
        }
        cursor.close();

//        checking = new ArrayList<String>();
//        Item item = new Item();
//
//        item.id = groupName = "" + "None";
//        item.name = groupName;
//        groupList.add(item);

        return groupList;

    }


    private ArrayList<Item> fetchGroupMembers(String groupId) {

        StrictMode.ThreadPolicy policy1 = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy1);
        String hi1 = "";

        ArrayList<Item> groupMembers = new ArrayList<Item>();

        Log.i("GroupsListSizehi ", "");

        System.out.println("category is1 "+groupId);

        int ni= 1;
        Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE category = '"+groupId+"'", null);
        if (cursor.moveToFirst()){
            do {
                hi1 = cursor.getString(1);
                System.out.println("Itemname is "+hi1);
                System.out.println("total Itemname is "+ni);
                String tax1 = cursor.getString(5);
                String tax2 = cursor.getString(28);
                String tax3 = cursor.getString(30);
                String tax4 = cursor.getString(32);
                String tax5 = cursor.getString(34);

                TextView tax1_t = new TextView(Taxes_Multiselection_Items.this);
                tax1_t.setText(tax1);
                TextView tax2_t = new TextView(Taxes_Multiselection_Items.this);
                tax2_t.setText(tax2);
                TextView tax3_t = new TextView(Taxes_Multiselection_Items.this);
                tax3_t.setText(tax3);
                TextView tax4_t = new TextView(Taxes_Multiselection_Items.this);
                tax4_t.setText(tax4);
                TextView tax5_t = new TextView(Taxes_Multiselection_Items.this);
                tax5_t.setText(tax5);

                if (tax1_t.getText().toString().equals("") || tax2_t.getText().toString().equals("") || tax3_t.getText().toString().equals("") ||
                        tax4_t.getText().toString().equals("") || tax5_t.getText().toString().equals("") ||
                        tax1_t.getText().toString().equals("None") || tax2_t.getText().toString().equals("None") || tax3_t.getText().toString().equals("None") ||
                        tax4_t.getText().toString().equals("None") || tax5_t.getText().toString().equals("None")||
                        tax1_t.getText().toString().equals("NONE") || tax2_t.getText().toString().equals("NONE") || tax3_t.getText().toString().equals("NONE") ||
                        tax4_t.getText().toString().equals("NONE") || tax5_t.getText().toString().equals("NONE")) {
                    Log.i("GroupsListSizehi2 ", "");

                    System.out.println("checking.size() "+checking.size());

//                    for (int i = 0; i < checking.size(); i++) {
//                        String s = checking.get(i);

                    Log.i("GroupsListSizehi3 "+groupId+" ", hi1);
//                        hi1 = hi1.replace(s, "");

                    Log.i("GroupsListSizehi4 "+groupId+" ", hi1);


                    Item item = new Item();
                    String groupName;
                    item.id = groupName = "" + hi1;
                    item.name = groupName;

                    groupMembers.add(item);
//                    }
                }

                ni++;
            }while (cursor.moveToNext());
        }
        cursor.close();

        return groupMembers;
    }

    class DownloadMusicfromInternetdel extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            try {



                String webservicequery1="update Items set status_perm='yes' where status_temp = 'yes'";
                try {
                    db.execSQL(webservicequery1);
                    Log.e("webservicequery",webservicequery1);
                } catch (SQLException e) {
                    Log.e("webs-exception",webservicequery1);
                    e.printStackTrace();
                }
                webservicequery(webservicequery1);

                String webservicequery2="update Items set status_perm='' where status_temp = '' OR status_temp = '0'";
                try {
                    db.execSQL(webservicequery2);
                    Log.e("webservicequery",webservicequery2);
                } catch (SQLException e) {
                    Log.e("webs-exception",webservicequery2);
                    e.printStackTrace();
                }
                webservicequery(webservicequery2);


//                Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE status_temp = 'yes'", null);
//                if (cursor.moveToFirst()){
//                    do {
//                        String id = cursor.getString(0);
//
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("status_perm", "yes");
//                        String where = " _id ='" + id + "'";
//                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                        getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                        resultUri = new Uri.Builder()
//                                .scheme("content")
//                                .authority(StubProviderApp.AUTHORITY)
//                                .path("Items")
//                                .appendQueryParameter("operation", "update")
//                                .appendQueryParameter("_id",id)
//                                .build();
//                        getContentResolver().notifyChange(resultUri, null);
////                        db.update("Items", contentValues, where, new String[]{});
//
//                        String where1_v1 = "docid = '" + id + "' ";
//                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//
//                    }while (cursor.moveToNext());
//                }
//                cursor.close();

                Cursor cursor1 = db.rawQuery("SELECT COUNT(status_temp) FROM Items WHERE status_temp = 'yes'", null);
                if (cursor1.moveToFirst()){
                    int l = cursor1.getInt(0);
                    co = String.valueOf(l);
                }
                cursor1.close();

            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Integer file_url) {
            progressbar.setVisibility(View.GONE);



            Intent intent = new Intent();
            intent.putExtra("editTextValue", co);
            intent.putExtra("player1name", player1name);
//                Toast.makeText(Add_Items_List1.this, "1q "+items_count.getText().toString(), Toast.LENGTH_LONG).show();
            setResult(RESULT_OK, intent);
            finish();

        }
    }


    class DownloadMusicfromInternetde_update01 extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            try {
//                Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
//                if (cursor.moveToFirst()) {
//                    do {
//                        String id = cursor.getString(0);
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("status_temp", "");
//                        contentValues.put("status_perm", "");
//                        String where = "_id = '"+id+"' ";
//                        db.update("Items", contentValues, where, new String[]{});
//                    } while (cursor.moveToNext());
//                }
//
//                cursor.close();

//                db.execSQL("UPDATE Items SET status_temp = '', status_perm = ''");

                db.execSQL("UPDATE Items SET status_temp = '' AND status_perm = ''");
//                db.execSQL("UPDATE Items SET status_perm = ''");

                webservicequery("UPDATE Items SET status_temp = '' AND status_perm = ''");

//                Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
//                if (cursor.moveToFirst()){
//                    do {
//                        String id = cursor.getString(0);
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("status_temp", "");
//                        contentValues.put("status_perm", "");
//                        String where = "_id = '"+id+"' ";
//                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                        getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                        resultUri = new Uri.Builder()
//                                .scheme("content")
//                                .authority(StubProviderApp.AUTHORITY)
//                                .path("Items")
//                                .appendQueryParameter("operation", "update")
//                                .appendQueryParameter("_id",id)
//                                .build();
//                        getContentResolver().notifyChange(resultUri, null);
////
//                        String where1_v1 = "docid = '" + id + "' ";
//                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//                    }while (cursor.moveToNext());
//                }
//                cursor.close();

            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Integer file_url) {
            DownloadMusicfromInternetde downloadMusicfromInternetde = new DownloadMusicfromInternetde();
            downloadMusicfromInternetde.execute();
        }
    }


    class DownloadMusicfromInternetde extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            try {

                if (player1name.equals("insert")){
//                    Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE status_temp = 'yes'", null);
//                    if (cursor.moveToFirst()){
//                        do {
//                            String id = cursor.getString(0);
//
//                            ContentValues contentValues = new ContentValues();
//                            contentValues.put("status_temp", "");
//                            contentValues.put("status_perm", "");
//                            String where = " _id ='" + id + "'";
//                            db.update("Items", contentValues, where, new String[]{});
//                        }while (cursor.moveToNext());
//                    }
//                    cursor.close();

                }else {

//                    Cursor cursor0 = db.rawQuery("SELECT * FROM Items WHERE status_temp = 'yes'", null);
//                    if (cursor0.moveToFirst()){
//                        do {
//                            String id = cursor0.getString(0);
//
//                            ContentValues contentValues = new ContentValues();
//                            contentValues.put("status_temp", "");
//                            contentValues.put("status_perm", "");
//                            String where = " _id ='" + id + "'";
//                            db.update("Items", contentValues, where, new String[]{});
//                        }while (cursor0.moveToNext());
//                    }
//                    cursor0.close();

//                    Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE itemtax = '"+taxname+"'", null);
//                    if (cursor.moveToFirst()){
//                        do {
//                            String id = cursor.getString(0);
//                            System.out.println("itemname is "+id);
//                            ContentValues contentValues = new ContentValues();
//                            contentValues.put("status_temp", "yes");
//                            contentValues.put("status_perm", "yes");
//                            String where = " _id ='" + id + "'";
//                            db.update("Items", contentValues, where, new String[]{});
//                        }while (cursor.moveToNext());
//                    }
//                    cursor.close();
//
//                    Cursor cursor2 = db.rawQuery("SELECT * FROM Items WHERE itemtax2 = '"+taxname+"'", null);
//                    if (cursor2.moveToFirst()){
//                        do {
//                            String id = cursor2.getString(0);
//                            System.out.println("itemname is2 "+id);
//                            ContentValues contentValues = new ContentValues();
//                            contentValues.put("status_temp", "yes");
//                            contentValues.put("status_perm", "yes");
//                            String where = " _id ='" + id + "'";
//                            db.update("Items", contentValues, where, new String[]{});
//                        }while (cursor2.moveToNext());
//                    }
//                    cursor2.close();
//
//                    Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE itemtax3 = '"+taxname+"'", null);
//                    if (cursor3.moveToFirst()){
//                        do {
//                            String id = cursor3.getString(0);
//                            System.out.println("itemname is3 "+id);
//                            ContentValues contentValues = new ContentValues();
//                            contentValues.put("status_temp", "yes");
//                            contentValues.put("status_perm", "yes");
//                            String where = " _id ='" + id + "'";
//                            db.update("Items", contentValues, where, new String[]{});
//                        }while (cursor3.moveToNext());
//                    }
//                    cursor3.close();
//
//                    Cursor cursor4 = db.rawQuery("SELECT * FROM Items WHERE itemtax4 = '"+taxname+"'", null);
//                    if (cursor4.moveToFirst()){
//                        do {
//                            String id = cursor4.getString(0);
//                            System.out.println("itemname is4 "+id);
//                            ContentValues contentValues = new ContentValues();
//                            contentValues.put("status_temp", "yes");
//                            contentValues.put("status_perm", "yes");
//                            String where = " _id ='" + id + "'";
//                            db.update("Items", contentValues, where, new String[]{});
//                        }while (cursor4.moveToNext());
//                    }
//                    cursor4.close();
//
//                    Cursor cursor5 = db.rawQuery("SELECT * FROM Items WHERE itemtax5 = '"+taxname+"'", null);
//                    if (cursor5.moveToFirst()){
//                        do {
//                            String id = cursor5.getString(0);
//                            System.out.println("itemname is5 "+id);
//                            ContentValues contentValues = new ContentValues();
//                            contentValues.put("status_temp", "yes");
//                            contentValues.put("status_perm", "yes");
//                            String where = " _id ='" + id + "'";
//                            db.update("Items", contentValues, where, new String[]{});
//                        }while (cursor5.moveToNext());
//                    }
//                    cursor5.close();

                    db.execSQL("UPDATE Items SET status_temp = 'yes' WHERE itemtax = '"+taxname+"' OR itemtax2 = '"+taxname+"' OR itemtax3 = '"+taxname+"' OR itemtax4 = '"+taxname+"' OR itemtax5 = '"+taxname+"'");
                    db.execSQL("UPDATE Items SET status_perm = 'yes' WHERE itemtax = '"+taxname+"' OR itemtax2 = '"+taxname+"' OR itemtax3 = '"+taxname+"' OR itemtax4 = '"+taxname+"' OR itemtax5 = '"+taxname+"'");

                    webservicequery("UPDATE Items SET status_temp = 'yes' WHERE itemtax = '"+taxname+"' OR itemtax2 = '"+taxname+"' OR itemtax3 = '"+taxname+"' OR itemtax4 = '"+taxname+"' OR itemtax5 = '"+taxname+"'");
                    webservicequery("UPDATE Items SET status_perm = 'yes' WHERE itemtax = '"+taxname+"' OR itemtax2 = '"+taxname+"' OR itemtax3 = '"+taxname+"' OR itemtax4 = '"+taxname+"' OR itemtax5 = '"+taxname+"'");

//                    Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE itemtax = '"+taxname+"' OR itemtax2 = '"+taxname+"' OR itemtax3 = '"+taxname+"' OR itemtax4 = '"+taxname+"' OR itemtax5 = '"+taxname+"'", null);
//                    if (cursor.moveToFirst()){
//                        do {
//                            String id1 = cursor.getString(0);
//                            System.out.println("itemname is there "+id1);
//                            ContentValues contentValues = new ContentValues();
//                            contentValues.put("status_temp", "yes");
//                            contentValues.put("status_perm", "yes");
//                            String where = " _id ='" + id1 + "'";
//                            //   db.update("Items", contentValues, where, new String[]{});
//
//                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                            getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                            resultUri = new Uri.Builder()
//                                    .scheme("content")
//                                    .authority(StubProviderApp.AUTHORITY)
//                                    .path("Items")
//                                    .appendQueryParameter("operation", "update")
//                                    .appendQueryParameter("_id",id1)
//                                    .build();
//                            getContentResolver().notifyChange(resultUri, null);
//
//
//
//                            String where1_v1 = "docid = '" + id1 + "' ";
//                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//                        }while (cursor.moveToNext());
//                    }

                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Integer file_url) {
//            progressbar.setVisibility(View.GONE);

            DownloadMusicfromInternetde11 downloadMusicfromInternetde = new DownloadMusicfromInternetde11();
            downloadMusicfromInternetde.execute();


        }
    }


    class DownloadMusicfromInternetde11 extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            try {

                groupList = new LinkedHashMap<Item, ArrayList<Item>>();

                ArrayList<Item> groupsList = fetchGroups();
                Log.i("GroupsListSize", String.valueOf(groupsList.size()));

                for (Item item : groupsList) {
                    String[] ids = item.id.split(",");
                    ArrayList<Item> groupMembers = new ArrayList<Item>();
                    for (int i = 0; i < ids.length; i++) {
                        String groupId = ids[i];
                        Log.i("GroupsListSize", groupId);
                        groupMembers.addAll(fetchGroupMembers(groupId));
                    }

                    item.name = item.name + "";
                    groupList.put(item, groupMembers);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Integer file_url) {
            progressbar.setVisibility(View.GONE);
            btnsave.setVisibility(View.VISIBLE);

            ExpandableAdapter adapter = new ExpandableAdapter(Taxes_Multiselection_Items.this, expandableListView, groupList, selection);
            expandableListView.setAdapter(adapter);

//            for (int i = 0; i < adapter.getGroupCount(); i++)
//                expandableListView.expandGroup(i);

        }
    }

    public void webservicequery(final String webserviceQuery){


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
        RequestQueue queue;
        StringRequest sr1;
        // queue = Volley.newRequestQueue(getActivity());

        queue= RequestSingleton.getInstance(getApplicationContext()).getInstance();

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


}
