package com.example.androiddemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.animation.AnimActivity
import com.example.androiddemo.drawable.DrawableActivity
import com.example.androiddemo.jetpack.JetpackActivity
import com.example.androiddemo.material.ToolbarActivity
import com.example.androiddemo.customview.CustomViewActivity
import com.example.androiddemo.databinding.ActivityMainBinding
import com.example.androiddemo.media.MediaActivity
import com.example.androiddemo.view.ViewActivity

/**
 * demo编写组织规则：
 * 1、类按功能、类型划分到对应的package；
 * 2、类、资源命名要有含义，见名知意；
 * 3、入口activity要有对应的label.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cpToCustomView.setOnClickListener {
            startActivity(Intent(this, CustomViewActivity::class.java))
        }

        binding.cpToAnim.setOnClickListener {
            startActivity(Intent(this, AnimActivity::class.java))
        }

        binding.cpToDrawable.setOnClickListener{
            startActivity(Intent(this, DrawableActivity::class.java))
        }

        binding.cpToMaterial.setOnClickListener {
            startActivity(Intent(this, ToolbarActivity::class.java))
        }

        binding.cpToJetpack.setOnClickListener {
            startActivity(Intent(this, JetpackActivity::class.java))
        }

        binding.cpToView.setOnClickListener {
            startActivity(Intent(this, ViewActivity::class.java))
        }

        binding.cpToMedia.setOnClickListener {
            startActivity(Intent(this, MediaActivity::class.java))
        }
    }
}
