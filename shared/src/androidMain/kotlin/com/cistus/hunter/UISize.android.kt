package com.cistus.hunter

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

fun UISize.toDpSize(): DpSize {
    return DpSize(width.toFloat().dp, height.toFloat().dp)
}