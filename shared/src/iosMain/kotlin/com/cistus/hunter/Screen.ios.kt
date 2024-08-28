package com.cistus.hunter

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.UIScreen

class IOSScreen: ScreenIn {
    @OptIn(ExperimentalForeignApi::class)
    override val width = UIScreen.mainScreen.bounds.useContents { size.width }
    @OptIn(ExperimentalForeignApi::class)
    override val height = UIScreen.mainScreen.bounds.useContents { size.height }
}

actual fun getScreen(): ScreenIn = IOSScreen()