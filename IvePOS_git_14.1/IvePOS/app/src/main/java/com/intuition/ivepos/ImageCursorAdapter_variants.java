package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 5/23/2015.
 */
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ImageCursorAdapter_variants extends SimpleCursorAdapter {

    private Cursor c;
    private Context context;

    public ImageCursorAdapter_variants(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.c = c;
        this.context = context;
    }

    public View getView(int pos, View inView, ViewGroup parent) {
        View v = inView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.variants_listview, null);
        }
        this.c.moveToPosition(pos);
        String var_1 = this.c.getString(this.c.getColumnIndex("vari1"));


        String var_p1 = this.c.getString(this.c.getColumnIndex("varprice1"));


        TextView tv_var1 = new TextView(context);
        tv_var1.setText(var_1);

        TextView fname = (TextView) v.findViewById(R.id.var_name);
        TextView fprice = (TextView) v.findViewById(R.id.var_price);

        if (tv_var1.getText().toString().equals("")){

        }else {
            fname.setText(var_1);
            fprice.setText(var_p1);
        }
        return (v);
    }
}