package com.example.weworkclock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.weworkclock.service.WeWorkAccessibilityService;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String ACTION_CHECK_IN = "com.example.weworkclock.ACTION_CHECK_IN";
    public static final String ACTION_CHECK_OUT = "com.example.weworkclock.ACTION_CHECK_OUT";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) return;

        WeWorkAccessibilityService service = WeWorkAccessibilityService.getInstance();
        if (service == null) return;

        if (ACTION_CHECK_IN.equals(action)) {
            service.startCheckIn();
        } else if (ACTION_CHECK_OUT.equals(action)) {
            service.startCheckOut();
        }
    }
}
