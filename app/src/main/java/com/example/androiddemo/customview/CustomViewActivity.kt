package com.example.androiddemo.customview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.R
import com.example.androiddemo.customview.canvas.CanvasActivity
import com.example.androiddemo.customview.martix.BitmapActivity
import com.example.androiddemo.customview.patheffect.PathEffectActivity
import com.example.androiddemo.customview.shader.ShaderActivity
import com.example.androiddemo.customview.xfermode.XfermodeActivity
import com.example.androiddemo.databinding.ActivityCustomViewBinding

class CustomViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCustomViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cpToCanvas.setOnClickListener {
            startActivity(Intent(this, CanvasActivity::class.java))
        }

        binding.cpToPatheffect.setOnClickListener {
            startActivity(Intent(this, PathEffectActivity::class.java))
        }

        binding.cpToShader.setOnClickListener {
            startActivity(Intent(this, ShaderActivity::class.java))
        }

        binding.cpToXfermode.setOnClickListener {
            startActivity(Intent(this, XfermodeActivity::class.java))
        }

        binding.cpToMatrix.setOnClickListener {
            startActivity(Intent(this, BitmapActivity::class.java))
        }

    }
}
