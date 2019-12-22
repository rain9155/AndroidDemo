package com.example.androiddemo.drawabletest

import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.LevelListDrawable
import android.graphics.drawable.ScaleDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.R
import kotlinx.android.synthetic.main.activity_drawable.*

/**
 * 各种Drawable使用示例:
 * 1、LevelListDrawable
 * 2、TransitionDrawable
 * 3、ScaleDrawable
 * 4、ClipDrawable
 * 5、ShapeDrawable
 * 6、StateListDrawable
 * 7、LayerDrawable
 * 8、BitmapDrawable
 * 9、InsetDrawable
 * 其他演示在xml文件中
 */
class DrawableActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawable)

        /* LevelListDrawable的使用 */
        btn_level_list.setOnClickListener {
            val levelListDrawable = btn_level_list.background as LevelListDrawable
            levelListDrawable.level = 1
        }

        /* TransitionDrawable的使用，通过startTransition()和revereTransition()实现淡入淡出效果和反序过程 */
        btn_transition.setOnClickListener {
            val transitionDrawable = btn_transition.background as TransitionDrawable
            transitionDrawable.startTransition(1000)
        }

        /* ScaleDrawable使用，设置等级大于0小于10000 */
        val scaleDrawable = btn_scale.background as ScaleDrawable
        scaleDrawable.level = 1000

        /* ClipDrawable使用， 设置等级大于0(0代表完全裁剪)小于10000（10000等于不裁剪） */
        val clipDrawable = btn_clip.background as ClipDrawable
        clipDrawable.level = 5000
    }
}