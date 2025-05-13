package eu.peernetwork.social.ui.peers

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.social.ui.followings.DaggerFollowings_Component
import eu.peernetwork.social.ui.followings.FollowingsModule
import eu.peernetwork.social.ui.provider.SocialProvider

interface Peers: SocialProvider{
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Peers::class],
        modules = [PeersModule::class]
    )
    interface Component : Peers {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Peers) : UiComponent.DefaultBuilder<Peers, Component>() {
        override fun build(context: Context): Component {
            return DaggerPeers_Component.builder().peers(dependency).build()
        }
    }
}