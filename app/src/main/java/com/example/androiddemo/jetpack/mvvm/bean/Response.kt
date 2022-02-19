package com.example.androiddemo.jetpack.mvvm.bean

data class Response(
    val data: List<Data>,
    val page: Int,
    val page_count: Int,
    val status: Int,
    val total_counts: Int
)