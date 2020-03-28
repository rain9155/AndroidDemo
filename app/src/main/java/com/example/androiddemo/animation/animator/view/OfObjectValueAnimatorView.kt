package com.example.androiddemo.animationtest.animator.view

import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.BounceInterpolator

/**
 * ValueAnimator示例:
 * public static ValueAnimator ofObject(TypeEvaluator evaluator, Object... values)使用示例
 * Created by ASUS on 2018/6/29.
 */
class OfObjectValueAnimatorView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var mPoint: Point? = null
    private var mPaint =  Paint(Paint.ANTI_ALIAS_FLAG)
    private val mValueAnimator: ValueAnimator by lazy {
        //ValueAnimator的ofObject方法使用
        ValueAnimator.ofObject(
            MyEvaluator(),
            Point(100f),
            Point(200f)
        )
    }

    init {
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(
            width / 2.toFloat(),
            height / 2.toFloat(),
            mPoint?.radius?: 0f,
        mPaint)
    }

    fun doAnimator() {
        if(mValueAnimator.isRunning){
            mValueAnimator.cancel()
        }
        mValueAnimator.run {
            duration = 1000
            interpolator = BounceInterpolator()
            addUpdateListener{ animation ->
                //更新Point
                mPoint = animation.animatedValue as Point
                invalidate()
            }
        }
        mValueAnimator.start()
    }

    /**
     * 自定义Evaluator，计算动画过程
     */
    private inner class MyEvaluator : TypeEvaluator<Point> {

        override fun evaluate(
            fraction: Float,//动画进度
            startValue: Point,//动画起始值
            endValue: Point//动画结束值
        ): Point {
            //根据动画进度fraction，计算出当前动画值
            val radius: Float = startValue.radius + fraction * (endValue.radius - startValue.radius)
            return Point(radius)
        }
    }
}