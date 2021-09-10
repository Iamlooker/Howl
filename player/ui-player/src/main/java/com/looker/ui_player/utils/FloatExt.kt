package com.looker.ui_player.utils

object FloatExt {
    fun Float.seekForward(by: Float = 0.1f): Float =
        if (this.plus(by) >= 1f) 1f
        else this.plus(by)

    fun Float.seekBack(by: Float = 0.1f): Float =
        if (this.minus(by) <= 0f) 0f
        else this.minus(by)
}