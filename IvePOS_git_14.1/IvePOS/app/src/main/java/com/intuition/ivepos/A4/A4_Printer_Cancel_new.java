package com.intuition.ivepos.A4;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.intuition.ivepos.R;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class A4_Printer_Cancel_new extends AppCompatActivity {

    Button btn_create_pdf;

    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;

    String billnumb, datee, timee;
    String billtypea, paymmethoda, billtypeaa, paymmethodaa;
    String NAME, tableidaa, tableida;
    String assa, assa1, assa2;

    String strcompanyname, straddress1, straddress2, straddress3, strphone, stremailid, strwebsite, strtaxone, strbillone;
    TextView tvkot;

    int charlength, charlength1, charlength2, quanlentha;

    TextView tv8, disc_tv;
    float ss;
    String ss1;
    float aqq1;
    String aqq2;

    String total_disc_print_q;

    String sub;
    TextView mTextView1, mTextView2;
    String taxpe, dsirs, subro, alltotal1;
    String total; float total1, discount;

    int i;
    float sub2a;

    String dsirs1, on1;

    String refundamounta;

    String str_round_off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a4_printer_layout);

        Bundle extras = getIntent().getExtras();
        billnumb = extras.getString("billnumber");
        tvkot = new TextView(A4_Printer_Cancel_new.this);

        charlength = 23;
        charlength1 = 46;
        charlength2 = 69;
        quanlentha = 5;

        db = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        db1 = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

//        btn_create_pdf = (Button) findViewById(R.id.btn_create_pdf);

        createPDFFile(Common.getAppPath(A4_Printer_Cancel_new.this)+"test_pdf.pdf");

//        Dexter.withActivity(this)
//                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse response) {
//                        btn_create_pdf.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                createPDFFile(Common.getAppPath(A4_Printer_Cancel_new.this)+"test_pdf.pdf");
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse response) {
//
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//
//                    }
//                })
//                .check();
    }

    public void createPDFFile(String path) {
        if (new File(path).exists())
            new File(path).delete();

        try {
            Document document = new Document();
            //save
            PdfWriter.getInstance(document, new FileOutputStream(path));
            //open to write
            document.open();

            //setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("EDMTDev");
            document.addCreator("Rohith");

            //Font setting
            BaseColor colorAccent = new BaseColor(0, 153, 204, 255);
            float fontSize = 11.0f;
            float valueFontSize = 26.0f;

            //BaseFont
            BaseFont fontname = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);

            //Create Title of Document
            Font titleFont = new Font(fontname, 13.0f, Font.NORMAL, BaseColor.BLACK);
            Font main_titleFont = new Font(fontname, 25.0f, Font.NORMAL, BaseColor.BLACK);

            //Add more
            Font orderNumberFont = new Font(fontname, fontSize, Font.NORMAL, colorAccent);

            Font orderNumberValueFont = new Font(fontname, valueFontSize, Font.NORMAL, BaseColor.BLACK);

            Cursor round_off_s = db1.rawQuery("SELECT * FROM Round_off WHERE _id = '1'", null);
            if (round_off_s.moveToFirst()){
                str_round_off = round_off_s.getString(1);
            }

            Cursor getcom = db1.rawQuery("SELECT * FROM Companydetailss", null);
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
            getcom.close();

            addNewItem(document, "Invoice", Element.ALIGN_RIGHT, main_titleFont);

            tvkot.setText(strtaxone);
            if (tvkot.getText().toString().equals("")) {

            } else {
                addNewItem(document, "GSTIN: "+strtaxone, Element.ALIGN_RIGHT, titleFont);
            }

            addLineSpace(document);
            addLineSpace(document);
            addNewItem(document, " ", Element.ALIGN_RIGHT, titleFont);
            addNewItem(document, " ", Element.ALIGN_RIGHT, titleFont);

            tvkot.setText(strcompanyname);
            if (tvkot.getText().toString().equals("")) {

            } else {
                addNewItem(document, strcompanyname, Element.ALIGN_RIGHT, titleFont);
            }

/////////
            tvkot.setText(straddress1);
            if (tvkot.getText().toString().equals("")) {

            } else {
                addNewItem(document, straddress1, Element.ALIGN_RIGHT, titleFont);
            }


            tvkot.setText(straddress2);
            if (tvkot.getText().toString().equals("")) {

            } else {
                addNewItem(document, straddress2, Element.ALIGN_RIGHT, titleFont);
            }


            tvkot.setText(straddress3);
            if (tvkot.getText().toString().equals("")) {

            } else {
                addNewItem(document, straddress3, Element.ALIGN_RIGHT, titleFont);
            }

            addLineSpace(document);
            addNewItem(document, " ", Element.ALIGN_RIGHT, titleFont);

            tvkot.setText(strphone);
            String pp = "Ph. " + strphone;
            if (tvkot.getText().toString().equals("")) {

            } else {
                addNewItem(document, strphone, Element.ALIGN_RIGHT, titleFont);
            }

            tvkot.setText(stremailid);
            if (tvkot.getText().toString().equals("")) {

            } else {
                addNewItem(document, stremailid, Element.ALIGN_RIGHT, titleFont);
            }

            tvkot.setText(strwebsite);
            if (tvkot.getText().toString().equals("")) {

            } else {
                addNewItem(document, strwebsite, Element.ALIGN_RIGHT, titleFont);
            }


            addLineSeperator_light(document);

            String nam = "",addr = "",phon = "",emai = "";
            Cursor caddress = db.rawQuery("SELECT * FROM Customerdetails WHERE billnumber = '" + billnumb + "'", null);
            if (caddress.moveToFirst()) {
                nam = caddress.getString(1);
                addr = caddress.getString(4);
                phon = caddress.getString(2);
                emai = caddress.getString(3);

//                if (nam.length() > 0 || addr.length() > 0 ||
//                        phon.length() > 0 || emai.length() > 0) {
//                    addNewItem(document, "Customer", Element.ALIGN_LEFT, titleFont);
//                }
//
//                if (nam.length() > 0) {
//                    addNewItem(document, nam, Element.ALIGN_LEFT, titleFont);
//                }
//
//                if (addr.length() > 0) {
//                    addNewItem(document, addr, Element.ALIGN_LEFT, titleFont);
//                }
//
//                if (phon.length() > 0) {
//                    addNewItem(document, "Ph. " + phon, Element.ALIGN_LEFT, titleFont);
//                }
//
//                if (emai.length() > 0) {
//                    addNewItem(document, emai, Element.ALIGN_LEFT, titleFont);
//                }
            }
            caddress.close();

            Cursor cursor10 = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billnumb + "'", null);
            if (cursor10.moveToFirst()) {
                billtypea = cursor10.getString(5);
                paymmethoda = cursor10.getString(6);
            }
            cursor10.close();

            addNewItemWithLeftAndRight(document, "BILL TO", "Invoice no. "+billnumb, titleFont, titleFont);

            TextView ttv = new TextView(A4_Printer_Cancel_new.this);
            ttv.setText(billtypea);

            TextView ttv1 = new TextView(A4_Printer_Cancel_new.this);
            ttv1.setText(paymmethoda);

//            if (ttv.getText().toString().equals("  Cash")) {
//                billtypeaa = "Cash";
//            } else {
//                billtypeaa = "Card";
//            }

            if (ttv.getText().toString().equals("  Cash")) {
                billtypeaa = "Cash"; //0
            }
            if (ttv.getText().toString().equals("  Card")) {
                billtypeaa = "Card"; //0
            }
            if (ttv.getText().toString().equals("  Paytm")) {
                billtypeaa = "Paytm"; //0
            }
            if (ttv.getText().toString().equals("  Mobikwik")) {
                billtypeaa = "Mobikwik"; //0
            }
            if (ttv.getText().toString().equals("  Freecharge")) {
                billtypeaa = "Freecharge"; //0
            }
            if (ttv.getText().toString().equals(getString(R.string.pay_later))) {
                billtypeaa = getString(R.string.pay_later_nospace); //0
            }
            if (ttv.getText().toString().equals("  Cheque")) {
                billtypeaa = "Cheque"; //0
            }
            if (ttv.getText().toString().equals("  Sodexo")) {
                billtypeaa = "Sodexo"; //0
            }
            if (ttv.getText().toString().equals("  Zeta")) {
                billtypeaa = "Zeta"; //0
            }
            if (ttv.getText().toString().equals("  Ticket")) {
                billtypeaa = "Ticket"; //0
            }
            if (ttv.getText().toString().equals("  Upiqr")) {
                billtypeaa = "upiqr"; //0
            }
            billtypeaa = ttv.getText().toString().replace(" ", "");

            if (ttv1.getText().toString().equals("  Dine-in") || ttv1.getText().toString().equals(getString(R.string.general)) || ttv1.getText().toString().equals("  Others")) {
                paymmethodaa = getString(R.string.pref_header_general);
                //billtypee.setText("Dine-in");
            } else {
                if (ttv1.getText().toString().equals("  Takeaway") || ttv1.getText().toString().equals("  Main")) {
                    paymmethodaa = "Takeaway";
                    //billtypee.setText("Takeaway");
                } else {
                    paymmethodaa = "Home delivery";
                    //billtypee.setText("Home delivery");
                }
            }

            Cursor date = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "'", null);
            if (date.moveToFirst()) {
                datee = date.getString(25);
                timee = date.getString(12);
            } else {
                Cursor date_cancel = db.rawQuery("Select * from All_Sales_cancelled WHERE bill_no = '" + billnumb + "'", null);
                if (date_cancel.moveToFirst()) {
                    datee = date_cancel.getString(22);
                    timee = date_cancel.getString(12);
                }
            }
            date.close();

            addNewItemWithLeftAndRight(document, nam, datee, titleFont, titleFont);
            addNewItemWithLeftAndRight(document, addr, timee, titleFont, titleFont);
            addNewItemWithLeftAndRight(document, phon, "", titleFont, titleFont);
            addNewItemWithLeftAndRight(document, emai, "", titleFont, titleFont);


            Cursor cursor9 = db.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billnumb + "'", null);
            if (cursor9.moveToFirst()) {
                tableida = cursor9.getString(15);
                Cursor vbnm = db1.rawQuery("SELECT * FROM asd1 WHERE _id = '" + tableida + "'", null);
                if (vbnm.moveToFirst()) {
                    assa1 = vbnm.getString(1);
                    assa2 = vbnm.getString(2);
                }
                vbnm.close();
                TextView cx = new TextView(A4_Printer_Cancel_new.this);
                cx.setText(assa1);

                if (cx.getText().toString().equals("")) {
                    tableidaa = "Tab" + assa2;

                } else {
                    tableidaa = "Tab" + assa1;

                }

            }
            cursor9.close();

//            addNewItemWithLeftAndRight(document, tableidaa, timee, titleFont, titleFont);





            addLineSeperator(document);

            addNewItemWithLeftAndRight(document, "Qty*Item", "Price/Amount", titleFont, titleFont);

            addLineSeperator(document);

            Cursor ccursorr = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (ccursorr.moveToFirst()) {

                do {

                    String name = ccursorr.getString(1);
                    String value = ccursorr.getString(2);
                    String pq = ccursorr.getString(5);
                    String itna = ccursorr.getString(2);
                    String pricee = ccursorr.getString(3);
                    String tototot = ccursorr.getString(4);

                    final String newid = ccursorr.getString(20);

                    final String newids = ccursorr.getString(0);

                    int padding_in_px;

                    int padding_in_dp = 30;  // 34 dps
                    final float scale1 = getResources().getDisplayMetrics().density;
                    padding_in_px = (int) (padding_in_dp * scale1 + 0.5f);


                    if (pq.equals(getString(R.string.action_item))) {
                        final TableRow row = new TableRow(A4_Printer_Cancel_new.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));


                        final TableRow row1 = new TableRow(A4_Printer_Cancel_new.this);
                        row1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));

                        final TableRow row2 = new TableRow(A4_Printer_Cancel_new.this);
                        row2.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                        //TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
                        final TableLayout tableLayout1 = new TableLayout(A4_Printer_Cancel_new.this);

                        TableRow.LayoutParams lp, lp1, lp2;

                        TextView tv = new TextView(A4_Printer_Cancel_new.this);
                        tv.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 0.70f));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tv.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                        //tv.setTextSize(18);
                        tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        //tv.setPadding(0, 0, 0, 0);
                        //text = ccursorr.getString(1);
                        tv.setText(value);
                        row.addView(tv);

                        TextView tv1 = new TextView(A4_Printer_Cancel_new.this);
                        tv1.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.6f));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv1.setGravity(Gravity.CENTER_VERTICAL);
                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        //tv1.setTextSize(15);
                        //tv.setPadding(0, 0, 0, 0);
                        //text = ccursorr.getString(1);
                        tv1.setText(name);
                        String value1 = tv1.getText().toString();
                        row.addView(tv1);

                        TextView tv2 = new TextView(A4_Printer_Cancel_new.this);
                        //lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 4.5f);
                        tv2.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.0f));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tv2.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        //tv2.append(value + "% " + name);
                        tv2.setText(pricee);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        //tv2.setTextColor(R.color.black);
                        row.addView(tv2);
                        TextView tv3 = new TextView(A4_Printer_Cancel_new.this);
                        //lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                        tv3.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                        //tv3.setPadding(5, 0, 0, 0);
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tv2.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        //tv2.setPadding(0, 0, 1, 0);
                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tv3.setText(tototot);
                        //tv3.setTextColor(R.color.black);
                        //row.addView(tv3);


                        String value2 = tv3.getText().toString();

                        Cursor modcursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND parent = '" + name + "' AND parentid = '" + newid + "' ", null);

                        if (modcursor.moveToFirst()) {

                            Cursor cursor4 = db.rawQuery("SELECT SUM(total) FROM All_Sales WHERE bill_no = '" + billnumb + "'AND parent = '" + name + "' AND parentid = '" + newid + "'", null);
                            if (cursor4.moveToFirst()) {
                                sub2a = cursor4.getFloat(0);
                                String sub2a1 = String.valueOf(sub2a);
                                ss = Float.parseFloat(sub2a1) + Float.parseFloat(tototot);
                                ss1 = String.format("%.1f", ss);
                            }

                            if (name.toString().length() > charlength) {
                                int print1 = 0;

                                if (value.length() > quanlentha && name.toString().length() > charlength) {
                                    String string1quan = value.substring(0, quanlentha);
                                    String string2quan = value.substring(quanlentha);
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength);

                                    if (print1 == 0) {

                                        addNewItemWithLeftAndRight(document, value+"*"+name, pricee+"/"+ss1, titleFont, titleFont);
                                        print1 = 1;
                                    }

                                }
                                if (value.length() <= quanlentha && name.toString().length() > charlength) {
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength);


                                    if (print1 == 0) {

                                        addNewItemWithLeftAndRight(document, value+"*"+name, pricee+"/"+ss1, titleFont, titleFont);
                                        print1 = 1;
                                    }

                                }

/////////////////////////////////////////
                                if (value.length() > quanlentha && name.toString().length() > charlength1) {
                                    String string1quan = value.substring(0, quanlentha);
                                    String string2quan = value.substring(quanlentha);
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength, charlength1);
                                    String string3 = name.substring(charlength1);

                                    if (print1 == 0) {
                                        addNewItemWithLeftAndRight(document, value + "*" + name, pricee + "/" + ss1, titleFont, titleFont);
                                        print1 = 1;
                                    }

                                }
                                if (value.length() <= quanlentha && name.toString().length() > charlength1) {
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength, charlength1);
                                    String string3 = name.substring(charlength1);

                                    if(print1 == 0) {
                                        addNewItemWithLeftAndRight(document, value + "*" + name, pricee + "/" + ss1, titleFont, titleFont);
                                        print1 = 1;
                                    }

                                }

                                Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                if (ccursor.moveToFirst()) {
                                    String hsn = ccursor.getString(34);

                                    TextView hsn_hsn = new TextView(A4_Printer_Cancel_new.this);
                                    hsn_hsn.setText(hsn);

                                    if (hsn_hsn.getText().toString().equals("")) {
                                    } else {
                                        if (hsn_hsn.getText().toString().equals("")) {
                                        } else {
                                            addNewItem(document, hsn, Element.ALIGN_LEFT, titleFont);
                                        }
                                    }
                                }

                            } else {
                                if (value.toString().length() > quanlentha) {
                                    String string1quan = value.substring(0, quanlentha);
                                    String string2quan = value.substring(quanlentha);


                                    addNewItemWithLeftAndRight(document, value+"*"+name, pricee+"/"+ss1, titleFont, titleFont);

                                    Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                    if (ccursor.moveToFirst()) {
                                        String hsn = ccursor.getString(34);

                                        TextView hsn_hsn = new TextView(A4_Printer_Cancel_new.this);
                                        hsn_hsn.setText(hsn);

                                        if (hsn_hsn.getText().toString().equals("")) {
                                        } else {
                                            addNewItem(document, hsn, Element.ALIGN_LEFT, titleFont);
                                        }
                                    }

                                } else {
                                    addNewItemWithLeftAndRight(document, value+"*"+name, pricee+"/"+ss1, titleFont, titleFont);

                                    Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                    if (ccursor.moveToFirst()) {
                                        String hsn = ccursor.getString(34);

                                        TextView hsn_hsn = new TextView(A4_Printer_Cancel_new.this);
                                        hsn_hsn.setText(hsn);

                                        if (hsn_hsn.getText().toString().equals("")) {
                                        } else {
                                            addNewItem(document, hsn, Element.ALIGN_LEFT, titleFont);
                                        }
                                    }

                                }

                            }

                            do {

                                final String modiname = modcursor.getString(1);
                                final String modiquan = modcursor.getString(2);
                                String modiprice = modcursor.getString(3);
                                String moditotal = modcursor.getString(4);
                                final String modiid = modcursor.getString(0);

                                float modprice1 = Float.parseFloat(modiprice);
                                String modpricestr = String.valueOf(modprice1);

                                if (modiname.toString().length() > charlength) {
                                    if (modiname.toString().length() > charlength) {
                                        String string1 = modiname.substring(0, charlength);
                                        String string2 = modiname.substring(charlength);

                                        addNewItemWithLeftAndRight(document, "      "+"*"+modiname, modiprice+"/"+"", titleFont, titleFont);

                                    }
                                    if (modiname.toString().length() > charlength1) {
                                        String string1 = modiname.substring(0, charlength);
                                        String string2 = modiname.substring(charlength, charlength1);
                                        String string3 = modiname.substring(charlength1);

                                        addNewItemWithLeftAndRight(document, "      "+"*"+modiname, modiprice+"/"+"", titleFont, titleFont);

                                    }

                                    Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                    if (ccursor.moveToFirst()) {
                                        String hsn = ccursor.getString(34);

                                        TextView hsn_hsn = new TextView(A4_Printer_Cancel_new.this);
                                        hsn_hsn.setText(hsn);

                                        if (hsn_hsn.getText().toString().equals("")) {
                                        } else {
                                            addNewItem(document, hsn, Element.ALIGN_LEFT, titleFont);
                                        }
                                    }

                                } else {
                                    addNewItemWithLeftAndRight(document, "      "+"*"+modiname, modiprice+"/"+"", titleFont, titleFont);

                                    Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                    if (ccursor.moveToFirst()) {
                                        String hsn = ccursor.getString(34);

                                        TextView hsn_hsn = new TextView(A4_Printer_Cancel_new.this);
                                        hsn_hsn.setText(hsn);

                                        addNewItem(document, hsn, Element.ALIGN_LEFT, titleFont);
                                    }

                                }

                                final TableRow tableRow11 = new TableRow(A4_Printer_Cancel_new.this);
                                tableRow11.setLayoutParams(new TableLayout.LayoutParams(
                                        TableRow.LayoutParams.MATCH_PARENT,
                                        TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                                final TextView tvv = new TextView(A4_Printer_Cancel_new.this);
                                // tv.setLayoutParams(new TableRow.LayoutParams(40, TableRow.LayoutParams.MATCH_PARENT));
                                tvv.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 0.70f));
                                //tv.setBackgroundResource(R.drawable.cell_shape);
                                //tv.setGravity(Gravity.CENTER);
                                tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                                tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                                tvv.setText("");
                                tableRow11.addView(tvv);

                                TextView tv4 = new TextView(A4_Printer_Cancel_new.this);
                                //lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                                tv4.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.6f));
                                //tv3.setPadding(5, 0, 0, 0);
                                //tv.setBackgroundResource(R.drawable.cell_shape);
                                tv4.setText(modiname);
                                tv4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                                tv4.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                                tv4.setGravity(Gravity.CENTER_VERTICAL);
                                //tv3.setBackgroundColor(getResources().getColor(R.color.six));
                                //tv3.setTextColor(R.color.black);
                                tableRow11.addView(tv4);

                                TextView tv5 = new TextView(A4_Printer_Cancel_new.this);
                                //lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                                tv5.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.0f));
                                //tv3.setPadding(5, 0, 0, 0);
                                //tv.setBackgroundResource(R.drawable.cell_shape);
                                tv5.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                                tv5.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                                //tv2.setPadding(0, 0, 1, 0);
                                tv5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                                tv5.setText(modiprice);
                                //tv3.setBackgroundColor(getResources().getColor(R.color.six));
                                //tv3.setTextColor(R.color.black);
                                tableRow11.addView(tv5);

                                TextView tv6 = new TextView(A4_Printer_Cancel_new.this);
                                //lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                                tv6.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                                //tv3.setPadding(5, 0, 0, 0);
                                tv6.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                                tv6.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                                tv6.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                                //tv.setBackgroundResource(R.drawable.cell_shape);
                                tv6.setText("");
                                //tv3.setBackgroundColor(getResources().getColor(R.color.six));
                                //tv3.setTextColor(R.color.black);
                                tableRow11.addView(tv6);


                                final TextView tv7 = new TextView(A4_Printer_Cancel_new.this);
                                //tv3.setLayoutParams(new TableRow.LayoutParams(90, TableRow.LayoutParams.MATCH_PARENT));
                                tv7.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                                tv7.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                                tv7.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                                //tv3.setPadding(0,0,10,0);
                                tv7.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                                String modtotal = String.valueOf(Float.parseFloat(modiquan) * Float.parseFloat(modiprice));

                                final String number = tv.getText().toString();
                                float newmul = Float.parseFloat(number);
                                //final float in = Float.parseFloat(cursor.getString(4));
                                String multiply = String.valueOf(newmul * Float.parseFloat(pricee));
                                //newmul = Integer.parseInt(multiply);
                                //tv3.setText(String.valueOf(Float.parseFloat(multiply)+Float.parseFloat(modtotal)));
                                //row.addView(tv3);

                                row.removeView(tv8);


                                tv8 = new TextView(A4_Printer_Cancel_new.this);
                                tv8.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                                //tv3.setLayoutParams(new TableRow.LayoutParams(90, TableRow.LayoutParams.MATCH_PARENT));
                                //tv3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                                //tv.setBackgroundResource(R.drawable.cell_shape);
                                tv8.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                                //tv3.setPadding(0, 0, 10, 0);
                                tv8.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                                tv8.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                                final String numberr = tv.getText().toString();
                                float newmulr = Float.parseFloat(numberr);
                                //final float in = Float.parseFloat(cursor.getString(4));
                                String multiplyr = String.valueOf(newmulr * Float.parseFloat(pricee));
                                //newmul = Integer.parseInt(multiply);
                                tv8.setText(String.valueOf(ss));
                                row.addView(tv8);


                                tableLayout1.addView(tableRow11);
                            } while (modcursor.moveToNext());

                            Cursor disc_cursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '" + name + "' AND _id = '" + newids + "'  ", null);
                            if (disc_cursor.moveToFirst()) {
                                do {
                                    String disc_vv = disc_cursor.getString(12);
                                    String disc_there = disc_cursor.getString(30);
                                    if (disc_there.toString().equals(getString(R.string.no))) {

                                    } else {

                                        Cursor cursor113 = db.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billnumb + "' AND disc_thereornot = 'yes' AND itemname = '" + name + "' AND _id = '" + newids + "'", null);
                                        if (cursor113.moveToFirst()) {
                                            float vtq = cursor113.getFloat(31);
                                            total_disc_print_q = String.valueOf(vtq);
                                        }

                                        addNewItemWithLeftAndRight(document, "      "+"*"+"      ", "      "+"/"+"("+"-"+total_disc_print_q+")", titleFont, titleFont);

                                    }
                                } while (disc_cursor.moveToNext());
                            }
                        } else {

                            float fgh = Float.parseFloat(tototot);
                            String tototott = String.format("%.1f", fgh);
                            if (name.toString().length() > charlength) {
                                int print1 = 0;
                                if (value.length() > quanlentha && name.toString().length() > charlength) {
                                    String string1quan = value.substring(0, quanlentha);
                                    String string2quan = value.substring(quanlentha);
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength);

                                    if (print1 == 0) {
                                        addNewItemWithLeftAndRight(document, value + "*" + name, pricee + "/" + tototott, titleFont, titleFont);
                                        print1 = 1;
                                    }

                                }
                                if (value.length() <= quanlentha && name.toString().length() > charlength) {
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength);

                                    if (print1 == 0) {
                                        addNewItemWithLeftAndRight(document, value + "*" + name, pricee + "/" + tototott, titleFont, titleFont);
                                        print1 = 1;
                                    }

                                }

////////////////////////////////////////
                                if (value.length() > quanlentha && name.toString().length() > charlength1) {
                                    String string1quan = value.substring(0, quanlentha);
                                    String string2quan = value.substring(quanlentha);
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength, charlength1);
                                    String string3 = name.substring(charlength1);

                                    if (print1 == 0) {
                                        addNewItemWithLeftAndRight(document, value + "*" + name, pricee + "/" + tototott, titleFont, titleFont);
                                        print1 = 1;
                                    }

                                }
                                if (value.length() <= quanlentha && name.toString().length() > charlength1) {
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength, charlength1);
                                    String string3 = name.substring(charlength1);

                                    if (print1 == 0) {
                                        addNewItemWithLeftAndRight(document, value + "*" + name, pricee + "/" + tototott, titleFont, titleFont);
                                        print1 = 1;
                                    }

                                }

                                Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                if (ccursor.moveToFirst()) {
                                    String hsn = ccursor.getString(34);

                                    TextView hsn_hsn = new TextView(A4_Printer_Cancel_new.this);
                                    hsn_hsn.setText(hsn);

                                    if (hsn_hsn.getText().toString().equals("")) {
                                    } else {
                                        addNewItem(document, hsn, Element.ALIGN_LEFT, titleFont);
                                    }
                                }

                            } else {
                                if (value.toString().length() > quanlentha) {
                                    String string1quan = value.substring(0, quanlentha);
                                    String string2quan = value.substring(quanlentha);
                                    addNewItemWithLeftAndRight(document, value+"*"+name, pricee+"/"+tototott, titleFont, titleFont);
                                } else {
                                    addNewItemWithLeftAndRight(document, value+"*"+name, pricee+"/"+tototot, titleFont, titleFont);
                                }

                                Cursor ccursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                if (ccursor.moveToFirst()) {
                                    String hsn = ccursor.getString(34);

                                    TextView hsn_hsn = new TextView(A4_Printer_Cancel_new.this);
                                    hsn_hsn.setText(hsn);

                                    if (hsn_hsn.getText().toString().equals("")) {
                                    } else {
                                        addNewItem(document, hsn, Element.ALIGN_LEFT, titleFont);
                                    }
                                }

                            }

                            tv8 = new TextView(A4_Printer_Cancel_new.this);
                            tv8.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                            //tv3.setLayoutParams(new TableRow.LayoutParams(90, TableRow.LayoutParams.MATCH_PARENT));
                            //tv3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            tv8.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                            //tv3.setPadding(0, 0, 10, 0);
                            tv8.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tv8.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                            final String number = tv.getText().toString();
                            float newmul = Float.parseFloat(number);
                            //final float in = Float.parseFloat(cursor.getString(4));
                            String multiply = String.valueOf(newmul * Float.parseFloat(pricee));
                            //newmul = Integer.parseInt(multiply);
                            tv8.setText(String.valueOf(multiply));
                            row.addView(tv8);

                            Cursor disc_cursor = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "' AND itemname = '" + name + "' AND _id = '" + newids + "'  ", null);
                            if (disc_cursor.moveToFirst()) {
                                do {
                                    String disc_vv = disc_cursor.getString(12);
                                    String disc_there = disc_cursor.getString(30);
                                    if (disc_there.toString().equals(getString(R.string.no))) {

                                    } else {

                                        Cursor cursor113 = db.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billnumb + "' AND disc_thereornot = 'yes' AND itemname = '" + name + "' AND _id = '" + newids + "'", null);
                                        if (cursor113.moveToFirst()) {
                                            float vtq = cursor113.getFloat(31);
                                            total_disc_print_q = String.valueOf(vtq);
                                        }

                                        addNewItemWithLeftAndRight(document, "      "+"*"+"      ", "      "+"/"+"("+"-"+total_disc_print_q+")", titleFont, titleFont);

                                    }
                                } while (disc_cursor.moveToNext());
                            }

                        }
                    }


                } while (ccursorr.moveToNext());
            }

            addNewItem(document, "Returns/Cancel:", Element.ALIGN_LEFT, titleFont);


            Cursor ccursorreturn = db.rawQuery("Select * from All_Sales_Cancelled WHERE bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (ccursorreturn.moveToFirst()) {

                do {

                    String name = ccursorreturn.getString(1);
                    String value = ccursorreturn.getString(2);
                    String pq = ccursorreturn.getString(5);
                    String itna = ccursorreturn.getString(2);
                    String pricee = ccursorreturn.getString(3);
                    String tototot = ccursorreturn.getString(4);

                    final String newtt = ccursorreturn.getString(4);

                    final String newid = ccursorreturn.getString(19);

                    final String newids = ccursorreturn.getString(0);
                    int padding_in_px;

                    int padding_in_dp = 30;  // 34 dps
                    final float scale1 = getResources().getDisplayMetrics().density;
                    padding_in_px = (int) (padding_in_dp * scale1 + 0.5f);


                    if (pq.equals(getString(R.string.action_item))) {
                        final TableRow row = new TableRow(A4_Printer_Cancel_new.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));


                        final TableRow row1 = new TableRow(A4_Printer_Cancel_new.this);
                        row1.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));

                        final TableRow row2 = new TableRow(A4_Printer_Cancel_new.this);
                        row2.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                        //TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
                        final TableLayout tableLayout1 = new TableLayout(A4_Printer_Cancel_new.this);

                        TableRow.LayoutParams lp, lp1, lp2;

                        TextView tv = new TextView(A4_Printer_Cancel_new.this);
                        tv.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 0.70f));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tv.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                        //tv.setTextSize(18);
                        tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        //tv.setPadding(0, 0, 0, 0);
                        //text = ccursorr.getString(1);
                        tv.setText(value);
                        row.addView(tv);

                        TextView tv1 = new TextView(A4_Printer_Cancel_new.this);
                        tv1.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.6f));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv1.setGravity(Gravity.CENTER_VERTICAL);
                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        //tv1.setTextSize(15);
                        //tv.setPadding(0, 0, 0, 0);
                        //text = ccursorr.getString(1);
                        tv1.setText(name);
                        String value1 = tv1.getText().toString();
                        row.addView(tv1);

                        TextView tv2 = new TextView(A4_Printer_Cancel_new.this);
                        //lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 4.5f);
                        tv2.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.0f));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tv2.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        //tv2.append(value + "% " + name);
                        tv2.setText(pricee);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        //tv2.setTextColor(R.color.black);
                        row.addView(tv2);

//                                            TextView textView1 = new TextView(A4_Printer_Cancel_new.this);
//                                            //lp1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f);
//                                            textView1.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.WRAP_CONTENT,
//                                                    android.widget.TableRow.LayoutParams.WRAP_CONTENT, 1.5f));
//                                            //tv.setBackgroundResource(R.drawable.cell_shape);
//                                            textView1.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
//                                            textView1.setText(insert1_cc);
//                                            //textView1.setBackgroundResource(R.drawable.rs_border);
//                                            textView1.setPadding(0, 0, 5, 0);
//                                            //textView1.setTextColor(R.color.black);
//                                            textView1.setTextSize(14);
//                                            row.addView(textView1);

                        TextView tv3 = new TextView(A4_Printer_Cancel_new.this);
                        //lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                        tv3.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                        //tv3.setPadding(5, 0, 0, 0);
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tv2.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        //tv2.setPadding(0, 0, 1, 0);
                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tv3.setText(tototot);
                        //tv3.setTextColor(R.color.black);
                        //row.addView(tv3);


                        String value2 = tv3.getText().toString();

                        //tableLayoutt.addView(row);

                        Cursor modcursor = db.rawQuery("Select * from All_Sales_Cancelled WHERE bill_no = '" + billnumb + "' AND parent = '" + name + "' AND parentid = '" + newid + "' ", null);

                        if (modcursor.moveToFirst()) {

                            Cursor modt = db.rawQuery("Select SUM(total) FROM All_Sales_Cancelled WHERE bill_no = '" + billnumb + "' AND parent = '" + name + "' AND parentid = '" + newid + "' ", null);
                            if (modt.moveToFirst()) {
                                do {
                                    //row.removeView(tv3);
                                    float aq = modt.getFloat(0);
                                    String aqq = String.valueOf(aq);
                                    aqq1 = Float.parseFloat(aqq) + Float.parseFloat(newtt);
                                    aqq2 = String.format("%.1f", aqq1);
                                } while (modt.moveToNext());
                            }


                            if (name.toString().length() > charlength) {

                                if (value.length() > quanlentha && name.toString().length() > charlength) {
                                    String string1quan = value.substring(0, quanlentha);
                                    String string2quan = value.substring(quanlentha);
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength);

                                    addNewItemWithLeftAndRight(document, value+"*"+name, pricee+"/"+aqq2, titleFont, titleFont);

                                }
                                if (value.length() <= quanlentha && name.toString().length() > charlength) {
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength);

                                    addNewItemWithLeftAndRight(document, value+"*"+name, pricee+"/"+aqq2, titleFont, titleFont);

                                }

////////////////////////////////////////////
                                if (value.length() > quanlentha && name.toString().length() > charlength1) {
                                    String string1quan = total.substring(0, quanlentha);
                                    String string2quan = total.substring(quanlentha);
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength, charlength1);
                                    String string3 = name.substring(charlength1);

                                    addNewItemWithLeftAndRight(document, value+"*"+name, pricee+"/"+aqq2, titleFont, titleFont);

                                }
                                if (value.length() <= quanlentha && name.toString().length() > charlength1) {
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength, charlength1);
                                    String string3 = name.substring(charlength1);

                                    addNewItemWithLeftAndRight(document, value+"*"+name, pricee+"/"+aqq2, titleFont, titleFont);

                                }

                                Cursor ccursor = db.rawQuery("Select * from All_Sales_Cancelled WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                if (ccursor.moveToFirst()) {
                                    String hsn = ccursor.getString(38);

                                    TextView hsn_hsn = new TextView(A4_Printer_Cancel_new.this);
                                    hsn_hsn.setText(hsn);

                                    if (hsn_hsn.getText().toString().equals("")) {
                                    } else {
                                        addNewItem(document, hsn, Element.ALIGN_LEFT, titleFont);
                                    }
                                }

                            } else {
                                if (total.toString().length() > quanlentha) {
                                    String string1quan = total.substring(0, quanlentha);
                                    String string2quan = total.substring(quanlentha);

                                    addNewItemWithLeftAndRight(document, value+"*"+name, pricee+"/"+aqq2, titleFont, titleFont);

                                    Cursor ccursor = db.rawQuery("Select * from All_Sales_Cancelled WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                    if (ccursor.moveToFirst()) {
                                        String hsn = ccursor.getString(38);

                                        TextView hsn_hsn = new TextView(A4_Printer_Cancel_new.this);
                                        hsn_hsn.setText(hsn);

                                        if (hsn_hsn.getText().toString().equals("")) {
                                        } else {
                                            addNewItem(document, hsn, Element.ALIGN_LEFT, titleFont);
                                        }
                                    }

                                } else {

                                    addNewItemWithLeftAndRight(document, value+"*"+name, pricee+"/"+aqq2, titleFont, titleFont);

                                    Cursor ccursor = db.rawQuery("Select * from All_Sales_Cancelled WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                    if (ccursor.moveToFirst()) {
                                        String hsn = ccursor.getString(38);

                                        TextView hsn_hsn = new TextView(A4_Printer_Cancel_new.this);
                                        hsn_hsn.setText(hsn);

                                        if (hsn_hsn.getText().toString().equals("")) {
                                        } else {
                                            addNewItem(document, hsn, Element.ALIGN_LEFT, titleFont);
                                        }
                                    }

                                }

                            }

                            do {

                                final String modiname = modcursor.getString(1);
                                final String modiquan = modcursor.getString(2);
                                String modiprice = modcursor.getString(3);
                                String moditotal = modcursor.getString(4);
                                final String modiid = modcursor.getString(0);

                                float modprice1 = Float.parseFloat(modiprice);
                                String modpricestr = String.valueOf(modprice1);

                                if (modiname.toString().length() > charlength) {
                                    if (modiname.toString().length() > charlength) {
                                        String string1 = modiname.substring(0, charlength);
                                        String string2 = modiname.substring(charlength);

                                        addNewItemWithLeftAndRight(document, "      "+"*"+modiname, modiprice+"/"+"", titleFont, titleFont);

                                    }
                                    if (modiname.toString().length() > charlength1) {
                                        String string1 = modiname.substring(0, charlength);
                                        String string2 = modiname.substring(charlength, charlength1);
                                        String string3 = modiname.substring(charlength1);

                                        addNewItemWithLeftAndRight(document, "      "+"*"+modiname, modiprice+"/"+"", titleFont, titleFont);

                                    }

                                    Cursor ccursor = db.rawQuery("Select * from All_Sales_Cancelled WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                    if (ccursor.moveToFirst()) {
                                        String hsn = ccursor.getString(38);

                                        TextView hsn_hsn = new TextView(A4_Printer_Cancel_new.this);
                                        hsn_hsn.setText(hsn);

                                        if (hsn_hsn.getText().toString().equals("")) {
                                        } else {
                                            addNewItem(document, hsn, Element.ALIGN_LEFT, titleFont);
                                        }
                                    }

                                } else {
                                    addNewItemWithLeftAndRight(document, "      "+"*"+modiname, modiprice+"/"+"", titleFont, titleFont);

                                    Cursor ccursor = db.rawQuery("Select * from All_Sales_Cancelled WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                    if (ccursor.moveToFirst()) {
                                        String hsn = ccursor.getString(38);

                                        TextView hsn_hsn = new TextView(A4_Printer_Cancel_new.this);
                                        hsn_hsn.setText(hsn);

                                        if (hsn_hsn.getText().toString().equals("")) {
                                        } else {
                                            addNewItem(document, hsn, Element.ALIGN_LEFT, titleFont);
                                        }
                                    }

                                }


                                final TableRow tableRow11 = new TableRow(A4_Printer_Cancel_new.this);
                                tableRow11.setLayoutParams(new TableLayout.LayoutParams(
                                        TableRow.LayoutParams.MATCH_PARENT,
                                        TableRow.LayoutParams.WRAP_CONTENT, 4.5f));


                                final TextView tvv = new TextView(A4_Printer_Cancel_new.this);
                                // tv.setLayoutParams(new TableRow.LayoutParams(40, TableRow.LayoutParams.MATCH_PARENT));
                                tvv.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 0.70f));
                                //tv.setBackgroundResource(R.drawable.cell_shape);
                                //tv.setGravity(Gravity.CENTER);
                                tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                                tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                                tvv.setText("");
                                tableRow11.addView(tvv);

                                TextView tv4 = new TextView(A4_Printer_Cancel_new.this);
                                //lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                                tv4.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.6f));
                                //tv3.setPadding(5, 0, 0, 0);
                                //tv.setBackgroundResource(R.drawable.cell_shape);
                                tv4.setText(modiname);
                                tv4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                                tv4.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                                tv4.setGravity(Gravity.CENTER_VERTICAL);
                                //tv3.setBackgroundColor(getResources().getColor(R.color.six));
                                //tv3.setTextColor(R.color.black);
                                tableRow11.addView(tv4);

                                TextView tv5 = new TextView(A4_Printer_Cancel_new.this);
                                //lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                                tv5.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.0f));
                                //tv3.setPadding(5, 0, 0, 0);
                                //tv.setBackgroundResource(R.drawable.cell_shape);
                                tv5.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                                tv5.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                                //tv2.setPadding(0, 0, 1, 0);
                                tv5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                                tv5.setText(modiprice);
                                //tv3.setBackgroundColor(getResources().getColor(R.color.six));
                                //tv3.setTextColor(R.color.black);
                                tableRow11.addView(tv5);

                                TextView tv6 = new TextView(A4_Printer_Cancel_new.this);
                                //lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                                tv6.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                                //tv3.setPadding(5, 0, 0, 0);
                                tv6.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                                tv6.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                                tv6.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                                //tv.setBackgroundResource(R.drawable.cell_shape);
                                tv6.setText("");
                                //tv3.setBackgroundColor(getResources().getColor(R.color.six));
                                //tv3.setTextColor(R.color.black);
                                tableRow11.addView(tv6);


                                final TextView tv7 = new TextView(A4_Printer_Cancel_new.this);
                                //tv3.setLayoutParams(new TableRow.LayoutParams(90, TableRow.LayoutParams.MATCH_PARENT));
                                tv7.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                                tv7.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                                tv7.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                                //tv3.setPadding(0,0,10,0);
                                tv7.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                                String modtotal = String.valueOf(Float.parseFloat(modiquan) * Float.parseFloat(modiprice));

                                final String number = tv.getText().toString();
                                float newmul = Float.parseFloat(number);
                                //final float in = Float.parseFloat(cursor.getString(4));
                                String multiply = String.valueOf(newmul * Float.parseFloat(pricee));
                                //newmul = Integer.parseInt(multiply);
                                //tv3.setText(String.valueOf(Float.parseFloat(multiply)+Float.parseFloat(modtotal)));
                                //row.addView(tv3);


                                row.removeView(tv8);
                                tv8 = new TextView(A4_Printer_Cancel_new.this);
                                tv8.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                                //tv3.setLayoutParams(new TableRow.LayoutParams(90, TableRow.LayoutParams.MATCH_PARENT));
                                //tv3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                                //tv.setBackgroundResource(R.drawable.cell_shape);
                                tv8.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                                //tv3.setPadding(0, 0, 10, 0);
                                tv8.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                                tv8.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                                final String numberr = tv.getText().toString();
                                float newmulr = Float.parseFloat(numberr);
                                //final float in = Float.parseFloat(cursor.getString(4));
                                String multiplyr = String.valueOf(newmulr * Float.parseFloat(pricee));
                                //newmul = Integer.parseInt(multiply);
                                tv8.setText(String.valueOf(Float.parseFloat(multiply) + Float.parseFloat(modtotal)));
                                row.addView(tv8);


                                tableLayout1.addView(tableRow11);
                            } while (modcursor.moveToNext());

                            //Cursor modcursor = db.rawQuery("Select * from All_Sales_Cancelled WHERE bill_no = '" + billnumb + "' AND parent = '" + name + "' AND parentid = '" + newid + "' ", null);
                            Cursor disc_cursor = db.rawQuery("Select * from All_Sales_Cancelled WHERE bill_no = '" + billnumb + "' AND itemname = '" + name + "' AND _id = '" + newids + "'  ", null);
                            if (disc_cursor.moveToFirst()) {
                                do {
                                    String disc_vv = disc_cursor.getString(12);
                                    String disc_there = disc_cursor.getString(27);
                                    if (disc_there.toString().equals(getString(R.string.no))) {

                                    } else {

                                        Cursor cursor113 = db.rawQuery("SELECT * FROM All_Sales_Cancelled WHERE bill_no = '" + billnumb + "' AND disc_thereornot = 'yes' AND itemname = '" + name + "' AND _id = '" + newids + "'", null);
                                        if (cursor113.moveToFirst()) {
                                            float vtq = cursor113.getFloat(28);
                                            total_disc_print_q = String.valueOf(vtq);
                                        }

                                        addNewItemWithLeftAndRight(document, "      "+"*"+"      ", "      "+"/"+"("+"-"+total_disc_print_q+")", titleFont, titleFont);

                                    }
                                } while (disc_cursor.moveToNext());
                            }
                        } else {

                            float fgh = Float.parseFloat(tototot);
                            String tototott = String.format("%.1f", fgh);

                            if (name.toString().length() > charlength) {
                                if (value.length() > quanlentha && name.toString().length() > charlength) {
                                    String string1quan = value.substring(0, quanlentha);
                                    String string2quan = value.substring(quanlentha);
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength);
                                    addNewItemWithLeftAndRight(document, value + "*" + name, pricee + "/" + tototott, titleFont, titleFont);
                                }
                                if (value.length() <= quanlentha && name.toString().length() > charlength) {
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength);
                                    addNewItemWithLeftAndRight(document, value + "*" + name, pricee + "/" + tototott, titleFont, titleFont);
                                }

//////////////////////////////////////////////
                                if (value.length() > quanlentha && name.toString().length() > charlength1) {
                                    String string1quan = value.substring(0, quanlentha);
                                    String string2quan = value.substring(quanlentha);
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength, charlength1);
                                    String string3 = name.substring(charlength1);
                                    addNewItemWithLeftAndRight(document, value + "*" + name, pricee + "/" + tototott, titleFont, titleFont);
                                }
                                if (value.length() <= quanlentha && name.toString().length() > charlength1) {
                                    String string1 = name.substring(0, charlength);
                                    String string2 = name.substring(charlength, charlength1);
                                    String string3 = name.substring(charlength1);
                                    addNewItemWithLeftAndRight(document, value + "*" + name, pricee + "/" + tototott, titleFont, titleFont);
                                }

                                Cursor ccursor = db.rawQuery("Select * from All_Sales_Cancelled WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                if (ccursor.moveToFirst()) {
                                    String hsn = ccursor.getString(38);

                                    TextView hsn_hsn = new TextView(A4_Printer_Cancel_new.this);
                                    hsn_hsn.setText(hsn);

                                    if (hsn_hsn.getText().toString().equals("")) {
                                    } else {
                                        addNewItem(document, hsn, Element.ALIGN_LEFT, titleFont);
                                    }
                                }

                            } else {

                                if (value.toString().length() > quanlentha) {
                                    String string1quan = value.substring(0, quanlentha);
                                    String string2quan = value.substring(quanlentha);
                                    addNewItemWithLeftAndRight(document, value+"*"+name, pricee+"/"+tototott, titleFont, titleFont);
                                } else {
                                    addNewItemWithLeftAndRight(document, value+"*"+name, pricee+"/"+tototott, titleFont, titleFont);
                                }

                                Cursor ccursor = db.rawQuery("Select * from All_Sales_Cancelled WHERE bill_no = '" + billnumb + "' AND itemname = '"+name+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                                if (ccursor.moveToFirst()) {
                                    String hsn = ccursor.getString(38);

                                    TextView hsn_hsn = new TextView(A4_Printer_Cancel_new.this);
                                    hsn_hsn.setText(hsn);

                                    if (hsn_hsn.getText().toString().equals("")) {
                                    } else {
                                        addNewItem(document, hsn, Element.ALIGN_LEFT, titleFont);
                                    }
                                }

                            }

                            tv8 = new TextView(A4_Printer_Cancel_new.this);
                            tv8.setLayoutParams(new TableRow.LayoutParams(0, padding_in_px, 1.2f));
                            //tv3.setLayoutParams(new TableRow.LayoutParams(90, TableRow.LayoutParams.MATCH_PARENT));
                            //tv3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            tv8.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                            //tv3.setPadding(0, 0, 10, 0);
                            tv8.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tv8.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                            final String number = tv.getText().toString();
                            float newmul = Float.parseFloat(number);
                            //final float in = Float.parseFloat(cursor.getString(4));
                            String multiply = String.valueOf(newmul * Float.parseFloat(pricee));
                            //newmul = Integer.parseInt(multiply);
                            tv8.setText(String.valueOf(multiply));
                            row.addView(tv8);

                            Cursor disc_cursor = db.rawQuery("Select * from All_Sales_Cancelled WHERE bill_no = '" + billnumb + "' AND itemname = '" + name + "' AND _id = '" + newids + "'  ", null);
                            if (disc_cursor.moveToFirst()) {
                                do {
                                    String disc_vv = disc_cursor.getString(12);
                                    String disc_there = disc_cursor.getString(27);
                                    if (disc_there.toString().equals(getString(R.string.no))) {

                                    } else {

                                        Cursor cursor113 = db.rawQuery("SELECT * FROM All_Sales_Cancelled WHERE bill_no = '" + billnumb + "' AND disc_thereornot = 'yes' AND itemname = '" + name + "' AND _id = '" + newids + "'", null);
                                        if (cursor113.moveToFirst()) {
                                            float vtq = cursor113.getFloat(28);
                                            total_disc_print_q = String.valueOf(vtq);
                                        }

                                        addNewItemWithLeftAndRight(document, "      "+"*"+"      ", "      "+"/"+"("+"-"+total_disc_print_q+")", titleFont, titleFont);

                                    }
                                } while (disc_cursor.moveToNext());
                            }

                        }

                    }
                } while (ccursorreturn.moveToNext());


            }
////////////////////////////////////sub total

            float sub1 = 0, sub2 = 0;

            Cursor cursor3 = db.rawQuery("SELECT SUM(total) FROM All_Sales WHERE bill_no = '" + billnumb + "'", null);
            if (cursor3.moveToFirst()) {
                sub1 = cursor3.getFloat(0);
            }

            Cursor cursor4 = db.rawQuery("SELECT SUM(total) FROM All_Sales_Cancelled WHERE bill_no = '" + billnumb + "'", null);
            if (cursor4.moveToFirst()) {
                sub2 = cursor4.getFloat(0);
            }
            float sub12 = sub1 + sub2;
            String total2 = String.valueOf(sub12);
            float to = Float.parseFloat(total2);
            String tot = String.valueOf(to);

            addLineSeperator_light(document);

            addNewItemWithLeftAndRight(document, "Sub total", tot, titleFont, titleFont);


/////////////////////////tax
            TableLayout tableLayout1 = new TableLayout(A4_Printer_Cancel_new.this);
            tableLayout1.removeAllViews();

            Cursor ccursor = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (ccursor.moveToFirst()) {

                do {
                    String name = ccursor.getString(10);
                    String value = ccursor.getString(9);
                    String pq = ccursor.getString(4);
                    String itna = ccursor.getString(1);

                    TextView v = new TextView(A4_Printer_Cancel_new.this);
                    v.setText(value);

                    TextView v1 = new TextView(A4_Printer_Cancel_new.this);
                    v1.setText(name);
                    if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                            || v.getText().toString().equals("")) {

                    } else {
                        final TableRow row = new TableRow(A4_Printer_Cancel_new.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                        TableRow.LayoutParams lp, lp1, lp2;

                        TextView tvv = new TextView(A4_Printer_Cancel_new.this);
                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tvv.setGravity(Gravity.START);
                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tvv.setText(name);

                        TextView tv1 = new TextView(A4_Printer_Cancel_new.this);
                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        tv1.setGravity(Gravity.START);
                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tv1.setText(value);
                        float vbn = Float.parseFloat(value);
                        String bvn = String.format(Locale.US,"%.2f", vbn);
                        String value1 = tv1.getText().toString();

                        TextView tv2 = new TextView(A4_Printer_Cancel_new.this);
                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                        tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                        tv2.append(name + "@" + bvn + "%");
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv2.setPadding(0, 0, 20, 0);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        //tv2.setTextColor(Color.parseColor("#000000"));
                        row.addView(tv2);

                        TextView textView1 = new TextView(A4_Printer_Cancel_new.this);
                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(textView1);

                        TextView tv3 = new TextView(A4_Printer_Cancel_new.this);
                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                        String tota1 = String.format(Locale.US,"%.2f", tota);
                        tv3.setText(String.valueOf(tota));
                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        String value2 = tv3.getText().toString();
                        row.addView(tv3);

                        tableLayout1.addView(row);
                    }
                } while (ccursor.moveToNext());
            }
            ccursor.close();

            Cursor ccursor2 = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (ccursor2.moveToFirst()) {

                do {
                    String name = ccursor2.getString(35);
                    String value = ccursor2.getString(36);
                    String pq = ccursor2.getString(4);
                    String itna = ccursor2.getString(1);

                    TextView v = new TextView(A4_Printer_Cancel_new.this);
                    v.setText(value);

                    TextView v1 = new TextView(A4_Printer_Cancel_new.this);
                    v1.setText(name);
                    if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                            || v.getText().toString().equals("")) {

                    } else {
                        final TableRow row = new TableRow(A4_Printer_Cancel_new.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                        TableRow.LayoutParams lp, lp1, lp2;

                        TextView tvv = new TextView(A4_Printer_Cancel_new.this);
                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tvv.setGravity(Gravity.START);
                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tvv.setText(name);

                        TextView tv1 = new TextView(A4_Printer_Cancel_new.this);
                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        tv1.setGravity(Gravity.START);
                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tv1.setText(value);
                        float vbn = Float.parseFloat(value);
                        String bvn = String.format(Locale.US,"%.2f", vbn);
                        String value1 = tv1.getText().toString();

                        TextView tv2 = new TextView(A4_Printer_Cancel_new.this);
                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                        tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                        tv2.append(name + "@" + bvn + "%");
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv2.setPadding(0, 0, 20, 0);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        //tv2.setTextColor(Color.parseColor("#000000"));
                        row.addView(tv2);

                        TextView textView1 = new TextView(A4_Printer_Cancel_new.this);
                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(textView1);

                        TextView tv3 = new TextView(A4_Printer_Cancel_new.this);
                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                        String tota1 = String.format(Locale.US,"%.2f", tota);
                        tv3.setText(String.valueOf(tota));
                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        String value2 = tv3.getText().toString();
                        row.addView(tv3);

                        tableLayout1.addView(row);
                    }
                } while (ccursor2.moveToNext());
            }
            ccursor2.close();

            Cursor ccursor3 = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (ccursor3.moveToFirst()) {

                do {
                    String name = ccursor3.getString(37);
                    String value = ccursor3.getString(38);
                    String pq = ccursor3.getString(4);
                    String itna = ccursor3.getString(1);

                    TextView v = new TextView(A4_Printer_Cancel_new.this);
                    v.setText(value);

                    TextView v1 = new TextView(A4_Printer_Cancel_new.this);
                    v1.setText(name);
                    if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                            || v.getText().toString().equals("")) {

                    } else {
                        final TableRow row = new TableRow(A4_Printer_Cancel_new.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                        TableRow.LayoutParams lp, lp1, lp2;

                        TextView tvv = new TextView(A4_Printer_Cancel_new.this);
                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tvv.setGravity(Gravity.START);
                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tvv.setText(name);

                        TextView tv1 = new TextView(A4_Printer_Cancel_new.this);
                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        tv1.setGravity(Gravity.START);
                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tv1.setText(value);
                        float vbn = Float.parseFloat(value);
                        String bvn = String.format(Locale.US,"%.2f", vbn);
                        String value1 = tv1.getText().toString();

                        TextView tv2 = new TextView(A4_Printer_Cancel_new.this);
                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                        tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                        tv2.append(name + "@" + bvn + "%");
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv2.setPadding(0, 0, 20, 0);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        //tv2.setTextColor(Color.parseColor("#000000"));
                        row.addView(tv2);

                        TextView textView1 = new TextView(A4_Printer_Cancel_new.this);
                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(textView1);

                        TextView tv3 = new TextView(A4_Printer_Cancel_new.this);
                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                        String tota1 = String.format(Locale.US,"%.2f", tota);
                        tv3.setText(String.valueOf(tota));
                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        String value2 = tv3.getText().toString();
                        row.addView(tv3);

                        tableLayout1.addView(row);
                    }
                } while (ccursor3.moveToNext());
            }
            ccursor3.close();

            Cursor ccursor4 = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (ccursor4.moveToFirst()) {

                do {
                    String name = ccursor4.getString(39);
                    String value = ccursor4.getString(40);
                    String pq = ccursor4.getString(4);
                    String itna = ccursor4.getString(1);

                    TextView v = new TextView(A4_Printer_Cancel_new.this);
                    v.setText(value);

                    TextView v1 = new TextView(A4_Printer_Cancel_new.this);
                    v1.setText(name);
                    if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                            || v.getText().toString().equals("")) {

                    } else {
                        final TableRow row = new TableRow(A4_Printer_Cancel_new.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                        TableRow.LayoutParams lp, lp1, lp2;

                        TextView tvv = new TextView(A4_Printer_Cancel_new.this);
                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tvv.setGravity(Gravity.START);
                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tvv.setText(name);

                        TextView tv1 = new TextView(A4_Printer_Cancel_new.this);
                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        tv1.setGravity(Gravity.START);
                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tv1.setText(value);
                        float vbn = Float.parseFloat(value);
                        String bvn = String.format(Locale.US,"%.2f", vbn);
                        String value1 = tv1.getText().toString();

                        TextView tv2 = new TextView(A4_Printer_Cancel_new.this);
                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                        tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                        tv2.append(name + "@" + bvn + "%");
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv2.setPadding(0, 0, 20, 0);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        //tv2.setTextColor(Color.parseColor("#000000"));
                        row.addView(tv2);

                        TextView textView1 = new TextView(A4_Printer_Cancel_new.this);
                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(textView1);

                        TextView tv3 = new TextView(A4_Printer_Cancel_new.this);
                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                        String tota1 = String.format(Locale.US,"%.2f", tota);
                        tv3.setText(String.valueOf(tota));
                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        String value2 = tv3.getText().toString();
                        row.addView(tv3);

                        tableLayout1.addView(row);
                    }
                } while (ccursor4.moveToNext());
            }
            ccursor4.close();

            Cursor ccursor5 = db.rawQuery("Select * from All_Sales where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (ccursor5.moveToFirst()) {

                do {
                    String name = ccursor5.getString(41);
                    String value = ccursor5.getString(42);
                    String pq = ccursor5.getString(4);
                    String itna = ccursor5.getString(1);

                    TextView v = new TextView(A4_Printer_Cancel_new.this);
                    v.setText(value);

                    TextView v1 = new TextView(A4_Printer_Cancel_new.this);
                    v1.setText(name);
                    if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
                            || v.getText().toString().equals("")) {

                    } else {
                        final TableRow row = new TableRow(A4_Printer_Cancel_new.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));

                        TableRow.LayoutParams lp, lp1, lp2;

                        TextView tvv = new TextView(A4_Printer_Cancel_new.this);
                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tvv.setGravity(Gravity.START);
                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tvv.setText(name);

                        TextView tv1 = new TextView(A4_Printer_Cancel_new.this);
                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        tv1.setGravity(Gravity.START);
                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tv1.setText(value);
                        float vbn = Float.parseFloat(value);
                        String bvn = String.format(Locale.US,"%.2f", vbn);
                        String value1 = tv1.getText().toString();

                        TextView tv2 = new TextView(A4_Printer_Cancel_new.this);
                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                        tv2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                        tv2.append(name + "@" + bvn + "%");
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv2.setPadding(0, 0, 20, 0);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        //tv2.setTextColor(Color.parseColor("#000000"));
                        row.addView(tv2);

                        TextView textView1 = new TextView(A4_Printer_Cancel_new.this);
                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(textView1);

                        TextView tv3 = new TextView(A4_Printer_Cancel_new.this);
                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        float tota = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
                        String tota1 = String.format(Locale.US,"%.2f", tota);
                        tv3.setText(String.valueOf(tota));
                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        String value2 = tv3.getText().toString();
                        row.addView(tv3);

                        tableLayout1.addView(row);
                    }
                } while (ccursor5.moveToNext());
            }
            ccursor5.close();




//            Cursor ccursortaxreturn = db.rawQuery("Select * from All_Sales_Cancelled where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
//            if (ccursortaxreturn.moveToFirst()) {
//
//                do {
//                    String name = ccursortaxreturn.getString(10);
//                    String value = ccursortaxreturn.getString(9);
//                    String pq = ccursortaxreturn.getString(4);
//                    String itna = ccursortaxreturn.getString(1);
//
//                    TextView v = new TextView(A4_Printer_Cancel_new.this);
//                    v.setText(value);
//
//                    TextView v1 = new TextView(A4_Printer_Cancel_new.this);
//                    v1.setText(name);
//                    if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
//                            || v.getText().toString().equals("")) {
//
//                    } else {
//
//                        final TableRow row = new TableRow(A4_Printer_Cancel_new.this);
//                        row.setLayoutParams(new TableLayout.LayoutParams(
//                                TableRow.LayoutParams.MATCH_PARENT,
//                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));
//
//                        TextView tvv = new TextView(A4_Printer_Cancel_new.this);
//                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
//                        tvv.setGravity(Gravity.START);
//                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tvv.setText(name);
//
//                        TextView tv1 = new TextView(A4_Printer_Cancel_new.this);
//                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
//                        tv1.setGravity(Gravity.START);
//                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        tv1.setText(value);
//                        String value1 = tv1.getText().toString();
//
//                        TextView tv2 = new TextView(A4_Printer_Cancel_new.this);
//                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
//                        tv2.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//                        tv2.append(name + "@" + value + "%");
//                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tv2.setPadding(0, 0, 20, 0);
//                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        row.addView(tv2);
//
//                        TextView textView1 = new TextView(A4_Printer_Cancel_new.this);
//                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        row.addView(textView1);
//
//                        TextView tv3 = new TextView(A4_Printer_Cancel_new.this);
//                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
//                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
//                        float mul = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
//                        float tota = mul;
//                        String tota1 = String.format("%.2f", tota);
//                        tv3.setText(String.valueOf(tota));
//                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//
//                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        String value2 = tv3.getText().toString();
//                        row.addView(tv3);
//
//                        tableLayout1.addView(row);
//                    }
//
//                } while (ccursortaxreturn.moveToNext());
//            }
//
//            Cursor ccursortaxreturn2 = db.rawQuery("Select * from All_Sales_Cancelled where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
//            if (ccursortaxreturn2.moveToFirst()) {
//
//                do {
//                    String name = ccursortaxreturn2.getString(30);
//                    String value = ccursortaxreturn2.getString(31);
//                    String pq = ccursortaxreturn2.getString(4);
//                    String itna = ccursortaxreturn2.getString(1);
//
//                    TextView v = new TextView(A4_Printer_Cancel_new.this);
//                    v.setText(value);
//
//                    TextView v1 = new TextView(A4_Printer_Cancel_new.this);
//                    v1.setText(name);
//                    if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
//                            || v.getText().toString().equals("")) {
//
//                    } else {
//
//                        final TableRow row = new TableRow(A4_Printer_Cancel_new.this);
//                        row.setLayoutParams(new TableLayout.LayoutParams(
//                                TableRow.LayoutParams.MATCH_PARENT,
//                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));
//
//                        TextView tvv = new TextView(A4_Printer_Cancel_new.this);
//                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
//                        tvv.setGravity(Gravity.START);
//                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tvv.setText(name);
//
//                        TextView tv1 = new TextView(A4_Printer_Cancel_new.this);
//                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
//                        tv1.setGravity(Gravity.START);
//                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        tv1.setText(value);
//                        String value1 = tv1.getText().toString();
//
//                        TextView tv2 = new TextView(A4_Printer_Cancel_new.this);
//                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
//                        tv2.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//                        tv2.append(name + "@" + value + "%");
//                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tv2.setPadding(0, 0, 20, 0);
//                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        row.addView(tv2);
//
//                        TextView textView1 = new TextView(A4_Printer_Cancel_new.this);
//                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        row.addView(textView1);
//
//                        TextView tv3 = new TextView(A4_Printer_Cancel_new.this);
//                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
//                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
//                        float mul = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
//                        float tota = mul;
//                        String tota1 = String.format("%.2f", tota);
//                        tv3.setText(String.valueOf(tota));
//                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//
//                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        String value2 = tv3.getText().toString();
//                        row.addView(tv3);
//
//                        tableLayout1.addView(row);
//                    }
//
//                } while (ccursortaxreturn2.moveToNext());
//            }
//
//            Cursor ccursortaxreturn3 = db.rawQuery("Select * from All_Sales_Cancelled where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
//            if (ccursortaxreturn3.moveToFirst()) {
//
//                do {
//                    String name = ccursortaxreturn3.getString(32);
//                    String value = ccursortaxreturn3.getString(33);
//                    String pq = ccursortaxreturn3.getString(4);
//                    String itna = ccursortaxreturn3.getString(1);
//
//                    TextView v = new TextView(A4_Printer_Cancel_new.this);
//                    v.setText(value);
//
//                    TextView v1 = new TextView(A4_Printer_Cancel_new.this);
//                    v1.setText(name);
//                    if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
//                            || v.getText().toString().equals("")) {
//
//                    } else {
//
//                        final TableRow row = new TableRow(A4_Printer_Cancel_new.this);
//                        row.setLayoutParams(new TableLayout.LayoutParams(
//                                TableRow.LayoutParams.MATCH_PARENT,
//                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));
//
//                        TextView tvv = new TextView(A4_Printer_Cancel_new.this);
//                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
//                        tvv.setGravity(Gravity.START);
//                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tvv.setText(name);
//
//                        TextView tv1 = new TextView(A4_Printer_Cancel_new.this);
//                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
//                        tv1.setGravity(Gravity.START);
//                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        tv1.setText(value);
//                        String value1 = tv1.getText().toString();
//
//                        TextView tv2 = new TextView(A4_Printer_Cancel_new.this);
//                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
//                        tv2.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//                        tv2.append(name + "@" + value + "%");
//                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tv2.setPadding(0, 0, 20, 0);
//                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        row.addView(tv2);
//
//                        TextView textView1 = new TextView(A4_Printer_Cancel_new.this);
//                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        row.addView(textView1);
//
//                        TextView tv3 = new TextView(A4_Printer_Cancel_new.this);
//                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
//                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
//                        float mul = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
//                        float tota = mul;
//                        String tota1 = String.format("%.2f", tota);
//                        tv3.setText(String.valueOf(tota));
//                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//
//                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        String value2 = tv3.getText().toString();
//                        row.addView(tv3);
//
//                        tableLayout1.addView(row);
//                    }
//
//                } while (ccursortaxreturn3.moveToNext());
//            }
//
//            Cursor ccursortaxreturn4 = db.rawQuery("Select * from All_Sales_Cancelled where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
//            if (ccursortaxreturn4.moveToFirst()) {
//
//                do {
//                    String name = ccursortaxreturn4.getString(34);
//                    String value = ccursortaxreturn4.getString(35);
//                    String pq = ccursortaxreturn4.getString(4);
//                    String itna = ccursortaxreturn4.getString(1);
//
//                    TextView v = new TextView(A4_Printer_Cancel_new.this);
//                    v.setText(value);
//
//                    TextView v1 = new TextView(A4_Printer_Cancel_new.this);
//                    v1.setText(name);
//                    if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
//                            || v.getText().toString().equals("")) {
//
//                    } else {
//
//                        final TableRow row = new TableRow(A4_Printer_Cancel_new.this);
//                        row.setLayoutParams(new TableLayout.LayoutParams(
//                                TableRow.LayoutParams.MATCH_PARENT,
//                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));
//
//                        TextView tvv = new TextView(A4_Printer_Cancel_new.this);
//                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
//                        tvv.setGravity(Gravity.START);
//                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tvv.setText(name);
//
//                        TextView tv1 = new TextView(A4_Printer_Cancel_new.this);
//                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
//                        tv1.setGravity(Gravity.START);
//                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        tv1.setText(value);
//                        String value1 = tv1.getText().toString();
//
//                        TextView tv2 = new TextView(A4_Printer_Cancel_new.this);
//                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
//                        tv2.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//                        tv2.append(name + "@" + value + "%");
//                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tv2.setPadding(0, 0, 20, 0);
//                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        row.addView(tv2);
//
//                        TextView textView1 = new TextView(A4_Printer_Cancel_new.this);
//                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        row.addView(textView1);
//
//                        TextView tv3 = new TextView(A4_Printer_Cancel_new.this);
//                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
//                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
//                        float mul = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
//                        float tota = mul;
//                        String tota1 = String.format("%.2f", tota);
//                        tv3.setText(String.valueOf(tota));
//                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//
//                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        String value2 = tv3.getText().toString();
//                        row.addView(tv3);
//
//                        tableLayout1.addView(row);
//                    }
//
//                } while (ccursortaxreturn4.moveToNext());
//            }
//
//            Cursor ccursortaxreturn5 = db.rawQuery("Select * from All_Sales_Cancelled where bill_no = '" + billnumb + "'", null);//replace to cursor = dbHelper.fetchAllHotels();
//            if (ccursortaxreturn5.moveToFirst()) {
//
//                do {
//                    String name = ccursortaxreturn5.getString(36);
//                    String value = ccursortaxreturn5.getString(37);
//                    String pq = ccursortaxreturn5.getString(4);
//                    String itna = ccursortaxreturn5.getString(1);
//
//                    TextView v = new TextView(A4_Printer_Cancel_new.this);
//                    v.setText(value);
//
//                    TextView v1 = new TextView(A4_Printer_Cancel_new.this);
//                    v1.setText(name);
//                    if (v.getText().toString().equals("0") || v1.getText().toString().equals("NONE") || v1.getText().toString().equals("None") || v.getText().toString().equals("0.0")
//                            || v.getText().toString().equals("")) {
//
//                    } else {
//
//                        final TableRow row = new TableRow(A4_Printer_Cancel_new.this);
//                        row.setLayoutParams(new TableLayout.LayoutParams(
//                                TableRow.LayoutParams.MATCH_PARENT,
//                                TableRow.LayoutParams.WRAP_CONTENT, 4.5f));
//
//                        TextView tvv = new TextView(A4_Printer_Cancel_new.this);
//                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
//                        tvv.setGravity(Gravity.START);
//                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tvv.setText(name);
//
//                        TextView tv1 = new TextView(A4_Printer_Cancel_new.this);
//                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
//                        tv1.setGravity(Gravity.START);
//                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        tv1.setText(value);
//                        String value1 = tv1.getText().toString();
//
//                        TextView tv2 = new TextView(A4_Printer_Cancel_new.this);
//                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
//                        tv2.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//                        tv2.append(name + "@" + value + "%");
//                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                        tv2.setPadding(0, 0, 20, 0);
//                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        row.addView(tv2);
//
//                        TextView textView1 = new TextView(A4_Printer_Cancel_new.this);
//                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        row.addView(textView1);
//
//                        TextView tv3 = new TextView(A4_Printer_Cancel_new.this);
//                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
//                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
//                        float mul = Float.parseFloat(value1) * Float.parseFloat(pq) / 100;
//                        float tota = mul;
//                        String tota1 = String.format("%.2f", tota);
//                        tv3.setText(String.valueOf(tota));
//                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//
//                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                        String value2 = tv3.getText().toString();
//                        row.addView(tv3);
//
//                        tableLayout1.addView(row);
//                    }
//
//                } while (ccursortaxreturn5.moveToNext());
//            }


            ArrayList<String> groupList1 = new ArrayList<String>();

            float sum_p1 = 0;
            for (int i = 0; i < tableLayout1.getChildCount(); i++) {
                TableRow mRow = (TableRow) tableLayout1.getChildAt(i);
                TextView mTextView = (TextView) mRow.getChildAt(0);
//                                Toast.makeText(A4_Printer_Cancel_new.this, "a "+mTextView.getText().toString(), Toast.LENGTH_LONG).show();

                if (groupList1.contains(mTextView.getText().toString())) {

                }else {
                    sum_p1 = 0;
                    for (int j = 0; j < tableLayout1.getChildCount(); j++) {
                        TableRow mRow1 = (TableRow) tableLayout1.getChildAt(j);
                        mTextView1 = (TextView) mRow1.getChildAt(0);
                        mTextView2 = (TextView) mRow1.getChildAt(2);
                        if (groupList1.contains(mTextView.getText().toString())) {
                            if (mTextView.getText().toString().equals(mTextView1.getText().toString())) {
                                sum_p1 = sum_p1+Float.parseFloat(mTextView2.getText().toString());
//                                                Toast.makeText(A4_Printer_Cancel_new.this, "b " + mTextView2.getText().toString()+" "+sum, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            if (mTextView.getText().toString().equals(mTextView1.getText().toString())) {
                                groupList1.add(mTextView.getText().toString());
                                sum_p1 = sum_p1+Float.parseFloat(mTextView2.getText().toString());
//                                                Toast.makeText(A4_Printer_Cancel_new.this, "c " + mTextView2.getText().toString()+" "+sum, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
//                    Toast.makeText(A4_Printer_Cancel_new.this, "aa "+mTextView.getText().toString() +" "+sum_p1, Toast.LENGTH_LONG).show();

                    String mod1 = mTextView.getText().toString() + "---" + String.valueOf(String.format("%.2f", sum_p1));

                    addNewItem(document, mod1, Element.ALIGN_LEFT, titleFont);

                    String match = "@";
                    int position = mTextView.getText().toString().indexOf(match);
                    String mod2 = mTextView.getText().toString().substring(0, position);//keep toastmessage
//                    Toast.makeText(A4_Printer_Cancel_new.this, " "+mod2, Toast.LENGTH_LONG).show();
                    Cursor ccursor6 = db.rawQuery("Select * from All_Sales_Cancelled WHERE bill_no = '" + billnumb + "' AND taxname = '"+mod2+"' OR taxname2 = '"+mod2+"' OR taxname3 = '"+mod2+"' OR taxname4 = '"+mod2+"' OR taxname5 = '"+mod2+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                    if (ccursor6.moveToFirst()) {
                        String hsn = ccursor6.getString(38);

                        TextView hsn_hsn = new TextView(A4_Printer_Cancel_new.this);
                        hsn_hsn.setText(hsn);

                        if (hsn_hsn.getText().toString().equals("")) {
                        } else {
                            addNewItem(document, hsn, Element.ALIGN_LEFT, titleFont);
                        }
                    }

                }
            }


            String phon1 = "0";

            Cursor caddress1 = db.rawQuery("SELECT * FROM Customerdetails WHERE billnumber = '" + billnumb + "'", null);
            if (caddress1.moveToFirst()) {
                phon1 = caddress1.getString(2);
            }

            TextView tvvs = new TextView(A4_Printer_Cancel_new.this);
            tvvs.setText(phon1);


            Cursor us_name1 = db.rawQuery("Select * from Customerdetails WHERE phoneno = '" + tvvs.getText().toString() + "'", null);
            if (us_name1.moveToLast()) {
//            Toast.makeText(A4_Printer_Cancel_new.this, "user id there", Toast.LENGTH_LONG).show();
                String na53 = us_name1.getString(53);
                String na38 = us_name1.getString(38);
                String na39 = us_name1.getString(39);
                String na40 = us_name1.getString(40);
                String na41 = us_name1.getString(41);
                String na42 = us_name1.getString(42);
                String na43 = us_name1.getString(43);
                String na44 = us_name1.getString(44);
                String na45 = us_name1.getString(45);
                String na46 = us_name1.getString(46);
                String na47 = us_name1.getString(47);
                String na48 = us_name1.getString(48);
                String na49 = us_name1.getString(49);
                String na50 = us_name1.getString(50);
                String na51 = us_name1.getString(51);
                String na52 = us_name1.getString(52);
                String na38_value = us_name1.getString(54);
                String na39_value = us_name1.getString(55);
                String na40_value = us_name1.getString(56);
                String na41_value = us_name1.getString(57);
                String na42_value = us_name1.getString(58);
                String na43_value = us_name1.getString(59);
                String na44_value = us_name1.getString(60);
                String na45_value = us_name1.getString(61);
                String na46_value = us_name1.getString(62);
                String na47_value = us_name1.getString(63);
                String na48_value = us_name1.getString(64);
                String na49_value = us_name1.getString(65);
                String na50_value = us_name1.getString(66);
                String na51_value = us_name1.getString(67);
                String na52_value = us_name1.getString(68);

                String proc = us_name1.getString(69);

                TextView hid = new TextView(A4_Printer_Cancel_new.this);
                hid.setText(proc);

                if (hid.getText().toString().equals("off")) {
                    Cursor cursorr = null;
                    if (paymmethoda.toString().equals("  Dine-in") || paymmethoda.toString().equals(getString(R.string.general)) || paymmethoda.toString().equals("  Others")) {
                        cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax1 = 'dine_in'", null);//replace to cursor = dbHelper.fetchAllHotels();
                    }
                    if (paymmethoda.toString().equals("  Takeaway") || paymmethoda.toString().equals("  Main")) {
                        cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax2 = 'takeaway'", null);//replace to cursor = dbHelper.fetchAllHotels();
                    }
                    if (paymmethoda.toString().equals("  Home delivery")) {
                        cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax3 = 'homedelivery'", null);//replace to cursor = dbHelper.fetchAllHotels();
                    }
//            ccursor = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax'", null);//replace to ccursor = dbHelper.fetchAllHotels();
                    if (cursorr.moveToFirst()) {

                        do {

                            String name = cursorr.getString(1);
                            String value = cursorr.getString(2);

                            final TableRow row = new TableRow(A4_Printer_Cancel_new.this);
                            row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));
                            row.setGravity(Gravity.CENTER);

                            TableRow.LayoutParams lp, lp1, lp2;

//                                final TextView tv = new TextView(A4_Printer_Cancel_new.this);
//                                //tv.setLayoutParams(new TableRow.LayoutParams(40, TableRow.LayoutParams.MATCH_PARENT));
//                                tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.7f));
//                                tv.setTextSize(16);
//                                tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                                row.addView(tv);

                            TextView tvv = new TextView(A4_Printer_Cancel_new.this);
                            tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            tvv.setGravity(Gravity.START);
                            tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tvv.setText(name);

                            TextView tv1 = new TextView(A4_Printer_Cancel_new.this);
                            tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                            tv1.setGravity(Gravity.START);
                            tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tv1.setText(value);
                            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            String value1 = tv1.getText().toString();

                            TextView tv2 = new TextView(A4_Printer_Cancel_new.this);
                            //lp = new TableRow.LayoutParams(145, TableRow.LayoutParams.WRAP_CONTENT);
                            //tv2.setLayoutParams(lp);
                            tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                            tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tv2.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            tv2.append(name + " @ " + value + "%");
                            tv2.setPadding(0, 0, 20, 0);
                            tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            row.addView(tv2);

                            TextView textView1 = new TextView(A4_Printer_Cancel_new.this);
                            textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            row.addView(textView1);

                            TextView tv3 = new TextView(A4_Printer_Cancel_new.this);
//                lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
//                tv3.setLayoutParams(lp2);
                            tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                            //tv3.setPadding(0,0,10,0);
                            tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                            tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                            float mul = Float.parseFloat(value1) * Float.parseFloat(String.valueOf(sub12)) / 100;
                            float tota = mul;
                            String tota1 = String.format("%.2f", tota);
                            tv3.setText(String.valueOf(tota));
                            //row.addView(tv3);


                            tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            String value2 = tv3.getText().toString();
                            row.addView(tv3);

                            tableLayout1.addView(row);

                            String mod1 = name + " @ " + value + "%" + "---" + String.valueOf(tota1);

                            addNewItem(document, mod1, Element.ALIGN_LEFT, titleFont);

                        } while (cursorr.moveToNext());
                    }
                } else {
                    for (int i2 = 38; i2 < 53; i2++) {

//                                tv33.setText("0.0");
//                                for (int i1 = 54; i1<69; i1++) {
                        int i1 = 0;
                        if (i2 == 38) {
                            i1 = 54;
                        }
                        if (i2 == 39) {
                            i1 = 55;
                        }
                        if (i2 == 40) {
                            i1 = 56;
                        }
                        if (i2 == 41) {
                            i1 = 57;
                        }
                        if (i2 == 42) {
                            i1 = 58;
                        }
                        if (i2 == 43) {
                            i1 = 59;
                        }
                        if (i2 == 44) {
                            i1 = 60;
                        }
                        if (i2 == 45) {
                            i1 = 61;
                        }
                        if (i2 == 46) {
                            i1 = 62;
                        }
                        if (i2 == 47) {
                            i1 = 63;
                        }
                        if (i2 == 48) {
                            i1 = 64;
                        }
                        if (i2 == 49) {
                            i1 = 65;
                        }
                        if (i2 == 50) {
                            i1 = 66;
                        }
                        if (i2 == 51) {
                            i1 = 67;
                        }
                        if (i2 == 52) {
                            i1 = 68;
                        }


                        final TableRow row = new TableRow(A4_Printer_Cancel_new.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        row.setGravity(Gravity.CENTER);

                        TableRow.LayoutParams lp, lp1, lp2;

                        TextView tv = new TextView(A4_Printer_Cancel_new.this);
                        tv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        tv.setGravity(Gravity.START);
                        tv.setTextSize(15);
                        //text = cursor.getString(1);
//                String v = na;

                        tv.setText(us_name1.getString(i2));


                        TextView tv1 = new TextView(A4_Printer_Cancel_new.this);
                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        tv1.setGravity(Gravity.START);
                        //text = cursor.getString(1);
                        tv1.setText(us_name1.getString(i1));
                        String value1 = "0";
                        if (tv1.getText().toString().equals("")) {

                        } else {
                            value1 = tv1.getText().toString();
                        }


                        TextView tv2 = new TextView(A4_Printer_Cancel_new.this);
                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                        tv2.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                        tv2.append(us_name1.getString(i2) + " @ " + us_name1.getString(i1) + "%");
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv2.setPadding(0, 0, 20, 0);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(tv2);
//                    Toast.makeText(A4_Printer_Cancel_new.this, "hiii "+na38 + " @ " + us_name1.getString(i1) + "%", Toast.LENGTH_LONG).show();

                        TextView textView1 = new TextView(A4_Printer_Cancel_new.this);
                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(textView1);

//                    Toast.makeText(A4_Printer_Cancel_new.this, " "+i1 + " @ " + value1 + "%", Toast.LENGTH_LONG).show();

                        TextView tv33 = new TextView(A4_Printer_Cancel_new.this);
                        tv33.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                        tv33.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        float mul = Float.parseFloat(value1) * Float.parseFloat(String.valueOf(sub12)) / 100;
//                float mul = Float.parseFloat(value1) * Float.parseFloat(sub) / 100;
//                float mul = Float.parseFloat(value1) * Float.parseFloat(String.valueOf(to)) / 100;
//                float mul = ((Float.parseFloat(total2)+Float.parseFloat(total_disc_print)) / 100) * Float.parseFloat(value1);
//                    float mul = Float.parseFloat(value1) * (Float.parseFloat(total)+Float.parseFloat(total_disc)) / 100;
                        float tota = mul;
                        String tota1 = String.format("%.2f", tota);
                        tv33.setText(String.valueOf(tota));
                        tv33.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        //tv3.setTextColor(Color.parseColor("#000000"));
                        //row.addView(tv3);


                        tv33.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(tv33);

                        String value2 = tv33.getText().toString();
//                    Toast.makeText(A4_Printer_Cancel_new.this, "11 " + String.valueOf(tota1), Toast.LENGTH_LONG).show();

                        if (tv33.getText().toString().equals("0") || tv33.getText().toString().equals("0.0") || tv33.getText().toString().equals("0.00")
                                || tv33.getText().toString().equals("") || tv.getText().toString().equals("")) {

                        } else {
                            tableLayout1.addView(row);

                            String mod1 = us_name1.getString(i2) + " @ " + us_name1.getString(i1) + "%" + "---" + String.valueOf(tota1);

                            addNewItem(document, mod1, Element.ALIGN_LEFT, titleFont);

                        }

                    }
                }

            } else {
//            Toast.makeText(A4_Printer_Cancel_new.this, "user id not there", Toast.LENGTH_LONG).show();
                Cursor cursorr = null;
                if (paymmethoda.toString().equals("  Dine-in") || paymmethoda.toString().equals(getString(R.string.general)) || paymmethoda.toString().equals("  Others")) {
                    cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax1 = 'dine_in'", null);//replace to cursor = dbHelper.fetchAllHotels();
                }
                if (paymmethoda.toString().equals("  Takeaway") || paymmethoda.toString().equals("  Main")) {
                    cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax2 = 'takeaway'", null);//replace to cursor = dbHelper.fetchAllHotels();
                }
                if (paymmethoda.toString().equals("  Home delivery")) {
                    cursorr = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax' AND tax3 = 'homedelivery'", null);//replace to cursor = dbHelper.fetchAllHotels();
                }
//            ccursor = db1.rawQuery("Select * from Taxes WHERE taxtype = 'Globaltax'", null);//replace to ccursor = dbHelper.fetchAllHotels();
                if (cursorr.moveToFirst()) {

                    do {

                        String name = cursorr.getString(1);
                        String value = cursorr.getString(2);

                        final TableRow row = new TableRow(A4_Printer_Cancel_new.this);
                        row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        row.setGravity(Gravity.CENTER);

                        TableRow.LayoutParams lp, lp1, lp2;

//                                final TextView tv = new TextView(A4_Printer_Cancel_new.this);
//                                //tv.setLayoutParams(new TableRow.LayoutParams(40, TableRow.LayoutParams.MATCH_PARENT));
//                                tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.7f));
//                                tv.setTextSize(16);
//                                tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
//                                row.addView(tv);

                        TextView tvv = new TextView(A4_Printer_Cancel_new.this);
                        tvv.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        //tv.setBackgroundResource(R.drawable.cell_shape);
                        tvv.setGravity(Gravity.START);
                        tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tvv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tvv.setText(name);

                        TextView tv1 = new TextView(A4_Printer_Cancel_new.this);
                        tv1.setLayoutParams(new TableRow.LayoutParams(89, 34));
                        tv1.setGravity(Gravity.START);
                        tv1.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv1.setText(value);
                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        String value1 = tv1.getText().toString();

                        TextView tv2 = new TextView(A4_Printer_Cancel_new.this);
                        //lp = new TableRow.LayoutParams(145, TableRow.LayoutParams.WRAP_CONTENT);
                        //tv2.setLayoutParams(lp);
                        tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.6f));
                        tv2.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv2.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                        tv2.append(name + " @ " + value + "%");
                        tv2.setPadding(0, 0, 20, 0);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(tv2);

                        TextView textView1 = new TextView(A4_Printer_Cancel_new.this);
                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        row.addView(textView1);

                        TextView tv3 = new TextView(A4_Printer_Cancel_new.this);
//                lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
//                tv3.setLayoutParams(lp2);
                        tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                        //tv3.setPadding(0,0,10,0);
                        tv3.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                        tv3.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        float mul = Float.parseFloat(value1) * Float.parseFloat(String.valueOf(sub12)) / 100;
                        float tota = mul;
                        String tota1 = String.format("%.2f", tota);
                        tv3.setText(String.valueOf(tota));
                        //row.addView(tv3);


                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        String value2 = tv3.getText().toString();
                        row.addView(tv3);

                        tableLayout1.addView(row);

                        String mod1 = name + " @ " + value + "%" + "---" + String.valueOf(tota1);

                        addNewItem(document, mod1, Element.ALIGN_LEFT, titleFont);

                    } while (cursorr.moveToNext());
                }
            }


            float sum = 0;
            for (int i = 0; i < tableLayout1.getChildCount(); i++) {
                TableRow mRow = (TableRow) tableLayout1.getChildAt(i);
                TextView mTextView = (TextView) mRow.getChildAt(2);
                sum = sum
                        + Float.parseFloat(mTextView.getText().toString());
            }


            String newsum = String.format("%.2f", sum);

            if (sum == 0 || sum == 0.0 || sum == 0.00) {

            } else {

                addNewItemWithLeftAndRight(document, getString(R.string.action_tax), newsum, titleFont, titleFont);

            }


///////////////////////////////// discount


            Cursor cursor5 = db.rawQuery("SELECT * FROM Discountdetails WHERE billno = '" + billnumb + "'", null);
            if (cursor5.moveToFirst()) {
                dsirs = cursor5.getString(7);
                float ds = Float.parseFloat(dsirs);
                dsirs1 = String.format("%.2f", ds);
            } else {
                dsirs = "0";
            }
            TextView dis = new TextView(A4_Printer_Cancel_new.this);
            Cursor cursor7 = db.rawQuery("SELECT * FROM Discountdetails WHERE billno = '" + billnumb + "'", null);
            if (cursor7.moveToFirst()) {
                taxpe = cursor7.getString(5);
                float on = (Float.parseFloat(tot) / 100) * Float.parseFloat(taxpe);
                on1 = String.format("%.1f", on);
                if (on % 1 != 0) {
                    // //////Toast.makeText(A4_Printer_Cancel_new.this, "Decimal values"+newe, Toast.LENGTH_SHORT).show();
                    int newww = (int) on;
                    float decpart = on - newww;
                    ////////Toast.makeText(A4_Printer_Cancel_new.this, "DECIMALLLLLLLLLLLLLLLL"+newww, Toast.LENGTH_SHORT).show();
                    ////////Toast.makeText(A4_Printer_Cancel_new.this, "DECIMALLLLLLLLLLLLLLLL"+decpart, Toast.LENGTH_SHORT).show();
                    if (decpart > 0.5) {
                        ////////Toast.makeText(A4_Printer_Cancel_new.this, getString(R.string.Button9), Toast.LENGTH_SHORT).show();

//                            String rou = round.getText().toString();
//                            float roun = Float.parseFloat(rou);
                        float addall = on;
                        //alltotal.setText(String.valueOf(newww + 1));
                        //TextView round = (TextView) mView.findViewById(R.id.roundvalue);
                        float dee = 1 - decpart;
                        dis.setText("+ " + String.format("%.2f", dee));

                    } else {
                        // //////Toast.makeText(A4_Printer_Cancel_new.this, getString(R.string.Button10), Toast.LENGTH_SHORT).show();
                        //TextView alltotal1 = (TextView) mView.findViewById(R.id.fulltotal1);
                        float addall = on;
                        //alltotal1.setText(String.valueOf(newww));
                        //TextView round1 = (TextView) mView.findViewById(R.id.roundvalue);
                        dis.setText("- " + String.format("%.2f", decpart));

                    }
                } else {
                    ////////Toast.makeText(A4_Printer_Cancel_new.this, "Not having decimals", Toast.LENGTH_SHORT).show();

                    //TextView alltotal1 = (TextView) mView.findViewById(R.id.fulltotal1);
                    int addall = (int) on;
                    //alltotal1.setText(String.valueOf(addall));
                    //TextView round = (TextView) mView.findViewById(R.id.roundvalue);
                    dis.setText("+ " + "0.00");
                    ////////Toast.makeText(MainActivity.this, "decimal is there", Toast.LENGTH_SHORT).show();
                }
            } else {
                taxpe = "0";
                dis.setText("+ " + "0.00");
                on1 = "0";
            }

            //tot-is-subtotal

            String alldiscinperc1 = "Discount(" + taxpe + "%)";
            addNewItemWithLeftAndRight(document, alldiscinperc1, on1, titleFont, titleFont);
            float newe;

//////////////////////////////////////////

            Cursor cursor113 = db.rawQuery("SELECT SUM(disc_indiv_total) FROM All_Sales WHERE bill_no = '" + billnumb + "' AND disc_thereornot = 'yes'", null);
            if (cursor113.moveToFirst()) {
                float level = cursor113.getFloat(0);
                total = String.valueOf(level);
                total1 = Float.parseFloat(total);
                total_disc_print_q = String.format("%.2f", total1);

                addNewItemWithLeftAndRight(document, "Savings", total_disc_print_q, titleFont, titleFont);

            }

////////////////////////////////rounded

            if (taxpe.toString().equals("")) {
                newe = sum + Float.parseFloat(String.valueOf(tot));
            } else {
                newe = sum + Float.parseFloat(String.valueOf(tot)) + Float.parseFloat(on1);
            }

            if (str_round_off.toString().equals("No")) {

            }else {
                TextView round = new TextView(A4_Printer_Cancel_new.this);
                if (newe % 1 != 0) {
                    int newww = (int) newe;
                    float decpart = newe - newww;
                    if (decpart > 0.5) {
                        float addall = newe;
                        float dee = 1 - decpart;
                        round.setText("+ " + String.format("%.2f", dee));
                    } else {
                        float addall = newe;
                        round.setText("- " + String.format("%.2f", decpart));
                    }
                } else {
                    int addall = (int) newe;
                    round.setText("+ " + "0.00");
                }

                addNewItemWithLeftAndRight(document, "Rounded", round.getText().toString(), titleFont, titleFont);

                addLineSeperator(document);

            }

/////////////////////refund

            //19
            Cursor billcan = db.rawQuery("SELECT * FROM All_Sales WHERE bill_no = '" + billnumb + "'", null);
            if (billcan.moveToFirst()) {
                String asd = billcan.getString(18);
                refundamounta = String.valueOf(asd);
            } else {
                Cursor billcan_cancel = db.rawQuery("SELECT * FROM All_Sales_cancelled WHERE bill_no = '" + billnumb + "'", null);
                if (billcan_cancel.moveToFirst()) {
                    String asd = billcan_cancel.getString(17);
                    refundamounta = String.valueOf(asd);
                }
            }

            Cursor c1 = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber = '" + billnumb + "'", null);
            if (c1.moveToFirst()) {
                String c11 = c1.getString(2);
                alltotal1 = c11;
            } else {
                alltotal1 = "0";
            }
            float min = Float.parseFloat(refundamounta) - Float.parseFloat(alltotal1);
            String mn1 = String.valueOf(min);

            addNewItemWithLeftAndRight(document, "Refund", "-"+mn1, titleFont, titleFont);

            Cursor dateq = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billnumb + "'", null);
            if (dateq.moveToFirst()) {
                datee = dateq.getString(25);
                timee = dateq.getString(12);
            } else {
                Cursor date_cancel = db.rawQuery("Select * from All_Sales_cancelled WHERE bill_no = '" + billnumb + "'", null);
                if (date_cancel.moveToFirst()) {
                    datee = date_cancel.getString(22);
                    timee = date_cancel.getString(12);
                }
            }

            String refundtime = "(" + datee + "," + timee + ")";
            addNewItem(document, refundtime, Element.ALIGN_LEFT, titleFont);


            float all = Float.parseFloat(alltotal1);
            String newf = String.valueOf(all);

            addNewItemWithLeftAndRight(document, "Total", "Rs "+newf, titleFont, titleFont);

            addLineSeperator(document);

//            tvkot.setText("Thank you! visit again.");
//            if (tvkot.getText().toString().equals("")) {
//
//            } else {
//                addNewItem(document, "Thank you! visit again.", Element.ALIGN_CENTER, titleFont);
//            }

            tvkot.setText(strbillone);
            if (tvkot.getText().toString().equals("")) {

            } else {
                addNewItem(document, strbillone, Element.ALIGN_CENTER, titleFont);
            }

            document.close();

//            Toast.makeText(this, getString(R.string.title41), Toast.LENGTH_LONG).show();

            printPDF();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void printPDF() {
        PrintManager printManager = (PrintManager)getSystemService(Context.PRINT_SERVICE);
        try {
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(A4_Printer_Cancel_new.this, Common.getAppPath(A4_Printer_Cancel_new.this)+"test_pdf.pdf");
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());
        }catch (Exception ex) {
            Log.v("Rohith", ""+ex.getMessage());
        }
    }

    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textLeftFont, Font textRightFont) throws DocumentException {
        Chunk chunkTextLeft = new Chunk(textLeft, textLeftFont);
        Chunk chunkTextRight = new Chunk(textRight, textRightFont);
        Paragraph p = new Paragraph(chunkTextLeft);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(chunkTextRight);
        document.add(p);
    }

    private void addLineSeperator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0, 0, 0));
//        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
//        addLineSpace(document);
    }

    private void addLineSeperator_light(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(192, 192, 192));
//        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
//        addLineSpace(document);
    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Resume string");
//        Toast.makeText(A4_Printer_Cancel_new.this, "resume", Toast.LENGTH_SHORT).show();
        if (i == 1) {
//            Toast.makeText(A4_Printer_Cancel_new.this, "close", Toast.LENGTH_SHORT).show();
            System.out.println(getString(R.string.close));
            finish();
        }
        i = 1;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Toast.makeText(A4_Printer_Cancel_new.this, "back pressed", Toast.LENGTH_SHORT).show();
        i = 1;
//        A4_Printer_Cancel_new.this.finish();
    }
}
