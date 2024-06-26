package com.intuition.ivepos;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Rohithkumar on 5/29/2015.
 */
public class LocalLoginActivity extends AppCompatActivity {

    private static final int ACTIVITY_CONSTANT = 1;
    LoginDataBaseAdapter loginDataBaseAdapter;
    Button gotolocal;
    Dialog dialog;

    String account_selection;
    String WebserviceUrl;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locallogin1);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(LocalLoginActivity.this);
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

        ImageView arrow = (ImageView) findViewById(R.id.leftarrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocalLoginActivity.this, ForgotPasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        Button question = (Button)findViewById(R.id.question);
        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(LocalLoginActivity.this , R.style.cust_dialog);
                dialog.setContentView(R.layout.ladminquestiondetails);
                dialog.setTitle(getString(R.string.title22));
                dialog.show();

                Button ok = (Button)dialog.findViewById(R.id.close);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        Button gotohome = (Button)findViewById(R.id.gotomainpage);
        gotohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (account_selection.toString().equals("Dine") || account_selection.toString().equals(getString(R.string.app_name))) {
                    Intent intent = new Intent(LocalLoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    if (account_selection.toString().equals("Qsr")) {
                        Intent intent = new Intent(LocalLoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(LocalLoginActivity.this, MainActivity_Retail.class);
                        startActivity(intent);
                        finish();
                    }
                }

//                Intent intent = new Intent(LocalLoginActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });
        loginDataBaseAdapter=new LoginDataBaseAdapter(LocalLoginActivity.this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        gotolocal = (Button)findViewById(R.id.login);

        final EditText editTextPassword = (EditText) findViewById(R.id.etPass);

        editTextPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    gotolocal.performClick();
                    return true;
                }
                return false;
            }
        });


        gotolocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editTextUserName = (EditText) findViewById(R.id.username);

                String userName = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();

                // fetch the Password form database for respective user name
                String storedPassword = loginDataBaseAdapter.getSinlgeEntryLocal(userName);
                if (password.equals(storedPassword)) {
                    Intent intent = new Intent(LocalLoginActivity.this, LadminPassRetrievalActivity.class);
                    startActivityForResult(intent);
                } else {
                    Toast.makeText(LocalLoginActivity.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void startActivityForResult(Intent intent) {
        startActivityForResult(new Intent(LocalLoginActivity.this, LadminPassRetrievalActivity.class), ACTIVITY_CONSTANT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTIVITY_CONSTANT)
        {
            finish();
        }
    }
}

