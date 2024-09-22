package com.cistus.hunter

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSLibraryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.URLByAppendingPathComponent

actual class Directory(path: String) {
    private val fileManager = NSFileManager.defaultManager
    private val url = NSFileManager.defaultManager.URLsForDirectory(NSLibraryDirectory, NSUserDomainMask).first() as NSURL
    private val fileURL = url.URLByAppendingPathComponent(path)

    actual fun isExists(): Boolean {
        fileURL?.path?.let {
            return fileManager.fileExistsAtPath(it)
        }
        return false
    }
    @OptIn(ExperimentalForeignApi::class)
    actual fun create(): Boolean {
        fileURL?.let {
            return fileManager.createDirectoryAtURL(it, true, null, null)
        }
        return false
    }
    @OptIn(ExperimentalForeignApi::class)
    actual fun delete(): Boolean {
        fileURL?.let {
            return fileManager.removeItemAtURL(it, null)
        }
        return false
    }
    actual fun getPath(): String? {
        fileURL?.path?.let {
            return it
        }
        return null
    }
}