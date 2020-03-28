package com.example.androiddemo.animationtest.animator.view

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.activity_object_animator.*

/**
 * ObjectAnimator实例:
 * public static ObjectAnimator ofObject(Object target, String propertyName,TypeEvaluator evaluator, Object... values)实例
 * Created by ASUS on 2018/6/30.
 */
class OfObjectObjectAnimatorView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var point: Point? = null
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        mPaint.color = Color.BLUE
        mPaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(
            width / 2.toFloat(),
            height / 2.toFloat(),
            point?.radius?: 0f,
            mPaint)
    }

    fun setPoint(point: Point?) {
        this.point = point
        invalidate()
    }

}