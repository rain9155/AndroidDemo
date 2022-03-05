package com.example.androiddemo.jetpack.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddemo.R
import com.example.androiddemo.databinding.ActivityRoomBinding
import com.example.androiddemo.jetpack.room.model.AppDatabase
import com.example.androiddemo.jetpack.room.model.entity.UserEntity

/**
 * 参考：https://developer.android.com/training/data-storage/room
 *
 * 可观察的数据库Room：
 * Room是在SQLite基础上的进一步封装的数据库，它解决了SQLite使用复杂、无法及时更新数据等问题
 *
 * 组成：
 * Database：表示数据库，使用@Database注解，继承自RoomDatabase，在注解注释中关联多张表，并在内部包含多个获取DAO的抽象方法
 * Entity：表示数据库中的表，使用@Entity注解，必须是一个数据类
 * DAO：用于访问数据库，包含用于访问数据库的方法，使用@Dao注解
 *
 * 基本使用：
 * 1、使用@Entity定义表；
 * 2、为每张表使用@Dao定义DAO；
 * 3、使用@Database定义数据库，并关联表和包含获取DAO的抽象方法
 *
 * 注意：DAO中所有访问Room的方法只能在子线程中调用，除非你在Room builder中调用了allowMainThreadQueries方法或者你调用返回LiveData对象的DAO方法
 */
class RoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userList = ArrayList<UserEntity>()
        val datasAdapter = DatasAdapter(userList)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = datasAdapter

        //初始化数据库
        val appDatabase = AppDatabase.getInstance(this.applicationContext)

        //通过DAO访问数据库，观察数据，当数据库中的数据更新时，onChange方法回调
        appDatabase.getUserDao().loadUsers().observe(this, Observer {
            //更新UI
            if(it.isNotEmpty()){
                userList.addAll(it)
                datasAdapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    class DatasAdapter(var datas: List<UserEntity>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview, parent, false)
            return MyViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return datas.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if(holder is MyViewHolder){
                val user = datas[position]
                holder.textView.text = "姓名：${user.name},  年龄：${user.age}"
            }
        }

        private class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

            val textView: TextView = itemView.findViewById(R.id.text_view)

        }
    }
}
