package io.github.klahap.fraploy.model

import kotlinx.serialization.Serializable


@Serializable
data class DeployCandidate(
    val name: String,
    val status: String,
    val jobs: List<Job>,
) {
    val isSuccess get() = status == "Success" && jobs.isNotEmpty() && jobs.all { it.isSuccess }
    val isFailure get() = status == "Failure" || jobs.any { it.isFailure }

    @Serializable
    data class Job(
        val name: String,
        val status: String,
    ) {
        val isSuccess get() = status == "Success"
        val isFailure get() = status == "Failure" || status == "Delivery Failure"
    }
}
