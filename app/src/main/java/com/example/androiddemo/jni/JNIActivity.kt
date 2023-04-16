package com.example.androiddemo.jni

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.R
import com.example.nativelib.NativeLib

class JNIActivity : AppCompatActivity() {

    private val nativeLib = NativeLib()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jni)

        nativeLib.stringFromJNI()
    }
}