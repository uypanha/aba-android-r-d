package io.panha.rd_app.feature.ml_kits.di

import io.panha.rd_app.feature.ml_kits.MLKitsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mlKitsModule = module {
    viewModel { MLKitsViewModel() }
}