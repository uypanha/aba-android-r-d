package io.panha.rd_app.feature.sse

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieCompositionSpec
import io.panha.core.common.AssetReader
import io.panha.core.designsystem.component.EmptyOrError
import io.panha.core.designsystem.component.RDBasicTextField
import io.panha.core.designsystem.component.RDTextField
import io.panha.core.designsystem.component.RDTopAppBar
import io.panha.core.designsystem.theme.LocaleTheme
import io.panha.rd_app.feature.sse.SSEUiState.*
import io.panha.rd_app.feature.sse.items.HeaderItemViewModel
import io.panha.rd_app.feature.sse.model.Closed
import io.panha.rd_app.feature.sse.model.EventModel
import io.panha.rd_app.feature.sse.model.Opened
import io.panha.rd_app.feature.sse.model.SseEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SSERoute(
    onBackClick: () -> Unit,
    openMarkdownScreen: (String) -> Unit,
    sseScreenState: SSEScreenState = rememberSSEScreenState()
) {
    SSEScreen(
        onBackClick = onBackClick,
        openMarkdownScreen = openMarkdownScreen,
        sseScreenState
    )
}

@SuppressLint("MutableCollectionMutableState")
@Composable
internal fun SSEScreen(onBackClick: () -> Unit, openMarkdownScreen: (String) -> Unit, sseScreenState: SSEScreenState) {

    val uiState = sseScreenState.uiState
    val isSubscribed = sseScreenState.isSubscribed
    val unreadKeys = sseScreenState.unreadKeys

    val context = LocalContext.current

    Scaffold(
        topBar = {
            RDTopAppBar(
                title = stringResource(id = R.string.server_sent_events),
                navigationIcon = Icons.Outlined.ArrowBack,
                onNavigationClick = onBackClick,
                actionIcon = Icons.Outlined.Description,
                onActionClick = {
                    val content = AssetReader.read(context, "sse_readme.md")
                    openMarkdownScreen(content)
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            Column(modifier = Modifier.fillMaxSize()) {
                SSEURLInput(modifier = Modifier.fillMaxWidth(), sseScreenState = sseScreenState)

                when (uiState) {
                    is Events -> {
                        EventsContent(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            events = uiState.events,
                            unreadKeys = unreadKeys,
                            shouldClearUnread = { key ->
                                key?.let {
                                    sseScreenState.updateUnread(key, true)
                                } ?: sseScreenState.clearUnread()
                            },
                            coroutineScope = sseScreenState.coroutinesScope,
                        )
                    }
                    is Empty -> {
                        if (!isSubscribed) {
                            val lottie = LottieCompositionSpec.Asset("animations/chat_.json")
                            EmptyOrError(
                                title = uiState.title,
                                message = uiState.message,
                                lottie = lottie
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SSEURLInput(modifier: Modifier = Modifier, sseScreenState: SSEScreenState) {

    val headers = sseScreenState.headerState
    val url = sseScreenState.url
    val error by sseScreenState.sseViewModel.urlError.observeAsState(null)
    val isSendEnabled by sseScreenState.sseViewModel.isSendEnabled.observeAsState(false)

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            RDTextField(
                value = url,
                onValueChange = {
                    sseScreenState.onUrlTextChanged(it)
                },
                modifier = Modifier.weight(1f),
                label = {
                    Text(text = "SSE URL")
                },
                placeholder = {
                    Text(text = "Enter SSE Url")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                singleLine = true,
                enabled = !sseScreenState.isSubscribed,
                isError = error != null,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    containerColor = Color.Transparent,
                )
            )

            Button(
                onClick = {
                    sseScreenState.onConnectClick()
                },
                modifier = Modifier,
                enabled = isSendEnabled,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = if (!sseScreenState.isSubscribed) "Connect" else "Disconnect")
            }
        }

        SSEHeadersInput(
            modifier = Modifier.fillMaxWidth(),
            headerState = headers,
            isSubscribed = sseScreenState.isSubscribed,
            onAddHeaderClick = { sseScreenState.onAddHeaderClick() },
            onRemoveHeaderClick = { sseScreenState.onRemoveHeaderClick(it) },
        )

        if (sseScreenState.isSubscribed) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        } else {
            Divider(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.outline)
        }
    }
}

@Composable
internal fun SSEHeadersInput(
    modifier: Modifier = Modifier,
    headerState: SSEHeaderState,
    isSubscribed: Boolean = false,
    onAddHeaderClick: () -> Unit = {},
    onRemoveHeaderClick: (Int) -> Unit = {}
) {

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp), horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = onAddHeaderClick,
                enabled = !isSubscribed
            ) {
                Image(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add Icons",
                    colorFilter = ColorFilter.tint(LocalContentColor.current),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Header",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        when (headerState) {
            SSEHeaderState.Empty -> {}
            is SSEHeaderState.Headers -> {
                HeadersContent(
                    modifier = Modifier.fillMaxWidth(),
                    headers = headerState.headers,
                    isSubscribed = isSubscribed,
                    onRemoveHeaderClick = onRemoveHeaderClick
                )
            }
        }
    }
}

@Composable
internal fun HeadersContent(
    modifier: Modifier = Modifier,
    headers: List<HeaderItemViewModel>,
    isSubscribed: Boolean,
    onRemoveHeaderClick: (Int) -> Unit = {}
) {

    LazyColumn(
        modifier = modifier
            .padding(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        headers.forEachIndexed { index, header ->
            val key = index + header.hashCode()
            item(key = key) {
                HeaderItem(header = header, enabled = !isSubscribed, index = index, onRemoveHeaderClick = onRemoveHeaderClick)
            }
        }
    }
}

@Composable
internal fun EventsContent(
    modifier: Modifier = Modifier,
    events: List<SseEvent>,
    unreadKeys: List<String>,
    coroutineScope: CoroutineScope,
    shouldClearUnread: (key: String?) -> Unit,
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = 0)

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        LazyColumn(modifier = modifier.fillMaxSize(), state = listState) {
            events.forEach {
                item(it.hashCode().toString()) {
                    EventItem(it, modifier = Modifier.fillMaxWidth())
                }
            }
        }

        val shouldShowNewMessage by remember {
            derivedStateOf {
                listState.firstVisibleItemIndex > 0
            }
        }

        val shouldStayOnTop by remember {
            derivedStateOf {
                !listState.isScrollInProgress && listState.firstVisibleItemIndex == 1
            }
        }

//        val itemKeyVisible by remember {
//            derivedStateOf {
//                listState.layoutInfo
//                    .visibleItemsInfo
//            }
//        }

        val firstItemVisible by remember {
            derivedStateOf {
                listState.firstVisibleItemIndex == 0
            }
        }

        if (firstItemVisible) {
            shouldClearUnread(null)
        } else {
//            itemKeyVisible.forEach {
//                shouldClearUnread(it.key.toString())
//            }
        }

        if (shouldStayOnTop) {
            LaunchedEffect(key1 = 1) {
                coroutineScope.launch {
                    listState.scrollToItem(0, scrollOffset = 0)
                }
            }
        } else {
            val showMessage = shouldShowNewMessage && unreadKeys.isNotEmpty()
            AnimatedVisibility(
                visible = showMessage,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Card(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable {
                            coroutineScope.launch {
                                listState.animateScrollToItem(0, scrollOffset = 0)
                            }
                        },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(Color(0xFF4190FF))
                ) {
                    Row(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            imageVector = Icons.Outlined.ArrowUpward,
                            modifier = Modifier.size(14.dp),
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Text(
                            text = "New messages (${unreadKeys.count()})",
                            style = TextStyle(fontSize = 12.sp, color = Color.White)
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun EventItem(event: SseEvent, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val icon = if (event == Closed || event is Opened) Icons.Outlined.Info else Icons.Filled.ArrowDownward

        if (event == Closed || event is Opened) {
            Image(
                imageVector = icon,
                contentDescription = "",
                colorFilter = ColorFilter.tint(LocaleTheme.current.colorScheme.onSurface),
            )
        } else {
            Surface(color = Color(0xFF0043a1).copy(alpha = 0.2f)) {
                Image(
                    imageVector = icon,
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color(0xFF4190FF)),
                    modifier = Modifier
                        .padding(4.dp)
                        .size(16.dp)
                )
            }
        }

        (event as? EventModel)?.let {
            val backgroundColor = it.color.copy(alpha = 0.2f)
            val textColor = it.color
            Card(colors = CardDefaults.cardColors(backgroundColor), shape = RoundedCornerShape(4.dp), elevation = CardDefaults.cardElevation(0.dp), border = BorderStroke(1.dp, it.borderColor)) {
                Text(
                    text = it.type ?: "undefined",
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight(700)),
                    color = textColor
                )
            }
        }

        Text(text = event.title, modifier = Modifier.weight(1f), style = TextStyle(fontSize = 14.sp))
    }
}

@Composable
fun HeaderItem(modifier: Modifier = Modifier, header: HeaderItemViewModel, index: Int, enabled: Boolean, onRemoveHeaderClick: (Int) -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        val key by header.key.collectAsStateWithLifecycle()
        val value by header.value.collectAsStateWithLifecycle()

        RDBasicTextField(
            value = key,
            modifier = Modifier
                .weight(1f)
                .height(36.dp),
            onValueChange = {
                header.key.value = it
            },
            placeholder = "Key",
            singleLine = true,
            textStyle = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
            enabled = enabled
        )

        RDBasicTextField(
            modifier = Modifier
                .weight(1f)
                .height(36.dp),
            value = value,
            onValueChange = {
                header.value.value = it
            },
            placeholder = "Value",
            singleLine = true,
            textStyle = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
            enabled = enabled
        )

        Box(modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onRemoveHeaderClick(index)
            }
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete",
                colorFilter = ColorFilter.tint(LocaleTheme.current.colorScheme.error)
            )
        }
    }
}


@Stable
class SSEScreenState(
    val sseViewModel: SSEViewModel,
    val coroutinesScope: CoroutineScope,
) : SSEViewModelInterface {

    val uiState: SSEUiState
        @Composable
        get() = sseViewModel.uiState.collectAsStateWithLifecycle().value

    val headerState: SSEHeaderState
        @Composable
        get() = sseViewModel.headerState.collectAsStateWithLifecycle().value

    val isSubscribed: Boolean
        @Composable get() = sseViewModel.isSubscribed.observeAsState(false).value

    val unreadKeys: List<String>
        @Composable get() = sseViewModel.unreadKeys.observeAsState(arrayListOf()).value

    val url: String
        @Composable get() = sseViewModel.url.observeAsState("").value

    override fun onConnectClick() {
        sseViewModel.onConnectClick()
    }

    fun updateUnread(key: String, read: Boolean = false) {
        sseViewModel.updateUnread(key, read)
    }

    fun clearUnread() {
        sseViewModel.clearUnread()
    }

    override fun onAddHeaderClick() {
        sseViewModel.onAddHeaderClick()
    }

    override fun onRemoveHeaderClick(index: Int) {
        sseViewModel.onRemoveHeaderClick(index)
    }

    fun onUrlTextChanged(text: String) {
        this.sseViewModel.url.value = text
    }
}

@Composable
fun rememberSSEScreenState(
    sseViewModel: SSEViewModel = koinViewModel(),
    coroutinesScope: CoroutineScope = rememberCoroutineScope(),
) = remember {
    SSEScreenState(sseViewModel, coroutinesScope)
}