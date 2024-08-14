package io.github.klahap.fraploy.service

import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource


data class TimeMarkService(
    val t0: TimeSource.Monotonic.ValueTimeMark = TimeSource.Monotonic.markNow(),
    val maxStringLength: Int = 7,
) {
    override fun toString() = (TimeSource.Monotonic.markNow() - t0).inWholeSeconds.seconds.toString()
        .padStart(length = maxStringLength).let { "[$it]" }
}
