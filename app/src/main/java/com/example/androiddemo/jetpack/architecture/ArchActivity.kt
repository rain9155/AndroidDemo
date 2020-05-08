package com.example.androiddemo.jetpack.architecture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.R
import com.example.androiddemo.jetpack.architecture.lifecycle.LifecycleActivity
import kotlinx.android.synthetic.main.activity_arc.*

class ArchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arc)

        cp_to_lifecycle.setOnClickListener {
            startActivity(Intent(this, LifecycleActivity::class.java))
        }

    }
}
