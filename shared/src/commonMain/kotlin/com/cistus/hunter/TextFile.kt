package com.cistus.hunter

expect class TextFile {
    fun isExists(): Boolean
    fun read(): String?
    fun write(contents: String): Boolean
    fun append(contents: String): Boolean
    fun delete(): Boolean
    fun getPath(): String
}