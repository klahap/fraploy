package io.github.klahap.fraploy.service

import io.github.klahap.fraploy.model.*
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient


class FraployService(
    client: OkHttpClient = OkHttpClient(),
    credentials: FraployConfig.Credentials,
) {
    private val frappeCloudService = FrappeCloudService(client = client, credentials = credentials)

    suspend fun update(source: FraployConfig.Source): DeployName? {
        val releaseGroupName = frappeCloudService.getReleaseGroupName(source.releaseGroupTitle)
        val apps = frappeCloudService.getReleaseGroup(releaseGroupName)
        val sites = frappeCloudService.getSites(releaseGroupName)

        val appUpdates = apps.getUpdates(source.updateRequests)
        val siteUpdates = sites.map { it.toUpdate() }.toSet()

        if (appUpdates.isEmpty()) {
            println("all apps are already up-to-date. No deployment necessary.")
            return null
        } else {
            println("App updates:")
            val maxAppNameLength = appUpdates.maxOf { it.app.name.length }
            appUpdates.forEach { println("${it.app.name.padStart(maxAppNameLength + 4)} -> ${it.hash}") }
            println()
        }

        println("Sites at $releaseGroupName (${source.releaseGroupTitle}):")
        if (siteUpdates.isEmpty())
            println("<no-sites>")
        else
            siteUpdates.forEach { println("    ${it.name}") }
        println()

        val deployName = frappeCloudService.deployAndUpdate(
            releaseGroupName = releaseGroupName,
            apps = appUpdates,
            sites = siteUpdates,
        )
        println("deployment started: $deployName")
        return deployName
    }

    suspend fun blocking(deployName: DeployName, blockingConfig: FraployConfig.Blocking) {
        if (!blockingConfig.enable) return
        val marker = TimeMarkService()
        while (true) {
            val deployment = frappeCloudService.getDeployCandidate(deployName)
            if (deployment.isSuccess) {
                println("$marker deployment finished successfully")
                return
            } else if (deployment.isFailure) {
                println("$marker deployment finished with error")
                throw ErrorDuringDeploymentException()
            } else {
                println("$marker ...")
            }
            delay(blockingConfig.pollDelay)
        }
    }

    companion object {
        fun Collection<FrappeAppWithReleases>.getUpdates(updateRequests: Map<AppName, GitVersion>): Set<AppUpdate> {
            val appMap = associateBy { it.app }
            return updateRequests.mapNotNull { (name, version) ->
                val app = appMap[name] ?: throw FrappeAppNotExistsException(name)
                val hasAlreadyVersion = app.currentVersions.contains(version)
                if (hasAlreadyVersion) return@mapNotNull null
                val release = app.releases.firstOrNull { it.versions.contains(version) }
                    ?: throw FrappeAppReleaseNotExistsException(name, version) // TODO create release if not exists
                AppUpdate(
                    app = name,
                    source = app.source,
                    release = release.name,
                    hash = release.hash,
                )
            }.toSet()
        }

        suspend fun run(config: FraployConfig) {
            val service = FraployService(credentials = config.credentials)
            val deployName = service.update(source = config.source) ?: return
            service.blocking(deployName = deployName, blockingConfig = config.blocking)
        }
    }
}
