plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    compileOnly("com.android.tools.build:gradle:7.4.2")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
}

gradlePlugin {
    plugins {
//        register("androidApplicationCompose") {
//            id = "nowinandroid.android.application.compose"
//            implementationClass = "AndroidApplicationComposeConventionPlugin"
//        }
        register("androidApplication") {
            id = "rd_android.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
//        register("androidApplicationJacoco") {
//            id = "rd-android.android.application.jacoco"
//            implementationClass = "AndroidApplicationJacocoConventionPlugin"
//        }
        register("androidLibraryCompose") {
            id = "rd_android.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "rd_android.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = "rd_android.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
//        register("androidLibraryJacoco") {
//            id = "nowinandroid.android.library.jacoco"
//            implementationClass = "AndroidLibraryJacocoConventionPlugin"
//        }
//        register("androidTest") {
//            id = "nowinandroid.android.test"
//            implementationClass = "AndroidTestConventionPlugin"
//        }
//        register("androidHilt") {
//            id = "nowinandroid.android.hilt"
//            implementationClass = "AndroidHiltConventionPlugin"
//        }
//        register("firebase-perf") {
//            id = "nowinandroid.firebase-perf"
//            implementationClass = "FirebasePerfConventionPlugin"
//        }
    }
}

repositories {
    mavenCentral()
    google()
}