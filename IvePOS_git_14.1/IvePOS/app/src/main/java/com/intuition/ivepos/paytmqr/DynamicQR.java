package com.intuition.ivepos.paytmqr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.intuition.ivepos.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;



import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import com.imin.image.ILcdManager;

public class DynamicQR extends Activity {

   public static String url = "https://securegw-stage.paytm.in/paymentservices/qr/create";

  //  public static String url = "https://securegw.paytm.in/paymentservices/qr/create";
    RequestQueue queue;
    ImageView imageView;
    public SQLiteDatabase db = null;
    String MID="",Merchant_key="",PosID="",amount="";
    Button getTxnStatus;
    int statusCount=0;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(getResources().getBoolean(R.bool.portrait_only)){
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
        setContentView(R.layout.paytm_sample);



          dialog = new ProgressDialog(DynamicQR.this, R.style.timepicker_date_dialog);
        dialog.setMessage("Loading");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        imageView= findViewById(R.id.imageView);

        getTxnStatus= findViewById(R.id.getTxnStatus);
        getTxnStatus.setVisibility(View.INVISIBLE);



        queue = Volley.newRequestQueue(DynamicQR.this);

        amount = getIntent().getStringExtra("amount");

        double f = Double.parseDouble(amount);
        amount = String.format("%.2f", new BigDecimal(f));

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor c1 = db.rawQuery("SELECT * FROM PaytmMerchantReg WHERE Account = 'Registered'", null);
        if (c1.moveToFirst()) {
            do {
                String account = c1.getString(1);
                String MerchantName = c1.getString(2);
                //  String guid = c1.getString(3);
                 MID = c1.getString(4);
                 Merchant_key = c1.getString(5);
                 PosID = c1.getString(6);
//                    Thread.sleep(1000);

            } while (c1.moveToNext());

        }
        c1.close();


        getchecksum();

//        Button button11 = findViewById(R.id.button11);
//
//        button11.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//
//            }
//        });

    }



    public void getQR(String billno, String amount,String checksum){



            JSONObject headjson = new JSONObject();
            try {
                headjson.put("clientId", "C11");
                headjson.put("version", "v1");
                headjson.put("requestTimestamp", "TIME");
             //   headjson.put("channelId", "WEB");
                headjson.put("signature", checksum);

            } catch (JSONException e) {
                e.printStackTrace();
            }
           JSONObject bodyjson = new JSONObject();

          try {

              bodyjson.put("mid", "Intuit20065743662825");
              bodyjson.put("orderId", "OREDRID98765");
              bodyjson.put("amount", amount);
              bodyjson.put("businessType", "UPI_QR_CODE");
           //   bodyjson.put("displayName", "BrandName");
           //   bodyjson.put("expiryDate", "Intuit20065743662825");
              bodyjson.put("posId", "S12_123");
              bodyjson.put("orderDetails", "true");
//              bodyjson.put("imageRequired", "Intuit20065743662825");
//              bodyjson.put("productDetails", "Intuit20065743662825");
//              bodyjson.put("productId", "Intuit20065743662825");
//              bodyjson.put("productType", "Intuit20065743662825");
//              bodyjson.put("contactPhoneNo", "Intuit20065743662825");
//              bodyjson.put("comment", "Intuit20065743662825");
//              bodyjson.put("inventoryCount", "Intuit20065743662825");
              bodyjson.put("invoiceDetails", "true");
//
//
//              bodyjson.put("additionalInfo", "Intuit20065743662825");
//              bodyjson.put("subWalletAmount", "Intuit20065743662825");



          } catch (JSONException e) {
            e.printStackTrace();
         }

          JSONObject fullJson = new JSONObject();


        try {
            fullJson.put("head",headjson);
            fullJson.put("body",bodyjson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

            Log.e("qr request",fullJson.toString());

            JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, "https://securegw-stage.paytm.in/paymentservices/qr/create", fullJson,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jarray) {
                            String response = jarray.toString();

                          Log.e("qr response",response);
//                            try {
//                                response = jarray.getString("status");
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            if (response.equalsIgnoreCase("success")) {
//
//                            } else {
//
//                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    });

            queue.add(jobReq);


    }

    public void getchecksum(){



        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                "https://theandroidpos.com/testapi/Paytm_PHP/sample1.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {

                        dialog.dismiss();
                        Log.e("checksum response",responseString);

                        JSONObject jsonObject=null;
                        try {
                            jsonObject=new JSONObject(responseString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            String response = jsonObject.getJSONObject("body").getJSONObject("resultInfo").getString("resultStatus");


                            if(response.equalsIgnoreCase("SUCCESS")){
                                String qrData=jsonObject.getJSONObject("body").getString("qrData");

                                Bitmap bitmap = encodeAsBitmap(qrData);
                                imageView.setImageBitmap(bitmap);

                                Bitmap bitmap4= Bitmap.createScaledBitmap(bitmap, 300, 240, false);
                                ILcdManager.getInstance(DynamicQR.this).sendLCDCommand(1);
                                ILcdManager.getInstance(DynamicQR.this).sendLCDCommand(4);
                                ILcdManager.getInstance(DynamicQR.this).sendLCDBitmap(bitmap4);

                                String[] arrOfStr = qrData.split("&", -2);

                                String orderid = arrOfStr[3];
                                    orderid = orderid.replaceAll("tr=","");
                                statusCount=0;
                                callstatusapi(orderid);
                            }else{
                                String message = jsonObject.getJSONObject("body").getJSONObject("resultInfo").getString("resultMsg");

                                Toast.makeText(DynamicQR.this,message,Toast.LENGTH_LONG).show();
                            }





                        } catch (JSONException | WriterException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                })  {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                               /*     params.put("email", email + "");
                                    params.put("password", password + "");*/
                Log.e("amount",amount);
                Log.e("mid",MID);
                Log.e("posId",PosID);
                Log.e("mkey",Merchant_key);


                params.put("amount",amount
                );
                params.put("mid",MID);
                params.put("posId",PosID);
                params.put("mkey",Merchant_key);
                return params;
            }
        };
    /*    sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        sr.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        queue.add(sr);
    }

    private void callstatusapi(final String orderid) {




        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                "https://theandroidpos.com/testapi/Paytm_PHP/status.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {

                        Log.e("status response",responseString);
                        JSONObject jsonObject=null;
                        try {
                            jsonObject=new JSONObject(responseString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            String response = jsonObject.getJSONObject("body").getJSONObject("resultInfo").getString("resultStatus");
                            String resultcode = jsonObject.getJSONObject("body").getJSONObject("resultInfo").getString("resultCode");
                            String resultmessage = jsonObject.getJSONObject("body").getJSONObject("resultInfo").getString("resultMsg");

                            if(resultcode.equalsIgnoreCase("01")){
                                Intent intent=new Intent();
                                intent.putExtra("MESSAGE","SUCCESS");
                                setResult(4,intent);
                                finish();

                            }else if(resultcode.equalsIgnoreCase("400")||resultcode.equalsIgnoreCase("402")){

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Do something after 5000ms
                                        statusCount++;
                                        if(statusCount>=10){
                                            getTxnStatus.setVisibility(View.VISIBLE);

                                            getTxnStatus.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    callstatusapi(orderid);
                                                }
                                            });

                                        }else{
                                            callstatusapi(orderid);
                                        }


                                    }
                                }, 5000);


                            }else{
                                Toast.makeText(DynamicQR.this,resultmessage,Toast.LENGTH_LONG).show();
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                })  {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                               /*     params.put("email", email + "");
                                    params.put("password", password + "");*/
                Log.e("orderid",orderid);
               // Log.e("response",responseString);


                params.put("mkey",Merchant_key);
                params.put("mid",MID);
                params.put("orderid",orderid);
               // params.put("response",responseString);
                return params;
            }
        };
    /*    sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        sr.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        queue.add(sr);


    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 100, 100, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 100, 0, 0, w, h);
        return bitmap;
    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("MESSAGE",getString(R.string.setmessage6));
        setResult(5,intent);
        finish();
    }
}
