package eu.peernetwork.social.ui.member

import android.content.Context
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.social.ui.provider.RendererProvider
import eu.peernetwork.social.ui.provider.SocialProvider

interface Member : SocialProvider, RendererProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Member::class ],
    )
    interface Component : Member

    class Builder(private val dependency: Member) : UiComponent.DefaultBuilder<Member, Component>() {
        override fun build(context: Context): Component {
            return DaggerMember_Component.builder().member(dependency).build()
        }
    }
}
