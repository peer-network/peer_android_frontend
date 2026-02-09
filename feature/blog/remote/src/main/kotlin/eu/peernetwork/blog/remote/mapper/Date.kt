package eu.peernetwork.blog.remote.mapper

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun String.toTimestamp(pattern: String = "yyyy-MM-dd HH:mm:ss.SSSSSS"): Long {
    return (SimpleDateFormat(pattern, Locale.getDefault()).parse(this)?.time ?: 0L).run {
        this + TimeZone.getDefault().getOffset(this)
    }
}
