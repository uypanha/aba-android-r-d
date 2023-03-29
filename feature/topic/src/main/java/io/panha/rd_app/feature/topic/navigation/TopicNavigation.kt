package io.panha.rd_app.feature.topic.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.panha.core.model.Topic
import io.panha.rd_app.feature.topic.TopicRoute

const val topicGraphRoutePattern = "topicGraphRoutePattern"
const val topicRoute = "topic_route"

fun NavController.navigateToTopicGraph(navOptions: NavOptions? = null) {
    this.navigate(topicGraphRoutePattern, navOptions)
}

fun NavGraphBuilder.topicGraph(
    navigateToTopic: (Topic) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
) {
    navigation(
        route = topicGraphRoutePattern,
        startDestination = topicRoute
    ) {
        composable(route = topicRoute) {
            TopicRoute(navigateToTopic = navigateToTopic)
        }

        nestedGraphs()
    }
}