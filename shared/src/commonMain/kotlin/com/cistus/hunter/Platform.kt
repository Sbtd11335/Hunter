package com.cistus.hunter

expect class Platform {
    companion object {
        val os: String
        val version: String
    }
}