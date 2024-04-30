package com.intuition.ivepos.paytm;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.intuition.ivepos.R;
import com.intuition.ivepos.syncapp.StubProviderApp;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by intuition on 12-10-2017.
 */

public class MobikwikSetting extends AppCompatActivity {
    LinearLayout reset;
    TextView register;
    ImageView back;
    ProgressDialog progress;
    EditText merchantname, mid_otp, secretkey_otp, mid_debit, secretkey_debit;

    public static final String PACKAGE_NAME = "package com.intuition.ivepos";
    public static final String DATABASE_NAME = "mydb_Appdata";
    public SQLiteDatabase db = null;
    private static final File DATA_DIRECTORY_DATABASE =
            new File(Environment.getDataDirectory() + "/data/" + PACKAGE_NAME + "/databases/" + DATABASE_NAME);
    long aaa = 0;
    Uri contentUri,resultUri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobikwik_setting);

        db = openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);

        merchantname = (EditText) findViewById(R.id.merchantname);
        mid_otp = (EditText) findViewById(R.id.mid_otp);
        secretkey_otp = (EditText) findViewById(R.id.secretkey_otp);
        mid_debit = (EditText) findViewById(R.id.mid_debit);
        secretkey_debit = (EditText) findViewById(R.id.secretkey_debit);
        reset = (LinearLayout) findViewById(R.id.btnreset_pane);
        register = (TextView) findViewById(R.id.btnreg);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(merchantname.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mid_otp.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(secretkey_otp.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mid_debit.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(secretkey_debit.getWindowToken(), 0);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MobikwikSetting.this, Card_Wallets_Settings.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
            }
        });

//        db.execSQL("CREATE TABLE IF NOT EXISTS MobikwikMerchantReg" +
//                "(_id integer primary key autoincrement unique, Account text, Merchant_name text, Mid_otp text, Secretkey_otp text, Mid_debit text, Secretkey_debit text)");
        db.execSQL("CREATE TABLE IF NOT EXISTS MobikwikMerchantReg(_id integer primary key autoincrement unique, Account text, Merchant_name text, " +
                "Mid_otp text, Secretkey_otp text, Mid_debit text, Secretkey_debit text)");

        Cursor c1 = db.rawQuery("SELECT * FROM MobikwikMerchantReg WHERE Account = 'Registered'", null);
        if (c1.moveToFirst()) {
            do {
                String account = c1.getString(1);
                String MerchantName = c1.getString(2);
                String Mid_otp = c1.getString(3);
                String Secretkey_otp = c1.getString(4);
                String Mid_debit = c1.getString(5);
                String Secretkey_debit = c1.getString(6);
                if (account.equals("Registered")) {
                    merchantname.setText(MerchantName);
                    mid_otp.setText(Mid_otp);
                    secretkey_otp.setText(Secretkey_otp);
                    mid_debit.setText(Mid_debit);
                    secretkey_debit.setText(Secretkey_debit);

                    register.setVisibility(View.GONE);
                    reset.setVisibility(View.VISIBLE);
                }
                else {
                    register.setVisibility(View.VISIBLE);
                    reset.setVisibility(View.GONE);
                }
            } while (c1.moveToNext());
        }
        else {
            reset.setVisibility(View.GONE);
            register.setVisibility(View.VISIBLE);
        }
        c1.close();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progress = new ProgressDialog(MobikwikSetting.this);
//                progress.setTitle("Please Wait!!");
//                progress.setMessage("Registering...");
//                progress.setCancelable(true);
//                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                progress.show();

//                Timer timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        progress.dismiss();
//
//                    }
//                }, 1000);

                if (merchantname.getText().toString().equals("")
                        || mid_otp.getText().toString().equals("") || mid_otp.getText().toString().length() < 7
                        || secretkey_otp.getText().toString().equals("") || secretkey_otp.getText().toString().length() < 28
                        || mid_debit.getText().toString().equals("") || mid_debit.getText().toString().length() < 9
                        || secretkey_debit.getText().toString().equals("") || secretkey_debit.getText().toString().length() < 28) {
                    if (merchantname.getText().toString().equals("") ) {
                        merchantname.setError("Enter valid Merchat Name");
                    }
                    if (mid_otp.getText().toString().equals("") || mid_otp.getText().toString().length() < 36) {
                        mid_otp.setError("Enter valid MID-OTP");
                    }
                    if (secretkey_otp.getText().toString().equals("") || secretkey_otp.getText().toString().length() < 20) {
                        secretkey_otp.setError("Enter valid Secret Key-OTP");
                    }
                    if (mid_debit.getText().toString().equals("") || mid_debit.getText().toString().length() < 16) {
                        mid_debit.setError("Enter valid MID-Debit");
                    }
                    if (secretkey_debit.getText().toString().equals("") || secretkey_debit.getText().toString().length() < 16) {
                        secretkey_debit.setError("Enter valid Secret Key-Debit");
                    }
                }
 else {
                    Cursor cursor = db.rawQuery("SELECT * FROM MobikwikMerchantReg WHERE Account = 'Not_Registered'", null);
                    if (cursor.moveToFirst()) {
                        ContentValues contentV = new ContentValues();
                        contentV.put("Account", "Registered");
                        contentV.put("Merchant_name", merchantname.getText().toString());
                        contentV.put("Mid_otp", mid_otp.getText().toString());
                        contentV.put("Secretkey_otp", secretkey_otp.getText().toString());
                        contentV.put("Mid_debit", mid_debit.getText().toString());
                        contentV.put("Secretkey_debit", secretkey_debit.getText().toString());


                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "MobikwikMerchantReg");
                        getContentResolver().update(contentUri, contentV, "Account = 'Not_Registered'",null);
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("MobikwikMerchantReg")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("Account","Not_Registered")
                                .build();
                        getContentResolver().notifyChange(resultUri, null);



                      //  db.update("MobikwikMerchantReg", contentV, "Account = 'Not_Registered'", null);
                        Toast.makeText(MobikwikSetting.this, getString(R.string.title41), Toast.LENGTH_SHORT).show();
                        register.setVisibility(View.GONE);
                        reset.setVisibility(View.VISIBLE);

                        startActivity(new Intent(MobikwikSetting.this, Card_Wallets_Settings.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        return;
                    }
//                    Intent inent = new Intent(MobikwikSetting.this, Card_Wallets_Settings.class);
//                    inent.addFlags(inent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(inent);
                    cursor.close();
                }
            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(com.intuition.ivepos.paytm.MobikwikSetting.this);
                builder.setTitle("Are you sure, you want to delete account?");
                builder.setMessage(getString(R.string.setmessage42));
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                progress = new ProgressDialog(MobikwikSetting.this);
                                progress.setTitle("Please Wait!!");
                                progress.setMessage(getString(R.string.setmessage43));
                                progress.setCancelable(true);
                                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progress.show();
                                Timer timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        progress.dismiss();

                                    }
                                }, 1000);
                                // Delete Table data...
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("Account", "Not_Registered");
                                contentValues.put("Mid_otp", "");
                                contentValues.put("Merchant_name", "");
                                contentValues.put("Secretkey_otp", "");
                                contentValues.put("Mid_debit", "");
                                contentValues.put("Secretkey_debit", "");


                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "MobikwikMerchantReg");
                                getContentResolver().update(contentUri, contentValues, "Account = 'Registered'",null);
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProviderApp.AUTHORITY)
                                        .path("MobikwikMerchantReg")
                                        .appendQueryParameter("operation", "update")
                                        .appendQueryParameter("Account","Registered")
                                        .build();
                                getContentResolver().notifyChange(resultUri, null);


                         //       db.update("MobikwikMerchantReg", contentValues, "Account = 'Registered'", null);
                                Toast.makeText(MobikwikSetting.this, "Data Deleted", Toast.LENGTH_SHORT).show();

                                merchantname.setText("");
                                mid_otp.setText("");
                                secretkey_otp.setText("");
                                mid_debit.setText("");
                                secretkey_debit.setText("");

                                reset.setVisibility(View.GONE);
                                register.setVisibility(View.VISIBLE);
                            }
                        }
                )
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                }
                        );
                builder.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Card_Wallets_Settings.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        return;
    }
}
