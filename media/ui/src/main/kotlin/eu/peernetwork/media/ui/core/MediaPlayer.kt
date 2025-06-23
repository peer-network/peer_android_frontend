package eu.peernetwork.media.ui.core

import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MediaPlayer {
    val observer: StateFlow<Map<String, Float>>

    fun mute(): Flow<Boolean>

    fun expandedMode(): Boolean

    fun player(): ExoPlayer
}
