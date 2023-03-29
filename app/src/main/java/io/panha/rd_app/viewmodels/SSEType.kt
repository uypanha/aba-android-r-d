package io.panha.rd_app.viewmodels

enum class SSEType {
    OKHTTP, SignalR;

    val title: String
        get() {
            return when (this) {
                OKHTTP -> "OKHTTP"
                SignalR -> "SignalR"
            }
        }
}