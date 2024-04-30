package com.intuition.ivepos.syncapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by HP on 8/10/2018.
 */

public class StubProviderApp extends ContentProvider{

    public static final String AUTHORITY = "com.intuition.ivepos.syncapp.provider";//specific for our our app, will be specified in maninfed
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    @Override
    public boolean onCreate() {
        SQLiteDatabase database = getContext().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        return true;
    }

    @Override
    public int delete(Uri uri, String where, String[] args) {
//        String table = getTableName(uri);
//        SQLiteDatabase dataBase=getContext().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
//        return dataBase.delete(table, where, args);
        String table = getTableName(uri);
        SQLiteDatabase dataBase=getContext().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        int a = dataBase.delete(table, where, args);
        dataBase.close();
        return a;
    }

    @Override
    public String getType(Uri arg0) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        String table = getTableName(uri);
        SQLiteDatabase database = getContext().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        long value = database.insert(table, null, initialValues);

        if (value > 0) {
            Uri notesUri  = Uri.withAppendedPath(CONTENT_URI, table);
           /* notesUri = ContentUris.withAppendedId(notesUri,
                    value);*/
            //  getContext().getContentResolver().notifyChange(notesUri, null);
            return notesUri;
        }
        throw new IllegalArgumentException("<Illegal>Unknown URI: " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String table =getTableName(uri);
        SQLiteDatabase database = getContext().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        Cursor cursor =database.query(table,  projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String whereClause,
                      String[] whereArgs) {
        String table = getTableName(uri);
        SQLiteDatabase database = getContext().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        return database.update(table, values, whereClause, whereArgs);
    }

    public static String getTableName(Uri uri){
        String value = uri.getPath();
        value = value.replace("/", "");//we need to remove '/'
        return value;
    }
}