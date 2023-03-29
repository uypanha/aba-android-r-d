package io.panha.core.data.di

import io.panha.core.data.repository.OfflineFirstUserDataRepository
import io.panha.core.data.repository.TopicsRepository
import io.panha.core.data.repository.UserDataRepository
import io.panha.core.data.repository.fake.TopicsRepositoryImpl
import io.panha.core.data.util.ConnectivityManagerNetworkMonitor
import io.panha.core.data.util.NetworkMonitor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<TopicsRepository> { TopicsRepositoryImpl() }
    single<UserDataRepository> { OfflineFirstUserDataRepository(get()) }
    single<NetworkMonitor> { ConnectivityManagerNetworkMonitor(androidContext()) }
}