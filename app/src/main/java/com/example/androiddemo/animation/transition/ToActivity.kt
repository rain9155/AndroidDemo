package com.example.androiddemo.animation.transition

import android.os.Build
import android.os.Bundle
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionInflater
import android.view.Window
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.R

/**
 * Activity的转场动画:
 *
 * ---方式1：
 * 在代码中设置，从FromActivity转到ToActivity，在ToActivity中的操作步骤：
 * (1)如果使用进入和退出效果：
 * 1、在ToActivity中要设置以下代码:
 * getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
 * 或以下样式文件：
 * <item name="android:windowContentTransitions">true</item>
 * 2、设置动画效果:
 * - 进入活动动画：
 * getWindow().setEnterTransition(new Explode());
 * getWindow().setEnterTransition(new Slide());
 * getWindow().setEnterTransition(new Fade());
 * 或
 * 使用TransitionInflater的inflateTransition方法从xml文件加载Explode、Slide、Fade
 * - 离开活动动画
 * getWindow().setExitTransition(new Explode());
 * getWindow().setExitTransition(new Slide());
 * getWindow().setExitTransition(new Fade());
 * 或
 * 使用TransitionInflater的inflateTransition方法从xml文件加载Explode、Slide、Fade
 * (2)如果使用共享元素：
 * 1、在布局文件中设置 android:transitionName="XX"，XX与MainActivity相同
 */
class ToActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        setContentView(R.layout.activity_to)

        val flag: Int = intent?.extras?.getInt("flag") ?: 3
        when (flag) {
            0 -> window.enterTransition = //通过xml文件加载Explode
                TransitionInflater.from(this).inflateTransition(R.transition.transition_explode) as Explode
            1 -> window.enterTransition = Slide()
            2 -> window.enterTransition = Fade()
            3 -> Toast.makeText(this, "Share Element", Toast.LENGTH_SHORT).show()
        }
    }
}