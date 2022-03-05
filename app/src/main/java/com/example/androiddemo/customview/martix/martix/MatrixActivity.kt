package com.example.androiddemo.customview.martix.martix

import android.graphics.*
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.R
import com.example.androiddemo.databinding.ActivityMatrixBinding

/**
 * 参考：https://www.gcssloop.com/customview/Matrix_Basic， https://www.gcssloop.com/customview/Matrix_Method
 * Matrix变形矩阵:
 * 通过Matrix变形矩阵可以实现图片的图形变换
 *
 * 初始矩阵如下：
 * |  1           0            0   |
 * |  0           1            0   |
 * |  0           0            1   |
 *
 * 各个系数代表如下：
 * |Scale_X   Skew_X    Translate_X |
 * |Skew_Y    Scale_Y   Translate_Y|
 * |MPERSP_0  MPERSP_0  MPERSP_0 |   透视
 *
 * 变形矩阵有以下4种运算：
 * Translate --- 平移变换，通过改变Translate_X，Translate_Y
 * Rotate    --- 旋转变换，通过改变Translate_X，Translate_Y，Scale_X， Scale_Y，kew_Y， Skew_X
 * Scale     --- 缩放变换，通过改变Scale_X， Scale_Y
 * Skew      --- 错切变换，通过改变Skew_Y， Skew_X
 *
 * 也可以通过Matrix相关函数实现四种变换：
 * Matrix.setRotate --- 旋转变换
 * Matrix.setTranslate --- 平移变换
 * Matrix.setScale --- 缩放变换
 * Matrix.setSkew --- 错切变换
 * pre()和post（）为矩阵的前乘和后乘
 */
class MatrixActivity : AppCompatActivity() {


    companion object{
        private const val COUNT = 3 * 3
    }

    //用一个一维数组保存变形矩阵的9个矩阵值
    private var values = floatArrayOf(
        1f, 0f, 0f,
        0f, 1f, 0f,
        0f, 0f, 1f
    )
    //变形矩阵
    private var matrix = Matrix()
    //用9个EditText保存输入的9个矩阵值
    private var editTexts: Array<EditText?> = arrayOfNulls(COUNT)
    //需要通过变形矩阵改变现状大小的图片
    private val bitmap  by lazy {
        BitmapFactory.decodeResource(resources, R.drawable.girl)
    }
    private var etWidth = 0
    private var etHeight = 0

    private lateinit var binding: ActivityMatrixBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatrixBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //初始化图片
        binding.ivLauncher.setImageBitmap(bitmap)

        //初始化矩阵
        binding.gridLayout.post {
            etWidth = binding.gridLayout.width / 3
            etHeight = binding.gridLayout.height / 3
            initGridLayout()
            initEditTexts()
        }

        //把变形矩阵作用到图片
        binding.btnChange.setOnClickListener {
            updateMatrix()
            setImageMatrix()
        }

        //重置矩阵为初始状态
        binding.btnReset.setOnClickListener {
            resetMatrix()
            setImageMatrix()
        }
    }


    /**
     * 往网格布局中添加格子,形成矩阵
     */
    private fun initGridLayout() {
        for (i in 0..8) {
            val editText = EditText(this@MatrixActivity)
            editTexts[i] = editText
            binding.gridLayout.addView(editText, etWidth, etHeight)
        }
    }

    /**
     * 初始化EditText矩阵
     */
    private fun initEditTexts() {
        for (i in 0..8) {
            if (i % 4 == 0) {
                editTexts[i]!!.setText("1")
            } else {
                editTexts[i]!!.setText("0")
            }
        }
    }

    /**
     * 获得9个矩阵值, 更新变形矩阵
     */
    private fun updateMatrix() {
        for (i in 0..8) {
            values[i] = editTexts[i]!!.text.toString().toFloat()
        }
        matrix.setValues(values)
    }

    /**
     * 重置变形矩阵
     */
    private fun resetMatrix(){
        initEditTexts()
        matrix.reset()
        matrix.getValues(values)
    }


    /**
     * 把变形矩阵值设置到图片
     */
    private fun setImageMatrix() {
        val newBitmap = Bitmap.createBitmap(bitmap!!.width, bitmap!!.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        //将原图以这个变形矩阵的形式重新绘制
        canvas.drawBitmap(bitmap, matrix,  Paint())
        binding.ivLauncher.setImageBitmap(newBitmap)
    }

}