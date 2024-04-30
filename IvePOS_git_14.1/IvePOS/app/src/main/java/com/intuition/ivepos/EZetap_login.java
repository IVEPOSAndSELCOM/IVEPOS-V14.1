package com.intuition.ivepos;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.intuition.ivepos.mSwipe.LoginView;
import com.intuition.ivepos.paytm.Card_Wallets_Settings1;
import com.intuition.ivepos.syncapp.StubProviderApp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EZetap_login extends AppCompatActivity {

    public SQLiteDatabase db = null;
    Uri contentUri,resultUri;
    LinearLayout clear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_ezetap);

        ImageView back_activity = (ImageView) findViewById(R.id.back);
        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(New_OrderActivity_w.this, MainActivity.class);
                startActivity(intent);*/
                finish();
            }
        });

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        EditText login_TXT_merchantid = (EditText) findViewById(R.id.login_TXT_merchantid);
        EditText login_TXT_merchantpassword = (EditText) findViewById(R.id.login_TXT_merchantpassword);
        EditText prod_key = (EditText) findViewById(R.id.prod_key);
        EditText orgcode = (EditText) findViewById(R.id.orgcode);

        TextView login_BTN_signin = (TextView) findViewById(R.id.login_BTN_signin);
        clear = (LinearLayout) findViewById(R.id.clear);

        Cursor cursor = db.rawQuery("SELECT * FROM CardSwiperActivation WHERE _id = 6", null);
        if (cursor.moveToFirst()) {
            String un = cursor.getString(2);
            String pw = cursor.getString(3);
            String acti = cursor.getString(4);
            String prkey = cursor.getString(5);
            String orcode = cursor.getString(6);

            if (acti.toString().equals("Activated")) {
                login_TXT_merchantid.setText(un);
                login_TXT_merchantpassword.setText(pw);
                prod_key.setText(prkey);
                orgcode.setText(orcode);

                clear.setVisibility(View.VISIBLE);
                login_BTN_signin.setVisibility(View.GONE);
            }

        }
        cursor.close();


        login_BTN_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login_TXT_merchantid.getText().toString().equals("") || login_TXT_merchantpassword.getText().toString().equals("")) {
                    if (login_TXT_merchantid.getText().toString().equals("")) {
                        login_TXT_merchantid.setError("Enter Username");
                    }
                    if (login_TXT_merchantpassword.getText().toString().equals("")) {
                        login_TXT_merchantpassword.setError("Enter Password");
                    }
                }else {
                    ContentValues cv = new ContentValues();
                    cv.put("merchantKey", login_TXT_merchantid.getText().toString());
                    cv.put("partnerkey", login_TXT_merchantpassword.getText().toString());
                    cv.put("Config_status", "Activated");
                    cv.put("prodAppKey", prod_key.getText().toString());
                    cv.put("orgCode", orgcode.getText().toString());

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
                    getContentResolver().update(contentUri, cv, "_id=" + "6",null);
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("CardSwiperActivation")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id","6")
                            .build();
                    getContentResolver().notifyChange(resultUri, null);

                    startActivity(new Intent(EZetap_login.this, Card_Wallets_Settings1.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EZetap_login.this);
                builder.setTitle("Are you sure ?");
                builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // Delete Table data...
                                        ContentValues contentValues = new ContentValues();
//                                contentValues.put("CardSwiperName", "mSwipe");
                                        contentValues.put("merchantKey", "");
                                        contentValues.put("partnerkey", "");
                                        contentValues.put("Config_status", "Not Activated");
                                        db.update("CardSwiperActivation", contentValues, "CardSwiperName = 'ezetap'", null);
                                        Toast.makeText(EZetap_login.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                                        login_TXT_merchantid.setText("");
                                        login_TXT_merchantpassword.setText("");

                                        clear.setVisibility(View.GONE);
                                        login_BTN_signin.setVisibility(View.VISIBLE);

                                        startActivity(new Intent(EZetap_login.this, Card_Wallets_Settings1.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
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
        finish();
    }
}

