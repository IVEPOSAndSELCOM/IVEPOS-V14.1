package com.intuition.ivepos;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intuition.ivepos.syncapp.StubProviderApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

public class KOT_MultiSelection_items extends AppCompatActivity {

    public SQLiteDatabase db = null;

    private LinkedHashMap<Item, ArrayList<Item>> groupList;
    ArrayList<String> checking;
    String groupName, select;

    RelativeLayout progressbar;
    String co = "0";
    Uri contentUri,resultUri;
    String player1name, dept_name_get;

    private ArrayList<Item> item;
    ExpandableListView expandableListView;

    TextView selection;
    LinearLayout btnsave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandablelist_items_categories);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        progressbar = (RelativeLayout) findViewById(R.id.progressbar);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        expandableListView = (ExpandableListView) findViewById(R.id.expandablelist);
        selection = (TextView) findViewById(R.id.selection);

        Bundle extras = getIntent().getExtras();
        dept_name_get = extras.getString("dept_name_get");

        btnsave = (LinearLayout) findViewById(R.id.btnn);

        DownloadMusicfromInternetde_update01 downloadMusicfromInternetde_update01 = new DownloadMusicfromInternetde_update01();
        downloadMusicfromInternetde_update01.execute();

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadMusicfromInternetdel downloadMusicfromInternetdel = new DownloadMusicfromInternetdel();
                downloadMusicfromInternetdel.execute();
            }
        });

        LinearLayout back_activity = (LinearLayout) findViewById(R.id.back_activity);
        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    class DownloadMusicfromInternetde_update01 extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            try {

                db.execSQL("UPDATE Items SET status_temp = ''");
//                if (cursor.moveToFirst()){
//                    do {
//                        String id = cursor.getString(0);
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("status_temp", "");
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

                db.execSQL("UPDATE Items SET status_temp = 'yes' WHERE dept_name = '"+dept_name_get+"'");
//                if (cursor.moveToFirst()){
//                    do {
//                        String id1 = cursor.getString(0);
//                        System.out.println("itemname is there "+id1);
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("status_temp", "yes");
//                        String where = " _id ='" + id1 + "'";
//                        //   db.update("Items", contentValues, where, new String[]{});
//
//                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                        getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                        resultUri = new Uri.Builder()
//                                .scheme("content")
//                                .authority(StubProviderApp.AUTHORITY)
//                                .path("Items")
//                                .appendQueryParameter("operation", "update")
//                                .appendQueryParameter("_id",id1)
//                                .build();
//                        getContentResolver().notifyChange(resultUri, null);
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

            ExpandableAdapter_kot adapter = new ExpandableAdapter_kot(KOT_MultiSelection_items.this, expandableListView, groupList, selection);
            expandableListView.setAdapter(adapter);

//            for (int i = 0; i < adapter.getGroupCount(); i++)
//                expandableListView.expandGroup(i);

        }
    }


    class DownloadMusicfromInternetdel extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            try {

                db.execSQL("UPDATE Items SET dept_name = '' WHERE dept_name = '"+dept_name_get+"'");
//                Cursor c_a = db.rawQuery("SELECT * FROM Items WHERE dept_name = '"+dept_name_get+"'", null);
//                if (c_a.moveToFirst()){
//                    do {
//                        String id = c_a.getString(0);
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("dept_name", "");
//                        String where = " _id ='" + id + "'";
//                        db.update("Items", contentValues, where, new String[]{});
//                    }while (c_a.moveToNext());
//                }
//                c_a.close();

//                db.execSQL("UPDATE Items SET dept_name = '"+dept_name_get+"' WHERE status_temp = 'yes'", null);
                Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE status_temp = 'yes'", null);
                if (cursor.moveToFirst()){
                    do {
                        String id = cursor.getString(0);

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("dept_name", dept_name_get);
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
                        db.update("Items", contentValues, where, new String[]{});


                    }while (cursor.moveToNext());
                }
                cursor.close();

                Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE status_temp = '' AND dept_name = ''", null);
                if (cursor3.moveToFirst()){
                    do {
                        String id = cursor3.getString(0);

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("dept_name", "");
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
                        db.update("Items", contentValues, where, new String[]{});


                    }while (cursor3.moveToNext());
                }
                cursor3.close();

                Cursor cursor1 = db.rawQuery("SELECT COUNT(status_temp) FROM Items WHERE status_temp = 'yes'", null);
                if (cursor1.moveToFirst()){
                    int l = cursor1.getInt(0);
                    co = String.valueOf(l);
                }
                cursor1.close();

                db.execSQL("UPDATE Items SET status_temp = ''", null);
//                Cursor cursor2 = db.rawQuery("SELECT * FROM Items", null);
//                if (cursor2.moveToFirst()){
//                    do {
//                        String id = cursor2.getString(0);
//                        String temp = cursor2.getString(36);
//
//                        TextView tv = new TextView(KOT_MultiSelection_items.this);
//                        tv.setText(temp);
//
//                        if (tv.getText().toString().equals("yes")) {
//                            ContentValues contentValues = new ContentValues();
//                            contentValues.put("status_temp", "");
//                            String where = " _id ='" + id + "'";
//                            db.update("Items", contentValues, where, new String[]{});
//
//                            String where1_v1 = "docid = '" + id + "'";
//                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//                        }
//                    }while (cursor2.moveToNext());
//                }
//                cursor2.close();

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
//                Toast.makeText(Add_Items_List1.this, "1q "+items_count.getText().toString(), Toast.LENGTH_LONG).show();
            setResult(RESULT_OK, intent);
            finish();

        }
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

                        Collections.sort(groupList, new Comparator<Item>() {

                            public int compare(Item item1, Item item2) {

                                return item2.name.compareTo(item1.name) < 0
                                        ? 0 : -1;
                            }
                        });

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

        Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE category = '"+groupId+"' AND (dept_name = '' OR dept_name IS null)", null);
        if (cursor.moveToFirst()){
            do {
                hi1 = cursor.getString(1);
                System.out.println("Itemname is "+hi1);
                Log.i("GroupsListSizehi2 ", "");

                for (int i = 0; i < checking.size(); i++) {
                    String s = checking.get(i);

                    Log.i("GroupsListSizehi3 ", hi1);
                    hi1 = hi1.replace(s, "");

                    Log.i("GroupsListSizehi4 ", hi1);


                    Item item = new Item();
                    String groupName;
                    item.id = groupName = "" + hi1;
                    item.name = groupName;

                    groupMembers.add(item);
                }

            }while (cursor.moveToNext());
        }
        cursor.close();

        Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE category = '"+groupId+"' AND dept_name = '"+dept_name_get+"'", null);
        if (cursor1.moveToFirst()){
            do {
                hi1 = cursor1.getString(1);
                System.out.println("Itemname is "+hi1);
                Log.i("GroupsListSizehi2 ", "");

                for (int i = 0; i < checking.size(); i++) {
                    String s = checking.get(i);

                    Log.i("GroupsListSizehi3 ", hi1);
                    hi1 = hi1.replace(s, "");

                    Log.i("GroupsListSizehi4 ", hi1);


                    Item item = new Item();
                    String groupName;
                    item.id = groupName = "" + hi1;
                    item.name = groupName;

                    groupMembers.add(item);
                }

            }while (cursor1.moveToNext());
        }
        cursor1.close();

        return groupMembers;
    }

}
