package com.example.androiddemo.view.recyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.androiddemo.databinding.ActivityRecyclerviewBinding
import com.example.androiddemo.view.recyclerview.adapter.RecyclerViewAdapter
import com.example.androiddemo.view.recyclerview.decoration.CustomItemDecoration
import com.example.androiddemo.view.recyclerview.layoutmanager.CustomLayoutManager2
import java.util.*

/**
 * RecyclerView的基本使用：
 */
class RecyclerViewActivity : AppCompatActivity() {

    lateinit var datas: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRecyclerviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* 初始化数据源 */
        initData()

        /* 网格布局 */
        // GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);

        /* 瀑布流 */
        // StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        //StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.HORIZONTAL);

        /* 线性布局 */
//        recycler_view.layoutManager =  LinearLayoutManager(this).also {
//            it.orientation = RecyclerView.VERTICAL
//        }

        /* 自定义layoutManager */
        binding.recyclerView.layoutManager = CustomLayoutManager2()

        /* 适配器 */
        binding.recyclerView.adapter = RecyclerViewAdapter(datas)

        /* 为RecyclerView添加分割线，mRecyclerView.addItemDecoration()  */
        binding.recyclerView.addItemDecoration(
           CustomItemDecoration(this)
        )

        /*
            为RecyclerView添加item动画，mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            注意：这里更新数据集不是用adapter.notifyDataSetChanged()， 而是notifyItemInserted(position)与notifyItemRemoved(position)
         */
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
    }

    private fun initData() {
        datas = ArrayList()
        var i = 'A'.code
        while (i < 'z'.code) {
            datas.add("" + i.toChar())
            i++
        }
    }
}
