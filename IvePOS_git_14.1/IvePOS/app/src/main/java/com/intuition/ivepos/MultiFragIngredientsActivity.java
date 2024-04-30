package com.intuition.ivepos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MultiFragIngredientsActivity extends AppCompatActivity {

    public SQLiteDatabase db = null;
    String WebserviceUrl;
    String account_selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_multi_frag);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title28));

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menuContainer, new IngredientMenuFragment()).commit();
        }

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(MultiFragIngredientsActivity.this);
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){

            if (account_selection.toString().equals("Dine")) {
                Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                if (cursor3.moveToFirst()) {
                    String lite_pro = cursor3.getString(1);

                    TextView tv = new TextView(MultiFragIngredientsActivity.this);
                    tv.setText(lite_pro);

                    if (tv.getText().toString().equals("Lite")) {
                        Intent intent = new Intent(MultiFragIngredientsActivity.this, BeveragesMenuFragment_Dine_l.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(MultiFragIngredientsActivity.this, BeveragesMenuFragment_Dine.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }else {
                    Intent intent = new Intent(MultiFragIngredientsActivity.this, BeveragesMenuFragment_Dine_l.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }else {
                if (account_selection.toString().equals("Qsr")) {
                    Intent intent = new Intent(MultiFragIngredientsActivity.this, BeveragesMenuFragment_Qsr.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MultiFragIngredientsActivity.this, BeveragesMenuFragment_Retail.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

//            Intent intent = new Intent(MultiFragIngredientsActivity.this, BeveragesMenuFragment_Dine.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (account_selection.toString().equals("Dine")) {
            Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
            if (cursor3.moveToFirst()) {
                String lite_pro = cursor3.getString(1);

                TextView tv = new TextView(MultiFragIngredientsActivity.this);
                tv.setText(lite_pro);

                if (tv.getText().toString().equals("Lite")) {
                    Intent intent = new Intent(MultiFragIngredientsActivity.this, BeveragesMenuFragment_Dine_l.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MultiFragIngredientsActivity.this, BeveragesMenuFragment_Dine.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }else {
                Intent intent = new Intent(MultiFragIngredientsActivity.this, BeveragesMenuFragment_Dine_l.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }else {
            if (account_selection.toString().equals("Qsr")) {
                Intent intent = new Intent(MultiFragIngredientsActivity.this, BeveragesMenuFragment_Qsr.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else {
                Intent intent = new Intent(MultiFragIngredientsActivity.this, BeveragesMenuFragment_Retail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

//        Intent intent = new Intent(MultiFragIngredientsActivity.this, BeveragesMenuFragment_Dine.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> list= fragmentManager.getFragments();
        Fragment fragment = fragmentManager.findFragmentByTag("DatabaseActivity");
        if (fragment != null)
        {
            ((DatabaseitemActivity)fragment).onActivityResult(requestCode, resultCode,data);
        }

    }


}