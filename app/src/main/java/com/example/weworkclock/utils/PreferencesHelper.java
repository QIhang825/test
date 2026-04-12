package com.example.weworkclock.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {
    private static final String PREF_NAME = "WeWorkClockPrefs";
    private static final String KEY_CHECK_IN_HOUR = "check_in_hour";
    private static final String KEY_CHECK_IN_MINUTE = "check_in_minute";
    private static final String KEY_CHECK_OUT_HOUR = "check_out_hour";
    private static final String KEY_CHECK_OUT_MINUTE = "check_out_minute";
    private static final String KEY_SERVICE_ENABLED = "service_enabled";

    private SharedPreferences sharedPreferences;

    public PreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveCheckInTime(int hour, int minute) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_CHECK_IN_HOUR, hour);
        editor.putInt(KEY_CHECK_IN_MINUTE, minute);
        editor.apply();
    }

    public void saveCheckOutTime(int hour, int minute) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_CHECK_OUT_HOUR, hour);
        editor.putInt(KEY_CHECK_OUT_MINUTE, minute);
        editor.apply();
    }

    public int getCheckInHour() {
        return sharedPreferences.getInt(KEY_CHECK_IN_HOUR, 9);
    }

    public int getCheckInMinute() {
        return sharedPreferences.getInt(KEY_CHECK_IN_MINUTE, 0);
    }

    public int getCheckOutHour() {
        return sharedPreferences.getInt(KEY_CHECK_OUT_HOUR, 18);
    }

    public int getCheckOutMinute() {
        return sharedPreferences.getInt(KEY_CHECK_OUT_MINUTE, 0);
    }

    public void setServiceEnabled(boolean enabled) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_SERVICE_ENABLED, enabled);
        editor.apply();
    }

    public boolean isServiceEnabled() {
        return sharedPreferences.getBoolean(KEY_SERVICE_ENABLED, false);
    }
}
