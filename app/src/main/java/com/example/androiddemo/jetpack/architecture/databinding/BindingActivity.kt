package com.example.androiddemo.jetpack.architecture.databinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.MutableDouble
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import com.example.androiddemo.R
import com.example.androiddemo.databinding.ActivityBindingBinding
import kotlinx.android.synthetic.main.activity_binding.*

/**
 * 参考：
 * https://developer.android.com/topic/libraries/data-binding
 * https://www.jianshu.com/p/e3b881d80c6d
 *
 * 数据绑定DataBinding：
 * DataBinding库可以把数据源直接绑定到视图中，每当数据更新时，视图能自动更新，从而避免我们手动更新视图的操作
 * 所以使用DataBinding可以减少的Activity/Fragment代码量，通过DataBinding可以更容易的实现MVVM
 *
 * 组成：
 * <layout><layout>: 里面包含<data><data> + 视图的根布局，其中<data>标签中可以通过<variable>标签定义变量
 * DataBindingUtil：用于生成XXBinging类，XXBinging类的内容根据<layout><layout>中的绑定关系生成
 *
 * 基本使用：
 * 1、视图的根布局使用<layout><layout>包裹；
 * 2、通过DataBindingUtil的setContentView/inflate/bind方法来进行视图的绑定，生成XXBinding类；
 * 3、给XXBinding类中定义的variable变量赋值
 *
 * ps：与视图进行绑定的数据源最好是可观察的，这样数据源更新时才能自动反馈到UI上，可以使用DataBinding的ObservableXX类包裹，或使用LiveData包裹
 */
class BindingActivity : AppCompatActivity() {

    companion object{
        private val TAG = BindingActivity.javaClass.simpleName
    }

    lateinit var binding: ActivityBindingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /** 2、通过DataBindingUtil的相应方法来进行视图的绑定，生成XXBinding类； */
        binding = DataBindingUtil.setContentView(this, R.layout.activity_binding)

        /** 3、给XXBinding类中定义的variable变量赋值 */
        binding.viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        binding.lifecycleOwner = this

    }

}

class MyViewModel: ViewModel(){

    private var _likeCount: MutableLiveData<Int> = MutableLiveData(0)

    var likeCount: LiveData<Int> = _likeCount
    var popularity: LiveData<Int> = Transformations.map(_likeCount){
        it * 10
    }

    fun onLike(){
        _likeCount.value = (_likeCount.value ?: 0) + 1
    }

}

