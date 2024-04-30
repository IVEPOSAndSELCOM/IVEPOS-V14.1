package com.intuition.ivepos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.google.api.services.gmail.model.Message;
import com.intuition.ivepos.csv.RequestSingleton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
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

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

public class EmailSetup extends AppCompatActivity {

    GoogleAccountCredential mCredential;
    ProgressDialog mProgress;


    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Gmail API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { GmailScopes.GMAIL_SEND };


    public SQLiteDatabase db = null;

    String WebserviceUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_service_providers);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(EmailSetup.this);
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

        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

//        CustomList adapter = new
//                CustomList(EmailSetup.this, web, imageId);
//        list = (ListView) findViewById(R.id.list);
//        list.setAdapter(adapter);
//        list.setBackgroundColor(Color.WHITE);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                if (position == 0) {
//                    Intent myIntent = new Intent(EmailSetup.this, EmailSetup_Google.class);
//                    startActivity(myIntent);
//
//                }
//
//                if (position == 1) {
//                    Intent myIntent = new Intent(EmailSetup.this, EmailSetup_Microsoft.class);
//                    startActivity(myIntent);
//
//                }
////
//                if (position == 2) {
//                    Intent myIntent = new Intent(EmailSetup.this, EmailSetup_Yahoo.class);
//                    startActivity(myIntent);
//
//                }
//
//
//            }
//
//
//        });


        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.back_activity);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

//                new MakeRequestTask(mCredential).execute();
            }
        });


        LinearLayout google_layout = (LinearLayout) findViewById(R.id.google_layout);
        google_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursorr = db.rawQuery("SELECT * FROM Email_setup", null);
                if (cursorr.moveToFirst()) {
                    String unn = cursorr.getString(1);
                    String client = cursorr.getString(3);

                    final TextView tvv = new TextView(EmailSetup.this);
                    tvv.setText(unn);

                    if (tvv.getText().toString().equals("")){
                        Intent myIntent = new Intent(EmailSetup.this, EmailSetup_Google.class);
                        startActivity(myIntent);
                    }else {
                        Toast.makeText(EmailSetup.this, "To reconfigure new email remove old email id", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Intent myIntent = new Intent(EmailSetup.this, EmailSetup_Google.class);
                    startActivity(myIntent);
                }
            }
        });

        LinearLayout office365_exchange_layout = (LinearLayout) findViewById(R.id.office365_exchange_layout);
        office365_exchange_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursorr = db.rawQuery("SELECT * FROM Email_setup", null);
                if (cursorr.moveToFirst()) {
                    String unn = cursorr.getString(1);
                    String client = cursorr.getString(3);

                    final TextView tvv = new TextView(EmailSetup.this);
                    tvv.setText(unn);

                    if (tvv.getText().toString().equals("")){
                        Intent myIntent = new Intent(EmailSetup.this, EmailSetup_Office365_Exchange.class);
                        startActivity(myIntent);
                    }else {
                        Toast.makeText(EmailSetup.this, "To reconfigure new email remove old email id", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Intent myIntent = new Intent(EmailSetup.this, EmailSetup_Office365_Exchange.class);
                    startActivity(myIntent);
                }
            }
        });

        LinearLayout hotmail_outlook_layout = (LinearLayout) findViewById(R.id.hotmail_outlook_layout);
        hotmail_outlook_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursorr = db.rawQuery("SELECT * FROM Email_setup", null);
                if (cursorr.moveToFirst()) {
                    String unn = cursorr.getString(1);
                    String client = cursorr.getString(3);

                    final TextView tvv = new TextView(EmailSetup.this);
                    tvv.setText(unn);

                    if (tvv.getText().toString().equals("")){
                        Intent myIntent = new Intent(EmailSetup.this, EmailSetup_Hotmail_Outlook.class);
                        startActivity(myIntent);
                    }else {
                        Toast.makeText(EmailSetup.this, "To reconfigure new email remove old email id", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Intent myIntent = new Intent(EmailSetup.this, EmailSetup_Hotmail_Outlook.class);
                    startActivity(myIntent);
                }
            }
        });

        LinearLayout yahoo_layout = (LinearLayout) findViewById(R.id.yahoo_layout);
        yahoo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursorr = db.rawQuery("SELECT * FROM Email_setup", null);
                if (cursorr.moveToFirst()) {
                    String unn = cursorr.getString(1);
                    String client = cursorr.getString(3);

                    final TextView tvv = new TextView(EmailSetup.this);
                    tvv.setText(unn);

                    if (tvv.getText().toString().equals("")){
                        Intent myIntent = new Intent(EmailSetup.this, EmailSetup_Yahoo.class);
                        startActivity(myIntent);
                    }else {
                        Toast.makeText(EmailSetup.this, "To reconfigure new email remove old email id", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Intent myIntent = new Intent(EmailSetup.this, EmailSetup_Yahoo.class);
                    startActivity(myIntent);
                }
            }
        });

        final LinearLayout sync_mail_layout = (LinearLayout) findViewById(R.id.sync_mail_layout);

        Cursor cursorr = db.rawQuery("SELECT * FROM Email_setup", null);
        if (cursorr.moveToFirst()) {
            String unn = cursorr.getString(1);
            String client = cursorr.getString(3);

            final TextView tvv = new TextView(EmailSetup.this);
            tvv.setText(unn);

            if (tvv.getText().toString().equals("")){
                sync_mail_layout.setVisibility(View.GONE);
            }else {
                TextView configured_mail = (TextView) findViewById(R.id.configured_mail);
                configured_mail.setText(tvv.getText().toString());

                ImageView configured_mail_client = (ImageView) findViewById(R.id.configured_mail_client);
                if (client.toString().equals("Gmail")){
                    configured_mail_client.setImageResource(R.drawable.ic_google);
                }else {
                    if (client.toString().equals("Yahoo")){
                        configured_mail_client.setImageResource(R.drawable.ic_yahoo);
                    }else {
                        configured_mail_client.setImageResource(R.drawable.ic_microsoft);
                    }
                }

                ImageView btndelete = (ImageView) findViewById(R.id.btndelete);
                btndelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog_delete_confirmation = new Dialog(EmailSetup.this, R.style.timepicker_date_dialog);
                        dialog_delete_confirmation.setContentView(R.layout.dialog_email_setup_delete_confirmation);
                        dialog_delete_confirmation.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialog_delete_confirmation.show();

                        ImageView closetext = (ImageView) dialog_delete_confirmation.findViewById(R.id.closetext);
                        closetext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog_delete_confirmation.dismiss();
                            }
                        });

                        Button cancel = (Button) dialog_delete_confirmation.findViewById(R.id.cancel);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog_delete_confirmation.dismiss();
                            }
                        });

                        Button ok = (Button) dialog_delete_confirmation.findViewById(R.id.ok);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String where = "username = '"+tvv.getText().toString()+"'";
                                db.delete("Email_setup", where, new String[]{});

                                webservicequery("delete from Email_setup");

                                dialog_delete_confirmation.dismiss();

                                sync_mail_layout.setVisibility(View.GONE);

                            }
                        });

                    }
                });
            }
        }else {
            sync_mail_layout.setVisibility(View.GONE);
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
            TextView edtToAddress = new TextView(EmailSetup.this);
            edtToAddress.setText("krrohith934@gmail.com");

            TextView edtSubject = new TextView(EmailSetup.this);
            edtSubject.setText("krrohith934@gmail.com");

            TextView edtMessage = new TextView(EmailSetup.this);
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
                Toast.makeText(EmailSetup.this, "not success", Toast.LENGTH_SHORT).show();
//                showMessage(view, "No results returned.");
            } else {
                Toast.makeText(EmailSetup.this, "success", Toast.LENGTH_SHORT).show();
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
                EmailSetup.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(EmailSetup.this, MultiFragPreferenceActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//    }



    public void webservicequery(final String webserviceQuery){


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(EmailSetup.this);
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
        RequestQueue queue;
        StringRequest sr1;
        // queue = Volley.newRequestQueue(getActivity());

        queue= RequestSingleton.getInstance(EmailSetup.this).getInstance();

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
