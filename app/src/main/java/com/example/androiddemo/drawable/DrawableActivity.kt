package com.example.androiddemo.drawable

import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.LevelListDrawable
import android.graphics.drawable.ScaleDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.databinding.ActivityDrawableBinding

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
        val binding = ActivityDrawableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* LevelListDrawable的使用 */
        binding.btnLevelList.setOnClickListener {
            val levelListDrawable = binding.btnLevelList.background as LevelListDrawable
            levelListDrawable.level = 1
        }

        /* TransitionDrawable的使用，通过startTransition()和revereTransition()实现淡入淡出效果和反序过程 */
        binding.btnTransition.setOnClickListener {
            val transitionDrawable = binding.btnTransition.background as TransitionDrawable
            transitionDrawable.startTransition(1000)
        }

        /* ScaleDrawable使用，设置等级大于0小于10000 */
        val scaleDrawable = binding.btnScale.background as ScaleDrawable
        scaleDrawable.level = 1000

        /* ClipDrawable使用， 设置等级大于0(0代表完全裁剪)小于10000（10000等于不裁剪） */
        val clipDrawable = binding.btnClip.background as ClipDrawable
        clipDrawable.level = 5000
    }
}