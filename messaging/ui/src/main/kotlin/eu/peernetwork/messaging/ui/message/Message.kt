package eu.peernetwork.messaging.ui.message

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.messaging.domain.provider.MessagingProvider

interface Message : MessagingProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Message::class ],
        modules = [ MessageModule::class ]
    )
    interface Component : Message {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Message) : UiComponent.DefaultBuilder<Message, Component>() {
        override fun build(context: Context): Component {
            return DaggerMessage_Component.builder().message(dependency).build()
        }
    }
}