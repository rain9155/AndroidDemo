package com.example.androiddemo.media.audio

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.R
import com.example.androiddemo.databinding.ActivityAudioBinding
import com.example.permission.IResultCallback
import com.example.permission.PermissionHelper
import java.io.IOException

/**
 * Android音频采集(AudioRecord)与音频播放(AudioTrack):
 * 使用AudioRecord采集音频PCM数据并保存为wav文件，然后使用AudioTrack播放wav文件中的PCM数据
 */
class AudioActivity : AppCompatActivity() {

    companion object {
        private val TAG = AudioActivity::class.java.simpleName
    }

    private val audioRecorder = AudioRecorder()

    private val audioTracker = AudioTracker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAudioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PermissionHelper.with(this).permissions(Manifest.permission.RECORD_AUDIO).request(object : IResultCallback{
            override fun onResult(
                isAllGrant: Boolean,
                grantedPermissions: List<String>,
                rejectedPermissions: List<String>
            ) {
                if(!isAllGrant) {
                    Log.e(TAG, "no RECORD_AUDIO permission")
                }
            }
        })

        //使用AudioRecord采集音频PCM并保存到文件
        binding.btnRecord.setOnClickListener { v ->
            val button = v as Button
            if(!audioRecorder.isRecording()) {
                //把pcm数据保存到wav文件中
                audioRecorder.recordListener = object : AudioRecorder.RecordListener {
                    private var wavWriter: WavFile.Writer? = null

                    override fun onRecordStart() {
                        wavWriter = WavFile.createWriter(
                            this@AudioActivity,
                            audioRecorder.sampleRateInHz,
                            audioRecorder.inChannelConfig,
                            audioRecorder.encoding
                        )
                    }

                    override fun onRecording(datas: ByteArray, offset: Int, size: Int) {
                        wavWriter?.writeData(datas, offset, size)
                    }

                    override fun onRecordEnd(code: Int, msg: String?) {
                        wavWriter?.close()
                    }
                }
                if(audioRecorder.startRecord()) {
                    button.text = getString(R.string.stop_record)
                }
            }else {
                if(audioRecorder.stopRecord()) {
                    button.text = getString(R.string.start_record)
                }
            }
        }

        //使用AudioTack播放音频
        binding.btnPlay.setOnClickListener { v ->
            val button = v as Button
            if (!audioTracker.isPlaying()) {
                if(audioTracker.startPlay()) {
                    button.text = getString(R.string.stop_play)
                    //在子线程中读取wav文件播放
                    Thread {
                        val wavReader = WavFile.createReader(this@AudioActivity)
                        try {
                            val audioData = ByteArray(audioTracker.minWriteBufferSizeInBytes)
                            while (wavReader.available()) {
                                val read = wavReader.readData(audioData, 0, audioData.size)
                                if(read > 0) {
                                    audioTracker.play(audioData, 0, read)
                                }else {
                                    break
                                }
                            }
                        }catch (e: IOException) {
                            e.printStackTrace()
                        }finally {
                            wavReader.close()
                        }
                    }.start()
                }
            }else {
                if(audioTracker.stopPlay()) {
                    button.text = getString(R.string.play_record)
                }
            }
        }
    }
}