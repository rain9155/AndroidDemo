package com.example.androiddemo.jetpack.startup

import android.content.Context
import androidx.startup.Initializer

/**
 * 多个初始化器
 * @author chenjianyu
 * @date 2021/9/3
 */

class InitializerA : Initializer<ComponentA>{

    override fun create(context: Context): ComponentA {
        return ComponentA.init()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return emptyList<Class<out Initializer<*>>>().toMutableList()
    }

}

class InitializerB : Initializer<ComponentB>{

    override fun create(context: Context): ComponentB {
        return ComponentB.init()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return emptyList<Class<out Initializer<*>>>().toMutableList()
    }

}

/**
 * ComponentC依赖ComponentA、ComponentB的初始化
 */
class InitializerC : Initializer<ComponentC>{

    override fun create(context: Context): ComponentC {
        return ComponentC.init()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return listOf(InitializerA::class.java, InitializerB::class.java).toMutableList()
    }

}