package com.cistus.hunter

expect class Directory {
    fun isExists(): Boolean
    fun create(): Boolean
    fun delete(): Boolean
    fun getPath(): String?
}