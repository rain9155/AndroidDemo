package com.example.androiddemo.utils

import java.io.Closeable
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * 工具方法
 * Created by chenjianyu at 2022/2/20PcmToWav
 */

fun close(closeable: Closeable?) {
    try {
        closeable?.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun intToByteArray(intData: Int): ByteArray {
    return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(intData).array()
}

fun shortToByteArray(shortData: Short): ByteArray {
    return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(shortData).array()
}

fun byteArrayToShort(byteArray: ByteArray): Short {
    return ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN).short
}

fun byteArrayToInt(byteArray: ByteArray): Int {
    return ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN).int
}