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
import com.example.androiddemo.databinding.ActivityAnimBinding

/**
 * 各种动画使用示例：
 */
class AnimActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAnimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cpToAnimationXml.setOnClickListener {
            startActivity(Intent(this, AnimationXmlActivity::class.java))
        }

        binding.cpToAnimation.setOnClickListener {
            startActivity(Intent(this, AnimationActivity::class.java))
        }

        binding.cpToValueAnimator.setOnClickListener {
            startActivity(Intent(this, ValueAnimatorActivity::class.java))
        }

        binding.cpToObjectAnimator.setOnClickListener {
            startActivity(Intent(this, ObjectAnimatorActivity::class.java))
        }

        binding.cpToValuesHolder.setOnClickListener {
            startActivity(Intent(this, ValuesHolderActivity::class.java))
        }

        binding.cpToSetAnimator.setOnClickListener {
            startActivity(Intent(this, AnimatorSetActivity::class.java))
        }

        binding.cpToXmlAnimator.setOnClickListener {
            startActivity(Intent(this, XmlAnimatorActivity::class.java))
        }

        binding.cpToLayoutAnimation.setOnClickListener {
            startActivity(Intent(this, LayoutAnimationActivity::class.java))
        }

        binding.cpToLayoutTransition.setOnClickListener {
            startActivity(Intent(this, LayoutTransitionActivity::class.java))
        }

        binding.cpToRevealAnimation.setOnClickListener{
            startActivity(Intent(this, RevealActivity::class.java))
        }

        binding.cpToRippleAnimation.setOnClickListener {
            startActivity(Intent(this, RippleActivity::class.java))
        }

        binding.cpToStateAnimation.setOnClickListener {
            startActivity(Intent(this, StateActivity::class.java))
        }

        binding.cpToActivityTransition.setOnClickListener {
            startActivity(Intent(this, FromActivity::class.java))
        }
    }
}
