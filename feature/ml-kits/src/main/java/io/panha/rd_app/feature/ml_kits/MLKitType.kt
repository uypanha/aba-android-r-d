package io.panha.rd_app.feature.ml_kits

enum class MLKitType {
    SEGMENTATION_SELFIE;

    val route: String
        get() {
            return when (this) {
                SEGMENTATION_SELFIE -> "ml_kits_segmentation_selfie_route"
            }
        }

    val title: String
        get() {
            return when (this) {
                SEGMENTATION_SELFIE -> "Segmentation Selfie"
            }
        }
}