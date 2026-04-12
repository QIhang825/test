package com.example.weworkclock.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.weworkclock.utils.NotificationHelper;

import java.util.List;

public class WeWorkAccessibilityService extends AccessibilityService {
    private static final String WEWORK_PACKAGE = "com.tencent.wework";
    private static WeWorkAccessibilityService instance;
    private NotificationHelper notificationHelper;
    private boolean isPerformingCheckIn = false;
    private boolean isPerformingCheckOut = false;

    public static WeWorkAccessibilityService getInstance() {
        return instance;
    }

    public static boolean isServiceRunning() {
        return instance != null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        notificationHelper = new NotificationHelper(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        instance = this;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getPackageName() == null) return;

        String packageName = event.getPackageName().toString();
        if (!WEWORK_PACKAGE.equals(packageName)) return;

        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode == null) return;

        if (isPerformingCheckIn) {
            performCheckInFlow(rootNode);
        } else if (isPerformingCheckOut) {
            performCheckOutFlow(rootNode);
        }
    }

    @Override
    public void onInterrupt() {
    }

    public void startCheckIn() {
        isPerformingCheckIn = true;
        isPerformingCheckOut = false;
        openWeWork();
    }

    public void startCheckOut() {
        isPerformingCheckIn = false;
        isPerformingCheckOut = true;
        openWeWork();
    }

    private void openWeWork() {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(WEWORK_PACKAGE);
        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(launchIntent);
        }
    }

    private void performCheckInFlow(AccessibilityNodeInfo rootNode) {
        try {
            Thread.sleep(1000);

            AccessibilityNodeInfo workbenchNode = findNodeByText(rootNode, "工作台");
            if (workbenchNode != null) {
                performClick(workbenchNode);
                Thread.sleep(1500);
            }

            AccessibilityNodeInfo checkInNode = findNodeByText(rootNode, "打卡");
            if (checkInNode != null) {
                performClick(checkInNode);
                Thread.sleep(2000);
            }

            AccessibilityNodeInfo checkInBtnNode = findNodeByText(rootNode, "上班打卡");
            if (checkInBtnNode != null) {
                performClick(checkInBtnNode);
                Thread.sleep(1000);
                notificationHelper.showClockNotification(true, "上班打卡成功！");
                resetFlags();
                return;
            }

            AccessibilityNodeInfo alreadyCheckedNode = findNodeByText(rootNode, "已打卡");
            if (alreadyCheckedNode != null) {
                notificationHelper.showClockNotification(true, "今日已上班打卡！");
                resetFlags();
                return;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            notificationHelper.showClockNotification(false, "打卡过程被中断");
        }
    }

    private void performCheckOutFlow(AccessibilityNodeInfo rootNode) {
        try {
            Thread.sleep(1000);

            AccessibilityNodeInfo workbenchNode = findNodeByText(rootNode, "工作台");
            if (workbenchNode != null) {
                performClick(workbenchNode);
                Thread.sleep(1500);
            }

            AccessibilityNodeInfo checkInNode = findNodeByText(rootNode, "打卡");
            if (checkInNode != null) {
                performClick(checkInNode);
                Thread.sleep(2000);
            }

            AccessibilityNodeInfo checkOutBtnNode = findNodeByText(rootNode, "下班打卡");
            if (checkOutBtnNode != null) {
                performClick(checkOutBtnNode);
                Thread.sleep(1000);
                notificationHelper.showClockNotification(true, "下班打卡成功！");
                resetFlags();
                return;
            }

            AccessibilityNodeInfo alreadyCheckedNode = findNodeByText(rootNode, "已打卡");
            if (alreadyCheckedNode != null) {
                notificationHelper.showClockNotification(true, "今日已下班打卡！");
                resetFlags();
                return;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            notificationHelper.showClockNotification(false, "打卡过程被中断");
        }
    }

    private AccessibilityNodeInfo findNodeByText(AccessibilityNodeInfo rootNode, String text) {
        if (rootNode == null) return null;

        List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByText(text);
        if (nodes != null && !nodes.isEmpty()) {
            return nodes.get(0);
        }

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            AccessibilityNodeInfo child = rootNode.getChild(i);
            AccessibilityNodeInfo result = findNodeByText(child, text);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    private void performClick(AccessibilityNodeInfo node) {
        if (node == null) return;

        AccessibilityNodeInfo clickableNode = node;
        while (clickableNode != null && !clickableNode.isClickable()) {
            clickableNode = clickableNode.getParent();
        }

        if (clickableNode != null) {
            clickableNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    private void resetFlags() {
        isPerformingCheckIn = false;
        isPerformingCheckOut = false;
    }
}
