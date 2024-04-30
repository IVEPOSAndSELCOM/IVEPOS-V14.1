package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 1/6/2015.
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Rohithkumar on 1/6/2015.
 */
public class UserFragment extends Fragment {

    Fragment frag;
    FragmentTransaction fragTransaction;
    LoginDataBaseAdapter loginDataBaseAdapter;

    public SQLiteDatabase db = null;
    String username, password, usernamee1, usernamee2, usernamee3, usernamee4, usernamee5, usernamee6;


    public UserFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootview = inflater.inflate(R.layout.six_users_click, null);

        if (getActivity() instanceof AppCompatActivity){
            androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionbar.setSubtitle("User");
        }

        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);
        Cursor access = db.rawQuery("SELECT * FROM User1 ", null);
        if (access.moveToFirst()) {
            do {
                usernamee1 = access.getString(1);
                username = access.getString(2);
                password = access.getString(3);

            } while (access.moveToNext());
        }

        Cursor access2 = db.rawQuery("SELECT * FROM User2 ", null);
        if (access2.moveToFirst()) {
            do {
                usernamee2 = access2.getString(1);
                username = access2.getString(2);
                password = access2.getString(3);

            } while (access2.moveToNext());
        }

        Cursor access3 = db.rawQuery("SELECT * FROM User3 ", null);
        if (access3.moveToFirst()) {
            do {
                usernamee3 = access3.getString(1);
                username = access3.getString(2);
                password = access3.getString(3);

            } while (access3.moveToNext());
        }

        Cursor access4 = db.rawQuery("SELECT * FROM User4 ", null);
        if (access4.moveToFirst()) {
            do {
                usernamee4 = access4.getString(1);
                username = access4.getString(2);
                password = access4.getString(3);

            } while (access4.moveToNext());
        }

        Cursor access5 = db.rawQuery("SELECT * FROM User5 ", null);
        if (access5.moveToFirst()) {
            do {
                usernamee5 = access5.getString(1);
                username = access5.getString(2);
                password = access5.getString(3);

            } while (access5.moveToNext());
        }

        Cursor access6 = db.rawQuery("SELECT * FROM User6 ", null);
        if (access6.moveToFirst()) {
            do {
                usernamee6 = access6.getString(1);
                username = access6.getString(2);
                password = access6.getString(3);

            } while (access6.moveToNext());
        }

        TextView textView = (TextView)rootview.findViewById(R.id.user1name);
        textView.setText(usernamee1);
        TextView textView2 = (TextView)rootview.findViewById(R.id.user2name);
        textView2.setText(usernamee2);
        TextView textView3 = (TextView)rootview.findViewById(R.id.user3name);
        textView3.setText(usernamee3);
        TextView textView4 = (TextView)rootview.findViewById(R.id.user4name);
        textView4.setText(usernamee4);
        TextView textView5 = (TextView)rootview.findViewById(R.id.user5name);
        textView5.setText(usernamee5);
        TextView textView6 = (TextView)rootview.findViewById(R.id.user6name);
        textView6.setText(usernamee6);


        RelativeLayout relativeLayout = (RelativeLayout)rootview.findViewById(R.id.userone);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag = new user1();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
            }
        });

        RelativeLayout relativeLayout2 = (RelativeLayout)rootview.findViewById(R.id.usertwo);
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag = new user2();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
            }
        });

        RelativeLayout relativeLayout3 = (RelativeLayout)rootview.findViewById(R.id.userthree);
        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag = new user3();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
            }
        });

        RelativeLayout relativeLayout4 = (RelativeLayout)rootview.findViewById(R.id.userfour);
        relativeLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag = new user4();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
            }
        });

        RelativeLayout relativeLayout5 = (RelativeLayout)rootview.findViewById(R.id.userfive);
        relativeLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag = new user5();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
            }
        });

        RelativeLayout relativeLayout6 = (RelativeLayout)rootview.findViewById(R.id.usersix);
        relativeLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag = new user6();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
            }
        });

        return rootview;
    }

/////////user1

    public static class user1 extends Fragment{
        public SQLiteDatabase db = null;
        public user1(){

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootview1 = inflater.inflate(R.layout.fragment_multi_userall, null);

            LoginDataBaseAdapter loginDataBaseAdapter;

            loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
            loginDataBaseAdapter.open();

            loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
            loginDataBaseAdapter=loginDataBaseAdapter.open();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

            final EditText editText = (EditText)rootview1.findViewById(R.id.etPass);
            final ImageView imageView = (ImageView) rootview1.findViewById(R.id.btngouser);

            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_GO) {
                        imageView.performClick();
                        return true;
                    }
                    return false;
                }
            });


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String password = editText.getText().toString();

                    LoginDataBaseAdapter loginDataBaseAdapter;
                    loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
                    loginDataBaseAdapter=loginDataBaseAdapter.open();

                    String storedPassword = loginDataBaseAdapter.getSinlgeEntrypass(password);
                    String storedpasswordlad = loginDataBaseAdapter.getSinlgeEntryladpass(password);
                    String storedpassworduser1 = getSingleEntryUser1pass(password);
                    if (password.equals(storedPassword)) {
                        Fragment frag;
                        frag = new UserPass1Activity();
                        hideKeyboard(getContext());
                        FragmentTransaction ft= getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        ft.commit();

                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        db.delete("UserLogin_Checking", null, null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("password", editText.getText().toString());
                        contentValues.put("name", "madmin");
                        db.insert("UserLogin_Checking", null, contentValues);

                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("Status", "yes");
                        String where = "_id = '1'";
                        db.update("Menulogin_checking", contentValues1, where, new String[]{});

                        //Toast.makeText(getActivity(), " " + editText.getText().toString(), Toast.LENGTH_SHORT).show();

                    } else if (password.equals(storedpasswordlad)) {
                        Fragment frag;
                        frag = new UserPass1Activity();
                        hideKeyboard(getContext());
                        FragmentTransaction ft= getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        ft.commit();

                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        db.delete("UserLogin_Checking", null, null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("password", editText.getText().toString());
                        contentValues.put("name", "ladmin");
                        db.insert("UserLogin_Checking", null, contentValues);

                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("Status", "yes");
                        String where = "_id = '1'";
                        db.update("Menulogin_checking", contentValues1, where, new String[]{});
                        //Toast.makeText(getActivity(), " " + editText.getText().toString(), Toast.LENGTH_SHORT).show();

                    } else if (password.equals(storedpassworduser1)) {
                        Fragment frag;
                        frag = new UserPass1Activity();
                        hideKeyboard(getContext());
                        FragmentTransaction ft= getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        ft.commit();

                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        db.delete("UserLogin_Checking", null, null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("password", editText.getText().toString());
                        contentValues.put("name", "User1");
                        db.insert("UserLogin_Checking", null, contentValues);

                        //Toast.makeText(getActivity(), " " + editText.getText().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        editText.setError("Incorrect password");
                    }

                }
            });

//            fragTransaction.commit();

            return  rootview1;
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

    }

////////user2
     public static class user2 extends Fragment{
        public SQLiteDatabase db = null;
        public user2(){

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootview1 = inflater.inflate(R.layout.fragment_multi_userall, null);

            LoginDataBaseAdapter loginDataBaseAdapter;

            loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
            loginDataBaseAdapter.open();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

            final EditText editText = (EditText)rootview1.findViewById(R.id.etPass);
            final ImageView imageView = (ImageView)rootview1.findViewById(R.id.btngouser);

            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_GO) {
                        imageView.performClick();
                        return true;
                    }
                    return false;
                }
            });


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String password=editText.getText().toString();


                    LoginDataBaseAdapter loginDataBaseAdapter;
                    loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
                    loginDataBaseAdapter=loginDataBaseAdapter.open();


                    String storedPassword=loginDataBaseAdapter.getSinlgeEntrypass(password);
                    String storedpasswordlad = loginDataBaseAdapter.getSinlgeEntryladpass(password);
                    String storedpassworduser1 = getSingleEntryUser2pass(password);
                    if(password.equals(storedPassword)) {
                        Fragment frag;
                        frag = new UserPass2Activity();
                        hideKeyboard(getContext());
                        FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        ft.commit();

                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        db.delete("UserLogin_Checking", null, null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("password", editText.getText().toString());
                        contentValues.put("name", "madmin");
                        db.insert("UserLogin_Checking", null, contentValues);

                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("Status", "yes");
                        String where = "_id = '1'";
                        db.update("Menulogin_checking", contentValues1, where, new String[]{});

                        //Toast.makeText(getActivity(), " "+editText.getText().toString(), Toast.LENGTH_SHORT).show();

                    }
                    else if(password.equals(storedpasswordlad)){
                        Fragment frag;
                        frag = new UserPass2Activity();
                        hideKeyboard(getContext());
                        FragmentTransaction ft= getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        ft.commit();

                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        db.delete("UserLogin_Checking", null, null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("password", editText.getText().toString());
                        contentValues.put("name", "ladmin");
                        db.insert("UserLogin_Checking", null, contentValues);

                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("Status", "yes");
                        String where = "_id = '1'";
                        db.update("Menulogin_checking", contentValues1, where, new String[]{});

                        //Toast.makeText(getActivity(), " "+editText.getText().toString(), Toast.LENGTH_SHORT).show();

                    }else if (password.equals(storedpassworduser1)){
                        Fragment frag;
                        frag = new UserPass2Activity();
                        hideKeyboard(getContext());
                        FragmentTransaction ft= getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        ft.commit();

                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        db.delete("UserLogin_Checking", null, null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("password", editText.getText().toString());
                        contentValues.put("name", "User2");
                        db.insert("UserLogin_Checking", null, contentValues);

                        //Toast.makeText(getActivity(), " "+editText.getText().toString(), Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        editText.setError("Incorrect password");
                    }

                }
            });

//            fragTransaction.commit();

            return  rootview1;
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

    }

////////user3
public static class user3 extends Fragment{
        public SQLiteDatabase db = null;
        public user3(){

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootview1 = inflater.inflate(R.layout.fragment_multi_userall, null);

            LoginDataBaseAdapter loginDataBaseAdapter;

            loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
            loginDataBaseAdapter.open();

            loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
            loginDataBaseAdapter=loginDataBaseAdapter.open();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

            final EditText editText = (EditText)rootview1.findViewById(R.id.etPass);
            final ImageView imageView = (ImageView)rootview1.findViewById(R.id.btngouser);

            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_GO) {
                        imageView.performClick();
                        return true;
                    }
                    return false;
                }
            });


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String password=editText.getText().toString();


                    LoginDataBaseAdapter loginDataBaseAdapter;
                    loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
                    loginDataBaseAdapter=loginDataBaseAdapter.open();
                    String storedPassword=loginDataBaseAdapter.getSinlgeEntrypass(password);
                    String storedpasswordlad = loginDataBaseAdapter.getSinlgeEntryladpass(password);
                    String storedpassworduser1 = getSingleEntryUser3pass(password);
                    if(password.equals(storedPassword)) {
                        Fragment frag;
                        frag = new UserPass3Activity();
                        hideKeyboard(getContext());
                        FragmentTransaction ft= getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        ft.commit();

                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        db.delete("UserLogin_Checking", null, null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("password", editText.getText().toString());
                        contentValues.put("name", "madmin");
                        db.insert("UserLogin_Checking", null, contentValues);

                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("Status", "yes");
                        String where = "_id = '1'";
                        db.update("Menulogin_checking", contentValues1, where, new String[]{});

                    }
                    else if(password.equals(storedpasswordlad)){
                        Fragment frag;
                        frag = new UserPass3Activity();
                        hideKeyboard(getContext());
                        FragmentTransaction ft= getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        ft.commit();

                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        db.delete("UserLogin_Checking", null, null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("password", editText.getText().toString());
                        contentValues.put("name", "ladmin");
                        db.insert("UserLogin_Checking", null, contentValues);

                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("Status", "yes");
                        String where = "_id = '1'";
                        db.update("Menulogin_checking", contentValues1, where, new String[]{});

                    }else if (password.equals(storedpassworduser1)){
                        Fragment frag;
                        frag = new UserPass3Activity();
                        hideKeyboard(getContext());
                        FragmentTransaction ft= getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        ft.commit();

                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        db.delete("UserLogin_Checking", null, null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("password", editText.getText().toString());
                        contentValues.put("name", "User3");
                        db.insert("UserLogin_Checking", null, contentValues);

                    }
                    else
                    {
                        editText.setError("Incorrect password");
                    }

                }
            });

//            fragTransaction.commit();

            return  rootview1;
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

    }

////////user4
public static class user4 extends Fragment{
        public SQLiteDatabase db = null;
        public user4(){

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootview1 = inflater.inflate(R.layout.fragment_multi_userall, null);

            LoginDataBaseAdapter loginDataBaseAdapter;

            loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
            loginDataBaseAdapter.open();

            loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
            loginDataBaseAdapter=loginDataBaseAdapter.open();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

            final EditText editText = (EditText)rootview1.findViewById(R.id.etPass);
            final ImageView imageView = (ImageView)rootview1.findViewById(R.id.btngouser);

            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_GO) {
                        imageView.performClick();
                        return true;
                    }
                    return false;
                }
            });


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String password=editText.getText().toString();

                    LoginDataBaseAdapter loginDataBaseAdapter;
                    loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
                    loginDataBaseAdapter=loginDataBaseAdapter.open();


                    String storedPassword=loginDataBaseAdapter.getSinlgeEntrypass(password);
                    String storedpasswordlad = loginDataBaseAdapter.getSinlgeEntryladpass(password);
                    String storedpassworduser1 = getSingleEntryUser4pass(password);
                    if(password.equals(storedPassword)) {
                        Fragment frag;
                        frag = new UserPass4Activity();
                        hideKeyboard(getContext());
                        FragmentTransaction ft= getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        ft.commit();

                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        db.delete("UserLogin_Checking", null, null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("password", editText.getText().toString());
                        contentValues.put("name", "madmin");
                        db.insert("UserLogin_Checking", null, contentValues);

                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("Status", "yes");
                        String where = "_id = '1'";
                        db.update("Menulogin_checking", contentValues1, where, new String[]{});

                    }
                    else if(password.equals(storedpasswordlad)){
                        Fragment frag;
                        frag = new UserPass4Activity();
                        hideKeyboard(getContext());
                        FragmentTransaction ft= getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        ft.commit();

                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        db.delete("UserLogin_Checking", null, null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("password", editText.getText().toString());
                        contentValues.put("name", "ladmin");
                        db.insert("UserLogin_Checking", null, contentValues);

                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("Status", "yes");
                        String where = "_id = '1'";
                        db.update("Menulogin_checking", contentValues1, where, new String[]{});

                    }else if (password.equals(storedpassworduser1)){
                        Fragment frag;
                        frag = new UserPass4Activity();
                        hideKeyboard(getContext());
                        FragmentTransaction ft= getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        ft.commit();

                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        db.delete("UserLogin_Checking", null, null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("password", editText.getText().toString());
                        contentValues.put("name", "User4");
                        db.insert("UserLogin_Checking", null, contentValues);

                    }
                    else
                    {
                        editText.setError("Incorrect password");
                    }

                }
            });

//            fragTransaction.commit();

            return  rootview1;
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

    }

////////user5
       public static class user5 extends Fragment{
        public SQLiteDatabase db = null;
        public user5(){

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootview1 = inflater.inflate(R.layout.fragment_multi_userall, null);

            LoginDataBaseAdapter loginDataBaseAdapter;

            loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
            loginDataBaseAdapter.open();

            loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
            loginDataBaseAdapter=loginDataBaseAdapter.open();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

            final EditText editText = (EditText)rootview1.findViewById(R.id.etPass);
            final ImageView imageView = (ImageView)rootview1.findViewById(R.id.btngouser);

            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_GO) {
                        imageView.performClick();
                        return true;
                    }
                    return false;
                }
            });


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String password=editText.getText().toString();

                    LoginDataBaseAdapter loginDataBaseAdapter;
                    loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
                    loginDataBaseAdapter=loginDataBaseAdapter.open();


                    String storedPassword=loginDataBaseAdapter.getSinlgeEntrypass(password);
                    String storedpasswordlad = loginDataBaseAdapter.getSinlgeEntryladpass(password);
                    String storedpassworduser1 = getSingleEntryUser5pass(password);
                    if(password.equals(storedPassword)) {
                        Fragment frag;
                        frag = new UserPass5Activity();
                        hideKeyboard(getContext());
                        FragmentTransaction ft= getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        ft.commit();

                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        db.delete("UserLogin_Checking", null, null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("password", editText.getText().toString());
                        contentValues.put("name", "madmin");
                        db.insert("UserLogin_Checking", null, contentValues);

                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("Status", "yes");
                        String where = "_id = '1'";
                        db.update("Menulogin_checking", contentValues1, where, new String[]{});

                    }
                    else if(password.equals(storedpasswordlad)){
                        Fragment frag;
                        frag = new UserPass5Activity();
                        hideKeyboard(getContext());
                        FragmentTransaction ft= getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        ft.commit();

                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        db.delete("UserLogin_Checking", null, null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("password", editText.getText().toString());
                        contentValues.put("name", "ladmin");
                        db.insert("UserLogin_Checking", null, contentValues);

                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("Status", "yes");
                        String where = "_id = '1'";
                        db.update("Menulogin_checking", contentValues1, where, new String[]{});

                    }else if (password.equals(storedpassworduser1)){
                        Fragment frag;
                        frag = new UserPass5Activity();
                        hideKeyboard(getContext());
                        FragmentTransaction ft= getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        ft.commit();

                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        db.delete("UserLogin_Checking", null, null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("password", editText.getText().toString());
                        contentValues.put("name", "User5");
                        db.insert("UserLogin_Checking", null, contentValues);

                    }
                    else
                    {
                        editText.setError("Incorrect password");
                    }

                }
            });

//            fragTransaction.commit();

            return  rootview1;
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

    }

////////user6
    public static class user6 extends Fragment{
        public SQLiteDatabase db = null;
        public user6(){

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootview1 = inflater.inflate(R.layout.fragment_multi_userall, null);

            LoginDataBaseAdapter loginDataBaseAdapter;

            loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
            loginDataBaseAdapter.open();

            loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
            loginDataBaseAdapter=loginDataBaseAdapter.open();

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

            final EditText editText = (EditText)rootview1.findViewById(R.id.etPass);
            final ImageView imageView = (ImageView)rootview1.findViewById(R.id.btngouser);

            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_GO) {
                        imageView.performClick();
                        return true;
                    }
                    return false;
                }
            });


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String password=editText.getText().toString();

                    LoginDataBaseAdapter loginDataBaseAdapter;
                    loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
                    loginDataBaseAdapter=loginDataBaseAdapter.open();


                    String storedPassword=loginDataBaseAdapter.getSinlgeEntrypass(password);
                    String storedpasswordlad = loginDataBaseAdapter.getSinlgeEntryladpass(password);
                    String storedpassworduser1 = getSingleEntryUser6pass(password);
                    if(password.equals(storedPassword)) {
                        Fragment frag;
                        frag = new UserPass6Activity();
                        hideKeyboard(getContext());
                        FragmentTransaction ft= getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        ft.commit();

                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        db.delete("UserLogin_Checking", null, null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("password", editText.getText().toString());
                        contentValues.put("name", "madmin");
                        db.insert("UserLogin_Checking", null, contentValues);

                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("Status", "yes");
                        String where = "_id = '1'";
                        db.update("Menulogin_checking", contentValues1, where, new String[]{});

                    }
                    else if(password.equals(storedpasswordlad)){
                        Fragment frag;
                        frag = new UserPass6Activity();
                        hideKeyboard(getContext());
                        FragmentTransaction ft= getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        ft.commit();

                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        db.delete("UserLogin_Checking", null, null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("password", editText.getText().toString());
                        contentValues.put("name", "ladmin");
                        db.insert("UserLogin_Checking", null, contentValues);

                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("Status", "yes");
                        String where = "_id = '1'";
                        db.update("Menulogin_checking", contentValues1, where, new String[]{});

                    }else if (password.equals(storedpassworduser1)){
                        Fragment frag;
                        frag = new UserPass6Activity();
                        hideKeyboard(getContext());
                        FragmentTransaction ft= getFragmentManager().beginTransaction().replace(R.id.container, frag);
                        ft.commit();

                        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                        db.delete("UserLogin_Checking", null, null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("password", editText.getText().toString());
                        contentValues.put("name", "User6");
                        db.insert("UserLogin_Checking", null, contentValues);



                    }
                    else
                    {
                        editText.setError("Incorrect password");
                    }

                }
            });

//            fragTransaction.commit();

            return  rootview1;
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



//    public void onActivityCreated(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onActivityCreated(savedInstanceState);
//
//        loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
//        loginDataBaseAdapter=loginDataBaseAdapter.open();
//
//        ImageView btnuser = (ImageView)getActivity().findViewById(R.id.btngouser);
//
//        btnuser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                final EditText editTextPassword=(EditText)getActivity().findViewById(R.id.etPass);
//
//                String password=editTextPassword.getText().toString();
//
//
//                String storedPassword=loginDataBaseAdapter.getSinlgeEntrypass(password);
//                String storedpasswordlad = loginDataBaseAdapter.getSinlgeEntryladpass(password);
//                if(password.equals(storedPassword)) {
//                    frag = new UserPassActivity();
//                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                    fragTransaction.commit();
//                }
//                else if(password.equals(storedpasswordlad)){
//                    frag = new UserPassActivity();
//                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                    fragTransaction.commit();
//                }
//                else
//                {
//                    Toast.makeText(getActivity(), "User Name or Password does not match", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//
//    }

}
