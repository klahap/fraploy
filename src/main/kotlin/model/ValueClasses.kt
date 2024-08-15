package io.github.klahap.fraploy.model

import io.github.klahap.fraploy.util.ValueClassSerializer
import kotlinx.serialization.Serializable

interface ValueClass {
    val name: String
}

@JvmInline
@Serializable(with = ServerBench.Serializer::class)
value class ServerBench(override val name: String) : ValueClass {
    override fun toString() = name

    object Serializer : ValueClassSerializer<ServerBench>(ServerBench::class)
}

@JvmInline
@Serializable(with = ReleaseGroupName.Serializer::class)
value class ReleaseGroupName(override val name: String) : ValueClass {
    override fun toString() = name

    object Serializer : ValueClassSerializer<ReleaseGroupName>(ReleaseGroupName::class)
}

@JvmInline
@Serializable(with = AppName.Serializer::class)
value class AppName(override val name: String) : ValueClass {
    override fun toString() = name

    object Serializer : ValueClassSerializer<AppName>(AppName::class)
}

@JvmInline
@Serializable(with = AppSource.Serializer::class)
value class AppSource(override val name: String) : ValueClass {
    override fun toString() = name

    object Serializer : ValueClassSerializer<AppSource>(AppSource::class)
}

@JvmInline
@Serializable(with = AppReleaseName.Serializer::class)
value class AppReleaseName(override val name: String) : ValueClass {
    override fun toString() = name

    object Serializer : ValueClassSerializer<AppReleaseName>(AppReleaseName::class)
}

@JvmInline
@Serializable(with = GitCommit.Serializer::class)
value class GitCommit(override val name: String) : ValueClass {
    override fun toString() = name
    fun toVersion() = GitVersion(name)

    object Serializer : ValueClassSerializer<GitCommit>(GitCommit::class)
}

@JvmInline
@Serializable(with = GitTag.Serializer::class)
value class GitTag(override val name: String) : ValueClass {
    override fun toString() = name
    fun toVersion() = GitVersion(name)

    object Serializer : ValueClassSerializer<GitTag>(GitTag::class)
}


@JvmInline
@Serializable(with = GitVersion.Serializer::class)
value class GitVersion(override val name: String) : ValueClass {
    override fun toString() = name

    object Serializer : ValueClassSerializer<GitVersion>(GitVersion::class)
}

@JvmInline
@Serializable(with = SiteName.Serializer::class)
value class SiteName(override val name: String) : ValueClass {
    override fun toString() = name

    object Serializer : ValueClassSerializer<SiteName>(SiteName::class)
}

@JvmInline
@Serializable(with = ServerName.Serializer::class)
value class ServerName(override val name: String) : ValueClass {
    override fun toString() = name

    object Serializer : ValueClassSerializer<ServerName>(ServerName::class)
}

@JvmInline
@Serializable(with = DeployName.Serializer::class)
value class DeployName(override val name: String) : ValueClass {
    override fun toString() = name

    object Serializer : ValueClassSerializer<DeployName>(DeployName::class)
}
