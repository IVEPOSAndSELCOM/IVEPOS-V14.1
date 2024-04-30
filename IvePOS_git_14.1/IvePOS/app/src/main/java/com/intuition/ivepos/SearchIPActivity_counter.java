package com.intuition.ivepos;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.discovery.DeviceInfo;
import com.epson.epos2.discovery.Discovery;
import com.epson.epos2.discovery.DiscoveryListener;
import com.epson.epos2.discovery.FilterOption;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class SearchIPActivity_counter extends AppCompatActivity implements OnClickListener {

    //    private static Handler mHandler = null;
    private static String TAG = "ConnectIPActivity";

    public SQLiteDatabase db = null;
    EditText inputIp, inputPort;
    private ProgressDialog dialog;

    LinearLayout linearlayoutdevices1net;
    String ipget, portget, statusget, p_name_get;
    TextView button1;


    private Context mContext = null;

    private ArrayList<HashMap<String, String>> mPrinterList = null;
    private SimpleAdapter mPrinterListAdapter = null;
    private FilterOption mFilterOption = null;

    TextView ip1, p_name;
    String ip;


    private WifiPrintDriver2 wifiSocket2 = null;

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_TOAST = 4;

    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    public static int revBytes=0;

    private static final boolean D = true;

    String WebserviceUrl;
    String account_selection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connectip2);

        wifiSocket2 = new WifiPrintDriver2(this, mHandler);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title31));

        db =   openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(SearchIPActivity_counter.this);
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

        linearlayoutdevices1net = (LinearLayout)findViewById(R.id.linearlayoutdevices1net);
        findViewById(R.id.buttonConnectIP).setOnClickListener(this);
        inputIp = (EditText) findViewById(R.id.editTextInputIp);
        inputPort = (EditText) findViewById(R.id.editTextInputPort);
        dialog = new ProgressDialog(this, R.style.timepicker_date_dialog);

//        mHandler = new MHandler(this);
//        DrawerService1.addHandler(mHandler);

        SharedPreferences settings = getSharedPreferences(
                Global1.PREFERENCES_FILENAME, 0);
        inputIp.setText(settings.getString(Global1.PREFERENCES_IPADDRESS, "192.168.1.87"));
        inputPort.setText(""
                + settings.getInt(Global1.PREFERENCES_PORTNUMBER, 9100));


        Button gousbbt = (Button)findViewById(R.id.gotousbbt);
        gousbbt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchIPActivity_counter.this, SearchBTActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                LoginActivity.this.finish();
                startActivity(intent);
            }
        });

        getconnecteddevices();

        mContext = this;

        mPrinterList = new ArrayList<HashMap<String, String>>();
        mPrinterListAdapter = new SimpleAdapter(this, mPrinterList, R.layout.list_at,
                new String[] {"PrinterName", "PrinterIP"},
                new int[] {R.id.p_name, R.id.ip});
        ListView list = (ListView)findViewById(R.id.lstReceiveData);
        list.setAdapter(mPrinterListAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ip1 = (TextView) view.findViewById(R.id.ip);
                p_name = (TextView) view.findViewById(R.id.p_name);
                boolean valid = false;
                int port = 9100;
                try {
                    ip = ip1.getText().toString();
                    if (null == IPString.IsIPValid(ip))
                        throw new Exception("Invalid IP Address");
                    port = Integer.parseInt(inputPort.getText().toString());
                    valid = true;
                } catch (NumberFormatException e) {
                    Toast.makeText(SearchIPActivity_counter.this, "Invalid Port Number", Toast.LENGTH_LONG)
                            .show();
                    valid = false;
                } catch (Exception e) {
                    Toast.makeText(SearchIPActivity_counter.this, "Invalid IP Address", Toast.LENGTH_LONG)
                            .show();
                    valid = false;
                }
                if (valid) {
                    SharedPreferences settings = getSharedPreferences(
                            Global1.PREFERENCES_FILENAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(Global1.PREFERENCES_IPADDRESS, ip);
                    editor.putInt(Global1.PREFERENCES_PORTNUMBER, port);
                    editor.commit();

                    // ????????????????????
                    dialog.setMessage("connecting " + ip + ":" + port);
                    dialog.setIndeterminate(true);
                    dialog.setCancelable(false);
                    dialog.show();
                    wifiSocket2.WIFISocket(ip, 9100);
//                    DrawerService1.workThread.connectNet(ip, port);

                    if(wifiSocket2.IsNoConnection()){
//                        Toast.makeText(SearchIPActivity_counter.this, "no connee", Toast.LENGTH_LONG).show();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("status", "");
                        String where1 = "_id = '1' ";
                        db.update("IPConn_Counter", contentValues, where1, new String[]{});
                        dialog.cancel();
                        return;
                    }else {
                        dialog.cancel();
//                        Toast.makeText(SearchIPActivity_counter.this, "connee", Toast.LENGTH_LONG).show();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("ipname", ip1.getText().toString());
                        contentValues.put("port", inputPort.getText().toString());
                        contentValues.put("printer_name", p_name.getText().toString());
                        contentValues.put("status", "ok");
                        String where1 = "_id = '1' ";
                        db.update("IPConn_Counter", contentValues, where1, new String[]{});
                        getconnecteddevices();
                    }

                }
            }
        });

        mFilterOption = new FilterOption();
        mFilterOption.setDeviceType(Discovery.TYPE_PRINTER);
        mFilterOption.setEpsonFilter(Discovery.FILTER_NAME);
        try {
            Discovery.start(this, mFilterOption, mDiscoveryListener);
        }
        catch (Exception e) {
//            ShowMsg.showException(e, "start", mContext);
        }

    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {}
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
//                    dialogconn.dismiss();
                    String Msg = null;
                    byte[] readBuf = (byte[]) msg.obj;
                    if(D) Log.i(TAG, "readBuf[0]:"+readBuf[0] + "  revBytes:"+revBytes);

                    Msg = "";
                    for(int i=0;i<revBytes;i++)
                    {
                        Msg = Msg +" 0x";
                        Msg = Msg + Integer.toHexString(readBuf[i]);
                    }
//                DisplayToast(Msg);
                    break;
                case MESSAGE_TOAST:
                    break;
            }
        }
    };

    private DiscoveryListener mDiscoveryListener = new DiscoveryListener() {
        @Override
        public void onDiscovery(final DeviceInfo deviceInfo) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("PrinterName", deviceInfo.getDeviceName());
                    item.put("PrinterIP", deviceInfo.getIpAddress());
//                    item.put("Target", deviceInfo.getTarget());
                    mPrinterList.add(item);
                    mPrinterListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    public void getconnecteddevices(){
        linearlayoutdevices1net.removeAllViews();
        Cursor cursor = db.rawQuery("SELECT * FROM IPConn_Counter", null);
        if (cursor.moveToFirst()){
            ipget = cursor.getString(1);
            portget = cursor.getString(2);
            statusget = cursor.getString(3);
            p_name_get = cursor.getString(4);
            if (statusget.toString().equals("ok")){
                final LinearLayout linearLayout = new LinearLayout(SearchIPActivity_counter.this);
                linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                ImageButton imageButton = new ImageButton(SearchIPActivity_counter.this);
                imageButton.setBackgroundColor(getResources().getColor(R.color.white));
                imageButton.setImageResource(R.drawable.network_pref_icon_small);
                TextView button = new TextView(SearchIPActivity_counter.this);
                button.setClickable(true);
                button.setGravity(android.view.Gravity.CENTER_VERTICAL
                        | Gravity.LEFT);
                //button.setCompoundDrawables(null, null, drawableTop, null);


                //if (nameget != null && addget != null && statusget != null && nameget.toString().equals(deviceBTName) && addget.equals(deviceaddress) && statusget.equals("ok")){
                button.setText(p_name_get+"  "+ipget + ": " + portget);
                button.setGravity(android.view.Gravity.CENTER_VERTICAL
                        | Gravity.LEFT);
                button1 = new TextView(SearchIPActivity_counter.this);
                button1.setText("   Connected");
                button1.setVisibility(View.VISIBLE);
                button1.setBackgroundColor(getResources().getColor(R.color.white));
                //linearLayout.addView(button1);
                //Toast.makeText(SearchIPActivity_counter.this, " 333 "+ipget, Toast.LENGTH_SHORT).show();
                //}

//                button1 = new TextView(SearchIPActivity_counter.this);
//                button1.setText("Connected");
//                button1.setVisibility(View.INVISIBLE);
//                Toast.makeText(SearchIPActivity_counter.this, " 333 "+deviceBTName, Toast.LENGTH_SHORT).show();

                linearLayout.addView(imageButton);
                linearLayout.addView(button);
                linearLayout.addView(button1);
                linearlayoutdevices1net.addView(linearLayout);
                button1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearlayoutdevices1net.removeAllViews();
//                        DrawerService1.workThread.disconnectNet();
                        wifiSocket2.stop();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("status", "");
                        String where1 = "_id = '1' ";
                        db.update("IPConn_Counter", contentValues, where1, new String[]{});

                    }
                });

                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearlayoutdevices1net.removeAllViews();
//                        DrawerService1.workThread.disconnectNet();
                        wifiSocket2.stop();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("status", "");
                        String where1 = "_id = '1' ";
                        db.update("IPConn_Counter", contentValues, where1, new String[]{});

                    }
                });

                linearlayoutdevices1net.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearlayoutdevices1net.removeAllViews();
//                        DrawerService1.workThread.disconnectNet();
                        wifiSocket2.stop();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("status", "");
                        String where1 = "_id = '1' ";
                        db.update("IPConn_Counter", contentValues, where1, new String[]{});

                    }
                });
            }
        }
        cursor.close();
    }
    @Override
    public void onBackPressed() {
        if (account_selection.toString().equals("Dine")) {
            Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
            if (cursor3.moveToFirst()) {
                String lite_pro = cursor3.getString(1);

                TextView tv = new TextView(SearchIPActivity_counter.this);
                tv.setText(lite_pro);

                if (tv.getText().toString().equals("Lite")) {
                    Intent intent = new Intent(SearchIPActivity_counter.this, BeveragesMenuFragment_Dine_l.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(SearchIPActivity_counter.this, BeveragesMenuFragment_Dine.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }else {
                Intent intent = new Intent(SearchIPActivity_counter.this, BeveragesMenuFragment_Dine_l.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
//            Intent intent = new Intent(SearchIPActivity_counter.this, BeveragesMenuFragment_Dine.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                LoginActivity.this.finish();
//            startActivity(intent);
        }else {
            if (account_selection.toString().equals("Qsr")) {
                Intent intent = new Intent(SearchIPActivity_counter.this, BeveragesMenuFragment_Qsr.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                LoginActivity.this.finish();
                startActivity(intent);
            }else {
                Intent intent = new Intent(SearchIPActivity_counter.this, BeveragesMenuFragment_Retail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                LoginActivity.this.finish();
                startActivity(intent);
            }
        }

//        Intent intent = new Intent(SearchIPActivity_counter.this, BeveragesMenuFragment_Dine.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                LoginActivity.this.finish();
//        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        DrawerService1.delHandler(mHandler);
//        mHandler = null;

        while (true) {
            try {
                Discovery.stop();
                break;
            }
            catch (Epos2Exception e) {
                if (e.getErrorStatus() != Epos2Exception.ERR_PROCESSING) {
                    break;
                }
            }
        }

//        mFilterOption = null;
    }

    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {

            case R.id.buttonConnectIP:
                // Toast.makeText(SearchIPActivity_counter.this, "ip is "+inputIp.getText().toString(), Toast.LENGTH_SHORT).show();
                boolean valid = false;
                int port = 9100;
                String ip = "";
                try {
                    ip = inputIp.getText().toString();
                    if (null == IPString.IsIPValid(ip))
                        throw new Exception("Invalid IP Address");
                    port = Integer.parseInt(inputPort.getText().toString());
                    valid = true;
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid Port Number", Toast.LENGTH_LONG)
                            .show();
                    valid = false;
                } catch (Exception e) {
                    Toast.makeText(this, "Invalid IP Address", Toast.LENGTH_LONG)
                            .show();
                    valid = false;
                }
                if (valid) {
                    SharedPreferences settings = getSharedPreferences(
                            Global1.PREFERENCES_FILENAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(Global1.PREFERENCES_IPADDRESS, ip);
                    editor.putInt(Global1.PREFERENCES_PORTNUMBER, port);
                    editor.commit();

                    // ????????????????????
                    dialog.setMessage("connecting " + ip + ":" + port);
                    dialog.setIndeterminate(true);
                    dialog.setCancelable(false);
                    dialog.show();
                    wifiSocket2.WIFISocket(ip, 9100);
//                    DrawerService1.workThread.connectNet(ip, port);

                    if(wifiSocket2.IsNoConnection()){
//                        Toast.makeText(SearchIPActivity_counter.this, "no connee", Toast.LENGTH_LONG).show();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("status", "");
                        String where1 = "_id = '1' ";
                        db.update("IPConn_Counter", contentValues, where1, new String[]{});
                        dialog.cancel();
                        return;
                    }else {
                        dialog.cancel();
//                        Toast.makeText(SearchIPActivity_counter.this, "connee", Toast.LENGTH_LONG).show();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("ipname", ip1.getText().toString());
                        contentValues.put("port", inputPort.getText().toString());
                        contentValues.put("printer_name", p_name.getText().toString());
                        contentValues.put("status", "ok");
                        String where1 = "_id = '1' ";
                        db.update("IPConn_Counter", contentValues, where1, new String[]{});
                        getconnecteddevices();
                    }

                }
                break;

        }

    }


    class MHandler extends Handler {

        WeakReference<SearchIPActivity_counter> mActivity;

        MHandler(SearchIPActivity_counter activity) {
            mActivity = new WeakReference<SearchIPActivity_counter>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SearchIPActivity_counter theActivity = mActivity.get();
            switch (msg.what) {
                /**
                 * DrawerService ? onStartCommand???????
                 */

                case Global1.MSG_WORKTHREAD_SEND_CONNECTNETRESULT: {
                    int result = msg.arg1;
//                    Toast.makeText(theActivity, (result == 1) ? "connection succeeded" : "Connection failed",
//                            Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Connect Result: " + result);
                    theActivity.dialog.cancel();

                    if (result == 1){
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("ipname", ip1.getText().toString());
                        contentValues.put("port", inputPort.getText().toString());
                        contentValues.put("printer_name", p_name.getText().toString());
                        contentValues.put("status", "ok");
                        String where1 = "_id = '1' ";
                        db.update("IPConn_Counter", contentValues, where1, new String[]{});
                        getconnecteddevices();
                    }else {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("status", "");
                        String where1 = "_id = '1' ";
                        db.update("IPConn_Counter", contentValues, where1, new String[]{});
                        getconnecteddevices();
                    }
                    break;
                }

            }
        }
    }
}