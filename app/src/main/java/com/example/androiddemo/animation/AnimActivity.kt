package com.example.androiddemo.animation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.R
import com.example.androiddemo.animation.reveal.RevealActivity
import com.example.androiddemo.animation.ripple.RippleActivity
import com.example.androiddemo.animation.state.StateActivity
import com.example.androiddemo.animation.transition.FromActivity
import com.example.androiddemo.animation.animation.AnimationXmlActivity
import com.example.androiddemo.animation.animation.AnimationActivity
import com.example.androiddemo.animation.animator.*
import com.example.androiddemo.animation.layoutanim.LayoutAnimationActivity
import com.example.androiddemo.animation.layoutanim.LayoutTransitionActivity
import kotlinx.android.synthetic.main.activity_anim.*

/**
 * 各种动画使用示例：
 */
class AnimActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anim)

        cp_to_animation1.setOnClickListener {
            startActivity(Intent(this, AnimationXmlActivity::class.java))
        }

        cp_to_animation2.setOnClickListener {
            startActivity(Intent(this, AnimationActivity::class.java))
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

        cp_to_reveal_animation.setOnClickListener{
            startActivity(Intent(this, RevealActivity::class.java))
        }

        cp_to_ripple_animation.setOnClickListener {
            startActivity(Intent(this, RippleActivity::class.java))
        }

        cp_to_state_animation.setOnClickListener {
            startActivity(Intent(this, StateActivity::class.java))
        }

        cp_to_activity_transition.setOnClickListener {
            startActivity(Intent(this, FromActivity::class.java))
        }
    }
}
