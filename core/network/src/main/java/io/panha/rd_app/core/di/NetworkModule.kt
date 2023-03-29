package io.panha.rd_app.core.di

import io.panha.rd_app.core.network.RetrofitClient
import org.koin.dsl.module

val networkModule = module {
    factory { RetrofitClient.getClient() }
}