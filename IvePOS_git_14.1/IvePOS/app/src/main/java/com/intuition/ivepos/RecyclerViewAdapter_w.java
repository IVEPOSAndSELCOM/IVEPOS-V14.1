package com.intuition.ivepos;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RecyclerViewAdapter_w extends RecyclerView.Adapter<RecyclerViewAdapter_w.ViewHolder> {

    ArrayList<DataModel_w> mValues;
    static Context mContext;
    protected static ItemListener mListener;
    DataModel_w item = null;
    static int rr_id = 0;
    static String preptime;
    static JSONArray itemsdetails;

    public RecyclerViewAdapter_w(Context context, ArrayList<DataModel_w> arrayList, ItemListener itemListener) {

        mValues = arrayList;
        mContext = context;
        mListener = itemListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_view_w, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder Vholder, int position) {
        Vholder.setData(mValues.get(position));
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public interface ItemListener {
                void onItemClick(DataModel_w item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, AdapterView.OnItemSelectedListener {

        public TextView textView;
        public TextView acceptorder;
        public TextView rejectorder;
        public LinearLayout items;
        public TextView no_items;
        public TextView totalprice;
        public TextView order_type;
        public JSONArray icart;
        public TextView cust_name, cust_name1, custphno, custemail, custadd, custdarea, custdins;
        public TextView swiggysupport;
        // public TextView time;
        public TextView str_merch_id, ordinstr;
        DataModel_w item;

        final String url_accept = "https://api.werafoods.com/pos/v2/order/accept";
        final String url_reject = "https://api.werafoods.com/pos/v2/order/reject";
        final String url_pending = "https://api.werafoods.com/pos/v2/merchant/pendingorders";
        final String get_cust = "https://api.werafoods.com/pos/v2/order/getcustomernumber";


        final String url_swiggysupport = "https://api.werafoods.com/pos/v2/order/callsupport";

        JSONObject jsonBody;

        public ViewHolder(View v) {
            super(v);

            v.setOnClickListener(this);
            textView = (TextView) v.findViewById(R.id.order_id);
            acceptorder = (TextView) v.findViewById(R.id.acceptorder);
            rejectorder = (TextView) v.findViewById(R.id.rejectorder);
            items = (LinearLayout) v.findViewById(R.id.items);
            no_items = (TextView) v.findViewById(R.id.item_number);
            totalprice = (TextView) v.findViewById(R.id.totalprice);
            order_type = (TextView) v.findViewById(R.id.order_type);
            cust_name = (TextView) v.findViewById(R.id.cust_name);
            swiggysupport = (TextView) v.findViewById(R.id.swiggysupportb);


            // time = (TextView) v.findViewById(R.id.time);
            str_merch_id = (TextView) v.findViewById(R.id.rest_id);
//            imageView = (ImageView) v.findViewById(R.id.imageView);
//            relativeLayout = (RelativeLayout) v.findViewById(R.id.relativeLayout);

            acceptorder.setOnClickListener(this);
            rejectorder.setOnClickListener(this);
            items.setOnClickListener(this);
            cust_name.setOnClickListener(this);
            swiggysupport.setOnClickListener(this);
        }

        public void setData(DataModel_w item) {
            this.item = item;

            textView.setText(item.text);
            no_items.setText(item.no_items);
            totalprice.setText(item.totalprice);
            order_type.setText(item.order_type);
            cust_name.setText(item.cust_name);
            str_merch_id.setText(item.str_merch_id);
            itemsdetails=item.items1;
            /*cust_name1.setText(item.cust_name);
            custphno.setText(item.cust_phno);
            custemail.setText(item.cust_email);
            custadd.setText(item.cust_address);
            custdarea.setText(item.cust_deliv_area);
            custdins.setText(item.cust_deli_instru);*/



        }

        @Override
        public void onClick(View view) {

            if (view.getId() == acceptorder.getId()) {

                ((New_OrderActivity_w) mContext).remaining_orders1("1");

                final Dialog dialog = new Dialog(mContext, R.style.timepicker_date_dialog);
                dialog.setContentView(R.layout.preparation_time_w);
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

                TextView cancel = (TextView) dialog.findViewById(R.id.preparationtime);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        EditText itmname = (EditText) dialog.findViewById(R.id.ptimer);
                        preptime=itmname.getText().toString();


                acceptorder.setText(textView.getText().toString());
                try {
                    //I try to use this for send Header is application/json
                    jsonBody = new JSONObject();
                    jsonBody.put("merchant_id", str_merch_id.getText().toString());
                    jsonBody.put("order_id", textView.getText().toString());
                    jsonBody.put("preparation_time", preptime);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }


                RequestQueue mQueue = Volley.newRequestQueue(mContext);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url_accept, jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(mContext, "Order accepted", Toast.LENGTH_SHORT).show();
                                Log.d("TAG", response.toString());
                                String staus="Accepted";
                                //((New_OrderActivity_w) mContext).backend();
                                System.out.println(" Order ID " + textView.getText().toString());
                                ((New_OrderActivity_w) mContext).updateweraindextable(textView.getText().toString(), staus, item.no_items);
//                                ((New_OrderActivity_w) mContext).saveinDB(textView.getText().toString());
                                //((New_OrderActivity_w) mContext).updateorderdatadb(textView.getText().toString(), staus, item.no_items);
                                /*final Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {*/
                                // Do something after 10 seconds
                                //((New_OrderActivity_w) mContext).all_new_orders();
                                ((New_OrderActivity_w) mContext).newbackend();
                                //((New_OrderActivity_w) mContext).all_new_orders_v();
                                                        /*}
                                                    }, 1000);*/
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
                //((New_OrderActivity_w) mContext).refresh();


                //------------------------------------------------------
                    }
                });



            } else {
                if (view.getId() == rejectorder.getId()) {

                    if(item.order_type.equalsIgnoreCase("swiggy")){
                        Toast.makeText(mContext, "Please contact Swiggy support for Swiggy orders", Toast.LENGTH_LONG).show();
                    } else {

                        //Toast.makeText(mContext, "Order Rejected", Toast.LENGTH_SHORT).show();
                        final Dialog dialog = new Dialog(mContext, R.style.timepicker_date_dialog);
                        dialog.setContentView(R.layout.food_delivery_neworder_reject_dialog_w);
                        dialog.show();

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

                        Spinner reason_reject_order = (Spinner) dialog.findViewById(R.id.reason_reject_order);
                        String reason = reason_reject_order.getSelectedItem().toString();
                        final String[] text = new String[1];
                        reason_reject_order.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String rreason = reason_reject_order.getSelectedItem().toString();
                                text[0] = parent.getItemAtPosition(position).toString();

                                switch (text[0]) {
                                    case "Select Rejection reason":
                                        rr_id = 0;
                                        break;
                                    case "Items out of stock":
                                        rr_id = 1;
                                        break;
                                    case "No delivery boys available":
                                        rr_id = 2;
                                        break;
                                    case "Nearing closing time":
                                        rr_id = 3;
                                        break;
                                    case "Out of Subzone/Area":
                                        rr_id = 4;
                                        break;
                                    case "Kitchen is Full":
                                        rr_id = 5;
                                        break;
                                    default:
                                        throw new IllegalStateException("Unexpected value: " + text[0]);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                        TextView reject_ord = (TextView) dialog.findViewById(R.id.reject_ord);
                        reject_ord.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                System.out.println("Reject reason code is " + rr_id + " and reason is " + text[0]);

                                if (rr_id == 0) {
                                    Toast.makeText(mContext, "Select the rejection reason", Toast.LENGTH_LONG).show();
                                } else {
                                    dialog.dismiss();
                                    try {
                                        //I try to use this for send Header is application/json
                                        jsonBody = new JSONObject();
                                        jsonBody.put("merchant_id", str_merch_id.getText().toString());
                                        jsonBody.put("order_id", textView.getText().toString());
                                        jsonBody.put("rejection_id", rr_id);
                                    } catch (JSONException ex) {
                                        ex.printStackTrace();
                                    }

                                    RequestQueue mQueue = Volley.newRequestQueue(mContext);

                                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url_reject, jsonBody,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    Log.d("TAG", response.toString());
//                                          Toast.makeText(Pending_order_Activity.this,"reject order", Toast.LENGTH_LONG).show();
                                                    String staus="Reject";
                                                    ((New_OrderActivity_w) mContext).updateweraindextable(textView.getText().toString(), staus, item.no_items);
                                                   // ((New_OrderActivity_w) mContext).updateorderdatadb(textView.getText().toString(), staus, item.no_items);
                                                    /*final Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {*/
                                                            // Do something after 10 seconds
                                                            //((New_OrderActivity_w) mContext).all_new_orders();
                                                    //((New_OrderActivity_w) mContext).all_new_orders();
                                                      //      ((New_OrderActivity_w) mContext).all_new_orders_v();
                                                        /*}
                                                    }, 5000);*/
                                                    ((New_OrderActivity_w) mContext).newbackend();


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
                                    //((New_OrderActivity_w) mContext).refresh();
                                    Toast.makeText(mContext, "Order Rejected", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    if (view.getId() == items.getId()) {

                        //((New_OrderActivity_w) mContext).order_items(item.text, str_merch_id.getText().toString(), mContext);

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
                                                                itmqnty.append(item_quantity + "  " + "\n");
                                                                itmname.append(item_name +"-"+var_name+ "  " + "\n");
                                                                ordinst.append(instructions + "     " + "\n");
                                                            }
                                                        } catch (JSONException e) {
                                                            throw new RuntimeException(e);
                                                        }
                                                    }
                        //ordinst.setText(instructions);

                    } else {
                        if (view.getId() == cust_name.getId()) {

                            //((New_OrderActivity_w) mContext).customer_details(item.text, str_merch_id.getText().toString(), mContext);




                            final Dialog dialog = new Dialog(mContext, R.style.timepicker_date_dialog);
                            dialog.setContentView(R.layout.customer_details_w);
                            dialog.show();
                            Context icontext = mContext;

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


                            String order_id = textView.getText().toString();

                            custname.setText(item.cust_name);
                            custphno.setText(item.cust_phno);
                            custemail.setText(item.cust_email);
                            custadd.setText(item.cust_address);
                            custdarea.setText(item.cust_deliv_area);
                            custdins.setText(item.cust_deli_instru);



                        } else {
                            if (view.getId() == swiggysupport.getId()) {

                                if(item.order_type.equalsIgnoreCase("Swiggy")) {
                                    final Dialog dialog = new Dialog(mContext, R.style.timepicker_date_dialog);
                                    dialog.setContentView(R.layout.call_swiggy_support_w);
                                    dialog.show();

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

                                    Spinner reason_swiggysupport = (Spinner) dialog.findViewById(R.id.reason_swiggysupport);
                                    //String reason = reason_swiggysupport.getSelectedItem().toString();
                                    final String[] reason = new String[1];
                                    reason_swiggysupport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            String rreason = reason_swiggysupport.getSelectedItem().toString();
                                            reason[0] = parent.getItemAtPosition(position).toString();
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });

                                    TextView reject_ord = (TextView) dialog.findViewById(R.id.reject_ord);
                                    reject_ord.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                            System.out.println("Support call reason code is " + reason[0]);

                                            try {
                                                //I try to use this for send Header is application/json
                                                jsonBody = new JSONObject();
                                                jsonBody.put("merchant_id", str_merch_id.getText().toString());
                                                jsonBody.put("order_id", textView.getText().toString());
                                                jsonBody.put("remark", reason);
                                            } catch (JSONException ex) {
                                                ex.printStackTrace();
                                            }

                                            RequestQueue mQueue = Volley.newRequestQueue(mContext);

                                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url_swiggysupport, jsonBody,
                                                    new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            Log.d("TAG", response.toString());
                                                            String staus="Cancelled by swiggy";
                                                            ((New_OrderActivity_w) mContext).updateweraindextable(textView.getText().toString(), staus, item.no_items);
                                                           // ((New_OrderActivity_w) mContext).updateorderdatadb(textView.getText().toString(), staus, item.no_items);
                                                            /*((New_OrderActivity_w) mContext).all_new_orders();
                                                            ((New_OrderActivity_w) mContext).all_new_orders_v();*/
                                                            ((New_OrderActivity_w) mContext).newbackend();

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
                                            Toast.makeText(mContext, "Swiggy support contacted", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else {Toast.makeText(mContext, "Available only for Swiggy orders", Toast.LENGTH_SHORT).show();}
                            } else {
                                if (mListener != null) {
                                    mListener.onItemClick(item);
                                }
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             /*String text = parent.getItemAtPosition(position).toString();
            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
            switch (text){
                case "Select Rejection reason": rr_id=0; break;
                case "Items out of stock": rr_id=1; break;
                case "No delivery boys available": rr_id=2;break;
                case "Nearing closing time": rr_id=3; break;
                case "Out of Subzone/Area": rr_id=4;break;
                case "Kitchen is Full": rr_id=5; break;
            }

             */

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

}

