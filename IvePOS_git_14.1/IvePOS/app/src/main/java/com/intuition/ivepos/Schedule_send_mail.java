package com.intuition.ivepos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.Message;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * Created by Rohithkumar on 6/12/2017.
 */

public class Schedule_send_mail extends BroadcastReceiver {


    final String emailPort = "587";// gmail's smtp port
    final String smtpAuth = "true";
    final String starttls = "true";
    final String emailHost = "smtp.office365.com";
    // final String fromUser = "giftvincy@gmail.com";
    // final String fromUserEmailPassword = "jk2008gv";

//    String fromEmail;
//    String fromPassword;
    List<String> toEmailList;
    String emailSubject;
    String emailBody;



    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;

    SimpleDateFormat sdff2, sdff1;
    String currentDateandTimee1;
    String timee1;


    GoogleAccountCredential mCredential; String response;
    ProgressDialog mProgress;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Gmail API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { GmailScopes.GMAIL_SEND };


    TextView edtToAddress, edtSubject, edtMessage;

    public SQLiteDatabase db = null;
    public SQLiteDatabase db1 = null;

    String strcompanyname, straddress1;

    File file=null, file1=null;


    @Override
    public void onReceive(Context context, Intent intent) {

        mCredential = GoogleAccountCredential.usingOAuth2(
                context.getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

//        mProgress = new ProgressDialog(context);
//        mProgress.setMessage(getString(R.string.setmessage16));


        sdff2 = new SimpleDateFormat("ddMMMyy");
        currentDateandTimee1 = sdff2.format(new Date());

        Date dt = new Date();
        sdff1 = new SimpleDateFormat("hhmmssaa");
        timee1 = sdff1.format(dt);

        Log.d("hiiiiii11", "Schedule_send_mail");


        db = context.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        db1 = context.openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);

        File dbFile=context.getDatabasePath("mydb_Salesdata");
        //Log.v(TAG, "Db path is: "+dbFile);  //get the path of db

//        File exportDir = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_reports/IVEPOS_Billwise_items_report");
//        if (!exportDir.exists()) {
//            exportDir.mkdirs();
//        }
//
//        File exportDir1 = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_reports/IVEPOS_sales_report");
//        if (!exportDir1.exists()) {
//            exportDir1.mkdirs();
//        }

        File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_reports/IVEPOS_Billwise_items_report");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File exportDir1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_reports/IVEPOS_sales_report");
        if (!exportDir1.exists()) {
            exportDir1.mkdirs();
        }

        file = new File(exportDir, "IvePOS_Billwise_items_report"+currentDateandTimee1+"_"+timee1+".csv");
        file1 = new File(exportDir1, "IvePOS_sales_report"+currentDateandTimee1+"_"+timee1+".csv");
        try {

            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

            // this is the Column of the table and same for Header of CSV file
            String arrStr1[] ={"Date", "Time", "User", "Bill count", "Bill no.", "Type", "Mode", "Table", "Itemname", "Qty.", "Individualprice", "Total price"};
            csvWrite.writeNext(arrStr1);

            db1 = context.openOrCreateDatabase("mydb_Salesdata",
                    Context.MODE_PRIVATE, null);
            Cursor curCSV = db1.rawQuery("SELECT * FROM Generalorderlistascdesc1",null);
            //csvWrite.writeNext(curCSV.getColumnNames());

            while(curCSV.moveToNext())  {
                String arrStr[] ={curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(18), curCSV.getString(4), curCSV.getString(9),
                        curCSV.getString(8), curCSV.getString(12), curCSV.getString(10), curCSV.getString(11), curCSV.getString(13), curCSV.getString(17)};
//	                curCSV.getString(2),curCSV.getString(3),curCSV.getString(4),
                csvWrite.writeNext(arrStr);

            }

            csvWrite.close();

            file1.createNewFile();
            CSVWriter csvWrite1 = new CSVWriter(new FileWriter(file1));

            // this is the Column of the table and same for Header of CSV file
            String arrStr11[] ={"Date", "Time", "User", "Bill count", "Bill no.", "Type", "Mode", "Table", "Subtotal", "Tax", "Discount", "Total"};
            csvWrite1.writeNext(arrStr11);

            db1 = context.openOrCreateDatabase("mydb_Salesdata",
                    Context.MODE_PRIVATE, null);
            Cursor curCSVv = db1.rawQuery("SELECT * FROM Billnumber",null);
            //csvWrite.writeNext(curCSV.getColumnNames());

            if (curCSVv.moveToFirst())  {
                do {
                    String billnos = curCSVv.getString(1);
                    Cursor curCSV1 = db1.rawQuery("SELECT * FROM Generalorderlistascdesc1 WHERE billno = '"+billnos+"'",null);
                    if (curCSV1.moveToFirst()){
                        String arrStr[] ={curCSV1.getString(1), curCSV1.getString(2), curCSVv.getString(3), curCSVv.getString(11), curCSVv.getString(1), curCSVv.getString(6),
                                curCSVv.getString(5), curCSV1.getString(12), curCSVv.getString(7), curCSVv.getString(8), curCSV1.getString(7), curCSVv.getString(2)};
//	                curCSV.getString(2),curCSV.getString(3),curCSV.getString(4),
                        csvWrite1.writeNext(arrStr);
                    }
                    curCSV1.close();
                }while (curCSVv.moveToNext());

            }
            curCSVv.close();
            csvWrite1.close();

//            return true;

        }
        catch (IOException e){
            Log.e("MainActivity", e.getMessage(), e);
//            return false;

        }

        edtToAddress = new TextView(context);
        edtSubject = new TextView(context);
        edtMessage = new TextView(context);

//        getResultsFromApi(context);

        Cursor cursorr = db.rawQuery("SELECT * FROM Email_setup", null);
        if (cursorr.moveToFirst()) {
            String unn = cursorr.getString(1);
//            Toast.makeText(getActivity(), "a4 " + unn, Toast.LENGTH_SHORT).show();

            TextView tvv = new TextView(context);
            tvv.setText(unn);

            if (tvv.getText().toString().equals("")) {

            }else {
                mCredential.setSelectedAccountName(tvv.getText().toString());
            }
        }


        Cursor cursor = db.rawQuery("SELECT * FROM Companydetailss", null);
        if (cursor.moveToFirst()) {
            strcompanyname = cursor.getString(1);
        }else {
            strcompanyname = "";
        }

        String msg = "";

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
                    if (em_ca.toString().equals("Gmail")) {
                        new MakeRequestTask1(mCredential).execute();

                        new MakeRequestTask(mCredential, context).execute();
                        new MakeRequestTask_billwise(mCredential, context).execute();
                    }else {
                        if (em_ca.toString().equals("Yahoo")){
//                            Toast.makeText(getActivity(), "yahoo "+un, Toast.LENGTH_LONG).show();
                            Cursor cursor1 = db.rawQuery("SELECT * FROM Email_recipient", null);
                            if (cursor1.moveToFirst()) {
                                do {
                                    String unn = cursor1.getString(3);
                                    String toEmails = unn;
                                    List toEmailList = Arrays.asList(toEmails
                                            .split("\\s*,\\s*"));
                                    new SendMailTask_Yahoo_attachment_GenOrderlist((Activity) context).execute(un,
                                            pwd, toEmailList, strcompanyname, msg, currentDateandTimee1, timee1);
                                    new SendMailTask_Yahoo_attachment_GenOrderlist1((Activity) context).execute(un,
                                            pwd, toEmailList, strcompanyname, msg, currentDateandTimee1, timee1);
                                } while (cursor1.moveToNext());
                            }


                        }else {
                            if (em_ca.toString().equals("Hotmail")){
//                                Toast.makeText(getActivity(), "Hotmail and Outlook "+un, Toast.LENGTH_LONG).show();
                                Cursor cursor1 = db.rawQuery("SELECT * FROM Email_recipient", null);
                                if (cursor1.moveToFirst()) {
                                    do {
                                        String unn = cursor1.getString(3);
                                        String toEmails = unn;
                                        List toEmailList = Arrays.asList(toEmails
                                                .split("\\s*,\\s*"));
                                        new SendMailTask_Hotmail_Outlook_attachment_GenOrderlist((Activity) context).execute(un,
                                                pwd, toEmailList, strcompanyname, msg, currentDateandTimee1, timee1);
                                        new SendMailTask_Hotmail_Outlook_attachment_GenOrderlist1((Activity) context).execute(un,
                                                pwd, toEmailList, strcompanyname, msg, currentDateandTimee1, timee1);
                                    } while (cursor1.moveToNext());
                                }
                            }else {
                                if (em_ca.toString().equals("Office365")) {
//                                    Toast.makeText(getActivity(), "office 365 " + un, Toast.LENGTH_LONG).show();
                                    Cursor cursor1 = db.rawQuery("SELECT * FROM Email_recipient", null);
                                    if (cursor1.moveToFirst()) {
                                        do {
                                            String unn = cursor1.getString(3);
                                            String toEmails = unn;
                                            List toEmailList = Arrays.asList(toEmails
                                                    .split("\\s*,\\s*"));
//                                            new SendMailTask_Office365_attachment_GenOrderlist((Activity) context).execute(un,
//                                                    pwd, toEmailList, strcompanyname, msg, currentDateandTimee1, timee1);
//                                            new SendMailTask_Office365_attachment_GenOrderlist1((Activity) context).execute(un,
//                                                    pwd, toEmailList, strcompanyname, msg, currentDateandTimee1, timee1);

//                                            new SendMailTask_Outlook((Activity) context).execute(un,
//                                                    pwd, toEmailList, strcompanyname, "OTP is " + time1);

//                                            try {
//                                                Office365_attachment_Genorderlist androidEmail = new Office365_attachment_Genorderlist(un,
//                                                        pwd,
//                                                        toEmailList,
//                                                        strcompanyname,
//                                                        msg, currentDateandTimee1, timee1);
//                                                androidEmail.createEmailMessage();
////                                                publishProgress("Sending email....");
//                                                try {
//                                                    androidEmail.sendEmail();
//                                                }catch (Exception e){
//                                                    Log.i("SendMailTask_Office365", "Mail not Sent.");
//                                                }
//                                            } catch (Exception e) {
////                                                publishProgress(e.getMessage());
//                                                Log.e("SendMailTask_Office365", e.getMessage(), e);
//                                            }

//                                            this.fromEmail = un;
//                                            this.fromPassword = pwd;
                                            this.toEmailList = toEmailList;
                                            this.emailSubject = strcompanyname;
                                            this.emailBody = msg;

                                            this.currentDateandTimee1 = currentDateandTimee1;
                                            this.timee1 = timee1;

                                            emailProperties = System.getProperties();
                                            emailProperties.put("mail.smtp.port", emailPort);
                                            emailProperties.put("mail.smtp.auth", smtpAuth);
                                            emailProperties.put("mail.smtp.starttls.enable", starttls);
                                            Log.i("Office365", "Mail server properties set.");

                                            List<String> toe = toEmailList;
                                            try {
//                                                createEmailMessage();
//                                                sendEmail();

                                                mailSession = Session.getDefaultInstance(emailProperties, null);
                                                emailMessage = new MimeMessage(mailSession);

                                                emailMessage.setFrom(new InternetAddress(un, un));
                                                for (String toEmail : toe) {
                                                    Log.i("GMail","toEmail: "+toEmail);
                                                    emailMessage.addRecipient(javax.mail.Message.RecipientType.TO,
                                                            new InternetAddress(toEmail));
                                                }

                                                emailMessage.setSubject(emailSubject);
                                                emailMessage.setContent(emailBody, "text/html");// for a html email
                                                // emailMessage.setText(emailBody);// for a text email
                                                Log.i("GMail", "Email Message created.");

                                                Transport transport = mailSession.getTransport("smtp");
                                                transport.connect(emailHost, un, pwd);
                                                Log.i("Office365","allrecipients: "+emailMessage.getAllRecipients());
                                                transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
                                                transport.close();
                                            } catch (Exception e) {
//                                                publishProgress(e.getMessage());
                                                Log.e("SendMailTask_Office365", e.getMessage(), e);
                                            }

                                        } while (cursor1.moveToNext());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }



    }

//    public MimeMessage createEmailMessage() throws AddressException,
//            MessagingException, UnsupportedEncodingException {
//
//        mailSession = Session.getDefaultInstance(emailProperties, null);
//        emailMessage = new MimeMessage(mailSession);
//
//        emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));
//        for (String toEmail : toEmailList) {
//            Log.i("Office365","toEmail: "+toEmail);
//            emailMessage.addRecipient(javax.mail.Message.RecipientType.TO,
//                    new InternetAddress(toEmail));
//        }
//
//        BodyPart messageBodyPart = new MimeBodyPart();
//
//        // Now set the actual message
//        messageBodyPart.setText(emailBody);
//        // Create a multipar message
//        Multipart multipart = new MimeMultipart();
//
//        // Set text message part
//        multipart.addBodyPart(messageBodyPart);
//        messageBodyPart = new MimeBodyPart();
////        String filename = "sdcard/Download/IvePOS_items_report"+currentDateandTimee1+"_"+timee1+".csv";
//        String filename = "sdcard/IVEPOS_reports/IVEPOS_sales_report/IvePOS_sales_report"+"13Jun17"+"_"+"070700PM"+".csv";
////        String filename = "sdcard/IVEPOS_reports/IVEPOS_product_report/IvePOS_product_report"+currentDateandTimee1+"_"+timee1+".csv";
////        String filename = "sdcard/IVEPOS_reports/IVEPOS_customer_list/IvePOS_customer_list"+currentDateandTimee1+"_"+timee1+".csv";
//        DataSource source = new FileDataSource(filename);
//        messageBodyPart.setDataHandler(new DataHandler(source));
//        messageBodyPart.setFileName(filename);
//        multipart.addBodyPart(messageBodyPart);
//
//
//        emailMessage.setText(emailBody);
//        emailMessage.setContent(multipart);
//        emailMessage.setSubject(emailSubject);
//
////        emailMessage.setSubject(emailSubject);
////        emailMessage.setContent(emailBody, "text/html");// for a html email
//        // emailMessage.setText(emailBody);// for a text email
//        Log.i("Office365", "Email Message created.");
//        return emailMessage;
//    }
//
//    public void sendEmail() throws MessagingException {
//
////        try {
//        Transport transport = mailSession.getTransport("smtp");
//        transport.connect(emailHost, fromEmail, fromPassword);
//        Log.i("Office365","allrecipients: "+emailMessage.getAllRecipients());
//        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
//        transport.close();
//        Log.i("Office365", "Email sent successfully.");
////        }catch (Exception e){
////            Log.i("GMail", "Email not sent.");
//////            AlertDialogog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
////        }
//
//    }


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
//            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
//            System.out.println("labelsss " + output);//will be displaying details and folders in mail like inbox, sent, outbox, junk, etc
//            mProgress.hide();
            if (output == null || output.size() == 0) {
//                mOutputText.setText("No results returned.");
            } else {
                output.add(0, "Data retrieved using the Gmail API:");
//                mOutputText.setText(TextUtils.join("\n", output));
            }
        }

        @Override
        protected void onCancelled() {
//            mProgress.hide();
//            if (mLastError != null) {
//                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
//                    showGooglePlayServicesAvailabilityErrorDialog(
//                            ((GooglePlayServicesAvailabilityIOException) mLastError)
//                                    .getConnectionStatusCode());
//                } else if (mLastError instanceof UserRecoverableAuthIOException) {
//                    startActivityForResult(
//                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
//                            EmailSetup_Google.REQUEST_AUTHORIZATION);
//                } else {
////                    mOutputText.setText("The following error occurred:\n"
////                            + mLastError.getMessage());
//                }
//            } else {
////                mOutputText.setText("Request cancelled.");
//            }
        }
    }


    private class MakeRequestTask extends AsyncTask<Void, Void, String> {
        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;
//        private View view = sendFabButton;

        public MakeRequestTask(GoogleAccountCredential credential, Context context) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.gmail.Gmail.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName(context.getResources().getString(R.string.app_name))
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

            Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
            if (getcom.moveToFirst()) {
                do {
                    strcompanyname = getcom.getString(1);
                    straddress1 = getcom.getString(14);
                } while (getcom.moveToNext());
            }

            String url = "www.intuitionsoftwares.com";

            String msg = "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
                    "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
                    "Powered by: " + Uri.parse(url);

            Cursor cursor1 = db.rawQuery("SELECT * FROM Email_recipient", null);
            if (cursor1.moveToFirst()) {
                do {
                    String unn = cursor1.getString(3);
//                    TextView edtToAddress = new TextView(getActivity());
                    edtToAddress.setText(unn);

//                    TextView edtSubject = new TextView(getActivity());
                    edtSubject.setText(strcompanyname);

//                    TextView edtMessage = new TextView(getActivity());
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

//                        String filename = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_reports/IVEPOS_sales_report/IvePOS_sales_report"+currentDateandTimee1+"_"+timee1+".csv";
                        String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/IVEPOS_reports/IVEPOS_sales_report/IvePOS_sales_report"+currentDateandTimee1+"_"+timee1+".csv";

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
            Message message = createMessageWithEmail(email);
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

        private Message createMessageWithEmail(MimeMessage email)
                throws MessagingException, IOException {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            email.writeTo(bytes);
            String encodedEmail = Base64.encodeBase64URLSafeString(bytes.toByteArray());
            Message message = new Message();
            message.setRaw(encodedEmail);
            return message;
        }

        @Override
        protected void onPreExecute() {
//            mProgress.show();
        }

        @Override
        protected void onPostExecute(String output) {
            Log.d("post execute", "error");
//            mProgress.hide();
            if (output == null || output.length() == 0) {
//                Toast.makeText(getActivity(), "not success", Toast.LENGTH_SHORT).show();
//                showMessage(view, "No results returned.");
            } else {
//                Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
//                showMessage(view, output);
            }
        }

        @Override
        protected void onCancelled() {
//            mProgress.hide();

        }
    }

    private class MakeRequestTask_billwise extends AsyncTask<Void, Void, String> {
        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;
//        private View view = sendFabButton;

        public MakeRequestTask_billwise(GoogleAccountCredential credential, Context context) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.gmail.Gmail.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName(context.getResources().getString(R.string.app_name))
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

            Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
            if (getcom.moveToFirst()) {
                do {
                    strcompanyname = getcom.getString(1);
                    straddress1 = getcom.getString(14);
                } while (getcom.moveToNext());
            }

            String url = "www.intuitionsoftwares.com";

            String msg = "Disclaimer: This information may be confidential and is intended only for the recipient. If you think this mail is not for you, please delete it.\n\n" +
                    "You can open the attached file with apps like Microsoft excel, WPS office, Apple numbers and etc.,\n\n" +
                    "Powered by: " + Uri.parse(url);

            Cursor cursor1 = db.rawQuery("SELECT * FROM Email_recipient", null);
            if (cursor1.moveToFirst()) {
                do {
                    String unn = cursor1.getString(3);
//                    TextView edtToAddress = new TextView(getActivity());
                    edtToAddress.setText(unn);

//                    TextView edtSubject = new TextView(getActivity());
                    edtSubject.setText(strcompanyname);

//                    TextView edtMessage = new TextView(getActivity());
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

//                        String filename = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_reports/IVEPOS_Billwise_items_report/IvePOS_Billwise_items_report"+currentDateandTimee1+"_"+timee1+".csv";
                        String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/IVEPOS_reports/IVEPOS_Billwise_items_report/IvePOS_Billwise_items_report"+currentDateandTimee1+"_"+timee1+".csv";

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
            Message message = createMessageWithEmail(email);
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

        private Message createMessageWithEmail(MimeMessage email)
                throws MessagingException, IOException {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            email.writeTo(bytes);
            String encodedEmail = Base64.encodeBase64URLSafeString(bytes.toByteArray());
            Message message = new Message();
            message.setRaw(encodedEmail);
            return message;
        }

        @Override
        protected void onPreExecute() {
//            mProgress.show();
        }

        @Override
        protected void onPostExecute(String output) {
            Log.d("post execute", "error");
//            mProgress.hide();
            if (output == null || output.length() == 0) {
//                Toast.makeText(getActivity(), "not success", Toast.LENGTH_SHORT).show();
//                showMessage(view, "No results returned.");
            } else {
//                Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
//                showMessage(view, output);
            }
        }

        @Override
        protected void onCancelled() {
//            mProgress.hide();

        }
    }

}
