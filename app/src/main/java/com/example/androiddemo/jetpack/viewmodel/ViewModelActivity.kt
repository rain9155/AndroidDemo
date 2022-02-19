package com.example.androiddemo.jetpack.viewmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.*
import com.example.androiddemo.R

/**
 * 参考：https://developer.android.com/topic/libraries/architecture/viewmodel#sharing
 *
 * 界面数据存储器ViewModel：
 * ViewModel可以在Activity/Fragment配置更改时保存自身，等Activity/Fragment重建时恢复自身，从而达到保存恢复数据的目的，
 * 避免了重复获取数据的资源浪费，同时ViewModel更有效地把视图数据处理逻辑与视图控制逻辑分离，它具有以下三个特点：
 * 1、ViewModel的生命周期是从Activity/Fragment创建(created)到它们真正销毁(finished)时;
 * 2、当所有Activity/Fragment销毁时，ViewModel的onCleared()方法会回调，以便它回收资源(例如停止所有异步回调)，避免内存泄露；
 * 3、ViewModel相比onSaveInstanceState()方法支持存储大量的数据，且这些数据不需要支持序列化；
 *
 * 组成：
 * ViewModelStoreOwner: ViewModelStore持有者，里面有一个getViewModelStore方法返回ViewModelStore实例
 * ViewModelStore：ViewModel保存者，里面通过Map来保存ViewModel实例与类名的映射
 * ViewModel：数据存储者，保存着界面数据，负责界面数据的加载逻辑
 * ViewModelProvider：ViewModel提供者，通过它的get方法返回ViewModel实例
 *
 * 基本使用：
 * 1、扩展ViewModel类，在里面定义LiveData；
 * 2、通过ViewModelProvider的get方法获取自定义的ViewModel实例
 * 3、观察ViewModel类中LiveData，当数据变化时，更新UI
 *
 * 在Fragment之间共享数据：
 * 多个Fragment可以通过共享其Activity范围内的ViewModel实现通信
 *
 * 注意：
 * 1、ViewModel绝不能引用视图、Lifecycle或可能存储对Activity上下文的引用的任何类；
 * 2、如果ViewModel需要Application上下文，可以扩展 AndroidViewModel类并设置用于接收Application的构造函数;
 * 3、当系统进程由于low memory被杀死重建后，此时ViewModel保存的数据会丢失，如果想要在系统杀死进程后数据还得以保存，可以使用OnSaveInstanceState方法或使用SaveStateHandler for ViewModel
 *   (参考：https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate、https://www.codenong.com/js82d49ea06029/)
 */
class ViewModelActivity : AppCompatActivity() {

    companion object{
        private val TAG = ViewModelActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model)

        /** 2、通过ViewModelProvider的get方法获取自定义的ViewModel实例 */
        //当Activity重新创建时，通过ViewModelProvider获取的ViewModel实例和上一个Activity实例获取的相同
        val viewModel = ViewModelProvider(this).get(CustomViewModel::class.java)

        /** 3、观察ViewModel类中LiveData，当数据变化时，更新UI */
        viewModel.getUsers().observe(this, Observer {
            Log.d(TAG, "onCreate(): user data update, users = ${it}")
        })
    }

    /** 1、扩展ViewModel类，在里面定义LiveData； */
    class CustomViewModel : ViewModel(){
        private val users: MutableLiveData<List<User>> by lazy {
            MutableLiveData<List<User>>().also {
                loadUsers()
            }
        }

        private fun loadUsers(){
            //执行异步调用，异步结果返回后设置给LiveData
            Handler(Looper.getMainLooper()).postDelayed({
                users.value = listOf(
                    User("rain"), User("chenjianyu"), User("hello")
                )
            }, 1000)
        }

        fun getUsers(): LiveData<List<User>>{
            return users
        }

        override fun onCleared() {
            super.onCleared()
            //todo: clear the subscription
        }

        data class User(var name: String)
    }
}

/**
 * 在Fragment之间共享的ViewModel
 */
class SharedViewModel : ViewModel(){

    private val process: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    fun getProcess(): LiveData<Int>{
        return process
    }

    fun setProcess(pro: Int){
        process.value = pro
    }

}