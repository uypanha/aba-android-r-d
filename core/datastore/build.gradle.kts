@file:Suppress("UnstableApiUsage")

plugins {
    id("rd_android.android.library")
}

android {
    namespace = "io.panha.rd_app.core.datastore"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation(Dependencies.Coroutines.kotlinXAndroid)
    implementation(Dependencies.AndroidX.DataStore.preferences)
    implementation(Dependencies.AndroidX.DataStore.core)

    implementation(Dependencies.Koin.koinAndroid)
}