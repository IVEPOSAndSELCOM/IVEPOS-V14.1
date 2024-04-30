package com.intuition.ivepos;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import static com.intuition.ivepos.Stock_Transfer_Processing.getDefaultSharedPreferencesMultiProcess;

public class ConfigureCarAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;
    public int[] checkedChildPosition;  // Save checked child position for each group.

    public ConfigureCarAdapter(Context context, List<String> expandableListTitle,
                               HashMap<String, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        checkedChildPosition = new int[expandableListTitle.size()];
        resetCheckedChildPosition(0);
    }

    @Override
    public String getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String expandedListText = getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item1, null);
        }
        CheckedTextView expandedListTextView = convertView.findViewById(R.id.expandedListItem);

        RelativeLayout rrr = (RelativeLayout) convertView.findViewById(R.id.rrr);

        SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(context);

        final String store_ori= sharedpreferences.getString("storename", null);
        final String device_ori= sharedpreferences.getString("devicename", null);

        final String childText = (String) getChild(listPosition, expandedListPosition);
        final String groupText = (String) getGroup(listPosition);

        if (groupText.toString().equalsIgnoreCase(store_ori) && childText.toString().equalsIgnoreCase(device_ori)){
//            txtListChild.setVisibility(View.GONE);
//            cb.setVisibility(View.GONE);
            rrr.setVisibility(View.GONE);
            expandedListTextView.setVisibility(View.GONE);
        }else {
            expandedListTextView.setText(childText);
        }

//        expandedListTextView.setText(expandedListText);

        // Checked the view if this child is checked.
        expandedListTextView.setChecked(checkedChildPosition[listPosition] == expandedListPosition);

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
    }

    @Override
    public String getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group1, null);
        }
        TextView listTitleTextView = convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);

        TextView selectedChild = convertView.findViewById(R.id.selectedChild);
        if(checkedChildPosition[listPosition] != -1){
            // if group have selected child, get child and display inside header.
            selectedChild.setText(getChild(listPosition, checkedChildPosition[listPosition]));
        }else{
            selectedChild.setText("");
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    // Reset checked childs starts from group "from" to last group.
    private void resetCheckedChildPosition(int from) {
//        if(from < checkedChildPosition.length)
        for(int i=0; i<checkedChildPosition.length; i++) checkedChildPosition[i] = -1;
    }

    // Update adapter when a child is selected.
    public void updateWithChildSelected(int group, int child){
        resetCheckedChildPosition(group);
        checkedChildPosition[group] = child;
        notifyDataSetChanged();
    }

}