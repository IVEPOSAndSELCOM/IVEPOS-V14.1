package com.intuition.ivepos;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
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


public class ItemwiseOrderlistActivity extends Fragment {

    Fragment frag;
    FragmentTransaction fragTransaction;

    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;
    Button btnok;
    Spinner getlisting, getsorting, hightolow, lowtohigh;
    int level;
    String total, Itemtype, total1, total1quan, itemtotal, one1, two2, three3, salesee1, salesee2, salesee3;
    String pone, ptwo, pthree;
    TextView totalsalesitemwise, topone, toptwo, topthree;
    TextView topitem1percent, topitem2percent, topitem3percent;
    ListView listView;
    SimpleCursorAdapter adapter;
    String id;
    Cursor cursor1;

    File file = null;
    SimpleDateFormat sdff2, sdff1;
    String currentDateandTimee1;
    String timee1;

    String companynameis;

    String onee, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve;
    String onee1, two1, three1, four1, five1, six1, seven1, eight1, nine1, ten1, eleven1, twelve1;

    int clickcount = 1, clickcounts = 1;
    private int year, year1;
    private int month, month1;
    private int day, day1;


    LinearLayout panel1, panel11, panel111;
    LinearLayout text1, text11, text111;
    ImageView rotatearrow, rotatearrow1, rotatearrow11;
    View openLayout;

    TextView editText1, editText2, editText11, editText22;


    List toEmailList;

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

    String strcompanyname, straddress1;


    public ItemwiseOrderlistActivity() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setHasOptionsMenu(true);
        final View rootview = inflater.inflate(R.layout.fragment_multi_itemwiseorderlist2, null);

        if (getActivity() instanceof AppCompatActivity){
            androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionbar.setSubtitle("Product");
        }

        mCredential = GoogleAccountCredential.usingOAuth2(
                getActivity().getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage(getString(R.string.setmessage4));

        //ActionBar actionBar = getActivity().getActionBar();


        //actionBar.setTitle("Product report");

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy MM dd");
        final String currentDateandTime1 = sdf2.format(new Date());

        SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM yyyy");
        final String currentDateandTime2 = sdf3.format(new Date());

        editText1 = (TextView)rootview.findViewById(R.id.editText1);
        editText1.setText(currentDateandTime1);
        editText2 = (TextView)rootview.findViewById(R.id.editText2);
        editText2.setText(currentDateandTime1);


        editText11 = (TextView)rootview.findViewById(R.id.editText11);
        editText11.setText(currentDateandTime2);


        editText22 = (TextView)rootview.findViewById(R.id.editText22);
        editText22.setText(currentDateandTime2);


        panel1 = (LinearLayout) rootview.findViewById(R.id.relativeLayout3);
        text1 = (LinearLayout)rootview.findViewById(R.id.text1);
        rotatearrow = (ImageView) rootview.findViewById(R.id.arrow);


        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hideOthers(v);
                if (panel1.getVisibility() == View.VISIBLE){
                    rotatearrow.setRotation(180);
                    panel1.setVisibility(View.GONE);
                }else {
                    rotatearrow.setRotation(360);
                    panel1.setVisibility(View.VISIBLE);
                }
            }
        });

        btnok = (Button)rootview.findViewById(R.id.okok);
        getlisting = (Spinner)rootview.findViewById(R.id.chocolate_itemwise);
        getsorting = (Spinner)rootview.findViewById(R.id.chocolate_spinner);
        hightolow = (Spinner)rootview.findViewById(R.id.chocolate_hightolow);

        listView = (ListView)rootview.findViewById(R.id.listView11);



        totalsalesitemwise = (TextView)rootview.findViewById(R.id.totalsales1);
        topone = (TextView)rootview.findViewById(R.id.topitem1);
        toptwo = (TextView)rootview.findViewById(R.id.topitem2);
        topthree = (TextView)rootview.findViewById(R.id.topitem3);

        topitem1percent = (TextView)rootview.findViewById(R.id.topitem1percen);
        topitem2percent = (TextView)rootview.findViewById(R.id.topitem2percen);
        topitem3percent = (TextView)rootview.findViewById(R.id.topitem3percen);

//        listView = (ListView)rootview.findViewById(R.id.listView6);



        editText11.setOnClickListener(new View.OnClickListener() {

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
//                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        datePickerListener,now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");

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
                TextView mEdit = (TextView) rootview.findViewById(R.id.editText1);
                TextView mEdit1  = (TextView)rootview.findViewById(R.id.editText11);
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
//                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                //if (clickcount == 1){
                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        datePickerListener,now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                clickcount++;
//                }else {
//                    Calendar now = Calendar.getInstance();
//                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
//                            datePickerListener, year, month, day
//                    );
//
//                    dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
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
                TextView mEdit = (TextView) rootview.findViewById(R.id.editText2);
                TextView mEdit1  = (TextView)rootview.findViewById(R.id.editText22);
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
//                    return new DatePickerDialog(getActivity(), this, yy, mm, dd);
//                }
//
//
//                @Override
//                public void onDateSet(DatePicker view, int yy, int mm, int dd) {
//                    populateSetDate(yy, mm + 1, dd);
//                }
//            }
        });




        String Text = getlisting.getSelectedItem().toString();

        db = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        Cursor cursor11 = db.rawQuery("SELECT SUM(total) FROM Billnumber WHERE date >= '" + editText1.getText().toString() + "' AND date <='" + editText2.getText().toString() + "' ", null);
        if (cursor11.moveToFirst()) {
            level = cursor11.getInt(0);
            total = String.valueOf(level);
        }
//                Toast.makeText(getActivity(), Text+" total is "+total, Toast.LENGTH_SHORT).show();
        totalsalesitemwise.setText(R.string.Rs);
        totalsalesitemwise.append(total);

        DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
        downloadMusicfromInternet.execute(editText1.getText().toString() + editText2.getText().toString());


//        if (Text.equals("Items")) {
//            Cursor cursor = db.rawQuery("Select DISTINCT * from All_Sales WHERE date >= '" + editText1.getText().toString() + "' AND date <= '" + editText2.getText().toString() + "' GROUP BY itemname ", null);//replace to cursor = dbHelper.fetchAllHotels();
//            db.execSQL("delete from Itemwiseorderlistitems");
//            if (cursor.moveToFirst()) {
//                do {
//
//                    Itemtype = cursor.getString(5);
//
//                    if (Itemtype.toString().equals("Item")) {
//
//                        itemtotal = cursor.getString(1);
//
//                        final TableRow row = new TableRow(getActivity());
//                        row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
//                                TableRow.LayoutParams.MATCH_PARENT, 1.0f));
//                        row.setGravity(Gravity.CENTER_VERTICAL);
//
//                        db1 = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//                        Cursor modcursor1 = db1.rawQuery("Select * from Items WHERE itemname = '" + itemtotal + "' ", null);
//                        if (modcursor1.moveToFirst()) {
//
//                            do {
//
//                                id = modcursor1.getString(0);
//                                final TextView tv0 = new TextView(getActivity());
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
//
//                                id = modcursor1.getString(0);
//                                final TextView tv = new TextView(getActivity());
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
//                            } while (modcursor1.moveToNext());
//                        }
//
//                        final TextView tv1 = new TextView(getActivity());
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
//
//                        Cursor modcursor = db.rawQuery("Select SUM(total) from All_Sales WHERE itemname = '" + itemtotal + "' AND date >= '" + editText1.getText().toString() + "' AND date <='" + editText2.getText().toString() + "' GROUP BY itemname ", null);
//                        if (modcursor.moveToFirst()) {
//
//                            do {
//                                level = modcursor.getInt(0);
//                                total1 = String.valueOf(level);
//                                final TextView tv2 = new TextView(getActivity());
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
//                            } while (modcursor.moveToNext());
//                        }
//
//                        Cursor modcursorsor = db.rawQuery("Select SUM(quantity) from All_Sales WHERE itemname = '" + itemtotal + "' AND date >= '" + editText1.getText().toString() + "' AND date <='" + editText2.getText().toString() + "' GROUP BY itemname ", null);
//                        if (modcursorsor.moveToFirst()) {
//
//                            do {
//                                level = modcursorsor.getInt(0);
//                                total1quan = String.valueOf(level);
//                                final TextView tv2 = new TextView(getActivity());
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
//                            } while (modcursor.moveToNext());
//                        }
//
//
//                        Cursor cursorr11 = db.rawQuery("SELECT SUM(total) FROM Billnumber WHERE date >= '" + editText1.getText().toString() + "' AND date <='" + editText2.getText().toString() + "' ", null);
//                        if (cursorr11.moveToFirst()) {
//                            level = cursorr11.getInt(0);
//                            total = String.valueOf(level);
//                        }
//
//                        float perc = Float.parseFloat(total1) * 100 / Float.parseFloat(total);
//                        String percen = String.format("%.2f", perc);
//
//                        final TextView tv3 = new TextView(getActivity());
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
//
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("itemno", String.valueOf(id));
//                        contentValues.put("itemname", itemtotal);
//                        contentValues.put("sales", total1);
//                        contentValues.put("salespercentage", percen);
//                        contentValues.put("itemtotalquan", total1quan);
//                        db.insert("Itemwiseorderlistitems", null, contentValues);
//                    }
//                }while (cursor.moveToNext());
//            }
//        }


        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Text = getlisting.getSelectedItem().toString();

                db = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor cursor11 = db.rawQuery("SELECT SUM(total) FROM Billnumber WHERE date >= '" + editText1.getText().toString() + "' AND date <='" + editText2.getText().toString() + "' ", null);
                if (cursor11.moveToFirst()) {
                    level = cursor11.getInt(0);
                    total = String.valueOf(level);
                }
//                Toast.makeText(getActivity(), Text+" total is "+total, Toast.LENGTH_SHORT).show();
                totalsalesitemwise.setText(R.string.Rs);
                totalsalesitemwise.append(total);

                DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
                downloadMusicfromInternet.execute(editText1.getText().toString() + editText2.getText().toString());

//                if (Text.equals("Items")) {
//                    Cursor cursor = db.rawQuery("Select DISTINCT * from All_Sales WHERE date >= '" + editText1.getText().toString() + "' AND date <= '" + editText2.getText().toString() + "' GROUP BY itemname ", null);//replace to cursor = dbHelper.fetchAllHotels();
//                    db.execSQL("delete from Itemwiseorderlistitems");
//                    if (cursor.moveToFirst()) {
//                        do {
//
//                            Itemtype = cursor.getString(5);
//
//                            if (Itemtype.toString().equals("Item")) {
//
//                                itemtotal = cursor.getString(1);
//
//                                final TableRow row = new TableRow(getActivity());
//                                row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
//                                        TableRow.LayoutParams.MATCH_PARENT, 1.0f));
//                                row.setGravity(Gravity.CENTER_VERTICAL);
//
//                                db1 = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//                                Cursor modcursor1 = db1.rawQuery("Select * from Items WHERE itemname = '" + itemtotal + "' ", null);
//                                if (modcursor1.moveToFirst()) {
//
//                                    do {
//
//                                        id = modcursor1.getString(0);
//                                        final TextView tv0 = new TextView(getActivity());
//                                        tv0.setLayoutParams(new TableRow.LayoutParams(90, ViewGroup.LayoutParams.MATCH_PARENT));
//                                        //tv.setBackgroundResource(R.drawable.cell_shape);
//                                        tv0.setGravity(Gravity.CENTER);
//                                        tv0.setTextSize(15);
//                                        tv0.setTypeface(null, Typeface.NORMAL);
//                                        tv0.setPadding(5, 0, 0, 0);
//                                        tv0.setBackgroundResource(R.drawable.cell_shape);
//                                        //text = cursor.getString(1);
//                                        tv0.setText(id);
//                                        row.addView(tv0);
//
//                                        id = modcursor1.getString(0);
//                                        final TextView tv = new TextView(getActivity());
//                                        tv.setLayoutParams(new TableRow.LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT));
//                                        //tv.setBackgroundResource(R.drawable.cell_shape);
//                                        tv.setGravity(Gravity.CENTER);
//                                        tv.setTextSize(15);
//                                        tv.setTypeface(null, Typeface.NORMAL);
//                                        tv.setPadding(5, 0, 0, 0);
//                                        tv.setBackgroundResource(R.drawable.cell_shape);
//                                        //text = cursor.getString(1);
//                                        tv.setText(id);
//                                        row.addView(tv);
//                                    } while (modcursor1.moveToNext());
//                                }
//
//                                final TextView tv1 = new TextView(getActivity());
//                                tv1.setLayoutParams(new TableRow.LayoutParams(260, ViewGroup.LayoutParams.MATCH_PARENT));
//                                //tv.setBackgroundResource(R.drawable.cell_shape);
//                                tv1.setGravity(Gravity.CENTER);
//                                tv1.setTextSize(15);
//                                tv1.setTypeface(null, Typeface.NORMAL);
//                                tv1.setPadding(5, 0, 0, 0);
//                                tv1.setBackgroundResource(R.drawable.cell_shape);
//                                //text = cursor.getString(1);
//                                tv1.setText(itemtotal);
//                                row.addView(tv1);
//
//                                Cursor modcursor = db.rawQuery("Select SUM(total) from All_Sales WHERE itemname = '" + itemtotal + "' AND date >= '" + editText1.getText().toString() + "' AND date <='" + editText2.getText().toString() + "' GROUP BY itemname ", null);
//                                if (modcursor.moveToFirst()) {
//
//                                    do {
//                                        level = modcursor.getInt(0);
//                                        total1 = String.valueOf(level);
//                                        final TextView tv2 = new TextView(getActivity());
//                                        tv2.setLayoutParams(new TableRow.LayoutParams(150, ViewGroup.LayoutParams.MATCH_PARENT));
//                                        //tv.setBackgroundResource(R.drawable.cell_shape);
//                                        tv2.setGravity(Gravity.CENTER);
//                                        tv2.setTextSize(15);
//                                        tv2.setTypeface(null, Typeface.NORMAL);
//                                        tv2.setPadding(5, 0, 0, 0);
//                                        tv2.setBackgroundResource(R.drawable.cell_shape);
//                                        //text = cursor.getString(1);
//                                        tv2.setText(total1);
//                                        row.addView(tv2);
//                                    } while (modcursor.moveToNext());
//                                }
//
//                                Cursor modcursorsor = db.rawQuery("Select SUM(quantity) from All_Sales WHERE itemname = '" + itemtotal + "' AND date >= '" + editText1.getText().toString() + "' AND date <='" + editText2.getText().toString() + "' GROUP BY itemname ", null);
//                                if (modcursorsor.moveToFirst()) {
//
//                                    do {
//                                        level = modcursorsor.getInt(0);
//                                        total1quan = String.valueOf(level);
//                                        final TextView tv2 = new TextView(getActivity());
//                                        tv2.setLayoutParams(new TableRow.LayoutParams(150, ViewGroup.LayoutParams.MATCH_PARENT));
//                                        //tv.setBackgroundResource(R.drawable.cell_shape);
//                                        tv2.setGravity(Gravity.CENTER);
//                                        tv2.setTextSize(15);
//                                        tv2.setTypeface(null, Typeface.NORMAL);
//                                        tv2.setPadding(5, 0, 0, 0);
//                                        tv2.setBackgroundResource(R.drawable.cell_shape);
//                                        //text = cursor.getString(1);
//                                        tv2.setText(total1quan);
//                                        row.addView(tv2);
//                                    } while (modcursor.moveToNext());
//                                }
//
//
//                                Cursor cursorr11 = db.rawQuery("SELECT SUM(total) FROM Billnumber WHERE date >= '" + editText1.getText().toString() + "' AND date <='" + editText2.getText().toString() + "' ", null);
//                                if (cursorr11.moveToFirst()) {
//                                    level = cursorr11.getInt(0);
//                                    total = String.valueOf(level);
//                                }
//
//                                float perc = Float.parseFloat(total1) * 100 / Float.parseFloat(total);
//                                String percen = String.format("%.2f", perc);
//
//                                final TextView tv3 = new TextView(getActivity());
//                                tv3.setLayoutParams(new TableRow.LayoutParams(170, ViewGroup.LayoutParams.MATCH_PARENT));
//                                //tv.setBackgroundResource(R.drawable.cell_shape);
//                                tv3.setGravity(Gravity.CENTER);
//                                tv3.setTextSize(15);
//                                tv3.setTypeface(null, Typeface.NORMAL);
//                                tv3.setPadding(5, 0, 0, 0);
//                                tv3.setBackgroundResource(R.drawable.cell_shape);
//                                //text = cursor.getString(1);
//                                tv3.setText(percen);
//                                row.addView(tv3);
//
//                                ContentValues contentValues = new ContentValues();
//                                contentValues.put("itemno", String.valueOf(id));
//                                contentValues.put("itemname", itemtotal);
//                                contentValues.put("sales", total1);
//                                contentValues.put("salespercentage", percen);
//                                contentValues.put("itemtotalquan", total1quan);
//                                db.insert("Itemwiseorderlistitems", null, contentValues);
//                            }
//                        }while (cursor.moveToNext());
//                    }
//                }
//                if (Text.equals("Modifiers")) {
//                    Cursor cursor = db.rawQuery("Select DISTINCT * from All_Sales WHERE date >= '" + editText1.getText().toString() + "' AND date <='" + editText2.getText().toString() + "' AND type = 'Modifier' GROUP BY itemname ", null);//replace to cursor = dbHelper.fetchAllHotels();
//                    db.execSQL("delete from Itemwiseorderlistmodifiers");
//                    Toast.makeText(getActivity(), "Modifiersrsrsrsrsrsrsrs", Toast.LENGTH_SHORT).show();
//                    if (cursor.moveToFirst()) {
//                        Toast.makeText(getActivity(), "Modifiersrsrsrsrsrsrsrs111", Toast.LENGTH_SHORT).show();
//                        //do {
//                            Toast.makeText(getActivity(), "Modifiersrsrsrsrsrsrsrs222", Toast.LENGTH_SHORT).show();
//
//                            String itemtotal = cursor.getString(1);
//
//                            final TableRow row = new TableRow(getActivity());
//                            row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
//                                    TableRow.LayoutParams.MATCH_PARENT, 1.0f));
//                            row.setGravity(Gravity.CENTER_VERTICAL);
//
//                            Cursor modcursor1 = db1.rawQuery("Select * from Modifiers WHERE modifiername = '" + itemtotal + "' ", null);
//
//                            if (modcursor1.moveToFirst()) {
//                                do {
//
//                                    id = modcursor1.getString(0);
//                                    final TextView tv0 = new TextView(getActivity());
//                                    tv0.setLayoutParams(new TableRow.LayoutParams(90, ViewGroup.LayoutParams.MATCH_PARENT));
//                                    //tv.setBackgroundResource(R.drawable.cell_shape);
//                                    tv0.setGravity(Gravity.CENTER);
//                                    tv0.setTextSize(15);
//                                    tv0.setTypeface(null, Typeface.NORMAL);
//                                    tv0.setPadding(5, 0, 0, 0);
//                                    tv0.setBackgroundResource(R.drawable.cell_shape);
//                                    //text = cursor.getString(1);
//                                    tv0.setText(id);
//                                    row.addView(tv0);
//
//                                    id = modcursor1.getString(0);
//                                    final TextView tv = new TextView(getActivity());
//                                    tv.setLayoutParams(new TableRow.LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT));
//                                    //tv.setBackgroundResource(R.drawable.cell_shape);
//                                    tv.setGravity(Gravity.CENTER);
//                                    tv.setTextSize(15);
//                                    tv.setTypeface(null, Typeface.NORMAL);
//                                    tv.setPadding(5, 0, 0, 0);
//                                    tv.setBackgroundResource(R.drawable.cell_shape);
//                                    //text = cursor.getString(1);
//                                    tv.setText(id);
//                                    row.addView(tv);
//                                } while (modcursor1.moveToNext());
//                            }
//
//                            final TextView tv1 = new TextView(getActivity());
//                            tv1.setLayoutParams(new TableRow.LayoutParams(260, ViewGroup.LayoutParams.MATCH_PARENT));
//                            //tv.setBackgroundResource(R.drawable.cell_shape);
//                            tv1.setGravity(Gravity.CENTER);
//                            tv1.setTextSize(15);
//                            tv1.setTypeface(null, Typeface.NORMAL);
//                            tv1.setPadding(5, 0, 0, 0);
//                            tv1.setBackgroundResource(R.drawable.cell_shape);
//                            //text = cursor.getString(1);
//                            tv1.setText(itemtotal);
//                            row.addView(tv1);
//
//                            Cursor modcursor = db.rawQuery("Select SUM(total) from All_Sales WHERE itemname = '" + itemtotal + "' AND date >= '" + editText1.getText().toString() + "' AND date <='" + editText2.getText().toString() + "' GROUP BY itemname ", null);
//
//                            if (modcursor.moveToFirst()) {
//                                do {
//                                    level = modcursor.getInt(0);
//                                    total1 = String.valueOf(level);
//                                    final TextView tv2 = new TextView(getActivity());
//                                    tv2.setLayoutParams(new TableRow.LayoutParams(150, ViewGroup.LayoutParams.MATCH_PARENT));
//                                    //tv.setBackgroundResource(R.drawable.cell_shape);
//                                    tv2.setGravity(Gravity.CENTER);
//                                    tv2.setTextSize(15);
//                                    tv2.setTypeface(null, Typeface.NORMAL);
//                                    tv2.setPadding(5, 0, 0, 0);
//                                    tv2.setBackgroundResource(R.drawable.cell_shape);
//                                    //text = cursor.getString(1);
//                                    tv2.setText(total1);
//                                    row.addView(tv2);
//                                } while (modcursor.moveToNext());
//                            }
//
//                            Cursor modcursorsor = db.rawQuery("Select SUM(quantity) from All_Sales WHERE itemname = '" + itemtotal + "' AND date >= '" + editText1.getText().toString() + "' AND date <='" + editText2.getText().toString() + "' GROUP BY itemname ", null);
//                            if (modcursorsor.moveToFirst()) {
//
//                                do {
//                                    level = modcursorsor.getInt(0);
//                                    total1quan = String.valueOf(level);
//                                    final TextView tv2 = new TextView(getActivity());
//                                    tv2.setLayoutParams(new TableRow.LayoutParams(150, ViewGroup.LayoutParams.MATCH_PARENT));
//                                    //tv.setBackgroundResource(R.drawable.cell_shape);
//                                    tv2.setGravity(Gravity.CENTER);
//                                    tv2.setTextSize(15);
//                                    tv2.setTypeface(null, Typeface.NORMAL);
//                                    tv2.setPadding(5, 0, 0, 0);
//                                    tv2.setBackgroundResource(R.drawable.cell_shape);
//                                    //text = cursor.getString(1);
//                                    tv2.setText(total1quan);
//                                    row.addView(tv2);
//                                } while (modcursor.moveToNext());
//                            }
//
//                            Cursor cursorr11 = db.rawQuery("SELECT SUM(total) FROM Billnumber WHERE date >= '" + editText1.getText().toString() + "' AND date <='" + editText2.getText().toString() + "' ", null);
//                            if (cursorr11.moveToFirst()) {
//                                level = cursorr11.getInt(0);
//                                total = String.valueOf(level);
//                            }
//
//                            float perc = Float.parseFloat(total1) * 100 / Float.parseFloat(total);
//                            String percen = String.format("%.2f", perc);
//
//                            final TextView tv3 = new TextView(getActivity());
//                            tv3.setLayoutParams(new TableRow.LayoutParams(170, ViewGroup.LayoutParams.MATCH_PARENT));
//                            //tv.setBackgroundResource(R.drawable.cell_shape);
//                            tv3.setGravity(Gravity.CENTER);
//                            tv3.setTextSize(15);
//                            tv3.setTypeface(null, Typeface.NORMAL);
//                            tv3.setPadding(5, 0, 0, 0);
//                            tv3.setBackgroundResource(R.drawable.cell_shape);
//                            //text = cursor.getString(1);
//                            tv3.setText(percen);
//                            row.addView(tv3);
//
//                            ContentValues contentValues = new ContentValues();
//                            contentValues.put("modno", String.valueOf(id));
//                            contentValues.put("modname", itemtotal);
//                            contentValues.put("sales", total1);
//                            contentValues.put("salespercentage", percen);
//                            contentValues.put("modtotalquan", total1quan);
//                            db.insert("Itemwiseorderlistmodifiers", null, contentValues);
//                        //}while (cursor.moveToNext());
//                    }
//                }
            }
        });
//            final String itemmod = getlisting.getSelectedItem().toString();
//
//            final Spinner getSorting1 = (Spinner)rootview.findViewById(R.id.chocolate_spinner);
//
//            ArrayList<String> my_tax = new ArrayList<String>();
//            my_tax.add("All");
////                    db = getActivity().openOrCreateDatabase("mydb", Context.MODE_PRIVATE, null);
//            Cursor alltaxes = db.rawQuery("SELECT * FROM Itemwiseorderlistitems ORDER BY itemname", null);
//            if (alltaxes.moveToFirst()) {
//                do {
//
//                    String ID = alltaxes.getString(0);
//                    String NAME = alltaxes.getString(2);
//                    String PLACE = alltaxes.getString(2);
//                    my_tax.add(NAME);
//
//                } while (alltaxes.moveToNext());
//            }
//            alltaxes.close();
//            ArrayAdapter my_AdapterTax = new ArrayAdapter(getActivity(), R.layout.spinner_item_orderlist,
//                    my_tax);
//            my_AdapterTax.setDropDownViewResource(R.layout.spinner_row_orderlist);
//            getSorting1.setAdapter(my_AdapterTax);
//
//
//            final Spinner hightolow1 = (Spinner)rootview.findViewById(R.id.chocolate_hightolow);
//
//
//            getSorting1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    String sort = parent.getItemAtPosition(position).toString();
//                    if (sort.equals("All")) {
//                        final String selectQuery = "SELECT * FROM Itemwiseorderlistitems ORDER BY sales";
//
//                        cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
//                        // The desired columns to be bound
//                        final String[] fromFieldNames = {"itemno", "itemname", "sales", "salespercentage", "itemtotalquan"};
//                        // the XML defined views which the data will be bound to
//                        final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity};
//                        //Log.e("Checamos que hay id", String.valueOf(R.id.name));
//                        adapter = new ImageCursorAdapter6(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
//                        listView.setAdapter(adapter);
//                    } else {
//                        final String selectQuery = "SELECT * FROM Itemwiseorderlistitems itemname WHERE itemname = '" + sort + "'";
//
//                        cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
//                        // The desired columns to be bound
//                        final String[] fromFieldNames = {"itemno", "itemname", "sales", "salespercentage", "itemtotalquan"};
//                        // the XML defined views which the data will be bound to
//                        final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity};
//                        //Log.e("Checamos que hay id", String.valueOf(R.id.name));
//                        adapter = new ImageCursorAdapter6(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
//                        listView.setAdapter(adapter);
//                    }
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//
//
//
//
//            hightolow1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    String tee = getlisting.getSelectedItem().toString();
//                    String sort = getsorting.getSelectedItem().toString();
//                    String highlow = hightolow1.getSelectedItem().toString();
//
//                    if (sort.equals("All") && highlow.equals("High to low")) {
//                        final String selectQuery = "SELECT * FROM Itemwiseorderlistitems ORDER BY sales DESC ";
//
//                        cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
//                        // The desired columns to be bound
//                        final String[] fromFieldNames = {"itemno", "itemname", "sales", "salespercentage", "itemtotalquan"};
//                        // the XML defined views which the data will be bound to
//                        final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity};
//                        //Log.e("Checamos que hay id", String.valueOf(R.id.name));
//                        adapter = new ImageCursorAdapter6(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
//                        listView.setAdapter(adapter);
//                    }
//                    if (sort.equals("All") && highlow.equals("Low to high")) {
//                        final String selectQuery = "SELECT * FROM Itemwiseorderlistitems ORDER BY sales ASC ";
//
//                        cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
//                        // The desired columns to be bound
//                        final String[] fromFieldNames = {"itemno", "itemname", "sales", "salespercentage", "itemtotalquan"};
//                        // the XML defined views which the data will be bound to
//                        final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity};
//                        //Log.e("Checamos que hay id", String.valueOf(R.id.name));
//                        adapter = new ImageCursorAdapter6(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
//                        listView.setAdapter(adapter);
//                    }
//                    if (!sort.equals("All")) {
//                        final String selectQuery = "SELECT * FROM Itemwiseorderlistitems itemname WHERE itemname = '" + sort + "'";
//
//                        cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
//                        // The desired columns to be bound
//                        final String[] fromFieldNames = {"itemno", "itemname", "sales", "salespercentage", "itemtotalquan"};
//                        // the XML defined views which the data will be bound to
//                        final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity};
//                        //Log.e("Checamos que hay id", String.valueOf(R.id.name));
//                        adapter = new ImageCursorAdapter6(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
//                        listView.setAdapter(adapter);
//                    }
//
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//
//            Cursor one = db.rawQuery("SELECT MAX(sales) FROM Itemwiseorderlistitems ", null);
//            if (one.moveToFirst()) {
//                int one11 = one.getInt(0);
//                one = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE sales = '" + one11 + "' ", null);
//                if (one.moveToFirst()) {
//                    one1 = one.getString(2);
//                    pone = one.getString(4);
//                    topone.setText(one1);
//                    topitem1percent.setText(pone);
//                    topitem1percent.append("%");
////                                    one = db.rawQuery("SELECT MAX(sales) FROM Itemwiseorderlistitems WHERE MAX(sales)<'"+one11+"' ", null);
////                                    if (one.moveToFirst()){
////                                        two2 = one.getString(2);
////                                        toptwo.setText(two2);
////                                    }
//                    Cursor two = db.rawQuery("SELECT MAX(sales) FROM Itemwiseorderlistitems WHERE sales < '" + one11 + "' ", null);
//                    if (two.moveToFirst()) {
//                        int two111 = two.getInt(0);
//                        two = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE sales = '" + two111 + "' ", null);
//                        if (two.moveToFirst()) {
//                            two2 = two.getString(2);
//                            ptwo = two.getString(4);
//                            toptwo.setText(two2);
//                            topitem2percent.setText(ptwo);
//                            topitem2percent.append("%");
//
//                            Cursor three = db.rawQuery("SELECT MAX(sales) FROM Itemwiseorderlistitems WHERE sales < '" + two111 + "' ", null);
//                            if (three.moveToFirst()) {
//                                int three11 = three.getInt(0);
//                                three = db.rawQuery("SELECT * FROM Itemwiseorderlistitems WHERE sales = '" + three11 + "' ", null);
//                                if (three.moveToFirst()) {
//                                    three3 = three.getString(2);
//                                    pthree = three.getString(4);
//                                    topthree.setText(three3);
//                                    topitem3percent.setText(pthree);
//                                    topitem3percent.append("%");
//                                } else {
//                                    topthree.setText("NA");
//                                    topitem3percent.setText("0%");
//                                }
//                            }
//                        } else {
//                            toptwo.setText("NA");
//                            topitem2percent.setText("0%");
//                        }
//                    }
//                } else {
//                    topone.setText("NA");
//                    topitem1percent.setText("0%");
//                }
//            }
//        }


        return rootview;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.

        inflater.inflate(R.menu.itemwise_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_general:
                frag = new GenOrderlistActivity();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
                break;

            case R.id.action_userwise:
                frag = new UserwiseOrderlistActivity();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
                break;


            case R.id.action_cancellation:
                frag = new CancellationOrderlistActivity();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
                break;

            case R.id.action_discountwise:
                frag = new DiscountlistReportActivity();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
                break;

            case R.id.action_export:

                if (adapter.isEmpty()) {
                    //MenuItem bedMenuItem = menu.findItem(R.id.action_export);
                    //bedMenuItem.setVisible(false);
                    Toast.makeText(getActivity(), getString(R.string.no_report_to_export), Toast.LENGTH_SHORT).show();
                }else {
                    sdff2 = new SimpleDateFormat("ddMMMyy");
                    currentDateandTimee1 = sdff2.format(new Date());

                    Date dt = new Date();
                    sdff1 = new SimpleDateFormat("hhmmssaa");
                    timee1 = sdff1.format(dt);

                    ExportDatabaseCSVTask task = new ExportDatabaseCSVTask();
                    task.execute();
                }
                break;

            case R.id.action_exportmail:

                if (adapter.isEmpty()) {
                    //MenuItem bedMenuItem = menu.findItem(R.id.action_export);
                    //bedMenuItem.setVisible(false);
                    Toast.makeText(getActivity(), getString(R.string.no_report_to_mail), Toast.LENGTH_SHORT).show();
                }else {
                    sdff2 = new SimpleDateFormat("ddMMMyy");
                    currentDateandTimee1 = sdff2.format(new Date());

                    Date dt1 = new Date();
                    sdff1 = new SimpleDateFormat("hhmmssaa");
                    timee1 = sdff1.format(dt1);

                    db1 = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor cursor = db1.rawQuery("SELECT * FROM Companydetailss", null);
                    if (cursor.moveToFirst()) {
                        companynameis = cursor.getString(1);
                    }else {
                        companynameis = "";
                    }

                    File dbFile = getActivity().getDatabasePath("mydb_Salesdata");
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

                                db = getActivity().openOrCreateDatabase("mydb_Salesdata",
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

                    String url = "www.intuitionsoftwares.com";

                    String msg = "Product report (" + editText11.getText().toString() + " - " + editText22.getText().toString() + ")\n\n" + "Overview" + " (Detailed report attached)" +
                            "\n\nTotal sales: " + totalsalesitemwise.getText().toString() + "\n\nTop selling products:\n\n" +
                            "1." + topone.getText().toString() + " - Rs. " + salesee1 + "(" + topitem1percent.getText().toString() + ")\n" +
                            "2." + toptwo.getText().toString() + " - Rs. " + salesee2 + "(" + topitem2percent.getText().toString() + ")\n" +
                            "3." + topthree.getText().toString() + " - Rs. " + salesee3 + "(" + topitem3percent.getText().toString() + ")\n\n" +
                            "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
                            "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
                            "Powered by: " + Uri.parse(url);

////                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
////                emailIntent.setType("image/jpeg");
////                emailIntent.putExtra(Intent.EXTRA_EMAIL, u1);
////                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//
//                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//                    emailIntent.setData(Uri.parse("mailto:"));
////                emailIntent.setType("image/jpeg");
////                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{to});
////                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subj);
////                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, msg);
//
//                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, companynameis);
//                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, msg);
//
//                    emailIntent.putExtra(Intent.EXTRA_STREAM, u1);
//                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));

//                    Cursor cursor2 = db1.rawQuery("SELECT * FROM Email_setup", null);
//                    if (cursor2.moveToFirst()) {
//                        String un = cursor2.getString(1);
//                        String pwd = cursor2.getString(2);
//
//                        Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
//                        if (cursor1.moveToFirst()) {
//                            do {
//                                String unn = cursor1.getString(3);
//                                String toEmails = unn;
//                                List toEmailList = Arrays.asList(toEmails
//                                        .split("\\s*,\\s*"));
////                                Toast.makeText(getActivity(), "1 " + toEmails, Toast.LENGTH_SHORT).show();
////                                Toast.makeText(getActivity(), "2 " + toEmailList, Toast.LENGTH_SHORT).show();
//
////                                        if (em_ca.toString().equals("Gmail")) {
//                                new SendMailTask_itemwise(getActivity()).execute(un,
//                                        pwd, toEmailList, companynameis, msg, currentDateandTimee1, timee1);
////                                        }
//                            } while (cursor1.moveToNext());
//                        }
//                    }

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
                                                new SendMailTask_Yahoo_attachment_Productlist(getActivity()).execute(un,
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
                                                    new SendMailTask_Hotmail_Outlook_attachment_Productlist(getActivity()).execute(un,
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
                                                        new SendMailTask_Office365_attachment_Productlist(getActivity()).execute(un,
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
                            final Dialog dialoge = new Dialog(getActivity(), R.style.timepicker_date_dialog);
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
                                    Intent intent = new Intent(getActivity(), EmailSetup_Recipients.class);
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
                                    Intent intent = new Intent(getActivity(), EmailSetup_Recipients.class);
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
                            final Dialog dialoge = new Dialog(getActivity(), R.style.timepicker_date_dialog);
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
                                    Intent intent = new Intent(getActivity(), EmailSetup.class);
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
                                    Intent intent = new Intent(getActivity(), EmailSetup.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
//                                                                getActivity().finish();
                                    dialoge.dismiss();
                                }
                            });

                        }else {
                            //both recipient and sender not there
                            final Dialog dialoge = new Dialog(getActivity(), R.style.timepicker_date_dialog);
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
                                    Intent intent = new Intent(getActivity(), EmailSetup.class);
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
                                    Intent intent = new Intent(getActivity(), EmailSetup_Recipients.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
//                                                                getActivity().finish();
                                    dialoge.dismiss();
                                }
                            });

                        }
                    }


                }

                break;

            default:
                break;
//            case R.id.action_hightolow:
//                String itemmod = getlisting.getSelectedItem().toString();
//                if (itemmod.equals("Items")){
//                    final String selectQuery = "SELECT * FROM Itemwiseorderlistitems ORDER BY sales DESC ";
//
//                    cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
//                    // The desired columns to be bound
//                    final String[] fromFieldNames = {"itemno", "itemname", "sales", "salespercentage"};
//                    // the XML defined views which the data will be bound to
//                    final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper};
//                    //Log.e("Checamos que hay id", String.valueOf(R.id.name));
//                    adapter = new ImageCursorAdapter6(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
//                    listView.setAdapter(adapter);
//                }else {
//                    final String selectQuery = "SELECT * FROM Itemwiseorderlistmodifiers ORDER BY sales DESC";
//
//                    Cursor cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
//                    // The desired columns to be bound
//                    final String[] fromFieldNames = {"modno", "modname", "sales", "salespercentage"};
//                    // the XML defined views which the data will be bound to
//                    final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper};
//                    //Log.e("Checamos que hay id", String.valueOf(R.id.name));
//                    SimpleCursorAdapter adapter = new ImageCursorAdapter7(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
//                    listView = (ListView)getActivity().findViewById(R.id.listView11);
//                    listView.setAdapter(adapter);
//                }
//
//            case R.id.action_lowtohigh:
//                String itemmod1 = getlisting.getSelectedItem().toString();
//                if (itemmod1.equals("Items")){
//                    final String selectQuery = "SELECT * FROM Itemwiseorderlistitems ORDER BY sales ASC ";
//
//                    cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
//                    // The desired columns to be bound
//                    final String[] fromFieldNames = {"itemno", "itemname", "sales", "salespercentage"};
//                    // the XML defined views which the data will be bound to
//                    final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper};
//                    //Log.e("Checamos que hay id", String.valueOf(R.id.name));
//                    adapter = new ImageCursorAdapter6(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
//                    listView.setAdapter(adapter);
//                }else {
//                    final String selectQuery = "SELECT * FROM Itemwiseorderlistmodifiers ORDER BY sales ASC";
//
//                    Cursor cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
//                    // The desired columns to be bound
//                    final String[] fromFieldNames = {"modno", "modname", "sales", "salespercentage"};
//                    // the XML defined views which the data will be bound to
//                    final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper};
//                    //Log.e("Checamos que hay id", String.valueOf(R.id.name));
//                    SimpleCursorAdapter adapter = new ImageCursorAdapter7(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
//                    listView = (ListView)getActivity().findViewById(R.id.listView11);
//                    listView.setAdapter(adapter);
//                }
//
//


        }
        return super.onOptionsItemSelected(item);
    }


    private class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage(getString(R.string.setmessage13));
            this.dialog.show();

        }
        protected Boolean doInBackground(final String... args){

            File dbFile=getActivity().getDatabasePath("mydb_Salesdata");
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

                db = getActivity().openOrCreateDatabase("mydb_Salesdata",
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
                Toast.makeText(getActivity(), getString(R.string.export_successful), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(), getString(R.string.export_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }



    class DownloadMusicfromInternet extends AsyncTask<String, Void, Integer>{

        private ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);


        @Override
        protected Integer doInBackground(String... params) {


            final String str = getlisting.getSelectedItem().toString();
            if (str.equals("Items")) {

                Cursor cursor = db.rawQuery("Select DISTINCT * from All_Sales WHERE date >= '" + editText1.getText().toString() + "' AND date <= '" + editText2.getText().toString() + "' GROUP BY itemname ", null);//replace to cursor = dbHelper.fetchAllHotels();
                db.execSQL("delete from Itemwiseorderlistitems");
                if (cursor.moveToFirst()) {
                    do {

                        Itemtype = cursor.getString(5);

                        if (Itemtype.toString().equals("Item")) {

                            itemtotal = cursor.getString(1);

//                            final TableRow row = new TableRow(getActivity());
//                            row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
//                                    TableRow.LayoutParams.MATCH_PARENT, 1.0f));
//                            row.setGravity(Gravity.CENTER_VERTICAL);

                            db1 = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                            Cursor modcursor1 = db1.rawQuery("Select * from Items WHERE itemname = '" + itemtotal + "' ", null);
                            if (modcursor1.moveToFirst()) {

                                do {

                                    id = modcursor1.getString(0);
                                    final TextView tv0 = new TextView(getActivity());
                                    tv0.setLayoutParams(new TableRow.LayoutParams(90, ViewGroup.LayoutParams.MATCH_PARENT));
                                    //tv.setBackgroundResource(R.drawable.cell_shape);
                                    tv0.setGravity(Gravity.CENTER);
                                    tv0.setTextSize(15);
                                    tv0.setTypeface(null, Typeface.NORMAL);
                                    tv0.setPadding(5, 0, 0, 0);
                                    tv0.setBackgroundResource(R.drawable.cell_shape);
                                    //text = cursor.getString(1);
                                    tv0.setText(id);
                                    //row.addView(tv0);

                                    id = modcursor1.getString(2);
                                    final TextView tv = new TextView(getActivity());
                                    tv.setLayoutParams(new TableRow.LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT));
                                    //tv.setBackgroundResource(R.drawable.cell_shape);
                                    tv.setGravity(Gravity.CENTER);
                                    tv.setTextSize(15);
                                    tv.setTypeface(null, Typeface.NORMAL);
                                    tv.setPadding(5, 0, 0, 0);
                                    tv.setBackgroundResource(R.drawable.cell_shape);
                                    //text = cursor.getString(1);
                                    tv.setText(id);
                                    //row.addView(tv);
                                } while (modcursor1.moveToNext());
                            }
                            modcursor1.close();

                            final TextView tv1 = new TextView(getActivity());
                            tv1.setLayoutParams(new TableRow.LayoutParams(260, ViewGroup.LayoutParams.MATCH_PARENT));
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            tv1.setGravity(Gravity.CENTER);
                            tv1.setTextSize(15);
                            tv1.setTypeface(null, Typeface.NORMAL);
                            tv1.setPadding(5, 0, 0, 0);
                            tv1.setBackgroundResource(R.drawable.cell_shape);
                            //text = cursor.getString(1);
                            tv1.setText(itemtotal);
                            //row.addView(tv1);

                            Cursor modcursor = db.rawQuery("Select SUM(total) from All_Sales WHERE itemname = '" + itemtotal + "' AND date >= '" + editText1.getText().toString() + "' AND date <='" + editText2.getText().toString() + "' GROUP BY itemname ", null);
                            if (modcursor.moveToFirst()) {

                                do {
                                    level = modcursor.getInt(0);
                                    total1 = String.valueOf(level);
                                    final TextView tv2 = new TextView(getActivity());
                                    tv2.setLayoutParams(new TableRow.LayoutParams(150, ViewGroup.LayoutParams.MATCH_PARENT));
                                    //tv.setBackgroundResource(R.drawable.cell_shape);
                                    tv2.setGravity(Gravity.CENTER);
                                    tv2.setTextSize(15);
                                    tv2.setTypeface(null, Typeface.NORMAL);
                                    tv2.setPadding(5, 0, 0, 0);
                                    tv2.setBackgroundResource(R.drawable.cell_shape);
                                    //text = cursor.getString(1);
                                    tv2.setText(total1);
                                    //row.addView(tv2);
                                } while (modcursor.moveToNext());
                            }
                            modcursor.close();

                            Cursor modcursorsor = db.rawQuery("Select SUM(quantity) from All_Sales WHERE itemname = '" + itemtotal + "' AND date >= '" + editText1.getText().toString() + "' AND date <='" + editText2.getText().toString() + "' GROUP BY itemname ", null);
                            if (modcursorsor.moveToFirst()) {

                                do {
                                    float level = modcursorsor.getFloat(0);
                                    total1quan = String.valueOf(level);
                                    final TextView tv2 = new TextView(getActivity());
                                    tv2.setLayoutParams(new TableRow.LayoutParams(150, ViewGroup.LayoutParams.MATCH_PARENT));
                                    //tv.setBackgroundResource(R.drawable.cell_shape);
                                    tv2.setGravity(Gravity.CENTER);
                                    tv2.setTextSize(15);
                                    tv2.setTypeface(null, Typeface.NORMAL);
                                    tv2.setPadding(5, 0, 0, 0);
                                    tv2.setBackgroundResource(R.drawable.cell_shape);
                                    //text = cursor.getString(1);
                                    tv2.setText(total1quan);
                                    //row.addView(tv2);
                                } while (modcursorsor.moveToNext());
                            }
                            modcursorsor.close();


                            Cursor cursorr11 = db.rawQuery("SELECT SUM(subtotal) FROM Billnumber WHERE date >= '" + editText1.getText().toString() + "' AND date <='" + editText2.getText().toString() + "' ", null);
                            if (cursorr11.moveToFirst()) {
                                level = cursorr11.getInt(0);
                                total = String.valueOf(level);
                            }

                            float perc = Float.parseFloat(total1) * 100 / Float.parseFloat(total);
                            String percen = String.format("%.2f", perc);

                            final TextView tv3 = new TextView(getActivity());
                            tv3.setLayoutParams(new TableRow.LayoutParams(170, ViewGroup.LayoutParams.MATCH_PARENT));
                            //tv.setBackgroundResource(R.drawable.cell_shape);
                            tv3.setGravity(Gravity.CENTER);
                            tv3.setTextSize(15);
                            tv3.setTypeface(null, Typeface.NORMAL);
                            tv3.setPadding(5, 0, 0, 0);
                            tv3.setBackgroundResource(R.drawable.cell_shape);
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
                Cursor cursor = db.rawQuery("Select DISTINCT * from All_Sales WHERE date >= '" + editText1.getText().toString() + "' AND date <='" + editText2.getText().toString() + "' AND type = 'Modifier' GROUP BY itemname ", null);//replace to cursor = dbHelper.fetchAllHotels();
                db.execSQL("delete from Itemwiseorderlistmodifiers");
                //Toast.makeText(getActivity(), "Modifiersrsrsrsrsrsrsrs", Toast.LENGTH_SHORT).show();
                if (cursor.moveToFirst()) {
                    do {
                        //Toast.makeText(getActivity(), "Modifiersrsrsrsrsrsrsrs111", Toast.LENGTH_SHORT).show();
                        //do {
                        //Toast.makeText(getActivity(), "Modifiersrsrsrsrsrsrsrs222", Toast.LENGTH_SHORT).show();

                        String itemtotal = cursor.getString(1);

//                        final TableRow row = new TableRow(getActivity());
//                        row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
//                                TableRow.LayoutParams.MATCH_PARENT, 1.0f));
//                        row.setGravity(Gravity.CENTER_VERTICAL);

                        db1 = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        Cursor modcursor1 = db1.rawQuery("Select * from Modifiers WHERE modifiername = '" + itemtotal + "' ", null);

                        if (modcursor1.moveToFirst()) {
                            do {

                                id = modcursor1.getString(0);
//                                final TextView tv0 = new TextView(getActivity());
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
//                                final TextView tv = new TextView(getActivity());
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

//                        final TextView tv1 = new TextView(getActivity());
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

                        Cursor modcursor = db.rawQuery("Select SUM(total) from All_Sales WHERE itemname = '" + itemtotal + "' AND date >= '" + editText1.getText().toString() + "' AND date <='" + editText2.getText().toString() + "' GROUP BY itemname ", null);

                        if (modcursor.moveToFirst()) {
                            do {
                                level = modcursor.getInt(0);
                                total1 = String.valueOf(level);
//                                final TextView tv2 = new TextView(getActivity());
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

                        Cursor modcursorsor = db.rawQuery("Select SUM(quantity) from All_Sales WHERE itemname = '" + itemtotal + "' AND date >= '" + editText1.getText().toString() + "' AND date <='" + editText2.getText().toString() + "' GROUP BY itemname ", null);
                        if (modcursorsor.moveToFirst()) {

                            do {
                                float level = modcursorsor.getFloat(0);
                                total1quan = String.valueOf(level);
//                                final TextView tv2 = new TextView(getActivity());
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

                        Cursor cursorr11 = db.rawQuery("SELECT SUM(total) FROM Billnumber WHERE date >= '" + editText1.getText().toString() + "' AND date <='" + editText2.getText().toString() + "' ", null);
                        if (cursorr11.moveToFirst()) {
                            level = cursorr11.getInt(0);
                            total = String.valueOf(level);
                        }

                        float perc = Float.parseFloat(total1) * 100 / Float.parseFloat(total);
                        String percen = String.format("%.2f", perc);

//                        final TextView tv3 = new TextView(getActivity());
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
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();

            //dialog.dismiss();

            final Spinner getitemwise = (Spinner)getActivity().findViewById(R.id.chocolate_itemwise);

            final Spinner getSorting1 = (Spinner)getActivity().findViewById(R.id.chocolate_spinner);

            final Spinner hightolow1 = (Spinner)getActivity().findViewById(R.id.chocolate_hightolow);

            if (getitemwise.getSelectedItem().toString().equals("Items")){
                listView.setAdapter(null);
                ArrayList<String> my_tax = new ArrayList<String>();
                my_tax.add("All");
//                    db = getActivity().openOrCreateDatabase("mydb", Context.MODE_PRIVATE, null);
                Cursor alltaxes = db.rawQuery("SELECT * FROM Itemwiseorderlistitems GROUP BY itemname ORDER BY itemname", null);
                if (alltaxes.moveToFirst()) {
                    do {

                        String ID = alltaxes.getString(0);
                        String NAME = alltaxes.getString(2);
                        String PLACE = alltaxes.getString(2);
                        my_tax.add(NAME);

                    } while (alltaxes.moveToNext());
                }
                alltaxes.close();
                ArrayAdapter my_AdapterTax = new ArrayAdapter(getActivity(), R.layout.spinner_item_orderlist,
                        my_tax);
                my_AdapterTax.setDropDownViewResource(R.layout.spinner_row_orderlist);
                getSorting1.setAdapter(my_AdapterTax);





                getSorting1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String sort = parent.getItemAtPosition(position).toString();
                        if (sort.equals("All") && hightolow1.getSelectedItem().toString().equals("High to low")) {
                            final String selectQuery = "SELECT * FROM Itemwiseorderlistitems GROUP BY itemname ORDER BY sales DESC";

                            cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                            // The desired columns to be bound
                            final String[] fromFieldNames = {"itemname", "itemno", "sales", "salespercentage", "itemtotalquan"};
                            // the XML defined views which the data will be bound to
                            final int[] toViewsID = {R.id.itemname, R.id.num, R.id.sales, R.id.salesper, R.id.quantity};
                            //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                            adapter = new ImageCursorAdapter6(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
                            listView.setAdapter(adapter);
                            //Toast.makeText(getActivity(), "1", Toast.LENGTH_LONG).show();
                        } else {
                            if (sort.equals("All") && hightolow1.getSelectedItem().toString().equals("Low to high")) {
                                final String selectQuery = "SELECT * FROM Itemwiseorderlistitems GROUP BY itemname ORDER BY sales ASC ";

                                cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                                // The desired columns to be bound
                                final String[] fromFieldNames = {"itemname", "itemno", "sales", "salespercentage", "itemtotalquan"};
                                // the XML defined views which the data will be bound to
                                final int[] toViewsID = {R.id.itemname, R.id.num, R.id.sales, R.id.salesper, R.id.quantity};
                                //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                                adapter = new ImageCursorAdapter6(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
                                listView.setAdapter(adapter);
                            }else {
                                final String selectQuery = "SELECT * FROM Itemwiseorderlistitems WHERE itemname = '" + sort + "'";

                                cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                                // The desired columns to be bound
                                final String[] fromFieldNames = {"itemname", "itemno", "sales", "salespercentage", "itemtotalquan"};
                                // the XML defined views which the data will be bound to
                                final int[] toViewsID = {R.id.itemname, R.id.num, R.id.sales, R.id.salesper, R.id.quantity};
                                //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                                adapter = new ImageCursorAdapter6(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
                                listView.setAdapter(adapter);
                                //Toast.makeText(getActivity(), "11", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });




                hightolow1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String tee = getlisting.getSelectedItem().toString();
                        String sort = getsorting.getSelectedItem().toString();
                        String highlow = hightolow1.getSelectedItem().toString();

                        if (sort.equals("All") && highlow.equals("High to low")) {
                            final String selectQuery = "SELECT * FROM Itemwiseorderlistitems GROUP BY itemname ORDER BY sales DESC ";

                            cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                            // The desired columns to be bound
                            final String[] fromFieldNames = {"itemname", "itemno", "sales", "salespercentage", "itemtotalquan"};
                            // the XML defined views which the data will be bound to
                            final int[] toViewsID = {R.id.itemname, R.id.num, R.id.sales, R.id.salesper, R.id.quantity};
                            //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                            adapter = new ImageCursorAdapter6(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
                            listView.setAdapter(adapter);
                            //Toast.makeText(getActivity(), "3", Toast.LENGTH_LONG).show();
                        }
                        if (sort.equals("All") && highlow.equals("Low to high")) {
                            final String selectQuery = "SELECT * FROM Itemwiseorderlistitems GROUP BY itemname ORDER BY sales ASC ";

                            cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                            // The desired columns to be bound
                            final String[] fromFieldNames = {"itemname", "itemno", "sales", "salespercentage", "itemtotalquan"};
                            // the XML defined views which the data will be bound to
                            final int[] toViewsID = {R.id.itemname, R.id.num, R.id.sales, R.id.salesper, R.id.quantity};
                            //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                            adapter = new ImageCursorAdapter6(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
                            listView.setAdapter(adapter);
                            //Toast.makeText(getActivity(), "4", Toast.LENGTH_LONG).show();
                        }
                        if (!sort.equals("All")) {
                            final String selectQuery = "SELECT * FROM Itemwiseorderlistitems WHERE itemname = '" + sort + "'";

                            cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                            // The desired columns to be bound
                            final String[] fromFieldNames = {"itemname", "itemno", "sales", "salespercentage", "itemtotalquan"};
                            // the XML defined views which the data will be bound to
                            final int[] toViewsID = {R.id.itemname, R.id.num, R.id.sales, R.id.salesper, R.id.quantity};
                            //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                            adapter = new ImageCursorAdapter6(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
                            listView.setAdapter(adapter);
                            //Toast.makeText(getActivity(), "5", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

//                        if (sort.equals("All") && highlow.equals("High to low")) {
//                            final String selectQuery = "SELECT * FROM Itemwiseorderlistitems ORDER BY sales DESC ";
//
//                            cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
//                            // The desired columns to be bound
//                            final String[] fromFieldNames = {"itemno", "itemname", "sales", "salespercentage", "itemtotalquan"};
//                            // the XML defined views which the data will be bound to
//                            final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity};
//                            //Log.e("Checamos que hay id", String.valueOf(R.id.name));
//                            adapter = new ImageCursorAdapter6(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
//                            listView.setAdapter(adapter);
//                        } else {
//                            if (sort.equals("All") && highlow.equals("Low to high")) {
//                                final String selectQuery = "SELECT * FROM Itemwiseorderlistitems ORDER BY sales ASC ";
//
//                                cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
//                                // The desired columns to be bound
//                                final String[] fromFieldNames = {"itemno", "itemname", "sales", "salespercentage", "itemtotalquan"};
//                                // the XML defined views which the data will be bound to
//                                final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity};
//                                //Log.e("Checamos que hay id", String.valueOf(R.id.name));
//                                adapter = new ImageCursorAdapter6(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
//                                listView.setAdapter(adapter);
//                            } else {
//                                final String selectQuery = "SELECT * FROM Itemwiseorderlistitems itemname WHERE itemname = '" + sort + "'";
//
//                                cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
//                                // The desired columns to be bound
//                                final String[] fromFieldNames = {"itemno", "itemname", "sales", "salespercentage", "itemtotalquan"};
//                                // the XML defined views which the data will be bound to
//                                final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity};
//                                //Log.e("Checamos que hay id", String.valueOf(R.id.name));
//                                adapter = new ImageCursorAdapter6(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
//                                listView.setAdapter(adapter);
//                            }
//                        }

                topone.setText("NA");
                toptwo.setText("NA");
                topthree.setText("NA");
                topitem1percent.setText("0%");
                topitem2percent.setText("0%");
                topitem3percent.setText("0%");


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
            }

            if (getitemwise.getSelectedItem().toString().equals("Modifiers")){
                listView.setAdapter(null);
                ArrayList<String> my_tax = new ArrayList<String>();
                my_tax.add("All");
//                    db = getActivity().openOrCreateDatabase("mydb", Context.MODE_PRIVATE, null);
                Cursor alltaxes = db.rawQuery("SELECT * FROM Itemwiseorderlistmodifiers GROUP BY modname ORDER BY modname", null);
                if (alltaxes.moveToFirst()) {
                    do {

                        String ID = alltaxes.getString(0);
                        String NAME = alltaxes.getString(2);
                        String PLACE = alltaxes.getString(2);
                        my_tax.add(NAME);

                    } while (alltaxes.moveToNext());
                }
                alltaxes.close();
                ArrayAdapter my_AdapterTax = new ArrayAdapter(getActivity(), R.layout.spinner_item_orderlist,
                        my_tax);
                my_AdapterTax.setDropDownViewResource(R.layout.spinner_row_orderlist);
                getSorting1.setAdapter(my_AdapterTax);

                getSorting1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String sort = parent.getItemAtPosition(position).toString();
                        if (sort.equals("All")) {
                            final String selectQuery = "SELECT * FROM Itemwiseorderlistmodifiers GROUP BY modname ORDER BY sales DESC ";

                            cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                            // The desired columns to be bound
                            final String[] fromFieldNames = {"modname", "modno", "sales", "salespercentage", "modtotalquan"};
                            // the XML defined views which the data will be bound to
                            final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity};
                            //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                            adapter = new ImageCursorAdapter7(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
                            listView.setAdapter(adapter);
                        } else {
                            final String selectQuery = "SELECT * FROM Itemwiseorderlistmodifiers GROUP BY modname WHERE modname = '" + sort + "'";

                            cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                            // The desired columns to be bound
                            final String[] fromFieldNames = {"modname", "modno", "sales", "salespercentage", "modtotalquan"};
                            // the XML defined views which the data will be bound to
                            final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity};
                            //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                            adapter = new ImageCursorAdapter7(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
                            listView.setAdapter(adapter);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                //final Spinner hightolow1 = (Spinner)rootview.findViewById(R.id.chocolate_hightolow);


                hightolow1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String tee = getlisting.getSelectedItem().toString();
                        String sort = getsorting.getSelectedItem().toString();
                        String highlow = hightolow1.getSelectedItem().toString();

                        if (sort.equals("All") && highlow.equals("High to low")) {
                            final String selectQuery = "SELECT * FROM Itemwiseorderlistmodifiers GROUP BY modname ORDER BY sales DESC ";

                            cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                            // The desired columns to be bound
                            final String[] fromFieldNames = {"modname", "modno", "sales", "salespercentage", "modtotalquan"};
                            // the XML defined views which the data will be bound to
                            final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity};
                            //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                            adapter = new ImageCursorAdapter7(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
                            listView.setAdapter(adapter);
                        } else {
                            if (sort.equals("All") && highlow.equals("Low to high")) {
                                final String selectQuery = "SELECT * FROM Itemwiseorderlistmodifiers GROUP BY modname ORDER BY sales ASC ";

                                cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                                // The desired columns to be bound
                                final String[] fromFieldNames = {"modname", "modno", "sales", "salespercentage", "modtotalquan"};
                                // the XML defined views which the data will be bound to
                                final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity};
                                //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                                adapter = new ImageCursorAdapter7(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
                                listView.setAdapter(adapter);
                            } else {
                                final String selectQuery = "SELECT * FROM Itemwiseorderlistmodifiers GROUP BY modname WHERE modname = '" + sort + "'";

                                cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
                                // The desired columns to be bound
                                final String[] fromFieldNames = {"modname", "modno", "sales", "salespercentage", "modtotalquan"};
                                // the XML defined views which the data will be bound to
                                final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity};
                                //Log.e("Checamos que hay id", String.valueOf(R.id.name));
                                adapter = new ImageCursorAdapter7(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
                                listView.setAdapter(adapter);
                            }
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });




//                                    if (sort.equals("All")) {
//                                        final String selectQuery = "SELECT * FROM Itemwiseorderlistmodifiers";
//
//                                        Cursor cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
//                                        // The desired columns to be bound
//                                        final String[] fromFieldNames = {"modno", "modname", "sales", "salespercentage", "modtotalquan"};
//                                        // the XML defined views which the data will be bound to
//                                        final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity};
//                                        //Log.e("Checamos que hay id", String.valueOf(R.id.name));
//                                        SimpleCursorAdapter adapter = new ImageCursorAdapter7(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
//                                        listView.setAdapter(adapter);
//                                    } else {
//                                        final String selectQuery = "SELECT * FROM Itemwiseorderlistmodifiers WHERE modname = '" + sort + "'";
//
//                                        Cursor cursor1 = db.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
//                                        // The desired columns to be bound
//                                        final String[] fromFieldNames = {"modno", "modname", "sales", "salespercentage", "modtotalquan"};
//                                        // the XML defined views which the data will be bound to
//                                        final int[] toViewsID = {R.id.num, R.id.itemname, R.id.sales, R.id.salesper, R.id.quantity};
//                                        //Log.e("Checamos que hay id", String.valueOf(R.id.name));
//                                        SimpleCursorAdapter adapter = new ImageCursorAdapter7(getActivity(), R.layout.itemwise_boxes, cursor1, fromFieldNames, toViewsID);
//                                        listView.setAdapter(adapter);
//                                    }

                topone.setText("NA");
                toptwo.setText("NA");
                topthree.setText("NA");
                topitem1percent.setText("0%");
                topitem2percent.setText("0%");
                topitem3percent.setText("0%");
                Cursor one = db.rawQuery("SELECT MAX(sales) FROM Itemwiseorderlistmodifiers ", null);
                if (one.moveToFirst()) {
                    int one11 = one.getInt(0);
                    one = db.rawQuery("SELECT * FROM Itemwiseorderlistmodifiers WHERE sales = '" + one11 + "' ", null);
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
                        Cursor two = db.rawQuery("SELECT MAX(sales) FROM Itemwiseorderlistmodifiers WHERE sales < '" + one11 + "' ", null);
                        if (two.moveToFirst()) {
                            int two111 = two.getInt(0);
                            two = db.rawQuery("SELECT * FROM Itemwiseorderlistmodifiers WHERE sales = '" + two111 + "' ", null);
                            if (two.moveToFirst()) {
                                two2 = two.getString(2);
                                //salesee2 = one.getString(3);
                                ptwo = two.getString(4);
                                toptwo.setText(two2);
                                topitem2percent.setText(ptwo);
                                topitem2percent.append("%");

                                Cursor three = db.rawQuery("SELECT MAX(sales) FROM Itemwiseorderlistmodifiers WHERE sales < '" + two111 + "' ", null);
                                if (three.moveToFirst()) {
                                    int three11 = three.getInt(0);
                                    three = db.rawQuery("SELECT * FROM Itemwiseorderlistmodifiers WHERE sales = '" + three11 + "' ", null);
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
            }
            dialog.dismiss();

        }
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
                    TextView edtToAddress = new TextView(getActivity());
                    edtToAddress.setText(unn);

                    TextView edtSubject = new TextView(getActivity());
                    edtSubject.setText(strcompanyname);

                    TextView edtMessage = new TextView(getActivity());
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
                Toast.makeText(getActivity(), "not success", Toast.LENGTH_SHORT).show();
//                showMessage(view, "No results returned.");
            } else {
                Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
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
                getActivity(),
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private void getResultsFromApi() {

        Cursor cursorr = db1.rawQuery("SELECT * FROM Email_setup", null);
        if (cursorr.moveToFirst()) {
            String unn = cursorr.getString(1);
//            Toast.makeText(getActivity(), "a4 " + unn, Toast.LENGTH_SHORT).show();

            TextView tvv = new TextView(getActivity());
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
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
                apiAvailability.isGooglePlayServicesAvailable(getActivity());
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
                apiAvailability.isGooglePlayServicesAvailable(getActivity());
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
                getActivity(), Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getActivity().getPreferences(Context.MODE_PRIVATE)
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
                    Manifest.permission.GET_ACCOUNTS);
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