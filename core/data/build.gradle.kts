@file:Suppress("UnstableApiUsage")

plugins {
    id("rd_android.android.library")
}

android {
    namespace = "io.panha.core.data"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:datastore"))

    implementation(Dependencies.AndroidX.appCompat)

    // Coroutines
    implementation(Dependencies.Coroutines.kotlinXAndroid)
    implementation(Dependencies.LifeCycle.runTime)
    implementation(Dependencies.AndroidX.DataStore.core)

    // Koin
    implementation(Dependencies.Koin.koinAndroid)
}