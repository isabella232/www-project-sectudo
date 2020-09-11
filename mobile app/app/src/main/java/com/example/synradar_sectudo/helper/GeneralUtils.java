package com.example.synradar_sectudo.helper;

import android.content.SharedPreferences;

public class GeneralUtils {

    public static final String SHARED_PREFS = "sharedPrefs";

    public static void saveSharedPrefs(SharedPreferences prefs, String key, String value){
        SharedPreferences.Editor editor =prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String readSharedPrefs(SharedPreferences prefs, String key){

        String value = prefs.getString(key, "");
        return value;

    }

}