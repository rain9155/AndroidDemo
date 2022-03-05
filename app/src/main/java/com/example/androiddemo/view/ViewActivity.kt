package com.example.androiddemo.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.R
import com.example.androiddemo.databinding.ActivityViewBinding
import com.example.androiddemo.view.listview.ListViewActivity
import com.example.androiddemo.view.recyclerview.RecyclerViewActivity
import com.example.androiddemo.view.scroller.ScrollerActivity
import com.example.androiddemo.view.surfaceview.SurfaceViewActivity
import com.example.androiddemo.view.viewdraghelper.DragActivity

class ViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cpToScroller.setOnClickListener {
            startActivity(Intent(this, ScrollerActivity::class.java))
        }

        binding.cpToListview.setOnClickListener {
            startActivity(Intent(this, ListViewActivity::class.java))
        }

        binding.cpToRecyclerview.setOnClickListener {
            startActivity(Intent(this, RecyclerViewActivity::class.java))
        }

        binding.cpToSurfaceview.setOnClickListener {
            startActivity(Intent(this, SurfaceViewActivity::class.java))
        }

        binding.cpToDraghelper.setOnClickListener {
            startActivity(Intent(this, DragActivity::class.java))
        }
    }
}