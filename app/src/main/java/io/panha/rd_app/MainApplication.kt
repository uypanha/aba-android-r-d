package io.panha.rd_app

import android.app.Application
import io.panha.core.common.di.commonModule
import io.panha.core.data.di.dataModule
import io.panha.rd_app.core.datastore.di.dataStoreModule
import io.panha.rd_app.core.di.networkModule
import io.panha.rd_app.di.appModule
import io.panha.rd_app.feature.markdown.di.markDownModule
import io.panha.rd_app.feature.mini_app.di.miniAppModule
import io.panha.rd_app.feature.ml_kits.di.mlKitsModule
import io.panha.rd_app.feature.sse.di.sseModule
import io.panha.rd_app.feature.topic.di.topicModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Dependencies Injection: Koin
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                commonModule,
                networkModule,
                dataModule,
                dataStoreModule,
                topicModule,
                sseModule,
                markDownModule,
                mlKitsModule,
                miniAppModule,
                appModule
            )
        }
    }
}