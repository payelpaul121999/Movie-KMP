package com.pajo.movieapp_kmp

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import java.lang.Float.min

class CircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        style = Paint.Style.STROKE   // Use FILL if you want solid
        strokeWidth = 20f
    }

    // Progress ring
    private val paintProgress = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 22f
        strokeCap = Paint.Cap.ROUND
    }
    // Background circle
    private val paintBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeWidth = 20f
    }
    private val arcRect = RectF()

    private var progress = 0.5f   // 0f..1f
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cx = width / 2f
        val cy = height / 2f
        val radius = min(cx, cy) - 150f

        canvas.drawCircle(cx, cy, radius, paint)
        // Background circle
        canvas.drawCircle(cx, cy, radius, paintBackground)

        // Arc bounds
        arcRect.set(
            cx - radius,
            cy - radius,
            cx + radius,
            cy + radius
        )

        // Progress arc
        canvas.drawArc(
            arcRect,
            -90f,                 // start at top
            progress * 360f,      // sweep angle
            false,                // DO NOT use center for stroke
            paintProgress
        )
    }

    // Set progress instantly
    fun setProgress(value: Float) {
        progress = value.coerceIn(0f, 1f)
        invalidate()
    }

    // Animate progress
    fun animateProgress(
        to: Float,
        duration: Long = 1000
    ) {
        ValueAnimator.ofFloat(progress, to.coerceIn(0f, 1f)).apply {
            this.duration = duration
            addUpdateListener {
                progress = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }
}
