package eu.peernetwork.app

import android.content.Context
import dagger.android.AndroidInjector
import eu.peernetwork.app.module.core.CoreModule
import eu.peernetwork.app.module.user.UserModule
import javax.inject.Singleton

interface Peer {
    fun getApplicationContext(): Context

    @Singleton
    @dagger.Component(
        dependencies = [Peer::class],
        modules = [
            CoreModule::class,
            UserModule::class,
        ]
    )
    interface Component : Peer, AndroidInjector<PeerApplication>
}
