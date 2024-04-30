package com.intuition.ivepos;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Rohithkumar on 5/29/2015.
 */
public class UniversalPassRetrievalActivity extends AppCompatActivity {

    Dialog dialog;
    TextView madminusername, madminpassword, ladminusername, ladminpassword, useroneusername, useronepassword, usertwousername, usertwopassword;
    TextView userthreeusername, userthreepassword, userfourusername, userfourpassword, userfiveusername, userfivepassword, usersixusername, usersixpassword;
    private Cursor c;
    public SQLiteDatabase db = null;

    String account_selection;
    String WebserviceUrl;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universalallpasswords2);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(UniversalPassRetrievalActivity.this);
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

        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {

                finish();
                // If you want to call Activity then call from here for 5 seconds it automatically call and your image disappear....
            }
        }, 60000);


        ImageView arrow = (ImageView) findViewById(R.id.leftarrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UniversalPassRetrievalActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

//        ImageView closelayout = (ImageView)findViewById(R.id.close);
//        closelayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UniversalPassRetrievalActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });

        RelativeLayout masterpass = (RelativeLayout) findViewById(R.id.masterpassretrieval);
        masterpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(UniversalPassRetrievalActivity.this, R.style.cust_dialog);
                dialog.setContentView(R.layout.credevtials_retireval);
                dialog.setTitle(Html.fromHtml("Master admin"));

                madminusername = (TextView)dialog.findViewById(R.id.username);
                db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                Cursor allrows = db.rawQuery("SELECT * FROM LOGIN WHERE ID = '1'", null);
                if (allrows.moveToFirst()) {
                    do {
                        String NAME = allrows.getString(1);
                        madminusername.setText(NAME);

                    } while (allrows.moveToNext());
                }
                allrows.close();

                madminpassword = (TextView)dialog.findViewById(R.id.etPass);
                db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                allrows = db.rawQuery("SELECT * FROM LOGIN WHERE ID = '1'", null);
                if (allrows.moveToFirst()) {
                    do {
                        String NAME = allrows.getString(2);
                        madminpassword.setText(NAME);

                    } while (allrows.moveToNext());
                }
                allrows.close();

                Button ok = (Button)dialog.findViewById(R.id.close);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        RelativeLayout ladminpass = (RelativeLayout)findViewById(R.id.ladminpassretrieval);
        ladminpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(UniversalPassRetrievalActivity.this, R.style.cust_dialog);
                dialog.setContentView(R.layout.credevtials_retireval);
                dialog.setTitle(Html.fromHtml("Local admin"));

                ladminusername = (TextView)dialog.findViewById(R.id.username);
                db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                Cursor allrows = db.rawQuery("SELECT * FROM LAdmin WHERE _id = '1'", null);
                if (allrows.moveToFirst()) {
                    do {
                        String NAME = allrows.getString(1);
                        ladminusername.setText(NAME);

                    } while (allrows.moveToNext());
                }
                allrows.close();

                ladminpassword = (TextView)dialog.findViewById(R.id.etPass);
                db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                allrows = db.rawQuery("SELECT * FROM LAdmin WHERE _id = '1'", null);
                if (allrows.moveToFirst()) {
                    do {
                        String NAME = allrows.getString(2);
                        ladminpassword.setText(NAME);

                    } while (allrows.moveToNext());
                }
                allrows.close();

                Button ok = (Button)dialog.findViewById(R.id.close);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        RelativeLayout user1pass = (RelativeLayout)findViewById(R.id.userpassretrieval);
        user1pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(UniversalPassRetrievalActivity.this, R.style.cust_dialog);
                dialog.setContentView(R.layout.credevtials_retireval);
                dialog.setTitle(Html.fromHtml("User 1"));

                useroneusername = (TextView)dialog.findViewById(R.id.username);
                db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                Cursor allrows = db.rawQuery("SELECT * FROM User1 WHERE _id = '1'", null);
                if (allrows.moveToFirst()) {
                    do {
                        String NAME = allrows.getString(2);
                        useroneusername.setText(NAME);

                    } while (allrows.moveToNext());
                }
                allrows.close();

                useronepassword = (TextView)dialog.findViewById(R.id.etPass);
                db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                allrows = db.rawQuery("SELECT * FROM User1 WHERE _id = '1'", null);
                if (allrows.moveToFirst()) {
                    do {
                        String NAME = allrows.getString(3);
                        useronepassword.setText(NAME);

                    } while (allrows.moveToNext());
                }
                allrows.close();

                Button ok = (Button)dialog.findViewById(R.id.close);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        RelativeLayout user2pass = (RelativeLayout)findViewById(R.id.usertwopassretrieval);
        user2pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(UniversalPassRetrievalActivity.this, R.style.cust_dialog);
                dialog.setContentView(R.layout.credevtials_retireval);
                dialog.setTitle(Html.fromHtml("User 2"));

                usertwousername = (TextView)dialog.findViewById(R.id.username);
                db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                Cursor allrows = db.rawQuery("SELECT * FROM User2 WHERE _id = '1'", null);
                if (allrows.moveToFirst()) {
                    do {
                        String NAME = allrows.getString(2);
                        usertwousername.setText(NAME);

                    } while (allrows.moveToNext());
                }
                allrows.close();

                usertwopassword = (TextView)dialog.findViewById(R.id.etPass);
                db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                allrows = db.rawQuery("SELECT * FROM User2 WHERE _id = '1'", null);
                if (allrows.moveToFirst()) {
                    do {
                        String NAME = allrows.getString(3);
                        usertwopassword.setText(NAME);

                    } while (allrows.moveToNext());
                }
                allrows.close();

                Button ok = (Button)dialog.findViewById(R.id.close);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        RelativeLayout user3pass = (RelativeLayout)findViewById(R.id.userthreepassretrieval);
        user3pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(UniversalPassRetrievalActivity.this, R.style.cust_dialog);
                dialog.setContentView(R.layout.credevtials_retireval);
                dialog.setTitle(Html.fromHtml("User 3"));

                userthreeusername = (TextView)dialog.findViewById(R.id.username);
                db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                Cursor allrows = db.rawQuery("SELECT * FROM User3 WHERE _id = '1'", null);
                if (allrows.moveToFirst()) {
                    do {
                        String NAME = allrows.getString(2);
                        userthreeusername.setText(NAME);

                    } while (allrows.moveToNext());
                }
                allrows.close();

                userthreepassword = (TextView)dialog.findViewById(R.id.etPass);
                db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                allrows = db.rawQuery("SELECT * FROM User3 WHERE _id = '1'", null);
                if (allrows.moveToFirst()) {
                    do {
                        String NAME = allrows.getString(3);
                        userthreepassword.setText(NAME);

                    } while (allrows.moveToNext());
                }
                allrows.close();

                Button ok = (Button)dialog.findViewById(R.id.close);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        RelativeLayout user4pass = (RelativeLayout)findViewById(R.id.userfourpassretrieval);
        user4pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(UniversalPassRetrievalActivity.this, R.style.cust_dialog);
                dialog.setContentView(R.layout.credevtials_retireval);
                dialog.setTitle(Html.fromHtml("User 4"));

                userfourusername = (TextView)dialog.findViewById(R.id.username);
                db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                Cursor allrows = db.rawQuery("SELECT * FROM User4 WHERE _id = '1'", null);
                if (allrows.moveToFirst()) {
                    do {
                        String NAME = allrows.getString(2);
                        userfourusername.setText(NAME);

                    } while (allrows.moveToNext());
                }
                allrows.close();

                userfourpassword = (TextView)dialog.findViewById(R.id.etPass);
                db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                allrows = db.rawQuery("SELECT * FROM User4 WHERE _id = '1'", null);
                if (allrows.moveToFirst()) {
                    do {
                        String NAME = allrows.getString(3);
                        userfourpassword.setText(NAME);

                    } while (allrows.moveToNext());
                }
                allrows.close();

                Button ok = (Button)dialog.findViewById(R.id.close);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        RelativeLayout user5pass = (RelativeLayout)findViewById(R.id.userfivepassretrieval);
        user5pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(UniversalPassRetrievalActivity.this, R.style.cust_dialog);
                dialog.setContentView(R.layout.credevtials_retireval);
                dialog.setTitle(Html.fromHtml("User 5"));

                userfiveusername = (TextView)dialog.findViewById(R.id.username);
                db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                Cursor allrows = db.rawQuery("SELECT * FROM User5 WHERE _id = '1'", null);
                if (allrows.moveToFirst()) {
                    do {
                        String NAME = allrows.getString(2);
                        userfiveusername.setText(NAME);

                    } while (allrows.moveToNext());
                }
                allrows.close();

                userfivepassword = (TextView)dialog.findViewById(R.id.etPass);
                db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                allrows = db.rawQuery("SELECT * FROM User5 WHERE _id = '1'", null);
                if (allrows.moveToFirst()) {
                    do {
                        String NAME = allrows.getString(3);
                        userfivepassword.setText(NAME);

                    } while (allrows.moveToNext());
                }
                allrows.close();

                Button ok = (Button)dialog.findViewById(R.id.close);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        RelativeLayout user6pass = (RelativeLayout)findViewById(R.id.usersixpassretrieval);
        user6pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(UniversalPassRetrievalActivity.this, R.style.cust_dialog);
                dialog.setContentView(R.layout.credevtials_retireval);
                dialog.setTitle(Html.fromHtml("User 6"));

                usersixusername = (TextView)dialog.findViewById(R.id.username);
                db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                Cursor allrows = db.rawQuery("SELECT * FROM User6 WHERE _id = '1'", null);
                if (allrows.moveToFirst()) {
                    do {
                        String NAME = allrows.getString(2);
                        usersixusername.setText(NAME);

                    } while (allrows.moveToNext());
                }
                allrows.close();

                usersixpassword = (TextView)dialog.findViewById(R.id.etPass);
                db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                allrows = db.rawQuery("SELECT * FROM User6 WHERE _id = '1'", null);
                if (allrows.moveToFirst()) {
                    do {
                        String NAME = allrows.getString(3);
                        usersixpassword.setText(NAME);

                    } while (allrows.moveToNext());
                }
                allrows.close();

                Button ok = (Button)dialog.findViewById(R.id.close);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        Button gotohome = (Button)findViewById(R.id.gotomainpage);
        gotohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(UniversalPassRetrievalActivity.this, MainActivity.class);
//                startActivity(intent);

                if (account_selection.toString().equals("Dine") || account_selection.toString().equals(getString(R.string.app_name))) {
                    Intent intent = new Intent(UniversalPassRetrievalActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("from","home");
                    startActivity(intent);
                }else {
                    if (account_selection.toString().equals("Qsr")) {
                        Intent intent = new Intent(UniversalPassRetrievalActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("from","home");
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(UniversalPassRetrievalActivity.this, MainActivity_Retail.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("from","home");
                        startActivity(intent);
                    }
                }

//                Intent intent = new Intent(UniversalPassRetrievalActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("from","home");
//                startActivity(intent);
            }
        });
    }
}
