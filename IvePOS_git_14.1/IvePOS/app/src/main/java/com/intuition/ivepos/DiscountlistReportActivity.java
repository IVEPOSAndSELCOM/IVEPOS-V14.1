package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 1/6/2015.
 */

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
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

/**
 * Created by Rohithkumar on 1/6/2015.
 */
public class DiscountlistReportActivity extends Fragment {

    Fragment frag;
    FragmentTransaction fragTransaction;

    TextView discounttotal;
    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;
    int level, levelgg;
    String total, totalgg, ruppercent1;

    Cursor cursor;
    SimpleCursorAdapter dataAdapterr;
    ListView listview;

    File file=null;
    SimpleDateFormat sdff2, sdff1;
    String currentDateandTimee1;
    String timee1;

    String companynameis;
    TextView editText1, editText2, editText11, editText22;
    String onee, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve;
    String onee1, two1, three1, four1, five1, six1, seven1, eight1, nine1, ten1, eleven1, twelve1;
    private int year, year1;
    private int month, month1;
    private int day, day1;
    int clickcount=1, clickcounts = 1;
    Spinner getlisting;
    Button btnok;
    RelativeLayout sales, product, seller, refund, discount1;


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




    public DiscountlistReportActivity(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View rootview = inflater.inflate(R.layout.fragment_multi_discountlistreport1, null);

        if (getActivity() instanceof AppCompatActivity){
            androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionbar.setSubtitle("Discount");
        }


        mCredential = GoogleAccountCredential.usingOAuth2(
                getActivity().getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage(getString(R.string.setmessage4));

//

        //ActionBar actionBar = getActivity().getActionBar();


        //actionBar.setTitle("Discount list");

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
                        datePickerListener, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                //clickcounts++;


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
                TextView mEdit1 = (TextView) rootview.findViewById(R.id.editText11);
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
                        datePickerListener, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
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
                TextView mEdit1 = (TextView) rootview.findViewById(R.id.editText22);
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

        discounttotal = (TextView)rootview.findViewById(R.id.totaldiscount);

        getlisting = (Spinner)rootview.findViewById(R.id.chocolate_spinner);

        db = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        Cursor cursor11 = db.rawQuery("SELECT SUM(Discount_rupees) FROM Discountdetails WHERE date >= '"+editText1.getText().toString()+"' AND date <='"+editText2.getText().toString()+"'", null);
        if (cursor11.moveToFirst()) {
            level = cursor11.getInt(0);
            total = String.valueOf(level);
        }

        String rs = String.valueOf(R.string.insert);
        discounttotal.setText(R.string.Rs);
        discounttotal.append(total);

        final EditText myFilter = (EditText)rootview.findViewById(R.id.searchView);



        final String Text = getlisting.getSelectedItem().toString();

        if (Text.equals("Ascending")){
            db = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("Select * from Discountdetails WHERE date >= '"+editText1.getText().toString()+"' AND date <='"+editText2.getText().toString()+"' ORDER BY Discount_rupees ASC", null);//replace to cursor = dbHelper.fetchAllHotels();
            // The desired columns to be bound
            String[] fromFieldNames = {"_id", "date1", "time", "billno", "Discountcodeno", "Discount_percent", "Billamount_rupess", "Discount_rupees"};
            // the XML defined views which the data will be bound to
            int[] toViewsID = {R.id.sno, R.id.date, R.id.time, R.id.billno, R.id.discountcode, R.id.discountpercent, R.id.billamount, R.id.discountrs};
            Log.e("Checamos que hay id", String.valueOf(R.id.name));
            dataAdapterr = new SimpleCursorAdapter(getActivity(), R.layout.discount_list, cursor, fromFieldNames, toViewsID, 0);
            listview = (ListView) rootview.findViewById(R.id.listView);
            listview.setAdapter(dataAdapterr);// Assign adapter to ListView.... here... the bitch error
        }else {
            db = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("Select * from Discountdetails WHERE date >= '"+editText1.getText().toString()+"' AND date <='"+editText2.getText().toString()+"' ORDER BY Discount_rupees DESC", null);//replace to cursor = dbHelper.fetchAllHotels();
            // The desired columns to be bound
            String[] fromFieldNames = {"_id", "date1", "time", "billno", "Discountcodeno", "Discount_percent", "Billamount_rupess", "Discount_rupees"};
            // the XML defined views which the data will be bound to
            int[] toViewsID = {R.id.sno, R.id.date, R.id.time, R.id.billno, R.id.discountcode, R.id.discountpercent, R.id.billamount, R.id.discountrs};
            Log.e("Checamos que hay id", String.valueOf(R.id.name));
            dataAdapterr = new SimpleCursorAdapter(getActivity(), R.layout.discount_list, cursor, fromFieldNames, toViewsID, 0);
            listview = (ListView) rootview.findViewById(R.id.listView);
            listview.setAdapter(dataAdapterr);// Assign adapter to ListView.... here... the bitch error
        }



        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                db = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor cursor11_1 = db.rawQuery("SELECT SUM(Discount_rupees) FROM Discountdetails WHERE date >= '"+editText1.getText().toString()+"' AND date <='"+editText2.getText().toString()+"' AND billno LIKE '%" + myFilter.getText().toString() + "%' OR Discountcodeno LIKE '%" + myFilter.getText().toString() + "%'", null);
                if (cursor11_1.moveToFirst()) {
                    float level = cursor11_1.getFloat(0);
                    String total = String.valueOf(level);

                    discounttotal.setText(R.string.Rs);
                    discounttotal.append(total);
                }
                dataAdapterr.getFilter().filter(s.toString());
            }
        });

        ImageView deleteicon = (ImageView)rootview.findViewById(R.id.delete_icon);
        deleteicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFilter.setText("");
            }
        });

        dataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {

                return fetchCountriesByName(constraint.toString());
            }
        });


        btnok = (Button)rootview.findViewById(R.id.okok);

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor cursor11 = db.rawQuery("SELECT SUM(Discount_rupees) FROM Discountdetails WHERE date >= '"+editText1.getText().toString()+"' AND date <='"+editText2.getText().toString()+"'", null);
                if (cursor11.moveToFirst()) {
                    level = cursor11.getInt(0);
                    total = String.valueOf(level);
                }

                String rs = String.valueOf(R.string.insert);
                discounttotal.setText(R.string.Rs);
                discounttotal.append(total);

                final EditText myFilter = (EditText)rootview.findViewById(R.id.searchView);

                db = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                Cursor cursor11_1 = db.rawQuery("SELECT SUM(Discount_rupees) FROM Discountdetails WHERE date >= '"+editText1.getText().toString()+"' AND date <='"+editText2.getText().toString()+"' AND billno LIKE '%" + myFilter.getText().toString() + "%' OR Discountcodeno LIKE '%" + myFilter.getText().toString() + "%'", null);
                if (cursor11_1.moveToFirst()) {
                    float level = cursor11_1.getFloat(0);
                    String total = String.valueOf(level);

                    discounttotal.setText(R.string.Rs);
                    discounttotal.append(total);
                }

                final String Text = getlisting.getSelectedItem().toString();
                if (Text.equals("Ascending")){
                    db = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    cursor = db.rawQuery("Select * from Discountdetails WHERE date >= '"+editText1.getText().toString()+"' AND date <='"+editText2.getText().toString()+"' ORDER BY Discount_rupees ASC", null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    String[] fromFieldNames = {"_id", "date1", "time", "billno", "Discountcodeno", "Discount_percent", "Billamount_rupess", "Discount_rupees"};
                    // the XML defined views which the data will be bound to
                    int[] toViewsID = {R.id.sno, R.id.date, R.id.time, R.id.billno, R.id.discountcode, R.id.discountpercent, R.id.billamount, R.id.discountrs};
                    Log.e("Checamos que hay id", String.valueOf(R.id.name));
                    dataAdapterr = new SimpleCursorAdapter(getActivity(), R.layout.discount_list, cursor, fromFieldNames, toViewsID, 0);
                    listview = (ListView) rootview.findViewById(R.id.listView);
                    listview.setAdapter(dataAdapterr);// Assign adapter to ListView.... here... the bitch error
                }else {
                    db = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    cursor = db.rawQuery("Select * from Discountdetails WHERE date >= '"+editText1.getText().toString()+"' AND date <='"+editText2.getText().toString()+"' ORDER BY Discount_rupees DESC", null);//replace to cursor = dbHelper.fetchAllHotels();
                    // The desired columns to be bound
                    String[] fromFieldNames = {"_id", "date1", "time", "billno", "Discountcodeno", "Discount_percent", "Billamount_rupess", "Discount_rupees"};
                    // the XML defined views which the data will be bound to
                    int[] toViewsID = {R.id.sno, R.id.date, R.id.time, R.id.billno, R.id.discountcode, R.id.discountpercent, R.id.billamount, R.id.discountrs};
                    Log.e("Checamos que hay id", String.valueOf(R.id.name));
                    dataAdapterr = new SimpleCursorAdapter(getActivity(), R.layout.discount_list, cursor, fromFieldNames, toViewsID, 0);
                    listview = (ListView) rootview.findViewById(R.id.listView);
                    listview.setAdapter(dataAdapterr);// Assign adapter to ListView.... here... the bitch error
                }

                myFilter.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        db = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                        Cursor cursor11_1 = db.rawQuery("SELECT SUM(Discount_rupees) FROM Discountdetails WHERE date >= '"+editText1.getText().toString()+"' AND date <='"+editText2.getText().toString()+"' AND billno LIKE '%" + myFilter.getText().toString() + "%' OR Discountcodeno LIKE '%" + myFilter.getText().toString() + "%'", null);
                        if (cursor11_1.moveToFirst()) {
                            float level = cursor11_1.getFloat(0);
                            String total = String.valueOf(level);

                            discounttotal.setText(R.string.Rs);
                            discounttotal.append(total);
                        }
                        dataAdapterr.getFilter().filter(s.toString());
                    }
                });

                ImageView deleteicon = (ImageView)rootview.findViewById(R.id.delete_icon);
                deleteicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myFilter.setText("");
                    }
                });

                dataAdapterr.setFilterQueryProvider(new FilterQueryProvider() {
                    public Cursor runQuery(CharSequence constraint) {

                        return fetchCountriesByName(constraint.toString());
                    }
                });
            }
        });

        return rootview;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.

        inflater.inflate(R.menu.discount_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_general:
                frag = new GenOrderlistActivity();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
                hideKeyboard(getContext());
                donotshowKeyboard(getActivity());
                break;

            case R.id.action_itemwise:
                frag = new ItemwiseOrderlistActivity();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
                hideKeyboard(getContext());
                donotshowKeyboard(getActivity());
                break;


            case R.id.action_userwise:
                frag = new UserwiseOrderlistActivity();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
                hideKeyboard(getContext());
                donotshowKeyboard(getActivity());
                break;

            case R.id.action_cancellation:
                frag = new CancellationOrderlistActivity();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
                hideKeyboard(getContext());
                donotshowKeyboard(getActivity());
                break;

            case R.id.action_export:

                if (dataAdapterr.isEmpty()) {
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

                hideKeyboard(getContext());
                donotshowKeyboard(getActivity());
                if (dataAdapterr.isEmpty()) {
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

                    Cursor ccursore = db1.rawQuery("SELECT * FROM Email_setup", null);
                    if (ccursore.moveToFirst()) {
                        Cursor ccursoree = db1.rawQuery("SELECT * FROM Email_recipient", null);
                        if (ccursoree.moveToFirst()) {
                            File dbFile = getActivity().getDatabasePath("mydb_Salesdata");
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
                                String arrStr1[] = {"Date", "Time", "Bill No.", "Discount code", "Discount(%)", "Discount(Rs.)", "Net sales"};
                                csvWrite.writeNext(arrStr1);

                                db = getActivity().openOrCreateDatabase("mydb_Salesdata",
                                        Context.MODE_PRIVATE, null);
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


                    db = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor cursor11 = db.rawQuery("SELECT SUM(total) FROM Billnumber", null);
                    if (cursor11.moveToFirst()) {
                        levelgg = cursor11.getInt(0);
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
                            "Total sales: Rs. " + totalgg + "\n\n" +
                            "Discounts: " + discounttotal.getText().toString() + "(" + ruppercent1 + "%)\n\n" +
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
//                                new SendMailTask_discountwise(getActivity()).execute(un,
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
                                                new SendMailTask_Yahoo_attachment_Discountlist(getActivity()).execute(un,
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
                                                    new SendMailTask_Hotmail_Outlook_attachment_Discountlist(getActivity()).execute(un,
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
                                                        new SendMailTask_Office365_attachment_Discountlist(getActivity()).execute(un,
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
                String arrStr1[] ={"Date", "Time", "Bill No.", "Discount code", "Discount(%)", "Discount(Rs.)", "Net sales"};
                csvWrite.writeNext(arrStr1);

                db = getActivity().openOrCreateDatabase("mydb_Salesdata",
                        Context.MODE_PRIVATE, null);
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
                Toast.makeText(getActivity(), getString(R.string.export_successful), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(), getString(R.string.export_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public Cursor fetchCountriesByName(String inputtext) throws SQLException {

        Cursor mCursor = null;
        if (inputtext == null  ||  inputtext.length () == 0)  {
            mCursor = db.query("Discountdetails", new String[] {"_id", "date", "time", "billno", "Discountcodeno", "Discount_percent", "Billamount_rupess", "Discount_rupees", "date1", "original_amount"},
                    null, null, null, null, null);

        }
        else {
            mCursor = db.rawQuery("SELECT * FROM Discountdetails WHERE billno LIKE '%" + inputtext + "%' OR Discountcodeno LIKE '%" + inputtext + "%'", null);
//            mCursor = db.query(true, "Discountdetails", new String[] {"_id", "date", "time", "billno", "Discountcodeno", "Discount_percent", "Billamount_rupess", "Discount_rupees", "date1", "original_amount"},
//                    "billno" +" , "+ "Discountcodeno" + " like" + " '%" + inputtext + "%'", null,
//                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public static void donotshowKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
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

            String msg = "Discount list (list attached)\n\n" +
                    "Total sales: Rs. " + totalgg + "\n\n" +
                    "Discounts: " + discounttotal.getText().toString() + "(" + ruppercent1 + "%)\n\n" +
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
