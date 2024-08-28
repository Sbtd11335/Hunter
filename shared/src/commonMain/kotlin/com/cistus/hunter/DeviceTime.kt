package com.cistus.hunter

expect class DeviceTime {
    companion object {
        fun nanoTime(): Double
    }
}