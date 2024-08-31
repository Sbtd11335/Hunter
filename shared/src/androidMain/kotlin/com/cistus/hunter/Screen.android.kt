package com.cistus.hunter

import android.graphics.Point
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity

class AndroidScreen: ScreenIn {
    override val width = Companion.width
    override val height = Companion.height

    companion object {
        private var width = 0.0
        private var height = 0.0
        @Composable
        fun Initialize(windowManager: WindowManager) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                val point = Point()
                windowManager.defaultDisplay.getRealSize(point)
                with(LocalDensity.current) {
                    width = point.x.toDp().value.toDouble()
                    height = point.y.toDp().value.toDouble()
                }
            }
            else {
                with(LocalDensity.current) {
                    width = windowManager.currentWindowMetrics.bounds.width().toDp().value.toDouble()
                    height = windowManager.currentWindowMetrics.bounds.height().toDp().value.toDouble()
                }
            }
        }
        fun hideSystemBars(window: Window) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            }
            else {
                window.decorView.windowInsetsController?.apply {
                    hide(WindowInsets.Type.systemBars())
                    systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }
        }
    }
}

actual fun getScreen(): ScreenIn = AndroidScreen()