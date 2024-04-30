package com.intuition.ivepos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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

/**
 * Created by Rohithkumar on 7/13/2017.
 */

public class Categorywise_Saleslist extends AppCompatActivity {


    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;

    int level;
    String total, Itemtype, total1, total1quan, itemtotal, one1, two2, three3, salesee1, salesee2, salesee3;

    File file = null;
    SimpleDateFormat sdff2, sdff1;
    String currentDateandTimee1;
    String timee1;

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

    private int hour;
    private int minute;


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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemwise_saleslist);


        editText1_filter = new EditText(Categorywise_Saleslist.this);
        editText2_filter = new EditText(Categorywise_Saleslist.this);

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

                dpd.show(Categorywise_Saleslist.this.getFragmentManager(), "Datepickerdialog");




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
//                dpd.show(Categorywise_Saleslist.this.getFragmentManager(), "Datepickerdialog");
                //if (clickcount == 1){
                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        datePickerListener,now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.show(Categorywise_Saleslist.this.getFragmentManager(), "Datepickerdialog");
                clickcount++;
//                }else {
//                    Calendar now = Calendar.getInstance();
//                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
//                            datePickerListener, year, month, day
//                    );
//
//                    dpd.show(Categorywise_Saleslist.this.getFragmentManager(), "Datepickerdialog");
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
//                    return new DatePickerDialog(Categorywise_Saleslist.this, this, yy, mm, dd);
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
                android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(Categorywise_Saleslist.this, R.style.timepicker_date_dialog, timePickerListener_open, hour, minute,
                        false);

                timePickerDialog.show();
            }
        });

        editText_to_day_visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(Categorywise_Saleslist.this, R.style.timepicker_date_dialog, timePickerListener_close, hour, minute,
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

        getlisting = (Spinner) findViewById(R.id.chocolate_category);

        tvkot = new TextView(Categorywise_Saleslist.this);
        totalsales_r = new TextView(Categorywise_Saleslist.this);
        avgsales_r = new TextView(Categorywise_Saleslist.this);
        final Calendar c = Calendar.getInstance();
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);

        mCredential = GoogleAccountCredential.usingOAuth2(
                Categorywise_Saleslist.this.getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        mProgress = new ProgressDialog(Categorywise_Saleslist.this, R.style.timepicker_date_dialog);
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
        if (player3name.toString().equals("Modifiers")){
            getlisting.setSelection(1);
        }
        if (player3name.toString().equals("Category")){
            getlisting.setSelection(2);
        }

//        Toast.makeText(Categorywise_Saleslist.this, "1 "+editText1_filter.getText().toString(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(Categorywise_Saleslist.this, "2 "+editText1_filter.getText().toString(), Toast.LENGTH_SHORT).show();


        DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
        downloadMusicfromInternet.execute(editText1_filter.getText().toString() + editText2_filter.getText().toString());


        final String Text = getlisting.getSelectedItem().toString();

        //dialog.dismiss();
        if (Text.equals("Items")) {
            final String selectQuery = "SELECT * FROM Itemwiseorderlistitems GROUP BY itemname ORDER BY sales DESC ";

            cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
            // The desired columns to be bound
            final String[] fromFieldNames = {"itemname", "itemno", "sales", "salespercentage", "itemtotalquan"};
            // the XML defined views which the data will be bound to
            final int[] toViewsID = {R.id.itemname, R.id.num, R.id.sales, R.id.salesper, R.id.quantity};
            //Log.e("Checamos que hay id", String.valueOf(R.id.name));
            adapter = new SimpleCursorAdapter(Categorywise_Saleslist.this, R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID, 0);
            listView.setAdapter(adapter);

            adapter.setFilterQueryProvider(new FilterQueryProvider() {
                public Cursor runQuery(CharSequence constraint) {
                    return fetchCountriesByName_items(constraint.toString());
                }
            });

        }else {
            final String selectQuery = "SELECT * FROM Itemwiseorderlistmodifiers GROUP BY modname ORDER BY sales DESC ";

            cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
            // The desired columns to be bound
            final String[] fromFieldNames = {"modname", "modno", "sales", "salespercentage", "modtotalquan"};
            // the XML defined views which the data will be bound to
            final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity};
            //Log.e("Checamos que hay id", String.valueOf(R.id.name));
            adapter = new SimpleCursorAdapter(Categorywise_Saleslist.this, R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID, 0);
            listView.setAdapter(adapter);

            adapter.setFilterQueryProvider(new FilterQueryProvider() {
                public Cursor runQuery(CharSequence constraint) {
                    return fetchCountriesByName_modifiers(constraint.toString());
                }
            });

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
                    final String selectQuery = "SELECT * FROM Itemwiseorderlistitems GROUP BY itemname ORDER BY sales DESC ";

                    cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"itemname", "itemno", "sales", "salespercentage", "itemtotalquan"};
                    // the XML defined views which the data will be bound to
                    final int[] toViewsID = {R.id.itemname, R.id.num, R.id.sales, R.id.salesper, R.id.quantity};
                    //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                    adapter = new SimpleCursorAdapter(Categorywise_Saleslist.this, R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);

                    adapter.setFilterQueryProvider(new FilterQueryProvider() {
                        public Cursor runQuery(CharSequence constraint) {
                            return fetchCountriesByName_items(constraint.toString());
                        }
                    });

                }else {
                    if (spineerseleitem.equals("Modifiers")) {
                        final String selectQuery = "SELECT * FROM Itemwiseorderlistmodifiers GROUP BY modname ORDER BY sales DESC ";

                        cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                        // The desired columns to be bound
                        final String[] fromFieldNames = {"modname", "modno", "sales", "salespercentage", "modtotalquan"};
                        // the XML defined views which the data will be bound to
                        final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity};
                        //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                        adapter = new SimpleCursorAdapter(Categorywise_Saleslist.this, R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID, 0);
                        listView.setAdapter(adapter);

                        adapter.setFilterQueryProvider(new FilterQueryProvider() {
                            public Cursor runQuery(CharSequence constraint) {
                                return fetchCountriesByName_modifiers(constraint.toString());
                            }
                        });
                    }else {

                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                    final String selectQuery = "SELECT * FROM Itemwiseorderlistitems GROUP BY itemname ORDER BY sales DESC ";

                    cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"itemname", "itemno", "sales", "salespercentage", "itemtotalquan"};
                    // the XML defined views which the data will be bound to
                    final int[] toViewsID = {R.id.itemname, R.id.num, R.id.sales, R.id.salesper, R.id.quantity};
                    //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                    adapter = new SimpleCursorAdapter(Categorywise_Saleslist.this, R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);

                    adapter.setFilterQueryProvider(new FilterQueryProvider() {
                        public Cursor runQuery(CharSequence constraint) {
                            return fetchCountriesByName_items(constraint.toString());
                        }
                    });

                }else {
                    final String selectQuery = "SELECT * FROM Itemwiseorderlistmodifiers GROUP BY modname ORDER BY sales DESC ";

                    cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    final String[] fromFieldNames = {"modname", "modno", "sales", "salespercentage", "modtotalquan"};
                    // the XML defined views which the data will be bound to
                    final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity};
                    //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                    adapter = new SimpleCursorAdapter(Categorywise_Saleslist.this, R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID, 0);
                    listView.setAdapter(adapter);

                    adapter.setFilterQueryProvider(new FilterQueryProvider() {
                        public Cursor runQuery(CharSequence constraint) {
                            return fetchCountriesByName_modifiers(constraint.toString());
                        }
                    });

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
                    Toast.makeText(Categorywise_Saleslist.this, getString(R.string.no_report_to_export), Toast.LENGTH_SHORT).show();
                }else {
                    //MenuItem bedMenuItem = menu.findItem(R.id.action_export);
                    //bedMenuItem.setVisible(true);
//                    Toast.makeText(Categorywise_Saleslist.this, "yes", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(Categorywise_Saleslist.this, getString(R.string.no_report_to_mail), Toast.LENGTH_SHORT).show();
                }else {
                    sdff2 = new SimpleDateFormat("ddMMMyy");
                    currentDateandTimee1 = sdff2.format(new Date());

                    Date dt1 = new Date();
                    sdff1 = new SimpleDateFormat("hhmmssaa");
                    timee1 = sdff1.format(dt1);

                    db1 = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor cursor = db1.rawQuery("SELECT * FROM Companydetailss", null);
                    if (cursor.moveToFirst()) {
                        companynameis = cursor.getString(1);
                    }else {
                        companynameis = "";
                    }

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
                                String arrStr1[] = {"Item name", "Individual price", "Quantity sold", "Sales(Rs.)", "Sales(%)"};
                                csvWrite.writeNext(arrStr1);

                                db = openOrCreateDatabase("mydb_Salesdata",
                                        Context.MODE_PRIVATE, null);
                                Cursor curCSV = db.rawQuery("SELECT * FROM Itemwiseorderlistitems", null);
                                //csvWrite.writeNext(curCSV.getColumnNames());

                                while (curCSV.moveToNext()) {
                                    String arrStr[] = {curCSV.getString(2), curCSV.getString(1), curCSV.getString(5), curCSV.getString(3), curCSV.getString(4)};
//	                curCSV.getString(2),curCSV.getString(3),curCSV.getString(4),
                                    csvWrite.writeNext(arrStr);

                                }

                                curCSV = db.rawQuery("SELECT * FROM Itemwiseorderlistmodifiers", null);
                                //csvWrite.writeNext(curCSV.getColumnNames());

                                while (curCSV.moveToNext()) {
                                    String arrStr[] = {curCSV.getString(2), curCSV.getString(1), curCSV.getString(5), curCSV.getString(3), curCSV.getString(4)};
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

                    String pone = "", ptwo = "", pthree = "";

                    TextView topone = new TextView(Categorywise_Saleslist.this);
                    TextView topitem1percent = new TextView(Categorywise_Saleslist.this);
                    TextView toptwo = new TextView(Categorywise_Saleslist.this);
                    TextView topitem2percent = new TextView(Categorywise_Saleslist.this);
                    TextView topthree = new TextView(Categorywise_Saleslist.this);
                    TextView topitem3percent = new TextView(Categorywise_Saleslist.this);
                    TextView totalsalesitemwise = new TextView(Categorywise_Saleslist.this);


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


                    Cursor top1item = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE itemname = '" + topone.getText().toString() + "' AND salespercentage = '" + pone + "' ", null);
                    if (top1item.moveToFirst()) {
                        salesee1 = top1item.getString(3);
                    }
                    Cursor top2item = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE itemname = '" + toptwo.getText().toString() + "' AND salespercentage = '" + ptwo + "' ", null);
                    if (top2item.moveToFirst()) {
                        salesee2 = top2item.getString(3);
                    }
                    Cursor top3item = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE itemname = '" + topthree.getText().toString() + "' AND salespercentage = '" + pthree + "' ", null);
                    if (top3item.moveToFirst()) {
                        salesee3 = top3item.getString(3);
                    }


                    Cursor cursor11 = db.rawQuery("SELECT SUM(total) FROM Billnumber WHERE date >= '" + editText1.getText().toString() + "' AND date <='" + editText2.getText().toString() + "' ", null);
                    if (cursor11.moveToFirst()) {
                        level = cursor11.getInt(0);
                        total = String.valueOf(level);
                    }
//                Toast.makeText(Categorywise_Saleslist.this, Text+" total is "+total, Toast.LENGTH_SHORT).show();
                    totalsalesitemwise.setText(R.string.Rs);
                    totalsalesitemwise.append(total);


                    String url = "www.intuitionsoftwares.com";

                    String msg = "Product report (" + editText11.getText().toString() + " - " + editText22.getText().toString() + ")\n\n" + "Overview" + " (Detailed report attached)" +
                            "\n\nTotal sales: " + totalsalesitemwise.getText().toString() + "\n\nTop selling products:\n\n" +
                            "1." + topone.getText().toString() + " - Rs. " + salesee1 + "(" + topitem1percent.getText().toString() + ")\n" +
                            "2." + toptwo.getText().toString() + " - Rs. " + salesee2 + "(" + topitem2percent.getText().toString() + ")\n" +
                            "3." + topthree.getText().toString() + " - Rs. " + salesee3 + "(" + topitem3percent.getText().toString() + ")\n\n" +
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
//                                        Toast.makeText(Categorywise_Saleslist.this, "yahoo "+un, Toast.LENGTH_LONG).show();
                                        Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
                                        if (cursor1.moveToFirst()) {
                                            do {
                                                String unn = cursor1.getString(3);
                                                String toEmails = unn;
                                                toEmailList = Arrays.asList(toEmails
                                                        .split("\\s*,\\s*"));
                                                new SendMailTask_Yahoo_attachment_Productlist(Categorywise_Saleslist.this).execute(un,
                                                        pwd, toEmailList, companynameis, msg, currentDateandTimee1, timee1);
                                            } while (cursor1.moveToNext());
                                        }


                                    }else {
                                        if (em_ca.toString().equals("Hotmail")){
//                                            Toast.makeText(Categorywise_Saleslist.this, "Hotmail and Outlook "+un, Toast.LENGTH_LONG).show();
                                            Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
                                            if (cursor1.moveToFirst()) {
                                                do {
                                                    String unn = cursor1.getString(3);
                                                    String toEmails = unn;
                                                    toEmailList = Arrays.asList(toEmails
                                                            .split("\\s*,\\s*"));
                                                    new SendMailTask_Hotmail_Outlook_attachment_Productlist(Categorywise_Saleslist.this).execute(un,
                                                            pwd, toEmailList, companynameis, msg, currentDateandTimee1, timee1);
                                                } while (cursor1.moveToNext());
                                            }
                                        }else {
                                            if (em_ca.toString().equals("Office365")) {
//                                                Toast.makeText(Categorywise_Saleslist.this, "office 365 " + un, Toast.LENGTH_LONG).show();
                                                Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
                                                if (cursor1.moveToFirst()) {
                                                    do {
                                                        String unn = cursor1.getString(3);
                                                        String toEmails = unn;
                                                        toEmailList = Arrays.asList(toEmails
                                                                .split("\\s*,\\s*"));
                                                        new SendMailTask_Office365_attachment_Productlist(Categorywise_Saleslist.this).execute(un,
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
                            final Dialog dialoge = new Dialog(Categorywise_Saleslist.this, R.style.timepicker_date_dialog);
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
                                    Intent intent = new Intent(Categorywise_Saleslist.this, EmailSetup_Recipients.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
//                                                                Categorywise_Saleslist.this.finish();
                                    dialoge.dismiss();
                                }
                            });

                            Button gotosettings1 = (Button) dialoge.findViewById(R.id.gotosettings1);
                            gotosettings1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Categorywise_Saleslist.this, EmailSetup_Recipients.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
//                                                                Categorywise_Saleslist.this.finish();
                                    dialoge.dismiss();
                                }
                            });


                        }
                    }else {
                        Cursor cursoree = db1.rawQuery("SELECT * FROM Email_recipient", null);
                        if (cursoree.moveToFirst()){
                            //only sender not there
                            final Dialog dialoge = new Dialog(Categorywise_Saleslist.this, R.style.timepicker_date_dialog);
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
                                    Intent intent = new Intent(Categorywise_Saleslist.this, EmailSetup.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
//                                                                Categorywise_Saleslist.this.finish();
                                    dialoge.dismiss();
                                }
                            });

                            Button gotosettings1 = (Button) dialoge.findViewById(R.id.gotosettings1);
                            gotosettings1.setVisibility(View.GONE);
                            gotosettings1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Categorywise_Saleslist.this, EmailSetup.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
//                                                                Categorywise_Saleslist.this.finish();
                                    dialoge.dismiss();
                                }
                            });

                        }else {
                            //both recipient and sender not there
                            final Dialog dialoge = new Dialog(Categorywise_Saleslist.this, R.style.timepicker_date_dialog);
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
                                    Intent intent = new Intent(Categorywise_Saleslist.this, EmailSetup.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
//                                                                Categorywise_Saleslist.this.finish();
                                    dialoge.dismiss();
                                }
                            });

                            Button gotosettings1 = (Button) dialoge.findViewById(R.id.gotosettings1);
                            gotosettings1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Categorywise_Saleslist.this, EmailSetup_Recipients.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
//                                                                Categorywise_Saleslist.this.finish();
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
        private final ProgressDialog dialog = new ProgressDialog(Categorywise_Saleslist.this, R.style.timepicker_date_dialog);

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
                String arrStr1[] ={"Item name", "Individual price", "Quantity sold", "Sales(Rs.)", "Sales(%)"};
                csvWrite.writeNext(arrStr1);

                db = openOrCreateDatabase("mydb_Salesdata",
                        Context.MODE_PRIVATE, null);
                Cursor curCSV = db.rawQuery("SELECT * FROM Itemwiseorderlistitems",null);
                //csvWrite.writeNext(curCSV.getColumnNames());

                while(curCSV.moveToNext())  {
                    String arrStr[] ={curCSV.getString(2), curCSV.getString(1), curCSV.getString(5), curCSV.getString(3), curCSV.getString(4)};
//	                curCSV.getString(2),curCSV.getString(3),curCSV.getString(4),
                    csvWrite.writeNext(arrStr);

                }

                curCSV = db.rawQuery("SELECT * FROM Itemwiseorderlistmodifiers", null);
                //csvWrite.writeNext(curCSV.getColumnNames());

                while(curCSV.moveToNext())  {
                    String arrStr[] ={curCSV.getString(2), curCSV.getString(1), curCSV.getString(5), curCSV.getString(3), curCSV.getString(4)};
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
                Toast.makeText(Categorywise_Saleslist.this, getString(R.string.export_successful), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(Categorywise_Saleslist.this, getString(R.string.export_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }


    class DownloadMusicfromInternet extends AsyncTask<String, Void, Integer> {

        private ProgressDialog dialog = new ProgressDialog(Categorywise_Saleslist.this, R.style.timepicker_date_dialog);


        @Override
        protected Integer doInBackground(String... params) {


            final String str = getlisting.getSelectedItem().toString();
            if (str.equals("Items")) {

                Cursor cursor = db.rawQuery("Select DISTINCT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <= '" + editText2_filter.getText().toString() + "' GROUP BY itemname ", null);//replace to cursor = dbHelper.fetchAllHotels();
                db.execSQL("delete from Itemwiseorderlistitems");
                if (cursor.moveToFirst()) {
                    do {

                        Itemtype = cursor.getString(5);

                        if (Itemtype.toString().equals("Item")) {

                            itemtotal = cursor.getString(1);

//                            final TableRow row = new TableRow(Categorywise_Saleslist.this);
//                            row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
//                                    TableRow.LayoutParams.MATCH_PARENT, 1.0f));
//                            row.setGravity(Gravity.CENTER_VERTICAL);

                            db1 = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                            Cursor modcursor1 = db1.rawQuery("Select * from Items WHERE itemname = '" + itemtotal + "' ", null);
                            if (modcursor1.moveToFirst()) {

                                do {

                                    id = modcursor1.getString(0);
                                    final TextView tv0 = new TextView(Categorywise_Saleslist.this);
//                                    tv0.setLayoutParams(new TableRow.LayoutParams(90, ViewGroup.LayoutParams.MATCH_PARENT));
//                                    //tv.setBackgroundResource(R.drawable.cell_shape);
//                                    tv0.setGravity(Gravity.CENTER);
//                                    tv0.setTextSize(15);
//                                    tv0.setTypeface(null, Typeface.NORMAL);
//                                    tv0.setPadding(5, 0, 0, 0);
//                                    tv0.setBackgroundResource(R.drawable.cell_shape);
                                    //text = cursor.getString(1);
                                    tv0.setText(id);
                                    //row.addView(tv0);

                                    id = modcursor1.getString(2);
                                    final TextView tv = new TextView(Categorywise_Saleslist.this);
//                                    tv.setLayoutParams(new TableRow.LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT));
//                                    //tv.setBackgroundResource(R.drawable.cell_shape);
//                                    tv.setGravity(Gravity.CENTER);
//                                    tv.setTextSize(15);
//                                    tv.setTypeface(null, Typeface.NORMAL);
//                                    tv.setPadding(5, 0, 0, 0);
//                                    tv.setBackgroundResource(R.drawable.cell_shape);
                                    //text = cursor.getString(1);
                                    tv.setText(id);
                                    //row.addView(tv);
                                } while (modcursor1.moveToNext());
                            }
                            modcursor1.close();

                            final TextView tv1 = new TextView(Categorywise_Saleslist.this);
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
                                    final TextView tv2 = new TextView(Categorywise_Saleslist.this);
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
                                    final TextView tv2 = new TextView(Categorywise_Saleslist.this);
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

                            float perc = Float.parseFloat(total1) * 100 / Float.parseFloat(total);
                            String percen = String.format("%.2f", perc);

                            final TextView tv3 = new TextView(Categorywise_Saleslist.this);
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
                            Cursor cat = db1.rawQuery("SELECT * FROM Items WHERE itemname = '"+itemtotal+"'", null);
                            if (cat.moveToFirst()){
                                String cat1 = cat.getString(4);
                                contentValues.put("category", cat1);

                            }
                            db.insert("Itemwiseorderlistitems", null, contentValues);
                        }
                    }while (cursor.moveToNext());
                }
            }

            if (str.equals("Modifiers")) {
                Cursor cursor = db.rawQuery("Select DISTINCT * from All_Sales WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' AND type = 'Modifier' GROUP BY itemname ", null);//replace to cursor = dbHelper.fetchAllHotels();
                db.execSQL("delete from Itemwiseorderlistmodifiers");
                //Toast.makeText(Categorywise_Saleslist.this, "Modifiersrsrsrsrsrsrsrs", Toast.LENGTH_SHORT).show();
                if (cursor.moveToFirst()) {
                    do {
                        //Toast.makeText(Categorywise_Saleslist.this, "Modifiersrsrsrsrsrsrsrs111", Toast.LENGTH_SHORT).show();
                        //do {
                        //Toast.makeText(Categorywise_Saleslist.this, "Modifiersrsrsrsrsrsrsrs222", Toast.LENGTH_SHORT).show();

                        String itemtotal = cursor.getString(1);

//                        final TableRow row = new TableRow(Categorywise_Saleslist.this);
//                        row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
//                                TableRow.LayoutParams.MATCH_PARENT, 1.0f));
//                        row.setGravity(Gravity.CENTER_VERTICAL);

                        db1 = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor modcursor1 = db1.rawQuery("Select * from Modifiers WHERE modifiername = '" + itemtotal + "' ", null);

                        if (modcursor1.moveToFirst()) {
                            do {

                                id = modcursor1.getString(0);
//                                final TextView tv0 = new TextView(Categorywise_Saleslist.this);
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
//                                final TextView tv = new TextView(Categorywise_Saleslist.this);
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

//                        final TextView tv1 = new TextView(Categorywise_Saleslist.this);
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
//                                final TextView tv2 = new TextView(Categorywise_Saleslist.this);
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
//                                final TextView tv2 = new TextView(Categorywise_Saleslist.this);
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

                        float perc = Float.parseFloat(total1) * 100 / Float.parseFloat(total);
                        String percen = String.format("%.2f", perc);

//                        final TextView tv3 = new TextView(Categorywise_Saleslist.this);
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
                        db.insert("Itemwiseorderlistmodifiers", null, contentValues);
                    }while (cursor.moveToNext());
                    //}while (cursor.moveToNext());
                }
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

            final String Text = getlisting.getSelectedItem().toString();

            //dialog.dismiss();
            if (Text.equals("Items")) {
                final String selectQuery = "SELECT * FROM Itemwiseorderlistitems GROUP BY itemname ORDER BY sales DESC ";

                cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                // The desired columns to be bound
                final String[] fromFieldNames = {"itemname", "itemno", "sales", "salespercentage", "itemtotalquan"};
                // the XML defined views which the data will be bound to
                final int[] toViewsID = {R.id.itemname, R.id.num, R.id.sales, R.id.salesper, R.id.quantity};
                //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                adapter = new SimpleCursorAdapter(Categorywise_Saleslist.this, R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID, 0);
                listView.setAdapter(adapter);

                adapter.setFilterQueryProvider(new FilterQueryProvider() {
                    public Cursor runQuery(CharSequence constraint) {
                        return fetchCountriesByName_items(constraint.toString());
                    }
                });

            }else {
                final String selectQuery = "SELECT * FROM Itemwiseorderlistmodifiers GROUP BY modname ORDER BY sales DESC ";

                cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                // The desired columns to be bound
                final String[] fromFieldNames = {"modname", "modno", "sales", "salespercentage", "modtotalquan"};
                // the XML defined views which the data will be bound to
                final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity};
                //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                adapter = new SimpleCursorAdapter(Categorywise_Saleslist.this, R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID, 0);
                listView.setAdapter(adapter);

                adapter.setFilterQueryProvider(new FilterQueryProvider() {
                    public Cursor runQuery(CharSequence constraint) {
                        return fetchCountriesByName_modifiers(constraint.toString());
                    }
                });

            }

        }
    }


    public Cursor fetchCountriesByName_items(String inputtext) throws SQLException {
//        SELECT * FROM Generalorderlistascdesc1 WHERE paymentmethod = '  Card' GROUP By billno ORDER BY datetimee DESC
        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
            mCursor = db.rawQuery("SELECT * FROM Itemwiseorderlistitems GROUP BY itemname ORDER BY sales DESC", null);
//            mCursor = db.rawQuery("SELECT * FROM Generalorderlistascdesc1 WHERE billtype = '  Dine-in' GROUP By billno ORDER BY datetimee DESC", null);
//            mCursor = db.rawQuery("SELECT * FROM Generalorderlistascdesc1 GROUP By billno ORDER BY datetimee DESC", null);
//            mCursor = db.query("Generalorderlistascdesc1", new String[] {"_id", "date", "time", "user", "billcount", "billno", "sales"},
//                    null, null, null, null, null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE itemname LIKE '%" + inputtext + "%' GROUP BY itemname ORDER BY sales DESC", null);
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

    public Cursor fetchCountriesByName_modifiers(String inputtext) throws SQLException {
//        SELECT * FROM Generalorderlistascdesc1 WHERE paymentmethod = '  Card' GROUP By billno ORDER BY datetimee DESC
        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
            mCursor = db.rawQuery("SELECT * FROM Itemwiseorderlistmodifiers GROUP BY modname ORDER BY sales DESC", null);
//            mCursor = db.rawQuery("SELECT * FROM Generalorderlistascdesc1 WHERE billtype = '  Dine-in' GROUP By billno ORDER BY datetimee DESC", null);
//            mCursor = db.rawQuery("SELECT * FROM Generalorderlistascdesc1 GROUP By billno ORDER BY datetimee DESC", null);
//            mCursor = db.query("Generalorderlistascdesc1", new String[] {"_id", "date", "time", "user", "billcount", "billno", "sales"},
//                    null, null, null, null, null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Itemwiseorderlistmodifiers WHERE modname LIKE '%" + inputtext + "%' GROUP BY modname ORDER BY sales DESC", null);
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

            TextView topone = new TextView(Categorywise_Saleslist.this);
            TextView topitem1percent = new TextView(Categorywise_Saleslist.this);
            TextView toptwo = new TextView(Categorywise_Saleslist.this);
            TextView topitem2percent = new TextView(Categorywise_Saleslist.this);
            TextView topthree = new TextView(Categorywise_Saleslist.this);
            TextView topitem3percent = new TextView(Categorywise_Saleslist.this);
            TextView totalsalesitemwise = new TextView(Categorywise_Saleslist.this);


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


            Cursor top1item = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE itemname = '" + topone.getText().toString() + "' AND salespercentage = '" + pone + "' ", null);
            if (top1item.moveToFirst()) {
                salesee1 = top1item.getString(3);
            }
            Cursor top2item = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE itemname = '" + toptwo.getText().toString() + "' AND salespercentage = '" + ptwo + "' ", null);
            if (top2item.moveToFirst()) {
                salesee2 = top2item.getString(3);
            }
            Cursor top3item = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE itemname = '" + topthree.getText().toString() + "' AND salespercentage = '" + pthree + "' ", null);
            if (top3item.moveToFirst()) {
                salesee3 = top3item.getString(3);
            }


            Cursor cursor11 = db.rawQuery("SELECT SUM(total) FROM Billnumber WHERE date >= '" + editText1.getText().toString() + "' AND date <='" + editText2.getText().toString() + "' ", null);
            if (cursor11.moveToFirst()) {
                level = cursor11.getInt(0);
                total = String.valueOf(level);
            }
//                Toast.makeText(Categorywise_Saleslist.this, Text+" total is "+total, Toast.LENGTH_SHORT).show();
            totalsalesitemwise.setText(R.string.Rs);
            totalsalesitemwise.append(total);

            String url = "www.intuitionsoftwares.com";

//            String msg = "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
//                    "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
//                    "Powered by: " + Uri.parse(url);

            String msg = "Product report (" + editText11.getText().toString() + " - " + editText22.getText().toString() + ")\n\n" + "Overview" + " (Detailed report attached)" +
                    "\n\nTotal sales: " + totalsalesitemwise.getText().toString() + "\n\nTop selling products:\n\n" +
                    "1." + topone.getText().toString() + " - Rs. " + salesee1 + "(" + topitem1percent.getText().toString() + ")\n" +
                    "2." + toptwo.getText().toString() + " - Rs. " + salesee2 + "(" + topitem2percent.getText().toString() + ")\n" +
                    "3." + topthree.getText().toString() + " - Rs. " + salesee3 + "(" + topitem3percent.getText().toString() + ")\n\n" +
                    "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
                    "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
                    "Powered by: " + Uri.parse(url);

            Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
            if (cursor1.moveToFirst()) {
                do {
                    String unn = cursor1.getString(3);
                    TextView edtToAddress = new TextView(Categorywise_Saleslist.this);
                    edtToAddress.setText(unn);

                    TextView edtSubject = new TextView(Categorywise_Saleslist.this);
                    edtSubject.setText(strcompanyname);

                    TextView edtMessage = new TextView(Categorywise_Saleslist.this);
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
                Toast.makeText(Categorywise_Saleslist.this, "not success", Toast.LENGTH_SHORT).show();
//                showMessage(view, "No results returned.");
            } else {
                Toast.makeText(Categorywise_Saleslist.this, "success", Toast.LENGTH_SHORT).show();
//                showMessage(view, output);
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                Log.v("Errors3", mLastError.getMessage());
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    Log.v("Errors1", mLastError.getMessage());
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    Log.v("Errors2", mLastError.getMessage());
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            Utils.REQUEST_AUTHORIZATION);
                } else {
//                    showMessage(view, "The following error occurred:\n" + mLastError.getMessage());
                    Log.v("Errors", mLastError.getMessage());
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
                Categorywise_Saleslist.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private void getResultsFromApi() {

        Cursor cursorr = db1.rawQuery("SELECT * FROM Email_setup", null);
        if (cursorr.moveToFirst()) {
            String unn = cursorr.getString(1);
//            Toast.makeText(Categorywise_Saleslist.this, "a4 " + unn, Toast.LENGTH_SHORT).show();

            TextView tvv = new TextView(Categorywise_Saleslist.this);
            tvv.setText(unn);

            if (tvv.getText().toString().equals("")) {

            }else {
                mCredential.setSelectedAccountName(tvv.getText().toString());
            }
        }

        if (! isGooglePlayServicesAvailable()) {
//            Toast.makeText(Categorywise_Saleslist.this, "1", Toast.LENGTH_SHORT).show();
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
//            Toast.makeText(Categorywise_Saleslist.this, "2", Toast.LENGTH_SHORT).show();
//            chooseAccount();
        } else if (! isDeviceOnline()) {
//            Toast.makeText(Categorywise_Saleslist.this, "3", Toast.LENGTH_SHORT).show();
//            mOutputText.setText("No network connection available.");
        } else {
//            Toast.makeText(Categorywise_Saleslist.this, "4", Toast.LENGTH_SHORT).show();
            new MakeRequestTask1(mCredential).execute();
        }
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) Categorywise_Saleslist.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
                apiAvailability.isGooglePlayServicesAvailable(Categorywise_Saleslist.this);
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
                apiAvailability.isGooglePlayServicesAvailable(Categorywise_Saleslist.this);
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
//        Toast.makeText(Categorywise_Saleslist.this, "s1", Toast.LENGTH_SHORT).show();
        if (EasyPermissions.hasPermissions(
                Categorywise_Saleslist.this, android.Manifest.permission.GET_ACCOUNTS)) {
            String accountName = Categorywise_Saleslist.this.getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
//            Toast.makeText(Categorywise_Saleslist.this, "s2", Toast.LENGTH_SHORT).show();
//            if (accountName != null) {
//                mCredential.setSelectedAccountName(accountName);
//                Toast.makeText(Categorywise_Saleslist.this, "s3", Toast.LENGTH_SHORT).show();
//                getResultsFromApi();
//            } else {
            // Start a dialog from which the user can choose an account
            startActivityForResult(
                    mCredential.newChooseAccountIntent(),
                    REQUEST_ACCOUNT_PICKER);
//            Toast.makeText(Categorywise_Saleslist.this, "s4", Toast.LENGTH_SHORT).show();
//            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
//            Toast.makeText(Categorywise_Saleslist.this, "s5", Toast.LENGTH_SHORT).show();
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

}
