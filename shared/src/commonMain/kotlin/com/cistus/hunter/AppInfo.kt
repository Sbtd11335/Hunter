package com.cistus.hunter

expect class AppInfo {
    companion object {
        val appName: String
        val version: String
        val build: String
    }
}