@file:Suppress("UnstableApiUsage")

plugins {
    id("rd_android.android.library")
}

android {
    namespace = "io.panha.rd_app.core.network"

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
    // define a BOM
    implementation(platform(Dependencies.Squareup.bom))

    // Okhttp
    implementation(Dependencies.Squareup.okhttp)
    implementation(Dependencies.Squareup.retrofit)
    implementation(Dependencies.Squareup.converter)
    implementation(Dependencies.Squareup.loggingInterceptor)

    // Koin
    implementation(Dependencies.Koin.koinAndroid)
}