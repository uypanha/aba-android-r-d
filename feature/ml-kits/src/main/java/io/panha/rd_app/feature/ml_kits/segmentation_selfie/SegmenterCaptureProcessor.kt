package io.panha.rd_app.feature.ml_kits.segmentation_selfie

import android.annotation.SuppressLint
import android.util.Size
import android.view.Surface
import androidx.camera.core.impl.CaptureProcessor
import androidx.camera.core.impl.ImageProxyBundle

@SuppressLint("RestrictedApi")
class SegmenterCaptureProcessor : CaptureProcessor {

    override fun onOutputSurface(surface: Surface, imageFormat: Int) {
//        Log.i("SegmenterCaptureProcessor", "Image Format == $imageFormat")
    }

    override fun process(bundle: ImageProxyBundle) {
//        Log.i("SegmenterCaptureProcessor", bundle.toString())
    }

    override fun onResolutionUpdate(size: Size) {
//        Log.i("SegmenterCaptureProcessor", "Size == $size")
    }
}