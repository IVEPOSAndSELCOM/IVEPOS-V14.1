package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 5/23/2015.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ImageCursorAdapter1 extends SimpleCursorAdapter {

    private Cursor c;
    private Context context;
    public SQLiteDatabase db = null;
    String str_country, insert1_cc;

    public ImageCursorAdapter1(Context context, int layout, Cursor c, String[] from, int[] to, String insert1_cc) {
        super(context, layout, c, from, to);
        this.c = c;
        this.context = context;
        this.insert1_cc = insert1_cc;
    }

    public View getView(int pos, View inView, ViewGroup parent) {
        View v = inView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.categoryitems_gridview, null);
        }
        this.c.moveToPosition(pos);
        String firstName = this.c.getString(this.c.getColumnIndex("itemname"));
        int price = c.getInt(c.getColumnIndex("price"));
        String item_content2 = this.c.getString(this.c.getColumnIndex("price"));

        byte[] image = this.c.getBlob(this.c.getColumnIndex("image"));
        ImageView iv = (ImageView) v.findViewById(R.id.imageView13);

        String imagetext = c.getString(c.getColumnIndex("image_text"));
        TextView imagetext1 = (TextView) v.findViewById(R.id.imagetext);
        imagetext1.setText(imagetext);

        Bitmap bmPicture = BitmapFactory.decodeByteArray(image, 0, image.length);
        if (bmPicture == null){
//                                    Toast.makeText(getActivity(), "image not there", Toast.LENGTH_SHORT).show();
            imagetext1.setText(imagetext);
            imagetext1.setVisibility(View.VISIBLE);
            iv.setVisibility(View.INVISIBLE);
        }else {
//                                    Toast.makeText(getActivity(), "image there", Toast.LENGTH_SHORT).show();
            // If there is no image in the database "NA" is stored instead of a blob
            // test if there more than 3 chars "NA" + a terminating char if more than
            // there is an image otherwise load the default
            imagetext1.setVisibility(View.INVISIBLE);
            iv.setVisibility(View.VISIBLE);
            if (image.length > 3) {
                iv.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
            } else {
                iv.setImageResource(R.drawable.icon);
            }
        }

        String taxx_v = c.getString(c.getColumnIndex("tax_value"));
        String taxx_v2 = c.getString(c.getColumnIndex("tax_value2"));
        String taxx_v3 = c.getString(c.getColumnIndex("tax_value3"));
        String taxx_v4 = c.getString(c.getColumnIndex("tax_value4"));
        String taxx_v5 = c.getString(c.getColumnIndex("tax_value5"));

        float tax1_g = 0, tax2_g = 0, tax3_g = 0, tax4_g = 0, tax5_g = 0;
        TextView ta_val1 = new TextView(context);
        ta_val1.setText(taxx_v);
        if (ta_val1.getText().toString().equals("0") || ta_val1.getText().toString().equals("") || ta_val1.getText().toString().equals("null")) {
            tax1_g = 0;
        }else {
            tax1_g = Float.parseFloat(taxx_v);
        }
        TextView ta_val2 = new TextView(context);
        ta_val2.setText(taxx_v2);
        if (ta_val2.getText().toString().equals("0") || ta_val2.getText().toString().equals("") || ta_val2.getText().toString().equals("null")) {
            tax2_g = 0;
        }else {
            tax2_g = Float.parseFloat(taxx_v2);
        }
        TextView ta_val3 = new TextView(context);
        ta_val3.setText(taxx_v3);
        if (ta_val3.getText().toString().equals("0") || ta_val3.getText().toString().equals("") || ta_val3.getText().toString().equals("null")) {
            tax3_g = 0;
        }else {
            tax3_g = Float.parseFloat(taxx_v3);
        }
        TextView ta_val4 = new TextView(context);
        ta_val4.setText(taxx_v4);
        if (ta_val4.getText().toString().equals("0") || ta_val4.getText().toString().equals("") || ta_val4.getText().toString().equals("null")) {
            tax4_g = 0;
        }else {
            tax4_g = Float.parseFloat(taxx_v4);
        }
        TextView ta_val5 = new TextView(context);
        ta_val5.setText(taxx_v5);
        if (ta_val5.getText().toString().equals("0") || ta_val5.getText().toString().equals("") || ta_val5.getText().toString().equals("null")) {
            tax5_g = 0;
        }else {
            tax5_g = Float.parseFloat(taxx_v5);
        }

        float pri1 = tax1_g+tax2_g+tax3_g+tax4_g+tax5_g;
        float pri = Float.parseFloat(item_content2)+((Float.parseFloat(item_content2)/100)*(pri1));

        TextView lname2 = (TextView) v.findViewById(R.id.price2);
        lname2.setText(String.valueOf(pri));

        System.out.println(firstName+" "+pri);

//        if (imagetext1.getText().toString().equals("")){
//            imagetext1.setVisibility(View.INVISIBLE);
//            iv.setVisibility(View.VISIBLE);
//            if (image != null) {
//                // If there is no image in the database "NA" is stored instead of a blob
//                // test if there more than 3 chars "NA" + a terminating char if more than
//                // there is an image otherwise load the default
//                if (image.length > 3) {
//                    iv.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
//                } else {
//                    iv.setImageResource(R.drawable.icon);
//                }
//            }
//        }else {
//            imagetext1.setText(imagetext);
//            imagetext1.setVisibility(View.VISIBLE);
//            iv.setVisibility(View.INVISIBLE);
//        }

        TextView fname = (TextView) v.findViewById(R.id.name);
        fname.setText(firstName);

        TextView lname = (TextView) v.findViewById(R.id.price);
        lname.setText(String.valueOf(price));

//        db = context.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//        Cursor cursor_country = db.rawQuery("SELECT * FROM Country_Selection", null);
//        if (cursor_country.moveToFirst()){
//            str_country = cursor_country.getString(1);
//        }

        TextView inn = (TextView) v.findViewById(R.id.inn);
        inn.setText(insert1_cc);

        TextView inn2 = (TextView) v.findViewById(R.id.inn2);
        inn2.setText(insert1_cc);
//        if (str_country.toString().equals("Rupee") || str_country.toString().equals("India")) {
//            System.out.println("Rohith India");
//            insert1_cc = "\u20B9";
//            inn.setText(insert1_cc);
//        }else {
//            if (str_country.toString().equals("Pound") || str_country.toString().equals("UK Pound")) {
//                insert1_cc = "\u00a3";
//                inn.setText(insert1_cc);
//            }else {
//                if (str_country.toString().equals("Euro") || str_country.toString().equals("UK Euro")) {
//                    insert1_cc = "\u20ac";
//                    inn.setText(insert1_cc);
//                }else {
//                    if (str_country.toString().equals("Dollar")) {
//                        insert1_cc = "\u0024";
//                        inn.setText(insert1_cc);
//                    }else {
//                        if (str_country.toString().equals("Taiwan")) {
//                            insert1_cc = "\u0024";
//                            inn.setText(insert1_cc);
//                        }else {
//                            if (str_country.toString().equals("Newzealand")) {
//                                insert1_cc = "\u0024";
//                                inn.setText(insert1_cc);
//                            }else {
//                                if (str_country.toString().equals("Zimbabwe")) {
//                                    insert1_cc = "\u0024";
//                                    inn.setText(insert1_cc);
//                                }else {
//                                    if (str_country.toString().equals("Jamaica")) {
//                                        insert1_cc = "\u0024";
//                                        inn.setText(insert1_cc);
//                                    }else {
//                                        if (str_country.toString().equals("US")) {
//                                            insert1_cc = "\u0024";
//                                            inn.setText(insert1_cc);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }

        return (v);
    }
}