package com.example.androiddemo.jetpack.startup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.startup.AppInitializer
import com.example.androiddemo.R

/**
 * 参考：https://developer.android.com/topic/libraries/app-startup
 *
 * 组件初始化器：
 * AppStartup通过共享一个ContentProvider(ContentProvider的创建比Application还早)在app启动时进行组件的初始化逻辑
 *
 * 组成：
 *  1、InitializationProvider：ContentProvider，AppStartup在里面进行组件的初始化
 *  2、Initializer<T>: 组件初始化器，抽象组件初始化的接口
 *  3、AppInitializer：启动、管理组件初始化的类
 *
 *  基本使用：
 *  1、实现Initializer接口，定义组件初始化逻辑，设置初始化依赖，返回初始化后的组件实例 T
 *  2、在Manifest文件中定义InitializationProvider，通过<meta-data>声明Initializer实现类
 *  3、使用AppInitializer启动初始化流程(app启动时会自动使用AppInitializer启动初始化流程)
 *
 *  注意：
 *  1、如果组件需要在app启动时初始化，组件的Initializer必须要在provider的<meta-data>声明，同时依赖的Initializer无需二次声明
 *  2、如果组件不需要在app启动时初始化，需要自己手动启动初始化流程，则需要在provider中添加tools:node="remove"标记，合并manifest时移除所有组件的Initializer
 *  3、如果某个组件不需要在app启动时初始化，需要自己手动启动初始化流程，则需要在meta-data>中添加tools:node="remove"标记，合并manifest时这个组件的Initializer
 */
class AppStartupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_startup)

        //手动启动初始化流程
        AppInitializer.getInstance(this).initializeComponent(InitializerC::class.java)
    }
}