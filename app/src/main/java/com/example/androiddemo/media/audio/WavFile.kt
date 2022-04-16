package com.example.androiddemo.media.audio

import android.content.Context
import android.media.AudioFormat
import android.os.Environment
import android.util.Log
import com.example.androiddemo.utils.*
import java.io.*

/**
 * WAV文件 = WAVE HEAD + PCM数据(data)，是微软开发的一种RIFF文件格式规范，与bitmap类似，
 * wav文件可以直接被MediaPlayer播放，解析后获取到的PCM数据可以直接被AudioTrack播放
 *
 * 参考文档：
 *  - [WAVE PCM soundfile format](http://soundfile.sapp.org/doc/WaveFormat/)
 *  - [如何存储和解析wav文件](https://blog.51cto.com/ticktick/1752947)
 */
class WavFile {

    companion object {
        private const val WAV_FILE_NAME = "test.wav"

        fun createReader(context: Context): Reader {
            return Reader(getFile(context).path)
        }

        fun createWriter(context: Context, sampleRateInHz: Int, channelConfig: Int, encoding: Int): Writer {
            return Writer(getFile(context).path, sampleRateInHz, getChannels(channelConfig), getBitsPerSample(encoding))
        }

        fun getSizeInBytes(context: Context): Int {
            var `is`: FileInputStream? = null
            return try {
                `is` = FileInputStream(getFile(context))
                `is`.available()
            }catch (e: IOException) {
                e.printStackTrace()
                0
            }finally {
                close(`is`)
            }
        }

        private fun getFile(context: Context, name: String = WAV_FILE_NAME): File {
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), name)
            if(!file.exists()) {
                try {
                    file.createNewFile()
                }catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return file
        }

        private fun getChannels(channelConfig: Int): Int {
            return if(channelConfig == AudioFormat.CHANNEL_IN_STEREO || channelConfig == AudioFormat.CHANNEL_OUT_STEREO) { 2 }else { 1 }
        }

        private fun getBitsPerSample(encoding: Int): Int {
            return if(encoding == AudioFormat.ENCODING_PCM_16BIT) 16 else 8
        }
    }

    /**
     * 从WAV文件读取PCM
     */
    class Reader(private val filePath: String) : Closeable{

        companion object {
            private val TAG = Reader::class.java.simpleName
        }

        private var wavHeader: WavHeader? = null
        private var dataIs: DataInputStream? = null

        init {
            readHeader()
        }

        /**
         * 打开wav文件流，读取wav文件的header
         */
        private fun readHeader() {
            try {
                dataIs = DataInputStream(FileInputStream(filePath))
                val header = WavHeader()
                val intBytes = ByteArray(4)
                val shortBytes = ByteArray(2)
                //RIFF chunk
                header.chunkId = "${dataIs?.readByte()?.toChar()}${dataIs?.readByte()?.toChar()}${dataIs?.readByte()?.toChar()}${dataIs?.readByte()?.toChar()}"
                dataIs?.read(intBytes)
                header.chunkSize = byteArrayToInt(intBytes)
                header.format = "${dataIs?.readByte()?.toChar()}${dataIs?.readByte()?.toChar()}${dataIs?.readByte()?.toChar()}${dataIs?.readByte()?.toChar()}"
                //fmt sub-chunk
                header.subChunk1Id = "${dataIs?.readByte()?.toChar()}${dataIs?.readByte()?.toChar()}${dataIs?.readByte()?.toChar()}${dataIs?.readByte()?.toChar()}"
                dataIs?.read(intBytes)
                header.subChunk1Size = byteArrayToInt(intBytes)
                dataIs?.read(shortBytes)
                header.audioFormat = byteArrayToShort(shortBytes)
                dataIs?.read(shortBytes)
                header.numChannel = byteArrayToShort(shortBytes)
                dataIs?.read(intBytes)
                header.sampleRate = byteArrayToInt(intBytes)
                dataIs?.read(intBytes)
                header.byteRate = byteArrayToInt(intBytes)
                dataIs?.read(shortBytes)
                header.blockAlign = byteArrayToShort(shortBytes)
                dataIs?.read(shortBytes)
                header.bitsPerSample = byteArrayToShort(shortBytes)
                //data sub-chunk
                header.subChunk2Id = "${dataIs?.readByte()?.toChar()}${dataIs?.readByte()?.toChar()}${dataIs?.readByte()?.toChar()}${dataIs?.readByte()?.toChar()}"
                dataIs?.read(intBytes)
                header.subChunk2Size = byteArrayToInt(intBytes)
                wavHeader = header
                Log.d(TAG, "readHeader success: wavHeader = $wavHeader")
            }catch (e: IOException) {
                e.printStackTrace()
                Log.d(TAG, "readHeader fail: errorMsg = ${e.message}")
                close()
            }
        }

        /**
         * 判断wav文件是否读取完毕
         */
        @Throws(IOException::class)
        fun available(): Boolean {
            return dataIs?.available()!! > 0
        }

        /**
         * 读取wav文件的data区域
         */
        @Throws(IOException::class)
        fun readData(bytes: ByteArray?, offset: Int, len: Int): Int {
            return dataIs?.read(bytes, offset, len) ?: -1
        }

        @Throws(IOException::class)
        fun readDatas(): ByteArray {
            return dataIs?.readBytes() ?: ByteArray(0)
        }

        /**
         * 关闭wav文件流
         */
        override fun close() {
            close(dataIs)
            dataIs = null
            Log.d(TAG, "close")
        }

        fun getWavHeader(): WavHeader? {
            return wavHeader
        }
    }

    /**
     * PCM写入为WAV文件
     */
    class Writer (
        private val filePath: String,
        private val sampleRateInHz: Int,
        private val channels: Int,
        private val bitsPerSample: Int
    ) : Closeable {

        companion object {
            private val TAG = Writer::class.java.simpleName
        }

        private var dataSize: Int = 0
        private var dataOs: DataOutputStream? = null

        init {
            writeHeader()
        }

        /**
         * 打开wav文件流，写入wav的header
         */
        private fun writeHeader() {
            try {
                dataOs = DataOutputStream(FileOutputStream(filePath))
                val header = WavHeader(
                    sampleRate = sampleRateInHz,
                    numChannel = channels.toShort(),
                    bitsPerSample = bitsPerSample.toShort()
                )
                dataOs?.writeBytes(header.chunkId)
                dataOs?.write(intToByteArray(header.chunkSize), 0, 4)
                dataOs?.writeBytes(header.format)
                dataOs?.writeBytes(header.subChunk1Id)
                dataOs?.write(intToByteArray(header.subChunk1Size), 0, 4)
                dataOs?.write(shortToByteArray(header.audioFormat), 0, 2)
                dataOs?.write(shortToByteArray(header.numChannel), 0, 2)
                dataOs?.write(intToByteArray(header.sampleRate), 0, 4)
                dataOs?.write(intToByteArray(header.byteRate), 0, 4)
                dataOs?.write(shortToByteArray(header.blockAlign), 0, 2)
                dataOs?.write(shortToByteArray(header.bitsPerSample), 0, 2)
                dataOs?.writeBytes(header.subChunk2Id)
                dataOs?.write(intToByteArray(header.subChunk2Size), 0, 4)
                Log.d(TAG, "writeHeader success: wavHeader = $header")
            }catch (e: IOException) {
                e.printStackTrace()
                Log.e(TAG, "writeHeader fail: errorMsg = ${e.message}")
                close()
            }
        }

        /**
         * 写入数据到wav的data区域
         */
        @Throws(IOException::class)
        fun writeData(bytes: ByteArray?, offset: Int, len: Int) {
            dataOs?.write(bytes, offset, len)
            dataSize += len
        }

        /**
         * 写入wav文件长度，关闭wav文件流
         */
        override fun close() {
            writeDataSize()
            close(dataOs)
            dataOs = null
            dataSize = 0
            Log.d(TAG, "close")
        }

        private fun writeDataSize() {
            var wavFile: RandomAccessFile? = null
            try {
                wavFile = RandomAccessFile(filePath, "rw")
                wavFile.seek(WavHeader.WAV_CHUNKSIZE_OFFSET)
                wavFile.write(intToByteArray(dataSize + WavHeader.WAV_CHUNKSIZE_EXCLUDE_DATA), 0, 4)
                wavFile.seek(WavHeader.WAV_SUB_CHUNKSIZE2_OFFSET)
                wavFile.write(intToByteArray(dataSize), 0, 4)
                Log.d(TAG, "writeDataSize success: dataSize = $dataSize bytes")
            }catch (e: IOException) {
                e.printStackTrace()
                Log.e(TAG, "writeDataSize fail: errorMsg = ${e.message}")
            }finally {
                wavFile?.close()
            }
        }
    }

    /**
     * WAV HEAD：包含RIFF、fmt、data三部分，各部分含义如下：
     * RIFF chunk: 通过chunkId来表示这是一个RIFF格式的文件，通过chunkSize记录整个wav文件的总字节数，通过format填入WAVE来标识这是一个wav文件；
     * fmt sub-chunk：通过subChunk1Id来表示这属于fmt信息，通过subChunk1Size记录后面音频参数信息存储的字节数，接着后面记录了本wav文件的详细音频参数信息(通道数、采样率、位宽等)；
     * data sub-chunk：通过subChunk2Id来表示这属于data信息，通过subChunk2Size记录后面“data”块存储的二进制原始音频数据的字节数.
     */
    data class WavHeader(
        //The "RIFF" chunk
        var chunkId: String = "RIFF",
        var chunkSize: Int = 0,
        var format: String = "WAVE",
        //The "fmt" sub-chunk
        var subChunk1Id: String = "fmt ",
        var subChunk1Size: Int = 16,
        var audioFormat: Short = 1,//PCM = 1
        var numChannel: Short = 1,//Mono = 1, Stereo = 2, etc
        var sampleRate: Int = 8000,// 8000, 44100, etc.
        var byteRate: Int = 0,//SampleRate * NumChannels * BitsPerSample / 8
        var blockAlign: Short = 0,//NumChannels * BitsPerSample / 8
        var bitsPerSample: Short = 8,//8 bits = 8, 16 bits = 16, etc.
        //The "data" sub-chunk
        var subChunk2Id: String = "data",
        var subChunk2Size: Int = 0
    ) {
        companion object {
            /**
             * WAV HEAD的总字节数
             */
            const val WAV_HEADER_SIZE = 44

            /**
             * WAV HEAD减去chunkId和chunkSize后的字节数, 即chunkSize的值减去“data”长度后的字节数
             */
            const val WAV_CHUNKSIZE_EXCLUDE_DATA = 36

            /**
             * chunkSize处于WAV HEAD的偏移
             */
            const val WAV_CHUNKSIZE_OFFSET = 4L

            /**
             * subChunk1Size处于WAV HEAD的偏移
             */
            const val WAV_SUB_CHUNKSIZE1_OFFSET = 16

            /**
             * subChunk2Size处于WAV HEAD的偏移
             */
            const val WAV_SUB_CHUNKSIZE2_OFFSET = 40L
        }
    }
}