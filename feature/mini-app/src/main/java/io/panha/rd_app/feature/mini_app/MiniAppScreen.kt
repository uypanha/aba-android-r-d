package io.panha.rd_app.feature.mini_app

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Web
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.panha.core.designsystem.component.EmptyOrError
import io.panha.core.designsystem.component.RDTopAppBar
import io.panha.rd_app.feature.mini_app.utils.AssetLoaderUtil
import org.koin.androidx.compose.koinViewModel
import java.io.File

@Composable
fun MiniAppRoute(onBackClick: () -> Unit) {
    MiniAppScreen(
        onBackClick = onBackClick,
        screenState = rememberMiniAppScreenState()
    )
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MiniAppScreen(
    onBackClick: () -> Unit,
    screenState: MiniAppScreenState
) {
    Scaffold(
        topBar = {
            RDTopAppBar(
                title = "Mini App",
                navigationIcon = Icons.Outlined.ArrowBack,
                onNavigationClick = onBackClick
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            val context = LocalContext.current
            val isAppDownloaded = screenState.isAppDownloaded
            val path = screenState.path

            if (isAppDownloaded) {
                val url = AssetLoaderUtil.getAssetPathUrl(path = path, domain = "panha.io")
                // url ==> "https://panha.io/mini-app/mini-app/sample-mini-app/index.html"

                val miniAppDir = File(context.filesDir, "mini-app")

                AndroidView(factory = {
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        val assetLoader = AssetLoaderUtil.getWebViewAssetLoader(context, domain = "panha.io")
                        webViewClient = LocalContentWebViewClient(assetLoader)

                        if (BuildConfig.DEBUG) {
                            webChromeClient = object : WebChromeClient() {

                                override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                                    return true
                                }
                            }
                        }

                        settings.allowFileAccess = true
                        settings.allowContentAccess = true
                        loadUrl(url)
                    }
                }, update = { webView ->
                    webView.loadUrl(url)
                })
            } else {
                EmptyOrError(
                    title = "App not found",
                    message = "Download app mini content by clicking the button below.",
                    imageVector = Icons.Outlined.Web
                ) {
                    Button(onClick = screenState::downloadContentClick) {
                        Text(text = "Download App Content")
                    }
                }
            }
        }
    }
}

@Stable
class MiniAppScreenState(
    private val viewModel: MiniAppViewModel
) : MiniAppCommand {

    val path: String
        @Composable
        get() = viewModel.path.collectAsStateWithLifecycle("").value

    val isAppDownloaded: Boolean
        @Composable
        get() = viewModel.isAppDownloaded.collectAsStateWithLifecycle(false).value

    override fun downloadContentClick() = viewModel.downloadContentClick()
}

@Composable
internal fun rememberMiniAppScreenState(
    viewModel: MiniAppViewModel = koinViewModel()
) = remember {
    MiniAppScreenState(viewModel)
}