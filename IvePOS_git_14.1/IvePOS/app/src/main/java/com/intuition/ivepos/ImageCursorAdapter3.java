package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 6/30/2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ImageCursorAdapter3 extends SimpleCursorAdapter {

    private Cursor c;
    private Context context;
    private int selectedPosition;
    public SQLiteDatabase db1 = null;
    //Context mBase;

    public ImageCursorAdapter3(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.c = c;
        this.context = context;
        //this.mBase = context;
    }

    public View getView(int pos, View inView, ViewGroup parent) {
        View v = inView;
        if (inView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.tables_listview, parent, false);
        }
            this.c.moveToPosition(pos);

            int price = c.getInt(c.getColumnIndex("_id"));

            int date = c.getInt(c.getColumnIndex("pDate"));

        final LinearLayout linearLayouthi = (LinearLayout)v.findViewById(R.id.linearLayouthi);
            String firstName = this.c.getString(this.c.getColumnIndex("pName"));
            TextView fname = (TextView) v.findViewById(R.id.name);
            if (firstName != null) {
                if (firstName.length() == 0) {
                    fname.setText(String.valueOf(date));
                } else {
                    fname.setText(firstName);
                }
            }

            if (firstName == null) {
                //if (firstName.length() == 0) {
                    fname.setText(String.valueOf(date));
//                } else {
//                    fname.setText(firstName);
//                }
            }

//        linearLayouthi.setBackgroundResource(R.drawable.table_back_green);
//
//        if (pos==selectedPosition) {
//            linearLayouthi.setBackgroundResource(R.drawable.table_back_green);
//        }
//        else {
//            //linearLayouthi.setBackgroundResource(R.drawable.table_back_gr);
//
//            db1 = context.openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
//            Cursor mcursor = db1.rawQuery("SELECT count(*) FROM Table" + (pos+1) + "", null);
//            if (mcursor.moveToFirst()) {
//                int icount = mcursor.getInt(0);
//                if (icount > 0) {
//                    linearLayouthi.setBackgroundResource(R.drawable.table_back_red);
//                    //iv.setBackgroundColor(R.color.red1);
//                    //linearLayouthi.setBackgroundDrawable(getResources().getDrawable(R.drawable.table_back_green));
//                } else {
//                    linearLayouthi.setBackgroundResource(R.drawable.table_back_gr);
//                    //iv.setBackgroundColor(R.color.green);
//                    //linearLayouthi.setBackgroundDrawable(getResources().getDrawable(R.drawable.table_back_gr));
//                }
//            }
//            mcursor.close();
//
//
//        }

        linearLayouthi.setBackgroundResource(R.drawable.table_back_green);

        if (pos==selectedPosition) {
            linearLayouthi.setBackgroundResource(R.drawable.table_back_green);
        }
        else {
            linearLayouthi.setBackgroundResource(R.drawable.table_back_gr);





        }

        return v;
    }

    //@Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return context.openOrCreateDatabase(name, mode, factory);
    }

    public void setSelected(int position) {
        selectedPosition = position;
    }

}

