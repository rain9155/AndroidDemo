package com.example.androiddemo.animation.animator

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.R
import kotlinx.android.synthetic.main.activity_xml_animator.*

/**
 * ValueAnimator,ObjectAnimator和AnimatorSet的XML实现
 */
class XmlAnimatorActivity : AppCompatActivity() {

    private var valueAnimator: ValueAnimator? = null
    private var objectAnimator: ObjectAnimator? = null
    private var animatorSet: AnimatorSet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xml_animator)


        btn_start_value.setOnClickListener {
            doValueAnimator()
        }

        btn_start_object.setOnClickListener {
            doObjectAnimator()
        }

        btn_start_set.setOnClickListener {
            doAnimatorSet()
        }
    }

    /**
     * 从xml中加载ValueAnimator动画
     */
    private fun doValueAnimator() {
        /* 1、通过AnimatorInflater.loadAnimator（）加载xml属性动画 */
        valueAnimator = AnimatorInflater.loadAnimator(this, R.animator.animator_value) as ValueAnimator

        /* 2、添加监听 */
        valueAnimator!!.addUpdateListener { animation ->
            //获得加速器计算的值
            val value = animation.animatedValue as Int
            text_1.layout(
                text_1.left,
                text_1.top + value,
                text_1.right,
                text_1.bottom + value
            )
        }

        /* 3、启动 */
        valueAnimator!!.start()
    }

    /**
     * 从xml中加载ObjectAnimator动画
     */
    private fun doObjectAnimator() {
        /* 1、通过AnimatorInflater.loadAnimator（）加载xml属性动画 */
        objectAnimator = AnimatorInflater.loadAnimator(this, R.animator.animator_object) as ObjectAnimator

        /* 2、绑定上动画目标 */
        objectAnimator!!.target = text_2

        /* 3、启动 */
        objectAnimator!!.start()
    }

    /**
     * 从xml中加载AnimatorSet动画
     */
    private fun doAnimatorSet() {
        /* 1、通过AnimatorInflater.loadAnimator（）加载xml属性动画 */
        animatorSet = AnimatorInflater.loadAnimator(this, R.animator.animator_set) as AnimatorSet

        /* 2、绑定上动画目标 */
        animatorSet!!.setTarget(text_3)

        /* 3、启动 */
        animatorSet!!.start()
    }
}