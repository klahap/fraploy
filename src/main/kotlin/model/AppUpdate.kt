package io.github.klahap.fraploy.model

import io.github.klahap.fraploy.util.*
import kotlinx.serialization.Serializable


@Serializable
data class AppUpdate(
    @Serializable(with = AppNameSerializer::class) val app: AppName,
    @Serializable(with = AppSourceSerializer::class) val source: AppSource,
    @Serializable(with = AppReleaseNameSerializer::class) val release: AppReleaseName,
    @Serializable(with = GitCommitSerializer::class) val hash: GitCommit,
)
