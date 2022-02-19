package com.example.androiddemo.jetpack.mvvm.bean

/**
 * 加载状态
 * @author chenjianyu
 * @date 2020/6/1
 */
sealed class LoadState(val msg: String){
    class Loading(msg: String = "") : LoadState(msg)
    class Success(msg: String = "") : LoadState(msg)
    class Fail(msg: String) : LoadState(msg)
}