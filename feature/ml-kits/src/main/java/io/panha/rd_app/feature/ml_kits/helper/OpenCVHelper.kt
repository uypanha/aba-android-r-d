package io.panha.rd_app.feature.ml_kits.helper

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

object OpenCVHelper {

    fun blur(bitmap: Bitmap): Bitmap {
        val src = Mat()
        Utils.bitmapToMat(bitmap, src)
        Imgproc.GaussianBlur(src, src, Size(15.0, 15.0), 10.0)
        Utils.matToBitmap(src, bitmap)
        return bitmap
    }
}