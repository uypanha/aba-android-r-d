package io.panha.core.common.di

import io.panha.core.common.StringDecoder
import io.panha.core.common.UriDecoder
import org.koin.dsl.module

var commonModule = module {
    single<StringDecoder> { UriDecoder() }
}