package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 5/23/2015.
 */
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ImageCursorAdapter_Indent_tax extends SimpleCursorAdapter {

    private Cursor c;
    private Context context;

    public ImageCursorAdapter_Indent_tax(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.c = c;
        this.context = context;
    }

    public View getView(int pos, View inView, ViewGroup parent) {
        View v = inView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.dialof_listview_taxlist, null);
        }
        this.c.moveToPosition(pos);
        String firstName = this.c.getString(this.c.getColumnIndex("taxname"));
        String price = c.getString(c.getColumnIndex("value"));

        String quantity = c.getString(c.getColumnIndex("checked"));


        TextView fname = (TextView) v.findViewById(R.id.label);
        fname.setText(firstName);

        TextView lname = (TextView) v.findViewById(R.id.value);
        lname.setText(String.valueOf(price));

        CheckBox title = (CheckBox) v.findViewById(R.id.check);

        if (quantity.toString().equals("checked_s")){
            title.setChecked(true);
        }else {
            title.setChecked(false);
        }
        return (v);
    }
}