package com.example.weworkclock.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.weworkclock.receiver.AlarmReceiver;

import java.util.Calendar;

public class AlarmHelper {
    private static final int CHECK_IN_REQUEST_CODE = 1001;
    private static final int CHECK_OUT_REQUEST_CODE = 1002;

    private Context context;
    private AlarmManager alarmManager;

    public AlarmHelper(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarms(int checkInHour, int checkInMinute, int checkOutHour, int checkOutMinute) {
        cancelAlarms();
        setCheckInAlarm(checkInHour, checkInMinute);
        setCheckOutAlarm(checkOutHour, checkOutMinute);
    }

    public void cancelAlarms() {
        cancelCheckInAlarm();
        cancelCheckOutAlarm();
    }

    private void setCheckInAlarm(int hour, int minute) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(AlarmReceiver.ACTION_CHECK_IN);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, CHECK_IN_REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
    }

    private void setCheckOutAlarm(int hour, int minute) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(AlarmReceiver.ACTION_CHECK_OUT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, CHECK_OUT_REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
    }

    private void cancelCheckInAlarm() {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(AlarmReceiver.ACTION_CHECK_IN);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, CHECK_IN_REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
    }

    private void cancelCheckOutAlarm() {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(AlarmReceiver.ACTION_CHECK_OUT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, CHECK_OUT_REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
    }
}
