//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.intuition.ivepos;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
//import com.RT_Printer.RTPrinterActivity;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

public class WifiPrintDriver_Retail {
    private static final String TAG = "WifiPrintDriver_Retail";
    private static final boolean D = true;
    public static Socket mysocket = null;
    public static OutputStream mWifiOutputStream = null;
    public static InputStream mWifiInputStream = null;
    private final Handler mHandler;
    private static WifiPrintDriver_Retail.ConnectedThread mConnectedThread;
    private static int mState;
    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;
    public static final int UPCA = 0;
    public static final int UPCE = 1;
    public static final int EAN13 = 2;
    public static final int EAN8 = 3;
    public static final int CODE39 = 4;
    public static final int ITF = 5;
    public static final int CODEBAR = 6;
    public static final int CODE93 = 7;
    public static final int Code128_B = 8;
    public static final int CODE11 = 9;
    public static final int MSI = 10;

    public WifiPrintDriver_Retail(Context context, Handler handler) {
        this.mHandler = handler;
    }

    private synchronized void setState(int state) {
        Log.d("WifiPrintDriver_Retail", "setState() " + mState + " -> " + state);
        mState = state;
        this.mHandler.obtainMessage(1, state, -1).sendToTarget();
    }

    public synchronized void stop() {
        Log.d("WifiPrintDriver_Retail", "stop");
        if(mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        this.setState(0);
    }

    public boolean WIFISocket(String ip, int port) {
        boolean error = false;
        if(mysocket != null) {
            try {
                mysocket.close();
            } catch (IOException var7) {
                var7.printStackTrace();
            }

            mysocket = null;
        }

        try {
            mysocket = new Socket(ip, port);
            if(mysocket != null) {
                mWifiOutputStream = mysocket.getOutputStream();
                mWifiInputStream = mysocket.getInputStream();
                mConnectedThread = new WifiPrintDriver_Retail.ConnectedThread();
                mConnectedThread.start();
                this.setState(1);
            } else {
                mWifiOutputStream = null;
                mWifiInputStream = null;
                error = true;
            }
        } catch (UnknownHostException var5) {
            var5.printStackTrace();
            return false;
        } catch (IOException var6) {
            var6.printStackTrace();
            return false;
        }

        if(error) {
            this.stop();
            return false;
        } else {
            return true;
        }
    }

    public static void WIFI_Write(String dataString) {
        byte[] data = null;
        if(mState == 1) {
            WifiPrintDriver_Retail.ConnectedThread r = mConnectedThread;

            try {
                data = dataString.getBytes("GBK");
            } catch (UnsupportedEncodingException var5) {
                var5.printStackTrace();
            }

            r.write(data);
        }
    }

    public void WIFI_Write(String dataString, boolean bGBK) {
        byte[] data = null;
        if(mState == 1) {
            WifiPrintDriver_Retail.ConnectedThread r = mConnectedThread;
            if(bGBK) {
                try {
                    data = dataString.getBytes("GBK");
                } catch (UnsupportedEncodingException var6) {
                    ;
                }
            } else {
                data = dataString.getBytes();
            }

            r.write(data);
        }
    }

    public static void WIFI_Write(byte[] out) {
        if(mState == 1) {
            WifiPrintDriver_Retail.ConnectedThread r = mConnectedThread;
            r.write(out);
        }
    }

    public void WIFI_Write(byte[] out, int dataLen) {
        if(mState == 1) {
            WifiPrintDriver_Retail.ConnectedThread r = mConnectedThread;
            r.write(out, dataLen);
        }
    }

    public boolean IsNoConnection() {
        return mState != 1;
    }

    public static boolean InitPrinter() {
        byte[] combyte = new byte[]{27, 64};
        if(mState != 1) {
            return false;
        } else {
            WIFI_Write(combyte);
            return true;
        }
    }

    public static void WakeUpPritner() {
        byte[] b = new byte[3];

        try {
            WIFI_Write(b);
            Thread.sleep(100L);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public void Begin() {
        WakeUpPritner();
        InitPrinter();
    }

    public void LF() {
        byte[] cmd = new byte[]{13};
        WIFI_Write(cmd);
    }

    public static void CR() {
        byte[] cmd = new byte[]{10};
        WIFI_Write(cmd);
    }

    public void SelftestPrint() {
        byte[] cmd = new byte[]{18, 84};
        this.WIFI_Write(cmd, 2);
    }

    public void StatusInquiry() {
        byte[] cmd = new byte[]{29, 73, 66, 16, 4, 2};
        this.WIFI_Write(cmd, 6);
    }

    public static void SetRightSpacing(byte Distance) {
        byte[] cmd = new byte[]{27, 32, Distance};
        WIFI_Write(cmd);
    }

    public static void SetAbsolutePrintPosition(byte nL, byte nH) {
        byte[] cmd = new byte[]{27, 36, nL, nH};
        WIFI_Write(cmd);
    }

    public static void SetRelativePrintPosition(byte nL, byte nH) {
        byte[] cmd = new byte[]{27, 92, nL, nH};
        WIFI_Write(cmd);
    }

    public static void SetDefaultLineSpacing() {
        byte[] cmd = new byte[]{27, 50};
        WIFI_Write(cmd);
    }

    public void SetLineSpacing(byte LineSpacing) {
        byte[] cmd = new byte[]{27, 51, LineSpacing};
        WIFI_Write(cmd);
    }

    public static void SetLeftStartSpacing(byte nL, byte nH) {
        byte[] cmd = new byte[]{29, 76, nL, nH};
        WIFI_Write(cmd);
    }

    public static void SetAreaWidth(byte nL, byte nH) {
        byte[] cmd = new byte[]{29, 87, nL, nH};
        WIFI_Write(cmd);
    }

    public static void SetCharacterPrintMode(byte CharacterPrintMode) {
        byte[] cmd = new byte[]{27, 33, CharacterPrintMode};
        WIFI_Write(cmd);
    }

    public void SetUnderline(byte UnderlineEn) {
        byte[] cmd = new byte[]{27, 45, UnderlineEn};
        WIFI_Write(cmd);
    }

    public void SetBold(byte BoldEn) {
        byte[] cmd = new byte[]{27, 69, BoldEn};
        WIFI_Write(cmd);
    }

    public void SetCharacterFont(byte Font) {
        byte[] cmd = new byte[]{27, 77, Font};
        WIFI_Write(cmd);
    }

    public static void SetRotate(byte RotateEn) {
        byte[] cmd = new byte[]{27, 86, RotateEn};
        WIFI_Write(cmd);
    }

    public void SetAlignMode(byte AlignMode) {
        byte[] cmd = new byte[]{27, 97, AlignMode};
        WIFI_Write(cmd);
    }

    public static void SetInvertPrint(byte InvertModeEn) {
        byte[] cmd = new byte[]{27, 123, InvertModeEn};
        WIFI_Write(cmd);
    }

    public void SetFontEnlarge(byte FontEnlarge) {
        byte[] cmd = new byte[]{29, 33, FontEnlarge};
        WIFI_Write(cmd);
    }

    public void SetBlackReversePrint(byte BlackReverseEn) {
        byte[] cmd = new byte[]{29, 66, BlackReverseEn};
        WIFI_Write(cmd);
    }

    public static void SetChineseCharacterMode(byte ChineseCharacterMode) {
        byte[] cmd = new byte[]{28, 33, ChineseCharacterMode};
        WIFI_Write(cmd);
    }

    public static void SelChineseCodepage() {
        byte[] cmd = new byte[]{28, 38};
        WIFI_Write(cmd);
    }

    public static void CancelChineseCodepage() {
        byte[] cmd = new byte[]{28, 46};
        WIFI_Write(cmd);
    }

    public static void SetChineseUnderline(byte ChineseUnderlineEn) {
        byte[] cmd = new byte[]{28, 45, ChineseUnderlineEn};
        WIFI_Write(cmd);
    }

    public static void OpenDrawer(byte DrawerNumber, byte PulseStartTime, byte PulseEndTime) {
        byte[] cmd = new byte[]{27, 112, DrawerNumber, PulseStartTime, PulseEndTime};
        WIFI_Write(cmd);
    }

    public void CutPaper() {
        byte[] cmd = new byte[]{27, 105};
        WIFI_Write(cmd);
    }

    public void PartialCutPaper() {
        byte[] cmd = new byte[]{27, 109};
        WIFI_Write(cmd);
    }

    public void FeedAndCutPaper(byte CutMode) {
        byte[] cmd = new byte[]{29, 86, CutMode};
        WIFI_Write(cmd);
    }

    public void FeedAndCutPaper(byte CutMode, byte FeedDistance) {
        byte[] cmd = new byte[]{29, 86, CutMode, FeedDistance};
        WIFI_Write(cmd);
    }

    public void AddCodePrint(int CodeType, String data) {
        switch(CodeType) {
            case 0:
                UPCA(data);
                break;
            case 1:
                UPCE(data);
                break;
            case 2:
                EAN13(data);
                break;
            case 3:
                EAN8(data);
                break;
            case 4:
                CODE39(data);
                break;
            case 5:
                ITF(data);
                break;
            case 6:
                CODEBAR(data);
                break;
            case 7:
                CODE93(data);
                break;
            case 8:
                Code128_B(data);
            case 9:
            case 10:
        }

    }

    public static void UPCA(String data) {
        int m = 0;
        int num = data.length();
        int mIndex = 0;
        byte[] cmd = new byte[1024];
        int var6 = mIndex + 1;
        cmd[mIndex] = 29;
        cmd[var6++] = 107;
        cmd[var6++] = (byte)m;

        int i;
        for(i = 0; i < num; ++i) {
            if(data.charAt(i) > 57 || data.charAt(i) < 48) {
                return;
            }
        }

        if(num <= 30) {
            for(i = 0; i < num; ++i) {
                cmd[var6++] = (byte)data.charAt(i);
            }

            WIFI_Write(cmd);
        }
    }

    public static void UPCE(String data) {
        int m = 1;
        int num = data.length();
        int mIndex = 0;
        byte[] cmd = new byte[1024];
        int var6 = mIndex + 1;
        cmd[mIndex] = 29;
        cmd[var6++] = 107;
        cmd[var6++] = (byte)m;

        int i;
        for(i = 0; i < num; ++i) {
            if(data.charAt(i) > 57 || data.charAt(i) < 48) {
                return;
            }
        }

        if(num <= 30) {
            for(i = 0; i < num; ++i) {
                cmd[var6++] = (byte)data.charAt(i);
            }

            WIFI_Write(cmd);
        }
    }

    public static void EAN13(String data) {
        int m = 2;
        int num = data.length();
        int mIndex = 0;
        byte[] cmd = new byte[1024];
        int var6 = mIndex + 1;
        cmd[mIndex] = 29;
        cmd[var6++] = 107;
        cmd[var6++] = (byte)m;

        int i;
        for(i = 0; i < num; ++i) {
            if(data.charAt(i) > 57 || data.charAt(i) < 48) {
                return;
            }
        }

        if(num <= 30) {
            for(i = 0; i < num; ++i) {
                cmd[var6++] = (byte)data.charAt(i);
            }

            WIFI_Write(cmd);
        }
    }

    public static void EAN8(String data) {
        int m = 3;
        int num = data.length();
        int mIndex = 0;
        byte[] cmd = new byte[1024];
        int var6 = mIndex + 1;
        cmd[mIndex] = 29;
        cmd[var6++] = 107;
        cmd[var6++] = (byte)m;

        int i;
        for(i = 0; i < num; ++i) {
            if(data.charAt(i) > 57 || data.charAt(i) < 48) {
                return;
            }
        }

        if(num <= 30) {
            for(i = 0; i < num; ++i) {
                cmd[var6++] = (byte)data.charAt(i);
            }

            WIFI_Write(cmd);
        }
    }

    public static void CODE39(String data) {
        int m = 4;
        int num = data.length();
        int mIndex = 0;
        byte[] cmd = new byte[1024];
        int var6 = mIndex + 1;
        cmd[mIndex] = 29;
        cmd[var6++] = 107;
        cmd[var6++] = (byte)m;

        int i;
        for(i = 0; i < num; ++i) {
            if(data.charAt(i) > 127 || data.charAt(i) < 32) {
                return;
            }
        }

        if(num <= 30) {
            for(i = 0; i < num; ++i) {
                cmd[var6++] = (byte)data.charAt(i);
            }

            WIFI_Write(cmd);
        }
    }

    public static void ITF(String data) {
        int m = 5;
        int num = data.length();
        int mIndex = 0;
        byte[] cmd = new byte[1024];
        int var6 = mIndex + 1;
        cmd[mIndex] = 29;
        cmd[var6++] = 107;
        cmd[var6++] = (byte)m;

        int i;
        for(i = 0; i < num; ++i) {
            if(data.charAt(i) > 57 || data.charAt(i) < 48) {
                return;
            }
        }

        if(num <= 30) {
            for(i = 0; i < num; ++i) {
                cmd[var6++] = (byte)data.charAt(i);
            }

            WIFI_Write(cmd);
        }
    }

    public static void CODEBAR(String data) {
        int m = 6;
        int num = data.length();
        int mIndex = 0;
        byte[] cmd = new byte[1024];
        int var6 = mIndex + 1;
        cmd[mIndex] = 29;
        cmd[var6++] = 107;
        cmd[var6++] = (byte)m;

        int i;
        for(i = 0; i < num; ++i) {
            if(data.charAt(i) > 127 || data.charAt(i) < 32) {
                return;
            }
        }

        if(num <= 30) {
            for(i = 0; i < num; ++i) {
                cmd[var6++] = (byte)data.charAt(i);
            }

            WIFI_Write(cmd);
        }
    }

    public static void CODE93(String data) {
        int m = 7;
        int num = data.length();
        int mIndex = 0;
        byte[] cmd = new byte[1024];
        int var6 = mIndex + 1;
        cmd[mIndex] = 29;
        cmd[var6++] = 107;
        cmd[var6++] = (byte)m;

        int i;
        for(i = 0; i < num; ++i) {
            if(data.charAt(i) > 127 || data.charAt(i) < 32) {
                return;
            }
        }

        if(num <= 30) {
            for(i = 0; i < num; ++i) {
                cmd[var6++] = (byte)data.charAt(i);
            }

            WIFI_Write(cmd);
        }
    }

    public static void Code128_B(String data) {
        int m = 73;
        int num = data.length();
        int transNum = 0;
        int mIndex = 0;
        byte[] cmd = new byte[1024];
        int var10 = mIndex + 1;
        cmd[mIndex] = 29;
        cmd[var10++] = 107;
        cmd[var10++] = (byte)m;
        int Code128C = var10++;
        cmd[var10++] = 123;
        cmd[var10++] = 66;

        int checkcodeID;
        for(checkcodeID = 0; checkcodeID < num; ++checkcodeID) {
            if(data.charAt(checkcodeID) > 127 || data.charAt(checkcodeID) < 32) {
                return;
            }
        }

        if(num <= 30) {
            for(checkcodeID = 0; checkcodeID < num; ++checkcodeID) {
                cmd[var10++] = (byte)data.charAt(checkcodeID);
                if(data.charAt(checkcodeID) == 123) {
                    cmd[var10++] = (byte)data.charAt(checkcodeID);
                    ++transNum;
                }
            }

            checkcodeID = 104;
            int n = 1;

            for(int i = 0; i < num; ++i) {
                checkcodeID += n++ * (data.charAt(i) - 32);
            }

            checkcodeID %= 103;
            if(checkcodeID >= 0 && checkcodeID <= 95) {
                cmd[var10++] = (byte)(checkcodeID + 32);
                cmd[Code128C] = (byte)(num + 3 + transNum);
            } else if(checkcodeID == 96) {
                cmd[var10++] = 123;
                cmd[var10++] = 51;
                cmd[Code128C] = (byte)(num + 4 + transNum);
            } else if(checkcodeID == 97) {
                cmd[var10++] = 123;
                cmd[var10++] = 50;
                cmd[Code128C] = (byte)(num + 4 + transNum);
            } else if(checkcodeID == 98) {
                cmd[var10++] = 123;
                cmd[var10++] = 83;
                cmd[Code128C] = (byte)(num + 4 + transNum);
            } else if(checkcodeID == 99) {
                cmd[var10++] = 123;
                cmd[var10++] = 67;
                cmd[Code128C] = (byte)(num + 4 + transNum);
            } else if(checkcodeID == 100) {
                cmd[var10++] = 123;
                cmd[var10++] = 52;
                cmd[Code128C] = (byte)(num + 4 + transNum);
            } else if(checkcodeID == 101) {
                cmd[var10++] = 123;
                cmd[var10++] = 65;
                cmd[Code128C] = (byte)(num + 4 + transNum);
            } else if(checkcodeID == 102) {
                cmd[var10++] = 123;
                cmd[var10++] = 49;
                cmd[Code128C] = (byte)(num + 4 + transNum);
            }

            WIFI_Write(cmd);
        }
    }

    public static void printString(String str) {
        try {
            WIFI_Write(str.getBytes("GBK"));
            WIFI_Write(new byte[]{10});
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public static void printParameterSet(byte[] buf) {
        WIFI_Write(buf);
    }

    public static void printByteData(byte[] buf) {
        WIFI_Write(buf);
        WIFI_Write(new byte[]{10});
    }

    public void printImage() {
        printParameterSet(new byte[]{27, 64});
        printParameterSet(new byte[]{27, 33, 0});
        byte[] bufTemp2 = new byte[]{27, 64, 27, 74, 24, 29, 118, 48, 0, 24, 0, -112, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, -64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1, -32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, -1, -16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, -1, -4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, -1, -4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, -1, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, -1, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 63, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 63, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, -1, -64, 127, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -8, -1, -1, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, -1, -1, -1, -1, -1, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, -1, -1, -1, -1, -1, -4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -1, -1, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, -125, -1, -1, -1, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1, 7, -1, -1, -1, -1, -32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, -1, 31, -1, -1, -1, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, -1, -1, -8, -1, -1, -16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, -61, -1, -64, -1, -1, -32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 63, -125, -8, 0, -1, -1, -16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 126, 3, -64, 0, -1, -1, -16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -4, 3, -2, 0, -1, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -8, 3, -1, -32, -1, -1, -4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, -8, 3, -1, -16, -1, -1, -4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, -16, 7, -1, -16, -1, -1, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, -32, 15, -1, -16, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, -32, 15, -1, -16, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, -32, 31, -1, -16, -1, -1, -1, -128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, -64, 63, -1, -16, -1, -1, -1, -128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, -64, 127, -1, -16, -1, -1, -1, -64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, -64, -1, -1, -16, -1, -1, -1, -64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, -29, -1, -1, -16, -1, -1, -1, -32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 63, -1, -1, -1, -16, -1, -1, -1, -32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 63, -1, -1, -1, -16, -1, -1, -1, -32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 63, -1, -1, -1, -16, -1, -1, -1, -16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 63, -1, -1, -1, -16, -1, -1, -1, -16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -1, -16, -1, -1, -1, -16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -1, -16, -1, -1, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -1, -16, -1, -1, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -1, -16, -1, -1, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -1, -16, -1, -1, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -1, -16, -1, -1, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -1, -16, -1, -1, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -1, -16, -1, -1, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -1, -16, -1, -1, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -1, -16, -1, -1, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -1, -16, -1, -1, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -1, -16, -1, -1, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -1, -16, -1, -1, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 63, -1, -1, -1, -16, -1, -1, -1, -16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, -64, 63, -1, -1, -1, -16, -1, -1, -1, -8, 126, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, -1, -16, 63, -1, -1, -1, -16, -1, -1, -1, -1, -1, -128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, -1, -4, 63, -1, -1, -1, -16, -1, -1, -1, -1, -1, -64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, -1, -1, 63, -1, -1, -1, -16, -1, -1, -1, -1, -1, -16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 63, -1, -1, -1, -1, -1, -1, -16, -1, -1, -1, -1, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -1, -1, -1, -1, -16, -1, -1, -1, -1, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -16, -1, -1, -1, -1, -1, -4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -16, -1, -1, -1, -1, -1, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, -1, -1, -1, -1, -1, -1, -16, -1, -1, -1, -1, -1, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, -1, -1, -1, -1, -1, -1, -16, -1, -1, -1, -1, -1, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1, -1, -1, -1, -1, -1, -1, -16, -1, -1, -1, -1, -1, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1, -1, -1, -1, -1, -1, -1, -16, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1, -1, -1, -1, -1, -1, -1, -16, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1, -1, -1, -1, -1, -1, -1, -32, -1, -1, -4, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1, -1, -1, -2, 127, -1, -1, 0, -1, -1, -8, 127, -1, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1, -1, -1, -4, 63, -1, -8, 0, -1, -1, -8, 127, -1, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1, -1, -1, -4, 31, -1, -64, 0, -1, -1, -16, 63, -1, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1, -1, -1, -8, 15, -1, 0, 0, -1, -1, -32, 31, -1, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, -1, -1, -16, 7, -2, 0, 0, -1, -1, -128, 31, -1, -4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, -1, -1, -32, 3, -16, 0, 0, -1, -1, 0, 15, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -32, 0, 0, 0, 0, -1, -2, 0, 3, -1, -16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -64, 15, -2, 0, 0, -1, -4, 0, 1, -1, -64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -128, 127, -1, -64, 0, -1, -16, 0, 0, 31, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 63, -1, -1, -128, -1, -1, -16, 0, -1, -64, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, -1, -1, 3, -1, -1, -8, 0, -1, 1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, -1, -2, 7, -1, -1, -4, 0, 7, -1, -1, -128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, -1, -4, 7, -1, -1, -2, 0, 31, -1, -1, -128, 0, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, -16, 15, -1, -1, -2, 0, 63, -1, -1, -128, 1, -4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, -128, 15, -1, -1, -2, 0, 63, -1, -1, 0, 3, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, -1, -1, -1, 0, 63, -1, -2, 0, 3, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, -1, -1, -1, 0, 63, -1, -128, 0, 3, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, -1, -1, -2, 0, 63, -1, 0, 0, 1, -4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, -1, -1, -2, 0, 63, -1, 0, 0, 0, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, -64, 7, -1, -1, -4, 0, 31, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, -1, -32, 3, -1, -1, -4, 0, 15, -4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, -1, -8, 1, -1, -1, -8, 0, 1, -32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, -1, -8, 0, -1, -1, -32, 0, 0, 0, 0, 7, -16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, -1, -8, 0, 31, -1, -128, 0, 0, 0, 0, 31, -4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, -1, -8, 0, 3, -8, 0, 63, -64, 1, -128, 63, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, -1, -8, 0, 0, 0, 1, -1, -8, 7, -32, 127, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, -1, -8, 0, 0, 0, 3, -1, -4, 15, -16, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, -1, -16, 0, 0, 0, 7, -1, -2, 15, -8, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1, -32, 0, 0, 0, 15, -1, -1, 15, -8, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 15, -1, -1, -113, -16, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, -8, 0, 31, -1, -1, -121, -32, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, -2, 0, 31, -1, -1, -127, -128, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, -1, 0, 31, -1, -1, -128, 0, 127, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 63, -1, -128, 31, -1, -1, -128, 0, 63, -4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 63, -1, -128, 15, -1, -1, -128, 0, 15, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 63, -1, -128, 15, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 63, -1, -128, 7, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 63, -1, 0, 3, -1, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, -1, 0, 1, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, -2, 0, 0, 127, -32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 63, -8, 0, 0, 0, 0, 127, -128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 1, -32, 112, 0, 0, -1, -4, 0, 0, 0, 15, -1, -16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, -125, -16, -8, 0, 3, -4, 30, 0, 0, 0, 127, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, -121, 113, -36, 0, 7, -16, 7, 0, 0, 1, -4, 0, 56, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -50, 51, -116, 0, 15, 96, 3, -128, 0, 3, -32, 0, 28, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -50, 59, 12, 0, 30, 0, 1, -128, 0, 7, -128, 0, 28, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -52, 31, 12, 0, 56, 0, 0, -64, 0, 14, 0, 0, 56, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -36, 30, 12, 0, 120, 0, 0, -64, 0, 12, 0, 1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -8, 30, 12, 0, 112, 0, 0, -64, 0, 12, 0, 1, -32, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, -8, 28, 12, 0, -32, 0, 0, -64, 0, 15, 0, 0, 0, 3, -58, 0, 0, 0, 0, 0, 0, 0, 0, 0, 120, 28, 12, 0, -32, 0, 0, -64, 0, 7, -128, 0, 0, 3, -1, -8, 0, 0, 0, 0, 0, 0, 0, 0, 112, 28, 12, 0, -64, 0, 0, -64, 0, 3, -32, 0, 0, 0, 47, -16, 0, 0, 0, 0, 0, 0, 0, 0, 112, 24, 24, 0, -64, 0, 1, -128, 0, 0, -8, 0, 0, 0, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 112, 24, 24, 1, -64, 0, 1, -128, 0, 0, 63, 0, 0, 0, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 96, 24, 24, 3, -64, 0, 3, -128, -16, 0, 7, -32, 0, 7, -116, 0, -16, 112, 0, 0, 0, 0, 0, 0, 96, 24, 24, -122, -64, 0, 3, 31, -2, 96, 0, -4, 3, -57, -116, 1, -8, -8, 3, 0, 0, 0, 0, 0, 96, 24, 25, -116, -64, 0, 7, 63, -74, 96, 0, 63, 7, -18, -52, 3, -7, -37, 51, 0, 0, 0, 0, 0, 96, 24, 25, -116, -32, 0, 14, 119, -77, -32, 0, 7, -58, 126, -56, 3, -71, -5, 127, -128, 0, 0, 0, 0, -32, 16, 25, -100, -32, 0, 28, -29, -13, -31, 0, 1, -20, 123, -40, 51, 51, -5, -1, 0, 0, 0, 0, 0, -64, 48, 27, 28, 112, 0, 120, -57, -29, -31, -64, 0, 124, 121, -8, 99, 3, 127, -1, 0, 0, 0, 0, 0, -64, 48, 27, 61, -68, 1, -16, -57, -57, -93, -16, 0, 120, -37, -116, 123, 7, -77, -35, 0, 0, 0, 0, 0, -64, 48, 27, -5, -97, -97, -63, -19, -1, -66, 60, 1, -1, -33, 15, -37, -113, -13, -35, -104, 0, 0, 0, 0, -64, 0, 25, -18, 15, -1, 1, -8, -5, 28, 15, -1, -33, -114, 7, -103, -3, -29, -103, -16, 0, 0, 0, 0, -64, 0, 12, 28, 0, 112, 1, -64, 0, 0, 0, -4, 60, 0, 0, 0, 0, 1, 0, -32, 0, 0, 0, 0, 0, 0, 0, 56, 0, 0, 3, 64, 0, 0, 0, 0, 60, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 120, 0, 0, 3, 96, 0, 0, 0, 0, 60, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -8, 0, 0, 3, 96, 0, 0, 0, 0, 124, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -40, 0, 0, 3, 96, 0, 0, 0, 0, 108, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -104, 0, 0, 6, 96, 0, 0, 0, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, -104, 0, 0, 6, 96, 0, 0, 0, 0, 102, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 24, 0, 0, 6, 96, 0, 0, 0, 0, 102, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 56, 0, 0, 6, 96, 0, 0, 0, 0, 102, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 48, 0, 0, 6, 96, 0, 0, 0, 0, 108, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 48, 0, 0, 6, 96, 0, 0, 0, 0, 108, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, -16, 0, 0, 6, -64, 0, 0, 0, 0, 108, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, -32, 0, 0, 7, -64, 0, 0, 0, 0, 124, 0, 0, 0, 0, 0, 0, 0, 0, 10};
        printByteData(bufTemp2);
        printString("");
        printParameterSet(new byte[]{27, 64});
        printParameterSet(new byte[]{27, 97, 0});
    }

    private class ConnectedThread extends Thread {
        public ConnectedThread() {
            Log.d("WifiPrintDriver_Retail", "create ConnectedThread");
        }

        public void run() {
            Log.i("WifiPrintDriver_Retail", "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];

            while(true) {
                try {
                    while(true) {
                        if(WifiPrintDriver_Retail.mWifiInputStream.available() != 0) {
                            BeveragesMenuFragment_Retail.revBytes1 = WifiPrintDriver_Retail.mWifiInputStream.read(buffer);
                            Log.i("WifiPrintDriver_Retail", "revBuffer[0]:" + buffer[0] + "  RTPrinterActivity.revBytes:" + BeveragesMenuFragment_Retail.revBytes1);
                            WifiPrintDriver_Retail.this.mHandler.obtainMessage(2, BeveragesMenuFragment_Retail.revBytes1, -1, buffer).sendToTarget();
                        }
                    }
                } catch (IOException var3) {
                    Log.e("WifiPrintDriver_Retail", "disconnected", var3);
                    return;
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                WifiPrintDriver_Retail.mWifiOutputStream.write(buffer);
                WifiPrintDriver_Retail.this.mHandler.obtainMessage(3, -1, -1, buffer).sendToTarget();
            } catch (IOException var3) {
                Log.e("WifiPrintDriver_Retail", "Exception during write", var3);
            }

        }

        public void write(byte[] buffer, int dataLen) {
            try {
                for(int i = 0; i < dataLen; ++i) {
                    WifiPrintDriver_Retail.mWifiOutputStream.write(buffer[i]);
                }

                WifiPrintDriver_Retail.this.mHandler.obtainMessage(3, -1, -1, buffer).sendToTarget();
            } catch (IOException var4) {
                Log.e("WifiPrintDriver_Retail", "Exception during write", var4);
            }

        }

        public void cancel() {
            try {
                WifiPrintDriver_Retail.mysocket.close();
            } catch (IOException var2) {
                Log.e("WifiPrintDriver_Retail", "close() of connect socket failed", var2);
            }

        }
    }
}
