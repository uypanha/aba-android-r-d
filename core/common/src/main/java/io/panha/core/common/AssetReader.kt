package io.panha.core.common

import android.content.Context
import java.io.InputStream

object AssetReader {

    fun read(context: Context, filename: String): String {
        val inputStream: InputStream = context.assets.open(filename)
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        return String(buffer)
    }
}