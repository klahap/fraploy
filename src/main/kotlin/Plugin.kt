package io.github.klahap.fraploy

import io.github.klahap.fraploy.model.FraployConfig
import io.github.klahap.fraploy.service.FraployService
import kotlinx.coroutines.runBlocking
import org.gradle.api.Project


class Plugin : org.gradle.api.Plugin<Project> {
    override fun apply(project: Project) {
        val configBuilder = project.extensions.create("fraploy", FraployConfig.Builder::class.java)

        project.task("fraployDeploy") { task ->
            task.doLast {
                val config = configBuilder.build()
                runBlocking {
                    FraployService.run(config)
                }
            }
        }
    }
}