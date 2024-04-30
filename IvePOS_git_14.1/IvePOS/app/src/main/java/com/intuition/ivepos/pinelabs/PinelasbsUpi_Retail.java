package com.intuition.ivepos.pinelabs;

import static android.content.Context.BIND_AUTO_CREATE;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.intuition.ivepos.BeveragesMenuFragment_Retail;
import com.intuition.ivepos.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PinelasbsUpi_Retail {
    public static String PLUTUS_SMART_PACKAGE="com.pinelabs.masterapp";
    public static String PLUTUS_SMART_ACTION="com.pinelabs.masterapp.SERVER";
    public static int MESSAGE_CODE=1001;
    public static String BILLING_REQUEST_TAG ="BILLINGAPPREQUEST";
    public static String BILLING_RESPONSE_TAG="BILLINGAPPRESPONSE";
    public static Messenger mServerMessenger;
    public static boolean isBound=false;
    public static Context context;
    public static String billnumber="";
    public static String amount="";
    public static BeveragesMenuFragment_Retail instance=null;
    public static ArrayList<PrintData> printDataList1;
    public static String methodid="";


    public void startPinelabsPayment(BeveragesMenuFragment_Retail instance1, Context ctx, String billno, String amt) {
        context=ctx;
        instance=instance1;
        billnumber=billno;
        amount=amt;
        methodid="1001";
        Log.e("amount-",amount);
        try {
            Intent intent = new Intent();
            intent.setAction(PLUTUS_SMART_ACTION);
            intent.setPackage(PLUTUS_SMART_PACKAGE);
            if(isBound){


                if(methodid.equalsIgnoreCase("1001")){

                    isBound = true;
                    Message message = Message.obtain(null, 1001);
                    Bundle data = new Bundle();

                    Gson gson=new Gson();
                    HashMap<String,String> map=new HashMap<>();
                  //  map.put("ApplicationId","c77a0574581f4d92a641117895238e7d");
                   //  map.put("ApplicationId","64ce90f2-2074-4132-92cb-97ec45876fe8");
                    //map.put("ApplicationId","27b08910b1824d5fbb74fc7b16628d3f");
map.put("ApplicationId","8cddee6a60e94584809d778d0c9d0454");



                    map.put("UserId","user1234");
                    map.put("MethodId","1001");
                    map.put("VersionNo","1.0");

                    HashMap<String,String> map1=new HashMap<>();
                    map1.put("TransactionType","5120");
                    map1.put("BillingRefNo",billnumber);
                    map1.put("PaymentAmount",amount);


                    HashMap<String,HashMap<String,String>> h1=new HashMap<>();
                    h1.put("Header",map);
                    h1.put("Detail",map1);


                    Log.e("tag",gson.toJson(h1));

                    //String value = { "Header": { "ApplicationId": "abcdefgh",  "UserId": "user1234",  "MethodId": "1004",  "VersionNo": "1.0"} }";

                    data.putString("MASTERAPPREQUEST", gson.toJson(h1));

                    message.setData(data);

                    try {

                        message.replyTo = new Messenger(new IncomingHandler());

                        mServerMessenger.send(message);


                    } catch (RemoteException e) {

                        e.printStackTrace();

                    }



                }else if(methodid.equalsIgnoreCase("1002")){


                    isBound = true;
                    Message message = Message.obtain(null, 1001);
                    Bundle data = new Bundle();

                    Gson gson=new Gson();
                    HashMap<String,Object> map=new HashMap<>();
                   // map.put("ApplicationId","c77a0574581f4d92a641117895238e7d");
                   //  map.put("ApplicationId","64ce90f2-2074-4132-92cb-97ec45876fe8");
                    //map.put("ApplicationId","27b08910b1824d5fbb74fc7b16628d3f");
map.put("ApplicationId","8cddee6a60e94584809d778d0c9d0454");

                    map.put("UserId","user1234");
                    map.put("MethodId","1002");
                    map.put("VersionNo","1.0");

                    HashMap<String,Object> map1=new HashMap<>();
                    map1.put("PrintRefNo","123456");
                    map1.put("SavePrintData",true);
                    map1.put("Data",printDataList1);


                    HashMap<String,HashMap<String,Object>> h1=new HashMap<>();
                    h1.put("Header",map);
                    h1.put("Detail",map1);


                    Log.e("tag",gson.toJson(h1));

                    //String value = { "Header": { "ApplicationId": "abcdefgh",  "UserId": "user1234",  "MethodId": "1004",  "VersionNo": "1.0"} }";

                    data.putString("MASTERAPPREQUEST", gson.toJson(h1));

                    message.setData(data);

                    try {

                        message.replyTo = new Messenger(new IncomingHandler());

                        mServerMessenger.send(message);


                    } catch (RemoteException e) {

                        e.printStackTrace();

                    }

                }



            }else{
                context.bindService(intent, connection, BIND_AUTO_CREATE);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {


            if(methodid.equalsIgnoreCase("1001")){


                mServerMessenger = new Messenger(service);
                isBound = true;
                Message message = Message.obtain(null, 1001);
                Bundle data = new Bundle();

                Gson gson=new Gson();
                HashMap<String,String> map=new HashMap<>();
               // map.put("ApplicationId","c77a0574581f4d92a641117895238e7d");
              //  map.put("ApplicationId","64ce90f2-2074-4132-92cb-97ec45876fe8");
                //map.put("ApplicationId","27b08910b1824d5fbb74fc7b16628d3f");
map.put("ApplicationId","8cddee6a60e94584809d778d0c9d0454");

                map.put("UserId","user1234");
                map.put("MethodId","1001");
                map.put("VersionNo","1.0");

                HashMap<String,String> map1=new HashMap<>();
                map1.put("TransactionType","5120");
                map1.put("BillingRefNo",billnumber);
                map1.put("PaymentAmount",amount);


                HashMap<String,HashMap<String,String>> h1=new HashMap<>();
                h1.put("Header",map);
                h1.put("Detail",map1);


                Log.e("tag",gson.toJson(h1));

                //String value = { "Header": { "ApplicationId": "abcdefgh",  "UserId": "user1234",  "MethodId": "1004",  "VersionNo": "1.0"} }";

                data.putString("MASTERAPPREQUEST", gson.toJson(h1));

                message.setData(data);

                try {

                    message.replyTo = new Messenger(new IncomingHandler());

                    mServerMessenger.send(message);


                } catch (RemoteException e) {

                    e.printStackTrace();

                }



            }else if(methodid.equalsIgnoreCase("1002")){

                mServerMessenger = new Messenger(service);
                isBound = true;
                Message message = Message.obtain(null, 1001);
                Bundle data = new Bundle();

                Gson gson=new Gson();
                HashMap<String,Object> map=new HashMap<>();
                //map.put("ApplicationId","c77a0574581f4d92a641117895238e7d");
                // map.put("ApplicationId","64ce90f2-2074-4132-92cb-97ec45876fe8");
                //map.put("ApplicationId","27b08910b1824d5fbb74fc7b16628d3f");
map.put("ApplicationId","8cddee6a60e94584809d778d0c9d0454");

                map.put("UserId","user1234");
                map.put("MethodId","1002");
                map.put("VersionNo","1.0");

                HashMap<String,Object> map1=new HashMap<>();
                map1.put("PrintRefNo","123456");
                map1.put("SavePrintData",true);
                map1.put("Data",printDataList1);


                HashMap<String,HashMap<String,Object>> h1=new HashMap<>();
                h1.put("Header",map);
                h1.put("Detail",map1);


                Log.e("tag",gson.toJson(h1));

                //String value = { "Header": { "ApplicationId": "abcdefgh",  "UserId": "user1234",  "MethodId": "1004",  "VersionNo": "1.0"} }";

                data.putString("MASTERAPPREQUEST", gson.toJson(h1));

                message.setData(data);

                try {

                    message.replyTo = new Messenger(new IncomingHandler());

                    mServerMessenger.send(message);


                } catch (RemoteException e) {

                    e.printStackTrace();

                }

            }



        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServerMessenger = null;
            isBound = false;
        }

    };



    private ServiceConnection printconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {


            if(methodid.equalsIgnoreCase("1001")){


                mServerMessenger = new Messenger(service);
                isBound = true;
                Message message = Message.obtain(null, 1001);
                Bundle data = new Bundle();

                Gson gson=new Gson();
                HashMap<String,String> map=new HashMap<>();
              //  map.put("ApplicationId","c77a0574581f4d92a641117895238e7d");
               //   map.put("ApplicationId","64ce90f2-2074-4132-92cb-97ec45876fe8");
                //map.put("ApplicationId","27b08910b1824d5fbb74fc7b16628d3f");
map.put("ApplicationId","8cddee6a60e94584809d778d0c9d0454");

                map.put("UserId","user1234");
                map.put("MethodId","1001");
                map.put("VersionNo","1.0");

                HashMap<String,String> map1=new HashMap<>();
                map1.put("TransactionType","4001");
                map1.put("BillingRefNo",billnumber);
                map1.put("PaymentAmount",amount);


                HashMap<String,HashMap<String,String>> h1=new HashMap<>();
                h1.put("Header",map);
                h1.put("Detail",map1);


                Log.e("tag",gson.toJson(h1));

                //String value = { "Header": { "ApplicationId": "abcdefgh",  "UserId": "user1234",  "MethodId": "1004",  "VersionNo": "1.0"} }";

                data.putString("MASTERAPPREQUEST", gson.toJson(h1));

                message.setData(data);

                try {

                    message.replyTo = new Messenger(new IncomingHandler());

                    mServerMessenger.send(message);


                } catch (RemoteException e) {

                    e.printStackTrace();

                }



            }else if(methodid.equalsIgnoreCase("1002")){

                mServerMessenger = new Messenger(service);
                isBound = true;
                Message message = Message.obtain(null, 1001);
                Bundle data = new Bundle();

                Gson gson=new Gson();
                HashMap<String,Object> map=new HashMap<>();
               // map.put("ApplicationId","c77a0574581f4d92a641117895238e7d");
               //   map.put("ApplicationId","64ce90f2-2074-4132-92cb-97ec45876fe8");
                //map.put("ApplicationId","27b08910b1824d5fbb74fc7b16628d3f");
map.put("ApplicationId","8cddee6a60e94584809d778d0c9d0454");

                map.put("UserId","user1234");
                map.put("MethodId","1002");
                map.put("VersionNo","1.0");

                HashMap<String,Object> map1=new HashMap<>();
                map1.put("PrintRefNo","123456");
                map1.put("SavePrintData",true);
                map1.put("Data",printDataList1);


                HashMap<String,HashMap<String,Object>> h1=new HashMap<>();
                h1.put("Header",map);
                h1.put("Detail",map1);


                Log.e("tag",gson.toJson(h1));

                //String value = { "Header": { "ApplicationId": "abcdefgh",  "UserId": "user1234",  "MethodId": "1004",  "VersionNo": "1.0"} }";

                data.putString("MASTERAPPREQUEST", gson.toJson(h1));

                message.setData(data);

                try {

                    message.replyTo = new Messenger(new IncomingHandler());

                    mServerMessenger.send(message);


                } catch (RemoteException e) {

                    e.printStackTrace();

                }

            }



        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServerMessenger = null;
            isBound = false;
        }

    };



    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            try {
                Bundle bundle = msg.getData();
                String value = bundle.getString("MASTERAPPRESPONSE");

                Log.e("value",value);
                JSONObject jsonObject=new JSONObject(value);
                JSONObject header=jsonObject.getJSONObject("Header");
                String methodId=header.getString("MethodId");

                if(methodId.equalsIgnoreCase("1001")){
                    JSONObject response=jsonObject.getJSONObject("Response");
                    String responseMessage=response.getString("ResponseMsg");
                    String responseCode=response.getString("ResponseCode");
                    if(responseMessage.equalsIgnoreCase("Success")){
                        Toast.makeText(context, (R.string.transaction_approve), Toast.LENGTH_SHORT).show();

                        final Runnable r = new Runnable() {
                            public void run() {

                               // instance.tickdonecard.performClick();

                               // Toast.makeText(PinelasbsUpi.this,"APPROVED",Toast.LENGTH_LONG).show();
                                Toast.makeText(instance,"SUCCESS",Toast.LENGTH_LONG).show();
                                instance.e_wallet =  "upiqr";
                                instance.custdetailsOnClick();
                                instance.checkprinterconn5(instance.dialog_p1);

                            }
                        };
                        Handler handler=new Handler();
                        handler.postDelayed(r,100);

                    }else{

                         Toast.makeText(context,(R.string.setmessage5),Toast.LENGTH_LONG).show();

//                        instance.builder.setMessage(getString(R.string.setmessage5)+"").setTitle(R.string.failed1_title);
//
//                        //Setting message manually and performing action on button click
//                        instance.builder.setMessage(getString(R.string.setmessage5)+"")
//                                .setCancelable(false)
//                                .setPositiveButton("okay", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        instance.alert.cancel();
//                                        instance.dialog_p1.dismiss();
//                                    }
//                                });
//                        //Creating dialog box
//                        instance.alert = instance.builder.create();
//                        //Setting the title manually
//                        instance.alert.setTitle(getString(R.string.failed1));
//                        instance.alert.show();

                    }
                }else if(methodId.equalsIgnoreCase("1002")){

                }



            } catch (Exception e) {
                e.printStackTrace();
            }

// process the response Json as required.
        }
    }





    public void printData(Context ctx, BeveragesMenuFragment_Retail instance1, ArrayList<PrintData> printDataList){
        methodid="1002";
        printDataList1=printDataList;

        if(isBound){

            // mServerMessenger = new Messenger(service);
            isBound = true;
            Message message = Message.obtain(null, 1001);
            Bundle data = new Bundle();

            Gson gson=new Gson();
            HashMap<String,Object> map=new HashMap<>();
           // map.put("ApplicationId","c77a0574581f4d92a641117895238e7d");
          //   map.put("ApplicationId","64ce90f2-2074-4132-92cb-97ec45876fe8");
            //map.put("ApplicationId","27b08910b1824d5fbb74fc7b16628d3f");
             map.put("ApplicationId","8cddee6a60e94584809d778d0c9d0454");

            map.put("UserId","user1234");
            map.put("MethodId","1002");
            map.put("VersionNo","1.0");

            HashMap<String,Object> map1=new HashMap<>();
            map1.put("PrintRefNo","123456");
            map1.put("SavePrintData",true);
            map1.put("Data",printDataList1);


            HashMap<String,HashMap<String,Object>> h1=new HashMap<>();
            h1.put("Header",map);
            h1.put("Detail",map1);


            Log.e("tag",gson.toJson(h1));

            //String value = { "Header": { "ApplicationId": "abcdefgh",  "UserId": "user1234",  "MethodId": "1004",  "VersionNo": "1.0"} }";

            data.putString("MASTERAPPREQUEST", gson.toJson(h1));

            message.setData(data);

            try {

                message.replyTo = new Messenger(new IncomingHandler());

                mServerMessenger.send(message);


            } catch (RemoteException e) {

                e.printStackTrace();

            }

        }else{
            context=ctx;
            instance=instance1;
            try {
                Intent intent = new Intent();
                intent.setAction(PLUTUS_SMART_ACTION);
                intent.setPackage(PLUTUS_SMART_PACKAGE);
                context.bindService(intent, connection, BIND_AUTO_CREATE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void unbindpinelabsservice(){
        if(isBound){
            context.unbindService(connection);
            isBound = false;
        }
    }







}