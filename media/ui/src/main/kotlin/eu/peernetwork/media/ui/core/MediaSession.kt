package eu.peernetwork.media.ui.core

import android.media.MediaPlayer
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MediaSession {
    val observer: StateFlow<Map<String, Float>>

    fun mute(): Flow<Boolean>

    suspend fun mute(enable: Boolean)

    fun exoPlayer(): ExoPlayer

    fun audioPlayer(): MediaPlayer
}
