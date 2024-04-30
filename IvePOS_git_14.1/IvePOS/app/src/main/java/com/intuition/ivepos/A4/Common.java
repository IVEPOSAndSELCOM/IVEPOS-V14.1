package com.intuition.ivepos.A4;

import android.content.Context;
import android.os.Environment;

import com.intuition.ivepos.R;

import java.io.File;

public class Common {

    public static String getAppPath(Context context){
//        File dir = new File(android.os.Environment.getExternalStorageDirectory()
//                +File.separator
//                +context.getResources().getString(R.string.app_name)
//                +File.separator
//        );
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                +File.separator
                +context.getResources().getString(R.string.app_name)
                +File.separator
        );

        if (!dir.exists())
            dir.mkdir();

        return dir.getPath() + File.separator;
    }
}