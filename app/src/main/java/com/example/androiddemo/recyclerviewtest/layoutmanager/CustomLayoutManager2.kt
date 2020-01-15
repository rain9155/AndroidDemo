package com.example.androiddemo.recyclerviewtest.layoutmanager

import android.graphics.Rect
import android.util.SparseBooleanArray
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

/**
 * 自定义LayoutManager实现回收复用方式2：（与第一种方法不同的是没有使用offsetChildrenVertical（dy）来移动item，而是通过再重新布局所有item的方法来实现移动item，）
 * 滚动item后，越界的item回收复用并标志没有被布局，
 * 在当前屏幕中没有越界的item重新布局到滚动后的位置并表示已经被重新布局，
 * 然后再通过是否布局标示用屏幕外的item填充滚动后的空白区域。
 * Created by 陈健宇 at 2019/1/5
 */
class CustomLayoutManager2 : CustomLayoutManager() {

    private val mAttachedItems = SparseBooleanArray() //标示item是否被重新布局

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)
        //初始化mAttachedItems，都设置为false，表示所有item都没有被重新布局
        mAttachedItems.clear()
        for (i in 0 until itemCount)
            mAttachedItems.put(i, false)
    }

    override fun scrollVerticallyBy(dy: Int, recycler: Recycler, state: RecyclerView.State): Int {
        var tempY = dy

        if (mScrollY + dy < 0) //判断滑动到顶部，当mSumConsumeY + dy < 0 时, rv到达顶部
            tempY = -mScrollY
        else if (mScrollY + dy > mItemTotalHeigth - recyclerViewHeight) //判断滑动到底部
            tempY = mItemTotalHeigth - recyclerViewHeight - mScrollY

        //获得滚动item后的空白区域
        val visibleRect = getVisibleArea(tempY)

        //先将移动距离mScrollY进行了累加,后面重新布局item
        mScrollY += tempY

        //判断item是否越界
        for (i in childCount - 1 downTo 0) {
            val view = getChildAt(i)
            val position = getPosition(view!!)
            val rect = mItemRects[position]
            //回收越界item并把布局标志设为false
            if (!Rect.intersects(rect, visibleRect)) {
                removeAndRecycleView(view, recycler)
                mAttachedItems.put(position, false)
            } else { //重新布局当前屏幕中滚动后的item位置，并把布局标志设为true
                layoutDecoratedWithMargins(
                    view,
                    rect.left,
                    rect.top - mScrollY,
                    rect.right,
                    rect.bottom - mScrollY
                )
                mAttachedItems.put(position, true)
            }
        }

        //先得到当前在显示的第一个item和最后一个item的索引
        val lastView = getChildAt(childCount - 1)
        val firstView = getChildAt(0)

        //向上滚动,为滚动后的空白处填充Item
        if (tempY >= 0) {
            //从屏幕可见的第一个item开始遍历
            val minPos = getPosition(firstView!!)
            //顺序addChildView
            for (i in minPos until itemCount) {
                val rect = mItemRects[i]
                //当前item在显示区域内且没有被布局过
                if (Rect.intersects(visibleRect, rect) && !mAttachedItems[i]) {
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
                }
            }
        }
        //向下滚动,为滚动后的空白处填充Item
        if (tempY < 0) {
            //从屏幕可见的最后一个item开始遍历
            val maxPos = getPosition(lastView!!)
            for (i in maxPos downTo 0) {
                val rect = mItemRects[i]
                //当前item在显示区域内且没有被布局过
                if (Rect.intersects(visibleRect, rect) && !mAttachedItems[i]) {
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
                }
            }
        }
        return dy
    }
}