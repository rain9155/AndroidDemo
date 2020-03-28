package com.example.androiddemo.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.R
import com.example.androiddemo.view.canvas.CanvasActivity
import com.example.androiddemo.view.martix.BitmapActivity
import com.example.androiddemo.view.patheffect.PathEffectActivity
import com.example.androiddemo.view.shader.ShaderActivity
import com.example.androiddemo.view.xfermode.XfermodeActivity
import kotlinx.android.synthetic.main.activity_view.*

class ViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        cp_to_canvas.setOnClickListener {
            startActivity(Intent(this, CanvasActivity::class.java))
        }

        cp_to_patheffect.setOnClickListener {
            startActivity(Intent(this, PathEffectActivity::class.java))
        }

        cp_to_shader.setOnClickListener {
            startActivity(Intent(this, ShaderActivity::class.java))
        }

        cp_to_xfermode.setOnClickListener {
            startActivity(Intent(this, XfermodeActivity::class.java))
        }

        cp_to_matrix.setOnClickListener {
            startActivity(Intent(this, BitmapActivity::class.java))
        }

    }
}
