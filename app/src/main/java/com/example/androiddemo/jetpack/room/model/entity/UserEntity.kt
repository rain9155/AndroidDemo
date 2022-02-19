package com.example.androiddemo.jetpack.room.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 用户实体类
 */
@Entity(tableName = "User")
data class UserEntity(
    var name: String = "",
    var age: Int = -1,
    var city: String = "",
    var hobby: String = ""
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    var id: Long = 0
}