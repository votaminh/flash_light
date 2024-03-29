package com.flash.light.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.flash.light.R

class SeekBarVolume @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progress = 100f
    private var maxProgress = 100f

    var onProgressChange: ((Float) -> Unit)? = null
    var onProgressChanged: ((Float) -> Unit)? = null

    private val rect = RectF()
    private val radius by lazy { context.resources.getDimension(R.dimen.size15) }
    private val outerMargin by lazy { context.resources.getDimension(R.dimen.size3) }
    private val outerMarginCircle by lazy { context.resources.getDimension(R.dimen.size1) }
    private val circleRadius by lazy { context.resources.getDimension(R.dimen.size6) }

    private val outerPaint = Paint().apply {
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
        strokeWidth = context.resources.getDimension(R.dimen.size1)
        color = ContextCompat.getColor(context, R.color.colorAccent)
    }

    private val fillPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.colorAccent)
    }

    override fun onDraw(canvas: Canvas?) {
        rect.set(
            outerMarginCircle / 2f,
            outerMarginCircle / 2f,
            width.toFloat() - outerMarginCircle / 2f,
            height.toFloat() - outerMarginCircle / 2f
        )
        canvas?.drawRoundRect(rect, radius, radius, outerPaint)
        val maxWidth = width - outerMargin * 2 - outerMarginCircle * 2 - circleRadius * 2
        val right =
            outerMargin + outerMarginCircle + circleRadius + progress / maxProgress * maxWidth + outerMarginCircle + circleRadius
        rect.set(outerMargin, outerMargin, right, height - outerMargin)
        fillPaint.color = ContextCompat.getColor(context, R.color.colorAccent)
        canvas?.drawRoundRect(rect, radius, radius, fillPaint)
        val cx = outerMargin + outerMarginCircle + circleRadius + progress / maxProgress * maxWidth
        fillPaint.color = Color.WHITE
        canvas?.drawCircle(cx, height / 2f, circleRadius, fillPaint)
    }

    fun setProgress(progress: Float) {
        this.progress = progress
        invalidate()
    }

    fun getProgress() = this.progress

    fun setMaxProgress(maxProgress: Float) {
        this.maxProgress = maxProgress
        invalidate()
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                parent?.requestDisallowInterceptTouchEvent(true)
                calculatorProgress(event.x)
                onProgressChange?.invoke(progress)
            }

            MotionEvent.ACTION_MOVE -> {
                calculatorProgress(event.x)
                onProgressChange?.invoke(progress)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent?.requestDisallowInterceptTouchEvent(false)
                calculatorProgress(event.x)
                onProgressChanged?.invoke(progress)
            }
        }
        return true
    }

    private fun calculatorProgress(positionX: Float) {
        val maxValue = width - outerMargin * 2 - outerMarginCircle * 2 - circleRadius * 2
        val minValue = outerMargin + outerMarginCircle + circleRadius

        progress = if (positionX > maxValue) {
            100f
        } else if (positionX < minValue) {
            0f
        } else {
            (positionX - minValue) / (maxValue - minValue) * 100
        }
        invalidate()
    }

}