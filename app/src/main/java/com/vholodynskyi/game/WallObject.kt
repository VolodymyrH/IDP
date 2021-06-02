package com.vholodynskyi.game

import android.graphics.Bitmap

class WallObject (
    override val view: GameView,
    override var bitmap: Bitmap,
    override var x: Float,
    override var y: Float
) : GameObject() {

    var damageCount = 2

    override fun consumeDamage(): Boolean {
        damageCount--

        if (damageCount == 0) {
            valid = false
        }

        return valid
    }
}