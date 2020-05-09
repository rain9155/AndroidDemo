package com.example.androiddemo.jetpack.architecture.livedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import com.example.androiddemo.R
import kotlinx.android.synthetic.main.activity_live_data.*

/**
 * 可观察的数据源LiveData：
 * LiveData它是具有生命周期感应能力的、可观察的数据存储器，它具有以下三个特点：
 * 1、只有当观察者处于活跃状态(STARTED、RESUMED), LiveData才会发送更新通知给它，而处于非活跃状态(PAUSED、STOPPED)的观察者则不会收到更新通知
 * 2、当观察者由非活跃状态转为活跃状态时，LiveData将发送最新的更新通知给它，如果没有数据更新，则观察者不会发生更新
 * 3、当观察者关联的生命周期组件被销毁时(DESTROYED), LiveDate会自动移除该观察者
 *
 * 组成：
 * LiveData：抽象类，不可修改数据
 * Observer：观察者，用来观察LiveData，里面只有一个onChange方法
 * MutableLiveData：LiveData的子类，可以修改数据
 * MediatorLiveData：LiveData的子类，可以合并多个数据源LiveData
 *
 * 一、LiveData的基本使用：
 * 1、定义LiveData对象，包裹住需要存储的数据的数据类型
 * 2、实现Observer接口的onChange方法，在onChange方法中更新UI
 * 3、使用LiveData的observe方法观察LiveData，并传入Observer对象
 * 4、调用LiveData的setValue/postValue方法更新数据
 *
 * 二、扩展LiveData：
 * 继承LiveData可以重写onActive和onInActive方法
 *
 * 三、转换LiveData：
 * 1、通过Transformations的map方法把LiveData中的值转换成另外一种数据类型
 * 2、通过Transformations的switchMap方法根据LiveData中的值把LiveData转换成不同的LiveData实例
 *
 * 四、合并、观察多个LiveData：
 * 通过MediatorLiveData的addSource方法添加多个LiveDate，从而监听多个LiveData，当某个LiveData改变时
 * MediatorLiveData就会监听到, 从而触发MediatorLiveData的观察者更新
 */
class LiveDataActivity : AppCompatActivity() {

    companion object{
        private val TAG = LiveDataActivity::class.java.simpleName
    }

    /** 1、定义LiveData对象 */
    private val stringLiveData: MutableLiveData<String> = MyMutableLiveData("初始值")

    /** 2、实现Observer接口的onChange方法 */
    private val stringObserver: Observer<String> = Observer {
        tv_string.text = it
    }

    private var control = false
    /**
     * 通过Transformations的map方法把LiveData中的值转换成另外一种数据类型
     */
    private val boolLiveData: LiveData<Boolean> = Transformations.map(stringLiveData){
        control = !control
        control
    }

    /**
     * 通过Transformations的switchMap方法根据LiveData中的值把LiveData转换成不同的LiveData实例
     */
    private val transformedLiveData: LiveData<Any> = Transformations.switchMap(boolLiveData){
        if(it){
            MutableLiveData<Any>(9155)
        }else{
            MutableLiveData<Any>(9155.0)
        }
    }

    /** 网络源 */
    private val netWorkLiveData: LiveData<String> = MutableLiveData("从网络返回的值")
    /** 本地源 */
    private val localLiveData: LiveData<String> = MutableLiveData("从本地取出的值")
    /** 用来监听多个数据源更新的MediatorLiveData */
    private val mergeLiveData: MediatorLiveData<String> = MediatorLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data)

        /** 3、使用observe方法观察LiveData，并传入Observer对象和LifecycleOwner对象 */
        stringLiveData.observe(this, stringObserver)

        //使用observeForever方法观察LiveData，只传入Observer对象，不传入LifecycleOwner对象
        //通过observeForever方法注册的Observer不与具体的生命周期组件绑定，所以只要LiveData发生变化，Observer就会收到更新通知
        //可以通过removeObserver方法移除这个Observer对象
        stringLiveData.observeForever {
            Log.d(TAG, "onCreate(): stringLiveData change, date = $it")
        }

        btn_change.setOnClickListener {
            /** 4、调用LiveData的setValue/postValue方法更新数据 */
            //setValue方法在main thread调用，postValue方法在work thread调用
            stringLiveData.value = "改变后的值"
        }

        //stringLiveData的下游，当stringLiveData改变，boolLiveData的Observer更新
        boolLiveData.observe(this, Observer {
            Log.d(TAG, "onCreate(): boolLiveData change, data = $it")
        })

        //boolLiveData的下游，当boolLiveData改变，transformedLiveData的Observer更新
        transformedLiveData.observe(this, Observer {
            Log.d(TAG, "onCreate(): transformedLiveData change, data = $it")
        })

        /**
         * 通过MediatorLiveData的addSource方法合并多个LiveDate
         */

        //添加网络源到MediatorLiveData中
        mergeLiveData.addSource(netWorkLiveData){
            //调用MediatorLiveData的set/postValue方法
            mergeLiveData.value = it
        }

        //添加本地源到MediatorLiveData中
        mergeLiveData.addSource(localLiveData){
            //调用MediatorLiveData的set/postValue方法
            mergeLiveData.value = it
        }

        //观察MediatorLiveData，当某个源更新时，Observer的onChange方法就会回调
        mergeLiveData.observe(this, Observer {
            Log.d(TAG, "onCreate(): mergeLiveData change, data = $it")
        })
    }

    /**
     * 扩展LiveData：
     * 当LiveData由不活跃状态转为活跃状态时，onActive方法回调，当LiveData中没有观察者处于活跃状态时，onInActive方法回调
     */
    private class MyMutableLiveData<T>(value: T) : MutableLiveData<T>(value) {

        override fun onActive() {
            Log.d(TAG, "MyMutableLiveData, onActive(): liveData active")
            //todo: 开始连接服务
        }

        override fun onInactive() {
            Log.d(TAG, "MyMutableLiveData, onInActive(): liveData inActive")
            //todo：断开服务
        }
    }


}
