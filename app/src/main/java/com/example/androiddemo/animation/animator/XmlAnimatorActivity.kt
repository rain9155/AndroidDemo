package com.example.androiddemo.animation.animator

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.R
import com.example.androiddemo.databinding.ActivityXmlAnimatorBinding

/**
 * ValueAnimator,ObjectAnimator和AnimatorSet的XML实现
 */
class XmlAnimatorActivity : AppCompatActivity() {

    private var valueAnimator: ValueAnimator? = null
    private var objectAnimator: ObjectAnimator? = null
    private var animatorSet: AnimatorSet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityXmlAnimatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartValue.setOnClickListener {
            doValueAnimator(binding.text1)
        }

        binding.btnStartObject.setOnClickListener {
            doObjectAnimator(binding.text2)
        }

        binding.btnStartSet.setOnClickListener {
            doAnimatorSet(binding.text3)
        }
    }

    /**
     * 从xml中加载ValueAnimator动画
     */
    private fun doValueAnimator(textView: TextView) {
        /* 1、通过AnimatorInflater.loadAnimator（）加载xml属性动画 */
        valueAnimator = AnimatorInflater.loadAnimator(this, R.animator.animator_value) as ValueAnimator

        /* 2、添加监听 */
        valueAnimator!!.addUpdateListener { animation ->
            //获得加速器计算的值
            val value = animation.animatedValue as Int
            textView.layout(
                textView.left,
                textView.top + value,
                textView.right,
                textView.bottom + value
            )
        }

        /* 3、启动 */
        valueAnimator!!.start()
    }

    /**
     * 从xml中加载ObjectAnimator动画
     */
    private fun doObjectAnimator(textView: TextView) {
        /* 1、通过AnimatorInflater.loadAnimator（）加载xml属性动画 */
        objectAnimator = AnimatorInflater.loadAnimator(this, R.animator.animator_object) as ObjectAnimator

        /* 2、绑定上动画目标 */
        objectAnimator!!.target = textView

        /* 3、启动 */
        objectAnimator!!.start()
    }

    /**
     * 从xml中加载AnimatorSet动画
     */
    private fun doAnimatorSet(textView: TextView) {
        /* 1、通过AnimatorInflater.loadAnimator（）加载xml属性动画 */
        animatorSet = AnimatorInflater.loadAnimator(this, R.animator.animator_set) as AnimatorSet

        /* 2、绑定上动画目标 */
        animatorSet!!.setTarget(textView)

        /* 3、启动 */
        animatorSet!!.start()
    }
}