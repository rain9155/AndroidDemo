package com.example.androiddemo.animationtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.R
import com.example.androiddemo.animationtest.animation.AnimationActivity1
import com.example.androiddemo.animationtest.animation.AnimationActivity2
import com.example.androiddemo.animationtest.animator.*
import com.example.androiddemo.animationtest.layoutanim.LayoutAnimationActivity
import com.example.androiddemo.animationtest.layoutanim.LayoutTransitionActivity
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

        cp_to_value_animator.setOnClickListener {
            startActivity(Intent(this, ValueAnimatorActivity::class.java))
        }

        cp_to_object_animator.setOnClickListener {
            startActivity(Intent(this, ObjectAnimatorActivity::class.java))
        }

        cp_to_values_holder.setOnClickListener {
            startActivity(Intent(this, ValuesHolderActivity::class.java))
        }

        cp_to_set_animator.setOnClickListener {
            startActivity(Intent(this, AnimatorSetActivity::class.java))
        }

        cp_to_xml_animator.setOnClickListener {
            startActivity(Intent(this, XmlAnimatorActivity::class.java))
        }

        cp_to_layout_animation.setOnClickListener {
            startActivity(Intent(this, LayoutAnimationActivity::class.java))
        }

        cp_to_transition_animation.setOnClickListener {
            startActivity(Intent(this, LayoutTransitionActivity::class.java))
        }
    }
}
