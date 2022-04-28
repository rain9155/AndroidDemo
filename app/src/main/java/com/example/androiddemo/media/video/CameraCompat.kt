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
 * 使用Camera/Camera2采集视频YUV数据:
 * android有两套Camera Api, 分为为android 5.0以前的Camera和android 5.0以后的Camera2，通过它们提供的Api我们可以在应用中使用相机资源，进行拍照、预览等操作，
 * 相机是一个共享资源，当相机使用完毕后，必须正确地将其释放，以免其它程序访问使用时发生冲突，同时要注意兼容性问题，Camera/Camera2里的各种特性不一定所有设备都支持.
 *
 * Camera使用步骤：
 * 1、遍历摄像头，通过Camera的getCameraInfo方法获取摄像头id
 * 2、调用Camera的open方法通过cameraId打开摄像头；
 * 3、调用Camera的getParameters方法获取参数进行配置，例如配置数据回调格式、闪光灯、对焦模式、场景模式等；
 * 4、调用Camera的setPreviewDisplay方法设置预览，通过SurfaceView或TextureView的Surface；
 * 5、通过Camera的setPreviewCallback方法监听预览的回调，取到NV21(默认)或其他格式的数据回调；
 * 6、调用Camera的startPreview方法开启预览，这时就可以在控件中看到实时的预览画面；
 * 7、当不需要预览时调用Camera的stopPreview方法停止预览，同时把PreviewCallback置空；
 * 8、当不需要使用摄像头时调用Camera的release方法释放摄像头资源
 *
 * Camera2使用步骤：
 * 1、获取系统服务CameraManager，遍历摄像头，通过CameraManager的getCameraCharacteristics方法获取摄像头id；
 * 2、调用CameraManager的openCamera方法根据cameraId打开摄像头，摄像头打开回调CameraDevice.StateCallback；
 * 3、当CameraDevice.StateCallback的onOpened方法回调时获取CameraDevice实例，它代表了打开的摄像头，类似于Camera；
 * 4、调用CameraDevice的createCaptureRequest方法创建请求CaptureRequest，有六种类型的请求，预览请求为CameraDevice.TEMPLATE_PREVIEW；
 * 5、接着调用CameraDevice的createCaptureSession方法创建会话CameraCaptureSession，并传入CameraCaptureSession.StateCallback；
 * 6、同时在创建的CaptureRequest和CameraCaptureSession中传入预览的SurfaceView或TextureView的Surface和获取预览数据的ImageReader的Surface；
 * 7、同时通过ImageReader的setOnImageAvailableListener方法监听预览的回调，取到YUV_420_888(可配置)格式的数据回调；
 * 7、当CameraCaptureSession.StateCallback的onConfigured方法回调时获取CameraCaptureSession实例，调用它的setRepeatingRequest方法持续的进行预览，这时就可以在控件中看到实时的预览画面；
 * 8、当不需要预览时调用CameraCaptureSession的close方法停止预览；
 * 9、当不需要摄像头时调用cameraDevice的close方法释放摄像头资源
 *
 * YUV数据：
 * YUV是一种颜色编码方法，类似于RGB颜色编码方法，它采用Y(明亮度，Luma)和UV(色度，Chroma)来表示，而色度又定义了颜色的两个方面：色彩和饱和度，
 * YUV表示的图像每个像素点都包含Y、U、V分量，但是它的Y和UV分量是可以分离的，没有UV分量一样可以显示完整的图像，只不过是黑白的，同时对于YUV图像来说，
 * 并不是每个像素点都需要包含了Y、U、V三个分量，根据不同的采样格式，可以每个Y分量都对应自己的UV分量，也可以几个Y分量共用一组UV分量，主要有以下几种采样格式：
 * - YUV 4:4:4 采样：每一个Y对应一组UV分量
 * - YUV 4:2:2 采样：每两个Y共用一组UV分量
 * - YUV 4:2:0 采样: 每四个Y共用一组UV分量
 * YUV数据采样后，根据它的存储方式，又有多种存储格式，主要有以下几种存储格式：
 * - planar平面格式：先连续存储所有像素点的Y分量，然后存储U分量，最后是V分量，如YYYYYYYYUUUUVVVV
 * - packed打包模式：每个像素点的Y、U、V分量是连续交替存储的，如YUVYUVYUVYUV
 * 根据采样方式和存储格式的不同，就有了多种YUV格式，这些格式主要是基于YUV 4:2:2和YUV 4:2:0采样：
 * - 常见的基于YUV 4:2:2采样的YUV格式：YUYV格式(packed)、UYVY格式(packed)、YUV 422P格式(planar)
 * - 常见的基于YUV 4:2:0采样的YUV格式：YUV 420P类型(YV12格式，YU12格式)(three-planar)、YUV 420SP类型(NV12 格式、NV21 格式)(two-planar)
 *
 * 参考文档：
 * - [YUV Pixel Formats](https://www.fourcc.org/yuv/)
 * - [学会分析YUV数据](https://blog.51cto.com/ticktick/555791)
 * - [一文读懂 YUV 的采样与格式](https://glumes.com/post/ffmpeg/understand-yuv-format/#yu12-%E5%92%8C-yv12-%E6%A0%BC%E5%BC%8F)
 * - [Android Image类浅析, 结合YUV_420_888](https://www.polarxiong.com/archives/Android-Image%E7%B1%BB%E6%B5%85%E6%9E%90-%E7%BB%93%E5%90%88YUV_420_888.html)
 * - [ImageReader获得预览数据](https://www.jianshu.com/p/1a0f43813433)
 * - [Android Camera/Camera2开发指南](https://mp.weixin.qq.com/s/wskgxB-YJT-z5FhslXr1oQ)
 * - [Android相机尺寸和方向开发](https://mp.weixin.qq.com/s/E5q-lghfv3YC0_exVARqFg)
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
    private var supportCamera2 = isSupportCamera2()

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
    @SuppressLint("MissingPermission", "NewApi")
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
    @SuppressLint("NewApi")
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
    @SuppressLint("NewApi")
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

    /**
     * 获取前置或后置摄像头id和方向
     */
    @SuppressLint("NewApi")
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

    /**
     * 查询当前设备是否支持Camera2的所有特性, 通过判断CameraCharacteristics的INFO_SUPPORTED_HARDWARE_LEVEL取值：
     * INFO_SUPPORTED_HARDWARE_LEVEL_FULL：全方位的硬件支持Camera2的特性
     * INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED：有限支持Camera2的特性
     * INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY：只支持过时的Camera的特性
     */
    private fun isSupportCamera2(): Boolean {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val camera2Manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraIds = camera2Manager.cameraIdList
            if(cameraIds.isEmpty()) {
                return false
            }
            var support = true
            for (id in cameraIds) {
                if(TextUtils.isEmpty(id)) {
                    support = false
                    break
                }
                val characteristics = camera2Manager.getCameraCharacteristics(id)
                val supportLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)
                if (supportLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
                    support = false
                    break
                }
            }
            return support
        }
        return false
    }

    private fun useCamera2(): Boolean {
        return camera2 && supportCamera2
    }

    abstract class CameraListener {

        open fun onCameraOpen() {}

        open fun onPreviewStart() {}

        open fun onPreviewFrame(byteArray: ByteArray) {}
    }
}