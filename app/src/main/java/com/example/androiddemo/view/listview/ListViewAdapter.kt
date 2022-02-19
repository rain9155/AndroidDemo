package com.example.androiddemo.view.listview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.androiddemo.R

/**
 * ListView的Adapter
 * Created by ASUS on 2018/7/7.
 */
class ListViewAdapter(context: Context, private val datas: List<Int>) : BaseAdapter() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mCurrentItem = -1

    override fun getCount(): Int {
        return datas.size
    }

    override fun getItem(position: Int): Any {
        return datas[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * 重写此方法可以返回第position个item时何种类型
     * 从而在getView方法中根据不同类型返回不同convertView
     */
    override fun getItemViewType(position: Int): Int {
        return 0
    }

    /**
     * 重写此方法返回不同布局总数(默认1)，结合getItemViewType（）来用
     */
    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

        var convertView = convertView
        val viewHolder: ViewHolder?

        if (convertView == null) { //判断是否有缓存
            viewHolder = ViewHolder()
            //通过LayoutInflater实例化布局
            convertView = mInflater.inflate(R.layout.item_listview, null)
            viewHolder.imageView1 = convertView.findViewById<View>(R.id.image_1) as ImageView
            viewHolder.textView1 = convertView.findViewById<View>(R.id.text_view) as TextView
            convertView.tag = viewHolder
        } else { //通过Tag找到缓存的布局
            viewHolder = convertView.tag as ViewHolder
        }

        //把数据设置到布局中
        viewHolder.imageView1!!.setImageResource(R.mipmap.ic_launcher)
        viewHolder.textView1!!.text = datas[position].toString()
        return convertView
    }

    private inner class ViewHolder {
        var imageView1: ImageView? = null
        var textView1: TextView? = null
    }

    fun setCurrentItem(i: Int) {
        mCurrentItem = i
    }

}