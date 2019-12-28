package com.example.androiddemo.viewtest.martix.colormartix

import android.graphics.*
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.R
import kotlinx.android.synthetic.main.activity_color_matrix1.*

/**
 * 参考：https://blog.csdn.net/harvic880925/article/details/51187277
 * ColorMatrix颜色矩阵:
 * 一张图片每个像素的的RGBA矩阵 = 颜色矩阵 * 初始(R, G, B, A)值，颜色矩阵初始值如下：
 *
 *                     |偏移量|
 * R :  1   0   0   0     0        红
 * G :  0   1   0   0     0        绿
 * B :  0   0   1   0     0        蓝
 * A :  0   0   0   1     0      透明度
 * 颜色矩阵是一个 4 * 5 的矩阵，每一行代表一个颜色通道，最后一列代表每个颜色通道的偏移量，
 * 可以通过改变颜色系数（0 ~ 255）或每个颜色通道的偏移量，从而改变颜色初始矩阵（ColorMatrix），从而修改图片的色调，饱和度， 亮度。
 *
 * 颜色矩阵的4种运算：
 * 1、平移运算：在最后一列的偏移量加上某个值，这样通过平移就可以增加特定颜色的饱和度
 * 2、缩放运算：在颜色矩阵的对角线上(对应RGBA)分别乘以指定的值，达到特定颜色的放大缩小运算，这样通过缩放就可以调节特定颜色的亮度
 * 3、旋转运算：旋转运算是对RGB三个颜色分量的运算，建立起RGB三色立体坐标系，然后保持某个轴不变，另外两个轴在平面上进行旋转，这样通过旋转就可以调节特定颜色的色相（ 例如当围绕红色轴进行旋转时，红色轴的色彩是不变的，而绿色和蓝色的颜色值在动态改变，这叫红色色相调节）
 * 4、投射运算：利用其它颜色分量的倍数来更改自己颜色分量的值，投射的一个最简单应用就是黑白图片
 */
class ColorMatrixActivity1 : AppCompatActivity() {

    companion object{
        private const val COUNT = 4 * 5
    }

    //用一维数组保存颜色矩阵的20个矩阵值
    var values= floatArrayOf(
        1f, 0f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f, 0f,
        0f, 0f, 1f, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
    )
    //颜色矩阵
    var colorMatrix = ColorMatrix(values)
    //用20个EditText保存输入的20个矩阵值
    var editTexts = ArrayList<EditText>(COUNT)
    //需要通过颜色矩阵改变颜色的图片
    val bitmap by lazy {
        BitmapFactory.decodeResource(resources, R.drawable.girl)
    }
    var etWidth = 0
    var etHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_matrix1)

        //初始化图片
        iv_girl.setImageBitmap(bitmap)

        //初始化矩阵
        grid_layout.post {
            etWidth = grid_layout.width / 5
            etHeight = grid_layout.height / 4
            initGridLayout()
            initEditTexts()
        }

        //把颜色矩阵作用到图片
        btn_change.setOnClickListener {
            updateColorMatrix()
            setImageColorMatrix()
        }

        //重置矩阵为初始状态
        btn_reset.setOnClickListener {
            resetColorMatrix()
            setImageColorMatrix()
        }
    }


    /**
     * 往网格布局中添加格子，形成矩阵
     */
    private fun initGridLayout() {
        for (i in 0..19) {
            val editText = EditText(this@ColorMatrixActivity1)
            editTexts.add(editText)
            grid_layout.addView(editText, etWidth, etHeight)
        }
    }

    /**
     * 初始化EditText矩阵
     */
    private fun initEditTexts() {
        for (i in 0..19) {
            if (i % 6 == 0) {
                editTexts[i].setText("1")
            } else {
                editTexts[i].setText("0")
            }
        }
    }

    /**
     * 获得20个矩阵值，更新颜色矩阵
     */
    private fun updateColorMatrix(){
        for (i in 0..19) {
            values[i] = editTexts[i].text.toString().toFloat()
        }
        colorMatrix.set(values)
    }

    /**
     * 重置颜色矩阵
     */
    private fun resetColorMatrix(){
        initEditTexts()
        colorMatrix.reset()
        values = colorMatrix.array
    }

    /**
     * 把颜色矩阵值设置到图片
     */
    private fun setImageColorMatrix() {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        val paint = Paint()
        //给画笔设置ColorMatrixColorFilter
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        //把原图重新绘制
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        iv_girl.setImageBitmap(newBitmap)
    }

}