package eu.peernetwork.blog.ui.mapper

import android.content.Context
import eu.peernetwork.blog.ui.R
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

fun Context.timeAgo(current: Long, time: Long): String {
    val diff = time - current
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    return when {
        minutes < 1 -> getString(R.string.now_label)
        minutes < 60 -> getString(R.string.minute_label, minutes)
        hours < 24 -> getString(R.string.hour_label, hours)
        days == 1L -> getString(R.string.day_label)
        days < 7 -> getString(R.string.days_label, days)
        else -> {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = current
            String.format(Locale.getDefault(), "%1\$tb %1\$td, %1\$tY", calendar)
        }
    }
}
