package io.panha.rd_app.feature.ml_kits.segmentation_selfie

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.media.Image
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.mlkit.common.MlKitException
import io.panha.core.designsystem.component.EmptyOrError
import io.panha.core.designsystem.component.Permission
import io.panha.core.designsystem.component.RDTopAppBar
import io.panha.core.designsystem.component.demoLottie
import io.panha.rd_app.feature.ml_kits.GraphicOverlay
import kotlinx.coroutines.launch
import org.opencv.android.OpenCVLoader
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
internal fun MLKitSegmentationSelfieRoute(onBackClick: () -> Unit) {
    MLKitSegmentationSelfieScreen(onBackClick = onBackClick)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun MLKitSegmentationSelfieScreen(onBackClick: () -> Unit) {
    OpenCVLoader.initDebug()
    Scaffold(
        topBar = {
            RDTopAppBar(
                title = "Segmentation Selfie",
                navigationIcon = Icons.Outlined.Close,
                onNavigationClick = onBackClick
            )
        }
    ) { paddingValues ->
        Permission(
            permission = Manifest.permission.CAMERA,
            rational = "This permission is important for this app. Please grant the permission.",
            permissionNotAvailableContent = { permissionState ->
                val lottie = LottieCompositionSpec.Asset("animations/camera_permission.json")

                EmptyOrError(
                    title = "Camera Permission Required",
                    message = "We can't access to the camera right now until we are granted.",
                    lottie = LottieCompositionSpec.JsonString(demoLottie)
                ) {
                    TextButton(onClick = {
                        permissionState.launchPermissionRequest()
                    }) {
                        Text(text = "Request Permission")
                    }
                }
            }
        ) {

            var imageFile: Bitmap? by remember {
                mutableStateOf(null)
            }

            Box(modifier = Modifier.fillMaxSize()) {
                MLCameraPreview(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                ) {
                    imageFile = it
                }

                imageFile?.let {
                    Image(bitmap = it.asImageBitmap(), contentDescription = "")
                }
            }
        }
    }
}

@SuppressLint("RestrictedApi", "UnsafeOptInUsageError")
@Composable
internal fun MLCameraPreview(
    modifier: Modifier = Modifier,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA,
    onImageCapture: (Bitmap) -> Unit
) {

    val imageCaptureUseCase by remember {
        mutableStateOf(
            ImageCapture.Builder()
                .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()
        )
    }

    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        AndroidView(modifier = Modifier.fillMaxSize(), factory = { context ->
            val previewView = PreviewView(context).apply {
                this.scaleType = scaleType
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            val graphicOverlay = GraphicOverlay(context, null).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            // CameraX Preview UseCase
            val previewUseCase = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            val imageAnalysis = ImageAnalysis.Builder().build()
            val imageProcessor = SegmenterProcessor(context)
            imageAnalysis.setAnalyzer(Executors.newFixedThreadPool(1)) { imageProxy ->
                val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                if (rotationDegrees == 0 || rotationDegrees == 180) {
                    graphicOverlay.setImageSourceInfo(imageProxy.width, imageProxy.height, true)
                } else {
                    graphicOverlay.setImageSourceInfo(imageProxy.height, imageProxy.width, true)
                }

                try {
                    imageProcessor.processImageProxy(imageProxy, graphicOverlay)
                } catch (e: MlKitException) {
                    Log.e("MlKitException", "Failed to process image. Error: " + e.localizedMessage)
                }
            }

            coroutineScope.launch {
                val cameraProvider = context.getCameraProvider()
                try {
                    // Must unbind the use-cases before rebinding them.
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, imageCaptureUseCase, previewUseCase, imageAnalysis
                    )
                } catch (ex: Exception) {
                    Log.e("CameraPreview", "Use case binding failed", ex)
                }
            }

            FrameLayout(context).apply {
                this.addView(previewView)
                this.addView(graphicOverlay)

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        })

        val context = LocalContext.current
        Button(onClick = {
            coroutineScope.launch {
                imageCaptureUseCase.takePicture(context.executor, object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        image.image?.toBitmap()?.let {
                            onImageCapture(it)
                        }
                    }
                })
            }
        }) {
            Text(text = "Capture")
        }
    }
}

@Suppress("BlockingMethodInNonBlockingContext")
private suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { future ->
        future.addListener({
            continuation.resume(future.get())
        }, executor)
    }
}

private val Context.executor: Executor
    get() = ContextCompat.getMainExecutor(this)

fun Image.toBitmap(): Bitmap {
    val buffer = planes[0].buffer
    buffer.rewind()
    val bytes = ByteArray(buffer.capacity())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}
