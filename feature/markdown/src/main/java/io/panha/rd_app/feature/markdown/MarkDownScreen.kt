package io.panha.rd_app.feature.markdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.TextUnit
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.CodeBlockStyle
import com.halilibo.richtext.ui.HeadingStyle
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.string.RichTextStringStyle
import io.panha.core.designsystem.component.RDTopAppBar
import io.panha.core.designsystem.theme.LocaleTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MarkDownRoute(onBackClick: () -> Unit, viewModel: MarkDownViewModel = koinViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MarkDownScreen(uiState = uiState, onBackClick = onBackClick)
}

@Composable
fun MarkDownScreen(uiState: MarkDownUiState, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            RDTopAppBar(
                title = "How to use?",
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
            when (uiState) {
                MarkDownUiState.Error -> {}
                MarkDownUiState.Loading -> {}
                is MarkDownUiState.Success -> {
                    MarkDownReader(content = uiState.content)
                }
            }
        }
    }
}

@Composable
fun MarkDownReader(content: String) {
    RichText(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        style = RichTextStyle(
            headingStyle = HeadingStyle(LocaleTheme.current.colorScheme.onSurface),
            codeBlockStyle = CodeBlockStyle(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.LightGray)
            ),
            stringStyle = RichTextStringStyle(
                codeStyle = SpanStyle(
                    color = LocaleTheme.current.colorScheme.onSurface
                )
            )
        )
    ) {
        Markdown(content = content)
    }
}

fun HeadingStyle(color: Color): HeadingStyle = { level, textStyle ->
    when (level) {
        0 -> TextStyle(
            color = color,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )
        1 -> TextStyle(
            color = color,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        2 -> TextStyle(
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = color.copy(alpha = .7F)
        )
        3 -> TextStyle(
            color = color,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )
        4 -> TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = color.copy(alpha = .7F)
        )
        5 -> TextStyle(
            fontWeight = FontWeight.Bold,
            color = color.copy(alpha = .5f)
        )
        else -> textStyle
    }
}