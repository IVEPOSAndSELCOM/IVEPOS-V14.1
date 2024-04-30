package com.intuition.ivepos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import com.example.hp.amazon.Charity;
//import com.example.hp.amazon.Children;



/**
 * Created by HATSUN on 3/23/2017.
 */

public class DatabaseHelper1 extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "amazon.db";
    private static final int DATABASE_VERSION = 10;
    private static final String TABLE_REGISTER = "register";
    private static final String TABLE_FOLDER = "folder";
    private static final String TABLE_STORE = "store";


    public static final String KEY_ID = "id";
    public static final String COMPANY_NAME = "companyname";
    public static final String STORE = "storename";
    public static final String DEVICE = "devicename";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String CONFIRMPASSWORD = "confirmpassword";

    public static final String FOLDER_ID = "folder_id";
    public static final String FILE_NAME = "filename";
    public static final String FOLDER_NAME = "foldername";


    public static final String STORE_ID = "store_id";
    public static final String STORE_USER_NAME = "storeusername";
    public static final String STORE_PASSWORD = "storepassword";
    public static final String STORE_NAME = "storename";


    private static final String CREATE_TABLE = "CREATE TABLE if not exists "
            + TABLE_REGISTER + "(" + KEY_ID + " INTEGER PRIMARY KEY ,"
            + COMPANY_NAME + " TEXT,"
            + STORE + " TEXT,"
            + DEVICE + " TEXT,"
            + EMAIL + " TEXT,"
            + PASSWORD + " TEXT,"
            + CONFIRMPASSWORD + " TEXT)";


//    private static final String CREATE_TABLE_COMPANY_FOLDER = "CREATE TABLE if not exists "
//            + TABLE_FOLDER + "(" + FOLDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//            + FILE_NAME + " TEXT,"
//            + FOLDER_NAME + " TEXT)";
//

//    private static final String CREATE_TABLE_STORE = "CREATE TABLE if not exists "
//            + TABLE_STORE + "(" + STORE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//            + STORE_USER_NAME+ " TEXT,"
//            + STORE_PASSWORD + " TEXT,"
//            + STORE_NAME + " TEXT)";


    public DatabaseHelper1(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
//        db.execSQL(CREATE_TABLE_STORE);
//        db.execSQL(CREATE_TABLE_COMPANY_FOLDER);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTER);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE);
        onCreate(db);
    }

    public void insert(Charity charity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
//        cv.put(KEY_ID,charity.getId());
        cv.put(COMPANY_NAME, charity.getCompanyName());
        cv.put(STORE, charity.getStorename());
        cv.put(DEVICE, charity.getDevicename());
        cv.put(EMAIL, charity.getEmail());
        cv.put(PASSWORD, charity.getPassword());
        cv.put(CONFIRMPASSWORD, charity.getPassword());


////        db.insert(TABLE_REGISTER,null,cv);
//        db.insertWithOnConflict(TABLE_REGISTER, null, cv,SQLiteDatabase.CREATE_IF_NECESSARY);
        long id = db.insert(TABLE_REGISTER, null, cv);
//        if(id != -1){
//            System.out.println("Inserted charity details...");
//
//        }else {
//            System.out.println("Not inserted charity details...");
//        }


    }

    public void insertStore(Store store) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        cv.put(KEY_ID,charity.getId());
        contentValues.put(STORE_USER_NAME, store.getStoreuserName());
        contentValues.put(STORE_PASSWORD, store.getStorepassword());
        contentValues.put(STORE_NAME, store.getStorename());

////        db.insert(TABLE_REGISTER,null,cv);
//        db.insertWithOnConflict(TABLE_REGISTER, null, cv,SQLiteDatabase.CREATE_IF_NECESSARY);
        long id = db.insert(TABLE_STORE, null, contentValues);
//        long id1 = db.insert(TABLE_FOLDER, null, contentValues);
//        if(id != -1){
//            System.out.println("Inserted charity details...");
//
//        }else {
//            System.out.println("Not inserted charity details...");
//        }


    }

//    public void insertFolder(Folder folder) {
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        //   for(int i=0;i<=specialization.size();i++) {
//        cv.put(FOLDER_ID, folder.get_id());
//        cv.put(FILE_NAME , folder.getFilename());
//        cv.put(FOLDER_NAME ,folder.getFolderame());
//
//
//
//        db.insert(COMPANY_FOLDER_TABLE, null, cv);
//
//
//        // }
//        long id = db.insert(COMPANY_FOLDER_TABLE, null, cv);
//        if (id != -1) {
//            System.out.println("Inserted Folder details     ......");
//        } else {
//            System.out.println(" not Inserted Folder details ......");
//
//        }
//        db.close();
//
//    }


    //code to get the register
    public String getregister(String username) {
        String password = "";
        SQLiteDatabase db = this.getReadableDatabase();
        //String selectquery="SELECT * FROM TABLE_REGISTER";
        Cursor cursor = db.query(TABLE_REGISTER, null, "email=?", new String[]{username}, null, null, null, null);

        if (cursor.getCount() < 1) {
            cursor.close();
            return "Not Exist";
        } else if (cursor.getCount() >= 1 && cursor.moveToFirst()) {

            password = cursor.getString(cursor.getColumnIndex(PASSWORD));
            cursor.close();
            System.out.println("Login successfully");
        }
        return password;
    }


}