package com.intuition.ivepos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;

public class RecyclerViewAdapter_cancel extends RecyclerView.Adapter<com.intuition.ivepos.RecyclerViewAdapter_cancel.ViewHolder> {

        ArrayList<DataModel_w> mValues;
        Context mContext;
    protected ItemListener mListener;

        public RecyclerViewAdapter_cancel (Context context, ArrayList<DataModel_w> values, ItemListener itemListener) {

            mValues = values;
            mContext = context;
            mListener=itemListener;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView textView;
            public TextView no_items;
            public TextView totalprice;
            public TextView order_type;
            public TextView cust_name;
            // public TextView time;
            public TextView status;
            public TextView deli_per_name, items, deli_per_status;
            public TextView deli_per_phone;
            public TextView str_merch_id;
            public TextView cancelreason;

            DataModel_w item;


            JSONObject jsonBody;

            public ViewHolder(View v) {

                super(v);

                v.setOnClickListener(this);
                textView = (TextView) v.findViewById(R.id.order_id);
                //items = (TextView) v.findViewById(R.id.items);
                //no_items = (TextView) v.findViewById(R.id.noitems);
                totalprice = (TextView) v.findViewById(R.id.totalprice);
                order_type = (TextView) v.findViewById(R.id.order_type);
                cust_name = (TextView) v.findViewById(R.id.pcust_name);
                str_merch_id = (TextView) v.findViewById(R.id.rest_id);
                //cancelreason =(TextView) v.findViewById(R.id.cancel_description);
                status= (TextView) v.findViewById(R.id.cancel_reason);


            }

            public void setData(DataModel_w item) {
                this.item = item;

                textView.setText(item.text);
                //no_items.setText(item.no_items);
                totalprice.setText(item.totalprice);
                order_type.setText(item.order_type);
                cust_name.setText(item.cust_name);
                // time.setText(item.time);
                status.setText(item.status);
                str_merch_id.setText(item.str_merch_id);
                //cancelreason.setText(item.order_cancelreason);


            }


            @Override
            public void onClick(View view) {


            }
        }

        @Override
        public com.intuition.ivepos.RecyclerViewAdapter_cancel.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.order_cancled_w, parent, false);

            return new com.intuition.ivepos.RecyclerViewAdapter_cancel.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(com.intuition.ivepos.RecyclerViewAdapter_cancel.ViewHolder Vholder, int position) {
            Vholder.setData(mValues.get(position));

        }

        @Override
        public int getItemCount() {

            return mValues.size();
        }

        public interface ItemListener {
            void onItemClick(DataModel_w item);
        }
    }
