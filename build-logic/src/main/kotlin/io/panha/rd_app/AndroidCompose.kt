package io.panha.rd_app

import Dependencies
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import java.io.File

/**
 * Configure Compose-specific options
 */
@Suppress("UnstableApiUsage")
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *>,
) {
//    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = "1.4.2"
        }

        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + buildComposeMetricsParameters()
        }

        dependencies {
//            val bom = libs.findLibrary("androidx-compose-bom").get()
//            add("implementation", platform(bom))
//            add("androidTestImplementation", platform(bom))

            add("implementation", Dependencies.Compose.Material.material)
            add("implementation", Dependencies.Compose.Material.windowSizeClass)
            add("implementation", Dependencies.Compose.activity)
            add("implementation", Dependencies.Compose.ui)
            add("implementation", Dependencies.Compose.toolingPreview)
            add("implementation", Dependencies.Compose.tooling)
            add("implementation", Dependencies.Compose.lifecycleCompose)
            add("implementation", Dependencies.Compose.runTimeLiveData)
            add("implementation", Dependencies.AndroidX.navigationCompose)
        }
    }
}

private fun Project.buildComposeMetricsParameters(): List<String> {
    val metricParameters = mutableListOf<String>()
    val enableMetricsProvider = project.providers.gradleProperty("enableComposeCompilerMetrics")
    val enableMetrics = (enableMetricsProvider.orNull == "true")
    if (enableMetrics) {
        val metricsFolder = File(project.buildDir, "compose-metrics")
        metricParameters.add("-P")
        metricParameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + metricsFolder.absolutePath
        )
    }

    val enableReportsProvider = project.providers.gradleProperty("enableComposeCompilerReports")
    val enableReports = (enableReportsProvider.orNull == "true")
    if (enableReports) {
        val reportsFolder = File(project.buildDir, "compose-reports")
        metricParameters.add("-P")
        metricParameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" + reportsFolder.absolutePath
        )
    }
    return metricParameters.toList()
}
