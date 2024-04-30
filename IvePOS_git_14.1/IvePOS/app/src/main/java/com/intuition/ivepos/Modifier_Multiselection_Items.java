package com.intuition.ivepos;

import android.content.ContentValues;
import android.content.Context;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

public class Modifier_Multiselection_Items extends AppCompatActivity {

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

    String modname;

    String WebserviceUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandablelist_items_modifiers);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(Modifier_Multiselection_Items.this);
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

        progressbar = (RelativeLayout) findViewById(R.id.progressbar);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        expandableListView = (ExpandableListView) findViewById(R.id.expandablelist);
        selection = (TextView) findViewById(R.id.selection);

        Bundle extras = getIntent().getExtras();
        player1name = extras.getString("PLAYER1NAME");
        modname = extras.getString("modifier_option_name");

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
//                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

//                        db.update("Items", contentValues, where, new String[]{});
                    }while (cursor.moveToNext());
                }
                cursor.close();

                finish();
            }
        });

        DownloadMusicfromInternetde_update01 downloadMusicfromInternetde_update01 = new DownloadMusicfromInternetde_update01();
        downloadMusicfromInternetde_update01.execute();


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

        int ni= 1;
        if (player1name.equals("insert")) {
            Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE category = '" + groupId + "'", null);
            if (cursor.moveToFirst()) {
                do {
                    hi1 = cursor.getString(1);
                    System.out.println("Itemname is " + hi1);
                    System.out.println("total Itemname is " + ni);
                    String mod1 = cursor.getString(52);
                    String mod2 = cursor.getString(53);
                    String mod3 = cursor.getString(54);
                    String mod4 = cursor.getString(55);
                    String mod5 = cursor.getString(56);

                    TextView mod1_t = new TextView(Modifier_Multiselection_Items.this);
                    mod1_t.setText(mod1);
                    TextView mod2_t = new TextView(Modifier_Multiselection_Items.this);
                    mod2_t.setText(mod2);
                    TextView mod3_t = new TextView(Modifier_Multiselection_Items.this);
                    mod3_t.setText(mod3);
                    TextView mod4_t = new TextView(Modifier_Multiselection_Items.this);
                    mod4_t.setText(mod4);
                    TextView mod5_t = new TextView(Modifier_Multiselection_Items.this);
                    mod5_t.setText(mod5);

                    if (mod1_t.getText().toString().equals("") || mod2_t.getText().toString().equals("") || mod3_t.getText().toString().equals("") ||
                            mod4_t.getText().toString().equals("") || mod5_t.getText().toString().equals("") ||
                            mod1_t.getText().toString().equals("None") || mod2_t.getText().toString().equals("None") || mod3_t.getText().toString().equals("None") ||
                            mod4_t.getText().toString().equals("None") || mod5_t.getText().toString().equals("None") ||
                            mod1_t.getText().toString().equals("NONE") || mod2_t.getText().toString().equals("NONE") || mod3_t.getText().toString().equals("NONE") ||
                            mod4_t.getText().toString().equals("NONE") || mod5_t.getText().toString().equals("NONE")) {
                        Log.i("GroupsListSizehi2 ", "");

                        System.out.println("checking.size() " + checking.size());

//                    for (int i = 0; i < checking.size(); i++) {
//                        String s = checking.get(i);

                        Log.i("GroupsListSizehi3 " + groupId + " ", hi1);
//                        hi1 = hi1.replace(s, "");

                        Log.i("GroupsListSizehi4 " + groupId + " ", hi1);


                        Item item = new Item();
                        String groupName;
                        item.id = groupName = "" + hi1;
                        item.name = groupName;

                        groupMembers.add(item);
//                    }
                    }

                    ni++;
                } while (cursor.moveToNext());
            }
            cursor.close();
        }else {
            Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE category = '" + groupId + "'", null);
            if (cursor.moveToFirst()) {
                do {
                    hi1 = cursor.getString(1);
                    System.out.println("Itemname is " + hi1);
                    System.out.println("total Itemname is " + ni);
                    String mod1 = cursor.getString(52);
                    String mod2 = cursor.getString(53);
                    String mod3 = cursor.getString(54);
                    String mod4 = cursor.getString(55);
                    String mod5 = cursor.getString(56);

                    TextView mod1_t = new TextView(Modifier_Multiselection_Items.this);
                    mod1_t.setText(mod1);
                    TextView mod2_t = new TextView(Modifier_Multiselection_Items.this);
                    mod2_t.setText(mod2);
                    TextView mod3_t = new TextView(Modifier_Multiselection_Items.this);
                    mod3_t.setText(mod3);
                    TextView mod4_t = new TextView(Modifier_Multiselection_Items.this);
                    mod4_t.setText(mod4);
                    TextView mod5_t = new TextView(Modifier_Multiselection_Items.this);
                    mod5_t.setText(mod5);

//                    if (mod1_t.getText().toString().equals("") || mod2_t.getText().toString().equals("") || mod3_t.getText().toString().equals("") ||
//                            mod4_t.getText().toString().equals("") || mod5_t.getText().toString().equals("") ||
//                            mod1_t.getText().toString().equals("None") || mod2_t.getText().toString().equals("None") || mod3_t.getText().toString().equals("None") ||
//                            mod4_t.getText().toString().equals("None") || mod5_t.getText().toString().equals("None") ||
//                            mod1_t.getText().toString().equals("NONE") || mod2_t.getText().toString().equals("NONE") || mod3_t.getText().toString().equals("NONE") ||
//                            mod4_t.getText().toString().equals("NONE") || mod5_t.getText().toString().equals("NONE")) {
                        Log.i("GroupsListSizehi2 ", "");

                        System.out.println("checking.size() " + checking.size());

//                    for (int i = 0; i < checking.size(); i++) {
//                        String s = checking.get(i);

                        Log.i("GroupsListSizehi3 " + groupId + " ", hi1);
//                        hi1 = hi1.replace(s, "");

                        Log.i("GroupsListSizehi4 " + groupId + " ", hi1);


                        Item item = new Item();
                        String groupName;
                        item.id = groupName = "" + hi1;
                        item.name = groupName;

                        groupMembers.add(item);
//                    }
//                    }

                    ni++;
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

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

                String webservicequery2="update Items_Virtual set status_perm='yes' where status_temp = 'yes'";
                try {
                    db.execSQL(webservicequery2);
                    Log.e("webservicequery",webservicequery2);
                } catch (SQLException e) {
                    Log.e("webs-exception",webservicequery2);
                    e.printStackTrace();
                }


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



//            Intent intent = new Intent();
//            intent.putExtra("editTextValue", co);
//            intent.putExtra("player1name", player1name);
////                Toast.makeText(Add_Items_List1.this, "1q "+items_count.getText().toString(), Toast.LENGTH_LONG).show();
//            setResult(RESULT_OK, intent);
//            finish();


            DownloadMusicfromInternetde_insert_item downloadMusicfromInternetde_insert = new DownloadMusicfromInternetde_insert_item();
            downloadMusicfromInternetde_insert.execute();

        }
    }


    class DownloadMusicfromInternetde_update01 extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            try {

                db.execSQL("UPDATE Items SET status_temp = ''");
                db.execSQL("UPDATE Items SET status_perm = ''");

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

                }else {


                    db.execSQL("UPDATE Items SET status_temp = 'yes' WHERE mod_set1 = '"+modname+"' OR mod_set2 = '"+modname+"' OR mod_set3 = '"+modname+"' OR mod_set4 = '"+modname+"' OR mod_set5 = '"+modname+"'");
                    db.execSQL("UPDATE Items SET status_perm = 'yes' WHERE mod_set1 = '"+modname+"' OR mod_set2 = '"+modname+"' OR mod_set3 = '"+modname+"' OR mod_set4 = '"+modname+"' OR mod_set5 = '"+modname+"'");


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

            ExpandableAdapter adapter = new ExpandableAdapter(Modifier_Multiselection_Items.this, expandableListView, groupList, selection);
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


    class DownloadMusicfromInternetde_insert_item extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

//            ContentValues newValues = new ContentValues();
//            newValues.put("taxname", String.valueOf(columnvalue)+"("+editText.getText().toString()+"%)");
//            newValues.put("hsn_code", editText1_HSN.getText().toString());
//            newValues.put("value", Float.valueOf(editText.getText().toString()));
//
//            newValues.put("taxtype", "Itemtax");
//
//            newValues.put("tax1", "dine_in");
//            newValues.put("tax2", "takeaway");
//            newValues.put("tax3", "homedelivery");
//
//
//
//            final String webserviceQuery1="INSERT INTO `taxes`(`taxname`, `value`, `taxtype`, `tax1`, `tax2`, `tax3`, `hsn_code`) " +
//                    "VALUES ('"+String.valueOf(columnvalue)+"("+editText.getText().toString()+"%)"+"',"+Float.valueOf(editText.getText().toString())+",'Itemtax','dine_in','takeaway','homedelivery','"+ editText1_HSN.getText().toString()+"')";
//////
////            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Taxes");
////            resultUri = getActivity().getContentResolver().insert(contentUri, newValues);
////            getActivity().getContentResolver().notifyChange(resultUri, null);
////
//            db.execSQL(webserviceQuery1);
//
////            webservicequery(webserviceQuery1);
//
//            db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//            Cursor cursor_t = db.rawQuery("SELECt * FROM taxes WHERE taxname = '"+String.valueOf(columnvalue)+"("+editText.getText().toString()+"%)"+"'", null);
//            if (cursor_t.moveToFirst()){
//                String id = cursor_t.getString(0);
//                String name = cursor_t.getString(1);
//                String value = cursor_t.getString(2);
//                String taxtype = cursor_t.getString(3);
//                String tax1 = cursor_t.getString(5);
//                String tax2 = cursor_t.getString(6);
//                String tax3 = cursor_t.getString(7);
//                String hsn = cursor_t.getString(8);
//
//                final String webserviceQuery2="INSERT INTO `taxes`(`_id`,`taxname`, `value`, `taxtype`, `tax1`, `tax2`, `tax3`, `hsn_code`) " +
//                        "VALUES ('"+id+"', '"+name+"',"+value+",'Itemtax','dine_in','takeaway','homedelivery','"+hsn+"')";
//                webservicequery(webserviceQuery2);
//            }
////
////            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Taxes");
////            resultUri = getActivity().getContentResolver().insert(contentUri, newValues);
////            getActivity().getContentResolver().notifyChange(resultUri, null);
//
////            db.insert("Taxes", null, newValues);

            Cursor taxeitem_all = db.rawQuery("SELECT * FROM Items", null);
            if (taxeitem_all.moveToFirst()){
                do {
                    String id = taxeitem_all.getString(0);
                    String itemname_t=taxeitem_all.getString(1);
                    String one = taxeitem_all.getString(52);
                    String two = taxeitem_all.getString(53);
                    String three = taxeitem_all.getString(54);
                    String four = taxeitem_all.getString(55);
                    String five = taxeitem_all.getString(56);
                    String st = taxeitem_all.getString(37);

                    TextView tv = new TextView(Modifier_Multiselection_Items.this);
                    tv.setText(one);
                    TextView tv2 = new TextView(Modifier_Multiselection_Items.this);
                    tv2.setText(two);
                    TextView tv3 = new TextView(Modifier_Multiselection_Items.this);
                    tv3.setText(three);
                    TextView tv4 = new TextView(Modifier_Multiselection_Items.this);
                    tv4.setText(four);
                    TextView tv5 = new TextView(Modifier_Multiselection_Items.this);
                    tv5.setText(five);

                    TextView stt = new TextView(Modifier_Multiselection_Items.this);
                    stt.setText(st);

                    if (stt.getText().toString().equals("yes")) {
                        if (tv.getText().toString().equals(modname) ||
                                tv2.getText().toString().equals(modname) ||
                                tv3.getText().toString().equals(modname) ||
                                tv4.getText().toString().equals(modname) ||
                                tv5.getText().toString().equals(modname)){

                        }else {
//                        Toast.makeText(getActivity(), "id "+id, Toast.LENGTH_LONG).show();
                            if (tv.getText().toString().equals("") || tv.getText().toString().equals("None")) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("mod_set1", modname);
                                String where = "_id = '" + id + "' ";

//                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                                getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                                resultUri = new Uri.Builder()
//                                        .scheme("content")
//                                        .authority(StubProviderApp.AUTHORITY)
//                                        .path("Items")
//                                        .appendQueryParameter("operation", "update")
//                                        .appendQueryParameter("_id",id)
//                                        .build();
//                                getActivity().getContentResolver().notifyChange(resultUri, null);
//                                String where1_v1 = "docid = '" + id + "'";
//                                db.update("Items_Virtual", contentValues, where1_v1, new String[]{});



                                final String webserviceQuery ="update Items set mod_set1 = '"+modname+"' where itemname='"+itemname_t+"'";
                                //db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                                db.execSQL(webserviceQuery);
                                webservicequery(webserviceQuery);




//                            Toast.makeText(getActivity(), "itemtax "+String.valueOf(columnvalue), Toast.LENGTH_LONG).show();
                            } else {
                                if (tv2.getText().toString().equals("") || tv2.getText().toString().equals("None")) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("mod_set2", modname);
                                    String where = "_id = '" + id + "' ";

//                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                                    getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
//                                    resultUri = new Uri.Builder()
//                                            .scheme("content")
//                                            .authority(StubProviderApp.AUTHORITY)
//                                            .path("Items")
//                                            .appendQueryParameter("operation", "update")
//                                            .appendQueryParameter("_id",id)
//                                            .build();
//                                    getActivity().getContentResolver().notifyChange(resultUri, null);
//
//                                    String where1_v1 = "docid = '" + id + "'";
//                                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});


                                    final String webserviceQuery ="update Items set mod_set2 = '"+modname+"' where itemname='"+itemname_t+"'";
                                    db.execSQL(webserviceQuery);

                                    webservicequery(webserviceQuery);


                                    //                                    db.update("Items", contentValues, where, new String[]{});
//                                Toast.makeText(getActivity(), "mod_set2 "+String.valueOf(columnvalue), Toast.LENGTH_LONG).show();
                                } else {
                                    if (tv3.getText().toString().equals("") || tv3.getText().toString().equals("None")) {
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("mod_set3", modname);
                                        String where = "_id = '" + id + "' ";

                                        //   db.update("Items", contentValues, where, new String[]{});

                                        String where1_v1 = "docid = '" + id + "'";
//                                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});


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
//                                        db.update("Items", contentValues, where, new String[]{});
//                                    Toast.makeText(getActivity(), "mod_set3 "+String.valueOf(columnvalue), Toast.LENGTH_LONG).show();
                                    } else {
                                        if (tv4.getText().toString().equals("") || tv4.getText().toString().equals("None")) {
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("mod_set4", modname);
                                            String where = "_id = '" + id + "' ";

                                            //  db.update("Items", contentValues, where, new String[]{});

                                            String where1_v1 = "docid = '" + id + "'";
//                                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});


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
//                                            db.update("Items", contentValues, where, new String[]{});
//                                        Toast.makeText(getActivity(), "mod_set4 "+String.valueOf(columnvalue), Toast.LENGTH_LONG).show();
                                        } else {
                                            if (tv5.getText().toString().equals("") || tv5.getText().toString().equals("None")) {
                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("mod_set5", modname);
                                                String where = "_id = '" + id + "' ";
                                                //  db.update("Items", contentValues, where, new String[]{});

                                                //   db.update("Items", contentValues, where, new String[]{});

                                                String where1_v1 = "docid = '" + id + "'";
//                                                db.update("Items_Virtual", contentValues, where1_v1, new String[]{});



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
//                                                db.update("Items", contentValues, where, new String[]{});
//                                            Toast.makeText(getActivity(), "mod_set5 "+String.valueOf(columnvalue), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }else {
                        if (tv.getText().toString().equals(modname)){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("itemtax", "");
                            contentValues.put("tax_value", "");
                            String where = "_id = '" + id + "' ";

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
//                            db.update("Items", contentValues, where, new String[]{});
                        }
                        if (tv2.getText().toString().equals(modname)){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("mod_set2", "");
                            String where = "_id = '" + id + "' ";
                            //    db.update("Items", contentValues, where, new String[]{});

                            String where1_v1 = "docid = '" + id + "'";
//                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                        }
                        if (tv3.getText().toString().equals(modname)){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("mod_set2", "");
                            String where = "_id = '" + id + "' ";

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
//                            db.update("Items", contentValues, where, new String[]{});

                            String where1_v1 = "docid = '" + id + "'";
//                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                        }
                        if (tv4.getText().toString().equals(modname)){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("mod_set4", "");
                            String where = "_id = '" + id + "' ";
                            //    db.update("Items", contentValues, where, new String[]{});

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
//                            db.update("Items", contentValues, where, new String[]{});
                            String where1_v1 = "docid = '" + id + "'";
//                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});


                        }
                        if (tv5.getText().toString().equals(modname)){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("mod_set2", "");
                            String where = "_id = '" + id + "' ";
                            //   db.update("Items", contentValues, where, new String[]{});

                            String where1_v1 = "docid = '" + id + "'";
//                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

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
//                            db.update("Items", contentValues, where, new String[]{});
                        }
                    }
                }while (taxeitem_all.moveToNext());
            }
            taxeitem_all.close();

            Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
            if (cursor.moveToFirst()){
                do {
                    String id = cursor.getString(0);
                    String temp = cursor.getString(36);

                    TextView tv = new TextView(Modifier_Multiselection_Items.this);
                    tv.setText(temp);

                    if (tv.getText().toString().equals("yes")) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("status_temp", "");
                        contentValues.put("status_perm", "");
                        String where = " _id ='" + id + "'";
                        db.update("Items", contentValues, where, new String[]{});

                        String where1_v1 = "docid = '" + id + "'";
//                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                    }
                }while (cursor.moveToNext());
            }
            cursor.close();

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
//            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Integer file_url) {
            progressbar.setVisibility(View.GONE);

//            Toast.makeText(Modifier_Multiselection_Items.this.getBaseContext(),
//                    "Tax added", Toast.LENGTH_SHORT).show();
            finish();
//            linearLayout.setVisibility(View.GONE);
//            additem.setVisibility(View.VISIBLE);
//            hideKeyboard(getContext());

        }
    }

}
