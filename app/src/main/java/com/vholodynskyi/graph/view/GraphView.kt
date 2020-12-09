package com.vholodynskyi.graph.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.*

class GraphView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "GraphView"
    }

    private fun Int.toRealX() = width - (toFloat() / xMax * width)
    private fun Int.toRealY() = height - (toFloat() / yMax * height)

    private val dataSet = mutableListOf<DataPoint>()
    private var xMin = 0
    private var xMax = 0
    private var yMin = 0
    private var yMax = 0

    init {
        val demoList = LinkedList<DataPoint>()
        for (i in 0..9) {
            demoList.add(generateRandom(i))
        }
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

    private val axisLinePaint = Paint().apply {
        color = Color.RED
        strokeWidth = 10f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

//        dataSet.forEachIndexed { index, currentDataPoint ->
//            val realX = currentDataPoint.x.toRealX()
//            val realY = currentDataPoint.y.toRealY()
//
//            if (index < dataSet.size - 1) {
//                val nextDataPoint = dataSet[index + 1]
//                val startX = currentDataPoint.x.toRealX()
//                val startY = currentDataPoint.y.toRealY()
//                val endX = nextDataPoint.x.toRealX()
//                val endY = nextDataPoint.y.toRealY()
//                canvas.drawLine(startX, startY, endX, endY, dataPointLinePaint)
//            }
//
//            canvas.drawCircle(realX, realY, 10f, dataPointFillPaint)
//            canvas.drawCircle(realX, realY, 10f, dataPointPaint)
//        }

        canvas.drawLine(0f, 0f, 0f, height.toFloat(), axisLinePaint)
        canvas.drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), axisLinePaint)
    }

    fun setData(newDataSet: List<DataPoint>) {
        xMin = newDataSet.minBy { it.x }?.x ?: 0
        xMax = newDataSet.maxBy { it.x }?.x ?: 0 + 1
        yMin = newDataSet.minBy { it.y }?.y ?: 0
        yMax = newDataSet.maxBy { it.y }?.y ?: 0 + 1
        dataSet.clear()
        dataSet.addAll(newDataSet)
        invalidate()
    }
}