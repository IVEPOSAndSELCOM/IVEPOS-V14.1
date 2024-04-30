package com.intuition.ivepos.firebase;

/**
 * Created by HP on 10/3/2018.
 */

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.intuition.ivepos.csv.ForegroundService;
import com.intuition.ivepos.csv.UpdateTaxeService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        /*if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }*/

        // Check if message contains a data payload.

      /*  NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.playNotificationSound();*/
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    public void startService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Processing");

        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void startServiceTaxes() {
        Intent serviceIntent = new Intent(this, UpdateTaxeService.class);
        serviceIntent.putExtra("inputExtra", "Processing");

        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");
            String query="INSERT INTO items(_id,itemname,price,stockquan,category,itemtax,image,favourites,disc_type,disc_value,image_text,barcode_value,quantity_sold,minimum_qty,minimum_qty_copy,add_qty,unit_type,tax_value,itemtax2,tax_value2,itemtax3,tax_value3,itemtax4,tax_value4,itemtax5,tax_value5,variant2,variant_price2,variant3,variant_price3,variant4,variant_price4,variant5,variant_price5) VALUES";

            if (data.has("message1")) {

                boolean foregroud = new ForegroundCheckTask().execute(getApplicationContext()).get();

                if(foregroud){
                    Intent intent = new Intent("myFunction");
                    intent.putExtra("items","items");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }

                startService();
//                SQLiteDatabase db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//                String message1 = data.getString("message1");
//                db.execSQL(query+message1);
//                String message2 = data.getString("message2");
//                db.execSQL(query+message2);
//                String message3 = data.getString("message3");
//                db.execSQL(query+message3);
//                String message4 = data.getString("message4");
//                db.execSQL(query+message4);
//                String message5 = data.getString("message5");
//                db.execSQL(query+message5);
//                String message6 = data.getString("message6");
//                db.execSQL(query+message6);
//                String message7 = data.getString("message7");
//                db.execSQL(query+message7);
//                String message8 = data.getString("message8");
//                db.execSQL(query+message8);
//                String message9 = data.getString("message9");
//                db.execSQL(query+message9);
            }else if(data.has("message2")) {
                boolean foregroud = new ForegroundCheckTask().execute(getApplicationContext()).get();

                if(foregroud){
                    Intent intent = new Intent("myFunction");
                    intent.putExtra("items","items");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }
                startServiceTaxes();
            }else{
                String message = data.getString("message");
                SQLiteDatabase db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                SQLiteDatabase db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                if(message.equalsIgnoreCase("delete from items")){

                }


                if ((message.contains("customerdetails")) || (message.contains("cust_feedback"))){
                    db1.execSQL(message);
                }else {
                    db.execSQL(message);
                }
            }



        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }

        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }
}