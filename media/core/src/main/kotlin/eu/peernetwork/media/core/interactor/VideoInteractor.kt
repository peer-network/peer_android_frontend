package eu.peernetwork.media.core.interactor

import android.graphics.SurfaceTexture

interface VideoInteractor {
    fun save()

    suspend fun mute(enable: Boolean)

    fun attach(texture: SurfaceTexture, url: String)

    fun restore()

    fun detach(texture: SurfaceTexture)

    fun dispose()
}
