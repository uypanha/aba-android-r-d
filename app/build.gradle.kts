@file:Suppress("UnstableApiUsage")

plugins {
    id("rd_android.android.application")
}

android {
    namespace = "io.panha.rd_app"

    defaultConfig {
        applicationId = "io.panha.rd_app"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.2"
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:data"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:model"))
    implementation(project(":core:datastore"))
    implementation(project(":feature:topic"))
    implementation(project(":feature:sse"))
    implementation(project(":feature:markdown"))
    implementation(project(":feature:mini-app"))
    implementation(project(":feature:ml-kits"))

    implementation(Dependencies.Material.material)

    // AndroidX
    implementation(Dependencies.AndroidX.coreKtx)
    implementation(Dependencies.AndroidX.tracing)

    // Navigation
    implementation(Dependencies.AndroidX.navigationCompose)
    implementation(Dependencies.Google.Accompanist.navigationAnimation)
    implementation(Dependencies.Google.Accompanist.systemuicontroller)

    // Compose
    implementation(Dependencies.Compose.Material.material)
    implementation(Dependencies.Compose.Material.windowSizeClass)
    implementation(Dependencies.Compose.activity)
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.toolingPreview)
    implementation(Dependencies.Compose.tooling)
    implementation(Dependencies.Compose.uiTestManifest)
    implementation(Dependencies.Compose.runTimeLiveData)
    implementation(Dependencies.Compose.lifecycleCompose)

    // Coroutines
    implementation(Dependencies.Coroutines.kotlinXAndroid)
    implementation(Dependencies.LifeCycle.runTime)

    // Koin
    implementation(Dependencies.Koin.koinAndroid)
    implementation(Dependencies.Koin.compose)
}