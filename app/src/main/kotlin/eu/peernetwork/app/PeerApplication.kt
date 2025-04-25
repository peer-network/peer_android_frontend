package eu.peernetwork.app

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import eu.peernetwork.core.ui.component.UiComponent

class PeerApplication : Application(), Peer, UiComponent.Provider<Peer.Component> {
    override val injector: Peer.Component by lazy {
        DaggerPeer_Component.builder().peer(this).build()
    }

    override fun onCreate() {
        super.onCreate()
        injector.inject(this)
        FirebaseApp.initializeApp(this)
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null)
    }
}
