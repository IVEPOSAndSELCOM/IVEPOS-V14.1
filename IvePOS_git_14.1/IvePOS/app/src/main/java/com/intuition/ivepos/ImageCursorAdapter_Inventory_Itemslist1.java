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

public class ImageCursorAdapter_Inventory_Itemslist1 extends SimpleCursorAdapter {

    private Cursor c;
    private Context context;
    public SQLiteDatabase db = null;
    String str_country, insert1_cc;

    public ImageCursorAdapter_Inventory_Itemslist1(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.c = c;
        this.context = context;
    }

    public View getView(int pos, View inView, ViewGroup parent) {
        View v = inView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.dialog_inventory_indent_itemslist, null);
        }
        this.c.moveToPosition(pos);
        String firstName = this.c.getString(this.c.getColumnIndex("itemname"));
        String price = c.getString(c.getColumnIndex("qty_add"));

        String quantity = c.getString(c.getColumnIndex("individual_price"));


        TextView fname = (TextView) v.findViewById(R.id.itemname);
        fname.setText(firstName);

        TextView lname = (TextView) v.findViewById(R.id.qty);
        lname.setText(String.valueOf(price));

        TextView title = (TextView) v.findViewById(R.id.indiv_price);
        title.setText(quantity);

        TextView totl_p = (TextView) v.findViewById(R.id.total_price);

        float one = Float.parseFloat(title.getText().toString())*Float.parseFloat(lname.getText().toString());
        totl_p.setText(String.format("%.1f", one));

        db = context.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor cursor_country = db.rawQuery("SELECT * FROM Country_Selection", null);
        if (cursor_country.moveToFirst()){
            str_country = cursor_country.getString(1);
        }

        TextView inn = (TextView) v.findViewById(R.id.inn);
        TextView inn1 = (TextView) v.findViewById(R.id.inn1);

        if (str_country.toString().equals("Rupee") || str_country.toString().equals("India")) {
            System.out.println("Rohith India");
            insert1_cc = "\u20B9";
            inn.setText(insert1_cc);
            inn1.setText(insert1_cc);
        }else {
            if (str_country.toString().equals("Pound") || str_country.toString().equals("UK Pound")) {
                insert1_cc = "\u00a3";
                inn.setText(insert1_cc);
                inn1.setText(insert1_cc);
            }else {
                if (str_country.toString().equals("Euro") || str_country.toString().equals("UK Euro")) {
                    insert1_cc = "\u20ac";
                    inn.setText(insert1_cc);
                    inn1.setText(insert1_cc);
                }else {
                    if (str_country.toString().equals("Dollar")) {
                        insert1_cc = "\u0024";
                        inn.setText(insert1_cc);
                        inn1.setText(insert1_cc);
                    }else {
                        if (str_country.toString().equals("Dinars")) {
                            insert1_cc = "D";
                            inn.setText(insert1_cc);
                            inn1.setText(insert1_cc);
                        }else {
                            if (str_country.toString().equals("Shilling")) {
                                insert1_cc = "S";
                                inn.setText(insert1_cc);
                                inn1.setText(insert1_cc);
                            }else {
                                if (str_country.toString().equals("Ringitt")) {
                                    insert1_cc = "R";
                                    inn.setText(insert1_cc);
                                    inn1.setText(insert1_cc);
                                }else {
                                    if (str_country.toString().equals("Rial")) {
                                        insert1_cc = "R";
                                        inn.setText(insert1_cc);
                                        inn1.setText(insert1_cc);
                                    }else {
                                        if (str_country.toString().equals("Yen")) {
                                            insert1_cc = "\u00a5";
                                            inn.setText(insert1_cc);
                                            inn1.setText(insert1_cc);
                                        }else {
                                            if (str_country.toString().equals("Papua New Guinean")) {
                                                insert1_cc = "K";
                                                inn.setText(insert1_cc);
                                                inn1.setText(insert1_cc);
                                            }else {
                                                if (str_country.toString().equals("UAE")) {
                                                    insert1_cc = "D";
                                                    inn.setText(insert1_cc);
                                                    inn1.setText(insert1_cc);
                                                }else {
                                                    if (str_country.toString().equals("South African Rand")) {
                                                        insert1_cc = "R";
                                                        inn.setText(insert1_cc);
                                                        inn1.setText(insert1_cc);
                                                    }else {
                                                        if (str_country.toString().equals("Congolese Franc")) {
                                                            insert1_cc = "F";
                                                            inn.setText(insert1_cc);
                                                            inn1.setText(insert1_cc);
                                                        }else {
                                                            if (str_country.toString().equals("Qatari Riyals")) {
                                                                insert1_cc = "QAR";
                                                                inn.setText(insert1_cc);
                                                                inn1.setText(insert1_cc);
                                                            }else {
                                                                if (str_country.toString().equals("Dirhams")) {
                                                                    insert1_cc = "AED";
                                                                    inn.setText(insert1_cc);
                                                                    inn1.setText(insert1_cc);
                                                                }else {
                                                                    if (str_country.toString().equals("Kuwait Dinar")) {
                                                                        insert1_cc = "KWD";
                                                                        inn.setText(insert1_cc);
                                                                        inn1.setText(insert1_cc);
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