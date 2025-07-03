package eu.peernetwork.media.core.interactor

import android.graphics.SurfaceTexture

interface VideoInteractor {
    suspend fun mute(enable: Boolean)

    fun attach(texture: SurfaceTexture, url: String)

    fun detach(texture: SurfaceTexture)

    fun dispose()
}
