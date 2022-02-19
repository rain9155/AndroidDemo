package com.example.androiddemo.jetpack.mvvm.model.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * ApiService实例创建
 * @author chenjianyu
 * @date 2020/6/1
 */
object ServiceCreator {
    private const val BASE_URI = "https://gank.io/api/v2/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URI)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(classs: Class<T>): T = retrofit.create(classs)
}