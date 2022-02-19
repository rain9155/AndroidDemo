package com.example.androiddemo.jetpack.room.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.androiddemo.jetpack.room.model.entity.UserBaseEntity
import com.example.androiddemo.jetpack.room.model.entity.UserEntity

/**
 * User表的Data Access Object
 */
@Dao
interface UserDao {

    /**
     * 查询User表, 返回所有
     */
    @Query("select * from User")
    fun loadAllUsers(): List<UserEntity>

    /**
     * 查询User表, 可观察的查询
     */
    @Query("select * from User")
    fun loadUsers(): LiveData<List<UserEntity>>

    /**
     * 查询User表, 投影查询
     */
    @Query("select User.name, User.age from User")
    fun getUsers(): List<UserBaseEntity>

    /**
     * 查询User表，条件查询
     */
    @Query("select * from User where name = :name")
    fun getUser(name: String): UserEntity

    /**
     * 插入参数列表给出的User到User表中，返回插入项的rowId
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: List<UserEntity>)

    /**
     * 根据主键匹配在User表中删除参数列表给出的User，返回删除的行数
     */
    @Delete
    fun deleteUser(vararg user: UserEntity)

    /**
     * 根据主键匹配在User表中更新参数列表给出的User，返回更新的行数
     */
    @Update
    fun updateUser(vararg user: UserEntity)

}