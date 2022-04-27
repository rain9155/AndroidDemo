package com.example.androiddemo.media.video

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.androiddemo.R
import com.example.androiddemo.databinding.ActivityVideoBinding
import com.example.permission.IResultCallback
import com.example.permission.PermissionHelper

/**
 * Android视音频开发之视频采集（Camera），解编码（MediaCodec），音视频合成mp4（MediaMuxer ）
 *
 * 使用MediaCodecAPI解编码（完成音频 AAC 硬编、硬解，完成视频 H.264 的硬编、硬解）
 */
class VideoActivity : AppCompatActivity() {

    companion object{
        private val TAG = VideoActivity::class.java.simpleName
    }

    private val cameraManager = CameraCompat(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PermissionHelper.with(this)
            .permissions(Manifest.permission.CAMERA)
            .request(object : IResultCallback {
                override fun onResult(
                    isAllGrant: Boolean,
                    grantedPermissions: List<String>,
                    rejectedPermissions: List<String>
                ) {
                    if(!isAllGrant) {
                        Log.e(TAG, "no CAMERA permission")
                        finish()
                    }
                }
            })

        //使用camera预览
        binding.btnPreview.setOnClickListener {
            if(!cameraManager.isPreview()) {
                cameraManager.cameraListener = object : CameraCompat.CameraListener() {
                    override fun onCameraOpen() {
                        cameraManager.startPreview(binding.svPreview.holder)
                        binding.btnPreview.text = getString(R.string.stop_preview)
                    }

                }
                cameraManager.openCamera()
            }else {
                cameraManager.stopPreview()
                cameraManager.releaseCamera()
                binding.btnPreview.text = getString(R.string.start_preview)
            }
        }
    }

}