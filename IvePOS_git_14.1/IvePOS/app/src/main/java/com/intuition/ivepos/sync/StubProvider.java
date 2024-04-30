package com.intuition.ivepos.sync;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by HP on 8/10/2018.
 */

public class StubProvider extends ContentProvider{

    public static final String AUTHORITY = "com.intuition.ivepos.provider";//specific for our our app, will be specified in maninfed
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    @Override
    public boolean onCreate() {
        SQLiteDatabase database = getContext().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE,null);
        return true;
    }

    @Override
    public int delete(Uri uri, String where, String[] args) {
        String table = getTableName(uri);
        SQLiteDatabase dataBase=getContext().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE,null);
       // getContext().getContentResolver().notifyChange(uri, null);
        return dataBase.delete(table, where, args);
    }

    @Override
    public String getType(Uri arg0) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        String table = getTableName(uri);
        SQLiteDatabase database = getContext().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE,null);
        long rowId = database.insert(table, null, initialValues);
        System.out.println("itemname is there sync "+initialValues);

        if (rowId > 0) {
            Uri notesUri  = Uri.withAppendedPath(CONTENT_URI, table);
            System.out.println("itemname is there sync1111 "+initialValues);
       /*     notesUri = ContentUris.withAppendedId(notesUri,
                    rowId);*/
          //  getContext().getContentResolver().notifyChange(notesUri, null);
            return notesUri;
        }
        throw new IllegalArgumentException("<Illegal>Unknown URI: " + uri);

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
//        String table =getTableName(uri);
//        SQLiteDatabase database = getContext().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE,null);
//        Cursor cursor =database.query(table,  projection, selection, selectionArgs, null, null, sortOrder);
//        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String whereClause,
                      String[] whereArgs) {
        String table = getTableName(uri);
        SQLiteDatabase database = getContext().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE,null);
        int count=database.update(table, values, whereClause, whereArgs);
       // getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    public static String getTableName(Uri uri){
        String value = uri.getPath();
        value = value.replace("/", "");//we need to remove '/'
        return value;
    }
}