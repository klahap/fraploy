package io.github.klahap.fraploy.model

import io.github.klahap.fraploy.util.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SiteUpdate(
    @Serializable(with = SiteNameSerializer::class) val name: SiteName,
    @Serializable(with = ServerNameSerializer::class) val server: ServerName,
    @SerialName("skip_failing_patches") val skipFailingPatches: Boolean = false,
    @SerialName("skip_backups") val skipBackups: Boolean = false,
    @Serializable(with = ServerBenchSerializer::class) val bench: ServerBench,
)
