package com.vholodynskyi.graph.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*

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
    }

    private val dataSet = mutableListOf<DataPoint>()
    private var xMin = 0
    private var xMax = 0
    private var yMin = 0
    private var yMax = 0

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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.translate(DEF_PADDING, -DEF_PADDING)

        drawGraph(canvas)
        drawAxis(canvas)
        drawGrid(canvas)
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

            canvas.drawCircle(startX, startY, 10f, dataPointFillPaint)
            canvas.drawCircle(startX, startY, 10f, dataPointPaint)
        }
    }

    private fun drawGrid(canvas: Canvas) {
        for (i in 1..yMax) {
            val iToY = i.toGraphY()
            canvas.drawText(i.toString(), -DEF_PADDING, iToY, textPaint)
            canvas.drawLine(0f, iToY, width.toFloat(), iToY, gridLinePaint)
        }
        for (i in 1..xMax) {
            val iToX = i.toGraphX()
            canvas.drawText(i.toString(), iToX, height.toFloat() + DEF_PADDING, textPaint)
            canvas.drawLine(iToX, 0f, iToX, height.toFloat(), gridLinePaint)
        }
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