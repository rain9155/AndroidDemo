package com.example.androiddemo.animation.animation

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.R
import com.example.androiddemo.databinding.ActivityAnimationXmlBinding

/**
 * 视图动画之alpha、scale、translate、rotate、set的xml属性及用法:
 * scale标签 --- 缩放动画
 * alpha标签 --- 调节透明度动画
 * rotate标签 --- 旋转动画
 * translate标签 --- 平移动画
 * set标签 --- 几个不同的动画定义成一个组
 *
 * Interpolator(插值器) --- 定义动画变换速率
 * AccelerateDecelerateInterpolator   在动画开始与介绍的地方速率改变比较慢，在中间的时候加速
 * AccelerateInterpolator             在动画开始的地方速率改变比较慢，然后开始加速
 * AnticipateInterpolator             开始的时候向后然后向前甩
 * AnticipateOvershootInterpolator    开始的时候向后然后向前甩一定值后返回最后的值
 * BounceInterpolator                 动画结束的时候弹起
 * CycleInterpolator                  动画循环播放特定的次数，速率改变沿着正弦曲线
 * DecelerateInterpolator             在动画开始的地方快然后慢
 * LinearInterpolator                 以常量速率改变
 * OvershootInterpolator              向前甩一定值后再回到原来位置
 */
class AnimationXmlActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAnimationXmlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* 缩放动画 */
        //1、通过 AnimationUtils.loadAnimation(this, R.anim.XX_anim)从XML文件中获取动画
        val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_scale)
        binding.btnScale.setOnClickListener {
            //2、利用startAnimation将动画传递给指定控件显示
            binding.textView.startAnimation(scaleAnimation)
        }
        /* 调节透明度动画 */
        val alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha)
        binding.btnAlpha.setOnClickListener {
            binding.textView.startAnimation(alphaAnimation)
        }

        /* 旋转动画 */
        val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate)
        binding.btnRotate.setOnClickListener {
            binding.textView.startAnimation(rotateAnimation)
        }

        /* 平移动画 */
        val translateAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_translate)
        binding.btnTranslate.setOnClickListener {
            binding.textView.startAnimation(translateAnimation)
        }

        /* 几个不同的动画定义成一个组 */
        val setAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_set)
        binding.btnSet.setOnClickListener{
            binding.textView.startAnimation(setAnimation)
        }

        binding.textView.setOnClickListener {
            Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT)
                .show()
        }
    }
}