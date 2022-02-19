package com.example.androiddemo.jetpack.mvvm.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androiddemo.jetpack.mvvm.model.DataRepository

/**
 * MvvmViewModel的Factory
 * @author chenjianyu
 * @date 2020/6/1
 */
class MvvmViewModelFactory(private val dataRepository: DataRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val constructor = modelClass.getConstructor(dataRepository.javaClass)
        return constructor.newInstance(dataRepository)
    }
}