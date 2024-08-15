package io.github.klahap.fraploy.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SiteUpdate(
    val name: SiteName,
    val server: ServerName,
    @SerialName("skip_failing_patches") val skipFailingPatches: Boolean = false,
    @SerialName("skip_backups") val skipBackups: Boolean = false,
    val bench: ServerBench,
)
