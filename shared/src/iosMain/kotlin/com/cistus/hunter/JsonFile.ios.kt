package com.cistus.hunter

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSJSONReadingAllowFragments
import platform.Foundation.NSJSONSerialization
import platform.Foundation.NSJSONWritingPrettyPrinted
import platform.Foundation.NSLibraryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.URLByAppendingPathComponent
import platform.Foundation.create
import platform.Foundation.writeToURL

actual class JsonFile(private val path: String) {
    val url = NSFileManager.defaultManager.URLsForDirectory(NSLibraryDirectory, NSUserDomainMask).first() as NSURL
    val fileURL = url.URLByAppendingPathComponent(path)

    actual fun isExists(): Boolean {
        fileURL?.path?.let {
            return NSFileManager.defaultManager.fileExistsAtPath(it)
        }
        return false
    }
    @OptIn(BetaInteropApi::class, ExperimentalForeignApi::class)
    actual fun read(): Map<String, *>? {
        fileURL?.let {
            NSData.create(it)?.let { data ->
                return NSJSONSerialization.JSONObjectWithData(data, NSJSONReadingAllowFragments, null) as Map<String, *>?
            }
        }
        return null
    }
    @OptIn(ExperimentalForeignApi::class)
    actual fun write(contents: Map<String, *>): Boolean {
        val dataOptional = NSJSONSerialization.dataWithJSONObject(contents, NSJSONWritingPrettyPrinted, null)
        dataOptional?.let { data ->
            fileURL?.let {
                return data.writeToURL(it, true)
            }
        }
        return false
    }
    actual fun append(contents: Map<String, *>): Boolean {
        read()?.let { data ->
            val mutableData = data.toMutableMap()
            for (key in contents.keys) {
                mutableData[key] = contents[key]
            }
            return write(mutableData)
        }
        return write(contents)
    }
    @OptIn(ExperimentalForeignApi::class)
    actual fun delete(keys: List<Any?>?): Boolean {
        fileURL?.let { file ->
            keys?.let { keys ->
                read()?.let {
                    val mutableData = it.toMutableMap()
                    for (key in keys) {
                        mutableData.remove(key)
                    }
                    return write(mutableData)
                }
            }
            return NSFileManager.defaultManager.removeItemAtURL(file, null)
        }
        return false
    }
    actual fun getPath(): String? = path
}