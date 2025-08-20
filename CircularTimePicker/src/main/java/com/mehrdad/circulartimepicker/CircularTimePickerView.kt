package com.mehrdad.circulartimepicker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

class CircularTimePickerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    var onTimeSelected: ((hour: Int, minute: Int) -> Unit)? = null
    private var radius = 0f
    private var secondRadius = 0f
    private var centerX = 0f
    private var centerY = 0f
    private var handleAngle = -90.0
    private var isHourMode = true
    private var selectedHour = 12
    private var selectedMinute = 0
    private var isMoveOnSecondCircle = false
    private var circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        color = Color.GRAY
    }
    private var secondCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 10f
        color = Color.GRAY
    }
    private var handlerPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 2f
        isAntiAlias = true
    }
    private var textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }
    private var tickPaintMain = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        strokeWidth = 4f
    }
    private var tickPaintMinor = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        strokeWidth = 2f
    }
    private var circleMovable = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
    }

    fun getSelectedHour() = selectedHour



    fun getSelectedMinute() = selectedMinute


    //---------------------------------------------------------------------------------------------- setSelectedTime
    fun setSelectedTime(hour: Int, minute: Int) {
        selectedHour = hour
        selectedMinute = minute
        resetToHourMode()
    }
    //---------------------------------------------------------------------------------------------- setSelectedTime


    //---------------------------------------------------------------------------------------------- setStrokeStyle
    fun setStrokeStyle(strokeColor: Int, width: Float, numberColor: Int, circle: Int, handlerWidth: Float) {
        circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = width
            color = strokeColor
        }
        secondCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = width / 2
            color = strokeColor
        }
        tickPaintMinor = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = strokeColor
            strokeWidth = 2f
        }
        tickPaintMain = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = numberColor
            strokeWidth = 4f
        }
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = numberColor
            textSize = 40f
            textAlign = Paint.Align.CENTER
        }
        circleMovable = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = circle
        }
        handlerPaint = Paint().apply {
            color = circle
            strokeWidth = handlerWidth
            isAntiAlias = true
        }
        invalidate()
    }
    //---------------------------------------------------------------------------------------------- setStrokeStyle


    //---------------------------------------------------------------------------------------------- onSizeChanged
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        centerX = (w / 2).toFloat()
        centerY = (h / 2).toFloat()
        radius = (min(w, h) / 2 * 0.8).toFloat()
        secondRadius = radius / 1.7f
    }
    //---------------------------------------------------------------------------------------------- onSizeChanged


    //---------------------------------------------------------------------------------------------- onDraw
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawCircle(centerX, centerY, radius, circlePaint)

        if (isHourMode)
            canvas.drawCircle(centerX, centerY, secondRadius, secondCirclePaint)

        val steps = if (isHourMode) 12 else 60
        val stepAngle = 360f / steps

        for (i in 0 until steps) {
            val angleRad = Math.toRadians((i * stepAngle - 90).toDouble())
            val hourLineLength = 30
            val paint = if (!isHourMode && i % 5 != 0) tickPaintMinor else tickPaintMain

            // دایرع بیرونی
            val hourLineStartX = (centerX + cos(angleRad) * (radius - hourLineLength)).toFloat()
            val hourLineStartY = (centerY + sin(angleRad) * (radius - hourLineLength)).toFloat()
            val hourLineEndX = (centerX + cos(angleRad) * radius).toFloat()
            val hourLineEndY = (centerY + sin(angleRad) * radius).toFloat()
            canvas.drawLine(hourLineStartX, hourLineStartY, hourLineEndX, hourLineEndY, paint)
            if (isHourMode || i % 5 == 0) {
                val textX = (centerX + cos(angleRad) * (radius - 60)).toFloat()
                val textY = (centerY + sin(angleRad) * (radius - 60) + 15).toFloat()
                val textValue = if (isHourMode) {
                    (if (i == 0) 12 else i) + 12
                } else {
                    i
                }
                canvas.drawText(textValue.toString(), textX, textY, textPaint)
            }

            // دایرع داخلی
            if (isHourMode) {
                val hourSecondLineStartX = (centerX + cos(angleRad) * (secondRadius - hourLineLength / 1.5)).toFloat()
                val hourSecondLineStartY = (centerY + sin(angleRad) * (secondRadius - hourLineLength / 1.5)).toFloat()
                val hourSecondLineEndX = (centerX + cos(angleRad) * secondRadius).toFloat()
                val hourSecondLineEndY = (centerY + sin(angleRad) * secondRadius).toFloat()
                canvas.drawLine(hourSecondLineStartX, hourSecondLineStartY, hourSecondLineEndX, hourSecondLineEndY, paint)
                if (isHourMode || i % 5 == 0) {
                    val textX = (centerX + cos(angleRad) * (secondRadius - 60)).toFloat()
                    val textY = (centerY + sin(angleRad) * (secondRadius - 60) + 15).toFloat()
                    val textValue = if (i == 0) 12 else i
                    canvas.drawText(textValue.toString(), textX, textY, textPaint)
                }
            }
        }


        // دایره روی خط
        val handleRadius = if (isHourMode && isMoveOnSecondCircle) secondRadius else radius
        if (!isHourMode)
            resetMinute()
        val handleX = (centerX + cos(Math.toRadians(handleAngle)) * handleRadius).toFloat()
        val handleY = (centerY + sin(Math.toRadians(handleAngle)) * handleRadius).toFloat()
        canvas.drawCircle(handleX, handleY, 25f, circleMovable)
        canvas.drawCircle(centerX, centerY, 8f, circleMovable)
        canvas.drawLine(centerX, centerY, handleX, handleY, handlerPaint)
        onTimeSelected?.invoke(selectedHour, selectedMinute)

    }
    //---------------------------------------------------------------------------------------------- onDraw



    //---------------------------------------------------------------------------------------------- onTouchEvent
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_DOWN -> {
                val dx = event.x - centerX
                val dy = event.y - centerY
                val distance = sqrt(dx*dx + dy*dy)
                isMoveOnSecondCircle = (abs(distance - secondRadius) < abs(distance - radius))

                var angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble()))
                if (angle < 0) angle += 360.0

                val steps = if (isHourMode) 12 else 60
                val stepAngle = 360.0 / steps

                // 🔹 قفل کردن روی نزدیک‌ترین Step
                val snapped = ((angle + 90) % 360 / stepAngle).roundToInt() * stepAngle
                handleAngle = (snapped - 90 + 360) % 360

                val index = ((snapped) / stepAngle).toInt() % steps
                val result = if (isHourMode && index == 0) 12 else index

                if (isHourMode) {
                    selectedHour = if (isMoveOnSecondCircle) result else result + 12
                } else {
                    selectedMinute = index
                }

                onTimeSelected?.invoke(selectedHour, selectedMinute)
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                if (isHourMode)
                    isHourMode = false

                invalidate()
            }
        }
        return true
    }
//---------------------------------------------------------------------------------------------- onTouchEvent


    //---------------------------------------------------------------------------------------------- resetToHourMode
    fun resetToHourMode() {
        isHourMode = true
        val hourForAngle = if (selectedHour > 12) selectedHour - 12 else selectedHour
        isMoveOnSecondCircle = selectedHour <= 12
        val steps = 12
        val stepAngle = 360.0 / steps
        handleAngle = (hourForAngle % 12) * stepAngle - 90.0
        invalidate()
    }
    //---------------------------------------------------------------------------------------------- resetToHourMode


    //---------------------------------------------------------------------------------------------- resetMinute
    private fun resetMinute() {
        val steps = 60
        val stepAngle = 360.0 / steps
        handleAngle = (selectedMinute % 60) * stepAngle - 90.0
    }
    //---------------------------------------------------------------------------------------------- resetMinute

}



