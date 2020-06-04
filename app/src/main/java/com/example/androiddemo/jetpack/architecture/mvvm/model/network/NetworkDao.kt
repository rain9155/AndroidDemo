package com.example.androiddemo.jetpack.architecture.mvvm.model.network

import com.example.androiddemo.jetpack.architecture.mvvm.model.network.api.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 网络访问层
 * @author chenjianyu
 * @date 2020/6/1
 */
class NetworkDao private constructor(){

    companion object{
        val instance by lazy {
            NetworkDao()
        }
    }

    private val apiService = ServiceCreator.create(ApiService::class.java)

    suspend fun getImages(page: Int, count: Int) = apiService.getImages(page, count)

}