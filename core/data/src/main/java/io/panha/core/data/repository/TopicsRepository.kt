package io.panha.core.data.repository

import io.panha.core.model.Topic
import kotlinx.coroutines.flow.Flow

interface TopicsRepository {

    fun getTopic(): Flow<List<Topic>>

}