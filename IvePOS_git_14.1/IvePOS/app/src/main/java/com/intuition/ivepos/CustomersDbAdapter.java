package com.intuition.ivepos;

/**
 * Created by HP on 7/5/2018.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CustomersDbAdapter {

    public static final String KEY_ROWID = "rowid";
    public static final String KEY_CUSTOMER = "itemname";
    public static final String KEY_NAME = "category";
    public static final String KEY_ADDRESS1 = "barcodevalue";
    public static final String KEY_ADDRESS2 = "stockquan";
    public static final String KEY_ADDRESS3 = "price";
//    public static final String KEY_ADDRESS2 = "address2";
//    public static final String KEY_CITY = "city";
//    public static final String KEY_STATE = "state";
//    public static final String KEY_ZIP = "zipCode";
//    public static final String KEY_SEARCH = "searchData";

    private static final String TAG = "CustomersDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "mydb_Appdata";
    private static final String FTS_VIRTUAL_TABLE = "Items_Virtual";
    private static final int DATABASE_VERSION = 1;

    //Create a FTS3 Virtual Table for fast searches
    private static final String DATABASE_CREATE =
            "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE + " USING fts3(" +
                    KEY_CUSTOMER + "," +
                    KEY_NAME + "," +
                    KEY_ADDRESS2 + "," +
                    KEY_ADDRESS1 + "," +
                    KEY_ADDRESS3 + "," +
                    " UNIQUE (" + KEY_CUSTOMER + "));";


    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
//            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }
    }

    public CustomersDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public CustomersDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }


    public long createCustomer(String itemname, String category, String barcodevalue) {

        ContentValues initialValues = new ContentValues();
        String searchValue = itemname + " " +
                category + " " +
                barcodevalue;
        initialValues.put(KEY_CUSTOMER, itemname);
        initialValues.put(KEY_NAME, category);
        initialValues.put(KEY_ADDRESS1, barcodevalue);

        return mDb.insert(FTS_VIRTUAL_TABLE, null, initialValues);
    }


    public Cursor searchCustomer(String s) throws SQLException {
        Log.w(TAG, s);
        String query = "SELECT docid as _id," +
                "itemname" +","+
                "barcode_value" +
                " from " + "Items_Virtual" +
                " where " + "Items_Virtual" + " MATCH '" + s + "';";
        Log.w(TAG, query);
        Cursor mCursor = mDb.rawQuery(query, null);

//        if (mCursor != null) {
//            mCursor.moveToFirst();
//        }
        return mCursor;

    }


    public boolean deleteAllCustomers() {

        int doneDelete = 0;
        doneDelete = mDb.delete(FTS_VIRTUAL_TABLE, null, null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }



}