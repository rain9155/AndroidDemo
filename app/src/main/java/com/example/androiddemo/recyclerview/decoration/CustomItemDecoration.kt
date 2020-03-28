package com.example.androiddemo.recyclerview.decoration

import android.content.Context
import android.graphics.*
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.androiddemo.R

/**
 * 自定义分割线:
 * ps1:
 * 需要注意的是,getItemOffsets是针对每个Item都会走一次，也就是说每个Item的outRect都可以不同,
 * 但是onDraw和onDrawOver对所有ItemDecoration只执行一次的，并不是针对Item的，
 * 所以我们需要在onDraw和onDrawOver中绘图时，一次性将所有Item的ItemDecoration绘制完成，
 * 从上面也可以看出，这里在onDraw函数中绘图时，通过for循环对每一个item画上一个绿色圆。
 * ps2:
 * 因为
 * RecyclerView的绘制流程函数，ItemDecoration#onDraw（） -> Item#OnDraw() -> ItemDecoration#OnDrawOver()，
 * 所以
 * 在ItemDecoration的onDraw函数中绘制的内容，当超出边界时，会被Item所覆盖，
 * 在onDrawOver中绘制的内容就不受outRect边界的限制，可以覆盖Item的区域显示。
 */
class CustomItemDecoration(context: Context) : ItemDecoration() {

    private val mPaint =  Paint(Paint.ANTI_ALIAS_FLAG)
    private val mBitmap: Bitmap


    init {
        mPaint.color = Color.BLUE
        mBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.image)
    }

    /**
     * 给item的四周加上边距，实现的效果类似于margin，将item的四周撑开一些距离
     * @param outRect outRect就是表示在item的上下左右所撑开的距离
     * @param view    指当前Item的View对象
     * @param parent  是指RecyclerView 本身
     * @param state   当前RecyclerView的状态,也可以通过State在RecyclerView各组件间传递参数
     */
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = 100
        outRect.bottom = 1
    }

    /**
     * 使用getItemOffsets后，就可以利用onDraw函数，在这个距离上进行绘图了
     * @param c 是指通过getItemOffsets撑开的空白区域所对应的画布，
     * 通过这个canvas对象，可以在getItemOffsets所撑出来的区域任意绘图。
     */
    override fun onDraw(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.onDraw(c, parent, state)
        //动态获取item的outRect的left, right, top, bottom的值
        //int left = manager.getLeftDecorationWidth(child);
        //int top = manager.getTopDecorationHeight(child);
        //int right = manager.getRightDecorationWidth(child);
        //int bottom = manager.getBottomDecorationHeight(child);
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            //动态获取item的outRect的left值
            val itemLeftMargin = parent.layoutManager!!.getLeftDecorationWidth(child)
            val cx = itemLeftMargin / 4 * 3
            val cy = child.top + child.height / 2
            c.drawCircle(cx.toFloat(), cy.toFloat(), 20f, mPaint)
        }
    }

    /**
     * ItemDecoration#onDraw（） -> Item#OnDraw() -> ItemDecoration#OnDrawOver()，
     * 在此绘制的内容可以覆盖在item和itemDecoration上面
     */
    override fun onDrawOver(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.onDrawOver(c, parent, state)
        //第一个item画蒙版
        val view = parent.getChildAt(0)
        val linearGradient = LinearGradient(
            0f, parent.height / 2f,
            parent.layoutManager!!.getLeftDecorationWidth(view).toFloat(), parent.height / 2f,
            -0xffff01, 0x000000ff,
            Shader.TileMode.CLAMP
        )
        mPaint.shader = linearGradient
        c.drawRect(
            0f, 0f,
            parent.layoutManager!!.getLeftDecorationWidth(view).toFloat(), parent.height.toFloat(),
            mPaint
        )
        //每个5个item画一个勋章
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val indexInAdapter = parent.getChildAdapterPosition(child)
            if (indexInAdapter % 5 == 0) {
                val itemLeftMargin = parent.layoutManager!!.getLeftDecorationWidth(child)
                val bitmapWidth = mBitmap.width
                val cx = itemLeftMargin - bitmapWidth / 2
                val cy = child.top
                c.drawBitmap(mBitmap, cx.toFloat(), cy.toFloat(), mPaint)
            }
        }
    }
}