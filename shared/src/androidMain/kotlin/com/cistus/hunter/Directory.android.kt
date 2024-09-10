package com.cistus.hunter

import java.io.File

actual class Directory(private val path: String) {
    private val file = File(path)
    actual fun isExists() = file.exists()
    actual fun create() = file.mkdir()
    actual fun delete() = file.delete()
    actual fun getPath() = path
}