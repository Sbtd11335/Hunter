package com.cistus.hunter

open class UISize(var width: Double, var height: Double) {
    fun getSmallerSize(): Double {
        if (width < height)
            return width
        return height
    }
    fun getBiggerSize(): Double {
        if (width > height)
            return width
        return height
    }
}