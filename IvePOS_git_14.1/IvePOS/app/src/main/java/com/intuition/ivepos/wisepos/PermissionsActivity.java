package com.intuition.ivepos.wisepos;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;


import com.intuition.ivepos.R;
import com.intuition.ivepos.mSwipe.ApplicationData;
import com.intuition.ivepos.mSwipe.Logs;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PermissionsActivity extends Activity {

    public static final int REQUEST_RECORD_AUDIO = 0;
    public static final int REQUEST_BBPOS = 1;
    public static final int REQUEST_READ_PHONE_STATE = 2;
    public static final int REQUEST_READ_EXTERNAL_STORAGE = 3;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 4;
    public static final int REQUEST_ACCESS_COARSE_LOCATION = 5;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONSS = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermissions();
    }

    private void requestPermissions()
    {
        int permissionAudio = checkSelfPermission(RECORD_AUDIO);
        int permissionBBPOS = checkSelfPermission("android.permission.BBPOS");
        int permissionPhoneState = checkSelfPermission(READ_PHONE_STATE);
        int permissionReadStorage = checkSelfPermission(READ_EXTERNAL_STORAGE);
        int permissionWriteStorage = checkSelfPermission(WRITE_EXTERNAL_STORAGE);
        int permissionCoarseLocation = checkSelfPermission(ACCESS_COARSE_LOCATION);

        Logs.v(ApplicationData.packName, "permissionAudio: " + permissionAudio, true, true);
        Logs.v(ApplicationData.packName, "permissionBBPOS: " + permissionBBPOS, true, true);
        Logs.v(ApplicationData.packName, "permissionPhoneState: " + permissionPhoneState, true, true);
        Logs.v(ApplicationData.packName, "permissionReadStorage: " + permissionReadStorage, true, true);
        Logs.v(ApplicationData.packName, "permissionWriteStorage: " + permissionWriteStorage, true, true);

        Logs.v(ApplicationData.packName, "permissionCoarseLocation: " + permissionCoarseLocation, true, true);

        if (permissionAudio == PackageManager.PERMISSION_GRANTED
                && permissionBBPOS == PackageManager.PERMISSION_GRANTED
                && permissionPhoneState == PackageManager.PERMISSION_GRANTED
                && permissionReadStorage == PackageManager.PERMISSION_GRANTED
                && permissionWriteStorage == PackageManager.PERMISSION_GRANTED
                && permissionCoarseLocation == PackageManager.PERMISSION_GRANTED) {

        } else {

            List<String> listPermissionsNeeded = new ArrayList<>();
            if (permissionAudio != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(RECORD_AUDIO);
            }
            if (permissionBBPOS != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add("android.permission.BBPOS");
            }
            if (permissionPhoneState != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(READ_PHONE_STATE);
            }
            if (permissionReadStorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(READ_EXTERNAL_STORAGE);
            }
            if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(WRITE_EXTERNAL_STORAGE);
            }
            if (permissionCoarseLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(ACCESS_COARSE_LOCATION);
            }

            if (!listPermissionsNeeded.isEmpty()) {
                requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONSS);
            }
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        Logs.v(ApplicationData.packName, "requestCode: " + requestCode, true, true);
        Logs.v(ApplicationData.packName, "grantResults.length: " + grantResults.length, true, true);

        try {
            for (int i =0; i< grantResults.length; i++)
            {
                Logs.v(ApplicationData.packName, "grantResults["+i+"] :" + grantResults[i], true, true);
            }
        }catch (Exception e){}


        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Logs.v(ApplicationData.packName, "permissionsGranted : REQUEST_RECORD_AUDIO", true, true);
            }
        }

        if (requestCode == REQUEST_BBPOS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Logs.v(ApplicationData.packName, "permissionsGranted : REQUEST_BBPOS", true, true);
            }
        }

        if (requestCode == REQUEST_READ_PHONE_STATE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Logs.v(ApplicationData.packName, "permissionsGranted : REQUEST_READ_PHONE_STATE", true, true);
            }
        }

        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Logs.v(ApplicationData.packName, "permissionsGranted : REQUEST_READ_EXTERNAL_STORAGE", true, true);
            }
        }

        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Logs.v(ApplicationData.packName, "permissionsGranted : REQUEST_WRITE_EXTERNAL_STORAGE", true, true);
            }
        }

        if (requestCode == REQUEST_ACCESS_COARSE_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Logs.v(ApplicationData.packName, "permissionsGranted : REQUEST_ACCESS_COARSE_LOCATION", true, true);
            }
        }

        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONSS) {
            Logs.v(ApplicationData.packName, "permissionsGranted : ALL", true, true);
        }

        permissionsGranted();
    }

    private void permissionsGranted() {
        finish();
    }

}
