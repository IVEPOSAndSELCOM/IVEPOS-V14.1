package com.intuition.ivepos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.android.gms.auth.GoogleAuthException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * Created by Rohithkumar on 4/4/2018.
 */

public class Otp_checking extends AppCompatActivity {

    SQLiteDatabase db;
    DatabaseHelper1 databaseHelper;
    Charity registeredData;

    String emailid, password;

    EditText editTextRegUserCompany, spinner, spinner1, editTextRegotp;
    TextView editTextRegEmail;
    private static final String SUFFIX = "/";

    RelativeLayout progressBar1;

    AmazonS3 s3client;
    TransferUtility transferUtility;
    String token;

//    String path1111 = Environment.getExternalStorageDirectory().toString() + "/IVEPOS/Credentials/masteradmin_cred.csv";
//    String path222 = Environment.getExternalStorageDirectory().toString() + "/IVEPOS/Credentials/storeadmin_cred.csv";
    String path1111 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/IVEPOS/Credentials/masteradmin_cred.csv";
    String path222 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/IVEPOS/Credentials/storeadmin_cred.csv";

    private AlertDialog userDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_checking);

        db = openOrCreateDatabase("amazon.db", Context.MODE_PRIVATE, null);

        databaseHelper = new DatabaseHelper1(this);
        registeredData = new Charity();

        Bundle extras = getIntent().getExtras();
        emailid = extras.getString("mytext");
        password = extras.getString("pass");

        try {
            credentialsProvider();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GoogleAuthException e) {
            e.printStackTrace();
        }

        setTransferUtility();

        ImageView leftarrow = (ImageView) findViewById(R.id.leftarrow);
        leftarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editTextRegEmail = (TextView) findViewById(R.id.editTextRegEmail);
        editTextRegEmail.setText(emailid);

        editTextRegUserCompany = (EditText) findViewById(R.id.editTextRegUserCompany);
        spinner = (EditText) findViewById(R.id.spinner);
        spinner1 = (EditText) findViewById(R.id.spinner1);

        editTextRegotp = (EditText) findViewById(R.id.editTextRegotp);

        editTextRegUserCompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editTextRegUserCompany.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        spinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                spinner.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        spinner1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                spinner1.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        TextView signUp = (TextView) findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextRegUserCompany.getText().toString().equals("") || spinner.getText().toString().equals("")
                        || spinner1.getText().toString().equals("") || editTextRegotp.getText().toString().equals("")) {
                    if (editTextRegUserCompany.getText().toString().equals("")) {
                        editTextRegUserCompany.setError("Enter Company name");
                    }
                    if (spinner.getText().toString().equals("")) {
                        spinner.setError("Enter Store name");
                    }
                    if (spinner1.getText().toString().equals("")) {
                        spinner1.setError("Enter Device name");
                    }
                    if (editTextRegotp.getText().toString().equals("")) {
                        editTextRegotp.setError("Enter OTP");
                    }
                }else {
                    if (isDeviceOnline() == true) {

                        db.delete("register", null, null);


                        registeredData.setCompanyName(editTextRegUserCompany.getText().toString());
                        registeredData.setEmail(emailid);
                        registeredData.setPassword(password);
                        registeredData.setConfirmpassword(password);
                        registeredData.setStorename(spinner.getText().toString());
                        registeredData.setDevicename(spinner1.getText().toString());

                        // registeredData.setMobile_number(strNumber);
                        databaseHelper.insert(registeredData);

                        new LoginTask().execute();

                    } else {
                        showAlert();
                    }
                }
            }
        });

    }

    private class LoginTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progressBar1 = (RelativeLayout) findViewById(R.id.progressbar1);

            progressBar1.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... urls) {
            String confirmCode = editTextRegotp.getText().toString();
            if (emailid == null || emailid.length() < 1) {
                TextView label = (TextView) findViewById(R.id.textViewConfirmUserIdMessage);
                label.setText(emailid + " cannot be empty");
//            username.setBackground(getDrawable(R.drawable.text_border_error));
            }

            if (confirmCode == null || confirmCode.length() < 1) {
                TextView label = (TextView) findViewById(R.id.textViewConfirmCodeMessage);
                label.setText(editTextRegotp.getHint() + " cannot be empty");
//            confCode.setBackground(getDrawable(R.drawable.text_border_error));
            }


//new version code....
            return null;
        }

        protected void onProgressUpdate(String... progress) {

        }

        protected void onPostExecute(String result) {

            ExportDatabaseCSVTask task = new ExportDatabaseCSVTask();
            task.execute();

            progressBar1.setVisibility(View.GONE);

            String confirmCode = editTextRegotp.getText().toString();

            AppHelper.getPool().getUser(editTextRegEmail.getText().toString()).confirmSignUpInBackground(confirmCode, true, confHandler);
        }
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(Otp_checking.this).create();

        alertDialog.setTitle(getString(R.string.title10));
        alertDialog.setMessage(getString(R.string.setmessage19));
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
          /*  alertDialog.setButton("OK", DialogInterface.BUTTON_POSITIVE,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();

                }
            });*/
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // finish();
            }
        });

        alertDialog.show();
    }

    public void credentialsProvider() throws IOException, GoogleAuthException {

//        // Initialize the Amazon Cognito credentials provider
//        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
//                getApplicationContext(),
//                "ap-south-1:c2b96ff9-4cfc-49cc-867c-c630022abba2", // Identity pool ID
//                Regions.AP_SOUTH_1 // Region
//        );
        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider1 = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-south-1:210cc88c-9644-44cc-bbe3-6d7219ae6c20", // Identity pool ID
                Regions.AP_SOUTH_1 // Region
        );

        setAmazonS3Client(credentialsProvider1);
    }

    public void setAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider) throws IOException, GoogleAuthException {

        // Create an S3 client
        s3client = new AmazonS3Client(credentialsProvider);


//        Map<String, String> logins = new HashMap<String, String>();
//        logins.put("login.provider.com", token);
//        credentialsProvider.setLogins(logins);

        Map<String, String> loginss = new HashMap<String, String>();
        loginss.put("login.provider.com", token);
        credentialsProvider.setLogins(loginss);


//
        Map<String, String> logins = new HashMap<String, String>();
        logins.put("accounts.google.com", token);
        credentialsProvider.setLogins(logins);

        // Set the region of your S3 bucket
        s3client.setRegion(Region.getRegion(Regions.AP_SOUTH_1));

    }

    public void setTransferUtility() {

        transferUtility = new TransferUtility(s3client, getApplicationContext());
    }

    public void transferObserverListener(TransferObserver transferObserver) {

        transferObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e("statechange", state + "");
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
//                int percentage = (int) (bytesCurrent/bytesTotal * 100);
//                Log.e("percentage",percentage +"");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error", "error");
            }

        });
    }

    public static void createFolder(String bucketName, String folderName, AmazonS3 client) {
        // create meta-data for your folder and set content-length to 0
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);

        // create empty content
        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

        // create a PutObjectRequest passing the folder name suffixed by /
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
                folderName + SUFFIX, emptyContent, metadata);

        // send request to S3 to create folder
        client.putObject(putObjectRequest);
    }

    GenericHandler confHandler = new GenericHandler() {
        @Override
        public void onSuccess() {
            showDialogMessage("Success!", emailid + " has been confirmed!", true);
        }

        @Override
        public void onFailure(Exception exception) {
            TextView label = (TextView) findViewById(R.id.textViewConfirmUserIdMessage);
            label.setText(getString(R.string.confi4));
//            username.setBackground(getDrawable(R.drawable.text_border_error));

            label = (TextView) findViewById(R.id.textViewConfirmCodeMessage);
            label.setText(getString(R.string.confi4));
//            confCode.setBackground(getDrawable(R.drawable.text_border_error));

            showDialogMessage(getString(R.string.sdm14), AppHelper.formatException(exception), false);
        }
    };


    private void showDialogMessage(String title, String body, final boolean exitActivity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                    if (exitActivity) {
                        Intent i = new Intent(Otp_checking.this, MainActivity_subscription.class);
                        i.putExtra("emailid", emailid);
                        i.putExtra("storename", spinner.getText().toString());
                        i.putExtra("devicename", spinner1.getText().toString());
                        i.putExtra("mytext", editTextRegUserCompany.getText().toString());
                        i.putExtra("from", "register");
                        startActivity(i);
                    }
                } catch (Exception e) {
                    exit();
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }

    private void exit() {
        Intent intent = new Intent();
        if (emailid == null)
            emailid = "";
        intent.putExtra("name", "");
        intent.putExtra("mytext", "");
        intent.putExtra("email", emailid);
        intent.putExtra("text", "");
        setResult(RESULT_OK, intent);
        finish();
    }

    private class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(Otp_checking.this);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage(getString(R.string.setmessage13));
            this.dialog.show();

        }

        protected Boolean doInBackground(final String... args) {

//            File exportDir = new File(Environment.getExternalStorageDirectory(), "IVEPOS/Credentials");
            File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "IVEPOS/Credentials");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            File file = new File(exportDir, "masteradmin_cred.csv");
            try {

                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

                // this is the Column of the table and same for Header of CSV file
                String arrStr1[] = {"COMPANY_NAME", "EMAIL", "PASSWORD"};
                csvWrite.writeNext(arrStr1);

                SQLiteDatabase db = openOrCreateDatabase("amazon.db",
                        Context.MODE_PRIVATE, null);
                Cursor curCSV = db.rawQuery("SELECT * FROM register", null);

                if (curCSV.moveToFirst()) {
                    do {
                        String arrStr[] = {curCSV.getString(1), curCSV.getString(4),
                                curCSV.getString(5)};
                        csvWrite.writeNext(arrStr);
                    } while (curCSV.moveToNext());
                }


                csvWrite.close();


                return true;

            } catch (IOException e) {
                Log.e("MainActivity", e.getMessage(), e);
                return false;

            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (success) {
//                Toast.makeText(MainActivity_Register.this, getString(R.string.export_successful), Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(MainActivity_Register.this, getString(R.string.export_failed), Toast.LENGTH_SHORT).show();
            }

            ExportDatabaseCSVTask1 task1 = new ExportDatabaseCSVTask1();
            task1.execute();
        }
    }


    private class ExportDatabaseCSVTask1 extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(Otp_checking.this);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage(getString(R.string.setmessage13));
            this.dialog.show();

        }

        protected Boolean doInBackground(final String... args) {


//            File dbFile = getDatabasePath("mydb_Salesdata");
            //Log.v(TAG, "Db path is: "+dbFile);  //get the path of db_appdata

//            File exportDir = new File(Environment.getExternalStorageDirectory(), "/IVEPOS/Credentials");
            File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS/Credentials");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            File file = new File(exportDir, "storeadmin_cred.csv");
            try {

                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

                // this is the Column of the table and same for Header of CSV file
                String arrStr1[] = {"COMPANY_NAME", "STORE", "DEVICE", "EMAIL", "PASSWORD"};
                csvWrite.writeNext(arrStr1);

                SQLiteDatabase db = openOrCreateDatabase("amazon.db",
                        Context.MODE_PRIVATE, null);
                Cursor curCSV = db.rawQuery("SELECT * FROM register", null);
                //csvWrite.writeNext(curCSV.getColumnNames());

                if (curCSV.moveToFirst()) {
                    do {
                        String arrStr[] = {curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4),
                                curCSV.getString(5)};
//                 curCSV.getString(2),curCSV.getString(3),curCSV.getString(4),
                        csvWrite.writeNext(arrStr);
                    } while (curCSV.moveToNext());

                }

                csvWrite.close();


                return true;

            } catch (IOException e) {
                Log.e("MainActivity", e.getMessage(), e);
                return false;

            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (success) {
//                Toast.makeText(MainActivity_Register.this, "Export successful11!", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(MainActivity_Register.this, "Export failed11!", Toast.LENGTH_SHORT).show();
            }

            String bucketName1 = "intuitionclients";
            System.out.println("textcompanyname...." + editTextRegUserCompany.getText().toString());
            System.out.println("email...." + emailid);
            System.out.println("storeitem...." + spinner.getText().toString());
            System.out.println("deviceitem...." + spinner1.getText().toString());

            String company = editTextRegUserCompany.getText().toString() + "-company";
            createFolder(bucketName1, company, s3client);

            String store = company + "/" + spinner.getText().toString() + "-store--" + emailid;
            createFolder(bucketName1, store, s3client);

            String device = store + "/" + "dev-" + spinner1.getText().toString();
            createFolder(bucketName1, device, s3client);


            String credentials = company + "/" + "admin_credentials";

            createFolder(bucketName1, credentials, s3client);


            String credentials_store = store + "/" + "store_admin_credentials";

            createFolder(bucketName1, credentials_store, s3client);


            String fileNameeee1 = credentials_store + SUFFIX + "store" + "_" + spinner.getText().toString() + "admin_cred.csv";//cloud crete file name
            s3client.putObject(new PutObjectRequest(bucketName1, fileNameeee1,
                    new File(path1111))
                    .withCannedAcl(CannedAccessControlList.PublicRead));


            String fileNameeee2 = credentials + SUFFIX + "masteradmin_cred.csv";//cloud crete file name
            s3client.putObject(new PutObjectRequest(bucketName1, fileNameeee2,
                    new File(path222))
                    .withCannedAcl(CannedAccessControlList.PublicRead));


            String DATABASE_NAME = "mydb_Appdata";
            String DATABASE_NAME1 = "mydb_Salesdata";

            File DATA_DIRECTORY_DATABASE =
                    new File(Environment.getDataDirectory() +
                            "/data/" + "com.intuition.ivepos" +
                            "/databases/" + DATABASE_NAME);

            File DATA_DIRECTORY_DATABASE1 =
                    new File(Environment.getDataDirectory() +
                            "/data/" + "com.intuition.ivepos" +
                            "/databases/" + DATABASE_NAME1);


            File dbFile1 = DATA_DIRECTORY_DATABASE;
            TransferObserver transferObserver = transferUtility.upload(
                    bucketName1 + "/" + device,     /* The bucket to download from */
                    "mydb_Appdata",    /* The key for the object to download */
                    dbFile1       /* The file to download the object to */
            );
            transferObserverListener(transferObserver);


            File dbFile = DATA_DIRECTORY_DATABASE1;
            TransferObserver transferObserver1 = transferUtility.upload(
                    bucketName1 + "/" + device,     /* The bucket to download from */
                    "mydb_Salesdata",    /* The key for the object to download */
                    dbFile       /* The file to download the object to */
            );
            transferObserverListener(transferObserver1);

        }
    }

}
