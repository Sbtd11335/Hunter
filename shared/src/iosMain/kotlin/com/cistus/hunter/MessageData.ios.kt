package com.cistus.hunter

import kotlinx.cinterop.BetaInteropApi
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.create
import kotlin.math.pow

actual class MessageData(val date: String, val message: String, val fromUser: Boolean) {
    actual fun toUnixTime(): Double = date.toDouble() * 10.0.pow(-9)
    @OptIn(BetaInteropApi::class)
    actual fun toFormattedTime(format: String): String {
        val formatter = NSDateFormatter()
        val time = NSDate.create(timeIntervalSince1970 = toUnixTime())
        formatter.dateFormat = format
        return formatter.stringFromDate(time)
    }

}