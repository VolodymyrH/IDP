package com.vholodynskyi.graph.view

data class DataPoint(val x: Int, val y: Int) {
    companion object {
        val Null = DataPoint(-1, -1)
    }
}