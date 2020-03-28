package com.example.androiddemo.animationtest.animation

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.R
import kotlinx.android.synthetic.main.activity_animation1.*

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
class AnimationActivity1 : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation1)

        /* 缩放动画 */
        //1、通过 AnimationUtils.loadAnimation(this, R.anim.XX_anim)从XML文件中获取动画
        val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_scale)
        btn_scale.setOnClickListener {
            //2、利用startAnimation将动画传递给指定控件显示
            text_view.startAnimation(scaleAnimation)
        }
        /* 调节透明度动画 */
        val alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha)
        btn_alpha.setOnClickListener {
            text_view.startAnimation(alphaAnimation)
        }

        /* 旋转动画 */
        val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate)
        btn_rotate.setOnClickListener {
            text_view.startAnimation(rotateAnimation)
        }

        /* 平移动画 */
        val translateAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_translate)
        btn_translate.setOnClickListener {
            text_view.startAnimation(translateAnimation)
        }

        /* 几个不同的动画定义成一个组 */
        val setAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_set)
        btn_set.setOnClickListener{
            text_view.startAnimation(setAnimation)
        }

        text_view.setOnClickListener {
            Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT)
                .show()
        }
    }
}