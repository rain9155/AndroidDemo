package com.example.androiddemo.media.opengl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLES30
import android.opengl.GLUtils
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**
 * 绘制一张图片，演示texture的基本使用
 *
 * 参考文档：
 * - [纹理](https://learnopengl-cn.github.io/01%20Getting%20started/06%20Textures/)
 * - [textures_combined.cpp](https://learnopengl.com/code_viewer_gh.php?code=src/1.getting_started/4.2.textures_combined/textures_combined.cpp)
 */
class Image(context: Context) {

    private val vertexShaderSource = """
        attribute vec3 vPosition;
        attribute vec2 vTexPosition;
        
        // out
        varying vec2 aTexPosition;
        
        void main() {
            gl_Position = vec4(vPosition, 1.0);
            aTexPosition = vTexPosition;
        }
    """

    private val fragmentShaderSource = """
        precision mediump float;
        
        // in
        varying vec2 aTexPosition;
        
        // texture samplers
        uniform sampler2D vTex;

        void main() {
            gl_FragColor = texture2D(vTex, aTexPosition);
        }
    """

    private val imageVertices = floatArrayOf(
        // positions          // texture coords
        0.5f,  0.5f, 0.0f,    1.0f, 1.0f, // top right
        0.5f, -0.5f, 0.0f,    1.0f, 0.0f, // bottom right
        -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, // bottom left
        -0.5f,  0.5f, 0.0f,   0.0f, 1.0f  // top left
    )

    private val imageIndices = intArrayOf(
        0, 1, 3, // first triangle
        1, 2, 3  // second triangle
    )

    private val imageProgram: Program

    private val vertexArrayObject: Int

    private val texture: Int

    init {
        // 加载编译 vertexShader 和 fragmentShader, 并创建 program
        imageProgram = Program(vertexShaderSource, fragmentShaderSource)

        //创建 VAO
        val vaoByteBuffer = IntBuffer.allocate(1)
        GLES30.glGenVertexArrays(1, vaoByteBuffer)
        vertexArrayObject = vaoByteBuffer.get()

        // 创建 VBO
        val vboByteBuffer = IntBuffer.allocate(1)
        GLES30.glGenBuffers(1, vboByteBuffer)
        val vertexBufferObject = vboByteBuffer.get()

        // 创建 EBO
        val eboByteBuffer = IntBuffer.allocate(1)
        GLES30.glGenBuffers(1, eboByteBuffer)
        val elementArrayBuffer = eboByteBuffer.get()

        // 绑定 VAO，下面的属性设置都会保存在 VAO 中，供后续绘制使用
        GLES30.glBindVertexArray(vertexArrayObject)

        // 绑定 VBO
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferObject)

        // 传递顶点数据到绑定的 VBO
        val vertexSize = imageVertices.size * 4
        val vertexByteBuffer = FloatBuffer.allocate(vertexSize)
        vertexByteBuffer.put(imageVertices)
        vertexByteBuffer.position(0)
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertexSize, vertexByteBuffer, GLES30.GL_STATIC_DRAW)

        // 绑定 EBO
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, elementArrayBuffer)

        // 传递索引数据到绑定到 EBO
        val indicesSize = imageIndices.size * 4
        val indicesByteBuffer = IntBuffer.allocate(indicesSize)
        indicesByteBuffer.put(imageIndices)
        indicesByteBuffer.position(0)
        GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, indicesSize, indicesByteBuffer, GLES30.GL_STATIC_DRAW)

        // 设置 vertexShader 中的 vPosition 属性, 让 vertexShader 知道如何从绑定的 VBO 中获取顶点坐标
        val vPositionHandle = imageProgram.getAttributeLocation("vPosition")
        GLES30.glEnableVertexAttribArray(vPositionHandle)
        GLES30.glVertexAttribPointer(
            vPositionHandle,
            3,
            GLES30.GL_FLOAT,
            false,
            5 * 4,
            0
        )

        // 设置 vertexShader 中的 vTexPosition 属性, 让 vertexShader 知道如何从绑定的 VBO 中获取纹理坐标
        val vTexPositionHandle = imageProgram.getAttributeLocation("vTexPosition")
        GLES30.glEnableVertexAttribArray(vTexPositionHandle)
        GLES30.glVertexAttribPointer(
            vTexPositionHandle,
            2,
            GLES30.GL_FLOAT,
            false,
            5 * 4,
            3 * 4
        )

        // 创建 texture
        val textureByteBuffer = IntBuffer.allocate(1)
        GLES30.glGenTextures(1, textureByteBuffer)
        texture = textureByteBuffer.get()

        // 绑定 texture，并设置参数
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture)
        // texture 环绕方式设置
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT)
        // texture 过滤方式设置
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)

        // 传递图片数据到绑定的 texture
        val bitmap = BitmapFactory.decodeStream(context.resources.assets.open("image.png"))
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0)

        // 设置 fragmentShader 中的 vTex 属性, 绑定到 GL_TEXTURE0，让 fragmentShader 可以从 GL_TEXTURE0 中获取纹理数据
        imageProgram.use() // uniform 属性设置前需要先激活 program
        val vTexHandle = imageProgram.getUniformLocation("vTex")
        GLES30.glUniform1i(vTexHandle, 0)
    }

    public fun draw() {
        // 清除上一次绘制
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        // 激活 GL_TEXTURE0，绑定 texture 到 GL_TEXTURE0
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture)

        // 激活 program
        imageProgram.use()

        // 绑定 VAO
        GLES30.glBindVertexArray(vertexArrayObject)

        // 绘制四边形
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_INT, 0)
    }
}