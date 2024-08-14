package io.github.klahap.fraploy.model

import io.github.klahap.fraploy.util.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class FrappeAppWithReleases(
    @Serializable(with = AppNameSerializer::class) val app: AppName,
    @Serializable(with = AppSourceSerializer::class) val source: AppSource,
    @SerialName("current_hash") @Serializable(with = GitCommitSerializer::class) val currentHash: GitCommit,
    @SerialName("current_tag") @Serializable(with = GitTagSerializer::class) val currentTag: GitTag?,
    @SerialName("current_release") @Serializable(with = AppReleaseNameSerializer::class) val currentRelease: AppReleaseName,
    val releases: List<Release>,
) {
    val currentVersions get() = setOfNotNull(currentHash.toVersion(), currentTag?.toVersion())

    @Serializable
    data class Release(
        @Serializable(with = AppReleaseNameSerializer::class) val name: AppReleaseName,
        @Serializable(with = GitCommitSerializer::class) val hash: GitCommit,
        @Serializable(with = GitTagSerializer::class) val tag: GitTag?,
    ) {
        val versions get() = setOfNotNull(hash.toVersion(), tag?.toVersion())
    }
}
