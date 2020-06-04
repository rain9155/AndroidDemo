package com.example.androiddemo.jetpack.architecture.mvvm.bean

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("_id")
    val id: String,
    val author: String,
    val category: String,
    val createdAt: String,
    val desc: String,
    val images: List<String>,
    val likeCounts: Int,
    val publishedAt: String,
    val stars: Int,
    val title: String,
    val type: String,
    val url: String,
    val views: Int
)