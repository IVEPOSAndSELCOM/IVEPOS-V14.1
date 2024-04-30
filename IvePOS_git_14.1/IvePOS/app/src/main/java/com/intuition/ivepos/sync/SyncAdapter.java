package com.intuition.ivepos.sync;

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
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

/**
 * Created by HP on 8/10/2018.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    Context ctx;
    private String TAG="syncadapter";
    String all_sales="0",all_sales_cancelled="0",billnumber="0",cardnumber="0";
    String WebserviceUrl;


    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
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
    public SyncAdapter(
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
                download();
            }else{
                syncperiodic();
            }
        }else{
            syncperiodic();
        }




    }

    private void download() {
        SQLiteDatabase syncdb=null;
        SQLiteDatabase mydbsalesdata=null;
        RequestQueue queue = Volley.newRequestQueue(ctx);
        syncdb=ctx.openOrCreateDatabase("syncdb", Context.MODE_PRIVATE,null);

        Cursor cursor=null;
        try {

            cursor=syncdb.rawQuery("select * from salesdata",null);
            mydbsalesdata=ctx.openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE,null);
            if(cursor.moveToFirst()){
                do{
                    final int lastsynced= cursor.getInt(2);
                    final int id= cursor.getInt(0);
                    final String table=cursor.getString(1);

                    if((!table.equalsIgnoreCase("android_metadata"))&&(!table.equalsIgnoreCase("sqlite_sequence"))){


                        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getContext());
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


                   /*     JsonArrayRequest jobReq = new JsonArrayRequest(Request.Method.POST, Constant.WebserviceUrl+"downloadsalesdata.php", params, new Response.Listener<JSONArray>() {
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
                                        mydbsalesdata=ctx.openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE,null);
                                        long rowid=mydbsalesdata.insert(table, null, cv);
                                        if(rowid!=-1){
                                            Cursor synCursor=mydbsalesdata.rawQuery("SELECT * FROM "+table+" WHERE   _id = (SELECT MAX(_id)  FROM "+table+")",null);
                                            if(synCursor.moveToFirst()){
                                                lastid=  synCursor.getString(0);
                                            }
                                            ContentValues cv1=new ContentValues();
                                            cv1.put("_id",id);
                                            cv1.put("tablename",table);
                                            cv1.put("lastsyncedid",lastid);
                                            String where1 = "_id = "+id;
                                            syncdb.update("salesdata", cv, where1, new String[]{});
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
                        queue.add(jobReq);*/
    /*
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
                                });*/

                        //    queue.add(jobReq);
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

    private void syncperiodic() {
        RequestQueue queue = Volley.newRequestQueue(ctx);

        SQLiteDatabase mydbsalesdata=null;
        SQLiteDatabase mydbappdata=null;
        final SQLiteDatabase syncdb=ctx.openOrCreateDatabase("syncdb", Context.MODE_PRIVATE,null);


        Cursor cursor=null;
        try {
            cursor=syncdb.rawQuery("select * from salesdata",null);

            mydbsalesdata=ctx.openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE,null);
            mydbappdata=ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
            if(cursor.moveToFirst()){
                do{
                    final int lastsynced= cursor.getInt(2);
                    final int id= cursor.getInt(0);
                    final String table=cursor.getString(1);

                    if((!table.equalsIgnoreCase("android_metadata"))&&(!table.equalsIgnoreCase("sqlite_sequence"))&&(!table.contains("Table"))){
                        mydbsalesdata=ctx.openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE,null);
                        Cursor syncCursor=null;
                        try {
                            syncCursor=mydbsalesdata.rawQuery("select * from "+table+" where _id >"+lastsynced,null);

                            if(syncCursor.getCount()>0) {

                                JSONArray jsonArray;

                                jsonArray = cur2Json(syncCursor);


                                JSONObject tablejson = new JSONObject();
                                try {
                                    tablejson.put("table", table);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                jsonArray.put(tablejson);

                                if ((!table.equalsIgnoreCase("android_metadata")) && (!table.equalsIgnoreCase("sqlite_sequence")) && (!table.contains("Table"))) {
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
                                String lastid1 ="";
                                try {
                                    synCursor = mydbsalesdata.rawQuery("SELECT * FROM " + table + " WHERE   _id = (SELECT MAX(_id)  FROM " + table + ")", null);
                                    if (synCursor.moveToFirst()) {
                                        lastid1 = synCursor.getString(0);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    if(synCursor!=null)
                                        synCursor.close();
                                }
                                final String lastid = lastid1;
                                JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, WebserviceUrl+"syncsalesdata.php", params,
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
                                                    syncdb.update("salesdata", cv, where1, new String[]{});
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
                            if(syncCursor!=null)
                                syncCursor.close();
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

        SQLiteDatabase mydbsalesdata=null;
        RequestQueue queue = Volley.newRequestQueue(ctx);

        SQLiteDatabase mydbappdata=null;



        final SQLiteDatabase syncdb=ctx.openOrCreateDatabase("syncdb", Context.MODE_PRIVATE,null);

        Cursor cursor=null;

//        Log.d("synctable count",cursor.getCount()+"");

        try {
            cursor=syncdb.rawQuery("select * from salesdata where tablename = '"+table+"'",null);
            if(cursor.moveToFirst()){
                do{
                    final int lastsynced= cursor.getInt(2);
                    final int id= cursor.getInt(0);
                    mydbsalesdata=ctx.openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE,null);
                    mydbappdata=ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);

                    //   Log.d("synctable",table);
                    //    Log.d("lastsynced",lastsynced+"");
                    Cursor syncCursor=null;
                    try {

                        syncCursor=mydbsalesdata.rawQuery("select * from "+table+" where _id >"+lastsynced,null);

                        if(syncCursor.getCount()>0) {

                            JSONArray jsonArray;

                            jsonArray = cur2Json(syncCursor);


                            JSONObject tablejson = new JSONObject();
                            try {
                                tablejson.put("table", table);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            jsonArray.put(tablejson);

                            if ((!table.equalsIgnoreCase("android_metadata")) && (!table.equalsIgnoreCase("sqlite_sequence")) && (!table.contains("Table"))) {
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

                            System.out.println("sync update "+company+store+device+params);

                            Cursor synCursor=null;
                            String lastid1="";
                            try {
                                synCursor = mydbsalesdata.rawQuery("SELECT * FROM " + table + " WHERE   _id = (SELECT MAX(_id)  FROM " + table + ")", null);
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

                            //       Log.d("synctable params", params.toString());

                            final String lastid= lastid1;
                            JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, WebserviceUrl+"syncsalesdata.php", params,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject jarray) {
                                            String response = null;
                                            //   Log.d("synctable response",response.toString());
                                            try {
                                                response = jarray.getString("status");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            if (response.equalsIgnoreCase("success")) {

                                                if(table.equalsIgnoreCase("All_Sales")||table.equalsIgnoreCase("All_Sales_Cancelled")||table.equalsIgnoreCase("Cardnumber")||table.equalsIgnoreCase("Billnumber")){

                                                    syncsales();
                                                }else{
                                                    ContentValues cv = new ContentValues();
                                                    cv.put("_id", id);
                                                    cv.put("tablename", table);
                                                    cv.put("lastsyncedid", lastid);
                                                    String where1 = "_id = " + id;
                                                    syncdb.update("salesdata", cv, where1, new String[]{});
                                                }


                                            } else {

                                            }

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {
                                            //                      Log.d("synctable error", volleyError.toString());
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

                }while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor!=null)
                cursor.close();
        }


    }

    public JSONArray cur2Json(Cursor cursor) {

        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        rowObject.put(cursor.getColumnName(i),
                                cursor.getString(i));
                    } catch (Exception e) {
                        //          Log.d(TAG, e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        try {
            cursor.close();
        } finally {
            if(cursor!=null){
                cursor.close();
            }

        }
        return resultSet;

    }

    private void syncDelete(final String table,final String where) {

        String account_selection = "";
        SQLiteDatabase db=ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
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

        RequestQueue queue = Volley.newRequestQueue(ctx);

        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getContext());
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
        JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, WebserviceUrl+"deletesalesdata.php", params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jarray) {
                        String response= null;
                        try {
                            response = jarray.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    private void syncUpdate(final String table,final String where) {
        SQLiteDatabase syncdb=null;
        SQLiteDatabase mydbsalesdata=null;
        SQLiteDatabase mydbappdata=null;
        RequestQueue queue = Volley.newRequestQueue(ctx);
        mydbsalesdata=ctx.openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE,null);
        mydbappdata=ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);

        Cursor syncCursor=null;

        try {
            syncCursor=mydbsalesdata.rawQuery("select * from "+table+" where "+where,null);
            if(syncCursor.getCount()>0) {
                JSONArray jsonArray;

                jsonArray = cur2Json(syncCursor);

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
                Cursor selec = mydbappdata.rawQuery("SELECT * FROM Account_selection", null);
                if (selec.moveToFirst()) {
                    account_selection = selec.getString(1);
                }
                System.out.println("login is1 "+account_selection);

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

                JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, WebserviceUrl+"updatesalesdata.php", params,
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


    public void syncsales() {

        SQLiteDatabase mydbappdata=null;
        mydbappdata=ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
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

        final SQLiteDatabase syncdb=ctx.openOrCreateDatabase("syncdb", Context.MODE_PRIVATE,null);
        SharedPreferences sharedpreferences=getDefaultSharedPreferencesMultiProcess(getContext());
        String company= sharedpreferences.getString("companyname", null);
        String store= sharedpreferences.getString("storename", null);
        String device= sharedpreferences.getString("devicename", null);

        JSONObject params = new JSONObject();

        try {
            params.put("device",device);
            params.put("store",store);
            params.put("company",company);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  if(queue==null){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        // }


        JsonObjectRequest sr = new JsonObjectRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"getlastids.php",params,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responseString) {


                        Log.e("responseString",responseString.toString());
                        String response= "";
                        JSONObject jsonObject=null;
                        try {
                            jsonObject=responseString;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            response = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(response.equalsIgnoreCase("success")){

                            try {

                                all_sales=jsonObject.getString("all_sales");
                                all_sales_cancelled=jsonObject.getString("all_sales_cancelled");
                                billnumber=jsonObject.getString("billnumber");
                                cardnumber=jsonObject.getString("cardnumber");

                                Log.e("all_sales",all_sales);
                                Log.e("all_sales_cancelled",all_sales_cancelled);
                                Log.e("billnumber",billnumber);
                                Log.e("cardnumber",cardnumber);

                                ContentValues contentValues = new ContentValues();
                                contentValues.put("lastsyncedid", Integer.parseInt(all_sales));
                                String wherecu = "tablename = '" + "All_Sales" + "'";
                                int update1 =syncdb.update("salesdata", contentValues, wherecu, new String[]{});

                                Log.e("upadate1",update1+"");



                                contentValues = new ContentValues();
                                contentValues.put("lastsyncedid", Integer.parseInt(all_sales_cancelled));
                                wherecu = "tablename = '" + "All_Sales_Cancelled" + "'";
                                update1 =syncdb.update("salesdata", contentValues, wherecu, new String[]{});

                                Log.e("upadate1",update1+"");



                                contentValues = new ContentValues();
                                contentValues.put("lastsyncedid", Integer.parseInt(cardnumber));
                                wherecu = "tablename = '" + "Cardnumber" + "'";
                                update1 =syncdb.update("salesdata", contentValues, wherecu, new String[]{});

                                Log.e("upadate1",update1+"");




                                contentValues = new ContentValues();
                                contentValues.put("lastsyncedid", Integer.parseInt(billnumber));
                                wherecu = "tablename = '" + "Billnumber" + "'";
                                update1 = syncdb.update("salesdata", contentValues, wherecu, new String[]{});

                                Log.e("upadate1",update1+"");



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }else{

                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                })  {

        };
/*    sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        sr.setRetryPolicy(new DefaultRetryPolicy(0,-1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);

    }



}