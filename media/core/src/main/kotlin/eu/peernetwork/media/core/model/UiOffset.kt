package eu.peernetwork.media.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class UiOffset(
    val start: Long,
    val stop: Long
) : Parcelable {
    data object None : UiOffset(0L, 0L)
    data class Value(
        private val _start: Long,
        private val _stop: Long,
    ): UiOffset(_start, _stop)
}
