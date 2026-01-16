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
import kotlin.math.cos
import kotlin.math.sin

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
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }
    private val majorTickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        strokeWidth = 5f
        strokeCap = Paint.Cap.ROUND
    }

    private val minorTickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        strokeWidth = 3f
        strokeCap = Paint.Cap.ROUND
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cx = width / 2f
        val cy = height / 2f
        val radius = min(cx, cy) - 150f

        canvas.drawCircle(cx, cy, radius, paint)
        // Background circle
        canvas.drawCircle(cx, cy, radius, paintBackground)
        // Clock ticks
        drawClockTicks(canvas, cx, cy, radius-80f)
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
        drawNumbers(canvas, cx, cy, radius - 180f)
        //animateProgress(0.25f)
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

    private fun drawNumbers(
        canvas: Canvas,
        cx: Float,
        cy: Float,
        textRadius: Float
    ) {
        val textHeightOffset =
            (textPaint.descent() + textPaint.ascent()) / 2

        for (i in 1..12) {
            val angleDeg = i * 30f - 90f
            val angleRad = Math.toRadians(angleDeg.toDouble())

            val x = cx + cos(angleRad) * textRadius
            val y = cy + sin(angleRad) * textRadius - textHeightOffset

            canvas.drawText(i.toString(), x.toFloat(), y.toFloat(), textPaint)
        }
    }
    private fun drawClockTicks(
        canvas: Canvas,
        cx: Float,
        cy: Float,
        radius: Float
    ) {
        for (i in 0 until 60) {
            val isMajor = i % 5 == 0

            val paint = if (isMajor) majorTickPaint else minorTickPaint
            val tickLength = if (isMajor) 30f else 15f

            val angleDeg = i * 6f - 90f
            val angleRad = Math.toRadians(angleDeg.toDouble())

            val startX = cx + cos(angleRad) * (radius - tickLength)
            val startY = cy + sin(angleRad) * (radius - tickLength)

            val endX = cx + cos(angleRad) * radius
            val endY = cy + sin(angleRad) * radius

            canvas.drawLine(
                startX.toFloat(),
                startY.toFloat(),
                endX.toFloat(),
                endY.toFloat(),
                paint
            )
        }
    }

}
