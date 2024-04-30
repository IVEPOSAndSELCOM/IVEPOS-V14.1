package com.intuition.ivepos.syncapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import static android.content.Context.ACCOUNT_SERVICE;

/**
 * Created by HP on 8/14/2018.
 */

public class SyncHelperApp {


    // Constants
    // Content provider authority
    String TAG="SyncHelper";

    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 1L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
                    SECONDS_PER_MINUTE;
    // Global variables
    // A content resolver for accessing the provider
    ContentResolver mResolver;

    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.intuition.ivepos.syncapp.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "intuitionsdine.com";
    // The account name
    public static final String ACCOUNT = "dummy_account_dine";
    public static final String SCHEME = "content://";
    // Instance fields
    public static  Account mAccount;
    Uri mUri;
    SQLiteDatabase syncdbapp=null;


    public void beginPeriodicSync(final long updateConfigInterval,Context ctx,Bundle extras) {
        Log.d(TAG, "beginPeriodicSync() called with: updateConfigInterval = [" +
                updateConfigInterval + "]");
        mAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);

        final AccountManager accountManager = (AccountManager) ctx
                .getSystemService(ACCOUNT_SERVICE);

        if (!accountManager.addAccountExplicitly(mAccount, null, null)) {
            mAccount = accountManager.getAccountsByType(ACCOUNT_TYPE)[0];
        }

        setAccountSyncable();

        if(extras!=null){
            try {
                ContentResolver.addPeriodicSync(mAccount, AUTHORITY,
                      extras, updateConfigInterval);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try {
                ContentResolver.addPeriodicSync(mAccount, AUTHORITY,
                        Bundle.EMPTY, updateConfigInterval);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
    }

    private void setAccountSyncable() {
        if (ContentResolver.getIsSyncable(mAccount, AUTHORITY) == 0) {
            ContentResolver.setIsSyncable(mAccount, ACCOUNT, 1);
        }
    }

    public void tablesync(Context ctx){
        // Get the content resolver object for your app
        mResolver = ctx.getContentResolver();
        // Construct a URI that points to the content provider data table

        /*
         * Create a content observer object.
         * Its code does not mutate the provider, so set
         * selfChange to "false"
         */
      TableObserver observer =new TableObserver(new Handler());
        /*
         * Register the observer for the data table. The table's path
         * and any of its subpaths trigger the observer.
         */
        syncdbapp=ctx.openOrCreateDatabase("syncdbapp", Context.MODE_PRIVATE,null);
        Cursor syncCusrsor=syncdbapp.rawQuery("SELECT * FROM appdata",null);
        if(syncCusrsor.moveToFirst()){
            do{
               String tablename=syncCusrsor.getString(1);
                mUri = new Uri.Builder()
                        .scheme(SCHEME)
                        .authority(AUTHORITY)
                        .path(tablename)
                        .build();
                mResolver.registerContentObserver(mUri, true, observer);
            }while (syncCusrsor.moveToNext());

        }
        syncCusrsor.close();


    }

    public class TableObserver extends ContentObserver {
        long lastTimeofCall = 0L;
        long lastTimeofUpdate = 0L;
        long threshold_time = 10000;


        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public TableObserver(Handler handler) {
            super(handler);
        }

        /*
                 * Define a method that's called when data in the
                 * observed content provider changes.
                 * This method signature is provided for compatibility with
                 * older platforms.
                 *
                 *
                 */


        @Override
        public void onChange(boolean selfChange) {
            /*
             * Invoke the method signature available as of
             * Android platform version 4.1, with a null URI.
             */
            onChange(selfChange, null);
        }
        /*
         * Define a method that's called when data in the
         * observed content provider changes.
         */
        @Override
        public void onChange(boolean selfChange, Uri changeUri) {
            /*
             * Ask the framework to run your sync adapter.
             * To maintain backward compatibility, assume that
             * changeUri is null.
             */

         /*   if(getTableName(changeUri).equalsIgnoreCase("")) {
                //do your stuff
            }*/

         if(changeUri.getQueryParameter("operation")==null){
            //    lastTimeofCall = System.currentTimeMillis();

             //   if(lastTimeofCall - lastTimeofUpdate > threshold_time){

                    //write your code to find updated contacts here

               //     lastTimeofUpdate = System.currentTimeMillis();

                    Bundle extras=new Bundle();
                    extras.putString("table",getTableName(changeUri));

             extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL,true);
             ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
                    ContentResolver.requestSync(null, AUTHORITY, extras);
               // }
            }else if(changeUri.getQueryParameter("operation").equalsIgnoreCase("update")){

             String query=changeUri.getQuery();
             String[] splitarray=query.split("&");
             String where="";
             for(int i=1;i<splitarray.length;i++){
                 String[] splitarray2=splitarray[i].split("=");
                 String param=splitarray2[1];
                 param="'"+param+"'";
                 if(i>1){
                     where=where+" AND ";
                 }
                 where=where+splitarray2[0]+"="+param;

             }

             Bundle extras=new Bundle();
             extras.putString("table",getTableName(changeUri));
             extras.putString("where",where);
             extras.putString("operation","update");

             extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL,true);
             ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
             ContentResolver.requestSync(null, AUTHORITY, extras);


         }else if(changeUri.getQueryParameter("operation").equalsIgnoreCase("delete")){

             String query=changeUri.getQuery();
             String[] splitarray=query.split("&");
             String where="";
             for(int i=1;i<splitarray.length;i++){
                 where=where+splitarray[i];
             }

             Bundle extras=new Bundle();
             extras.putString("table",getTableName(changeUri));
             extras.putString("where",where);
             extras.putString("operation","delete");

             extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL,true);
             ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
             ContentResolver.requestSync(null, AUTHORITY, extras);


         }

        }

        public  String getTableName(Uri uri){
            String value = uri.getPath();
            value = value.replace("/", "");//we need to remove '/'
            return value;
        }
    }

    public void stopSync(Context ctx) {

        mAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);

        final AccountManager accountManager = (AccountManager) ctx
                .getSystemService(ACCOUNT_SERVICE);

        if (!accountManager.addAccountExplicitly(mAccount, null, null)) {
            mAccount = accountManager.getAccountsByType(ACCOUNT_TYPE)[0];
        }

        if (ContentResolver.getIsSyncable(mAccount, AUTHORITY) == 1) {
            ContentResolver.setIsSyncable(mAccount, ACCOUNT, 0);
        }


    }


    public void startSync(Context ctx) {

        mAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);

        final AccountManager accountManager = (AccountManager) ctx
                .getSystemService(ACCOUNT_SERVICE);

        if (!accountManager.addAccountExplicitly(mAccount, null, null)) {
            mAccount = accountManager.getAccountsByType(ACCOUNT_TYPE)[0];
        }

        if (ContentResolver.getIsSyncable(mAccount, AUTHORITY) == 0) {
            ContentResolver.setIsSyncable(mAccount, ACCOUNT, 1);
        }


    }

}
