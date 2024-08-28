package com.cistus.hunter

import platform.UIKit.UIDevice

actual class Platform {
    actual companion object {
        actual val os = UIDevice.currentDevice.systemName
        actual val version = UIDevice.currentDevice.systemVersion
    }
}