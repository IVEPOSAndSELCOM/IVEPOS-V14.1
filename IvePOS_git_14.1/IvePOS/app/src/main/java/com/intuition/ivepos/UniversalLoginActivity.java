package com.intuition.ivepos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
public class UniversalLoginActivity extends AppCompatActivity {

    private static final int ACTIVITY_CONSTANT = 1;
    LoginDataBaseAdapter loginDataBaseAdapter;
    public SQLiteDatabase db;
    Button gotouniversal;
    String uname = "intuition";
    String pass = "166251951";

    String account_selection;
    String WebserviceUrl;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universallogin1);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(UniversalLoginActivity.this);
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
                Intent intent = new Intent(UniversalLoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        Button gotohome = (Button)findViewById(R.id.gotomainpage);
        gotohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(UniversalLoginActivity.this, MainActivity.class);
//                startActivity(intent);

                if (account_selection.toString().equals("Dine") || account_selection.toString().equals(getString(R.string.app_name))) {
                    Intent intent = new Intent(UniversalLoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("from","home");
                    startActivity(intent);
                }else {
                    if (account_selection.toString().equals("Qsr")) {
                        Intent intent = new Intent(UniversalLoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("from","home");
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(UniversalLoginActivity.this, MainActivity_Retail.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("from","home");
                        startActivity(intent);
                    }
                }

//                Intent intent = new Intent(UniversalLoginActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("from","home");
//                startActivity(intent);
            }
        });

        loginDataBaseAdapter=new LoginDataBaseAdapter(UniversalLoginActivity.this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        gotouniversal = (Button)findViewById(R.id.login);

        final EditText editTextPassword = (EditText) findViewById(R.id.etPass);

        editTextPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    gotouniversal.performClick();
                    return true;
                }
                return false;
            }
        });


        gotouniversal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editTextUserName = (EditText) findViewById(R.id.username);

                String userName = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();

                // fetch the Password form database for respective user name
                String storedPassword = loginDataBaseAdapter.getSinlgeEntryUniversal(userName);
                if (editTextUserName.getText().toString().equals(uname) && editTextPassword.getText().toString().equals(pass)) {
                    Intent intent = new Intent(UniversalLoginActivity.this, UniversalPassRetrievalActivity.class);
                    startActivityForResult(intent);
                } else {
                    Toast.makeText(UniversalLoginActivity.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void startActivityForResult(Intent intent) {
        startActivityForResult(new Intent(UniversalLoginActivity.this, UniversalPassRetrievalActivity.class), ACTIVITY_CONSTANT);
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
