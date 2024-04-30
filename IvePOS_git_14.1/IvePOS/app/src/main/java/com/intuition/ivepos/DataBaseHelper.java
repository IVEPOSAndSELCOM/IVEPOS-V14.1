package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 2/6/2015.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper
{

    private static DataBaseHelper instance;


    private static final String DATABASE_NAME = "mydb_Appdata";
    private static final int DATABASE_VERSION = 1;

    public DataBaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Called when no database exists in disk and the helper class needs
    // to create a new one.
    @Override
    public void onCreate(SQLiteDatabase _db)
    {

        _db.execSQL(LoginDataBaseAdapter.TABLE_ITEMLIST);

       

    }
    // Called when there is a database version mismatch meaning that the version
    // of the database on disk needs to be upgraded to the current version.
    @Override
    public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion)
    {
        // Log the version upgrade.
        Log.w("TaskDBAdapter", "Upgrading from version " +_oldVersion + " to " +_newVersion + ", which will destroy all old data");

        // Upgrade the existing database to conform to the new version. Multiple
        // previous versions can be handled by comparing _oldVersion and _newVersion
        // values.
        // The simplest case is to drop the old table and create a new one.
        _db.execSQL("DROP TABLE IF EXISTS " + "TEMPLATE" );
        // Create a new one.
        onCreate(_db);
    }


    public static synchronized DataBaseHelper getHelper(Context context) {
        if (instance == null)
            instance = new DataBaseHelper(context);
        return instance;
    }
}