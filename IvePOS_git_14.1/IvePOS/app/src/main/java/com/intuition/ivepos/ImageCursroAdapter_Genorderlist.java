package com.intuition.ivepos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ImageCursroAdapter_Genorderlist extends SimpleCursorAdapter {

    private Cursor c;
    private Context context;
    public SQLiteDatabase db = null;
    String str_country, insert1_cc;
    String editText1_filter, editText2_filter;
    String bill_coun, total1;

    public ImageCursroAdapter_Genorderlist(Context context, int layout, Cursor c, String[] from, int[] to, String insert1_cc, String editText1_filter, String editText2_filter) {
        super(context, layout, c, from, to);
        this.c = c;
        this.context = context;
        this.insert1_cc = insert1_cc;
        this.editText1_filter = editText1_filter;
        this.editText2_filter = editText2_filter;
    }

    public View getView(int pos, View inView, ViewGroup parent) {
        View v = inView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.general_wise, null);
        }
        this.c.moveToPosition(pos);

//        String firstName = this.c.getString(this.c.getColumnIndex("itemname"));
        TextView date_get = (TextView) v.findViewById(R.id.date);
        TextView time_get = (TextView) v.findViewById(R.id.time);
        TextView user_get = (TextView) v.findViewById(R.id.user);
        TextView bill_coun_get = (TextView) v.findViewById(R.id.billcount);
        TextView bill_no_get = (TextView) v.findViewById(R.id.billno);
        TextView inn_get = (TextView) v.findViewById(R.id.inn);
        TextView sale_get = (TextView) v.findViewById(R.id.sales);

        System.out.println("date selected is "+editText1_filter+" "+editText2_filter);

        db = context.openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("Select DISTINCT * from All_Sales WHERE datetimee_new >= '" + editText1_filter + "' AND datetimee_new <= '" + editText2_filter + "' GROUP BY bill_no ", null);//replace to cursor = dbHelper.fetchAllHotels();
//        db.execSQL("delete from Generalorderlistascdesc1");
        if (cursor.moveToFirst()) {
//            do {

            String billno = cursor.getString(11);
            System.out.println("bill no is "+billno);

            Cursor modcursor = db.rawQuery("Select * from Billnumber WHERE billnumber = '" + billno + "' AND datetimee_new >= '" + editText1_filter + "' AND datetimee_new <='" + editText2_filter + "' ", null);
            if (modcursor.moveToFirst()) {
                //level = modcursor.getString(2);
                total1 = modcursor.getString(2);
                //final TextView tv2 = new TextView(Cash_Card_Credit_Sales.this);
//                        tv2.setLayoutParams(new TableRow.LayoutParams(150, ViewGroup.LayoutParams.MATCH_PARENT));
//                        //tv.setBackgroundResource(R.drawable.cell_shape);
//                        tv2.setGravity(Gravity.CENTER);
//                        tv2.setTextSize(15);
//                        tv2.setTypeface(null, Typeface.NORMAL);
//                        tv2.setPadding(5, 0, 0, 0);
//                        tv2.setBackgroundResource(R.drawable.cell_shape);
//                        //text = cursor.getString(1);
//                        tv2.setText(total1);
                //row.addView(tv2);

            }
            modcursor.close();

            Cursor dis = db.rawQuery("Select * from Discountdetails WHERE billno = '" + billno + "' ", null);
            if (dis.moveToFirst()) {
                String discount = dis.getString(7);
            } else {
                String discount = "0";
            }
            dis.close();

            Cursor billtype = db.rawQuery("Select * from Billnumber WHERE billnumber = '" + billno + "' ", null);
            if (billtype.moveToFirst()) {
                String strpaymentmethod = billtype.getString(5);
                String strbilltype = billtype.getString(6);
                bill_coun = billtype.getString(11);
            }
            billtype.close();

            Cursor cardno = db.rawQuery("Select * from Cardnumber WHERE billnumber = '" + billno + "' ", null);
            if (cardno.moveToFirst()) {
                String cardnum = cardno.getString(1);
            } else {
                String cardnum = "";
            }
            cardno.close();


            Cursor itemmnn = db.rawQuery("Select * from All_Sales WHERE bill_no = '" + billno + "' ", null);
            if (itemmnn.moveToFirst()) {

                do {
                    String date = itemmnn.getString(25);
                    String date11 = itemmnn.getString(13);
                    String date111 = itemmnn.getString(26);

                    String time = itemmnn.getString(12);

                    String user = itemmnn.getString(14);

                    String iittnn = itemmnn.getString(1);
                    String iittnnquan = itemmnn.getString(2);
                    String iittnntable = itemmnn.getString(15);
                    String iittnnindprice = itemmnn.getString(3);
                    String iittnnindtotalprice = itemmnn.getString(4);
                    String iittnninddate = itemmnn.getString(33);
                    String add_sec = itemmnn.getString(12);

                    if (iittnninddate.contains(":")) {
                        iittnninddate = iittnninddate.replace(":", "");
                    }

                    if (add_sec.contains(":")) {
                        add_sec = add_sec.replace(":", "");
                    }

                    add_sec = add_sec.substring(4, 6);

                    date_get.setText(date);
                    time_get.setText(time);
                    user_get.setText(user);
                    bill_coun_get.setText(bill_coun);
                    bill_no_get.setText(billno);
                    inn_get.setText(insert1_cc);
                    sale_get.setText(total1);

//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("date", date);
//                        contentValues.put("time", time);
//                        contentValues.put("user", user);
//                        contentValues.put("billno", billno);
//                        contentValues.put("sales", total1);
//                        contentValues.put("discountamount", discount);
//                        contentValues.put("paymentmethod", strpaymentmethod);
//                        contentValues.put("billtype", strbilltype);
//                        contentValues.put("itemname", iittnn);
//                        contentValues.put("quan", iittnnquan);
//                        contentValues.put("tableid", iittnntable);
//                        contentValues.put("individualprice", iittnnindprice);
//                        contentValues.put("individualtotal", iittnnindtotalprice);
//                        contentValues.put("billcount", bill_coun);
//                        contentValues.put("date1", date11);
//                        contentValues.put("datetimee", iittnninddate + "" + add_sec);
//                        contentValues.put("cardnumber", cardnum);
//                        db.insert("Generalorderlistascdesc1", null, contentValues);
                } while (itemmnn.moveToNext());


            }
            itemmnn.close();
//            } while (cursor.moveToNext());
        }
        cursor.close();

        return (v);
    }

}
