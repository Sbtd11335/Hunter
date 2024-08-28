package com.cistus.hunter

actual class AppInfo {
    actual companion object {
        actual val appName = BuildConfig.APP_NAME
        actual val version = BuildConfig.VERSION_NAME
        actual val build = BuildConfig.VERSION_CODE.toString()
    }
}