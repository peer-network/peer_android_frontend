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

fun Long.offset(time: Long): Long {
    val seconds = this / 1000
    return when {
        seconds >= 3600 -> {
            time * 3600
        }
        seconds >= 60 -> {
            time * 60
        }
        else -> {
            time
        }
    } * 1000
}
