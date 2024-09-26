package com.cistus.hunter

expect class MessageData {
    fun toUnixTime(): Double
    fun toFormattedTime(format: String): String
}