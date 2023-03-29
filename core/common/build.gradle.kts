plugins {
    id("rd_android.android.library")
}

android {
    namespace = "io.panha.core.common"

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
    // Coroutines
    implementation(Dependencies.Coroutines.kotlinXAndroid)
    implementation(Dependencies.LifeCycle.runTime)

    // Koin
    implementation(Dependencies.Koin.koinAndroid)
}