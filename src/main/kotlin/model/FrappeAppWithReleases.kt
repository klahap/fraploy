package io.github.klahap.fraploy.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class FrappeAppWithReleases(
    val app: AppName,
    val source: AppSource,
    @SerialName("current_hash") val currentHash: GitCommit,
    @SerialName("current_tag") val currentTag: GitTag?,
    @SerialName("current_release") val currentRelease: AppReleaseName,
    val releases: List<Release>,
) {
    val currentVersions get() = setOfNotNull(currentHash.toVersion(), currentTag?.toVersion())

    @Serializable
    data class Release(
        val name: AppReleaseName,
        val hash: GitCommit,
        val tag: GitTag?,
    ) {
        val versions get() = setOfNotNull(hash.toVersion(), tag?.toVersion())
    }
}
