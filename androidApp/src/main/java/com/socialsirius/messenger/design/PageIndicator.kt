package com.socialsirius.messenger.design

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.socialsirius.messenger.R


class PageIndicator(context: Context, attrs: AttributeSet)
    : View(context, attrs) {

    companion object {
        private const val DEFAULT_COLOR = 0xff000000.toInt()
        private const val DEFAULT_SIZE = 10F
    }

    private val paint = Paint().apply { isAntiAlias = true }
    private val argbEvaluator = ArgbEvaluator()

    private var selectedPosition = 0
    private var selectedPositionOffset = 0F
    private var pagesCount = 0

    private var indicatorColor = 0
    private var indicatorSelectedColor = 0
    private var indicatorSize = 0F
    private var indicatorSelectedSize = 0F
    private var indicatorSpacing = 0F

    init {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.PageIndicator)
        indicatorColor = arr.getColor(R.styleable.PageIndicator_indicatorColor, DEFAULT_COLOR)
        indicatorSelectedColor = arr.getColor(R.styleable.PageIndicator_indicatorSelectedColor, DEFAULT_COLOR)
        indicatorSize = arr.getDimension(R.styleable.PageIndicator_indicatorSize, DEFAULT_SIZE)
        indicatorSelectedSize = arr.getDimension(R.styleable.PageIndicator_indicatorSelectedSize, DEFAULT_SIZE * 2) - indicatorSize
        indicatorSpacing = arr.getDimension(R.styleable.PageIndicator_indicatorSpacing, DEFAULT_SIZE)
        arr.recycle()
    }


    override fun onDraw(canvas: Canvas) {
        val posTop = (height - indicatorSize) / 2
        val posBottom = (height + indicatorSize) / 2
        val cornerRadius = indicatorSize / 2

        var dx = (width - ((pagesCount - 1) * (indicatorSize + indicatorSpacing) + indicatorSelectedSize) )/ 2

        for(index in 0 until pagesCount) {

            val (size, color) = when (index) {
                selectedPosition ->
                    indicatorSize + indicatorSelectedSize * (1F - selectedPositionOffset) to argbEvaluator.evaluate(selectedPositionOffset, indicatorSelectedColor, indicatorColor) as Int
                selectedPosition.plus(1) ->
                    indicatorSize + indicatorSelectedSize * selectedPositionOffset to argbEvaluator.evaluate(selectedPositionOffset, indicatorColor, indicatorSelectedColor) as Int
                else -> indicatorSize to indicatorColor
            }

            paint.color = color
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                canvas.drawRoundRect(dx, posTop, dx + size, posBottom, cornerRadius, cornerRadius, paint)
            }else{
                //canvas.drawRoundRect(dx, posTop, dx + size, posBottom, cornerRadius, cornerRadius, paint)
            }

            dx += size + indicatorSpacing
        }
    }

    fun attachViewPager(viewPager: ViewPager2) {
        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                selectedPosition = position
                selectedPositionOffset = positionOffset
                pagesCount = viewPager.adapter!!.itemCount
                invalidate()
            }
        })
    }
}