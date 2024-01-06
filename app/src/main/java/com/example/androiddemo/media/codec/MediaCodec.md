Android音视频编解码之MediaCodec：
Android 底层多媒体模块采用的是 OpenMax 框架，任何 Android 底层编解码模块的实现，都必须遵循 OpenMax 标准，Google 官方默认提供了一系列的软件编解码能力， 例如 OMX.google.h264.encoder，
OMX.google.h264.encoder， OMX.google.aac.encoder， OMX.google.aac.decoder等， 而硬件编解码功能， 则需要由芯片厂商依照 OpenMax 框架标准来完成，所以一般采用不同芯片型号的手机，
硬件编解码的实现和性能是不同的，Android 应用层统一由 MediaCodec API 来提供各种音视频编解码功能，由参数配置来决定采用何种编解码算法、是否采用硬件编解码加速等

MediaCodec架构上采用了一种基于“环形缓冲区”的“生产者-消费者”模式，使用了2个“环形缓冲区”，一个在input端，一个在output端，使用流程如下：
1、Client 从 input 缓冲区队列申请 empty buffer [dequeueInputBuffer]
2、Client 把需要编解码的数据拷贝到 empty buffer，然后放入 input 缓冲区队列 [queueInputBuffer]
3、MediaCodec 模块从 input 缓冲区队列取一帧数据进行编解码处理，
4、编解码处理结束后，MediaCodec 将原始数据 buffer 置为 empty 后放回 input 缓冲区队列，将编解码后的数据放入到 output 缓冲区队列
5、Client 从 output 缓冲区队列申请编解码后的 buffer [dequeueOutputBuffer]
6、Client 对编解码后的 buffer 进行渲染/播放
7、渲染/播放完成后，Client 再将该 buffer 放回 output 缓冲区队列 [releaseOutputBuffer]

MediaCodec 从4.3以后支持通过 Surface 进行输入/输出的数据获取，使用 Surface 可以减少数据拷贝次数：
1、Buffer to Buffer：输入和输出都通过 ByteBuffer 获取数据
2、Surface to Buffer（only encoder）：输入通过 Surface 获取数据，输出使用 ByteBuffer 获取数据 
3、Buffer to Surface（only decoder）：输入通过 ByteBuffer 获取数据， 输出使用 Surface 获取数据 

MediaCodec 接受以下 YUV 格式的画面数据输入，不同手机设备可能会要求不同的格式输入：
1、 COLOR_FormatYUV420Planar (I420)
2、 COLOR_FormatYUV420PackedPlanar (also I420)
3、 COLOR_FormatYUV420SemiPlanar (NV12)
4、 COLOR_FormatYUV420PackedSemiPlanar (also NV12)
5、 COLOR_TI_FormatYUV420PackedSemiPlanar (also also NV12)

MediaCodec使用示例：
1、[EncodeAndMuxTest.java](https://bigflake.com/mediacodec/EncodeAndMuxTest.java.txt)
- 使用 MediaCodec 从 Surface 中获取 OpenGL ES 绘制的画面数据进行编码，把编码后的数据输出到 ByteBuffer 中，然后使用 MediaMuxer 把编码后的数据封装成mp4
2、[CameraToMpegTest.java](https://bigflake.com/mediacodec/CameraToMpegTest.java.txt)
- 使用 MediaCodec 从 Surface 中获取 Camera 捕获的画面纹理数据进行编码，把编码后的数据输出到 ByteBuffer 中，然后使用 MediaMuxer 把编码后的数据封装成mp4
3、[EncodeDecodeTest.java](https://android.googlesource.com/platform/cts/+/jb-mr2-release/tests/tests/media/src/android/media/cts/EncodeDecodeTest.java)
- 使用 MediaCodec 编码和解码代码生成的 YUV 数据，分别演示了编解码时Buffer to Buffer、Surface to Buffer、Buffer to Surface 的使用方式
4、[EncoderTest.java](https://android.googlesource.com/platform/cts/+/jb-mr2-release/tests/tests/media/src/android/media/cts/EncoderTest.java)
   [DecoderTest.java](https://android.googlesource.com/platform/cts/+/jb-mr2-release/tests/tests/media/src/android/media/cts/DecoderTest.java)
- 使用 MediaCodec 编码和解码音频数据
5、[MediaMuxerTest.java](https://android.googlesource.com/platform/cts/+/kitkat-release/tests/tests/media/src/android/media/cts/MediaMuxerTest.java)
- 使用 MediaExtractor 解析音视频的音频和视频，然后使用 MediaMuxer 把音频和视频再次封装
6、[screenrecord](https://android.googlesource.com/platform/frameworks/av/+/master/cmds/screenrecord/)
- adb shell屏幕录制命令，可以录制手机屏幕，里面使用了 native 版本的 MediaCodec and MediaMuxer 来实现整个录制过程

参考文档：
- [Android MediaCodec stuff](https://bigflake.com/mediacodec/)
- [Android音频开发：音频数据的编解码](https://blog.51cto.com/ticktick/1956269)
- [Android音视频开发：MediaCodec API详解](https://www.cnblogs.com/renhui/p/7478527.html)
