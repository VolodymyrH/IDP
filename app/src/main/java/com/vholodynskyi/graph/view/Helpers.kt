package com.vholodynskyi.graph.view

import java.util.*

fun generateRandom(seed: Int): DataPoint {
    return DataPoint(seed, Random().nextInt(5))
}