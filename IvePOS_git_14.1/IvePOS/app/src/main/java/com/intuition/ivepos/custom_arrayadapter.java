package com.intuition.ivepos;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class custom_arrayadapter<T> extends ArrayAdapter<T> {

    public custom_arrayadapter(Context context, int resource, List<T> objects) {
        super(context, resource, new ArrayList<>(new HashSet<>(objects)));
    }
}
