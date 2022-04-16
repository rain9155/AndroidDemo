package com.example.androiddemo.media.video

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.R

/**
 * Android视音频开发之视频采集（Camera），解编码（MediaCodec），音视频合成mp4（MediaMuxer ）
 * 一、使用Camera进行视频采集
 * 步骤：
 * 1、打开摄像头，预览（使用SurfaceView）Camera数据
 * 2、配置数据回调的格式
 * 3、设置预览，SurfaceView或TextureView
 * 4、取到 NV21 的数据回调，通过setPreviewCallback方法监听预览的回调
 * 5、停止预览并释放资源
 * 二、使用MediaCodecAPI解编码（完成音频 AAC 硬编、硬解，完成视频 H.264 的硬编、硬解）
 *
 */
class VideoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
    }

}