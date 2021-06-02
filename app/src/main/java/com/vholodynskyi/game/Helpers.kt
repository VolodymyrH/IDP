package com.vholodynskyi.game

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log

fun Drawable.drawableToBitmap(size: Int = 150): Bitmap {
    if (this is BitmapDrawable) {
        return Bitmap.createScaledBitmap(bitmap, size, size, false)
    }
    val bitmap = Bitmap.createBitmap(
        200,
        200,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    canvas.drawBitmap(bitmap, null, Rect(0, 0, size, size), null)
    return bitmap
}

fun Bitmap.rotate(degrees: Int): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees.toFloat()) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}