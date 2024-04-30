package com.intuition.ivepos;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rohithkumar on 4/13/2018.
 */

public class Upload_Task extends JobService {

    JobParameters params;
    DoItTask doIt;

    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;
    public SQLiteDatabase db_inapp = null;

    AmazonS3 s3client;
    TransferUtility transferUtility, transferUtility1, transferUtility2;
    Date tempdate, tempdate1;

    int i_up = 0 , level_up = 0;

    @Override
    public boolean onStartJob(JobParameters params) {

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);

        credentialsProvider();

        // callback method to call the setTransferUtility method
        setTransferUtility();

        this.params = params;
        Log.d("TestService", "Work to be called from here");
        doIt = new DoItTask();
        doIt.execute();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("TestService", "System calling to stop the job here");
        if (doIt != null)
            doIt.cancel(true);
        return false;
    }

    private class DoItTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("DoItTask", "Clean up the task here and call jobFinished...");
//            jobFinished(params, false);

            SimpleDateFormat normal2 = new SimpleDateFormat("dd MMM yy");
            final String normal1 = normal2.format(new Date());

            Date dt = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aa");
            final String time1 = sdf1.format(dt);

            if (isDeviceOnline() == true) {
                Cursor cursor = db.rawQuery("SELECT * FROM Sync_time", null);
                if (cursor.moveToFirst()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("last_time", normal1+", "+time1);
                    String where = "_id = '1' ";

                    db.update("Sync_time", contentValues, where, new String[]{});
                }
            }

            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d("DoItTask", "Working here...");

            String textcompanyname = "", storeitem = "", deviceitem = "", compemailid = "";
            db_inapp = openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);
            Cursor cursor_cred = db_inapp.rawQuery("SELECT * FROM credentialstime", null);
            if (cursor_cred.moveToFirst()){
                textcompanyname = cursor_cred.getString(6);
                storeitem = cursor_cred.getString(7);
                deviceitem = cursor_cred.getString(8);
                compemailid = cursor_cred.getString(5);
            }

            final String finalTextcompanyname = textcompanyname;
            final String finalStoreitem = storeitem;
            final String finalDeviceitem = deviceitem;
            final String finalCompemailid = compemailid;

            System.out.println("home page company"+finalTextcompanyname);
            System.out.println("home page store"+finalStoreitem);
            System.out.println("home page device"+finalDeviceitem);
            System.out.println("home page emailid"+finalCompemailid);

            if (isDeviceOnline() == true) {
                String DATABASE_NAME21 = "mydb_Salesdata";


                File file2 = new File(Environment.getDataDirectory() +
                        "/data/" + "com.intuition.ivepos" +
                        "/databases/" + DATABASE_NAME21);
                Date lastModDate = new Date(file2.lastModified());

                System.out.println("checccc " + lastModDate);

                String bucketName1 = "intuitionclients";

                String company = finalTextcompanyname + "-company";

                String store = company + "/" + finalStoreitem + "-store--" + finalCompemailid;

                String device = store + "/" + "dev-" + finalDeviceitem;

                String DATABASE_NAME1 = "mydb_Appdata";

                String DATABASE_NAME2 = "mydb_Salesdata";

                File DATA_DIRECTORY_DATABASE1 =
                        new File(Environment.getDataDirectory() +
                                "/data/" + "com.intuition.ivepos" +
                                "/databases/" + DATABASE_NAME1);

                File DATA_DIRECTORY_DATABASE2 =
                        new File(Environment.getDataDirectory() +
                                "/data/" + "com.intuition.ivepos" +
                                "/databases/" + DATABASE_NAME2);

                File dbFile = DATA_DIRECTORY_DATABASE1;

                File dbFile2 = DATA_DIRECTORY_DATABASE2;


                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);


                System.out.println("home page company i is "+i_up);
                System.out.println("home page company level is "+level_up);

                Cursor count = db1.rawQuery("SELECT COUNT(total) FROM All_Sales", null);
                if (count.moveToFirst()) {
                    level_up = count.getInt(0);
                }
                count.close();

                if (i_up == level_up){

                }else {
//                    if (String.valueOf(lastModDate).equals(String.valueOf(d))) {
//
//                    } else {

           /*         String filename = "mydb_Salesdata";

                    File exportDir = new File(Environment.getExternalStorageDirectory(), "/upload_backup/");
                    if (!exportDir.exists()) {
                        exportDir.mkdirs();
                    }


                    //File exportDir = DATABASE_DIRECTORY;
                    File file = new File(exportDir, filename);

                    if (!exportDir.exists()) {
                        exportDir.mkdirs();
                    }

                    try {
                        copyFile(dbFile2, file);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String filename1 = "mydb_Appdata";


                    //File exportDir = DATABASE_DIRECTORY;
                    File file1 = new File(exportDir, filename1);

                    if (!exportDir.exists()) {
                        exportDir.mkdirs();
                    }

                    try {
                        copyFile(dbFile, file1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    dbFile2=new File(Environment.getExternalStorageDirectory()+"/upload_backup/"+filename);
                    dbFile=new File(Environment.getExternalStorageDirectory()+"/upload_backup/"+filename1);





                  */



                    TransferObserver transferObserver = transferUtility.upload(
                            "intuitionclients" + "/" + device,     /* The bucket to download from */
                            "mydb_Salesdata",    /* The key for the object to download */
                            dbFile2       /* The file to download the object to */
                    );
                    transferObserverListener(transferObserver);

                    TransferObserver transferObserver1 = transferUtility.upload(
                            "intuitionclients" + "/" + device,     /* The bucket to download from */
                            "mydb_Appdata",    /* The key for the object to download */
                            dbFile        /* The file to download the object to */
                    );
                    transferObserverListener(transferObserver1);
//                    }

                    System.out.println("home page company i is 1 "+i_up);
                    System.out.println("home page company level is 1 "+level_up);

                    i_up = level_up;
                }
            }

            return null;
        }
    }
    private static void copyFile(File src, File dst) throws IOException {
        Log.e("1", "111hi1");
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        Log.e("1", "111hi2");
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
            Log.e("1", "111hi3");
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
            Log.e("1", "111hi4");
        }
    }
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void credentialsProvider() {

//        // Initialize the Amazon Cognito credentials provider
//        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
//                getApplicationContext(),
//                "ap-south-1:c2b96ff9-4cfc-49cc-867c-c630022abba2", // Identity pool ID
//                Regions.AP_SOUTH_1 // Region
//        );
        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider1 = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-south-1:2a2043d1-95c8-4913-a38e-6cbbd01c4f0b", // Identity pool ID
                Regions.AP_SOUTH_1 // Region
        );


        setAmazonS3Client(credentialsProvider1);
    }

    public void setAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider) {

        // Create an S3 client
        s3client = new AmazonS3Client(credentialsProvider);

        // Set the region of your S3 bucket
        s3client.setRegion(Region.getRegion(Regions.AP_SOUTH_1));

    }

    public void setTransferUtility() {

        transferUtility = new TransferUtility(s3client, getApplicationContext());
    }


    public void transferObserverListener(TransferObserver transferObserver) {

        transferObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e("statechange", state + "");
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
//                int percentage = (int) (bytesCurrent/bytesTotal * 100);
//                Log.e("percentage",percentage +"");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error", "error");
            }

        });
    }


}
