package com.example.androiddemo.viewtest.martix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.R
import com.example.androiddemo.viewtest.martix.colormartix.ColorMatrixActivity1
import com.example.androiddemo.viewtest.martix.colormartix.ColorMatrixActivity2
import com.example.androiddemo.viewtest.martix.martix.MatrixActivity
import com.example.androiddemo.viewtest.martix.pixel.PixelActivity
import kotlinx.android.synthetic.main.activity_bitmap.*

/**
 * 图片Matrix和ColorMatrix示例：
 */
class BitmapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bitmap)

        cp_to_color_matrix1.setOnClickListener {
            startActivity(Intent(this, ColorMatrixActivity1::class.java))
        }

        cp_to_color_matrix2.setOnClickListener {
            startActivity(Intent(this, ColorMatrixActivity2::class.java))
        }

        cp_to_matrix.setOnClickListener {
            startActivity(Intent(this, MatrixActivity::class.java))
        }

        cp_to_pixel.setOnClickListener {
            startActivity(Intent(this, PixelActivity::class.java))
        }
    }
}
