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
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Customer_ListviewAdapter extends SimpleCursorAdapter {

    private Cursor c;
    private Context context;
    public SQLiteDatabase db = null;
    String str_country, insert1_cc;

    public Customer_ListviewAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.c = c;
        this.context = context;
    }

    public View getView(int pos, View inView, ViewGroup parent) {
        View v = inView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.dialog_customer_management_listview, null);
        }
        this.c.moveToPosition(pos);
        String date = this.c.getString(this.c.getColumnIndex("date1"));
        String time = this.c.getString(this.c.getColumnIndex("time1"));
        String billno = this.c.getString(this.c.getColumnIndex("billnumber"));
        String total_sale = this.c.getString(this.c.getColumnIndex("total_amount"));
        String credit = this.c.getString(this.c.getColumnIndex("credit"));
        String refunds = this.c.getString(this.c.getColumnIndex("refunds"));
        String charges = this.c.getString(this.c.getColumnIndex("charges"));
        String deposit = this.c.getString(this.c.getColumnIndex("deposit"));
        String cashout = this.c.getString(this.c.getColumnIndex("cashout"));
        String cashouttype = this.c.getString(this.c.getColumnIndex("cashout_type"));


        RelativeLayout rl_refunds = (RelativeLayout) v.findViewById(R.id.refunds);
        RelativeLayout rl_charges = (RelativeLayout) v.findViewById(R.id.charges1);
        RelativeLayout rl_deposits = (RelativeLayout) v.findViewById(R.id.deposits1);


        TextView credit_minus = (TextView) v.findViewById(R.id.credit_minus);


        TextView date1 = (TextView) v.findViewById(R.id.date1);
        TextView time1 = (TextView) v.findViewById(R.id.time1);
        TextView billno1 = (TextView) v.findViewById(R.id.bill_no);

        TextView total_sale1 = (TextView) v.findViewById(R.id.sale1);
        TextView rs_total_sale1 = (TextView) v.findViewById(R.id.rs_sale1);

        TextView credit1 = (TextView) v.findViewById(R.id.credit1);
        TextView rs_credit1 = (TextView) v.findViewById(R.id.rs_credit1);

        TextView refunds1 = (TextView) v.findViewById(R.id.refunds_val);
        TextView rs_refunds1 = (TextView) v.findViewById(R.id.rs_refunds_val);
        TextView symbol_refunds1 = (TextView) v.findViewById(R.id.ri_text);

        TextView charges1 = (TextView) v.findViewById(R.id.charges_val);
        TextView rs_charges1 = (TextView) v.findViewById(R.id.rs_charges_val);
        TextView symbol_charges1 = (TextView) v.findViewById(R.id.ci_text);

        TextView deposit1 = (TextView) v.findViewById(R.id.deposits_val);
        TextView rs_deposit1 = (TextView) v.findViewById(R.id.rs_deposits_val);
        TextView symbol_deposit1 = (TextView) v.findViewById(R.id.di_text);

        TextView cashout1 = (TextView) v.findViewById(R.id.cashout_val);
        TextView rs_cashout1 = (TextView) v.findViewById(R.id.rs_cashout_val);
        TextView symbol_cashout1 = (TextView) v.findViewById(R.id.m_text);
        TextView product_cashout1 = (TextView) v.findViewById(R.id.m_text);
        TextView money_cashout1 = (TextView) v.findViewById(R.id.m_text);

        date1.setText(date);
        time1.setText(time);


        TextView tv0 = new TextView(context);
        tv0.setText(billno);
        if (tv0.getText().toString().equals("")){
            billno1.setVisibility(View.GONE);
        }else {
            billno1.setVisibility(View.VISIBLE);
            billno1.setText(billno);
        }

        TextView tv = new TextView(context);
        tv.setText(total_sale);
        if (tv.getText().toString().equals("")){
            rs_total_sale1.setVisibility(View.GONE);
            total_sale1.setVisibility(View.GONE);
        }else {
            rs_total_sale1.setVisibility(View.VISIBLE);
            total_sale1.setVisibility(View.VISIBLE);
            total_sale1.setText(total_sale);
        }

        TextView tv1 = new TextView(context);
        tv1.setText(credit);
        if (tv1.getText().toString().equals("")){
            rs_credit1.setVisibility(View.GONE);
            credit_minus.setVisibility(View.GONE);
            credit1.setVisibility(View.GONE);
        }else {
            rs_credit1.setVisibility(View.VISIBLE);
            credit_minus.setVisibility(View.VISIBLE);
            credit1.setVisibility(View.VISIBLE);
            credit1.setText(credit);
        }

        TextView tv2 = new TextView(context);
        tv2.setText(refunds);
        if (tv2.getText().toString().equals("")){
            rs_refunds1.setVisibility(View.GONE);
            symbol_refunds1.setVisibility(View.GONE);
            rl_refunds.setVisibility(View.GONE);
            refunds1.setVisibility(View.GONE);
        }else {
            rs_refunds1.setVisibility(View.VISIBLE);
            symbol_refunds1.setVisibility(View.VISIBLE);
            rl_refunds.setVisibility(View.VISIBLE);
            refunds1.setVisibility(View.VISIBLE);
            refunds1.setText(refunds);
        }

        TextView tv3 = new TextView(context);
        tv3.setText(charges);
        if (tv3.getText().toString().equals("")){
            rs_charges1.setVisibility(View.GONE);
            symbol_charges1.setVisibility(View.GONE);
            rl_charges.setVisibility(View.GONE);
            charges1.setVisibility(View.GONE);
        }else {
            rs_charges1.setVisibility(View.VISIBLE);
            symbol_charges1.setVisibility(View.VISIBLE);
            rl_charges.setVisibility(View.VISIBLE);
            charges1.setVisibility(View.VISIBLE);
            charges1.setText(charges);
        }

        TextView tv4 = new TextView(context);
        tv4.setText(deposit);
        if (tv4.getText().toString().equals("")){
            rs_deposit1.setVisibility(View.GONE);
            symbol_deposit1.setVisibility(View.GONE);
            rl_deposits.setVisibility(View.GONE);
            deposit1.setVisibility(View.GONE);
        }else {
            rs_deposit1.setVisibility(View.VISIBLE);
            symbol_deposit1.setVisibility(View.VISIBLE);
            deposit1.setVisibility(View.VISIBLE);
            rl_deposits.setVisibility(View.VISIBLE);
            deposit1.setText(deposit);
        }

        TextView tv5 = new TextView(context);
        tv5.setText(cashout);
        if (tv5.getText().toString().equals("")){
            rs_cashout1.setVisibility(View.GONE);
            symbol_cashout1.setVisibility(View.GONE);
            product_cashout1.setVisibility(View.GONE);
            cashout1.setVisibility(View.GONE);
        }else {
            TextView tv6 = new TextView(context);
            tv6.setText(cashouttype);
            rs_cashout1.setVisibility(View.VISIBLE);
            cashout1.setVisibility(View.VISIBLE);
            cashout1.setText(cashout);
            if (tv6.getText().toString().equals("Money")){
                symbol_cashout1.setVisibility(View.VISIBLE);
                product_cashout1.setVisibility(View.GONE);
            }else {
                symbol_cashout1.setVisibility(View.GONE);
                product_cashout1.setVisibility(View.VISIBLE);
            }

        }


        db = context.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor cursor_country = db.rawQuery("SELECT * FROM Country_Selection", null);
        if (cursor_country.moveToFirst()){
            str_country = cursor_country.getString(1);
        }
        cursor_country.close();

        if (str_country.toString().equals("Rupee") || str_country.toString().equals("India")) {
            System.out.println("Rohith India");
            insert1_cc = "\u20B9";
            rs_total_sale1.setText(insert1_cc);
            rs_credit1.setText(insert1_cc);
            rs_refunds1.setText(insert1_cc);
            rs_charges1.setText(insert1_cc);
            rs_deposit1.setText(insert1_cc);
            rs_cashout1.setText(insert1_cc);
        }else {
            if (str_country.toString().equals("Pound") || str_country.toString().equals("UK Pound")) {
                insert1_cc = "\u00a3";
                rs_total_sale1.setText(insert1_cc);
                rs_credit1.setText(insert1_cc);
                rs_refunds1.setText(insert1_cc);
                rs_charges1.setText(insert1_cc);
                rs_deposit1.setText(insert1_cc);
                rs_cashout1.setText(insert1_cc);
            }else {
                if (str_country.toString().equals("Euro") || str_country.toString().equals("UK Euro")) {
                    insert1_cc = "\u20ac";
                    rs_total_sale1.setText(insert1_cc);
                    rs_credit1.setText(insert1_cc);
                    rs_refunds1.setText(insert1_cc);
                    rs_charges1.setText(insert1_cc);
                    rs_deposit1.setText(insert1_cc);
                    rs_cashout1.setText(insert1_cc);
                }else {
                    if (str_country.toString().equals("Dollar")) {
                        insert1_cc = "\u0024";
                        rs_total_sale1.setText(insert1_cc);
                        rs_credit1.setText(insert1_cc);
                        rs_refunds1.setText(insert1_cc);
                        rs_charges1.setText(insert1_cc);
                        rs_deposit1.setText(insert1_cc);
                        rs_cashout1.setText(insert1_cc);
                    }else {
                        if (str_country.toString().equals("Dinars")) {
                            insert1_cc = "D";
                            rs_total_sale1.setText(insert1_cc);
                            rs_credit1.setText(insert1_cc);
                            rs_refunds1.setText(insert1_cc);
                            rs_charges1.setText(insert1_cc);
                            rs_deposit1.setText(insert1_cc);
                            rs_cashout1.setText(insert1_cc);
                        }else {
                            if (str_country.toString().equals("Shilling")) {
                                insert1_cc = "S";
                                rs_total_sale1.setText(insert1_cc);
                                rs_credit1.setText(insert1_cc);
                                rs_refunds1.setText(insert1_cc);
                                rs_charges1.setText(insert1_cc);
                                rs_deposit1.setText(insert1_cc);
                                rs_cashout1.setText(insert1_cc);
                            }else {
                                if (str_country.toString().equals("Ringitt")) {
                                    insert1_cc = "R";
                                    rs_total_sale1.setText(insert1_cc);
                                    rs_credit1.setText(insert1_cc);
                                    rs_refunds1.setText(insert1_cc);
                                    rs_charges1.setText(insert1_cc);
                                    rs_deposit1.setText(insert1_cc);
                                    rs_cashout1.setText(insert1_cc);
                                }else {
                                    if (str_country.toString().equals("Rial")) {
                                        insert1_cc = "R";
                                        rs_total_sale1.setText(insert1_cc);
                                        rs_credit1.setText(insert1_cc);
                                        rs_refunds1.setText(insert1_cc);
                                        rs_charges1.setText(insert1_cc);
                                        rs_deposit1.setText(insert1_cc);
                                        rs_cashout1.setText(insert1_cc);
                                    }else {
                                        if (str_country.toString().equals("Yen")) {
                                            insert1_cc = "\u00a5";
                                            rs_total_sale1.setText(insert1_cc);
                                            rs_credit1.setText(insert1_cc);
                                            rs_refunds1.setText(insert1_cc);
                                            rs_charges1.setText(insert1_cc);
                                            rs_deposit1.setText(insert1_cc);
                                            rs_cashout1.setText(insert1_cc);
                                        }else {
                                            if (str_country.toString().equals("Papua New Guinean")) {
                                                insert1_cc = "K";
                                                rs_total_sale1.setText(insert1_cc);
                                                rs_credit1.setText(insert1_cc);
                                                rs_refunds1.setText(insert1_cc);
                                                rs_charges1.setText(insert1_cc);
                                                rs_deposit1.setText(insert1_cc);
                                                rs_cashout1.setText(insert1_cc);
                                            }else {
                                                if (str_country.toString().equals("UAE")) {
                                                    insert1_cc = "D";
                                                    rs_total_sale1.setText(insert1_cc);
                                                    rs_credit1.setText(insert1_cc);
                                                    rs_refunds1.setText(insert1_cc);
                                                    rs_charges1.setText(insert1_cc);
                                                    rs_deposit1.setText(insert1_cc);
                                                    rs_cashout1.setText(insert1_cc);
                                                }else {
                                                    if (str_country.toString().equals("South African Rand")) {
                                                        insert1_cc = "R";
                                                        rs_total_sale1.setText(insert1_cc);
                                                        rs_credit1.setText(insert1_cc);
                                                        rs_refunds1.setText(insert1_cc);
                                                        rs_charges1.setText(insert1_cc);
                                                        rs_deposit1.setText(insert1_cc);
                                                        rs_cashout1.setText(insert1_cc);
                                                    }else {
                                                        if (str_country.toString().equals("Congolese Franc")) {
                                                            insert1_cc = "F";
                                                            rs_total_sale1.setText(insert1_cc);
                                                            rs_credit1.setText(insert1_cc);
                                                            rs_refunds1.setText(insert1_cc);
                                                            rs_charges1.setText(insert1_cc);
                                                            rs_deposit1.setText(insert1_cc);
                                                            rs_cashout1.setText(insert1_cc);
                                                        }else {
                                                            if (str_country.toString().equals("Qatari Riyals")) {
                                                                insert1_cc = "QAR";
                                                                rs_total_sale1.setText(insert1_cc);
                                                                rs_credit1.setText(insert1_cc);
                                                                rs_refunds1.setText(insert1_cc);
                                                                rs_charges1.setText(insert1_cc);
                                                                rs_deposit1.setText(insert1_cc);
                                                                rs_cashout1.setText(insert1_cc);
                                                            }else {
                                                                if (str_country.toString().equals("Dirhams")) {
                                                                    insert1_cc = "AED";
                                                                    rs_total_sale1.setText(insert1_cc);
                                                                    rs_credit1.setText(insert1_cc);
                                                                    rs_refunds1.setText(insert1_cc);
                                                                    rs_charges1.setText(insert1_cc);
                                                                    rs_deposit1.setText(insert1_cc);
                                                                    rs_cashout1.setText(insert1_cc);
                                                                }else {
                                                                    if (str_country.toString().equals("Kuwait Dinar")) {
                                                                        insert1_cc = "KWD";
                                                                        rs_total_sale1.setText(insert1_cc);
                                                                        rs_credit1.setText(insert1_cc);
                                                                        rs_refunds1.setText(insert1_cc);
                                                                        rs_charges1.setText(insert1_cc);
                                                                        rs_deposit1.setText(insert1_cc);
                                                                        rs_cashout1.setText(insert1_cc);
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



//        fname.setText(firstName);
//
//        TextView lname = (TextView) v.findViewById(R.id.price);
//        lname.setText(String.valueOf(price));
//
//        TextView title = (TextView) v.findViewById(R.id.inventory);
//        title.setText(String.format("%.2f", quantity));
//        title.setVisibility(View.VISIBLE);
        return (v);
    }
}