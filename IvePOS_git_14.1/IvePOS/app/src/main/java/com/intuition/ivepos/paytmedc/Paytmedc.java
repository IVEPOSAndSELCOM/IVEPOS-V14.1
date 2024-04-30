package com.intuition.ivepos.paytmedc;

import android.content.Context;
import android.content.Intent;

public class Paytmedc {

    public void startPayment(Context ctx,String amount,String orderId) {
        String EDC_PACKAGE = "com.paytm.pos"; // If you are using Debug EDC App then
        // Use "com.paytm.pos."
        String packageName = ctx.getPackageName(); // Package name of your Application
        String PAY_DEEP_LINK = "sampledeeplink://payment"; // It should be different for
        // different operations :-Payment, Status & Void
        String callbackAction = "com.paytm.pos.payment.CALL_BACK_RESULT"; //This action
        // is same as defined in the manifest file, if this field is null then
        // you will get the result in Launcher Activity of your application
       // String orderId = "123456789"; // Make sure this is unique for every payment
        String payMode = "All"; // Possible values are "CARD", "QR" & "ALL", this field is optional
       // String amount = "1200"; //1200 means ?12.00, last two digits are for paise
        String param1 = "val1"; // Optional additional parameter, which if sent in request will be
        // returned as it is in response. There can be any number of
        // additional parameters, and their name can be anything, but
        // make sure their name does not match with any of the
        // Pre-Defined parameters(which will be described later)
        String param2 = "val2"; // Optional additional parameter
        String deepLink = "paytmedc://paymentV2?" + "callbackAction=com.paytm.pos.payment.CALL_BACK_RESULT" + "&stackClear=true" + "&callbackPkg=" + packageName + "&callbackDl=" + PAY_DEEP_LINK
                + "&requestPayMode=" + payMode + "&orderId=" + orderId + "&amount=" + amount +
                "&param1=" + param1 + "&param2=" + param2;
        Intent launchIntent = ctx.getPackageManager().getLaunchIntentForPackage(EDC_PACKAGE);
        if (launchIntent != null) {
            launchIntent.putExtra("deeplink", deepLink);
            ctx.startActivity(launchIntent);
        }


    }
}
