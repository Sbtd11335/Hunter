package com.cistus.hunter

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager

actual class Directory(private val path: String) {
    private val fileManager = NSFileManager.defaultManager
    actual fun isExists() = fileManager.fileExistsAtPath(path)
    @OptIn(ExperimentalForeignApi::class)
    actual fun create() = fileManager.createDirectoryAtPath(path, true, null, null)
    @OptIn(ExperimentalForeignApi::class)
    actual fun delete() = fileManager.removeItemAtPath(path, null)
    actual fun getPath() = path
}