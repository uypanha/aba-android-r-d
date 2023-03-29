@file:Suppress("UnstableApiUsage")

plugins {
    id("rd_android.android.library")
    id("rd_android.android.library.compose")
}

android {
    namespace = "io.panha.core.designsystem"

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
    implementation(Dependencies.Compose.Material.iconCore)
    implementation(Dependencies.Compose.Material.iconExtended)

    implementation(Dependencies.AndroidX.appCompat)
    implementation(Dependencies.Lottie.compose)

    // Permission
    implementation(Dependencies.Google.Accompanist.permission)
}