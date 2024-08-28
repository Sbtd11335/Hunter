package com.cistus.hunter.android

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource

val Color.Companion.Primary: Color
    @Composable
    get() = MaterialTheme.colorScheme.primary
val Color.Companion.AppColor1: Color
    @Composable
    get() = colorResource(id = R.color.AppColor1)
val Color.Companion.AppColor2: Color
    @Composable
    get() = colorResource(id = R.color.AppColor2)