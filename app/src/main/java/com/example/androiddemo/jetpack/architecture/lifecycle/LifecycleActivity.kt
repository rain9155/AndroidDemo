package com.example.androiddemo.jetpack.architecture.lifecycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.OnLifecycleEvent
import com.example.androiddemo.R

/**
 * 参考：
 * https://developer.android.com/topic/libraries/architecture/lifecycle#lc
 * http://liuwangshu.cn/application/jetpack/2-lifecycle-use.html
 *
 * 生命周期感应组件Lifecycle：
 * 1、在实际开发中有很多组件都会依赖于Activity或Fragment的生命周期方法进行回调，这样会导致Activity或Fragment的生命周期方法中参杂大量代码，使得它们难以维护
 * 2、有些组件在启动之前需要在Activity或Fragment的某个生命周期方法(如onStart)中做耗时操作，这样会导致在Activity或Fragment结束(如onStop)之前该组件还没有启动
 * 所以Lifecycle就是为了解决以上两个问题：
 * 当使用Lifecycle后，组件不必再把回调或操作放入Activity或Fragment的相应生命周期方法中，组件只需要观察Lifecycle，
 * 当生命周期改变时，Lifecycle会感知到，并调用组件相应的回调方法，这样组件就可以响应生命周期的变化从而做出相应的操作，
 * 所以使用Lifecycle可以把组件依赖于生命周期的操作移进组件本身，使得这些操作不再与Activity或Fragment耦合
 *
 * 组成：
 * Event：生命周期事件
 * State：Lifecycle的状态
 * LifecycleObserver：观察者
 * LifecycleOwner：Lifecycle持有者，里面有一个getLifecycle方法返回Lifecycle实例
 * Lifecycle：生命周期感应组件
 * LifecycleRegistry：android中Lifecycle的默认实现类
 *
 * Lifecycle使用步骤：
 * 1、定义继承自LifecycleObserver的观察者；
 * 2、在观察者中使用@OnLifecycleEvent注解标记需要调用的方法
 * 3、把观察者注册进Lifecycle中去.
 */
class LifecycleActivity : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName

    /** 1、定义继承LifecycleObserver的观察者 */
    interface IPresenter : LifecycleObserver{

        /** 2、在观察者中使用@OnLifecycleEvent注解标记需要调用的方法 */

        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        fun onAny()

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun onCreate()

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun onStart()

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume()

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPause()

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onStop()

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy()
    }

    internal inner class MyPresenter : IPresenter{
        override fun onAny() {
            Log.d(TAG, "Lifecycle invoke onAny()")
        }

        override fun onCreate() {
            Log.d(TAG, "Lifecycle invoke onCreate()")
        }

        override fun onStart() {
            Log.d(TAG, "Lifecycle invoke onStart()")
        }

        override fun onResume() {
            Log.d(TAG, "Lifecycle invoke onResume()")
        }

        override fun onPause() {
            Log.d(TAG, "Lifecycle invoke onPause()")
        }

        override fun onStop() {
            Log.d(TAG, "Lifecycle invoke onStop()")
        }

        override fun onDestroy() {
            Log.d(TAG, "Lifecycle invoke onDestroy()")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lifecycle)

        /** 2、把观察者注册进Lifecycle中去 */
        lifecycle.addObserver(MyPresenter())
    }
}
