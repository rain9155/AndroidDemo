package com.example.androiddemo.customview.martix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.R
import com.example.androiddemo.customview.martix.colormartix.ColorMatrixActivity
import com.example.androiddemo.customview.martix.colormartix.ColorMatrixApiActivity
import com.example.androiddemo.customview.martix.martix.MatrixActivity
import com.example.androiddemo.customview.martix.pixel.PixelActivity
import kotlinx.android.synthetic.main.activity_bitmap.*

/**
 * 图片Matrix和ColorMatrix示例：
 */
class BitmapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bitmap)

        cp_to_color_matrix1.setOnClickListener {
            startActivity(Intent(this, ColorMatrixActivity::class.java))
        }

        cp_to_color_matrix2.setOnClickListener {
            startActivity(Intent(this, ColorMatrixApiActivity::class.java))
        }

        cp_to_matrix.setOnClickListener {
            startActivity(Intent(this, MatrixActivity::class.java))
        }

        cp_to_pixel.setOnClickListener {
            startActivity(Intent(this, PixelActivity::class.java))
        }
    }
}
