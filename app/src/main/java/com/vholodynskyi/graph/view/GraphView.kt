package com.vholodynskyi.graph.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.animation.doOnStart
import kotlin.math.abs
import kotlin.math.sqrt

class GraphView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private fun Int.toGraphX() = (((width.toFloat() * 0.9) / xMax) * this).toFloat()
    private fun Int.toGraphY() =
        (height.toFloat() - ((height.toFloat() * 0.9) / yMax) * this).toFloat()

    companion object {
        private const val TAG = "GraphView"

        private const val POINTER_RADIUS = 10f
        private const val DEF_PADDING = 50f
        private const val MIN_SWIPE_LENGTH = 150f
        private const val SCROLL_FRACTION = 4
    }

    private val dataSet = mutableListOf<GraphData>()
    private var xMax = 0
    private var yMin = 0
    private var yMax = 0
    private var closestPoint = DataPoint(xMax + 1, yMax)
    private var swipeAnchorX = 0f
    private var scroll = 0f
    private var step = 0f
    private val scrollAnimator = ValueAnimator()

    private val dataPointPaint = Paint().apply {
        color = Color.GREEN
        strokeWidth = 7f
        style = Paint.Style.STROKE
    }

    private val dataPointFillPaint = Paint().apply {
        color = Color.WHITE
    }

    private val dataPointLinePaint = Paint().apply {
        color = Color.GREEN
        strokeWidth = 7f
        isAntiAlias = true
    }

    private val yAxisTextPaint = TextPaint().apply {
        color = Color.BLUE
        textSize = 40f
    }

    private val xAxisTextPaint = TextPaint().apply {
        color = Color.BLUE
        textSize = 30f
    }

    private val axisLinePaint = Paint().apply {
        color = Color.RED
        strokeWidth = 7f
    }

    private val popupLinePaint = Paint().apply {
        color = Color.GRAY
        strokeWidth = 5f
    }

    private val gridLinePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 3f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        step = w / 4f
    }

    init {
        scrollAnimator.addUpdateListener {
            val animatedValue = it.animatedValue as Float
            val endX = xMax * step

            scroll = when {
                animatedValue < 0 -> animatedValue
                animatedValue > endX -> endX
                else -> 0f
            }

            invalidate()
        }
        scrollAnimator.duration = 1000
        scrollAnimator.doOnStart { dismissPopup() }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                swipeAnchorX = ev.x
                return true
            }
            MotionEvent.ACTION_UP -> {
                val swipeDistance = ev.x - swipeAnchorX
                val absSwipeDistance = abs(swipeDistance)

                if (absSwipeDistance > MIN_SWIPE_LENGTH) {
                    scrollAnimator.setFloatValues(scroll, scroll + swipeDistance)
                    scrollAnimator.start()
                } else {
                    closestPoint = getClosest(ev.x, ev.y)
                    invalidate()
                }
                return true
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val scrollX = (scroll * SCROLL_FRACTION) + DEF_PADDING
        canvas.translate(scrollX, -DEF_PADDING)
        drawGraph(canvas)
        drawAxis(canvas)
    }

    private fun drawGraph(canvas: Canvas) {
        var startX = 0f

        dataSet.forEachIndexed { index, currentDataPoint ->
            val startY = currentDataPoint.days.y.toGraphY()

            if (index != dataSet.size - 1) {
                val nextDataPoint = dataSet[index + 1]
                val endX = startX + step
                val endY = nextDataPoint.days.y.toGraphY()
                canvas.drawLine(startX, startY, endX, endY, dataPointLinePaint)
                startX = endX
            }

            if (currentDataPoint.days == closestPoint) {
                showPopup(currentDataPoint, canvas)
            }
        }
    }

    private fun drawAxis(canvas: Canvas) {
        val initialX = 0.toGraphX()
        val initialY = 0.toGraphY()
        val endX = initialX + xMax * width / 4f
        val independentX = 0.toGraphX() + scroll

        var currentX = initialX

        // Y axis
        canvas.drawLine(independentX, 0f, independentX, initialY, axisLinePaint)
        for (i in 1..yMax step (yMax * 0.1).toInt()) {
            val iToY = i.toGraphY()
            canvas.drawText(i.toString(), independentX - DEF_PADDING, iToY, yAxisTextPaint)
            canvas.drawLine(independentX, iToY, endX, iToY, gridLinePaint)
        }

        // X axis
        canvas.drawLine(independentX, initialY, endX, initialY, axisLinePaint)
        for (i in 0 until dataSet.size) {
            val text = dataSet[i].month.name

            canvas.drawText(text, currentX - DEF_PADDING, height.toFloat() + DEF_PADDING, xAxisTextPaint)
            currentX = step + i * step
        }
    }

    private fun getClosest(x: Float, y: Float): DataPoint {
        var shortest = 1000000f
        var closest = DataPoint.Null

        dataSet.forEachIndexed { index, dataPoint ->
            val deltaX = x - index * step
            val deltaY = y - dataPoint.days.y.toGraphY()

            val d = sqrt(deltaX * deltaX + deltaY * deltaY)

            if (d < shortest) {
                shortest = d
                closest = dataPoint.days
            }
        }
        return closest
    }

    private fun showPopup(dataPoint: GraphData, canvas: Canvas) {
        val index = dataSet.indexOf(dataPoint)
        val x = index * step
        val y = dataPoint.days.y.toGraphY()
        canvas.drawLine(x, 0f, x, 0.toGraphY(), popupLinePaint)
        canvas.drawCircle(x, y, POINTER_RADIUS, dataPointFillPaint)
        canvas.drawCircle(x, y, POINTER_RADIUS, dataPointPaint)

        (parent as? GraphLayout)?.showPopup(x)
    }

    private fun dismissPopup() {
        (parent as? GraphLayout)?.dismissPopup()
        closestPoint = DataPoint(xMax + 1, yMax)
    }

    fun setData(newDataSet: List<GraphData>) {
        xMax = newDataSet.size
        yMin = newDataSet.minOf { it.days.y }
        yMax = newDataSet.maxOf { it.days.y }

        dataSet.clear()
        dataSet.addAll(newDataSet)
        invalidate()
    }
}