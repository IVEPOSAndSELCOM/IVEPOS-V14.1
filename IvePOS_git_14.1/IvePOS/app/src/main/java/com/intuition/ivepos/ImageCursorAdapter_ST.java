package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 5/23/2015.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ImageCursorAdapter_ST extends SimpleCursorAdapter {

    private Cursor c;
    private Context context;
    public SQLiteDatabase db = null;

    public ImageCursorAdapter_ST(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.c = c;
        this.context = context;
    }

    public View getView(int pos, View inView, ViewGroup parent) {
        View v = inView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.stock_transfer_items_history_listview, null);
        }
        this.c.moveToPosition(pos);
        String from_date_full = this.c.getString(this.c.getColumnIndex("datetimee_new_from"));
        String from_date = this.c.getString(this.c.getColumnIndex("from_date"));
        String from_time = this.c.getString(this.c.getColumnIndex("from_time"));
        String to_store = this.c.getString(this.c.getColumnIndex("to_store"));
        String to_device = this.c.getString(this.c.getColumnIndex("to_device"));
        String itemname = this.c.getString(this.c.getColumnIndex("itemname"));
        String qty_add = this.c.getString(this.c.getColumnIndex("qty_add"));

        TextView date = (TextView) v.findViewById(R.id.date);
        date.setText(from_date);

        TextView time = (TextView) v.findViewById(R.id.time);
        time.setText(from_time);

        TextView to_sto = (TextView) v.findViewById(R.id.to_store);
        to_sto.setText(to_store);

        TextView to_dev = (TextView) v.findViewById(R.id.to_device);
        to_dev.setText(to_device);

//        ListView listView = (ListView) v.findViewById(R.id.list);

        db = context.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        TableLayout tableLayout = (TableLayout) v.findViewById(R.id.lytpedido);
        tableLayout.removeAllViews();


        Cursor cursor = db.rawQuery("SELECT * FROM Stock_transfer_item_details WHERE datetimee_new_from = '"+from_date_full+"'", null);
        if (cursor.moveToFirst()){
            do {
                String text = cursor.getString(1);
                String add_qty = cursor.getString(2);

                final TableRow row = new TableRow(context);
                row.setLayoutParams(new TableLayout.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 4.04f));

                final TextView tv = new TextView(context);
                tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 4.04f));
                tv.setGravity(Gravity.CENTER_VERTICAL);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tv.setTextColor(Color.parseColor("#000000"));

                tv.setText(add_qty+" * "+text);
                row.addView(tv);

                tableLayout.addView(row);

            }while (cursor.moveToNext());
        }






//        Cursor cursor = db.rawQuery("SELECT * FROM Stock_transfer_item_details WHERE datetimee_new_from = '"+from_date_full+"'", null);
//        String[] fromFieldNames = {"itemname"};
//        int[] toViewsID = {R.id.itemname};
//        SimpleCursorAdapter ddataAdapterr = new SimpleCursorAdapter(context, R.layout.stock_transfer_items_history_listview2, cursor, fromFieldNames, toViewsID, 0);
//        listView.setAdapter(ddataAdapterr);


        return (v);
    }
}