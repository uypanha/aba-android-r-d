package io.panha.core.common

import android.content.Context
import java.io.File
import java.io.FileWriter

object FileManager {

    fun write(context: Context, content: String, filename: String, folder: String? = null): File? {
        var rootPath = context.filesDir.absolutePath
        folder?.let { rootPath += File.separator + folder }
        val folderDir = File(rootPath)

        if (!folderDir.exists()) {
            folderDir.mkdirs()
        }

        try {
            val fileToWrite = File(folderDir, filename)
            val writer = FileWriter(fileToWrite)
            writer.append(content)
            writer.flush()
            writer.close()
            return fileToWrite
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }
}