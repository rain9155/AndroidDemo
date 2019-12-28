package com.example.androiddemo.viewtest.layer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * 图层
 * Created by ASUS on 2018/6/16.
 */
@Deprecated("android版本 >= P, Canvas中的saveFlags只剩下ALL_SAVE_FLAG, 其他都丢弃")
class LayerView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val mPaint = Paint()

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)

        //canvas.saveLayerAlpha(0f, 0f, 400f, 400f, 127, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG)
        mPaint.color = Color.BLUE
        canvas.drawCircle(150f, 150f, 100f, mPaint)

        //canvas.saveLayerAlpha(0f, 0f, 400f, 400f, 127, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG)
        mPaint.color = Color.RED
        canvas.drawCircle(200f, 200f, 100f, mPaint)
        canvas.restore()
    }
}