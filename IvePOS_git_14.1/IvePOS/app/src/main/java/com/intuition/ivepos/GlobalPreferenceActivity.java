package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 1/6/2015.
 */

import static android.app.Activity.RESULT_OK;
import static com.intuition.ivepos.SplashScreenActivity.getApplicationContex;
import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;
import static com.intuition.ivepos.sync.SyncHelper.mAccount;
import static com.intuition.ivepos.syncapp.SyncHelperApp.AUTHORITY;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.amplifyframework.core.Amplify;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.intuition.ivepos.languagelocale.LanguageConstant;
import com.intuition.ivepos.languagelocale.LocaleUtils;
import com.intuition.ivepos.paytm.Card_Wallets_Settings;
import com.intuition.ivepos.paytm.Card_Wallets_Settings1;
import com.intuition.ivepos.sync.StubProvider;
import com.intuition.ivepos.syncapp.StubProviderApp;
import com.intuition.ivepos.syncdb.MyServiceAppData_local;
import com.intuition.ivepos.syncdb.SyncDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Rohithkumar on 1/6/2015.
 */
public class GlobalPreferenceActivity extends Fragment implements EasyPermissions.PermissionCallbacks, MyServiceAppData_local.OnProgressUpdateListener {

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

    Fragment frag;
    FragmentTransaction fragTransaction;

    public SQLiteDatabase db = null;
    public SQLiteDatabase db_inapp = null;

    EditText text, text_bill_id_tag, textprice, textquan;
    ImageButton insert, imgblob, get_image;
    Spinner spinnerquickedit, stockcontrol, billingmode, billingtype, country_selection, itemsort, printer_type; Spinner auto_connect, round_off, estimate_off, tab_kot_manag; TextView weighing_scale_onoff;
    public Cursor cursor;
    Switch mySwitch, mySwitch_barcode;
    TextView auto_generation_status;
    RelativeLayout auto_generation_layout;
    String NAME1, NAME2, NAME3;
    TextView timeset, timedis;
    private int hour;
    private int minute;
    String ItemID, NAME;
    BeveragesMenuFragment_Dine BeveragesMenuFragment_Dine;
    final static int RQS_1 = 1;

    String NAme1, NAme11, NAme2, NAAme1, country_sel;
    Button save_image, save_bill_id_tag;


    public SQLiteDatabase db1 = null;
    String qwe;
    int qwee;
    String Itemtype, itemname, itemquan, parent, parentid, modass;
    float aaaaa;
    int aid, and;
    String idd, awq, iddd, qws, ids, hii;
    String modinameee, modieeid, modieequan, nummm;

    float a;
    int b, c, d;
    float item_content10;


    int countt1;
    Dialog dialog, dialog1;

    TextView textView, textView1, editText11_time_hide, editText1_time_hide;
    Uri contentUri,resultUri;




    GoogleAccountCredential mCredential; String response;
    ProgressDialog mProgress;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Gmail API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { GmailScopes.GMAIL_SEND };



    TextView sync_time_get ;
    RelativeLayout card_settings, wallet_settings;

    RelativeLayout def_bill_count, def_merchant_id;
    TextView bill_count, merchant_id;
    RelativeLayout loyalty_points;
    TextView loyalty_money_point, loyalty_point_money;
    TextView dialog_amount, dialog_point;
    String t_money, t_point, t_money2, t_point2;
    private String company="",store="",device="";
    RequestQueue requestQueue;

    private static final int SECOND_ACTIVITY_RESULT_CODE = 0;
    ProgressDialog progressDialog;

    JSONObject json2;
    String str_merch_id;
    RequestQueue queue;
    String items="0",taxes="0",hotel="0",discount="0";
     ProgressDialog statusDialog;

    String WebserviceUrl;

    String insert1_cc = "", insert1_rs = "", str_country;
    private TextView language;

    LinearLayout action;
    LinearLayout action1;
    ProgressBar updateBar;
    CardView progressBar_license;
    TextView tv_perc;
    boolean reachable;
    public static boolean appdataBool=false,salesdataBool=false;
    public static int prog=0;
    public static int progSales=0;

    public GlobalPreferenceActivity() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ApplicationData.getInstance().initAppLanguage(requireActivity());
        LocaleUtils.initialize(requireActivity(), LocaleUtils.getSelectedLanguageId(requireActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View rootview = inflater.inflate(R.layout.fragment_multi_globalpref1, null);
        if (getActivity() instanceof AppCompatActivity){
            androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionbar.setSubtitle("General");
        }

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

        card_settings  = (RelativeLayout)rootview.findViewById(R.id.card_settings);
        wallet_settings = (RelativeLayout)rootview.findViewById(R.id.wallet_setting);

        editText11_time_hide = new TextView(getActivity());
        editText1_time_hide = new TextView(getActivity());
        //final ActionBar actionBar = getActivity().getActionBar();

        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        hideKeyboard(getContext());
        donotshowKeyboard(getActivity());

        final Calendar c = Calendar.getInstance();
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);

        //actionBar.setTitle("General settings");

        db1 = getActivity().openOrCreateDatabase("mydb_Salesdata",
                Context.MODE_PRIVATE, null);

        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        db_inapp = getActivity().openOrCreateDatabase("amazoninapp", Context.MODE_PRIVATE, null);

//        LinearLayout btnglobalpref = (LinearLayout)rootview.findViewById(R.id.buttonglobalpref);
//        LinearLayout btnnetworkpref = (LinearLayout)rootview.findViewById(R.id.buttonnetworkpref);
//        LinearLayout btndevicepref = (LinearLayout)rootview.findViewById(R.id.buttondevicepref);
//        LinearLayout btnquickaccess = (LinearLayout)rootview.findViewById(R.id.buttonquickaccess);
//        LinearLayout btninventory = (LinearLayout)rootview.findViewById(R.id.buttoninventory);


//        btnglobalpref.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                //actionBar.setTitle(" Global Preference");
//                frag = new GlobalPreferenceActivity();
//                hideKeyboard(getContext());
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });
//
//        btnnetworkpref.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                //actionBar.setTitle(" Network Preference");
//                frag = new NetworkPreferenceActivity();
//                hideKeyboard(getContext());
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });
//
//        btndevicepref.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                //actionBar.setTitle(" Device Preference");
//                frag = new DevicePreferenceActivity();
//                hideKeyboard(getContext());
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });
//
//        btnquickaccess.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                //actionBar.setTitle(" Quick access");
//                frag = new QuickAccessActivity();
//                hideKeyboard(getContext());
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });
//
//        btninventory.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                //actionBar.setTitle(" Inventory");
//                frag = new InventoryActivity();
//                hideKeyboard(getContext());
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });


//        spinnerquickedit = (Spinner)rootview.findViewById(R.id.chocolate_spinner);
//        final int selectionCurrent = spinnerquickedit.getSelectedItemPosition();

        //db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        mCredential = GoogleAccountCredential.usingOAuth2(
                getActivity().getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage(getString(R.string.setmessage16));

        Cursor cursor_f = db.rawQuery("SELECT * FROM Working_hours", null);
        if (cursor_f.moveToFirst()) {
            String id = cursor_f.getString(0);
            String five = cursor_f.getString(5);
            String six = cursor_f.getString(6);

            editText11_time_hide.setText(five);
            editText1_time_hide.setText(six);
        }
        cursor_f.close();


        Cursor allrowss = db.rawQuery("SELECT * FROM Quickedit WHERE _id = '1'", null);
        if (allrowss.moveToFirst()) {
            do {
                NAme2 = allrowss.getString(1);

            } while (allrowss.moveToNext());
        }
        allrowss.close();

        Cursor allrowwss = db.rawQuery("SELECT * FROM DeleteDB_time WHERE _id = '1'", null);
        if (allrowwss.moveToFirst()) {
            do {
                NAME2 = allrowwss.getString(1);
            } while (allrowwss.moveToNext());
        }
        allrowwss.close();


//        final Button mCallApiButton = (Button) rootview.findViewById(R.id.call_gmail_api);
////        mCallApiButton.setText(BUTTON_TEXT);
//        mCallApiButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mCallApiButton.setEnabled(false);
////                mOutputText.setText("");
//                getResultsFromApi();
//                mCallApiButton.setEnabled(true);
//            }
//        });


        timedis = (TextView) rootview.findViewById(R.id.time_dis);
        timedis.setText(NAME2);
//        spinnerquickedit.setSelection(getIndex(spinnerquickedit, NAme2));
//        spinnerquickedit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String quick = parent.getItemAtPosition(position).toString();
//                //saveInDBedit();
//                SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
//                        Context.MODE_PRIVATE, null);
//                //myDb.execSQL("delete from " + "Quickedit");          // clearing the table
//                ContentValues newValues = new ContentValues();
//                newValues.put("quickedittype", String.valueOf(quick));
//                String where = "_id = '1'";
//                myDb.update("Quickedit", newValues, where, new String[]{});
//                myDb.close();
//                ////Toast.makeText(parent.getContext(), "Quick Edit is : \n" + parent.getItemAtPosition(position).toString(),
//                //Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });


        mySwitch = (Switch) rootview.findViewById(R.id.mySwitch);

        Cursor callrows = db.rawQuery("SELECT * FROM DeleteDBon_off WHERE _id = '1'", null);
        if (callrows.moveToFirst()) {
            do {
                NAME1 = callrows.getString(1);
            } while (callrows.moveToNext());
        }
        callrows.close();

//        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);


        if (NAME1.toString().equals("On")) {
            mySwitch.setChecked(true);
            //Toast.makeText(getActivity(), "on", Toast.LENGTH_SHORT).show();
        } else {
            mySwitch.setChecked(false);
            //Toast.makeText(getActivity(), "off", Toast.LENGTH_SHORT).show();
        }

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
//                    Toast.makeText(getActivity(), "Checked", Toast.LENGTH_SHORT).show();

                    final Dialog dialog = new Dialog(getActivity(), R.style.notitle);
                    dialog.setContentView(R.layout.dialog_deletedb_schedule_freq_time);
                    dialog.show();

                    final Button cancel = (Button) dialog.findViewById(R.id.cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            mySwitch.setChecked(false);
                        }
                    });



                    timeset = (TextView) dialog.findViewById(R.id.timeget);

                    timeset.setText(NAME2);

                    timeset.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(getActivity(), R.style.timepicker_date_dialog, timePickerListener, hour, minute,
                                    false);

                            timePickerDialog.show();
                        }
                    });


                    Button save = (Button) dialog.findViewById(R.id.savetime);
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final Dialog dialog1 = new Dialog(getActivity(), R.style.notitle);
                            dialog1.setContentView(R.layout.dialog_db_delete_warning);
                            dialog1.show();

                            ImageView ca = (ImageView) dialog1.findViewById(R.id.closetext);
                            ca.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog1.dismiss();
                                }
                            });

                            Button cancel2 = (Button) dialog1.findViewById(R.id.cancel);
                            cancel2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog1.dismiss();
                                }
                            });

                            Button save2 = (Button) dialog1.findViewById(R.id.ok);
                            save2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog1.dismiss();
                                    dialog.dismiss();
                                    mySwitch.setChecked(true);
                                    String time = timeset.getText().toString();

                                    ContentValues newValues = new ContentValues();
                                    newValues.put("status", "On");
                                    String where = "_id = '1'";
                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "DeleteDBon_off");
                                    getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
                                    resultUri = new Uri.Builder()
                                            .scheme("content")
                                            .authority(StubProviderApp.AUTHORITY)
                                            .path("DeleteDBon_off")
                                            .appendQueryParameter("operation", "update")
                                            .appendQueryParameter("_id", "1")
                                            .build();
                                    getActivity().getContentResolver().notifyChange(resultUri, null);
//                                    db.update("DeleteDBon_off", newValues, where, new String[]{});

                                    ContentValues newValues1 = new ContentValues();
                                    newValues1.put("time", time);
                                    String where1 = "_id = '1'";

                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "DeleteDB_time");
                                    getActivity().getContentResolver().update(contentUri, newValues1,where,new String[]{});
                                    resultUri = new Uri.Builder()
                                            .scheme("content")
                                            .authority(StubProviderApp.AUTHORITY)
                                            .path("DeleteDB_time")
                                            .appendQueryParameter("operation", "update")
                                            .appendQueryParameter("_id", "1")
                                            .build();
                                    getActivity().getContentResolver().notifyChange(resultUri, null);
//                                    db.update("DeleteDB_time", newValues1, where1, new String[]{});

                                    timedis.setText(time);
                                }
                            });


                        }
                    });


                } else {
//                    Toast.makeText(getActivity(), "not checked", Toast.LENGTH_SHORT).show();
                    ContentValues newValues = new ContentValues();
                    newValues.put("status", "Off");
                    String where = "_id = '1'";

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "DeleteDBon_off");
                    getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("DeleteDBon_off")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id", "1")
                            .build();
                    getActivity().getContentResolver().notifyChange(resultUri, null);
//                    db.update("DeleteDBon_off", newValues, where, new String[]{});
                }
            }
        });

        mySwitch_barcode = (Switch) rootview.findViewById(R.id.mySwitch_barcode);
        auto_generation_status = (TextView) rootview.findViewById(R.id.auto_generation_status);
        auto_generation_layout = (RelativeLayout) rootview.findViewById(R.id.auto_generation);

        Cursor callrowss = db.rawQuery("SELECT * FROM Auto_generate_barcode WHERE _id = '1'", null);
        if (callrowss.moveToFirst()) {
            do {
                NAME3 = callrowss.getString(1);
            } while (callrowss.moveToNext());
        }
        callrowss.close();

        if (NAME3.toString().equals("On")) {
            mySwitch_barcode.setChecked(true);
            auto_generation_status.setText("On");

            //Toast.makeText(getActivity(), "on", Toast.LENGTH_SHORT).show();
        } else {
            mySwitch_barcode.setChecked(false);
            auto_generation_status.setText("Off");
            //Toast.makeText(getActivity(), "off", Toast.LENGTH_SHORT).show();
        }

        auto_generation_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor callrowss = db.rawQuery("SELECT * FROM Auto_generate_barcode WHERE _id = '1'", null);
                if (callrowss.moveToFirst()) {
                    do {
                        NAME3 = callrowss.getString(1);
                    } while (callrowss.moveToNext());
                }
                callrowss.close();

//                dialog1 = new Dialog(getActivity(), R.style.notitle);
//                dialog1.setContentView(R.layout.dialog_barcode_auto_generation_on_off);
//                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                dialog1.show();

                if (NAME3.toString().equals("On")) {
                    dialog1 = new Dialog(getActivity(), R.style.notitle);
                    dialog1.setContentView(R.layout.dialog_barcode_auto_generation_on_off);
                    dialog1.show();

                    final CheckBox clear_barcode = (CheckBox) dialog1.findViewById(R.id.clear_barcode);

                    Button cancel = (Button) dialog1.findViewById(R.id.cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog1.dismiss();
                        }
                    });

                    ImageView cancel1 = (ImageView) dialog1.findViewById(R.id.closetext);
                    cancel1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog1.dismiss();
                        }
                    });

                    Button save = (Button) dialog1.findViewById(R.id.ok);
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (clear_barcode.isChecked()){
                                final Dialog dialog2 = new Dialog(getActivity(), R.style.notitle);
                                dialog2.setContentView(R.layout.dialog_barcode_values_remove);
                                dialog2.show();

                                ImageView cancel2 = (ImageView) dialog2.findViewById(R.id.closetext);
                                cancel2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog2.dismiss();
                                    }
                                });

                                Button cancel3 = (Button) dialog2.findViewById(R.id.cancel);
                                cancel3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog2.dismiss();
                                    }
                                });

                                Button save2 = (Button) dialog2.findViewById(R.id.ok);
                                save2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ContentValues newValues = new ContentValues();
                                        newValues.put("generate", "Off");
                                        String where = "_id = '1'";

                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Auto_generate_barcode");
                                        getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProviderApp.AUTHORITY)
                                                .path("Auto_generate_barcode")
                                                .appendQueryParameter("operation", "update")
                                                .appendQueryParameter("_id", "1")
                                                .build();
                                        getActivity().getContentResolver().notifyChange(resultUri, null);
//                                        db.update("Auto_generate_barcode", newValues, where, new String[]{});
                                        auto_generation_status.setText("Off");

                                        DownloadMusicfromInternet11 downloadMusicfromInternet = new DownloadMusicfromInternet11();
                                        downloadMusicfromInternet.execute();

//                                        nullified
//                                        Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
//                                        if (cursor.moveToFirst()){
//                                            do {
//                                                String item_id = cursor.getString(0);
//                                                ContentValues contentValues = new ContentValues();
//                                                contentValues.put("barcode_value", "");
//                                                String where1 = "_id = '" + item_id + "' ";
//                                                db.update("Items", contentValues, where1, new String[]{});
//                                            }while (cursor.moveToNext());
//                                        }


                                        dialog2.dismiss();
                                        dialog1.dismiss();
                                        //delete database barcode values

                                    }
                                });
                            }else {
                                ContentValues newValues = new ContentValues();
                                newValues.put("generate", "Off");
                                String where = "_id = '1'";
                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Auto_generate_barcode");
                                getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProviderApp.AUTHORITY)
                                        .path("Auto_generate_barcode")
                                        .appendQueryParameter("operation", "update")
                                        .appendQueryParameter("_id", "1")
                                        .build();
                                getActivity().getContentResolver().notifyChange(resultUri, null);
//                                db.update("Auto_generate_barcode", newValues, where, new String[]{});
                                auto_generation_status.setText("Off");
                                dialog1.dismiss();
                            }
                        }
                    });
                }else {//1st initially
                    dialog1 = new Dialog(getActivity(), R.style.notitle);
                    dialog1.setContentView(R.layout.dialog_barcode_auto_generation_off_on);
                    dialog1.show();

                    final CheckBox clear_barcode = (CheckBox) dialog1.findViewById(R.id.clear_barcode);

                    Button cancel = (Button) dialog1.findViewById(R.id.cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog1.dismiss();
                        }
                    });

                    ImageView cancel1 = (ImageView) dialog1.findViewById(R.id.closetext);
                    cancel1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog1.dismiss();
                        }
                    });

                    Button save = (Button) dialog1.findViewById(R.id.ok);
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (clear_barcode.isChecked()){
                                final Dialog dialog2 = new Dialog(getActivity(), R.style.notitle);
                                dialog2.setContentView(R.layout.dialog_barcode_values_remove);
                                dialog2.show();

                                ImageView cancel2 = (ImageView) dialog2.findViewById(R.id.closetext);
                                cancel2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog2.dismiss();
                                    }
                                });

                                Button cancel3 = (Button) dialog2.findViewById(R.id.cancel);
                                cancel3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog2.dismiss();
                                    }
                                });

                                Button save2 = (Button) dialog2.findViewById(R.id.ok);
                                save2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ContentValues newValues = new ContentValues();
                                        newValues.put("generate", "On");
                                        String where = "_id = '1'";
                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Auto_generate_barcode");
                                        getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProviderApp.AUTHORITY)
                                                .path("Auto_generate_barcode")
                                                .appendQueryParameter("operation", "update")
                                                .appendQueryParameter("_id", "1")
                                                .build();
                                        getActivity().getContentResolver().notifyChange(resultUri, null);
//                                        db.update("Auto_generate_barcode", newValues, where, new String[]{});
                                        auto_generation_status.setText("On");

                                        DownloadMusicfromInternet111 downloadMusicfromInternet = new DownloadMusicfromInternet111();
                                        downloadMusicfromInternet.execute();

//                                        nullified
//                                        Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
//                                        if (cursor.moveToFirst()){
//                                            do {
//                                                String item_id = cursor.getString(0);
////                                                Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE barcode_value = '"+item_id+"'", null);
////                                                if (cursor1.moveToFirst()){
////                                                    do {
//////                                                        int count = cursor1.getCount();
////                                                        ContentValues contentValues = new ContentValues();
////                                                        contentValues.put("barcode_value", item_id+"_1");
////                                                        String where1 = "_id = '" + item_id + "' ";
////                                                        db.update("Items", contentValues, where1, new String[]{});
////                                                    }while (cursor1.moveToNext());
////                                                }else {
//                                                    ContentValues contentValues = new ContentValues();
//                                                    contentValues.put("barcode_value", item_id);
//                                                    String where1 = "_id = '" + item_id + "' ";
//                                                    db.update("Items", contentValues, where1, new String[]{});
////                                                }
//                                            }while (cursor.moveToNext());
//                                        }


                                        dialog2.dismiss();
                                        dialog1.dismiss();
                                        //delete database barcode values

                                    }
                                });
                            }else {

                                ContentValues newValues = new ContentValues();
                                newValues.put("generate", "On");
                                String where = "_id = '1'";

                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Auto_generate_barcode");
                                getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProviderApp.AUTHORITY)
                                        .path("Auto_generate_barcode")
                                        .appendQueryParameter("operation", "update")
                                        .appendQueryParameter("_id", "1")
                                        .build();
                                getActivity().getContentResolver().notifyChange(resultUri, null);
//                                db.update("Auto_generate_barcode", newValues, where, new String[]{});
                                auto_generation_status.setText("On");
                                dialog1.dismiss();

                                DownloadMusicfromInternet1111 downloadMusicfromInternet = new DownloadMusicfromInternet1111();
                                downloadMusicfromInternet.execute();

//                                nullified
//                                Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE barcode_value = '' OR barcode_value IS NULL ", null);
//                                if (cursor.moveToFirst()){
//                                    do {
//                                        String item_id = cursor.getString(0);
////                                        Toast.makeText(getActivity(), " "+item_id, Toast.LENGTH_SHORT).show();
////                                        ContentValues contentValues1 = new ContentValues();
////                                        contentValues1.put("barcode_value", item_id);
////                                        String where11 = "_id = '" + item_id + "' ";
////                                        db.update("Items", contentValues1, where11, new String[]{});
//                                        Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE barcode_value = '"+item_id+"'", null);
//                                        if (cursor1.moveToFirst()){
//                                            do {
////                                                int count = cursor1.getCount();
////                                                int co = count+1;
////                                                Cursor cursor2 = db.rawQuery("SELECT * FROM Items WHERE barcode_value LIKE '1_%'  ", null);
////                                                if (cursor2.moveToFirst()){
////                                                    do {
////                                                        int count1 = cursor2.getCount();
////                                                        int co2 = count1+1;
//////                                                        String item_id1 = cursor1.getString(0);
//////                                                        ContentValues contentValues = new ContentValues();
//////                                                        contentValues.put("barcode_value", item_id+"_"+String.valueOf(count+count1));
//////                                                        String where1 = "_id = '" + item_id1 + "' ";
//////                                                        db.update("Items", contentValues, where1, new String[]{});
////
////                                                        ContentValues contentValues1 = new ContentValues();
////                                                        contentValues1.put("barcode_value", item_id+"_"+String.valueOf(count+count1));
////                                                        String where11 = "_id = '" + item_id + "' ";
////                                                        db.update("Items", contentValues1, where11, new String[]{});
////                                                    }while (cursor2.moveToNext());
////                                                }else {
//////                                                    String item_id1 = cursor1.getString(0);
//////                                                    ContentValues contentValues = new ContentValues();
//////                                                    contentValues.put("barcode_value", item_id+"_"+String.valueOf(co));
//////                                                    String where1 = "_id = '" + item_id1 + "' ";
//////                                                    db.update("Items", contentValues, where1, new String[]{});
////
//////                                                String item_id1 = cursor1.getString(0);
////                                                    ContentValues contentValues1 = new ContentValues();
////                                                    contentValues1.put("barcode_value", item_id+"_"+String.valueOf(co));
////                                                    String where11 = "_id = '" + item_id + "' ";
////                                                    db.update("Items", contentValues1, where11, new String[]{});
////                                                }
//
//
//                                            }while (cursor1.moveToNext());
//                                        }else {
//                                            ContentValues contentValues = new ContentValues();
//                                            contentValues.put("barcode_value", item_id);
//                                            String where1 = "_id = '" + item_id + "' ";
//                                            db.update("Items", contentValues, where1, new String[]{});
//                                        }
//                                    }while (cursor.moveToNext());
//                                }


                            }
                        }
                    });
                }

            }
        });

        mySwitch_barcode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
//                    Toast.makeText(getActivity(), "Checked", Toast.LENGTH_SHORT).show();

//                    final Dialog dialog = new Dialog(getActivity(), R.style.notitle);
//                    dialog.setContentView(R.layout.dialog_deletedb_schedule_freq_time);
//                    dialog.show();
//
//                    final Button cancel = (Button) dialog.findViewById(R.id.cancel);
//                    cancel.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialog.dismiss();
//                            mySwitch_barcode.setChecked(false);
//                        }
//                    });
//
//
//                    Button save = (Button) dialog.findViewById(R.id.savetime);
//                    save.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {

//                            final Dialog dialog1 = new Dialog(getActivity(), R.style.notitle);
//                            dialog1.setContentView(R.layout.dialog_db_delete_warning);
//                            dialog1.show();

//                            ImageView ca = (ImageView) dialog1.findViewById(R.id.closetext);
//                            ca.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    dialog1.dismiss();
//                                }
//                            });

//                            Button cancel2 = (Button) dialog1.findViewById(R.id.cancel);
//                            cancel2.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    dialog1.dismiss();
//                                }
//                            });

//                            Button save2 = (Button) dialog1.findViewById(R.id.ok);
//                            save2.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    dialog1.dismiss();
//                                    dialog.dismiss();
                                    mySwitch_barcode.setChecked(true);
//                                    String time = timeset.getText().toString();

                    ContentValues newValues = new ContentValues();
                    newValues.put("generate", "On");
                    String where = "_id = '1'";

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Auto_generate_barcode");
                    getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Auto_generate_barcode")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id", "1")
                            .build();
                    getActivity().getContentResolver().notifyChange(resultUri, null);
//                    db.update("Auto_generate_barcode", newValues, where, new String[]{});

//                                    timedis.setText(time);
//                                }
//                            });


//                        }
//                    });


                } else {
//                    Toast.makeText(getActivity(), "not checked", Toast.LENGTH_SHORT).show();
                    ContentValues newValues = new ContentValues();
                    newValues.put("generate", "Off");
                    String where = "_id = '1'";


                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Auto_generate_barcode");
                    getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Auto_generate_barcode")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id", "1")
                            .build();
                    getActivity().getContentResolver().notifyChange(resultUri, null);
//                    db.update("Auto_generate_barcode", newValues, where, new String[]{});
                }
            }
        });

        itemsort = (Spinner) rootview.findViewById(R.id.controlsort);
        Cursor allrowswss = db.rawQuery("SELECT * FROM Itemsort WHERE _id = '1'", null);
        if (allrowswss.moveToFirst()) {
            do {
                String NAME = allrowswss.getString(1);
                itemsort.setSelection(getIndex(itemsort, NAME));

            } while (allrowswss.moveToNext());
        }
        allrowswss.close();
        final int selectionCurrentt1 = itemsort.getSelectedItemPosition();
        itemsort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                        Context.MODE_PRIVATE, null);
                ContentValues newValues = new ContentValues();
                newValues.put("itemsorttype", String.valueOf(itemsort.getSelectedItem().toString()));
                String where = "_id = '1'";

                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Itemsort");
                getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
                resultUri = new Uri.Builder()
                        .scheme("content")
                        .authority(StubProviderApp.AUTHORITY)
                        .path("Itemsort")
                        .appendQueryParameter("operation", "update")
                        .appendQueryParameter("_id","1")
                        .build();
                getActivity().getContentResolver().notifyChange(resultUri, null);


//                myDb.update("Itemsort", newValues, where, new String[]{});
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        stockcontrol = (Spinner) rootview.findViewById(R.id.controlstock);
        //db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor allrowsws = db.rawQuery("SELECT * FROM Stockcontrol WHERE _id = '1'", null);
        if (allrowsws.moveToFirst()) {
            do {
                String NAME = allrowsws.getString(1);
                stockcontrol.setSelection(getIndex(stockcontrol, NAME));

            } while (allrowsws.moveToNext());
        }
        allrowsws.close();
        final int selectionCurrentt = stockcontrol.getSelectedItemPosition();
        stockcontrol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                        Context.MODE_PRIVATE, null);
                ContentValues newValues = new ContentValues();
                newValues.put("stockcontroltype", String.valueOf(stockcontrol.getSelectedItem().toString()));
                String where = "_id = '1'";


                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Stockcontrol");
                getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
                resultUri = new Uri.Builder()
                        .scheme("content")
                        .authority(StubProviderApp.AUTHORITY)
                        .path("Stockcontrol")
                        .appendQueryParameter("operation", "update")
                        .appendQueryParameter("_id","1")
                        .build();
                getActivity().getContentResolver().notifyChange(resultUri, null);

//                myDb.update("Stockcontrol", newValues, where, new String[]{});

                if (stockcontrol.getSelectedItem().toString().equals("Off")) {
                    ContentValues newValues1 = new ContentValues();
                    newValues1.put("stockresettype", "Off");
                    String where1 = "_id = '1'";

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Stockreset");
                    getActivity().getContentResolver().update(contentUri, newValues1,where1,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Stockreset")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id","1")
                            .build();
                    getActivity().getContentResolver().notifyChange(resultUri, null);
//                    myDb.update("Stockreset", newValues1, where1, new String[]{});
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        def_bill_count = (RelativeLayout) rootview.findViewById(R.id.def_bill_count);
        bill_count = (TextView) rootview.findViewById(R.id.bill_count);

        Cursor cursor_bill_count = db1.rawQuery("SELECT * FROM BillCount", null);
        if (cursor_bill_count.moveToFirst()){
            String str_bill_count = cursor_bill_count.getString(1);
            bill_count.setText(str_bill_count);
//            Toast.makeText(getActivity(), "data is "+str_bill_count,
//                    Toast.LENGTH_SHORT).show();
        }
        cursor_bill_count.close();

        def_bill_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity(),
                        R.style.timepicker_date_dialog);
                dialog.setContentView(R.layout.dialog_bill_count);

                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                final EditText editText1 = (EditText)
                        dialog.findViewById(R.id.editText1);
                editText1.setText(bill_count.getText().toString());

                ImageButton btncancel = (ImageButton)
                        dialog.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                Button btnsave = (Button) dialog.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ContentValues newValues = new ContentValues();
                        newValues.put("value", editText1.getText().toString());
                        String where = "_id = '1'";
                        db1.update("BillCount", newValues, where, new String[]{});
//                        contentUri =
                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "BillCount");
//
                        getActivity().getContentResolver().update(contentUri,
                                newValues,where,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProvider.AUTHORITY)
                                .path("BillCount")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id","1")
                                .build();
//
                        getActivity().getContentResolver().notifyChange(resultUri, null);

                        dialog.dismiss();

                        bill_count.setText(editText1.getText().toString());
                    }
                });


            }
        });

        def_merchant_id = (RelativeLayout) rootview.findViewById(R.id.def_merchant_id);
        merchant_id = (TextView) rootview.findViewById(R.id.merchant_id);

        Cursor cursor_merchant_id = db.rawQuery("SELECT * FROM Restaurant_id", null);
        if (cursor_merchant_id.moveToFirst()){
            String str_merchant_id = cursor_merchant_id.getString(1);
            merchant_id.setText(str_merchant_id);
//            Toast.makeText(getActivity(), "data is "+str_merchant_id,
//                    Toast.LENGTH_SHORT).show();
        }
        cursor_merchant_id.close();

        def_merchant_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity(),
                        R.style.timepicker_date_dialog);
                dialog.setContentView(R.layout.dialog_merchant_id);

                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                final EditText editText1 = (EditText)
                        dialog.findViewById(R.id.editText1);
                editText1.setText(merchant_id.getText().toString());

                ImageButton btncancel = (ImageButton)
                        dialog.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                ImageButton btnsave = (ImageButton) dialog.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (editText1.getText().toString().equals("")){

                        }else {

                            db.execSQL("delete from Restaurant_id");
                            webservicequery("delete from Restaurant_id");

//                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Restaurant_id");
//                            getActivity().getContentResolver().delete(contentUri, null, null);
//                            resultUri = new Uri.Builder()
//                                    .scheme("content")
//                                    .authority(StubProviderApp.AUTHORITY)
//                                    .path("Restaurant_id")
//                                    .appendQueryParameter("operation", "delete")
//                                    .appendQueryParameter("1", "1")
//                                    .build();
//                            getActivity().getContentResolver().notifyChange(resultUri, null);


                            ContentValues newValues = new ContentValues();
                            newValues.put("merchant_id", editText1.getText().toString());
                            db.insert("Restaurant_id", null, newValues);

                            webservicequery("INSERT INTO Restaurant_id (_id, merchant_id) VALUES ('1', '"+editText1.getText().toString()+"')");
//

//                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Restaurant_id");
//                            resultUri = getActivity().getContentResolver().insert(contentUri, newValues);
//                            getActivity().getContentResolver().notifyChange(resultUri, null);

                            dialog.dismiss();

                            merchant_id.setText(editText1.getText().toString());


//                            try {
//                                io.socket.client.Socket socket = IO.socket("https://www.werafoods.com:9005");
//                                socket.disconnect();
//                                socket.off();
//                                socket.connect();
//                                socket.on("notification", new Emitter.Listener() {
//                                    @Override
//                                    public void call(Object... args) {
//                                        try {
//                                            JSONObject obj = (JSONObject) args[0];
//                                            JSONObject obj1 = (JSONObject) obj;
//                                            System.out.println("Merchant Id : " + obj1.getString("message") + " "+editText1.getText().toString());
//
//
//                                            if (obj1.getString("message").equals(editText1.getText().toString())){
//                                                System.out.println("order received "+editText1.getText().toString());
//
//                                            }
//                                        } catch (Exception e) {
//                                            System.out.println(e.getLocalizedMessage());
//                                        }
//                                    }
//                                });
//                            }catch (Exception e) {
//                                e.printStackTrace();
//                            }


                            Cursor cursor_merchant_id = db.rawQuery("SELECT * FROM Restaurant_id", null);
                            if (cursor_merchant_id.moveToFirst()){
                                str_merch_id = cursor_merchant_id.getString(1);
                            }
                            cursor_merchant_id.close();


                            Toast.makeText(getActivity(), "menu upload", Toast.LENGTH_SHORT).show();

                            try {

                                JSONObject  jsonArray = new JSONObject();
                                JSONArray entityarray = new JSONArray();
                                JSONArray mainCategories = new JSONArray();
                                JSONArray items = new JSONArray();
                                JSONObject entity = new JSONObject();


                                Cursor cursor12 = db.rawQuery("SELECT * FROM Hotel", null);
                                if (cursor12.moveToFirst()){
                                    do {
                                        System.out.println("menu upload begins ");
                                        String c_id1 = cursor12.getString(0);
                                        TextView c_id = new TextView(getActivity());
                                        c_id.setText(c_id1);
                                        String category1 = cursor12.getString(1);
                                        TextView category = new TextView(getActivity());
                                        category.setText(category1);

//                                         JSONArray entityarray = new JSONArray();
                                        //JSONObject entity = new JSONObject();
//                                         JSONArray mainCategories = new JSONArray();
                                        JSONObject mainCategory = new JSONObject();
                                        mainCategory.put("id", c_id.getText().toString());
                                        mainCategory.put("name", category.getText().toString());
                                        mainCategory.put("description", null);
                                        mainCategory.put("order", null);
                                        JSONArray subCategories = new JSONArray();
                                        JSONObject subCategory = new JSONObject();
                                        subCategory.put("id", c_id.getText().toString());
                                        subCategory.put("name", category.getText().toString());
                                        subCategory.put("description", category.getText().toString());
                                        subCategory.put("order", null);
                                        subCategories.put(subCategory);
                                        mainCategory.put("sub_categories", subCategories);
                                        mainCategories.put(mainCategory);
                                        entity.put("main_categories", mainCategories);
                                        //jsonArray.put("entity",entity);

//
                                        System.out.println("menu category json array: "+mainCategories);
                                        System.out.println("menu json array: "+jsonArray);
                                    }while (cursor12.moveToNext());
                                }
                                cursor12.close();


                                Cursor cursor = db.rawQuery("SELECT * FROM items", null);
                                if (cursor.moveToFirst()){
                                    do {
                                        System.out.println("menu upload begins ");
                                        String id11 = cursor.getString(0);

                                        TextView id1 = new TextView(getActivity());
                                        id1.setText(id11);
                                        String itemname1 = cursor.getString(1);
                                        TextView itemname = new TextView(getActivity());
                                        itemname.setText(itemname1);
                                        String price1 = cursor.getString(2);
                                        TextView price = new TextView(getActivity());
                                        price.setText(price1);
                                        String categoryi = cursor.getString(4);
                                        TextView category = new TextView(getActivity());
                                        category.setText(categoryi);
                                        String categoryid1=null;
                                        TextView categoryid = new TextView(getActivity());
                                        Cursor cursorcat = db.rawQuery("SELECT * FROM Hotel", null);
                                        if (cursorcat.moveToFirst()){
                                            do {

                                                String categorycat1 = cursorcat.getString(1);
                                                TextView categorycat = new TextView(getActivity());
                                                categorycat.setText(categorycat1);
                                                if(category.getText().toString().equalsIgnoreCase(categorycat.getText().toString())){
                                                    categoryid1 = cursorcat.getString(0);
                                                    categoryid.setText(categoryid1);
                                                }
                                            }while (cursorcat.moveToNext());
                                        }
                                        cursor12.close();
                                        String quanti1 = cursor.getString(3);
                                        TextView quanti = new TextView(getActivity());
                                        quanti.setText(quanti1);
                                        String unit1 = cursor.getString(26);
                                        TextView unit = new TextView(getActivity());
                                        unit.setText(unit1);
                                        String instock1 = cursor.getString(51);
                                        TextView instock = new TextView(getActivity());
                                        instock.setText(instock1);
                                        boolean stockflag = true;
                                        if (instock.getText().toString().isEmpty()) {
                                        }else {
                                            if (instock.getText().toString().equalsIgnoreCase("yes")) {
                                                stockflag = false;
                                            } else if (instock.getText().toString().equalsIgnoreCase("no")) {
                                                stockflag = true;
                                            }
                                        }
                                        String isveg1 = cursor.getString(57);
                                        TextView isveg = new TextView(getActivity());
                                        isveg.setText(isveg1);
                                        String packingcharges1 = cursor.getString(58);
                                        TextView packingcharges = new TextView(getActivity());
                                        packingcharges.setText(packingcharges1);



//                                         JSONArray entityarray = new JSONArray();
                                        //JSONObject entity = new JSONObject();
//                                         JSONArray mainCategories = new JSONArray();
                                         /*JSONObject mainCategory = new JSONObject();
                                         mainCategory.put("id", id1);
                                         mainCategory.put("name", category);
                                         mainCategory.put("description", null);
                                         mainCategory.put("order", null);
                                         JSONArray subCategories = new JSONArray();
                                         JSONObject subCategory = new JSONObject();
                                         subCategory.put("id", id1);
                                         subCategory.put("name", category);
                                         subCategory.put("description", category);
                                         subCategory.put("order", null);
                                         subCategories.put(subCategory);
                                         mainCategory.put("sub_categories", subCategories);
                                         mainCategories.put(mainCategory);
                                         entity.put("main_categories", mainCategories);*/

//                                         JSONArray items = new JSONArray();
                                        JSONObject itemdetails = new JSONObject();
                                        itemdetails.put("id", id1.getText().toString());
                                        itemdetails.put("category_id", categoryid.getText().toString());
                                        itemdetails.put("sub_category_id", categoryid.getText().toString());
                                        itemdetails.put("name", itemname.getText().toString());
                                        itemdetails.put("is_veg", isveg.getText().toString());
                                        itemdetails.put("image_url_swiggy", null);
                                        itemdetails.put("image_url_zomato", null);
                                        itemdetails.put("description", null);
                                        itemdetails.put("price", price.getText().toString());
                                        JSONArray gst_details = new JSONArray();
                                        JSONObject gstdetail = new JSONObject();
                                        gstdetail.put("igst", null);
                                        gstdetail.put("sgst", null);
                                        gstdetail.put("cgst", null);
                                        gstdetail.put("inclusive", null);
                                        gstdetail.put("gst_liability", null);
                                        gst_details.put(gstdetail);
                                        itemdetails.put("gst_details",gst_details);
                                        itemdetails.put("packing_charges", packingcharges.getText().toString());
                                        itemdetails.put("enable", 1);
                                        itemdetails.put("in_stock", stockflag);
                                        itemdetails.put("addon_free_limit", -1);
                                        itemdetails.put("addon_limit", -1);
                                        itemdetails.put("image_url", null);

                                        Cursor cursor1 = db.rawQuery("SELECT * FROM Working_hours", null);
                                        if (cursor1.moveToFirst()) {
                                            String opening1 = cursor1.getString(2);
                                            TextView opening = new TextView(getActivity());
                                            opening.setText(opening1);
                                            String closing1 = cursor1.getString(4);
                                            TextView closing = new TextView(getActivity());
                                            closing.setText(closing1);
                                            JSONArray item_slots = new JSONArray();
                                            JSONObject itemslots = new JSONObject();
                                            itemslots.put("open_time", opening.getText().toString());
                                            itemslots.put("close_time", closing.getText().toString());
                                            itemslots.put("day_of_week", null);
                                            item_slots.put(itemslots);
                                            itemdetails.put("item_slots", item_slots);
                                        }
                                        cursor1.close();


                                        JSONArray variant_groups = new JSONArray();
                                        //JSONObject variantgroups = new JSONObject();47
                                        for(int m = 38; m<48;m=m+2){
                                            System.out.println("variants upload begins ");
                                            String variant1 = cursor.getString(m);
                                            TextView variant = new TextView(getActivity());
                                            variant.setText(variant1);
                                            if(variant.getText().toString().isEmpty()) {
                                            }else {
                                                String vprice1 = cursor.getString(m+1);
                                                TextView vprice = new TextView(getActivity());
                                                vprice.setText(vprice1);
                                                JSONObject variantgroups1 = new JSONObject();
                                                variantgroups1.put("id", variant.getText().toString());
                                                variantgroups1.put("name", variant.getText().toString());
                                                variantgroups1.put("order", null);

                                                JSONArray variants = new JSONArray();
                                                JSONObject variants1 = new JSONObject();
                                                variants1.put("id", variant.getText().toString());
                                                variants1.put("name", variant.getText().toString());
                                                variants1.put("price", vprice.getText().toString());
                                                variants1.put("default", null);
                                                variants1.put("order", null);
                                                variants1.put("in_stock", null);
                                                variants1.put("is_veg", isveg.getText().toString());
                                                JSONArray vgst_details = new JSONArray();
                                                JSONObject vgstdetails = new JSONObject();
                                                vgstdetails.put("igst", null);
                                                vgstdetails.put("sgst", null);
                                                vgstdetails.put("cgst", null);
                                                vgstdetails.put("inclusive", null);
                                                vgstdetails.put("gst_liability", null);
                                                vgst_details.put(vgstdetails);
                                                variants1.put("gst_details",vgst_details);
                                                variants.put(variants1);
                                                variantgroups1.put("variants",variants);
                                                variant_groups.put(variantgroups1);
                                            }
                                        }

                                        itemdetails.put("variant_groups",variant_groups);



                                        JSONArray pricing_combinations = new JSONArray();



                                        for(int m = 38; m<48;m=m+2){

                                            String variant1 = cursor.getString(m);
                                            TextView variant = new TextView(getActivity());
                                            variant.setText(variant1);
                                            System.out.println("variants combination upload begins "+ variant);
                                            if(variant.getText().toString().isEmpty()) {
                                            }else {
                                                JSONObject pricingcombinations = new JSONObject();
                                                String vprice1 = cursor.getString(m+1);
                                                TextView vprice = new TextView(getActivity());
                                                vprice.setText(vprice1);
                                                pricingcombinations.put("price",vprice.getText().toString());
                                                JSONArray variant_combination = new JSONArray();
                                                JSONObject variantcombination = new JSONObject();
                                                variantcombination.put("variant_group_id", variant.getText().toString());
                                                variantcombination.put("variant_id", variant.getText().toString());
                                                variant_combination.put(variantcombination);
                                                pricingcombinations.put("addon_combination",null);
                                                pricingcombinations.put("variant_combination",variant_combination);
                                                pricing_combinations.put(pricingcombinations);
                                            }
                                        }




                                        itemdetails.put("pricing_combinations",pricing_combinations);

                                        itemdetails.put("order", null);
                                        itemdetails.put("recommended", false);
                                        JSONArray catalog_attributes = new JSONArray();
                                        JSONObject catalogattributes = new JSONObject();
                                        catalogattributes.put("spice_level", null);
                                        catalogattributes.put("sweet_level", null);
                                        catalogattributes.put("gravy_property", null);
                                        catalogattributes.put("bone_property", null);
                                        catalogattributes.put("contain_seasonal_ingredients", null);
                                        catalogattributes.put("accompaniments", null);
                                        JSONArray quantity = new JSONArray();
                                        JSONObject quant = new JSONObject();
                                        quant.put("value", quanti.getText().toString());
                                        quant.put("unit", unit.getText().toString());
                                        quantity.put(quant);
                                        catalogattributes.put("quantity",quantity);
                                        catalogattributes.put("serves_how_many", null);
                                        catalog_attributes.put(catalogattributes);
                                        itemdetails.put("catalog_attributes", catalogattributes);



                                        items.put(itemdetails);

                                        //items.put(variant_groups);
                                        entity.put("items", items);




                                         /*JSONObject json1 = new JSONObject();
                                         json1.put("id", id1);
                                         json1.put("item_name", itemname);
                                         json1.put("price", price);
                                         json1.put("price", price);
                                         json1.put("active", "true");
                                         jsonArray.put(json1);*/
                                        // entityarray.put(entity);
                                        //  jsonArray.put("entity",entity);
                                        System.out.println("menu entity : "+entity);
                                        System.out.println("menu individual json array: "+items);
                                        System.out.println("menu json array: "+jsonArray);
                                    }while (cursor.moveToNext());
                                }
                                cursor.close();

                                jsonArray.put("entity",entity);


                                json2= new JSONObject();
                                json2.put("merchant_id",str_merch_id);
                                json2.put("menu",jsonArray);
                                System.out.println(" Final menu json array: "+json2);

                                String jsonString = json2.toString();

                            }catch (JSONException ex){
                                ex.printStackTrace();
                            }

                            RequestQueue mQueue = Volley.newRequestQueue(getActivity());

                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://api.werafoods.com/pos/v2/menu/fullmenu", json2,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d("TAG", response.toString());
                                            Toast.makeText(getActivity(),"menu uploaded", Toast.LENGTH_LONG).show();
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("TAG", error.getMessage(), error);
                                }
                            }) { //no semicolon or coma
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("Content-Type", "X-Wera-Api-Key");
                                    params.put("X-Wera-Api-Key", "9b1bc8d1-99d2-4597-aa7e-64e0a9580c10");
                                    params.put("Accept", "");
                                    return params;
                                }
                            };
                            mQueue.add(jsonObjectRequest);



                        }
                    }
                });


                ImageButton btnreset = (ImageButton) dialog.findViewById(R.id.btnreset);
                btnreset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(getString(R.string.title19));
                        builder.setMessage("\n"+getString(R.string.setmessage24)+"\n");
                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                db.execSQL("delete from Restaurant_id");
                                webservicequery("delete from Restaurant_id");

                                ContentValues newValues = new ContentValues();
                                newValues.put("merchant_id", "0");
                                db.insert("Restaurant_id", null, newValues);

                                webservicequery("INSERT INTO Restaurant_id (_id, merchant_id) VALUES ('1', '0')");
                                editText1.setText("0");
                                dialog.dismiss();

                                merchant_id.setText("0");
                            }
                        });
                        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                });

                ImageButton btnreceipt = (ImageButton) dialog.findViewById(R.id.btnreceipt);
                btnreceipt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), Online_deli_chan.class);
                        startActivity(intent);
                    }
                });

            }
        });

        Cursor cursor_country = db.rawQuery("SELECT * FROM Country_Selection", null);
        if (cursor_country.moveToFirst()){
            str_country = cursor_country.getString(1);
        }
        cursor_country.close();

        TextView rs_text = (TextView) rootview.findViewById(R.id.rs_text);

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
                        if (str_country.toString().equals("Dinars")) {
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
                                                                }else {
                                                                    if (str_country.toString().equals("Kuwait Dinar")) {
                                                                        insert1_cc = "KWD";
                                                                        insert1_rs = "KWD.";
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
        }

        loyalty_points = (RelativeLayout) rootview.findViewById(R.id.loyalty_points);
        loyalty_money_point = (TextView) rootview.findViewById(R.id.loyalty_money_point);
        loyalty_point_money = (TextView) rootview.findViewById(R.id.loyalty_point_money);

        TextView inn = (TextView) rootview.findViewById(R.id.inn);
        TextView inn1 = (TextView) rootview.findViewById(R.id.inn1);
        inn.setText(insert1_rs);
        inn1.setText(insert1_rs);

        Cursor cursor_loyalty_points = db.rawQuery("SELECT * FROM loyalty_points", null);
        if (cursor_loyalty_points.moveToFirst()){
            t_money = cursor_loyalty_points.getString(1);
            t_point = cursor_loyalty_points.getString(2);
            t_point2 = cursor_loyalty_points.getString(3);
            t_money2 = cursor_loyalty_points.getString(4);

            loyalty_money_point.setText(t_money);
            loyalty_point_money.setText(t_money2);
        }
        cursor_loyalty_points.close();

        final LinearLayout loyal_not_set = (LinearLayout) rootview.findViewById(R.id.loyal_not_set);
        final LinearLayout loyal_set = (LinearLayout) rootview.findViewById(R.id.loyal_set);

        Cursor cursor_loyal = db.rawQuery("SELECT * FROM loyalty_points_status", null);
        if (cursor_loyal.moveToFirst()) {
            String sta = cursor_loyal.getString(1);
            if (sta.toString().equals("") || sta.toString().equals("no")) {
                loyal_not_set.setVisibility(View.VISIBLE);
                loyal_set.setVisibility(View.GONE);
            }else {
                loyal_not_set.setVisibility(View.GONE);
                loyal_set.setVisibility(View.VISIBLE);
            }
        }
        cursor_loyal.close();

        loyalty_points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity(), R.style.timepicker_date_dialog);
                dialog.setContentView(R.layout.dialog_loyalty_points);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                ImageButton btncancel = (ImageButton) dialog.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                Cursor cursor_loyalty_points = db.rawQuery("SELECT * FROM loyalty_points", null);
                if (cursor_loyalty_points.moveToFirst()){
                    t_money = cursor_loyalty_points.getString(1);
                    t_point = cursor_loyalty_points.getString(2);
                    t_point2 = cursor_loyalty_points.getString(3);
                    t_money2 = cursor_loyalty_points.getString(4);
                }
                cursor_loyalty_points.close();

                dialog_amount = (TextView) dialog.findViewById(R.id.dialog_amount);
                dialog_point = (TextView) dialog.findViewById(R.id.dialog_point);
                final TextView dialog_point2 = (TextView) dialog.findViewById(R.id.dialog_point2);
                final TextView dialog_amount2 = (TextView) dialog.findViewById(R.id.dialog_amount2);

                dialog_amount.setText(t_money);
                dialog_point.setText("1");
                dialog_point2.setText("1");
                dialog_amount2.setText(t_money2);

                ImageButton btnsave = (ImageButton) dialog.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db1.execSQL("CREATE TABLE if not exists customer_loyalty_points (_id integer PRIMARY KEY UNIQUE, phoneno text, points text);");
                        if (dialog_amount.getText().toString().equals("") || dialog_point.getText().toString().equals("") ||
                                dialog_point2.getText().toString().equals("") || dialog_amount2.getText().toString().equals("")){
                            if (dialog_amount.getText().toString().equals("")){
                                dialog_amount.setError("Enter Amount");
                            }
                            if (dialog_point.getText().toString().equals("")){
                                dialog_point.setError("Enter Point");
                            }
                            if (dialog_point2.getText().toString().equals("")){
                                dialog_point2.setError("Enter Point");
                            }
                            if (dialog_amount2.getText().toString().equals("")){
                                dialog_amount2.setError("Enter Amount");
                            }
                        }else {
                            db.execSQL("delete from loyalty_points");
                            webservicequery("delete from loyalty_points");

                            ContentValues newValues = new ContentValues();
                            newValues.put("money", dialog_amount.getText().toString());
                            newValues.put("point", dialog_point.getText().toString());
                            newValues.put("point2", dialog_point2.getText().toString());
                            newValues.put("money2", dialog_amount2.getText().toString());
                            db.insert("loyalty_points", null, newValues);

                            webservicequery("INSERT INTO loyalty_points (_id, money, point, point2, money2) VALUES ('1', '"+dialog_amount.getText().toString()+"', '"+dialog_point.getText().toString()+"', '"+dialog_point2.getText().toString()+"', '"+dialog_amount2.getText().toString()+"')");

                            dialog.dismiss();
                            loyalty_money_point.setText(dialog_amount.getText().toString());
                            loyalty_point_money.setText(dialog_amount2.getText().toString());

                            Cursor cursor = db.rawQuery("SELECT * FROM loyalty_points_status", null);
                            if (cursor.moveToFirst()){
                                String sta = cursor.getString(1);
                                if (sta.toString().equals("") || sta.toString().equals("no")){
                                    db.execSQL("delete from loyalty_points_status");
                                    webservicequery("delete from loyalty_points_status");

                                    ContentValues newValues1 = new ContentValues();
                                    newValues1.put("status", "yes");
                                    db.insert("loyalty_points_status", null, newValues1);

                                    webservicequery("INSERT INTO loyalty_points_status (_id, status) VALUES ('1', 'yes')");

                                    loyal_not_set.setVisibility(View.GONE);
                                    loyal_set.setVisibility(View.VISIBLE);

//                                    DownloadMusicfromInternet1_loya downloadMusicfromInternet1_loya = new DownloadMusicfromInternet1_loya();
//                                    downloadMusicfromInternet1_loya.execute(text.getText().toString());

                                }else {

                                }
                            }
                            cursor.close();
                        }
                    }
                });


            }
        });


        round_off = (Spinner) rootview.findViewById(R.id.round_off);
        //db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor aallrowswsq = db.rawQuery("SELECT * FROM Round_off WHERE _id = '1'", null);
        if (aallrowswsq.moveToFirst()) {
            do {
                String NAME = aallrowswsq.getString(1);
                round_off.setSelection(getIndex(round_off, NAME));

            } while (aallrowswsq.moveToNext());
        }
        aallrowswsq.close();
        final int selectionCurrentt111 = round_off.getSelectedItemPosition();
        round_off.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                        Context.MODE_PRIVATE, null);
                ContentValues newValues = new ContentValues();
                newValues.put("round_off_status", String.valueOf(round_off.getSelectedItem().toString()));
                String where = "_id = '1'";
                myDb.update("Round_off", newValues, where, new String[]{});
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        estimate_off = (Spinner) rootview.findViewById(R.id.estimate_off);
        //db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor estimate_print = db.rawQuery("SELECT * FROM Estimate_print WHERE _id = '1'", null);
        if (estimate_print.moveToFirst()) {
            do {
                String NAME = estimate_print.getString(1);
                estimate_off.setSelection(getIndex(estimate_off, NAME));

            } while (estimate_print.moveToNext());
        }
        estimate_print.close();
        final int selectionCurrentt1111 = estimate_off.getSelectedItemPosition();
        estimate_off.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                        Context.MODE_PRIVATE, null);
                ContentValues newValues = new ContentValues();
                newValues.put("status", String.valueOf(estimate_off.getSelectedItem().toString()));
                String where = "_id = '1'";
                myDb.update("Estimate_print", newValues, where, new String[]{});
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        RelativeLayout tablay = (RelativeLayout) rootview.findViewById(R.id.tablay);
        tab_kot_manag = (Spinner) rootview.findViewById(R.id.tab_kot_manag);
        //db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor cursor_tab_kot_manag = db.rawQuery("SELECT * FROM Table_kot WHERE _id = '1'", null);
        if (cursor_tab_kot_manag.moveToFirst()) {
            do {
                String NAME = cursor_tab_kot_manag.getString(1);
                tab_kot_manag.setSelection(getIndex(tab_kot_manag, NAME));

                if (NAME.toString().equals("Lite")) {
                    tablay.setVisibility(View.VISIBLE);
                }else {
                    tablay.setVisibility(View.GONE);
                }

            } while (cursor_tab_kot_manag.moveToNext());
        }
        cursor_tab_kot_manag.close();
        final int selectionCurrentt11111 = tab_kot_manag.getSelectedItemPosition();
        tab_kot_manag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                        Context.MODE_PRIVATE, null);
                ContentValues newValues = new ContentValues();
                newValues.put("status", String.valueOf(tab_kot_manag.getSelectedItem().toString()));
                String where = "_id = '1'";
                myDb.update("Table_kot", newValues, where, new String[]{});

                if (tab_kot_manag.getSelectedItem().toString().equals("Lite")) {
                    tablay.setVisibility(View.VISIBLE);
                }else {
                    tablay.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        auto_connect = (Spinner) rootview.findViewById(R.id.auto_connect);
        //db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor aallrowsws = db.rawQuery("SELECT * FROM Auto_Connect WHERE _id = '1'", null);
        if (aallrowsws.moveToFirst()) {
            do {
                String NAME = aallrowsws.getString(1);
                auto_connect.setSelection(getIndex(auto_connect, NAME));

            } while (aallrowsws.moveToNext());
        }
        aallrowsws.close();
        final int selectionCurrentt11 = auto_connect.getSelectedItemPosition();
        auto_connect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                        Context.MODE_PRIVATE, null);
                ContentValues newValues = new ContentValues();
                newValues.put("auto_connect_status", String.valueOf(auto_connect.getSelectedItem().toString()));
                String where = "_id = '1'";

                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Auto_Connect");
                getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
                resultUri = new Uri.Builder()
                        .scheme("content")
                        .authority(StubProviderApp.AUTHORITY)
                        .path("Auto_Connect")
                        .appendQueryParameter("operation", "update")
                        .appendQueryParameter("_id","1")
                        .build();
                getActivity().getContentResolver().notifyChange(resultUri, null);
//                myDb.update("Auto_Connect", newValues, where, new String[]{});
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        weighing_scale_onoff = (TextView) rootview.findViewById(R.id.weighing_scale_onoff);


        Cursor cursor1a = db.rawQuery("SELECT * FROM Weighing_Scale_status", null);
        if (cursor1a.moveToFirst()){
            String sta = cursor1a.getString(1);

            if (sta.toString().equals("Connected")) {
                weighing_scale_onoff.setText("Connected");
            }else {
                weighing_scale_onoff.setText("Not Connected");
            }
        }
        cursor1a.close();

        final TextView home_delivery_prints_status = (TextView) rootview.findViewById(R.id.home_delivery_prints_status);

        int i1 = 0;
        Cursor cursor = db.rawQuery("SELECT * FROM HomeDelivery_prints", null);
        if (cursor.moveToFirst()){
            String companycopy = cursor.getString(1);
            String customercopy =  cursor.getString(2);

            TextView tv = new TextView(getActivity());
            tv.setText(companycopy);
            TextView tv1 = new TextView(getActivity());
            tv1.setText(customercopy);
            if (tv.getText().toString().equals("yes")){
                i1++;
                home_delivery_prints_status.setText("Company copy");
            }
            if (tv1.getText().toString().equals("yes")){
                i1++;
                home_delivery_prints_status.setText("Customer copy");
            }
        }
        cursor.close();
        if (i1 == 0){
            home_delivery_prints_status.setText("no prints");
        }
        if (i1 == 2){
            home_delivery_prints_status.setText("2 prints");
        }

        RelativeLayout home_delivery_prints = (RelativeLayout)rootview.findViewById(R.id.home_delivery_prints);
        home_delivery_prints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog_credentials = new Dialog(getActivity(), R.style.timepicker_date_dialog);
                dialog_credentials.setContentView(R.layout.dialog_no_of_prints_homedelivery);
                dialog_credentials.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog_credentials.setCanceledOnTouchOutside(false);
                dialog_credentials.show();

                ImageButton btncancel = (ImageButton) dialog_credentials.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_credentials.dismiss();
                    }
                });

                final CheckBox company_copy= (CheckBox) dialog_credentials.findViewById(R.id.company_copy);
                final CheckBox customer_copy= (CheckBox) dialog_credentials.findViewById(R.id.customer_copy);

                Cursor cursor = db.rawQuery("SELECT * FROM HomeDelivery_prints", null);
                if (cursor.moveToFirst()){
                    String companycopy = cursor.getString(1);
                    String customercopy =  cursor.getString(2);

                    TextView tv = new TextView(getActivity());
                    tv.setText(companycopy);
                    TextView tv1 = new TextView(getActivity());
                    tv1.setText(customercopy);
                    if (tv.getText().toString().equals("yes")){
                        company_copy.setChecked(true);
                    }
                    if (tv1.getText().toString().equals("yes")){
                        customer_copy.setChecked(true);
                    }
                }
                cursor.close();

                Button btnsave = (Button) dialog_credentials.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int i2 = 0;
                        if (company_copy.isChecked()){
                            i2++;
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("companycopy", "yes");
                            String where = "_id = '1'";
                            db.update("HomeDelivery_prints", contentValues, where, new String[]{});
                            home_delivery_prints_status.setText("Company copy");
                        }else {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("companycopy", "");
                            String where = "_id = '1'";
                            db.update("HomeDelivery_prints", contentValues, where, new String[]{});
                        }

                        if (customer_copy.isChecked()){
                            i2++;
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("customercopy", "yes");
                            String where = "_id = '1'";
                            db.update("HomeDelivery_prints", contentValues, where, new String[]{});
                            home_delivery_prints_status.setText("Customer copy");
                        }else {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("customercopy", "");
                            String where = "_id = '1'";
                            db.update("HomeDelivery_prints", contentValues, where, new String[]{});
                        }

                        if (i2 == 0){
                            home_delivery_prints_status.setText("no prints");
                        }
                        if (i2 == 2){
                            home_delivery_prints_status.setText("2 prints");
                        }

                        dialog_credentials.dismiss();
                    }
                });



            }
        });

        RelativeLayout weighing_scale_settings = (RelativeLayout)rootview.findViewById(R.id.weighing_scale_settings);
        weighing_scale_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            permissions(),
                            1);
                   /* // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                1);

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                1);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }*/
                }else {
//                    Toast.makeText(ForgotPasswordActivity.this, "hiiii", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), Weighing_Scale_Configuration.class);
                    startActivityForResult(intent, SECOND_ACTIVITY_RESULT_CODE);
                }


            }
        });

        billingmode = (Spinner) rootview.findViewById(R.id.billing_spinner);
        final int selectionCurrenttt = billingmode.getSelectedItemPosition();

        //db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor allrows = db.rawQuery("SELECT * FROM BIllingmode WHERE _id = '1'", null);
        if (allrows.moveToFirst()) {
            do {
                NAme1 = allrows.getString(1);

            } while (allrows.moveToNext());
        }
        allrows.close();

        billingmode.setSelection(getIndex(billingmode, NAme1));
        billingmode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String billmode1 = parent.getItemAtPosition(position).toString();
//                if (selectionCurrenttt != position){
                //saveInDBbillingmode();
                SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                        Context.MODE_PRIVATE, null);
                //myDb.execSQL("delete from " + "BIllingmode");          // clearing the table
                ContentValues newValues = new ContentValues();
                newValues.put("billingtype", String.valueOf(billmode1));
                String where = "_id = '1'";
                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "BIllingmode");
                getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
                resultUri = new Uri.Builder()
                        .scheme("content")
                        .authority(StubProviderApp.AUTHORITY)
                        .path("BIllingmode")
                        .appendQueryParameter("operation", "update")
                        .appendQueryParameter("_id","1")
                        .build();
                getActivity().getContentResolver().notifyChange(resultUri, null);
//                myDb.update("BIllingmode", newValues, where, new String[]{});    myDb.close();
                ////Toast.makeText(parent.getContext(), "Billing mode is : \n" + parent.getItemAtPosition(position).toString(),
                //Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        billingtype = (Spinner) rootview.findViewById(R.id.billing_type_spinner);
        final int selectionCurrentttt = billingtype.getSelectedItemPosition();

        //db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor alllrows = db.rawQuery("SELECT * FROM BIllingtype WHERE _id = '1'", null);
        if (alllrows.moveToFirst()) {
            do {
                NAme11 = alllrows.getString(1);

            } while (alllrows.moveToNext());
        }
        alllrows.close();

        billingtype.setSelection(getIndex(billingtype, NAme11));
        billingtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String billmode1 = parent.getItemAtPosition(position).toString();
//                if (selectionCurrenttt != position){
                //saveInDBbillingmode();
                SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                        Context.MODE_PRIVATE, null);
                //myDb.execSQL("delete from " + "BIllingmode");          // clearing the table
                ContentValues newValues = new ContentValues();
                newValues.put("billingtype_type", String.valueOf(billmode1));
                String where = "_id = '1'";
                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "BIllingtype");
                getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
                resultUri = new Uri.Builder()
                        .scheme("content")
                        .authority(StubProviderApp.AUTHORITY)
                        .path("BIllingtype")
                        .appendQueryParameter("operation", "update")
                        .appendQueryParameter("_id","1")
                        .build();
                getActivity().getContentResolver().notifyChange(resultUri, null);
//                myDb.update("BIllingtype", newValues, where, new String[]{});
//                myDb.close();
                ////Toast.makeText(parent.getContext(), "Billing mode is : \n" + parent.getItemAtPosition(position).toString(),
                //Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        country_selection = (Spinner) rootview.findViewById(R.id.country_selection);
        final int selectionCurrenttt1 = country_selection.getSelectedItemPosition();

        //db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor allrows_c = db.rawQuery("SELECT * FROM Country_Selection WHERE _id = '1'", null);
        if (allrows_c.moveToFirst()) {
            do {
                country_sel = allrows_c.getString(1);

            } while (allrows_c.moveToNext());
        }
        allrows_c.close();

        if (country_sel.contains("Rupee")){
            country_selection.setSelection(0);
        }else {
            if (country_sel.contains("Pound")){
                country_selection.setSelection(1);
            }else {
                if (country_sel.contains("Euro")){
                    country_selection.setSelection(2);
                }else {
                    if (country_sel.contains("Dollar")){
                        country_selection.setSelection(3);
                    }else {
                        if (country_sel.contains("Dinars")){
                            country_selection.setSelection(4);
                        }else {
                            if (country_sel.contains("Shilling")) {
                                country_selection.setSelection(5);
                            }else {
                                if (country_sel.contains("Ringitt")) {
                                    country_selection.setSelection(6);
                                }else {
                                    if (country_sel.contains("Rial")) {
                                        country_selection.setSelection(7);
                                    }else {
                                        if (country_sel.contains("Yen")) {
                                            country_selection.setSelection(8);
                                        }else {
                                            if (country_sel.contains("Papua New Guinean")) {
                                                country_selection.setSelection(9);
                                            }else {
                                                if (country_sel.contains("UAE")) {
                                                    country_selection.setSelection(10);
                                                }else {
                                                    if (country_sel.contains("South African Rand")) {
                                                        country_selection.setSelection(11);
                                                    }else {
                                                        if (str_country.toString().equals("Congolese Franc")) {
                                                            country_selection.setSelection(12);
                                                        }else {
                                                            if (str_country.toString().equals("Qatari Riyals")) {
                                                                country_selection.setSelection(14);
                                                            }else {
                                                                if (str_country.toString().equals("Dirhams")) {
                                                                    country_selection.setSelection(13);
                                                                }else {
                                                                    if (str_country.toString().equals("Kuwait Dinar")) {
                                                                        country_selection.setSelection(15);
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
        }
//        country_selection.setSelection(getIndex(country_selection, country_sel));
        country_selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String billmode1 = parent.getItemAtPosition(position).toString();

                String match = " (";
                int coun_pos = billmode1.indexOf(match);
                String mod2 = billmode1.substring(0, coun_pos);//keep toastmessage

//                if (selectionCurrenttt != position){
                //saveInDBbillingmode();
                SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                        Context.MODE_PRIVATE, null);
                //myDb.execSQL("delete from " + "BIllingmode");          // clearing the table
                ContentValues newValues = new ContentValues();
                newValues.put("country", String.valueOf(mod2));
                String where = "_id = '1'";
//                myDb.update("Country_Selection", newValues, where, new String[]{});
//                myDb.close();

                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Country_Selection");
                getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
                resultUri = new Uri.Builder()
                        .scheme("content")
                        .authority(StubProviderApp.AUTHORITY)
                        .path("Country_Selection")
                        .appendQueryParameter("operation", "update")
                        .appendQueryParameter("_id","1")
                        .build();
                getActivity().getContentResolver().notifyChange(resultUri, null);

                ////Toast.makeText(parent.getContext(), "Billing mode is : \n" + parent.getItemAtPosition(position).toString(),
                //Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        printer_type = (Spinner) rootview.findViewById(R.id.printer_type);
        Cursor aallrows = db.rawQuery("SELECT * FROM Printer_type WHERE _id = '1'", null);
        if (aallrows.moveToFirst()) {
            do {
                NAAme1 = aallrows.getString(1);

            } while (aallrows.moveToNext());
        }
        aallrows.close();

        printer_type.setSelection(getIndex(printer_type, NAAme1));
        printer_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String billmode1 = parent.getItemAtPosition(position).toString();
//                if (selectionCurrenttt != position){
                //saveInDBbillingmode();
                SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                        Context.MODE_PRIVATE, null);
                //myDb.execSQL("delete from " + "BIllingmode");          // clearing the table
                ContentValues newValues = new ContentValues();
                newValues.put("printer_type", String.valueOf(billmode1));
                String where = "_id = '1'";
                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Printer_type");
                getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
                resultUri = new Uri.Builder()
                        .scheme("content")
                        .authority(StubProviderApp.AUTHORITY)
                        .path("Printer_type")
                        .appendQueryParameter("operation", "update")
                        .appendQueryParameter("_id","1")
                        .build();
                getActivity().getContentResolver().notifyChange(resultUri, null);
//                myDb.update("Printer_type", newValues, where, new String[]{});
                myDb.close();
                ////Toast.makeText(parent.getContext(), "Billing mode is : \n" + parent.getItemAtPosition(position).toString(),
                //Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

//CARD SWIPER
        card_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GlobalPreferenceActivity.this.getActivity(), Card_Wallets_Settings1.class);
                startActivity(i);

            }
        });
        //WALLET SETTING
        wallet_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(GlobalPreferenceActivity.this.getActivity(), Card_Wallets_Settings.class);
                startActivity(i);
            }
        });

        final TextView auto_generation_status3 = (TextView) rootview.findViewById(R.id.auto_generation_status3);


        Cursor cursor1 = db.rawQuery("SELECT * FROM Email_setup", null);
        if (cursor1.moveToFirst()){
            auto_generation_status3.setText("Set");
        }else {
            auto_generation_status3.setText("Not set");
        }
        cursor1.close();

        RelativeLayout credentials = (RelativeLayout)rootview.findViewById(R.id.credentials);
        credentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getResultsFromApi();
////////                                new MakeRequestTask1(mCredential).execute();
//////
//                new MakeRequestTask(mCredential).execute();


                final Dialog dialog_credentials = new Dialog(getActivity(), R.style.timepicker_date_dialog);
                dialog_credentials.setContentView(R.layout.dialog_email_confirm);
                dialog_credentials.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog_credentials.setCanceledOnTouchOutside(false);
                dialog_credentials.show();

                dialog_credentials.setOnCancelListener(new DialogInterface.OnCancelListener()
                {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Cursor cursor1 = db.rawQuery("SELECT * FROM Email_setup", null);
                        if (cursor1.moveToFirst()){
                            auto_generation_status3.setText("Set");
                        }else {
                            auto_generation_status3.setText("Not set");
                        }
                        cursor1.close();
                    }
                });

                ImageView btncancel = (ImageView) dialog_credentials.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_credentials.dismiss();
                        Cursor cursor1 = db.rawQuery("SELECT * FROM Email_setup", null);
                        if (cursor1.moveToFirst()){
                            auto_generation_status3.setText("Set");
                        }else {
                            auto_generation_status3.setText("Not set");
                        }
                        cursor1.close();
                    }
                });

                ImageView closetext = (ImageView) dialog_credentials.findViewById(R.id.closetext);
                closetext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_credentials.dismiss();
                        Cursor cursor1 = db.rawQuery("SELECT * FROM Email_setup", null);
                        if (cursor1.moveToFirst()){
                            auto_generation_status3.setText("Set");
                        }else {
                            auto_generation_status3.setText("Not set");
                        }
                        cursor1.close();
                    }
                });

                Button cancel = (Button) dialog_credentials.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_credentials.dismiss();

                        Cursor cursor1 = db.rawQuery("SELECT * FROM Email_setup", null);
                        if (cursor1.moveToFirst()){
                            auto_generation_status3.setText("Set");
                        }else {
                            auto_generation_status3.setText("Not set");
                        }
                        cursor1.close();
                    }
                });

                Button ok = (Button) dialog_credentials.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_credentials.dismiss();
                        Cursor cursor1 = db.rawQuery("SELECT * FROM Email_setup", null);
                        if (cursor1.moveToFirst()){
                            auto_generation_status3.setText("Set");
                        }else {
                            auto_generation_status3.setText("Not set");
                        }
                        cursor1.close();
                    }
                });

                Intent intent = new Intent(getActivity(), EmailSetup.class);
                startActivity(intent);


//                Cursor cursorr = db.rawQuery("SELECT * FROM Email_setup", null);
//                if (cursorr.moveToFirst()){
//                    String unn = cursorr.getString(1);
//                    Toast.makeText(getActivity(), "a4 "+unn, Toast.LENGTH_SHORT).show();
//
//                    TextView tvv = new TextView(getActivity());
//                    tvv.setText(unn);
//
//                    if (tvv.getText().toString().equals("")){
//                        Intent intent = new Intent(getActivity(), EmailSetup.class);
//                        startActivity(intent);
//                    }else {
//                        final Dialog dialog_credentials = new Dialog(getActivity(), R.style.timepicker_date_dialog);
//                        dialog_credentials.setContentView(R.layout.dialog_settings_email_setup);
//                        dialog_credentials.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                        dialog_credentials.show();
//
//
////                        final TextInputLayout username_layout = (TextInputLayout) dialog_credentials.findViewById(R.id.username_layout);
////                        final TextInputLayout password_layout = (TextInputLayout) dialog_credentials.findViewById(R.id.password_layout);
//
//                        final TextView username = (TextView) dialog_credentials.findViewById(R.id.username);
////                        final EditText password = (EditText) dialog_credentials.findViewById(R.id.password);
//
//                        final Spinner email_client = (Spinner) dialog_credentials.findViewById(R.id.email_client);
//                        email_client.setEnabled(false);
//                        email_client.setClickable(false);
//
//
//                        final Cursor cursor = db.rawQuery("SELECT * FROM Email_setup", null);
//                        if (cursor.moveToFirst()) {
//                            String un = cursor.getString(1);
//                            String pwd = cursor.getString(2);
//                            String em_ca = cursor.getString(3);
//                            username.setText(un);
////                            password.setText(pwd);
//                            TextView tv = new TextView(getActivity());
//                            tv.setText(em_ca);
//                            if (tv.getText().toString().equals("Gmail")) {
//                                email_client.setSelection(0);
//                            } else {
//                                if (tv.getText().toString().equals("Yahoo")) {
//                                    email_client.setSelection(1);
//                                } else {
//                                    if (tv.getText().toString().equals("Hotmail")) {
//                                        email_client.setSelection(2);
//                                    } else {
//                                        if (tv.getText().toString().equals("Outlook")) {
//                                            email_client.setSelection(3);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//
//                        ImageButton btndelete = (ImageButton) dialog_credentials.findViewById(R.id.btndelete);
//                        btndelete.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                final Dialog dialog_delete_confirmation = new Dialog(getActivity(), R.style.timepicker_date_dialog);
//                                dialog_delete_confirmation.setContentView(R.layout.dialog_email_setup_delete_confirmation);
//                                dialog_delete_confirmation.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                                dialog_delete_confirmation.show();
//
//                                ImageView closetext = (ImageView) dialog_delete_confirmation.findViewById(R.id.closetext);
//                                closetext.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog_delete_confirmation.dismiss();
//                                    }
//                                });
//
//                                Button cancel = (Button) dialog_delete_confirmation.findViewById(R.id.cancel);
//                                cancel.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog_delete_confirmation.dismiss();
//                                    }
//                                });
//
//                                Button ok = (Button) dialog_delete_confirmation.findViewById(R.id.ok);
//                                ok.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        auto_generation_status3.setText("Not Set");
//                                        String where = "username = '"+username.getText().toString()+"'";
//                                        db.delete("Email_setup", where, new String[]{});
//
//                                        dialog_delete_confirmation.dismiss();
//                                        dialog_credentials.dismiss();
//
//                                        Intent intent = new Intent(getActivity(), EmailSetup.class);
//                                        startActivity(intent);
//                                    }
//                                });
//
//                            }
//                        });
//
//                        ImageButton btncancel = (ImageButton) dialog_credentials.findViewById(R.id.btncancel);
//                        btncancel.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialog_credentials.dismiss();
//                            }
//                        });
//
//                        ImageButton btnsave = (ImageButton) dialog_credentials.findViewById(R.id.btnsave);
//                        btnsave.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialog_credentials.dismiss();
////                                getResultsFromApi();
//////                                new MakeRequestTask1(mCredential).execute();
////
////                                new MakeRequestTask(mCredential).execute();
//
////                                if (username.getText().toString().equals("")){
////                                    if (username.getText().toString().equals("")){
////                                        username_layout.setError("Enter username");
////                                    }
//////                                    if (password.getText().toString().equals("")){
//////                                        password_layout.setError("Enter password");
//////                                    }
////                                }else {
////                                    final Object position1 = email_client.getSelectedItem();
////                                    Cursor cursor2 = db.rawQuery("SELECT * FROM Email_setup", null);
////                                    if (cursor2.moveToFirst()){
////                                        do {
////                                            String id = cursor2.getString(0);
////                                            String where = "_id = '" + id + "' ";
////                                            db.delete("Email_setup", where, new String[]{});
////                                        }while (cursor2.moveToNext());
////                                    }
//////                            Toast.makeText(getActivity(), "entered", Toast.LENGTH_LONG).show();
////                                    ContentValues contentValues = new ContentValues();
////                                    contentValues.put("username", username.getText().toString());
////                                    contentValues.put("password", password.getText().toString());
////                                    contentValues.put("client", position1.toString());
////                                    db.insert("Email_setup", null, contentValues);
////                                    auto_generation_status3.setText("Set");
////                                    dialog_credentials.dismiss();
////                                }
//                            }
//                        });
//                    }
//                }else {
//                    Intent intent = new Intent(getActivity(), EmailSetup.class);
//                    startActivity(intent);
//                }

            }
        });



        sync_time_get = (TextView) rootview.findViewById(R.id.sync_time_get);

        Cursor cursorx = db.rawQuery("SELECT * FROM Sync_time", null);
        if (cursorx.moveToFirst()){
            String syn_tim = cursorx.getString(1);
            sync_time_get.setText("Last synced: "+syn_tim);
        }
        cursorx.close();

        ImageView sync_manual = (ImageView) rootview.findViewById(R.id.sync_manual);
        sync_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDeviceOnline() == true) {

                }
            }
        });

        Cursor cursora = db.rawQuery("SELECT * FROM Email_recipient", null);
        int gb = cursora.getCount();
        final TextView recipient_auto_generation_status3 = (TextView) rootview.findViewById(R.id.recipient_auto_generation_status3);
        recipient_auto_generation_status3.setText(String.valueOf(gb)+ " Recipients");

        RelativeLayout recipient_credentials = (RelativeLayout) rootview.findViewById(R.id.recipient_credentials);
        recipient_credentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog_credentials = new Dialog(getActivity(), R.style.timepicker_date_dialog);
                dialog_credentials.setContentView(R.layout.dialog_email_confirm);
                dialog_credentials.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog_credentials.setCanceledOnTouchOutside(false);
                dialog_credentials.show();

                dialog_credentials.setOnCancelListener(new DialogInterface.OnCancelListener()
                {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Cursor cursor1 = db.rawQuery("SELECT * FROM Email_setup", null);
                        if (cursor1.moveToFirst()){
                            auto_generation_status3.setText("Set");
                        }else {
                            auto_generation_status3.setText("Not set");
                        }
                        cursor1.close();
                    }
                });

                ImageView btncancel = (ImageView) dialog_credentials.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_credentials.dismiss();
                        Cursor cursora = db.rawQuery("SELECT * FROM Email_recipient", null);
                        int gb = cursora.getCount();
                        recipient_auto_generation_status3.setText(String.valueOf(gb)+ " Recipients");
                    }
                });

                ImageView closetext = (ImageView) dialog_credentials.findViewById(R.id.closetext);
                closetext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_credentials.dismiss();
                        Cursor cursora = db.rawQuery("SELECT * FROM Email_recipient", null);
                        int gb = cursora.getCount();
                        recipient_auto_generation_status3.setText(String.valueOf(gb)+ " Recipients");
                    }
                });

                Button cancel = (Button) dialog_credentials.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_credentials.dismiss();

                        Cursor cursora = db.rawQuery("SELECT * FROM Email_recipient", null);
                        int gb = cursora.getCount();
                        recipient_auto_generation_status3.setText(String.valueOf(gb)+ " Recipients");
                    }
                });

                Button ok = (Button) dialog_credentials.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_credentials.dismiss();
                        Cursor cursora = db.rawQuery("SELECT * FROM Email_recipient", null);
                        int gb = cursora.getCount();
                        recipient_auto_generation_status3.setText(String.valueOf(gb)+ " Recipients");
                    }
                });
                Intent intent = new Intent(getActivity(), EmailSetup_Recipients.class);
                startActivity(intent);

//                final Dialog dialog_recipient_credentials = new Dialog(getActivity(), R.style.timepicker_date_dialog);
//                dialog_recipient_credentials.setContentView(R.layout.dialog_settings_email_recipient);
//                dialog_recipient_credentials.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                dialog_recipient_credentials.show();
//
//                ImageButton btncancel = (ImageButton) dialog_recipient_credentials.findViewById(R.id.btncancel);
//                btncancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog_recipient_credentials.dismiss();
//                    }
//                });
//
//
//                final ListView listView = (ListView) dialog_recipient_credentials.findViewById(R.id.list);
//
//                final Cursor cursor = db.rawQuery("SELECT * FROM Email_recipient", null);
//                String[] fromFieldNames = {"name", "ph_no", "email"};
//                // the XML defined views which the data will be bound to
//                int[] toViewsID = {R.id.tv, R.id.tv1, R.id.tv2};
////                Log.e("Checamos que hay id", String.valueOf(R.id.name));
//                final SimpleCursorAdapter ddataAdapter = new SimpleCursorAdapter(getActivity(), R.layout.email_recipient_listview, cursor, fromFieldNames, toViewsID, 0);
//                listView.setAdapter(ddataAdapter);
//
//
//                final TextView no_reci = (TextView) dialog_recipient_credentials.findViewById(R.id.no_reci);
//                if (ddataAdapter.isEmpty()){
//                    no_reci.setVisibility(View.VISIBLE);
//                }else {
//                    no_reci.setVisibility(View.GONE);
//                }
//
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//
//                        Cursor cv = (Cursor) parent.getItemAtPosition(position);
//
//                        final String rec_id = cv.getString(cv.getColumnIndex("_id"));
//                        final String rec_name = cv.getString(cv.getColumnIndex("name"));
//                        final String rec_pho = cv.getString(cv.getColumnIndex("ph_no"));
//                        final String rec_email = cv.getString(cv.getColumnIndex("email"));
//
//                        final Dialog dialog_edit_recipient_credentials = new Dialog(getActivity(), R.style.timepicker_date_dialog);
//                        dialog_edit_recipient_credentials.setContentView(R.layout.dialog_settings_email_recipient_add);
//                        dialog_edit_recipient_credentials.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                        dialog_edit_recipient_credentials.show();
//
//
//                        LinearLayout qwer = (LinearLayout) dialog_edit_recipient_credentials.findViewById(R.id.qwer);
//                        qwer.setVisibility(View.VISIBLE);
//
//                        ImageButton btncancel = (ImageButton) dialog_edit_recipient_credentials.findViewById(R.id.btncancel);
//                        btncancel.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialog_edit_recipient_credentials.dismiss();
//                            }
//                        });
//
//                        Button delete = (Button) dialog_edit_recipient_credentials.findViewById(R.id.cancel);
//                        delete.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                final Dialog dialog_delete_recipient_credentials = new Dialog(getActivity(), R.style.timepicker_date_dialog);
//                                dialog_delete_recipient_credentials.setContentView(R.layout.dialog_settings_email_recipient_del_con);
//                                dialog_delete_recipient_credentials.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                                dialog_delete_recipient_credentials.show();
//
//                                ImageView closetext = (ImageView) dialog_delete_recipient_credentials.findViewById(R.id.closetext);
//                                closetext.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog_delete_recipient_credentials.dismiss();
//                                    }
//                                });
//
//                                Button cancel = (Button) dialog_delete_recipient_credentials.findViewById(R.id.cancel);
//                                cancel.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog_delete_recipient_credentials.dismiss();
//                                    }
//                                });
//
//                                Button delete = (Button) dialog_delete_recipient_credentials.findViewById(R.id.ok);
//                                delete.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        String where = "_id = '"+rec_id+"'";
//                                        db.delete("Email_recipient", where, new String[]{});
//                                        dialog_delete_recipient_credentials.dismiss();
//                                        dialog_edit_recipient_credentials.dismiss();
//
//                                        final Cursor cursor = db.rawQuery("SELECT * FROM Email_recipient", null);
//                                        String[] fromFieldNames = {"name", "ph_no", "email"};
//                                        // the XML defined views which the data will be bound to
//                                        int[] toViewsID = {R.id.tv, R.id.tv1, R.id.tv2};
////                              Log.e("Checamos que hay id", String.valueOf(R.id.name));
//                                        final SimpleCursorAdapter ddataAdapter = new SimpleCursorAdapter(getActivity(), R.layout.email_recipient_listview, cursor, fromFieldNames, toViewsID, 0);
//                                        listView.setAdapter(ddataAdapter);
//
//                                        cursor.moveToPosition(position);
//                                        cursor.requery();
//                                        ddataAdapter.notifyDataSetChanged();
//
//
//                                        if (ddataAdapter.isEmpty()){
//                                            no_reci.setVisibility(View.VISIBLE);
//                                        }else {
//                                            no_reci.setVisibility(View.GONE);
//                                        }
//
//
//                                        Cursor cursora = db.rawQuery("SELECT * FROM Email_recipient", null);
//                                        int gb = cursora.getCount();
//                                        recipient_auto_generation_status3.setText(String.valueOf(gb)+ " Recipients");
//
//                                    }
//                                });
//
//
//                            }
//                        });
//
//                        final TextInputLayout name_layout = (TextInputLayout) dialog_edit_recipient_credentials.findViewById(R.id.name_layout);
//                        TextInputLayout ph_no_layout = (TextInputLayout) dialog_edit_recipient_credentials.findViewById(R.id.ph_no_layout);
//                        final TextInputLayout email_layout = (TextInputLayout) dialog_edit_recipient_credentials.findViewById(R.id.email_layout);
//
//                        final EditText editText_name = (EditText) dialog_edit_recipient_credentials.findViewById(R.id.editText_name);
//                        final EditText editText_ph_no = (EditText) dialog_edit_recipient_credentials.findViewById(R.id.editText_ph_no);
//                        final EditText editText_email = (EditText) dialog_edit_recipient_credentials.findViewById(R.id.editText_email);
//
//
//                        editText_name.setText(rec_name);
//                        editText_ph_no.setText(rec_pho);
//                        editText_email.setText(rec_email);
//
//                        ImageButton btnsave = (ImageButton) dialog_edit_recipient_credentials.findViewById(R.id.btnsave);
//                        btnsave.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//
//
//                                if (editText_name.getText().toString().equals("") || editText_email.getText().toString().equals("")){
//                                    if (editText_name.getText().toString().equals("")){
//                                        name_layout.setError("Enter valid name");
//                                    }
//                                    if (editText_email.getText().toString().equals("")){
//                                        email_layout.setError("Enter valid email");
//                                    }
//                                }else {
//                                    ContentValues contentValues = new ContentValues();
//                                    contentValues.put("name", editText_name.getText().toString());
//                                    contentValues.put("ph_no", editText_ph_no.getText().toString());
//                                    contentValues.put("email", editText_email.getText().toString());
//                                    String where = "_id = '"+rec_id+"'";
//                                    db.update("Email_recipient", contentValues, where, new String[]{});
//                                    dialog_edit_recipient_credentials.dismiss();
//
//
//                                    final Cursor cursor = db.rawQuery("SELECT * FROM Email_recipient", null);
//                                    String[] fromFieldNames = {"name", "ph_no", "email"};
//                                    // the XML defined views which the data will be bound to
//                                    int[] toViewsID = {R.id.tv, R.id.tv1, R.id.tv2};
////                              Log.e("Checamos que hay id", String.valueOf(R.id.name));
//                                    final SimpleCursorAdapter ddataAdapter = new SimpleCursorAdapter(getActivity(), R.layout.email_recipient_listview, cursor, fromFieldNames, toViewsID, 0);
//                                    listView.setAdapter(ddataAdapter);
//
//                                    cursor.moveToPosition(position);
//                                    cursor.requery();
//                                    ddataAdapter.notifyDataSetChanged();
//
//
//                                    if (ddataAdapter.isEmpty()){
//                                        no_reci.setVisibility(View.VISIBLE);
//                                    }else {
//                                        no_reci.setVisibility(View.GONE);
//                                    }
//
//                                    Cursor cursora = db.rawQuery("SELECT * FROM Email_recipient", null);
//                                    int gb = cursora.getCount();
//                                    recipient_auto_generation_status3.setText(String.valueOf(gb)+ " Recipients");
//                                }
//                            }
//                        });
//
//                        editText_name.addTextChangedListener(new TextWatcher() {
//                            @Override
//                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                            }
//
//                            @Override
//                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                                name_layout.setError(null);
//                            }
//
//                            @Override
//                            public void afterTextChanged(Editable editable) {
//
//                            }
//                        });
//
//                        editText_email.addTextChangedListener(new TextWatcher() {
//                            @Override
//                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                            }
//
//                            @Override
//                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                                email_layout.setError(null);
//                            }
//
//                            @Override
//                            public void afterTextChanged(Editable editable) {
//
//                            }
//                        });
//
//                    }
//                });
//
//
//                ImageButton btnadd = (ImageButton) dialog_recipient_credentials.findViewById(R.id.btnadd);
//                btnadd.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        final Dialog dialog_add_recipient_credentials = new Dialog(getActivity(), R.style.timepicker_date_dialog);
//                        dialog_add_recipient_credentials.setContentView(R.layout.dialog_settings_email_recipient_add);
//                        dialog_add_recipient_credentials.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                        dialog_add_recipient_credentials.show();
//
//
//                        LinearLayout qwer = (LinearLayout) dialog_add_recipient_credentials.findViewById(R.id.qwer);
//                        qwer.setVisibility(View.GONE);
//
//                        ImageButton btncancel = (ImageButton) dialog_add_recipient_credentials.findViewById(R.id.btncancel);
//                        btncancel.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialog_add_recipient_credentials.dismiss();
//                            }
//                        });
//
//
//                        final TextInputLayout name_layout = (TextInputLayout) dialog_add_recipient_credentials.findViewById(R.id.name_layout);
//                        TextInputLayout ph_no_layout = (TextInputLayout) dialog_add_recipient_credentials.findViewById(R.id.ph_no_layout);
//                        final TextInputLayout email_layout = (TextInputLayout) dialog_add_recipient_credentials.findViewById(R.id.email_layout);
//
//                        final EditText editText_name = (EditText) dialog_add_recipient_credentials.findViewById(R.id.editText_name);
//                        final EditText editText_ph_no = (EditText) dialog_add_recipient_credentials.findViewById(R.id.editText_ph_no);
//                        final EditText editText_email = (EditText) dialog_add_recipient_credentials.findViewById(R.id.editText_email);
//
//                        ImageButton btnsave = (ImageButton) dialog_add_recipient_credentials.findViewById(R.id.btnsave);
//                        btnsave.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (editText_name.getText().toString().equals("") || editText_email.getText().toString().equals("")){
//                                    if (editText_name.getText().toString().equals("")){
//                                        name_layout.setError("Enter valid name");
//                                    }
//                                    if (editText_email.getText().toString().equals("")){
//                                        email_layout.setError("Enter valid email");
//                                    }
//                                }else {
//                                    ContentValues contentValues = new ContentValues();
//                                    contentValues.put("name", editText_name.getText().toString());
//                                    contentValues.put("ph_no", editText_ph_no.getText().toString());
//                                    contentValues.put("email", editText_email.getText().toString());
//                                    db.insert("Email_recipient", null, contentValues);
//                                    dialog_add_recipient_credentials.dismiss();
//
//
//                                    final Cursor cursor = db.rawQuery("SELECT * FROM Email_recipient", null);
//                                    String[] fromFieldNames = {"name", "ph_no", "email"};
//                                    // the XML defined views which the data will be bound to
//                                    int[] toViewsID = {R.id.tv, R.id.tv1, R.id.tv2};
////                              Log.e("Checamos que hay id", String.valueOf(R.id.name));
//                                    final SimpleCursorAdapter ddataAdapter = new SimpleCursorAdapter(getActivity(), R.layout.email_recipient_listview, cursor, fromFieldNames, toViewsID, 0);
//                                    listView.setAdapter(ddataAdapter);
//
////                                cursor.moveToPosition(position);
////                                cursor.requery();
////                                ddataAdapter.notifyDataSetChanged();
//
//                                    if (ddataAdapter.isEmpty()){
//                                        no_reci.setVisibility(View.VISIBLE);
//                                    }else {
//                                        no_reci.setVisibility(View.GONE);
//                                    }
//
//                                    Cursor cursora = db.rawQuery("SELECT * FROM Email_recipient", null);
//                                    int gb = cursora.getCount();
//                                    recipient_auto_generation_status3.setText(String.valueOf(gb)+ " Recipients");
//                                }
//                            }
//                        });
//
//                        editText_name.addTextChangedListener(new TextWatcher() {
//                            @Override
//                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                            }
//
//                            @Override
//                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                                name_layout.setError(null);
//                            }
//
//                            @Override
//                            public void afterTextChanged(Editable editable) {
//
//                            }
//                        });
//
//                        editText_email.addTextChangedListener(new TextWatcher() {
//                            @Override
//                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                            }
//
//                            @Override
//                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                                email_layout.setError(null);
//                            }
//
//                            @Override
//                            public void afterTextChanged(Editable editable) {
//
//                            }
//                        });
//
//                    }
//                });
            }
        });

        RelativeLayout signout = (RelativeLayout) rootview.findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.title20))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db_inapp.close();
                                try {

                                    SharedPreferences preferences = Stock_Transfer_Processing.getDefaultSharedPreferencesMultiProcess(getContext());
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear();
                                    editor.apply();

                                    SharedPreferences preferences1 = SplashScreenActivity.getDefaultSharedPreferencesMultiProcess(getContext());
                                    SharedPreferences.Editor editor1 = preferences1.edit();
                                    editor1.clear();
                                    editor1.apply();

                                    R_NewLogIn_BusinessType.storeList.clear();
                                    MainActivity_Signin_OTPbased.storeList.clear();


                                    if(getActivity().deleteDatabase("amazoninapp")){
                                        if(getActivity().deleteDatabase("syncdb")){
                                            if(getActivity().deleteDatabase("syncdbapp")){

                                                progressDialog=new ProgressDialog(getActivity());
                                                progressDialog.setMessage("loading");
                                                progressDialog.show();
                                                new DeleteSalesData().execute();

                                            }else{
                                                Toast.makeText(getActivity(),"Please try again",Toast.LENGTH_LONG).show();
                                            }
                                        }else{
                                            Toast.makeText(getActivity(),"Please try again",Toast.LENGTH_LONG).show();
                                        }
                                    }else{
                                        Toast.makeText(getActivity(),"Please try again",Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();








            }
        });


        RelativeLayout inventory = (RelativeLayout) rootview.findViewById(R.id.inventory);

        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.title21))
                        .setMessage(getString(R.string.setmessage25))

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                dialog.dismiss();
                                syncinventory();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                dialog.dismiss();
                            }
                        })

                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });


        MyServiceAppData_local.setOnProgressChangedListener(this);

        prog=0;
        progSales=0;
        appdataBool=false;
        salesdataBool=false;

        action = (LinearLayout) rootview.findViewById(R.id.action);
        action1 = (LinearLayout) rootview.findViewById(R.id.action1);

        updateBar= rootview.findViewById(R.id.updatebar);
        updateBar.setMax(320);
        progressBar_license = (CardView) rootview.findViewById(R.id.progressbar1);
        tv_perc=rootview.findViewById(R.id.tv_perc);

        RelativeLayout inventory_local = (RelativeLayout) rootview.findViewById(R.id.inventory_local);

        inventory_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.title21))
                        .setMessage(getString(R.string.setmessage25_1))

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                dialog.dismiss();
                                syncinventory_local();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                dialog.dismiss();
                            }
                        })

                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });




        final TextView day1 = (TextView) rootview.findViewById(R.id.day1);
        final TextView time1 = (TextView) rootview.findViewById(R.id.time1);
        final TextView day2 = (TextView) rootview.findViewById(R.id.day2);
        final TextView time2 = (TextView) rootview.findViewById(R.id.time2);

        Cursor working_hours_get = db.rawQuery("SELECT * FROM Working_hours", null);
        if (working_hours_get.moveToFirst()){
            String day1get = working_hours_get.getString(1);
            String time1get = working_hours_get.getString(2);
            String day2get = working_hours_get.getString(3);
            String time2get = working_hours_get.getString(4);

            day1.setText(day1get);
            time1.setText(time1get);
            day2.setText(day2get);
            time2.setText(time2get);
        }
        working_hours_get.close();

        RelativeLayout working_hours = (RelativeLayout) rootview.findViewById(R.id.working_hours);
        working_hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog_working_hours = new Dialog(getActivity(), R.style.timepicker_date_dialog);
                dialog_working_hours.setContentView(R.layout.dialog_settings_working_hours);
                dialog_working_hours.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog_working_hours.show();

                ImageButton btncancel = (ImageButton) dialog_working_hours.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_working_hours.dismiss();
                    }
                });

                final Spinner spinner = (Spinner) dialog_working_hours.findViewById(R.id.opening_day);
                final Spinner spinner1 = (Spinner) dialog_working_hours.findViewById(R.id.closing_day);

                textView = (TextView) dialog_working_hours.findViewById(R.id.opening_time);
                textView1 = (TextView) dialog_working_hours.findViewById(R.id.closing_time);


                Cursor working_hours_get = db.rawQuery("SELECT * FROM Working_hours", null);
                if (working_hours_get.moveToFirst()){
                    String day1get = working_hours_get.getString(1);
                    String time1get = working_hours_get.getString(2);
                    String day2get = working_hours_get.getString(3);
                    String time2get = working_hours_get.getString(4);

                    spinner.setSelection(0);
                    textView.setText(time1get);
                    if (day2get.toString().equals("Today")) {
                        spinner1.setSelection(0);
                    }else {
                        spinner1.setSelection(1);
                    }
                    textView1.setText(time2get);
                }
                working_hours_get.close();

                ImageButton btnsave = (ImageButton) dialog_working_hours.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        Toast.makeText(getActivity(), "opening "+spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getActivity(), "closing "+spinner1.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                        Cursor cursor = db.rawQuery("SELECT * FROM Working_hours", null);
                        if (cursor.moveToFirst()) {
                            String id = cursor.getString(0);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("opening", spinner.getSelectedItem().toString());
                            contentValues.put("opening_time", textView.getText().toString());
                            contentValues.put("closing", spinner1.getSelectedItem().toString());
                            contentValues.put("closing_time", textView1.getText().toString());
                            contentValues.put("opening_time_system", editText11_time_hide.getText().toString());
                            contentValues.put("closing_time_system", editText1_time_hide.getText().toString());
                            String where = "_id = '"+id+"'";

                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Working_hours");
                            getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Working_hours")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id", id)
                                    .build();
                            getActivity().getContentResolver().notifyChange(resultUri, null);

//                            db.update("Working_hours", contentValues, where, new String[]{});
                        }
                        cursor.close();


                        day1.setText(spinner.getSelectedItem().toString());
                        time1.setText(textView.getText().toString());
                        if (spinner1.getSelectedItem().toString().equals("Today")) {
                            day2.setText("Today");
                        }else {
                            day2.setText("Tomorrow");
                        }
                        time2.setText(textView1.getText().toString());

                        dialog_working_hours.dismiss();
                    }
                });


                ImageButton restore = (ImageButton) dialog_working_hours.findViewById(R.id.btnrestore);
                restore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(getString(R.string.title19));
                        builder.setMessage("\n"+getString(R.string.setmessage27)+"\n");
                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Cursor cursor = db.rawQuery("SELECT * FROM Working_hours", null);
                                if (cursor.moveToFirst()) {
                                    String id1 = cursor.getString(0);
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("opening", "Today");
                                    contentValues.put("opening_time", "12:01 AM");
                                    contentValues.put("closing", "Today");
                                    contentValues.put("closing_time", "11:59 PM");
                                    contentValues.put("opening_time_system", "00:01");
                                    contentValues.put("closing_time_system", "23:59");
                                    String where = "_id = '"+id1+"'";

                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Working_hours");
                                    getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                                    resultUri = new Uri.Builder()
                                            .scheme("content")
                                            .authority(StubProviderApp.AUTHORITY)
                                            .path("Working_hours")
                                            .appendQueryParameter("operation", "update")
                                            .appendQueryParameter("_id", id1)
                                            .build();
                                    getActivity().getContentResolver().notifyChange(resultUri, null);

//                                    db.update("Working_hours", contentValues, where, new String[]{});
                                }
                                cursor.close();
                                dialog.cancel();
                                dialog_working_hours.dismiss();

                                day1.setText("Today");
                                time1.setText("12:01 AM");
                                day2.setText("Today");
                                time2.setText("11:59 PM");

                            }
                        });
                        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                        builder.show();




                    }
                });

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(getActivity(), R.style.timepicker_date_dialog, timePickerListener_open, hour, minute,
                                false);

                        timePickerDialog.show();
                    }
                });

                textView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(getActivity(), R.style.timepicker_date_dialog, timePickerListener_close, hour, minute,
                                false);

                        timePickerDialog.show();
                    }
                });
            }
        });


        try {

            //db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
            entervalue(rootview);

        } catch (SQLiteException e) {
            alertas("Error inesperado: " + e.getMessage());
        }

        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.language = view.findViewById(R.id.languageTextView);

        String selectedLanguage = LocaleUtils.getSelectedLanguage(requireActivity());
        if (selectedLanguage != null) {
            language.setText(selectedLanguage);
        } else {
            language.setText(getString(R.string.english));
        }

        RelativeLayout languageLayout = view.findViewById(R.id.language_setting_relative_layout);
        languageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguage();
            }
        });

    }

    private void changeLanguage() {
//        final String[] languages = {"English", "Arabic", "Hindi"};
        final String[] languages = {"English", "Arabic"};
        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(requireActivity());
        mBuilder.setTitle("Choose Language");
        mBuilder.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    language.setText(languages[0]);
                    LocaleUtils.setSelectedLanguageId(LanguageConstant.ENGLISH, languages[0], requireActivity());
                    getActivity().recreate();
                } else if (which == 1) {
                    language.setText(languages[1]);
                    LocaleUtils.setSelectedLanguageId(LanguageConstant.ARABIC, languages[1], requireActivity());
                    getActivity().recreate();
                }
/*                if (which == 2) {
                    language.setText(languages[2]);
                    LocaleUtils.setSelectedLanguageId(LanguageConstant.HINDI, languages[2]);
                    getActivity().recreate();
                }*/
            }
        });
        mBuilder.create();
        mBuilder.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        TextView r2= (TextView) getActivity().findViewById(R.id.r2);
        TextView r1= (TextView) getActivity().findViewById(R.id.r1);
        String activated="";
        Cursor mswipe = db.rawQuery("SELECT * FROM CardSwiperActivation WHERE _id=2", null);
        if (mswipe.moveToFirst()) {
            activated= mswipe.getString(mswipe.getColumnIndex("Config_status"));

        }

        Cursor cursor = db.rawQuery("SELECT * FROM RazorpayMerchantReg WHERE account = 'Registered'", null);
        if (cursor.moveToFirst()) {
            r1.setText("Razorpay");
        }else {
            Cursor cursor1 = db.rawQuery("SELECT * FROM PaytmMerchantReg WHERE Account = 'Registered'", null);
            if (cursor1.moveToFirst()) {
                r1.setText("Paytm");
            }
            cursor1.close();
        }
        cursor.close();


        if(activated.equalsIgnoreCase("Activated")){
            Log.e("activted","activated1");
        }else{
            Log.e("activted","activated3");
            Cursor pinelabs = db.rawQuery("SELECT * FROM CardSwiperActivation WHERE _id=3", null);
            if (pinelabs.moveToFirst()) {
                activated= pinelabs.getString(pinelabs.getColumnIndex("Config_status"));
                if(activated.equalsIgnoreCase("Activated")){
                    r2.setText("Pine Labs");
                    r1.setText("Pine Labs");
                    Log.e("activted","activated2");
                }else{


                    Cursor paytm = db.rawQuery("SELECT * FROM CardSwiperActivation WHERE _id=5", null);
                    if (paytm.moveToFirst()) {
                        activated= paytm.getString(pinelabs.getColumnIndex("Config_status"));
                        if(activated.equalsIgnoreCase("Activated")){
                            r2.setText("Paytm");
                            Log.e("activted","activated2");
                        }else{

                        }
                    }



                }
            }
        }

//        registerReceiver(receiver, new IntentFilter(
//                "com.intuition.ivepos.GlobalPreferenceActivity.receiver"));

        getActivity().registerReceiver(receiverapp, new IntentFilter(
                "com.intuition.ivepos.GlobalPreferenceActivity.receiverapp"));

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("downloadpref", Context.MODE_PRIVATE);
        if(sharedpreferences.contains("download")){

            String download = sharedpreferences.getString("download", null);
            if(download.equalsIgnoreCase("complete")){

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("download", "");
                editor.commit();
                new syncTaskApp().execute();

            }
//            String downloadsales = sharedpreferences.getString("downloadsales", null);
//            if(downloadsales.equalsIgnoreCase("complete")){
//
//                SharedPreferences.Editor editor = sharedpreferences.edit();
//                editor.putString("downloadsales", "");
//                editor.commit();
//                new Checking_Store.syncTask().execute();
//
//            }


        }else{
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("download", "");
            editor.commit();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
//        unregisterReceiver(receiver);
        getActivity().unregisterReceiver(receiverapp);
    }


    public void syncinventory() {

        SharedPreferences sharedpreferences=getDefaultSharedPreferencesMultiProcess(getActivity());
        String company= sharedpreferences.getString("companyname", null);
        String store= sharedpreferences.getString("storename", null);
        String device= sharedpreferences.getString("devicename", null);

        JSONObject params = new JSONObject();

        try {
            params.put("device",device);
            params.put("store",store);
            params.put("company",company);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  if(queue==null){
        queue = Volley.newRequestQueue(getActivity());
        // }

        statusDialog = new ProgressDialog(getActivity());
        statusDialog.setMessage(getString(R.string.setmessage23));
        statusDialog.setIndeterminate(false);
        statusDialog.setCancelable(false);
        statusDialog.show();

        JsonObjectRequest sr = new JsonObjectRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl+"getlastidsapp.php",params,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responseString) {

                        Log.e("responseString",responseString.toString());
                        String response= "";
                        JSONObject jsonObject=null;
                        try {
                            jsonObject=responseString;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            response = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(response.equalsIgnoreCase("success")){

                            try {

                                items=jsonObject.getString("items");
                                taxes=jsonObject.getString("taxes");
                                hotel=jsonObject.getString("hotel");
                                discount=jsonObject.getString("discount");

                                Log.e("items",items);
                                Log.e("taxes",taxes);
                                Log.e("hotel",hotel);
                                Log.e("discount",discount);

                                callsyncadapter(items,taxes,hotel,discount);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }else{
                            statusDialog.dismiss();
                            Toast.makeText(getActivity(), "sync failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        statusDialog.dismiss();
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                })  {

        };
/*    sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        sr.setRetryPolicy(new DefaultRetryPolicy(0,-1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);

    }

    public void callsyncadapter(String items,String taxes,String hotel,String discount) {
        SQLiteDatabase syncdb= getActivity().openOrCreateDatabase("syncdbapp", Context.MODE_PRIVATE,null);

        Cursor cursor=syncdb.rawQuery("select * from appdata",null);
        if(cursor.moveToFirst()) {
            do {
                final int lastsynced = cursor.getInt(2);
                final int id = cursor.getInt(0);
                final String table = cursor.getString(1);

                Log.e(table,lastsynced+"");


            }while (cursor.moveToNext());
        }





        ContentValues contentValues = new ContentValues();
        contentValues.put("lastsyncedid", Integer.parseInt(items));
        String wherecu = "tablename = '" + "Items" + "'";
        int update1 =syncdb.update("appdata", contentValues, wherecu, new String[]{});

        Log.e("upadate1",update1+"");



        contentValues = new ContentValues();
        contentValues.put("lastsyncedid", Integer.parseInt(taxes));
        wherecu = "tablename = '" + "Taxes" + "'";
        update1 =syncdb.update("appdata", contentValues, wherecu, new String[]{});

        Log.e("upadate1",update1+"");

        Bundle extras = new Bundle();
        extras.putString("table","Taxes");

        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL,true);
        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
        ContentResolver.requestSync(null, AUTHORITY, extras);

        contentValues = new ContentValues();
        contentValues.put("lastsyncedid", Integer.parseInt(hotel));
        wherecu = "tablename = '" + "Hotel" + "'";
        update1 =syncdb.update("appdata", contentValues, wherecu, new String[]{});

        Log.e("upadate1",update1+"");

        extras = new Bundle();
        extras.putString("table","Hotel");

        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL,true);
        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
        ContentResolver.requestSync(null, AUTHORITY, extras);


        contentValues = new ContentValues();
        contentValues.put("lastsyncedid", Integer.parseInt(discount));
        wherecu = "tablename = '" + "Discount_details" + "'";
        update1 = syncdb.update("appdata", contentValues, wherecu, new String[]{});

        Log.e("upadate1",update1+"");

        extras = new Bundle();
        extras.putString("table","Discount_details");

        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL,true);
        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
        ContentResolver.requestSync(null, AUTHORITY, extras);



        extras = new Bundle();
        extras.putString("table","Items");

        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL,true);
        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
        ContentResolver.requestSync(null, AUTHORITY, extras);

        statusDialog.dismiss();

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.

        inflater.inflate(R.menu.general_pref, menu);

//        MenuItem item = menu.findItem(R.id.action_billsettings);
//        item.setTitle(Html.fromHtml("<font color='#ffffff'>Enter the password</font>"));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_billsettings:
                frag = new DevicePreferenceActivity();
                hideKeyboard(getContext());
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
                break;

//            case R.id.action_speeddial:
//                frag = new QuickAccessActivity();
//                hideKeyboard(getContext());
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//                break;


            case R.id.action_smartinvent:
                frag = new InventoryActivity();
                hideKeyboard(getContext());
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
                break;

//            case R.id.action_network:
//                frag = new NetworkPreferenceActivity();
//                hideKeyboard(getContext());
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private int getIndex(Spinner spinner, String myString) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }

    public void alertas(String alerta) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme);
        AlertDialog.Builder builder = new AlertDialog.Builder(wrapper);
        builder.setIcon(R.drawable.icon);
        builder.setTitle(R.string.app_name);
        builder.setMessage(alerta);
        builder.create().show();
    }

    public void crearYasignar(SQLiteDatabase dbllega) {
        try {

            dbllega.execSQL("CREATE TABLE if not exists Totaltables (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, nooftables text);");
            dbllega.execSQL("CREATE TABLE if not exists Quickedit (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, quickedittype text);");
            dbllega.execSQL("CREATE TABLE if not exists Stockcontrol (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, stockcontroltype text);");
            dbllega.execSQL("CREATE TABLE if not exists BIllingmode (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, billingtype text);");
            db = dbllega;

        } catch (SQLiteException e) {
            alertas("Error desconocido: " + e.getMessage());
        }
    }

    void saveInDB() {
        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                Context.MODE_PRIVATE, null);
        // myDb.execSQL("delete from " + "Totaltables");          // clearing the table
        ContentValues newValues = new ContentValues();
        newValues.put("nooftables", text.getText().toString());
        String where = "_id = '1'";
        myDb.update("Totaltables", newValues, where, new String[]{});
        myDb.close();
    }

    void saveInDBedit() {
        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                Context.MODE_PRIVATE, null);
        //myDb.execSQL("delete from " + "Quickedit");          // clearing the table
        ContentValues newValues = new ContentValues();
        newValues.put("quickedittype", String.valueOf(spinnerquickedit.getSelectedItem().toString()));
        String where = "_id = '1'";

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Quickedit");
        getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("Quickedit")
                .appendQueryParameter("operation", "update")
                .appendQueryParameter("_id", "1")
                .build();
        getActivity().getContentResolver().notifyChange(resultUri, null);
//        myDb.update("Quickedit", newValues, where, new String[]{});
        myDb.close();
    }


    void saveInDBbillingmode() {
        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                Context.MODE_PRIVATE, null);
        //myDb.execSQL("delete from " + "BIllingmode");          // clearing the table
        ContentValues newValues = new ContentValues();
        newValues.put("billingtype", String.valueOf(billingmode.getSelectedItem().toString()));
        String where = "_id = '1'";

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "BIllingmode");
        getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("BIllingmode")
                .appendQueryParameter("operation", "update")
                .appendQueryParameter("_id", "1")
                .build();
        getActivity().getContentResolver().notifyChange(resultUri, null);
//        myDb.update("BIllingmode", newValues, where, new String[]{});
        myDb.close();
    }


    public void entervalue(final View root) {

        final SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                Context.MODE_PRIVATE, null);

        //db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT _id FROM asd1", null);
//                if (cursor.moveToFirst()) {
//                    do {
//                        NAME = cursor.getString(1);
//
//
//                    } while (cursor.moveToNext());
//                }
        int countt = cursor.getCount();
        countt1 = countt;
        final String countt2 = String.valueOf(countt1);
        //////Toast.makeText(getActivity(), "there are " + countt2 + " tables", Toast.LENGTH_SHORT).show();


        //db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        cursor = db.rawQuery("SELECT * FROM Totaltables", null);
        if (cursor.moveToFirst()) {
            do {
                NAME = cursor.getString(1);


            } while (cursor.moveToNext());
        }
//        int counttt = cursor.getCount();
        final int counttt1 = Integer.parseInt(NAME);
        final String counttt2 = String.valueOf(counttt1);
        //////Toast.makeText(getActivity(), "there are " + counttt2 + " tables", Toast.LENGTH_SHORT).show();

        text = (EditText) root.findViewById(R.id.editText4);

        text.setText(String.valueOf(countt2));

//        text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog dialogz = new Dialog(getActivity(), R.style.notitle);
//                dialogz.setContentView(R.layout.dialog_table_value);
//                dialogz.show();
//
//                final EditText tablev = (EditText)dialogz.findViewById(R.id.numoftables);
//                tablev.setText(String.valueOf(countt2));
//
//                Button close = (Button)dialogz.findViewById(R.id.closedialog);
//                close.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialogz.dismiss();
//                        hideKeyboard(getContext());
//                    }
//                });
//
//
//                final Button save = (Button)dialogz.findViewById(R.id.go);
//
//                tablev.setOnEditorActionListener(new EditText.OnEditorActionListener() {
//                    @Override
//                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                        if (actionId == EditorInfo.IME_ACTION_GO) {
//                            save.performClick();
//                            return true;
//                        }
//                        return false;
//                    }
//                });
//
//
//                save.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (tablev.getText().toString().equals("0") || tablev.getText().toString().equals("") || tablev.getText().toString().equals("00")) {
//                            tablev.setError("Enter valid number");
//                        }else {
//                            text.setText(tablev.getText().toString());
//                            dialogz.dismiss();
//                            hideKeyboard(getContext());
//                        }
//
//                    }
//                });
//            }
//        });


        save_image = (Button) root.findViewById(R.id.savetables);
        save_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (text.getText().toString().equals("0") || text.getText().toString().equals("") || text.getText().toString().equals("00")) {
                    text.setError("Enter valid number");
                }else {
                    ImageView imageView1 = (ImageView) getActivity().findViewById(R.id.tick);
                    imageView1.setVisibility(View.INVISIBLE);
                    if (Float.parseFloat(text.getText().toString()) >= 100) {
                        text.setError("Enter lessthan 99");
                        ImageView imageView = (ImageView) getActivity().findViewById(R.id.tick);
                        imageView.setVisibility(View.INVISIBLE);
                    } else {
                        if (text.getText().toString().equals("0") || text.getText().toString().equals("") || text.getText().toString().equals("00")) {
//                    ////Toast.makeText(getActivity(), "There must be atleast one table",
//                            Toast.LENGTH_SHORT).show();
                            text.setError("Enter valid number");
                            ImageView imageView = (ImageView) getActivity().findViewById(R.id.tick);
                            imageView.setVisibility(View.INVISIBLE);
                        } else {
                            saveInDB();
                            hideKeyboard(getContext());

                            Cursor cursor = db.rawQuery("SELECT _id FROM asd1", null);
//                if (cursor.moveToFirst()) {
//                    do {
//                        NAME = cursor.getString(1);
//
//
//                    } while (cursor.moveToNext());
//                }
                            int countt = cursor.getCount();
                            countt1 = countt;

                            //////Toast.makeText(getActivity(), "Total tables is: " + text.getText().toString(),
                            //        Toast.LENGTH_SHORT).show();

                            if (Integer.parseInt(text.getText().toString()) > countt1) {

                                DownloadMusicfromInternet1 downloadMusicfromInternet = new DownloadMusicfromInternet1();
                                downloadMusicfromInternet.execute(text.getText().toString());


                            } else {


                                int neww = countt1 - Integer.parseInt(text.getText().toString());
                                ////Toast.makeText(getActivity(), "Tables Savedddddddddd  "+neww, Toast.LENGTH_SHORT).show();
//                        Cursor cursor = db.rawQuery("SELECT _id FROM asd1", null);
////                if (cursor.moveToFirst()) {
////                    do {
////                        NAME = cursor.getString(1);
////
////
////                    } while (cursor.moveToNext());
////                }
//                        int count = cursor.getCount();
//                        int count1 = count+1;
//                        String count2 = String.valueOf(count1);
////                db.delete("Items", "_id"+"="+count2, null);
//                        for (int i = countt1 ; i>Integer.parseInt(text.getText().toString()) ; i--) {
//                            ////Toast.makeText(getActivity(), " deleting "+i, Toast.LENGTH_SHORT).show();
//                        }
                                dialog = new Dialog(getActivity(), R.style.notitle);
                                dialog.setContentView(R.layout.dialog_delete_tables_warning);
                                dialog.show();

                                Button cancel = (Button) dialog.findViewById(R.id.cancel);
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ImageView imageView = (ImageView) getActivity().findViewById(R.id.tick);
                                        imageView.setVisibility(View.INVISIBLE);
                                        dialog.dismiss();
                                    }
                                });

                                ImageView cancel1 = (ImageView) dialog.findViewById(R.id.closetext);
                                cancel1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ImageView imageView = (ImageView) getActivity().findViewById(R.id.tick);
                                        imageView.setVisibility(View.INVISIBLE);
                                        dialog.dismiss();
                                    }
                                });

                                Button okk = (Button) dialog.findViewById(R.id.ok);
                                //final int finalI = i;
                                okk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ImageView imageView = (ImageView) getActivity().findViewById(R.id.tick);
                                        imageView.setVisibility(View.VISIBLE);


                                        DownloadMusicfromInternet downloadMusicfromInternet = new DownloadMusicfromInternet();
                                        downloadMusicfromInternet.execute(text.getText().toString());

                                    }
                                });


                            }
                        }
                    }
                }
            }
        });

        text_bill_id_tag = (EditText) root.findViewById(R.id.editText4_bill_id_tag);

        Cursor cursor1 = db.rawQuery("SELECT * FROM Billcount_tag", null);
        if (cursor1.moveToFirst()){
            String tag = cursor1.getString(1);
            text_bill_id_tag.setText(tag);
        }
        cursor1.close();

        save_bill_id_tag = (Button) root.findViewById(R.id.save_bill_id_tag);
        save_bill_id_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor cursor2 = db.rawQuery("SELECT * FROM Billcount_tag", null);
                if (cursor2.moveToFirst()){
                    String id = cursor2.getString(0);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("tag_name", text_bill_id_tag.getText().toString());
                    String where = "_id = '"+id+"'";
//                        db.update("Billcount_tag", contentValues, where, new String[]{});

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Billcount_tag");
                    getActivity().getContentResolver().update(contentUri, contentValues,where,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Billcount_tag")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id", id)
                            .build();
                    getActivity().getContentResolver().notifyChange(resultUri, null);

                    ImageView imageView1 = (ImageView) getActivity().findViewById(R.id.bill_id_tick);
                    imageView1.setVisibility(View.VISIBLE);

                }else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("tag_name", text_bill_id_tag.getText().toString());
//                        db.insert("Billcount_tag", null, contentValues);

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Billcount_tag");
                    resultUri = getActivity().getContentResolver().insert(contentUri, contentValues);
                    getActivity().getContentResolver().notifyChange(resultUri, null);

                    ImageView imageView1 = (ImageView) getActivity().findViewById(R.id.bill_id_tick);
                    imageView1.setVisibility(View.VISIBLE);

                }
                cursor2.close();

                hideKeyboard(getContext());

            }
        });

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


    void updateinventory(Integer _id) {
        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                Context.MODE_PRIVATE, null);
        ContentValues newValues = new ContentValues();
        newValues.put("stockquan", item_content10);
        String where = "_id = ?";

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
        getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("Items")
                .appendQueryParameter("operation", "update")
                .appendQueryParameter("_id", Integer.toString(_id))
                .build();
        getActivity().getContentResolver().notifyChange(resultUri, null);
//        db.update("Items", newValues, where, new String[]{Integer.toString(_id)});
        String where1_v1 = "docid = ?";
        db.update("Items_Virtual", newValues, where1_v1, new String[]{});


        myDb.close();

    }

    void updateinventory1(Integer _id) {
        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                Context.MODE_PRIVATE, null);
        ContentValues newValues = new ContentValues();
        newValues.put("modstockquan", item_content10);
        String where = "_id = ?";
        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Modifiers");
        getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("Modifiers")
                .appendQueryParameter("operation", "update")
                .appendQueryParameter("_id", Integer.toString(_id))
                .build();
        getActivity().getContentResolver().notifyChange(resultUri, null);

//        db.update("Modifiers", newValues, where, new String[]{Integer.toString(_id)});
        myDb.close();

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    class DownloadMusicfromInternet1 extends AsyncTask<String, Void, Integer> {
        private ProgressDialog dialog6 = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);

        @Override
        protected Integer doInBackground(String... params) {
            int neww = Integer.parseInt(text.getText().toString()) - countt1;
            ////Toast.makeText(getActivity(), "Table Saved" , Toast.LENGTH_SHORT).show();
            for (int i = 1; i <= neww; i++) {
                //db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                Cursor cursorr = db.rawQuery("SELECT _id FROM asd1", null);
//                if (cursor.moveToFirst()) {
//                    do {
//                        NAME = cursor.getString(1);
//
//
//                    } while (cursor.moveToNext());
//                }
                int count = cursorr.getCount();
                countt1 = count + 1;
                String count2 = String.valueOf(countt1);
//                            ////Toast.makeText(getActivity(), "adding "+ count2 +" items" , Toast.LENGTH_SHORT).show();
                SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                        Context.MODE_PRIVATE, null);
                byte[] byteImage1;
                byte[] img;
                String s = myDb.getPath();
                ContentValues newValues = new ContentValues();
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.c_table_empty_normal_6d6e71);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.PNG, 100, bos);
                img = bos.toByteArray();
                newValues.put("image", img);
                newValues.put("_id", count2);
                newValues.put("pName", "");
                newValues.put("pDate", count2);
                myDb.insert("asd1", null, newValues);
//                            displaylistview();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);

            dialog6.setMessage("Loading");
            dialog6.setCanceledOnTouchOutside(false);
            dialog6.setCancelable(false);
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
            dialog6.show();
        }

        @Override
        protected void onPostExecute(Integer file_url) {
            dialog6.dismiss();
            ImageView imageView1 = (ImageView) getActivity().findViewById(R.id.tick);
            imageView1.setVisibility(View.VISIBLE);
        }
    }

    class DownloadMusicfromInternet extends AsyncTask<String, Void, Integer> {

        private ProgressDialog dialog5 = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);

        @Override
        protected Integer doInBackground(String... params) {

            for (int i = countt1; i > Integer.parseInt(text.getText().toString()); i--) {
                //db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                Cursor cursor = db.rawQuery("SELECT _id FROM asd1", null);
                int count = cursor.getCount();
                int count1 = count;
                final String count2 = String.valueOf(count1);

                Cursor cursor1 = db.rawQuery("SELECT * FROM asd1 ORDER BY _id DESC", null);
                if (cursor1.moveToFirst()) {
                    //qwe = cursor1.getString(2);
                    qwee = cursor1.getInt(2);
                }
                cursor1.close();
                ////Toast.makeText(getActivity(), "removing table"+qwee, Toast.LENGTH_SHORT).show();
                Cursor rr = db1.rawQuery("Select * from Table" + qwee + "", null);
                if (rr.moveToFirst()) {


                    SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                            Context.MODE_PRIVATE, null);

                    //myDb.delete("asd1", "pDate" + "=" + i, null);

                    String wherre = "pDate = '" + qwee + "' ";
                    myDb.delete("asd1", wherre, new String[]{});


//                                        db1 = getActivity().openOrCreateDatabase("mydb_Salesdata",
//                                                Context.MODE_PRIVATE, null);
                    Cursor cursorre = db1.rawQuery("Select * from Table" + qwee + "", null);
                    if (cursorre.moveToFirst()) {
                        ////Toast.makeText(getActivity(), "deleting table is " + i, Toast.LENGTH_SHORT).show();
                        do {
                            Itemtype = cursorre.getString(5);

                            if (Itemtype.toString().equals("Item")) {
                                itemquan = cursorre.getString(1);
                                hii = cursorre.getString(2);
                                idd = cursorre.getString(7);
                                modass = cursorre.getString(8);


                                //db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                                Cursor resetmode = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + hii + "'", null);
                                if (resetmode.moveToFirst()) {
                                    String NAME = resetmode.getString(3);
                                    aaaaa = Float.parseFloat(NAME);
                                    String aaid = resetmode.getString(0);
                                    aid = Integer.parseInt(aaid);

                                }
                                //db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                                resetmode = db1.rawQuery("Select * from Table" + qwee + " WHERE itemname = '" + hii + "'", null);
                                if (resetmode.moveToFirst()) {
                                    idd = resetmode.getString(0);
                                    ////Toast.makeText(getActivity(), "name is " + hii + " quan is " + itemquan, Toast.LENGTH_SHORT).show();
                                }

                                Cursor modcursor = db1.rawQuery("Select * from Table" + qwee + " WHERE parent = '" + hii + "' AND parentid = '" + idd + "'  ", null);

                                while (modcursor.moveToNext()) {
                                    modinameee = modcursor.getString(2);
                                    nummm = modcursor.getString(1);
                                    ////Toast.makeText(getActivity(), "name is " + modinameee + " quan is " + nummm, Toast.LENGTH_SHORT).show();

                                    Cursor zxcv = db.rawQuery("Select * from Modifiers WHERE modifiername = '" + modinameee + "' ", null);
                                    if (zxcv.moveToFirst()) {
                                        String aa = zxcv.getString(3);
                                        a = Float.parseFloat(aa);
                                        String bb = zxcv.getString(0);
                                        b = Integer.parseInt(bb);
                                        item_content10 = a + Float.parseFloat(nummm);

                                        updateinventory1(b);
                                    }
                                    zxcv.close();
                                }
                                modcursor.close();

                                item_content10 = aaaaa + Float.parseFloat(itemquan);
                                updateinventory(aid);

                                //////Toast.makeText(getActivity().this, "Id of '"+hii+"' is "+idd+" quan is "+aaaaa, Toast.LENGTH_SHORT).show();


                                //row2.removeAllViews();

                                String where = "_id = '" + idd + "' ";
                                db1.delete("Table" + qwee, where, new String[]{});

                                String where1 = "parentid = '" + idd + "' ";
                                db1.delete("Table" + qwee, where1, new String[]{});

                                ////Toast.makeText(getActivity(), "deleting "+ i, Toast.LENGTH_SHORT).show();

                            }
                        } while (cursorre.moveToNext());

                    }
                    cursorre.close();
                    dialog.dismiss();

                    //dialog.show();
                } else {
                    SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                            Context.MODE_PRIVATE, null);
                    //myDb.delete("asd1", "pDate" + "=" + i, null);

                    String wherre = "pDate = '" + qwee + "' ";
                    myDb.delete("asd1", wherre, new String[]{});

                    //db1 = getActivity().openOrCreateDatabase("mydb_Salesdata",
                    //Context.MODE_PRIVATE, null);
                    Cursor cursorre = db1.rawQuery("Select * from Table" + qwee + "", null);
                    while (cursorre.moveToNext()) {

                        Itemtype = cursorre.getString(5);

                        if (Itemtype.toString().equals("Item")) {
                            itemquan = cursorre.getString(1);
                            hii = cursorre.getString(2);
                            idd = cursorre.getString(7);
                            modass = cursorre.getString(8);


                            //db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                            Cursor resetmode = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + hii + "'", null);
                            if (resetmode.moveToFirst()) {
                                String NAME = resetmode.getString(3);
                                aaaaa = Float.parseFloat(NAME);
                                String aaid = resetmode.getString(0);
                                aid = Integer.parseInt(aaid);

                            }
                            //db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                            resetmode = db1.rawQuery("Select * from Table" + qwee + " WHERE itemname = '" + hii + "'", null);
                            if (resetmode.moveToFirst()) {
                                idd = resetmode.getString(0);
                                ////Toast.makeText(getActivity(), "name is " + hii + " quan is " + itemquan, Toast.LENGTH_SHORT).show();
                            }

                            Cursor modcursor = db1.rawQuery("Select * from Table" + qwee + " WHERE parent = '" + hii + "' AND parentid = '" + idd + "'  ", null);

                            while (modcursor.moveToNext()) {
                                modinameee = modcursor.getString(2);
                                nummm = modcursor.getString(1);
                                ////Toast.makeText(getActivity(), "name is " + modinameee + " quan is " + nummm, Toast.LENGTH_SHORT).show();

                                Cursor zxcv = db.rawQuery("Select * from Modifiers WHERE modifiername = '" + modinameee + "' ", null);
                                if (zxcv.moveToFirst()) {
                                    String aa = zxcv.getString(3);
                                    a = Float.parseFloat(aa);
                                    String bb = zxcv.getString(0);
                                    b = Integer.parseInt(bb);
                                    item_content10 = a + Float.parseFloat(nummm);

                                    updateinventory1(b);
                                }
                                zxcv.close();
                            }
                            modcursor.close();

                            item_content10 = aaaaa + Float.parseFloat(itemquan);
                            updateinventory(aid);

                            //////Toast.makeText(getActivity().this, "Id of '"+hii+"' is "+idd+" quan is "+aaaaa, Toast.LENGTH_SHORT).show();


                            //row2.removeAllViews();

                            String where = "_id = '" + idd + "' ";
                            db1.delete("Table" + qwee, where, new String[]{});

                            String where1 = "parentid = '" + idd + "' ";
                            db1.delete("Table" + qwee, where1, new String[]{});

                            ////Toast.makeText(getActivity(), "deletinggggg "+i, Toast.LENGTH_SHORT).show();

                        }
                    }
                    cursorre.close();
                }
                rr.close();


//                            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata",
//                                    Context.MODE_PRIVATE, null);
//                            Cursor cursorre = db1.rawQuery("Select * from Table" + count2 + "", null);
//                            while (cursorre.moveToNext()) {
//
//                                Itemtype = cursorre.getString(5);
//
//                                if (Itemtype.toString().equals("Item")) {
//                                    itemquan = cursorre.getString(1);
//                                    itemname = cursorre.getString(2);
//                                    parent = cursorre.getString(6);
//                                    parentid = cursorre.getString(7);
//                                    modass = cursorre.getString(8);
//
//
//                                    db =   getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//                                    Cursor resetmode = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + itemname + "'", null);
//                                    if (resetmode.moveToFirst()) {
//                                        String NAME = resetmode.getString(3);
//                                        aaaaa = Integer.parseInt(NAME);
//                                        String aaid = resetmode.getString(0);
//                                        aid = Integer.parseInt(aaid);
//
//                                    }
//
//
//
//                                    Cursor modcursor = db1.rawQuery("Select * from Table" + count2 + " WHERE parent = '" + parent + "' AND parentid = '" + parentid + "'  ", null);
//
//                                    while (modcursor.moveToNext()) {
//                                        modinameee = modcursor.getString(2);
//                                        modiquan = modcursor.getString(1);
//                                        ////Toast.makeText(getActivity(), "name is " + modinameee + " quan is " + modiquan, Toast.LENGTH_SHORT).show();
//
//                                        Cursor zxcv = db.rawQuery("Select * from Modifiers WHERE modifiername = '" + modinameee + "' ", null);
//                                        if (zxcv.moveToFirst()) {
//                                            String aa = zxcv.getString(3);
//                                            a = Integer.parseInt(aa);
//                                            String bb = zxcv.getString(0);
//                                            b = Integer.parseInt(bb);
//                                            item_content10 = a + Integer.parseInt(modiquan);
//
//                                            updateinventory1(b);
//                                        }
//                                    }
//
//                                }
//                            }
            }

//            for (int i = countt1 ; i>Integer.parseInt(text.getText().toString()) ; i--) {
//                //db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//                Cursor cursor = db.rawQuery("SELECT _id FROM asd1", null);
//                int count = cursor.getCount();
//                int count1 = count;
//                final String count2 = String.valueOf(count1);
//
//                Cursor cursor1 = db.rawQuery("SELECT * FROM asd1 ORDER BY pDate DESC LIMIT 1", null);
//                if (cursor1.moveToFirst()){
//                    qwe = cursor1.getString(2);
//                }
//
//                Cursor rr = db1.rawQuery("Select * from Table" + qwe + "", null);
//                if (rr.moveToFirst()){
//
//
//                    //Toast.makeText(getActivity(), "removing table"+qwe, Toast.LENGTH_SHORT).show();
//
//
//
//                    SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
//                            Context.MODE_PRIVATE, null);
//
//                    //myDb.delete("asd1", "pDate" + "=" + i, null);
//
//                    String wherre = "pDate = '" + qwe + "' ";
//                    myDb.delete("asd1", wherre, new String[]{});
//
//
////                                        db1 = getActivity().openOrCreateDatabase("mydb_Salesdata",
////                                                Context.MODE_PRIVATE, null);
//                    Cursor cursorre = db1.rawQuery("Select * from Table" + qwe + "", null);
//                    if (cursorre.moveToFirst()) {
//                        ////Toast.makeText(getActivity(), "deleting table is " + i, Toast.LENGTH_SHORT).show();
//                        do {
//                            Itemtype = cursorre.getString(5);
//
//                            if (Itemtype.toString().equals("Item")) {
//                                itemquan = cursorre.getString(1);
//                                hii = cursorre.getString(2);
//                                idd = cursorre.getString(7);
//                                modass = cursorre.getString(8);
//
//
//                                //db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//                                Cursor resetmode = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + hii + "'", null);
//                                if (resetmode.moveToFirst()) {
//                                    String NAME = resetmode.getString(3);
//                                    aaaaa = Integer.parseInt(NAME);
//                                    String aaid = resetmode.getString(0);
//                                    aid = Integer.parseInt(aaid);
//
//                                }
//                                //db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
//                                resetmode = db1.rawQuery("Select * from Table" + qwe + " WHERE itemname = '" + hii + "'", null);
//                                if (resetmode.moveToFirst()) {
//                                    idd = resetmode.getString(0);
//                                    ////Toast.makeText(getActivity(), "name is " + hii + " quan is " + itemquan, Toast.LENGTH_SHORT).show();
//                                }
//
//                                Cursor modcursor = db1.rawQuery("Select * from Table" + qwe + " WHERE parent = '" + hii + "' AND parentid = '" + idd + "'  ", null);
//
//                                while (modcursor.moveToNext()) {
//                                    modinameee = modcursor.getString(2);
//                                    nummm = modcursor.getString(1);
//                                    ////Toast.makeText(getActivity(), "name is " + modinameee + " quan is " + nummm, Toast.LENGTH_SHORT).show();
//
//                                    Cursor zxcv = db.rawQuery("Select * from Modifiers WHERE modifiername = '" + modinameee + "' ", null);
//                                    if (zxcv.moveToFirst()) {
//                                        String aa = zxcv.getString(3);
//                                        a = Integer.parseInt(aa);
//                                        String bb = zxcv.getString(0);
//                                        b = Integer.parseInt(bb);
//                                        item_content10 = a + Integer.parseInt(nummm);
//
//                                        updateinventory1(b);
//                                    }
//                                }
//
//                                item_content10 = aaaaa + Integer.parseInt(itemquan);
//                                updateinventory(aid);
//
//                                //////Toast.makeText(getActivity().this, "Id of '"+hii+"' is "+idd+" quan is "+aaaaa, Toast.LENGTH_SHORT).show();
//
//
//                                //row2.removeAllViews();
//
//                                String where = "_id = '" + idd + "' ";
//                                db1.delete("Table" + qwe, where, new String[]{});
//
//                                String where1 = "parentid = '" + idd + "' ";
//                                db1.delete("Table" + qwe, where1, new String[]{});
//
//                                ////Toast.makeText(getActivity(), "deleting "+ i, Toast.LENGTH_SHORT).show();
//
//                            }
//                        }while (cursorre.moveToNext());
//
//                    }
//                    dialog.dismiss();
//
//                    //dialog.show();
//                }else {
//                    SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
//                            Context.MODE_PRIVATE, null);
//                    //myDb.delete("asd1", "pDate" + "=" + i, null);
//
//                    String wherre = "pDate = '" + qwe + "' ";
//                    myDb.delete("asd1", wherre, new String[]{});
//
//                    //db1 = getActivity().openOrCreateDatabase("mydb_Salesdata",
//                    //Context.MODE_PRIVATE, null);
//                    Cursor cursorre = db1.rawQuery("Select * from Table" + qwe + "", null);
//                    while (cursorre.moveToNext()) {
//
//                        Itemtype = cursorre.getString(5);
//
//                        if (Itemtype.toString().equals("Item")) {
//                            itemquan = cursorre.getString(1);
//                            hii = cursorre.getString(2);
//                            idd = cursorre.getString(7);
//                            modass = cursorre.getString(8);
//
//
//                            //db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//                            Cursor resetmode = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + hii + "'", null);
//                            if (resetmode.moveToFirst()) {
//                                String NAME = resetmode.getString(3);
//                                aaaaa = Integer.parseInt(NAME);
//                                String aaid = resetmode.getString(0);
//                                aid = Integer.parseInt(aaid);
//
//                            }
//                            //db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
//                            resetmode = db1.rawQuery("Select * from Table" + qwe + " WHERE itemname = '" + hii + "'", null);
//                            if (resetmode.moveToFirst()) {
//                                idd = resetmode.getString(0);
//                                ////Toast.makeText(getActivity(), "name is " + hii + " quan is " + itemquan, Toast.LENGTH_SHORT).show();
//                            }
//
//                            Cursor modcursor = db1.rawQuery("Select * from Table" + qwe + " WHERE parent = '" + hii + "' AND parentid = '" + idd + "'  ", null);
//
//                            while (modcursor.moveToNext()) {
//                                modinameee = modcursor.getString(2);
//                                nummm = modcursor.getString(1);
//                                ////Toast.makeText(getActivity(), "name is " + modinameee + " quan is " + nummm, Toast.LENGTH_SHORT).show();
//
//                                Cursor zxcv = db.rawQuery("Select * from Modifiers WHERE modifiername = '" + modinameee + "' ", null);
//                                if (zxcv.moveToFirst()) {
//                                    String aa = zxcv.getString(3);
//                                    a = Integer.parseInt(aa);
//                                    String bb = zxcv.getString(0);
//                                    b = Integer.parseInt(bb);
//                                    item_content10 = a + Integer.parseInt(nummm);
//
//                                    updateinventory1(b);
//                                }
//                            }
//
//                            item_content10 = aaaaa + Integer.parseInt(itemquan);
//                            updateinventory(aid);
//
//                            //////Toast.makeText(getActivity().this, "Id of '"+hii+"' is "+idd+" quan is "+aaaaa, Toast.LENGTH_SHORT).show();
//
//
//                            //row2.removeAllViews();
//
//                            String where = "_id = '" + idd + "' ";
//                            db1.delete("Table" + qwe, where, new String[]{});
//
//                            String where1 = "parentid = '" + idd + "' ";
//                            db1.delete("Table" + qwe, where1, new String[]{});
//
//                            ////Toast.makeText(getActivity(), "deletinggggg "+i, Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                }
//
//
//
//
//
//
//
//
////                            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata",
////                                    Context.MODE_PRIVATE, null);
////                            Cursor cursorre = db1.rawQuery("Select * from Table" + count2 + "", null);
////                            while (cursorre.moveToNext()) {
////
////                                Itemtype = cursorre.getString(5);
////
////                                if (Itemtype.toString().equals("Item")) {
////                                    itemquan = cursorre.getString(1);
////                                    itemname = cursorre.getString(2);
////                                    parent = cursorre.getString(6);
////                                    parentid = cursorre.getString(7);
////                                    modass = cursorre.getString(8);
////
////
////                                    db =   getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
////                                    Cursor resetmode = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + itemname + "'", null);
////                                    if (resetmode.moveToFirst()) {
////                                        String NAME = resetmode.getString(3);
////                                        aaaaa = Integer.parseInt(NAME);
////                                        String aaid = resetmode.getString(0);
////                                        aid = Integer.parseInt(aaid);
////
////                                    }
////
////
////
////                                    Cursor modcursor = db1.rawQuery("Select * from Table" + count2 + " WHERE parent = '" + parent + "' AND parentid = '" + parentid + "'  ", null);
////
////                                    while (modcursor.moveToNext()) {
////                                        modinameee = modcursor.getString(2);
////                                        modiquan = modcursor.getString(1);
////                                        ////Toast.makeText(getActivity(), "name is " + modinameee + " quan is " + modiquan, Toast.LENGTH_SHORT).show();
////
////                                        Cursor zxcv = db.rawQuery("Select * from Modifiers WHERE modifiername = '" + modinameee + "' ", null);
////                                        if (zxcv.moveToFirst()) {
////                                            String aa = zxcv.getString(3);
////                                            a = Integer.parseInt(aa);
////                                            String bb = zxcv.getString(0);
////                                            b = Integer.parseInt(bb);
////                                            item_content10 = a + Integer.parseInt(modiquan);
////
////                                            updateinventory1(b);
////                                        }
////                                    }
////
////                                }
////                            }
//            }
            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);

            dialog5.setMessage("Loading");
            dialog5.setCanceledOnTouchOutside(false);
            dialog5.setCancelable(false);
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
            dialog5.show();
        }

        @Override
        protected void onPostExecute(Integer file_url) {
            dialog5.dismiss();
            dialog.dismiss();
        }

    }


    class DownloadMusicfromInternet11 extends AsyncTask<String, Void, Integer> {

        private ProgressDialog dialog5 = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);

        @Override
        protected Integer doInBackground(String... params) {

            Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
            if (cursor.moveToFirst()){
                do {
                    String item_id = cursor.getString(0);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("barcode_value", "");
                    String where1 = "_id = '" + item_id + "' ";

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                    getActivity().getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Items")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id",item_id)
                            .build();
                    getActivity().getContentResolver().notifyChange(resultUri, null);
                    String where1_v1 = "docid = '" + item_id + "' ";
                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                }while (cursor.moveToNext());
            }
            cursor.close();

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog5.setMessage("Loading");
            dialog5.setCanceledOnTouchOutside(false);
            dialog5.setCancelable(false);

            dialog5.show();
        }

        @Override
        protected void onPostExecute(Integer file_url) {
            dialog5.dismiss();
        }

    }

    class DownloadMusicfromInternet111 extends AsyncTask<String, Void, Integer> {

        private ProgressDialog dialog5 = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);

        @Override
        protected Integer doInBackground(String... params) {

            Cursor cursor = db.rawQuery("SELECT * FROM Items", null);
            if (cursor.moveToFirst()){
                do {
                    String item_id = cursor.getString(0);
//                                                Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE barcode_value = '"+item_id+"'", null);
//                                                if (cursor1.moveToFirst()){
//                                                    do {
////                                                        int count = cursor1.getCount();
//                                                        ContentValues contentValues = new ContentValues();
//                                                        contentValues.put("barcode_value", item_id+"_1");
//                                                        String where1 = "_id = '" + item_id + "' ";
//                                                        db.update("Items", contentValues, where1, new String[]{});
//                                                    }while (cursor1.moveToNext());
//                                                }else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("barcode_value", item_id);
                    String where1 = "_id = '" + item_id + "' ";

                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                    getActivity().getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("Items")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id",item_id)
                            .build();
                    getActivity().getContentResolver().notifyChange(resultUri, null);
//                    db.update("Items", contentValues, where1, new String[]{});
                    String where1_v1 = "docid = '" + item_id + "' ";
                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                }while (cursor.moveToNext());
            }
            cursor.close();

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog5.setMessage("Loading");
            dialog5.setCanceledOnTouchOutside(false);
            dialog5.setCancelable(false);

            dialog5.show();
        }

        @Override
        protected void onPostExecute(Integer file_url) {
            dialog5.dismiss();
        }

    }

    class DownloadMusicfromInternet1111 extends AsyncTask<String, Void, Integer> {

        private ProgressDialog dialog5 = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);

        @Override
        protected Integer doInBackground(String... params) {

            TextView tv = new TextView(getActivity());
            Cursor cursor1 = db.rawQuery("SELECT * FROM Items", null);
            if (cursor1.moveToFirst()){
                do {
                    String item_id = cursor1.getString(0);
                    String bc = cursor1.getString(16);
                    tv.setText(bc);
                    if (tv.getText().toString().equals("")) {

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("barcode_value", item_id);
                        String where1 = "_id = '" + item_id + "' ";
                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                        getActivity().getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("Items")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id",item_id)
                                .build();
                        getActivity().getContentResolver().notifyChange(resultUri, null);
//                        db.update("Items", contentValues, where1, new String[]{});
                        String where1_v1 = "docid = '" + item_id + "' ";
                        db.update("Items_Virtual", contentValues, where1_v1, new String[]{});


                    }
                }while (cursor1.moveToNext());
            }
            cursor1.close();


            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog5.setMessage("Loading");
            dialog5.setCanceledOnTouchOutside(false);
            dialog5.setCancelable(false);

            dialog5.show();
        }

        @Override
        protected void onPostExecute(Integer file_url) {
            dialog5.dismiss();
        }

    }

    public static void donotshowKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    private android.app.TimePickerDialog.OnTimeSetListener timePickerListener = new android.app.TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime(hour, minute);

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minutes);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

//            final Cursor one = db.rawQuery("SELECT * FROM Alaramdays where _id ='1' ;", null);
//            if (one.moveToFirst()){
//                String onee = one.getString(1);
//                if (onee.toString().equals("Monday")){
//                    if(calSet.compareTo(calNow) <= 0){
//                        //Today Set time passed, count to tomorrow
//                        calSet.add(Calendar.DATE, 1);
//
//                        //Toast.makeText(getActivity(), " monday", Toast.LENGTH_SHORT).show();
//                    }
            setAlarm(calSet);

            //Toast.makeText(getActivity(), " monday is selected ", Toast.LENGTH_SHORT).show();
//                }else {
//                    //Toast.makeText(getActivity(), " monday is not selected ", Toast.LENGTH_SHORT).show();
//                }
//            }

        }
    };

    private void updateTime(int hours, int mins) {

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

        timeset.setText(aTime);
    }


    private void setAlarm(Calendar targetCal){
        Intent intent = new Intent(getActivity(), AlarmReceiver2.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), RQS_1, intent, 0);
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(getActivity(), RQS_1, intent, PendingIntent.FLAG_MUTABLE);
        }
        else {
            pendingIntent = PendingIntent.getBroadcast(getActivity(), RQS_1, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
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

            editText11_time_hide.setText(hour1 + ":" + minutes1);


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

            editText1_time_hide.setText(hour1 + ":" + minutes1);
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

        textView.setText(aTime);
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

        textView1.setText(aTime);
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

    private class MakeRequestTask1 extends AsyncTask<Void, Void, List<String>> {
        private Gmail mService = null;
        private Exception mLastError = null;

        MakeRequestTask1(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            System.out.println("labels mservice11 " + mService);

            mService = new Gmail.Builder(
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
        private Gmail mService = null;
        private Exception mLastError = null;
//        private View view = sendFabButton;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new Gmail.Builder(
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

            Cursor cursor1 = db.rawQuery("SELECT * FROM Email_recipient", null);
            if (cursor1.moveToFirst()) {
                do {
                    String unn = cursor1.getString(3);
                    TextView edtToAddress = new TextView(getActivity());
                    edtToAddress.setText(unn);

                    TextView edtSubject = new TextView(getActivity());
                    edtSubject.setText("krrohith934@gmail.com");

                    TextView edtMessage = new TextView(getActivity());
                    edtMessage.setText("krrohith934@gmail.com");

                    String user = "me";
                    String to = Utils.getString(edtToAddress);
                    String from = mCredential.getSelectedAccountName();
                    Log.v("sender email", from);
                    String subject = Utils.getString(edtSubject);
                    String body = Utils.getString(edtMessage);
                    MimeMessage mimeMessage;
                    response = "";
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
            chooseAccount();
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
    public void onActivityResult(
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
//                Toast.makeText(getActivity(), "a1", Toast.LENGTH_SHORT).show();
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
//                    Toast.makeText(getActivity(), "a1 "+accountName, Toast.LENGTH_SHORT).show();
                    if (accountName != null) {
//                        Toast.makeText(getActivity(), "a2 "+accountName, Toast.LENGTH_SHORT).show();
                        SharedPreferences settings =
                                getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);

//                        Toast.makeText(getActivity(), "a3 "+accountName, Toast.LENGTH_SHORT).show();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("username", accountName);
                        contentValues.put("password", "");
                        contentValues.put("client", "Gmail");
                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Email_setup");
                        resultUri = getActivity().getContentResolver().insert(contentUri, contentValues);
                        getActivity().getContentResolver().notifyChange(resultUri, null);
//                        db.insert("Email_setup", null, contentValues);

                        Cursor cursor = db.rawQuery("SELECT * FROM Email_setup", null);
                        if (cursor.moveToFirst()){
                            String un = cursor.getString(1);
//                            Toast.makeText(getActivity(), "a4 "+un, Toast.LENGTH_SHORT).show();
                        }
                        cursor.close();

                        getResultsFromApi();
                    }
                }else {
//                    Toast.makeText(getActivity(), "a11", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), EmailSetup.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
            case SECOND_ACTIVITY_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    String ret = data.getStringExtra("Weighing_stat");
                    weighing_scale_onoff.setText(ret);

                    System.out.println("retttttt" + "connected");
                }
        }
    }





    private void updateloginstatus() {

        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("loading");
        progressDialog.show();

        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getActivity());
        company= sharedpreferences.getString("companyname", null);
        store= sharedpreferences.getString("storename", null);
        device= sharedpreferences.getString("devicename", null);
        requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"loginstatussignout.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if(response.equalsIgnoreCase("success")){
                            Intent i=new Intent(getActivity(),MainActivity_Signin_OTPbased.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            getActivity().startActivity(i);
                        }else{
                            Toast.makeText(getActivity(),"Please try again",Toast.LENGTH_LONG).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put("company", company+ "");
                params.put("store", store+ "");
                params.put("device", device);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);


    }

    class DeleteData extends AsyncTask<String, Void, Integer> {


        @Override
        protected Integer doInBackground(String... strings) {
            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            if (c.moveToFirst()) {
                while ( !c.isAfterLast() ) {
                    String tablename=c.getString(0);
                    db.execSQL("delete from "+ tablename);
                    c.moveToNext();
                }
            }
            c.close();

            db.execSQL("vacuum");
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            // updateloginstatus();

            Intent i=new Intent(getActivity(),MainActivity_Signin_OTPbased.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getActivity().startActivity(i);

            Amplify.Auth.signOut(
                    () -> Log.i("AuthQuickstart", "Signed out successfully"),
                    error -> Log.e("AuthQuickstart", error.toString())
            );

            SharedPreferenceManager.getInstance().setDataDownloadValue(false);

        }
    }


    class DeleteSalesData extends AsyncTask<String, Void, Integer> {




        @Override
        protected Integer doInBackground(String... strings) {
            db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            // db1=  SQLiteDatabase.openDatabase("mydb_Appdata", null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);

            Cursor c = db1.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            if (c.moveToFirst()) {
                while ( !c.isAfterLast() ) {
                    String tablename=c.getString(0);
                    db1.execSQL("delete from "+ tablename);
                    c.moveToNext();
                }
            }
            c.close();

            db1.execSQL("vacuum");
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            new DeleteData().execute();

        }
    }


    public void webservicequery(final String webserviceQuery){


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getContext());
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
        RequestQueue queue;
        StringRequest sr1;
        // queue = Volley.newRequestQueue(getActivity());

        queue= RequestSingleton.getInstance(getActivity()).getInstance();

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


    public void webservicequery_sales(final String webserviceQuery){


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(getContext());
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
        RequestQueue queue;
        StringRequest sr1;
        // queue = Volley.newRequestQueue(getActivity());

        queue= RequestSingleton.getInstance(getActivity()).getInstance();

        sr1 = new StringRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl + "webservicequery_sales.php",
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


    class DownloadMusicfromInternet1_loya extends AsyncTask<String, Void, Integer> {
        private ProgressDialog dialog6 = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);

        @Override
        protected Integer doInBackground(String... params) {
            Cursor cursor1 = db1.rawQuery("SELECT * FROM Customerdetails", null);
            if (cursor1.moveToFirst()){
                do {
                    String id = cursor1.getString(0);
                    String bill = cursor1.getString(5);

                    TextView tv = new TextView(getActivity());
                    tv.setText(bill);

                    if (tv.getText().toString().equals("")){

                    }else {
                        float v = (Float.parseFloat(bill) / Float.parseFloat(dialog_amount.getText().toString())) * Float.parseFloat(dialog_point.getText().toString());

                        db1.execSQL("update Customerdetails set loyal_points = '"+String.format("%.2f", v)+"' WHERE _id = '"+id+"'");
                        webservicequery_sales("update Customerdetails set loyal_points = '"+String.format("%.2f", v)+"' WHERE _id = '"+id+"'");

                    }
                }while (cursor1.moveToNext());
            }
            cursor1.close();

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);

            dialog6.setMessage(getString(R.string.setmessage27));
            dialog6.setCanceledOnTouchOutside(false);
            dialog6.setCancelable(false);
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
            dialog6.show();
        }

        @Override
        protected void onPostExecute(Integer file_url) {
            dialog6.dismiss();

            DownloadMusicfromInternet1_loya1 downloadMusicfromInternet1_loya1 = new DownloadMusicfromInternet1_loya1();
            downloadMusicfromInternet1_loya1.execute(text.getText().toString());

//            Cursor cursor2 = db1.rawQuery("SELECT * FROM Customerdetails GROUP BY phoneno", null);
//            if (cursor2.moveToFirst()) {
//                do {
////                    String id = cursor2.getString(0);
//                    String pho = cursor2.getString(2);
//
//                    Cursor cursor3 = db1.rawQuery("SELECT SUM(loyal_points) FROM Customerdetails WHERE phoneno = '"+pho+"'", null);
//                    if (cursor3.moveToFirst()) {
//                        float leveliss = cursor3.getFloat(0);
//                        String tot = String.valueOf(leveliss);
//
//                        ContentValues newValues2 = new ContentValues();
//                        newValues2.put("phoneno", pho);
//                        newValues2.put("points", tot);
//                        db1.insert("customer_loyalty_points", null, newValues2);
//
//                        webservicequery("INSERT INTO customer_loyalty_points (phoneno, points) VALUES ('"+pho+"', '"+tot+"')");
//
//                    }
//                }while (cursor2.moveToNext());
//            }
//            cursor2.close();
        }
    }


    class DownloadMusicfromInternet1_loya1 extends AsyncTask<String, Void, Integer> {
        private ProgressDialog dialog6 = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);

        @Override
        protected Integer doInBackground(String... params) {
            Cursor cursor2 = db1.rawQuery("SELECT * FROM Customerdetails GROUP BY phoneno", null);
            if (cursor2.moveToFirst()) {
                do {
//                    String id = cursor2.getString(0);
                    String pho = cursor2.getString(2);

                    Cursor cursor3 = db1.rawQuery("SELECT SUM(loyal_points) FROM Customerdetails WHERE phoneno = '"+pho+"'", null);
                    if (cursor3.moveToFirst()) {
                        float leveliss = cursor3.getFloat(0);
                        String tot = String.valueOf(leveliss);

                        ContentValues newValues2 = new ContentValues();
                        newValues2.put("phoneno", pho);
                        newValues2.put("points", tot);
                        db1.insert("customer_loyalty_points", null, newValues2);

//                        contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "customer_loyalty_points");
//                        resultUri = getActivity().getContentResolver().insert(contentUri, newValues2);
//                        getActivity().getContentResolver().notifyChange(resultUri, null);

                        webservicequery_sales("INSERT INTO customer_loyalty_points (phoneno, points) VALUES ('"+pho+"', '"+tot+"')");

                    }
                }while (cursor2.moveToNext());
            }
            cursor2.close();

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);

            dialog6.setMessage(getString(R.string.setmessage28));
            dialog6.setCanceledOnTouchOutside(false);
            dialog6.setCancelable(false);
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
            dialog6.show();
        }

        @Override
        protected void onPostExecute(Integer file_url) {
            dialog6.dismiss();

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Toast.makeText(getActivity(), "permission granted", Toast.LENGTH_SHORT).show();
                    System.out.println("permission granted");

                    Intent intent = new Intent(getActivity(), Weighing_Scale_Configuration.class);
                    startActivityForResult(intent, SECOND_ACTIVITY_RESULT_CODE);


                } else {

                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void syncinventory_local() {
        updateBar.setMax(200);

        final ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                try {
                    Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
                    int returnVal = p1.waitFor();
                    reachable = (returnVal==0);
                    System.out.println(""+reachable);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
//                            return result;
            } else {
                boolean isOnline = false;
                try {
                    ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkCapabilities capabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());  // need ACCESS_NETWORK_STATE permission
                    isOnline = capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (isOnline){
                    System.out.println("internet");
                    reachable = true;
                }else {
                    System.out.println("no internet");
                    reachable = false;
                }



//                    return isOnline;
            }

            if(reachable){


                action.setVisibility(View.GONE);

                action1.setVisibility(View.GONE);

                progressBar_license.setVisibility(View.VISIBLE);

                new DeleteData_local().execute();

            }else {
                AlertDialog alertDialog2 = new AlertDialog.Builder(getActivity()).create();

                alertDialog2.setTitle(getString(R.string.title10));
                alertDialog2.setMessage(getString(R.string.setmessage19));
                alertDialog2.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog2.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // finish();
                    }
                });

                alertDialog2.show();

//                            pullToRefresh.setRefreshing(false);

            }

        }
    }

    class DeleteData_local extends AsyncTask<String, Void, Integer> {


        @Override
        protected Integer doInBackground(String... strings) {
            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            if (c.moveToFirst()) {
                while ( !c.isAfterLast() ) {
                    String tablename=c.getString(0);
//                    if (tablename.equalsIgnoreCase("items") || tablename.equalsIgnoreCase("hotel")
//                            || tablename.equalsIgnoreCase("taxes") || tablename.equalsIgnoreCase("discount_details")) {
                        db.execSQL("delete from " + tablename);
//                    }
                    c.moveToNext();
                }
            }
            c.close();
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            appdataBool=false;

            Intent intent = new Intent(getActivity(), MyServiceAppData_local.class);
            ContextCompat.startForegroundService(getActivity(), intent);

//            startDownload();
//            Intent serviceIntent = new Intent(getActivity(), ForegroundService.class);
//            serviceIntent.putExtra("inputExtra", "Processing");
//
//            ContextCompat.startForegroundService(getActivity(), serviceIntent);
        }
    }

    private BroadcastReceiver receiverapp = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            SharedPreferences sharedpreferences = getActivity().getSharedPreferences("downloadpref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("download", "");
            //  editor.putString("downloadsales", "");
            editor.commit();
            new syncTaskApp().execute();

        }
    };

    class syncTaskApp extends AsyncTask<String, Void, Integer> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SyncDatabase syncdatabase=new SyncDatabase();
            syncdatabase.updateSyncDb(getActivity());

        }

        @Override
        protected Integer doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            salesdataBool=true;
            appdataBool=true;
            //  while (true){
            if(salesdataBool&&appdataBool){
//                getlicenses();

                progressBar_license.setVisibility(View.GONE);
//                progressBar.setVisibility(View.GONE);
                action.setVisibility(View.VISIBLE);

                action1.setVisibility(View.GONE);
                //   break;
            }

            //  }


            // getlicenses();

        }
    }

    @Override
    public void onProgressUpdate(int progress) {
        // Do update your progress...

        updateBar.setVisibility(View.VISIBLE);
        Log.e("appdata progress",progress+"");
        //  updateBar.setProgress(progress);
        prog = progress;
        updateBar.setProgress(prog+progSales);

        float perc= ((float)updateBar.getProgress()/(float)updateBar.getMax())*100;
        int p=(int) perc;
        tv_perc.setText(p+"%");
    }

}
