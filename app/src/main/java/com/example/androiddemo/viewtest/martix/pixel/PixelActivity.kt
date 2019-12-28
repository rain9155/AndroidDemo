package com.example.androiddemo.viewtest.martix.pixel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.R
import kotlinx.android.synthetic.main.activity_pixel.*

/**
 * ---1、图像的像素点分析:
 * 通过改变图片的每一个像素点的RGBA值，来达到处理一张图片效果的目的
 *
 * 使用bitmap.getPixels(@ColorInt int[] pixels, int offset, int stride, int x, int y, int width, int height) 来提取整个图片的像素点
 * pixels --- 接受位图颜色值的数组
 * offset --- 写入到pixels[]中第一个像素索引值
 * stride --- pixels[]中的行间距
 * x      --- 从位图中读取的第一个像素的x坐标值
 * y      --- 从位图中读取的第一个像素的y坐标值
 * width  --- 从每一行中读取的像素宽度
 * height --- 读取的行数
 *
 * ---2、图像的像素块分析:
 * 把图像分成一个网格，通过改变每一个网格交叉点的坐标来修改整个图像的形状
 *
 * 使用drawBitmapMesh(Bitmap bitmap, int meshWidth, int meshHeight, float[] verts, int vertOffset, int[] colors, int colorOffset, Paint paint)
 * 几个主要的参数：
 * bitmap --- 将要扭曲的图像
 * meshWidth --- 需要横向的网格数目
 * meshHeight --- 需要纵向的网格数目
 * verts --- 网格交叉点的数组坐标（数组长度为[（ meshWidth + 1）*（meshHeight + 1）* 2]）
 * vertOffset --- verts数组中开始跳过（x， y）坐标对的数目
 *
 */
class PixelActivity : AppCompatActivity() {

    companion object {
        private const val HEIGHT = 20
        private const val WIDTH = 20
    }

    lateinit var bitmap: Bitmap
    lateinit var origPxl: IntArray
    var width = 0
    var height = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pixel)

        //初始化照片
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.girl)
        width = bitmap.width
        height = bitmap.height

        //获得照片的原始像素，用于还原
        origPxl = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(origPxl, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        //还原
        btn_reset.setOnClickListener {
            val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            newBitmap.setPixels(origPxl, 0, width, 0, 0, width, height)
            iv_girl.setImageBitmap(newBitmap)
        }

        //图片的像素点处理
        btn_change1.setOnClickListener {
            iv_girl.setImageBitmap(handleImageNegative(bitmap))
        }

        //图片的像素块处理
        btn_change2.setOnClickListener {
            iv_girl.setImageBitmap(handleImageShape(bitmap))
        }

    }

    /**
     * 图像的像素点分析:
     * 通过像素点处理图片效果 ，实现底片效果, 类似的有老照片效果:
     * r1 = （int） （0.393 * r + 0.769 * g + 0.189 * ）b
     * g1 = （int） （0.349 * r + 0.686 * g + 0.168 * ）b
     * b1 = （int） （0.272 * r + 0.534 * g + 0.131 * ）b
     * @param original 原图
     * @return 处理后的图片
     */
    private fun handleImageNegative(original: Bitmap): Bitmap {
        val width = original.width
        val height = original.height
        var color: Int
        var r: Int
        var g: Int
        var b: Int
        var a: Int
        var oldPx1 = IntArray(width * height)
        var newPx1 = IntArray(width * height)
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        //把整个原图bm的像素点提取到oldPx数组中
        original.getPixels(oldPx1, 0, width, 0, 0, width, height)
        for (i in 0 until width * height) {
            //获取到每个像素具体的RGBA值
            color = oldPx1[i]
            r = Color.red(color)
            g = Color.green(color)
            b = Color.blue(color)
            a = Color.alpha(color)
            //通过相应的算法修改r，g，b，a，这里是底片效果
            r = 255 - r
            g = 255 - g
            b = 255 - b
            if (r > 255) {
                r = 255
            } else if (r < 0) {
                r = 0
            }
            if (g > 255) {
                g = 255
            } else if (g < 0) {
                g = 0
            }
            if (b > 255) {
                b = 255
            } else if (b < 0) {
                b = 0
            }
            //将RGBA合成新的像素点
            newPx1[i] = Color.argb(a, r, g, b)
        }
        //将新的像素点数组重新set给Bitmap即bmp
        bmp.setPixels(newPx1, 0, width, 0, 0, width, height)
        return bmp
    }


    /**
     * 图像的像素块分析:
     * 通过像素块改变图像形状，实现旗帜飞扬的效果
     * @param original 原图
     * @return 改变后的图片
     */
    private fun handleImageShape(original: Bitmap): Bitmap {
        val bitmapWidth = original.width.toFloat()
        val bitmapHeight = original.height.toFloat()
        var index = 0
        var orig = FloatArray((WIDTH + 1) * (HEIGHT + 1) * 2)
        var verts = FloatArray((WIDTH + 1) * (HEIGHT + 1) * 2)
        val bmp = Bitmap.createBitmap(original.width, original.height, Bitmap.Config.ARGB_8888)
        //遍历网格的交叉点，按比例取出每一个点的坐标，保存到orig与verts里
        for (y in 0..HEIGHT) {
            val fy = bitmapHeight * y / HEIGHT
            for (x in 0..WIDTH) {
                val fx = bitmapWidth * x / WIDTH
                verts[index * 2 + 0] = fx
                orig[index * 2 + 0] = verts[index * 2 + 0]
                //这里将坐标+100是为了让图像下移，避免扭曲后被屏幕阻挡
                verts[index * 2 + 0] = fy + 100
                orig[index * 2 + 1] = verts[index * 2 + 0]
                index += 1
            }
        }
        //改变交叉点的坐标，实现旗帜飞扬的效果（使用一个sinx来改变纵坐标的值，而横坐标不变，将变化后的值保存到verts中）
        for (i in 0..HEIGHT) {
            for (j in 0..WIDTH) {
                //verts[(i * (WIDTH + 1) + j) * 2 + 0] += 0
                val offsetY = Math.sin(j.toFloat() / WIDTH * 2 * Math.PI).toFloat()
                verts[(i * (WIDTH + 1) + j) * 2 + 1] = orig[(i * (WIDTH + 1) + j) * 2 + 1] + offsetY * 50
            }
        }
        //将处理后的图像画出来
        val canvas = Canvas(bmp)
        canvas.drawBitmapMesh(original, WIDTH, HEIGHT, verts, 0, null, 0, null)
        return bmp
    }

}