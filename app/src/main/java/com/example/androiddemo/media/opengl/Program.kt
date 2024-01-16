package com.example.androiddemo.media.opengl

import android.opengl.GLES30
import android.util.Log
import java.nio.IntBuffer

/**
 * 加载编译 vertex shader 和 fragment shader，并把它们链接成 program 使用
 */
class Program(vertexSource: String, fragmentSource: String) {

    private var program = 0

    init {
        //加载 vertex shader
        val vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexSource)
        checkShaderCompileError(vertexShader)

        //加载 fragment shader
        val fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentSource)
        checkShaderCompileError(fragmentShader)

        // 把 shader 链接成 program
        program = GLES30.glCreateProgram()
        GLES30.glAttachShader(program, vertexShader)
        GLES30.glAttachShader(program, fragmentShader)
        GLES30.glLinkProgram(program)
        checkProgramLinkError(program)

        // shader 链接成功后删除释放
        GLES30.glDeleteShader(vertexShader)
        GLES30.glDeleteShader(fragmentShader)
    }

    /**
     * 加载编译shader
     */
    private fun loadShader(shaderType: Int, shaderSource: String): Int {
        val shader = GLES30.glCreateShader(shaderType)
        GLES30.glShaderSource(shader, shaderSource)
        GLES30.glCompileShader(shader)
        return shader
    }

    /**
     * 检查shader是否编译错误
     */
    private fun checkShaderCompileError(shader: Int) {
        val result = IntBuffer.allocate(1)
        GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, result)
        val success = result.get() > 0
        if (!success) {
            val infoLog = GLES30.glGetShaderInfoLog(shader)
            Log.e("Program", "checkShaderCompileErrors, $infoLog")
        }
    }

    /**
     * 检查program是否链接错误
     */
    private fun checkProgramLinkError(program: Int) {
        val result = IntBuffer.allocate(1)
        GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, result)
        val success = result.get() > 0
        if (!success) {
            val infoLog = GLES30.glGetProgramInfoLog(program)
            Log.e("Program", "checkProgramLinkError, $infoLog")
        }
    }

    /**
     * 激活 program
     */
    public fun use() {
        GLES30.glUseProgram(program)
    }

    /**
     * 获取 program 内的 uniform 属性引用
     */
    public fun getUniformLocation(name: String): Int {
        return GLES30.glGetUniformLocation(program, name)
    }

    /**
     * 获取 program 内的 attribute 属性引用
     */
    public fun getAttributeLocation(name: String): Int {
        return GLES30.glGetAttribLocation(program, name)
    }
}