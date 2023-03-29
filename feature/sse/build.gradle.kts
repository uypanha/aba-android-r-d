@file:Suppress("UnstableApiUsage")

plugins {
    id("rd_android.android.feature")
    id("rd_android.android.library.compose")
}

android {
    namespace = "io.panha.rd_app.feature.sse"

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
    implementation(project(":core:network"))

    // define a BOM
    implementation(platform(Dependencies.Squareup.bom))

    // Server Sent Events
    implementation(Dependencies.Squareup.okhttpSSE)
    testImplementation(Dependencies.Squareup.okhttpSSE)

    implementation(Dependencies.Compose.Material.iconExtended)

    implementation(Dependencies.Lottie.compose)
}