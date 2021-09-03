package com.example.androiddemo.jetpack.architecture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.R
import com.example.androiddemo.jetpack.architecture.databinding.BindingActivity
import com.example.androiddemo.jetpack.architecture.lifecycle.LifecycleActivity
import com.example.androiddemo.jetpack.architecture.livedata.LiveDataActivity
import com.example.androiddemo.jetpack.architecture.mvvm.ui.MvvmActivity
import com.example.androiddemo.jetpack.architecture.navigation.NavigationActivity
import com.example.androiddemo.jetpack.architecture.room.RoomActivity
import com.example.androiddemo.jetpack.architecture.startup.AppStartupActivity
import com.example.androiddemo.jetpack.architecture.viewmodel.ViewModelActivity
import kotlinx.android.synthetic.main.activity_arc.*

/**
 * Android Jetpack库中的Architecture Component学习
 */
class ArchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arc)

        cp_to_lifecycle.setOnClickListener {
            startActivity(Intent(this, LifecycleActivity::class.java))
        }

        cp_to_livedata.setOnClickListener {
            startActivity(Intent(this, LiveDataActivity::class.java))
        }

        cp_to_viewmodel.setOnClickListener {
            startActivity(Intent(this, ViewModelActivity::class.java))
        }

        cp_to_room.setOnClickListener {
            startActivity(Intent(this, RoomActivity::class.java))
        }

        cp_to_binding.setOnClickListener {
            startActivity(Intent(this, BindingActivity::class.java))
        }

        cp_to_navigation.setOnClickListener {
            startActivity(Intent(this, NavigationActivity::class.java))
        }

        cp_to_mvvm.setOnClickListener {
            startActivity(Intent(this, MvvmActivity::class.java))
        }

        cp_to_startup.setOnClickListener {
            startActivity(Intent(this, AppStartupActivity::class.java))
        }
    }
}
