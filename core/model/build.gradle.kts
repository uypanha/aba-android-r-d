@file:Suppress("UnstableApiUsage")

plugins {
    id("rd_android.android.library")
}

android {
    namespace = "io.panha.core.model"

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
}