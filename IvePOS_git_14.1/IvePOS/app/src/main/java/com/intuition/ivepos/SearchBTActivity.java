package com.intuition.ivepos;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.epson.epos2.discovery.DeviceInfo;
import com.epson.epos2.discovery.Discovery;
import com.epson.epos2.discovery.DiscoveryListener;
import com.epson.epos2.discovery.FilterOption;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;

import static com.intuition.ivepos.R.id.Target;
import static com.intuition.ivepos.R.id.usbDevice;


public class SearchBTActivity extends AppCompatActivity implements OnClickListener {

    private static final String USB_PERMISSION_STRING = "com.intuition.ivepos.SearchBTActivity.20140701";

    private LinearLayout linearlayoutdevices, linearlayoutdevices1, linearlayoutdevices11, linearLayoutUSBDevices;
    TextView button1;
    private ProgressBar progressBarSearchStatus;
    private ProgressDialog dialog;

    private BroadcastReceiver broadcastReceiver = null;
    private IntentFilter intentFilter = null;

//    private static Handler mHandler = null;
    private static String TAG = "SearchBTActivity";


    private static ListView listView;
    public static final String ICON = "ICON";
    public static final String PRINTERNAME = "PRINTERNAME";
    public static final String PRINTERMAC = "PRINTERMAC";
    private static List<Map<String, Object>> boundedPrinters;


    private SharedPreferences mSp;

    String deviceBTName, deviceaddress;
    ScrollView scrollView;
    TextView textView;

    public SQLiteDatabase db = null;
    String addget, nameget, statusget, deviceget;
    int vendorid, productid;

    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothPrintDriver mChatService = null;

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    private String mConnectedDeviceName = null;

    Dialog dialogas;

    byte[][] allbufqty;
    byte[] setHT34;


    private Context mContext = null;
    private ArrayList<HashMap<String, String>> mPrinterList = null;
    private SimpleAdapter mPrinterListAdapter = null;
    private FilterOption mFilterOption = null;

    TextView deviceInfo;
    private UsbDevice mDevice;
    private UsbManager mUsbManager;
    private PendingIntent mPermissionIntent;
    private UsbDeviceConnection mConnection;
    private UsbInterface mInterface;
    private UsbEndpoint mEndPoint;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    HashMap<String, UsbDevice> mDeviceList;
    Iterator<UsbDevice> mDeviceIterator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_devices_all);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title30));

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        deviceInfo = findViewById(R.id.usbDevice);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(mUsbReceiver, filter, RECEIVER_EXPORTED);
        }else {
            registerReceiver(mUsbReceiver, filter);
        }

//        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            mPermissionIntent = PendingIntent.getBroadcast(SearchBTActivity.this, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_MUTABLE);
        }
        else {
            mPermissionIntent = PendingIntent.getBroadcast(SearchBTActivity.this, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_ONE_SHOT);
        }

        mUsbManager = (UsbManager) getSystemService(this.USB_SERVICE);
        mDeviceList = mUsbManager.getDeviceList();
        if (mDeviceList.size() > 0) {
            mDeviceIterator = mDeviceList.values().iterator();
            String usbDevice = "";
            while (mDeviceIterator.hasNext()) {
                UsbDevice usbDevice1 = mDeviceIterator.next();
                mDevice = usbDevice1;
                usbDevice += "\n" +
//                        "DeviceID: " + usbDevice1.getDeviceId() + "\n" +
                        "DeviceName: " + usbDevice1.getDeviceName() + "\n"+
//                        "Protocol: " + usbDevice1.getDeviceProtocol() + "\n" +
//                        //"Product Name: " + usbDevice1.getProductName() + "\n" +
//                        //"Manufacturer Name: " + usbDevice1.getManufacturerName() + "\n" +
                        "DeviceClass: " + usbDevice1.getDeviceClass() + " - " + translateDeviceClass(usbDevice1.getDeviceClass()) + "\n" ;
//                        "DeviceSubClass: " + usbDevice1.getDeviceSubclass() + "\n" +
//                        "VendorID: " + usbDevice1.getVendorId() + "\n" +
//                        "ProductID: " + usbDevice1.getProductId() + "\n";

                System.out.println("device is "+usbDevice1.getDeviceName());
            }
            deviceInfo.setText(usbDevice);
            mUsbManager.requestPermission(mDevice, mPermissionIntent);
        }


        linearLayoutUSBDevices = (LinearLayout) findViewById(R.id.linearLayoutUSBDevices);

        progressBarSearchStatus = (ProgressBar) findViewById(R.id.progressBarSearchStatus);
        linearlayoutdevices = (LinearLayout) findViewById(R.id.linearlayoutdevices);
        linearlayoutdevices1 = (LinearLayout) findViewById(R.id.linearlayoutdevices1);
        linearlayoutdevices11 = (LinearLayout) findViewById(R.id.linearlayoutdevices11);
        scrollView = (ScrollView) findViewById(R.id.scrollView1);
        textView = (TextView) findViewById(R.id.available);
        dialog = new ProgressDialog(this, R.style.timepicker_date_dialog);
        progressBarSearchStatus.setVisibility(View.GONE);
        mSp = PreferenceManager.getDefaultSharedPreferences(this);


        setupChat();


        Cursor conn = db.rawQuery("SELECT * FROM BTConn", null);
        if (conn.moveToFirst()){
            nameget = conn.getString(1);
            addget = conn.getString(2);
            statusget = conn.getString(3);
            deviceget = conn.getString(4);
            if (statusget.toString().equals("ok")){
                if (deviceget.toString().equals("bluetooth")){
                    LinearLayout linearLayout = new LinearLayout(SearchBTActivity.this);
                    linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    ImageButton imageButton = new ImageButton(SearchBTActivity.this);
                    imageButton.setBackgroundColor(getResources().getColor(R.color.white));
                    imageButton.setImageResource(R.drawable.ic_bluetooth_black_24dp);
                    TextView button = new TextView(SearchBTActivity.this);
                    button.setClickable(true);
                    button.setGravity(android.view.Gravity.CENTER_VERTICAL
                            | Gravity.LEFT);
                    //button.setCompoundDrawables(null, null, drawableTop, null);


                    //if (nameget != null && addget != null && statusget != null && nameget.toString().equals(deviceBTName) && addget.equals(deviceaddress) && statusget.equals("ok")){
                    button.setText(nameget + ": " + addget);
                    button.setGravity(android.view.Gravity.CENTER_VERTICAL
                            | Gravity.LEFT);
                    button1 = new TextView(SearchBTActivity.this);
                    button1.setText("   Connected");
                    button1.setVisibility(View.VISIBLE);
                    button1.setBackgroundColor(getResources().getColor(R.color.white));
                    //linearLayout.addView(button1);
                    //Toast.makeText(SearchBTActivity.this, " 333 "+nameget, Toast.LENGTH_SHORT).show();
                    //}

//                button1 = new TextView(SearchBTActivity.this);
//                button1.setText("Connected");
//                button1.setVisibility(View.INVISIBLE);
//                Toast.makeText(SearchBTActivity.this, " 333 "+deviceBTName, Toast.LENGTH_SHORT).show();

                    linearLayout.addView(imageButton);
                    linearLayout.addView(button);
                    linearLayout.addView(button1);
                    linearlayoutdevices11.addView(linearLayout);
                    deviceBTName = nameget;
                    deviceaddress = addget;
                    //}
                }else {
                    LinearLayout linearLayout = new LinearLayout(SearchBTActivity.this);
                    linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    ImageButton imageButton = new ImageButton(SearchBTActivity.this);
                    imageButton.setBackgroundColor(getResources().getColor(R.color.white));
                    imageButton.setImageResource(R.drawable.ic_usb_black_24dp);
                    TextView button = new TextView(SearchBTActivity.this);
                    button.setClickable(true);
                    button.setGravity(android.view.Gravity.CENTER_VERTICAL
                            | Gravity.LEFT);
                    //button.setCompoundDrawables(null, null, drawableTop, null);


                    //if (nameget != null && addget != null && statusget != null && nameget.toString().equals(deviceBTName) && addget.equals(deviceaddress) && statusget.equals("ok")){
                    //button.setText(nameget + ": " + addget);
                    button.setText(String.valueOf(nameget)+": "+String.valueOf(addget));
                    button.setGravity(android.view.Gravity.CENTER_VERTICAL
                            | Gravity.LEFT);
                    button1 = new TextView(SearchBTActivity.this);
                    button1.setText("   Connected");
                    button1.setVisibility(View.VISIBLE);
                    button1.setBackgroundColor(getResources().getColor(R.color.white));
                    //linearLayout.addView(button1);
                    //Toast.makeText(SearchBTActivity.this, " 333 "+nameget, Toast.LENGTH_SHORT).show();
                    //}

//                button1 = new TextView(SearchBTActivity.this);
//                button1.setText("Connected");
//                button1.setVisibility(View.INVISIBLE);
//                Toast.makeText(SearchBTActivity.this, " 333 "+deviceBTName, Toast.LENGTH_SHORT).show();

                    linearLayout.addView(imageButton);
                    linearLayout.addView(button);
                    linearLayout.addView(button1);
                    linearlayoutdevices11.addView(linearLayout);
                    //}
                }
                //if (nameget != null && addget != null && statusget != null && nameget.toString().equals(deviceBTName) && addget.equals(deviceaddress) && statusget.equals("ok")){

            }

            //DrawerService.workThread.connectBt(addget);
        }else {

        }
        conn.close();


        Button gonetwork = (Button)findViewById(R.id.gotonetwork);
        gonetwork.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

//                byte[] setHT34 = new byte[]{0x1b,0x44,0x06,0x12,0x19,0x00};//4 tabs 3"
//                BluetoothPrintDriver.BT_Write(new byte[]{0x1b,0x44,0x06,0x19,0x21,0x00});	//BT_Write(new byte[]{0x1d,0x21,0x00},3);	////
////			BluetoothPrintDriver.SetAlignMode((byte)0);//left
////			BluetoothPrintDriver.SetAlignMode((byte)1);//center
////			BluetoothPrintDriver.SetAlignMode((byte)2);//right
////                String tmpContent = mPrintContent.getText().toString();
//                BT_Write("Qty");
//                byte[] HT = new byte[]{0x09};//HT
//                BluetoothPrintDriver.BT_Write(HT);	//
//
//                BT_Write("Item");
//                BluetoothPrintDriver.BT_Write(HT);	//
//
//                BT_Write("Price");
//                BluetoothPrintDriver.BT_Write(HT);	//
//
//                BT_Write("Amount");
//                BluetoothPrintDriver.BT_Write(new byte[]{0x0d,0x0a});
//
//
//                BluetoothPrintDriver.BT_Write(new byte[]{0x1b,0x44,0x23,0x00});	//BT_Write(new byte[]{0x1d,0x21,0x00},3);	////
////			BluetoothPrintDriver.SetAlignMode((byte)0);//left
////			BluetoothPrintDriver.SetAlignMode((byte)1);//center
////			BluetoothPrintDriver.SetAlignMode((byte)2);//right
//                BT_Write("Qty");
//                BluetoothPrintDriver.BT_Write(HT);	//
//
//                BT_Write("Item");
//                BluetoothPrintDriver.BT_Write(new byte[]{0x0d,0x0a});
//
//                //////////////////////////////////////////////////////////////////////////////////////////////////
//
////                byte[] setHT34 = new byte[]{0x1b,0x44,0x06,0x12,0x19,0x00};//4 tabs 3"
//                BluetoothPrintDriver.BT_Write(new byte[]{0x1b,0x44,0x06,0x12,0x19,0x00});	//BT_Write(new byte[]{0x1d,0x21,0x00},3);	////
////			BluetoothPrintDriver.SetAlignMode((byte)0);//left
////			BluetoothPrintDriver.SetAlignMode((byte)1);//center
////			BluetoothPrintDriver.SetAlignMode((byte)2);//right
////                String tmpContent = mPrintContent.getText().toString();
//                BT_Write("Qty");
////                byte[] HT = new byte[]{0x09};//HT
//                BluetoothPrintDriver.BT_Write(HT);	//
//
//                BT_Write("Item");
//                BluetoothPrintDriver.BT_Write(HT);	//
//
//                BT_Write("Price");
//                BluetoothPrintDriver.BT_Write(HT);	//
//
//                BT_Write("Amount");
//                BluetoothPrintDriver.BT_Write(new byte[]{0x0d,0x0a});
//
//
//                BluetoothPrintDriver.BT_Write(new byte[]{0x1b,0x44,0x19,0x00});	//BT_Write(new byte[]{0x1d,0x21,0x00},3);	////
////			BluetoothPrintDriver.SetAlignMode((byte)0);//left
////			BluetoothPrintDriver.SetAlignMode((byte)1);//center
////			BluetoothPrintDriver.SetAlignMode((byte)2);//right
//                BT_Write("Qty");
//                BluetoothPrintDriver.BT_Write(HT);	//
//
//                BT_Write("Item");
//                BluetoothPrintDriver.BT_Write(new byte[]{0x0d,0x0a});

                Intent intent = new Intent(SearchBTActivity.this, SearchIPActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                LoginActivity.this.finish();
                startActivity(intent);

            }
        });


        //IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);

        //this.registerReceiver(broadcastReceiver, filter1);

//        boundedPrinters = getBoundedPrinters();
        //listView = (ListView) findViewById(R.id.listViewSettingConnect);
//        listView.setAdapter(new SimpleAdapter(this, boundedPrinters,
//                R.layout.list_item_printernameandmac, new String[]{ICON,
//                PRINTERNAME, PRINTERMAC}, new int[]{
//                R.id.btListItemPrinterIcon, R.id.tvListItemPrinterName,
//                R.id.tvListItemPrinterMac}));
//        listView.setOnItemClickListener(this);



//        ArrayAdapter<String> btArrayAdapter
//                = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1);
//
//        BluetoothAdapter bluetoothAdapter
//                = BluetoothAdapter.getDefaultAdapter();
//        Set<BluetoothDevice> pairedDevices
//                = bluetoothAdapter.getBondedDevices();
//
//        if (pairedDevices.size() > 0) {
//            for (BluetoothDevice device : pairedDevices) {
//                deviceBTName = device.getName();
//                deviceaddress = device.getAddress();
////                String deviceBTMajorClass
////                        = getBTMajorDeviceClass(device
////                        .getBluetoothClass()
////                        .getMajorDeviceClass());
//                btArrayAdapter.add(" "+deviceBTName +":"
//                        + deviceaddress);
//            }
//        }
//        listView.setAdapter(btArrayAdapter);
//        listView.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //String address = (String) boundedPrinters.get(position).get(deviceaddress);
//                dialog.setMessage("connecting " + deviceaddress);
//                dialog.setIndeterminate(true);
//                dialog.setCancelable(false);
//                dialog.show();
//                DrawerService.workThread.connectBt(deviceaddress);
//            }
//        });

        progressBarSearchStatus.setVisibility(View.VISIBLE);
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null){

        }else {
            if (null == adapter) {
                finish();

            }

            if (!adapter.isEnabled()) {
                if (adapter.enable()) {
                    while (!adapter.isEnabled())
                        ;
                    Log.v(TAG, "Enable BluetoothAdapter");
                } else {
                    finish();

                }
            }

            adapter.cancelDiscovery();
            linearlayoutdevices.removeAllViews();
            //listView.setAdapter(null);
            adapter.startDiscovery();




            initBroadcast();

        }
        
        BluetoothAdapter bluetoothAdapter
                = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null){
            Toast.makeText(SearchBTActivity.this, "Bluettoh is not supported", Toast.LENGTH_SHORT).show();
        }else {
            Set<BluetoothDevice> pairedDevices
                    = bluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (final BluetoothDevice device : pairedDevices) {
                    //deviceBTName = device.getName();
                    //deviceaddress = device.getAddress();
//                String deviceBTMajorClass
//                        = getBTMajorDeviceClass(device
//                        .getBluetoothClass()
//                        .getMajorDeviceClass());

                    LinearLayout linearLayout = new LinearLayout(SearchBTActivity.this);
                    linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    ImageButton imageButton = new ImageButton(SearchBTActivity.this);
                    imageButton.setBackgroundColor(getResources().getColor(R.color.white));
                    imageButton.setImageResource(R.drawable.ic_bluetooth_black_24dp);
                    TextView button = new TextView(SearchBTActivity.this);
                    button.setClickable(true);
                    button.setText(device.getName() + ": " + device.getAddress());
                    button.setGravity(android.view.Gravity.CENTER_VERTICAL
                            | Gravity.LEFT);
                    //button.setCompoundDrawables(null, null, drawableTop, null);

                    Log.e("paired devices", device.getAddress());

                    button.setOnClickListener(new OnClickListener() {

                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            // ????????????????????
                            Cursor conn = db.rawQuery("SELECT * FROM BTConn", null);
                            if (conn.moveToFirst()) {
                                nameget = conn.getString(1);
                                addget = conn.getString(2);
                                statusget = conn.getString(3);
                            }
                            conn.close();
                            if (statusget.toString().equals("ok")) {//connected now disconnect
                                mChatService.stop();
//                                DrawerService.workThread.disconnectBt();
//                                Toast.makeText(SearchBTActivity.this, "a 1 disconnected", Toast.LENGTH_SHORT).show();
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("status", "");
                                String where1 = "_id = '1' ";
                                db.update("BTConn", contentValues, where1, new String[]{});

                                getconnectedprinters();
                            } else { //not connected now connect
//                                DrawerService.workThread.disconnectBt();
                                deviceBTName = device.getName();
                                deviceaddress = device.getAddress();
//                                dialog.setMessage("connecting " + device.getAddress());
//                                dialog.setIndeterminate(true);
//                                dialog.setCancelable(false);
//                                dialog.show();
//                                DrawerService.workThread.connectBt(device.getAddress());
//                                Toast.makeText(SearchBTActivity.this, " 1 " + deviceBTName +" 1 "+device.getName(), Toast.LENGTH_SHORT).show();

//                                String info = ((TextView) v).getText().toString();
//                                String address = info.substring(info.length() - 17);

//                                Toast.makeText(DeviceListActivity.this, "name1 "+address, Toast.LENGTH_LONG).show();
//                                Toast.makeText(DeviceListActivity.this, "name2 "+address, Toast.LENGTH_LONG).show();
//                                Toast.makeText(DeviceListActivity.this, "name3 "+address, Toast.LENGTH_LONG).show();
                                // Create the result Intent and include the MAC address
//                                Intent intent = new Intent();
//                                intent.putExtra(EXTRA_DEVICE_ADDRESS, device.getAddress());
//
//                                // Set result and finish this Activity
//                                setResult(Activity.RESULT_OK, intent);
//                                finish();

                                dialog.setMessage(getString(R.string.connecting) + device.getAddress());
                                dialog.setIndeterminate(true);
                                dialog.setCancelable(false);
                                dialog.show();
                                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(deviceaddress);
//                                Toast.makeText(SearchBTActivity.this, "add " + deviceaddress, Toast.LENGTH_LONG).show();
                                // Attempt to connect to the device
                                mChatService.connect(device);
                            }

                        }
                    });

                    linearLayout.addView(imageButton);
                    linearLayout.addView(button);

                    linearLayout.setOnClickListener(new OnClickListener() {

                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            // ????????????????????
                            Cursor conn = db.rawQuery("SELECT * FROM BTConn", null);
                            if (conn.moveToFirst()) {
                                nameget = conn.getString(1);
                                addget = conn.getString(2);
                                statusget = conn.getString(3);
                            }
                            conn.close();
                            if (statusget.toString().equals("ok")) {//connected now disconnect
                                mChatService.stop();
//                                DrawerService.workThread.disconnectBt();
//                                Toast.makeText(SearchBTActivity.this, "b 11 disconnected", Toast.LENGTH_SHORT).show();
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("status", "");
                                String where1 = "_id = '1' ";
                                db.update("BTConn", contentValues, where1, new String[]{});

                                getconnectedprinters();
                            } else { //not connected now connect
//                                DrawerService.workThread.disconnectBt();
                                deviceBTName = device.getName();
                                deviceaddress = device.getAddress();
//                                dialog.setMessage("connecting " + device.getAddress());
//                                dialog.setIndeterminate(true);
//                                dialog.setCancelable(false);
//                                dialog.show();
//                                DrawerService.workThread.connectBt(device.getAddress());
                                dialog.setMessage("connecting " + device.getAddress());
                                dialog.setIndeterminate(true);
                                dialog.setCancelable(false);
                                dialog.show();
//                                Toast.makeText(SearchBTActivity.this, " 11 " + deviceBTName + " 11 "+device.getName(), Toast.LENGTH_SHORT).show();
                                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(deviceaddress);
//                                Toast.makeText(SearchBTActivity.this, "add " + deviceaddress, Toast.LENGTH_LONG).show();
                                // Attempt to connect to the device
                                mChatService.connect(device);
                            }
                        }
                    });
                    button.setBackgroundColor(getResources().getColor(R.color.white));


//                if (nameget != null && addget != null && statusget != null && nameget.toString().equals(deviceBTName) && addget.equals(deviceaddress) && statusget.equals("ok")){
//                    button1 = new TextView(SearchBTActivity.this);
//                    button1.setText("Connected");
//                    button1.setVisibility(View.VISIBLE);
//                    button1.setBackgroundColor(getResources().getColor(R.color.white));
//                    linearLayout.addView(button1);
//                    Toast.makeText(SearchBTActivity.this, " 111 "+deviceBTName, Toast.LENGTH_SHORT).show();
//                }else {
//                    button1 = new TextView(SearchBTActivity.this);
//                    button1.setText("");
//                    button1.setVisibility(View.VISIBLE);
//                    button1.setBackgroundColor(getResources().getColor(R.color.white));
//                    linearLayout.addView(button1);
//                }

                    linearlayoutdevices1.addView(linearLayout);

                }
            }
        }


//        if (BluetoothDevice.ACTION_ACL_CONNECTED) {
//            //Do something if connected
//            //Toast.makeText(getApplicationContext(), "BT Connected", Toast.LENGTH_SHORT).show();
//            linearlayoutdevices.setBackgroundColor(getResources().getColor(R.color.blue));
//            linearlayoutdevices1.setBackgroundColor(getResources().getColor(R.color.blue));
//
//        }






//        mHandler = new MHandler(this);
//        DrawerService.addHandler(mHandler);



//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
//            probe();
//        } else {
//            finish();
//        }

//        mPrinterList = new ArrayList<HashMap<String, String>>();
//        mPrinterListAdapter = new SimpleAdapter(this, mPrinterList, R.layout.list_at_usb,
//                new String[] { "PrinterName","PrinterIP", "Target" },
//                new int[] { R.id.p_name, R.id.ip, Target });
//        ListView list = (ListView)findViewById(R.id.lstReceiveData);
//        list.setAdapter(mPrinterListAdapter);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                TextView one = (TextView) view.findViewById(R.id.Target);
//                TextView two = (TextView) view.findViewById(R.id.p_name);
//
//                ContentValues contentValues = new ContentValues();
//                contentValues.put("name", two.getText().toString());
//                contentValues.put("address", one.getText().toString());
//                contentValues.put("status", "ok");
//                String where1 = "_id = '1' ";
//                contentValues.put("device", "usb");
//                db.update("BTConn", contentValues, where1, new String[]{});
//                setStatusMsg("Connected");
//                getconnectedprinters();
//
//            }
//        });
//
//        mFilterOption = new FilterOption();
//        mFilterOption.setDeviceType(Discovery.TYPE_PRINTER);
//        mFilterOption.setEpsonFilter(Discovery.FILTER_NAME);
//        try {
//            Discovery.start(this, mFilterOption, mDiscoveryListener);
//        }
//        catch (Exception e) {
////            ShowMsg.showException(e, "start", mContext);
//        }



    }

    private DiscoveryListener mDiscoveryListener = new DiscoveryListener() {
        @Override
        public void onDiscovery(final DeviceInfo deviceInfo) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("PrinterName", deviceInfo.getDeviceName());
                    item.put("PrinterIP", deviceInfo.getIpAddress());
                    item.put("Target", deviceInfo.getTarget());
                    mPrinterList.add(item);
                    mPrinterListAdapter.notifyDataSetChanged();
                }
            });
        }
    };


    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {

            default:
                break;

        }

    }


    private void probe() {
        final UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        if (deviceList.size() > 0) {
            // ???????????????????

            while (deviceIterator.hasNext()) { // ???if??while??????????device
                final UsbDevice device = deviceIterator.next();
                final Drawable drawableTop = getResources().getDrawable(R.drawable.ic_usb_black_24dp);
                LinearLayout linearLayout = new LinearLayout(linearLayoutUSBDevices.getContext());
                linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                ImageButton imageButton = new ImageButton(linearLayoutUSBDevices.getContext());
                imageButton.setBackgroundColor(getResources().getColor(R.color.white));
                imageButton.setImageResource(R.drawable.ic_usb_black_24dp);
                TextView button = new TextView(linearLayoutUSBDevices.getContext());
                button.setClickable(true);
                button.setText(String.format(" VID:%04X PID:%04X",
                        device.getVendorId(), device.getProductId()));
                vendorid = device.getVendorId();
                productid = device.getProductId();
                button.setGravity(android.view.Gravity.CENTER_VERTICAL
                        | Gravity.LEFT);
                button.setCompoundDrawables(null, null, drawableTop, null);
//                Button btDevice = new Button(
//                        linearLayoutUSBDevices.getContext());
//                btDevice.setLayoutParams(new ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                btDevice.setGravity(android.view.Gravity.CENTER_VERTICAL
//                        | Gravity.LEFT);
//                btDevice.setText(String.format(" VID:%04X PID:%04X",
//                        device.getVendorId(), device.getProductId()));
                button.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        Cursor conn = db.rawQuery("SELECT * FROM BTConn", null);
                        if (conn.moveToFirst()) {
                            nameget = conn.getString(1);
                            addget = conn.getString(2);
                            statusget = conn.getString(3);
                        }
                        conn.close();
                        if (statusget.toString().equals("ok")){
//                            DrawerService.workThread.disconnectUsb();
//                            Toast.makeText(SearchBTActivity.this, "c 1 disconnected", Toast.LENGTH_SHORT).show();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("status", "");
                            String where1 = "_id = '1' ";
                            db.update("BTConn", contentValues, where1, new String[]{});

                            getconnectedprinters();
                        }else {
//                            DrawerService.workThread.disconnectUsb();
                            PendingIntent mPermissionIntent = null;
//                            PendingIntent mPermissionIntent = PendingIntent.getBroadcast(SearchBTActivity.this, 0, new Intent(USB_PERMISSION_STRING), 0);
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                                mPermissionIntent = PendingIntent.getBroadcast
                                        (SearchBTActivity.this, 0, new Intent(USB_PERMISSION_STRING), PendingIntent.FLAG_MUTABLE);
                            }
                            else {
                                mPermissionIntent = PendingIntent.getBroadcast
                                        (SearchBTActivity.this, 0, new Intent(USB_PERMISSION_STRING), PendingIntent.FLAG_ONE_SHOT);
                            }
                            if (!mUsbManager.hasPermission(device)) {
                                mUsbManager.requestPermission(device,
                                        mPermissionIntent);
                                //Toast.makeText(getApplicationContext(),"???????USB????????????", Toast.LENGTH_LONG).show();
                            } else {
                                USBDriver.USBPort port = new USBDriver.USBPort(mUsbManager, device,
                                        mPermissionIntent);
                                PL2303Driver.TTYTermios serial = new PL2303Driver.TTYTermios(9600,
                                        PL2303Driver.TTYTermios.FlowControl.NONE, PL2303Driver.TTYTermios.Parity.NONE,
                                        PL2303Driver.TTYTermios.StopBits.ONE, 8);
//                                DrawerService.workThread.connectUsb(port, serial);
//                                Toast.makeText(SearchBTActivity.this, " "+port+" "+serial, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                linearlayoutdevices1.setOnClickListener(new OnClickListener() {

                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        // ????????????????????
                        Cursor conn = db.rawQuery("SELECT * FROM BTConn", null);
                        if (conn.moveToFirst()) {
                            nameget = conn.getString(1);
                            addget = conn.getString(2);
                            statusget = conn.getString(3);
                        }
                        conn.close();
                        if (statusget.toString().equals("ok")){
//                            DrawerService.workThread.disconnectUsb();
//                            Toast.makeText(SearchBTActivity.this, "d 33 disconnected", Toast.LENGTH_SHORT).show();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("status", "");
                            String where1 = "_id = '1' ";
                            db.update("BTConn", contentValues, where1, new String[]{});

                            getconnectedprinters();
                        }else {
//                            DrawerService.workThread.disconnectUsb();
                            PendingIntent mPermissionIntent = null;
//                            PendingIntent mPermissionIntent = PendingIntent.getBroadcast(SearchBTActivity.this, 0, new Intent(USB_PERMISSION_STRING), 0);
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                                mPermissionIntent = PendingIntent.getBroadcast
                                        (SearchBTActivity.this, 0, new Intent(USB_PERMISSION_STRING), PendingIntent.FLAG_MUTABLE);
                            }
                            else {
                                mPermissionIntent = PendingIntent.getBroadcast
                                        (SearchBTActivity.this, 0, new Intent(USB_PERMISSION_STRING), PendingIntent.FLAG_ONE_SHOT);
                            }
                            if (!mUsbManager.hasPermission(device)) {
                                mUsbManager.requestPermission(device,
                                        mPermissionIntent);
                                //Toast.makeText(getApplicationContext(),"???????USB????????????", Toast.LENGTH_LONG).show();
                            } else {
                                USBDriver.USBPort port = new USBDriver.USBPort(mUsbManager, device,
                                        mPermissionIntent);
                                PL2303Driver.TTYTermios serial = new PL2303Driver.TTYTermios(9600,
                                        PL2303Driver.TTYTermios.FlowControl.NONE, PL2303Driver.TTYTermios.Parity.NONE,
                                        PL2303Driver.TTYTermios.StopBits.ONE, 8);
//                                DrawerService.workThread.connectUsb(port, serial);
                            }
                        }

                    }
                });
                button.setBackgroundColor(getResources().getColor(R.color.white));

//                        button1 = new TextView(context);
//                        button1.setText("Connected");
//                        button1.setVisibility(View.INVISIBLE);

                //Toast.makeText(SearchBTActivity.this, " 222 "+deviceBTName, Toast.LENGTH_SHORT).show();

                linearLayout.addView(imageButton);
                linearLayout.addView(button);
//                        linearLayout.addView(button1);
                linearlayoutdevices1.addView(linearLayout);
                //linearLayoutUSBDevices.addView(button);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_bt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search_bt:
                progressBarSearchStatus.setVisibility(View.VISIBLE);
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                if (null == adapter) {
                    finish();
                    break;
                }

                if (!adapter.isEnabled()) {
                    if (adapter.enable()) {
                        while (!adapter.isEnabled())
                            ;
                        Log.v(TAG, "Enable BluetoothAdapter");
                    } else {
                        finish();
                        break;
                    }
                }

                adapter.cancelDiscovery();
                linearlayoutdevices.removeAllViews();
                //listView.setAdapter(null);
                adapter.startDiscovery();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //    private String getBTMajorDeviceClass(int major){
//        switch(major){
//            case BluetoothClass.Device.Major.AUDIO_VIDEO:
//                return "AUDIO_VIDEO";
//            case BluetoothClass.Device.Major.COMPUTER:
//                return "COMPUTER";
//            case BluetoothClass.Device.Major.HEALTH:
//                return "HEALTH";
//            case BluetoothClass.Device.Major.IMAGING:
//                return "IMAGING";
//            case BluetoothClass.Device.Major.MISC:
//                return "MISC";
//            case BluetoothClass.Device.Major.NETWORKING:
//                return "NETWORKING";
//            case BluetoothClass.Device.Major.PERIPHERAL:
//                return "PERIPHERAL";
//            case BluetoothClass.Device.Major.PHONE:
//                return "PHONE";
//            case BluetoothClass.Device.Major.TOY:
//                return "TOY";
//            case BluetoothClass.Device.Major.UNCATEGORIZED:
//                return "UNCATEGORIZED";
//            case BluetoothClass.Device.Major.WEARABLE:
//                return "AUDIO_VIDEO";
//            default: return "unknown!";
//        }
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        DrawerService.delHandler(mHandler);
////        mHandler = null;
//
////        uninitBroadcast();
//
//        while (true) {
//            try {
//                Discovery.stop();
//
//                if (broadcastReceiver != null)
//                    unregisterReceiver(broadcastReceiver);
//
//                break;
//            }
//            catch (Epos2Exception e) {
//                if (e.getErrorStatus() != Epos2Exception.ERR_PROCESSING) {
//                    break;
//                }
//            }
//        }
//
////        mFilterOption = null;
//    }

//    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//                            long id) {
//        // TODO Auto-generated method stub
//
//        String address = (String) boundedPrinters.get(position).get(PRINTERMAC);
//        dialog.setMessage("connecting " + address);
//        dialog.setIndeterminate(true);
//        dialog.setCancelable(false);
//        dialog.show();
//        DrawerService.workThread.connectBt(address);
//    }

//    public void onClick(View arg0) {
//        // TODO Auto-generated method stub
//        switch (arg0.getId()) {
//            case R.id.buttonSearch: {
//                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
//                if (null == adapter) {
//                    finish();
//                    break;
//                }
//
//                if (!adapter.isEnabled()) {
//                    if (adapter.enable()) {
//                        while (!adapter.isEnabled())
//                            ;
//                        Log.v(TAG, "Enable BluetoothAdapter");
//                    } else {
//                        finish();
//                        break;
//                    }
//                }
//
//                adapter.cancelDiscovery();
//                linearlayoutdevices.removeAllViews();
//                //listView.setAdapter(null);
//                adapter.startDiscovery();
//                break;
//            }
//        }
//    }

    private void initBroadcast() {
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                String action = intent.getAction();
                final BluetoothDevice device1 = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                progressBarSearchStatus.setVisibility(View.VISIBLE);

                if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                    //Do something if connected
                    Toast.makeText(getApplicationContext(), "BT Connected", Toast.LENGTH_SHORT).show();
//                    linearlayoutdevices.setBackgroundColor(getResources().getColor(R.color.blue));
//                    linearlayoutdevices1.setBackgroundColor(getResources().getColor(R.color.blue));

                }

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    if (device1.getBondState() != BluetoothDevice.BOND_BONDED) {
                        if (device1 == null)
                            return;

                        //deviceBTName = device1.getName();
                        //deviceaddress = device1.getAddress();

                        scrollView.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);

                        final String address = device1.getAddress();
                        String name = device1.getName();
                        if (name == null)
                            name = "Bluetooth devices";
                        else if (name.equals(address))
                            name = "Bluetooth devices";
                        final Drawable drawableTop = getResources().getDrawable(R.drawable.ic_bluetooth_black_24dp);
                        LinearLayout linearLayout = new LinearLayout(context);
                        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        ImageButton imageButton = new ImageButton(context);
                        imageButton.setBackgroundColor(getResources().getColor(R.color.white));
                        imageButton.setImageResource(R.drawable.ic_bluetooth_black_24dp);
                        TextView button = new TextView(context);
                        button.setClickable(true);
                        button.setText(name + ": " + address);
                        button.setGravity(android.view.Gravity.CENTER_VERTICAL
                                | Gravity.LEFT);
                        button.setCompoundDrawables(null, null, drawableTop, null);

                        Log.e("1paired devices", address);

                        button.setOnClickListener(new OnClickListener() {

                            public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                // ????????????????????
                                Cursor conn = db.rawQuery("SELECT * FROM BTConn", null);
                                if (conn.moveToFirst()) {
                                    nameget = conn.getString(1);
                                    addget = conn.getString(2);
                                    statusget = conn.getString(3);
                                }
                                conn.close();
                                if (statusget.toString().equals("ok")){
                                    mChatService.stop();
//                                    DrawerService.workThread.disconnectBt();
//                                    Toast.makeText(SearchBTActivity.this, "e 2 disconnected", Toast.LENGTH_SHORT).show();
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("status", "");
                                    String where1 = "_id = '1' ";
                                    db.update("BTConn", contentValues, where1, new String[]{});

                                    getconnectedprinters();
                                }else {
                                    mChatService.stop();
//                                    DrawerService.workThread.disconnectBt();
                                    deviceBTName = device1.getName();
                                    deviceaddress = device1.getAddress();
                                    dialog.setMessage("connecting " + address);
                                    dialog.setIndeterminate(true);
                                    dialog.setCancelable(false);
                                    dialog.show();
//                                    DrawerService.workThread.connectBt(address);
//                                    progressBarSearchStatus.setVisibility(View.GONE);
//                                    Toast.makeText(SearchBTActivity.this, " 2 " + deviceBTName, Toast.LENGTH_SHORT).show();
                                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(deviceaddress);
//                                    Toast.makeText(SearchBTActivity.this, "add " + deviceaddress, Toast.LENGTH_LONG).show();
                                    // Attempt to connect to the device
                                    mChatService.connect(device);
                                }
                            }
                        });

                        linearlayoutdevices.setOnClickListener(new OnClickListener() {

                            public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                // ????????????????????
                                Cursor conn = db.rawQuery("SELECT * FROM BTConn", null);
                                if (conn.moveToFirst()) {
                                    nameget = conn.getString(1);
                                    addget = conn.getString(2);
                                    statusget = conn.getString(3);
                                }
                                conn.close();
                                if (statusget.toString().equals("ok")){
                                    mChatService.stop();
                                    deviceBTName = device1.getName();
                                    deviceaddress = device1.getAddress();
//                                    DrawerService.workThread.disconnectBt();
//                                    Toast.makeText(SearchBTActivity.this, "f 22 disconnected", Toast.LENGTH_SHORT).show();
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("status", "");
                                    String where1 = "_id = '1' ";
                                    db.update("BTConn", contentValues, where1, new String[]{});

                                    getconnectedprinters();
                                }else {
                                    mChatService.stop();
//                                    DrawerService.workThread.disconnectBt();
                                    deviceBTName = device1.getName();
                                    deviceaddress = device1.getAddress();
                                    dialog.setMessage("connecting " + address);
                                    dialog.setIndeterminate(true);
                                    dialog.setCancelable(false);
                                    dialog.show();
//                                    DrawerService.workThread.connectBt(address);
//                                    progressBarSearchStatus.setVisibility(View.GONE);
//                                    Toast.makeText(SearchBTActivity.this, " 22 " + deviceBTName, Toast.LENGTH_SHORT).show();
                                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(deviceaddress);
//                                    Toast.makeText(SearchBTActivity.this, "add " + deviceaddress, Toast.LENGTH_LONG).show();
                                    // Attempt to connect to the device
                                    mChatService.connect(device);

                                }
                            }
                        });
                        button.setBackgroundColor(getResources().getColor(R.color.white));

//                        button1 = new TextView(context);
//                        button1.setText("Connected");
//                        button1.setVisibility(View.INVISIBLE);

                        //Toast.makeText(SearchBTActivity.this, " 222 "+deviceBTName, Toast.LENGTH_SHORT).show();

                        linearLayout.addView(imageButton);
                        linearLayout.addView(button);
//                        linearLayout.addView(button1);
                        linearlayoutdevices.addView(linearLayout);
                        //linearlayoutdevices.addView(button);
                    }



                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED
                        .equals(action)) {
                    progressBarSearchStatus.setVisibility(View.VISIBLE);
                    progressBarSearchStatus.setIndeterminate(true);
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                        .equals(action)) {
                    progressBarSearchStatus.setIndeterminate(false);
                    progressBarSearchStatus.setVisibility(View.GONE);
                }

            }

        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(broadcastReceiver, intentFilter, RECEIVER_EXPORTED);
        }else {
            registerReceiver(broadcastReceiver, intentFilter);
        }

    }

    private void uninitBroadcast() {
        if (broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
    }

    private static List<Map<String, Object>> getBoundedPrinters() {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            return list;
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                .getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a
                // ListView
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(ICON, android.R.drawable.stat_sys_data_bluetooth);
                // //Toast.makeText(this,
                // ""+device.getBluetoothClass().getMajorDeviceClass(),
                // Toast.LENGTH_LONG).show();
                map.put(PRINTERNAME, device.getName());
                map.put(PRINTERMAC, device.getAddress());
                list.add(map);
            }
        }
        return list;
    }


    public void getnewdevices1() {

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (null == adapter) {
            finish();

        }

        if (!adapter.isEnabled()) {
            if (adapter.enable()) {
                while (!adapter.isEnabled())
                    ;
                Log.v(TAG, "Enable BluetoothAdapter");
            } else {
                finish();

            }
        }

        adapter.cancelDiscovery();
        linearlayoutdevices.removeAllViews();
        //listView.setAdapter(null);
        adapter.startDiscovery();
    }

    class MHandler extends Handler {

        WeakReference<SearchBTActivity> mActivity;

        MHandler(SearchBTActivity activity) {
            mActivity = new WeakReference<SearchBTActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SearchBTActivity theActivity = mActivity.get();
            switch (msg.what) {
                /**
                 * DrawerService ? onStartCommand???????
                 */

                case Global.MSG_WORKTHREAD_SEND_CONNECTBTRESULT: {
                    int result = msg.arg1;
                    //Toast.makeText(theActivity, (result == 1) ? "connection succeeded" : "Connection failed",  Toast.LENGTH_SHORT).show();
                    //Toast.makeText(SearchBTActivity.this, "111", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Connect Result: " + result);
                    theActivity.dialog.cancel();
                    if (result == 1){

                        //Toast.makeText(SearchBTActivity.this, "name "+deviceBTName+" address "+deviceaddress, Toast.LENGTH_SHORT).show();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("name", deviceBTName);
                        contentValues.put("address", deviceaddress);
                        contentValues.put("status", "ok");
                        contentValues.put("device", "bluetooth");
                        String where1 = "_id = '1' ";
                        db.update("BTConn", contentValues, where1, new String[]{});
                        //Toast.makeText(SearchBTActivity.this, " name is "+deviceBTName+" address "+deviceaddress, Toast.LENGTH_SHORT).show();
//                        getBoundedPrinters1();
//                        getnewdevices1();
                        setStatusMsg("Connected");
                        getconnectedprinters();
//                        Intent intent = getIntent();
//                        getWindow().setWindowAnimations(0);
//                        finish();
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//
//                        startActivity(intent);
//                        overridePendingTransition(0,0);
                    }else {
//                        Toast.makeText(SearchBTActivity.this, "i 33 disconnected", Toast.LENGTH_SHORT).show();
                        setStatusMsg1("Connected");
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("status", "");
                        String where1 = "_id = '1' ";
                        db.update("BTConn", contentValues, where1, new String[]{});
                        //Toast.makeText(SearchBTActivity.this, " disconnected "+deviceBTName+" address "+deviceaddress, Toast.LENGTH_SHORT).show();
//                        getBoundedPrinters1();
//                        getnewdevices1();
//                        setStatusMsg1("Connected");
//                        getconnectedprinters();

//                        Intent intent = getIntent();
//                        getWindow().setWindowAnimations(0);
//                        finish();
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//
//                        startActivity(intent);
//                        overridePendingTransition(0,0);
                    }

                    break;
                }


                case Global.MSG_WORKTHREAD_SEND_CONNECTUSBRESULT: {
                    int result = msg.arg1;
                    //Toast.makeText(theActivity, (result == 1) ? "connection succeeded" : "Connection failed",Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Connect Result: " + result);

                    if (result == 1){
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("name", vendorid);
                        contentValues.put("address", productid);
                        contentValues.put("status", "ok");
                        String where1 = "_id = '1' ";
                        contentValues.put("device", "usb");
                        db.update("BTConn", contentValues, where1, new String[]{});
                        setStatusMsg("Connected");
                        getconnectedprinters();
                    }else {
//                        Toast.makeText(SearchBTActivity.this, "j 33 disconnected", Toast.LENGTH_SHORT).show();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("status", "");
                        String where1 = "_id = '1' ";
                        db.update("BTConn", contentValues, where1, new String[]{});
                    }

                    break;
                }

            }
        }
    }





    private void setStatusMsg(String sts) {
        button1 = new TextView(SearchBTActivity.this);
        button1.setText("   Connected");
        button1.setVisibility(View.VISIBLE);
        button1.setBackgroundColor(getResources().getColor(R.color.white));
//        linearlayoutdevices.setBackgroundColor(getResources().getColor(R.color.blue));
//        linearlayoutdevices1.setBackgroundColor(getResources().getColor(R.color.blue));
    }

    private void setStatusMsg1(String sts) {
        button1 = new TextView(SearchBTActivity.this);
        button1.setText("");
        button1.setVisibility(View.VISIBLE);
        button1.setBackgroundColor(getResources().getColor(R.color.white));
//        linearlayoutdevices.setBackgroundColor(getResources().getColor(R.color.blue));
//        linearlayoutdevices1.setBackgroundColor(getResources().getColor(R.color.blue));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchBTActivity.this, Connect_Counter.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                LoginActivity.this.finish();
        startActivity(intent);

//        mChatService.stop();
//
//        Cursor cursor1 = db.rawQuery("SELECT * FROM BTConn", null);
//        if (cursor1.moveToFirst()){
//            nameget = cursor1.getString(1);
//            addget = cursor1.getString(2);
//            statusget = cursor1.getString(3);
//            deviceget = cursor1.getString(4);
//
//            TextView tv = new TextView(SearchBTActivity.this);
//            tv.setText(statusget);
//
//            if (tv.getText().toString().equals("ok")) {
//                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(addget);
////                                    Toast.makeText(SearchBTActivity.this, "add " + deviceaddress, Toast.LENGTH_LONG).show();
//                // Attempt to connect to the device
//                mChatService.connect(device);
//            }else {
//
//            }
//
//        }
//        cursor1.close();

        super.onBackPressed();
    }

    public void getconnectedprinters() {
        linearlayoutdevices11.removeAllViews();
        Cursor conn = db.rawQuery("SELECT * FROM BTConn", null);
        if (conn.moveToFirst()){
            nameget = conn.getString(1);
            addget = conn.getString(2);
            statusget = conn.getString(3);
            deviceget = conn.getString(4);
            if (statusget.toString().equals("ok")){
                if (deviceget.toString().equals("bluetooth")){
                    //if (nameget != null && addget != null && statusget != null && nameget.toString().equals(deviceBTName) && addget.equals(deviceaddress) && statusget.equals("ok")){
                    LinearLayout linearLayout = new LinearLayout(SearchBTActivity.this);
                    linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    ImageButton imageButton = new ImageButton(SearchBTActivity.this);
                    imageButton.setBackgroundColor(getResources().getColor(R.color.white));
                    imageButton.setImageResource(R.drawable.ic_bluetooth_black_24dp);
                    TextView button = new TextView(SearchBTActivity.this);
                    button.setClickable(true);
                    button.setGravity(android.view.Gravity.CENTER_VERTICAL
                            | Gravity.LEFT);
                    //button.setCompoundDrawables(null, null, drawableTop, null);


                    //if (nameget != null && addget != null && statusget != null && nameget.toString().equals(deviceBTName) && addget.equals(deviceaddress) && statusget.equals("ok")){
                    button.setText(nameget + ": " + addget);
                    button.setGravity(android.view.Gravity.CENTER_VERTICAL
                            | Gravity.LEFT);
                    button1 = new TextView(SearchBTActivity.this);
                    button1.setText("   Connected");
                    button1.setVisibility(View.VISIBLE);
                    button1.setBackgroundColor(getResources().getColor(R.color.white));
                    //linearLayout.addView(button1);
                    //Toast.makeText(SearchBTActivity.this, " 333 "+nameget, Toast.LENGTH_SHORT).show();
                    //}

//                button1 = new TextView(SearchBTActivity.this);
//                button1.setText("Connected");
//                button1.setVisibility(View.INVISIBLE);
//                Toast.makeText(SearchBTActivity.this, " 333 "+deviceBTName, Toast.LENGTH_SHORT).show();

                    linearLayout.addView(imageButton);
                    linearLayout.addView(button);
                    linearLayout.addView(button1);
                    linearlayoutdevices11.addView(linearLayout);
                    //}
                    deviceBTName = nameget;
                    deviceaddress = addget;
                }else {
                    LinearLayout linearLayout = new LinearLayout(SearchBTActivity.this);
                    linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    ImageButton imageButton = new ImageButton(SearchBTActivity.this);
                    imageButton.setBackgroundColor(getResources().getColor(R.color.white));
                    imageButton.setImageResource(R.drawable.ic_usb_black_24dp);
                    TextView button = new TextView(SearchBTActivity.this);
                    button.setClickable(true);
                    button.setGravity(android.view.Gravity.CENTER_VERTICAL
                            | Gravity.LEFT);
                    //button.setCompoundDrawables(null, null, drawableTop, null);


                    //if (nameget != null && addget != null && statusget != null && nameget.toString().equals(deviceBTName) && addget.equals(deviceaddress) && statusget.equals("ok")){
                    //button.setText(nameget + ": " + addget);
                    button.setText(String.valueOf(addget));
                    button.setGravity(android.view.Gravity.CENTER_VERTICAL
                            | Gravity.LEFT);
                    button1 = new TextView(SearchBTActivity.this);
                    button1.setText("   Connected");
                    button1.setVisibility(View.VISIBLE);
                    button1.setBackgroundColor(getResources().getColor(R.color.white));
                    //linearLayout.addView(button1);
                    //Toast.makeText(SearchBTActivity.this, " 333 "+nameget, Toast.LENGTH_SHORT).show();
                    //}

//                button1 = new TextView(SearchBTActivity.this);
//                button1.setText("Connected");
//                button1.setVisibility(View.INVISIBLE);
//                Toast.makeText(SearchBTActivity.this, " 333 "+deviceBTName, Toast.LENGTH_SHORT).show();

                    linearLayout.addView(imageButton);
                    linearLayout.addView(button);
                    linearLayout.addView(button1);
                    linearlayoutdevices11.addView(linearLayout);
                }

            }

            //DrawerService.workThread.connectBt(addget);
        }else {

        }
        conn.close();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
//        Toast.makeText(SearchBTActivity.this, "on activity result", Toast.LENGTH_LONG).show();
//        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
//                Toast.makeText(SearchBTActivity.this, "add1", Toast.LENGTH_LONG).show();
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras()
                            .getString(EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
//                    Toast.makeText(SearchBTActivity.this, "add " + address, Toast.LENGTH_LONG).show();
                    // Attempt to connect to the device
                    mChatService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
//                Toast.makeText(SearchBTActivity.this, "add2", Toast.LENGTH_LONG).show();
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    //Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    private void setupChat() {
//        Toast.makeText(getApplicationContext(), "sertupchat",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "setupChat()");
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothPrintDriver(this, mHandler);
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            Toast.makeText(getApplicationContext(), "no connnnnnnnnnn", Toast.LENGTH_SHORT).show();
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
//                    if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothPrintDriver.STATE_CONNECTED:
                            dialog.cancel();
//                            Toast.makeText(getApplicationContext(), "connected",Toast.LENGTH_SHORT).show();
//                            i = 1;
//                	mTitle.setText(R.string.title_connected_to);
//                    mTitle.append(mConnectedDeviceName);
                            //setTitle(R.string.title_connected_to);
                            //setTitle(mConnectedDeviceName);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("name", deviceBTName);
                            contentValues.put("address", deviceaddress);
                            contentValues.put("status", "ok");
                            contentValues.put("device", "bluetooth");
                            String where1 = "_id = '1' ";
                            db.update("BTConn", contentValues, where1, new String[]{});
                            //Toast.makeText(SearchBTActivity.this, " name is "+deviceBTName+" address "+deviceaddress, Toast.LENGTH_SHORT).show();
//                        getBoundedPrinters1();
//                        getnewdevices1();
                            setStatusMsg("Connected");
                            getconnectedprinters();

                            break;
                        case BluetoothPrintDriver.STATE_CONNECTING:
//                            Toast.makeText(getApplicationContext(), "connecting",Toast.LENGTH_SHORT).show();
//                	mTitle.setText(R.string.title_connecting);
                            //setTitle(R.string.title_connecting);
                            break;
                        case BluetoothPrintDriver.STATE_LISTEN:
//                            Toast.makeText(getApplicationContext(), "hi",Toast.LENGTH_SHORT).show();
                        case BluetoothPrintDriver.STATE_NONE:
                            dialog.cancel();
//                            Toast.makeText(getApplicationContext(), "not connected",Toast.LENGTH_SHORT).show();
//                	mTitle.setText(R.string.title_not_connected);
                            //setTitle(R.string.title_not_connected);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
                    String ErrorMsg = null;
                    byte[] readBuf = (byte[]) msg.obj;
                    float Voltage = 0;
//                    if(D) Log.i(TAG, "readBuf[0]:"+readBuf[0]+"  readBuf[1]:"+readBuf[1]+"  readBuf[2]:"+readBuf[2]);
                    if(readBuf[2]==0)
                        ErrorMsg = "NO ERROR!         ";
                    else
                    {
                        if((readBuf[2] & 0x02) != 0)
                            ErrorMsg = "ERROR: No printer connected!";
                        if((readBuf[2] & 0x04) != 0)
                            ErrorMsg = "ERROR: No paper!  ";
                        if((readBuf[2] & 0x08) != 0)
                            ErrorMsg = "ERROR: Voltage is too low!  ";
                        if((readBuf[2] & 0x40) != 0)
                            ErrorMsg = "ERROR: Printer Over Heat!  ";
                    }
                    Voltage = (float) ((readBuf[0]*256 + readBuf[1])/10.0);
                    //if(D) Log.i(TAG, "Voltage: "+Voltage);
                    DisplayToast(ErrorMsg+"                                        "+"Battery voltage??"+Voltage+" V");
//                    Toast.makeText(getApplicationContext(), "connnnnnnnnnn", Toast.LENGTH_SHORT).show();

//					if (i == 10){
//			Toast.makeText(getApplicationContext(), "3 conn", Toast.LENGTH_SHORT).show();
//                    btn_check_status.setText("connected");
//                    dialogconn.dismiss();
//					}else {
//			Toast.makeText(getApplicationContext(), "3 no conn", Toast.LENGTH_SHORT).show();
//						btn_check_status.setText("not connected");
//					}

//                    i = 10;
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
//                    Toast.makeText(getApplicationContext(), "1 "+msg.getData().getString(TOAST),
//                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void DisplayToast(String str)
    {
//        Toast toast = Toast.makeText(this,"3 "+ str, Toast.LENGTH_SHORT);
//        //????toast????????
//        toast.setGravity(Gravity.TOP, 0, 100);
//        //?????Toast
//        toast.show();



    }

    // BroadcastReceiver for usb events
    BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                Toast.makeText(context, "USB Connected", Toast.LENGTH_SHORT).show();
                mUsbManager = (UsbManager) getSystemService(context.USB_SERVICE);
                mDeviceList = mUsbManager.getDeviceList();
                if (mDeviceList.size() > 0) {
                    linearLayoutUSBDevices.setVisibility(View.VISIBLE);
                    mDeviceIterator = mDeviceList.values().iterator();
                    String usbDevice = "";
                    while (mDeviceIterator.hasNext()) {
                        UsbDevice usbDevice1 = mDeviceIterator.next();
                        mDevice = usbDevice1;
                        usbDevice +=
//                                "DeviceID: " + usbDevice1.getDeviceId() + "\n" +
                                "DeviceName: " + usbDevice1.getDeviceName() + "\n"+
//                                "Protocol: " + usbDevice1.getDeviceProtocol() + "\n" +
//                                //"Product Name: " + usbDevice1.getProductName() + "\n" +
//                                //"Manufacturer Name: " + usbDevice1.getManufacturerName() + "\n" +
                                "DeviceClass: " + usbDevice1.getDeviceClass() + " - " + translateDeviceClass(usbDevice1.getDeviceClass()) + "\n" ;
//                                "DeviceSubClass: " + usbDevice1.getDeviceSubclass() + "\n" +
//                                "VendorID: " + usbDevice1.getVendorId() + "\n" +
//                                "ProductID: " + usbDevice1.getProductId() + "\n";

                        System.out.println("device is "+usbDevice1.getDeviceName());
                    }
                    deviceInfo.setText(usbDevice);
                    mUsbManager.requestPermission(mDevice, mPermissionIntent);
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                Toast.makeText(context, "USB Disconnected", Toast.LENGTH_SHORT).show();
            }

            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, true)) {
                        if (device != null) {
                            //call method to set up device communication
                            mInterface = device.getInterface(0);
                            mEndPoint = mInterface.getEndpoint(1);// 0 IN and  1 OUT to printer.
                            mConnection = mUsbManager.openDevice(device);
                            Toast.makeText(context, "Connected "+deviceInfo.getText().toString(), Toast.LENGTH_SHORT).show();

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("name", "");
                            contentValues.put("address", deviceInfo.getText().toString());
                            contentValues.put("status", "ok");
                            String where1 = "_id = '1' ";
                            contentValues.put("device", "usb");
                            db.update("BTConn", contentValues, where1, new String[]{});
                            setStatusMsg("Connected");
                            getconnectedprinters();

                        }else{
                            System.out.println("MHandler  14");
                            //call method to set up device communication
                            String dev = String.valueOf(usbDevice);
                            mInterface = mDevice.getInterface(0);
//                            //mEndPoint = mInterface.getEndpoint(1);// 0 IN and  1 OUT to printer.
                            mConnection = mUsbManager.openDevice(mDevice);
                            Toast.makeText(context, "Connected "+deviceInfo.getText().toString(), Toast.LENGTH_SHORT).show();

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("name", "");
                            contentValues.put("address", deviceInfo.getText().toString());
                            contentValues.put("status", "ok");
                            String where1 = "_id = '1' ";
                            contentValues.put("device", "usb");
                            db.update("BTConn", contentValues, where1, new String[]{});
                            setStatusMsg("Connected");
                            getconnectedprinters();
                        }
                    } else {
                        Toast.makeText(context, "PERMISSION DENIED FOR THIS DEVICE", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    };

    // use if you want to identify device type
    private String translateDeviceClass(int deviceClass) {

        switch (deviceClass) {

            case UsbConstants.USB_CLASS_PRINTER:
                return "USB class for printers";

            default:
                return "Unknown USB class!";
        }
    }

}
