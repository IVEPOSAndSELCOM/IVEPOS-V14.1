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
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Rohithkumar on 1/6/2015.
 */
public class MasterAdminActivity extends Fragment {


    Fragment frag;
    FragmentTransaction fragTransaction;

    LoginDataBaseAdapter loginDataBaseAdapter;
    SimpleCursorAdapter dataAdapter, dataadapter;
    ListView listView;
    public SQLiteDatabase db = null;
    public Cursor cursor, cursors;
    private EditText empNameEtxt;
    private LinearLayout submitLayout;
    protected SQLiteDatabase database;
    private static final String WHERE_ID_EQUALS = "_id"+ " =?";
    DatabasecategoryActivity databasecategorycativity;
    private String name;
    ContentValues cv;
    DataBaseHelper dbhelper;
    public Spinner spinner;
    TextView textView;

    ArrayList<String> list = new ArrayList<String>();
    String path[] = {"Laptops","DesktopPC","Tablets","Add-Ons","Gaming"};

    public static ArrayList<String> ArrayofName = new ArrayList<String>();
    public static final String ARG_ITEM_ID = "Create_category_fragment";

    public MasterAdminActivity(){
        super();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        View rootview = inflater.inflate(R.layout.fragment_multi_admin, null);
        //final ActionBar actionBar = getActivity().getActionBar();
        if (getActivity() instanceof AppCompatActivity){
            androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionbar.setSubtitle("Master admin");
        }

        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);

        //actionBar.setTitle("Master admin - Control panel");




    return rootview;


    }


    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);

        final ImageView btnadmin = (ImageView)getActivity().findViewById(R.id.btngoadmin);

        final EditText editText = (EditText)getActivity().findViewById(R.id.etPass);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
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
            public void onClick(View view) {

                final EditText editTextPassword = (EditText) getActivity().findViewById(R.id.etPass);

                String password = editTextPassword.getText().toString();


                String storedPassword = loginDataBaseAdapter.getSinlgeEntrypass(password);
                String storedpasswordlad = loginDataBaseAdapter.getSinlgeEntryladpass(password);
                if (password.equals(storedPassword)) {
                    frag = new AdminPassActivity();
                    hideKeyboard(getContext());
                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                    fragTransaction.commit();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("Status", "yes");
                    String where = "_id = '1'";
                    db.update("Menulogin_checking", contentValues, where, new String[]{});

                    db.delete("UserLogin_Checking", null, null);
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("password", editTextPassword.getText().toString());
                    contentValues1.put("name", "madmin");
                    db.insert("UserLogin_Checking", null, contentValues1);
                } else {
                    //Toast.makeText(getActivity(), "User Name or Password does not match", Toast.LENGTH_LONG).show();
                    editTextPassword.setError("Incorrect password");
                }
            }
        });


    }



    public String getSinlgeEntrypassmad(String passwordd) {
        Cursor cursor=db.query("LOGIN", null, " PASSWORD=?", new String[]{passwordd}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        return passwordd;
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
