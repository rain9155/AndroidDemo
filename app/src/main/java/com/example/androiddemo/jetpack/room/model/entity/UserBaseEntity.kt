package com.example.androiddemo.jetpack.room.model.entity

/**
 * 用户的基本信息实体类（只包含用户的姓名、年龄）
 */
data class UserBaseEntity(
    var name: String,
    var age: Int
)