package com.intuition.ivepos;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.intuition.ivepos.syncapp.StubProviderApp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Stock_Transfer_Processing extends AppCompatActivity {

    Uri contentUri,resultUri;
    public SQLiteDatabase db = null; String add_qt;
    TextView item_ro, billamount, total_amount;

    TextView editText_from_day_visible, editText_from_day_hide, editText_to_day_visible, editText_to_day_hide;
    TextView editText1, editText11, editText2, editText22;
    String time24_new;

    String onee, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve;
    String onee1, two1, three1, four1, five1, six1, seven1, eight1, nine1, ten1, eleven1, twelve1;

    private int year, year1;
    private int month, month1;
    private int day, day1;


    private int hour;
    private int minute;

    ListView popupSpinner;

    String insert1_cc = "", insert1_rs = "", str_country;

    public static  ArrayList<StoreBean> storeList=new ArrayList<StoreBean>();
    ExpandableListView expandableListView;

    ConfigureCarAdapter expandableListAdapter;
    private int lastExpandedPosition = -1;

    String tekst_device, tekst1_store;


    public static SharedPreferences getDefaultSharedPreferencesMultiProcess(
            Context context) {
        return context.getSharedPreferences(
                context.getPackageName() + "_preferences",
                Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
    }

    String WebserviceUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_transfer_process);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity_Selection.getDefaultSharedPreferencesMultiProcess(Stock_Transfer_Processing.this);
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

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE,null);

        ImageView closetext = (ImageView) findViewById(R.id.closetext);
        closetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        item_ro = (TextView) findViewById(R.id.item_ro);

        Cursor cursor_qw1 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated_st = 'Add'", null);
        int co1 = cursor_qw1.getCount();
        item_ro.setText(String.valueOf(co1));

        ListView items_list = (ListView) findViewById(R.id.items_list);

        final Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated_st = 'Add'", null);
        String[] fromFieldNames = {"itemname", "add_qty_st"};
        int[] toViewsID = {R.id.itemname, R.id.qty};
        BaseAdapter ddataAdapterr = new ImageCursorAdapter_StockTransfer_Itemslist(Stock_Transfer_Processing.this, R.layout.dialog_stock_transfer_itemslist, cursor, fromFieldNames, toViewsID);
        items_list.setAdapter(ddataAdapterr);


        Button quantitycontinue = (Button) findViewById(R.id.quantitycontinue);
        quantitycontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog_vendor = new Dialog(Stock_Transfer_Processing.this, R.style.timepicker_date_dialog);
                dialog_vendor.setContentView(R.layout.dialog_stock_transfer_device_selection);
                dialog_vendor.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                dialog_vendor.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog_vendor.setCanceledOnTouchOutside(false);
                dialog_vendor.show();


                LinearLayout back_activity = (LinearLayout) dialog_vendor.findViewById(R.id.back_activity);
                back_activity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_vendor.dismiss();
                    }
                });


                final ArrayList<String> expandableListTitle = new ArrayList<String>();
                final HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
                expandableListTitle.clear();
                expandableListDetail.clear();
                System.out.println("MainActivity_Signin_OTPbased.storeList.size() "+MainActivity_Signin_OTPbased.storeList.size());

                for(int i=0;i<MainActivity_Signin_OTPbased.storeList.size();i++){
                    String storeName=MainActivity_Signin_OTPbased.storeList.get(i).getStoreName();
                    expandableListTitle.add(storeName);
                    List<String> groupItems = new ArrayList<String>();
                    groupItems.clear();
                    String[] devices=MainActivity_Signin_OTPbased.storeList.get(i).getDevices();
                    for(int j=0;j<devices.length;j++){
                        groupItems.add(devices[j]);
                    }
                    expandableListDetail.put(storeName, groupItems);
                }

                expandableListView = dialog_vendor.findViewById(R.id.expandablelist);

                expandableListAdapter = new ConfigureCarAdapter(Stock_Transfer_Processing.this, expandableListTitle, expandableListDetail);
                expandableListView.setAdapter(expandableListAdapter);

                for (int i = 0; i < expandableListAdapter.getGroupCount(); i++)
                    expandableListView.expandGroup(i);

                expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    @Override
                    public void onGroupExpand(int groupPosition) {
//                if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
//                    expandableListView.collapseGroup(lastExpandedPosition);
//                }
                        lastExpandedPosition = groupPosition;
                    }
                });
//        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
//            @Override
//            public void onGroupCollapse(int groupPosition) {}
//        });
                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        expandableListAdapter.updateWithChildSelected(groupPosition, childPosition);
                        if(groupPosition < expandableListAdapter.getGroupCount() - 1){
                            expandableListView.expandGroup(groupPosition + 1);




                        }else{
//                    expandableListView.collapseGroup(expandableListAdapter.getGroupCount() - 1);
                        }

                        tekst_device = (String) parent.getExpandableListAdapter().getChild(groupPosition,childPosition);
                        tekst1_store = (String) parent.getExpandableListAdapter().getGroup(groupPosition);

                        return true;
                    }
                });

                for (int i = 0; i < expandableListAdapter.getGroupCount(); i++)
                    expandableListView.expandGroup(i);


                Button btnn = (Button) dialog_vendor.findViewById(R.id.btnn);
                btnn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                        final String currentDateandTime1 = sdf2.format(new Date());

                        SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM yyyy");
                        final String currentDateandTime2 = sdf3.format(new Date());


                        editText1 = new TextView(Stock_Transfer_Processing.this);
                        editText1.setText(currentDateandTime1);

                        editText11 = new TextView(Stock_Transfer_Processing.this);
                        editText11.setText(currentDateandTime2);


                        editText2 = new TextView(Stock_Transfer_Processing.this);
                        editText2.setText(currentDateandTime1);

                        editText22 = new TextView(Stock_Transfer_Processing.this);
                        editText22.setText(currentDateandTime2);

                        Date dtt_new = new Date();
                        SimpleDateFormat sdf1t_new = new SimpleDateFormat("yyyyMMddkkmm");

                        editText_from_day_hide = new TextView(Stock_Transfer_Processing.this);
                        editText_from_day_visible = new TextView(Stock_Transfer_Processing.this);

                        Date dt = new Date();
                        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aa");
                        final String time1 = sdf1.format(dt);

                        Date dt1 = new Date();
                        SimpleDateFormat sddff1 = new SimpleDateFormat("kkmm");
                        String timee1 = sddff1.format(dt1);

                        editText_from_day_visible.setText(time1);
                        editText_from_day_hide.setText(timee1);

                        time24_new = editText1.getText().toString() + editText_from_day_hide.getText().toString();

                        if (time24_new.toString().contains(" ")) {
                            time24_new = time24_new.replace(" ", "");
                        }

                        SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(Stock_Transfer_Processing.this);

                        final String store_ori= sharedpreferences.getString("storename", null);
                        final String device_ori= sharedpreferences.getString("devicename", null);
                        final String company= sharedpreferences.getString("companyname", null);

                        final String store= sharedpreferences.getString("storename1", null);
                        final String device= sharedpreferences.getString("devicename1", null);
//                        Toast.makeText(Stock_Transfer_Processing.this, "Store name "+store+" device name "+device, Toast.LENGTH_LONG).show();



                        final Dialog dialog_stock_transfer_save_confirmation = new Dialog(Stock_Transfer_Processing.this, R.style.timepicker_date_dialog);
                        dialog_stock_transfer_save_confirmation.setContentView(R.layout.dialog_stock_transfer_confirmation_save);
                        dialog_stock_transfer_save_confirmation.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        dialog_stock_transfer_save_confirmation.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialog_stock_transfer_save_confirmation.setCanceledOnTouchOutside(false);
                        dialog_stock_transfer_save_confirmation.show();

                        Button cancel = (Button) dialog_stock_transfer_save_confirmation.findViewById(R.id.cancel);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog_stock_transfer_save_confirmation.dismiss();
                            }
                        });

                        ImageView closetext = (ImageView) dialog_stock_transfer_save_confirmation.findViewById(R.id.closetext);
                        closetext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog_stock_transfer_save_confirmation.dismiss();
                            }
                        });

                        Button ok = (Button) dialog_stock_transfer_save_confirmation.findViewById(R.id.ok);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Cursor cursor1 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated_st = 'Add'", null);
                                if (cursor1.moveToFirst()) {
                                    do {
                                        add_qt = "0";
                                        String id = cursor1.getString(0);
                                        String item_name = cursor1.getString(1);
                                        add_qt = cursor1.getString(49);
                                        String cur_qty = cursor1.getString(3);
                                        setstock_transfer(item_name, add_qt, dialog_vendor, dialog_stock_transfer_save_confirmation);

                                    } while (cursor1.moveToNext());
                                }
                                cursor1.close();

//                                db.execSQL("UPDATE Items SET status_qty_updated_st = '' ");
//                                db.execSQL("UPDATE Items SET add_qty_st = '' ");

                                final Dialog dialog_stock_transfer_confirmation = new Dialog(Stock_Transfer_Processing.this, R.style.timepicker_date_dialog);
                                dialog_stock_transfer_confirmation.setContentView(R.layout.dialog_stock_transfer_confirmation);
                                dialog_stock_transfer_confirmation.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog_stock_transfer_confirmation.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                dialog_stock_transfer_confirmation.setCanceledOnTouchOutside(false);
                                dialog_stock_transfer_confirmation.show();

                                Button done = (Button) dialog_stock_transfer_confirmation.findViewById(R.id.done);
                                done.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog_stock_transfer_confirmation.dismiss();

                                        Intent intent = new Intent(Stock_Transfer_Processing.this, Stock_Transfer.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);

                                    }
                                });



                            }
                        });



                    }
                });
            }
        });


        Button clear_all = (Button) findViewById(R.id.clear_all);
        clear_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog_clear_warning = new Dialog(Stock_Transfer_Processing.this, R.style.timepicker_date_dialog);
                dialog_clear_warning.setContentView(R.layout.dialog_inventory_intent_clear_warning);
                dialog_clear_warning.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                dialog_clear_warning.setCanceledOnTouchOutside(false);
                dialog_clear_warning.show();

                ImageView closetext = (ImageView) dialog_clear_warning.findViewById(R.id.closetext);
                closetext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_clear_warning.dismiss();
                    }
                });

                Button cancel = (Button) dialog_clear_warning.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_clear_warning.dismiss();
                    }
                });

                Button ok = (Button) dialog_clear_warning.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Cursor cursor4 = db.rawQuery("SELECT * FROM Items WHERE status_qty_updated_st = 'Add'", null);
                        if (cursor4.moveToFirst()){
                            do {
                                String id = cursor4.getString(0);
                                String add_qt = cursor4.getString(49);
                                String cur_qty = cursor4.getString(3);

                                ContentValues contentValues1 = new ContentValues();
                                contentValues1.put("add_qty_st", "0");
                                contentValues1.put("status_qty_updated_st", "");
                                String where = "_id = '" + id + "'";
                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                                getContentResolver().update(contentUri, contentValues1, where,new String[]{});
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProviderApp.AUTHORITY)
                                        .path("Items")
                                        .appendQueryParameter("operation", "update")
                                        .appendQueryParameter("_id", id)
                                        .build();
                                getContentResolver().notifyChange(resultUri, null);
//                                db.update("Items", contentValues1, where, new String[]{});

//                                String where1_v1 = "docid = '" + id + "' ";
//                                db.update("Items_Virtual", contentValues1, where1_v1, new String[]{});


                            }while (cursor4.moveToNext());
                        }

                        dialog_clear_warning.dismiss();
                        Intent intent = new Intent(Stock_Transfer_Processing.this, Stock_Transfer.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });

    }

    public ArrayList<String> getTableValuesall() {
        ArrayList<String> my_array = new ArrayList<String>();
        try {
            db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
            Cursor allrows = db.rawQuery("SELECT * FROM Vendor_details", null);
            System.out.println("COUNT : " + allrows.getCount());

            if (allrows.moveToFirst()) {
                do {
                    String ID = allrows.getString(0);
                    String NAME = allrows.getString(1);
                    String PLACE = allrows.getString(2);

                    my_array.add(NAME+" - "+PLACE);

                } while (allrows.moveToNext());
            }
            allrows.close();
            //db.close();
        } catch (Exception e) {
            Toast.makeText(Stock_Transfer_Processing.this, "Error encountered.",
                    Toast.LENGTH_LONG);
        }
        return my_array;
    }

    private android.app.TimePickerDialog.OnTimeSetListener timePickerListener_open = new android.app.TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime_open(hour, minute);

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minutes);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);


            String hour1 = "";
            if (hour < 10)
                hour1 = "0" + hour;
            else
                hour1 = String.valueOf(hour);

            String minutes1 = "";
            if (minute < 10)
                minutes1 = "0" + minute;
            else
                minutes1 = String.valueOf(minute);

            editText_from_day_hide.setText(hour1 + "" + minutes1);


        }
    };

    private void updateTime_open(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        editText_from_day_visible.setText(aTime);
    }

    private void setstock_transfer(final String item_name, final String add_qty, final Dialog dialog_vendor, final Dialog dialog_stock_transfer_save_confirmation) {


        SharedPreferences sharedpreferences =  getDefaultSharedPreferencesMultiProcess(Stock_Transfer_Processing.this);
        final String company= sharedpreferences.getString("companyname", null);
        final String store= sharedpreferences.getString("storename1", null);
        final String device= sharedpreferences.getString("devicename1", null);

        final String store_ori= sharedpreferences.getString("storename", null);
        final String device_ori= sharedpreferences.getString("devicename", null);
//        JSONObject params = new JSONObject();
//
//        try {
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                WebserviceUrl+"stock_transfer.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  if(response.equalsIgnoreCase("success")) {
                        dialog_vendor.dismiss();
                        dialog_stock_transfer_save_confirmation.dismiss();
//                            finish();

//                            updatequan();

                        Cursor cursor = db.rawQuery("SELECT * FROM Items WHERE itemname = '"+item_name+"'", null);
                        if (cursor.moveToFirst()){
                            String id = cursor.getString(0);
                            String add_qt = cursor.getString(49);
                            String cur_qty = cursor.getString(3);

                            float q = Float.parseFloat(cur_qty) - Float.parseFloat(add_qt);

                            ContentValues newValues1 = new ContentValues();
                            newValues1.put("stockquan", String.valueOf(q));
                            newValues1.put("status_qty_updated_st", "");
                            newValues1.put("add_qty_st", "");
                            String where = "_id = '"+id+"'";

                            contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Items");
                            getContentResolver().update(contentUri, newValues1,where,new String[]{});
                            resultUri = new Uri.Builder()
                                    .scheme("content")
                                    .authority(StubProviderApp.AUTHORITY)
                                    .path("Items")
                                    .appendQueryParameter("operation", "update")
                                    .appendQueryParameter("_id", id)
                                    .build();
                            getContentResolver().notifyChange(resultUri, null);
                        }
                        cursor.close();

                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("itemname", item_name);
                        contentValues1.put("qty_add", add_qty);
                        contentValues1.put("company", company);
                        contentValues1.put("from_store", store_ori);
                        contentValues1.put("from_device", device_ori);
                        contentValues1.put("to_store", store);
                        contentValues1.put("to_device", device);
                        contentValues1.put("from_time", editText_from_day_visible.getText().toString());
                        contentValues1.put("from_date", editText11.getText().toString());

                        Date dtt = new Date();
                        SimpleDateFormat sdf1t = new SimpleDateFormat("ss");
                        String time24 = sdf1t.format(dtt);

                        contentValues1.put("datetimee_new_from", time24_new+""+time24);
//                                        db.insert("Stock_transfer_item_details", null, contentValues1);

                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "Stock_transfer_item_details");
                        resultUri = getContentResolver().insert(contentUri, contentValues1);
                        getContentResolver().notifyChange(resultUri, null);


                        //    }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                               /*     params.put("email", email + "");
                                    params.put("password", password + "");*/
                params.put("device1",tekst_device);
                params.put("store1",tekst1_store);
                params.put("company",company);
                params.put("item_name",item_name);
                params.put("add_qt",add_qty);

                params.put("from_store", store_ori);
                params.put("from_device", device_ori);
                params.put("from_time", editText_from_day_visible.getText().toString());
                params.put("from_date", editText11.getText().toString());

                Date dtt = new Date();
                SimpleDateFormat sdf1t = new SimpleDateFormat("ss");
                String time24 = sdf1t.format(dtt);

                params.put("datetimee_new_from", time24_new+""+time24);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

    }

    public void updatequan(){

    }

}
