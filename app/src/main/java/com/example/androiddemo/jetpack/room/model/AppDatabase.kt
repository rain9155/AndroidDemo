package com.example.androiddemo.jetpack.room.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.androiddemo.jetpack.room.model.dao.UserDao
import com.example.androiddemo.jetpack.room.model.entity.UserEntity

/**
 * 数据库
 */
@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getUserDao(): UserDao

    /**
     * 单例模式
     */
    companion object{
        const val DATABASE_NAME = "MyDatabase"

        var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            return instance?: synchronized(AppDatabase::class.java){
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
                        Thread {
                            Thread.sleep(3000)
                            //初始化数据
                            val userList = DataGenerator.loadUsers(context)
                            getInstance(context)
                                .getUserDao()
                                .insertUser(userList)
                        }.start()
                    }
                })
                .build()
        }
    }


}