package io.panha.core.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RDTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = null,
    navigationIconContentDescription: String? = null,
    actionIcon: ImageVector? = null,
    actionIconContentDescription: String? = null,
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
) {

    TopAppBar(
        modifier = modifier.testTag("rdTopAppBar"),
        title = { Text(text = title) },
        navigationIcon = {
            navigationIcon?.let {
                IconButton(onClick = onNavigationClick) {
                    Icon(imageVector = navigationIcon, contentDescription = navigationIconContentDescription)
                }
            }
        },
        actions = {
            actionIcon?.let {
                IconButton(onClick = onActionClick) {
                    Icon(imageVector = actionIcon, contentDescription = actionIconContentDescription)
                }
            }
        },
    )
}

@Composable
fun RDTopAppBar(
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = null,
    navigationIconContentDescription: String? = null,
    actionIcon: ImageVector? = null,
    actionIconContentDescription: String? = null,
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
) {

    RDTopAppBar(
        title = stringResource(id = titleRes),
        modifier = modifier,
        navigationIcon = navigationIcon,
        navigationIconContentDescription = navigationIconContentDescription,
        actionIcon = actionIcon,
        actionIconContentDescription = actionIconContentDescription,
        onNavigationClick = onNavigationClick,
        onActionClick = onActionClick,
    )
}

@Preview("Top App Bar")
@Composable
private fun RDTopAppBarPreview() {
    RDTopAppBar(
        titleRes = android.R.string.untitled,
        navigationIcon = Icons.Outlined.Search,
        navigationIconContentDescription = "Navigation icon",
        actionIcon = Icons.Outlined.MoreVert,
        actionIconContentDescription = "Action icon",
    )
}
