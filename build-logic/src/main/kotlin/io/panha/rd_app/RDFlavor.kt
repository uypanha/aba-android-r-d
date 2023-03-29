package io.panha.rd_app

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor
import org.gradle.api.Project


@Suppress("EnumEntryName")
enum class FlavorDimension {
    contentType
}

// The content for the app can either come from local static data which is useful for demo
// purposes, or from a production backend server which supplies up-to-date, real content.
// These two product flavors reflect this behaviour.
@Suppress("EnumEntryName")
enum class RDFlavor(val dimension: FlavorDimension, val applicationIdSuffix: String? = null) {
    demo(FlavorDimension.contentType),
    prod(FlavorDimension.contentType, ".prod")
}

@Suppress("UnstableApiUsage")
fun Project.configureFlavors(
    commonExtension: CommonExtension<*, *, *, *>,
    flavorConfigurationBlock: ProductFlavor.(flavor: RDFlavor) -> Unit = {}
) {
//    commonExtension.apply {
//        flavorDimensions += FlavorDimension.contentType.name
//        productFlavors {
//            RDFlavor.values().forEach {
//                create(it.name) {
//                    dimension = it.dimension.name
//                    flavorConfigurationBlock(this, it)
//                    if (this@apply is ApplicationExtension && this is ApplicationProductFlavor) {
//                        if (it.applicationIdSuffix != null) {
//                            this.applicationIdSuffix = it.applicationIdSuffix
//                        }
//                    }
//                }
//            }
//        }
//    }
}
