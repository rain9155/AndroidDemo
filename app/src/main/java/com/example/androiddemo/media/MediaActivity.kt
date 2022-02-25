package com.example.androiddemo.media

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.R
import com.example.androiddemo.media.audio.AudioActivity
import kotlinx.android.synthetic.main.activity_media.*

class MediaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        cp_to_audio.setOnClickListener {
            startActivity(Intent(this, AudioActivity::class.java))
        }
    }
}