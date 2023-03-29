package io.panha.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*

@Composable
private fun EmptyOrError(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    imageContent: @Composable () -> Unit,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, end = 40.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                imageContent()
            }

            Text(text = title, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
            Text(text = message, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
            content()
        }
    }
}

@Composable
fun EmptyOrError(
    title: String,
    message: String,
    lottie: LottieCompositionSpec,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {

    EmptyOrError(modifier = modifier, title, message, imageContent = {
        val composition by rememberLottieComposition(
            lottie,
            cacheKey = lottie.toString()
        )

        val progress by animateLottieCompositionAsState(
            composition, iterations = LottieConstants.IterateForever
        )

        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .fillMaxSize()
        )
    }, content = content)
}

@Composable
fun EmptyOrError(
    title: String,
    message: String,
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    EmptyOrError(modifier = modifier, title, message, imageContent = {
        Image(
            imageVector = imageVector, contentDescription = "", modifier = Modifier
                .fillMaxSize()
        )
    }, content = content)
}

@Preview
@Composable
private fun EmptyOrErrorPreview() {
    EmptyOrError(
        title = "This is title",
        message = "This is message",
        imageVector = Icons.Filled.ArrowDownward
    )
}

@Preview
@Composable
private fun EmptyOrErrorPreviewLottie() {
    EmptyOrError(
        title = "This is title",
        message = "This is message",
        lottie = LottieCompositionSpec.JsonString(demoLottie)
    )
}