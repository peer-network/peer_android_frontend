package eu.peernetwork.social.ui.member

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.social.ui.followers.Followers
import eu.peernetwork.social.ui.followings.Followings
import eu.peernetwork.social.ui.peers.Peers
import eu.peernetwork.social.ui.provider.RendererProvider
import eu.peernetwork.social.ui.provider.SocialProvider

interface Member : SocialProvider, RendererProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Member::class ],
        modules = [MemberModule::class]
    )
    interface Component : Member, Followers, UiComponentProvider, Followings, Peers {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Member) : UiComponent.DefaultBuilder<Member, Component>() {
        override fun build(context: Context): Component {
            return DaggerMember_Component.builder().member(dependency).build()
        }
    }
}
