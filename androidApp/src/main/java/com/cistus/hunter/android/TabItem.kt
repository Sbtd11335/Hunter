package com.cistus.hunter.android

import androidx.compose.runtime.Composable

interface TabItem {
    val label: String
    val icon: Int?
    val badge: Any?
    @Composable
    fun Draw()
}