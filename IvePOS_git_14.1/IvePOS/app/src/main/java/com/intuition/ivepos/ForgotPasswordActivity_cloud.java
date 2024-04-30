package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 4/2/2018.
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgotPasswordActivity_cloud extends AppCompatActivity {

    private EditText passwordInput;
    private EditText codeInput;
    private EditText confirmpasswordInput;
    private Button setPassword;
    private AlertDialog userDialog;
    String dest="";
    String password="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        init();
    }

    public void forgotPassword(View view) {
        getCode();
    }

    private void init(){

        Bundle extras = getIntent().getExtras();
        if (extras !=null) {
            if (extras.containsKey("destination")) {
                dest = extras.getString("destination");
                String delMed = extras.getString("deliveryMed");
                TextView message = (TextView) findViewById(R.id.textViewForgotPasswordMessage);
                String textToDisplay = "Code to set a new password was sent to " + dest + " via "+delMed;
                message.setText(textToDisplay);
            }
        }

        passwordInput = (EditText) findViewById(R.id.editTextForgotPasswordPass);
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewForgotPasswordUserIdLabel);
                    label.setText(passwordInput.getHint());
//                    passwordInput.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewForgotPasswordUserIdMessage);
                label.setText(" ");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewForgotPasswordUserIdLabel);
                    label.setText("");
                }
            }
        });

        confirmpasswordInput = (EditText) findViewById(R.id.editTextForgotConfirmPassword);
        confirmpasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {

                    TextView label = (TextView) findViewById(R.id.textViewForgotConfirmPassword);
                    label.setText(confirmpasswordInput.getHint());
//                    passwordInput.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewForgotConfirmPasswordCodeMessage);
                label.setText(" ");
            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (s.length() == 0) {
//                    TextView label = (TextView) findViewById(R.id.textViewForgotConfirmPassword);
//                    label.setText("");
//                }
                TextView label = (TextView) findViewById(R.id.textViewForgotConfirmPassword);

                String strPass1 = passwordInput.getText().toString();
                String strPass2 = confirmpasswordInput.getText().toString();

                if (strPass1.equals(strPass2)) {
                    Toast.makeText(ForgotPasswordActivity_cloud.this, "Passwords Match", Toast.LENGTH_SHORT).show();


                } else {
                    confirmpasswordInput.setError("Please enter the correct new password");

//                    Toast.makeText(ForgotPasswordActivity.this, "Passwords Does not Match", Toast.LENGTH_SHORT).show();
                }
            }
        });


        codeInput = (EditText) findViewById(R.id.editTextForgotPasswordCode);
        codeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewForgotPasswordCodeLabel);
                    label.setText(codeInput.getHint());
//                    codeInput.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewForgotPasswordCodeMessage);
                label.setText(" ");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewForgotPasswordCodeLabel);
                    label.setText("");
                }
            }
        });
    }

    private void getCode() {
        String newPassword = passwordInput.getText().toString();

        if (newPassword == null || newPassword.length() < 1) {
            TextView label = (TextView) findViewById(R.id.textViewForgotPasswordUserIdMessage);
            label.setText(passwordInput.getHint() + " cannot be empty");
//            passwordInput.setBackground(getDrawable(R.drawable.text_border_error));
            return;
        }

        String verCode = codeInput.getText().toString();

        if (verCode == null || verCode.length() < 1) {
            TextView label = (TextView) findViewById(R.id.textViewForgotPasswordCodeMessage);
            label.setText(codeInput.getHint() + " cannot be empty");
//            codeInput.setBackground(getDrawable(R.drawable.text_border_error));
            return;
        }
        String confCode = confirmpasswordInput.getText().toString();

        if (confCode == null || confCode.length() < 1) {
            TextView label = (TextView) findViewById(R.id.textViewForgotConfirmPasswordCodeMessage);
            label.setText(confirmpasswordInput.getHint() + " cannot be empty");
//            confirmpasswordInput.setBackground(getDrawable(R.drawable.text_border_error));
            return;
        }

        exit(newPassword,confCode,verCode);
    }

    private void exit(String newPass,String confPass,String code) {
        Intent intent = new Intent();
        if(newPass == null ||confPass == null || code == null) {
            newPass = "";
            confPass = "";
            code = "";
        }
        intent.putExtra("newPass", newPass);
        intent.putExtra("confPass", confPass);
        intent.putExtra("code", code);
        intent.putExtra("destination", dest);
        intent.putExtra("password", passwordInput.getText().toString());


        setResult(RESULT_OK, intent);
        finish();
    }
}
