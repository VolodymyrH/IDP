package com.vholodynskyi.graph.view

import java.time.Month
import kotlin.collections.ArrayList
import kotlin.random.Random

data class GraphData(val month: Month, val days: DataPoint)

fun generateData(): ArrayList<GraphData> {
    val months = Month.values()

    val result = ArrayList<GraphData>()

    months.forEach {
        result.add(GraphData(it, (DataPoint(it.ordinal * Random.nextInt(1, 10), it.ordinal * Random.nextInt(1, 10)))))
    }

    return result
}

private fun dataForDays(): List<DataPoint> {
    val result = ArrayList<DataPoint>()

    for (i in 1..31) {
        result.add((DataPoint(i * Random.nextInt(1, 10), i * Random.nextInt(1, 10))))
    }

    return result
}