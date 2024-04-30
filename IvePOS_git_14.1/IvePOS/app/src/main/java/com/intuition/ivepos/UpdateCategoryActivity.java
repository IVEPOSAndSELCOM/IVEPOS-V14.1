package com.intuition.ivepos;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Rohithkumar on 4/13/2015.
 */
public class UpdateCategoryActivity extends FragmentActivity {


    EditText et;
    Button edit_bt, delete_bt;

    long member_id;

    LoginDataBaseAdapter dbcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_update_category);


    }

    public void gotoCategory(View v) {

        Intent intent = new Intent(this, DatabasecategoryActivity.class);
        startActivity(intent);



    }




}