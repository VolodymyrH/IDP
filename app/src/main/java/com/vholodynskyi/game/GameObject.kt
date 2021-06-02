package com.vholodynskyi.game

import android.graphics.Bitmap

abstract class GameObject {
    abstract val view: GameView
    abstract var bitmap: Bitmap
    abstract var x: Float
    abstract var y: Float
    var valid: Boolean = true
}