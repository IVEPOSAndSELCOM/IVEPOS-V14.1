package com.intuition.ivepos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity_Selection extends AppCompatActivity {

    RadioGroup radioGroupSplit;
    RadioButton radioBtnsplit, radio_dine, radio_qsr, radio_retail;
    LinearLayout layout_dine, layout_qsr, layout_retail;
    RadioGroup radioGroup1;
//    LinearLayout bottom;

    String selection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_selection);

        SharedPreferences pref1 = getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
        SharedPreferences.Editor editor = pref1.edit();
        editor.putString("account_selection", "");
        editor.commit();

//        android.widget.ImageView back_pressed = (ImageView) findViewById(R.id.back_pressed);
//        back_pressed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                startActivity(intent);
//
//                int pid = android.os.Process.myPid();
//                android.os.Process.killProcess(pid);
//            }
//        });

//        bottom = (LinearLayout) findViewById(R.id.bottom);

        layout_dine = (LinearLayout) findViewById(R.id.layout_dine);
        layout_qsr = (LinearLayout) findViewById(R.id.layout_qsr);
        layout_retail = (LinearLayout) findViewById(R.id.layout_retail);

        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);

        radio_dine = (RadioButton) findViewById(R.id.radio_dine);
        radio_qsr = (RadioButton) findViewById(R.id.radio_qsr);
        radio_retail = (RadioButton) findViewById(R.id.radio_retail);

        final int selected1 = radioGroup1.getCheckedRadioButtonId();
        if (selected1 == radio_dine.getId()) {
            radio_dine.setChecked(true);
            radio_qsr.setChecked(false);
            radio_retail.setChecked(false);
            selection = "Dine";
        }
        if (selected1 == radio_qsr.getId()) {
            radio_dine.setChecked(false);
            radio_qsr.setChecked(true);
            radio_retail.setChecked(false);
            selection = "Qsr";
        }
        if (selected1 == radio_retail.getId()) {
            radio_dine.setChecked(false);
            radio_qsr.setChecked(false);
            radio_retail.setChecked(true);
            selection = "Retail";
        }

        layout_dine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_dine.setChecked(true);
                radio_qsr.setChecked(false);
                radio_retail.setChecked(false);
                selection = "Dine";
            }
        });

        layout_qsr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_dine.setChecked(false);
                radio_qsr.setChecked(true);
                radio_retail.setChecked(false);
                selection = "Qsr";
            }
        });

        layout_retail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_dine.setChecked(false);
                radio_qsr.setChecked(false);
                radio_retail.setChecked(true);
                selection = "Retail";
            }
        });

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                final int selected1 = radioGroup1.getCheckedRadioButtonId();
                radioBtnsplit = (RadioButton) findViewById(selected1);

                if (selected1 == radio_dine.getId()) {
                    radio_dine.setChecked(true);
                    radio_qsr.setChecked(false);
                    radio_retail.setChecked(false);
                    selection = "Dine";
                }
                if (selected1 == radio_qsr.getId()) {
                    radio_dine.setChecked(false);
                    radio_qsr.setChecked(true);
                    radio_retail.setChecked(false);
                    selection = "Qsr";
                }
                if (selected1 == radio_retail.getId()) {
                    radio_dine.setChecked(false);
                    radio_qsr.setChecked(false);
                    radio_retail.setChecked(true);
                    selection = "Retail";
                }

            }
        });

        radio_dine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radio_dine.isChecked()) {
                    radio_qsr.setChecked(false);
                    radio_retail.setChecked(false);
//                    bottom.setVisibility(View.VISIBLE);
                    selection = "Dine";
                }
            }
        });

        radio_qsr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radio_qsr.isChecked()) {
                    radio_dine.setChecked(false);
                    radio_retail.setChecked(false);
//                    bottom.setVisibility(View.VISIBLE);
                    selection = "Qsr";
                }
            }
        });

        radio_retail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radio_retail.isChecked()) {
                    radio_dine.setChecked(false);
                    radio_qsr.setChecked(false);
//                    bottom.setVisibility(View.VISIBLE);
                    selection = "Retail";
                }
            }
        });

//        bottom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences pref1 = getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
//                SharedPreferences.Editor editor = pref1.edit();
//                editor.putString("account_selection", selection);
//                editor.commit();
//
//                Intent intent = new Intent(SplashScreenActivity_Selection.this, MainActivity_Signin.class);
//                startActivity(intent);
//            }
//        });

    }

    public static SharedPreferences getDefaultSharedPreferencesMultiProcess(
            Context context) {
        return context.getSharedPreferences(
                context.getPackageName() + "_preferences",
                Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
    }
}
