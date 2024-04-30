package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 2/6/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class LoginDataBaseAdapter
{
    static final String DATABASE_NAME = "mydb";
    static final int DATABASE_VERSION = 1;

    private static final String LOGTAG = "IVEPOS";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ITEMNAME = "itemname";
    public static final String COLUMN_ITEMCATEGORY = "itemcategory";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_ITEMTAX = "itemtax";
    public static final String COLUMN_STOCKQUANTITY = "stockquan";

    public static final String COLUMN_INVENTORY = "INVENTORY";
    public static final String COLUMN_CATEGORY = "ALL_CATEGORY";
    public static final String CATEGORY_TBL = "ALL_CATEGORY";

    private static final String TAG = "DatabaseCategory";

    private static LoginDataBaseAdapter instance;



    private static final String[] allcolumns = {

            LoginDataBaseAdapter.COLUMN_ID, LoginDataBaseAdapter.CATEGORY_TBL
             };

    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.

    static final String TABLE_ITEMLIST = "create table "+"ITEMSLIST"+
            "( " +COLUMN_ID+ " integer primary key autoincrement," +
            COLUMN_ITEMNAME +" text," +
            COLUMN_ITEMCATEGORY +" text, "+
            COLUMN_PRICE + " numeric," +
            COLUMN_ITEMTAX + " numeric" +
            COLUMN_STOCKQUANTITY + " numeric" +")";



    static final String INSERT_QUERY = "INSERT INTO ITEMLIST " +
                    "( "+COLUMN_ITEMNAME+ ", "+COLUMN_PRICE+ ", "+COLUMN_INVENTORY+" )" +
                    "VALUES " +
                    "('Coke', '300', '55')";




    // Variable to hold the database instance
    public  SQLiteDatabase db;
    // Context of the application using the database.
    private Context context;
    // Database open/upgrade helper
    private DataBaseHelper dbHelper;
    private LoginDataBaseAdapter writableDatabase;

    public  LoginDataBaseAdapter(Context _context)
    {
        this.context = _context;
        dbHelper = new DataBaseHelper(context);
    }

    public static synchronized LoginDataBaseAdapter getHelper(Context context) {
        if (instance == null)
            instance = new LoginDataBaseAdapter(context);
        return instance;
    }




    public  LoginDataBaseAdapter open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        return this;
    }
    public  LoginDataBaseAdapter opentoread() throws SQLException
    {
        db = dbHelper.getReadableDatabase();
        return this;
    }
    public void close()
    {
        db.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }

    public void insertEntry(String userName,String password)
    {
        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put("USERNAME", userName);
        newValues.put("PASSWORD",password);

        // Insert the row into your table
        db.insert("LOGIN", null, newValues);
        ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
    }
    public int deleteEntry(String UserName)
    {
        //String id=String.valueOf(ID);
        String where="USERNAME=?";
        int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{UserName}) ;
        // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }
    public String getSinlgeEntry(String userName)
    {
        Cursor cursor=db.query("LOGIN", null, " USERNAME=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("PASSWORD"));
        cursor.close();
        return password;
    }

    public String getSinlgeEntrypass(String password)
    {
        Cursor cursor=db.query("LOGIN", null, " PASSWORD=?", new String[]{password}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        return password;
    }

    public String getSinlgeEntrylad(String userName)
    {
        Cursor cursor=db.query("LAdmin", null, " username=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("password"));
        cursor.close();
        return password;
    }

    public String getSinlgeEntryladpass(String password)
    {
        Cursor cursor=db.query("LAdmin", null, " password=?", new String[]{password}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        return password;
    }

    public String getSinlgeEntryuserone(String userName)
    {
        Cursor cursor=db.query("User1", null, " username=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("password"));
        cursor.close();
        return password;
    }

    public String getSinlgeEntryusertwo(String userName)
    {
        Cursor cursor=db.query("User2", null, " username=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("password"));
        cursor.close();
        return password;
    }

    public String getSinlgeEntryuserthree(String userName)
    {
        Cursor cursor=db.query("User3", null, " username=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("password"));
        cursor.close();
        return password;
    }

    public String getSinlgeEntryuserfour(String userName)
    {
        Cursor cursor=db.query("User4", null, " username=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("password"));
        cursor.close();
        return password;
    }

    public String getSinlgeEntryuserfive(String userName)
    {
        Cursor cursor=db.query("User5", null, " username=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("password"));
        cursor.close();
        return password;
    }

    public String getSinlgeEntryusersix(String userName)
    {
        Cursor cursor=db.query("User6", null, " username=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("password"));
        cursor.close();
        return password;
    }

    public String getSinlgeEntryUniversal(String userName){
        Cursor cursor=db.query("Universalcredentials", null, " username=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("password"));
        cursor.close();
        return password;
    }

    public String getSinlgeEntryMaster(String userName){
        Cursor cursor=db.query("Login", null, " USERNAME=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("PASSWORD"));
        cursor.close();
        return password;
    }

    public String getSinlgeEntryLocal(String userName){
        Cursor cursor=db.query("LAdmin", null, " username=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("password"));
        cursor.close();
        return password;
    }


    public void  updateEntry(String userName,String password)
    {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put("USERNAME", userName);
        updatedValues.put("PASSWORD", password);

        String where="USERNAME = ?";
        db.update("LOGIN", updatedValues, where, new String[]{userName});
      }

    public List<Hotel> findAll(){
        List<Hotel> itemlists = new ArrayList<Hotel>();

        Cursor cursor = db.query(LoginDataBaseAdapter.CATEGORY_TBL, allcolumns, null, null, null, null, null);

        Log.i(LOGTAG, "Returned" + cursor.getCount() + " rows");

        if(cursor.getCount() > 0){
            while (cursor.moveToNext()){
                Hotel itemlist = new Hotel();

                itemlist.setName(cursor.getString(cursor.getColumnIndex(LoginDataBaseAdapter.COLUMN_CATEGORY)));
                itemlist.setId(cursor.getInt(cursor.getColumnIndex(LoginDataBaseAdapter.COLUMN_ID)));

                itemlists.add(itemlist);
            }
        }
        return itemlists;
    }


    public void textinsert(String name)
    {
        ContentValues cv=new ContentValues();
        cv.put("name", name);
        db.insert("Hotel", null, cv);
    }

    public void textinsertitems(String itemname, Integer price, Integer stockquan, String category, String itemtax)
    {
        ContentValues cv=new ContentValues();
        cv.put("itemname", itemname);
        cv.put("price", price);
        cv.put("stockquan", stockquan);
        cv.put("category", category);
        cv.put("itemtax", itemtax);
        db.insert("Items", null, cv);
        db.insert("Items_Virtual", null, cv);
    }

    public void delete_byID(int _id){
        db.delete("Hotel", "_id" + "=" + _id, null);
    }

    public void updateData(long _id, String name) {
        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put("name", name);
        db.update("Hotel", cvUpdate, "_id" + " = " + _id, null);

    }



    public Cursor queueAll(){
        String[] columns = new String[]{COLUMN_ID, COLUMN_CATEGORY};
        Cursor cursor = db.query(CATEGORY_TBL, columns,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor fetchAllCountries() {

        Cursor mCursor = db.query("Hotel", new String[] {"_id" , "nombre"},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public List<Hotel> getAllLabels(){

        List<Hotel> labels = new ArrayList<Hotel>();

// Select All Query
        String selectQuery = "SELECT * FROM " + CATEGORY_TBL;

        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Hotel label = new Hotel();
                label.setId(cursor.getInt(0));
                label.setName(cursor.getString(1));

                String name = cursor.getString(0)+"\n"+cursor.getString(1)+"\n"+cursor.getString(2)+"\n"+cursor.getString(3);
                DatabasecategoryActivity.ArrayofName.add(name);

                labels.add(label);
            } while (cursor.moveToNext());
        }

// closing connection
        cursor.close();
        db.close();

// returning lables
        return labels;

    }

   public Cursor fetchCountriesByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = db.query("Hotel", new String[] {"_id",
                            "nombre"},
                    null, null, null, null, null);

        }
        else {
            mCursor = db.query(true, "Hotel", new String[] {"_id",
                            "nombre"},
                    "nombre" + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

   }

   public int del(String contactID){
        int cnt = db.delete("Hotel", "_id="+contactID, null);
        return cnt;
   }

    /** Returns all the contacts in the table */
   public Cursor getAllContacts(){
        return db.query("Hotel", new String[] { "_id",  "name" } , null, null, null, null, "name" + " asc ");
   }

    /** Returns a contact by passing its id */
   public Cursor getContactByID(String contactID){
        return db.query("Hotel", new String[] { "_id",  "name" } , "_ID="+contactID, null, null, null, "name" + " asc ");
   }
    /** Updates a contact */
   public int update(ContentValues contentValues,String contactID){
        int cnt = db.update("Hotel", contentValues, "_id=" + contactID, null);
        return cnt;
   }
    /** Inserts a new contact to the table contacts */
    public long insert(ContentValues contentValues){
        long rowID = db.insert("Hotel", null, contentValues);
        return rowID;
    }

    public String getType(Cursor c) {
        return(c.getString(3));
    }


    public String getSinlgeEntrypasss(String password)
    {
        Cursor cursor=db.query("LOGIN", null, " PASSWORD=?", new String[]{password}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        return password;
    }

    public String getSinlgeEntryladpasss(String password)
    {
        Cursor cursor=db.query("LAdmin", null, " password=?", new String[]{password}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        return password;
    }

    public Cursor readEntry() {

        String[] allColumns = new String[] { "itemname", "price"};

        Cursor c = db.query("Items", allColumns, null, null, null,
                null, null);

        if (c != null) {
            c.moveToFirst();
        }
        return c;

    }

}