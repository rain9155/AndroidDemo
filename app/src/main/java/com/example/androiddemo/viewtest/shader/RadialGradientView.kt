package com.example.androiddemo.viewtest.shader

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * 参考：https://blog.csdn.net/harvic880925/article/details/52653811
 * 画笔特效之RadialGradient:
 * 通过Paint对画布进行放射渐变填充
 * 1、两色渐变
 * RadialGradient(float centerX, float centerY, float radius, int centerColor, int edgeColor, Shader.TileMode tileMode)
 * centerX --- 渐变中心点X坐标
 * centerY --- 渐变中心点Y坐标
 * radius --- 渐变半径
 * centerColor --- 渐变的起始颜色，即渐变中心点的颜色,取值类型必须是八位的0xAARRGGBB色值！透明底Alpha值不能省略，不然不会显示出颜色
 * edgeColor --- 渐变结束时的颜色，即渐变圆边缘的颜色，同样，取值类型必须是八位的0xAARRGGBB色值
 * TileMode --- 与我们前面讲的各个Shader一样，用于指定当控件区域大于指定的渐变区域时，空白区域的颜色填充方式
 * 2、多色渐变
 * RadialGradient(float centerX, float centerY, float radius, int[] colors, float[] stops, Shader.TileMode tileMode)
 * centerX --- 渐变中心点X坐标
 * centerY --- 渐变中心点Y坐标
 * radius --- 渐变半径
 * colors --- 表示所需要的渐变颜色数组
 * stops --- 表示每个渐变颜色所在的位置百分点，取值0-1
 * Created by ASUS on 2018/6/25.
 */
/**
 * 用RadialGradient写出彩色文字
 * Created by ASUS on 2018/6/24.
 */
class RadialGradientView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var mPaint = Paint()

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mPaint.textSize = 100f
        mPaint.textAlign = Paint.Align.CENTER
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)


        val colors = intArrayOf(-0x10000, -0xff0100, -0xffff01, -0x100, -0xff0001)
        val pos = floatArrayOf(0f, 0.2f, 0.4f, 0.6f, 1.0f)
        //给画笔添加RadialGradient
        mPaint.shader = RadialGradient(
            width / 2f, height / 2f,
            600f,
            colors,
            pos,
            Shader.TileMode.CLAMP
        )

//        mPaint.shader = RadialGradient(
//            width / 2f, height / 2f,
//            150f,
//            Color.BLACK,
//            Color.WHITE,
//            Shader.TileMode.CLAMP)

    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText("陈健宇", width / 2.toFloat(), height / 2.toFloat(), mPaint)
    }
}