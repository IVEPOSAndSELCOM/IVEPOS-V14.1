package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 1/6/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.intuition.ivepos.csv.RequestSingleton;
import com.intuition.ivepos.languagelocale.LocaleUtils;
import com.intuition.ivepos.syncapp.StubProviderApp;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

/**
 * Created by Rohithkumar on 1/6/2015.
 */
public class DevicePreferenceActivity extends Fragment {


    ImageView viewImage;
    ImageButton b;

    Fragment frag;
    FragmentTransaction fragTransaction;

    public SQLiteDatabase db = null;
    private RadioGroup radioGroupWebsite, radioGroupWebsitecash, radioGroupWebsitebarcode;
    private RadioButton radioBtn1, radioBtncash, radioBtnscanner;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    ImageButton save_image;
    public Cursor cursor;
    EditText doorno, substreetname, streetname, cityname, statename, countryname, pinnumber, taxtwo, billtwo;
    SimpleCursorAdapter dataAdapter, dataadapter, dataAdapterr;
    ListView listview;


    EditText companyname, address1, address2, address3, phone, emailid, website, taxone, billone;
    TextView companynameget, address1get, address2get, address3get, phoneget, emailidget, websiteget, taxoneget, billoneget, datete, timeme;
    String strcompanyname, straddress1, straddress2, straddress3, strphone, stremailid, strwebsite, strtaxone, strbillone;

    String currentDateandTime, time1, NAME;
    RadioGroup radioGroupWebsitepaper;
    ImageView get_image; Bitmap b1;
    Bundle extras;
    byte[] img;
    private int selectedImagewidth, selectedImageheight;
    Uri selectedImageUri;
    final int PIC_CROP = 2;



    byte[][] allbuftaxestype1;

    Uri contentUri,resultUri;

    String WebserviceUrl;
    String account_selection;

    public DevicePreferenceActivity(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ApplicationData.getInstance().initAppLanguage(requireActivity());
        LocaleUtils.initialize(requireActivity(), LocaleUtils.getSelectedLanguageId(requireActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View rootview = inflater.inflate(R.layout.fragment_multi_billsettings, null);

        if (getActivity() instanceof AppCompatActivity){
            androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionbar.setSubtitle("Bill settings");
        }

        //final ActionBar actionBar = getActivity().getActionBar();

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(getActivity());
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

        //actionBar.setTitle(" Bill settings");


        SimpleDateFormat sdfdf = new SimpleDateFormat("dd MMM yy");
        currentDateandTime = sdfdf.format(new Date());

        Date dtt = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
        time1 = sdf1.format(dtt);

        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
        if (getcom.moveToFirst()) {
            do {
                strcompanyname = getcom.getString(1);
                straddress1 = getcom.getString(14);
                straddress2 = getcom.getString(17);
                straddress3 = getcom.getString(18);
                strphone = getcom.getString(2);
                stremailid = getcom.getString(15);
                strwebsite = getcom.getString(16);
                strtaxone = getcom.getString(10);
                strbillone = getcom.getString(12);

            } while (getcom.moveToNext());
        }



//        companynameget = (TextView)rootview.findViewById(R.id.compname);
//        if (strcompanyname.toString().equals("")){
//            companynameget.setText("Company name");
//        }else {
//            companynameget.setText(strcompanyname);
//        }
//        //companynameget.setText(strcompanyname);
//        address1get = (TextView)rootview.findViewById(R.id.address1);
//        if (straddress1.toString().equals("")){
//            address1get.setText("Address line 1");
//        }else {
//            address1get.setText(straddress1);
//        }
////                            address1get.setText(straddress1);
//        address2get = (TextView)rootview.findViewById(R.id.address2);
//        if (straddress2.toString().equals("")){
//            address2get.setText("Address line 2");
//        }else {
//            address2get.setText(straddress2);
//        }
//        //address2get.setText(straddress2);
//        address3get = (TextView)rootview.findViewById(R.id.address3);
//        if (straddress3.toString().equals("")){
//            address3get.setText("Address line 3");
//        }else {
//            address3get.setText(straddress3);
//        }
//        //address3get.setText(straddress3);
//        phoneget = (TextView)rootview.findViewById(R.id.phoneno);
//        if (strphone.toString().equals("")){
//            phoneget.setText("Phone no");
//        }else {
//            phoneget.setText(strphone);
//        }
//        //phoneget.setText(strphone);
//        emailidget = (TextView)rootview.findViewById(R.id.emailid);
//        if (stremailid.toString().equals("")){
//            emailidget.setText("E-mail id");
//        }else {
//            emailidget.setText(stremailid);
//        }
//        //emailidget.setText(stremailid);
//        websiteget = (TextView)rootview.findViewById(R.id.website);
//        if (strwebsite.toString().equals("")){
//            websiteget.setText("Website");
//        }else {
//            websiteget.setText(strwebsite);
//        }
//        //websiteget.setText(strwebsite);
//        taxoneget = (TextView)rootview.findViewById(R.id.taxlineone);
//        if (strtaxone.toString().equals("")){
//            taxoneget.setText("Website");
//        }else {
//            taxoneget.setText(strtaxone);
//        }
//        //taxoneget.setText(strtaxone);
//        billoneget = (TextView)rootview.findViewById(R.id.billfooterone);
//        if (strbillone.toString().equals("")){
//            billoneget.setText("Website");
//        }else {
//            billoneget.setText(strbillone);
//        }


        //billoneget.setText(strbillone);



        final Spinner print_size = (Spinner) rootview.findViewById(R.id.print_size);
        final String sel1 = print_size.getSelectedItem().toString();
//        Toast.makeText(getActivity(), "standard "+sel1, Toast.LENGTH_SHORT).show();

        Cursor getprint_type = db.rawQuery("SELECT * FROM Printer_text_size", null);
        if (getprint_type.moveToFirst()){
            String type = getprint_type.getString(1);

            if (type.toString().equals("Standard")){
//                Toast.makeText(getActivity(), "selected Standard", Toast.LENGTH_SHORT).show();
                print_size.setSelection(0);
            }else {
//                Toast.makeText(getActivity(), "selected Compact", Toast.LENGTH_SHORT).show();
                print_size.setSelection(1);
            }
        }


        print_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String sel = print_size.getSelectedItem().toString();
                if (sel.toString().equals("Standard")){
//                    Toast.makeText(getActivity(), "1 "+sel, Toast.LENGTH_SHORT).show();
//                    byte[] LF = {0x0d,0x0a};
//
//                    allbuftaxestype1 = new byte[][]{
//                            "".getBytes(),LF
//                    };
//
////                String encoding = "US-ASCII";
//
//                    if (DrawerService.workThread.isConnected()) {
//                        byte[] bufmodi = DataUtils.byteArraysToBytes(allbuftaxestype1);
//                        Bundle data = new Bundle();
////                    Bundle dataTextOut = new Bundle();
//                        data.putByteArray(Global.BYTESPARA1, bufmodi);
//                        data.putInt(Global.INTPARA1, 0);
//                        data.putInt(Global.INTPARA2, bufmodi.length);
//
////                    dataTextOut.putString(Global.STRPARA2, encoding);
////                    dataTextOut.putInt(Global.INTPARA1, 0);
////                    dataTextOut.putInt(Global.INTPARA4, 1);
//
////                    DrawerService.workThread.handleCmd(Global.CMD_POS_STEXTOUT,
////                            dataTextOut);
//                        DrawerService.workThread.handleCmd(Global.CMD_POS_WRITE, data);
//                    }
//
//                    int charset = 0, codepage = 0;
//                    String text = "";
//                    String encoding = "";
//                    byte[] addBytes = new byte[0];
////            if (arg0.getId() == R.id.standard) {
//                    text = "";
//                    encoding = "US-ASCII";
//                    charset = 0;
//                    codepage = 0;
////            }
//
//
//                    Bundle dataCP = new Bundle();
//                    Bundle dataLineHeight = new Bundle();
//                    Bundle dataTextOut = new Bundle();
//                    Bundle dataWrite = new Bundle();
//                    dataCP.putInt(Global.INTPARA1, charset);
//                    dataCP.putInt(Global.INTPARA2, codepage);
//                    dataLineHeight.putInt(Global.INTPARA1, 32);
//                    dataTextOut.putString(Global.STRPARA1, text);
//                    dataTextOut.putString(Global.STRPARA2, encoding);
//                    dataTextOut.putInt(Global.INTPARA1, 0);
//                    dataTextOut.putInt(Global.INTPARA4, 0);
//                    dataWrite.putByteArray(Global.BYTESPARA1, addBytes);
//                    dataWrite.putInt(Global.INTPARA1, 0);
//                    dataWrite.putInt(Global.INTPARA2, addBytes.length);
//
//                    DrawerService.workThread.handleCmd(
//                            Global.CMD_POS_SETCHARSETANDCODEPAGE, dataCP);
//                    DrawerService.workThread.handleCmd(
//                            Global.CMD_POS_SETLINEHEIGHT, dataLineHeight);
//                    DrawerService.workThread.handleCmd(Global.CMD_POS_STEXTOUT,
//                            dataTextOut);
//                    DrawerService.workThread.handleCmd(Global.CMD_POS_WRITE,
//                            dataWrite);

//                    if (DrawerService.workThread.isConnected()) {
//                        String encoding = "";
//                        encoding = "US-ASCII";
//                        String text = "";
//                        text = " ";
//                        byte[] bufq1 = DataUtils.byteArraysToBytes(allbuftaxestype1);
//                        Bundle data = new Bundle();
//                        Bundle dataTextOut = new Bundle();
//                        dataTextOut.putString(Global.STRPARA1, text);
//                        dataTextOut.putString(Global.STRPARA2, encoding);
//                        dataTextOut.putInt(Global.INTPARA1, 0);
//                        dataTextOut.putInt(Global.INTPARA4, 0);
//                        data.putByteArray(Global.BYTESPARA1, bufq1);
//                        data.putInt(Global.INTPARA1, 0);
//                        data.putInt(Global.INTPARA2, bufq1.length);
//                        DrawerService.workThread.handleCmd(Global.CMD_POS_STEXTOUT, dataTextOut);
//                        DrawerService.workThread.handleCmd(Global.CMD_POS_WRITE, data);
//                    }

                    Cursor getprint_type = db.rawQuery("SELECT * FROM Printer_text_size", null);
                    if (getprint_type.moveToFirst()){
                        String id = getprint_type.getString(0);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("type", "Standard");
                        String whereh1 = "_id = '" +id+ "' ";
                        db.update("Printer_text_size", contentValues, whereh1, new String[]{});
                    }

                }else {
//                    Toast.makeText(getActivity(), "2 "+sel, Toast.LENGTH_SHORT).show();
//
//                    byte[] LF = {0x0d,0x0a};
//
//                    allbuftaxestype1 = new byte[][]{
//                            "".getBytes(),LF
//                    };
//
////                String encoding = "US-ASCII";
//
//                    if (DrawerService.workThread.isConnected()) {
//                        byte[] bufmodi = DataUtils.byteArraysToBytes(allbuftaxestype1);
//                        Bundle data = new Bundle();
////                    Bundle dataTextOut = new Bundle();
//                        data.putByteArray(Global.BYTESPARA1, bufmodi);
//                        data.putInt(Global.INTPARA1, 0);
//                        data.putInt(Global.INTPARA2, bufmodi.length);
//
////                    dataTextOut.putString(Global.STRPARA2, encoding);
////                    dataTextOut.putInt(Global.INTPARA1, 0);
////                    dataTextOut.putInt(Global.INTPARA4, 1);
//
////                    DrawerService.workThread.handleCmd(Global.CMD_POS_STEXTOUT,
////                            dataTextOut);
//                        DrawerService.workThread.handleCmd(Global.CMD_POS_WRITE, data);
//                    }
//
//                    if (DrawerService.workThread.isConnected()) {
//                        int charset = 0, codepage = 0;
//                        String text = "";
//                        String encoding = "";
//                        byte[] addBytes = DataUtils.byteArraysToBytes(allbuftaxestype1);
////                        if (arg0.getId() == R.id.compact) {
////                            Toast.makeText(Test.this, "1", Toast.LENGTH_LONG).show();
//                            text = "";
//                            encoding = "US-ASCII";
//                            charset = 0;
//                            codepage = 0;
////                        addBytes = DataUtils.byteArraysToBytes(allbuftaxestype1);
////                        }
//
//
//                        Bundle dataCP = new Bundle();
//                        Bundle dataLineHeight = new Bundle();
//                        Bundle dataTextOut = new Bundle();
//                        Bundle dataWrite = new Bundle();
//                        dataCP.putInt(Global.INTPARA1, charset);
//                        dataCP.putInt(Global.INTPARA2, codepage);
//                        dataLineHeight.putInt(Global.INTPARA1, 26);
//                        dataTextOut.putString(Global.STRPARA1, text);
//                        dataTextOut.putString(Global.STRPARA2, encoding);
//                        dataTextOut.putInt(Global.INTPARA1, 0);
//                        dataTextOut.putInt(Global.INTPARA4, 1);
//                        dataWrite.putByteArray(Global.BYTESPARA1, addBytes);
//                        dataWrite.putInt(Global.INTPARA1, 0);
//                        dataWrite.putInt(Global.INTPARA2, addBytes.length);
//
//                        DrawerService.workThread.handleCmd(
//                                Global.CMD_POS_SETCHARSETANDCODEPAGE, dataCP);
//                        DrawerService.workThread.handleCmd(
//                                Global.CMD_POS_SETLINEHEIGHT, dataLineHeight);
//                        DrawerService.workThread.handleCmd(Global.CMD_POS_STEXTOUT,
//                                dataTextOut);
//                        DrawerService.workThread.handleCmd(Global.CMD_POS_WRITE,
//                                dataWrite);
//                    }

//                    if (DrawerService.workThread.isConnected()) {
//                        String text = "";
//                        text = " ";
//                        String encoding = "";
//                        encoding = "US-ASCII";
//                        byte[] bufq1 = DataUtils.byteArraysToBytes(allbuftaxestype1);
//                        Bundle data = new Bundle();
//                        Bundle dataTextOut = new Bundle();
//                        dataTextOut.putString(Global.STRPARA1, text);
//                        dataTextOut.putString(Global.STRPARA2, encoding);
//                        dataTextOut.putInt(Global.INTPARA1, 0);
//                        dataTextOut.putInt(Global.INTPARA4, 1);
//                        data.putByteArray(Global.BYTESPARA1, bufq1);
//                        data.putInt(Global.INTPARA1, 0);
//                        data.putInt(Global.INTPARA2, bufq1.length);
//                        DrawerService.workThread.handleCmd(Global.CMD_POS_STEXTOUT, dataTextOut);
//                        DrawerService.workThread.handleCmd(Global.CMD_POS_WRITE, data);
//                    }

                    Cursor getprint_type = db.rawQuery("SELECT * FROM Printer_text_size", null);
                    if (getprint_type.moveToFirst()){
                        String id = getprint_type.getString(0);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("type", "Compact");
                        String whereh1 = "_id = '" +id+ "' ";
                        db.update("Printer_text_size", contentValues, whereh1, new String[]{});
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        companynameget = (TextView)rootview.findViewById(R.id.compname);
        companynameget.setText(strcompanyname);

        address1get = (TextView)rootview.findViewById(R.id.address1);
        address1get.setText(straddress1);

        address2get = (TextView)rootview.findViewById(R.id.address2);
        address2get.setText(straddress2);

        address3get = (TextView)rootview.findViewById(R.id.address3);
        address3get.setText(straddress3);

        phoneget = (TextView)rootview.findViewById(R.id.phoneno);
        phoneget.setText(strphone);

        //phoneget.setText(strphone);
        emailidget = (TextView)rootview.findViewById(R.id.emailid);
        emailidget.setText(stremailid);

        //emailidget.setText(stremailid);
        websiteget = (TextView)rootview.findViewById(R.id.website);
        websiteget.setText(strwebsite);

        //websiteget.setText(strwebsite);
        taxoneget = (TextView)rootview.findViewById(R.id.taxlineone);
        taxoneget.setText(strtaxone);

        //taxoneget.setText(strtaxone);
        billoneget = (TextView)rootview.findViewById(R.id.billfooterone);
        billoneget.setText(strbillone);


        if (companynameget.getText().toString().equals("")){
            companynameget.setText("Company Name");
        }else {
            companynameget.setText(strcompanyname);
        }

        if (address1get.getText().toString().equals("")){
            address1get.setText("Address1");
        }else {
            address1get.setText(straddress1);
        }

        if (address2get.getText().toString().equals("")){
            address2get.setText("Address2");
        }else {
            address2get.setText(straddress2);
        }

        if (address3get.getText().toString().equals("")){
            address3get.setText("Address3");
        }else {
            address3get.setText(straddress3);
        }

        if (phoneget.getText().toString().equals("")){
            phoneget.setText("Phone No.");
        }else {
            phoneget.setText(strphone);
        }

        if (emailidget.getText().toString().equals("")){
            emailidget.setText("Email");
        }else {
            emailidget.setText(stremailid);
        }

        if (websiteget.getText().toString().equals("")){
            websiteget.setText("Website");
        }else {
            websiteget.setText(strwebsite);
        }

        if (taxoneget.getText().toString().equals("")){
            taxoneget.setText("Tax/License nos");
        }else {
            taxoneget.setText(strtaxone);
        }


        if (billoneget.getText().toString().equals("")){
            billoneget.setText("Bill Footer");
        }else {
            billoneget.setText(strbillone);
        }




        datete = (TextView)rootview.findViewById(R.id.date);
        datete.setText(currentDateandTime);
        timeme = (TextView)rootview.findViewById(R.id.time);
        timeme.setText(time1);

        get_image = (ImageView) rootview.findViewById(R.id.viewImagee);
        String[] col={"companylogo"};
        Cursor c=db.query("Logo", col, null, null, null, null, null);

        //if(c!=null){
        if (c.moveToFirst()) {
            do {
                img = c.getBlob(c.getColumnIndex("companylogo"));

                final Bitmap b1=BitmapFactory.decodeByteArray(img, 0, img.length);
                get_image.setImageBitmap(b1);

            } while (c.moveToNext());
        }else {

        }
        //}



        Button editbill = (Button)rootview.findViewById(R.id.edit_bill);
        editbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog1 = new Dialog(getActivity(), R.style.notitle);
                dialog1.setContentView(R.layout.dialog_bill_edit1);
                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//                ImageView close = (ImageView)dialog1.findViewById(R.id.closetext);
//                close.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog1.dismiss();
//                    }
//                });

                ImageView close1 = (ImageView)dialog1.findViewById(R.id.closedialog);
                close1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });

                Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
                if (getcom.moveToFirst()) {
                    do {
                        strcompanyname = getcom.getString(1);
                        straddress1 = getcom.getString(14);
                        straddress2 = getcom.getString(17);
                        straddress3 = getcom.getString(18);
                        strphone = getcom.getString(2);
                        stremailid = getcom.getString(15);
                        strwebsite = getcom.getString(16);
                        strtaxone = getcom.getString(10);
                        strbillone = getcom.getString(12);

                    } while (getcom.moveToNext());
                }

                SimpleDateFormat sdfdf = new SimpleDateFormat("dd MMM yy");
                final String currentDateandTime = sdfdf.format(new Date());

                Date dtt = new Date();
                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
                final String time1 = sdf1.format(dtt);


                companyname = (EditText)dialog1.findViewById(R.id.compnameedit);
                companyname.setText(strcompanyname);
//                            doorno = (EditText)dialog.findViewById(R.id.doorno);
//                            substreetname = (EditText)dialog.findViewById(R.id.sub_street);
//                            streetname = (EditText)dialog.findViewById(R.id.streetname);
//                            cityname = (EditText)dialog.findViewById(R.id.cityname);
//                            statename = (EditText)dialog.findViewById(R.id.statename);
//                            countryname = (EditText)dialog.findViewById(R.id.countryname);
//                            pinnumber = (EditText)dialog.findViewById(R.id.pinnumber);
                address1 = (EditText)dialog1.findViewById(R.id.addressedit1);
                address1.setText(straddress1);
                address2 = (EditText)dialog1.findViewById(R.id.addressedit2);
                address2.setText(straddress2);
                address3 = (EditText)dialog1.findViewById(R.id.addressedit3);
                address3.setText(straddress3);
                phone = (EditText)dialog1.findViewById(R.id.phonenoedit);
                phone.setText(strphone);
                emailid = (EditText)dialog1.findViewById(R.id.emailidedit);
                emailid.setText(stremailid);
                website = (EditText)dialog1.findViewById(R.id.websiteedit);
                website.setText(strwebsite);
                taxone = (EditText)dialog1.findViewById(R.id.taxlineoneedit);
                taxone.setText(strtaxone);
//                            taxtwo = (EditText)dialog1.findViewById(R.id.taxlinetwo);
                billone = (EditText)dialog1.findViewById(R.id.billfooteroneedit);
                billone.setText(strbillone);
//                            billtwo = (EditText)dialog1.findViewById(R.id.billfootertwo);

                radioGroupWebsitepaper = (RadioGroup) dialog1.findViewById(R.id.radios);


                get_image = (ImageView) dialog1.findViewById(R.id.viewImagee);
                String[] col={"companylogo"};
                Cursor c=db.query("Logo", col, null, null, null, null, null);
                //if(c!=null){
                if(c.moveToFirst()) {
                    do {
                        img = c.getBlob(c.getColumnIndex("companylogo"));

                        b1=BitmapFactory.decodeByteArray(img, 0, img.length);
                        get_image.setImageBitmap(b1);

                    } while (c.moveToNext());
                }else {

                }
                //}

                Button removelogo = (Button) dialog1.findViewById(R.id.remove);
                removelogo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.execSQL("delete from " + "Logo");
                        webservicequery("delete from Logo");
                        ContentValues cv = new ContentValues();
//                                  cv.put("_id", id);
                        final SQLiteDatabase syncdbapp=getActivity().openOrCreateDatabase("syncdbapp", Context.MODE_PRIVATE,null);
                        cv.put("tablename", "Logo");
                        cv.put("lastsyncedid", "0");
                        String where1 = "tablename = 'Logo'";
                        syncdbapp.update("appdata", cv, where1, new String[]{});


                        get_image.setImageBitmap(null);
                        get_image.setImageResource(R.drawable.print_logo);
                        selectedImagePath = null;
                        b1 = null;
                    }
                });

                get_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        // Start the Intent
                        startActivityForResult(galleryIntent, SELECT_PICTURE);

//                        Intent intent = new Intent();
//                        intent.setType("image/*");
//                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                        startActivityForResult(intent, 0);
                    }
                });

                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                cursor=db.rawQuery("SELECT * FROM Printerreceiptsize", null);

                String[] coll={"papersize"};
                Cursor cc=db.rawQuery("SELECT * FROM Printerreceiptsize", null);

                if(cc.moveToFirst()){
                    cc.moveToFirst();
                    do{
                        NAME = cc.getString(1);
                        if (NAME.equals("3 inch")) {
                            radioGroupWebsitepaper.check(R.id.btnthree);
                        }
                        else {
                            if (NAME.equals("A4")) {
                                radioGroupWebsitepaper.check(R.id.btnfour);
                            }else {
                                radioGroupWebsitepaper.check(R.id.btntwo);
                            }
                        }
                    }while(cc.moveToNext());
                }

//                radioGroupWebsitepaper.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                        final int selected = radioGroupWebsitepaper.getCheckedRadioButtonId();
//                        radioBtncash = (RadioButton) dialog1.findViewById(selected);
//                    }
//                });

                ImageView savedialod = (ImageView)dialog1.findViewById(R.id.savedetais);
                savedialod.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        savealldetails();

                        final int selected = radioGroupWebsitepaper.getCheckedRadioButtonId();
                        radioBtncash = (RadioButton) dialog1.findViewById(selected);

                        saveInDBpapersize();

                        dialog1.dismiss();
//                        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
//                                Context.MODE_PRIVATE, null);
                        byte[] img;
                        byte[] byteImage1 = null;
//                        String s = myDb.getPath();

                        db.execSQL("delete from " + "Logo");
                        webservicequery("delete from Logo");
                        ContentValues cv = new ContentValues();
//                                  cv.put("_id", id);
                        final SQLiteDatabase syncdbapp=getActivity().openOrCreateDatabase("syncdbapp", Context.MODE_PRIVATE,null);
                        cv.put("tablename", "Logo");
                        cv.put("lastsyncedid", "0");
                        String where1 = "tablename = 'Logo'";
                        syncdbapp.update("appdata", cv, where1, new String[]{});
                        ContentValues newValues = new ContentValues();

                        if (selectedImagePath != null){
                            //Toast.makeText(getActivity(), "Image path is not null.", Toast.LENGTH_SHORT).show();
                            Bitmap thePic = extras.getParcelable("data");
                            get_image.setImageBitmap(thePic);

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            thePic.compress(Bitmap.CompressFormat.PNG, 100, bos);
                            img = bos.toByteArray();
//                            newValues.put("_id", "1");
                            newValues.put("companylogo", img);
//                            db.insert("Logo", null, newValues);

                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Logo");
                            resultUri = getActivity().getContentResolver().insert(contentUri, newValues);
                            getActivity().getContentResolver().notifyChange(resultUri, null);
//                            final String webserviceQuery ="INSERT INTO `Logo`(`companylogo`) " + "VALUES ('"+img+"')";
//                            webservicequery(webserviceQuery);

                        }
                        else {
                            //Toast.makeText(getActivity(), "Image path is null.", Toast.LENGTH_SHORT).show();
                            //Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.solid_intuitionsoftwares_logo_new);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            if (b1 == null){

                            }else {
                                b1.compress(Bitmap.CompressFormat.PNG, 100, bos);
                                img = bos.toByteArray();
//                                newValues.put("_id", "1");
                                newValues.put("companylogo", img);
//                                myDb.insert("Logo", null, newValues);

                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Logo");
                                resultUri = getActivity().getContentResolver().insert(contentUri, newValues);
                                getActivity().getContentResolver().notifyChange(resultUri, null);
                            }


                        }
//                        myDb.close();

                        frag = new DevicePreferenceActivity();
                        hideKeyboard(getContext());
                        fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        fragTransaction.commit();

                    }
                });

                dialog1.show();
            }
        });

//        ImageView b=(ImageView)rootview.findViewById(R.id.btnselectgallery);
//
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(
//                        Intent.createChooser(intent, "Select Picture"),
//                        SELECT_PICTURE);
//            }
//        });
//
//        save_image = (ImageButton)rootview.findViewById(R.id.saveimage);
//        save_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveInDBlogo();
//            }
//        });
//
//        viewImage = (ImageView) rootview.findViewById(R.id.viewImagee);
//
//
//        companyname = (EditText)rootview.findViewById(R.id.compname);
//        doorno = (EditText)rootview.findViewById(R.id.doorno);
//        substreetname = (EditText)rootview.findViewById(R.id.sub_street);
//        streetname = (EditText)rootview.findViewById(R.id.streetname);
//        cityname = (EditText)rootview.findViewById(R.id.cityname);
//        statename = (EditText)rootview.findViewById(R.id.statename);
//        countryname = (EditText)rootview.findViewById(R.id.countryname);
//        pinnumber = (EditText)rootview.findViewById(R.id.pinnumber);
//        phone = (EditText)rootview.findViewById(R.id.phone);
//        taxone = (EditText)rootview.findViewById(R.id.taxlineone);
//        taxtwo = (EditText)rootview.findViewById(R.id.taxlinetwo);
//        billone = (EditText)rootview.findViewById(R.id.billfooterone);
//        billtwo = (EditText)rootview.findViewById(R.id.billfootertwo);
//
//        Button savecompanydetails = (Button)rootview.findViewById(R.id.savedetais);
//        savecompanydetails.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                savealldetails();
//            }
//        });


//        LinearLayout btnglobalpref = (LinearLayout)rootview.findViewById(R.id.buttonglobalpref);
//        LinearLayout btnnetworkpref = (LinearLayout)rootview.findViewById(R.id.buttonnetworkpref);
//        LinearLayout btndevicepref = (LinearLayout)rootview.findViewById(R.id.buttondevicepref);
//        LinearLayout btnquickaccess = (LinearLayout)rootview.findViewById(R.id.buttonquickaccess);
//        LinearLayout btninventory = (LinearLayout)rootview.findViewById(R.id.buttoninventory);


//        btndevicepref.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                actionBar.setTitle(" Bill settings");
//                frag = new DevicePreferenceActivity();
//                hideKeyboard(getContext());
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });
//
//        btnquickaccess.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                actionBar.setTitle(" Speed dial");
//                frag = new QuickAccessActivity();
//                hideKeyboard(getContext());
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });
//
//        btninventory.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                actionBar.setTitle(" Inventory");
//                frag = new InventoryActivity();
//                hideKeyboard(getContext());
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });
//
//
//        btnglobalpref.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                actionBar.setTitle(" Global Preference");
//                frag = new GlobalPreferenceActivity();
//                hideKeyboard(getContext());
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });
//
//        btnnetworkpref.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                actionBar.setTitle(" Network Preference");
//                frag = new NetworkPreferenceActivity();
//                hideKeyboard(getContext());
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });



//                            doorno = (EditText)dialog.findViewById(R.id.doorno);
//                            substreetname = (EditText)dialog.findViewById(R.id.sub_street);
//                            streetname = (EditText)dialog.findViewById(R.id.streetname);
//                            cityname = (EditText)dialog.findViewById(R.id.cityname);
//                            statename = (EditText)dialog.findViewById(R.id.statename);
//                            countryname = (EditText)dialog.findViewById(R.id.countryname);
//                            pinnumber = (EditText)dialog.findViewById(R.id.pinnumber);



//        db = getActivity().openOrCreateDatabase("mydb", Context.MODE_PRIVATE,null);
//        Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
//        if (getcom.moveToFirst()) {
//            do {
//                strcompanyname = getcom.getString(1);
//                straddress1 = getcom.getString(14);
//                straddress2 = getcom.getString(17);
//                straddress3 = getcom.getString(18);
//                strphone = getcom.getString(2);
//                stremailid = getcom.getString(15);
//                strwebsite = getcom.getString(16);
//                strtaxone = getcom.getString(10);
//                strbillone = getcom.getString(12);
//
//            } while (getcom.moveToNext());
//        }
//
//        companyname = (EditText)rootview.findViewById(R.id.compnameedit);
//        companyname.setText(strcompanyname);
//        address1 = (EditText)rootview.findViewById(R.id.addressedit1);
//        address1.setText(straddress1);
//        address2 = (EditText)rootview.findViewById(R.id.addressedit2);
//        address2.setText(straddress2);
//        address3 = (EditText)rootview.findViewById(R.id.addressedit3);
//        address3.setText(straddress3);
//        phone = (EditText)rootview.findViewById(R.id.phonenoedit);
//        phone.setText(strphone);
//        emailid = (EditText)rootview.findViewById(R.id.emailidedit);
//        emailid.setText(stremailid);
//        website = (EditText)rootview.findViewById(R.id.websiteedit);
//        website.setText(strwebsite);
//        taxone = (EditText)rootview.findViewById(R.id.taxlineoneedit);
//        taxone.setText(strtaxone);
////                            taxtwo = (EditText)rootview.findViewById(R.id.taxlinetwo);
//        billone = (EditText)rootview.findViewById(R.id.billfooteroneedit);
//        billone.setText(strbillone);
////                            billtwo = (EditText)rootview.findViewById(R.id.billfootertwo);


//        radioGroupWebsite = (RadioGroup) rootview.findViewById(R.id.radioprinter);
//        radioGroupWebsite.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                final int selected = radioGroupWebsite.getCheckedRadioButtonId();
//                radioBtn1 = (RadioButton) rootview.findViewById(selected);
//                saveInDB();
//            }
//        });
//
//
//        radioGroupWebsitecash = (RadioGroup) rootview.findViewById(R.id.radiocashdrawer);
//        radioGroupWebsitecash.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                final int selected =radioGroupWebsitecash.getCheckedRadioButtonId();
//                radioBtncash = (RadioButton) rootview.findViewById(selected);
//                saveInDBcash();
//            }
//        });
//
//
//        radioGroupWebsitebarcode = (RadioGroup) rootview.findViewById(R.id.radiobarcodescanner);
//        radioGroupWebsitebarcode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                final int selected =radioGroupWebsitebarcode.getCheckedRadioButtonId();
//                radioBtnscanner = (RadioButton) rootview.findViewById(selected);
//                saveInDBbarcode();
//            }
//        });



        try {

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
            //displayListView(rootview);
        }catch (SQLiteException e){
            alertas("Error inesperado: " + e.getMessage());
        }

        return rootview;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.

        inflater.inflate(R.menu.bill_pref, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_general:
                if (account_selection.toString().equals("Dine")) {
                    frag = new GlobalPreferenceActivity();
                    hideKeyboard(getContext());
                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                    fragTransaction.commit();
                }else {
                    if (account_selection.toString().equals("Qsr")) {
                        frag = new GlobalPreferenceActivity_Qsr();
                        hideKeyboard(getContext());
                        fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        fragTransaction.commit();
                    }else {
                        frag = new GlobalPreferenceActivity_Retail();
                        hideKeyboard(getContext());
                        fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        fragTransaction.commit();
                    }
                }
//                frag = new GlobalPreferenceActivity();
//                hideKeyboard(getContext());
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
                break;

//            case R.id.action_speeddial:
//                frag = new QuickAccessActivity();
//                hideKeyboard(getContext());
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//                break;


            case R.id.action_smartinvent:
                frag = new InventoryActivity();
                hideKeyboard(getContext());
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
                break;

//            case R.id.action_network:
//                frag = new NetworkPreferenceActivity();
//                hideKeyboard(getContext());
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void alertas(String alerta) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme);
        AlertDialog.Builder builder = new AlertDialog.Builder(wrapper);
        builder.setIcon(R.drawable.icon);
        builder.setTitle(R.string.app_name);
        builder.setMessage(alerta);
        builder.create().show();
    }


    void saveInDB() {
        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                Context.MODE_PRIVATE, null);
        myDb.execSQL("delete from " + "Printerconnectivity");          // clearing the table
        ContentValues newValues = new ContentValues();
        newValues.put("printercontype", radioBtn1.getText().toString());
        myDb.insert("Printerconnectivity", null, newValues);
        myDb.close();
//        Toast.makeText(getActivity().getBaseContext(),
//                "Image Saved in DB successfully.", Toast.LENGTH_SHORT).show();
    }


    void saveInDBcash() {
        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                Context.MODE_PRIVATE, null);
        myDb.execSQL("delete from " + "Cashdrawerconnectivity");          // clearing the table
        ContentValues newValues = new ContentValues();
        newValues.put("cashdrawercontype", radioBtncash.getText().toString());
        myDb.insert("Cashdrawerconnectivity", null, newValues);
        myDb.close();
//        Toast.makeText(getActivity().getBaseContext(),
//                "Image Saved in DB successfully.", Toast.LENGTH_SHORT).show();
    }


    void saveInDBbarcode() {
        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                Context.MODE_PRIVATE, null);
        myDb.execSQL("delete from " + "Barcodescannerconnectivity");          // clearing the table
        ContentValues newValues = new ContentValues();
        newValues.put("barcodescannercontype", radioBtnscanner.getText().toString());
        myDb.insert("Barcodescannerconnectivity", null, newValues);
        myDb.close();
//        Toast.makeText(getActivity().getBaseContext(),
//                "Image Saved in DB successfully.", Toast.LENGTH_SHORT).show();
    }



    void savealldetails() {
//        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
//                Context.MODE_PRIVATE, null);

        db.execSQL("delete from " + "Companydetailss");          // clearing the table
        webservicequery("delete from " + "Companydetailss");

        ContentValues newValues = new ContentValues();
        newValues.put("companyname", companyname.getText().toString());
        newValues.put("address1", address1.getText().toString());
        newValues.put("address2", address2.getText().toString());
        newValues.put("address3", address3.getText().toString());
        newValues.put("doorno", phone.getText().toString());//saving phonenumber in door(1169)
        //newValues.put("substreet", substreetname.getText().toString());
        //newValues.put("street", streetname.getText().toString());
        //newValues.put("city", cityname.getText().toString());
        //newValues.put("state", statename.getText().toString());
        //newValues.put("country", countryname.getText().toString());
        //newValues.put("pincode", pinnumber.getText().toString());
//        newValues.put("phoneno", phone.getText().toString());//removed bcoz of integer
        newValues.put("email", emailid.getText().toString());
        newValues.put("website", website.getText().toString());
        newValues.put("taxone", taxone.getText().toString());
        //newValues.put("taxtwo", taxtwo.getText().toString());
        newValues.put("footerone", billone.getText().toString());
        //newValues.put("footertwo", billtwo.getText().toString());

        db.insert("Companydetailss", null, newValues);

        final String webserviceQuery1="INSERT INTO `Companydetailss`(`companyname`, `address1`, `address2`, `address3`, `doorno`, `email`, `website`, `taxone`, `footerone`) " +
                "VALUES ('"+companyname.getText().toString()+"','"+address1.getText().toString()+"','"+address2.getText().toString()+"','"+address3.getText().toString()+"'," +
                "'"+phone.getText().toString()+"','"+emailid.getText().toString()+"','"+website.getText().toString()+"','"+taxone.getText().toString()+"','"+billone.getText().toString()+"')";

        webservicequery(webserviceQuery1);

//        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Companydetailss");
//        resultUri = getActivity().getContentResolver().insert(contentUri, newValues);
//        getActivity().getContentResolver().notifyChange(resultUri, null);

//        myDb.close();
        //Toast.makeText(LoginActivity.this.getBaseContext(),
        // "Image Saved in DB successfully.", Toast.LENGTH_SHORT).show();
    }

    void saveInDBpapersize() {
//        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
//                Context.MODE_PRIVATE, null);
        db.execSQL("delete from " + "Printerreceiptsize");          // clearing the table
        webservicequery("delete from Printerreceiptsize");

        ContentValues newValues = new ContentValues();
        newValues.put("papersize", radioBtncash.getText().toString());

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Printerreceiptsize");
        resultUri = getActivity().getContentResolver().insert(contentUri, newValues);
        getActivity().getContentResolver().notifyChange(resultUri, null);

//        myDb.insert("Printerreceiptsize", null, newValues);
//        myDb.close();
        //Toast.makeText(getActivity(), "Image Saved in DB successfully.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(getActivity(), "selecting image 2", Toast.LENGTH_SHORT).show();
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                performCrop();
                selectedImagePath = getPath(selectedImageUri);
                selectedImagewidth = getWidth(selectedImageUri);
                selectedImageheight = getHeight(selectedImageUri);
                //Toast.makeText(getActivity(), "image path is "+selectedImagePath, Toast.LENGTH_SHORT).show();
                System.out.println("Image Path : " + selectedImagePath);
//                get_image.setVisibility(View.VISIBLE);
//                get_image.setImageURI(selectedImageUri);
                get_image.setCropToPadding(true);
            }
            else if (requestCode == PIC_CROP){
                get_image.setVisibility(View.VISIBLE);
                get_image.setImageURI(selectedImageUri);
//                        Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
//                    if (requestCode == SELECT_PICTURE) {
//                        Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();
                //get the returned data
                extras = data.getExtras();
//get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                get_image.setImageBitmap(thePic);
//                    }else {
//                        Toast.makeText(getActivity(), "4", Toast.LENGTH_SHORT).show();
//                    }
            }else {
                selectedImageUri = data.getData();
                performCrop1();
                selectedImagePath = getPath(selectedImageUri);
                selectedImagewidth = getWidth(selectedImageUri);
                selectedImageheight = getHeight(selectedImageUri);
                //Toast.makeText(getActivity(), "image pathhhhhhhhhhhhhhhhhh is "+selectedImagePath, Toast.LENGTH_SHORT).show();
                System.out.println("Image Path : " + selectedImagePath);
                get_image.setVisibility(View.VISIBLE);
                get_image.setImageURI(selectedImageUri);
                get_image.setCropToPadding(true);
            }
        }else {
            selectedImagePath = null;
            selectedImageUri = null;
//                get_image.setImageURI(null);
            get_image.setCropToPadding(false);
        }
    }

    private void performCrop(){
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(selectedImageUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
//            cropIntent.putExtra("outputX", 576);
//            cropIntent.putExtra("outputY", 95);
            cropIntent.putExtra("aspectX", 6);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("scale", true);
            cropIntent.putExtra("scaleUpIfNeeded", true);
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void performCrop1(){
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(selectedImageUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("outputX", 106);
            cropIntent.putExtra("outputY", 60);
//            cropIntent.putExtra("aspectX", 16);
//            cropIntent.putExtra("aspectY", 9);
            cropIntent.putExtra("scale", true);
            cropIntent.putExtra("scaleUpIfNeeded", true);
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @SuppressWarnings("deprecation")
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public int getWidth(Uri width){
        //Toast.makeText(getActivity(), "selecting image 9", Toast.LENGTH_SHORT).show();
        String[] projection = { MediaStore.Images.Media.WIDTH };
        Cursor cursor = getActivity().managedQuery(width, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH);
        cursor.moveToFirst();
        return cursor.getInt(column_index);
    }

    public int getHeight(Uri height){
        //Toast.makeText(getActivity(), "selecting image 10", Toast.LENGTH_SHORT).show();
        String[] projection = { MediaStore.Images.Media.HEIGHT };
        Cursor cursor = getActivity().managedQuery(height, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT);
        cursor.moveToFirst();
        return cursor.getInt(column_index);
    }

    void setImage(byte[] byteImage2) {
        viewImage.setImageBitmap(BitmapFactory.decodeByteArray(byteImage2, 0,
                byteImage2.length));

    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void webservicequery(final String webserviceQuery){


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getContext());
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
        RequestQueue queue;
        StringRequest sr1;
        // queue = Volley.newRequestQueue(getActivity());

        queue= RequestSingleton.getInstance(getActivity()).getInstance();

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
