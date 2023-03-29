package io.panha.rd_app.feature.sse.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.panha.rd_app.feature.sse.SSERoute

fun NavController.navigateToSSE() {
    this.navigate("sse_route")
}

fun NavGraphBuilder.sseScreen(
    onBackClick: () -> Unit,
    openMarkdownScreen: (String) -> Unit
) {
    composable("sse_route") {
        SSERoute(onBackClick = onBackClick, openMarkdownScreen = openMarkdownScreen)
    }
}