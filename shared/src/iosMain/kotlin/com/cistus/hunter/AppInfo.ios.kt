package com.cistus.hunter

import platform.Foundation.NSBundle

actual class AppInfo {
    actual companion object {
        actual val appName = NSBundle.mainBundle.infoDictionary?.get("CFBundleDisplayName").toString()
        actual val version = NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString").toString()
        actual val build = NSBundle.mainBundle.infoDictionary?.get("CFBundleVersion").toString()
    }
}