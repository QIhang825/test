package com.example.weworkclock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.weworkclock.utils.AlarmHelper;
import com.example.weworkclock.utils.PreferencesHelper;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            PreferencesHelper preferencesHelper = new PreferencesHelper(context);
            if (preferencesHelper.isServiceEnabled()) {
                AlarmHelper alarmHelper = new AlarmHelper(context);
                alarmHelper.setAlarms(
                        preferencesHelper.getCheckInHour(),
                        preferencesHelper.getCheckInMinute(),
                        preferencesHelper.getCheckOutHour(),
                        preferencesHelper.getCheckOutMinute()
                );
            }
        }
    }
}
