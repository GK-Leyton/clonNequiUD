package com.example.nequi_proyecto_componentes

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View.MeasureSpec

class RingProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var progress = 0
    private var maxProgress = 100
    private var ringWidth = 8f
    private var ringColor = Color.WHITE
    private var backgroundColor = Color.parseColor("#D80081") // Color hexadecimal
    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    private val rectF = RectF()

    init {
        // Aquí puedes cargar los atributos del XML si es necesario
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()
        val radius = Math.min(width, height) / 2 - ringWidth / 2

        rectF.set(ringWidth / 2, ringWidth / 2, width - ringWidth / 2, height - ringWidth / 2)

        // Fondo del anillo
        paint.color = backgroundColor
        paint.strokeWidth = ringWidth
        canvas.drawArc(rectF, 0f, 360f, false, paint)

        // Anillo de progreso
        paint.color = ringColor
        val sweepAngle = 360f * progress / maxProgress
        canvas.drawArc(rectF, -90f, sweepAngle, false, paint)
    }

    fun setProgress(progress: Int) {
        this.progress = progress
        invalidate() // Redibuja el anillo con el nuevo progreso
    }

    fun getProgress(): Int = progress

    fun setMaxProgress(maxProgress: Int) {
        this.maxProgress = maxProgress
        invalidate() // Redibuja el anillo si se cambia el máximo progreso
    }



    fun getMaxProgress(): Int = maxProgress
}
