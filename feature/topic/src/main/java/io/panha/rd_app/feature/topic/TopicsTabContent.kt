package io.panha.rd_app.feature.topic

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.panha.core.designsystem.theme.LocaleTheme
import io.panha.core.designsystem.theme.RDApplicationTheme
import io.panha.core.model.Topic

@Composable
fun TopicsTabContent(
    topics: List<Topic>,
    onTopicClick: (Topic) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(top = 8.dp)
    ) {
        topics.forEach { topic ->
            item(key = topic.name) {
                TopicItem(topic = topic, onClicked = {
                    onTopicClick(topic)
                })
            }
        }
    }
}

@Composable
fun TopicItem(
    topic: Topic,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClicked() },
    ) {
        Text(
            text = topic.title,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                .weight(1f)
        )

        Image(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "",
            modifier = Modifier.padding(end = 16.dp),
            colorFilter = ColorFilter.tint(LocaleTheme.current.colorScheme.onBackground)
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TopicItemPreview() {
    RDApplicationTheme {
        TopicItem(topic = Topic.SSE)
    }
}