package io.github.klahap.fraploy

import io.github.klahap.fraploy.model.FraployConfig
import io.github.klahap.fraploy.service.FraployService
import kotlin.time.Duration.Companion.seconds


suspend fun main() {
    val config = FraployConfig.Builder().apply {
        credentials {
            token = System.getenv("FRAPPE_CLOUD_TOKEN")
            team = System.getenv("FRAPPE_CLOUD_TEAM")
        }
        source {
            releaseGroupTitle = System.getenv("FRAPPE_CLOUD_RELEASE_GROUP_TITLE")
            addAppUpdate(appName = "frappe", version = "v15.37.0")
        }
        blocking {
            pollDelay = 5.seconds
        }
    }.build()
    FraployService.run(config)
}
