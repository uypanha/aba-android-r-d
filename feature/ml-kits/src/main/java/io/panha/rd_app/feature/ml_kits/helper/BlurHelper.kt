package io.panha.rd_app.feature.ml_kits.helper

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

object BlurHelper {

    private const val BLUR_SIZE = 29.0
    private const val BASE_PIXELS = 1080.0 * 1080.0
    private const val BASE_SIZE = BASE_PIXELS / BLUR_SIZE

    fun blur(bitmap: Bitmap): Bitmap {
        val pixels = bitmap.width * bitmap.height
        val src = Mat()
        Utils.bitmapToMat(bitmap, src)
        val size = getBlurSize(pixels)
        Imgproc.GaussianBlur(src, src, Size(size, size), 10.0)
        Utils.matToBitmap(src, bitmap)
        return bitmap
    }

    private fun getBlurSize(pixels: Int): Double {
        val size = pixels / BASE_SIZE
        val remain = size % 2
        if (remain < 1.0) {
            return (size.toInt() + 1).toDouble()
        }
        return size.toInt().toDouble()
    }
}