package io.panha.rd_app.feature.mini_app

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.panha.core.common.AssetReader
import io.panha.core.common.FileManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

interface MiniAppCommand {

    fun downloadContentClick()
}

@SuppressLint("StaticFieldLeak")
class MiniAppViewModel(private val context: Context) : ViewModel(), MiniAppCommand {

    private val _path: MutableStateFlow<String> = MutableStateFlow("mini-app/sample-mini-app/index.html")
    val path: StateFlow<String> = _path

    val isAppDownloaded: StateFlow<Boolean> = _path.map { path ->
        val file = File(context.filesDir.absolutePath + File.separator + path)
        file.exists()
    }.stateIn(
        scope = viewModelScope,
        initialValue = false,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    override fun downloadContentClick() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val content = AssetReader.read(context, "index.html")
                FileManager.write(context, content, "index.html", "mini-app/sample-mini-app")

                withContext(Dispatchers.Main) {
                    this@MiniAppViewModel._path.value = ""
                    this@MiniAppViewModel._path.value = "mini-app/sample-mini-app/index.html"
                }
            }
        }
    }
}