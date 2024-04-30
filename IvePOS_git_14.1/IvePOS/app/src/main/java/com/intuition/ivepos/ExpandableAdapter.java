package com.intuition.ivepos;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.intuition.ivepos.csv.RequestSingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    private LayoutInflater layoutInflater;
    private LinkedHashMap<Item, ArrayList<Item>> groupList;
    private ArrayList<Item> mainGroup;
    private int[] groupStatus;
    private ExpandableListView listView;

    SQLiteDatabase db = null;
    private int positionMax;
    private int checkAccumulator = 0;
    private int uncheckAccumulator = 0;
    private int x = 0;

    TextView selection;
    String co;
    int i = 1, j = 1, k = 1;
    private Context context;

    String WebserviceUrl;


    public ExpandableAdapter(Context context, ExpandableListView listView,
                             LinkedHashMap<Item, ArrayList<Item>> groupsList, TextView selection) {

        db = context.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);


        layoutInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        this.groupList = groupsList;
        groupStatus = new int[groupsList.size()];

        this.selection = selection;
        this.context = context;

//        selection = new TextView(context);
//        selection = selection1;

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(context);
        String account_selection= sharedpreferences_select.getString("account_selection", null);

        if (account_selection.toString().equals("Dine")) {
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }else {
            if (account_selection.toString().equals("Qsr")) {
                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
            }else {
                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
            }
        }

        Cursor cursor1 = db.rawQuery("SELECT COUNT(status_temp) FROM Items WHERE status_temp = 'yes'", null);
        if (cursor1.moveToFirst()){
            int l = cursor1.getInt(0);
            co = String.valueOf(l);
        }
        cursor1.close();

//        Toast.makeText(context, "total selected "+co, Toast.LENGTH_LONG).show();
        selection.setText(co+" selected");

        listView.setOnGroupExpandListener(new OnGroupExpandListener() {

            public void onGroupExpand(int groupPosition) {
                Item group = mainGroup.get(groupPosition);
                if (groupList.get(group).size() > 0)
                    groupStatus[groupPosition] = 1;

            }
        });

        listView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            public void onGroupCollapse(int groupPosition) {
                Item group = mainGroup.get(groupPosition);
                if (groupList.get(group).size() > 0)
                    groupStatus[groupPosition] = 0;

            }
        });

        mainGroup = new ArrayList<Item>();
        for (Map.Entry<Item, ArrayList<Item>> mapEntry : groupList.entrySet()) {
            mainGroup.add(mapEntry.getKey());
        }


    }

    public Item getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        Item item = mainGroup.get(groupPosition);
        return groupList.get(item).get(childPosition);

    }

    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub

        final ChildHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.group_item, null);
            holder = new ChildHolder();
            holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.text = (TextView) convertView.findViewById(R.id.text);

            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }


        final Item child = getChild(groupPosition, childPosition);
        holder.cb.setChecked(child.isChecked);
        holder.title.setText(child.name);

        System.out.println("selected id is itemm "+holder.title.getText().toString());

        Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + holder.title.getText().toString() + "'", null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String st_temp = cursor.getString(36);
                String st_perm = cursor.getString(37);

                TextView tv = new TextView(parent.getContext());
                tv.setText(st_temp);

                TextView tv1 = new TextView(parent.getContext());
                tv1.setText(st_perm);

                if (tv.getText().toString().equals("yes")) {
                    System.out.println("selected id is item " + id + holder.title.getText().toString());
                    holder.cb.setChecked(true);
                } else {
                    holder.cb.setChecked(false);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (i == 1) {
            Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE itemname = '" + holder.title.getText().toString() + "'", null);
            if (cursor1.moveToFirst()) {
                do {
                    String id = cursor1.getString(0);
                    String st_temp = cursor1.getString(36);
                    String st_perm = cursor1.getString(37);

                    TextView tv = new TextView(parent.getContext());
                    tv.setText(st_temp);

                    TextView tv1 = new TextView(parent.getContext());
                    tv1.setText(st_perm);

                    if (tv1.getText().toString().equals("yes") || tv.getText().toString().equals("yes")) {
                        System.out.println("selected id is item " + id + holder.title.getText().toString());
                        holder.cb.setChecked(true);
                    } else {
                        holder.cb.setChecked(false);
                    }
                } while (cursor1.moveToNext());
            }
            cursor1.close();
        }

        final View finalConvertView = convertView;

        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                j++;
                k++;

                Item parentGroup = getGroup(groupPosition);
                child.isChecked = holder.cb.isChecked();

                //if the CHILD is checked
                //if the CHILD is checked
                //TODO: Here add/remove from list

                if (holder.cb.isChecked()) {
//                    Toast.makeText(parent.getContext(), "checked child", Toast.LENGTH_LONG).show();
                    x = x + 1;
//                    holder.text.setText(x);
//					countCheck(isChecked);
                    Log.i("MAIN", x + "");

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("status_temp", "yes");
                    String where = " itemname ='" + holder.title.getText().toString() + "'";
                    db.update("Items", contentValues, where, new String[]{});
                    webservicequery("UPDATE Items set status_temp = 'yes' WHERe itemname = '"+holder.title.getText().toString()+"'");

                    String where1_v1 = "itemname ='" + holder.title.getText().toString() + "'";
                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                    ArrayList<Item> childList = getChild(parentGroup);
                    int childIndex = childList.indexOf(child);
                    boolean isAllChildClicked = true;
                    for (int i = 0; i < childList.size(); i++) {
                        if (i != childIndex) {
                            Item siblings = childList.get(i);
                            if (!siblings.isChecked) {
                                isAllChildClicked = false;
                                //if(DataHolder.checkedChilds.containsKey(child.name)==false){
                                DataHolder.checkedChilds.put(child.name,
                                        parentGroup.name);
                                // }
                                break;
                            }
                        }
                    }

                    //All the children are checked
                    if (isAllChildClicked) {
                        Log.i("All should be checked", "Each child is Clicked1!!");
                        parentGroup.isChecked = true;
                        if (!(DataHolder.checkedChilds.containsKey(child.name) == true)) {
                            DataHolder.checkedChilds.put(child.name,
                                    parentGroup.name);
                        }
                        checkAll = false;
                    }
                }
                //not all of the children are checked
                else {
//                    Toast.makeText(parent.getContext(), "unchecked child", Toast.LENGTH_LONG).show();
                    x = x - 1;
//					countCheck(isChecked);
                    Log.i("MAIN", x + "");

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("status_temp", "");
                    contentValues.put("status_perm", "");
                    String where = " itemname ='" + holder.title.getText().toString() + "'";
                    db.update("Items", contentValues, where, new String[]{});

                    webservicequery("UPDATE Items set status_temp = '' AND status_perm = '' WHERe itemname = '"+holder.title.getText().toString()+"'");

                    String where1_v1 = "itemname ='" + holder.title.getText().toString() + "'";
                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                    if (parentGroup.isChecked) {
                        parentGroup.isChecked = false;
                        checkAll = false;
                        DataHolder.checkedChilds.remove(child.name);
                    } else {
                        checkAll = true;
                        DataHolder.checkedChilds.remove(child.name);
                    }
                    // child.isChecked =false;
                }
                notifyDataSetChanged();

                Cursor cursor1 = db.rawQuery("SELECT COUNT(status_temp) FROM Items WHERE status_temp = 'yes'", null);
                if (cursor1.moveToFirst()){
                    int l = cursor1.getInt(0);
                    co = String.valueOf(l);
                }
                cursor1.close();

//                Toast.makeText(parent.getContext(), "total selected "+co, Toast.LENGTH_LONG).show();
                selection.setText(co+" selected");

            }
        });

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        Item item = mainGroup.get(groupPosition);
        return groupList.get(item).size();
    }

    public Item getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return mainGroup.get(groupPosition);
    }

    public int getGroupCount() {
        // TODO Auto-generated method stub
        return mainGroup.size();
    }

    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return 0;
    }
    //works with the GroupView

    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, final ViewGroup parent) {

        final GroupHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.group_list, null);
            holder = new GroupHolder();
            holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
//            holder.cb.setChecked(true);
            holder.imageView = (ImageView) convertView
                    .findViewById(R.id.label_indicator);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }

        holder.imageView
                .setImageResource(groupStatus[groupPosition] == 0 ? R.drawable.ic_arrow_drop_down_black_24dp
                        : R.drawable.ic_arrow_drop_up_black_24dp);
        final Item groupItem = getGroup(groupPosition);
        holder.cb.setChecked(groupItem.isChecked);
        holder.title.setText(groupItem.name);

        final SharedPreferences sharedPreferences = parent.getContext().getSharedPreferences("yourSharedPref", 0);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE category = '" + holder.title.getText().toString() + "' GROUP BY category", null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String st_temp = cursor.getString(36);
                String st_perm = cursor.getString(37);

                int count1 = 0, count2 = 0;
                Cursor c1 = db.rawQuery("SELECT count(*) FROM Items WHERE category = '" + holder.title.getText().toString() + "'", null);
                if (c1.moveToFirst()) {
                    count1 = c1.getInt(0);
                }
                c1.close();

                Cursor c2 = db.rawQuery("SELECT count(*) FROM Items WHERE category = '" + holder.title.getText().toString() + "' AND status_temp = 'yes'", null);
                if (c2.moveToFirst()) {
                    count2 = c2.getInt(0);
                }
                c2.close();

                if (count1 == count2) {
                    System.out.println("selected id is item " + id + holder.title.getText().toString());
                    holder.cb.setChecked(true);
                } else {
                    holder.cb.setChecked(false);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (k == 1) {
            Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE category = '" + holder.title.getText().toString() + "' GROUP BY category", null);
            if (cursor1.moveToFirst()) {
                do {
                    String id = cursor1.getString(0);
                    String st_temp = cursor1.getString(36);
                    String st_perm = cursor1.getString(37);

                    int count1 = 0, count2 = 0;
                    Cursor c1 = db.rawQuery("SELECT count(*) FROM Items WHERE category = '" + holder.title.getText().toString() + "'", null);
                    if (c1.moveToFirst()) {
                        count1 = c1.getInt(0);
                    }
                    c1.close();

                    Cursor c2 = db.rawQuery("SELECT count(*) FROM Items WHERE category = '" + holder.title.getText().toString() + "' AND status_temp = 'yes'", null);
                    if (c2.moveToFirst()) {
                        count2 = c2.getInt(0);
                    }
                    c2.close();

                    if (count1 == count2) {
                        System.out.println("selected id is item " + id + holder.title.getText().toString());
                        holder.cb.setChecked(true);
                    } else {
                        holder.cb.setChecked(false);
                    }
                } while (cursor1.moveToNext());
            }
            cursor1.close();

        }

        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                j++;
                i++;
                k++;

                groupItem.isChecked = holder.cb.isChecked();

                if (holder.cb.isChecked()) {
//                    Toast.makeText(parent.getContext(), "checked parent", Toast.LENGTH_LONG).show();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("status_temp", "yes");
                    String where = " category ='" + holder.title.getText().toString() + "'";
                    db.update("Items", contentValues, where, new String[]{});

                    webservicequery("UPDATE Items set status_temp = 'yes' WHERe BINARY category = '"+holder.title.getText().toString()+"'");

                    String where1_v1 = "category ='" + holder.title.getText().toString() + "'";
                    db.update("Items_Virtual", contentValues, where1_v1, new String[]{});

                    //commenting
                    //        Toast.makeText(parent.getContext(), "checked " + holder.title.getText().toString(), Toast.LENGTH_LONG).show();


                    ArrayList<Item> childItem = getChild(groupItem);

                    int childIndex = childItem.indexOf(groupItem);

                    boolean isAllChildClicked = true;
                    for (int i = 0; i < childItem.size(); i++) {
                        if (i != childIndex) {
                            Item siblings = childItem.get(i);
                            if (!siblings.isChecked) {
                                isAllChildClicked = false;
                                //if(DataHolder.checkedChilds.containsKey(child.name)==false){
                                // }
                                break;
                            }
                        }
                    }

                    for (Item children : childItem) {
                        children.isChecked = holder.cb.isChecked();
                        //TODO: Here update the list
                    }
                    //All the children are checked
                    if (isAllChildClicked) {
                        Log.i("All should be checked", "Each child is Clicked2!!");
                        groupItem.isChecked = true;
                        checkAll = false;
                    }

                } else {
//                    Toast.makeText(parent.getContext(), "unchecked parent", Toast.LENGTH_LONG).show();
                    ContentValues contentValues11 = new ContentValues();
                    contentValues11.put("status_temp", "");
                    contentValues11.put("status_perm", "");
                    String where = " category ='" + holder.title.getText().toString() + "'";
                    db.update("Items", contentValues11, where, new String[]{});

                    webservicequery("UPDATE Items set status_temp = '' AND status_perm = '' WHERe BINARY category = '"+holder.title.getText().toString()+"'");

                    String where1_v1 = "category ='" + holder.title.getText().toString() + "'";
                    db.update("Items_Virtual", contentValues11, where1_v1, new String[]{});


                }


                if (checkAll) {

                    editor.putBoolean("CheckBoxState" + holder.cb, true);

                    ArrayList<Item> childItem = getChild(groupItem);

                    for (Item children : childItem) {
                        children.isChecked = holder.cb.isChecked();
                        //TODO: Here update the list
                    }


//                    Toast.makeText(parent.getContext(), "parent " + holder.title.getText().toString(), Toast.LENGTH_LONG).show();


                }


                groupItem.isChecked = holder.cb.isChecked();

                notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {

                    public void run() {
                        // TODO Auto-generated method stub
                        if (!checkAll)
                            checkAll = true;
                    }
                }, 50);

                Cursor cursor1 = db.rawQuery("SELECT COUNT(status_temp) FROM Items WHERE status_temp = 'yes'", null);
                if (cursor1.moveToFirst()){
                    int l = cursor1.getInt(0);
                    co = String.valueOf(l);
                }
                cursor1.close();

//                Toast.makeText(parent.getContext(), "total selected "+co, Toast.LENGTH_LONG).show();
                selection.setText(co+" selected");

            }
        });

        return convertView;
    }

    private boolean checkAll = true;

    private ArrayList<Item> getChild(Item group) {
        return groupList.get(group);
    }

    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

    private class GroupHolder {
        public ImageView imageView;
        public CheckBox cb;
        public TextView title;

    }

    private class ChildHolder {
        public TextView title;
        public TextView check;
        public CheckBox cb;
        public TextView text;
    }

    private void countCheck(boolean isChecked) {

        checkAccumulator += isChecked ? 1 : -1;
    }


    public void webservicequery(final String webserviceQuery){


        SharedPreferences sharedpreferences = getDefaultSharedPreferencesMultiProcess(context);
        final String company = sharedpreferences.getString("companyname", null);
        final String store = sharedpreferences.getString("storename", null);
        final String device = sharedpreferences.getString("devicename", null);
        RequestQueue queue;
        StringRequest sr1;
        // queue = Volley.newRequestQueue(getActivity());

        queue= RequestSingleton.getInstance(context).getInstance();

        sr1 = new StringRequest(
                com.android.volley.Request.Method.POST,
                WebserviceUrl + "webservicequery.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                               /*     params.put("email", email + "");
                                    params.put("password", password + "");*/


//                            final String email = prefs.getString("emailid", "");
//                            final String pwd = prefs.getString("password", "");
                params.put("device", device);
                params.put("store", store);
                params.put("company", company);
                params.put("data", webserviceQuery);
                return params;
            }
        };
    /*    sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        sr1.setRetryPolicy(new DefaultRetryPolicy(0, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr1);
    }

//	private void countunCheck(boolean isChecked) {
//
//		uncheckAccumulator += isChecked ? -1 : 1 ;
//	}
}