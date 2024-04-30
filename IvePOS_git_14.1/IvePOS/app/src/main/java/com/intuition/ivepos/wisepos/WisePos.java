package com.intuition.ivepos.wisepos;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.mswipetech.wisepad.sdk.view.MSAARHandlerActivity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class WisePos {
    private int REQUEST_CODE_PERMISSIONS = 2002;
    private static final String MS_CARDSALE_ACTIVITY_INTENT_ACTION =
            "mswipe.wisepad.sdk.CardSaleAction";




    SQLiteDatabase db=null;

    public void startmswipePayment(Activity ctx, String fulltotal, String phone, String billnumber){

             permissions(ctx);

                                         db = ctx.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                            String username="";
                                    String password="";
                                    Cursor payswiff = db.rawQuery("SELECT * FROM CardSwiperActivation WHERE _id=2", null);
                                    if (payswiff.moveToFirst()) {
                                         username= payswiff.getString(payswiff.getColumnIndex("merchantKey"));
                                         password= payswiff.getString(payswiff.getColumnIndex("partnerkey"));

                                    }

                                        try {
                                        Intent intent = new Intent(ctx, MSAARHandlerActivity.class);
                                        intent.setType(MS_CARDSALE_ACTIVITY_INTENT_ACTION);
                                        intent.putExtra("username", username);
                                        intent.putExtra("password", password);
                                        intent.putExtra("production", false);

                                        if (fulltotal.contains(".")) {
                                            String newtotal = fulltotal;
                                            intent.putExtra("Amount", newtotal);
                                        } else {
                                            String newtotal = fulltotal + ".00";
                                            intent.putExtra("Amount", newtotal);
                                        }

                                        intent.putExtra("MobileNumber", phone);
                                        intent.putExtra("Reciept",billnumber);
                                        intent.putExtra("Notes", "");
                                        intent.putExtra("MailId", "");
                                        intent.putExtra("extra1", "");
                                        intent.putExtra("extra2", "");
                                        intent.putExtra("extra3", "");
                                        intent.putExtra("extra2", "");

                                        intent.putExtra("orientation", "auto");
                                        intent.putExtra("isSignatureRequired", false);
                                        intent.putExtra("isPrinterSupported", false);
                                        intent.putExtra("isPrintSignatureOnReceipt", false);
                                        ctx.startActivityForResult(intent, 1003);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

    }

    private void permissions(Activity ctx) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.RECORD_AUDIO,
                    "android.permission.BBPOS"};

            int permissionAudio = ctx.checkSelfPermission(RECORD_AUDIO);
            int permissionBBPOS = ctx.checkSelfPermission("android.permission.BBPOS");
            int permissionPhoneState = ctx.checkSelfPermission(READ_PHONE_STATE);
            int permissionReadStorage = ctx.checkSelfPermission(READ_EXTERNAL_STORAGE);
            int permissionWriteStorage = ctx.checkSelfPermission(WRITE_EXTERNAL_STORAGE);
            int permissionCoarseLocation = ctx.checkSelfPermission(ACCESS_COARSE_LOCATION);

            if (permissionAudio == PackageManager.PERMISSION_GRANTED
                    && permissionPhoneState == PackageManager.PERMISSION_GRANTED
                    && permissionReadStorage == PackageManager.PERMISSION_GRANTED
                    && permissionWriteStorage == PackageManager.PERMISSION_GRANTED
                    && permissionCoarseLocation == PackageManager.PERMISSION_GRANTED) {
            }
            else {
                requestPermissions(ctx);
            }

        }

    }

    private void requestPermissions(Activity ctx) {
        Intent intent = new Intent(ctx, PermissionsActivity.class);
        ctx.startActivityForResult(intent, REQUEST_CODE_PERMISSIONS);
    }



    public void startpay(Activity ctx, String fulltotal, String phone, String billnumber){

        Intent intent = new Intent(ctx, MSAARHandlerActivity.class);
        intent.setType(MS_CARDSALE_ACTIVITY_INTENT_ACTION);

        //Intent intent = new Intent();
        //intent.setAction(MS_CARDSALE_ACTIVITY_INTENT_ACTION);
        intent.putExtra("username", "9110846980");
        intent.putExtra("password", "123456");
        intent.putExtra("production", false);



        intent.putExtra("Amount", "125.00");
        intent.putExtra("MobileNumber", "7907993209");
        intent.putExtra("Reciept", "qbc1234");
        intent.putExtra("Notes", "");
        intent.putExtra("MailId", "");
        intent.putExtra("extra1", "");
        intent.putExtra("extra2", "");
        intent.putExtra("extra3", "");
        intent.putExtra("extra4", "");
        intent.putExtra("extra5","");
        intent.putExtra("extra6", "");
        intent.putExtra("extra7","");
        intent.putExtra("extra8", "");
        intent.putExtra("extra9","");
        intent.putExtra("extra10", "");
        intent.putExtra("networkSourceType", "sim");
        intent.putExtra("orientation", "auto");
        intent.putExtra("isSignatureRequired", false);
        intent.putExtra("isPrinterSupported",false);
        intent.putExtra("isPrintSignatureOnReceipt",false);
        ctx.startActivityForResult(intent, 1003);

    }


}
