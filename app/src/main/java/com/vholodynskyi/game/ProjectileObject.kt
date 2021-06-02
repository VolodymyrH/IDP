package com.vholodynskyi.game

import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.util.Log

class ProjectileObject(
    override val view: GameView,
    override var bitmap: Bitmap,
    override var x: Float,
    override var y: Float
) : GameObject() {

    private var orientation = Orientation.Up

    private val projectileXAnimator = ValueAnimator.ofFloat().apply {
        duration = 500
        addUpdateListener {
            val value = it.animatedValue as Float

            if (value >= view.width || value <= 0) {
                Log.d("GameView", "invalidate")
                valid = false
                return@addUpdateListener
            }

            x = value
        }
    }

    private val projectileYAnimator = ValueAnimator.ofFloat().apply {
        duration = 500
        addUpdateListener {
            val value = it.animatedValue as Float

            if (value >= view.height || value <= 0) {
                valid = false
                return@addUpdateListener
            }

            y = value
        }
    }

    fun shootLeft() {
        val changedAngle = orientation.degree - Orientation.Left.degree
        orientation = Orientation.Left
        bitmap = bitmap.rotate(changedAngle)
        projectileXAnimator.apply {
            setFloatValues(x, 0f)
            start()
        }
    }

    fun shootRight() {
        val changedAngle = orientation.degree - Orientation.Right.degree
        orientation = Orientation.Right
        bitmap = bitmap.rotate(changedAngle)
        projectileXAnimator.apply {
            setFloatValues(x, view.width.toFloat())
            start()
        }
    }

    fun shootDown() {
        val changedAngle = orientation.degree - Orientation.Down.degree
        orientation = Orientation.Down
        bitmap = bitmap.rotate(changedAngle)
        projectileYAnimator.apply {
            setFloatValues(y, view.height.toFloat())
            start()
        }
    }

    fun shootUp() {
        val changedAngle = orientation.degree - Orientation.Up.degree
        orientation = Orientation.Up
        bitmap = bitmap.rotate(changedAngle)
        projectileYAnimator.apply {
            setFloatValues(y, 0f)
            start()
        }
    }
}