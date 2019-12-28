package com.example.androiddemo.viewtest.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Canvas的相关函数：
 * 1、Canvas.scale(float sx, float sy) --- 缩放（scale ）
 * float sx --- 水平方向伸缩的比例，sx为小数为缩小，sx为整数为放大
 * float sy --- 垂直方向伸缩的比例，同样，小数为缩小，整数为放大
 *
 * 2、Canvas.skew(float sx, float sy) --- 扭曲,错切（skew）
 * float sx --- 将画布在x方向上倾斜相应的角度，sx倾斜角度的tan值
 * float sy --- 将画布在y轴方向上倾斜相应的角度，sy为倾斜角度的tan值
 *
 * 3、裁剪画布（clip系列函数）
 *
 * 4、Canvas.translate(float dx, float dy)----平移坐标系
 * float dx --- 水平方向平移的距离，正数指向正方向（向右）平移的量，负数为向负方向（向左）平移的量
 * float dy --- 垂直方向平移的距离，正数指向正方向（向下）平移的量，负数为向负方向（向上）平移的量
 *
 * 5、Canvas.rotate(float degrees, float px, float py)----旋转坐标系
 * float degrees --- 旋转的度数
 * float px, float py --- 旋转的中心点坐标
 *
 * 6、Canvas.save()----保存画布，使之前已绘制的图像保存起来，值得后续操作好像在新图层上操作一样
 *
 * 7、Canvas.restore（）----将Canvas.save()之前绘制的图像和Canvas.save()之后绘制的图像进行合并
 */

/**
 * 使用Canvas画一个仪表盘
 * Created by ASUS on 2018/6/16.
 */
class CanvasView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    //仪表盘的形状画笔
    var paintCircle = Paint(Paint.ANTI_ALIAS_FLAG) //Paint.ANTI_ALIAS_FLAG,抗锯齿，使边缘清晰
    //仪表盘的刻度画笔
    var paintScale = Paint(Paint.ANTI_ALIAS_FLAG)
    var paintScaleValue =  Paint(Paint.ANTI_ALIAS_FLAG)
    //时针画笔
    var paintHour = Paint(Paint.ANTI_ALIAS_FLAG)
    //分针画笔
    var paintMinute = Paint(Paint.ANTI_ALIAS_FLAG)
    //秒针画笔
    var paintSecond = Paint(Paint.ANTI_ALIAS_FLAG)
    //周期
    var s = 0

    init {
        paintCircle.style = Paint.Style.STROKE
        paintCircle.strokeWidth = 5f
        paintHour.strokeWidth = 30f
        paintMinute.strokeWidth = 10f
        paintSecond.strokeWidth = 5f
    }

    override fun onDraw(canvas: Canvas) {
        if (s == 60) {
            s = 0
        }
        s++

        /* 画外圆 */
        canvas.drawCircle(
            measuredWidth / 2.toFloat(),
            measuredHeight / 2.toFloat(),
            measuredWidth / 2.toFloat(),
            paintCircle
        )

        /* 画刻度 */
        for (i in 60 downTo 1) {
            if (i % 5 == 0) {
                paintScale.strokeWidth = 10f
                paintScaleValue.textSize = 50f
                canvas.drawLine(
                    measuredWidth / 2.toFloat(), measuredHeight / 2 - measuredWidth / 2.toFloat(),
                    measuredWidth / 2.toFloat(), measuredHeight / 2 - measuredWidth / 2 + 60.toFloat(),
                    paintScale
                )
                val value = (i / 5).toString()
                canvas.drawText(
                    value,
                    measuredWidth / 2 - paintScaleValue!!.measureText(value) / 2,
                    measuredHeight / 2 - measuredWidth / 2 + 100.toFloat(),
                    paintScaleValue
                )
            } else {
                paintScale.strokeWidth = 3f
                canvas.drawLine(
                    measuredWidth / 2.toFloat(), measuredHeight / 2 - measuredWidth / 2.toFloat(),
                    measuredWidth / 2.toFloat(), measuredHeight / 2 - measuredWidth / 2 + 30.toFloat(),
                    paintScale
                )
            }
            //通过旋转画布来简化运算，指定旋转中心
            canvas.rotate(-6f, measuredWidth / 2.toFloat(), measuredHeight / 2.toFloat())
        }
        canvas.save()
        //平移画布
        canvas.translate(measuredWidth / 2.toFloat(), measuredHeight / 2.toFloat())
        paintCircle.style = Paint.Style.FILL
        canvas.drawCircle(0f, 0f, 30f, paintCircle)
        //画指针
        canvas.drawLine(0f, 0f, 200f, 0f, paintHour)
        canvas.drawLine(0f, 0f, 300f, 90f, paintMinute)
        canvas.drawLine(0f, 0f, measuredWidth / 2 - 90.toFloat(), 180f, paintSecond)
        canvas.restore()
    }
}