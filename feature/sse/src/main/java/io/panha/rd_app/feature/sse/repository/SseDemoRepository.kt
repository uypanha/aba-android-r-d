package io.panha.rd_app.feature.sse.repository

import android.util.Log
import io.panha.rd_app.feature.sse.model.Closed
import io.panha.rd_app.feature.sse.model.EventModel
import io.panha.rd_app.feature.sse.model.Opened
import io.panha.rd_app.feature.sse.model.SseEvent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.cancellable
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import java.util.concurrent.TimeUnit

@Suppress("ThrowableNotThrown")
class SseDemoRepository(private val httpClient: OkHttpClient) {

    suspend fun connectToSSE(url: String, headers: Headers): Flow<SseEvent> {
        val client = this.httpClient.newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.MINUTES)
            .build()

        val builder = Request.Builder()
            .get()
            .url(url)
            .addHeader("Connection", "keep-alive")
            .headers(headers)

        val request = builder.build()
        return callbackFlow {
            val listener = object : EventSourceListener() {

                override fun onClosed(eventSource: EventSource) {
                    trySendBlocking(Closed)
                    close()
                }

                override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                    val eventModel = EventModel(id, type, data)
                    Log.i("SseDemoRepository", eventModel.title)
                    trySendBlocking(eventModel)
                }

                override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
                    close(t ?: IllegalStateException())
                }

                override fun onOpen(eventSource: EventSource, response: Response) {
                    trySendBlocking(Opened(response.request.url.toString()))
                }
            }

            val eventSource = EventSources.createFactory(client).newEventSource(request, listener)
            awaitClose { eventSource.cancel() }
        }.cancellable()
    }
}