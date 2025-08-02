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

fun roundOffsetToNearestBucket(value: Float): Float {
    if (value == 0f) return 0f

    val absoluteValue = Math.abs(value)
    val magnitude = Math.pow(10.0, Math.floor(Math.log10(absoluteValue.toDouble()))).toFloat()
    val firstDigit = (absoluteValue / magnitude).toInt()  // Truncate (floor) instead of rounding

    return firstDigit * magnitude * if (value < 0) -1 else 1
}
