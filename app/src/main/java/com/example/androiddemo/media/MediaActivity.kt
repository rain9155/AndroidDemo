package com.example.androiddemo.media

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.databinding.ActivityMediaBinding
import com.example.androiddemo.media.audio.AudioActivity
import com.example.androiddemo.media.video.VideoActivity

class MediaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cpToAudio.setOnClickListener {
            startActivity(Intent(this, AudioActivity::class.java))
        }

        binding.cpToVideo.setOnClickListener {
            startActivity(Intent(this, VideoActivity::class.java))
        }
    }
}