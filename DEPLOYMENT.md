# 企微打卡助手 - 部署上线指南

## 1. 准备发布构建

### 1.1 更新版本信息
首先，更新 [app/build.gradle](file:///workspace/app/build.gradle#L13-L14) 中的版本信息：

```gradle
defaultConfig {
    versionCode 2          // 每次发布递增
    versionName "1.1"      // 显示给用户的版本号
}
```

### 1.2 配置签名密钥

#### 1.2.1 生成签名密钥库
在项目根目录下创建密钥库：

```bash
keytool -genkey -v -keystore release-key.keystore -alias release-key-alias -keyalg RSA -keysize 2048 -validity 10000
```

按照提示输入密钥库密码、别名密码等信息。

#### 1.2.2 在 build.gradle 中配置签名
修改 [app/build.gradle](file:///workspace/app/build.gradle)，添加签名配置：

```gradle
android {
    signingConfigs {
        release {
            storeFile file("../release-key.keystore")
            storePassword "your-store-password"
            keyAlias "release-key-alias"
            keyPassword "your-key-password"
        }
    }
    
    buildTypes {
        release {
            minifyEnabled true                    // 启用代码混淆
            shrinkResources true                  // 启用资源压缩
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release  // 使用发布签名
        }
    }
}
```

**注意**：为了安全，建议使用 `local.properties` 文件存储密码，而不是直接写在 build.gradle 中。

### 1.3 优化配置

#### 启用代码混淆
确保 [app/proguard-rules.pro](file:///workspace/app/proguard-rules.pro) 配置正确。当前项目可能需要添加以下规则：

```proguard
# 保持 AndroidX 组件
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

# 保持应用中的四大组件
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# 保持无障碍服务相关
-keep class com.example.weworkclock.service.** { *; }
```

## 2. 构建发布版本

### 2.1 使用 Gradle 命令行构建
在项目根目录执行：

```bash
# 构建 Release APK
./gradlew assembleRelease

# 构建 App Bundle（推荐用于 Google Play）
./gradlew bundleRelease
```

构建产物位置：
- APK: `app/build/outputs/apk/release/app-release.apk`
- AAB: `app/build/outputs/bundle/release/app-release.aab`

### 2.2 使用 Android Studio 构建
1. 打开 Android Studio
2. 选择 `Build` → `Generate Signed Bundle/APK`
3. 选择 `APK` 或 `Android App Bundle`
4. 选择签名密钥库并填写密码
5. 选择 `release` 构建类型
6. 点击 `Finish`

## 3. 测试发布版本

### 3.1 安装测试
```bash
adb install app/build/outputs/apk/release/app-release.apk
```

### 3.2 功能测试清单
- [ ] 无障碍服务是否正常工作
- [ ] 定时闹钟是否准时触发
- [ ] 打卡功能是否正常
- [ ] 通知是否正常显示
- [ ] 前台服务是否稳定运行
- [ ] 开机自启动是否生效
- [ ] 数据持久化是否正常

## 4. 应用商店发布

### 4.1 应用图标和截图准备
- 应用图标：512x512 PNG
- 启动图标：mipmap-hdpi/xhdpi/xxhdpi/xxxhdpi
- 截图：至少 3-5 张应用界面截图
- 特性图：1024x500（部分商店需要）

### 4.2 主要应用商店发布流程

#### Google Play Store
1. 注册 Google Play 开发者账号（$25 一次性费用）
2. 访问 https://play.google.com/console
3. 创建应用，填写应用信息
4. 上传 AAB 文件
5. 填写应用描述、截图等
6. 设置应用内容分级
7. 选择发布国家/地区
8. 提交审核

#### 华为应用市场
1. 注册华为开发者账号
2. 访问 https://developer.huawei.com/consumer/cn/
3. 创建应用，获取 AppGallery Connect 配置
4. 上传 APK
5. 完善应用信息
6. 提交审核

#### 腾讯应用宝
1. 注册腾讯开放平台账号
2. 访问 https://open.tencent.com/
3. 创建应用，提交资质审核
4. 上传 APK
5. 完善应用信息
6. 提交审核

#### 小米应用商店
1. 注册小米开发者账号
2. 访问 https://dev.mi.com/distribute
3. 创建应用
4. 上传 APK
5. 完善应用信息
6. 提交审核

#### OPPO 软件商店
1. 注册 OPPO 开发者账号
2. 访问 https://open.oppomobile.com/
3. 创建应用
4. 上传 APK
5. 完善应用信息
6. 提交审核

#### vivo 应用商店
1. 注册 vivo 开发者账号
2. 访问 https://dev.vivo.com.cn/
3. 创建应用
4. 上传 APK
5. 完善应用信息
6. 提交审核

### 4.3 应用信息准备清单
- **应用名称**：企微打卡助手
- **应用包名**：com.example.weworkclock
- **应用简介**：一款基于 Android 无障碍服务的企业微信定时打卡助手
- **详细描述**：
  - 定时上班打卡
  - 定时下班打卡
  - 手动打卡测试
  - 打卡状态通知
  - 开机自启动
  - 前台服务保活
- **更新日志**：本次更新的内容
- **关键词**：企业微信、打卡、考勤、自动化、定时
- **隐私政策链接**：需要准备隐私政策页面
- **服务条款链接**：（可选）
- **开发者邮箱**：用于用户反馈
- **应用分类**：工具类/效率类
- **内容分级**：适合所有年龄段

## 5. 发布后的维护

### 5.1 版本更新流程
1. 修改代码和功能
2. 更新版本号（versionCode 和 versionName）
3. 测试新功能
4. 构建发布版本
5. 准备更新日志
6. 提交到应用商店

### 5.2 问题跟踪
- 设置用户反馈渠道
- 收集崩溃日志（可集成 Firebase Crashlytics 或 Bugly）
- 定期分析用户反馈

### 5.3 性能监控
- 监控应用启动时间
- 监控内存使用情况
- 监控电量消耗

## 6. 注意事项

### 6.1 合规性
- 确保应用遵守各应用商店的政策
- 准备完善的隐私政策
- 明确应用的使用目的
- 注意免责声明

### 6.2 安全性
- 妥善保管签名密钥库
- 不要将密钥库提交到版本控制
- 定期更新依赖库，修复安全漏洞

### 6.3 用户体验
- 确保应用稳定运行
- 提供清晰的使用说明
- 及时响应用户反馈
- 持续优化功能

## 7. 快速部署脚本

创建一个自动化部署脚本 `deploy.sh`：

```bash
#!/bin/bash

# 部署脚本
echo "开始构建发布版本..."

# 清理旧构建
./gradlew clean

# 构建 Release APK
./gradlew assembleRelease

if [ $? -eq 0 ]; then
    echo "构建成功！"
    echo "APK 位置: app/build/outputs/apk/release/app-release.apk"
    
    # 显示 APK 信息
    echo ""
    echo "APK 信息："
    ls -lh app/build/outputs/apk/release/
else
    echo "构建失败！"
    exit 1
fi
```

使用方法：
```bash
chmod +x deploy.sh
./deploy.sh
```

## 总结

部署上线是一个多步骤的过程，重点关注：
1. 正确配置签名密钥
2. 充分测试发布版本
3. 准备完善的应用商店素材
4. 遵守各平台的政策规定
5. 建立发布后的维护机制

按照本指南操作，您可以顺利将企微打卡助手部署到各大应用商店！