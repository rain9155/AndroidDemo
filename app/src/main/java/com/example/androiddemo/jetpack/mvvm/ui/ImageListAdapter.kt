package com.example.androiddemo.jetpack.mvvm.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androiddemo.R
import com.example.androiddemo.databinding.ItemImgBinding

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
        return ImageListViewHolder(ItemImgBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ImageListViewHolder, position: Int) {
        datas?.let {
            holder.bind(it[position])
        }
    }

    class ImageListViewHolder(val binding: ItemImgBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(imageUri: String){
            Glide.with(binding.root)
                .asDrawable()
                .load(imageUri)
                .error(R.drawable.girl)
                .into(binding.ivImg)
        }

    }

}