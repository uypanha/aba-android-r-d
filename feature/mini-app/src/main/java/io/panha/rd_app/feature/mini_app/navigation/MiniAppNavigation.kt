package io.panha.rd_app.feature.mini_app.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.panha.core.common.StringDecoder
import io.panha.rd_app.feature.mini_app.MiniAppRoute


@VisibleForTesting
internal const val miniAppIdArg = "mini_app_id"

internal class MiniAppArgs(
    val path: String
) {

    constructor(savedStateHandle: SavedStateHandle, stringDecoder: StringDecoder) : this(stringDecoder.decodeString(checkNotNull(savedStateHandle[miniAppIdArg])))
}

fun NavController.navigateToMiniApp(id: String) {
    this.navigate(route = "mini_app_route/$id")
}

fun NavGraphBuilder.miniAppScreen(
    onBackClick: () -> Unit
) {
    composable(
        route = "mini_app_route/{$miniAppIdArg}",
        arguments = listOf(
            navArgument(miniAppIdArg) { type = NavType.StringType },
        )
    ) {
        MiniAppRoute(onBackClick = onBackClick)
    }
}