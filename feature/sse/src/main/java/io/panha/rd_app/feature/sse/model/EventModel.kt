package io.panha.rd_app.feature.sse.model

import androidx.compose.ui.graphics.Color

sealed interface SseEvent {
    val title: String
}

data class EventModel(
    val id: String?,
    val type: String?,
    val data: String
) : SseEvent {

    override val title: String
        get() = this.data

    val color: Color
        get() {
            return when (this.type) {
                "ping" -> Color(0xFFF7D15B)
                "info" -> Color(0xFF68C97C)
                "error" -> Color(0xFFEDADA7)
                "notification" -> Color(0xFFDD8B69)
                "message" -> Color(0xFF81ADF0)
                else -> Color(0xFFF7D15B)
            }
        }

    val borderColor: Color
        get() {
            return color
        }
}

data class Opened(val url: String) : SseEvent {

    override val title: String
        get() = "Connected to $url"
}

object Closed : SseEvent {

    override val title: String
        get() = "Connection closed"
}