package eu.peernetwork.app

import android.content.Context
import dagger.android.AndroidInjector
import eu.peernetwork.app.module.blog.BlogModule
import eu.peernetwork.app.module.core.CoreModule
import eu.peernetwork.app.module.core.UiModule
import eu.peernetwork.app.module.media.MediaModule
import eu.peernetwork.app.module.social.SocialModule
import eu.peernetwork.app.module.user.UserModule
import eu.peernetwork.app.module.wallet.WalletModule
import eu.peernetwork.app.service.MessagingService
import eu.peernetwork.app.ui.main.Main
import eu.peernetwork.core.ui.component.UiComponentProvider
import javax.inject.Singleton

interface Peer {
    fun getApplicationContext(): Context

    @Singleton
    @dagger.Component(
        dependencies = [Peer::class],
        modules = [
            CoreModule::class,
            MediaModule::class,
            UiModule::class,
            UserModule::class,
            WalletModule::class,
            BlogModule::class,
            SocialModule::class
        ]
    )
    interface Component : Peer, AndroidInjector<PeerApplication>, UiComponentProvider, Main {
        fun inject(service: MessagingService)
    }
}
