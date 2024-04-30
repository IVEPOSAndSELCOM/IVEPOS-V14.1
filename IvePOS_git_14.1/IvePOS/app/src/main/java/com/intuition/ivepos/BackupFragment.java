package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 1/6/2015.
 */

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Rohithkumar on 1/6/2015.
 */
public class BackupFragment extends Fragment {

    Fragment frag;
    FragmentTransaction fragTransaction;
    LoginDataBaseAdapter loginDataBaseAdapter;

    SQLiteDatabase db = null;
    EditText editTextPassword;

    public BackupFragment(){

    }
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootview = inflater.inflate(R.layout.fragment_multi_backup, null);

        final EditText editTextPassword=(EditText)rootview.findViewById(R.id.etPass);
        editTextPassword.requestFocus();
        //displayKeyboard();

        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);

        return rootview;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        final ImageView btnadmin = (ImageView)getActivity().findViewById(R.id.btngoadmin);

        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        editTextPassword=(EditText)getActivity().findViewById(R.id.etPass);


        editTextPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    btnadmin.performClick();
                    return true;
                }
                return false;
            }
        });

        btnadmin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            permissions(),
                            1);
                    /*// Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

//                        Toast.makeText(ForgotPasswordActivity.this, "111111111", Toast.LENGTH_SHORT).show();

                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1);
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
//                        Toast.makeText(ForgotPasswordActivity.this, "no permission", Toast.LENGTH_SHORT).show();

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }*/
                }else {
//                    Toast.makeText(ForgotPasswordActivity.this, "hiiii", Toast.LENGTH_SHORT).show();

                    if (!SdIsPresent()) ;

                    String password = editTextPassword.getText().toString();

                    // fetch the Password form database for respective user name
                    String storedPassword = loginDataBaseAdapter.getSinlgeEntrypass(password);
                    String storedpasswordlad = loginDataBaseAdapter.getSinlgeEntryladpass(password);
                    if (password.equals(storedPassword)) {
                        frag = new BackupActivity();
                        fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        fragTransaction.commit();
                        hideKeyboard(getContext());
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("Status", "yes");
                        String where = "_id = '1'";
                        db.update("Menulogin_checking", contentValues, where, new String[]{});

                    } else if (password.equals(storedpasswordlad)) {
                        frag = new BackupActivity();
                        fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        fragTransaction.commit();
                        hideKeyboard(getContext());
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("Status", "yes");
                        String where = "_id = '1'";
                        db.update("Menulogin_checking", contentValues, where, new String[]{});
                    } else {
                        editTextPassword.setError("Incorrect Password");
                        //Toast.makeText(getActivity(), "User Name or Password does not match", Toast.LENGTH_LONG).show();
                    }

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
                    //Toast.makeText(getActivity(), "permission granted", Toast.LENGTH_SHORT).show();
                    System.out.println("permission granted");
                    if (!SdIsPresent()) ;

                    String password = editTextPassword.getText().toString();

                    // fetch the Password form database for respective user name
                    String storedPassword = loginDataBaseAdapter.getSinlgeEntrypass(password);
                    String storedpasswordlad = loginDataBaseAdapter.getSinlgeEntryladpass(password);
                    if (password.equals(storedPassword)) {
                        frag = new BackupActivity();
                        fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        fragTransaction.commit();
                        hideKeyboard(getContext());
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("Status", "yes");
                        String where = "_id = '1'";
                        db.update("Menulogin_checking", contentValues, where, new String[]{});

                    } else if (password.equals(storedpasswordlad)) {
                        frag = new BackupActivity();
                        fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        fragTransaction.commit();
                        hideKeyboard(getContext());
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("Status", "yes");
                        String where = "_id = '1'";
                        db.update("Menulogin_checking", contentValues, where, new String[]{});
                    } else {
                        editTextPassword.setError("Incorrect Password");
                        //Toast.makeText(getActivity(), "User Name or Password does not match", Toast.LENGTH_LONG).show();
                    }


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


    public static boolean SdIsPresent() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
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

    public void displayKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

}