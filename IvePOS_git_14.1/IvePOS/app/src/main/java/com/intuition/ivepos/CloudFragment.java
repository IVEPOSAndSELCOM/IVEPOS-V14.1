package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 1/6/2015.
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Rohithkumar on 1/6/2015.
 */
public class CloudFragment extends Fragment {

    Fragment frag;
    FragmentTransaction fragTransaction;
    LoginDataBaseAdapter loginDataBaseAdapter;

    SQLiteDatabase db = null;

    String WebserviceUrl;
    String account_selection;


    public CloudFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        final View rootview = inflater.inflate(R.layout.fragment_multi_admin, null);

        final EditText editTextPassword=(EditText)rootview.findViewById(R.id.etPass);
        editTextPassword.requestFocus();
        //displayKeyboard();
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

        return rootview;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        final ImageView btnadmin = (ImageView)getActivity().findViewById(R.id.btngoadmin);
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        final EditText editTextPassword=(EditText)getActivity().findViewById(R.id.etPass);

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


                String password=editTextPassword.getText().toString();

                // fetch the Password form database for respective user name
                String storedPassword=loginDataBaseAdapter.getSinlgeEntrypass(password);
                String storedpasswordlad = loginDataBaseAdapter.getSinlgeEntryladpass(password);
                if(password.equals(storedPassword))
                {
                    if (account_selection.toString().equals("Dine")) {
                        Intent intent = new Intent(getActivity(), CloudSubscritionActivity.class);
                        startActivity(intent);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("Status", "yes");
                        String where = "_id = '1'";
                        db.update("Menulogin_checking", contentValues, where, new String[]{});
                    }else {
                        if (account_selection.toString().equals("Qsr")) {
                            Intent intent = new Intent(getActivity(), CloudSubscritionActivity.class);
                            startActivity(intent);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("Status", "yes");
                            String where = "_id = '1'";
                            db.update("Menulogin_checking", contentValues, where, new String[]{});
                        }else {
                            Intent intent = new Intent(getActivity(), CloudSubscritionActivity_Retail.class);
                            startActivity(intent);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("Status", "yes");
                            String where = "_id = '1'";
                            db.update("Menulogin_checking", contentValues, where, new String[]{});
                        }
                    }
//                    Intent intent = new Intent(getActivity(), CloudSubscritionActivity.class);
//                    startActivity(intent);
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("Status", "yes");
//                    String where = "_id = '1'";
//                    db.update("Menulogin_checking", contentValues, where, new String[]{});
                }
                else if(password.equals(storedpasswordlad)){
                    if (account_selection.toString().equals("Dine")) {
                        Intent intent = new Intent(getActivity(), CloudSubscritionActivity.class);
                        startActivity(intent);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("Status", "yes");
                        String where = "_id = '1'";
                        db.update("Menulogin_checking", contentValues, where, new String[]{});
                    }else {
                        if (account_selection.toString().equals("Qsr")) {
                            Intent intent = new Intent(getActivity(), CloudSubscritionActivity.class);
                            startActivity(intent);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("Status", "yes");
                            String where = "_id = '1'";
                            db.update("Menulogin_checking", contentValues, where, new String[]{});
                        }else {
                            Intent intent = new Intent(getActivity(), CloudSubscritionActivity_Retail.class);
                            startActivity(intent);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("Status", "yes");
                            String where = "_id = '1'";
                            db.update("Menulogin_checking", contentValues, where, new String[]{});
                        }
                    }
//                    Intent intent = new Intent(getActivity(), CloudSubscritionActivity.class);
//                    startActivity(intent);
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("Status", "yes");
//                    String where = "_id = '1'";
//                    db.update("Menulogin_checking", contentValues, where, new String[]{});
                }

                else
                {
                    editTextPassword.setError("Incorrect password");
                }


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

    public void displayKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void nodisplayKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideKeyboard(getContext());
        donotshowKeyboard(getActivity());
        //nodisplayKeyboard();
        //getActivity().finish();
    }

    public static void donotshowKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }
}

