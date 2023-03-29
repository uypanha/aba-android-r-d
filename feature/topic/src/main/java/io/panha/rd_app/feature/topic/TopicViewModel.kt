package io.panha.rd_app.feature.topic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.panha.core.data.repository.TopicsRepository
import io.panha.core.data.repository.UserDataRepository
import io.panha.core.model.DarkThemeConfig
import io.panha.core.model.Topic
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TopicViewModel(
    private val topicsRepository: TopicsRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    val uiState: StateFlow<TopicUiState> = topicsRepository.getTopic()
        .map(TopicUiState::Topics)
        .stateIn(
            scope = this.viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TopicUiState.Loading
        )

    val themeConfigState: StateFlow<DarkThemeConfig> = this.userDataRepository.userData.map { it.darkThemeConfig }
        .stateIn(
            scope = this.viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DarkThemeConfig.FOLLOW_SYSTEM
        )

    fun updateDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        viewModelScope.launch {
            userDataRepository.setDarkThemeConfig(darkThemeConfig)
        }
    }
}

sealed interface TopicUiState {
    object Loading : TopicUiState

    data class Topics(
        val topics: List<Topic>,
    ) : TopicUiState

    object Empty : TopicUiState
}