package com.intuition.ivepos;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import com.epson.epos2.printer.*;
import com.epson.epos2.printer.Printer;
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

import au.com.bytecode.opencsv.CSVWriter;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.intuition.ivepos.BluetoothPrintDriver.BT_Write;

/**
 * Created by Rohithkumar on 7/13/2017.
 */

public class Itemwise_Saleslist extends AppCompatActivity implements ReceiveListener {


    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;

    int level;
    String total, Itemtype, total1, total1quan, itemtotal, one1, two2, three3, salesee1, salesee2, salesee3, catt;

    File file = null;
    SimpleDateFormat sdff2, sdff1;
    String currentDateandTimee1;
    String timee1;

    String ipnamegets, portgets, statusnets, addgets, namegets, statussusbs;
    String ipnamegets_counter, portgets_counter, statusnets_counter;


    String ipnameget, portget, statusnet, nameget, addget, statussusb;
    String ipnameget_counter, portget_counter, statusnet_counter;


    byte[][] allbuf, allbuf1, allbuf2, allbuf3, allbuf4, allbuf5, allbuf6, allbuf7, allbuf8, allbuf9, allbuf10, allbuf11, allbufqty, allbufitems, allbufmodifiers, allbufsubtot,
            allbuftax, allbufdisc, allbufrounded, allbuffulltot, allbuf12, allbuf13, allbuf14,allbufbillno,allbuftime,allbufline1,allbufline,allbufcust,allbufcustname,
            allbufcustadd,allbufcustph,allbufcustemail, allbuftaxestype2, allbuftaxestype1, allbuf1122, allbufKOT;
    String strcompanyname, straddress1, straddress2, straddress3, strphone, stremailid, strwebsite, strtaxone, strbillone;

    byte[] setHT32, setHT321, setHT33, setHT34, setHT3212, setHTKOT, feedcut2;
    int nPaperWidth;
    int charlength, charlength1, charlength2, quanlentha;

    String NAME;

    String companynameis;

    List toEmailList;

    SimpleCursorAdapter adapter;
    String id;
    Cursor cursor1;

    EditText search;
    ListView listView;
    Spinner getlisting;

    TextView tvkot;
    TextView totalsales, totalsales_r, noofbills, avgsales, avgsales_r;

    TextView i_mi_totalsales1, i_cost, mi_cost, i_cost_percent;

    private int hour;
    private int minute;

    LinearLayout item_list, modifier_list, category_list;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemwise_saleslist);

        mContext = this;

        mSpnSeries = (Spinner) findViewById(R.id.spnModel);
        ArrayAdapter<SpnModelsItem> seriesAdapter = new ArrayAdapter<SpnModelsItem>(Itemwise_Saleslist.this, android.R.layout.simple_spinner_item);
        seriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t82), com.epson.epos2.printer.Printer.TM_T82));
        mSpnSeries.setAdapter(seriesAdapter);
        mSpnSeries.setSelection(0);

        mSpnLang = (Spinner) findViewById(R.id.spnLang);
        ArrayAdapter<SpnModelsItem> langAdapter = new ArrayAdapter<SpnModelsItem>(Itemwise_Saleslist.this, android.R.layout.simple_spinner_item);
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_ank), com.epson.epos2.printer.Printer.MODEL_ANK));
        mSpnLang.setAdapter(langAdapter);
        mSpnLang.setSelection(0);


//        try {
//            com.epson.epos2.Log.setLogSettings(mContext, com.epson.epos2.Log.PERIOD_TEMPORARY, com.epson.epos2.Log.OUTPUT_STORAGE, null, 0, 1, com.epson.epos2.Log.LOGLEVEL_LOW);
//        } catch (Exception e) {
////            Toast.makeText(Itemwise_Saleslist.this, "Here8", Toast.LENGTH_SHORT).show();
//            ShowMsg.showException(e, "setLogSettings", mContext);
//        }
        mEditTarget = (EditText) findViewById(R.id.edtTarget);

        editText1_filter = new EditText(Itemwise_Saleslist.this);
        editText2_filter = new EditText(Itemwise_Saleslist.this);

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

                dpd.show(Itemwise_Saleslist.this.getFragmentManager(), "Datepickerdialog");




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

                dpd.show(Itemwise_Saleslist.this.getFragmentManager(), "Datepickerdialog");
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
                android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(Itemwise_Saleslist.this, R.style.timepicker_date_dialog, timePickerListener_open, hour, minute,
                        false);

                timePickerDialog.show();
            }
        });

        editText_to_day_visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(Itemwise_Saleslist.this, R.style.timepicker_date_dialog, timePickerListener_close, hour, minute,
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

        TextView inn = (TextView) findViewById(R.id.inn);
        TextView inn1 = (TextView) findViewById(R.id.inn1);
        TextView inn2 = (TextView) findViewById(R.id.inn2);

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

        inn.setText(insert1_cc);
        inn1.setText(insert1_cc);
        inn2.setText(insert1_cc);

        Cursor ccornn = db1.rawQuery("SELECT * FROM BTConn", null);
        if (ccornn.moveToFirst()) {
            nameget = ccornn.getString(1);
            addget = ccornn.getString(2);
            statussusb = ccornn.getString(3);
            mEditTarget.setText(addget);
        }

        getlisting = (Spinner) findViewById(R.id.chocolate_category);

        item_list = (LinearLayout) findViewById(R.id.item_list);
        modifier_list = (LinearLayout) findViewById(R.id.modifier_list);
        category_list = (LinearLayout) findViewById(R.id.category_list);

        tvkot = new TextView(Itemwise_Saleslist.this);
        totalsales_r = new TextView(Itemwise_Saleslist.this);
        avgsales_r = new TextView(Itemwise_Saleslist.this);
        final Calendar c = Calendar.getInstance();
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);

        mCredential = GoogleAccountCredential.usingOAuth2(
                Itemwise_Saleslist.this.getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        mProgress = new ProgressDialog(Itemwise_Saleslist.this, R.style.timepicker_date_dialog);
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


        if (player3name.toString().equals("Items")){
            getlisting.setSelection(0);
        }
        if (player3name.toString().equals("Category")){
            getlisting.setSelection(1);
        }



        i_mi_totalsales1 = (TextView) findViewById(R.id.totalsales1);
        i_cost = (TextView) findViewById(R.id.i_cost);
        mi_cost = (TextView) findViewById(R.id.mi_cost);
        i_cost_percent = (TextView) findViewById(R.id.i_cost_percent);

//        Toast.makeText(Itemwise_Saleslist.this, "1 "+editText1_filter.getText().toString(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(Itemwise_Saleslist.this, "2 "+editText2_filter.getText().toString(), Toast.LENGTH_SHORT).show();


        DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
        downloadMusicfromInternet.execute(editText1_filter.getText().toString() + editText2_filter.getText().toString());

        final Cursor cursor_country1 = db1.rawQuery("SELECT * FROM Country_Selection", null);
        if (cursor_country1.moveToFirst()){
            str_country = cursor_country1.getString(1);
        }

        final String Text = getlisting.getSelectedItem().toString();

        //dialog.dismiss();
        if (Text.equals("Items")) {
            item_list.setVisibility(View.VISIBLE);
            modifier_list.setVisibility(View.GONE);
            category_list.setVisibility(View.GONE);
            final String selectQuery = "SELECT * FROM Itemwiseorderlistitems GROUP BY itemname";

            cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
            // The desired columns to be bound
            final String[] fromFieldNames = {"_id", "itemname", "itemno", "sales", "salespercentage", "itemtotalquan", "i_buying_price", "i_cost_value", "i_cost_percent", "mi_buying_price", "mi_cost_value", "mi_cost_percent", "mi_cost_percent", "mi_cost_percent", "mi_cost_percent", "mi_cost_percent", "mi_cost_percent", "mi_cost_percent", "barcode_value"};
            // the XML defined views which the data will be bound to
            final int[] toViewsID = {R.id.sno, R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity, R.id.i_buying_price, R.id.i_cost_value, R.id.i_cost_percent, R.id.mi_buying_price, R.id.mi_cost_value, R.id.mi_cost_percent, R.id.inn, R.id.inn1, R.id.inn2, R.id.inn3, R.id.inn4, R.id.inn5, R.id.barcode_value};
            //Log.e("Checamos que hay id", String.valueOf(R.id.name));
            adapter = new SimpleCursorAdapter(Itemwise_Saleslist.this, R.layout.itemwise_boxes_new_items, cursor1, fromFieldNames, toViewsID, 0);
//            listView.setAdapter(adapter);
            adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                    if (view.getId() == R.id.inn || view.getId() == R.id.inn1 || view.getId() == R.id.inn2 || view.getId() == R.id.inn3 || view.getId() == R.id.inn4 || view.getId() == R.id.inn5) {
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
            listView.setAdapter(adapter);

            adapter.setFilterQueryProvider(new FilterQueryProvider() {
                public Cursor runQuery(CharSequence constraint) {
                    return fetchCountriesByName_items(constraint.toString());
                }
            });

        }else {

            if (Text.equals("Category")) {
                item_list.setVisibility(View.GONE);
                modifier_list.setVisibility(View.GONE);
                category_list.setVisibility(View.VISIBLE);
                final String selectQuery = "SELECT * FROM Itemwiseorderlistcategory";

                cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                // The desired columns to be bound
                final String[] fromFieldNames = {"_id", "categoryname", "sales", "salespercentage", "itemtotalquan", "itemtotalquan"};
                // the XML defined views which the data will be bound to
                final int[] toViewsID = {R.id.sno, R.id.num, R.id.sales, R.id.salesper, R.id.quantity, R.id.inn};
                //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                adapter = new SimpleCursorAdapter(Itemwise_Saleslist.this, R.layout.categorywise_boxes, cursor1, fromFieldNames, toViewsID, 0);
//                listView.setAdapter(adapter);
                adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                        if (view.getId() == R.id.inn) {
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
                listView.setAdapter(adapter);

                adapter.setFilterQueryProvider(new FilterQueryProvider() {
                    public Cursor runQuery(CharSequence constraint) {
                        return fetchCountriesByName_category(constraint.toString());
                    }
                });
            }

        }

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                adapter.getFilter().filter(s.toString());
            }
        });

        getlisting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spineerseleitem = parent.getItemAtPosition(position).toString();
                if (spineerseleitem.equals("Items")) {
                    item_list.setVisibility(View.VISIBLE);
                    modifier_list.setVisibility(View.GONE);
                    category_list.setVisibility(View.GONE);
                    final String selectQuery = "SELECT * FROM Itemwiseorderlistitems GROUP BY itemname";

                    cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"_id", "itemname", "itemno", "sales", "salespercentage", "itemtotalquan", "i_buying_price", "i_cost_value", "i_cost_percent", "mi_buying_price", "mi_cost_value", "mi_cost_percent", "mi_cost_percent", "mi_cost_percent", "mi_cost_percent", "mi_cost_percent", "mi_cost_percent", "mi_cost_percent", "barcode_value"};
                    // the XML defined views which the data will be bound to
                    final int[] toViewsID = {R.id.sno, R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity, R.id.i_buying_price, R.id.i_cost_value, R.id.i_cost_percent, R.id.mi_buying_price, R.id.mi_cost_value, R.id.mi_cost_percent, R.id.inn, R.id.inn1, R.id.inn2, R.id.inn3, R.id.inn4, R.id.inn5, R.id.barcode_value};
                    //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                    adapter = new SimpleCursorAdapter(Itemwise_Saleslist.this, R.layout.itemwise_boxes_new_items, cursor1, fromFieldNames, toViewsID, 0);
//                    listView.setAdapter(adapter);
                    adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                        @Override
                        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                            if (view.getId() == R.id.inn || view.getId() == R.id.inn1 || view.getId() == R.id.inn2 || view.getId() == R.id.inn3 || view.getId() == R.id.inn4 || view.getId() == R.id.inn5) {
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
                    listView.setAdapter(adapter);

                    adapter.setFilterQueryProvider(new FilterQueryProvider() {
                        public Cursor runQuery(CharSequence constraint) {
                            return fetchCountriesByName_items(constraint.toString());
                        }
                    });

                }else {

                    if (spineerseleitem.equals("Category")) {
                        item_list.setVisibility(View.GONE);
                        modifier_list.setVisibility(View.GONE);
                        category_list.setVisibility(View.VISIBLE);
                        final String selectQuery = "SELECT * FROM Itemwiseorderlistcategory";

                        cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                        // The desired columns to be bound
                        final String[] fromFieldNames = {"_id", "categoryname", "sales", "salespercentage", "itemtotalquan", "itemtotalquan"};
                        // the XML defined views which the data will be bound to
                        final int[] toViewsID = {R.id.sno, R.id.num, R.id.sales, R.id.salesper, R.id.quantity, R.id.inn};
                        //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                        adapter = new SimpleCursorAdapter(Itemwise_Saleslist.this, R.layout.categorywise_boxes, cursor1, fromFieldNames, toViewsID, 0);
//                        listView.setAdapter(adapter);
                        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                            @Override
                            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                                if (view.getId() == R.id.inn) {
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
                        listView.setAdapter(adapter);

                        adapter.setFilterQueryProvider(new FilterQueryProvider() {
                            public Cursor runQuery(CharSequence constraint) {
                                return fetchCountriesByName_category(constraint.toString());
                            }
                        });
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = getlisting.getSelectedItem().toString();
                if (text.toString().equals("Category")){
                    final Dialog dialoge = new Dialog(Itemwise_Saleslist.this, R.style.timepicker_date_dialog);
                    dialoge.setContentView(R.layout.dialog_category_items_listview);
                    dialoge.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    dialoge.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    dialoge.show();

                    final Cursor cursor2 = (Cursor) parent.getItemAtPosition(position);
                    catt = cursor2.getString(cursor2.getColumnIndex("categoryname"));

                    final EditText search1 = (EditText) dialoge.findViewById(R.id.search_selecteditem);
                    final ListView gb = (ListView) dialoge.findViewById(R.id.listview);


                    TextView items_pane = (TextView) dialoge.findViewById(R.id.items_pane);

                    final String selectQuery = "SELECT * FROM Itemwiseorderlistitems WHERE category = '"+catt+"' GROUP BY itemname";

                    Cursor cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"itemtotalquan", "itemname", "itemno", "sales", "sales", "sales"};
                    // the XML defined views which the data will be bound to
                    final int[] toViewsID = {R.id.qty, R.id.itemname, R.id.indiv_price, R.id.total_price, R.id.inn, R.id.inn1};
                    //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                    final SimpleCursorAdapter adapter = new SimpleCursorAdapter(Itemwise_Saleslist.this, R.layout.dialog_items_report, cursor1, fromFieldNames, toViewsID, 0);
//                    gb.setAdapter(adapter);
                    adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
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
                    gb.setAdapter(adapter);

                    ImageButton btncancel = (ImageButton) dialoge.findViewById(R.id.btncancel);
                    btncancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialoge.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            donotshowKeyboard(Itemwise_Saleslist.this);
                            dialoge.dismiss();
                        }
                    });

                    adapter.setFilterQueryProvider(new FilterQueryProvider() {
                        public Cursor runQuery(CharSequence constraint) {
                            return fetchCountriesByName_items_cat(constraint.toString());
                        }
                    });


                    search1.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {
                        }

                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            adapter.getFilter().filter(s.toString());
                        }
                    });

                    items_pane.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String selectQuery = "SELECT * FROM Itemwiseorderlistitems WHERE category = '"+catt+"' GROUP BY itemname";

                            Cursor cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                            // The desired columns to be bound
                            final String[] fromFieldNames = {"itemtotalquan", "itemname", "itemno", "sales", "sales", "sales"};
                            // the XML defined views which the data will be bound to
                            final int[] toViewsID = {R.id.qty, R.id.itemname, R.id.indiv_price, R.id.total_price, R.id.inn, R.id.inn1};
                            //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                            final SimpleCursorAdapter adapter = new SimpleCursorAdapter(Itemwise_Saleslist.this, R.layout.dialog_items_report, cursor1, fromFieldNames, toViewsID, 0);
//                            gb.setAdapter(adapter);
                            adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
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
                            gb.setAdapter(adapter);

                            adapter.setFilterQueryProvider(new FilterQueryProvider() {
                                public Cursor runQuery(CharSequence constraint) {
                                    return fetchCountriesByName_items_cat(constraint.toString());
                                }
                            });


                            search1.addTextChangedListener(new TextWatcher() {

                                public void afterTextChanged(Editable s) {
                                }

                                public void beforeTextChanged(CharSequence s, int start,
                                                              int count, int after) {
                                }

                                public void onTextChanged(CharSequence s, int start,
                                                          int before, int count) {
                                    adapter.getFilter().filter(s.toString());
                                }
                            });
                        }
                    });


//                    TextView itemname = (TextView) dialoge.findViewById(R.id.itemname);
//                    TextView quantity = (TextView) dialoge.findViewById(R.id.quantity);
//                    TextView indiv_price = (TextView) dialoge.findViewById(R.id.indiv_price);
//                    TextView total_price = (TextView) dialoge.findViewById(R.id.total_price);
//
//                    Cursor cv = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE category = '"+catt+"'", null);
//                    if (cv.moveToFirst()){
//                        do {
//                            String na = cv.getString(2);
//                            String qu = cv.getString(5);
//                            String tp = cv.getString(3);
//
//
//                            itemname.setText(na);
//                            quantity.setText(qu);
//                            total_price.setText(tp);
//
//                            Cursor bn = db1.rawQuery("SELECT * FROM Items WHERE itemname = '"+na+"'", null);
//                            if (bn.moveToFirst()){
//                                String r = bn.getString(2);
//
//                                indiv_price.setText(r);
//                            }
//                        }while (cv.moveToNext());
//                    }
                }
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


                DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
                downloadMusicfromInternet.execute(editText1_filter.getText().toString() + editText2_filter.getText().toString());


                final String Text = getlisting.getSelectedItem().toString();

                //dialog.dismiss();
                if (Text.equals("Items")) {
                    item_list.setVisibility(View.VISIBLE);
                    modifier_list.setVisibility(View.GONE);
                    category_list.setVisibility(View.GONE);
                    final String selectQuery = "SELECT * FROM Itemwiseorderlistitems GROUP BY itemname";

                    cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"_id", "itemname", "itemno", "sales", "salespercentage", "itemtotalquan", "i_buying_price", "i_cost_value", "i_cost_percent", "mi_buying_price", "mi_cost_value", "mi_cost_percent", "mi_cost_percent", "mi_cost_percent", "mi_cost_percent", "mi_cost_percent", "mi_cost_percent", "mi_cost_percent", "barcode_value"};
                    // the XML defined views which the data will be bound to
                    final int[] toViewsID = {R.id.sno, R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity, R.id.i_buying_price, R.id.i_cost_value, R.id.i_cost_percent, R.id.mi_buying_price, R.id.mi_cost_value, R.id.mi_cost_percent, R.id.inn, R.id.inn1, R.id.inn2, R.id.inn3, R.id.inn4, R.id.inn5, R.id.barcode_value};
                    //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                    adapter = new SimpleCursorAdapter(Itemwise_Saleslist.this, R.layout.itemwise_boxes_new_items, cursor1, fromFieldNames, toViewsID, 0);
//                    listView.setAdapter(adapter);
                    adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                        @Override
                        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                            if (view.getId() == R.id.inn || view.getId() == R.id.inn1 || view.getId() == R.id.inn2 || view.getId() == R.id.inn3 || view.getId() == R.id.inn4 || view.getId() == R.id.inn5) {
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
                    listView.setAdapter(adapter);

                    adapter.setFilterQueryProvider(new FilterQueryProvider() {
                        public Cursor runQuery(CharSequence constraint) {
                            return fetchCountriesByName_items(constraint.toString());
                        }
                    });

                }else {
                    if (Text.equals("Category")) {
                        item_list.setVisibility(View.GONE);
                        modifier_list.setVisibility(View.GONE);
                        category_list.setVisibility(View.VISIBLE);
                        final String selectQuery = "SELECT * FROM Itemwiseorderlistcategory";

                        cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                        // The desired columns to be bound
                        final String[] fromFieldNames = {"_id", "categoryname", "sales", "salespercentage", "itemtotalquan", "itemtotalquan"};
                        // the XML defined views which the data will be bound to
                        final int[] toViewsID = {R.id.sno, R.id.num, R.id.sales, R.id.salesper, R.id.quantity, R.id.inn};
                        //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                        adapter = new SimpleCursorAdapter(Itemwise_Saleslist.this, R.layout.categorywise_boxes, cursor1, fromFieldNames, toViewsID, 0);
//                        listView.setAdapter(adapter);
                        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                            @Override
                            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                                if (view.getId() == R.id.inn) {
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
                        listView.setAdapter(adapter);

                        adapter.setFilterQueryProvider(new FilterQueryProvider() {
                            public Cursor runQuery(CharSequence constraint) {
                                return fetchCountriesByName_category(constraint.toString());
                            }
                        });
                    }

                }
            }
        });


        ImageButton action_print = (ImageButton) findViewById(R.id.action_print);
        action_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.isEmpty()){
                    //MenuItem bedMenuItem = menu.findItem(R.id.action_export);
                    //bedMenuItem.setVisible(false);
                    Toast.makeText(Itemwise_Saleslist.this, getString(R.string.no_report_to_export), Toast.LENGTH_SHORT).show();
                }else {
                    Cursor connnet = db1.rawQuery("SELECT * FROM IPConn", null);
                    if (connnet.moveToFirst()) {
                        ipnameget = connnet.getString(1);
                        portget = connnet.getString(2);
                        statusnet = connnet.getString(3);
                    }
                    connnet.close();

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
                    conn.close();
                    if (statusnet.toString().equals("ok") || statusnet_counter.toString().equals("ok") || statussusb.toString().equals("ok")) {

                        final String Text = getlisting.getSelectedItem().toString();

                        if (Text.equals("Items")) {
                            printbillcopy_minireceipt();
                        }
                        if (Text.equals("Category")) {
                            printbillcopy_minireceipt_category();
                        }
                        //dialog.dismiss();


                    }else {
                        final Dialog dialogconn = new Dialog(Itemwise_Saleslist.this, R.style.notitle);
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
                if (adapter.isEmpty()){
                    //MenuItem bedMenuItem = menu.findItem(R.id.action_export);
                    //bedMenuItem.setVisible(false);
                    Toast.makeText(Itemwise_Saleslist.this, getString(R.string.no_report_to_export), Toast.LENGTH_SHORT).show();
                }else {
                    //MenuItem bedMenuItem = menu.findItem(R.id.action_export);
                    //bedMenuItem.setVisible(true);
//                    Toast.makeText(Itemwise_Saleslist.this, "yes", Toast.LENGTH_SHORT).show();
                    sdff2 = new SimpleDateFormat("ddMMMyy");
                    currentDateandTimee1 = sdff2.format(new Date());

                    Date dt = new Date();
                    sdff1 = new SimpleDateFormat("hhmmssaa");
                    timee1 = sdff1.format(dt);

                    ExportDatabaseCSVTask task=new ExportDatabaseCSVTask();
                    task.execute();
                }
            }
        });


        ImageButton action_exportmail = (ImageButton) findViewById(R.id.action_exportmail);
        action_exportmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.isEmpty()) {
                    //MenuItem bedMenuItem = menu.findItem(R.id.action_export);
                    //bedMenuItem.setVisible(false);
                    Toast.makeText(Itemwise_Saleslist.this, getString(R.string.no_report_to_mail), Toast.LENGTH_SHORT).show();
                }else {
                    sdff2 = new SimpleDateFormat("ddMMMyy");
                    currentDateandTimee1 = sdff2.format(new Date());

                    Date dt1 = new Date();
                    sdff1 = new SimpleDateFormat("hhmmssaa");
                    timee1 = sdff1.format(dt1);

//                    db1 = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor cursor = db1.rawQuery("SELECT * FROM Companydetailss", null);
                    if (cursor.moveToFirst()) {
                        companynameis = cursor.getString(1);
                    }else {
                        companynameis = "";
                    }
                    cursor.close();

                    File dbFile = getDatabasePath("mydb_Salesdata");
                    //Log.v(TAG, "Db path is: "+dbFile);  //get the path of db

                    Cursor ccursore = db1.rawQuery("SELECT * FROM Email_setup", null);
                    if (ccursore.moveToFirst()) {
                        Cursor ccursoree = db1.rawQuery("SELECT * FROM Email_recipient", null);
                        if (ccursoree.moveToFirst()) {
//                            File exportDir = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_reports/IVEPOS_product_report");
                            File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_reports/IVEPOS_product_report");
                            if (!exportDir.exists()) {
                                exportDir.mkdirs();
                            }

                            file = new File(exportDir, "IvePOS_product_report" + currentDateandTimee1 + "_" + timee1 + ".csv");
                            try {

                                file.createNewFile();
                                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

                                //ormlite core method
//                List<Person> listdata=dbhelper.GetDataPerson();
//                Person person=null;

                                // this is the Column of the table and same for Header of CSV file
                                String arrStr1[] ={"Category name", "Quantity sold", "Sales("+insert1_rs+")", "Sales(%)"};
                                csvWrite.writeNext(arrStr1);

//                                db = openOrCreateDatabase("mydb_Salesdata",
//                                        Context.MODE_PRIVATE, null);

                                Cursor curCSV1 = db.rawQuery("SELECT * FROM Itemwiseorderlistcategory",null);
                                //csvWrite.writeNext(curCSV.getColumnNames());

                                while(curCSV1.moveToNext())  {
                                    String arrStr[] ={curCSV1.getString(2), curCSV1.getString(5), curCSV1.getString(3), curCSV1.getString(4)};
//	                curCSV.getString(2),curCSV.getString(3),curCSV.getString(4),
                                    csvWrite.writeNext(arrStr);

                                }

                                String arrStr1_1[] ={"", "", "", ""};
                                csvWrite.writeNext(arrStr1_1);
                                String arrStr1_11[] ={"", "", "", ""};
                                csvWrite.writeNext(arrStr1_11);

                                String arrStr11[] ={"Item name", "barcode_value", "Category", "Individual price", "Quantity sold", "Sales("+insert1_rs+")", "Sales(%)"};
                                csvWrite.writeNext(arrStr11);

                                Cursor curCSV = db.rawQuery("SELECT * FROM Itemwiseorderlistitems",null);
                                //csvWrite.writeNext(curCSV.getColumnNames());

                                while(curCSV.moveToNext())  {
                                    String arrStr[] ={curCSV.getString(2), curCSV.getString(6), curCSV.getString(1), curCSV.getString(5), curCSV.getString(3), curCSV.getString(4)};
//	                curCSV.getString(2),curCSV.getString(3),curCSV.getString(4),
                                    csvWrite.writeNext(arrStr);

                                }

                                String arrStr11_1[] ={"", "", "", ""};
                                csvWrite.writeNext(arrStr11_1);
                                String arrStr11_11[] ={"", "", "", ""};
                                csvWrite.writeNext(arrStr11_11);

                                String arrStr111[] ={"Modifier name", "Category", "Individual price", "Quantity sold", "Sales("+insert1_rs+")", "Sales(%)"};
                                csvWrite.writeNext(arrStr111);

                                curCSV = db.rawQuery("SELECT * FROM Itemwiseorderlistmodifiers", null);
                                //csvWrite.writeNext(curCSV.getColumnNames());

                                while(curCSV.moveToNext())  {
                                    String arrStr[] ={curCSV.getString(2), curCSV.getString(6), curCSV.getString(1), curCSV.getString(5), curCSV.getString(3), curCSV.getString(4)};
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
                    ccursore.close();

                    String pone = "", ptwo = "", pthree = "";

                    TextView topone = new TextView(Itemwise_Saleslist.this);
                    TextView topitem1percent = new TextView(Itemwise_Saleslist.this);
                    TextView toptwo = new TextView(Itemwise_Saleslist.this);
                    TextView topitem2percent = new TextView(Itemwise_Saleslist.this);
                    TextView topthree = new TextView(Itemwise_Saleslist.this);
                    TextView topitem3percent = new TextView(Itemwise_Saleslist.this);
                    TextView totalsalesitemwise = new TextView(Itemwise_Saleslist.this);


                    Cursor one = db.rawQuery("SELECT MAX(sales) FROM Itemwiseorderlistitems ", null);
                    if (one.moveToFirst()) {
                        int one11 = one.getInt(0);
                        one = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE sales = '" + one11 + "' ", null);
                        if (one.moveToFirst()) {
                            one1 = one.getString(2);
                            //salesee1 = one.getString(3);
                            pone = one.getString(4);
                            topone.setText(one1);
                            topitem1percent.setText(pone);
                            topitem1percent.append("%");
//                                    one = db.rawQuery("SELECT MAX(sales) FROM Itemwiseorderlistitems WHERE MAX(sales)<'"+one11+"' ", null);
//                                    if (one.moveToFirst()){
//                                        two2 = one.getString(2);
//                                        toptwo.setText(two2);
//                                    }
                            Cursor two = db.rawQuery("SELECT MAX(sales) FROM Itemwiseorderlistitems WHERE sales < '" + one11 + "' ", null);
                            if (two.moveToFirst()) {
                                int two111 = two.getInt(0);
                                two = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE sales = '" + two111 + "' ", null);
                                if (two.moveToFirst()) {
                                    two2 = two.getString(2);
                                    //salesee2 = one.getString(3);
                                    ptwo = two.getString(4);
                                    toptwo.setText(two2);
                                    topitem2percent.setText(ptwo);
                                    topitem2percent.append("%");

                                    Cursor three = db.rawQuery("SELECT MAX(sales) FROM Itemwiseorderlistitems WHERE sales < '" + two111 + "' ", null);
                                    if (three.moveToFirst()) {
                                        int three11 = three.getInt(0);
                                        three = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE sales = '" + three11 + "' ", null);
                                        if (three.moveToFirst()) {
                                            three3 = three.getString(2);
                                            //salesee3 = one.getString(3);
                                            pthree = three.getString(4);
                                            topthree.setText(three3);
                                            topitem3percent.setText(pthree);
                                            topitem3percent.append("%");
                                        } else {
                                            topthree.setText("NA");
                                            topitem3percent.setText("0%");
                                        }
                                    }
                                } else {
                                    toptwo.setText("NA");
                                    topitem2percent.setText("0%");
                                }
                            }
                        } else {
                            topone.setText("NA");
                            topitem1percent.setText("0%");
                        }
                    }
                    one.close();


                    Cursor top1item = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE itemname = '" + topone.getText().toString() + "' AND salespercentage = '" + pone + "' ", null);
                    if (top1item.moveToFirst()) {
                        salesee1 = top1item.getString(3);
                    }
                    top1item.close();
                    Cursor top2item = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE itemname = '" + toptwo.getText().toString() + "' AND salespercentage = '" + ptwo + "' ", null);
                    if (top2item.moveToFirst()) {
                        salesee2 = top2item.getString(3);
                    }
                    top2item.close();
                    Cursor top3item = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE itemname = '" + topthree.getText().toString() + "' AND salespercentage = '" + pthree + "' ", null);
                    if (top3item.moveToFirst()) {
                        salesee3 = top3item.getString(3);
                    }
                    top3item.close();


                    Cursor cursor11 = db.rawQuery("SELECT SUM(total) FROM Billnumber WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' ", null);
                    if (cursor11.moveToFirst()) {
                        level = cursor11.getInt(0);
                        total = String.valueOf(level);
                    }
                    cursor11.close();
//                Toast.makeText(Itemwise_Saleslist.this, Text+" total is "+total, Toast.LENGTH_SHORT).show();
                    totalsalesitemwise.setText(insert1_cc);
                    totalsalesitemwise.append(total);


                    String url = "www.intuitionsoftwares.com";

                    String msg = "Product report (" + editText11.getText().toString() + " - " + editText22.getText().toString() + ")\n\n" + "Overview" + " (Detailed report attached)" +
                            "\n\nTotal sales: " + totalsalesitemwise.getText().toString() + "\n\nTop selling products:\n\n" +
                            "1." + topone.getText().toString() + " - "+insert1_rs+" " + salesee1 + "(" + topitem1percent.getText().toString() + ")\n" +
                            "2." + toptwo.getText().toString() + " - "+insert1_rs+" " + salesee2 + "(" + topitem2percent.getText().toString() + ")\n" +
                            "3." + topthree.getText().toString() + " - "+insert1_rs+" " + salesee3 + "(" + topitem3percent.getText().toString() + ")\n\n" +
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
//                                        Toast.makeText(Itemwise_Saleslist.this, "yahoo "+un, Toast.LENGTH_LONG).show();
                                        Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
                                        if (cursor1.moveToFirst()) {
                                            do {
                                                String unn = cursor1.getString(3);
                                                String toEmails = unn;
                                                toEmailList = Arrays.asList(toEmails
                                                        .split("\\s*,\\s*"));
                                                new SendMailTask_Yahoo_attachment_Productlist(Itemwise_Saleslist.this).execute(un,
                                                        pwd, toEmailList, companynameis, msg, currentDateandTimee1, timee1);
                                            } while (cursor1.moveToNext());
                                        }


                                    }else {
                                        if (em_ca.toString().equals("Hotmail")){
//                                            Toast.makeText(Itemwise_Saleslist.this, "Hotmail and Outlook "+un, Toast.LENGTH_LONG).show();
                                            Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
                                            if (cursor1.moveToFirst()) {
                                                do {
                                                    String unn = cursor1.getString(3);
                                                    String toEmails = unn;
                                                    toEmailList = Arrays.asList(toEmails
                                                            .split("\\s*,\\s*"));
                                                    new SendMailTask_Hotmail_Outlook_attachment_Productlist(Itemwise_Saleslist.this).execute(un,
                                                            pwd, toEmailList, companynameis, msg, currentDateandTimee1, timee1);
                                                } while (cursor1.moveToNext());
                                            }
                                        }else {
                                            if (em_ca.toString().equals("Office365")) {
//                                                Toast.makeText(Itemwise_Saleslist.this, "office 365 " + un, Toast.LENGTH_LONG).show();
                                                Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
                                                if (cursor1.moveToFirst()) {
                                                    do {
                                                        String unn = cursor1.getString(3);
                                                        String toEmails = unn;
                                                        toEmailList = Arrays.asList(toEmails
                                                                .split("\\s*,\\s*"));
                                                        new SendMailTask_Office365_attachment_Productlist(Itemwise_Saleslist.this).execute(un,
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
                            final Dialog dialoge = new Dialog(Itemwise_Saleslist.this, R.style.timepicker_date_dialog);
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
                                    Intent intent = new Intent(Itemwise_Saleslist.this, EmailSetup_Recipients.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
//                                                                Itemwise_Saleslist.this.finish();
                                    dialoge.dismiss();
                                }
                            });

                            Button gotosettings1 = (Button) dialoge.findViewById(R.id.gotosettings1);
                            gotosettings1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Itemwise_Saleslist.this, EmailSetup_Recipients.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
//                                                                Itemwise_Saleslist.this.finish();
                                    dialoge.dismiss();
                                }
                            });


                        }
                    }else {
                        Cursor cursoree = db1.rawQuery("SELECT * FROM Email_recipient", null);
                        if (cursoree.moveToFirst()){
                            //only sender not there
                            final Dialog dialoge = new Dialog(Itemwise_Saleslist.this, R.style.timepicker_date_dialog);
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
                                    Intent intent = new Intent(Itemwise_Saleslist.this, EmailSetup.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
//                                                                Itemwise_Saleslist.this.finish();
                                    dialoge.dismiss();
                                }
                            });

                            Button gotosettings1 = (Button) dialoge.findViewById(R.id.gotosettings1);
                            gotosettings1.setVisibility(View.GONE);
                            gotosettings1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Itemwise_Saleslist.this, EmailSetup.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
//                                                                Itemwise_Saleslist.this.finish();
                                    dialoge.dismiss();
                                }
                            });

                        }else {
                            //both recipient and sender not there
                            final Dialog dialoge = new Dialog(Itemwise_Saleslist.this, R.style.timepicker_date_dialog);
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
                                    Intent intent = new Intent(Itemwise_Saleslist.this, EmailSetup.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
//                                                                Itemwise_Saleslist.this.finish();
                                    dialoge.dismiss();
                                }
                            });

                            Button gotosettings1 = (Button) dialoge.findViewById(R.id.gotosettings1);
                            gotosettings1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Itemwise_Saleslist.this, EmailSetup_Recipients.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
//                                                                Itemwise_Saleslist.this.finish();
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
        private final ProgressDialog dialog = new ProgressDialog(Itemwise_Saleslist.this, R.style.timepicker_date_dialog);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage(getString(R.string.setmessage13));
            this.dialog.show();

        }
        protected Boolean doInBackground(final String... args){

            File dbFile = getDatabasePath("mydb_Salesdata");
            //Log.v(TAG, "Db path is: "+dbFile);  //get the path of db

//            File exportDir = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_reports/IVEPOS_product_report");
            File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_reports/IVEPOS_product_report");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            file = new File(exportDir, "IvePOS_product_report"+currentDateandTimee1+"_"+timee1+".csv");
            try {

                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

                //ormlite core method
//                List<Person> listdata=dbhelper.GetDataPerson();
//                Person person=null;

                // this is the Column of the table and same for Header of CSV file
                String arrStr1[] ={"Category name", "Quantity sold", "Sales("+insert1_rs+")", "Sales(%)"};
                csvWrite.writeNext(arrStr1);

//                db = openOrCreateDatabase("mydb_Salesdata",
//                        Context.MODE_PRIVATE, null);

                Cursor curCSV1 = db.rawQuery("SELECT * FROM Itemwiseorderlistcategory",null);
                //csvWrite.writeNext(curCSV.getColumnNames());

                while(curCSV1.moveToNext())  {
                    String arrStr[] ={curCSV1.getString(2), curCSV1.getString(5), curCSV1.getString(3), curCSV1.getString(4)};
//	                curCSV.getString(2),curCSV.getString(3),curCSV.getString(4),
                    csvWrite.writeNext(arrStr);

                }

                String arrStr1_1[] ={"", "", "", ""};
                csvWrite.writeNext(arrStr1_1);
                String arrStr1_11[] ={"", "", "", ""};
                csvWrite.writeNext(arrStr1_11);

                String arrStr11[] ={"Item name", "barcode_value", "Category", "Individual price", "Quantity sold", "Sales("+insert1_rs+")", "Sales(%)"};
                csvWrite.writeNext(arrStr11);

                Cursor curCSV = db.rawQuery("SELECT * FROM Itemwiseorderlistitems",null);
                //csvWrite.writeNext(curCSV.getColumnNames());

                while(curCSV.moveToNext())  {
                    String arrStr[] ={curCSV.getString(2), curCSV.getString(13), curCSV.getString(6), curCSV.getString(1), curCSV.getString(5), curCSV.getString(3), curCSV.getString(4)};
//	                curCSV.getString(2),curCSV.getString(3),curCSV.getString(4),
                    csvWrite.writeNext(arrStr);

                }

                String arrStr11_1[] ={"", "", "", ""};
                csvWrite.writeNext(arrStr11_1);
                String arrStr11_11[] ={"", "", "", ""};
                csvWrite.writeNext(arrStr11_11);

                String arrStr111[] ={"Modifier name", "Category", "Individual price", "Quantity sold", "Sales("+insert1_rs+")", "Sales(%)"};
                csvWrite.writeNext(arrStr111);

                curCSV = db.rawQuery("SELECT * FROM Itemwiseorderlistmodifiers", null);
                //csvWrite.writeNext(curCSV.getColumnNames());

                while(curCSV.moveToNext())  {
                    String arrStr[] ={curCSV.getString(2), curCSV.getString(6), curCSV.getString(1), curCSV.getString(5), curCSV.getString(3), curCSV.getString(4)};
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
                Toast.makeText(Itemwise_Saleslist.this, getString(R.string.export_successful), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(Itemwise_Saleslist.this, getString(R.string.export_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }


    class DownloadMusicfromInternet extends AsyncTask<String, Void, Integer> {

        private ProgressDialog dialog = new ProgressDialog(Itemwise_Saleslist.this, R.style.timepicker_date_dialog);


        @Override
        protected Integer doInBackground(String... params) {


            final String str = getlisting.getSelectedItem().toString();
//            if (str.equals("Items")) {

            Cursor cursor = db.rawQuery("Select DISTINCT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' GROUP BY itemname ", null);//replace to cursor = dbHelper.fetchAllHotels();
            db.execSQL("delete from Itemwiseorderlistitems");
            if (cursor.moveToFirst()) {
                do {

                    Itemtype = cursor.getString(5);
                    String barc = cursor.getString(48);

                    if (Itemtype.toString().equals("Item")) {

                        itemtotal = cursor.getString(1);
                        String cat1 = cursor.getString(43);

//                            final TableRow row = new TableRow(Itemwise_Saleslist.this);
//                            row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
//                                    TableRow.LayoutParams.MATCH_PARENT, 1.0f));
//                            row.setGravity(Gravity.CENTER_VERTICAL);

//                            db1 = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor modcursor1 = db1.rawQuery("Select * from Items WHERE itemname = '" + itemtotal + "' ", null);
                        if (modcursor1.moveToFirst()) {

                            do {

                                id = modcursor1.getString(0);

                                id = modcursor1.getString(2);

//                                barc = modcursor1.getString(16);
                            } while (modcursor1.moveToNext());
                        }
                        modcursor1.close();

                        final TextView tv1 = new TextView(Itemwise_Saleslist.this);
//                            tv1.setLayoutParams(new TableRow.LayoutParams(260, ViewGroup.LayoutParams.MATCH_PARENT));
//                            //tv.setBackgroundResource(R.drawable.cell_shape);
//                            tv1.setGravity(Gravity.CENTER);
//                            tv1.setTextSize(15);
//                            tv1.setTypeface(null, Typeface.NORMAL);
//                            tv1.setPadding(5, 0, 0, 0);
//                            tv1.setBackgroundResource(R.drawable.cell_shape);
                        //text = cursor.getString(1);
                        tv1.setText(itemtotal);
                        //row.addView(tv1);

                        Cursor modcursor = db.rawQuery("Select SUM(total) from All_Sales WHERE itemname = '" + itemtotal + "' AND datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' GROUP BY itemname ", null);
                        if (modcursor.moveToFirst()) {

                            do {
                                level = modcursor.getInt(0);
                                total1 = String.valueOf(level);
                                final TextView tv2 = new TextView(Itemwise_Saleslist.this);
//                                    tv2.setLayoutParams(new TableRow.LayoutParams(150, ViewGroup.LayoutParams.MATCH_PARENT));
//                                    //tv.setBackgroundResource(R.drawable.cell_shape);
//                                    tv2.setGravity(Gravity.CENTER);
//                                    tv2.setTextSize(15);
//                                    tv2.setTypeface(null, Typeface.NORMAL);
//                                    tv2.setPadding(5, 0, 0, 0);
//                                    tv2.setBackgroundResource(R.drawable.cell_shape);
                                //text = cursor.getString(1);
                                tv2.setText(total1);
                                //row.addView(tv2);
                            } while (modcursor.moveToNext());
                        }
                        modcursor.close();

                        Cursor modcursorsor = db.rawQuery("Select SUM(quantity) from All_Sales WHERE itemname = '" + itemtotal + "' AND datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' GROUP BY itemname ", null);
                        if (modcursorsor.moveToFirst()) {

                            do {
                                float level = modcursorsor.getFloat(0);
                                total1quan = String.valueOf(level);
                                final TextView tv2 = new TextView(Itemwise_Saleslist.this);
//                                    tv2.setLayoutParams(new TableRow.LayoutParams(150, ViewGroup.LayoutParams.MATCH_PARENT));
//                                    //tv.setBackgroundResource(R.drawable.cell_shape);
//                                    tv2.setGravity(Gravity.CENTER);
//                                    tv2.setTextSize(15);
//                                    tv2.setTypeface(null, Typeface.NORMAL);
//                                    tv2.setPadding(5, 0, 0, 0);
//                                    tv2.setBackgroundResource(R.drawable.cell_shape);
                                //text = cursor.getString(1);
                                tv2.setText(total1quan);
                                //row.addView(tv2);
                            } while (modcursorsor.moveToNext());
                        }
                        modcursorsor.close();


                        Cursor cursorr11 = db.rawQuery("SELECT SUM(subtotal) FROM Billnumber WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' ", null);
                        if (cursorr11.moveToFirst()) {
                            level = cursorr11.getInt(0);
                            total = String.valueOf(level);
                        }
                        cursorr11.close();

                        float perc = Float.parseFloat(total1) * 100 / Float.parseFloat(total);
                        String percen = String.format("%.2f", perc);

                        final TextView tv3 = new TextView(Itemwise_Saleslist.this);
//                            tv3.setLayoutParams(new TableRow.LayoutParams(170, ViewGroup.LayoutParams.MATCH_PARENT));
//                            //tv.setBackgroundResource(R.drawable.cell_shape);
//                            tv3.setGravity(Gravity.CENTER);
//                            tv3.setTextSize(15);
//                            tv3.setTypeface(null, Typeface.NORMAL);
//                            tv3.setPadding(5, 0, 0, 0);
//                            tv3.setBackgroundResource(R.drawable.cell_shape);
                        //text = cursor.getString(1);
                        tv3.setText(percen);
                        //row.addView(tv3);

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("itemno", String.valueOf(id));
                        contentValues.put("itemname", itemtotal);
                        contentValues.put("sales", total1);
                        contentValues.put("salespercentage", percen);
                        contentValues.put("itemtotalquan", total1quan);
                        contentValues.put("barcode_value", barc);
                        contentValues.put("category", cat1);
//                        Cursor cat = db1.rawQuery("SELECT * FROM Items WHERE itemname = '"+itemtotal+"'", null);
//                        if (cat.moveToFirst()){
//                            String cat1 = cat.getString(4);
//                            contentValues.put("category", cat1);
//                        }else {
//                            contentValues.put("category", "Renamed");
//                        }
//                        cat.close();

                        float a2 = 0;
                        Cursor cursor1 = db1.rawQuery("SELECT SUM(total_price) FROM Vendor_sold_item_details WHERE itemname = '" + itemtotal + "'", null);
                        if (cursor1.moveToFirst()) {
                            float a = cursor1.getFloat(0);

                            Cursor cursor2 = db1.rawQuery("SELECT SUM(qty_add) FROM Vendor_sold_item_details WHERE itemname = '" + itemtotal + "'", null);
                            if (cursor2.moveToFirst()) {
                                int a1 = cursor2.getInt(0);

                                a2 = a / a1;
                            }
                        }
                        cursor1.close();

                        Log.d("a2", "outcome = " +a2);

                        if (a2 == 0 || String.valueOf(a2).equals("NaN")){
                            contentValues.put("i_buying_price", "0.0");
                            contentValues.put("i_cost_value", "0.0");
                            contentValues.put("i_cost_percent", "0.0");
                        }else {
                            contentValues.put("i_buying_price", String.valueOf(a2));

                            float a3 = Float.parseFloat(total1quan) * a2;
                            contentValues.put("i_cost_value", String.format("%.1f", a3));

                            float a4 = (a3 / Float.parseFloat(total1)) * 100;
                            contentValues.put("i_cost_percent", String.format("%.1f", a4));
                        }

                        float b = 0;
                        Cursor cursor2 = db1.rawQuery("SELECT * FROM Ingredient_items_list WHERE itemname = '" + itemtotal + "'", null);
                        if (cursor2.moveToFirst()){
                            do {
                                String ingr_name = cursor2.getString(1);
                                String used = cursor2.getString(3);

                                Cursor cursor3 = db1.rawQuery("SELECT * FROM Ingredients WHERE ingredient_name = '"+ingr_name+"'", null);
                                if (cursor3.moveToFirst()){
                                    String avg_pr = cursor3.getString(13);

                                    b = b + (Float.parseFloat(avg_pr) * Float.parseFloat(used));
                                }
                            }while (cursor2.moveToNext());
                        }
                        cursor2.close();

                        contentValues.put("mi_buying_price", String.format("%.1f", b));

                        float a31 = Float.parseFloat(total1quan)*b;
                        contentValues.put("mi_cost_value", String.format("%.1f", a31));

                        float a41 = (a31/Float.parseFloat(total1))*100;
                        contentValues.put("mi_cost_percent", String.format("%.1f", a41));




                        db.insert("Itemwiseorderlistitems", null, contentValues);
                    }
                }while (cursor.moveToNext());
            }
            cursor.close();
//            }

            /*//modifiers not required now
//            if (str.equals("Modifiers")) {
            Cursor ccursor = db.rawQuery("Select DISTINCT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' AND type = 'Modifier' GROUP BY itemname ", null);//replace to cursor = dbHelper.fetchAllHotels();
            db.execSQL("delete from Itemwiseorderlistmodifiers");
            //Toast.makeText(Itemwise_Saleslist.this, "Modifiersrsrsrsrsrsrsrs", Toast.LENGTH_SHORT).show();
            if (ccursor.moveToFirst()) {
                do {
                    //Toast.makeText(Itemwise_Saleslist.this, "Modifiersrsrsrsrsrsrsrs111", Toast.LENGTH_SHORT).show();
                    //do {
                    //Toast.makeText(Itemwise_Saleslist.this, "Modifiersrsrsrsrsrsrsrs222", Toast.LENGTH_SHORT).show();

                    String itemtotal = ccursor.getString(1);

//                        final TableRow row = new TableRow(Itemwise_Saleslist.this);
//                        row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
//                                TableRow.LayoutParams.MATCH_PARENT, 1.0f));
//                        row.setGravity(Gravity.CENTER_VERTICAL);

//                        db1 = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor modcursor1 = db1.rawQuery("Select * from Modifiers WHERE modifiername = '" + itemtotal + "' ", null);

                    if (modcursor1.moveToFirst()) {
                        do {

                            id = modcursor1.getString(0);
//                                final TextView tv0 = new TextView(Itemwise_Saleslist.this);
//                                tv0.setLayoutParams(new TableRow.LayoutParams(90, ViewGroup.LayoutParams.MATCH_PARENT));
//                                //tv.setBackgroundResource(R.drawable.cell_shape);
//                                tv0.setGravity(Gravity.CENTER);
//                                tv0.setTextSize(15);
//                                tv0.setTypeface(null, Typeface.NORMAL);
//                                tv0.setPadding(5, 0, 0, 0);
//                                tv0.setBackgroundResource(R.drawable.cell_shape);
//                                //text = cursor.getString(1);
//                                tv0.setText(id);
//                                row.addView(tv0);

                            id = modcursor1.getString(2);
//                                final TextView tv = new TextView(Itemwise_Saleslist.this);
//                                tv.setLayoutParams(new TableRow.LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT));
//                                //tv.setBackgroundResource(R.drawable.cell_shape);
//                                tv.setGravity(Gravity.CENTER);
//                                tv.setTextSize(15);
//                                tv.setTypeface(null, Typeface.NORMAL);
//                                tv.setPadding(5, 0, 0, 0);
//                                tv.setBackgroundResource(R.drawable.cell_shape);
//                                //text = cursor.getString(1);
//                                tv.setText(id);
//                                row.addView(tv);
                        } while (modcursor1.moveToNext());
                    }
                    modcursor1.close();

//                        final TextView tv1 = new TextView(Itemwise_Saleslist.this);
//                        tv1.setLayoutParams(new TableRow.LayoutParams(260, ViewGroup.LayoutParams.MATCH_PARENT));
//                        //tv.setBackgroundResource(R.drawable.cell_shape);
//                        tv1.setGravity(Gravity.CENTER);
//                        tv1.setTextSize(15);
//                        tv1.setTypeface(null, Typeface.NORMAL);
//                        tv1.setPadding(5, 0, 0, 0);
//                        tv1.setBackgroundResource(R.drawable.cell_shape);
//                        //text = cursor.getString(1);
//                        tv1.setText(itemtotal);
//                        row.addView(tv1);

                    Cursor modcursor = db.rawQuery("Select SUM(total) from All_Sales WHERE itemname = '" + itemtotal + "' AND datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' GROUP BY itemname ", null);

                    if (modcursor.moveToFirst()) {
                        do {
                            level = modcursor.getInt(0);
                            total1 = String.valueOf(level);
//                                final TextView tv2 = new TextView(Itemwise_Saleslist.this);
//                                tv2.setLayoutParams(new TableRow.LayoutParams(150, ViewGroup.LayoutParams.MATCH_PARENT));
//                                //tv.setBackgroundResource(R.drawable.cell_shape);
//                                tv2.setGravity(Gravity.CENTER);
//                                tv2.setTextSize(15);
//                                tv2.setTypeface(null, Typeface.NORMAL);
//                                tv2.setPadding(5, 0, 0, 0);
//                                tv2.setBackgroundResource(R.drawable.cell_shape);
//                                //text = cursor.getString(1);
//                                tv2.setText(total1);
//                                row.addView(tv2);
                        } while (modcursor.moveToNext());
                    }
                    modcursor.close();

                    Cursor modcursorsor = db.rawQuery("Select SUM(quantity) from All_Sales WHERE itemname = '" + itemtotal + "' AND datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' GROUP BY itemname ", null);
                    if (modcursorsor.moveToFirst()) {

                        do {
                            float level = modcursorsor.getFloat(0);
                            total1quan = String.valueOf(level);
//                                final TextView tv2 = new TextView(Itemwise_Saleslist.this);
//                                tv2.setLayoutParams(new TableRow.LayoutParams(150, ViewGroup.LayoutParams.MATCH_PARENT));
//                                //tv.setBackgroundResource(R.drawable.cell_shape);
//                                tv2.setGravity(Gravity.CENTER);
//                                tv2.setTextSize(15);
//                                tv2.setTypeface(null, Typeface.NORMAL);
//                                tv2.setPadding(5, 0, 0, 0);
//                                tv2.setBackgroundResource(R.drawable.cell_shape);
//                                //text = cursor.getString(1);
//                                tv2.setText(total1quan);
//                                row.addView(tv2);
                        } while (modcursorsor.moveToNext());
                    }
                    modcursorsor.close();

                    Cursor cursorr11 = db.rawQuery("SELECT SUM(total) FROM Billnumber WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' ", null);
                    if (cursorr11.moveToFirst()) {
                        level = cursorr11.getInt(0);
                        total = String.valueOf(level);
                    }
                    cursorr11.close();

                    float perc = Float.parseFloat(total1) * 100 / Float.parseFloat(total);
                    String percen = String.format("%.2f", perc);

//                        final TextView tv3 = new TextView(Itemwise_Saleslist.this);
//                        tv3.setLayoutParams(new TableRow.LayoutParams(170, ViewGroup.LayoutParams.MATCH_PARENT));
//                        //tv.setBackgroundResource(R.drawable.cell_shape);
//                        tv3.setGravity(Gravity.CENTER);
//                        tv3.setTextSize(15);
//                        tv3.setTypeface(null, Typeface.NORMAL);
//                        tv3.setPadding(5, 0, 0, 0);
//                        tv3.setBackgroundResource(R.drawable.cell_shape);
//                        //text = cursor.getString(1);
//                        tv3.setText(percen);
//                        row.addView(tv3);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("modno", String.valueOf(id));
                    contentValues.put("modname", itemtotal);
                    contentValues.put("sales", total1);
                    contentValues.put("salespercentage", percen);
                    contentValues.put("modtotalquan", total1quan);
                    Cursor cat = db1.rawQuery("SELECT * FROM Modifiers WHERE modifiername = '"+itemtotal+"'", null);
                    if (cat.moveToFirst()){
                        String cat1 = cat.getString(4);
                        contentValues.put("category", cat1);

                    }
                    cat.close();
                    db.insert("Itemwiseorderlistmodifiers", null, contentValues);
                }while (ccursor.moveToNext());
                //}while (cursor.moveToNext());
            }
            ccursor.close();
//            }
                */

//            if (str.equals("Category")) {
            db.execSQL("delete from Itemwiseorderlistcategory");

            Cursor currsor = db.rawQuery("Select DISTINCT * from Itemwiseorderlistitems GROUP BY category ORDER BY category ASC", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (currsor.moveToFirst()){
                do {
                    String catn = currsor.getString(6);
                    ContentValues contentValues = new ContentValues();

                    TextView tv = new TextView(Itemwise_Saleslist.this);
                    tv.setText(catn);
                    if (tv.getText().toString().equals("")){

                    }else {
                        contentValues.put("categoryname", catn);
                        Cursor cursorr11 = db.rawQuery("SELECT SUM(itemtotalquan) FROM Itemwiseorderlistitems WHERE category = '" + catn + "'", null);
                        if (cursorr11.moveToFirst()) {
                            level = cursorr11.getInt(0);
                            String total = String.valueOf(level);
                            contentValues.put("itemtotalquan", total);
                        }
                        cursorr11.close();

                        Cursor cuwrsorr12 = db.rawQuery("SELECT SUM(sales) FROM Itemwiseorderlistitems WHERE category = '" + catn + "'", null);
                        if (cuwrsorr12.moveToFirst()) {
                            level = cuwrsorr12.getInt(0);
                            String totdal = String.valueOf(level);
//                            contentValues.put("sales", totdal);

                            Cursor cursorr12 = db.rawQuery("SELECT SUM(sales) FROM Itemwiseorderlistmodifiers WHERE category = '" + catn + "'", null);
                            if (cursorr12.moveToFirst()) {
                                level = cursorr12.getInt(0);
                                String total = String.valueOf(level);
//                                contentValues.put("sales", total);

                                Cursor cursor13 = db.rawQuery("SELECT SUM(total) FROM Billnumber WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' ", null);
                                if (cursor13.moveToFirst()) {
                                    level = cursor13.getInt(0);
                                    String totalq = String.valueOf(level);

                                    float total1 = Float.parseFloat(totdal) + Float.parseFloat(total);
                                    contentValues.put("sales", String.valueOf(total1));

                                    float a = (total1 / Float.parseFloat(totalq)) * 100;
                                    String rupper1 = String.format("%.1f", a);
                                    contentValues.put("salespercentage", rupper1);
                                }
                                cursor13.close();
                            }
                            cursorr12.close();

//                            Cursor cursor11 = db.rawQuery("SELECT SUM(total) FROM Billnumber WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ", null);
//                            if (cursor11.moveToFirst()) {
//                                level = cursor11.getInt(0);
//                                String totalq = String.valueOf(level);
//
//                                float a = (Float.parseFloat(total) / Float.parseFloat(totalq))*100;
//                                String rupper1 = String.format("%.1f", a);
//                                contentValues.put("salespercentage", rupper1);
//                            }
                        }
                        cuwrsorr12.close();

                        db.insert("Itemwiseorderlistcategory", null, contentValues);
                    }
                }while (currsor.moveToNext());
                currsor.close();
//                }

//                Cursor cursor11 = db.rawQuery("Select DISTINCT * from Itemwiseorderlistmodifiers GROUP BY category ", null);//replace to cursor = dbHelper.fetchAllHotels();
//                if (cursor11.moveToFirst()){
//                    do {
//                        String catn = cursor11.getString(6);
//                        ContentValues contentValues = new ContentValues();
//
//                        contentValues.put("categoryname", catn);
//                        Cursor cursorr11 = db.rawQuery("SELECT SUM(modtotalquan) FROM Itemwiseorderlistmodifiers WHERE category = '"+catn+"'", null);
//                        if (cursorr11.moveToFirst()) {
//                            level = cursorr11.getInt(0);
//                            String total = String.valueOf(level);
//                            contentValues.put("itemtotalquan", total);
//                        }
//
//                        Cursor cursorr12 = db.rawQuery("SELECT SUM(sales) FROM Itemwiseorderlistmodifiers WHERE category = '"+catn+"'", null);
//                        if (cursorr12.moveToFirst()) {
//                            level = cursorr12.getInt(0);
//                            String total = String.valueOf(level);
//                            contentValues.put("sales", total);
//
//                            Cursor cursor13 = db.rawQuery("SELECT SUM(total) FROM Billnumber WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ", null);
//                            if (cursor13.moveToFirst()) {
//                                level = cursor13.getInt(0);
//                                String totalq = String.valueOf(level);
//
//                                float a = (Float.parseFloat(total) / Float.parseFloat(totalq))*100;
//                                String rupper1 = String.format("%.1f", a);
//                                contentValues.put("salespercentage", rupper1);
//                            }
//                        }
//
//                        db.insert("Itemwiseorderlistcategory", null, contentValues);
//                    }while (cursor11.moveToNext());
//                }
            }


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



        // Once Music File is downloaded
        @Override
        protected void onPostExecute(Integer file_url) {
            dialog.dismiss();

            final Cursor cursor_country1 = db1.rawQuery("SELECT * FROM Country_Selection", null);
            if (cursor_country1.moveToFirst()){
                str_country = cursor_country1.getString(1);
            }

            final String Text = getlisting.getSelectedItem().toString();

            //dialog.dismiss();
            if (Text.equals("Items")) {
                item_list.setVisibility(View.VISIBLE);
                modifier_list.setVisibility(View.GONE);
                category_list.setVisibility(View.GONE);
                final String selectQuery = "SELECT * FROM Itemwiseorderlistitems GROUP BY itemname";

                cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                // The desired columns to be bound
                final String[] fromFieldNames = {"_id", "itemname", "itemno", "sales", "salespercentage", "itemtotalquan", "i_buying_price", "i_cost_value", "i_cost_percent", "mi_buying_price", "mi_cost_value", "mi_cost_percent", "mi_cost_percent", "mi_cost_percent", "mi_cost_percent", "mi_cost_percent", "mi_cost_percent", "mi_cost_percent", "barcode_value"};
                // the XML defined views which the data will be bound to
                final int[] toViewsID = {R.id.sno, R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity, R.id.i_buying_price, R.id.i_cost_value, R.id.i_cost_percent, R.id.mi_buying_price, R.id.mi_cost_value, R.id.mi_cost_percent, R.id.inn, R.id.inn1, R.id.inn2, R.id.inn3, R.id.inn4, R.id.inn5, R.id.barcode_value};
                //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                adapter = new SimpleCursorAdapter(Itemwise_Saleslist.this, R.layout.itemwise_boxes_new_items, cursor1, fromFieldNames, toViewsID, 0);
//                listView.setAdapter(adapter);
                adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                        if (view.getId() == R.id.inn || view.getId() == R.id.inn1 || view.getId() == R.id.inn2 || view.getId() == R.id.inn3 || view.getId() == R.id.inn4 || view.getId() == R.id.inn5) {
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
                listView.setAdapter(adapter);

                adapter.setFilterQueryProvider(new FilterQueryProvider() {
                    public Cursor runQuery(CharSequence constraint) {
                        return fetchCountriesByName_items(constraint.toString());
                    }
                });

            }else {

                if (Text.equals("Category")) {
                    item_list.setVisibility(View.GONE);
                    modifier_list.setVisibility(View.GONE);
                    category_list.setVisibility(View.VISIBLE);
                    final String selectQuery = "SELECT * FROM Itemwiseorderlistcategory";

                    cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"_id", "categoryname", "sales", "salespercentage", "itemtotalquan", "itemtotalquan"};
                    // the XML defined views which the data will be bound to
                    final int[] toViewsID = {R.id.sno, R.id.num, R.id.sales, R.id.salesper, R.id.quantity, R.id.inn};
                    //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                    adapter = new SimpleCursorAdapter(Itemwise_Saleslist.this, R.layout.categorywise_boxes, cursor1, fromFieldNames, toViewsID, 0);
//                    listView.setAdapter(adapter);
                    adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                        @Override
                        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                            if (view.getId() == R.id.inn) {
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
                    listView.setAdapter(adapter);

                    adapter.setFilterQueryProvider(new FilterQueryProvider() {
                        public Cursor runQuery(CharSequence constraint) {
                            return fetchCountriesByName_category(constraint.toString());
                        }
                    });
                }

            }

            Cursor cursor2 = db.rawQuery("SELECT SUM(sales) FROM Itemwiseorderlistitems", null);
            if (cursor2.moveToFirst()){
                float a = cursor2.getFloat(0);
                i_mi_totalsales1.setText(String.valueOf(a));
            }
            cursor2.close();

            float a2 = 0;
            Cursor cursor = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE i_buying_price != 'NaN'", null);
            if (cursor.moveToFirst()){
                do {
                    String qty = cursor.getString(5);
                    String pri = cursor.getString(7);

                    float a = Float.parseFloat(qty)*Float.parseFloat(pri);
                    a2 = a2+a;
                    i_cost.setText(String.format("%.2f", a2));

                }while (cursor.moveToNext());
            }
//            Cursor cursor = db.rawQuery("SELECT SUM(i_buying_price) FROM Itemwiseorderlistitems", null);
//            if (cursor.moveToFirst()){
//                float a = cursor.getFloat(0);
//
//                Cursor cursor3 = db.rawQuery("SELECT SUM(itemtotalquan) FROM Itemwiseorderlistitems", null);
//                if (cursor3.moveToFirst()){
//                    float a1 = cursor3.getFloat(0);
//                    float a2 = a1*a;
//                    i_cost.setText(String.valueOf(a2));
//                }
//            }
            cursor.close();

            float a3 = 0;
            Cursor cursor1 = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE mi_buying_price != 'NaN'", null);
            if (cursor1.moveToFirst()){
                do {
                    String qty = cursor1.getString(5);
                    String pri = cursor1.getString(8);

                    float a = Float.parseFloat(qty)*Float.parseFloat(pri);
                    a3 = a3+a;
                    mi_cost.setText(String.format("%.2f", a3));

                }while (cursor1.moveToNext());
            }

//            Cursor cursor1 = db.rawQuery("SELECT SUM(mi_buying_price) FROM Itemwiseorderlistitems", null);
//            if (cursor1.moveToFirst()){
//                float a = cursor1.getFloat(0);
//                mi_cost.setText(String.valueOf(a));
//            }
            cursor1.close();

            float qw = ((Float.parseFloat(i_cost.getText().toString()) + Float.parseFloat(mi_cost.getText().toString())) * 100 ) / Float.parseFloat(i_mi_totalsales1.getText().toString());
            i_cost_percent.setText(String.format("%.1f", qw));
        }
    }

    public Cursor fetchCountriesByName_items_cat(String inputtext) throws SQLException {
//        SELECT * FROM Generalorderlistascdesc1 WHERE paymentmethod = '  Card' GROUP By billno ORDER BY datetimee DESC
        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
            mCursor = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE category = '"+catt+"' GROUP BY itemname", null);
//            mCursor = db.rawQuery("SELECT * FROM Generalorderlistascdesc1 WHERE billtype = '  Dine-in' GROUP By billno ORDER BY datetimee DESC", null);
//            mCursor = db.rawQuery("SELECT * FROM Generalorderlistascdesc1 GROUP By billno ORDER BY datetimee DESC", null);
//            mCursor = db.query("Generalorderlistascdesc1", new String[] {"_id", "date", "time", "user", "billcount", "billno", "sales"},
//                    null, null, null, null, null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE itemname LIKE '%" + inputtext + "%' AND category = '"+catt+"'", null);
//            mCursor = db.rawQuery("SELECT * FROM Generalorderlistascdesc1 WHERE billno LIKE '%" + inputtext + "%' AND billtype = '  Dine-in' GROUP By billno ORDER BY datetimee DESC", null);
//            mCursor = db.rawQuery("SELECT * FROM Generalorderlistascdesc1 WHERE billno LIKE '%" + inputtext + "%' GROUP BY billno ORDER BY datetimee DESC", null);
//            mCursor = db.query(true, "Generalorderlistascdesc1", new String[] {"date", "time", "user", "billcount", "billno", "sales"},
//                    "billno" + " like" + " '%" + inputtext + "%'", null,
//                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }


    public Cursor fetchCountriesByName_items(String inputtext) throws SQLException {
//        SELECT * FROM Generalorderlistascdesc1 WHERE paymentmethod = '  Card' GROUP By billno ORDER BY datetimee DESC
        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
            mCursor = db.rawQuery("SELECT * FROM Itemwiseorderlistitems GROUP BY itemname", null);
//            mCursor = db.rawQuery("SELECT * FROM Generalorderlistascdesc1 WHERE billtype = '  Dine-in' GROUP By billno ORDER BY datetimee DESC", null);
//            mCursor = db.rawQuery("SELECT * FROM Generalorderlistascdesc1 GROUP By billno ORDER BY datetimee DESC", null);
//            mCursor = db.query("Generalorderlistascdesc1", new String[] {"_id", "date", "time", "user", "billcount", "billno", "sales"},
//                    null, null, null, null, null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE itemname LIKE '%" + inputtext + "%' OR barcode_value LIKE '%" + inputtext + "%' GROUP BY itemname", null);
//            mCursor = db.rawQuery("SELECT * FROM Generalorderlistascdesc1 WHERE billno LIKE '%" + inputtext + "%' AND billtype = '  Dine-in' GROUP By billno ORDER BY datetimee DESC", null);
//            mCursor = db.rawQuery("SELECT * FROM Generalorderlistascdesc1 WHERE billno LIKE '%" + inputtext + "%' GROUP BY billno ORDER BY datetimee DESC", null);
//            mCursor = db.query(true, "Generalorderlistascdesc1", new String[] {"date", "time", "user", "billcount", "billno", "sales"},
//                    "billno" + " like" + " '%" + inputtext + "%'", null,
//                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    public Cursor fetchCountriesByName_category(String inputtext) throws SQLException {
//        SELECT * FROM Generalorderlistascdesc1 WHERE paymentmethod = '  Card' GROUP By billno ORDER BY datetimee DESC
        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
            mCursor = db.rawQuery("SELECT * FROM Itemwiseorderlistcategory GROUP BY categoryname", null);
//            mCursor = db.rawQuery("SELECT * FROM Generalorderlistascdesc1 WHERE billtype = '  Dine-in' GROUP By billno ORDER BY datetimee DESC", null);
//            mCursor = db.rawQuery("SELECT * FROM Generalorderlistascdesc1 GROUP By billno ORDER BY datetimee DESC", null);
//            mCursor = db.query("Generalorderlistascdesc1", new String[] {"_id", "date", "time", "user", "billcount", "billno", "sales"},
//                    null, null, null, null, null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Itemwiseorderlistcategory WHERE categoryname LIKE '%" + inputtext + "%' GROUP BY categoryname", null);
//            mCursor = db.rawQuery("SELECT * FROM Generalorderlistascdesc1 WHERE billno LIKE '%" + inputtext + "%' AND billtype = '  Dine-in' GROUP By billno ORDER BY datetimee DESC", null);
//            mCursor = db.rawQuery("SELECT * FROM Generalorderlistascdesc1 WHERE billno LIKE '%" + inputtext + "%' GROUP BY billno ORDER BY datetimee DESC", null);
//            mCursor = db.query(true, "Generalorderlistascdesc1", new String[] {"date", "time", "user", "billcount", "billno", "sales"},
//                    "billno" + " like" + " '%" + inputtext + "%'", null,
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

            String pone = "", ptwo = "", pthree = "";

            TextView topone = new TextView(Itemwise_Saleslist.this);
            TextView topitem1percent = new TextView(Itemwise_Saleslist.this);
            TextView toptwo = new TextView(Itemwise_Saleslist.this);
            TextView topitem2percent = new TextView(Itemwise_Saleslist.this);
            TextView topthree = new TextView(Itemwise_Saleslist.this);
            TextView topitem3percent = new TextView(Itemwise_Saleslist.this);
            TextView totalsalesitemwise = new TextView(Itemwise_Saleslist.this);


            Cursor one = db.rawQuery("SELECT MAX(sales) FROM Itemwiseorderlistitems ", null);
            if (one.moveToFirst()) {
                int one11 = one.getInt(0);
                one = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE sales = '" + one11 + "' ", null);
                if (one.moveToFirst()) {
                    one1 = one.getString(2);
                    //salesee1 = one.getString(3);
                    pone = one.getString(4);
                    topone.setText(one1);
                    topitem1percent.setText(pone);
                    topitem1percent.append("%");
//                                    one = db.rawQuery("SELECT MAX(sales) FROM Itemwiseorderlistitems WHERE MAX(sales)<'"+one11+"' ", null);
//                                    if (one.moveToFirst()){
//                                        two2 = one.getString(2);
//                                        toptwo.setText(two2);
//                                    }
                    Cursor two = db.rawQuery("SELECT MAX(sales) FROM Itemwiseorderlistitems WHERE sales < '" + one11 + "' ", null);
                    if (two.moveToFirst()) {
                        int two111 = two.getInt(0);
                        two = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE sales = '" + two111 + "' ", null);
                        if (two.moveToFirst()) {
                            two2 = two.getString(2);
                            //salesee2 = one.getString(3);
                            ptwo = two.getString(4);
                            toptwo.setText(two2);
                            topitem2percent.setText(ptwo);
                            topitem2percent.append("%");

                            Cursor three = db.rawQuery("SELECT MAX(sales) FROM Itemwiseorderlistitems WHERE sales < '" + two111 + "' ", null);
                            if (three.moveToFirst()) {
                                int three11 = three.getInt(0);
                                three = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE sales = '" + three11 + "' ", null);
                                if (three.moveToFirst()) {
                                    three3 = three.getString(2);
                                    //salesee3 = one.getString(3);
                                    pthree = three.getString(4);
                                    topthree.setText(three3);
                                    topitem3percent.setText(pthree);
                                    topitem3percent.append("%");
                                } else {
                                    topthree.setText("NA");
                                    topitem3percent.setText("0%");
                                }
                            }
                        } else {
                            toptwo.setText("NA");
                            topitem2percent.setText("0%");
                        }
                    }
                } else {
                    topone.setText("NA");
                    topitem1percent.setText("0%");
                }
            }
            one.close();


            Cursor top1item = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE itemname = '" + topone.getText().toString() + "' AND salespercentage = '" + pone + "' ", null);
            if (top1item.moveToFirst()) {
                salesee1 = top1item.getString(3);
            }
            top1item.close();
            Cursor top2item = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE itemname = '" + toptwo.getText().toString() + "' AND salespercentage = '" + ptwo + "' ", null);
            if (top2item.moveToFirst()) {
                salesee2 = top2item.getString(3);
            }
            top2item.close();
            Cursor top3item = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE itemname = '" + topthree.getText().toString() + "' AND salespercentage = '" + pthree + "' ", null);
            if (top3item.moveToFirst()) {
                salesee3 = top3item.getString(3);
            }
            top3item.close();


            Cursor cursor11 = db.rawQuery("SELECT SUM(total) FROM Billnumber WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' ", null);
            if (cursor11.moveToFirst()) {
                level = cursor11.getInt(0);
                total = String.valueOf(level);
            }
            cursor11.close();
//                Toast.makeText(Itemwise_Saleslist.this, Text+" total is "+total, Toast.LENGTH_SHORT).show();
            totalsalesitemwise.setText(insert1_cc);
            totalsalesitemwise.append(total);

            String url = "www.intuitionsoftwares.com";

//            String msg = "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
//                    "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
//                    "Powered by: " + Uri.parse(url);

            String msg = "Product report (" + editText11.getText().toString() + " - " + editText22.getText().toString() + ")\n\n" + "Overview" + " (Detailed report attached)" +
                    "\n\nTotal sales: " + totalsalesitemwise.getText().toString() + "\n\nTop selling products:\n\n" +
                    "1." + topone.getText().toString() + " - "+insert1_rs+" " + salesee1 + "(" + topitem1percent.getText().toString() + ")\n" +
                    "2." + toptwo.getText().toString() + " - "+insert1_rs+" " + salesee2 + "(" + topitem2percent.getText().toString() + ")\n" +
                    "3." + topthree.getText().toString() + " - "+insert1_rs+" " + salesee3 + "(" + topitem3percent.getText().toString() + ")\n\n" +
                    "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
                    "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
                    "Powered by: " + Uri.parse(url);

            Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
            if (cursor1.moveToFirst()) {
                do {
                    String unn = cursor1.getString(3);
                    TextView edtToAddress = new TextView(Itemwise_Saleslist.this);
                    edtToAddress.setText(unn);

                    TextView edtSubject = new TextView(Itemwise_Saleslist.this);
                    edtSubject.setText(strcompanyname);

                    TextView edtMessage = new TextView(Itemwise_Saleslist.this);
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
//                        String filename = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_reports/IVEPOS_product_report/IvePOS_product_report"+currentDateandTimee1+"_"+timee1+".csv";
                        String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/IVEPOS_reports/IVEPOS_product_report/IvePOS_product_report"+currentDateandTimee1+"_"+timee1+".csv";
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
            cursor1.close();
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
                Toast.makeText(Itemwise_Saleslist.this, "not success", Toast.LENGTH_SHORT).show();
//                showMessage(view, "No results returned.");
            } else {
                Toast.makeText(Itemwise_Saleslist.this, "success", Toast.LENGTH_SHORT).show();
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
                Itemwise_Saleslist.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private void getResultsFromApi() {

        Cursor cursorr = db1.rawQuery("SELECT * FROM Email_setup", null);
        if (cursorr.moveToFirst()) {
            String unn = cursorr.getString(1);
//            Toast.makeText(Itemwise_Saleslist.this, "a4 " + unn, Toast.LENGTH_SHORT).show();

            TextView tvv = new TextView(Itemwise_Saleslist.this);
            tvv.setText(unn);

            if (tvv.getText().toString().equals("")) {

            }else {
                mCredential.setSelectedAccountName(tvv.getText().toString());
            }
        }

        if (! isGooglePlayServicesAvailable()) {
//            Toast.makeText(Itemwise_Saleslist.this, "1", Toast.LENGTH_SHORT).show();
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
//            Toast.makeText(Itemwise_Saleslist.this, "2", Toast.LENGTH_SHORT).show();
//            chooseAccount();
        } else if (! isDeviceOnline()) {
//            Toast.makeText(Itemwise_Saleslist.this, "3", Toast.LENGTH_SHORT).show();
//            mOutputText.setText("No network connection available.");
        } else {
//            Toast.makeText(Itemwise_Saleslist.this, "4", Toast.LENGTH_SHORT).show();
            new MakeRequestTask1(mCredential).execute();
        }
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) Itemwise_Saleslist.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
                apiAvailability.isGooglePlayServicesAvailable(Itemwise_Saleslist.this);
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
                apiAvailability.isGooglePlayServicesAvailable(Itemwise_Saleslist.this);
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
//        Toast.makeText(Itemwise_Saleslist.this, "s1", Toast.LENGTH_SHORT).show();
        if (EasyPermissions.hasPermissions(
                Itemwise_Saleslist.this, android.Manifest.permission.GET_ACCOUNTS)) {
            String accountName = Itemwise_Saleslist.this.getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
//            Toast.makeText(Itemwise_Saleslist.this, "s2", Toast.LENGTH_SHORT).show();
//            if (accountName != null) {
//                mCredential.setSelectedAccountName(accountName);
//                Toast.makeText(Itemwise_Saleslist.this, "s3", Toast.LENGTH_SHORT).show();
//                getResultsFromApi();
//            } else {
            // Start a dialog from which the user can choose an account
            startActivityForResult(
                    mCredential.newChooseAccountIntent(),
                    REQUEST_ACCOUNT_PICKER);
//            Toast.makeText(Itemwise_Saleslist.this, "s4", Toast.LENGTH_SHORT).show();
//            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
//            Toast.makeText(Itemwise_Saleslist.this, "s5", Toast.LENGTH_SHORT).show();
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
        connnet.close();

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
        connusb.close();

        //Toast.makeText(Itemwise_Saleslist.this, "printbillonly one ", Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(Itemwise_Saleslist.this, "phi", Toast.LENGTH_SHORT).show();
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
                        HT1 = new byte[]{0x09};
                        str_line = "--------------------------------";
                        allbufline = new byte[][]{
                                left, un1, "--------------------------------".getBytes(), LF

                        };
                    }else {
                        if (str_print_ty.toString().equals("Epson/others")) {
//                        Toast.makeText(Itemwise_Saleslist.this, "epson", Toast.LENGTH_SHORT).show();
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
        TextView qazcvb = new TextView(Itemwise_Saleslist.this);
        Cursor cvonnusb = db1.rawQuery("SELECT * FROM BTConn", null);
        if (cvonnusb.moveToFirst()) {
            addgets = cvonnusb.getString(1);
            namegets = cvonnusb.getString(2);
            statussusbs = cvonnusb.getString(3);
            dd = cvonnusb.getString(4);
        }
        qazcvb.setText(dd);
        if (qazcvb.getText().toString().equals("usb") && statussusbs.toString().equals("ok")) {
            runPrintCouponSequence_minireceipt_items();
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
            getcom.close();


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
                        wifiSocket2.WIFI_Write(normal);	//
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);	//
                        wifiSocket2.WIFI_Write(straddress1);
                        wifiSocket2.WIFI_Write(LF);	//
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(normal);	//
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);	//
                            wifiSocket.WIFI_Write(straddress1);
                            wifiSocket.WIFI_Write(LF);	//
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
                        wifiSocket2.WIFI_Write(normal);	//
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);	//
                        wifiSocket2.WIFI_Write(straddress2);
                        wifiSocket2.WIFI_Write(LF);	//
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(normal);	//
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);	//
                            wifiSocket.WIFI_Write(straddress2);
                            wifiSocket.WIFI_Write(LF);	//
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
                        wifiSocket2.WIFI_Write(normal);	//
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);	//
                        wifiSocket2.WIFI_Write(straddress3);
                        wifiSocket2.WIFI_Write(LF);	//
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(normal);	//
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);	//
                            wifiSocket.WIFI_Write(straddress3);
                            wifiSocket.WIFI_Write(LF);	//
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
                        wifiSocket2.WIFI_Write(normal);	//
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);	//
                        wifiSocket2.WIFI_Write(pp);
                        wifiSocket2.WIFI_Write(LF);	//
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(normal);	//
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);	//
                            wifiSocket.WIFI_Write(pp);
                            wifiSocket.WIFI_Write(LF);	//
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
                        wifiSocket2.WIFI_Write(normal);	//
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);	//
                        wifiSocket2.WIFI_Write(stremailid);
                        wifiSocket2.WIFI_Write(LF);	//
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(normal);	//
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);	//
                            wifiSocket.WIFI_Write(stremailid);
                            wifiSocket.WIFI_Write(LF);	//
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
                        wifiSocket2.WIFI_Write(normal);	//
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);	//
                        wifiSocket2.WIFI_Write(strwebsite);
                        wifiSocket2.WIFI_Write(LF);	//
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(normal);	//
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);	//
                            wifiSocket.WIFI_Write(strwebsite);
                            wifiSocket.WIFI_Write(LF);	//
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
                        wifiSocket2.WIFI_Write(normal);	//
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);	//
                        wifiSocket2.WIFI_Write(strtaxone);
                        wifiSocket2.WIFI_Write(LF);	//
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(normal);	//
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);	//
                            wifiSocket.WIFI_Write(strtaxone);
                            wifiSocket.WIFI_Write(LF);	//
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
                level = cursor11.getInt(0);
                total = String.valueOf(level);
            }
            cursor11.close();

            Cursor count = db.rawQuery("SELECT COUNT(total) FROM Billnumber WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' ", null);
            if (count.moveToFirst()) {
                level = count.getInt(0);
                total1 = String.valueOf(level);
            }
            count.close();

            String avgbill = String.valueOf(Integer.parseInt(total) / Integer.parseInt(total1));


            noofbills = new TextView(Itemwise_Saleslist.this);


            Cursor countc = db.rawQuery("SELECT COUNT(total) FROM Billnumber WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' ", null);
            if (countc.moveToFirst()) {
                level = countc.getInt(0);
                String total1 = String.valueOf(level);
                noofbills.setText(total1);
            }
            countc.close();


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


            allbuftaxestype1 = new byte[][]{
                    left, normal, un1, "Itemname".getBytes(), " x ".getBytes(), "qty".getBytes(), HT, LF
            };

            if (statussusbs.toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(normal);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BT_Write("Itemname");
                BT_Write(" x ");
                BT_Write("qty");
                BluetoothPrintDriver.BT_Write(HT);    //
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(left);    //
                    wifiSocket2.WIFI_Write(normal);    //
                    wifiSocket2.WIFI_Write(un1);    //
                    wifiSocket2.WIFI_Write("Itemname");
                    wifiSocket2.WIFI_Write(" x ");
                    wifiSocket2.WIFI_Write("qty");
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write(LF);    //
                }else {
                    if (statusnets.toString().equals("ok")) {
                        wifiSocket.WIFI_Write(left);    //
                        wifiSocket.WIFI_Write(normal);    //
                        wifiSocket.WIFI_Write(un1);    //
                        wifiSocket.WIFI_Write("Itemname");
                        wifiSocket.WIFI_Write(" x ");
                        wifiSocket.WIFI_Write("qty");
                        wifiSocket.WIFI_Write(HT);    //
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }

            allbufqty = new byte[][]{
                    setHT33, normal, "price".getBytes(), HT, "total".getBytes(), HT, "%".getBytes(), LF
            };

            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

            if (statussusbs.toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHT33);    //
                BluetoothPrintDriver.BT_Write(normal);    //
                BT_Write("price");
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write("total");
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write("%");
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(setHT33);    //
                    wifiSocket2.WIFI_Write(normal);    //
                    wifiSocket2.WIFI_Write("price");
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write("total");
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write("%");
                    wifiSocket2.WIFI_Write(LF);    //
                }else {
                    if (statusnets.toString().equals("ok")) {
                        wifiSocket.WIFI_Write(setHT33);    //
                        wifiSocket.WIFI_Write(normal);    //
                        wifiSocket.WIFI_Write("price");
                        wifiSocket.WIFI_Write(HT);    //
                        wifiSocket.WIFI_Write("total");
                        wifiSocket.WIFI_Write(HT);    //
                        wifiSocket.WIFI_Write("%");
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

            Cursor cursor = db.rawQuery("SELECT * FROM Itemwiseorderlistitems GROUP BY category ORDER BY sales DESC", null);
            if (cursor.moveToFirst()) {
                do {
                    String category = cursor.getString(6);

                    String c_p = "";
                    Cursor cursor3 = db.rawQuery("SELECT * FROM  Itemwiseorderlistcategory WHERE categoryname = '" + category + "'", null);
                    if (cursor3.moveToFirst()) {
                        c_p = cursor3.getString(3);
                    }
                    cursor3.close();


                    allbuftaxestype1 = new byte[][]{
                            setHT321, left, un, category.getBytes(), un1, HT, "  ".getBytes(), "(Rs.".getBytes(), c_p.getBytes(), ")".getBytes(), LF
                    };

                    if (statussusbs.toString().equals("ok")) {
                        BluetoothPrintDriver.BT_Write(setHT321);    //
                        BluetoothPrintDriver.BT_Write(left);    //
                        BluetoothPrintDriver.BT_Write(un);    //
                        BT_Write(category);
                        BluetoothPrintDriver.BT_Write(un1);    //
                        BluetoothPrintDriver.BT_Write(HT);    //
                        BT_Write("  ");
                        BT_Write("("+insert1_rs + c_p + ")");
                        BluetoothPrintDriver.BT_Write(LF);    //
                    } else {
                        if (statusnets_counter.toString().equals("ok")) {
                            wifiSocket2.WIFI_Write(setHT321);    //
                            wifiSocket2.WIFI_Write(left);    //
                            wifiSocket2.WIFI_Write(un);    //
                            wifiSocket2.WIFI_Write(category);
                            wifiSocket2.WIFI_Write(un1);    //
                            wifiSocket2.WIFI_Write(HT);    //
                            wifiSocket2.WIFI_Write("  ");
                            wifiSocket2.WIFI_Write("("+insert1_rs + c_p + ")");
                            wifiSocket2.WIFI_Write(LF);    //
                        }else {
                            if (statusnets.toString().equals("ok")) {
                                wifiSocket.WIFI_Write(setHT321);    //
                                wifiSocket.WIFI_Write(left);    //
                                wifiSocket.WIFI_Write(un);    //
                                wifiSocket.WIFI_Write(category);
                                wifiSocket.WIFI_Write(un1);    //
                                wifiSocket.WIFI_Write(HT);    //
                                wifiSocket.WIFI_Write("  ");
                                wifiSocket.WIFI_Write("("+insert1_rs + c_p + ")");
                                wifiSocket.WIFI_Write(LF);    //
                            }
                        }
                    }

                    Cursor cursor2 = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE category = '" + category + "'", null);
                    if (cursor2.moveToFirst()) {
                        do {
                            String itemnam = cursor2.getString(2);
                            String qty = cursor2.getString(5);
                            String indiv_price = cursor2.getString(1);
                            String sales = cursor2.getString(3);
                            String sales_p = cursor2.getString(4);

                            allbuftaxestype1 = new byte[][]{
                                    left, normal, un1, itemnam.getBytes(), " x ".getBytes(), qty.getBytes(), HT, LF
                            };

                            if (statussusbs.toString().equals("ok")) {
                                BluetoothPrintDriver.BT_Write(left);    //
                                BluetoothPrintDriver.BT_Write(normal);    //
                                BluetoothPrintDriver.BT_Write(un1);    //
                                BT_Write(itemnam);
                                BT_Write(" x ");
                                BT_Write(qty);
                                BluetoothPrintDriver.BT_Write(HT);    //
                                BluetoothPrintDriver.BT_Write(LF);    //
                            } else {
                                if (statusnets_counter.toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(left);    //
                                    wifiSocket2.WIFI_Write(normal);    //
                                    wifiSocket2.WIFI_Write(un1);    //
                                    wifiSocket2.WIFI_Write(itemnam);
                                    wifiSocket2.WIFI_Write(" x ");
                                    wifiSocket2.WIFI_Write(qty);
                                    wifiSocket2.WIFI_Write(HT);    //
                                    wifiSocket2.WIFI_Write(LF);    //
                                }else {
                                    if (statusnets.toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(left);    //
                                        wifiSocket.WIFI_Write(normal);    //
                                        wifiSocket.WIFI_Write(un1);    //
                                        wifiSocket.WIFI_Write(itemnam);
                                        wifiSocket.WIFI_Write(" x ");
                                        wifiSocket.WIFI_Write(qty);
                                        wifiSocket.WIFI_Write(HT);    //
                                        wifiSocket.WIFI_Write(LF);    //
                                    }
                                }
                            }


                            allbufqty = new byte[][]{
                                    setHT33, normal, "Rs.".getBytes(), indiv_price.getBytes(), HT, "Rs.".getBytes(), sales.getBytes(), HT, sales_p.getBytes(), "%".getBytes(), LF
                            };

                            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                            if (statussusbs.toString().equals("ok")) {
                                BluetoothPrintDriver.BT_Write(setHT33);    //
                                BluetoothPrintDriver.BT_Write(normal);    //
                                BT_Write(insert1_rs+"" + indiv_price);
                                BluetoothPrintDriver.BT_Write(HT);    //
                                BT_Write(insert1_rs+"" + sales);
                                BluetoothPrintDriver.BT_Write(HT);    //
                                BT_Write(sales_p + "%");
                                BluetoothPrintDriver.BT_Write(LF);    //
                            } else {
                                if (statusnets_counter.toString().equals("ok")) {
                                    wifiSocket2.WIFI_Write(setHT33);    //
                                    wifiSocket2.WIFI_Write(normal);    //
                                    wifiSocket2.WIFI_Write(insert1_rs+"" + indiv_price);
                                    wifiSocket2.WIFI_Write(HT);    //
                                    wifiSocket2.WIFI_Write(insert1_rs+"" + sales);
                                    wifiSocket2.WIFI_Write(HT);    //
                                    wifiSocket2.WIFI_Write(sales_p + "%");
                                    wifiSocket2.WIFI_Write(LF);    //
                                }else {
                                    if (statusnets.toString().equals("ok")) {
                                        wifiSocket.WIFI_Write(setHT33);    //
                                        wifiSocket.WIFI_Write(normal);    //
                                        wifiSocket.WIFI_Write(insert1_rs+"" + indiv_price);
                                        wifiSocket.WIFI_Write(HT);    //
                                        wifiSocket.WIFI_Write(insert1_rs+"" + sales);
                                        wifiSocket.WIFI_Write(HT);    //
                                        wifiSocket.WIFI_Write(sales_p + "%");
                                        wifiSocket.WIFI_Write(LF);    //
                                    }
                                }
                            }


                        } while (cursor2.moveToNext());
                    }
                    cursor2.close();


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


                    String cou = "";

                    Cursor cursor5 = db.rawQuery("SELECT COUNT(itemtotalquan) FROM Itemwiseorderlistitems WHERE category = '" + category + "'", null);
                    if (cursor5.moveToFirst()) {
                        int level = cursor5.getInt(0);
                        cou = String.valueOf(level);
                    }
                    cursor5.close();


                    Cursor cursor4 = db.rawQuery("SELECT SUM(itemtotalquan) FROM Itemwiseorderlistitems WHERE category = '" + category + "'", null);
                    if (cursor4.moveToFirst()) {
                        float level = cursor4.getInt(0);
                        String total = String.valueOf(level);

                        allbuftaxestype1 = new byte[][]{
                                setHT321, left, "no. of items/qty".getBytes(), HT, "  ".getBytes(), cou.getBytes(), "/".getBytes(), total.getBytes(), LF
                        };

                        if (statussusbs.toString().equals("ok")) {
                            BluetoothPrintDriver.BT_Write(setHT321);    //
                            BluetoothPrintDriver.BT_Write(left);    //
                            BT_Write("no. of items/qty");
                            BluetoothPrintDriver.BT_Write(HT);    //
                            BT_Write("  ");
                            BT_Write(cou);
                            BT_Write("/");
                            BT_Write(total);
                            BluetoothPrintDriver.BT_Write(LF);    //
                        } else {
                            if (statusnets_counter.toString().equals("ok")) {
                                wifiSocket2.WIFI_Write(setHT321);    //
                                wifiSocket2.WIFI_Write(left);    //
                                wifiSocket2.WIFI_Write("no. of items/qty");
                                wifiSocket2.WIFI_Write(HT);    //
                                wifiSocket2.WIFI_Write("  ");
                                wifiSocket2.WIFI_Write(cou);
                                wifiSocket2.WIFI_Write("/");
                                wifiSocket2.WIFI_Write(total);
                                wifiSocket2.WIFI_Write(LF);    //
                            }else {
                                if (statusnets.toString().equals("ok")) {
                                    wifiSocket.WIFI_Write(setHT321);    //
                                    wifiSocket.WIFI_Write(left);    //
                                    wifiSocket.WIFI_Write("no. of items/qty");
                                    wifiSocket.WIFI_Write(HT);    //
                                    wifiSocket.WIFI_Write("  ");
                                    wifiSocket.WIFI_Write(cou);
                                    wifiSocket.WIFI_Write("/");
                                    wifiSocket.WIFI_Write(total);
                                    wifiSocket.WIFI_Write(LF);    //
                                }
                            }
                        }

                    }
                    cursor4.close();

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
        cc.close();

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

    public void printbillcopy_minireceipt_category(){
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

        //Toast.makeText(Itemwise_Saleslist.this, "printbillonly one ", Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(Itemwise_Saleslist.this, "phi", Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(Itemwise_Saleslist.this, "epson", Toast.LENGTH_SHORT).show();
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
        TextView qazcvb = new TextView(Itemwise_Saleslist.this);
        Cursor cvonnusb = db1.rawQuery("SELECT * FROM BTConn", null);
        if (cvonnusb.moveToFirst()) {
            addgets = cvonnusb.getString(1);
            namegets = cvonnusb.getString(2);
            statussusbs = cvonnusb.getString(3);
            dd = cvonnusb.getString(4);
        }
        qazcvb.setText(dd);
        if (qazcvb.getText().toString().equals("usb") && statussusbs.toString().equals("ok")) {
            runPrintCouponSequence_minireceipt_category();
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
                        wifiSocket2.WIFI_Write(normal);	//
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);	//
                        wifiSocket2.WIFI_Write(straddress1);
                        wifiSocket2.WIFI_Write(LF);	//
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(normal);	//
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);	//
                            wifiSocket.WIFI_Write(straddress1);
                            wifiSocket.WIFI_Write(LF);	//
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
                        wifiSocket2.WIFI_Write(normal);	//
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);	//
                        wifiSocket2.WIFI_Write(straddress2);
                        wifiSocket2.WIFI_Write(LF);	//
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(normal);	//
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);	//
                            wifiSocket.WIFI_Write(straddress2);
                            wifiSocket.WIFI_Write(LF);	//
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
                        wifiSocket2.WIFI_Write(normal);	//
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);	//
                        wifiSocket2.WIFI_Write(straddress3);
                        wifiSocket2.WIFI_Write(LF);	//
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(normal);	//
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);	//
                            wifiSocket.WIFI_Write(straddress3);
                            wifiSocket.WIFI_Write(LF);	//
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
                        wifiSocket2.WIFI_Write(normal);	//
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);	//
                        wifiSocket2.WIFI_Write(pp);
                        wifiSocket2.WIFI_Write(LF);	//
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(normal);	//
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);	//
                            wifiSocket.WIFI_Write(pp);
                            wifiSocket.WIFI_Write(LF);	//
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
                        wifiSocket2.WIFI_Write(normal);	//
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);	//
                        wifiSocket2.WIFI_Write(stremailid);
                        wifiSocket2.WIFI_Write(LF);	//
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(normal);	//
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);	//
                            wifiSocket.WIFI_Write(stremailid);
                            wifiSocket.WIFI_Write(LF);	//
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
                        wifiSocket2.WIFI_Write(normal);	//
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);	//
                        wifiSocket2.WIFI_Write(strwebsite);
                        wifiSocket2.WIFI_Write(LF);	//
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(normal);	//
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);	//
                            wifiSocket.WIFI_Write(strwebsite);
                            wifiSocket.WIFI_Write(LF);	//
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
                        wifiSocket2.WIFI_Write(normal);	//
                        wifiSocket2.WIFI_Write(un1);    //
                        wifiSocket2.WIFI_Write(cen);	//
                        wifiSocket2.WIFI_Write(strtaxone);
                        wifiSocket2.WIFI_Write(LF);	//
                    }else {
                        if (statusnets.toString().equals("ok")) {
                            wifiSocket.WIFI_Write(normal);	//
                            wifiSocket.WIFI_Write(un1);    //
                            wifiSocket.WIFI_Write(cen);	//
                            wifiSocket.WIFI_Write(strtaxone);
                            wifiSocket.WIFI_Write(LF);	//
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
                level = cursor11.getInt(0);
                total = String.valueOf(level);
            }

            Cursor count = db.rawQuery("SELECT COUNT(total) FROM Billnumber WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' ", null);
            if (count.moveToFirst()) {
                level = count.getInt(0);
                total1 = String.valueOf(level);
            }

            String avgbill = String.valueOf(Integer.parseInt(total) / Integer.parseInt(total1));


            noofbills = new TextView(Itemwise_Saleslist.this);


            Cursor countc = db.rawQuery("SELECT COUNT(total) FROM Billnumber WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' ", null);
            if (countc.moveToFirst()) {
                level = countc.getInt(0);
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


            allbuftax = new byte[][]{
                    setHT321, left, "Category".getBytes(), HT, "  ".getBytes(), "Total".getBytes(), LF
            };

            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

            if (statussusbs.toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHT321);    //
                BluetoothPrintDriver.BT_Write(left);    //
                BT_Write("Category");
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write("  ");
                BT_Write("Total");
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(setHT321);    //
                    wifiSocket2.WIFI_Write(left);    //
                    wifiSocket2.WIFI_Write("Category");
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write("  ");
                    wifiSocket2.WIFI_Write("Total");
                    wifiSocket2.WIFI_Write(LF);    //
                }else {
                    if (statusnets.toString().equals("ok")) {
                        wifiSocket.WIFI_Write(setHT321);    //
                        wifiSocket.WIFI_Write(left);    //
                        wifiSocket.WIFI_Write("Category");
                        wifiSocket.WIFI_Write(HT);    //
                        wifiSocket.WIFI_Write("  ");
                        wifiSocket.WIFI_Write("Total");
                        wifiSocket.WIFI_Write(LF);    //
                    }
                }
            }

            allbuftax = new byte[][]{
                    setHT321, left, "Qty".getBytes(), HT, "  ".getBytes(), "%".getBytes(), LF
            };

            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

            if (statussusbs.toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHT321);    //
                BluetoothPrintDriver.BT_Write(left);    //
                BT_Write("Qty");
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write("  ");
                BT_Write("%");
                BluetoothPrintDriver.BT_Write(LF);    //
            } else {
                if (statusnets_counter.toString().equals("ok")) {
                    wifiSocket2.WIFI_Write(setHT321);    //
                    wifiSocket2.WIFI_Write(left);    //
                    wifiSocket2.WIFI_Write("Qty");
                    wifiSocket2.WIFI_Write(HT);    //
                    wifiSocket2.WIFI_Write("  ");
                    wifiSocket2.WIFI_Write("%");
                    wifiSocket2.WIFI_Write(LF);    //
                }else {
                    if (statusnets.toString().equals("ok")) {
                        wifiSocket.WIFI_Write(setHT321);    //
                        wifiSocket.WIFI_Write(left);    //
                        wifiSocket.WIFI_Write("Qty");
                        wifiSocket.WIFI_Write(HT);    //
                        wifiSocket.WIFI_Write("  ");
                        wifiSocket.WIFI_Write("%");
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

            Cursor cursor = db.rawQuery("SELECT * FROM Itemwiseorderlistcategory ORDER BY sales DESC", null);
            if (cursor.moveToFirst()) {
                do {
                    String category = cursor.getString(2);
                    String tot = cursor.getString(3);
                    String qty = cursor.getString(5);
                    String pe = cursor.getString(4);

                    allbuftax = new byte[][]{
                            setHT321, left, category.getBytes(), HT, "  ".getBytes(), "Rs.".getBytes(), tot.getBytes(), LF
                    };

                    //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                    if (statussusbs.toString().equals("ok")) {
                        BluetoothPrintDriver.BT_Write(setHT321);    //
                        BluetoothPrintDriver.BT_Write(left);    //
                        BT_Write(category);
                        BluetoothPrintDriver.BT_Write(HT);    //
                        BT_Write("  ");
                        BT_Write(insert1_rs+"" + tot);
                        BluetoothPrintDriver.BT_Write(LF);    //
                    } else {
                        if (statusnets_counter.toString().equals("ok")) {
                            wifiSocket2.WIFI_Write(setHT321);    //
                            wifiSocket2.WIFI_Write(left);    //
                            wifiSocket2.WIFI_Write(category);
                            wifiSocket2.WIFI_Write(HT);    //
                            wifiSocket2.WIFI_Write("  ");
                            wifiSocket2.WIFI_Write(insert1_rs+"" + tot);
                            wifiSocket2.WIFI_Write(LF);    //
                        }else {
                            if (statusnets.toString().equals("ok")) {
                                wifiSocket.WIFI_Write(setHT321);    //
                                wifiSocket.WIFI_Write(left);    //
                                wifiSocket.WIFI_Write(category);
                                wifiSocket.WIFI_Write(HT);    //
                                wifiSocket.WIFI_Write("  ");
                                wifiSocket.WIFI_Write(insert1_rs+"" + tot);
                                wifiSocket.WIFI_Write(LF);    //
                            }
                        }
                    }

                    allbuftax = new byte[][]{
                            setHT321, left, qty.getBytes(), HT, "  ".getBytes(), pe.getBytes(), "%".getBytes(), LF
                    };

                    //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                    if (statussusbs.toString().equals("ok")) {
                        BluetoothPrintDriver.BT_Write(setHT321);    //
                        BluetoothPrintDriver.BT_Write(left);    //
                        BT_Write(qty);
                        BluetoothPrintDriver.BT_Write(HT);    //
                        BT_Write("  ");
                        BT_Write(pe + "%");
                        BluetoothPrintDriver.BT_Write(LF);    //
                    } else {
                        if (statusnets_counter.toString().equals("ok")) {
                            wifiSocket2.WIFI_Write(setHT321);    //
                            wifiSocket2.WIFI_Write(left);    //
                            wifiSocket2.WIFI_Write(qty);
                            wifiSocket2.WIFI_Write(HT);    //
                            wifiSocket2.WIFI_Write("  ");
                            wifiSocket2.WIFI_Write(pe + "%");
                            wifiSocket2.WIFI_Write(LF);    //
                        }else {
                            if (statusnets.toString().equals("ok")) {
                                wifiSocket.WIFI_Write(setHT321);    //
                                wifiSocket.WIFI_Write(left);    //
                                wifiSocket.WIFI_Write(qty);
                                wifiSocket.WIFI_Write(HT);    //
                                wifiSocket.WIFI_Write("  ");
                                wifiSocket.WIFI_Write(pe + "%");
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


    public static void donotshowKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cursor1.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor1.close();
    }

    private boolean runPrintCouponSequence_minireceipt_items() {
        if (!initializeObject()) {
            return false;
        }

        if (!createCouponData_minireceipt_items()) {
            finalizeObject();
            return false;
        }

        if (!printData()) {
            finalizeObject();
            return false;
        }

        return true;
    }

    private boolean runPrintCouponSequence_minireceipt_category() {
        if (!initializeObject()) {
            return false;
        }

        if (!createCouponData_minireceipt_category()) {
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
//            Toast.makeText(Itemwise_Saleslist.this, "Here3", Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(Itemwise_Saleslist.this, "Here6", Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(Itemwise_Saleslist.this, "Here7", Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(Itemwise_Saleslist.this, "Here9", Toast.LENGTH_SHORT).show();
                // Do nothing
            }
            return false;
        }

        try {
            mPrinter.sendData(com.epson.epos2.printer.Printer.PARAM_DEFAULT);
        } catch (Exception e) {
//            Toast.makeText(Itemwise_Saleslist.this, "Here10", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, "sendData", mContext);
            try {
                mPrinter.disconnect();
            } catch (Exception ex) {
//                Toast.makeText(Itemwise_Saleslist.this, "Here11", Toast.LENGTH_SHORT).show();
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
//            Toast.makeText(Itemwise_Saleslist.this, "Here4", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, "connect", mContext);
            return false;
        }

        try {
            mPrinter.beginTransaction();
            isBeginTransaction = true;
        } catch (Exception e) {
//            Toast.makeText(Itemwise_Saleslist.this, "Here12", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, "beginTransaction", mContext);
        }

        if (isBeginTransaction == false) {
            try {
                mPrinter.disconnect();
            } catch (Epos2Exception e) {
//                Toast.makeText(Itemwise_Saleslist.this, "Here5", Toast.LENGTH_SHORT).show();
                // Do nothing
                return false;
            }
        }

        return true;
    }

    private boolean createCouponData_minireceipt_items() {

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
                level = cursor11.getInt(0);
                total = String.valueOf(level);
            }
            cursor11.close();

            Cursor count = db.rawQuery("SELECT COUNT(total) FROM Billnumber WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' ", null);
            if (count.moveToFirst()) {
                level = count.getInt(0);
                total1 = String.valueOf(level);
            }
            count.close();

            String avgbill = String.valueOf(Integer.parseInt(total) / Integer.parseInt(total1));


            noofbills = new TextView(Itemwise_Saleslist.this);


            Cursor countc = db.rawQuery("SELECT COUNT(total) FROM Billnumber WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' ", null);
            if (countc.moveToFirst()) {
                level = countc.getInt(0);
                String total1 = String.valueOf(level);
                noofbills.setText(total1);
            }
            countc.close();


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


            allbuftaxestype1 = new byte[][]{
                    left, normal, un1, "Itemname".getBytes(), " x ".getBytes(), "qty".getBytes(), HT, LF
            };

            if (statussusbs.toString().equals("ok")) {
                mPrinter.addCommand(left);
                mPrinter.addCommand(normal);
                mPrinter.addCommand(un1);
                StringBuilder textData1 = new StringBuilder();
                textData1.append("Itemname x qty");
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(HT);
                mPrinter.addCommand(LF); //LF
            }

            allbufqty = new byte[][]{
                    setHT33, normal, "price".getBytes(), HT, "total".getBytes(), HT, "%".getBytes(), LF
            };

            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

            if (statussusbs.toString().equals("ok")) {
                mPrinter.addCommand(setHT33);
                mPrinter.addCommand(normal);
                StringBuilder textData1 = new StringBuilder();
                textData1.append("Price");
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(HT);
                StringBuilder textData2 = new StringBuilder();
                textData2.append("total");
                mPrinter.addText(textData2.toString());
                mPrinter.addCommand(HT);
                StringBuilder textData3 = new StringBuilder();
                textData3.append("%");
                mPrinter.addText(textData3.toString());
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

            Cursor cursor = db.rawQuery("SELECT * FROM Itemwiseorderlistitems GROUP BY category ORDER BY sales DESC", null);
            if (cursor.moveToFirst()) {
                do {
                    String category = cursor.getString(6);

                    String c_p = "";
                    Cursor cursor3 = db.rawQuery("SELECT * FROM  Itemwiseorderlistcategory WHERE categoryname = '" + category + "'", null);
                    if (cursor3.moveToFirst()) {
                        c_p = cursor3.getString(3);
                    }
                    cursor3.close();


                    allbuftaxestype1 = new byte[][]{
                            setHT321, left, un, category.getBytes(), un1, HT, "  ".getBytes(), "(Rs.".getBytes(), c_p.getBytes(), ")".getBytes(), LF
                    };

                    if (statussusbs.toString().equals("ok")) {
                        mPrinter.addCommand(setHT321);
                        mPrinter.addCommand(left);
                        mPrinter.addCommand(un);
                        StringBuilder textData1 = new StringBuilder();
                        textData1.append(category);
                        mPrinter.addText(textData1.toString());
                        mPrinter.addCommand(un1);
                        mPrinter.addCommand(HT);
                        StringBuilder textData2 = new StringBuilder();
                        textData2.append("  "+"("+insert1_rs+c_p+")");
                        mPrinter.addText(textData2.toString());
                        mPrinter.addCommand(LF); //LF
                    }

                    Cursor cursor2 = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE category = '" + category + "'", null);
                    if (cursor2.moveToFirst()) {
                        do {
                            String itemnam = cursor2.getString(2);
                            String qty = cursor2.getString(5);
                            String indiv_price = cursor2.getString(1);
                            String sales = cursor2.getString(3);
                            String sales_p = cursor2.getString(4);

                            allbuftaxestype1 = new byte[][]{
                                    left, normal, un1, itemnam.getBytes(), " x ".getBytes(), qty.getBytes(), HT, LF
                            };

                            if (statussusbs.toString().equals("ok")) {
                                mPrinter.addCommand(left);
                                mPrinter.addCommand(normal);
                                mPrinter.addCommand(un1);
                                StringBuilder textData1 = new StringBuilder();
                                textData1.append(itemnam+" x "+qty);
                                mPrinter.addText(textData1.toString());
                                mPrinter.addCommand(HT);
                                mPrinter.addCommand(LF); //LF
                            }


                            allbufqty = new byte[][]{
                                    setHT33, normal, "Rs.".getBytes(), indiv_price.getBytes(), HT, "Rs.".getBytes(), sales.getBytes(), HT, sales_p.getBytes(), "%".getBytes(), LF
                            };

                            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                            if (statussusbs.toString().equals("ok")) {
                                mPrinter.addCommand(setHT33);
                                mPrinter.addCommand(normal);
                                StringBuilder textData1 = new StringBuilder();
                                textData1.append(insert1_rs+""+indiv_price);
                                mPrinter.addText(textData1.toString());
                                mPrinter.addCommand(HT);
                                StringBuilder textData2 = new StringBuilder();
                                textData2.append(insert1_rs+""+sales);
                                mPrinter.addText(textData2.toString());
                                mPrinter.addCommand(HT);
                                StringBuilder textData3 = new StringBuilder();
                                textData3.append(sales_p+"%");
                                mPrinter.addText(textData3.toString());
                                mPrinter.addCommand(LF); //LF
                            }


                        } while (cursor2.moveToNext());
                    }
                    cursor2.close();


                    if (statussusbs.toString().equals("ok")) {
                        mPrinter.addCommand(left);
                        mPrinter.addCommand(un1);
                        StringBuilder textData1 = new StringBuilder();
                        textData1.append(str_line);
                        mPrinter.addText(textData1.toString());
                        mPrinter.addCommand(LF); //LF
                    }


                    String cou = "";

                    Cursor cursor5 = db.rawQuery("SELECT COUNT(itemtotalquan) FROM Itemwiseorderlistitems WHERE category = '" + category + "'", null);
                    if (cursor5.moveToFirst()) {
                        int level = cursor5.getInt(0);
                        cou = String.valueOf(level);
                    }
                    cursor5.close();


                    Cursor cursor4 = db.rawQuery("SELECT SUM(itemtotalquan) FROM Itemwiseorderlistitems WHERE category = '" + category + "'", null);
                    if (cursor4.moveToFirst()) {
                        float level = cursor4.getInt(0);
                        String total = String.valueOf(level);

                        allbuftaxestype1 = new byte[][]{
                                setHT321, left, "no. of items/qty".getBytes(), HT, "  ".getBytes(), cou.getBytes(), "/".getBytes(), total.getBytes(), LF
                        };

                        if (statussusbs.toString().equals("ok")) {
                            mPrinter.addCommand(setHT321);
                            mPrinter.addCommand(left);
                            StringBuilder textData1 = new StringBuilder();
                            textData1.append("no. of items/qty");
                            mPrinter.addText(textData1.toString());
                            mPrinter.addCommand(HT);
                            StringBuilder textData2 = new StringBuilder();
                            textData2.append("  "+cou+"/"+total);
                            mPrinter.addText(textData2.toString());
                            mPrinter.addCommand(LF); //LF
                        }

                    }
                    cursor4.close();

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
//            Toast.makeText(Itemwise_Saleslist.this, "Here2", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, method, mContext);
            return false;
        }

        return true;
    }

    private boolean createCouponData_minireceipt_category() {

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
                level = cursor11.getInt(0);
                total = String.valueOf(level);
            }

            Cursor count = db.rawQuery("SELECT COUNT(total) FROM Billnumber WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' ", null);
            if (count.moveToFirst()) {
                level = count.getInt(0);
                total1 = String.valueOf(level);
            }

            String avgbill = String.valueOf(Integer.parseInt(total) / Integer.parseInt(total1));


            noofbills = new TextView(Itemwise_Saleslist.this);


            Cursor countc = db.rawQuery("SELECT COUNT(total) FROM Billnumber WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' ", null);
            if (countc.moveToFirst()) {
                level = countc.getInt(0);
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


            allbuftax = new byte[][]{
                    setHT321, left, "Category".getBytes(), HT, "  ".getBytes(), "Total".getBytes(), LF
            };

            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

            if (statussusbs.toString().equals("ok")) {
                mPrinter.addCommand(setHT321);
                mPrinter.addCommand(left);
                StringBuilder textData1 = new StringBuilder();
                textData1.append("Category");
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(HT);
                StringBuilder textData2 = new StringBuilder();
                textData2.append("  "+"Total");
                mPrinter.addText(textData2.toString());
                mPrinter.addCommand(LF); //LF
            }

            allbuftax = new byte[][]{
                    setHT321, left, "Qty".getBytes(), HT, "  ".getBytes(), "%".getBytes(), LF
            };

            //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

            if (statussusbs.toString().equals("ok")) {
                mPrinter.addCommand(setHT321);
                mPrinter.addCommand(left);
                StringBuilder textData1 = new StringBuilder();
                textData1.append("Qty");
                mPrinter.addText(textData1.toString());
                mPrinter.addCommand(HT);
                StringBuilder textData2 = new StringBuilder();
                textData2.append("  "+"%");
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

            Cursor cursor = db.rawQuery("SELECT * FROM Itemwiseorderlistcategory ORDER BY sales DESC", null);
            if (cursor.moveToFirst()) {
                do {
                    String category = cursor.getString(2);
                    String tot = cursor.getString(3);
                    String qty = cursor.getString(5);
                    String pe = cursor.getString(4);

                    allbuftax = new byte[][]{
                            setHT321, left, category.getBytes(), HT, "  ".getBytes(), "Rs.".getBytes(), tot.getBytes(), LF
                    };

                    //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                    if (statussusbs.toString().equals("ok")) {
                        mPrinter.addCommand(setHT321);
                        mPrinter.addCommand(left);
                        StringBuilder textData1 = new StringBuilder();
                        textData1.append(category);
                        mPrinter.addText(textData1.toString());
                        mPrinter.addCommand(HT);
                        StringBuilder textData2 = new StringBuilder();
                        textData2.append("  "+insert1_rs+""+tot);
                        mPrinter.addText(textData2.toString());
                        mPrinter.addCommand(LF); //LF
                    }

                    allbuftax = new byte[][]{
                            setHT321, left, qty.getBytes(), HT, "  ".getBytes(), pe.getBytes(), "%".getBytes(), LF
                    };

                    //byte[] buf1 = DataUtils.byteArraysToBytes(compname);

                    if (statussusbs.toString().equals("ok")) {
                        mPrinter.addCommand(setHT321);
                        mPrinter.addCommand(left);
                        StringBuilder textData1 = new StringBuilder();
                        textData1.append(qty);
                        mPrinter.addText(textData1.toString());
                        mPrinter.addCommand(HT);
                        StringBuilder textData2 = new StringBuilder();
                        textData2.append("  "+pe+"%");
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
//            Toast.makeText(Itemwise_Saleslist.this, "Here2", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, method, mContext);
            return false;
        }

        return true;
    }

}
