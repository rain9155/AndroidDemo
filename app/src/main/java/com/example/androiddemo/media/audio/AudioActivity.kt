package com.example.androiddemo.media.audio

import android.Manifest
import android.annotation.SuppressLint
import android.media.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddemo.R
import com.example.androiddemo.databinding.ActivityAudioBinding
import com.example.permission.IResultCallback
import com.example.permission.PermissionHelper
import java.io.*

/**
 * Android音频采集(AudioRecord)与音频播放(AudioTrack):
 * 1、android提供了MediaRecorder和AudioRecord进行音频采集，MediaRecorder是一个更上层一点的Api，可以直接把采集到的音频进行编码压缩(mp3、amr等)保存为文件，
 * 而AudioRecord是一个更底层一点的Api，可以直接采集到未编码压缩的PCM格式的音频原始数据，MediaRecorder底层也是使用了AudioRecord与AudioFlinger服务进行交互;
 * 2、android提供了MediaPlayer、SoundPool和AudioTrack进行音频播放，MediaPlayer和SoundPool都是更上层一点的Api，MediaPlayer适合在后台长时间地播放本地音频
 * 文件或者在线流式资源，SoundPool则适合播放比较短的音频片段比如游戏音、按键声、铃声等，它可以同时播放多个音频，而AudioTrack则更接近底层，它播放的是解码后的
 * PCM数据，提供了非常强大的控制能力，支持低延迟播放，适合流媒体播放等场景，MediaPlayer底层对于音频的播放也是使用了AudioTrack与AudioFlinger服务进行交互。
 *
 * 一、使用AudioRecord采集音频PCM：
 * 1、配置参数，初始化AudioRecord内部的音频缓冲区
 * 2、调用AudioRecord的startRecording方法开始采集音频
 * 3、在子线程中调用AudioRecord的read方法不断地从AudioRecord的缓冲区将音频数据读取出来
 * 4、采集结束后，调用AudioRecord的stop方法停止采集，并调用AudioRecord的release方法释放资源
 *
 * 二、使用AudioTrack播放PCM格式的音频：
 * 1、配置参数，初始化AudioTrack内部的音频缓冲区
 * 2、调用AudioTrack的play方法开始播放音频
 * 3、根据不同模式调用AudioTrack的write方法把音频数据写入AudioTrack的缓冲区
 * 4、播放结束后，调用AudioTrack的stop方法停止采集，并调用AudioTrack的release方法释放资源
 *
 * 参考文档：
 *  - [如何采集一帧音频](https://blog.51cto.com/ticktick/1749719)
 *  - [如何播放一帧音频](https://blog.51cto.com/ticktick/1750593)
 *  - [使用AudioRecord采集音频PCM并保存到文件](https://www.cnblogs.com/renhui/p/7457321.html)
 *  - [使用 AudioTrack 播放PCM音频](https://www.cnblogs.com/renhui/p/7463287.html)
 *  - [PCM音频采样数据处理](https://blog.csdn.net/leixiaohua1020/article/details/50534316)
 */
class AudioActivity : AppCompatActivity() {

    companion object {
        private val TAG = AudioActivity::class.java.simpleName
    }

    /**
     * 音频采集的输入源，常用的有：DEFAULT(默认)、VOICE_RECOGNITION(用于语音识别，等同于DEFAULT)、MIC(手机麦克风输入)、VOICE_COMMUNICATION(用于VoIP应用)
     */
    private val audioSource = MediaRecorder.AudioSource.MIC

    /**
     * 采样率，常用的有44100Hz、22050Hz、16000Hz、 11025Hz，其中44100Hz能够保证在所有设备上使用
     */
    private val sampleRateInHz = 44100

    /**
     * 声道数，常用的有CHANNEL_XX_MONO(单声道)、CHANNEL_XX_STEREO(双声道)，其中CHANNEL_XX_MONO能够保证在所有设备上使用
     */
    private val inChannelConfig = AudioFormat.CHANNEL_IN_MONO //for AudioRecord
    private val outChannelConfig = AudioFormat.CHANNEL_OUT_MONO //for AudioTrack

    /**
     * 数据位宽，常用的有ENCODING_PCM_16BIT(16位)、ENCODING_PCM_8BIT(8位), 其中ENCODING_PCM_16BIT能够保证在所有设备上使用
     */
    private val encoding = AudioFormat.ENCODING_PCM_16BIT

    /**
     * AudioRecord能接受的最小的buffer大小，buffer大小不能低于一帧音频的大小，一帧音频大小计算公式为：采样率 x 位宽 x 采样时间 x 通道数，
     * 采样率、位宽、通道数外部可以指定，采样时间由硬件厂商决定一般为2.5ms~120ms之间，而getMinBufferSize方法就是根据音频帧大小计算公式返
     * 回至少一帧的音频大小，当AudioRecord采集的音频数据填满buffer大小时，外部必须及时掉用read方法读取掉数据，不然会出现“over-running”
     */
    private val minReadBufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, inChannelConfig, encoding)

    /**
     * 当前应用使用的音频管理策略，当系统有多个进程需要播放音频时，这个管理策略会决定最终的展现效果，该参数的可选值定义在AudioManager类中，主要包括：
     * STREAM_VOCIE_CALL(电话声音)、STREAM_SYSTEM(系统声音)、STREAM_RING(铃声)、STREAM_MUSCI(音乐声)、STREAM_ALARM(警告声)、STREAM_NOTIFICATION(通知声)
     */
    private val streamType = AudioManager.STREAM_MUSIC

    /**
     * AudioTrack的播放模式， AudioTrack有两种播放模式：
     * - MODE_STATIC：static模式，播放前把音频数据一次性write到AudioTrack的内部缓冲区，这种模式适用于像铃声这种内存占用量较小、延时要求较高的文件，它有一个缺点，就是
     *              一次write的数据不能太多，否则系统无法分配足够的内存来存储全部数据；
     * - MODE_STREAM：stream模式，播放时把音频数据不断地write到AudioTrack的内部缓冲区，这和平时通过write方法调用往文件中写数据类似，所以这在一定程度上会使引入播放延时.
     */
    private val playMode = AudioTrack.MODE_STREAM

    /**
     * AudioTrack在MODE_STREAM模式下能接受的最小的buffer大小, 含义和[minReadBufferSizeInBytes]类似, 当AudioTrack在MODE_STATIC模式下时它的buffer大小要大于要播放的音频文件大小
     */
    private val minWriteBufferSizeInBytes = AudioTrack.getMinBufferSize(sampleRateInHz, outChannelConfig, encoding)

    private var audioRecord: AudioRecord? = null
    private var audioTrack: AudioTrack? = null

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
            if(!isRecording()) {
                if(startRecord()) {
                    button.text = getString(R.string.stop_record)
                }
            }else {
                if(stopRecord()) {
                    button.text = getString(R.string.start_record)
                }
            }
        }

        //使用AudioTack播放音频
        binding.btnPlay.setOnClickListener { v ->
            val button = v as Button
            if (!isPlaying()) {
                if(playRecord()) {
                    button.text = getString(R.string.stop_play)
                }
            }else {
                if(stopPlay()) {
                    button.text = getString(R.string.play_record)
                }
            }
        }
    }

    /**
     * 开始录音
     */
    @SuppressLint("MissingPermission")
    private fun startRecord(): Boolean {
        if(isRecording()) {
            return false
        }
        /* 1、配置参数，初始化AudioRecord内部的音频缓冲区 */
        audioRecord = AudioRecord(
            audioSource,
            sampleRateInHz,
            inChannelConfig,
            encoding,
            minReadBufferSizeInBytes
        )
        if(audioRecord?.state == AudioRecord.STATE_UNINITIALIZED) {
            Log.e(TAG, "startRecord fail: audioRecord init fail")
            return false
        }
        /* 2、调用startRecording方法开始采集音频 */
        audioRecord?.startRecording()
        /* 3、在子线程中调用AudioRecord的read方法不断地从AudioRecord的缓冲区将音频数据读取出来 */
        RecordingThread().start()
        Log.d(TAG, "startRecord success: minReadBufferSizeInBytes = $minReadBufferSizeInBytes bytes")
        return true
    }

    /**
     * 停止录音
     */
    private fun stopRecord(): Boolean {
        if(!isRecording()) {
            return false
        }
        /* 4、录音结束后，关闭数据流，释放资源，停止录音 */
        audioRecord?.stop()
        audioRecord?.release()
        Log.d(TAG, "stopRecord")
        return true
    }

    /**
     * 是否正在录音
     */
    private fun isRecording(): Boolean {
        return audioRecord?.recordingState == AudioRecord.RECORDSTATE_RECORDING
    }

    /**
     * 播放录音：STATIC模式下初始化AudioTrack的缓冲区时要初始化为将要播放的音频文件的大小，然后调用write方法后再调用play方法，
     * STREAM模式下初始化AudioTrack的缓冲区时初始化为[minWriteBufferSizeInBytes], 然后调用play方法后再调用write方法
     */
    private fun playRecord(): Boolean {
        if(isPlaying()) {
            return false
        }
        /* 1、配置参数，初始化AudioTrack内部的音频缓冲区 */
        val writeBufferSizeInBytes = if(playMode == AudioTrack.MODE_STATIC)
            WavFile.getSizeInBytes(this)
        else
            minWriteBufferSizeInBytes
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            audioTrack = AudioTrack(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build(),
                AudioFormat.Builder()
                    .setSampleRate(sampleRateInHz)
                    .setEncoding(encoding)
                    .setChannelMask(outChannelConfig)
                    .build(),
                writeBufferSizeInBytes,
                playMode,
                AudioManager.AUDIO_SESSION_ID_GENERATE
            )
        }else{
            audioTrack = AudioTrack(
                streamType,
                sampleRateInHz,
                outChannelConfig,
                encoding,
                writeBufferSizeInBytes,
                playMode
            )
        }
        if(audioTrack?.state == AudioTrack.STATE_UNINITIALIZED) {
            Log.e(TAG, "playRecord fail: audioTrack init fail")
            return false
        }
        if(playMode == AudioTrack.MODE_STREAM) {
            /* 2、调用AudioTrack的play方法开始播放音频 */
            audioTrack?.play()
        }
        /* 3、根据不同模式调用AudioTrack的write方法把音频数据写入AudioTrack的缓冲区 */
        PlayingThread().start()
        Log.d(TAG, "playRecord success: writeBufferSizeInBytes = $writeBufferSizeInBytes bytes, playMode = $playMode")
        return true
    }

    /**
     * 停止播放音频
     */
    private fun stopPlay(): Boolean {
        if(!isPlaying()) {
            return false
        }
        /* 4、播放结束后，调用AudioTrack的stop方法停止采集，并调用AudioTrack的release方法释放资源 */
        audioTrack?.stop()
        audioTrack?.release()
        Log.d(TAG, "stopPlay")
        return true
    }

    /**
     * 是否正在播放录音
     */
    private fun isPlaying(): Boolean {
        return audioTrack?.playState == AudioTrack.PLAYSTATE_PLAYING
    }

    /**
     * 从AudioRecord读取音频数据线程
     */
    private inner class RecordingThread : Thread() {

        override fun run() {
            super.run()
            val wavWriter = WavFile.createWriter(this@AudioActivity, sampleRateInHz, inChannelConfig, encoding)
            try {
                val audioData = ByteArray(minReadBufferSizeInBytes)
                while (isRecording()) {
                    val read = audioRecord?.read(audioData, 0, audioData.size) ?: AudioRecord.ERROR
                    if(read >= 0) {
                        //如果读取音频数据没有出现错误，就将数据写入到wav文件
                        wavWriter.writeData(audioData, 0, read)
                    }else {
                        Log.e(TAG, "RecordingThread: read audioData fail, errorCode = $read")
                        break
                    }
                }
            }catch (e: IOException) {
                e.printStackTrace()
                Log.e(TAG, "RecordingThread: read audioData fail, errorMsg = ${e.message}")
            }finally {
                wavWriter.close()
            }
        }
    }

    /**
     * 往AudioTrack写入音频数据线程
     */
    private inner class PlayingThread() : Thread() {

        override fun run() {
            super.run()
            val wavReader = WavFile.createReader(this@AudioActivity)
            try {
                if(playMode == AudioTrack.MODE_STATIC) {//一次性把音频数据写入到AudioTrack
                    val audioData = wavReader.readDatas()
                    val write = audioTrack?.write(audioData, 0, audioData.size) ?: AudioTrack.ERROR
                    if(write >= 0) {//
                        audioTrack?.play()
                    }else {
                        Log.e(TAG, "PlayingThread: write audioData fail, errorCode = $write")
                    }
                }else { //不断地读取音频数据写入到AudioTrack
                    val audioData = ByteArray(minWriteBufferSizeInBytes)
                    while (isPlaying()) {
                        val read = wavReader.readData(audioData, 0, audioData.size)
                        if(read >= 0) {
                            val write = audioTrack?.write(audioData, 0, read) ?: AudioTrack.ERROR
                            if(write < 0) {
                                Log.e(TAG, "PlayingThread: write audioData fail, errorCode = $write")
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e(TAG, "PlayingThread: write audioData fail, errorMsg = ${e.message}")
            }
        }
    }

}