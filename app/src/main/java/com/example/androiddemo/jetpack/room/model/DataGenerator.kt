package com.example.androiddemo.jetpack.room.model

import android.content.Context
import android.util.Xml
import com.example.androiddemo.jetpack.room.model.entity.UserEntity
import org.xmlpull.v1.XmlPullParser

/**
 * 数据生成器
 */
object DataGenerator {

    fun loadUsers(context: Context): List<UserEntity>{
        val assetManager = context.assets
        //打开文件
        val inputStream = assetManager.open("user_database.xml")
        //得到pull解析器
        val parser = Xml.newPullParser()
        //初始化解析器,第一个参数代表包含xml的数据
        parser.setInput(inputStream, Charsets.UTF_8.name())
        //得到当前事件的类型
        var type = parser.eventType
        var userList: MutableList<UserEntity> = mutableListOf()
        var user: UserEntity? = null
        while (type != XmlPullParser.END_DOCUMENT) {
             when(type){
                 XmlPullParser.START_TAG -> {
                     when {
                         "user" == parser.name -> {
                             user = UserEntity()
                         }
                         "name" == parser.name -> {
                             user?.name = parser.nextText()
                         }
                         "age" == parser.name -> {
                             user?.age = parser.nextText().toInt()
                         }
                         "city" == parser.name -> {
                             user?.city = parser.nextText()
                         }
                         "hobby" == (parser.name) -> {
                             user?.hobby = parser.nextText()
                         }
                     }
                 }
                 XmlPullParser.END_TAG -> {
                    if("user" == parser.name){
                        user?.let {
                            userList.add(it)
                            user = null
                        }

                    }
                 }
             }
            type = parser.next()
        }
        return userList
    }

}