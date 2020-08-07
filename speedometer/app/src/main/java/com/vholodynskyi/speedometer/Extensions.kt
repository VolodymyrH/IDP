package com.vholodynskyi.speedometer

import android.content.Context
import android.graphics.PointF
import android.util.TypedValue
import kotlin.math.cos
import kotlin.math.sin

fun PointF.computeXYForItem(startAngle: Float, radiusStep: Float = 0f, radius: Float, width: Int) {
    val viewRadius = width / 2
    val angle = startAngle + radiusStep
    x = viewRadius + radius * cos(angle.degToRad())
    y = viewRadius + radius * sin(angle.degToRad())
}

fun Float.degToRad(): Float {
    return Math.toRadians(this.toDouble()).toFloat()
}

fun Int.toDP(context: Context): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),context.resources.displayMetrics).toInt()
}