package com.cistus.hunter

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.writeToFile

actual class TextFile(private val path: String) {
    private val fileManager = NSFileManager.defaultManager

    actual fun isExists() = fileManager.fileExistsAtPath(path)
    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual fun read(): String? {
        NSString.create(contentsOfFile = path, NSUTF8StringEncoding, null)?.let { contents ->
            return contents.toString()
        }
        return null
    }
    @OptIn(BetaInteropApi::class, ExperimentalForeignApi::class)
    actual fun write(contents: String): Boolean {
        val saveContent = NSString.create(string = contents)
        return saveContent.writeToFile(path, true, NSUTF8StringEncoding, null)
    }
    actual fun append(contents: String): Boolean {
        var saveContents = ""
        read()?.let { readContents ->
            saveContents = readContents
        }
        return write("${saveContents}${contents}")
    }
    @OptIn(ExperimentalForeignApi::class)
    actual fun delete(): Boolean = fileManager.removeItemAtPath(path, null)
    actual fun getPath() = path
}