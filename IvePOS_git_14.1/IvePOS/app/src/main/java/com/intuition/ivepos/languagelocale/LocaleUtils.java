package com.intuition.ivepos.languagelocale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import com.intuition.ivepos.mSwipe.ApplicationData;

import java.util.Locale;

public class LocaleUtils {

    private static final String PREF_NAME = "language_resource";

    public static void initialize(Context context, String defaultLanguage) {
        setLocale(context, defaultLanguage);
    }

    public static void setLocale(Context context, String language) {
        updateResources(context, language);
    }

    private static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        context.createConfigurationContext(configuration);

        if (Build.VERSION.SDK_INT >= 17) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
//        configuration.locale = locale;
//        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    private static SharedPreferences getDefaultSharedPreference(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void setSelectedLanguageId(String languageId, String language, Context context) {
        final SharedPreferences prefs = getDefaultSharedPreference(context);
        if (prefs != null) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("app_language_id", languageId);
            editor.putString("language", language);
            editor.apply();
        }
    }

    public static String getSelectedLanguageId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("app_language_id", "en");
    }

    public static String getSelectedLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("language", "English");
    }
}


