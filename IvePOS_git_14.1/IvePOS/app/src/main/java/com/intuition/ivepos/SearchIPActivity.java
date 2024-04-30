package com.intuition.ivepos;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

public class SearchIPActivity extends AppCompatActivity implements OnClickListener {

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


    private WifiPrintDriver wifiSocket = null;

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_TOAST = 4;

    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    public static int revBytes=0;

    private static final boolean D = true;

    Dialog dialog1;
    EditText ip_address_manual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connectip2);

        wifiSocket = new WifiPrintDriver(this, mHandler);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Network/Wifi printers");

        db =   openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

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
                Intent intent = new Intent(SearchIPActivity.this, SearchBTActivity.class);
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
                    Toast.makeText(SearchIPActivity.this, "Invalid Port Number", Toast.LENGTH_LONG)
                            .show();
                    valid = false;
                } catch (Exception e) {
                    Toast.makeText(SearchIPActivity.this, "Invalid IP Address", Toast.LENGTH_LONG)
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
                    wifiSocket.WIFISocket(ip, 9100);
//                    DrawerService1.workThread.connectNet(ip, port);

                    if(wifiSocket.IsNoConnection()){
//                        Toast.makeText(SearchIPActivity.this, "no connee", Toast.LENGTH_LONG).show();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("status", "");
                        String where1 = "_id = '1' ";
                        db.update("IPConn", contentValues, where1, new String[]{});
                        dialog.cancel();
                        return;
                    }else {
                        dialog.cancel();
//                        Toast.makeText(SearchIPActivity.this, "connee", Toast.LENGTH_LONG).show();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("ipname", ip1.getText().toString());
                        contentValues.put("port", inputPort.getText().toString());
                        contentValues.put("printer_name", p_name.getText().toString());
                        contentValues.put("status", "ok");
                        String where1 = "_id = '1' ";
                        db.update("IPConn", contentValues, where1, new String[]{});
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
        Cursor cursor = db.rawQuery("SELECT * FROM IPConn", null);
        if (cursor.moveToFirst()){
            ipget = cursor.getString(1);
            portget = cursor.getString(2);
            statusget = cursor.getString(3);
            p_name_get = cursor.getString(4);
            if (statusget.toString().equals("ok")){
                final LinearLayout linearLayout = new LinearLayout(SearchIPActivity.this);
                linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                ImageButton imageButton = new ImageButton(SearchIPActivity.this);
                imageButton.setBackgroundColor(getResources().getColor(R.color.white));
                imageButton.setImageResource(R.drawable.network_pref_icon_small);
                TextView button = new TextView(SearchIPActivity.this);
                button.setClickable(true);
                button.setGravity(android.view.Gravity.CENTER_VERTICAL
                        | Gravity.LEFT);
                //button.setCompoundDrawables(null, null, drawableTop, null);


                //if (nameget != null && addget != null && statusget != null && nameget.toString().equals(deviceBTName) && addget.equals(deviceaddress) && statusget.equals("ok")){
                button.setText(p_name_get+"  "+ipget + ": " + portget);
                button.setGravity(android.view.Gravity.CENTER_VERTICAL
                        | Gravity.LEFT);
                button1 = new TextView(SearchIPActivity.this);
                button1.setText("   Connected");
                button1.setVisibility(View.VISIBLE);
                button1.setBackgroundColor(getResources().getColor(R.color.white));
                //linearLayout.addView(button1);
                //Toast.makeText(SearchIPActivity.this, " 333 "+ipget, Toast.LENGTH_SHORT).show();
                //}

//                button1 = new TextView(SearchIPActivity.this);
//                button1.setText("Connected");
//                button1.setVisibility(View.INVISIBLE);
//                Toast.makeText(SearchIPActivity.this, " 333 "+deviceBTName, Toast.LENGTH_SHORT).show();

                linearLayout.addView(imageButton);
                linearLayout.addView(button);
                linearLayout.addView(button1);
                linearlayoutdevices1net.addView(linearLayout);
                button1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearlayoutdevices1net.removeAllViews();
//                        DrawerService1.workThread.disconnectNet();
                        wifiSocket.stop();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("status", "");
                        String where1 = "_id = '1' ";
                        db.update("IPConn", contentValues, where1, new String[]{});

                    }
                });

                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearlayoutdevices1net.removeAllViews();
//                        DrawerService1.workThread.disconnectNet();
                        wifiSocket.stop();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("status", "");
                        String where1 = "_id = '1' ";
                        db.update("IPConn", contentValues, where1, new String[]{});

                    }
                });

                linearlayoutdevices1net.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearlayoutdevices1net.removeAllViews();
//                        DrawerService1.workThread.disconnectNet();
                        wifiSocket.stop();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("status", "");
                        String where1 = "_id = '1' ";
                        db.update("IPConn", contentValues, where1, new String[]{});

                    }
                });
            }
        }
        cursor.close();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchIPActivity.this, Connect_Counter.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                LoginActivity.this.finish();
        startActivity(intent);



        wifiSocket.stop();

        Cursor cursor1 = db.rawQuery("SELECT * FROM IPconn", null);
        if (cursor1.moveToFirst()){
            ipget = cursor1.getString(1);
            portget = cursor1.getString(2);
            statusget = cursor1.getString(3);
            p_name_get = cursor1.getString(4);

            TextView tv = new TextView(SearchIPActivity.this);
            tv.setText(statusget);

            if (tv.getText().toString().equals("ok")) {
                wifiSocket.WIFISocket(ipget, 9100);
//                                    Toast.makeText(SearchBTActivity.this, "add " + deviceaddress, Toast.LENGTH_LONG).show();
                // Attempt to connect to the device
//                wifiSocket.connect(device);
            }else {

            }

        }
        cursor1.close();

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
                // Toast.makeText(SearchIPActivity.this, "ip is "+inputIp.getText().toString(), Toast.LENGTH_SHORT).show();
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
                    wifiSocket.WIFISocket(ip, 9100);
//                    DrawerService1.workThread.connectNet(ip, port);

                    if(wifiSocket.IsNoConnection()){
//                        Toast.makeText(SearchIPActivity.this, "no connee", Toast.LENGTH_LONG).show();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("status", "");
                        String where1 = "_id = '1' ";
                        db.update("IPConn", contentValues, where1, new String[]{});
                        dialog.cancel();
                        return;
                    }else {
                        dialog.cancel();
//                        Toast.makeText(SearchIPActivity.this, "connee", Toast.LENGTH_LONG).show();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("ipname", ip1.getText().toString());
                        contentValues.put("port", inputPort.getText().toString());
                        contentValues.put("printer_name", p_name.getText().toString());
                        contentValues.put("status", "ok");
                        String where1 = "_id = '1' ";
                        db.update("IPConn", contentValues, where1, new String[]{});
                        getconnecteddevices();
                    }

                }
                break;

        }

    }


    class MHandler extends Handler {

        WeakReference<SearchIPActivity> mActivity;

        MHandler(SearchIPActivity activity) {
            mActivity = new WeakReference<SearchIPActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SearchIPActivity theActivity = mActivity.get();
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
                        db.update("IPConn", contentValues, where1, new String[]{});
                        getconnecteddevices();
                    }else {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("status", "");
                        String where1 = "_id = '1' ";
                        db.update("IPConn", contentValues, where1, new String[]{});
                        getconnecteddevices();
                    }
                    break;
                }

            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ip_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_enter_manually:
//                Toast.makeText(SearchIPActivity.this, "Enter Manually", Toast.LENGTH_LONG).show();

                dialog1 = new Dialog(SearchIPActivity.this, R.style.notitle);
                dialog1.setContentView(R.layout.dialog_enter_ipaddress_manually);
                dialog1.show();


                ImageButton btncancel = (ImageButton) dialog1.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();

                        donotshowKeyboard(SearchIPActivity.this);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        }

                    }
                });

                String ip = "";
                Cursor cursor = db.rawQuery("SELECT * FROM IPConn", null);
                if (cursor.moveToFirst()) {
                    ip = cursor.getString(1);
                }

                ip_address_manual = (EditText) dialog1.findViewById(R.id.ip_address_manual);
                ip_address_manual.setText(ip);

                ImageButton btnsave = (ImageButton) dialog1.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DownloadMusicfromInternet1 downloadMusicfromInternet1 = new DownloadMusicfromInternet1();
                        downloadMusicfromInternet1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);





                    }
                });


                return (true);
        }
        return super.onOptionsItemSelected(item);
    }

    class DownloadMusicfromInternet1 extends AsyncTask<String, Void, Integer> {
        private ProgressDialog dialogp = new ProgressDialog(SearchIPActivity.this, R.style.timepicker_date_dialog);

        @Override
        protected Integer doInBackground(String... params) {


            wifiSocket.WIFISocket(ip_address_manual.getText().toString(), 9100);
//                    DrawerService1.workThread.connectNet(ip, port);

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // ????????????????????
            dialog.setMessage("connecting " + ip_address_manual.getText().toString() + ":" + "9100");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();

//            dialogp.setMessage("connecting " + ipnameget +" "+portget);
//            dialogp.setIndeterminate(true);
//            dialogp.setCancelable(false);
//            dialogp.show();
        }


        @Override
        protected void onPostExecute(Integer file_url) {
//            dialogp.dismiss();
            if(wifiSocket.IsNoConnection()){
                Toast.makeText(SearchIPActivity.this, "No connection", Toast.LENGTH_LONG).show();
                ContentValues contentValues = new ContentValues();
                contentValues.put("status", "");
                String where1 = "_id = '1' ";
                db.update("IPConn", contentValues, where1, new String[]{});
                dialog.cancel();
                return;
            }else {
                dialog1.dismiss();
                dialog.cancel();
                Toast.makeText(SearchIPActivity.this, "Connected", Toast.LENGTH_LONG).show();
                ContentValues contentValues = new ContentValues();
                contentValues.put("ipname", ip_address_manual.getText().toString());
                contentValues.put("port", "9100");
                contentValues.put("printer_name", "");
                contentValues.put("status", "ok");
                String where1 = "_id = '1' ";
                db.update("IPConn", contentValues, where1, new String[]{});
                getconnecteddevices();
                hideKeyboard(SearchIPActivity.this);


                hideKeyboard(SearchIPActivity.this);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    donotshowKeyboard(SearchIPActivity.this);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }

        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void donotshowKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }
}