package com.intuition.ivepos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Rohithkumar on 4/24/2015.
 */
public class UpdateItemActivity extends Fragment {

    public SQLiteDatabase db = null;
    public Cursor cursor;

    public UpdateItemActivity() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_add_item, null);

        return rootview;
    }
}
