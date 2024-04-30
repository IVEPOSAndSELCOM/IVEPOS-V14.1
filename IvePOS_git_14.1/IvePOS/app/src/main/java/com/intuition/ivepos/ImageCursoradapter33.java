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
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ImageCursoradapter33 extends SimpleCursorAdapter {

    private Cursor c;
    private Context context;
    private int selectedPosition;
    public SQLiteDatabase db1 = null;
    //Context mBase;

    public ImageCursoradapter33(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.c = c;
        this.context = context;
        //this.mBase = context;
    }

    public View getView(int pos, View inView, ViewGroup parent) {
        View v = inView;
        if (inView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_listview, parent, false);
        }
        this.c.moveToPosition(pos);


        String date = c.getString(c.getColumnIndex("name"));

//        TextView iv = (TextView) v.findViewById(R.id.imageView13);

        final RelativeLayout linearLayouthi = (RelativeLayout)v.findViewById(R.id.linearLayouthi);
        TextView fname = (TextView) v.findViewById(R.id.name);

            fname.setText(date);
//                } else {
//                    fname.setText(firstName);
//                }


        linearLayouthi.setBackgroundResource(R.drawable.table_back_gr);

        if (pos==selectedPosition) {
            linearLayouthi.setBackgroundResource(R.drawable.table_back_gr);
        }
        else {
            linearLayouthi.setBackgroundResource(R.drawable.category_back_white);





        }





        return v;
    }



    public void setSelected(int position) {
        selectedPosition = position;
    }

}

