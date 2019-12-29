package com.example.androiddemo.animationtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.R
import com.example.androiddemo.animationtest.animation.AnimationActivity1
import com.example.androiddemo.animationtest.animation.AnimationActivity2
import kotlinx.android.synthetic.main.activity_anim.*

/**
 * 各种动画使用示例：
 */
class AnimActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anim)

        cp_to_animation1.setOnClickListener {
            startActivity(Intent(this, AnimationActivity1::class.java))
        }

        cp_to_animation2.setOnClickListener {
            startActivity(Intent(this, AnimationActivity2::class.java))
        }

    }
}
