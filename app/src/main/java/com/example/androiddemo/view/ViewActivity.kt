package com.example.androiddemo.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.R
import com.example.androiddemo.view.listview.ListViewActivity
import com.example.androiddemo.view.recyclerview.RecyclerViewActivity
import com.example.androiddemo.view.scroller.ScrollerActivity
import com.example.androiddemo.view.surfaceview.SurfaceViewActivity
import com.example.androiddemo.view.viewdraghelper.DragActivity
import kotlinx.android.synthetic.main.activity_view.*

class ViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        cp_to_scroller.setOnClickListener {
            startActivity(Intent(this, ScrollerActivity::class.java))
        }

        cp_to_listview.setOnClickListener {
            startActivity(Intent(this, ListViewActivity::class.java))
        }

        cp_to_recyclerview.setOnClickListener {
            startActivity(Intent(this, RecyclerViewActivity::class.java))
        }

        cp_to_surfaceview.setOnClickListener {
            startActivity(Intent(this, SurfaceViewActivity::class.java))
        }

        cp_to_draghelper.setOnClickListener {
            startActivity(Intent(this, DragActivity::class.java))
        }
    }
}