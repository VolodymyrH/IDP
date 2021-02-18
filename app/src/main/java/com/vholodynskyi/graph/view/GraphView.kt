package com.vholodynskyi.graph.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import java.util.*
import kotlin.math.abs
import kotlin.math.sqrt

class GraphView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private fun Int.toGraphX() = (((width.toFloat() * 0.9) / xMax) * this).toFloat()
    private fun Int.toGraphY() = (height.toFloat() - ((height.toFloat() * 0.9) / yMax) * this).toFloat()

    companion object {
        private const val TAG = "GraphView"

        private const val DEF_PADDING = 50f
        private const val RANGE_STEP = 1
    }

    private val dataSet = mutableListOf<DataPoint>()
    private var xMin = 0
    private var xMax = 0
    private var yMin = 0
    private var yMax = 0

    private var closestPoint = DataPoint(xMax + 1, yMax)

    private var swipeAnchorX = 0f

    init {
        val demoList = LinkedList<DataPoint>()
        demoList.add(DataPoint(0, 0))
        demoList.add(DataPoint(1, 1))
        demoList.add(DataPoint(2, 2))
        demoList.add(DataPoint(3, 3))
        demoList.add(DataPoint(4, 4))
        demoList.add(DataPoint(5, 10))
        setData(demoList)
    }

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

    private val textPaint = Paint().apply {
        color = Color.BLUE
        textSize = 50f
        style = Paint.Style.STROKE
    }

    private val axisLinePaint = Paint().apply {
        color = Color.RED
        strokeWidth = 7f
    }

    private val gridLinePaint = Paint().apply {
        color = Color.GRAY
        strokeWidth = 3f
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                swipeAnchorX = ev.x
                closestPoint = getClosest(ev.x, ev.y)

                invalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
                val swipeDistance = abs(ev.x - swipeAnchorX).toInt()

                if (swipeDistance > 150) {
                    if (ev.x > swipeAnchorX) {
                        Log.d(TAG, "Swipe Right, distance $swipeDistance")
                    } else {
                        Log.d(TAG, "Swipe Left, distance $swipeDistance")
                    }
                }
                return true
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.translate(DEF_PADDING, -DEF_PADDING)

        drawGrid(canvas)
        drawGraph(canvas)
        drawAxis(canvas)
    }

    private fun drawAxis(canvas: Canvas) {
        val h = height.toFloat()
        val w = width.toFloat()

        canvas.drawLine(0f, 0f, 0f, h, axisLinePaint)
        canvas.drawLine(0f, h, w, h, axisLinePaint)
    }

    private fun drawGraph(canvas: Canvas) {
        dataSet.forEachIndexed { index, currentDataPoint ->
            val startX = currentDataPoint.x.toGraphX()
            val startY = currentDataPoint.y.toGraphY()

            if (index != dataSet.size - 1) {
                val nextDataPoint = dataSet[index + 1]
                val endX = nextDataPoint.x.toGraphX()
                val endY = nextDataPoint.y.toGraphY()
                canvas.drawLine(startX, startY, endX, endY, dataPointLinePaint)
            }

            if (currentDataPoint == closestPoint) {
                showPopup(currentDataPoint, canvas)
            }
        }
    }

    private fun drawGrid(canvas: Canvas) {
        for (i in 1..yMax) {
            //add text layout for text alignment
            val iToY = i.toGraphY()
            canvas.drawText(i.toString(), -DEF_PADDING, iToY, textPaint)
            canvas.drawLine(0f, iToY, width.toFloat(), iToY, gridLinePaint)
        }
        for (i in 1..xMax) {
            val iToX = i.toGraphX()
            canvas.drawText(i.toString(), iToX, height.toFloat() + DEF_PADDING, textPaint)
//            canvas.drawLine(iToX, 0f, iToX, height.toFloat(), gridLinePaint)
        }
    }

    private fun getClosest(x: Float, y: Float) : DataPoint {
        var shortest = 10000f
        var closest = DataPoint(xMax + 1, yMax)

        dataSet.forEachIndexed { _, dataPoint ->
            val deltaX = x - dataPoint.x.toGraphX()
            val deltaY = y - dataPoint.y.toGraphY()

            val d = sqrt(deltaX * deltaX + deltaY * deltaY)

            if (d < shortest) {
                shortest = d
                closest = dataPoint
            }
        }
        return closest
    }

    private fun showPopup(dataPoint: DataPoint, canvas: Canvas) {
        val x = dataPoint.x.toGraphX()
        val y = dataPoint.x.toGraphY()
        canvas.drawCircle(x, y, 10f, dataPointFillPaint)
        canvas.drawCircle(x, y, 10f, dataPointPaint)
        Toast.makeText(context, "Колись зроблю. ${dataPoint.x} - ${dataPoint.y}", Toast.LENGTH_SHORT).show()
    }

    private fun setData(newDataSet: List<DataPoint>) {
        xMin = newDataSet.minBy { it.x }?.x ?: 0
        xMax = newDataSet.maxBy { it.x }?.x ?: 0
        yMin = newDataSet.minBy { it.y }?.y ?: 0
        yMax = newDataSet.maxBy { it.y }?.y ?: 0
        dataSet.clear()
        dataSet.addAll(newDataSet)
        invalidate()
    }
}