package com.example.androiddemo.jetpack.architecture.startup

import android.util.Log

/**
 * 多个组件
 * @author chenjianyu
 * @date 2021/9/3
 */

const val TAG = "Component"

class ComponentA private constructor(){

    companion object {
        fun init(): ComponentA{
            Log.d(TAG, "ComponentA init")
            return ComponentA()
        }
    }

}

class ComponentB private constructor(){

    companion object {
        fun init(): ComponentB{
            Log.d(TAG, "ComponentB init")
            return ComponentB()
        }
    }

}

class ComponentC private constructor(){

    companion object {
        fun init(): ComponentC{
            Log.d(TAG, "ComponentC init")
            return ComponentC()
        }
    }

}