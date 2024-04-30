package com.intuition.ivepos;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.intuition.ivepos.paytm.Card_Wallets_Settings1;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Connect_Counter extends AppCompatActivity {

    public SQLiteDatabase db = null;
    String nameget, addget, statusget, deviceget;
    String ipnamegets, statusnets, portgets;
    String ipget, portget, p_statusget, p_name_get;
    TextView item_selec_number;

    String WebserviceUrl;
    String account_selection;

    private static final String[] BLE_PERMISSIONS = new String[]{
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    };

    @RequiresApi(api = Build.VERSION_CODES.S)
    private static final String[] ANDROID_12_BLE_PERMISSIONS = new String[]{
            android.Manifest.permission.BLUETOOTH_SCAN,
            android.Manifest.permission.BLUETOOTH_CONNECT,
            android.Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
    };

    public static void requestBlePermissions(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            ActivityCompat.requestPermissions(activity, ANDROID_12_BLE_PERMISSIONS, requestCode);
        else
            ActivityCompat.requestPermissions(activity, BLE_PERMISSIONS, requestCode);
    }
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kot_slot_settings);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        TextView conn_status = (TextView) findViewById(R.id.conn_status);
        TextView printer_name = (TextView) findViewById(R.id.printer_name);
        item_selec_number = (TextView) findViewById(R.id.item_selec_number);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(Connect_Counter.this);
        account_selection= sharedpreferences_select.getString("account_selection", null);

        if (account_selection.toString().equals("Dine")) {
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }else {
            if (account_selection.toString().equals("Qsr")) {
                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
            }else {
                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
            }
        }

        RelativeLayout department_name = (RelativeLayout) findViewById(R.id.department_name);
        department_name.setVisibility(View.GONE);

        RelativeLayout item_selec = (RelativeLayout) findViewById(R.id.item_selec);
        item_selec.setVisibility(View.GONE);

        TextView user = (TextView) findViewById(R.id.user);
        user.setText("Counter");
        Cursor conn = db.rawQuery("SELECT * FROM BTConn", null);
        if (conn.moveToFirst()) {
            nameget = conn.getString(1);
            addget = conn.getString(2);
            statusget = conn.getString(3);
            deviceget = conn.getString(4);
            if (statusget.toString().equals("ok")) {
                if (deviceget.toString().equals("bluetooth")) {
                    conn_status.setText("Connected");
                    printer_name.setText(deviceget);
                }
            }
        }
        conn.close();

        Cursor connnet = db.rawQuery("SELECT * FROM IPConn", null);
        if (connnet.moveToFirst()) {
            ipnamegets = connnet.getString(1);
            portgets = connnet.getString(2);
            statusnets = connnet.getString(3);
            if (statusnets.toString().equals("ok")) {
                conn_status.setText("Connected");
                printer_name.setText("Wifi");
            }
        }
        connnet.close();




        ImageButton btncancel = (ImageButton) findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (account_selection.toString().equals("Dine")) {
                    Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                    if (cursor3.moveToFirst()) {
                        String lite_pro = cursor3.getString(1);

                        TextView tv = new TextView(Connect_Counter.this);
                        tv.setText(lite_pro);

                        if (tv.getText().toString().equals("Lite")) {
                            Intent intent = new Intent(Connect_Counter.this, BeveragesMenuFragment_Dine_l.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(Connect_Counter.this, BeveragesMenuFragment_Dine.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }else {
                        Intent intent = new Intent(Connect_Counter.this, BeveragesMenuFragment_Dine_l.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
//                    Intent intent = new Intent(Connect_Counter.this, BeveragesMenuFragment_Dine.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                LoginActivity.this.finish();
//                    startActivity(intent);
                }else {
                    if (account_selection.toString().equals("Qsr")) {
                        Intent intent = new Intent(Connect_Counter.this, BeveragesMenuFragment_Qsr.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                LoginActivity.this.finish();
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(Connect_Counter.this, BeveragesMenuFragment_Retail.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                LoginActivity.this.finish();
                        startActivity(intent);
                    }
                }
//                Intent intent = new Intent(Connect_Counter.this, BeveragesMenuFragment_Dine.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                LoginActivity.this.finish();
//                startActivity(intent);
            }
        });

//        RelativeLayout department_name = (RelativeLayout) findViewById(R.id.department_name);
        department_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialogauth = new Dialog(Connect_Counter.this, R.style.notitle);
                dialogauth.setContentView(R.layout.kot_slot_settings);
                dialogauth.setCanceledOnTouchOutside(false);
                dialogauth.show();
            }
        });

        RelativeLayout printer_selec = (RelativeLayout) findViewById(R.id.printer_selec);
        printer_selec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Connect_Counter.this);
//                alertDialog.setMessage("hi");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Connect_Counter.this, android.R.layout.simple_selectable_list_item);
                arrayAdapter.add("Bluetooth");
                arrayAdapter.add("Wifi/Network");
                alertDialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String strName = arrayAdapter.getItem(which);
                        if (strName.equals("Bluetooth")){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                                ActivityCompat.requestPermissions(Connect_Counter.this, ANDROID_12_BLE_PERMISSIONS, 9);
                            else
                                ActivityCompat.requestPermissions(Connect_Counter.this, BLE_PERMISSIONS, 9);
//                            Intent intentt = new Intent(Connect_Counter.this, SearchBTActivity.class);
//                            startActivity(intentt);
//                            hideSoftKeyboard();
                        }else {
                            if (strName.equals("Wifi/Network")) {
                                Intent intenttt = new Intent(Connect_Counter.this, SearchIPActivity.class);
                                startActivity(intenttt);
                                hideSoftKeyboard();
                            }
                        }

                    }
                });
                alertDialog.show();
            }
        });

//        RelativeLayout item_selec = (RelativeLayout) findViewById(R.id.item_selec);
        item_selec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Connect_Counter.this, KOT_MultiSelection_items.class);
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (account_selection.toString().equals("Dine")) {
            Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
            if (cursor3.moveToFirst()) {
                String lite_pro = cursor3.getString(1);

                TextView tv = new TextView(Connect_Counter.this);
                tv.setText(lite_pro);

                if (tv.getText().toString().equals("Lite")) {
                    Intent intent = new Intent(Connect_Counter.this, BeveragesMenuFragment_Dine_l.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(Connect_Counter.this, BeveragesMenuFragment_Dine.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }else {
                Intent intent = new Intent(Connect_Counter.this, BeveragesMenuFragment_Dine_l.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
//            Intent intent = new Intent(Connect_Counter.this, BeveragesMenuFragment_Dine.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                LoginActivity.this.finish();
//            startActivity(intent);
        }else {
            if (account_selection.toString().equals("Qsr")) {
                Intent intent = new Intent(Connect_Counter.this, BeveragesMenuFragment_Qsr.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                LoginActivity.this.finish();
                startActivity(intent);
            }else {
                Intent intent = new Intent(Connect_Counter.this, BeveragesMenuFragment_Retail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                LoginActivity.this.finish();
                startActivity(intent);
            }
        }

//        Intent intent = new Intent(Connect_Counter.this, BeveragesMenuFragment_Dine.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                LoginActivity.this.finish();
//        startActivity(intent);
        super.onBackPressed();
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        Toast.makeText(Add_manage_ingredient_Activity.this, "1", Toast.LENGTH_LONG).show();
//        if (requestCode == 1) {
////            Toast.makeText(Add_manage_ingredient_Activity.this, "2", Toast.LENGTH_LONG).show();
//            if(resultCode == RESULT_OK) {
////                Toast.makeText(Add_manage_ingredient_Activity.this, "3", Toast.LENGTH_LONG).show();
//                String strEditText = data.getStringExtra("editTextValue");
//                String player1name = data.getStringExtra("player1name");
////                Toast.makeText(Add_manage_ingredient_Activity.this, "2q "+strEditText, Toast.LENGTH_LONG).show();
//                if (player1name.equals("insert")) {
//                    item_selec_number.setText(strEditText+" selected");
//                }
//            }
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 9: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intentt = new Intent(Connect_Counter.this, SearchBTActivity.class);
                    startActivity(intentt);
                    hideSoftKeyboard();

                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                    if (mBluetoothAdapter == null) {

                    } else {
                        if (null == mBluetoothAdapter) {
                            finish();

                        }

                        if (!mBluetoothAdapter.isEnabled()) {
                            if (mBluetoothAdapter.enable()) {
                                while (!mBluetoothAdapter.isEnabled())
                                    ;
                                Log.v("Connect_Counter", "Enable BluetoothAdapter");
                            } else {
                                finish();

                            }
                        }

                        mBluetoothAdapter.cancelDiscovery();
                        mBluetoothAdapter.startDiscovery();

                    }

                } else {
                    Toast.makeText(Connect_Counter.this, "Permission denied, Enable near by permission from Settings", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

}
