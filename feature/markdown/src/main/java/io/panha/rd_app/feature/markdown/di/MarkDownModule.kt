package io.panha.rd_app.feature.markdown.di

import io.panha.rd_app.feature.markdown.MarkDownViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

var markDownModule = module {
    viewModelOf(::MarkDownViewModel)
}