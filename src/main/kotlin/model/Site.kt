package io.github.klahap.fraploy.model

import io.github.klahap.fraploy.util.*
import kotlinx.serialization.Serializable


@Serializable
data class Site(
    @Serializable(with = SiteNameSerializer::class) val name: SiteName,
    @Serializable(with = ServerBenchSerializer::class) val bench: ServerBench,
    @Serializable(with = BenchSerializer::class) val group: ReleaseGroupName,
    @Serializable(with = ServerNameSerializer::class) val server: ServerName,
) {
    fun toUpdate() = SiteUpdate(
        name = name,
        server = server,
        bench = bench,
    )
}