package com.example.androiddemo.media.audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import java.io.IOException

/**
 * 使用AudioRecord采集音频PCM：
 * android提供了MediaRecorder和AudioRecord进行音频采集，MediaRecorder是一个更上层一点的Api，可以直接把采集到的音频进行编码压缩(mp3、amr等)保存为文件，
 * 而AudioRecord是一个更底层一点的Api，可以直接采集到未编码压缩的PCM格式的音频原始数据，MediaRecorder底层也是使用了AudioRecord与AudioFlinger服务进行交互;
 *
 * 主要步骤：
 * 1、配置参数，初始化AudioRecord内部的音频缓冲区
 * 2、调用AudioRecord的startRecording方法开始采集音频
 * 3、在子线程中调用AudioRecord的read方法不断地从AudioRecord的缓冲区将音频数据读取出来
 * 4、采集结束后，调用AudioRecord的stop方法停止采集，并调用AudioRecord的release方法释放资源
 *
 * 参考文档：
 *  - [如何采集一帧音频](https://blog.51cto.com/ticktick/1749719)
 *  - [使用AudioRecord采集音频PCM并保存到文件](https://www.cnblogs.com/renhui/p/7457321.html)
 *  - [PCM音频采样数据处理](https://blog.csdn.net/leixiaohua1020/article/details/50534316)
 */
class AudioRecorder(
    /**
     * 音频采集的输入源，常用的有：DEFAULT(默认)、VOICE_RECOGNITION(用于语音识别，等同于DEFAULT)、MIC(手机麦克风输入)、VOICE_COMMUNICATION(用于VoIP应用)
     */
    val audioSource: Int = MediaRecorder.AudioSource.MIC,
    /**
     * 采样率，常用的有44100Hz、22050Hz、16000Hz、 11025Hz，其中44100Hz能够保证在所有设备上使用
     */
    val sampleRateInHz: Int = 44100,
    /**
     * 声道数，常用的有CHANNEL_XX_MONO(单声道)、CHANNEL_XX_STEREO(双声道)，其中CHANNEL_XX_MONO能够保证在所有设备上使用
     */
    val inChannelConfig: Int = AudioFormat.CHANNEL_IN_MONO,
    /**
     * 数据位宽，常用的有ENCODING_PCM_16BIT(16位)、ENCODING_PCM_8BIT(8位), 其中ENCODING_PCM_16BIT能够保证在所有设备上使用
     */
    val encoding: Int = AudioFormat.ENCODING_PCM_16BIT,
    /**
     * AudioRecord能接受的最小的buffer大小，buffer大小不能低于一帧音频的大小，一帧音频大小计算公式为：采样率 x 位宽 x 采样时间 x 通道数，
     * 采样率、位宽、通道数外部可以指定，采样时间由硬件厂商决定一般为2.5ms~120ms之间，而getMinBufferSize方法就是根据音频帧大小计算公式返
     * 回至少一帧的音频大小，当AudioRecord采集的音频数据填满buffer大小时，外部必须及时掉用read方法读取掉数据，不然会出现“over-running”
     */
    val minReadBufferSizeInBytes: Int = AudioRecord.getMinBufferSize(sampleRateInHz, inChannelConfig, encoding)
) {

    companion object {
        private val TAG = AudioRecorder::class.java.simpleName
    }

    private var audioRecord: AudioRecord? = null

    var recordListener: RecordListener? = null

    /**
     * 是否正在录音
     */
    fun isRecording(): Boolean {
        return audioRecord?.recordingState == AudioRecord.RECORDSTATE_RECORDING
    }

    /**
     * 开始录音
     */
    @SuppressLint("MissingPermission")
    fun startRecord(): Boolean {
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
    fun stopRecord(): Boolean {
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
     * 从AudioRecord读取音频数据线程
     */
    private inner class RecordingThread : Thread() {

        override fun run() {
            super.run()
            try {
                recordListener?.onRecordStart()
                val audioData = ByteArray(minReadBufferSizeInBytes)
                while (isRecording()) {
                    val read = audioRecord?.read(audioData, 0, audioData.size) ?: AudioRecord.ERROR
                    if(read >= 0) {
                        //如果读取音频数据没有出现错误，就将数据回调出去
                        recordListener?.onRecording(audioData, 0, read)
                    }else {
                        Log.e(TAG, "RecordingThread: read audioData fail, errorCode = $read")
                        recordListener?.onRecordEnd(read)
                        return
                    }
                }
                recordListener?.onRecordEnd()
            }catch (e: IOException) {
                e.printStackTrace()
                Log.e(TAG, "RecordingThread: read audioData fail, errorMsg = ${e.message}")
                recordListener?.onRecordEnd(-1, e.message)
            }
        }
    }

    /**
     * 录音状态回调，在子线程回调
     */
    interface RecordListener {

        fun onRecordStart()

        fun onRecording(datas: ByteArray, offset: Int, size: Int)

        fun onRecordEnd(code: Int = 0, msg: String? = null)
    }
}