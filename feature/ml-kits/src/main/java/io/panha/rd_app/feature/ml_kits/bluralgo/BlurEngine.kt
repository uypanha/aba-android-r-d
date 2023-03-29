package io.panha.rd_app.feature.ml_kits.bluralgo

import android.graphics.Bitmap

interface BlurEngine {
    fun blur(image: Bitmap, radius: Int): Bitmap
    fun getType(): String
}
