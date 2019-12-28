package com.example.androiddemo.viewtest.xfermode

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.example.androiddemo.R

/**
 * Xfermode实例2：用SRC_IN实现圆角图片的效果
 * Created by ASUS on 2018/6/23.
 */
class RoundRectImageView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var mDstBitmap =  BitmapFactory.decodeResource(resources, R.drawable.dog_bg)
    private var mSrcBitmap = BitmapFactory.decodeResource(resources, R.drawable.dog)
    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        val layerID = canvas.saveLayer(
            0f, 0f,
            width.toFloat(), height.toFloat(),
            null,
            Canvas.ALL_SAVE_FLAG
        )

        //画目标图像，一个圆角矩形
        canvas.drawRoundRect(
            0f, 0f,
            mSrcBitmap.width.toFloat(), mSrcBitmap.height.toFloat(),
            60f, 60f,
            mPaint)
        //给画笔添加SRC_IN模式，计算源图像的显式区域
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(mSrcBitmap, 0f, 0f, mPaint)
        mPaint.xfermode = null

        canvas.restoreToCount(layerID)
    }
}