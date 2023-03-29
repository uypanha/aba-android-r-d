package io.panha.rd_app.feature.sse.di

import io.panha.rd_app.feature.sse.SSEViewModel
import io.panha.rd_app.feature.sse.repository.SseDemoRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val sseModule = module {
    single { SseDemoRepository(get()) }
    viewModel { SSEViewModel(get(), get()) }
}