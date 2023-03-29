package io.panha.rd_app.feature.topic.di

import io.panha.rd_app.feature.topic.TopicViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val topicModule = module {
    viewModel { TopicViewModel(get(), get()) }
}