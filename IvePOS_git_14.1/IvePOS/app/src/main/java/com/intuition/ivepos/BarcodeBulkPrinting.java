package com.intuition.ivepos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.*;
import com.epson.epos2.printer.Printer;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.intuition.ivepos.csv.RequestSingleton;
import com.intuition.ivepos.syncapp.StubProviderApp;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static com.intuition.ivepos.BluetoothPrintDriver.BT_Write;
import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

public class BarcodeBulkPrinting extends AppCompatActivity implements ReceiveListener {

    ListView myListView;
    Button getResult;
    SQLiteDatabase db = null;
    Uri contentUri,resultUri;
    Fragment frag;
    FragmentTransaction fragTransaction;

    String addget, nameget, statussusb, deviceget;

    byte[] setHT32, setHT321, setHT33, setHT34, setHT3212, setHTKOT, feedcut2, feed;
    int nPaperWidth;
    int charlength, charlength1, charlength2, quanlentha;
    byte[][] allbufline, allbuf1, allbuf2;
    String NAME;
    String ipnamegets, portgets, statusnets, addgets, namegets, statussusbs;
    String ipnamegets_counter, portgets_counter, statusnets_counter;
    String strcompanyname, straddress1,stritemname,strbarcodeno,strprice;
    TextView tvkot;
    private static int nBarcodetype, nStartOrgx, nBarcodeWidth = 1,
            nBarcodeHeight = 1, nBarcodeFontType, nBarcodeFontPosition = 2;
    private Button buttonBarcodetype, buttonStartOrgx, buttonBarcodeWidth,
            buttonBarcodeHeight, buttonBarcodeFontType,
            buttonBarcodeFontPosition;


    ArrayAdapter<Country_barcodebulk> adapter;
    ArrayList<Country_barcodebulk> list = new ArrayList<Country_barcodebulk>();
    ArrayAdapter<Country_barcodebulk_printing> adapter_dialog;
    ArrayList<Country_barcodebulk_printing> list_dialog = new ArrayList<Country_barcodebulk_printing>();

    String str_print_ty;

    private Context mContext = null;

    private Printer mPrinter = null;
    int barcodeWidth, barcodeHeight, pageAreaHeight, pageAreaWidth;

    private EditText mEditTarget = null;
    private Spinner mSpnSeries = null;
    private Spinner mSpnLang = null;
    Bitmap logoData, yourBitmap;

    private WifiPrintDriver wifiSocket = null;
    private WifiPrintDriver2 wifiSocket2 = null;

    String insert1_cc = "", insert1_rs = "", str_country;

    String WebserviceUrl;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_bulk);

        mContext = this;

        mSpnSeries = (Spinner) findViewById(R.id.spnModel);
        ArrayAdapter<SpnModelsItem> seriesAdapter = new ArrayAdapter<SpnModelsItem>(BarcodeBulkPrinting.this, android.R.layout.simple_spinner_item);
        seriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t82), com.epson.epos2.printer.Printer.TM_T82));
        mSpnSeries.setAdapter(seriesAdapter);
        mSpnSeries.setSelection(0);

        mSpnLang = (Spinner) findViewById(R.id.spnLang);
        ArrayAdapter<SpnModelsItem> langAdapter = new ArrayAdapter<SpnModelsItem>(BarcodeBulkPrinting.this, android.R.layout.simple_spinner_item);
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_ank), com.epson.epos2.printer.Printer.MODEL_ANK));
        mSpnLang.setAdapter(langAdapter);
        mSpnLang.setSelection(0);


//        try {
//            com.epson.epos2.Log.setLogSettings(mContext, com.epson.epos2.Log.PERIOD_TEMPORARY, com.epson.epos2.Log.OUTPUT_STORAGE, null, 0, 1, com.epson.epos2.Log.LOGLEVEL_LOW);
//        } catch (Exception e) {
////            Toast.makeText(BarcodeBulkPrinting.this, "Here8", Toast.LENGTH_SHORT).show();
//            ShowMsg.showException(e, "setLogSettings", mContext);
//        }
        mEditTarget = (EditText) findViewById(R.id.edtTarget);

        buttonBarcodetype = new Button(BarcodeBulkPrinting.this);
        buttonStartOrgx = new Button(BarcodeBulkPrinting.this);
        buttonBarcodeWidth = new Button(BarcodeBulkPrinting.this);
        buttonBarcodeHeight = new Button(BarcodeBulkPrinting.this);
        buttonBarcodeFontType = new Button(BarcodeBulkPrinting.this);
        buttonBarcodeFontPosition = new Button(BarcodeBulkPrinting.this);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(BarcodeBulkPrinting.this);
        String account_selection= sharedpreferences_select.getString("account_selection", null);

        if (account_selection.toString().equals("Dine")) {
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }else {
            if (account_selection.toString().equals("Qsr")) {
                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
            }else {
                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
            }
        }

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        crearYasignar(db);

        Cursor cursor_country = db.rawQuery("SELECT * FROM Country_Selection", null);
        if (cursor_country.moveToFirst()){
            str_country = cursor_country.getString(1);
        }

        if (str_country.toString().equals("Rupee") || str_country.toString().equals("India")) {
            insert1_cc = "\u20B9";
            insert1_rs = "Rs.";
        }else {
            if (str_country.toString().equals("Pound") || str_country.toString().equals("UK Pound")) {
                insert1_cc = "\u00a3";
                insert1_rs = "BP.";
            }else {
                if (str_country.toString().equals("Euro") || str_country.toString().equals("UK Euro")) {
                    insert1_cc = "\u20ac";
                    insert1_rs = "EU.";
                }else {
                    if (str_country.toString().equals("Dollar")) {
                        insert1_cc = "\u0024";
                        insert1_rs = "\u0024";
                    }else {
                        if (str_country.toString().equals("Dinar")) {
                            insert1_cc = "D";
                            insert1_rs = "KD.";
                        }else {
                            if (str_country.toString().equals("Shilling")) {
                                insert1_cc = "S";
                                insert1_rs = "S.";
                            }else {
                                if (str_country.toString().equals("Ringitt")) {
                                    insert1_cc = "R";
                                    insert1_rs = "RM.";
                                }else {
                                    if (str_country.toString().equals("Rial")) {
                                        insert1_cc = "R";
                                        insert1_rs = "OR.";
                                    }else {
                                        if (str_country.toString().equals("Yen")) {
                                            insert1_cc = "\u00a5";
                                            insert1_rs = "\u00a5";
                                        }else {
                                            if (str_country.toString().equals("Papua New Guinean")) {
                                                insert1_cc = "K";
                                                insert1_rs = "K.";
                                            }else {
                                                if (str_country.toString().equals("UAE")) {
                                                    insert1_cc = "D";
                                                    insert1_rs = "DH.";
                                                }else {
                                                    if (str_country.toString().equals("South African Rand")) {
                                                        insert1_cc = "R";
                                                        insert1_rs = "R.";
                                                    }else {
                                                        if (str_country.toString().equals("Congolese Franc")) {
                                                            insert1_cc = "F";
                                                            insert1_rs = "FC.";
                                                        }else {
                                                            if (str_country.toString().equals("Qatari Riyals")) {
                                                                insert1_cc = "QAR";
                                                                insert1_rs = "QAR.";
                                                            }else {
                                                                if (str_country.toString().equals("Dirhams")) {
                                                                    insert1_cc = "AED";
                                                                    insert1_rs = "AED.";
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
                        }
                    }
                }
            }
        }

        Cursor ccornn = db.rawQuery("SELECT * FROM BTConn", null);
        if (ccornn.moveToFirst()) {
            nameget = ccornn.getString(1);
            addget = ccornn.getString(2);
            statussusb = ccornn.getString(3);
            mEditTarget.setText(addget);
        }

        DownloadMusicfromInternet3 downloadMusicfromInternet = new DownloadMusicfromInternet3();
        downloadMusicfromInternet.execute();


        myListView = (ListView) findViewById(R.id.list);
        final EditText myFilter = (EditText) findViewById(R.id.searchView);

//        final Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE barcode_value IS NOT NULL", null);
//        String[] fromFieldNames = {"itemname"};
//        int[] toViewsID = {R.id.text1};
//        final SimpleCursorAdapter ddataAdapter = new SimpleCursorAdapter(BarcodeBulkPrinting.this, R.layout.itemslistview_checkbox, cursor, fromFieldNames, toViewsID, 0);
//        myListView.setAdapter(ddataAdapter);

        String statement = "SELECT * FROM Items WHERE barcode_value != ''";
        //Execute the query
        Cursor aallrows = db.rawQuery(statement, null);
        System.out.println("COUNT : " + aallrows.getCount());
        ////Toast.makeText(BarcodeBulkPrinting.this, "limit is a " + limit, Toast.LENGTH_SHORT).show();
        if (aallrows.moveToFirst()) {
            do {
                String ID = aallrows.getString(0);
                String NAme = aallrows.getString(1);
//                float  nmf = aallrows.getFloat(2);
//                String PlaCe = String.format("%.2f", nmf);
//                String QUaN = aallrows.getString(3);
//                String CaTe = aallrows.getString(4);
                String BAr = aallrows.getString(16);

                Country_barcodebulk NAME = new Country_barcodebulk(NAme);
                list.add(NAME);

            } while (aallrows.moveToNext());
        }
        aallrows.close();

        adapter = new MyAdapter(this,list);
        myListView.setAdapter(adapter);

//        ddataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
//
//            @Override
//            public Cursor runQuery(CharSequence constraint) {
//                String partialValue = constraint.toString();
//                return itemData.getAllSuggestedValues(partialValue);
//
//            }
//        });

        ImageView back_pressed = (ImageView) findViewById(R.id.back_pressed);
        back_pressed.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BarcodeBulkPrinting.this, MultiFragDatabaseActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
//
////        frag = new DatabaseFragment();
//////        hideKeyboard(getContext());
////        fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
////        fragTransaction.commit();
//////        donotshowKeyboard(getActivity());
            }
        });
        ImageView closetext = (ImageView) findViewById(R.id.delete_icon);
        closetext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                myFilter.setText("");
            }
        });

        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (count == 0){
//                    adapter = new MyAdapter(BarcodeBulkPrinting.this,list);
//                    myListView.setAdapter(adapter);
//                }else {
                adapter.getFilter().filter(s.toString());
//                }
//                Toast.makeText(BarcodeBulkPrinting.this, "1 "+s.toString(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(BarcodeBulkPrinting.this, "2 "+start, Toast.LENGTH_SHORT).show();
//                Toast.makeText(BarcodeBulkPrinting.this, "3 "+before, Toast.LENGTH_SHORT).show();
//                Toast.makeText(BarcodeBulkPrinting.this, "4 "+count, Toast.LENGTH_SHORT).show();

            }
        });

//        adapter.setFilterQueryProvider(new FilterQueryProvider() {
//            public Cursor runQuery(CharSequence constraint) {
//                return fetchCountriesByName(constraint.toString());
//            }
//        });

        final CheckBox chkAll =  (CheckBox) findViewById(R.id.chkAll);
        final ImageView display = (ImageView) findViewById(R.id.display);

        display.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE checked = 'checked'", null);
//                if (cursor1.moveToFirst()){
//                    do {
//                        String name = cursor1.getString(1);
//                        Toast.makeText(BarcodeBulkPrinting.this, " "+name, Toast.LENGTH_SHORT).show();
//                    }while (cursor1.moveToNext());
//                }
                ////print clicked design file
                final Dialog dialog = new Dialog(BarcodeBulkPrinting.this, R.style.timepicker_date_dialog);
                dialog.setContentView(R.layout.dialog_barcode_bulk_printing_confirm);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog.show();

                Cursor cursor2 = db.rawQuery("SELECT * FROM Items WHERE checked = 'checked'", null);
                int coun = cursor2.getCount();

                TextView tv = (TextView) dialog.findViewById(R.id.noofselected);
                tv.setText(String.valueOf(coun));

                EditText editText = (EditText) dialog.findViewById(R.id.search_selecteditem);

                final ListView listView = (ListView) dialog.findViewById(R.id.listview);

                final Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE checked = 'checked'", null);
                String[] fromFieldNames = {"itemname", "print_value"};
                int[] toViewsID = {R.id.selected_item, R.id.enter_value};
                final SimpleCursorAdapter ddataAdapterr = new SimpleCursorAdapter(BarcodeBulkPrinting.this, R.layout.dialog_listview_bulk_noofprints, cursor, fromFieldNames, toViewsID, 0);
                listView.setAdapter(ddataAdapterr);


//                String statement = "SELECT * FROM Items WHERE checked = 'checked'";
//                //Execute the query
//                Cursor aallrows = db.rawQuery(statement, null);
//                System.out.println("COUNT : " + aallrows.getCount());
//                ////Toast.makeText(BarcodeBulkPrinting.this, "limit is a " + limit, Toast.LENGTH_SHORT).show();
//                if (aallrows.moveToFirst()) {
//                    do {
//                        String ID = aallrows.getString(0);
//                        String NAme = aallrows.getString(1);
////                float  nmf = aallrows.getFloat(2);
////                String PlaCe = String.format("%.2f", nmf);
////                String QUaN = aallrows.getString(3);
////                String CaTe = aallrows.getString(4);
//                        String BAr = aallrows.getString(16);
//
//                        Country_barcodebulk_printing NAME = new Country_barcodebulk_printing(NAme);
//                        list_dialog.add(NAME);
//
//                    } while (aallrows.moveToNext());
//                }
//
//                adapter_dialog = new MyAdapter_bulk_printing(BarcodeBulkPrinting.this,list_dialog);
//                listView.setAdapter(adapter_dialog);

                editText.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        ddataAdapterr.getFilter().filter(s.toString());
                    }
                });

                ddataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
                    public Cursor runQuery(CharSequence constraint) {
                        return fetchCountriesByName1(constraint.toString());
                    }
                });

                ImageButton imageButton = (ImageButton) dialog.findViewById(R.id.btncancel);
                imageButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                final EditText noofprints = (EditText) dialog.findViewById(R.id.noofprints);
                Button enter = (Button) dialog.findViewById(R.id.enter);
                enter.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (noofprints.getText().toString().equals("")){

                        }else {
                            Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
                            if (cursor.moveToFirst()) {
                                do {
                                    String id = cursor.getString(0);
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("print_value", noofprints.getText().toString());
                                    String where1 = "_id = '" + id + "' ";

                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                    getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                                    resultUri = new Uri.Builder()
                                            .scheme("content")
                                            .authority(StubProviderApp.AUTHORITY)
                                            .path("Items")
                                            .appendQueryParameter("operation", "update")
                                            .appendQueryParameter("_id", id)
                                            .build();
                                    getContentResolver().notifyChange(resultUri, null);


                                   // db.update("Items", contentValues, where1, new String[]{});
                                    String where1_v1 = "docid = '" + id + "'";
                                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
                                } while (cursor.moveToNext());
                            }
                            cursor.close();

                            listView.setAdapter(null);
                            final Cursor cursor2 = db.rawQuery("SELECT * FROM Items WHERE checked = 'checked'", null);
                            String[] fromFieldNames = {"itemname", "print_value"};
                            int[] toViewsID = {R.id.selected_item, R.id.enter_value};
                            final SimpleCursorAdapter ddataAdapterr = new SimpleCursorAdapter(BarcodeBulkPrinting.this, R.layout.dialog_listview_bulk_noofprints, cursor2, fromFieldNames, toViewsID, 0);
                            listView.setAdapter(ddataAdapterr);

//                        for(int i=0; i < listView.getChildCount(); i++){
//                            RelativeLayout itemLayout = (RelativeLayout) listView.getChildAt(i);
//                            TextView cb = (TextView) itemLayout.findViewById(R.id.enter_value);
//                            cb.setText(noofprints.getText().toString());
//                        }
                        }
                    }
                });

                ImageButton printing = (ImageButton) dialog.findViewById(R.id.btnsave);
                printing.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Cursor connnett = db.rawQuery("SELECT * FROM IPConn", null);
                        if (connnett.moveToFirst()) {
                            ipnamegets = connnett.getString(1);
                            portgets = connnett.getString(2);
                            statusnets = connnett.getString(3);
                        }
                        connnett.close();

                        Cursor connnet_counter = db.rawQuery("SELECT * FROM IPConn_Counter", null);
                        if (connnet_counter.moveToFirst()) {
                            ipnamegets_counter = connnet_counter.getString(1);
                            portgets_counter = connnet_counter.getString(2);
                            statusnets_counter = connnet_counter.getString(3);
                        }
                        connnet_counter.close();

                        Cursor connusbb = db.rawQuery("SELECT * FROM BTConn", null);
                        if (connusbb.moveToFirst()) {
                            addgets = connusbb.getString(1);
                            namegets = connusbb.getString(2);
                            statussusbs = connusbb.getString(3);
                        }
                        connusbb.close();
                        if (statussusbs.toString().equals("ok") || statusnets_counter.toString().equals("ok") || statusnets.toString().equals("ok")) {

                            String dd = "";
                            TextView qazcvb = new TextView(BarcodeBulkPrinting.this);
                            Cursor cvonnusb = db.rawQuery("SELECT * FROM BTConn", null);
                            if (cvonnusb.moveToFirst()) {
                                addgets = cvonnusb.getString(1);
                                namegets = cvonnusb.getString(2);
                                statussusbs = cvonnusb.getString(3);
                                dd = cvonnusb.getString(4);
                            }
                            qazcvb.setText(dd);
                            if (qazcvb.getText().toString().equals("usb")) {
                                runPrintCouponSequence(dialog);
                            }else {
                                Cursor che = db.rawQuery("SELECT * FROM Items WHERE checked = 'checked'", null);
                                if (che.moveToFirst()) {
                                    do {
                                        String cbvalue = che.getString(18);
                                        String cbname = che.getString(1);

                                        Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + cbname + "'", null);
                                        if (cursor3.moveToFirst()) {
                                            String barvalue = cursor3.getString(16);

                                            for (int j = 0; j < Integer.parseInt(cbvalue); j++) {
                                                Cursor connnet = db.rawQuery("SELECT * FROM IPConn", null);
                                                if (connnet.moveToFirst()) {
                                                    ipnamegets = connnet.getString(1);
                                                    portgets = connnet.getString(2);
                                                    statusnets = connnet.getString(3);
                                                }
                                                connnet.close();

                                                Cursor connnet_counter1 = db.rawQuery("SELECT * FROM IPConn_Counter", null);
                                                if (connnet_counter1.moveToFirst()) {
                                                    ipnamegets_counter = connnet_counter1.getString(1);
                                                    portgets_counter = connnet_counter1.getString(2);
                                                    statusnets_counter = connnet_counter1.getString(3);
                                                }
                                                connnet_counter1.close();

                                                Cursor connusb = db.rawQuery("SELECT * FROM BTConn", null);
                                                if (connusb.moveToFirst()) {
                                                    addgets = connusb.getString(1);
                                                    namegets = connusb.getString(2);
                                                    statussusbs = connusb.getString(3);
                                                }
                                                connusb.close();

                                                //Toast.makeText(BarcodeBulkPrinting.this, "printbillonly one ", Toast.LENGTH_SHORT).show();
                                                byte[] setHT34M = {0x1b, 0x44, 0x04, 0x11, 0x19, 0x00};
                                                byte[] dotfeed = {0x1b, 0x4a, 0x15};
                                                byte[] HTRight = {0x1b, 0x61, 0x02, 0x09};
                                                byte[] HT = {0x09};
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

                                                tvkot = new TextView(BarcodeBulkPrinting.this);

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
                                                            setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x20, 0x29, 0x00};//4 tabs 3"
                                                            nPaperWidth = 576;
                                                            charlength = 23;
                                                            charlength1 = 46;
                                                            charlength2 = 69;
                                                            quanlentha = 5;
                                                            feed = new byte[]{0x1b, 0x64, 0x01};
                                                            str_line = "------------------------------------------";
                                                            allbufline = new byte[][]{
                                                                    left, un1, "------------------------------------------------".getBytes(), LF

                                                            };
                                                        } else {
//                                                        setHT32 = new byte[]{0x1b, 0x44, 0x16, 0x00};//2 tabs 2"
//                                                        setHT321 = new byte[]{0x1b, 0x44, 0x13, 0x00};//2 tabs 3"
//                                                        setHT3212 = new byte[]{0x1b, 0x44, 0x15, 0x00};//2 tabs 3"
//                                                        setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
//                                                        setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x12, 0x19, 0x00};//4 tabs 2"
//                                                        nPaperWidth = 384;
//                                                        charlength = 10;
//                                                        charlength1 = 20;
//                                                        charlength2 = 30;
//                                                        quanlentha = 5;
//                                                        feed = new byte[]{0x1b,0x64};
//                                                        allbufline = new byte[][]{
//                                                                left, un1, "--------------------------------".getBytes(), LF
//
//                                                        };
                                                            Cursor print_ty = db.rawQuery("SELECT * FROM Printer_type", null);
                                                            if (print_ty.moveToFirst()) {
                                                                str_print_ty = print_ty.getString(1);
                                                            }
                                                            if (str_print_ty.toString().equals("Generic")) {
//                                                            Toast.makeText(BarcodeBulkPrinting.this, "phi", Toast.LENGTH_SHORT).show();
                                                                setHT32 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 2"
                                                                setHT321 = new byte[]{0x1b, 0x44, 0x13, 0x00};//2 tabs 3"
                                                                setHT3212 = new byte[]{0x1b, 0x44, 0x15, 0x00};//2 tabs 3"
                                                                setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                                                                setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x12, 0x19, 0x00};//4 tabs 2"
                                                                nPaperWidth = 384;
                                                                charlength = 10;
                                                                charlength1 = 20;
                                                                charlength2 = 30;
                                                                quanlentha = 5;
                                                                str_line = "--------------------------------";
                                                                allbufline = new byte[][]{
                                                                        left, un1, "--------------------------------".getBytes(), LF

                                                                };
                                                            } else {
//                                                            Toast.makeText(BarcodeBulkPrinting.this, "epson", Toast.LENGTH_SHORT).show();
                                                                setHT32 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 2"
                                                                setHT321 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 3"
                                                                setHT3212 = new byte[]{0x1b, 0x44, 0x21, 0x00};//2 tabs 3"
                                                                setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                                                                setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x19, 0x21, 0x00};//4 tabs 2"
                                                                nPaperWidth = 384;
                                                                charlength = 16;
                                                                charlength1 = 32;
                                                                charlength2 = 48;
                                                                quanlentha = 5;
                                                                str_line = "------------------------------------------";
                                                                allbufline = new byte[][]{
                                                                        left, un1, "------------------------------------------".getBytes(), LF
                                                                };
                                                            }
                                                        }
                                                    } while (cc.moveToNext());
                                                }

                                                Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
                                                if (getcom.moveToFirst()) {
                                                    do {
                                                        strcompanyname = getcom.getString(1);
                                                        straddress1 = getcom.getString(14);
                                                    } while (getcom.moveToNext());
                                                }
                                                getcom.close();

                                                Cursor getcom1 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + cbname + "'", null);
                                                if (getcom1.moveToFirst()) {
                                                    do {
                                                        stritemname = getcom1.getString(1);
                                                        strbarcodeno = getcom1.getString(16);
                                                        strprice = getcom1.getString(2);
                                                    } while (getcom1.moveToNext());
                                                }
                                                getcom1.close();

                                                allbuf1 = new byte[][]{
                                                        dotfeed
                                                };
                                                if (statussusbs.toString().equals("ok")) {
                                                    BluetoothPrintDriver.BT_Write(dotfeed);    //
                                                } else {
                                                    if (statusnets_counter.toString().equals("ok")) {
                                                        wifiSocket2.WIFI_Write(dotfeed);    //
                                                    }else {
                                                        if (statusnets.toString().equals("ok")) {
                                                            wifiSocket.WIFI_Write(dotfeed);    //
                                                        }
                                                    }
                                                }

                                                tvkot.setText(strcompanyname);
                                                if (tvkot.getText().toString().equals("")) {
                                                    allbuf1 = new byte[][]{
                                                            left, bold, setHT321, "".getBytes(), LF
                                                    };
                                                    if (statussusbs.toString().equals("ok")) {
                                                        BluetoothPrintDriver.BT_Write(left);    //
                                                        BluetoothPrintDriver.BT_Write(bold);    //
                                                        BluetoothPrintDriver.BT_Write(setHT321);    //
                                                        BT_Write("");
                                                        BluetoothPrintDriver.BT_Write(LF);    //
                                                    } else {
                                                        if (statusnets_counter.toString().equals("ok")) {
                                                            wifiSocket2.WIFI_Write(left);    //
                                                            wifiSocket2.WIFI_Write(bold);    //
                                                            wifiSocket2.WIFI_Write(setHT321);    //
                                                            wifiSocket2.WIFI_Write("");
                                                            wifiSocket2.WIFI_Write(LF);    //
                                                        }else {
                                                            if (statusnets.toString().equals("ok")) {
                                                                wifiSocket.WIFI_Write(left);    //
                                                                wifiSocket.WIFI_Write(bold);    //
                                                                wifiSocket.WIFI_Write(setHT321);    //
                                                                wifiSocket.WIFI_Write("");
                                                                wifiSocket.WIFI_Write(LF);    //
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    int len = strcompanyname.length();

                                                    if (len >= 30) {
                                                        String string1 = strcompanyname.substring(0, 30);
                                                        allbuf1 = new byte[][]{
                                                                left, bold, setHT321, string1.getBytes(), "..".getBytes(), LF
                                                        };
                                                        if (statussusbs.toString().equals("ok")) {
                                                            BluetoothPrintDriver.BT_Write(left);    //
                                                            BluetoothPrintDriver.BT_Write(bold);    //
                                                            BluetoothPrintDriver.BT_Write(setHT321);    //
                                                            BT_Write(string1 + "..");
                                                            BluetoothPrintDriver.BT_Write(LF);    //
                                                        } else {
                                                            if (statusnets_counter.toString().equals("ok")) {
                                                                wifiSocket2.WIFI_Write(left);    //
                                                                wifiSocket2.WIFI_Write(bold);    //
                                                                wifiSocket2.WIFI_Write(setHT321);    //
                                                                wifiSocket2.WIFI_Write(string1 + "..");
                                                                wifiSocket2.WIFI_Write(LF);    //
                                                            }else {
                                                                if (statusnets.toString().equals("ok")) {
                                                                    wifiSocket.WIFI_Write(left);    //
                                                                    wifiSocket.WIFI_Write(bold);    //
                                                                    wifiSocket.WIFI_Write(setHT321);    //
                                                                    wifiSocket.WIFI_Write(string1 + "..");
                                                                    wifiSocket.WIFI_Write(LF);    //
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        allbuf1 = new byte[][]{
                                                                left, bold, setHT321, strcompanyname.getBytes(), LF
                                                        };
                                                        if (statussusbs.toString().equals("ok")) {
                                                            BluetoothPrintDriver.BT_Write(left);    //
                                                            BluetoothPrintDriver.BT_Write(bold);    //
                                                            BluetoothPrintDriver.BT_Write(setHT321);    //
                                                            BT_Write(strcompanyname);
                                                            BluetoothPrintDriver.BT_Write(LF);    //
                                                        } else {
                                                            if (statusnets_counter.toString().equals("ok")) {
                                                                wifiSocket2.WIFI_Write(left);    //
                                                                wifiSocket2.WIFI_Write(bold);    //
                                                                wifiSocket2.WIFI_Write(setHT321);    //
                                                                wifiSocket2.WIFI_Write(strcompanyname);
                                                                wifiSocket2.WIFI_Write(LF);    //
                                                            }else {
                                                                if (statusnets.toString().equals("ok")) {
                                                                    wifiSocket.WIFI_Write(left);    //
                                                                    wifiSocket.WIFI_Write(bold);    //
                                                                    wifiSocket.WIFI_Write(setHT321);    //
                                                                    wifiSocket.WIFI_Write(strcompanyname);
                                                                    wifiSocket.WIFI_Write(LF);    //
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                tvkot.setText(stritemname);
                                                if (tvkot.getText().toString().equals("")) {

                                                } else {

                                                    int len = stritemname.length();
                                                    if (len >= 19) {
                                                        String string1 = stritemname.substring(0, 19);
//                                                String string2 = stritemname.substring(charlength);
                                                        allbuf2 = new byte[][]{
                                                                setHT32, left, string1.getBytes(), "..".getBytes(), HT, "Rs".getBytes(), strprice.getBytes(), "/-".getBytes(), LF
                                                        };

                                                        if (statussusbs.toString().equals("ok")) {
                                                            BluetoothPrintDriver.BT_Write(setHT32);    //
                                                            BluetoothPrintDriver.BT_Write(left);    //
                                                            BT_Write(string1 + "..");
                                                            BluetoothPrintDriver.BT_Write(HT);    //
                                                            BT_Write(insert1_rs+"" + strprice + "/-");
                                                            BluetoothPrintDriver.BT_Write(LF);    //
                                                        } else {
                                                            if (statusnets_counter.toString().equals("ok")) {
                                                                wifiSocket2.WIFI_Write(setHT32);    //
                                                                wifiSocket2.WIFI_Write(left);    //
                                                                wifiSocket2.WIFI_Write(string1 + "..");
                                                                wifiSocket2.WIFI_Write(HT);    //
                                                                wifiSocket2.WIFI_Write(insert1_rs+"" + strprice + "/-");
                                                                wifiSocket2.WIFI_Write(LF);    //
                                                            }else {
                                                                if (statusnets.toString().equals("ok")) {
                                                                    wifiSocket.WIFI_Write(setHT32);    //
                                                                    wifiSocket.WIFI_Write(left);    //
                                                                    wifiSocket.WIFI_Write(string1 + "..");
                                                                    wifiSocket.WIFI_Write(HT);    //
                                                                    wifiSocket.WIFI_Write(insert1_rs+"" + strprice + "/-");
                                                                    wifiSocket.WIFI_Write(LF);    //
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        allbuf2 = new byte[][]{
                                                                setHT32, left, stritemname.getBytes(), HT, "Rs.".getBytes(), strprice.getBytes(), "/-".getBytes(), LF
                                                        };

                                                        if (statussusbs.toString().equals("ok")) {
                                                            BluetoothPrintDriver.BT_Write(setHT32);    //
                                                            BluetoothPrintDriver.BT_Write(left);    //
                                                            BT_Write(stritemname);
                                                            BluetoothPrintDriver.BT_Write(HT);    //
                                                            BT_Write(insert1_rs+"" + strprice + "/-");
                                                            BluetoothPrintDriver.BT_Write(LF);    //
                                                        } else {
                                                            if (statusnets_counter.toString().equals("ok")) {
                                                                wifiSocket2.WIFI_Write(setHT32);    //
                                                                wifiSocket2.WIFI_Write(left);    //
                                                                wifiSocket2.WIFI_Write(stritemname);
                                                                wifiSocket2.WIFI_Write(HT);    //
                                                                wifiSocket2.WIFI_Write(insert1_rs+"" + strprice + "/-");
                                                                wifiSocket2.WIFI_Write(LF);    //
                                                            }else {
                                                                if (statusnets.toString().equals("ok")) {
                                                                    wifiSocket.WIFI_Write(setHT32);    //
                                                                    wifiSocket.WIFI_Write(left);    //
                                                                    wifiSocket.WIFI_Write(stritemname);
                                                                    wifiSocket.WIFI_Write(HT);    //
                                                                    wifiSocket.WIFI_Write(insert1_rs+"" + strprice + "/-");
                                                                    wifiSocket.WIFI_Write(LF);    //
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                buttonBarcodetype.setText(getResources().getStringArray(
                                                        R.array.barcodetype)[nBarcodetype]);
                                                buttonStartOrgx.setText(getResources().getStringArray(
                                                        R.array.barcodestartorgx)[nStartOrgx]);
                                                buttonBarcodeWidth.setText(getResources().getStringArray(
                                                        R.array.barcodewidth)[nBarcodeWidth]);
                                                buttonBarcodeHeight.setText(getResources().getStringArray(
                                                        R.array.barcodeheight)[nBarcodeHeight]);
                                                buttonBarcodeFontType.setText(getResources().getStringArray(
                                                        R.array.barcodefonttype)[nBarcodeFontType]);
                                                buttonBarcodeFontPosition.setText(getResources().getStringArray(
                                                        R.array.barcodefontposition)[nBarcodeFontPosition]);

                                                tvkot.setText(strbarcodeno);
                                                if (tvkot.getText().toString().equals("")) {

                                                } else {
                                                    String strBarcode = strbarcodeno.toString();
                                                    int nOrgx = nStartOrgx * 12;
                                                    int nType = Cmd.Constant.BARCODE_TYPE_CODE128 + nBarcodetype;
                                                    int nWidthX = nBarcodeWidth + 1;
                                                    int nHeight = (nBarcodeHeight + 1) * 24;
                                                    int nHriFontType = nBarcodeFontType;
                                                    int nHriFontPosition = nBarcodeFontPosition;

                                                    if (statussusbs.toString().equals("ok")) {
                                                        try {

                                                            Bitmap bitmap = null;
                                                            ImageView iv = new ImageView(BarcodeBulkPrinting.this);

                                                            bitmap = encodeAsBitmap1(strBarcode, BarcodeFormat.CODE_128, 240, 48);
                                                            iv.setImageBitmap(bitmap);

                                                        } catch (WriterException e) {
                                                            e.printStackTrace();
                                                        }
                                                        Bundle data = new Bundle();
                                                        data.putString(Global.STRPARA1, strBarcode);
                                                        data.putInt(Global.INTPARA1, nOrgx);
                                                        data.putInt(Global.INTPARA2, nType);
                                                        data.putInt(Global.INTPARA3, nWidthX);
                                                        data.putInt(Global.INTPARA4, nHeight);
                                                        data.putInt(Global.INTPARA5, nHriFontType);
                                                        data.putInt(Global.INTPARA6, nHriFontPosition);
//                                                        DrawerService.workThread.handleCmd(Global.CMD_POS_SETBARCODE,
//                                                                data);
                                                    } else {
                                                        if (statusnets_counter.toString().equals("ok")) {

                                                            try {

                                                                Bitmap bitmap = null;
                                                                ImageView iv = new ImageView(BarcodeBulkPrinting.this);

                                                                bitmap = encodeAsBitmap2_counter(strBarcode, BarcodeFormat.CODE_128, 240, 48);
                                                                iv.setImageBitmap(bitmap);

                                                            } catch (WriterException e) {
                                                                e.printStackTrace();
                                                            }

                                                            Bundle data = new Bundle();
                                                            data.putString(Global.STRPARA1, strBarcode);
                                                            data.putInt(Global.INTPARA1, nOrgx);
                                                            data.putInt(Global.INTPARA2, nType);
                                                            data.putInt(Global.INTPARA3, nWidthX);
                                                            data.putInt(Global.INTPARA4, nHeight);
                                                            data.putInt(Global.INTPARA5, nHriFontType);
                                                            data.putInt(Global.INTPARA6, nHriFontPosition);
//                                                            DrawerService1.workThread.handleCmd(Global1.CMD_POS_SETBARCODE,
//                                                                    data);

                                                        }else {
                                                            if (statusnets.toString().equals("ok")) {

                                                                try {

                                                                    Bitmap bitmap = null;
                                                                    ImageView iv = new ImageView(BarcodeBulkPrinting.this);

                                                                    bitmap = encodeAsBitmap2(strBarcode, BarcodeFormat.CODE_128, 240, 48);
                                                                    iv.setImageBitmap(bitmap);

                                                                } catch (WriterException e) {
                                                                    e.printStackTrace();
                                                                }

                                                                Bundle data = new Bundle();
                                                                data.putString(Global.STRPARA1, strBarcode);
                                                                data.putInt(Global.INTPARA1, nOrgx);
                                                                data.putInt(Global.INTPARA2, nType);
                                                                data.putInt(Global.INTPARA3, nWidthX);
                                                                data.putInt(Global.INTPARA4, nHeight);
                                                                data.putInt(Global.INTPARA5, nHriFontType);
                                                                data.putInt(Global.INTPARA6, nHriFontPosition);
//                                                            DrawerService1.workThread.handleCmd(Global1.CMD_POS_SETBARCODE,
//                                                                    data);

                                                            }
                                                        }
                                                    }
                                                }

                                                allbuf1 = new byte[][]{
                                                        dotfeed, dotfeed, dotfeed, dotfeed
                                                };
                                                if (statussusbs.toString().equals("ok")) {
                                                    BluetoothPrintDriver.BT_Write(dotfeed);    //
                                                    BluetoothPrintDriver.BT_Write(dotfeed);    //
                                                    BluetoothPrintDriver.BT_Write(dotfeed);    //
                                                    BluetoothPrintDriver.BT_Write(dotfeed);    //
                                                } else {
                                                    if (statusnets_counter.toString().equals("ok")) {
                                                        wifiSocket2.WIFI_Write(dotfeed);    //
                                                        wifiSocket2.WIFI_Write(dotfeed);    //
                                                        wifiSocket2.WIFI_Write(dotfeed);    //
                                                        wifiSocket2.WIFI_Write(dotfeed);    //
                                                    }else {
                                                        if (statusnets.toString().equals("ok")) {
                                                            wifiSocket.WIFI_Write(dotfeed);    //
                                                            wifiSocket.WIFI_Write(dotfeed);    //
                                                            wifiSocket.WIFI_Write(dotfeed);    //
                                                            wifiSocket.WIFI_Write(dotfeed);    //
                                                        }
                                                    }
                                                }

                                            }
                                        }
                                        cursor3.close();

                                    } while (che.moveToNext());
                                }
                            }
                            dialog.dismiss();
                        }else {
                            Toast.makeText(BarcodeBulkPrinting.this, "printer not connected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                listView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long l) {
                        final Cursor cursor1 = (Cursor) parent.getItemAtPosition(position);
                        final String ItemID = cursor1.getString(cursor1.getColumnIndex("itemname"));

                        final TextView tv = (TextView) view.findViewById(R.id.enter_value);

//                        Toast.makeText(BarcodeBulkPrinting.this, "itemname "+ItemID+" "+tv.getText().toString(), Toast.LENGTH_SHORT).show();

                        final Dialog dialog1 = new Dialog(BarcodeBulkPrinting.this, R.style.timepicker_date_dialog);
                        dialog1.setContentView(R.layout.dialog_barcode_printing_value);
                        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialog1.show();

                        TextView tv1 = (TextView) dialog1.findViewById(R.id.itemname);
                        tv1.setText(ItemID);

                        final TextView tv2 = (TextView) dialog1.findViewById(R.id.editText1);
                        tv2.setText(tv.getText().toString());

//                        Button delete = (Button) dialog1.findViewById(R.id.btndelete);
//                        delete.setOnClickListener(new OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialog1.dismiss();
//                            }
//                        });

                        ImageButton delete1 = (ImageButton) dialog1.findViewById(R.id.btncancel);
                        delete1.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.dismiss();
                            }
                        });

                        Button save = (Button) dialog1.findViewById(R.id.btnsave);
                        save.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (tv2.getText().toString().equals("")){
                                    dialog1.dismiss();
                                }else {
                                    tv.setText(tv2.getText().toString());
                                    Cursor cursor5 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + ItemID + "'", null);
                                    if (cursor5.moveToFirst()) {
                                        do {
                                            String id = cursor5.getString(0);
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("print_value", tv2.getText().toString());
                                            String where1 = "_id = '" + id + "' ";

                                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                            getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                                            resultUri = new Uri.Builder()
                                                    .scheme("content")
                                                    .authority(StubProviderApp.AUTHORITY)
                                                    .path("Items")
                                                    .appendQueryParameter("operation", "update")
                                                    .appendQueryParameter("_id", id)
                                                    .build();
                                            getContentResolver().notifyChange(resultUri, null);
                                        //    db.update("Items", contentValues, where1, new String[]{});
                                            String where1_v1 = "docid = '" + id + "'";
                                            db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
                                        } while (cursor5.moveToNext());
                                    }
                                    cursor5.close();

                                    cursor1.moveToPosition(position);
                                    cursor1.requery();
                                    ddataAdapterr.notifyDataSetChanged();

                                    dialog1.dismiss();
//                                cursor.moveToPosition(position);
//                                cursor.requery();
//                                ddataAdapterr.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                });
            }
        });

//        chkAll.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (chkAll.isChecked()){
//                    for(int i=0; i < myListView.getChildCount(); i++){
//                        LinearLayout itemLayout = (LinearLayout)myListView.getChildAt(i);
//                        CheckedTextView cb = (CheckedTextView)itemLayout.findViewById(R.id.text1);
//                        cb.setChecked(true);
//
//
//                    }
//                    DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
//                    downloadMusicfromInternet.execute();
//                }else {
//                    for(int i=0; i < myListView.getChildCount(); i++){
//                        LinearLayout itemLayout = (LinearLayout)myListView.getChildAt(i);
//                        CheckedTextView cb = (CheckedTextView)itemLayout.findViewById(R.id.text1);
//                        cb.setChecked(false);
//                    }
//                    DownloadMusicfromInternet1 downloadMusicfromInternet = new DownloadMusicfromInternet1();
//                    downloadMusicfromInternet.execute();
//                }
//
//                for(int i=0; i < myListView.getChildCount(); i++){
//                    LinearLayout itemLayout = (LinearLayout)myListView.getChildAt(i);
//                    CheckedTextView cb = (CheckedTextView)itemLayout.findViewById(R.id.text1);
//                    if (cb.isChecked()){
//                        display.setVisibility(View.VISIBLE);
//                    }else {
//                        display.setVisibility(View.GONE);
//                    }
//                }
//
////                CheckBox chk = (CheckBox) v;
////                int itemCount = myListView.getCount();
////                for(int i=0 ; i < itemCount ; i++){
////                    myListView.setItemChecked(i, chk.isChecked());
////                }
//            }
//        });

        chkAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                View v;
                CheckBox chBox;
//                listView.performItemClick(listView.getAdapter().getView(3, null, null),
//                        3,
//                        listView.getAdapter().getItemId(3));
                if (chkAll.isChecked()){
                    for(int i=0; i < myListView.getCount(); i++){
                        v = myListView.getAdapter().getView(i, null, null);
//                        LinearLayout itemLayout = (LinearLayout)listView.getChildAt(i);
                        CheckBox cb = (CheckBox)v.findViewById(R.id.check);
                        cb.setChecked(true);
                        adapter.notifyDataSetChanged();
                    }
                    DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
                    downloadMusicfromInternet.execute();
                }else {
                    for(int i=0; i < myListView.getCount(); i++){
                        v = myListView.getAdapter().getView(i, null, null);
//                        LinearLayout itemLayout = (LinearLayout)listView.getChildAt(i);
                        CheckBox cb = (CheckBox)v.findViewById(R.id.check);
                        cb.setChecked(false);
                        adapter.notifyDataSetChanged();
                    }
                    DownloadMusicfromInternet1 downloadMusicfromInternet = new DownloadMusicfromInternet1();
                    downloadMusicfromInternet.execute();
                }

                int count = 0;
                int size = list.size();
                for (int i1=0; i1<size; i1++){
                    if (list.get(i1).isSelected()){
                        count++;
                    }
                }

                if(myListView.getCount()==count)
                    chkAll.setChecked(true);
                else
                    chkAll.setChecked(false);

                if (count >= 1){
                    display.setVisibility(View.VISIBLE);
                }else {
                    display.setVisibility(View.GONE);
                }
            }
        });


        /** Defining click event listener for the listitem checkbox */
//        myListView.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long l) {
//
//                final Cursor country = (Cursor) adapterView.getItemAtPosition(position);
//                String name = country.getString(country.getColumnIndex("itemname"));
////                String text = adapterView.getItemAtPosition(position).toString();
////                Toast.makeText(BarcodeBulkPrinting.this, " "+name, Toast.LENGTH_SHORT).show();
//
//                CheckedTextView cv = (CheckedTextView)view.findViewById(R.id.text1);
//                if (cv.isChecked()){
//                    cv.setChecked(false);
//                    Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE itemname = '"+name+"'", null);
//                    if (cursor1.moveToFirst()){
//                        String id = cursor1.getString(0);
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("checked", "");
//                        String where1 = "_id = '"+id+"' ";
//                        db.update("Items", contentValues, where1, new String[]{});
//                    }
//                }else {
//                    cv.setChecked(true);
//                    Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE itemname = '"+name+"'", null);
//                    if (cursor1.moveToFirst()){
//                        String id = cursor1.getString(0);
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("checked", "checked");
//                        String where1 = "_id = '"+id+"' ";
//                        db.update("Items", contentValues, where1, new String[]{});
//                    }
//                }
//
//                Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE checked = 'checked'", null);
//                int cou = cursor1.getCount();
//
//                if (cou >= 1){
//                    display.setVisibility(View.VISIBLE);
//                }else {
//                    display.setVisibility(View.GONE);
//                }
//
//                CheckBox chk = (CheckBox) findViewById(R.id.chkAll);
//                final int checkedItemCount = getCheckedItemCount();
//
//                if(myListView.getCount()==checkedItemCount)
//                    chk.setChecked(true);
//                else
//                    chk.setChecked(false);
//            }
//        });

        myListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long l) {

//                final Cursor country = (Cursor) adapterView.getItemAtPosition(position);
//                String name = country.getString(country.getColumnIndex("itemname"));
////                String text = adapterView.getItemAtPosition(position).toString();
////                Toast.makeText(BarcodeBulkPrinting.this, " "+name, Toast.LENGTH_SHORT).show();
//
//                CheckedTextView cv = (CheckedTextView)view.findViewById(R.id.text1);
//                if (cv.isChecked()){
//                    cv.setChecked(false);
//                    Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE itemname = '"+name+"'", null);
//                    if (cursor1.moveToFirst()){
//                        String id = cursor1.getString(0);
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("checked", "");
//                        String where1 = "_id = '"+id+"' ";
//                        db.update("Items", contentValues, where1, new String[]{});
//                    }
//                }else {
//                    cv.setChecked(true);
//                    Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE itemname = '"+name+"'", null);
//                    if (cursor1.moveToFirst()){
//                        String id = cursor1.getString(0);
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("checked", "checked");
//                        String where1 = "_id = '"+id+"' ";
//                        db.update("Items", contentValues, where1, new String[]{});
//                    }
//                }
//
//                Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE checked = 'checked'", null);
//                int cou = cursor1.getCount();
//
//                if (cou >= 1){
//                    display.setVisibility(View.VISIBLE);
//                }else {
//                    display.setVisibility(View.GONE);
//                }

                TextView txtview = (TextView) view.findViewById(R.id.label);
                final String item = txtview.getText().toString();

//                final Cursor country = (Cursor) adapterView.getItemAtPosition(position);
//                String name = country.getString(country.getColumnIndex("itemname"));

                final CheckBox checkbox = (CheckBox) view.getTag(R.id.check);

                if (checkbox.isChecked()){
                    checkbox.setChecked(false);
                    Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE itemname = '"+item+"'", null);
                    if (cursor1.moveToFirst()){
                        String id = cursor1.getString(0);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("checked", "");
                        String where1 = "_id = '"+id+"' ";


                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                        getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("Items")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id", id)
                                .build();
                        getContentResolver().notifyChange(resultUri, null);

                        String where1_v1 = "docid = '" + id + "'";
                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});


                        //    db.update("Items", contentValues, where1, new String[]{});
                    }
                    cursor1.close();
                }else {
                    checkbox.setChecked(true);
                    Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE itemname = '"+item+"'", null);
                    if (cursor1.moveToFirst()){
                        String id = cursor1.getString(0);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("checked", "checked");
                        String where1 = "_id = '"+id+"' ";



                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                        getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("Items")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id", id)
                                .build();
                        getContentResolver().notifyChange(resultUri, null);


                        String where1_v1 = "docid = '" + id + "'";
                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                        //       db.update("Items", contentValues, where1, new String[]{});
                    }
                    cursor1.close();
                }

                int count = 0;
                int size = list.size();
                for (int i1=0; i1<size; i1++){
                    if (list.get(i1).isSelected()){
                        count++;
                    }
                }

                if(myListView.getCount()==count)
                    chkAll.setChecked(true);
                else
                    chkAll.setChecked(false);

                if (count >= 1){
                    display.setVisibility(View.VISIBLE);
                }else {
                    display.setVisibility(View.GONE);
                }

                adapter.notifyDataSetChanged();
            }
        });

    }

    public Cursor fetchCountriesByName(String inputtext) throws SQLException {

        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
            mCursor = db.query("Items", new String[] {"_id","itemname"},
                    null, null, null, null, null);

        }
        else {
            mCursor = db.query(true, "Items", new String[] {"_id","itemname"},
                    "itemname" + " like" + " '%" + inputtext + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchCountriesByName1(String inputtext) throws SQLException {

        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
            mCursor = db.rawQuery("SELECT * FROM Items WHERE checked = 'checked'", null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Items WHERE itemname LIKE '%" + inputtext + "%' AND checked = 'checked' ", null);
        }

        return mCursor;
    }

    /**
     *
     * Returns the number of checked items
     */
    private int getCheckedItemCount(){
        int cnt = 0;
        SparseBooleanArray positions = myListView.getCheckedItemPositions();
        int itemCount = myListView.getCount();

        for(int i=0;i<itemCount;i++){
            if(positions.get(i))
                cnt++;
        }
        return cnt;
    }

    public void crearYasignar(SQLiteDatabase dbllega){
        try {
            dbllega.execSQL("CREATE TABLE if not exists Items (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, checked text);");
        }catch (SQLiteException e){
            alertas("Error desconocido: "+e.getMessage());
        }
    }

    public void alertas(String alerta) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(BarcodeBulkPrinting.this, R.style.AppTheme_NoActionBar2);
        AlertDialog.Builder builder = new AlertDialog.Builder(wrapper);
//        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle(R.string.app_name);
        builder.setMessage(alerta);
        builder.create().show();
    }

    class DownloadMusicfromInternet extends AsyncTask<String, Void, Integer>{

        private ProgressDialog dialog = new ProgressDialog(BarcodeBulkPrinting.this, R.style.timepicker_date_dialog);

        @Override
        protected Integer doInBackground(String... strings) {
            Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE barcode_value != ''", null);
            if (cursor1.moveToFirst()){
                do {
                    String id = cursor1.getString(0);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("checked", "checked");
                    String where1 = "_id = '"+id+"' ";



                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                    getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Items")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id", id)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);


                    String where1_v1 = "docid = '" + id + "'";
                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});



                    //        db.update("Items", contentValues, where1, new String[]{});
                }while (cursor1.moveToNext());
            }
            cursor1.close();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method

            dialog.setMessage("Loading");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Integer file_url) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();
            dialog.dismiss();
        }

    }

    class DownloadMusicfromInternet1 extends AsyncTask<String, Void, Integer>{

        private ProgressDialog dialog = new ProgressDialog(BarcodeBulkPrinting.this, R.style.timepicker_date_dialog);

        @Override
        protected Integer doInBackground(String... strings) {
            Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE barcode_value != ''", null);
            if (cursor1.moveToFirst()){
                do {
                    String id = cursor1.getString(0);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("checked", "");
                    String where1 = "_id = '"+id+"' ";




                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                    getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Items")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id", id)
                            .build();
                    getContentResolver().notifyChange(resultUri, null);

                    String where1_v1 = "docid = '" + id + "'";
                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});


                    //    db.update("Items", contentValues, where1, new String[]{});
                }while (cursor1.moveToNext());
            }
            cursor1.close();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method

            dialog.setMessage("Loading");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Integer file_url) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();
            dialog.dismiss();
        }

    }

    private List<Country_barcodebulk> getModel() {
        String statement = "SELECT * FROM Items";
        //Execute the query
        Cursor aallrows = db.rawQuery(statement, null);
        System.out.println("COUNT : " + aallrows.getCount());
        ////Toast.makeText(BarcodeBulkPrinting.this, "limit is a " + limit, Toast.LENGTH_SHORT).show();
        if (aallrows.moveToFirst()) {
            do {
                String ID = aallrows.getString(0);
                String NAme = aallrows.getString(1);
//                float  nmf = aallrows.getFloat(2);
//                String PlaCe = String.format("%.2f", nmf);
//                String QUaN = aallrows.getString(3);
//                String CaTe = aallrows.getString(4);
                String BAr = aallrows.getString(16);

                Country_barcodebulk NAME = new Country_barcodebulk(NAme);
                list.add(NAME);

            } while (aallrows.moveToNext());
        }
        return list;
    }

    class DownloadMusicfromInternet3 extends AsyncTask<Void, Integer, Void> {
        private ProgressDialog dialog = new ProgressDialog(BarcodeBulkPrinting.this, R.style.timepicker_date_dialog);

        @Override
        protected Void doInBackground(Void... params) {

            db.execSQL("UPDATE Items set checked = ''");
            db.execSQL("UPDATE Items set print_value = '0'");

            db.execSQL("UPDATE Items_Virtual set checked = ''");
            db.execSQL("UPDATE Items_Virtual set print_value = '0'");

            webservicequery("UPDATE Items set checked = ''");
            webservicequery("UPDATE Items set print_value = '0'");

//            Cursor cursor1 = db.rawQuery("SELECT * FROM Items", null);
//            if (cursor1.moveToFirst()){
//                do {
//                    String id = cursor1.getString(0);
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("checked", "");
//                    String where1 = "_id = '"+id+"' ";
//
//
//                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                    getContentResolver().update(contentUri, contentValues,where1,new String[]{});
//                    resultUri = new Uri.Builder()
//                            .scheme("content")
//                            .authority(StubProviderApp.AUTHORITY)
//                            .path("Items")
//                            .appendQueryParameter("operation", "update")
//                            .appendQueryParameter("_id", id)
//                            .build();
//                    getContentResolver().notifyChange(resultUri, null);
//
//                    String where1_v1 = "docid = '" + id + "'";
//                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});
//
//                //    db.update("Items", contentValues, where1, new String[]{});
//
//                    ContentValues contentValues1 = new ContentValues();
//                    contentValues1.put("print_value", "0");
//                    String where11 = "_id = '"+id+"' ";
//
//
//
//                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
//                    getContentResolver().update(contentUri, contentValues1,where11,new String[]{});
//                    resultUri = new Uri.Builder()
//                            .scheme("content")
//                            .authority(StubProviderApp.AUTHORITY)
//                            .path("Items")
//                            .appendQueryParameter("operation", "update")
//                            .appendQueryParameter("_id", id)
//                            .build();
//                    getContentResolver().notifyChange(resultUri, null);
//
//                    String where1_v11 = "docid = '" + id + "'";
//                    db.update("Items_Virtual", contentValues1, where1_v11, new String[]{});
//
//
//                    //     db.update("Items", contentValues1, where11, new String[]{});
//                }while (cursor1.moveToNext());
//            }
//            cursor1.close();

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);

            dialog.setMessage("Loading");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            dialog.setMax(1000);
            //Set the current progress to zero
            dialog.setProgress(0);
            //Display the progress dialog
//            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                @Override
//                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        //dialog.dismiss();
//                        //row.setBackgroundResource(0);
//                        return true;
//                    }
//                    return false;
//                }
//            });
            dialog.show();
        }


        @Override
        protected void onPostExecute(Void result) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();
            dialog.dismiss();

        }
    }
//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(BarcodeBulkPrinting.this, MultiFragDatabaseActivity.class);
//        startActivity(intent);
//
////        frag = new DatabaseFragment();
//////        hideKeyboard(getContext());
////        fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
////        fragTransaction.commit();
//////        donotshowKeyboard(getActivity());
//
//        super.onBackPressed();
//    }

    Bitmap encodeAsBitmap1(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        byte[] command = Utils.decodeBitmap(bitmap);
        printByteData(command);

        return bitmap;
    }


    Bitmap encodeAsBitmap2(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        byte[] command = Utils.decodeBitmap(bitmap);
        printByteData_wifi(command);

        return bitmap;
    }

    Bitmap encodeAsBitmap2_counter(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        byte[] command = Utils.decodeBitmap(bitmap);
        printByteData_wifi_counter(command);

        return bitmap;
    }

    public static void printByteData(byte[] buf) {
        BT_Write(buf);
        BT_Write(new byte[]{10});
    }

    public void printByteData_wifi(byte[] buf) {
        wifiSocket.WIFI_Write(buf);
        wifiSocket.WIFI_Write(new byte[]{10});
    }


    public void printByteData_wifi_counter(byte[] buf) {
        wifiSocket2.WIFI_Write(buf);
        wifiSocket2.WIFI_Write(new byte[]{10});
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    private boolean runPrintCouponSequence(Dialog dialog) {
        if (!initializeObject()) {
            return false;
        }

        if (!createCouponData(dialog)) {
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
            mPrinter = new com.epson.epos2.printer.Printer(((SpnModelsItem) mSpnSeries.getSelectedItem()).getModelConstant(),
                    ((SpnModelsItem) mSpnLang.getSelectedItem()).getModelConstant(),
                    mContext);
        } catch (Exception e) {
//            Toast.makeText(BarcodeBulkPrinting.this, "Here3", Toast.LENGTH_SHORT).show();
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
    public void onPtrReceive(final com.epson.epos2.printer.Printer printerObj, final int code, final PrinterStatusInfo status, final String printJobId) {
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

        if (status.getOnline() == com.epson.epos2.printer.Printer.FALSE) {
            msg += getString(R.string.handlingmsg_err_offline);
        }
        if (status.getConnection() == com.epson.epos2.printer.Printer.FALSE) {
            msg += getString(R.string.handlingmsg_err_no_response);
        }
        if (status.getCoverOpen() == com.epson.epos2.printer.Printer.TRUE) {
            msg += getString(R.string.handlingmsg_err_cover_open);
        }
        if (status.getPaper() == com.epson.epos2.printer.Printer.PAPER_EMPTY) {
            msg += getString(R.string.handlingmsg_err_receipt_end);
        }
        if (status.getPaperFeed() == com.epson.epos2.printer.Printer.TRUE || status.getPanelSwitch() == com.epson.epos2.printer.Printer.SWITCH_ON) {
            msg += getString(R.string.handlingmsg_err_paper_feed);
        }
        if (status.getErrorStatus() == com.epson.epos2.printer.Printer.MECHANICAL_ERR || status.getErrorStatus() == com.epson.epos2.printer.Printer.AUTOCUTTER_ERR) {
            msg += getString(R.string.handlingmsg_err_autocutter);
            msg += getString(R.string.handlingmsg_err_need_recover);
        }
        if (status.getErrorStatus() == com.epson.epos2.printer.Printer.UNRECOVER_ERR) {
            msg += getString(R.string.handlingmsg_err_unrecover);
        }
        if (status.getErrorStatus() == com.epson.epos2.printer.Printer.AUTORECOVER_ERR) {
            if (status.getAutoRecoverError() == com.epson.epos2.printer.Printer.HEAD_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_head);
            }
            if (status.getAutoRecoverError() == com.epson.epos2.printer.Printer.MOTOR_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_motor);
            }
            if (status.getAutoRecoverError() == com.epson.epos2.printer.Printer.BATTERY_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_battery);
            }
            if (status.getAutoRecoverError() == com.epson.epos2.printer.Printer.WRONG_PAPER) {
                msg += getString(R.string.handlingmsg_err_wrong_paper);
            }
        }
        if (status.getBatteryLevel() == com.epson.epos2.printer.Printer.BATTERY_LEVEL_0) {
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

        if (status.getPaper() == com.epson.epos2.printer.Printer.PAPER_NEAR_END) {
            warningsMsg += getString(R.string.handlingmsg_warn_receipt_near_end);
        }

        if (status.getBatteryLevel() == com.epson.epos2.printer.Printer.BATTERY_LEVEL_1) {
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
//                    Toast.makeText(BarcodeBulkPrinting.this, "Here6", Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(BarcodeBulkPrinting.this, "Here7", Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(BarcodeBulkPrinting.this, "Here9", Toast.LENGTH_SHORT).show();
                // Do nothing
            }
            return false;
        }

        try {
            mPrinter.sendData(com.epson.epos2.printer.Printer.PARAM_DEFAULT);
        } catch (Exception e) {
//            Toast.makeText(BarcodeBulkPrinting.this, "Here10", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, "sendData", mContext);
            try {
                mPrinter.disconnect();
            } catch (Exception ex) {
//                Toast.makeText(BarcodeBulkPrinting.this, "Here11", Toast.LENGTH_SHORT).show();
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

        if (status.getConnection() == com.epson.epos2.printer.Printer.FALSE) {
            return false;
        } else if (status.getOnline() == com.epson.epos2.printer.Printer.FALSE) {
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
            mPrinter.connect(mEditTarget.getText().toString(), com.epson.epos2.printer.Printer.PARAM_DEFAULT);
        } catch (Exception e) {
//            Toast.makeText(BarcodeBulkPrinting.this, "Here4", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, "connect", mContext);
            return false;
        }

        try {
            mPrinter.beginTransaction();
            isBeginTransaction = true;
        } catch (Exception e) {
//            Toast.makeText(BarcodeBulkPrinting.this, "Here12", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, "beginTransaction", mContext);
        }

        if (isBeginTransaction == false) {
            try {
                mPrinter.disconnect();
            } catch (Epos2Exception e) {
//                Toast.makeText(BarcodeBulkPrinting.this, "Here5", Toast.LENGTH_SHORT).show();
                // Do nothing
                return false;
            }
        }

        return true;
    }

    private boolean createCouponData(Dialog dialog) {

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

        Cursor cc = db.rawQuery("SELECT * FROM Printerreceiptsize", null);

        if(cc.moveToFirst()){
            cc.moveToFirst();
            do{
                NAME = cc.getString(1);
                if (NAME.equals("3 inch")) {
                    setHT32 = new byte[]{0x1b, 0x44, 0x29, 0x00};//2 tabs 3"
                    setHT321 = new byte[]{0x1b,0x44,0x23,0x00};//2 tabs 3"
                    setHT3212 = new byte[]{0x1b,0x44,0x25,0x00};//2 tabs 3"
                    setHT33 = new byte[]{0x1b,0x44,0x13,0x27,0x00};//3 tabs 3"
                    setHT34 = new byte[]{0x1b,0x44,0x06,0x20,0x29,0x00};//4 tabs 3"
                    nPaperWidth = 576;
                    charlength = 23;
                    charlength1 = 46;
                    charlength2 = 69;
                    quanlentha = 5;
                    str_line = "------------------------------------------------";
                    allbufline = new byte[][]{
                            left,un1, "------------------------------------------------".getBytes(), LF

                    };
                }
                else {
                    Cursor print_ty = db.rawQuery("SELECT * FROM Printer_type", null);
                    if (print_ty.moveToFirst()){
                        str_print_ty = print_ty.getString(1);
                    }
                    if (str_print_ty.toString().equals("Generic")) {
//                        Toast.makeText(Cash_Card_Credit_Sales1.this, "phi", Toast.LENGTH_SHORT).show();
                        setHT32 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 2"
                        setHT321 = new byte[]{0x1b, 0x44, 0x13, 0x00};//2 tabs 3"
                        setHT3212 = new byte[]{0x1b, 0x44, 0x15, 0x00};//2 tabs 3"
                        setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                        setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x12, 0x19, 0x00};//4 tabs 2"
                        nPaperWidth = 384;
                        charlength = 10;
                        charlength1 = 20;
                        charlength2 = 30;
                        quanlentha = 5;
                        str_line = "--------------------------------";
                        allbufline = new byte[][]{
                                left, un1, "--------------------------------".getBytes(), LF

                        };
                    }else {
//                        Toast.makeText(Cash_Card_Credit_Sales1.this, "epson", Toast.LENGTH_SHORT).show();
                        setHT32 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 2"
                        setHT321 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 3"
                        setHT3212 = new byte[]{0x1b, 0x44, 0x21, 0x00};//2 tabs 3"
                        setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                        setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x19, 0x21, 0x00};//4 tabs 2"
                        nPaperWidth = 384;
                        charlength = 16;
                        charlength1 = 32;
                        charlength2 = 48;
                        quanlentha = 5;
                        str_line = "------------------------------------------";
                        allbufline = new byte[][]{
                                left, un1, "------------------------------------------".getBytes(), LF
                        };
                    }
                }
            }while(cc.moveToNext());
        }

//        Cursor getprint_type = db1.rawQuery("SELECT * FROM Printer_text_size", null);
//        if (getprint_type.moveToFirst()) {
//            String type = getprint_type.getString(1);
//
//            Cursor cc = db1.rawQuery("SELECT * FROM Printerreceiptsize", null);
//
//            if (cc.moveToFirst()) {
//                cc.moveToFirst();
//                do {
//                    NAME = cc.getString(1);
//                    if (NAME.equals("3 inch")) {
//                        setHT32 = new byte[]{0x1b, 0x44, 0x29, 0x00};//2 tabs 3"
//                        setHT321 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 3"
//                        setHT3212 = new byte[]{0x1b, 0x44, 0x25, 0x00};//2 tabs 3"
//                        setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
//                        setHT34 = new byte[]{0x1b, 0x44, 0x08, 0x20, 0x29, 0x00};//4 tabs 3"
//                        setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 3"
//                        feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
//                        nPaperWidth = 576;
//                        charlength = 41;
//                        str_line = "------------------------------------------------";
//                        allbufline = new byte[][]{
//                                left, un1, "------------------------------------------------".getBytes(), LF
//
//                        };
//                    } else {
//
//                        Cursor print_ty = db1.rawQuery("SELECT * FROM Printer_type", null);
//                        if (print_ty.moveToFirst()){
//                            str_print_ty = print_ty.getString(1);
//                        }
//                        if (str_print_ty.toString().equals("Generic")) {
//                            Toast.makeText(Cash_Card_Credit_Sales1.this, "phi", Toast.LENGTH_SHORT).show();
//                            setHT32 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 2"
//                            setHT321 = new byte[]{0x1b, 0x44, 0x13, 0x00};//2 tabs 3"
//                            setHT3212 = new byte[]{0x1b, 0x44, 0x15, 0x00};//2 tabs 3"
//                            setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
//                            setHT34 = new byte[]{0x1b, 0x44, 0x04, 0x12, 0x19, 0x00};//4 tabs 2"
//                            setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 2"
//                            feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
//                            nPaperWidth = 384;
//                            charlength = 25;
//                            str_line = "--------------------------------";
//                            allbufline = new byte[][]{
//                                    left, un1, "--------------------------------".getBytes(), LF
//                            };
//                        }else {
//                            Toast.makeText(Cash_Card_Credit_Sales1.this, "epson", Toast.LENGTH_SHORT).show();
//                            setHT32 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 2"
//                            setHT321 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 3"
//                            setHT3212 = new byte[]{0x1b, 0x44, 0x21, 0x00};//2 tabs 3"
//                            setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
//                            setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x19, 0x21, 0x00};//4 tabs 2"
//                            setHTKOT = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
//                            feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
//                            nPaperWidth = 384;
//                            charlength = 28;
//                            str_line = "------------------------------------------";
//                            allbufline = new byte[][]{
//                                    left, un1, "------------------------------------------".getBytes(), LF
//                            };
//                        }
//                    }
//                } while (cc.moveToNext());
//            }
//
//        }

//        final int barcodeWidth = 2;
//        final int barcodeHeight = 64;
        final int pageAreaHeight = 384;
        final int pageAreaWidth = 384;
//        final int fontAHeight = 24;
//        final int fontAWidth = 12;
//        final int barcodeWidthPos = 110;
//        final int barcodeHeightPos = 70;

        tvkot = new TextView(BarcodeBulkPrinting.this);

        ArrayList<byte[]> list = new ArrayList<byte[]>();
        String method = "";
        String[] col = {"companylogo"};
        Cursor c = db.query("Logo", col, null, null, null, null, null);
        if (c.moveToFirst()) {
            byte[] img = c.getBlob(c.getColumnIndex("companylogo"));
            yourBitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        }

//        String method = "";
//        Bitmap coffeeData = BitmapFactory.decodeResource(getResources(), R.drawable.coffee);
//        Bitmap wmarkData = BitmapFactory.decodeResource(getResources(), R.drawable.wmark);

        if (mPrinter == null) {
            return false;
        }
        try{
//            method = "addPageBegin";
//            mPrinter.addPageBegin();

            method = "addPageArea";
            mPrinter.addPageArea(0, 0, nPaperWidth, pageAreaHeight);

            method = "addPageDirection";
            mPrinter.addPageDirection(com.epson.epos2.printer.Printer.DIRECTION_TOP_TO_BOTTOM);

            method = "addFeedLine";
            mPrinter.addFeedLine(1);
            method = "addPagePosition";
            mPrinter.addPagePosition(0, nPaperWidth);

//            // RECEIPT BODY//

            mPrinter.addCommand(LF);

//            method = "addPagePosition";
//            mPrinter.addPagePosition(0, wmarkData.getHeight());
//            mPrinter.addPagePosition(0, logoData.getScaledHeight(0));

//            method = "addImage";
//            mPrinter.addImage(wmarkData, 0, 0, wmarkData.getWidth(), wmarkData.getHeight(),
//                    Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT,
//                    Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT);
//Free coffee text

//            method = "addTextSize";
//            mPrinter.addTextSize(3, 3);
//            method = "addTextStyle";
//            mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.TRUE, Printer.PARAM_DEFAULT);
//            method = "addTextSmooth";
//            mPrinter.addTextSmooth(Printer.TRUE);
//            method = "addText";
//            mPrinter.addText("FREE Coffee\n");

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


            Cursor che = db.rawQuery("SELECT * FROM Items WHERE checked = 'checked'", null);
            if (che.moveToFirst()) {
                do {
                    String cbvalue = che.getString(18);
                    String cbname = che.getString(1);

                    Cursor cursor3 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + cbname + "'", null);
                    if (cursor3.moveToFirst()) {
                        String barvalue = cursor3.getString(16);

                        for (int j = 0; j < Integer.parseInt(cbvalue); j++) {

                            Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
                            if (getcom.moveToFirst()) {
                                do {
                                    strcompanyname = getcom.getString(1);
                                    straddress1 = getcom.getString(14);
                                } while (getcom.moveToNext());
                            }
                            getcom.close();

                            Cursor getcom1 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + cbname + "'", null);
                            if (getcom1.moveToFirst()) {
                                do {
                                    stritemname = getcom1.getString(1);
                                    strbarcodeno = getcom1.getString(16);
                                    strprice = getcom1.getString(2);
                                } while (getcom1.moveToNext());
                            }
                            getcom1.close();

                            allbuf1 = new byte[][]{
                                    dotfeed
                            };
                            if (statussusbs.toString().equals("ok")) {
                                mPrinter.addCommand(dotfeed);
                            }

                            tvkot.setText(strcompanyname);
                            if (tvkot.getText().toString().equals("")) {
                                allbuf1 = new byte[][]{
                                        left, bold, setHT321, "".getBytes(), LF
                                };
                                if (statussusbs.toString().equals("ok")) {
                                    mPrinter.addCommand(left);
                                    mPrinter.addCommand(bold);
                                    mPrinter.addCommand(setHT321);
                                    StringBuilder textData1 = new StringBuilder();
                                    textData1.append("");
                                    mPrinter.addText(textData1.toString());
                                    mPrinter.addCommand(LF); //LF
                                }
                            } else {
                                int len = strcompanyname.length();

                                if (len >= 30) {
                                    String string1 = strcompanyname.substring(0, 30);
                                    allbuf1 = new byte[][]{
                                            left, bold, setHT321, string1.getBytes(), "..".getBytes(), LF
                                    };
                                    if (statussusbs.toString().equals("ok")) {
                                        mPrinter.addCommand(left);
                                        mPrinter.addCommand(bold);
                                        mPrinter.addCommand(setHT321);
                                        StringBuilder textData1 = new StringBuilder();
                                        textData1.append(string1+"..");
                                        mPrinter.addText(textData1.toString());
                                        mPrinter.addCommand(LF); //LF
                                    }
                                } else {
                                    allbuf1 = new byte[][]{
                                            left, bold, setHT321, strcompanyname.getBytes(), LF
                                    };
                                    if (statussusbs.toString().equals("ok")) {
                                        mPrinter.addCommand(left);
                                        mPrinter.addCommand(bold);
                                        mPrinter.addCommand(setHT321);
                                        StringBuilder textData1 = new StringBuilder();
                                        textData1.append(strcompanyname);
                                        mPrinter.addText(textData1.toString());
                                        mPrinter.addCommand(LF); //LF
                                    }
                                }
                            }

                            tvkot.setText(stritemname);
                            if (tvkot.getText().toString().equals("")) {

                            } else {

                                int len = stritemname.length();
                                if (len >= 19) {
                                    String string1 = stritemname.substring(0, 19);
//                                                String string2 = stritemname.substring(charlength);
                                    allbuf2 = new byte[][]{
                                            setHT32, left, string1.getBytes(), "..".getBytes(), HT, "Rs".getBytes(), strprice.getBytes(), "/-".getBytes(), LF
                                    };

                                    if (statussusbs.toString().equals("ok")) {
                                        mPrinter.addCommand(setHT32);
                                        mPrinter.addCommand(left);
                                        StringBuilder textData1 = new StringBuilder();
                                        textData1.append(string1+"..");
                                        mPrinter.addText(textData1.toString());
                                        mPrinter.addCommand(HT);
                                        StringBuilder textData2 = new StringBuilder();
                                        textData2.append(insert1_rs+""+strprice+"/-");
                                        mPrinter.addText(textData2.toString());
                                        mPrinter.addCommand(LF); //LF
                                    }
                                } else {
                                    allbuf2 = new byte[][]{
                                            setHT32, left, stritemname.getBytes(), HT, "Rs.".getBytes(), strprice.getBytes(), "/-".getBytes(), LF
                                    };

                                    if (statussusbs.toString().equals("ok")) {
                                        mPrinter.addCommand(setHT32);
                                        mPrinter.addCommand(left);
                                        StringBuilder textData1 = new StringBuilder();
                                        textData1.append(stritemname);
                                        mPrinter.addText(textData1.toString());
                                        mPrinter.addCommand(HT);
                                        StringBuilder textData2 = new StringBuilder();
                                        textData2.append(insert1_rs+""+strprice+"/-");
                                        mPrinter.addText(textData2.toString());
                                        mPrinter.addCommand(LF); //LF
                                    }
                                }
                            }


                            tvkot.setText(strbarcodeno);
                            if (tvkot.getText().toString().equals("")) {

                            } else {


                                if (statussusbs.toString().equals("ok")) {


                                } else {
                                    if (statusnets.toString().equals("ok")) {



                                    }
                                }
                            }

                            mPrinter.addCommand(cen);
                            method = "addBarcode";

                            int barcodeWidth, barcodeHeight, pageAreaHeight1, pageAreaWidth1;

                            if (NAME.equals("3 inch")) {
                                barcodeWidth = 4;
                                barcodeHeight = 75;
                                pageAreaHeight1 = 384;
                                pageAreaWidth1 = 384;
                            } else {
                                barcodeWidth = 3;
                                barcodeHeight = 50;
                                pageAreaHeight1 = 384;
                                pageAreaWidth1 = 384;
                            }

                            byte[] dotfeed1 = {0x1b, 0x4a, 0x17};

                            tvkot.setText(strbarcodeno);
                            if (tvkot.getText().toString().equals("")) {

                            } else {

                                if (statussusbs.toString().equals("ok")) {

                                } else {
                                    if (statusnets.toString().equals("ok")) {

                                    }
                                }
                            }


//                            mPrinter.addCommand(dotfeed);


                            String extention = "{B";
                            String totalbarcod = extention + strbarcodeno;

                            mPrinter.addCommand(left);
                            mPrinter.addBarcode(totalbarcod,
                                    Printer.BARCODE_CODE128,
                                    Printer.HRI_BELOW,
                                    Printer.FONT_A,
                                    barcodeWidth,
                                    barcodeHeight);
                            mPrinter.addCommand(dotfeed);
                            mPrinter.addCommand(dotfeed);
                            mPrinter.addCommand(dotfeed);
                            mPrinter.addCommand(dotfeed);

                        }
                    }
                    cursor3.close();

                } while (che.moveToNext());
            }


        } catch (Exception e) {
//            Toast.makeText(BarcodeBulkPrinting.this, "Here2", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, method, mContext);
            return false;
        }

        return true;
    }

    public void webservicequery(final String webserviceQuery){


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(BarcodeBulkPrinting.this);
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
        RequestQueue queue;
        StringRequest sr1;
        // queue = Volley.newRequestQueue(getActivity());

        queue= RequestSingleton.getInstance(BarcodeBulkPrinting.this).getInstance();

        sr1 = new StringRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl + "webservicequery.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                               /*     params.put("email", email + "");
                                    params.put("password", password + "");*/


//                            final String email = prefs.getString("emailid", "");
//                            final String pwd = prefs.getString("password", "");
                params.put("device", device);
                params.put("store", store);
                params.put("company", company);
                params.put("data", webserviceQuery);
                return params;
            }
        };
    /*    sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        sr1.setRetryPolicy(new DefaultRetryPolicy(0, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr1);
    }

}