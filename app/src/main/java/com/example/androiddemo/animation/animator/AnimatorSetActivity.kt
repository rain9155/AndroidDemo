package com.example.androiddemo.animation.animator

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.R
import kotlinx.android.synthetic.main.activity_set_animator.*

/**
 * 属性动画之AnimatorSet（联合动画）
 * AnimatorSet不仅能像PropertyValuesHolder对一个控件对象同时作用多个属性动画效果，还能实现精确的顺序控制
 *
 * 一、AnimatorSet --- playSequentially,playTogether
 * 1、playSequentially函数 --- 顺序播放动画
 * public void playSequentially(Animator... items);
 * -Animator... items    --- 可变长参数，可以传进去任意多个Animator对象
 * public void playSequentially(List<Animator> items);
 * -List<Animator> items --- 传进去一个List< Animator>的列表
 *
 * 2、playTogether函数 --- 表示将所有动画一起播放
 * public void playTogether(Animator... items);
 * -Animator... items    --- 可变长参数，可以传进去任意多个Animator对象
 * public void playTogether(Collection<Animator> items);
 * -List<Animator> items --- 传进去一个List< Animator>的列表
 *
 * 3、playSequentially,playTogether真正意义：
 * 第一：playTogether和playSequentially在激活动画后，控件的动画情况与它们无关，他们只负责定时激活控件动画。
 * 第二：playSequentially只有上一个控件做完动画以后，才会激活下一个控件的动画，如果上一控件的动画是无限循环，那下一个控件就别再指望能做动画了。
 *
 * 二、AnimatorSet.Builder --- 自由设置动画顺序
 * public Builder play(Animator anim)   ---  表示要播放哪个动画,//调用AnimatorSet中的play方法是获取AnimatorSet.Builder对象的唯一途径
 * public Builder with(Animator anim)   --- 和前面动画一起执行
 * public Builder before(Animator anim) --- 执行前面的动画后才执行该动画
 * public Builder after(Animator anim)  --- 执行先执行这个动画再执行前面动画
 * public Builder after(long delay)     --- 延迟n毫秒之后执行动画
 *
 * 三、AnimatorSet监听器
 * public void addListener(AnimatorListener listener);
 * 1、AnimatorSet的监听函数只是用来监听AnimatorSet的状态的，与其中的动画无关；
 * 2、AnimatorSet中没有设置循环的函数，所以AnimatorSet监听器中永远无法运行到onAnimationRepeat()中
 *
 * 四、通用函数：
 * public AnimatorSet setDuration(long duration)              --- 设置单次动画时长
 * public void setInterpolator(TimeInterpolator interpolator) --- 设置加速器
 * public void setTarget(Object target)                       --- 设置ObjectAnimator动画目标控件
 *
 * -这些函数在AnimatorSet中设置以后，会覆盖单个ObjectAnimator中的设置；
 * -即如果AnimatorSet中没有设置，那么就以ObjectAnimator中的设置为准，如果AnimatorSet中设置以后，ObjectAnimator中的设置就会无效。
 *
 * 五、AnimatorSet之setStartDelay(long startDelay)
 * public void setStartDelay(long startDelay) --- 设置延时开始动画时长
 * - AnimatorSet中setStartDelay的延时是仅针对性的延长AnimatorSet激活时间的，对单个动画的延时设置没有影响。
 * - AnimatorSet真正激活延时 = AnimatorSet.startDelay+第一个动画.startDelay
 * - 在AnimatorSet激活之后，第一个动画绝对是会开始运行的，后面的动画则根据自己是否延时自行处理
 */

/* AnimatorSet针对ValueAnimator和ObjectAnimator都是适用的,由于一般我们不会用到ValueAnimator的组合动画，所以我们这篇仅讲解ObjectAnimator下的组合动画实现 */
class AnimatorSetActivity : AppCompatActivity() {

    companion object{
        private val TAG = AnimatorSetActivity::class.java.simpleName
    }

    private var animatorSet: AnimatorSet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_animator)

        btn_start_set1.setOnClickListener {
            doPlayTogetherAnim()
        }

        btn_start_set2.setOnClickListener {
            doPlaySequentiallyAnim()
        }

        btn_start_set3.setOnClickListener {
            doBudilderAnim()
        }
    }

    /**
     * playTogether函数的使用
     */
    private fun doPlayTogetherAnim() {
        /* 1、创建一个AnimatorSet */
        animatorSet = AnimatorSet()

        /* 2、构建一个或多个属性动画 */
        val tv1TranslateY: ObjectAnimator = ObjectAnimator.ofFloat(text_1, "translationY", 0f, 400f, 0f)
        val tv2TranslateY: ObjectAnimator = ObjectAnimator.ofFloat(text_1, "translationY", 0f, 400f, 0f)
        val tv2Rotation: ObjectAnimator = ObjectAnimator.ofFloat(text_1, "rotation", 0f, 360f, 0f)

        /* 3、使用playTogether */
        animatorSet!!.playTogether(tv1TranslateY, tv2Rotation, tv2TranslateY)
        animatorSet!!.duration = 2000

        /* 4、启动动画 */
        animatorSet!!.start()
    }

    /**
     * playSequentially函数实用实例
     */
    private fun doPlaySequentiallyAnim() {
        /* 1、创建一个AnimatorSet */
        animatorSet = AnimatorSet()

        /* 2、构建一个或多个属性动画 */
        val tv1TranslateY: ObjectAnimator = ObjectAnimator.ofFloat(text_2, "translationY", 0f, 400f, 0f)
        val tv2TranslateY: ObjectAnimator = ObjectAnimator.ofFloat(text_2, "translationY", 0f, 400f, 0f)
        val tv2Rotation: ObjectAnimator = ObjectAnimator.ofFloat(text_2, "rotation", 0f, 360f, 0f)

        /* 3、使用playSequentially */
        animatorSet!!.playSequentially(tv1TranslateY, tv2Rotation, tv2TranslateY)
        animatorSet?.duration = 2000

        /* 4、启动动画 */animatorSet?.start()
    }

    /**
     * AnimatorSet.Builder
     * AnimatorSet中play(),with(), before(),after()的使用
     */
    private fun doBudilderAnim() {
        /* 1、创建一个AnimatorSet */
        animatorSet = AnimatorSet()
        animatorSet!!.addListener(object : Animator.AnimatorListener {
            // 当AnimatorSet开始时调用
            override fun onAnimationStart(animation: Animator) {
                Log.d(TAG, "onAnimationStart")
            }

            //当AnimatorSet结束时调用
            override fun onAnimationEnd(animation: Animator) {
                Log.d(TAG, "onAnimationEnd")
            }

            //当AnimatorSet被取消时调用
            override fun onAnimationCancel(animation: Animator) {
                Log.d(TAG, "onAnimationCancel")
            }

            //当AnimatorSet重复时调用，由于AnimatorSet没有设置repeat的函数，所以这个方法永远不会被调用
            override fun onAnimationRepeat(animation: Animator) {
                Log.d(TAG, "onAnimationRepeat")
            }
        })

        /* 2、构建一个或多个属性动画 */
        val tv1TranslateY: ObjectAnimator = ObjectAnimator.ofFloat(text_3, "translationY", 0f, 400f, 0f)
        val tv2TranslateY: ObjectAnimator = ObjectAnimator.ofFloat(text_3, "translationY", 0f, 400f, 0f)
        val tv2Rotation: ObjectAnimator = ObjectAnimator.ofFloat(text_3, "rotation", 0f, 360f, 0f)

        /* 3、使用 */
        //方式一：使用builder对象逐个添加动画
        val builder = animatorSet!!.play(tv1TranslateY)
        builder.with(tv2Rotation)
        builder.after(tv2TranslateY)
        //方式二：串行方式
        //animatorSet.play(tv1TranslateY).with(tv2Rotation).after(tv2TranslateY);

        /* 4、启动动画 */
        animatorSet!!.duration = 2000
        animatorSet!!.start()
    }
}