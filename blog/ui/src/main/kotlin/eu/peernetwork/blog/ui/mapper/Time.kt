package eu.peernetwork.blog.ui.mapper

import android.content.Context
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.model.UiTimer
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

fun Long.mapFromDomain(): UiTimer {
    val current = System.currentTimeMillis()
    val diff = current - this
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    return when {
        minutes < 1 -> UiTimer.Now
        minutes < 60 -> UiTimer.Minutes(minutes)
        hours < 24 -> UiTimer.Hours(hours)
        days == 1L -> UiTimer.Yesterday
        days < 7 -> UiTimer.Days(days)
        else -> {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = this
            val date = String.format(Locale.getDefault(), "%1\$tb %1\$td, %1\$tY", calendar)
            UiTimer.Date(date)
        }
    }
}

fun Context.format(timer: UiTimer): String = when (timer) {
    is UiTimer.Now -> getString(R.string.now_label)
    is UiTimer.Minutes -> getString(R.string.minute_label, timer.value)
    is UiTimer.Hours -> getString(R.string.hour_label, timer.value)
    is UiTimer.Yesterday -> getString(R.string.day_label)
    is UiTimer.Days -> getString(R.string.days_label, timer.value)
    is UiTimer.Date -> timer.value
}
