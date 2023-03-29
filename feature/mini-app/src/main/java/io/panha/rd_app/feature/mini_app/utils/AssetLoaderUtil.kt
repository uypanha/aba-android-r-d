package io.panha.rd_app.feature.mini_app.utils

import android.content.Context
import androidx.webkit.WebViewAssetLoader
import java.io.File

object AssetLoaderUtil {

    private const val DEFAULT_DOMAIN_NAME = "panha.io"
    private const val ASSET_LOCALE_FILE_PATH = "/mini-app/"

    fun getWebViewAssetLoader(context: Context, domain: String = DEFAULT_DOMAIN_NAME): WebViewAssetLoader {
        val miniAppDir = File(context.filesDir.absolutePath)
        return WebViewAssetLoader.Builder()
            .setDomain(domain)
            .addPathHandler(
                ASSET_LOCALE_FILE_PATH,
                WebViewAssetLoader.InternalStoragePathHandler(context, miniAppDir)
            ).build()
    }

    fun getAssetPathUrl(path: String, domain: String = DEFAULT_DOMAIN_NAME): String {
        return "https://$domain$ASSET_LOCALE_FILE_PATH$path"
    }
}