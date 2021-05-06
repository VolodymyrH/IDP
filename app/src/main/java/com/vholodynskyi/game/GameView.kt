package com.vholodynskyi.game

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.vholodynskyi.graph.R

class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "GameView"
        private const val FPS = 60
        private const val MOVE_DISTANCE = 100f
    }

    private val viewHolder = holder
    private var tank = TankObject(resources.getDrawable(R.mipmap.ic_launcher, null).drawableToBitmap())
    private val gameLoopThread = GameLoopThread(this)

    private var tankXPosition = 0f
    private var tankYPosition = 0f

    private val xMoveAnimator = ValueAnimator.ofFloat().apply {
        duration = 500
        addUpdateListener {
            val value = it.animatedValue as Float

            if (value < 0) {
                tankXPosition = 0f
                return@addUpdateListener
            }

            if (value > width - tank.bitmap.width) {
                tankXPosition = (width - tank.bitmap.width).toFloat()
                return@addUpdateListener
            }

            tankXPosition = value
        }
    }

    private val yMoveAnimator = ValueAnimator.ofFloat().apply {
        duration = 500
        addUpdateListener {
            val value = it.animatedValue as Float

            if (value < 0) {
                tankYPosition = 0f
                return@addUpdateListener
            }

            if (value > height - tank.bitmap.height) {
                tankYPosition = height.toFloat()
                return@addUpdateListener
            }

            tankYPosition = value
        }
    }

    init {
        viewHolder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                gameLoopThread.running = true
                gameLoopThread.start()
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                var retry = true
                gameLoopThread.running = false
                while (retry) {
                    try {
                        gameLoopThread.join()
                        retry = false
                    } catch (e: InterruptedException) {
                        Log.e(TAG, "very sad times $e")
                    }
                }
            }
        })
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        canvas.drawColor(Color.DKGRAY)
        canvas.drawBitmap(tank.bitmap, tankXPosition, tankYPosition, null)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        gameLoopThread.running = false
        gameLoopThread.interrupt()
    }

    fun up() {
        yMoveAnimator.apply {
            setFloatValues(tankYPosition, tankYPosition - MOVE_DISTANCE)
            start()
        }

        tank.goUp()
    }

    fun down() {
        yMoveAnimator.apply {
            setFloatValues(tankYPosition, tankYPosition + MOVE_DISTANCE)
            start()
        }

        tank.goDown()
    }

    fun right() {
        xMoveAnimator.apply {
            setFloatValues(tankXPosition, tankXPosition + MOVE_DISTANCE)
            start()
        }

        tank.goRight()
    }

    fun left() {
        xMoveAnimator.apply {
            setFloatValues(tankXPosition, tankXPosition - MOVE_DISTANCE)
            start()
        }

        tank.goLeft()
    }

    fun fire() {
        Log.d(TAG, "FIRE")
    }

    private class GameLoopThread(private val view: GameView) : Thread() {
        var running = false

        override fun run() {
            val fraction = 1000 / FPS
            var startTime: Long
            var sleepTime: Long

            while (running) {
                startTime = System.currentTimeMillis()
                var canvas: Canvas? = null
                try {
                    canvas = view.viewHolder.lockCanvas()
                    synchronized(view.viewHolder) {
                        view.draw(canvas)
                    }
                } finally {
                    if (canvas != null) {
                        view.viewHolder.unlockCanvasAndPost(canvas)
                    }
                }

                sleepTime = fraction - (System.currentTimeMillis() - startTime)
                try {
                    if (sleepTime > 0) sleep(sleepTime)
                    else sleep(10)
                } catch (e: InterruptedException) {
                }
            }
        }
    }
}