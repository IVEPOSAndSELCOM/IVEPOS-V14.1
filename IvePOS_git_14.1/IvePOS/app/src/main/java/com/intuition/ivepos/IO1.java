package com.intuition.ivepos;

import android.os.Environment;

/**
 * IO ?????????????? ???????????IO?????????? IO ????rwbt??rwwifi??rwusb??rwbuf????????
 *
 * @author Administrator
 *
 */
public class IO1 {

    public static final String debugtxt = Environment
            .getExternalStorageDirectory() + "/debug.txt";

    public static final int PORT_NET = 1;

    private static int curPort = PORT_NET;

    public static synchronized int GetCurPort() {
        return curPort;
    }

    public static synchronized void SetCurPort(int port) {
        curPort = port;
    }

    public static synchronized int Write(byte[] buffer, int offset, int count) {
        FileUtils1.AddToFile(DataUtils1.bytesToStr(buffer, offset, count)
                + "\r\n", debugtxt);
        if (curPort == PORT_NET) {
            return NETRWThread.Write(buffer, offset, count);
        } else {
            return 0;
        }
    }

    public static synchronized int Read(byte[] buffer, int byteOffset,
                                        int byteCount, int timeout) {
        if (curPort == PORT_NET) {
            return NETRWThread.Read(buffer, byteOffset, byteCount, timeout);
        } else {
            return 0;
        }
    }

    public static synchronized boolean Request(byte sendbuf[], int sendlen,
                                               int requestlen, byte recbuf[], Integer reclen, int timeout) {
        if (curPort == PORT_NET) {
            return NETRWThread.Request(sendbuf, sendlen, requestlen, recbuf,
                    reclen, timeout);
        } else {
            return false;
        }
    }

    public static synchronized void ClrRec() {
        if (curPort == PORT_NET)
            NETRWThread.ClrRec();

    }

    public static synchronized boolean IsEmpty() {
        if (curPort == PORT_NET)
            return NETRWThread.IsEmpty();

        else
            return true;
    }

    public static synchronized byte GetByte() {
        if (curPort == PORT_NET)
            return NETRWThread.GetByte();

        else
            return 0;
    }

    public static synchronized boolean IsOpened() {
        if (curPort == PORT_NET)
            return NETRWThread.IsOpened();

        else
            return false;
    }

}
