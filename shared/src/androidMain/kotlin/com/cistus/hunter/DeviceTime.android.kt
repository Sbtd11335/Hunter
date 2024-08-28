package com.cistus.hunter

import kotlin.math.pow

actual class DeviceTime {
    actual companion object {
        actual fun nanoTime(): Double = System.nanoTime() * 10.0.pow(-9)
    }
}