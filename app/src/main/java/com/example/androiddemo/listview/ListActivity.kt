package com.example.androiddemo.listview

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewConfiguration
import android.widget.AbsListView
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.androiddemo.R
import kotlinx.android.synthetic.main.activity_list.*
import kotlin.collections.ArrayList

/**
 * ListView使用示例：
 */
class ListActivity : AppCompatActivity() {

    companion object{
        private val TAG = ListActivity::class.java.simpleName
    }

    private var adapter: ListViewAdapter? = null
    private var lastVisibleItemPosition = 0
    private var touchSlop = 0
    private var direction = 0
    private var lastY = 0f
    private var currentY = 0f
    private var isShow = true
    private var o: ObjectAnimator? = null

    private val datas: List<Int>
        get() {
            var listDate = ArrayList<Int>()
            var i = 0
            while (i < Math.random() * 100 % 51 + 20) {
                listDate.add(i + 1)
                i++
            }
            return listDate
        }

    //当ListView下滑时，显示Toolbar, 当ListView上滑时，隐藏Toolbar
    private val listener = OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> lastY = event.y
            MotionEvent.ACTION_MOVE -> {
                currentY = event.y
                if (currentY - lastY > touchSlop) {
                    direction = 0 //下滑
                } else if (lastY - currentY > touchSlop) {
                    direction = 1 //上滑
                }
                if (direction == 0) {
                    if (isShow) {
                        toolbarAnim(0)
                        isShow = false
                    }
                } else if (direction == 1) {
                    if (!isShow) {
                        toolbarAnim(1)
                        isShow = true
                    }
                }
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setSupportActionBar(toolbar as Toolbar)
        touchSlop = ViewConfiguration.get(this).scaledTouchSlop //获取系统认为的最低滑动距离，即超过这个距离，系统就定义为滑动状态

        adapter = ListViewAdapter(this@ListActivity, datas)
        listView.adapter = adapter
        listView.setOnTouchListener(listener)

        listView.lastVisiblePosition //获取可视视图区域内最后一个ItemID
        listView.firstVisiblePosition //获取可视区域内第一个ItemID
        listView.setSelection(5) //设置Listview需要显示在第几项，默认是第一项
        //当ListView为空时显示的一张图片
        listView.emptyView = findViewById(R.id.iv_empty)
        val header = View(this)
        header.layoutParams = AbsListView.LayoutParams(
            AbsListView.LayoutParams.MATCH_PARENT,
            //获取ActionBar高度
            resources.getDimension(R.dimen.abc_action_bar_default_height_material).toInt()
        )
        //添加Header
        listView.addHeaderView(header)
        //item点击监听
        listView!!.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            adapter!!.setCurrentItem(position)
            toast(this@ListActivity, "You Click $position")
        }

        //滑动监听 - OnScrollListener（AbsListView内）
        listView.setOnScrollListener(object : AbsListView.OnScrollListener{

            /**
             * 滚动一直调用
             * @param firstVisibleItem 当前能看见的第一个ItemID（从零开始）
             * @param visibleItemCount 当前能看见的item总数
             * @param totalItemCount   整个ListView的Item总数
             */
            override fun onScroll(
                view: AbsListView?,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                Log.d(TAG, "onScroll: 滚动一直调用")
                //判断滚动到了最后一行
                if(firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0){

                }
                //判断上滑
                if(firstVisibleItem > lastVisibleItemPosition){

                }
                //判断下滑
                if (firstVisibleItem < lastVisibleItemPosition){

                }
                lastVisibleItemPosition = firstVisibleItem
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                when (scrollState){
                    //滑动停止时
                    AbsListView.OnScrollListener.SCROLL_STATE_IDLE -> {
                        Log.d(TAG, "SCROLL_STATE_IDLE: 滑动停止时 ")
                    }
                    // 正在滑动
                    AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL ->{
                        Log.d(TAG, "SCROLL_STATE_TOUCH_SCROLL: 正在滑动 ")
                    }
                    //手指用力滑动，离开屏幕，ListView由于惯性继续滑动
                    AbsListView.OnScrollListener.SCROLL_STATE_FLING -> {
                        Log.d(TAG, "SCROLL_STATE_FLING: 手指用力滑动，离开屏幕，ListView由于惯性继续滑动 ")
                    }
                }
            }

        })
    }


    /**
     * 隐藏动画
     * @param i
     */
    private fun toolbarAnim(i: Int) {
        if (o != null && o!!.isRunning) {
            o!!.cancel()
        }
        if (i == 0) {
            o = ObjectAnimator.ofFloat(toolbar, "translationY", -toolbar.height.toFloat(), 0f)
        } else {
            o = ObjectAnimator.ofFloat(toolbar, "translationY", 0f, -toolbar.height.toFloat())
        }
        o!!.duration = 100
        o!!.start()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        finish()
    }

    private var toast: Toast? = null

    /**
     * 封装Toast,避免重复提示
     */
    fun toast(context: Context?, text: String?) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(text)
        }
        toast!!.show()
    }
}