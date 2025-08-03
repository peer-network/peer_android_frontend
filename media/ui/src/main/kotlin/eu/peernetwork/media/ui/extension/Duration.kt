package eu.peernetwork.media.ui.extension

import kotlin.math.roundToLong

fun Long.format(): Long {
    val seconds = this / 1000.0
    return when {
        seconds >= 3600 -> {
            seconds / 3600
        }
        seconds >= 60 -> {
            seconds / 60
        }
        else -> {
            seconds
        }
    }.roundToLong()
}
