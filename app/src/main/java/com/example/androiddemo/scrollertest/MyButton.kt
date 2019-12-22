package com.example.androiddemo.scrollertest

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller
import androidx.appcompat.widget.AppCompatButton

/**
 * 参考：https://blog.csdn.net/guolin_blog/article/details/48719871
 * 使用Scroller：
 * Scroller就是可以平滑实现scrollTo和scrollBy方法的工具类
 * 不管scrollTo还是scrollBy方法，它们移动的都是View中的内容而不是View本身
 * 正值表示向左或向上移动，负值表示向右或向下移动
 * 使用步骤：
 * 1、初始化Scroller
 * 2、重写computeScroll（），实现模拟滑动
 * 3、startScroll（）开启模拟过程
 * Created by ASUS on 2018/6/14.
 */
class MyButton(context: Context?, attrs: AttributeSet?) : AppCompatButton(context, attrs) {

    companion object{
        private val TAG = MyButton::class.java.simpleName
    }

    /* 1、初始化Scroller */
    private val mScroller: Scroller = Scroller(context)

    var lastX = 0
    var lastY = 0

    /* 2、重写computeScroll（），实现模拟滑动 */
    override fun computeScroll() {
        super.computeScroll()
        //判断Scroll是否执行完毕
        if (mScroller.computeScrollOffset()) {
            (parent as View).scrollTo(mScroller.currX, mScroller.currY)
            //通过不断重绘来调用computeScroll（）
            invalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        val x2 = event.rawX.toInt()
        val y2 = event.rawY.toInt()
        //MyButton的位置会一直不变化
        Log.d(
            TAG,
            "button.getLeft: $left , button.getTop: $top , button.getRight: $right , button.getBottom: $bottom"
        )
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                //纪录触摸点坐标
                lastX = x
                lastY = y
            }
            MotionEvent.ACTION_UP -> {
                /* 3、startScroll（）开启模拟过程 */
                val view = parent as View
                mScroller.startScroll(view.scrollX, view.scrollY, -view.scrollX, -view.scrollY)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                val offsetX = x - lastX
                val offsetY = y - lastY
                (parent as View).scrollBy(-offsetX, -offsetY)
            }
            else -> {
            }
        }
        return super.onTouchEvent(event)
    }


}