package com.intuition.ivepos;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.intuition.ivepos.mSwipe.ApplicationData;
import com.intuition.ivepos.mSwipe.Logs;
import com.intuition.ivepos.syncapp.StubProviderApp;
import com.intuition.ivepos.wisepos.ReceiptBitmap;
import com.mswipetech.wisepad.sdk.Print;
import com.mswipetech.wisepad.sdk.device.MSWisepadDeviceController;
import com.socsi.smartposapi.printer.Align;
import com.socsi.smartposapi.printer.FontLattice;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.view.View.GONE;
import static com.intuition.ivepos.BluetoothPrintDriver.BT_Write;

/**
 * Created by Rohithkumar on 8/7/2017.
 */

public class KOT_Management extends AppCompatActivity implements ReceiveListener {


    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;

    private View mView;
    private LayoutInflater inflater;

    String total, name, price, aqq;

    TextView viv;

    String addget, nameget, statussusb, deviceget;
    String ipnameget, portget, statusnet;
    String ipnameget_counter, portget_counter, statusnet_counter;
    String addgets, namegets, statussusbs;
    String ipnamegets, portgets, statusnets;
    String ipnamegets_kot1, portgets_kot1, statusnets_kot1, name_kot1;
    String ipnamegets_kot2, portgets_kot2, statusnets_kot2, name_kot2;
    String ipnamegets_kot3, portgets_kot3, statusnets_kot3, name_kot3;
    String ipnamegets_kot4, portgets_kot4, statusnets_kot4, name_kot4;
    String ipnamegets_counter, portgets_counter, statusnets_counter;
    TextView textViewstatussusbs, textViewstatusnets, textViewstatusnets_counter;
    TextView textViewstatusnets_kot1, textViewstatusnets_kot2, textViewstatusnets_kot3, textViewstatusnets_kot4;
    TextView tvkot;
//    TextView textViewstatussusbs, textViewstatusnets, tvkot;
//    TextView textViewstatusnets_counter;
    int printer = 0;

    String assa, assa1, assa2;


    EditText companyname, address1, address2, address3, phone, emailid, website, taxone, billone;
    TextView companynameget, address1get, address2get, address3get, phoneget, emailidget, websiteget, taxoneget, billoneget, datete, timeme;
    String strcompanyname, straddress1, straddress2, straddress3, strphone, stremailid, strwebsite, strtaxone, strbillone;
    String stritemname,strbarcodeno,strprice;



    String aqq2;
    float aqq1;

    String totalquanret1 = "0", totalquanret2 = "0", totalquanret;

    byte[] setHT32, setHT321, setHT33, setHT34, setHT3212, setHTKOT, feedcut2;
    int nPaperWidth;

    int iRowNumber, charlength, charlength1, charlength2, quanlentha;

    byte[][] allbuf, allbuf1, allbuf2, allbuf3, allbuf4, allbuf5, allbuf6, allbuf7, allbuf8, allbuf9, allbuf10, allbuf11, allbufqty, allbufitems, allbufmodifiers, allbufsubtot,
            allbuftax, allbufdisc, allbufrounded, allbuffulltot, allbuf12, allbuf13, allbuf14,allbufbillno,allbuftime,allbufline1,allbufline,allbufcust,allbufcustname,
            allbufcustadd,allbufcustph,allbufcustemail, allbuftaxestype2, allbuftaxestype1, allbuf1122, allbufKOT;

    int padding_in_px, padding_in_px1, padding_in_px2,size_in_95px, size_in_116px, size_in_107px, size_in_60px;

    String ItemIDtable;

    String str_print_ty;
    String NAME;
    private WifiPrintDriver wifiSocket = null;
    private WifiPrintDriver2 wifiSocket2 = null;
    private WifiPrintDriver_kot1 wifiSocket_kot1 = null;
    private WifiPrintDriver_kot2 wifiSocket_kot2 = null;
    private WifiPrintDriver_kot3 wifiSocket_kot3 = null;
    private WifiPrintDriver_kot4 wifiSocket_kot4 = null;

    public static int revBytes1 = 0;
    public static int revBytes2 = 0;
    public static int revBytes1_kot1 = 0;
    public static int revBytes1_kot2 = 0;
    public static int revBytes1_kot3 = 0;
    public static int revBytes1_kot4 = 0;
    private static final String TAG = "WorkThread";

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    private static final boolean D = true;


    private Printer mPrinter = null;
    int barcodeWidth, barcodeHeight, pageAreaHeight, pageAreaWidth;

    private EditText mEditTarget = null;
    private Spinner mSpnSeries = null;
    private Spinner mSpnLang = null;
    Bitmap yourBitmap;
    private Context mContext = null;

    Dialog dialog;

    TableLayout tableLayout;

    Uri contentUri,resultUri;

    public MSWisepadDeviceController mMSWisepadDeviceController = null;
    private ArrayList<byte[]> mPrintData;

    boolean mswipe_text = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kot_order_management);

        mContext = this;

        mSpnSeries = (Spinner) findViewById(R.id.spnModel);
        ArrayAdapter<SpnModelsItem> seriesAdapter = new ArrayAdapter<SpnModelsItem>(this, android.R.layout.simple_spinner_item);
        seriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t82), Printer.TM_T82));
        mSpnSeries.setAdapter(seriesAdapter);
        mSpnSeries.setSelection(0);

        mSpnLang = (Spinner) findViewById(R.id.spnLang);
        ArrayAdapter<SpnModelsItem> langAdapter = new ArrayAdapter<SpnModelsItem>(this, android.R.layout.simple_spinner_item);
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_ank), Printer.MODEL_ANK));
        mSpnLang.setAdapter(langAdapter);
        mSpnLang.setSelection(0);

//        try {
//            com.epson.epos2.Log.setLogSettings(mContext, com.epson.epos2.Log.PERIOD_TEMPORARY, com.epson.epos2.Log.OUTPUT_STORAGE, null, 0, 1, com.epson.epos2.Log.LOGLEVEL_LOW);
//        } catch (Exception e) {
////            Toast.makeText(KOT_Management.this, "Here8", Toast.LENGTH_SHORT).show();
//            ShowMsg.showException(e, "setLogSettings", mContext);
//        }
        mEditTarget = (EditText) findViewById(R.id.edtTarget);

        textViewstatusnets = new TextView(KOT_Management.this);
        textViewstatusnets_counter = new TextView(KOT_Management.this);
        textViewstatussusbs = new TextView(KOT_Management.this);

        tvkot = new TextView(KOT_Management.this);

        int padding_in_dp = 10;  // 20 dps
        final float scale = getResources().getDisplayMetrics().density;
        padding_in_px = (int) (padding_in_dp * scale + 0.5f);

        int padding_in_dp1 = 12;  // 12 dps
        final float scale1 = getResources().getDisplayMetrics().density;
        padding_in_px1 = (int) (padding_in_dp1 * scale1 + 0.5f);

        int padding_in_dp2 = 9;  // 8 dps
        final float scale2 = getResources().getDisplayMetrics().density;
        padding_in_px2 = (int) (padding_in_dp2 * scale2 + 0.5f);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);


        Bundle extras = getIntent().getExtras();
        String player1name = extras.getString("PLAYER1NAME");
        ItemIDtable = extras.getString("PLAYER2NAME");

//        Toast.makeText(KOT_Management.this, "PLAYER1NAME "+player1name, Toast.LENGTH_LONG).show();
//        Toast.makeText(KOT_Management.this, "PLAYER2NAME "+ItemIDtable, Toast.LENGTH_LONG).show();

        LinearLayout back_activity = (LinearLayout) findViewById(R.id.back_activity);
        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("table", ItemIDtable);
                myEdit.apply();

                Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                if (cursor3.moveToFirst()) {
                    String lite_pro = cursor3.getString(1);

                    TextView tv = new TextView(KOT_Management.this);
                    tv.setText(lite_pro);

                    if (tv.getText().toString().equals("Lite")) {
                        Intent intent = new Intent(KOT_Management.this, BeveragesMenuFragment_Dine_l.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(KOT_Management.this, BeveragesMenuFragment_Dine.class);
                        startActivity(intent);
                    }
                }else {
                    Intent intent = new Intent(KOT_Management.this, BeveragesMenuFragment_Dine_l.class);
                    startActivity(intent);
                }

//                Intent intent = new Intent(KOT_Management.this, BeveragesMenuFragment_Dine.class);
//                startActivity(intent);

            }
        });

        TextView cust_name = (TextView) findViewById(R.id.cust_name);
//        cust_name.setText("Tab"+ItemIDtable);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String get_table = sh.getString("table", "");
        String get_floor = sh.getString("floor", "");
        String get_position = sh.getString("position", "");
        String get_pDate = sh.getString("table_number", "");

        cust_name.setText(get_floor+","+get_pDate);

        int oone11 = 0;

//        Cursor cv = db1.rawQuery("SELECT * FROM Table" + ItemIDtable + "management", null);
//        if (cv.moveToFirst()){
//            do {
//                String idd = cv.getString(0);
//                String ina = cv.getString(1);
//                String qt = cv.getString(2);
//                String pa = cv.getString(6);
//
//                if (Float.parseFloat(qt)<0){
//                    Cursor cvf = db1.rawQuery("SELECT SUM(qty) FROM Table" + ItemIDtable + "management WHERE par_id = '"+pa+"' AND itemname = '"+ina+"'", null);
//                    if (cvf.moveToFirst()){
//                        oone11 = cvf.getInt(0);
//                    }
//
//                    Cursor cvf1 = db1.rawQuery("SELECT * FROM Table" + ItemIDtable + "management WHERE par_id = '"+pa+"' AND itemname = '"+ina+"'", null);
//                    if (cvf1.moveToFirst()){
//                        do {
//                            ContentValues contentValues = new ContentValues();
//                            contentValues.put("qty", oone11);
//                            String id = cvf1.getString(0);
//                            String whereh1 = "_id = '" +id+ "' ";
//                            db1.update("Table" + ItemIDtable + "management", contentValues, whereh1, new String[]{});
//                        }while (cvf1.moveToNext());
//                    }
//
//                }
//
//            }while (cv.moveToNext());
//        }
//
//        Toast.makeText(KOT_Management.this, "one11 "+oone11, Toast.LENGTH_LONG).show();



        Cursor cvf1 = db1.rawQuery("SELECT * FROM Table" + ItemIDtable + "management", null);
        if (cvf1.moveToFirst()){
            do {
                String id = cvf1.getString(0);
                String par_id = cvf1.getString(6);

                Cursor cvf2 = db1.rawQuery("SELECT * FROM Table" + ItemIDtable + " WHERE _id = '"+par_id+"'", null);
                if (cvf2.moveToFirst()){

                }else {
                    String where = "_id = '" + id + "' ";
                    db1.delete("Table" + ItemIDtable + "management", where, new String[]{});
                }
            }while (cvf1.moveToNext());
        }

        Cursor cvf11 = db1.rawQuery("SELECT * FROM Table" + ItemIDtable + "management WHERE itemtype = 'Modifier'", null);
        if (cvf11.moveToFirst()){
            do {
                String id = cvf11.getString(0);
                String par_id = cvf11.getString(6);
                String ina = cvf11.getString(1);

                Cursor cvf2 = db1.rawQuery("SELECT * FROM Table" + ItemIDtable + " WHERE parentid = '"+par_id+"' AND itemname = '"+ina+"'", null);
                if (cvf2.moveToFirst()){

                }else {
                    String where = "_id = '" + id + "' ";
                    db1.delete("Table" + ItemIDtable + "management", where, new String[]{});
                }
            }while (cvf11.moveToNext());
        }

//        Cursor cursorqa = db1.rawQuery("SELECT * FROM Table" + ItemIDtable + "management WHERE itemtype = 'Item'", null);
//        if (cursorqa.moveToFirst()){
//            do {
//                String id = cursorqa.getString(0);
//                String in = cursorqa.getString(1);
//                String pid = cursorqa.getString(6);
//
//                String qt = cursorqa.getString(2);
//                String tg = cursorqa.getString(3);
//                String dt = cursorqa.getString(4);
//                String ti = cursorqa.getString(5);
//
//
//                Toast.makeText(KOT_Management.this, id + " same is " + in, Toast.LENGTH_LONG).show();
//
//                Cursor cursora = db1.rawQuery("SELECT * FROM Table" + ItemIDtable + "management WHERE par_id = '"+pid+"' AND itemtype = 'Item' AND _id != '"+id+"'", null);
//                if (cursora.moveToFirst()){
//                    do {
//                        String ida = cursora.getString(0);
//                        String ina = cursora.getString(1);
//
//                        Toast.makeText(KOT_Management.this, ida + " inside is " + ina, Toast.LENGTH_LONG).show();
//
//                        String where = "_id = '" + ida + "' ";
//                        db1.delete("Table" + ItemIDtable + "management", where, new String[]{});
//
//                    }while (cursora.moveToNext());
//                }
//
//                ContentValues cva = new ContentValues();
//                cva.put("itemname", in);
//                cva.put("qty", qt);
//                cva.put("tagg", tg);
//                cva.put("date", dt);
//                cva.put("time", ti);
//                cva.put("par_id", pid);
//                cva.put("itemtype", "Item");
//                db1.insert("Table" + ItemIDtable + "management", null, cva);
//
//
//            }while (cursorqa.moveToNext());
//        }


        textViewstatusnets_kot1 = new TextView(KOT_Management.this);
        textViewstatusnets_kot2 = new TextView(KOT_Management.this);
        textViewstatusnets_kot3 = new TextView(KOT_Management.this);
        textViewstatusnets_kot4 = new TextView(KOT_Management.this);

        wifiSocket = new WifiPrintDriver(this, mHandlera2);
        wifiSocket_kot1 = new WifiPrintDriver_kot1(this, mHandlera2_kot1);
        wifiSocket_kot2 = new WifiPrintDriver_kot2(this, mHandlera2_kot2);
        wifiSocket_kot3 = new WifiPrintDriver_kot3(this, mHandlera2_kot3);
        wifiSocket_kot4 = new WifiPrintDriver_kot4(this, mHandlera2_kot4);

        tableLayout = (TableLayout) findViewById(R.id.lytpedido);
        tableLayout.removeAllViews();





//        TableLayout tableLayout1 = (TableLayout) findViewById(R.id.table);


        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        displaydata();




        Button printall = (Button) findViewById(R.id.printall);
        printall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (printer == 0) {
                Cursor getprint_type = db.rawQuery("SELECT * FROM Printer_text_size", null);
                if (getprint_type.moveToFirst()) {
                    String type = getprint_type.getString(1);

                    if (type.toString().equals("Standard")) {
//                Toast.makeText(, "selected Standard", Toast.LENGTH_SHORT).show();
                        byte[] LF = {0x0d, 0x0a};

                        allbuftaxestype1 = new byte[][]{
                                " ".getBytes(), LF
                        };

                    } else {
//                Toast.makeText(getActivity(), "selected Compact", Toast.LENGTH_SHORT).show();
                        byte[] LF = {0x0d, 0x0a};

                        allbuftaxestype1 = new byte[][]{
                                " ".getBytes(), LF
                        };

                    }
                }
//                }


                Cursor connnet = db.rawQuery("SELECT * FROM IPConn", null);
                if (connnet.moveToFirst()) {
                    ipnameget = connnet.getString(1);
                    portget = connnet.getString(2);
                    statusnet = connnet.getString(3);
                }

                Cursor connnet_counter = db.rawQuery("SELECT * FROM IPConn_Counter", null);
                if (connnet_counter.moveToFirst()) {
                    ipnameget_counter = connnet_counter.getString(1);
                    portget_counter = connnet_counter.getString(2);
                    statusnet_counter = connnet_counter.getString(3);
                }
                connnet_counter.close();

                Cursor conn = db.rawQuery("SELECT * FROM BTConn", null);
                if (conn.moveToFirst()) {
                    nameget = conn.getString(1);
                    addget = conn.getString(2);
                    statussusb = conn.getString(3);
                }



                Cursor c_kot1 = db.rawQuery("SELECT * FROM IPConn_KOT1", null);
                if (c_kot1.moveToFirst()) {
                    ipnamegets_kot1 = c_kot1.getString(1);
                    portgets_kot1 = c_kot1.getString(2);
                    statusnets_kot1 = c_kot1.getString(3);
                    name_kot1 = c_kot1.getString(5);
                }
                c_kot1.close();

                Cursor c_kot2 = db.rawQuery("SELECT * FROM IPConn_KOT2", null);
                if (c_kot2.moveToFirst()) {
                    ipnamegets_kot2 = c_kot2.getString(1);
                    portgets_kot2 = c_kot2.getString(2);
                    statusnets_kot2 = c_kot2.getString(3);
                    name_kot2 = c_kot2.getString(5);
                }
                c_kot2.close();

                Cursor c_kot3 = db.rawQuery("SELECT * FROM IPConn_KOT3", null);
                if (c_kot3.moveToFirst()) {
                    ipnamegets_kot3 = c_kot3.getString(1);
                    portgets_kot3 = c_kot3.getString(2);
                    statusnets_kot3 = c_kot3.getString(3);
                    name_kot3 = c_kot3.getString(5);
                }
                c_kot3.close();

                Cursor c_kot4 = db.rawQuery("SELECT * FROM IPConn_KOT4", null);
                if (c_kot4.moveToFirst()) {
                    ipnamegets_kot4 = c_kot4.getString(1);
                    portgets_kot4 = c_kot4.getString(2);
                    statusnets_kot4 = c_kot4.getString(3);
                    name_kot4 = c_kot4.getString(5);
                }
                c_kot4.close();

                if (statusnet.toString().equals("ok") || statusnet_counter.toString().equals("ok") || statussusb.toString().equals("ok")
                        || statusnets_kot1.toString().equals("ok") || statusnets_kot2.toString().equals("ok")
                        || statusnets_kot3.toString().equals("ok") || statusnets_kot4.toString().equals("ok")) {

                    if (statusnet.toString().equals("ok")) {
                        wifiSocket.WIFISocket(ipnameget, 9100);
                    }
                    if (statusnets_kot1.toString().equals("ok")) {
                        wifiSocket_kot1.WIFISocket(ipnamegets_kot1, 9100);
                    }
                    if (statusnets_kot2.toString().equals("ok")) {
                        wifiSocket_kot2.WIFISocket(ipnamegets_kot2, 9100);
                    }
                    if (statusnets_kot3.toString().equals("ok")) {
                        wifiSocket_kot3.WIFISocket(ipnamegets_kot3, 9100);
                    }
                    if (statusnets_kot4.toString().equals("ok")) {
                        wifiSocket_kot4.WIFISocket(ipnamegets_kot4, 9100);
                    }

                    Cursor kot = db1.rawQuery("Select * from Table" + ItemIDtable + "", null);
                    if (kot.moveToFirst()){
//                        printbillsplithome();
                        Cursor cursor1 = db1.rawQuery("Select * from Table" + ItemIDtable +" WHERE dept_name = '"+name_kot1+"'", null);
                        if (cursor1.moveToFirst()) {
                            String ns = cursor1.getString(2);
                            if (statusnets_kot1.toString().equals("ok")) {
                                System.out.println("assigned for kot1 "+ns);
                                printbillsplithome_kot1(name_kot1);
                                Handler handler =  new Handler(KOT_Management.this.getMainLooper());
                                handler.post( new Runnable(){
                                    public void run(){
                                        Toast.makeText(KOT_Management.this, "KOT printed",Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {

                                String printer_type="";
                                Cursor aallrows = db.rawQuery("SELECT * FROM Printer_type WHERE _id = '1'", null);
                                if (aallrows.moveToFirst()) {
                                    do {
                                        printer_type = aallrows.getString(1);

                                    } while (aallrows.moveToNext());
                                }
                                aallrows.close();

                                if(printer_type.equalsIgnoreCase("wiseposplus")) {

                                    if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO)
                                    {
                                        mPrintData = new ArrayList<>();
                                        byte[] receiptData = neoprintbillsplithome_kot_BT(name_kot1);
                                        mPrintData.add(receiptData);


                                        if(mPrintData != null && mPrintData.size() > 0)
                                        {
                                            mMSWisepadDeviceController.print(mPrintData);
                                        }

                                    }else{

                                        System.out.println("assigned for kot11 "+ns);
                                        wiseposprintbillsplithome_kot_BT(name_kot1);
                                        Handler handler =  new Handler(KOT_Management.this.getMainLooper());
                                        handler.post( new Runnable(){
                                            public void run(){
                                                Toast.makeText(KOT_Management.this, "KOT printed",Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }

                                }else {
                                    System.out.println("assigned for kot11 " + ns);
                                    printbillsplithome_kot_BT(name_kot1);
                                    Handler handler = new Handler(KOT_Management.this.getMainLooper());
                                    handler.post(new Runnable() {
                                        public void run() {
                                            Toast.makeText(KOT_Management.this, "KOT printed", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        }
                        cursor1.close();

                        Cursor cursor2 = db1.rawQuery("Select * from Table" + ItemIDtable +" WHERE dept_name = '"+name_kot2+"'", null);
                        if (cursor2.moveToFirst()) {
                            String ns = cursor2.getString(2);
                            if (statusnets_kot2.toString().equals("ok")) {
                                System.out.println("assigned for kot2 "+ns);
                                printbillsplithome_kot2(name_kot2);
                                Handler handler =  new Handler(KOT_Management.this.getMainLooper());
                                handler.post( new Runnable(){
                                    public void run(){
                                        Toast.makeText(KOT_Management.this, "KOT printed",Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                System.out.println("assigned for kot22 "+ns);
                                String printer_type="";
                                Cursor aallrows = db.rawQuery("SELECT * FROM Printer_type WHERE _id = '1'", null);
                                if (aallrows.moveToFirst()) {
                                    do {
                                        printer_type = aallrows.getString(1);

                                    } while (aallrows.moveToNext());
                                }
                                aallrows.close();

                                if(printer_type.equalsIgnoreCase("wiseposplus")) {

                                    if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO)
                                    {
                                        mPrintData = new ArrayList<>();
                                        byte[] receiptData = neoprintbillsplithome_kot_BT(name_kot2);
                                        mPrintData.add(receiptData);


                                        if(mPrintData != null && mPrintData.size() > 0)
                                        {
                                            mMSWisepadDeviceController.print(mPrintData);
                                        }

                                    }else{
                                        wiseposprintbillsplithome_kot_BT(name_kot2);
                                        Handler handler =  new Handler(KOT_Management.this.getMainLooper());
                                        handler.post( new Runnable(){
                                            public void run(){
                                                Toast.makeText(KOT_Management.this, "KOT printed",Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }


                                }else {
                                    System.out.println("assigned for kot22 " + ns);
                                    printbillsplithome_kot_BT(name_kot2);
                                    Handler handler = new Handler(KOT_Management.this.getMainLooper());
                                    handler.post(new Runnable() {
                                        public void run() {
                                            Toast.makeText(KOT_Management.this, "KOT printed", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        }
                        cursor2.close();

                        Cursor cursor3 = db1.rawQuery("Select * from Table" + ItemIDtable +" WHERE dept_name = '"+name_kot3+"'", null);
                        if (cursor3.moveToFirst()) {
                            String ns = cursor3.getString(2);
                            if (statusnets_kot3.toString().equals("ok")) {
                                System.out.println("assigned for kot3 "+ns);
                                printbillsplithome_kot3(name_kot3);
                                Handler handler =  new Handler(KOT_Management.this.getMainLooper());
                                handler.post( new Runnable(){
                                    public void run(){
                                        Toast.makeText(KOT_Management.this, "KOT printed",Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                System.out.println("assigned for kot33 "+ns);
                                printbillsplithome_kot_BT(name_kot3);
                                Handler handler =  new Handler(KOT_Management.this.getMainLooper());
                                handler.post( new Runnable(){
                                    public void run(){
                                        Toast.makeText(KOT_Management.this, "KOT printed",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                        cursor3.close();

                        Cursor cursor4 = db1.rawQuery("Select * from Table" + ItemIDtable +" WHERE dept_name = '"+name_kot4+"'", null);
                        if (cursor4.moveToFirst()) {
                            String ns = cursor4.getString(2);
                            if (statusnets_kot4.toString().equals("ok")) {
                                System.out.println("assigned for kot4 "+ns);
                                printbillsplithome_kot4(name_kot4);
                                Handler handler =  new Handler(KOT_Management.this.getMainLooper());
                                handler.post( new Runnable(){
                                    public void run(){
                                        Toast.makeText(KOT_Management.this, "KOT printed",Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                System.out.println("assigned for kot44 "+ns);
                                printbillsplithome_kot_BT(name_kot4);
                                Handler handler =  new Handler(KOT_Management.this.getMainLooper());
                                handler.post( new Runnable(){
                                    public void run(){
                                        Toast.makeText(KOT_Management.this, "KOT printed",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                        cursor4.close();

                        Cursor cursor_ns = db1.rawQuery("Select * from Table" + ItemIDtable +" WHERE dept_name = '' OR dept_name IS null", null);
                        if (cursor_ns.moveToFirst()) {
                            String na = cursor_ns.getString(2);
                            System.out.println("not assigned "+na);
                            printbillsplithome();
                            Handler handler =  new Handler(KOT_Management.this.getMainLooper());
                            handler.post( new Runnable(){
                                public void run(){
                                    Toast.makeText(KOT_Management.this, "KOT printed",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        cursor_ns.close();
                    }else {

                    }

                }else {
                    String printer_type="";
                    Cursor aallrows = db.rawQuery("SELECT * FROM Printer_type WHERE _id = '1'", null);
                    if (aallrows.moveToFirst()) {
                        do {
                            printer_type = aallrows.getString(1);

                        } while (aallrows.moveToNext());
                    }
                    aallrows.close();

                    if(printer_type.equalsIgnoreCase("wiseposplus")) {

                        if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO)
                        {
                            mPrintData = new ArrayList<>();
                            byte[] receiptData = neoprintbillsplithome();
                            mPrintData.add(receiptData);


                            if(mPrintData != null && mPrintData.size() > 0)
                            {
                                mMSWisepadDeviceController.print(mPrintData);
                            }

                        }else{

                            wiseposprintbillsplithome();
                            Handler handler =  new Handler(KOT_Management.this.getMainLooper());
                            handler.post( new Runnable(){
                                public void run(){
                                    Toast.makeText(KOT_Management.this, "KOT printed",Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }else {
                        final Dialog dialogconn = new Dialog(KOT_Management.this, R.style.notitle);
                        dialogconn.setContentView(R.layout.dialog_printer_conn_error_kot);

                        Button conti = (Button) dialogconn.findViewById(R.id.ok);
                        conti.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ////Toast.makeText(KOT_Management.this, "checkprinterconncash11", Toast.LENGTH_SHORT).show();
                                dialogconn.dismiss();
                            }
                        });

                        dialogconn.show();
                    }
                }
            }
        });


    }

    private final Handler mHandlera2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
//                    dialogconn.dismiss();
                    String Msg = null;
                    byte[] readBuf = (byte[]) msg.obj;
                    if (D) Log.i(TAG, "readBuf[0]:" + readBuf[0] + "  revBytes:" + revBytes1);

                    Msg = "";
                    for (int i = 0; i < revBytes1; i++) {
                        Msg = Msg + " 0x";
                        Msg = Msg + Integer.toHexString(readBuf[i]);
                    }
//                DisplayToast(Msg);
                    break;
                case MESSAGE_TOAST:
                    break;
            }
        }
    };

    private final Handler mHandlera2_kot1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
//                    dialogconn.dismiss();
                    String Msg = null;
                    byte[] readBuf = (byte[]) msg.obj;
                    if (D) Log.i(TAG, "readBuf[0]:" + readBuf[0] + "  revBytes:" + revBytes1_kot1);

                    Msg = "";
                    for (int i = 0; i < revBytes1_kot1; i++) {
                        Msg = Msg + " 0x";
                        Msg = Msg + Integer.toHexString(readBuf[i]);
                    }
//                DisplayToast(Msg);
                    break;
                case MESSAGE_TOAST:
                    break;
            }
        }
    };

    private final Handler mHandlera2_kot2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
//                    dialogconn.dismiss();
                    String Msg = null;
                    byte[] readBuf = (byte[]) msg.obj;
                    if (D) Log.i(TAG, "readBuf[0]:" + readBuf[0] + "  revBytes:" + revBytes1_kot2);

                    Msg = "";
                    for (int i = 0; i < revBytes1_kot2; i++) {
                        Msg = Msg + " 0x";
                        Msg = Msg + Integer.toHexString(readBuf[i]);
                    }
//                DisplayToast(Msg);
                    break;
                case MESSAGE_TOAST:
                    break;
            }
        }
    };

    private final Handler mHandlera2_kot3 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
//                    dialogconn.dismiss();
                    String Msg = null;
                    byte[] readBuf = (byte[]) msg.obj;
                    if (D) Log.i(TAG, "readBuf[0]:" + readBuf[0] + "  revBytes:" + revBytes1_kot3);

                    Msg = "";
                    for (int i = 0; i < revBytes1_kot3; i++) {
                        Msg = Msg + " 0x";
                        Msg = Msg + Integer.toHexString(readBuf[i]);
                    }
//                DisplayToast(Msg);
                    break;
                case MESSAGE_TOAST:
                    break;
            }
        }
    };

    private final Handler mHandlera2_kot4 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
//                    dialogconn.dismiss();
                    String Msg = null;
                    byte[] readBuf = (byte[]) msg.obj;
                    if (D) Log.i(TAG, "readBuf[0]:" + readBuf[0] + "  revBytes:" + revBytes1_kot4);

                    Msg = "";
                    for (int i = 0; i < revBytes1_kot4; i++) {
                        Msg = Msg + " 0x";
                        Msg = Msg + Integer.toHexString(readBuf[i]);
                    }
//                DisplayToast(Msg);
                    break;
                case MESSAGE_TOAST:
                    break;
            }
        }
    };

    public void displaydata() {
        int one11 = 0;
        Cursor cursor_max = db1.rawQuery("Select MAX(tagg) from Table" + ItemIDtable + "management", null);
        if (cursor_max.moveToFirst()){
            one11 = cursor_max.getInt(0);
        }



        for (int i = one11; i >= 1; i--){

            Cursor cursor = db1.rawQuery("Select * from Table" + ItemIDtable + "management WHERE tagg = '" + i + "' AND itemtype = 'Item' GROUP BY par_id", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (cursor.moveToFirst()) {

                String dat = cursor.getString(4);
                String tim = cursor.getString(5);
                String kot_no = cursor.getString(3);

                // inflate add_view.xml to create new edit text views
                View newView1 = inflater.inflate(R.layout.kot_order_management_tablerows, null);
                TextView dat_t = (TextView) newView1.findViewById(R.id.date);
                final TextView tim_t = (TextView) newView1.findViewById(R.id.time);
                final TextView kot_t = (TextView) newView1.findViewById(R.id.kot_no);
                dat_t.setText(dat);
                tim_t.setText(tim);
                kot_t.setText(kot_no);

                tableLayout.addView(newView1);

                TextView print_again = (TextView) newView1.findViewById(R.id.savetables);
                print_again.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(KOT_Management.this, " "+kot_t.getText().toString(), Toast.LENGTH_LONG).show();


                        Cursor getprint_type = db.rawQuery("SELECT * FROM Printer_text_size", null);
                        if (getprint_type.moveToFirst()) {
                            String type = getprint_type.getString(1);

                            if (type.toString().equals("Standard")) {
//                Toast.makeText(, "selected Standard", Toast.LENGTH_SHORT).show();
                                byte[] LF = {0x0d, 0x0a};

                                allbuftaxestype1 = new byte[][]{
                                        " ".getBytes(), LF
                                };

                            } else {
//                Toast.makeText(getActivity(), "selected Compact", Toast.LENGTH_SHORT).show();
                                byte[] LF = {0x0d, 0x0a};

                                allbuftaxestype1 = new byte[][]{
                                        " ".getBytes(), LF
                                };
                            }
                        }
//                }


                        Cursor connnet = db.rawQuery("SELECT * FROM IPConn", null);
                        if (connnet.moveToFirst()) {
                            ipnameget = connnet.getString(1);
                            portget = connnet.getString(2);
                            statusnet = connnet.getString(3);
                        }

                        Cursor connnet_counter = db.rawQuery("SELECT * FROM IPConn_Counter", null);
                        if (connnet_counter.moveToFirst()) {
                            ipnameget_counter = connnet_counter.getString(1);
                            portget_counter = connnet_counter.getString(2);
                            statusnet_counter = connnet_counter.getString(3);
                        }
                        connnet_counter.close();

                        Cursor conn = db.rawQuery("SELECT * FROM BTConn", null);
                        if (conn.moveToFirst()) {
                            nameget = conn.getString(1);
                            addget = conn.getString(2);
                            statussusb = conn.getString(3);
                        }



                        Cursor c_kot1 = db.rawQuery("SELECT * FROM IPConn_KOT1", null);
                        if (c_kot1.moveToFirst()) {
                            ipnamegets_kot1 = c_kot1.getString(1);
                            portgets_kot1 = c_kot1.getString(2);
                            statusnets_kot1 = c_kot1.getString(3);
                            name_kot1 = c_kot1.getString(5);
                        }
                        c_kot1.close();

                        Cursor c_kot2 = db.rawQuery("SELECT * FROM IPConn_KOT2", null);
                        if (c_kot2.moveToFirst()) {
                            ipnamegets_kot2 = c_kot2.getString(1);
                            portgets_kot2 = c_kot2.getString(2);
                            statusnets_kot2 = c_kot2.getString(3);
                            name_kot2 = c_kot2.getString(5);
                        }
                        c_kot2.close();

                        Cursor c_kot3 = db.rawQuery("SELECT * FROM IPConn_KOT3", null);
                        if (c_kot3.moveToFirst()) {
                            ipnamegets_kot3 = c_kot3.getString(1);
                            portgets_kot3 = c_kot3.getString(2);
                            statusnets_kot3 = c_kot3.getString(3);
                            name_kot3 = c_kot3.getString(5);
                        }
                        c_kot3.close();

                        Cursor c_kot4 = db.rawQuery("SELECT * FROM IPConn_KOT4", null);
                        if (c_kot4.moveToFirst()) {
                            ipnamegets_kot4 = c_kot4.getString(1);
                            portgets_kot4 = c_kot4.getString(2);
                            statusnets_kot4 = c_kot4.getString(3);
                            name_kot4 = c_kot4.getString(5);
                        }
                        c_kot4.close();

                        if (statusnet.toString().equals("ok") || statusnet_counter.toString().equals("ok") || statussusb.toString().equals("ok")
                                || statusnets_kot1.toString().equals("ok") || statusnets_kot2.toString().equals("ok")
                                || statusnets_kot3.toString().equals("ok") || statusnets_kot4.toString().equals("ok")) {






                            printbillsplithome1(kot_t.getText().toString());





                        }else {
                            String printer_type="";
                            Cursor aallrows = db.rawQuery("SELECT * FROM Printer_type WHERE _id = '1'", null);
                            if (aallrows.moveToFirst()) {
                                do {
                                    printer_type = aallrows.getString(1);

                                } while (aallrows.moveToNext());
                            }
                            aallrows.close();

                            if(printer_type.equalsIgnoreCase("wiseposplus")) {

                                if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO)
                                {
                                    mPrintData = new ArrayList<>();
                                    byte[] receiptData = neoprintbillsplithome1(kot_t.getText().toString());
                                    mPrintData.add(receiptData);


                                    if(mPrintData != null && mPrintData.size() > 0)
                                    {
                                        mMSWisepadDeviceController.print(mPrintData);
                                    }

                                }else{

                                    wiseposprintbillsplithome1(kot_t.getText().toString());
                                    Handler handler =  new Handler(KOT_Management.this.getMainLooper());
                                    handler.post( new Runnable(){
                                        public void run(){
                                            Toast.makeText(KOT_Management.this, "KOT printed",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                            }else {
                                final Dialog dialogconn = new Dialog(KOT_Management.this, R.style.notitle);
                                dialogconn.setContentView(R.layout.dialog_printer_conn_error_kot);

                                Button conti = (Button) dialogconn.findViewById(R.id.ok);
                                conti.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                                    Toast.makeText(KOT_Management.this, "checkprinterconncash11", Toast.LENGTH_SHORT).show();
                                        dialogconn.dismiss();
                                    }
                                });

                                dialogconn.show();
                            }
                        }

                    }
                });


                do {

                    View newView = inflater.inflate(R.layout.kot_order_management_tablerows1, null);

                    TextView itemname_t = (TextView) newView.findViewById(R.id.itemname);
                    TextView qty_t = (TextView) newView.findViewById(R.id.quantity);

                    String id = cursor.getString(0);
                    String name = cursor.getString(1);
                    String total = cursor.getString(2);
                    final String hii = cursor.getString(6);

                    itemname_t.setText(String.valueOf(name));
                    qty_t.setText(String.valueOf(total));

                    tableLayout.addView(newView);

                    Cursor modcursor = db1.rawQuery("Select * from Table" + ItemIDtable + "management WHERE par_id = '" + hii + "' AND itemtype = 'Modifier' GROUP BY itemname, par_id, tagg", null);
                    if (modcursor.moveToFirst()) {
                        do {

                            View newView3 = inflater.inflate(R.layout.kot_order_management_tablerows_modi, null);


                            TextView modname = (TextView) newView3.findViewById(R.id.modname);

                            final String modiname = modcursor.getString(1);

                            modname.setVisibility(View.VISIBLE);

                            modname.setText(modiname);

                            tableLayout.addView(newView3);

                        } while (modcursor.moveToNext());

                    }

                    newView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            System.out.println("clicked itemname is "+String.valueOf(name)+" id is "+hii);
                            newView.setBackgroundResource(R.drawable.white_click);

                            dialog = new Dialog(KOT_Management.this, R.style.notitle);
                            dialog.setContentView(R.layout.quantity_bill_pane_cancel);
//                        dialog.setCanceledOnTouchOutside(false);
                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            dialog.show();

                            String qty_home = "", price = "";
                            Cursor cursor1 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE _id = '"+hii+"'", null);
                            if (cursor1.moveToFirst()) {
                                qty_home = cursor1.getString(1);
                                price = cursor1.getString(3);
                            }

                            Button btncancel = (Button) dialog.findViewById(R.id.btncancel);
                            btncancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            RelativeLayout edittext_reason = (RelativeLayout) dialog.findViewById(R.id.edittext_reason);

                            TextView itemname = (TextView) dialog.findViewById(R.id.itemname);
                            itemname.setText(itemname_t.getText().toString());

                            EditText displayquantity = (EditText) dialog.findViewById(R.id.displayquantity);
                            displayquantity.setText(qty_t.getText().toString());

                            displayquantity.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    System.out.println("testwatchertt "+s);
                                    String one = displayquantity.getText().toString();

                                    if (one.toString().equals("")) {
                                        displayquantity.setText("0");
                                    }

                                    if (displayquantity.getText().toString().equals("0") || displayquantity.getText().toString().equals("")) {

                                    } else {
                                        if (displayquantity.getText().toString().equals(".") || displayquantity.getText().toString().equals(".0")) {
                                            displayquantity.setText("0.0");
                                            displayquantity.setSelection(displayquantity.getText().length());
                                        } else {

                                        }
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });

                            EditText add_reason = (EditText) dialog.findViewById(R.id.add_reason);

                            ImageButton quantitydecrease = (ImageButton) dialog.findViewById(R.id.quantitydecrease);
                            quantitydecrease.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    edittext_reason.setVisibility(View.VISIBLE);
                                    int clickcountsss = 0;
                                    float incc = Float.parseFloat(displayquantity.getText().toString());
                                    clickcountsss++;

                                    String newv = String.valueOf(incc - clickcountsss);
                                    float newvvint = Float.parseFloat(newv);

                                    if (newvvint < 0) {

                                    } else {
                                        displayquantity.setText(newv);
                                    }
                                }
                            });

                            if (Float.parseFloat(qty_t.getText().toString()) == 1) {
                                quantitydecrease.setVisibility(GONE);
                            }

                            Button quantityremove = (Button) dialog.findViewById(R.id.quantityremove);
                            String finalQty_home = qty_home;
                            String finalPrice = price;
                            quantityremove.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    edittext_reason.setVisibility(View.VISIBLE);
                                    if (add_reason.getText().toString().equals("") || add_reason.getText().toString().equals(" ") || add_reason.getText().toString().equals("0")) {
                                        add_reason.setError("Enter valid reason");
                                    }else {

                                        final Dialog dialog_warning = new Dialog(KOT_Management.this, R.style.notitle);
                                        dialog_warning.setContentView(R.layout.dialog_kot_cancel_warning);
                                        dialog_warning.show();

                                        Button closecust = (Button) dialog_warning.findViewById(R.id.cancel);
                                        closecust.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog_warning.dismiss();
                                            }
                                        });

                                        Button ok = (Button) dialog_warning.findViewById(R.id.ok);
                                        ok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog_warning.dismiss();

                                                Cursor connnet = db.rawQuery("SELECT * FROM IPConn", null);
                                                if (connnet.moveToFirst()) {
                                                    ipnameget = connnet.getString(1);
                                                    portget = connnet.getString(2);
                                                    statusnet = connnet.getString(3);
                                                }

                                                Cursor connnet_counter = db.rawQuery("SELECT * FROM IPConn_Counter", null);
                                                if (connnet_counter.moveToFirst()) {
                                                    ipnameget_counter = connnet_counter.getString(1);
                                                    portget_counter = connnet_counter.getString(2);
                                                    statusnet_counter = connnet_counter.getString(3);
                                                }
                                                connnet_counter.close();

                                                Cursor conn = db.rawQuery("SELECT * FROM BTConn", null);
                                                if (conn.moveToFirst()) {
                                                    nameget = conn.getString(1);
                                                    addget = conn.getString(2);
                                                    statussusb = conn.getString(3);
                                                }



                                                Cursor c_kot1 = db.rawQuery("SELECT * FROM IPConn_KOT1", null);
                                                if (c_kot1.moveToFirst()) {
                                                    ipnamegets_kot1 = c_kot1.getString(1);
                                                    portgets_kot1 = c_kot1.getString(2);
                                                    statusnets_kot1 = c_kot1.getString(3);
                                                    name_kot1 = c_kot1.getString(5);
                                                }
                                                c_kot1.close();

                                                Cursor c_kot2 = db.rawQuery("SELECT * FROM IPConn_KOT2", null);
                                                if (c_kot2.moveToFirst()) {
                                                    ipnamegets_kot2 = c_kot2.getString(1);
                                                    portgets_kot2 = c_kot2.getString(2);
                                                    statusnets_kot2 = c_kot2.getString(3);
                                                    name_kot2 = c_kot2.getString(5);
                                                }
                                                c_kot2.close();

                                                Cursor c_kot3 = db.rawQuery("SELECT * FROM IPConn_KOT3", null);
                                                if (c_kot3.moveToFirst()) {
                                                    ipnamegets_kot3 = c_kot3.getString(1);
                                                    portgets_kot3 = c_kot3.getString(2);
                                                    statusnets_kot3 = c_kot3.getString(3);
                                                    name_kot3 = c_kot3.getString(5);
                                                }
                                                c_kot3.close();

                                                Cursor c_kot4 = db.rawQuery("SELECT * FROM IPConn_KOT4", null);
                                                if (c_kot4.moveToFirst()) {
                                                    ipnamegets_kot4 = c_kot4.getString(1);
                                                    portgets_kot4 = c_kot4.getString(2);
                                                    statusnets_kot4 = c_kot4.getString(3);
                                                    name_kot4 = c_kot4.getString(5);
                                                }
                                                c_kot4.close();

                                                if (statusnet.toString().equals("ok") || statusnet_counter.toString().equals("ok") || statussusb.toString().equals("ok")
                                                        || statusnets_kot1.toString().equals("ok") || statusnets_kot2.toString().equals("ok")
                                                        || statusnets_kot3.toString().equals("ok") || statusnets_kot4.toString().equals("ok")) {
                                                    edittext_reason.setVisibility(View.VISIBLE);
                                                    dialog.dismiss();

                                                    System.out.println("id is " + hii);

//                                              String where = "_id = '" + hii + "' ";
//                                              db1.delete("Table" + ItemIDtable, where, new String[]{});

                                                    String p_qty = "", upd_qty = "", p_price = "";
                                                    Cursor cursor2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE _id = '" + hii + "'", null);
                                                    if (cursor2.moveToFirst()) {
                                                        p_qty = cursor2.getString(1);
                                                        upd_qty = cursor2.getString(20);
                                                        p_price = cursor2.getString(3);
                                                    }

                                                    String tot1 = "";
                                                    if (Float.parseFloat(qty_t.getText().toString()) == Float.parseFloat(finalQty_home)) {
                                                        String where = "_id = '" + hii + "' ";
                                                        db1.delete("Table" + ItemIDtable, where, new String[]{});

                                                        float tot = Float.parseFloat(finalQty_home) * Float.parseFloat(p_price);
                                                        tot1 = String.format("%.2f", tot);

                                                    } else {

                                                        float a = Float.parseFloat(p_qty) - Float.parseFloat(qty_t.getText().toString());
                                                        float b = Float.parseFloat(p_price) * a;

                                                        float a1 = Float.parseFloat(upd_qty) - Float.parseFloat(qty_t.getText().toString());

                                                        ContentValues cv = new ContentValues();
                                                        cv.put("quantity", String.format("%.1f", a));
                                                        cv.put("total", String.format("%.2f", b));
                                                        cv.put("newtotal", String.format("%.2f", b));
                                                        cv.put("updated_quantity", String.format("%.2f", a1));
                                                        String where = "_id = '" + hii + "' ";
                                                        db1.update("Table" + ItemIDtable, cv, where, new String[]{});

                                                        float tot = Float.parseFloat(finalQty_home) * Float.parseFloat(p_price);
                                                        tot1 = String.format("%.2f", tot);

                                                    }


                                                    String where1 = "_id = '" + id + "' ";
//                                        db1.delete("Table" + ItemIDtable, where1, new String[]{});
                                                    db1.delete("Table" + ItemIDtable + "management", where1, null);

                                                    String q = "0", _id = "0";
                                                    if (itemname_t.getText().toString().contains("(")) {
                                                        String match = "(";
                                                        int position = itemname_t.getText().toString().indexOf(match);
                                                        String itemnameget1 = itemname_t.getText().toString().substring(0, position);
                                                        Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + itemnameget1 + "'", null);
                                                        if (cursor3.moveToFirst()) {
                                                            _id = cursor3.getString(0);
                                                            q = cursor3.getString(3);
                                                        }
                                                    } else {
                                                        Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + itemname_t.getText().toString() + "'", null);
                                                        if (cursor3.moveToFirst()) {
                                                            _id = cursor3.getString(0);
                                                            q = cursor3.getString(3);
                                                        }
                                                    }

                                                    float item_content10 = Float.parseFloat(q) + Float.parseFloat(qty_t.getText().toString());

                                                    ContentValues newValues = new ContentValues();
                                                    newValues.put("stockquan", item_content10);
                                                    String where = "_id = '" + _id + "'";

                                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                    getContentResolver().update(contentUri, newValues, where, new String[]{});
                                                    resultUri = new Uri.Builder()
                                                            .scheme("content")
                                                            .authority(StubProviderApp.AUTHORITY)
                                                            .path("Items")
                                                            .appendQueryParameter("operation", "update")
                                                            .appendQueryParameter("_id", _id)
                                                            .build();
                                                    getContentResolver().notifyChange(resultUri, null);

                                                    String where1_v1 = "docid = ?";
                                                    db.update("Items_Virtual", newValues, where1_v1, new String[]{_id});


                                                    tableLayout.removeAllViews();
                                                    displaydata();

                                                    kotcanceldata(hii, itemname.getText().toString(), qty_t.getText().toString(), ItemIDtable, add_reason.getText().toString(), p_price, tot1);

                                                }else {
                                                    String printer_type="";
                                                    Cursor aallrows = db.rawQuery("SELECT * FROM Printer_type WHERE _id = '1'", null);
                                                    if (aallrows.moveToFirst()) {
                                                        do {
                                                            printer_type = aallrows.getString(1);

                                                        } while (aallrows.moveToNext());
                                                    }
                                                    aallrows.close();

                                                    if(printer_type.equalsIgnoreCase("wiseposplus")) {

                                                        if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO)
                                                        {
                                                            mPrintData = new ArrayList<>();

                                                            dialog.dismiss();

                                                            System.out.println("id is " + hii);

//                                              String where = "_id = '" + hii + "' ";
//                                              db1.delete("Table" + ItemIDtable, where, new String[]{});

                                                            String p_qty = "", upd_qty = "", p_price = "";
                                                            Cursor cursor2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE _id = '" + hii + "'", null);
                                                            if (cursor2.moveToFirst()) {
                                                                p_qty = cursor2.getString(1);
                                                                upd_qty = cursor2.getString(20);
                                                                p_price = cursor2.getString(3);
                                                            }

                                                            String tot1 = "";
                                                            if (Float.parseFloat(qty_t.getText().toString()) == Float.parseFloat(finalQty_home)) {
                                                                String where = "_id = '" + hii + "' ";
                                                                db1.delete("Table" + ItemIDtable, where, new String[]{});

                                                                float tot = Float.parseFloat(finalQty_home) * Float.parseFloat(p_price);
                                                                tot1 = String.format("%.2f", tot);

                                                            } else {

                                                                float a = Float.parseFloat(p_qty) - Float.parseFloat(qty_t.getText().toString());
                                                                float b = Float.parseFloat(p_price) * a;

                                                                float a1 = Float.parseFloat(upd_qty) - Float.parseFloat(qty_t.getText().toString());

                                                                ContentValues cv = new ContentValues();
                                                                cv.put("quantity", String.format("%.1f", a));
                                                                cv.put("total", String.format("%.2f", b));
                                                                cv.put("newtotal", String.format("%.2f", b));
                                                                cv.put("updated_quantity", String.format("%.2f", a1));
                                                                String where = "_id = '" + hii + "' ";
                                                                db1.update("Table" + ItemIDtable, cv, where, new String[]{});

                                                                float tot = Float.parseFloat(finalQty_home) * Float.parseFloat(p_price);
                                                                tot1 = String.format("%.2f", tot);

                                                            }


                                                            String where1 = "_id = '" + id + "' ";
//                                        db1.delete("Table" + ItemIDtable, where1, new String[]{});
                                                            db1.delete("Table" + ItemIDtable + "management", where1, null);

                                                            String q = "0", _id = "0";
                                                            if (itemname_t.getText().toString().contains("(")) {
                                                                String match = "(";
                                                                int position = itemname_t.getText().toString().indexOf(match);
                                                                String itemnameget1 = itemname_t.getText().toString().substring(0, position);
                                                                Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + itemnameget1 + "'", null);
                                                                if (cursor3.moveToFirst()) {
                                                                    _id = cursor3.getString(0);
                                                                    q = cursor3.getString(3);
                                                                }
                                                            } else {
                                                                Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + itemname_t.getText().toString() + "'", null);
                                                                if (cursor3.moveToFirst()) {
                                                                    _id = cursor3.getString(0);
                                                                    q = cursor3.getString(3);
                                                                }
                                                            }

                                                            float item_content10 = Float.parseFloat(q) + Float.parseFloat(qty_t.getText().toString());

                                                            ContentValues newValues = new ContentValues();
                                                            newValues.put("stockquan", item_content10);
                                                            String where = "_id = '" + _id + "'";

                                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                            getContentResolver().update(contentUri, newValues, where, new String[]{});
                                                            resultUri = new Uri.Builder()
                                                                    .scheme("content")
                                                                    .authority(StubProviderApp.AUTHORITY)
                                                                    .path("Items")
                                                                    .appendQueryParameter("operation", "update")
                                                                    .appendQueryParameter("_id", _id)
                                                                    .build();
                                                            getContentResolver().notifyChange(resultUri, null);

                                                            String where1_v1 = "docid = ?";
                                                            db.update("Items_Virtual", newValues, where1_v1, new String[]{_id});


                                                            tableLayout.removeAllViews();
                                                            displaydata();



                                                            kotcanceldata(hii, itemname.getText().toString(), qty_t.getText().toString(), ItemIDtable, add_reason.getText().toString(), p_price, tot1);



                                                            if(mPrintData != null && mPrintData.size() > 0)
                                                            {
                                                                mMSWisepadDeviceController.print(mPrintData);
                                                            }

                                                        }else{

                                                            dialog.dismiss();

                                                            System.out.println("id is " + hii);

//                                              String where = "_id = '" + hii + "' ";
//                                              db1.delete("Table" + ItemIDtable, where, new String[]{});

                                                            String p_qty = "", upd_qty = "", p_price = "";
                                                            Cursor cursor2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE _id = '" + hii + "'", null);
                                                            if (cursor2.moveToFirst()) {
                                                                p_qty = cursor2.getString(1);
                                                                upd_qty = cursor2.getString(20);
                                                                p_price = cursor2.getString(3);
                                                            }

                                                            String tot1 = "";
                                                            if (Float.parseFloat(qty_t.getText().toString()) == Float.parseFloat(finalQty_home)) {
                                                                String where = "_id = '" + hii + "' ";
                                                                db1.delete("Table" + ItemIDtable, where, new String[]{});

                                                                float tot = Float.parseFloat(finalQty_home) * Float.parseFloat(p_price);
                                                                tot1 = String.format("%.2f", tot);

                                                            } else {

                                                                float a = Float.parseFloat(p_qty) - Float.parseFloat(qty_t.getText().toString());
                                                                float b = Float.parseFloat(p_price) * a;

                                                                float a1 = Float.parseFloat(upd_qty) - Float.parseFloat(qty_t.getText().toString());

                                                                ContentValues cv = new ContentValues();
                                                                cv.put("quantity", String.format("%.1f", a));
                                                                cv.put("total", String.format("%.2f", b));
                                                                cv.put("newtotal", String.format("%.2f", b));
                                                                cv.put("updated_quantity", String.format("%.2f", a1));
                                                                String where = "_id = '" + hii + "' ";
                                                                db1.update("Table" + ItemIDtable, cv, where, new String[]{});

                                                                float tot = Float.parseFloat(finalQty_home) * Float.parseFloat(p_price);
                                                                tot1 = String.format("%.2f", tot);

                                                            }


                                                            String where1 = "_id = '" + id + "' ";
//                                        db1.delete("Table" + ItemIDtable, where1, new String[]{});
                                                            db1.delete("Table" + ItemIDtable + "management", where1, null);

                                                            String q = "0", _id = "0";
                                                            if (itemname_t.getText().toString().contains("(")) {
                                                                String match = "(";
                                                                int position = itemname_t.getText().toString().indexOf(match);
                                                                String itemnameget1 = itemname_t.getText().toString().substring(0, position);
                                                                Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + itemnameget1 + "'", null);
                                                                if (cursor3.moveToFirst()) {
                                                                    _id = cursor3.getString(0);
                                                                    q = cursor3.getString(3);
                                                                }
                                                            } else {
                                                                Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + itemname_t.getText().toString() + "'", null);
                                                                if (cursor3.moveToFirst()) {
                                                                    _id = cursor3.getString(0);
                                                                    q = cursor3.getString(3);
                                                                }
                                                            }

                                                            float item_content10 = Float.parseFloat(q) + Float.parseFloat(qty_t.getText().toString());

                                                            ContentValues newValues = new ContentValues();
                                                            newValues.put("stockquan", item_content10);
                                                            String where = "_id = '" + _id + "'";

                                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                            getContentResolver().update(contentUri, newValues, where, new String[]{});
                                                            resultUri = new Uri.Builder()
                                                                    .scheme("content")
                                                                    .authority(StubProviderApp.AUTHORITY)
                                                                    .path("Items")
                                                                    .appendQueryParameter("operation", "update")
                                                                    .appendQueryParameter("_id", _id)
                                                                    .build();
                                                            getContentResolver().notifyChange(resultUri, null);

                                                            String where1_v1 = "docid = ?";
                                                            db.update("Items_Virtual", newValues, where1_v1, new String[]{_id});


                                                            tableLayout.removeAllViews();
                                                            displaydata();



                                                            kotcanceldata(hii, itemname.getText().toString(), qty_t.getText().toString(), ItemIDtable, add_reason.getText().toString(), p_price, tot1);
                                                            Handler handler =  new Handler(KOT_Management.this.getMainLooper());
                                                            handler.post( new Runnable(){
                                                                public void run(){
                                                                    Toast.makeText(KOT_Management.this, "KOT printed",Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                        }

                                                    }else {
                                                        edittext_reason.setVisibility(View.VISIBLE);
                                                        final Dialog dialogconn = new Dialog(KOT_Management.this, R.style.notitle);
                                                        dialogconn.setContentView(R.layout.dialog_printer_conn_error_kot1);
                                                        dialogconn.show();

                                                        Button closecust = (Button) dialogconn.findViewById(R.id.close);
                                                        closecust.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                dialogconn.dismiss();
                                                            }
                                                        });

                                                        Button conti = (Button) dialogconn.findViewById(R.id.ok);
                                                        conti.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                dialogconn.dismiss();
                                                                dialog.dismiss();

                                                                System.out.println("id is " + hii);

//                                              String where = "_id = '" + hii + "' ";
//                                              db1.delete("Table" + ItemIDtable, where, new String[]{});

                                                                String p_qty = "", upd_qty = "", p_price = "";
                                                                Cursor cursor2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE _id = '" + hii + "'", null);
                                                                if (cursor2.moveToFirst()) {
                                                                    p_qty = cursor2.getString(1);
                                                                    upd_qty = cursor2.getString(20);
                                                                    p_price = cursor2.getString(3);
                                                                }

                                                                String tot1 = "";
                                                                if (Float.parseFloat(qty_t.getText().toString()) == Float.parseFloat(finalQty_home)) {
                                                                    String where = "_id = '" + hii + "' ";
                                                                    db1.delete("Table" + ItemIDtable, where, new String[]{});

                                                                    float tot = Float.parseFloat(finalQty_home) * Float.parseFloat(p_price);
                                                                    tot1 = String.format("%.2f", tot);

                                                                } else {

                                                                    float a = Float.parseFloat(p_qty) - Float.parseFloat(qty_t.getText().toString());
                                                                    float b = Float.parseFloat(p_price) * a;

                                                                    float a1 = Float.parseFloat(upd_qty) - Float.parseFloat(qty_t.getText().toString());

                                                                    ContentValues cv = new ContentValues();
                                                                    cv.put("quantity", String.format("%.1f", a));
                                                                    cv.put("total", String.format("%.2f", b));
                                                                    cv.put("newtotal", String.format("%.2f", b));
                                                                    cv.put("updated_quantity", String.format("%.2f", a1));
                                                                    String where = "_id = '" + hii + "' ";
                                                                    db1.update("Table" + ItemIDtable, cv, where, new String[]{});

                                                                    float tot = Float.parseFloat(finalQty_home) * Float.parseFloat(p_price);
                                                                    tot1 = String.format("%.2f", tot);

                                                                }


                                                                String where1 = "_id = '" + id + "' ";
//                                        db1.delete("Table" + ItemIDtable, where1, new String[]{});
                                                                db1.delete("Table" + ItemIDtable + "management", where1, null);

                                                                String q = "0", _id = "0";
                                                                if (itemname_t.getText().toString().contains("(")) {
                                                                    String match = "(";
                                                                    int position = itemname_t.getText().toString().indexOf(match);
                                                                    String itemnameget1 = itemname_t.getText().toString().substring(0, position);
                                                                    Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + itemnameget1 + "'", null);
                                                                    if (cursor3.moveToFirst()) {
                                                                        _id = cursor3.getString(0);
                                                                        q = cursor3.getString(3);
                                                                    }
                                                                } else {
                                                                    Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + itemname_t.getText().toString() + "'", null);
                                                                    if (cursor3.moveToFirst()) {
                                                                        _id = cursor3.getString(0);
                                                                        q = cursor3.getString(3);
                                                                    }
                                                                }

                                                                float item_content10 = Float.parseFloat(q) + Float.parseFloat(qty_t.getText().toString());

                                                                ContentValues newValues = new ContentValues();
                                                                newValues.put("stockquan", item_content10);
                                                                String where = "_id = '" + _id + "'";

                                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                                getContentResolver().update(contentUri, newValues, where, new String[]{});
                                                                resultUri = new Uri.Builder()
                                                                        .scheme("content")
                                                                        .authority(StubProviderApp.AUTHORITY)
                                                                        .path("Items")
                                                                        .appendQueryParameter("operation", "update")
                                                                        .appendQueryParameter("_id", _id)
                                                                        .build();
                                                                getContentResolver().notifyChange(resultUri, null);

                                                                String where1_v1 = "docid = ?";
                                                                db.update("Items_Virtual", newValues, where1_v1, new String[]{_id});


                                                                tableLayout.removeAllViews();
                                                                displaydata();

                                                                kotcanceldata(hii, itemname.getText().toString(), qty_t.getText().toString(), ItemIDtable, add_reason.getText().toString(), p_price, tot1);
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        });

                                    }

                                }
                            });

                            Button btnsave = (Button) dialog.findViewById(R.id.btnsave);
                            btnsave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (Float.parseFloat(displayquantity.getText().toString()) == Float.parseFloat(qty_t.getText().toString())) {
                                        Toast.makeText(KOT_Management.this, "no reduce", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    }else {
                                        if (add_reason.getText().toString().equals("") || add_reason.getText().toString().equals(" ") || add_reason.getText().toString().equals("0")) {
                                            add_reason.setError("Enter valid reason");
                                        } else {
                                            if (displayquantity.getText().toString().equals("0") || displayquantity.getText().toString().equals("")
                                                    || displayquantity.getText().toString().equals("0.") || displayquantity.getText().toString().equals("0.0")) {
                                                displayquantity.setError("Enter valid number");
                                            } else {
                                                if (Float.parseFloat(displayquantity.getText().toString()) > Float.parseFloat(qty_t.getText().toString())) {
                                                    displayquantity.setError("Enter less than " + (Float.parseFloat(qty_t.getText().toString()) + 1));
                                                } else {
                                                    Toast.makeText(KOT_Management.this, "quantity is " + displayquantity.getText().toString(), Toast.LENGTH_LONG).show();

                                                    if (Float.parseFloat(displayquantity.getText().toString()) == Float.parseFloat(qty_t.getText().toString())) {
                                                        Toast.makeText(KOT_Management.this, "no reduce", Toast.LENGTH_LONG).show();
                                                        dialog.dismiss();
                                                    } else {

                                                        final Dialog dialog_warning = new Dialog(KOT_Management.this, R.style.notitle);
                                                        dialog_warning.setContentView(R.layout.dialog_kot_cancel_warning);
                                                        dialog_warning.show();

                                                        Button closecust = (Button) dialog_warning.findViewById(R.id.cancel);
                                                        closecust.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                dialog_warning.dismiss();
                                                            }
                                                        });

                                                        Button ok = (Button) dialog_warning.findViewById(R.id.ok);
                                                        ok.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                dialog_warning.dismiss();

                                                                Cursor connnet = db.rawQuery("SELECT * FROM IPConn", null);
                                                                if (connnet.moveToFirst()) {
                                                                    ipnameget = connnet.getString(1);
                                                                    portget = connnet.getString(2);
                                                                    statusnet = connnet.getString(3);
                                                                }

                                                                Cursor connnet_counter = db.rawQuery("SELECT * FROM IPConn_Counter", null);
                                                                if (connnet_counter.moveToFirst()) {
                                                                    ipnameget_counter = connnet_counter.getString(1);
                                                                    portget_counter = connnet_counter.getString(2);
                                                                    statusnet_counter = connnet_counter.getString(3);
                                                                }
                                                                connnet_counter.close();

                                                                Cursor conn = db.rawQuery("SELECT * FROM BTConn", null);
                                                                if (conn.moveToFirst()) {
                                                                    nameget = conn.getString(1);
                                                                    addget = conn.getString(2);
                                                                    statussusb = conn.getString(3);
                                                                }



                                                                Cursor c_kot1 = db.rawQuery("SELECT * FROM IPConn_KOT1", null);
                                                                if (c_kot1.moveToFirst()) {
                                                                    ipnamegets_kot1 = c_kot1.getString(1);
                                                                    portgets_kot1 = c_kot1.getString(2);
                                                                    statusnets_kot1 = c_kot1.getString(3);
                                                                    name_kot1 = c_kot1.getString(5);
                                                                }
                                                                c_kot1.close();

                                                                Cursor c_kot2 = db.rawQuery("SELECT * FROM IPConn_KOT2", null);
                                                                if (c_kot2.moveToFirst()) {
                                                                    ipnamegets_kot2 = c_kot2.getString(1);
                                                                    portgets_kot2 = c_kot2.getString(2);
                                                                    statusnets_kot2 = c_kot2.getString(3);
                                                                    name_kot2 = c_kot2.getString(5);
                                                                }
                                                                c_kot2.close();

                                                                Cursor c_kot3 = db.rawQuery("SELECT * FROM IPConn_KOT3", null);
                                                                if (c_kot3.moveToFirst()) {
                                                                    ipnamegets_kot3 = c_kot3.getString(1);
                                                                    portgets_kot3 = c_kot3.getString(2);
                                                                    statusnets_kot3 = c_kot3.getString(3);
                                                                    name_kot3 = c_kot3.getString(5);
                                                                }
                                                                c_kot3.close();

                                                                Cursor c_kot4 = db.rawQuery("SELECT * FROM IPConn_KOT4", null);
                                                                if (c_kot4.moveToFirst()) {
                                                                    ipnamegets_kot4 = c_kot4.getString(1);
                                                                    portgets_kot4 = c_kot4.getString(2);
                                                                    statusnets_kot4 = c_kot4.getString(3);
                                                                    name_kot4 = c_kot4.getString(5);
                                                                }
                                                                c_kot4.close();

                                                                if (statusnet.toString().equals("ok") || statusnet_counter.toString().equals("ok") || statussusb.toString().equals("ok")
                                                                        || statusnets_kot1.toString().equals("ok") || statusnets_kot2.toString().equals("ok")
                                                                        || statusnets_kot3.toString().equals("ok") || statusnets_kot4.toString().equals("ok")) {

                                                                    Toast.makeText(KOT_Management.this, "partial cancel", Toast.LENGTH_LONG).show();

                                                                    float qw = Float.parseFloat(displayquantity.getText().toString());
                                                                    ContentValues contentValues = new ContentValues();
                                                                    contentValues.put("qty", String.format("%.2f", qw));
                                                                    String where1 = "_id = '" + id + "' ";
                                                                    db1.update("Table" + ItemIDtable + "management", contentValues, where1, new String[]{});


                                                                    String p_qty = "", upd_qty = "", p_price = "";
                                                                    Cursor cursor2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE _id = '" + hii + "'", null);
                                                                    if (cursor2.moveToFirst()) {
                                                                        p_qty = cursor2.getString(1);
                                                                        upd_qty = cursor2.getString(20);
                                                                        p_price = cursor2.getString(3);
                                                                    }

                                                                    float reduce = Float.parseFloat(qty_t.getText().toString()) - Float.parseFloat(displayquantity.getText().toString());
                                                                    float a = Float.parseFloat(p_qty) - reduce;
                                                                    float b = a * Float.parseFloat(p_price);

                                                                    float a1 = Float.parseFloat(upd_qty) - reduce;

                                                                    ContentValues cv = new ContentValues();
                                                                    cv.put("quantity", String.format("%.1f", a));
                                                                    cv.put("total", String.format("%.2f", b));
                                                                    cv.put("newtotal", String.format("%.2f", b));
                                                                    cv.put("updated_quantity", String.format("%.2f", a1));
                                                                    cv.put("status", "print");
                                                                    String where = "_id = '" + hii + "' ";
                                                                    db1.update("Table" + ItemIDtable, cv, where, new String[]{});

                                                                    float tot = reduce * Float.parseFloat(p_price);
                                                                    String tot1 = String.format("%.2f", tot);

                                                                    String q = "0", _id = "0";
                                                                    if (itemname_t.getText().toString().contains("(")) {
                                                                        String match = "(";
                                                                        int position = itemname_t.getText().toString().indexOf(match);
                                                                        String itemnameget1 = itemname_t.getText().toString().substring(0, position);
                                                                        Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + itemnameget1 + "'", null);
                                                                        if (cursor3.moveToFirst()) {
                                                                            _id = cursor3.getString(0);
                                                                            q = cursor3.getString(3);
                                                                        }
                                                                    } else {
                                                                        Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + itemname_t.getText().toString() + "'", null);
                                                                        if (cursor3.moveToFirst()) {
                                                                            _id = cursor3.getString(0);
                                                                            q = cursor3.getString(3);
                                                                        }
                                                                    }

                                                                    float item_content10 = Float.parseFloat(q) + reduce;

                                                                    ContentValues newValues = new ContentValues();
                                                                    newValues.put("stockquan", item_content10);
                                                                    String where3 = "_id = '" + _id + "'";

                                                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                                    getContentResolver().update(contentUri, newValues, where3, new String[]{});
                                                                    resultUri = new Uri.Builder()
                                                                            .scheme("content")
                                                                            .authority(StubProviderApp.AUTHORITY)
                                                                            .path("Items")
                                                                            .appendQueryParameter("operation", "update")
                                                                            .appendQueryParameter("_id", _id)
                                                                            .build();
                                                                    getContentResolver().notifyChange(resultUri, null);

                                                                    String where1_v1 = "docid = ?";
                                                                    db.update("Items_Virtual", newValues, where1_v1, new String[]{_id});

                                                                    dialog.dismiss();

                                                                    tableLayout.removeAllViews();
                                                                    displaydata();

                                                                    kotcanceldata(hii, itemname.getText().toString(), String.format("%.1f", reduce), ItemIDtable, add_reason.getText().toString(), p_price, tot1);

                                                                } else {

                                                                    String printer_type="";
                                                                    Cursor aallrows = db.rawQuery("SELECT * FROM Printer_type WHERE _id = '1'", null);
                                                                    if (aallrows.moveToFirst()) {
                                                                        do {
                                                                            printer_type = aallrows.getString(1);

                                                                        } while (aallrows.moveToNext());
                                                                    }
                                                                    aallrows.close();

                                                                    if(printer_type.equalsIgnoreCase("wiseposplus")) {

                                                                        if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO)
                                                                        {
                                                                            mPrintData = new ArrayList<>();

                                                                            dialog.dismiss();

                                                                            float qw = Float.parseFloat(displayquantity.getText().toString());
                                                                            ContentValues contentValues = new ContentValues();
                                                                            contentValues.put("qty", String.format("%.2f", qw));
                                                                            String where1 = "_id = '" + id + "' ";
                                                                            db1.update("Table" + ItemIDtable + "management", contentValues, where1, new String[]{});


                                                                            String p_qty = "", upd_qty = "", p_price = "";
                                                                            Cursor cursor2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE _id = '" + hii + "'", null);
                                                                            if (cursor2.moveToFirst()) {
                                                                                p_qty = cursor2.getString(1);
                                                                                upd_qty = cursor2.getString(20);
                                                                                p_price = cursor2.getString(3);
                                                                            }

                                                                            float reduce = Float.parseFloat(qty_t.getText().toString()) - Float.parseFloat(displayquantity.getText().toString());
                                                                            float a = Float.parseFloat(p_qty) - reduce;
                                                                            float b = a * Float.parseFloat(p_price);

                                                                            float a1 = Float.parseFloat(upd_qty) - reduce;

                                                                            ContentValues cv = new ContentValues();
                                                                            cv.put("quantity", String.format("%.1f", a));
                                                                            cv.put("total", String.format("%.2f", b));
                                                                            cv.put("newtotal", String.format("%.2f", b));
                                                                            cv.put("updated_quantity", String.format("%.2f", a1));
                                                                            cv.put("status", "print");
                                                                            String where = "_id = '" + hii + "' ";
                                                                            db1.update("Table" + ItemIDtable, cv, where, new String[]{});

                                                                            float tot = reduce * Float.parseFloat(p_price);
                                                                            String tot1 = String.format("%.2f", tot);

                                                                            String q = "0", _id = "0";
                                                                            if (itemname_t.getText().toString().contains("(")) {
                                                                                String match = "(";
                                                                                int position = itemname_t.getText().toString().indexOf(match);
                                                                                String itemnameget1 = itemname_t.getText().toString().substring(0, position);
                                                                                Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + itemnameget1 + "'", null);
                                                                                if (cursor3.moveToFirst()) {
                                                                                    _id = cursor3.getString(0);
                                                                                    q = cursor3.getString(3);
                                                                                }
                                                                            } else {
                                                                                Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + itemname_t.getText().toString() + "'", null);
                                                                                if (cursor3.moveToFirst()) {
                                                                                    _id = cursor3.getString(0);
                                                                                    q = cursor3.getString(3);
                                                                                }
                                                                            }

                                                                            float item_content10 = Float.parseFloat(q) + reduce;

                                                                            ContentValues newValues = new ContentValues();
                                                                            newValues.put("stockquan", item_content10);
                                                                            String where3 = "_id = '" + _id + "'";

                                                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                                            getContentResolver().update(contentUri, newValues, where3, new String[]{});
                                                                            resultUri = new Uri.Builder()
                                                                                    .scheme("content")
                                                                                    .authority(StubProviderApp.AUTHORITY)
                                                                                    .path("Items")
                                                                                    .appendQueryParameter("operation", "update")
                                                                                    .appendQueryParameter("_id", _id)
                                                                                    .build();
                                                                            getContentResolver().notifyChange(resultUri, null);

                                                                            String where1_v1 = "docid = ?";
                                                                            db.update("Items_Virtual", newValues, where1_v1, new String[]{_id});

                                                                            dialog.dismiss();

                                                                            tableLayout.removeAllViews();
                                                                            displaydata();

                                                                            kotcanceldata(hii, itemname.getText().toString(), String.format("%.1f", reduce), ItemIDtable, add_reason.getText().toString(), p_price, tot1);



                                                                            if(mPrintData != null && mPrintData.size() > 0)
                                                                            {
                                                                                mMSWisepadDeviceController.print(mPrintData);
                                                                            }

                                                                        }else{

                                                                            dialog.dismiss();

                                                                            float qw = Float.parseFloat(displayquantity.getText().toString());
                                                                            ContentValues contentValues = new ContentValues();
                                                                            contentValues.put("qty", String.format("%.2f", qw));
                                                                            String where1 = "_id = '" + id + "' ";
                                                                            db1.update("Table" + ItemIDtable + "management", contentValues, where1, new String[]{});


                                                                            String p_qty = "", upd_qty = "", p_price = "";
                                                                            Cursor cursor2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE _id = '" + hii + "'", null);
                                                                            if (cursor2.moveToFirst()) {
                                                                                p_qty = cursor2.getString(1);
                                                                                upd_qty = cursor2.getString(20);
                                                                                p_price = cursor2.getString(3);
                                                                            }

                                                                            float reduce = Float.parseFloat(qty_t.getText().toString()) - Float.parseFloat(displayquantity.getText().toString());
                                                                            float a = Float.parseFloat(p_qty) - reduce;
                                                                            float b = a * Float.parseFloat(p_price);

                                                                            float a1 = Float.parseFloat(upd_qty) - reduce;

                                                                            ContentValues cv = new ContentValues();
                                                                            cv.put("quantity", String.format("%.1f", a));
                                                                            cv.put("total", String.format("%.2f", b));
                                                                            cv.put("newtotal", String.format("%.2f", b));
                                                                            cv.put("updated_quantity", String.format("%.2f", a1));
                                                                            cv.put("status", "print");
                                                                            String where = "_id = '" + hii + "' ";
                                                                            db1.update("Table" + ItemIDtable, cv, where, new String[]{});

                                                                            float tot = reduce * Float.parseFloat(p_price);
                                                                            String tot1 = String.format("%.2f", tot);

                                                                            String q = "0", _id = "0";
                                                                            if (itemname_t.getText().toString().contains("(")) {
                                                                                String match = "(";
                                                                                int position = itemname_t.getText().toString().indexOf(match);
                                                                                String itemnameget1 = itemname_t.getText().toString().substring(0, position);
                                                                                Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + itemnameget1 + "'", null);
                                                                                if (cursor3.moveToFirst()) {
                                                                                    _id = cursor3.getString(0);
                                                                                    q = cursor3.getString(3);
                                                                                }
                                                                            } else {
                                                                                Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + itemname_t.getText().toString() + "'", null);
                                                                                if (cursor3.moveToFirst()) {
                                                                                    _id = cursor3.getString(0);
                                                                                    q = cursor3.getString(3);
                                                                                }
                                                                            }

                                                                            float item_content10 = Float.parseFloat(q) + reduce;

                                                                            ContentValues newValues = new ContentValues();
                                                                            newValues.put("stockquan", item_content10);
                                                                            String where3 = "_id = '" + _id + "'";

                                                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                                            getContentResolver().update(contentUri, newValues, where3, new String[]{});
                                                                            resultUri = new Uri.Builder()
                                                                                    .scheme("content")
                                                                                    .authority(StubProviderApp.AUTHORITY)
                                                                                    .path("Items")
                                                                                    .appendQueryParameter("operation", "update")
                                                                                    .appendQueryParameter("_id", _id)
                                                                                    .build();
                                                                            getContentResolver().notifyChange(resultUri, null);

                                                                            String where1_v1 = "docid = ?";
                                                                            db.update("Items_Virtual", newValues, where1_v1, new String[]{_id});

                                                                            dialog.dismiss();

                                                                            tableLayout.removeAllViews();
                                                                            displaydata();

                                                                            kotcanceldata(hii, itemname.getText().toString(), String.format("%.1f", reduce), ItemIDtable, add_reason.getText().toString(), p_price, tot1);

                                                                            Handler handler =  new Handler(KOT_Management.this.getMainLooper());
                                                                            handler.post( new Runnable(){
                                                                                public void run(){
                                                                                    Toast.makeText(KOT_Management.this, "KOT printed",Toast.LENGTH_LONG).show();
                                                                                }
                                                                            });
                                                                        }

                                                                    }else {

                                                                        final Dialog dialogconn = new Dialog(KOT_Management.this, R.style.notitle);
                                                                        dialogconn.setContentView(R.layout.dialog_printer_conn_error_kot1);
                                                                        dialogconn.show();

                                                                        Button closecust = (Button) dialogconn.findViewById(R.id.close);
                                                                        closecust.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                dialogconn.dismiss();
                                                                            }
                                                                        });

                                                                        Button conti = (Button) dialogconn.findViewById(R.id.ok);
                                                                        conti.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View view) {
                                                                                dialogconn.dismiss();
//                                                                Toast.makeText(KOT_Management.this, "partial cancel", Toast.LENGTH_LONG).show();

                                                                                float qw = Float.parseFloat(displayquantity.getText().toString());
                                                                                ContentValues contentValues = new ContentValues();
                                                                                contentValues.put("qty", String.format("%.2f", qw));
                                                                                String where1 = "_id = '" + id + "' ";
                                                                                db1.update("Table" + ItemIDtable + "management", contentValues, where1, new String[]{});


                                                                                String p_qty = "", upd_qty = "", p_price = "";
                                                                                Cursor cursor2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE _id = '" + hii + "'", null);
                                                                                if (cursor2.moveToFirst()) {
                                                                                    p_qty = cursor2.getString(1);
                                                                                    upd_qty = cursor2.getString(20);
                                                                                    p_price = cursor2.getString(3);
                                                                                }

                                                                                float reduce = Float.parseFloat(qty_t.getText().toString()) - Float.parseFloat(displayquantity.getText().toString());
                                                                                float a = Float.parseFloat(p_qty) - reduce;
                                                                                float b = a * Float.parseFloat(p_price);

                                                                                float a1 = Float.parseFloat(upd_qty) - reduce;

                                                                                ContentValues cv = new ContentValues();
                                                                                cv.put("quantity", String.format("%.1f", a));
                                                                                cv.put("total", String.format("%.2f", b));
                                                                                cv.put("newtotal", String.format("%.2f", b));
                                                                                cv.put("updated_quantity", String.format("%.2f", a1));
                                                                                cv.put("status", "print");
                                                                                String where = "_id = '" + hii + "' ";
                                                                                db1.update("Table" + ItemIDtable, cv, where, new String[]{});

                                                                                float tot = reduce * Float.parseFloat(p_price);
                                                                                String tot1 = String.format("%.2f", tot);

                                                                                String q = "0", _id = "0";
                                                                                if (itemname_t.getText().toString().contains("(")) {
                                                                                    String match = "(";
                                                                                    int position = itemname_t.getText().toString().indexOf(match);
                                                                                    String itemnameget1 = itemname_t.getText().toString().substring(0, position);
                                                                                    Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + itemnameget1 + "'", null);
                                                                                    if (cursor3.moveToFirst()) {
                                                                                        _id = cursor3.getString(0);
                                                                                        q = cursor3.getString(3);
                                                                                    }
                                                                                } else {
                                                                                    Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + itemname_t.getText().toString() + "'", null);
                                                                                    if (cursor3.moveToFirst()) {
                                                                                        _id = cursor3.getString(0);
                                                                                        q = cursor3.getString(3);
                                                                                    }
                                                                                }

                                                                                float item_content10 = Float.parseFloat(q) + reduce;

                                                                                ContentValues newValues = new ContentValues();
                                                                                newValues.put("stockquan", item_content10);
                                                                                String where3 = "_id = '" + _id + "'";

                                                                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                                                                getContentResolver().update(contentUri, newValues, where3, new String[]{});
                                                                                resultUri = new Uri.Builder()
                                                                                        .scheme("content")
                                                                                        .authority(StubProviderApp.AUTHORITY)
                                                                                        .path("Items")
                                                                                        .appendQueryParameter("operation", "update")
                                                                                        .appendQueryParameter("_id", _id)
                                                                                        .build();
                                                                                getContentResolver().notifyChange(resultUri, null);

                                                                                String where1_v1 = "docid = ?";
                                                                                db.update("Items_Virtual", newValues, where1_v1, new String[]{_id});

                                                                                dialog.dismiss();

                                                                                tableLayout.removeAllViews();
                                                                                displaydata();

                                                                                kotcanceldata(hii, itemname.getText().toString(), String.format("%.1f", reduce), ItemIDtable, add_reason.getText().toString(), p_price, tot1);
                                                                            }
                                                                        });
                                                                    }
                                                                }

                                                            }
                                                        });
                                                    }
//
                                                }
                                            }
                                        }
                                    }
                                }
                            });


                        }
                    });


                } while (cursor.moveToNext());

                View newView2 = inflater.inflate(R.layout.kot_order_management_tablerows_line, null);
                ImageView imageView_t = (ImageView) newView1.findViewById(R.id.line);


                tableLayout.addView(newView2);

            }
        }
    }

    public void printbillsplithome1(String kot){
        byte[] setHT34M = {0x1b,0x44,0x04,0x11,0x19,0x00};
        byte[] dotfeed = {0x1b,0x4a,0x10};
        byte[] HTRight = {0x1b,0x61, 0x02,0x09};
        byte[] HT = {0x09};
        byte[] HT1 = {0x09};
        byte[] LF = {0x0d,0x0a};

        byte[] left = {0x1b,0x61, 0x00};
        byte[] cen = {0x1b,0x61, 0x01};
        byte[] right = {0x1b,0x61, 0x02};
        byte[] bold = {0x1B,0x21,0x10};
        byte[] normal = {0x1d, 0x21, 0x00};
        byte[] horiz1 = {0x1b,0x44,0x19, 0x19, 0x00};
        byte[] horiz = {0x1b,0x44, 0x04, 0x06, 0x04, 0x04, 0x0A, 0x00, 0x09, 0x30, 0x09, 0x31, 0x09, 0x32, 0x09, 0x33, 0x09, 0x34, 0x0d,0x0a};

        byte[] un = {0x1b, 0x2d, 0x02};
        byte[] un1 = {0x1b, 0x2d, 0x00};
        String str_line = "";

        Cursor print_ty = db.rawQuery("SELECT * FROM Printer_type", null);
        if (print_ty.moveToFirst()){
            str_print_ty = print_ty.getString(1);
        }
        print_ty.close();

        Cursor getprint_type = db.rawQuery("SELECT * FROM Printer_text_size", null);
        if (getprint_type.moveToFirst()) {
            String type = getprint_type.getString(1);

            Cursor cc = db.rawQuery("SELECT * FROM Printerreceiptsize", null);

            if (cc.moveToFirst()) {
                cc.moveToFirst();
                do {
                    NAME = cc.getString(1);
                    if (NAME.equals("3 inch")) {
                        if (str_print_ty.toString().equals("Generic") || str_print_ty.toString().equals("Epson/others")) {
                            setHT32 = new byte[]{0x1b, 0x44, 0x29, 0x00};//2 tabs 3"
                            setHT321 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x25, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                            setHT34 = new byte[]{0x1b, 0x44, 0x08, 0x20, 0x29, 0x00};//4 tabs 3"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 3"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                            nPaperWidth = 576;
                            charlength = 41;
                            HT1 = new byte[]{0x09};
                            str_line = "------------------------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "------------------------------------------------".getBytes(), LF

                            };
                        }else {
                            if (str_print_ty.toString().equals("POS")) {
                                setHT32 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT321 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x10, 0x15, 0x00};//4 tabs 3"
                                setHTKOT = new byte[]{0x1b, 0x44, 0x05, 0x00};//2 tabs 3"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                nPaperWidth = 576;
                                charlength = 23;
                                charlength1 = 46;
                                charlength2 = 69;
                                quanlentha = 4;
                                HT1 = new byte[]{0x2F};
                                str_line = "------------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------------".getBytes(), LF

                                };
                            }
                        }
                    } else {
                        if (str_print_ty.toString().equals("Generic")) {
//                            Toast.makeText(KOT_Management.this, "phi", Toast.LENGTH_SHORT).show();
                            setHT32 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 2"
                            setHT321 = new byte[]{0x1b, 0x44, 0x13, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x15, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                            setHT34 = new byte[]{0x1b, 0x44, 0x04, 0x12, 0x19, 0x00};//4 tabs 2"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 2"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                            nPaperWidth = 384;
                            charlength = 25;
                            HT1 = new byte[]{0x09};
                            str_line = "--------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "--------------------------------".getBytes(), LF
                            };
                        }else {
                            if (str_print_ty.toString().equals("Epson/others")) {
//                            Toast.makeText(KOT_Management.this, "epson", Toast.LENGTH_SHORT).show();
                                setHT32 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 2"
                                setHT321 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x21, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                                setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x19, 0x21, 0x00};//4 tabs 2"
                                setHTKOT = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                                nPaperWidth = 384;
                                charlength = 28;
                                HT1 = new byte[]{0x09};
                                str_line = "------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------".getBytes(), LF
                                };
                            }else {
                                if (str_print_ty.toString().equals("POS")) {
                                    setHT32 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT321 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT3212 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 3"
                                    setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x12, 0x21, 0x00};//4 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x05, 0x08, 0x00};//4 tabs 2"
                                    setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x08, 0x09, 0x00};//4 tabs 2"
                                    setHTKOT = new byte[]{0x1b, 0x44, 0x05, 0x00};//2 tabs 3"
                                    feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                    nPaperWidth = 384;
                                    charlength = 11;
                                    charlength1 = 22;
                                    charlength2 = 33;
                                    quanlentha = 3;
                                    HT1 = new byte[]{0x2F};
                                    str_line = "--------------------------------";
                                    allbufline = new byte[][]{
                                            left, un1, "--------------------------------".getBytes(), LF
                                    };
                                }
                            }
                        }
                    }
                } while (cc.moveToNext());
            }
            cc.close();

        }
        getprint_type.close();


        String dd = "";
        TextView qazcvb = new TextView(KOT_Management.this);
        Cursor cvonnusb = db.rawQuery("SELECT * FROM BTConn", null);
        if (cvonnusb.moveToFirst()) {
            addgets = cvonnusb.getString(1);
            namegets = cvonnusb.getString(2);
            statussusbs = cvonnusb.getString(3);
            dd = cvonnusb.getString(4);
        }
        cvonnusb.close();
        qazcvb.setText(dd);
        if (qazcvb.getText().toString().equals("usb") && statussusbs.toString().equals("ok")) {
            runPrintCouponSequence1(kot);
        }else {
            Cursor connnet = db.rawQuery("SELECT * FROM IPConn", null);
            if (connnet.moveToFirst()) {
                ipnamegets = connnet.getString(1);
                portgets = connnet.getString(2);
                statusnets = connnet.getString(3);
            }

            Cursor connnet_counter = db.rawQuery("SELECT * FROM IPConn_Counter", null);
            if (connnet_counter.moveToFirst()) {
                ipnamegets_counter = connnet_counter.getString(1);
                portgets_counter = connnet_counter.getString(2);
                statusnets_counter = connnet_counter.getString(3);
            }
            connnet_counter.close();

            Cursor connusb = db.rawQuery("SELECT * FROM BTConn", null);
            if (connusb.moveToFirst()) {
                addgets = connusb.getString(1);
                namegets = connusb.getString(2);
                statussusbs = connusb.getString(3);
            }

            Cursor c_kot1 = db.rawQuery("SELECT * FROM IPConn_KOT1", null);
            if (c_kot1.moveToFirst()) {
                ipnamegets_kot1 = c_kot1.getString(1);
                portgets_kot1 = c_kot1.getString(2);
                statusnets_kot1 = c_kot1.getString(3);
                name_kot1 = c_kot1.getString(5);
            }
            c_kot1.close();

            Cursor c_kot2 = db.rawQuery("SELECT * FROM IPConn_KOT2", null);
            if (c_kot2.moveToFirst()) {
                ipnamegets_kot2 = c_kot2.getString(1);
                portgets_kot2 = c_kot2.getString(2);
                statusnets_kot2 = c_kot2.getString(3);
                name_kot2 = c_kot2.getString(5);
            }
            c_kot2.close();

            Cursor c_kot3 = db.rawQuery("SELECT * FROM IPConn_KOT3", null);
            if (c_kot3.moveToFirst()) {
                ipnamegets_kot3 = c_kot3.getString(1);
                portgets_kot3 = c_kot3.getString(2);
                statusnets_kot3 = c_kot3.getString(3);
                name_kot3 = c_kot3.getString(5);
            }
            c_kot3.close();

            Cursor c_kot4 = db.rawQuery("SELECT * FROM IPConn_KOT4", null);
            if (c_kot4.moveToFirst()) {
                ipnamegets_kot4 = c_kot4.getString(1);
                portgets_kot4 = c_kot4.getString(2);
                statusnets_kot4 = c_kot4.getString(3);
                name_kot4 = c_kot4.getString(5);
            }
            c_kot4.close();

            textViewstatusnets.setText(statusnet);
            textViewstatusnets_counter.setText(statusnets_counter);
            textViewstatussusbs.setText(statussusbs);
            textViewstatusnets_kot1.setText(statusnets_kot1);
            textViewstatusnets_kot2.setText(statusnets_kot2);
            textViewstatusnets_kot3.setText(statusnets_kot3);
            textViewstatusnets_kot4.setText(statusnets_kot4);

            Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
            if (getcom.moveToFirst()) {
                do {
                    strcompanyname = getcom.getString(1);
                } while (getcom.moveToNext());
            }


            tvkot.setText(strcompanyname);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf1 = new byte[][]{
                        bold, cen, strcompanyname.getBytes(), LF, LF

                };
                if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                    wifiSocket_kot1.WIFI_Write(bold);    //
                    wifiSocket_kot1.WIFI_Write(cen);    //
                    wifiSocket_kot1.WIFI_Write(strcompanyname);
                    wifiSocket_kot1.WIFI_Write(LF);    //
                    wifiSocket_kot1.WIFI_Write(LF);    //
                }else {
                    if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                        wifiSocket_kot2.WIFI_Write(bold);    //
                        wifiSocket_kot2.WIFI_Write(cen);    //
                        wifiSocket_kot2.WIFI_Write(strcompanyname);
                        wifiSocket_kot2.WIFI_Write(LF);    //
                        wifiSocket_kot2.WIFI_Write(LF);    //
                    }else {
                        if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                            wifiSocket_kot3.WIFI_Write(bold);    //
                            wifiSocket_kot3.WIFI_Write(cen);    //
                            wifiSocket_kot3.WIFI_Write(strcompanyname);
                            wifiSocket_kot3.WIFI_Write(LF);    //
                            wifiSocket_kot3.WIFI_Write(LF);    //
                        }else {
                            if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                wifiSocket_kot4.WIFI_Write(bold);    //
                                wifiSocket_kot4.WIFI_Write(cen);    //
                                wifiSocket_kot4.WIFI_Write(strcompanyname);
                                wifiSocket_kot4.WIFI_Write(LF);    //
                                wifiSocket_kot4.WIFI_Write(LF);    //
                            }else {
                                if (textViewstatusnets.getText().toString().equals("ok")) {
                                    wifiSocket.WIFI_Write(bold);    //
                                    wifiSocket.WIFI_Write(cen);    //
                                    wifiSocket.WIFI_Write(strcompanyname);
                                    wifiSocket.WIFI_Write(LF);    //
                                    wifiSocket.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                        wifiSocket2.WIFI_Write(bold);    //
                                        wifiSocket2.WIFI_Write(cen);    //
                                        wifiSocket2.WIFI_Write(strcompanyname);
                                        wifiSocket2.WIFI_Write(LF);    //
                                        wifiSocket2.WIFI_Write(LF);    //
                                    } else {
                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(bold);    //
                                            BluetoothPrintDriver.BT_Write(cen);    //
                                            BT_Write(strcompanyname);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }


            allbufKOT = new byte[][]{
                    un, cen, "Order Ticket copy".getBytes(), LF
            };
            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(un);    //
                wifiSocket_kot1.WIFI_Write(cen);    //
                wifiSocket_kot1.WIFI_Write("Order Ticket copy");
                wifiSocket_kot1.WIFI_Write(LF);    //
            }else {
                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                    wifiSocket_kot2.WIFI_Write(un);    //
                    wifiSocket_kot2.WIFI_Write(cen);    //
                    wifiSocket_kot2.WIFI_Write("Order Ticket copy");
                    wifiSocket_kot2.WIFI_Write(LF);    //
                }else {
                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                        wifiSocket_kot3.WIFI_Write(un);    //
                        wifiSocket_kot3.WIFI_Write(cen);    //
                        wifiSocket_kot3.WIFI_Write("Order Ticket copy");
                        wifiSocket_kot3.WIFI_Write(LF);    //
                    }else {
                        if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                            wifiSocket_kot4.WIFI_Write(un);    //
                            wifiSocket_kot4.WIFI_Write(cen);    //
                            wifiSocket_kot4.WIFI_Write("Order Ticket copy");
                            wifiSocket_kot4.WIFI_Write(LF);    //
                        }else {
                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                wifiSocket.WIFI_Write(un);    //
                                wifiSocket.WIFI_Write(cen);    //
                                wifiSocket.WIFI_Write("Order Ticket copy");
                                wifiSocket.WIFI_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(un);    //
                                    wifiSocket2.WIFI_Write(cen);    //
                                    wifiSocket2.WIFI_Write("Order Ticket copy");
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(un);    //
                                        BluetoothPrintDriver.BT_Write(cen);    //
                                        BT_Write("Order Ticket copy");
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }
                                }
                            }
                        }
                    }
                }
            }



            Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
            if (vbnm.moveToFirst()) {
                assa1 = vbnm.getString(1);
                assa2 = vbnm.getString(2);
            }
            TextView cx = new TextView(KOT_Management.this);
            cx.setText(assa1);
            String tablen = "";
            if (cx.getText().toString().equals("")) {
                tablen = "Tab" + assa2;
                allbuf11 = new byte[][]{
                        left, un1, setHT321, tablen.getBytes(), LF
                };
            } else {
                tablen = "Tab" + assa1;
                allbuf11 = new byte[][]{
                        left, un1, setHT321, tablen.getBytes(), LF
                };
            }

//        String tablen = "Table"+ItemIDtable;
//        allbuf11 = new byte[][]{
//                left,un1,setHT321, tablen.getBytes(),LF
//        };
            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(setHT321);    //
                wifiSocket_kot1.WIFI_Write(un1);    //
                wifiSocket_kot1.WIFI_Write(left);    //
                wifiSocket_kot1.WIFI_Write(tablen);
                wifiSocket_kot1.WIFI_Write(LF);    //
            }else {
                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                    wifiSocket_kot2.WIFI_Write(setHT321);    //
                    wifiSocket_kot2.WIFI_Write(un1);    //
                    wifiSocket_kot2.WIFI_Write(left);    //
                    wifiSocket_kot2.WIFI_Write(tablen);
                    wifiSocket_kot2.WIFI_Write(LF);    //
                }else {
                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                        wifiSocket_kot3.WIFI_Write(setHT321);    //
                        wifiSocket_kot3.WIFI_Write(un1);    //
                        wifiSocket_kot3.WIFI_Write(left);    //
                        wifiSocket_kot3.WIFI_Write(tablen);
                        wifiSocket_kot3.WIFI_Write(LF);    //
                    }else {
                        if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                            wifiSocket_kot4.WIFI_Write(setHT321);    //
                            wifiSocket_kot4.WIFI_Write(un1);    //
                            wifiSocket_kot4.WIFI_Write(left);    //
                            wifiSocket_kot4.WIFI_Write(tablen);
                            wifiSocket_kot4.WIFI_Write(LF);    //
                        }else {
                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                wifiSocket.WIFI_Write(setHT321);    //
                                wifiSocket.WIFI_Write(un1);    //
                                wifiSocket.WIFI_Write(left);    //
                                wifiSocket.WIFI_Write(tablen);
                                wifiSocket.WIFI_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(setHT321);    //
                                    wifiSocket2.WIFI_Write(un1);    //
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write(tablen);
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(setHT321);    //
                                        BluetoothPrintDriver.BT_Write(un1);    //
                                        BluetoothPrintDriver.BT_Write(left);    //
                                        BluetoothPrintDriver.BT_Write(tablen);
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }
                                }
                            }
                        }
                    }
                }
            }


            SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyyyy");
            String normal1 = normal2.format(new Date());

            Date dt = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
            String time1 = sdf1.format(dt);

            Date dtt = new Date();
            SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
            String time24 = sdf1t.format(dtt);

            Cursor cu_da = db1.rawQuery("SELECT * from Table" + ItemIDtable + "management WHERE tagg = '" + kot + "'", null);
            if (cu_da.moveToFirst()) {
                normal1 = cu_da.getString(4);
                time1 = cu_da.getString(5);
            }


            allbuf10 = new byte[][]{
                    setHT321, left, normal1.getBytes(), HT, "   ".getBytes(), time1.getBytes(), LF
            };
            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(setHT321);    //
                wifiSocket_kot1.WIFI_Write(left);    //
                wifiSocket_kot1.WIFI_Write(normal1);
                wifiSocket_kot1.WIFI_Write(HT);    //
                wifiSocket_kot1.WIFI_Write("   ");
                wifiSocket_kot1.WIFI_Write(time1);
                wifiSocket_kot1.WIFI_Write(LF);    //
            }else {
                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                    wifiSocket_kot2.WIFI_Write(setHT321);    //
                    wifiSocket_kot2.WIFI_Write(left);    //
                    wifiSocket_kot2.WIFI_Write(normal1);
                    wifiSocket_kot2.WIFI_Write(HT);    //
                    wifiSocket_kot2.WIFI_Write("   ");
                    wifiSocket_kot2.WIFI_Write(time1);
                    wifiSocket_kot2.WIFI_Write(LF);    //
                }else {
                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                        wifiSocket_kot3.WIFI_Write(setHT321);    //
                        wifiSocket_kot3.WIFI_Write(left);    //
                        wifiSocket_kot3.WIFI_Write(normal1);
                        wifiSocket_kot3.WIFI_Write(HT);    //
                        wifiSocket_kot3.WIFI_Write("   ");
                        wifiSocket_kot3.WIFI_Write(time1);
                        wifiSocket_kot3.WIFI_Write(LF);    //
                    }else {
                        if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                            wifiSocket_kot4.WIFI_Write(setHT321);    //
                            wifiSocket_kot4.WIFI_Write(left);    //
                            wifiSocket_kot4.WIFI_Write(normal1);
                            wifiSocket_kot4.WIFI_Write(HT);    //
                            wifiSocket_kot4.WIFI_Write("   ");
                            wifiSocket_kot4.WIFI_Write(time1);
                            wifiSocket_kot4.WIFI_Write(LF);    //
                        }else {
                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                wifiSocket.WIFI_Write(setHT321);    //
                                wifiSocket.WIFI_Write(left);    //
                                wifiSocket.WIFI_Write(normal1);
                                wifiSocket.WIFI_Write(HT);    //
                                wifiSocket.WIFI_Write("   ");
                                wifiSocket.WIFI_Write(time1);
                                wifiSocket.WIFI_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(setHT321);    //
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write(normal1);
                                    wifiSocket2.WIFI_Write(HT);    //
                                    wifiSocket2.WIFI_Write("   ");
                                    wifiSocket2.WIFI_Write(time1);
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(setHT321);    //
                                        BluetoothPrintDriver.BT_Write(left);    //
                                        BT_Write(normal1);
                                        BluetoothPrintDriver.BT_Write(HT);    //
                                        BT_Write("   ");
                                        BT_Write(time1);
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }
                                }
                            }
                        }
                    }
                }
            }


            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(left);    //
                wifiSocket_kot1.WIFI_Write(un1);    //
                wifiSocket_kot1.WIFI_Write(str_line);
                wifiSocket_kot1.WIFI_Write(LF);    //
            }else {
                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                    wifiSocket_kot2.WIFI_Write(left);    //
                    wifiSocket_kot2.WIFI_Write(un1);    //
                    wifiSocket_kot2.WIFI_Write(str_line);
                    wifiSocket_kot2.WIFI_Write(LF);    //
                }else {
                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                        wifiSocket_kot3.WIFI_Write(left);    //
                        wifiSocket_kot3.WIFI_Write(un1);    //
                        wifiSocket_kot3.WIFI_Write(str_line);
                        wifiSocket_kot3.WIFI_Write(LF);    //
                    }else {
                        if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                            wifiSocket_kot4.WIFI_Write(left);    //
                            wifiSocket_kot4.WIFI_Write(un1);    //
                            wifiSocket_kot4.WIFI_Write(str_line);
                            wifiSocket_kot4.WIFI_Write(LF);    //
                        }else {
                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                wifiSocket.WIFI_Write(left);    //
                                wifiSocket.WIFI_Write(un1);    //
                                wifiSocket.WIFI_Write(str_line);
                                wifiSocket.WIFI_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write(un1);    //
                                    wifiSocket2.WIFI_Write(str_line);
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(left);    //
                                        BluetoothPrintDriver.BT_Write(un1);    //
                                        BT_Write(str_line);
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }
                                }
                            }
                        }
                    }
                }
            }


            allbufqty = new byte[][]{
                    setHTKOT, normal, "Qty".getBytes(), HT, "Item".getBytes(), left, LF
            };
            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(setHTKOT);    //
                wifiSocket_kot1.WIFI_Write(normal);    //
                wifiSocket_kot1.WIFI_Write("Qty");
                wifiSocket_kot1.WIFI_Write(HT);    //
                wifiSocket_kot1.WIFI_Write("Item");
                wifiSocket_kot1.WIFI_Write(left);    //
                wifiSocket_kot1.WIFI_Write(LF);    //
            }else {
                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                    wifiSocket_kot2.WIFI_Write(setHTKOT);    //
                    wifiSocket_kot2.WIFI_Write(normal);    //
                    wifiSocket_kot2.WIFI_Write("Qty");
                    wifiSocket_kot2.WIFI_Write(HT);    //
                    wifiSocket_kot2.WIFI_Write("Item");
                    wifiSocket_kot2.WIFI_Write(left);    //
                    wifiSocket_kot2.WIFI_Write(LF);    //
                }else {
                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                        wifiSocket_kot3.WIFI_Write(setHTKOT);    //
                        wifiSocket_kot3.WIFI_Write(normal);    //
                        wifiSocket_kot3.WIFI_Write("Qty");
                        wifiSocket_kot3.WIFI_Write(HT);    //
                        wifiSocket_kot3.WIFI_Write("Item");
                        wifiSocket_kot3.WIFI_Write(left);    //
                        wifiSocket_kot3.WIFI_Write(LF);    //
                    }else {
                        if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                            wifiSocket_kot4.WIFI_Write(setHTKOT);    //
                            wifiSocket_kot4.WIFI_Write(normal);    //
                            wifiSocket_kot4.WIFI_Write("Qty");
                            wifiSocket_kot4.WIFI_Write(HT);    //
                            wifiSocket_kot4.WIFI_Write("Item");
                            wifiSocket_kot4.WIFI_Write(left);    //
                            wifiSocket_kot4.WIFI_Write(LF);    //
                        }else {
                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                wifiSocket.WIFI_Write(setHTKOT);    //
                                wifiSocket.WIFI_Write(normal);    //
                                wifiSocket.WIFI_Write("Qty");
                                wifiSocket.WIFI_Write(HT);    //
                                wifiSocket.WIFI_Write("Item");
                                wifiSocket.WIFI_Write(left);    //
                                wifiSocket.WIFI_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(setHTKOT);    //
                                    wifiSocket2.WIFI_Write(normal);    //
                                    wifiSocket2.WIFI_Write("Qty");
                                    wifiSocket2.WIFI_Write(HT);    //
                                    wifiSocket2.WIFI_Write("Item");
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                        BluetoothPrintDriver.BT_Write(normal);    //
                                        BT_Write("Qty");
                                        BluetoothPrintDriver.BT_Write(HT);    //
                                        BT_Write("Item");
                                        BluetoothPrintDriver.BT_Write(left);    //
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }
                                }
                            }
                        }
                    }
                }
            }


            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(left);    //
                wifiSocket_kot1.WIFI_Write(un1);    //
                wifiSocket_kot1.WIFI_Write(str_line);
                wifiSocket_kot1.WIFI_Write(LF);    //
            }else {
                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                    wifiSocket_kot2.WIFI_Write(left);    //
                    wifiSocket_kot2.WIFI_Write(un1);    //
                    wifiSocket_kot2.WIFI_Write(str_line);
                    wifiSocket_kot2.WIFI_Write(LF);    //
                }else {
                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                        wifiSocket_kot3.WIFI_Write(left);    //
                        wifiSocket_kot3.WIFI_Write(un1);    //
                        wifiSocket_kot3.WIFI_Write(str_line);
                        wifiSocket_kot3.WIFI_Write(LF);    //
                    }else {
                        if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                            wifiSocket_kot4.WIFI_Write(left);    //
                            wifiSocket_kot4.WIFI_Write(un1);    //
                            wifiSocket_kot4.WIFI_Write(str_line);
                            wifiSocket_kot4.WIFI_Write(LF);    //
                        }else {
                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                wifiSocket.WIFI_Write(left);    //
                                wifiSocket.WIFI_Write(un1);    //
                                wifiSocket.WIFI_Write(str_line);
                                wifiSocket.WIFI_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write(un1);    //
                                    wifiSocket2.WIFI_Write(str_line);
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(left);    //
                                        BluetoothPrintDriver.BT_Write(un1);    //
                                        BT_Write(str_line);
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }
                                }
                            }
                        }
                    }
                }
            }



            Cursor cursor = db1.rawQuery("Select * from Table" + ItemIDtable + "management WHERE tagg = '" + kot + "' AND itemtype = 'Item' GROUP BY par_id", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (cursor.moveToFirst()) {
                do {
                    String Itemtype = cursor.getString(7);
                    String total = cursor.getString(2);
                    final String idid = cursor.getString(6);
                    String name = cursor.getString(1);


                    if (Itemtype.toString().equals("Item")) {

                        Cursor modcursorc = db1.rawQuery("Select * from Table" + ItemIDtable + "management WHERE par_id = '" + idid + "' AND itemtype = 'Modifier' GROUP BY itemname, par_id, tagg", null);
                        if (modcursorc.moveToFirst()) {

                            if (name.toString().length() > charlength) {
                                String str1 = name.substring(0, charlength);
                                String str2 = name.substring(charlength);
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "         ".getBytes(), str2.getBytes(), left, LF,
                                };
                                if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                                    wifiSocket_kot1.WIFI_Write(setHTKOT);    //
                                    wifiSocket_kot1.WIFI_Write(normal);    //
                                    wifiSocket_kot1.WIFI_Write(total);
                                    wifiSocket_kot1.WIFI_Write(HT);    //
                                    wifiSocket_kot1.WIFI_Write(str1);
                                    wifiSocket_kot1.WIFI_Write(LF);    //
                                    wifiSocket_kot1.WIFI_Write("         ");
                                    wifiSocket_kot1.WIFI_Write(str2);
                                    wifiSocket_kot1.WIFI_Write(left);    //
                                    wifiSocket_kot1.WIFI_Write(LF);    //
                                }else {
                                    if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                                        wifiSocket_kot2.WIFI_Write(setHTKOT);    //
                                        wifiSocket_kot2.WIFI_Write(normal);    //
                                        wifiSocket_kot2.WIFI_Write(total);
                                        wifiSocket_kot2.WIFI_Write(HT);    //
                                        wifiSocket_kot2.WIFI_Write(str1);
                                        wifiSocket_kot2.WIFI_Write(LF);    //
                                        wifiSocket_kot2.WIFI_Write("         ");
                                        wifiSocket_kot2.WIFI_Write(str2);
                                        wifiSocket_kot2.WIFI_Write(left);    //
                                        wifiSocket_kot2.WIFI_Write(LF);    //
                                    }else {
                                        if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                                            wifiSocket_kot3.WIFI_Write(setHTKOT);    //
                                            wifiSocket_kot3.WIFI_Write(normal);    //
                                            wifiSocket_kot3.WIFI_Write(total);
                                            wifiSocket_kot3.WIFI_Write(HT);    //
                                            wifiSocket_kot3.WIFI_Write(str1);
                                            wifiSocket_kot3.WIFI_Write(LF);    //
                                            wifiSocket_kot3.WIFI_Write("         ");
                                            wifiSocket_kot3.WIFI_Write(str2);
                                            wifiSocket_kot3.WIFI_Write(left);    //
                                            wifiSocket_kot3.WIFI_Write(LF);    //
                                        }else {
                                            if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                                wifiSocket_kot4.WIFI_Write(setHTKOT);    //
                                                wifiSocket_kot4.WIFI_Write(normal);    //
                                                wifiSocket_kot4.WIFI_Write(total);
                                                wifiSocket_kot4.WIFI_Write(HT);    //
                                                wifiSocket_kot4.WIFI_Write(str1);
                                                wifiSocket_kot4.WIFI_Write(LF);    //
                                                wifiSocket_kot4.WIFI_Write("         ");
                                                wifiSocket_kot4.WIFI_Write(str2);
                                                wifiSocket_kot4.WIFI_Write(left);    //
                                                wifiSocket_kot4.WIFI_Write(LF);    //
                                            }else {
                                                if (textViewstatusnets.getText().toString().equals("ok")) {
                                                    wifiSocket.WIFI_Write(setHTKOT);    //
                                                    wifiSocket.WIFI_Write(normal);    //
                                                    wifiSocket.WIFI_Write(total);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(str1);
                                                    wifiSocket.WIFI_Write(LF);    //
                                                    wifiSocket.WIFI_Write("         ");
                                                    wifiSocket.WIFI_Write(str2);
                                                    wifiSocket.WIFI_Write(left);    //
                                                    wifiSocket.WIFI_Write(LF);    //
                                                } else {
                                                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                                        wifiSocket2.WIFI_Write(setHTKOT);    //
                                                        wifiSocket2.WIFI_Write(normal);    //
                                                        wifiSocket2.WIFI_Write(total);
                                                        wifiSocket2.WIFI_Write(HT);    //
                                                        wifiSocket2.WIFI_Write(str1);
                                                        wifiSocket2.WIFI_Write(LF);    //
                                                        wifiSocket2.WIFI_Write("         ");
                                                        wifiSocket2.WIFI_Write(str2);
                                                        wifiSocket2.WIFI_Write(left);    //
                                                        wifiSocket2.WIFI_Write(LF);    //
                                                    } else {
                                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                            BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                                            BluetoothPrintDriver.BT_Write(normal);    //
                                                            BT_Write(total);
                                                            BluetoothPrintDriver.BT_Write(HT);    //
                                                            BT_Write(str1);
                                                            BluetoothPrintDriver.BT_Write(LF);    //
                                                            BT_Write("         ");
                                                            BT_Write(str2);
                                                            BluetoothPrintDriver.BT_Write(left);    //
                                                            BluetoothPrintDriver.BT_Write(LF);    //
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            } else {
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                };
                                if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                                    wifiSocket_kot1.WIFI_Write(setHTKOT);    //
                                    wifiSocket_kot1.WIFI_Write(normal);    //
                                    wifiSocket_kot1.WIFI_Write(total);
                                    wifiSocket_kot1.WIFI_Write(HT);    //
                                    wifiSocket_kot1.WIFI_Write(name);
                                    wifiSocket_kot1.WIFI_Write(left);    //
                                    wifiSocket_kot1.WIFI_Write(LF);    //
                                }else {
                                    if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                                        wifiSocket_kot2.WIFI_Write(setHTKOT);    //
                                        wifiSocket_kot2.WIFI_Write(normal);    //
                                        wifiSocket_kot2.WIFI_Write(total);
                                        wifiSocket_kot2.WIFI_Write(HT);    //
                                        wifiSocket_kot2.WIFI_Write(name);
                                        wifiSocket_kot2.WIFI_Write(left);    //
                                        wifiSocket_kot2.WIFI_Write(LF);    //
                                    }else {
                                        if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                                            wifiSocket_kot3.WIFI_Write(setHTKOT);    //
                                            wifiSocket_kot3.WIFI_Write(normal);    //
                                            wifiSocket_kot3.WIFI_Write(total);
                                            wifiSocket_kot3.WIFI_Write(HT);    //
                                            wifiSocket_kot3.WIFI_Write(name);
                                            wifiSocket_kot3.WIFI_Write(left);    //
                                            wifiSocket_kot3.WIFI_Write(LF);    //
                                        }else {
                                            if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                                wifiSocket_kot4.WIFI_Write(setHTKOT);    //
                                                wifiSocket_kot4.WIFI_Write(normal);    //
                                                wifiSocket_kot4.WIFI_Write(total);
                                                wifiSocket_kot4.WIFI_Write(HT);    //
                                                wifiSocket_kot4.WIFI_Write(name);
                                                wifiSocket_kot4.WIFI_Write(left);    //
                                                wifiSocket_kot4.WIFI_Write(LF);    //
                                            }else {
                                                if (textViewstatusnets.getText().toString().equals("ok")) {
                                                    wifiSocket.WIFI_Write(setHTKOT);    //
                                                    wifiSocket.WIFI_Write(normal);    //
                                                    wifiSocket.WIFI_Write(total);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(name);
                                                    wifiSocket.WIFI_Write(left);    //
                                                    wifiSocket.WIFI_Write(LF);    //
                                                } else {
                                                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                                        wifiSocket2.WIFI_Write(setHTKOT);    //
                                                        wifiSocket2.WIFI_Write(normal);    //
                                                        wifiSocket2.WIFI_Write(total);
                                                        wifiSocket2.WIFI_Write(HT);    //
                                                        wifiSocket2.WIFI_Write(name);
                                                        wifiSocket2.WIFI_Write(left);    //
                                                        wifiSocket2.WIFI_Write(LF);    //
                                                    } else {
                                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                            BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                                            BluetoothPrintDriver.BT_Write(normal);    //
                                                            BT_Write(total);
                                                            BluetoothPrintDriver.BT_Write(HT);    //
                                                            BT_Write(name);
                                                            BluetoothPrintDriver.BT_Write(left);    //
                                                            BluetoothPrintDriver.BT_Write(LF);    //
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }


                            do {
                                final String modiname = modcursorc.getString(1);
                                String mod = modiname;


                                if (mod.toString().length() > charlength) {
                                    String str1 = mod.substring(0, charlength);
                                    String str2 = mod.substring(charlength);
                                    allbufmodifiers = new byte[][]{
                                            setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), str1.getBytes(), LF, "         ".getBytes(), str2.getBytes(), left, LF,
                                    };
                                    if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                                        wifiSocket_kot1.WIFI_Write(setHTKOT);    //
                                        wifiSocket_kot1.WIFI_Write(normal);    //
                                        wifiSocket_kot1.WIFI_Write("");
                                        wifiSocket_kot1.WIFI_Write(HT);    //
                                        wifiSocket_kot1.WIFI_Write(">");
                                        wifiSocket_kot1.WIFI_Write(str1);
                                        wifiSocket_kot1.WIFI_Write(LF);    //
                                        wifiSocket_kot1.WIFI_Write("         ");
                                        wifiSocket_kot1.WIFI_Write(str2);
                                        wifiSocket_kot1.WIFI_Write(left);    //
                                        wifiSocket_kot1.WIFI_Write(LF);    //
                                    }else {
                                        if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                                            wifiSocket_kot2.WIFI_Write(setHTKOT);    //
                                            wifiSocket_kot2.WIFI_Write(normal);    //
                                            wifiSocket_kot2.WIFI_Write("");
                                            wifiSocket_kot2.WIFI_Write(HT);    //
                                            wifiSocket_kot2.WIFI_Write(">");
                                            wifiSocket_kot2.WIFI_Write(str1);
                                            wifiSocket_kot2.WIFI_Write(LF);    //
                                            wifiSocket_kot2.WIFI_Write("         ");
                                            wifiSocket_kot2.WIFI_Write(str2);
                                            wifiSocket_kot2.WIFI_Write(left);    //
                                            wifiSocket_kot2.WIFI_Write(LF);    //
                                        }else {
                                            if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                                                wifiSocket_kot3.WIFI_Write(setHTKOT);    //
                                                wifiSocket_kot3.WIFI_Write(normal);    //
                                                wifiSocket_kot3.WIFI_Write("");
                                                wifiSocket_kot3.WIFI_Write(HT);    //
                                                wifiSocket_kot3.WIFI_Write(">");
                                                wifiSocket_kot3.WIFI_Write(str1);
                                                wifiSocket_kot3.WIFI_Write(LF);    //
                                                wifiSocket_kot3.WIFI_Write("         ");
                                                wifiSocket_kot3.WIFI_Write(str2);
                                                wifiSocket_kot3.WIFI_Write(left);    //
                                                wifiSocket_kot3.WIFI_Write(LF);    //
                                            }else {
                                                if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                                    wifiSocket_kot4.WIFI_Write(setHTKOT);    //
                                                    wifiSocket_kot4.WIFI_Write(normal);    //
                                                    wifiSocket_kot4.WIFI_Write("");
                                                    wifiSocket_kot4.WIFI_Write(HT);    //
                                                    wifiSocket_kot4.WIFI_Write(">");
                                                    wifiSocket_kot4.WIFI_Write(str1);
                                                    wifiSocket_kot4.WIFI_Write(LF);    //
                                                    wifiSocket_kot4.WIFI_Write("         ");
                                                    wifiSocket_kot4.WIFI_Write(str2);
                                                    wifiSocket_kot4.WIFI_Write(left);    //
                                                    wifiSocket_kot4.WIFI_Write(LF);    //
                                                }else {
                                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                                        wifiSocket.WIFI_Write(setHTKOT);    //
                                                        wifiSocket.WIFI_Write(normal);    //
                                                        wifiSocket.WIFI_Write("");
                                                        wifiSocket.WIFI_Write(HT);    //
                                                        wifiSocket.WIFI_Write(">");
                                                        wifiSocket.WIFI_Write(str1);
                                                        wifiSocket.WIFI_Write(LF);    //
                                                        wifiSocket.WIFI_Write("         ");
                                                        wifiSocket.WIFI_Write(str2);
                                                        wifiSocket.WIFI_Write(left);    //
                                                        wifiSocket.WIFI_Write(LF);    //
                                                    } else {
                                                        if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                                            wifiSocket2.WIFI_Write(setHTKOT);    //
                                                            wifiSocket2.WIFI_Write(normal);    //
                                                            wifiSocket2.WIFI_Write("");
                                                            wifiSocket2.WIFI_Write(HT);    //
                                                            wifiSocket2.WIFI_Write(">");
                                                            wifiSocket2.WIFI_Write(str1);
                                                            wifiSocket2.WIFI_Write(LF);    //
                                                            wifiSocket2.WIFI_Write("         ");
                                                            wifiSocket2.WIFI_Write(str2);
                                                            wifiSocket2.WIFI_Write(left);    //
                                                            wifiSocket2.WIFI_Write(LF);    //
                                                        } else {
                                                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                                BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                                                BluetoothPrintDriver.BT_Write(normal);    //
                                                                BT_Write("");
                                                                BluetoothPrintDriver.BT_Write(HT);    //
                                                                BT_Write(">");
                                                                BT_Write(str1);
                                                                BluetoothPrintDriver.BT_Write(LF);    //
                                                                BT_Write("      ");
                                                                BT_Write(str2);
                                                                BluetoothPrintDriver.BT_Write(left);    //
                                                                BluetoothPrintDriver.BT_Write(LF);    //
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                } else {
                                    allbufmodifiers = new byte[][]{
                                            setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), mod.getBytes(), left, LF
                                    };
                                    if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                                        wifiSocket_kot1.WIFI_Write(setHTKOT);    //
                                        wifiSocket_kot1.WIFI_Write(normal);    //
                                        wifiSocket_kot1.WIFI_Write("");
                                        wifiSocket_kot1.WIFI_Write(HT);    //
                                        wifiSocket_kot1.WIFI_Write(">");
                                        wifiSocket_kot1.WIFI_Write(mod);
                                        wifiSocket_kot1.WIFI_Write(left);    //
                                        wifiSocket_kot1.WIFI_Write(LF);    //
                                    }else {
                                        if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                                            wifiSocket_kot2.WIFI_Write(setHTKOT);    //
                                            wifiSocket_kot2.WIFI_Write(normal);    //
                                            wifiSocket_kot2.WIFI_Write("");
                                            wifiSocket_kot2.WIFI_Write(HT);    //
                                            wifiSocket_kot2.WIFI_Write(">");
                                            wifiSocket_kot2.WIFI_Write(mod);
                                            wifiSocket_kot2.WIFI_Write(left);    //
                                            wifiSocket_kot2.WIFI_Write(LF);    //
                                        }else {
                                            if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                                                wifiSocket_kot3.WIFI_Write(setHTKOT);    //
                                                wifiSocket_kot3.WIFI_Write(normal);    //
                                                wifiSocket_kot3.WIFI_Write("");
                                                wifiSocket_kot3.WIFI_Write(HT);    //
                                                wifiSocket_kot3.WIFI_Write(">");
                                                wifiSocket_kot3.WIFI_Write(mod);
                                                wifiSocket_kot3.WIFI_Write(left);    //
                                                wifiSocket_kot3.WIFI_Write(LF);    //
                                            }else {
                                                if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                                    wifiSocket_kot4.WIFI_Write(setHTKOT);    //
                                                    wifiSocket_kot4.WIFI_Write(normal);    //
                                                    wifiSocket_kot4.WIFI_Write("");
                                                    wifiSocket_kot4.WIFI_Write(HT);    //
                                                    wifiSocket_kot4.WIFI_Write(">");
                                                    wifiSocket_kot4.WIFI_Write(mod);
                                                    wifiSocket_kot4.WIFI_Write(left);    //
                                                    wifiSocket_kot4.WIFI_Write(LF);    //
                                                }else {
                                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                                        wifiSocket.WIFI_Write(setHTKOT);    //
                                                        wifiSocket.WIFI_Write(normal);    //
                                                        wifiSocket.WIFI_Write("");
                                                        wifiSocket.WIFI_Write(HT);    //
                                                        wifiSocket.WIFI_Write(">");
                                                        wifiSocket.WIFI_Write(mod);
                                                        wifiSocket.WIFI_Write(left);    //
                                                        wifiSocket.WIFI_Write(LF);    //
                                                    } else {
                                                        if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                                            wifiSocket2.WIFI_Write(setHTKOT);    //
                                                            wifiSocket2.WIFI_Write(normal);    //
                                                            wifiSocket2.WIFI_Write("");
                                                            wifiSocket2.WIFI_Write(HT);    //
                                                            wifiSocket2.WIFI_Write(">");
                                                            wifiSocket2.WIFI_Write(mod);
                                                            wifiSocket2.WIFI_Write(left);    //
                                                            wifiSocket2.WIFI_Write(LF);    //
                                                        } else {
                                                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                                BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                                                BluetoothPrintDriver.BT_Write(normal);    //
                                                                BT_Write("");
                                                                BluetoothPrintDriver.BT_Write(HT);    //
                                                                BT_Write(">");
                                                                BT_Write(mod);
                                                                BluetoothPrintDriver.BT_Write(left);    //
                                                                BluetoothPrintDriver.BT_Write(LF);    //
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }


                            } while (modcursorc.moveToNext());
                        } else {
                            if (name.toString().length() > charlength) {
                                String str1 = name.substring(0, charlength);
                                String str2 = name.substring(charlength);
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "         ".getBytes(), str2.getBytes(), left, LF,
                                };
                                if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                                    wifiSocket_kot1.WIFI_Write(setHTKOT);    //
                                    wifiSocket_kot1.WIFI_Write(normal);    //
                                    wifiSocket_kot1.WIFI_Write(total);
                                    wifiSocket_kot1.WIFI_Write(HT);    //
                                    wifiSocket_kot1.WIFI_Write(str1);
                                    wifiSocket_kot1.WIFI_Write(LF);    //
                                    wifiSocket_kot1.WIFI_Write("         ");
                                    wifiSocket_kot1.WIFI_Write(str2);
                                    wifiSocket_kot1.WIFI_Write(left);    //
                                    wifiSocket_kot1.WIFI_Write(LF);    //
                                }else {
                                    if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                                        wifiSocket_kot2.WIFI_Write(setHTKOT);    //
                                        wifiSocket_kot2.WIFI_Write(normal);    //
                                        wifiSocket_kot2.WIFI_Write(total);
                                        wifiSocket_kot2.WIFI_Write(HT);    //
                                        wifiSocket_kot2.WIFI_Write(str1);
                                        wifiSocket_kot2.WIFI_Write(LF);    //
                                        wifiSocket_kot2.WIFI_Write("         ");
                                        wifiSocket_kot2.WIFI_Write(str2);
                                        wifiSocket_kot2.WIFI_Write(left);    //
                                        wifiSocket_kot2.WIFI_Write(LF);    //
                                    }else {
                                        if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                                            wifiSocket_kot3.WIFI_Write(setHTKOT);    //
                                            wifiSocket_kot3.WIFI_Write(normal);    //
                                            wifiSocket_kot3.WIFI_Write(total);
                                            wifiSocket_kot3.WIFI_Write(HT);    //
                                            wifiSocket_kot3.WIFI_Write(str1);
                                            wifiSocket_kot3.WIFI_Write(LF);    //
                                            wifiSocket_kot3.WIFI_Write("         ");
                                            wifiSocket_kot3.WIFI_Write(str2);
                                            wifiSocket_kot3.WIFI_Write(left);    //
                                            wifiSocket_kot3.WIFI_Write(LF);    //
                                        }else {
                                            if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                                wifiSocket_kot4.WIFI_Write(setHTKOT);    //
                                                wifiSocket_kot4.WIFI_Write(normal);    //
                                                wifiSocket_kot4.WIFI_Write(total);
                                                wifiSocket_kot4.WIFI_Write(HT);    //
                                                wifiSocket_kot4.WIFI_Write(str1);
                                                wifiSocket_kot4.WIFI_Write(LF);    //
                                                wifiSocket_kot4.WIFI_Write("         ");
                                                wifiSocket_kot4.WIFI_Write(str2);
                                                wifiSocket_kot4.WIFI_Write(left);    //
                                                wifiSocket_kot4.WIFI_Write(LF);    //
                                            }else {
                                                if (textViewstatusnets.getText().toString().equals("ok")) {
                                                    wifiSocket.WIFI_Write(setHTKOT);    //
                                                    wifiSocket.WIFI_Write(normal);    //
                                                    wifiSocket.WIFI_Write(total);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(str1);
                                                    wifiSocket.WIFI_Write(LF);    //
                                                    wifiSocket.WIFI_Write("         ");
                                                    wifiSocket.WIFI_Write(str2);
                                                    wifiSocket.WIFI_Write(left);    //
                                                    wifiSocket.WIFI_Write(LF);    //
                                                } else {
                                                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                                        wifiSocket2.WIFI_Write(setHTKOT);    //
                                                        wifiSocket2.WIFI_Write(normal);    //
                                                        wifiSocket2.WIFI_Write(total);
                                                        wifiSocket2.WIFI_Write(HT);    //
                                                        wifiSocket2.WIFI_Write(str1);
                                                        wifiSocket2.WIFI_Write(LF);    //
                                                        wifiSocket2.WIFI_Write("         ");
                                                        wifiSocket2.WIFI_Write(str2);
                                                        wifiSocket2.WIFI_Write(left);    //
                                                        wifiSocket2.WIFI_Write(LF);    //
                                                    } else {
                                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                            BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                                            BluetoothPrintDriver.BT_Write(normal);    //
                                                            BT_Write(total);
                                                            BluetoothPrintDriver.BT_Write(HT);    //
                                                            BT_Write(str1);
                                                            BluetoothPrintDriver.BT_Write(LF);    //
                                                            BT_Write("         ");
                                                            BT_Write(str2);
                                                            BluetoothPrintDriver.BT_Write(left);    //
                                                            BluetoothPrintDriver.BT_Write(LF);    //
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            } else {
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                };
                                if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                                    wifiSocket_kot1.WIFI_Write(setHTKOT);    //
                                    wifiSocket_kot1.WIFI_Write(normal);    //
                                    wifiSocket_kot1.WIFI_Write(total);
                                    wifiSocket_kot1.WIFI_Write(HT);    //
                                    wifiSocket_kot1.WIFI_Write(name);
                                    wifiSocket_kot1.WIFI_Write(left);    //
                                    wifiSocket_kot1.WIFI_Write(LF);    //
                                }else {
                                    if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                                        wifiSocket_kot2.WIFI_Write(setHTKOT);    //
                                        wifiSocket_kot2.WIFI_Write(normal);    //
                                        wifiSocket_kot2.WIFI_Write(total);
                                        wifiSocket_kot2.WIFI_Write(HT);    //
                                        wifiSocket_kot2.WIFI_Write(name);
                                        wifiSocket_kot2.WIFI_Write(left);    //
                                        wifiSocket_kot2.WIFI_Write(LF);    //
                                    }else {
                                        if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                                            wifiSocket_kot3.WIFI_Write(setHTKOT);    //
                                            wifiSocket_kot3.WIFI_Write(normal);    //
                                            wifiSocket_kot3.WIFI_Write(total);
                                            wifiSocket_kot3.WIFI_Write(HT);    //
                                            wifiSocket_kot3.WIFI_Write(name);
                                            wifiSocket_kot3.WIFI_Write(left);    //
                                            wifiSocket_kot3.WIFI_Write(LF);    //
                                        }else {
                                            if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                                wifiSocket_kot4.WIFI_Write(setHTKOT);    //
                                                wifiSocket_kot4.WIFI_Write(normal);    //
                                                wifiSocket_kot4.WIFI_Write(total);
                                                wifiSocket_kot4.WIFI_Write(HT);    //
                                                wifiSocket_kot4.WIFI_Write(name);
                                                wifiSocket_kot4.WIFI_Write(left);    //
                                                wifiSocket_kot4.WIFI_Write(LF);    //
                                            }else {
                                                if (textViewstatusnets.getText().toString().equals("ok")) {
                                                    wifiSocket.WIFI_Write(setHTKOT);    //
                                                    wifiSocket.WIFI_Write(normal);    //
                                                    wifiSocket.WIFI_Write(total);
                                                    wifiSocket.WIFI_Write(HT);    //
                                                    wifiSocket.WIFI_Write(name);
                                                    wifiSocket.WIFI_Write(left);    //
                                                    wifiSocket.WIFI_Write(LF);    //
                                                } else {
                                                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                                        wifiSocket2.WIFI_Write(setHTKOT);    //
                                                        wifiSocket2.WIFI_Write(normal);    //
                                                        wifiSocket2.WIFI_Write(total);
                                                        wifiSocket2.WIFI_Write(HT);    //
                                                        wifiSocket2.WIFI_Write(name);
                                                        wifiSocket2.WIFI_Write(left);    //
                                                        wifiSocket2.WIFI_Write(LF);    //
                                                    } else {
                                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                            BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                                            BluetoothPrintDriver.BT_Write(normal);    //
                                                            BT_Write(total);
                                                            BluetoothPrintDriver.BT_Write(HT);    //
                                                            BT_Write(name);
                                                            BluetoothPrintDriver.BT_Write(left);    //
                                                            BluetoothPrintDriver.BT_Write(LF);    //
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }

                        }
                    }


                } while (cursor.moveToNext());
            }

            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(left);    //
                wifiSocket_kot1.WIFI_Write(un1);    //
                wifiSocket_kot1.WIFI_Write(str_line);
                wifiSocket_kot1.WIFI_Write(LF);    //
            }else {
                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                    wifiSocket_kot2.WIFI_Write(left);    //
                    wifiSocket_kot2.WIFI_Write(un1);    //
                    wifiSocket_kot2.WIFI_Write(str_line);
                    wifiSocket_kot2.WIFI_Write(LF);    //
                }else {
                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                        wifiSocket_kot3.WIFI_Write(left);    //
                        wifiSocket_kot3.WIFI_Write(un1);    //
                        wifiSocket_kot3.WIFI_Write(str_line);
                        wifiSocket_kot3.WIFI_Write(LF);    //
                    }else {
                        if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                            wifiSocket_kot4.WIFI_Write(left);    //
                            wifiSocket_kot4.WIFI_Write(un1);    //
                            wifiSocket_kot4.WIFI_Write(str_line);
                            wifiSocket_kot4.WIFI_Write(LF);    //
                        }else {
                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                wifiSocket.WIFI_Write(left);    //
                                wifiSocket.WIFI_Write(un1);    //
                                wifiSocket.WIFI_Write(str_line);
                                wifiSocket.WIFI_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write(un1);    //
                                    wifiSocket2.WIFI_Write(str_line);
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(left);    //
                                        BluetoothPrintDriver.BT_Write(un1);    //
                                        BT_Write(str_line);
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //feedcut();

            totalquanret1 = "0";
            totalquanret2 = "0";

            Cursor toalitems = db1.rawQuery("Select * from Table" + ItemIDtable + "management WHERE tagg = '" + kot + "'", null);
            if (toalitems.moveToFirst()) {
                Cursor toalitems2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity <= '1.0'", null);
                if (toalitems2.moveToFirst()) {
                    int toalzx = toalitems2.getCount();
                    totalquanret1 = String.valueOf(toalzx);
                }
                Cursor toalitems1 = db1.rawQuery("Select sum(quantity) from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity > '1.0'", null);
                if (toalitems1.moveToFirst()) {
                    int toalzx = toalitems1.getInt(0);
                    totalquanret2 = String.valueOf(toalzx);
                }
                int cvvc = Integer.parseInt(totalquanret1) + Integer.parseInt(totalquanret2);
                String total = String.valueOf(cvvc);
                String totalqu = "No. of items : " + total;
                allbuf11 = new byte[][]{
                        left, setHT321, totalqu.getBytes(), LF
                };
                if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                    wifiSocket_kot1.WIFI_Write(left);    //
                    wifiSocket_kot1.WIFI_Write(setHT321);    //
                    wifiSocket_kot1.WIFI_Write(totalqu);
                    wifiSocket_kot1.WIFI_Write(LF);    //
                }else {
                    if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                        wifiSocket_kot2.WIFI_Write(left);    //
                        wifiSocket_kot2.WIFI_Write(setHT321);    //
                        wifiSocket_kot2.WIFI_Write(totalqu);
                        wifiSocket_kot2.WIFI_Write(LF);    //
                    }else {
                        if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                            wifiSocket_kot3.WIFI_Write(left);    //
                            wifiSocket_kot3.WIFI_Write(setHT321);    //
                            wifiSocket_kot3.WIFI_Write(totalqu);
                            wifiSocket_kot3.WIFI_Write(LF);    //
                        }else {
                            if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                wifiSocket_kot4.WIFI_Write(left);    //
                                wifiSocket_kot4.WIFI_Write(setHT321);    //
                                wifiSocket_kot4.WIFI_Write(totalqu);
                                wifiSocket_kot4.WIFI_Write(LF);    //
                            }else {
                                if (textViewstatusnets.getText().toString().equals("ok")) {
                                    wifiSocket.WIFI_Write(left);    //
                                    wifiSocket.WIFI_Write(setHT321);    //
                                    wifiSocket.WIFI_Write(totalqu);
                                    wifiSocket.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                        wifiSocket2.WIFI_Write(left);    //
                                        wifiSocket2.WIFI_Write(setHT321);    //
                                        wifiSocket2.WIFI_Write(totalqu);
                                        wifiSocket2.WIFI_Write(LF);    //
                                    } else {
                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(left);    //
                                            BluetoothPrintDriver.BT_Write(setHT321);    //
                                            BT_Write(totalqu);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }


            byte[][] allbuf = new byte[][]{
                    feedcut2
            };
            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(feedcut2);    //
            }else {
                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                    wifiSocket_kot2.WIFI_Write(feedcut2);    //
                }else {
                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                        wifiSocket_kot3.WIFI_Write(feedcut2);    //
                    }else {
                        if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                            wifiSocket_kot4.WIFI_Write(feedcut2);    //
                        }else {
                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                wifiSocket.WIFI_Write(feedcut2);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(feedcut2);    //
                                } else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(feedcut2);    //
                                    }
                                }
                            }
                        }
                    }
                }
            }


            if (str_print_ty.toString().equals("POS")) {
                if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                    wifiSocket_kot1.WIFI_Write(feedcut2);    //
                }else {
                    if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                        wifiSocket_kot2.WIFI_Write(feedcut2);    //
                    }else {
                        if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                            wifiSocket_kot3.WIFI_Write(feedcut2);    //
                        }else {
                            if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                wifiSocket_kot4.WIFI_Write(feedcut2);    //
                            }else {
                                if (textViewstatusnets.getText().toString().equals("ok")) {
                                    wifiSocket.WIFI_Write(feedcut2);    //
                                } else {
                                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                        wifiSocket2.WIFI_Write(feedcut2);    //
                                    } else {
                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(feedcut2);    //
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    public void printbillsplithome_kot1(String name_kot1) {
        byte[] setHT34M = {0x1b, 0x44, 0x04, 0x11, 0x19, 0x00};
        byte[] dotfeed = {0x1b, 0x4a, 0x10};
        byte[] HTRight = {0x1b, 0x61, 0x02, 0x09};
        byte[] HT = {0x09};
        byte[] HT1 = {0x09};
        byte[] LF = {0x0d, 0x0a};

        byte[] left = {0x1b, 0x61, 0x00};
        byte[] cen = {0x1b, 0x61, 0x01};
        byte[] right = {0x1b, 0x61, 0x02};
        byte[] bold = {0x1B,0x21,0x10};
        byte[] normal = {0x1d, 0x21, 0x00};
        byte[] horiz1 = {0x1b, 0x44, 0x19, 0x19, 0x00};
        byte[] horiz = {0x1b, 0x44, 0x04, 0x06, 0x04, 0x04, 0x0A, 0x00, 0x09, 0x30, 0x09, 0x31, 0x09, 0x32, 0x09, 0x33, 0x09, 0x34, 0x0d, 0x0a};

        byte[] un = {0x1b, 0x2d, 0x02};
        byte[] un1 = {0x1b, 0x2d, 0x00};
        String str_line = "";

        Cursor print_ty = db.rawQuery("SELECT * FROM Printer_type", null);
        if (print_ty.moveToFirst()) {
            str_print_ty = print_ty.getString(1);
        }
        print_ty.close();

        Cursor getprint_type = db.rawQuery("SELECT * FROM Printer_text_size", null);
        if (getprint_type.moveToFirst()) {
            String type = getprint_type.getString(1);

            Cursor cc = db.rawQuery("SELECT * FROM Printerreceiptsize", null);

            if (cc.moveToFirst()) {
                cc.moveToFirst();
                do {
                    NAME = cc.getString(1);
                    if (NAME.equals("3 inch")) {
                        if (str_print_ty.toString().equals("Generic") || str_print_ty.toString().equals("Epson/others")) {
                            setHT32 = new byte[]{0x1b, 0x44, 0x29, 0x00};//2 tabs 3"
                            setHT321 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x25, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                            setHT34 = new byte[]{0x1b, 0x44, 0x08, 0x20, 0x29, 0x00};//4 tabs 3"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 3"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                            nPaperWidth = 576;
                            charlength = 41;
                            HT1 = new byte[]{0x09};
                            str_line = "------------------------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "------------------------------------------------".getBytes(), LF

                            };
                        } else {
                            if (str_print_ty.toString().equals("POS")) {
                                setHT32 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT321 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x10, 0x15, 0x00};//4 tabs 3"
                                setHTKOT = new byte[]{0x1b, 0x44, 0x05, 0x00};//2 tabs 3"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                nPaperWidth = 576;
                                charlength = 23;
                                charlength1 = 46;
                                charlength2 = 69;
                                quanlentha = 4;
                                HT1 = new byte[]{0x2F};
                                str_line = "------------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------------".getBytes(), LF

                                };
                            }
                        }
                    } else {
                        if (str_print_ty.toString().equals("Generic")) {
//                            Toast.makeText(KOT_Management.this, "phi", Toast.LENGTH_SHORT).show();
                            setHT32 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 2"
                            setHT321 = new byte[]{0x1b, 0x44, 0x13, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x15, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                            setHT34 = new byte[]{0x1b, 0x44, 0x04, 0x12, 0x19, 0x00};//4 tabs 2"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 2"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                            nPaperWidth = 384;
                            charlength = 25;
                            HT1 = new byte[]{0x09};
                            str_line = "--------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "--------------------------------".getBytes(), LF
                            };
                        } else {
                            if (str_print_ty.toString().equals("Epson/others") || (str_print_ty.toString().equals("wiseposplus"))) {
//                            Toast.makeText(KOT_Management.this, "epson", Toast.LENGTH_SHORT).show();
                                setHT32 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 2"
                                setHT321 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x21, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                                setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x19, 0x21, 0x00};//4 tabs 2"
                                setHTKOT = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                                nPaperWidth = 384;
                                charlength = 28;
                                HT1 = new byte[]{0x09};
                                str_line = "------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------".getBytes(), LF
                                };
                            } else {
                                if (str_print_ty.toString().equals("POS")) {
                                    setHT32 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT321 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT3212 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 3"
                                    setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x12, 0x21, 0x00};//4 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x05, 0x08, 0x00};//4 tabs 2"
                                    setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x08, 0x09, 0x00};//4 tabs 2"
                                    setHTKOT = new byte[]{0x1b, 0x44, 0x05, 0x00};//2 tabs 3"
                                    feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                    nPaperWidth = 384;
                                    charlength = 11;
                                    charlength1 = 22;
                                    charlength2 = 33;
                                    quanlentha = 3;
                                    HT1 = new byte[]{0x2F};
                                    str_line = "--------------------------------";
                                    allbufline = new byte[][]{
                                            left, un1, "--------------------------------".getBytes(), LF
                                    };
                                }
                            }
                        }
                    }
                } while (cc.moveToNext());
            }
            cc.close();

        }
        getprint_type.close();

        String dd = "";
        TextView qazcvb = new TextView(KOT_Management.this);
        Cursor cvonnusb = db.rawQuery("SELECT * FROM BTConn", null);
        if (cvonnusb.moveToFirst()) {
            addgets = cvonnusb.getString(1);
            namegets = cvonnusb.getString(2);
            statussusbs = cvonnusb.getString(3);
            dd = cvonnusb.getString(4);
        }
        cvonnusb.close();
        qazcvb.setText(dd);
        if (qazcvb.getText().toString().equals("usb") && statussusbs.toString().equals("ok")) {
            runPrintCouponSequence();
        } else {

            Cursor c_kot1 = db.rawQuery("SELECT * FROM IPConn_KOT1", null);
            if (c_kot1.moveToFirst()) {
                ipnamegets_kot1 = c_kot1.getString(1);
                portgets_kot1 = c_kot1.getString(2);
                statusnets_kot1 = c_kot1.getString(3);
                name_kot1 = c_kot1.getString(5);
            }
            c_kot1.close();

            textViewstatusnets_kot1.setText(statusnets_kot1);

            Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
            if (getcom.moveToFirst()) {
                strcompanyname = getcom.getString(1);
            }
            getcom.close();

            tvkot.setText(strcompanyname);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf1 = new byte[][]{
                        bold, cen, strcompanyname.getBytes(), LF, LF

                };

                if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                    wifiSocket_kot1.WIFI_Write(bold);    //
                    wifiSocket_kot1.WIFI_Write(cen);    //
                    wifiSocket_kot1.WIFI_Write(strcompanyname);
                    wifiSocket_kot1.WIFI_Write(LF);    //
                    wifiSocket_kot1.WIFI_Write(LF);    //
                }
            }

            int one_11 = 0;
            Cursor cursor_6 = db1.rawQuery("Select MAX(tagg) from Table" + ItemIDtable, null);
            if (cursor_6.moveToFirst()) {
                one_11 = cursor_6.getInt(0);
            }
            cursor_6.close();

            int one111 = one_11+1;
            String one_1 = String.valueOf(one111);

            allbufKOT = new byte[][]{
                    un, cen, "Order Ticket copy".getBytes(), LF
            };
            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(normal);    //
                wifiSocket_kot1.WIFI_Write(un);    //
                wifiSocket_kot1.WIFI_Write(cen);    //
                wifiSocket_kot1.WIFI_Write("Order Ticket copy "+one_1);
                wifiSocket_kot1.WIFI_Write(LF);    //
            }

            Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
            if (vbnm.moveToFirst()) {
                assa1 = vbnm.getString(1);
                assa2 = vbnm.getString(2);
            }
            vbnm.close();
            TextView cx = new TextView(KOT_Management.this);
            cx.setText(assa1);
            String tablen = "";
            if (cx.getText().toString().equals("")) {
                tablen = "Tab" + assa2;
            } else {
                tablen = "Tab" + assa1;
            }

            allbuf11 = new byte[][]{
                    left, un1, setHT321, tablen.getBytes(), LF
            };

            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(left);    //
                wifiSocket_kot1.WIFI_Write(un1);    //
                wifiSocket_kot1.WIFI_Write(tablen);
                wifiSocket_kot1.WIFI_Write(LF);    //
            }

            SimpleDateFormat normal2 = new SimpleDateFormat("dd MMM yyyy");
            final String normal1 = normal2.format(new Date());

            Date dt = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
            final String time1 = sdf1.format(dt);

            Date dtt_new = new Date();
            SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");
            String time24_new = sdf1t_new.format(dtt_new);

            Date dtt = new Date();
            SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
            String time24 = sdf1t.format(dtt);

            allbuf10 = new byte[][]{
                    setHT321, left, normal1.getBytes(), HT, "   ".getBytes(), time1.getBytes(), LF
            };
            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(setHT321);    //
                wifiSocket_kot1.WIFI_Write(left);    //
                wifiSocket_kot1.WIFI_Write(normal1);
                wifiSocket_kot1.WIFI_Write(HT);    //
                wifiSocket_kot1.WIFI_Write("  ");
                wifiSocket_kot1.WIFI_Write(time1);
                wifiSocket_kot1.WIFI_Write(LF);    //
            }


            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(left);    //
                wifiSocket_kot1.WIFI_Write(un1);    //
                wifiSocket_kot1.WIFI_Write(str_line);
                wifiSocket_kot1.WIFI_Write(LF);    //
            }

            allbufqty = new byte[][]{
                    setHTKOT, normal, "Qty".getBytes(), HT, "Item".getBytes(), left, LF
            };
            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(setHTKOT);    //
                wifiSocket_kot1.WIFI_Write(normal);    //
                wifiSocket_kot1.WIFI_Write("Qty");
                wifiSocket_kot1.WIFI_Write(HT);    //
                wifiSocket_kot1.WIFI_Write("Item");
                wifiSocket_kot1.WIFI_Write(left);    //
                wifiSocket_kot1.WIFI_Write(LF);    //
            }

            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(left);    //
                wifiSocket_kot1.WIFI_Write(un1);    //
                wifiSocket_kot1.WIFI_Write(str_line);
                wifiSocket_kot1.WIFI_Write(LF);    //
            }

            Cursor cursor = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE dept_name = '"+name_kot1+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (cursor.moveToFirst()) {
                do {
                    String Itemtype = cursor.getString(5);
                    String total = cursor.getString(20);
                    final String idid = cursor.getString(0);
                    String name = cursor.getString(2);
                    final String iidd = cursor.getString(0);
                    final String hii = cursor.getString(2);
                    final String newtt = cursor.getString(4);
                    final float f = Float.parseFloat(cursor.getString(3));
                    String price = String.valueOf(f);
                    String stat = cursor.getString(16);
                    String sev = cursor.getString(7);
                    String add_note_print = cursor.getString(30);

                    System.out.println("name is therre "+name);
                    TextView tvbh = new TextView(KOT_Management.this);

                    tvbh.setText(stat);


                    TextView tv_add_note_print = new TextView(KOT_Management.this);
                    tv_add_note_print.setText(add_note_print);


                    if (Itemtype.toString().equals("Item")) {

                        Cursor modcursorc = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE parent = '" + hii + "' AND parentid = '" + idid + "'  ", null);
                        if (modcursorc.moveToFirst()) {
                            Cursor modt = db1.rawQuery("Select SUM(total) from Table" + ItemIDtable + " WHERE parent = '" + name + "' AND parentid = '" + idid + "' ", null);
                            if (modt.moveToFirst()) {
                                do {
                                    //row.removeView(tv3);
                                    float aq = modt.getFloat(0);
                                    aqq = String.valueOf(aq);
                                    aqq1 = Float.parseFloat(aqq) + Float.parseFloat(newtt);
                                    aqq2 = String.valueOf(aqq1);
                                } while (modt.moveToNext());
                            }
                            modt.close();

                            if (name.toString().length() > charlength) {
                                String str1 = name.substring(0, charlength);
                                String str2 = name.substring(charlength);
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                };
                                if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                                    wifiSocket_kot1.WIFI_Write(setHTKOT);    //
                                    wifiSocket_kot1.WIFI_Write(normal);    //
                                    wifiSocket_kot1.WIFI_Write(total);
                                    wifiSocket_kot1.WIFI_Write(HT);    //
                                    wifiSocket_kot1.WIFI_Write(str1);
                                    wifiSocket_kot1.WIFI_Write(LF);    //
                                    wifiSocket_kot1.WIFI_Write("      ");
                                    wifiSocket_kot1.WIFI_Write(str2);
                                    wifiSocket_kot1.WIFI_Write(left);    //
                                    wifiSocket_kot1.WIFI_Write(LF);    //
                                }

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                                        wifiSocket_kot1.WIFI_Write(normal);    //
                                        wifiSocket_kot1.WIFI_Write(add_note_print);
                                        wifiSocket_kot1.WIFI_Write(LF);    //
                                    }
                                }
                            } else {
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                };
                                if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                                    wifiSocket_kot1.WIFI_Write(setHTKOT);    //
                                    wifiSocket_kot1.WIFI_Write(normal);    //
                                    wifiSocket_kot1.WIFI_Write(total);
                                    wifiSocket_kot1.WIFI_Write(HT);    //
                                    wifiSocket_kot1.WIFI_Write(name);
                                    wifiSocket_kot1.WIFI_Write(left);    //
                                    wifiSocket_kot1.WIFI_Write(LF);    //
                                }

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                                        wifiSocket_kot1.WIFI_Write(normal);    //
                                        wifiSocket_kot1.WIFI_Write(add_note_print);
                                        wifiSocket_kot1.WIFI_Write(LF);    //
                                    }
                                }

                            }

                            do {
                                final String modiname = modcursorc.getString(2);
                                final String modiquan = modcursorc.getString(1);
                                String modiprice = modcursorc.getString(3);
                                String moditotal = modcursorc.getString(4);
                                final String modiid = modcursorc.getString(0);
                                String mod = modiname;


                                if (mod.toString().length() > charlength) {
                                    String str1 = mod.substring(0, charlength);
                                    String str2 = mod.substring(charlength);
                                    allbufmodifiers = new byte[][]{
                                            setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                    };
                                    if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                                        wifiSocket_kot1.WIFI_Write(setHTKOT);    //
                                        wifiSocket_kot1.WIFI_Write(normal);    //
                                        wifiSocket_kot1.WIFI_Write("");
                                        wifiSocket_kot1.WIFI_Write(HT);    //
                                        wifiSocket_kot1.WIFI_Write(">");
                                        wifiSocket_kot1.WIFI_Write(str1);
                                        wifiSocket_kot1.WIFI_Write(LF);    //
                                        wifiSocket_kot1.WIFI_Write("      ");
                                        wifiSocket_kot1.WIFI_Write(str2);
                                        wifiSocket_kot1.WIFI_Write(left);    //
                                        wifiSocket_kot1.WIFI_Write(LF);    //
                                    }

                                    if (tv_add_note_print.getText().toString().equals("")){

                                    }else {
                                        if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                                            wifiSocket_kot1.WIFI_Write(normal);    //
                                            wifiSocket_kot1.WIFI_Write(add_note_print);
                                            wifiSocket_kot1.WIFI_Write(LF);    //
                                        }
                                    }

                                } else {
                                    allbufmodifiers = new byte[][]{
                                            setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), mod.getBytes(), left, LF
                                    };
                                    if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                                        wifiSocket_kot1.WIFI_Write(setHTKOT);    //
                                        wifiSocket_kot1.WIFI_Write(normal);    //
                                        wifiSocket_kot1.WIFI_Write("");
                                        wifiSocket_kot1.WIFI_Write(HT);    //
                                        wifiSocket_kot1.WIFI_Write(">");
                                        wifiSocket_kot1.WIFI_Write(mod);
                                        wifiSocket_kot1.WIFI_Write(left);    //
                                        wifiSocket_kot1.WIFI_Write(LF);    //
                                    }

                                    if (tv_add_note_print.getText().toString().equals("")){

                                    }else {
                                        if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                                            wifiSocket_kot1.WIFI_Write(normal);    //
                                            wifiSocket_kot1.WIFI_Write(add_note_print);
                                            wifiSocket_kot1.WIFI_Write(LF);    //
                                        }
                                    }

                                }

                            } while (modcursorc.moveToNext());
                        } else {
                            if (name.toString().length() > charlength) {
                                String str1 = name.substring(0, charlength);
                                String str2 = name.substring(charlength);
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                };
                                if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                                    wifiSocket_kot1.WIFI_Write(setHTKOT);    //
                                    wifiSocket_kot1.WIFI_Write(normal);    //
                                    wifiSocket_kot1.WIFI_Write(total);
                                    wifiSocket_kot1.WIFI_Write(HT);    //
                                    wifiSocket_kot1.WIFI_Write(str1);
                                    wifiSocket_kot1.WIFI_Write(LF);    //
                                    wifiSocket_kot1.WIFI_Write("      ");
                                    wifiSocket_kot1.WIFI_Write(str2);
                                    wifiSocket_kot1.WIFI_Write(left);    //
                                    wifiSocket_kot1.WIFI_Write(LF);    //
                                }

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                                        wifiSocket_kot1.WIFI_Write(normal);    //
                                        wifiSocket_kot1.WIFI_Write(add_note_print);
                                        wifiSocket_kot1.WIFI_Write(LF);    //
                                    }
                                }

                            } else {
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                };
                                if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                                    wifiSocket_kot1.WIFI_Write(setHTKOT);    //
                                    wifiSocket_kot1.WIFI_Write(normal);    //
                                    wifiSocket_kot1.WIFI_Write(total);
                                    wifiSocket_kot1.WIFI_Write(HT);    //
                                    wifiSocket_kot1.WIFI_Write(name);
                                    wifiSocket_kot1.WIFI_Write(left);    //
                                    wifiSocket_kot1.WIFI_Write(LF);    //
                                }

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                                        wifiSocket_kot1.WIFI_Write(normal);    //
                                        wifiSocket_kot1.WIFI_Write(add_note_print);
                                        wifiSocket_kot1.WIFI_Write(LF);    //
                                    }
                                }

                            }

                        }
                        modcursorc.close();
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();

            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(left);    //
                wifiSocket_kot1.WIFI_Write(un1);    //
                wifiSocket_kot1.WIFI_Write(str_line);
                wifiSocket_kot1.WIFI_Write(LF);    //
            }
            //feedcut();

            Cursor toalitems = db1.rawQuery("Select * from Table" + ItemIDtable, null);
            if (toalitems.moveToFirst()) {
                Cursor toalitems2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity <= '1.0'", null);
                if (toalitems2.moveToFirst()) {
                    int toalzx = toalitems2.getCount();
                    totalquanret1 = String.valueOf(toalzx);
                }
                toalitems2.close();

                Cursor toalitems1 = db1.rawQuery("Select sum(quantity) from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity > '1.0'", null);
                if (toalitems1.moveToFirst()) {
                    int toalzx = toalitems1.getInt(0);
                    totalquanret2 = String.valueOf(toalzx);
                }
                toalitems1.close();

                int cvvc = Integer.parseInt(totalquanret1) + Integer.parseInt(totalquanret2);
                String total = String.valueOf(cvvc);
                String totalqu = "No. of items : " + total;
                allbuf11 = new byte[][]{
                        left, setHT321, totalqu.getBytes(), LF
                };
                if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                    wifiSocket_kot1.WIFI_Write(left);    //
                    wifiSocket_kot1.WIFI_Write(setHT321);    //
                    wifiSocket_kot1.WIFI_Write(totalqu);
                    wifiSocket_kot1.WIFI_Write(LF);    //
                }
            }
            toalitems.close();

            byte[][] allbuf = new byte[][]{
                    feedcut2
            };
            System.out.println("feedcut executed now 4");
            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(feedcut2);    //
            }

//            if (str_print_ty.toString().equals("POS")) {
//                if (textViewstatusnets.getText().toString().equals("ok")) {
//                    wifiSocket.WIFI_Write(feedcut2);    //
//                } else {
//                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
//                        wifiSocket2.WIFI_Write(feedcut2);    //
//                    } else {
//                        if (textViewstatussusbs.getText().toString().equals("ok")) {
//                            BluetoothPrintDriver.BT_Write(feedcut2);    //
//                        }
//                    }
//                }
//            }
        }
//        Toast.makeText(KOT_Management.this, "KOT printed", Toast.LENGTH_LONG).show();
    }

    public void printbillsplithome_kot2(String name_kot2) {
        byte[] setHT34M = {0x1b, 0x44, 0x04, 0x11, 0x19, 0x00};
        byte[] dotfeed = {0x1b, 0x4a, 0x10};
        byte[] HTRight = {0x1b, 0x61, 0x02, 0x09};
        byte[] HT = {0x09};
        byte[] HT1 = {0x09};
        byte[] LF = {0x0d, 0x0a};

        byte[] left = {0x1b, 0x61, 0x00};
        byte[] cen = {0x1b, 0x61, 0x01};
        byte[] right = {0x1b, 0x61, 0x02};
        byte[] bold = {0x1B,0x21,0x10};
        byte[] normal = {0x1d, 0x21, 0x00};
        byte[] horiz1 = {0x1b, 0x44, 0x19, 0x19, 0x00};
        byte[] horiz = {0x1b, 0x44, 0x04, 0x06, 0x04, 0x04, 0x0A, 0x00, 0x09, 0x30, 0x09, 0x31, 0x09, 0x32, 0x09, 0x33, 0x09, 0x34, 0x0d, 0x0a};

        byte[] un = {0x1b, 0x2d, 0x02};
        byte[] un1 = {0x1b, 0x2d, 0x00};
        String str_line = "";

        Cursor print_ty = db.rawQuery("SELECT * FROM Printer_type", null);
        if (print_ty.moveToFirst()) {
            str_print_ty = print_ty.getString(1);
        }
        print_ty.close();

        Cursor getprint_type = db.rawQuery("SELECT * FROM Printer_text_size", null);
        if (getprint_type.moveToFirst()) {
            String type = getprint_type.getString(1);

            Cursor cc = db.rawQuery("SELECT * FROM Printerreceiptsize", null);

            if (cc.moveToFirst()) {
                cc.moveToFirst();
                do {
                    NAME = cc.getString(1);
                    if (NAME.equals("3 inch")) {
                        if (str_print_ty.toString().equals("Generic") || str_print_ty.toString().equals("Epson/others")) {
                            setHT32 = new byte[]{0x1b, 0x44, 0x29, 0x00};//2 tabs 3"
                            setHT321 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x25, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                            setHT34 = new byte[]{0x1b, 0x44, 0x08, 0x20, 0x29, 0x00};//4 tabs 3"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 3"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                            nPaperWidth = 576;
                            charlength = 41;
                            HT1 = new byte[]{0x09};
                            str_line = "------------------------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "------------------------------------------------".getBytes(), LF

                            };
                        } else {
                            if (str_print_ty.toString().equals("POS")) {
                                setHT32 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT321 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x10, 0x15, 0x00};//4 tabs 3"
                                setHTKOT = new byte[]{0x1b, 0x44, 0x05, 0x00};//2 tabs 3"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                nPaperWidth = 576;
                                charlength = 23;
                                charlength1 = 46;
                                charlength2 = 69;
                                quanlentha = 4;
                                HT1 = new byte[]{0x2F};
                                str_line = "------------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------------".getBytes(), LF

                                };
                            }
                        }
                    } else {
                        if (str_print_ty.toString().equals("Generic")) {
//                            Toast.makeText(KOT_Management.this, "phi", Toast.LENGTH_SHORT).show();
                            setHT32 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 2"
                            setHT321 = new byte[]{0x1b, 0x44, 0x13, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x15, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                            setHT34 = new byte[]{0x1b, 0x44, 0x04, 0x12, 0x19, 0x00};//4 tabs 2"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 2"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                            nPaperWidth = 384;
                            charlength = 25;
                            HT1 = new byte[]{0x09};
                            str_line = "--------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "--------------------------------".getBytes(), LF
                            };
                        } else {
                            if (str_print_ty.toString().equals("Epson/others") || (str_print_ty.toString().equals("wiseposplus"))) {
//                            Toast.makeText(KOT_Management.this, "epson", Toast.LENGTH_SHORT).show();
                                setHT32 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 2"
                                setHT321 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x21, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                                setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x19, 0x21, 0x00};//4 tabs 2"
                                setHTKOT = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                                nPaperWidth = 384;
                                charlength = 28;
                                HT1 = new byte[]{0x09};
                                str_line = "------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------".getBytes(), LF
                                };
                            } else {
                                if (str_print_ty.toString().equals("POS")) {
                                    setHT32 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT321 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT3212 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 3"
                                    setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x12, 0x21, 0x00};//4 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x05, 0x08, 0x00};//4 tabs 2"
                                    setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x08, 0x09, 0x00};//4 tabs 2"
                                    setHTKOT = new byte[]{0x1b, 0x44, 0x05, 0x00};//2 tabs 3"
                                    feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                    nPaperWidth = 384;
                                    charlength = 11;
                                    charlength1 = 22;
                                    charlength2 = 33;
                                    quanlentha = 3;
                                    HT1 = new byte[]{0x2F};
                                    str_line = "--------------------------------";
                                    allbufline = new byte[][]{
                                            left, un1, "--------------------------------".getBytes(), LF
                                    };
                                }
                            }
                        }
                    }
                } while (cc.moveToNext());
            }
            cc.close();

        }
        getprint_type.close();

        String dd = "";
        TextView qazcvb = new TextView(KOT_Management.this);
        Cursor cvonnusb = db.rawQuery("SELECT * FROM BTConn", null);
        if (cvonnusb.moveToFirst()) {
            addgets = cvonnusb.getString(1);
            namegets = cvonnusb.getString(2);
            statussusbs = cvonnusb.getString(3);
            dd = cvonnusb.getString(4);
        }
        cvonnusb.close();
        qazcvb.setText(dd);
        if (qazcvb.getText().toString().equals("usb") && statussusbs.toString().equals("ok")) {
            runPrintCouponSequence();
        } else {

            Cursor c_kot2 = db.rawQuery("SELECT * FROM IPConn_KOT2", null);
            if (c_kot2.moveToFirst()) {
                ipnamegets_kot2 = c_kot2.getString(1);
                portgets_kot2 = c_kot2.getString(2);
                statusnets_kot2 = c_kot2.getString(3);
                name_kot2 = c_kot2.getString(5);
            }
            c_kot2.close();

            textViewstatusnets_kot2.setText(statusnets_kot2);

            Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
            if (getcom.moveToFirst()) {
                strcompanyname = getcom.getString(1);
            }
            getcom.close();

            tvkot.setText(strcompanyname);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf1 = new byte[][]{
                        bold, cen, strcompanyname.getBytes(), LF, LF

                };

                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                    wifiSocket_kot2.WIFI_Write(bold);    //
                    wifiSocket_kot2.WIFI_Write(cen);    //
                    wifiSocket_kot2.WIFI_Write(strcompanyname);
                    wifiSocket_kot2.WIFI_Write(LF);    //
                    wifiSocket_kot2.WIFI_Write(LF);    //
                }
            }

            int one_11 = 0;
            Cursor cursor_6 = db1.rawQuery("Select MAX(tagg) from Table" + ItemIDtable, null);
            if (cursor_6.moveToFirst()) {
                one_11 = cursor_6.getInt(0);
            }
            cursor_6.close();

            int one111 = one_11+1;
            String one_1 = String.valueOf(one111);

            allbufKOT = new byte[][]{
                    un, cen, "Order Ticket copy".getBytes(), LF
            };
            if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                wifiSocket_kot2.WIFI_Write(normal);    //
                wifiSocket_kot2.WIFI_Write(un);    //
                wifiSocket_kot2.WIFI_Write(cen);    //
                wifiSocket_kot2.WIFI_Write("Order Ticket copy "+one_1);
                wifiSocket_kot2.WIFI_Write(LF);    //
            }

            Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
            if (vbnm.moveToFirst()) {
                assa1 = vbnm.getString(1);
                assa2 = vbnm.getString(2);
            }
            vbnm.close();
            TextView cx = new TextView(KOT_Management.this);
            cx.setText(assa1);
            String tablen = "";
            if (cx.getText().toString().equals("")) {
                tablen = "Tab" + assa2;
            } else {
                tablen = "Tab" + assa1;
            }

            allbuf11 = new byte[][]{
                    left, un1, setHT321, tablen.getBytes(), LF
            };

            if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                wifiSocket_kot2.WIFI_Write(left);    //
                wifiSocket_kot2.WIFI_Write(un1);    //
                wifiSocket_kot2.WIFI_Write(tablen);
                wifiSocket_kot2.WIFI_Write(LF);    //
            }

            SimpleDateFormat normal2 = new SimpleDateFormat("dd MMM yyyy");
            final String normal1 = normal2.format(new Date());

            Date dt = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
            final String time1 = sdf1.format(dt);

            Date dtt_new = new Date();
            SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");
            String time24_new = sdf1t_new.format(dtt_new);

            Date dtt = new Date();
            SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
            String time24 = sdf1t.format(dtt);

            allbuf10 = new byte[][]{
                    setHT321, left, normal1.getBytes(), HT, "   ".getBytes(), time1.getBytes(), LF
            };
            if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                wifiSocket_kot2.WIFI_Write(setHT321);    //
                wifiSocket_kot2.WIFI_Write(left);    //
                wifiSocket_kot2.WIFI_Write(normal1);
                wifiSocket_kot2.WIFI_Write(HT);    //
                wifiSocket_kot2.WIFI_Write("  ");
                wifiSocket_kot2.WIFI_Write(time1);
                wifiSocket_kot2.WIFI_Write(LF);    //
            }


            if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                wifiSocket_kot2.WIFI_Write(left);    //
                wifiSocket_kot2.WIFI_Write(un1);    //
                wifiSocket_kot2.WIFI_Write(str_line);
                wifiSocket_kot2.WIFI_Write(LF);    //
            }

            allbufqty = new byte[][]{
                    setHTKOT, normal, "Qty".getBytes(), HT, "Item".getBytes(), left, LF
            };
            if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                wifiSocket_kot2.WIFI_Write(setHTKOT);    //
                wifiSocket_kot2.WIFI_Write(normal);    //
                wifiSocket_kot2.WIFI_Write("Qty");
                wifiSocket_kot2.WIFI_Write(HT);    //
                wifiSocket_kot2.WIFI_Write("Item");
                wifiSocket_kot2.WIFI_Write(left);    //
                wifiSocket_kot2.WIFI_Write(LF);    //
            }

            if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                wifiSocket_kot2.WIFI_Write(left);    //
                wifiSocket_kot2.WIFI_Write(un1);    //
                wifiSocket_kot2.WIFI_Write(str_line);
                wifiSocket_kot2.WIFI_Write(LF);    //
            }

            Cursor cursor = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE dept_name = '"+name_kot2+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (cursor.moveToFirst()) {
                do {
                    String Itemtype = cursor.getString(5);
                    String total = cursor.getString(20);
                    final String idid = cursor.getString(0);
                    String name = cursor.getString(2);
                    final String iidd = cursor.getString(0);
                    final String hii = cursor.getString(2);
                    final String newtt = cursor.getString(4);
                    final float f = Float.parseFloat(cursor.getString(3));
                    String price = String.valueOf(f);
                    String stat = cursor.getString(16);
                    String sev = cursor.getString(7);
                    String add_note_print = cursor.getString(30);

                    System.out.println("name is therre "+name);
                    TextView tvbh = new TextView(KOT_Management.this);

                    tvbh.setText(stat);


                    TextView tv_add_note_print = new TextView(KOT_Management.this);
                    tv_add_note_print.setText(add_note_print);


                    if (Itemtype.toString().equals("Item")) {

                        Cursor modcursorc = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE parent = '" + hii + "' AND parentid = '" + idid + "'  ", null);
                        if (modcursorc.moveToFirst()) {
                            Cursor modt = db1.rawQuery("Select SUM(total) from Table" + ItemIDtable + " WHERE parent = '" + name + "' AND parentid = '" + idid + "' ", null);
                            if (modt.moveToFirst()) {
                                do {
                                    //row.removeView(tv3);
                                    float aq = modt.getFloat(0);
                                    aqq = String.valueOf(aq);
                                    aqq1 = Float.parseFloat(aqq) + Float.parseFloat(newtt);
                                    aqq2 = String.valueOf(aqq1);
                                } while (modt.moveToNext());
                            }
                            modt.close();

                            if (name.toString().length() > charlength) {
                                String str1 = name.substring(0, charlength);
                                String str2 = name.substring(charlength);
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                };
                                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                                    wifiSocket_kot2.WIFI_Write(setHTKOT);    //
                                    wifiSocket_kot2.WIFI_Write(normal);    //
                                    wifiSocket_kot2.WIFI_Write(total);
                                    wifiSocket_kot2.WIFI_Write(HT);    //
                                    wifiSocket_kot2.WIFI_Write(str1);
                                    wifiSocket_kot2.WIFI_Write(LF);    //
                                    wifiSocket_kot2.WIFI_Write("      ");
                                    wifiSocket_kot2.WIFI_Write(str2);
                                    wifiSocket_kot2.WIFI_Write(left);    //
                                    wifiSocket_kot2.WIFI_Write(LF);    //
                                }

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket_kot2.WIFI_Write(normal);    //
                                        wifiSocket_kot2.WIFI_Write(add_note_print);
                                        wifiSocket_kot2.WIFI_Write(LF);    //
                                    }
                                }
                            } else {
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                };
                                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                                    wifiSocket_kot2.WIFI_Write(setHTKOT);    //
                                    wifiSocket_kot2.WIFI_Write(normal);    //
                                    wifiSocket_kot2.WIFI_Write(total);
                                    wifiSocket_kot2.WIFI_Write(HT);    //
                                    wifiSocket_kot2.WIFI_Write(name);
                                    wifiSocket_kot2.WIFI_Write(left);    //
                                    wifiSocket_kot2.WIFI_Write(LF);    //
                                }

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                                        wifiSocket_kot2.WIFI_Write(normal);    //
                                        wifiSocket_kot2.WIFI_Write(add_note_print);
                                        wifiSocket_kot2.WIFI_Write(LF);    //
                                    }
                                }

                            }

                            do {
                                final String modiname = modcursorc.getString(2);
                                final String modiquan = modcursorc.getString(1);
                                String modiprice = modcursorc.getString(3);
                                String moditotal = modcursorc.getString(4);
                                final String modiid = modcursorc.getString(0);
                                String mod = modiname;


                                if (mod.toString().length() > charlength) {
                                    String str1 = mod.substring(0, charlength);
                                    String str2 = mod.substring(charlength);
                                    allbufmodifiers = new byte[][]{
                                            setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                    };
                                    if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                                        wifiSocket_kot2.WIFI_Write(setHTKOT);    //
                                        wifiSocket_kot2.WIFI_Write(normal);    //
                                        wifiSocket_kot2.WIFI_Write("");
                                        wifiSocket_kot2.WIFI_Write(HT);    //
                                        wifiSocket_kot2.WIFI_Write(">");
                                        wifiSocket_kot2.WIFI_Write(str1);
                                        wifiSocket_kot2.WIFI_Write(LF);    //
                                        wifiSocket_kot2.WIFI_Write("      ");
                                        wifiSocket_kot2.WIFI_Write(str2);
                                        wifiSocket_kot2.WIFI_Write(left);    //
                                        wifiSocket_kot2.WIFI_Write(LF);    //
                                    }

                                    if (tv_add_note_print.getText().toString().equals("")){

                                    }else {
                                        if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                                            wifiSocket_kot2.WIFI_Write(normal);    //
                                            wifiSocket_kot2.WIFI_Write(add_note_print);
                                            wifiSocket_kot2.WIFI_Write(LF);    //
                                        }
                                    }

                                } else {
                                    allbufmodifiers = new byte[][]{
                                            setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), mod.getBytes(), left, LF
                                    };
                                    if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                                        wifiSocket_kot2.WIFI_Write(setHTKOT);    //
                                        wifiSocket_kot2.WIFI_Write(normal);    //
                                        wifiSocket_kot2.WIFI_Write("");
                                        wifiSocket_kot2.WIFI_Write(HT);    //
                                        wifiSocket_kot2.WIFI_Write(">");
                                        wifiSocket_kot2.WIFI_Write(mod);
                                        wifiSocket_kot2.WIFI_Write(left);    //
                                        wifiSocket_kot2.WIFI_Write(LF);    //
                                    }

                                    if (tv_add_note_print.getText().toString().equals("")){

                                    }else {
                                        if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                                            wifiSocket_kot2.WIFI_Write(normal);    //
                                            wifiSocket_kot2.WIFI_Write(add_note_print);
                                            wifiSocket_kot2.WIFI_Write(LF);    //
                                        }
                                    }

                                }

                            } while (modcursorc.moveToNext());
                        } else {
                            if (name.toString().length() > charlength) {
                                String str1 = name.substring(0, charlength);
                                String str2 = name.substring(charlength);
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                };
                                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                                    wifiSocket_kot2.WIFI_Write(setHTKOT);    //
                                    wifiSocket_kot2.WIFI_Write(normal);    //
                                    wifiSocket_kot2.WIFI_Write(total);
                                    wifiSocket_kot2.WIFI_Write(HT);    //
                                    wifiSocket_kot2.WIFI_Write(str1);
                                    wifiSocket_kot2.WIFI_Write(LF);    //
                                    wifiSocket_kot2.WIFI_Write("      ");
                                    wifiSocket_kot2.WIFI_Write(str2);
                                    wifiSocket_kot2.WIFI_Write(left);    //
                                    wifiSocket_kot2.WIFI_Write(LF);    //
                                }

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                                        wifiSocket_kot2.WIFI_Write(normal);    //
                                        wifiSocket_kot2.WIFI_Write(add_note_print);
                                        wifiSocket_kot2.WIFI_Write(LF);    //
                                    }
                                }

                            } else {
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                };
                                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                                    wifiSocket_kot2.WIFI_Write(setHTKOT);    //
                                    wifiSocket_kot2.WIFI_Write(normal);    //
                                    wifiSocket_kot2.WIFI_Write(total);
                                    wifiSocket_kot2.WIFI_Write(HT);    //
                                    wifiSocket_kot2.WIFI_Write(name);
                                    wifiSocket_kot2.WIFI_Write(left);    //
                                    wifiSocket_kot2.WIFI_Write(LF);    //
                                }

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                                        wifiSocket_kot2.WIFI_Write(normal);    //
                                        wifiSocket_kot2.WIFI_Write(add_note_print);
                                        wifiSocket_kot2.WIFI_Write(LF);    //
                                    }
                                }

                            }

                        }
                        modcursorc.close();
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();

            if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                wifiSocket_kot2.WIFI_Write(left);    //
                wifiSocket_kot2.WIFI_Write(un1);    //
                wifiSocket_kot2.WIFI_Write(str_line);
                wifiSocket_kot2.WIFI_Write(LF);    //
            }
            //feedcut();

            Cursor toalitems = db1.rawQuery("Select * from Table" + ItemIDtable, null);
            if (toalitems.moveToFirst()) {
                Cursor toalitems2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity <= '1.0'", null);
                if (toalitems2.moveToFirst()) {
                    int toalzx = toalitems2.getCount();
                    totalquanret1 = String.valueOf(toalzx);
                }
                toalitems2.close();

                Cursor toalitems1 = db1.rawQuery("Select sum(quantity) from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity > '1.0'", null);
                if (toalitems1.moveToFirst()) {
                    int toalzx = toalitems1.getInt(0);
                    totalquanret2 = String.valueOf(toalzx);
                }
                toalitems1.close();

                int cvvc = Integer.parseInt(totalquanret1) + Integer.parseInt(totalquanret2);
                String total = String.valueOf(cvvc);
                String totalqu = "No. of items : " + total;
                allbuf11 = new byte[][]{
                        left, setHT321, totalqu.getBytes(), LF
                };
                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                    wifiSocket_kot2.WIFI_Write(left);    //
                    wifiSocket_kot2.WIFI_Write(setHT321);    //
                    wifiSocket_kot2.WIFI_Write(totalqu);
                    wifiSocket_kot2.WIFI_Write(LF);    //
                }
            }
            toalitems.close();

            byte[][] allbuf = new byte[][]{
                    feedcut2
            };
            System.out.println("feedcut executed now 5");
            if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                wifiSocket_kot2.WIFI_Write(feedcut2);    //
            }

//            if (str_print_ty.toString().equals("POS")) {
//                if (textViewstatusnets.getText().toString().equals("ok")) {
//                    wifiSocket.WIFI_Write(feedcut2);    //
//                } else {
//                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
//                        wifiSocket2.WIFI_Write(feedcut2);    //
//                    } else {
//                        if (textViewstatussusbs.getText().toString().equals("ok")) {
//                            BluetoothPrintDriver.BT_Write(feedcut2);    //
//                        }
//                    }
//                }
//            }
        }
//        Toast.makeText(KOT_Management.this, "KOT printed", Toast.LENGTH_LONG).show();
    }

    public void printbillsplithome_kot3(String name_kot3) {
        byte[] setHT34M = {0x1b, 0x44, 0x04, 0x11, 0x19, 0x00};
        byte[] dotfeed = {0x1b, 0x4a, 0x10};
        byte[] HTRight = {0x1b, 0x61, 0x02, 0x09};
        byte[] HT = {0x09};
        byte[] HT1 = {0x09};
        byte[] LF = {0x0d, 0x0a};

        byte[] left = {0x1b, 0x61, 0x00};
        byte[] cen = {0x1b, 0x61, 0x01};
        byte[] right = {0x1b, 0x61, 0x02};
        byte[] bold = {0x1B,0x21,0x10};
        byte[] normal = {0x1d, 0x21, 0x00};
        byte[] horiz1 = {0x1b, 0x44, 0x19, 0x19, 0x00};
        byte[] horiz = {0x1b, 0x44, 0x04, 0x06, 0x04, 0x04, 0x0A, 0x00, 0x09, 0x30, 0x09, 0x31, 0x09, 0x32, 0x09, 0x33, 0x09, 0x34, 0x0d, 0x0a};

        byte[] un = {0x1b, 0x2d, 0x02};
        byte[] un1 = {0x1b, 0x2d, 0x00};
        String str_line = "";

        Cursor print_ty = db.rawQuery("SELECT * FROM Printer_type", null);
        if (print_ty.moveToFirst()) {
            str_print_ty = print_ty.getString(1);
        }
        print_ty.close();

        Cursor getprint_type = db.rawQuery("SELECT * FROM Printer_text_size", null);
        if (getprint_type.moveToFirst()) {
            String type = getprint_type.getString(1);

            Cursor cc = db.rawQuery("SELECT * FROM Printerreceiptsize", null);

            if (cc.moveToFirst()) {
                cc.moveToFirst();
                do {
                    NAME = cc.getString(1);
                    if (NAME.equals("3 inch")) {
                        if (str_print_ty.toString().equals("Generic") || str_print_ty.toString().equals("Epson/others")) {
                            setHT32 = new byte[]{0x1b, 0x44, 0x29, 0x00};//2 tabs 3"
                            setHT321 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x25, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                            setHT34 = new byte[]{0x1b, 0x44, 0x08, 0x20, 0x29, 0x00};//4 tabs 3"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 3"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                            nPaperWidth = 576;
                            charlength = 41;
                            HT1 = new byte[]{0x09};
                            str_line = "------------------------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "------------------------------------------------".getBytes(), LF

                            };
                        } else {
                            if (str_print_ty.toString().equals("POS")) {
                                setHT32 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT321 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x10, 0x15, 0x00};//4 tabs 3"
                                setHTKOT = new byte[]{0x1b, 0x44, 0x05, 0x00};//2 tabs 3"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                nPaperWidth = 576;
                                charlength = 23;
                                charlength1 = 46;
                                charlength2 = 69;
                                quanlentha = 4;
                                HT1 = new byte[]{0x2F};
                                str_line = "------------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------------".getBytes(), LF

                                };
                            }
                        }
                    } else {
                        if (str_print_ty.toString().equals("Generic")) {
//                            Toast.makeText(KOT_Management.this, "phi", Toast.LENGTH_SHORT).show();
                            setHT32 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 2"
                            setHT321 = new byte[]{0x1b, 0x44, 0x13, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x15, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                            setHT34 = new byte[]{0x1b, 0x44, 0x04, 0x12, 0x19, 0x00};//4 tabs 2"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 2"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                            nPaperWidth = 384;
                            charlength = 25;
                            HT1 = new byte[]{0x09};
                            str_line = "--------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "--------------------------------".getBytes(), LF
                            };
                        } else {
                            if (str_print_ty.toString().equals("Epson/others") || (str_print_ty.toString().equals("wiseposplus"))) {
//                            Toast.makeText(KOT_Management.this, "epson", Toast.LENGTH_SHORT).show();
                                setHT32 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 2"
                                setHT321 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x21, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                                setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x19, 0x21, 0x00};//4 tabs 2"
                                setHTKOT = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                                nPaperWidth = 384;
                                charlength = 28;
                                HT1 = new byte[]{0x09};
                                str_line = "------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------".getBytes(), LF
                                };
                            } else {
                                if (str_print_ty.toString().equals("POS")) {
                                    setHT32 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT321 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT3212 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 3"
                                    setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x12, 0x21, 0x00};//4 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x05, 0x08, 0x00};//4 tabs 2"
                                    setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x08, 0x09, 0x00};//4 tabs 2"
                                    setHTKOT = new byte[]{0x1b, 0x44, 0x05, 0x00};//2 tabs 3"
                                    feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                    nPaperWidth = 384;
                                    charlength = 11;
                                    charlength1 = 22;
                                    charlength2 = 33;
                                    quanlentha = 3;
                                    HT1 = new byte[]{0x2F};
                                    str_line = "--------------------------------";
                                    allbufline = new byte[][]{
                                            left, un1, "--------------------------------".getBytes(), LF
                                    };
                                }
                            }
                        }
                    }
                } while (cc.moveToNext());
            }
            cc.close();

        }
        getprint_type.close();

        String dd = "";
        TextView qazcvb = new TextView(KOT_Management.this);
        Cursor cvonnusb = db.rawQuery("SELECT * FROM BTConn", null);
        if (cvonnusb.moveToFirst()) {
            addgets = cvonnusb.getString(1);
            namegets = cvonnusb.getString(2);
            statussusbs = cvonnusb.getString(3);
            dd = cvonnusb.getString(4);
        }
        cvonnusb.close();
        qazcvb.setText(dd);
        if (qazcvb.getText().toString().equals("usb") && statussusbs.toString().equals("ok")) {
            runPrintCouponSequence();
        } else {

            Cursor c_kot3 = db.rawQuery("SELECT * FROM IPConn_KOT3", null);
            if (c_kot3.moveToFirst()) {
                ipnamegets_kot3 = c_kot3.getString(1);
                portgets_kot3 = c_kot3.getString(2);
                statusnets_kot3 = c_kot3.getString(3);
                name_kot3 = c_kot3.getString(5);
            }
            c_kot3.close();

            textViewstatusnets_kot3.setText(statusnets_kot3);

            Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
            if (getcom.moveToFirst()) {
                strcompanyname = getcom.getString(1);
            }
            getcom.close();

            tvkot.setText(strcompanyname);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf1 = new byte[][]{
                        bold, cen, strcompanyname.getBytes(), LF, LF

                };

                if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                    wifiSocket_kot3.WIFI_Write(bold);    //
                    wifiSocket_kot3.WIFI_Write(cen);    //
                    wifiSocket_kot3.WIFI_Write(strcompanyname);
                    wifiSocket_kot3.WIFI_Write(LF);    //
                    wifiSocket_kot3.WIFI_Write(LF);    //
                }
            }

            int one_11 = 0;
            Cursor cursor_6 = db1.rawQuery("Select MAX(tagg) from Table" + ItemIDtable, null);
            if (cursor_6.moveToFirst()) {
                one_11 = cursor_6.getInt(0);
            }
            cursor_6.close();

            int one111 = one_11+1;
            String one_1 = String.valueOf(one111);

            allbufKOT = new byte[][]{
                    un, cen, "Order Ticket copy".getBytes(), LF
            };
            if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                wifiSocket_kot3.WIFI_Write(normal);    //
                wifiSocket_kot3.WIFI_Write(un);    //
                wifiSocket_kot3.WIFI_Write(cen);    //
                wifiSocket_kot3.WIFI_Write("Order Ticket copy "+one_1);
                wifiSocket_kot3.WIFI_Write(LF);    //
            }

            Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
            if (vbnm.moveToFirst()) {
                assa1 = vbnm.getString(1);
                assa2 = vbnm.getString(2);
            }
            vbnm.close();
            TextView cx = new TextView(KOT_Management.this);
            cx.setText(assa1);
            String tablen = "";
            if (cx.getText().toString().equals("")) {
                tablen = "Tab" + assa2;
            } else {
                tablen = "Tab" + assa1;
            }

            if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                wifiSocket_kot3.WIFI_Write(left);    //
                wifiSocket_kot3.WIFI_Write(un1);    //
                wifiSocket_kot3.WIFI_Write(tablen);
                wifiSocket_kot3.WIFI_Write(LF);    //
            }

            SimpleDateFormat normal2 = new SimpleDateFormat("dd MMM yyyy");
            final String normal1 = normal2.format(new Date());

            Date dt = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
            final String time1 = sdf1.format(dt);

            Date dtt_new = new Date();
            SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");
            String time24_new = sdf1t_new.format(dtt_new);

            Date dtt = new Date();
            SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
            String time24 = sdf1t.format(dtt);

            allbuf10 = new byte[][]{
                    setHT321, left, normal1.getBytes(), HT, "   ".getBytes(), time1.getBytes(), LF
            };
            if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                wifiSocket_kot3.WIFI_Write(setHT321);    //
                wifiSocket_kot3.WIFI_Write(left);    //
                wifiSocket_kot3.WIFI_Write(normal1);
                wifiSocket_kot3.WIFI_Write(HT);    //
                wifiSocket_kot3.WIFI_Write("  ");
                wifiSocket_kot3.WIFI_Write(time1);
                wifiSocket_kot3.WIFI_Write(LF);    //
            }


            if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                wifiSocket_kot3.WIFI_Write(left);    //
                wifiSocket_kot3.WIFI_Write(un1);    //
                wifiSocket_kot3.WIFI_Write(str_line);
                wifiSocket_kot3.WIFI_Write(LF);    //
            }

            allbufqty = new byte[][]{
                    setHTKOT, normal, "Qty".getBytes(), HT, "Item".getBytes(), left, LF
            };
            if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                wifiSocket_kot3.WIFI_Write(setHTKOT);    //
                wifiSocket_kot3.WIFI_Write(normal);    //
                wifiSocket_kot3.WIFI_Write("Qty");
                wifiSocket_kot3.WIFI_Write(HT);    //
                wifiSocket_kot3.WIFI_Write("Item");
                wifiSocket_kot3.WIFI_Write(left);    //
                wifiSocket_kot3.WIFI_Write(LF);    //
            }

            if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                wifiSocket_kot3.WIFI_Write(left);    //
                wifiSocket_kot3.WIFI_Write(un1);    //
                wifiSocket_kot3.WIFI_Write(str_line);
                wifiSocket_kot3.WIFI_Write(LF);    //
            }

            Cursor cursor = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE dept_name = '"+name_kot3+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (cursor.moveToFirst()) {
                do {
                    String Itemtype = cursor.getString(5);
                    String total = cursor.getString(20);
                    final String idid = cursor.getString(0);
                    String name = cursor.getString(2);
                    final String iidd = cursor.getString(0);
                    final String hii = cursor.getString(2);
                    final String newtt = cursor.getString(4);
                    final float f = Float.parseFloat(cursor.getString(3));
                    String price = String.valueOf(f);
                    String stat = cursor.getString(16);
                    String sev = cursor.getString(7);
                    String add_note_print = cursor.getString(30);

                    System.out.println("name is therre "+name);
                    TextView tvbh = new TextView(KOT_Management.this);

                    tvbh.setText(stat);


                    TextView tv_add_note_print = new TextView(KOT_Management.this);
                    tv_add_note_print.setText(add_note_print);


                    if (Itemtype.toString().equals("Item")) {

                        Cursor modcursorc = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE parent = '" + hii + "' AND parentid = '" + idid + "'  ", null);
                        if (modcursorc.moveToFirst()) {
                            Cursor modt = db1.rawQuery("Select SUM(total) from Table" + ItemIDtable + " WHERE parent = '" + name + "' AND parentid = '" + idid + "' ", null);
                            if (modt.moveToFirst()) {
                                do {
                                    //row.removeView(tv3);
                                    float aq = modt.getFloat(0);
                                    aqq = String.valueOf(aq);
                                    aqq1 = Float.parseFloat(aqq) + Float.parseFloat(newtt);
                                    aqq2 = String.valueOf(aqq1);
                                } while (modt.moveToNext());
                            }
                            modt.close();

                            if (name.toString().length() > charlength) {
                                String str1 = name.substring(0, charlength);
                                String str2 = name.substring(charlength);
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                };
                                if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                                    wifiSocket_kot3.WIFI_Write(setHTKOT);    //
                                    wifiSocket_kot3.WIFI_Write(normal);    //
                                    wifiSocket_kot3.WIFI_Write(total);
                                    wifiSocket_kot3.WIFI_Write(HT);    //
                                    wifiSocket_kot3.WIFI_Write(str1);
                                    wifiSocket_kot3.WIFI_Write(LF);    //
                                    wifiSocket_kot3.WIFI_Write("      ");
                                    wifiSocket_kot3.WIFI_Write(str2);
                                    wifiSocket_kot3.WIFI_Write(left);    //
                                    wifiSocket_kot3.WIFI_Write(LF);    //
                                }

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket_kot3.WIFI_Write(normal);    //
                                        wifiSocket_kot3.WIFI_Write(add_note_print);
                                        wifiSocket_kot3.WIFI_Write(LF);    //
                                    }
                                }
                            } else {
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                };
                                if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                                    wifiSocket_kot3.WIFI_Write(setHTKOT);    //
                                    wifiSocket_kot3.WIFI_Write(normal);    //
                                    wifiSocket_kot3.WIFI_Write(total);
                                    wifiSocket_kot3.WIFI_Write(HT);    //
                                    wifiSocket_kot3.WIFI_Write(name);
                                    wifiSocket_kot3.WIFI_Write(left);    //
                                    wifiSocket_kot3.WIFI_Write(LF);    //
                                }

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                                        wifiSocket_kot3.WIFI_Write(normal);    //
                                        wifiSocket_kot3.WIFI_Write(add_note_print);
                                        wifiSocket_kot3.WIFI_Write(LF);    //
                                    }
                                }

                            }

                            do {
                                final String modiname = modcursorc.getString(2);
                                final String modiquan = modcursorc.getString(1);
                                String modiprice = modcursorc.getString(3);
                                String moditotal = modcursorc.getString(4);
                                final String modiid = modcursorc.getString(0);
                                String mod = modiname;


                                if (mod.toString().length() > charlength) {
                                    String str1 = mod.substring(0, charlength);
                                    String str2 = mod.substring(charlength);
                                    allbufmodifiers = new byte[][]{
                                            setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                    };
                                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                                        wifiSocket_kot3.WIFI_Write(setHTKOT);    //
                                        wifiSocket_kot3.WIFI_Write(normal);    //
                                        wifiSocket_kot3.WIFI_Write("");
                                        wifiSocket_kot3.WIFI_Write(HT);    //
                                        wifiSocket_kot3.WIFI_Write(">");
                                        wifiSocket_kot3.WIFI_Write(str1);
                                        wifiSocket_kot3.WIFI_Write(LF);    //
                                        wifiSocket_kot3.WIFI_Write("      ");
                                        wifiSocket_kot3.WIFI_Write(str2);
                                        wifiSocket_kot3.WIFI_Write(left);    //
                                        wifiSocket_kot3.WIFI_Write(LF);    //
                                    }

                                    if (tv_add_note_print.getText().toString().equals("")){

                                    }else {
                                        if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                                            wifiSocket_kot3.WIFI_Write(normal);    //
                                            wifiSocket_kot3.WIFI_Write(add_note_print);
                                            wifiSocket_kot3.WIFI_Write(LF);    //
                                        }
                                    }

                                } else {
                                    allbufmodifiers = new byte[][]{
                                            setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), mod.getBytes(), left, LF
                                    };
                                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                                        wifiSocket_kot3.WIFI_Write(setHTKOT);    //
                                        wifiSocket_kot3.WIFI_Write(normal);    //
                                        wifiSocket_kot3.WIFI_Write("");
                                        wifiSocket_kot3.WIFI_Write(HT);    //
                                        wifiSocket_kot3.WIFI_Write(">");
                                        wifiSocket_kot3.WIFI_Write(mod);
                                        wifiSocket_kot3.WIFI_Write(left);    //
                                        wifiSocket_kot3.WIFI_Write(LF);    //
                                    }

                                    if (tv_add_note_print.getText().toString().equals("")){

                                    }else {
                                        if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                                            wifiSocket_kot3.WIFI_Write(normal);    //
                                            wifiSocket_kot3.WIFI_Write(add_note_print);
                                            wifiSocket_kot3.WIFI_Write(LF);    //
                                        }
                                    }

                                }

                            } while (modcursorc.moveToNext());
                        } else {
                            if (name.toString().length() > charlength) {
                                String str1 = name.substring(0, charlength);
                                String str2 = name.substring(charlength);
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                };
                                if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                                    wifiSocket_kot3.WIFI_Write(setHTKOT);    //
                                    wifiSocket_kot3.WIFI_Write(normal);    //
                                    wifiSocket_kot3.WIFI_Write(total);
                                    wifiSocket_kot3.WIFI_Write(HT);    //
                                    wifiSocket_kot3.WIFI_Write(str1);
                                    wifiSocket_kot3.WIFI_Write(LF);    //
                                    wifiSocket_kot3.WIFI_Write("      ");
                                    wifiSocket_kot3.WIFI_Write(str2);
                                    wifiSocket_kot3.WIFI_Write(left);    //
                                    wifiSocket_kot3.WIFI_Write(LF);    //
                                }

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                                        wifiSocket_kot3.WIFI_Write(normal);    //
                                        wifiSocket_kot3.WIFI_Write(add_note_print);
                                        wifiSocket_kot3.WIFI_Write(LF);    //
                                    }
                                }

                            } else {
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                };
                                if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                                    wifiSocket_kot3.WIFI_Write(setHTKOT);    //
                                    wifiSocket_kot3.WIFI_Write(normal);    //
                                    wifiSocket_kot3.WIFI_Write(total);
                                    wifiSocket_kot3.WIFI_Write(HT);    //
                                    wifiSocket_kot3.WIFI_Write(name);
                                    wifiSocket_kot3.WIFI_Write(left);    //
                                    wifiSocket_kot3.WIFI_Write(LF);    //
                                }

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                                        wifiSocket_kot3.WIFI_Write(normal);    //
                                        wifiSocket_kot3.WIFI_Write(add_note_print);
                                        wifiSocket_kot3.WIFI_Write(LF);    //
                                    }
                                }

                            }

                        }
                        modcursorc.close();
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();

            if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                wifiSocket_kot3.WIFI_Write(left);    //
                wifiSocket_kot3.WIFI_Write(un1);    //
                wifiSocket_kot3.WIFI_Write(str_line);
                wifiSocket_kot3.WIFI_Write(LF);    //
            }
            //feedcut();

            Cursor toalitems = db1.rawQuery("Select * from Table" + ItemIDtable, null);
            if (toalitems.moveToFirst()) {
                Cursor toalitems2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity <= '1.0'", null);
                if (toalitems2.moveToFirst()) {
                    int toalzx = toalitems2.getCount();
                    totalquanret1 = String.valueOf(toalzx);
                }
                toalitems2.close();

                Cursor toalitems1 = db1.rawQuery("Select sum(quantity) from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity > '1.0'", null);
                if (toalitems1.moveToFirst()) {
                    int toalzx = toalitems1.getInt(0);
                    totalquanret2 = String.valueOf(toalzx);
                }
                toalitems1.close();

                int cvvc = Integer.parseInt(totalquanret1) + Integer.parseInt(totalquanret2);
                String total = String.valueOf(cvvc);
                String totalqu = "No. of items : " + total;
                allbuf11 = new byte[][]{
                        left, setHT321, totalqu.getBytes(), LF
                };
                if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                    wifiSocket_kot3.WIFI_Write(left);    //
                    wifiSocket_kot3.WIFI_Write(setHT321);    //
                    wifiSocket_kot3.WIFI_Write(totalqu);
                    wifiSocket_kot3.WIFI_Write(LF);    //
                }
            }
            toalitems.close();

            byte[][] allbuf = new byte[][]{
                    feedcut2
            };
            System.out.println("feedcut executed now 6");
            if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                wifiSocket_kot3.WIFI_Write(feedcut2);    //
            }

//            if (str_print_ty.toString().equals("POS")) {
//                if (textViewstatusnets.getText().toString().equals("ok")) {
//                    wifiSocket.WIFI_Write(feedcut2);    //
//                } else {
//                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
//                        wifiSocket2.WIFI_Write(feedcut2);    //
//                    } else {
//                        if (textViewstatussusbs.getText().toString().equals("ok")) {
//                            BluetoothPrintDriver.BT_Write(feedcut2);    //
//                        }
//                    }
//                }
//            }
        }
//        Toast.makeText(KOT_Management.this, "KOT printed", Toast.LENGTH_LONG).show();
    }

    public void printbillsplithome_kot4(String name_kot4) {
        byte[] setHT34M = {0x1b, 0x44, 0x04, 0x11, 0x19, 0x00};
        byte[] dotfeed = {0x1b, 0x4a, 0x10};
        byte[] HTRight = {0x1b, 0x61, 0x02, 0x09};
        byte[] HT = {0x09};
        byte[] HT1 = {0x09};
        byte[] LF = {0x0d, 0x0a};

        byte[] left = {0x1b, 0x61, 0x00};
        byte[] cen = {0x1b, 0x61, 0x01};
        byte[] right = {0x1b, 0x61, 0x02};
        byte[] bold = {0x1B,0x21,0x10};
        byte[] normal = {0x1d, 0x21, 0x00};
        byte[] horiz1 = {0x1b, 0x44, 0x19, 0x19, 0x00};
        byte[] horiz = {0x1b, 0x44, 0x04, 0x06, 0x04, 0x04, 0x0A, 0x00, 0x09, 0x30, 0x09, 0x31, 0x09, 0x32, 0x09, 0x33, 0x09, 0x34, 0x0d, 0x0a};

        byte[] un = {0x1b, 0x2d, 0x02};
        byte[] un1 = {0x1b, 0x2d, 0x00};
        String str_line = "";

        Cursor print_ty = db.rawQuery("SELECT * FROM Printer_type", null);
        if (print_ty.moveToFirst()) {
            str_print_ty = print_ty.getString(1);
        }
        print_ty.close();

        Cursor getprint_type = db.rawQuery("SELECT * FROM Printer_text_size", null);
        if (getprint_type.moveToFirst()) {
            String type = getprint_type.getString(1);

            Cursor cc = db.rawQuery("SELECT * FROM Printerreceiptsize", null);

            if (cc.moveToFirst()) {
                cc.moveToFirst();
                do {
                    NAME = cc.getString(1);
                    if (NAME.equals("3 inch")) {
                        if (str_print_ty.toString().equals("Generic") || str_print_ty.toString().equals("Epson/others")) {
                            setHT32 = new byte[]{0x1b, 0x44, 0x29, 0x00};//2 tabs 3"
                            setHT321 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x25, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                            setHT34 = new byte[]{0x1b, 0x44, 0x08, 0x20, 0x29, 0x00};//4 tabs 3"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 3"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                            nPaperWidth = 576;
                            charlength = 41;
                            HT1 = new byte[]{0x09};
                            str_line = "------------------------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "------------------------------------------------".getBytes(), LF

                            };
                        } else {
                            if (str_print_ty.toString().equals("POS")) {
                                setHT32 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT321 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x10, 0x15, 0x00};//4 tabs 3"
                                setHTKOT = new byte[]{0x1b, 0x44, 0x05, 0x00};//2 tabs 3"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                nPaperWidth = 576;
                                charlength = 23;
                                charlength1 = 46;
                                charlength2 = 69;
                                quanlentha = 4;
                                HT1 = new byte[]{0x2F};
                                str_line = "------------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------------".getBytes(), LF

                                };
                            }
                        }
                    } else {
                        if (str_print_ty.toString().equals("Generic")) {
//                            Toast.makeText(KOT_Management.this, "phi", Toast.LENGTH_SHORT).show();
                            setHT32 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 2"
                            setHT321 = new byte[]{0x1b, 0x44, 0x13, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x15, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                            setHT34 = new byte[]{0x1b, 0x44, 0x04, 0x12, 0x19, 0x00};//4 tabs 2"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 2"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                            nPaperWidth = 384;
                            charlength = 25;
                            HT1 = new byte[]{0x09};
                            str_line = "--------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "--------------------------------".getBytes(), LF
                            };
                        } else {
                            if (str_print_ty.toString().equals("Epson/others") || (str_print_ty.toString().equals("wiseposplus"))) {
//                            Toast.makeText(KOT_Management.this, "epson", Toast.LENGTH_SHORT).show();
                                setHT32 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 2"
                                setHT321 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x21, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                                setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x19, 0x21, 0x00};//4 tabs 2"
                                setHTKOT = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                                nPaperWidth = 384;
                                charlength = 28;
                                HT1 = new byte[]{0x09};
                                str_line = "------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------".getBytes(), LF
                                };
                            } else {
                                if (str_print_ty.toString().equals("POS")) {
                                    setHT32 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT321 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT3212 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 3"
                                    setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x12, 0x21, 0x00};//4 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x05, 0x08, 0x00};//4 tabs 2"
                                    setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x08, 0x09, 0x00};//4 tabs 2"
                                    setHTKOT = new byte[]{0x1b, 0x44, 0x05, 0x00};//2 tabs 3"
                                    feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                    nPaperWidth = 384;
                                    charlength = 11;
                                    charlength1 = 22;
                                    charlength2 = 33;
                                    quanlentha = 3;
                                    HT1 = new byte[]{0x2F};
                                    str_line = "--------------------------------";
                                    allbufline = new byte[][]{
                                            left, un1, "--------------------------------".getBytes(), LF
                                    };
                                }
                            }
                        }
                    }
                } while (cc.moveToNext());
            }
            cc.close();

        }
        getprint_type.close();

        String dd = "";
        TextView qazcvb = new TextView(KOT_Management.this);
        Cursor cvonnusb = db.rawQuery("SELECT * FROM BTConn", null);
        if (cvonnusb.moveToFirst()) {
            addgets = cvonnusb.getString(1);
            namegets = cvonnusb.getString(2);
            statussusbs = cvonnusb.getString(3);
            dd = cvonnusb.getString(4);
        }
        cvonnusb.close();
        qazcvb.setText(dd);
        if (qazcvb.getText().toString().equals("usb") && statussusbs.toString().equals("ok")) {
            runPrintCouponSequence();
        } else {

            Cursor c_kot4 = db.rawQuery("SELECT * FROM IPConn_KOT4", null);
            if (c_kot4.moveToFirst()) {
                ipnamegets_kot4 = c_kot4.getString(1);
                portgets_kot4 = c_kot4.getString(2);
                statusnets_kot4 = c_kot4.getString(3);
                name_kot4 = c_kot4.getString(5);
            }
            c_kot4.close();

            textViewstatusnets_kot4.setText(statusnets_kot4);

            Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
            if (getcom.moveToFirst()) {
                strcompanyname = getcom.getString(1);
            }
            getcom.close();

            tvkot.setText(strcompanyname);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf1 = new byte[][]{
                        bold, cen, strcompanyname.getBytes(), LF, LF

                };

                if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                    wifiSocket_kot4.WIFI_Write(bold);    //
                    wifiSocket_kot4.WIFI_Write(cen);    //
                    wifiSocket_kot4.WIFI_Write(strcompanyname);
                    wifiSocket_kot4.WIFI_Write(LF);    //
                    wifiSocket_kot4.WIFI_Write(LF);    //
                }
            }

            int one_11 = 0;
            Cursor cursor_6 = db1.rawQuery("Select MAX(tagg) from Table" + ItemIDtable, null);
            if (cursor_6.moveToFirst()) {
                one_11 = cursor_6.getInt(0);
            }
            cursor_6.close();

            int one111 = one_11+1;
            String one_1 = String.valueOf(one111);

            allbufKOT = new byte[][]{
                    un, cen, "Order Ticket copy".getBytes(), LF
            };
            if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                wifiSocket_kot4.WIFI_Write(normal);    //
                wifiSocket_kot4.WIFI_Write(un);    //
                wifiSocket_kot4.WIFI_Write(cen);    //
                wifiSocket_kot4.WIFI_Write("Order Ticket copy "+one_1);
                wifiSocket_kot4.WIFI_Write(LF);    //
            }

            Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
            if (vbnm.moveToFirst()) {
                assa1 = vbnm.getString(1);
                assa2 = vbnm.getString(2);
            }
            vbnm.close();
            TextView cx = new TextView(KOT_Management.this);
            cx.setText(assa1);
            String tablen = "";
            if (cx.getText().toString().equals("")) {
                tablen = "Tab" + assa2;
            } else {
                tablen = "Tab" + assa1;
            }

            allbuf11 = new byte[][]{
                    left, un1, setHT321, tablen.getBytes(), LF
            };

            if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                wifiSocket_kot4.WIFI_Write(left);    //
                wifiSocket_kot4.WIFI_Write(un1);    //
                wifiSocket_kot4.WIFI_Write(tablen);
                wifiSocket_kot4.WIFI_Write(LF);    //
            }

            SimpleDateFormat normal2 = new SimpleDateFormat("dd MMM yyyy");
            final String normal1 = normal2.format(new Date());

            Date dt = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
            final String time1 = sdf1.format(dt);

            Date dtt_new = new Date();
            SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");
            String time24_new = sdf1t_new.format(dtt_new);

            Date dtt = new Date();
            SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
            String time24 = sdf1t.format(dtt);

            allbuf10 = new byte[][]{
                    setHT321, left, normal1.getBytes(), HT, "   ".getBytes(), time1.getBytes(), LF
            };
            if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                wifiSocket_kot4.WIFI_Write(setHT321);    //
                wifiSocket_kot4.WIFI_Write(left);    //
                wifiSocket_kot4.WIFI_Write(normal1);
                wifiSocket_kot4.WIFI_Write(HT);    //
                wifiSocket_kot4.WIFI_Write("  ");
                wifiSocket_kot4.WIFI_Write(time1);
                wifiSocket_kot4.WIFI_Write(LF);    //
            }


            if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                wifiSocket_kot4.WIFI_Write(left);    //
                wifiSocket_kot4.WIFI_Write(un1);    //
                wifiSocket_kot4.WIFI_Write(str_line);
                wifiSocket_kot4.WIFI_Write(LF);    //
            }

            allbufqty = new byte[][]{
                    setHTKOT, normal, "Qty".getBytes(), HT, "Item".getBytes(), left, LF
            };
            if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                wifiSocket_kot4.WIFI_Write(setHTKOT);    //
                wifiSocket_kot4.WIFI_Write(normal);    //
                wifiSocket_kot4.WIFI_Write("Qty");
                wifiSocket_kot4.WIFI_Write(HT);    //
                wifiSocket_kot4.WIFI_Write("Item");
                wifiSocket_kot4.WIFI_Write(left);    //
                wifiSocket_kot4.WIFI_Write(LF);    //
            }

            if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                wifiSocket_kot4.WIFI_Write(left);    //
                wifiSocket_kot4.WIFI_Write(un1);    //
                wifiSocket_kot4.WIFI_Write(str_line);
                wifiSocket_kot4.WIFI_Write(LF);    //
            }

            Cursor cursor = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE dept_name = '"+name_kot4+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (cursor.moveToFirst()) {
                do {
                    String Itemtype = cursor.getString(5);
                    String total = cursor.getString(20);
                    final String idid = cursor.getString(0);
                    String name = cursor.getString(2);
                    final String iidd = cursor.getString(0);
                    final String hii = cursor.getString(2);
                    final String newtt = cursor.getString(4);
                    final float f = Float.parseFloat(cursor.getString(3));
                    String price = String.valueOf(f);
                    String stat = cursor.getString(16);
                    String sev = cursor.getString(7);
                    String add_note_print = cursor.getString(30);

                    System.out.println("name is therre "+name);
                    TextView tvbh = new TextView(KOT_Management.this);

                    tvbh.setText(stat);


                    TextView tv_add_note_print = new TextView(KOT_Management.this);
                    tv_add_note_print.setText(add_note_print);


                    if (Itemtype.toString().equals("Item")) {

                        Cursor modcursorc = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE parent = '" + hii + "' AND parentid = '" + idid + "'  ", null);
                        if (modcursorc.moveToFirst()) {
                            Cursor modt = db1.rawQuery("Select SUM(total) from Table" + ItemIDtable + " WHERE parent = '" + name + "' AND parentid = '" + idid + "' ", null);
                            if (modt.moveToFirst()) {
                                do {
                                    //row.removeView(tv3);
                                    float aq = modt.getFloat(0);
                                    aqq = String.valueOf(aq);
                                    aqq1 = Float.parseFloat(aqq) + Float.parseFloat(newtt);
                                    aqq2 = String.valueOf(aqq1);
                                } while (modt.moveToNext());
                            }
                            modt.close();

                            if (name.toString().length() > charlength) {
                                String str1 = name.substring(0, charlength);
                                String str2 = name.substring(charlength);
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                };
                                if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                    wifiSocket_kot4.WIFI_Write(setHTKOT);    //
                                    wifiSocket_kot4.WIFI_Write(normal);    //
                                    wifiSocket_kot4.WIFI_Write(total);
                                    wifiSocket_kot4.WIFI_Write(HT);    //
                                    wifiSocket_kot4.WIFI_Write(str1);
                                    wifiSocket_kot4.WIFI_Write(LF);    //
                                    wifiSocket_kot4.WIFI_Write("      ");
                                    wifiSocket_kot4.WIFI_Write(str2);
                                    wifiSocket_kot4.WIFI_Write(left);    //
                                    wifiSocket_kot4.WIFI_Write(LF);    //
                                }

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket_kot4.WIFI_Write(normal);    //
                                        wifiSocket_kot4.WIFI_Write(add_note_print);
                                        wifiSocket_kot4.WIFI_Write(LF);    //
                                    }
                                }
                            } else {
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                };
                                if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                    wifiSocket_kot4.WIFI_Write(setHTKOT);    //
                                    wifiSocket_kot4.WIFI_Write(normal);    //
                                    wifiSocket_kot4.WIFI_Write(total);
                                    wifiSocket_kot4.WIFI_Write(HT);    //
                                    wifiSocket_kot4.WIFI_Write(name);
                                    wifiSocket_kot4.WIFI_Write(left);    //
                                    wifiSocket_kot4.WIFI_Write(LF);    //
                                }

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                        wifiSocket_kot4.WIFI_Write(normal);    //
                                        wifiSocket_kot4.WIFI_Write(add_note_print);
                                        wifiSocket_kot4.WIFI_Write(LF);    //
                                    }
                                }

                            }

                            do {
                                final String modiname = modcursorc.getString(2);
                                final String modiquan = modcursorc.getString(1);
                                String modiprice = modcursorc.getString(3);
                                String moditotal = modcursorc.getString(4);
                                final String modiid = modcursorc.getString(0);
                                String mod = modiname;


                                if (mod.toString().length() > charlength) {
                                    String str1 = mod.substring(0, charlength);
                                    String str2 = mod.substring(charlength);
                                    allbufmodifiers = new byte[][]{
                                            setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                    };
                                    if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                        wifiSocket_kot4.WIFI_Write(setHTKOT);    //
                                        wifiSocket_kot4.WIFI_Write(normal);    //
                                        wifiSocket_kot4.WIFI_Write("");
                                        wifiSocket_kot4.WIFI_Write(HT);    //
                                        wifiSocket_kot4.WIFI_Write(">");
                                        wifiSocket_kot4.WIFI_Write(str1);
                                        wifiSocket_kot4.WIFI_Write(LF);    //
                                        wifiSocket_kot4.WIFI_Write("      ");
                                        wifiSocket_kot4.WIFI_Write(str2);
                                        wifiSocket_kot4.WIFI_Write(left);    //
                                        wifiSocket_kot4.WIFI_Write(LF);    //
                                    }

                                    if (tv_add_note_print.getText().toString().equals("")){

                                    }else {
                                        if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                            wifiSocket_kot4.WIFI_Write(normal);    //
                                            wifiSocket_kot4.WIFI_Write(add_note_print);
                                            wifiSocket_kot4.WIFI_Write(LF);    //
                                        }
                                    }

                                } else {
                                    allbufmodifiers = new byte[][]{
                                            setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), mod.getBytes(), left, LF
                                    };
                                    if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                        wifiSocket_kot4.WIFI_Write(setHTKOT);    //
                                        wifiSocket_kot4.WIFI_Write(normal);    //
                                        wifiSocket_kot4.WIFI_Write("");
                                        wifiSocket_kot4.WIFI_Write(HT);    //
                                        wifiSocket_kot4.WIFI_Write(">");
                                        wifiSocket_kot4.WIFI_Write(mod);
                                        wifiSocket_kot4.WIFI_Write(left);    //
                                        wifiSocket_kot4.WIFI_Write(LF);    //
                                    }

                                    if (tv_add_note_print.getText().toString().equals("")){

                                    }else {
                                        if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                            wifiSocket_kot4.WIFI_Write(normal);    //
                                            wifiSocket_kot4.WIFI_Write(add_note_print);
                                            wifiSocket_kot4.WIFI_Write(LF);    //
                                        }
                                    }

                                }

                            } while (modcursorc.moveToNext());
                        } else {
                            if (name.toString().length() > charlength) {
                                String str1 = name.substring(0, charlength);
                                String str2 = name.substring(charlength);
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                };
                                if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                    wifiSocket_kot4.WIFI_Write(setHTKOT);    //
                                    wifiSocket_kot4.WIFI_Write(normal);    //
                                    wifiSocket_kot4.WIFI_Write(total);
                                    wifiSocket_kot4.WIFI_Write(HT);    //
                                    wifiSocket_kot4.WIFI_Write(str1);
                                    wifiSocket_kot4.WIFI_Write(LF);    //
                                    wifiSocket_kot4.WIFI_Write("      ");
                                    wifiSocket_kot4.WIFI_Write(str2);
                                    wifiSocket_kot4.WIFI_Write(left);    //
                                    wifiSocket_kot4.WIFI_Write(LF);    //
                                }

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                        wifiSocket_kot4.WIFI_Write(normal);    //
                                        wifiSocket_kot4.WIFI_Write(add_note_print);
                                        wifiSocket_kot4.WIFI_Write(LF);    //
                                    }
                                }

                            } else {
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                };
                                if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                    wifiSocket_kot4.WIFI_Write(setHTKOT);    //
                                    wifiSocket_kot4.WIFI_Write(normal);    //
                                    wifiSocket_kot4.WIFI_Write(total);
                                    wifiSocket_kot4.WIFI_Write(HT);    //
                                    wifiSocket_kot4.WIFI_Write(name);
                                    wifiSocket_kot4.WIFI_Write(left);    //
                                    wifiSocket_kot4.WIFI_Write(LF);    //
                                }

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                        wifiSocket_kot4.WIFI_Write(normal);    //
                                        wifiSocket_kot4.WIFI_Write(add_note_print);
                                        wifiSocket_kot4.WIFI_Write(LF);    //
                                    }
                                }

                            }

                        }
                        modcursorc.close();
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();

            if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                wifiSocket_kot4.WIFI_Write(left);    //
                wifiSocket_kot4.WIFI_Write(un1);    //
                wifiSocket_kot4.WIFI_Write(str_line);
                wifiSocket_kot4.WIFI_Write(LF);    //
            }
            //feedcut();

            Cursor toalitems = db1.rawQuery("Select * from Table" + ItemIDtable, null);
            if (toalitems.moveToFirst()) {
                Cursor toalitems2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity <= '1.0'", null);
                if (toalitems2.moveToFirst()) {
                    int toalzx = toalitems2.getCount();
                    totalquanret1 = String.valueOf(toalzx);
                }
                toalitems2.close();

                Cursor toalitems1 = db1.rawQuery("Select sum(quantity) from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity > '1.0'", null);
                if (toalitems1.moveToFirst()) {
                    int toalzx = toalitems1.getInt(0);
                    totalquanret2 = String.valueOf(toalzx);
                }
                toalitems1.close();

                int cvvc = Integer.parseInt(totalquanret1) + Integer.parseInt(totalquanret2);
                String total = String.valueOf(cvvc);
                String totalqu = "No. of items : " + total;
                allbuf11 = new byte[][]{
                        left, setHT321, totalqu.getBytes(), LF
                };
                if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                    wifiSocket_kot4.WIFI_Write(left);    //
                    wifiSocket_kot4.WIFI_Write(setHT321);    //
                    wifiSocket_kot4.WIFI_Write(totalqu);
                    wifiSocket_kot4.WIFI_Write(LF);    //
                }
            }
            toalitems.close();

            byte[][] allbuf = new byte[][]{
                    feedcut2
            };
            System.out.println("feedcut executed now 7");
            if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                wifiSocket_kot4.WIFI_Write(feedcut2);    //
            }

//            if (str_print_ty.toString().equals("POS")) {
//                if (textViewstatusnets.getText().toString().equals("ok")) {
//                    wifiSocket.WIFI_Write(feedcut2);    //
//                } else {
//                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
//                        wifiSocket2.WIFI_Write(feedcut2);    //
//                    } else {
//                        if (textViewstatussusbs.getText().toString().equals("ok")) {
//                            BluetoothPrintDriver.BT_Write(feedcut2);    //
//                        }
//                    }
//                }
//            }
        }
//        Toast.makeText(KOT_Management.this, "KOT printed", Toast.LENGTH_LONG).show();
    }

    public void printbillsplithome_kot_BT(String name_kot1) {
        byte[] setHT34M = {0x1b, 0x44, 0x04, 0x11, 0x19, 0x00};
        byte[] dotfeed = {0x1b, 0x4a, 0x10};
        byte[] HTRight = {0x1b, 0x61, 0x02, 0x09};
        byte[] HT = {0x09};
        byte[] HT1 = {0x09};
        byte[] LF = {0x0d, 0x0a};

        byte[] left = {0x1b, 0x61, 0x00};
        byte[] cen = {0x1b, 0x61, 0x01};
        byte[] right = {0x1b, 0x61, 0x02};
        byte[] bold = {0x1B,0x21,0x10};
        byte[] normal = {0x1d, 0x21, 0x00};
        byte[] horiz1 = {0x1b, 0x44, 0x19, 0x19, 0x00};
        byte[] horiz = {0x1b, 0x44, 0x04, 0x06, 0x04, 0x04, 0x0A, 0x00, 0x09, 0x30, 0x09, 0x31, 0x09, 0x32, 0x09, 0x33, 0x09, 0x34, 0x0d, 0x0a};

        byte[] un = {0x1b, 0x2d, 0x02};
        byte[] un1 = {0x1b, 0x2d, 0x00};
        String str_line = "";

        Cursor print_ty = db.rawQuery("SELECT * FROM Printer_type", null);
        if (print_ty.moveToFirst()) {
            str_print_ty = print_ty.getString(1);
        }
        print_ty.close();

        Cursor getprint_type = db.rawQuery("SELECT * FROM Printer_text_size", null);
        if (getprint_type.moveToFirst()) {
            String type = getprint_type.getString(1);

            Cursor cc = db.rawQuery("SELECT * FROM Printerreceiptsize", null);

            if (cc.moveToFirst()) {
                cc.moveToFirst();
                do {
                    NAME = cc.getString(1);
                    if (NAME.equals("3 inch")) {
                        if (str_print_ty.toString().equals("Generic") || str_print_ty.toString().equals("Epson/others")) {
                            setHT32 = new byte[]{0x1b, 0x44, 0x29, 0x00};//2 tabs 3"
                            setHT321 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x25, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                            setHT34 = new byte[]{0x1b, 0x44, 0x08, 0x20, 0x29, 0x00};//4 tabs 3"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 3"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                            nPaperWidth = 576;
                            charlength = 41;
                            HT1 = new byte[]{0x09};
                            str_line = "------------------------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "------------------------------------------------".getBytes(), LF

                            };
                        } else {
                            if (str_print_ty.toString().equals("POS")) {
                                setHT32 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT321 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x10, 0x15, 0x00};//4 tabs 3"
                                setHTKOT = new byte[]{0x1b, 0x44, 0x05, 0x00};//2 tabs 3"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                nPaperWidth = 576;
                                charlength = 23;
                                charlength1 = 46;
                                charlength2 = 69;
                                quanlentha = 4;
                                HT1 = new byte[]{0x2F};
                                str_line = "------------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------------".getBytes(), LF

                                };
                            }
                        }
                    } else {
                        if (str_print_ty.toString().equals("Generic")) {
//                            Toast.makeText(KOT_Management.this, "phi", Toast.LENGTH_SHORT).show();
                            setHT32 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 2"
                            setHT321 = new byte[]{0x1b, 0x44, 0x13, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x15, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                            setHT34 = new byte[]{0x1b, 0x44, 0x04, 0x12, 0x19, 0x00};//4 tabs 2"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 2"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                            nPaperWidth = 384;
                            charlength = 25;
                            HT1 = new byte[]{0x09};
                            str_line = "--------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "--------------------------------".getBytes(), LF
                            };
                        } else {
                            if (str_print_ty.toString().equals("Epson/others")) {
//                            Toast.makeText(KOT_Management.this, "epson", Toast.LENGTH_SHORT).show();
                                setHT32 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 2"
                                setHT321 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x21, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                                setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x19, 0x21, 0x00};//4 tabs 2"
                                setHTKOT = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                                nPaperWidth = 384;
                                charlength = 28;
                                HT1 = new byte[]{0x09};
                                str_line = "------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------".getBytes(), LF
                                };
                            } else {
                                if (str_print_ty.toString().equals("POS")) {
                                    setHT32 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT321 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT3212 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 3"
                                    setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x12, 0x21, 0x00};//4 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x05, 0x08, 0x00};//4 tabs 2"
                                    setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x08, 0x09, 0x00};//4 tabs 2"
                                    setHTKOT = new byte[]{0x1b, 0x44, 0x05, 0x00};//2 tabs 3"
                                    feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                    nPaperWidth = 384;
                                    charlength = 11;
                                    charlength1 = 22;
                                    charlength2 = 33;
                                    quanlentha = 3;
                                    HT1 = new byte[]{0x2F};
                                    str_line = "--------------------------------";
                                    allbufline = new byte[][]{
                                            left, un1, "--------------------------------".getBytes(), LF
                                    };
                                }
                            }
                        }
                    }
                } while (cc.moveToNext());
            }
            cc.close();

        }
        getprint_type.close();

        String dd = "";
        TextView qazcvb = new TextView(KOT_Management.this);
        Cursor cvonnusb = db.rawQuery("SELECT * FROM BTConn", null);
        if (cvonnusb.moveToFirst()) {
            addgets = cvonnusb.getString(1);
            namegets = cvonnusb.getString(2);
            statussusbs = cvonnusb.getString(3);
            dd = cvonnusb.getString(4);
        }
        cvonnusb.close();
        qazcvb.setText(dd);
        if (qazcvb.getText().toString().equals("usb") && statussusbs.toString().equals("ok")) {
            runPrintCouponSequence();
        } else {

            Cursor connnet = db.rawQuery("SELECT * FROM IPConn", null);
            if (connnet.moveToFirst()) {
                ipnamegets = connnet.getString(1);
                portgets = connnet.getString(2);
                statusnets = connnet.getString(3);
            }
            connnet.close();

            Cursor connnet_counter = db.rawQuery("SELECT * FROM IPConn_Counter", null);
            if (connnet_counter.moveToFirst()) {
                ipnamegets_counter = connnet_counter.getString(1);
                portgets_counter = connnet_counter.getString(2);
                statusnets_counter = connnet_counter.getString(3);
            }
            connnet_counter.close();

            Cursor connusb = db.rawQuery("SELECT * FROM BTConn", null);
            if (connusb.moveToFirst()) {
                addgets = connusb.getString(1);
                namegets = connusb.getString(2);
                statussusbs = connusb.getString(3);
            }
            connusb.close();

            textViewstatusnets.setText(statusnet);
            textViewstatusnets_counter.setText(statusnets_counter);
            textViewstatussusbs.setText(statussusbs);

            Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
            if (getcom.moveToFirst()) {
                strcompanyname = getcom.getString(1);
            }
            getcom.close();

            tvkot.setText(strcompanyname);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf1 = new byte[][]{
                        bold, cen, strcompanyname.getBytes(), LF, LF

                };

                if (textViewstatussusbs.getText().toString().equals("ok")) {
                    BluetoothPrintDriver.BT_Write(bold);    //
                    BluetoothPrintDriver.BT_Write(cen);    //
                    BT_Write(strcompanyname);
                    BluetoothPrintDriver.BT_Write(LF);    //
                    BluetoothPrintDriver.BT_Write(LF);    //
                }else {
                    if (textViewstatusnets.getText().toString().equals("ok")) {
                        wifiSocket.WIFI_Write(bold);    //
                        wifiSocket.WIFI_Write(cen);    //
                        wifiSocket.WIFI_Write(strcompanyname);
                        wifiSocket.WIFI_Write(LF);    //
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }

            int one_11 = 0;
            Cursor cursor_6 = db1.rawQuery("Select MAX(tagg) from Table" + ItemIDtable, null);
            if (cursor_6.moveToFirst()) {
                one_11 = cursor_6.getInt(0);
            }
            cursor_6.close();

            int one111 = one_11+1;
            String one_1 = String.valueOf(one111);

            allbufKOT = new byte[][]{
                    un, cen, "Order Ticket copy".getBytes(), LF
            };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(normal);    //
                BluetoothPrintDriver.BT_Write(un);    //
                BluetoothPrintDriver.BT_Write(cen);    //
                BT_Write("Order Ticket copy "+one_1);
                BluetoothPrintDriver.BT_Write(LF);    //
            }else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(normal);    //
                    wifiSocket.WIFI_Write(un);    //
                    wifiSocket.WIFI_Write(cen);    //
                    wifiSocket.WIFI_Write("Order Ticket copy "+one_1);
                    wifiSocket.WIFI_Write(LF);    //
                }
            }

            Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
            if (vbnm.moveToFirst()) {
                assa1 = vbnm.getString(1);
                assa2 = vbnm.getString(2);
            }
            vbnm.close();
            TextView cx = new TextView(KOT_Management.this);
            cx.setText(assa1);
            String tablen = "";
            if (cx.getText().toString().equals("")) {
                tablen = "Tab" + assa2;
            } else {
                tablen = "Tab" + assa1;
            }

            allbuf11 = new byte[][]{
                    left, un1, setHT321, tablen.getBytes(), LF
            };

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BT_Write(tablen);
                BluetoothPrintDriver.BT_Write(LF);    //
            }else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(left);    //
                    wifiSocket.WIFI_Write(un1);    //
                    wifiSocket.WIFI_Write(tablen);
                    wifiSocket.WIFI_Write(LF);    //
                }
            }

            SimpleDateFormat normal2 = new SimpleDateFormat("dd MMM yyyy");
            final String normal1 = normal2.format(new Date());

            Date dt = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
            final String time1 = sdf1.format(dt);

            Date dtt_new = new Date();
            SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");
            String time24_new = sdf1t_new.format(dtt_new);

            Date dtt = new Date();
            SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
            String time24 = sdf1t.format(dtt);

            allbuf10 = new byte[][]{
                    setHT321, left, normal1.getBytes(), HT, "   ".getBytes(), time1.getBytes(), LF
            };

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHT321);    //
                BluetoothPrintDriver.BT_Write(left);    //
                BT_Write(normal1);
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write("   ");
                BT_Write(time1);
                BluetoothPrintDriver.BT_Write(LF);    //
            }else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(setHT321);    //
                    wifiSocket.WIFI_Write(left);    //
                    wifiSocket.WIFI_Write(normal1);
                    wifiSocket.WIFI_Write(HT);    //
                    wifiSocket.WIFI_Write("   ");
                    wifiSocket.WIFI_Write(time1);
                    wifiSocket.WIFI_Write(LF);    //
                }
            }


            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BT_Write(str_line);
                BluetoothPrintDriver.BT_Write(LF);    //
            }else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(left);    //
                    wifiSocket.WIFI_Write(un1);    //
                    wifiSocket.WIFI_Write(str_line);
                    wifiSocket.WIFI_Write(LF);    //
                }
            }

            allbufqty = new byte[][]{
                    setHTKOT, normal, "Qty".getBytes(), HT, "Item".getBytes(), left, LF
            };

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHTKOT);    //
                BluetoothPrintDriver.BT_Write(normal);    //
                BT_Write("Qty");
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write("Item");
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(LF);    //
            }else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(setHTKOT);    //
                    wifiSocket.WIFI_Write(normal);    //
                    wifiSocket.WIFI_Write("Qty");
                    wifiSocket.WIFI_Write(HT);    //
                    wifiSocket.WIFI_Write("Item");
                    wifiSocket.WIFI_Write(left);    //
                    wifiSocket.WIFI_Write(LF);    //
                }
            }

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BT_Write(str_line);
                BluetoothPrintDriver.BT_Write(LF);    //
            }else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(left);    //
                    wifiSocket.WIFI_Write(un1);    //
                    wifiSocket.WIFI_Write(str_line);
                    wifiSocket.WIFI_Write(LF);    //
                }
            }

            Cursor cursor = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE dept_name = '"+name_kot1+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (cursor.moveToFirst()) {
                do {
                    String Itemtype = cursor.getString(5);
                    String total = cursor.getString(20);
                    final String idid = cursor.getString(0);
                    String name = cursor.getString(2);
                    final String iidd = cursor.getString(0);
                    final String hii = cursor.getString(2);
                    final String newtt = cursor.getString(4);
                    final float f = Float.parseFloat(cursor.getString(3));
                    String price = String.valueOf(f);
                    String stat = cursor.getString(16);
                    String sev = cursor.getString(7);
                    String add_note_print = cursor.getString(30);

                    TextView tvbh = new TextView(KOT_Management.this);

                    tvbh.setText(stat);


                    TextView tv_add_note_print = new TextView(KOT_Management.this);
                    tv_add_note_print.setText(add_note_print);


                    if (Itemtype.toString().equals("Item")) {

                        Cursor modcursorc = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE parent = '" + hii + "' AND parentid = '" + idid + "'  ", null);
                        if (modcursorc.moveToFirst()) {
                            Cursor modt = db1.rawQuery("Select SUM(total) from Table" + ItemIDtable + " WHERE parent = '" + name + "' AND parentid = '" + idid + "' ", null);
                            if (modt.moveToFirst()) {
                                do {
                                    //row.removeView(tv3);
                                    float aq = modt.getFloat(0);
                                    aqq = String.valueOf(aq);
                                    aqq1 = Float.parseFloat(aqq) + Float.parseFloat(newtt);
                                    aqq2 = String.valueOf(aqq1);
                                } while (modt.moveToNext());
                            }
                            modt.close();

                            if (name.toString().length() > charlength) {
                                String str1 = name.substring(0, charlength);
                                String str2 = name.substring(charlength);
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                };

                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                    BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                    BluetoothPrintDriver.BT_Write(normal);    //
                                    BT_Write(total);
                                    BluetoothPrintDriver.BT_Write(HT);    //
                                    BT_Write(str1);
                                    BluetoothPrintDriver.BT_Write(LF);    //
                                    BT_Write("      ");
                                    BT_Write(str2);
                                    BluetoothPrintDriver.BT_Write(left);    //
                                    BluetoothPrintDriver.BT_Write(LF);    //
                                }else {
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(setHTKOT);    //
                                        wifiSocket.WIFI_Write(normal);    //
                                        wifiSocket.WIFI_Write(total);
                                        wifiSocket.WIFI_Write(HT);    //
                                        wifiSocket.WIFI_Write(str1);
                                        wifiSocket.WIFI_Write(LF);    //
                                        wifiSocket.WIFI_Write("      ");
                                        wifiSocket.WIFI_Write(str2);
                                        wifiSocket.WIFI_Write(left);    //
                                        wifiSocket.WIFI_Write(LF);    //
                                    }
                                }

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(normal);    //
                                        BT_Write(add_note_print);
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }else {
                                        if (textViewstatusnets.getText().toString().equals("ok")) {
                                            wifiSocket.WIFI_Write(normal);    //
                                            wifiSocket.WIFI_Write(add_note_print);
                                            wifiSocket.WIFI_Write(LF);    //
                                        }
                                    }
                                }
                            } else {
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                };
                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                    BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                    BluetoothPrintDriver.BT_Write(normal);    //
                                    BT_Write(total);
                                    BluetoothPrintDriver.BT_Write(HT);    //
                                    BT_Write(name);
                                    BluetoothPrintDriver.BT_Write(left);    //
                                    BluetoothPrintDriver.BT_Write(LF);    //
                                }else {
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(setHTKOT);    //
                                        wifiSocket.WIFI_Write(normal);    //
                                        wifiSocket.WIFI_Write(total);
                                        wifiSocket.WIFI_Write(HT);    //
                                        wifiSocket.WIFI_Write(name);
                                        wifiSocket.WIFI_Write(left);    //
                                        wifiSocket.WIFI_Write(LF);    //
                                    }
                                }

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(normal);    //
                                        BT_Write(add_note_print);
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }else {
                                        if (textViewstatusnets.getText().toString().equals("ok")) {
                                            wifiSocket.WIFI_Write(normal);    //
                                            wifiSocket.WIFI_Write(add_note_print);
                                            wifiSocket.WIFI_Write(LF);    //
                                        }
                                    }
                                }

                            }

                            do {
                                final String modiname = modcursorc.getString(2);
                                final String modiquan = modcursorc.getString(1);
                                String modiprice = modcursorc.getString(3);
                                String moditotal = modcursorc.getString(4);
                                final String modiid = modcursorc.getString(0);
                                String mod = modiname;


                                if (mod.toString().length() > charlength) {
                                    String str1 = mod.substring(0, charlength);
                                    String str2 = mod.substring(charlength);
                                    allbufmodifiers = new byte[][]{
                                            setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                    };
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                        BluetoothPrintDriver.BT_Write(normal);    //
                                        BT_Write("");
                                        BluetoothPrintDriver.BT_Write(HT);    //
                                        BT_Write(">");
                                        BT_Write(str1);
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                        BT_Write("      ");
                                        BT_Write(str2);
                                        BluetoothPrintDriver.BT_Write(left);    //
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }else {
                                        if (textViewstatusnets.getText().toString().equals("ok")) {
                                            wifiSocket.WIFI_Write(setHTKOT);    //
                                            wifiSocket.WIFI_Write(normal);    //
                                            wifiSocket.WIFI_Write("");
                                            wifiSocket.WIFI_Write(HT);    //
                                            wifiSocket.WIFI_Write(">");
                                            wifiSocket.WIFI_Write(str1);
                                            wifiSocket.WIFI_Write(LF);    //
                                            wifiSocket.WIFI_Write("      ");
                                            wifiSocket.WIFI_Write(str2);
                                            wifiSocket.WIFI_Write(left);    //
                                            wifiSocket.WIFI_Write(LF);    //
                                        }
                                    }

                                    if (tv_add_note_print.getText().toString().equals("")){

                                    }else {
                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write(add_note_print);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        }else {
                                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                                wifiSocket.WIFI_Write(normal);    //
                                                wifiSocket.WIFI_Write(add_note_print);
                                                wifiSocket.WIFI_Write(LF);    //
                                            }
                                        }
                                    }

                                } else {
                                    allbufmodifiers = new byte[][]{
                                            setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), mod.getBytes(), left, LF
                                    };
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                        BluetoothPrintDriver.BT_Write(normal);    //
                                        BT_Write("");
                                        BluetoothPrintDriver.BT_Write(HT);    //
                                        BT_Write(">");
                                        BT_Write(mod);
                                        BluetoothPrintDriver.BT_Write(left);    //
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }else {
                                        if (textViewstatusnets.getText().toString().equals("ok")) {
                                            wifiSocket.WIFI_Write(setHTKOT);    //
                                            wifiSocket.WIFI_Write(normal);    //
                                            wifiSocket.WIFI_Write("");
                                            wifiSocket.WIFI_Write(HT);    //
                                            wifiSocket.WIFI_Write(">");
                                            wifiSocket.WIFI_Write(mod);
                                            wifiSocket.WIFI_Write(left);    //
                                            wifiSocket.WIFI_Write(LF);    //
                                        }
                                    }

                                    if (tv_add_note_print.getText().toString().equals("")){

                                    }else {
                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write(add_note_print);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        }else {
                                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                                wifiSocket.WIFI_Write(normal);    //
                                                wifiSocket.WIFI_Write(add_note_print);
                                                wifiSocket.WIFI_Write(LF);    //
                                            }
                                        }
                                    }

                                }

                            } while (modcursorc.moveToNext());
                        } else {
                            if (name.toString().length() > charlength) {
                                String str1 = name.substring(0, charlength);
                                String str2 = name.substring(charlength);
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                };
                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                    BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                    BluetoothPrintDriver.BT_Write(normal);    //
                                    BT_Write(total);
                                    BluetoothPrintDriver.BT_Write(HT);    //
                                    BT_Write(str1);
                                    BluetoothPrintDriver.BT_Write(LF);    //
                                    BT_Write("      ");
                                    BT_Write(str2);
                                    BluetoothPrintDriver.BT_Write(left);    //
                                    BluetoothPrintDriver.BT_Write(LF);    //
                                }else {
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(setHTKOT);    //
                                        wifiSocket.WIFI_Write(normal);    //
                                        wifiSocket.WIFI_Write(total);
                                        wifiSocket.WIFI_Write(HT);    //
                                        wifiSocket.WIFI_Write(str1);
                                        wifiSocket.WIFI_Write(LF);    //
                                        wifiSocket.WIFI_Write("      ");
                                        wifiSocket.WIFI_Write(str2);
                                        wifiSocket.WIFI_Write(left);    //
                                        wifiSocket.WIFI_Write(LF);    //
                                    }
                                }

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(normal);    //
                                        BT_Write(add_note_print);
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }else {
                                        if (textViewstatusnets.getText().toString().equals("ok")) {
                                            wifiSocket.WIFI_Write(normal);    //
                                            wifiSocket.WIFI_Write(add_note_print);
                                            wifiSocket.WIFI_Write(LF);    //
                                        }
                                    }
                                }

                            } else {
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                };
                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                    BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                    BluetoothPrintDriver.BT_Write(normal);    //
                                    BT_Write(total);
                                    BluetoothPrintDriver.BT_Write(HT);    //
                                    BT_Write(name);
                                    BluetoothPrintDriver.BT_Write(left);    //
                                    BluetoothPrintDriver.BT_Write(LF);    //
                                }else {
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(setHTKOT);    //
                                        wifiSocket.WIFI_Write(normal);    //
                                        wifiSocket.WIFI_Write(total);
                                        wifiSocket.WIFI_Write(HT);    //
                                        wifiSocket.WIFI_Write(name);
                                        wifiSocket.WIFI_Write(left);    //
                                        wifiSocket.WIFI_Write(LF);    //
                                    }
                                }

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(normal);    //
                                        BT_Write(add_note_print);
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }else {
                                        if (textViewstatusnets.getText().toString().equals("ok")) {
                                            wifiSocket.WIFI_Write(normal);    //
                                            wifiSocket.WIFI_Write(add_note_print);
                                            wifiSocket.WIFI_Write(LF);    //
                                        }
                                    }
                                }

                            }

                        }
                        modcursorc.close();
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BT_Write(str_line);
                BluetoothPrintDriver.BT_Write(LF);    //
            }else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(left);    //
                    wifiSocket.WIFI_Write(un1);    //
                    wifiSocket.WIFI_Write(str_line);
                    wifiSocket.WIFI_Write(LF);    //
                }
            }
            //feedcut();

            Cursor toalitems = db1.rawQuery("Select * from Table" + ItemIDtable, null);
            if (toalitems.moveToFirst()) {
                Cursor toalitems2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity <= '1.0'", null);
                if (toalitems2.moveToFirst()) {
                    int toalzx = toalitems2.getCount();
                    totalquanret1 = String.valueOf(toalzx);
                }
                toalitems2.close();

                Cursor toalitems1 = db1.rawQuery("Select sum(quantity) from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity > '1.0'", null);
                if (toalitems1.moveToFirst()) {
                    int toalzx = toalitems1.getInt(0);
                    totalquanret2 = String.valueOf(toalzx);
                }
                toalitems1.close();

                int cvvc = Integer.parseInt(totalquanret1) + Integer.parseInt(totalquanret2);
                String total = String.valueOf(cvvc);
                String totalqu = "No. of items : " + total;
                allbuf11 = new byte[][]{
                        left, setHT321, totalqu.getBytes(), LF
                };
                if (textViewstatussusbs.getText().toString().equals("ok")) {
                    BluetoothPrintDriver.BT_Write(left);    //
                    BluetoothPrintDriver.BT_Write(setHT321);    //
                    BT_Write(totalqu);
                    BluetoothPrintDriver.BT_Write(LF);    //
                }else {
                    if (textViewstatusnets.getText().toString().equals("ok")) {
                        wifiSocket.WIFI_Write(left);    //
                        wifiSocket.WIFI_Write(setHT321);    //
                        wifiSocket.WIFI_Write(totalqu);
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }
            toalitems.close();

            byte[][] allbuf = new byte[][]{
                    feedcut2
            };
            System.out.println("feedcut executed now 3");
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(feedcut2);    //
            }else {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(feedcut2);    //
                }
            }

//            if (str_print_ty.toString().equals("POS")) {
//                if (textViewstatusnets.getText().toString().equals("ok")) {
//                    wifiSocket.WIFI_Write(feedcut2);    //
//                } else {
//                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
//                        wifiSocket2.WIFI_Write(feedcut2);    //
//                    } else {
//                        if (textViewstatussusbs.getText().toString().equals("ok")) {
//                            BluetoothPrintDriver.BT_Write(feedcut2);    //
//                        }
//                    }
//                }
//            }
        }
//        Toast.makeText(KOT_Management.this, "KOT printed", Toast.LENGTH_LONG).show();
    }

    public byte[] neoprintbillsplithome_kot_BT_NS() {



        Typeface tf = Typeface.SERIF;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ReceiptBitmap receiptBitmap = new ReceiptBitmap().getInstance();
        int cont=0;
        Cursor cursor34 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE dept_name = '"+name_kot1+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (cursor34.moveToFirst()) {
            cont=cursor34.getCount();

        }
        cursor34.close();
        receiptBitmap.init(200+(cont*50));
        receiptBitmap.setTextSize(25);
        receiptBitmap.setTypeface(Typeface.create(tf, Typeface.NORMAL));


        charlength = 10;
        charlength1 = 20;
        charlength2 = 30;
        quanlentha = 5;

        Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
        if (getcom.moveToFirst()) {
            strcompanyname = getcom.getString(1);
        }
        getcom.close();

        tvkot.setText(strcompanyname);
        if (tvkot.getText().toString().equals("")) {

        } else {
            receiptBitmap.drawCenterText(strcompanyname);

        }

        receiptBitmap.drawLeftText("Order Ticket copy");
        Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
        if (vbnm.moveToFirst()) {
            assa1 = vbnm.getString(1);
            assa2 = vbnm.getString(2);
        }
        vbnm.close();
        TextView cx = new TextView(KOT_Management.this);
        cx.setText(assa1);
        String tablen = "";
        if (cx.getText().toString().equals("")) {
            tablen = "Tab" + assa2;
        } else {
            tablen = "Tab" + assa1;
        }

        receiptBitmap.drawLeftText(tablen);


        SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyyyy");
        final String normal1 = normal2.format(new Date());

        Date dt = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
        final String time1 = sdf1.format(dt);

        Date dtt = new Date();
        SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
        String time24 = sdf1t.format(dtt);


        receiptBitmap.drawLeftText(normal1+"   "+time1);
        String str_line="----------------------";
        receiptBitmap.drawLeftText(str_line);
        receiptBitmap.drawLeftText("Qty"+"  "+"Item");
        receiptBitmap.drawLeftText(str_line);




        Cursor cursor = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE dept_name = '' OR dept_name IS null", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (cursor.moveToFirst()) {
            do {
                String Itemtype = cursor.getString(5);
                String total = cursor.getString(20);
                final String idid = cursor.getString(0);
                String name = cursor.getString(2);
                final String iidd = cursor.getString(0);
                final String hii = cursor.getString(2);
                final String newtt = cursor.getString(4);
                final float f = Float.parseFloat(cursor.getString(3));
                String price = String.valueOf(f);
                String stat = cursor.getString(16);
                String sev = cursor.getString(7);
                String add_note_print = cursor.getString(30);

                TextView tvbh = new TextView(KOT_Management.this);

                tvbh.setText(stat);


                TextView tv_add_note_print = new TextView(KOT_Management.this);
                tv_add_note_print.setText(add_note_print);


                if (Itemtype.toString().equals("Item")) {

                    Cursor modcursorc = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE parent = '" + hii + "' AND parentid = '" + idid + "'  ", null);
                    if (modcursorc.moveToFirst()) {
                        Cursor modt = db1.rawQuery("Select SUM(total) from Table" + ItemIDtable + " WHERE parent = '" + name + "' AND parentid = '" + idid + "' ", null);
                        if (modt.moveToFirst()) {
                            do {
                                //row.removeView(tv3);
                                float aq = modt.getFloat(0);
                                aqq = String.valueOf(aq);
                                aqq1 = Float.parseFloat(aqq) + Float.parseFloat(newtt);
                                aqq2 = String.valueOf(aqq1);
                            } while (modt.moveToNext());
                        }
                        modt.close();

                        if (name.toString().length() > charlength) {
                            String str1 = name.substring(0, charlength);
                            String str2 = name.substring(charlength);

                            receiptBitmap.drawLeftText(total+"  "+str1);
                            receiptBitmap.drawLeftText(str2);
                            receiptBitmap.drawLeftText(add_note_print);



                        } else {

                            receiptBitmap.drawLeftText(total);
                            receiptBitmap.drawLeftText(name);


                            if (tv_add_note_print.getText().toString().equals("")){

                            }else {

                                receiptBitmap.drawLeftText(add_note_print);

                            }

                        }

                        do {
                            final String modiname = modcursorc.getString(2);
                            final String modiquan = modcursorc.getString(1);
                            String modiprice = modcursorc.getString(3);
                            String moditotal = modcursorc.getString(4);
                            final String modiid = modcursorc.getString(0);
                            String mod = modiname;


                            if (mod.toString().length() > charlength) {
                                String str1 = mod.substring(0, charlength);
                                String str2 = mod.substring(charlength);


                                receiptBitmap.drawLeftText(""+"  "+">"+str1);
                                receiptBitmap.drawLeftText("      "+str2);


                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    receiptBitmap.drawLeftText(add_note_print);
                                }

                            } else {

                                receiptBitmap.drawLeftText(""+"  "+">"+mod);


                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {

                                    receiptBitmap.drawLeftText(add_note_print);

                                }

                            }

                        } while (modcursorc.moveToNext());
                    } else {
                        if (name.toString().length() > charlength) {
                            String str1 = name.substring(0, charlength);
                            String str2 = name.substring(charlength);


                            receiptBitmap.drawLeftText(total+"  "+str1);
                            receiptBitmap.drawLeftText(str2);


                            if (tv_add_note_print.getText().toString().equals("")){

                            }else {
                                receiptBitmap.drawLeftText(add_note_print);
                            }

                        } else {

                            receiptBitmap.drawLeftText(total+"  "+name);

                            if (tv_add_note_print.getText().toString().equals("")){

                            }else {
                                receiptBitmap.drawLeftText(add_note_print);

                            }

                        }

                    }
                    modcursorc.close();
                }

            } while (cursor.moveToNext());
        }
        cursor.close();

        receiptBitmap.drawLeftText(str_line);

        Cursor toalitems = db1.rawQuery("Select * from Table" + ItemIDtable, null);
        if (toalitems.moveToFirst()) {
            Cursor toalitems2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity <= '1.0'", null);
            if (toalitems2.moveToFirst()) {
                int toalzx = toalitems2.getCount();
                totalquanret1 = String.valueOf(toalzx);
            }
            toalitems2.close();

            Cursor toalitems1 = db1.rawQuery("Select sum(quantity) from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity > '1.0'", null);
            if (toalitems1.moveToFirst()) {
                int toalzx = toalitems1.getInt(0);
                totalquanret2 = String.valueOf(toalzx);
            }
            toalitems1.close();

            int cvvc = Integer.parseInt(totalquanret1) + Integer.parseInt(totalquanret2);
            String total = String.valueOf(cvvc);
            String totalqu = "No. of items : " + total;

            receiptBitmap.drawLeftText(totalqu);

        }
        toalitems.close();

        receiptBitmap.drawLeftText("       ");
        receiptBitmap.drawLeftText("       ");
        receiptBitmap.drawLeftText("       ");


        Bitmap canvasbitmap = receiptBitmap.getReceiptBitmap();

        if(ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"getHeight: " + canvasbitmap.getHeight(),true,true);

        if(ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"getReceiptHeight: " + receiptBitmap.getReceiptHeight(),true,true);

        Bitmap croppedBmp = Bitmap.createBitmap(canvasbitmap, 0, 0, canvasbitmap.getWidth(), canvasbitmap.getHeight());

        byte[] imageCommand = mMSWisepadDeviceController.getPrintData(croppedBmp, 150);

        baos.write(imageCommand, 0, imageCommand.length);

        if(ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"end of reciept",true,true);

        return baos.toByteArray();


    }


    public  byte[] neoprintbillsplithome_kot_BT(String name_kot1) {


        Typeface tf = Typeface.SERIF;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ReceiptBitmap receiptBitmap = new ReceiptBitmap().getInstance();
        int cont=0;
        Cursor cursor34 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE dept_name = '"+name_kot1+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (cursor34.moveToFirst()) {
            cont=cursor34.getCount();

        }
        cursor34.close();
        receiptBitmap.init(200+(cont*50));
        receiptBitmap.setTextSize(25);
        receiptBitmap.setTypeface(Typeface.create(tf, Typeface.NORMAL));


        charlength = 10;
        charlength1 = 20;
        charlength2 = 30;
        quanlentha = 5;

        Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
        if (getcom.moveToFirst()) {
            strcompanyname = getcom.getString(1);
        }
        getcom.close();

        tvkot.setText(strcompanyname);
        if (tvkot.getText().toString().equals("")) {

        } else {
            // Print.StartPrinting(strcompanyname ,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
            receiptBitmap.drawCenterText(strcompanyname);
        }

        receiptBitmap.drawCenterText("Order Ticket copy");



        Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
        if (vbnm.moveToFirst()) {
            assa1 = vbnm.getString(1);
            assa2 = vbnm.getString(2);
        }
        vbnm.close();
        TextView cx = new TextView(KOT_Management.this);
        cx.setText(assa1);
        String tablen = "";
        if (cx.getText().toString().equals("")) {
            tablen = "Tab" + assa2;
        } else {
            tablen = "Tab" + assa1;
        }


        //   Print.StartPrinting(NAme111+"   "+tablen,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        receiptBitmap.drawLeftText(tablen);


        SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyyyy");
        final String normal1 = normal2.format(new Date());

        Date dt = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
        final String time1 = sdf1.format(dt);

        Date dtt = new Date();
        SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
        String time24 = sdf1t.format(dtt);

        receiptBitmap.drawLeftText(normal1+"   "+time1);
        String str_line="----------------------";
        receiptBitmap.drawLeftText(str_line);
        receiptBitmap.drawLeftText("Qty"+"  "+"Item");
        receiptBitmap.drawLeftText(str_line);





        Cursor cursor = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE dept_name = '"+name_kot1+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (cursor.moveToFirst()) {
            do {
                String Itemtype = cursor.getString(5);
                String total = cursor.getString(20);
                final String idid = cursor.getString(0);
                String name = cursor.getString(2);
                final String iidd = cursor.getString(0);
                final String hii = cursor.getString(2);
                final String newtt = cursor.getString(4);
                final float f = Float.parseFloat(cursor.getString(3));
                String price = String.valueOf(f);
                String stat = cursor.getString(16);
                String sev = cursor.getString(7);
                String add_note_print = cursor.getString(30);

                TextView tvbh = new TextView(KOT_Management.this);

                tvbh.setText(stat);


                TextView tv_add_note_print = new TextView(KOT_Management.this);
                tv_add_note_print.setText(add_note_print);


                if (Itemtype.toString().equals("Item")) {

                    Cursor modcursorc = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE parent = '" + hii + "' AND parentid = '" + idid + "'  ", null);
                    if (modcursorc.moveToFirst()) {
                        Cursor modt = db1.rawQuery("Select SUM(total) from Table" + ItemIDtable + " WHERE parent = '" + name + "' AND parentid = '" + idid + "' ", null);
                        if (modt.moveToFirst()) {
                            do {
                                //row.removeView(tv3);
                                float aq = modt.getFloat(0);
                                aqq = String.valueOf(aq);
                                aqq1 = Float.parseFloat(aqq) + Float.parseFloat(newtt);
                                aqq2 = String.valueOf(aqq1);
                            } while (modt.moveToNext());
                        }
                        modt.close();

                        if (name.toString().length() > charlength) {
                            String str1 = name.substring(0, charlength);
                            String str2 = name.substring(charlength);

                            receiptBitmap.drawLeftText(total+"  "+str1);
                            receiptBitmap.drawLeftText(str2);
                            receiptBitmap.drawLeftText(add_note_print);

                        } else {

                            receiptBitmap.drawLeftText(total);
                            receiptBitmap.drawLeftText(name);


                            if (tv_add_note_print.getText().toString().equals("")){

                            }else {
                                receiptBitmap.drawLeftText(add_note_print);

                            }

                        }

                        do {
                            final String modiname = modcursorc.getString(2);
                            final String modiquan = modcursorc.getString(1);
                            String modiprice = modcursorc.getString(3);
                            String moditotal = modcursorc.getString(4);
                            final String modiid = modcursorc.getString(0);
                            String mod = modiname;


                            if (mod.toString().length() > charlength) {
                                String str1 = mod.substring(0, charlength);
                                String str2 = mod.substring(charlength);

                                receiptBitmap.drawLeftText(""+"  "+">"+str1);
                                receiptBitmap.drawLeftText("      "+str2);


                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    receiptBitmap.drawLeftText(add_note_print);

                                }

                            } else {


                                receiptBitmap.drawLeftText(""+"  "+">"+mod);

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {

                                    receiptBitmap.drawLeftText(add_note_print);
                                }

                            }

                        } while (modcursorc.moveToNext());
                    } else {
                        if (name.toString().length() > charlength) {
                            String str1 = name.substring(0, charlength);
                            String str2 = name.substring(charlength);


                            receiptBitmap.drawLeftText(total+"  "+str1);
                            receiptBitmap.drawLeftText(str2);



                            if (tv_add_note_print.getText().toString().equals("")){

                            }else {
                                receiptBitmap.drawLeftText(add_note_print);
                            }

                        } else {

                            receiptBitmap.drawLeftText(total+"  "+name);

                            if (tv_add_note_print.getText().toString().equals("")){

                            }else {

                                receiptBitmap.drawLeftText(add_note_print);

                            }

                        }

                    }
                    modcursorc.close();
                }

            } while (cursor.moveToNext());
        }
        cursor.close();

        receiptBitmap.drawLeftText(str_line);

        Cursor toalitems = db1.rawQuery("Select * from Table" + ItemIDtable, null);
        if (toalitems.moveToFirst()) {
            Cursor toalitems2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity <= '1.0'", null);
            if (toalitems2.moveToFirst()) {
                int toalzx = toalitems2.getCount();
                totalquanret1 = String.valueOf(toalzx);
            }
            toalitems2.close();

            Cursor toalitems1 = db1.rawQuery("Select sum(quantity) from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity > '1.0'", null);
            if (toalitems1.moveToFirst()) {
                int toalzx = toalitems1.getInt(0);
                totalquanret2 = String.valueOf(toalzx);
            }
            toalitems1.close();

            int cvvc = Integer.parseInt(totalquanret1) + Integer.parseInt(totalquanret2);
            String total = String.valueOf(cvvc);
            String totalqu = "No. of items : " + total;

            receiptBitmap.drawLeftText(totalqu);


        }
        toalitems.close();


        receiptBitmap.drawLeftText("       ");
        receiptBitmap.drawLeftText("       ");
        receiptBitmap.drawLeftText("       ");

        Bitmap canvasbitmap = receiptBitmap.getReceiptBitmap();

        if(ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"getHeight: " + canvasbitmap.getHeight(),true,true);

        if(ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"getReceiptHeight: " + receiptBitmap.getReceiptHeight(),true,true);

        Bitmap croppedBmp = Bitmap.createBitmap(canvasbitmap, 0, 0, canvasbitmap.getWidth(), canvasbitmap.getHeight());

        byte[] imageCommand = mMSWisepadDeviceController.getPrintData(croppedBmp, 150);

        baos.write(imageCommand, 0, imageCommand.length);

        if(ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"end of reciept",true,true);

        return baos.toByteArray();

    }


    public void wiseposprintbillsplithome_kot_BT(String name_kot1) {

        charlength = 10;
        charlength1 = 20;
        charlength2 = 30;
        quanlentha = 5;

        Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
        if (getcom.moveToFirst()) {
            strcompanyname = getcom.getString(1);
        }
        getcom.close();

        tvkot.setText(strcompanyname);
        if (tvkot.getText().toString().equals("")) {

        } else {
            Print.StartPrinting(strcompanyname ,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        }
        Print.StartPrinting("Order Ticket copy" ,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);

        Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
        if (vbnm.moveToFirst()) {
            assa1 = vbnm.getString(1);
            assa2 = vbnm.getString(2);
        }
        vbnm.close();
        TextView cx = new TextView(KOT_Management.this);
        cx.setText(assa1);
        String tablen = "";
        if (cx.getText().toString().equals("")) {
            tablen = "Tab" + assa2;
        } else {
            tablen = "Tab" + assa1;
        }


        Print.StartPrinting(tablen,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);



        SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyyyy");
        final String normal1 = normal2.format(new Date());

        Date dt = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
        final String time1 = sdf1.format(dt);

        Date dtt = new Date();
        SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
        String time24 = sdf1t.format(dtt);


        Print.StartPrinting(normal1+"   "+time1,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);

        String str_line="----------------------";
        Print.StartPrinting(str_line,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);

        Print.StartPrinting("Qty"+"  "+"Item",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
        Print.StartPrinting(str_line,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);




        Cursor cursor = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE dept_name = '"+name_kot1+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (cursor.moveToFirst()) {
            do {
                String Itemtype = cursor.getString(5);
                String total = cursor.getString(20);
                final String idid = cursor.getString(0);
                String name = cursor.getString(2);
                final String iidd = cursor.getString(0);
                final String hii = cursor.getString(2);
                final String newtt = cursor.getString(4);
                final float f = Float.parseFloat(cursor.getString(3));
                String price = String.valueOf(f);
                String stat = cursor.getString(16);
                String sev = cursor.getString(7);
                String add_note_print = cursor.getString(30);

                TextView tvbh = new TextView(KOT_Management.this);

                tvbh.setText(stat);


                TextView tv_add_note_print = new TextView(KOT_Management.this);
                tv_add_note_print.setText(add_note_print);


                if (Itemtype.toString().equals("Item")) {

                    Cursor modcursorc = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE parent = '" + hii + "' AND parentid = '" + idid + "'  ", null);
                    if (modcursorc.moveToFirst()) {
                        Cursor modt = db1.rawQuery("Select SUM(total) from Table" + ItemIDtable + " WHERE parent = '" + name + "' AND parentid = '" + idid + "' ", null);
                        if (modt.moveToFirst()) {
                            do {
                                //row.removeView(tv3);
                                float aq = modt.getFloat(0);
                                aqq = String.valueOf(aq);
                                aqq1 = Float.parseFloat(aqq) + Float.parseFloat(newtt);
                                aqq2 = String.valueOf(aqq1);
                            } while (modt.moveToNext());
                        }
                        modt.close();

                        if (name.toString().length() > charlength) {
                            String str1 = name.substring(0, charlength);
                            String str2 = name.substring(charlength);

                            Print.StartPrinting(total+"  "+str1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                            Print.StartPrinting(str2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                            Print.StartPrinting(add_note_print,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);


                        } else {

                            Print.StartPrinting(total,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                            Print.StartPrinting(name,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                            if (tv_add_note_print.getText().toString().equals("")){

                            }else {
                                Print.StartPrinting(add_note_print,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                            }

                        }

                        do {
                            final String modiname = modcursorc.getString(2);
                            final String modiquan = modcursorc.getString(1);
                            String modiprice = modcursorc.getString(3);
                            String moditotal = modcursorc.getString(4);
                            final String modiid = modcursorc.getString(0);
                            String mod = modiname;


                            if (mod.toString().length() > charlength) {
                                String str1 = mod.substring(0, charlength);
                                String str2 = mod.substring(charlength);


                                Print.StartPrinting(""+"  "+">"+str1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                Print.StartPrinting("      "+str2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    Print.StartPrinting(add_note_print,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                }

                            } else {



                                Print.StartPrinting(""+"  "+">"+mod,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    Print.StartPrinting(add_note_print,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                }

                            }

                        } while (modcursorc.moveToNext());
                    } else {
                        if (name.toString().length() > charlength) {
                            String str1 = name.substring(0, charlength);
                            String str2 = name.substring(charlength);


                            Print.StartPrinting(total+"  "+str1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                            Print.StartPrinting(str2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);


                            if (tv_add_note_print.getText().toString().equals("")){

                            }else {
                                Print.StartPrinting(add_note_print,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                            }

                        } else {


                            Print.StartPrinting(total+"  "+name,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                            if (tv_add_note_print.getText().toString().equals("")){

                            }else {
                                Print.StartPrinting(add_note_print,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                            }

                        }

                    }
                    modcursorc.close();
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        Print.StartPrinting(str_line,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);


        Cursor toalitems = db1.rawQuery("Select * from Table" + ItemIDtable, null);
        if (toalitems.moveToFirst()) {
            Cursor toalitems2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity <= '1.0'", null);
            if (toalitems2.moveToFirst()) {
                int toalzx = toalitems2.getCount();
                totalquanret1 = String.valueOf(toalzx);
            }
            toalitems2.close();

            Cursor toalitems1 = db1.rawQuery("Select sum(quantity) from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity > '1.0'", null);
            if (toalitems1.moveToFirst()) {
                int toalzx = toalitems1.getInt(0);
                totalquanret2 = String.valueOf(toalzx);
            }
            toalitems1.close();

            int cvvc = Integer.parseInt(totalquanret1) + Integer.parseInt(totalquanret2);
            String total = String.valueOf(cvvc);
            String totalqu = "No. of items : " + total;


            Print.StartPrinting(totalqu,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

        }
        toalitems.close();


        Print.StartPrinting("       ",FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        Print.StartPrinting("       ",FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        Print.StartPrinting("       ",FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);

//            if (str_print_ty.toString().equals("POS")) {
//                if (textViewstatusnets.getText().toString().equals("ok")) {
//                    wifiSocket.WIFI_Write(feedcut2);    //
//                } else {
//                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
//                        wifiSocket2.WIFI_Write(feedcut2);    //
//                    } else {
//                        if (textViewstatussusbs.getText().toString().equals("ok")) {
//                            BluetoothPrintDriver.BT_Write(feedcut2);    //
//                        }
//                    }
//                }
//            }

//        Toast.makeText(KOT_Management.this, "KOT printed", Toast.LENGTH_LONG).show();
    }

    public void printbillsplithome(){
        byte[] setHT34M = {0x1b,0x44,0x04,0x11,0x19,0x00};
        byte[] dotfeed = {0x1b,0x4a,0x10};
        byte[] HTRight = {0x1b,0x61, 0x02,0x09};
        byte[] HT = {0x09};
        byte[] HT1 = {0x09};
        byte[] LF = {0x0d,0x0a};

        byte[] left = {0x1b,0x61, 0x00};
        byte[] cen = {0x1b,0x61, 0x01};
        byte[] right = {0x1b,0x61, 0x02};
        byte[] bold = {0x1B,0x21,0x10};
        byte[] normal = {0x1d, 0x21, 0x00};
        byte[] horiz1 = {0x1b,0x44,0x19, 0x19, 0x00};
        byte[] horiz = {0x1b,0x44, 0x04, 0x06, 0x04, 0x04, 0x0A, 0x00, 0x09, 0x30, 0x09, 0x31, 0x09, 0x32, 0x09, 0x33, 0x09, 0x34, 0x0d,0x0a};

        byte[] un = {0x1b, 0x2d, 0x02};
        byte[] un1 = {0x1b, 0x2d, 0x00};
        String str_line = "";

        Cursor print_ty = db.rawQuery("SELECT * FROM Printer_type", null);
        if (print_ty.moveToFirst()){
            str_print_ty = print_ty.getString(1);
        }
        print_ty.close();

        Cursor getprint_type = db.rawQuery("SELECT * FROM Printer_text_size", null);
        if (getprint_type.moveToFirst()) {
            String type = getprint_type.getString(1);

            Cursor cc = db.rawQuery("SELECT * FROM Printerreceiptsize", null);

            if (cc.moveToFirst()) {
                cc.moveToFirst();
                do {
                    NAME = cc.getString(1);
                    if (NAME.equals("3 inch")) {
                        if (str_print_ty.toString().equals("Generic") || str_print_ty.toString().equals("Epson/others")) {
                            setHT32 = new byte[]{0x1b, 0x44, 0x29, 0x00};//2 tabs 3"
                            setHT321 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x25, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                            setHT34 = new byte[]{0x1b, 0x44, 0x08, 0x20, 0x29, 0x00};//4 tabs 3"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 3"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                            nPaperWidth = 576;
                            charlength = 41;
                            HT1 = new byte[]{0x09};
                            str_line = "------------------------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "------------------------------------------------".getBytes(), LF

                            };
                        }else {
                            if (str_print_ty.toString().equals("POS")) {
                                setHT32 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT321 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x10, 0x15, 0x00};//4 tabs 3"
                                setHTKOT = new byte[]{0x1b, 0x44, 0x05, 0x00};//2 tabs 3"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                nPaperWidth = 576;
                                charlength = 23;
                                charlength1 = 46;
                                charlength2 = 69;
                                quanlentha = 4;
                                HT1 = new byte[]{0x2F};
                                str_line = "------------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------------".getBytes(), LF

                                };
                            }
                        }
                    } else {
                        if (str_print_ty.toString().equals("Generic")) {
//                            Toast.makeText(KOT_Management.this, "phi", Toast.LENGTH_SHORT).show();
                            setHT32 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 2"
                            setHT321 = new byte[]{0x1b, 0x44, 0x13, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x15, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                            setHT34 = new byte[]{0x1b, 0x44, 0x04, 0x12, 0x19, 0x00};//4 tabs 2"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 2"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                            nPaperWidth = 384;
                            charlength = 25;
                            HT1 = new byte[]{0x09};
                            str_line = "--------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "--------------------------------".getBytes(), LF
                            };
                        }else {
                            if (str_print_ty.toString().equals("Epson/others")) {
//                            Toast.makeText(KOT_Management.this, "epson", Toast.LENGTH_SHORT).show();
                                setHT32 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 2"
                                setHT321 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x21, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                                setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x19, 0x21, 0x00};//4 tabs 2"
                                setHTKOT = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                                nPaperWidth = 384;
                                charlength = 28;
                                HT1 = new byte[]{0x09};
                                str_line = "------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------".getBytes(), LF
                                };
                            }else {
                                if (str_print_ty.toString().equals("POS")) {
                                    setHT32 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT321 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT3212 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 3"
                                    setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x12, 0x21, 0x00};//4 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x05, 0x08, 0x00};//4 tabs 2"
                                    setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x08, 0x09, 0x00};//4 tabs 2"
                                    setHTKOT = new byte[]{0x1b, 0x44, 0x05, 0x00};//2 tabs 3"
                                    feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                    nPaperWidth = 384;
                                    charlength = 11;
                                    charlength1 = 22;
                                    charlength2 = 33;
                                    quanlentha = 3;
                                    HT1 = new byte[]{0x2F};
                                    str_line = "--------------------------------";
                                    allbufline = new byte[][]{
                                            left, un1, "--------------------------------".getBytes(), LF
                                    };
                                }
                            }
                        }
                    }
                } while (cc.moveToNext());
            }
            cc.close();

        }
        getprint_type.close();

        String dd = "";
        TextView qazcvb = new TextView(KOT_Management.this);
        Cursor cvonnusb = db.rawQuery("SELECT * FROM BTConn", null);
        if (cvonnusb.moveToFirst()) {
            addgets = cvonnusb.getString(1);
            namegets = cvonnusb.getString(2);
            statussusbs = cvonnusb.getString(3);
            dd = cvonnusb.getString(4);
        }
        cvonnusb.close();
        qazcvb.setText(dd);
        if (qazcvb.getText().toString().equals("usb") && statussusbs.toString().equals("ok")) {
            runPrintCouponSequence();
        }else {
            Cursor connnet = db.rawQuery("SELECT * FROM IPConn", null);
            if (connnet.moveToFirst()) {
                ipnamegets = connnet.getString(1);
                portgets = connnet.getString(2);
                statusnets = connnet.getString(3);
            }

            Cursor connnet_counter = db.rawQuery("SELECT * FROM IPConn_Counter", null);
            if (connnet_counter.moveToFirst()) {
                ipnamegets_counter = connnet_counter.getString(1);
                portgets_counter = connnet_counter.getString(2);
                statusnets_counter = connnet_counter.getString(3);
            }
            connnet_counter.close();

            Cursor connusb = db.rawQuery("SELECT * FROM BTConn", null);
            if (connusb.moveToFirst()) {
                addgets = connusb.getString(1);
                namegets = connusb.getString(2);
                statussusbs = connusb.getString(3);
            }

            textViewstatusnets.setText(statusnet);
            textViewstatusnets_counter.setText(statusnets_counter);
            textViewstatussusbs.setText(statussusbs);

            Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
            if (getcom.moveToFirst()) {
                do {
                    strcompanyname = getcom.getString(1);
                } while (getcom.moveToNext());
            }


            tvkot.setText(strcompanyname);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf1 = new byte[][]{
                        bold, cen, strcompanyname.getBytes(), LF, LF

                };

                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(bold);    //
                    wifiSocket.WIFI_Write(cen);    //
                    wifiSocket.WIFI_Write(strcompanyname);
                    wifiSocket.WIFI_Write(LF);    //
                    wifiSocket.WIFI_Write(LF);    //
                } else {
                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                        wifiSocket2.WIFI_Write(bold);    //
                        wifiSocket2.WIFI_Write(cen);    //
                        wifiSocket2.WIFI_Write(strcompanyname);
                        wifiSocket2.WIFI_Write(LF);    //
                        wifiSocket2.WIFI_Write(LF);    //
                    } else {
                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                            BluetoothPrintDriver.BT_Write(bold);    //
                            BluetoothPrintDriver.BT_Write(cen);    //
                            BT_Write(strcompanyname);
                            BluetoothPrintDriver.BT_Write(LF);    //
                            BluetoothPrintDriver.BT_Write(LF);    //
                        }
                    }
                }
            }


            allbufKOT = new byte[][]{
                    un, cen, "Order Ticket copy".getBytes(), LF
            };
            if (textViewstatusnets.getText().toString().equals("ok")) {
                wifiSocket.WIFI_Write(un);    //
                wifiSocket.WIFI_Write(cen);    //
                wifiSocket.WIFI_Write("Order Ticket copy");
                wifiSocket.WIFI_Write(LF);    //
            } else {
                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(un);    //
                    wifiSocket2.WIFI_Write(cen);    //
                    wifiSocket2.WIFI_Write("Order Ticket copy");
                    wifiSocket2.WIFI_Write(LF);    //
                } else {
                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                        BluetoothPrintDriver.BT_Write(un);    //
                        BluetoothPrintDriver.BT_Write(cen);    //
                        BT_Write("Order Ticket copy");
                        BluetoothPrintDriver.BT_Write(LF);    //
                    }
                }
            }


            Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
            if (vbnm.moveToFirst()) {
                assa1 = vbnm.getString(1);
                assa2 = vbnm.getString(2);
            }
            TextView cx = new TextView(KOT_Management.this);
            cx.setText(assa1);
            String tablen = "";
            if (cx.getText().toString().equals("")) {
                tablen = "Tab" + assa2;
                allbuf11 = new byte[][]{
                        left, un1, setHT321, tablen.getBytes(), LF
                };
            } else {
                tablen = "Tab" + assa1;
                allbuf11 = new byte[][]{
                        left, un1, setHT321, tablen.getBytes(), LF
                };
            }

//        String tablen = "Table"+ItemIDtable;
//        allbuf11 = new byte[][]{
//                left,un1,setHT321, tablen.getBytes(),LF
//        };
            if (textViewstatusnets.getText().toString().equals("ok")) {
                wifiSocket.WIFI_Write(left);    //
                wifiSocket.WIFI_Write(un1);    //
                wifiSocket.WIFI_Write(setHT321);    //
                wifiSocket.WIFI_Write(tablen);
                wifiSocket.WIFI_Write(LF);    //
            } else {
                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(left);    //
                    wifiSocket2.WIFI_Write(un1);    //
                    wifiSocket2.WIFI_Write(setHT321);    //
                    wifiSocket2.WIFI_Write(tablen);
                    wifiSocket2.WIFI_Write(LF);    //
                } else {
                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                        BluetoothPrintDriver.BT_Write(left);    //
                        BluetoothPrintDriver.BT_Write(un1);    //
                        BluetoothPrintDriver.BT_Write(setHT321);    //
                        BT_Write(tablen);
                        BluetoothPrintDriver.BT_Write(LF);    //
                    }
                }
            }

            SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyyyy");
            final String normal1 = normal2.format(new Date());

            Date dt = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
            final String time1 = sdf1.format(dt);

            Date dtt = new Date();
            SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
            String time24 = sdf1t.format(dtt);

            allbuf10 = new byte[][]{
                    setHT321, left, normal1.getBytes(), HT, "   ".getBytes(), time1.getBytes(), LF
            };
            if (textViewstatusnets.getText().toString().equals("ok")) {
                wifiSocket.WIFI_Write(setHT321);    //
                wifiSocket.WIFI_Write(left);    //
                wifiSocket.WIFI_Write(normal1);
                wifiSocket.WIFI_Write(HT);    //
                wifiSocket.WIFI_Write("   ");
                wifiSocket.WIFI_Write(time1);
                wifiSocket.WIFI_Write(LF);    //
            } else {
                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(setHT321);    //
                    wifiSocket2.WIFI_Write(left);    //
                    wifiSocket2.WIFI_Write(normal1);
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write("   ");
                    wifiSocket2.WIFI_Write(time1);
                    wifiSocket2.WIFI_Write(LF);    //
                } else {
                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                        BluetoothPrintDriver.BT_Write(setHT321);    //
                        BluetoothPrintDriver.BT_Write(left);    //
                        BT_Write(normal1);
                        BluetoothPrintDriver.BT_Write(HT);    //
                        BT_Write("   ");
                        BT_Write(time1);
                        BluetoothPrintDriver.BT_Write(LF);    //
                    }
                }
            }

            if (textViewstatusnets.getText().toString().equals("ok")) {
                wifiSocket.WIFI_Write(left);    //
                wifiSocket.WIFI_Write(un1);    //
                wifiSocket.WIFI_Write(str_line);
                wifiSocket.WIFI_Write(LF);    //
            } else {
                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(left);    //
                    wifiSocket2.WIFI_Write(un1);    //
                    wifiSocket2.WIFI_Write(str_line);
                    wifiSocket2.WIFI_Write(LF);    //
                } else {
                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                        BluetoothPrintDriver.BT_Write(left);    //
                        BluetoothPrintDriver.BT_Write(un1);    //
                        BT_Write(str_line);
                        BluetoothPrintDriver.BT_Write(LF);    //
                    }
                }
            }

            allbufqty = new byte[][]{
                    setHTKOT, normal, "Qty".getBytes(), HT, "Item".getBytes(), left, LF
            };
            if (textViewstatusnets.getText().toString().equals("ok")) {
                wifiSocket.WIFI_Write(setHTKOT);    //
                wifiSocket.WIFI_Write(normal);    //
                wifiSocket.WIFI_Write("Qty");
                wifiSocket.WIFI_Write(HT);    //
                wifiSocket.WIFI_Write("Item");
                wifiSocket.WIFI_Write(left);    //
                wifiSocket.WIFI_Write(LF);    //
            } else {
                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(setHTKOT);    //
                    wifiSocket2.WIFI_Write(normal);    //
                    wifiSocket2.WIFI_Write("Qty");
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write("Item");
                    wifiSocket2.WIFI_Write(left);    //
                    wifiSocket2.WIFI_Write(LF);    //
                } else {
                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                        BluetoothPrintDriver.BT_Write(setHTKOT);    //
                        BluetoothPrintDriver.BT_Write(normal);    //
                        BT_Write("Qty");
                        BluetoothPrintDriver.BT_Write(HT);    //
                        BT_Write("Item");
                        BluetoothPrintDriver.BT_Write(left);    //
                        BluetoothPrintDriver.BT_Write(LF);    //
                    }
                }
            }

            if (textViewstatusnets.getText().toString().equals("ok")) {
                wifiSocket.WIFI_Write(left);    //
                wifiSocket.WIFI_Write(un1);    //
                wifiSocket.WIFI_Write(str_line);
                wifiSocket.WIFI_Write(LF);    //
            } else {
                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(left);    //
                    wifiSocket2.WIFI_Write(un1);    //
                    wifiSocket2.WIFI_Write(str_line);
                    wifiSocket2.WIFI_Write(LF);    //
                } else {
                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                        BluetoothPrintDriver.BT_Write(left);    //
                        BluetoothPrintDriver.BT_Write(un1);    //
                        BT_Write(str_line);
                        BluetoothPrintDriver.BT_Write(LF);    //
                    }
                }
            }


            Cursor cursor = db1.rawQuery("Select * from Table" + ItemIDtable, null);//replace to cursor = dbHelper.fetchAllHotels();
            if (cursor.moveToFirst()) {
                do {
                    String Itemtype = cursor.getString(5);
                    String total = cursor.getString(1);
                    final String idid = cursor.getString(0);
                    String name = cursor.getString(2);
                    final String iidd = cursor.getString(0);
                    final String hii = cursor.getString(2);
                    final String newtt = cursor.getString(4);
                    final float f = Float.parseFloat(cursor.getString(3));
                    String price = String.valueOf(f);
                    if (Itemtype.toString().equals("Item")) {

                        Cursor modcursorc = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE parent = '" + hii + "' AND parentid = '" + idid + "'  ", null);
                        if (modcursorc.moveToFirst()) {
                            Cursor modt = db1.rawQuery("Select SUM(total) from Table" + ItemIDtable + " WHERE parent = '" + name + "' AND parentid = '" + idid + "' ", null);
                            if (modt.moveToFirst()) {
                                do {
                                    //row.removeView(tv3);
                                    float aq = modt.getFloat(0);
                                    aqq = String.valueOf(aq);
                                    aqq1 = Float.parseFloat(aqq) + Float.parseFloat(newtt);
                                    aqq2 = String.valueOf(aqq1);
                                } while (modt.moveToNext());
                            }

                            if (name.toString().length() > charlength) {
                                String str1 = name.substring(0, charlength);
                                String str2 = name.substring(charlength);
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "         ".getBytes(), str2.getBytes(), left, LF,
                                };
                                if (textViewstatusnets.getText().toString().equals("ok")) {
                                    wifiSocket.WIFI_Write(setHTKOT);    //
                                    wifiSocket.WIFI_Write(normal);    //
                                    wifiSocket.WIFI_Write(total);
                                    wifiSocket.WIFI_Write(HT);    //
                                    wifiSocket.WIFI_Write(str1);
                                    wifiSocket.WIFI_Write(LF);    //
                                    wifiSocket.WIFI_Write("         ");
                                    wifiSocket.WIFI_Write(str2);
                                    wifiSocket.WIFI_Write(left);    //
                                    wifiSocket.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                        wifiSocket2.WIFI_Write(setHTKOT);    //
                                        wifiSocket2.WIFI_Write(normal);    //
                                        wifiSocket2.WIFI_Write(total);
                                        wifiSocket2.WIFI_Write(HT);    //
                                        wifiSocket2.WIFI_Write(str1);
                                        wifiSocket2.WIFI_Write(LF);    //
                                        wifiSocket2.WIFI_Write("         ");
                                        wifiSocket2.WIFI_Write(str2);
                                        wifiSocket2.WIFI_Write(left);    //
                                        wifiSocket2.WIFI_Write(LF);    //
                                    } else {
                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write(total);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write(str1);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            BT_Write("         ");
                                            BT_Write(str2);
                                            BluetoothPrintDriver.BT_Write(left);    //
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        }
                                    }
                                }
                            } else {
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                };
                                if (textViewstatusnets.getText().toString().equals("ok")) {
                                    wifiSocket.WIFI_Write(setHTKOT);    //
                                    wifiSocket.WIFI_Write(normal);    //
                                    wifiSocket.WIFI_Write(total);
                                    wifiSocket.WIFI_Write(HT);    //
                                    wifiSocket.WIFI_Write(name);
                                    wifiSocket.WIFI_Write(left);    //
                                    wifiSocket.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                        wifiSocket2.WIFI_Write(setHTKOT);    //
                                        wifiSocket2.WIFI_Write(normal);    //
                                        wifiSocket2.WIFI_Write(total);
                                        wifiSocket2.WIFI_Write(HT);    //
                                        wifiSocket2.WIFI_Write(name);
                                        wifiSocket2.WIFI_Write(left);    //
                                        wifiSocket2.WIFI_Write(LF);    //
                                    } else {
                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write(total);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write(name);
                                            BluetoothPrintDriver.BT_Write(left);    //
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        }
                                    }
                                }
                            }

                            do {
                                final String modiname = modcursorc.getString(2);
                                final String modiquan = modcursorc.getString(1);
                                String modiprice = modcursorc.getString(3);
                                String moditotal = modcursorc.getString(4);
                                final String modiid = modcursorc.getString(0);
                                String mod = modiname;


                                if (mod.toString().length() > charlength) {
                                    String str1 = mod.substring(0, charlength);
                                    String str2 = mod.substring(charlength);
                                    allbufmodifiers = new byte[][]{
                                            setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), str1.getBytes(), LF, "         ".getBytes(), str2.getBytes(), left, LF,
                                    };
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(setHTKOT);    //
                                        wifiSocket.WIFI_Write(normal);    //
                                        wifiSocket.WIFI_Write("");
                                        wifiSocket.WIFI_Write(HT);    //
                                        wifiSocket.WIFI_Write(">");
                                        wifiSocket.WIFI_Write(str1);
                                        wifiSocket.WIFI_Write(LF);    //
                                        wifiSocket.WIFI_Write("         ");
                                        wifiSocket.WIFI_Write(str2);
                                        wifiSocket.WIFI_Write(left);    //
                                        wifiSocket.WIFI_Write(LF);    //
                                    } else {
                                        if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                            wifiSocket2.WIFI_Write(setHTKOT);    //
                                            wifiSocket2.WIFI_Write(normal);    //
                                            wifiSocket2.WIFI_Write("");
                                            wifiSocket2.WIFI_Write(HT);    //
                                            wifiSocket2.WIFI_Write(">");
                                            wifiSocket2.WIFI_Write(str1);
                                            wifiSocket2.WIFI_Write(LF);    //
                                            wifiSocket2.WIFI_Write("         ");
                                            wifiSocket2.WIFI_Write(str2);
                                            wifiSocket2.WIFI_Write(left);    //
                                            wifiSocket2.WIFI_Write(LF);    //
                                        } else {
                                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                                BluetoothPrintDriver.BT_Write(normal);    //
                                                BT_Write("");
                                                BluetoothPrintDriver.BT_Write(HT);    //
                                                BT_Write(">");
                                                BT_Write(str1);
                                                BluetoothPrintDriver.BT_Write(LF);    //
                                                BT_Write("      ");
                                                BT_Write(str2);
                                                BluetoothPrintDriver.BT_Write(left);    //
                                                BluetoothPrintDriver.BT_Write(LF);    //
                                            }
                                        }
                                    }
                                } else {
                                    allbufmodifiers = new byte[][]{
                                            setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), mod.getBytes(), left, LF
                                    };
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(setHTKOT);    //
                                        wifiSocket.WIFI_Write(normal);    //
                                        wifiSocket.WIFI_Write("");
                                        wifiSocket.WIFI_Write(HT);    //
                                        wifiSocket.WIFI_Write(">");
                                        wifiSocket.WIFI_Write(mod);
                                        wifiSocket.WIFI_Write(left);    //
                                        wifiSocket.WIFI_Write(LF);    //
                                    } else {
                                        if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                            wifiSocket2.WIFI_Write(setHTKOT);    //
                                            wifiSocket2.WIFI_Write(normal);    //
                                            wifiSocket2.WIFI_Write("");
                                            wifiSocket2.WIFI_Write(HT);    //
                                            wifiSocket2.WIFI_Write(">");
                                            wifiSocket2.WIFI_Write(mod);
                                            wifiSocket2.WIFI_Write(left);    //
                                            wifiSocket2.WIFI_Write(LF);    //
                                        } else {
                                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                                BluetoothPrintDriver.BT_Write(normal);    //
                                                BT_Write("");
                                                BluetoothPrintDriver.BT_Write(HT);    //
                                                BT_Write(">");
                                                BT_Write(mod);
                                                BluetoothPrintDriver.BT_Write(left);    //
                                                BluetoothPrintDriver.BT_Write(LF);    //
                                            }
                                        }
                                    }
                                }
                            } while (modcursorc.moveToNext());
                        } else {
                            if (name.toString().length() > charlength) {
                                String str1 = name.substring(0, charlength);
                                String str2 = name.substring(charlength);
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "         ".getBytes(), str2.getBytes(), left, LF,
                                };
                                if (textViewstatusnets.getText().toString().equals("ok")) {
                                    wifiSocket.WIFI_Write(setHTKOT);    //
                                    wifiSocket.WIFI_Write(normal);    //
                                    wifiSocket.WIFI_Write(total);
                                    wifiSocket.WIFI_Write(HT);    //
                                    wifiSocket.WIFI_Write(str1);
                                    wifiSocket.WIFI_Write(LF);    //
                                    wifiSocket.WIFI_Write("         ");
                                    wifiSocket.WIFI_Write(str2);
                                    wifiSocket.WIFI_Write(left);    //
                                    wifiSocket.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                        wifiSocket2.WIFI_Write(setHTKOT);    //
                                        wifiSocket2.WIFI_Write(normal);    //
                                        wifiSocket2.WIFI_Write(total);
                                        wifiSocket2.WIFI_Write(HT);    //
                                        wifiSocket2.WIFI_Write(str1);
                                        wifiSocket2.WIFI_Write(LF);    //
                                        wifiSocket2.WIFI_Write("         ");
                                        wifiSocket2.WIFI_Write(str2);
                                        wifiSocket2.WIFI_Write(left);    //
                                        wifiSocket2.WIFI_Write(LF);    //
                                    } else {
                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write(total);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write(str1);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            BT_Write("      ");
                                            BT_Write(str2);
                                            BluetoothPrintDriver.BT_Write(left);    //
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        }
                                    }
                                }
                            } else {
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                };
                                if (textViewstatusnets.getText().toString().equals("ok")) {
                                    wifiSocket.WIFI_Write(setHTKOT);    //
                                    wifiSocket.WIFI_Write(normal);    //
                                    wifiSocket.WIFI_Write(total);
                                    wifiSocket.WIFI_Write(HT);    //
                                    wifiSocket.WIFI_Write(name);
                                    wifiSocket.WIFI_Write(left);    //
                                    wifiSocket.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                        wifiSocket2.WIFI_Write(setHTKOT);    //
                                        wifiSocket2.WIFI_Write(normal);    //
                                        wifiSocket2.WIFI_Write(total);
                                        wifiSocket2.WIFI_Write(HT);    //
                                        wifiSocket2.WIFI_Write(name);
                                        wifiSocket2.WIFI_Write(left);    //
                                        wifiSocket2.WIFI_Write(LF);    //
                                    } else {
                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write(total);
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write(name);
                                            BluetoothPrintDriver.BT_Write(left);    //
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        }
                                    }
                                }
                            }
                        }
                    }

                } while (cursor.moveToNext());
            }

            if (textViewstatusnets.getText().toString().equals("ok")) {
                wifiSocket.WIFI_Write(left);    //
                wifiSocket.WIFI_Write(un1);    //
                wifiSocket.WIFI_Write(str_line);
                wifiSocket.WIFI_Write(LF);    //
            } else {
                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(left);    //
                    wifiSocket2.WIFI_Write(un1);    //
                    wifiSocket2.WIFI_Write(str_line);
                    wifiSocket2.WIFI_Write(LF);    //
                } else {
                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                        BluetoothPrintDriver.BT_Write(left);    //
                        BluetoothPrintDriver.BT_Write(un1);    //
                        BT_Write(str_line);
                        BluetoothPrintDriver.BT_Write(LF);    //
                    }
                }
            }
            //feedcut();

            Cursor toalitems = db1.rawQuery("Select * from Table" + ItemIDtable, null);
            if (toalitems.moveToFirst()) {
                Cursor toalitems2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity <= '1.0'", null);
                if (toalitems2.moveToFirst()) {
                    int toalzx = toalitems2.getCount();
                    totalquanret1 = String.valueOf(toalzx);
                }
                Cursor toalitems1 = db1.rawQuery("Select sum(quantity) from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity > '1.0'", null);
                if (toalitems1.moveToFirst()) {
                    int toalzx = toalitems1.getInt(0);
                    totalquanret2 = String.valueOf(toalzx);
                }
                int cvvc = Integer.parseInt(totalquanret1) + Integer.parseInt(totalquanret2);
                String total = String.valueOf(cvvc);
                String totalqu = "No. of items : " + total;
                allbuf11 = new byte[][]{
                        left, setHT321, totalqu.getBytes(), LF
                };
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(left);    //
                    wifiSocket.WIFI_Write(setHT321);    //
                    wifiSocket.WIFI_Write(totalqu);
                    wifiSocket.WIFI_Write(LF);    //
                } else {
                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                        wifiSocket2.WIFI_Write(left);    //
                        wifiSocket2.WIFI_Write(setHT321);    //
                        wifiSocket2.WIFI_Write(totalqu);
                        wifiSocket2.WIFI_Write(LF);    //
                    } else {
                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                            BluetoothPrintDriver.BT_Write(left);    //
                            BluetoothPrintDriver.BT_Write(setHT321);    //
                            BT_Write(totalqu);
                            BluetoothPrintDriver.BT_Write(LF);    //
                        }
                    }
                }
            }


            byte[][] allbuf = new byte[][]{
                    feedcut2
            };
            if (textViewstatusnets.getText().toString().equals("ok")) {
                wifiSocket.WIFI_Write(feedcut2);    //
            } else {
                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(feedcut2);    //
                } else {
                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                        BluetoothPrintDriver.BT_Write(feedcut2);    //
                    }
                }
            }

            if (str_print_ty.toString().equals("POS")) {
                if (textViewstatusnets.getText().toString().equals("ok")) {
                    wifiSocket.WIFI_Write(feedcut2);    //
                } else {
                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                        wifiSocket2.WIFI_Write(feedcut2);    //
                    } else {
                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                            BluetoothPrintDriver.BT_Write(feedcut2);    //
                        }
                    }
                }
            }
        }
    }

    public  byte[] neoprintbillsplithome() {


        Typeface tf = Typeface.SERIF;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ReceiptBitmap receiptBitmap = new ReceiptBitmap().getInstance();
        int cont=0;
        Cursor cursor34 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE status = 'print'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (cursor34.moveToFirst()) {
            cont=cursor34.getCount();

        }
        cursor34.close();
        receiptBitmap.init(200+(cont*50));
        receiptBitmap.setTextSize(25);
        receiptBitmap.setTypeface(Typeface.create(tf, Typeface.NORMAL));


        charlength = 10;
        charlength1 = 20;
        charlength2 = 30;
        quanlentha = 5;

        Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
        if (getcom.moveToFirst()) {
            strcompanyname = getcom.getString(1);
        }
        getcom.close();

        tvkot.setText(strcompanyname);
        if (tvkot.getText().toString().equals("")) {

        } else {
            // Print.StartPrinting(strcompanyname ,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
            receiptBitmap.drawCenterText(strcompanyname);
        }

        receiptBitmap.drawCenterText("Order Ticket copy");



        Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
        if (vbnm.moveToFirst()) {
            assa1 = vbnm.getString(1);
            assa2 = vbnm.getString(2);
        }
        vbnm.close();
        TextView cx = new TextView(KOT_Management.this);
        cx.setText(assa1);
        String tablen = "";
        if (cx.getText().toString().equals("")) {
            tablen = "Tab" + assa2;
        } else {
            tablen = "Tab" + assa1;
        }


        //   Print.StartPrinting(NAme111+"   "+tablen,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        receiptBitmap.drawLeftText(tablen);


        SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyyyy");
        final String normal1 = normal2.format(new Date());

        Date dt = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
        final String time1 = sdf1.format(dt);

        Date dtt = new Date();
        SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
        String time24 = sdf1t.format(dtt);

        receiptBitmap.drawLeftText(normal1+"   "+time1);
        String str_line="----------------------";
        receiptBitmap.drawLeftText(str_line);
        receiptBitmap.drawLeftText("Qty"+"  "+"Item");
        receiptBitmap.drawLeftText(str_line);





        Cursor cursor = db1.rawQuery("Select * from Table" + ItemIDtable + "", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (cursor.moveToFirst()) {
            do {
                String Itemtype = cursor.getString(5);
                String total = cursor.getString(20);
                final String idid = cursor.getString(0);
                String name = cursor.getString(2);
                final String iidd = cursor.getString(0);
                final String hii = cursor.getString(2);
                final String newtt = cursor.getString(4);
                final float f = Float.parseFloat(cursor.getString(3));
                String price = String.valueOf(f);
                String stat = cursor.getString(16);
                String sev = cursor.getString(7);
                String add_note_print = cursor.getString(30);

                TextView tvbh = new TextView(KOT_Management.this);

                tvbh.setText(stat);


                TextView tv_add_note_print = new TextView(KOT_Management.this);
                tv_add_note_print.setText(add_note_print);


                if (Itemtype.toString().equals("Item")) {

                    Cursor modcursorc = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE parent = '" + hii + "' AND parentid = '" + idid + "'  ", null);
                    if (modcursorc.moveToFirst()) {
                        Cursor modt = db1.rawQuery("Select SUM(total) from Table" + ItemIDtable + " WHERE parent = '" + name + "' AND parentid = '" + idid + "' ", null);
                        if (modt.moveToFirst()) {
                            do {
                                //row.removeView(tv3);
                                float aq = modt.getFloat(0);
                                aqq = String.valueOf(aq);
                                aqq1 = Float.parseFloat(aqq) + Float.parseFloat(newtt);
                                aqq2 = String.valueOf(aqq1);
                            } while (modt.moveToNext());
                        }
                        modt.close();

                        if (name.toString().length() > charlength) {
                            String str1 = name.substring(0, charlength);
                            String str2 = name.substring(charlength);

                            receiptBitmap.drawLeftText(total+"  "+str1);
                            receiptBitmap.drawLeftText(str2);
                            receiptBitmap.drawLeftText(add_note_print);

                        } else {

                            receiptBitmap.drawLeftText(total);
                            receiptBitmap.drawLeftText(name);


                            if (tv_add_note_print.getText().toString().equals("")){

                            }else {
                                receiptBitmap.drawLeftText(add_note_print);

                            }

                        }



                        do {
                            final String modiname = modcursorc.getString(2);
                            final String modiquan = modcursorc.getString(1);
                            String modiprice = modcursorc.getString(3);
                            String moditotal = modcursorc.getString(4);
                            final String modiid = modcursorc.getString(0);
                            String mod = modiname;


                            if (mod.toString().length() > charlength) {
                                String str1 = mod.substring(0, charlength);
                                String str2 = mod.substring(charlength);

                                receiptBitmap.drawLeftText(""+"  "+">"+str1);
                                receiptBitmap.drawLeftText("      "+str2);


                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    receiptBitmap.drawLeftText(add_note_print);

                                }

                            } else {


                                receiptBitmap.drawLeftText(""+"  "+">"+mod);

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {

                                    receiptBitmap.drawLeftText(add_note_print);
                                }

                            }



                        } while (modcursorc.moveToNext());
                    } else {
                        if (name.toString().length() > charlength) {
                            String str1 = name.substring(0, charlength);
                            String str2 = name.substring(charlength);


                            receiptBitmap.drawLeftText(total+"  "+str1);
                            receiptBitmap.drawLeftText(str2);



                            if (tv_add_note_print.getText().toString().equals("")){

                            }else {
                                receiptBitmap.drawLeftText(add_note_print);
                            }

                        } else {

                            receiptBitmap.drawLeftText(total+"  "+name);

                            if (tv_add_note_print.getText().toString().equals("")){

                            }else {

                                receiptBitmap.drawLeftText(add_note_print);

                            }

                        }



                    }
                    modcursorc.close();
                }

            } while (cursor.moveToNext());
        }
        cursor.close();

        receiptBitmap.drawLeftText(str_line);

        Cursor toalitems = db1.rawQuery("Select * from Table" + ItemIDtable, null);
        if (toalitems.moveToFirst()) {
            Cursor toalitems2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity <= '1.0'", null);
            if (toalitems2.moveToFirst()) {
                int toalzx = toalitems2.getCount();
                totalquanret1 = String.valueOf(toalzx);
            }
            toalitems2.close();

            Cursor toalitems1 = db1.rawQuery("Select sum(quantity) from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity > '1.0'", null);
            if (toalitems1.moveToFirst()) {
                int toalzx = toalitems1.getInt(0);
                totalquanret2 = String.valueOf(toalzx);
            }
            toalitems1.close();

            int cvvc = Integer.parseInt(totalquanret1) + Integer.parseInt(totalquanret2);
            String total = String.valueOf(cvvc);
            String totalqu = "No. of items : " + total;

            receiptBitmap.drawLeftText(totalqu);


        }
        toalitems.close();


        receiptBitmap.drawLeftText("       ");
        receiptBitmap.drawLeftText("       ");
        receiptBitmap.drawLeftText("       ");

        Bitmap canvasbitmap = receiptBitmap.getReceiptBitmap();

        if(ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"getHeight: " + canvasbitmap.getHeight(),true,true);

        if(ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"getReceiptHeight: " + receiptBitmap.getReceiptHeight(),true,true);

        Bitmap croppedBmp = Bitmap.createBitmap(canvasbitmap, 0, 0, canvasbitmap.getWidth(), canvasbitmap.getHeight());

        byte[] imageCommand = mMSWisepadDeviceController.getPrintData(croppedBmp, 150);

        baos.write(imageCommand, 0, imageCommand.length);

        if(ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"end of reciept",true,true);

        return baos.toByteArray();

    }

    public void wiseposprintbillsplithome() {

        charlength = 10;
        charlength1 = 20;
        charlength2 = 30;
        quanlentha = 5;

        Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
        if (getcom.moveToFirst()) {
            strcompanyname = getcom.getString(1);
        }
        getcom.close();

        tvkot.setText(strcompanyname);
        if (tvkot.getText().toString().equals("")) {

        } else {
            Print.StartPrinting(strcompanyname , FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        }
        Print.StartPrinting("Order Ticket copy" ,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);

        Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
        if (vbnm.moveToFirst()) {
            assa1 = vbnm.getString(1);
            assa2 = vbnm.getString(2);
        }
        vbnm.close();
        TextView cx = new TextView(KOT_Management.this);
        cx.setText(assa1);
        String tablen = "";
        if (cx.getText().toString().equals("")) {
            tablen = "Tab" + assa2;
        } else {
            tablen = "Tab" + assa1;
        }


        Print.StartPrinting(tablen,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);



        SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyyyy");
        final String normal1 = normal2.format(new Date());

        Date dt = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
        final String time1 = sdf1.format(dt);

        Date dtt = new Date();
        SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
        String time24 = sdf1t.format(dtt);


        Print.StartPrinting(normal1+"   "+time1,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);

        String str_line="----------------------";
        Print.StartPrinting(str_line,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);

        Print.StartPrinting("Qty"+"  "+"Item",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
        Print.StartPrinting(str_line,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);



        Cursor cursor = db1.rawQuery("Select * from Table" + ItemIDtable + "", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (cursor.moveToFirst()) {
            do {
                String Itemtype = cursor.getString(5);
                String total = cursor.getString(20);
                final String idid = cursor.getString(0);
                String name = cursor.getString(2);
                final String iidd = cursor.getString(0);
                final String hii = cursor.getString(2);
                final String newtt = cursor.getString(4);
                final float f = Float.parseFloat(cursor.getString(3));
                String price = String.valueOf(f);
                String stat = cursor.getString(16);
                String sev = cursor.getString(7);
                String add_note_print = cursor.getString(30);

                TextView tvbh = new TextView(KOT_Management.this);

                tvbh.setText(stat);


                TextView tv_add_note_print = new TextView(KOT_Management.this);
                tv_add_note_print.setText(add_note_print);


                if (Itemtype.toString().equals("Item")) {

                    Cursor modcursorc = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE parent = '" + hii + "' AND parentid = '" + idid + "'  ", null);
                    if (modcursorc.moveToFirst()) {
                        Cursor modt = db1.rawQuery("Select SUM(total) from Table" + ItemIDtable + " WHERE parent = '" + name + "' AND parentid = '" + idid + "' ", null);
                        if (modt.moveToFirst()) {
                            do {
                                //row.removeView(tv3);
                                float aq = modt.getFloat(0);
                                aqq = String.valueOf(aq);
                                aqq1 = Float.parseFloat(aqq) + Float.parseFloat(newtt);
                                aqq2 = String.valueOf(aqq1);
                            } while (modt.moveToNext());
                        }
                        modt.close();

                        if (name.toString().length() > charlength) {
                            String str1 = name.substring(0, charlength);
                            String str2 = name.substring(charlength);

                            Print.StartPrinting(total+"  "+str1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                            Print.StartPrinting(str2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                            Print.StartPrinting(add_note_print,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);


                        } else {

                            Print.StartPrinting(total,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                            Print.StartPrinting(name,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                            if (tv_add_note_print.getText().toString().equals("")){

                            }else {
                                Print.StartPrinting(add_note_print,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                            }

                        }



                        do {
                            final String modiname = modcursorc.getString(2);
                            final String modiquan = modcursorc.getString(1);
                            String modiprice = modcursorc.getString(3);
                            String moditotal = modcursorc.getString(4);
                            final String modiid = modcursorc.getString(0);
                            String mod = modiname;


                            if (mod.toString().length() > charlength) {
                                String str1 = mod.substring(0, charlength);
                                String str2 = mod.substring(charlength);


                                Print.StartPrinting(""+"  "+">"+str1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                Print.StartPrinting("      "+str2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    Print.StartPrinting(add_note_print,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                }

                            } else {



                                Print.StartPrinting(""+"  "+">"+mod,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    Print.StartPrinting(add_note_print,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                }

                            }



                        } while (modcursorc.moveToNext());
                    } else {
                        if (name.toString().length() > charlength) {
                            String str1 = name.substring(0, charlength);
                            String str2 = name.substring(charlength);


                            Print.StartPrinting(total+"  "+str1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                            Print.StartPrinting(str2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);


                            if (tv_add_note_print.getText().toString().equals("")){

                            }else {
                                Print.StartPrinting(add_note_print,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                            }

                        } else {


                            Print.StartPrinting(total+"  "+name,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                            if (tv_add_note_print.getText().toString().equals("")){

                            }else {
                                Print.StartPrinting(add_note_print,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                            }

                        }

                    }
                    modcursorc.close();
                }



            } while (cursor.moveToNext());
        }
        cursor.close();
        Print.StartPrinting(str_line,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);


        Cursor toalitems = db1.rawQuery("Select * from Table" + ItemIDtable, null);
        if (toalitems.moveToFirst()) {
            Cursor toalitems2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity <= '1.0'", null);
            if (toalitems2.moveToFirst()) {
                int toalzx = toalitems2.getCount();
                totalquanret1 = String.valueOf(toalzx);
            }
            toalitems2.close();

            Cursor toalitems1 = db1.rawQuery("Select sum(quantity) from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity > '1.0'", null);
            if (toalitems1.moveToFirst()) {
                int toalzx = toalitems1.getInt(0);
                totalquanret2 = String.valueOf(toalzx);
            }
            toalitems1.close();

            int cvvc = Integer.parseInt(totalquanret1) + Integer.parseInt(totalquanret2);
            String total = String.valueOf(cvvc);
            String totalqu = "No. of items : " + total;


            Print.StartPrinting(totalqu,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

        }
        toalitems.close();


        Print.StartPrinting("       ",FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        Print.StartPrinting("       ",FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        Print.StartPrinting("       ",FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);

//            if (str_print_ty.toString().equals("POS")) {
//                if (textViewstatusnets.getText().toString().equals("ok")) {
//                    wifiSocket.WIFI_Write(feedcut2);    //
//                } else {
//                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
//                        wifiSocket2.WIFI_Write(feedcut2);    //
//                    } else {
//                        if (textViewstatussusbs.getText().toString().equals("ok")) {
//                            BluetoothPrintDriver.BT_Write(feedcut2);    //
//                        }
//                    }
//                }
//            }

//        Toast.makeText(KOT_Management.this, "KOT printed", Toast.LENGTH_LONG).show();
    }

    public  byte[] neoprintbillsplithome1(String kot) {


        Typeface tf = Typeface.SERIF;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ReceiptBitmap receiptBitmap = new ReceiptBitmap().getInstance();
        int cont=0;
        Cursor cursor34 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE status = 'print'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (cursor34.moveToFirst()) {
            cont=cursor34.getCount();

        }
        cursor34.close();
        receiptBitmap.init(200+(cont*50));
        receiptBitmap.setTextSize(25);
        receiptBitmap.setTypeface(Typeface.create(tf, Typeface.NORMAL));


        charlength = 10;
        charlength1 = 20;
        charlength2 = 30;
        quanlentha = 5;

        Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
        if (getcom.moveToFirst()) {
            strcompanyname = getcom.getString(1);
        }
        getcom.close();

        tvkot.setText(strcompanyname);
        if (tvkot.getText().toString().equals("")) {

        } else {
            // Print.StartPrinting(strcompanyname ,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
            receiptBitmap.drawCenterText(strcompanyname);
        }

        receiptBitmap.drawCenterText("Order Ticket copy");



        Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
        if (vbnm.moveToFirst()) {
            assa1 = vbnm.getString(1);
            assa2 = vbnm.getString(2);
        }
        vbnm.close();
        TextView cx = new TextView(KOT_Management.this);
        cx.setText(assa1);
        String tablen = "";
        if (cx.getText().toString().equals("")) {
            tablen = "Tab" + assa2;
        } else {
            tablen = "Tab" + assa1;
        }


        //   Print.StartPrinting(NAme111+"   "+tablen,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        receiptBitmap.drawLeftText(tablen);


        SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyyyy");
        String normal1 = normal2.format(new Date());

        Date dt = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
        String time1 = sdf1.format(dt);

        Date dtt = new Date();
        SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
        String time24 = sdf1t.format(dtt);

        Cursor cu_da = db1.rawQuery("SELECT * from Table" + ItemIDtable + "management WHERE tagg = '" + kot + "'", null);
        if (cu_da.moveToFirst()) {
            normal1 = cu_da.getString(4);
            time1 = cu_da.getString(5);
        }

        receiptBitmap.drawLeftText(normal1+"   "+time1);
        String str_line="----------------------";
        receiptBitmap.drawLeftText(str_line);
        receiptBitmap.drawLeftText("Qty"+"  "+"Item");
        receiptBitmap.drawLeftText(str_line);




        Cursor cursor = db1.rawQuery("Select * from Table" + ItemIDtable + "management WHERE tagg = '" + kot + "' AND itemtype = 'Item' GROUP BY par_id", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (cursor.moveToFirst()) {
            do {
                String Itemtype = cursor.getString(5);
                String total = cursor.getString(20);
                final String idid = cursor.getString(0);
                String name = cursor.getString(2);
                final String iidd = cursor.getString(0);
                final String hii = cursor.getString(2);
                final String newtt = cursor.getString(4);
                final float f = Float.parseFloat(cursor.getString(3));
                String price = String.valueOf(f);
                String stat = cursor.getString(16);
                String sev = cursor.getString(7);
                String add_note_print = cursor.getString(30);

                TextView tvbh = new TextView(KOT_Management.this);

                tvbh.setText(stat);


                TextView tv_add_note_print = new TextView(KOT_Management.this);
                tv_add_note_print.setText(add_note_print);


                if (Itemtype.toString().equals("Item")) {

                    Cursor modcursorc = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE parent = '" + hii + "' AND parentid = '" + idid + "'  ", null);
                    if (modcursorc.moveToFirst()) {
                        Cursor modt = db1.rawQuery("Select SUM(total) from Table" + ItemIDtable + " WHERE parent = '" + name + "' AND parentid = '" + idid + "' ", null);
                        if (modt.moveToFirst()) {
                            do {
                                //row.removeView(tv3);
                                float aq = modt.getFloat(0);
                                aqq = String.valueOf(aq);
                                aqq1 = Float.parseFloat(aqq) + Float.parseFloat(newtt);
                                aqq2 = String.valueOf(aqq1);
                            } while (modt.moveToNext());
                        }
                        modt.close();

                        if (name.toString().length() > charlength) {
                            String str1 = name.substring(0, charlength);
                            String str2 = name.substring(charlength);

                            receiptBitmap.drawLeftText(total+"  "+str1);
                            receiptBitmap.drawLeftText(str2);
                            receiptBitmap.drawLeftText(add_note_print);

                        } else {

                            receiptBitmap.drawLeftText(total);
                            receiptBitmap.drawLeftText(name);


                            if (tv_add_note_print.getText().toString().equals("")){

                            }else {
                                receiptBitmap.drawLeftText(add_note_print);

                            }

                        }



                        do {
                            final String modiname = modcursorc.getString(2);
                            final String modiquan = modcursorc.getString(1);
                            String modiprice = modcursorc.getString(3);
                            String moditotal = modcursorc.getString(4);
                            final String modiid = modcursorc.getString(0);
                            String mod = modiname;


                            if (mod.toString().length() > charlength) {
                                String str1 = mod.substring(0, charlength);
                                String str2 = mod.substring(charlength);

                                receiptBitmap.drawLeftText(""+"  "+">"+str1);
                                receiptBitmap.drawLeftText("      "+str2);


                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    receiptBitmap.drawLeftText(add_note_print);

                                }

                            } else {


                                receiptBitmap.drawLeftText(""+"  "+">"+mod);

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {

                                    receiptBitmap.drawLeftText(add_note_print);
                                }

                            }



                        } while (modcursorc.moveToNext());
                    } else {
                        if (name.toString().length() > charlength) {
                            String str1 = name.substring(0, charlength);
                            String str2 = name.substring(charlength);


                            receiptBitmap.drawLeftText(total+"  "+str1);
                            receiptBitmap.drawLeftText(str2);



                            if (tv_add_note_print.getText().toString().equals("")){

                            }else {
                                receiptBitmap.drawLeftText(add_note_print);
                            }

                        } else {

                            receiptBitmap.drawLeftText(total+"  "+name);

                            if (tv_add_note_print.getText().toString().equals("")){

                            }else {

                                receiptBitmap.drawLeftText(add_note_print);

                            }

                        }



                    }
                    modcursorc.close();
                }



            } while (cursor.moveToNext());
        }
        cursor.close();

        receiptBitmap.drawLeftText(str_line);

        Cursor toalitems = db1.rawQuery("Select * from Table" + ItemIDtable + "management WHERE tagg = '" + kot + "'", null);
        if (toalitems.moveToFirst()) {
            Cursor toalitems2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity <= '1.0'", null);
            if (toalitems2.moveToFirst()) {
                int toalzx = toalitems2.getCount();
                totalquanret1 = String.valueOf(toalzx);
            }
            toalitems2.close();

            Cursor toalitems1 = db1.rawQuery("Select sum(quantity) from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity > '1.0'", null);
            if (toalitems1.moveToFirst()) {
                int toalzx = toalitems1.getInt(0);
                totalquanret2 = String.valueOf(toalzx);
            }
            toalitems1.close();

            int cvvc = Integer.parseInt(totalquanret1) + Integer.parseInt(totalquanret2);
            String total = String.valueOf(cvvc);
            String totalqu = "No. of items : " + total;

            receiptBitmap.drawLeftText(totalqu);


        }
        toalitems.close();


        receiptBitmap.drawLeftText("       ");
        receiptBitmap.drawLeftText("       ");
        receiptBitmap.drawLeftText("       ");

        Bitmap canvasbitmap = receiptBitmap.getReceiptBitmap();

        if(ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"getHeight: " + canvasbitmap.getHeight(),true,true);

        if(ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"getReceiptHeight: " + receiptBitmap.getReceiptHeight(),true,true);

        Bitmap croppedBmp = Bitmap.createBitmap(canvasbitmap, 0, 0, canvasbitmap.getWidth(), canvasbitmap.getHeight());

        byte[] imageCommand = mMSWisepadDeviceController.getPrintData(croppedBmp, 150);

        baos.write(imageCommand, 0, imageCommand.length);

        if(ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"end of reciept",true,true);

        return baos.toByteArray();

    }

    public void wiseposprintbillsplithome1(String kot) {

        charlength = 10;
        charlength1 = 20;
        charlength2 = 30;
        quanlentha = 5;

        Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
        if (getcom.moveToFirst()) {
            strcompanyname = getcom.getString(1);
        }
        getcom.close();

        tvkot.setText(strcompanyname);
        if (tvkot.getText().toString().equals("")) {

        } else {
            Print.StartPrinting(strcompanyname , FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        }
        Print.StartPrinting("Order Ticket copy" ,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);

        Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
        if (vbnm.moveToFirst()) {
            assa1 = vbnm.getString(1);
            assa2 = vbnm.getString(2);
        }
        vbnm.close();
        TextView cx = new TextView(KOT_Management.this);
        cx.setText(assa1);
        String tablen = "";
        if (cx.getText().toString().equals("")) {
            tablen = "Tab" + assa2;
        } else {
            tablen = "Tab" + assa1;
        }


        Print.StartPrinting(tablen,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);



        SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyyyy");
        String normal1 = normal2.format(new Date());

        Date dt = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
        String time1 = sdf1.format(dt);

        Date dtt = new Date();
        SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
        String time24 = sdf1t.format(dtt);

        Cursor cu_da = db1.rawQuery("SELECT * from Table" + ItemIDtable + "management WHERE tagg = '" + kot + "'", null);
        if (cu_da.moveToFirst()) {
            normal1 = cu_da.getString(4);
            time1 = cu_da.getString(5);
        }

        Print.StartPrinting(normal1+"   "+time1,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);

        String str_line="----------------------";
        Print.StartPrinting(str_line,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);

        Print.StartPrinting("Qty"+"  "+"Item",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
        Print.StartPrinting(str_line,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);



        Cursor cursor = db1.rawQuery("Select * from Table" + ItemIDtable + "management WHERE tagg = '" + kot + "' AND itemtype = 'Item' GROUP BY par_id", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (cursor.moveToFirst()) {
            do {
                String Itemtype = cursor.getString(7);
                String total = cursor.getString(2);
                final String idid = cursor.getString(6);
                String name = cursor.getString(1);

                TextView tvbh = new TextView(KOT_Management.this);




                TextView tv_add_note_print = new TextView(KOT_Management.this);
                tv_add_note_print.setText("");


                if (Itemtype.toString().equals("Item")) {

                    Cursor modcursorc = db1.rawQuery("Select * from Table" + ItemIDtable + "management WHERE par_id = '" + idid + "' AND itemtype = 'Modifier' GROUP BY itemname, par_id, tagg", null);
                    if (modcursorc.moveToFirst()) {


                        if (name.toString().length() > charlength) {
                            String str1 = name.substring(0, charlength);
                            String str2 = name.substring(charlength);

                            Print.StartPrinting(total+"  "+str1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                            Print.StartPrinting(str2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                            Print.StartPrinting("",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);


                        } else {

                            Print.StartPrinting(total,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                            Print.StartPrinting(name,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                            if (tv_add_note_print.getText().toString().equals("")){

                            }else {
                                Print.StartPrinting("",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                            }

                        }



                        do {
                            final String modiname = modcursorc.getString(2);
                            final String modiquan = modcursorc.getString(1);
                            String modiprice = modcursorc.getString(3);
                            String moditotal = modcursorc.getString(4);
                            final String modiid = modcursorc.getString(0);
                            String mod = modiname;


                            if (mod.toString().length() > charlength) {
                                String str1 = mod.substring(0, charlength);
                                String str2 = mod.substring(charlength);


                                Print.StartPrinting(""+"  "+">"+str1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                Print.StartPrinting("      "+str2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    Print.StartPrinting("",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                }

                            } else {



                                Print.StartPrinting(""+"  "+">"+mod,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                                if (tv_add_note_print.getText().toString().equals("")){

                                }else {
                                    Print.StartPrinting("",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                                }

                            }



                        } while (modcursorc.moveToNext());
                    } else {
                        if (name.toString().length() > charlength) {
                            String str1 = name.substring(0, charlength);
                            String str2 = name.substring(charlength);


                            Print.StartPrinting(total+"  "+str1,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                            Print.StartPrinting(str2,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);


                            if (tv_add_note_print.getText().toString().equals("")){

                            }else {
                                Print.StartPrinting("",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
                            }

                        } else {


                            Print.StartPrinting(total+"  "+name,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                            if (tv_add_note_print.getText().toString().equals("")){

                            }else {
                                Print.StartPrinting("",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

                            }

                        }

                    }
                    modcursorc.close();
                }



            } while (cursor.moveToNext());
        }
        cursor.close();
        Print.StartPrinting(str_line,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);


        Cursor toalitems = db1.rawQuery("Select * from Table" + ItemIDtable + "management WHERE tagg = '" + kot + "'", null);
        if (toalitems.moveToFirst()) {
            Cursor toalitems2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity <= '1.0'", null);
            if (toalitems2.moveToFirst()) {
                int toalzx = toalitems2.getCount();
                totalquanret1 = String.valueOf(toalzx);
            }
            toalitems2.close();

            Cursor toalitems1 = db1.rawQuery("Select sum(quantity) from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity > '1.0'", null);
            if (toalitems1.moveToFirst()) {
                int toalzx = toalitems1.getInt(0);
                totalquanret2 = String.valueOf(toalzx);
            }
            toalitems1.close();

            int cvvc = Integer.parseInt(totalquanret1) + Integer.parseInt(totalquanret2);
            String total = String.valueOf(cvvc);
            String totalqu = "No. of items : " + total;


            Print.StartPrinting(totalqu,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

        }
        toalitems.close();


        Print.StartPrinting("       ",FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        Print.StartPrinting("       ",FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        Print.StartPrinting("       ",FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);

//            if (str_print_ty.toString().equals("POS")) {
//                if (textViewstatusnets.getText().toString().equals("ok")) {
//                    wifiSocket.WIFI_Write(feedcut2);    //
//                } else {
//                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
//                        wifiSocket2.WIFI_Write(feedcut2);    //
//                    } else {
//                        if (textViewstatussusbs.getText().toString().equals("ok")) {
//                            BluetoothPrintDriver.BT_Write(feedcut2);    //
//                        }
//                    }
//                }
//            }

//        Toast.makeText(KOT_Management.this, "KOT printed", Toast.LENGTH_LONG).show();
    }


    private boolean runPrintCouponSequence1(String kot) {
        if (!initializeObject()) {
            return false;
        }

        if (!createCouponData1(kot)) {
            finalizeObject();
            return false;
        }

        if (!printData()) {
            finalizeObject();
            return false;
        }

        return true;
    }

    private boolean runPrintCouponSequence() {
        if (!initializeObject()) {
            return false;
        }

        if (!createCouponData()) {
            finalizeObject();
            return false;
        }

        if (!printData()) {
            finalizeObject();
            return false;
        }

        return true;
    }

    private boolean runPrintCouponSequence_kotcancel(String kot_no2, String date1, String time1, String reason) {
        if (!initializeObject()) {
            return false;
        }

        if (!createCouponData_kotcancel(kot_no2, date1, time1, reason)) {
            finalizeObject();
            return false;
        }

        if (!printData()) {
            finalizeObject();
            return false;
        }

        return true;
    }


    private boolean initializeObject() {
        try {
            mPrinter = new Printer(((SpnModelsItem) mSpnSeries.getSelectedItem()).getModelConstant(),
                    ((SpnModelsItem) mSpnLang.getSelectedItem()).getModelConstant(),
                    mContext);
        } catch (Exception e) {
//            Toast.makeText(KOT_Management.this, "Here3", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, "Printer", mContext);
            return false;
        }

        mPrinter.setReceiveEventListener(this);

        return true;
    }

    private void finalizeObject() {
        if (mPrinter == null) {
            return;
        }

        mPrinter.clearCommandBuffer();

        mPrinter.setReceiveEventListener(null);

        mPrinter = null;
    }

    @Override
    public void onPtrReceive(final Printer printerObj, final int code, final PrinterStatusInfo status, final String printJobId) {
        runOnUiThread(new Runnable() {
            @Override
            public synchronized void run() {
                ShowMsg.showResult(code, makeErrorMessage(status), mContext);

                dispPrinterWarnings(status);

//                updateButtonState(true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        disconnectPrinter();
                    }
                }).start();
            }
        });
    }

    private String makeErrorMessage(PrinterStatusInfo status) {
        String msg = "";

        if (status.getOnline() == Printer.FALSE) {
            msg += getString(R.string.handlingmsg_err_offline);
        }
        if (status.getConnection() == Printer.FALSE) {
            msg += getString(R.string.handlingmsg_err_no_response);
        }
        if (status.getCoverOpen() == Printer.TRUE) {
            msg += getString(R.string.handlingmsg_err_cover_open);
        }
        if (status.getPaper() == Printer.PAPER_EMPTY) {
            msg += getString(R.string.handlingmsg_err_receipt_end);
        }
        if (status.getPaperFeed() == Printer.TRUE || status.getPanelSwitch() == Printer.SWITCH_ON) {
            msg += getString(R.string.handlingmsg_err_paper_feed);
        }
        if (status.getErrorStatus() == Printer.MECHANICAL_ERR || status.getErrorStatus() == Printer.AUTOCUTTER_ERR) {
            msg += getString(R.string.handlingmsg_err_autocutter);
            msg += getString(R.string.handlingmsg_err_need_recover);
        }
        if (status.getErrorStatus() == Printer.UNRECOVER_ERR) {
            msg += getString(R.string.handlingmsg_err_unrecover);
        }
        if (status.getErrorStatus() == Printer.AUTORECOVER_ERR) {
            if (status.getAutoRecoverError() == Printer.HEAD_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_head);
            }
            if (status.getAutoRecoverError() == Printer.MOTOR_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_motor);
            }
            if (status.getAutoRecoverError() == Printer.BATTERY_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_battery);
            }
            if (status.getAutoRecoverError() == Printer.WRONG_PAPER) {
                msg += getString(R.string.handlingmsg_err_wrong_paper);
            }
        }
        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_0) {
            msg += getString(R.string.handlingmsg_err_battery_real_end);
        }

        return msg;
    }

    private void dispPrinterWarnings(PrinterStatusInfo status) {
//        EditText edtWarnings = (EditText) findViewById(R.id.edtWarnings);
        String warningsMsg = "";

        if (status == null) {
            return;
        }

        if (status.getPaper() == Printer.PAPER_NEAR_END) {
            warningsMsg += getString(R.string.handlingmsg_warn_receipt_near_end);
        }

        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_1) {
            warningsMsg += getString(R.string.handlingmsg_warn_battery_near_end);
        }

//        edtWarnings.setText(warningsMsg);
    }

    private void disconnectPrinter() {
        if (mPrinter == null) {
            return;
        }

        try {
            mPrinter.endTransaction();
        } catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
//                    Toast.makeText(KOT_Management.this, "Here6", Toast.LENGTH_SHORT).show();
                    ShowMsg.showException(e, "endTransaction", mContext);
                }
            });
        }

        try {
            mPrinter.disconnect();
        } catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
//                    Toast.makeText(KOT_Management.this, "Here7", Toast.LENGTH_SHORT).show();
                    ShowMsg.showException(e, "disconnect", mContext);
                }
            });
        }

        finalizeObject();
    }

    private boolean printData() {
        if (mPrinter == null) {
            return false;
        }

        if (!connectPrinter()) {
            return false;
        }

        PrinterStatusInfo status = mPrinter.getStatus();

        dispPrinterWarnings(status);

        if (!isPrintable(status)) {
            ShowMsg.showMsg(makeErrorMessage(status), mContext);
            try {
                mPrinter.disconnect();
            } catch (Exception ex) {
//                Toast.makeText(KOT_Management.this, "Here9", Toast.LENGTH_SHORT).show();
                // Do nothing
            }
            return false;
        }

        try {
            mPrinter.sendData(Printer.PARAM_DEFAULT);
        } catch (Exception e) {
//            Toast.makeText(KOT_Management.this, "Here10", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, "sendData", mContext);
            try {
                mPrinter.disconnect();
            } catch (Exception ex) {
//                Toast.makeText(KOT_Management.this, "Here11", Toast.LENGTH_SHORT).show();
                // Do nothing
            }
            return false;
        }

        return true;
    }

    private boolean isPrintable(PrinterStatusInfo status) {
        if (status == null) {
            return false;
        }

        if (status.getConnection() == Printer.FALSE) {
            return false;
        } else if (status.getOnline() == Printer.FALSE) {
            return false;
        } else {
            ;//print available
        }

        return true;
    }

    private boolean connectPrinter() {
        boolean isBeginTransaction = false;

        if (mPrinter == null) {
            return false;
        }

        try {
            mPrinter.connect(mEditTarget.getText().toString(), Printer.PARAM_DEFAULT);
        } catch (Exception e) {
//            Toast.makeText(KOT_Management.this, "Here4", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, "connect", mContext);
            return false;
        }

        try {
            mPrinter.beginTransaction();
            isBeginTransaction = true;
        } catch (Exception e) {
//            Toast.makeText(KOT_Management.this, "Here12", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, "beginTransaction", mContext);
        }

        if (isBeginTransaction == false) {
            try {
                mPrinter.disconnect();
            } catch (Epos2Exception e) {
//                Toast.makeText(KOT_Management.this, "Here5", Toast.LENGTH_SHORT).show();
                // Do nothing
                return false;
            }
        }

        return true;
    }

    private boolean createCouponData1(String kot) {

        byte[] setHT34M = {0x1b,0x44,0x04,0x11,0x19,0x00};
        byte[] dotfeed = {0x1b,0x4a,0x10};
        byte[] HTRight = {0x1b,0x61, 0x02,0x09};
        byte[] HT = {0x09};
        byte[] LF = {0x0d,0x0a};

        byte[] left = {0x1b,0x61, 0x00};
        byte[] cen = {0x1b,0x61, 0x01};
        byte[] right = {0x1b,0x61, 0x02};
        byte[] bold = {0x1B,0x21,0x10};
        byte[] normal = {0x1d, 0x21, 0x00};
        byte[] horiz1 = {0x1b,0x44,0x19, 0x19, 0x00};
        byte[] horiz = {0x1b,0x44, 0x04, 0x06, 0x04, 0x04, 0x0A, 0x00, 0x09, 0x30, 0x09, 0x31, 0x09, 0x32, 0x09, 0x33, 0x09, 0x34, 0x0d,0x0a};

        byte[] un = {0x1b, 0x2d, 0x02};
        byte[] un1 = {0x1b, 0x2d, 0x00};
        String str_line = "";

        Cursor getprint_type = db.rawQuery("SELECT * FROM Printer_text_size", null);
        if (getprint_type.moveToFirst()) {
            String type = getprint_type.getString(1);

            Cursor cc = db.rawQuery("SELECT * FROM Printerreceiptsize", null);

            if (cc.moveToFirst()) {
                cc.moveToFirst();
                do {
                    NAME = cc.getString(1);
                    if (NAME.equals("3 inch")) {
                        setHT32 = new byte[]{0x1b, 0x44, 0x29, 0x00};//2 tabs 3"
                        setHT321 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 3"
                        setHT3212 = new byte[]{0x1b, 0x44, 0x25, 0x00};//2 tabs 3"
                        setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                        setHT34 = new byte[]{0x1b, 0x44, 0x08, 0x20, 0x29, 0x00};//4 tabs 3"
                        setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 3"
                        feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                        nPaperWidth = 576;
                        charlength = 41;
                        str_line = "------------------------------------------------";
                        allbufline = new byte[][]{
                                left, un1, "------------------------------------------------".getBytes(), LF

                        };
                    } else {

                        Cursor print_ty = db.rawQuery("SELECT * FROM Printer_type", null);
                        if (print_ty.moveToFirst()){
                            str_print_ty = print_ty.getString(1);
                        }
                        if (str_print_ty.toString().equals("Generic")) {
//                            Toast.makeText(KOT_Management.this, "phi", Toast.LENGTH_SHORT).show();
                            setHT32 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 2"
                            setHT321 = new byte[]{0x1b, 0x44, 0x13, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x15, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                            setHT34 = new byte[]{0x1b, 0x44, 0x04, 0x12, 0x19, 0x00};//4 tabs 2"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 2"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                            nPaperWidth = 384;
                            charlength = 25;
                            str_line = "--------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "--------------------------------".getBytes(), LF
                            };
                        }else {
//                            Toast.makeText(KOT_Management.this, "epson", Toast.LENGTH_SHORT).show();
                            setHT32 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 2"
                            setHT321 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x21, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                            setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x19, 0x21, 0x00};//4 tabs 2"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                            nPaperWidth = 384;
                            charlength = 28;
                            str_line = "------------------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "------------------------------------------".getBytes(), LF
                            };
                        }
                    }
                } while (cc.moveToNext());
            }
            cc.close();
        }
        getprint_type.close();

        final int pageAreaHeight = 384;
        final int pageAreaWidth = 384;


        ArrayList<byte[]> list = new ArrayList<byte[]>();
        String method = "";
        String[] col = {"companylogo"};
        Cursor c = db.query("Logo", col, null, null, null, null, null);
        if (c.moveToFirst()) {
            byte[] img = c.getBlob(c.getColumnIndex("companylogo"));
//            logoData = BitmapFactory.decodeByteArray(img, 0, img.length);
        }
        c.close();

        if (mPrinter == null) {
            return false;
        }
        try {
            method = "addPageArea";
            mPrinter.addPageArea(0, 0, nPaperWidth, pageAreaHeight);

            method = "addPageDirection";
            mPrinter.addPageDirection(Printer.DIRECTION_TOP_TO_BOTTOM);

            method = "addPagePosition";
            mPrinter.addTextAlign(Printer.ALIGN_LEFT);
            method = "addImage";

            Cursor connnet = db.rawQuery("SELECT * FROM IPConn", null);
            if (connnet.moveToFirst()) {
                ipnamegets = connnet.getString(1);
                portgets = connnet.getString(2);
                statusnets = connnet.getString(3);
            }

            Cursor connnet_counter = db.rawQuery("SELECT * FROM IPConn_Counter", null);
            if (connnet_counter.moveToFirst()) {
                ipnamegets_counter = connnet_counter.getString(1);
                portgets_counter = connnet_counter.getString(2);
                statusnets_counter = connnet_counter.getString(3);
            }
            connnet_counter.close();

            Cursor connusb = db.rawQuery("SELECT * FROM BTConn", null);
            if (connusb.moveToFirst()) {
                addgets = connusb.getString(1);
                namegets = connusb.getString(2);
                statussusbs = connusb.getString(3);
            }

            textViewstatusnets.setText(statusnet);
            textViewstatusnets_counter.setText(statusnet_counter);
            textViewstatussusbs.setText(statussusbs);

            Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
            if (getcom.moveToFirst()) {
                do {
                    strcompanyname = getcom.getString(1);
                } while (getcom.moveToNext());
            }


            tvkot.setText(strcompanyname);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf1 = new byte[][]{
                        bold, cen, strcompanyname.getBytes(), LF, LF

                };
                if (textViewstatussusbs.getText().toString().equals("ok")) {
                    mPrinter.addCommand(bold);
                    mPrinter.addCommand(cen);
                    StringBuilder textData1 = new StringBuilder();
                    textData1.append(strcompanyname);
                    mPrinter.addText(textData1.toString());
                    mPrinter.addCommand(LF);
                    mPrinter.addCommand(LF); //LF
                }
            }


            allbufKOT = new byte[][]{
                    un, cen, "Order Ticket copy".getBytes(), LF
            };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(un);
                mPrinter.addCommand(cen);
                StringBuilder textData1 = new StringBuilder();
                textData1.append("Order Ticket copy");
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(LF); //LF
            }


            Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
            if (vbnm.moveToFirst()) {
                assa1 = vbnm.getString(1);
                assa2 = vbnm.getString(2);
            }
            TextView cx = new TextView(KOT_Management.this);
            cx.setText(assa1);
            String tablen = "";
            if (cx.getText().toString().equals("")) {
                tablen = "Tab" + assa2;
                allbuf11 = new byte[][]{
                        left, un1, setHT321, tablen.getBytes(), LF
                };
            } else {
                tablen = "Tab" + assa1;
                allbuf11 = new byte[][]{
                        left, un1, setHT321, tablen.getBytes(), LF
                };
            }

//        String tablen = "Table"+ItemIDtable;
//        allbuf11 = new byte[][]{
//                left,un1,setHT321, tablen.getBytes(),LF
//        };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(left);
                mPrinter.addCommand(un1);
                mPrinter.addCommand(setHT321);
                StringBuilder textData1 = new StringBuilder();
                textData1.append(tablen);
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(LF); //LF
            }

            SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyyyy");
            String normal1 = normal2.format(new Date());

            Date dt = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
            String time1 = sdf1.format(dt);

            Date dtt = new Date();
            SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
            String time24 = sdf1t.format(dtt);

            Cursor cu_da = db1.rawQuery("SELECT * from Table" + ItemIDtable + "management WHERE tagg = '" + kot + "'", null);
            if (cu_da.moveToFirst()) {
                normal1 = cu_da.getString(4);
                time1 = cu_da.getString(5);
            }


            allbuf10 = new byte[][]{
                    setHT321, left, normal1.getBytes(), HT, "   ".getBytes(), time1.getBytes(), LF
            };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(setHT321);
                mPrinter.addCommand(left);
                StringBuilder textData1 = new StringBuilder();
                textData1.append(normal1);
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(HT);
                StringBuilder textData2 = new StringBuilder();
                textData2.append("   ");
                mPrinter.addText(textData2.toString());
                StringBuilder textData3 = new StringBuilder();
                textData3.append(time1);
                mPrinter.addText(textData3.toString());
                mPrinter.addCommand(LF); //LF
            }

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(left);
                mPrinter.addCommand(un1);
                StringBuilder textData1 = new StringBuilder();
                textData1.append(str_line);
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(LF); //LF
            }

            allbufqty = new byte[][]{
                    setHTKOT, normal, "Qty".getBytes(), HT, "Item".getBytes(), left, LF
            };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(setHTKOT);
                mPrinter.addCommand(normal);
                StringBuilder textData1 = new StringBuilder();
                textData1.append("Qty");
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(HT);
                StringBuilder textData2 = new StringBuilder();
                textData2.append("Item");
                mPrinter.addText(textData2.toString());
                mPrinter.addCommand(left); //LF
                mPrinter.addCommand(LF); //LF
            }

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(left);
                mPrinter.addCommand(un1);
                StringBuilder textData1 = new StringBuilder();
                textData1.append(str_line);
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(LF); //LF
            }


            Cursor cursor = db1.rawQuery("Select * from Table" + ItemIDtable + "management WHERE tagg = '" + kot + "' AND itemtype = 'Item' GROUP BY par_id", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (cursor.moveToFirst()) {
                do {
                    String Itemtype = cursor.getString(7);
                    String total = cursor.getString(2);
                    final String idid = cursor.getString(6);
                    String name = cursor.getString(1);


                    if (Itemtype.toString().equals("Item")) {

                        Cursor modcursorc = db1.rawQuery("Select * from Table" + ItemIDtable + "management WHERE par_id = '" + idid + "' AND itemtype = 'Modifier' GROUP BY itemname, par_id, tagg", null);
                        if (modcursorc.moveToFirst()) {

                            if (name.toString().length() > charlength) {
                                String str1 = name.substring(0, charlength);
                                String str2 = name.substring(charlength);
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "         ".getBytes(), str2.getBytes(), left, LF,
                                };
                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                    mPrinter.addCommand(setHTKOT);
                                    mPrinter.addCommand(normal);
                                    StringBuilder textData1 = new StringBuilder();
                                    textData1.append(total);
                                    mPrinter.addText(textData1.toString());
                                    mPrinter.addCommand(HT);
                                    StringBuilder textData2 = new StringBuilder();
                                    textData2.append(str1);
                                    mPrinter.addText(textData2.toString());
                                    mPrinter.addCommand(LF);
                                    StringBuilder textData3 = new StringBuilder();
                                    textData3.append("         ");
                                    mPrinter.addText(textData3.toString());
                                    StringBuilder textData4 = new StringBuilder();
                                    textData4.append(str2);
                                    mPrinter.addText(textData4.toString());
                                    mPrinter.addCommand(left); //LF
                                    mPrinter.addCommand(LF); //LF
                                }
                            } else {
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                };
                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                    mPrinter.addCommand(setHTKOT);
                                    mPrinter.addCommand(normal);
                                    StringBuilder textData1 = new StringBuilder();
                                    textData1.append(total);
                                    mPrinter.addText(textData1.toString());
                                    mPrinter.addCommand(HT);
                                    StringBuilder textData2 = new StringBuilder();
                                    textData2.append(name);
                                    mPrinter.addText(textData2.toString());
                                    mPrinter.addCommand(left); //LF
                                    mPrinter.addCommand(LF); //LF
                                }
                            }


                            do {
                                final String modiname = modcursorc.getString(1);
                                String mod = modiname;


                                if (mod.toString().length() > charlength) {
                                    String str1 = mod.substring(0, charlength);
                                    String str2 = mod.substring(charlength);
                                    allbufmodifiers = new byte[][]{
                                            setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), str1.getBytes(), LF, "         ".getBytes(), str2.getBytes(), left, LF,
                                    };
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        mPrinter.addCommand(setHTKOT);
                                        mPrinter.addCommand(normal);
                                        StringBuilder textData1 = new StringBuilder();
                                        textData1.append("");
                                        mPrinter.addText(textData1.toString());
                                        mPrinter.addCommand(HT);
                                        StringBuilder textData2 = new StringBuilder();
                                        textData2.append(">");
                                        mPrinter.addText(textData2.toString());
                                        StringBuilder textData3 = new StringBuilder();
                                        textData3.append(str1);
                                        mPrinter.addText(textData3.toString());
                                        mPrinter.addCommand(LF);
                                        StringBuilder textData4 = new StringBuilder();
                                        textData4.append("         ");
                                        mPrinter.addText(textData4.toString());
                                        StringBuilder textData5 = new StringBuilder();
                                        textData5.append(str2);
                                        mPrinter.addText(textData5.toString());
                                        mPrinter.addCommand(left); //LF
                                        mPrinter.addCommand(LF); //LF
                                    }
                                } else {
                                    allbufmodifiers = new byte[][]{
                                            setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), mod.getBytes(), left, LF
                                    };
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        mPrinter.addCommand(setHTKOT);
                                        mPrinter.addCommand(normal);
                                        StringBuilder textData1 = new StringBuilder();
                                        textData1.append("");
                                        mPrinter.addText(textData1.toString());
                                        mPrinter.addCommand(HT);
                                        StringBuilder textData2 = new StringBuilder();
                                        textData2.append(">");
                                        mPrinter.addText(textData2.toString());
                                        StringBuilder textData3 = new StringBuilder();
                                        textData3.append(mod);
                                        mPrinter.addText(textData3.toString());
                                        mPrinter.addCommand(left); //LF
                                        mPrinter.addCommand(LF); //LF
                                    }
                                }


                            } while (modcursorc.moveToNext());
                        } else {
                            if (name.toString().length() > charlength) {
                                String str1 = name.substring(0, charlength);
                                String str2 = name.substring(charlength);
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "         ".getBytes(), str2.getBytes(), left, LF,
                                };
                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                    mPrinter.addCommand(setHTKOT);
                                    mPrinter.addCommand(normal);
                                    StringBuilder textData1 = new StringBuilder();
                                    textData1.append(total);
                                    mPrinter.addText(textData1.toString());
                                    mPrinter.addCommand(HT);
                                    StringBuilder textData2 = new StringBuilder();
                                    textData2.append(str1);
                                    mPrinter.addText(textData2.toString());
                                    mPrinter.addCommand(LF);
                                    StringBuilder textData3 = new StringBuilder();
                                    textData3.append("         ");
                                    mPrinter.addText(textData3.toString());
                                    StringBuilder textData4 = new StringBuilder();
                                    textData4.append(str2);
                                    mPrinter.addText(textData4.toString());
                                    mPrinter.addCommand(left); //LF
                                    mPrinter.addCommand(LF); //LF
                                }
                            } else {
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                };
                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                    mPrinter.addCommand(setHTKOT);
                                    mPrinter.addCommand(normal);
                                    StringBuilder textData1 = new StringBuilder();
                                    textData1.append(total);
                                    mPrinter.addText(textData1.toString());
                                    mPrinter.addCommand(HT);
                                    StringBuilder textData2 = new StringBuilder();
                                    textData2.append(name);
                                    mPrinter.addText(textData2.toString());
                                    mPrinter.addCommand(left); //LF
                                    mPrinter.addCommand(LF); //LF
                                }
                            }

                        }
                    }


                } while (cursor.moveToNext());
            }

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(left);
                mPrinter.addCommand(un1);
                StringBuilder textData1 = new StringBuilder();
                textData1.append(str_line);
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(LF); //LF
            }
            //feedcut();

            totalquanret1 = "0";
            totalquanret2 = "0";

            Cursor toalitems = db1.rawQuery("Select * from Table" + ItemIDtable + "management WHERE tagg = '" + kot + "'", null);
            if (toalitems.moveToFirst()) {
                Cursor toalitems2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity <= '1.0'", null);
                if (toalitems2.moveToFirst()) {
                    int toalzx = toalitems2.getCount();
                    totalquanret1 = String.valueOf(toalzx);
                }
                Cursor toalitems1 = db1.rawQuery("Select sum(quantity) from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity > '1.0'", null);
                if (toalitems1.moveToFirst()) {
                    int toalzx = toalitems1.getInt(0);
                    totalquanret2 = String.valueOf(toalzx);
                }
                int cvvc = Integer.parseInt(totalquanret1) + Integer.parseInt(totalquanret2);
                String total = String.valueOf(cvvc);
                String totalqu = "No. of items : " + total;
                allbuf11 = new byte[][]{
                        left, setHT321, totalqu.getBytes(), LF
                };
                if (textViewstatussusbs.getText().toString().equals("ok")) {
                    mPrinter.addCommand(left);
                    mPrinter.addCommand(setHT321);
                    StringBuilder textData1 = new StringBuilder();
                    textData1.append(totalqu);
                    mPrinter.addText(textData1.toString());
                    mPrinter.addCommand(LF); //LF
                }
            }


            byte[][] allbuf = new byte[][]{
                    feedcut2
            };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(feedcut2);
            }

            method = "addPageEnd";
            mPrinter.addPageEnd();

        } catch (Exception e) {
//            Toast.makeText(KOT_Management.this, "Here2", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, method, mContext);
            return false;
        }

        return true;
    }

    private boolean createCouponData() {

        byte[] setHT34M = {0x1b,0x44,0x04,0x11,0x19,0x00};
        byte[] dotfeed = {0x1b,0x4a,0x10};
        byte[] HTRight = {0x1b,0x61, 0x02,0x09};
        byte[] HT = {0x09};
        byte[] LF = {0x0d,0x0a};

        byte[] left = {0x1b,0x61, 0x00};
        byte[] cen = {0x1b,0x61, 0x01};
        byte[] right = {0x1b,0x61, 0x02};
        byte[] bold = {0x1B,0x21,0x10};
        byte[] normal = {0x1d, 0x21, 0x00};
        byte[] horiz1 = {0x1b,0x44,0x19, 0x19, 0x00};
        byte[] horiz = {0x1b,0x44, 0x04, 0x06, 0x04, 0x04, 0x0A, 0x00, 0x09, 0x30, 0x09, 0x31, 0x09, 0x32, 0x09, 0x33, 0x09, 0x34, 0x0d,0x0a};

        byte[] un = {0x1b, 0x2d, 0x02};
        byte[] un1 = {0x1b, 0x2d, 0x00};
        String str_line = "";

        Cursor getprint_type = db.rawQuery("SELECT * FROM Printer_text_size", null);
        if (getprint_type.moveToFirst()) {
            String type = getprint_type.getString(1);

            Cursor cc = db.rawQuery("SELECT * FROM Printerreceiptsize", null);

            if (cc.moveToFirst()) {
                cc.moveToFirst();
                do {
                    NAME = cc.getString(1);
                    if (NAME.equals("3 inch")) {
                        setHT32 = new byte[]{0x1b, 0x44, 0x29, 0x00};//2 tabs 3"
                        setHT321 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 3"
                        setHT3212 = new byte[]{0x1b, 0x44, 0x25, 0x00};//2 tabs 3"
                        setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                        setHT34 = new byte[]{0x1b, 0x44, 0x08, 0x20, 0x29, 0x00};//4 tabs 3"
                        setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 3"
                        feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                        nPaperWidth = 576;
                        charlength = 41;
                        str_line = "------------------------------------------------";
                        allbufline = new byte[][]{
                                left, un1, "------------------------------------------------".getBytes(), LF

                        };
                    } else {

                        Cursor print_ty = db.rawQuery("SELECT * FROM Printer_type", null);
                        if (print_ty.moveToFirst()){
                            str_print_ty = print_ty.getString(1);
                        }
                        if (str_print_ty.toString().equals("Generic")) {
//                            Toast.makeText(KOT_Management.this, "phi", Toast.LENGTH_SHORT).show();
                            setHT32 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 2"
                            setHT321 = new byte[]{0x1b, 0x44, 0x13, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x15, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                            setHT34 = new byte[]{0x1b, 0x44, 0x04, 0x12, 0x19, 0x00};//4 tabs 2"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 2"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                            nPaperWidth = 384;
                            charlength = 25;
                            str_line = "--------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "--------------------------------".getBytes(), LF
                            };
                        }else {
//                            Toast.makeText(KOT_Management.this, "epson", Toast.LENGTH_SHORT).show();
                            setHT32 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 2"
                            setHT321 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x21, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                            setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x19, 0x21, 0x00};//4 tabs 2"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                            nPaperWidth = 384;
                            charlength = 28;
                            str_line = "------------------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "------------------------------------------".getBytes(), LF
                            };
                        }
                    }
                } while (cc.moveToNext());
            }
            cc.close();
        }
        getprint_type.close();

        final int pageAreaHeight = 384;
        final int pageAreaWidth = 384;


        ArrayList<byte[]> list = new ArrayList<byte[]>();
        String method = "";
        String[] col = {"companylogo"};
        Cursor c = db.query("Logo", col, null, null, null, null, null);
        if (c.moveToFirst()) {
            byte[] img = c.getBlob(c.getColumnIndex("companylogo"));
//            logoData = BitmapFactory.decodeByteArray(img, 0, img.length);
        }
        c.close();

        if (mPrinter == null) {
            return false;
        }
        try {
            method = "addPageArea";
            mPrinter.addPageArea(0, 0, nPaperWidth, pageAreaHeight);

            method = "addPageDirection";
            mPrinter.addPageDirection(Printer.DIRECTION_TOP_TO_BOTTOM);

            method = "addPagePosition";
            mPrinter.addTextAlign(Printer.ALIGN_LEFT);
            method = "addImage";

            Cursor connnet = db.rawQuery("SELECT * FROM IPConn", null);
            if (connnet.moveToFirst()) {
                ipnamegets = connnet.getString(1);
                portgets = connnet.getString(2);
                statusnets = connnet.getString(3);
            }

            Cursor connnet_counter = db.rawQuery("SELECT * FROM IPConn_Counter", null);
            if (connnet_counter.moveToFirst()) {
                ipnamegets_counter = connnet_counter.getString(1);
                portgets_counter = connnet_counter.getString(2);
                statusnets_counter = connnet_counter.getString(3);
            }
            connnet_counter.close();

            Cursor connusb = db.rawQuery("SELECT * FROM BTConn", null);
            if (connusb.moveToFirst()) {
                addgets = connusb.getString(1);
                namegets = connusb.getString(2);
                statussusbs = connusb.getString(3);
            }

            textViewstatusnets.setText(statusnet);
            textViewstatusnets_counter.setText(statusnet_counter);
            textViewstatussusbs.setText(statussusbs);

            Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
            if (getcom.moveToFirst()) {
                do {
                    strcompanyname = getcom.getString(1);
                } while (getcom.moveToNext());
            }


            tvkot.setText(strcompanyname);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf1 = new byte[][]{
                        bold, cen, strcompanyname.getBytes(), LF, LF

                };
                if (textViewstatussusbs.getText().toString().equals("ok")) {
                    mPrinter.addCommand(bold);
                    mPrinter.addCommand(cen);
                    StringBuilder textData1 = new StringBuilder();
                    textData1.append(strcompanyname);
                    mPrinter.addText(textData1.toString());
                    mPrinter.addCommand(LF);
                    mPrinter.addCommand(LF); //LF
                }
            }


            allbufKOT = new byte[][]{
                    un, cen, "Order Ticket copy".getBytes(), LF
            };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(un);
                mPrinter.addCommand(cen);
                StringBuilder textData1 = new StringBuilder();
                textData1.append("Order Ticket copy");
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(LF); //LF
            }


            Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
            if (vbnm.moveToFirst()) {
                assa1 = vbnm.getString(1);
                assa2 = vbnm.getString(2);
            }
            TextView cx = new TextView(KOT_Management.this);
            cx.setText(assa1);
            String tablen = "";
            if (cx.getText().toString().equals("")) {
                tablen = "Tab" + assa2;
                allbuf11 = new byte[][]{
                        left, un1, setHT321, tablen.getBytes(), LF
                };
            } else {
                tablen = "Tab" + assa1;
                allbuf11 = new byte[][]{
                        left, un1, setHT321, tablen.getBytes(), LF
                };
            }

//        String tablen = "Table"+ItemIDtable;
//        allbuf11 = new byte[][]{
//                left,un1,setHT321, tablen.getBytes(),LF
//        };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(left);
                mPrinter.addCommand(un1);
                mPrinter.addCommand(setHT321);
                StringBuilder textData1 = new StringBuilder();
                textData1.append(tablen);
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(LF); //LF
            }

            SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyyyy");
            final String normal1 = normal2.format(new Date());

            Date dt = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
            final String time1 = sdf1.format(dt);

            Date dtt = new Date();
            SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
            String time24 = sdf1t.format(dtt);

            allbuf10 = new byte[][]{
                    setHT321, left, normal1.getBytes(), HT, "   ".getBytes(), time1.getBytes(), LF
            };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(setHT321);
                mPrinter.addCommand(left);
                StringBuilder textData1 = new StringBuilder();
                textData1.append(normal1);
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(HT);
                StringBuilder textData2 = new StringBuilder();
                textData2.append("   ");
                mPrinter.addText(textData2.toString());
                StringBuilder textData3 = new StringBuilder();
                textData3.append(time1);
                mPrinter.addText(textData3.toString());
                mPrinter.addCommand(LF); //LF
            }

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(left);
                mPrinter.addCommand(un1);
                StringBuilder textData1 = new StringBuilder();
                textData1.append(str_line);
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(LF); //LF
            }

            allbufqty = new byte[][]{
                    setHTKOT, normal, "Qty".getBytes(), HT, "Item".getBytes(), left, LF
            };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(setHTKOT);
                mPrinter.addCommand(normal);
                StringBuilder textData1 = new StringBuilder();
                textData1.append("Qty");
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(HT);
                StringBuilder textData2 = new StringBuilder();
                textData2.append("Item");
                mPrinter.addText(textData2.toString());
                mPrinter.addCommand(left); //LF
                mPrinter.addCommand(LF); //LF
            }

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(left);
                mPrinter.addCommand(un1);
                StringBuilder textData1 = new StringBuilder();
                textData1.append(str_line);
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(LF); //LF
            }


            Cursor cursor = db1.rawQuery("Select * from Table" + ItemIDtable, null);//replace to cursor = dbHelper.fetchAllHotels();
            if (cursor.moveToFirst()) {
                do {
                    String Itemtype = cursor.getString(5);
                    String total = cursor.getString(1);
                    final String idid = cursor.getString(0);
                    String name = cursor.getString(2);
                    final String iidd = cursor.getString(0);
                    final String hii = cursor.getString(2);
                    final String newtt = cursor.getString(4);
                    final float f = Float.parseFloat(cursor.getString(3));
                    String price = String.valueOf(f);
                    if (Itemtype.toString().equals("Item")) {

                        Cursor modcursorc = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE parent = '" + hii + "' AND parentid = '" + idid + "'  ", null);
                        if (modcursorc.moveToFirst()) {
                            Cursor modt = db1.rawQuery("Select SUM(total) from Table" + ItemIDtable + " WHERE parent = '" + name + "' AND parentid = '" + idid + "' ", null);
                            if (modt.moveToFirst()) {
                                do {
                                    //row.removeView(tv3);
                                    float aq = modt.getFloat(0);
                                    aqq = String.valueOf(aq);
                                    aqq1 = Float.parseFloat(aqq) + Float.parseFloat(newtt);
                                    aqq2 = String.valueOf(aqq1);
                                } while (modt.moveToNext());
                            }

                            if (name.toString().length() > charlength) {
                                String str1 = name.substring(0, charlength);
                                String str2 = name.substring(charlength);
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "         ".getBytes(), str2.getBytes(), left, LF,
                                };
                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                    mPrinter.addCommand(setHTKOT);
                                    mPrinter.addCommand(normal);
                                    StringBuilder textData1 = new StringBuilder();
                                    textData1.append(total);
                                    mPrinter.addText(textData1.toString());
                                    mPrinter.addCommand(HT);
                                    StringBuilder textData2 = new StringBuilder();
                                    textData2.append(str1);
                                    mPrinter.addText(textData2.toString());
                                    mPrinter.addCommand(LF);
                                    StringBuilder textData3 = new StringBuilder();
                                    textData3.append("         ");
                                    mPrinter.addText(textData3.toString());
                                    StringBuilder textData4 = new StringBuilder();
                                    textData4.append(str2);
                                    mPrinter.addText(textData4.toString());
                                    mPrinter.addCommand(left); //LF
                                    mPrinter.addCommand(LF); //LF
                                }
                            } else {
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                };
                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                    mPrinter.addCommand(setHTKOT);
                                    mPrinter.addCommand(normal);
                                    StringBuilder textData1 = new StringBuilder();
                                    textData1.append(total);
                                    mPrinter.addText(textData1.toString());
                                    mPrinter.addCommand(HT);
                                    StringBuilder textData2 = new StringBuilder();
                                    textData2.append(name);
                                    mPrinter.addText(textData2.toString());
                                    mPrinter.addCommand(left); //LF
                                    mPrinter.addCommand(LF); //LF
                                }
                            }

                            do {
                                final String modiname = modcursorc.getString(2);
                                final String modiquan = modcursorc.getString(1);
                                String modiprice = modcursorc.getString(3);
                                String moditotal = modcursorc.getString(4);
                                final String modiid = modcursorc.getString(0);
                                String mod = modiname;


                                if (mod.toString().length() > charlength) {
                                    String str1 = mod.substring(0, charlength);
                                    String str2 = mod.substring(charlength);
                                    allbufmodifiers = new byte[][]{
                                            setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), str1.getBytes(), LF, "         ".getBytes(), str2.getBytes(), left, LF,
                                    };
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        mPrinter.addCommand(setHTKOT);
                                        mPrinter.addCommand(normal);
                                        StringBuilder textData1 = new StringBuilder();
                                        textData1.append("");
                                        mPrinter.addText(textData1.toString());
                                        mPrinter.addCommand(HT);
                                        StringBuilder textData2 = new StringBuilder();
                                        textData2.append(">");
                                        mPrinter.addText(textData2.toString());
                                        StringBuilder textData3 = new StringBuilder();
                                        textData3.append(str1);
                                        mPrinter.addText(textData3.toString());
                                        mPrinter.addCommand(LF);
                                        StringBuilder textData4 = new StringBuilder();
                                        textData4.append("         ");
                                        mPrinter.addText(textData4.toString());
                                        StringBuilder textData5 = new StringBuilder();
                                        textData5.append(str2);
                                        mPrinter.addText(textData5.toString());
                                        mPrinter.addCommand(left); //LF
                                        mPrinter.addCommand(LF); //LF
                                    }
                                } else {
                                    allbufmodifiers = new byte[][]{
                                            setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), mod.getBytes(), left, LF
                                    };
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        mPrinter.addCommand(setHTKOT);
                                        mPrinter.addCommand(normal);
                                        StringBuilder textData1 = new StringBuilder();
                                        textData1.append("");
                                        mPrinter.addText(textData1.toString());
                                        mPrinter.addCommand(HT);
                                        StringBuilder textData2 = new StringBuilder();
                                        textData2.append(">");
                                        mPrinter.addText(textData2.toString());
                                        StringBuilder textData3 = new StringBuilder();
                                        textData3.append(mod);
                                        mPrinter.addText(textData3.toString());
                                        mPrinter.addCommand(left); //LF
                                        mPrinter.addCommand(LF); //LF
                                    }
                                }
                            } while (modcursorc.moveToNext());
                        } else {
                            if (name.toString().length() > charlength) {
                                String str1 = name.substring(0, charlength);
                                String str2 = name.substring(charlength);
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "         ".getBytes(), str2.getBytes(), left, LF,
                                };
                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                    mPrinter.addCommand(setHTKOT);
                                    mPrinter.addCommand(normal);
                                    StringBuilder textData1 = new StringBuilder();
                                    textData1.append(total);
                                    mPrinter.addText(textData1.toString());
                                    mPrinter.addCommand(HT);
                                    StringBuilder textData2 = new StringBuilder();
                                    textData2.append(str1);
                                    mPrinter.addText(textData2.toString());
                                    mPrinter.addCommand(LF);
                                    StringBuilder textData3 = new StringBuilder();
                                    textData3.append("         ");
                                    mPrinter.addText(textData3.toString());
                                    StringBuilder textData4 = new StringBuilder();
                                    textData4.append(str2);
                                    mPrinter.addText(textData4.toString());
                                    mPrinter.addCommand(left); //LF
                                    mPrinter.addCommand(LF); //LF
                                }
                            } else {
                                allbufitems = new byte[][]{
                                        setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                };
                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                    mPrinter.addCommand(setHTKOT);
                                    mPrinter.addCommand(normal);
                                    StringBuilder textData1 = new StringBuilder();
                                    textData1.append(total);
                                    mPrinter.addText(textData1.toString());
                                    mPrinter.addCommand(HT);
                                    StringBuilder textData2 = new StringBuilder();
                                    textData2.append(name);
                                    mPrinter.addText(textData2.toString());
                                    mPrinter.addCommand(left); //LF
                                    mPrinter.addCommand(LF); //LF
                                }
                            }
                        }
                    }

                } while (cursor.moveToNext());
            }

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(left);
                mPrinter.addCommand(un1);
                StringBuilder textData1 = new StringBuilder();
                textData1.append(str_line);
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(LF); //LF
            }
            //feedcut();

            Cursor toalitems = db1.rawQuery("Select * from Table" + ItemIDtable, null);
            if (toalitems.moveToFirst()) {
                Cursor toalitems2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity <= '1.0'", null);
                if (toalitems2.moveToFirst()) {
                    int toalzx = toalitems2.getCount();
                    totalquanret1 = String.valueOf(toalzx);
                }
                Cursor toalitems1 = db1.rawQuery("Select sum(quantity) from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity > '1.0'", null);
                if (toalitems1.moveToFirst()) {
                    int toalzx = toalitems1.getInt(0);
                    totalquanret2 = String.valueOf(toalzx);
                }
                int cvvc = Integer.parseInt(totalquanret1) + Integer.parseInt(totalquanret2);
                String total = String.valueOf(cvvc);
                String totalqu = "No. of items : " + total;
                allbuf11 = new byte[][]{
                        left, setHT321, totalqu.getBytes(), LF
                };
                if (textViewstatussusbs.getText().toString().equals("ok")) {
                    mPrinter.addCommand(left);
                    mPrinter.addCommand(setHT321);
                    StringBuilder textData1 = new StringBuilder();
                    textData1.append(totalqu);
                    mPrinter.addText(textData1.toString());
                    mPrinter.addCommand(LF); //LF
                }
            }


            byte[][] allbuf = new byte[][]{
                    feedcut2
            };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(feedcut2);
            }

            method = "addPageEnd";
            mPrinter.addPageEnd();

        } catch (Exception e) {
//            Toast.makeText(KOT_Management.this, "Here2", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, method, mContext);
            return false;
        }

        return true;
    }

    public void kotcanceldata(String hii, String itemname, String qty, String ItemIDtable, String reason, String p_price, String tot1) {

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy MM dd");
        final String date = sdf2.format(new Date());

        SimpleDateFormat normal = new SimpleDateFormat("dd MMM yyyy");
        final String date1 = normal.format(new Date());

        Date dt = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
        final String time1 = sdf1.format(dt);

        Date dtt_new = new Date();
        SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");
        String time24_new = sdf1t_new.format(dtt_new);

        String u_name = "", u_username = "", username = "", password = "";
        Cursor cursor_user = db.rawQuery("SELECT * FROM LoginUser", null);
        if (cursor_user.moveToFirst()) {
            u_username = cursor_user.getString(1);
            username = cursor_user.getString(1);
            password = cursor_user.getString(2);
            Cursor ladmin = db.rawQuery("SELECT * FROM LAdmin WHERE username = '" + u_username + "'", null);
            if (ladmin.moveToFirst()) {
                u_name = ladmin.getString(3);
            } else {
                Cursor madmin = db.rawQuery("SELECT * FROM LOGIN WHERE username = '" + u_username + "'", null);
                if (madmin.moveToFirst()) {
                    u_name = madmin.getString(3);
                } else {
                    Cursor user1 = db.rawQuery("SELECT * FROM User1 WHERE username = '" + u_username + "'", null);
                    if (user1.moveToFirst()) {
                        u_name = user1.getString(1);
                    } else {
                        Cursor user2 = db.rawQuery("SELECT * FROM User2 WHERE username = '" + u_username + "'", null);
                        if (user2.moveToFirst()) {
                            u_name = user2.getString(1);
                        } else {
                            Cursor user3 = db.rawQuery("SELECT * FROM User3 WHERE username = '" + u_username + "'", null);
                            if (user3.moveToFirst()) {
                                u_name = user3.getString(1);
                            } else {
                                Cursor user4 = db.rawQuery("SELECT * FROM User4 WHERE username = '" + u_username + "'", null);
                                if (user4.moveToFirst()) {
                                    u_name = user4.getString(1);
                                } else {
                                    Cursor user5 = db.rawQuery("SELECT * FROM User5 WHERE username = '" + u_username + "'", null);
                                    if (user5.moveToFirst()) {
                                        u_name = user5.getString(1);
                                    } else {
                                        Cursor user6 = db.rawQuery("SELECT * FROM User6 WHERE username = '" + u_username + "'", null);
                                        if (user6.moveToFirst()) {
                                            u_name = user6.getString(1);
                                        } else {

                                        }
                                        user6.close();
                                    }
                                    user5.close();
                                }
                                user4.close();
                            }
                            user3.close();
                        }
                        user2.close();
                    }
                    user1.close();
                }
                madmin.close();
            }
            ladmin.close();
        }
        cursor_user.close();

        String category = "", itemtype = "";
        Cursor cursor = db1.rawQuery("SELECT * FROM Table" + ItemIDtable + " WHERE _id = '"+hii+"'", null);
        if (cursor.moveToFirst()) {
            category = cursor.getString(29);
            itemtype = cursor.getString(5);
        }

        String kot_no = "0";
        Cursor cursor2 = db1.rawQuery("SELECT * FROM Kotcancelled", null);
        if (cursor2.moveToLast()) {
            kot_no = cursor2.getString(11);
        }

        int kot_no1 = Integer.parseInt(kot_no);
        int kot_no2 = kot_no1+1;



        ContentValues contentValues = new ContentValues();
        contentValues.put("itemname", itemname);
        contentValues.put("qty", qty);
        contentValues.put("tableid", ItemIDtable);
        contentValues.put("reason", reason);
        contentValues.put("date", date);
        contentValues.put("date1", date1);
        contentValues.put("time", time1);
        contentValues.put("datetimee_new", time24_new);
        contentValues.put("category", category);
        contentValues.put("user", u_name);
        contentValues.put("kot_no", String.valueOf(kot_no2));
        contentValues.put("itemtype", itemtype);
        contentValues.put("price", p_price);
        contentValues.put("total", tot1);
        db1.insert("Kotcancelled", null, contentValues);

        Cursor getprint_type = db.rawQuery("SELECT * FROM Printer_text_size", null);
        if (getprint_type.moveToFirst()) {
            String type = getprint_type.getString(1);

            if (type.toString().equals("Standard")) {
//                Toast.makeText(, "selected Standard", Toast.LENGTH_SHORT).show();
                byte[] LF = {0x0d, 0x0a};

                allbuftaxestype1 = new byte[][]{
                        " ".getBytes(), LF
                };

            } else {
//                Toast.makeText(getActivity(), "selected Compact", Toast.LENGTH_SHORT).show();
                byte[] LF = {0x0d, 0x0a};

                allbuftaxestype1 = new byte[][]{
                        " ".getBytes(), LF
                };
            }
        }
//                }


        Cursor connnet = db.rawQuery("SELECT * FROM IPConn", null);
        if (connnet.moveToFirst()) {
            ipnameget = connnet.getString(1);
            portget = connnet.getString(2);
            statusnet = connnet.getString(3);
        }

        Cursor connnet_counter = db.rawQuery("SELECT * FROM IPConn_Counter", null);
        if (connnet_counter.moveToFirst()) {
            ipnameget_counter = connnet_counter.getString(1);
            portget_counter = connnet_counter.getString(2);
            statusnet_counter = connnet_counter.getString(3);
        }
        connnet_counter.close();

        Cursor conn = db.rawQuery("SELECT * FROM BTConn", null);
        if (conn.moveToFirst()) {
            nameget = conn.getString(1);
            addget = conn.getString(2);
            statussusb = conn.getString(3);
        }



        Cursor c_kot1 = db.rawQuery("SELECT * FROM IPConn_KOT1", null);
        if (c_kot1.moveToFirst()) {
            ipnamegets_kot1 = c_kot1.getString(1);
            portgets_kot1 = c_kot1.getString(2);
            statusnets_kot1 = c_kot1.getString(3);
            name_kot1 = c_kot1.getString(5);
        }
        c_kot1.close();

        Cursor c_kot2 = db.rawQuery("SELECT * FROM IPConn_KOT2", null);
        if (c_kot2.moveToFirst()) {
            ipnamegets_kot2 = c_kot2.getString(1);
            portgets_kot2 = c_kot2.getString(2);
            statusnets_kot2 = c_kot2.getString(3);
            name_kot2 = c_kot2.getString(5);
        }
        c_kot2.close();

        Cursor c_kot3 = db.rawQuery("SELECT * FROM IPConn_KOT3", null);
        if (c_kot3.moveToFirst()) {
            ipnamegets_kot3 = c_kot3.getString(1);
            portgets_kot3 = c_kot3.getString(2);
            statusnets_kot3 = c_kot3.getString(3);
            name_kot3 = c_kot3.getString(5);
        }
        c_kot3.close();

        Cursor c_kot4 = db.rawQuery("SELECT * FROM IPConn_KOT4", null);
        if (c_kot4.moveToFirst()) {
            ipnamegets_kot4 = c_kot4.getString(1);
            portgets_kot4 = c_kot4.getString(2);
            statusnets_kot4 = c_kot4.getString(3);
            name_kot4 = c_kot4.getString(5);
        }
        c_kot4.close();

        if (statusnet.toString().equals("ok") || statusnet_counter.toString().equals("ok") || statussusb.toString().equals("ok")
                || statusnets_kot1.toString().equals("ok") || statusnets_kot2.toString().equals("ok")
                || statusnets_kot3.toString().equals("ok") || statusnets_kot4.toString().equals("ok")) {
            printbill_kotcancel(String.valueOf(kot_no2), date1, time1, reason);
        }else {
            String printer_type="";
            Cursor aallrows = db.rawQuery("SELECT * FROM Printer_type WHERE _id = '1'", null);
            if (aallrows.moveToFirst()) {
                do {
                    printer_type = aallrows.getString(1);

                } while (aallrows.moveToNext());
            }
            aallrows.close();

            if(printer_type.equalsIgnoreCase("wiseposplus")) {

                if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO)
                {
                    mPrintData = new ArrayList<>();
                    byte[] receiptData = neoprintbill_kotcancel(String.valueOf(kot_no2), date1, time1, reason);
                    mPrintData.add(receiptData);


                    if(mPrintData != null && mPrintData.size() > 0)
                    {
                        mMSWisepadDeviceController.print(mPrintData);
                    }

                }else{

                    wiseposprintbill_kotcancel(String.valueOf(kot_no2), date1, time1, reason);
                    Handler handler =  new Handler(KOT_Management.this.getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(KOT_Management.this, "KOT printed",Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }

    }

    public void printbill_kotcancel(String kot_no2, String date1, String time1, String reason) {
        byte[] setHT34M = {0x1b,0x44,0x04,0x11,0x19,0x00};
        byte[] dotfeed = {0x1b,0x4a,0x10};
        byte[] HTRight = {0x1b,0x61, 0x02,0x09};
        byte[] HT = {0x09};
        byte[] HT1 = {0x09};
        byte[] LF = {0x0d,0x0a};

        byte[] left = {0x1b,0x61, 0x00};
        byte[] cen = {0x1b,0x61, 0x01};
        byte[] right = {0x1b,0x61, 0x02};
        byte[] bold = {0x1B,0x21,0x10};
        byte[] normal = {0x1d, 0x21, 0x00};
        byte[] horiz1 = {0x1b,0x44,0x19, 0x19, 0x00};
        byte[] horiz = {0x1b,0x44, 0x04, 0x06, 0x04, 0x04, 0x0A, 0x00, 0x09, 0x30, 0x09, 0x31, 0x09, 0x32, 0x09, 0x33, 0x09, 0x34, 0x0d,0x0a};

        byte[] un = {0x1b, 0x2d, 0x02};
        byte[] un1 = {0x1b, 0x2d, 0x00};
        String str_line = "";

        Cursor print_ty = db.rawQuery("SELECT * FROM Printer_type", null);
        if (print_ty.moveToFirst()){
            str_print_ty = print_ty.getString(1);
        }
        print_ty.close();

        Cursor getprint_type = db.rawQuery("SELECT * FROM Printer_text_size", null);
        if (getprint_type.moveToFirst()) {
            String type = getprint_type.getString(1);

            Cursor cc = db.rawQuery("SELECT * FROM Printerreceiptsize", null);

            if (cc.moveToFirst()) {
                cc.moveToFirst();
                do {
                    NAME = cc.getString(1);
                    if (NAME.equals("3 inch")) {
                        if (str_print_ty.toString().equals("Generic") || str_print_ty.toString().equals("Epson/others")) {
                            setHT32 = new byte[]{0x1b, 0x44, 0x29, 0x00};//2 tabs 3"
                            setHT321 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x25, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                            setHT34 = new byte[]{0x1b, 0x44, 0x08, 0x20, 0x29, 0x00};//4 tabs 3"
                            setHTKOT = new byte[]{0x1b, 0x06, 0x44, 0x00};//2 tabs 3"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                            nPaperWidth = 576;
                            charlength = 41;
                            HT1 = new byte[]{0x09};
                            str_line = "------------------------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "------------------------------------------------".getBytes(), LF

                            };
                        }else {
                            if (str_print_ty.toString().equals("POS")) {
                                setHT32 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT321 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x10, 0x15, 0x00};//4 tabs 3"
                                setHTKOT = new byte[]{0x1b, 0x05, 0x44, 0x00};//2 tabs 3"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                nPaperWidth = 576;
                                charlength = 23;
                                charlength1 = 46;
                                charlength2 = 69;
                                quanlentha = 4;
                                HT1 = new byte[]{0x2F};
                                str_line = "------------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------------".getBytes(), LF

                                };
                            }
                        }
                    } else {
                        if (str_print_ty.toString().equals("Generic")) {
//                            Toast.makeText(KOT_Management.this, "phi", Toast.LENGTH_SHORT).show();
                            setHT32 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 2"
                            setHT321 = new byte[]{0x1b, 0x44, 0x13, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x15, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                            setHT34 = new byte[]{0x1b, 0x44, 0x04, 0x12, 0x19, 0x00};//4 tabs 2"
                            setHTKOT = new byte[]{0x1b, 0x06, 0x44, 0x00};//2 tabs 2"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                            nPaperWidth = 384;
                            charlength = 25;
                            HT1 = new byte[]{0x09};
                            str_line = "--------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "--------------------------------".getBytes(), LF
                            };
                        }else {
                            if (str_print_ty.toString().equals("Epson/others")) {
//                            Toast.makeText(KOT_Management.this, "epson", Toast.LENGTH_SHORT).show();
                                setHT32 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 2"
                                setHT321 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x21, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                                setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x19, 0x21, 0x00};//4 tabs 2"
                                setHTKOT = new byte[]{0x1b, 0x09, 0x44, 0x00};//2 tabs 2"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                                nPaperWidth = 384;
                                charlength = 28;
                                HT1 = new byte[]{0x09};
                                str_line = "------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------".getBytes(), LF
                                };
                            }else {
                                if (str_print_ty.toString().equals("POS")) {
                                    setHT32 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT321 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT3212 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 3"
                                    setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x12, 0x21, 0x00};//4 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x05, 0x08, 0x00};//4 tabs 2"
                                    setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x08, 0x09, 0x00};//4 tabs 2"
                                    setHTKOT = new byte[]{0x1b, 0x05, 0x44, 0x00};//2 tabs 3"
                                    feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                    nPaperWidth = 384;
                                    charlength = 11;
                                    charlength1 = 22;
                                    charlength2 = 33;
                                    quanlentha = 3;
                                    HT1 = new byte[]{0x2F};
                                    str_line = "--------------------------------";
                                    allbufline = new byte[][]{
                                            left, un1, "--------------------------------".getBytes(), LF
                                    };
                                }
                            }
                        }
                    }
                } while (cc.moveToNext());
            }
            cc.close();

        }
        getprint_type.close();

        String dd = "";
        TextView qazcvb = new TextView(KOT_Management.this);
        Cursor cvonnusb = db.rawQuery("SELECT * FROM BTConn", null);
        if (cvonnusb.moveToFirst()) {
            addgets = cvonnusb.getString(1);
            namegets = cvonnusb.getString(2);
            statussusbs = cvonnusb.getString(3);
            dd = cvonnusb.getString(4);
        }
        cvonnusb.close();
        qazcvb.setText(dd);
        if (qazcvb.getText().toString().equals("usb") && statussusbs.toString().equals("ok")) {
            runPrintCouponSequence_kotcancel(kot_no2, date1, time1, reason);
        }else {
            Cursor connnet = db.rawQuery("SELECT * FROM IPConn", null);
            if (connnet.moveToFirst()) {
                ipnamegets = connnet.getString(1);
                portgets = connnet.getString(2);
                statusnets = connnet.getString(3);
            }

            Cursor connnet_counter = db.rawQuery("SELECT * FROM IPConn_Counter", null);
            if (connnet_counter.moveToFirst()) {
                ipnamegets_counter = connnet_counter.getString(1);
                portgets_counter = connnet_counter.getString(2);
                statusnets_counter = connnet_counter.getString(3);
            }
            connnet_counter.close();

            Cursor connusb = db.rawQuery("SELECT * FROM BTConn", null);
            if (connusb.moveToFirst()) {
                addgets = connusb.getString(1);
                namegets = connusb.getString(2);
                statussusbs = connusb.getString(3);
            }

            Cursor c_kot1 = db.rawQuery("SELECT * FROM IPConn_KOT1", null);
            if (c_kot1.moveToFirst()) {
                ipnamegets_kot1 = c_kot1.getString(1);
                portgets_kot1 = c_kot1.getString(2);
                statusnets_kot1 = c_kot1.getString(3);
                name_kot1 = c_kot1.getString(5);
            }
            c_kot1.close();

            Cursor c_kot2 = db.rawQuery("SELECT * FROM IPConn_KOT2", null);
            if (c_kot2.moveToFirst()) {
                ipnamegets_kot2 = c_kot2.getString(1);
                portgets_kot2 = c_kot2.getString(2);
                statusnets_kot2 = c_kot2.getString(3);
                name_kot2 = c_kot2.getString(5);
            }
            c_kot2.close();

            Cursor c_kot3 = db.rawQuery("SELECT * FROM IPConn_KOT3", null);
            if (c_kot3.moveToFirst()) {
                ipnamegets_kot3 = c_kot3.getString(1);
                portgets_kot3 = c_kot3.getString(2);
                statusnets_kot3 = c_kot3.getString(3);
                name_kot3 = c_kot3.getString(5);
            }
            c_kot3.close();

            Cursor c_kot4 = db.rawQuery("SELECT * FROM IPConn_KOT4", null);
            if (c_kot4.moveToFirst()) {
                ipnamegets_kot4 = c_kot4.getString(1);
                portgets_kot4 = c_kot4.getString(2);
                statusnets_kot4 = c_kot4.getString(3);
                name_kot4 = c_kot4.getString(5);
            }
            c_kot4.close();

            textViewstatusnets.setText(statusnet);
            textViewstatusnets_counter.setText(statusnets_counter);
            textViewstatussusbs.setText(statussusbs);
            textViewstatusnets_kot1.setText(statusnets_kot1);
            textViewstatusnets_kot2.setText(statusnets_kot2);
            textViewstatusnets_kot3.setText(statusnets_kot3);
            textViewstatusnets_kot4.setText(statusnets_kot4);

            Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
            if (getcom.moveToFirst()) {
                do {
                    strcompanyname = getcom.getString(1);
                } while (getcom.moveToNext());
            }


            tvkot.setText(strcompanyname);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf1 = new byte[][]{
                        bold, cen, strcompanyname.getBytes(), LF, LF

                };
                if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                    wifiSocket_kot1.WIFI_Write(bold);    //
                    wifiSocket_kot1.WIFI_Write(cen);    //
                    wifiSocket_kot1.WIFI_Write(strcompanyname);
                    wifiSocket_kot1.WIFI_Write(LF);    //
                    wifiSocket_kot1.WIFI_Write(LF);    //
                }else {
                    if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                        wifiSocket_kot2.WIFI_Write(bold);    //
                        wifiSocket_kot2.WIFI_Write(cen);    //
                        wifiSocket_kot2.WIFI_Write(strcompanyname);
                        wifiSocket_kot2.WIFI_Write(LF);    //
                        wifiSocket_kot2.WIFI_Write(LF);    //
                    }else {
                        if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                            wifiSocket_kot3.WIFI_Write(bold);    //
                            wifiSocket_kot3.WIFI_Write(cen);    //
                            wifiSocket_kot3.WIFI_Write(strcompanyname);
                            wifiSocket_kot3.WIFI_Write(LF);    //
                            wifiSocket_kot3.WIFI_Write(LF);    //
                        }else {
                            if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                wifiSocket_kot4.WIFI_Write(bold);    //
                                wifiSocket_kot4.WIFI_Write(cen);    //
                                wifiSocket_kot4.WIFI_Write(strcompanyname);
                                wifiSocket_kot4.WIFI_Write(LF);    //
                                wifiSocket_kot4.WIFI_Write(LF);    //
                            }else {
                                if (textViewstatusnets.getText().toString().equals("ok")) {
                                    wifiSocket.WIFI_Write(bold);    //
                                    wifiSocket.WIFI_Write(cen);    //
                                    wifiSocket.WIFI_Write(strcompanyname);
                                    wifiSocket.WIFI_Write(LF);    //
                                    wifiSocket.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                        wifiSocket2.WIFI_Write(bold);    //
                                        wifiSocket2.WIFI_Write(cen);    //
                                        wifiSocket2.WIFI_Write(strcompanyname);
                                        wifiSocket2.WIFI_Write(LF);    //
                                        wifiSocket2.WIFI_Write(LF);    //
                                    } else {
                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(bold);    //
                                            BluetoothPrintDriver.BT_Write(cen);    //
                                            BT_Write(strcompanyname);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }


            allbufKOT = new byte[][]{
                    un, cen, "Cancel Order Ticket".getBytes(), LF
            };
            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(un);    //
                wifiSocket_kot1.WIFI_Write(cen);    //
                wifiSocket_kot1.WIFI_Write("Cancel Order Ticket");
                wifiSocket_kot1.WIFI_Write(LF);    //
            }else {
                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                    wifiSocket_kot2.WIFI_Write(un);    //
                    wifiSocket_kot2.WIFI_Write(cen);    //
                    wifiSocket_kot2.WIFI_Write("Cancel Order Ticket");
                    wifiSocket_kot2.WIFI_Write(LF);    //
                }else {
                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                        wifiSocket_kot3.WIFI_Write(un);    //
                        wifiSocket_kot3.WIFI_Write(cen);    //
                        wifiSocket_kot3.WIFI_Write("Cancel Order Ticket");
                        wifiSocket_kot3.WIFI_Write(LF);    //
                    }else {
                        if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                            wifiSocket_kot4.WIFI_Write(un);    //
                            wifiSocket_kot4.WIFI_Write(cen);    //
                            wifiSocket_kot4.WIFI_Write("Cancel Order Ticket");
                            wifiSocket_kot4.WIFI_Write(LF);    //
                        }else {
                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                wifiSocket.WIFI_Write(un);    //
                                wifiSocket.WIFI_Write(cen);    //
                                wifiSocket.WIFI_Write("Cancel Order Ticket");
                                wifiSocket.WIFI_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(un);    //
                                    wifiSocket2.WIFI_Write(cen);    //
                                    wifiSocket2.WIFI_Write("Cancel Order Ticket");
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(un);    //
                                        BluetoothPrintDriver.BT_Write(cen);    //
                                        BT_Write("Cancel Order Ticket");
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
            if (vbnm.moveToFirst()) {
                assa1 = vbnm.getString(1);
                assa2 = vbnm.getString(2);
            }
            TextView cx = new TextView(KOT_Management.this);
            cx.setText(assa1);
            String tablen = "";
            if (cx.getText().toString().equals("")) {
                tablen = "Tab" + assa2;
                allbuf11 = new byte[][]{
                        left, un1, setHT321, tablen.getBytes(), LF
                };
            } else {
                tablen = "Tab" + assa1;
                allbuf11 = new byte[][]{
                        left, un1, setHT321, tablen.getBytes(), LF
                };
            }

//        String tablen = "Table"+ItemIDtable;
//        allbuf11 = new byte[][]{
//                left,un1,setHT321, tablen.getBytes(),LF
//        };
            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(setHT321);    //
                wifiSocket_kot1.WIFI_Write(normal);    //
                wifiSocket_kot1.WIFI_Write(un1);    //
                wifiSocket_kot1.WIFI_Write(left);    //
                wifiSocket_kot1.WIFI_Write(tablen);
                wifiSocket_kot1.WIFI_Write(LF);    //
            }else {
                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                    wifiSocket_kot2.WIFI_Write(setHT321);    //
                    wifiSocket_kot2.WIFI_Write(normal);    //
                    wifiSocket_kot2.WIFI_Write(un1);    //
                    wifiSocket_kot2.WIFI_Write(left);    //
                    wifiSocket_kot2.WIFI_Write(tablen);
                    wifiSocket_kot2.WIFI_Write(LF);    //
                } else {
                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                        wifiSocket_kot3.WIFI_Write(setHT321);    //
                        wifiSocket_kot3.WIFI_Write(normal);    //
                        wifiSocket_kot3.WIFI_Write(un1);    //
                        wifiSocket_kot3.WIFI_Write(left);    //
                        wifiSocket_kot3.WIFI_Write(tablen);
                        wifiSocket_kot3.WIFI_Write(LF);    //
                    } else {
                        if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                            wifiSocket_kot4.WIFI_Write(setHT321);    //
                            wifiSocket_kot4.WIFI_Write(normal);    //
                            wifiSocket_kot4.WIFI_Write(un1);    //
                            wifiSocket_kot4.WIFI_Write(left);    //
                            wifiSocket_kot4.WIFI_Write(tablen);
                            wifiSocket_kot4.WIFI_Write(LF);    //
                        } else {
                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                wifiSocket.WIFI_Write(setHT321);    //
                                wifiSocket.WIFI_Write(normal);    //
                                wifiSocket.WIFI_Write(un1);    //
                                wifiSocket.WIFI_Write(left);    //
                                wifiSocket.WIFI_Write(tablen);
                                wifiSocket.WIFI_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(setHT321);    //
                                    wifiSocket2.WIFI_Write(normal);    //
                                    wifiSocket2.WIFI_Write(un1);    //
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write(tablen);
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(setHT321);    //
                                        BluetoothPrintDriver.BT_Write(normal);    //
                                        BluetoothPrintDriver.BT_Write(un1);    //
                                        BluetoothPrintDriver.BT_Write(left);    //
                                        BluetoothPrintDriver.BT_Write(tablen);
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }
                                }
                            }
                        }
                    }
                }
            }


            allbuf10 = new byte[][]{
                    setHT321, left, date1.getBytes(), HT, "   ".getBytes(), time1.getBytes(), LF
            };
            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(setHT321);    //
                wifiSocket_kot1.WIFI_Write(left);    //
                wifiSocket_kot1.WIFI_Write(date1);
                wifiSocket_kot1.WIFI_Write(HT);    //
                wifiSocket_kot1.WIFI_Write("   ");
                wifiSocket_kot1.WIFI_Write(time1);
                wifiSocket_kot1.WIFI_Write(LF);    //
            }else {
                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                    wifiSocket_kot2.WIFI_Write(setHT321);    //
                    wifiSocket_kot2.WIFI_Write(left);    //
                    wifiSocket_kot2.WIFI_Write(date1);
                    wifiSocket_kot2.WIFI_Write(HT);    //
                    wifiSocket_kot2.WIFI_Write("   ");
                    wifiSocket_kot2.WIFI_Write(time1);
                    wifiSocket_kot2.WIFI_Write(LF);    //
                } else {
                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                        wifiSocket_kot3.WIFI_Write(setHT321);    //
                        wifiSocket_kot3.WIFI_Write(left);    //
                        wifiSocket_kot3.WIFI_Write(date1);
                        wifiSocket_kot3.WIFI_Write(HT);    //
                        wifiSocket_kot3.WIFI_Write("   ");
                        wifiSocket_kot3.WIFI_Write(time1);
                        wifiSocket_kot3.WIFI_Write(LF);    //
                    } else {
                        if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                            wifiSocket_kot4.WIFI_Write(setHT321);    //
                            wifiSocket_kot4.WIFI_Write(left);    //
                            wifiSocket_kot4.WIFI_Write(date1);
                            wifiSocket_kot4.WIFI_Write(HT);    //
                            wifiSocket_kot4.WIFI_Write("   ");
                            wifiSocket_kot4.WIFI_Write(time1);
                            wifiSocket_kot4.WIFI_Write(LF);    //
                        } else {
                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                wifiSocket.WIFI_Write(setHT321);    //
                                wifiSocket.WIFI_Write(left);    //
                                wifiSocket.WIFI_Write(date1);
                                wifiSocket.WIFI_Write(HT);    //
                                wifiSocket.WIFI_Write("   ");
                                wifiSocket.WIFI_Write(time1);
                                wifiSocket.WIFI_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(setHT321);    //
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write(date1);
                                    wifiSocket2.WIFI_Write(HT);    //
                                    wifiSocket2.WIFI_Write("   ");
                                    wifiSocket2.WIFI_Write(time1);
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(setHT321);    //
                                        BluetoothPrintDriver.BT_Write(left);    //
                                        BT_Write(date1);
                                        BluetoothPrintDriver.BT_Write(HT);    //
                                        BT_Write("   ");
                                        BT_Write(time1);
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }
                                }
                            }
                        }
                    }
                }
            }

            allbufKOT = new byte[][]{
                    un, cen, "KOT no. : Cancelled ".getBytes(), LF
            };
            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write("KOT no. : Cancelled "+kot_no2);
                wifiSocket_kot1.WIFI_Write(LF);    //
            }else {
                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                    wifiSocket_kot2.WIFI_Write("KOT no. : Cancelled "+kot_no2);
                    wifiSocket_kot2.WIFI_Write(LF);    //
                }else {
                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                        wifiSocket_kot3.WIFI_Write("KOT no. : Cancelled "+kot_no2);
                        wifiSocket_kot3.WIFI_Write(LF);    //
                    }else {
                        if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                            wifiSocket_kot4.WIFI_Write("KOT no. : Cancelled "+kot_no2);
                            wifiSocket_kot4.WIFI_Write(LF);    //
                        }else {
                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                wifiSocket.WIFI_Write("KOT no. : Cancelled "+kot_no2);
                                wifiSocket.WIFI_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write("KOT no. : Cancelled "+kot_no2);
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BT_Write("KOT no. : Cancelled "+kot_no2);
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }
                                }
                            }
                        }
                    }
                }
            }


            allbufKOT = new byte[][]{
                    un, cen, "Reason  : ".getBytes(), LF
            };
            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write("Reason  : "+reason);
                wifiSocket_kot1.WIFI_Write(LF);    //
            }else {
                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                    wifiSocket_kot2.WIFI_Write("Reason  : "+reason);
                    wifiSocket_kot2.WIFI_Write(LF);    //
                }else {
                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                        wifiSocket_kot3.WIFI_Write("Reason  : "+reason);
                        wifiSocket_kot3.WIFI_Write(LF);    //
                    }else {
                        if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                            wifiSocket_kot4.WIFI_Write("Reason  : "+reason);
                            wifiSocket_kot4.WIFI_Write(LF);    //
                        }else {
                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                wifiSocket.WIFI_Write("Reason  : "+reason);
                                wifiSocket.WIFI_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write("Reason  : "+reason);
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BT_Write("Reason  : "+reason);
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }
                                }
                            }
                        }
                    }
                }
            }


            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(left);    //
                wifiSocket_kot1.WIFI_Write(un1);    //
                wifiSocket_kot1.WIFI_Write(str_line);
                wifiSocket_kot1.WIFI_Write(LF);    //
            }else {
                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                    wifiSocket_kot2.WIFI_Write(left);    //
                    wifiSocket_kot2.WIFI_Write(un1);    //
                    wifiSocket_kot2.WIFI_Write(str_line);
                    wifiSocket_kot2.WIFI_Write(LF);    //
                }else {
                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                        wifiSocket_kot3.WIFI_Write(left);    //
                        wifiSocket_kot3.WIFI_Write(un1);    //
                        wifiSocket_kot3.WIFI_Write(str_line);
                        wifiSocket_kot3.WIFI_Write(LF);    //
                    }else {
                        if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                            wifiSocket_kot4.WIFI_Write(left);    //
                            wifiSocket_kot4.WIFI_Write(un1);    //
                            wifiSocket_kot4.WIFI_Write(str_line);
                            wifiSocket_kot4.WIFI_Write(LF);    //
                        }else {
                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                wifiSocket.WIFI_Write(left);    //
                                wifiSocket.WIFI_Write(un1);    //
                                wifiSocket.WIFI_Write(str_line);
                                wifiSocket.WIFI_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write(un1);    //
                                    wifiSocket2.WIFI_Write(str_line);
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(left);    //
                                        BluetoothPrintDriver.BT_Write(un1);    //
                                        BT_Write(str_line);
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }
                                }
                            }
                        }
                    }
                }
            }


            allbufqty = new byte[][]{
                    setHT3212, normal, "Item".getBytes(), HT, "-Qty".getBytes(), left, LF
            };
            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(setHT3212);    //
                wifiSocket_kot1.WIFI_Write(normal);    //
                wifiSocket_kot1.WIFI_Write("Item");
                wifiSocket_kot1.WIFI_Write(HT);    //
                wifiSocket_kot1.WIFI_Write("-Qty");
                wifiSocket_kot1.WIFI_Write(left);    //
                wifiSocket_kot1.WIFI_Write(LF);    //
            }else {
                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                    wifiSocket_kot2.WIFI_Write(setHT3212);    //
                    wifiSocket_kot2.WIFI_Write(normal);    //
                    wifiSocket_kot2.WIFI_Write("Item");
                    wifiSocket_kot2.WIFI_Write(HT);    //
                    wifiSocket_kot2.WIFI_Write("-Qty");
                    wifiSocket_kot2.WIFI_Write(left);    //
                    wifiSocket_kot2.WIFI_Write(LF);    //
                }else {
                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                        wifiSocket_kot3.WIFI_Write(setHT3212);    //
                        wifiSocket_kot3.WIFI_Write(normal);    //
                        wifiSocket_kot3.WIFI_Write("Item");
                        wifiSocket_kot3.WIFI_Write(HT);    //
                        wifiSocket_kot3.WIFI_Write("-Qty");
                        wifiSocket_kot3.WIFI_Write(left);    //
                        wifiSocket_kot3.WIFI_Write(LF);    //
                    }else {
                        if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                            wifiSocket_kot4.WIFI_Write(setHT3212);    //
                            wifiSocket_kot4.WIFI_Write(normal);    //
                            wifiSocket_kot4.WIFI_Write("Item");
                            wifiSocket_kot4.WIFI_Write(HT);    //
                            wifiSocket_kot4.WIFI_Write("-Qty");
                            wifiSocket_kot4.WIFI_Write(left);    //
                            wifiSocket_kot4.WIFI_Write(LF);    //
                        }else {
                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                wifiSocket.WIFI_Write(setHT3212);    //
                                wifiSocket.WIFI_Write(normal);    //
                                wifiSocket.WIFI_Write("Item");
                                wifiSocket.WIFI_Write(HT);    //
                                wifiSocket.WIFI_Write("-Qty");
                                wifiSocket.WIFI_Write(left);    //
                                wifiSocket.WIFI_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(setHT3212);    //
                                    wifiSocket2.WIFI_Write(normal);    //
                                    wifiSocket2.WIFI_Write("Item");
                                    wifiSocket2.WIFI_Write(HT);    //
                                    wifiSocket2.WIFI_Write("-Qty");
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(setHT3212);    //
                                        BluetoothPrintDriver.BT_Write(normal);    //
                                        BT_Write("Item");
                                        BluetoothPrintDriver.BT_Write(HT);    //
                                        BT_Write("-Qty");
                                        BluetoothPrintDriver.BT_Write(left);    //
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }
                                }
                            }
                        }
                    }
                }
            }


            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(left);    //
                wifiSocket_kot1.WIFI_Write(un1);    //
                wifiSocket_kot1.WIFI_Write(str_line);
                wifiSocket_kot1.WIFI_Write(LF);    //
            }else {
                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                    wifiSocket_kot2.WIFI_Write(left);    //
                    wifiSocket_kot2.WIFI_Write(un1);    //
                    wifiSocket_kot2.WIFI_Write(str_line);
                    wifiSocket_kot2.WIFI_Write(LF);    //
                }else {
                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                        wifiSocket_kot3.WIFI_Write(left);    //
                        wifiSocket_kot3.WIFI_Write(un1);    //
                        wifiSocket_kot3.WIFI_Write(str_line);
                        wifiSocket_kot3.WIFI_Write(LF);    //
                    }else {
                        if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                            wifiSocket_kot4.WIFI_Write(left);    //
                            wifiSocket_kot4.WIFI_Write(un1);    //
                            wifiSocket_kot4.WIFI_Write(str_line);
                            wifiSocket_kot4.WIFI_Write(LF);    //
                        }else {
                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                wifiSocket.WIFI_Write(left);    //
                                wifiSocket.WIFI_Write(un1);    //
                                wifiSocket.WIFI_Write(str_line);
                                wifiSocket.WIFI_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write(un1);    //
                                    wifiSocket2.WIFI_Write(str_line);
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(left);    //
                                        BluetoothPrintDriver.BT_Write(un1);    //
                                        BT_Write(str_line);
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }
                                }
                            }
                        }
                    }
                }
            }



            Cursor cursor = db1.rawQuery("SELECT * FROM Kotcancelled WHERE kot_no = '"+kot_no2+"'", null);
            if (cursor.moveToFirst()) {
                do {
                    String itemname = cursor.getString(1);
                    String qty = cursor.getString(2);

                    allbufqty = new byte[][]{
                            setHT3212, normal, itemname.getBytes(), HT, "-Qty".getBytes(), left, LF
                    };
                    if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                        wifiSocket_kot1.WIFI_Write(setHT3212);    //
                        wifiSocket_kot1.WIFI_Write(normal);    //
                        wifiSocket_kot1.WIFI_Write(itemname);
                        wifiSocket_kot1.WIFI_Write(HT);    //
                        wifiSocket_kot1.WIFI_Write("-"+qty);
                        wifiSocket_kot1.WIFI_Write(left);    //
                        wifiSocket_kot1.WIFI_Write(LF);    //
                    }else {
                        if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                            wifiSocket_kot2.WIFI_Write(setHT3212);    //
                            wifiSocket_kot2.WIFI_Write(normal);    //
                            wifiSocket_kot2.WIFI_Write(itemname);
                            wifiSocket_kot2.WIFI_Write(HT);    //
                            wifiSocket_kot2.WIFI_Write("-"+qty);
                            wifiSocket_kot2.WIFI_Write(left);    //
                            wifiSocket_kot2.WIFI_Write(LF);    //
                        }else {
                            if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                                wifiSocket_kot3.WIFI_Write(setHT3212);    //
                                wifiSocket_kot3.WIFI_Write(normal);    //
                                wifiSocket_kot3.WIFI_Write(itemname);
                                wifiSocket_kot3.WIFI_Write(HT);    //
                                wifiSocket_kot3.WIFI_Write("-"+qty);
                                wifiSocket_kot3.WIFI_Write(left);    //
                                wifiSocket_kot3.WIFI_Write(LF);    //
                            }else {
                                if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                    wifiSocket_kot4.WIFI_Write(setHT3212);    //
                                    wifiSocket_kot4.WIFI_Write(normal);    //
                                    wifiSocket_kot4.WIFI_Write(itemname);
                                    wifiSocket_kot4.WIFI_Write(HT);    //
                                    wifiSocket_kot4.WIFI_Write("-"+qty);
                                    wifiSocket_kot4.WIFI_Write(left);    //
                                    wifiSocket_kot4.WIFI_Write(LF);    //
                                }else {
                                    if (textViewstatusnets.getText().toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(setHT3212);    //
                                        wifiSocket.WIFI_Write(normal);    //
                                        wifiSocket.WIFI_Write(itemname);
                                        wifiSocket.WIFI_Write(HT);    //
                                        wifiSocket.WIFI_Write("-"+qty);
                                        wifiSocket.WIFI_Write(left);    //
                                        wifiSocket.WIFI_Write(LF);    //
                                    } else {
                                        if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                            wifiSocket2.WIFI_Write(setHT3212);    //
                                            wifiSocket2.WIFI_Write(normal);    //
                                            wifiSocket2.WIFI_Write(itemname);
                                            wifiSocket2.WIFI_Write(HT);    //
                                            wifiSocket2.WIFI_Write("-"+qty);
                                            wifiSocket2.WIFI_Write(left);    //
                                            wifiSocket2.WIFI_Write(LF);    //
                                        } else {
                                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                BluetoothPrintDriver.BT_Write(setHT3212);    //
                                                BluetoothPrintDriver.BT_Write(normal);    //
                                                BT_Write(itemname);
                                                BluetoothPrintDriver.BT_Write(HT);    //
                                                BT_Write("-"+qty);
                                                BluetoothPrintDriver.BT_Write(left);    //
                                                BluetoothPrintDriver.BT_Write(LF);    //
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }


                }while (cursor.moveToNext());
            }


            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(left);    //
                wifiSocket_kot1.WIFI_Write(un1);    //
                wifiSocket_kot1.WIFI_Write(str_line);
                wifiSocket_kot1.WIFI_Write(LF);    //
            }else {
                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                    wifiSocket_kot2.WIFI_Write(left);    //
                    wifiSocket_kot2.WIFI_Write(un1);    //
                    wifiSocket_kot2.WIFI_Write(str_line);
                    wifiSocket_kot2.WIFI_Write(LF);    //
                }else {
                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                        wifiSocket_kot3.WIFI_Write(left);    //
                        wifiSocket_kot3.WIFI_Write(un1);    //
                        wifiSocket_kot3.WIFI_Write(str_line);
                        wifiSocket_kot3.WIFI_Write(LF);    //
                    }else {
                        if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                            wifiSocket_kot4.WIFI_Write(left);    //
                            wifiSocket_kot4.WIFI_Write(un1);    //
                            wifiSocket_kot4.WIFI_Write(str_line);
                            wifiSocket_kot4.WIFI_Write(LF);    //
                        }else {
                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                wifiSocket.WIFI_Write(left);    //
                                wifiSocket.WIFI_Write(un1);    //
                                wifiSocket.WIFI_Write(str_line);
                                wifiSocket.WIFI_Write(LF);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write(un1);    //
                                    wifiSocket2.WIFI_Write(str_line);
                                    wifiSocket2.WIFI_Write(LF);    //
                                } else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(left);    //
                                        BluetoothPrintDriver.BT_Write(un1);    //
                                        BT_Write(str_line);
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }
                                }
                            }
                        }
                    }
                }
            }





            byte[][] allbuf = new byte[][]{
                    feedcut2
            };
            if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                wifiSocket_kot1.WIFI_Write(feedcut2);    //
            }else {
                if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                    wifiSocket_kot2.WIFI_Write(feedcut2);    //
                }else {
                    if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                        wifiSocket_kot3.WIFI_Write(feedcut2);    //
                    }else {
                        if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                            wifiSocket_kot4.WIFI_Write(feedcut2);    //
                        }else {
                            if (textViewstatusnets.getText().toString().equals("ok")) {
                                wifiSocket.WIFI_Write(feedcut2);    //
                            } else {
                                if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(feedcut2);    //
                                } else {
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(feedcut2);    //
                                    }
                                }
                            }
                        }
                    }
                }
            }


            if (str_print_ty.toString().equals("POS")) {
                if (textViewstatusnets_kot1.getText().toString().equals("ok")) {
                    wifiSocket_kot1.WIFI_Write(feedcut2);    //
                }else {
                    if (textViewstatusnets_kot2.getText().toString().equals("ok")) {
                        wifiSocket_kot2.WIFI_Write(feedcut2);    //
                    }else {
                        if (textViewstatusnets_kot3.getText().toString().equals("ok")) {
                            wifiSocket_kot3.WIFI_Write(feedcut2);    //
                        }else {
                            if (textViewstatusnets_kot4.getText().toString().equals("ok")) {
                                wifiSocket_kot4.WIFI_Write(feedcut2);    //
                            }else {
                                if (textViewstatusnets.getText().toString().equals("ok")) {
                                    wifiSocket.WIFI_Write(feedcut2);    //
                                } else {
                                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
                                        wifiSocket2.WIFI_Write(feedcut2);    //
                                    } else {
                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(feedcut2);    //
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }

        }
    }

    public  byte[] neoprintbill_kotcancel(String kot_no2, String date1, String time1, String reason) {


        Typeface tf = Typeface.SERIF;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ReceiptBitmap receiptBitmap = new ReceiptBitmap().getInstance();
        int cont=0;
        Cursor cursor34 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE status = 'print'", null);//replace to cursor = dbHelper.fetchAllHotels();
        if (cursor34.moveToFirst()) {
            cont=cursor34.getCount();

        }
        cursor34.close();
        receiptBitmap.init(200+(cont*50));
        receiptBitmap.setTextSize(25);
        receiptBitmap.setTypeface(Typeface.create(tf, Typeface.NORMAL));


        charlength = 10;
        charlength1 = 20;
        charlength2 = 30;
        quanlentha = 5;

        Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
        if (getcom.moveToFirst()) {
            strcompanyname = getcom.getString(1);
        }
        getcom.close();

        tvkot.setText(strcompanyname);
        if (tvkot.getText().toString().equals("")) {

        } else {
            // Print.StartPrinting(strcompanyname ,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
            receiptBitmap.drawCenterText(strcompanyname);
        }

        receiptBitmap.drawCenterText("Cancel Order Ticket");



        Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
        if (vbnm.moveToFirst()) {
            assa1 = vbnm.getString(1);
            assa2 = vbnm.getString(2);
        }
        vbnm.close();
        TextView cx = new TextView(KOT_Management.this);
        cx.setText(assa1);
        String tablen = "";
        if (cx.getText().toString().equals("")) {
            tablen = "Tab" + assa2;
        } else {
            tablen = "Tab" + assa1;
        }


        //   Print.StartPrinting(NAme111+"   "+tablen,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        receiptBitmap.drawLeftText(tablen);




        receiptBitmap.drawLeftText(date1+"   "+time1);

        receiptBitmap.drawLeftText("KOT no. : Cancelled "+kot_no2);
        receiptBitmap.drawLeftText("Reason  : "+reason);

        String str_line="----------------------";
        receiptBitmap.drawLeftText(str_line);
        receiptBitmap.drawLeftText("Item"+"  "+"-Qty");
        receiptBitmap.drawLeftText(str_line);





        Cursor cursor = db1.rawQuery("SELECT * FROM Kotcancelled WHERE kot_no = '"+kot_no2+"'", null);
        if (cursor.moveToFirst()) {
            do {
                String itemname = cursor.getString(1);
                String qty = cursor.getString(2);


                receiptBitmap.drawLeftText(itemname+"  -"+qty);

            }while (cursor.moveToNext());
        }

        receiptBitmap.drawLeftText(str_line);




        receiptBitmap.drawLeftText("       ");
        receiptBitmap.drawLeftText("       ");
        receiptBitmap.drawLeftText("       ");

        Bitmap canvasbitmap = receiptBitmap.getReceiptBitmap();

        if(ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"getHeight: " + canvasbitmap.getHeight(),true,true);

        if(ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"getReceiptHeight: " + receiptBitmap.getReceiptHeight(),true,true);

        Bitmap croppedBmp = Bitmap.createBitmap(canvasbitmap, 0, 0, canvasbitmap.getWidth(), canvasbitmap.getHeight());

        byte[] imageCommand = mMSWisepadDeviceController.getPrintData(croppedBmp, 150);

        baos.write(imageCommand, 0, imageCommand.length);

        if(ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"end of reciept",true,true);

        return baos.toByteArray();

    }

    public void wiseposprintbill_kotcancel(String kot_no2, String date1, String time1, String reason) {

        charlength = 10;
        charlength1 = 20;
        charlength2 = 30;
        quanlentha = 5;

        Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
        if (getcom.moveToFirst()) {
            strcompanyname = getcom.getString(1);
        }
        getcom.close();

        tvkot.setText(strcompanyname);
        if (tvkot.getText().toString().equals("")) {

        } else {
            Print.StartPrinting(strcompanyname , FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        }
        Print.StartPrinting("Cancel Order Ticket" ,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);

        Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
        if (vbnm.moveToFirst()) {
            assa1 = vbnm.getString(1);
            assa2 = vbnm.getString(2);
        }
        vbnm.close();
        TextView cx = new TextView(KOT_Management.this);
        cx.setText(assa1);
        String tablen = "";
        if (cx.getText().toString().equals("")) {
            tablen = "Tab" + assa2;
        } else {
            tablen = "Tab" + assa1;
        }


        Print.StartPrinting(tablen,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);






        Print.StartPrinting(date1+"   "+time1,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);

        String str_line="----------------------";
        Print.StartPrinting(str_line,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);

        Print.StartPrinting("Qty"+"  "+"Item",FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);
        Print.StartPrinting(str_line,FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);


        Cursor cursor = db1.rawQuery("SELECT * FROM Kotcancelled WHERE kot_no = '"+kot_no2+"'", null);
        if (cursor.moveToFirst()) {
            do {
                String itemname = cursor.getString(1);
                String qty = cursor.getString(2);

                Print.StartPrinting(itemname+"  -"+qty,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);

            }while (cursor.moveToNext());
        }


        Print.StartPrinting(str_line,FontLattice.TWENTY_FOUR, mswipe_text, Align.LEFT, true);





        Print.StartPrinting("       ",FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        Print.StartPrinting("       ",FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);
        Print.StartPrinting("       ",FontLattice.TWENTY_FOUR, mswipe_text, Align.CENTER, true);

//            if (str_print_ty.toString().equals("POS")) {
//                if (textViewstatusnets.getText().toString().equals("ok")) {
//                    wifiSocket.WIFI_Write(feedcut2);    //
//                } else {
//                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
//                        wifiSocket2.WIFI_Write(feedcut2);    //
//                    } else {
//                        if (textViewstatussusbs.getText().toString().equals("ok")) {
//                            BluetoothPrintDriver.BT_Write(feedcut2);    //
//                        }
//                    }
//                }
//            }

//        Toast.makeText(KOT_Management.this, "KOT printed", Toast.LENGTH_LONG).show();
    }

    private boolean createCouponData_kotcancel(String kot_no2, String date1, String time1, String reason) {

        byte[] setHT34M = {0x1b,0x44,0x04,0x11,0x19,0x00};
        byte[] dotfeed = {0x1b,0x4a,0x10};
        byte[] HTRight = {0x1b,0x61, 0x02,0x09};
        byte[] HT = {0x09};
        byte[] HT1 = {0x09};
        byte[] LF = {0x0d,0x0a};

        byte[] left = {0x1b,0x61, 0x00};
        byte[] cen = {0x1b,0x61, 0x01};
        byte[] right = {0x1b,0x61, 0x02};
        byte[] bold = {0x1B,0x21,0x10};
        byte[] normal = {0x1d, 0x21, 0x00};
        byte[] horiz1 = {0x1b,0x44,0x19, 0x19, 0x00};
        byte[] horiz = {0x1b,0x44, 0x04, 0x06, 0x04, 0x04, 0x0A, 0x00, 0x09, 0x30, 0x09, 0x31, 0x09, 0x32, 0x09, 0x33, 0x09, 0x34, 0x0d,0x0a};

        byte[] un = {0x1b, 0x2d, 0x02};
        byte[] un1 = {0x1b, 0x2d, 0x00};
        String str_line = "";

        Cursor print_ty = db.rawQuery("SELECT * FROM Printer_type", null);
        if (print_ty.moveToFirst()){
            str_print_ty = print_ty.getString(1);
        }
        print_ty.close();

        Cursor getprint_type = db.rawQuery("SELECT * FROM Printer_text_size", null);
        if (getprint_type.moveToFirst()) {
            String type = getprint_type.getString(1);

            Cursor cc = db.rawQuery("SELECT * FROM Printerreceiptsize", null);

            if (cc.moveToFirst()) {
                cc.moveToFirst();
                do {
                    NAME = cc.getString(1);
                    if (NAME.equals("3 inch")) {
                        if (str_print_ty.toString().equals("Generic") || str_print_ty.toString().equals("Epson/others")) {
                            setHT32 = new byte[]{0x1b, 0x44, 0x29, 0x00};//2 tabs 3"
                            setHT321 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x25, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                            setHT34 = new byte[]{0x1b, 0x44, 0x08, 0x20, 0x29, 0x00};//4 tabs 3"
                            setHTKOT = new byte[]{0x1b, 0x06, 0x44, 0x00};//2 tabs 3"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                            nPaperWidth = 576;
                            charlength = 41;
                            HT1 = new byte[]{0x09};
                            str_line = "------------------------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "------------------------------------------------".getBytes(), LF

                            };
                        }else {
                            if (str_print_ty.toString().equals("POS")) {
                                setHT32 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT321 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x10, 0x15, 0x00};//4 tabs 3"
                                setHTKOT = new byte[]{0x1b, 0x05, 0x44, 0x00};//2 tabs 3"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                nPaperWidth = 576;
                                charlength = 23;
                                charlength1 = 46;
                                charlength2 = 69;
                                quanlentha = 4;
                                HT1 = new byte[]{0x2F};
                                str_line = "------------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------------".getBytes(), LF

                                };
                            }
                        }
                    } else {
                        if (str_print_ty.toString().equals("Generic")) {
//                            Toast.makeText(KOT_Management.this, "phi", Toast.LENGTH_SHORT).show();
                            setHT32 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 2"
                            setHT321 = new byte[]{0x1b, 0x44, 0x13, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x15, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                            setHT34 = new byte[]{0x1b, 0x44, 0x04, 0x12, 0x19, 0x00};//4 tabs 2"
                            setHTKOT = new byte[]{0x1b, 0x06, 0x44, 0x00};//2 tabs 2"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                            nPaperWidth = 384;
                            charlength = 25;
                            HT1 = new byte[]{0x09};
                            str_line = "--------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "--------------------------------".getBytes(), LF
                            };
                        }else {
                            if (str_print_ty.toString().equals("Epson/others")) {
//                            Toast.makeText(KOT_Management.this, "epson", Toast.LENGTH_SHORT).show();
                                setHT32 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 2"
                                setHT321 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x21, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                                setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x19, 0x21, 0x00};//4 tabs 2"
                                setHTKOT = new byte[]{0x1b, 0x09, 0x44, 0x00};//2 tabs 2"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                                nPaperWidth = 384;
                                charlength = 28;
                                HT1 = new byte[]{0x09};
                                str_line = "------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------".getBytes(), LF
                                };
                            }else {
                                if (str_print_ty.toString().equals("POS")) {
                                    setHT32 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT321 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT3212 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 3"
                                    setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x12, 0x21, 0x00};//4 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x05, 0x08, 0x00};//4 tabs 2"
                                    setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x08, 0x09, 0x00};//4 tabs 2"
                                    setHTKOT = new byte[]{0x1b, 0x05, 0x44, 0x00};//2 tabs 3"
                                    feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                    nPaperWidth = 384;
                                    charlength = 11;
                                    charlength1 = 22;
                                    charlength2 = 33;
                                    quanlentha = 3;
                                    HT1 = new byte[]{0x2F};
                                    str_line = "--------------------------------";
                                    allbufline = new byte[][]{
                                            left, un1, "--------------------------------".getBytes(), LF
                                    };
                                }
                            }
                        }
                    }
                } while (cc.moveToNext());
            }
            cc.close();

        }
        getprint_type.close();

        final int pageAreaHeight = 384;
        final int pageAreaWidth = 384;


        ArrayList<byte[]> list = new ArrayList<byte[]>();
        String method = "";
        String[] col = {"companylogo"};
        Cursor c = db.query("Logo", col, null, null, null, null, null);
        if (c.moveToFirst()) {
            byte[] img = c.getBlob(c.getColumnIndex("companylogo"));
//            logoData = BitmapFactory.decodeByteArray(img, 0, img.length);
        }
        c.close();

        if (mPrinter == null) {
            return false;
        }
        try {
            method = "addPageArea";
            mPrinter.addPageArea(0, 0, nPaperWidth, pageAreaHeight);

            method = "addPageDirection";
            mPrinter.addPageDirection(Printer.DIRECTION_TOP_TO_BOTTOM);

            method = "addPagePosition";
            mPrinter.addTextAlign(Printer.ALIGN_LEFT);
            method = "addImage";

            Cursor connnet = db.rawQuery("SELECT * FROM IPConn", null);
            if (connnet.moveToFirst()) {
                ipnamegets = connnet.getString(1);
                portgets = connnet.getString(2);
                statusnets = connnet.getString(3);
            }

            Cursor connnet_counter = db.rawQuery("SELECT * FROM IPConn_Counter", null);
            if (connnet_counter.moveToFirst()) {
                ipnamegets_counter = connnet_counter.getString(1);
                portgets_counter = connnet_counter.getString(2);
                statusnets_counter = connnet_counter.getString(3);
            }
            connnet_counter.close();

            Cursor connusb = db.rawQuery("SELECT * FROM BTConn", null);
            if (connusb.moveToFirst()) {
                addgets = connusb.getString(1);
                namegets = connusb.getString(2);
                statussusbs = connusb.getString(3);
            }

            textViewstatusnets.setText(statusnet);
            textViewstatusnets_counter.setText(statusnet_counter);
            textViewstatussusbs.setText(statussusbs);

            Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
            if (getcom.moveToFirst()) {
                do {
                    strcompanyname = getcom.getString(1);
                } while (getcom.moveToNext());
            }


            tvkot.setText(strcompanyname);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf1 = new byte[][]{
                        bold, cen, strcompanyname.getBytes(), LF, LF

                };
                if (textViewstatussusbs.getText().toString().equals("ok")) {
                    mPrinter.addCommand(bold);
                    mPrinter.addCommand(cen);
                    StringBuilder textData1 = new StringBuilder();
                    textData1.append(strcompanyname);
                    mPrinter.addText(textData1.toString());
                    mPrinter.addCommand(LF);
                    mPrinter.addCommand(LF); //LF
                }
            }


            allbufKOT = new byte[][]{
                    un, cen, "Cancel Order Ticket".getBytes(), LF
            };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(un);
                mPrinter.addCommand(cen);
                StringBuilder textData1 = new StringBuilder();
                textData1.append("Cancel Order Ticket");
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(LF); //LF
            }


            Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
            if (vbnm.moveToFirst()) {
                assa1 = vbnm.getString(1);
                assa2 = vbnm.getString(2);
            }
            TextView cx = new TextView(KOT_Management.this);
            cx.setText(assa1);
            String tablen = "";
            if (cx.getText().toString().equals("")) {
                tablen = "Tab" + assa2;
                allbuf11 = new byte[][]{
                        left, un1, setHT321, tablen.getBytes(), LF
                };
            } else {
                tablen = "Tab" + assa1;
                allbuf11 = new byte[][]{
                        left, un1, setHT321, tablen.getBytes(), LF
                };
            }

//        String tablen = "Table"+ItemIDtable;
//        allbuf11 = new byte[][]{
//                left,un1,setHT321, tablen.getBytes(),LF
//        };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(left);
                mPrinter.addCommand(un1);
                mPrinter.addCommand(setHT321);
                StringBuilder textData1 = new StringBuilder();
                textData1.append(tablen);
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(LF); //LF
            }


            allbuf10 = new byte[][]{
                    setHT321, left, date1.getBytes(), HT, "   ".getBytes(), time1.getBytes(), LF
            };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(setHT321);
                mPrinter.addCommand(left);
                StringBuilder textData1 = new StringBuilder();
                textData1.append(date1);
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(HT);
                StringBuilder textData2 = new StringBuilder();
                textData2.append("   ");
                mPrinter.addText(textData2.toString());
                StringBuilder textData3 = new StringBuilder();
                textData3.append(time1);
                mPrinter.addText(textData3.toString());
                mPrinter.addCommand(LF); //LF
            }

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(left);
                mPrinter.addCommand(un1);
                StringBuilder textData1 = new StringBuilder();
                textData1.append("KOT no. : Cancelled "+kot_no2);
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(LF); //LF
            }

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(left);
                mPrinter.addCommand(un1);
                StringBuilder textData1 = new StringBuilder();
                textData1.append("Reason  : "+reason);
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(LF); //LF
            }

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(left);
                mPrinter.addCommand(un1);
                StringBuilder textData1 = new StringBuilder();
                textData1.append(str_line);
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(LF); //LF
            }

            allbufqty = new byte[][]{
                    setHT3212, normal, "Item".getBytes(), HT, "-Qty".getBytes(), left, LF
            };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(setHT3212);
                mPrinter.addCommand(normal);
                StringBuilder textData1 = new StringBuilder();
                textData1.append("Item");
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(HT);
                StringBuilder textData2 = new StringBuilder();
                textData2.append("-Qty");
                mPrinter.addText(textData2.toString());
                mPrinter.addCommand(left); //LF
                mPrinter.addCommand(LF); //LF
            }

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(left);
                mPrinter.addCommand(un1);
                StringBuilder textData1 = new StringBuilder();
                textData1.append(str_line);
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(LF); //LF
            }

            Cursor cursor = db1.rawQuery("SELECT * FROM Kotcancelled WHERE kot_no = '"+kot_no2+"'", null);
            if (cursor.moveToFirst()) {
                do {
                    String itemname = cursor.getString(1);
                    String qty = cursor.getString(2);

                    allbufqty = new byte[][]{
                            setHT3212, normal, itemname.getBytes(), HT, "-Qty".getBytes(), left, LF
                    };
                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                        mPrinter.addCommand(setHT3212);
                        mPrinter.addCommand(normal);
                        StringBuilder textData1 = new StringBuilder();
                        textData1.append(itemname);
                        mPrinter.addText(textData1.toString());
                        mPrinter.addCommand(HT);
                        StringBuilder textData2 = new StringBuilder();
                        textData2.append("-"+qty);
                        mPrinter.addText(textData2.toString());
                        mPrinter.addCommand(left); //LF
                        mPrinter.addCommand(LF); //LF
                    }

                }while (cursor.moveToNext());
            }

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(left);
                mPrinter.addCommand(un1);
                StringBuilder textData1 = new StringBuilder();
                textData1.append(str_line);
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(LF); //LF
            }
            //feedcut();


            byte[][] allbuf = new byte[][]{
                    feedcut2
            };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                mPrinter.addCommand(feedcut2);
            }

            method = "addPageEnd";
            mPrinter.addPageEnd();

        } catch (Exception e) {
//            Toast.makeText(KOT_Management.this, "Here2", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, method, mContext);
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        Toast.makeText(KOT_Management.this, "back pressed", Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("table", ItemIDtable);
        myEdit.apply();

        Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
        if (cursor3.moveToFirst()) {
            String lite_pro = cursor3.getString(1);

            TextView tv = new TextView(KOT_Management.this);
            tv.setText(lite_pro);

            if (tv.getText().toString().equals("Lite")) {
                Intent intent = new Intent(KOT_Management.this, BeveragesMenuFragment_Dine_l.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(KOT_Management.this, BeveragesMenuFragment_Dine.class);
                startActivity(intent);
            }
        }else {
            Intent intent = new Intent(KOT_Management.this, BeveragesMenuFragment_Dine_l.class);
            startActivity(intent);
        }

//        Intent intent = new Intent(KOT_Management.this, BeveragesMenuFragment_Dine.class);
//        startActivity(intent);

    }
}
