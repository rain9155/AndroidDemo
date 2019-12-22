package com.example.androiddemo.surfaceviewtest

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.androiddemo.R

/**
 * View的兄弟之SurfaceView
 * SurfaceView与View的区别：
 * 1、View主要用于主动动更新情况下，SurfaceView主要适用于被动更新，例如频繁地更新
 * 2,、View在主线程对画面进行刷新，SurfaceView通常通过一个子线程来进行页面的刷新
 * 3、View在绘图时没有双缓冲机制，SurfaceView在底层实现了双缓冲机制
 * 一句话总结，如果自定义View需要频繁的刷新，或者刷新的数据量比较大，那么可以考虑使用SurfaceView取代View
 * Created by ASUS on 2018/6/27.
 */

/* SurfaceView的使用模板 */
/* 1、创建自定义SurfaceView继承自SurfaceView，并实现SurfaceHolder.Callback和Runnable接口 */
class MySurfaceView : SurfaceView, SurfaceHolder.Callback, Runnable {

    companion object {
        private  val TAG = MySurfaceView::class.java.simpleName
    }

    /* 1、获得SurfaceHolder */
    private var mSurfaceHolder = holder
    //用于绘图的Canvas
    private var mCanvas: Canvas? = null
    //子线程标志位 = false
    private var mIsDrawing = false
    //画笔
    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /* 2、在构造器中注册SurfaceHolder.Callback回调 */
    constructor(context: Context?) : super(context) {
        initSurfaceView()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initSurfaceView()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initSurfaceView()
    }

    private fun initSurfaceView() {
        //注册SurfaceHolder的回调方法
        mSurfaceHolder.addCallback(this)
        //一些属性控制
        isFocusable = true
        isFocusableInTouchMode = true
        this.keepScreenOn = true
    }

    /* 3、重写相应的Callback方法 */
    //对应surfaceview的创建,开启子线程,改变标志位
    override fun surfaceCreated(holder: SurfaceHolder) {
        mIsDrawing = true
        Thread(this).start()
        Log.d(TAG, "surfaceCreated: ")
    }

    //对应surfaceview的改变
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d(TAG, "surfaceChanged: ")
    }

    //对应surfaceview的销毁,改变标志位
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mIsDrawing = false
        Log.d(TAG, "surfaceDestroyed: ")
    }

    /* 4、使用SurfaceView */
    override fun run() {
        //子线程刷新视图
        while (mIsDrawing) {
            draw()
        }
    }

    private fun draw() {
        try {
            /* 5、获得画布，像在View中一样进行绘制 */
            mCanvas = mSurfaceHolder.lockCanvas()
            mCanvas?.drawColor(Color.YELLOW)
            mCanvas?.drawText("rain", width / 2f, height / 2f, mPaint)
            //draw something
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            /* 6、提交画布 */
            if (mCanvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas)
            }
        }
    }
}