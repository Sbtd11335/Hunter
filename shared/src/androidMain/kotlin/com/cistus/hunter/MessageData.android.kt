package com.cistus.hunter

import java.io.Serializable
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.pow

actual class MessageData(val date: String, val message: String, val fromUser: Boolean) {
    actual fun toUnixTime(): Double = date.toDouble() * 10.0.pow(-9)
    actual fun toFormattedTime(format: String): String {
        val formatter = DateTimeFormatter.ofPattern(format).withZone(ZoneId.systemDefault())
        val instant = Instant.ofEpochSecond(toUnixTime().toLong())
        return formatter.format(instant)
    }
}