package com.example.androiddemo.media.opengl

import android.opengl.GLES20
import android.opengl.GLES30
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**
 * 绘制四边形，通过索引绘制两个三角形实现，演示EBO的基本使用
 *
 * 参考文档：
 * - [Hello Triangle](https://learnopengl-cn.github.io/01%20Getting%20started/04%20Hello%20Triangle/)
 * - [hello_triangle_indexed.cpp](https://learnopengl.com/code_viewer_gh.php?code=src/1.getting_started/2.2.hello_triangle_indexed/hello_triangle_indexed.cpp)
 */
class Square {

    private val vertexShaderSource = """
        attribute vec3 vPosition;
        void main() {
            gl_Position = vec4(vPosition, 1.0);
        }
    """

    private val fragmentShaderSource = """
        precision mediump float;
        void main() {
            gl_FragColor = vec4(1.0, 0.5, 0.2, 1.0);
        }
    """

    private val squareVertices = floatArrayOf(
        0.5f,  0.5f, 0.0f,  // top right
        0.5f, -0.5f, 0.0f,  // bottom right
        -0.5f, -0.5f, 0.0f,  // bottom left
        -0.5f,  0.5f, 0.0f   // top left
    )

    private val squareIndices = intArrayOf(
        0, 1, 3,  // first Triangle
        1, 2, 3   // second Triangle
    )

    private val squareProgram: Program

    private val vertexArrayObject: Int

    init {
        // 加载编译 vertexShader 和 fragmentShader, 并创建 program
        squareProgram = Program(vertexShaderSource, fragmentShaderSource)

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
        val vertexSize = squareVertices.size * 4
        val vertexByteBuffer = FloatBuffer.allocate(vertexSize)
        vertexByteBuffer.put(squareVertices)
        vertexByteBuffer.position(0)
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertexSize, vertexByteBuffer, GLES30.GL_STATIC_DRAW)

        // 绑定 EBO
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, elementArrayBuffer)

        // 传递索引数据到绑定到 EBO
        val indicesSize = squareIndices.size * 4
        val indicesByteBuffer = IntBuffer.allocate(indicesSize)
        indicesByteBuffer.put(squareIndices)
        indicesByteBuffer.position(0)
        GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, indicesSize, indicesByteBuffer, GLES30.GL_STATIC_DRAW)

        // 设置 vertexShader 中的 vPosition 属性, 让 vertexShader 知道如何从绑定的 VBO 中获取顶点坐标
        val vPositionHandle = squareProgram.getAttributeLocation("vPosition")
        GLES30.glEnableVertexAttribArray(vPositionHandle)
        GLES30.glVertexAttribPointer(
            vPositionHandle,
            3,
            GLES30.GL_FLOAT,
            false,
            3 * 4,
            0
        )
    }

    public fun draw() {
        // 清除上一次绘制
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        // 激活 program
        squareProgram.use()

        // 绑定 VAO
        GLES30.glBindVertexArray(vertexArrayObject)

        // 绘制四边形
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_INT, 0)
    }
}