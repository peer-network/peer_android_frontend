package eu.peernetwork.social.ui.search.member

import android.content.Context
import eu.peernetwork.core.ui.component.UiComponent

interface Member {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Member::class ],
        modules = [ MemberModule::class ]
    )
    interface Component : Member

    class Builder(private val dependency: Member) : UiComponent.DefaultBuilder<Member, Component>() {
        override fun build(context: Context): Component {
            return DaggerMember_Component.builder().member(dependency).build()
        }
    }
}
