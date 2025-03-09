package eu.peernetwork.app

import android.app.Application

class PeerApplication : Application(), Peer {
    override fun onCreate() {
        super.onCreate()
        DaggerPeer_Component.builder().peer(this).build().inject(this)
    }
}
