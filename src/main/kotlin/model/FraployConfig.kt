package io.github.klahap.fraploy.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


data class FraployConfig(
    val credentials: Credentials,
    val source: Source,
    val blocking: Blocking,
) {
    open class Builder(
        private var credentials: Credentials? = null,
        private var source: Source? = null,
        private var blocking: Blocking = Blocking.Builder().build(),
    ) {
        fun build() = FraployConfig(
            credentials = credentials ?: throw Exception("no fraploy->credentials defined"),
            source = source ?: throw Exception("no fraploy->source defined"),
            blocking = blocking,
        )

        fun credentials(block: Credentials.Builder.() -> Unit) {
            credentials = Credentials.Builder().apply(block).build()
        }

        fun source(block: Source.Builder.() -> Unit) {
            source = Source.Builder().apply(block).build()
        }

        fun blocking(block: Blocking.Builder.() -> Unit) {
            blocking = Blocking.Builder().apply(block).build()
        }

        companion object {
            private fun Credentials.Builder.build() = Credentials(
                token = token ?: throw Exception("no fraploy->credentials->token defined"),
                team = team ?: throw Exception("no fraploy->credentials->team defined"),
            )

            private fun Source.Builder.build() = Source(
                releaseGroupTitle = releaseGroupTitle
                    ?: throw Exception("no fraploy->source->releaseGroupTitle defined"),
                updateRequests = updateRequests.toMap().takeIf { it.isNotEmpty() }
                    ?: throw Exception("no fraploy->source->addAppUpdate defined"),
            )

            private fun Blocking.Builder.build() = Blocking(
                enable = enable,
                pollDelay = pollDelay
            )
        }
    }

    data class Credentials(
        val token: String,
        val team: String,
    ) {
        data class Builder(
            var token: String? = null,
            var team: String? = null,
        )
    }

    data class Source(
        val releaseGroupTitle: String,
        val updateRequests: Map<AppName, GitVersion>,
    ) {
        data class Builder(
            var releaseGroupTitle: String? = null,
            val updateRequests: MutableMap<AppName, GitVersion> = mutableMapOf(),
        ) {
            fun addAppUpdate(appName: String, version: String) {
                updateRequests[AppName(appName)] = GitVersion((version))
            }
        }
    }

    data class Blocking(
        val enable: Boolean,
        val pollDelay: Duration,
    ) {
        data class Builder(
            var enable: Boolean = false,
            var pollDelay: Duration = 5.seconds,
        )
    }
}
