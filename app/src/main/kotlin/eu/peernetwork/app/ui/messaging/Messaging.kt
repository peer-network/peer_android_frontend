package eu.peernetwork.app.ui.messaging

import android.content.Context
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.messaging.ui.chat.Chat
import eu.peernetwork.messaging.ui.message.Message

interface Messaging : ApplicationProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies =  [ Messaging::class ],
        modules = [ MessagingModule::class ]
    )
    interface Component : Messaging, Chat, Message, UiComponentProvider

    class Builder(private val dependency: Messaging) : UiComponent.DefaultBuilder<Messaging, Component>() {
        override fun build(context: Context): Component {
            return DaggerMessaging_Component.builder().messaging(dependency).build()
        }
    }
}