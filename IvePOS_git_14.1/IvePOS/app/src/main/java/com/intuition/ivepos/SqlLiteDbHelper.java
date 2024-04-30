package com.intuition.ivepos;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
public class SqlLiteDbHelper extends SQLiteOpenHelper {
    private final static String TAG = "SqlLiteDbHelper";
    private final Context myContext;
    private static final String DATABASE_NAME = "mydb_Appdata";
    private static final int DATABASE_VERSION = 1;
    private String pathToSaveDBFile;

    private static final File DATA_DIRECTORY_DATABASE =
            new File(Environment.getDataDirectory() +
                    "/data/" + "com.intuition.ivepos" +
                    "/databases/" + "mydb_Appdata");

    public SqlLiteDbHelper(Context context, String filePath) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        pathToSaveDBFile = new StringBuffer(filePath).append("/").append(DATABASE_NAME).toString();
    }
    public void prepareDatabase() throws IOException {
        try {
            this.getReadableDatabase();
            copyDataBase();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }
    private boolean checkDataBase() {
        boolean checkDB = false;
        try {
            File file = new File(pathToSaveDBFile);
            checkDB = file.exists();
        } catch(SQLiteException e) {
            Log.d(TAG, e.getMessage());
        }
        return checkDB;
    }
    private void copyDataBase() throws IOException {

        try {

            AssetManager am = myContext.getAssets();
            OutputStream os = new FileOutputStream(DATA_DIRECTORY_DATABASE);
            byte[] b = new byte[1024];
            String[] files = am.list("");
//                Arrays.sort(files);
            int r;
//                for (int i = 1; i <= 16; i++) {
            InputStream is = am.open("sqlite/grocery/"+DATABASE_NAME);
            while ((r = is.read(b)) > 0) {
                os.write(b, 0, r);
            }
            Log.i("BABY_DATABASE_HELPER", "Copying the database ");
            is.close();
//                }
            os.close();
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }

//        OutputStream os = new FileOutputStream(pathToSaveDBFile);
//        InputStream is = myContext.getAssets().open("sqlite/grocery/"+DATABASE_NAME);
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = is.read(buffer)) > 0) {
//            os.write(buffer, 0, length);
//        }
//        is.close();
//        os.flush();
//        os.close();
    }
    public void deleteDb() {
        File file = new File(pathToSaveDBFile);
        if(file.exists()) {
            file.delete();
            Log.d(TAG, "Database deleted.");
        }
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    public List<Contact> getEmployees() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT * FROM Items";
        Cursor cursor = db.rawQuery(query, null);
        List<Contact> list = new ArrayList<Contact>();
        if (cursor.moveToFirst()) {
            do {
                Contact employee = new Contact();
                employee.setId(cursor.getInt(0));
                employee.setName(cursor.getString(1));
                employee.setAge(cursor.getInt(2));
                list.add(employee);
            }while (cursor.moveToNext());
        }
        db.close();
        return list;
    }
//    private int getVersionId() {
//        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
//        String query = "SELECT version_id FROM dbVersion";
//        Cursor cursor = db.rawQuery(query, null);
//        cursor.moveToFirst();
//        int v =  cursor.getInt(0);
//        db.close();
//        return v;
//    }
}
