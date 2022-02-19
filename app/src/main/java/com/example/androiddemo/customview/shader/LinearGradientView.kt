package com.example.androiddemo.customview.shader

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


/**
 * 参考：https://blog.csdn.net/harvic880925/article/details/52350154，
 * 画笔特效之LinearGradient：
 * 通过Paint对画布进行线性渐变填充
 * 1、俩色渐变：
 * public LinearGradient(float x0, float y0, float x1, float y1,int color0, int color1, TileMode tile)
 * x0,y0 --- 起始渐变点坐标
 * x1,y1 --- 结束渐变点坐标
 * color0 --- 就是起始颜色
 * color1 --- 就是终止颜色
 * tile --- 有TileMode.CLAMP（拉伸），TileMode.REPEAT（重复），TileMode.MIRROR（镜像）三种模式
 * 2、多色渐变
 * public LinearGradient(float x0, float y0, float x1, float y1,int colors[], float positions[], TileMode tile)
 * x0,y0 --- 起始渐变点坐标
 * x1,y1 --- 结束渐变点坐标
 * colors[] --- 用于指定渐变的颜色值数组，颜色值必须使用0xAARRGGBB形式的16进制表示
 * positions[] --- 与渐变的颜色相对应，取值是0-1的float类型，表示在每一个颜色在整条渐变线中的百分比位置
 */

/**
 * 用LinearGradient写出彩色文字
 * Created by ASUS on 2018/6/24.
 */
class LinearGradientView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var mPaint = Paint()

    init {
        mPaint.textSize = 100f
        mPaint.textAlign = Paint.Align.CENTER
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val colors = intArrayOf(-0x10000, -0xff0100, -0xffff01, -0x100, -0xff0001)
        val pos = floatArrayOf(0f, 0.2f, 0.4f, 0.6f, 1.0f)

        //给画笔添加LinearGradient
        mPaint.shader = LinearGradient(
            0f, height / 2f,
            width.toFloat(), height / 2f,
            colors,
            pos,
            Shader.TileMode.REPEAT
        )

//        mPaint.shader = LinearGradient(
//            0f, 0f,
//            width.toFloat(), height.toFloat(),
//            Color.BLACK,
//            Color.WHITE,
//            Shader.TileMode.CLAMP
//        )
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText("陈健宇", width / 2.toFloat(), height / 2.toFloat(), mPaint)
    }

}