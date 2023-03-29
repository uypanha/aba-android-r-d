package io.panha.rd_app

import io.panha.rd_app.core.APIConfig
import io.panha.rd_app.core.network.RetrofitClient
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import org.junit.Test
import java.io.IOException

class SSEUnitTest {

    @Test
    fun testSSEOkttp() {
        val eventSourceListener = object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: Response) {
                super.onOpen(eventSource, response)
                Log.d("Test", "Connection Opened")
            }

            override fun onClosed(eventSource: EventSource) {
                super.onClosed(eventSource)
                Log.d("Test", "Connection Closed")
            }

            override fun onEvent(
                eventSource: EventSource,
                id: String?,
                type: String?,
                data: String
            ) {
                super.onEvent(eventSource, id, type, data)
                Log.d("Test", "On Event Received! Data -: $data")
                assert(true, lazyMessage = {
                    "On Event Received! Data -: $data"
                })
            }

            override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
                super.onFailure(eventSource, t, response)
                Log.d("\"Test\"", "On Failure -: ${response?.body}")
                assert(false, lazyMessage = {
                    "On Failure -: ${response?.body}"
                })
            }
        }

        val client = RetrofitClient.getClient()
        val config = APIConfig.getInstance(
            APIConfig.Builder().protocol("https").domainUrl("mdev.ababank.com/sse-poc")
        )

        val request = Request.Builder()
            .url("${config.baseUrl}/sse-events?apikey=hqPPoGFlLSB8syfOXMwZZzlFn4b44th8&abaid=26669")
            .header("Accept", "application/json; q=0.5")
            .addHeader("Accept", "text/event-stream")
            .build()

        EventSources.createFactory(client)
            .newEventSource(request = request, listener = eventSourceListener)

        client.newCall(request).enqueue(responseCallback = object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Test", "API Call Failure ${e.localizedMessage}")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("Test", "APi Call Success ${response.body.toString()}")
            }
        })

        Thread.sleep(30000)
    }
}