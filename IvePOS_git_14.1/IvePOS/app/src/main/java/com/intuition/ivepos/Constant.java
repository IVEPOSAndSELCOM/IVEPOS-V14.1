package com.intuition.ivepos;

import android.os.Environment;

import java.io.File;

/**
 * ????????????õ??????????????
 *
 * @author Administrator
 *
 */
public class Constant {

    public static final int MSG_BTHEARTBEATTHREAD_UPDATESTATUS = 100200;
    public static final int MSG_USBHEARTBEATTHREAD_UPDATESTATUS = 100300;

    public static final String dumpfile = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + File.separator
            + "dump.txt";

    public static String WebserviceUrl="https://theandroidpos.com/IVEPOS_NEW/";

//    public static String WebserviceUrl="http://13.232.180.189/IVEPOS/";

}
