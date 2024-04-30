package com.intuition.ivepos;

import android.os.Environment;

import java.io.File;

/**
 * ????????????õ??????????????
 *
 * @author Administrator
 *
 */
public class Constant1 {

    public static final int MSG_NETHEARTBEATTHREAD_UPDATESTATUS = 100100;

    public static final String dumpfile = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + File.separator
            + "dump.txt";
}
