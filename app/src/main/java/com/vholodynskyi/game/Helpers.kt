package com.vholodynskyi.game

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

fun Drawable.drawableToBitmap(scale: Int = 2): Bitmap {
    if (this is BitmapDrawable) {
        return bitmap
    }
    val bitmap = Bitmap.createBitmap(
        intrinsicWidth / scale,
        intrinsicHeight / scale,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}

fun Bitmap.rotate(degrees: Int): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees.toFloat()) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}