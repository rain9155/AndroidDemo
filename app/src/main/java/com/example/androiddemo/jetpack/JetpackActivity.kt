package com.example.androiddemo.jetpack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.R
import com.example.androiddemo.jetpack.architecture.ArchActivity
import kotlinx.android.synthetic.main.activity_jetpack.*
import kotlinx.android.synthetic.main.activity_main.*

class JetpackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jetpack)

        cp_to_arch.setOnClickListener {
            startActivity(Intent(this, ArchActivity::class.java))
        }
    }
}
