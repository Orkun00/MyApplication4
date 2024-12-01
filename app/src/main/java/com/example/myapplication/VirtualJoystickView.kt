package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.accessibility.AccessibilityEvent
import kotlin.math.hypot

class VirtualJoystickView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val basePaint = Paint().apply {
        color = Color.LTGRAY
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val knobPaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val centerX: Float get() = width / 2f
    private val centerY: Float get() = height / 2f

    private var knobX = centerX
    private var knobY = centerY

    private var listener: ((x: Float, y: Float) -> Unit)? = null

    init {
        isFocusable = true // Enable accessibility focus
    }

    fun setOnMoveListener(listener: (x: Float, y: Float) -> Unit) {
        this.listener = listener
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw joystick base
        canvas.drawCircle(centerX, centerY, width / 3f, basePaint)
        // Draw joystick knob
        canvas.drawCircle(knobX, knobY, width / 6f, knobPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_DOWN -> {
                val dx = event.x - centerX
                val dy = event.y - centerY
                val distance = hypot(dx, dy)
                val radius = width / 3f

                // Ensure knob stays within bounds
                if (distance < radius) {
                    knobX = event.x
                    knobY = event.y
                } else {
                    val ratio = radius / distance
                    knobX = centerX + dx * ratio
                    knobY = centerY + dy * ratio
                }

                // Pass normalized x and y values
                val normalizedX = (knobX - centerX) / radius
                val normalizedY = (knobY - centerY) / radius
                listener?.invoke(normalizedX, normalizedY) // Safely invoke listener
                sendAccessibilityEvent(normalizedX, normalizedY)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                knobX = centerX
                knobY = centerY
                listener?.invoke(0f, 0f) // Reset to center safely
                sendAccessibilityEvent(0f, 0f)
                invalidate()
            }
        }
        return true
    }

    private fun sendAccessibilityEvent(x: Float, y: Float) {
        val accessibilityManager =
            context.getSystemService(Context.ACCESSIBILITY_SERVICE) as android.view.accessibility.AccessibilityManager
        if (accessibilityManager.isEnabled) { // Check if accessibility is enabled
            val event = AccessibilityEvent.obtain().apply {
                eventType = AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED
                contentDescription = "Joystick moved to X: $x, Y: $y"
                packageName = context.packageName
                className = this@VirtualJoystickView.javaClass.name
            }
            try {
                sendAccessibilityEventUnchecked(event)
            } catch (e: Exception) {
                e.printStackTrace() // Log any unexpected errors
            }
        } else {
            println("Accessibility is disabled; skipping accessibility event.")
        }
    }
}
