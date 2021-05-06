package com.vholodynskyi.game

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

fun Drawable.drawableToBitmap(): Bitmap {
    if (this is BitmapDrawable) {
        return this.bitmap
    }
    val bitmap = Bitmap.createBitmap(
        this.intrinsicWidth,
        this.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    this.setBounds(0, 0, canvas.width, canvas.height)
    this.draw(canvas)
    return bitmap
}

fun Bitmap.rotate(degrees: Int): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees.toFloat()) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}