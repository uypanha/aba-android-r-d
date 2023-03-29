package io.panha.rd_app.feature.sse

import android.webkit.URLUtil
import androidx.lifecycle.*
import io.panha.core.data.extensions.handleErrors
import io.panha.core.data.util.NetworkMonitor
import io.panha.rd_app.core.APIConfig
import io.panha.rd_app.feature.sse.items.HeaderItemViewModel
import io.panha.rd_app.feature.sse.model.Closed
import io.panha.rd_app.feature.sse.model.SseEvent
import io.panha.rd_app.feature.sse.repository.SseDemoRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import okhttp3.Headers.Companion.toHeaders
import java.net.UnknownHostException

interface SSEViewModelInterface {

    fun onConnectClick()

    fun onAddHeaderClick()

    fun onRemoveHeaderClick(index: Int)
}

class SSEViewModel(
    private val sseDemoRepository: SseDemoRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel(), SSEViewModelInterface {

    private val _events: MutableLiveData<List<SseEvent>> = MutableLiveData(arrayListOf())

    private val _uiState: MutableStateFlow<SSEUiState> = MutableStateFlow(SSEUiState.Empty("", ""))
    val uiState: StateFlow<SSEUiState> = this._uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SSEUiState.Empty("", ""),
        )

    private val _unreadKeys: MutableLiveData<ArrayList<String>> = MutableLiveData(arrayListOf())
    val unreadKeys: LiveData<ArrayList<String>> = this._unreadKeys.distinctUntilChanged()

    private val _isSubscribed: MutableLiveData<Boolean> = MutableLiveData(false)
    val isSubscribed: LiveData<Boolean> = this._isSubscribed.distinctUntilChanged()

    var url: MutableLiveData<String> = MutableLiveData("")
    var urlError: MutableLiveData<String?> = MutableLiveData(null)

    private val _headers: MutableLiveData<ArrayList<HeaderItemViewModel>> = MutableLiveData(arrayListOf())

    private val _headerState: MutableStateFlow<SSEHeaderState> = MutableStateFlow(SSEHeaderState.Empty)
    val headerState: StateFlow<SSEHeaderState> = _headerState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SSEHeaderState.Empty
        )

    var isSendEnabled: LiveData<Boolean> = this.url.distinctUntilChanged().map {
        !it.isNullOrEmpty()
    }

    private var chatEventJob: Job? = null

    init {
        if (BuildConfig.DEBUG) {
            url.value = "https://mdev.ababank.com/sse-poc/events"
            // "postman-echo.com/server-events/100"
            // "realtime.ably.io/event-stream?channels=rest-example&v=1.2&key=xVLyHw.-pwh7w:QRx3GoMdsFHcHcwa"
        }

        _headers.value = arrayListOf(
            HeaderItemViewModel("x-id-key", "2823CBB3C63D"),
//            HeaderItemViewModel("ts", "77777777777"),
//            HeaderItemViewModel("hash", "A831F0327D4B23EE446963FD818ADF58D6C6973E"),
        )

        viewModelScope.launch {
            _events.asFlow().collect {
                this@SSEViewModel._uiState.value = if (it.isEmpty()) SSEUiState.Empty("Demo SSE", "Start connecting to your Server by providing the URL with above text field and tap connect.")
                else SSEUiState.Events(it)
            }
        }

        viewModelScope.launch {
            _headers.asFlow().collect {
                this@SSEViewModel._headerState.value = if (it.isEmpty()) SSEHeaderState.Empty else SSEHeaderState.Headers(it)
            }
        }
    }

    override fun onConnectClick() {
        if (this.chatEventJob?.isActive == true) {
            this@SSEViewModel.appendEvent(Closed)
            this.endSSE()
        } else {
            if (this.validateInputUrl()) {
                this.urlError.postValue(null)
                this.connect(this.prepareRequestUrl())
            } else {
                this.urlError.postValue("Please enter a valid url")
            }
        }
    }

    override fun onAddHeaderClick() {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                val headers = this@SSEViewModel._headers.value ?: arrayListOf()
                headers.add(HeaderItemViewModel())
                this@SSEViewModel._headers.postValue(headers)
            }
        }
    }

    override fun onRemoveHeaderClick(index: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                val headers = this@SSEViewModel._headers.value ?: arrayListOf()
                headers.getOrNull(index)?.let {
                    headers.removeAt(index)
                }
                this@SSEViewModel._headers.postValue(headers)
            }
        }
    }

    private fun connect(url: String) {
        this.clearEvents()
        this._isSubscribed.value = true

        val headers = this._headers.value?.map { it }?.filter { it.key.value.isNotEmpty() && it.value.value.isNotEmpty() } ?: listOf()
        val headerMaps = mapOf(pairs = headers.map { Pair(it.key.value, it.value.value) }.toTypedArray())

        this.chatEventJob = viewModelScope.launch {
            this@SSEViewModel.sseDemoRepository.connectToSSE(url, headerMaps.toHeaders())
                .handleErrors { error ->
                    if (error is UnknownHostException) {
                        this@SSEViewModel.urlError.postValue(error.localizedMessage)
                    }
                    this@SSEViewModel.endSSE()
                }
                .collect {
                    this@SSEViewModel.appendEvent(it)
                    delay(100)
                }
        }
    }

    private fun validateInputUrl(): Boolean {
        val url = this.prepareRequestUrl()
        return URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url)
    }

    private fun prepareRequestUrl(): String {
        val protocol = if ((this.url.value ?: "").contains("http://")) "http" else "https"
        val urlString = this.url.value?.replace("https://", "")?.replace("http://", "")
        val config = APIConfig.getInstance(
            APIConfig.Builder().protocol(protocol).domainUrl(urlString ?: "")
        )
        return config.baseUrl
    }

    private fun endSSE() {
        this._isSubscribed.postValue(false)
        this.chatEventJob?.cancel()
        this.chatEventJob = null
    }

    private fun appendEvent(event: SseEvent) {
        val events = ArrayList(this._events.value ?: arrayListOf())
        events.add(0, event)
        updateUnread(event.hashCode().toString())
        this._events.postValue(events)

        if (event == Closed) {
            this._isSubscribed.postValue(false)
        }
    }

    fun updateUnread(key: String, read: Boolean = false) {
        var unreadKeys = this._unreadKeys.value ?: arrayListOf()
        if (read) {
            if (!unreadKeys.contains(key)) return
            unreadKeys = ArrayList(unreadKeys.filter { it != key })
        } else {
            unreadKeys.add(key)
        }
        this._unreadKeys.postValue(unreadKeys)
    }

    private fun clearEvents() {
        this._events.postValue(arrayListOf())
        clearUnread()
    }

    fun clearUnread() {
        this._unreadKeys.postValue(arrayListOf())
    }
}

sealed interface SSEUiState {
    data class Events(
        val events: List<SseEvent>,
    ) : SSEUiState

    data class Empty(val title: String, val message: String) : SSEUiState
}

sealed interface SSEHeaderState {

    data class Headers(
        val headers: List<HeaderItemViewModel>
    ) : SSEHeaderState

    object Empty : SSEHeaderState
}