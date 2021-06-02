package com.vholodynskyi.game

import android.animation.ValueAnimator
import android.graphics.Bitmap

class TankObject(
    override val view: GameView,
    override var bitmap: Bitmap,
    override var x: Float,
    override var y: Float,
    val enemy: Boolean = true,
    private val collidableObjects: List<GameObject>
) : GameObject() {

    companion object {
        private const val MOVE_DISTANCE = 100f
        private const val HIT_BOX_SIZE = 20
    }

    var orientation = Orientation.Up

    private val xMoveAnimator = ValueAnimator.ofFloat().apply {
        duration = 500
        addUpdateListener {
            val value = it.animatedValue as Float

            if (value < 0) {
                x = 0f
                return@addUpdateListener
            }

            collidableObjects.forEach { obj ->
                if ((x + bitmap.width) in (obj.x - HIT_BOX_SIZE)..(obj.x + obj.bitmap.width + HIT_BOX_SIZE)
                    && (y + bitmap.height) in (obj.y - HIT_BOX_SIZE)..(obj.y + obj.bitmap.height + HIT_BOX_SIZE)
                ) {
                    return@addUpdateListener
                }
            }

            if (value > view.width - bitmap.width) {
                x = (view.width - bitmap.width).toFloat()
                return@addUpdateListener
            }

            x = value
        }
    }

    private val yMoveAnimator = ValueAnimator.ofFloat().apply {
        duration = 500
        addUpdateListener {
            val value = it.animatedValue as Float

            if (value < 0) {
                y = 0f
                return@addUpdateListener
            }

            if (value > view.height - bitmap.height) {
                y = view.height.toFloat()
                return@addUpdateListener
            }

            y = value
        }
    }

    init {
        goUp()
    }

    override fun consumeDamage(): Boolean {
        valid = false
        return false
    }

    fun goLeft() {
        val changedAngle = orientation.degree - Orientation.Left.degree
        orientation = Orientation.Left
        bitmap = bitmap.rotate(changedAngle)

        xMoveAnimator.apply {
            setFloatValues(x, x - MOVE_DISTANCE)
            start()
        }
    }

    fun goRight() {
        val changedAngle = orientation.degree - Orientation.Right.degree
        orientation = Orientation.Right
        bitmap = bitmap.rotate(changedAngle)

        xMoveAnimator.apply {
            setFloatValues(x, x + MOVE_DISTANCE)
            start()
        }
    }

    fun goDown() {
        val changedAngle = orientation.degree - Orientation.Down.degree
        orientation = Orientation.Down
        bitmap = bitmap.rotate(changedAngle)

        yMoveAnimator.apply {
            setFloatValues(y, y + MOVE_DISTANCE)
            start()
        }
    }

    fun goUp() {
        val changedAngle = orientation.degree - Orientation.Up.degree
        orientation = Orientation.Up
        bitmap = bitmap.rotate(changedAngle)

        yMoveAnimator.apply {
            setFloatValues(y, y - MOVE_DISTANCE)
            start()
        }
    }
}