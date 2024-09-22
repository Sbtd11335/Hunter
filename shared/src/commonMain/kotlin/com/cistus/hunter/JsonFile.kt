package com.cistus.hunter

expect class JsonFile {
    fun isExists(): Boolean
    fun read(): Map<String, *>?
    fun write(contents: Map<String, *>): Boolean
    fun append(contents:Map<String, *>): Boolean
    fun delete(keys: List<Any?>?): Boolean
    fun getPath(): String?
}