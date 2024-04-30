package com.intuition.ivepos;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.api.services.gmail.model.Message;
import com.intuition.ivepos.csv.RequestSingleton;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

/**
 * Created by Rohithkumar on 5/26/2017.
 */

public class EmailSetup_Google extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks{


    GoogleAccountCredential mCredential;
    ProgressDialog mProgress;


    static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Gmail API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { GmailScopes.GMAIL_SEND };


    public SQLiteDatabase db = null;

    String WebserviceUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.button);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(EmailSetup_Google.this);
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

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.setmessage16));


        // Initialize credentials and service object.

        Button b = (Button) findViewById(R.id.send_mail);
//        b.setText("Send Mail");

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new MakeRequestTask(mCredential).execute();

//                String toEmails = "rohith934@gmail.com";
//                List toEmailList = Arrays.asList(toEmails
//                        .split("\\s*,\\s*"));
//
//                new SendMailTask_new(MainActivity.this).execute("krrohith934@gmail.com",
//                        "9581497872", toEmailList, "Hi", "msg", "03May17", "012752pm");
//
////                GMailSender sender = new GMailSender("krrohith934@gmail.com", "9581497872");
////                try {
////                    sender.sendMail("This is Subject",
////                            "This is Body",
////                            "rohith934@gmail.com",
////                            "krrohith934@gmail.com");
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }

            }
        });

        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

//        getResultsFromApi();

        chooseAccount();


    }


    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */

    private void getResultsFromApi() {
//        if (! isGooglePlayServicesAvailable()) {
//            Toast.makeText(EmailSetup_Google.this, "1", Toast.LENGTH_SHORT).show();
//            acquireGooglePlayServices();
//        } else {
//            Cursor cursorr = db.rawQuery("SELECT * FROM Email_setup", null);
//            if (cursorr.moveToFirst()) {
//                String unn = cursorr.getString(1);
//                Toast.makeText(EmailSetup_Google.this, "a4 " + unn, Toast.LENGTH_SHORT).show();
//
//                TextView tvv = new TextView(EmailSetup_Google.this);
//                tvv.setText(unn);
//
//                if (tvv.getText().toString().equals("")) {
//                    chooseAccount();
//                }else {
//                    if (! isDeviceOnline()) {
//                        Toast.makeText(EmailSetup_Google.this, "3", Toast.LENGTH_SHORT).show();
////                      mOutputText.setText("No network connection available.");
//                    } else {
//                        Toast.makeText(EmailSetup_Google.this, "4", Toast.LENGTH_SHORT).show();
//                        new MakeRequestTask1(mCredential).execute();
//                    }
//                }
//            }else {
//                chooseAccount();
//            }
////            if (mCredential.getSelectedAccountName() == null) {
////                Toast.makeText(EmailSetup_Google.this, "2", Toast.LENGTH_SHORT).show();
////
////            }
//        }

        Cursor cursorr = db.rawQuery("SELECT * FROM Email_setup", null);
        if (cursorr.moveToFirst()) {
            String unn = cursorr.getString(1);
//            Toast.makeText(EmailSetup_Google.this, "a4 " + unn, Toast.LENGTH_SHORT).show();

            TextView tvv = new TextView(EmailSetup_Google.this);
            tvv.setText(unn);

            if (tvv.getText().toString().equals("")) {

            }else {
                mCredential.setSelectedAccountName(tvv.getText().toString());
            }
        }


        if (! isGooglePlayServicesAvailable()) {
//            Toast.makeText(EmailSetup_Google.this, "1", Toast.LENGTH_SHORT).show();
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
//            Toast.makeText(EmailSetup_Google.this, "2", Toast.LENGTH_SHORT).show();
            chooseAccount();
        } else if (! isDeviceOnline()) {
//            Toast.makeText(EmailSetup_Google.this, "3", Toast.LENGTH_SHORT).show();
//            mOutputText.setText("No network connection available.");
        } else {
//            Toast.makeText(EmailSetup_Google.this, "4", Toast.LENGTH_SHORT).show();
            new MakeRequestTask1(mCredential).execute();

            Intent intent = new Intent(EmailSetup_Google.this, EmailSetup.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
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
//        Toast.makeText(EmailSetup_Google.this, "s1", Toast.LENGTH_SHORT).show();
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
//            Toast.makeText(EmailSetup_Google.this, "s2", Toast.LENGTH_SHORT).show();
//            if (accountName != null) {
//                mCredential.setSelectedAccountName(accountName);
//                Toast.makeText(EmailSetup_Google.this, "s3", Toast.LENGTH_SHORT).show();
//                getResultsFromApi();
//            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
//                Toast.makeText(EmailSetup_Google.this, "s4", Toast.LENGTH_SHORT).show();
//            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
//            Toast.makeText(EmailSetup_Google.this, "s5", Toast.LENGTH_SHORT).show();
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
//                    mOutputText.setText(
//                            "This app requires Google Play Services. Please install " +
//                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
//                Toast.makeText(EmailSetup_Google.this, "a1", Toast.LENGTH_SHORT).show();
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
//                    Toast.makeText(EmailSetup_Google.this, "a1 "+accountName, Toast.LENGTH_SHORT).show();
                    if (accountName != null) {
//                        Toast.makeText(EmailSetup_Google.this, "a2 "+accountName, Toast.LENGTH_SHORT).show();
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);

//                        Toast.makeText(EmailSetup_Google.this, "a3 "+accountName, Toast.LENGTH_SHORT).show();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("username", accountName);
                        contentValues.put("password", "");
                        contentValues.put("client", "Gmail");
                        db.insert("Email_setup", null, contentValues);

                        webservicequery("INSERT INTO Email_setup (_id, username, password, client) VALUES ('1', '"+accountName+"', '', 'Gmail')");

                        Cursor cursor = db.rawQuery("SELECT * FROM Email_setup", null);
                        if (cursor.moveToFirst()){
                            String un = cursor.getString(1);
//                            Toast.makeText(EmailSetup_Google.this, "a4 "+un, Toast.LENGTH_SHORT).show();
                        }

                        getResultsFromApi();
                    }
                }else {
//                    Toast.makeText(EmailSetup_Google.this, "a11", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EmailSetup_Google.this, EmailSetup.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
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
                apiAvailability.isGooglePlayServicesAvailable(this);
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
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                EmailSetup_Google.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
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
            TextView edtToAddress = new TextView(EmailSetup_Google.this);
            edtToAddress.setText("krrohith934@gmail.com");

            TextView edtSubject = new TextView(EmailSetup_Google.this);
            edtSubject.setText("krrohith934@gmail.com");

            TextView edtMessage = new TextView(EmailSetup_Google.this);
            edtMessage.setText("krrohith934@gmail.com");

            String user = "me";
            String to = Utils.getString(edtToAddress);
            String from = mCredential.getSelectedAccountName();
            Log.v("sender email", from);
            String subject = Utils.getString(edtSubject);
            String body = Utils.getString(edtMessage);
            MimeMessage mimeMessage;
            String response = "";
            try {

//                String filename = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_reports/IVEPOS_sales_report/IvePOS_sales_report"+"12May17"+"_"+"013048PM"+".csv";
////                String path = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_backup/";
//
//
//                File f = new File(filename);
//
//                mimeMessage = createEmailWithAttachment(to, from, subject, body, f);



                mimeMessage = createEmail(to, from, subject, body);
                response = sendMessage(mService, user, mimeMessage);
            } catch (MessagingException e) {
                e.printStackTrace();
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
            mProgress.show();
        }

        @Override
        protected void onPostExecute(String output) {
            Log.d("post execute", "error");
            mProgress.hide();
            if (output == null || output.length() == 0) {
                Toast.makeText(EmailSetup_Google.this, "not success", Toast.LENGTH_SHORT).show();
//                showMessage(view, "No results returned.");
            } else {
                Toast.makeText(EmailSetup_Google.this, "success", Toast.LENGTH_SHORT).show();
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

    public void webservicequery(final String webserviceQuery){


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(EmailSetup_Google.this);
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
        RequestQueue queue;
        StringRequest sr1;
        // queue = Volley.newRequestQueue(getActivity());

        queue= RequestSingleton.getInstance(EmailSetup_Google.this).getInstance();

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
