package com.intuition.ivepos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.File;
import java.util.Date;

public class Uploa_Task1 extends BroadcastReceiver {

    public SQLiteDatabase db = null;
    public SQLiteDatabase db_inapp = null;

    AmazonS3 s3client;
    TransferUtility transferUtility, transferUtility1, transferUtility2;
    Date tempdate, tempdate1;
    long temp = 0, temp1 = 0;
    Context context1;

    @Override
    public void onReceive(Context context, Intent intent) {

        context1 = context;

        credentialsProvider();

        // callback method to call the setTransferUtility method
        setTransferUtility();

        db = context.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        db_inapp = context.openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);

        Log.d("DoItTask", "Working here...");

        String textcompanyname = "", storeitem = "", deviceitem = "", compemailid = "";

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

            final String device = store + "/" + "dev-" + finalDeviceitem;

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

            final File dbFile = DATA_DIRECTORY_DATABASE1;

            final File dbFile2 = DATA_DIRECTORY_DATABASE2;


            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            ObjectListing listing1 = s3client.listObjects(new ListObjectsRequest().withBucketName(bucketName1).withPrefix(device + "/mydb_Salesdata"));
            Date d = null;
            for (S3ObjectSummary objectSummary : listing1.getObjectSummaries()) {
                String hi = objectSummary.getKey();
                long siz = objectSummary.getSize();
                d = objectSummary.getLastModified();
                temp = siz;
//                            Toast.makeText(Uploa_Task1.this, "last modified " + d, Toast.LENGTH_LONG).show();

                System.out.println("checccc1 " + d);
            }


            if (String.valueOf(lastModDate).equals(String.valueOf(d))) {

            } else {

                Runnable runnable=new Runnable() {
                    @Override
                    public void run() {
                        Log.e("transferUtility",transferUtility.toString());
                        Log.e("dbFile2",dbFile2.toString());
                        Log.e("path","intuitionclients" + "/" + device);
                        TransferObserver transferObserver = transferUtility.upload(
                                "intuitionclients" + "/" + device,     /* The bucket to download from */
                                "mydb_Salesdata",    /* The key for the object to download */
                                dbFile2       /* The file to download the object to */
                        );
                        Log.e("transferObserver",transferObserver.toString());
                        transferObserverListener(transferObserver);


                        TransferObserver transferObserver1 = transferUtility.upload(
                                "intuitionclients" + "/" + device,     /* The bucket to download from */
                                "mydb_Appdata",    /* The key for the object to download */
                                dbFile        /* The file to download the object to */
                        );
                        transferObserverListener(transferObserver1);
                    }
                };
                Thread thread=new Thread(runnable);
                thread.start();
                Log.e("path","intuitionclients" + "/" + device);


                System.out.println("checccc111111");

//                SimpleDateFormat normal2 = new SimpleDateFormat("dd MMM yy");
//                final String normal1 = normal2.format(new Date());
//
//                Date dt = new Date();
//                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aa");
//                final String time1 = sdf1.format(dt);
//
//                if (isDeviceOnline() == true) {
//                    Cursor cursor = db.rawQuery("SELECT * FROM Sync_time", null);
//                    if (cursor.moveToFirst()) {
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("last_time", normal1+", "+time1);
//                        String where = "_id = '1' ";
//
//                        db.update("Sync_time", contentValues, where, new String[]{});
//                    }
//                }
            }
        }

    }


    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) context1.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void credentialsProvider() {

//        // Initialize the Amazon Cognito credentials provider
//        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
//                context1,
//                "ap-south-1:c2b96ff9-4cfc-49cc-867c-c630022abba2", // Identity pool ID
//                Regions.AP_SOUTH_1 // Region
//        );
        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider1 = new CognitoCachingCredentialsProvider(
                context1,
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

        transferUtility = new TransferUtility(s3client, context1);
    }


    public void transferObserverListener(TransferObserver transferObserver) {
        Log.e("Observer in listener", transferObserver.toString());

        transferObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e("statechange", state + "");
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                Log.e("statechange1", "");
//                int percentage = (int) (bytesCurrent/bytesTotal * 100);
//                Log.e("percentage",percentage +"");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("statechange2", "");
                Log.e("error", "error");
            }

        });
    }

}