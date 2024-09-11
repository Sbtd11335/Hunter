package com.cistus.hunter

import android.annotation.SuppressLint
import java.io.File

actual class TextFile(private val path: String) {
    private val file = File(path)

    actual fun isExists(): Boolean = file.exists()
    actual fun read(): String? {
        return if (!isExists()) null else file.readText()
    }
    @SuppressLint("SuspiciousIndentation")
    actual fun write(contents: String): Boolean {
        file.writeText(contents)
        return true
    }
    actual fun append(contents: String): Boolean {
        file.appendText(contents)
        return true
    }
    actual fun delete(): Boolean = file.delete()
    actual fun getPath() = path
}