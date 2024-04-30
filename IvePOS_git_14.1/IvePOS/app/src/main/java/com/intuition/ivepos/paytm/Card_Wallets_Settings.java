package com.intuition.ivepos.paytm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.intuition.ivepos.EzeTapSetting;
import com.intuition.ivepos.R;
import com.intuition.ivepos.razorpay.RazorPaySetting;
import com.pnsol.sdk.interfaces.PaymentTransactionConstants;

import java.io.File;
/**
 * Created by intuition on 25-07-2016.
 */
public class Card_Wallets_Settings extends Activity implements
        PaymentTransactionConstants {

    RelativeLayout relativeLayout, relativeLayout1, relativeLayoutpaytm, relativeLayoutmobikwik, relativeLayoutfree;
    EditText etpass, etmkey;
    String mkey, mkey2;
    TextView configured, configured_paytm;
    RelativeLayout configbtn2, configbtn2_razor, configbtn2_ezetap;
    Intent intent;
    Spinner spinner;
    String[] web = {"Paytm", "Mobikwik!"};
//    Integer[] imageId = {R.drawable.paytm, R.drawable.mobikwik};
    public static final String PACKAGE_NAME = "com.intuition.ivepos.paytm";
    public static final String DATABASE_NAME = "mydb_Appdata";
    public static final String TABLE_NAME = "PaynearActivation";
    public SQLiteDatabase db = null;
    private static final File DATA_DIRECTORY_DATABASE =
            new File(Environment.getDataDirectory() + "/data/" + PACKAGE_NAME + "/databases/" + DATABASE_NAME);

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_wallet_settings);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        final Dialog dialog = new Dialog(Card_Wallets_Settings.this, R.style.timepicker_date_dialog);
        final ImageView notconf = (ImageView) findViewById(R.id.notconfig);
        final ImageView configured = (ImageView) findViewById(R.id.configured);
        final ListView listView = (ListView) findViewById(R.id.list_item);
        ImageView back =(ImageView)findViewById(R.id.back);

        initViews();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initViews() {


        final LinearLayout propLayout = (LinearLayout) findViewById(R.id.linearcard);
        final ImageView notconfig = (ImageView) findViewById(R.id.notconfig);
        final ImageView notconfig_razor = (ImageView) findViewById(R.id.notconfig_razor);
        final ImageView configured = (ImageView) findViewById(R.id.configured);
        final ImageView configured_razor = (ImageView) findViewById(R.id.configured_razor);
        final ImageView notconf2 = (ImageView) findViewById(R.id.notconf2);
        final ImageView configured2 = (ImageView) findViewById(R.id.configured2);


        final ImageView notconfig_ezetap = (ImageView) findViewById(R.id.notconfig_ezetap);
        final ImageView configured_ezetap = (ImageView) findViewById(R.id.configured_ezetap);

//        Cursor c1 = db.rawQuery("SELECT * FROM PaytmMerchantReg WHERE Account = 'Not_Registered'", null);
        Cursor c1 = db.rawQuery("SELECT * FROM PaytmMerchantReg", null);
        if (c1.moveToFirst()) {
            do {
                String activated = c1.getString(1);
                if (activated.equals("Registered")) {
                    configured.setVisibility(View.VISIBLE);
                    notconfig.setVisibility(View.GONE);
                }
//                else{
//                    notconfig.setVisibility(View.VISIBLE);
//                    configured.setVisibility(View.GONE);
//                }

            }while (c1.moveToNext());
        }
        c1.close();

        Cursor c1_1 = db.rawQuery("SELECT * FROM RazorpayMerchantReg", null);
        if (c1_1.moveToFirst()) {
            do {
                String activated = c1_1.getString(1);
                if (activated.equals("Registered")) {
                    configured_razor.setVisibility(View.VISIBLE);
                    notconfig_razor.setVisibility(View.GONE);
                }
//                else{
//                    notconfig_razor.setVisibility(View.VISIBLE);
//                    configured.setVisibility(View.GONE);
//                }

            }while (c1_1.moveToNext());
        }
        c1_1.close();

        Cursor c1_2 = db.rawQuery("SELECT * FROM EzetapMerchantReg", null);
        if (c1_2.moveToFirst()) {
            do {
                String activated = c1_2.getString(1);
                if (activated.equals("Registered")) {
                    configured_ezetap.setVisibility(View.VISIBLE);
                    notconfig_ezetap.setVisibility(View.GONE);
                }
//                else{
//                    notconfig_razor.setVisibility(View.VISIBLE);
//                    configured.setVisibility(View.GONE);
//                }

            }while (c1_2.moveToNext());
        }
        c1_2.close();

//        Cursor c2 = db.rawQuery("SELECT * FROM MobikwikMerchantReg WHERE Account = 'Registered'", null);
        Cursor c2 = db.rawQuery("SELECT * FROM MobikwikMerchantReg", null);
        if (c2.moveToFirst()) {
            do {
                String activated = c2.getString(1);
                if (activated.equals("Registered")) {
                    configured2.setVisibility(View.VISIBLE);
                    notconf2.setVisibility(View.GONE);
                }
//                else{
//                    configured2.setVisibility(View.GONE);
//                    notconf2.setVisibility(View.VISIBLE);
//                }

            }while (c2.moveToNext());
        }
        c2.close();


        configbtn2 = (RelativeLayout) findViewById(R.id.configbtn2);
        configbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor cursor = db.rawQuery("SELECT * FROM RazorpayMerchantReg WHERE account = 'Registered'", null);
                if (cursor.moveToFirst()) {
                    new AlertDialog.Builder(Card_Wallets_Settings.this)
                            .setTitle(getString(R.string.title36))
                            .setMessage(getString(R.string.setmessage36))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })

                            .show();
                }else {
                    Cursor cursor2 = db.rawQuery("SELECT * FROM EzetapMerchantReg WHERE account = 'Registered'", null);
                    if (cursor2.moveToFirst()) {
                        new AlertDialog.Builder(Card_Wallets_Settings.this)
                                .setTitle("Alert")
                                .setMessage("EzeTap is already registered. Reset EzeTap for activating Paytm")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })

                                .show();
                    }else {
                        intent = new Intent(Card_Wallets_Settings.this, PaytmSetting.class);
                        startActivity(intent);
                    }
                }
                cursor.close();


            }
        });

        configbtn2_razor = (RelativeLayout) findViewById(R.id.configbtn2_razor);
        configbtn2_razor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor cursor1 = db.rawQuery("SELECT * FROM PaytmMerchantReg WHERE Account = 'Registered'", null);
                if (cursor1.moveToFirst()) {
                    new AlertDialog.Builder(Card_Wallets_Settings.this)
                            .setTitle(getString(R.string.title36))
                            .setMessage(getString(R.string.setmessage37))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })

                            .show();
                }else {
                    Cursor cursor2 = db.rawQuery("SELECT * FROM EzetapMerchantReg WHERE account = 'Registered'", null);
                    if (cursor2.moveToFirst()) {
                        new AlertDialog.Builder(Card_Wallets_Settings.this)
                                .setTitle("Alert")
                                .setMessage("EzeTap is already registered. Reset EzeTap for activating Paytm")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })

                                .show();
                    }else {
                        intent = new Intent(Card_Wallets_Settings.this, RazorPaySetting.class);
                        startActivity(intent);
                    }
                }
                cursor1.close();


            }
        });

        configbtn2_ezetap = (RelativeLayout) findViewById(R.id.configbtn2_ezetap);
        configbtn2_ezetap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor cursor = db.rawQuery("SELECT * FROM RazorpayMerchantReg WHERE account = 'Registered'", null);
                if (cursor.moveToFirst()) {
                    new AlertDialog.Builder(Card_Wallets_Settings.this)
                            .setTitle("Alert")
                            .setMessage("Razorpay is already registered. Reset Razorpay for activating Paytm")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })

                            .show();
                }else {
                    Cursor cursor1 = db.rawQuery("SELECT * FROM PaytmMerchantReg WHERE Account = 'Registered'", null);
                    if (cursor1.moveToFirst()) {
                        new AlertDialog.Builder(Card_Wallets_Settings.this)
                                .setTitle("Alert")
                                .setMessage("Paytm is already registered. Reset Paytm for activating Razorpay")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })

                                .show();
                    } else {
                        intent = new Intent(Card_Wallets_Settings.this, EzeTapSetting.class);
                        startActivity(intent);
                    }
                }
                cursor.close();


            }
        });

        RelativeLayout textView = (RelativeLayout) findViewById(R.id.configbtn);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Card_Wallets_Settings.this, MobikwikSetting.class);
                startActivity(intent);
            }
        });
    }
}
