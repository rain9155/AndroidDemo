package com.example.androiddemo.media.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import android.util.Log

/**
 * 使用AudioTrack播放PCM格式的音频：
 * android提供了MediaPlayer、SoundPool和AudioTrack进行音频播放，MediaPlayer和SoundPool都是更上层一点的Api，MediaPlayer适合在后台长时间地播放本地音频
 * 文件或者在线流式资源，SoundPool则适合播放比较短的音频片段比如游戏音、按键声、铃声等，它可以同时播放多个音频，而AudioTrack则更接近底层，它播放的是解码后的
 * PCM数据，提供了非常强大的控制能力，支持低延迟播放，适合流媒体播放等场景，MediaPlayer底层对于音频的播放也是使用了AudioTrack与AudioFlinger服务进行交互
 *
 * 主要步骤：
 * 1、配置参数，初始化AudioTrack内部的音频缓冲区
 * 2、调用AudioTrack的play方法开始播放音频
 * 3、根据不同模式调用AudioTrack的write方法把音频数据写入AudioTrack的缓冲区
 * 4、播放结束后，调用AudioTrack的stop方法停止采集，并调用AudioTrack的release方法释放资源
 *
 * 参考文档：
 * - [如何播放一帧音频](https://blog.51cto.com/ticktick/1750593)
 * - [使用 AudioTrack 播放PCM音频](https://www.cnblogs.com/renhui/p/7463287.html)
 */
class AudioTracker(
    /**
     * 当前应用使用的音频管理策略，当系统有多个进程需要播放音频时，这个管理策略会决定最终的展现效果，该参数的可选值定义在AudioManager类中，主要包括：
     * STREAM_VOCIE_CALL(电话声音)、STREAM_SYSTEM(系统声音)、STREAM_RING(铃声)、STREAM_MUSCI(音乐声)、STREAM_ALARM(警告声)、STREAM_NOTIFICATION(通知声)
     */
    val streamType: Int = AudioManager.STREAM_MUSIC,
    /**
     * 采样率，常用的有44100Hz、22050Hz、16000Hz、 11025Hz，其中44100Hz能够保证在所有设备上使用
     */
    val sampleRateInHz: Int = 44100,
    /**
     * 声道数，常用的有CHANNEL_XX_MONO(单声道)、CHANNEL_XX_STEREO(双声道)，其中CHANNEL_XX_MONO能够保证在所有设备上使用
     */
    val outChannelConfig: Int = AudioFormat.CHANNEL_OUT_MONO,
    /**
     * 数据位宽，常用的有ENCODING_PCM_16BIT(16位)、ENCODING_PCM_8BIT(8位), 其中ENCODING_PCM_16BIT能够保证在所有设备上使用
     */
    val encoding: Int = AudioFormat.ENCODING_PCM_16BIT,
    /**
     * AudioTrack的播放模式， AudioTrack有两种播放模式：
     * - MODE_STATIC：static模式，播放前把音频数据一次性write到AudioTrack的内部缓冲区，这种模式适用于像铃声这种内存占用量较小、延时要求较高的文件，它有一个缺点，就是
     *              一次write的数据不能太多，否则系统无法分配足够的内存来存储全部数据；
     * - MODE_STREAM：stream模式，播放时把音频数据不断地write到AudioTrack的内部缓冲区，这和平时通过write方法调用往文件中写数据类似，所以这在一定程度上会使引入播放延时.
     */
    val playMode: Int = AudioTrack.MODE_STREAM,
    /**
     * AudioTrack能接受的最小的buffer大小, 含义和[AudioTracker.minReadBufferSizeInBytes]类似，当AudioTrack在MODE_STATIC模式下时它的buffer大小要大于要播放的音频文件大小
     */
    val minWriteBufferSizeInBytes: Int = AudioTrack.getMinBufferSize(sampleRateInHz, outChannelConfig, encoding)
) {

    companion object {
        private val TAG = AudioTracker::class.java.simpleName
    }

    private var audioTrack: AudioTrack? = null

    /**
     * 是否正在播放音频
     */
    fun isPlaying(): Boolean {
        return audioTrack?.playState == AudioTrack.PLAYSTATE_PLAYING
    }

    /**
     * 准备播放音频:
     * STATIC模式下初始化AudioTrack的缓冲区时要初始化为将要播放的音频文件的大小,
     * STREAM模式下初始化AudioTrack的缓冲区时初始化为[minWriteBufferSizeInBytes]
     */
    fun startPlay(): Boolean {
        if(isPlaying()) {
            return false
        }
        /* 1、配置参数，初始化AudioTrack内部的音频缓冲区 */
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
                minWriteBufferSizeInBytes,
                playMode,
                AudioManager.AUDIO_SESSION_ID_GENERATE
            )
        }else{
            audioTrack = AudioTrack(
                streamType,
                sampleRateInHz,
                outChannelConfig,
                encoding,
                minWriteBufferSizeInBytes,
                playMode
            )
        }
        if(audioTrack?.state == AudioTrack.STATE_UNINITIALIZED) {
            Log.e(TAG, "startPlay fail: audioTrack init fail")
            return false
        }
        Log.d(TAG, "startPlay success: writeBufferSizeInBytes = $minWriteBufferSizeInBytes bytes, playMode = $playMode")
        return true
    }

    /**
     * 播放音频，往AudioTrack写入音频数据：
     * STATIC模式下先调用write方法后再调用play方法，
     * STREAM模式下先调用play方法后再调用write方法
     */
    fun play(datas: ByteArray, offset: Int, size: Int): Boolean {
        if(audioTrack?.state == AudioTrack.STATE_UNINITIALIZED) {
            return false
        }
        /* 2、调用AudioTrack的play方法开始播放音频 */
        /* 3、根据不同模式调用AudioTrack的write方法把音频数据写入AudioTrack的缓冲区 */
        if(playMode == AudioTrack.MODE_STATIC) {//STATIC模式下先write后play，一次性把音频数据写入到AudioTrack
            val write = audioTrack?.write(datas, offset, size) ?: AudioTrack.ERROR
            if(write >= 0) {
                audioTrack?.play()
            }else {
                Log.e(TAG, "play: write audioData fail, errorCode = $write")
                return false
            }
        }else {//STREAM模式下先play后write，不断地把音频数据写入到AudioTrack
            if(!isPlaying()) {
                audioTrack?.play()
            }
            val write = audioTrack?.write(datas, offset, size) ?: AudioTrack.ERROR
            if(write < 0) {
                Log.e(TAG, "play: write audioData fail, errorCode = $write")
                return false
            }
        }
        return true
    }

    /**
     * 停止播放音频
     */
    fun stopPlay(): Boolean {
        if(!isPlaying()) {
            return false
        }
        /* 4、播放结束后，调用AudioTrack的stop方法停止采集，并调用AudioTrack的release方法释放资源 */
        audioTrack?.stop()
        audioTrack?.release()
        Log.d(TAG, "stopPlay")
        return true
    }
}