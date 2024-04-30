package com.intuition.ivepos;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.WINDOW_SERVICE;

public class DynamicFragment extends Fragment {

    private SQLiteDatabase db,db1;
    String floorname;
    String table_iddd;
    GridView gridView;
    ImageCursorAdapter31 tableadapter;

    Cursor cursor;
    DownloadMusicfromInternet2 downloadMusicfromInternet;
    View view;

    public static DynamicFragment newInstance() {
        return new DynamicFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        floorname = getArguments().getString("floorname");
        table_iddd = getArguments().getString("table_iddd");
//        db = getActivity().openOrCreateDatabase("mydb_Appdata", MODE_PRIVATE, null);
//        db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", MODE_PRIVATE, null);
//        Toast.makeText(getActivity(), "text2 "+floorname, Toast.LENGTH_LONG).show();

    }

    // adding the layout with inflater
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dynamic, container, false);

        floorname = getArguments().getString("floorname");
        table_iddd = getArguments().getString("table_iddd");
        db = getActivity().openOrCreateDatabase("mydb_Appdata", MODE_PRIVATE, null);
        db1 = getActivity().openOrCreateDatabase("mydb_Salesdata", MODE_PRIVATE, null);
        System.out.println("Dynamic fragment "+table_iddd);
//        Toast.makeText(getActivity(), "text22 "+floorname, Toast.LENGTH_LONG).show();

        initViews(view);
        return view;
    }

    // initialise the categories
    private void initViews(View view) {
        final TextView textView = view.findViewById(R.id.commonTextView);
        textView.setText(String.valueOf("Category : " + getArguments().getInt("position")));

        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "category "+textView.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });

        gridView = (GridView) view.findViewById(R.id.gridviewitems);

        downloadMusicfromInternet = new DownloadMusicfromInternet2();
        downloadMusicfromInternet.execute();



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor5 = (Cursor) parent.getItemAtPosition(position);
                int item_content_id = cursor5.getInt(cursor5.getColumnIndex("_id"));
                String floorname = cursor5.getString(cursor5.getColumnIndex("floor"));
                String pDate = cursor5.getString(cursor5.getColumnIndex("pDate"));

                TextView name = (TextView) view.findViewById(R.id.name);

                Toast.makeText(getActivity(), "text "+floorname+" "+String.valueOf(item_content_id), Toast.LENGTH_LONG).show();

                final Dialog dialog = new Dialog(getActivity(), R.style.notitle);
                dialog.setContentView(R.layout.tab_management_dialog_empty_click);
                dialog.show();

                EditText pax_occupied = (EditText) dialog.findViewById(R.id.pax_occupied);

                Cursor cursor1 = db.rawQuery("SELECT * FROM asd1 WHERE _id = '"+item_content_id+"'", null);
                if (cursor1.moveToFirst()) {
                    String pax = cursor1.getString(7);
                    pax_occupied.setText(pax);
                }
                cursor1.close();

                TextView table = (TextView) dialog.findViewById(R.id.table);
                table.setText("Tab"+pDate);

                TextView table_name = (TextView) dialog.findViewById(R.id.table_name);
                table_name.setText(name.getText().toString());

                ImageButton btncancel = (ImageButton) dialog.findViewById(R.id.btncancel);
                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideKeyboard(getActivity());
                        donotshowKeyboard(getActivity());
                        dialog.dismiss();
                    }
                });

                ImageView btnsave = (ImageView) dialog.findViewById(R.id.btnsave);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pax_occupied.getText().toString().equals("") || pax_occupied.getText().toString().equals("0")) {
                            pax_occupied.setError("Enter valid number");
                        }else {
                            db.execSQL("UPDATE asd1 SET present = '"+pax_occupied.getText().toString()+"' WHERE _id = '"+item_content_id+"'");
                            dialog.dismiss();

//                            cursor5.moveToPosition(position);
//                            cursor5.requery();
//                            tableadapter.notifyDataSetChanged();

                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString("table", String.valueOf(item_content_id));
                            myEdit.apply();

                            Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
                            if (cursor3.moveToFirst()) {
                                String lite_pro = cursor3.getString(1);

                                TextView tv = new TextView(getActivity());
                                tv.setText(lite_pro);

                                if (tv.getText().toString().equals("Lite")) {
                                    Intent intent = new Intent(getActivity(), BeveragesMenuFragment_Dine_l.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(getActivity(), BeveragesMenuFragment_Dine.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }else {
                                Intent intent = new Intent(getActivity(), BeveragesMenuFragment_Dine_l.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }

                        }
                    }
                });

            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    // pause function call
    @Override
    public void onPause() {
        super.onPause();
    }

    // resume function call
    @Override
    public void onResume() {
        super.onResume();
    }

    // stop when we close
    @Override
    public void onStop() {
        super.onStop();
    }

    // destroy the view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cursor.close();
        db.close();
        tableadapter = null;
        gridView = null;
        downloadMusicfromInternet.cancel(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Dynamic fragment destroy");
        cursor.close();
        db.close();
        tableadapter = null;
        gridView = null;
        downloadMusicfromInternet.cancel(true);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void donotshowKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }


    class DownloadMusicfromInternet2 extends AsyncTask<Void, Integer, Void> {
        private ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.timepicker_date_dialog);

        @Override
        protected Void doInBackground(Void... params) {



            return null;
        }

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage("Loading...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            dialog.setMax(1000);
            //Set the current progress to zero
            dialog.setProgress(0);


            dialog.show();
        }


        @Override
        protected void onPostExecute(Void result) {

//            dialog.dismiss();

            System.out.println("table is "+floorname);
            cursor = db.rawQuery("Select * from asd1 WHERE position = '"+getArguments().getInt("position")+"'", null);
            String[] fromFieldNames = {"pName", "_id", "floor", "max"};
            int[] toViewsID = {R.id.name};
            Log.e("Checamos que hay id", String.valueOf(R.id.name));
            tableadapter = new ImageCursorAdapter31(getActivity(), R.layout.tables_management_listview, cursor, fromFieldNames, toViewsID, table_iddd);

            gridView.setAdapter(tableadapter);// Assign adapter to ListView.... here... the bitch error


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    downloadMusicfromInternet.cancel(true);
                }
            }, 2000); //3000 L = 3 detik

        }
    }
}
