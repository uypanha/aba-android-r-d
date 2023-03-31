@file:Suppress("UnstableApiUsage")

plugins {
    id("rd_android.android.feature")
    id("rd_android.android.library.compose")
}

android {
    namespace = "io.panha.rd_app.feature.ml_kits"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    @Suppress("UnstableApiUsage")
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        aidl = true
    }
}

dependencies {
    implementation(project(":open-cv"))

    // Navigation
    implementation(Dependencies.Google.Accompanist.navigationAnimation)
    implementation(Dependencies.Compose.Material.iconExtended)

    implementation(Dependencies.Koin.koinAndroid)
    implementation(Dependencies.Koin.compose)

    implementation(Dependencies.Google.MLKit.segmentationSelfie)

    // CameraX
    implementation(Dependencies.AndroidX.Camera.camera)
    implementation(Dependencies.AndroidX.Camera.lifecycle)
    implementation(Dependencies.AndroidX.Camera.cameraView)
    implementation(Dependencies.AndroidX.Camera.extensions)

    implementation(Dependencies.Lottie.compose)
    implementation(Dependencies.Google.Accompanist.permission)
    implementation("com.google.guava:guava:27.1-android")
}