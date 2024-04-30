package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 1/6/2015.
 */
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.intuition.ivepos.syncapp.StubProviderApp;

/**
 * Created by Rohithkumar on 1/6/2015.
 */
public class UserPass6Activity extends Fragment {

    Fragment frag;
    FragmentTransaction fragTransaction;

    public SQLiteDatabase db = null;
    String username, password, usernamee, name, password_check1, password_check2, password_check3, password_check4, password_check5, password_check6, password_checkladmin, password_check3madmin ;
    Uri contentUri,resultUri;
    String status;
    public  UserPass6Activity() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //final ActionBar actionBar = getActivity().getActionBar();
        View view = inflater.inflate(R.layout.user_chg_pwd6, container, false);

        if (getActivity() instanceof AppCompatActivity){
            androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionbar.setSubtitle("User 6");
        }

        //actionBar.setTitle("User 6 - Control Panel");

//        LinearLayout relativeLayout1 = (LinearLayout)view.findViewById(R.id.user1);
//        relativeLayout1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                frag = new UserPass1Activity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });

        LinearLayout relativeLayout1 = (LinearLayout)view.findViewById(R.id.user1);
        relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                Cursor access = db.rawQuery("SELECT * FROM UserLogin_Checking ", null);
                if (access.moveToFirst()) {
                    do {
                        username = access.getString(1);
                        password = access.getString(2);
                        name = access.getString(3);


                    } while (access.moveToNext());
                }

                Cursor cursor = db.rawQuery("SELECT * FROM Menulogin_checking", null);
                if (cursor.moveToFirst()) {
                    status = cursor.getString(1);

                }

                if (name.toString().equals("madmin") || name.toString().equals("ladmin") || status.toString().equals("yes")){
                    frag = new UserPass1Activity();
                    hideKeyboard(getContext());
                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                    fragTransaction.commit();
                }else {
                    final Dialog dialog = new Dialog(getActivity(), R.style.notitle);
                    dialog.setContentView(R.layout.dialog_user_authentication);
                    //dialog.setTitle(Html.fromHtml("<font color='#ffffff'>Enter the password</font>"));

                    Button close = (Button) dialog.findViewById(R.id.closedialog);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    final EditText editTextPassword = (EditText) dialog.findViewById(R.id.etPass);
                    final Button goedit = (Button) dialog.findViewById(R.id.go);
                    editTextPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_GO) {
                                goedit.performClick();
                                return true;
                            }
                            return false;
                        }
                    });

                    goedit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String password = editTextPassword.getText().toString();

                            String storedPassword= getSingleEntrymadminpass(password);
                            String storedpasswordlad = getSingleEntryladminpass(password);
                            String storedpassworduser1 = getSingleEntryUser1pass(password);
                            if(password.equals(storedPassword)) {
                                frag = new UserPass1Activity();
                                hideKeyboard(getContext());
                                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                fragTransaction.commit();
                                dialog.dismiss();
                            }
                            else if (password.equals(storedpasswordlad)){
                                frag = new UserPass1Activity();
                                hideKeyboard(getContext());
                                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                fragTransaction.commit();
                                dialog.dismiss();
                            }else  if (password.equals(storedpassworduser1)){
                                frag = new UserPass1Activity();
                                hideKeyboard(getContext());
                                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                fragTransaction.commit();
                                dialog.dismiss();
                            }else {
                                //Toast.makeText(getActivity(), "User Name or Password does not match", Toast.LENGTH_LONG).show();
                                editTextPassword.setError("Incorrect password");
                            }
                        }
                    });
                    dialog.show();
                }
//                frag = new UserPass2Activity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
            }
        });

//        LinearLayout relativeLayout2 = (LinearLayout)view.findViewById(R.id.user2);
//        relativeLayout2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                frag = new UserPass2Activity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });

        LinearLayout relativeLayout2 = (LinearLayout)view.findViewById(R.id.user2);
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                Cursor access = db.rawQuery("SELECT * FROM UserLogin_Checking ", null);
                if (access.moveToFirst()) {
                    do {
                        username = access.getString(1);
                        password = access.getString(2);
                        name = access.getString(3);


                    } while (access.moveToNext());
                }

                Cursor cursor = db.rawQuery("SELECT * FROM Menulogin_checking", null);
                if (cursor.moveToFirst()) {
                    status = cursor.getString(1);

                }

                if (name.toString().equals("madmin") || name.toString().equals("ladmin") || status.toString().equals("yes")){
                    frag = new UserPass2Activity();
                    hideKeyboard(getContext());
                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                    fragTransaction.commit();
                }else {
                    final Dialog dialog = new Dialog(getActivity(), R.style.notitle);
                    dialog.setContentView(R.layout.dialog_user_authentication);
                    //dialog.setTitle(Html.fromHtml("<font color='#ffffff'>Enter the password</font>"));

                    Button close = (Button) dialog.findViewById(R.id.closedialog);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    final EditText editTextPassword = (EditText) dialog.findViewById(R.id.etPass);

                    final Button goedit = (Button) dialog.findViewById(R.id.go);
                    editTextPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_GO) {
                                goedit.performClick();
                                return true;
                            }
                            return false;
                        }
                    });

                    goedit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String password = editTextPassword.getText().toString();

                            String storedPassword= getSingleEntrymadminpass(password);
                            String storedpasswordlad = getSingleEntryladminpass(password);
                            String storedpassworduser2 = getSingleEntryUser2pass(password);
                            if(password.equals(storedPassword)) {
                                frag = new UserPass2Activity();
                                hideKeyboard(getContext());
                                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                fragTransaction.commit();
                                dialog.dismiss();
                            }
                            else if (password.equals(storedpasswordlad)){
                                frag = new UserPass2Activity();
                                hideKeyboard(getContext());
                                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                fragTransaction.commit();
                                dialog.dismiss();
                            }else  if (password.equals(storedpassworduser2)){
                                frag = new UserPass2Activity();
                                hideKeyboard(getContext());
                                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                fragTransaction.commit();
                                dialog.dismiss();
                            }else {
                                //Toast.makeText(getActivity(), "User Name or Password does not match", Toast.LENGTH_LONG).show();
                                editTextPassword.setError("Incorrect password");
                            }
                        }
                    });
                    dialog.show();
                }
//                frag = new UserPass2Activity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
            }
        });

//        LinearLayout relativeLayout3 = (LinearLayout)view.findViewById(R.id.user3);
//        relativeLayout3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                frag = new UserPass3Activity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });

        LinearLayout relativeLayout3 = (LinearLayout)view.findViewById(R.id.user3);
        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                Cursor access = db.rawQuery("SELECT * FROM UserLogin_Checking ", null);
                if (access.moveToFirst()) {
                    do {
                        username = access.getString(1);
                        password = access.getString(2);
                        name = access.getString(3);


                    } while (access.moveToNext());
                }

                Cursor cursor = db.rawQuery("SELECT * FROM Menulogin_checking", null);
                if (cursor.moveToFirst()) {
                    status = cursor.getString(1);

                }

                if (name.toString().equals("madmin") || name.toString().equals("ladmin") || status.toString().equals("yes")){
                    frag = new UserPass3Activity();
                    hideKeyboard(getContext());
                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                    fragTransaction.commit();
                }else {
                    final Dialog dialog = new Dialog(getActivity(), R.style.notitle);
                    dialog.setContentView(R.layout.dialog_user_authentication);
                    //dialog.setTitle(Html.fromHtml("<font color='#ffffff'>Enter the password</font>"));

                    Button close = (Button) dialog.findViewById(R.id.closedialog);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    final EditText editTextPassword = (EditText) dialog.findViewById(R.id.etPass);

                    final Button goedit = (Button) dialog.findViewById(R.id.go);
                    editTextPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_GO) {
                                goedit.performClick();
                                return true;
                            }
                            return false;
                        }
                    });

                    goedit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String password = editTextPassword.getText().toString();

                            String storedPassword= getSingleEntrymadminpass(password);
                            String storedpasswordlad = getSingleEntryladminpass(password);
                            String storedpassworduser3 = getSingleEntryUser3pass(password);
                            if(password.equals(storedPassword)) {
                                frag = new UserPass3Activity();
                                hideKeyboard(getContext());
                                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                fragTransaction.commit();
                                dialog.dismiss();
                            }
                            else if (password.equals(storedpasswordlad)){
                                frag = new UserPass3Activity();
                                hideKeyboard(getContext());
                                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                fragTransaction.commit();
                                dialog.dismiss();
                            }else  if (password.equals(storedpassworduser3)){
                                frag = new UserPass3Activity();
                                hideKeyboard(getContext());
                                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                fragTransaction.commit();
                                dialog.dismiss();
                            }else {
                                //Toast.makeText(getActivity(), "User Name or Password does not match", Toast.LENGTH_LONG).show();
                                editTextPassword.setError("Incorrect password");
                            }
                        }
                    });
                    dialog.show();
                }
//                frag = new UserPass2Activity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
            }
        });

//        LinearLayout relativeLayout4 = (LinearLayout)view.findViewById(R.id.user4);
//        relativeLayout4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                frag = new UserPass4Activity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });

        LinearLayout relativeLayout4 = (LinearLayout)view.findViewById(R.id.user4);
        relativeLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                Cursor access = db.rawQuery("SELECT * FROM UserLogin_Checking ", null);
                if (access.moveToFirst()) {
                    do {
                        username = access.getString(1);
                        password = access.getString(2);
                        name = access.getString(3);


                    } while (access.moveToNext());
                }

                Cursor cursor = db.rawQuery("SELECT * FROM Menulogin_checking", null);
                if (cursor.moveToFirst()) {
                    status = cursor.getString(1);

                }

                if (name.toString().equals("madmin") || name.toString().equals("ladmin") || status.toString().equals("yes")){
                    frag = new UserPass4Activity();
                    hideKeyboard(getContext());
                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                    fragTransaction.commit();
                }else {
                    final Dialog dialog = new Dialog(getActivity(), R.style.notitle);
                    dialog.setContentView(R.layout.dialog_user_authentication);
                    //dialog.setTitle(Html.fromHtml("<font color='#ffffff'>Enter the password</font>"));

                    Button close = (Button) dialog.findViewById(R.id.closedialog);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    final EditText editTextPassword = (EditText) dialog.findViewById(R.id.etPass);

                    final Button goedit = (Button) dialog.findViewById(R.id.go);
                    editTextPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_GO) {
                                goedit.performClick();
                                return true;
                            }
                            return false;
                        }
                    });

                    goedit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String password = editTextPassword.getText().toString();

                            String storedPassword= getSingleEntrymadminpass(password);
                            String storedpasswordlad = getSingleEntryladminpass(password);
                            String storedpassworduser4 = getSingleEntryUser4pass(password);
                            if(password.equals(storedPassword)) {
                                frag = new UserPass4Activity();
                                hideKeyboard(getContext());
                                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                fragTransaction.commit();
                                dialog.dismiss();
                            }
                            else if (password.equals(storedpasswordlad)){
                                frag = new UserPass4Activity();
                                hideKeyboard(getContext());
                                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                fragTransaction.commit();
                                dialog.dismiss();
                            }else  if (password.equals(storedpassworduser4)){
                                frag = new UserPass4Activity();
                                hideKeyboard(getContext());
                                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                fragTransaction.commit();
                                dialog.dismiss();
                            }else {
                                //Toast.makeText(getActivity(), "User Name or Password does not match", Toast.LENGTH_LONG).show();
                                editTextPassword.setError("Incorrect password");
                            }
                        }
                    });
                    dialog.show();
                }
//                frag = new UserPass2Activity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
            }
        });

//        LinearLayout relativeLayout5 = (LinearLayout)view.findViewById(R.id.user5);
//        relativeLayout5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                frag = new UserPass5Activity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });

        LinearLayout relativeLayout5 = (LinearLayout)view.findViewById(R.id.user5);
        relativeLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                Cursor access = db.rawQuery("SELECT * FROM UserLogin_Checking ", null);
                if (access.moveToFirst()) {
                    do {
                        username = access.getString(1);
                        password = access.getString(2);
                        name = access.getString(3);


                    } while (access.moveToNext());
                }

                Cursor cursor = db.rawQuery("SELECT * FROM Menulogin_checking", null);
                if (cursor.moveToFirst()) {
                    status = cursor.getString(1);

                }

                if (name.toString().equals("madmin") || name.toString().equals("ladmin") || status.toString().equals("yes")){
                    frag = new UserPass5Activity();
                    hideKeyboard(getContext());
                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                    fragTransaction.commit();
                }else {
                    final Dialog dialog = new Dialog(getActivity(), R.style.notitle);
                    dialog.setContentView(R.layout.dialog_user_authentication);
                    //dialog.setTitle(Html.fromHtml("<font color='#ffffff'>Enter the password</font>"));

                    Button close = (Button) dialog.findViewById(R.id.closedialog);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    final EditText editTextPassword = (EditText) dialog.findViewById(R.id.etPass);

                    final Button goedit = (Button) dialog.findViewById(R.id.go);
                    editTextPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_GO) {
                                goedit.performClick();
                                return true;
                            }
                            return false;
                        }
                    });

                    goedit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String password = editTextPassword.getText().toString();

                            String storedPassword= getSingleEntrymadminpass(password);
                            String storedpasswordlad = getSingleEntryladminpass(password);
                            String storedpassworduser5 = getSingleEntryUser5pass(password);
                            if(password.equals(storedPassword)) {
                                frag = new UserPass5Activity();
                                hideKeyboard(getContext());
                                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                fragTransaction.commit();
                                dialog.dismiss();
                            }
                            else if (password.equals(storedpasswordlad)){
                                frag = new UserPass5Activity();
                                hideKeyboard(getContext());
                                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                fragTransaction.commit();
                                dialog.dismiss();
                            }else  if (password.equals(storedpassworduser5)){
                                frag = new UserPass5Activity();
                                hideKeyboard(getContext());
                                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                                fragTransaction.commit();
                                dialog.dismiss();
                            }else {
                                //Toast.makeText(getActivity(), "User Name or Password does not match", Toast.LENGTH_LONG).show();
                                editTextPassword.setError("Incorrect password");
                            }
                        }
                    });
                    dialog.show();
                }
//                frag = new UserPass2Activity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
            }
        });

        LinearLayout relativeLayout6 = (LinearLayout)view.findViewById(R.id.user6);
        relativeLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag = new UserPass6Activity();
                hideKeyboard(getContext());
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
            }
        });


        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        Cursor access = db.rawQuery("SELECT * FROM User6 ", null);
        if (access.moveToFirst()) {
            do {
                username = access.getString(2);
                password = access.getString(3);
                usernamee = access.getString(1);


            } while (access.moveToNext());
        }


        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        Cursor access1 = db.rawQuery("SELECT * FROM User1 ", null);
        if (access1.moveToFirst()) {
            do {
                password_check1 = access1.getString(3);
            } while (access1.moveToNext());
        }
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        Cursor access2 = db.rawQuery("SELECT * FROM User2 ", null);
        if (access2.moveToFirst()) {
            do {
                password_check2 = access2.getString(3);
            } while (access2.moveToNext());
        }
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        Cursor access3 = db.rawQuery("SELECT * FROM User3 ", null);
        if (access3.moveToFirst()) {
            do {
                password_check3 = access3.getString(3);
            } while (access3.moveToNext());
        }
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        Cursor access4 = db.rawQuery("SELECT * FROM User4 ", null);
        if (access4.moveToFirst()) {
            do {
                password_check4 = access4.getString(3);
            } while (access4.moveToNext());
        }
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        Cursor access5 = db.rawQuery("SELECT * FROM User5 ", null);
        if (access5.moveToFirst()) {
            do {
                password_check5 = access5.getString(3);
            } while (access5.moveToNext());
        }
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        Cursor access6 = db.rawQuery("SELECT * FROM User6 ", null);
        if (access6.moveToFirst()) {
            do {
                password_check6 = access6.getString(3);
            } while (access6.moveToNext());
        }
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        Cursor accessladmin = db.rawQuery("SELECT * FROM LAdmin ", null);
        if (accessladmin.moveToFirst()) {
            do {
                password_checkladmin = accessladmin.getString(2);
            } while (accessladmin.moveToNext());
        }
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        Cursor accessmadmin = db.rawQuery("SELECT * FROM LOGIN ", null);
        if (accessmadmin.moveToFirst()) {
            do {
                password_check3madmin = accessmadmin.getString(2);
            } while (accessmadmin.moveToNext());
        }


        final EditText editText = (EditText)view.findViewById(R.id.editText1);
        editText.setText(usernamee);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final Button button = (Button)view.findViewById(R.id.proceed_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals("") || editText.getText().toString().equals(" ")){
                    editText.setError("Field empty");
                }else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("name", String.valueOf(editText.getText().toString()));
                    String where1 = "_id = '1' ";
                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User6");
                    getActivity().getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                    resultUri = new Uri.Builder()
                            .scheme("content")
                            .authority(StubProviderApp.AUTHORITY)
                            .path("User6")
                            .appendQueryParameter("operation", "update")
                            .appendQueryParameter("_id","1")
                            .build();
                    getActivity().getContentResolver().notifyChange(resultUri, null);
//                    db.update("User6", contentValues, where1, new String[]{});

                    hideKeyboard(getContext());
                    donotshowKeyboard(getActivity());

                    Toast.makeText(getActivity(), "Name updated Successfully ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button button1 = (Button)view.findViewById(R.id.changeusername);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity(), R.style.notitle);
                dialog.setContentView(R.layout.dialog_change_username);

                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                Cursor access = db.rawQuery("SELECT * FROM User6 ", null);
                if (access.moveToFirst()) {
                    do {
                        username = access.getString(2);
                        password = access.getString(3);
                        usernamee = access.getString(1);


                    } while (access.moveToNext());
                }

                dialog.setCanceledOnTouchOutside(false);
                final EditText olduname = (EditText)dialog.findViewById(R.id.oldname);
                final EditText newuname = (EditText)dialog.findViewById(R.id.newname);
                final EditText confirmuname = (EditText)dialog.findViewById(R.id.confirmname);

//                ImageView imageView = (ImageView)dialog.findViewById(R.id.closetext);
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });

                olduname.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        olduname.setError(null);

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                newuname.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        newuname.setError(null);

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                confirmuname.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        confirmuname.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                ImageButton button2 = (ImageButton)dialog.findViewById(R.id.btncancel);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        hideKeyboard(getContext());
                        donotshowKeyboard(getActivity());
                    }
                });

                final ImageButton submit = (ImageButton) dialog.findViewById(R.id.btnsave);

                confirmuname.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_GO) {
                            submit.performClick();
                            return true;
                        }
                        return false;
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (olduname.getText().toString().equals("") || newuname.getText().toString().equals("") || confirmuname.getText().toString().equals("")) {
                            //Toast.makeText(getActivity(), "Incomplete details", Toast.LENGTH_SHORT).show();
                            if (olduname.getText().toString().equals("")){
                                olduname.setError("Field empty");
                            }
                            if (newuname.getText().toString().equals("")){
                                newuname.setError("Field empty");
                            }
                            if (confirmuname.getText().toString().equals("")){
                                confirmuname.setError("Field empty");
                            }
                        } else {
                            if (username.equals(olduname.getText().toString())) {

                                if (newuname.getText().toString().equals(confirmuname.getText().toString())) {

                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("username", String.valueOf(confirmuname.getText().toString()));
                                    String where1 = "_id = '1' ";

                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User6");
                                    getActivity().getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                                    resultUri = new Uri.Builder()
                                            .scheme("content")
                                            .authority(StubProviderApp.AUTHORITY)
                                            .path("User6")
                                            .appendQueryParameter("operation", "update")
                                            .appendQueryParameter("_id","1")
                                            .build();
                                    getActivity().getContentResolver().notifyChange(resultUri, null);

//                                    db.update("User6", contentValues, where1, new String[]{});
                                    Toast.makeText(getActivity(), "Username updated Successfully ", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                                    ContentValues contentValues1 = new ContentValues();
                                    contentValues1.put("username", String.valueOf(confirmuname.getText().toString()));
                                    String where2 = "_id = '6' ";
                                    contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User_privilege");
                                    getActivity().getContentResolver().update(contentUri, contentValues,where2,new String[]{});
                                    resultUri = new Uri.Builder()
                                            .scheme("content")
                                            .authority(StubProviderApp.AUTHORITY)
                                            .path("User_privilege")
                                            .appendQueryParameter("operation", "update")
                                            .appendQueryParameter("_id","6")
                                            .build();
                                    getActivity().getContentResolver().notifyChange(resultUri, null);
//                                    db.update("User_privilege", contentValues, where2, new String[]{});
                                    hideKeyboard(getContext());
                                    donotshowKeyboard(getActivity());

                                    hideKeyboard(getContext());
                                    donotshowKeyboard(getActivity());

                                } else {
                                    //Toast.makeText(getActivity(), "Confirm username not matching ", Toast.LENGTH_SHORT).show();
                                    confirmuname.setError("New username not matching");
                                }
                            } else {
                                //Toast.makeText(getActivity(), "wrong Old username", Toast.LENGTH_SHORT).show();
                                olduname.setError("Incorrect username");
                            }
                        }


                    }
                });

                dialog.show();
            }
        });


        /////////////////////////////////////////


        Button button2 = (Button)view.findViewById(R.id.changepassword);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity(), R.style.notitle);
                dialog.setContentView(R.layout.dialog_change_password);

                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                Cursor access = db.rawQuery("SELECT * FROM User6 ", null);
                if (access.moveToFirst()) {
                    do {
                        username = access.getString(2);
                        password = access.getString(3);
                        usernamee = access.getString(1);


                    } while (access.moveToNext());
                }

                dialog.setCanceledOnTouchOutside(false);
                final EditText oldpwd = (EditText)dialog.findViewById(R.id.oldpaswd);
                final EditText newpwd = (EditText)dialog.findViewById(R.id.newpaswd);
                final EditText confirmpwd = (EditText)dialog.findViewById(R.id.confirmpaswd);

//                ImageView imageView = (ImageView)dialog.findViewById(R.id.closetext);
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });

                ImageButton button2 = (ImageButton)dialog.findViewById(R.id.btncancel);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        hideKeyboard(getContext());
                        donotshowKeyboard(getActivity());
                    }
                });

                final ImageButton submit = (ImageButton) dialog.findViewById(R.id.btnsave);

                confirmpwd.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_GO) {
                            submit.performClick();
                            return true;
                        }
                        return false;
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (oldpwd.getText().toString().equals("") || newpwd.getText().toString().equals("") || confirmpwd.getText().toString().equals("")
                                || newpwd.getText().length()<4 || confirmpwd.getText().length()<4){
                            //Toast.makeText(getActivity(), "Incomplete details", Toast.LENGTH_SHORT).show();
                            if (oldpwd.getText().toString().equals("")){
                                oldpwd.setError("Field empty");
                            }
                            if (newpwd.getText().toString().equals("")){
                                newpwd.setError("Field empty");
                            }
                            if (confirmpwd.getText().toString().equals("")){
                                confirmpwd.setError("Field empty");
                            }
                            if (newpwd.getText().length()<4){
                                newpwd.setError("Enter 4 digits");
                            }
                            if (confirmpwd.getText().length()<4){
                                confirmpwd.setError("Enter 4 digits");
                            }
                        }else {
                            if (password.equals(oldpwd.getText().toString())){

                                if (newpwd.getText().toString().equals(confirmpwd.getText().toString())){

                                    if (password_check1.equals(confirmpwd.getText().toString())){
                                        //Toast.makeText(getActivity(), "Entered Password is already used", Toast.LENGTH_SHORT).show();
                                        newpwd.setError("Enter different password");
                                    }else if (password_check2.equals(confirmpwd.getText().toString())){
                                        //Toast.makeText(getActivity(), "Entered Password is already used", Toast.LENGTH_SHORT).show();
                                        newpwd.setError("Enter different password");
                                    }else if (password_check3.equals(confirmpwd.getText().toString())){
                                        //Toast.makeText(getActivity(), "Entered Password is already used", Toast.LENGTH_SHORT).show();
                                        newpwd.setError("Enter different password");
                                    }else if (password_check4.equals(confirmpwd.getText().toString())){
                                        //Toast.makeText(getActivity(), "Entered Password is already used", Toast.LENGTH_SHORT).show();
                                        newpwd.setError("Enter different password");
                                    }else if (password_check5.equals(confirmpwd.getText().toString())){
                                        //Toast.makeText(getActivity(), "Entered Password is already used", Toast.LENGTH_SHORT).show();
                                        newpwd.setError("Enter different password");
                                    }else if (password_checkladmin.equals(confirmpwd.getText().toString())){
                                        //Toast.makeText(getActivity(), "Entered Password is already used", Toast.LENGTH_SHORT).show();
                                        newpwd.setError("Enter different password");
                                    }else if (password_check3madmin.equals(confirmpwd.getText().toString())){
                                        //Toast.makeText(getActivity(), "Entered Password is already used", Toast.LENGTH_SHORT).show();
                                        newpwd.setError("Enter different password");
                                    }else {
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("password", String.valueOf(confirmpwd.getText().toString()));
                                        String where1 = "_id = '1' ";
                                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User6");
                                        getActivity().getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                                        resultUri = new Uri.Builder()
                                                .scheme("content")
                                                .authority(StubProviderApp.AUTHORITY)
                                                .path("User6")
                                                .appendQueryParameter("operation", "update")
                                                .appendQueryParameter("_id","1")
                                                .build();
                                        getActivity().getContentResolver().notifyChange(resultUri, null);
//                                        db.update("User6", contentValues, where1, new String[]{});
                                        Toast.makeText(getActivity(), "Password updated Successfully ", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();

                                        hideKeyboard(getContext());
                                        donotshowKeyboard(getActivity());
                                    }



                                }else {
                                    //Toast.makeText(getActivity(), "Confirm password not matching ", Toast.LENGTH_SHORT).show();
                                    confirmpwd.setError("New password not matching");
                                }
                            }else {
                                //Toast.makeText(getActivity(), "wrong Old password", Toast.LENGTH_SHORT).show();
                                oldpwd.setError("Incorrect password");
                            }
                        }



                    }
                });

                dialog.show();
            }
        });

        return view;
    }

    public String getSingleEntrymadminpass(String password)
    {
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor cursor=db.query("LOGIN", null, " PASSWORD=?", new String[]{password}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        return password;
    }

    public String getSingleEntryladminpass(String password)
    {
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor cursor=db.query("LAdmin", null, " password=?", new String[]{password}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        return password;
    }

    public String getSingleEntryUser1pass(String password)
    {
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor cursor=db.query("User1", null, " password=?", new String[]{password}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        return password;
    }

    public String getSingleEntryUser2pass(String password)
    {
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor cursor=db.query("User2", null, " password=?", new String[]{password}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        return password;
    }

    public String getSingleEntryUser3pass(String password)
    {
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor cursor=db.query("User3", null, " password=?", new String[]{password}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        return password;
    }

    public String getSingleEntryUser4pass(String password)
    {
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor cursor=db.query("User4", null, " password=?", new String[]{password}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        return password;
    }

    public String getSingleEntryUser5pass(String password)
    {
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor cursor=db.query("User5", null, " password=?", new String[]{password}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        return password;
    }

    public String getSingleEntryUser6pass(String password)
    {
        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor cursor=db.query("User6", null, " password=?", new String[]{password}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        return password;
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
}
