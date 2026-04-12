# 企微打卡助手

一个基于 Android 无障碍服务的企业微信定时打卡助手应用。

## 功能特性

- 定时上班打卡
- 定时下班打卡
- 手动打卡测试
- 打卡状态通知
- 开机自启动
- 前台服务保活

## 项目结构

```
app/
├── src/main/
│   ├── java/com/example/weworkclock/
│   │   ├── MainActivity.java              # 主界面
│   │   ├── service/
│   │   │   ├── WeWorkAccessibilityService.java  # 无障碍服务（核心打卡逻辑）
│   │   │   └── ForegroundService.java           # 前台服务（保活）
│   │   ├── receiver/
│   │   │   ├── AlarmReceiver.java         # 定时闹钟接收器
│   │   │   └── BootReceiver.java          # 开机启动接收器
│   │   └── utils/
│   │       ├── PreferencesHelper.java     # 配置存储工具
│   │       ├── AlarmHelper.java           # 闹钟管理工具
│   │       └── NotificationHelper.java    # 通知管理工具
│   ├── res/
│   │   ├── layout/
│   │   │   └── activity_main.xml          # 主界面布局
│   │   ├── values/
│   │   │   ├── strings.xml                # 字符串资源
│   │   │   ├── colors.xml                 # 颜色资源
│   │   │   └── themes.xml                 # 主题资源
│   │   └── xml/
│   │       ├── accessibility_service_config.xml  # 无障碍服务配置
│   │       ├── backup_rules.xml
│   │       └── data_extraction_rules.xml
│   └── AndroidManifest.xml                # 应用清单文件
├── build.gradle                           # 应用级 Gradle 配置
└── proguard-rules.pro                     # ProGuard 规则
```

## 使用说明

### 1. 编译安装

使用 Android Studio 打开项目，连接 Android 设备，点击运行按钮编译安装。

### 2. 配置权限

首次打开应用时，需要授予以下权限：

- **悬浮窗权限**：用于在后台显示
- **无障碍服务权限**：核心权限，用于自动操作企业微信

### 3. 启用无障碍服务

1. 点击应用中的「打开无障碍设置」按钮
2. 在系统设置中找到「企微打卡助手」
3. 打开开关并确认授权

### 4. 设置打卡时间

1. 在主界面设置上班打卡时间（默认 09:00）
2. 设置下班打卡时间（默认 18:00）
3. 点击「设置时间」按钮保存

### 5. 手动测试

可以使用「手动上班打卡」和「手动下班打卡」按钮测试功能是否正常。

## 重要提示

### 使用前准备

1. **确保手机位置在公司打卡范围内**
2. **企业微信已登录并保持后台运行**
3. **手机不要设置屏幕锁，或使用智能解锁**
4. **建议将应用加入电池优化白名单**

### 企业微信界面适配

由于企业微信版本不同，界面元素可能变化。如果打卡失败，可能需要调整 [WeWorkAccessibilityService.java](app/src/main/java/com/example/weworkclock/service/WeWorkAccessibilityService.java) 中的文本查找关键词。

当前查找的关键词：
- 「工作台」
- 「打卡」
- 「上班打卡」
- 「下班打卡」
- 「已打卡」

### 关于未解锁状态

Android 系统在未解锁状态下有诸多限制：

1. **建议使用无密码或滑动解锁**
2. **或在设置中启用「智能解锁」**
3. **确保手机电量充足，避免系统进入深度休眠**

## 技术实现

### 核心技术栈

- **无障碍服务 (AccessibilityService)**：用于自动查找和点击 UI 元素
- **AlarmManager**：用于设置定时任务
- **前台服务 (ForegroundService)**：用于应用保活
- **SharedPreferences**：用于存储配置信息

### 工作流程

1. 用户设置打卡时间
2. AlarmManager 设置每日重复闹钟
3. 闹钟触发时，AlarmReceiver 接收广播
4. 启动无障碍服务
5. 无障碍服务打开企业微信
6. 自动查找并点击「工作台」→「打卡」→「上班/下班打卡」
7. 发送打卡结果通知

## 注意事项

⚠️ **免责声明**：本应用仅供学习交流使用，请勿用于违反公司规定的行为。使用本应用所产生的一切后果由使用者自行承担。

## 开发环境

- Android Studio Hedgehog | 2023.1.1+
- minSdk: 26 (Android 8.0)
- targetSdk: 34 (Android 14)
- Gradle: 8.0
- JDK: 8+

## 许可证

MIT License
