package com.example.androiddemo.customview.martix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.R
import com.example.androiddemo.customview.martix.colormartix.ColorMatrixActivity
import com.example.androiddemo.customview.martix.colormartix.ColorMatrixApiActivity
import com.example.androiddemo.customview.martix.martix.MatrixActivity
import com.example.androiddemo.customview.martix.pixel.PixelActivity
import com.example.androiddemo.databinding.ActivityBitmapBinding

/**
 * 图片Matrix和ColorMatrix示例：
 */
class BitmapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBitmapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cpToColorMatrix.setOnClickListener {
            startActivity(Intent(this, ColorMatrixActivity::class.java))
        }

        binding.cpToColorMatrixApi.setOnClickListener {
            startActivity(Intent(this, ColorMatrixApiActivity::class.java))
        }

        binding.cpToMatrix.setOnClickListener {
            startActivity(Intent(this, MatrixActivity::class.java))
        }

        binding.cpToPixel.setOnClickListener {
            startActivity(Intent(this, PixelActivity::class.java))
        }
    }
}
