package com.intuition.ivepos;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {

    private static final String PREF_NAME = "configResource";
    private static final String KEY_VALUE = "configValue";

    private static final String KEY_DATA_DOWNLOAD = "dataDownload";

    private static SharedPreferenceManager sInstance;
    private final SharedPreferences mPref;

    private SharedPreferenceManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SharedPreferenceManager(context);
        }
    }

    public static synchronized SharedPreferenceManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(SharedPreferenceManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    public void setValue(int value) {
        mPref.edit()
                .putInt(KEY_VALUE, value)
                .apply();
    }

    public void setValue(String value) {
        mPref.edit()
                .putString(KEY_VALUE, value)
                .apply();
    }

    public int getValue() {
        return mPref.getInt(KEY_VALUE, 0);
    }


    public void setDataDownloadValue(boolean value){
        mPref.edit()
                .putBoolean(KEY_DATA_DOWNLOAD, value)
                .apply();

    }

    public boolean getDataDownloadValue(){
        return mPref.getBoolean(KEY_DATA_DOWNLOAD, false);
    }

    public void remove(String key) {
        mPref.edit()
                .remove(key)
                .apply();
    }

    public boolean clear() {
        return mPref.edit()
                .clear()
                .commit();
    }
}
