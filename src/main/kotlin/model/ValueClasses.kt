package io.github.klahap.fraploy.model

interface ValueClass {
    val name: String
}

@JvmInline
value class ServerBench(override val name: String) : ValueClass {
    override fun toString() = name
}

@JvmInline
value class ReleaseGroupName(override val name: String) : ValueClass {
    override fun toString() = name
}

@JvmInline
value class AppName(override val name: String) : ValueClass {
    override fun toString() = name
}

@JvmInline
value class AppSource(override val name: String) : ValueClass {
    override fun toString() = name
}

@JvmInline
value class AppReleaseName(override val name: String) : ValueClass {
    override fun toString() = name
}

@JvmInline
value class GitCommit(override val name: String) : ValueClass {
    override fun toString() = name
    fun toVersion() = GitVersion(name)
}

@JvmInline
value class GitTag(override val name: String) : ValueClass {
    override fun toString() = name
    fun toVersion() = GitVersion(name)
}


@JvmInline
value class GitVersion(override val name: String) : ValueClass {
    override fun toString() = name
}

@JvmInline
value class SiteName(override val name: String) : ValueClass {
    override fun toString() = name
}

@JvmInline
value class ServerName(override val name: String) : ValueClass {
    override fun toString() = name
}

@JvmInline
value class DeployName(override val name: String) : ValueClass {
    override fun toString() = name
}
