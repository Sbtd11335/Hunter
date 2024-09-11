package com.cistus.hunter

import platform.Foundation.NSHomeDirectory

actual class LocalDataConfig {
    actual class ToS {
        actual val directoryPath = "${NSHomeDirectory()}/Library/Caches/ToS"
        actual val dateFilePath = "${directoryPath}/date.txt"
    }
}