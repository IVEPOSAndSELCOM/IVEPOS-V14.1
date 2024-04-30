package com.intuition.ivepos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import au.com.bytecode.opencsv.CSVWriter;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.intuition.ivepos.BluetoothPrintDriver.BT_Write;

/**
 * Created by Rohithkumar on 7/20/2017.
 */

public class Discountwise_Saleslist extends AppCompatActivity implements ReceiveListener {


    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;


    SimpleCursorAdapter dataAdapterr;
    Cursor cursor;

    String ipnameget, portget, statusnet, nameget, addget, statussusb;
    String ipnameget_counter, portget_counter, statusnet_counter;

    String statussusbs, statusnets, ipnamegets, portgets, addgets, namegets, papersizeget;
    String statusnets_counter, ipnamegets_counter, portgets_counter;
    String tableida, billtypea, paymmethoda, tableidaa, billtypeaa, paymmethodaa;
    byte[][] allbuf, allbuf1, allbuf2, allbuf3, allbuf4, allbuf5, allbuf6, allbuf7, allbuf8, allbuf9, allbuf10, allbuf11, allbufqty, allbufitems, allbufmodifiers, allbufsubtot,
            allbuftax, allbufdisc, allbufrounded, allbuffulltot, allbuf12, allbuf13, allbuf14,allbufbillno,allbuftime,allbufline1,allbufline,allbufcust,allbufcustname,
            allbufcustadd,allbufcustph,allbufcustemail, allbuftaxestype2, allbuftaxestype1, allbuf1122, allbufKOT;


    byte[] setHT32, setHT321, setHT33, setHT34, setHT3212, setHTKOT, feedcut2;
    int nPaperWidth;
    String strcompanyname, straddress1, straddress2, straddress3, strphone, stremailid, strwebsite, strtaxone, strbillone;
    String NAME;
    int charlength, charlength1, charlength2, quanlentha;

    String total, companynameis;

    ListView listView;
    EditText search;

    File file=null;
    SimpleDateFormat sdff2, sdff1;
    String currentDateandTimee1;
    String timee1;

    List toEmailList;

    private int hour;
    private int minute;

    TextView refundamount, refundpercentage, crasherpercentage, crashername, refundamount_aa;
    TextView discounttotal;

    TextView tvkot;
    TextView totalsales, totalsales_r, noofbills, avgsales, avgsales_r;

    String response;
    GoogleAccountCredential mCredential;
    ProgressDialog mProgress;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Gmail API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { GmailScopes.GMAIL_SEND };

    EditText editText1_filter, editText2_filter;

    TextView editText1, editText2, editText11, editText22;
    TextView editText_from_day_hide, editText_from_day_visible, editText_to_day_hide, editText_to_day_visible;
    private int year, year1;
    private int month, month1;
    private int day, day1;

    String onee, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve;
    String onee1, two1, three1, four1, five1, six1, seven1, eight1, nine1, ten1, eleven1, twelve1;

    int clickcount=1, clickcounts = 1;

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
    String account_selection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discountwise_saleslist);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(Discountwise_Saleslist.this);
        account_selection= sharedpreferences_select.getString("account_selection", null);

        mContext = this;

        mSpnSeries = (Spinner) findViewById(R.id.spnModel);
        ArrayAdapter<SpnModelsItem> seriesAdapter = new ArrayAdapter<SpnModelsItem>(Discountwise_Saleslist.this, android.R.layout.simple_spinner_item);
        seriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t82), com.epson.epos2.printer.Printer.TM_T82));
        mSpnSeries.setAdapter(seriesAdapter);
        mSpnSeries.setSelection(0);

        mSpnLang = (Spinner) findViewById(R.id.spnLang);
        ArrayAdapter<SpnModelsItem> langAdapter = new ArrayAdapter<SpnModelsItem>(Discountwise_Saleslist.this, android.R.layout.simple_spinner_item);
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_ank), com.epson.epos2.printer.Printer.MODEL_ANK));
        mSpnLang.setAdapter(langAdapter);
        mSpnLang.setSelection(0);


//        try {
//            com.epson.epos2.Log.setLogSettings(mContext, com.epson.epos2.Log.PERIOD_TEMPORARY, com.epson.epos2.Log.OUTPUT_STORAGE, null, 0, 1, com.epson.epos2.Log.LOGLEVEL_LOW);
//        } catch (Exception e) {
////            Toast.makeText(Discountwise_Saleslist.this, "Here8", Toast.LENGTH_SHORT).show();
//            ShowMsg.showException(e, "setLogSettings", mContext);
//        }
        mEditTarget = (EditText) findViewById(R.id.edtTarget);

        editText1_filter = new EditText(Discountwise_Saleslist.this);
        editText2_filter = new EditText(Discountwise_Saleslist.this);

        editText1 = (TextView) findViewById(R.id.editText1);
//        editText1.setText(currentDateandTime1);
        editText2 = (TextView) findViewById(R.id.editText2);
//        editText2.setText(currentDateandTime1);

        editText11 = (TextView) findViewById(R.id.editText11);
//        editText11.setText(currentDateandTime2);


        editText22 = (TextView) findViewById(R.id.editText22);
//        editText22.setText(currentDateandTime2);


        editText_from_day_hide = (TextView) findViewById(R.id.editText_from_day_hide);
        editText_from_day_visible = (TextView) findViewById(R.id.editText_from_day_visible);


        editText_to_day_hide = (TextView) findViewById(R.id.editText_to_day_hide);
        editText_to_day_visible = (TextView) findViewById(R.id.editText_to_day_visible);


        editText11.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        datePickerListener,now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.show(Discountwise_Saleslist.this.getFragmentManager(), "Datepickerdialog");




            }

            com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener datePickerListener
                    = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog, int selectedYear1, int selectedMonth1, int selectedDay1) {
                    year1 = selectedYear1;
                    month1 = selectedMonth1;
                    day1 = selectedDay1;

                    // set selected date into textview
                    populateSetDate(year1, month1 + 1, day1);
                }
            };





            public void populateSetDate(int year, int month, int day) {
                TextView mEdit = (TextView) findViewById(R.id.editText1);
                TextView mEdit1  = (TextView) findViewById(R.id.editText11);
                if (month == 1 && day < 10) {
                    mEdit.setText(year + " " + "0" + 1 + " " + "0" + day);
                    onee1 = "0" + day + " " + "Jan" + " " + year;
                    mEdit1.setText(onee1);
                } else {
                    if (month == 1) {
                        mEdit.setText(year + " " + "0" + 1 + " " + day);
                        onee = day + " " + "Jan" + " " + year;
                        mEdit1.setText(onee);
                    }
                }

                if (month == 2 && day < 10) {
                    mEdit.setText(year + " " + "0" + 2 + " " + "0" + day);
                    two1 = "0" + day + " " + "Feb" + " " + year;
                    mEdit1.setText(two1);
                } else {
                    if (month == 2) {
                        mEdit.setText(year + " " + "0" + 2 + " " + day);
                        two = day + " " + "Feb" + " " + year;
                        mEdit1.setText(two);
                    }
                }

                if (month == 3 && day < 10) {
                    mEdit.setText(year + " " + "0" + 3 + " " + "0" + day);
                    three1 = "0" + day + " " + "Mar" + " " + year;
                    mEdit1.setText(three1);
                } else {
                    if (month == 3) {
                        mEdit.setText(year + " " + "0" + 3 + " " + day);
                        three = day + " " + "Mar" + " " + year;
                        mEdit1.setText(three);
                    }
                }

                if (month == 4 && day < 10) {
                    mEdit.setText(year + " " + "0" + 4 + " " + "0" + day);
                    four1 = "0" + day + " " + "Apr" + " " + year;
                    mEdit1.setText(four1);
                } else {
                    if (month == 4) {
                        mEdit.setText(year + " " + "0" + 4 + " " + day);
                        four = day + " " + "Apr" + " " + year;
                        mEdit1.setText(four);
                    }
                }

                if (month == 5 && day < 10) {
                    mEdit.setText(year + " " + "0" + 5 + " " + "0" + day);
                    five1 = "0" + day + " " + "May" + " " + year;
                    mEdit1.setText(five1);
                } else {
                    if (month == 5) {
                        mEdit.setText(year + " " + "0" + 5 + " " + day);
                        five = day + " " + "May" + " " + year;
                        mEdit1.setText(five);
                    }
                }

                if (month == 6 && day < 10) {
                    mEdit.setText(year + " " + "0" + 6 + " " + "0" + day);
                    six1 = "0" + day + " " + "Jun" + " " + year;
                    mEdit1.setText(six1);
                } else {
                    if (month == 6) {
                        mEdit.setText(year + " " + "0" + 6 + " " + day);
                        six = day + " " + "Jun" + " " + year;
                        mEdit1.setText(six);
                    }
                }

                if (month == 7 && day < 10) {
                    mEdit.setText(year + " " + "0" + 7 + " " + "0" + day);
                    seven1 = "0" + day + " " + "Jul" + " " + year;
                    mEdit1.setText(seven1);
                } else {
                    if (month == 7) {
                        mEdit.setText(year + " " + "0" + 7 + " " + day);
                        seven = day + " " + "Jul" + " " + year;
                        mEdit1.setText(seven);
                    }
                }

                if (month == 8 && day < 10) {
                    mEdit.setText(year + " " + "0" + 8 + " " + "0" + day);
                    eight1 = "0" + day + " " + "Aug" + " " + year;
                    mEdit1.setText(eight1);
                } else {
                    if (month == 8) {
                        mEdit.setText(year + " " + "0" + 8 + " " + day);
                        eight = day + " " + "Aug" + " " + year;
                        mEdit1.setText(eight);
                    }
                }

                if (month == 9 && day < 10) {
                    mEdit.setText(year + " " + "0" + 9 + " " + "0" + day);
                    nine1 = "0" + day + " " + "Sep" + " " + year;
                    mEdit1.setText(nine1);
                } else {
                    if (month == 9) {
                        mEdit.setText(year + " " + "0" + 9 + " " + day);
                        nine = day + " " + "Sep" + " " + year;
                        mEdit1.setText(nine);
                    }
                }

                if (month == 10 && day < 10) {
                    mEdit.setText(year + " " + 10 + " " + "0" + day);
                    ten1 = "0" + day + " " + "Oct" + " " + year;
                    mEdit1.setText(ten1);
                } else {
                    if (month == 10) {
                        mEdit.setText(year + " " + 10 + " " + day);
                        ten = day + " " + "Oct" + " " + year;
                        mEdit1.setText(ten);
                    }
                }

                if (month == 11 && day < 10) {
                    mEdit.setText(year + " " + 11 + " " + "0" + day);
                    eleven1 = "0" + day + " " + "Nov" + " " + year;
                    mEdit1.setText(eleven1);
                } else {
                    if (month == 11) {
                        mEdit.setText(year + " " + 11 + " " + day);
                        eleven = day + " " + "Nov" + " " + year;
                        mEdit1.setText(eleven);
                    }
                }

                if (month == 12 && day < 10) {
                    mEdit.setText(year + " " + 12 + " " + "0" + day);
                    twelve1 = "0" + day + " " + "Dec" + " " + year;
                    mEdit1.setText(twelve1);
                } else {
                    if (month == 12) {
                        mEdit.setText(year + " " + 12 + " " + day);
                        twelve = day + " " + "Dec" + " " + year;
                        mEdit1.setText(twelve);
                    }
                }

            }

        });


        editText22.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Calendar now = Calendar.getInstance();
//                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
//                        datePickerListener,
//                        now.get(Calendar.YEAR),
//                        now.get(Calendar.MONTH),
//                        now.get(Calendar.DAY_OF_MONTH)
//
//
//                );
//
//                dpd.show(Itemwise_Saleslist.this.getFragmentManager(), "Datepickerdialog");
                //if (clickcount == 1){
                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        datePickerListener,now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.show(Discountwise_Saleslist.this.getFragmentManager(), "Datepickerdialog");
                clickcount++;
//                }else {
//                    Calendar now = Calendar.getInstance();
//                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
//                            datePickerListener, year, month, day
//                    );
//
//                    dpd.show(Itemwise_Saleslist.this.getFragmentManager(), "Datepickerdialog");
//                }

            }

            com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener datePickerListener
                    = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog, int selectedYear, int selectedMonth, int selectedDay) {
                    year = selectedYear;
                    month = selectedMonth;
                    day = selectedDay;

                    // set selected date into textview
                    populateSetDate(year, month + 1, day);
                }
            };

//                // when dialog box is closed, below method will be called.
//                public void onDateSet(DatePicker view, int selectedYear,
//                                      int selectedMonth, int selectedDay) {
//
//
//
//
//                }
//            };


            public void populateSetDate(int year, int month, int day) {
                TextView mEdit = (TextView) findViewById(R.id.editText2);
                TextView mEdit1  = (TextView) findViewById(R.id.editText22);
                if (month == 1 && day < 10) {
                    mEdit.setText(year + " " + "0" + 1 + " " + "0" + day);
                    onee1 = "0" + day + " " + "Jan" + " " + year;
                    mEdit1.setText(onee1);
                } else {
                    if (month == 1) {
                        mEdit.setText(year + " " + "0" + 1 + " " + day);
                        onee = day + " " + "Jan" + " " + year;
                        mEdit1.setText(onee);
                    }
                }

                if (month == 2 && day < 10) {
                    mEdit.setText(year + " " + "0" + 2 + " " + "0" + day);
                    two1 = "0" + day + " " + "Feb" + " " + year;
                    mEdit1.setText(two1);
                } else {
                    if (month == 2) {
                        mEdit.setText(year + " " + "0" + 2 + " " + day);
                        two = day + " " + "Feb" + " " + year;
                        mEdit1.setText(two);
                    }
                }

                if (month == 3 && day < 10) {
                    mEdit.setText(year + " " + "0" + 3 + " " + "0" + day);
                    three1 = "0" + day + " " + "Mar" + " " + year;
                    mEdit1.setText(three1);
                } else {
                    if (month == 3) {
                        mEdit.setText(year + " " + "0" + 3 + " " + day);
                        three = day + " " + "Mar" + " " + year;
                        mEdit1.setText(three);
                    }
                }

                if (month == 4 && day < 10) {
                    mEdit.setText(year + " " + "0" + 4 + " " + "0" + day);
                    four1 = "0" + day + " " + "Apr" + " " + year;
                    mEdit1.setText(four1);
                } else {
                    if (month == 4) {
                        mEdit.setText(year + " " + "0" + 4 + " " + day);
                        four = day + " " + "Apr" + " " + year;
                        mEdit1.setText(four);
                    }
                }

                if (month == 5 && day < 10) {
                    mEdit.setText(year + " " + "0" + 5 + " " + "0" + day);
                    five1 = "0" + day + " " + "May" + " " + year;
                    mEdit1.setText(five1);
                } else {
                    if (month == 5) {
                        mEdit.setText(year + " " + "0" + 5 + " " + day);
                        five = day + " " + "May" + " " + year;
                        mEdit1.setText(five);
                    }
                }

                if (month == 6 && day < 10) {
                    mEdit.setText(year + " " + "0" + 6 + " " + "0" + day);
                    six1 = "0" + day + " " + "Jun" + " " + year;
                    mEdit1.setText(six1);
                } else {
                    if (month == 6) {
                        mEdit.setText(year + " " + "0" + 6 + " " + day);
                        six = day + " " + "Jun" + " " + year;
                        mEdit1.setText(six);
                    }
                }

                if (month == 7 && day < 10) {
                    mEdit.setText(year + " " + "0" + 7 + " " + "0" + day);
                    seven1 = "0" + day + " " + "Jul" + " " + year;
                    mEdit1.setText(seven1);
                } else {
                    if (month == 7) {
                        mEdit.setText(year + " " + "0" + 7 + " " + day);
                        seven = day + " " + "Jul" + " " + year;
                        mEdit1.setText(seven);
                    }
                }

                if (month == 8 && day < 10) {
                    mEdit.setText(year + " " + "0" + 8 + " " + "0" + day);
                    eight1 = "0" + day + " " + "Aug" + " " + year;
                    mEdit1.setText(eight1);
                } else {
                    if (month == 8) {
                        mEdit.setText(year + " " + "0" + 8 + " " + day);
                        eight = day + " " + "Aug" + " " + year;
                        mEdit1.setText(eight);
                    }
                }

                if (month == 9 && day < 10) {
                    mEdit.setText(year + " " + "0" + 9 + " " + "0" + day);
                    nine1 = "0" + day + " " + "Sep" + " " + year;
                    mEdit1.setText(nine1);
                } else {
                    if (month == 9) {
                        mEdit.setText(year + " " + "0" + 9 + " " + day);
                        nine = day + " " + "Sep" + " " + year;
                        mEdit1.setText(nine);
                    }
                }

                if (month == 10 && day < 10) {
                    mEdit.setText(year + " " + 10 + " " + "0" + day);
                    ten1 = "0" + day + " " + "Oct" + " " + year;
                    mEdit1.setText(ten1);
                } else {
                    if (month == 10) {
                        mEdit.setText(year + " " + 10 + " " + day);
                        ten = day + " " + "Oct" + " " + year;
                        mEdit1.setText(ten);
                    }
                }

                if (month == 11 && day < 10) {
                    mEdit.setText(year + " " + 11 + " " + "0" + day);
                    eleven1 = "0" + day + " " + "Nov" + " " + year;
                    mEdit1.setText(eleven1);
                } else {
                    if (month == 11) {
                        mEdit.setText(year + " " + 11 + " " + day);
                        eleven = day + " " + "Nov" + " " + year;
                        mEdit1.setText(eleven);
                    }
                }

                if (month == 12 && day < 10) {
                    mEdit.setText(year + " " + 12 + " " + "0" + day);
                    twelve1 = "0" + day + " " + "Dec" + " " + year;
                    mEdit1.setText(twelve1);
                } else {
                    if (month == 12) {
                        mEdit.setText(year + " " + 12 + " " + day);
                        twelve = day + " " + "Dec" + " " + year;
                        mEdit1.setText(twelve);
                    }
                }

            }

//            class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
//                @Override
//                public Dialog onCreateDialog(Bundle savedInstanceState) {
//                    final Calendar calendar = Calendar.getInstance();
//                    int yy = calendar.get(Calendar.YEAR);
//                    int mm = calendar.get(Calendar.MONTH);
//                    int dd = calendar.get(Calendar.DAY_OF_MONTH);
//                    return new DatePickerDialog(Itemwise_Saleslist.this, this, yy, mm, dd);
//                }
//
//
//                @Override
//                public void onDateSet(DatePicker view, int yy, int mm, int dd) {
//                    populateSetDate(yy, mm + 1, dd);
//                }
//            }

        });

        editText_from_day_visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(Discountwise_Saleslist.this, R.style.timepicker_date_dialog, timePickerListener_open, hour, minute,
                        false);

                timePickerDialog.show();
            }
        });

        editText_to_day_visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(Discountwise_Saleslist.this, R.style.timepicker_date_dialog, timePickerListener_close, hour, minute,
                        false);

                timePickerDialog.show();
            }
        });


        listView = (ListView) findViewById(R.id.listView11);

        search = (EditText) findViewById(R.id.searchView);

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search.getWindowToken(), 0);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ImageView deleteicon = (ImageView) findViewById(R.id.delete_icon);
        deleteicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
            }
        });

        LinearLayout back_activity = (LinearLayout) findViewById(R.id.back_activity);
        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        db = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        db1 = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        Cursor cursor_country = db1.rawQuery("SELECT * FROM Country_Selection", null);
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

        Cursor ccornn = db1.rawQuery("SELECT * FROM BTConn", null);
        if (ccornn.moveToFirst()) {
            nameget = ccornn.getString(1);
            addget = ccornn.getString(2);
            statussusb = ccornn.getString(3);
            mEditTarget.setText(addget);
        }

        tvkot = new TextView(Discountwise_Saleslist.this);
        totalsales_r = new TextView(Discountwise_Saleslist.this);
        avgsales_r = new TextView(Discountwise_Saleslist.this);

        refundamount = new TextView(Discountwise_Saleslist.this);
        refundpercentage = new TextView(Discountwise_Saleslist.this);
        crasherpercentage = new TextView(Discountwise_Saleslist.this);
        crashername = new TextView(Discountwise_Saleslist.this);

        refundamount_aa = new TextView(Discountwise_Saleslist.this);

        discounttotal = (TextView) findViewById(R.id.totaldiscount);

        final Calendar c = Calendar.getInstance();
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);

        mCredential = GoogleAccountCredential.usingOAuth2(
                Discountwise_Saleslist.this.getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        mProgress = new ProgressDialog(Discountwise_Saleslist.this, R.style.timepicker_date_dialog);
        mProgress.setMessage(getString(R.string.setmessage14));

        Bundle extras = getIntent().getExtras();
        String player1name = extras.getString("PLAYER1NAME");
        String player2name = extras.getString("PLAYER2NAME");
        String player3name = extras.getString("PLAYER3NAME");
        String str_edittext1 = extras.getString("edittext1");
        String str_edittext11 = extras.getString("edittext11");
        String str_edittext2 = extras.getString("edittext2");
        String str_edittext22 = extras.getString("edittext22");
        String str_edittext_from_day_visible = extras.getString("edittext_from_day_visible");
        String str_edittext_from_day_hide = extras.getString("edittext_from_day_hide");
        String str_edittext_to_day_visible = extras.getString("edittext_to_day_visible");
        String str_edittext_to_day_hide = extras.getString("edittext_to_day_hide");

        editText1_filter.setText(player1name);
        editText2_filter.setText(player2name);

        editText1.setText(str_edittext1);
        editText11.setText(str_edittext11);
        editText2.setText(str_edittext2);
        editText22.setText(str_edittext22);
        editText_from_day_visible.setText(str_edittext_from_day_visible);
        editText_from_day_hide.setText(str_edittext_from_day_hide);
        editText_to_day_visible.setText(str_edittext_to_day_visible);
        editText_to_day_hide.setText(str_edittext_to_day_hide);


        Cursor cursor11 = db.rawQuery("SELECT SUM(Discount_rupees) FROM Discountdetails WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"'", null);
        if (cursor11.moveToFirst()) {
            int level = cursor11.getInt(0);
            total = String.valueOf(level);
        }

        String rs = String.valueOf(insert1_cc);
        discounttotal.setText(insert1_cc);
        discounttotal.append(total);

        final Cursor cursor_country1 = db1.rawQuery("SELECT * FROM Country_Selection", null);
        if (cursor_country1.moveToFirst()){
            str_country = cursor_country1.getString(1);
        }

        cursor = db.rawQuery("Select * from Discountdetails WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
        // The desired columns to be bound
        String[] fromFieldNames = {"date1", "time", "billno", "Discountcodeno", "Discount_percent", "Billamount_rupess", "Discount_rupees", "Discount_rupees", "Discount_rupees"};
        // the XML defined views which the data will be bound to
        int[] toViewsID = {R.id.date, R.id.time, R.id.billno, R.id.discountcode, R.id.discountpercent, R.id.billamount, R.id.discountrs, R.id.inn, R.id.inn1};
        Log.e("Checamos que hay id", String.valueOf(R.id.name));
        dataAdapterr = new SimpleCursorAdapter(Discountwise_Saleslist.this, R.layout.discount_list, cursor, fromFieldNames, toViewsID, 0);
//        listView.setAdapter(dataAdapterr);// Assign adapter to ListView.... here... the bitch error
        dataAdapterr.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.inn || view.getId() == R.id.inn1) {
                    final String tadl_id = cursor_country1.getString(cursor_country1.getColumnIndex("country"));
                    TextView dateTextView = (TextView) view;
                    if (tadl_id.toString().equals("India")){
                        dateTextView.setText(insert1_cc);
                    }else {
                        dateTextView.setText(insert1_cc);
                    }
                    return true;
                }
                return false;
            }
        });
        listView.setAdapter(dataAdapterr);

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
//                if (search.getText().toString().equals("")) {
//                    Cursor cursor11_1 = db.rawQuery("SELECT SUM(Discount_rupees) FROM Discountdetails WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "'", null);
//                    if (cursor11_1.moveToFirst()) {
//                        int level = cursor11_1.getInt(0);
//                        String total = String.valueOf(level);
//
//                        discounttotal.setText(insert1_cc);
//                        discounttotal.append(total);
//                    }
//                }else {
                    Cursor cursor11_1 = db.rawQuery("SELECT SUM(Discount_rupees) FROM Discountdetails WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' AND (billno LIKE '%" + search.getText().toString() + "%' OR Discountcodeno LIKE '%" + search.getText().toString() + "%')", null);
                    if (cursor11_1.moveToFirst()) {
                        int level = cursor11_1.getInt(0);
                        String total = String.valueOf(level);

                        discounttotal.setText(insert1_cc);
                        discounttotal.append(total);
                    }
//                }
                dataAdapterr.getFilter().filter(s.toString());
            }
        });

        dataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {

                return fetchCountriesByName(constraint.toString());
            }
        });


        Button btnok = (Button) findViewById(R.id.okok);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String r1, r2, r3, r4;
                r1 = editText1.getText().toString();
                r2 = editText2.getText().toString();
                if (r1.toString().contains(" ")) {
                    r1 = r1.replace(" ", "");
                }
                if (r2.toString().contains(" ")) {
                    r2 = r2.replace(" ", "");
                }

                r3 = editText_from_day_hide.getText().toString();
                r4 = editText_to_day_hide.getText().toString();
                if (r3.toString().contains(":")) {
                    r3 = r3.replace(":", "");
                }
                if (r4.toString().contains(":")) {
                    r4 = r4.replace(":", "");
                }

                editText1_filter.setText(r1 + "" + r3);
                editText2_filter.setText(r2 + "" + r4);


                Cursor cursor11 = db.rawQuery("SELECT SUM(Discount_rupees) FROM Discountdetails WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"'", null);
                if (cursor11.moveToFirst()) {
                    int level = cursor11.getInt(0);
                    total = String.valueOf(level);
                }

                String rs = String.valueOf(insert1_cc);
                discounttotal.setText(insert1_cc);
                discounttotal.append(total);


                cursor = db.rawQuery("Select * from Discountdetails WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"'", null);//replace to cursor = dbHelper.fetchAllHotels();
                // The desired columns to be bound
                String[] fromFieldNames = {"_id", "date1", "time", "billno", "Discountcodeno", "Discount_percent", "Billamount_rupess", "Discount_rupees", "Discount_rupees", "Discount_rupees"};
                // the XML defined views which the data will be bound to
                int[] toViewsID = {R.id.sno, R.id.date, R.id.time, R.id.billno, R.id.discountcode, R.id.discountpercent, R.id.billamount, R.id.discountrs, R.id.inn, R.id.inn1};
                Log.e("Checamos que hay id", String.valueOf(R.id.name));
                dataAdapterr = new SimpleCursorAdapter(Discountwise_Saleslist.this, R.layout.discount_list, cursor, fromFieldNames, toViewsID, 0);
//                listView.setAdapter(dataAdapterr);// Assign adapter to ListView.... here... the bitch error
                dataAdapterr.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                        if (view.getId() == R.id.inn || view.getId() == R.id.inn1) {
                            final String tadl_id = cursor_country1.getString(cursor_country1.getColumnIndex("country"));
                            TextView dateTextView = (TextView) view;
                            if (tadl_id.toString().equals("India")){
                                dateTextView.setText(insert1_cc);
                            }else {
                                dateTextView.setText(insert1_cc);
                            }
                            return true;
                        }
                        return false;
                    }
                });
                listView.setAdapter(dataAdapterr);

                search.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        Cursor cursor11_1 = db.rawQuery("SELECT SUM(Discount_rupees) FROM Discountdetails WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' AND (billno LIKE '%" + search.getText().toString() + "%' OR Discountcodeno LIKE '%" + search.getText().toString() + "%')", null);
                        if (cursor11_1.moveToFirst()) {
                            int level = cursor11_1.getInt(0);
                            String total = String.valueOf(level);

                            discounttotal.setText(insert1_cc);
                            discounttotal.append(total);
                        }
                        dataAdapterr.getFilter().filter(s.toString());
                    }
                });

                dataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
                    public Cursor runQuery(CharSequence constraint) {

                        return fetchCountriesByName(constraint.toString());
                    }
                });

            }
        });


        ImageButton action_print = (ImageButton) findViewById(R.id.action_print);
        action_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataAdapterr.isEmpty()){
                    //MenuItem bedMenuItem = menu.findItem(R.id.action_export);
                    //bedMenuItem.setVisible(false);
                    Toast.makeText(Discountwise_Saleslist.this, getString(R.string.no_report_to_export), Toast.LENGTH_SHORT).show();
                }else {
                    Cursor connnet = db1.rawQuery("SELECT * FROM IPConn", null);
                    if (connnet.moveToFirst()) {
                        ipnameget = connnet.getString(1);
                        portget = connnet.getString(2);
                        statusnet = connnet.getString(3);
                    }

                    Cursor connnet_counter = db1.rawQuery("SELECT * FROM IPConn_Counter", null);
                    if (connnet_counter.moveToFirst()) {
                        ipnameget_counter = connnet_counter.getString(1);
                        portget_counter = connnet_counter.getString(2);
                        statusnet_counter = connnet_counter.getString(3);
                    }
                    connnet_counter.close();

                    Cursor conn = db1.rawQuery("SELECT * FROM BTConn", null);
                    if (conn.moveToFirst()) {
                        nameget = conn.getString(1);
                        addget = conn.getString(2);
                        statussusb = conn.getString(3);
                    }
                    if (statusnet.toString().equals("ok") || statusnet_counter.toString().equals("ok") || statussusb.toString().equals("ok")) {

                        printbillcopy_minireceipt();
                        //dialog.dismiss();


                    }else {
                        final Dialog dialogconn = new Dialog(Discountwise_Saleslist.this, R.style.notitle);
                        dialogconn.setContentView(R.layout.dialog_printer_conn_error_orderlist);

                        Button conti = (Button) dialogconn.findViewById(R.id.ok);
                        conti.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(getActivity(), "checkprinterconncash11", Toast.LENGTH_SHORT).show();
                                dialogconn.dismiss();
                            }
                        });

                        dialogconn.show();
                    }
                }
            }
        });


        ImageButton action_export = (ImageButton) findViewById(R.id.action_export);
        action_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataAdapterr.isEmpty()) {
                    Toast.makeText(Discountwise_Saleslist.this, getString(R.string.no_report_to_export), Toast.LENGTH_SHORT).show();
                }else {
                    sdff2 = new SimpleDateFormat("ddMMMyy");
                    currentDateandTimee1 = sdff2.format(new Date());

                    Date dt = new Date();
                    sdff1 = new SimpleDateFormat("hhmmssaa");
                    timee1 = sdff1.format(dt);

                    ExportDatabaseCSVTask task = new ExportDatabaseCSVTask();
                    task.execute();
                }
            }
        });


        ImageButton action_export_mail = (ImageButton) findViewById(R.id.action_exportmail);
        action_export_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataAdapterr.isEmpty()) {
                    Toast.makeText(Discountwise_Saleslist.this, getString(R.string.no_report_to_export), Toast.LENGTH_SHORT).show();
                }else {
                    sdff2 = new SimpleDateFormat("ddMMMyy");
                    currentDateandTimee1 = sdff2.format(new Date());

                    Date dt1 = new Date();
                    sdff1 = new SimpleDateFormat("hhmmssaa");
                    timee1 = sdff1.format(dt1);

                    Cursor cursor = db1.rawQuery("SELECT * FROM Companydetailss", null);
                    if (cursor.moveToFirst()) {
                        companynameis = cursor.getString(1);
                    }else {
                        companynameis = "";
                    }

                    Cursor ccursore = db1.rawQuery("SELECT * FROM Email_setup", null);
                    if (ccursore.moveToFirst()) {
                        Cursor ccursoree = db1.rawQuery("SELECT * FROM Email_recipient", null);
                        if (ccursoree.moveToFirst()) {
                            File dbFile = getDatabasePath("mydb_Salesdata");
                            //Log.v(TAG, "Db path is: "+dbFile);  //get the path of db

//                            File exportDir = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_reports/IVEPOS_discount_list");
                            File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_reports/IVEPOS_discount_list");
                            if (!exportDir.exists()) {
                                exportDir.mkdirs();
                            }

                            file = new File(exportDir, "IvePOS_discount_list" + currentDateandTimee1 + "_" + timee1 + ".csv");
                            try {

                                file.createNewFile();
                                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

                                //ormlite core method
//                List<Person> listdata=dbhelper.GetDataPerson();
//                Person person=null;

                                // this is the Column of the table and same for Header of CSV file
                                String arrStr1[] = {"Date", "Time", "Bill No.", "Discount code", "Discount(%)", "Discount("+insert1_rs+")", "Net sales"};
                                csvWrite.writeNext(arrStr1);


                                Cursor curCSV = db.rawQuery("SELECT * FROM Discountdetails", null);
                                //csvWrite.writeNext(curCSV.getColumnNames());

                                while (curCSV.moveToNext()) {
                                    String arrStr[] = {curCSV.getString(8), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4),
                                            curCSV.getString(5), curCSV.getString(7), curCSV.getString(6)};
//	                curCSV.getString(2),curCSV.getString(3),curCSV.getString(4),
                                    csvWrite.writeNext(arrStr);

                                }

                                csvWrite.close();


                            } catch (IOException e) {
                                Log.e("MainActivity", e.getMessage(), e);


                            }

                            Uri u1 = null;
                            u1 = Uri.fromFile(file);
                        }
                    }


                    String totalgg = "", ruppercent1 = "";
                    Cursor cursor11 = db.rawQuery("SELECT SUM(total) FROM Billnumber", null);
                    if (cursor11.moveToFirst()) {
                        int levelgg = cursor11.getInt(0);
                        totalgg = String.valueOf(levelgg);
                    }

                    if (Integer.parseInt(totalgg) == 0 || totalgg.toString().length() < 0) {

                    } else {
                        float perc = Float.parseFloat(total) * 100 / Float.parseFloat(totalgg);
                        ruppercent1 = String.format("%.1f", perc);
                    }


//                Toast.makeText(getActivity(), Text+" total is "+total, Toast.LENGTH_SHORT).show();


                    String url = "www.intuitionsoftwares.com";

                    String msg = "Discount list (list attached)\n\n" +
                            "Total sales: "+insert1_rs+" " + totalgg + "\n\n" +
                            "Discounts: " + discounttotal.getText().toString() + "(" + ruppercent1 + "%)\n\n" +
                            "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
                            "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
                            "Powered by: " + Uri.parse(url);



                    Cursor cursore = db1.rawQuery("SELECT * FROM Email_setup", null);
                    if (cursore.moveToFirst()){
                        Cursor cursoree = db1.rawQuery("SELECT * FROM Email_recipient", null);
                        if (cursoree.moveToFirst()){
                            //both are there
                            Cursor cursoor = db1.rawQuery("SELECT * FROM Email_setup", null);
                            if (cursoor.moveToFirst()) {
                                String un = cursoor.getString(1);
                                String pwd = cursoor.getString(2);
                                String em_ca = cursoor.getString(3);
                                if (em_ca.toString().equals("Gmail")) {
                                    getResultsFromApi();
                                    new MakeRequestTask(mCredential).execute();
                                }else {
                                    if (em_ca.toString().equals("Yahoo")){
//                                        Toast.makeText(getActivity(), "yahoo "+un, Toast.LENGTH_LONG).show();
                                        Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
                                        if (cursor1.moveToFirst()) {
                                            do {
                                                String unn = cursor1.getString(3);
                                                String toEmails = unn;
                                                toEmailList = Arrays.asList(toEmails
                                                        .split("\\s*,\\s*"));
                                                new SendMailTask_Yahoo_attachment_Discountlist(Discountwise_Saleslist.this).execute(un,
                                                        pwd, toEmailList, companynameis, msg, currentDateandTimee1, timee1);
                                            } while (cursor1.moveToNext());
                                        }


                                    }else {
                                        if (em_ca.toString().equals("Hotmail")){
//                                            Toast.makeText(getActivity(), "Hotmail and Outlook "+un, Toast.LENGTH_LONG).show();
                                            Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
                                            if (cursor1.moveToFirst()) {
                                                do {
                                                    String unn = cursor1.getString(3);
                                                    String toEmails = unn;
                                                    toEmailList = Arrays.asList(toEmails
                                                            .split("\\s*,\\s*"));
                                                    new SendMailTask_Hotmail_Outlook_attachment_Discountlist(Discountwise_Saleslist.this).execute(un,
                                                            pwd, toEmailList, companynameis, msg, currentDateandTimee1, timee1);
                                                } while (cursor1.moveToNext());
                                            }
                                        }else {
                                            if (em_ca.toString().equals("Office365")) {
//                                                Toast.makeText(getActivity(), "office 365 " + un, Toast.LENGTH_LONG).show();
                                                Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
                                                if (cursor1.moveToFirst()) {
                                                    do {
                                                        String unn = cursor1.getString(3);
                                                        String toEmails = unn;
                                                        toEmailList = Arrays.asList(toEmails
                                                                .split("\\s*,\\s*"));
                                                        new SendMailTask_Office365_attachment_Discountlist(Discountwise_Saleslist.this).execute(un,
                                                                pwd, toEmailList, companynameis, msg, currentDateandTimee1, timee1);
                                                    } while (cursor1.moveToNext());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }else {
                            //only recipient not there
                            final Dialog dialoge = new Dialog(Discountwise_Saleslist.this, R.style.timepicker_date_dialog);
                            dialoge.setContentView(R.layout.email_prerequisites);
                            dialoge.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                            dialoge.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            dialoge.show();

                            ImageButton btncancel = (ImageButton) dialoge.findViewById(R.id.btncancel);
                            btncancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialoge.dismiss();
                                }
                            });

                            ImageView recipient_notset = (ImageView) dialoge.findViewById(R.id.recipient_notset);
                            ImageView recipient_set = (ImageView) dialoge.findViewById(R.id.recipient_set);

                            ImageView sender_notset = (ImageView) dialoge.findViewById(R.id.sender_notset);
                            ImageView sender_set = (ImageView) dialoge.findViewById(R.id.sender_set);

                            recipient_notset.setVisibility(View.VISIBLE);

                            sender_set.setVisibility(View.VISIBLE);

                            Button gotosettings = (Button) dialoge.findViewById(R.id.gotosettings);
                            gotosettings.setVisibility(View.GONE);
                            gotosettings.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Discountwise_Saleslist.this, EmailSetup_Recipients.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
//                                                                getActivity().finish();
                                    dialoge.dismiss();
                                }
                            });

                            Button gotosettings1 = (Button) dialoge.findViewById(R.id.gotosettings1);
                            gotosettings1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Discountwise_Saleslist.this, EmailSetup_Recipients.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
//                                                                getActivity().finish();
                                    dialoge.dismiss();
                                }
                            });


                        }
                    }else {
                        Cursor cursoree = db1.rawQuery("SELECT * FROM Email_recipient", null);
                        if (cursoree.moveToFirst()){
                            //only sender not there
                            final Dialog dialoge = new Dialog(Discountwise_Saleslist.this, R.style.timepicker_date_dialog);
                            dialoge.setContentView(R.layout.email_prerequisites);
                            dialoge.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                            dialoge.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            dialoge.show();

                            ImageButton btncancel = (ImageButton) dialoge.findViewById(R.id.btncancel);
                            btncancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialoge.dismiss();
                                }
                            });

                            ImageView recipient_notset = (ImageView) dialoge.findViewById(R.id.recipient_notset);
                            ImageView recipient_set = (ImageView) dialoge.findViewById(R.id.recipient_set);

                            ImageView sender_notset = (ImageView) dialoge.findViewById(R.id.sender_notset);
                            ImageView sender_set = (ImageView) dialoge.findViewById(R.id.sender_set);

                            sender_notset.setVisibility(View.VISIBLE);

                            recipient_set.setVisibility(View.VISIBLE);

                            Button gotosettings = (Button) dialoge.findViewById(R.id.gotosettings);
                            gotosettings.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Discountwise_Saleslist.this, EmailSetup.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
//                                                                getActivity().finish();
                                    dialoge.dismiss();
                                }
                            });

                            Button gotosettings1 = (Button) dialoge.findViewById(R.id.gotosettings1);
                            gotosettings1.setVisibility(View.GONE);
                            gotosettings1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Discountwise_Saleslist.this, EmailSetup.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
//                                                                getActivity().finish();
                                    dialoge.dismiss();
                                }
                            });

                        }else {
                            //both recipient and sender not there
                            final Dialog dialoge = new Dialog(Discountwise_Saleslist.this, R.style.timepicker_date_dialog);
                            dialoge.setContentView(R.layout.email_prerequisites);
                            dialoge.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                            dialoge.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            dialoge.show();

                            ImageButton btncancel = (ImageButton) dialoge.findViewById(R.id.btncancel);
                            btncancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialoge.dismiss();
                                }
                            });

                            ImageView recipient_notset = (ImageView) dialoge.findViewById(R.id.recipient_notset);
                            ImageView recipient_set = (ImageView) dialoge.findViewById(R.id.recipient_set);

                            ImageView sender_notset = (ImageView) dialoge.findViewById(R.id.sender_notset);
                            ImageView sender_set = (ImageView) dialoge.findViewById(R.id.sender_set);

                            recipient_notset.setVisibility(View.VISIBLE);
                            sender_notset.setVisibility(View.VISIBLE);

                            Button gotosettings = (Button) dialoge.findViewById(R.id.gotosettings);
                            gotosettings.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Discountwise_Saleslist.this, EmailSetup.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
//                                                                getActivity().finish();
                                    dialoge.dismiss();
                                }
                            });

                            Button gotosettings1 = (Button) dialoge.findViewById(R.id.gotosettings1);
                            gotosettings1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Discountwise_Saleslist.this, EmailSetup_Recipients.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
//                                                                getActivity().finish();
                                    dialoge.dismiss();
                                }
                            });

                        }
                    }
                }
            }
        });

    }


    private class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(Discountwise_Saleslist.this, R.style.timepicker_date_dialog);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage(getString(R.string.setmessage13));
            this.dialog.show();

        }
        protected Boolean doInBackground(final String... args){

            File dbFile = getDatabasePath("mydb_Salesdata");
            //Log.v(TAG, "Db path is: "+dbFile);  //get the path of db

//            File exportDir = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_reports/IVEPOS_discount_list");
            File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_reports/IVEPOS_discount_list");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            file = new File(exportDir, "IvePOS_discount_list"+currentDateandTimee1+"_"+timee1+".csv");
            try {

                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

                //ormlite core method
//                List<Person> listdata=dbhelper.GetDataPerson();
//                Person person=null;

                // this is the Column of the table and same for Header of CSV file
                String arrStr1[] ={"Date", "Time", "Bill No.", "Discount code", "Discount(%)", "Discount("+insert1_rs+")", "Net sales"};
                csvWrite.writeNext(arrStr1);


                Cursor curCSV = db.rawQuery("SELECT * FROM Discountdetails",null);
                //csvWrite.writeNext(curCSV.getColumnNames());

                while(curCSV.moveToNext())  {
                    String arrStr[] ={curCSV.getString(8), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4),
                            curCSV.getString(5), curCSV.getString(7), curCSV.getString(6)};
//	                curCSV.getString(2),curCSV.getString(3),curCSV.getString(4),
                    csvWrite.writeNext(arrStr);

                }

                csvWrite.close();
                return true;

            }
            catch (IOException e){
                Log.e("MainActivity", e.getMessage(), e);
                return false;

            }
        }

        @Override
        protected void onPostExecute(final Boolean success)	{

            if (this.dialog.isShowing()){
                this.dialog.dismiss();
            }
            if (success){
                Toast.makeText(Discountwise_Saleslist.this, getString(R.string.export_successful), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(Discountwise_Saleslist.this, getString(R.string.export_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public Cursor fetchCountriesByName(String inputtext) throws SQLException {

        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
            mCursor = db.rawQuery("SELECT * FROM Discountdetails WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "'", null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Discountdetails WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' AND (billno LIKE '%" + inputtext + "%' OR Discountcodeno LIKE '%" + inputtext + "%')", null);
//            mCursor = db.query(true, "Discountdetails", new String[] {"_id", "date", "time", "billno", "Discountcodeno", "Discount_percent", "Billamount_rupess", "Discount_rupees", "date1", "original_amount"},
//                    "billno" +" , "+ "Discountcodeno" + " like" + " '%" + inputtext + "%'", null,
//                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    private android.app.TimePickerDialog.OnTimeSetListener timePickerListener_open = new android.app.TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime_open(hour, minute);

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minutes);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);


            String hour1 = "";
            if (hour < 10)
                hour1 = "0" + hour;
            else
                hour1 = String.valueOf(hour);

            String minutes1 = "";
            if (minute < 10)
                minutes1 = "0" + minute;
            else
                minutes1 = String.valueOf(minute);

            editText_from_day_hide.setText(hour1 + "" + minutes1);


        }
    };

    private android.app.TimePickerDialog.OnTimeSetListener timePickerListener_close = new android.app.TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime_close(hour, minute);

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minutes);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            String hour1 = "";
            if (hour < 10)
                hour1 = "0" + hour;
            else
                hour1 = String.valueOf(hour);

            String minutes1 = "";
            if (minute < 10)
                minutes1 = "0" + minute;
            else
                minutes1 = String.valueOf(minute);

            editText_to_day_hide.setText(hour1 + "" + minutes1);
        }
    };

    private void updateTime_open(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        editText_from_day_visible.setText(aTime);
    }

    private void updateTime_close(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        editText_to_day_visible.setText(aTime);
    }


    private class MakeRequestTask extends AsyncTask<Void, Void, String> {
        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;
//        private View view = sendFabButton;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.gmail.Gmail.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName(getResources().getString(R.string.app_name))
                    .build();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private String getDataFromApi() throws IOException {
            // getting Values for to Address, from Address, Subject and Body

            String strcompanyname = "", straddress1 = "";
            Cursor getcom = db1.rawQuery("SELECT * FROM Companydetailss", null);
            if (getcom.moveToFirst()) {
                do {
                    strcompanyname = getcom.getString(1);
                    straddress1 = getcom.getString(14);
                } while (getcom.moveToNext());
            }

            String url = "www.intuitionsoftwares.com";

//            String msg = "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
//                    "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
//                    "Powered by: " + Uri.parse(url);


            String totalgg = "", ruppercent1 = "";
            Cursor cursor11 = db.rawQuery("SELECT SUM(total) FROM Billnumber", null);
            if (cursor11.moveToFirst()) {
                int levelgg = cursor11.getInt(0);
                totalgg = String.valueOf(levelgg);
            }

            if (Integer.parseInt(totalgg) == 0 || totalgg.toString().length() < 0) {

            } else {
                float perc = Float.parseFloat(total) * 100 / Float.parseFloat(totalgg);
                ruppercent1 = String.format("%.1f", perc);
            }

            String msg = "Discount list (list attached)\n\n" +
                    "Total sales: "+insert1_rs+" " + totalgg + "\n\n" +
                    "Discounts: " + discounttotal.getText().toString() + "(" + ruppercent1 + "%)\n\n" +
                    "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
                    "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
                    "Powered by: " + Uri.parse(url);

            Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
            if (cursor1.moveToFirst()) {
                do {
                    String unn = cursor1.getString(3);
                    TextView edtToAddress = new TextView(Discountwise_Saleslist.this);
                    edtToAddress.setText(unn);

                    TextView edtSubject = new TextView(Discountwise_Saleslist.this);
                    edtSubject.setText(strcompanyname);

                    TextView edtMessage = new TextView(Discountwise_Saleslist.this);
                    edtMessage.setText(msg);

                    String user = "me";
                    String to = Utils.getString(edtToAddress);
                    String from = mCredential.getSelectedAccountName();
                    Log.v("sender email", from);
                    String subject = Utils.getString(edtSubject);
                    String body = Utils.getString(edtMessage);
                    MimeMessage mimeMessage;
                    response = "";
                    try {

//                        File exportDir = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_reports/IVEPOS_customer_list");
//                        if (!exportDir.exists()) {
//                            exportDir.mkdirs();
//                        }
//
//                        file = new File(exportDir, "IvePOS_customer_list" + currentDateandTimee1 + "_" + timee1 + ".csv");

//                        File exportDir1 = new File(Environment.getExternalStorageDirectory(), "/Download");
//                        String filename = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_reports/IVEPOS_discount_list/IvePOS_discount_list"+currentDateandTimee1+"_"+timee1+".csv";
                        String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/IVEPOS_reports/IVEPOS_discount_list/IvePOS_discount_list"+currentDateandTimee1+"_"+timee1+".csv";
//                        String filename = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_reports/IVEPOS_product_report/IvePOS_product_report"+currentDateandTimee1+"_"+timee1+".csv";
//                        String filename = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_reports/IVEPOS_customer_list/IvePOS_customer_list"+currentDateandTimee1+"_"+timee1+".csv";

//                String filename = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_reports/IVEPOS_sales_report/IvePOS_sales_report"+"12May17"+"_"+"013048PM"+".csv";
//                String path = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_backup/";


                        File f = new File(filename);
//
                        mimeMessage = createEmailWithAttachment(to, from, subject, body, f);



//                        mimeMessage = createEmail(to, from, subject, body);
                        response = sendMessage(mService, user, mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }

                } while (cursor1.moveToNext());
            }
            return response;
        }

        // Method to send email
        private String sendMessage(Gmail service,
                                   String userId,
                                   MimeMessage email)
                throws MessagingException, IOException {
            com.google.api.services.gmail.model.Message message = createMessageWithEmail(email);
            // GMail's official method to send email with oauth2.0
            message = service.users().messages().send(userId, message).execute();

            System.out.println("user ID " + userId);

            System.out.println("Message id: " + message.getId());
            System.out.println(message.toPrettyString());
            return message.getId();
        }

        public MimeMessage createEmailWithAttachment(String to,
                                                     String from,
                                                     String subject,
                                                     String bodyText,
                                                     File file)
                throws MessagingException, IOException {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            MimeMessage email = new MimeMessage(session);

            email.setFrom(new InternetAddress(from));
            email.addRecipient(javax.mail.Message.RecipientType.TO,
                    new InternetAddress(to));
            email.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(bodyText, "text/plain");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            mimeBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(file);

            mimeBodyPart.setDataHandler(new DataHandler(source));
            mimeBodyPart.setFileName(file.getName());

            multipart.addBodyPart(mimeBodyPart);
            email.setContent(multipart);

            return email;
        }

        // Method to create email Params
        private MimeMessage createEmail(String to,
                                        String from,
                                        String subject,
                                        String bodyText) throws MessagingException {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            MimeMessage email = new MimeMessage(session);
            InternetAddress tAddress = new InternetAddress(to);
            InternetAddress fAddress = new InternetAddress(from);


            System.out.println("From  " + from);
            System.out.println("To  " + to);


            email.setFrom(fAddress);
            email.addRecipient(javax.mail.Message.RecipientType.TO, tAddress);
            email.setSubject(subject);
            email.setText(bodyText);
            return email;
        }

        private com.google.api.services.gmail.model.Message createMessageWithEmail(MimeMessage email)
                throws MessagingException, IOException {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            email.writeTo(bytes);
            String encodedEmail = Base64.encodeBase64URLSafeString(bytes.toByteArray());
            com.google.api.services.gmail.model.Message message = new com.google.api.services.gmail.model.Message();
            message.setRaw(encodedEmail);
            return message;
        }

        @Override
        protected void onPreExecute() {
            mProgress.show();
        }

        @Override
        protected void onPostExecute(String output) {
            Log.d("post execute", "error");
            mProgress.hide();
            if (output == null || output.length() == 0) {
                Toast.makeText(Discountwise_Saleslist.this, "not success", Toast.LENGTH_SHORT).show();
//                showMessage(view, "No results returned.");
            } else {
                Toast.makeText(Discountwise_Saleslist.this, "success", Toast.LENGTH_SHORT).show();
//                showMessage(view, output);
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
//                Log.v("Errors3", mLastError.getMessage());
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
//                    Log.v("Errors1", mLastError.getMessage());
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
//                    Log.v("Errors2", mLastError.getMessage());
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            Utils.REQUEST_AUTHORIZATION);
                } else {
//                    showMessage(view, "The following error occurred:\n" + mLastError.getMessage());
//                    Log.v("Errors", mLastError.getMessage());
                }
            } else {
//                showMessage(view, "Request Cancelled.");
            }
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                Discountwise_Saleslist.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private void getResultsFromApi() {

        Cursor cursorr = db1.rawQuery("SELECT * FROM Email_setup", null);
        if (cursorr.moveToFirst()) {
            String unn = cursorr.getString(1);
//            Toast.makeText(getActivity(), "a4 " + unn, Toast.LENGTH_SHORT).show();

            TextView tvv = new TextView(Discountwise_Saleslist.this);
            tvv.setText(unn);

            if (tvv.getText().toString().equals("")) {

            }else {
                mCredential.setSelectedAccountName(tvv.getText().toString());
            }
        }

        if (! isGooglePlayServicesAvailable()) {
//            Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
//            Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
//            chooseAccount();
        } else if (! isDeviceOnline()) {
//            Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();
//            mOutputText.setText("No network connection available.");
        } else {
//            Toast.makeText(getActivity(), "4", Toast.LENGTH_SHORT).show();
            new MakeRequestTask1(mCredential).execute();
        }
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(Discountwise_Saleslist.this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(Discountwise_Saleslist.this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
//        Toast.makeText(getActivity(), "s1", Toast.LENGTH_SHORT).show();
        if (EasyPermissions.hasPermissions(
                Discountwise_Saleslist.this, android.Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
//            Toast.makeText(getActivity(), "s2", Toast.LENGTH_SHORT).show();
//            if (accountName != null) {
//                mCredential.setSelectedAccountName(accountName);
//                Toast.makeText(getActivity(), "s3", Toast.LENGTH_SHORT).show();
//                getResultsFromApi();
//            } else {
            // Start a dialog from which the user can choose an account
            startActivityForResult(
                    mCredential.newChooseAccountIntent(),
                    REQUEST_ACCOUNT_PICKER);
//            Toast.makeText(getActivity(), "s4", Toast.LENGTH_SHORT).show();
//            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
//            Toast.makeText(getActivity(), "s5", Toast.LENGTH_SHORT).show();
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    android.Manifest.permission.GET_ACCOUNTS);
        }
    }

    private class MakeRequestTask1 extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;

        MakeRequestTask1(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            System.out.println("labels mservice11 " + mService);

            mService = new com.google.api.services.gmail.Gmail.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Gmail API Android Quickstart")
                    .build();
            Log.d("labels credential", String.valueOf(credential));

            System.out.println("labels mservice " + mService);
        }

        /**
         * Background task to call Gmail API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            Log.d("hiiiiii11", "error");

            try {
                Log.d("hiiiiii111", "error");
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                Log.d("hiiiiii1111", "error");
                return null;
            }
        }

        /**
         * Fetch a list of Gmail labels attached to the specified account.
         * @return List of Strings labels.
         * @throws IOException
         */
        private List<String> getDataFromApi() throws IOException {
            // Get the labels in the user's account.
            String user = "me";
            List<String> labels = new ArrayList<String>();
            ListLabelsResponse listResponse =
                    mService.users().labels().list(user).execute();
            System.out.println("ListLabelsResponse " + listResponse);
            for (Label label : listResponse.getLabels()) {
                labels.add(label.getName());

//                Log.d("labels", String.valueOf(labels));//will be displaying all the folders one by one by looping

//                System.out.println("user ID " + labels.add(label.getName()));
            }
            return labels;
        }


        @Override
        protected void onPreExecute() {
//            mOutputText.setText("");
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
//            System.out.println("labelsss " + output);//will be displaying details and folders in mail like inbox, sent, outbox, junk, etc
            mProgress.hide();
            if (output == null || output.size() == 0) {
//                mOutputText.setText("No results returned.");
            } else {
                output.add(0, "Data retrieved using the Gmail API:");
//                mOutputText.setText(TextUtils.join("\n", output));
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            EmailSetup_Google.REQUEST_AUTHORIZATION);
                } else {
//                    mOutputText.setText("The following error occurred:\n"
//                            + mLastError.getMessage());
                }
            } else {
//                mOutputText.setText("Request cancelled.");
            }
        }
    }


    public void printbillcopy_minireceipt(){
        Cursor connnet = db1.rawQuery("SELECT * FROM IPConn", null);
        if (connnet.moveToFirst()) {
            ipnamegets = connnet.getString(1);
            portgets = connnet.getString(2);
            statusnets = connnet.getString(3);
        }

        Cursor connnet_counter = db1.rawQuery("SELECT * FROM IPConn_Counter", null);
        if (connnet_counter.moveToFirst()) {
            ipnamegets_counter = connnet_counter.getString(1);
            portgets_counter = connnet_counter.getString(2);
            statusnets_counter = connnet_counter.getString(3);
        }
        connnet_counter.close();

        Cursor connusb = db1.rawQuery("SELECT * FROM BTConn", null);
        if (connusb.moveToFirst()) {
            addgets = connusb.getString(1);
            namegets = connusb.getString(2);
            statussusbs = connusb.getString(3);
        }

        //Toast.makeText(Discountwise_Saleslist.this, "printbillonly one ", Toast.LENGTH_SHORT).show();
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

        Cursor print_ty = db1.rawQuery("SELECT * FROM Printer_type", null);
        if (print_ty.moveToFirst()){
            str_print_ty = print_ty.getString(1);
        }

        Cursor cc=db1.rawQuery("SELECT * FROM Printerreceiptsize", null);

        if(cc.moveToFirst()){
            cc.moveToFirst();
            do{
                NAME = cc.getString(1);
                if (NAME.equals("3 inch")) {
                    if (str_print_ty.toString().equals("Generic") || str_print_ty.toString().equals("Epson/others")) {
                        setHT32 = new byte[]{0x1b, 0x44, 0x29, 0x00};//2 tabs 3"
                        setHT321 = new byte[]{0x1b, 0x44, 0x18, 0x00};//2 tabs 3"
                        setHT3212 = new byte[]{0x1b, 0x44, 0x25, 0x00};//2 tabs 3"
                        setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                        setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x20, 0x29, 0x00};//4 tabs 3"
                        nPaperWidth = 576;
                        charlength = 23;
                        charlength1 = 46;
                        charlength2 = 69;
                        quanlentha = 5;
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
                }
                else {
                    if (str_print_ty.toString().equals("Generic")) {
//                        Toast.makeText(Discountwise_Saleslist.this, "phi", Toast.LENGTH_SHORT).show();
                        setHT32 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 2"
                        setHT321 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                        setHT3212 = new byte[]{0x1b, 0x44, 0x15, 0x00};//2 tabs 3"
                        setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                        setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x12, 0x19, 0x00};//4 tabs 2"
                        nPaperWidth = 384;
                        charlength = 10;
                        charlength1 = 20;
                        charlength2 = 30;
                        quanlentha = 5;
                        HT1 = new byte[]{0x09};
                        str_line = "--------------------------------";
                        allbufline = new byte[][]{
                                left, un1, "--------------------------------".getBytes(), LF

                        };
                    }else {
                        if (str_print_ty.toString().equals("Epson/others")) {
//                        Toast.makeText(Discountwise_Saleslist.this, "epson", Toast.LENGTH_SHORT).show();
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
                                nPaperWidth = 384;
                                charlength = 11;
                                charlength1 = 22;
                                charlength2 = 33;
                                quanlentha = 4;
                                HT1 = new byte[]{0x2F};
                                str_line = "--------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "--------------------------------".getBytes(), LF
                                };
                            }
                        }
                    }
                }
            }while(cc.moveToNext());
        }

        String dd = "";
        TextView qazcvb = new TextView(Discountwise_Saleslist.this);
        Cursor cvonnusb = db1.rawQuery("SELECT * FROM BTConn", null);
        if (cvonnusb.moveToFirst()) {
            addgets = cvonnusb.getString(1);
            namegets = cvonnusb.getString(2);
            statussusbs = cvonnusb.getString(3);
            dd = cvonnusb.getString(4);
        }
        qazcvb.setText(dd);
        if (qazcvb.getText().toString().equals("usb") && statussusbs.toString().equals("ok")) {
            runPrintCouponSequence_minireceipt_all();
        }else {
            allbuf1 = new byte[][]{
                    bold, un, "Sales Overview".getBytes(), LF

            };
            if (statussusbs.toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(bold);    //
                BluetoothPrintDriver.BT_Write(un);    //
                BT_Write("Sales Overview");
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(bold);    //
                    wifiSocket2.WIFI_Write(un);    //
                    wifiSocket2.WIFI_Write("Sales Overview");
                    wifiSocket2.WIFI_Write(LF);    //
                }else {
                    if (statusnets.toString().equals("ok")) {
                        wifiSocket.WIFI_Write(bold);    //
                        wifiSocket.WIFI_Write(un);    //
                        wifiSocket.WIFI_Write("Sales Overview");
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
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


            tvkot.setText(strcompanyname);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf1 = new byte[][]{
                        bold, un1, cen, strcompanyname.getBytes(), LF

                };
                if (statussusbs.toString().equals("ok")) {
                    BluetoothPrintDriver.BT_Write(bold);    //
                    BluetoothPrintDriver.BT_Write(un1);    //
                    BluetoothPrintDriver.BT_Write(cen);    //
                    BT_Write(strcompanyname);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.toString().equals("ok")) {
                        wifiSocket2.WIFI_Write(bold);    //
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);    //
                        wifiSocket2.WIFI_Write(strcompanyname);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(bold);    //
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);    //
                            wifiSocket.WIFI_Write(strcompanyname);
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }

/////////
            tvkot.setText(straddress1);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf2 = new byte[][]{
                        normal, un1, cen, straddress1.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.toString().equals("ok")) {
                    BluetoothPrintDriver.BT_Write(normal);    //
                    BluetoothPrintDriver.BT_Write(un1);    //
                    BluetoothPrintDriver.BT_Write(cen);    //
                    BT_Write(straddress1);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.toString().equals("ok")) {
                        wifiSocket2.WIFI_Write(bold);    //
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);    //
                        wifiSocket2.WIFI_Write(straddress1);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(bold);    //
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);    //
                            wifiSocket.WIFI_Write(straddress1);
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }


            tvkot.setText(straddress2);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf3 = new byte[][]{
                        normal, un1, cen, straddress2.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.toString().equals("ok")) {
                    BluetoothPrintDriver.BT_Write(normal);    //
                    BluetoothPrintDriver.BT_Write(un1);    //
                    BluetoothPrintDriver.BT_Write(cen);    //
                    BT_Write(straddress2);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.toString().equals("ok")) {
                        wifiSocket2.WIFI_Write(bold);    //
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);    //
                        wifiSocket2.WIFI_Write(straddress2);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(bold);    //
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);    //
                            wifiSocket.WIFI_Write(straddress2);
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }


            tvkot.setText(straddress3);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf4 = new byte[][]{
                        normal, un1, cen, straddress3.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.toString().equals("ok")) {
                    BluetoothPrintDriver.BT_Write(normal);    //
                    BluetoothPrintDriver.BT_Write(un1);    //
                    BluetoothPrintDriver.BT_Write(cen);    //
                    BT_Write(straddress3);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.toString().equals("ok")) {
                        wifiSocket2.WIFI_Write(bold);    //
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);    //
                        wifiSocket2.WIFI_Write(straddress3);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(bold);    //
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);    //
                            wifiSocket.WIFI_Write(straddress3);
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }

            tvkot.setText(strphone);
            String pp = "Ph. " + strphone;
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf5 = new byte[][]{
                        normal, un1, cen, pp.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.toString().equals("ok")) {
                    BluetoothPrintDriver.BT_Write(normal);    //
                    BluetoothPrintDriver.BT_Write(un1);    //
                    BluetoothPrintDriver.BT_Write(cen);    //
                    BT_Write(pp);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.toString().equals("ok")) {
                        wifiSocket2.WIFI_Write(bold);    //
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);    //
                        wifiSocket2.WIFI_Write(pp);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(bold);    //
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);    //
                            wifiSocket.WIFI_Write(pp);
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }

            tvkot.setText(stremailid);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf6 = new byte[][]{
                        normal, un1, cen, stremailid.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.toString().equals("ok")) {
                    BluetoothPrintDriver.BT_Write(normal);    //
                    BluetoothPrintDriver.BT_Write(un1);    //
                    BluetoothPrintDriver.BT_Write(cen);    //
                    BT_Write(stremailid);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.toString().equals("ok")) {
                        wifiSocket2.WIFI_Write(bold);    //
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);    //
                        wifiSocket2.WIFI_Write(stremailid);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(bold);    //
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);    //
                            wifiSocket.WIFI_Write(stremailid);
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }

            tvkot.setText(strwebsite);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf7 = new byte[][]{
                        normal, un1, cen, strwebsite.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.toString().equals("ok")) {
                    BluetoothPrintDriver.BT_Write(normal);    //
                    BluetoothPrintDriver.BT_Write(un1);    //
                    BluetoothPrintDriver.BT_Write(cen);    //
                    BT_Write(strwebsite);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.toString().equals("ok")) {
                        wifiSocket2.WIFI_Write(bold);    //
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);    //
                        wifiSocket2.WIFI_Write(strwebsite);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(bold);    //
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);    //
                            wifiSocket.WIFI_Write(strwebsite);
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }

            tvkot.setText(strtaxone);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf8 = new byte[][]{
                        normal, un1, cen, strtaxone.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.toString().equals("ok")) {
                    BluetoothPrintDriver.BT_Write(normal);    //
                    BluetoothPrintDriver.BT_Write(un1);    //
                    BluetoothPrintDriver.BT_Write(cen);    //
                    BT_Write(strtaxone);
                    BluetoothPrintDriver.BT_Write(LF);    //
                } else {
                    if (statusnets_counter.toString().equals("ok")) {
                        wifiSocket2.WIFI_Write(bold);    //
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);    //
                        wifiSocket2.WIFI_Write(strtaxone);
                        wifiSocket2.WIFI_Write(LF);    //
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(bold);    //
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);    //
                            wifiSocket.WIFI_Write(strtaxone);
                            wifiSocket.WIFI_Write(LF);    //
                        }
                    }
                }
            }


            if (statussusbs.toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BT_Write(str_line);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(left);	//
                    wifiSocket2.WIFI_Write(un1);	//
                    wifiSocket2.WIFI_Write(str_line);
                    wifiSocket2.WIFI_Write(LF);	//
                }else {
                    if (statusnets.toString().equals("ok")) {
                        wifiSocket.WIFI_Write(left);	//
                        wifiSocket.WIFI_Write(un1);	//
                        wifiSocket.WIFI_Write(str_line);
                        wifiSocket.WIFI_Write(LF);	//
                    }
                }
            }


            allbuf10 = new byte[][]{
                    setHT321, left, editText11.getText().toString().getBytes(), HT, "  ".getBytes(), editText22.getText().toString().getBytes(), LF
                    //setHT321,left,paymmethodaa.getBytes(),HT,datee.getBytes(),LF

            };

            if (statussusbs.toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHT321);    //
                BluetoothPrintDriver.BT_Write(left);    //
                BT_Write(editText11.getText().toString());
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write("  ");
                BT_Write(editText22.getText().toString());
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(setHT321);    //
                    wifiSocket2.WIFI_Write(left);    //
                    wifiSocket2.WIFI_Write(editText11.getText().toString());
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write("  ");
                    wifiSocket2.WIFI_Write(editText22.getText().toString());
                    wifiSocket2.WIFI_Write(LF);    //
                }else {
                    if (statusnets.toString().equals("ok")) {
                        wifiSocket.WIFI_Write(setHT321);    //
                        wifiSocket.WIFI_Write(left);    //
                        wifiSocket.WIFI_Write(editText11.getText().toString());
                        wifiSocket.WIFI_Write(HT);    //
                        wifiSocket.WIFI_Write("  ");
                        wifiSocket.WIFI_Write(editText22.getText().toString());
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }

            allbuftax = new byte[][]{
                    setHT321, left, editText_from_day_visible.getText().toString().getBytes(), HT, "  ".getBytes(), editText_to_day_visible.getText().toString().getBytes(), LF
            };

            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

            if (statussusbs.toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHT321);    //
                BluetoothPrintDriver.BT_Write(left);    //
                BT_Write(editText_from_day_visible.getText().toString());
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write("  ");
                BT_Write(editText_to_day_visible.getText().toString());
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(setHT321);    //
                    wifiSocket2.WIFI_Write(left);    //
                    wifiSocket2.WIFI_Write(editText_from_day_visible.getText().toString());
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write("  ");
                    wifiSocket2.WIFI_Write(editText_to_day_visible.getText().toString());
                    wifiSocket2.WIFI_Write(LF);    //
                }else {
                    if (statusnets.toString().equals("ok")) {
                        wifiSocket.WIFI_Write(setHT321);    //
                        wifiSocket.WIFI_Write(left);    //
                        wifiSocket.WIFI_Write(editText_from_day_visible.getText().toString());
                        wifiSocket.WIFI_Write(HT);    //
                        wifiSocket.WIFI_Write("  ");
                        wifiSocket.WIFI_Write(editText_to_day_visible.getText().toString());
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }


            if (statussusbs.toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BT_Write(str_line);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(left);	//
                    wifiSocket2.WIFI_Write(un1);	//
                    wifiSocket2.WIFI_Write(str_line);
                    wifiSocket2.WIFI_Write(LF);	//
                }else {
                    if (statusnets.toString().equals("ok")) {
                        wifiSocket.WIFI_Write(left);	//
                        wifiSocket.WIFI_Write(un1);	//
                        wifiSocket.WIFI_Write(str_line);
                        wifiSocket.WIFI_Write(LF);	//
                    }
                }
            }


            allbuftax = new byte[][]{
                    setHT321, left, "Sales".getBytes(), HT, "| ".getBytes(), "No. of bills".getBytes(), LF
            };

            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

            if (statussusbs.toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHT321);    //
                BluetoothPrintDriver.BT_Write(left);    //
                BT_Write("Sales");
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write("| ");
                BT_Write("No. of bills");
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(setHT321);    //
                    wifiSocket2.WIFI_Write(left);    //
                    wifiSocket2.WIFI_Write("Sales");
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write("| ");
                    wifiSocket2.WIFI_Write("No. of bills");
                    wifiSocket2.WIFI_Write(LF);    //
                }else {
                    if (statusnets.toString().equals("ok")) {
                        wifiSocket.WIFI_Write(setHT321);    //
                        wifiSocket.WIFI_Write(left);    //
                        wifiSocket.WIFI_Write("Sales");
                        wifiSocket.WIFI_Write(HT);    //
                        wifiSocket.WIFI_Write("| ");
                        wifiSocket.WIFI_Write("No. of bills");
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }

            Cursor cursor11 = db.rawQuery("SELECT SUM(total) FROM Billnumber WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' ", null);
            if (cursor11.moveToFirst()) {
                int level = cursor11.getInt(0);
                total = String.valueOf(level);
            }

            String total1e = "";
            Cursor count = db.rawQuery("SELECT COUNT(total) FROM Billnumber WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' ", null);
            if (count.moveToFirst()) {
                int level = count.getInt(0);
                total1e = String.valueOf(level);
            }

            String avgbill = String.valueOf(Integer.parseInt(total) / Integer.parseInt(total1e));


            noofbills = new TextView(Discountwise_Saleslist.this);


            Cursor countc = db.rawQuery("SELECT COUNT(total) FROM Billnumber WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' ", null);
            if (countc.moveToFirst()) {
                int level = countc.getInt(0);
                String total1 = String.valueOf(level);
                noofbills.setText(total1);
            }


            allbuftax = new byte[][]{
                    setHT321, left, "Rs.".getBytes(), total.toString().getBytes(), HT, "| ".getBytes(), noofbills.getText().toString().getBytes(), LF
            };

            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

            if (statussusbs.toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHT321);    //
                BluetoothPrintDriver.BT_Write(left);    //
                BT_Write(insert1_rs);
                BT_Write(total);
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write("| ");
                BT_Write(noofbills.getText().toString());
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(setHT321);    //
                    wifiSocket2.WIFI_Write(left);    //
                    wifiSocket2.WIFI_Write(insert1_rs);
                    wifiSocket2.WIFI_Write(total);
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write("| ");
                    wifiSocket2.WIFI_Write(noofbills.getText().toString());
                    wifiSocket2.WIFI_Write(LF);    //
                }else {
                    if (statusnets.toString().equals("ok")) {
                        wifiSocket.WIFI_Write(setHT321);    //
                        wifiSocket.WIFI_Write(left);    //
                        wifiSocket.WIFI_Write(insert1_rs);
                        wifiSocket.WIFI_Write(total);
                        wifiSocket.WIFI_Write(HT);    //
                        wifiSocket.WIFI_Write("| ");
                        wifiSocket.WIFI_Write(noofbills.getText().toString());
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }

            if (statussusbs.toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BT_Write(str_line);
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(left);	//
                    wifiSocket2.WIFI_Write(un1);	//
                    wifiSocket2.WIFI_Write(str_line);
                    wifiSocket2.WIFI_Write(LF);	//
                }else {
                    if (statusnets.toString().equals("ok")) {
                        wifiSocket.WIFI_Write(left);	//
                        wifiSocket.WIFI_Write(un1);	//
                        wifiSocket.WIFI_Write(str_line);
                        wifiSocket.WIFI_Write(LF);	//
                    }
                }
            }


            Cursor cursor = db.rawQuery("SELECT * FROM Discountdetails WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "'", null);
            if (cursor.moveToFirst()) {
                do {
                    String bill_no = cursor.getString(3);
                    String date = cursor.getString(8);
                    String time = cursor.getString(2);
                    String am = cursor.getString(9);
                    String code = cursor.getString(4);
                    String disc_am = cursor.getString(7);
                    String disc_pe = cursor.getString(5);

                    Cursor cursor2 = db.rawQuery("SELECt * FROM Billnumber WHERE billnumber = '" + bill_no + "'", null);
                    if (cursor2.moveToFirst()) {
                        String billtypea = cursor2.getString(5);
                        String paymmethoda = cursor2.getString(6);

//                        if (billtypea.toString().equals("  Cash")) {
//                            billtypeaa = "Cash";
//                        } else {
//                            billtypeaa = "Card";
//                        }

                        if (billtypea.toString().equals("  Cash")) {
                            billtypeaa = "Cash"; //0
                        }
                        if (billtypea.toString().equals("  Card")) {
                            billtypeaa = "Card"; //0
                        }
                        if (billtypea.toString().equals("  Paytm")) {
                            billtypeaa = "Paytm"; //0
                        }
                        if (billtypea.toString().equals("  Mobikwik")) {
                            billtypeaa = "Mobikwik"; //0
                        }
                        if (billtypea.toString().equals("  Freecharge")) {
                            billtypeaa = "Freecharge"; //0
                        }
                        if (billtypea.toString().equals("  Pay Later")) {
                            billtypeaa = "Pay Later"; //0
                        }
                        if (billtypea.toString().equals("  Cheque")) {
                            billtypeaa = "Cheque"; //0
                        }
                        if (billtypea.toString().equals("  Sodexo")) {
                            billtypeaa = "Sodexo"; //0
                        }
                        if (billtypea.toString().equals("  Zeta")) {
                            billtypeaa = "Zeta"; //0
                        }
                        if (billtypea.toString().equals("  Ticket")) {
                            billtypeaa = "Ticket"; //0
                        }
                        billtypeaa = billtypea.toString().replace(" ", "");


                        if (paymmethoda.toString().equals("  Dine-in") || paymmethoda.toString().equals("  General") || paymmethoda.toString().equals("  Others")) {
//                            paymmethodaa = "Dine-in";
                            //billtypee.setText("Dine-in");
                            if (account_selection.toString().equals("Dine") || account_selection.toString().equals("Qsr")) {
                                paymmethodaa = "Dine-in";
                            }else {
                                paymmethodaa = "General";
                            }
                        } else {
                            if (paymmethoda.toString().equals("  Takeaway") || paymmethoda.toString().equals("  Main")) {
                                paymmethodaa = "Takeaway";
                                //billtypee.setText("Takeaway");
                            } else {
                                paymmethodaa = "Home delivery";
                                //billtypee.setText("Home delivery");
                            }
                        }


                        Cursor billtype = db.rawQuery("Select * from Billnumber WHERE billnumber = '" + bill_no + "' ", null);
                        if (billtype.moveToFirst()) {
                            String bill_coun = billtype.getString(11);

                            if (statussusbs.equals("ok")) {
                                BluetoothPrintDriver.BT_Write(normal);    //
                                BluetoothPrintDriver.BT_Write(un1);    //
                                BT_Write("Bill id."+bill_coun);
                                BluetoothPrintDriver.BT_Write(LF);    //
                            } else {
                                if (statusnets_counter.equals("ok")) {
                                    wifiSocket2.WIFI_Write(normal);    //
                                    wifiSocket2.WIFI_Write(un1);    //
                                    wifiSocket2.WIFI_Write("Bill id."+bill_coun);
                                    wifiSocket2.WIFI_Write(LF);    //
                                }else {
                                    if (statusnets.equals("ok")) {
                                        wifiSocket.WIFI_Write(normal);    //
                                        wifiSocket.WIFI_Write(un1);    //
                                        wifiSocket.WIFI_Write("Bill id."+bill_coun);
                                        wifiSocket.WIFI_Write(LF);    //
                                    }
                                }
                            }
                        }
                        billtype.close();


                        allbufbillno = new byte[][]{
                                setHT321, un1, "Bill no.".getBytes(), bill_no.getBytes(), HT, "   ".getBytes(), billtypeaa.getBytes(), LF
//						left, normal, setHT22, "DECAF16".getBytes(), HT, right, "30".getBytes(), LF,
//						left, normal, setHT22, "BREVE".getBytes(), HT, right, "1000".getBytes(), LF,
                        };


                        if (statussusbs.toString().equals("ok")) {
                            BluetoothPrintDriver.BT_Write(setHT321);    //
                            BluetoothPrintDriver.BT_Write(un1);    //
                            BT_Write("Bill no." + bill_no);
                            BluetoothPrintDriver.BT_Write(HT);    //
                            BT_Write("   ");
                            BT_Write(billtypeaa);
                            BluetoothPrintDriver.BT_Write(LF);    //
                        } else {
                            if (statusnets_counter.toString().equals("ok")) {
                                wifiSocket2.WIFI_Write(setHT321);    //
                                wifiSocket2.WIFI_Write(un1);    //
                                wifiSocket2.WIFI_Write("Bill no." + bill_no);
                                wifiSocket2.WIFI_Write(HT);    //
                                wifiSocket2.WIFI_Write("   ");
                                wifiSocket2.WIFI_Write(billtypeaa);
                                wifiSocket2.WIFI_Write(LF);    //
                            }else {
                                if (statusnets.toString().equals("ok")) {
                                    wifiSocket.WIFI_Write(setHT321);    //
                                    wifiSocket.WIFI_Write(un1);    //
                                    wifiSocket.WIFI_Write("Bill no." + bill_no);
                                    wifiSocket.WIFI_Write(HT);    //
                                    wifiSocket.WIFI_Write("   ");
                                    wifiSocket.WIFI_Write(billtypeaa);
                                    wifiSocket.WIFI_Write(LF);    //
                                }
                            }
                        }

                        allbuf10 = new byte[][]{
                                setHT321, left, paymmethodaa.getBytes(), HT, "  ".getBytes(), date.getBytes(), LF
                                //setHT321,left,paymmethodaa.getBytes(),HT,datee.getBytes(),LF

                        };
                        //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                        if (statussusbs.toString().equals("ok")) {
                            BluetoothPrintDriver.BT_Write(setHT321);    //
                            BluetoothPrintDriver.BT_Write(left);    //
                            BT_Write(paymmethodaa);
                            BluetoothPrintDriver.BT_Write(HT);    //
                            BT_Write("  ");
                            BT_Write(date);
                            BluetoothPrintDriver.BT_Write(LF);    //
                        } else {
                            if (statusnets_counter.toString().equals("ok")) {
                                wifiSocket2.WIFI_Write(setHT321);    //
                                wifiSocket2.WIFI_Write(left);    //
                                wifiSocket2.WIFI_Write(paymmethodaa);
                                wifiSocket2.WIFI_Write(HT);    //
                                wifiSocket2.WIFI_Write("  ");
                                wifiSocket2.WIFI_Write(date);
                                wifiSocket2.WIFI_Write(LF);    //
                            }else {
                                if (statusnets.toString().equals("ok")) {
                                    wifiSocket.WIFI_Write(setHT321);    //
                                    wifiSocket.WIFI_Write(left);    //
                                    wifiSocket.WIFI_Write(paymmethodaa);
                                    wifiSocket.WIFI_Write(HT);    //
                                    wifiSocket.WIFI_Write("  ");
                                    wifiSocket.WIFI_Write(date);
                                    wifiSocket.WIFI_Write(LF);    //
                                }
                            }
                        }

                        allbuftime = new byte[][]{
                                setHT321, left, "Rs.".getBytes(), am.getBytes(), HT, "  ".getBytes(), time.getBytes(), LF
                        };


                        if (statussusbs.toString().equals("ok")) {
                            BluetoothPrintDriver.BT_Write(setHT321);    //
                            BluetoothPrintDriver.BT_Write(left);    //
                            BT_Write(insert1_rs);
                            BT_Write(am);
                            BluetoothPrintDriver.BT_Write(HT);    //
                            BT_Write("  ");
                            BT_Write(time);
                            BluetoothPrintDriver.BT_Write(LF);    //
                        } else {
                            if (statusnets_counter.toString().equals("ok")) {
                                wifiSocket2.WIFI_Write(setHT321);    //
                                wifiSocket2.WIFI_Write(left);    //
                                wifiSocket2.WIFI_Write(insert1_rs);
                                wifiSocket2.WIFI_Write(am);
                                wifiSocket2.WIFI_Write(HT);    //
                                wifiSocket2.WIFI_Write("  ");
                                wifiSocket2.WIFI_Write(time);
                                wifiSocket2.WIFI_Write(LF);    //
                            }else {
                                if (statusnets.toString().equals("ok")) {
                                    wifiSocket.WIFI_Write(setHT321);    //
                                    wifiSocket.WIFI_Write(left);    //
                                    wifiSocket.WIFI_Write(insert1_rs);
                                    wifiSocket.WIFI_Write(am);
                                    wifiSocket.WIFI_Write(HT);    //
                                    wifiSocket.WIFI_Write("  ");
                                    wifiSocket.WIFI_Write(time);
                                    wifiSocket.WIFI_Write(LF);    //
                                }
                            }
                        }


                    }

                    allbuftaxestype1 = new byte[][]{
                            left, normal, "Discount amount(".getBytes(), disc_pe.getBytes(), "%".getBytes(), "): Rs.".getBytes(), disc_am.getBytes(), HT, LF
                    };

                    if (statussusbs.toString().equals("ok")) {
                        BluetoothPrintDriver.BT_Write(left);    //
                        BluetoothPrintDriver.BT_Write(normal);    //
                        BT_Write("Discount amount(" + disc_pe + "%" + "): "+insert1_rs + disc_am);
                        BluetoothPrintDriver.BT_Write(HT);    //
                        BluetoothPrintDriver.BT_Write(LF);    //
                    } else {
                        if (statusnets_counter.toString().equals("ok")) {
                            wifiSocket2.WIFI_Write(left);    //
                            wifiSocket2.WIFI_Write(normal);    //
                            wifiSocket2.WIFI_Write("Discount amount(" + disc_pe + "%" + "): "+insert1_rs + disc_am);
                            wifiSocket2.WIFI_Write(HT);    //
                            wifiSocket2.WIFI_Write(LF);    //
                        }else {
                            if (statusnets.toString().equals("ok")) {
                                wifiSocket.WIFI_Write(left);    //
                                wifiSocket.WIFI_Write(normal);    //
                                wifiSocket.WIFI_Write("Discount amount(" + disc_pe + "%" + "): "+insert1_rs + disc_am);
                                wifiSocket.WIFI_Write(HT);    //
                                wifiSocket.WIFI_Write(LF);    //
                            }
                        }
                    }

                    allbuftaxestype1 = new byte[][]{
                            left, normal, "Discount code: ".getBytes(), code.getBytes(), HT, LF
                    };

                    if (statussusbs.toString().equals("ok")) {
                        BluetoothPrintDriver.BT_Write(left);    //
                        BluetoothPrintDriver.BT_Write(normal);    //
                        BT_Write("Discount code: ");
                        BT_Write(code);
                        BluetoothPrintDriver.BT_Write(HT);    //
                        BluetoothPrintDriver.BT_Write(LF);    //
                    } else {
                        if (statusnets_counter.toString().equals("ok")) {
                            wifiSocket2.WIFI_Write(left);    //
                            wifiSocket2.WIFI_Write(normal);    //
                            wifiSocket2.WIFI_Write("Discount code: ");
                            wifiSocket2.WIFI_Write(code);
                            wifiSocket2.WIFI_Write(HT);    //
                            wifiSocket2.WIFI_Write(LF);    //
                        }else {
                            if (statusnets.toString().equals("ok")) {
                                wifiSocket.WIFI_Write(left);    //
                                wifiSocket.WIFI_Write(normal);    //
                                wifiSocket.WIFI_Write("Discount code: ");
                                wifiSocket.WIFI_Write(code);
                                wifiSocket.WIFI_Write(HT);    //
                                wifiSocket.WIFI_Write(LF);    //
                            }
                        }
                    }


                    if (statussusbs.toString().equals("ok")) {
                        BluetoothPrintDriver.BT_Write(left);    //
                        BluetoothPrintDriver.BT_Write(un1);    //
                        BT_Write(str_line);
                        BluetoothPrintDriver.BT_Write(LF);    //
                    } else {
                        if (statusnets_counter.toString().equals("ok")) {
                            wifiSocket2.WIFI_Write(left);	//
                            wifiSocket2.WIFI_Write(un1);	//
                            wifiSocket2.WIFI_Write(str_line);
                            wifiSocket2.WIFI_Write(LF);	//
                        }else {
                            if (statusnets.toString().equals("ok")) {
                                wifiSocket.WIFI_Write(left);	//
                                wifiSocket.WIFI_Write(un1);	//
                                wifiSocket.WIFI_Write(str_line);
                                wifiSocket.WIFI_Write(LF);	//
                            }
                        }
                    }


                } while (cursor.moveToNext());
            }

            feedcut();
        }
    }

    public void feedcut(){
        Cursor cc=db1.rawQuery("SELECT * FROM Printerreceiptsize", null);

        if(cc.moveToFirst()){
            cc.moveToFirst();
            do{
                NAME = cc.getString(1);
                if (NAME.equals("3 inch")) {
                    feedcut2 = new byte[]{0x1b,0x64,0x05, 0x1d,0x56,0x00};
                }
                else {
                    feedcut2 = new byte[]{0x1b,0x64,0x03, 0x1d,0x56,0x00};
                }
            }while(cc.moveToNext());
        }

        byte[][] allbuf = new byte[][]{
                feedcut2
        };
        if (statussusbs.toString().equals("ok")) {
            BluetoothPrintDriver.BT_Write(feedcut2);	//
        }else {
            if (statusnets_counter.toString().equals("ok")) {
                wifiSocket2.WIFI_Write(feedcut2);	//
            }else {
                if (statusnets.toString().equals("ok")) {
                    wifiSocket.WIFI_Write(feedcut2);	//
                }
            }
        }
        if (str_print_ty.equals("POS")) {
            if (statussusbs.toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(feedcut2);	//
            }else {
                if (statusnets_counter.toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(feedcut2);	//
                }else {
                    if (statusnets.toString().equals("ok")) {
                        wifiSocket.WIFI_Write(feedcut2);	//
                    }
                }
            }
        }
    }


    private boolean runPrintCouponSequence_minireceipt_all() {
        if (!initializeObject()) {
            return false;
        }

        if (!createCouponData_minireceipt_all()) {
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
//            Toast.makeText(Discountwise_Saleslist.this, "Here3", Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(Discountwise_Saleslist.this, "Here6", Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(Discountwise_Saleslist.this, "Here7", Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(Discountwise_Saleslist.this, "Here9", Toast.LENGTH_SHORT).show();
                // Do nothing
            }
            return false;
        }

        try {
            mPrinter.sendData(com.epson.epos2.printer.Printer.PARAM_DEFAULT);
        } catch (Exception e) {
//            Toast.makeText(Discountwise_Saleslist.this, "Here10", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, "sendData", mContext);
            try {
                mPrinter.disconnect();
            } catch (Exception ex) {
//                Toast.makeText(Discountwise_Saleslist.this, "Here11", Toast.LENGTH_SHORT).show();
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
//            Toast.makeText(Discountwise_Saleslist.this, "Here4", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, "connect", mContext);
            return false;
        }

        try {
            mPrinter.beginTransaction();
            isBeginTransaction = true;
        } catch (Exception e) {
//            Toast.makeText(Discountwise_Saleslist.this, "Here12", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, "beginTransaction", mContext);
        }

        if (isBeginTransaction == false) {
            try {
                mPrinter.disconnect();
            } catch (Epos2Exception e) {
//                Toast.makeText(Discountwise_Saleslist.this, "Here5", Toast.LENGTH_SHORT).show();
                // Do nothing
                return false;
            }
        }

        return true;
    }

    private boolean createCouponData_minireceipt_all() {

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

        Cursor cc=db1.rawQuery("SELECT * FROM Printerreceiptsize", null);

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
                    Cursor print_ty = db1.rawQuery("SELECT * FROM Printer_type", null);
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

        ArrayList<byte[]> list = new ArrayList<byte[]>();
        String method = "";
        String[] col = {"companylogo"};
        Cursor c = db1.query("Logo", col, null, null, null, null, null);
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
            mPrinter.addPageDirection(Printer.DIRECTION_TOP_TO_BOTTOM);

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

            Cursor connnet = db1.rawQuery("SELECT * FROM IPConn", null);
            if (connnet.moveToFirst()) {
                ipnamegets = connnet.getString(1);
                portgets = connnet.getString(2);
                statusnets = connnet.getString(3);
            }

            Cursor connnet_counter = db1.rawQuery("SELECT * FROM IPConn_Counter", null);
            if (connnet_counter.moveToFirst()) {
                ipnamegets_counter = connnet_counter.getString(1);
                portgets_counter = connnet_counter.getString(2);
                statusnets_counter = connnet_counter.getString(3);
            }
            connnet_counter.close();

            Cursor connusb = db1.rawQuery("SELECT * FROM BTConn", null);
            if (connusb.moveToFirst()) {
                addgets = connusb.getString(1);
                namegets = connusb.getString(2);
                statussusbs = connusb.getString(3);
            }

            allbuf1 = new byte[][]{
                    bold, un, "Sales Overview".getBytes(), LF

            };
            if (statussusbs.toString().equals("ok")) {
                mPrinter.addCommand(bold);
                mPrinter.addCommand(un);
                StringBuilder textData1 = new StringBuilder();
                textData1.append("Sales Overview");
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(LF); //LF
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


            tvkot.setText(strcompanyname);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf1 = new byte[][]{
                        bold, un1, cen, strcompanyname.getBytes(), LF

                };
                if (statussusbs.toString().equals("ok")) {
                    mPrinter.addCommand(bold);
                    mPrinter.addCommand(un1);
                    mPrinter.addCommand(cen);
                    StringBuilder textData1 = new StringBuilder();
                    textData1.append(strcompanyname);
                    mPrinter.addText(textData1.toString());
                    mPrinter.addCommand(LF); //LF
                }
            }

/////////
            tvkot.setText(straddress1);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf2 = new byte[][]{
                        normal, un1, cen, straddress1.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.toString().equals("ok")) {
                    mPrinter.addCommand(normal);
                    mPrinter.addCommand(un1);
                    mPrinter.addCommand(cen);
                    StringBuilder textData1 = new StringBuilder();
                    textData1.append(straddress1);
                    mPrinter.addText(textData1.toString());
                    mPrinter.addCommand(LF); //LF
                }
            }


            tvkot.setText(straddress2);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf3 = new byte[][]{
                        normal, un1, cen, straddress2.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.toString().equals("ok")) {
                    mPrinter.addCommand(normal);
                    mPrinter.addCommand(un1);
                    mPrinter.addCommand(cen);
                    StringBuilder textData1 = new StringBuilder();
                    textData1.append(straddress2);
                    mPrinter.addText(textData1.toString());
                    mPrinter.addCommand(LF); //LF
                }
            }


            tvkot.setText(straddress3);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf4 = new byte[][]{
                        normal, un1, cen, straddress3.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.toString().equals("ok")) {
                    mPrinter.addCommand(normal);
                    mPrinter.addCommand(un1);
                    mPrinter.addCommand(cen);
                    StringBuilder textData1 = new StringBuilder();
                    textData1.append(straddress3);
                    mPrinter.addText(textData1.toString());
                    mPrinter.addCommand(LF); //LF
                }
            }

            tvkot.setText(strphone);
            String pp = "Ph. " + strphone;
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf5 = new byte[][]{
                        normal, un1, cen, pp.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.toString().equals("ok")) {
                    mPrinter.addCommand(normal);
                    mPrinter.addCommand(un1);
                    mPrinter.addCommand(cen);
                    StringBuilder textData1 = new StringBuilder();
                    textData1.append(pp);
                    mPrinter.addText(textData1.toString());
                    mPrinter.addCommand(LF); //LF
                }
            }

            tvkot.setText(stremailid);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf6 = new byte[][]{
                        normal, un1, cen, stremailid.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.toString().equals("ok")) {
                    mPrinter.addCommand(normal);
                    mPrinter.addCommand(un1);
                    mPrinter.addCommand(cen);
                    StringBuilder textData1 = new StringBuilder();
                    textData1.append(stremailid);
                    mPrinter.addText(textData1.toString());
                    mPrinter.addCommand(LF); //LF
                }
            }

            tvkot.setText(strwebsite);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf7 = new byte[][]{
                        normal, un1, cen, strwebsite.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.toString().equals("ok")) {
                    mPrinter.addCommand(normal);
                    mPrinter.addCommand(un1);
                    mPrinter.addCommand(cen);
                    StringBuilder textData1 = new StringBuilder();
                    textData1.append(strwebsite);
                    mPrinter.addText(textData1.toString());
                    mPrinter.addCommand(LF); //LF
                }
            }

            tvkot.setText(strtaxone);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf8 = new byte[][]{
                        normal, un1, cen, strtaxone.getBytes(), LF

                };
                //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                if (statussusbs.toString().equals("ok")) {
                    mPrinter.addCommand(normal);
                    mPrinter.addCommand(un1);
                    mPrinter.addCommand(cen);
                    StringBuilder textData1 = new StringBuilder();
                    textData1.append(strtaxone);
                    mPrinter.addText(textData1.toString());
                    mPrinter.addCommand(LF); //LF
                }
            }


            if (statussusbs.toString().equals("ok")) {
                mPrinter.addCommand(left);
                mPrinter.addCommand(un1);
                StringBuilder textData1 = new StringBuilder();
                textData1.append(str_line);
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(LF); //LF
            }


            allbuf10 = new byte[][]{
                    setHT321, left, editText11.getText().toString().getBytes(), HT, "  ".getBytes(), editText22.getText().toString().getBytes(), LF
                    //setHT321,left,paymmethodaa.getBytes(),HT,datee.getBytes(),LF

            };

            if (statussusbs.toString().equals("ok")) {
                mPrinter.addCommand(setHT321);
                mPrinter.addCommand(left);
                StringBuilder textData1 = new StringBuilder();
                textData1.append(editText11.getText().toString());
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(HT);
                StringBuilder textData2 = new StringBuilder();
                textData2.append("  "+editText22.getText().toString());
                mPrinter.addText(textData2.toString());
                mPrinter.addCommand(LF); //LF
            }

            allbuftax = new byte[][]{
                    setHT321, left, editText_from_day_visible.getText().toString().getBytes(), HT, "  ".getBytes(), editText_to_day_visible.getText().toString().getBytes(), LF
            };

            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

            if (statussusbs.toString().equals("ok")) {
                mPrinter.addCommand(setHT321);
                mPrinter.addCommand(left);
                StringBuilder textData1 = new StringBuilder();
                textData1.append(editText_from_day_visible.getText().toString());
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(HT);
                StringBuilder textData2 = new StringBuilder();
                textData2.append("  "+editText_to_day_visible.getText().toString());
                mPrinter.addText(textData2.toString());
                mPrinter.addCommand(LF); //LF
            }


            if (statussusbs.toString().equals("ok")) {
                mPrinter.addCommand(left);
                mPrinter.addCommand(un1);
                StringBuilder textData1 = new StringBuilder();
                textData1.append(str_line);
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(LF); //LF
            }


            allbuftax = new byte[][]{
                    setHT321, left, "Sales".getBytes(), HT, "| ".getBytes(), "No. of bills".getBytes(), LF
            };

            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

            if (statussusbs.toString().equals("ok")) {
                mPrinter.addCommand(setHT321);
                mPrinter.addCommand(left);
                StringBuilder textData1 = new StringBuilder();
                textData1.append("Sales");
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(HT);
                StringBuilder textData2 = new StringBuilder();
                textData2.append("| "+"No. of bills");
                mPrinter.addText(textData2.toString());
                mPrinter.addCommand(LF); //LF
            }

            Cursor cursor11 = db.rawQuery("SELECT SUM(total) FROM Billnumber WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' ", null);
            if (cursor11.moveToFirst()) {
                int level = cursor11.getInt(0);
                total = String.valueOf(level);
            }

            String total1e = "";
            Cursor count = db.rawQuery("SELECT COUNT(total) FROM Billnumber WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' ", null);
            if (count.moveToFirst()) {
                int level = count.getInt(0);
                total1e = String.valueOf(level);
            }

            String avgbill = String.valueOf(Integer.parseInt(total) / Integer.parseInt(total1e));


            noofbills = new TextView(Discountwise_Saleslist.this);


            Cursor countc = db.rawQuery("SELECT COUNT(total) FROM Billnumber WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' ", null);
            if (countc.moveToFirst()) {
                int level = countc.getInt(0);
                String total1 = String.valueOf(level);
                noofbills.setText(total1);
            }


            allbuftax = new byte[][]{
                    setHT321, left, "Rs.".getBytes(), total.toString().getBytes(), HT, "| ".getBytes(), noofbills.getText().toString().getBytes(), LF
            };

            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

            if (statussusbs.toString().equals("ok")) {
                mPrinter.addCommand(setHT321);
                mPrinter.addCommand(left);
                StringBuilder textData1 = new StringBuilder();
                textData1.append(insert1_rs+""+total);
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(HT);
                StringBuilder textData2 = new StringBuilder();
                textData2.append("| "+noofbills.getText().toString());
                mPrinter.addText(textData2.toString());
                mPrinter.addCommand(LF); //LF
            }

            if (statussusbs.toString().equals("ok")) {
                mPrinter.addCommand(left);
                mPrinter.addCommand(un1);
                StringBuilder textData1 = new StringBuilder();
                textData1.append(str_line);
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(LF); //LF
            }


            Cursor cursor = db.rawQuery("SELECT * FROM Discountdetails WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "'", null);
            if (cursor.moveToFirst()) {
                do {
                    String bill_no = cursor.getString(3);
                    String date = cursor.getString(8);
                    String time = cursor.getString(2);
                    String am = cursor.getString(9);
                    String code = cursor.getString(4);
                    String disc_am = cursor.getString(7);
                    String disc_pe = cursor.getString(5);

                    Cursor cursor2 = db.rawQuery("SELECt * FROM Billnumber WHERE billnumber = '" + bill_no + "'", null);
                    if (cursor2.moveToFirst()) {
                        String billtypea = cursor2.getString(5);
                        String paymmethoda = cursor2.getString(6);

//                        if (billtypea.toString().equals("  Cash")) {
//                            billtypeaa = "Cash";
//                        } else {
//                            billtypeaa = "Card";
//                        }

                        if (billtypea.toString().equals("  Cash")) {
                            billtypeaa = "Cash"; //0
                        }
                        if (billtypea.toString().equals("  Card")) {
                            billtypeaa = "Card"; //0
                        }
                        if (billtypea.toString().equals("  Paytm")) {
                            billtypeaa = "Paytm"; //0
                        }
                        if (billtypea.toString().equals("  Mobikwik")) {
                            billtypeaa = "Mobikwik"; //0
                        }
                        if (billtypea.toString().equals("  Freecharge")) {
                            billtypeaa = "Freecharge"; //0
                        }
                        if (billtypea.toString().equals("  Pay Later")) {
                            billtypeaa = "Pay Later"; //0
                        }
                        if (billtypea.toString().equals("  Cheque")) {
                            billtypeaa = "Cheque"; //0
                        }
                        if (billtypea.toString().equals("  Sodexo")) {
                            billtypeaa = "Sodexo"; //0
                        }
                        if (billtypea.toString().equals("  Zeta")) {
                            billtypeaa = "Zeta"; //0
                        }
                        if (billtypea.toString().equals("  Ticket")) {
                            billtypeaa = "Ticket"; //0
                        }
                        billtypeaa = billtypea.toString().replace(" ", "");


                        if (paymmethoda.toString().equals("  Dine-in") || paymmethoda.toString().equals("  General") || paymmethoda.toString().equals("  Others")) {
//                            paymmethodaa = "Dine-in";
                            //billtypee.setText("Dine-in");
                            if (account_selection.toString().equals("Dine") || account_selection.toString().equals("Qsr")) {
                                paymmethodaa = "Dine-in";
                            }else {
                                paymmethodaa = "General";
                            }
                        } else {
                            if (paymmethoda.toString().equals("  Takeaway") || paymmethoda.toString().equals("  Main")) {
                                paymmethodaa = "Takeaway";
                                //billtypee.setText("Takeaway");
                            } else {
                                paymmethodaa = "Home delivery";
                                //billtypee.setText("Home delivery");
                            }
                        }


                        Cursor billtype = db.rawQuery("Select * from Billnumber WHERE billnumber = '" + bill_no + "' ", null);
                        if (billtype.moveToFirst()) {
                            String bill_coun = billtype.getString(11);

                            if (statussusbs.equals("ok")) {
                                mPrinter.addCommand(normal);
                                mPrinter.addCommand(un1);
                                StringBuilder textData1 = new StringBuilder();
                                textData1.append("Bill id."+bill_coun);
                                mPrinter.addText(textData1.toString());
                                mPrinter.addCommand(LF); //LF
                            }
                        }


                        allbufbillno = new byte[][]{
                                setHT321, un1, "Bill no.".getBytes(), bill_no.getBytes(), HT, "   ".getBytes(), billtypeaa.getBytes(), LF
//						left, normal, setHT22, "DECAF16".getBytes(), HT, right, "30".getBytes(), LF,
//						left, normal, setHT22, "BREVE".getBytes(), HT, right, "1000".getBytes(), LF,
                        };


                        if (statussusbs.toString().equals("ok")) {
                            mPrinter.addCommand(setHT321);
                            mPrinter.addCommand(un1);
                            StringBuilder textData1 = new StringBuilder();
                            textData1.append("Bill no."+bill_no);
                            mPrinter.addText(textData1.toString());
                            mPrinter.addCommand(HT);
                            StringBuilder textData2 = new StringBuilder();
                            textData2.append("   "+billtypeaa);
                            mPrinter.addText(textData2.toString());
                            mPrinter.addCommand(LF); //LF
                        }

                        allbuf10 = new byte[][]{
                                setHT321, left, paymmethodaa.getBytes(), HT, "  ".getBytes(), date.getBytes(), LF
                                //setHT321,left,paymmethodaa.getBytes(),HT,datee.getBytes(),LF

                        };
                        //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                        if (statussusbs.toString().equals("ok")) {
                            mPrinter.addCommand(setHT321);
                            mPrinter.addCommand(left);
                            StringBuilder textData1 = new StringBuilder();
                            textData1.append(paymmethodaa);
                            mPrinter.addText(textData1.toString());
                            mPrinter.addCommand(HT);
                            StringBuilder textData2 = new StringBuilder();
                            textData2.append("  "+date);
                            mPrinter.addText(textData2.toString());
                            mPrinter.addCommand(LF); //LF
                        }

                        allbuftime = new byte[][]{
                                setHT321, left, "Rs.".getBytes(), am.getBytes(), HT, "  ".getBytes(), time.getBytes(), LF
                        };


                        if (statussusbs.toString().equals("ok")) {
                            mPrinter.addCommand(setHT321);
                            mPrinter.addCommand(left);
                            StringBuilder textData1 = new StringBuilder();
                            textData1.append(insert1_rs+""+am);
                            mPrinter.addText(textData1.toString());
                            mPrinter.addCommand(HT);
                            StringBuilder textData2 = new StringBuilder();
                            textData2.append("  "+time);
                            mPrinter.addText(textData2.toString());
                            mPrinter.addCommand(LF); //LF
                        }


                    }

                    allbuftaxestype1 = new byte[][]{
                            left, normal, "Discount amount(".getBytes(), disc_pe.getBytes(), "%".getBytes(), "): Rs.".getBytes(), disc_am.getBytes(), HT, LF
                    };

                    if (statussusbs.toString().equals("ok")) {
                        mPrinter.addCommand(left);
                        mPrinter.addCommand(normal);
                        StringBuilder textData1 = new StringBuilder();
                        textData1.append("Discount amount("+disc_pe+"%"+"): "+insert1_rs+disc_am);
                        mPrinter.addText(textData1.toString());
                        mPrinter.addCommand(HT);
                        mPrinter.addCommand(LF); //LF
                    }

                    allbuftaxestype1 = new byte[][]{
                            left, normal, "Discount code: ".getBytes(), code.getBytes(), HT, LF
                    };

                    if (statussusbs.toString().equals("ok")) {
                        mPrinter.addCommand(left);
                        mPrinter.addCommand(normal);
                        StringBuilder textData1 = new StringBuilder();
                        textData1.append("Discount code: "+code);
                        mPrinter.addText(textData1.toString());
                        mPrinter.addCommand(HT);
                        mPrinter.addCommand(LF); //LF
                    }


                    if (statussusbs.toString().equals("ok")) {
                        mPrinter.addCommand(left);
                        mPrinter.addCommand(un1);
                        StringBuilder textData1 = new StringBuilder();
                        textData1.append(str_line);
                        mPrinter.addText(textData1.toString());
                        mPrinter.addCommand(LF); //LF
                    }


                } while (cursor.moveToNext());
            }



            Cursor acc=db1.rawQuery("SELECT * FROM Printerreceiptsize", null);

            if(acc.moveToFirst()){
                acc.moveToFirst();
                do{
                    NAME = acc.getString(1);
                    if (NAME.equals("3 inch")) {
                        feedcut2 = new byte[]{0x1b,0x64,0x05, 0x1d,0x56,0x00};
                    }
                    else {
                        feedcut2 = new byte[]{0x1b,0x64,0x03, 0x1d,0x56,0x00};
                    }
                }while(acc.moveToNext());
            }

            byte[][] allbuf = new byte[][]{
                    feedcut2
            };
            if (statussusbs.toString().equals("ok")) {
                mPrinter.addCommand(feedcut2);
            }


//            method = "addCut";
//            mPrinter.addCut(Printer.CUT_FEED);
        } catch (Exception e) {
//            Toast.makeText(Discountwise_Saleslist.this, "Here2", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, method, mContext);
            return false;
        }

        return true;
    }
}
