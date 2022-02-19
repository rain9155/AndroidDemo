package com.example.androiddemo.customview.xfermode

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.androiddemo.R

/**
 * Xfermode实例1：用SRC_OUT实现橡皮檫效果
 * Created by ASUS on 2018/6/23.
 */
class RubberView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var mSrcBitmap = BitmapFactory.decodeResource(resources, R.drawable.dog)
    private var mDstBitmap = Bitmap.createBitmap(mSrcBitmap.width, mSrcBitmap.height, Bitmap.Config.ARGB_8888)
    private var mPaint: Paint = Paint()
    private var mPath = Path()
    private var mPreX = 0f
    private var mPreY = 0f

    init {
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 80f
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        val layerId = canvas.saveLayer(
            0f, 0f,
            width.toFloat(), height.toFloat(),
            null,
            Canvas.ALL_SAVE_FLAG
        )

        //先把手指轨迹画到目标Bitmap上
        val c = Canvas(mDstBitmap)
        c.drawPath(mPath, mPaint)
        //然后把目标图像画到画布上
        canvas.drawBitmap(mDstBitmap, 0f, 0f, mPaint)
        //给画笔添加SRC_OUT模式，计算源图像显式区域
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        canvas.drawBitmap(mSrcBitmap, 0f, 0f, mPaint)
        mPaint.xfermode = null

        canvas.restoreToCount(layerId)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPath.moveTo(event.x, event.y)
                mPreX = event.x
                mPreY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = (event.x + mPreX) / 2
                val endY = (event.y + mPreY) / 2
                mPath.quadTo(mPreX, mPreY, endX, endY)
                mPreX = event.x
                mPreY = event.y
                postInvalidate()
            }
            MotionEvent.ACTION_UP -> {
            }
            else -> {
            }
        }
        return true
    }
}