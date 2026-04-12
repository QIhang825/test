package com.example.weworkclock.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.weworkclock.utils.NotificationHelper;

public class ForegroundService extends Service {
    private static final int NOTIFICATION_ID = 1001;
    private NotificationHelper notificationHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationHelper = new NotificationHelper(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = notificationHelper.getForegroundNotification();
        startForeground(NOTIFICATION_ID, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
