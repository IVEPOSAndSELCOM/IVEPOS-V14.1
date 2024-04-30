package com.intuition.ivepos.paytmedc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.intuition.ivepos.BeveragesMenuFragment_Dine;
import com.intuition.ivepos.R;

import androidx.annotation.Nullable;

public class PaytmEdcActivity extends Activity {
    String amount ="",billnumber ="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edc);

        amount = getIntent().getStringExtra("amount");
        billnumber = getIntent().getStringExtra("billnumber");

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        new Paytmedc().startPayment(PaytmEdcActivity.this, amount, ts+billnumber);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.hasExtra("deeplink")) {
            String dl = intent.getStringExtra("deeplink");
            Uri uri = Uri.parse(dl);
            switch (uri.getHost()) {
                case "payment":
                    String amount = uri.getQueryParameter("amount");
                    String orderId = uri.getQueryParameter("orderId");
                    String status = uri.getQueryParameter("status");


                    Intent resultintent=new Intent();
                    resultintent.putExtra("MESSAGE",status);
                    setResult(7,resultintent);
                    finish();//finishing activity


            }
        }
    }

    private void startPayment() {
        String EDC_PACKAGE = "com.paytm.pos"; // If you are using Debug EDC App then
        // Use "com.paytm.pos."
        String packageName = getPackageName(); // Package name of your Application
        String PAY_DEEP_LINK = "sampledeeplink://payment"; // It should be different for
        // different operations :-Payment, Status & Void
        String callbackAction = "com.paytm.pos.payment.CALL_BACK_RESULT"; //This action
        // is same as defined in the manifest file, if this field is null then
        // you will get the result in Launcher Activity of your application
        String orderId = "123456789"; // Make sure this is unique for every payment
        String payMode = "All"; // Possible values are "CARD", "QR" & "ALL", this field is optional
        String amount = "1200"; //1200 means ?12.00, last two digits are for paise
        String param1 = "val1"; // Optional additional parameter, which if sent in request will be
        // returned as it is in response. There can be any number of
        // additional parameters, and their name can be anything, but
        // make sure their name does not match with any of the
// Pre-Defined parameters(which will be described later)
        String param2 = "val2"; // Optional additional parameter
        String deepLink = "paytmedc://paymentV2?" + "callbackAction=com.paytm.pos.payment.CALL_BACK_RESULT" + "&stackClear=true" + "&callbackPkg=" + packageName + "&callbackDl=" + PAY_DEEP_LINK
                + "&requestPayMode=" + payMode + "&orderId=" + orderId + "&amount=" + amount +
"&param1=" + param1 + "&param2=" + param2;
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(EDC_PACKAGE);
        if (launchIntent != null) {
            launchIntent.putExtra("deeplink", deepLink);
            startActivity(launchIntent);
        }


    }


}
