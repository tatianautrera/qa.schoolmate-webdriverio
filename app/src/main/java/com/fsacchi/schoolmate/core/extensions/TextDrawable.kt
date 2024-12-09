package com.fsacchi.schoolmate.core.extensions

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

class TextDrawable(private val text: String, context: Context) : Drawable() {
    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, android.R.color.black)
        textSize = 64f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    override fun draw(canvas: Canvas) {
        val bounds = bounds
        val x = bounds.centerX().toFloat()
        val y = bounds.centerY() - (paint.descent() + paint.ascent()) / 2
        canvas.drawText(text, x, y, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: android.graphics.ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int = android.graphics.PixelFormat.OPAQUE
}
