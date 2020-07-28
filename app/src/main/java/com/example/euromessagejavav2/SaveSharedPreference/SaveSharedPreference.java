package com.example.euromessagejavav2.SaveSharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    static final String PREF_USERNAME = "username";

    static SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setUserName(Context context, String userName){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USERNAME, userName);
        editor.apply();
    }

    public static void setToken(Context context, String token){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("token", token);
        editor.apply();
    }

    public static String getUserName(Context context){
        return getSharedPreferences(context).getString(PREF_USERNAME, "");
    }

    public static String getToken(Context context){
        return getSharedPreferences(context).getString("token", "");
    }

    public static void clearUserName(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }
}
