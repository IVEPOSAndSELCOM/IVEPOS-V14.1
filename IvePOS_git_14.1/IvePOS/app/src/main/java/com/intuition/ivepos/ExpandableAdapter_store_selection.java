package com.intuition.ivepos;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.intuition.ivepos.dashboard.R;

import static com.intuition.ivepos.Stock_Transfer_Processing.getDefaultSharedPreferencesMultiProcess;

public class ExpandableAdapter_store_selection extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public static ArrayList<String> selected=new ArrayList<String>();

    OnDataChangeListener mOnDataChangeListener;
    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        mOnDataChangeListener = onDataChangeListener;
    }

    public ExpandableAdapter_store_selection(Context context, List<String> listDataHeader,
                                             HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);
        final String groupText = (String) getGroup(groupPosition);
        //   if (convertView == null) {
        LayoutInflater infalInflater = (LayoutInflater) this._context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.group_item, null);
        //  }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.title);

        RelativeLayout rrr = (RelativeLayout) convertView.findViewById(R.id.rrr);

//        txtListChild.setText(childText);

        System.out.println("parent text is "+groupText);
        System.out.println("child text is "+childText);

        final CheckBox cb = (CheckBox) convertView.findViewById(R.id.cb);
        if(selected.contains(groupText+"_"+childText)){
            cb.setChecked(true);
        }else{
            cb.setChecked(false);
        }


        SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(_context);

        final String store_ori= sharedpreferences.getString("storename", null);
        final String device_ori= sharedpreferences.getString("devicename", null);

        if (groupText.toString().equalsIgnoreCase(store_ori) && childText.toString().equalsIgnoreCase(device_ori)){
//            txtListChild.setVisibility(View.GONE);
//            cb.setVisibility(View.GONE);
            rrr.setVisibility(View.GONE);
        }else {
            txtListChild.setText(childText);
        }

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    selected.add(groupText+"_"+childText);
                }else{
                    selected.remove(groupText+"_"+childText);
                }
                Log.e("Selected size is ", String.valueOf(selected.size()));
                if(selected.size()==1){
                    Log.e("Selected devices 1", String.valueOf(selected.size()));

                    SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(_context);
                    String company= sharedpreferences.getString("companyname", null);
                    String[] store_device=ExpandableAdapter_store_selection.selected.get(0).split("_");
                    String store= store_device[0];
                    String device= store_device[1];

                    SharedPreferences.Editor editor = getDefaultSharedPreferencesMultiProcess(_context).edit();
                    editor.putString("storename1",store);
                    editor.putString("devicename1",device);
                    editor.apply();
                    Log.e("store_adapter",store);
                    Log.e("device_adapter",device);

                }else {
                    Log.e("Selected devices 2", String.valueOf(selected.size()));
                }

                if(mOnDataChangeListener != null){
                    mOnDataChangeListener.onDataChanged(selected.size());
                }


            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_list_store, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.title);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public interface OnDataChangeListener{
        public void onDataChanged(int size);
    }
}