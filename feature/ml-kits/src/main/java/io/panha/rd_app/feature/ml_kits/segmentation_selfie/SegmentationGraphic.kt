/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.panha.rd_app.feature.ml_kits.segmentation_selfie

import android.graphics.*
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.core.graphics.toColor
import com.google.mlkit.vision.segmentation.SegmentationMask
import io.panha.rd_app.feature.ml_kits.GraphicOverlay
import io.panha.rd_app.feature.ml_kits.bluralgo.BlurStackOptimized
import io.panha.rd_app.feature.ml_kits.helper.OpenCVHelper
import java.nio.ByteBuffer

/** Draw the mask from SegmentationResult in preview.  */
class SegmentationGraphic(overlay: GraphicOverlay, segmentationMask: SegmentationMask, val original: Bitmap?) :
    GraphicOverlay.Graphic(overlay) {
    private val mask: ByteBuffer
    private val maskWidth: Int
    private val maskHeight: Int
    private val isRawSizeMaskEnabled: Boolean
    private val scaleX: Float
    private val scaleY: Float

    /** Draws the segmented background on the supplied canvas.  */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun draw(canvas: Canvas) {
        var bitmap = Bitmap.createBitmap(
            maskColorsFromByteBuffer(mask, this.original?.toIntArray()), maskWidth, maskHeight, Bitmap.Config.ARGB_8888
        )
        bitmap = OpenCVHelper.blur(bitmap)//BlurStackOptimized().blur(bitmap, 5)

        if (isRawSizeMaskEnabled) {
            val matrix = Matrix(transformationMatrix)
            matrix.preScale(scaleX, scaleY)
            canvas.drawBitmap(bitmap, matrix, null)
        } else {
            canvas.drawBitmap(bitmap, transformationMatrix, null)
        }
        bitmap.recycle()
        // Reset byteBuffer pointer to beginning, so that the mask can be redrawn if screen is refreshed
        mask.rewind()
    }

    /** Converts byteBuffer floats to ColorInt array that can be used as a mask.  */
    @RequiresApi(Build.VERSION_CODES.O)
    @ColorInt
    private fun maskColorsFromByteBuffer(byteBuffer: ByteBuffer, src: IntArray?): IntArray {
        @ColorInt val colors = IntArray(maskWidth * maskHeight)
        for (i in 0 until maskWidth * maskHeight) {
            val backgroundLikelihood = 1 - byteBuffer.float
//            colors[i] = byteBuffer.int
            if (backgroundLikelihood > 0.9) {
                colors[i] = src?.get(i) ?: Color.argb(128, 57, 191, 201)
            } else if (backgroundLikelihood > 0.2) {
                // Linear interpolation to make sure when backgroundLikelihood is 0.2, the alpha is 0 and
                // when backgroundLikelihood is 0.9, the alpha is 128.
                // +0.5 to round the float value to the nearest int.
                val alpha = (182.9 * backgroundLikelihood - 36.6 + 0.5).toInt()
                val bitmapColor = src?.get(i)?.toColor()
                val color = Color.valueOf(bitmapColor?.toArgb() ?: Color.argb(255, 57, 191, 201))
                colors[i] = Color.argb(alpha.toFloat(), color.red(), color.green(), color.blue())
            }
        }
        return colors
    }

//    private fun

    init {
        mask = segmentationMask.buffer
        maskWidth = segmentationMask.width
        maskHeight = segmentationMask.height
        isRawSizeMaskEnabled =
            maskWidth != overlay.imageWidth || maskHeight != overlay.imageHeight
        scaleX = overlay.imageWidth * 1f / maskWidth
        scaleY = overlay.imageHeight * 1f / maskHeight
    }
}

fun Bitmap.overlay(bitmap2: Bitmap): Bitmap? {
    val bmOverlay = Bitmap.createBitmap(this.width, this.height, this.config)
    val canvas = Canvas(bmOverlay)
    canvas.drawBitmap(this, Matrix(), null)
    canvas.drawBitmap(bitmap2, 0f, 0f, null)
    this.recycle()
    bitmap2.recycle()
    return bmOverlay
}

fun Bitmap.toIntArray(): IntArray {
    val intArray = IntArray(this.width * this.height)
    this.getPixels(intArray, 0, width, 0, 0, width, height)
    return intArray
}

//fun getIntArray(byteBuffer: ByteBuffer): IntArray {
//    val array = IntArray(byteBuffer.capacity())
//    for (i in array.indices) {
//        array[i] = byteBuffer.getInt(i)
//    }
//    return array
//}