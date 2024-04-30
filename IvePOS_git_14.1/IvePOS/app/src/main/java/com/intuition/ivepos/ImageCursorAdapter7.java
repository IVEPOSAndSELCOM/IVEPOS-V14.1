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

public class ImageCursorAdapter7 extends SimpleCursorAdapter {

    private Cursor c;
    private Context context;

    public ImageCursorAdapter7(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.c = c;
        this.context = context;
    }

    public View getView(int pos, View inView, ViewGroup parent) {
        View v = inView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.itemwise_boxes, null);
        }
        this.c.moveToPosition(pos);
        String firstName = this.c.getString(this.c.getColumnIndex("modname"));
        String price = c.getString(c.getColumnIndex("modno"));

        float quantity = c.getFloat(c.getColumnIndex("sales"));
        float quantity1 = this.c.getFloat(this.c.getColumnIndex("salespercentage"));
        String quantity2 = this.c.getString(this.c.getColumnIndex("modtotalquan"));
//        ImageView iv = (ImageView) v.findViewById(R.id.imageView13);
//        if (image != null) {
//            // If there is no image in the database "NA" is stored instead of a blob
//            // test if there more than 3 chars "NA" + a terminating char if more than
//            // there is an image otherwise load the default
//            if (image.length > 3) {
//                iv.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
//            } else {
//                iv.setImageResource(R.drawable.icon);
//            }
//        }

        int temp = c.getPosition()+1;

        TextView sno = (TextView)v.findViewById(R.id.sno);
        sno.setText(String.valueOf(temp));

        TextView fname = (TextView) v.findViewById(R.id.num);
        fname.setText(firstName);

        TextView lname = (TextView) v.findViewById(R.id.itemname);
        lname.setText(String.valueOf(price));

        TextView title = (TextView) v.findViewById(R.id.sales);
        title.setText(String.valueOf(quantity));

        TextView title1 = (TextView) v.findViewById(R.id.salesper);
        title1.setText(String.valueOf(quantity1));

        TextView title2 = (TextView) v.findViewById(R.id.quantity);
        title2.setText(String.valueOf(quantity2));

        return (v);
    }
}