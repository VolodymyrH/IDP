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

    private fun Int.toGraphX() = DEF_PADDING + ((width.toFloat() - DEF_PADDING - DEF_PADDING) / xMax) * this
    private fun Int.toGraphY() = DEF_PADDING + ((height.toFloat() - DEF_PADDING - DEF_PADDING) / yMax) * this

    companion object {
        private const val TAG = "GraphView"

        private const val DEF_PADDING = 50f
    }

    private val dataSet = mutableListOf<DataPoint>()
    private var xMin = 0
    private var xMax = 0
    private var yMin = 0
    private var yMax = 0

    private val graphBoundX: Pair<Float, Float>
    private val graphBoundY: Pair<Float, Float>

    init {
        val demoList = LinkedList<DataPoint>()
        for (i in 0..9) {
            demoList.add(generateRandom(i+1))
        }
        setData(demoList)

        graphBoundX = Pair(DEF_PADDING, width.toFloat() - DEF_PADDING)
        graphBoundY = Pair(DEF_PADDING, height.toFloat() - DEF_PADDING)
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

        dataSet.forEachIndexed { index, currentDataPoint ->
            if (index < dataSet.size - 1) {
                val nextDataPoint = dataSet[index + 1]

                val startX = currentDataPoint.x.toGraphX()
                val startY = currentDataPoint.y.toGraphY()
                val endX = nextDataPoint.x.toGraphX()
                val endY = nextDataPoint.y.toGraphY()
                canvas.drawLine(startX, startY, endX, endY, dataPointLinePaint)

                canvas.drawCircle(startX, startY, 10f, dataPointFillPaint)
                canvas.drawCircle(startX, startY, 10f, dataPointPaint)
            }
        }

        canvas.drawLine(DEF_PADDING, DEF_PADDING, DEF_PADDING, height.toFloat() - DEF_PADDING, axisLinePaint)
        canvas.drawLine(
            DEF_PADDING,
            height.toFloat() - DEF_PADDING,
            width.toFloat() - DEF_PADDING,
            height.toFloat() - DEF_PADDING,
            axisLinePaint
        )
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