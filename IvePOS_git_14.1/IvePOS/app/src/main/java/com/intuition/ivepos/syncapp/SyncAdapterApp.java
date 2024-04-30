package com.intuition.ivepos.syncapp;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncInfo;
import android.content.SyncResult;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.intuition.ivepos.csv.RequestSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

/**
 * Created by HP on 8/10/2018.
 */

public class SyncAdapterApp extends AbstractThreadedSyncAdapter {

    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    Context ctx;

    String WebserviceUrl;

    private String TAG="syncadapterapp";




    /**
     * Set up the sync adapter
     */
    public SyncAdapterApp(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        ctx=context;

        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapterApp(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();

    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        /*
         * Put the data transfer code here.
         */


//        SharedPreferences sharedpreferences =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(ctx);
//        String account_selection= sharedpreferences.getString("account_selection", null);
//
//        if (sharedpreferences.getString("account_selection", null).toString().equals("")) {
//
//        }else {
//            if (account_selection.toString().equals("Dine")) {
//                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
//            }else {
//                if (account_selection.toString().equals("Qsr")) {
//                    WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
//                }else {
//                    WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
//                }
//            }
//        }


        try {
            if(extras!=null){
                if(extras.getString("table")!=null){
                    String table=extras.getString("table");

                    if(extras.getString("where")!=null){
                        String where =extras.getString("where");

                        if(extras.getString("operation").equalsIgnoreCase("update")){
                            syncUpdate(table,where);
                        }else if(extras.getString("operation").equalsIgnoreCase("delete")){
                            syncDelete(table,where);
                        }
                    }else{
                        synctable(table);
                    }

                }else if(extras.getString("download")!=null){
                    //   download();
                }else if (extras.getString("query")!=null){
                    webservicequery(extras.getString("query"));

                }else {
                    syncperiodic();
                }
            }else{
                syncperiodic();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* private void download() {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        syncdbapp=ctx.openOrCreateDatabase("syncdbapp", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);
        Cursor cursor=syncdbapp.rawQuery("select * from appdata",null);
        mydbappdata=ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);
        if(cursor.moveToFirst()){
            do{
                final int lastsynced= cursor.getInt(2);
                final int id= cursor.getInt(0);
                final String table=cursor.getString(1);

                if((!table.equalsIgnoreCase("android_metadata"))&&(!table.equalsIgnoreCase("sqlite_sequence"))){


                    SharedPreferences sharedpreferences = getContext().getSharedPreferences("MyPref",
                            Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS);
                    String company= sharedpreferences.getString("companyname", null);
                    String store= sharedpreferences.getString("storename", null);
                    String device= sharedpreferences.getString("devicename", null);


                    JSONObject params = new JSONObject();
                    try {
                        params.put("company",company);
                        params.put("store",store);
                        params.put("device",device);
                        params.put("table",table);
                        params.put("lastsynced",lastsynced+"");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    JsonArrayRequest jobReq = new JsonArrayRequest(Request.Method.POST, Constant.WebserviceUrl+"downloadappdata.php", params, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            try {
                                for(int i=0;i<response.length();i++){
                                    JSONObject jobj=response.getJSONObject(i);
                                    ContentValues cv=new ContentValues();
                                    Iterator<?> keys = jobj.keys();
                                    while(keys.hasNext() ) {
                                        String key = (String)keys.next();
                                        String value=jobj.getString(key);
                                        cv.put(key,value);
                                    }
                                    mydbappdata=ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);
                                    long rowid=mydbappdata.insert(table, null, cv);
                                    if(rowid!=-1){
                                        Cursor synCursor=mydbappdata.rawQuery("SELECT * FROM "+table+" WHERE   _id = (SELECT MAX(_id)  FROM "+table+")",null);
                                        if(synCursor.moveToFirst()){
                                            lastid=  synCursor.getString(0);
                                        }
                                        ContentValues cv1=new ContentValues();
                                        cv1.put("_id",id);
                                        cv1.put("tablename",table);
                                        cv1.put("lastsyncedid",lastid);
                                        String where1 = "_id = "+id;
                                        syncdbapp.update("appdata", cv, where1, new String[]{});
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    queue.add(jobReq);
*//*
                            JsonArrayRequest jobReq = new JsonArrayRequest(Request.Method.POST, Constant.WebserviceUrl+"downloadappdata.php", params,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONObject jarray) {
                                    String response= null;
                                    try {
                                        response = jarray.getString("status");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if(response.equalsIgnoreCase("success")){
                                        ContentValues cv=new ContentValues();
                                        cv.put("_id",id);
                                        cv.put("tablename",table);
                                        cv.put("lastsyncedid",lastid);
                                        String where1 = "_id = "+id;
                                        syncdbapp.update("appdata", cv, where1, new String[]{});
                                    }else{

                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                }
                            });*//*

                    queue.add(jobReq);
                }




            }while (cursor.moveToNext());
        }
        try {
            cursor.close();
        } finally {
            cursor.close();
        }

    }*/

    private void syncperiodic() {
        RequestQueue queue = Volley.newRequestQueue(ctx);

        SQLiteDatabase mydbappdata=null;
        final SQLiteDatabase syncdb1=ctx.openOrCreateDatabase("syncdbapp", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);
        Cursor cursor1=null;
        try {
            cursor1 = syncdb1.rawQuery("select * from appdata", null);

            mydbappdata = ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS, null);
            if (cursor1.moveToFirst()) {
                do {
                    final int lastsynced = cursor1.getInt(2);
                    final int id = cursor1.getInt(0);
                    final String table = cursor1.getString(1);

                    if ((!table.equalsIgnoreCase("android_metadata")) && (!table.equalsIgnoreCase("sqlite_sequence")) && (!table.contains("Table"))) {
                        String account_selection = "";
                        Cursor selec = mydbappdata.rawQuery("SELECT * FROM Account_selection", null);
                        if (selec.moveToFirst()) {
                            account_selection = selec.getString(1);
                        }
                        System.out.println("login is3 "+account_selection);

                        if (account_selection.toString().equals("Dine")) {
                            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                        }else {
                            if (account_selection.toString().equals("Qsr")) {
                                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                            }else {
                                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
                            }
                        }
                    }
                } while (cursor1.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor1!=null){
                cursor1.close();
            }

        }


//        SQLiteDatabase mydbappdata=null;

        final SQLiteDatabase syncdbapp=ctx.openOrCreateDatabase("syncdbapp", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);


        Cursor cursor= null;
        try {
            cursor = syncdbapp.rawQuery("select * from appdata",null);
            if(cursor.moveToFirst()){
                do{
                    final int lastsynced= cursor.getInt(2);
                    final int id= cursor.getInt(0);
                    final String table=cursor.getString(1);

                    if((!table.equalsIgnoreCase("android_metadata"))&&(!table.equalsIgnoreCase("sqlite_sequence"))&&(!table.contains("Items_Virtual"))&&(!table.contains("ITEMSLIST"))){

                        mydbappdata=ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);
                        if (table.toString().equals("LOGIN")){
                            Cursor syncCursor = null;
                       /* if(table.equalsIgnoreCase("LOGIN"))
                             syncCursor=mydbappdata.rawQuery("select * from "+table+" where ID > "+lastsynced,null);
                        else*/
                            try {
                                syncCursor = mydbappdata.rawQuery("select * from " + table + " where ID > " + lastsynced, null);

                                if (syncCursor.getCount() > 0) {
                                    JSONArray jsonArray;

                                    jsonArray = cur2Json(syncCursor,table);

                                    JSONObject tablejson = new JSONObject();
                                    try {
                                        tablejson.put("table", table);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    jsonArray.put(tablejson);

                                    String account_selection = "";
                                    SQLiteDatabase db=ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);
                                    Cursor selec = db.rawQuery("SELECT * FROM Account_selection", null);
                                    if (selec.moveToFirst()) {
                                        account_selection = selec.getString(1);
                                    }
                                    System.out.println("login is "+account_selection);

                                    if (account_selection.toString().equals("Dine")) {
                                        WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                                    }else {
                                        if (account_selection.toString().equals("Qsr")) {
                                            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                                        }else {
                                            WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
                                        }
                                    }

                                    SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(getContext());
                                    String company = sharedpreferences.getString("companyname", null);
                                    String store = sharedpreferences.getString("storename", null);
                                    String device = sharedpreferences.getString("devicename", null);


                                    JSONObject params = new JSONObject();
                                    try {
                                        params.put("company", company);
                                        params.put("store", store);
                                        params.put("device", device);
                                        params.put("data", jsonArray);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Cursor synCursor=null;
                                    String lastid1="";
                                    try {
                                        synCursor = mydbappdata.rawQuery("SELECT * FROM " + table + " WHERE   ID = (SELECT MAX(ID)  FROM " + table + ")", null);
                                        if (synCursor.moveToFirst()) {
                                            lastid1 = synCursor.getString(0);
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        if(synCursor!=null)
                                            synCursor.close();
                                    }

                                    System.out.println("sync appdata1 "+company+store+device+params);

                                    final String lastid=lastid1;
                                    JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, WebserviceUrl + "syncappdata.php", params,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject jarray) {
                                                    String response = null;
                                                    try {
                                                        response = jarray.getString("status");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    if (response.equalsIgnoreCase("success")) {
                                                        ContentValues cv = new ContentValues();
                                                        cv.put("_id", id);
                                                        cv.put("tablename", table);
                                                        cv.put("lastsyncedid", lastid);
                                                        String where1 = "_id = " + id;
                                                        syncdbapp.update("appdata", cv, where1, new String[]{});
                                                    } else {

                                                    }

                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError volleyError) {

                                                }
                                            });

                                    queue.add(jobReq);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {

                                if(syncCursor!=null){
                                    syncCursor.close();
                                }

                            }


                        }else {
                            Cursor syncCursor = null;
                       /* if(table.equalsIgnoreCase("LOGIN"))
                             syncCursor=mydbappdata.rawQuery("select * from "+table+" where ID > "+lastsynced,null);
                        else*/
                            try {
                                syncCursor = mydbappdata.rawQuery("select * from " + table + " where _id > " + lastsynced, null);

                                if (syncCursor.getCount() > 0) {
                                    JSONArray jsonArray;

                                    jsonArray = cur2Json(syncCursor,table);

                                    JSONObject tablejson = new JSONObject();
                                    try {
                                        tablejson.put("table", table);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    jsonArray.put(tablejson);

                                    String account_selection = "";
                                    SQLiteDatabase db=ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);
                                    Cursor selec = db.rawQuery("SELECT * FROM Account_selection", null);
                                    if (selec.moveToFirst()) {
                                        account_selection = selec.getString(1);
                                    }
                                    System.out.println("login is "+account_selection);

                                    if (account_selection.toString().equals("Dine")) {
                                        WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                                    }else {
                                        if (account_selection.toString().equals("Qsr")) {
                                            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                                        }else {
                                            WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
                                        }
                                    }

                                    SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getContext());
                                    String company = sharedpreferences.getString("companyname", null);
                                    String store = sharedpreferences.getString("storename", null);
                                    String device = sharedpreferences.getString("devicename", null);


                                    JSONObject params = new JSONObject();
                                    try {
                                        params.put("company", company);
                                        params.put("store", store);
                                        params.put("device", device);
                                        params.put("data", jsonArray);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Cursor synCursor=null;
                                    String lastid1="";
                                    try {
                                        synCursor = mydbappdata.rawQuery("SELECT * FROM " + table + " WHERE   _id = (SELECT MAX(_id)  FROM " + table + ")", null);
                                        if (synCursor.moveToFirst()) {
                                            lastid1 = synCursor.getString(0);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        if(syncCursor!=null)
                                            synCursor.close();
                                    }

                                    final String lastid=lastid1;
                                    JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, WebserviceUrl + "syncappdata.php", params,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject jarray) {
                                                    String response = null;
                                                    try {
                                                        response = jarray.getString("status");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    if (response.equalsIgnoreCase("success")) {
                                                        ContentValues cv = new ContentValues();
                                                        cv.put("_id", id);
                                                        cv.put("tablename", table);
                                                        cv.put("lastsyncedid", lastid);
                                                        String where1 = "_id = " + id;
                                                        syncdbapp.update("appdata", cv, where1, new String[]{});
                                                    } else {

                                                    }

                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError volleyError) {

                                                }
                                            });

                                    queue.add(jobReq);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {

                                if(syncCursor!=null){
                                    syncCursor.close();
                                }

                            }
                        }
                    }




                }while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor!=null){
                cursor.close();
            }
        }

    }

    private void synctable(final String table) {
        RequestQueue queue = Volley.newRequestQueue(ctx);

        SQLiteDatabase mydbappdata=null;
        final SQLiteDatabase syncdb1=ctx.openOrCreateDatabase("syncdbapp", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);
        Cursor cursor1=null;
        try {
            cursor1 = syncdb1.rawQuery("select * from appdata", null);

            mydbappdata = ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS, null);
            if (cursor1.moveToFirst()) {
                do {
                    final int lastsynced = cursor1.getInt(2);
                    final int id = cursor1.getInt(0);
                    final String table1 = cursor1.getString(1);

                    if ((!table1.equalsIgnoreCase("android_metadata")) && (!table1.equalsIgnoreCase("sqlite_sequence")) && (!table1.contains("Table"))) {
                        String account_selection = "";
                        Cursor selec = mydbappdata.rawQuery("SELECT * FROM Account_selection", null);
                        if (selec.moveToFirst()) {
                            account_selection = selec.getString(1);
                        }
                        System.out.println("login is "+account_selection);

                        if (account_selection.toString().equals("Dine")) {
                            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                        }else {
                            if (account_selection.toString().equals("Qsr")) {
                                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                            }else {
                                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
                            }
                        }
                    }
                } while (cursor1.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor1!=null){
                cursor1.close();
            }

        }

//        SQLiteDatabase mydbappdata=null;

        final SQLiteDatabase syncdbapp=ctx.openOrCreateDatabase("syncdbapp", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);

        Cursor cursor=null;
        try {
            cursor=syncdbapp.rawQuery("select * from appdata where tablename = '"+table+"'",null);
            if(cursor.moveToFirst()){
                do{
                    final int lastsynced= cursor.getInt(2);
                    final int id= cursor.getInt(0);
                    if((!table.equalsIgnoreCase("android_metadata"))&&(!table.equalsIgnoreCase("sqlite_sequence"))&&(!table.contains("Items_Virtual"))&&(!table.contains("ITEMSLIST"))) {
                        mydbappdata = ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS, null);
                        if (table.toString().equals("LOGIN")){

                            Cursor syncCursor=null;
                            try {

                                syncCursor = mydbappdata.rawQuery("select * from " + table + " where ID > " + lastsynced, null);
                                if (syncCursor.getCount() > 0) {
                                    JSONArray jsonArray;

                                    jsonArray = cur2Json(syncCursor,table);

                                    JSONObject tablejson = new JSONObject();
                                    try {
                                        tablejson.put("table", table);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    jsonArray.put(tablejson);

                                    String account_selection = "";
                                    SQLiteDatabase db=ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);
                                    Cursor selec = db.rawQuery("SELECT * FROM Account_selection", null);
                                    if (selec.moveToFirst()) {
                                        account_selection = selec.getString(1);
                                    }
                                    System.out.println("login is "+account_selection);

                                    if (account_selection.toString().equals("Dine")) {
                                        WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                                    }else {
                                        if (account_selection.toString().equals("Qsr")) {
                                            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                                        }else {
                                            WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
                                        }
                                    }

                                    SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getContext());
                                    String company = sharedpreferences.getString("companyname", null);
                                    String store = sharedpreferences.getString("storename", null);
                                    String device = sharedpreferences.getString("devicename", null);


                                    JSONObject params = new JSONObject();
                                    try {
                                        params.put("company", company);
                                        params.put("store", store);
                                        params.put("device", device);
                                        params.put("data", jsonArray);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Cursor synCursor=null;
                                    String lastid1="";
                                    try {
                                        synCursor = mydbappdata.rawQuery("SELECT * FROM " + table + " WHERE   ID = (SELECT MAX(ID)  FROM " + table + ")", null);
                                        if (synCursor.moveToFirst()) {
                                            lastid1 = synCursor.getString(0);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        if(synCursor!=null){
                                            synCursor.close();
                                        }
                                    }

                                    System.out.println("sync appdate "+company+store+device+params);

                                    final String lastid=lastid1;
                                    JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, WebserviceUrl + "syncappdata.php", params,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject jarray) {
                                                    String response = null;
                                                    try {
                                                        response = jarray.getString("status");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    if (response.equalsIgnoreCase("success")) {
                                                        ContentValues cv = new ContentValues();
                                                        cv.put("_id", id);
                                                        cv.put("tablename", table);
                                                        cv.put("lastsyncedid", lastid);
                                                        String where1 = "_id = " + id;
                                                        syncdbapp.update("appdata", cv, where1, new String[]{});
                                                    } else {

                                                    }

                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError volleyError) {

                                                }
                                            });

                                    queue.add(jobReq);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if(syncCursor!=null){
                                    syncCursor.close();
                                }
                            }


                        }else {
                            System.out.println("sync table1 "+table+ " "+lastsynced);
                            Cursor syncCursor=null;
                            try {
                                syncCursor = mydbappdata.rawQuery("select * from " + table + " where _id > " + lastsynced, null);
                                if (syncCursor.getCount() > 0) {
                                    JSONArray jsonArray;

                                    jsonArray = cur2Json(syncCursor,table);

                                    JSONObject tablejson = new JSONObject();
                                    try {
                                        tablejson.put("table", table);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    jsonArray.put(tablejson);

                                    String account_selection = "";
                                    SQLiteDatabase db=ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);
                                    Cursor selec = db.rawQuery("SELECT * FROM Account_selection", null);
                                    if (selec.moveToFirst()) {
                                        account_selection = selec.getString(1);
                                    }
                                    System.out.println("login is "+account_selection);

                                    if (account_selection.toString().equals("Dine")) {
                                        WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                                    }else {
                                        if (account_selection.toString().equals("Qsr")) {
                                            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                                        }else {
                                            WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
                                        }
                                    }

                                    SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getContext());
                                    String company = sharedpreferences.getString("companyname", null);
                                    String store = sharedpreferences.getString("storename", null);
                                    String device = sharedpreferences.getString("devicename", null);


                                    JSONObject params = new JSONObject();
                                    try {
                                        params.put("company", company);
                                        params.put("store", store);
                                        params.put("device", device);
                                        params.put("data", jsonArray);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    System.out.println("sync table2 "+company+store+device+params);

                                    Cursor synCursor=null;
                                    String lastid1="";
                                    try {
                                        synCursor = mydbappdata.rawQuery("SELECT * FROM " + table + " WHERE   _id = (SELECT MAX(_id)  FROM " + table + ")", null);
                                        if (synCursor.moveToFirst()) {
                                            lastid1 = synCursor.getString(0);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        if(synCursor!=null){
                                            synCursor.close();
                                        }
                                    }

                                    final String lastid=lastid1;
                                    JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, WebserviceUrl + "syncappdata.php", params,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject jarray) {
                                                    System.out.println("sync table array "+jarray);
                                                    String response = null;
                                                    try {
                                                        response = jarray.getString("status");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    System.out.println("sync table3 "+response);
                                                    if (response.equalsIgnoreCase("success")) {
                                                        ContentValues cv = new ContentValues();
                                                        cv.put("_id", id);
                                                        cv.put("tablename", table);
                                                        cv.put("lastsyncedid", lastid);
                                                        String where1 = "_id = " + id;
                                                        syncdbapp.update("appdata", cv, where1, new String[]{});
                                                    } else {

                                                    }

                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError volleyError) {

                                                }
                                            });

                                    queue.add(jobReq);
                                }else {
                                    System.out.println("sync table cannot insert");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if(syncCursor!=null){
                                    syncCursor.close();
                                }
                            }
                        }
                    }


                }while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor!=null){
                cursor.close();
            }

        }


    }


    private void syncUpdate(final String table,final String where) {

        SQLiteDatabase mydbappdata=null;
        final SQLiteDatabase syncdb1=ctx.openOrCreateDatabase("syncdbapp", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);
        Cursor cursor1=null;
        try {
            cursor1 = syncdb1.rawQuery("select * from appdata", null);

            mydbappdata = ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS, null);
            if (cursor1.moveToFirst()) {
                do {
                    final int lastsynced = cursor1.getInt(2);
                    final int id = cursor1.getInt(0);
                    final String table1 = cursor1.getString(1);

                    if ((!table1.equalsIgnoreCase("android_metadata")) && (!table1.equalsIgnoreCase("sqlite_sequence")) && (!table1.contains("Table"))) {
                        String account_selection = "";
                        Cursor selec = mydbappdata.rawQuery("SELECT * FROM Account_selection", null);
                        if (selec.moveToFirst()) {
                            account_selection = selec.getString(1);
                        }
                        System.out.println("login is2 "+account_selection);

                        if (account_selection.toString().equals("Dine")) {
                            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                        }else {
                            if (account_selection.toString().equals("Qsr")) {
                                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                            }else {
                                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
                            }
                        }
                    }
                } while (cursor1.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor1!=null){
                cursor1.close();
            }

        }

        SQLiteDatabase syncdbapp=null;

//        SQLiteDatabase mydbappdata=null;

        RequestQueue queue = Volley.newRequestQueue(ctx);
        mydbappdata=ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);

        Cursor syncCursor=null;
        try {
            syncCursor=mydbappdata.rawQuery("select * from "+table+" where "+where,null);
            if(syncCursor.getCount()>0) {
                JSONArray jsonArray;

                jsonArray = cur2Json(syncCursor,table);

                JSONObject tablejson = new JSONObject();
                try {
                    tablejson.put("table", table);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(tablejson);

                JSONObject tablejson1 = new JSONObject();
                try {
                    tablejson1.put("where", where);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(tablejson1);

                String account_selection = "";
                SQLiteDatabase db=ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);
                Cursor selec = db.rawQuery("SELECT * FROM Account_selection", null);
                if (selec.moveToFirst()) {
                    account_selection = selec.getString(1);
                }
                System.out.println("login is "+account_selection);

                if (account_selection.toString().equals("Dine")) {
                    WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                }else {
                    if (account_selection.toString().equals("Qsr")) {
                        WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
                    }else {
                        WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
                    }
                }

                SharedPreferences sharedpreferences =getDefaultSharedPreferencesMultiProcess(getContext());
                String company = sharedpreferences.getString("companyname", null);
                String store = sharedpreferences.getString("storename", null);
                String device = sharedpreferences.getString("devicename", null);


                JSONObject params = new JSONObject();
                try {
                    params.put("company", company);
                    params.put("store", store);
                    params.put("device", device);
                    params.put("data", jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("sync update "+company+store+device+params);

                JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, WebserviceUrl+"updateappdata.php", params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jarray) {
                                System.out.println("sync update array "+jarray);
                                String response = null;
                                try {
                                    response = jarray.getString("status");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("sync update "+response);
                                if (response.equalsIgnoreCase("success")) {

                                } else {

                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        });

                queue.add(jobReq);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(syncCursor!=null){
                syncCursor.close();
            }
        }

    }


    private void syncDelete(final String table,final String where) {

        RequestQueue queue = Volley.newRequestQueue(ctx);

        String account_selection = "";
        SQLiteDatabase db=ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);
        Cursor selec = db.rawQuery("SELECT * FROM Account_selection", null);
        if (selec.moveToFirst()) {
            account_selection = selec.getString(1);
        }
        System.out.println("login is "+account_selection);

        if (account_selection.toString().equals("Dine")) {
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }else {
            if (account_selection.toString().equals("Qsr")) {
                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
            }else {
                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
            }
        }

        SharedPreferences sharedpreferences =getDefaultSharedPreferencesMultiProcess(getContext());
        String company= sharedpreferences.getString("companyname", null);
        String store= sharedpreferences.getString("storename", null);
        String device= sharedpreferences.getString("devicename", null);


        JSONObject params = new JSONObject();
        try {
            params.put("company",company);
            params.put("store",store);
            params.put("device",device);
            params.put("where",where);
            params.put("table",table);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("sync delete "+company+store+device+params);

        JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, WebserviceUrl+"deleteappdata.php", params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jarray) {
                        String response= null;
                        try {
                            response = jarray.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("sync delete "+response);
                        if(response.equalsIgnoreCase("success")){

                        }else{

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });

        queue.add(jobReq);

    }



    public JSONArray cur2Json(Cursor cursor,String table) {

        SQLiteDatabase syncdbapp=null;

        SQLiteDatabase mydbappdata=null;

        JSONArray resultSet = null;
        try {
            resultSet = new JSONArray();
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                int totalColumn = cursor.getColumnCount();
                JSONObject rowObject = new JSONObject();
                for (int i = 0; i < totalColumn; i++) {
                    if (cursor.getColumnName(i) != null) {


                        if(table.equalsIgnoreCase("Items")||table.equalsIgnoreCase("Logo")){
//                            // mydbappdata=  SQLiteDatabase.openDatabase("mydb_Appdata", null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
//                            try {
//                                mydbappdata=ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                            String var = cursor.getColumnName(i);
                            //                        Cursor blobcursor= mydbappdata.rawQuery("select typeof (" + var + ") from " + table,null);
                            //                        blobcursor.moveToFirst();
                            //                        String type=blobcursor.getString(0);
                            if((var.equalsIgnoreCase("image"))&&(table.equalsIgnoreCase("Items"))){


                                byte[] image =cursor.getBlob(i);
                                if(image.length>1){
                                    String imgString = Base64.encodeToString(image,
                                            Base64.NO_WRAP);
                                    try {
                                        rowObject.put(cursor.getColumnName(i),
                                                imgString);
                                    } catch (Exception e) {
                                        //       Log.d(TAG, e.getMessage());
                                    }
                                }else{
                                    try {
                                        rowObject.put(cursor.getColumnName(i),
                                                "");
                                    } catch (Exception e) {
                                        //        Log.d(TAG, e.getMessage());
                                    }
                                }

                            }else if((var.equalsIgnoreCase("image"))&&(table.equalsIgnoreCase("Items1"))){


                                byte[] image =cursor.getBlob(i);
                                if(image.length>1){
                                    String imgString = Base64.encodeToString(image,
                                            Base64.NO_WRAP);
                                    try {
                                        rowObject.put(cursor.getColumnName(i),
                                                imgString);
                                    } catch (Exception e) {
                                        //         Log.d(TAG, e.getMessage());
                                    }
                                }else{
                                    try {
                                        rowObject.put(cursor.getColumnName(i),
                                                "");
                                    } catch (Exception e) {
                                        //          Log.d(TAG, e.getMessage());
                                    }
                                }

                            }else if((var.equalsIgnoreCase("companylogo"))&&(table.equalsIgnoreCase("Logo"))){


                                byte[] image =cursor.getBlob(i);
                                if(image.length>1){
                                    String imgString = Base64.encodeToString(image,
                                            Base64.NO_WRAP);
                                    try {
                                        rowObject.put(cursor.getColumnName(i),
                                                imgString);
                                    } catch (Exception e) {
                                        //           Log.d(TAG, e.getMessage());
                                    }
                                }else{
                                    try {
                                        rowObject.put(cursor.getColumnName(i),
                                                "");
                                    } catch (Exception e) {
                                        //       Log.d(TAG, e.getMessage());
                                    }
                                }

                            }else if((var.equalsIgnoreCase("modimage"))&&(table.equalsIgnoreCase("Modifiers"))){


                                byte[] image =cursor.getBlob(i);
                                if(image.length>1){
                                    String imgString = Base64.encodeToString(image,
                                            Base64.NO_WRAP);
                                    try {
                                        rowObject.put(cursor.getColumnName(i),
                                                imgString);
                                    } catch (Exception e) {
                                        //          Log.d(TAG, e.getMessage());
                                    }
                                }else{
                                    try {
                                        rowObject.put(cursor.getColumnName(i),
                                                "");
                                    } catch (Exception e) {
                                        //               Log.d(TAG, e.getMessage());
                                    }
                                }

                            }else if((var.equalsIgnoreCase("image"))&&(table.equalsIgnoreCase("asd1"))){


                                byte[] image =cursor.getBlob(i);
                                if(image.length>1){
                                    String imgString = Base64.encodeToString(image,
                                            Base64.NO_WRAP);
                                    try {
                                        rowObject.put(cursor.getColumnName(i),
                                                imgString);
                                    } catch (Exception e) {
                                        //              Log.d(TAG, e.getMessage());
                                    }
                                }else{
                                    try {
                                        rowObject.put(cursor.getColumnName(i),
                                                "");
                                    } catch (Exception e) {
                                        //                 Log.d(TAG, e.getMessage());
                                    }
                                }

                            }else{
                                try {
                                    rowObject.put(var,
                                            cursor.getString(i));
                                } catch (Exception e) {
                                    //           Log.d(TAG, e.getMessage());
                                }
                            }
                        }else{
                            try {
                                rowObject.put(cursor.getColumnName(i),
                                        cursor.getString(i));
                            } catch (Exception e) {
                                //          Log.d(TAG, e.getMessage());
                            }
                        }




                    }
                }
                resultSet.put(rowObject);
                cursor.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor!=null)
                cursor.close();
        }


        return resultSet;

    }



    private static boolean isSyncActive(Account account, String authority)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            return isSyncActiveHoneycomb(account, authority);
        } else
        {
            SyncInfo currentSync = ContentResolver.getCurrentSync();
            return currentSync != null && currentSync.account.equals(account) &&
                    currentSync.authority.equals(authority);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static boolean isSyncActiveHoneycomb(Account account, String authority)
    {
        for(SyncInfo syncInfo : ContentResolver.getCurrentSyncs())
        {
            if(syncInfo.account.equals(account) &&
                    syncInfo.authority.equals(authority))
            {
                return true;
            }
        }
        return false;
    }


    public void webservicequery(final String webserviceQuery){

        String account_selection = "";
        SQLiteDatabase db=ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);
        Cursor selec = db.rawQuery("SELECT * FROM Account_selection", null);
        if (selec.moveToFirst()) {
            account_selection = selec.getString(1);
        }
        System.out.println("login is "+account_selection);

        if (account_selection.toString().equals("Dine")) {
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }else {
            if (account_selection.toString().equals("Qsr")) {
                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
            }else {
                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
            }
        }

        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getContext());
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
        RequestQueue queue;
        StringRequest sr1;
        // queue = Volley.newRequestQueue(getActivity());

        queue= RequestSingleton.getInstance(getContext()).getInstance();

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