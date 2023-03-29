package io.panha.rd_app.core

class APIConfig private constructor(var domainUrl: String, var protocol: String) {

    companion object {

        private lateinit var _instance: APIConfig
        val instance: APIConfig
            get() {
                if (this::_instance.isInitialized) {
                    assert(false) { "APIConfig hasn't been configured. Call APIConfig,config() before calling its instance." }
                }
                return _instance
            }

        fun config(builder: Builder) {
            _instance = getInstance(builder)
        }

        fun getInstance(builder: Builder): APIConfig {
            return builder.build()
        }
    }

    val baseUrl: String
        get() {
            return "${this.protocol}://${this.domainUrl}"
        }

    data class Builder(
        internal var domainUrl: String = "",
        internal var protocol: String = "https"
    ) {

        fun domainUrl(domain: String) = apply { this.domainUrl = domain }

        fun protocol(protocol: String) = apply { this.protocol = protocol }

        fun build(): APIConfig {
            return APIConfig(
                this.domainUrl,
                this.protocol
            )
        }
    }
}