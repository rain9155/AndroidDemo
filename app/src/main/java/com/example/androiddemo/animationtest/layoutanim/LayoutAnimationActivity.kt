package com.example.androiddemo.animationtest.layoutanim

import android.os.Bundle
import android.view.animation.*
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.R
import kotlinx.android.synthetic.main.activity_layout_animation.*

/**
 * Android布局动画之LayoutAnimation:
 * 布局动画是指作用在ViewGroup上，给ViewGroup增加View时添加一个动画过渡效果, 只能在第一次创建时有效
 */

/**
 * 一、LayoutAnimation的代码实现 --- LayoutAnimationController
 *
 * 构造器：
 * public LayoutAnimationController(Animation animation)
 * public LayoutAnimationController(Animation animation, float delay)
 * animation --- 对应标签中的android:animation属性
 * delay     --- 对应标签中的android:delay属性
 *
 * 常用函数：
 * public void setAnimation(Animation animation) --- 设置animation动画
 * public void setDelay(float delay)             --- 设置单个item开始动画延时
 * public void setOrder(int order)               --- 设置viewGroup中控件开始动画顺序，取值为ORDER_NORMAL(正序)、ORDER_REVERSE(倒序)、ORDER_RANDOM(随机)
 */

/**
 * 二、LayoutAnimation的xml实现 --- xml
 * 1、定义一个控件动画,如：(anim/animation_set.xml)
 * 2、定义一个layoutAnimation的animation文件，如：(anim/animation_layout.xml)
 * 3、在viewGroup类型中，添加android:layoutAnimation=”@anim/animation_layout”标签
 */

/**
 * 三、总结：
 * 1、LayoutAnimationt和GridLayoutAnimation是在api1时就已经引入进来了，所以不用担心API不支持的问题
 * 2、gridLayoutAnimation与layoutAnimation一样，都只是在viewGroup创建的时候，会对其中的item添加进入动画，在创建完成后，再添加数据将不会再有动画！
 * 3、LayoutAnimationt和GridLayoutAnimation仅支持Animation动画，不支持Animator动画；正是因为它们在api 1就引入进来了，而Animator是在API 11才引入的，所以它们是不可能支持Animator动画的
 */
class LayoutAnimationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_animation)

        doLayoutAnimation()
    }

    /**
     * LayoutAnimation代码实现, LayoutAnimationController的使用
     */
    private fun doLayoutAnimation(){
        /* 1、定义一个控件动画, 通过Xml定义 或 代码定义动画 */
        val animation = AnimationUtils.loadAnimation(this,R.anim.animation_set)

        /* 2、定义一个LayoutAnimationController对象 , 把动画传入构造*/
        val layoutAnimationController = LayoutAnimationController(animation)

        /* 3、设置属性 */
        layoutAnimationController.delay = 0.5f
        layoutAnimationController.order = LayoutAnimationController.ORDER_NORMAL

        /* 4、用ViewGroup中的setLayoutAnimation（）为ViewGroup设置LayoutAnimationController属性 */
        ll_items1.layoutAnimation = layoutAnimationController
    }
}
