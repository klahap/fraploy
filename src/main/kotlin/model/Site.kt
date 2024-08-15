package io.github.klahap.fraploy.model

import kotlinx.serialization.Serializable


@Serializable
data class Site(
    val name: SiteName,
    val bench: ServerBench,
    val group: ReleaseGroupName,
    val server: ServerName,
) {
    fun toUpdate() = SiteUpdate(
        name = name,
        server = server,
        bench = bench,
    )
}