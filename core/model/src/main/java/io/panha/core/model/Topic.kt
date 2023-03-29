package io.panha.core.model

enum class Topic {
    SSE,
    MINI_APP_ASSET_LOADER,
    ML_KITS;

    val title: String
        get() {
            return when (this) {
                SSE -> "Server Sent Event"
                MINI_APP_ASSET_LOADER -> "Mini App (Using Asset Loader)"
                ML_KITS -> "ML Kits"
            }
        }
}