package com.example.androiddemo.jetpack.databinding.util

import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.androiddemo.R

/**
 * 在BindingAdapters.kt中使用@BindingAdapter定义和自定义属性绑定的方法，DataBinding框架会使用方法绑定的自定义属性值作为这些方法的参数
 */

//单个属性
@BindingAdapter(value = ["hideIfZero"])
fun hideIfZero(view: View, count: Int){
    view.visibility = if(count == 0) View.INVISIBLE else View.VISIBLE
}

//多个属性，requireAll为true表示所有属性都要定义在xml中
@BindingAdapter(value = ["max", "changeColor"], requireAll = true)
fun TextView.changeColor(max: Int, count: Int){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if(count >= max){
            setTextColor(context.getColor(R.color.colorPrimary))
        }
    }
}
