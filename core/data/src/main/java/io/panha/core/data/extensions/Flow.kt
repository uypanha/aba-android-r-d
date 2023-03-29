package io.panha.core.data.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

fun <T> Flow<T>.handleErrors(handler: (Throwable) -> Unit = {}): Flow<T> =
    catch { e -> handler(e) }