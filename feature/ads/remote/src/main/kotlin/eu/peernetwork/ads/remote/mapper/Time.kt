package eu.peernetwork.ads.remote.mapper

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun Any.mapToTimestamp(pattern: String = "yyyy-MM-dd HH:mm:ss.SSSSSS"): Long {
    return (SimpleDateFormat(pattern, Locale.getDefault())
        .parse(this.toString())?.time ?: 0L).run {
            this + TimeZone.getDefault().getOffset(this)
        }
}
