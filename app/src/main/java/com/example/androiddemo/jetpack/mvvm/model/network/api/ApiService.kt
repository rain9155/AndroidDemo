package com.example.androiddemo.jetpack.mvvm.model.network.api

import com.example.androiddemo.jetpack.mvvm.bean.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 网络接口
 * @author chenjianyu
 * @date 2020/6/1
 */
interface ApiService {

    @GET("data/category/Girl/type/Girl/page/{page}/count/{count}")
    suspend fun getImages(
        @Path("page") page: Int = 1,
        @Path("count") count: Int = 10
    ): Response

}