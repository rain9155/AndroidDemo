package com.example.androiddemo.jetpack.mvvm.model

import com.example.androiddemo.jetpack.mvvm.model.network.NetworkDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 数据仓库
 * @author chenjianyu
 * @date 2020/6/1
 */
class DataRepository private constructor(private val networkDao: NetworkDao){

    companion object{
        val instance by lazy {
            DataRepository(NetworkDao.instance)
        }
    }

    suspend fun getImages(page: Int, count: Int): List<String> = withContext(Dispatchers.IO){
        val response = networkDao.getImages(page, count)
        val datas = response.data
        val result = ArrayList<String>(datas.size)
        datas.forEach {
            if(!it.images.isNullOrEmpty()){
                result.add(it.images[0])
            }
        }
        return@withContext result
    }

}