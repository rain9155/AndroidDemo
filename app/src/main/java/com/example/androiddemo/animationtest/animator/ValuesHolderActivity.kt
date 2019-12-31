package com.example.androiddemo.animationtest.animator

import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.R
import kotlinx.android.synthetic.main.activity_holder_animator.*

/**
 * 属性动画之PropertyValuesHolder --- 不需要使用AnimatorSet，也能实现多个动画同时播放：
 * 前面的ObjectAnimator,ValueAnimator介绍的构造只能对一个属性同时实现一个动画
 * 如果要针对同一个对象的多个属性同时作用多个动画，可以使用PropertyValuesHolder封装多个动画状态
 *
 * 构造器：
 * public static PropertyValuesHolder ofFloat(String propertyName, float... values)
 * public static PropertyValuesHolder ofInt(String propertyName, int... values)
 * public static PropertyValuesHolder ofObject(String propertyName, TypeEvaluator evaluator,Object... values)
 * propertyName ，values，evaluator --- 含义都与ObjectAnimator,ValueAnimator中的一样
 * public static PropertyValuesHolder ofKeyframe(String propertyName, Keyframe... values)
 * propertyName --- 动画所要操作的属性名
 * int, float...--- 可变参数长参数,可以传入任何数量的值,传进去的值列表，就表示动画时的变化范围
 * KeyFrame...  --- 关键帧列表，根据动画的显示进度，控制动画速率的变化，PropertyValuesHolder会根据每个Keyframe的设定，定时将指定的值输出给动画
 *                  构造器：
 *                  public static Keyframe ofFloat(float fraction, float value)
 *                  public static Keyframe ofFloat(float fraction, float value)
 *                  public static Keyframe ofObject(float fraction, Object value)
 *                  fraction --- 表示当前的显示进度，即从加速器中getInterpolation()函数的返回值
 *                  value    --- 表示当前动画应该在的位置
 *                  常用函数：
 *                  void setFraction(float fraction)  --- 设置fraction参数，即Keyframe所对应的进度
 *                  void setValue(Object value) --- 设置当前Keyframe所对应的值
 *                  void setInterpolator(TimeInterpolator interpolator) --- 设置Keyframe动作期间所对应的插值器
 *
 * 常用函数：
 * void setEvaluator(TypeEvaluator evaluator) --- 设置动画的Evaluator
 * void setFloatValues(float... values)       --- 用于设置ofFloat所对应的动画值列表
 * void setIntValues(int... values)           --- 用于设置ofInt所对应的动画值列表
 * void setKeyframes(Keyframe... values)      --- 用于设置ofKeyframe所对应的动画值列表
 * void setObjectValues(Object... values)     --- 用于设置ofObject所对应的动画值列表
 * void setPropertyName(String propertyName   --- 设置动画属性名
 */

/* *
 * ObjectAnimator,ValueAnimator中关于PropertyValuesHolder的构造：
 * public static ValueAnimator ofPropertyValuesHolder(PropertyValuesHolder... values)
 * public static ObjectAnimator ofPropertyValuesHolder(Object target,PropertyValuesHolder... values)
 * values --- 是一个可变长参数，可以传进去多个PropertyValuesHolder实例
 * target --- 指需要执行动画的控件
 */

/* ValueAnimator不常用，以下使用ObjectAnimator示例 */

class ValuesHolderActivity : AppCompatActivity() {

    private var objectAnimator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holder_animator)

        //启动PropertyValuesHolder动画
        btn_start.setOnClickListener(View.OnClickListener {
            doAnimator()
        })

        text_view.setOnClickListener(View.OnClickListener {
            Toast.makeText(
                this@ValuesHolderActivity,
                "Hello!",
                Toast.LENGTH_SHORT
            ).show()
        })

        //启动KeyFrame动画
        btn_start_keyFame.setOnClickListener {
            doAnimator2()
        }
    }

    /**
     * PropertyValuesHolder使用
     */
    private fun doAnimator() {
        /* 1、创建PropertyValuesHolder实例，封装一个或多个动画属性 */
        val pvh1 = PropertyValuesHolder.ofFloat("rotation", 60f, -60f, 40f, -40f, -20f, 20f, 10f, -10f, 0f)
        val pvh2 = PropertyValuesHolder.ofInt("BackgroundColor", -0x1, -0xff01, -0x100, -0x1, 0x00000000, -0xff01)
        val pvh3 = PropertyValuesHolder.ofFloat("scaleX", 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 1f)

        /* 2、使用ObjectAnimator的ofPropertyValuesHolder构造，把刚创建的PropertyValuesHolder实例传进去 */
        objectAnimator = ObjectAnimator.ofPropertyValuesHolder(text_view, pvh1, pvh2, pvh3)

        /* 3、设置动画相关 */
        objectAnimator?.duration = 1000


        /* 4、启动动画 */
        objectAnimator?.start()
    }

    /**
     * Keyframe使用
     */
    private fun doAnimator2() {
        /* 1、生成Keyframe对象 */
        /**
         * 注意：
         * 如果去掉第0帧，将以第一个关键帧为起始位置
         * 如果去掉结束帧，将以最后一个关键帧为结束位置
         * 使用Keyframe来构建动画，至少要有两个或两个以上帧
         */
        //rotation帧
        val frame0 = Keyframe.ofFloat(0f, 0f)
        val frame1 = Keyframe.ofFloat(0.1f, -20f)
        val frame2 = Keyframe.ofFloat(0.2f, 20f)
        val frame3 = Keyframe.ofFloat(0.3f, -20f)
        val frame4 = Keyframe.ofFloat(0.4f, 20f)
        val frame5 = Keyframe.ofFloat(0.5f, -20f)
        val frame6 = Keyframe.ofFloat(0.6f, 20f)
        val frame7 = Keyframe.ofFloat(0.7f, -20f)
        val frame8 = Keyframe.ofFloat(0.8f, 20f)
        val frame9 = Keyframe.ofFloat(0.9f, -20f)
        val frame10 = Keyframe.ofFloat(1f, 0f)
        //scaleX帧
        val f0 = Keyframe.ofFloat(0f, 1f)
        val f1 = Keyframe.ofFloat(0.1f, 1.1f)
        val f2 = Keyframe.ofFloat(0.2f, 1.2f)
        val f3 = Keyframe.ofFloat(0.3f, 1.3f)
        val f4 = Keyframe.ofFloat(0.4f, 1.4f)
        val f5 = Keyframe.ofFloat(0.5f, 1.5f)
        val f6 = Keyframe.ofFloat(0.6f, 1.4f)
        val f7 = Keyframe.ofFloat(0.7f, 1.3f)
        val f8 = Keyframe.ofFloat(0.8f, 1.3f)
        val f9 = Keyframe.ofFloat(0.9f, 1.1f)
        val f10 = Keyframe.ofFloat(1f, 1f)

        /* 2、利用PropertyValuesHolder.ofKeyframe()生成PropertyValuesHolder对象  */
        val pvh1 = PropertyValuesHolder.ofKeyframe(
            "rotation",
            frame0,
            frame1,
            frame2,
            frame3,
            frame4,
            frame5,
            frame6,
            frame7,
            frame8,
            frame9,
            frame10
        )
        val phv2 = PropertyValuesHolder.ofKeyframe(
            "scaleX",
            f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10)

        /* 3、ObjectAnimator.ofPropertyValuesHolder()生成对应的Animator */
        objectAnimator = ObjectAnimator.ofPropertyValuesHolder(image_view, pvh1, phv2)
        objectAnimator?.duration = 1000
        objectAnimator?.start()
    }
}