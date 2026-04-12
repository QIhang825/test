# 企微打卡助手 - 使用 Android Studio 构建 APK 指南

## 方法一：使用 Android Studio 构建（推荐）

### 1. 准备工作
- 下载并安装 [Android Studio](https://developer.android.com/studio)
- 确保电脑上已安装 JDK 8 或更高版本

### 2. 打开项目
1. 启动 Android Studio
2. 选择 `File` → `Open`
3. 选择项目根目录 `/workspace` 或您存放项目的目录
4. 等待 Gradle 同步完成（首次同步可能需要下载依赖，请耐心等待）

### 3. 配置国内镜像（可选，如果网络慢）
如果您在中国内地，建议配置镜像源加速依赖下载：

**步骤 1：修改项目的 build.gradle**
打开 `build.gradle`（项目根目录下的），将仓库源替换为：
```gradle
buildscript {
    repositories {
        maven { url 'https://maven.aliyun.com/repository/google' }
        maven { url 'https://maven.aliyun.com/repository/central' }
        maven { url 'https://maven.aliyun.com/repository/public' }
        maven { url 'https://maven.aliyun.com/repository/gradle-plugin' }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:8.1.0'
    }
}

allprojects {
    repositories {
        maven { url 'https://maven.aliyun.com/repository/google' }
        maven { url 'https://maven.aliyun.com/repository/central' }
        maven { url 'https://maven.aliyun.com/repository/public' }
    }
}
```

**步骤 2：修改 settings.gradle**
打开 `settings.gradle`，修改为：
```gradle
pluginManagement {
    repositories {
        maven { url 'https://maven.aliyun.com/repository/google' }
        maven { url 'https://maven.aliyun.com/repository/central' }
        maven { url 'https://maven.aliyun.com/repository/gradle-plugin' }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url 'https://maven.aliyun.com/repository/google' }
        maven { url 'https://maven.aliyun.com/repository/central' }
        maven { url 'https://maven.aliyun.com/repository/public' }
    }
}

rootProject.name = "WeWorkClock"
include ':app'
```

**步骤 3：点击 Sync 按钮重新同步项目**

### 4. 构建 Debug APK
1. 在 Android Studio 顶部工具栏，选择 Build Variants（通常在左下角）
2. 确保选择了 `debug` 构建变体
3. 点击菜单 `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`
4. 等待构建完成，右下角会显示通知
5. 点击通知中的 `locate` 可以找到生成的 APK 文件

APK 文件位置：
```
app/build/outputs/apk/debug/app-debug.apk
```

### 5. 构建 Release APK（可选）
如果您需要构建发布版本：
1. 点击菜单 `Build` → `Generate Signed Bundle / APK`
2. 选择 `APK`，点击 `Next`
3. 如果是第一次，点击 `Create new...` 创建密钥库
4. 填写密钥库信息并保存
5. 选择 `release` 构建类型
6. 点击 `Finish` 开始构建

## 方法二：使用命令行构建（需要配置环境）

如果您已经配置好了 Android SDK 和 Gradle 环境：

### 1. 配置环境变量
确保以下环境变量已设置：
- `ANDROID_HOME`：指向 Android SDK 目录
- 将 `$ANDROID_HOME/platform-tools` 和 `$ANDROID_HOME/tools` 添加到 PATH

### 2. 构建 Debug APK
在项目根目录执行：
```bash
# Windows
gradlew.bat assembleDebug

# Linux/Mac
./gradlew assembleDebug
```

### 3. 构建 Release APK
```bash
# Windows
gradlew.bat assembleRelease

# Linux/Mac
./gradlew assembleRelease
```

## 安装 APK 到手机

### 方法 1：通过 USB 安装
1. 在手机上启用「开发者选项」和「USB 调试」
2. 用 USB 数据线连接手机和电脑
3. 在手机上允许 USB 调试
4. 执行命令：
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

### 方法 2：直接传输 APK
1. 将 `app-debug.apk` 文件复制到手机
2. 在手机上找到该文件
3. 点击安装（需要允许安装未知来源应用）

## 常见问题

### Q: Gradle 同步失败怎么办？
A: 
1. 检查网络连接
2. 配置国内镜像源（见上文）
3. 尝试 `File` → `Invalidate Caches / Restart`

### Q: 构建时提示 SDK 未安装？
A: 
1. 打开 `Tools` → `SDK Manager`
2. 安装 Android 14.0 (API 34) SDK Platform
3. 安装 Android SDK Build-Tools 34.x

### Q: 安装 APK 时提示解析错误？
A: 
1. 确认手机 Android 版本 ≥ 8.0 (API 26)
2. 重新构建 APK
3. 检查 APK 文件是否完整

## 使用应用

安装完成后，请参考 [README.md](README.md) 中的使用说明配置和使用应用。