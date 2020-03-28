package com.example.androiddemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.animationtest.AnimActivity
import com.example.androiddemo.drawable.DrawableActivity
import com.example.androiddemo.listview.ListActivity
import com.example.androiddemo.material.ToolbarActivity
import com.example.androiddemo.recyclerview.RecyclerActivity
import com.example.androiddemo.scroller.ScrollerActivity
import com.example.androiddemo.surfaceview.SurfaceViewActivity
import com.example.androiddemo.viewdraghelper.DragActivity
import com.example.androiddemo.view.ViewActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cp_to_scroll.setOnClickListener {
            startActivity(Intent(this, ScrollerActivity::class.java))
        }

        cp_to_drag.setOnClickListener {
            startActivity(Intent(this, DragActivity::class.java))
        }

        cp_to_list.setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
        }

        cp_to_drawable.setOnClickListener{
            startActivity(Intent(this, DrawableActivity::class.java))
        }

        cp_to_surface.setOnClickListener {
            startActivity(Intent(this, SurfaceViewActivity::class.java))
        }

        cp_to_view.setOnClickListener {
            startActivity(Intent(this, ViewActivity::class.java))
        }

        cp_to_anim.setOnClickListener {
            startActivity(Intent(this, AnimActivity::class.java))
        }

        cp_to_recycler.setOnClickListener {
            startActivity(Intent(this, RecyclerActivity::class.java))
        }

        cp_to_material.setOnClickListener {
            startActivity(Intent(this, ToolbarActivity::class.java))
        }

    }
}
