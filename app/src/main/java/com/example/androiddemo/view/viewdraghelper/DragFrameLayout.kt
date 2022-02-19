package com.example.androiddemo.view.viewdraghelper

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper

/**
 * 源码解析：https://www.cnblogs.com/lqstayreal/p/4500219.html
 * 使用ViewDragHelper实现侧滑:
 * 1、处理Callback接口
 * 2、初始化ViewDragHelper
 * 3、ViewDragHelper拦截事件
 * 4、将触摸事件传递给ViewDragHelper
 * 5、重写computeScorll（）
 * Created by ASUS on 2018/6/15.
 */
class DragFrameLayout(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var mMenuView: View? = null
    private var mMainView: View? = null
    private var mWidth = 0

    /* 1、处理Callback接口 */
    private val callback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {

        //何时开始检测事件
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child === mMainView
        }

        //开启水平滑动，left表示水平方向上child移动的距离
        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return left
        }

        //开启垂直滑动，top表示垂直方向上child移动的距离
        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return 0
        }

        //拖动结束后调用
        override fun onViewReleased(
            releasedChild: View,
            xvel: Float,
            yvel: Float) {

            super.onViewReleased(releasedChild, xvel, yvel)

            //手指抬起后，缓慢移动到指定位置
            if (mMainView!!.left < 500) { //关闭菜单
                //相当于Scroller的stratScroll（）
                mViewDragHelper.smoothSlideViewTo(mMainView!!, 0, 0)
                ViewCompat.postInvalidateOnAnimation(this@DragFrameLayout)
            } else { //打开菜单
                mViewDragHelper.smoothSlideViewTo(mMainView!!, 300, 0)
                ViewCompat.postInvalidateOnAnimation(this@DragFrameLayout)
            }
        }
    }

    /* 2、初始化ViewDragHelper */
    private var mViewDragHelper  = ViewDragHelper.create(this, callback)

    /* 3、ViewDragHelper拦截事件 */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return mViewDragHelper.shouldInterceptTouchEvent(ev)
    }

    /* 4、将触摸事件传递给ViewDragHelper,此操作必不可少 */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mViewDragHelper.processTouchEvent(event)
        return true
    }

    /* 5、重写computeScorll（） */
    override fun computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    //加载完布局文件后调用,获得子控件实例
    override fun onFinishInflate() {
        super.onFinishInflate()
        mMenuView = getChildAt(0)
        mMainView = getChildAt(1)
    }

    //组件大小改变时调用，获得子控件大小
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = mMenuView!!.measuredWidth
    }

}