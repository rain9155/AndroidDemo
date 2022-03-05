package com.example.androiddemo.animation.layoutanim

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.databinding.ActivityLayoutTransitionBinding

/**
 * 布局动画之animateLayoutChanges属性与LayoutTransition类:
 * 实现在ViewGroup创建后添加控件仍能应用动画

 * 一、animateLayoutChanges属性使用
 * 在布局文件加入android:animateLayoutChanges=[true]即可实现内部控件添加删除时都加上动画效果
 *
 * 二、LayoutTransition类使用
 *
 * 主要方法：
 * public void setAnimator(int transitionType, Animator animator)
 * transitionType --- 表示当前应用动画的对象范围，取值有：
 *                                    APPEARING —— 元素在容器中出现时所定义的动画。
 *                                    DISAPPEARING —— 元素在容器中消失时所定义的动画。
 *                                    CHANGE_APPEARING —— 由于容器中要显现一个新的元素，其它需要变化的元素所应用的动画
 *                                    CHANGE_DISAPPEARING —— 当容器中某个元素消失，其它需要变化的元素所应用的动画
 * animator      --- 表示当前所选范围的控件所使用的动画
 *
 * 常用方法：
 * public void setDuration(long duration) --- 设置所有动画完成所需要的时长
 * 以下：transitionType取值为：APPEARING、DISAPPEARING、CHANGE_APPEARING、CHANGE_DISAPPEARING
 * public void setDuration(int transitionType, long duration) --- 针对单个type，设置动画时长；
 * public void setInterpolator(int transitionType, TimeInterpolator interpolator) --- 针对单个type设置插值器
 * public void setStartDelay(int transitionType, long delay)   --- 针对单个type设置动画延时
 * public void setStagger(int transitionType, long duration --- 针对单个type设置，每个子item动画的时间间隔
 *
 * LayoutTransition设置监听:
 * public void addTransitionListener(TransitionListener listener)
 */

class LayoutTransitionActivity : AppCompatActivity() {

    companion object{
        private val TAG = LayoutAnimationActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLayoutTransitionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //给ll_items2布局添加ViewGroup动画
        doLayoutTransition(binding.llItems2)

        //添加item
        binding.btnAdd.setOnClickListener {
            val textView = TextView(this)
            textView.text = "item1"
            textView.textSize = 20f
            textView.setBackgroundColor(Color.RED)
            textView.setPadding(20, 20, 20, 20)
            textView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            binding.llItems.addView(textView, 0)

            val textView2 = TextView(this)
            textView2.text = "item1"
            textView2.textSize = 20f
            textView2.setBackgroundColor(Color.RED)
            textView2.setPadding(20, 20, 20, 20)
            textView2.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            binding.llItems2.addView(textView2, 0)
        }

        //删除控件
        binding.btnRemove.setOnClickListener{
            if(binding.llItems.childCount != 0){
                binding.llItems.removeViewAt(binding.llItems.childCount - 1)
            }

            if(binding.llItems2.childCount != 0){
                binding.llItems2.removeViewAt(binding.llItems2.childCount - 1)
            }
        }
    }

    /**
     * LayoutTransition类使用
     */
    @SuppressLint("ObjectAnimatorBinding")
    private fun  doLayoutTransition(linearLayout: LinearLayout){
        /* 1、创建LayoutTransition */
        val layoutTransition = LayoutTransition()

        /* 2、创建动画(进入控件的动画，离开控件的动画) */
        //item添加动画 - LayoutTransition.APPEARING
        val enterAnim = ObjectAnimator.ofFloat(null, "scaleX", 0f, 1f)
        //item删除动画 - LayoutTransition.DISAPPEARING
        val leaveAnim = ObjectAnimator.ofFloat(null, "scaleX", 1f, 0f)

        //注意：
        //1、LayoutTransition.CHANGE_APPEARING和LayoutTransition.CHANGE_DISAPPEARING必须使用PropertyValuesHolder所构造的动画才会有效果，不然无效
        //2、在构造PropertyValuesHolder动画时，”left”、”top”属性的变动是必写的
        //3、在构造PropertyValuesHolder时，所使用的ofInt,ofFloat中的参数值，第一个值和最后一个值必须相同，不然此属性所对应的的动画将被放弃
        //4、在构造PropertyValuesHolder时，所使用的ofInt,ofFloat中，如果所有参数值都相同，也将不会有动画效果

        //某个item添加，其他item的动画 - CHANGE_APPEARING动画
        val pvhLeft = PropertyValuesHolder.ofInt("left", 0, 0) //必须
        val pvhTop = PropertyValuesHolder.ofInt("top", 1, 1) //必须
        val phv1 = PropertyValuesHolder.ofFloat("rotation", 0f, 360f, 0f)
        val changeEnterAnim = ObjectAnimator.ofPropertyValuesHolder(linearLayout, phv1, pvhLeft, pvhTop)
        //某个item删除，其他item的动画 - CHANGE_DISAPPEARING动画
        val pvhLeft2 = PropertyValuesHolder.ofInt("left", 0, 0) //必须
        val pvhTop2 = PropertyValuesHolder.ofInt("top", 0, 0) //必须
        val phv2 = PropertyValuesHolder.ofFloat("rotation", 360f, 0f, 360f)
        val changeLeaveAnim = ObjectAnimator.ofPropertyValuesHolder(linearLayout, phv2, pvhLeft2, pvhTop2)

        /* 3、设置动画类型：transitionType */
        layoutTransition.setAnimator(LayoutTransition.APPEARING, enterAnim)
        layoutTransition.setAnimator(LayoutTransition.DISAPPEARING, leaveAnim)
        layoutTransition.setAnimator(LayoutTransition.CHANGE_APPEARING, changeEnterAnim)
        layoutTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, changeLeaveAnim)

        /* 4、将LayoutTransaction设置进ViewGroup */
        linearLayout.layoutTransition = layoutTransition

        /**
         * LayoutTransition设置监听
         * LayoutTransition transition：当前的LayoutTransition实例
         * ViewGroup container：当前应用LayoutTransition的container
         * View view：当前在做动画的View对象
         * int transitionType：当前的LayoutTransition类型，取值有：APPEARING、DISAPPEARING、CHANGE_APPEARING、CHANGE_DISAPPEARING
         */
        layoutTransition.addTransitionListener(object : LayoutTransition.TransitionListener {

            override fun startTransition(
                transition: LayoutTransition,
                container: ViewGroup,
                view: View,
                transitionType: Int
            ) {
                Log.d(
                    TAG,
                    "start:" + "transitionType:" + transitionType + "count:" + container.childCount + "view:" + view.javaClass.name
                )
            }

            override fun endTransition(
                transition: LayoutTransition,
                container: ViewGroup,
                view: View,
                transitionType: Int
            ) {
                Log.d(
                    TAG,
                    "start:" + "transitionType:" + transitionType + "count:" + container.childCount + "view:" + view.javaClass.name
                )
            }
        })
    }
}
