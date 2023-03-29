plugins {
    id("rd_android.android.feature")
    id("rd_android.android.library.compose")
}

android {
    namespace = "io.panha.rd_app.feature.topic"

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
}

dependencies {
    // Navigation
    implementation(Dependencies.Google.Accompanist.navigationAnimation)
    implementation(Dependencies.Compose.Material.iconExtended)
}