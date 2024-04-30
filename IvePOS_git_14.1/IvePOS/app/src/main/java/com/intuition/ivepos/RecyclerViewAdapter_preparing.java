package com.intuition.ivepos;

import android.app.Dialog;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RecyclerViewAdapter_preparing extends RecyclerView.Adapter<RecyclerViewAdapter_preparing.ViewHolder> {

    ArrayList<DataModel> mValues;
    Context mContext;
    protected ItemListener mListener;

    public RecyclerViewAdapter_preparing(Context context, ArrayList<DataModel> values, ItemListener itemListener) {

        mValues = values;
        mContext = context;
        mListener=itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        public CheckedTextView tick_prepare;
        public CheckedTextView tick_pickup;
        public CheckedTextView tick_deliver;
        public TextView rejectorder;
        public TextView no_items;
        public TextView totalprice;
        public TextView order_type;
        public TextView cust_name;
        public TextView time;
        public String status;
        public TextView deli_per_name;
        public TextView deli_per_phone;
        public TextView str_merch_id;
        //        public ImageView imageView;
//        public RelativeLayout relativeLayout;
        DataModel item;

        final String url_accept = "https://api.werafoods.com/pos/v2/order/accept";
        final String url_reject = "https://api.werafoods.com/pos/v2/order/reject";
        final String url_fodready = "https://api.werafoods.com/pos/v2/order/food-ready";
                final String url_delivered = "https://api.werafoods.com/pos/v2/order/delivered";
//        final String url_delivered = "https://api.werafoods.com/pos/v2/order/getde";
        final String url_pickup = "https://api.werafoods.com/pos/v2/order/pickedup";

        JSONObject jsonBody;

        public ViewHolder(View v) {

            super(v);

            v.setOnClickListener(this);
            textView = (TextView) v.findViewById(R.id.order_id);
            tick_prepare = (CheckedTextView) v.findViewById(R.id.tick_prepare);
            tick_pickup = (CheckedTextView) v.findViewById(R.id.tick_pickup);
            tick_deliver = (CheckedTextView) v.findViewById(R.id.tick_delivered);
//            rejectorder = (TextView) v.findViewById(R.id.rejectorder);
            no_items = (TextView) v.findViewById(R.id.noitems);
            totalprice = (TextView) v.findViewById(R.id.totalprice);
            order_type = (TextView) v.findViewById(R.id.order_type);
            cust_name = (TextView) v.findViewById(R.id.cust_name);
            time = (TextView) v.findViewById(R.id.time);
            deli_per_name = (TextView) v.findViewById(R.id.deli_per_name);
            deli_per_phone = (TextView) v.findViewById(R.id.deli_per_phone);
            str_merch_id = (TextView) v.findViewById(R.id.rest_id);
//            imageView = (ImageView) v.findViewById(R.id.imageView);
//            relativeLayout = (RelativeLayout) v.findViewById(R.id.relativeLayout);

            tick_prepare.setOnClickListener(this);
            tick_pickup.setOnClickListener(this);
            tick_deliver.setOnClickListener(this);
//            rejectorder.setOnClickListener(this);

        }

        public void setData(DataModel item) {
            this.item = item;

            textView.setText(item.text);
            no_items.setText(item.no_items);
            totalprice.setText(item.totalprice);
            order_type.setText(item.order_type);
            cust_name.setText(item.cust_name);
            time.setText(item.time);
            status = item.status;
            deli_per_name.setText(item.deli_per_name);
            deli_per_phone.setText(item.deli_per_phon);
            str_merch_id.setText(item.str_merch_id);
            if (status.toString().equals("Food Ready")){
                tick_prepare.setCheckMarkDrawable(R.drawable.ic_success_green_32dp);
            }
            if (status.toString().equals("Picked Up")){
                tick_prepare.setCheckMarkDrawable(R.drawable.ic_success_green_32dp);
                tick_pickup.setCheckMarkDrawable(R.drawable.ic_success_green_32dp);
            }

            if (status.toString().equals("Delivered")){
                tick_prepare.setCheckMarkDrawable(R.drawable.ic_success_green_32dp);
                tick_pickup.setCheckMarkDrawable(R.drawable.ic_success_green_32dp);
                tick_deliver.setCheckMarkDrawable(R.drawable.ic_success_green_32dp);
            }
//            imageView.setImageResource(item.drawable);
//            relativeLayout.setBackgroundColor(Color.parseColor(item.color));

        }


        @Override
        public void onClick(View view) {
            if (view.getId() == tick_prepare.getId()){
//                tick_prepare.setText(textView.getText().toString());

                final Dialog dialog = new Dialog(mContext, R.style.timepicker_date_dialog);
                dialog.setContentView(R.layout.dialog_confirmation_prepare);
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
                        tick_prepare.setCheckMarkDrawable(R.drawable.ic_success_green_32dp);
                        dialog.dismiss();

                        try {
                            //I try to use this for send Header is application/json
                            jsonBody = new JSONObject();
                            jsonBody.put("merchant_id", str_merch_id);
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
                                        Toast.makeText(mContext,"food ready", Toast.LENGTH_LONG).show();
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
                    }
                });

            }else {
                if (view.getId() == tick_pickup.getId()){
                    tick_pickup.setCheckMarkDrawable(R.drawable.ic_success_green_32dp);
                    try {
                        //I try to use this for send Header is application/json
                        jsonBody = new JSONObject();
                        jsonBody.put("merchant_id", str_merch_id);
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
                                    Log.d("TAG", response.toString());
//                                    Toast.makeText(Pending_order_Activity.this,"reject order", Toast.LENGTH_LONG).show();
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
                }else {
                    if (view.getId() == tick_deliver.getId()){
                        tick_deliver.setCheckMarkDrawable(R.drawable.ic_success_green_32dp);

                        try {
                            //I try to use this for send Header is application/json
                            jsonBody = new JSONObject();
                            jsonBody.put("merchant_id", str_merch_id);
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
                                        Toast.makeText(mContext,"delivered", Toast.LENGTH_LONG).show();

//                                        ((Preparing_Orders_w)mContext).saveinDB(textView.getText().toString());

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

                    }else {
                        if (mListener != null) {
                            mListener.onItemClick(item);
                        }
                    }
                }
            }
        }
    }

    @Override
    public RecyclerViewAdapter_preparing.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.pendingorder_listview, parent, false);

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
        void onItemClick(DataModel item);
    }
}
