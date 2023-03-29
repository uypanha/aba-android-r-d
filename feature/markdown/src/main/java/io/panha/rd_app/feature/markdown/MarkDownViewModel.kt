package io.panha.rd_app.feature.markdown

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.panha.core.common.StringDecoder
import io.panha.rd_app.feature.markdown.navigation.MarkdownArgs
import kotlinx.coroutines.flow.*

class MarkDownViewModel(
    savedStateHandle: SavedStateHandle,
    stringDecoder: StringDecoder,
) : ViewModel() {

    private val markDownArgs: MarkdownArgs = MarkdownArgs(savedStateHandle, stringDecoder)

    val uiState: StateFlow<MarkDownUiState> = markDownUiState(markDownArgs.content)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MarkDownUiState.Loading,
        )
}

private fun markDownUiState(content: String): Flow<MarkDownUiState> {
    return flow { emit(MarkDownUiState.Success(content)) }
}

sealed interface MarkDownUiState {
    data class Success(val content: String) : MarkDownUiState
    object Error : MarkDownUiState
    object Loading : MarkDownUiState
}