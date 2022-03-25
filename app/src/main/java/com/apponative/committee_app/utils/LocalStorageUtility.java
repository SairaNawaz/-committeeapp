package com.apponative.committee_app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Muhammad Waqas on 6/2/2017.
 */

public class LocalStorageUtility {

    /**
     * Save data in shared preference
     * @param context
     * @param Key
     * @param Value
     */
    public static void saveToSharedPref(Context context, String Key, String Value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(Key,Value).commit();
    }

    public static int saveIntToSharedPref(Context context,String Key, int Value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putInt(Key,Value).commit();
        return Value;
    }
    /**
     * Save data in shared preference
     * @param context
     * @param Key
     */
    public static void saveBoolToSharedPref(Context context,String Key, boolean Value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(Key,Value).commit();
    }

    /**
     * Get data from shared preference
     * @param context
     * @param Key
     */
    public static String getFromSharedPref(Context context,String Key) {
        SharedPreferences manger = PreferenceManager.getDefaultSharedPreferences(context);
        return manger.getString(Key, null);
    }

    public static int getIntFromSharedPref(Context context,String Key) {
        SharedPreferences manger = PreferenceManager.getDefaultSharedPreferences(context);
        return manger.getInt(Key, 0);
    }
    /**
     * Get data from shared preference
     * @param context
     * @param Key
     */
    public static boolean getBooleanFromSharedPref(Context context,String Key) {
        SharedPreferences manger = PreferenceManager.getDefaultSharedPreferences(context);
        return manger.getBoolean(Key, false);
    }
}
