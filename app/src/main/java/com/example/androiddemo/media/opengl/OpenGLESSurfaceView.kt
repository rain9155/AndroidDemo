package com.example.androiddemo.media.opengl

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 使用 GLSurfaceView 展示 OpenGL ES 绘制的图形
 */
class OpenGLESSurfaceView(context: Context) : GLSurfaceView(context) {

    private lateinit var triangle: Triangle
    private lateinit var square: Square
    private lateinit var image: Image

    private val triangleRenderer = object : Renderer {
        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            triangle = Triangle()
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES30.glViewport(0, 0, width, height)
        }

        override fun onDrawFrame(gl: GL10?) {
            triangle.draw()
        }

    }

    private val squareRenderer = object : Renderer {
        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            square = Square()
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES30.glViewport(0, 0, width, height)
        }

        override fun onDrawFrame(gl: GL10?) {
            square.draw()
        }

    }

    private val imageRenderer = object : Renderer {

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            image = Image(context)
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES30.glViewport(0, 0, width, height)
        }

        override fun onDrawFrame(gl: GL10?) {
            image.draw()
        }

    }

    init {
        setEGLContextClientVersion(3)

        //setRenderer(triangleRenderer)

        //setRenderer(squareRenderer)

        setRenderer(imageRenderer)
    }

}