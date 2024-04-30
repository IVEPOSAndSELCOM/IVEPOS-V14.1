package com.intuition.ivepos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmReceiver2 extends BroadcastReceiver {


    SimpleDateFormat sdff2, sdff1;
    String currentDateandTimee1;
    String timee1;

    public SQLiteDatabase db1 = null;

    public static final String PACKAGE_NAME = "com.intuition.ivepos";
    public static final String DATABASE_NAME = "mydb_Appdata";
    public static final String DATABASE_NAME1 = "mydb_Salesdata";
    public static final String DATABASE_TABLE = "entryTable";

    /** Contains: /data/data/com.example.app/databases/example.db **/
    private static final File DATA_DIRECTORY_DATABASE =
            new File(Environment.getDataDirectory() +
                    "/data/" + PACKAGE_NAME +
                    "/databases/" + DATABASE_NAME1 );



    @Override
    public void onReceive(Context context, Intent intent) {


        sdff2 = new SimpleDateFormat("ddMMMyy");
        currentDateandTimee1 = sdff2.format(new Date());

        Date dt = new Date();
        sdff1 = new SimpleDateFormat("hhmmssaa");
        timee1 = sdff1.format(dt);

        // For our recurring task, we'll just display a message
        //Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();

//        SQLiteDatabase.deleteDatabase(DATA_DIRECTORY_DATABASE);

//        context.execSQL("delete from All_Sales");

        db1 = context.openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        crearYasignar1(db1, context);

//        if (!SdIsPresent()) ;
//
//        SQLiteDatabase.deleteDatabase(DATA_DIRECTORY_DATABASE);
//        File dbFile1 = DATA_DIRECTORY_DATABASE;
//        String filename1 = "mydb_Appdata";
//
//        File exportDir1 = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
//        if (!exportDir1.exists()) {
//            exportDir1.mkdirs();
//        }
//
//
//        //File exportDir = DATABASE_DIRECTORY;
//        File file1 = new File(exportDir1, filename1);
//
//        if (!exportDir1.exists()) {
//            exportDir1.mkdirs();
//        }
//
//        try {
////                    file.createNewFile();
//            copyFile(dbFile1, file1);
//            Log.e("1", "11");
//
//
//            //return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e("2", "22");
//
//            //return false;
//        }
//
//        File dbFile = DATA_DIRECTORY_DATABASE;
//        String filename = "mydb_Salesdata";
//
//        File exportDir = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
//        if (!exportDir.exists()) {
//            exportDir.mkdirs();
//        }
//
//
//        //File exportDir = DATABASE_DIRECTORY;
//        File file = new File(exportDir, filename);
//
//        if (!exportDir.exists()) {
//            exportDir.mkdirs();
//        }
//
//        try {
////                    file.createNewFile();
//            copyFile(dbFile, file);
//            Log.e("1", "11");
//
//
//            //return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e("2", "22");
//
//            //return false;
//        }

    }


    public static boolean SdIsPresent() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    private static void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }


    public void crearYasignar1(SQLiteDatabase dbllega, Context context){
        try {
            dbllega.execSQL("delete from All_Sales");
            dbllega.execSQL("delete from Itemwiseorderlistitems");
            dbllega.execSQL("delete from Itemwiseorderlistmodifiers");
            dbllega.execSQL("delete from Userwiseorderlistitems");
            dbllega.execSQL("delete from Generalorderlistascdesc");
            dbllega.execSQL("delete from Generalorderlistascdesc1");
            dbllega.execSQL("delete from userdata");
            dbllega.execSQL("delete from itemdata");
            dbllega.execSQL("delete from All_Sales_Cancelled");
            dbllega.execSQL("delete from Cancelwiseorderlistitems");
            dbllega.execSQL("delete from usercancelleddata");
            dbllega.execSQL("delete from Customerdetails");
            dbllega.execSQL("delete from Tablepayment");
            dbllega.execSQL("delete from Billnumber");
            dbllega.execSQL("delete from Discountdetails");
            dbllega.execSQL("delete from Cardnumber");
            dbllega.execSQL("delete from Splitdata");
            for (int i=1;i<=100;i++ ){
                dbllega.execSQL("delete from Table" + i + " ");
            }


//            dbllega.execSQL("CREATE TABLE if not exists All_Sales (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, quantity text, price text, total text," +
//                    "type text, parent text, parentid text, mod_assigned text, tax text, taxname text, bill_no text, time text, date text, user text, table_id text, billtype text," +
//                    "paymentmethod text, billamount_disapply text, billamount_disnotapply text, _idd text, deleted_not text, modifiedquantity text, quantitycopy text, " +
//                    "modifiedtotal text, date1 text, datetimee text, disc_type text, disc_value text, newtotal text, disc_thereornot text," +
//                    " disc_indiv_total text, new_modified_total text);");
//
//            dbllega.execSQL("CREATE TABLE if not exists Itemwiseorderlistitems (_id integer PRIMARY KEY UNIQUE, itemno text, itemname text, sales integer, salespercentage integer," +
//                    "itemtotalquan text);");
//            dbllega.execSQL("CREATE TABLE if not exists Itemwiseorderlistmodifiers (_id integer PRIMARY KEY UNIQUE, modno text, modname text, sales integer, salespercentage integer," +
//                    "modtotalquan text);");
//            dbllega.execSQL("CREATE TABLE if not exists Userwiseorderlistitems (_id integer PRIMARY KEY UNIQUE, username text, sales integer, salespercentage integer);");
//            dbllega.execSQL("CREATE TABLE if not exists Generalorderlistascdesc (_id integer PRIMARY KEY UNIQUE, date text, time text, user text, billno text, " +
//                    "billdetails text, sales integer, discountamount text, paymentmethod text, billtype text, itemname text, quan text);");
//            dbllega.execSQL("CREATE TABLE if not exists Generalorderlistascdesc1 (_id integer PRIMARY KEY UNIQUE, date text, time text, user text, billno text, " +
//                    "billdetails text, sales integer, discountamount text, paymentmethod text, billtype text, itemname text, quan text, tableid text, individualprice text" +
//                    ", date1 text, datetimee text);");
//            dbllega.execSQL("CREATE TABLE if not exists userdata (_id integer PRIMARY KEY UNIQUE, username text, total integer);");
//            dbllega.execSQL("CREATE TABLE if not exists itemdata (_id integer PRIMARY KEY UNIQUE, itemname text, total integer);");
//
//            dbllega.execSQL("CREATE TABLE if not exists All_Sales_Cancelled (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, quantity text, price text, total text," +
//                    "type text, parent text, parentid text, mod_assigned text, tax text, taxname text, bill_no text, time text, date text, user text, billtype text," +
//                    " paymentmethod text, billamount_disapply text, billamount_disnotapply text, _idd text, reason text, " +
//                    "billamount_cancelled text, date1 text, billamount_cancelled_user text, disc_type text, disc_value text, newtotal text, disc_thereornot text," +
//                    " disc_indiv_total text);");
//
//            dbllega.execSQL("CREATE TABLE if not exists Cancelwiseorderlistitems (_id integer PRIMARY KEY UNIQUE, date text, time text, user text, billno text, sale text, " +
//                    "refund text, reason text );");
//
//            dbllega.execSQL("CREATE TABLE if not exists usercancelleddata (_id integer PRIMARY KEY UNIQUE, username text, total integer);");
//            dbllega.execSQL("CREATE TABLE if not exists Customerdetails (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, name text, phoneno text, emailid text, address text, " +
//                    "rupees text, billnumber text);");
//            dbllega.execSQL("CREATE TABLE if not exists Tablepayment (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, tablename text, tableid text, price text, print text);");
//            dbllega.execSQL("CREATE TABLE if not exists Billnumber (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, billnumber text, total text, user text, date text," +
//                    " paymentmethod text, billtype text, subtotal text, taxtotal text, roundoff text, globaltaxtotal text);");
//            dbllega.execSQL("CREATE TABLE if not exists Discountdetails (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, date text, time text, billno text, Discountcodeno text, " +
//                    "Discount_percent text, Billamount_rupess text, Discount_rupees text, date1 text, original_amount text);");
//            dbllega.execSQL("CREATE TABLE if not exists Cardnumber (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, cardnumber text);");
//            dbllega.execSQL("CREATE TABLE if not exists Splitdata (_id integer PRIMARY KEY UNIQUE, billnum text, total text, splittype text, split1 text, split2 text, split3 text);");
//
//            for (int i=1;i<=100;i++ ){
//                dbllega.execSQL("CREATE TABLE if not exists Table" + i + " (_id integer PRIMARY KEY AUTOINCREMENT,quantity text, itemname text, price text, total text, type text," +
//                        " parent text, parentid text, modassigned text, tax text, taxname text, disc_type text, disc_value text, newtotal text, disc_thereornot text," +
//                        " disc_indiv_total text);");
//            }
//
//
//            Cursor co2 = dbllega.rawQuery("SELECT * FROM Cardnumber", null);
//            int cou2 = co2.getColumnCount();
//            //Toast.makeText(AlarmReceiver2.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
//            if (String.valueOf(cou2).equals("2")){
//                dbllega.execSQL("ALTER TABLE Cardnumber ADD COLUMN billnumber text DEFAULT 0");
//                //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
//            }
//
//            Cursor co = dbllega.rawQuery("SELECT * FROM Generalorderlistascdesc1", null);
//            int cou = co.getColumnCount();
//            //Toast.makeText(AlarmReceiver2.this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
//            if (String.valueOf(cou).equals("16")){
//                dbllega.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN cardnumber text DEFAULT 0");
//            }
//
//            Cursor co5 = dbllega.rawQuery("SELECT * FROM All_Sales", null);
//            int cou5 = co5.getColumnCount();
//            if (String.valueOf(cou5).equals("27")){
//                dbllega.execSQL("ALTER TABLE All_Sales ADD COLUMN disc_type text DEFAULT 0");
//                dbllega.execSQL("ALTER TABLE All_Sales ADD COLUMN disc_value text DEFAULT 0");
//                dbllega.execSQL("ALTER TABLE All_Sales ADD COLUMN newtotal text DEFAULT 0");
//                dbllega.execSQL("ALTER TABLE All_Sales ADD COLUMN disc_thereornot text DEFAULT 0");
//                dbllega.execSQL("ALTER TABLE All_Sales ADD COLUMN disc_indiv_total text DEFAULT 0");
//                dbllega.execSQL("ALTER TABLE All_Sales ADD COLUMN new_modified_total text DEFAULT 0");
//            }
//
//            Cursor co6 = dbllega.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
//            int cou6 = co6.getColumnCount();
//            if (String.valueOf(cou6).equals("24")){
//                dbllega.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_type text DEFAULT 0");
//                dbllega.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_value text DEFAULT 0");
//                dbllega.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN newtotal text DEFAULT 0");
//                dbllega.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_thereornot text DEFAULT 0");
//                dbllega.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_indiv_total text DEFAULT 0");
//            }
//
//            Cursor co7 = dbllega.rawQuery("SELECT * FROM Table1", null);
//            int cou7 = co7.getColumnCount();
//            if (String.valueOf(cou7).equals("11")){
//                for (int i=1;i<=100;i++ ){
//                    dbllega.execSQL("ALTER TABLE Table" + i + " ADD COLUMN disc_type text DEFAULT 0");
//                    dbllega.execSQL("ALTER TABLE Table" + i + " ADD COLUMN disc_value text DEFAULT 0");
//                    dbllega.execSQL("ALTER TABLE Table" + i + " ADD COLUMN newtotal text DEFAULT 0");
//                    dbllega.execSQL("ALTER TABLE Table" + i + " ADD COLUMN disc_thereornot text DEFAULT 0");
//                    dbllega.execSQL("ALTER TABLE Table" + i + " ADD COLUMN disc_indiv_total text DEFAULT 0");
//                }
//            }
//
//            Cursor co8 = dbllega.rawQuery("SELECT * FROM Billnumber", null);
//            int cou8 = co8.getColumnCount();
//            if (String.valueOf(cou8).equals("11")){
//                dbllega.execSQL("ALTER TABLE Billnumber ADD COLUMN billcount text");
//            }
//
//
//            dbllega = context.openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
//
//            Cursor co9 = dbllega.rawQuery("SELECT * FROM Generalorderlistascdesc1", null);
//            int cou9 = co9.getColumnCount();
//            //Toast.makeText(AlarmReceiver2.this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
//            if (String.valueOf(cou9).equals("17")){
//                dbllega.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN individualtotal text");
//                dbllega.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN billcount text");
//            }
//
//            Cursor co10 = dbllega.rawQuery("SELECT * FROM Discountdetails", null);
//            int cou10 = co10.getColumnCount();
//            //Toast.makeText(AlarmReceiver2.this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
//            if (String.valueOf(cou10).equals("10")){
//                dbllega.execSQL("ALTER TABLE Discountdetails ADD COLUMN billcount text");
//            }
//
//            Cursor co11 = dbllega.rawQuery("SELECT * FROM Cancelwiseorderlistitems", null);
//            int cou11 = co11.getColumnCount();
//            //Toast.makeText(AlarmReceiver2.this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
//            if (String.valueOf(cou11).equals("8")){
//                dbllega.execSQL("ALTER TABLE Cancelwiseorderlistitems ADD COLUMN billcount text");
//            }

        }catch (SQLiteException e){
//            alertas("Error desconocido: "+e.getMessage());
        }
    }

//    public void alertas(String alerta) {
//        ContextThemeWrapper wrapper = new ContextThemeWrapper(AlarmReceiver2.this, R.style.AppTheme);
//        AlertDialog.Builder builder = new AlertDialog.Builder(wrapper);
//        builder.setIcon(R.drawable.icon);
//        builder.setTitle(R.string.app_name);
//        builder.setMessage(alerta);
//        builder.create().show();
//    }


}