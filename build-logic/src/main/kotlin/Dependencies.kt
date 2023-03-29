object Versions {

    const val compileVersion: Int = 33
    const val minSdk: Int = 24
}

object Dependencies {

    object Kotlin {
        const val version = "1.7.0"
    }

    object JUnit {
        const val junit = "junit:junit:4.13.2"
    }

    object Material {
        private const val version = "1.5.0"

        const val material = "com.google.android.material:material:$version"
    }

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:${Kotlin.version}"
        const val appCompat = "androidx.appcompat:appcompat:1.4.1"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.3"

        const val navigationCompose = "androidx.navigation:navigation-compose:2.5.3"

        const val testExt = "androidx.test.ext:junit:1.1.5"
        const val espressoCore = "androidx.test.espresso:espresso-core:3.5.1"

        const val tracing = "androidx.tracing:tracing-ktx:1.1.0"

        const val webKit = "androidx.webkit:webkit:1.6.0"

        object DataStore {
            const val preferences = "androidx.datastore:datastore-preferences:1.0.0"
            const val core = "androidx.datastore:datastore-preferences-core:1.0.0"
        }

        object Camera {
            private const val cameraXVersion = "1.2.2"

            const val camera = "androidx.camera:camera-camera2:$cameraXVersion"
            const val lifecycle = "androidx.camera:camera-lifecycle:$cameraXVersion"
            const val cameraView = "androidx.camera:camera-view:$cameraXVersion"
            const val extensions = "androidx.camera:camera-extensions:$cameraXVersion"
        }
    }

    object Squareup {
        private const val version = "5.0.0-alpha.11"
        private const val retrofitVersion = "2.3.0"

        const val bom = "com.squareup.okhttp3:okhttp-bom:$version"

        const val okhttp = "com.squareup.okhttp3:okhttp"
        const val okhttpSSE = "com.squareup.okhttp3:okhttp-sse"

        const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
        const val converter = "com.squareup.retrofit2:converter-gson:$retrofitVersion"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object Coroutines {
        private const val version = "1.6.0"

        const val kotlinXAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    }

    object LifeCycle {
        private const val version = "2.4.1"

        const val runTime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:2.5.1"
    }

    object Compose {
        private const val version = "1.3.3"
        const val bom = "androidx.compose:compose-bom:2023.01.00"

        const val ui = "androidx.compose.ui:ui:$version"
        const val toolingPreview = "androidx.compose.ui:ui-tooling-preview:$version"
        const val tooling = "androidx.compose.ui:ui-tooling:$version"
        const val uiTest = "androidx.compose.ui:ui-test-junit4:$version"
        const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:$version"

        const val activity = "androidx.activity:activity-compose:1.6.1"

        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1"
        const val runTimeLiveData = "androidx.compose.runtime:runtime-livedata:1.3.3"
        const val lifecycleCompose = "androidx.lifecycle:lifecycle-runtime-compose:2.6.0-rc01"

        object Material {
            private const val iconVersion = "1.4.0-beta01"

            const val material = "androidx.compose.material3:material3:1.1.0-alpha08"
            const val windowSizeClass = "androidx.compose.material3:material3-window-size-class:1.0.1"
            const val iconCore = "androidx.compose.material:material-icons-core:1.4.0-beta01"
            const val iconExtended = "androidx.compose.material:material-icons-extended:$iconVersion"
        }
    }

    object Lottie {

        const val compose = "com.airbnb.android:lottie-compose:6.0.0"
    }

    object Koin {
        private const val version = "3.3.3"

        const val koinAndroid = "io.insert-koin:koin-android:$version"
        const val compose = "io.insert-koin:koin-androidx-compose:3.4.2"
    }

    object Google {

        object Accompanist {
            private const val version = "0.30.0"

            const val navigationAnimation = "com.google.accompanist:accompanist-navigation-animation:$version"
            const val systemuicontroller = "com.google.accompanist:accompanist-systemuicontroller:$version"
            const val permission = "com.google.accompanist:accompanist-permissions:$version"
        }

        object MLKit {
            const val segmentationSelfie = "com.google.mlkit:segmentation-selfie:16.0.0-beta4"
        }
    }

    object OtherTools {
        const val markdown = "com.halilibo.compose-richtext:richtext-commonmark:0.16.0"
    }
}