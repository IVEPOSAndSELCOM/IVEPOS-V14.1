package com.intuition.ivepos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmReceiver1 extends BroadcastReceiver {


    SimpleDateFormat sdff2, sdff1;
    String currentDateandTimee1;
    String timee1;



    public static final String PACKAGE_NAME = "com.intuition.ivepos";
    public static final String DATABASE_NAME = "mydb_Appdata";
    public static final String DATABASE_NAME1 = "mydb_Salesdata";
    public static final String DATABASE_TABLE = "entryTable";

    /** Contains: /data/data/com.example.app/databases/example.db **/
    private static final File DATA_DIRECTORY_DATABASE =
            new File(Environment.getDataDirectory() +
                    "/data/" + PACKAGE_NAME +
                    "/databases/" + DATABASE_NAME );



    @Override
    public void onReceive(Context context, Intent intent) {


        sdff2 = new SimpleDateFormat("ddMMMyy");
        currentDateandTimee1 = sdff2.format(new Date());

        Date dt = new Date();
        sdff1 = new SimpleDateFormat("hhmmssaa");
        timee1 = sdff1.format(dt);

        // For our recurring task, we'll just display a message
//        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();

        Log.d("hiiiiii11", "error");

        if (!SdIsPresent()) ;

        File dbFile1 = DATA_DIRECTORY_DATABASE;
        String filename1 = "mydb_Appdata";

//        File exportDir1 = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
//        if (!exportDir1.exists()) {
//            exportDir1.mkdirs();
//        }
        File exportDir1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
        if (!exportDir1.exists()) {
            exportDir1.mkdirs();
        }


        //File exportDir = DATABASE_DIRECTORY;
        File file1 = new File(exportDir1, filename1);

        if (!exportDir1.exists()) {
            exportDir1.mkdirs();
        }

        try {
//                    file.createNewFile();
            copyFile(dbFile1, file1);
            Log.e("1", "11");


            //return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("2", "22");

            //return false;
        }

        File dbFile = DATA_DIRECTORY_DATABASE;
        String filename = "mydb_Salesdata";

//        File exportDir = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
//        if (!exportDir.exists()) {
//            exportDir.mkdirs();
//        }
        File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }


        //File exportDir = DATABASE_DIRECTORY;
        File file = new File(exportDir, filename);

        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        try {
//                    file.createNewFile();
            copyFile(dbFile, file);
            Log.e("1", "11");


            //return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("2", "22");

            //return false;
        }

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

}