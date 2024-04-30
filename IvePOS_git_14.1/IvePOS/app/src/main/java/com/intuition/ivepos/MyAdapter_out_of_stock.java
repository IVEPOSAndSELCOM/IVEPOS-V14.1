package com.intuition.ivepos;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rohithkumar on 12/9/2016.
 */

public class MyAdapter_out_of_stock extends ArrayAdapter<Country_out_of_stock> {

    private ArrayList<Country_out_of_stock> list;
    private ArrayList<Country_out_of_stock> list1;
    //    private ArrayList<Country_out_of_stock> originalList;
    private final Activity context;
    boolean checkAll_flag = false;
    boolean checkItem_flag = false;
    private CountryFilter filter;
//    private List<Country_out_of_stock> countryList;

    public MyAdapter_out_of_stock(Activity context, ArrayList<Country_out_of_stock> list) {
        super(context, R.layout.row, list);
        this.context = context;
//        this.list = list;
//        this.list = new ArrayList<Country_out_of_stock>();
//        this.list.addAll(list);
        this.list = new ArrayList<Country_out_of_stock>();
        this.list.addAll(list);
        this.list1 = new ArrayList<Country_out_of_stock>();
        this.list1.addAll(list);
    }



    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter  = new MyAdapter_out_of_stock.CountryFilter();
        }
        return filter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.row, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.label);
            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.check);
            viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                    list.get(getPosition).setSelected(buttonView.isChecked()); // Set the value of checkbox to maintain its state.
                }
            });

//            viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(getContext(), "clickdeddddd", Toast.LENGTH_SHORT).show();
//                    int count = 0;
//                    int size = list.size();
//                    for (int i1=0; i1<size; i1++){
//                        if (list.get(i1).isSelected()){
//                            count++;
//                        }
//                    }
//
//                    if(listView.getCount()==count)
//                        chkAll.setChecked(true);
//                    else
//                        chkAll.setChecked(false);
//                }
//            });

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.label, viewHolder.text);
            convertView.setTag(R.id.check, viewHolder.checkbox);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.checkbox.setTag(position); // This line is important.

        viewHolder.text.setText(list.get(position).getName());
//        viewHolder.checkbox.setText(list.get(position).getName());
        viewHolder.checkbox.setChecked(list.get(position).isSelected());

        return convertView;
    }

    private class CountryFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
//            Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0) {
                ArrayList<Country_out_of_stock> filteredItems = new ArrayList<Country_out_of_stock>();
                for(int i = 0, l = list1.size(); i < l; i++) {
                    Country_out_of_stock country = list1.get(i);
                    if(country.toString().toLowerCase().contains(constraint))
                        filteredItems.add(country);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            }
            else {
                synchronized(this) {
                    result.values = list1;
                    result.count = list1.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            list = (ArrayList<Country_out_of_stock>)results.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0, l = list.size(); i < l; i++)
                add(list.get(i));
            notifyDataSetInvalidated();
        }
    }

}
