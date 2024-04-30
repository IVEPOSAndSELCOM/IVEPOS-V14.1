package com.intuition.ivepos;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Rohithkumar on 5/29/2015.
 */
public class ForgotPasswordActivity extends AppCompatActivity {

    Dialog dialog;

    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;
    }

    SimpleDateFormat sdff2, sdff1;
    String currentDateandTimee1;
    String timee1;

    public static final String PACKAGE_NAME = "com.intuition.ivepos";
    public static final String DATABASE_NAME = "mydb_Appdata";
    public static final String DATABASE_NAME1 = "mydb_Salesdata";
    public static final String DATABASE_TABLE = "entryTable";

    /** Contains: /data/data/com.example.app/databases/example.db **/
    private static final File DATA_DIRECTORY_DATABASE =
            new File(Environment.getDataDirectory() +
                    "/data/" + PACKAGE_NAME +
                    "/databases/" + DATABASE_NAME );

    private static final File DATA_DIRECTORY_DATABASE1 =
            new File(Environment.getDataDirectory() +
                    "/data/" + PACKAGE_NAME +
                    "/databases/" + DATABASE_NAME1 );

    String account_selection;
    String WebserviceUrl;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.threelogins1);


        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(ForgotPasswordActivity.this);
        account_selection= sharedpreferences_select.getString("account_selection", null);

        if (account_selection.toString().equals("Dine")) {
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }else {
            if (account_selection.toString().equals("Qsr")) {
                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
            }else {
                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
            }
        }

        ImageView arrow = (ImageView) findViewById(R.id.leftarrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class );
//                startActivity(intent);

                if (account_selection.toString().equals("Dine") || account_selection.toString().equals(getString(R.string.app_name))) {
                    Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("from","home");
                    startActivity(intent);
                }else {
                    if (account_selection.toString().equals("Qsr")) {
                        Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("from","home");
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity_Retail.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("from","home");
                        startActivity(intent);
                    }
                }

//                Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("from","home");
//                startActivity(intent);
            }
        });

//        ImageView question = (ImageView)findViewById(R.id.question);
//        question.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog = new Dialog(ForgotPasswordActivity.this , R.style.cust_dialog);
//                dialog.setContentView(R.layout.questiondetails);
//                dialog.setTitle(Html.fromHtml("<font color='#ffffff'>HELP</font>"));
//                dialog.show();
//
//                Button ok = (Button)dialog.findViewById(R.id.close);
//                ok.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//            }
//        });

        Button gotomain = (Button)findViewById(R.id.gotomainpage);
        gotomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class );
//                startActivity(intent);

                if (account_selection.toString().equals("Dine") || account_selection.toString().equals(getString(R.string.app_name))) {
                    Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("from","home");
                    startActivity(intent);
                }else {
                    if (account_selection.toString().equals("Qsr")) {
                        Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("from","home");
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity_Retail.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("from","home");
                        startActivity(intent);
                    }
                }

//                Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("from","home");
//                startActivity(intent);
            }
        });

        RelativeLayout universalpassword = (RelativeLayout) findViewById(R.id.universallogin);
        universalpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, UniversalLoginActivity.class );
                startActivity(intent);
            }
        });

        RelativeLayout masterpassword = (RelativeLayout)findViewById(R.id.button);
        masterpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, MasterLoginActivity.class );
                startActivity(intent);
            }
        });

        RelativeLayout localpassword = (RelativeLayout)findViewById(R.id.locallogin);
        localpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LocalLoginActivity.class );
                startActivity(intent);
            }
        });

        RelativeLayout userslogin = (RelativeLayout)findViewById(R.id.userslogin);
        userslogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(ForgotPasswordActivity.this , R.style.cust_dialog);
                dialog.setContentView(R.layout.userrecovery_dialog);
                dialog.setTitle("User");

                Button close1 = (Button)dialog.findViewById(R.id.close);
                close1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();


            }
        });


        RelativeLayout backup = (RelativeLayout) findViewById(R.id.backup);
        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sdff2 = new SimpleDateFormat("ddMMMyy");
                currentDateandTimee1 = sdff2.format(new Date());

                Date dt = new Date();
                sdff1 = new SimpleDateFormat("hhmmssaa");
                timee1 = sdff1.format(dt);

                if (ContextCompat.checkSelfPermission(ForgotPasswordActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ForgotPasswordActivity.this,
                            permissions(),
                            1);
                   /* // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ForgotPasswordActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

//                        Toast.makeText(ForgotPasswordActivity.this, "111111111", Toast.LENGTH_SHORT).show();

                        ActivityCompat.requestPermissions(ForgotPasswordActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1);
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
//                        Toast.makeText(ForgotPasswordActivity.this, "no permission", Toast.LENGTH_SHORT).show();

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(ForgotPasswordActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }*/
                }else {
//                    Toast.makeText(ForgotPasswordActivity.this, "hiiii", Toast.LENGTH_SHORT).show();

                    if (!SdIsPresent()) ;

                    DownloadMusicfromInternet3 downloadMusicfromInternet = new DownloadMusicfromInternet3();
                    downloadMusicfromInternet.execute();
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //Toast.makeText(ForgotPasswordActivity.this, "permission granted", Toast.LENGTH_SHORT).show();
                    System.out.println("permission granted");
                    if (!SdIsPresent()) ;

                    DownloadMusicfromInternet3 downloadMusicfromInternet = new DownloadMusicfromInternet3();
                    downloadMusicfromInternet.execute();


                } else {

                    Toast.makeText(ForgotPasswordActivity.this, "permission denied", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static boolean SdIsPresent() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (account_selection.toString().equals("Dine") || account_selection.toString().equals(getString(R.string.app_name))) {
            Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("from","home");
            startActivity(intent);
        }else {
            if (account_selection.toString().equals("Qsr")) {
                Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("from","home");
                startActivity(intent);
            }else {
                Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity_Retail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("from","home");
                startActivity(intent);
            }
        }

//        Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("from","home");
//        startActivity(intent);
    }



    class DownloadMusicfromInternet3 extends AsyncTask<String, Void, Integer> {
        private ProgressDialog dialog = new ProgressDialog(ForgotPasswordActivity.this, R.style.timepicker_date_dialog);

        @Override
        protected Integer doInBackground(String... params) {

            File dbFile1 = DATA_DIRECTORY_DATABASE;
            String filename1 = "mydb_Appdata";

//            File exportDir1 = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
            File exportDir1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
            if (!exportDir1.exists()) {
                exportDir1.mkdirs();
            }


            //File exportDir = DATABASE_DIRECTORY;
            File file1 = new File(exportDir1, filename1);

            if (!exportDir1.exists()) {
                exportDir1.mkdirs();
            }

            try {
//                    file.createNewFile();
                copyFile(dbFile1, file1);
                Log.e("1", "111");
//                circle1.setVisibility(View.INVISIBLE);
//                tick1.setVisibility(View.VISIBLE);
//                error1.setVisibility(View.INVISIBLE);

                //return true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("2", "22");
//                circle1.setVisibility(View.INVISIBLE);
//                tick1.setVisibility(View.INVISIBLE);
//                error1.setVisibility(View.VISIBLE);
                //return false;
            }

            File dbFile = DATA_DIRECTORY_DATABASE1;
            String filename = "mydb_Salesdata";

//            File exportDir = new File(Environment.getExternalStorageDirectory(), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
            File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/IVEPOS_backup/" + currentDateandTimee1 + "_" + timee1);
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }


            //File exportDir = DATABASE_DIRECTORY;
            File file = new File(exportDir, filename);

            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            try {
//                    file.createNewFile();
                copyFile(dbFile, file);
                Log.e("1", "1111");
//                circle1.setVisibility(View.INVISIBLE);
//                tick1.setVisibility(View.VISIBLE);
//                error1.setVisibility(View.INVISIBLE);

                //return true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("2", "22");
//                circle1.setVisibility(View.INVISIBLE);
//                tick1.setVisibility(View.INVISIBLE);
//                error1.setVisibility(View.VISIBLE);
                //return false;
            }
            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);

            dialog.setMessage(getString(R.string.setmessage3));
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


        @Override
        protected void onPostExecute(Integer file_url) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();
            dialog.dismiss();

            Toast.makeText(ForgotPasswordActivity.this, "Back up successfull", Toast.LENGTH_SHORT).show();

            List<String> your_array_list = new ArrayList<String>();
//            String path = Environment.getExternalStorageDirectory().toString()+"/IVEPOS_backup/";
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/IVEPOS_backup/";

            File f = new File(path);
            File[] files = f.listFiles();
            for (File inFile : files) {
                if (inFile.isDirectory()) {
                    // in here, you can add directory names into an ArrayList and populate your ListView.
                    your_array_list.add(inFile.getName());
                    //Toast.makeText(getActivity(), "file anmem is "+inFile.getName(), Toast.LENGTH_SHORT).show();
                }
            }


        }

    }


    private static void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }


}
