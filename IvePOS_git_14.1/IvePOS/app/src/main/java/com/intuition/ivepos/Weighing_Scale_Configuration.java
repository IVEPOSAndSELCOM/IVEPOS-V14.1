package com.intuition.ivepos;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Weighing_Scale_Configuration extends AppCompatActivity implements BLEControllerCallback
{

    ListView DevicesListM;
    boolean bScaleControlActivityIsLaunchedM = false;

    private ProgressDialog ProgressDialogM;

    public SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weighing_scale_device_scan);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        TextView MsgViewL = (TextView)findViewById(R.id.pickDeviceMsg);
        MsgViewL.setText("Looking for devices..");

        LinearLayout back_activity = (LinearLayout) findViewById(R.id.back_activity);
        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        DevicesListM = (ListView) findViewById(R.id.deviceslist);

        TextView MsgViewL1 = (TextView)findViewById(R.id.pickDeviceMsg);
        MsgViewL1.setText("");
        String[] Devices = {""};

        ArrayAdapter<String> AdapterL = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Devices);
        DevicesListM.setAdapter(AdapterL);

        DevicesListM.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                String DeviceNameL = ((TextView) view).getText().toString();

                BLEController TheBleControllerL = BeveragesMenuFragment_Dine.BLEControllerM;
                TheBleControllerL.ConnectToDevice(DeviceNameL, true);

                Toast.makeText(Weighing_Scale_Configuration.this, " "+DeviceNameL, Toast.LENGTH_LONG).show();
                System.out.println("DeviceNameL "+DeviceNameL);

                ContentValues contentValues = new ContentValues();
                contentValues.put("scale_name", DeviceNameL);
                db.insert("Weighing_Scale_name", null, contentValues);
            }
        });

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //Make sure dialog is hidden
        ProgressDialogM.dismiss();
        //Cancel any scans in progress
        BLEController TheBleControllerL = BeveragesMenuFragment_Dine.BLEControllerM;
        if (!bScaleControlActivityIsLaunchedM)
        {
            TheBleControllerL.Disconnect();
            TheBleControllerL.StopScan();
        }
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (ProgressDialogM != null)
        {
            ProgressDialogM.dismiss();
            ProgressDialogM = null;
        }
    }

    @Override
    public void onResume()
    {
        ProgressDialogM = new ProgressDialog(this);
        ProgressDialogM.setIndeterminate(true);
        ProgressDialogM.setCancelable(false);

        bScaleControlActivityIsLaunchedM = false;
        BLEController TheBleControllerL = BeveragesMenuFragment_Dine.BLEControllerM;
        if (TheBleControllerL!=null)
        {
            TheBleControllerL.Disconnect();
            TheBleControllerL.SetCallBack(this);
            TheBleControllerL.StartScan(true, 5);
        }
        super.onResume();
    }

    @Override
    public void ShowProgressMessage(String MessageP)
    {
        mHandler.sendMessage(Message.obtain(null, MSG_PROGRESS, MessageP));
    }

    private static final int MSG_PROGRESS = 201;

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            BluetoothGattCharacteristic characteristic;
            switch (msg.what)
            {
                case MSG_PROGRESS:
                    String TraceMsgL = (String) msg.obj;
                    ShowProgressMessageInternal(TraceMsgL);
                    break;

            }
        }
    };

    void ShowProgressMessageInternal(String MessageP)
    {
        if (ProgressDialogM == null)
        {
            return;
        }
        ProgressDialogM.setMessage(MessageP);
        if (!ProgressDialogM.isShowing())
        {
            ProgressDialogM.show();
        }
    }

    public boolean isLocationEnabled()
    {
        int locationMode = 0;
        try
        {
            locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
        }
        catch (Settings.SettingNotFoundException e)
        {
            //e.printStackTrace();
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF;
    }

    @Override
    public void OnBleScanComplete(HashMap<String, BluetoothDevice> DevicesP)
    {
        ProgressDialogM.hide();
        TextView MsgViewL = (TextView)findViewById(R.id.pickDeviceMsg);
        if (DevicesP.size() == 0)
        {
            String MsgL = "No Phoenix Scale Found!";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (!isLocationEnabled())
                {
                    MsgL += " Your device may require you to turn on GPS for bluetooth scanning.";
                }
            }

            MsgViewL.setText(MsgL);

            String[] Devices = {""};

            ArrayAdapter<String> AdapterL = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Devices);
            DevicesListM.setAdapter(AdapterL);
        }
        else
        {
            MsgViewL.setText("Please select one device below to connect");
            String[] DeviceNamesL = DevicesP.keySet().toArray(new String[DevicesP.keySet().size()]);
            ArrayAdapter<String> AdapterL = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DeviceNamesL);
            DevicesListM.setAdapter(AdapterL);
        }
    }

    @Override
    public void NotificationReceived(byte[] NotificationDataP)
    {
        if (bScaleControlActivityIsLaunchedM)
        {
            return;
        }
        ProgressDialogM.hide();
        bScaleControlActivityIsLaunchedM = true;
//        Intent intent = new Intent(this, BeveragesMenuFragment_Dine.class);
//        startActivity(intent);
//        finish();

        Intent intent = new Intent();
        intent.putExtra("Weighing_stat", "Connected");
        setResult(RESULT_OK, intent);
        finish();

        ContentValues contentValues = new ContentValues();
        contentValues.put("Weighing_Scale_onoff", "Connected");
        String where = "_id = '1'";
        db.update("Weighing_Scale_status", contentValues, where, new String[]{});
    }

    @Override
    public void DeviceIsDisconnected()
    {

    }

    @Override
    public void ErrorsOccured(String ErrorsP)
    {

    }
}
