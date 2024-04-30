package com.intuition.ivepos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.intuition.ivepos.csv.RequestSingleton;
import com.intuition.ivepos.syncapp.StubProviderApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import androidx.appcompat.app.AppCompatActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

public class Online_deli_chan extends AppCompatActivity {

    private View mView;
    EditText date_of_order, rest_name, rest_addre, rest_num, rest_emailid, gst_no, owners_name, owners_num, owners_email_id, zomato_rela_manag_email_id;
    EditText zomato_regi_email_id, zomato_order_url, menu_timings, deli_charg, pack_charg, gst_any, contact_num, swiggy_relat_manag_email_id, swiggy_order_url;

    public SQLiteDatabase db1 = null;
    String strcompanyname;

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

    Uri contentUri,resultUri;

    String WebserviceUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_deli_chan_form);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(Online_deli_chan.this);
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

        db1 = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        mCredential = GoogleAccountCredential.usingOAuth2(
                Online_deli_chan.this.getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        mProgress = new ProgressDialog(Online_deli_chan.this, R.style.timepicker_date_dialog);
        mProgress.setMessage(getString(R.string.setmessage14));

        LinearLayout back_activity = (LinearLayout) findViewById(R.id.back_activity);
        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        date_of_order = (EditText) findViewById(R.id.date_of_order);
        rest_name = (EditText) findViewById(R.id.rest_name);
        rest_addre = (EditText) findViewById(R.id.rest_addre);
        rest_num = (EditText) findViewById(R.id.rest_num);
        rest_emailid = (EditText) findViewById(R.id.rest_emailid);
        gst_no = (EditText) findViewById(R.id.gst_no);
        owners_name = (EditText) findViewById(R.id.owners_name);
        owners_num = (EditText) findViewById(R.id.owners_num);
        owners_email_id = (EditText) findViewById(R.id.owners_email_id);
        zomato_rela_manag_email_id = (EditText) findViewById(R.id.zomato_rela_manag_email_id);
        zomato_regi_email_id = (EditText) findViewById(R.id.zomato_regi_email_id);
        zomato_order_url = (EditText) findViewById(R.id.zomato_order_url);
        menu_timings = (EditText) findViewById(R.id.menu_timings);
        deli_charg = (EditText) findViewById(R.id.deli_charg);
        pack_charg = (EditText) findViewById(R.id.pack_charg);
        gst_any = (EditText) findViewById(R.id.gst_any);
        contact_num = (EditText) findViewById(R.id.contact_num);
        swiggy_relat_manag_email_id = (EditText) findViewById(R.id.swiggy_relat_manag_email_id);
        swiggy_order_url = (EditText) findViewById(R.id.swiggy_order_url);

        Cursor cursor = db1.rawQuery("SELECT * FROM Online_order_form", null);
        if (cursor.moveToFirst()){
            String date_of_order2 = cursor.getString(1);
            String rest_name2 = cursor.getString(2);
            String rest_addre2 = cursor.getString(3);
            String rest_num2 = cursor.getString(4);
            String rest_emailid2 = cursor.getString(5);
            String gst_no2 = cursor.getString(6);
            String owners_name2 = cursor.getString(7);
            String owners_num2 = cursor.getString(8);
            String owners_email_id2 = cursor.getString(9);
            String zomato_rela_manag_email_id2 = cursor.getString(10);
            String zomato_regi_email_id2 = cursor.getString(11);
            String zomato_order_url2 = cursor.getString(12);
            String menu_timings2 = cursor.getString(13);
            String deli_charg2 = cursor.getString(14);
            String pack_charg2 = cursor.getString(15);
            String gst_any2 = cursor.getString(16);
            String contact_num2 = cursor.getString(17);
            String swiggy_relat_manag_email_id2 = cursor.getString(18);
            String swiggy_order_url2 = cursor.getString(19);

            date_of_order.setText(date_of_order2);
            rest_name.setText(rest_name2);
            rest_addre.setText(rest_addre2);
            rest_num.setText(rest_num2);
            rest_emailid.setText(rest_emailid2);
            gst_no.setText(gst_no2);
            owners_name.setText(owners_name2);
            owners_num.setText(owners_num2);
            owners_email_id.setText(owners_email_id2);
            zomato_rela_manag_email_id.setText(zomato_rela_manag_email_id2);
            zomato_regi_email_id.setText(zomato_regi_email_id2);
            zomato_order_url.setText(zomato_order_url2);
            menu_timings.setText(menu_timings2);
            deli_charg.setText(deli_charg2);
            pack_charg.setText(pack_charg2);
            gst_any.setText(gst_any2);
            contact_num.setText(contact_num2);
            swiggy_relat_manag_email_id.setText(swiggy_relat_manag_email_id2);
            swiggy_order_url.setText(swiggy_order_url2);

        }

        ImageButton action_exportmail = (ImageButton) findViewById(R.id.action_exportmail);
        action_exportmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();

                db1.execSQL("DELETE FROM Online_order_form");
                webservicequery("delete from Online_order_form");

                ContentValues contentValues = new ContentValues();
                contentValues.put("date_of_order", date_of_order.getText().toString());
                contentValues.put("rest_name", rest_name.getText().toString());
                contentValues.put("rest_addre", rest_addre.getText().toString());
                contentValues.put("rest_num", rest_num.getText().toString());
                contentValues.put("rest_emailid", rest_emailid.getText().toString());
                contentValues.put("gst_no", gst_no.getText().toString());
                contentValues.put("owners_name", owners_name.getText().toString());
                contentValues.put("owners_num", owners_num.getText().toString());
                contentValues.put("owners_email_id", owners_email_id.getText().toString());
                contentValues.put("zomato_rela_manag_email_id", zomato_rela_manag_email_id.getText().toString());
                contentValues.put("zomato_regi_email_id", zomato_regi_email_id.getText().toString());
                contentValues.put("zomato_order_url", zomato_order_url.getText().toString());
                contentValues.put("menu_timings", menu_timings.getText().toString());
                contentValues.put("deli_charg", deli_charg.getText().toString());
                contentValues.put("pack_charg", pack_charg.getText().toString());
                contentValues.put("gst_any", gst_any.getText().toString());
                contentValues.put("contact_num", contact_num.getText().toString());
                contentValues.put("swiggy_relat_manag_email_id", swiggy_relat_manag_email_id.getText().toString());
                contentValues.put("swiggy_order_url", swiggy_order_url.getText().toString());

//                db1.insert("Online_order_form", null, contentValues);

                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Online_order_form");
                resultUri = getContentResolver().insert(contentUri, contentValues);
                getContentResolver().notifyChange(resultUri, null);


                email();

            }
        });


    }


    public void email() {
        LinearLayout include = (LinearLayout) findViewById(R.id.include);
        final LinearLayout imageview = (LinearLayout) findViewById(R.id.imageview);

        mView = findViewById(R.id.f_view);

        ImageView imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);

        TextView date_of_order1 = (TextView)mView.findViewById(R.id.date_of_order1);
        TextView rest_name1 = (TextView)mView.findViewById(R.id.rest_name1);
        TextView rest_addre1 = (TextView)mView.findViewById(R.id.rest_addre1);
        TextView rest_num1 = (TextView)mView.findViewById(R.id.rest_num1);
        TextView rest_emailid1 = (TextView)mView.findViewById(R.id.rest_emailid1);
        TextView gst_no1 = (TextView)mView.findViewById(R.id.gst_no1);
        TextView owners_name1 = (TextView)mView.findViewById(R.id.owners_name1);
        TextView owners_num1 = (TextView)mView.findViewById(R.id.owners_num1);
        TextView owners_email_id1 = (TextView)mView.findViewById(R.id.owners_email_id1);
        TextView zomato_rela_manag_email_id1 = (TextView)mView.findViewById(R.id.zomato_rela_manag_email_id1);
        TextView zomato_regi_email_id1 = (TextView)mView.findViewById(R.id.zomato_regi_email_id1);
        TextView zomato_order_url1 = (TextView)mView.findViewById(R.id.zomato_order_url1);
        TextView menu_timings1 = (TextView)mView.findViewById(R.id.menu_timings1);
        TextView deli_charg1 = (TextView)mView.findViewById(R.id.deli_charg1);
        TextView pack_charg1 = (TextView)mView.findViewById(R.id.pack_charg1);
        TextView gst_any1 = (TextView)mView.findViewById(R.id.gst_any1);
        TextView contact_num1 = (TextView)mView.findViewById(R.id.contact_num1);
        TextView swiggy_relat_manag_email_id1 = (TextView)mView.findViewById(R.id.swiggy_relat_manag_email_id1);
        TextView swiggy_order_url1 = (TextView)mView.findViewById(R.id.swiggy_order_url1);

        date_of_order1.setText(date_of_order.getText().toString());
        rest_name1.setText(rest_name.getText().toString());
        rest_addre1.setText(rest_addre.getText().toString());
        rest_num1.setText(rest_num.getText().toString());
        rest_emailid1.setText(rest_emailid.getText().toString());
        gst_no1.setText(gst_no.getText().toString());
        owners_name1.setText(owners_name.getText().toString());
        owners_num1.setText(owners_num.getText().toString());
        owners_email_id1.setText(owners_email_id.getText().toString());
        zomato_rela_manag_email_id1.setText(zomato_rela_manag_email_id.getText().toString());
        zomato_regi_email_id1.setText(zomato_regi_email_id.getText().toString());
        zomato_order_url1.setText(zomato_order_url.getText().toString());
        menu_timings1.setText(menu_timings.getText().toString());
        deli_charg1.setText(deli_charg.getText().toString());
        pack_charg1.setText(pack_charg.getText().toString());
        gst_any1.setText(gst_any.getText().toString());
        contact_num1.setText(contact_num.getText().toString());
        swiggy_relat_manag_email_id1.setText(swiggy_relat_manag_email_id.getText().toString());
        swiggy_order_url1.setText(swiggy_order_url.getText().toString());


        mView.setDrawingCacheEnabled(true);
        mView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        mView.layout(0, 0, mView.getMeasuredWidth(), mView.getMeasuredHeight());
        mView.buildDrawingCache(true);
        include.setVisibility(View.INVISIBLE);
        imageview.setVisibility(View.INVISIBLE);

        imageview.setVisibility(View.INVISIBLE);
        Bitmap b = Bitmap.createBitmap(mView.getDrawingCache());
        mView.setDrawingCacheEnabled(false);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        imageViewPicture.setImageBitmap(b);

        Bitmap mBitmap = ((BitmapDrawable) imageViewPicture.getDrawable())
                .getBitmap();

        Drawable d = new BitmapDrawable(getResources(), mBitmap);


//        File exportDir = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_Online_Order");
        File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_Online_Order");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }


        File file = new File(exportDir, "IvePOS_online_order_form.jpeg");

        try {
            FileOutputStream ostream = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);
            ostream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Cursor getcommm = db1.rawQuery("SELECT * FROM Companydetailss", null);
        if (getcommm.moveToFirst()) {
            strcompanyname = getcommm.getString(1);
        }else {
            strcompanyname = "";
        }

        String url = "www.intuitionsoftwares.com";

        String msg = "Online work order form\n\n" +
                "Powered by: " + Uri.parse(url);

        Cursor cursore = db1.rawQuery("SELECT * FROM Email_setup", null);
        if (cursore.moveToFirst()) {
            //email setup
            final String un = cursore.getString(1);
            final String pwd = cursore.getString(2);
            final String client = cursore.getString(3);

            String em1 = "rohith@intuitionsoftwares.com";
            String em2 = "support@intuitionsoftwares.com";
            String em3 = "intuitionsoftwares@gmail.com";
            String em4 = "pravin.tripathi@werafoods.com";
            String em5 = "support@werafoods.com";
            if (client.toString().equals("Gmail")) {
//                email_id_send.setText(em1);
                getResultsFromApi();
                new MakeRequestTask2(mCredential).execute();
                new MakeRequestTask3(mCredential).execute();
                new MakeRequestTask4(mCredential).execute();
                new MakeRequestTask5(mCredential).execute();
                new MakeRequestTask6(mCredential).execute();
            }else {
                if (client.toString().equals("Yahoo")){
//                        Toast.makeText(Online_deli_chan.this, "yahoo "+un, Toast.LENGTH_LONG).show();

                    String toEmails = em1;
                    List toEmailList = Arrays.asList(toEmails
                            .split("\\s*,\\s*"));
                    new SendMailTask_Yahoo_Online_order_form(Online_deli_chan.this).execute(un,
                            pwd, toEmailList, strcompanyname, msg, "", "");

                    String toEmails2 = em2;
                    List toEmailList2 = Arrays.asList(toEmails2
                            .split("\\s*,\\s*"));
                    new SendMailTask_Yahoo_Online_order_form(Online_deli_chan.this).execute(un,
                            pwd, toEmailList2, strcompanyname, msg, "", "");

                    String toEmails3 = em3;
                    List toEmailList3 = Arrays.asList(toEmails3
                            .split("\\s*,\\s*"));
                    new SendMailTask_Yahoo_Online_order_form(Online_deli_chan.this).execute(un,
                            pwd, toEmailList3, strcompanyname, msg, "", "");

                    String toEmails4 = em4;
                    List toEmailList4 = Arrays.asList(toEmails4
                            .split("\\s*,\\s*"));
                    new SendMailTask_Yahoo_Online_order_form(Online_deli_chan.this).execute(un,
                            pwd, toEmailList4, strcompanyname, msg, "", "");

                    String toEmails5 = em5;
                    List toEmailList5 = Arrays.asList(toEmails5
                            .split("\\s*,\\s*"));
                    new SendMailTask_Yahoo_Online_order_form(Online_deli_chan.this).execute(un,
                            pwd, toEmailList5, strcompanyname, msg, "", "");



                }else {
                    if (client.toString().equals("Hotmail")){
//                            Toast.makeText(Online_deli_chan.this, "Hotmail and Outlook "+un, Toast.LENGTH_LONG).show();

                        String toEmails = em1;
                        List toEmailList = Arrays.asList(toEmails
                                .split("\\s*,\\s*"));
                        new SendMailTask_Hotmail_Outlook_Online_order_form(Online_deli_chan.this).execute(un,
                                pwd, toEmailList, strcompanyname, msg, "", "");

                        String toEmails2 = em2;
                        List toEmailList2 = Arrays.asList(toEmails2
                                .split("\\s*,\\s*"));
                        new SendMailTask_Hotmail_Outlook_Online_order_form(Online_deli_chan.this).execute(un,
                                pwd, toEmailList2, strcompanyname, msg, "", "");

                        String toEmails3 = em3;
                        List toEmailList3 = Arrays.asList(toEmails3
                                .split("\\s*,\\s*"));
                        new SendMailTask_Hotmail_Outlook_Online_order_form(Online_deli_chan.this).execute(un,
                                pwd, toEmailList3, strcompanyname, msg, "", "");

                        String toEmails4 = em4;
                        List toEmailList4 = Arrays.asList(toEmails4
                                .split("\\s*,\\s*"));
                        new SendMailTask_Hotmail_Outlook_Online_order_form(Online_deli_chan.this).execute(un,
                                pwd, toEmailList4, strcompanyname, msg, "", "");

                        String toEmails5 = em5;
                        List toEmailList5 = Arrays.asList(toEmails5
                                .split("\\s*,\\s*"));
                        new SendMailTask_Hotmail_Outlook_Online_order_form(Online_deli_chan.this).execute(un,
                                pwd, toEmailList5, strcompanyname, msg, "", "");

                    }else {
                        if (client.toString().equals("Office365")) {
//                                Toast.makeText(Online_deli_chan.this, "office 365 " + un, Toast.LENGTH_LONG).show();

                            String toEmails = em1;
                            List toEmailList = Arrays.asList(toEmails
                                    .split("\\s*,\\s*"));
                            new SendMailTask_Office365_Online_order_form(Online_deli_chan.this).execute(un,
                                    pwd, toEmailList, strcompanyname, msg, "", "");

                            String toEmails2 = em2;
                            List toEmailList2 = Arrays.asList(toEmails2
                                    .split("\\s*,\\s*"));
                            new SendMailTask_Office365_Online_order_form(Online_deli_chan.this).execute(un,
                                    pwd, toEmailList2, strcompanyname, msg, "", "");

                            String toEmails3 = em3;
                            List toEmailList3 = Arrays.asList(toEmails3
                                    .split("\\s*,\\s*"));
                            new SendMailTask_Office365_Online_order_form(Online_deli_chan.this).execute(un,
                                    pwd, toEmailList3, strcompanyname, msg, "", "");

                            String toEmails4 = em4;
                            List toEmailList4 = Arrays.asList(toEmails4
                                    .split("\\s*,\\s*"));
                            new SendMailTask_Office365_Online_order_form(Online_deli_chan.this).execute(un,
                                    pwd, toEmailList4, strcompanyname, msg, "", "");

                            String toEmails5 = em5;
                            List toEmailList5 = Arrays.asList(toEmails5
                                    .split("\\s*,\\s*"));
                            new SendMailTask_Office365_Online_order_form(Online_deli_chan.this).execute(un,
                                    pwd, toEmailList5, strcompanyname, msg, "", "");

                        }
                    }
                }
            }
            
        }else {
            //no sender
            final Dialog dialoge = new Dialog(Online_deli_chan.this, R.style.timepicker_date_dialog);
            dialoge.setContentView(R.layout.email_prerequisites_sender);
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

//            ImageView recipient_notset = (ImageView) dialoge.findViewById(R.id.recipient_notset);
//            ImageView recipient_set = (ImageView) dialoge.findViewById(R.id.recipient_set);
//
//            ImageView sender_notset = (ImageView) dialoge.findViewById(R.id.sender_notset);
//            ImageView sender_set = (ImageView) dialoge.findViewById(R.id.sender_set);
//
//            LinearLayout recipient_layout = (LinearLayout) dialoge.findViewById(R.id.recipient_layout);
//            recipient_layout.setVisibility(View.GONE);
//
//            sender_notset.setVisibility(View.VISIBLE);
//
//            recipient_set.setVisibility(View.VISIBLE);

            Button gotosettings = (Button) dialoge.findViewById(R.id.gotosettings);
            gotosettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Online_deli_chan.this, EmailSetup.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
//                                                                Online_deli_chan.this.finish();
                    dialoge.dismiss();
                }
            });
        }
        

    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                Online_deli_chan.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }
    
    private void getResultsFromApi() {

        Cursor cursorr = db1.rawQuery("SELECT * FROM Email_setup", null);
        if (cursorr.moveToFirst()) {
            String unn = cursorr.getString(1);
//            Toast.makeText(Online_deli_chan.this, "a4 " + unn, Toast.LENGTH_SHORT).show();

            TextView tvv = new TextView(Online_deli_chan.this);
            tvv.setText(unn);

            if (tvv.getText().toString().equals("")) {

            }else {
                mCredential.setSelectedAccountName(tvv.getText().toString());
            }
        }

        if (! isGooglePlayServicesAvailable()) {
//            Toast.makeText(Online_deli_chan.this, "1", Toast.LENGTH_SHORT).show();
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
//            Toast.makeText(Online_deli_chan.this, "2", Toast.LENGTH_SHORT).show();
//            chooseAccount();
        } else if (! isDeviceOnline()) {
//            Toast.makeText(Online_deli_chan.this, "3", Toast.LENGTH_SHORT).show();
//            mOutputText.setText("No network connection available.");
        } else {
//            Toast.makeText(Online_deli_chan.this, "4", Toast.LENGTH_SHORT).show();
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
                apiAvailability.isGooglePlayServicesAvailable(Online_deli_chan.this);
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
                apiAvailability.isGooglePlayServicesAvailable(Online_deli_chan.this);
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
//        Toast.makeText(Online_deli_chan.this, "s1", Toast.LENGTH_SHORT).show();
        if (EasyPermissions.hasPermissions(
                Online_deli_chan.this, android.Manifest.permission.GET_ACCOUNTS)) {
            String accountName = Online_deli_chan.this.getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
//            Toast.makeText(Online_deli_chan.this, "s2", Toast.LENGTH_SHORT).show();
//            if (accountName != null) {
//                mCredential.setSelectedAccountName(accountName);
//                Toast.makeText(Online_deli_chan.this, "s3", Toast.LENGTH_SHORT).show();
//                getResultsFromApi();
//            } else {
            // Start a dialog from which the user can choose an account
            startActivityForResult(
                    mCredential.newChooseAccountIntent(),
                    REQUEST_ACCOUNT_PICKER);
//            Toast.makeText(Online_deli_chan.this, "s4", Toast.LENGTH_SHORT).show();
//            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
//            Toast.makeText(Online_deli_chan.this, "s5", Toast.LENGTH_SHORT).show();
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



    private class MakeRequestTask2 extends AsyncTask<Void, Void, String> {
        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;
//        private View view = sendFabButton;

        public MakeRequestTask2(GoogleAccountCredential credential) {
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
                } while (getcom.moveToNext());
            }

            String url = "www.intuitionsoftwares.com";

//            String msg = "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
//                    "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
//                    "Powered by: " + Uri.parse(url);

            TextView textView = new TextView(Online_deli_chan.this);
            textView.setText(R.string.underline);



            String msg = "Online work order form\n\n" +
                    "Powered by: " + Uri.parse(url);

//            Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
//            if (cursor1.moveToFirst()) {
//                do {
                    String unn = "rohith@intuitionsoftwares.com";
                    TextView edtToAddress = new TextView(Online_deli_chan.this);
                    edtToAddress.setText(unn);

                    TextView edtSubject = new TextView(Online_deli_chan.this);
                    edtSubject.setText(strcompanyname);

                    TextView edtMessage = new TextView(Online_deli_chan.this);
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

//                        String filename = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_Online_Order/IvePOS_online_order_form.jpeg";
                        String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/IVEPOS_Online_Order/IvePOS_online_order_form.jpeg";


                        File f = new File(filename);
//
                        mimeMessage = createEmailWithAttachment(to, from, subject, body, f);



//                        mimeMessage = createEmail(to, from, subject, body);
                        response = sendMessage(mService, user, mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }

//                } while (cursor1.moveToNext());
//            }
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
                Toast.makeText(Online_deli_chan.this, "not success", Toast.LENGTH_SHORT).show();
//                showMessage(view, "No results returned.");
            } else {
                Toast.makeText(Online_deli_chan.this, "success", Toast.LENGTH_SHORT).show();
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


    private class MakeRequestTask3 extends AsyncTask<Void, Void, String> {
        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;
//        private View view = sendFabButton;

        public MakeRequestTask3(GoogleAccountCredential credential) {
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
                } while (getcom.moveToNext());
            }

            String url = "www.intuitionsoftwares.com";

//            String msg = "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
//                    "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
//                    "Powered by: " + Uri.parse(url);

            TextView textView = new TextView(Online_deli_chan.this);
            textView.setText(R.string.underline);



            String msg = "Online work order form\n\n" +
                    "Powered by: " + Uri.parse(url);

//            Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
//            if (cursor1.moveToFirst()) {
//                do {
            String unn = "support@intuitionsoftwares.com";
            TextView edtToAddress = new TextView(Online_deli_chan.this);
            edtToAddress.setText(unn);

            TextView edtSubject = new TextView(Online_deli_chan.this);
            edtSubject.setText(strcompanyname);

            TextView edtMessage = new TextView(Online_deli_chan.this);
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

//                String filename = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_Online_Order/IvePOS_online_order_form.jpeg";
                String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/IVEPOS_Online_Order/IvePOS_online_order_form.jpeg";


                File f = new File(filename);
//
                mimeMessage = createEmailWithAttachment(to, from, subject, body, f);



//                        mimeMessage = createEmail(to, from, subject, body);
                response = sendMessage(mService, user, mimeMessage);
            } catch (MessagingException e) {
                e.printStackTrace();
            }

//                } while (cursor1.moveToNext());
//            }
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
                Toast.makeText(Online_deli_chan.this, "not success", Toast.LENGTH_SHORT).show();
//                showMessage(view, "No results returned.");
            } else {
                Toast.makeText(Online_deli_chan.this, "success", Toast.LENGTH_SHORT).show();
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

    private class MakeRequestTask4 extends AsyncTask<Void, Void, String> {
        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;
//        private View view = sendFabButton;

        public MakeRequestTask4(GoogleAccountCredential credential) {
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
                } while (getcom.moveToNext());
            }

            String url = "www.intuitionsoftwares.com";

//            String msg = "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
//                    "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
//                    "Powered by: " + Uri.parse(url);

            TextView textView = new TextView(Online_deli_chan.this);
            textView.setText(R.string.underline);



            String msg = "Online work order form\n\n" +
                    "Powered by: " + Uri.parse(url);

//            Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
//            if (cursor1.moveToFirst()) {
//                do {
            String unn = "intuitionsoftwares@gmail.com";
            TextView edtToAddress = new TextView(Online_deli_chan.this);
            edtToAddress.setText(unn);

            TextView edtSubject = new TextView(Online_deli_chan.this);
            edtSubject.setText(strcompanyname);

            TextView edtMessage = new TextView(Online_deli_chan.this);
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

//                String filename = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_Online_Order/IvePOS_online_order_form.jpeg";
                String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/IVEPOS_Online_Order/IvePOS_online_order_form.jpeg";


                File f = new File(filename);
//
                mimeMessage = createEmailWithAttachment(to, from, subject, body, f);



//                        mimeMessage = createEmail(to, from, subject, body);
                response = sendMessage(mService, user, mimeMessage);
            } catch (MessagingException e) {
                e.printStackTrace();
            }

//                } while (cursor1.moveToNext());
//            }
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
                Toast.makeText(Online_deli_chan.this, "not success", Toast.LENGTH_SHORT).show();
//                showMessage(view, "No results returned.");
            } else {
                Toast.makeText(Online_deli_chan.this, "success", Toast.LENGTH_SHORT).show();
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

    private class MakeRequestTask5 extends AsyncTask<Void, Void, String> {
        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;
//        private View view = sendFabButton;

        public MakeRequestTask5(GoogleAccountCredential credential) {
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
                } while (getcom.moveToNext());
            }

            String url = "www.intuitionsoftwares.com";

//            String msg = "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
//                    "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
//                    "Powered by: " + Uri.parse(url);

            TextView textView = new TextView(Online_deli_chan.this);
            textView.setText(R.string.underline);



            String msg = "Online work order form\n\n" +
                    "Powered by: " + Uri.parse(url);

//            Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
//            if (cursor1.moveToFirst()) {
//                do {
            String unn = "pravin.tripathi@werafoods.com";
            TextView edtToAddress = new TextView(Online_deli_chan.this);
            edtToAddress.setText(unn);

            TextView edtSubject = new TextView(Online_deli_chan.this);
            edtSubject.setText(strcompanyname);

            TextView edtMessage = new TextView(Online_deli_chan.this);
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

//                String filename = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_Online_Order/IvePOS_online_order_form.jpeg";
                String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/IVEPOS_Online_Order/IvePOS_online_order_form.jpeg";


                File f = new File(filename);
//
                mimeMessage = createEmailWithAttachment(to, from, subject, body, f);



//                        mimeMessage = createEmail(to, from, subject, body);
                response = sendMessage(mService, user, mimeMessage);
            } catch (MessagingException e) {
                e.printStackTrace();
            }

//                } while (cursor1.moveToNext());
//            }
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
                Toast.makeText(Online_deli_chan.this, "not success", Toast.LENGTH_SHORT).show();
//                showMessage(view, "No results returned.");
            } else {
                Toast.makeText(Online_deli_chan.this, "success", Toast.LENGTH_SHORT).show();
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

    private class MakeRequestTask6 extends AsyncTask<Void, Void, String> {
        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;
//        private View view = sendFabButton;

        public MakeRequestTask6(GoogleAccountCredential credential) {
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
                } while (getcom.moveToNext());
            }

            String url = "www.intuitionsoftwares.com";

//            String msg = "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
//                    "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
//                    "Powered by: " + Uri.parse(url);

            TextView textView = new TextView(Online_deli_chan.this);
            textView.setText(R.string.underline);



            String msg = "Online work order form\n\n" +
                    "Powered by: " + Uri.parse(url);

//            Cursor cursor1 = db1.rawQuery("SELECT * FROM Email_recipient", null);
//            if (cursor1.moveToFirst()) {
//                do {
            String unn = "support@werafoods.com";
            TextView edtToAddress = new TextView(Online_deli_chan.this);
            edtToAddress.setText(unn);

            TextView edtSubject = new TextView(Online_deli_chan.this);
            edtSubject.setText(strcompanyname);

            TextView edtMessage = new TextView(Online_deli_chan.this);
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

//                String filename = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_Online_Order/IvePOS_online_order_form.jpeg";
                String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/IVEPOS_Online_Order/IvePOS_online_order_form.jpeg";


                File f = new File(filename);
//
                mimeMessage = createEmailWithAttachment(to, from, subject, body, f);



//                        mimeMessage = createEmail(to, from, subject, body);
                response = sendMessage(mService, user, mimeMessage);
            } catch (MessagingException e) {
                e.printStackTrace();
            }

//                } while (cursor1.moveToNext());
//            }
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
                Toast.makeText(Online_deli_chan.this, "not success", Toast.LENGTH_SHORT).show();
//                showMessage(view, "No results returned.");
            } else {
                Toast.makeText(Online_deli_chan.this, "success", Toast.LENGTH_SHORT).show();
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


    public void webservicequery(final String webserviceQuery){


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(Online_deli_chan.this);
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
        RequestQueue queue;
        StringRequest sr1;
        // queue = Volley.newRequestQueue(getActivity());

        queue= RequestSingleton.getInstance(Online_deli_chan.this).getInstance();

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
