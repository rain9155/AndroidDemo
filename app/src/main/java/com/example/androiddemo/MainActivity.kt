package com.example.androiddemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.listviewtest.ListActivity
import com.example.androiddemo.scrolltest.ScrollActivity
import com.example.androiddemo.viewdraghelpertest.DragActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_to_scroll.setOnClickListener {
            startActivity(Intent(this, ScrollActivity::class.java))
        }

        btn_to_drag.setOnClickListener {
            startActivity(Intent(this, DragActivity::class.java))
        }

        btn_to_list.setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
        }

    }
}
