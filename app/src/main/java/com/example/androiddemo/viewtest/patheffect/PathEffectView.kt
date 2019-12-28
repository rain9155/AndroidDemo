package com.example.androiddemo.viewtest.patheffect

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * 画笔特效之PathEffect（用各种笔触效果来绘制一个路径）:
 *
 * 1、public CornerPathEffect(float radius) --- 将拐角处变得圆滑
 * radius --- 拐角的圆滑角度
 *
 * 2、public DiscretePathEffect(float segmentLength, float deviation) --- 对原始路径在一个的范围内（范围就是设置的segmentLength 和 deviation）进行无规则的离散
 * segmentLength --- path分隔成segmentLength大小的lines
 * deviation --- 并以deviation为长度进行离散
 * segmentLength 越小 离散效果越明显，deviation 越大 离散效果越明显
 *
 * 3、public DashPathEffect(float intervals[], float phase) --- 绘制虚线
 * intervals[] --- 用来表示虚线的长度的数组，一段虚线最少包括一段实线和一段空白线段，个数必须是偶数个
 * phase --- 偏移量，正数为向左偏移，负数为向右偏移
 *
 * 4、public PathDashPathEffect(Path shape, float advance, float phase, Style style) --- 以Path填充虚线
 * shape --- 要填充的path的形状
 * advance --- 相邻shape之间的间隔大小
 * phase --- 偏移量，正数为向左偏移，负数为向右偏移
 * style --- 有三种值可选：
 * TRANSLATE --- 不改变填充shape的形状和方向，只是平移
 * ROTATE --- 不改变填充shape的形状。旋转shape，改变方向
 * MORPH --- 改变填充shape的形状和方向
 *
 * 5、public ComposePathEffect(PathEffect outerpe, PathEffect innerpe) --- 将以上任意俩种路径特性混合起来
 * outterpe --- 路径特性
 * innetpe --- 路径特性
 *
 * Created by ASUS on 2018/6/26.
 */
class PathEffectView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mPaint: Paint = Paint()
    private var mPaths = arrayListOf<Path>()
    private var mPathEffects : Array<PathEffect>

    init {
        // 填充用shape（三角形） --- PathDashPathEffect
        val shapePath = Path()
        shapePath.moveTo(0f, 0f)
        shapePath.rLineTo(10f, 10f)
        shapePath.rLineTo(-20f, 0f)
        shapePath.close()

        //初始化各种PathEffect
        mPathEffects = arrayOf(
            CornerPathEffect(30f),
            DiscretePathEffect(10f, 5f),
            DashPathEffect(floatArrayOf(20f, 20f), 0f),
            DashPathEffect(floatArrayOf(20f, 20f), 50f),
            PathDashPathEffect(shapePath, 60f, 0f, PathDashPathEffect.Style.TRANSLATE),
            PathDashPathEffect(shapePath, 60f, 0f, PathDashPathEffect.Style.ROTATE),
            PathDashPathEffect(shapePath, 60f, 0f, PathDashPathEffect.Style.MORPH),
            ComposePathEffect( CornerPathEffect(30f),  DiscretePathEffect(10f, 5f))
        )

        var i = 0
        //使用随机数生成一些随机点，连成路径
        while (i < mPathEffects.size){
            var path = Path()
            for (j in 0..100) {
                path.lineTo(j * 35.toFloat(), (Math.random() * 100).toFloat())
            }
            mPaths.add(path)
            i++
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.translate(0f, 50f)
        mPaths.forEachIndexed { index, path ->

            mPaint.pathEffect = null
            mPaint.textSize = 50f
            mPaint.style = Paint.Style.FILL
            canvas.drawText(mPathEffects[index].javaClass.simpleName, 10f, 50f, mPaint)

            mPaint.strokeWidth = 10f
            mPaint.style = Paint.Style.STROKE
            mPaint.pathEffect = mPathEffects[index]
            canvas.drawPath(path, mPaint)

            canvas.translate(0f, 250f)
        }
    }

}