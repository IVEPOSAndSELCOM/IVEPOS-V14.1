package com.intuition.ivepos;

public interface RecvCallBack {
    public void onRecv(byte[] buffer, int byteOffset, int byteCount);
}
