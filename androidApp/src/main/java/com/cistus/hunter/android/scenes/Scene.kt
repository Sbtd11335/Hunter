package com.cistus.hunter.android.scenes

import androidx.compose.runtime.Composable

interface Scene {
    val route: String
    @Composable
    fun Draw()
}