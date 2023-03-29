package io.panha.rd_app.feature.sse.items

import kotlinx.coroutines.flow.MutableStateFlow

class HeaderItemViewModel(key: String = "", value: String = "") {

    var key: MutableStateFlow<String> = MutableStateFlow(key)
    var value: MutableStateFlow<String> = MutableStateFlow(value)

}