package com.example.androiddemo.animation.animation

import android.os.Bundle
import android.view.animation.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.R
import com.example.androiddemo.databinding.ActivityAnimBinding
import com.example.androiddemo.databinding.ActivityAnimationBinding

/**
 * 视图动画之通过java代码实现alpha、scale、translate、rotate、set动画效果:
 * scale --- 有三个构造函数
 * ScaleAnimation(Context context, AttributeSet attrs)  从XML文件加载动画，基本用不到
 * ScaleAnimation(float fromX, float toX, float fromY, float toY)
 * ScaleAnimation(float fromX, float toX, float fromY, float toY, float pivotX, float pivotY)
 * ScaleAnimation(float fromX, float toX, float fromY, float toY, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)
 *
 * alpha --- 有俩个构造函数
 * AlphaAnimation(Context context, AttributeSet attrs)  同样，从本地XML加载动画，基本不用
 * AlphaAnimation(float fromAlpha, float toAlpha)
 *
 * rotate --- 有四个构造函数
 * RotateAnimation(Context context, AttributeSet attrs)　　从本地XML文档加载动画，同样，基本不用
 * RotateAnimation(float fromDegrees, float toDegrees)
 * RotateAnimation(float fromDegrees, float toDegrees, float pivotX, float pivotY)
 * RotateAnimation(float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)
 *
 * translate --- 有三个构造函数
 * TranslateAnimation(Context context, AttributeSet attrs)  同样，基本不用
 * TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta)
 * TranslateAnimation(int fromXType, float fromXValue, int toXType, float toXValue, int fromYType, float fromYValue, int toYType, float toYValue)
 *
 * set --- 俩个构造函数
 * AnimationSet(Context context, AttributeSet attrs)  同样，基本不用
 * AnimationSet(boolean shareInterpolator)
 * shareInterpolator --- 取值true或false，取true时，指在AnimationSet中定义一个插值器（interpolator），它下面的所有动画共同。如果设为false，则表示它下面的动画自己定义各自的插值器。
 */
/**
 * 在标签属性android:pivotX(android:pivotY)中有三种取值，数，百分数，百分数p,体现在构造函数中，就是最后一个构造函数的pivotXType(pivotYType)
 * 它的取值有三个，Animation.ABSOLUTE，Animation.RELATIVE_TO_SELF，Animation.RELATIVE_TO_PARENT
 */
class AnimationActivity : AppCompatActivity() {

    companion object{
        private const val DURATION = 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAnimationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* 缩放动画 */
        val scaleAnimation = ScaleAnimation(
            0f, 2f,
            0f, 2f,
            Animation.RELATIVE_TO_SELF, 0.5f,//设置x轴方向的旋转参考系为自身中心点
            Animation.RELATIVE_TO_SELF, 0.5f//设置y轴方向的旋转参考系为自身中心点
        )
        scaleAnimation.run {
            interpolator = LinearInterpolator()//设置插值器
            duration =
                DURATION
        }
        binding.btnScale.setOnClickListener{
            binding.textView.startAnimation(scaleAnimation)
        }

        /* 调节透明度动画 */
        val alphaAnimation = AlphaAnimation(0f, 1f)
        alphaAnimation.run {
            interpolator = CycleInterpolator(180f)
            duration =
                DURATION
        }
        binding.btnAlpha.setOnClickListener{
            binding.textView.startAnimation(alphaAnimation)
        }

        /* 旋转动画 */
        val rotateAnimation = RotateAnimation(
            360f, 0f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnimation.run {
            interpolator = DecelerateInterpolator()
            duration =
                DURATION
        }
        binding.btnRotate.setOnClickListener{
            binding.textView.startAnimation(rotateAnimation)
        }

        /* 平移动画 */
        val translateAnimation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, -0.5f,
            Animation.RELATIVE_TO_SELF, 2.0f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        translateAnimation.run {
            interpolator = AccelerateInterpolator()
            duration =
                DURATION
        }
        binding.btnTranslate.setOnClickListener{
            binding.textView.startAnimation(translateAnimation)
        }

        /* 几个不同的动画定义成一个组 */
        val setAnimation = AnimationSet(false)
        setAnimation.run {
            duration =
                DURATION
            animations.add(scaleAnimation)
            animations.add(alphaAnimation)
            animations.add(translateAnimation)
            animations.add(rotateAnimation)
        }
        binding.btnSet.setOnClickListener{
            binding.textView.startAnimation(setAnimation)
        }

        binding.textView.setOnClickListener {
            Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT)
                .show()
        }
    }
}