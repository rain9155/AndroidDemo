package com.example.androiddemo.jetpack.architecture.mvvm.ui

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androiddemo.R
import com.example.androiddemo.databinding.ActivityMvvmBinding
import com.example.androiddemo.jetpack.architecture.mvvm.bean.LoadState
import com.example.androiddemo.jetpack.architecture.mvvm.model.DataRepository
import com.example.androiddemo.jetpack.architecture.mvvm.viewModel.MvvmViewModel
import com.example.androiddemo.jetpack.architecture.mvvm.viewModel.MvvmViewModelFactory

import kotlinx.android.synthetic.main.activity_mvvm.*

class MvvmActivity : AppCompatActivity() {

    private lateinit var viewModel: MvvmViewModel
    private lateinit var imageListAdapter: ImageListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //检索ViewModel
        viewModel = ViewModelProvider(this, MvvmViewModelFactory(DataRepository.instance))[MvvmViewModel::class.java]

        //数据绑定
        val binding = DataBindingUtil.setContentView<ActivityMvvmBinding>(this, R.layout.activity_mvvm)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //视图初始化
        recycler_view.run {
            recycler_view.layoutManager = LinearLayoutManager(this@MvvmActivity)
            imageListAdapter = ImageListAdapter()
            recycler_view.adapter = imageListAdapter
        }

        //观察ViewModel中的数据更新
        viewModel.loadState.observe(this, Observer {
            when(it){
                is LoadState.Loading -> {
                    progress_bar.visibility = View.VISIBLE
                    recycler_view.visibility = View.INVISIBLE
                    fab.isEnabled = false
                }
                is LoadState.Success -> {
                    progress_bar.visibility = View.INVISIBLE
                    recycler_view.visibility = View.VISIBLE
                    fab.isEnabled = true
                    Snackbar.make(progress_bar, "loading success", Snackbar.LENGTH_LONG)
                        .setAction("confirm", null)
                        .show()
                }
                is LoadState.Fail -> {
                    progress_bar.visibility = View.INVISIBLE
                    fab.isEnabled = true
                    Snackbar.make(progress_bar, "loading fail, msg = ${it.msg}", Snackbar.LENGTH_LONG)
                        .setAction("confirm", null)
                        .show()
                }
            }
        })
        viewModel.imageList.observe(this, Observer {
            imageListAdapter.datas = it
        })

        viewModel.getImages()
    }
}



