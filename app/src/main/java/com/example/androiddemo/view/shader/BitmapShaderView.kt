package com.example.androiddemo.view.shader

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.androiddemo.R

/**
 * 参考：https://blog.csdn.net/harvic880925/article/details/52039081
 * 画笔特效之BitmapShader：
 * 通过Paint对画布进行Bitmap填充,先填充y轴，再填充x轴
 * 有以下三种模式：
 * TileMode.CLAMP（拉伸） --- 用边缘色彩填充多余空间
 * TileMode.REPEAT（重复）--- 重复原图像来填充多余空间
 * TileMode.MIRROR（镜像）--- 重复使用镜像模式的图像来填充多余空间
 * 使用以下构造：
 * public BitmapShader(Bitmap bitmap, TileMode tileX, TileMode tileY)
 * bitmap --- 用来指定图案
 * tileX --- 用来指定当X轴超出单个图片大小时时所使用的重复策略
 * tileY --- 用于指定当Y轴超出单个图片大小时时所使用的重复策略
 */

/**
 * 使用BitmapShader实现望远镜效果
 * Created by ASUS on 2018/6/23.
 */
class BitmapShaderView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    val bgBitmap: Bitmap by lazy {
        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    }
    var paint = Paint()
    var preX = -1f
    var preY = -1f


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        //把照片拉伸到和控件一样大小
        val sceneryBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.dog)
        val c = Canvas(bgBitmap)
        c.drawBitmap(
            sceneryBitmap,
            null,
            Rect(0, 0, width, height),
            paint)

        //给画笔添加BitmapShader
        paint.shader = BitmapShader(bgBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    }

    override fun onDraw(canvas: Canvas) {
        if (preX != -1f && preY != -1f) {
            canvas.drawCircle(preX, preY, 300f, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                preX = event.x
                preY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                preX = event.x
                preY = event.y
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                preY = -1f
                preX = -1f
            }
            else -> {
            }
        }
        postInvalidate()
        return true
    }
}