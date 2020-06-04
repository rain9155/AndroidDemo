package com.example.androiddemo.jetpack.architecture.mvvm.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddemo.jetpack.architecture.mvvm.bean.LoadState
import com.example.androiddemo.jetpack.architecture.mvvm.model.DataRepository
import kotlinx.coroutines.launch

/**
 * MvvmActivity的ViewModel
 * @author chenjianyu
 * @date 2020/6/1
 */
class MvvmViewModel(private val dataRepository: DataRepository) : ViewModel(){

    companion object{
        private const val PAGE_COUNT = 10
    }

    private var curPage = 1

    //存放图片的列表
    var imageList = MutableLiveData<List<String>>()
    //加载状态
    var loadState = MutableLiveData<LoadState>()


    fun getImages(){
        launch({

            loadState.value = LoadState.Loading()
            imageList.value = dataRepository.getImages(curPage, PAGE_COUNT)

        }, onError = {

            it.printStackTrace()
            loadState.value = LoadState.Fail(it.message ?: "")

        }, onComplete = {

            loadState.value = LoadState.Success()

        })
    }

    fun refreshImages(){
        curPage++
        getImages()
    }
}