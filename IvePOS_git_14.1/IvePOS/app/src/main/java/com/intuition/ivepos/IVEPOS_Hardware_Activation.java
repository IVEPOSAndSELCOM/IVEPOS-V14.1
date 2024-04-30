package com.intuition.ivepos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rohithkumar on 3/23/2016.
 */

public class IVEPOS_Hardware_Activation extends AppCompatActivity {


    String activation_code_program = "0000";
    String activation_code_address = "ac:af:b9:42:36:32";
    public SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_hardware_activation);

        //Toast.makeText(IVEPOS_Hardware_Activation.this, "same mac address 10", Toast.LENGTH_SHORT).show();

        //Toast.makeText(IVEPOS_Hardware_Activation.this, " "+getMacAddr(), Toast.LENGTH_SHORT).show();

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        final String macAddress = getMacAddr();
//        final String macAddress = "0166fba6-657e-4081-8e5b-c02545ba2bd1";

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//        String id = UUID.randomUUID().toString();
//        Toast.makeText(IVEPOS_Hardware_Activation.this, "UUID is "+id, Toast.LENGTH_SHORT).show();

        db = openOrCreateDatabase("mydb_Activateddata", Context.MODE_PRIVATE, null);
        crearYasignar(db);


        //Toast.makeText(IVEPOS_Hardware_Activation.this, "same mac address 0", Toast.LENGTH_SHORT).show();
        db = openOrCreateDatabase("mydb_Activateddata", Context.MODE_PRIVATE, null);

//        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy MM dd");
//        final String currentDateandTime1 = sdf2.format(new Date());
//
//        ContentValues contentValues1 = new ContentValues();
//        contentValues1.put("date", currentDateandTime1);
//        db.execSQL("DROP TABLE IF EXISTS Activated_date");
//        crearYasignar(db);
//        db.insert("Activated_date", null, contentValues1);
//
//        Cursor date = db.rawQuery("SELECT * FROM Activated_date", null);
//        if (date.moveToFirst()){
//            String date1 = date.getString(1);
//            if (date1.toString().equals("2016 06 27")){
//                activation_code_address = "0:22:f4:3d:7f:7";
//            }
//        }

        Cursor access = db.rawQuery("SELECT * FROM Activated ", null);

        if (access.moveToFirst()) {
            //Toast.makeText(IVEPOS_Hardware_Activation.this, "same mac address 1", Toast.LENGTH_SHORT).show();
            String status = access.getString(1);
            if (status.toString().equals("yes")){
                //Toast.makeText(IVEPOS_Hardware_Activation.this, "same mac address 2", Toast.LENGTH_SHORT).show();
                if (macAddress.toString().equals(activation_code_address)){
                    Intent intent = new Intent(IVEPOS_Hardware_Activation.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    //Toast.makeText(IVEPOS_Hardware_Activation.this, "same mac address 3", Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(IVEPOS_Hardware_Activation.this, "same mac address 4", Toast.LENGTH_SHORT).show();
                    final Dialog dialog = new Dialog(IVEPOS_Hardware_Activation.this, R.style.timepicker_date_dialog);
                    dialog.setContentView(R.layout.dialog_hardware_activation_failed);
                    dialog.show();

                    dialog.setCanceledOnTouchOutside(false);
                    Button button = (Button) dialog.findViewById(R.id.donee);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });

                    dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                return true;
                            }
                            return false;
                        }
                    });
                }
                //Toast.makeText(IVEPOS_Hardware_Activation.this, "same mac address 5", Toast.LENGTH_SHORT).show();

            }else {
                //Toast.makeText(IVEPOS_Hardware_Activation.this, "same mac address 6", Toast.LENGTH_SHORT).show();

            }
        }else {
            //Toast.makeText(IVEPOS_Hardware_Activation.this, "same mac address 7", Toast.LENGTH_SHORT).show();
            ContentValues contentValues = new ContentValues();
            contentValues.put("status", "no");
            db.insert("Activated", null, contentValues);
        }

//        Dialog dialog = new Dialog(IVEPOS_Hardware_Activation.this, R.style.notitle);
//        dialog.setContentView(R.layout.dialog_hardware_activation);
//        dialog.show();



        final EditText activation_code = (EditText) findViewById(R.id.activation_code);

        final Button submit = (Button) findViewById(R.id.btnsave);

        activation_code.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    submit.performClick();
                    return true;
                }
                return false;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activation_code.getText().toString().equals(activation_code_program)){
                    if (macAddress.toString().equals(activation_code_address)){
                        ////Toast.makeText(IVEPOS_Hardware_Activation.this, "same mac address", Toast.LENGTH_SHORT).show();
                        Dialog dialog1 = new Dialog(IVEPOS_Hardware_Activation.this, R.style.timepicker_date_dialog);
                        dialog1.setContentView(R.layout.dialog_hardware_activated);
                        dialog1.show();

                        db.execSQL("delete from Activated");
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("status", "yes");
                        db.insert("Activated", null, contentValues);


                        Intent intent = new Intent(IVEPOS_Hardware_Activation.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }else {
                        activation_code.setError("Activation failed");
                    }
                }else {
                    activation_code.setError("Invalid activation code");
                }
            }
        });
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
                //Toast.makeText(MainActivity.this, " "+res1.toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public void crearYasignar(SQLiteDatabase dbllega) {
        try {
            dbllega.execSQL("CREATE TABLE if not exists Activated (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, status text);");
            dbllega.execSQL("CREATE TABLE if not exists Activated_date (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, date text);");

        } catch (SQLiteException e) {
            alertas("Error desconocido: " + e.getMessage());
        }
    }

    public void alertas(String alerta) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(IVEPOS_Hardware_Activation.this, R.style.AppTheme);
        AlertDialog.Builder builder = new AlertDialog.Builder(wrapper);
        builder.setIcon(R.drawable.icon);
        builder.setTitle(R.string.app_name);
        builder.setMessage(alerta);
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
