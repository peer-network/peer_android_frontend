package eu.peernetwork.media.ui.interactor

import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MediaInteractor {
    val observer: StateFlow<Map<String, Float>>

    fun mute(): Flow<Boolean>

    suspend fun mute(enable: Boolean)

    fun exoPlayer(): ExoPlayer
}