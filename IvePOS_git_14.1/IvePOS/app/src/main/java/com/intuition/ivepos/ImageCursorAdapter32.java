package com.intuition.ivepos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class ImageCursorAdapter32 extends SimpleCursorAdapter {

    private Cursor c;
    private Context context;
    private int selectedPosition;
    public SQLiteDatabase db1 = null;
    public SQLiteDatabase db = null;
    //Context mBase;

    public ImageCursorAdapter32(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.c = c;
        this.context = context;
        //this.mBase = context;
    }

    public View getView(int pos, View inView, ViewGroup parent) {
        View v = inView;
        if (inView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.table_setup_listview, parent, false);
        }
        this.c.moveToPosition(pos);

        int price = c.getInt(c.getColumnIndex("_id"));

        int date = c.getInt(c.getColumnIndex("pDate"));

        String firstName = this.c.getString(this.c.getColumnIndex("pName"));
        TextView tname = (TextView) v.findViewById(R.id.table_name);
        if (firstName != null) {
            if (firstName.length() == 0) {
                tname.setText(String.valueOf(date));
            } else {
                tname.setText(firstName);
            }
        }

        String max = this.c.getString(this.c.getColumnIndex("max"));
        TextView pax_limit = (TextView) v.findViewById(R.id.pax_limit);
        pax_limit.setText(max);
//        ImageView imageView = (ImageView) v.findViewById(R.id.table);
//
//        if (max.toString().equals("2")) {
//            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_table2));
//        }else if (max.toString().equals("4")) {
//            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_table4));
//        } else if (max.toString().equals("6")) {
//            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_table6));
//        }
//
//        TextView max_pax = (TextView) v.findViewById(R.id.max_pax);
//        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//        Cursor cursor = db.rawQuery("SELECT * FROM asd1 WHERE _id = '"+price+"'", null);
//        if (cursor.moveToFirst()) {
//            do {
//                String pax_max1 = cursor.getString(6);
//                max_pax.setText(pax_max1);
//            }while (cursor.moveToNext());
//        }

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

//        linearLayouthi.setBackgroundResource(R.drawable.table_back_green);
//
//        if (pos==selectedPosition) {
//            linearLayouthi.setBackgroundResource(R.drawable.table_back_green);
//        }
//        else {
//            linearLayouthi.setBackgroundResource(R.drawable.table_back_gr);
//
//
//
//
//
//        }

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
