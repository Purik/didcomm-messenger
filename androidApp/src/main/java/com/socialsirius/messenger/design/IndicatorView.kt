package com.socialsirius.messenger.design

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.socialsirius.messenger.R


private const val DEFAULT_ALL_NUMBER = 6
private const val DEFAULT_VALUE = 0

class IndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var emptyIndicatorPaint: Paint
    private lateinit var filledIndicatorPaint: Paint
    private var indicatorStartX: Float = 0f
    private var indicatorStartY: Float = 0f

    private var filledColor: Int = 0
    private var errorColor: Int = 0
    var countFilled: Int = 0
        private set
    private var indicatorPadding: Float = 0f
    private var emptyIndicatorSize: Float = 0f
    private var indicatorStroke: Float = 0f
    private var totalIndicatorWidth: Float = 0f
    var countIndicatorAll: Int = 0

    init {
        val values = context.theme.obtainStyledAttributes(attrs,
            R.styleable.IndicatorView, defStyleAttr, 0)

        filledColor = ContextCompat.getColor(context, R.color.defaultColor)
        errorColor = ContextCompat.getColor(context, R.color.errorColor)
        countFilled = DEFAULT_VALUE
        countIndicatorAll = values.getInteger(R.styleable.IndicatorView_ivCountAll, DEFAULT_ALL_NUMBER)
        indicatorPadding = resources.getDimension(R.dimen.common_gap_16)
        emptyIndicatorSize = resources.getDimension(R.dimen.common_gap_8)
        indicatorStroke = resources.getDimension(R.dimen.common_gap_1)
        totalIndicatorWidth = emptyIndicatorSize + indicatorPadding + (indicatorStroke * 2)

        values.recycle()
        preparePaint()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        computeMeasureDimension(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawIndicators(canvas)
    }

    fun setupError() {
        emptyIndicatorPaint.color = errorColor
        clearAll()
    }

    fun setupDefaultColor() {
        emptyIndicatorPaint.color = filledColor
        invalidate()
    }

    fun setupNumberFilled(count: Int) {
        if (count <= countIndicatorAll) {
            countFilled = count
            invalidate()
        }
    }

    fun clearAll() {
        countFilled = 0
        invalidate()
    }

    private fun computeMeasureDimension(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        var measuredWidth = 0
        var measuredHeight = 0
        if (widthMode == View.MeasureSpec.EXACTLY || widthMode == View.MeasureSpec.AT_MOST) {
            measuredWidth = computeTotalWidth().toInt()
        }

        if (heightMode == View.MeasureSpec.EXACTLY) {
            measuredHeight = View.MeasureSpec.getSize(heightMeasureSpec) + (emptyIndicatorSize + indicatorStroke * 2).toInt()
        } else if (heightMode == View.MeasureSpec.AT_MOST) {
            measuredHeight = (emptyIndicatorSize + indicatorStroke * 2).toInt()
        }

        setMeasuredDimension(measuredWidth, measuredHeight)
        computeCircleStartXY()
    }

    private fun computeTotalWidth(): Float {
        val diameterSum = countIndicatorAll * emptyIndicatorSize
        val totalSpace = indicatorPadding * (countIndicatorAll - 1)
        val totalBorder = (indicatorStroke * 2) * countIndicatorAll
        return diameterSum + totalSpace + totalBorder + 8
    }

    private fun computeCircleStartXY() {
        val totalWidth = computeTotalWidth()
        indicatorStartX = measuredWidth / 2 - totalWidth / 2 + indicatorPadding / 2 + indicatorStroke / 2
        indicatorStartY = (measuredHeight / 2).toFloat()
    }

    private fun drawIndicators(canvas: Canvas) {
        var x = indicatorStartX
        val y = indicatorStartY

        for (i in 1..countFilled) {
            canvas.drawCircle(x, y, emptyIndicatorSize / 2, filledIndicatorPaint)
            x += totalIndicatorWidth
        }

        for (i in countFilled..countIndicatorAll) {
            canvas.drawCircle(x, y, emptyIndicatorSize / 2, emptyIndicatorPaint)
            x += totalIndicatorWidth
        }
    }

    private fun preparePaint() {
        emptyIndicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        emptyIndicatorPaint.style = Paint.Style.STROKE
        emptyIndicatorPaint.strokeWidth = indicatorStroke
        emptyIndicatorPaint.color = filledColor

        filledIndicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        filledIndicatorPaint.style = Paint.Style.FILL
        filledIndicatorPaint.strokeWidth = indicatorStroke
        filledIndicatorPaint.color = filledColor
    }

}