package com.example.androiddemo.jetpack.architecture.room.model

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.androiddemo.jetpack.architecture.room.model.dao.UserDao
import com.example.androiddemo.jetpack.architecture.room.model.entity.UserEntity
import java.util.*
import java.util.concurrent.Executors

/**
 * 数据库
 */
@Database(entities = arrayOf(UserEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getUserDao(): UserDao

    /**
     * 单例模式
     */
    companion object{
        val DATABASE_NAME = "MyDatabase"

        var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            return instance?: synchronized(AppDatabase.javaClass){
                instance?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase{
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback(){

                    //当数据库第一次创建时调用
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Thread(Runnable {
                            Thread.sleep(3000)
                            //初始化数据
                            val userList = DataGenerator.loadUsers(context)
                            AppDatabase.getInstance(context)
                                .getUserDao()
                                .insertUser(userList)
                        }).start()
                    }

                })
                .build()
        }
    }


}