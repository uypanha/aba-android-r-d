package io.panha.rd_app.feature.ml_kits.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.panha.rd_app.feature.ml_kits.MLKitType
import io.panha.rd_app.feature.ml_kits.MLKitsRoute
import io.panha.rd_app.feature.ml_kits.segmentation_selfie.MLKitSegmentationSelfieRoute


fun NavController.navigateToMLKits() {
    this.navigate(route = "ml_kits_route")
}

fun NavGraphBuilder.mlKitsScreen(
    navController: NavController,
    onBackClick: () -> Unit
) {
    composable(
        route = "ml_kits_route",
    ) {
        MLKitsRoute(navController = navController, onBackClick = onBackClick)
    }

    mlKitSegmentationSelfie(onBackClick = onBackClick)
}

internal fun NavGraphBuilder.mlKitSegmentationSelfie(onBackClick: () -> Unit) {
    composable(MLKitType.SEGMENTATION_SELFIE.route) {
        MLKitSegmentationSelfieRoute(onBackClick = onBackClick)
    }
}