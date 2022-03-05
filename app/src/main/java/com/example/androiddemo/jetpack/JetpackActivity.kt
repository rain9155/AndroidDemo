package com.example.androiddemo.jetpack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.R
import com.example.androiddemo.databinding.ActivityJetpackBinding
import com.example.androiddemo.jetpack.databinding.BindingActivity
import com.example.androiddemo.jetpack.lifecycle.LifecycleActivity
import com.example.androiddemo.jetpack.livedata.LiveDataActivity
import com.example.androiddemo.jetpack.mvvm.ui.MvvmActivity
import com.example.androiddemo.jetpack.navigation.NavigationActivity
import com.example.androiddemo.jetpack.room.RoomActivity
import com.example.androiddemo.jetpack.startup.AppStartupActivity
import com.example.androiddemo.jetpack.viewmodel.ViewModelActivity

/**
 * Android Jetpack库中的Architecture Component学习
 */
class JetpackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityJetpackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cpToLifecycle.setOnClickListener {
            startActivity(Intent(this, LifecycleActivity::class.java))
        }

        binding.cpToLivedata.setOnClickListener {
            startActivity(Intent(this, LiveDataActivity::class.java))
        }

        binding.cpToViewmodel.setOnClickListener {
            startActivity(Intent(this, ViewModelActivity::class.java))
        }

        binding.cpToRoom.setOnClickListener {
            startActivity(Intent(this, RoomActivity::class.java))
        }

        binding.cpToBinding.setOnClickListener {
            startActivity(Intent(this, BindingActivity::class.java))
        }

        binding.cpToNavigation.setOnClickListener {
            startActivity(Intent(this, NavigationActivity::class.java))
        }

        binding.cpToMvvm.setOnClickListener {
            startActivity(Intent(this, MvvmActivity::class.java))
        }

        binding.cpToStartup.setOnClickListener {
            startActivity(Intent(this, AppStartupActivity::class.java))
        }
    }
}
