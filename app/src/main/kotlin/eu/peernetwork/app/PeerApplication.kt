package eu.peernetwork.app

import android.app.Application
import eu.peernetwork.core.ui.component.UiComponent

class PeerApplication : Application(), Peer, UiComponent.Provider<Peer.Component> {
    override val injector: Peer.Component by lazy {
        DaggerPeer_Component.builder().peer(this).build()
    }

    override fun onCreate() {
        super.onCreate()
        injector.inject(this)
    }
}
