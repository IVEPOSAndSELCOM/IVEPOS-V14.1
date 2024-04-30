package com.intuition.ivepos.razorpay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.intuition.ivepos.BeveragesMenuFragment_Dine;
import com.intuition.ivepos.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class RazorPayQRCodeActivity extends Activity {

    String image_url;

    ImageView imgURL;
    ProgressBar pbbar;

    AlertDialog alertDialogItems;

    String account_id = "", event = "", id = "", order_id = "", description = "", rrn = "", upi_transaction_id = "";

    int i = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_qrcode_activity);

        Bundle extras = getIntent().getExtras();
        image_url = extras.getString("url");

        System.out.println("response result1 "+ image_url);

        imgURL = findViewById(R.id.imgURL);
        pbbar = findViewById(R.id.pbbar);

        pbbar.setVisibility(View.GONE);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("myFunction1"));

        LoadImageURL loadImageURL = new LoadImageURL();
        loadImageURL.execute("");
    }

    private class LoadImageURL extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
            imgURL.setVisibility(View.GONE);
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                URL url = new URL(image_url);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                Log.e(getString(R.string.error_title), e.getMessage());
                e.printStackTrace();
                bmp = null;
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null)

                imgURL.setImageBitmap(result);
            imgURL.setScaleType(ImageView.ScaleType.CENTER_CROP);
            pbbar.setVisibility(View.GONE);
            imgURL.setVisibility(View.VISIBLE);
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getExtras().getString("items1")!=null){
                alertDialogItems = new AlertDialog.Builder(RazorPayQRCodeActivity.this).create();
                alertDialogItems.setTitle(getString(R.string.title42));
                alertDialogItems.setMessage(getString(R.string.setmessage12));
                if (i == 0) {
                    alertDialogItems.show();
                    i = 1;
                }else {

                }


                System.out.println("received message is "+intent.getExtras().getString("items1"));


                try {
                    JSONObject emp=(new JSONObject(intent.getExtras().getString("items1")));
                    account_id=emp.getString("account_id");
                    event=emp.getString("event");
//                    System.out.println("received message is "+account_id+" "+event);

                    JSONObject details =emp.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");
                    id=details.getString("id");
                    order_id=details.getString("order_id");
                    description=details.getString("description");
//                    System.out.println("received message is "+id+" "+order_id+" "+description);

                    JSONObject acquirer_data =details.getJSONObject("acquirer_data");
                    rrn=acquirer_data.getString("rrn");
//                    upi_transaction_id=acquirer_data.getString("upi_transaction_id");
//                    System.out.println("received message is "+rrn+" "+upi_transaction_id);

                    done(event);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
    };
    public void done(String event) {

        TextView account_id1 = new TextView(RazorPayQRCodeActivity.this);
        account_id1.setText(account_id);
        TextView event1 = new TextView(RazorPayQRCodeActivity.this);
        event1.setText(event);
        TextView id1 = new TextView(RazorPayQRCodeActivity.this);
        id1.setText(id);
        TextView order_id1 = new TextView(RazorPayQRCodeActivity.this);
        order_id1.setText(order_id);
        TextView description1 = new TextView(RazorPayQRCodeActivity.this);
        description1.setText(description);
        TextView rrn1 = new TextView(RazorPayQRCodeActivity.this);
        rrn1.setText(rrn);
        TextView upi_transaction_id1 = new TextView(RazorPayQRCodeActivity.this);
        upi_transaction_id1.setText(upi_transaction_id);

        if (account_id1.getText().toString().equals("")) {
            account_id = "";
        }
        if (event1.getText().toString().equals("")) {
            event = "";
        }
        if (id1.getText().toString().equals("")) {
            id = "";
        }
        if (order_id1.getText().toString().equals("")) {
            order_id = "";
        }
        if (description1.getText().toString().equals("")) {
            description = "";
        }
        if (rrn1.getText().toString().equals("")) {
            rrn = "";
        }
        if (upi_transaction_id1.getText().toString().equals("")) {
            upi_transaction_id = "";
        }

        Intent intent=new Intent();
        intent.putExtra("MESSAGE",event);
        intent.putExtra("account_id",account_id);
        intent.putExtra("event",event);
        intent.putExtra("id",id);
        intent.putExtra("order_id",order_id);
        intent.putExtra("description",description);
        intent.putExtra("rrn",rrn);
        intent.putExtra("upi_transaction_id",upi_transaction_id);
        setResult(6,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("MESSAGE",getString(R.string.setmessage6));

//        intent.putExtra("account_id",account_id);
//        intent.putExtra("event",event);
//        intent.putExtra("id",id);
//        intent.putExtra("order_id",order_id);
//        intent.putExtra("description",description);
//        intent.putExtra("rrn",rrn);
//        intent.putExtra("upi_transaction_id",upi_transaction_id);

        setResult(6,intent);
        finish();

    }

}
