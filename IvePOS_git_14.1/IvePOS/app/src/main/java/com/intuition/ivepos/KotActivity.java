package com.intuition.ivepos;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import au.com.bytecode.opencsv.CSVWriter;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class KotActivity extends Fragment {

    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;
    View rootview;

    TextView editText1_dialog,editText2_dialog,editText11_dialog,editText22_dialog,editText_from_day_hide_dialog,editText_from_day_visible_dialog;
    TextView editText_to_day_hide_dialog,editText_to_day_visible_dialog;
    String str_editText11_dialog="",str_editText22_dialog="",str_editText_from_day_visible_dialog="",str_editText_to_day_visible_dialog="";
    String onee, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve;
    String onee1, two1, three1, four1, five1, six1, seven1, eight1, nine1, ten1, eleven1, twelve1;
    private int year, year1;
    private int month, month1;
    private int day, day1;
    private int hour;
    private int minute;
    TextView tv_dateselecter;

    TextView editText_from_day_visible, editText_from_day_hide, editText_to_day_visible, editText_to_day_hide;
    TextView editText1, editText2, editText11, editText22, editText1_filter, editText2_filter;

    LinearLayout ll_dateselecter;
    LinearLayout ll_custom;
    int selectedPosition=0;

    String date1 = "201707210001";
    String date2 = "201707212359";

    Button btnok;
    int clickcount=1, clickcounts = 1;
    ListView listView;
    Dialog dialog;

    String response;
    GoogleAccountCredential mCredential;
    ProgressDialog mProgress;

    String u_name, u_username, username, password;
    String insert1_cc = "", insert1_rs = "", str_country;

    RadioButton radioBtnselect;
    String selected = "0";

    ProgressBar progressBar_table_name, progressBar_user_name;
    TextView table_name, user_name;

    SimpleDateFormat sdff2, sdff1;
    String currentDateandTimee1;
    String timee1;
    File file=null;

    TextView total_sales_cancel, total_transactions;
    PieChart mChart_pie_table;
    ProgressBar progressBar_cyclic_table;

    TextView no_data_table;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Gmail API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { GmailScopes.GMAIL_SEND };

    List toEmailList;

    String WebserviceUrl;

    public KotActivity(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        rootview = inflater.inflate(R.layout.kot_management, null);

        mCredential = GoogleAccountCredential.usingOAuth2(
                getActivity().getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        mProgress = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);
        mProgress.setMessage(getString(R.string.setmessage14));

        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE,null);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(getActivity());
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

        Cursor cursor_country = db.rawQuery("SELECT * FROM Country_Selection", null);
        if (cursor_country.moveToFirst()){
            str_country = cursor_country.getString(1);
        }
        cursor_country.close();


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

        TextView inn = (TextView) rootview.findViewById(R.id.rs_exp_val);
        inn.setText(insert1_rs);

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

        donotshowKeyboard(getActivity());

        db1.execSQL("Create table if not exists dummy_table_kot (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, tableid text, transactions text, amount integer);");
        db1.execSQL("Create table if not exists dummy_user_kot (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, user text, transactions text, amount integer);");

        btnok = (Button) rootview.findViewById(R.id.okok);

        tv_dateselecter= (TextView)rootview.findViewById(R.id.tv_dateselecter);

        total_sales_cancel = (TextView) rootview.findViewById(R.id.total_sales_cancel);
        total_transactions = (TextView) rootview.findViewById(R.id.transactions);

        progressBar_table_name = (ProgressBar) rootview.findViewById(R.id.progressBar_table_name);
        table_name = (TextView) rootview.findViewById(R.id.table_name);

        progressBar_user_name = (ProgressBar) rootview.findViewById(R.id.progressBar_user_name);
        user_name = (TextView) rootview.findViewById(R.id.user_name);

        mChart_pie_table = (PieChart) rootview.findViewById(R.id.mChart_pie_table);

        mChart_pie_table = (PieChart) rootview.findViewById(R.id.mChart_pie_table);
        no_data_table = (TextView) rootview.findViewById(R.id.no_data_table);

        progressBar_cyclic_table = (ProgressBar) rootview.findViewById(R.id.progressBar_cyclic_table);

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd", Locale.US);
        final String currentDateandTime1 = sdf2.format(new Date());

        SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM yyyy",Locale.US);
        final String currentDateandTime2 = sdf3.format(new Date());


        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",Locale.US);
        String time_hide = sdf.format(new Date());

        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm aa",Locale.US);
        String time_visible = sdf1.format(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        Calendar calendar11 = Calendar.getInstance();
        calendar11.add(Calendar.DAY_OF_YEAR, -1);
        Date yesterday = calendar11.getTime();

        editText1_filter = new TextView(getActivity());
        editText2_filter = new TextView(getActivity());

        editText1 = (TextView)rootview.findViewById(R.id.editText1);
        editText1.setText(currentDateandTime1);
        editText2 = (TextView)rootview.findViewById(R.id.editText2);
        editText2.setText(currentDateandTime1);

        editText11 = (TextView)rootview.findViewById(R.id.editText11);
        editText11.setText(currentDateandTime2);


        editText22 = (TextView)rootview.findViewById(R.id.editText22);
        editText22.setText(currentDateandTime2);

        editText_from_day_hide = (TextView) rootview.findViewById(R.id.editText_from_day_hide);
        editText_from_day_visible = (TextView) rootview.findViewById(R.id.editText_from_day_visible);


        editText_to_day_hide = (TextView) rootview.findViewById(R.id.editText_to_day_hide);
        editText_to_day_visible = (TextView) rootview.findViewById(R.id.editText_to_day_visible);

        Cursor time_get = db.rawQuery("SELECT * FROM Working_hours", null);
        if (time_get.moveToFirst()){
            String two = time_get.getString(2);
            String four = time_get.getString(4);
            String five = time_get.getString(5);
            String six = time_get.getString(6);
            String three = time_get.getString(3);

            editText_from_day_hide.setText(five);
            editText_from_day_visible.setText(two);
            editText_to_day_hide.setText(six);
            editText_to_day_visible.setText(four);

            SimpleDateFormat sdf4 = new SimpleDateFormat("dd MMM yyyy");
            final String currentDateandTime4 = sdf4.format(new Date());
            tv_dateselecter.setText(currentDateandTime2+","+two+" - "+currentDateandTime4+","+four);

            if (three.equals("Tomorrow")) {

                try {
//                    String string1 = five;
                    Date time1 = new SimpleDateFormat("HH:mm", Locale.US).parse(five);
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.add(Calendar.DAY_OF_YEAR, 0);
                    calendar1.setTime(time1);

                    String string2 = "23:59";
                    Date time2 = new SimpleDateFormat("HH:mm",Locale.US).parse(string2);
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(time2);

                    String string1_new = "00:00";
                    Date time1_new = new SimpleDateFormat("HH:mm",Locale.US).parse(string1_new);
                    Calendar calendar1_new = Calendar.getInstance();
                    calendar1_new.setTime(time1_new);
                    calendar1_new.add(Calendar.DATE, 1);

//                    String string2_new = five;
                    Date time2_new = new SimpleDateFormat("HH:mm",Locale.US).parse(five);
                    Calendar calendar2_new = Calendar.getInstance();
                    calendar2_new.setTime(time2_new);
                    calendar2_new.add(Calendar.DATE, 1);


//                    String someRandomTime = time_hide;
                    Date d = new SimpleDateFormat("HH:mm",Locale.US).parse(time_hide);
                    Calendar calendar3 = Calendar.getInstance();
                    calendar3.setTime(d);
//                    calendar3.add(Calendar.DATE, 1);

//                    String someRandomTime_new = time_hide;
                    Date d_new = new SimpleDateFormat("HH:mm",Locale.US).parse(time_hide);
                    Calendar calendar3_new = Calendar.getInstance();
                    calendar3_new.setTime(d_new);
                    calendar3_new.add(Calendar.DATE, 1);

                    Date x = calendar3.getTime();
                    Date x_new = calendar3_new.getTime();
                    if ((x.after(calendar1.getTime()) && x.before(calendar2.getTime())) || (x_new.after(calendar1_new.getTime()) && x_new.before(calendar2_new.getTime()))) {
                        //checkes whether the current time is between 14:49:00 and 20:11:13.
                        System.out.println(true);
//                    Toast.makeText(getActivity(),"yes", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity(),"time is "+time_hide, Toast.LENGTH_SHORT).show();


                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy",Locale.US);
                        String yesterday_visible = dateFormat.format(yesterday);

                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd",Locale.US);
                        String yesterday_hide = dateFormat1.format(yesterday);


                        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy",Locale.US);
                        String tomorrow_visible = dateFormat2.format(tomorrow);

                        SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyyMMdd",Locale.US);
                        String tomorrow_hide = dateFormat3.format(tomorrow);

                        if ((x.after(calendar1.getTime()) && x.before(calendar2.getTime()))) {
                            editText11.setText(currentDateandTime2);
                            editText22.setText(tomorrow_visible);

                            editText1.setText(currentDateandTime1);
                            editText2.setText(tomorrow_hide);
                        }
                        if ((x_new.after(calendar1_new.getTime()) && x_new.before(calendar2_new.getTime()))) {
                            editText11.setText(yesterday_visible);
                            editText22.setText(currentDateandTime2);

                            editText1.setText(yesterday_hide);
                            editText2.setText(currentDateandTime1);
                        }


                    } else {
//                    Toast.makeText(getActivity(),"no", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity(),"time is "+time_hide, Toast.LENGTH_SHORT).show();

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy",Locale.US);
                        String tomorrow_visible = dateFormat.format(tomorrow);

                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd",Locale.US);
                        String tomorrow_hide = dateFormat1.format(tomorrow);

                        editText11.setText(currentDateandTime2);
                        editText22.setText(tomorrow_visible);

                        editText1.setText(currentDateandTime1);
                        editText2.setText(tomorrow_hide);

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


            if (five.contains(":")){
                five = five.replace(":", "");
            }

            if (six.contains(":")){
                six = six.replace(":", "");
            }

            String r1, r2;
            r1 = editText1.getText().toString();
            r2 = editText2.getText().toString();
            if (r1.contains(" ")){
                r1 = r1.replace(" ", "");
            }
            if (r2.contains(" ")){
                r2 = r2.replace(" ", "");
            }

            editText1_filter.setText(r1+""+five);
            editText2_filter.setText(r2+""+six);

        }
        time_get.close();


        date1 = editText1_filter.getText().toString();
        date2 = editText2_filter.getText().toString();



        if (date1.length() == 11) {
//            Toast.makeText(getActivity(), "11 "+date1, Toast.LENGTH_LONG).show();
//            Toast.makeText(getActivity(), "11 "+date1, Toast.LENGTH_LONG).show();
            String onetoeight = date1.substring(0, 11);
            date1 = onetoeight + "1";
        }else {
//            Toast.makeText(getActivity(), "12 "+date1, Toast.LENGTH_LONG).show();
//            Toast.makeText(getActivity(), "12 "+date1, Toast.LENGTH_LONG).show();
        }

        ll_custom=rootview.findViewById(R.id.ll_custom);

        ll_dateselecter=rootview.findViewById(R.id.ll_dateselecter);
        ll_dateselecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog1 = new Dialog(getActivity(), R.style.notitle);
                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog1.setContentView(R.layout.dialog_select_period);
                dialog1.show();

                final RadioGroup filter_selection = (RadioGroup) dialog1.findViewById(R.id.filter_selection);

                final RadioButton working_hours = (RadioButton) dialog1.findViewById(R.id.working_hours);
                final RadioButton this_week = (RadioButton) dialog1.findViewById(R.id.this_week);
                final RadioButton this_month = (RadioButton) dialog1.findViewById(R.id.this_month);
                final RadioButton this_year = (RadioButton) dialog1.findViewById(R.id.this_year);
                final RadioButton all_time = (RadioButton) dialog1.findViewById(R.id.all_time);
                final RadioButton dialog_custom = (RadioButton) dialog1.findViewById(R.id.dialog_custom);

                if (selected.toString().equals("0")) {
                    working_hours.setChecked(true);
                }
                if (selected.toString().equals("1")) {
                    this_week.setChecked(true);
                }
                if (selected.toString().equals("2")) {
                    this_month.setChecked(true);
                }
                if (selected.toString().equals("3")) {
                    this_year.setChecked(true);
                }
                if (selected.toString().equals("4")) {
                    all_time.setChecked(true);
                }
                if (selected.toString().equals("5")) {
                    dialog_custom.setChecked(true);
                }

                filter_selection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        int selected1 = filter_selection.getCheckedRadioButtonId();
                        radioBtnselect = (RadioButton) dialog1.findViewById(selected1);

                        if (radioBtnselect.getText().toString().equals("This week")){
                            selected = "1";
                            new WeekTask().execute();
                            ll_custom.setVisibility(View.GONE);
                            dialog1.dismiss();
                        }else {
                            if (radioBtnselect.getText().toString().equals("This month")){
                                selected = "2";
                                new MonthTask().execute();
                                ll_custom.setVisibility(View.GONE);
                                dialog1.dismiss();
                            }else {
                                if (radioBtnselect.getText().toString().equals("This year")){
                                    selected = "3";
                                    new YearTask().execute();
                                    ll_custom.setVisibility(View.GONE);
                                    dialog1.dismiss();
                                }else {
                                    if (radioBtnselect.getText().toString().equals("All time")){
                                        selected = "4";
                                        String hjk = "";
                                        Cursor cursor11 = db1.rawQuery("SELECT * FROM Expenses_sales", null);
                                        if (cursor11.moveToFirst()) {
                                            new AllTimeTask().execute();
                                            ll_custom.setVisibility(View.GONE);
                                        }else {
//                                        mchart_bar_expenses.setVisibility(View.GONE);
//                                        mChart_pie_table.setVisibility(View.GONE);
//                                        mChart_pie_user.setVisibility(View.GONE);
                                        }
                                        cursor11.close();
                                        dialog1.dismiss();
                                    }else {
                                        if (radioBtnselect.getText().toString().equals("Custom")){

                                            dialog1.dismiss();

                                            final Dialog dialoge1 = new Dialog(getActivity(), R.style.timepicker_date_dialog);
                                            dialoge1.setContentView(R.layout.customdialog);
                                            initCustom1(dialoge1);
                                            dialoge1.show();
                                            ImageView iv_cancel=dialoge1.findViewById(R.id.iv_cancel);
                                            iv_cancel.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialoge1.cancel();
                                                }
                                            });


                                            Button btn_ok=dialoge1.findViewById(R.id.okok);
                                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    selected = "5";

                                                    str_editText11_dialog= editText11_dialog.getText().toString();
                                                    str_editText22_dialog= editText22_dialog.getText().toString();
                                                    str_editText_from_day_visible_dialog= editText_from_day_visible_dialog.getText().toString();
                                                    str_editText_to_day_visible_dialog= editText_to_day_visible_dialog.getText().toString();



                                                    String[] date_start=str_editText11_dialog.split(" ");
                                                    String date_dialog1=date_start[2];
                                                    if(date_start[1].equalsIgnoreCase("Jan")){
                                                        date_dialog1=date_dialog1+"01";
                                                    }else if(date_start[1].equalsIgnoreCase("Feb")){
                                                        date_dialog1=date_dialog1+"02";
                                                    }else if(date_start[1].equalsIgnoreCase("Mar")){
                                                        date_dialog1=date_dialog1+"03";
                                                    }else if(date_start[1].equalsIgnoreCase("Apr")){
                                                        date_dialog1=date_dialog1+"04";
                                                    }else if(date_start[1].equalsIgnoreCase("May")){
                                                        date_dialog1=date_dialog1+"05";
                                                    }else if(date_start[1].equalsIgnoreCase("Jun")){
                                                        date_dialog1=date_dialog1+"06";
                                                    }else if(date_start[1].equalsIgnoreCase("Jul")){
                                                        date_dialog1=date_dialog1+"07";
                                                    }else if(date_start[1].equalsIgnoreCase("Aug")){
                                                        date_dialog1=date_dialog1+"08";
                                                    }else if(date_start[1].equalsIgnoreCase("Sep")){
                                                        date_dialog1=date_dialog1+"09";
                                                    }else if(date_start[1].equalsIgnoreCase("Oct")){
                                                        date_dialog1=date_dialog1+"10";
                                                    }else if(date_start[1].equalsIgnoreCase("Nov")){
                                                        date_dialog1=date_dialog1+"11";
                                                    }else if(date_start[1].equalsIgnoreCase("Dec")){
                                                        date_dialog1=date_dialog1+"12";
                                                    }
                                                    date_dialog1=date_dialog1+date_start[0];
                                                    String time_dialog1="";

                                                    SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
                                                    SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
                                                    Date date = null;
                                                    try {
                                                        date = parseFormat.parse(str_editText_from_day_visible_dialog);
                                                        time_dialog1=displayFormat.format(date);
                                                        time_dialog1=time_dialog1.replace(":","");
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }

                                                    date_dialog1=date_dialog1+time_dialog1;


                                                    String[] date_end=str_editText22_dialog.split(" ");
                                                    String date_dialog2=date_end[2];
                                                    if(date_end[1].equalsIgnoreCase("Jan")){
                                                        date_dialog2=date_dialog2+"01";
                                                    }else if(date_end[1].equalsIgnoreCase("Feb")){
                                                        date_dialog2=date_dialog2+"02";
                                                    }else if(date_end[1].equalsIgnoreCase("Mar")){
                                                        date_dialog2=date_dialog2+"03";
                                                    }else if(date_end[1].equalsIgnoreCase("Apr")){
                                                        date_dialog2=date_dialog2+"04";
                                                    }else if(date_end[1].equalsIgnoreCase("May")){
                                                        date_dialog2=date_dialog2+"05";
                                                    }else if(date_end[1].equalsIgnoreCase("Jun")){
                                                        date_dialog2=date_dialog2+"06";
                                                    }else if(date_end[1].equalsIgnoreCase("Jul")){
                                                        date_dialog2=date_dialog2+"07";
                                                    }else if(date_end[1].equalsIgnoreCase("Aug")){
                                                        date_dialog2=date_dialog2+"08";
                                                    }else if(date_end[1].equalsIgnoreCase("Sep")){
                                                        date_dialog2=date_dialog2+"09";
                                                    }else if(date_end[1].equalsIgnoreCase("Oct")){
                                                        date_dialog2=date_dialog2+"10";
                                                    }else if(date_end[1].equalsIgnoreCase("Nov")){
                                                        date_dialog2=date_dialog2+"11";
                                                    }else if(date_end[1].equalsIgnoreCase("Dec")){
                                                        date_dialog2=date_dialog2+"12";
                                                    }
                                                    date_dialog2=date_dialog2+date_end[0];

                                                    String time_dialog2="";

                                                    try {
                                                        date = parseFormat.parse(str_editText_to_day_visible_dialog);
                                                        time_dialog2=displayFormat.format(date);
                                                        time_dialog2=time_dialog2.replace(":","");
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                    date_dialog2=date_dialog2+time_dialog2;



                                                    editText1_filter.setText(date_dialog1);
                                                    editText2_filter.setText(date_dialog2);


                                                    date1 = editText1_filter.getText().toString();
                                                    date2 = editText2_filter.getText().toString();

                                                    editText1.setText(date1);
                                                    editText2.setText(date2);

                                                    if (date1.length() == 11) {
//                    Toast.makeText(getActivity(), "11 "+date1, Toast.LENGTH_LONG).show();
//                    Toast.makeText(getActivity(), "11 "+date1, Toast.LENGTH_LONG).show();
                                                        String onetoeight = date1.substring(0, 11);
                                                        date1 = onetoeight + "1";
                                                    }else {
//                    Toast.makeText(getActivity(), "12 "+date1, Toast.LENGTH_LONG).show();
//                    Toast.makeText(getActivity(), "12 "+date1, Toast.LENGTH_LONG).show();
                                                    }

//                Toast.makeText(getActivity(), "new "+date1, Toast.LENGTH_LONG).show();
//                Toast.makeText(getActivity(), "new "+date1, Toast.LENGTH_LONG).show();





                                                    dialoge1.dismiss();
                                                    tv_dateselecter.setText(str_editText11_dialog+","+str_editText_from_day_visible_dialog+" - "+str_editText22_dialog+","+str_editText_to_day_visible_dialog);


                                                    btnok.callOnClick();

                                                }

                                            });


                                        }else {
                                            selected = "0";
                                            dialog1.dismiss();
                                            new WorkingTask().execute();
                                            ll_custom.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            }
                        }

                    }
                });
            }
        });

        total_sales_cancel.setText("0");
        Cursor cursor_total_expense = db1.rawQuery("SELECT SUM(total) FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"'", null);
        if (cursor_total_expense.moveToFirst()) {
            float val = cursor_total_expense.getFloat(0);
            total_sales_cancel.setText(String.format("%.0f", val));
        }
        cursor_total_expense.close();

        Cursor cursor_transactions = db1.rawQuery("SELECT COUNT(*) FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"'", null);
        if (cursor_transactions.moveToFirst()) {
            float val = cursor_transactions.getFloat(0);
            total_transactions.setText(String.format("%.0f", val));
        }
        cursor_transactions.close();

        DownloadMusicfromInternet1 downloadMusicfromInternet = new DownloadMusicfromInternet1();
        downloadMusicfromInternet.execute(editText1_filter.getText().toString() + editText2_filter.getText().toString());

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String r1, r2, r3, r4;
                r1 = editText1.getText().toString();
                r2 = editText2.getText().toString();
                if (r1.contains(" ")){
                    r1 = r1.replace(" ", "");
                }
                if (r2.contains(" ")){
                    r2 = r2.replace(" ", "");
                }

                r3 = editText_from_day_hide.getText().toString();
                r4 = editText_to_day_hide.getText().toString();
                if (r3.contains(":")){
                    r3 = r3.replace(":", "");
                }
                if (r4.contains(":")){
                    r4 = r4.replace(":", "");
                }

                editText1_filter.setText(r1+""+r3);
                editText2_filter.setText(r2+""+r4);


                date1 = editText1_filter.getText().toString();
                date2 = editText2_filter.getText().toString();

                if (date1.length() == 11) {
//                    Toast.makeText(getActivity(), "11 "+date1, Toast.LENGTH_LONG).show();
//                    Toast.makeText(getActivity(), "11 "+date1, Toast.LENGTH_LONG).show();
                    String onetoeight = date1.substring(0, 11);
                    date1 = onetoeight + "1";
                }else {
//                    Toast.makeText(getActivity(), "12 "+date1, Toast.LENGTH_LONG).show();
//                    Toast.makeText(getActivity(), "12 "+date1, Toast.LENGTH_LONG).show();
                }

                DownloadMusicfromInternet1 downloadMusicfromInternet = new DownloadMusicfromInternet1();
                downloadMusicfromInternet.execute(editText1_filter.getText().toString() + editText2_filter.getText().toString());

                Cursor cursor_total_expense = db1.rawQuery("SELECT SUM(total) FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"'", null);
                if (cursor_total_expense.moveToFirst()) {
                    float val = cursor_total_expense.getFloat(0);
                    total_sales_cancel.setText(String.format("%.0f", val));
                }
                cursor_total_expense.close();

                Cursor cursor_transactions = db1.rawQuery("SELECT COUNT(*) FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"'", null);
                if (cursor_transactions.moveToFirst()) {
                    float val = cursor_transactions.getFloat(0);
                    total_transactions.setText(String.format("%.0f", val));
                }
                cursor_transactions.close();

            }
        });

        CardView all_sales = (CardView) rootview.findViewById(R.id.all_sales);
        all_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hjk = "";
                Cursor cursor11 = db1.rawQuery("SELECT SUM(total) FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ", null);
                if (cursor11.moveToFirst()) {
                    int level = cursor11.getInt(0);
                    hjk = String.valueOf(level);
                }
                cursor11.close();
                TextView ghj = new TextView(getActivity());
                ghj.setText(hjk);
                if (ghj.getText().toString().equals("") || ghj.getText().toString().equals("0")) {
                    Toast.makeText(getActivity(), "no cancel kot's to see", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getActivity(), KOT_Cancel_list.class);
                    intent.putExtra("PLAYER1NAME", editText1_filter.getText().toString());
                    intent.putExtra("PLAYER2NAME", editText2_filter.getText().toString());
                    intent.putExtra("edittext1", editText1.getText().toString());
                    intent.putExtra("edittext11", editText11.getText().toString());
                    intent.putExtra("edittext2", editText2.getText().toString());
                    intent.putExtra("edittext22", editText22.getText().toString());
                    intent.putExtra("edittext_from_day_visible", editText_from_day_visible.getText().toString());
                    intent.putExtra("edittext_from_day_hide", editText_from_day_hide.getText().toString());
                    intent.putExtra("edittext_to_day_visible", editText_to_day_visible.getText().toString());
                    intent.putExtra("edittext_to_day_hide", editText_to_day_hide.getText().toString());

                    startActivity(intent);
                }
            }
        });

        CardView all_transactions = (CardView) rootview.findViewById(R.id.all_transactions);
        all_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hjk = "";
                Cursor cursor11 = db1.rawQuery("SELECT SUM(total) FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ", null);
                if (cursor11.moveToFirst()) {
                    int level = cursor11.getInt(0);
                    hjk = String.valueOf(level);
                }
                cursor11.close();
                TextView ghj = new TextView(getActivity());
                ghj.setText(hjk);
                if (ghj.getText().toString().equals("") || ghj.getText().toString().equals("0")) {
                    Toast.makeText(getActivity(), "no cancel kot's to see", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getActivity(), KOT_Cancel_list.class);
                    intent.putExtra("PLAYER1NAME", editText1_filter.getText().toString());
                    intent.putExtra("PLAYER2NAME", editText2_filter.getText().toString());
                    intent.putExtra("edittext1", editText1.getText().toString());
                    intent.putExtra("edittext11", editText11.getText().toString());
                    intent.putExtra("edittext2", editText2.getText().toString());
                    intent.putExtra("edittext22", editText22.getText().toString());
                    intent.putExtra("edittext_from_day_visible", editText_from_day_visible.getText().toString());
                    intent.putExtra("edittext_from_day_hide", editText_from_day_hide.getText().toString());
                    intent.putExtra("edittext_to_day_visible", editText_to_day_visible.getText().toString());
                    intent.putExtra("edittext_to_day_hide", editText_to_day_hide.getText().toString());

                    startActivity(intent);
                }
            }
        });

        CardView all_sales2 = (CardView) rootview.findViewById(R.id.all_sales2);
        all_sales2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hjk = "";
                Cursor cursor11 = db1.rawQuery("SELECT SUM(total) FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ", null);
                if (cursor11.moveToFirst()) {
                    int level = cursor11.getInt(0);
                    hjk = String.valueOf(level);
                }
                cursor11.close();
                TextView ghj = new TextView(getActivity());
                ghj.setText(hjk);
                if (ghj.getText().toString().equals("") || ghj.getText().toString().equals("0")) {
                    Toast.makeText(getActivity(), "no cancel kot's to see", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getActivity(), Table_cancel_kot_list.class);
                    intent.putExtra("PLAYER1NAME", editText1_filter.getText().toString());
                    intent.putExtra("PLAYER2NAME", editText2_filter.getText().toString());
                    intent.putExtra("edittext1", editText1.getText().toString());
                    intent.putExtra("edittext11", editText11.getText().toString());
                    intent.putExtra("edittext2", editText2.getText().toString());
                    intent.putExtra("edittext22", editText22.getText().toString());
                    intent.putExtra("edittext_from_day_visible", editText_from_day_visible.getText().toString());
                    intent.putExtra("edittext_from_day_hide", editText_from_day_hide.getText().toString());
                    intent.putExtra("edittext_to_day_visible", editText_to_day_visible.getText().toString());
                    intent.putExtra("edittext_to_day_hide", editText_to_day_hide.getText().toString());

                    startActivity(intent);
                }
            }
        });

        CardView all_sales3 = (CardView) rootview.findViewById(R.id.all_sales3);
        all_sales3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hjk = "";
                Cursor cursor11 = db1.rawQuery("SELECT SUM(total) FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ", null);
                if (cursor11.moveToFirst()) {
                    int level = cursor11.getInt(0);
                    hjk = String.valueOf(level);
                }
                cursor11.close();
                TextView ghj = new TextView(getActivity());
                ghj.setText(hjk);
                if (ghj.getText().toString().equals("") || ghj.getText().toString().equals("0")) {
                    Toast.makeText(getActivity(), "no cancel kot's to see", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getActivity(), User_cancel_kot_list.class);
                    intent.putExtra("PLAYER1NAME", editText1_filter.getText().toString());
                    intent.putExtra("PLAYER2NAME", editText2_filter.getText().toString());
                    intent.putExtra("edittext1", editText1.getText().toString());
                    intent.putExtra("edittext11", editText11.getText().toString());
                    intent.putExtra("edittext2", editText2.getText().toString());
                    intent.putExtra("edittext22", editText22.getText().toString());
                    intent.putExtra("edittext_from_day_visible", editText_from_day_visible.getText().toString());
                    intent.putExtra("edittext_from_day_hide", editText_from_day_hide.getText().toString());
                    intent.putExtra("edittext_to_day_visible", editText_to_day_visible.getText().toString());
                    intent.putExtra("edittext_to_day_hide", editText_to_day_hide.getText().toString());

                    startActivity(intent);
                }
            }
        });

        return rootview;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.

        inflater.inflate(R.menu.expenses_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_export:

                String hjk = "";
                Cursor cursor11 = db1.rawQuery("SELECT SUM(total) FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ", null);
                if (cursor11.moveToFirst()) {
                    int level = cursor11.getInt(0);
                    hjk = String.valueOf(level);
                }
                cursor11.close();
                TextView ghj = new TextView(getActivity());
                ghj.setText(hjk);
                if (ghj.getText().toString().equals("") || ghj.getText().toString().equals("0")) {
                    Toast.makeText(getActivity(), "no cancel kot's to see", Toast.LENGTH_SHORT).show();
                }else {
                    //MenuItem bedMenuItem = menu.findItem(R.id.action_export);
                    //bedMenuItem.setVisible(true);
                    //Toast.makeText(getActivity(), "yes", Toast.LENGTH_SHORT).show();
                    sdff2 = new SimpleDateFormat("ddMMMyy",Locale.US);
                    currentDateandTimee1 = sdff2.format(new Date());

                    Date dt = new Date();
                    sdff1 = new SimpleDateFormat("hhmmssaa",Locale.US);
                    timee1 = sdff1.format(dt);

                    ExportDatabaseCSVTask task=new ExportDatabaseCSVTask();
                    task.execute();
                }


                break;

            case R.id.action_exportmail:

                String hjk1 = "";
                Cursor cursor111 = db1.rawQuery("SELECT SUM(total) FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ", null);
                if (cursor111.moveToFirst()) {
                    int level = cursor111.getInt(0);
                    hjk1 = String.valueOf(level);
                }
                cursor111.close();
                TextView ghj1 = new TextView(getActivity());
                ghj1.setText(hjk1);
                if (ghj1.getText().toString().equals("") || ghj1.getText().toString().equals("0")) {
                    Toast.makeText(getActivity(), "no cancel kot's to see", Toast.LENGTH_SHORT).show();
                }else {

                    //MenuItem bedMenuItem = menu.findItem(R.id.action_export);
                    //bedMenuItem.setVisible(true);
                    //Toast.makeText(getActivity(), "yes", Toast.LENGTH_SHORT).show();
                    sdff2 = new SimpleDateFormat("ddMMMyy",Locale.US);
                    currentDateandTimee1 = sdff2.format(new Date());

                    Date dt = new Date();
                    sdff1 = new SimpleDateFormat("hhmmssaa",Locale.US);
                    timee1 = sdff1.format(dt);

                    ExportDatabaseCSVTask task=new ExportDatabaseCSVTask();
                    task.execute();

                    String companynameis = "";
                    Cursor cursor = db.rawQuery("SELECT * FROM Companydetailss", null);
                    if (cursor.moveToFirst()) {
                        companynameis = cursor.getString(1);
                    }else {
                        companynameis = "";
                    }
                    cursor.close();

                    String url = "www.intuitionsoftwares.com";
                    String msg = "" + Uri.parse(url);

                    Cursor cursore = db.rawQuery("SELECT * FROM Email_setup", null);
                    if (cursore.moveToFirst()){
                        Cursor cursoree = db.rawQuery("SELECT * FROM Email_recipient", null);
                        if (cursoree.moveToFirst()){
                            //both are there
                            Cursor cursoor = db.rawQuery("SELECT * FROM Email_setup", null);
                            if (cursoor.moveToFirst()) {
                                String un = cursoor.getString(1);
                                String pwd = cursoor.getString(2);
                                String em_ca = cursoor.getString(3);
                                if (em_ca.equals("Gmail")) {
                                    getResultsFromApi();
                                    new MakeRequestTask(mCredential).execute();
                                }else {
                                    if (em_ca.equals("Yahoo")){
//                                        Toast.makeText(getActivity(), "yahoo "+un, Toast.LENGTH_LONG).show();
                                        Cursor cursor1 = db.rawQuery("SELECT * FROM Email_recipient", null);
                                        if (cursor1.moveToFirst()) {
                                            do {
                                                String unn = cursor1.getString(3);
                                                String toEmails = unn;
                                                toEmailList = Arrays.asList(toEmails
                                                        .split("\\s*,\\s*"));
                                                new SendMailTask_Yahoo_attachment_kot_can(getActivity()).execute(un,
                                                        pwd, toEmailList, companynameis, msg, currentDateandTimee1, timee1);
                                            } while (cursor1.moveToNext());
                                        }
                                        cursor1.close();


                                    }else {
                                        if (em_ca.equals("Hotmail")){
//                                            Toast.makeText(getActivity(), "Hotmail and Outlook "+un, Toast.LENGTH_LONG).show();
                                            Cursor cursor1 = db.rawQuery("SELECT * FROM Email_recipient", null);
                                            if (cursor1.moveToFirst()) {
                                                do {
                                                    String unn = cursor1.getString(3);
                                                    String toEmails = unn;
                                                    toEmailList = Arrays.asList(toEmails
                                                            .split("\\s*,\\s*"));
                                                    new SendMailTask_Hotmail_Outlook_attachment_kot_can(getActivity()).execute(un,
                                                            pwd, toEmailList, companynameis, msg, currentDateandTimee1, timee1);
                                                } while (cursor1.moveToNext());
                                            }
                                            cursor1.close();
                                        }else {
                                            if (em_ca.equals("Office365")) {
//                                                Toast.makeText(getActivity(), "office 365 " + un, Toast.LENGTH_LONG).show();
                                                Cursor cursor1 = db.rawQuery("SELECT * FROM Email_recipient", null);
                                                if (cursor1.moveToFirst()) {
                                                    do {
                                                        String unn = cursor1.getString(3);
                                                        String toEmails = unn;
                                                        toEmailList = Arrays.asList(toEmails
                                                                .split("\\s*,\\s*"));
                                                        new SendMailTask_Office365_attachment_kot_can(getActivity()).execute(un,
                                                                pwd, toEmailList, companynameis, msg, currentDateandTimee1, timee1);
                                                    } while (cursor1.moveToNext());
                                                }
                                                cursor1.close();
                                            }
                                        }
                                    }
                                }
                            }
                            cursoor.close();
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
                        cursoree.close();
                    }else {
                        Cursor cursoree = db.rawQuery("SELECT * FROM Email_recipient", null);
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
                        cursoree.close();
                    }
                    cursore.close();

                }

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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

    class WorkingTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            populateSetDate(year,month,day);
            populateSetDate_2(year,month,day);



            Cursor time_get = db.rawQuery("SELECT * FROM Working_hours", null);
            if (time_get.moveToFirst()) {

                String five = time_get.getString(5);
                String six = time_get.getString(6);

                String two= time_get.getString(2);
                String four=time_get.getString(4);

                String[] h=five.split(":");
                String[] m=six.split(":");

                updateTime_open(Integer.parseInt(h[0]), Integer.parseInt(h[1]));
                updateTime_close(Integer.parseInt(m[0]), Integer.parseInt(m[1]));

                SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM yyyy");
                final String currentDateandTime2 = sdf3.format(new Date());
                tv_dateselecter.setText(currentDateandTime2+","+two+" - "+currentDateandTime2+","+four);

            }
            time_get.close();

        }

        @Override
        protected String doInBackground(String... strings) {

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            btnok.callOnClick();
        }

        public void populateSetDate(int year, int month, int day) {
            TextView mEdit = (TextView) getActivity().findViewById(R.id.editText1);
            TextView mEdit1  = (TextView) getActivity().findViewById(R.id.editText11);
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



        public void populateSetDate_2(int year, int month, int day) {
            TextView mEdit = (TextView) getActivity().findViewById(R.id.editText2);
            TextView mEdit1  = (TextView)getActivity().findViewById(R.id.editText22);
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

    }

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

    class WeekTask extends AsyncTask<String, String, String>{
        String mon="";
        String mon1="";
        //  int dow;
        int day1,day2;
        int month1,month2;
        int year1;
        // int min;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Calendar myDate = Calendar.getInstance(); // set this up however you need it.

            //first day of week
            myDate.set(Calendar.DAY_OF_WEEK, 1);

            year1 = myDate.get(Calendar.YEAR);
            month1 = myDate.get(Calendar.MONTH)+1;
            day1 = myDate.get(Calendar.DAY_OF_MONTH);

          /*  //last day of week
            myDate.set(Calendar.DAY_OF_WEEK, 7);


             month2 = myDate.get(Calendar.MONTH)+1;
             day2 = myDate.get(Calendar.DAY_OF_MONTH);*/

            //dow = myDate.get (Calendar.DAY_OF_WEEK);

            Calendar cal = Calendar.getInstance();
            day2 = cal.get(Calendar.DATE);
            month2 = cal.get(Calendar.MONTH) + 1;
            //year = cal.get(Calendar.YEAR);
            // min=day-dow;

            populateSetDate(year1,month1,day1);
            populateSetDate_2(year1,month2,day2);
            updateTime_open(0, 1);
            updateTime_close(23, 59);
        }

        @Override
        protected String doInBackground(String... strings) {


            if(month1==1){
                mon="Jan";
            }else if(month1==2){
                mon="Feb";
            }else if(month1==3){
                mon="Mar";
            }else if(month1==4){
                mon="Apr";
            }else if(month1==5){
                mon="May";
            }else if(month1==6){
                mon="Jun";
            }else if(month1==7){
                mon="Jul";
            }else if(month1==8){
                mon="Aug";
            }else if(month1==9){
                mon="Sep";
            }else if(month1==10){
                mon="Oct";
            }else if(month1==11){
                mon="Nov";
            }else if(month1==12){
                mon="Dec";
            }


            if(month2==1){
                mon1="Jan";
            }else if(month2==2){
                mon1="Feb";
            }else if(month2==3){
                mon1="Mar";
            }else if(month2==4){
                mon1="Apr";
            }else if(month2==5){
                mon1="May";
            }else if(month2==6){
                mon1="Jun";
            }else if(month2==7){
                mon1="Jul";
            }else if(month2==8){
                mon1="Aug";
            }else if(month2==9){
                mon1="Sep";
            }else if(month2==10){
                mon1="Oct";
            }else if(month2==11){
                mon1="Nov";
            }else if(month2==12){
                mon1="Dec";
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            tv_dateselecter.setText((day1)+" "+mon+" - "+day2+" "+mon1);
            btnok.callOnClick();

        }




        public void populateSetDate(int year, int month, int day) {
            TextView mEdit = (TextView) getActivity().findViewById(R.id.editText1);
            TextView mEdit1  = (TextView)getActivity().findViewById(R.id.editText11);
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



        public void populateSetDate_2(int year, int month, int day) {
            TextView mEdit = (TextView) getActivity().findViewById(R.id.editText2);
            TextView mEdit1  = (TextView)getActivity().findViewById(R.id.editText22);
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

    }

    class MonthTask extends AsyncTask<String, String, String>{

        int year,month,day;
        String mon="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Calendar cal = Calendar.getInstance();
            day = cal.get(Calendar.DATE);
            month = cal.get(Calendar.MONTH) + 1;
            year = cal.get(Calendar.YEAR);
            populateSetDate(year,month,01);
            populateSetDate_2(year,month,31);
            updateTime_open(0, 1);
            updateTime_close(23, 59);
        }

        @Override
        protected String doInBackground(String... strings) {


            if(month==1){
                mon="Jan";
            }else if(month==2){
                mon="Feb";
            }else if(month==3){
                mon="Mar";
            }else if(month==4){
                mon="Apr";
            }else if(month==5){
                mon="May";
            }else if(month==6){
                mon="Jun";
            }else if(month==7){
                mon="Jul";
            }else if(month==8){
                mon="Aug";
            }else if(month==9){
                mon="Sep";
            }else if(month==10){
                mon="Oct";
            }else if(month==11){
                mon="Nov";
            }else if(month==12){
                mon="Dec";
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tv_dateselecter.setText(1+" "+mon+" - "+day+" "+mon);
            btnok.callOnClick();
        }

        public void populateSetDate(int year, int month, int day) {
            TextView mEdit = (TextView) getActivity().findViewById(R.id.editText1);
            TextView mEdit1  = (TextView)getActivity().findViewById(R.id.editText11);
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

        public void populateSetDate_2(int year, int month, int day) {
            TextView mEdit = (TextView) getActivity().findViewById(R.id.editText2);
            TextView mEdit1  = (TextView)getActivity().findViewById(R.id.editText22);
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


    }

    class YearTask extends AsyncTask<String, String, String>{
        String mon="";
        int day,month,year;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Calendar cal = Calendar.getInstance();
            day = cal.get(Calendar.DATE);
            month = cal.get(Calendar.MONTH) + 1;
            year = cal.get(Calendar.YEAR);
            populateSetDate(year,01,01);
            populateSetDate_2(year,month,day);
            updateTime_open(0, 1);
            updateTime_close(23, 59);
        }

        @Override
        protected String doInBackground(String... strings) {

            if(month==1){
                mon="Jan";
            }else if(month==2){
                mon="Feb";
            }else if(month==3){
                mon="Mar";
            }else if(month==4){
                mon="Apr";
            }else if(month==5){
                mon="May";
            }else if(month==6){
                mon="Jun";
            }else if(month==7){
                mon="Jul";
            }else if(month==8){
                mon="Aug";
            }else if(month==9){
                mon="Sep";
            }else if(month==10){
                mon="Oct";
            }else if(month==11){
                mon="Nov";
            }else if(month==12){
                mon="Dec";
            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tv_dateselecter.setText(1+"Jan - "+day+" "+mon);

            btnok.callOnClick();
        }

        public void populateSetDate(int year, int month, int day) {
            TextView mEdit = (TextView) getActivity().findViewById(R.id.editText1);
            TextView mEdit1  = (TextView)getActivity().findViewById(R.id.editText11);
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

        public void populateSetDate_2(int year, int month, int day) {
            TextView mEdit = (TextView) getActivity().findViewById(R.id.editText2);
            TextView mEdit1  = (TextView) getActivity().findViewById(R.id.editText22);
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

    }

    class AllTimeTask extends AsyncTask<String, String, String>{
        String mon="";
        int dow;
        int day;
        int month;
        int year;
        int min;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            Cursor time_get = db1.rawQuery("SELECT MIN(datetimee_new) FROM Expenses_Sales", null);
            if (time_get.moveToFirst()) {
                String date=time_get.getString(0);
                String year1 = date.substring(0, 4);
                String month1 = date.substring(4, 6);
                String date1 = date.substring(6, 8);

                year=Integer.parseInt(year1);
                month=Integer.parseInt(month1);
                day=Integer.parseInt(date1);
            }
            time_get.close();

            populateSetDate(year,month,day);
            Calendar cal = Calendar.getInstance();
            day = cal.get(Calendar.DATE);
            month = cal.get(Calendar.MONTH) + 1;
            year = cal.get(Calendar.YEAR);

            populateSetDate_2(year,month,day);
            updateTime_open(0, 1);
            updateTime_close(23, 59);
        }

        @Override
        protected String doInBackground(String... strings) {


            if(month==1){
                mon="Jan";
            }else if(month==2){
                mon="Feb";
            }else if(month==3){
                mon="Mar";
            }else if(month==4){
                mon="Apr";
            }else if(month==5){
                mon="May";
            }else if(month==6){
                mon="Jun";
            }else if(month==7){
                mon="Jul";
            }else if(month==8){
                mon="Aug";
            }else if(month==9){
                mon="Sep";
            }else if(month==10){
                mon="Oct";
            }else if(month==11){
                mon="Nov";
            }else if(month==12){
                mon="Dec";
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Cursor time_get = db1.rawQuery("SELECT MIN(datetimee_new) FROM Expenses_Sales", null);
            if (time_get.moveToFirst()) {
                String date=time_get.getString(0);

                Cursor time_get1 = db1.rawQuery("SELECT * FROM Expenses_Sales WHERE datetimee_new = '"+date+"'", null);
                if (time_get1.moveToFirst()) {
                    String date1=time_get1.getString(6);
                    SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM yyyy");
                    final String currentDateandTime2 = sdf3.format(new Date());
                    tv_dateselecter.setText(date1 + " - " + currentDateandTime2);
                }
                time_get1.close();
            }
            time_get.close();


            btnok.callOnClick();

        }




        public void populateSetDate(int year, int month, int day) {
            TextView mEdit = (TextView) getActivity().findViewById(R.id.editText1);
            TextView mEdit1  = (TextView)getActivity().findViewById(R.id.editText11);
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



        public void populateSetDate_2(int year, int month, int day) {
            TextView mEdit = (TextView) getActivity().findViewById(R.id.editText2);
            TextView mEdit1  = (TextView)getActivity().findViewById(R.id.editText22);
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

    }

    private void initCustom1(final Dialog dialoge1) {

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        final String currentDateandTime1 = sdf2.format(new Date());

        SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM yyyy");
        final String currentDateandTime2 = sdf3.format(new Date());


        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time_hide = sdf.format(new Date());

        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm aa");
        String time_visible = sdf1.format(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        Calendar calendar11 = Calendar.getInstance();
        calendar11.add(Calendar.DAY_OF_YEAR, -1);
        Date yesterday = calendar11.getTime();

        editText1_dialog = (TextView) dialoge1.findViewById(R.id.editText1_dialog);
        editText1_dialog.setText(currentDateandTime1);
    /*    editText1 = (TextView) findViewById(R.id.editText1);
        editText1.setText(currentDateandTime1);*/


        editText2_dialog = (TextView) dialoge1.findViewById(R.id.editText2_dialog);
        editText2_dialog.setText(currentDateandTime1);
/*        editText2 = (TextView) findViewById(R.id.editText2);
        editText2.setText(currentDateandTime1);*/

        editText11_dialog = (TextView) dialoge1.findViewById(R.id.editText11_dialog);
        editText11_dialog.setText(currentDateandTime2);
/*        editText11 = (TextView) findViewById(R.id.editText11);
        editText11.setText(currentDateandTime2);*/

        editText22_dialog = (TextView) dialoge1.findViewById(R.id.editText22_dialog);
        editText22_dialog.setText(currentDateandTime2);
  /*      editText22 = (TextView) findViewById(R.id.editText22);
        editText22.setText(currentDateandTime2);*/


        editText_from_day_hide_dialog = (TextView) dialoge1.findViewById(R.id.editText_from_day_hide_dialog);
        editText_from_day_visible_dialog = (TextView) dialoge1.findViewById(R.id.editText_from_day_visible_dialog);
        editText_to_day_hide_dialog = (TextView) dialoge1.findViewById(R.id.editText_to_day_hide_dialog);
        editText_to_day_visible_dialog = (TextView) dialoge1.findViewById(R.id.editText_to_day_visible_dialog);


/*
        editText_from_day_hide = (TextView) findViewById(R.id.editText_from_day_hide);
        editText_from_day_visible = (TextView) findViewById(R.id.editText_from_day_visible);
        editText_to_day_hide = (TextView) findViewById(R.id.editText_to_day_hide);
        editText_to_day_visible = (TextView) findViewById(R.id.editText_to_day_visible);
*/


        updateTime_open_dialog(0, 1);
        updateTime_close_dialog(23, 59);

        editText11_dialog.setOnClickListener(new View.OnClickListener() {

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
//                dpd.show(HomeActivity.this.getFragmentManager(), "Datepickerdialog");

                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        datePickerListener,now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMaxDate(Calendar.getInstance());
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
        /*        TextView mEdit = (TextView) dialoge1.findViewById(R.id.editText1);
                TextView mEdit1  = (TextView)dialoge1.findViewById(R.id.editText11);*/
                TextView mEdit_dialog = (TextView) dialoge1.findViewById(R.id.editText1_dialog);
                TextView mEdit1_dialog  = (TextView)dialoge1.findViewById(R.id.editText11_dialog);

                if (month == 1 && day < 10) {
                    //   mEdit.setText(year + " " + "0" + 1 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + "0" + 1 + " " + "0" + day);
                    onee1 = "0" + day + " " + "Jan" + " " + year;
                    //   mEdit1.setText(onee1);
                    mEdit1_dialog.setText(onee1);
                } else {
                    if (month == 1) {
                        mEdit_dialog.setText(year + " " + "0" + 1 + " " + day);
                        //     mEdit.setText(year + " " + "0" + 1 + " " + day);
                        onee = day + " " + "Jan" + " " + year;
                        //   mEdit1.setText(onee);
                        mEdit1_dialog.setText(onee);
                    }
                }

                if (month == 2 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 2 + " " + "0" + day);
                    //  mEdit.setText(year + " " + "0" + 2 + " " + "0" + day);
                    two1 = "0" + day + " " + "Feb" + " " + year;
                    //  mEdit1.setText(two1);
                    mEdit1_dialog.setText(two1);
                } else {
                    if (month == 2) {
                        mEdit_dialog.setText(year + " " + "0" + 2 + " " + day);
                        //    mEdit.setText(year + " " + "0" + 2 + " " + day);
                        two = day + " " + "Feb" + " " + year;
                        //     mEdit1.setText(two);
                        mEdit1_dialog.setText(two);
                    }
                }

                if (month == 3 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 3 + " " + "0" + day);
                    //      mEdit.setText(year + " " + "0" + 3 + " " + "0" + day);
                    three1 = "0" + day + " " + "Mar" + " " + year;
                    //    mEdit1.setText(three1);
                    mEdit1_dialog.setText(three1);
                } else {
                    if (month == 3) {
                        mEdit_dialog.setText(year + " " + "0" + 3 + " " + day);
                        //      mEdit.setText(year + " " + "0" + 3 + " " + day);
                        three = day + " " + "Mar" + " " + year;
                        //      mEdit1.setText(three);
                        mEdit1_dialog.setText(three);
                    }
                }

                if (month == 4 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 4 + " " + "0" + day);
                    //       mEdit.setText(year + " " + "0" + 4 + " " + "0" + day);
                    four1 = "0" + day + " " + "Apr" + " " + year;
                    //     mEdit1.setText(four1);
                    mEdit1_dialog.setText(four1);
                } else {
                    if (month == 4) {
                        mEdit_dialog.setText(year + " " + "0" + 4 + " " + day);
                        //       mEdit.setText(year + " " + "0" + 4 + " " + day);
                        four = day + " " + "Apr" + " " + year;
                        //      mEdit1.setText(four);
                        mEdit1_dialog.setText(four);
                    }
                }

                if (month == 5 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 5 + " " + "0" + day);
                    //     mEdit.setText(year + " " + "0" + 5 + " " + "0" + day);
                    five1 = "0" + day + " " + "May" + " " + year;
                    //     mEdit1.setText(five1);
                    mEdit1_dialog.setText(five1);
                } else {
                    if (month == 5) {
                        mEdit_dialog.setText(year + " " + "0" + 5 + " " + day);
                        //      mEdit.setText(year + " " + "0" + 5 + " " + day);
                        five = day + " " + "May" + " " + year;
                        //       mEdit1.setText(five);
                        mEdit1_dialog.setText(five);
                    }
                }

                if (month == 6 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 6 + " " + "0" + day);
                    //    mEdit.setText(year + " " + "0" + 6 + " " + "0" + day);
                    six1 = "0" + day + " " + "Jun" + " " + year;
                    //      mEdit1.setText(six1);
                    mEdit1_dialog.setText(six1);
                } else {
                    if (month == 6) {
                        mEdit_dialog.setText(year + " " + "0" + 6 + " " + day);
                        //        mEdit.setText(year + " " + "0" + 6 + " " + day);
                        six = day + " " + "Jun" + " " + year;
                        //        mEdit1.setText(six);
                        mEdit1_dialog.setText(six);
                    }
                }

                if (month == 7 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 7 + " " + "0" + day);
                    //       mEdit.setText(year + " " + "0" + 7 + " " + "0" + day);
                    seven1 = "0" + day + " " + "Jul" + " " + year;
                    //     mEdit1.setText(seven1);
                    mEdit1_dialog.setText(seven1);
                } else {
                    if (month == 7) {
                        mEdit_dialog.setText(year + " " + "0" + 7 + " " + day);
                        //         mEdit.setText(year + " " + "0" + 7 + " " + day);
                        seven = day + " " + "Jul" + " " + year;
                        //        mEdit1.setText(seven);
                        mEdit1_dialog.setText(seven);
                    }
                }

                if (month == 8 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 8 + " " + "0" + day);
                    //      mEdit.setText(year + " " + "0" + 8 + " " + "0" + day);
                    eight1 = "0" + day + " " + "Aug" + " " + year;
                    //     mEdit1.setText(eight1);
                    mEdit1_dialog.setText(eight1);
                } else {
                    if (month == 8) {
                        mEdit_dialog.setText(year + " " + "0" + 8 + " " + day);
                        //         mEdit.setText(year + " " + "0" + 8 + " " + day);
                        eight = day + " " + "Aug" + " " + year;
                        //          mEdit1.setText(eight);
                        mEdit1_dialog.setText(eight);
                    }
                }

                if (month == 9 && day < 10) {
                    mEdit_dialog.setText(year + " " + "0" + 9 + " " + "0" + day);
                    //      mEdit.setText(year + " " + "0" + 9 + " " + "0" + day);
                    nine1 = "0" + day + " " + "Sep" + " " + year;
                    //       mEdit1.setText(nine1);
                    mEdit1_dialog.setText(nine1);
                } else {
                    if (month == 9) {
                        mEdit_dialog.setText(year + " " + "0" + 9 + " " + day);
                        //          mEdit.setText(year + " " + "0" + 9 + " " + day);
                        nine = day + " " + "Sep" + " " + year;
                        //          mEdit1.setText(nine);
                        mEdit1_dialog.setText(nine);
                    }
                }

                if (month == 10 && day < 10) {
                    mEdit_dialog.setText(year + " " + 10 + " " + "0" + day);
                    //       mEdit.setText(year + " " + 10 + " " + "0" + day);
                    ten1 = "0" + day + " " + "Oct" + " " + year;
                    //       mEdit1.setText(ten1);
                    mEdit1_dialog.setText(ten1);
                } else {
                    if (month == 10) {
                        mEdit_dialog.setText(year + " " + 10 + " " + day);
                        ten = day + " " + "Oct" + " " + year;
                        //          mEdit1.setText(ten);
                        mEdit1_dialog.setText(ten);
                    }
                }

                if (month == 11 && day < 10) {
                    mEdit_dialog.setText(year + " " + 11 + " " + "0" + day);
                    //        mEdit.setText(year + " " + 11 + " " + "0" + day);
                    eleven1 = "0" + day + " " + "Nov" + " " + year;
                    //        mEdit1.setText(eleven1);
                    mEdit1_dialog.setText(eleven1);
                } else {
                    if (month == 11) {
                        mEdit_dialog.setText(year + " " + 11 + " " + day);
                        //          mEdit.setText(year + " " + 11 + " " + day);
                        eleven = day + " " + "Nov" + " " + year;
                        //        mEdit1.setText(eleven);
                        mEdit1_dialog.setText(eleven);
                    }
                }

                if (month == 12 && day < 10) {
                    mEdit_dialog.setText(year + " " + 12 + " " + "0" + day);
                    //       mEdit.setText(year + " " + 12 + " " + "0" + day);
                    twelve1 = "0" + day + " " + "Dec" + " " + year;
                    //    mEdit1.setText(twelve1);
                    mEdit1_dialog.setText(twelve1);
                } else {
                    if (month == 12) {
                        mEdit_dialog.setText(year + " " + 12 + " " + day);
                        //         mEdit.setText(year + " " + 12 + " " + day);
                        twelve = day + " " + "Dec" + " " + year;
                        //        mEdit1.setText(twelve);
                        mEdit1_dialog.setText(twelve);
                    }
                }

            }

        });


        editText22_dialog.setOnClickListener(new View.OnClickListener() {

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
//                dpd.show(HomeActivity.this.getFragmentManager(), "Datepickerdialog");
                //if (clickcount == 1){
                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        datePickerListener,now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMaxDate(Calendar.getInstance());
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                clickcount++;
//                }else {
//                    Calendar now = Calendar.getInstance();
//                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
//                            datePickerListener, year, month, day
//                    );
//
//                    dpd.show(HomeActivity.this.getFragmentManager(), "Datepickerdialog");
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
            /*    TextView mEdit = (TextView) dialoge1.findViewById(R.id.editText2);
                TextView mEdit1  = (TextView) dialoge1.findViewById(R.id.editText22);*/
                TextView mEdit_dialog = (TextView) dialoge1.findViewById(R.id.editText2_dialog);
                TextView mEdit1_dialog  = (TextView) dialoge1.findViewById(R.id.editText22_dialog);
                if (month == 1 && day < 10) {
                    //    mEdit.setText(year + " " + "0" + 1 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + "0" + 1 + " " + "0" + day);
                    onee1 = "0" + day + " " + "Jan" + " " + year;
                    //    mEdit1.setText(onee1);
                    mEdit1_dialog.setText(onee1);
                } else {
                    if (month == 1) {
                        //       mEdit.setText(year + " " + "0" + 1 + " " + day);
                        mEdit_dialog.setText(year + " " + "0" + 1 + " " + day);
                        onee = day + " " + "Jan" + " " + year;
                        //      mEdit1.setText(onee);
                        mEdit1_dialog.setText(onee);
                    }
                }

                if (month == 2 && day < 10) {
                    //    mEdit.setText(year + " " + "0" + 2 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + "0" + 2 + " " + "0" + day);
                    two1 = "0" + day + " " + "Feb" + " " + year;
                    mEdit1_dialog.setText(two1);
                } else {
                    if (month == 2) {
                        //       mEdit.setText(year + " " + "0" + 2 + " " + day);
                        mEdit_dialog.setText(year + " " + "0" + 2 + " " + day);
                        two = day + " " + "Feb" + " " + year;
                        ///      mEdit1.setText(two);
                        mEdit1_dialog.setText(two);
                    }
                }

                if (month == 3 && day < 10) {
                    //    mEdit.setText(year + " " + "0" + 3 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + "0" + 3 + " " + "0" + day);
                    three1 = "0" + day + " " + "Mar" + " " + year;
                    //    mEdit1.setText(three1);
                    mEdit1_dialog.setText(three1);
                } else {
                    if (month == 3) {
                        //        mEdit.setText(year + " " + "0" + 3 + " " + day);
                        mEdit_dialog.setText(year + " " + "0" + 3 + " " + day);
                        three = day + " " + "Mar" + " " + year;
                        //      mEdit1.setText(three);
                        mEdit1_dialog.setText(three);
                    }
                }

                if (month == 4 && day < 10) {
                    //    mEdit.setText(year + " " + "0" + 4 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + "0" + 4 + " " + "0" + day);
                    four1 = "0" + day + " " + "Apr" + " " + year;
                    //     mEdit1.setText(four1);
                    mEdit1_dialog.setText(four1);
                } else {
                    if (month == 4) {
                        //       mEdit.setText(year + " " + "0" + 4 + " " + day);
                        mEdit_dialog.setText(year + " " + "0" + 4 + " " + day);
                        four = day + " " + "Apr" + " " + year;
                        //       mEdit1.setText(four);
                        mEdit1_dialog.setText(four);
                    }
                }

                if (month == 5 && day < 10) {
                    //     mEdit.setText(year + " " + "0" + 5 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + "0" + 5 + " " + "0" + day);
                    five1 = "0" + day + " " + "May" + " " + year;
                    //     mEdit1.setText(five1);
                    mEdit1_dialog.setText(five1);
                } else {
                    if (month == 5) {
                        //         mEdit.setText(year + " " + "0" + 5 + " " + day);
                        mEdit_dialog.setText(year + " " + "0" + 5 + " " + day);
                        five = day + " " + "May" + " " + year;
                        //       mEdit1.setText(five);
                        mEdit1_dialog.setText(five);
                    }
                }

                if (month == 6 && day < 10) {
                    //     mEdit.setText(year + " " + "0" + 6 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + "0" + 6 + " " + "0" + day);
                    six1 = "0" + day + " " + "Jun" + " " + year;
                    //      mEdit1.setText(six1);
                    mEdit1_dialog.setText(six1);
                } else {
                    if (month == 6) {
                        //          mEdit.setText(year + " " + "0" + 6 + " " + day);
                        mEdit_dialog.setText(year + " " + "0" + 6 + " " + day);
                        six = day + " " + "Jun" + " " + year;
                        //        mEdit1.setText(six);
                        mEdit1_dialog.setText(six);
                    }
                }

                if (month == 7 && day < 10) {
                    //        mEdit.setText(year + " " + "0" + 7 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + "0" + 7 + " " + "0" + day);
                    seven1 = "0" + day + " " + "Jul" + " " + year;
                    //          mEdit1.setText(seven1);
                    mEdit1_dialog.setText(seven1);
                } else {
                    if (month == 7) {
                        //            mEdit.setText(year + " " + "0" + 7 + " " + day);
                        mEdit_dialog.setText(year + " " + "0" + 7 + " " + day);
                        seven = day + " " + "Jul" + " " + year;
                        //           mEdit1.setText(seven);
                        mEdit1_dialog.setText(seven);
                    }
                }

                if (month == 8 && day < 10) {
                    //       mEdit.setText(year + " " + "0" + 8 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + "0" + 8 + " " + "0" + day);
                    eight1 = "0" + day + " " + "Aug" + " " + year;
                    //        mEdit1.setText(eight1);
                    mEdit1_dialog.setText(eight1);
                } else {
                    if (month == 8) {
                        //             mEdit.setText(year + " " + "0" + 8 + " " + day);
                        mEdit_dialog.setText(year + " " + "0" + 8 + " " + day);
                        eight = day + " " + "Aug" + " " + year;
                        //           mEdit1.setText(eight);
                        mEdit1_dialog.setText(eight);
                    }
                }

                if (month == 9 && day < 10) {
                    //         mEdit.setText(year + " " + "0" + 9 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + "0" + 9 + " " + "0" + day);
                    nine1 = "0" + day + " " + "Sep" + " " + year;
                    //        mEdit1.setText(nine1);
                    mEdit1_dialog.setText(nine1);
                } else {
                    if (month == 9) {
                        //          mEdit.setText(year + " " + "0" + 9 + " " + day);
                        mEdit_dialog.setText(year + " " + "0" + 9 + " " + day);
                        nine = day + " " + "Sep" + " " + year;
                        //         mEdit1.setText(nine);
                        mEdit1_dialog.setText(nine);
                    }
                }

                if (month == 10 && day < 10) {
                    //        mEdit.setText(year + " " + 10 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + 10 + " " + "0" + day);
                    ten1 = "0" + day + " " + "Oct" + " " + year;
                    //       mEdit1.setText(ten1);
                    mEdit1_dialog.setText(ten1);
                } else {
                    if (month == 10) {
                        //            mEdit.setText(year + " " + 10 + " " + day);
                        mEdit_dialog.setText(year + " " + 10 + " " + day);
                        ten = day + " " + "Oct" + " " + year;
                        //           mEdit1.setText(ten);
                        mEdit1_dialog.setText(ten);
                    }
                }

                if (month == 11 && day < 10) {
                    //        mEdit.setText(year + " " + 11 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + 11 + " " + "0" + day);
                    eleven1 = "0" + day + " " + "Nov" + " " + year;
                    //       mEdit1.setText(eleven1);
                    mEdit1_dialog.setText(eleven1);
                } else {
                    if (month == 11) {
                        //          mEdit.setText(year + " " + 11 + " " + day);
                        mEdit_dialog.setText(year + " " + 11 + " " + day);
                        eleven = day + " " + "Nov" + " " + year;
                        //          mEdit1.setText(eleven);
                        mEdit1_dialog.setText(eleven);
                    }
                }

                if (month == 12 && day < 10) {
                    //      mEdit.setText(year + " " + 12 + " " + "0" + day);
                    mEdit_dialog.setText(year + " " + 12 + " " + "0" + day);
                    twelve1 = "0" + day + " " + "Dec" + " " + year;
                    //      mEdit1.setText(twelve1);
                    mEdit1_dialog.setText(twelve1);
                } else {
                    if (month == 12) {
                        //         mEdit.setText(year + " " + 12 + " " + day);
                        mEdit_dialog.setText(year + " " + 12 + " " + day);
                        twelve = day + " " + "Dec" + " " + year;
                        //       mEdit1.setText(twelve);
                        mEdit1_dialog.setText(twelve);
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
//                    return new DatePickerDialog(HomeActivity.this, this, yy, mm, dd);
//                }
//
//
//                @Override
//                public void onDateSet(DatePicker view, int yy, int mm, int dd) {
//                    populateSetDate(yy, mm + 1, dd);
//                }
//            }

        });

        editText_from_day_visible_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(getActivity(), R.style.timepicker_date_dialog, timePickerListener_open_dialogue, hour, minute,
                        false);

                timePickerDialog.show();
            }
        });

        editText_to_day_visible_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(getActivity(), R.style.timepicker_date_dialog, timePickerListener_close_dialogue, hour, minute,
                        false);

                timePickerDialog.show();
            }
        });

    }

    private void setData_table(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        float val = 2;

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
//        for (int i = 0; i < count; i++) {
//            entries.add(new PieEntry((float) (Math.random() * mult) + mult / 5, mParties[i % mParties.length]));

//        Cursor cursor = db1.rawQuery("SELECT * FROM Ingredient_sold_details WHERE datetimee_new_from >= '"+editText1_filter.getText().toString()+"' AND datetimee_new_from <='"+editText2_filter.getText().toString()+"' GROUP BY datetimee_new_from", null);


//            chart_category.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
//            chart_user.setBackgroundColor(getResources().getColor(R.color.white));

        Cursor cursor = db1.rawQuery("SELECT * FROM Kotcancelled WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' GROUP BY tableid", null);
        if (cursor.moveToFirst()) {
            do {

                String id = cursor.getString(0);
                String na = cursor.getString(3);
                Cursor cursor1 = db1.rawQuery("SELECT SUM(total) FROM Kotcancelled WHERE datetimee_new >= '" + editText1_filter.getText().toString() + "' AND datetimee_new <='" + editText2_filter.getText().toString() + "' AND tableid = '"+na+"'", null);
                if (cursor1.moveToFirst()) {
                    float am = cursor1.getFloat(0);
                    if (na.length() > 10) {
                        String na1 = na.substring(0, 10);
                        entries.add(new PieEntry((float) ((am)), na1 + "..."));
                    } else {
                        entries.add(new PieEntry((float) ((am)), na));
                    }
                }
                cursor1.close();


            } while (cursor.moveToNext());
        }
        cursor.close();


//        }

//        ArrayList<PieEntry> entries1 = new ArrayList<PieEntry>();
        PieDataSet dataSet = new PieDataSet(entries, "");
//        dataSet.setSliceSpace(3f);
//        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);

        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
//        data.setValueTypeface(mTfLight);
        mChart_pie_table.setData(data);

        // undo all highlights
        mChart_pie_table.highlightValues(null);

        mChart_pie_table.invalidate();
    }

    private void updateTime_open_dialog(int hours, int mins) {

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

        String hour1 = "";
        if (hours < 10)
            hour1 = "0" + hours;
        else
            hour1 = String.valueOf(hours);


        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hour1).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        editText_from_day_visible_dialog.setText(aTime);
        editText_from_day_hide_dialog.setText(aTime);
        //  editText_from_day_visible.setText(aTime);
    }

    private void updateTime_close_dialog(int hours, int mins) {

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

        String hour1 = "";
        if (hours < 10)
            hour1 = "0" + hours;
        else
            hour1 = String.valueOf(hours);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hour1).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        editText_to_day_visible_dialog.setText(aTime);
        editText_to_day_hide_dialog.setText(aTime);
        // editText_to_day_visible.setText(aTime);
    }

    private android.app.TimePickerDialog.OnTimeSetListener timePickerListener_open_dialogue = new android.app.TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime_open_dialog(hour, minute);

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

            // editText_from_day_hide.setText(hour1 + "" + minutes1);
            editText_from_day_hide_dialog.setText(hour1 + "" + minutes1);


        }
    };

    private android.app.TimePickerDialog.OnTimeSetListener timePickerListener_close_dialogue = new android.app.TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime_close_dialog(hour, minute);

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

            //editText_to_day_hide.setText(hour1 + "" + minutes1);
            editText_to_day_hide_dialog.setText(hour1 + "" + minutes1);
        }
    };

    class DownloadMusicfromInternet1 extends AsyncTask<String, Void, Integer> {

        private ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);


        @Override
        protected Integer doInBackground(String... params) {

            db1.execSQL("delete from dummy_table_kot");

            float icount1 = 0, icount2 = 0;
            final String selectQuery = "SELECT * FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' GROUP BY tableid";

            Cursor cursor = db1.rawQuery(selectQuery, null);//replace to cursor = dbHelper.fetchAllHotels();
            if (cursor.moveToFirst()) {
                do {
                    String tableid = cursor.getString(3);


                    Cursor cursor1 = db1.rawQuery("SELECT SUM(total) FROM Kotcancelled WHERE tableid = '"+tableid+"' AND datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' GROUP BY tableid", null);
                    if (cursor1.moveToFirst()) {
                        icount1 = cursor1.getFloat(0);
                    }
                    cursor1.close();

                    Cursor cursor2 = db1.rawQuery("SELECT Count(*) FROM Kotcancelled WHERE tableid = '"+tableid+"' AND datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' GROUP BY tableid", null);
                    if (cursor2.moveToFirst()) {
                        icount2 = cursor2.getFloat(0);
                    }
                    cursor2.close();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("tableid", tableid);
                    contentValues.put("amount", String.valueOf(icount1));
                    contentValues.put("transactions", String.valueOf(icount2));
                    db1.insert("dummy_table_kot", null, contentValues);
                }while (cursor.moveToNext());
            }
            cursor.close();

            db1.execSQL("delete from dummy_user_kot");

            float icount11 = 0, icount21 = 0;
            final String selectQuery1 = "SELECT * FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' GROUP BY user";

            Cursor cursor1 = db1.rawQuery(selectQuery1, null);//replace to cursor = dbHelper.fetchAllHotels();
            if (cursor1.moveToFirst()) {
                do {
                    String user = cursor1.getString(10);


                    Cursor cursor11 = db1.rawQuery("SELECT SUM(total) FROM Kotcancelled WHERE user = '"+user+"' AND datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' GROUP BY user", null);
                    if (cursor11.moveToFirst()) {
                        icount11 = cursor11.getFloat(0);
                    }
                    cursor11.close();

                    Cursor cursor2 = db1.rawQuery("SELECT Count(*) FROM Kotcancelled WHERE user = '"+user+"' AND datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' GROUP BY user", null);
                    if (cursor2.moveToFirst()) {
                        icount21 = cursor2.getFloat(0);
                    }
                    cursor2.close();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("user", user);
                    contentValues.put("amount", String.valueOf(icount11));
                    contentValues.put("transactions", String.valueOf(icount21));
                    db1.insert("dummy_user_kot", null, contentValues);
                }while (cursor1.moveToNext());
            }
            cursor1.close();

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);

            progressBar_table_name.setVisibility(View.VISIBLE);
            table_name.setVisibility(View.GONE);

            progressBar_user_name.setVisibility(View.VISIBLE);
            user_name.setVisibility(View.GONE);

            progressBar_cyclic_table.setVisibility(View.VISIBLE);
            mChart_pie_table.setVisibility(View.INVISIBLE);
        }


        // Once Music File is downloaded
        @Override
        protected void onPostExecute(Integer file_url) {

            table_name.setText("NA");
            user_name.setText("NA");
            mChart_pie_table.notifyDataSetChanged();
            mChart_pie_table.invalidate();

            mChart_pie_table.setUsePercentValues(true);
            mChart_pie_table.getDescription().setEnabled(false);
            mChart_pie_table.setExtraOffsets(25, 10, 5, 5);

            mChart_pie_table.setDragDecelerationFrictionCoef(0.95f);

//            mChart_pie_table.setCenterTextTypeface(mTfLight);
//            mChart_pie_table.setCenterText(generateCenterSpannableText());

            mChart_pie_table.setDrawHoleEnabled(true);
            mChart_pie_table.setHoleColor(Color.WHITE);

            mChart_pie_table.setTransparentCircleColor(Color.WHITE);
            mChart_pie_table.setTransparentCircleAlpha(110);

            mChart_pie_table.setHoleRadius(58f);
            mChart_pie_table.setTransparentCircleRadius(61f);

            mChart_pie_table.setDrawCenterText(true);

            mChart_pie_table.setRotationAngle(0);
            // enable rotation of the chart by touch
            mChart_pie_table.setRotationEnabled(true);
            mChart_pie_table.setHighlightPerTapEnabled(true);

            // mChart.setUnit(" ?");
            // mChart.setDrawUnitsInChart(true);

            // add a selection listener
//            mChart_pie_table.setOnChartValueSelectedListener(this);

            setData_table(4, 100);

            mChart_pie_table.animateY(1400, Easing.EasingOption.EaseInOutQuad);
            // mChart.spin(2000, 0, 360);

            Legend l = mChart_pie_table.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(false);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(7f);
            l.setYOffset(0f);

            // entry label styling
            mChart_pie_table.setEntryLabelColor(Color.BLACK);
//            mChart_pie_table.setEntryLabelTypeface(mTfRegular);
            mChart_pie_table.setEntryLabelTextSize(12f);

//            dialog.dismiss();
            progressBar_table_name.setVisibility(View.GONE);
            table_name.setVisibility(View.VISIBLE);

            progressBar_user_name.setVisibility(View.GONE);
            user_name.setVisibility(View.VISIBLE);

            if ((total_sales_cancel.getText().toString().equals("0")) || (total_sales_cancel.getText().toString().equals("0.0")) || (total_sales_cancel.getText().toString().equals("0.00")) || (Float.parseFloat(total_sales_cancel.getText().toString()) <= 0)) {
                progressBar_cyclic_table.setVisibility(View.GONE);
                mChart_pie_table.setVisibility(View.INVISIBLE);
            }else {
                progressBar_cyclic_table.setVisibility(View.GONE);
                mChart_pie_table.setVisibility(View.VISIBLE);
            }

            Cursor oone = db1.rawQuery("SELECT MAX(amount) FROM dummy_table_kot", null);
            if (oone.moveToFirst()) {
                float one11 = oone.getFloat(0);
                Cursor oone1 = db1.rawQuery("SELECT * FROM dummy_table_kot WHERE amount = '" + one11 + "' ", null);
                if (oone1.moveToFirst()) {
                    String one1 = oone1.getString(1);
                    table_name.setText(one1);
                }
            }

            oone = db1.rawQuery("SELECT MAX(amount) FROM dummy_user_kot", null);
            if (oone.moveToFirst()) {
                float one11 = oone.getFloat(0);
                Cursor oone1 = db1.rawQuery("SELECT * FROM dummy_user_kot WHERE amount = '" + one11 + "' ", null);
                if (oone1.moveToFirst()) {
                    String one1 = oone1.getString(1);
                    user_name.setText(one1);
                }
            }

            if ((total_sales_cancel.getText().toString().equals("0")) || (total_sales_cancel.getText().toString().equals("0.0")) || (total_sales_cancel.getText().toString().equals("0.00")) || (Float.parseFloat(total_sales_cancel.getText().toString()) <= 0)) {
                mChart_pie_table.setVisibility(View.GONE);
                no_data_table.setVisibility(View.VISIBLE);
            }else {
                mChart_pie_table.setVisibility(View.VISIBLE);
                no_data_table.setVisibility(View.GONE);
            }

        }

    }

    private class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage(getString(R.string.setmessage13));
            this.dialog.show();

        }
        protected Boolean doInBackground(final String... args) {

//            File folder = getActivity().getFilesDir("IVEPOS_reports/IVEPOS_expenses_report");

            File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_reports/IVEPOS_kot_cancel_report");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            file = new File(exportDir, "IvePOS_kot_cancel_report"+currentDateandTimee1+"_"+timee1+".csv");
            try {
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

                String arrStr1[] ={"Date", "Time", "Quantity", "Itemname", "Price", "Total", "Reason","Category", "User"};
                csvWrite.writeNext(arrStr1);


                Cursor curCSV = db1.rawQuery("SELECT * FROM Kotcancelled WHERE datetimee_new >= '"+editText1_filter.getText().toString()+"' AND datetimee_new <='"+editText2_filter.getText().toString()+"' ", null);
                //csvWrite.writeNext(curCSV.getColumnNames());

                while(curCSV.moveToNext())  {
                    String arrStr[] ={curCSV.getString(6), curCSV.getString(7), curCSV.getString(2), curCSV.getString(1), curCSV.getString(13), curCSV.getString(14), curCSV.getString(4), curCSV.getString(9), curCSV.getString(10)};
//	                curCSV.getString(2),curCSV.getString(3),curCSV.getString(4),
                    csvWrite.writeNext(arrStr);

                }
                curCSV.close();

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

        Cursor cursorr = db.rawQuery("SELECT * FROM Email_setup", null);
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
        cursorr.close();

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
                getActivity(), android.Manifest.permission.GET_ACCOUNTS)) {
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

            String strcompanyname = "";
            Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
            if (getcom.moveToFirst()) {
                do {
                    strcompanyname = getcom.getString(1);
                } while (getcom.moveToNext());
            }
            getcom.close();

            String url = "www.intuitionsoftwares.com";


            String msg = "" + Uri.parse(url);

            Cursor cursor1 = db.rawQuery("SELECT * FROM Email_recipient", null);
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
                        String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/IVEPOS_reports/IVEPOS_kot_cancel_report/IvePOS_kot_cancel_report"+currentDateandTimee1+"_"+timee1+".csv";
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
}
