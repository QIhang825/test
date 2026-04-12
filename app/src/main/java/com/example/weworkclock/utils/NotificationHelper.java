package com.example.weworkclock.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.weworkclock.MainActivity;
import com.example.weworkclock.R;

public class NotificationHelper {
    private static final String CHANNEL_ID = "wework_clock_channel";
    private static final int NOTIFICATION_ID = 1;
    private static final int FOREGROUND_NOTIFICATION_ID = 2;

    private Context context;
    private NotificationManager notificationManager;

    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.notification_channel_name);
            String description = context.getString(R.string.notification_channel_desc);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showClockNotification(boolean success, String message) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(success ? "打卡成功" : "打卡失败")
                .setContentText(message)
                .setPriority(success ? NotificationCompat.PRIORITY_DEFAULT : NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public Notification getForegroundNotification() {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(context.getString(R.string.foreground_service_title))
                .setContentText(context.getString(R.string.foreground_service_text))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
    }

    public void cancelAllNotifications() {
        notificationManager.cancelAll();
    }
}
