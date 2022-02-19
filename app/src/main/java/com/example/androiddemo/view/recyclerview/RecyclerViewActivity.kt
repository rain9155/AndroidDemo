package com.example.androiddemo.view.recyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.androiddemo.R
import com.example.androiddemo.view.recyclerview.adapter.RecyclerViewAdapter
import com.example.androiddemo.view.recyclerview.decoration.CustomItemDecoration
import com.example.androiddemo.view.recyclerview.layoutmanager.CustomLayoutManager2
import kotlinx.android.synthetic.main.activity_recyclerview.*
import java.util.*

/**
 * RecyclerView的基本使用：
 */
class RecyclerViewActivity : AppCompatActivity() {

    lateinit var datas: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)

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
        recycler_view.layoutManager = CustomLayoutManager2()

        /* 适配器 */
        recycler_view.adapter = RecyclerViewAdapter(datas)

        /* 为RecyclerView添加分割线，mRecyclerView.addItemDecoration()  */
        recycler_view.addItemDecoration(
           CustomItemDecoration(this)
        )
        /*
            为RecyclerView添加item动画，mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            注意：这里更新数据集不是用adapter.notifyDataSetChanged()， 而是notifyItemInserted(position)与notifyItemRemoved(position)
         */
        recycler_view.itemAnimator = DefaultItemAnimator()

    }

    private fun initData() {
        datas = ArrayList()
        var i = 'A'.toInt()
        while (i < 'z'.toInt()) {
            datas.add("" + i.toChar())
            i++
        }
    }
}
