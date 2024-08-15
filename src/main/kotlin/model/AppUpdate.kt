package io.github.klahap.fraploy.model

import kotlinx.serialization.Serializable


@Serializable
data class AppUpdate(
    val app: AppName,
    val source: AppSource,
    val release: AppReleaseName,
    val hash: GitCommit,
)
