package com.cistus.hunter

import android.content.Context

actual class LocalDataConfig {
    actual class ToS(context: Context) {
        actual val directoryPath = "${context.dataDir}/ToS"
        actual val dateFilePath = "${directoryPath}/date.txt"
    }
}