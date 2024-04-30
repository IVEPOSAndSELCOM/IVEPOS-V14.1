package com.intuition.ivepos;

import android.app.Dialog;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    ArrayList<DataModel> mValues;
    Context mContext;
    protected ItemListener mListener;

    New_OrderActivity new_orderActivity;
    BeveragesMenuFragment_Dine BeveragesMenuFragment_Dine;

    public RecyclerViewAdapter(Context context, ArrayList<DataModel> values, ItemListener itemListener) {

        mValues = values;
        mContext = context;
        mListener=itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        public TextView acceptorder;
        public TextView rejectorder;
        public TextView no_items;
        public TextView totalprice;
        public TextView order_type;
        public TextView cust_name;
        public TextView time;
        public TextView str_merch_id;
        //        public ImageView imageView;
//        public RelativeLayout relativeLayout;
        DataModel item;

        final String url_accept = "https://api.werafoods.com/pos/v2/order/accept";
        final String url_reject = "https://api.werafoods.com/pos/v2/order/reject";

        JSONObject jsonBody;

        public ViewHolder(View v) {

            super(v);

            v.setOnClickListener(this);
            textView = (TextView) v.findViewById(R.id.order_id);
            acceptorder = (TextView) v.findViewById(R.id.acceptorder);
            rejectorder = (TextView) v.findViewById(R.id.rejectorder);
            no_items = (TextView) v.findViewById(R.id.noitems);
            totalprice = (TextView) v.findViewById(R.id.totalprice);
            order_type = (TextView) v.findViewById(R.id.order_type);
            cust_name = (TextView) v.findViewById(R.id.cust_name);
            time = (TextView) v.findViewById(R.id.time);
            str_merch_id = (TextView) v.findViewById(R.id.rest_id);
//            imageView = (ImageView) v.findViewById(R.id.imageView);
//            relativeLayout = (RelativeLayout) v.findViewById(R.id.relativeLayout);

            acceptorder.setOnClickListener(this);
            rejectorder.setOnClickListener(this);

        }

        public void setData(DataModel item) {
            this.item = item;

            textView.setText(item.text);
            no_items.setText(item.no_items);
            totalprice.setText(item.totalprice);
            order_type.setText(item.order_type);
            cust_name.setText(item.cust_name);
            time.setText(item.time);
            str_merch_id.setText(item.str_merch_id);
//            imageView.setImageResource(item.drawable);
//            relativeLayout.setBackgroundColor(Color.parseColor(item.color));

        }


        @Override
        public void onClick(View view) {
            if (view.getId() == acceptorder.getId()){



                ((New_OrderActivity)mContext).remaining_orders1("1");



//                acceptorder.setText(textView.getText().toString());
                try {
                    //I try to use this for send Header is application/json
                    jsonBody = new JSONObject();
                    jsonBody.put("merchant_id", str_merch_id);
                    jsonBody.put("order_id", textView.getText().toString());
                    jsonBody.put("delivery_time", "45");
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                RequestQueue mQueue = Volley.newRequestQueue(mContext);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url_accept, jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("TAG", response.toString());
//                                Toast.makeText(Pending_order_Activity.this,"accept order", Toast.LENGTH_LONG).show();
//                                new_orderActivity.all_new_orders();
                                ((New_OrderActivity)mContext).all_new_orders();
                                ((New_OrderActivity)mContext).all_new_orders_v();

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
                if (view.getId() == rejectorder.getId()){

                    final Dialog dialog = new Dialog(mContext, R.style.timepicker_date_dialog);
                    dialog.setContentView(R.layout.food_delivery_neworder_reject_dialog);
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

                    TextView reject_ord = (TextView) dialog.findViewById(R.id.reject_ord);
                    reject_ord.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
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

                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url_reject, jsonBody,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d("TAG", response.toString());
//                                          Toast.makeText(Pending_order_Activity.this,"reject order", Toast.LENGTH_LONG).show();
                                            ((New_OrderActivity)mContext).all_new_orders();
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
                    if (mListener != null) {
                        mListener.onItemClick(item);
                    }
                }
            }
        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.neworder_listview, parent, false);

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
