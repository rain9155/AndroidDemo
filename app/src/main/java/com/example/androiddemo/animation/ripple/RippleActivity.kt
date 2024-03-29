package com.example.androiddemo.animation.ripple

import android.content.res.TypedArray
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.R
import com.example.androiddemo.databinding.ActivityRippleBinding

/**
 * Ripple动画效果:
 * 1、系统自带：通过设置background和foreground属性
 * android:background="?android:attr/selectableItemBackground"
 * android:background="?android:attr/selectableItemBackgroundBorderless"
 * 2、自定义：在drawable下新建ripple文件
 */
class RippleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRippleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //通过代码来给TextView设置ripple效果（有界）
        val attrs = intArrayOf(R.attr.selectableItemBackground)
        val typedArray: TypedArray = obtainStyledAttributes(attrs)
        val backgroundResource = typedArray.getResourceId(0, 0)
        binding.tvPuppet1.setBackgroundResource(backgroundResource)
        typedArray.recycle()
    }
}