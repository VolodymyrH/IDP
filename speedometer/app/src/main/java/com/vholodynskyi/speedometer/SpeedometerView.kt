package com.vholodynskyi.speedometer

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import kotlin.math.min

class SpeedometerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val RADIUS_OFFSET_INDICATOR = -45
        private const val NUMBERS_RAD_STEP = 260 / 13f
        private const val START_ANGLE = 135f
    }

    private var circleRadius = 0.0f
    private var numbersRadius = 0.0f

    private val numbersPosition: PointF = PointF(0.0f, 0.0f)
    private val arrowPosition: PointF = PointF(0.0f, 0.0f)

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        color = resources.getColor(R.color.colorArrow)
        textSize = 40.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = resources.getColor(R.color.colorPrimary)
    }

    private val arrowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 10f
        color = resources.getColor(R.color.colorArrow)
    }

    private val accelerateAnimation = ValueAnimator.ofFloat(START_ANGLE, 400f).apply {
        addUpdateListener {
            arrowPosition.computeXYForItem((it.animatedValue as Float), 0f, numbersRadius - 20, width)
            invalidate()
        }
        interpolator = DecelerateInterpolator()
        duration = 2000
    }

    private val decelerateAnimation = ValueAnimator.ofFloat(400f, START_ANGLE).apply {
        addUpdateListener {
            arrowPosition.computeXYForItem((it.animatedValue as Float), 0f, numbersRadius - 20, width)
            invalidate()
        }
        interpolator = AccelerateInterpolator()
        duration = 2000
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val side = min(
            measureDimension(desiredWidth, widthMeasureSpec),
            measureDimension(desiredHeight, heightMeasureSpec)
        )

        circleRadius = (side / 2.0 * 0.8).toFloat()
        numbersRadius = circleRadius + RADIUS_OFFSET_INDICATOR
        arrowPosition.computeXYForItem(START_ANGLE, 0f, numbersRadius - 20, side)

        setMeasuredDimension(side, side)
    }

    private fun measureDimension(desiredSize: Int, measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        var result: Int

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = desiredSize
            if (specMode == MeasureSpec.AT_MOST) {
                result = result.coerceAtMost(specSize)
            }
        }

        return result
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawCircle(canvas)
        drawSpeedNumbers(canvas)
        drawArrow(canvas)
    }

    private fun drawCircle(canvas: Canvas) {
        canvas.drawCircle(width / 2f, width / 2f, circleRadius, circlePaint)
    }

    private fun drawSpeedNumbers(canvas: Canvas) {
        for (i in 0..13) {
            numbersPosition.computeXYForItem(START_ANGLE, NUMBERS_RAD_STEP * i, numbersRadius, width)
            canvas.drawText((i * 20).toString(), numbersPosition.x, numbersPosition.y, textPaint)
        }
    }

    private fun drawArrow(canvas: Canvas) {
        canvas.drawLine(width / 2f, width / 2f, arrowPosition.x, arrowPosition.y, arrowPaint)
    }

    fun startAcceleration() {
        accelerateAnimation.start()
    }

    fun breakClicked() {
        decelerateAnimation.start()
    }
}