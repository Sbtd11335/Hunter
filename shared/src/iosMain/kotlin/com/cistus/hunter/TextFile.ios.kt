package com.cistus.hunter

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSLibraryDirectory
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.URLByAppendingPathComponent
import platform.Foundation.create
import platform.Foundation.writeToURL

actual class TextFile(path: String) {
    private val fileManager = NSFileManager.defaultManager
    private val url = fileManager.URLsForDirectory(NSLibraryDirectory, NSUserDomainMask).first() as NSURL
    private val fileURL = url.URLByAppendingPathComponent(path)

    actual fun isExists(): Boolean {
        fileURL?.path?.let {
            return fileManager.fileExistsAtPath(it)
        }
        return false
    }
    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual fun read(): String? {
        fileURL?.let {
            NSString.create(it, NSUTF8StringEncoding, null)?.let { contents ->
                return contents.toString()
            }
        }
        return null
    }
    @OptIn(BetaInteropApi::class, ExperimentalForeignApi::class)
    actual fun write(contents: String): Boolean {
        val saveContent = NSString.create(string = contents)
        fileURL?.let {
            return saveContent.writeToURL(it, true, NSUTF8StringEncoding, null)
        }
        return false
    }
    @OptIn(BetaInteropApi::class)
    actual fun append(contents: String): Boolean {
        val data = read() ?: ""
        val saveContent = NSString.create(string = contents)
        return write("$data$saveContent")
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