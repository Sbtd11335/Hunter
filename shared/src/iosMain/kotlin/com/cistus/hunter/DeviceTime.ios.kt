package com.cistus.hunter

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSinceReferenceDate

actual class DeviceTime {
    actual companion object {
        actual fun nanoTime(): Double = NSDate.timeIntervalSinceReferenceDate
    }
}