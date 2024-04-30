package com.intuition.ivepos;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;



public class WorkThread1 extends Thread {

    private static final String TAG = "WorkThread";
    public static Handler workHandler = null;
    private static Looper mLooper = null;
    public static Handler targetHandler = null;
    public static NETRWThread netRW;
    public static NETHeartBeatThread netHeartBeat;
    private static boolean threadInitOK = false;
    private static boolean isConnecting = false;

    public WorkThread1(Handler handler) {
        threadInitOK = false;
        targetHandler = handler;
        netRW = NETRWThread.InitInstant();
        netHeartBeat = NETHeartBeatThread.InitInstant(targetHandler);
    }

    @Override
    public void start() {
        super.start();
        while (!threadInitOK)
            ;

        netRW.start();
        netHeartBeat.start();
    }

    @Override
    public void run() {
        Looper.prepare();
        mLooper = Looper.myLooper();
        if (null == mLooper)
            Log.v(TAG, "mLooper is null pointer");
        else
            Log.v(TAG, "mLooper is valid");
        workHandler = new WorkHandler();
        threadInitOK = true;
        Looper.loop();
    }

    private static class WorkHandler extends Handler {

        boolean needPauseHeartBeat(int msgCode) {
            boolean isNeeded = true;

            switch (msgCode) {
                case Global1.CMD_POS_WRITE1:
                case Global1.CMD_POS_READ:
                case Global1.CMD_POS_SETKEY:
                case Global1.CMD_POS_CHECKKEY:
                case Global1.CMD_POS_PRINTPICTURE:
                case Global1.CMD_POS_PRINTBWPICTURE:
                case Global1.CMD_POS_SALIGN:
                case Global1.CMD_POS_SETLINEHEIGHT:
                case Global1.CMD_POS_SETRIGHTSPACE:
                case Global1.CMD_POS_STEXTOUT:
                case Global1.CMD_POS_SETCHARSETANDCODEPAGE:
                case Global1.CMD_POS_SETBARCODE:
                case Global1.CMD_POS_SETQRCODE:
                case Global1.CMD_EPSON_SETQRCODE:
                case Global1.MSG_PAUSE_HEARTBEAT:
                    isNeeded = true;
                    break;
                default:
                    isNeeded = false;
                    break;
            }

            return isNeeded;
        }

        boolean needResumeHeartBeat(int msgCode) {
            boolean isNeeded = true;

            switch (msgCode) {
                case Global1.CMD_POS_WRITE1:
                case Global1.CMD_POS_READ:
                case Global1.CMD_POS_SETKEY:
                case Global1.CMD_POS_CHECKKEY:
                case Global1.CMD_POS_PRINTPICTURE:
                case Global1.CMD_POS_PRINTBWPICTURE:
                case Global1.CMD_POS_SALIGN:
                case Global1.CMD_POS_SETLINEHEIGHT:
                case Global1.CMD_POS_SETRIGHTSPACE:
                case Global1.CMD_POS_STEXTOUT:
                case Global1.CMD_POS_SETCHARSETANDCODEPAGE:
                case Global1.CMD_POS_SETBARCODE:
                case Global1.CMD_POS_SETQRCODE:
                case Global1.CMD_EPSON_SETQRCODE:
                case Global1.MSG_RESUME_HEARTBEAT:
                    isNeeded = true;
                    break;
                default:
                    isNeeded = false;
                    break;
            }

            return isNeeded;
        }

        @Override
        public void handleMessage(Message msg) {


            if (needPauseHeartBeat(msg.what)) {
                NETHeartBeatThread.PauseHeartBeat();
            }

            switch (msg.what) {


                case Global1.MSG_WORKTHREAD_HANDLER_CONNECTNET: {
                    isConnecting = true;
                    IO1.SetCurPort(IO1.PORT_NET);
                    int PortNumber = msg.arg1;
                    String IPAddress = (String) msg.obj;
                    boolean result = NETRWThread.Open(IPAddress, PortNumber);
                    Message smsg = targetHandler
                            .obtainMessage(Global1.MSG_WORKTHREAD_SEND_CONNECTNETRESULT);
                    if (result) {
                        smsg.arg1 = 1;
                    } else {
                        smsg.arg1 = 0;
                    }
                    targetHandler.sendMessage(smsg);


                    if (result) {
                        NETHeartBeatThread.BeginHeartBeat();
                    }
                    isConnecting = false;
                    break;
                }



                case Global1.CMD_WRITE1: {
                    Bundle data = msg.getData();
                    byte[] buffer = data.getByteArray(Global1.BYTESPARA1);
                    int offset = data.getInt(Global1.INTPARA1);
                    int count = data.getInt(Global1.INTPARA2);

                    Message smsg = targetHandler
                            .obtainMessage(Global1.CMD_WRITE1RESULT);
                    if (IO1.Write(buffer, offset, count) == count) {
                        smsg.arg1 = 1;
                    } else {
                        smsg.arg1 = 0;
                    }
                    targetHandler.sendMessage(smsg);

                    break;
                }

                case Global1.CMD_POS_WRITE1: {
                    Bundle data = msg.getData();
                    byte[] buffer = data.getByteArray(Global1.BYTESPARA1);
                    int offset = data.getInt(Global1.INTPARA1);
                    int count = data.getInt(Global1.INTPARA2);

                    byte[] precbuf = new byte[1];
                    int timeout = 1000;
                    boolean result = Pos1.POS_QueryStatus(precbuf,timeout);
                    Message smsg = targetHandler
                            .obtainMessage(Global1.CMD_POS_WRITE1RESULT);

                    if (result) {
                        IO1.Write(buffer, offset, count);
                        smsg.arg1 = 1;
                    } else {
                        smsg.arg1 = 0;
                    }
                    targetHandler.sendMessage(smsg);

                    break;
                }

                case Global1.CMD_POS_SETKEY: {
                    Bundle data = msg.getData();
                    byte[] key = data.getByteArray(Global1.BYTESPARA1);

                    byte[] precbuf = new byte[1];
                    int timeout = 1000;
                    boolean result = Pos1.POS_QueryStatus(precbuf,timeout);
                    Message smsg = targetHandler
                            .obtainMessage(Global1.CMD_POS_SETKEYRESULT);

                    if (result) {
                        Pos1.POS_SetKey(key);
                        smsg.arg1 = 1;
                    } else {
                        smsg.arg1 = 0;
                    }
                    targetHandler.sendMessage(smsg);

                    break;
                }

                case Global1.CMD_POS_CHECKKEY: {
                    Bundle data = msg.getData();
                    byte[] key = data.getByteArray(Global1.BYTESPARA1);
                    byte[] random = data.getByteArray(Global1.BYTESPARA2);
                    byte[] precbuf = new byte[1];
                    int timeout = 1000;
                    boolean result = Pos1.POS_QueryStatus(precbuf,timeout);
                    Message smsg = targetHandler
                            .obtainMessage(Global1.CMD_POS_CHECKKEYRESULT);
                    if (result) {
                        if (Pos1.POS_CheckKey(key, random))
                            smsg.arg1 = 1;
                        else
                            smsg.arg1 = 0;
                    } else {
                        smsg.arg1 = 0;
                    }
                    targetHandler.sendMessage(smsg);

                    break;
                }

                case Global1.CMD_POS_PRINTPICTURE: {
                    Bundle data = msg.getData();
                    //Bitmap mBitmap = (Bitmap) data.get(Global1.OBJECT1);
                    Bitmap mBitmap = (Bitmap) data.getParcelable(Global1.PARCE1);
                    int nWidth = data.getInt(Global1.INTPARA1);
                    int nMode = data.getInt(Global1.INTPARA2);
                    byte[] precbuf = new byte[1];
                    int timeout = 1000;
                    boolean result = Pos1.POS_QueryStatus(precbuf,timeout);
                    Message smsg = targetHandler
                            .obtainMessage(Global1.CMD_POS_PRINTPICTURERESULT);
                    if (result) {
                        Pos1.POS_PrintPicture(mBitmap, nWidth, nMode);
                        if (Pos1.POS_QueryStatus(precbuf,timeout))
                            smsg.arg1 = 1;
                        else
                            smsg.arg1 = 0;
                    } else {
                        smsg.arg1 = 0;
                    }
                    targetHandler.sendMessage(smsg);

                    break;
                }

                case Global1.CMD_POS_PRINTBWPICTURE: {
                    Bundle data = msg.getData();
                    //Bitmap mBitmap = (Bitmap) data.get(Global1.OBJECT1);
                    Bitmap mBitmap = (Bitmap) data.getParcelable(Global1.PARCE1);
                    int nWidth = data.getInt(Global1.INTPARA1);
                    int nMode = data.getInt(Global1.INTPARA2);
                    byte[] precbuf = new byte[1];
                    int timeout = 1000;
                    boolean result = Pos1.POS_QueryStatus(precbuf,timeout);
                    Message smsg = targetHandler
                            .obtainMessage(Global1.CMD_POS_PRINTPICTURERESULT);
                    if (result) {
                        Pos1.POS_PrintBWPic(mBitmap, nWidth, nMode);
                        if (Pos1.POS_QueryStatus(precbuf,timeout))
                            smsg.arg1 = 1;
                        else
                            smsg.arg1 = 0;
                    } else {
                        smsg.arg1 = 0;
                    }
                    targetHandler.sendMessage(smsg);

                    break;
                }

                case Global1.CMD_POS_SALIGN: {
                    Bundle data = msg.getData();
                    int align = data.getInt(Global1.INTPARA1);
                    byte[] precbuf = new byte[1];
                    int timeout = 1000;
                    boolean result = Pos1.POS_QueryStatus(precbuf,timeout);
                    Message smsg = targetHandler
                            .obtainMessage(Global1.CMD_POS_SALIGNRESULT);
                    if (result) {
                        Pos1.POS_S_Align(align);
                        smsg.arg1 = 1;
                    } else {
                        smsg.arg1 = 0;
                    }
                    targetHandler.sendMessage(smsg);

                    break;
                }

                case Global1.CMD_POS_SETLINEHEIGHT: {
                    Bundle data = msg.getData();
                    int nHeight = data.getInt(Global1.INTPARA1);
                    byte[] precbuf = new byte[1];
                    int timeout = 1000;
                    boolean result = Pos1.POS_QueryStatus(precbuf,timeout);
                    Message smsg = targetHandler
                            .obtainMessage(Global1.CMD_POS_SETLINEHEIGHTRESULT);
                    if (result) {
                        Pos1.POS_SetLineHeight(nHeight);
                        smsg.arg1 = 1;
                    } else {
                        smsg.arg1 = 0;
                    }
                    targetHandler.sendMessage(smsg);

                    break;
                }

                case Global1.CMD_POS_SETRIGHTSPACE: {
                    Bundle data = msg.getData();
                    int nDistance = data.getInt(Global1.INTPARA1);
                    byte[] precbuf = new byte[1];
                    int timeout = 1000;
                    boolean result = Pos1.POS_QueryStatus(precbuf,timeout);
                    Message smsg = targetHandler
                            .obtainMessage(Global1.CMD_POS_SETRIGHTSPACERESULT);
                    if (result) {
                        Pos1.POS_SetRightSpacing(nDistance);
                        smsg.arg1 = 1;
                    } else {
                        smsg.arg1 = 0;
                    }
                    targetHandler.sendMessage(smsg);

                    break;
                }

                case Global1.CMD_POS_STEXTOUT: {
                    Bundle data = msg.getData();
                    String pszString = data.getString(Global1.STRPARA1);
                    String encoding = data.getString(Global1.STRPARA2);
                    int nOrgx = data.getInt(Global1.INTPARA1);
                    int nWidthTimes = data.getInt(Global1.INTPARA2);
                    int nHeightTimes = data.getInt(Global1.INTPARA3);
                    int nFontType = data.getInt(Global1.INTPARA4);
                    int nFontStyle = data.getInt(Global1.INTPARA5);

                    byte[] precbuf = new byte[1];
                    int timeout = 1000;
                    boolean result = Pos1.POS_QueryStatus(precbuf,timeout);
                    Message smsg = targetHandler
                            .obtainMessage(Global1.CMD_POS_STEXTOUTRESULT);
                    if (result) {
                        Pos1.POS_S_TextOut(pszString, encoding, nOrgx, nWidthTimes,
                                nHeightTimes, nFontType, nFontStyle);
                        smsg.arg1 = 1;
                    } else {
                        smsg.arg1 = 0;
                    }
                    targetHandler.sendMessage(smsg);

                    break;
                }

                case Global1.CMD_POS_SETCHARSETANDCODEPAGE: {
                    Bundle data = msg.getData();
                    int nCharSet = data.getInt(Global1.INTPARA1);
                    int nCodePage = data.getInt(Global1.INTPARA2);

                    byte[] precbuf = new byte[1];
                    int timeout = 1000;
                    boolean result = Pos1.POS_QueryStatus(precbuf,timeout);
                    Message smsg = targetHandler
                            .obtainMessage(Global1.CMD_POS_SETCHARSETANDCODEPAGERESULT);
                    if (result) {
                        Pos1.POS_SetCharSetAndCodePage(nCharSet, nCodePage);
                        smsg.arg1 = 1;
                    } else {
                        smsg.arg1 = 0;
                    }
                    targetHandler.sendMessage(smsg);

                    break;
                }

                case Global1.CMD_POS_SETBARCODE: {
                    Bundle data = msg.getData();
                    String strBarcode = data.getString(Global1.STRPARA1);
                    int nOrgx = data.getInt(Global1.INTPARA1);
                    int nType = data.getInt(Global1.INTPARA2);
                    int nWidthX = data.getInt(Global1.INTPARA3);
                    int nHeight = data.getInt(Global1.INTPARA4);
                    int nHriFontType = data.getInt(Global1.INTPARA5);
                    int nHriFontPosition = data.getInt(Global1.INTPARA6);

                    byte[] precbuf = new byte[1];
                    int timeout = 1000;
                    boolean result = Pos1.POS_QueryStatus(precbuf,timeout);
                    Message smsg = targetHandler
                            .obtainMessage(Global1.CMD_POS_SETBARCODERESULT);
                    if (result) {
                        Pos1.POS_S_SetBarcode(strBarcode, nOrgx, nType, nWidthX,
                                nHeight, nHriFontType, nHriFontPosition);
                        smsg.arg1 = 1;
                    } else {
                        smsg.arg1 = 0;
                    }
                    targetHandler.sendMessage(smsg);

                    break;
                }

                case Global1.CMD_POS_SETQRCODE: {
                    Bundle data = msg.getData();
                    String strQrcode = data.getString(Global1.STRPARA1);
                    int nWidthX = data.getInt(Global1.INTPARA1);
                    int necl = data.getInt(Global1.INTPARA2);

                    byte[] precbuf = new byte[1];
                    int timeout = 1000;
                    boolean result = Pos1.POS_QueryStatus(precbuf,timeout);
                    Message smsg = targetHandler
                            .obtainMessage(Global1.CMD_POS_SETQRCODERESULT);
                    if (result) {
                        Pos1.POS_S_SetQRcode(strQrcode, nWidthX, necl);
                        smsg.arg1 = 1;
                    } else {
                        smsg.arg1 = 0;
                    }
                    targetHandler.sendMessage(smsg);

                    break;
                }

                case Global1.CMD_EPSON_SETQRCODE: {
                    Bundle data = msg.getData();
                    String strQrcode = data.getString(Global1.STRPARA1);
                    int nWidthX = data.getInt(Global1.INTPARA1);
                    int necl = data.getInt(Global1.INTPARA2);

                    byte[] precbuf = new byte[1];
                    int timeout = 1000;
                    boolean result = Pos1.POS_QueryStatus(precbuf,timeout);
                    Message smsg = targetHandler
                            .obtainMessage(Global1.CMD_EPSON_SETQRCODERESULT);
                    if (result) {
                        Pos1.POS_EPSON_SetQRCode(strQrcode, nWidthX, necl);
                        smsg.arg1 = 1;
                    } else {
                        smsg.arg1 = 0;
                    }
                    targetHandler.sendMessage(smsg);

                    break;
                }
            }


            if (needResumeHeartBeat(msg.what)) {
                NETHeartBeatThread.ResumeHeartBeat();
            }
        }
    }

    public void quit() {
        try {
            NETHeartBeatThread.Quit();
            netHeartBeat = null;
            NETRWThread.Quit();
            netRW = null;

            if (null != mLooper) {
                mLooper.quit();
                mLooper = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void disconnectNet() {
        try {
            NETRWThread.Close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void connectNet(String IPAddress, int PortNumber) {
        if ((null != workHandler) && (null != mLooper)) {
            Message msg = workHandler
                    .obtainMessage(Global1.MSG_WORKTHREAD_HANDLER_CONNECTNET);
            msg.arg1 = PortNumber;
            msg.obj = IPAddress;
            workHandler.sendMessage(msg);
        } else {
            if (null == workHandler)
                Log.v("WorkThread connectNet", "workHandler is null pointer");

            if (null == mLooper)
                Log.v("WorkThread connectNet", "mLooper is null pointer");
        }
    }


    public boolean isConnecting() {
        return isConnecting;
    }

    public boolean isConnected() {
        if (NETRWThread.IsOpened())
            return true;
        else
            return false;
    }

    /**
     *
     * @param cmd
     */
    public void handleCmd(int cmd, Bundle data) {
        if ((null != workHandler) && (null != mLooper)) {
            Message msg = workHandler.obtainMessage(cmd);
            msg.setData(data);
            workHandler.sendMessage(msg);
        } else {
            if (null == workHandler)
                Log.v(TAG, "workHandler is null pointer");

            if (null == mLooper)
                Log.v(TAG, "mLooper is null pointer");
        }
    }

    public void handleCmd1(byte[] cmd, Bundle data) {
        if ((null != workHandler) && (null != mLooper)) {
            Message msg = workHandler.obtainMessage();
            msg.setData(data);
            workHandler.sendMessage(msg);
        } else {
            if (null == workHandler)
                Log.v(TAG, "workHandler is null pointer");

            if (null == mLooper)
                Log.v(TAG, "mLooper is null pointer");
        }
    }

}
