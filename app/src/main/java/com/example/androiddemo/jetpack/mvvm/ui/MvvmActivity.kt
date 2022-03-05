package com.example.androiddemo.jetpack.mvvm.ui

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
import com.example.androiddemo.jetpack.mvvm.bean.LoadState
import com.example.androiddemo.jetpack.mvvm.model.DataRepository
import com.example.androiddemo.jetpack.mvvm.viewModel.MvvmViewModel
import com.example.androiddemo.jetpack.mvvm.viewModel.MvvmViewModelFactory

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
        binding.recyclerView.run {
            binding.recyclerView.layoutManager = LinearLayoutManager(this@MvvmActivity)
            imageListAdapter = ImageListAdapter()
            binding.recyclerView.adapter = imageListAdapter
        }

        //观察ViewModel中的数据更新
        viewModel.loadState.observe(this, Observer {
            when(it){
                is LoadState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.INVISIBLE
                    binding.fab.isEnabled = false
                }
                is LoadState.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.fab.isEnabled = true
                    Snackbar.make(binding.progressBar, "loading success", Snackbar.LENGTH_LONG)
                        .setAction("confirm", null)
                        .show()
                }
                is LoadState.Fail -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.fab.isEnabled = true
                    Snackbar.make(binding.progressBar, "loading fail, msg = ${it.msg}", Snackbar.LENGTH_LONG)
                        .setAction("confirm", null)
                        .show()
                }
            }
        })
        viewModel.imageList.observe(this) {
            imageListAdapter.datas = it
        }

        viewModel.getImages()
    }
}



