package com.intuition.ivepos;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.intuition.ivepos.syncapp.StubProviderApp;

/**
 * Created by Rohithkumar on 5/26/2017.
 */

public class UserPrivilege extends AppCompatActivity{

    public SQLiteDatabase db = null;
    Uri contentUri,resultUri;
    String getuser1_name, getuser2_name, getuser3_name, getuser4_name, getuser5_name, getuser6_name;
    String getuser1_permissions, getuser2_permissions, getuser3_permissions, getuser4_permissions, getuser5_permissions, getuser6_permissions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_privilege);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("SELECT * FROM User1", null);
        if (cursor.moveToFirst()){
            getuser1_name = cursor.getString(1);
        }

        Cursor cursor2 = db.rawQuery("SELECT * FROM User2", null);
        if (cursor2.moveToFirst()){
            getuser2_name = cursor2.getString(1);
        }

        Cursor cursor3 = db.rawQuery("SELECT * FROM User3", null);
        if (cursor3.moveToFirst()){
            getuser3_name = cursor3.getString(1);
        }

        Cursor cursor4 = db.rawQuery("SELECT * FROM User4", null);
        if (cursor4.moveToFirst()){
            getuser4_name = cursor4.getString(1);
        }

        Cursor cursor5 = db.rawQuery("SELECT * FROM User5", null);
        if (cursor5.moveToFirst()){
            getuser5_name = cursor5.getString(1);
        }

        Cursor cursor6 = db.rawQuery("SELECT * FROM User6", null);
        if (cursor6.moveToFirst()){
            getuser6_name = cursor6.getString(1);
        }


        final TextView user1_permission = (TextView) findViewById(R.id.user1_permission);
        final TextView user2_permission = (TextView) findViewById(R.id.user2_permission);
        final TextView user3_permission = (TextView) findViewById(R.id.user3_permission);
        final TextView user4_permission = (TextView) findViewById(R.id.user4_permission);
        final TextView user5_permission = (TextView) findViewById(R.id.user5_permission);
        final TextView user6_permission = (TextView) findViewById(R.id.user6_permission);

        final Cursor curosr_user1_permissions = db.rawQuery("SELECT * FROM User_privilege WHERE _id = '1'", null);
        if (curosr_user1_permissions.moveToFirst()){
            String one = curosr_user1_permissions.getString(2);
            String two = curosr_user1_permissions.getString(3);
            String three = curosr_user1_permissions.getString(4);
            String four = curosr_user1_permissions.getString(5);
            String five = curosr_user1_permissions.getString(6);
            String six = curosr_user1_permissions.getString(7);
            String seven = curosr_user1_permissions.getString(8);
            String eight = curosr_user1_permissions.getString(9);

            int i = 0;
            if (one.toString().equals("yes")){
                i = i+1;
            }
            if (two.toString().equals("yes")){
                i = i+1;
            }
            if (three.toString().equals("yes")){
                i = i+1;
            }
            if (four.toString().equals("yes")){
                i = i+1;
            }
            if (five.toString().equals("yes")){
                i = i+1;
            }
            if (six.toString().equals("yes")){
                i = i+1;
            }
            if (seven.toString().equals("yes")){
                i = i+1;
            }
            if (eight.toString().equals("yes")){
                i = i+1;
            }
            if (i==0){
                user1_permission.setText("No permissons granted");
            }
            if (i==1){
                user1_permission.setText("1/8 permissions granted");
            }
            if (i==2){
                user1_permission.setText("2/8 permissions granted");
            }
            if (i==3){
                user1_permission.setText("3/8 permissions granted");
            }
            if (i==4){
                user1_permission.setText("4/8 permissions granted");
            }
            if (i==5){
                user1_permission.setText("5/8 permissions granted");
            }
            if (i==6){
                user1_permission.setText("6/8 permissions granted");
            }
            if (i==7){
                user1_permission.setText("7/8 permissions granted");
            }
            if (i==8){
                user1_permission.setText("All permissions granted");
            }

        }


        final Cursor curosr_user2_permissions = db.rawQuery("SELECT * FROM User_privilege WHERE _id = '2'", null);
        if (curosr_user2_permissions.moveToFirst()){
            String one = curosr_user2_permissions.getString(2);
            String two = curosr_user2_permissions.getString(3);
            String three = curosr_user2_permissions.getString(4);
            String four = curosr_user2_permissions.getString(5);
            String five = curosr_user2_permissions.getString(6);
            String six = curosr_user2_permissions.getString(7);
            String seven = curosr_user2_permissions.getString(8);
            String eight = curosr_user2_permissions.getString(9);

            int i = 0;
            if (one.toString().equals("yes")){
                i = i+1;
            }
            if (two.toString().equals("yes")){
                i = i+1;
            }
            if (three.toString().equals("yes")){
                i = i+1;
            }
            if (four.toString().equals("yes")){
                i = i+1;
            }
            if (five.toString().equals("yes")){
                i = i+1;
            }
            if (six.toString().equals("yes")){
                i = i+1;
            }
            if (seven.toString().equals("yes")){
                i = i+1;
            }
            if (eight.toString().equals("yes")){
                i = i+1;
            }
            if (i==0){
                user2_permission.setText("No permissons granted");
            }
            if (i==1){
                user2_permission.setText("1/8 permissions granted");
            }
            if (i==2){
                user2_permission.setText("2/8 permissions granted");
            }
            if (i==3){
                user2_permission.setText("3/8 permissions granted");
            }
            if (i==4){
                user2_permission.setText("4/8 permissions granted");
            }
            if (i==5){
                user2_permission.setText("5/8 permissions granted");
            }
            if (i==6){
                user2_permission.setText("6/8 permissions granted");
            }
            if (i==7){
                user2_permission.setText("7/8 permissions granted");
            }
            if (i==8){
                user2_permission.setText("All permissions granted");
            }

        }

        Cursor curosr_user3_permissions = db.rawQuery("SELECT * FROM User_privilege WHERE _id = '3'", null);
        if (curosr_user3_permissions.moveToFirst()){
            String one = curosr_user3_permissions.getString(2);
            String two = curosr_user3_permissions.getString(3);
            String three = curosr_user3_permissions.getString(4);
            String four = curosr_user3_permissions.getString(5);
            String five = curosr_user3_permissions.getString(6);
            String six = curosr_user3_permissions.getString(7);
            String seven = curosr_user3_permissions.getString(8);
            String eight = curosr_user3_permissions.getString(9);

            int i = 0;
            if (one.toString().equals("yes")){
                i = i+1;
            }
            if (two.toString().equals("yes")){
                i = i+1;
            }
            if (three.toString().equals("yes")){
                i = i+1;
            }
            if (four.toString().equals("yes")){
                i = i+1;
            }
            if (five.toString().equals("yes")){
                i = i+1;
            }
            if (six.toString().equals("yes")){
                i = i+1;
            }
            if (seven.toString().equals("yes")){
                i = i+1;
            }
            if (eight.toString().equals("yes")){
                i = i+1;
            }
            if (i==0){
                user3_permission.setText("No permissons granted");
            }
            if (i==1){
                user3_permission.setText("1/8 permissions granted");
            }
            if (i==2){
                user3_permission.setText("2/8 permissions granted");
            }
            if (i==3){
                user3_permission.setText("3/8 permissions granted");
            }
            if (i==4){
                user3_permission.setText("4/8 permissions granted");
            }
            if (i==5){
                user3_permission.setText("5/8 permissions granted");
            }
            if (i==6){
                user3_permission.setText("6/8 permissions granted");
            }
            if (i==7){
                user3_permission.setText("7/8 permissions granted");
            }
            if (i==8){
                user3_permission.setText("All permissions granted");
            }

        }

        Cursor curosr_user4_permissions = db.rawQuery("SELECT * FROM User_privilege WHERE _id = '4'", null);
        if (curosr_user4_permissions.moveToFirst()){
            String one = curosr_user4_permissions.getString(2);
            String two = curosr_user4_permissions.getString(3);
            String three = curosr_user4_permissions.getString(4);
            String four = curosr_user4_permissions.getString(5);
            String five = curosr_user4_permissions.getString(6);
            String six = curosr_user4_permissions.getString(7);
            String seven = curosr_user4_permissions.getString(8);
            String eight = curosr_user4_permissions.getString(9);

            int i = 0;
            if (one.toString().equals("yes")){
                i = i+1;
            }
            if (two.toString().equals("yes")){
                i = i+1;
            }
            if (three.toString().equals("yes")){
                i = i+1;
            }
            if (four.toString().equals("yes")){
                i = i+1;
            }
            if (five.toString().equals("yes")){
                i = i+1;
            }
            if (six.toString().equals("yes")){
                i = i+1;
            }
            if (seven.toString().equals("yes")){
                i = i+1;
            }
            if (eight.toString().equals("yes")){
                i = i+1;
            }
            if (i==0){
                user4_permission.setText("No permissons granted");
            }
            if (i==1){
                user4_permission.setText("1/8 permissions granted");
            }
            if (i==2){
                user4_permission.setText("2/8 permissions granted");
            }
            if (i==3){
                user4_permission.setText("3/8 permissions granted");
            }
            if (i==4){
                user4_permission.setText("4/8 permissions granted");
            }
            if (i==5){
                user4_permission.setText("5/8 permissions granted");
            }
            if (i==6){
                user4_permission.setText("6/8 permissions granted");
            }
            if (i==7){
                user4_permission.setText("7/8 permissions granted");
            }
            if (i==8){
                user4_permission.setText("All permissions granted");
            }

        }

        Cursor curosr_user5_permissions = db.rawQuery("SELECT * FROM User_privilege WHERE _id = '5'", null);
        if (curosr_user5_permissions.moveToFirst()){
            String one = curosr_user5_permissions.getString(2);
            String two = curosr_user5_permissions.getString(3);
            String three = curosr_user5_permissions.getString(4);
            String four = curosr_user5_permissions.getString(5);
            String five = curosr_user5_permissions.getString(6);
            String six = curosr_user5_permissions.getString(7);
            String seven = curosr_user5_permissions.getString(8);
            String eight = curosr_user5_permissions.getString(9);

            int i = 0;
            if (one.toString().equals("yes")){
                i = i+1;
            }
            if (two.toString().equals("yes")){
                i = i+1;
            }
            if (three.toString().equals("yes")){
                i = i+1;
            }
            if (four.toString().equals("yes")){
                i = i+1;
            }
            if (five.toString().equals("yes")){
                i = i+1;
            }
            if (six.toString().equals("yes")){
                i = i+1;
            }
            if (seven.toString().equals("yes")){
                i = i+1;
            }
            if (eight.toString().equals("yes")){
                i = i+1;
            }
            if (i==0){
                user5_permission.setText("No permissons granted");
            }
            if (i==1){
                user5_permission.setText("1/8 permissions granted");
            }
            if (i==2){
                user5_permission.setText("2/8 permissions granted");
            }
            if (i==3){
                user5_permission.setText("3/8 permissions granted");
            }
            if (i==4){
                user5_permission.setText("4/8 permissions granted");
            }
            if (i==5){
                user5_permission.setText("5/8 permissions granted");
            }
            if (i==6){
                user5_permission.setText("6/8 permissions granted");
            }
            if (i==7){
                user5_permission.setText("7/8 permissions granted");
            }
            if (i==8){
                user5_permission.setText("All permissions granted");
            }

        }

        Cursor curosr_user6_permissions = db.rawQuery("SELECT * FROM User_privilege WHERE _id = '6'", null);
        if (curosr_user6_permissions.moveToFirst()){
            String one = curosr_user6_permissions.getString(2);
            String two = curosr_user6_permissions.getString(3);
            String three = curosr_user6_permissions.getString(4);
            String four = curosr_user6_permissions.getString(5);
            String five = curosr_user6_permissions.getString(6);
            String six = curosr_user6_permissions.getString(7);
            String seven = curosr_user6_permissions.getString(8);
            String eight = curosr_user6_permissions.getString(9);

            int i = 0;
            if (one.toString().equals("yes")){
                i = i+1;
            }
            if (two.toString().equals("yes")){
                i = i+1;
            }
            if (three.toString().equals("yes")){
                i = i+1;
            }
            if (four.toString().equals("yes")){
                i = i+1;
            }
            if (five.toString().equals("yes")){
                i = i+1;
            }
            if (six.toString().equals("yes")){
                i = i+1;
            }
            if (seven.toString().equals("yes")){
                i = i+1;
            }
            if (eight.toString().equals("yes")){
                i = i+1;
            }
            if (i==0){
                user6_permission.setText("No permissons granted");
            }
            if (i==1){
                user6_permission.setText("1/8 permissions granted");
            }
            if (i==2){
                user6_permission.setText("2/8 permissions granted");
            }
            if (i==3){
                user6_permission.setText("3/8 permissions granted");
            }
            if (i==4){
                user6_permission.setText("4/8 permissions granted");
            }
            if (i==5){
                user6_permission.setText("5/8 permissions granted");
            }
            if (i==6){
                user6_permission.setText("6/8 permissions granted");
            }
            if (i==7){
                user6_permission.setText("7/8 permissions granted");
            }
            if (i==8){
                user6_permission.setText("All permissions granted");
            }

        }

        TextView user1_name = (TextView) findViewById(R.id.user1_name);
        user1_name.setText(getuser1_name);

        TextView user2_name = (TextView) findViewById(R.id.user2_name);
        user2_name.setText(getuser2_name);

        TextView user3_name = (TextView) findViewById(R.id.user3_name);
        user3_name.setText(getuser3_name);

        TextView user4_name = (TextView) findViewById(R.id.user4_name);
        user4_name.setText(getuser4_name);

        TextView user5_name = (TextView) findViewById(R.id.user5_name);
        user5_name.setText(getuser5_name);

        TextView user6_name = (TextView) findViewById(R.id.user6_name);
        user6_name.setText(getuser6_name);


        LinearLayout back_activity = (LinearLayout) findViewById(R.id.back_activity);
        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout user1 = (LinearLayout) findViewById(R.id.user1);
        user1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(UserPrivilege.this, R.style.timepicker_date_dialog);
                dialog.setContentView(R.layout.dialog_user_privilege);
                dialog.show();

                ImageButton btncancel = (ImageButton) dialog.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                TextView user = (TextView) dialog.findViewById(R.id.user);
                user.setText(getuser1_name);

                final Switch mySwitch_rr = (Switch) dialog.findViewById(R.id.mySwitch_rr);
                final Switch mySwitch_pt = (Switch) dialog.findViewById(R.id.mySwitch_pt);
                final Switch mySwitch_rep = (Switch) dialog.findViewById(R.id.mySwitch_rep);
                final Switch mySwitch_set = (Switch) dialog.findViewById(R.id.mySwitch_set);
                final Switch mySwitch_back = (Switch) dialog.findViewById(R.id.mySwitch_back);
                final Switch mySwitch_cust = (Switch) dialog.findViewById(R.id.mySwitch_cust);
                final Switch mySwitch_ingre = (Switch) dialog.findViewById(R.id.mySwitch_ingre);
                final Switch mySwitch_subs = (Switch) dialog.findViewById(R.id.mySwitch_subs);

                Cursor c1 = db.rawQuery("SELECT * FROM User_privilege WHERE _id = '1'", null);
                if (c1.moveToFirst()){
                    String rr = c1.getString(2);
                    String pt = c1.getString(3);
                    String rep = c1.getString(4);
                    String set = c1.getString(5);
                    String back = c1.getString(6);
                    String cust = c1.getString(7);
                    String ingre = c1.getString(8);
                    String subs = c1.getString(9);

                    if (rr.toString().equals("yes")){
                        mySwitch_rr.setChecked(true);
                    }else {
                        mySwitch_rr.setChecked(false);
                    }

                    if (pt.toString().equals("yes")){
                        mySwitch_pt.setChecked(true);
                    }else {
                        mySwitch_pt.setChecked(false);
                    }

                    if (rep.toString().equals("yes")){
                        mySwitch_rep.setChecked(true);
                    }else {
                        mySwitch_rep.setChecked(false);
                    }

                    if (set.toString().equals("yes")){
                        mySwitch_set.setChecked(true);
                    }else {
                        mySwitch_set.setChecked(false);
                    }

                    if (back.toString().equals("yes")){
                        mySwitch_back.setChecked(true);
                    }else {
                        mySwitch_back.setChecked(false);
                    }

                    if (cust.toString().equals("yes")){
                        mySwitch_cust.setChecked(true);
                    }else {
                        mySwitch_cust.setChecked(false);
                    }

                    if (ingre.toString().equals("yes")){
                        mySwitch_ingre.setChecked(true);
                    }else {
                        mySwitch_ingre.setChecked(false);
                    }

                    if (subs.toString().equals("yes")){
                        mySwitch_subs.setChecked(true);
                    }else {
                        mySwitch_subs.setChecked(false);
                    }
                }

                ImageButton btnsave = (ImageButton) dialog.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues contentValues = new ContentValues();
                        if (mySwitch_rr.isChecked()){
                            contentValues.put("returns_refunds", "yes");
                        }else {
                            contentValues.put("returns_refunds", "no");
                        }
                        if (mySwitch_pt.isChecked()){
                            contentValues.put("product_tax", "yes");
                        }else {
                            contentValues.put("product_tax", "no");
                        }
                        if (mySwitch_rep.isChecked()){
                            contentValues.put("reports", "yes");
                        }else {
                            contentValues.put("reports", "no");
                        }
                        if (mySwitch_set.isChecked()){
                            contentValues.put("settings", "yes");
                        }else {
                            contentValues.put("settings", "no");
                        }
                        if (mySwitch_back.isChecked()){
                            contentValues.put("backup", "yes");
                        }else {
                            contentValues.put("backup", "no");
                        }
                        if (mySwitch_cust.isChecked()){
                            contentValues.put("customer", "yes");
                        }else {
                            contentValues.put("customer", "no");
                        }
                        if (mySwitch_ingre.isChecked()){
                            contentValues.put("ingredients", "yes");
                        }else {
                            contentValues.put("ingredients", "no");
                        }
                        if (mySwitch_subs.isChecked()){
                            contentValues.put("subscriptions", "yes");
                        }else {
                            contentValues.put("subscriptions", "no");
                        }
                        String where1 = "_id = '1' ";
                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User_privilege");
                        getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("User_privilege")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id","1")
                                .build();
                        getContentResolver().notifyChange(resultUri, null);
//                        db.update("User_privilege", contentValues, where1, new String[]{});
                        dialog.dismiss();

                        Cursor curosr_user1_permissions = db.rawQuery("SELECT * FROM User_privilege WHERE _id = '1'", null);
                        if (curosr_user1_permissions.moveToFirst()){
                            String one = curosr_user1_permissions.getString(2);
                            String two = curosr_user1_permissions.getString(3);
                            String three = curosr_user1_permissions.getString(4);
                            String four = curosr_user1_permissions.getString(5);
                            String five = curosr_user1_permissions.getString(6);
                            String six = curosr_user1_permissions.getString(7);
                            String seven = curosr_user1_permissions.getString(8);
                            String eight = curosr_user1_permissions.getString(9);

                            int i = 0;
                            if (one.toString().equals("yes")){
                                i = i+1;
                            }
                            if (two.toString().equals("yes")){
                                i = i+1;
                            }
                            if (three.toString().equals("yes")){
                                i = i+1;
                            }
                            if (four.toString().equals("yes")){
                                i = i+1;
                            }
                            if (five.toString().equals("yes")){
                                i = i+1;
                            }
                            if (six.toString().equals("yes")){
                                i = i+1;
                            }
                            if (seven.toString().equals("yes")){
                                i = i+1;
                            }
                            if (eight.toString().equals("yes")){
                                i = i+1;
                            }
                            if (i==0){
                                user1_permission.setText("No permissons granted");
                            }
                            if (i==1){
                                user1_permission.setText("1/8 permissions granted");
                            }
                            if (i==2){
                                user1_permission.setText("2/8 permissions granted");
                            }
                            if (i==3){
                                user1_permission.setText("3/8 permissions granted");
                            }
                            if (i==4){
                                user1_permission.setText("4/8 permissions granted");
                            }
                            if (i==5){
                                user1_permission.setText("5/8 permissions granted");
                            }
                            if (i==6){
                                user1_permission.setText("6/8 permissions granted");
                            }
                            if (i==7){
                                user1_permission.setText("7/8 permissions granted");
                            }
                            if (i==8){
                                user1_permission.setText("All permissions granted");
                            }

                        }

                    }
                });
            }
        });

        LinearLayout user2 = (LinearLayout) findViewById(R.id.user2);
        user2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(UserPrivilege.this, R.style.timepicker_date_dialog);
                dialog.setContentView(R.layout.dialog_user_privilege);
                dialog.show();

                ImageButton btncancel = (ImageButton) dialog.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                TextView user = (TextView) dialog.findViewById(R.id.user);
                user.setText(getuser2_name);

                final Switch mySwitch_rr = (Switch) dialog.findViewById(R.id.mySwitch_rr);
                final Switch mySwitch_pt = (Switch) dialog.findViewById(R.id.mySwitch_pt);
                final Switch mySwitch_rep = (Switch) dialog.findViewById(R.id.mySwitch_rep);
                final Switch mySwitch_set = (Switch) dialog.findViewById(R.id.mySwitch_set);
                final Switch mySwitch_back = (Switch) dialog.findViewById(R.id.mySwitch_back);
                final Switch mySwitch_cust = (Switch) dialog.findViewById(R.id.mySwitch_cust);
                final Switch mySwitch_ingre = (Switch) dialog.findViewById(R.id.mySwitch_ingre);
                final Switch mySwitch_subs = (Switch) dialog.findViewById(R.id.mySwitch_subs);

                Cursor c1 = db.rawQuery("SELECT * FROM User_privilege WHERE _id = '2'", null);
                if (c1.moveToFirst()){
                    String rr = c1.getString(2);
                    String pt = c1.getString(3);
                    String rep = c1.getString(4);
                    String set = c1.getString(5);
                    String back = c1.getString(6);
                    String cust = c1.getString(7);
                    String ingre = c1.getString(8);
                    String subs = c1.getString(9);

                    if (rr.toString().equals("yes")){
                        mySwitch_rr.setChecked(true);
                    }else {
                        mySwitch_rr.setChecked(false);
                    }

                    if (pt.toString().equals("yes")){
                        mySwitch_pt.setChecked(true);
                    }else {
                        mySwitch_pt.setChecked(false);
                    }

                    if (rep.toString().equals("yes")){
                        mySwitch_rep.setChecked(true);
                    }else {
                        mySwitch_rep.setChecked(false);
                    }

                    if (set.toString().equals("yes")){
                        mySwitch_set.setChecked(true);
                    }else {
                        mySwitch_set.setChecked(false);
                    }

                    if (back.toString().equals("yes")){
                        mySwitch_back.setChecked(true);
                    }else {
                        mySwitch_back.setChecked(false);
                    }

                    if (cust.toString().equals("yes")){
                        mySwitch_cust.setChecked(true);
                    }else {
                        mySwitch_cust.setChecked(false);
                    }

                    if (ingre.toString().equals("yes")){
                        mySwitch_ingre.setChecked(true);
                    }else {
                        mySwitch_ingre.setChecked(false);
                    }

                    if (subs.toString().equals("yes")){
                        mySwitch_subs.setChecked(true);
                    }else {
                        mySwitch_subs.setChecked(false);
                    }
                }

                ImageButton btnsave = (ImageButton) dialog.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues contentValues = new ContentValues();
                        if (mySwitch_rr.isChecked()){
                            contentValues.put("returns_refunds", "yes");
                        }else {
                            contentValues.put("returns_refunds", "no");
                        }
                        if (mySwitch_pt.isChecked()){
                            contentValues.put("product_tax", "yes");
                        }else {
                            contentValues.put("product_tax", "no");
                        }
                        if (mySwitch_rep.isChecked()){
                            contentValues.put("reports", "yes");
                        }else {
                            contentValues.put("reports", "no");
                        }
                        if (mySwitch_set.isChecked()){
                            contentValues.put("settings", "yes");
                        }else {
                            contentValues.put("settings", "no");
                        }
                        if (mySwitch_back.isChecked()){
                            contentValues.put("backup", "yes");
                        }else {
                            contentValues.put("backup", "no");
                        }
                        if (mySwitch_cust.isChecked()){
                            contentValues.put("customer", "yes");
                        }else {
                            contentValues.put("customer", "no");
                        }
                        if (mySwitch_ingre.isChecked()){
                            contentValues.put("ingredients", "yes");
                        }else {
                            contentValues.put("ingredients", "no");
                        }
                        if (mySwitch_subs.isChecked()){
                            contentValues.put("subscriptions", "yes");
                        }else {
                            contentValues.put("subscriptions", "no");
                        }
                        String where1 = "_id = '2' ";
                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User_privilege");
                        getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("User_privilege")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id","2")
                                .build();
                        getContentResolver().notifyChange(resultUri, null);
//                        db.update("User_privilege", contentValues, where1, new String[]{});
                        dialog.dismiss();

                        Cursor curosr_user2_permissions = db.rawQuery("SELECT * FROM User_privilege WHERE _id = '2'", null);
                        if (curosr_user2_permissions.moveToFirst()){
                            String one = curosr_user2_permissions.getString(2);
                            String two = curosr_user2_permissions.getString(3);
                            String three = curosr_user2_permissions.getString(4);
                            String four = curosr_user2_permissions.getString(5);
                            String five = curosr_user2_permissions.getString(6);
                            String six = curosr_user2_permissions.getString(7);
                            String seven = curosr_user2_permissions.getString(8);
                            String eight = curosr_user2_permissions.getString(9);

                            int i = 0;
                            if (one.toString().equals("yes")){
                                i = i+1;
                            }
                            if (two.toString().equals("yes")){
                                i = i+1;
                            }
                            if (three.toString().equals("yes")){
                                i = i+1;
                            }
                            if (four.toString().equals("yes")){
                                i = i+1;
                            }
                            if (five.toString().equals("yes")){
                                i = i+1;
                            }
                            if (six.toString().equals("yes")){
                                i = i+1;
                            }
                            if (seven.toString().equals("yes")){
                                i = i+1;
                            }
                            if (eight.toString().equals("yes")){
                                i = i+1;
                            }
                            if (i==0){
                                user2_permission.setText("No permissons granted");
                            }
                            if (i==1){
                                user2_permission.setText("1/8 permissions granted");
                            }
                            if (i==2){
                                user2_permission.setText("2/8 permissions granted");
                            }
                            if (i==3){
                                user2_permission.setText("3/8 permissions granted");
                            }
                            if (i==4){
                                user2_permission.setText("4/8 permissions granted");
                            }
                            if (i==5){
                                user2_permission.setText("5/8 permissions granted");
                            }
                            if (i==6){
                                user2_permission.setText("6/8 permissions granted");
                            }
                            if (i==7){
                                user2_permission.setText("7/8 permissions granted");
                            }
                            if (i==8){
                                user2_permission.setText("All permissions granted");
                            }

                        }

                    }
                });
            }
        });

        LinearLayout user3 = (LinearLayout) findViewById(R.id.user3);
        user3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(UserPrivilege.this, R.style.timepicker_date_dialog);
                dialog.setContentView(R.layout.dialog_user_privilege);
                dialog.show();

                ImageButton btncancel = (ImageButton) dialog.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                TextView user = (TextView) dialog.findViewById(R.id.user);
                user.setText(getuser3_name);

                final Switch mySwitch_rr = (Switch) dialog.findViewById(R.id.mySwitch_rr);
                final Switch mySwitch_pt = (Switch) dialog.findViewById(R.id.mySwitch_pt);
                final Switch mySwitch_rep = (Switch) dialog.findViewById(R.id.mySwitch_rep);
                final Switch mySwitch_set = (Switch) dialog.findViewById(R.id.mySwitch_set);
                final Switch mySwitch_back = (Switch) dialog.findViewById(R.id.mySwitch_back);
                final Switch mySwitch_cust = (Switch) dialog.findViewById(R.id.mySwitch_cust);
                final Switch mySwitch_ingre = (Switch) dialog.findViewById(R.id.mySwitch_ingre);
                final Switch mySwitch_subs = (Switch) dialog.findViewById(R.id.mySwitch_subs);

                Cursor c1 = db.rawQuery("SELECT * FROM User_privilege WHERE _id = '3'", null);
                if (c1.moveToFirst()){
                    String rr = c1.getString(2);
                    String pt = c1.getString(3);
                    String rep = c1.getString(4);
                    String set = c1.getString(5);
                    String back = c1.getString(6);
                    String cust = c1.getString(7);
                    String ingre = c1.getString(8);
                    String subs = c1.getString(9);

                    if (rr.toString().equals("yes")){
                        mySwitch_rr.setChecked(true);
                    }else {
                        mySwitch_rr.setChecked(false);
                    }

                    if (pt.toString().equals("yes")){
                        mySwitch_pt.setChecked(true);
                    }else {
                        mySwitch_pt.setChecked(false);
                    }

                    if (rep.toString().equals("yes")){
                        mySwitch_rep.setChecked(true);
                    }else {
                        mySwitch_rep.setChecked(false);
                    }

                    if (set.toString().equals("yes")){
                        mySwitch_set.setChecked(true);
                    }else {
                        mySwitch_set.setChecked(false);
                    }

                    if (back.toString().equals("yes")){
                        mySwitch_back.setChecked(true);
                    }else {
                        mySwitch_back.setChecked(false);
                    }

                    if (cust.toString().equals("yes")){
                        mySwitch_cust.setChecked(true);
                    }else {
                        mySwitch_cust.setChecked(false);
                    }

                    if (ingre.toString().equals("yes")){
                        mySwitch_ingre.setChecked(true);
                    }else {
                        mySwitch_ingre.setChecked(false);
                    }

                    if (subs.toString().equals("yes")){
                        mySwitch_subs.setChecked(true);
                    }else {
                        mySwitch_subs.setChecked(false);
                    }
                }

                ImageButton btnsave = (ImageButton) dialog.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues contentValues = new ContentValues();
                        if (mySwitch_rr.isChecked()){
                            contentValues.put("returns_refunds", "yes");
                        }else {
                            contentValues.put("returns_refunds", "no");
                        }
                        if (mySwitch_pt.isChecked()){
                            contentValues.put("product_tax", "yes");
                        }else {
                            contentValues.put("product_tax", "no");
                        }
                        if (mySwitch_rep.isChecked()){
                            contentValues.put("reports", "yes");
                        }else {
                            contentValues.put("reports", "no");
                        }
                        if (mySwitch_set.isChecked()){
                            contentValues.put("settings", "yes");
                        }else {
                            contentValues.put("settings", "no");
                        }
                        if (mySwitch_back.isChecked()){
                            contentValues.put("backup", "yes");
                        }else {
                            contentValues.put("backup", "no");
                        }
                        if (mySwitch_cust.isChecked()){
                            contentValues.put("customer", "yes");
                        }else {
                            contentValues.put("customer", "no");
                        }
                        if (mySwitch_ingre.isChecked()){
                            contentValues.put("ingredients", "yes");
                        }else {
                            contentValues.put("ingredients", "no");
                        }
                        if (mySwitch_subs.isChecked()){
                            contentValues.put("subscriptions", "yes");
                        }else {
                            contentValues.put("subscriptions", "no");
                        }
                        String where1 = "_id = '3' ";

                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User_privilege");
                        getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("User_privilege")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id","3")
                                .build();
                        getContentResolver().notifyChange(resultUri, null);
//                        db.update("User_privilege", contentValues, where1, new String[]{});
                        dialog.dismiss();

                        Cursor curosr_user3_permissions = db.rawQuery("SELECT * FROM User_privilege WHERE _id = '3'", null);
                        if (curosr_user3_permissions.moveToFirst()){
                            String one = curosr_user3_permissions.getString(2);
                            String two = curosr_user3_permissions.getString(3);
                            String three = curosr_user3_permissions.getString(4);
                            String four = curosr_user3_permissions.getString(5);
                            String five = curosr_user3_permissions.getString(6);
                            String six = curosr_user3_permissions.getString(7);
                            String seven = curosr_user3_permissions.getString(8);
                            String eight = curosr_user3_permissions.getString(9);

                            int i = 0;
                            if (one.toString().equals("yes")){
                                i = i+1;
                            }
                            if (two.toString().equals("yes")){
                                i = i+1;
                            }
                            if (three.toString().equals("yes")){
                                i = i+1;
                            }
                            if (four.toString().equals("yes")){
                                i = i+1;
                            }
                            if (five.toString().equals("yes")){
                                i = i+1;
                            }
                            if (six.toString().equals("yes")){
                                i = i+1;
                            }
                            if (seven.toString().equals("yes")){
                                i = i+1;
                            }
                            if (eight.toString().equals("yes")){
                                i = i+1;
                            }
                            if (i==0){
                                user3_permission.setText("No permissons granted");
                            }
                            if (i==1){
                                user3_permission.setText("1/8 permissions granted");
                            }
                            if (i==2){
                                user3_permission.setText("2/8 permissions granted");
                            }
                            if (i==3){
                                user3_permission.setText("3/8 permissions granted");
                            }
                            if (i==4){
                                user3_permission.setText("4/8 permissions granted");
                            }
                            if (i==5){
                                user3_permission.setText("5/8 permissions granted");
                            }
                            if (i==6){
                                user3_permission.setText("6/8 permissions granted");
                            }
                            if (i==7){
                                user3_permission.setText("7/8 permissions granted");
                            }
                            if (i==8){
                                user3_permission.setText("All permissions granted");
                            }

                        }

                    }
                });
            }
        });

        LinearLayout user4 = (LinearLayout) findViewById(R.id.user4);
        user4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(UserPrivilege.this, R.style.timepicker_date_dialog);
                dialog.setContentView(R.layout.dialog_user_privilege);
                dialog.show();

                ImageButton btncancel = (ImageButton) dialog.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                TextView user = (TextView) dialog.findViewById(R.id.user);
                user.setText(getuser4_name);

                final Switch mySwitch_rr = (Switch) dialog.findViewById(R.id.mySwitch_rr);
                final Switch mySwitch_pt = (Switch) dialog.findViewById(R.id.mySwitch_pt);
                final Switch mySwitch_rep = (Switch) dialog.findViewById(R.id.mySwitch_rep);
                final Switch mySwitch_set = (Switch) dialog.findViewById(R.id.mySwitch_set);
                final Switch mySwitch_back = (Switch) dialog.findViewById(R.id.mySwitch_back);
                final Switch mySwitch_cust = (Switch) dialog.findViewById(R.id.mySwitch_cust);
                final Switch mySwitch_ingre = (Switch) dialog.findViewById(R.id.mySwitch_ingre);
                final Switch mySwitch_subs = (Switch) dialog.findViewById(R.id.mySwitch_subs);

                Cursor c1 = db.rawQuery("SELECT * FROM User_privilege WHERE _id = '4'", null);
                if (c1.moveToFirst()){
                    String rr = c1.getString(2);
                    String pt = c1.getString(3);
                    String rep = c1.getString(4);
                    String set = c1.getString(5);
                    String back = c1.getString(6);
                    String cust = c1.getString(7);
                    String ingre = c1.getString(8);
                    String subs = c1.getString(9);

                    if (rr.toString().equals("yes")){
                        mySwitch_rr.setChecked(true);
                    }else {
                        mySwitch_rr.setChecked(false);
                    }

                    if (pt.toString().equals("yes")){
                        mySwitch_pt.setChecked(true);
                    }else {
                        mySwitch_pt.setChecked(false);
                    }

                    if (rep.toString().equals("yes")){
                        mySwitch_rep.setChecked(true);
                    }else {
                        mySwitch_rep.setChecked(false);
                    }

                    if (set.toString().equals("yes")){
                        mySwitch_set.setChecked(true);
                    }else {
                        mySwitch_set.setChecked(false);
                    }

                    if (back.toString().equals("yes")){
                        mySwitch_back.setChecked(true);
                    }else {
                        mySwitch_back.setChecked(false);
                    }

                    if (cust.toString().equals("yes")){
                        mySwitch_cust.setChecked(true);
                    }else {
                        mySwitch_cust.setChecked(false);
                    }

                    if (ingre.toString().equals("yes")){
                        mySwitch_ingre.setChecked(true);
                    }else {
                        mySwitch_ingre.setChecked(false);
                    }

                    if (subs.toString().equals("yes")){
                        mySwitch_subs.setChecked(true);
                    }else {
                        mySwitch_subs.setChecked(false);
                    }
                }

                ImageButton btnsave = (ImageButton) dialog.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues contentValues = new ContentValues();
                        if (mySwitch_rr.isChecked()){
                            contentValues.put("returns_refunds", "yes");
                        }else {
                            contentValues.put("returns_refunds", "no");
                        }
                        if (mySwitch_pt.isChecked()){
                            contentValues.put("product_tax", "yes");
                        }else {
                            contentValues.put("product_tax", "no");
                        }
                        if (mySwitch_rep.isChecked()){
                            contentValues.put("reports", "yes");
                        }else {
                            contentValues.put("reports", "no");
                        }
                        if (mySwitch_set.isChecked()){
                            contentValues.put("settings", "yes");
                        }else {
                            contentValues.put("settings", "no");
                        }
                        if (mySwitch_back.isChecked()){
                            contentValues.put("backup", "yes");
                        }else {
                            contentValues.put("backup", "no");
                        }
                        if (mySwitch_cust.isChecked()){
                            contentValues.put("customer", "yes");
                        }else {
                            contentValues.put("customer", "no");
                        }
                        if (mySwitch_ingre.isChecked()){
                            contentValues.put("ingredients", "yes");
                        }else {
                            contentValues.put("ingredients", "no");
                        }
                        if (mySwitch_subs.isChecked()){
                            contentValues.put("subscriptions", "yes");
                        }else {
                            contentValues.put("subscriptions", "no");
                        }
                        String where1 = "_id = '4' ";
                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User_privilege");
                        getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("User_privilege")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id","4")
                                .build();
                        getContentResolver().notifyChange(resultUri, null);
//                        db.update("User_privilege", contentValues, where1, new String[]{});
                        dialog.dismiss();

                        Cursor curosr_user4_permissions = db.rawQuery("SELECT * FROM User_privilege WHERE _id = '4'", null);
                        if (curosr_user4_permissions.moveToFirst()){
                            String one = curosr_user4_permissions.getString(2);
                            String two = curosr_user4_permissions.getString(3);
                            String three = curosr_user4_permissions.getString(4);
                            String four = curosr_user4_permissions.getString(5);
                            String five = curosr_user4_permissions.getString(6);
                            String six = curosr_user4_permissions.getString(7);
                            String seven = curosr_user4_permissions.getString(8);
                            String eight = curosr_user4_permissions.getString(9);

                            int i = 0;
                            if (one.toString().equals("yes")){
                                i = i+1;
                            }
                            if (two.toString().equals("yes")){
                                i = i+1;
                            }
                            if (three.toString().equals("yes")){
                                i = i+1;
                            }
                            if (four.toString().equals("yes")){
                                i = i+1;
                            }
                            if (five.toString().equals("yes")){
                                i = i+1;
                            }
                            if (six.toString().equals("yes")){
                                i = i+1;
                            }
                            if (seven.toString().equals("yes")){
                                i = i+1;
                            }
                            if (eight.toString().equals("yes")){
                                i = i+1;
                            }
                            if (i==0){
                                user4_permission.setText("No permissons granted");
                            }
                            if (i==1){
                                user4_permission.setText("1/8 permissions granted");
                            }
                            if (i==2){
                                user4_permission.setText("2/8 permissions granted");
                            }
                            if (i==3){
                                user4_permission.setText("3/8 permissions granted");
                            }
                            if (i==4){
                                user4_permission.setText("4/8 permissions granted");
                            }
                            if (i==5){
                                user4_permission.setText("5/8 permissions granted");
                            }
                            if (i==6){
                                user4_permission.setText("6/8 permissions granted");
                            }
                            if (i==7){
                                user4_permission.setText("7/8 permissions granted");
                            }
                            if (i==8){
                                user4_permission.setText("All permissions granted");
                            }

                        }

                    }
                });
            }
        });

        LinearLayout user5 = (LinearLayout) findViewById(R.id.user5);
        user5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(UserPrivilege.this, R.style.timepicker_date_dialog);
                dialog.setContentView(R.layout.dialog_user_privilege);
                dialog.show();

                ImageButton btncancel = (ImageButton) dialog.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                TextView user = (TextView) dialog.findViewById(R.id.user);
                user.setText(getuser5_name);

                final Switch mySwitch_rr = (Switch) dialog.findViewById(R.id.mySwitch_rr);
                final Switch mySwitch_pt = (Switch) dialog.findViewById(R.id.mySwitch_pt);
                final Switch mySwitch_rep = (Switch) dialog.findViewById(R.id.mySwitch_rep);
                final Switch mySwitch_set = (Switch) dialog.findViewById(R.id.mySwitch_set);
                final Switch mySwitch_back = (Switch) dialog.findViewById(R.id.mySwitch_back);
                final Switch mySwitch_cust = (Switch) dialog.findViewById(R.id.mySwitch_cust);
                final Switch mySwitch_ingre = (Switch) dialog.findViewById(R.id.mySwitch_ingre);
                final Switch mySwitch_subs = (Switch) dialog.findViewById(R.id.mySwitch_subs);

                Cursor c1 = db.rawQuery("SELECT * FROM User_privilege WHERE _id = '5'", null);
                if (c1.moveToFirst()){
                    String rr = c1.getString(2);
                    String pt = c1.getString(3);
                    String rep = c1.getString(4);
                    String set = c1.getString(5);
                    String back = c1.getString(6);
                    String cust = c1.getString(7);
                    String ingre = c1.getString(8);
                    String subs = c1.getString(9);

                    if (rr.toString().equals("yes")){
                        mySwitch_rr.setChecked(true);
                    }else {
                        mySwitch_rr.setChecked(false);
                    }

                    if (pt.toString().equals("yes")){
                        mySwitch_pt.setChecked(true);
                    }else {
                        mySwitch_pt.setChecked(false);
                    }

                    if (rep.toString().equals("yes")){
                        mySwitch_rep.setChecked(true);
                    }else {
                        mySwitch_rep.setChecked(false);
                    }

                    if (set.toString().equals("yes")){
                        mySwitch_set.setChecked(true);
                    }else {
                        mySwitch_set.setChecked(false);
                    }

                    if (back.toString().equals("yes")){
                        mySwitch_back.setChecked(true);
                    }else {
                        mySwitch_back.setChecked(false);
                    }

                    if (cust.toString().equals("yes")){
                        mySwitch_cust.setChecked(true);
                    }else {
                        mySwitch_cust.setChecked(false);
                    }

                    if (ingre.toString().equals("yes")){
                        mySwitch_ingre.setChecked(true);
                    }else {
                        mySwitch_ingre.setChecked(false);
                    }

                    if (subs.toString().equals("yes")){
                        mySwitch_subs.setChecked(true);
                    }else {
                        mySwitch_subs.setChecked(false);
                    }
                }

                ImageButton btnsave = (ImageButton) dialog.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues contentValues = new ContentValues();
                        if (mySwitch_rr.isChecked()){
                            contentValues.put("returns_refunds", "yes");
                        }else {
                            contentValues.put("returns_refunds", "no");
                        }
                        if (mySwitch_pt.isChecked()){
                            contentValues.put("product_tax", "yes");
                        }else {
                            contentValues.put("product_tax", "no");
                        }
                        if (mySwitch_rep.isChecked()){
                            contentValues.put("reports", "yes");
                        }else {
                            contentValues.put("reports", "no");
                        }
                        if (mySwitch_set.isChecked()){
                            contentValues.put("settings", "yes");
                        }else {
                            contentValues.put("settings", "no");
                        }
                        if (mySwitch_back.isChecked()){
                            contentValues.put("backup", "yes");
                        }else {
                            contentValues.put("backup", "no");
                        }
                        if (mySwitch_cust.isChecked()){
                            contentValues.put("customer", "yes");
                        }else {
                            contentValues.put("customer", "no");
                        }
                        if (mySwitch_ingre.isChecked()){
                            contentValues.put("ingredients", "yes");
                        }else {
                            contentValues.put("ingredients", "no");
                        }
                        if (mySwitch_subs.isChecked()){
                            contentValues.put("subscriptions", "yes");
                        }else {
                            contentValues.put("subscriptions", "no");
                        }
                        String where1 = "_id = '5' ";

                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User_privilege");
                        getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("User_privilege")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id","5")
                                .build();
                        getContentResolver().notifyChange(resultUri, null);
//                        db.update("User_privilege", contentValues, where1, new String[]{});
                        dialog.dismiss();

                        Cursor curosr_user5_permissions = db.rawQuery("SELECT * FROM User_privilege WHERE _id = '5'", null);
                        if (curosr_user5_permissions.moveToFirst()){
                            String one = curosr_user5_permissions.getString(2);
                            String two = curosr_user5_permissions.getString(3);
                            String three = curosr_user5_permissions.getString(4);
                            String four = curosr_user5_permissions.getString(5);
                            String five = curosr_user5_permissions.getString(6);
                            String six = curosr_user5_permissions.getString(7);
                            String seven = curosr_user5_permissions.getString(8);
                            String eight = curosr_user5_permissions.getString(9);

                            int i = 0;
                            if (one.toString().equals("yes")){
                                i = i+1;
                            }
                            if (two.toString().equals("yes")){
                                i = i+1;
                            }
                            if (three.toString().equals("yes")){
                                i = i+1;
                            }
                            if (four.toString().equals("yes")){
                                i = i+1;
                            }
                            if (five.toString().equals("yes")){
                                i = i+1;
                            }
                            if (six.toString().equals("yes")){
                                i = i+1;
                            }
                            if (seven.toString().equals("yes")){
                                i = i+1;
                            }
                            if (eight.toString().equals("yes")){
                                i = i+1;
                            }
                            if (i==0){
                                user5_permission.setText("No permissons granted");
                            }
                            if (i==1){
                                user5_permission.setText("1/8 permissions granted");
                            }
                            if (i==2){
                                user5_permission.setText("2/8 permissions granted");
                            }
                            if (i==3){
                                user5_permission.setText("3/8 permissions granted");
                            }
                            if (i==4){
                                user5_permission.setText("4/8 permissions granted");
                            }
                            if (i==5){
                                user5_permission.setText("5/8 permissions granted");
                            }
                            if (i==6){
                                user5_permission.setText("6/8 permissions granted");
                            }
                            if (i==7){
                                user5_permission.setText("7/8 permissions granted");
                            }
                            if (i==8){
                                user5_permission.setText("All permissions granted");
                            }

                        }

                    }
                });
            }
        });

        LinearLayout user6 = (LinearLayout) findViewById(R.id.user6);
        user6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(UserPrivilege.this, R.style.timepicker_date_dialog);
                dialog.setContentView(R.layout.dialog_user_privilege);
                dialog.show();

                ImageButton btncancel = (ImageButton) dialog.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                TextView user = (TextView) dialog.findViewById(R.id.user);
                user.setText(getuser6_name);

                final Switch mySwitch_rr = (Switch) dialog.findViewById(R.id.mySwitch_rr);
                final Switch mySwitch_pt = (Switch) dialog.findViewById(R.id.mySwitch_pt);
                final Switch mySwitch_rep = (Switch) dialog.findViewById(R.id.mySwitch_rep);
                final Switch mySwitch_set = (Switch) dialog.findViewById(R.id.mySwitch_set);
                final Switch mySwitch_back = (Switch) dialog.findViewById(R.id.mySwitch_back);
                final Switch mySwitch_cust = (Switch) dialog.findViewById(R.id.mySwitch_cust);
                final Switch mySwitch_ingre = (Switch) dialog.findViewById(R.id.mySwitch_ingre);
                final Switch mySwitch_subs = (Switch) dialog.findViewById(R.id.mySwitch_subs);

                Cursor c1 = db.rawQuery("SELECT * FROM User_privilege WHERE _id = '6'", null);
                if (c1.moveToFirst()){
                    String rr = c1.getString(2);
                    String pt = c1.getString(3);
                    String rep = c1.getString(4);
                    String set = c1.getString(5);
                    String back = c1.getString(6);
                    String cust = c1.getString(7);
                    String ingre = c1.getString(8);
                    String subs = c1.getString(9);

                    if (rr.toString().equals("yes")){
                        mySwitch_rr.setChecked(true);
                    }else {
                        mySwitch_rr.setChecked(false);
                    }

                    if (pt.toString().equals("yes")){
                        mySwitch_pt.setChecked(true);
                    }else {
                        mySwitch_pt.setChecked(false);
                    }

                    if (rep.toString().equals("yes")){
                        mySwitch_rep.setChecked(true);
                    }else {
                        mySwitch_rep.setChecked(false);
                    }

                    if (set.toString().equals("yes")){
                        mySwitch_set.setChecked(true);
                    }else {
                        mySwitch_set.setChecked(false);
                    }

                    if (back.toString().equals("yes")){
                        mySwitch_back.setChecked(true);
                    }else {
                        mySwitch_back.setChecked(false);
                    }

                    if (cust.toString().equals("yes")){
                        mySwitch_cust.setChecked(true);
                    }else {
                        mySwitch_cust.setChecked(false);
                    }

                    if (ingre.toString().equals("yes")){
                        mySwitch_ingre.setChecked(true);
                    }else {
                        mySwitch_ingre.setChecked(false);
                    }

                    if (subs.toString().equals("yes")){
                        mySwitch_subs.setChecked(true);
                    }else {
                        mySwitch_subs.setChecked(false);
                    }
                }

                ImageButton btnsave = (ImageButton) dialog.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues contentValues = new ContentValues();
                        if (mySwitch_rr.isChecked()){
                            contentValues.put("returns_refunds", "yes");
                        }else {
                            contentValues.put("returns_refunds", "no");
                        }
                        if (mySwitch_pt.isChecked()){
                            contentValues.put("product_tax", "yes");
                        }else {
                            contentValues.put("product_tax", "no");
                        }
                        if (mySwitch_rep.isChecked()){
                            contentValues.put("reports", "yes");
                        }else {
                            contentValues.put("reports", "no");
                        }
                        if (mySwitch_set.isChecked()){
                            contentValues.put("settings", "yes");
                        }else {
                            contentValues.put("settings", "no");
                        }
                        if (mySwitch_back.isChecked()){
                            contentValues.put("backup", "yes");
                        }else {
                            contentValues.put("backup", "no");
                        }
                        if (mySwitch_cust.isChecked()){
                            contentValues.put("customer", "yes");
                        }else {
                            contentValues.put("customer", "no");
                        }
                        if (mySwitch_ingre.isChecked()){
                            contentValues.put("ingredients", "yes");
                        }else {
                            contentValues.put("ingredients", "no");
                        }
                        if (mySwitch_subs.isChecked()){
                            contentValues.put("subscriptions", "yes");
                        }else {
                            contentValues.put("subscriptions", "no");
                        }
                        String where1 = "_id = '6' ";

                        contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "User_privilege");
                        getContentResolver().update(contentUri, contentValues,where1,new String[]{});
                        resultUri = new Uri.Builder()
                                .scheme("content")
                                .authority(StubProviderApp.AUTHORITY)
                                .path("User_privilege")
                                .appendQueryParameter("operation", "update")
                                .appendQueryParameter("_id","6")
                                .build();
                        getContentResolver().notifyChange(resultUri, null);
//                        db.update("User_privilege", contentValues, where1, new String[]{});
                        dialog.dismiss();

                        Cursor curosr_user6_permissions = db.rawQuery("SELECT * FROM User_privilege WHERE _id = '6'", null);
                        if (curosr_user6_permissions.moveToFirst()){
                            String one = curosr_user6_permissions.getString(2);
                            String two = curosr_user6_permissions.getString(3);
                            String three = curosr_user6_permissions.getString(4);
                            String four = curosr_user6_permissions.getString(5);
                            String five = curosr_user6_permissions.getString(6);
                            String six = curosr_user6_permissions.getString(7);
                            String seven = curosr_user6_permissions.getString(8);
                            String eight = curosr_user6_permissions.getString(9);

                            int i = 0;
                            if (one.toString().equals("yes")){
                                i = i+1;
                            }
                            if (two.toString().equals("yes")){
                                i = i+1;
                            }
                            if (three.toString().equals("yes")){
                                i = i+1;
                            }
                            if (four.toString().equals("yes")){
                                i = i+1;
                            }
                            if (five.toString().equals("yes")){
                                i = i+1;
                            }
                            if (six.toString().equals("yes")){
                                i = i+1;
                            }
                            if (seven.toString().equals("yes")){
                                i = i+1;
                            }
                            if (eight.toString().equals("yes")){
                                i = i+1;
                            }
                            if (i==0){
                                user6_permission.setText("No permissons granted");
                            }
                            if (i==1){
                                user6_permission.setText("1/8 permissions granted");
                            }
                            if (i==2){
                                user6_permission.setText("2/8 permissions granted");
                            }
                            if (i==3){
                                user6_permission.setText("3/8 permissions granted");
                            }
                            if (i==4){
                                user6_permission.setText("4/8 permissions granted");
                            }
                            if (i==5){
                                user6_permission.setText("5/8 permissions granted");
                            }
                            if (i==6){
                                user6_permission.setText("6/8 permissions granted");
                            }
                            if (i==7){
                                user6_permission.setText("7/8 permissions granted");
                            }
                            if (i==8){
                                user6_permission.setText("All permissions granted");
                            }

                        }

                    }
                });
            }
        });

    }
}
