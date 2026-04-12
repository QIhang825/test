package com.example.weworkclock;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weworkclock.service.ForegroundService;
import com.example.weworkclock.service.WeWorkAccessibilityService;
import com.example.weworkclock.utils.AlarmHelper;
import com.example.weworkclock.utils.PreferencesHelper;

public class MainActivity extends AppCompatActivity {

    private TextView serviceStatus;
    private Button openAccessibilityBtn;
    private TimePicker checkInTimePicker;
    private TimePicker checkOutTimePicker;
    private Button saveTimeBtn;
    private Button manualCheckInBtn;
    private Button manualCheckOutBtn;

    private PreferencesHelper preferencesHelper;
    private AlarmHelper alarmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initUtils();
        setupListeners();
        loadSavedTimes();
        requestOverlayPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateServiceStatus();
    }

    private void initViews() {
        serviceStatus = findViewById(R.id.serviceStatus);
        openAccessibilityBtn = findViewById(R.id.openAccessibilityBtn);
        checkInTimePicker = findViewById(R.id.checkInTimePicker);
        checkOutTimePicker = findViewById(R.id.checkOutTimePicker);
        saveTimeBtn = findViewById(R.id.saveTimeBtn);
        manualCheckInBtn = findViewById(R.id.manualCheckInBtn);
        manualCheckOutBtn = findViewById(R.id.manualCheckOutBtn);

        checkInTimePicker.setIs24HourView(true);
        checkOutTimePicker.setIs24HourView(true);
    }

    private void initUtils() {
        preferencesHelper = new PreferencesHelper(this);
        alarmHelper = new AlarmHelper(this);
    }

    private void setupListeners() {
        openAccessibilityBtn.setOnClickListener(v -> openAccessibilitySettings());

        saveTimeBtn.setOnClickListener(v -> saveTimeSettings());

        manualCheckInBtn.setOnClickListener(v -> performManualCheckIn());

        manualCheckOutBtn.setOnClickListener(v -> performManualCheckOut());
    }

    private void loadSavedTimes() {
        int checkInHour = preferencesHelper.getCheckInHour();
        int checkInMinute = preferencesHelper.getCheckInMinute();
        int checkOutHour = preferencesHelper.getCheckOutHour();
        int checkOutMinute = preferencesHelper.getCheckOutMinute();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkInTimePicker.setHour(checkInHour);
            checkInTimePicker.setMinute(checkInMinute);
            checkOutTimePicker.setHour(checkOutHour);
            checkOutTimePicker.setMinute(checkOutMinute);
        } else {
            checkInTimePicker.setCurrentHour(checkInHour);
            checkInTimePicker.setCurrentMinute(checkInMinute);
            checkOutTimePicker.setCurrentHour(checkOutHour);
            checkOutTimePicker.setCurrentMinute(checkOutMinute);
        }
    }

    private void updateServiceStatus() {
        if (WeWorkAccessibilityService.isServiceRunning()) {
            serviceStatus.setText("无障碍服务已启用");
            serviceStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            serviceStatus.setText(R.string.service_not_enabled);
            serviceStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    private void openAccessibilitySettings() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }

    private void saveTimeSettings() {
        int checkInHour, checkInMinute, checkOutHour, checkOutMinute;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkInHour = checkInTimePicker.getHour();
            checkInMinute = checkInTimePicker.getMinute();
            checkOutHour = checkOutTimePicker.getHour();
            checkOutMinute = checkOutTimePicker.getMinute();
        } else {
            checkInHour = checkInTimePicker.getCurrentHour();
            checkInMinute = checkInTimePicker.getCurrentMinute();
            checkOutHour = checkOutTimePicker.getCurrentHour();
            checkOutMinute = checkOutTimePicker.getCurrentMinute();
        }

        preferencesHelper.saveCheckInTime(checkInHour, checkInMinute);
        preferencesHelper.saveCheckOutTime(checkOutHour, checkOutMinute);
        preferencesHelper.setServiceEnabled(true);

        alarmHelper.setAlarms(checkInHour, checkInMinute, checkOutHour, checkOutMinute);
        startForegroundService();

        Toast.makeText(this, "时间设置已保存！", Toast.LENGTH_SHORT).show();
    }

    private void performManualCheckIn() {
        if (!WeWorkAccessibilityService.isServiceRunning()) {
            Toast.makeText(this, "请先启用无障碍服务！", Toast.LENGTH_SHORT).show();
            return;
        }

        WeWorkAccessibilityService service = WeWorkAccessibilityService.getInstance();
        if (service != null) {
            service.startCheckIn();
        }
    }

    private void performManualCheckOut() {
        if (!WeWorkAccessibilityService.isServiceRunning()) {
            Toast.makeText(this, "请先启用无障碍服务！", Toast.LENGTH_SHORT).show();
            return;
        }

        WeWorkAccessibilityService service = WeWorkAccessibilityService.getInstance();
        if (service != null) {
            service.startCheckOut();
        }
    }

    private void startForegroundService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1000);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "悬浮窗权限已获取", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
