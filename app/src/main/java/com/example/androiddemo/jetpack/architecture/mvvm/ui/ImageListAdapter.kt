package com.example.androiddemo.jetpack.architecture.mvvm.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androiddemo.R
import kotlinx.android.synthetic.main.item_img.view.*

/**
 * 图片列表的Adapter
 * @author chenjianyu
 * @date 2020/6/1
 */
class ImageListAdapter : RecyclerView.Adapter<ImageListAdapter.ImageListViewHolder>(){

    var datas: List<String>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageListViewHolder {
        return ImageListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_img, null))
    }

    override fun onBindViewHolder(holder: ImageListViewHolder, position: Int) {
        datas?.let {
            holder.bind(it[position])
        }
    }

    class ImageListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(imageUri: String){
            Glide.with(itemView)
                .asDrawable()
                .load(imageUri)
                .error(R.drawable.girl)
                .into(itemView.iv_img)
        }

    }

}