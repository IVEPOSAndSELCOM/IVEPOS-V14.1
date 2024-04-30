package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 1/6/2015.
 */

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.intuition.ivepos.syncapp.StubProviderApp;

/**
 * Created by Rohithkumar on 1/6/2015.
 */
public class UserPassActivity extends Fragment {

    public SQLiteDatabase db = null;
    EditText useronename, oneuserusername, oneuseroldpass, oneusernewpass, oneuserconfirmpass;
    EditText usertwoname, twouserusername, twouseroldpass, twousernewpass, twouserconfirmpass;
    EditText userthreename, threeuserusername, threeuseroldpass, threeusernewpass, threeuserconfirmpass;
    EditText userfourname, fouruserusername, fouruseroldpass, fourusernewpass, fouruserconfirmpass;
    EditText userfivename, fiveuserusername, fiveuseroldpass, fiveusernewpass, fiveuserconfirmpass;
    EditText usersixname, sixuserusername, sixuseroldpass, sixusernewpass, sixuserconfirmpass;
    Uri contentUri,resultUri;
    public UserPassActivity(){


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar actionBar = getActivity().getActionBar();


        actionBar.setTitle(getString(R.string.title33));

        View rootview = inflater.inflate(R.layout.user_chg_pwd, null);

//userone access
        useronename = (EditText)rootview.findViewById(R.id.etuseronename);
        SQLiteDatabase myDb;
        myDb = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor cur = myDb.query("User1", null, null, null, null, null, null);
        cur.moveToFirst();
        while (cur.isAfterLast() == false) {
            useronename.append(cur.getString(1));
            cur.moveToNext();
        }
        //String useronehi = useronename.getText().toString();
        oneuserusername = (EditText)rootview.findViewById(R.id.etuseroneusername);
        oneuseroldpass = (EditText)rootview.findViewById(R.id.etuseroneoldpass);
        oneusernewpass = (EditText)rootview.findViewById(R.id.etuseronenewpass);
        oneuserconfirmpass = (EditText)rootview.findViewById(R.id.etuseroneconfirmpass);

        ImageButton userone = (ImageButton)rootview.findViewById(R.id.etuseronesubmit);
        userone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useronehi = useronename.getText().toString();
                String username = oneuserusername.getText().toString();
                String oldpassword = oneuseroldpass.getText().toString();
                String newpassword=oneusernewpass.getText().toString();
                String confirmPassword=oneuserconfirmpass.getText().toString();

                String storedPassword=getSinlgeEntrypassuserone(oldpassword);

                if(useronehi.equals("")||username.equals("")||oldpassword.equals("")||newpassword.equals("")||confirmPassword.equals(""))
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Field Empty", Toast.LENGTH_SHORT).show();
                }
                // check if both password matches
                if(!newpassword.equals(confirmPassword))
                {
                    Toast.makeText(getActivity().getApplicationContext(), "New Password does not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(oldpassword.equals(storedPassword))
                {
                    saveInuseroneDB();
                }
                else {
                    Toast.makeText(getActivity(), "Old Password not matching", Toast.LENGTH_SHORT).show();
                }
            }
        });


//usertwo access
        usertwoname = (EditText)rootview.findViewById(R.id.etusertwoname);
        myDb = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        cur = myDb.query("User2", null, null, null, null, null, null);
        cur.moveToFirst();
        while (cur.isAfterLast() == false) {
            usertwoname.append(cur.getString(1));
            cur.moveToNext();
        }
        //String useronehi = useronename.getText().toString();
        twouserusername = (EditText)rootview.findViewById(R.id.etusertwousername);
        twouseroldpass = (EditText)rootview.findViewById(R.id.etusertwooldpass);
        twousernewpass = (EditText)rootview.findViewById(R.id.etusertwonewpass);
        twouserconfirmpass = (EditText)rootview.findViewById(R.id.etusertwoconfirmpass);

        ImageButton usertwo = (ImageButton)rootview.findViewById(R.id.etusertwosubmit);
        usertwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usertwohi = usertwoname.getText().toString();
                String twousername = twouserusername.getText().toString();
                String twooldpassword = twouseroldpass.getText().toString();
                String twonewpassword=twousernewpass.getText().toString();
                String twoconfirmPassword=twouserconfirmpass.getText().toString();

                String storedPassword=getSinlgeEntrypassusertwo(twooldpassword);

                if(usertwohi.equals("")||twousername.equals("")||twooldpassword.equals("")||twonewpassword.equals("")||twoconfirmPassword.equals(""))
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Field Empty", Toast.LENGTH_SHORT).show();
                }
                // check if both password matches
                if(!twonewpassword.equals(twoconfirmPassword))
                {
                    Toast.makeText(getActivity().getApplicationContext(), "New Password does not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(twooldpassword.equals(storedPassword))
                {
                    saveInusertwoDB();
                }
                else {
                    Toast.makeText(getActivity(), "Old Password not matching", Toast.LENGTH_SHORT).show();
                }
            }
        });


//userthree access
        userthreename = (EditText)rootview.findViewById(R.id.userthreename);
        myDb = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        cur = myDb.query("User3", null, null, null, null, null, null);
        cur.moveToFirst();
        while (cur.isAfterLast() == false) {
            userthreename.append(cur.getString(1));
            cur.moveToNext();
        }
        //String useronehi = useronename.getText().toString();
        threeuserusername = (EditText)rootview.findViewById(R.id.userthreeusername);
        threeuseroldpass = (EditText)rootview.findViewById(R.id.userthreeoldpass);
        threeusernewpass = (EditText)rootview.findViewById(R.id.userthreenewpass);
        threeuserconfirmpass = (EditText)rootview.findViewById(R.id.userthreeconfirmpass);

        ImageButton userthree = (ImageButton)rootview.findViewById(R.id.userthreesubmit);
        userthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userthreehi = userthreename.getText().toString();
                String threeusername = threeuserusername.getText().toString();
                String threeoldpassword = threeuseroldpass.getText().toString();
                String threenewpassword=threeusernewpass.getText().toString();
                String threeconfirmPassword=threeuserconfirmpass.getText().toString();

                String storedPassword=getSinlgeEntrypassuserthree(threeoldpassword);

                if(userthreehi.equals("")||threeusername.equals("")||threeoldpassword.equals("")||threenewpassword.equals("")||threeconfirmPassword.equals(""))
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Field Empty", Toast.LENGTH_SHORT).show();
                }
                // check if both password matches
                if(!threenewpassword.equals(threeconfirmPassword))
                {
                    Toast.makeText(getActivity().getApplicationContext(), "New Password does not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(threeoldpassword.equals(storedPassword))
                {
                    saveInuserthreeDB();
                }
                else {
                    Toast.makeText(getActivity(), "Old Password not matching", Toast.LENGTH_SHORT).show();
                }
            }
        });


//userfour access
        userfourname = (EditText)rootview.findViewById(R.id.userfourname);
        myDb = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        cur = myDb.query("User4", null, null, null, null, null, null);
        cur.moveToFirst();
        while (cur.isAfterLast() == false) {
            userfourname.append(cur.getString(1));
            cur.moveToNext();
        }
        //String useronehi = useronename.getText().toString();
        fouruserusername = (EditText)rootview.findViewById(R.id.userfourusername);
        fouruseroldpass = (EditText)rootview.findViewById(R.id.userfouroldpass);
        fourusernewpass = (EditText)rootview.findViewById(R.id.userfournewpass);
        fouruserconfirmpass = (EditText)rootview.findViewById(R.id.userfourconfirmpass);

        ImageButton userfour = (ImageButton)rootview.findViewById(R.id.userfoursubmit);
        userfour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userfourhi = userfourname.getText().toString();
                String fourusername = fouruserusername.getText().toString();
                String fouroldpassword = fouruseroldpass.getText().toString();
                String fournewpassword=fourusernewpass.getText().toString();
                String fourconfirmPassword=fouruserconfirmpass.getText().toString();

                String storedPassword=getSinlgeEntrypassuserfour(fouroldpassword);

                if(userfourhi.equals("")||fourusername.equals("")||fouroldpassword.equals("")||fournewpassword.equals("")||fourconfirmPassword.equals(""))
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Field Empty", Toast.LENGTH_SHORT).show();
                }
                // check if both password matches
                if(!fournewpassword.equals(fourconfirmPassword))
                {
                    Toast.makeText(getActivity().getApplicationContext(), "New Password does not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(fouroldpassword.equals(storedPassword))
                {
                    saveInuserfourDB();
                }
                else {
                    Toast.makeText(getActivity(), "Old Password not matching", Toast.LENGTH_SHORT).show();
                }
            }
        });


//userfive access
        userfivename = (EditText)rootview.findViewById(R.id.userfivename);
        myDb = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        cur = myDb.query("User5", null, null, null, null, null, null);
        cur.moveToFirst();
        while (cur.isAfterLast() == false) {
            userfivename.append(cur.getString(1));
            cur.moveToNext();
        }
        //String useronehi = useronename.getText().toString();
        fiveuserusername = (EditText)rootview.findViewById(R.id.userfiveusername);
        fiveuseroldpass = (EditText)rootview.findViewById(R.id.userfiveoldpass);
        fiveusernewpass = (EditText)rootview.findViewById(R.id.userfivenewpass);
        fiveuserconfirmpass = (EditText)rootview.findViewById(R.id.userfiveconfirmpass);

        ImageButton userfive = (ImageButton)rootview.findViewById(R.id.userfivesubmit);
        userfive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userfivehi = userfivename.getText().toString();
                String fiveusername = fiveuserusername.getText().toString();
                String fiveoldpassword = fiveuseroldpass.getText().toString();
                String fivenewpassword=fiveusernewpass.getText().toString();
                String fiveconfirmPassword=fiveuserconfirmpass.getText().toString();

                String storedPassword=getSinlgeEntrypassuserfive(fiveoldpassword);

                if(userfivehi.equals("")||fiveusername.equals("")||fiveoldpassword.equals("")||fivenewpassword.equals("")||fiveconfirmPassword.equals(""))
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Field Empty", Toast.LENGTH_SHORT).show();
                }
                // check if both password matches
                if(!fivenewpassword.equals(fiveconfirmPassword))
                {
                    Toast.makeText(getActivity().getApplicationContext(), "New Password does not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(fiveoldpassword.equals(storedPassword))
                {
                    saveInuserfiveDB();
                }
                else {
                    Toast.makeText(getActivity(), "Old Password not matching", Toast.LENGTH_SHORT).show();
                }
            }
        });


//usersix access
        usersixname = (EditText)rootview.findViewById(R.id.usersixname);
        myDb = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        cur = myDb.query("User6", null, null, null, null, null, null);
        cur.moveToFirst();
        while (cur.isAfterLast() == false) {
            usersixname.append(cur.getString(1));
            cur.moveToNext();
        }
        //String useronehi = useronename.getText().toString();
        sixuserusername = (EditText)rootview.findViewById(R.id.usersixusername);
        sixuseroldpass = (EditText)rootview.findViewById(R.id.usersixoldpass);
        sixusernewpass = (EditText)rootview.findViewById(R.id.usersixnewpass);
        sixuserconfirmpass = (EditText)rootview.findViewById(R.id.usersixconfirmpass);

        ImageButton usersix = (ImageButton)rootview.findViewById(R.id.usersixsubmit);
        usersix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usersixhi = usersixname.getText().toString();
                String sixusername = sixuserusername.getText().toString();
                String sixoldpassword = sixuseroldpass.getText().toString();
                String sixnewpassword=sixusernewpass.getText().toString();
                String sixconfirmPassword=sixuserconfirmpass.getText().toString();

                String storedPassword=getSinlgeEntrypassusersix(sixoldpassword);

                if(usersixhi.equals("")||sixusername.equals("")||sixoldpassword.equals("")||sixnewpassword.equals("")||sixconfirmPassword.equals(""))
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Field Empty", Toast.LENGTH_SHORT).show();
                }
                // check if both password matches
                if(!sixnewpassword.equals(sixconfirmPassword))
                {
                    Toast.makeText(getActivity().getApplicationContext(), "New Password does not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(sixoldpassword.equals(storedPassword))
                {
                    saveInusersixDB();
                }
                else {
                    Toast.makeText(getActivity(), "Old Password not matching", Toast.LENGTH_SHORT).show();
                }
            }
        });




        try {

            db = getActivity().openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);


        }catch (SQLiteException e){
            alertas("Error inesperado: " + e.getMessage());
        }



        return rootview;
    }


    public void alertas(String alerta) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme);
        AlertDialog.Builder builder = new AlertDialog.Builder(wrapper);
        builder.setIcon(R.drawable.icon);
        builder.setTitle(R.string.app_name);
        builder.setMessage(alerta);
        builder.create().show();
    }

    void saveInuseroneDB() {
        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                Context.MODE_PRIVATE, null);
        ContentValues newValues = new ContentValues();
        newValues.put("name", useronename.getText().toString());
        newValues.put("password", oneuserconfirmpass.getText().toString());
        newValues.put("username", oneuserusername.getText().toString());
        String where = "_id = '1'";
        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User1");
        getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("User1")
                .appendQueryParameter("operation", "update")
                .appendQueryParameter("_id","1")
                .build();
        getActivity().getContentResolver().notifyChange(resultUri, null);
//        db.update("User1", newValues, where, new String[]{});
        Toast.makeText(getActivity(), "Account Successfully Updated ", Toast.LENGTH_SHORT).show();

    }
    public String getSinlgeEntrypassuserone(String passwordd)
    {
        Cursor cursor=db.query("User1", null, " password=?", new String[]{passwordd}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        return passwordd;
    }


    void saveInusertwoDB() {
        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                Context.MODE_PRIVATE, null);
        ContentValues newValues = new ContentValues();
        newValues.put("name", usertwoname.getText().toString());
        newValues.put("password", twouserconfirmpass.getText().toString());
        newValues.put("username", twouserusername.getText().toString());
        String where = "_id = '1'";
        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User2");
        getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("User2")
                .appendQueryParameter("operation", "update")
                .appendQueryParameter("_id","1")
                .build();
        getActivity().getContentResolver().notifyChange(resultUri, null);
//        db.update("User2", newValues, where, new String[]{});
        Toast.makeText(getActivity(), "Account Successfully Updated ", Toast.LENGTH_SHORT).show();

    }
    public String getSinlgeEntrypassusertwo(String passwordd)
    {
        Cursor cursor=db.query("User2", null, " password=?", new String[]{passwordd}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        return passwordd;
    }


    void saveInuserthreeDB() {
        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                Context.MODE_PRIVATE, null);
        ContentValues newValues = new ContentValues();
        newValues.put("name", userthreename.getText().toString());
        newValues.put("password", threeuserconfirmpass.getText().toString());
        newValues.put("username", threeuserusername.getText().toString());
        String where = "_id = '1'";
        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User3");
        getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("User3")
                .appendQueryParameter("operation", "update")
                .appendQueryParameter("_id","1")
                .build();
        getActivity().getContentResolver().notifyChange(resultUri, null);
//        db.update("User3", newValues, where, new String[]{});
        Toast.makeText(getActivity(), "Account Successfully Updated ", Toast.LENGTH_SHORT).show();

    }
    public String getSinlgeEntrypassuserthree(String passwordd)
    {
        Cursor cursor=db.query("User3", null, " password=?", new String[]{passwordd}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        return passwordd;
    }


    void saveInuserfourDB() {
        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                Context.MODE_PRIVATE, null);
        ContentValues newValues = new ContentValues();
        newValues.put("name", userfourname.getText().toString());
        newValues.put("password", fouruserconfirmpass.getText().toString());
        newValues.put("username", fouruserusername.getText().toString());
        String where = "_id = '1'";
        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User4");
        getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("User4")
                .appendQueryParameter("operation", "update")
                .appendQueryParameter("_id","1")
                .build();
        getActivity().getContentResolver().notifyChange(resultUri, null);
//        db.update("User4", newValues, where, new String[]{});
        Toast.makeText(getActivity(), "Account Successfully Updated ", Toast.LENGTH_SHORT).show();

    }
    public String getSinlgeEntrypassuserfour(String passwordd)
    {
        Cursor cursor=db.query("User4", null, " password=?", new String[]{passwordd}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        return passwordd;
    }


    void saveInuserfiveDB() {
        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                Context.MODE_PRIVATE, null);
        ContentValues newValues = new ContentValues();
        newValues.put("name", userfivename.getText().toString());
        newValues.put("password", fiveuserconfirmpass.getText().toString());
        newValues.put("username", fiveuserusername.getText().toString());
        String where = "_id = '1'";
        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User5");
        getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("User5")
                .appendQueryParameter("operation", "update")
                .appendQueryParameter("_id","1")
                .build();
        getActivity().getContentResolver().notifyChange(resultUri, null);
//        db.update("User5", newValues, where, new String[]{});
        Toast.makeText(getActivity(), "Account Successfully Updated ", Toast.LENGTH_SHORT).show();

    }
    public String getSinlgeEntrypassuserfive(String passwordd)
    {
        Cursor cursor=db.query("User5", null, " password=?", new String[]{passwordd}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        return passwordd;
    }


    void saveInusersixDB() {
        SQLiteDatabase myDb = getActivity().openOrCreateDatabase("mydb_Appdata",
                Context.MODE_PRIVATE, null);
        ContentValues newValues = new ContentValues();
        newValues.put("name", usersixname.getText().toString());
        newValues.put("password", sixuserconfirmpass.getText().toString());
        newValues.put("username", sixuserusername.getText().toString());
        String where = "_id = '1'";

        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User6");
        getActivity().getContentResolver().update(contentUri, newValues,where,new String[]{});
        resultUri = new Uri.Builder()
                .scheme("content")
                .authority(StubProviderApp.AUTHORITY)
                .path("User6")
                .appendQueryParameter("operation", "update")
                .appendQueryParameter("_id","1")
                .build();
        getActivity().getContentResolver().notifyChange(resultUri, null);

//        db.update("User6", newValues, where, new String[]{});
        Toast.makeText(getActivity(), "Account Successfully Updated ", Toast.LENGTH_SHORT).show();

    }
    public String getSinlgeEntrypassusersix(String passwordd)
    {
        Cursor cursor=db.query("User6", null, " password=?", new String[]{passwordd}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        return passwordd;
    }




}
