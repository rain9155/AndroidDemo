package com.example.androiddemo.ipc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.androiddemo.databinding.ActivityIpcBinding
import com.example.androiddemo.ipc.aidl.ClientActivity

class IPCActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityIpcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cpToAidl.setOnClickListener {
            startActivity(Intent(this, ClientActivity::class.java))
        }
    }
}