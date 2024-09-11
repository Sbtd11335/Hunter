package com.cistus.hunter

import android.content.Context

actual class LocalDataConfig {
    actual class ToS(context: Context) {
        actual val directoryPath = "${context.cacheDir}/ToS"
        actual val dateFilePath = "${directoryPath}/date.txt"
    }
}