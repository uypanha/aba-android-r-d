package io.panha.rd_app.feature.ml_kits

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import io.panha.core.designsystem.component.RDTopAppBar
import io.panha.core.designsystem.theme.LocaleTheme
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun MLKitsRoute(
    navController: NavController,
    onBackClick: () -> Unit
) {
    MLKitsScreen(
        screenState = rememberMLKitsScreenState(navController = navController),
        onBackClick = onBackClick
    )
}

@Composable
internal fun MLKitsScreen(
    screenState: MLKitsScreenState,
    onBackClick: () -> Unit
) {

    val types = screenState.types

    Scaffold(
        topBar = {
            RDTopAppBar(
                title = "ML Kits",
                navigationIcon = Icons.Outlined.ArrowBack,
                onNavigationClick = onBackClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(items = types, key = { it.hashCode() }) { type ->
                MLKitTypeItem(modifier = Modifier.clickable {
                    screenState.onTypeClick(type)
                }, type = type)
            }
        }
    }
}

@Composable
fun MLKitTypeItem(
    type: MLKitType,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = type.title,
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

@Stable
internal class MLKitsScreenState(
    private val navController: NavController,
    private val viewModel: MLKitsViewModel,
    private val coroutineScope: CoroutineScope
) {

    val types: List<MLKitType>
        @Composable get() = this.viewModel.types.collectAsStateWithLifecycle().value

    fun onTypeClick(type: MLKitType) {
        this.navController.navigate(type.route)
    }
}

@Composable
internal fun rememberMLKitsScreenState(
    navController: NavController,
    viewModel: MLKitsViewModel = koinViewModel(),
    coroutinesScope: CoroutineScope = rememberCoroutineScope(),
) = remember {
    MLKitsScreenState(navController, viewModel, coroutinesScope)
}
