package com.example.androiddemo.view.martix.colormartix

import android.graphics.*
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.R
import kotlinx.android.synthetic.main.activity_color_matrix2.*

/**
 * ColorMatrix颜色矩阵：
 * 通过ColorMatrix的相关函数实现颜色矩阵的平移运算、缩放运算、旋转运算，从而修改图片的RGBA的色调，饱和度， 亮度
 *
 * ---色调（物体传播的颜色）， 通过setRotate（）设置色调，原理是颜色矩阵的旋转运算：
 * ColorMatrix hueMatrix = new ColorMatrix();
 * hueMatrix.setRotate(0, hue0);
 * hueMatrix.setRotate(1, hue1);
 * hueMatrix.setRotate(2, hue2);
 * 第一个参数0,1,2, 分别代表 R，G, B 轴，0表示使用R 轴作为主轴, 1表示使用G 轴作为主轴，2表示使用B 轴作为主轴
 * 第二个参数表示旋转的度数,取值范围为[-180度, 180度]
 *
 * ---饱和度（颜色的纯度），通过setSaturation(）设置饱和度，原理是颜色矩阵的平移运算：
 * ColorMatrix saturationMatrix = new ColorMatrix();
 * saturationMatrix.setSaturation(saturation);
 * 第一个参数表示同时增强R，G，B饱和度的倍数，取值范围为[0（灰色），1（色彩无变化）， 大于1（过度饱和）]
 *
 * ---亮度（颜色的亮暗），通过setScale（）设置饱和度，原理是颜色矩阵的缩放运算
 * ColorMatrix lumMatrix = new ColorMatrix();
 * lumMatrix.setScale(lum, lum, lum, 1);
 * 第一，二，三个参数分别代表对角线上R,G,B系数三种色的缩放倍数
 * 第四个参数代表A，透明度，取值范围为[0（不透明）, 1（完全透明）]
 *
 * 通过颜色矩阵的postConcat(matrix)（矩阵后乘）、preConcat(matrix)(矩阵前乘)、setConcat(matrix1，matrix2)(两个矩阵相乘)矩阵运算将三种作用效果混合
 * ColorMatrix imageMatrix = new ColorMatrix();
 * imageMatrix.postConcat(hueMatrix);
 * imageMatrix.postConcat(saturationMatrix);
 * imageMatrix.postConcat(lumMatrix);
 */
class ColorMatrixActivity2 : AppCompatActivity(), OnSeekBarChangeListener {

    companion object {
        private const val MID_VALUE = 50f
    }

    //图片的色调
    var hue = 50f
    //图片的饱和度
    var saturation = 50f
    //图片的亮度
    var lum = 50f
    //需要被处理的图片
    val bitmap by lazy {
        BitmapFactory.decodeResource(resources, R.drawable.girl)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_matrix2)
        sb_hue.setOnSeekBarChangeListener(this)
        sb_saturation.setOnSeekBarChangeListener(this)
        sb_lum.setOnSeekBarChangeListener(this)
    }

    /** 按住seekbar时的事件监听处理   */
    override fun onStartTrackingTouch(seekBar: SeekBar) {}

    /** 放开seekbar时的时间监听处理   */
    override fun onStopTrackingTouch(seekBar: SeekBar) {}

    /**  seekbar进度改变时的事件监听处理   */
    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        when (seekBar.id) {
            R.id.sb_hue -> {
                hue = (progress - MID_VALUE) * 1.0f / MID_VALUE * 180
                tv_hue.text = "色调：$progress%"
            }
            R.id.sb_saturation -> {
                saturation = progress * 1.0f / MID_VALUE
                tv_saturation.text = "饱和度：$progress%"
            }
            R.id.sb_lum -> {
                lum = progress * 1.0f / MID_VALUE
                tv_lum.text = "亮度：$progress%"
            }
            else -> {
            }
        }
        val handledBitmap = handleBitmap(bitmap, hue, saturation, lum)
        iv_girl.setImageBitmap(handledBitmap)
    }

    /**
     * 设置图像矩阵
     * @param original 要改变 色调 或 饱和度 或 亮度 的bitmap图片
     * @param hue 色调
     * @param saturation 饱和度
     * @param lum 亮度
     * @return 改变后的bitmap图片
     */
    private fun handleBitmap(
        original: Bitmap,
        hue: Float,
        saturation: Float,
        lum: Float
    ): Bitmap {
        //通过原图bm创建同样大小的bitmap即bmp，最后将原图绘制到bmp中，完成修改原图
        val newBitmap = Bitmap.createBitmap(original.width, original.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        val paint = Paint()
        //通过颜色矩阵来设置颜色的色调
        val hueMatrix = ColorMatrix()
        hueMatrix.setRotate(0, hue)
        hueMatrix.setRotate(1, hue)
        hueMatrix.setRotate(2, hue)
        //通过颜色矩阵来设置颜色的饱和度
        val saturationMatrix = ColorMatrix()
        saturationMatrix.setSaturation(saturation)
        //通过颜色矩阵来设置颜色的亮度
        val lumMatrix = ColorMatrix()
        lumMatrix.setScale(lum, lum, lum, 1f)
        //通过颜色矩阵运算将三种作用效果混合
        val imageMatrix = ColorMatrix()
        imageMatrix.postConcat(hueMatrix)
        imageMatrix.postConcat(saturationMatrix)
        imageMatrix.postConcat(lumMatrix)
        //给画笔设置ColorMatrixColorFilter
        paint.colorFilter = ColorMatrixColorFilter(imageMatrix)
        //把原图重新绘制
        canvas.drawBitmap(original, 0f, 0f, paint)
        return newBitmap
    }
}