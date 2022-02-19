package com.example.androiddemo.animation.state

import android.animation.AnimatorInflater
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.R
import kotlinx.android.synthetic.main.activity_state.*

/**
 * 视图状态动画：
 * 所谓视图状态动画，就是 View 在状态改变时执行的动画效果,和之前我们通过 selector 选择器给 Button 设置不同状态下的背景效果是一样的
 *
 * View 状态改变的动画主要是两个类：
 * 1、StateListAnimator: 是个动画, 在 res/anim (或者 res/animator)中
 * 通过android:stateListAnimator="@animator/selector_press_state"加载 或 通过代码加载
 * 2、AnimatedStateListDrawable: 是个 Drawable, 在 res/drawable 中
 * 使用：android:background="@drawable/anim_selector_state"
 */
class StateActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state)

        //StateListAnimator通过代码加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val stateListAnimator = AnimatorInflater.loadStateListAnimator(this, R.animator.selector_press_state)
            tv_state_anim.stateListAnimator = stateListAnimator
        }
    }
}