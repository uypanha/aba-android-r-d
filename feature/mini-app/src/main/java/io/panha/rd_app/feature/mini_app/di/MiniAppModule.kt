package io.panha.rd_app.feature.mini_app.di

import io.panha.rd_app.feature.mini_app.MiniAppViewModel
import org.koin.dsl.module

val miniAppModule = module {
    factory { MiniAppViewModel(get()) }
}