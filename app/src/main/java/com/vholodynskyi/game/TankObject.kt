package com.vholodynskyi.game

import android.graphics.Bitmap

class TankObject(var bitmap: Bitmap) {
    private var orientation = Orientation.Up

    init {
        goUp()
    }

    fun goLeft() {
        val changedAngle = orientation.degree - Orientation.Left.degree
        orientation = Orientation.Left
        bitmap = bitmap.rotate(changedAngle)
    }

    fun goRight() {
        val changedAngle = orientation.degree - Orientation.Right.degree
        orientation = Orientation.Right
        bitmap = bitmap.rotate(changedAngle)
    }

    fun goDown() {
        val changedAngle = orientation.degree - Orientation.Down.degree
        orientation = Orientation.Down
        bitmap = bitmap.rotate(changedAngle)
    }

    fun goUp() {
        val changedAngle = orientation.degree - Orientation.Up.degree
        orientation = Orientation.Up
        bitmap = bitmap.rotate(changedAngle)
    }
}