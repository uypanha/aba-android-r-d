package io.panha.rd_app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import io.panha.core.designsystem.icon.Icon
import io.panha.rd_app.R

enum class TopLevelDestination(
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: Int,
    val titleTextId: Int
) {
    TOPIC(
        selectedIcon = Icon.ImageVectorIcon(Icons.Filled.Home),
        unselectedIcon = Icon.ImageVectorIcon(Icons.Outlined.Home),
        iconTextId = R.string.home,
        titleTextId = R.string.home
    )
}
