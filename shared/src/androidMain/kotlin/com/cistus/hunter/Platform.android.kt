package com.cistus.hunter

import android.os.Build

actual class Platform {
    actual companion object {
        actual val os = "Android"
        actual val version = Build.VERSION.SDK_INT.toString()
    }
}