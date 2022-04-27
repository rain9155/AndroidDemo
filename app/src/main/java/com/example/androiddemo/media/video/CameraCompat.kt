package com.example.androiddemo.media.video

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.hardware.camera2.*
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.media.ImageReader
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.SurfaceHolder
import androidx.annotation.RequiresApi
import java.util.concurrent.Executor
import kotlin.properties.Delegates

/**
 * 使用Camera进行视频采集:
 *
 * Camera使用步骤：
 * 1、打开摄像头，预览（使用SurfaceView）Camera数据
 * 2、配置数据回调的格式
 * 3、设置预览，SurfaceView或TextureView
 * 4、取到 NV21 的数据回调，通过setPreviewCallback方法监听预览的回调
 * 5、停止预览并释放资源
 */
class CameraCompat(
    val context: Context,
    /**
     * 是否使用Camera2
     */
    val camera2: Boolean = true,
    /**
     * 前置摄像头还是后置摄像头
     */
    val cameraFacingBack: Boolean = true
) {

    companion object{
        private val TAG = CameraCompat::class.java.simpleName
    }

    private var camera: Camera? = null
    private var cameraId = "" //摄像头id
    private var cameraOrientation = -1 //摄像头传感器方向
    private var camera2Manager: CameraManager? = null
    private var cameraDevice: CameraDevice? = null
    private var cameraCaptureSession: CameraCaptureSession? = null
    private var previewCaptureRequest: CaptureRequest.Builder? = null
    private var imageReader by Delegates.notNull<ImageReader>()

    private val mainHandler = Handler(Looper.getMainLooper())
    private val callbackExecutor = Executor { command ->
        mainHandler.post(command)
    }

    private var isOpen = false
    private var isPreview = false

    private val cameraPreviewCallback = Camera.PreviewCallback { data, _ ->
        Log.d(TAG, "cameraPreviewCallback: data size = ${data.size}")
        cameraListener?.onPreviewFrame(data)
    }

    private val imageAvailableListener = ImageReader.OnImageAvailableListener { imageReader ->
        val image = imageReader.acquireLatestImage()
        image?.run {
            Log.d(TAG, "imageAvailableListener: wxh = ${width}x${height}")
            cameraListener?.onPreviewFrame(YuvUtils.imageI420ToNV21(this))
        }
        image?.close()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val cameraDeviceStateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            Log.d(TAG, "onOpened: camera = $camera")
            cameraDevice = camera
            if(!isOpen) {
                isOpen = true
                cameraListener?.onCameraOpen()
            }
        }

        override fun onClosed(camera: CameraDevice) {
            Log.d(TAG, "onClosed: camera = $camera")
            isOpen = false
            isPreview = false
        }

        override fun onDisconnected(camera: CameraDevice) {
            Log.d(TAG, "onDisconnected: camera = $camera")
            cameraDevice?.close()
            isOpen = false
            isPreview = false
        }

        override fun onError(camera: CameraDevice, error: Int) {
            Log.e(TAG, "onError: camera = $camera, error = $error")
            cameraDevice?.close()
            isOpen = false
            isPreview = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val cameraCaptureSessionStateCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            Log.d(TAG, "onConfigured: session = $session")
            cameraCaptureSession = session
            cameraCaptureSession?.setRepeatingRequest(previewCaptureRequest!!.build(), cameraCaptureCallback, null)
        }

        override fun onConfigureFailed(session: CameraCaptureSession) {
            Log.e(TAG, "onConfigureFailed: session = $session")
        }

        override fun onClosed(session: CameraCaptureSession) {
            Log.d(TAG, "onClosed: session = $session")
            isPreview = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val cameraCaptureCallback = object : CameraCaptureSession.CaptureCallback() {
        override fun onCaptureStarted(session: CameraCaptureSession, request: CaptureRequest, timestamp: Long, frameNumber: Long
        ) {
            if(!isPreview) {
                Log.d(TAG, "onCaptureStarted: session = $session, request = $request, timetamp = $timestamp, frameNumber = $frameNumber")
                isPreview = true
                cameraListener?.onPreviewStart()
            }
        }

        override fun onCaptureFailed(session: CameraCaptureSession, request: CaptureRequest, failure: CaptureFailure) {
            Log.e(TAG, "onCaptureFailed: session = $session, request = $request, failure = $failure")
            isPreview = false
        }
    }

    var cameraListener: CameraListener? = null

    /**
     * 是否打开相机
     */
    fun isOpen(): Boolean {
        return isOpen
    }

    /**
     * 是否正在预览
     */
    fun isPreview(): Boolean {
        return isPreview
    }

    /**
     * 根据相机id打开相机
     */
    @SuppressLint("MissingPermission")
    fun openCamera() {
        Log.d(TAG, "openCamera")
        if(isOpen) {
            return
        }
        if(TextUtils.isEmpty(cameraId)) {
            init()
        }
        if(useCamera2()) {
            camera2Manager?.openCamera(cameraId, cameraDeviceStateCallback, null)
        }else {
            camera = Camera.open(cameraId.toInt())
            isOpen = true
            cameraListener?.onCameraOpen()
        }
    }

    /**
     * 开启相机预览，输出到surface
     */
    fun startPreview(surfaceHolder: SurfaceHolder) {
        Log.d(TAG, "startPreview")
        if(isPreview) {
            return
        }
        if(useCamera2()) {
            val surfaceRect = surfaceHolder.surfaceFrame
            imageReader = ImageReader.newInstance(surfaceRect.width(), surfaceRect.height(), ImageFormat.YUV_420_888, 3).apply {
                setOnImageAvailableListener(imageAvailableListener, mainHandler)
            }
            previewCaptureRequest = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)?.apply {
                addTarget(surfaceHolder.surface)
                addTarget(imageReader.surface)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val sessionConfiguration = SessionConfiguration(
                    SessionConfiguration.SESSION_REGULAR,
                    listOf(
                        OutputConfiguration(surfaceHolder.surface),
                        OutputConfiguration(imageReader.surface)
                    ),
                    callbackExecutor,
                    cameraCaptureSessionStateCallback
                )
                cameraDevice?.createCaptureSession(sessionConfiguration)
            } else {
                cameraDevice?.createCaptureSession(
                    arrayListOf(
                        surfaceHolder.surface,
                        imageReader.surface
                    ),
                    cameraCaptureSessionStateCallback,
                    mainHandler
                )
            }
        }else {
            val cameraParameters = camera?.parameters?.apply {
                previewFormat = ImageFormat.NV21
            }
            camera?.run {
                parameters = cameraParameters
                setPreviewDisplay(surfaceHolder)
                setPreviewCallback(cameraPreviewCallback)
                startPreview()
            }
            isPreview = true
            cameraListener?.onPreviewStart()
        }
    }

    /**
     * 关闭相机预览
     */
    fun stopPreview() {
        Log.d(TAG, "stopPreview")
        if(useCamera2()) {
            cameraCaptureSession?.run {
                abortCaptures()
                close()
            }
        }else {
            camera?.run {
                setPreviewCallback(null)
                stopPreview()
            }
            isPreview = false
        }
    }

    /**
     * 关闭相机，释放相机资源
     */
    fun releaseCamera() {
        Log.d(TAG, "releaseCamera")
        if(useCamera2()) {
            cameraDevice?.close()
        }else {
            camera?.release()
            isPreview = false
            isOpen = false
        }
    }

    private fun init() {
        if(useCamera2()) {
            camera2Manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraIds = camera2Manager!!.cameraIdList //有多少个摄像头和对应id
            for(i in cameraIds) {
                val cameraCharacteristics = camera2Manager!!.getCameraCharacteristics(i)
                val cameraFacing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)
                if(cameraFacingBack && cameraFacing == CameraCharacteristics.LENS_FACING_BACK) { //后置摄像头
                    cameraId = i
                    cameraOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!
                    break
                }
                if(!cameraFacingBack && cameraFacing == CameraCharacteristics.LENS_FACING_FRONT) { //前置摄像头
                    cameraId = i
                    cameraOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!
                    break
                }
            }
        }else {
            val numberOfCameras = Camera.getNumberOfCameras()
            for (i in 0 until numberOfCameras) {
                val cameraInfo = CameraInfo()
                Camera.getCameraInfo(i, cameraInfo)
                if(cameraFacingBack && cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                    cameraId = i.toString()
                    cameraOrientation = cameraInfo.orientation
                    break
                }
                if(!cameraFacingBack && cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                    cameraId = i.toString()
                    cameraOrientation = cameraInfo.orientation
                    break
                }
            }
        }
        Log.d(TAG, "init: cameraId = $cameraId, cameraOrientation = $cameraOrientation")
    }

    private fun useCamera2(): Boolean {
        return camera2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    abstract class CameraListener {

        open fun onCameraOpen() {}

        open fun onPreviewStart() {}

        open fun onPreviewFrame(byteArray: ByteArray) {}
    }
}