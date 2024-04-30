package com.intuition.ivepos.syncdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by HP on 8/3/2018.
 */

public class SyncDatabase {
    SQLiteDatabase syncdb=null;
    SQLiteDatabase syncdbapp=null;
    SQLiteDatabase mydbappdata=null;
    SQLiteDatabase mydbsalesdata=null;

    public void createSyncDb(Context ctx){
        syncdb=ctx.openOrCreateDatabase("syncdb", Context.MODE_PRIVATE,null);
        syncdbapp=ctx.openOrCreateDatabase("syncdbapp", Context.MODE_PRIVATE,null);

        syncdbapp.execSQL("CREATE TABLE if not exists appdata (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, tablename text, lastsyncedid integer);");
        syncdb.execSQL("CREATE TABLE if not exists salesdata (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, tablename text, lastsyncedid integer);");

        mydbappdata=ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        mydbsalesdata=ctx.openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE,null);

        // syncdbapp.delete("appdata",null,null);
        //   syncdb.delete("salesdata",null,null);
        Cursor c = mydbappdata.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                String tablename=c.getString(0);
           /*     Cursor tableCursor=mydbappdata.rawQuery("SELECT MAX(_id) FROM "+tablename,null);
               if(tableCursor.moveToFirst()){
                   int lastid=tableCursor.getInt(0);
                   ContentValues cv=new ContentValues();
                   cv.put("tablename",tablename);
                   cv.put("lastsyncedid",lastid);
                   syncdb.insert("appdata",null,cv);
               }else{*/
                int lastid=0;
                ContentValues cv=new ContentValues();
                cv.put("tablename",tablename);
                cv.put("lastsyncedid",lastid);
                syncdbapp.insert("appdata",null,cv);
                Log.d("table-app",tablename);
                c.moveToNext();
                // }
            }
        }
        c.close();

        Cursor c1 = mydbsalesdata.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c1.moveToFirst()) {
            while ( !c1.isAfterLast() ) {
                String tablename=c1.getString(0);
           /*     Cursor tableCursor=mydbsalesdata.rawQuery("SELECT MAX(_id) FROM "+tablename,null);
                if(tableCursor.moveToFirst()){
                    int lastid=tableCursor.getInt(0);
                    ContentValues cv=new ContentValues();
                    cv.put("tablename",tablename);
                    cv.put("lastsyncedid",lastid);
                    syncdb.insert("salesdata",null,cv);
                }else{*/
                int lastid=0;
                ContentValues cv=new ContentValues();
                cv.put("tablename",tablename);
                cv.put("lastsyncedid",lastid);
                syncdb.insert("salesdata",null,cv);
                Log.d("table-sales",tablename);
                c1.moveToNext();
                //}
            }
        }
        c1.close();


    }

    public void updateSyncDb(Context ctx){
        syncdb=ctx.openOrCreateDatabase("syncdb", Context.MODE_PRIVATE,null);

        syncdb.execSQL("CREATE TABLE if not exists salesdata (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, tablename text, lastsyncedid integer);");


        syncdb.execSQL("delete from salesdata");


        mydbsalesdata=ctx.openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE,null);



        Cursor c1 = mydbsalesdata.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c1.moveToFirst()) {
            while ( !c1.isAfterLast() ) {
                String tablename=c1.getString(0);
                if((!tablename.equalsIgnoreCase("android_metadata"))&&(!tablename.equalsIgnoreCase("sqlite_sequence"))&&(!tablename.contains("Items_Virtual"))&&(!tablename.contains("ITEMSLIST"))) {
                    Cursor tableCursor = mydbsalesdata.rawQuery("SELECT MAX(_id) FROM " + tablename, null);
                    if (tableCursor.moveToFirst()) {
                        int lastid = tableCursor.getInt(0);
                        ContentValues cv = new ContentValues();
                        cv.put("tablename", tablename);
                        cv.put("lastsyncedid", lastid);
                        syncdb.insert("salesdata", null, cv);
                        c1.moveToNext();
                    } else {
                        int lastid = 0;
                        ContentValues cv = new ContentValues();
                        cv.put("tablename", tablename);
                        cv.put("lastsyncedid", lastid);
                        syncdb.insert("salesdata", null, cv);
                        Log.d("table-sales", tablename);
                        c1.moveToNext();
                    }
                }else{
                    c1.moveToNext();
                }
            }
        }
        c1.close();
        //  updateSyncDbApp(ctx);


    }


    public void updateSyncDbApp(Context ctx){

        syncdbapp=ctx.openOrCreateDatabase("syncdbapp", Context.MODE_PRIVATE,null);
        syncdbapp.execSQL("CREATE TABLE if not exists appdata (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, tablename text, lastsyncedid integer);");
        syncdbapp.execSQL("delete from appdata");
        mydbappdata=ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);

        Cursor c = mydbappdata.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                String tablename=c.getString(0);

                if((!tablename.equalsIgnoreCase("android_metadata"))&&(!tablename.equalsIgnoreCase("sqlite_sequence"))&&(!tablename.contains("Items_Virtual"))&&(!tablename.contains("ITEMSLIST"))) {
                    Cursor tableCursor=null;
                    if (tablename.toString().equals("LOGIN")){
                        tableCursor = mydbappdata.rawQuery("SELECT MAX(ID) FROM " + tablename, null);
                    }else{
                        tableCursor = mydbappdata.rawQuery("SELECT MAX(_id) FROM " + tablename, null);
                    }

                    if (tableCursor.moveToFirst()) {
                        int lastid = tableCursor.getInt(0);
                        ContentValues cv = new ContentValues();
                        cv.put("tablename", tablename);
                        cv.put("lastsyncedid", lastid);
                        syncdbapp.insert("appdata", null, cv);
                        c.moveToNext();
                    } else {
                        int lastid = 0;
                        ContentValues cv = new ContentValues();
                        cv.put("tablename", tablename);
                        cv.put("lastsyncedid", lastid);
                        syncdbapp.insert("appdata", null, cv);
                        Log.d("table-app", tablename);
                        c.moveToNext();
                    }
                    tableCursor.close();
                }else{
                    c.moveToNext();
                }
            }
        }
        c.close();

    }




    public void clearSyncDbApp(Context ctx){

        syncdbapp=ctx.openOrCreateDatabase("syncdbapp", Context.MODE_PRIVATE,null);
        syncdbapp.execSQL("CREATE TABLE if not exists appdata (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, tablename text, lastsyncedid integer);");
        syncdbapp.execSQL("delete from appdata");
        mydbappdata=ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);

        Cursor c = mydbappdata.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                String tablename=c.getString(0);

                if((!tablename.equalsIgnoreCase("android_metadata"))&&(!tablename.equalsIgnoreCase("sqlite_sequence"))&&(!tablename.contains("Items_Virtual"))&&(!tablename.contains("ITEMSLIST"))) {

                    int lastid = 0;
                    ContentValues cv = new ContentValues();
                    cv.put("tablename", tablename);
                    cv.put("lastsyncedid", lastid);
                    syncdbapp.insert("appdata", null, cv);
                    Log.d("table-app", tablename);
                    c.moveToNext();

                }else{
                    c.moveToNext();
                }
            }
        }
        c.close();

    }



    public void clearSyncDb(Context ctx){
        syncdb=ctx.openOrCreateDatabase("syncdb", Context.MODE_PRIVATE,null);

        syncdb.execSQL("CREATE TABLE if not exists salesdata (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, tablename text, lastsyncedid integer);");


        syncdb.execSQL("delete from salesdata");


        mydbsalesdata=ctx.openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE,null);



        Cursor c1 = mydbsalesdata.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c1.moveToFirst()) {
            while ( !c1.isAfterLast() ) {
                String tablename=c1.getString(0);
                if((!tablename.equalsIgnoreCase("android_metadata"))&&(!tablename.equalsIgnoreCase("sqlite_sequence"))&&(!tablename.contains("Items_Virtual"))&&(!tablename.contains("ITEMSLIST"))) {


                    int lastid = 0;
                    ContentValues cv = new ContentValues();
                    cv.put("tablename", tablename);
                    cv.put("lastsyncedid", lastid);
                    syncdb.insert("salesdata", null, cv);
                    Log.d("table-sales", tablename);
                    c1.moveToNext();
                }else{
                    c1.moveToNext();
                }
            }
        }
        c1.close();


    }

}