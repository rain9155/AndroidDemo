package com.example.androiddemo.media.opengl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * Android OpenGL ES 使用示例
 *
 * 参考文档：
 * - [环境设置](https://developer.android.com/develop/ui/views/graphics/opengl/about-opengl?hl=zh-cn#manifest)
 * - [OpenGL ES 2.0 学习项目笔记](https://github.com/renhui/OpenGLES20Study)
 * - [LearnOpenGL CN](https://learnopengl-cn.github.io/intro/)
 * - [OpenGL Introduction](https://open.gl/introduction)
 */
class OpenGLESActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(OpenGLESSurfaceView(this))
    }
}