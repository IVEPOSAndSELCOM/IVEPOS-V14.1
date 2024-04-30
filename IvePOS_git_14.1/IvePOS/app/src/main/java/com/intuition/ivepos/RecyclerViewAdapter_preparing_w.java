package com.intuition.ivepos;

import static com.intuition.ivepos.BluetoothPrintDriver.BT_Write;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.epson.epos2.printer.Printer;
import com.mswipetech.wisepad.sdk.device.MSWisepadDeviceController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import it.sephiroth.android.library.widget.HListView;


public class RecyclerViewAdapter_preparing_w extends RecyclerView.Adapter<RecyclerViewAdapter_preparing_w.ViewHolder> {

    String addget, nameget, statussusb, deviceget;
    String ipnameget, portget, statusnet;
    String str_print_ty;
    String u_name, u_username;
    int iRowNumber, charlength, charlength1, charlength2, quanlentha;
    byte[][] allbuf, allbuf1, allbuf2, allbuf3, allbuf4, allbuf5, allbuf6, allbuf7, allbuf8, allbuf9, allbuf10, allbuf11, allbufqty, allbufitems, allbufmodifiers, allbufsubtot,
            allbuftax, allbufdisc, allbufrounded, allbuffulltot, allbuf12, allbuf13, allbuf14, allbufbillno, allbuftime, allbufline1, allbufline, allbufcust, allbufcustname,
            allbufcustadd, allbufcustph, allbufcustemail, allbuftaxestype2, allbuftaxestype1, allbuf1122, allbufKOT;
    String alltaxinprerc = "10", alltaxinrs;
    String alldiscinprerc = "5", alldiscinrs;
    TextView tvkot;
    String aqq2;
    float aqq1;
    String aqq = "0";
    String total2, rr1, rr2, rr3, rr4, rr5, total_disc_print, total_disc_print_q;
    String assa, assa1, assa2;
    byte[] setHT32, setHT321, setHT33, setHT34, setHT3212, setHT32122, setHTKOT, feedcut2;
    int nPaperWidth;
    String NAME, olddis, itemsort, orderby, ascdesc;
    String ipnameget_counter, portget_counter, statusnet_counter;
    String ipnameget_kot1, portget_kot1, statusnet_kot1;
    String ipnameget_kot2, portget_kot2, statusnet_kot2;
    String ipnameget_kot3, portget_kot3, statusnet_kot3;
    String ipnameget_kot4, portget_kot4, statusnet_kot4;
    String addgets, namegets, statussusbs;
    String strcompanyname, straddress1, straddress2, straddress3, strphone, stremailid, strwebsite, strtaxone, strbillone;
    TextView textViewstatussusbs, textViewstatusnets, textViewstatusnets_counter;
    String totalquanret1 = "0", totalquanret2 = "0", totalquanret, totalquantity="0";
    String NAme11, NAme111, bill_count_tag, bill_count_tag1 = "", bill_count_tag2 = "";
    TextView textViewstatusnets_kot1, textViewstatusnets_kot2, textViewstatusnets_kot3, textViewstatusnets_kot4;
    String ipnamegets, portgets, statusnets;
    String ipnamegets_kot1, portgets_kot1, statusnets_kot1, name_kot1;
    String ipnamegets_kot2, portgets_kot2, statusnets_kot2, name_kot2;
    String ipnamegets_kot3, portgets_kot3, statusnets_kot3, name_kot3;
    String ipnamegets_kot4, portgets_kot4, statusnets_kot4, name_kot4;
    String ipnamegets_counter, portgets_counter, statusnets_counter;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothPrintDriver mChatService = null;
    private WifiPrintDriver wifiSocket = null;
    private WifiPrintDriver2 wifiSocket2 = null;
    private WifiPrintDriver_kot1 wifiSocket_kot1 = null;
    private WifiPrintDriver_kot2 wifiSocket_kot2 = null;
    private WifiPrintDriver_kot3 wifiSocket_kot3 = null;
    private WifiPrintDriver_kot4 wifiSocket_kot4 = null;
    ArrayList<DataModel_w> mValues;
    RelativeLayout progressBar;
    static JSONArray itemsdetails;
    LinearLayout printreceiptlay,kotprint;
    LinearLayout preparing;
    Context mContext;
    String dateget, dateget1, timeget, printbillno;
    private SQLiteDatabase db, db1;
    final String get_cust = "https://api.werafoods.com/pos/v2/order/getcustomernumber";
    int flagpu;
    protected ItemListener mListener;

    public RecyclerViewAdapter_preparing_w(Context context, ArrayList<DataModel_w> values, ItemListener itemListener) {

        mValues = values;
        mContext = context;
        mListener=itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        public ImageView tickline;
        public CheckedTextView tick_prepare;
        public CheckedTextView tick_pickup;
        public CheckedTextView tick_deliver;
        public TextView rejectorder;
        public TextView no_items;
        public TextView totalprice;
        public TextView order_type;
        public TextView cust_name, cust_name1, custphno, custemail, custadd, custdarea, custdins;
        // public TextView time;
        public String status;
        public TextView deli_per_name, deli_per_status;
        public TextView deli_per_phone;
        public LinearLayout items;
        public TextView str_merch_id;
        public TextView swiggysupport, printreceipt, kotprintreceipt;
        LinearLayout deliveryagent;
        DataModel_w item;
        final TextView billnum;// = new TextView(mContext);

        final String url_accept = "https://api.werafoods.com/pos/v2/order/accept";
        final String url_reject = "https://api.werafoods.com/pos/v2/order/reject";
        final String url_fodready = "https://api.werafoods.com/pos/v2/order/food-ready";
        final String url_delivered = "https://api.werafoods.com/pos/v2/order/delivered";
        //        final String url_delivered = "https://api.werafoods.com/pos/v2/order/getde";
        final String url_pickup = "https://api.werafoods.com/pos/v2/order/pickedup";
        String url_orders = "https://api.werafoods.com/pos/v2/merchant/orders";
        final String url_swiggysupport = "https://api.werafoods.com/pos/v2/order/callsupport";

        JSONObject jsonBody;

        public ViewHolder(View v) {

            super(v);

            v.setOnClickListener(this);
            textView = (TextView) v.findViewById(R.id.order_id);
            progressBar = (RelativeLayout) v.findViewById(R.id.progressbar1);
            printreceiptlay = (LinearLayout) v.findViewById(R.id.printreceiptlay);
            kotprint = (LinearLayout) v.findViewById(R.id.printkotreceiptlay);
            tick_prepare = (CheckedTextView) v.findViewById(R.id.tick_prepare);
            tick_pickup = (CheckedTextView) v.findViewById(R.id.tick_pickup);
            tick_deliver = (CheckedTextView) v.findViewById(R.id.tick_delivered);
            tickline = (ImageView) v.findViewById(R.id.tick_line);
            items = (LinearLayout) v.findViewById(R.id.items);
//            rejectorder = (TextView) v.findViewById(R.id.rejectorder);
            no_items = (TextView) v.findViewById(R.id.noitems);
            totalprice = (TextView) v.findViewById(R.id.totalprice);
            order_type = (TextView) v.findViewById(R.id.order_type);
            cust_name = (TextView) v.findViewById(R.id.pcust_name);
            //swiggysupport = (TextView) v.findViewById(R.id.swiggysupport);
            printreceipt = (TextView) v.findViewById(R.id.printreceipt);
            kotprintreceipt = (TextView) v.findViewById(R.id.printkotreceipt);
            //time = (TextView) v.findViewById(R.id.time);
            deli_per_name = (TextView) v.findViewById(R.id.deli_per_name);
            deli_per_phone = (TextView) v.findViewById(R.id.deli_per_phone);
            // deli_per_status = (TextView) v.findViewById(R.id.deli_per_status);
            str_merch_id = (TextView) v.findViewById(R.id.rest_id);
            deliveryagent =(LinearLayout) v.findViewById(R.id.deliagent);
//            imageView = (ImageView) v.findViewById(R.id.imageView);
//            relativeLayout = (RelativeLayout) v.findViewById(R.id.relativeLayout);



            tick_prepare.setOnClickListener(this);
            tick_pickup.setOnClickListener(this);
            tick_deliver.setOnClickListener(this);
            items.setOnClickListener(this);
            //swiggysupport.setOnClickListener(this);
            printreceipt.setOnClickListener(this);
            kotprintreceipt.setOnClickListener(this);
            cust_name.setOnClickListener(this);
//            rejectorder.setOnClickListener(this);

            billnum = new TextView(mContext);;
        }

        public void setData(DataModel_w item) {
            this.item = item;

            textView.setText(item.text);
            no_items.setText(item.no_items);
            totalprice.setText(item.totalprice);
            order_type.setText(item.order_type);
            cust_name.setText(item.cust_name);
            // time.setText(item.time);
            status = item.status;
            deli_per_name.setText(item.deli_per_name);
            deli_per_phone.setText(item.deli_per_phon);
            // deli_per_status.setText(item.deli_per_status);
            str_merch_id.setText(item.str_merch_id);
            itemsdetails=item.items1;
            db = mContext.openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
            db1= mContext.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

            if (status.toString().equalsIgnoreCase("Accepted")){
                kotprint.setVisibility(View.VISIBLE);
                Cursor ffifteen_0 = db.rawQuery("SELECT * FROM Billnumber WHERE order_id = '" + item.text + "'", null);
                if (ffifteen_0.moveToFirst()) {
                    printreceiptlay.setVisibility(View.VISIBLE);
                }
                else{
//                    printreceiptlay.setVisibility(View.VISIBLE);
                }
                ffifteen_0.close();
            }
            if (status.toString().equalsIgnoreCase("Food Ready")){
                tick_prepare.setCheckMarkDrawable(R.drawable.ic_success_green_32dp);
                ((Preparing_Orders_w) mContext).new_orders();
                Cursor ffifteen_0 = db.rawQuery("SELECT * FROM Billnumber WHERE order_id = '" + item.text + "'", null);
                if (ffifteen_0.moveToFirst()) {
                    printreceiptlay.setVisibility(View.VISIBLE);
                }
                else{
//                    printreceiptlay.setVisibility(View.VISIBLE);
                }
                ffifteen_0.close();
            }
            if (status.toString().equalsIgnoreCase("Picked Up")){
                tick_prepare.setCheckMarkDrawable(R.drawable.ic_success_green_32dp);
                tick_pickup.setCheckMarkDrawable(R.drawable.ic_success_green_32dp);
                //====================================report creation=====================================
                /*String staus="Picked Up";
                ((Preparing_Orders_w) mContext).updateweraindextable(textView.getText().toString(), staus, item.no_items);*/

                ((Preparing_Orders_w) mContext).new_orders();
                Cursor ffifteen_0 = db.rawQuery("SELECT * FROM Billnumber WHERE order_id = '" + item.text + "'", null);
                if (ffifteen_0.moveToFirst()) {
                    printreceiptlay.setVisibility(View.VISIBLE);
                }
                else{
//                    printreceiptlay.setVisibility(View.VISIBLE);
                }
                ffifteen_0.close();
            }

            if (status.toString().equalsIgnoreCase("Delivered")){
                tick_prepare.setCheckMarkDrawable(R.drawable.ic_success_green_32dp);
                tick_pickup.setCheckMarkDrawable(R.drawable.ic_success_green_32dp);
                tick_deliver.setCheckMarkDrawable(R.drawable.ic_success_green_32dp);

                /*String staus="Delivered";
                ((Preparing_Orders_w) mContext).updateweraindextable(textView.getText().toString(), staus, item.no_items);*/
                ((Preparing_Orders_w) mContext).new_orders();
                Cursor ffifteen_0 = db.rawQuery("SELECT * FROM Billnumber WHERE order_id = '" + item.text + "'", null);
                if (ffifteen_0.moveToFirst()) {
                    printreceiptlay.setVisibility(View.VISIBLE);
                }
                else{
//                    printreceiptlay.setVisibility(View.VISIBLE);
                }
                ffifteen_0.close();

            }
            System.out.println("Recycler orderid: " + item.text + " status  "+ status.toString() +"  Agent Response: " + item.deli_per_name + "  "+item.deli_per_phon+"  "+item.deli_per_status);

            /*if (item.deli_per_name==null && item.deli_per_phon==null && item.deli_per_status==null) {
            }else{
                int dl=deliveryagent.getVisibility();
                if (dl== View.GONE){
                    deliveryagent.setVisibility(View.VISIBLE);
                }
            }*/
//            imageView.setImageResource(item.drawable);
//            relativeLayout.setBackgroundColor(Color.parseColor(item.color));

        }


        @Override
        public void onClick(View view) {
            if (view.getId() == tick_prepare.getId()){
//                tick_prepare.setText(textView.getText().toString());
                //progressBar.setVisibility(View.VISIBLE);


                final Dialog dialog = new Dialog(mContext, R.style.timepicker_date_dialog);
                dialog.setContentView(R.layout.dialog_confirmation_prepare_w);
                dialog.show();

                Button cancel = (Button) dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                ImageView closetext = (ImageView) dialog.findViewById(R.id.closetext);
                closetext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                Button ok = (Button) dialog.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();

                        if (item.status.equalsIgnoreCase("Accepted") || item.status.equalsIgnoreCase("auto-accept")) {
                            tick_prepare.setCheckMarkDrawable(R.drawable.ic_success_green_32dp);
                            db = mContext.openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                            db1 = mContext.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy MM dd");
                            final String currentDateandTime1 = sdf2.format(new Date());

                            SimpleDateFormat normal = new SimpleDateFormat("dd MMM yyyy");
                            final String normal1 = normal.format(new Date());

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                            final String currentDateandTime = sdf.format(new Date());

                            Date dt = new Date();
                            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
                            final String time1 = sdf1.format(dt);

                            Date dtt = new Date();
                            SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
                            final String time24 = sdf1t.format(dtt);

                            Date dtt_new = new Date();
                            SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");
                            final String time24_new = sdf1t_new.format(dtt_new);

                            Date dt4 = new Date();
                            SimpleDateFormat sdf14 = new SimpleDateFormat("kk");
                            String time14 = sdf14.format(dt4);

                            dateget = currentDateandTime1;
                            dateget1 = normal1;
                            timeget = time1;

                            Cursor count = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber LIKE  '" + currentDateandTime + "-" + "000" + "0" + "1" + "%' "+" AND order_id!='" + textView.getText().toString() + "'", null);

                            final int bill = count.getCount() + 1;
                            String bills = String.valueOf(bill);

                            billnum.setText(currentDateandTime + "-" + "000" + "0" + "1" + bills);
                            printbillno=billnum.getText().toString();

                            ((Preparing_Orders_w) mContext).saveinDB(textView.getText().toString(), currentDateandTime1,normal1, currentDateandTime, time1, time24, time24_new, time14, bills, printbillno);


                            System.out.println("Recycler View bill num: "+ printbillno+"   "+billnum.getText().toString());
                            try {
                                //I try to use this for send Header is application/json
                                jsonBody = new JSONObject();
                                jsonBody.put("merchant_id", str_merch_id.getText().toString());
                                jsonBody.put("order_id", textView.getText().toString());
//                            jsonBody.put("remark", "");
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }

                            RequestQueue mQueue = Volley.newRequestQueue(mContext);

                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url_fodready, jsonBody,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d("TAG", response.toString());
                                            String staus="Food Ready";
                                            ((Preparing_Orders_w) mContext).updateweraindextable(textView.getText().toString(), staus, item.no_items);

                                            //((Preparing_Orders_w) mContext).updateorderdatadb(textView.getText().toString());
                                            ((Preparing_Orders_w) mContext).new_orders();
                                            //Toast.makeText(mContext,"Food is ready", Toast.LENGTH_LONG).show();
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("TAG", error.getMessage(), error);
                                }
                            }) { //no semicolon or coma
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("Content-Type", "X-Wera-Api-Key");
                                    params.put("X-Wera-Api-Key", "9b1bc8d1-99d2-4597-aa7e-64e0a9580c10");
                                    params.put("Accept", "");
                                    return params;
                                }
                            };
                            mQueue.add(jsonObjectRequest);

                            //progressBar.setVisibility(View.GONE);
                            //((Preparing_Orders_w) mContext).refresh();
                            //item.status = "Food Ready";
                        } else {
                            Toast.makeText(mContext, "Please accept the order first.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }else {
                if (view.getId() == tick_pickup.getId()){

                    /*//progressBar.setVisibility(View.VISIBLE);
                    flagpu=0;
                    final Dialog dialog = new Dialog(mContext, R.style.timepicker_date_dialog);
                    dialog.setContentView(R.layout.dialog_confirmation_pickup_w);
                    dialog.show();


                    Button cancel = (Button) dialog.findViewById(R.id.cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    ImageView closetext = (ImageView) dialog.findViewById(R.id.closetext);
                    closetext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    Button ok = (Button) dialog.findViewById(R.id.ok);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            flagpu=1;
                            if (item.status.equalsIgnoreCase("Food Ready")) {
                                tick_pickup.setCheckMarkDrawable(R.drawable.ic_success_green_32dp);
                            try {
                                //I try to use this for send Header is application/json
                                jsonBody = new JSONObject();
                                jsonBody.put("merchant_id", str_merch_id.getText().toString());
                                jsonBody.put("order_id", textView.getText().toString());
//            jsonBody.put("rejection_id", "");
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }

                            RequestQueue mQueue = Volley.newRequestQueue(mContext);

                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url_pickup, jsonBody,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            *//*ProgressBar progressBar2 = new ProgressBar(mContext);
                                           // progressBar = (RelativeLayout) dialog.findViewById(R.id.progressbar1);
                                            progressBar2.setVisibility(View.VISIBLE);*//*
                                            Log.d("TAG", response.toString());
                                            String staus="Picked Up";
                                                    ((Preparing_Orders_w) mContext).updateweraindextable(textView.getText().toString(), staus, item.no_items);
                                            //dialog.dismiss();

                                            db = mContext.openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                                            db1 = mContext.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

                                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy MM dd");
                                            final String currentDateandTime1 = sdf2.format(new Date());

                                            SimpleDateFormat normal = new SimpleDateFormat("dd MMM yyyy");
                                            final String normal1 = normal.format(new Date());

                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                                            final String currentDateandTime = sdf.format(new Date());

                                            Date dt = new Date();
                                            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
                                            final String time1 = sdf1.format(dt);

                                            Date dtt = new Date();
                                            SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
                                            final String time24 = sdf1t.format(dtt);

                                            Date dtt_new = new Date();
                                            SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");
                                            final String time24_new = sdf1t_new.format(dtt_new);

                                            Date dt4 = new Date();
                                            SimpleDateFormat sdf14 = new SimpleDateFormat("kk");
                                            String time14 = sdf14.format(dt4);

                                            dateget = currentDateandTime1;
                                            dateget1 = normal1;
                                            timeget = time1;

                                            Cursor count = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber LIKE  '" + currentDateandTime + "-" + "000" + "0" + "1" + "%' "+" AND order_id!='" + textView.getText().toString() + "'", null);

                                            final int bill = count.getCount() + 1;
                                            String bills = String.valueOf(bill);


                                            billnum.setText(currentDateandTime + "-" + "000" + "0" + "1" + bills);
                                            printbillno=billnum.getText().toString();






                                            ((Preparing_Orders_w) mContext).saveinDB(textView.getText().toString(), currentDateandTime1,normal1, currentDateandTime, time1, time24, time24_new, time14, bills, printbillno);
                                            //((Preparing_Orders_w) mContext).printer1();
                                                    //((Preparing_Orders_w) mContext).updateorderdatadb(textView.getText().toString());
                                            ((Preparing_Orders_w) mContext).new_orders();
                                            //progressBar2.setVisibility(View.GONE);
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("TAG", error.getMessage(), error);
                                }
                            }) { //no semicolon or coma
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("Content-Type", "X-Wera-Api-Key");
                                    params.put("X-Wera-Api-Key", "9b1bc8d1-99d2-4597-aa7e-64e0a9580c10");
                                    params.put("Accept", "");
                                    return params;
                                }
                            };
                            mQueue.add(jsonObjectRequest);


                                //progressBar.setVisibility(View.GONE);
                                //((Preparing_Orders_w) mContext).refresh();
                                //item.status = "Picked Up";
                        }
                            else {
                                Toast.makeText(mContext, "Please prepare the food first.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    Button print = (Button) dialog.findViewById(R.id.billprinter);
                    print.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(flagpu == 1){
                                System.out.println("Recycler View bill num: "+ printbillno+"   "+billnum.getText().toString());
                                ((Preparing_Orders_w) mContext).printer(printbillno);
                                dialog.dismiss();
                            }else {
                                Toast.makeText(mContext, "Please click PICKED UP before print", Toast.LENGTH_LONG).show();

                            }
                        }
                    });*/
                }else {
                    if (view.getId() == tick_deliver.getId()){

                       /* //progressBar.setVisibility(View.VISIBLE);
                        final Dialog dialog = new Dialog(mContext, R.style.timepicker_date_dialog);
                        dialog.setContentView(R.layout.dialog_confirmation_deliver_w);
                        dialog.show();


                        Button cancel = (Button) dialog.findViewById(R.id.cancel);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        ImageView closetext = (ImageView) dialog.findViewById(R.id.closetext);
                        closetext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        Button ok = (Button) dialog.findViewById(R.id.ok);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.dismiss();

                                if (item.status.equalsIgnoreCase("Picked Up")) {
                                    tick_deliver.setCheckMarkDrawable(R.drawable.ic_success_green_32dp);
                                try {
                                    //I try to use this for send Header is application/json
                                    jsonBody = new JSONObject();
                                    jsonBody.put("merchant_id", str_merch_id.getText().toString());
                                    jsonBody.put("order_id", textView.getText().toString());
//            jsonBody.put("remark", "");
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }

                                RequestQueue mQueue = Volley.newRequestQueue(mContext);

                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url_delivered, jsonBody,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Log.d("TAG", response.toString());
                                                Toast.makeText(mContext, "Delivered", Toast.LENGTH_LONG).show();

//                                                        ((Preparing_Orders_w) mContext).saveinDB(textView.getText().toString());
                                                String staus="Delivered";
                                                        ((Preparing_Orders_w) mContext).updateweraindextable(textView.getText().toString(), staus, item.no_items);
                                                        //((Preparing_Orders_w) mContext).updateorderdatadb(textView.getText().toString());
                                                ((Preparing_Orders_w) mContext).new_orders();
                                                        //tick_deliver.setVisibility(View.GONE);tickline.setVisibility(View.GONE);

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("TAG", error.getMessage(), error);
                                    }
                                }) { //no semicolon or coma
                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("Content-Type", "X-Wera-Api-Key");
                                        params.put("X-Wera-Api-Key", "9b1bc8d1-99d2-4597-aa7e-64e0a9580c10");
                                        params.put("Accept", "");
                                        return params;
                                    }
                                };
                                mQueue.add(jsonObjectRequest);
                                    //progressBar.setVisibility(View.GONE);
                                    //((Preparing_Orders_w) mContext).refresh();

                            }else {
                                    Toast.makeText(mContext, "Please make sure, if the order is Picked up.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
*/
                    } else {
                        if (view.getId() == items.getId()) {

                            //((Preparing_Orders_w) mContext).pre_order_items(textView.getText().toString(),str_merch_id.getText().toString(), mContext);
                            final Dialog dialog = new Dialog(mContext, R.style.timepicker_date_dialog);
                            dialog.setContentView(R.layout.order_items_w);
                            dialog.show();
                            Context icontext= mContext;

                            // TableLayout itemtab = (TableLayout) dialog.findViewById(R.id.itemtab);

                            LinearLayout back_activity = (LinearLayout) dialog.findViewById(R.id.back_activity);
                            back_activity.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            TextView itmname = (TextView) dialog.findViewById(R.id.itemname);
                            TextView itmqnty = (TextView) dialog.findViewById(R.id.itemqnty);
                            TextView ordinst = (TextView) dialog.findViewById(R.id.ordinstruction);



                            for (int j = 0; j < item.items1.length(); j++) {
                                JSONObject items = null;
                                try {
                                    items = item.items1.getJSONObject(j);
                                    String itemid = items.getString("wera_item_id");
                                    String item_id = items.getString("item_id");
                                    String item_name = items.getString("item_name");
                                    String instructions = items.getString("instructions");
                                    String item_quantity = items.getString("item_quantity");
                                    //System.out.println("item name " + item_name + "item quantity " + item_quantity);
                                    String var_name = null;
                                    JSONArray variant, addons;
                                    try {
                                        variant = items.getJSONArray("variants");
                                    } catch (JSONException e) {
                                        System.out.println("Json error variants"+ e);
                                        throw new RuntimeException(e);
                                    }
                                    for (int p = 0; p < variant.length(); p++) {
                                        JSONObject var = variant.getJSONObject(p);
                                        try {
                                            String var_id = var.getString("variant_id");
                                            var_name = var.getString("variant_name");
                                            String var_size_id = var.getString("size_id");
                                            String var_size_name = var.getString("size_name");
                                            String var_price = var.getString("price");
                                            String var_cgst = var.getString("cgst");
                                            String var_sgst = var.getString("sgst");
                                            String var_cgst_percent = var.getString("cgst_percent");
                                            String var_sgst_percent = var.getString("sgst_percent");
                                        } catch (JSONException e) {
                                            System.out.println("Json error variants"+ e);
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    TextView vari = new TextView(mContext);
                                    vari.setText(var_name);

                                    //System.out.println("item name " + item_name + "item quantity " + item_quantity);
                                    if(vari.getText().toString().isEmpty()) {
                                        itmqnty.append(item_quantity + "  " + "\n");
                                        itmname.append(item_name + "     " + "\n");
                                        ordinst.append(instructions + "     " + "\n");
                                    }else{
                                        itmqnty.append(item_quantity + "   " + "\n");
                                        itmname.append(item_name +"-"+var_name+ "  " + "\n");
                                        ordinst.append(instructions + "     " + "\n");
                                    }

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }



                        } else {
                            if (view.getId() == cust_name.getId()) {

                                final Dialog dialog = new Dialog(mContext, R.style.timepicker_date_dialog);
                                dialog.setContentView(R.layout.customer_details_w);
                                dialog.show();
                                Context icontext= mContext;

                                LinearLayout back_activity = (LinearLayout) dialog.findViewById(R.id.back_activity);
                                back_activity.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });

                                TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });

                                TextView custname = (TextView) dialog.findViewById(R.id.custname);
                                TextView custadd = (TextView) dialog.findViewById(R.id.custadd);
                                TextView custdarea = (TextView) dialog.findViewById(R.id.custdarea);
                                TextView custdins = (TextView) dialog.findViewById(R.id.custdins);
                                TextView custemail = (TextView) dialog.findViewById(R.id.custemail);
                                TextView custphno = (TextView) dialog.findViewById(R.id.custphno);
                                TextView getcustphno = (TextView) dialog.findViewById(R.id.getcustphno);
                                String order_id=textView.getText().toString();

                                try {
                                    //I try to use this for send Header is application/json
                                    jsonBody = new JSONObject();
                                    //jsonBody.put("merchant_id", str_merch_id.getText().toString());
                                    jsonBody.put("order_id", textView.getText().toString());
                                    //  jsonBody.put("rejection_id", rr_id);
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }

                                RequestQueue mQueue = Volley.newRequestQueue(icontext);
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(get_cust, jsonBody,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject iresponse) {

                                                try {
                                                    //JSONObject jsonObject = new JSONObject(iresponse); // Parse the JSON string
                                                    JSONObject detailsObject = iresponse.getJSONObject("details"); // Get the 'details' object
                                                    String numberValue = detailsObject.getString("number"); // Get the 'number' value
                                                    getcustphno.setText(numberValue);
                                                    // Do something with the numberValue
                                                } catch (JSONException e) {
                                                    // Handle the exception
                                                }



                                            /*DataModel_w iden = null;
                                            JSONArray orders;
                                            try {
                                                JSONObject details = new JSONObject(iresponse);
                                                //orders = iresponse.getJSONArray("details");
                                                String cutnumber = details.getJSONObject("details").getString("number");
                                                getcustphno.setText(cutnumber);
                                                //JSONObject details = iresponse.getJSONObject("details");
                                                //orddetails = iresponse.getJSONArray("details");
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }*/

                                                /*for (int k = 0; k < orddetails.length(); k++) {
                                                    try{
                                                    JSONObject c = orddetails.getJSONObject(k);
                                                    String cutnumber = c.getString("number");
                                                    if(cutnumber.isEmpty()){
                                                        cutnumber=" ";
                                                    }
                                                    getcustphno.setText(cutnumber);
                                                   // JSONArray custnum = c.getJSONArray("details");
                                                 *//*   for (int j = 0; j < c.length(); j++) {
                                                        JSONObject items = c.getJSONObject(j);
                                                        String cutnumber = items.getString("number");
                                                        getcustphno.setText(cutnumber);
                                                    }*//*
                                                }catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                            }*/
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("TAG", error.getMessage(), error);
                                    }
                                }) { //no semicolon or coma
                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("Content-Type", "X-Wera-Api-Key");
                                        params.put("X-Wera-Api-Key", "9b1bc8d1-99d2-4597-aa7e-64e0a9580c10");
                                        params.put("Accept", "");
                                        return params;
                                    }
                                };
                                mQueue.add(jsonObjectRequest);


                                //String order_id = textView.getText().toString();

                                custname.setText(item.cust_name);
                                custphno.setText(item.cust_phno);
                                custemail.setText(item.cust_email);
                                custadd.setText(item.cust_address);
                                custdarea.setText(item.cust_deliv_area);
                                custdins.setText(item.cust_deli_instru);

                            }

                            if (view.getId() == printreceipt.getId()) {

                               /* db = mContext.openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                                db1 = mContext.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy MM dd");
                                final String currentDateandTime1 = sdf2.format(new Date());

                                SimpleDateFormat normal = new SimpleDateFormat("dd MMM yyyy");
                                final String normal1 = normal.format(new Date());

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                                final String currentDateandTime = sdf.format(new Date());

                                Date dt = new Date();
                                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
                                final String time1 = sdf1.format(dt);

                                Date dtt = new Date();
                                SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
                                final String time24 = sdf1t.format(dtt);

                                Date dtt_new = new Date();
                                SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");
                                final String time24_new = sdf1t_new.format(dtt_new);

                                Date dt4 = new Date();
                                SimpleDateFormat sdf14 = new SimpleDateFormat("kk");
                                String time14 = sdf14.format(dt4);

                                dateget = currentDateandTime1;
                                dateget1 = normal1;
                                timeget = time1;

                                Cursor count = db.rawQuery("SELECT * FROM Billnumber WHERE billnumber LIKE  '" + currentDateandTime + "-" + "000" + "0" + "1" + "%' "+" AND order_id!='" + textView.getText().toString() + "'", null);

                                final int bill = count.getCount() + 1;
                                String bills = String.valueOf(bill);

                                billnum.setText(currentDateandTime + "-" + "000" + "0" + "1" + bills);
                                printbillno=billnum.getText().toString();

                                ((Preparing_Orders_w) mContext).saveinDB(textView.getText().toString(), currentDateandTime1,normal1, currentDateandTime, time1, time24, time24_new, time14, bills, printbillno);


                                System.out.println("Recycler View bill num: "+ printbillno+"   "+billnum.getText().toString());*/
                                ((Preparing_Orders_w) mContext).printer(printbillno);
                                ((Preparing_Orders_w) mContext).new_orders();

                            }
                            if (view.getId() == kotprintreceipt.getId()) {

                                kotprintreceipt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                            byte[] setHT34M = {0x1b, 0x44, 0x04, 0x11, 0x19, 0x00};
                                            byte[] dotfeed = {0x1b, 0x4a, 0x10};
                                            byte[] HTRight = {0x1b, 0x61, 0x02, 0x09};
                                            byte[] HT = {0x09};
                                            byte[] HT1 = {0x09};
                                            byte[] LF = {0x0d, 0x0a};

                                            byte[] left = {0x1b, 0x61, 0x00};
                                            byte[] cen = {0x1b, 0x61, 0x01};
                                            byte[] right = {0x1b, 0x61, 0x02};
                                            byte[] bold = {0x1B,0x21,0x10};
                                            byte[] normal = {0x1d, 0x21, 0x00};
                                            byte[] horiz1 = {0x1b, 0x44, 0x19, 0x19, 0x00};
                                            byte[] horiz = {0x1b, 0x44, 0x04, 0x06, 0x04, 0x04, 0x0A, 0x00, 0x09, 0x30, 0x09, 0x31, 0x09, 0x32, 0x09, 0x33, 0x09, 0x34, 0x0d, 0x0a};

                                            byte[] un = {0x1b, 0x2d, 0x02};
                                            byte[] un1 = {0x1b, 0x2d, 0x00};
                                            String str_line = "";

                                            Cursor print_ty = db1.rawQuery("SELECT * FROM Printer_type", null);
                                            if (print_ty.moveToFirst()) {
                                                str_print_ty = print_ty.getString(1);
                                            }
                                            print_ty.close();

                                            Cursor getprint_type = db1.rawQuery("SELECT * FROM Printer_text_size", null);
                                            if (getprint_type.moveToFirst()) {
                                                String type = getprint_type.getString(1);

                                                Cursor cc = db1.rawQuery("SELECT * FROM Printerreceiptsize", null);

                                                if (cc.moveToFirst()) {
                                                    cc.moveToFirst();
                                                    do {
                                                        NAME = cc.getString(1);
                                                        if (NAME.equals("3 inch")) {
                                                            if (str_print_ty.toString().equals("Generic") || str_print_ty.toString().equals("Epson/others")) {
                                                                setHT32 = new byte[]{0x1b, 0x44, 0x29, 0x00};//2 tabs 3"
                                                                setHT321 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 3"
                                                                setHT3212 = new byte[]{0x1b, 0x44, 0x25, 0x00};//2 tabs 3"
                                                                setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                                                                setHT34 = new byte[]{0x1b, 0x44, 0x08, 0x20, 0x29, 0x00};//4 tabs 3"
                                                                setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 3"
                                                                feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                                                nPaperWidth = 576;
                                                                charlength = 41;
                                                                HT1 = new byte[]{0x09};
                                                                str_line = "------------------------------------------------";
                                                                allbufline = new byte[][]{
                                                                        left, un1, "------------------------------------------------".getBytes(), LF

                                                                };
                                                            } else {
                                                                if (str_print_ty.toString().equals("POS")) {
                                                                    setHT32 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                                                    setHT321 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                                                    setHT3212 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                                                    setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                                                                    setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x10, 0x15, 0x00};//4 tabs 3"
                                                                    setHTKOT = new byte[]{0x1b, 0x44, 0x05, 0x00};//2 tabs 3"
                                                                    feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                                                    nPaperWidth = 576;
                                                                    charlength = 23;
                                                                    charlength1 = 46;
                                                                    charlength2 = 69;
                                                                    quanlentha = 4;
                                                                    HT1 = new byte[]{0x2F};
                                                                    str_line = "------------------------------------------------";
                                                                    allbufline = new byte[][]{
                                                                            left, un1, "------------------------------------------------".getBytes(), LF

                                                                    };
                                                                }
                                                            }
                                                        } else {
                                                            if (str_print_ty.toString().equals("Generic")) {
//                            Toast.makeText(mContext, "phi", Toast.LENGTH_SHORT).show();
                                                                setHT32 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 2"
                                                                setHT321 = new byte[]{0x1b, 0x44, 0x13, 0x00};//2 tabs 3"
                                                                setHT3212 = new byte[]{0x1b, 0x44, 0x15, 0x00};//2 tabs 3"
                                                                setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                                                                setHT34 = new byte[]{0x1b, 0x44, 0x04, 0x12, 0x19, 0x00};//4 tabs 2"
                                                                setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 2"
                                                                feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                                                                nPaperWidth = 384;
                                                                charlength = 25;
                                                                HT1 = new byte[]{0x09};
                                                                str_line = "--------------------------------";
                                                                allbufline = new byte[][]{
                                                                        left, un1, "--------------------------------".getBytes(), LF
                                                                };
                                                            } else {
                                                                if (str_print_ty.toString().equals("Epson/others")) {
//                            Toast.makeText(mContext, "epson", Toast.LENGTH_SHORT).show();
                                                                    setHT32 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 2"
                                                                    setHT321 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 3"
                                                                    setHT3212 = new byte[]{0x1b, 0x44, 0x21, 0x00};//2 tabs 3"
                                                                    setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                                                                    setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x19, 0x21, 0x00};//4 tabs 2"
                                                                    setHTKOT = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                                                    feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                                                                    nPaperWidth = 384;
                                                                    charlength = 28;
                                                                    HT1 = new byte[]{0x09};
                                                                    str_line = "------------------------------------------";
                                                                    allbufline = new byte[][]{
                                                                            left, un1, "------------------------------------------".getBytes(), LF
                                                                    };
                                                                } else {
                                                                    if (str_print_ty.toString().equals("POS")) {
                                                                        setHT32 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                                                        setHT321 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                                                        setHT3212 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 3"
                                                                        setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x12, 0x21, 0x00};//4 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x05, 0x08, 0x00};//4 tabs 2"
                                                                        setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x08, 0x09, 0x00};//4 tabs 2"
                                                                        setHTKOT = new byte[]{0x1b, 0x44, 0x05, 0x00};//2 tabs 3"
                                                                        feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                                                        nPaperWidth = 384;
                                                                        charlength = 11;
                                                                        charlength1 = 22;
                                                                        charlength2 = 33;
                                                                        quanlentha = 3;
                                                                        HT1 = new byte[]{0x2F};
                                                                        str_line = "--------------------------------";
                                                                        allbufline = new byte[][]{
                                                                                left, un1, "--------------------------------".getBytes(), LF
                                                                        };
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } while (cc.moveToNext());
                                                }
                                                cc.close();

                                            }
                                            getprint_type.close();

                                            String dd = "";
                                            TextView qazcvb = new TextView(mContext);
                                            Cursor cvonnusb = db1.rawQuery("SELECT * FROM BTConn", null);
                                            if (cvonnusb.moveToFirst()) {
                                                addgets = cvonnusb.getString(1);
                                                namegets = cvonnusb.getString(2);
                                                statussusbs = cvonnusb.getString(3);
                                                dd = cvonnusb.getString(4);
                                            }
                                            cvonnusb.close();
                                            qazcvb.setText(dd);
                                            if (qazcvb.getText().toString().equals("usb") && statussusbs.toString().equals("ok")) {
                                                // runPrintCouponSequence();
                                            } else {

                                                Cursor connnet = db1.rawQuery("SELECT * FROM IPConn", null);
                                                if (connnet.moveToFirst()) {
                                                    ipnamegets = connnet.getString(1);
                                                    portgets = connnet.getString(2);
                                                    statusnets = connnet.getString(3);
                                                }
                                                connnet.close();

                                                Cursor connnet_counter = db1.rawQuery("SELECT * FROM IPConn_Counter", null);
                                                if (connnet_counter.moveToFirst()) {
                                                    ipnamegets_counter = connnet_counter.getString(1);
                                                    portgets_counter = connnet_counter.getString(2);
                                                    statusnets_counter = connnet_counter.getString(3);
                                                }
                                                connnet_counter.close();

                                                Cursor connusb = db1.rawQuery("SELECT * FROM BTConn", null);
                                                if (connusb.moveToFirst()) {
                                                    addgets = connusb.getString(1);
                                                    namegets = connusb.getString(2);
                                                    statussusbs = connusb.getString(3);
                                                }
                                                connusb.close();

                                                textViewstatusnets=new TextView(mContext);
                                                textViewstatusnets_counter=new TextView(mContext);
                                                textViewstatussusbs=new TextView(mContext);
                                                textViewstatusnets.setText(statusnet);
                                                textViewstatusnets_counter.setText(statusnets_counter);
                                                textViewstatussusbs.setText(statussusbs);

                                                Cursor getcom = db1.rawQuery("SELECT * FROM Companydetailss", null);
                                                if (getcom.moveToFirst()) {
                                                    strcompanyname = getcom.getString(1);
                                                }
                                                getcom.close();
                                                tvkot=new TextView(mContext);
                                                tvkot.setText(strcompanyname);
                                                if (tvkot.getText().toString().equals("")) {

                                                } else {
                                                    allbuf1 = new byte[][]{
                                                            bold, cen, strcompanyname.getBytes(), LF, LF

                                                    };


                                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                        BluetoothPrintDriver.BT_Write(bold);    //
                                                        BluetoothPrintDriver.BT_Write(cen);    //
                                                        BT_Write(strcompanyname);
                                                        BluetoothPrintDriver.BT_Write(LF);    //
                                                        BluetoothPrintDriver.BT_Write(LF);    //
                                                    }
                                                }

                                                //int one_11 = textView.getText().toString();
            /*Cursor cursor_6 = db1.rawQuery("Select MAX(tagg) from Table" + ItemIDtable, null);
            if (cursor_6.moveToFirst()) {
                one_11 = cursor_6.getInt(0);
            }
            cursor_6.close();*/

//                                                int one111 = one_11+1;
                                                String one_1 = textView.getText().toString();

                                                allbufKOT = new byte[][]{
                                                        un, cen, "Order Ticket".getBytes(), LF
                                                };
                                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                    BluetoothPrintDriver.BT_Write(normal);    //
                                                    BluetoothPrintDriver.BT_Write(un);    //
                                                    BluetoothPrintDriver.BT_Write(cen);    //
                                                    BT_Write("Order Ticket "+one_1);
                                                    BluetoothPrintDriver.BT_Write(LF);    //
                                                }

                                                /*Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
                                                if (vbnm.moveToFirst()) {
                                                    assa1 = vbnm.getString(1);
                                                    assa2 = vbnm.getString(2);
                                                }
                                                vbnm.close();
                                                TextView cx = new TextView(mContext);
                                                cx.setText(assa1);
                                                String tablen = "";
                                                if (cx.getText().toString().equals("")) {
                                                    tablen = "Tab" + assa2;
                                                } else {
                                                    tablen = "Tab" + assa1;
                                                }*/
                                                String tablen = "Tab" + order_type;

            /*Spinner billing_spinner = (Spinner) findViewById(R.id.billing_spinner);
            NAme111 = billing_spinner.getSelectedItem().toString();*/

                                                allbuf11 = new byte[][]{
                                                        left, un1, setHT321, tablen.getBytes(), LF
                                                };

                                                /*if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                    BluetoothPrintDriver.BT_Write(left);    //
                                                    BluetoothPrintDriver.BT_Write(un1);    //
                                                    BluetoothPrintDriver.BT_Write(setHT321);    //
                                                    BT_Write(NAme111);
                                                    BluetoothPrintDriver.BT_Write(HT);    //
                                                    BT_Write("  ");
                                                    BT_Write(tablen);
                                                    BluetoothPrintDriver.BT_Write(LF);    //
                                                }*/

                                                SimpleDateFormat normal2 = new SimpleDateFormat("dd MMM yyyy");
                                                final String normal1 = normal2.format(new Date());

                                                Date dt = new Date();
                                                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
                                                final String time1 = sdf1.format(dt);

                                                Date dtt_new = new Date();
                                                SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");
                                                String time24_new = sdf1t_new.format(dtt_new);

                                                Date dtt = new Date();
                                                SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
                                                String time24 = sdf1t.format(dtt);

                                                allbuf10 = new byte[][]{
                                                        setHT321, left, normal1.getBytes(), HT, "   ".getBytes(), time1.getBytes(), LF
                                                };

                                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                    BluetoothPrintDriver.BT_Write(setHT321);    //
                                                    BluetoothPrintDriver.BT_Write(left);    //
                                                    BT_Write(normal1);
                                                    BluetoothPrintDriver.BT_Write(HT);    //
                                                    BT_Write("  ");
                                                    BT_Write(time1);
                                                    BluetoothPrintDriver.BT_Write(LF);    //
                                                }


                                                /*allbuf11 = new byte[][]{
                                                        left, setHT321, "Counter person ".getBytes(), LF
                                                };
                                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                    BluetoothPrintDriver.BT_Write(left);    //
                                                    BluetoothPrintDriver.BT_Write(setHT321);    //
                                                    BT_Write("Counter person: "+u_name);
                                                    BluetoothPrintDriver.BT_Write(LF);    //
                                                }*/


                                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                    BluetoothPrintDriver.BT_Write(left);    //
                                                    BluetoothPrintDriver.BT_Write(un1);    //
                                                    BT_Write(str_line);
                                                    BluetoothPrintDriver.BT_Write(LF);    //
                                                }

                                                allbufqty = new byte[][]{
                                                        setHTKOT, normal, "Qty".getBytes(), HT, "Item".getBytes(), left, LF
                                                };

                                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                    BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                                    BluetoothPrintDriver.BT_Write(normal);    //
                                                    BT_Write("Qty");
                                                    BluetoothPrintDriver.BT_Write(HT);    //
                                                    BT_Write(mContext.getString(R.string.action_item));
                                                    BluetoothPrintDriver.BT_Write(left);    //
                                                    BluetoothPrintDriver.BT_Write(LF);    //
                                                }

                                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                    BluetoothPrintDriver.BT_Write(left);    //
                                                    BluetoothPrintDriver.BT_Write(un1);    //
                                                    BT_Write(str_line);
                                                    BluetoothPrintDriver.BT_Write(LF);    //
                                                }

                                                /*int one11 = 0;
                                                Cursor cursor6 = db1.rawQuery("Select MAX(tagg) from Table" + ItemIDtable + "management", null);
                                                if (cursor6.moveToFirst()) {
                                                    one11 = cursor6.getInt(0);
                                                }
                                                cursor6.close();*/
                                                for (int j = 0; j < item.items1.length(); j++) {
                                                    JSONObject items = null;
                                                    try {
                                                        items = item.items1.getJSONObject(j);
                                                        String itemid = items.getString("wera_item_id");
                                                        String item_id = items.getString("item_id");
                                                        String item_name = items.getString("item_name");
                                                        String instructions = items.getString("instructions");
                                                        String item_quantity = items.getString("item_quantity");
                                                        totalquantity=item.no_items;

                                                        String var_name = null;
                                                        JSONArray variant, addons;
                                                        try {
                                                            variant = items.getJSONArray("variants");
                                                        } catch (JSONException e) {
                                                            System.out.println("Json error variants"+ e);
                                                            throw new RuntimeException(e);
                                                        }
                                                        for (int p = 0; p < variant.length(); p++) {
                                                            JSONObject var = variant.getJSONObject(p);
                                                            try {
                                                                String var_id = var.getString("variant_id");
                                                                var_name = var.getString("variant_name");
                                                                String var_size_id = var.getString("size_id");
                                                                String var_size_name = var.getString("size_name");
                                                                String var_price = var.getString("price");
                                                                String var_cgst = var.getString("cgst");
                                                                String var_sgst = var.getString("sgst");
                                                                String var_cgst_percent = var.getString("cgst_percent");
                                                                String var_sgst_percent = var.getString("sgst_percent");
                                                            } catch (JSONException e) {
                                                                System.out.println("Json error variants"+ e);
                                                                throw new RuntimeException(e);
                                                            }
                                                        }
                                                        TextView vari = new TextView(mContext);
                                                        vari.setText(var_name);
                                                        TextView itemname = new TextView(mContext);
                                                        if(vari.getText().toString().isEmpty()) {
                                                            itemname.setText(item_name);
                                                            /*itmqnty.append(item_quantity + "  " + "\n");
                                                            itmname.append(item_name + "     " + "\n");
                                                            ordinst.append(instructions + "     " + "\n");*/
                                                        }else{
                                                            itemname.setText(item_name +"-"+var_name);
                                                            /*itmqnty.append(item_quantity + "  " + "\n");
                                                            itmname.append(item_name +"-"+var_name+ "  " + "\n");
                                                            ordinst.append(instructions + "     " + "\n");*/
                                                        }

                                                        String Itemtype = "Item";
                                                        String total = item_quantity;
//                                                        final String idid = cursor.getString(0);
                                                        String name = itemname.getText().toString();
//                                                        final String iidd = cursor.getString(0);
//                                                        final String hii = cursor.getString(2);
//                                                        final String newtt = cursor.getString(4);
//                                                        final float f = Float.parseFloat(cursor.getString(3));
//                                                        String price = String.valueOf(f);
                                                        String stat = "";
//                                                        String sev = cursor.getString(7);
                                                        String add_note_print = instructions;

                                                        TextView tvbh = new TextView(mContext);

                                                        tvbh.setText(stat);


                                                        TextView tv_add_note_print = new TextView(mContext);
                                                        tv_add_note_print.setText(add_note_print);

                                                        if (tvbh.getText().toString().equals("")) {
                                                            if (Itemtype.toString().equals("Item")) {
                                                                if (name.toString().length() > charlength) {
                                                                    String str1 = name.substring(0, charlength);
                                                                    String str2 = name.substring(charlength);
                                                                    allbufitems = new byte[][]{
                                                                            setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                                                    };
                                                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                                        BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                                                        BluetoothPrintDriver.BT_Write(normal);    //
                                                                        BT_Write(total);
                                                                        BluetoothPrintDriver.BT_Write(HT);    //
                                                                        BT_Write(str1);
                                                                        BluetoothPrintDriver.BT_Write(LF);    //
                                                                        BT_Write("      ");
                                                                        BT_Write(str2);
                                                                        BluetoothPrintDriver.BT_Write(left);    //
                                                                        BluetoothPrintDriver.BT_Write(LF);    //
                                                                    }

                                                                    if (tv_add_note_print.getText().toString().equals("")){

                                                                    }else {
                                                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                                            BluetoothPrintDriver.BT_Write(normal);    //
                                                                            BT_Write(add_note_print);
                                                                            BluetoothPrintDriver.BT_Write(LF);    //
                                                                        }
                                                                    }

                                                                } else {
                                                                    allbufitems = new byte[][]{
                                                                            setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                                                    };
                                                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                                        BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                                                        BluetoothPrintDriver.BT_Write(normal);    //
                                                                        BT_Write(total);
                                                                        BluetoothPrintDriver.BT_Write(HT);    //
                                                                        BT_Write(name);
                                                                        BluetoothPrintDriver.BT_Write(left);    //
                                                                        BluetoothPrintDriver.BT_Write(LF);    //
                                                                    }

                                                                    if (tv_add_note_print.getText().toString().equals("")){

                                                                    }else {
                                                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                                            BluetoothPrintDriver.BT_Write(normal);    //
                                                                            BT_Write(add_note_print);
                                                                            BluetoothPrintDriver.BT_Write(LF);    //
                                                                        }
                                                                    }

                                                                }

                                                                /*ContentValues cv = new ContentValues();
                                                                cv.put("itemname", name);
                                                                cv.put("qty", total);
                                                                cv.put("date", normal1);
                                                                cv.put("time", time1);
                                                                cv.put("par_id", idid);
                                                                cv.put("itemtype", Itemtype);
                                                                cv.put("datetimee_new", time24_new);
                                                                db1.insert("Table" + ItemIDtable + "management", null, cv);*/

                                                                /*Cursor cursor7 = db1.rawQuery("Select * from Table" + ItemIDtable + "management", null);
                                                                if (cursor7.moveToFirst()) {
                                                                    do {
                                                                        String id = cursor7.getString(0);
                                                                        String tagg = cursor7.getString(3);

                                                                        TextView fv = new TextView(mContext);
                                                                        fv.setText(tagg);
                                                                        if (fv.getText().toString().equals("")) {
                                                                            ContentValues cvv = new ContentValues();
                                                                            for (int i = 0; i <= one11; i++) {
                                                                                int j = one11 + 1;
                                                                                cvv.put("tagg", j);
                                                                            }
                                                                            String wherecu = "_id = '" + id + "'";
                                                                           // db1.update("Table" + ItemIDtable + "management", cvv, wherecu, new String[]{});
                                                                        }

                                                                    } while (cursor7.moveToNext());
                                                                }
                                                                cursor7.close();*/

                                                                /*Cursor modcursorc = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE parent = '" + hii + "' AND parentid = '" + idid + "'  ", null);
                                                                if (modcursorc.moveToFirst()) {

                                                                    if (name.toString().length() > charlength) {
                                                                        String str1 = name.substring(0, charlength);
                                                                        String str2 = name.substring(charlength);
                                                                        allbufitems = new byte[][]{
                                                                                setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                                                        };

                                                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                                            BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                                                            BluetoothPrintDriver.BT_Write(normal);    //
                                                                            BT_Write(total);
                                                                            BluetoothPrintDriver.BT_Write(HT);    //
                                                                            BT_Write(str1);
                                                                            BluetoothPrintDriver.BT_Write(LF);    //
                                                                            BT_Write("      ");
                                                                            BT_Write(str2);
                                                                            BluetoothPrintDriver.BT_Write(left);    //
                                                                            BluetoothPrintDriver.BT_Write(LF);    //
                                                                        }

                                                                        if (tv_add_note_print.getText().toString().equals("")){

                                                                        }else {
                                                                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                                                BluetoothPrintDriver.BT_Write(normal);    //
                                                                                BT_Write(add_note_print);
                                                                                BluetoothPrintDriver.BT_Write(LF);    //
                                                                            }
                                                                        }
                                                                    } else {
                                                                        allbufitems = new byte[][]{
                                                                                setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                                                        };
                                                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                                            BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                                                            BluetoothPrintDriver.BT_Write(normal);    //
                                                                            BT_Write(total);
                                                                            BluetoothPrintDriver.BT_Write(HT);    //
                                                                            BT_Write(name);
                                                                            BluetoothPrintDriver.BT_Write(left);    //
                                                                            BluetoothPrintDriver.BT_Write(LF);    //
                                                                        }

                                                                        if (tv_add_note_print.getText().toString().equals("")){

                                                                        }else {

                                                                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                                                BluetoothPrintDriver.BT_Write(normal);    //
                                                                                BT_Write(add_note_print);
                                                                                BluetoothPrintDriver.BT_Write(LF);    //
                                                                            }
                                                                        }

                                                                    }

                                                                    ContentValues cv = new ContentValues();
                                                                    cv.put("itemname", name);
                                                                    cv.put("qty", total);
                                                                    cv.put("date", normal1);
                                                                    cv.put("time", time1);
                                                                    cv.put("par_id", idid);
                                                                    cv.put("itemtype", Itemtype);
                                                                    cv.put("datetimee_new", time24_new);
                                                                    db1.insert("Table" + ItemIDtable + "management", null, cv);

                                                                    Cursor cursor7 = db1.rawQuery("Select * from Table" + ItemIDtable + "management", null);
                                                                    if (cursor7.moveToFirst()) {
                                                                        do {
                                                                            String id = cursor7.getString(0);
                                                                            String tagg = cursor7.getString(3);

                                                                            TextView fv = new TextView(mContext);
                                                                            fv.setText(tagg);
                                                                            if (fv.getText().toString().equals("")) {
                                                                                ContentValues cvv = new ContentValues();
                                                                                for (int i = 0; i <= one11; i++) {
                                                                                    int j = one11 + 1;
                                                                                    cvv.put("tagg", j);
                                                                                }
                                                                                String wherecu = "_id = '" + id + "'";
                                                                                db1.update("Table" + ItemIDtable + "management", cvv, wherecu, new String[]{});
                                                                            }
                                                                        } while (cursor7.moveToNext());
                                                                    }
                                                                    cursor7.close();

                                                                    do {
                                                                        final String modiname = modcursorc.getString(2);
                                                                        final String modiquan = modcursorc.getString(1);
                                                                        String modiprice = modcursorc.getString(3);
                                                                        String moditotal = modcursorc.getString(4);
                                                                        final String modiid = modcursorc.getString(0);
                                                                        String mod = modiname;


                                                                        if (mod.toString().length() > charlength) {
                                                                            String str1 = mod.substring(0, charlength);
                                                                            String str2 = mod.substring(charlength);
                                                                            allbufmodifiers = new byte[][]{
                                                                                    setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                                                            };
                                                                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                                                BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                                                                BluetoothPrintDriver.BT_Write(normal);    //
                                                                                BT_Write("");
                                                                                BluetoothPrintDriver.BT_Write(HT);    //
                                                                                BT_Write(">");
                                                                                BT_Write(str1);
                                                                                BluetoothPrintDriver.BT_Write(LF);    //
                                                                                BT_Write("      ");
                                                                                BT_Write(str2);
                                                                                BluetoothPrintDriver.BT_Write(left);    //
                                                                                BluetoothPrintDriver.BT_Write(LF);    //
                                                                            }

                                                                            if (tv_add_note_print.getText().toString().equals("")){

                                                                            }else {
                                                                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                                                    BluetoothPrintDriver.BT_Write(normal);    //
                                                                                    BT_Write(add_note_print);
                                                                                    BluetoothPrintDriver.BT_Write(LF);    //
                                                                                }
                                                                            }

                                                                        } else {
                                                                            allbufmodifiers = new byte[][]{
                                                                                    setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), mod.getBytes(), left, LF
                                                                            };
                                                                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                                                BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                                                                BluetoothPrintDriver.BT_Write(normal);    //
                                                                                BT_Write("");
                                                                                BluetoothPrintDriver.BT_Write(HT);    //
                                                                                BT_Write(">");
                                                                                BT_Write(mod);
                                                                                BluetoothPrintDriver.BT_Write(left);    //
                                                                                BluetoothPrintDriver.BT_Write(LF);    //
                                                                            }

                                                                            if (tv_add_note_print.getText().toString().equals("")){

                                                                            }else {
                                                                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                                                    BluetoothPrintDriver.BT_Write(normal);    //
                                                                                    BT_Write(add_note_print);
                                                                                    BluetoothPrintDriver.BT_Write(LF);    //
                                                                                }
                                                                            }

                                                                        }

                                                                        ContentValues cvv = new ContentValues();
                                                                        cvv.put("itemname", modiname);
                                                                        cvv.put("qty", modiquan);
                                                                        cvv.put("date", normal1);
                                                                        cvv.put("time", time1);
                                                                        cvv.put("par_id", idid);
                                                                        cv.put("itemtype", "Modifier");
                                                                        //                                db1.insert("Table" + ItemIDtable +"management", null, cvv);

                                                                        Cursor ccursor7 = db1.rawQuery("Select * from Table" + ItemIDtable + "management", null);
                                                                        if (ccursor7.moveToFirst()) {
                                                                            do {
                                                                                String id = ccursor7.getString(0);
                                                                                String tagg = ccursor7.getString(3);

                                                                                TextView fv = new TextView(mContext);
                                                                                fv.setText(tagg);
                                                                                if (fv.getText().toString().equals("")) {
                                                                                    ContentValues ccvv = new ContentValues();
                                                                                    for (int i = 0; i <= one11; i++) {
                                                                                        int j = one11 + 1;
                                                                                        ccvv.put("tagg", j);
                                                                                    }
                                                                                    String wherecu = "_id = '" + id + "'";
                                                                                }

                                                                            } while (ccursor7.moveToNext());
                                                                        }
                                                                        ccursor7.close();

                                                                    } while (modcursorc.moveToNext());
                                                                }
                                                                else {
                                                                    if (name.toString().length() > charlength) {
                                                                        String str1 = name.substring(0, charlength);
                                                                        String str2 = name.substring(charlength);
                                                                        allbufitems = new byte[][]{
                                                                                setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                                                        };
                                                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                                            BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                                                            BluetoothPrintDriver.BT_Write(normal);    //
                                                                            BT_Write(total);
                                                                            BluetoothPrintDriver.BT_Write(HT);    //
                                                                            BT_Write(str1);
                                                                            BluetoothPrintDriver.BT_Write(LF);    //
                                                                            BT_Write("      ");
                                                                            BT_Write(str2);
                                                                            BluetoothPrintDriver.BT_Write(left);    //
                                                                            BluetoothPrintDriver.BT_Write(LF);    //
                                                                        }

                                                                        if (tv_add_note_print.getText().toString().equals("")){

                                                                        }else {
                                                                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                                                BluetoothPrintDriver.BT_Write(normal);    //
                                                                                BT_Write(add_note_print);
                                                                                BluetoothPrintDriver.BT_Write(LF);    //
                                                                            }
                                                                        }

                                                                    } else {
                                                                        allbufitems = new byte[][]{
                                                                                setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                                                        };
                                                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                                            BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                                                            BluetoothPrintDriver.BT_Write(normal);    //
                                                                            BT_Write(total);
                                                                            BluetoothPrintDriver.BT_Write(HT);    //
                                                                            BT_Write(name);
                                                                            BluetoothPrintDriver.BT_Write(left);    //
                                                                            BluetoothPrintDriver.BT_Write(LF);    //
                                                                        }

                                                                        if (tv_add_note_print.getText().toString().equals("")){

                                                                        }else {
                                                                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                                                BluetoothPrintDriver.BT_Write(normal);    //
                                                                                BT_Write(add_note_print);
                                                                                BluetoothPrintDriver.BT_Write(LF);    //
                                                                            }
                                                                        }

                                                                    }

                                                                    ContentValues cv = new ContentValues();
                                                                    cv.put("itemname", name);
                                                                    cv.put("qty", total);
                                                                    cv.put("date", normal1);
                                                                    cv.put("time", time1);
                                                                    cv.put("par_id", idid);
                                                                    cv.put("itemtype", Itemtype);
                                                                    cv.put("datetimee_new", time24_new);
                                                                    db1.insert("Table" + ItemIDtable + "management", null, cv);

                                                                    Cursor cursor7 = db1.rawQuery("Select * from Table" + ItemIDtable + "management", null);
                                                                    if (cursor7.moveToFirst()) {
                                                                        do {
                                                                            String id = cursor7.getString(0);
                                                                            String tagg = cursor7.getString(3);

                                                                            TextView fv = new TextView(mContext);
                                                                            fv.setText(tagg);
                                                                            if (fv.getText().toString().equals("")) {
                                                                                ContentValues cvv = new ContentValues();
                                                                                for (int i = 0; i <= one11; i++) {
                                                                                    int j = one11 + 1;
                                                                                    cvv.put("tagg", j);
                                                                                }
                                                                                String wherecu = "_id = '" + id + "'";
                                                                                db1.update("Table" + ItemIDtable + "management", cvv, wherecu, new String[]{});
                                                                            }

                                                                        } while (cursor7.moveToNext());
                                                                    }
                                                                    cursor7.close();

                                                                }
                                                                modcursorc.close();*/
                                                            }

                                                        }




                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }

                                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                    BluetoothPrintDriver.BT_Write(left);    //
                                                    BluetoothPrintDriver.BT_Write(un1);    //
                                                    BT_Write(str_line);
                                                    BluetoothPrintDriver.BT_Write(LF);    //
                                                }
                                                //feedcut();
                                                String totalqu = "No. of items : " + totalquantity;
                                                allbuf11 = new byte[][]{
                                                        left, setHT321, totalqu.getBytes(), LF
                                                };
                                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                    BluetoothPrintDriver.BT_Write(left);    //
                                                    BluetoothPrintDriver.BT_Write(setHT321);    //
                                                    BT_Write(totalqu);
                                                    BluetoothPrintDriver.BT_Write(LF);    //
                                                }

                                               /* Cursor toalitems = db1.rawQuery("Select * from Table" + ItemIDtable, null);
                                                if (toalitems.moveToFirst()) {
                                                    Cursor toalitems2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity <= '1.0'", null);
                                                    if (toalitems2.moveToFirst()) {
                                                        int toalzx = toalitems2.getCount();
                                                        totalquanret1 = String.valueOf(toalzx);
                                                    }
                                                    toalitems2.close();

                                                    *//*Cursor toalitems1 = db1.rawQuery("Select sum(quantity) from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity > '1.0'", null);
                                                    if (toalitems1.moveToFirst()) {
                                                        int toalzx = toalitems1.getInt(0);
                                                        totalquanret2 = String.valueOf(toalzx);
                                                    }
                                                    toalitems1.close();*//*

                                                    int cvvc = Integer.parseInt(totalquanret1) + Integer.parseInt(totalquanret2);
                                                    String total = String.valueOf(cvvc);
                                                    String totalqu = "No. of items : " + totalquantity;
                                                    allbuf11 = new byte[][]{
                                                            left, setHT321, totalqu.getBytes(), LF
                                                    };
                                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                        BluetoothPrintDriver.BT_Write(left);    //
                                                        BluetoothPrintDriver.BT_Write(setHT321);    //
                                                        BT_Write(totalqu);
                                                        BluetoothPrintDriver.BT_Write(LF);    //
                                                    }
                                                }
                                                toalitems.close();*/

                                                byte[][] allbuf = new byte[][]{
                                                        feedcut2
                                                };
                                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                    BluetoothPrintDriver.BT_Write(feedcut2);    //
                                                    System.out.println("feedcut executed now 2");
                                                }




//                                                Cursor cursor = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE dept_name = '' OR dept_name IS null", null);//replace to cursor = dbHelper.fetchAllHotels();
//                                                if (cursor.moveToFirst()) {
//                                                    do {
//
//
//                                                    } while (cursor.moveToNext());
//                                                }
//                                                cursor.close();

                                                /*if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                    BluetoothPrintDriver.BT_Write(left);    //
                                                    BluetoothPrintDriver.BT_Write(un1);    //
                                                    BT_Write(str_line);
                                                    BluetoothPrintDriver.BT_Write(LF);    //
                                                }
                                                //feedcut();

                                                Cursor toalitems = db1.rawQuery("Select * from Table" + ItemIDtable, null);
                                                if (toalitems.moveToFirst()) {
                                                    Cursor toalitems2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity <= '1.0'", null);
                                                    if (toalitems2.moveToFirst()) {
                                                        int toalzx = toalitems2.getCount();
                                                        totalquanret1 = String.valueOf(toalzx);
                                                    }
                                                    toalitems2.close();

                                                    Cursor toalitems1 = db1.rawQuery("Select sum(quantity) from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity > '1.0'", null);
                                                    if (toalitems1.moveToFirst()) {
                                                        int toalzx = toalitems1.getInt(0);
                                                        totalquanret2 = String.valueOf(toalzx);
                                                    }
                                                    toalitems1.close();

                                                    int cvvc = Integer.parseInt(totalquanret1) + Integer.parseInt(totalquanret2);
                                                    String total = String.valueOf(cvvc);
                                                    String totalqu = "No. of items : " + item_quantity;
                                                    allbuf11 = new byte[][]{
                                                            left, setHT321, totalqu.getBytes(), LF
                                                    };
                                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                        BluetoothPrintDriver.BT_Write(left);    //
                                                        BluetoothPrintDriver.BT_Write(setHT321);    //
                                                        BT_Write(totalqu);
                                                        BluetoothPrintDriver.BT_Write(LF);    //
                                                    }
                                                }
                                                toalitems.close();

                                                byte[][] allbuf = new byte[][]{
                                                        feedcut2
                                                };
                                                if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                    BluetoothPrintDriver.BT_Write(feedcut2);    //
                                                    System.out.println("feedcut executed now 2");
                                                }*/

//            if (str_print_ty.toString().equals("POS")) {
//                if (textViewstatusnets.getText().toString().equals("ok")) {
//                    wifiSocket.WIFI_Write(feedcut2);    //
//                } else {
//                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
//                        wifiSocket2.WIFI_Write(feedcut2);    //
//                    } else {
//                        if (textViewstatussusbs.getText().toString().equals("ok")) {
//                            BluetoothPrintDriver.BT_Write(feedcut2);    //
//                        }
//                    }
//                }
//            }
                                            }
//        Toast.makeText(mContext, "KOT printed", Toast.LENGTH_LONG).show();












/*//                mHandler1 = new MHandler1(BeveragesMenuFragment_Dine_l.this);
//                DrawerService1.addHandler(mHandler1);

                                        Cursor conn = db.rawQuery("SELECT * FROM BTConn", null);
                                        if (conn.moveToFirst()) {
                                            nameget = conn.getString(1);
                                            addget = conn.getString(2);
                                            statussusb = conn.getString(3);
                                        }
                                        conn.close();

                                        Cursor connnet = db.rawQuery("SELECT * FROM IPConn", null);
                                        if (connnet.moveToFirst()) {
                                            ipnameget = connnet.getString(1);
                                            portget = connnet.getString(2);
                                            statusnet = connnet.getString(3);
                                        }
                                        connnet.close();

                                        Cursor c_kot1 = db.rawQuery("SELECT * FROM IPConn_KOT1", null);
                                        if (c_kot1.moveToFirst()) {
                                            ipnamegets_kot1 = c_kot1.getString(1);
                                            portgets_kot1 = c_kot1.getString(2);
                                            statusnets_kot1 = c_kot1.getString(3);
                                            name_kot1 = c_kot1.getString(5);
                                        }
                                        c_kot1.close();

                                        Cursor c_kot2 = db.rawQuery("SELECT * FROM IPConn_KOT2", null);
                                        if (c_kot2.moveToFirst()) {
                                            ipnamegets_kot2 = c_kot2.getString(1);
                                            portgets_kot2 = c_kot2.getString(2);
                                            statusnets_kot2 = c_kot2.getString(3);
                                            name_kot2 = c_kot2.getString(5);
                                        }
                                        c_kot2.close();

                                        Cursor c_kot3 = db.rawQuery("SELECT * FROM IPConn_KOT3", null);
                                        if (c_kot3.moveToFirst()) {
                                            ipnamegets_kot3 = c_kot3.getString(1);
                                            portgets_kot3 = c_kot3.getString(2);
                                            statusnets_kot3 = c_kot3.getString(3);
                                            name_kot3 = c_kot3.getString(5);
                                        }
                                        c_kot3.close();

                                        Cursor c_kot4 = db.rawQuery("SELECT * FROM IPConn_KOT4", null);
                                        if (c_kot4.moveToFirst()) {
                                            ipnamegets_kot4 = c_kot4.getString(1);
                                            portgets_kot4 = c_kot4.getString(2);
                                            statusnets_kot4 = c_kot4.getString(3);
                                            name_kot4 = c_kot4.getString(5);
                                        }
                                        c_kot4.close();

                                        if (statussusb.toString().equals("ok") || statusnet.toString().equals("ok") || statusnets_kot1.toString().equals("ok") ||
                                                statusnets_kot2.toString().equals("ok") || statusnets_kot3.toString().equals("ok") || statusnets_kot4.toString().equals("ok")) {

                                            if (statusnet.toString().equals("ok")) {
                                                wifiSocket.WIFISocket(ipnameget, 9100);
                                            }
                                            if (statusnets_kot1.toString().equals("ok")) {
                                                wifiSocket_kot1.WIFISocket(ipnamegets_kot1, 9100);
                                            }
                                            if (statusnets_kot2.toString().equals("ok")) {
                                                wifiSocket_kot2.WIFISocket(ipnamegets_kot2, 9100);
                                            }
                                            if (statusnets_kot3.toString().equals("ok")) {
                                                wifiSocket_kot3.WIFISocket(ipnamegets_kot3, 9100);
                                            }
                                            if (statusnets_kot4.toString().equals("ok")) {
                                                wifiSocket_kot4.WIFISocket(ipnamegets_kot4, 9100);
                                            }

                                            *//*DownloadMusicfromInternet2 downloadMusicfromInternet2 = new DownloadMusicfromInternet2();
                                            downloadMusicfromInternet2.execute();*//*

                                        } else {

                                            String printer_type="";
                                            Cursor aallrows = db.rawQuery("SELECT * FROM Printer_type WHERE _id = '1'", null);
                                            if (aallrows.moveToFirst()) {
                                                do {
                                                    printer_type = aallrows.getString(1);

                                                } while (aallrows.moveToNext());
                                            }
                                            aallrows.close();

                                            if(printer_type.equalsIgnoreCase("wiseposplus")){

                                                if (statusnet.toString().equals("ok")) {
                                                    wifiSocket.WIFISocket(ipnameget, 9100);
                                                }
                                                if (statusnets_kot1.toString().equals("ok")) {
                                                    wifiSocket_kot1.WIFISocket(ipnamegets_kot1, 9100);
                                                }
                                                if (statusnets_kot2.toString().equals("ok")) {
                                                    wifiSocket_kot2.WIFISocket(ipnamegets_kot2, 9100);
                                                }
                                                if (statusnets_kot3.toString().equals("ok")) {
                                                    wifiSocket_kot3.WIFISocket(ipnamegets_kot3, 9100);
                                                }
                                                if (statusnets_kot4.toString().equals("ok")) {
                                                    wifiSocket_kot4.WIFISocket(ipnamegets_kot4, 9100);
                                                }

                                                *//*DownloadMusicfromInternet2 downloadMusicfromInternet2 = new DownloadMusicfromInternet2();
                                                downloadMusicfromInternet2.execute();*//*

                                            }else {
                                                final Dialog dialogconn = new Dialog(mContext, R.style.notitle);
                                                dialogconn.setContentView(R.layout.dialog_printer_conn_error_kot);

                                                Button conti = dialogconn.findViewById(R.id.ok);
                                                conti.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialogconn.dismiss();
                                                    }
                                                });

                                                dialogconn.show();
                                            }
                                        }*/
                                    }
                                });


                               // ((Preparing_Orders_w) mContext).printer(printbillno);
                                ((Preparing_Orders_w) mContext).new_orders();

                            }
                            else {
                                if (mListener != null) {
                                    mListener.onItemClick(item);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /*class DownloadMusicfromInternet2 extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            try {

                Cursor cursorr8 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE type = 'Modifier'", null);
                if (cursorr8.moveToFirst()) {
                    do {
                        String id = cursorr8.getString(0);
                        String par = cursorr8.getString(6);
                        String par_id = cursorr8.getString(7);

                        Cursor cursorr81 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE itemname = '" + par + "' AND _id = '" + par_id + "'", null);
                        if (cursorr81.moveToFirst()) {
                            String status = cursorr81.getString(16);
                            String tagg = cursorr81.getString(17);
                            String date = cursorr81.getString(18);
                            String time = cursorr81.getString(19);
                            String up_qua = cursorr81.getString(20);

                            ContentValues cv = new ContentValues();
                            cv.put("status", status);
                            cv.put("tagg", tagg);
                            cv.put("date", date);
                            cv.put("time", time);
                            cv.put("updated_quantity", up_qua);

                            String wherecu = "_id = '" + id + "'";
                            db1.update("Table" + ItemIDtable, cv, wherecu, new String[]{});
                        }
                        cursorr81.close();
                    } while (cursorr8.moveToNext());
                }
                cursorr8.close();


                Cursor cursor8 = db1.rawQuery("Select * from Table" + ItemIDtable, null);
                if (cursor8.moveToFirst()) {
                    do {
                        String id = cursor8.getString(0);
                        String up_quan = cursor8.getString(20);
                        String old_qu = cursor8.getString(1);

                        TextView vqw = new TextView(BeveragesMenuFragment_Dine_l.this);
                        vqw.setText(up_quan);

                        if (vqw.getText().toString().equals(old_qu)) {

                        } else {
                            if (vqw.getText().toString().equals("")) {
//                                    Toast.makeText(BeveragesMenuFragment_Dine_l.this, "up quan", Toast.LENGTH_SHORT).show();
                                ContentValues cv = new ContentValues();
                                cv.put("updated_quantity", old_qu);
                                cv.put("status", "");

                                String wherecu = "_id = '" + id + "'";
                                db1.update("Table" + ItemIDtable, cv, wherecu, new String[]{});
                            } else {
                                float a1 = Float.parseFloat(old_qu) - Float.parseFloat(up_quan);
                                String a2 = String.format("%.2f", a1);

                                ContentValues cv = new ContentValues();
                                cv.put("updated_quantity", a2);
                                cv.put("status", "");

                                String wherecu = "_id = '" + id + "'";
                                db1.update("Table" + ItemIDtable, cv, wherecu, new String[]{});
                            }
                        }

                    } while (cursor8.moveToNext());
                }
                cursor8.close();

                Cursor kot = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE status = '' OR status IS NULL", null);
                if (kot.moveToFirst()) {
                    System.out.println("assigned for kot2");
                    Cursor c_kot1 = db.rawQuery("SELECT * FROM IPConn_KOT1", null);
                    if (c_kot1.moveToFirst()) {
                        ipnamegets_kot1 = c_kot1.getString(1);
                        portgets_kot1 = c_kot1.getString(2);
                        statusnets_kot1 = c_kot1.getString(3);
                        name_kot1 = c_kot1.getString(5);
                    }
                    c_kot1.close();

                    Cursor c_kot2 = db.rawQuery("SELECT * FROM IPConn_KOT2", null);
                    if (c_kot2.moveToFirst()) {
                        ipnamegets_kot2 = c_kot2.getString(1);
                        portgets_kot2 = c_kot2.getString(2);
                        statusnets_kot2 = c_kot2.getString(3);
                        name_kot2 = c_kot2.getString(5);
                    }
                    c_kot2.close();

                    Cursor c_kot3 = db.rawQuery("SELECT * FROM IPConn_KOT3", null);
                    if (c_kot3.moveToFirst()) {
                        ipnamegets_kot3 = c_kot3.getString(1);
                        portgets_kot3 = c_kot3.getString(2);
                        statusnets_kot3 = c_kot3.getString(3);
                        name_kot3 = c_kot3.getString(5);
                    }
                    c_kot3.close();

                    Cursor c_kot4 = db.rawQuery("SELECT * FROM IPConn_KOT4", null);
                    if (c_kot4.moveToFirst()) {
                        ipnamegets_kot4 = c_kot4.getString(1);
                        portgets_kot4 = c_kot4.getString(2);
                        statusnets_kot4 = c_kot4.getString(3);
                        name_kot4 = c_kot4.getString(5);
                    }
                    c_kot4.close();


                    String printer_type1="";
                    Cursor aallrows11 = db.rawQuery("SELECT * FROM Printer_type WHERE _id = '1'", null);
                    if (aallrows11.moveToFirst()) {
                        do {
                            printer_type1 = aallrows11.getString(1);

                        } while (aallrows11.moveToNext());
                    }
                    aallrows11.close();

//                        if(printer_type1.equalsIgnoreCase("wiseposplus")) {
//
//                            if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO)
//                            {
//                                mPrintData = new ArrayList<>();
//                                byte[] receiptData = neoprintbillsplithome_kot_BT(name_kot1);
//                                mPrintData.add(receiptData);
//
//
//                                if(mPrintData != null && mPrintData.size() > 0)
//                                {
//                                    mMSWisepadDeviceController.print(mPrintData);
//                                }
//
//                            }else{
//
//                                //   System.out.println("assigned for kot11 "+ns);
//                                wiseposprintbillsplithome_kot_BT(name_kot1);
//                                Handler handler =  new Handler(BeveragesMenuFragment_Dine_l.this.getMainLooper());
//                                handler.post( new Runnable(){
//                                    public void run(){
//                                        Toast.makeText(BeveragesMenuFragment_Dine_l.this, "KOT printed",Toast.LENGTH_LONG).show();
//                                    }
//                                });
//                            }
//
//                        }

                    Cursor cursor1 = db1.rawQuery("Select * from Table" + ItemIDtable +" WHERE dept_name = '"+name_kot1+"'", null);
                    if (cursor1.moveToFirst()) {
                        String ns = cursor1.getString(2);
                        if (statusnets_kot1.toString().equals("ok")) {
                            System.out.println("assigned for kot1 "+ns);
                            printbillsplithome_kot1(name_kot1);
                            Handler handler =  new Handler(BeveragesMenuFragment_Dine_l.this.getMainLooper());
                            handler.post( new Runnable(){
                                public void run(){
                                    Toast.makeText(BeveragesMenuFragment_Dine_l.this, "KOT printed",Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {

                            String printer_type="";
                            Cursor aallrows = db.rawQuery("SELECT * FROM Printer_type WHERE _id = '1'", null);
                            if (aallrows.moveToFirst()) {
                                do {
                                    printer_type = aallrows.getString(1);

                                } while (aallrows.moveToNext());
                            }
                            aallrows.close();

                            if(printer_type.equalsIgnoreCase("wiseposplus")) {

                                if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO)
                                {
                                    mPrintData = new ArrayList<>();
                                    byte[] receiptData = neoprintbillsplithome_kot_BT(name_kot1);
                                    mPrintData.add(receiptData);


                                    if(mPrintData != null && mPrintData.size() > 0)
                                    {
                                        mMSWisepadDeviceController.print(mPrintData);
                                    }

                                }else{

                                    System.out.println("assigned for kot11 "+ns);
                                    wiseposprintbillsplithome_kot_BT(name_kot1);
                                    Handler handler =  new Handler(BeveragesMenuFragment_Dine_l.this.getMainLooper());
                                    handler.post( new Runnable(){
                                        public void run(){
                                            Toast.makeText(BeveragesMenuFragment_Dine_l.this, "KOT printed",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                            }else {
                                System.out.println("assigned for kot11 " + ns);
                                printbillsplithome_kot_BT(name_kot1);
                                Handler handler = new Handler(BeveragesMenuFragment_Dine_l.this.getMainLooper());
                                handler.post(new Runnable() {
                                    public void run() {
                                        Toast.makeText(BeveragesMenuFragment_Dine_l.this, "KOT printed", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                    cursor1.close();

                    Cursor cursor2 = db1.rawQuery("Select * from Table" + ItemIDtable +" WHERE dept_name = '"+name_kot2+"'", null);
                    if (cursor2.moveToFirst()) {
                        String ns = cursor2.getString(2);
                        if (statusnets_kot2.toString().equals("ok")) {
                            System.out.println("assigned for kot2 "+ns);
                            printbillsplithome_kot2(name_kot2);
                            Handler handler =  new Handler(BeveragesMenuFragment_Dine_l.this.getMainLooper());
                            handler.post( new Runnable(){
                                public void run(){
                                    Toast.makeText(BeveragesMenuFragment_Dine_l.this, "KOT printed",Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            System.out.println("assigned for kot22 "+ns);
                            String printer_type="";
                            Cursor aallrows = db.rawQuery("SELECT * FROM Printer_type WHERE _id = '1'", null);
                            if (aallrows.moveToFirst()) {
                                do {
                                    printer_type = aallrows.getString(1);

                                } while (aallrows.moveToNext());
                            }
                            aallrows.close();

                            if(printer_type.equalsIgnoreCase("wiseposplus")) {

                                if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO)
                                {
                                    mPrintData = new ArrayList<>();
                                    byte[] receiptData = neoprintbillsplithome_kot_BT(name_kot2);
                                    mPrintData.add(receiptData);


                                    if(mPrintData != null && mPrintData.size() > 0)
                                    {
                                        mMSWisepadDeviceController.print(mPrintData);
                                    }

                                }else{
                                    wiseposprintbillsplithome_kot_BT(name_kot2);
                                    Handler handler =  new Handler(BeveragesMenuFragment_Dine_l.this.getMainLooper());
                                    handler.post( new Runnable(){
                                        public void run(){
                                            Toast.makeText(BeveragesMenuFragment_Dine_l.this, "KOT printed",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }


                            }else {
                                System.out.println("assigned for kot22 " + ns);
                                printbillsplithome_kot_BT(name_kot2);
                                Handler handler = new Handler(BeveragesMenuFragment_Dine_l.this.getMainLooper());
                                handler.post(new Runnable() {
                                    public void run() {
                                        Toast.makeText(BeveragesMenuFragment_Dine_l.this, "KOT printed", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                    cursor2.close();

                    Cursor cursor3 = db1.rawQuery("Select * from Table" + ItemIDtable +" WHERE dept_name = '"+name_kot3+"'", null);
                    if (cursor3.moveToFirst()) {
                        String ns = cursor3.getString(2);
                        if (statusnets_kot3.toString().equals("ok")) {
                            System.out.println("assigned for kot3 "+ns);
                            printbillsplithome_kot3(name_kot3);
                            Handler handler =  new Handler(BeveragesMenuFragment_Dine_l.this.getMainLooper());
                            handler.post( new Runnable(){
                                public void run(){
                                    Toast.makeText(BeveragesMenuFragment_Dine_l.this, "KOT printed",Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            System.out.println("assigned for kot33 "+ns);
                            printbillsplithome_kot_BT(name_kot3);
                            Handler handler =  new Handler(BeveragesMenuFragment_Dine_l.this.getMainLooper());
                            handler.post( new Runnable(){
                                public void run(){
                                    Toast.makeText(BeveragesMenuFragment_Dine_l.this, "KOT printed",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                    cursor3.close();

                    Cursor cursor4 = db1.rawQuery("Select * from Table" + ItemIDtable +" WHERE dept_name = '"+name_kot4+"'", null);
                    if (cursor4.moveToFirst()) {
                        String ns = cursor4.getString(2);
                        if (statusnets_kot4.toString().equals("ok")) {
                            System.out.println("assigned for kot4 "+ns);
                            printbillsplithome_kot4(name_kot4);
                            Handler handler =  new Handler(BeveragesMenuFragment_Dine_l.this.getMainLooper());
                            handler.post( new Runnable(){
                                public void run(){
                                    Toast.makeText(BeveragesMenuFragment_Dine_l.this, "KOT printed",Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            System.out.println("assigned for kot44 "+ns);
                            printbillsplithome_kot_BT(name_kot4);
                            Handler handler =  new Handler(BeveragesMenuFragment_Dine_l.this.getMainLooper());
                            handler.post( new Runnable(){
                                public void run(){
                                    Toast.makeText(BeveragesMenuFragment_Dine_l.this, "KOT printed",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                    cursor4.close();

                    Cursor cursor_ns = db1.rawQuery("Select * from Table" + ItemIDtable +" WHERE dept_name = '' OR dept_name IS null", null);
                    if (cursor_ns.moveToFirst()) {
                        String na = cursor_ns.getString(2);
                        System.out.println("not assigned "+na);
                        printbillsplithome_kot_BT_NS();
                        Handler handler =  new Handler(BeveragesMenuFragment_Dine_l.this.getMainLooper());
                        handler.post( new Runnable(){
                            public void run(){
                                Toast.makeText(BeveragesMenuFragment_Dine_l.this, "KOT printed",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    cursor_ns.close();

                    int one11 = 0;
                    Cursor cursor6 = db1.rawQuery("Select MAX(tagg) from Table" + ItemIDtable, null);
                    if (cursor6.moveToFirst()) {
                        one11 = cursor6.getInt(0);
                    }
                    cursor6.close();

                    Cursor cursor5 = db1.rawQuery("Select * from Table" + ItemIDtable, null);
                    if (cursor5.moveToFirst()) {
                        do {
                            String id = cursor5.getString(0);
                            ContentValues cv = new ContentValues();
                            cv.put("status", "print");
                            String wherecu = "_id = '" + id + "'";
                            db1.update("Table" + ItemIDtable, cv, wherecu, new String[]{});

                        } while (cursor5.moveToNext());
                    }
                    cursor5.close();

                    SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyyyy");
                    final String normal1 = normal2.format(new Date());

                    Date dt = new Date();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
                    final String time1 = sdf1.format(dt);


                    Cursor cursor7 = db1.rawQuery("Select * from Table" + ItemIDtable, null);
                    if (cursor7.moveToFirst()) {
                        do {
                            String id = cursor7.getString(0);
                            String tagg = cursor7.getString(17);

                            TextView fv = new TextView(BeveragesMenuFragment_Dine_l.this);
                            fv.setText(tagg);
                            if (fv.getText().toString().equals("")) {
                                ContentValues cv = new ContentValues();
                                for (int i = 0; i <= one11; i++) {
                                    int j = one11 + 1;
                                    cv.put("tagg", j);
                                    cv.put("date", normal1);
                                    cv.put("time", time1);
                                }
                                String wherecu = "_id = '" + id + "'";
                                db1.update("Table" + ItemIDtable, cv, wherecu, new String[]{});
                            }

                        } while (cursor7.moveToNext());
                    }
                    cursor7.close();

                    Cursor cursor81 = db1.rawQuery("Select * from Table" + ItemIDtable, null);
                    if (cursor81.moveToFirst()) {
                        do {
                            String id = cursor81.getString(0);
                            String up_quan = cursor81.getString(20);
                            String old_qu = cursor81.getString(1);

                            TextView vqw = new TextView(BeveragesMenuFragment_Dine_l.this);
                            vqw.setText(up_quan);
                            ContentValues cv = new ContentValues();
                            cv.put("updated_quantity", old_qu);

                            String wherecu = "_id = '" + id + "'";
                            db1.update("Table" + ItemIDtable, cv, wherecu, new String[]{});

                        } while (cursor81.moveToNext());
                    }
                    cursor81.close();

                }else {
                    System.out.println("assigned for kot1");
                }
                kot.close();

                SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyyyy");
                final String normal1 = normal2.format(new Date());

                Date dt = new Date();
                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
                final String time1 = sdf1.format(dt);

                Date dtt_new = new Date();
                SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");
                String time24_new = sdf1t_new.format(dtt_new);

                Cursor cursor81 = db1.rawQuery("Select * from Table" + ItemIDtable, null);
                if (cursor81.moveToFirst()) {
                    do {
                        String Itemtype = cursor81.getString(5);
                        String name = cursor81.getString(2);
                        String total = cursor81.getString(1);
                        String sev = cursor81.getString(7);

//                            Toast.makeText(BeveragesMenuFragment_Dine_l.this, "4 " + name, Toast.LENGTH_LONG).show();
                        if (Itemtype.toString().equals("Modifier")) {
//                                Toast.makeText(BeveragesMenuFragment_Dine_l.this, "41 " + name, Toast.LENGTH_LONG).show();
                            Cursor cursor2 = db1.rawQuery("SELECT * FROM Table" + ItemIDtable + "management WHERE par_id = '" + sev + "'", null);
                            if (cursor2.moveToFirst()) {
//                                    Toast.makeText(BeveragesMenuFragment_Dine_l.this, "42 " + name, Toast.LENGTH_LONG).show();
                                String didi = cursor2.getString(3);
                                ContentValues cv = new ContentValues();
                                cv.put("itemname", name);
                                cv.put("qty", total);
                                cv.put("date", normal1);
                                cv.put("time", time1);
                                cv.put("par_id", sev);
                                cv.put("tagg", didi);
                                cv.put("itemtype", Itemtype);
                                cv.put("datetimee_new", time24_new);

                                db1.insert("Table" + ItemIDtable + "management", null, cv);
                            }
                            cursor2.close();
                        }
                    } while (cursor81.moveToNext());
                }
                cursor81.close();


            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected void onPostExecute(Integer file_url) {

            gridView = (HListView) findViewById(R.id.tablegridview);
            LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewroo = inflater1.inflate(R.layout.tables_listview, null);
            if (gridView.isSelected()) {
                tableselected = 0;
            } else {
                if (tableselected == 1) {
                    clickedw(gridView, viewroo, TableIDPos, 1);
                }else {
                    clickedw(gridView, viewroo, TableIDPos, 1);
                }
            }

        }
    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.pendingorder_listview_w, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.setData(mValues.get(position));

    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(DataModel_w item);
    }
   /* class DownloadMusicfromInternet2 extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            try {

                Cursor cursorr8 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE type = 'Modifier'", null);
                if (cursorr8.moveToFirst()) {
                    do {
                        String id = cursorr8.getString(0);
                        String par = cursorr8.getString(6);
                        String par_id = cursorr8.getString(7);

                        Cursor cursorr81 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE itemname = '" + par + "' AND _id = '" + par_id + "'", null);
                        if (cursorr81.moveToFirst()) {
                            String status = cursorr81.getString(16);
                            String tagg = cursorr81.getString(17);
                            String date = cursorr81.getString(18);
                            String time = cursorr81.getString(19);
                            String up_qua = cursorr81.getString(20);

                            ContentValues cv = new ContentValues();
                            cv.put("status", status);
                            cv.put("tagg", tagg);
                            cv.put("date", date);
                            cv.put("time", time);
                            cv.put("updated_quantity", up_qua);

                            String wherecu = "_id = '" + id + "'";
                            db1.update("Table" + ItemIDtable, cv, wherecu, new String[]{});
                        }
                        cursorr81.close();
                    } while (cursorr8.moveToNext());
                }
                cursorr8.close();


                Cursor cursor8 = db1.rawQuery("Select * from Table" + ItemIDtable, null);
                if (cursor8.moveToFirst()) {
                    do {
                        String id = cursor8.getString(0);
                        String up_quan = cursor8.getString(20);
                        String old_qu = cursor8.getString(1);

                        TextView vqw = new TextView(mContext);
                        vqw.setText(up_quan);

                        if (vqw.getText().toString().equals(old_qu)) {

                        } else {
                            if (vqw.getText().toString().equals("")) {
//                                    Toast.makeText(mContext, "up quan", Toast.LENGTH_SHORT).show();
                                ContentValues cv = new ContentValues();
                                cv.put("updated_quantity", old_qu);
                                cv.put("status", "");

                                String wherecu = "_id = '" + id + "'";
                                db1.update("Table" + ItemIDtable, cv, wherecu, new String[]{});
                            } else {
                                float a1 = Float.parseFloat(old_qu) - Float.parseFloat(up_quan);
                                String a2 = String.format("%.2f", a1);

                                ContentValues cv = new ContentValues();
                                cv.put("updated_quantity", a2);
                                cv.put("status", "");

                                String wherecu = "_id = '" + id + "'";
                                db1.update("Table" + ItemIDtable, cv, wherecu, new String[]{});
                            }
                        }

                    } while (cursor8.moveToNext());
                }
                cursor8.close();

                Cursor kot = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE status = '' OR status IS NULL", null);
                if (kot.moveToFirst()) {
                    System.out.println("assigned for kot2");
                    Cursor c_kot1 = db.rawQuery("SELECT * FROM IPConn_KOT1", null);
                    if (c_kot1.moveToFirst()) {
                        ipnamegets_kot1 = c_kot1.getString(1);
                        portgets_kot1 = c_kot1.getString(2);
                        statusnets_kot1 = c_kot1.getString(3);
                        name_kot1 = c_kot1.getString(5);
                    }
                    c_kot1.close();

                    Cursor c_kot2 = db.rawQuery("SELECT * FROM IPConn_KOT2", null);
                    if (c_kot2.moveToFirst()) {
                        ipnamegets_kot2 = c_kot2.getString(1);
                        portgets_kot2 = c_kot2.getString(2);
                        statusnets_kot2 = c_kot2.getString(3);
                        name_kot2 = c_kot2.getString(5);
                    }
                    c_kot2.close();

                    Cursor c_kot3 = db.rawQuery("SELECT * FROM IPConn_KOT3", null);
                    if (c_kot3.moveToFirst()) {
                        ipnamegets_kot3 = c_kot3.getString(1);
                        portgets_kot3 = c_kot3.getString(2);
                        statusnets_kot3 = c_kot3.getString(3);
                        name_kot3 = c_kot3.getString(5);
                    }
                    c_kot3.close();

                    Cursor c_kot4 = db.rawQuery("SELECT * FROM IPConn_KOT4", null);
                    if (c_kot4.moveToFirst()) {
                        ipnamegets_kot4 = c_kot4.getString(1);
                        portgets_kot4 = c_kot4.getString(2);
                        statusnets_kot4 = c_kot4.getString(3);
                        name_kot4 = c_kot4.getString(5);
                    }
                    c_kot4.close();


                    String printer_type1="";
                    Cursor aallrows11 = db.rawQuery("SELECT * FROM Printer_type WHERE _id = '1'", null);
                    if (aallrows11.moveToFirst()) {
                        do {
                            printer_type1 = aallrows11.getString(1);

                        } while (aallrows11.moveToNext());
                    }
                    aallrows11.close();

//                        if(printer_type1.equalsIgnoreCase("wiseposplus")) {
//
//                            if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO)
//                            {
//                                mPrintData = new ArrayList<>();
//                                byte[] receiptData = neoprintbillsplithome_kot_BT(name_kot1);
//                                mPrintData.add(receiptData);
//
//
//                                if(mPrintData != null && mPrintData.size() > 0)
//                                {
//                                    mMSWisepadDeviceController.print(mPrintData);
//                                }
//
//                            }else{
//
//                                //   System.out.println("assigned for kot11 "+ns);
//                                wiseposprintbillsplithome_kot_BT(name_kot1);
//                                Handler handler =  new Handler(mContext.getMainLooper());
//                                handler.post( new Runnable(){
//                                    public void run(){
//                                        Toast.makeText(mContext, "KOT printed",Toast.LENGTH_LONG).show();
//                                    }
//                                });
//                            }
//
//                        }

                    Cursor cursor1 = db1.rawQuery("Select * from Table" + ItemIDtable +" WHERE dept_name = '"+name_kot1+"'", null);
                    if (cursor1.moveToFirst()) {
                        String ns = cursor1.getString(2);
                        if (statusnets_kot1.toString().equals("ok")) {
                            System.out.println("assigned for kot1 "+ns);
                            printbillsplithome_kot1(name_kot1);
                            Handler handler =  new Handler(mContext.getMainLooper());
                            handler.post( new Runnable(){
                                public void run(){
                                    Toast.makeText(mContext, "KOT printed",Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {

                            String printer_type="";
                            Cursor aallrows = db.rawQuery("SELECT * FROM Printer_type WHERE _id = '1'", null);
                            if (aallrows.moveToFirst()) {
                                do {
                                    printer_type = aallrows.getString(1);

                                } while (aallrows.moveToNext());
                            }
                            aallrows.close();

                            if(printer_type.equalsIgnoreCase("wiseposplus")) {

                                if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO)
                                {
                                    mPrintData = new ArrayList<>();
                                    byte[] receiptData = neoprintbillsplithome_kot_BT(name_kot1);
                                    mPrintData.add(receiptData);


                                    if(mPrintData != null && mPrintData.size() > 0)
                                    {
                                        mMSWisepadDeviceController.print(mPrintData);
                                    }

                                }else{

                                    System.out.println("assigned for kot11 "+ns);
                                    wiseposprintbillsplithome_kot_BT(name_kot1);
                                    Handler handler =  new Handler(mContext.getMainLooper());
                                    handler.post( new Runnable(){
                                        public void run(){
                                            Toast.makeText(mContext, "KOT printed",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                            }else {
                                System.out.println("assigned for kot11 " + ns);
                                printbillsplithome_kot_BT(name_kot1);
                                Handler handler = new Handler(mContext.getMainLooper());
                                handler.post(new Runnable() {
                                    public void run() {
                                        Toast.makeText(mContext, "KOT printed", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                    cursor1.close();

                    Cursor cursor2 = db1.rawQuery("Select * from Table" + ItemIDtable +" WHERE dept_name = '"+name_kot2+"'", null);
                    if (cursor2.moveToFirst()) {
                        String ns = cursor2.getString(2);
                        if (statusnets_kot2.toString().equals("ok")) {
                            System.out.println("assigned for kot2 "+ns);
                            printbillsplithome_kot2(name_kot2);
                            Handler handler =  new Handler(mContext.getMainLooper());
                            handler.post( new Runnable(){
                                public void run(){
                                    Toast.makeText(mContext, "KOT printed",Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            System.out.println("assigned for kot22 "+ns);
                            String printer_type="";
                            Cursor aallrows = db.rawQuery("SELECT * FROM Printer_type WHERE _id = '1'", null);
                            if (aallrows.moveToFirst()) {
                                do {
                                    printer_type = aallrows.getString(1);

                                } while (aallrows.moveToNext());
                            }
                            aallrows.close();

                            if(printer_type.equalsIgnoreCase("wiseposplus")) {

                                if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO)
                                {
                                    mPrintData = new ArrayList<>();
                                    byte[] receiptData = neoprintbillsplithome_kot_BT(name_kot2);
                                    mPrintData.add(receiptData);


                                    if(mPrintData != null && mPrintData.size() > 0)
                                    {
                                        mMSWisepadDeviceController.print(mPrintData);
                                    }

                                }else{
                                    wiseposprintbillsplithome_kot_BT(name_kot2);
                                    Handler handler =  new Handler(mContext.getMainLooper());
                                    handler.post( new Runnable(){
                                        public void run(){
                                            Toast.makeText(mContext, "KOT printed",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }


                            }else {
                                System.out.println("assigned for kot22 " + ns);
                                printbillsplithome_kot_BT(name_kot2);
                                Handler handler = new Handler(mContext.getMainLooper());
                                handler.post(new Runnable() {
                                    public void run() {
                                        Toast.makeText(mContext, "KOT printed", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                    cursor2.close();

                    Cursor cursor3 = db1.rawQuery("Select * from Table" + ItemIDtable +" WHERE dept_name = '"+name_kot3+"'", null);
                    if (cursor3.moveToFirst()) {
                        String ns = cursor3.getString(2);
                        if (statusnets_kot3.toString().equals("ok")) {
                            System.out.println("assigned for kot3 "+ns);
                            printbillsplithome_kot3(name_kot3);
                            Handler handler =  new Handler(mContext.getMainLooper());
                            handler.post( new Runnable(){
                                public void run(){
                                    Toast.makeText(mContext, "KOT printed",Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            System.out.println("assigned for kot33 "+ns);
                            printbillsplithome_kot_BT(name_kot3);
                            Handler handler =  new Handler(mContext.getMainLooper());
                            handler.post( new Runnable(){
                                public void run(){
                                    Toast.makeText(mContext, "KOT printed",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                    cursor3.close();

                    Cursor cursor4 = db1.rawQuery("Select * from Table" + ItemIDtable +" WHERE dept_name = '"+name_kot4+"'", null);
                    if (cursor4.moveToFirst()) {
                        String ns = cursor4.getString(2);
                        if (statusnets_kot4.toString().equals("ok")) {
                            System.out.println("assigned for kot4 "+ns);
                            printbillsplithome_kot4(name_kot4);
                            Handler handler =  new Handler(mContext.getMainLooper());
                            handler.post( new Runnable(){
                                public void run(){
                                    Toast.makeText(mContext, "KOT printed",Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            System.out.println("assigned for kot44 "+ns);
                            printbillsplithome_kot_BT(name_kot4);
                            Handler handler =  new Handler(mContext.getMainLooper());
                            handler.post( new Runnable(){
                                public void run(){
                                    Toast.makeText(mContext, "KOT printed",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                    cursor4.close();

                    Cursor cursor_ns = db1.rawQuery("Select * from Table" + ItemIDtable +" WHERE dept_name = '' OR dept_name IS null", null);
                    if (cursor_ns.moveToFirst()) {
                        String na = cursor_ns.getString(2);
                        System.out.println("not assigned "+na);
                        printbillsplithome_kot_BT_NS();
                        Handler handler =  new Handler(mContext.getMainLooper());
                        handler.post( new Runnable(){
                            public void run(){
                                Toast.makeText(mContext, "KOT printed",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    cursor_ns.close();

                    int one11 = 0;
                    Cursor cursor6 = db1.rawQuery("Select MAX(tagg) from Table" + ItemIDtable, null);
                    if (cursor6.moveToFirst()) {
                        one11 = cursor6.getInt(0);
                    }
                    cursor6.close();

                    Cursor cursor5 = db1.rawQuery("Select * from Table" + ItemIDtable, null);
                    if (cursor5.moveToFirst()) {
                        do {
                            String id = cursor5.getString(0);
                            ContentValues cv = new ContentValues();
                            cv.put("status", "print");
                            String wherecu = "_id = '" + id + "'";
                            db1.update("Table" + ItemIDtable, cv, wherecu, new String[]{});

                        } while (cursor5.moveToNext());
                    }
                    cursor5.close();

                    SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyyyy");
                    final String normal1 = normal2.format(new Date());

                    Date dt = new Date();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
                    final String time1 = sdf1.format(dt);


                    Cursor cursor7 = db1.rawQuery("Select * from Table" + ItemIDtable, null);
                    if (cursor7.moveToFirst()) {
                        do {
                            String id = cursor7.getString(0);
                            String tagg = cursor7.getString(17);

                            TextView fv = new TextView(mContext);
                            fv.setText(tagg);
                            if (fv.getText().toString().equals("")) {
                                ContentValues cv = new ContentValues();
                                for (int i = 0; i <= one11; i++) {
                                    int j = one11 + 1;
                                    cv.put("tagg", j);
                                    cv.put("date", normal1);
                                    cv.put("time", time1);
                                }
                                String wherecu = "_id = '" + id + "'";
                                db1.update("Table" + ItemIDtable, cv, wherecu, new String[]{});
                            }

                        } while (cursor7.moveToNext());
                    }
                    cursor7.close();

                    Cursor cursor81 = db1.rawQuery("Select * from Table" + ItemIDtable, null);
                    if (cursor81.moveToFirst()) {
                        do {
                            String id = cursor81.getString(0);
                            String up_quan = cursor81.getString(20);
                            String old_qu = cursor81.getString(1);

                            TextView vqw = new TextView(mContext);
                            vqw.setText(up_quan);
                            ContentValues cv = new ContentValues();
                            cv.put("updated_quantity", old_qu);

                            String wherecu = "_id = '" + id + "'";
                            db1.update("Table" + ItemIDtable, cv, wherecu, new String[]{});

                        } while (cursor81.moveToNext());
                    }
                    cursor81.close();

                }else {
                    System.out.println("assigned for kot1");
                }
                kot.close();

                SimpleDateFormat normal2 = new SimpleDateFormat("ddMMMyyyy");
                final String normal1 = normal2.format(new Date());

                Date dt = new Date();
                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ssaa");
                final String time1 = sdf1.format(dt);

                Date dtt_new = new Date();
                SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");
                String time24_new = sdf1t_new.format(dtt_new);

                Cursor cursor81 = db1.rawQuery("Select * from Table" + ItemIDtable, null);
                if (cursor81.moveToFirst()) {
                    do {
                        String Itemtype = cursor81.getString(5);
                        String name = cursor81.getString(2);
                        String total = cursor81.getString(1);
                        String sev = cursor81.getString(7);

//                            Toast.makeText(mContext, "4 " + name, Toast.LENGTH_LONG).show();
                        if (Itemtype.toString().equals("Modifier")) {
//                                Toast.makeText(mContext, "41 " + name, Toast.LENGTH_LONG).show();
                            Cursor cursor2 = db1.rawQuery("SELECT * FROM Table" + ItemIDtable + "management WHERE par_id = '" + sev + "'", null);
                            if (cursor2.moveToFirst()) {
//                                    Toast.makeText(mContext, "42 " + name, Toast.LENGTH_LONG).show();
                                String didi = cursor2.getString(3);
                                ContentValues cv = new ContentValues();
                                cv.put("itemname", name);
                                cv.put("qty", total);
                                cv.put("date", normal1);
                                cv.put("time", time1);
                                cv.put("par_id", sev);
                                cv.put("tagg", didi);
                                cv.put("itemtype", Itemtype);
                                cv.put("datetimee_new", time24_new);

                                db1.insert("Table" + ItemIDtable + "management", null, cv);
                            }
                            cursor2.close();
                        }
                    } while (cursor81.moveToNext());
                }
                cursor81.close();


            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected void onPostExecute(Integer file_url) {

            gridView = (HListView) findViewById(R.id.tablegridview);
            LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewroo = inflater1.inflate(R.layout.tables_listview, null);
            if (gridView.isSelected()) {
                tableselected = 0;
            } else {
                if (tableselected == 1) {
                    clickedw(gridView, viewroo, TableIDPos, 1);
                }else {
                    clickedw(gridView, viewroo, TableIDPos, 1);
                }
            }

        }
    }*/
   /* public void printbillsplithome_kot_BT_NS() {
        byte[] setHT34M = {0x1b, 0x44, 0x04, 0x11, 0x19, 0x00};
        byte[] dotfeed = {0x1b, 0x4a, 0x10};
        byte[] HTRight = {0x1b, 0x61, 0x02, 0x09};
        byte[] HT = {0x09};
        byte[] HT1 = {0x09};
        byte[] LF = {0x0d, 0x0a};

        byte[] left = {0x1b, 0x61, 0x00};
        byte[] cen = {0x1b, 0x61, 0x01};
        byte[] right = {0x1b, 0x61, 0x02};
        byte[] bold = {0x1B,0x21,0x10};
        byte[] normal = {0x1d, 0x21, 0x00};
        byte[] horiz1 = {0x1b, 0x44, 0x19, 0x19, 0x00};
        byte[] horiz = {0x1b, 0x44, 0x04, 0x06, 0x04, 0x04, 0x0A, 0x00, 0x09, 0x30, 0x09, 0x31, 0x09, 0x32, 0x09, 0x33, 0x09, 0x34, 0x0d, 0x0a};

        byte[] un = {0x1b, 0x2d, 0x02};
        byte[] un1 = {0x1b, 0x2d, 0x00};
        String str_line = "";

        Cursor print_ty = db.rawQuery("SELECT * FROM Printer_type", null);
        if (print_ty.moveToFirst()) {
            str_print_ty = print_ty.getString(1);
        }
        print_ty.close();

        Cursor getprint_type = db.rawQuery("SELECT * FROM Printer_text_size", null);
        if (getprint_type.moveToFirst()) {
            String type = getprint_type.getString(1);

            Cursor cc = db.rawQuery("SELECT * FROM Printerreceiptsize", null);

            if (cc.moveToFirst()) {
                cc.moveToFirst();
                do {
                    NAME = cc.getString(1);
                    if (NAME.equals("3 inch")) {
                        if (str_print_ty.toString().equals("Generic") || str_print_ty.toString().equals("Epson/others")) {
                            setHT32 = new byte[]{0x1b, 0x44, 0x29, 0x00};//2 tabs 3"
                            setHT321 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x25, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                            setHT34 = new byte[]{0x1b, 0x44, 0x08, 0x20, 0x29, 0x00};//4 tabs 3"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 3"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                            nPaperWidth = 576;
                            charlength = 41;
                            HT1 = new byte[]{0x09};
                            str_line = "------------------------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "------------------------------------------------".getBytes(), LF

                            };
                        } else {
                            if (str_print_ty.toString().equals("POS")) {
                                setHT32 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT321 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x10, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x13, 0x27, 0x00};//3 tabs 3"
                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x10, 0x15, 0x00};//4 tabs 3"
                                setHTKOT = new byte[]{0x1b, 0x44, 0x05, 0x00};//2 tabs 3"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                nPaperWidth = 576;
                                charlength = 23;
                                charlength1 = 46;
                                charlength2 = 69;
                                quanlentha = 4;
                                HT1 = new byte[]{0x2F};
                                str_line = "------------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------------".getBytes(), LF

                                };
                            }
                        }
                    } else {
                        if (str_print_ty.toString().equals("Generic")) {
//                            Toast.makeText(mContext, "phi", Toast.LENGTH_SHORT).show();
                            setHT32 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 2"
                            setHT321 = new byte[]{0x1b, 0x44, 0x13, 0x00};//2 tabs 3"
                            setHT3212 = new byte[]{0x1b, 0x44, 0x15, 0x00};//2 tabs 3"
                            setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                            setHT34 = new byte[]{0x1b, 0x44, 0x04, 0x12, 0x19, 0x00};//4 tabs 2"
                            setHTKOT = new byte[]{0x1b, 0x44, 0x06, 0x00};//2 tabs 2"
                            feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                            nPaperWidth = 384;
                            charlength = 25;
                            HT1 = new byte[]{0x09};
                            str_line = "--------------------------------";
                            allbufline = new byte[][]{
                                    left, un1, "--------------------------------".getBytes(), LF
                            };
                        } else {
                            if (str_print_ty.toString().equals("Epson/others")) {
//                            Toast.makeText(mContext, "epson", Toast.LENGTH_SHORT).show();
                                setHT32 = new byte[]{0x1b, 0x44, 0x23, 0x00};//2 tabs 2"
                                setHT321 = new byte[]{0x1b, 0x44, 0x19, 0x00};//2 tabs 3"
                                setHT3212 = new byte[]{0x1b, 0x44, 0x21, 0x00};//2 tabs 3"
                                setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
                                setHT34 = new byte[]{0x1b, 0x44, 0x06, 0x19, 0x21, 0x00};//4 tabs 2"
                                setHTKOT = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                feedcut2 = new byte[]{0x1b, 0x64, 0x03, 0x1d, 0x56, 0x00};
                                nPaperWidth = 384;
                                charlength = 28;
                                HT1 = new byte[]{0x09};
                                str_line = "------------------------------------------";
                                allbufline = new byte[][]{
                                        left, un1, "------------------------------------------".getBytes(), LF
                                };
                            } else {
                                if (str_print_ty.toString().equals("POS")) {
                                    setHT32 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT321 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 2"
                                    setHT3212 = new byte[]{0x1b, 0x44, 0x09, 0x00};//2 tabs 3"
                                    setHT33 = new byte[]{0x1b, 0x44, 0x09, 0x19, 0x00};//3 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x03, 0x12, 0x21, 0x00};//4 tabs 2"
//                                setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x05, 0x08, 0x00};//4 tabs 2"
                                    setHT34 = new byte[]{0x1b, 0x44, 0x02, 0x08, 0x09, 0x00};//4 tabs 2"
                                    setHTKOT = new byte[]{0x1b, 0x44, 0x05, 0x00};//2 tabs 3"
                                    feedcut2 = new byte[]{0x1b, 0x64, 0x05, 0x1d, 0x56, 0x00};
                                    nPaperWidth = 384;
                                    charlength = 11;
                                    charlength1 = 22;
                                    charlength2 = 33;
                                    quanlentha = 3;
                                    HT1 = new byte[]{0x2F};
                                    str_line = "--------------------------------";
                                    allbufline = new byte[][]{
                                            left, un1, "--------------------------------".getBytes(), LF
                                    };
                                }
                            }
                        }
                    }
                } while (cc.moveToNext());
            }
            cc.close();

        }
        getprint_type.close();

        String dd = "";
        TextView qazcvb = new TextView(mContext);
        Cursor cvonnusb = db.rawQuery("SELECT * FROM BTConn", null);
        if (cvonnusb.moveToFirst()) {
            addgets = cvonnusb.getString(1);
            namegets = cvonnusb.getString(2);
            statussusbs = cvonnusb.getString(3);
            dd = cvonnusb.getString(4);
        }
        cvonnusb.close();
        qazcvb.setText(dd);
        if (qazcvb.getText().toString().equals("usb") && statussusbs.toString().equals("ok")) {
           // runPrintCouponSequence();
        } else {

            Cursor connnet = db.rawQuery("SELECT * FROM IPConn", null);
            if (connnet.moveToFirst()) {
                ipnamegets = connnet.getString(1);
                portgets = connnet.getString(2);
                statusnets = connnet.getString(3);
            }
            connnet.close();

            Cursor connnet_counter = db.rawQuery("SELECT * FROM IPConn_Counter", null);
            if (connnet_counter.moveToFirst()) {
                ipnamegets_counter = connnet_counter.getString(1);
                portgets_counter = connnet_counter.getString(2);
                statusnets_counter = connnet_counter.getString(3);
            }
            connnet_counter.close();

            Cursor connusb = db.rawQuery("SELECT * FROM BTConn", null);
            if (connusb.moveToFirst()) {
                addgets = connusb.getString(1);
                namegets = connusb.getString(2);
                statussusbs = connusb.getString(3);
            }
            connusb.close();

            textViewstatusnets.setText(statusnet);
            textViewstatusnets_counter.setText(statusnets_counter);
            textViewstatussusbs.setText(statussusbs);

            Cursor getcom = db.rawQuery("SELECT * FROM Companydetailss", null);
            if (getcom.moveToFirst()) {
                strcompanyname = getcom.getString(1);
            }
            getcom.close();

            tvkot.setText(strcompanyname);
            if (tvkot.getText().toString().equals("")) {

            } else {
                allbuf1 = new byte[][]{
                        bold, cen, strcompanyname.getBytes(), LF, LF

                };


                if (textViewstatussusbs.getText().toString().equals("ok")) {
                    BluetoothPrintDriver.BT_Write(bold);    //
                    BluetoothPrintDriver.BT_Write(cen);    //
                    BT_Write(strcompanyname);
                    BluetoothPrintDriver.BT_Write(LF);    //
                    BluetoothPrintDriver.BT_Write(LF);    //
                }
            }

            //int one_11 = textView.getText().toString();
            *//*Cursor cursor_6 = db1.rawQuery("Select MAX(tagg) from Table" + ItemIDtable, null);
            if (cursor_6.moveToFirst()) {
                one_11 = cursor_6.getInt(0);
            }
            cursor_6.close();*//*

            int one111 = one_11+1;
            String one_1 = textView.getText().toString();

            allbufKOT = new byte[][]{
                    un, cen, "Order Ticket".getBytes(), LF
            };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(normal);    //
                BluetoothPrintDriver.BT_Write(un);    //
                BluetoothPrintDriver.BT_Write(cen);    //
                BT_Write("Order Ticket "+one_1);
                BluetoothPrintDriver.BT_Write(LF);    //
            }

            Cursor vbnm = db.rawQuery("SELECT * FROM asd1 WHERE _id = '" + ItemIDtable + "'", null);
            if (vbnm.moveToFirst()) {
                assa1 = vbnm.getString(1);
                assa2 = vbnm.getString(2);
            }
            vbnm.close();
            TextView cx = new TextView(mContext);
            cx.setText(assa1);
            String tablen = "";
            if (cx.getText().toString().equals("")) {
                tablen = "Tab" + assa2;
            } else {
                tablen = "Tab" + assa1;
            }

            *//*Spinner billing_spinner = (Spinner) findViewById(R.id.billing_spinner);
            NAme111 = billing_spinner.getSelectedItem().toString();*//*

            allbuf11 = new byte[][]{
                    left, un1, setHT321, tablen.getBytes(), LF
            };

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BluetoothPrintDriver.BT_Write(setHT321);    //
                BT_Write(NAme111);
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write("  ");
                BT_Write(tablen);
                BluetoothPrintDriver.BT_Write(LF);    //
            }

            SimpleDateFormat normal2 = new SimpleDateFormat("dd MMM yyyy");
            final String normal1 = normal2.format(new Date());

            Date dt = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
            final String time1 = sdf1.format(dt);

            Date dtt_new = new Date();
            SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");
            String time24_new = sdf1t_new.format(dtt_new);

            Date dtt = new Date();
            SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
            String time24 = sdf1t.format(dtt);

            allbuf10 = new byte[][]{
                    setHT321, left, normal1.getBytes(), HT, "   ".getBytes(), time1.getBytes(), LF
            };

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHT321);    //
                BluetoothPrintDriver.BT_Write(left);    //
                BT_Write(normal1);
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write("  ");
                BT_Write(time1);
                BluetoothPrintDriver.BT_Write(LF);    //
            }


            allbuf11 = new byte[][]{
                    left, setHT321, "Counter person ".getBytes(), LF
            };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(setHT321);    //
                BT_Write("Counter person: "+u_name);
                BluetoothPrintDriver.BT_Write(LF);    //
            }


            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BT_Write(str_line);
                BluetoothPrintDriver.BT_Write(LF);    //
            }

            allbufqty = new byte[][]{
                    setHTKOT, normal, "Qty".getBytes(), HT, "Item".getBytes(), left, LF
            };

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(setHTKOT);    //
                BluetoothPrintDriver.BT_Write(normal);    //
                BT_Write("Qty");
                BluetoothPrintDriver.BT_Write(HT);    //
                BT_Write(mContext.getString(R.string.action_item));
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(LF);    //
            }

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BT_Write(str_line);
                BluetoothPrintDriver.BT_Write(LF);    //
            }

            int one11 = 0;
            Cursor cursor6 = db1.rawQuery("Select MAX(tagg) from Table" + ItemIDtable + "management", null);
            if (cursor6.moveToFirst()) {
                one11 = cursor6.getInt(0);
            }
            cursor6.close();

            Cursor cursor = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE dept_name = '' OR dept_name IS null", null);//replace to cursor = dbHelper.fetchAllHotels();
            if (cursor.moveToFirst()) {
                do {
                    String Itemtype = cursor.getString(5);
                    String total = cursor.getString(20);
                    final String idid = cursor.getString(0);
                    String name = cursor.getString(2);
                    final String iidd = cursor.getString(0);
                    final String hii = cursor.getString(2);
                    final String newtt = cursor.getString(4);
                    final float f = Float.parseFloat(cursor.getString(3));
                    String price = String.valueOf(f);
                    String stat = cursor.getString(16);
                    String sev = cursor.getString(7);
                    String add_note_print = cursor.getString(30);

                    TextView tvbh = new TextView(mContext);

                    tvbh.setText(stat);


                    TextView tv_add_note_print = new TextView(mContext);
                    tv_add_note_print.setText(add_note_print);

                    if (tvbh.getText().toString().equals("")) {
                        if (Itemtype.toString().equals("Item")) {

                            Cursor modcursorc = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE parent = '" + hii + "' AND parentid = '" + idid + "'  ", null);
                            if (modcursorc.moveToFirst()) {
                                Cursor modt = db1.rawQuery("Select SUM(total) from Table" + ItemIDtable + " WHERE parent = '" + name + "' AND parentid = '" + idid + "' ", null);
                                if (modt.moveToFirst()) {
                                    do {
                                        //row.removeView(tv3);
                                        float aq = modt.getFloat(0);
                                        aqq = String.valueOf(aq);
                                        aqq1 = Float.parseFloat(aqq) + Float.parseFloat(newtt);
                                        aqq2 = String.valueOf(aqq1);
                                    } while (modt.moveToNext());
                                }
                                modt.close();

                                if (name.toString().length() > charlength) {
                                    String str1 = name.substring(0, charlength);
                                    String str2 = name.substring(charlength);
                                    allbufitems = new byte[][]{
                                            setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                    };

                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                        BluetoothPrintDriver.BT_Write(normal);    //
                                        BT_Write(total);
                                        BluetoothPrintDriver.BT_Write(HT);    //
                                        BT_Write(str1);
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                        BT_Write("      ");
                                        BT_Write(str2);
                                        BluetoothPrintDriver.BT_Write(left);    //
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }

                                    if (tv_add_note_print.getText().toString().equals("")){

                                    }else {
                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write(add_note_print);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        }
                                    }
                                } else {
                                    allbufitems = new byte[][]{
                                            setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                    };
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                        BluetoothPrintDriver.BT_Write(normal);    //
                                        BT_Write(total);
                                        BluetoothPrintDriver.BT_Write(HT);    //
                                        BT_Write(name);
                                        BluetoothPrintDriver.BT_Write(left);    //
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }

                                    if (tv_add_note_print.getText().toString().equals("")){

                                    }else {

                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write(add_note_print);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        }
                                    }

                                }

                                ContentValues cv = new ContentValues();
                                cv.put("itemname", name);
                                cv.put("qty", total);
                                cv.put("date", normal1);
                                cv.put("time", time1);
                                cv.put("par_id", idid);
                                cv.put("itemtype", Itemtype);
                                cv.put("datetimee_new", time24_new);
                                db1.insert("Table" + ItemIDtable + "management", null, cv);

                                Cursor cursor7 = db1.rawQuery("Select * from Table" + ItemIDtable + "management", null);
                                if (cursor7.moveToFirst()) {
                                    do {
                                        String id = cursor7.getString(0);
                                        String tagg = cursor7.getString(3);

                                        TextView fv = new TextView(mContext);
                                        fv.setText(tagg);
                                        if (fv.getText().toString().equals("")) {
                                            ContentValues cvv = new ContentValues();
                                            for (int i = 0; i <= one11; i++) {
                                                int j = one11 + 1;
                                                cvv.put("tagg", j);
                                            }
                                            String wherecu = "_id = '" + id + "'";
                                            db1.update("Table" + ItemIDtable + "management", cvv, wherecu, new String[]{});
                                        }
                                    } while (cursor7.moveToNext());
                                }
                                cursor7.close();

                                do {
                                    final String modiname = modcursorc.getString(2);
                                    final String modiquan = modcursorc.getString(1);
                                    String modiprice = modcursorc.getString(3);
                                    String moditotal = modcursorc.getString(4);
                                    final String modiid = modcursorc.getString(0);
                                    String mod = modiname;


                                    if (mod.toString().length() > charlength) {
                                        String str1 = mod.substring(0, charlength);
                                        String str2 = mod.substring(charlength);
                                        allbufmodifiers = new byte[][]{
                                                setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                        };
                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write("");
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write(">");
                                            BT_Write(str1);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                            BT_Write("      ");
                                            BT_Write(str2);
                                            BluetoothPrintDriver.BT_Write(left);    //
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        }

                                        if (tv_add_note_print.getText().toString().equals("")){

                                        }else {
                                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                BluetoothPrintDriver.BT_Write(normal);    //
                                                BT_Write(add_note_print);
                                                BluetoothPrintDriver.BT_Write(LF);    //
                                            }
                                        }

                                    } else {
                                        allbufmodifiers = new byte[][]{
                                                setHTKOT, normal, "".getBytes(), HT, ">".getBytes(), mod.getBytes(), left, LF
                                        };
                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write("");
                                            BluetoothPrintDriver.BT_Write(HT);    //
                                            BT_Write(">");
                                            BT_Write(mod);
                                            BluetoothPrintDriver.BT_Write(left);    //
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        }

                                        if (tv_add_note_print.getText().toString().equals("")){

                                        }else {
                                            if (textViewstatussusbs.getText().toString().equals("ok")) {
                                                BluetoothPrintDriver.BT_Write(normal);    //
                                                BT_Write(add_note_print);
                                                BluetoothPrintDriver.BT_Write(LF);    //
                                            }
                                        }

                                    }

                                    ContentValues cvv = new ContentValues();
                                    cvv.put("itemname", modiname);
                                    cvv.put("qty", modiquan);
                                    cvv.put("date", normal1);
                                    cvv.put("time", time1);
                                    cvv.put("par_id", idid);
                                    cv.put("itemtype", "Modifier");
                                    //                                db1.insert("Table" + ItemIDtable +"management", null, cvv);

                                    Cursor ccursor7 = db1.rawQuery("Select * from Table" + ItemIDtable + "management", null);
                                    if (ccursor7.moveToFirst()) {
                                        do {
                                            String id = ccursor7.getString(0);
                                            String tagg = ccursor7.getString(3);

                                            TextView fv = new TextView(mContext);
                                            fv.setText(tagg);
                                            if (fv.getText().toString().equals("")) {
                                                ContentValues ccvv = new ContentValues();
                                                for (int i = 0; i <= one11; i++) {
                                                    int j = one11 + 1;
                                                    ccvv.put("tagg", j);
                                                }
                                                String wherecu = "_id = '" + id + "'";
                                            }

                                        } while (ccursor7.moveToNext());
                                    }
                                    ccursor7.close();

                                } while (modcursorc.moveToNext());
                            } else {
                                if (name.toString().length() > charlength) {
                                    String str1 = name.substring(0, charlength);
                                    String str2 = name.substring(charlength);
                                    allbufitems = new byte[][]{
                                            setHTKOT, normal, total.getBytes(), HT, str1.getBytes(), LF, "      ".getBytes(), str2.getBytes(), left, LF,
                                    };
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                        BluetoothPrintDriver.BT_Write(normal);    //
                                        BT_Write(total);
                                        BluetoothPrintDriver.BT_Write(HT);    //
                                        BT_Write(str1);
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                        BT_Write("      ");
                                        BT_Write(str2);
                                        BluetoothPrintDriver.BT_Write(left);    //
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }

                                    if (tv_add_note_print.getText().toString().equals("")){

                                    }else {
                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write(add_note_print);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        }
                                    }

                                } else {
                                    allbufitems = new byte[][]{
                                            setHTKOT, normal, total.getBytes(), HT, name.getBytes(), left, LF,
                                    };
                                    if (textViewstatussusbs.getText().toString().equals("ok")) {
                                        BluetoothPrintDriver.BT_Write(setHTKOT);    //
                                        BluetoothPrintDriver.BT_Write(normal);    //
                                        BT_Write(total);
                                        BluetoothPrintDriver.BT_Write(HT);    //
                                        BT_Write(name);
                                        BluetoothPrintDriver.BT_Write(left);    //
                                        BluetoothPrintDriver.BT_Write(LF);    //
                                    }

                                    if (tv_add_note_print.getText().toString().equals("")){

                                    }else {
                                        if (textViewstatussusbs.getText().toString().equals("ok")) {
                                            BluetoothPrintDriver.BT_Write(normal);    //
                                            BT_Write(add_note_print);
                                            BluetoothPrintDriver.BT_Write(LF);    //
                                        }
                                    }

                                }

                                ContentValues cv = new ContentValues();
                                cv.put("itemname", name);
                                cv.put("qty", total);
                                cv.put("date", normal1);
                                cv.put("time", time1);
                                cv.put("par_id", idid);
                                cv.put("itemtype", Itemtype);
                                cv.put("datetimee_new", time24_new);
                                db1.insert("Table" + ItemIDtable + "management", null, cv);

                                Cursor cursor7 = db1.rawQuery("Select * from Table" + ItemIDtable + "management", null);
                                if (cursor7.moveToFirst()) {
                                    do {
                                        String id = cursor7.getString(0);
                                        String tagg = cursor7.getString(3);

                                        TextView fv = new TextView(mContext);
                                        fv.setText(tagg);
                                        if (fv.getText().toString().equals("")) {
                                            ContentValues cvv = new ContentValues();
                                            for (int i = 0; i <= one11; i++) {
                                                int j = one11 + 1;
                                                cvv.put("tagg", j);
                                            }
                                            String wherecu = "_id = '" + id + "'";
                                            db1.update("Table" + ItemIDtable + "management", cvv, wherecu, new String[]{});
                                        }

                                    } while (cursor7.moveToNext());
                                }
                                cursor7.close();

                            }
                            modcursorc.close();
                        }

                    }

                } while (cursor.moveToNext());
            }
            cursor.close();

            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(left);    //
                BluetoothPrintDriver.BT_Write(un1);    //
                BT_Write(str_line);
                BluetoothPrintDriver.BT_Write(LF);    //
            }
            //feedcut();

            Cursor toalitems = db1.rawQuery("Select * from Table" + ItemIDtable, null);
            if (toalitems.moveToFirst()) {
                Cursor toalitems2 = db1.rawQuery("Select * from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity <= '1.0'", null);
                if (toalitems2.moveToFirst()) {
                    int toalzx = toalitems2.getCount();
                    totalquanret1 = String.valueOf(toalzx);
                }
                toalitems2.close();

                Cursor toalitems1 = db1.rawQuery("Select sum(quantity) from Table" + ItemIDtable + " WHERE type = 'Item' AND quantity > '1.0'", null);
                if (toalitems1.moveToFirst()) {
                    int toalzx = toalitems1.getInt(0);
                    totalquanret2 = String.valueOf(toalzx);
                }
                toalitems1.close();

                int cvvc = Integer.parseInt(totalquanret1) + Integer.parseInt(totalquanret2);
                String total = String.valueOf(cvvc);
                String totalqu = "No. of items : " + total;
                allbuf11 = new byte[][]{
                        left, setHT321, totalqu.getBytes(), LF
                };
                if (textViewstatussusbs.getText().toString().equals("ok")) {
                    BluetoothPrintDriver.BT_Write(left);    //
                    BluetoothPrintDriver.BT_Write(setHT321);    //
                    BT_Write(totalqu);
                    BluetoothPrintDriver.BT_Write(LF);    //
                }
            }
            toalitems.close();

            byte[][] allbuf = new byte[][]{
                    feedcut2
            };
            if (textViewstatussusbs.getText().toString().equals("ok")) {
                BluetoothPrintDriver.BT_Write(feedcut2);    //
                System.out.println("feedcut executed now 2");
            }

//            if (str_print_ty.toString().equals("POS")) {
//                if (textViewstatusnets.getText().toString().equals("ok")) {
//                    wifiSocket.WIFI_Write(feedcut2);    //
//                } else {
//                    if (textViewstatusnets_counter.getText().toString().equals("ok")) {
//                        wifiSocket2.WIFI_Write(feedcut2);    //
//                    } else {
//                        if (textViewstatussusbs.getText().toString().equals("ok")) {
//                            BluetoothPrintDriver.BT_Write(feedcut2);    //
//                        }
//                    }
//                }
//            }
        }
//        Toast.makeText(mContext, "KOT printed", Toast.LENGTH_LONG).show();
    }*/
    /*private boolean runPrintCouponSequence() {
        if (!initializeObject()) {
            return false;
        }

        if (!createCouponData()) {
            finalizeObject();
            return false;
        }

        if (!printData()) {
            finalizeObject();
            return false;
        }

        return true;
    }
    private boolean initializeObject() {
        try {
            mPrinter = new Printer(((SpnModelsItem) mSpnSeries.getSelectedItem()).getModelConstant(),
                    ((SpnModelsItem) mSpnLang.getSelectedItem()).getModelConstant(),
                    mContext);
        } catch (Exception e) {
//            Toast.makeText(BeveragesMenuFragment_Dine.this, "Here3", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, "Printer", mContext);
            return false;
        }

        mPrinter.setReceiveEventListener(this);

        return true;
    }

    private void finalizeObject() {
        if (mPrinter == null) {
            return;
        }

        mPrinter.clearCommandBuffer();

        mPrinter.setReceiveEventListener(null);

        mPrinter = null;
    }*/
}
