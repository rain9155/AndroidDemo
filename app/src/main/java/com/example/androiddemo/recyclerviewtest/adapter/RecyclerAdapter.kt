package com.example.androiddemo.recyclerviewtest.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddemo.R
import com.example.androiddemo.recyclerviewtest.adapter.RecyclerAdapter.MyViewHolder

/**
 * recyclerView的adapter
 * Create by 陈健宇 at 2018/8/21
 */
class RecyclerAdapter(private val mDatas: List<String>) : RecyclerView.Adapter<MyViewHolder>() {

    private val TAG = RecyclerAdapter::class.java.simpleName
    private var mNum = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        Log.d(TAG, "onCreateViewHolder: " + mNum++)
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recycler, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ")
        holder.textView.text = mDatas[position]
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.text_view)
    }

}