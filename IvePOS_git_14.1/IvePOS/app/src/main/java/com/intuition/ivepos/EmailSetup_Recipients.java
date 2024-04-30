package com.intuition.ivepos;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

/**
 * Created by Rohithkumar on 6/9/2017.
 */

public class EmailSetup_Recipients extends AppCompatActivity {

    public SQLiteDatabase db = null;

    Switch mySwitch;
    TextView timeset;
    private int hour;
    private int minute;

    final static int RQS_1 = 1;

    String NAME, NAMEEE;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_recipients);


        final Calendar c = Calendar.getInstance();
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);


        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

        final ListView listView = (ListView) findViewById(R.id.list);

//        final Dialog dialog_recipient_credentials = new Dialog(EmailSetup_Recipients.this, R.style.timepicker_date_dialog);
//        dialog_recipient_credentials.setContentView(R.layout.dialog_settings_email_recipient);
//        dialog_recipient_credentials.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        dialog_recipient_credentials.show();

//        ImageButton btncancel = (ImageButton) findViewById(R.id.btncancel);
//        btncancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog_recipient_credentials.dismiss();
//            }
//        });


        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.back_activity);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

//                new MakeRequestTask(mCredential).execute();
            }
        });


        final Cursor cursor = db.rawQuery("SELECT * FROM Email_recipient", null);
        String[] fromFieldNames = {"name", "ph_no", "email"};
        // the XML defined views which the data will be bound to
        int[] toViewsID = {R.id.tv, R.id.tv1, R.id.tv2};
//                Log.e("Checamos que hay id", String.valueOf(R.id.name));
        final SimpleCursorAdapter ddataAdapter = new SimpleCursorAdapter(EmailSetup_Recipients.this, R.layout.email_recipient_listview, cursor, fromFieldNames, toViewsID, 0);
        listView.setAdapter(ddataAdapter);


        final TextView no_reci = (TextView) findViewById(R.id.no_reci);
        if (ddataAdapter.isEmpty()){
            no_reci.setVisibility(View.VISIBLE);
        }else {
            no_reci.setVisibility(View.GONE);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Cursor cv = (Cursor) parent.getItemAtPosition(position);

                final String rec_id = cv.getString(cv.getColumnIndex("_id"));
                final String rec_name = cv.getString(cv.getColumnIndex("name"));
                final String rec_pho = cv.getString(cv.getColumnIndex("ph_no"));
                final String rec_email = cv.getString(cv.getColumnIndex("email"));

                final Dialog dialog_edit_recipient_credentials = new Dialog(EmailSetup_Recipients.this, R.style.timepicker_date_dialog);
                dialog_edit_recipient_credentials.setContentView(R.layout.dialog_settings_email_recipient_add);
                dialog_edit_recipient_credentials.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog_edit_recipient_credentials.show();

                LinearLayout qwer = (LinearLayout) dialog_edit_recipient_credentials.findViewById(R.id.qwer);
                qwer.setVisibility(View.VISIBLE);

                ImageButton btncancel = (ImageButton) dialog_edit_recipient_credentials.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_edit_recipient_credentials.dismiss();
                    }
                });

                Button delete = (Button) dialog_edit_recipient_credentials.findViewById(R.id.cancel);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog_delete_recipient_credentials = new Dialog(EmailSetup_Recipients.this, R.style.timepicker_date_dialog);
                        dialog_delete_recipient_credentials.setContentView(R.layout.dialog_settings_email_recipient_del_con);
                        dialog_delete_recipient_credentials.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialog_delete_recipient_credentials.show();

                        ImageView closetext = (ImageView) dialog_delete_recipient_credentials.findViewById(R.id.closetext);
                        closetext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog_delete_recipient_credentials.dismiss();
                            }
                        });

                        Button cancel = (Button) dialog_delete_recipient_credentials.findViewById(R.id.cancel);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog_delete_recipient_credentials.dismiss();
                            }
                        });

                        Button delete = (Button) dialog_delete_recipient_credentials.findViewById(R.id.ok);
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String where = "_id = '"+rec_id+"'";
                                db.delete("Email_recipient", where, new String[]{});
                                dialog_delete_recipient_credentials.dismiss();
                                dialog_edit_recipient_credentials.dismiss();

                                final Cursor cursor = db.rawQuery("SELECT * FROM Email_recipient", null);
                                String[] fromFieldNames = {"name", "ph_no", "email"};
                                // the XML defined views which the data will be bound to
                                int[] toViewsID = {R.id.tv, R.id.tv1, R.id.tv2};
//                              Log.e("Checamos que hay id", String.valueOf(R.id.name));
                                final SimpleCursorAdapter ddataAdapter = new SimpleCursorAdapter(EmailSetup_Recipients.this, R.layout.email_recipient_listview, cursor, fromFieldNames, toViewsID, 0);
                                listView.setAdapter(ddataAdapter);

                                cursor.moveToPosition(position);
                                cursor.requery();
                                ddataAdapter.notifyDataSetChanged();


                                if (ddataAdapter.isEmpty()){
                                    no_reci.setVisibility(View.VISIBLE);
                                }else {
                                    no_reci.setVisibility(View.GONE);
                                }


//                                Cursor cursora = db.rawQuery("SELECT * FROM Email_recipient", null);
//                                int gb = cursora.getCount();
//                                recipient_auto_generation_status3.setText(String.valueOf(gb)+ " Recipients");

                            }
                        });


                    }
                });

                final TextInputLayout name_layout = (TextInputLayout) dialog_edit_recipient_credentials.findViewById(R.id.name_layout);
                TextInputLayout ph_no_layout = (TextInputLayout) dialog_edit_recipient_credentials.findViewById(R.id.ph_no_layout);
                final TextInputLayout email_layout = (TextInputLayout) dialog_edit_recipient_credentials.findViewById(R.id.email_layout);

                final EditText editText_name = (EditText) dialog_edit_recipient_credentials.findViewById(R.id.editText_name);
                final EditText editText_ph_no = (EditText) dialog_edit_recipient_credentials.findViewById(R.id.editText_ph_no);
                final EditText editText_email = (EditText) dialog_edit_recipient_credentials.findViewById(R.id.editText_email);


                editText_name.setText(rec_name);
                editText_ph_no.setText(rec_pho);
                editText_email.setText(rec_email);

                ImageButton btnsave = (ImageButton) dialog_edit_recipient_credentials.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        if (editText_name.getText().toString().equals("") || editText_email.getText().toString().equals("")){
                            if (editText_name.getText().toString().equals("")){
                                name_layout.setError("Enter valid name");
                            }
                            if (editText_email.getText().toString().equals("")){
                                email_layout.setError("Enter valid email");
                            }
                        }else {
                            if (editText_email.getText().toString().matches(emailPattern)) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("name", editText_name.getText().toString());
                                contentValues.put("ph_no", editText_ph_no.getText().toString());
                                contentValues.put("email", editText_email.getText().toString());
                                String where = "_id = '" + rec_id + "'";
                                db.update("Email_recipient", contentValues, where, new String[]{});
                                dialog_edit_recipient_credentials.dismiss();


                                final Cursor cursor = db.rawQuery("SELECT * FROM Email_recipient", null);
                                String[] fromFieldNames = {"name", "ph_no", "email"};
                                // the XML defined views which the data will be bound to
                                int[] toViewsID = {R.id.tv, R.id.tv1, R.id.tv2};
//                              Log.e("Checamos que hay id", String.valueOf(R.id.name));
                                final SimpleCursorAdapter ddataAdapter = new SimpleCursorAdapter(EmailSetup_Recipients.this, R.layout.email_recipient_listview, cursor, fromFieldNames, toViewsID, 0);
                                listView.setAdapter(ddataAdapter);

                                cursor.moveToPosition(position);
                                cursor.requery();
                                ddataAdapter.notifyDataSetChanged();


                                if (ddataAdapter.isEmpty()) {
                                    no_reci.setVisibility(View.VISIBLE);
                                } else {
                                    no_reci.setVisibility(View.GONE);
                                }

//                            Cursor cursora = db.rawQuery("SELECT * FROM Email_recipient", null);
//                            int gb = cursora.getCount();
//                            recipient_auto_generation_status3.setText(String.valueOf(gb)+ " Recipients");
                            }else {
                                email_layout.setError("Enter valid email");
                            }
                        }
                    }
                });

                editText_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        name_layout.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                editText_email.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        email_layout.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

            }
        });


        ImageButton btnadd = (ImageButton) findViewById(R.id.btnadd);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog_add_recipient_credentials = new Dialog(EmailSetup_Recipients.this, R.style.timepicker_date_dialog);
                dialog_add_recipient_credentials.setContentView(R.layout.dialog_settings_email_recipient_add);
//                dialog_add_recipient_credentials.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog_add_recipient_credentials.show();


                LinearLayout qwer = (LinearLayout) dialog_add_recipient_credentials.findViewById(R.id.qwer);
                qwer.setVisibility(View.GONE);

                ImageButton btncancel = (ImageButton) dialog_add_recipient_credentials.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_add_recipient_credentials.dismiss();
                    }
                });


                final TextInputLayout name_layout = (TextInputLayout) dialog_add_recipient_credentials.findViewById(R.id.name_layout);
                TextInputLayout ph_no_layout = (TextInputLayout) dialog_add_recipient_credentials.findViewById(R.id.ph_no_layout);
                final TextInputLayout email_layout = (TextInputLayout) dialog_add_recipient_credentials.findViewById(R.id.email_layout);

                final EditText editText_name = (EditText) dialog_add_recipient_credentials.findViewById(R.id.editText_name);
                editText_name.requestFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText_name, InputMethodManager.SHOW_IMPLICIT);

                final EditText editText_ph_no = (EditText) dialog_add_recipient_credentials.findViewById(R.id.editText_ph_no);
                final EditText editText_email = (EditText) dialog_add_recipient_credentials.findViewById(R.id.editText_email);

                ImageButton btnsave = (ImageButton) dialog_add_recipient_credentials.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editText_name.getText().toString().equals("") || editText_email.getText().toString().equals("")){
                            if (editText_name.getText().toString().equals("")){
                                name_layout.setError("Enter valid name");
                            }
                            if (editText_email.getText().toString().equals("")){
                                email_layout.setError("Enter valid email");
                            }
                        }else {
                            if (editText_email.getText().toString().matches(emailPattern)) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("name", editText_name.getText().toString());
                                contentValues.put("ph_no", editText_ph_no.getText().toString());
                                contentValues.put("email", editText_email.getText().toString());
                                db.insert("Email_recipient", null, contentValues);
                                dialog_add_recipient_credentials.dismiss();


                                final Cursor cursor = db.rawQuery("SELECT * FROM Email_recipient", null);
                                String[] fromFieldNames = {"name", "ph_no", "email"};
                                // the XML defined views which the data will be bound to
                                int[] toViewsID = {R.id.tv, R.id.tv1, R.id.tv2};
//                              Log.e("Checamos que hay id", String.valueOf(R.id.name));
                                final SimpleCursorAdapter ddataAdapter = new SimpleCursorAdapter(EmailSetup_Recipients.this, R.layout.email_recipient_listview, cursor, fromFieldNames, toViewsID, 0);
                                listView.setAdapter(ddataAdapter);

//                                cursor.moveToPosition(position);
//                                cursor.requery();
//                                ddataAdapter.notifyDataSetChanged();

                                if (ddataAdapter.isEmpty()){
                                    no_reci.setVisibility(View.VISIBLE);
                                }else {
                                    no_reci.setVisibility(View.GONE);
                                }

//                            Cursor cursora = db.rawQuery("SELECT * FROM Email_recipient", null);
//                            int gb = cursora.getCount();
//                            recipient_auto_generation_status3.setText(String.valueOf(gb)+ " Recipients");
                            }else {
                                email_layout.setError("Enter valid email");
                            }

                        }
                    }
                });

                editText_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        name_layout.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                editText_email.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        email_layout.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

            }
        });

        ImageButton btnadd1 = (ImageButton) findViewById(R.id.btnadd1);
        btnadd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(EmailSetup_Recipients.this, R.style.notitle);
                dialog.setContentView(R.layout.dialog_schedule_sendmail_time);
                dialog.show();

                Button cancel = (Button) dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                mySwitch = (Switch)dialog.findViewById(R.id.mySwitch);

                timeset = (TextView)dialog.findViewById(R.id.timeget);

                timeset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(EmailSetup_Recipients.this, R.style.timepicker_date_dialog, timePickerListener, hour, minute,
                                false);

                        timePickerDialog.show();
                    }
                });

                db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                Cursor allrowss = db.rawQuery("SELECT * FROM Schedule_mail_on_off WHERE _id = '1'", null);
                if (allrowss.moveToFirst()) {
                    do {
                        NAME = allrowss.getString(1);
                    } while (allrowss.moveToNext());
                }

                if (NAME.toString().equals("On")){
                    mySwitch.setChecked(true);
                }

                db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                Cursor aallrowss = db.rawQuery("SELECT * FROM Schedule_mail_time WHERE _id = '1'", null);
                if (aallrowss.moveToFirst()) {
                    do {
                        NAMEEE = aallrowss.getString(1);
                    } while (aallrowss.moveToNext());
                }
                timeset.setText(NAMEEE);


                mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        //Toast.makeText(getActivity(), "12", Toast.LENGTH_SHORT).show();

                        if (isChecked) {

                            Cursor allrowss = db.rawQuery("SELECT * FROM Schedule_mail_on_off WHERE _id = '1'", null);
                            if (allrowss.moveToFirst()) {
                                do {
                                    NAME = allrowss.getString(1);
                                } while (allrowss.moveToNext());
                            }

                            if (NAME.toString().equals("On")) {
                                ContentValues newValues = new ContentValues();
                                newValues.put("status", "Off");
                                String where = "_id = '1'";
                                db.update("Schedule_mail_on_off", newValues, where, new String[]{});
                            } else {
                                ContentValues newValues = new ContentValues();
                                newValues.put("status", "On");
                                String where = "_id = '1'";
                                db.update("Schedule_mail_on_off", newValues, where, new String[]{});
                            }


                        } else {
                            Cursor allrowss = db.rawQuery("SELECT * FROM Schedule_mail_on_off WHERE _id = '1'", null);
                            if (allrowss.moveToFirst()) {
                                do {
                                    NAME = allrowss.getString(1);
                                } while (allrowss.moveToNext());
                            }

                            if (NAME.toString().equals("On")) {
                                ContentValues newValues = new ContentValues();
                                newValues.put("status", "Off");
                                String where = "_id = '1'";
                                db.update("Schedule_mail_on_off", newValues, where, new String[]{});
                            } else {
                                ContentValues newValues = new ContentValues();
                                newValues.put("status", "On");
                                String where = "_id = '1'";
                                db.update("Schedule_mail_on_off", newValues, where, new String[]{});
                            }
                        }


                    }
                });

                Button timesave = (Button)dialog.findViewById(R.id.savetime);
                timesave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String time = timeset.getText().toString();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("time", time);
                        String where1 = "_id = '1' ";
                        db.update("Schedule_mail_time", contentValues, where1, new String[]{});

                        dialog.dismiss();
                    }
                });

            }
        });

    }

    private android.app.TimePickerDialog.OnTimeSetListener timePickerListener = new android.app.TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime(hour, minute);

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minutes);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);



                    if(calSet.compareTo(calNow) <= 0){
                        //Today Set time passed, count to tomorrow
                        calSet.add(Calendar.DATE, 1);

                        //Toast.makeText(getActivity(), " monday", Toast.LENGTH_SHORT).show();
                    }
                    setAlarm(calSet);

                    //Toast.makeText(getActivity(), " monday is selected ", Toast.LENGTH_SHORT).show();



        }
    };

    private void setAlarm(Calendar targetCal){
        Intent intent = new Intent(EmailSetup_Recipients.this, Schedule_send_mail.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(EmailSetup_Recipients.this, RQS_1, intent, 0);
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(EmailSetup_Recipients.this, RQS_1, intent, PendingIntent.FLAG_MUTABLE);
        }
        else {
            pendingIntent = PendingIntent.getBroadcast(EmailSetup_Recipients.this, RQS_1, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        timeset.setText(aTime);
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(EmailSetup_Recipients.this, MultiFragPreferenceActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//    }
}
