package com.cistus.hunter

import android.annotation.SuppressLint
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter

actual class TextFile(private val path: String) {
    private val file = File(path)

    actual fun isExists(): Boolean = file.exists()
    actual fun read(): String? {
        try {
            val fileReader = FileReader(file)
            val bufferedReader = BufferedReader(fileReader)
            return if (!isExists()) null else bufferedReader.readLine()
        }
        catch (e: Exception) {
            return null
        }
    }
    @SuppressLint("SuspiciousIndentation")
    actual fun write(contents: String): Boolean {
        try {
        val fileWriter = FileWriter(file)
        val bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(contents)
            return true
        }
        catch (e: Exception) {
            return false
        }
    }
    actual fun append(contents: String): Boolean {
        try {
            val fileWriter = FileWriter(file, true)
            val bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(contents)
            return true
        }
        catch (e: Exception) {
            return false
        }
    }
    actual fun delete(): Boolean = file.delete()
    actual fun getPath() = path
}