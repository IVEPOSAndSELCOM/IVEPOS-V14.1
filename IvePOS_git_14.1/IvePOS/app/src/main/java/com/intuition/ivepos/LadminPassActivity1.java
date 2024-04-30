package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 1/6/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
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

/**
 * Created by Rohithkumar on 1/6/2015.
 */
public class LadminPassActivity1 extends Fragment {


    public SQLiteDatabase db = null;
    EditText ladoldpass, ladnewpass, ladconfirmpass, ladusername, madminusername, madoldpass, madnewpass, madconfirmpass;
    Button ladsubmit;
    Cursor cursor, c;
    int id;

    Fragment frag;
    FragmentTransaction fragTransaction;

    String username, password, usernamee, password_check1, password_check2, password_check3, password_check4, password_check5, password_check6, password_checkladmin, password_check3madmin ;


    public LadminPassActivity1(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //ActionBar actionBar = getActivity().getActionBar();


        //actionBar.setTitle(" Local admin - Control Panel");

        View rootview = inflater.inflate(R.layout.admin_chg_pwd2, null);

        if (getActivity() instanceof AppCompatActivity){
            androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionbar.setSubtitle("Local admin");
        }

        LinearLayout re = (LinearLayout) rootview.findViewById(R.id.re);
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "hi", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), UserPrivilege.class);
                startActivity(intent);
            }
        });

        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        Cursor access = db.rawQuery("SELECT * FROM LAdmin ", null);
        if (access.moveToFirst()) {
            do {
                username = access.getString(3);
                password = access.getString(2);
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


        final EditText editText = (EditText)rootview.findViewById(R.id.editText1);
        editText.setText(username);


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


        Button button = (Button)rootview.findViewById(R.id.proceed_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals("") || editText.getText().toString().equals(" ")){
                    editText.setError("Field empty");
                }else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("name", String.valueOf(editText.getText().toString()));
                    String where1 = "_id = '1' ";
                    db.update("LAdmin", contentValues, where1, new String[]{});
                    hideKeyboard(getContext());
                    donotshowKeyboard(getActivity());
                    Toast.makeText(getActivity(), "Name updated Successfully ", Toast.LENGTH_SHORT).show();
                }

            }


        });


        Button button1 = (Button)rootview.findViewById(R.id.changeusername);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity(), R.style.notitle);
                dialog.setContentView(R.layout.dialog_change_username);

                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                Cursor access = db.rawQuery("SELECT * FROM LAdmin ", null);
                if (access.moveToFirst()) {
                    do {
                        username = access.getString(3);
                        password = access.getString(2);
                        usernamee = access.getString(1);


                    } while (access.moveToNext());
                }

                dialog.setCanceledOnTouchOutside(false);
                final EditText olduname = (EditText) dialog.findViewById(R.id.oldname);
                final EditText newuname = (EditText) dialog.findViewById(R.id.newname);
                final EditText confirmuname = (EditText) dialog.findViewById(R.id.confirmname);

//                ImageView imageView = (ImageView) dialog.findViewById(R.id.closetext);
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

                ImageButton button2 = (ImageButton) dialog.findViewById(R.id.btncancel);
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
                            if (usernamee.equals(olduname.getText().toString())) {

                                if (newuname.getText().toString().equals(confirmuname.getText().toString())) {

                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("username", String.valueOf(confirmuname.getText().toString()));
                                    String where1 = "_id = '1' ";
                                    db.update("LAdmin", contentValues, where1, new String[]{});
                                    Toast.makeText(getActivity(), "Username updated Successfully ", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
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


        Button button2 = (Button)rootview.findViewById(R.id.changepassword);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity(), R.style.notitle);
                dialog.setContentView(R.layout.dialog_change_password);

                db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
                Cursor access = db.rawQuery("SELECT * FROM LAdmin ", null);
                if (access.moveToFirst()) {
                    do {
                        username = access.getString(3);
                        password = access.getString(2);
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

                        } else {
                            if (password.equals(oldpwd.getText().toString())) {

                                if (newpwd.getText().toString().equals(confirmpwd.getText().toString())) {

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
                                    }else if (password_check6.equals(confirmpwd.getText().toString())){
                                        //Toast.makeText(getActivity(), "Entered Password is already used", Toast.LENGTH_SHORT).show();
                                        newpwd.setError("Enter different password");
                                    }else if (password_check3madmin.equals(confirmpwd.getText().toString())){
                                        //Toast.makeText(getActivity(), "Entered Password is already used", Toast.LENGTH_SHORT).show();
                                        newpwd.setError("Enter different password");
                                    }else {
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("password", String.valueOf(confirmpwd.getText().toString()));
                                        String where1 = "_id = '1' ";
                                        db.update("LAdmin", contentValues, where1, new String[]{});
                                        Toast.makeText(getActivity(), "Password updated Successfully ", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        hideKeyboard(getContext());
                                        donotshowKeyboard(getActivity());
                                    }



                                } else {
                                    //Toast.makeText(getActivity(), "Confirm password not matching ", Toast.LENGTH_SHORT).show();
                                    confirmpwd.setError("New password not matching");
                                }
                            } else {
                                //Toast.makeText(getActivity(), "wrong Old password", Toast.LENGTH_SHORT).show();
                                oldpwd.setError("Incorrect password");
                            }
                        }


                    }
                });

                dialog.show();
            }
        });


//        ladusername = (EditText)rootview.findViewById(R.id.etuserladold);
//        ladoldpass = (EditText)rootview.findViewById(R.id.etPassladold);
//        ladnewpass = (EditText)rootview.findViewById(R.id.etPassladnew);
//        ladconfirmpass = (EditText)rootview.findViewById(R.id.etPassladconfirm);
//
//
//        madminusername = (EditText)rootview.findViewById(R.id.etusermadold);
//        madoldpass = (EditText)rootview.findViewById(R.id.etPassold);
//        madnewpass = (EditText)rootview.findViewById(R.id.etPassnew);
//        madconfirmpass = (EditText)rootview.findViewById(R.id.etPassconfirm);
//
//
//
//        Button ladsubmit = (Button)rootview.findViewById(R.id.ladsubmit);
//        ladsubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String username = ladusername.getText().toString();
//                String oldpassword = ladoldpass.getText().toString();
//                String newpassword=ladnewpass.getText().toString();
//                String confirmPassword=ladconfirmpass.getText().toString();
//
//                String storedPassword=getSinlgeEntrypasss(oldpassword);
//
//                if(username.equals("")||oldpassword.equals("")||newpassword.equals("")||confirmPassword.equals(""))
//                {
//                    Toast.makeText(getActivity().getApplicationContext(), "Field Empty", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // check if both password matches
//                if(!newpassword.equals(confirmPassword))
//                {
//                    Toast.makeText(getActivity().getApplicationContext(), "New Password does not match", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if(oldpassword.equals(storedPassword))
//                {
//                    saveInDB();
//                }
//                else {
//                    Toast.makeText(getActivity(), "Old Password not matching", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//        });
//
//        //madmin credentialsl
//        Button madsubmit = (Button)rootview.findViewById(R.id.madsubmit);
//        madsubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String madusername = madminusername.getText().toString();
//                String madoldpassword = madoldpass.getText().toString();
//                String madnewpassword=madnewpass.getText().toString();
//                String madconfirmPassword=madconfirmpass.getText().toString();
//
//                String storedPasswordd=getSinlgeEntrypassmad(madoldpassword);
//
//                if(madusername.equals("")||madoldpassword.equals("")||madnewpassword.equals("")||madconfirmPassword.equals(""))
//                {
//                    Toast.makeText(getActivity().getApplicationContext(), "Field Empty", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // check if both password matches
//                if(!madnewpassword.equals(madconfirmPassword))
//                {
//                    Toast.makeText(getActivity().getApplicationContext(), "New Password does not match", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if(madoldpassword.equals(storedPasswordd))
//                {
//                    saveInmadDB();
//                }
//                else {
//                    Toast.makeText(getActivity(), "Old Password not matching", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//        });
//
//        try {
//
//            db = getActivity().openOrCreateDatabase("mydb", Context.MODE_PRIVATE,null);
//            crearYasignar(db);
//
//        }catch (SQLiteException e){
//            alertas("Error inesperado: " + e.getMessage());
//        }
//

//        RelativeLayout relativeLayout = (RelativeLayout)rootview.findViewById(R.id.masteradmin);
//        relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                frag = new MasterAdminActivity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });
//
//        RelativeLayout relativeLayout1 = (RelativeLayout)rootview.findViewById(R.id.localadmin);
//        relativeLayout1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                frag = new LocalAdminActivity();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });


        return rootview;
    }


    public void crearYasignar(SQLiteDatabase dbllega){
        try {

            dbllega.execSQL("CREATE TABLE if not exists LAdmin(_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, username text, password text);");

        }catch (SQLiteException e){
            alertas("Error desconocido: "+e.getMessage());
        }
    }

    public void alertas(String alerta) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme);
        AlertDialog.Builder builder = new AlertDialog.Builder(wrapper);
        builder.setIcon(R.drawable.icon);
        builder.setTitle(R.string.app_name);
        builder.setMessage(alerta);
        builder.create().show();
    }

    void saveInDB() {
        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                Context.MODE_PRIVATE, null);
        ContentValues newValues = new ContentValues();
        newValues.put("password", ladconfirmpass.getText().toString());
        newValues.put("username", ladusername.getText().toString());
        newValues.put("name", "ladmin");
        String where = "_id = '1'";
        db.update("LAdmin", newValues, where, new String[]{});
        Toast.makeText(getActivity(), "Account Successfully Updated ", Toast.LENGTH_SHORT).show();

    }

    void saveInmadDB() {
        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                Context.MODE_PRIVATE, null);
        ContentValues newValues = new ContentValues();
        newValues.put("PASSWORD", madconfirmpass.getText().toString());
        newValues.put("USERNAME", madminusername.getText().toString());
        newValues.put("name", "madmin");
        String where = "ID = '1'";
        db.update("LOGIN", newValues, where, new String[]{});
        Toast.makeText(getActivity(), "Account Successfully Updated ", Toast.LENGTH_SHORT).show();

    }

    public String getSinlgeEntrypasss(String password)
    {
        Cursor cursor=db.query("LAdmin", null, " password=?", new String[]{password}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        return password;
    }

    public String getSinlgeEntrypassmad(String passwordd)
    {
        Cursor cursor=db.query("LOGIN", null, " PASSWORD=?", new String[]{passwordd}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        return passwordd;
    }

    public static void donotshowKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
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


}