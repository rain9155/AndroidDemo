package com.example.androiddemo.recyclerview.layoutmanager

import android.graphics.Rect
import android.util.SparseArray
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

/**
 * 自定义LayoutManager实现线性布局
 * 一、自定义layoutManger的步骤：
 * 1、重写 RecyclerView.LayoutParams generateDefaultLayoutParams(),返回item的布局参数
 * 2、重写 void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state)，布局item
 * 3、重写canScrollVertically()或canScrollHorizontally(), 使RecyclerView具有滚动功能
 * 4、重写 scrollVerticallyBy(）或scrollHorizontally（）,使RecyclerView滚动, 还要加上一些判断条件
 * 二、为自定义layoutManager添加回收复用(方式1)功能：
 * 1、在onLayoutChildren()中，
 * 先使用detachAndScrapAttachedViews(recycler)将所有的可见HolderView剥离，
 * 然后一屏中能放几个item就获取几个HolderView，撑满初始化的一屏即可，不要多创建
 * 2、在 scrollVerticallyBy(）或scrollHorizontally（）中：
 * 处理滚动，判断滚动要需要回收哪些Item，使用 removeAndRecycleView(view, recycler)回收item
 * 需要新增显示哪些Item，之后再调用offsetChildrenVertical(-dy)实现滚动。
 * Created by 陈健宇 at 2018/12/31
 */
open class CustomLayoutManager : RecyclerView.LayoutManager() {

    protected var mScrollY = 0//垂直方向滚动出RecyclerView的dy = 0
    protected var mItemTotalHeigth = 0 //item总高度 = 0
    protected var mItemWidth = 0
    protected var mItemHeight = 0 //item的宽度，高度 = 0
    protected var mItemRects = SparseArray<Rect>() //item的位置信息

    /**
     * 此方法生成的是RecyclerView的item的LayoutParams
     * 若想要修改item的布局参数，可以在此修改
     * @return RecyclerView的item的LayoutParams
     */
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        //一般的写法, 让item自己决定高宽
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    /**
     * 必须重写此方法, 布局RecyclerView中的item，不然item不会显示出来
     */
    override fun onLayoutChildren(
        recycler: Recycler,
        state: RecyclerView.State
    ) {

        //在Adapter中没有数据的时候，直接将当前所有的Item从屏幕上剥离，将当前屏幕清空
        if (itemCount == 0) {
            detachAndScrapAttachedViews(recycler)
            return
        }

        //将当前所有的Item从屏幕上剥离, 放入mAttachedViews
        detachAndScrapAttachedViews(recycler)

        //向系统申请一个HolderView，然后测量它的宽度、高度，并计算可见的Item数
        val view = recycler.getViewForPosition(0)
        measureChildWithMargins(view, 0, 0)
        //获得Child的高度和宽度(得到的是item+decoration的总宽度和总高度)
        mItemWidth = getDecoratedMeasuredWidth(view)
        mItemHeight = getDecoratedMeasuredHeight(view)

        val visibleCount = recyclerViewHeight / mItemHeight //可见的item数量
        var offerY = 0

        mItemRects.clear()
        //保存所有的item的位置信息
        for (i in 0 until itemCount) {
            val rect = Rect(0, offerY, mItemWidth, mItemHeight + offerY)
            mItemRects.put(i, rect)
            offerY += mItemHeight
        }

        //只对可见的item布局
        for (i in 0 until visibleCount) {
            //获得item的位置信息
            val rect = mItemRects[i]
            //获得item, 并添加进布局
            //recycler.getViewForPosition(i)获取view的顺序：mAttachedViews -> mCachedViews > mViewCacheExtension > mRecyclerPool
            val child = recycler.getViewForPosition(i)
            addView(child)
            //测量Child的尺寸
            measureChildWithMargins(child, 0, 0)
            //每个item摆放在对应的位置
            layoutDecorated(child, rect.left, rect.top, rect.right, rect.bottom)
        }

        //如果所有子View的高度和没有填满RecyclerView的高度，
        // 则将高度设置为RecyclerView的高度
        mItemTotalHeigth = Math.max(
            offerY,
            recyclerViewHeight
        )
    }

    /**
     * 重写此方法，使LayoutManager具有垂直滚动功能
     */
    override fun canScrollVertically(): Boolean {
        return true
    }

    /**
     * 此方法可以接受每次滚动的距离dy，重写此方法使RecyclerView垂直滚动
     * @param dy 表示手指在屏幕上每次滑动的位移
     * 当手指上滑时,dy>0
     * 当手指下滑时,dy<0
     */
    override fun scrollVerticallyBy(dy: Int, recycler: Recycler, state: RecyclerView.State): Int {
        var tempY = dy

        if (mScrollY + dy < 0) //判断滑动到顶部，当mSumConsumeY + dy < 0 时, rv到达顶部
            tempY = -mScrollY
        else if (mScrollY + dy > mItemTotalHeigth - recyclerViewHeight) //判断滑动到底部
            tempY = mItemTotalHeigth - recyclerViewHeight - mScrollY

        //模拟滚动，判断item是否越界
        for (i in childCount - 1 downTo 0) {
            val view = getChildAt(i)
            if (tempY > 0) { //向上滚动，需要回收屏幕上边界越界的item
                if (getDecoratedBottom(view!!) - tempY < 0) { //remove和回收item，放入mCachedViews
                    removeAndRecycleView(view, recycler)
                }
            }
            if (tempY < 0) { //向下滚动，需要回收屏幕下边界越界的item
                if (getDecoratedTop(view!!) - tempY > height - paddingBottom) { //remove和回收item，放入mCachedViews
                    removeAndRecycleView(view, recycler)
                }
            }
        }

        val visibleRect = getVisibleArea(tempY)
        //向上滚动,为滚动后的空白处填充Item
        if (tempY >= 0) { //向上滚动，需要填充屏幕下面的item
        //从屏幕最后一个View+1开始
            val lastView = getChildAt(childCount - 1)
            val minPos = getPosition(lastView!!) + 1
            //顺序addChildView
            for (i in minPos until itemCount) {
                val rect = mItemRects[i]
                if (Rect.intersects(visibleRect, rect)) {
                    val child = recycler.getViewForPosition(i)
                    addView(child)
                    measureChildWithMargins(child, 0, 0)
                    layoutDecorated(
                        child,
                        rect.left,
                        rect.top - mScrollY,
                        rect.right,
                        rect.bottom - mScrollY
                    )
                } else {
                    break
                }
            }
        }
        //向下滚动,为滚动后的空白处填充Item
        if (tempY < 0) {
            val firstView = getChildAt(0)
            //从屏幕第一个View - 1开始
            val maxPos = getPosition(firstView!!) - 1
            for (i in maxPos downTo 0) {
                val rect = mItemRects[i]
                if (Rect.intersects(visibleRect, rect)) {
                    val child = recycler.getViewForPosition(i)
                    //将View添加至RecyclerView中，childIndex为1，但是View的位置还是由layout的位置决定
                    addView(child, 0)
                    measureChildWithMargins(child, 0, 0)
                    layoutDecoratedWithMargins(
                        child,
                        rect.left,
                        rect.top - mScrollY,
                        rect.right,
                        rect.bottom - mScrollY
                    )
                } else {
                    break
                }
            }
        }

        mScrollY += tempY

        //平移容器内的item
        offsetChildrenVertical(-tempY)
        return dy
    }

    /**
     * RecyclerView减去padding后的高度
     */
    protected val recyclerViewHeight by lazy {
        height - paddingBottom - paddingTop
    }

    /**
     * 得到滚动item后RecyclerView在屏幕中的显示区域加上ScrollY的距离的区域
     */
    protected fun getVisibleArea(travel: Int): Rect {
        return Rect(
            paddingLeft,
            paddingTop + mScrollY + travel,
            width + paddingRight,
            recyclerViewHeight + mScrollY + travel
        )
    }
}