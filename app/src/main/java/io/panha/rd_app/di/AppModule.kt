package io.panha.rd_app.di

import io.panha.rd_app.viewmodels.HomeViewModel
import io.panha.rd_app.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { HomeViewModel() }
}