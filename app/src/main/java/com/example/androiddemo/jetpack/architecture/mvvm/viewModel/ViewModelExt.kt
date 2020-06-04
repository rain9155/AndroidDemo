package com.example.androiddemo.jetpack.architecture.mvvm.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

fun ViewModel.launch(
    block: suspend () -> Unit,
    onError: (Throwable) -> Unit = {},
    onComplete: () -> Unit = {}
){
    val coroutineExceptionHandler = CoroutineExceptionHandler {_, throwable ->
        onError(throwable)
    }
    viewModelScope.launch(coroutineExceptionHandler){
        try {
            block()
        }finally {
            onComplete()
        }
    }
}