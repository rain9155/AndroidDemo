package com.example.androiddemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.androiddemo.animation.AnimActivity
import com.example.androiddemo.drawable.DrawableActivity
import com.example.androiddemo.jetpack.JetpackActivity
import com.example.androiddemo.view.listview.ListViewActivity
import com.example.androiddemo.material.ToolbarActivity
import com.example.androiddemo.view.recyclerview.RecyclerViewActivity
import com.example.androiddemo.view.scroller.ScrollerActivity
import com.example.androiddemo.view.surfaceview.SurfaceViewActivity
import com.example.androiddemo.view.viewdraghelper.DragActivity
import com.example.androiddemo.customview.CustomViewActivity
import com.example.androiddemo.view.ViewActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * demo编写组织规则：
 * 1、类按功能、类型划分到对应的package；
 * 2、类、资源命名要有含义，见名知意；
 * 3、入口activity要有对应的label.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cp_to_custom_view.setOnClickListener {
            startActivity(Intent(this, CustomViewActivity::class.java))
        }

        cp_to_anim.setOnClickListener {
            startActivity(Intent(this, AnimActivity::class.java))
        }

        cp_to_drawable.setOnClickListener{
            startActivity(Intent(this, DrawableActivity::class.java))
        }

        cp_to_material.setOnClickListener {
            startActivity(Intent(this, ToolbarActivity::class.java))
        }

        cp_to_jetpack.setOnClickListener {
            startActivity(Intent(this, JetpackActivity::class.java))
        }

        cp_to_view.setOnClickListener {
            startActivity(Intent(this, ViewActivity::class.java))
        }
    }
}
