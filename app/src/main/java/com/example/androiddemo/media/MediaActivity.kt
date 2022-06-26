package com.example.androiddemo.media

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.databinding.ActivityMediaBinding
import com.example.androiddemo.media.audio.AudioActivity
import com.example.androiddemo.media.video.VideoActivity

/**
 * android音视频学习
 *
 * 参考文档：
 * - [视音频编解码技术零基础学习方法](https://blog.csdn.net/leixiaohua1020/article/details/18893769)
 * - [Android音视频开发学习思路](https://www.cnblogs.com/renhui/p/7452572.html)
 * - [Android音视频开发入门指南](https://blog.51cto.com/ticktick/1956269)
 * - [Android supported media-formats](https://developer.android.com/guide/topics/media/media-formats?hl=zh-cn)
 */
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