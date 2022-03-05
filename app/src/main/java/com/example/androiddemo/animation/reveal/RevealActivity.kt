package com.example.androiddemo.animation.reveal

import android.animation.Animator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import androidx.annotation.RequiresApi
import com.example.androiddemo.databinding.ActivityRevealBinding
import kotlin.math.hypot

/**
 * 揭露动画:
 * 使用揭露动画非常简单，Android Sdk 中已经帮我们提供了一个工具类 ViewAnimationUtils 来创建揭露动画
 * ViewAnimationUtils里面只有一个静态方法createCircularReveal，返回一个 Animator 动画对象
 *
 * ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius) :  Animator
 * view    --- 是要执行揭露动画的 View 视图
 * centerX --- 动画开始时，相对于视图 View 的坐标系，动画圆的中心的x坐标
 * centerY --- 动画开始时，相对于视图 View 的坐标系，动画圆的中心的y坐标
 * startRadius --- 动画圆的起始半径
 * endRadius --- 动画圆的结束半径
 */
class RevealActivity : AppCompatActivity() {

    private var isReveal : Boolean = false //要揭露的View是否揭露了

    private lateinit var binding: ActivityRevealBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRevealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingButton.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                launchRevealAnimation()
            }
        }
        binding.toolBar.setNavigationOnClickListener{
            finish()
        }

        isReveal = binding.ivReveal.visibility == View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun launchRevealAnimation() {
        val animation = binding.ivReveal.animation
        if(animation != null){
           return
        }

        val location = IntArray(2)
        binding.floatingButton.getLocationInWindow(location)

        //求出扩散圆的中心坐标
        val x = location[0] + binding.floatingButton.width / 2
        val y = location[1] + binding.floatingButton.height / 2
        //求出要揭露 View 的对角线，来作为扩散圆的最大半径
        val radius = hypot(binding.ivGirl.width.toDouble(), binding.ivGirl.height.toDouble()).toFloat()

        if(isReveal){//已经揭露了，隐藏View
            val revealAnimator = ViewAnimationUtils.createCircularReveal(binding.ivReveal, x, y, radius, 0f)
            revealAnimator.run {
                duration = 800
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        binding.ivReveal.visibility = View.GONE
                        revealAnimator.removeListener(this);                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                })
                start()
            }
            isReveal = false
        }else {//没有揭露，显示View
            val revealAnimator = ViewAnimationUtils.createCircularReveal(binding.ivReveal, x, y, 0f, radius)
            revealAnimator.run {
                duration = 800
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        revealAnimator.removeListener(this);                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                        binding.ivReveal.visibility = View.VISIBLE
                    }

                })
                start()
            }
            isReveal = true
        }
    }
}
