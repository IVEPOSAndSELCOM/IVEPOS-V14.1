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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ImageCursorAdapter_User extends SimpleCursorAdapter {

    private Cursor c;
    private Context context;
    private int selectedPosition;
    public SQLiteDatabase db1 = null;

    String player1name1, player2name2;
    //Context mBase;

    public ImageCursorAdapter_User(Context context, int layout, Cursor c, String[] from, int[] to, String player1name, String player2name) {
        super(context, layout, c, from, to);
        this.c = c;
        this.context = context;
        this.player1name1 = player1name;
        this.player2name2 = player2name;
        //this.mBase = context;
    }

    public View getView(int pos, View inView, ViewGroup parent) {
        View v = inView;
        if (inView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.user_layout, parent, false);
        }
        this.c.moveToPosition(pos);

        String category = this.c.getString(this.c.getColumnIndex("counterperson_username"));
        TextView category_name = (TextView) v.findViewById(R.id.user);
        category_name.setText(category);

        TextView amount_name = (TextView) v.findViewById(R.id.amount);
        db1 = context.openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
        Cursor mcursor = db1.rawQuery("SELECT SUM(Price) FROM Expenses_sales WHERE counterperson_username = '"+category_name.getText().toString()+"' AND datetimee_new >= '"+player1name1+"' AND datetimee_new <='"+player2name2+"' GROUP BY counterperson_username", null);
        if (mcursor.moveToFirst()) {
            float icount = mcursor.getFloat(0);
            amount_name.setText(String.valueOf(icount));
        }

        TextView no_of_transactions_name = (TextView) v.findViewById(R.id.no_of_transactions);
//        db1 = context.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        Cursor mcursor1 = db1.rawQuery("SELECT Count(*) FROM Expenses_sales WHERE counterperson_username = '"+category_name.getText().toString()+"' AND datetimee_new >= '"+player1name1+"' AND datetimee_new <='"+player2name2+"' GROUP BY counterperson_username", null);
        if (mcursor1.moveToFirst()) {
            float icount = mcursor1.getFloat(0);
            no_of_transactions_name.setText(String.valueOf(icount));
        }

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

