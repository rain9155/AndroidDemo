package com.example.androiddemo.listviewtest

import android.content.Context
import android.util.AttributeSet
import android.widget.ListView

/**
 * 弹性滑动的ListView
 * Created by ASUS on 2018/7/7.
 */
class MyListView(mContext: Context, attrs: AttributeSet?) : ListView(mContext, attrs) {

    private var mMaxOverScrollY = 50 //dp

    init {
        initView()
    }

    /**
     * 重写此方法修改maxOverScrollY的值使得ListView滑到边缘时具有弹性
     * @param maxOverScrollY
     * @return
     */
    override fun overScrollBy(
        deltaX: Int,
        deltaY: Int,
        scrollX: Int,
        scrollY: Int,
        scrollRangeX: Int,
        scrollRangeY: Int,
        maxOverScrollX: Int,
        maxOverScrollY: Int,
        isTouchEvent: Boolean
    ): Boolean {
        var maxOverScrollY = mMaxOverScrollY
        return super.overScrollBy(
            deltaX,
            deltaY,
            scrollX,
            scrollY,
            scrollRangeX,
            scrollRangeY,
            maxOverScrollX,
            maxOverScrollY,
            isTouchEvent
        )
    }

    /**
     * 单位转换，使弹性距离适应不同分辨率
     */
    private fun initView() {
        val metrics = context.resources.displayMetrics
        val density = metrics.density
        mMaxOverScrollY = (density * mMaxOverScrollY).toInt() //dp转px
    }
}