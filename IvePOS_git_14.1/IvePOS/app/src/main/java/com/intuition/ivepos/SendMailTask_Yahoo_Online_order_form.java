package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 12/26/2016.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

public class SendMailTask_Yahoo_Online_order_form extends AsyncTask {

    private ProgressDialog statusDialog;
    private Activity sendMailActivity;

    public SendMailTask_Yahoo_Online_order_form(Activity activity) {
        sendMailActivity = activity;

    }

    protected void onPreExecute() {
        statusDialog = new ProgressDialog(sendMailActivity);
        statusDialog.setMessage(sendMailActivity.getString(R.string.setmessage32));
        statusDialog.setIndeterminate(false);
        statusDialog.setCancelable(false);
        statusDialog.show();
    }

    @Override
    protected Object doInBackground(Object... args) {
        try {
            Log.i("SendMailTask_Yahoo", "About to instantiate Yahoo...");
            publishProgress("Processing input....");
            Yahoo_Online_order_form androidEmail = new Yahoo_Online_order_form(args[0].toString(),
                    args[1].toString(),
                    (List) args[2],
                    args[3].toString(),
                    args[4].toString(), args[5].toString(), args[6].toString());
            publishProgress("Preparing mail message....");
            androidEmail.createEmailMessage();
            publishProgress("Sending email....");
            try {
                androidEmail.sendEmail();
            }catch (Exception e){
                Log.i("SendMailTask_Yahoo", "Mail not Sent.");
            }

            publishProgress("Email Sent.");
            Log.i("SendMailTask_Yahoo", "Mail Sent.");
        } catch (Exception e) {
            publishProgress(e.getMessage());
            Log.e("SendMailTask_Yahoo", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void onProgressUpdate(Object... values) {
        statusDialog.setMessage(values[0].toString());

    }

    @Override
    public void onPostExecute(Object result) {
        statusDialog.dismiss();
    }

}

