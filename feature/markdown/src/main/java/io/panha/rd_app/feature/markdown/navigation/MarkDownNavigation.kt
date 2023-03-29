package io.panha.rd_app.feature.markdown.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.panha.core.common.StringDecoder
import io.panha.rd_app.feature.markdown.MarkDownRoute

@VisibleForTesting
internal const val markDownContentArg = "markDownContent"

internal class MarkdownArgs(
    val content: String
) {

    constructor(savedStateHandle: SavedStateHandle, stringDecoder: StringDecoder) : this(stringDecoder.decodeString(checkNotNull(savedStateHandle[markDownContentArg])))
}

fun NavController.navigateToMarkdown(content: String) {
    this.navigate(route = "mark_down_route/$content")
}

fun NavGraphBuilder.markDownScreen(
    onBackClick: () -> Unit
) {
    composable(
        route = "mark_down_route/{$markDownContentArg}",
        arguments = listOf(
            navArgument(markDownContentArg) { type = NavType.StringType },
        )
    ) {
        MarkDownRoute(onBackClick = onBackClick)
    }
}