package io.panha.rd_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import io.panha.core.model.Topic
import io.panha.rd_app.feature.markdown.navigation.markDownScreen
import io.panha.rd_app.feature.markdown.navigation.navigateToMarkdown
import io.panha.rd_app.feature.mini_app.navigation.miniAppScreen
import io.panha.rd_app.feature.mini_app.navigation.navigateToMiniApp
import io.panha.rd_app.feature.ml_kits.navigation.mlKitsScreen
import io.panha.rd_app.feature.ml_kits.navigation.navigateToMLKits
import io.panha.rd_app.feature.sse.navigation.navigateToSSE
import io.panha.rd_app.feature.sse.navigation.sseScreen
import io.panha.rd_app.feature.topic.navigation.topicGraph
import io.panha.rd_app.feature.topic.navigation.topicGraphRoutePattern

@Composable
fun RDNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = topicGraphRoutePattern,
        modifier = modifier
    ) {
        topicGraph(navigateToTopic = {
            when (it) {
                Topic.SSE -> navController.navigateToSSE()
                Topic.MINI_APP_ASSET_LOADER -> navController.navigateToMiniApp("sample-web-app")
                Topic.ML_KITS -> navController.navigateToMLKits()
            }
        }, nestedGraphs = {
            sseScreen(onBackClick = {
                navController.popBackStack()
            }, openMarkdownScreen = { content ->
                navController.navigateToMarkdown(content)
            })

            markDownScreen(onBackClick = {
                navController.popBackStack()
            })

            miniAppScreen(onBackClick = {
                navController.popBackStack()
            })

            mlKitsScreen(
                navController = navController,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        })
    }
}