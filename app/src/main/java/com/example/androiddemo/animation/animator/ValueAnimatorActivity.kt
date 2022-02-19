package com.example.androiddemo.animation.animator

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.R
import kotlinx.android.synthetic.main.activity_value_animator.*

/**
 * 属性动画之ValueAnimator：
 * 本身不提供任何动画效果，是一个数值发生器，用来产生具有一定规律的数字，调用者通过监听这些值的渐变过程来自己操作控件
 *
 * 构造器:ValueAnimator.ofXX
 * public static ValueAnimator ofInt(int... values)
 * public static ValueAnimator ofFloat(float... values)
 * public static ValueAnimator ofObject(TypeEvaluator evaluator, Object... values) --- 示例在com\example\androiddemo\animation\animator\view\OfObjectObjectAnimatorView.kt中
 * int..., float..., Object... --- 可变参数长参数,可以传入任何数量的值,传进去的值列表，就表示动画时的变化范围
 * TypeEvaluator evaluator     --- 自定义的Evaluator,根据当前动画的显示进度，计算出当前进度下对应的值
 * 如：ofInt(2,90,45)，就表示从数值2变化到数字90再变化到数字45
 *
 * public static ValueAnimator ofPropertyValuesHolder(PropertyValuesHolder... values) --- ValueAnimatorActivity中讲解
 *
 * 常用方法:
 * ValueAnimator setDuration(long duration) --- 设置动画时长，单位是毫秒
 * Object getAnimatedValue()                --- 获取ValueAnimator在运动时，当前运动点的值
 * void setRepeatCount(int value)           --- 设置循环次数,设置为INFINITE表示无限循环,0表示不循环
 * void setRepeatMode(int value)            --- 设置循环模式，value取值有RESTART（正序重新开始），REVERSE（倒序重新开始）
 * ValueAnimator clone()                    --- 完全克隆一个ValueAnimator实例，包括它所有的设置以及所有对监听器代码的处理
 * void start()                             --- 开始动画
 * void setStartDelay(long startDelay)      --- 延时多久时间开始动画，单位是毫秒
 * void cancel()                            --- 取消动画
 * void removeAllUpdateListeners() --- 移除所有监听器
 * void removeUpdateListener(AnimatorUpdateListener listener) --- 移除AnimatorUpdateListener
 * void removeListener(AnimatorListener listener) --- 移除AnimatorListener
 *
 * 俩个监听器：
 * public static interface AnimatorUpdateListener --- 监听动画变化时的实时值
 * public static interface AnimatorListener       --- 监听动画变化时四个状态
 */

/* 如何使用ValueAnimator */
class ValueAnimatorActivity : AppCompatActivity() {

    companion object{
        private val TAG = ValueAnimatorActivity::class.java.simpleName
    }

    var valueAnimator: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_value_animator)

        //开始ValueAnimator动画
        btn_start_value1.setOnClickListener {
            doAnimator()
        }

        text_view.setOnClickListener {
            Toast.makeText(
                this@ValueAnimatorActivity,
                "Hello!",
                Toast.LENGTH_SHORT
            ).show()
        }

        //ValueAnimator的ofObject方法使用
        btn_start_value2.setOnClickListener {
            of_object_view.doAnimator()
        }

    }

    /**
     * 启动ValueAnimator动画
     */
    private fun doAnimator() {
        /* 1、通过ValueAnimator.of方法创建实例 */
        //监听动画变化时的实时值
        valueAnimator = ValueAnimator.ofInt(0, 500, 50)
        valueAnimator?.run {
            duration = 2000
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
        }

        /* 2、添加监听 */
        valueAnimator?.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            text_view.layout(
                text_view.left, value,
                text_view.right, text_view.height + value
            )
        }
        //监听动画变化时四个状态
        valueAnimator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                Log.d(TAG, "Animator Start ")
            }

            override fun onAnimationEnd(animation: Animator) {
                Log.d(TAG, "Animator End")
            }

            override fun onAnimationCancel(animation: Animator) {
                Log.d(TAG, "Animator Cancel ")
            }

            override fun onAnimationRepeat(animation: Animator) {
                Log.d(TAG, "Animator Repeat ")
            }
        })
        /* 3、启动动画 */
        valueAnimator?.start()
    }
}