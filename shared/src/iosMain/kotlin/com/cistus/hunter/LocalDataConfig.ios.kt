package com.cistus.hunter

actual class LocalDataConfig {
    actual class ToS {
        actual val directoryPath = "Caches/ToS"
        actual val dateFilePath = "${directoryPath}/date.txt"
    }
}