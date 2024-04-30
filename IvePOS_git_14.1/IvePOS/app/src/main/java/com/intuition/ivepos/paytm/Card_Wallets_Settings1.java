package com.intuition.ivepos.paytm;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.intuition.ivepos.BeveragesMenuFragment_Dine;
import com.intuition.ivepos.BeveragesMenuFragment_Qsr;
import com.intuition.ivepos.BeveragesMenuFragment_Retail;
import com.intuition.ivepos.EZetap_login;
import com.intuition.ivepos.MainActivity;
import com.intuition.ivepos.R;
//import com.intuition.ivepos.mSwipe.LoginView;
import com.intuition.ivepos.mSwipe.LoginView;
import com.intuition.ivepos.syncapp.StubProviderApp;
import com.pnsol.sdk.interfaces.PaymentTransactionConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

//import com.intuition.ivepos.mSwipe.AppSharedPrefrences;
//import com.intuition.ivepos.mSwipe.ApplicationData;
//import com.intuition.ivepos.mSwipe.CardSaleTransactionView;
//import com.intuition.ivepos.mSwipe.LoginView;
//import com.intuition.ivepos.mSwipe.Logs;


/**
 * Created by intuition on 25-07-2016.
 */
public class Card_Wallets_Settings1 extends Activity implements
        PaymentTransactionConstants {

    private Spinner pairedSpinnerSelectProvider;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private ArrayList<String> list;
    private int devicePosition;
    private static String MAC_ADDRESS = "macAddress";
    private String deviceMACAddress, deviceName, device_MAC_Add;
    private Button sale, rki, prepaid;
    private Set<BluetoothDevice> pairedDevices;
    private String partnerAPIKey = "2016665A802D";
    private Dialog dialog, dialog1;

    Button b1, b2, b3, b4;
    private final int CHANGE_PASSWORD = 1011;
    Intent intent = null;

    private int BLUETOOTH_CODE = 2002;
    private int BLUETOOTH_CODE_ADMIN = 2003;
    private int ACCESS_FINE_LOCATION_CODE = 2004;

    RelativeLayout relativeLayout, relativeLayout1, relativeLayoutpaytm, relativeLayoutmobikwik, relativeLayoutfree;
    EditText etpass, etmkey;
    String mkey, mkey2;
    TextView configured, configured_paytm;
//    Switch aSwitch, aSwitch2;
    ImageView imageView1, imageView2, imageView3;
    TextView textView1, textView2, textView3;
    RelativeLayout configbtn2, configbtn4, configbtn2pine,configbtn3;
    Spinner spinner;
    String[] web = {"Paytm", "Mobikwik!"};
//    Integer[] imageId = {R.drawable.paytm, R.drawable.mobikwik};
    public static final String PACKAGE_NAME = "com.intuition.ivepos";
    public static final String DATABASE_NAME = "mydb_Appdata";
    public static final String TABLE_NAME = "PaynearActivation";
    public SQLiteDatabase db = null;
    Uri contentUri,resultUri;
    private static final File DATA_DIRECTORY_DATABASE =
            new File(Environment.getDataDirectory() + "/data/" + PACKAGE_NAME + "/databases/" + DATABASE_NAME);

    private static final String TAG = "WorkThread";
    private static final String[] BLE_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @RequiresApi(api = Build.VERSION_CODES.S)
    private static final String[] ANDROID_12_BLE_PERMISSIONS = new String[]{
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
    };

    public static void requestBlePermissions(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            ActivityCompat.requestPermissions(activity, ANDROID_12_BLE_PERMISSIONS, requestCode);
        else
            ActivityCompat.requestPermissions(activity, BLE_PERMISSIONS, requestCode);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_cardswiper_setting_layout);

//        db = openOrCreateDatabase("IvePOS_Payment", Context.MODE_PRIVATE, null);
        db = openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);

        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        final Dialog dialog = new Dialog(Card_Wallets_Settings1.this, R.style.timepicker_date_dialog);
        final ImageView notconf = (ImageView) findViewById(R.id.notconfig);
        final ImageView configured = (ImageView) findViewById(R.id.configured);
        final ListView listView = (ListView) findViewById(R.id.list_item);
        ImageView back =(ImageView)findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        relativeLayout = (RelativeLayout) findViewById(R.id.layout);
//
//
//        relativeLayoutpaytm = (RelativeLayout) findViewById(R.id.layout_paytm);
//        relativeLayoutmobikwik = (RelativeLayout) findViewById(R.id.layout_mobikwik);
//        relativeLayoutfree = (RelativeLayout) findViewById(R.id.layout_freecharge);


//        if (Build.VERSION.SDK_INT >= 23) {
//
//            if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, 10001);
//            }
//
//            if (ContextCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE") != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_PHONE_STATE"}, 10001);
//            }
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            ActivityCompat.requestPermissions(this, ANDROID_12_BLE_PERMISSIONS, 9);
        else
            ActivityCompat.requestPermissions(this, BLE_PERMISSIONS, 9);

        test();

    }

    public void test(){
        Cursor payswiff = db.rawQuery("SELECT * FROM CardSwiperActivation", null);
        if (payswiff.moveToFirst()) {

            do{
            String test_id= payswiff.getString(payswiff.getColumnIndex("_id"));
            String test_name= payswiff.getString(payswiff.getColumnIndex("CardSwiperName"));

            Log.e(test_id,test_name);
          }while (payswiff.moveToNext());

        }
    }

    private void initViews() {

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported in this device",
                    Toast.LENGTH_LONG).show();
        } else {
            Intent discoveryIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(discoveryIntent, REQUEST_ENABLE_BT);
        }


        final LinearLayout propLayout = (LinearLayout) findViewById(R.id.linearcard);
        final ImageView notconf = (ImageView) findViewById(R.id.notconfig);
        final ImageView configured = (ImageView) findViewById(R.id.configured);
        final ImageView notconf2 = (ImageView) findViewById(R.id.notconfig2);
        final ImageView configured2 = (ImageView) findViewById(R.id.configured2);

        final ImageView notconfig2pine = (ImageView) findViewById(R.id.notconfig2pine);
        final ImageView configured2pine = (ImageView) findViewById(R.id.configured2pine);

        final ImageView notconf3 = (ImageView) findViewById(R.id.notconfig3);
        final ImageView configured3 = (ImageView) findViewById(R.id.configured3);

        final ImageView notconf4 = (ImageView) findViewById(R.id.notconfig4);
        final ImageView configured4 = (ImageView) findViewById(R.id.configured4);

        Cursor c1 = db.rawQuery("SELECT * FROM CardSwiperActivation WHERE Config_status = 'Activated'", null);
        if (c1.moveToFirst()) {
            do {
                String activated = c1.getString(1);
                if (activated.equals("PaySwiff")) {
                    notconf.setVisibility(View.GONE);
                    configured.setVisibility(View.VISIBLE);

                } else if (activated.equals("mSwipe")) {
                    notconf2.setVisibility(View.GONE);
                    configured2.setVisibility(View.VISIBLE);
                }else if (activated.equals("PineLabs")) {
                    notconfig2pine.setVisibility(View.GONE);
                    configured2pine.setVisibility(View.VISIBLE);
                }else if (activated.equals("Firstdata")) {

                }else if (activated.equals("Paytm")) {
                    notconf3.setVisibility(View.GONE);
                    configured3.setVisibility(View.VISIBLE);
                }else if (activated.equals("ezetap")) {
                    notconf4.setVisibility(View.GONE);
                    configured4.setVisibility(View.VISIBLE);
                }

            }while (c1.moveToNext());
        }
        c1.close();

        RelativeLayout textView = (RelativeLayout) findViewById(R.id.configbtn);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor payswiff = db.rawQuery("SELECT * FROM CardSwiperActivation WHERE CardSwiperName = 'PaySwiff' AND Config_status = 'Activated'", null);
                if (payswiff.moveToFirst()) {
                    Toast.makeText(Card_Wallets_Settings1.this, "Already Activated", Toast.LENGTH_SHORT).show();
                } else {
//
                }payswiff.close();


            }
        });
///////////mSWIPE Setting///////////////
        configbtn2 = (RelativeLayout) findViewById(R.id.configbtn2);
        configbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Cursor payswiff = db.rawQuery("SELECT * FROM CardSwiperActivation WHERE CardSwiperName = 'mSwipe' AND Config_status = 'Activated'", null);
//                if (payswiff.moveToFirst()) {
//                    Toast.makeText(Card_Wallets_Settings1.this, "Already Activated", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Card_Wallets_Settings1.this, LoginView.class);
                    startActivity(intent);
//                } else {
//                    intent = new Intent(Card_Wallets_Settings1.this, LoginView.class);
//                    startActivity(intent);
//                    return;
//                }
//                payswiff.close();


            }


        });



///////////eZetap Setting///////////////
        configbtn4 = (RelativeLayout) findViewById(R.id.configbtn4);
        configbtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Cursor payswiff = db.rawQuery("SELECT * FROM CardSwiperActivation WHERE CardSwiperName = 'mSwipe' AND Config_status = 'Activated'", null);
//                if (payswiff.moveToFirst()) {
//                    Toast.makeText(Card_Wallets_Settings1.this, "Already Activated", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Card_Wallets_Settings1.this, EZetap_login.class);
                    startActivity(intent);
//                } else {
//                    intent = new Intent(Card_Wallets_Settings1.this, LoginView.class);
//                    startActivity(intent);
//                    return;
//                }
//                payswiff.close();


            }


        });





        configbtn2pine = (RelativeLayout) findViewById(R.id.configbtn2pine);
        configbtn2pine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor payswiff = db.rawQuery("SELECT * FROM CardSwiperActivation WHERE CardSwiperName = 'PineLabs' AND Config_status = 'Activated'", null);
                if (payswiff.moveToFirst()) {
                    Toast.makeText(Card_Wallets_Settings1.this, "Already Activated", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(Card_Wallets_Settings1.this)
                            .setTitle("Reset")
                            .setMessage(getString(R.string.setmessage38))

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation

                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("Config_status", "Not Activated");
                                    String where = " _id = 3";
                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
                                    getContentResolver().update(contentUri, contentValues,where,new String[]{});
                                    resultUri = new Uri.Builder()
                                            .scheme("content")
                                            .authority(StubProviderApp.AUTHORITY)
                                            .path("CardSwiperActivation")
                                            .appendQueryParameter("operation", "update")
                                            .appendQueryParameter("_id","3")
                                            .build();
                                    getContentResolver().notifyChange(resultUri, null);
                                    db.update("CardSwiperActivation", contentValues, where, new String[]{});
                                    Toast.makeText(Card_Wallets_Settings1.this, "Deactivated", Toast.LENGTH_SHORT).show();

                                    notconfig2pine.setVisibility(View.VISIBLE);
                                    configured2pine.setVisibility(View.GONE);

                                    dialog.dismiss();

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    dialog.dismiss();
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                } else {

                    new AlertDialog.Builder(Card_Wallets_Settings1.this)
                            .setTitle(getString(R.string.title38))
                            .setMessage(getString(R.string.setmessage39))

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("Config_status", "Not Activated");
                                    String where = " _id = 5";
                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
                                    getContentResolver().update(contentUri, contentValues,where,new String[]{});
                                    resultUri = new Uri.Builder()
                                            .scheme("content")
                                            .authority(StubProviderApp.AUTHORITY)
                                            .path("CardSwiperActivation")
                                            .appendQueryParameter("operation", "update")
                                            .appendQueryParameter("_id","5")
                                            .build();
                                    getContentResolver().notifyChange(resultUri, null);
                                    db.update("CardSwiperActivation", contentValues, where, new String[]{});


                                    // Continue with delete operation

                                    ContentValues contentValues1 = new ContentValues();
                                    contentValues1.put("Config_status", "Activated");
                                    String where1 = " _id = 3";
                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
                                    getContentResolver().update(contentUri, contentValues1,where1,new String[]{});
                                    resultUri = new Uri.Builder()
                                            .scheme("content")
                                            .authority(StubProviderApp.AUTHORITY)
                                            .path("CardSwiperActivation")
                                            .appendQueryParameter("operation", "update")
                                            .appendQueryParameter("_id","3")
                                            .build();
                                    getContentResolver().notifyChange(resultUri, null);
                                    db.update("CardSwiperActivation", contentValues1, where1, new String[]{});
                                    Toast.makeText(Card_Wallets_Settings1.this, "Activated", Toast.LENGTH_SHORT).show();
                                    notconfig2pine.setVisibility(View.GONE);
                                    configured2pine.setVisibility(View.VISIBLE);

                                    dialog.dismiss();

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    dialog.dismiss();
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

//
                }payswiff.close();

            }
        });

        configbtn3 = (RelativeLayout) findViewById(R.id.configbtn3);
        configbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor payswiff = db.rawQuery("SELECT * FROM CardSwiperActivation WHERE CardSwiperName = 'Paytm' AND Config_status = 'Activated'", null);
                if (payswiff.moveToFirst()) {
                    Toast.makeText(Card_Wallets_Settings1.this, "Already Activated", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(Card_Wallets_Settings1.this)
                            .setTitle("Reset")
                            .setMessage(getString(R.string.setmessage40))

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation

                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("Config_status", "Not Activated");
                                    String where = " _id = 5";
                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
                                    getContentResolver().update(contentUri, contentValues,where,new String[]{});
                                    resultUri = new Uri.Builder()
                                            .scheme("content")
                                            .authority(StubProviderApp.AUTHORITY)
                                            .path("CardSwiperActivation")
                                            .appendQueryParameter("operation", "update")
                                            .appendQueryParameter("_id","5")
                                            .build();
                                    getContentResolver().notifyChange(resultUri, null);
                                    db.update("CardSwiperActivation", contentValues, where, new String[]{});
                                    Toast.makeText(Card_Wallets_Settings1.this, "Deactivated", Toast.LENGTH_SHORT).show();

                                    notconf3.setVisibility(View.VISIBLE);
                                    configured3.setVisibility(View.GONE);

                                    dialog.dismiss();

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    dialog.dismiss();
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                } else {

                    new AlertDialog.Builder(Card_Wallets_Settings1.this)
                            .setTitle("Reset")
                            .setMessage(getString(R.string.setmessage41))

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation


                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("Config_status", "Not Activated");
                                    String where = " _id = 3";
                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
                                    getContentResolver().update(contentUri, contentValues,where,new String[]{});
                                    resultUri = new Uri.Builder()
                                            .scheme("content")
                                            .authority(StubProviderApp.AUTHORITY)
                                            .path("CardSwiperActivation")
                                            .appendQueryParameter("operation", "update")
                                            .appendQueryParameter("_id","3")
                                            .build();
                                    getContentResolver().notifyChange(resultUri, null);
                                    db.update("CardSwiperActivation", contentValues, where, new String[]{});


                                    ContentValues contentValues1 = new ContentValues();
                                    contentValues1.put("Config_status", "Activated");
                                    String where1 = " _id = 5";
                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
                                    getContentResolver().update(contentUri, contentValues1,where1,new String[]{});
                                    resultUri = new Uri.Builder()
                                            .scheme("content")
                                            .authority(StubProviderApp.AUTHORITY)
                                            .path("CardSwiperActivation")
                                            .appendQueryParameter("operation", "update")
                                            .appendQueryParameter("_id","5")
                                            .build();
                                    getContentResolver().notifyChange(resultUri, null);
                                    db.update("CardSwiperActivation", contentValues1, where1, new String[]{});
                                    Toast.makeText(Card_Wallets_Settings1.this, "Activated", Toast.LENGTH_SHORT).show();
                                    notconf3.setVisibility(View.GONE);
                                    configured3.setVisibility(View.VISIBLE);

                                    dialog.dismiss();

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    dialog.dismiss();
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

//
                }payswiff.close();

            }
        });



//        configbtn2 = (RelativeLayout) findViewById(R.id.configbtn2);
//        configbtn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!isMerchantAuthenticated()) {
//
//                    final Dialog dialog = Constants.showDialog(Card_Wallets_Settings1.this, "SDK List",
//                            getString(R.string.loginactivity_disclaimer_message),
//                            Constants.CUSTOM_DLG_TYPE.CUSTOM_DLG_TYPE_RETURN_DLG_INFO, "ok", "no");
//
//                    Button yes = (Button) dialog.findViewById(R.id.customdlg_BTN_yes);
//                    yes.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            dialog.dismiss();
//                            intent = new Intent(Card_Wallets_Settings1.this, LoginView.class);
//                            startActivity(intent);
//                            return;
//                        }
//                    });
//
//                    dialog.show();
//
//                } else {
//                    intent = new Intent(Card_Wallets_Settings1.this, LoginView.class);
//                    promptMerchantRelogin();
//
//                }
////                notconf2.setVisibility(View.GONE);
////                configured2.setVisibility(View.VISIBLE);
//
//            }
//        });
    }


    final private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == SUCCESS) {
                final Dialog dialog1 = new Dialog(Card_Wallets_Settings1.this, R.style.timepicker_date_dialog);
                dialog1.setContentView(R.layout.dialog_card_configure_device_selection);
                dialog1.setCancelable(true);
                dialog1.show();

                final TextView notconf = (TextView) findViewById(R.id.notconfig);
                configured = (TextView) findViewById(R.id.configured);
//                configured_paytm = (TextView) findViewById(R.id.configured_paytm);

                Button configure = (Button) dialog1.findViewById(R.id.configure);
                configure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        notconf.setVisibility(View.GONE);
                        configured.setVisibility(View.VISIBLE);
                        configured_paytm.setVisibility(View.VISIBLE);
                        dialog1.dismiss();

                        String hi = pairedSpinnerSelectProvider.getSelectedItem().toString();
                        Toast.makeText(Card_Wallets_Settings1.this, "id is " + etpass.getText().toString(), Toast.LENGTH_LONG).show();
//                        dialog.dismiss();

                    }
                });

                Button recon = (Button) dialog1.findViewById(R.id.goa);
                recon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });

                Button c = (Button) dialog1.findViewById(R.id.close);
                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });


                pairedSpinnerSelectProvider = (Spinner) dialog1.findViewById(R.id.paired_provider);
                list = new ArrayList<String>();
                pairedDevices = mBluetoothAdapter.getBondedDevices();
                for (BluetoothDevice device : pairedDevices) {
                    deviceName = device.getName();

                    if (deviceName.startsWith("Paynear") || deviceName.startsWith("MPOS")) {
                        list.add(deviceName);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                Card_Wallets_Settings1.this, android.R.layout.simple_dropdown_item_1line, list);
                        pairedSpinnerSelectProvider.setAdapter(adapter);
                        pairedSpinnerSelectProvider
                                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(AdapterView<?> arg0,
                                                               View arg1, int position, long arg3) {
                                        devicePosition = position;
                                        device_MAC_Add = (String) pairedSpinnerSelectProvider.getItemAtPosition(devicePosition);
                                        Iterator<BluetoothDevice> i = pairedDevices.iterator();
                                        while (i.hasNext()) {
                                            BluetoothDevice device = i.next();
                                            if (device.getName().equalsIgnoreCase(device_MAC_Add)) {
                                                deviceMACAddress = device.getAddress();
                                            }
                                        }
//                                                    sale.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onNothingSelected(
                                            AdapterView<?> arg0) {

                                    }
                                });
                    }

                }

            }
        }
//
//            if (msg.what == FAIL) {
//                Toast.makeText(CardSettings.this, (String) msg.obj,
//                        Toast.LENGTH_SHORT).show();
//            }

//    };

    };

//    public boolean isMerchantAuthenticated() { //nullified
//
//        if (AppSharedPrefrences.getAppSharedPrefrencesInstace().getReferenceId().length() != 0
//                && AppSharedPrefrences.getAppSharedPrefrencesInstace().getReferenceId().length() != 0) {
//            return true;
//
//        }
//        return false;
//
//    }

//    public void promptMerchantRelogin() {
//
//        final Dialog dialog = Constants.showDialog(Card_Wallets_Settings1.this, "SDK List",
//                "Merchant authenticated, please select \"ok\" to continue or \"no\" to relogin again.",
//                Constants.CUSTOM_DLG_TYPE.CUSTOM_DLG_TYPE_RETURN_DLG_CONFIRMATION, "ok", "no");
//        Button yes = (Button) dialog.findViewById(R.id.customdlg_BTN_yes);
//        yes.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                dialog.dismiss();
//
//                if (!intent.getComponent().getClassName().equals(LoginView.class.getName())) {
//
//                    if (intent.getComponent().getClassName().equals(CardSaleTransactionView.class.getName())) {
//                        showWisePadConnectionDlg();
//
//                    } else {
//                        Card_Wallets_Settings1.this.startActivity(intent);
//                    }
//                }
//            }
//        });
//
//        Button no = (Button) dialog.findViewById(R.id.customdlg_BTN_no);
//        no.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                clearDeviceCache();
//                AppSharedPrefrences.getAppSharedPrefrencesInstace().setReferenceId("");
//                AppSharedPrefrences.getAppSharedPrefrencesInstace().setSessionToken("");
//
//                dialog.dismiss();
//
//            }
//        });
//        dialog.show();
//
//    }

    CheckBox chkLastKnownDevice;
    CheckBox chkSelectDevice;
    CheckBox chkAutoConnect;
    CheckBox chkCheckCard;

    ArrayList<String> pairedDevcieNameList;

//    public void showWisePadConnectionDlg() {
//        final Dialog dialog = Constants.showWisepadConnectionDialog(Card_Wallets_Settings1.this, "Wisepos Settings");
//        chkLastKnownDevice = (CheckBox) dialog.findViewById(R.id.customwisepadconnection_CHK_remembereddevcie);
//        chkSelectDevice = (CheckBox) dialog.findViewById(R.id.customwisepadconnection_CHK_selectdevice);
//        chkAutoConnect = (CheckBox) dialog.findViewById(R.id.customwisepadconnection_CHK_autoconnect);
//        chkCheckCard = (CheckBox) dialog.findViewById(R.id.customwisepadconnection_CHK_checkcard);
//
//        chkAutoConnect.setChecked(true);
//        chkCheckCard.setChecked(true);
//
//        SharedPreferences preferences;
//        preferences = PreferenceManager.getDefaultSharedPreferences(Card_Wallets_Settings1.this);
//        String lastConnectedDevice = preferences.getString("mswipeLastConnectedDeviceClassName", "");
//        if (ApplicationData.IS_DEBUGGING_ON)
//            Logs.v(ApplicationData.packName, "lastConnectedDevice " + lastConnectedDevice, true, true);
//
//        if (lastConnectedDevice.length() > 0) {
//            chkLastKnownDevice.setChecked(true);
//            ((TextView) dialog.findViewById(R.id.customwisepadconnection_LBL_remembereddevcie)).setText(lastConnectedDevice);
//        } else {
//            ((TextView) dialog.findViewById(R.id.customwisepadconnection_LBL_remembereddevcie)).setText("No device selected");
//
//        }
//
//        ((TextView) dialog.findViewById(R.id.customwisepadconnection_LBL_selecteddevice)).setText(("No device selected"));
//
//        chkLastKnownDevice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                // TODO Auto-generated method stub
//
//                if (isChecked) {
//                    chkSelectDevice.setChecked(false);
//                } else {
//                    chkSelectDevice.setChecked(true);
//                }
//            }
//        });
//
//        chkSelectDevice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                // TODO Auto-generated method stub
//
//                if (isChecked) {
//                    chkLastKnownDevice.setChecked(false);
//                } else {
//                    chkLastKnownDevice.setChecked(true);
//                }
//            }
//        });
//
//        Button ok = (Button) dialog.findViewById(R.id.customwisepadconnection_BTN_ok);
//        ok.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                dialog.dismiss();
//                SharedPreferences preferences;
//                preferences = PreferenceManager.getDefaultSharedPreferences(Card_Wallets_Settings1.this);
//
//
//                if (!chkLastKnownDevice.isChecked() && !chkSelectDevice.isChecked()) {
//                    preferences.edit().putString("mswipeLastConnectedDeviceClassName", "").commit();
//
//                } else if (chkLastKnownDevice.isChecked()) {
//
//                    String stDevice = ((TextView) dialog.findViewById(R.id.customwisepadconnection_LBL_remembereddevcie)).getText().toString();
//                    if (stDevice.startsWith("No device selected")) {
//                        preferences.edit().putString("mswipeLastConnectedDeviceClassName", "").commit();
//                    } else {
//                        preferences.edit().putString("mswipeLastConnectedDeviceClassName", stDevice).commit();
//
//                    }
//                } else if (chkSelectDevice.isChecked()) {
//
//                    String stDevice = ((TextView) dialog.findViewById(R.id.customwisepadconnection_LBL_selecteddevice)).getText().toString();
//
//                    if (stDevice.startsWith("No device selected")) {
//                        preferences.edit().putString("mswipeLastConnectedDeviceClassName", "").commit();
//
//                    } else {
//                        preferences.edit().putString("mswipeLastConnectedDeviceClassName", stDevice).commit();
//
//
//                    }
//                }
//                if (ApplicationData.IS_DEBUGGING_ON)
//                    Logs.v(ApplicationData.packName, "********************************************** " + preferences.getString("mswipeLastConnectedDeviceClassName", "") + " ******************************************************", true, true);
//
//                intent.putExtra("autoconnect", chkAutoConnect.isChecked());
//                intent.putExtra("checkcardAfterConnection", chkCheckCard.isChecked());
//                Card_Wallets_Settings1.this.startActivity(intent);
//
//            }
//        });
//
//        Button selectDevice = (Button) dialog.findViewById(R.id.customwisepadconnection_BTN_selectdevice);
//        selectDevice.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//
//                final Dialog devcieDialog = Constants.showAppCustomDialog(Card_Wallets_Settings1.this, "Select paired devices");
//
//                Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
//                pairedDevcieNameList = new ArrayList<String>();
//                for (BluetoothDevice device : pairedDevices) {
//                    pairedDevcieNameList.add(device.getName());
//                }
//
//                ListView deviceListView = (ListView) devcieDialog.findViewById(R.id.customapplicationdlg_LST_applications);
////                deviceListView.setAdapter(new ListAdapter(MainActivity.this, pairedDevcieNameList));
//
//                deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                        ((TextView) dialog.findViewById(R.id.customwisepadconnection_LBL_selecteddevice)).setText(pairedDevcieNameList.get(position));
//                        chkLastKnownDevice.setChecked(false);
//                        chkSelectDevice.setChecked(true);
//                        devcieDialog.dismiss();
//
//                    }
//
//                });
//
//                devcieDialog.findViewById(R.id.customapplicationdlg_BTN_cancel).setOnClickListener(new View.OnClickListener() {
//
//                    //                    @Override
//                    public void onClick(View v) {
//
//
//                        devcieDialog.dismiss();
//                    }
//                });
//                devcieDialog.show();
//
//
//            }
//        });
//
//        dialog.show();
//
//    }


//    public void clearDeviceCache() {
//        AppSharedPrefrences.getAppSharedPrefrencesInstace().setReferenceId("");
//        AppSharedPrefrences.getAppSharedPrefrencesInstace().setSessionToken("");
//
//        deleteCache(this);
//    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        return false;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.loginview, menu);
//        return true;
//    }

//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//            case R.id.loginview_exit: {
//                finish();
//                break;
//            }
//            case R.id.loginview_Help: {
////                new HelpView(this).show();
//                break;
//            }
//
//        }
//        return super.onOptionsItemSelected(item);
//
//    }


//    public class MainActivityAdapter extends BaseAdapter {
//        String[] listData = null;
//        Context context;
//
//        public MainActivityAdapter(Context context, String[] listData) {
//            this.listData = listData;
//            this.context = context;
//        }
//
//        @Override
//        public int getCount() {
//            // TODO Auto-generated method stub
//            return listData.length;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            // TODO Auto-generated method stub
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            // TODO Auto-generated method stub
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            // TODO Auto-generated method stub
//            if (convertView == null) {
//                LayoutInflater inflater = LayoutInflater.from(context);
//                convertView = inflater.inflate(R.layout.view_menulstitem, null);
//            }
//            TextView txtItem = (TextView) convertView
//                    .findViewById(R.id.menuview_lsttext);
//            txtItem.setText(listData[position]);
//
//            return convertView;
//        }
//    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (ApplicationData.IS_DEBUGGING_ON)
//            Logs.v(ApplicationData.packName, "onActivityResult ", true, true);
//
//
//        if (requestCode == 0) {
//            if (resultCode == RESULT_OK) {
//                this.finish();
//                startActivity(this.getIntent());
//            }
//        } else if (requestCode == CHANGE_PASSWORD) {
//            if (resultCode == RESULT_OK) {
//
//                clearDeviceCache();
//                Intent intent = new Intent(Card_Wallets_Settings1.this, LoginView.class);
//                startActivity(intent);
//            }
//        }
//    }


////    @Override
////    public void onClick(View v) {
////        switch (v.getId())
////        {
////            case (R.id.text_paytm):
////            {
////
////                Toast.makeText(this, "Paytm", Toast.LENGTH_SHORT).show();
////                checkBox1.setChecked(!checkBox1.isChecked());
////                checkBox2.setChecked(!checkBox2.isChecked());
////
////                break;
////            }
////            case (R.id.text_mobi):
////            {
////                Toast.makeText(this, "Mobikwik!", Toast.LENGTH_SHORT).show();
////                checkBox2.setChecked(!checkBox2.isChecked());
////                break;
////            }
////            case (R.id.text_freecharge):
////            {
////                Toast.makeText(this, "Free Charge", Toast.LENGTH_SHORT).show();
////                checkBox3.setChecked(!checkBox3.isChecked());
////                break;
////            }
////
////        }
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 9: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    initViews();

                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                    if (mBluetoothAdapter == null){

                    }else {
                        if (null == mBluetoothAdapter) {
                            finish();

                        }

                        if (!mBluetoothAdapter.isEnabled()) {
                            if (mBluetoothAdapter.enable()) {
                                while (!mBluetoothAdapter.isEnabled())
                                    ;
                                Log.v(TAG, "Enable BluetoothAdapter");
                            } else {
                                finish();

                            }
                        }

                        mBluetoothAdapter.cancelDiscovery();
                        mBluetoothAdapter.startDiscovery();

                    }

                } else {
                    Toast.makeText(Card_Wallets_Settings1.this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
