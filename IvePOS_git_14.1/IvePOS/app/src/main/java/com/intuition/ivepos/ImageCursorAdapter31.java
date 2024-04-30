package com.intuition.ivepos;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class ImageCursorAdapter31 extends SimpleCursorAdapter {

    private Cursor c;
    private Context context;
    private int selectedPosition;
    public SQLiteDatabase db1 = null;
    public SQLiteDatabase db = null;
    String table_iddd;
    //Context mBase;

    public ImageCursorAdapter31(Context context, int layout, Cursor c, String[] from, int[] to, String table_iddd) {
        super(context, layout, c, from, to);
        this.c = c;
        this.context = context;
        this.table_iddd = table_iddd;
        //this.mBase = context;
    }

    public View getView(int pos, View inView, ViewGroup parent) {
        View v = inView;
        if (inView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.tables_management_listview, parent, false);
        }
        this.c.moveToPosition(pos);

        db = context.openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        db1 = context.openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);

        int price = c.getInt(c.getColumnIndex("_id"));

        int date = c.getInt(c.getColumnIndex("pDate"));

        final LinearLayout linearLayouthi = (LinearLayout)v.findViewById(R.id.linearLayouthi);
        String firstName = this.c.getString(this.c.getColumnIndex("pName"));
        TextView fname = (TextView) v.findViewById(R.id.name);
        if (firstName != null) {
            if (firstName.length() == 0) {
                fname.setText(String.valueOf(date));
            } else {
                fname.setText(firstName);
            }
        }

        String floorname_name = this.c.getString(this.c.getColumnIndex("floor"));

        TextView floorname = (TextView) v.findViewById(R.id.floorname);
        floorname.setText(floorname_name);
        if (firstName == null) {
            //if (firstName.length() == 0) {
            fname.setText(String.valueOf(date));
//                } else {
//                    fname.setText(firstName);
//                }
        }


        int i_clear = 0;
        Cursor cursor = db1.rawQuery("SELECT * FROM Table" + (price) + "", null);
        if (cursor.moveToFirst()) {
            do {
                String status = cursor.getString(16);
                String pre_qty = cursor.getString(1);
                String upd_qty = cursor.getString(20);

                TextView tv = new TextView(context);
                tv.setText(status);
                TextView tv1 = new TextView(context);
                tv1.setText(pre_qty);
                TextView tv2 = new TextView(context);
                tv2.setText(upd_qty);

                if (tv.getText().toString().equals("print") && ((Float.parseFloat(tv1.getText().toString())) == (Float.parseFloat(tv2.getText().toString())))) {
                    i_clear = 1;
//                            System.out.println("print data");
                }else {
                    i_clear = 0;
//                            System.out.println("empty data");
                    break;
                }

            } while (cursor.moveToNext());
        }

        TextView kot_status = (TextView) v.findViewById(R.id.kot_status);
        if (i_clear == 0){
            kot_status.setText("No");
        }else {
            kot_status.setText("Yes");
        }

        System.out.println("position ImageCursorAdapter "+table_iddd+" "+String.valueOf(price));

        String max = this.c.getString(this.c.getColumnIndex("max"));
        ImageView imageView = (ImageView) v.findViewById(R.id.table);

//        System.out.println("Dynamic fragment1 "+table_iddd+" "+String.valueOf(price));
        if (max.toString().equals("2")) {
            if (table_iddd.toString().equals(String.valueOf(price))) {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_table2_green));
            }else {
                Cursor mcursor = db.rawQuery("SELECT * FROM asd1 WHERE _id = '"+price+"'", null);
                System.out.println("Table id is Table"+(price));
                if (mcursor.moveToFirst()) {
                    String icount = mcursor.getString(7);
                    if (icount != null && icount.length() != 0 && !icount.isEmpty() && Integer.parseInt(icount) != 0) {
                        System.out.println("Table id is there Table"+(price));
                        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_table2_red));
                    } else {
                        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_table2));
                    }
                }else {
                    System.out.println("Table id is no Table"+(price));
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_table2));
                }
                mcursor.close();
            }
        }else if (max.toString().equals("4")) {
            if (table_iddd.toString().equals(String.valueOf(price))) {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_table4_green));
            }else {
//                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_table4));
                Cursor mcursor = db.rawQuery("SELECT * FROM asd1 WHERE _id = '"+price+"'", null);
                System.out.println("Table id is Table"+(price));
                if (mcursor.moveToFirst()) {
                    String icount = mcursor.getString(7);
                    if (icount != null && icount.length() != 0 && !icount.isEmpty() && Integer.parseInt(icount) != 0) {
                        System.out.println("Table id is there Table"+(price));
                        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_table4_red));
                    } else {
                        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_table4));
                    }
                }else {
                    System.out.println("Table id is no Table"+(price));
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_table4));
                }
                mcursor.close();
            }
        } else if (max.toString().equals("6")) {
            if (table_iddd.toString().equals(String.valueOf(price))) {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_table6_green));
            }else {
//                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_table6));
                Cursor mcursor = db.rawQuery("SELECT * FROM asd1 WHERE _id = '"+price+"'", null);
                System.out.println("Table id is Table"+(price));
                if (mcursor.moveToFirst()) {
                    String icount = mcursor.getString(7);
                    if (icount != null && icount.length() != 0 && !icount.isEmpty() && Integer.parseInt(icount) != 0) {
                        System.out.println("Table id is there Table"+(price));
                        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_table6_red));
                    } else {
                        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_table6));
                    }
                }else {
                    System.out.println("Table id is no Table"+(price));
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_table6));
                }
                mcursor.close();
            }
        }

//        Cursor mcursor = db1.rawQuery("SELECT count(*) FROM Table" + (pos+1) + "", null);
//        if (mcursor.moveToFirst()) {
//            int icount = mcursor.getInt(0);
//            if (icount > 0) {
//                linearLayouthi.setBackgroundResource(R.drawable.table_back_red);
//            } else {
//                linearLayouthi.setBackgroundResource(R.drawable.table_back_green);
//            }
//        }
//        mcursor.close();


        TextView max_pax = (TextView) v.findViewById(R.id.max_pax);
//        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//        Cursor cursor = db.rawQuery("SELECT * FROM asd1 WHERE _id = '"+price+"'", null);
//        if (cursor.moveToFirst()) {
//            do {
//                String pax_max1 = cursor.getString(6);
                max_pax.setText(max);
//            }while (cursor.moveToNext());
//        }

        TextView no_of_pax = (TextView) v.findViewById(R.id.no_of_pax);
        String present_pax = this.c.getString(this.c.getColumnIndex("present"));
        TextView tv = new TextView(context);
        tv.setText(present_pax);
        if (tv.getText().toString().equals("")) {
            no_of_pax.setText("0");
        }else {
            no_of_pax.setText(present_pax);
        }

//        linearLayouthi.setBackgroundResource(R.drawable.table_back_green);
//
//        if (pos==selectedPosition) {
//            linearLayouthi.setBackgroundResource(R.drawable.table_back_green);
//        }
//        else {
//            //linearLayouthi.setBackgroundResource(R.drawable.table_back_gr);
//
//            db1 = context.openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
//            Cursor mcursor = db1.rawQuery("SELECT count(*) FROM Table" + (pos+1) + "", null);
//            if (mcursor.moveToFirst()) {
//                int icount = mcursor.getInt(0);
//                if (icount > 0) {
//                    linearLayouthi.setBackgroundResource(R.drawable.table_back_red);
//                    //iv.setBackgroundColor(R.color.red1);
//                    //linearLayouthi.setBackgroundDrawable(getResources().getDrawable(R.drawable.table_back_green));
//                } else {
//                    linearLayouthi.setBackgroundResource(R.drawable.table_back_gr);
//                    //iv.setBackgroundColor(R.color.green);
//                    //linearLayouthi.setBackgroundDrawable(getResources().getDrawable(R.drawable.table_back_gr));
//                }
//            }
//            mcursor.close();
//
//
//        }

//        linearLayouthi.setBackgroundResource(R.drawable.table_back_green);
//
//        if (pos==selectedPosition) {
//            linearLayouthi.setBackgroundResource(R.drawable.table_back_green);
//        }
//        else {
//            linearLayouthi.setBackgroundResource(R.drawable.table_back_gr);
//
//
//
//
//
//        }



        ImageView overflow = (ImageView) v.findViewById(R.id.overflow);
        final PopupMenu popup = new PopupMenu(context, overflow);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.table_management_menu, popup.getMenu());
        overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.action_clear_pax) {
                            System.out.println("table is "+table_iddd+" "+String.valueOf(price));

                            Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.dialog_clear_pax);
                            dialog.show();

                            ImageView closetext = (ImageView) dialog.findViewById(R.id.closetext);
                            closetext.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            Button cancel = (Button) dialog.findViewById(R.id.cancel);
                            closetext.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            Button ok = (Button) dialog.findViewById(R.id.ok);
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    db.execSQL("UPDATE asd1 SET present = '' WHERE _id = '"+price+"'");
//                                    func(pos);
//                                    c.moveToPosition(pos);
//                                    notifyDataSetChanged();

                                    c.moveToPosition(pos);
                                    c.requery();
                                    notifyDataSetChanged();

                                    dialog.dismiss();
                                }
                            });

                        }
                        return true;
                    }
                });

            }
        });

        return v;
    }

    public void func(int pos) {
        this.c.moveToPosition(pos);
    }

    //@Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return context.openOrCreateDatabase(name, mode, factory);
    }

    public void setSelected(int position) {
        selectedPosition = position;
    }

}
