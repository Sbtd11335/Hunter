package com.cistus.hunter

interface ScreenIn {
    val width: Double
    val height: Double
}

class Screen {
    companion object {
        val width = getScreen().width
        val height = getScreen().height
        val smallerSize = if (width < height)
            width
        else
            height
        val biggerSize = if (width > height)
            width
        else
            height
    }
}

expect fun getScreen(): ScreenIn