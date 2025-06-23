package eu.peernetwork.media.ui.view

import android.view.TextureView

class TextureViewWrapper(val view: TextureView) {
    private var callback: TextureView.SurfaceTextureListener? = null

    fun attachCallback(callback: TextureView.SurfaceTextureListener) {
        view.surfaceTextureListener = callback
        this.callback = callback
    }

    fun clearCallback() {
        callback?.let {
            view.surfaceTextureListener = null
            callback = null
        }
    }
}
