package com.vholodynskyi.game

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
        private const val HIT_BOX_SIZE = 10
    }

    private val viewHolder = holder
    private var myTank = TankObject(
        this,
        resources.getDrawable(R.mipmap.ic_launcher, null).drawableToBitmap(2),
        width / 2f,
        height / 2f
    )

    private val gameLoopThread = GameLoopThread(this)

    private val projectileList = ArrayList<ProjectileObject>()
    private val collidableObjects = ArrayList<GameObject>()

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
        canvas.drawBitmap(myTank.bitmap, myTank.x, myTank.y, null)

        projectileList.forEach {
            if (it.valid) {
                canvas.drawBitmap(it.bitmap, it.x, it.y, null)
            } else {
                projectileList.remove(it)
            }
            checkProjectileCollision(it)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        gameLoopThread.running = false
        gameLoopThread.interrupt()
    }

    fun up() {
        myTank.goUp()
    }

    fun down() {
        myTank.goDown()
    }

    fun right() {
        myTank.goRight()
    }

    fun left() {
        myTank.goLeft()
    }

    fun fire() {
        Log.d(TAG, "FIRE ${myTank.orientation}")

        val projectile = ProjectileObject(
            this,
            resources.getDrawable(R.mipmap.ic_launcher, null).drawableToBitmap(5),
            x = myTank.x,
            y = myTank.y
        )

        projectileList.add(projectile)

        when (myTank.orientation) {
            Orientation.Left -> projectile.shootLeft()
            Orientation.Right -> projectile.shootRight()
            Orientation.Up -> projectile.shootUp()
            Orientation.Down -> projectile.shootDown()
        }
    }

    private fun checkProjectileCollision(projectile: ProjectileObject) {
        collidableObjects.forEach {
            if (projectile.x in (it.x - HIT_BOX_SIZE)..(it.x + HIT_BOX_SIZE)
                && projectile.y in (it.y - HIT_BOX_SIZE)..(it.y + HIT_BOX_SIZE)
            ) {
                projectile.valid = false
                if (!it.consumeDamage()) {
                    collidableObjects.remove(it)
                }
            }
        }
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
// додати стіни
// додати інші танки