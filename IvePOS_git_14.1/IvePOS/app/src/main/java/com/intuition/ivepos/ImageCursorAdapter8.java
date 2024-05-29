package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 5/23/2015.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ImageCursorAdapter8 extends SimpleCursorAdapter {

    private Cursor c;
    private Context context;
    public SQLiteDatabase db1 = null;
    String str_country, insert1_cc;

    public ImageCursorAdapter8(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.c = c;
        this.context = context;
    }

    public View getView(int pos, View inView, ViewGroup parent) {
        View v = inView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.userwise_boxes, null);
        }
        this.c.moveToPosition(pos);
        String price = c.getString(c.getColumnIndex("username"));

        float quantity = c.getFloat(c.getColumnIndex("sales"));
        float quantity1 = this.c.getFloat(this.c.getColumnIndex("salespercentage"));
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

        TextView lname = (TextView) v.findViewById(R.id.username);
        lname.setText(String.valueOf(price));

        TextView title = (TextView) v.findViewById(R.id.sales);
        title.setText(String.valueOf(quantity));

        TextView title1 = (TextView) v.findViewById(R.id.salesper);
        title1.setText(String.valueOf(quantity1));

        db1 = context.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor cursor_country = db1.rawQuery("SELECT * FROM Country_Selection", null);
        if (cursor_country.moveToFirst()){
            str_country = cursor_country.getString(1);
        }

        TextView inn = (TextView) v.findViewById(R.id.inn);

        if (str_country.toString().equals("Rupee") || str_country.toString().equals("India")) {
            System.out.println("Rohith India");
            insert1_cc = "\u20B9";
            inn.setText(insert1_cc);
        }else {
            if (str_country.toString().equals("Pound") || str_country.toString().equals("UK Pound")) {
                insert1_cc = "\u00a3";
                inn.setText(insert1_cc);
            }else {
                if (str_country.toString().equals("Euro") || str_country.toString().equals("UK Euro")) {
                    insert1_cc = "\u20ac";
                    inn.setText(insert1_cc);
                }else {
                    if (str_country.toString().equals("Dollar")) {
                        insert1_cc = "\u0024";
                        inn.setText(insert1_cc);
                    }else {
                        if (str_country.toString().equals("Dinars")) {
                            insert1_cc = "D";
                            inn.setText(insert1_cc);
                        }else {
                            if (str_country.toString().equals("Shilling")) {
                                insert1_cc = "S";
                                inn.setText(insert1_cc);
                            }else {
                                if (str_country.toString().equals("Ringitt")) {
                                    insert1_cc = "R";
                                    inn.setText(insert1_cc);
                                }else {
                                    if (str_country.toString().equals("Rial")) {
                                        insert1_cc = "R";
                                        inn.setText(insert1_cc);
                                    }else {
                                        if (str_country.toString().equals("Yen")) {
                                            insert1_cc = "\u00a5";
                                            inn.setText(insert1_cc);
                                        }else {
                                            if (str_country.toString().equals("Papua New Guinean")) {
                                                insert1_cc = "K";
                                                inn.setText(insert1_cc);
                                            }else {
                                                if (str_country.toString().equals("UAE")) {
                                                    insert1_cc = "D";
                                                    inn.setText(insert1_cc);
                                                }else {
                                                    if (str_country.toString().equals("South African Rand")) {
                                                        insert1_cc = "R";
                                                        inn.setText(insert1_cc);
                                                    }else {
                                                        if (str_country.toString().equals("Congolese Franc")) {
                                                            insert1_cc = "F";
                                                            inn.setText(insert1_cc);
                                                        }else {
                                                            if (str_country.toString().equals("Qatari Riyals")) {
                                                                insert1_cc = "QAR";
                                                                inn.setText(insert1_cc);
                                                            }else {
                                                                if (str_country.toString().equals("Dirhams")) {
                                                                    insert1_cc = "AED";
                                                                    inn.setText(insert1_cc);
                                                                }else {
                                                                    if (str_country.toString().equals("Kuwait Dinar")) {
                                                                        insert1_cc = "KWD";
                                                                        inn.setText(insert1_cc);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return (v);
    }
}