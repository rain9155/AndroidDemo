package com.example.androiddemo

import android.content.Intent
import android.graphics.ColorMatrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.animationtest.AnimActivity
import com.example.androiddemo.animationtest.animation.AnimationActivity2
import com.example.androiddemo.drawabletest.DrawableActivity
import com.example.androiddemo.listviewtest.ListActivity
import com.example.androiddemo.scrollertest.ScrollerActivity
import com.example.androiddemo.surfaceviewtest.SurfaceViewActivity
import com.example.androiddemo.viewdraghelpertest.DragActivity
import com.example.androiddemo.viewtest.ViewActivity
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
    }
}
