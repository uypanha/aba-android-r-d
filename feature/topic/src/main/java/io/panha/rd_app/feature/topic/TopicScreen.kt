package io.panha.rd_app.feature.topic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.panha.core.designsystem.component.RDTopAppBar
import io.panha.core.model.DarkThemeConfig
import io.panha.core.model.Topic
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun TopicRoute(
    navigateToTopic: (Topic) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TopicViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TopicScreen(
        uiState = uiState,
        navigateToTopic = navigateToTopic,
        modifier = modifier,
        viewModel
    )
}

@Composable
internal fun TopicScreen(
    uiState: TopicUiState,
    navigateToTopic: (Topic) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TopicViewModel
) {

    Scaffold(
        topBar = {
            val themState by viewModel.themeConfigState.collectAsStateWithLifecycle()
            val icon = when (themState) {
                DarkThemeConfig.FOLLOW_SYSTEM -> Icons.Outlined.DarkMode
                DarkThemeConfig.LIGHT -> Icons.Outlined.DarkMode
                DarkThemeConfig.DARK -> Icons.Outlined.LightMode
            }
            RDTopAppBar(
                titleRes = R.string.research_and_development,
                actionIcon = icon,
                onActionClick = {
                    val toggledTheme = if (themState == DarkThemeConfig.LIGHT) DarkThemeConfig.DARK else DarkThemeConfig.LIGHT
                    viewModel.updateDarkThemeConfig(toggledTheme)
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = modifier.padding(paddingValues)) {
            when (uiState) {
                TopicUiState.Empty -> {
                }
                TopicUiState.Loading -> {
                }
                is TopicUiState.Topics -> {
                    TopicsTabContent(
                        topics = uiState.topics,
                        modifier = modifier.fillMaxWidth(),
                        onTopicClick = navigateToTopic,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
internal fun TopicScreenPreview() {
    TopicRoute(navigateToTopic = {})
}