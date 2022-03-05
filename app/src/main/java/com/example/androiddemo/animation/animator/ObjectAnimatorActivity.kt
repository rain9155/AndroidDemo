package com.example.androiddemo.animation.animator

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.animation.animator.view.OfObjectObjectAnimatorView
import com.example.androiddemo.animation.animator.view.Point
import com.example.androiddemo.databinding.ActivityObjectAnimatorBinding

/**
 * 属性动画之ObjectAnimator --- 继承于ValueAnimator:
 * 内部通过java反射机制来调用set函数修改对应控件对象的属性值
 *
 * 构造器：--- 构造类型的选择要与set函数的参数类型相关
 * public static ObjectAnimator ofFloat(Object target, String propertyName, float... values)
 * public static ObjectAnimator ofInt(Object target, String propertyName, int... values)
 * public static ObjectAnimator ofObject(Object target, String propertyName,TypeEvaluator evaluator, Object... values)
 * target       --- 用于指定这个动画要操作的是哪个控件
 * propertyName --- 用于指定这个动画要操作这个控件的哪个属性
 * values       --- 指这个属性变化的一个取值范围，和ValueAnimator的一样
 * evaluator    --- 自定义的Evaluator,根据当前动画的显示进度，计算出当前进度下对应的值
 *
 * public static ObjectAnimator ofPropertyValuesHolder(Object target,PropertyValuesHolder... values) ---  ValueAnimatorActivity中讲解
 *
 * 常用方法：
 * 大部分与ValueAnimator的一样
 *
 * 常用的ObjectAnimator可以直接使用（set）的属性动画的属性值，没有则自定义
 * 1、透明度：alpha
 * public void setAlpha(float alpha)               --- 表示View对象的透明度，默认值1（不透明），0代表完全透明
 *
 * 2、旋转度数：rotation、rotationX、rotationY
 * public void setRotation(float rotation)         --- 表示围绕Z旋转,rotation表示旋转度数
 * public void setRotationX(float rotationX)       --- 表示围绕X轴旋转，rotationX表示旋转度数
 * public void setRotationY(float rotationY)       --- 表示围绕Y轴旋转，rotationY表示旋转度数
 *
 * 3、平移：translationX、translationY
 * public void setTranslationX(float translationX) --- 表示在X轴上的平移距离,以当前控件为原点，向右为正方向，参数translationX表示移动的距离
 * public void setTranslationY(float translationY) --- 表示在Y轴上的平移距离，以当前控件为原点，向下为正方向，参数translationY表示移动的距离
 *
 * 4、缩放：scaleX、scaleY
 * public void setScaleX(float scaleX)             --- 在X轴上缩放，scaleX表示缩放倍数
 * public void setScaleY(float scaleY              --- 在Y轴上缩放，scaleY表示缩放倍数
 */

/* 如何使用ObjectAnimator */
class ObjectAnimatorActivity : AppCompatActivity() {

    var objectAnimator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityObjectAnimatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //启动ObjectAnimator动画
        binding.btnStartObject1.setOnClickListener {
            doAnimator(binding.textView)
        }

        binding.textView.setOnClickListener {
            Toast.makeText(
                this@ObjectAnimatorActivity,
                "Hello!",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnStartObject2.setOnClickListener {
            do2Animator(binding.ofObjectView)
        }
    }

    /**
     * 启动ObjectAnimator动画
     */
    private fun doAnimator(textView: TextView) {
        /* 1、通过ObjectAnimator的静态工厂方法，创建一个ObjectAnimator对象  */
        objectAnimator = ObjectAnimator.ofFloat(textView, "rotationX", 0f, 360f)

        /* 2、设置一些动画属性 */
        objectAnimator?.duration = 2000

        /* 3、启动动画 */
        objectAnimator?.start()
    }

    /**
     * ObjectAnimator.ofObject方法使用
     */
    private fun do2Animator(objectView: OfObjectObjectAnimatorView) {
        objectAnimator = ObjectAnimator.ofObject(
            objectView,
            "point",
            MyEvaluator(),
            Point(100f),
            Point(300f),
            Point(100f)
        )
        objectAnimator?.duration = 1000
        objectAnimator?.start()
    }

    /**
     * 自定义Evaluator，计算动画过程
     */
    private inner class MyEvaluator : TypeEvaluator<Point> {

        override fun evaluate(
            fraction: Float,
            startValue: Point,
            endValue: Point
        ): Point {
            val radius: Float = startValue.radius + fraction * (endValue.radius - startValue.radius)
            return Point(radius)
        }
    }

    /**
     * 如果一个属性没有get，set方法，可以自定义一个包装类，来间接的给这个属性增加get，set方法
     * 包装示例:
     * WrapperView wrapperView = new WrapperView(textView);
     * ObjectAnimator.ofInt(wrapperView, "width", 500).setDuration(1000).start();
     */
    class WrapperView(private val mTarget: View) {

        var width: Int
            get() = mTarget.layoutParams.width
            set(width) {
                mTarget.layoutParams.width = width
                mTarget.requestLayout()
            }

    }
}

