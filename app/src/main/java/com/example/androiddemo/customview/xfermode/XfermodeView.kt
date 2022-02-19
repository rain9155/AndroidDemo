package com.example.androiddemo.customview.xfermode

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * 参考：https://blog.csdn.net/harvic880925/article/details/51284710， https://blog.csdn.net/harvic880925/article/details/51288006
 * 画笔特效之PorterDuffXfermode:
 * 设置俩个图层交集区域的显示方式，Dst是先画的图形，Src是后画的图形
 * 有16种显示模式（Mode）
 */
/**
 * 一、颜色叠加相关模式
 * --- Mode.ADD（饱和度相加）
 * 对SRC与DST两张图片相交区域的饱和度进行相加
 * 算法: [Saturate(S + D)]
 *
 * --- Mode.LIGHTEN（变亮）
 * 两个图像重合的区域才会有颜色值变化，所以只有重合区域才有变亮的效果，源图像非重合的区域，由于对应区域的目标图像是空白像素，所以直接显示源图像
 * 算法: [Sa + Da - Sa*Da,Sc*(1 - Da) + Dc*(1 - Sa) + max(Sc, Dc)]
 *
 * --- Mode.DARKEN（变暗）
 * 两个图像重合的区域才会有颜色值变化，所以只有重合区域才有变暗的效果
 * 算法：[Sa + Da - Sa*Da,Sc*(1 - Da) + Dc*(1 - Sa) + min(Sc, Dc)]
 *
 * --- Mode.MULTIPLY(正片叠底)
 * 算法：[Sa * Da, Sc * Dc]
 *
 * --- Mode.OVERLAY（叠加）
 * 没算法
 *
 * --- Mode.SCREEN（滤色)
 * 源图像与目标图像交合部分有效果，源图像非交合部分保持原样
 * 算法：[Sa + Da - Sa * Da, Sc + Dc - Sc * Dc]
 *
 * 1、这几种模式都是PhotoShop中存在的模式，是通过计算改变交合区域的颜色值的
 * 2、除了Mode.MULTIPLY(正片叠底)会在目标图像透明时将结果对应区域置为透明，其它图像都不受目标图像透明像素影响，即源图像非交合部分保持原样
 */
/**
 * 二、SRC相关模式(以源图像显示为主)
 * --- Mode.SRC
 * 计算公式为：[Sa, Sc]
 * 在处理源图像所在区域的相交问题时，全部以源图像显示
 *
 * --- Mode.SRC_IN
 * 计算公式为：[Sa * Da, Sc * Da]
 * 在这个公式中结果值的透明度和颜色值都是由Sa,Sc分别乘以目标图像的Da来计算的。所以当目标图像为空白像素时，计算结果也将会为空白像素
 * SRC_IN模式是在相交时利用目标图像的透明度来改变源图像的透明度和饱和度。当目标图像透明度为0时，源图像就完全不显示
 *
 * --- Mode.SRC_OUT
 * 计算公式为：[Sa * (1 - Da), Sc * (1 - Da)]
 * 所以这个模式的特性可以概括为：以目标图像的透明度的补值来调节源图像的透明度和色彩饱和度。
 * 即当目标图像为完全透明时，就完全显示源图像，当目标图像完全不透明时，交合区域为空像素。
 *
 * --- Mode.SRC_OVER
 * 计算公式为：[Sa + (1 - Sa)*Da, Rc = Sc + (1 - Sa)*Dc]
 * 在计算结果中，源图像没有变。它的意思就是在目标图像的顶部绘制源图像。从公式中也可以看出目标图像的透明度为Sa + (1 - Sa)*Da；
 * 即在源图像的透明度基础上增加一部分目标图像的透明度。增加的透明度是源图像透明度的补量；
 * 目标图像的色彩值的计算方式同理，所以当源图像透明度为100%时，就原样显示源图像；
 *
 * --- Mode.SRC_ATOP
 * 计算公式为：[Da, Sc * Da + (1 - Sa) * Dc]
 * 直接使用目标图像的透明度做为结果透明度 ,颜色值在SRC_IN基础上增加了(1 - Sa) * Dc
 * 当透明度只有100%和0%时，SRC_ATOP是SRC_IN是通用的
 * 当透明度不只有100%和0%时，SRC_ATOP相比SRC_IN源图像的饱和度会增加，即会显得更亮！
 */
/**
 * 三、DST相关模式(以目标图像显示为主)
 * --- Mode.DST
 * 计算公式为：[Da, Dc]
 * 在处理源图像所在区域的相交问题时，正好与Mode.SRC相反，全部以目标图像显示
 *
 * --- Mode.DST_IN
 * 计算公式为：[Da * Sa,Dc * Sa]
 * 正好与SRC_IN相反，Mode.DST_IN是在相交时利用源图像的透明度来改变目标图像的透明度和饱和度。当源图像透明度为0时，目标图像就完全不显示。
 *
 * --- Mode.DST_OUT
 * ，在Mode.DST_OUT模式下，就是相交区域显示的是目标图像，目标图像的透明度和饱和度与源图像的透明度相反，
 * 当源图像透明底是100%时，则相交区域为空值。
 * 当源图像透明度为0时，则完全显示目标图像。非相交区域完全显示目标图像
 * 计算公式为：[Da * (1 - Sa), Dc * (1 - Sa)]
 *
 * --- Mode.DST_OVER
 * 它们的效果就是在SRC模式中以显示SRC图像为主变成了以显示DST图像为主
 * 从SRC模式中的使用目标图像控制结果图像的透明度和饱和度变成了由源图像控件结果图像的透明度和饱和度
 * 计算公式为：[Sa + (1 - Sa)*Da, Rc = Dc + (1 - Da)*Sc]
 *
 * --- Mode.DST_ATOP
 * 计算公式为：[Sa, Sa * Dc + Sc * (1 - Da)]
 * 一般而言DST_ATOP是可以和DST_IN通用的
 * 但DST_ATOP所产生的效果图在源图像的透明度不是0或100%的时候，会比DST_IN模式产生的图像更亮些
 */
/**
 * 四、其它模式
 * --- Mode.CLEAR
 * 计算公式：[0, 0]
 * 源图像所在区域都会变成空像素！
 *
 * --- Mode.XOR
 * 计算公式为：[Sa + Da - Sa*Da,Sc*(1 - Da) + Dc*(1 - Sa) + min(Sc, Dc)]
 */
/**
 * 在实际应用中，我们可以从下面三个方面来决定使用哪一个模式：
 * 1、首先，目标图像和源图像混合，需不需要生成颜色的叠加特效，如果需要叠加特效则从颜色叠加相关模式中选择，有Mode.ADD（饱和度相加）、Mode.DARKEN（变暗），Mode.LIGHTEN（变亮）、Mode.MULTIPLY（正片叠底）、Mode.OVERLAY（叠加），Mode.SCREEN（滤色）
 * 2、当不需要特效，而需要根据某一张图像的透明像素来裁剪时，就需要使用SRC相关模式或DST相关模式了。由于SRC相关模式与DST相关模式是相通的，唯一不同的是决定当前哪个是目标图像和源图像
 * 3、当需要清空图像时，使用Mode.CLEAR
 */
class XfermodeView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val mWidth = 400
    private val mHeigth = 400
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mSrcBitmap = makeSrc(mWidth, mHeigth)
    private val mDstBitmap = makeDst(mWidth, mHeigth)

    init {
        //禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {

        //离屏绘制
        val layerId = canvas.saveLayer(
            0f, 0f,
            mWidth * 2.toFloat(), mHeigth * 2.toFloat(),
            mPaint,
            Canvas.ALL_SAVE_FLAG
        )

        canvas.drawBitmap(mDstBitmap, 0f, 0f, mPaint)
        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(mSrcBitmap, mWidth / 2.toFloat(), mHeigth / 2.toFloat(), mPaint)
        mPaint.xfermode = null

        canvas.restoreToCount(layerId)
    }

    /**
     * 画一个黄色椭圆（目标图像）
     */
    private fun makeDst(w: Int, h: Int): Bitmap {
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.YELLOW
        canvas.drawOval(RectF(0f, 0f, w.toFloat(), h.toFloat()), paint)
        return bmp
    }

    /**
     * 画一个蓝色矩形（源图像）
     */
    private fun makeSrc(w: Int, h: Int): Bitmap {
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.BLUE
        canvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), paint)
        return bmp
    }
}