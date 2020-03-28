package com.example.androiddemo.animation.transition

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionInflater
import android.util.Pair
import android.view.View
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.R
import kotlinx.android.synthetic.main.activity_from.*

/**
 * 一、实现Activity转场动画的方式1（Android5.0后才出现）:
 *
 * 进入和退出效果：
 * explode：分解效果，从屏幕中间进或出，移动视图
 * slide：滑动效果，从屏幕边缘进或出，移动视图
 * fade：淡出效果，通过改变屏幕上的透明度达到添加或移除视图
 * 共享元素：
 * FromActivity和ToActivity都拥有的元素，在跳转中，共享元素通过动画效果显示出来
 *
 * 1)在代码中设置，从FromActivity转到ToActivity，在FromActivity中的操作步骤：
 * (1)如果使用进入和退出效果：
 * 1、启动活动时，使用startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
 * (2)如果使用共享元素：
 * 1、在布局文件中设置 android:transitionName="XX"
 * 2、启动活动时:
 * （共享一个）
 * 使用startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, view, "share").toBundle());
 * （共享多个）
 * 使用startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this,
 * Pair.create(view, "share"),
 * Pair.create(fad_button, "fad")).
 * toBundle());
 *
 * 系统提供的Translation有：
 * changeBounds：捕获改变目标视图的布局边界的变化，也就是改变view的宽高或者位置
 * changeClipBounds：捕获裁剪目标视图边界变化，view的裁剪区域边界
 * changeTransform： 捕获改变目标的缩放比例和旋转角度变化，即对view进行缩放，旋转操作
 * changeImageTransform：捕获改变目标图片的大小和缩放比例变化，也就是改变图片的ScaleType
 * arcMotion：圆弧效果
 * Slide:
 * Explode
 * Fade
 * Overlay
 * AutoTransition(默认)
 * ...
 *
 * 2)在 style 中设置:
 * 1、在主题中添加<item name="android:windowContentTransitions">true</item>
 * 2、在res中创建transition目录并新建slide或Explode或Fade动画
 * 3、在主题中引用
 *  <item name="android:windowEnterTransition">@transition/transition_fade</item>
 *  <item name="android:windowExitTransition">@transition/transition_slide</item>
 *
 * 二、实现Activity转场动画的方式2（Android5.0之前就有）：
 *
 * 1)使用 overridePendingTransition 方法实现 Activity 跳转动画：
 * overridePendingTransition 方法需要在 startActivity 方法或者是 finish 方法调用之后立即执行：
 * public void overridePendingTransition(int enterAnim, int exitAnim) {...}
 * enterAnim --- 表示的是从 Activity 1 跳转到 Activity 2，进入 2 时的动画效果
 * exitAnim --- 表示的是从 Activity 1 跳转到 Activity 2，离开 1 时的动画效果
 * 若进入 2 或者是离开 1 时不需要动画效果，则可以传值为 0
 *
 * 2)定义具体的 AppTheme样式并应用到 Application 的 style：
 *  <item name="android:windowAnimationStyle">@style/activityAnim</item>
 *  <!-- 使用style方式定义activity切换动画 -->
 * <style name="activityAnim">
 * <item name="android:activityOpenEnterAnimation">@anim/slide_in_top</item>
 * <item name="android:activityOpenExitAnimation">@anim/slide_in_top</item>
 *  ...
 * </style>
 * 而在 windowAnimationStyle 中存在四种动画：
 * activityOpenEnterAnimation // 用于设置打开新的 Activity 并进入新的 Activity 展示的动画
 * activityOpenExitAnimation // 用于设置打开新的 Activity 并销毁之前的 Activity 展示的动画
 * activityCloseEnterAnimation // 用于设置关闭当前 Activity 进入上一个 Activity 展示的动画
 * activityCloseExitAnimation // 用于设置关闭当前 Activity 时展示的动画
*/
class FromActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        }
        setContentView(R.layout.activity_from)
    }

    /**
     * 分解效果，从屏幕中间进或出，移动视图
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun explode(view: View?) {
        val intent = Intent(this@FromActivity, ToActivity::class.java)
        intent.putExtra("flag", 0)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        val slide = TransitionInflater.from(this).inflateTransition(R.transition.transition_slide) as Slide
    }

    /**
     * 滑动效果，从屏幕边缘进或出，移动视图
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun slide(view: View?) {
        val intent = Intent(this@FromActivity, ToActivity::class.java)
        intent.putExtra("flag", 1)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }

    /**
     * 淡出效果，通过改变屏幕上的透明度达到添加或移除视图
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun fade(view: View?) {
        val intent = Intent(this@FromActivity, ToActivity::class.java)
        intent.putExtra("flag", 2)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }

    /**
     * 共享元素
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun share(view: View) {
        val intent = Intent(this@FromActivity, ToActivity::class.java)
        intent.putExtra("flag", 3)
        //共享单个元素
//        startActivity(
//            intent,
//            ActivityOptions.makeSceneTransitionAnimation(
//            this,
//            share_element1,
//            "element1").toBundle()
//        )
        //共享多个元素
        val element1: View = share_element1 //使用共享，必须为View
        val element2: View = share_element2
        startActivity(
            intent,
            ActivityOptions.makeSceneTransitionAnimation(
                this,
                Pair.create(element1, "element1"),
                Pair.create(element2, "element2")).toBundle()
        )
    }

    /**
     * 使用 overridePendingTransition
     */
    fun overridePendingTransition(view: View) {
        val intent = Intent(this, ToActivity::class.java)
        intent.putExtra("flag", 4)
        startActivity(intent)
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
    }
}