package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 1/6/2015.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Rohithkumar on 1/6/2015.
 */
public class OrderlistMenuFragment extends Fragment {

    Fragment frag;
    FragmentTransaction fragTransaction;

    SQLiteDatabase db = null;
    String username, password, username1, password2, username11, password22;


    LinearLayout btnadmin, btnuser, btndatabase, btnorderlist, btnprefrence, btnbackup, btncustomer, btningredients;
    ImageView imageadmin, imageuser, imageproduct, imagereport, imagesettings, imagebackup, imagecustomer, imageingredients;
    TextView textadmin, textuser, textproduct, textreport, textsettings, textbackup, textcustomer, textingredients;

    String WebserviceUrl;
    String account_selection;
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

    public  OrderlistMenuFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.orderlistlayout , container, false);
        //final ActionBar actionBar = getActivity().getActionBar();

        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(getActivity());
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

        Cursor access = db.rawQuery("SELECT * FROM LoginUser ", null);
        if (access.moveToFirst()) {
            do {
                username = access.getString(1);
                password = access.getString(2);
            } while (access.moveToNext());
        }

        Cursor che = db.rawQuery("SELECT * FROM LOGIN ", null);
        if (che.moveToFirst()) {
            do {
                username1 = che.getString(1);
                password2 = che.getString(2);
            } while (che.moveToNext());
        }

        Cursor che1 = db.rawQuery("SELECT * FROM LAdmin ", null);
        if (che1.moveToFirst()) {
            do {
                username11 = che1.getString(1);
                password22 = che1.getString(2);
            } while (che1.moveToNext());
        }
        if (username.toString().equals(username1) || username.toString().equals(username11)){
            if (account_selection.toString().equals("Dine")) {
                frag = new GenOrderlistActivity();
                hideKeyboard(getContext());
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
            }else {
                if (account_selection.toString().equals("Qsr")) {
                    frag = new GenOrderlistActivity();
                    hideKeyboard(getContext());
                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                    fragTransaction.commit();
                }else {
                    frag = new GenOrderlistActivity_Retail();
                    hideKeyboard(getContext());
                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                    fragTransaction.commit();
                }
            }

            //actionBar.setTitle("Reports");
//            frag = new GenOrderlistActivity();
//            hideKeyboard(getContext());
//            fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//            fragTransaction.commit();
        }else {
            Cursor cursor = db.rawQuery("SELECT * FROM User_privilege WHERE username = '"+username+"'", null);
            if (cursor.moveToFirst()) {
                String pt = cursor.getString(4);
                if (pt.toString().equals("yes")) {
                    if (account_selection.toString().equals("Dine")) {
                        frag = new GenOrderlistActivity();
                        hideKeyboard(getContext());
                        fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        fragTransaction.commit();
                    }else {
                        if (account_selection.toString().equals("Qsr")) {
                            frag = new GenOrderlistActivity();
                            hideKeyboard(getContext());
                            fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                            fragTransaction.commit();
                        }else {
                            frag = new GenOrderlistActivity_Retail();
                            hideKeyboard(getContext());
                            fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                            fragTransaction.commit();
                        }
                    }
//                    frag = new GenOrderlistActivity();
//                    hideKeyboard(getContext());
//                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                    fragTransaction.commit();
                }else {
                    frag = new OrderlistFragment();
                    fragTransaction = getFragmentManager().beginTransaction().add(R.id.container, frag);
                    fragTransaction.commit();
                }
            }

        }

        btnadmin = (LinearLayout)view.findViewById(R.id.buttonadmin);
        btnuser = (LinearLayout)view.findViewById(R.id.buttonuser);
        btndatabase = (LinearLayout)view.findViewById(R.id.buttondatabase);
        btnorderlist = (LinearLayout)view.findViewById(R.id.buttonorderlist);
        btnprefrence = (LinearLayout)view.findViewById(R.id.buttonpreference);
        btnbackup = (LinearLayout)view.findViewById(R.id.buttonbackup);
        btncustomer = (LinearLayout)view.findViewById(R.id.buttoncustomer);
        btningredients = (LinearLayout)view.findViewById(R.id.buttoningredients);

        imageadmin = (ImageView)view.findViewById(R.id.image_admin);
        imageuser = (ImageView)view.findViewById(R.id.image_user);
        imageproduct = (ImageView)view.findViewById(R.id.image_product);
        imagereport = (ImageView)view.findViewById(R.id.image_report);
        imagesettings = (ImageView)view.findViewById(R.id.image_settings);
        imagebackup = (ImageView)view.findViewById(R.id.image_backup);
        imagecustomer = (ImageView)view.findViewById(R.id.image_customer);
        imageingredients = (ImageView)view.findViewById(R.id.image_ingredients);

        textadmin = (TextView)view.findViewById(R.id.text_admin);
        textuser = (TextView)view.findViewById(R.id.text_user);
        textproduct = (TextView)view.findViewById(R.id.text_product);
        textreport = (TextView)view.findViewById(R.id.text_report);
        textsettings = (TextView)view.findViewById(R.id.text_settings);
        textbackup = (TextView)view.findViewById(R.id.text_backup);
        textcustomer = (TextView)view.findViewById(R.id.text_customer);
        textingredients = (TextView)view.findViewById(R.id.text_ingredients);

        btnadmin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //actionBar.setTitle("Admin");
                if (getActivity() instanceof AppCompatActivity){
                    androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                    actionbar.setTitle("Control panel");
                    actionbar.setSubtitle("Admin");
                }
                frag = new AdminFragment();
                hideKeyboard(getContext());
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();

                btnadmin.setBackgroundColor(getResources().getColor(R.color.itemedit));
                btnuser.setBackgroundResource((R.drawable.admin_button));
                btndatabase.setBackgroundResource((R.drawable.admin_button));
                btnorderlist.setBackgroundResource((R.drawable.admin_button));
                btnprefrence.setBackgroundResource((R.drawable.admin_button));
                btnbackup.setBackgroundResource((R.drawable.admin_button));
                btncustomer.setBackgroundResource((R.drawable.admin_button));
                btningredients.setBackgroundResource((R.drawable.admin_button));

                imageadmin.setImageResource((R.drawable.admin_nocircle_24dp));
                imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                imageproduct.setImageResource((R.drawable.products_nocircle_grayscale_24dp));
                imagereport.setImageResource((R.drawable.reports_nocircle_greysacle_24dp));
                imagesettings.setImageResource((R.drawable.settings_nocircle_greyscale_24dp));
                imagebackup.setImageResource((R.drawable.backup_nocircle_grayscale_24dp));
                imagecustomer.setImageResource((R.drawable.customer_nocircle_greyscale_24dp));
                imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                textadmin.setTextColor(Color.parseColor("#00a99d"));
                textuser.setTextColor(Color.parseColor("#666666"));
                textproduct.setTextColor(Color.parseColor("#666666"));
                textreport.setTextColor(Color.parseColor("#666666"));
                textsettings.setTextColor(Color.parseColor("#666666"));
                textbackup.setTextColor(Color.parseColor("#666666"));
                textcustomer.setTextColor(Color.parseColor("#666666"));
                textingredients.setTextColor(Color.parseColor("#666666"));

            }
        });

        btnuser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                Cursor access = db.rawQuery("SELECT * FROM LoginUser ", null);
                if (access.moveToFirst()) {
                    do {
                        username = access.getString(1);
                        password = access.getString(2);
                    } while (access.moveToNext());
                }

                Cursor che = db.rawQuery("SELECT * FROM LOGIN ", null);
                if (che.moveToFirst()) {
                    do {
                        username1 = che.getString(1);
                        password2 = che.getString(2);
                    } while (che.moveToNext());
                }

                Cursor che1 = db.rawQuery("SELECT * FROM LAdmin ", null);
                if (che1.moveToFirst()) {
                    do {
                        username11 = che1.getString(1);
                        password22 = che1.getString(2);
                    } while (che1.moveToNext());
                }
                if (username.toString().equals(username1) || username.toString().equals(username11)){
                    //actionBar.setTitle("User");
                    if (getActivity() instanceof AppCompatActivity){
                        androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                        actionbar.setTitle("Control panel");
                        actionbar.setSubtitle("User");
                    }
                    frag = new UserPass1Activity();
                    hideKeyboard(getContext());
                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                    fragTransaction.commit();
                    donotshowKeyboard(getActivity());

                    btnadmin.setBackgroundResource((R.drawable.admin_button));
                    btnuser.setBackgroundColor(getResources().getColor(R.color.itemedit));
                    btndatabase.setBackgroundResource((R.drawable.admin_button));
                    btnorderlist.setBackgroundResource((R.drawable.admin_button));
                    btnprefrence.setBackgroundResource((R.drawable.admin_button));
                    btnbackup.setBackgroundResource((R.drawable.admin_button));
                    btncustomer.setBackgroundResource((R.drawable.admin_button));
                    btningredients.setBackgroundResource((R.drawable.admin_button));

                    imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                    imageuser.setImageResource((R.drawable.user_nocircle_24dp));
                    imageproduct.setImageResource((R.drawable.products_nocircle_grayscale_24dp));
                    imagereport.setImageResource((R.drawable.reports_nocircle_greysacle_24dp));
                    imagesettings.setImageResource((R.drawable.settings_nocircle_greyscale_24dp));
                    imagebackup.setImageResource((R.drawable.backup_nocircle_grayscale_24dp));
                    imagecustomer.setImageResource((R.drawable.customer_nocircle_greyscale_24dp));
                    imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                    textadmin.setTextColor(Color.parseColor("#666666"));
                    textuser.setTextColor(Color.parseColor("#00a99d"));
                    textproduct.setTextColor(Color.parseColor("#666666"));
                    textreport.setTextColor(Color.parseColor("#666666"));
                    textsettings.setTextColor(Color.parseColor("#666666"));
                    textbackup.setTextColor(Color.parseColor("#666666"));
                    textcustomer.setTextColor(Color.parseColor("#666666"));
                    textingredients.setTextColor(Color.parseColor("#666666"));
                }else {
                    Cursor cursor = db.rawQuery("SELECT * FROM Menulogin_checking", null);
                    if (cursor.moveToFirst()) {
                        String status = cursor.getString(1);
                        if (status.toString().equals("yes")) {
                            //actionBar.setTitle("User");
                            if (getActivity() instanceof AppCompatActivity){
                                androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                                actionbar.setTitle("Control panel");
                                actionbar.setSubtitle("User");
                            }
                            frag = new UserPass1Activity();
                            hideKeyboard(getContext());
                            fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                            fragTransaction.commit();

                            btnadmin.setBackgroundResource((R.drawable.admin_button));
                            btnuser.setBackgroundColor(getResources().getColor(R.color.itemedit));
                            btndatabase.setBackgroundResource((R.drawable.admin_button));
                            btnorderlist.setBackgroundResource((R.drawable.admin_button));
                            btnprefrence.setBackgroundResource((R.drawable.admin_button));
                            btnbackup.setBackgroundResource((R.drawable.admin_button));
                            btncustomer.setBackgroundResource((R.drawable.admin_button));
                            btningredients.setBackgroundResource((R.drawable.admin_button));

                            imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                            imageuser.setImageResource((R.drawable.user_nocircle_24dp));
                            imageproduct.setImageResource((R.drawable.products_nocircle_grayscale_24dp));
                            imagereport.setImageResource((R.drawable.reports_nocircle_greysacle_24dp));
                            imagesettings.setImageResource((R.drawable.settings_nocircle_greyscale_24dp));
                            imagebackup.setImageResource((R.drawable.backup_nocircle_grayscale_24dp));
                            imagecustomer.setImageResource((R.drawable.customer_nocircle_greyscale_24dp));
                            imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                            textadmin.setTextColor(Color.parseColor("#666666"));
                            textuser.setTextColor(Color.parseColor("#00a99d"));
                            textproduct.setTextColor(Color.parseColor("#666666"));
                            textreport.setTextColor(Color.parseColor("#666666"));
                            textsettings.setTextColor(Color.parseColor("#666666"));
                            textbackup.setTextColor(Color.parseColor("#666666"));
                            textcustomer.setTextColor(Color.parseColor("#666666"));
                            textingredients.setTextColor(Color.parseColor("#666666"));
                        } else {
                            //actionBar.setTitle("User");
                            if (getActivity() instanceof AppCompatActivity){
                                androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                                actionbar.setTitle("Control panel");
                                actionbar.setSubtitle("User");
                            }
                            frag = new UserFragment();
                            hideKeyboard(getContext());
                            fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                            fragTransaction.commit();

                            btnadmin.setBackgroundResource((R.drawable.admin_button));
                            btnuser.setBackgroundColor(getResources().getColor(R.color.itemedit));
                            btndatabase.setBackgroundResource((R.drawable.admin_button));
                            btnorderlist.setBackgroundResource((R.drawable.admin_button));
                            btnprefrence.setBackgroundResource((R.drawable.admin_button));
                            btnbackup.setBackgroundResource((R.drawable.admin_button));
                            btncustomer.setBackgroundResource((R.drawable.admin_button));
                            btningredients.setBackgroundResource((R.drawable.admin_button));

                            imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                            imageuser.setImageResource((R.drawable.user_nocircle_24dp));
                            imageproduct.setImageResource((R.drawable.products_nocircle_grayscale_24dp));
                            imagereport.setImageResource((R.drawable.reports_nocircle_greysacle_24dp));
                            imagesettings.setImageResource((R.drawable.settings_nocircle_greyscale_24dp));
                            imagebackup.setImageResource((R.drawable.backup_nocircle_grayscale_24dp));
                            imagecustomer.setImageResource((R.drawable.customer_nocircle_greyscale_24dp));
                            imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                            textadmin.setTextColor(Color.parseColor("#666666"));
                            textuser.setTextColor(Color.parseColor("#00a99d"));
                            textproduct.setTextColor(Color.parseColor("#666666"));
                            textreport.setTextColor(Color.parseColor("#666666"));
                            textsettings.setTextColor(Color.parseColor("#666666"));
                            textbackup.setTextColor(Color.parseColor("#666666"));
                            textcustomer.setTextColor(Color.parseColor("#666666"));
                            textingredients.setTextColor(Color.parseColor("#666666"));
                        }
                    }
                }



            }
        });

        btndatabase.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //actionBarsetTitle("Product and tax");
                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                Cursor access = db.rawQuery("SELECT * FROM LoginUser ", null);
                if (access.moveToFirst()) {
                    do {
                        username = access.getString(1);
                        password = access.getString(2);
                    } while (access.moveToNext());
                }

                Cursor che = db.rawQuery("SELECT * FROM LOGIN ", null);
                if (che.moveToFirst()) {
                    do {
                        username1 = che.getString(1);
                        password2 = che.getString(2);
                    } while (che.moveToNext());
                }

                Cursor che1 = db.rawQuery("SELECT * FROM LAdmin ", null);
                if (che1.moveToFirst()) {
                    do {
                        username11 = che1.getString(1);
                        password22 = che1.getString(2);
                    } while (che1.moveToNext());
                }
                if (username.toString().equals(username1) || username.toString().equals(username11)){
                    //actionBarsetTitle("Product and tax");
                    if (getActivity() instanceof AppCompatActivity){
                        androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                        actionbar.setTitle("Product");
                        actionbar.setSubtitle("Item");
                    }
                    frag = new DatabaseitemActivity();
                    hideKeyboard(getContext());
                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                    fragTransaction.commit();
                    donotshowKeyboard(getActivity());

                    btnadmin.setBackgroundResource((R.drawable.admin_button));
                    btnuser.setBackgroundResource((R.drawable.admin_button));
                    btndatabase.setBackgroundColor(getResources().getColor(R.color.itemedit));
                    btnorderlist.setBackgroundResource((R.drawable.admin_button));
                    btnprefrence.setBackgroundResource((R.drawable.admin_button));
                    btnbackup.setBackgroundResource((R.drawable.admin_button));
                    btncustomer.setBackgroundResource((R.drawable.admin_button));
                    btningredients.setBackgroundResource((R.drawable.admin_button));

                    imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                    imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                    imageproduct.setImageResource((R.drawable.products_nocircle_24dp));
                    imagereport.setImageResource((R.drawable.reports_nocircle_greysacle_24dp));
                    imagesettings.setImageResource((R.drawable.settings_nocircle_greyscale_24dp));
                    imagebackup.setImageResource((R.drawable.backup_nocircle_grayscale_24dp));
                    imagecustomer.setImageResource((R.drawable.customer_nocircle_greyscale_24dp));
                    imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                    textadmin.setTextColor(Color.parseColor("#666666"));
                    textuser.setTextColor(Color.parseColor("#666666"));
                    textproduct.setTextColor(Color.parseColor("#00a99d"));
                    textreport.setTextColor(Color.parseColor("#666666"));
                    textsettings.setTextColor(Color.parseColor("#666666"));
                    textbackup.setTextColor(Color.parseColor("#666666"));
                    textcustomer.setTextColor(Color.parseColor("#666666"));
                    textingredients.setTextColor(Color.parseColor("#666666"));
                }else {
                    Cursor cursor1 = db.rawQuery("SELECT * FROM User_privilege WHERE username = '"+username+"'", null);
                    if (cursor1.moveToFirst()){
                        String pt = cursor1.getString(3);
                        if (pt.toString().equals("yes")){
                            frag = new DatabaseitemActivity();
                            hideKeyboard(getContext());
                            fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                            fragTransaction.commit();
                            donotshowKeyboard(getActivity());

                            btnadmin.setBackgroundResource((R.drawable.admin_button));
                            btnuser.setBackgroundResource((R.drawable.admin_button));
                            btndatabase.setBackgroundColor(getResources().getColor(R.color.itemedit));
                            btnorderlist.setBackgroundResource((R.drawable.admin_button));
                            btnprefrence.setBackgroundResource((R.drawable.admin_button));
                            btnbackup.setBackgroundResource((R.drawable.admin_button));
                            btncustomer.setBackgroundResource((R.drawable.admin_button));
                            btningredients.setBackgroundResource((R.drawable.admin_button));

                            imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                            imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                            imageproduct.setImageResource((R.drawable.products_nocircle_24dp));
                            imagereport.setImageResource((R.drawable.reports_nocircle_greysacle_24dp));
                            imagesettings.setImageResource((R.drawable.settings_nocircle_greyscale_24dp));
                            imagebackup.setImageResource((R.drawable.backup_nocircle_grayscale_24dp));
                            imagecustomer.setImageResource((R.drawable.customer_nocircle_greyscale_24dp));
                            imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                            textadmin.setTextColor(Color.parseColor("#666666"));
                            textuser.setTextColor(Color.parseColor("#666666"));
                            textproduct.setTextColor(Color.parseColor("#00a99d"));
                            textreport.setTextColor(Color.parseColor("#666666"));
                            textsettings.setTextColor(Color.parseColor("#666666"));
                            textbackup.setTextColor(Color.parseColor("#666666"));
                            textcustomer.setTextColor(Color.parseColor("#666666"));
                            textingredients.setTextColor(Color.parseColor("#666666"));
                        }else {
                            Cursor cursor = db.rawQuery("SELECT * FROM Menulogin_checking", null);
                            if (cursor.moveToFirst()){
                                String status = cursor.getString(1);
                                if (status.toString().equals("yes")) {


                                    if (getActivity() instanceof AppCompatActivity){
                                        androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                                        actionbar.setTitle("Product");
                                        actionbar.setSubtitle("Item");
                                    }
                                    frag = new DatabaseitemActivity();
                                    hideKeyboard(getContext());
                                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                    fragTransaction.commit();

                                    btnadmin.setBackgroundResource((R.drawable.admin_button));
                                    btnuser.setBackgroundResource((R.drawable.admin_button));
                                    btndatabase.setBackgroundColor(getResources().getColor(R.color.itemedit));
                                    btnorderlist.setBackgroundResource((R.drawable.admin_button));
                                    btnprefrence.setBackgroundResource((R.drawable.admin_button));
                                    btnbackup.setBackgroundResource((R.drawable.admin_button));
                                    btncustomer.setBackgroundResource((R.drawable.admin_button));
                                    btningredients.setBackgroundResource((R.drawable.admin_button));

                                    imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                                    imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                                    imageproduct.setImageResource((R.drawable.products_nocircle_24dp));
                                    imagereport.setImageResource((R.drawable.reports_nocircle_greysacle_24dp));
                                    imagesettings.setImageResource((R.drawable.settings_nocircle_greyscale_24dp));
                                    imagebackup.setImageResource((R.drawable.backup_nocircle_grayscale_24dp));
                                    imagecustomer.setImageResource((R.drawable.customer_nocircle_greyscale_24dp));
                                    imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                                    textadmin.setTextColor(Color.parseColor("#666666"));
                                    textuser.setTextColor(Color.parseColor("#666666"));
                                    textproduct.setTextColor(Color.parseColor("#00a99d"));
                                    textreport.setTextColor(Color.parseColor("#666666"));
                                    textsettings.setTextColor(Color.parseColor("#666666"));
                                    textbackup.setTextColor(Color.parseColor("#666666"));
                                    textcustomer.setTextColor(Color.parseColor("#666666"));
                                    textingredients.setTextColor(Color.parseColor("#666666"));
                                }else {


                                    if (getActivity() instanceof AppCompatActivity){
                                        androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                                        actionbar.setTitle("Product");
                                        actionbar.setSubtitle("Item");

                                    }
                                    frag = new DatabaseFragment();
                                    hideKeyboard(getContext());
                                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                    fragTransaction.commit();

                                    btnadmin.setBackgroundResource((R.drawable.admin_button));
                                    btnuser.setBackgroundResource((R.drawable.admin_button));
                                    btndatabase.setBackgroundColor(getResources().getColor(R.color.itemedit));
                                    btnorderlist.setBackgroundResource((R.drawable.admin_button));
                                    btnprefrence.setBackgroundResource((R.drawable.admin_button));
                                    btnbackup.setBackgroundResource((R.drawable.admin_button));
                                    btncustomer.setBackgroundResource((R.drawable.admin_button));
                                    btningredients.setBackgroundResource((R.drawable.admin_button));

                                    imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                                    imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                                    imageproduct.setImageResource((R.drawable.products_nocircle_24dp));
                                    imagereport.setImageResource((R.drawable.reports_nocircle_greysacle_24dp));
                                    imagesettings.setImageResource((R.drawable.settings_nocircle_greyscale_24dp));
                                    imagebackup.setImageResource((R.drawable.backup_nocircle_grayscale_24dp));
                                    imagecustomer.setImageResource((R.drawable.customer_nocircle_greyscale_24dp));
                                    imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                                    textadmin.setTextColor(Color.parseColor("#666666"));
                                    textuser.setTextColor(Color.parseColor("#666666"));
                                    textproduct.setTextColor(Color.parseColor("#00a99d"));
                                    textreport.setTextColor(Color.parseColor("#666666"));
                                    textsettings.setTextColor(Color.parseColor("#666666"));
                                    textbackup.setTextColor(Color.parseColor("#666666"));
                                    textcustomer.setTextColor(Color.parseColor("#666666"));
                                    textingredients.setTextColor(Color.parseColor("#666666"));
                                }
                            }
                        }
                    }

                }






            }
        });

        btnorderlist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                Cursor access = db.rawQuery("SELECT * FROM LoginUser ", null);
                if (access.moveToFirst()) {
                    do {
                        username = access.getString(1);
                        password = access.getString(2);
                    } while (access.moveToNext());
                }

                Cursor che = db.rawQuery("SELECT * FROM LOGIN ", null);
                if (che.moveToFirst()) {
                    do {
                        username1 = che.getString(1);
                        password2 = che.getString(2);
                    } while (che.moveToNext());
                }

                Cursor che1 = db.rawQuery("SELECT * FROM LAdmin ", null);
                if (che1.moveToFirst()) {
                    do {
                        username11 = che1.getString(1);
                        password22 = che1.getString(2);
                    } while (che1.moveToNext());
                }
                if (username.toString().equals(username1) || username.toString().equals(username11)){
                    //actionBarsetTitle("Reports");
                    if (getActivity() instanceof AppCompatActivity){
                        androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                        actionbar.setTitle("Report");
                        actionbar.setSubtitle("Sales");
                    }
                    if (account_selection.toString().equals("Dine")) {
                        frag = new GenOrderlistActivity();
                        hideKeyboard(getContext());
                        fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        fragTransaction.commit();
                        donotshowKeyboard(getActivity());
                    }else {
                        if (account_selection.toString().equals("Qsr")) {
                            frag = new GenOrderlistActivity();
                            hideKeyboard(getContext());
                            fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                            fragTransaction.commit();
                            donotshowKeyboard(getActivity());
                        }else {
                            frag = new GenOrderlistActivity_Retail();
                            hideKeyboard(getContext());
                            fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                            fragTransaction.commit();
                            donotshowKeyboard(getActivity());
                        }
                    }
//                    frag = new GenOrderlistActivity();
//                    hideKeyboard(getContext());
//                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                    fragTransaction.commit();
//                    donotshowKeyboard(getActivity());

                    btnadmin.setBackgroundResource((R.drawable.admin_button));
                    btnuser.setBackgroundResource((R.drawable.admin_button));
                    btndatabase.setBackgroundResource((R.drawable.admin_button));
                    btnorderlist.setBackgroundColor(getResources().getColor(R.color.itemedit));
                    btnprefrence.setBackgroundResource((R.drawable.admin_button));
                    btnbackup.setBackgroundResource((R.drawable.admin_button));
                    btncustomer.setBackgroundResource((R.drawable.admin_button));
                    btningredients.setBackgroundResource((R.drawable.admin_button));

                    imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                    imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                    imageproduct.setImageResource((R.drawable.products_nocircle_grayscale_24dp));
                    imagereport.setImageResource((R.drawable.reports_nocircle_24dp));
                    imagesettings.setImageResource((R.drawable.settings_nocircle_greyscale_24dp));
                    imagebackup.setImageResource((R.drawable.backup_nocircle_grayscale_24dp));
                    imagecustomer.setImageResource((R.drawable.customer_nocircle_greyscale_24dp));
                    imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                    textadmin.setTextColor(Color.parseColor("#666666"));
                    textuser.setTextColor(Color.parseColor("#666666"));
                    textproduct.setTextColor(Color.parseColor("#666666"));
                    textreport.setTextColor(Color.parseColor("#00a99d"));
                    textsettings.setTextColor(Color.parseColor("#666666"));
                    textbackup.setTextColor(Color.parseColor("#666666"));
                    textcustomer.setTextColor(Color.parseColor("#666666"));
                    textingredients.setTextColor(Color.parseColor("#666666"));
                }else {
                    Cursor cursor1 = db.rawQuery("SELECT * FROM User_privilege WHERE username = '"+username+"'", null);
                    if (cursor1.moveToFirst()) {
                        String pt = cursor1.getString(4);
                        if (pt.toString().equals("yes")) {
                            if (account_selection.toString().equals("Dine")) {
                                frag = new GenOrderlistActivity();
                                hideKeyboard(getContext());
                                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                fragTransaction.commit();
                                donotshowKeyboard(getActivity());
                            }else {
                                if (account_selection.toString().equals("Qsr")) {
                                    frag = new GenOrderlistActivity();
                                    hideKeyboard(getContext());
                                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                    fragTransaction.commit();
                                    donotshowKeyboard(getActivity());
                                }else {
                                    frag = new GenOrderlistActivity_Retail();
                                    hideKeyboard(getContext());
                                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                    fragTransaction.commit();
                                    donotshowKeyboard(getActivity());
                                }
                            }
//                            frag = new GenOrderlistActivity();
//                            hideKeyboard(getContext());
//                            fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                            fragTransaction.commit();
//                            donotshowKeyboard(getActivity());

                            btnadmin.setBackgroundResource((R.drawable.admin_button));
                            btnuser.setBackgroundResource((R.drawable.admin_button));
                            btndatabase.setBackgroundResource((R.drawable.admin_button));
                            btnorderlist.setBackgroundColor(getResources().getColor(R.color.itemedit));
                            btnprefrence.setBackgroundResource((R.drawable.admin_button));
                            btnbackup.setBackgroundResource((R.drawable.admin_button));
                            btncustomer.setBackgroundResource((R.drawable.admin_button));
                            btningredients.setBackgroundResource((R.drawable.admin_button));

                            imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                            imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                            imageproduct.setImageResource((R.drawable.products_nocircle_grayscale_24dp));
                            imagereport.setImageResource((R.drawable.reports_nocircle_24dp));
                            imagesettings.setImageResource((R.drawable.settings_nocircle_greyscale_24dp));
                            imagebackup.setImageResource((R.drawable.backup_nocircle_grayscale_24dp));
                            imagecustomer.setImageResource((R.drawable.customer_nocircle_greyscale_24dp));
                            imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                            textadmin.setTextColor(Color.parseColor("#666666"));
                            textuser.setTextColor(Color.parseColor("#666666"));
                            textproduct.setTextColor(Color.parseColor("#666666"));
                            textreport.setTextColor(Color.parseColor("#00a99d"));
                            textsettings.setTextColor(Color.parseColor("#666666"));
                            textbackup.setTextColor(Color.parseColor("#666666"));
                            textcustomer.setTextColor(Color.parseColor("#666666"));
                            textingredients.setTextColor(Color.parseColor("#666666"));

                        }else {
                            Cursor cursor = db.rawQuery("SELECT * FROM Menulogin_checking", null);
                            if (cursor.moveToFirst()) {
                                String status = cursor.getString(1);
                                if (status.toString().equals("yes")) {
                                    //actionBarsetTitle("Reports");
                                    //Toast.makeText(getActivity(), " action bar title is "+//actionBargetTitle(), Toast.LENGTH_SHORT).show();
//                        //actionBarhide();
//                        final ActionBar actionBar1 = getActivity().getActionBar();
//                        actionBar1.show();
//                        actionBar1.setTitle("Reports");
                                    if (getActivity() instanceof AppCompatActivity){
                                        androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                                        actionbar.setTitle("Report");
                                        actionbar.setSubtitle("Sales");
                                    }
                                    if (account_selection.toString().equals("Dine")) {
                                        frag = new GenOrderlistActivity();
                                        hideKeyboard(getContext());
                                        fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                        fragTransaction.commit();
                                        donotshowKeyboard(getActivity());
                                    }else {
                                        if (account_selection.toString().equals("Qsr")) {
                                            frag = new GenOrderlistActivity();
                                            hideKeyboard(getContext());
                                            fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                            fragTransaction.commit();
                                            donotshowKeyboard(getActivity());
                                        }else {
                                            frag = new GenOrderlistActivity_Retail();
                                            hideKeyboard(getContext());
                                            fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                            fragTransaction.commit();
                                            donotshowKeyboard(getActivity());
                                        }
                                    }
//                                    frag = new GenOrderlistActivity();
//                                    hideKeyboard(getContext());
//                                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                                    fragTransaction.commit();

                                    btnadmin.setBackgroundResource((R.drawable.admin_button));
                                    btnuser.setBackgroundResource((R.drawable.admin_button));
                                    btndatabase.setBackgroundResource((R.drawable.admin_button));
                                    btnorderlist.setBackgroundColor(getResources().getColor(R.color.itemedit));
                                    btnprefrence.setBackgroundResource((R.drawable.admin_button));
                                    btnbackup.setBackgroundResource((R.drawable.admin_button));
                                    btncustomer.setBackgroundResource((R.drawable.admin_button));
                                    btningredients.setBackgroundResource((R.drawable.admin_button));

                                    imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                                    imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                                    imageproduct.setImageResource((R.drawable.products_nocircle_grayscale_24dp));
                                    imagereport.setImageResource((R.drawable.reports_nocircle_24dp));
                                    imagesettings.setImageResource((R.drawable.settings_nocircle_greyscale_24dp));
                                    imagebackup.setImageResource((R.drawable.backup_nocircle_grayscale_24dp));
                                    imagecustomer.setImageResource((R.drawable.customer_nocircle_greyscale_24dp));
                                    imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                                    textadmin.setTextColor(Color.parseColor("#666666"));
                                    textuser.setTextColor(Color.parseColor("#666666"));
                                    textproduct.setTextColor(Color.parseColor("#666666"));
                                    textreport.setTextColor(Color.parseColor("#00a99d"));
                                    textsettings.setTextColor(Color.parseColor("#666666"));
                                    textbackup.setTextColor(Color.parseColor("#666666"));
                                    textcustomer.setTextColor(Color.parseColor("#666666"));
                                    textingredients.setTextColor(Color.parseColor("#666666"));
                                } else {
                                    //actionBarsetTitle("Reports");
                                    //Toast.makeText(getActivity(), " action bar title is 1111 "+//actionBargetTitle(), Toast.LENGTH_SHORT).show();
//                        //actionBarhide();
//                        final ActionBar actionBar1 = getActivity().getActionBar();
//                        actionBar1.show();
//                        actionBar1.setTitle("Reports");
                                    if (getActivity() instanceof AppCompatActivity){
                                        androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                                        actionbar.setTitle("Report");
                                        actionbar.setSubtitle("Sales");
                                    }
                                    frag = new OrderlistFragment();
                                    hideKeyboard(getContext());
                                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                    fragTransaction.commit();

                                    btnadmin.setBackgroundResource((R.drawable.admin_button));
                                    btnuser.setBackgroundResource((R.drawable.admin_button));
                                    btndatabase.setBackgroundResource((R.drawable.admin_button));
                                    btnorderlist.setBackgroundColor(getResources().getColor(R.color.itemedit));
                                    btnprefrence.setBackgroundResource((R.drawable.admin_button));
                                    btnbackup.setBackgroundResource((R.drawable.admin_button));
                                    btncustomer.setBackgroundResource((R.drawable.admin_button));
                                    btningredients.setBackgroundResource((R.drawable.admin_button));

                                    imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                                    imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                                    imageproduct.setImageResource((R.drawable.products_nocircle_grayscale_24dp));
                                    imagereport.setImageResource((R.drawable.reports_nocircle_24dp));
                                    imagesettings.setImageResource((R.drawable.settings_nocircle_greyscale_24dp));
                                    imagebackup.setImageResource((R.drawable.backup_nocircle_grayscale_24dp));
                                    imagecustomer.setImageResource((R.drawable.customer_nocircle_greyscale_24dp));
                                    imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                                    textadmin.setTextColor(Color.parseColor("#666666"));
                                    textuser.setTextColor(Color.parseColor("#666666"));
                                    textproduct.setTextColor(Color.parseColor("#666666"));
                                    textreport.setTextColor(Color.parseColor("#00a99d"));
                                    textsettings.setTextColor(Color.parseColor("#666666"));
                                    textbackup.setTextColor(Color.parseColor("#666666"));
                                    textcustomer.setTextColor(Color.parseColor("#666666"));
                                    textingredients.setTextColor(Color.parseColor("#666666"));
                                }
                            }
                        }
                    }

                }





            }
        });

        btnprefrence.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                Cursor access = db.rawQuery("SELECT * FROM LoginUser ", null);
                if (access.moveToFirst()) {
                    do {
                        username = access.getString(1);
                        password = access.getString(2);
                    } while (access.moveToNext());
                }

                Cursor che = db.rawQuery("SELECT * FROM LOGIN ", null);
                if (che.moveToFirst()) {
                    do {
                        username1 = che.getString(1);
                        password2 = che.getString(2);
                    } while (che.moveToNext());
                }

                Cursor che1 = db.rawQuery("SELECT * FROM LAdmin ", null);
                if (che1.moveToFirst()) {
                    do {
                        username11 = che1.getString(1);
                        password22 = che1.getString(2);
                    } while (che1.moveToNext());
                }
                if (username.toString().equals(username1) || username.toString().equals(username11)){
                    //actionBarsetTitle("Settings");
                    if (getActivity() instanceof AppCompatActivity){
                        androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                        actionbar.setTitle("Settings");
                        actionbar.setSubtitle("General");
                    }
                    frag = new GlobalPreferenceActivity();
                    hideKeyboard(getContext());
                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                    fragTransaction.commit();
                    donotshowKeyboard(getActivity());

                    btnadmin.setBackgroundResource((R.drawable.admin_button));
                    btnuser.setBackgroundResource((R.drawable.admin_button));
                    btndatabase.setBackgroundResource((R.drawable.admin_button));
                    btnorderlist.setBackgroundResource((R.drawable.admin_button));
                    btnprefrence.setBackgroundColor(getResources().getColor(R.color.itemedit));
                    btnbackup.setBackgroundResource((R.drawable.admin_button));
                    btncustomer.setBackgroundResource((R.drawable.admin_button));
                    btningredients.setBackgroundResource((R.drawable.admin_button));

                    imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                    imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                    imageproduct.setImageResource((R.drawable.products_nocircle_grayscale_24dp));
                    imagereport.setImageResource((R.drawable.reports_nocircle_greysacle_24dp));
                    imagesettings.setImageResource((R.drawable.settings_nocircle_24dp));
                    imagebackup.setImageResource((R.drawable.backup_nocircle_grayscale_24dp));
                    imagecustomer.setImageResource((R.drawable.customer_nocircle_greyscale_24dp));
                    imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                    textadmin.setTextColor(Color.parseColor("#666666"));
                    textuser.setTextColor(Color.parseColor("#666666"));
                    textproduct.setTextColor(Color.parseColor("#666666"));
                    textreport.setTextColor(Color.parseColor("#666666"));
                    textsettings.setTextColor(Color.parseColor("#00a99d"));
                    textbackup.setTextColor(Color.parseColor("#666666"));
                    textcustomer.setTextColor(Color.parseColor("#666666"));
                    textingredients.setTextColor(Color.parseColor("#666666"));
                }else {
                    Cursor cursor1 = db.rawQuery("SELECT * FROM User_privilege WHERE username = '"+username+"'", null);
                    if (cursor1.moveToFirst()) {
                        String pt = cursor1.getString(5);
                        if (pt.toString().equals("yes")) {
                            frag = new GlobalPreferenceActivity();
                            hideKeyboard(getContext());
                            fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                            fragTransaction.commit();
                            donotshowKeyboard(getActivity());

                            btnadmin.setBackgroundResource((R.drawable.admin_button));
                            btnuser.setBackgroundResource((R.drawable.admin_button));
                            btndatabase.setBackgroundResource((R.drawable.admin_button));
                            btnorderlist.setBackgroundResource((R.drawable.admin_button));
                            btnprefrence.setBackgroundColor(getResources().getColor(R.color.itemedit));
                            btnbackup.setBackgroundResource((R.drawable.admin_button));
                            btncustomer.setBackgroundResource((R.drawable.admin_button));
                            btningredients.setBackgroundResource((R.drawable.admin_button));

                            imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                            imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                            imageproduct.setImageResource((R.drawable.products_nocircle_grayscale_24dp));
                            imagereport.setImageResource((R.drawable.reports_nocircle_greysacle_24dp));
                            imagesettings.setImageResource((R.drawable.settings_nocircle_24dp));
                            imagebackup.setImageResource((R.drawable.backup_nocircle_grayscale_24dp));
                            imagecustomer.setImageResource((R.drawable.customer_nocircle_greyscale_24dp));
                            imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                            textadmin.setTextColor(Color.parseColor("#666666"));
                            textuser.setTextColor(Color.parseColor("#666666"));
                            textproduct.setTextColor(Color.parseColor("#666666"));
                            textreport.setTextColor(Color.parseColor("#666666"));
                            textsettings.setTextColor(Color.parseColor("#00a99d"));
                            textbackup.setTextColor(Color.parseColor("#666666"));
                            textcustomer.setTextColor(Color.parseColor("#666666"));
                            textingredients.setTextColor(Color.parseColor("#666666"));
                        }else {
                            Cursor cursor = db.rawQuery("SELECT * FROM Menulogin_checking", null);
                            if (cursor.moveToFirst()) {
                                String status = cursor.getString(1);
                                if (status.toString().equals("yes")) {
                                    //actionBarsetTitle("Settings");
                                    if (getActivity() instanceof AppCompatActivity){
                                        androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                                        actionbar.setTitle("Settings");
                                        actionbar.setSubtitle("General");
                                    }
                                    frag = new GlobalPreferenceActivity();
                                    hideKeyboard(getContext());
                                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                    fragTransaction.commit();

                                    btnadmin.setBackgroundResource((R.drawable.admin_button));
                                    btnuser.setBackgroundResource((R.drawable.admin_button));
                                    btndatabase.setBackgroundResource((R.drawable.admin_button));
                                    btnorderlist.setBackgroundResource((R.drawable.admin_button));
                                    btnprefrence.setBackgroundColor(getResources().getColor(R.color.itemedit));
                                    btnbackup.setBackgroundResource((R.drawable.admin_button));
                                    btncustomer.setBackgroundResource((R.drawable.admin_button));
                                    btningredients.setBackgroundResource((R.drawable.admin_button));

                                    imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                                    imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                                    imageproduct.setImageResource((R.drawable.products_nocircle_grayscale_24dp));
                                    imagereport.setImageResource((R.drawable.reports_nocircle_greysacle_24dp));
                                    imagesettings.setImageResource((R.drawable.settings_nocircle_24dp));
                                    imagebackup.setImageResource((R.drawable.backup_nocircle_grayscale_24dp));
                                    imagecustomer.setImageResource((R.drawable.customer_nocircle_greyscale_24dp));
                                    imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                                    textadmin.setTextColor(Color.parseColor("#666666"));
                                    textuser.setTextColor(Color.parseColor("#666666"));
                                    textproduct.setTextColor(Color.parseColor("#666666"));
                                    textreport.setTextColor(Color.parseColor("#666666"));
                                    textsettings.setTextColor(Color.parseColor("#00a99d"));
                                    textbackup.setTextColor(Color.parseColor("#666666"));
                                    textcustomer.setTextColor(Color.parseColor("#666666"));
                                    textingredients.setTextColor(Color.parseColor("#666666"));
                                } else {
                                    //actionBarsetTitle("Settings");
                                    if (getActivity() instanceof AppCompatActivity){
                                        androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                                        actionbar.setTitle("Settings");
                                        actionbar.setSubtitle("General");
                                    }
                                    frag = new PreferenceFragment();
                                    hideKeyboard(getContext());
                                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                    fragTransaction.commit();

                                    btnadmin.setBackgroundResource((R.drawable.admin_button));
                                    btnuser.setBackgroundResource((R.drawable.admin_button));
                                    btndatabase.setBackgroundResource((R.drawable.admin_button));
                                    btnorderlist.setBackgroundResource((R.drawable.admin_button));
                                    btnprefrence.setBackgroundColor(getResources().getColor(R.color.itemedit));
                                    btnbackup.setBackgroundResource((R.drawable.admin_button));
                                    btncustomer.setBackgroundResource((R.drawable.admin_button));
                                    btningredients.setBackgroundResource((R.drawable.admin_button));

                                    imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                                    imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                                    imageproduct.setImageResource((R.drawable.products_nocircle_grayscale_24dp));
                                    imagereport.setImageResource((R.drawable.reports_nocircle_greysacle_24dp));
                                    imagesettings.setImageResource((R.drawable.settings_nocircle_24dp));
                                    imagebackup.setImageResource((R.drawable.backup_nocircle_grayscale_24dp));
                                    imagecustomer.setImageResource((R.drawable.customer_nocircle_greyscale_24dp));
                                    imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                                    textadmin.setTextColor(Color.parseColor("#666666"));
                                    textuser.setTextColor(Color.parseColor("#666666"));
                                    textproduct.setTextColor(Color.parseColor("#666666"));
                                    textreport.setTextColor(Color.parseColor("#666666"));
                                    textsettings.setTextColor(Color.parseColor("#00a99d"));
                                    textbackup.setTextColor(Color.parseColor("#666666"));
                                    textcustomer.setTextColor(Color.parseColor("#666666"));
                                    textingredients.setTextColor(Color.parseColor("#666666"));
                                }
                            }
                        }
                    }
                }



            }
        });



        btnbackup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                Cursor access = db.rawQuery("SELECT * FROM LoginUser ", null);
                if (access.moveToFirst()) {
                    do {
                        username = access.getString(1);
                        password = access.getString(2);
                    } while (access.moveToNext());
                }

                Cursor che = db.rawQuery("SELECT * FROM LOGIN ", null);
                if (che.moveToFirst()) {
                    do {
                        username1 = che.getString(1);
                        password2 = che.getString(2);
                    } while (che.moveToNext());
                }

                Cursor che1 = db.rawQuery("SELECT * FROM LAdmin ", null);
                if (che1.moveToFirst()) {
                    do {
                        username11 = che1.getString(1);
                        password22 = che1.getString(2);
                    } while (che1.moveToNext());
                }
                if (username.toString().equals(username1) || username.toString().equals(username11)){
                    //actionBarsetTitle("Backup");
                    if (getActivity() instanceof AppCompatActivity){
                        androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                        actionbar.setTitle("Backup");
                        actionbar.setSubtitle("Data");
                    }
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(getActivity(),
                                permissions(),
                                1);
                        /*// Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

//                            Toast.makeText(getActivity(), "111111111aa", Toast.LENGTH_SHORT).show();

                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    1);
                            // Show an explanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.
//                        Toast.makeText(ForgotPasswordActivity.this, "no permission", Toast.LENGTH_SHORT).show();

                        } else {

                            // No explanation needed, we can request the permission.
//                            Toast.makeText(getActivity(), "222222222bb", Toast.LENGTH_SHORT).show();
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    1);

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }*/
                    }else {
                        frag = new BackupActivity();
                        hideKeyboard(getContext());
                        fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        fragTransaction.commit();
                        donotshowKeyboard(getActivity());

                        btnadmin.setBackgroundResource((R.drawable.admin_button));
                        btnuser.setBackgroundResource((R.drawable.admin_button));
                        btndatabase.setBackgroundResource((R.drawable.admin_button));
                        btnorderlist.setBackgroundResource((R.drawable.admin_button));
                        btnprefrence.setBackgroundResource((R.drawable.admin_button));
                        btnbackup.setBackgroundColor(getResources().getColor(R.color.itemedit));
                        btncustomer.setBackgroundResource((R.drawable.admin_button));
                        btningredients.setBackgroundResource((R.drawable.admin_button));

                        imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                        imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                        imageproduct.setImageResource((R.drawable.products_nocircle_grayscale_24dp));
                        imagereport.setImageResource((R.drawable.reports_nocircle_greysacle_24dp));
                        imagesettings.setImageResource((R.drawable.settings_nocircle_greyscale_24dp));
                        imagebackup.setImageResource((R.drawable.backup_nocircle_24dp));
                        imagecustomer.setImageResource((R.drawable.customer_nocircle_greyscale_24dp));
                        imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                        textadmin.setTextColor(Color.parseColor("#666666"));
                        textuser.setTextColor(Color.parseColor("#666666"));
                        textproduct.setTextColor(Color.parseColor("#666666"));
                        textreport.setTextColor(Color.parseColor("#666666"));
                        textsettings.setTextColor(Color.parseColor("#666666"));
                        textbackup.setTextColor(Color.parseColor("#00a99d"));
                        textcustomer.setTextColor(Color.parseColor("#666666"));
                        textingredients.setTextColor(Color.parseColor("#666666"));
                    }
                }else {
                    Cursor cursor1 = db.rawQuery("SELECT * FROM User_privilege WHERE username = '"+username+"'", null);
                    if (cursor1.moveToFirst()) {
                        String pt = cursor1.getString(6);
                        if (pt.toString().equals("yes")) {
                            if (getActivity() instanceof AppCompatActivity){
                                androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                                actionbar.setTitle("Backup");
                                actionbar.setSubtitle("Data");
                            }
                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(getActivity(),
                                        permissions(),
                                        1);
                                /*// Should we show an explanation?
                                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

//                            Toast.makeText(getActivity(), "111111111aa", Toast.LENGTH_SHORT).show();

                                    ActivityCompat.requestPermissions(getActivity(),
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            1);
                                    // Show an explanation to the user *asynchronously* -- don't block
                                    // this thread waiting for the user's response! After the user
                                    // sees the explanation, try again to request the permission.
//                        Toast.makeText(ForgotPasswordActivity.this, "no permission", Toast.LENGTH_SHORT).show();

                                } else {

                                    // No explanation needed, we can request the permission.
//                            Toast.makeText(getActivity(), "222222222bb", Toast.LENGTH_SHORT).show();
                                    ActivityCompat.requestPermissions(getActivity(),
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            1);

                                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                    // app-defined int constant. The callback method gets the
                                    // result of the request.
                                }*/
                            }else {
                                frag = new BackupActivity();
                                hideKeyboard(getContext());
                                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                fragTransaction.commit();
                                donotshowKeyboard(getActivity());

                                btnadmin.setBackgroundResource((R.drawable.admin_button));
                                btnuser.setBackgroundResource((R.drawable.admin_button));
                                btndatabase.setBackgroundResource((R.drawable.admin_button));
                                btnorderlist.setBackgroundResource((R.drawable.admin_button));
                                btnprefrence.setBackgroundResource((R.drawable.admin_button));
                                btnbackup.setBackgroundColor(getResources().getColor(R.color.itemedit));
                                btncustomer.setBackgroundResource((R.drawable.admin_button));
                                btningredients.setBackgroundResource((R.drawable.admin_button));

                                imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                                imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                                imageproduct.setImageResource((R.drawable.products_nocircle_grayscale_24dp));
                                imagereport.setImageResource((R.drawable.reports_nocircle_greysacle_24dp));
                                imagesettings.setImageResource((R.drawable.settings_nocircle_greyscale_24dp));
                                imagebackup.setImageResource((R.drawable.backup_nocircle_24dp));
                                imagecustomer.setImageResource((R.drawable.customer_nocircle_greyscale_24dp));
                                imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                                textadmin.setTextColor(Color.parseColor("#666666"));
                                textuser.setTextColor(Color.parseColor("#666666"));
                                textproduct.setTextColor(Color.parseColor("#666666"));
                                textreport.setTextColor(Color.parseColor("#666666"));
                                textsettings.setTextColor(Color.parseColor("#666666"));
                                textbackup.setTextColor(Color.parseColor("#00a99d"));
                                textcustomer.setTextColor(Color.parseColor("#666666"));
                                textingredients.setTextColor(Color.parseColor("#666666"));
                            }
                        }else {
                            Cursor cursor = db.rawQuery("SELECT * FROM Menulogin_checking", null);
                            if (cursor.moveToFirst()) {
                                String status = cursor.getString(1);
                                if (status.toString().equals("yes")) {
                                    //actionBarsetTitle("Backup");
                                    if (getActivity() instanceof AppCompatActivity){
                                        androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                                        actionbar.setTitle("Backup");
                                        actionbar.setSubtitle("Data");
                                    }
                                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                            != PackageManager.PERMISSION_GRANTED) {

                                        ActivityCompat.requestPermissions(getActivity(),
                                                permissions(),
                                                1);
                                        /*// Should we show an explanation?
                                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

//                                    Toast.makeText(getActivity(), "111111111", Toast.LENGTH_SHORT).show();

                                            ActivityCompat.requestPermissions(getActivity(),
                                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    1);
                                            // Show an explanation to the user *asynchronously* -- don't block
                                            // this thread waiting for the user's response! After the user
                                            // sees the explanation, try again to request the permission.
//                        Toast.makeText(ForgotPasswordActivity.this, "no permission", Toast.LENGTH_SHORT).show();

                                        } else {

                                            // No explanation needed, we can request the permission.
//                                    Toast.makeText(getActivity(), "222222222", Toast.LENGTH_SHORT).show();
                                            ActivityCompat.requestPermissions(getActivity(),
                                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    1);

                                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                            // app-defined int constant. The callback method gets the
                                            // result of the request.
                                        }*/
                                    }else {
//                                Toast.makeText(getActivity(), "hiiii", Toast.LENGTH_SHORT).show();

                                        if (!SdIsPresent()) ;

                                        frag = new BackupActivity();
                                        hideKeyboard(getContext());
                                        fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                        fragTransaction.commit();

                                        btnadmin.setBackgroundResource((R.drawable.admin_button));
                                        btnuser.setBackgroundResource((R.drawable.admin_button));
                                        btndatabase.setBackgroundResource((R.drawable.admin_button));
                                        btnorderlist.setBackgroundResource((R.drawable.admin_button));
                                        btnprefrence.setBackgroundResource((R.drawable.admin_button));
                                        btnbackup.setBackgroundColor(getResources().getColor(R.color.itemedit));
                                        btncustomer.setBackgroundResource((R.drawable.admin_button));
                                        btningredients.setBackgroundResource((R.drawable.admin_button));

                                        imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                                        imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                                        imageproduct.setImageResource((R.drawable.products_nocircle_grayscale_24dp));
                                        imagereport.setImageResource((R.drawable.reports_nocircle_greysacle_24dp));
                                        imagesettings.setImageResource((R.drawable.settings_nocircle_greyscale_24dp));
                                        imagebackup.setImageResource((R.drawable.backup_nocircle_24dp));
                                        imagecustomer.setImageResource((R.drawable.customer_nocircle_greyscale_24dp));
                                        imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                                        textadmin.setTextColor(Color.parseColor("#666666"));
                                        textuser.setTextColor(Color.parseColor("#666666"));
                                        textproduct.setTextColor(Color.parseColor("#666666"));
                                        textreport.setTextColor(Color.parseColor("#666666"));
                                        textsettings.setTextColor(Color.parseColor("#666666"));
                                        textbackup.setTextColor(Color.parseColor("#00a99d"));
                                        textcustomer.setTextColor(Color.parseColor("#666666"));
                                        textingredients.setTextColor(Color.parseColor("#666666"));
                                    }
                                } else {
                                    //actionBarsetTitle("Backup");
                                    if (getActivity() instanceof AppCompatActivity){
                                        androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                                        actionbar.setTitle("Backup");
                                        actionbar.setSubtitle("Data");
                                    }
                                    frag = new BackupFragment();
                                    hideKeyboard(getContext());
                                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                    fragTransaction.commit();

                                    btnadmin.setBackgroundResource((R.drawable.admin_button));
                                    btnuser.setBackgroundResource((R.drawable.admin_button));
                                    btndatabase.setBackgroundResource((R.drawable.admin_button));
                                    btnorderlist.setBackgroundResource((R.drawable.admin_button));
                                    btnprefrence.setBackgroundResource((R.drawable.admin_button));
                                    btnbackup.setBackgroundColor(getResources().getColor(R.color.itemedit));
                                    btncustomer.setBackgroundResource((R.drawable.admin_button));
                                    btningredients.setBackgroundResource((R.drawable.admin_button));

                                    imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                                    imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                                    imageproduct.setImageResource((R.drawable.products_nocircle_grayscale_24dp));
                                    imagereport.setImageResource((R.drawable.reports_nocircle_greysacle_24dp));
                                    imagesettings.setImageResource((R.drawable.settings_nocircle_greyscale_24dp));
                                    imagebackup.setImageResource((R.drawable.backup_nocircle_24dp));
                                    imagecustomer.setImageResource((R.drawable.customer_nocircle_greyscale_24dp));
                                    imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                                    textadmin.setTextColor(Color.parseColor("#666666"));
                                    textuser.setTextColor(Color.parseColor("#666666"));
                                    textproduct.setTextColor(Color.parseColor("#666666"));
                                    textreport.setTextColor(Color.parseColor("#666666"));
                                    textsettings.setTextColor(Color.parseColor("#666666"));
                                    textbackup.setTextColor(Color.parseColor("#00a99d"));
                                    textcustomer.setTextColor(Color.parseColor("#666666"));
                                    textingredients.setTextColor(Color.parseColor("#666666"));
                                }
                            }
                        }
                    }

                }



            }
        });

        btncustomer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                Cursor access = db.rawQuery("SELECT * FROM LoginUser ", null);
                if (access.moveToFirst()) {
                    do {
                        username = access.getString(1);
                        password = access.getString(2);
                    } while (access.moveToNext());
                }

                Cursor che = db.rawQuery("SELECT * FROM LOGIN ", null);
                if (che.moveToFirst()) {
                    do {
                        username1 = che.getString(1);
                        password2 = che.getString(2);
                    } while (che.moveToNext());
                }

                Cursor che1 = db.rawQuery("SELECT * FROM LAdmin ", null);
                if (che1.moveToFirst()) {
                    do {
                        username11 = che1.getString(1);
                        password22 = che1.getString(2);
                    } while (che1.moveToNext());
                }
                if (username.toString().equals(username1) || username.toString().equals(username11)){
                    //actionBarsetTitle("Settings");
                    if (getActivity() instanceof AppCompatActivity){
                        androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                        actionbar.setTitle("Customer");
                        actionbar.setSubtitle("Management");
                    }
                    frag = new CustomerActivity();
                    hideKeyboard(getContext());
                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                    fragTransaction.commit();

                    btnadmin.setBackgroundResource((R.drawable.admin_button));
                    btnuser.setBackgroundResource((R.drawable.admin_button));
                    btndatabase.setBackgroundResource((R.drawable.admin_button));
                    btnorderlist.setBackgroundResource((R.drawable.admin_button));
                    btnprefrence.setBackgroundResource((R.drawable.admin_button));
                    btnbackup.setBackgroundResource((R.drawable.admin_button));
                    btncustomer.setBackgroundColor(getResources().getColor(R.color.itemedit));
                    btningredients.setBackgroundResource((R.drawable.admin_button));

                    imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                    imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                    imageproduct.setImageResource((R.drawable.products_nocircle_grayscale_24dp));
                    imagereport.setImageResource((R.drawable.reports_nocircle_greysacle_24dp));
                    imagesettings.setImageResource((R.drawable.settings_nocircle_greyscale_24dp));
                    imagebackup.setImageResource((R.drawable.backup_nocircle_grayscale_24dp));
                    imagecustomer.setImageResource((R.drawable.customer_nocircle_24dp));
                    imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                    textadmin.setTextColor(Color.parseColor("#666666"));
                    textuser.setTextColor(Color.parseColor("#666666"));
                    textproduct.setTextColor(Color.parseColor("#666666"));
                    textreport.setTextColor(Color.parseColor("#666666"));
                    textsettings.setTextColor(Color.parseColor("#666666"));
                    textbackup.setTextColor(Color.parseColor("#666666"));
                    textcustomer.setTextColor(Color.parseColor("#00a99d"));
                    textingredients.setTextColor(Color.parseColor("#666666"));
                }else {
                    Cursor cursor1 = db.rawQuery("SELECT * FROM User_privilege WHERE username = '"+username+"'", null);
                    if (cursor1.moveToFirst()) {
                        String pt = cursor1.getString(7);
                        if (pt.toString().equals("yes")) {
                            if (getActivity() instanceof AppCompatActivity){
                                androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                                actionbar.setTitle("Customer");
                                actionbar.setSubtitle("Management");
                            }
                            frag = new CustomerActivity();
                            hideKeyboard(getContext());
                            fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                            fragTransaction.commit();

                            btnadmin.setBackgroundResource((R.drawable.admin_button));
                            btnuser.setBackgroundResource((R.drawable.admin_button));
                            btndatabase.setBackgroundResource((R.drawable.admin_button));
                            btnorderlist.setBackgroundResource((R.drawable.admin_button));
                            btnprefrence.setBackgroundResource((R.drawable.admin_button));
                            btnbackup.setBackgroundResource((R.drawable.admin_button));
                            btncustomer.setBackgroundColor(getResources().getColor(R.color.itemedit));
                            btningredients.setBackgroundResource((R.drawable.admin_button));

                            imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                            imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                            imageproduct.setImageResource((R.drawable.products_nocircle_grayscale_24dp));
                            imagereport.setImageResource((R.drawable.reports_nocircle_greysacle_24dp));
                            imagesettings.setImageResource((R.drawable.settings_nocircle_greyscale_24dp));
                            imagebackup.setImageResource((R.drawable.backup_nocircle_grayscale_24dp));
                            imagecustomer.setImageResource((R.drawable.customer_nocircle_24dp));
                            imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                            textadmin.setTextColor(Color.parseColor("#666666"));
                            textuser.setTextColor(Color.parseColor("#666666"));
                            textproduct.setTextColor(Color.parseColor("#666666"));
                            textreport.setTextColor(Color.parseColor("#666666"));
                            textsettings.setTextColor(Color.parseColor("#666666"));
                            textbackup.setTextColor(Color.parseColor("#666666"));
                            textcustomer.setTextColor(Color.parseColor("#00a99d"));
                            textingredients.setTextColor(Color.parseColor("#666666"));
                        }else {
                            Cursor cursor = db.rawQuery("SELECT * FROM Menulogin_checking", null);
                            if (cursor.moveToFirst()) {
                                String status = cursor.getString(1);
                                if (status.toString().equals("yes")) {
                                    //actionBarsetTitle("Settings");
                                    if (getActivity() instanceof AppCompatActivity){
                                        androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                                        actionbar.setTitle("Customer");
                                        actionbar.setSubtitle("Management");
                                    }
                                    frag = new CustomerActivity();
                                    hideKeyboard(getContext());
                                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                    fragTransaction.commit();

                                    btnadmin.setBackgroundResource((R.drawable.admin_button));
                                    btnuser.setBackgroundResource((R.drawable.admin_button));
                                    btndatabase.setBackgroundResource((R.drawable.admin_button));
                                    btnorderlist.setBackgroundResource((R.drawable.admin_button));
                                    btnprefrence.setBackgroundResource((R.drawable.admin_button));
                                    btnbackup.setBackgroundResource((R.drawable.admin_button));
                                    btncustomer.setBackgroundColor(getResources().getColor(R.color.itemedit));
                                    btningredients.setBackgroundResource((R.drawable.admin_button));

                                    imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                                    imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                                    imageproduct.setImageResource((R.drawable.products_nocircle_grayscale_24dp));
                                    imagereport.setImageResource((R.drawable.reports_nocircle_greysacle_24dp));
                                    imagesettings.setImageResource((R.drawable.settings_nocircle_greyscale_24dp));
                                    imagebackup.setImageResource((R.drawable.backup_nocircle_grayscale_24dp));
                                    imagecustomer.setImageResource((R.drawable.customer_nocircle_24dp));
                                    imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                                    textadmin.setTextColor(Color.parseColor("#666666"));
                                    textuser.setTextColor(Color.parseColor("#666666"));
                                    textproduct.setTextColor(Color.parseColor("#666666"));
                                    textreport.setTextColor(Color.parseColor("#666666"));
                                    textsettings.setTextColor(Color.parseColor("#666666"));
                                    textbackup.setTextColor(Color.parseColor("#666666"));
                                    textcustomer.setTextColor(Color.parseColor("#00a99d"));
                                    textingredients.setTextColor(Color.parseColor("#666666"));
                                } else {
                                    //actionBarsetTitle("Settings");
                                    if (getActivity() instanceof AppCompatActivity){
                                        androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                                        actionbar.setTitle("Customer");
                                        actionbar.setSubtitle("Management");
                                    }
                                    frag = new CustomerMenuFragment();
                                    hideKeyboard(getContext());
                                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                    fragTransaction.commit();

                                    btnadmin.setBackgroundResource((R.drawable.admin_button));
                                    btnuser.setBackgroundResource((R.drawable.admin_button));
                                    btndatabase.setBackgroundResource((R.drawable.admin_button));
                                    btnorderlist.setBackgroundResource((R.drawable.admin_button));
                                    btnprefrence.setBackgroundResource((R.drawable.admin_button));
                                    btnbackup.setBackgroundResource((R.drawable.admin_button));
                                    btncustomer.setBackgroundColor(getResources().getColor(R.color.itemedit));
                                    btningredients.setBackgroundResource((R.drawable.admin_button));

                                    imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                                    imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                                    imageproduct.setImageResource((R.drawable.products_nocircle_grayscale_24dp));
                                    imagereport.setImageResource((R.drawable.reports_nocircle_greysacle_24dp));
                                    imagesettings.setImageResource((R.drawable.settings_nocircle_greyscale_24dp));
                                    imagebackup.setImageResource((R.drawable.backup_nocircle_grayscale_24dp));
                                    imagecustomer.setImageResource((R.drawable.customer_nocircle_24dp));
                                    imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                                    textadmin.setTextColor(Color.parseColor("#666666"));
                                    textuser.setTextColor(Color.parseColor("#666666"));
                                    textproduct.setTextColor(Color.parseColor("#666666"));
                                    textreport.setTextColor(Color.parseColor("#666666"));
                                    textsettings.setTextColor(Color.parseColor("#666666"));
                                    textbackup.setTextColor(Color.parseColor("#666666"));
                                    textcustomer.setTextColor(Color.parseColor("#00a99d"));
                                    textingredients.setTextColor(Color.parseColor("#666666"));
                                }
                            }
                        }
                    }
                }
            }
        });


        btningredients.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle(getString(R.string.title7));
                alertDialog.setMessage(getString(R.string.setmessage1));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            }
        });


        return view;
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

    public static void donotshowKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    public static boolean SdIsPresent() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
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
                    //Toast.makeText(getActivity(), "permission granted", Toast.LENGTH_SHORT).show();
                    System.out.println("permission granted");
                    if (!SdIsPresent()) ;

                    frag = new BackupActivity();
                    hideKeyboard(getContext());
                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                    fragTransaction.commit();
                    donotshowKeyboard(getActivity());

                    btnadmin.setBackgroundResource((R.drawable.admin_button));
                    btnuser.setBackgroundResource((R.drawable.admin_button));
                    btndatabase.setBackgroundResource((R.drawable.admin_button));
                    btnorderlist.setBackgroundResource((R.drawable.admin_button));
                    btnprefrence.setBackgroundResource((R.drawable.admin_button));
                    btnbackup.setBackgroundColor(getResources().getColor(R.color.itemedit));
                    btncustomer.setBackgroundResource((R.drawable.admin_button));
                    btningredients.setBackgroundResource((R.drawable.admin_button));

                    imageadmin.setImageResource((R.drawable.admin_nocircle_greyscale_24dp));
                    imageuser.setImageResource((R.drawable.user_nocircle_greyscale_24dp));
                    imageproduct.setImageResource((R.drawable.products_nocircle_grayscale_24dp));
                    imagereport.setImageResource((R.drawable.reports_nocircle_greysacle_24dp));
                    imagesettings.setImageResource((R.drawable.settings_nocircle_greyscale_24dp));
                    imagebackup.setImageResource((R.drawable.backup_nocircle_24dp));
                    imagecustomer.setImageResource((R.drawable.customer_nocircle_greyscale_24dp));
                    imageingredients.setImageResource((R.drawable.ingredients_micro_inventory_nocircle_grayscale_24dp));

                    textadmin.setTextColor(Color.parseColor("#666666"));
                    textuser.setTextColor(Color.parseColor("#666666"));
                    textproduct.setTextColor(Color.parseColor("#666666"));
                    textreport.setTextColor(Color.parseColor("#666666"));
                    textsettings.setTextColor(Color.parseColor("#666666"));
                    textbackup.setTextColor(Color.parseColor("#00a99d"));
                    textcustomer.setTextColor(Color.parseColor("#666666"));
                    textingredients.setTextColor(Color.parseColor("#666666"));


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

}
