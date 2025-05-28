package eu.peernetwork.messaging.ui.chat

import android.content.Context
import eu.peernetwork.core.ui.component.UiComponent

interface Chat {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Chat::class ],
        modules = [ ChatModule::class ]
    )
    interface Component : Chat

    class Builder(private val dependency: Chat) : UiComponent.DefaultBuilder<Chat, Component>() {
        override fun build(context: Context): Component {
            return DaggerChat_Component.builder().chat(dependency).build()
        }
    }
}
